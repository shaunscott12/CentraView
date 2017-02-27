/*
 * $RCSfile: ContactListEJB.java,v $    $Revision: 1.5 $  $Date: 2005/09/01 15:31:06 $ - $Author: mcallist $
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



package com.centraview.contact.contactlist;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.common.*;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class ContactListEJB implements SessionBean
{

	SessionContext sc;
	private String dataSource;
	private static Logger logger = Logger.getLogger(ContactListEJB.class);

  public ContactListEJB() {}

    /**
     * Called by the container to create a session bean instance. Its parameters typically
     * contain the information the client uses to customize the bean instance for its use.
     * It requires a matching pair in the bean class and its home interface.
     */
    public void ejbCreate() {}

    /**
     * A container invokes this method before it ends the life of the session object. This
     * happens as a result of a client's invoking a remove operation, or when a container
     * decides to terminate the session object after a timeout. This method is called with
     * no transaction context.
     */
    public void ejbRemove(){}

    /**
     * The activate method is called when the instance is activated from its 'passive' state.
     * The instance should acquire any resource that it has released earlier in the ejbPassivate()
     * method. This method is called with no transaction context.
     */
    public void ejbActivate(){}

    /**
     * The passivate method is called before the instance enters the 'passive' state. The
     * instance should release any resources that it can re-acquire later in the ejbActivate()
     * method. After the passivate method completes, the instance must be in a state that
     * allows the container to use the Java Serialization protocol to externalize and store
     * away the instance's state. This method is called with no transaction context.
     */
    public void ejbPassivate(){}

    /**
     * Set the associated session context. The container calls this method after the instance
     * creation. The enterprise Bean instance should store the reference to the context
     * object in an instance variable. This method is called with no transaction context.
     */
    public void setSessionContext(SessionContext sc)
    {
		this.sc = sc ;
    }

  /**
   * Calls the EJB/database layer to get a list of Entities
   * and returns a Collection of SQL results to the Struts
   * layer. The Action class which calls this method should
   * take the Collection returned and pass it to the ListGenerator
   * in order to create a DisplayList object for display to the
   * end user.
   *
   * @param individualID  int representation of the IndividualID
   *  of the currently logged in user. Used for record permissions.
   * @param pagingMap HashMap containing all "paging" parameters.
   *  The required values are:
   *  <ul>
   *    <li>recordsPerPage - (Integer) the number of records to show on each page.</li>
   *    <li>currentPage - (Integer) the page number that you would like to view.</li>
   *    <li>sortColumn - (String) the name of the column on which to sort results.</li>
   *  </ul>
   * @param listID int representing the marketing list ID which we
   *  want to return results from.
   *
   * @return A Collection of SQL results.
   */
    public Collection getEntityList(int individualID, HashMap pagingMap, int listID)
    {

      int recordsPerPage = ((Integer) pagingMap.get("recordsPerPage")).intValue();
      int currentPage = ((Integer) pagingMap.get("currentPage")).intValue();
      String sortColumn = (String) pagingMap.get("sortColumn");
      String searchString = (String) pagingMap.get("searchString");
      String sortDirection = (String) pagingMap.get("sortDirection");

		Integer dbIDtmp = (Integer)pagingMap.get("dbID");
		int dbID = 1;
		if (dbIDtmp != null)
		{
		  listID = dbIDtmp.intValue();
		}

      if (recordsPerPage < 0)
      {
        recordsPerPage = ListGenerator.DEFAULT_RECORDS_PER_PAGE;
      } //end of (recordsPerPage < 0)

      if (currentPage < 0)
      {
        currentPage = 1;
      } //end of if statement (currentPage < 0)

      int offset = 0;
      offset = recordsPerPage * (currentPage - 1);

      if (sortDirection == null || (! sortDirection.equals("ASC") && ! sortDirection.equals("DESC")) || (! sortDirection.equals("A") && ! sortDirection.equals("D")))
      {
        sortDirection = "ASC";
      } //end of if statement  (sortDirection == null  ...


      if (sortColumn == null || sortColumn.equals(""))
      {
        sortColumn = "EntityName";
      } //end of if statement (sortColumn == null ...

      Collection sqlResults = null;

      CVDal cvdl = new CVDal(dataSource);

      try
      {
        String sqlCreate ="CREATE TEMPORARY TABLE `entitylist` " +
          "SELECT e.EntityID EntityID, e.Name EntityName, " +
          "e.list dbid, i.FirstName FirstName, i.LastName  LastName, " +
          "i.IndividualID IndividualID, moc.Content Phone, moc.Content Email, " +
          "a.Website WebSite, a.Street1 Street1, a.Street2 Street2, " +
          "a.City City, st.Name State, a.Zip Zip, c.Name Country " +
          "FROM entity e " +
          "LEFT OUTER JOIN individual i ON (e.EntityID = i.Entity AND i.PrimaryContact = 'YES') " +
          "LEFT OUTER JOIN addressrelate ar ON (e.EntityID = ar.Contact AND ar.IsPrimary = 'YES') " +
          "LEFT OUTER JOIN contacttype ct ON (ar.ContactType = ct.ContactTypeID AND ct.Name = 'Entity') " +
          "LEFT OUTER JOIN address a ON (ar.Address = a.AddressID) " +
          "LEFT OUTER JOIN state st ON (a.State = st.StateID) " +
          "LEFT OUTER JOIN country c ON (a.Country = c.CountryID) " +
          "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) " +
          "where i.List = " + listID +  " ORDER BY " +
          sortColumn + " " + sortDirection + " LIMIT " + offset + ", " + recordsPerPage;

        cvdl.setSqlQuery(sqlCreate);
        cvdl.executeUpdate();
        cvdl.clearParameters();

        cvdl.setSql("contact.entitylistupdate3");
        cvdl.executeUpdate();
        cvdl.clearParameters();

        cvdl.setSql("contact.entitylistupdate4");
        cvdl.executeUpdate();
        cvdl.clearParameters();

        if (searchString != null && searchString.startsWith("ADVANCE:"))
        {
          searchString = searchString.substring(8);

          String searchTableSql = "CREATE TEMPORARY TABLE entitylistSearch " + searchString;
          cvdl.setSqlQueryToNull();
          cvdl.setSqlQuery(searchTableSql);
          cvdl.executeUpdate();
          cvdl.clearParameters();

          String sql = "SELECT entitylist.EntityID, entitylist.dbid dbid, " +
            "entitylist.EntityName, entitylist.Phone, entitylist.Email, entitylist.WebSite, " +
            "concat(entitylist.FirstName, ' ', entitylist.LastName) Name, " +
            "concat(entitylist.Street1, ' ', entitylist.Street2, ' ', entitylist.City, ' ', " +
            "entitylist.State, ' ', entitylist.Zip, ' ', entitylist.Country) Address, " +
            "entitylist.Street1, entitylist.Street2, entitylist.City, entitylist.State, " +
            "entitylist.Zip, entitylist.Country, entitylist.IndividualID from entitylist, " +
            "entitylistSearch WHERE entitylist.EntityID = entitylistSearch.EntityID";

          cvdl.setSqlQueryToNull();
          cvdl.setSqlQuery(sql);
          sqlResults = cvdl.executeQuery();
          cvdl.clearParameters();

          cvdl.setSqlQueryToNull();
          cvdl.setSqlQuery("DROP TABLE entitylistSearch");
          cvdl.executeUpdate();
          cvdl.clearParameters();
        } //end of if statement (searchString != null && searchString.startsWith("ADVANCE:"))
        else
        {
          cvdl.setSqlQueryToNull();
          cvdl.setSql("contact.entitylistselect");
          sqlResults = cvdl.executeQuery();
          cvdl.clearParameters();
        } //end of else statement (searchString != null && searchString.startsWith("ADVANCE:"))

        cvdl.setSqlQueryToNull();
        cvdl.setSql("contact.entitylistdroptable");
        sqlResults = cvdl.executeQuery();
        cvdl.clearParameters();
      } //end of try block
      catch (Exception e)
      {
        System.out.println("[Exception] ContactListEJB.getEntityList: " + e.toString());
        //e.printStackTrace();
      } //end of catch block (Exception)
      finally
      {
        cvdl.clearParameters();
        cvdl.destroy();
        cvdl = null;
      } //end of finally block

      if (sqlResults == null)
      {
        sqlResults = new ArrayList();
      } //end of if statement (sqlResults == null)

      return sqlResults;
    } //end of getEntityList method

  /**
   * Take the individualId and a set of preferences In a HashMap and build a
   * displaylist for the List.jsp
   *
   * @param individualId the id of the user trying to view the list.
   * @param preference Basically a HashMap that has sortColumn, sortDirection
   *          ADVANCESEARCHSTRING and ListID on it to help in building the
   *          display list.
   * @return An EntityList that can be displayed by the List.jsp
   */
  public EntityList getAllEntityList(int individualId, HashMap preference)
  {
    String sortColumn = preference.get("sortColumn") == null ? "EntityName" : (String) preference.get("sortColumn");
    if (sortColumn.equals(""))
    {
      sortColumn = "EntityName";
    }
    if (sortColumn.equals("PrimaryContact"))
    {
      sortColumn = "Name";
    }
    String sortDirection = preference.get("sortDirection") == null ? "A" : (String) preference.get("sortDirection");
    if (sortDirection.equals("D"))
    {
      sortDirection = "DESC";
    } else
    { // if it ain't decending it must be ascending.
      sortDirection = "ASC";
    }

    EntityList DL = new EntityList();
    Collection entityCollection = null;
    HashMap entityFieldRights = null;
    HashMap individualFieldRights = null;
    try
    {

			entityCollection = this.getAccessEntityList(individualId,preference);
      // get field rights
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome) ic.lookup("local/Authorization");
      AuthorizationLocal local = home.create();

      entityFieldRights = local.getNoneRightFieldMethod("Entity", individualId);
      if (entityFieldRights == null)
      {
        entityFieldRights = new HashMap();
      }
      individualFieldRights = local.getNoneRightFieldMethod("Individual", individualId);
      if (individualFieldRights == null)
      {
        individualFieldRights = new HashMap();
      }
    } catch (Exception e) {
      System.out.println("[Exception] ContactListEJB.getAllEntityList: " + e.toString());
      e.printStackTrace();
      throw new EJBException(e);
    }
    // Build the display List. This is really display layer type of stuff, and
    // should eventually be moved to the struts layer. A simple 2D table object would
    // really suffice and be more flexible for expansion.
    Iterator it = entityCollection.iterator();
    while (it.hasNext())
    {
      HashMap hm = (HashMap) it.next();
      int EntityID = 0;
      int IndividualID = 0;
      int dbID = 0;
      if (hm.get("EntityID") != null)
      {
        EntityID = ((Number) hm.get("EntityID")).intValue();
      }
      if (hm.get("IndividualID") != null)
      {
        IndividualID = ((Number) hm.get("IndividualID")).intValue();
      }
      if (hm.get("dbid") != null)
      {
        dbID = ((Number) hm.get("dbid")).intValue();
      }
      String EntityName = (String) hm.get("EntityName");
      EntityListElement ele = new EntityListElement(EntityID);
      IntMember intmem = new IntMember("EntityID", EntityID, 10, "", 'T', false, 10);
      IntMember intdbID = new IntMember("DbID", dbID, 10, "", 'T', false, 10);
      IntMember int_individual_mem = new IntMember("IndividualID", IndividualID, 10, "", 'T', false, 10);
      ele.put("EntityID", intmem);
      ele.put("DbID", intdbID);
      ele.put("IndividualID", int_individual_mem);

      int accountManagerID = 0;
      if (hm.get("AccountManagerID") != null)
      {
        accountManagerID = ((Number) hm.get("AccountManagerID")).intValue();
      }

      IntMember intAccountManagerID = new IntMember("AccountManagerID", accountManagerID, 10, "", 'T', false, 10);
      ele.put("AccountManagerID", intAccountManagerID);

      StringMember accountManager = new StringMember("AccountManager", (String) hm.get("AccountManager"), 10, "", 'T', false);
      ele.put("AccountManager", accountManager);

      if (entityFieldRights.containsKey("name"))
      {
        StringMember one = new StringMember("Name", "", 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.ENTITY + "&rowId=" + EntityID, 'T',
            true);
        ele.put("Name", one);
      } else
      {
        StringMember one = new StringMember("Name", (String) hm.get("EntityName"), 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.ENTITY
            + "&rowId=" + EntityID, 'T', true);
        ele.put("Name", one);
      }
      String tempName = (String) hm.get("Name");
      if (tempName != null)
      {
        if (individualFieldRights.containsKey("firstname"))
        {
          int indexOfSpace = tempName.indexOf(" ");
          if (indexOfSpace > 0)
          {
            tempName = tempName.substring(indexOfSpace, tempName.length());
          }
        }
        if (individualFieldRights.containsKey("lastname"))
        {
          int indexOfSpace = tempName.indexOf(" ");
          if (indexOfSpace > 0)
          {
            tempName = tempName.substring(indexOfSpace, tempName.length());
          } else
          {
            tempName = "";
          }
        }
      } // end if (tempName != null)
      if (tempName == null)
      {
        tempName = "";
      }
      StringMember two = new StringMember("PrimaryContact", tempName, 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.ENTITY + "&rowId="
          + EntityID, 'T', true);
      ele.put("PrimaryContact", two);
      if (entityFieldRights.containsKey("contactmethod"))
      {
        StringMember three = new StringMember("Phone", "", 10, "", 'T', false);
        StringMember four = new StringMember("Email", "", 10, "/centraview/jsp/common/MailCompose.jsp", 'T', true);
        ele.put("Phone", three);
        ele.put("Email", four);
      } else
      {
        StringMember three = new StringMember("Phone", (String) hm.get("Phone"), 10, "", 'T', false);
        StringMember four = new StringMember("Email", (String) hm.get("Email"), 10, "/centraview/jsp/common/MailCompose.jsp", 'T', true);
        ele.put("Phone", three);
        ele.put("Email", four);
      }
      if (entityFieldRights.containsKey("address"))
      {
        StringMember five = new StringMember("Website", "", 10, (String) hm.get("WebSite"), 'T', true);
        StringMember six = new StringMember("Address", "", 10, "", 'T', false);
        StringMember Add1 = new StringMember("Street1", "", 10, "", 'T', false);
        StringMember Add2 = new StringMember("Street2", "", 10, "", 'T', false);
        StringMember Add3 = new StringMember("City", "", 10, "", 'T', false);
        StringMember Add4 = new StringMember("State", "", 10, "", 'T', false);
        StringMember Add5 = new StringMember("Zip", "", 10, "", 'T', false);
        StringMember Add6 = new StringMember("Country", "", 10, "", 'T', false);
        ele.put("Website", five);
        ele.put("Address", six);
        ele.put("Street1", Add1);
        ele.put("Street2", Add2);
        ele.put("City", Add3);
        ele.put("State", Add4);
        ele.put("Zip", Add5);
        ele.put("Country", Add6);
      } else
      {
        StringMember five = new StringMember("Website", (String) hm.get("WebSite"), 10, (String) hm.get("WebSite"), 'T', true);
        StringMember six = new StringMember("Address", (String) hm.get("Address"), 10, "", 'T', false);
        StringMember Add1 = new StringMember("Street1", (String) hm.get("Street1"), 10, "", 'T', false);
        StringMember Add2 = new StringMember("Street2", (String) hm.get("Street2"), 10, "", 'T', false);
        StringMember Add3 = new StringMember("City", (String) hm.get("City"), 10, "", 'T', false);
        StringMember Add4 = new StringMember("State", (String) hm.get("State"), 10, "", 'T', false);
        StringMember Add5 = new StringMember("Zip", (String) hm.get("Zip"), 10, "", 'T', false);
        StringMember Add6 = new StringMember("Country", (String) hm.get("Country"), 10, "", 'T', false);
        ele.put("Website", five);
        ele.put("Address", six);
        ele.put("Street1", Add1);
        ele.put("Street2", Add2);
        ele.put("City", Add3);
        ele.put("State", Add4);
        ele.put("Zip", Add5);
        ele.put("Country", Add6);
      }

      DL.put(EntityName + EntityID, ele);
    } // end while (it.hasNext())

    DL.setSortMember(sortColumn);
    DL.setSortType(sortDirection.charAt(0));
    DL.setTotalNoOfRecords(DL.size());
    DL.setBeginIndex(1);
    DL.setEndIndex(DL.getTotalNoOfRecords());
    DL.setStartAT(1); // shouldn't this be on the preferences Hash
    DL.setEndAT(10); // shouldn't this be on the preferences Hash
    return DL;
  } //end of getAllEntityList


  public IndividualList getAllIndividualList(int individualId, HashMap preference)
  {
    IndividualList DL = new IndividualList();

    try
    {

      String sortColumn = (String) preference.get("sortColumn");
      String sortDirection = (String) preference.get("sortDirection");
      if (sortDirection == null || (!sortDirection.equals("A") && !sortDirection.equals("D")))
      {
        sortDirection = "ASC";
      } //end of if statement (sortDirection == null ...
      if (sortDirection != null && sortDirection.equals("A"))
      {
        sortDirection = "ASC";
      }
      if (sortDirection != null && sortDirection.equals("D"))
      {
        sortDirection = "DESC";
      }

 	  Collection individualCollection = this.getAccessIndividualList(individualId,preference);

      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome) ic.lookup("local/Authorization");
      AuthorizationLocal local = home.create();
      HashMap individualFieldRights = local.getNoneRightFieldMethod("Individual", individualId);
      HashMap entityFieldRights = local.getNoneRightFieldMethod("Entity", individualId);

      Iterator it = individualCollection.iterator();

      while (it.hasNext())
      {
        HashMap hm = (HashMap) it.next();
        String IndividualName = (String) hm.get("Name");
        int IndividualID = ((Long) hm.get("IndividualID")).intValue();
        int entId = ((Long) hm.get("EntityID")).intValue();
        int dbId = 0;

        if (hm.get("dbid") != null)
        {
          dbId = ((Long) hm.get("dbid")).intValue();
        } //end of if statement (hm.get("dbid") != null)

        IndividualListElement ele = new IndividualListElement(IndividualID);
        IntMember intmem = new IntMember("IndividualID", IndividualID, 10, "", 'T', false, 10);
        IntMember entityId = new IntMember("EntityID", entId, 10, "", 'T', false, 10);
        IntMember intDbId = new IntMember("DbID", dbId, 10, "", 'T', false, 10);

        ele.put("IndividualID", intmem);
        ele.put("EntityID", entityId);
        ele.put("DbID", intDbId);

        String tempName = (String) hm.get("Name");

        if (individualFieldRights != null && tempName != null)
        {
          if (individualFieldRights.containsKey("firstname"))
          {
            int indexOfSpace = tempName.indexOf(" ");
            if (indexOfSpace > 0)
            {
              tempName = tempName.substring(indexOfSpace, tempName.length());
            }
          }// End if (individualFieldRights.containsKey("firstname"))

          if (individualFieldRights.containsKey("lastname"))
          {
            int indexOfSpace = tempName.indexOf(" ");
            if (indexOfSpace > 0)
            {
              tempName = tempName.substring(indexOfSpace, tempName.length());
            } else
            {
              tempName = "";
            }
          }// End if (individualFieldRights.containsKey("firstname"))
        }//end if (entityFieldRights != null )

        if (tempName == null)
        {
          tempName = "";
        } //end of if statement (tempName == null)

        StringMember one = new StringMember("Name", tempName, 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL + "&rowId=" + IndividualID, 'T', true);
        ele.put("Name", one);

        if (individualFieldRights != null && individualFieldRights.containsKey("title"))
        {
          StringMember two = new StringMember("Title", "", 10, "", 'T', false);
          ele.put("Title", two);
        } else
        {
          StringMember two = new StringMember("Title", (String) hm.get("Title"), 10, "", 'T', false);
          ele.put("Title", two);
        }

        if (entityFieldRights != null && entityFieldRights.containsKey("name"))
        {
          StringMember three = new StringMember("Entity", "", 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL + "&rowId=" + IndividualID, 'T', true);
          ele.put("Entity", three);
        } else
        {
          StringMember three = new StringMember("Entity", (String) hm.get("Entity"), 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL + "&rowId=" + IndividualID, 'T', true);
          ele.put("Entity", three);
        }

        if (individualFieldRights != null && individualFieldRights.containsKey("contactmethod"))
        {
          StringMember four = new StringMember("Phone", "", 10, " ", 'T', false);
          StringMember five = new StringMember("Fax", "", 10, "#", 'T', false);
          StringMember six = new StringMember("Email", "", 10, "/centraview/jsp/common/MailCompose.jsp", 'T', true);
          ele.put("Phone", four);
          ele.put("Fax", five);
          ele.put("Email", six);
        } else
        {
          StringMember four = new StringMember("Phone", (String) hm.get("Phone"), 10, " ", 'T', false);
          StringMember five = new StringMember("Fax", (String) hm.get("Fax"), 10, "#", 'T', false);
          StringMember six = new StringMember("Email", (String) hm.get("Email"), 10, "/centraview/jsp/common/MailCompose.jsp", 'T', true);
          ele.put("Phone", four);
          ele.put("Fax", five);
          ele.put("Email", six);
        }

        if (individualFieldRights != null && individualFieldRights.containsKey("firstname"))
        {
          StringMember fname = new StringMember("FirstName", "", 10, "", 'T', false);
          ele.put("FirstName", fname);
        } else
        {
          StringMember fname = new StringMember("FirstName", (String) hm.get("FirstName"), 10, "", 'T', false);
          ele.put("FirstName", fname);
        }

        if (individualFieldRights != null && individualFieldRights.containsKey("lastname"))
        {
          StringMember lName = new StringMember("LastName", "", 10, "", 'T', false);
          ele.put("LastName", lName);
        } else
        {
          StringMember lName = new StringMember("LastName", (String) hm.get("LastName"), 10, "", 'T', false);
          ele.put("LastName", lName);
        }

        if (individualFieldRights != null && individualFieldRights.containsKey("firstname"))
        {
          StringMember fullName = new StringMember("FullName", "", 10, "", 'T', false);
          ele.put("FullName", fullName);
        } else
        {
          String fullNameString = "";
          if (hm.get("FirstName") != null)
          {
            fullNameString += (String) hm.get("FirstName");
          } //end of if statement (hm.get( "FirstName" ) != null)
          if (hm.get("LastName") != null)
          {
            if (fullNameString.length() > 0)
            {
              fullNameString += " ";
            } //end of if statement (fullNameString.length() > 0)
            fullNameString += (String) hm.get("LastName");
          } //end of if statement (hm.get( "LastName" ) != null)
          StringMember fullName = new StringMember("FullName", fullNameString, 10, "", 'T', false);
          ele.put("FullName", fullName);
        }

        if (individualFieldRights != null && individualFieldRights.containsKey("middlename"))
        {
          StringMember mName = new StringMember("MiddleInitial", "", 10, "", 'T', false);
          ele.put("MiddleInitial", mName);
        } else
        {
          StringMember mName = new StringMember("MiddleInitial", (String) hm.get("MiddleInitial"), 10, "", 'T', false);
          ele.put("MiddleInitial", mName);
        }

        DL.put(IndividualName + IndividualID, ele);
      } //end of while loop (it.hasNext())

      DL.setSortMember(sortColumn);
      DL.setSortType(sortDirection.charAt(0));
      DL.setTotalNoOfRecords(DL.size());
      DL.setBeginIndex(1);
      DL.setEndIndex(DL.getTotalNoOfRecords());
      DL.setStartAT(1);
      DL.setEndAT(10);
      DL.setIndividualId(individualId);
    } //end of try block
    catch (Exception e)
    {
      logger.error("[getAllIndividualList] Exception thrown.", e);
    } //end of catch block
    return DL;
  } //end of getAllIndividualList

    public GroupList getAllGroupList(int individualId, HashMap preference)
    {
		GroupList DL = new GroupList();
		try{

		    String advSearchstr = "";
		    if (preference != null)
		    {
		    	advSearchstr  = (String)preference.get("ADVANCESEARCHSTRING");
		    }


			DL.setSortMember("Name");
			CVDal cvdl = new CVDal(dataSource);

			cvdl.setSql("contact.grouplistcreatetable");
			cvdl.executeUpdate();
			cvdl.clearParameters();

			cvdl.setSql("contact.grouplistinsert");
      cvdl.setInt(1, individualId);
      cvdl.setInt(2, individualId);
			cvdl.executeUpdate();
			cvdl.clearParameters();


			InitialContext ic = CVUtility.getInitialContext();
			AuthorizationLocalHome home = (AuthorizationLocalHome)ic.lookup("local/Authorization");
			AuthorizationLocal local =  home.create();

			HashMap groupFieldRights = local.getNoneRightFieldMethod("Group", individualId);


			Collection v = null;

			/* Added for Advance Search	*/
			if (advSearchstr != null && advSearchstr.startsWith("ADVANCE:"))
			{
				advSearchstr = advSearchstr.substring(8);
				String str = "create TEMPORARY TABLE grouplistSearch "+advSearchstr;
				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery(str);
				cvdl.executeUpdate();
				cvdl.clearParameters();

				str = "Select grouplist.GroupID, grouplist.Name, grouplist.Description, grouplist.NoOfMembers from grouplist ,grouplistSearch where grouplist.GroupID =  grouplistSearch.GroupID ";
				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery(str);
				v = cvdl.executeQuery();

				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery("DROP TABLE grouplistSearch");
				cvdl.executeUpdate();
			}
			else
			{
				cvdl.setSql("contact.grouplistselect");
				v = cvdl.executeQuery();
			}

			cvdl.setSql("contact.grouplistdroptable");
			cvdl.executeUpdate();

			cvdl.clearParameters();
			cvdl.destroy();

			Iterator it = v.iterator();

			while( it.hasNext() )
			{

				HashMap hm = ( HashMap  )it.next();
				int GroupID = ((Long)hm.get("GroupID")).intValue();
				String GroupName = (String)hm.get( "Name" );

				GroupListElement ele = new GroupListElement( GroupID );

				IntMember intmem = new IntMember( "GroupID"  , GroupID , 10 , "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.GROUP  + "&" + Constants.PARAMID + "=" + GroupID , 'T' , false , 10 );
				ele.put( "GroupID", intmem );


				if (groupFieldRights != null && groupFieldRights.containsKey("groupname")){
					StringMember one  = new StringMember( "Name", "",10 , "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.GROUP  + "&rowId=" + GroupID, 'T' , true  );
					ele.put( "Name", one );
				}
				else{
					StringMember one  = new StringMember( "Name", (String) hm.get( "Name" ) ,10 , "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.GROUP  + "&rowId=" + GroupID, 'T' , true  );
					ele.put( "Name", one );
				}

				if (groupFieldRights != null && groupFieldRights.containsKey("groupdescription")){
					StringMember two  = new StringMember( "Description","" ,10 , "requestURL" , 'T' , false   );
					ele.put( "Description" ,  two );
				}
				else{
					StringMember two  = new StringMember( "Description", (String)hm.get( "Description" ) ,10 , "requestURL" , 'T' , false   );
					ele.put( "Description" ,  two );
				}

				int nom = ((Number)hm.get( "NoOfMembers" )).intValue();
				IntMember three= new IntMember( "NOOfMembers",nom ,10 , "" , 'T' , false, 10   );
				ele.put( "NOOfMembers" , three );
				DL.put( GroupName+GroupID , ele )
				;
			}
			DL.setTotalNoOfRecords(DL.size());
			DL.setBeginIndex(1);
			DL.setEndIndex(DL.getTotalNoOfRecords());
			DL.setStartAT( 1 );
			DL.setEndAT ( 10 );
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return DL ;
    }


    /**
     * Gets all Address for this contactID and ContactType.
     */
  public AddressList getAddressList(int userId, int contactId, int contactType)
  {
    AddressList addressList = new AddressList();
    CVDal dl = new CVDal(dataSource);
    try {
      String addressQuery = "SELECT a.AddressID, a.AddressType, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, a.Website, b.IsPrimary , a.jurisdictionID FROM address a, addressrelate b WHERE a.AddressID = b.Address AND b.Contact= ? and b.contactType= ? ";
      dl.setSqlQuery(addressQuery);
      dl.setInt(1, contactId);
      dl.setInt(2, contactType);
      Collection col = dl.executeQuery();

      if (col != null) {
        Iterator it = col.iterator();

        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          {
            int addressID = ((Long)hm.get("AddressID")).intValue();

            IntMember addId = new IntMember("AddressID", addressID, 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.GROUP + "&" + Constants.PARAMID + "=" + ((Long)hm.get("AddressID")).intValue(), 'T', false, 10);
            StringMember street1 = new StringMember("Street1", (String)hm.get("Street1"), 10, "requestURL", 'T', true);
            StringMember street2 = new StringMember("Street2", (String)hm.get("Street2"), 10, "requestURL", 'T', false);
            StringMember city = new StringMember("City", (String)hm.get("City"), 10, "requestURL", 'T', false);
            String stateValue = (String)hm.get("State");
            if (stateValue != null && stateValue.equals("0")) {
              stateValue = "";
            }
            StringMember state = new StringMember("State", stateValue, 10, "requestURL", 'T', false);
            StringMember zip = new StringMember("Zip Code", (String)hm.get("Zip"), 10, "requestURL", 'T', false);
            String countryValue = (String)hm.get("Country");
            if (countryValue != null && countryValue.equals("0")) {
              countryValue = "";
            }
            StringMember country = new StringMember("Country", countryValue, 10, "requestURL", 'T', false);
            StringMember website = new StringMember("Website", (String)hm.get("Website"), 10, "requestURL", 'T', false);

            int jurisdictionID = ((Number)hm.get("jurisdictionID")).intValue();

            StringMember strJurisdictionID = new StringMember("jurisdictionID", jurisdictionID + "", 10, "", 'T', false);

            AddressListElement listElement = new AddressListElement(addressID);
            listElement.put("AddressID", addId);
            listElement.put("Street1", street1);
            listElement.put("Street2", street2);
            listElement.put("City", city);
            listElement.put("ZipCode", zip);
            listElement.put("Country", country);
            listElement.put("State", state);
            listElement.put("Website", website);
            listElement.put("jurisdictionID", strJurisdictionID);
            addressList.put((String)hm.get("Street1") + addressID, listElement);
          }
        }
      }
    } catch (Exception e) {
      logger.error("[getAddressList] Exception thrown.", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return addressList;
  } //end of getAddressList method
  
  public ValueListVO getAddressValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE addresslistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
    }
    int numberOfRecords = 0;
    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "addresslistfilter", individualId, 0, "address", "addressId", "owner", null, false);
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildAddressListQuery(parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE addresslistfilter");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE listfilter");
    cvdl.executeUpdate();
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }
  private String buildAddressListQuery(ValueListParameters parameters)
  {
    String select = "SELECT a.addressId, a.street1, a.street2, a.city, a.state, a.zip, a.country, at.name ";
    StringBuffer from = new StringBuffer("FROM address AS a, addresstype AS at, listfilter AS lf ");
    StringBuffer where = new StringBuffer("WHERE at.typeId = a.addressType AND a.addressId = lf.addressId ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  public MOCList getMOCList(int userId, int contactId, int contactType)
  {
    MOCList ml = new MOCList();
    CVDal dl = new CVDal(dataSource);
    dl.setSql("contact.getallmoc");
    dl.setInt(1, contactId);
    dl.setInt(2, contactType);
    Collection col = dl.executeQuery();
    dl.clearParameters();
    dl.destroy();
    if (col != null) {
      Iterator it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        int mocID = ((Long)hm.get("mocid")).intValue();
        StringMember content = new StringMember("Content", (String)hm.get("Content"), 10, "requestURL", 'T', true);
        StringMember note = new StringMember("Notes", (String)hm.get("Note"), 10, "requestURL", 'T', false);
        StringMember mocType = new StringMember("Type", (String)hm.get("Name"), 10, "requestURL", 'T', true);
        StringMember isPrimary = new StringMember("IsPrimary", (String)hm.get("isPrimary"), 10, "requestURL", 'T', false);
        StringMember syncAs = new StringMember("SyncAs", (String)hm.get("syncas"), 10, "requestURL", 'T', false);
        StringMember mocOrder = new StringMember("Order", (String)hm.get("MOCOrder"), 10, "requestURL", 'T', false);
        MOCListElement listElement = new MOCListElement(mocID);
        listElement.put("Notes", note);
        listElement.put("Type", mocType);
        listElement.put("IsPrimary", isPrimary);
        listElement.put("Content", content);
        listElement.put("SyncAs", syncAs);
        listElement.put("Order", mocOrder);
        ml.put((String)hm.get("Content") + mocID, listElement);
      }
    }
    return ml;
  }

  public ValueListVO getMOCValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE moclistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
    }
    int numberOfRecords = 0;
    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "moclistfilter", individualId, 0, "methodofcontact", "mocId", "owner", null, false);
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildMOCListQuery(parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE moclistfilter");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE listfilter");
    cvdl.executeUpdate();
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }
  
  private String buildMOCListQuery(ValueListParameters parameters)
  {
    String select = "SELECT moc.mocid, mt.name AS mocType, moc.content, moc.note, moc.syncas ";
    StringBuffer from = new StringBuffer("FROM methodofcontact AS moc, moctype AS mt, listfilter AS lf ");
    StringBuffer where = new StringBuffer("WHERE moc.mocType = mt.mocTypeId AND moc.mocid = lf.mocid ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }
  
   public Vector getDBList(int userId)
  {
    Vector vecDB = new Vector();
    vecDB.addElement(new DDNameValue("-1","All Lists"));
    CVDal cvdl = new CVDal(dataSource);
    try
    {
      cvdl.setSql("contact.getAllDb");
      cvdl.setInt(1, userId);
      cvdl.setInt(2, userId);
      Collection colDB = cvdl.executeQuery();

      if (colDB.size() > 0)
      {
        Iterator iter = colDB.iterator();

        while (iter.hasNext())
        {
          HashMap hm = (HashMap)iter.next();
          if (hm.get("name") != null)
          {
            vecDB.addElement(new DDNameValue(((Long)hm.get("dbaseid")).toString(),(String)hm.get("name")));
          }
        }
      }
      
    }catch(Exception e){
      System.out.println("[Exception][ContactListEJB] Exception thrown in getDBList(): " + e);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return(vecDB);
	}

  /**
   * This method gets a list of individuals and their email addresses and then
   * a list of entities and their email address and squashes them together for
   * a email list lookup.
   * @param individualId
   * @param preference
   * @return
   */
  public IndividualList getAllIndividualAndEntityEmailList(int individualId, HashMap preference)
  {
    IndividualList DL = new IndividualList();
    CVDal cvdl = new CVDal(dataSource);
    try
    {
      String sortColumn = (String)preference.get("sortColumn");
      String sortDirection = (String)preference.get("sortDirection");
      if (sortDirection == null || (!sortDirection.equals("A") && !sortDirection.equals("D")))
      {
        sortDirection = "ASC";
      } //end of if statement (sortDirection == null ...
      if (sortDirection != null && sortDirection.equals("A"))
      {
        sortDirection = "ASC";
      }
      if (sortDirection != null && sortDirection.equals("D"))
      {
        sortDirection = "DESC";
      }

      Integer listId = preference.get("dbID") == null ? new Integer(1) : (Integer)preference.get("dbID");
      int list = listId.intValue();
      DL.setSortMember("Name");

      EJBUtil.buildListFilterTable(cvdl, null, individualId, 15, "individual", "individualId", "owner", null, true);
      String individualSelect = "SELECT i.individualId AS recordId, CONCAT(i.FirstName, ' ', i.LastName) AS Name, moc.Content AS Email"
        + " FROM individual i, mocrelate mr , methodofcontact moc, listfilter AS lf  "
        + " WHERE lf.individualId = i.IndividualID and i.list="+list
        + " and i.IndividualID=mr.ContactID AND moc.MOCID=mr.MOCID AND moc.MOCType=1 AND mr.contactType = 2"
        + " ORDER BY " + sortColumn + " " + sortDirection;
      cvdl.setSqlQuery(individualSelect);
      Collection individualResults = cvdl.executeQuery();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS listfilter");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      EJBUtil.buildListFilterTable(cvdl, null, individualId, 14, "entity", "entityId", "owner", null, true);
      String entitySelect = "SELECT e.entityId AS recordId, e.name AS Name, moc.Content AS Email"
        + " FROM entity AS e, mocrelate AS mr , methodofcontact AS moc, listfilter AS lf  "
        + " WHERE lf.entityId = e.entityId and e.list="+list
        + " and e.entityId = mr.contactId AND moc.MOCID = mr.MOCID AND moc.MOCType=1 AND mr.contactType = 1"
        + " ORDER BY " + sortColumn + " " + sortDirection;
      cvdl.setSqlQuery(entitySelect);
      Collection entityResults = cvdl.executeQuery();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS listfilter");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      
      this.PopulateEmailList(individualResults, DL, Constants.INDIVIDUAL);
      this.PopulateEmailList(entityResults, DL, Constants.ENTITY);
      
      DL.setTotalNoOfRecords(DL.size());
      DL.setBeginIndex(1);
      DL.setEndIndex(DL.getTotalNoOfRecords());
      DL.setStartAT(1);
      DL.setEndAT(10);
      DL.setIndividualId(individualId);
    } catch (Exception e) {
      logger.error("[getAllIndividualAndEntityEmailList] Exception thrown.", e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return DL;
  } //end of getAllIndividualList
  
  private void PopulateEmailList(Collection queryResults, IndividualList DL, String contactType)
  {
    Iterator it = queryResults.iterator();
    while (it.hasNext())
    {
      HashMap hm = (HashMap)it.next();
      String name = (String)hm.get("Name");
      int recordId = ((Number)hm.get("recordId")).intValue();
      IndividualListElement ele = new IndividualListElement(recordId);
      String tempName = (String)hm.get("Name");
      if (tempName == null)
      {
        tempName = "";
      } //end of if statement (tempName == null)
      StringMember one = new StringMember("Name", tempName, 10, "/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + contactType + "&rowId=" + recordId, 'T', true);
      ele.put("Name", one);
      String tempEmail = (String)hm.get("Email");
      StringMember email = new StringMember("Email", tempEmail, 10, "/centraview/jsp/common/MailCompose.jsp", 'T', true);
      ele.put("Email", email);
      DL.put(name + recordId + tempEmail, ele);
    } //end of while loop (it.hasNext())
  }


	/**
   * @author Kevin McAllister <kevin@centraview.com>This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }


	/**
   * Calls the EJB/database layer to get a list of Entity and returns a
   * Collection of SQL results to the EJB layer. The Action class which calls
   * this method should take the Collection returned and pass it to the calling
   * method in order to create a DisplayList object for display to the end user.
   *
   * @param individualID int representation of the IndividualID of the currently
   *          logged in user. Used for record permissions.
   * @param pagingMap HashMap containing all "paging" parameters. The required
   *          values are:
   *          <ul>
   *          <li>recordsPerPage - (Integer) the number of records to show on
   *          each page.</li>
   *          <li>currentPage - (Integer) the page number that you would like
   *          to view.</li>
   *          <li>sortColumn - (String) the name of the column on which to sort
   *          results.</li>
   *          </ul>
   */
	public Collection getAccessEntityList(int individualId, HashMap preference)
	{
		CVDal cvdl = new CVDal(dataSource);
		Collection entityCollection = null;
		try{

			String sortColumn = preference.get("sortColumn") == null ? "EntityName" : (String) preference.get("sortColumn");
			if (sortColumn.equals(""))
			{
				sortColumn = "EntityName";
			}
			if (sortColumn.equals("PrimaryContact"))
			{
				sortColumn = "Name";
			}
			String sortDirection = preference.get("sortDirection") == null ? "A" : (String) preference.get("sortDirection");
			if (sortDirection.equals("D"))
			{
				sortDirection = "DESC";
			} else
			{ // if it ain't decending it must be ascending.
				sortDirection = "ASC";
			}
      boolean search = false;
      String searchString = preference.get("ADVANCESEARCHSTRING") == null ? "" : (String) preference.get("ADVANCESEARCHSTRING");
      if (searchString.startsWith("ADVANCE:"))
      {
        // if there is an advancesearchstring then create a temporary table
        // this way we can join against it.
        searchString = searchString.substring(8);
        String str = "create TEMPORARY TABLE entitylistsearch " + searchString;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        search = true;
      } // end if (searchString.startsWith("ADVANCE:"))

      String select = "SELECT e.EntityID, e.Name AS EntityName, e.list AS dbid, e.AccountManagerID, " +
        "CONCAT(ami.firstName, ' ', ami.lastName) AS AccountManager, pci.IndividualID, " +
        "CONCAT(pci.FirstName, ' ', pci.LastName) AS Name, mocphone.content AS Phone, " +
        "mocemail.content AS Email, a.WebSite, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, " +
        "CONCAT(a.Street1, ' ', a.Street2, ' ', a.City, ' ', a.State, ' ', a.Zip, ' ', a.Country) AS Address ";

      String joinConditions =
        "LEFT OUTER JOIN individual AS pci ON (e.entityid = pci.entity AND pci.primaryContact = 'YES') " +
        "LEFT OUTER JOIN individual AS ami ON (e.accountManagerId = ami.individualId) " +
        "LEFT OUTER JOIN addressrelate AS ar ON (e.entityId = ar.contact AND ar.isPrimary = 'YES' AND ar.contactType = 1) "+
        "LEFT OUTER JOIN address AS a ON (ar.address = a.addressId) " +
        "LEFT OUTER JOIN methodofcontact AS mocphone ON (mocphone.mocId = 0) " +
        "LEFT OUTER JOIN methodofcontact AS mocemail ON (mocemail.mocId = 0) ";

      StringBuffer from = new StringBuffer("FROM entity AS e ");
      StringBuffer where = new StringBuffer("WHERE 1=1 ");
      Integer listId = preference.get("ListID") == null ? new Integer(0) : (Integer)preference.get("ListID");
      int list = listId.intValue();
      // if we are only selecting from a single list the a WHERE criteria
      // needs to be added.
      if (list > 0)
      {
        where.append("AND e.list = ");
        where.append(list);
        where.append(" ");
      }
      // if an advanced search query was run we need to do an inner join against the
      // temporary table
      if (search)
      {
        from.append(", entitylistsearch AS els ");
        where.append("AND e.entityId = els.entityId ");
      }

      // Build up the actual query using all the different permissions.
      // Where owner = passed individualId
      StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE entitylist ");
      query.append(select);
      query.append(from);
      query.append(joinConditions);
      query.append(where);
      query.append("AND e.owner = ?");
      // Do the whole query again with public records:
      query.append(" UNION ");
      query.append(select);
      query.append(from);
      query.append(", publicrecords AS pub ");
      query.append(joinConditions);
      query.append(where);
      query.append("AND pub.recordId = e.entityId AND pub.moduleId = 14");
      // Do the whole query a final time with recordauthorization
      query.append(" UNION ");
      query.append(select);
      query.append(from);
      query.append(", recordauthorisation AS auth ");
      query.append(joinConditions);
      query.append(where);
      query.append("AND auth.recordId = e.entityId AND auth.recordTypeId = 14 AND ");
      query.append("auth.individualId = ? AND auth.privilegeLevel < 40 AND auth.privilegeLevel > 0");
      cvdl.setSqlQuery(query.toString());
      cvdl.setInt(1, individualId);
      cvdl.setInt(2, individualId);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // obtain a Lock on the tables here to prevent deadlocks
      boolean previousFlag = cvdl.getAutoCommit();
      cvdl.setAutoCommit(false);
      cvdl.setSqlQuery("LOCK TABLES entitylist AS el WRITE, methodofcontact AS moc WRITE, mocrelate AS mr WRITE");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      // Populate the MOCs, unfortunately we need to create the
      // temporary table, because of the mocrelate table.
      cvdl.setSqlQuery("UPDATE entitylist AS el, methodofcontact AS moc, mocrelate AS mr SET el.phone = moc.content WHERE el.entityId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 4 AND mr.contactType = 1 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("UPDATE entitylist AS el, methodofcontact AS moc, mocrelate AS mr SET el.email = moc.content WHERE el.entityId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 1 AND mr.contactType = 1 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("UNLOCK TABLES");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setAutoCommit(previousFlag);
      // unlock tables
      // Now we are cool get the results.
      cvdl.setSqlQuery("SELECT * FROM entitylist");
      entityCollection = cvdl.executeQuery();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE entitylist");
      cvdl.executeUpdate();

      // throw away the temp table, if we did a search.
      if (search)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE entitylistsearch");
        cvdl.executeUpdate();
      }
		}
		catch(Exception e){
			System.out.println("[Exception] ContactListEJB.getAccessEntityList: " + e.toString());
			//e.printStackTrace();
		}
    finally
    {
      cvdl.destroy();
      cvdl = null;
    } //end of finally block
		return entityCollection;
	}


	/**
	* Calls the EJB/database layer to get a list of Individuals
	* and returns a Collection of SQL results to the EJB
	* layer. The Action class which calls this method should
	* take the Collection returned and pass it to the Calling method
	* in order to create a List object for display to the
	* end user.
	*
	* @param individualID  int representation of the IndividualID
	*        of the currently logged in user. Used for record permissions.
	* @param pagingMap HashMap containing all "paging" parameters.
	*        The required values are:
	*        <ul>
	*          <li>recordsPerPage - (Integer) the number of records to show on each page.</li>
	*          <li>currentPage - (Integer) the page number that you would like to view.</li>
	*          <li>sortColumn - (String) the name of the column on which to sort results.</li>
	*        </ul>
	*
	*/
	public Collection getAccessIndividualList(int individualId, HashMap preference)
	{
	  CVDal cvdl = new CVDal(dataSource);
	  Collection individualCollection = null;
	  try{

	  	  boolean adminUserFlag = false;
			
		  cvdl.clearParameters();
		  // get the user type of the given individual id.
		  cvdl.setSql("user.getUserType");
		  cvdl.setInt(1, individualId);
		  Collection userResults = cvdl.executeQuery();
		
		  String userType = new String("");
		  if (userResults != null)
		  {
		  	Iterator userIter = userResults.iterator();
		  	while (userIter.hasNext())
		  	{
		  		HashMap userRow = (HashMap)userIter.next();
		  		if (userRow != null)
		  		{
		  			userType = (String)userRow.get("usertype");
		  			if(userType != null && userType.equals("ADMINISTRATOR")){
		  			  	adminUserFlag = true;
		  		    }
		  			break;
		  		}
		  	}
		  }
		
	      String sortColumn = (String) preference.get("sortColumn");
	      String sortDirection = (String) preference.get("sortDirection");
	      if (sortDirection == null || (!sortDirection.equals("A") && !sortDirection.equals("D")))
	      {
	        sortDirection = "ASC";
	      } //end of if statement (sortDirection == null ...
	      if (sortDirection != null && sortDirection.equals("A"))
	      {
	        sortDirection = "ASC";
	      }
	      if (sortDirection != null && sortDirection.equals("D"))
	      {
	        sortDirection = "DESC";
	      }
	
	      boolean search = false;
	      String searchString = preference.get("ADVANCESEARCHSTRING") == null ? "" : (String) preference.get("ADVANCESEARCHSTRING");
	      if (searchString.startsWith("ADVANCE:"))
	      {
	        // if there is an advancesearchstring then create a temporary table
	        // this way we can join against it.
	        searchString = searchString.substring(8);
	        String str = "create TEMPORARY TABLE individuallistsearch " + searchString;
	        cvdl.setSqlQuery(str);
	        cvdl.executeUpdate();
	        cvdl.setSqlQueryToNull();
	        search = true;
	      } // end if (searchString.startsWith("ADVANCE:"))
	
	      String select = "SELECT i.IndividualID, i.List AS dbid, " +
	        "CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial, i.LastName, i.Title, " +
	        "i.entity AS EntityID, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, " +
	        "CONCAT(a.Street1, ' ', a.Street2, ' ', a.City, ' ', a.State, ' ', a.Zip, a.Country) AS Address, "+
	        "moc.Content AS Phone, moc.Content AS Email, moc.Content AS Fax ";
	
	      String joinConditions =
	        "LEFT OUTER JOIN entity AS e ON (i.Entity = e.EntityID) " +
	        "LEFT OUTER JOIN address a ON (a.AddressID = 0) " +
	        "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) ";
	
	      StringBuffer from = new StringBuffer("FROM individual AS i ");
	      StringBuffer where = new StringBuffer("WHERE 1=1 ");
	
	      Integer listId = preference.get("dbID") == null ? new Integer(0) : (Integer)preference.get("dbID");
	      int list = listId.intValue();
	      // if we are only selecting from a single list the a WHERE criteria
	      // needs to be added.
	      if (list > 0)
	      {
	        where.append("AND i.list = ");
	        where.append(list);
	        where.append(" ");
	      }
	      // if an advanced search query was run we need to do an inner join against the
	      // temporary table
	      if (search)
	      {
	        from.append(", individuallistsearch AS ils ");
	        where.append("AND i.individualId = ils.individualId ");
	      }
	      
	      StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE individuallist ");
	      if(adminUserFlag){
		      query.append(select);
		      query.append(from);
		      query.append(joinConditions);
		      query.append(where);
		      cvdl.setSqlQueryToNull();	
		      cvdl.setSqlQuery(query.toString());
		      cvdl.executeUpdate();
		      cvdl.setSqlQueryToNull();			      
	      }
	      else{
		      // Build up the actual query using all the different permissions.
		      // Where owner = passed individualId
		      query.append(select);
		      query.append(from);
		      query.append(joinConditions);
		      query.append(where);
		      query.append("AND i.owner = ?");
		      // Do the whole query again with public records:
		      query.append(" UNION ");
		      query.append(select);
		      query.append(from);
		      query.append(", publicrecords AS pub ");
		      query.append(joinConditions);
		      query.append(where);
		      query.append("AND pub.recordId = i.individualId AND pub.moduleId = 15");
		      // Do the whole query a final time with recordauthorization
		      query.append(" UNION ");
		      query.append(select);
		      query.append(from);
		      query.append(", recordauthorisation AS auth ");
		      query.append(joinConditions);
		      query.append(where);
		      query.append("AND auth.recordId = i.individualId AND auth.recordTypeId = 15 AND ");
		      query.append("auth.individualId = ? AND auth.privilegeLevel < 40 AND auth.privilegeLevel > 0");
		      cvdl.setSqlQueryToNull();	
		      cvdl.setSqlQuery(query.toString());
		      cvdl.setInt(1, individualId);
		      cvdl.setInt(2, individualId);
		      cvdl.executeUpdate();
		      cvdl.setSqlQueryToNull();			      
	      }

	      // obtain a Lock on the tables here to prevent deadlocks
	      boolean previousFlag = cvdl.getAutoCommit();
	      cvdl.setAutoCommit(false);
	      cvdl.setSqlQuery("LOCK TABLES individuallist AS il WRITE, methodofcontact AS moc WRITE, mocrelate AS mr WRITE");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      // Populate the MOCs, unfortunately we need to create the
	      // temporary table, because of the mocrelate table.
	      cvdl.setSqlQuery("UPDATE individuallist AS il, methodofcontact AS moc, mocrelate AS mr SET il.phone = moc.content WHERE il.individualId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 4 AND mr.contactType = 2 AND mr.isPrimary='YES'");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      cvdl.setSqlQuery("UPDATE individuallist AS il, methodofcontact AS moc, mocrelate AS mr SET il.email = moc.content WHERE il.individualId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 1 AND mr.contactType = 2 AND mr.isPrimary='YES'");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      cvdl.setSqlQuery("UPDATE individuallist AS il, methodofcontact AS moc, mocrelate AS mr SET il.fax = moc.content WHERE il.individualId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 2 AND mr.contactType = 2 AND mr.isPrimary='YES'");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      cvdl.setSqlQuery("UNLOCK TABLES");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      cvdl.setAutoCommit(previousFlag);
	      // unlock tables
	      // Now we are cool get the results.
	
	      // Lets lock the Address table
	
	      // obtain a Lock on the tables here to prevent deadlocks
	      previousFlag = cvdl.getAutoCommit();
	      cvdl.setAutoCommit(false);
	      cvdl.setSqlQuery("LOCK TABLES individuallist AS il WRITE, address AS a WRITE, addressrelate AS ar WRITE");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      // Populate the Address table, unfortunately we need to create the
	      // temporary table, because of the addressrelate table.
	      StringBuffer addressUpdateQuery = new StringBuffer();
	      addressUpdateQuery.append(" UPDATE individuallist AS il, address AS a, addressrelate AS ar SET ");
	      addressUpdateQuery.append(" il.Street1 = a.Street1, il.Street2 = a.Street2, il.City = a.City, ");
		  addressUpdateQuery.append(" il.State = a.State, il.Zip = a.Zip, il.Country = a.Country, ");
		  addressUpdateQuery.append(" il.Address = CONCAT(a.Street1, ' ', a.Street2, ' ', a.City, ' ', a.State, ' ', a.Zip, a.Country) ");
		  addressUpdateQuery.append(" WHERE il.individualId = ar.contact AND ar.Address = a.AddressID AND ar.contactType = 2 AND ar.isPrimary='YES'");
	
	      cvdl.setSqlQuery(addressUpdateQuery.toString());
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      cvdl.setSqlQuery("UNLOCK TABLES");
	      cvdl.executeUpdate();
	      cvdl.setSqlQueryToNull();
	      cvdl.setAutoCommit(previousFlag);
	      // unlock tables
	      // Now we are cool get the results.
	
	      cvdl.setSqlQuery("SELECT * FROM individuallist");
	      individualCollection = cvdl.executeQuery();
	      cvdl.setSqlQueryToNull();
	      cvdl.setSqlQuery("DROP TABLE individuallist");
	      cvdl.executeUpdate();
	
	      // throw away the temp table, if we did a search.
	      if (search)
	      {
	        cvdl.setSqlQueryToNull();
	        cvdl.setSqlQuery("DROP TABLE individuallistsearch");
	        cvdl.executeUpdate();
	      }
	  }
	  catch(Exception e){
		System.out.println("[Exception] ContactListEJB.getAccessIndividualList: " + e.toString());
		//e.printStackTrace();
	  }
      finally
      {
        cvdl.destroy();
        cvdl = null;
      } //end of finally block
	  return individualCollection;
	}

  public ValueListVO getEntityValueList(int individualId, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    //    data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an arraylist
    //    Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();
    // permissionSwitch turns the permission parts of the query on and off.
    // if individualId is less than zero then the list is requested without limiting
    // rows based on record rights.  If it is true than the rights are used.
    boolean permissionSwitch = individualId < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE entitylistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
    {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "entitylistfilter", individualId, 14, "entity", "entityId", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 14, "entity", "entityId", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildEntityListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    // obtain a Lock on the tables here to prevent deadlocks
    try
    {
      boolean autoCommitFlag = cvdl.getAutoCommit();
      cvdl.setAutoCommit(false);
      cvdl.setSqlQuery("LOCK TABLES entitylist AS el WRITE, methodofcontact AS moc WRITE, mocrelate AS mr WRITE");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      // Populate the MOCs, unfortunately we need to create the entitylist
      // temporary table, because of the mocrelate table.
      cvdl.setSqlQuery("UPDATE entitylist AS el, methodofcontact AS moc, mocrelate AS mr SET el.phone = moc.content WHERE el.entityId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 4 AND mr.contactType = 1 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("UPDATE entitylist AS el, methodofcontact AS moc, mocrelate AS mr SET el.Fax = moc.content WHERE el.entityId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 2 AND mr.contactType = 1 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("UPDATE entitylist AS el, methodofcontact AS moc, mocrelate AS mr SET el.email = moc.content WHERE el.entityId = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 1 AND mr.contactType = 1 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("UNLOCK TABLES");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      // unlock tables
      cvdl.setAutoCommit(autoCommitFlag);
      cvdl.setSqlQuery("SELECT * FROM entitylist");
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE entitylist");
      cvdl.executeUpdate();
      // throw away the temp filter table, if necessary.
      if (applyFilter)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE entitylistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
    } catch (SQLException e) {
      logger.error("[getEntityValueList] Exception thrown.", e);
      throw new EJBException(e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }   // end getEntityValueList() method

  private String buildEntityListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    String select = "SELECT e.EntityID, e.Name AS EntityName, e.list AS dbid, e.accountManagerId, " +
      "CONCAT(ami.firstName, ' ', ami.lastName) AS AccountManager, pci.IndividualID, " +
      "CONCAT(pci.FirstName, ' ', pci.LastName) AS Name, mocphone.content AS Phone, " +
      "mocemail.content AS Email, a.WebSite, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, " +
      "CONCAT(a.Street1, ' ', a.Street2, ' ', a.City, ' ', a.State, ' ', a.Zip, ' ', a.Country) AS Address, mocphone.Content AS Fax  ";

    String joinConditions = "LEFT OUTER JOIN individual AS pci ON (e.entityid = pci.entity AND pci.primaryContact = 'YES') " +
      "LEFT OUTER JOIN individual AS ami ON (ami.individualId = e.accountManagerId) " +
      "LEFT OUTER JOIN addressrelate AS ar ON (e.entityId = ar.contact AND ar.isPrimary = 'YES' AND ar.contactType = 1) " +
      "LEFT OUTER JOIN address AS a ON (ar.address = a.addressId) " + "LEFT OUTER JOIN methodofcontact AS mocphone ON (mocphone.mocId = 0) " +
      "LEFT OUTER JOIN methodofcontact AS mocemail ON (mocemail.mocId = 0) ";
      
    StringBuffer from = new StringBuffer("FROM entity AS e ");
    StringBuffer where = new StringBuffer("WHERE 1=1 ");
    String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch)
    {
      from.append(", listfilter AS lf ");
      where.append("AND e.entityId = lf .entityId");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE entitylist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }   // end buildEntityListQuery() method

  /**
   * Returns a ValueListVO representing a list of Individual records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getIndividualValueList(int individualID, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    //    data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an arraylist
    //    Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();

    // permissionSwitch turns the permission parts of the query on and off.
    // if individualID is less than zero then the list is requested without limiting
    // rows based on record rights.  If it is true than the rights are used.
    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      if (filter != null && filter.length() > 0)
      {
        String str = "CREATE TABLE individuallistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      if (applyFilter)
      {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "individuallistfilter", individualID, 15, "individual", "individualId", "owner", null, permissionSwitch);
      } else if (permissionSwitch) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 15, "individual", "individualId", "owner", null, permissionSwitch);
      }
      parameters.setTotalRecords(numberOfRecords);
      String query = this.buildIndividualListQuery(applyFilter, permissionSwitch, parameters);
      cvdl.setSqlQuery(query);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // obtain a Lock on the tables here to prevent deadlocks
      boolean autoCommitFlag = cvdl.getAutoCommit();
      cvdl.setAutoCommit(false);
      cvdl.setSqlQuery("LOCK TABLES individuallist AS il WRITE, methodofcontact AS moc WRITE, mocrelate AS mr WRITE");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // Populate the MOCs, unfortunately we need to create the individuallist
      // temporary table, because of the mocrelate table.
      cvdl.setSqlQuery("UPDATE individuallist AS il, methodofcontact AS moc, mocrelate AS mr SET il.phone=moc.content WHERE il.individualID=mr.contactID AND moc.mocID=mr.mocID AND moc.mocType=4 AND mr.contactType=2 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      cvdl.setSqlQuery("UPDATE individuallist AS il, methodofcontact AS moc, mocrelate AS mr SET il.email=moc.content WHERE il.individualID=mr.contactID AND moc.mocID=mr.mocID AND moc.mocType=1 AND mr.contactType=2 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      cvdl.setSqlQuery("UPDATE individuallist AS il, methodofcontact AS moc, mocrelate AS mr SET il.fax=moc.content WHERE il.individualID=mr.contactID AND moc.mocID=mr.mocID AND moc.mocType=2 AND mr.contactType=2 AND mr.isPrimary='YES'");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // unlock tables
      cvdl.setSqlQuery("UNLOCK TABLES");
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setAutoCommit(autoCommitFlag);

      // Now, Finally, we can just select the individual list and populate value List
      cvdl.setSqlQuery("SELECT * FROM individuallist");
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();

      // drop individuallist table
      cvdl.setSqlQuery("DROP TABLE individuallist");
      cvdl.executeUpdate();
      // throw away the temp filter table, if necessary.
      if (applyFilter)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE individuallistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
    }catch(SQLException e){
      logger.error("[getIndividualValueList] Exception thrown.", e);
      throw new EJBException(e);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }   // end getIndividualValueList() method


  private String buildIndividualListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    String select =
      "SELECT i.IndividualID, i.List AS dbid, " +
      "CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial, i.LastName, i.Title, " +
      "i.entity AS EntityID, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, a.Zip, a.Country, " +
      "CONCAT(a.Street1, ' ', a.Street2, ' ', a.City, ' ', a.State, ' ', a.Zip, a.Country) AS Address, "+
      "moc.Content AS Phone, moc.Content AS Email, moc.Content AS Fax ";

    String joinConditions =
      "LEFT OUTER JOIN entity AS e ON (i.Entity = e.EntityID) " +
      "LEFT OUTER JOIN addressrelate ar ON (i.individualId = ar.contact AND ar.contactType = 2 AND ar.isPrimary='YES') " +
      "LEFT OUTER JOIN address a ON (ar.Address = a.AddressID) " +
      "LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) ";

    StringBuffer from = new StringBuffer("FROM individual AS i ");
    StringBuffer where = new StringBuffer("WHERE 1=1 ");
    String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch)
    {
      from.append(", listfilter AS lf ");
      where.append("AND i.individualId = lf.individualId");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE individuallist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }   // end buildEntityListQuery() method

  /**
   * Returns a ValueListVO representing a list of Group records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getGroupValueList(int individualID, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    //    data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an arraylist
    //    Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();

    // permissionSwitch turns the permission parts of the query on and off.
    // if individualID is less than zero then the list is requested without limiting
    // rows based on record rights.  If it is true than the rights are used.
    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE grouplistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
    {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "grouplistfilter", individualID, 16, "grouptbl", "GroupId", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 16, "grouptbl", "GroupId", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildGroupListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE grouplistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildGroupListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    String select =
      "SELECT gt.GroupId, gt.Name, gt.Description, COUNT(m.ChildID) AS Num ";

    String joinConditions =
      "LEFT OUTER JOIN member m ON gt.GroupId = m.GroupID ";

    StringBuffer from = new StringBuffer("FROM grouptbl AS gt ");
    StringBuffer where = new StringBuffer("WHERE 1 = 1 ");
    String groupBy = "GROUP BY gt.GroupId ";
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch)
    {
      from.append(", listfilter AS lf ");
      where.append("AND gt.groupId = lf.groupId ");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(groupBy);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }
  
  /**
   * Returns a ValueListVO representing a list of Individual email records, based on
   * the <code>parameters</code> argument which limits results.
   */
  // TODO: Get entities in the list too.
  public ValueListVO getEmailLookupValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE emaillookuplistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "emaillookuplistfilter", individualID, 15, "individual", "IndividualId", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 15, "individual", "IndividualId", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildEmailLookupListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE emaillookuplistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildEmailLookupListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    String select =
      "SELECT i.IndividualId AS RecordId, moc.Content AS Email, CONCAT(i.FirstName, ' ', i.LastName) AS Name ";
    StringBuffer from = new StringBuffer("FROM individual i, mocrelate mr, methodofcontact moc ");
    StringBuffer where = new StringBuffer("WHERE i.IndividualID = mr.ContactID AND moc.MOCID = mr.MOCID AND " +
    		"moc.MOCType = 1 AND mr.contactType = 2 ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND i.IndividualId = lf.IndividualId ");
    }
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  public ValueListVO getAddressLookupValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE addresslistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
    }
    int numberOfRecords = 0;
    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "addresslistfilter", individualId, 0, "address", "addressId", "owner", null, false);
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildAddressLookupListQuery(parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE addresslistfilter");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE listfilter");
    cvdl.executeUpdate();
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }
  private String buildAddressLookupListQuery(ValueListParameters parameters)
  {
    String select = "SELECT a.addressId, concat(a.street1,' ', a.street2,' ', a.city,' ', a.state,' ',a.zip,' ', a.country) as Address, a.jurisdictionID ";
    StringBuffer from = new StringBuffer("FROM address AS a, addresstype AS at, listfilter AS lf ");
    StringBuffer where = new StringBuffer("WHERE at.typeId = a.addressType AND a.addressId = lf.addressId ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

}
