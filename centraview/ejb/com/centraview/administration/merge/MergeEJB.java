/*
 * $RCSfile: MergeEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:46 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.centraview.com/opensource/license.html
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Original Code is: CentraView Open Source. 
 * 
 * The developer of the Original Code is CentraView.  Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */


package com.centraview.administration.merge;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;

/**
 * The MergeEJB SessionBean will do all the heavy lifting for
 * the Merge / Purge functionallity.  The methods in here will
 * implement the business logic that populates the SearchCriteriaVO
 * with the right fieldlists.  That in turn, parses and does a search
 * based on the contents of a SearchCriteriaVO.  And returns a MergeSearchResultVO.
 *
 * Finally based on further selections by the user, Entity or Individual Records
 * will be updated, many many related records will be re-associated with the newly updated
 * (merged) record.  And purged records will be deleted through their
 * standard delete use methods.
 *
 * The phonetic algortihm searches (soundex, metaphone) will rely on the Jakarta commons
 * codec library.
 * <http://jakarta.apache.org/commons/codec/>
 * <http://jakarta.apache.org/commons/codec/apidocs/>
 *
 * @author Kevin McAllister <kevin@centraview.com>
 *
 */
public class MergeEJB implements SessionBean
{

  protected SessionContext sessionContext;
  private String dataSource = "MySqlDS";

  /**
   * @see javax.ejb.SessionBean#ejbActivate()
   */
  public void ejbActivate() throws EJBException, RemoteException
  {
  }

  /**
   * The required ejbCreate() method.
   */
  public void ejbCreate() throws CreateException, RemoteException
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbPassivate()
   */
  public void ejbPassivate() throws EJBException, RemoteException
  {
  }

  /**
   * @see javax.ejb.SessionBean#ejbRemove()
   */
  public void ejbRemove() throws EJBException, RemoteException
  {
  }

  /**
   * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
   */
  public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException
  {
    this.sessionContext = arg0;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
   public void setDataSource(String ds) {
    this.dataSource = ds;
   }

  /**
   * The SearchCriteriaVO is built initially in JBoss world, because we will need to stick
   * customfields on the fieldlists to provide more robust field criteria.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return
   */
  public SearchCriteriaVO getSearchCriteriaVO()
  {
    // TODO make sure custom field stuff gets into the SearchCriteria FieldLists.
    return new SearchCriteriaVO();
  }

  /**
   * Where the rubber meets the road in terms of actually finding "matching"
   * records.  This method will interpret the searchCriteria, perform the checks
   * for each criterion and build and return the results.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param criteria a populated SearchCriteriaVO.
   * @return a MergeSearchResultVO with the matches, if there were no matches the VO.size() will be 0.
   */
  public MergeSearchResultVO performSearch(SearchCriteriaVO criteria)
  {
    MergeSearchResultVO searchResults = new MergeSearchResultVO();
    ArrayList matchCriteria = criteria.getFieldCriteria();
    int mergeType = criteria.getType();  // 1 for Entity, 2 for Individual
    // need to pass the type back so the building of the list can
    // set links appropriately.
    searchResults.setMergeType(mergeType);
    int list = criteria.getSearchDomain();
    // Now get the id's of the entities or individuals of that domain.

    // Build the query to get All the search fields, and the info for the displaylist.
    // While you're at it, grab the appropriate FieldArrayList so you can dig out the
    // Queries.
    ArrayList fieldList = null;
    StringBuffer query = new StringBuffer("");
    switch (mergeType)
    {
      case 1:
        query.append("SELECT a.entityid as id, a.name as title, CONCAT(b.firstname, ' ', b.lastname) as owner ");
        query.append("FROM entity AS a, individual AS b ");
        query.append("WHERE a.owner = b.individualid AND a.entityid <> 1");
        fieldList = criteria.getEntityFieldList();
        break;
      case 2:
        query.append("SELECT a.individualid as id, CONCAT(a.firstname, ' ', a.lastname) as title, e.entityid as entityID, e.name as entity ");
        query.append("FROM individual AS a, entity AS e ");
        query.append("WHERE e.entityid = a.entity");
        fieldList = criteria.getIndividualFieldList();
        break;
      default : // we aren't 1 or 2 we cannot continue.
        throw new EJBException("Merge Type must be 1 or 2");
    }
    // If the list is not zero then we need to reduce the selection further.
    if (list != 0)
    {
      query.append(" AND a.list = ");
      query.append(list);
    }
    // The order of the returned results probably doesn't matter.
    // Now the query is ready, lets fire up the data access layer.
    StringBuffer domainQueryIds = new StringBuffer("(");
    CVDal cvdal = new CVDal(this.dataSource);
    ArrayList domain = new ArrayList();
    cvdal.setSqlQuery(query.toString());
    ResultSet domainResultSet = cvdal.executeQueryNonParsed();
    // Do this non-parsed so on one pass through the resultset
    // we can build the hashmap for display, and get the ids
    // gathered up for the query.
    try
    {
      boolean firstPass = true;
      while (domainResultSet.next())
      {
        HashMap record = new HashMap();
        Object id = domainResultSet.getObject(1);
        record.put("id",id); // The Object can be cast to a Number
        record.put("title", domainResultSet.getString(2));
        if(mergeType == 1){
        	record.put("owner", domainResultSet.getString(3));
        }
        if(mergeType == 2){
        	record.put("entityID", domainResultSet.getString(3));
        	record.put("entityName", domainResultSet.getString(4));
        }
        // This TreeMap will allow us to easily obtain the underlying
        // HashMap when we find matches.
        domain.add(record);
        if (!firstPass)
        {
          domainQueryIds.append(", ");
        } else {
          firstPass = false;
        }
        domainQueryIds.append(id);
      } // end while (domainResultSet.next())
      domainQueryIds.append(")");
    } catch (SQLException e) {
      System.out.println("[Exception][MergeEJB.performSearch] SQLException Thrown: " + e);
      cvdal.destroy();
      throw new EJBException(e);
    } finally {
      if (domainResultSet != null)
      {
        try { domainResultSet.close(); domainResultSet = null; } catch (SQLException e) {}
      }
      cvdal.setSqlQueryToNull();
    }
    // domain is now an TreeMap of hashmaps.  HashMaps are used to minimize changes
    // with the rest of the code during a rewrite of this algorithm on May 21, 2004.
    // Previously we just used CVDal.executeQuery(); which outputted collections of hashmaps.

    // Not enough results from the query to actually do anything
    // So return an empty ResultVO.
    if (domain.size() < 2)
    {
      return searchResults;  // is currently empty
    }

    int threshhold = criteria.getThreshhold();
    // Could potentially save a lot of time here by checking if the sum of the
    // criteria scores is < threshhold and just returrning no results
    // as there can never be a valid set of matches.

    // criteriaFields is an arraylist of HashMaps.  The internal HashMap contains the
    // actual fields pulled from the database.  Keyed to the ID
    // So comparison can be done fast and in memory.
    ArrayList criteriaFields = new ArrayList();
    int criteriaSize = matchCriteria.size();
    for (int i =  0; i < criteriaSize; i++)
    {
      HashMap criterion = (HashMap)matchCriteria.get(i);
      int fieldIndex = ((Integer)criterion.get(SearchCriteriaVO.FIELD_KEY)).intValue();
      MergeField field = (MergeField)fieldList.get(fieldIndex);
      StringBuffer fieldQuery = new StringBuffer(field.getQuery());
      fieldQuery.append(domainQueryIds);
      cvdal.setSqlQuery(fieldQuery.toString());
      ResultSet fieldResultSet = cvdal.executeQueryNonParsed();
      HashMap fieldMap = new HashMap();
      try
      {
        while(fieldResultSet.next())
        {
          // Put the String in the
          if(fieldResultSet.getObject(2) != null && fieldResultSet.getString(1) != null){
          	fieldMap.put(fieldResultSet.getObject(2), fieldResultSet.getString(1).trim());
	   	  }
        }
        criteriaFields.add(fieldMap);
      } catch (SQLException se) {
        System.out.println("[Exception][MergeEJB.performSearch] SQLException Thrown: " + se);
        throw new EJBException(se);
      } finally {
        if (domainResultSet != null)
        {
          try { fieldResultSet.close(); domainResultSet = null; } catch (SQLException e) {}
        }
        cvdal.setSqlQueryToNull();
      }
    } // end for (int i =  0; i < criteriaSize; i++)
    cvdal.destroy();

    // Now go through all the records and compare with each other.
    // do this with removal to limit the number of total comparisons
    // that need to be made.  We will continue to do this until there aren't
    // enough records left to do comparisons.  So a bug in our logic could potentially
    // cause an infinite loop, however our first operation is to pop the first element from
    // the vector, so unless something gets added back, it should be fine.
    while (domain.size() >= 2)
    {
      HashMap recordA = (HashMap)domain.remove(0); // pop the first record
      // Assuming we will find a successful match start building the ArrayList.
      // At the end if we still have only 1 record in there, then we obviously haven't matched.
      ArrayList matchGroup = new ArrayList();
      matchGroup.add(recordA);
      // Use an iterator to do our dirty work on the reducedDomain
      int i = 0;
      while( i < domain.size())
      {
        HashMap recordB = (HashMap)domain.get(i);
        int recordScore = 0;
        // Iterate the criteria and do the tests
        for (int j = 0; j < matchCriteria.size(); j++)
        {
          // criterion consists of three values keyed to FIELD_KEY, SEARCHTYPE_KEY and MATCHVALUE_KEY
          HashMap criterion = (HashMap)matchCriteria.get(j);
          int searchType = ((Integer)criterion.get(SearchCriteriaVO.SEARCHTYPE_KEY)).intValue();
          int criterionScore = ((Integer)criterion.get(SearchCriteriaVO.MATCHVALUE_KEY)).intValue();
          // 0 = case sensitive, 1 = case insensitive, 2 = soundex, 3 = metaphone
          // Defined in SearchCriteriaVO.populateSearchType()
          Number recordAId = (Number)recordA.get("id");
          Number recordBId =  (Number)recordB.get("id");
          HashMap fieldMap = (HashMap)criteriaFields.get(j);
          String recordAString = (String)fieldMap.get(recordAId);
          String recordBString = (String)fieldMap.get(recordBId);
          boolean match = false;
          if (recordAString != null && recordBString != null)
          {
            switch (searchType)
            {
              case 0:
                match = recordAString.equals(recordBString);
                break;
              case 1:
                match = recordAString.equalsIgnoreCase(recordBString);
                break;
              case 2:
                //match = this.soundexMatch();
                break;
              case 3:
                //match = this.metaphoneMatch();
                break;
              default:
                break;
            } // end switch (searchType.intValue())
          } // end if (recordAString != null && recordBString != null)
          if (match)
          {
          	// bump the score for this record by the criterion score
            recordScore += criterionScore;
            // now is a good time to check if we exceeded the threshhold.
            // it can save us time by not doing all the tests once we reach the threshhold.
            if (recordScore >= threshhold)
            {
                break;  // no need to do more tests on this record set, get out of the while loop.
            } // end if(recordScore >= threshhold)
          } // end if(match)
        } // end for (int j = 0; j < matchCriteria.size(); j++) ** Doing all the tests for a single set of records **
        
        i++;
        // At this point we have exited the field test loop now see if we have a "match"
        if (recordScore >= threshhold)
        {
          	// we match so stick B on the building matchGroup
	        matchGroup.add(recordB);
	        
	        // Then remove B from the collection, it doesn't need to be compared
	        // to anything else.
	        // We just incremeted the i by 1. We can't do it afterwords..
	        // thats the reason for decrementing by 1
	        domain.remove(i-1);
	        // reason why we have to initalize it to zero, because we might 
	        // loose the size count since we removed the node from the list
	        // its good if we start from first place we will match correctly.
	        i = 0;            	
        } // end if(recordScore >= threshhold)
      } // end while( i < domain.size()) ** Doing all the comparisons for a single recordA **
      
      // Now if the matchgroup has more than just recordA in it, we should add it to the search results.
      // otherwise just continue to the next' un.
      if (matchGroup.size() > 1)
      {
        searchResults.addGrouping(matchGroup);
      } // end if(matchGroup.size() > 1)
    } // end while (domain.size() >= 2) ** The main compare loop **
    return searchResults;
  } // end performSearch()

  /**
   * This method takes an ArrayList of Integers representing now
   * deleted Entities and an int (entityId) of the new merged record.  All newly orphaned
   * records will be adopted by the passed in entityId.  This is done using a series of database
   * queries.  It returns boolean if it thinks that it successfully rolled everything up.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param deletedEntities an ArrayList of Integers of the old Entities.
   * @param entityId an int representing the adopting parent record
   * @return true if successful, false otherwise.
   */
  public boolean rollUpEntityOrphans(ArrayList deletedEntities, int entityId)
  {
    // entityId will adopt all the orphans of deletedEntities, of the following record types:
    // Activities, Notes, Files, Opportunioties!, Projects
    // Tickets, Orders, Payments, Expenses, PO, Items.
    int recordType = 1;  // Entity is 1
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      for (int i = 0; i < deletedEntities.size(); i++)
      {
        int currentId = ((Integer)deletedEntities.get(i)).intValue();
        // Activites
        cvdal.setSqlQueryToNull();
        // UPDATE activitylink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?
        cvdal.setSql("mergeActivityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.setInt(3, recordType);
        cvdal.executeUpdate();
        // Notes
        cvdal.setSqlQueryToNull();
        // UPDATE note SET relateEntity = ? WHERE relateEntity = ?
        cvdal.setSql("mergeNoteEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Files
        cvdal.setSqlQueryToNull();      // UPDATE cvfile SET relateEntity = ? WHERE relateEntity = ?
        cvdal.setSql("mergeFileEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId
         );
        cvdal.executeUpdate();
        // Opportunities
        cvdal.setSqlQueryToNull();
        // UPDATE opportunity SET entityId = ? WHERE entityId = ?
        cvdal.setSql("mergeOpportunityEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Projects
        cvdal.setSqlQueryToNull();
        // Projects uses the module id in its link table, so we will comply.
        int entityModuleId = 14; // default to 14 as it is currently true.
        cvdal.setSqlQuery("SELECT moduleId FROM module WHERE name='entity'");
        Vector vec = (Vector)cvdal.executeQuery();
        if (vec != null)
        {
          HashMap result = (HashMap)vec.firstElement();
          entityModuleId =((Number)result.get("moduleId")).intValue();
        }
        // Now that we have that information we can do our update.
        cvdal.setSqlQueryToNull();
        // UPDATE projectlink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?
        cvdal.setSql("mergeProjectsRoll");
        cvdal.setInt(1,entityId);
        cvdal.setInt(2, currentId);
        cvdal.setInt(3, entityModuleId);
        cvdal.executeUpdate();
        // Tickets
        cvdal.setSqlQueryToNull();
        // UPDATE ticket SET entityId = ? WHERE entityId = ?
        cvdal.setSql("mergeTicketsEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Orders
        cvdal.setSqlQueryToNull();
        // UPDATE cvorder SET entityId = ? WHERE entityId = ?
        cvdal.setSql("mergeOrdersEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Invoices
        cvdal.setSqlQueryToNull();
        // UPDATE invoice SET customerId = ? WHERE customerId = ?
        cvdal.setSql("mergeInvoiceEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Payments
        cvdal.setSqlQueryToNull();
        // UPDATE payment SET entityId = ? WHERE entityId = ?
        cvdal.setSql("mergePaymentsEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Expense
        cvdal.setSqlQueryToNull();
        // UPDATE expense SET entityId = ? WHERE entityId = ?
        cvdal.setSql("mergeExpenseEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // PO
        cvdal.setSqlQueryToNull();
        // UPDATE purchaseorder SET entity = ? WHERE entity = ?
        cvdal.setSql("mergePurchaseOrderEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Items
        cvdal.setSqlQueryToNull();
        // UPDATE item SET vendorId = ? WHERE vendorId = ?
        cvdal.setSql("mergeItemVendorEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

        cvdal.setSqlQueryToNull();
        // UPDATE item SET manufacturerId = ? WHERE manufacturerId = ?
        cvdal.setSql("mergeItemManufacturerEntityRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Vendor
        cvdal.setSqlQueryToNull();
        // UPDATE vendor SET entityId = ? WHERE entityId = ?
        cvdal.setSql("mergeVendorRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
        // Inventory
        cvdal.setSqlQueryToNull();
        // UPDATE inventory SET vendorId = ? WHERE vendorId = ?
        cvdal.setSql("mergeInventoryVendorRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

        cvdal.setSqlQueryToNull();
        // UPDATE inventory SET customerId = ? WHERE customerId = ?
        cvdal.setSql("mergeInventoryCustomerRoll");
        cvdal.setInt(1, entityId);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();
      } // end for (int i = 0; i < deletedEntities.size(); i++)
    } finally {
      cvdal.destroy();
    }
    return true;
  } // end rollUpEntityOrphans(ArrayList deletedEntities, int entityId)

  /**
   * This method takes an ArrayList of Integers representing now
   * deleted Individuals and an int (individualID) of the new merged record.  All newly orphaned
   * records will be adopted by the passed in individualID.  This is done using a series of database
   * queries.  It returns boolean if it thinks that it successfully rolled everything up.
   *
   * @author Naresh Patel <npatel@centraview.com>
   * @param deletedIndividuals an ArrayList of Integers of the old Individuals.
   * @param individualID an int representing the adopting parent record
   * @return true if successful, false otherwise.
   */
  public boolean rollUpIndividualOrphans(ArrayList deletedIndividuals, int individualID)
  {
    // individualID will adopt all the orphans of deletedIndividuals, of the following record types:
    // Activities, Notes, Files, Opportunioties!, Projects
    // Tickets, Orders, Payments, Expenses, PO, Items.
    int recordType = 2;  // Individual is 1
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      for (int i = 0; i < deletedIndividuals.size(); i++)
      {
        int currentId = ((Integer)deletedIndividuals.get(i)).intValue();

        // Activites
        cvdal.setSqlQueryToNull();
        // UPDATE activitylink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?
        cvdal.setSql("mergeActivityRoll");
        cvdal.setInt(1, individualID);
        cvdal.setInt(2, currentId);
        cvdal.setInt(3, recordType);
        cvdal.executeUpdate();

        // Notes
        cvdal.setSqlQueryToNull();
        // UPDATE note SET relateIndividual = ? WHERE relateIndividual = ?
        cvdal.setSql("mergeNoteIndividualRoll");
        cvdal.setInt(1, individualID);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

        // Files
        cvdal.setSqlQueryToNull();
        // UPDATE cvfile SET relateIndividual = ? WHERE relateIndividual = ?
        cvdal.setSql("mergeFileIndividualRoll");
        cvdal.setInt(1, individualID);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

        // Opportunities
        cvdal.setSqlQueryToNull();
        // UPDATE opportunity SET individualID = ? WHERE individualID = ?
        cvdal.setSql("mergeOpportunityIndividualRoll");
        cvdal.setInt(1, individualID);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

        // Projects
        cvdal.setSqlQueryToNull();
        // Projects uses the module id in its link table, so we will comply.
        int IndividualModuleId = 15; // default to 15 as it is currently true.
        cvdal.setSqlQuery("SELECT moduleId FROM module WHERE name='Individual'");
        Vector vec = (Vector)cvdal.executeQuery();
        if (vec != null)
        {
          HashMap result = (HashMap)vec.firstElement();
          IndividualModuleId =((Number)result.get("moduleId")).intValue();
        }
        // Now that we have that information we can do our update.
        cvdal.setSqlQueryToNull();
        // UPDATE projectlink SET recordId = ? WHERE recordId = ? AND recordTypeId = ?
        cvdal.setSql("mergeProjectsRoll");
        cvdal.setInt(1,individualID);
        cvdal.setInt(2, currentId);
        cvdal.setInt(3, IndividualModuleId);
        cvdal.executeUpdate();

        // Tickets
        cvdal.setSqlQueryToNull();
        // UPDATE ticket SET individualid = ? WHERE individualid = ?
        cvdal.setSql("mergeTicketsIndividualRoll");
        cvdal.setInt(1, individualID);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

        // Proposal
        cvdal.setSqlQueryToNull();
        // UPDATE proposal SET individualid = ? WHERE individualid = ?
        cvdal.setSql("mergeProposalIndividualRoll");
        cvdal.setInt(1, individualID);
        cvdal.setInt(2, currentId);
        cvdal.executeUpdate();

      } // end for (int i = 0; i < deletedIndividuals.size(); i++)
    } finally {
      cvdal.destroy();
    }
    return true;
  } // end rollUpIndividualOrphans(ArrayList deletedIndividuals, int individualID)

}