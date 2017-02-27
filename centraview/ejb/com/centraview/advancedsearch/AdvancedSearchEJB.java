/*
 * $RCSfile: AdvancedSearchEJB.java,v $    $Revision: 1.2 $  $Date: 2005/08/25 17:00:55 $ - $Author: mcallist $
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

 
package com.centraview.advancedsearch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.contact.individual.IndividualLocal;
import com.centraview.contact.individual.IndividualLocalHome;
import com.centraview.contact.individual.IndividualPK;
import com.centraview.contact.individual.IndividualVO;

/**
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public class AdvancedSearchEJB implements SessionBean
{
  /** The SessionContext of this SessionBean. */
  protected SessionContext ctx;

  /** The JNDI/DataSource name this EJB will be using. */
  private String dataSource = "MySqlDS";
  
  private static Logger logger = Logger.getLogger(AdvancedSearchEJB.class);

  /**
   * Set the associated session context. The container calls
   * this method after the instance creation.<p>
   *
   * The enterprise Bean instance should store the reference
   * to the context object in an instance variable. <p>
   *
   * This method is called with no transaction context.
   *
   * @param ctx A SessionContext interface for the instance.
   *
   * @throws EJBException Thrown by the method to indicate a
   * failure caused by a system-level error.
   *
   */
  public void setSessionContext(SessionContext ctx)
    throws EJBException
  {
    this.ctx = ctx;
  } //end of setSessionContext method

  /**
   * The activate method is called when the instance is
   * activated from its "passive" state. The instance
   * should acquire any resource that it has released
   * earlier in the ejbPassivate() method. <p>
   *
   * This method is called with no transaction context.
   *
   * @throws EJBException Thrown by the method to indicate a
   * failure caused by a system-level error.
   *
   * instead of this exception.
   */
  public void ejbActivate()
    throws EJBException
  {
    //Not Implemented
  } //end of ejbActivate method

  /**
   * A container invokes this method before it ends the
   * life of the session object. This happens as a result
   * of a client's invoking a remove operation, or when a
   * container decides to terminate the session object after
   * a timeout. <p>
   *
   * This method is called with no transaction context.
   *
   * @throws EJBException Thrown by the method to indicate a
   * failure caused by a system-level error.
   *
   */
  public void ejbRemove()
    throws EJBException
  {
    //Not Implemented
  } //end of ejbRemove method

  /**
   * The passivate method is called before the instance
   * enters the "passive" state. The instance should
   * release any resources that it can re-acquire later
   * in the ejbActivate() method. <p>
   *
   * After the passivate method completes, the instance
   * must be in a state that allows the container to use the
   * Java Serialization protocol to externalize and store
   * away the instance's state. <p>
   *
   * This method is called with no transaction context.
   *
   * @throws EJBException Thrown by the method to indicate a
   * failure caused by a system-level error.
   *
   */
  public void ejbPassivate()
    throws EJBException
  {
    //Not Implemented
  } //end of ejbPassivate method

  /**
   * The required ejbCreate() method.
   *
   * @throws CreateException An instance of the EJB could not
   * be created.
   */
  public void ejbCreate()
    throws CreateException
  {
    //Not Implemented
  } //end of ejbCreate method

  /**
   * This simply sets the target datasource to be used
   * for DB interaction.
   *
   * @param ds A string that contains the cannonical JNDI
   * name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  } //end of setDataSource method

  /**
   * Returns the primary table information for
   * the primary table of a module. The data is
   * returned in a HashMap with the following
   * fields:
   * <br>
   * TableID : The ID of the Table (from the database). <br>
   * TableDisplayName : The pretty name of the Table. <br>
   * TableName : The name of the Table (from the database) <br>
   * TablePrimaryKey : The primary key of the Table (from the database) <br>
   *
   * @param moduleID The module ID
   *
   * @return A HashMap of information about the primary table of
   * a module.
   */
  public HashMap getPrimaryTableForModule(int moduleID)
  {
    HashMap primaryTableHashMap = new HashMap();
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      String sqlQuery = "SELECT st.SearchTableID, st.DisplayName, "
        + "st.TableName, st.TablePrimaryKey FROM searchtable st,"
        + "searchmodule sm WHERE st.SearchTableID = sm.SearchTableID "
        + "AND sm.IsPrimaryTable = 'Y' AND sm.moduleid = ?";
      cvdal.setSqlQuery(sqlQuery);
      cvdal.setInt(1, moduleID);
      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null)
      {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext())
        {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number tableID = (Number) resultsHashMap.get("SearchTableID");
          String tableDisplayName = (String) resultsHashMap.get("DisplayName");
          String tableName = (String) resultsHashMap.get("TableName");
          String tablePrimaryKey = (String) resultsHashMap.get("TablePrimaryKey");
          primaryTableHashMap.put("TableID", tableID);
          primaryTableHashMap.put("TableDisplayName", tableDisplayName);
          primaryTableHashMap.put("TableName", tableName);
          primaryTableHashMap.put("TablePrimaryKey", tablePrimaryKey);
        } //end of if statement (resultsIterator.hasNext())
      } //end of if statement (resultsCollection != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.getPrimaryTableForModule: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return primaryTableHashMap;
  } //end of getPrimaryTableForModule method

  /**
   * Returns a HashMap of tables available
   * for this moduleID. The Key of the HashMap
   * is the tableID. The value is the Display
   * Name of the table.
   *
   * @param moduleID The moduleID of the module.
   *
   * @return A HashMap of tables available
   * for this moduleID
   */
  public HashMap getSearchTablesForModule(int individualID, int moduleID)
  {
    HashMap tablesHashMap = new HashMap();
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      String sqlQuery = "SELECT st.SearchTableID, st.DisplayName "
        + "FROM searchtable st, searchmodule sm WHERE "
        + "st.SearchTableID = sm.SearchTableID AND sm.moduleid = ?";
      cvdal.setSqlQuery(sqlQuery);
      cvdal.setInt(1, moduleID);
      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null)
      {
        Iterator resultsIterator = resultsCollection.iterator();
        while (resultsIterator.hasNext())
        {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number tableID = (Number) resultsHashMap.get("SearchTableID");
          String tableName = (String) resultsHashMap.get("DisplayName");
          tablesHashMap.put(tableID, tableName);
        } //end of while loop (resultsIterator.hasNext())
      } //end of if statement (resultsCollection != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.getSearchTablesForModule: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block
    return tablesHashMap;
  } //end of getSearchTablesForModule method

  /**
   * Returns a HashMap of fields available
   * for this tableID. The Key of the HashMap
   * is the fieldID. The value is the Display
   * Name of the table.
   *
   * @param tableID The tableID of the table.
   * @param moduleId the moduleId the search is being done for.
   * currently used only for getting the right custom fields.
   * @return A HashMap of fields available
   * for this tableID.
   */
  public HashMap getSearchFieldsForTable(int individualID, int tableID, int moduleId)
  {
    HashMap fieldHashMap = new HashMap();
    CVDal cvdal = new CVDal(this.dataSource);
    Collection resultsCollection = null;
    try
    {
      if (tableID != Integer.parseInt(SearchVO.CUSTOM_FIELD_TABLEID))
      {
        String sqlString = "SELECT SearchFieldID, DisplayName "
          + "FROM searchfield WHERE searchtableid = ?";
        //Note: We will need to revisit field authorization
        //at a later point in time. Right now, the data
        //in modulefieldmapping is too messed up to effectivly
        //handle fieldauthorization with the correct field names
        //in the database.
        cvdal.setSqlQuery(sqlString);
        cvdal.setInt(1, tableID);
        resultsCollection = cvdal.executeQuery();
      } else { // end of if (tableID != Integer.parseInt(CUSTOM_FIELD_TABLEID))
        // we need to get the custom fields, first find the id for cvtable
        // based on the moduleId.
        Number tableId = null;
        String sqlString = "SELECT cv.tableid AS id FROM cvtable AS cv, module "
             + "WHERE module.moduleid = ? AND UPPER(cv.name) = UPPER(module.name)";
        cvdal.setSqlQuery(sqlString);
        cvdal.setInt(1, moduleId);
        Collection tableIds = cvdal.executeQuery();
        if (tableIds != null)
        {
          Iterator tableIdsIterator = tableIds.iterator();
          if (tableIdsIterator.hasNext())
          {
            HashMap tableIdMap = (HashMap)tableIdsIterator.next();
            tableId = (Number)tableIdMap.get("id");
          } // end if (tableIdsIterator.hasNext())
        } // end if (tableIds != null)
        // now get the list of Fields.
        sqlString = "SELECT customfieldid AS SearchFieldID, name AS DisplayName FROM customfield "
          + "WHERE recordtype = ?";
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery(sqlString);
        cvdal.setInt(1, tableId.intValue());
        resultsCollection = cvdal.executeQuery();
      } // end of else (if (tableID != Integer.parseInt(CUSTOM_FIELD_TABLEID)))
      // iterate the results and populate the return HashMap
      if (resultsCollection != null)
      {
        Iterator resultsIterator = resultsCollection.iterator();
        while (resultsIterator.hasNext())
        {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number fieldID = (Number) resultsHashMap.get("SearchFieldID");
          String fieldName = (String) resultsHashMap.get("DisplayName");
          fieldHashMap.put(fieldID, fieldName);
        } //end of while loop (resultsIterator.hasNext())
      } //end of if statement (resultsCollection != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.getSearchFieldsForTable: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return fieldHashMap;
  } //end of getSearchFieldsForTable method

  /**
   * Returns a boolean to determine whether the
   * field is viewable to this individual/user.
   * <code>false</code> is returned if the individual
   * does not have access or if something has
   * gone wrong during this query.
   *
   * @param individualID The ID of the individual
   * getting the fields.
   *
   * @param fieldID The field ID we are checking for.
   *
   * @return true if the user has access to this
   * field, false otherwise.
   */
  private boolean isFieldViewable(int individualID, int fieldID)
  {
    boolean isViewable = false;
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      String privilegeQuery = "SELECT fa.privilegelevel FROM "
        + "fieldauthorisation fa, usersecurityprofile usp, "
        + "cvtable ct, field f, searchfield sf, searchtable st WHERE "
        + "f.tableid = ct.tableid AND f.fieldid = fa.fieldid AND "
        + "fa.profileid = usp.profileid AND usp.individualid = ? AND "
        + "UPPER(f.name) = UPPER(sf.FieldName) AND "
        + "UPPER(cv.name) = UPPER(st.TableName) AND "
        + "(fa.privilegelevel > 0 AND fa.privilegelevel < 40) AND"
        + "sf.SearchTableID = st.SearchTableID AND sf.SearchFieldID = ?";
      cvdal.setSqlQuery(privilegeQuery);
      cvdal.setInt(1, individualID);
      cvdal.setInt(2, fieldID);
      Collection privilegeResults = cvdal.executeQuery();
      cvdal.setSqlQueryToNull();
      if (privilegeResults != null)
      {
        Iterator privilegeIterator = privilegeResults.iterator();
        if (privilegeIterator.hasNext())
        {
          HashMap tableFieldHashMap = (HashMap) privilegeIterator.next();
          Number privilegeLevel = (Number)
            tableFieldHashMap.get("privilegelevel");

          if (privilegeLevel.intValue() > 0
            && privilegeLevel.intValue() < 40)
          {
            isViewable = true;
          } //end of if statement (privilegeLevel.intValue() > 0...
        } //end of if statement (tableFieldIterator.hasNext())
      } //end of if statement (tableFieldResults != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.isFieldViewable: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return isViewable;
  } //end of isFieldViewable method

  /**
   * Adds a new Saved Search to the database. If the
   * search was saved successfully, then the new
   * ID of the search will be returned. If there
   * was a problem adding the search, -1 will
   * be returned.<p>
   *
   * The only values that need to be set in the
   * searchVO is the moduleID, the name and the
   * searchCriteria. The other attributes of the
   * SearchVO will be setup in this method.
   *
   * @param individualID The individualID of the
   * person creating the new search. Must be a
   * valid IndividualID.
   *
   * @param searchVO The SearchVO of the new
   * search object. Only the moduleID, and name
   * need to be set.
   *
   * @return The ID of the newly added search.
   */
  public int addNewSearch(int individualID, SearchVO searchVO)
  {
    int newSearchID = -1;
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      String sqlString = "INSERT INTO search (ModuleID, OwnerID, "
        + "CreatedBy, CreationDate, ModifiedBy, ModifiedDate, SearchName) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
      cvdal.setSqlQuery(sqlString);
      cvdal.setInt(1, searchVO.getModuleID());
      cvdal.setInt(2, individualID);
      cvdal.setInt(3, individualID);
      cvdal.setRealDate(4, new java.sql.Date(new Date().getTime()));
      cvdal.setInt(5, individualID);
      cvdal.setRealTimestamp(6, new Timestamp(new Date().getTime()));
      cvdal.setString(7, searchVO.getName());

      cvdal.executeUpdate();
      newSearchID = cvdal.getAutoGeneratedKey();

      Collection searchCriteria = searchVO.getSearchCriteria();
      if (searchCriteria != null)
      {
        Iterator iterator = searchCriteria.iterator();
        while (iterator.hasNext())
        {
          SearchCriteriaVO criteria = (SearchCriteriaVO) iterator.next();
          this.addNewSearchCriteria(newSearchID, criteria);
        } //end of while loop (iterator.hasNext())
      } //end of if statement (searchCriteria != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.addNewSearch: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return newSearchID;
  } //end of addNewSearch method

  /**
   * Adds a new search criteria to the database.
   * This new search criteria will be related to the
   * searchID passed into the method. <P>
   *
   * The new ID of the search criteria will be
   * returned if all is successful. -1 will
   * be returned if the method fails somewhere.
   *
   * @param searchID The ID of the search to
   * relate this search criteria to.
   *
   * @param searchCriteriaVO The VO of the
   * search criteria. All fields are necessary.
   *
   * @return The ID of the new search criteria.
   * -1 if the search criteria was not added.
   */
  private int addNewSearchCriteria(int searchID, SearchCriteriaVO searchCriteriaVO)
  {
    int newSearchCriteriaID = -1;
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      String sqlString = "INSERT INTO searchcriteria (SearchID, "
        + "SearchTableID, SearchFieldID, ConditionID, ExpressionType, "
        + "Value, CriteriaGroup) VALUES (?, ?, ?, ?, ?, ?, ?)";
      cvdal.setSqlQuery(sqlString);
      cvdal.setInt(1, searchID);
      cvdal.setInt(2, Integer.parseInt(searchCriteriaVO.getTableID()));
      cvdal.setInt(3, Integer.parseInt(searchCriteriaVO.getFieldID()));
      cvdal.setInt(4, Integer.parseInt(searchCriteriaVO.getConditionID()));
      cvdal.setString(5, searchCriteriaVO.getExpressionType());
      cvdal.setString(6, searchCriteriaVO.getValue());
      cvdal.setInt(7, Integer.parseInt(searchCriteriaVO.getGroupID()));

      cvdal.executeUpdate();
      newSearchCriteriaID = cvdal.getAutoGeneratedKey();
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.addNewSearchCriteria: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return newSearchCriteriaID;
  } //end of addNewSearchCriteria method

  /**
   * This method updates an existing Saved Search. It will
   * also update any associated search criteria. If there
   * is a new search criteria in the SearchVO, it will
   * be created.<p>
   *
   * <b>Note</b>: CreatedBy and CreationDate will not
   * be updated in this method. It doesn't make sense to
   * make this updatable.  <p>
   *
   * The number of records affected by the
   * update is returned. It should always be either
   * 0 or 1, if it is something else, something is
   * wrong with the update query.
   *
   * @param individualID The ID of the Individual updating
   * this search.
   *
   * @param searchVO The SearchVO of the updated Search.
   *
   * @return The number of records affected by the
   * update query.
   */
  public int updateSavedSearch(int individualID, SearchVO searchVO)
  {
    int recordsAffected = 0;
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      StringBuffer sqlString = new StringBuffer();
      sqlString.append("UPDATE search SET ModuleID = ?, OwnerID = ?, ");
      sqlString.append("ModifiedBy = ?, ModifiedDate = ?, SearchName = ? WHERE ");
      sqlString.append("SearchID = ?");
      cvdal.setSqlQuery(sqlString.toString());
      cvdal.setInt(1, searchVO.getModuleID());
      cvdal.setInt(2, searchVO.getOwnerID());
      cvdal.setInt(3, individualID);
      cvdal.setRealTimestamp(4, new Timestamp(new Date().getTime()));
      cvdal.setString(5, searchVO.getName());
      cvdal.setInt(6, searchVO.getSearchID());

      recordsAffected = cvdal.executeUpdate();

      Collection searchCriteria = searchVO.getSearchCriteria();
      if (searchCriteria != null)
      {
        ArrayList criteriaIds = new ArrayList();
        Iterator iterator = searchCriteria.iterator();
        while (iterator.hasNext())
        {
          SearchCriteriaVO searchCriteriaVO = (SearchCriteriaVO) iterator.next();
          if (searchCriteriaVO.getSearchCriteriaID().equals(""))
          {
            int newId = this.addNewSearchCriteria(searchVO.getSearchID(), searchCriteriaVO);
            criteriaIds.add(String.valueOf(newId));
          } //end of if statement (searchCriteriaVO.getSearchCriteriaID() <= 0)
          else
          {
            this.updateSearchCriteria(searchCriteriaVO);
            criteriaIds.add(searchCriteriaVO.getSearchCriteriaID());
          } //end of else statement (searchCriteriaVO.getSearchCriteriaID() <= 0)
        } //end of while loop (iterator.hasNext())
        // Delete any Critieria from the database that wasn't included in the
        // VO that was passed in.
        sqlString.replace(0, sqlString.length(), "DELETE FROM searchcriteria WHERE SearchID = ? AND SearchCriteriaID NOT IN (");
        for (int i = 0; i < criteriaIds.size(); i++)
        {
          if (i != 0)
          {
            sqlString.append(", ");
          }
          sqlString.append((String)criteriaIds.get(i));
        } // end for (int i = 0; i < criteriaIds.size(); i++)
        sqlString.append(")");
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery(sqlString.toString());
        cvdal.setInt(1, searchVO.getSearchID());
        cvdal.executeUpdate();
      } //end of if statement (searchCriteria != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.updateSavedSearch: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block
    return recordsAffected;
  } //end of updateSavedSearch method

  /**
   * Updates an existing search criteria in
   * the database. The SearchID is not updatable.<p>
   *
   * The number of records affected by the
   * update is returned. It should always be either
   * 0 or 1, if it is something else, something is
   * wrong with the update query.
   *
   * @param searchCriteriaVO The updated search criteria.
   *
   * @return The number of records affected by the
   * update query.
   */
  private int updateSearchCriteria(SearchCriteriaVO searchCriteriaVO)
  {
    int recordsAffected = 0;

    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      String sqlString = "UPDATE searchcriteria SET SearchTableID = ?, "
        + "SearchFieldID = ?, ConditionID = ?, ExpressionType = ?, "
        + "Value = ?, CriteriaGroup = ? WHERE SearchCriteriaID = ?";
      cvdal.setSqlQuery(sqlString);
      cvdal.setInt(1, Integer.parseInt(searchCriteriaVO.getTableID()));
      cvdal.setInt(2, Integer.parseInt(searchCriteriaVO.getFieldID()));
      cvdal.setInt(3, Integer.parseInt(searchCriteriaVO.getConditionID()));
      cvdal.setString(4, searchCriteriaVO.getExpressionType());
      cvdal.setString(5, searchCriteriaVO.getValue());
      cvdal.setInt(6, Integer.parseInt(searchCriteriaVO.getGroupID()));
      cvdal.setInt(7, Integer.parseInt(searchCriteriaVO.getSearchCriteriaID()));

      recordsAffected = cvdal.executeUpdate();
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.updateSearchCriteria: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block
    return recordsAffected;
  } //end of updateSearchCriteria method

  /**
   * Deletes a specific search from the
   * database. <p>
   *
   * The number of records affected by the
   * delete is returned. It should always be either
   * 0 or 1, if it is something else, something is
   * wrong with the delete query.
   *
   * @param searchID The ID of the search
   * to be deleted.
   *
   * @return The number of records affected by the
   * delete query.
   */
  public int deleteSearch(int searchID)
  {
    int recordsAffected = 0;
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      //There is no need to delete the searchcriteria records
      //they are taken care of with the foriegn key cascade delete.
      String sqlString = "DELETE FROM search WHERE SearchID = ?";
      cvdal.setSqlQuery(sqlString);
      cvdal.setInt(1, searchID);
      recordsAffected = cvdal.executeUpdate();
    } //end of try block
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return recordsAffected;
  } //end of deleteSearch method

  /**
   * Deletes a specific search criteria from the
   * database. <p>
   *
   * The number of records affected by the
   * delete is returned. It should always be either
   * 0 or 1, if it is something else, something is
   * wrong with the delete query.
   *
   * @param searchCriteriaID The ID of the search
   * criteria to be deleted.
   *
   * @return The number of records affected by the
   * delete query.
   */
  public int deleteSearchCriteria(int searchCriteriaID)
  {
    int recordsAffected = 0;
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      String sqlString = "DELETE FROM searchcriteria WHERE SearchCriteriaID = ?";
      cvdal.setSqlQuery(sqlString);
      cvdal.setInt(1, searchCriteriaID);
      recordsAffected = cvdal.executeUpdate();
    } //end of try block
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block
    return recordsAffected;
  } //end of deleteSearchCriteria method

  /**
   * Returns a HashMap of Saved Searches owned by a person for a particular
   * module. <p>The Key of the HashMap is the ID of the Saved Search. The
   * value associated to that key is the name of the saved search.
   * @param individualID The Individual ID (owner) of the Searches to look for.
   * @param moduleID The module ID of the module to find the searches for.
   * @return A HashMap of ID/Name value pairs.
   */
  public HashMap getSavedSearchList(int individualID, int moduleID)
  {
    HashMap searchHashMap = new HashMap();
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String sqlString = "SELECT SearchID, SearchName FROM search WHERE OwnerID = ? AND ModuleID = ?";
      cvdal.setSqlQuery(sqlString);
      cvdal.setInt(1, individualID);
      cvdal.setInt(2, moduleID);
      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null) {
        Iterator iterator = resultsCollection.iterator();
        while (iterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) iterator.next();
          Number searchID = (Number) resultsHashMap.get("SearchID");
          String searchName = (String) resultsHashMap.get("SearchName");
          searchHashMap.put(searchID, searchName);
        }
      }
    } catch(Exception e) {
      System.out.println("[Exception] AdvancedSearchEJB.getSavedSearchList: "  + e.toString());
    } finally {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return searchHashMap;
  }   // end getSavedSearchList() method

  /**
   * Returns the Saved Search from the database
   * based on the searchID.
   *
   * @param searchID The search ID of the Saved Search.
   * @param searchType The searchType is either ADVANCE/SIMPLE.
   * @param searchOn The searchOn is used only we if the SearchType is SIMPLE
   *
   * @return The SearchVO of the Saved Search.
   */
  public SearchVO getSavedSearch(int searchID, String searchType, String searchOn)
  {
		// If we are on Advance search the we have to assess the search table
		// Else we have to assess the simplesearch table
		String searchTable = "search";
		if (searchType != null && searchType.equals("SIMPLE")){
			searchTable = "simplesearch";
		}
    SearchVO searchVO = new SearchVO();
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      String searchQuery = "SELECT SearchID, ModuleID, OwnerID, CreatedBy, "
        + "CreationDate, ModifiedBy, ModifiedDate, SearchName FROM "+searchTable
        + " WHERE SearchID = ?";
      cvdal.setSqlQuery(searchQuery);
      cvdal.setInt(1, searchID);
      Collection searchResultsSet = cvdal.executeQuery();
      if (searchResultsSet != null)
      {
        Iterator searchIterator = searchResultsSet.iterator();
        //Only get the first one
        if (searchIterator.hasNext())
        {
          HashMap searchHashMap = (HashMap) searchIterator.next();
          Number savedSearchID = (Number) searchHashMap.get("SearchID");
          Number moduleID = (Number) searchHashMap.get("ModuleID");
          Number ownerIdObject = (Number)searchHashMap.get("OwnerID");
          Number createdByIdObject = (Number)searchHashMap.get("CreatedBy");
          Number modifiedByIdObject = (Number)searchHashMap.get("ModifiedBy");
          int ownerId = (ownerIdObject == null) ? 0 : ownerIdObject.intValue();
          int createdById = (createdByIdObject == null) ? 0 : createdByIdObject.intValue();
          int modifiedById = (modifiedByIdObject == null) ? 0 : modifiedByIdObject.intValue();
          String searchName = (String) searchHashMap.get("SearchName");

          SimpleDateFormat mysqlDateFormat =
            new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

          searchVO.setSearchID(savedSearchID.intValue());
          searchVO.setName(searchName);
          searchVO.setModuleID(moduleID.intValue());
          searchVO.setOwner(this.getIndividualVO(ownerId));
          searchVO.setOwnerID(ownerId);
          searchVO.setCreatedBy(this.getIndividualVO(createdById));
          searchVO.setCreatedByID(createdById);
          searchVO.setModifiedBy(this.getIndividualVO(modifiedById));
          searchVO.setModifiedByID(modifiedById);

          try {
            String createdDateString = (searchHashMap.get("CreationDate")).toString();
            Date createdDate = mysqlDateFormat.parse(createdDateString);
            searchVO.setCreationDate(createdDate);
          } catch (Exception e) {
            searchVO.setCreationDate(new Date());
          }
          try {
            String modifiedDateString = (searchHashMap.get("ModifiedDate")).toString();
            Date modifiedDate = mysqlDateFormat.parse(modifiedDateString);
            searchVO.setModifiedDate(modifiedDate);
          } catch (Exception e) {
            searchVO.setModifiedDate(new Date());
          }
          searchVO.setSearchCriteria(
            this.getSearchCriteriaForSearch(savedSearchID.intValue(), searchType, searchOn));
        } //end of if statement (searchIterator.hasNext())
      } //end of if statement (searchResultsSet != null)
    } catch (Exception e) {
      logger.error("[getSavedSearch]: Exception", e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }
    return searchVO;
  } //end of getSavedSearch method

  /**
   * Returns a Collection of SearchCriteriaVOs
   * for a specific seach based on the search ID.
   * An empty Collection will be returned if there
   * are no SearchCriteriaVOs related.
   *
   * @param searchID The ID of the search which these
   * SearchCriteriaVOs relate to.
   * @param searchType The searchType is either ADVANCE/SIMPLE.
   * @param searchOn The searchOn is used only we if the SearchType is SIMPLE
   *
   * @return A Collection of SearchCriteriaVOs. An empty
   * Collection if there are no search criteria.
   *
   * @see com.centraview.advancedsearch.SearchCriteriaVO
   */
  private Collection getSearchCriteriaForSearch(int searchID,String searchType, String searchOn)
  {
		// If we are on Advance search the we have to assess the searchcriteria table
		// Else we have to assess the simplesearchcriteria table
		String searchTable = "searchcriteria";
		if (searchType != null && searchType.equals("SIMPLE")){
			searchTable = "simplesearchcriteria";
		}

    Collection searchCriteria = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      String sqlQuery = "SELECT SearchCriteriaID, SearchID, SearchTableID, "
        + "SearchFieldID, ConditionID, ExpressionType, Value, CriteriaGroup "
        + "FROM "+searchTable+" WHERE SearchID = ?";
      cvdal.setSqlQuery(sqlQuery);
      cvdal.setInt(1, searchID);

      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null)
      {
        Iterator resultsIterator = resultsCollection.iterator();
        while (resultsIterator.hasNext())
        {
          SearchCriteriaVO criteriaVO = new SearchCriteriaVO();
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number searchCriteriaID = (Number) resultsHashMap.get("SearchCriteriaID");
          Number searchNumberID = (Number) resultsHashMap.get("SearchID");
          Number searchTableID = (Number) resultsHashMap.get("SearchTableID");
          Number searchFieldID = (Number) resultsHashMap.get("SearchFieldID");
          Number conditionID = (Number) resultsHashMap.get("ConditionID");
          Number groupID = (Number) resultsHashMap.get("CriteriaGroup");
          String expressionType = (String) resultsHashMap.get("ExpressionType");
          String value = (String) resultsHashMap.get("Value");

  				// If we are on SIMPLE search then we have to override the value to the current searchOn
  				if (searchType != null && searchType.equals("SIMPLE")){
        		value = searchOn;
					}

          criteriaVO.setSearchCriteriaID(searchCriteriaID.toString());
          criteriaVO.setSearchID(searchNumberID.toString());
          criteriaVO.setConditionID(conditionID.toString());
          criteriaVO.setExpressionType(expressionType);
          criteriaVO.setFieldID(searchFieldID.toString());
          criteriaVO.setGroupID(groupID.toString());
          criteriaVO.setTableID(searchTableID.toString());
          criteriaVO.setValue(value);

          searchCriteria.add(criteriaVO);
        } //end of while loop (resultsIterator.hasNext())
      } //end of if statement (searchResultsSet != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.getSearchCriteriaForSearch: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return searchCriteria;
  } //end of getSearchCriteriaForSearch method

  /**
   * A private method to return the IndividualVO
   * based on the individualID. This method is
   * not really necessary, but it allows duplicate
   * code not to be created.
   *
   * @param individualID The IndividualVO to search for.
   *
   * @return
   */
  private IndividualVO getIndividualVO(int individualID)
  {
    IndividualVO individualVO = new IndividualVO();
    try
    {
      InitialContext ic = new InitialContext();
      IndividualLocalHome home = (IndividualLocalHome)
        ic.lookup("local/Individual");
      IndividualLocal remote =  home.findByPrimaryKey(
        new IndividualPK(individualID, this.dataSource));
      individualVO = remote.getIndividualVOWithBasicReferences();
    } //end of try block
    catch (Exception e)
    {
      //Give empty information
      individualVO.setContactID(individualID);
      individualVO.setFirstName("");
      individualVO.setLastName("");
      System.out.println("[Exception] AdvancedSearchEJB.getIndividualVO: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    return individualVO;
  } //end of getIndividualVO method

  /**
   * Returns a Collection of IDs after running
   * a specific saved search.
   *
   * @param individualID The ID of the user running
   * the search.
   *
   * @param searchID The ID of the search to run.
   * @param searchType The searchType is either ADVANCE/SIMPLE.
   * @param searchOn The searchOn is used only we if the SearchType is SIMPLE
   *
   * @return A Collection of IDs after running
   * a specific saved search.
   */
  public Collection performSearch(int individualID, int searchID, String searchType, String searchOn)
  {
    SearchVO searchVO = this.getSavedSearch(searchID, searchType, searchOn);
    return this.performSearch(individualID, searchVO);
  } //end of performSearch method

  /**
   * Returns a Collection of IDs after running
   * a specific saved search.
   *
   * @param individualID The ID of the user running
   * the search.
   *
   * @param searchVO A fully populated SearchVO to
   * search with.
   *
   * @return A Collection of IDs after running
   * a specific saved search.
   */
  public Collection performSearch(int individualID, SearchVO searchVO)
  {
    //Note: This method is a mess... please forgive me :(

    //resulting IDs
    Collection resultsCollection = new ArrayList();
    //List of ALL the tables needed for this query
    Collection allTables = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);
    //Key: FieldID, Value: comma seperated IDs.
    HashMap interumResults = new HashMap();

    //Just so I don't have to query the db twice.
    //Key: FieldID, Value: Field Name.
    HashMap searchFields = new HashMap();
    //Just so I don't have to query the db twice.
    //Key: TableID, Value: Table Name.
    HashMap searchTables = new HashMap();

    try
    {
      int numberOfGroups = searchVO.getNumberOfGroups();
      int criteriaSize = searchVO.getNumberOfCriteria();
      String primaryTable = "";
      Number primaryTableID = new Integer(0);
      String primaryKey = "";

      //Get the Primary Table for this module.
      String mainTableQuery = "SELECT st.TablePrimaryKey, st.SearchTableID, "
        + "st.TableName FROM searchtable st, searchmodule sm WHERE "
        + "sm.SearchTableID = st.SearchTableID AND sm.ModuleID = ? "
        + "AND sm.IsPrimaryTable = 'Y'";
      cvdal.setSqlQuery(mainTableQuery);
      cvdal.setInt(1, searchVO.getModuleID());
      Collection mainTableResults = cvdal.executeQuery();
      cvdal.setSqlQueryToNull();
      if (mainTableResults != null)
      {
        Iterator mainTableIterator = mainTableResults.iterator();
        //Only get the first one.
        if (mainTableIterator.hasNext())
        {
          HashMap mainTableHashMap = (HashMap) mainTableIterator.next();
          primaryTable = (String) mainTableHashMap.get("TableName");
          primaryKey = (String) mainTableHashMap.get("TablePrimaryKey");
          primaryTableID = (Number) mainTableHashMap.get("SearchTableID");
        } //end of while loop (mainTableIterator.hasNext())
      } //end of if statement (mainTableResults != null)
      //end of Get the Primary Table for this module.

      //Start Criteria Searches
      Collection criteriaCollection = searchVO.getSearchCriteria();
      if (criteriaCollection != null)
      {
        Iterator criteriaIterator = criteriaCollection.iterator();
        // There are two paths for a criterion.  If it is a standard
        // field we have to do a whole bunch of stuff to build a fancy
        // query.  If it is a custom field then we can rather easily
        // get the data we seek.
        int searchCriteriaCounter = 0;
        while (criteriaIterator.hasNext())
        {
          String thisTable = "";
          String thisRealTable = "";
          String thisPrimaryKey = "";
          String thisField = "";
          String thisRelationship = "";
          SearchCriteriaVO searchCriteria = (SearchCriteriaVO)criteriaIterator.next();
          StringBuffer selectQuery = new StringBuffer();

          if (searchCriteria.getTableID().equals(SearchVO.CUSTOM_FIELD_TABLEID))
          {
            // We are a custom field criteria
            // Find out if we are scalar or multiple.
            String customFieldTypeQuery = "SELECT fieldType FROM customfield "
              + "WHERE customFieldId = ?";
            cvdal.setSqlQuery(customFieldTypeQuery);
            cvdal.setInt(1, Integer.parseInt(searchCriteria.getFieldID()));
            Collection typeResults = cvdal.executeQuery();
            cvdal.setSqlQueryToNull();
            String customFieldType = null;
            if (typeResults != null)
            {
              Iterator typeIterator = typeResults.iterator();
              if (typeIterator.hasNext())
              {
                HashMap type = (HashMap)typeIterator.next();
                customFieldType = (String)type.get("fieldType");
              } // end if (typeResults != null)
            } // end if (typeIterator.hasNext())
            if (customFieldType.equals("SCALAR"))
            {
              selectQuery.append("SELECT cfv.recordId FROM customfieldscalar AS cfv, "
                  + "customfield AS cf WHERE cf.customfieldid = cfv.customfieldid "
                  + "AND cf.customfieldid = ");
            } else {
              selectQuery.append("SELECT cfm.recordId FROM customfieldmultiple AS cfm, "
                  + "customfieldvalue AS cfv, customfield AS cf "
                  + "WHERE cfm.valueid = cfv.valueid AND cf.customfieldid = cfm.customfieldid "
                  + "AND cf.customfieldid = ");
            }
            selectQuery.append(searchCriteria.getFieldID());
            selectQuery.append(" AND cfv.value ");
            String valueCondition = (String) this.getConditionString(
                searchCriteria.getValue(), "value", "cfv",
                Integer.parseInt(searchCriteria.getConditionID()));
            selectQuery.append(valueCondition);
            thisPrimaryKey = "recordId";
            searchTables.put(new Integer(searchCriteria.getTableID()), primaryTable);
            searchFields.put(new Integer(searchCriteria.getFieldID()), primaryKey);
          } else {
            // Gather table information for each criterion
            String tableInfoString = "SELECT TableName, TablePrimaryKey "
              + "FROM searchtable WHERE SearchTableID = ?";
            cvdal.setSqlQuery(tableInfoString);
            cvdal.setInt(1, Integer.parseInt(searchCriteria.getTableID()));
            Collection tableResults = cvdal.executeQuery();
            cvdal.setSqlQueryToNull();
            if (tableResults != null)
            {
              Iterator tableIterator = tableResults.iterator();
              if (tableIterator.hasNext())
              {
                HashMap tableHashMap = (HashMap) tableIterator.next();
                thisTable = (String) tableHashMap.get("TableName");
                thisPrimaryKey = (String) tableHashMap.get("TablePrimaryKey");
                searchTables.put(new Integer(searchCriteria.getTableID()), thisTable);
                searchFields.put(new Integer(searchCriteria.getFieldID()), thisPrimaryKey);
                if (!allTables.contains(thisTable))
                {
                  allTables.add(thisTable);
                } //end of if statement (!tables.contains(tableName))
              } //end of while loop (tableIterator.hasNext())
            } //end of if statement (tableResults != null)

            // Gather Field Information for each criterion

            String fieldInfoString = "SELECT FieldName, "
              + "IsOnTable, RelationshipQuery, RealTableName "
              + "FROM searchfield WHERE SearchFieldID = ?";
            cvdal.setSqlQuery(fieldInfoString);
            cvdal.setInt(1, Integer.parseInt(searchCriteria.getFieldID()));
            Collection fieldResults = cvdal.executeQuery();
            cvdal.setSqlQueryToNull();
            if (fieldResults != null)
            {
              Iterator fieldIterator = fieldResults.iterator();
              if (fieldIterator.hasNext())
              {
                HashMap fieldHashMap = (HashMap) fieldIterator.next();
                String isOnTable = (String) fieldHashMap.get("IsOnTable");
                thisField = (String) fieldHashMap.get("FieldName");
                if (isOnTable.equalsIgnoreCase("N"))
                {
                  thisRealTable = (String) fieldHashMap.get("RealTableName");
                  if (!allTables.contains(thisRealTable))
                  {
                    allTables.add(thisRealTable);
                  } //end of if statement (!tables.contains(tableName))
                  // alias determination

                  Collection alias = new ArrayList();

                  StringTokenizer aliasCommaTokens = new StringTokenizer(thisRealTable, ",");

                  while(aliasCommaTokens.hasMoreTokens()){
						String aliasRealTable = aliasCommaTokens.nextToken();
						StringTokenizer aliasTokens = new StringTokenizer(aliasRealTable, " ");
						int tokenLen = aliasTokens.countTokens();
						if(tokenLen > 1){
							if(aliasTokens.hasMoreTokens())
							{
								String tempTableName = aliasTokens.nextToken();
								alias.add(aliasTokens.nextToken());
							}//end of if(aliasTokens.hasMoreTokens())
						}
						else{
							if(aliasTokens.hasMoreTokens())
							{
								alias.add(aliasTokens.nextToken());
							}
						}
				  }//end of while(aliasCommaTokens.hasMoreTokens())

                  thisRelationship = (String) fieldHashMap.get("RelationshipQuery");
                  if (thisRelationship != null)
                  {
                    StringTokenizer relationshipTokens = new StringTokenizer(thisRelationship, " ");
                    while (relationshipTokens.hasMoreTokens())
                    {
                      String thisToken = relationshipTokens.nextToken();
                      int index = thisToken.indexOf(".");
                      if (index > -1)
                      {
                        String tableName = null;
                        if (thisToken.indexOf("(") == 0) {  // grrrrrrr
                          tableName = thisToken.substring(1, index);
                        } else {
                          tableName = thisToken.substring(0, index);
                        }
                        //Incase if you added the new line to the Query then before
                        //check for the occurance. We will eliminate the new line return character.
                        tableName = tableName.replaceAll("\n","");
                        if ((!allTables.contains(tableName)) && (!alias.contains(tableName)))
                        {
                          allTables.add(tableName);
                        } //end of if statement (!tables.contains(tableName))
                      } //end of if statement (index > -1)
                    } //end of while loop (relationshipTokens.hasMoreTokens())
                  } //end of if statement (thisRelationship != null)
                } //end of if statement (isOnTable.equalsIgnoreCase("N"))
              } //end of if statement (fieldIterator.hasNext())
            } //end of if statement (fieldResults != null)

            if (thisRealTable.length() < 1)
            {
              thisRealTable = thisTable;
            } //end of if statement (thisRealTable.length() < 1)

            // This is the actual interesting part of the WHERE clause

            String valueCondition = (String) this.getConditionString(
              searchCriteria.getValue(), thisField, thisRealTable,
              Integer.parseInt(searchCriteria.getConditionID()));

            // Build the criterion Query
            selectQuery.append("SELECT ");
            selectQuery.append(thisTable);
            selectQuery.append(".");
            selectQuery.append(thisPrimaryKey);
            selectQuery.append(" FROM ");

            Iterator thisTableIterator = allTables.iterator();
            while (thisTableIterator.hasNext())
            {
              String currentTable = (String) thisTableIterator.next();
              selectQuery.append(currentTable);
              if (thisTableIterator.hasNext())
              {
                selectQuery.append(", ");
              } //end of if statement (thisTableIterator.hasNext())
            } //end of while loop (thisTableIterator.hasNext())

            // The actual Question the user is asking about this field:
            StringBuffer fieldCriterion = new StringBuffer();
            //If there is a relationship, this following piece
            //should be setup in the database.
            if (thisRelationship == null || thisRelationship.length() < 1)
            {
              fieldCriterion.append(thisRealTable);
              fieldCriterion.append(".");
            } //end of if statement (thisRelationship == null ...

            fieldCriterion.append(thisField);
            fieldCriterion.append(" ");
            fieldCriterion.append(valueCondition);

            // the following things don't have "record" rights applied
            // they can be skipped and just do this thing.
            if (thisRealTable.equalsIgnoreCase("methodofcontact")
                || thisRealTable.equalsIgnoreCase("customfield")
                || thisRealTable.equalsIgnoreCase("address")
                || (thisRelationship != null && thisRelationship.length() > 0))
            {
              //This is bad.... bad bad bad, but there isn't an easier way
              //to do this.
              selectQuery.append(" WHERE ");
              if (thisRelationship != null && thisRelationship.length() > 0)
              {
                selectQuery.append(thisRelationship);
                selectQuery.append(" AND ");
              } //end of if statement (thisRelationship != null ...

              selectQuery.append(fieldCriterion);

            } //end of if statement (thisTable.equals("methodofcontact"))...
            else
            {
              // Gotta do some record rights crap.
              selectQuery.append(this.getRecordPermissionQuery(individualID,
                thisRealTable, thisPrimaryKey, selectQuery, fieldCriterion,
                searchVO.getModuleID(),allTables));
            } //end of else statement (thisTable.equals("methodofcontact"))...
          } // end else for if (searchCriteria.getTableID().equals(CUSTOM_FIELD_TABLEID))

          // built the query now get iterum results.
          cvdal.setSqlQuery(selectQuery.toString());
          Collection searchResults = cvdal.executeQuery();
          cvdal.setSqlQueryToNull();
          if (searchResults != null)
          {
            StringBuffer commaDelimitedIDs = new StringBuffer("");
            Iterator resultsIterator = searchResults.iterator();
            while (resultsIterator.hasNext())
            {
              HashMap resultsHashMap = (HashMap) resultsIterator.next();
              Number primaryID = (Number) resultsHashMap.get(thisPrimaryKey);
              commaDelimitedIDs.append(primaryID.toString());
              if (resultsIterator.hasNext())
              {
                commaDelimitedIDs.append(", ");
              } //end of if statement (resultsIterator.hasNext())
            } //end of while loop (resultsIterator.hasNext())

            if (commaDelimitedIDs.length() < 1)
            {
              commaDelimitedIDs.append("0");
            } //end of if statement (commaDelimitedIDs.length() < 1)

            // interumResults are now keyed on a unique counter
            interumResults.put(new Integer(searchCriteriaCounter), commaDelimitedIDs.toString());
          } //end of if statement (searchResults != null)
          // whack the tables from this list
          allTables.clear();
          // The guaranteed unique counter is now stored in the collection of searchCriteria
          // which we are working with.
          searchCriteria.setSearchCriteriaID(String.valueOf(searchCriteriaCounter));
          searchCriteriaCounter++;
        } //end of while loop (criteriaIterator.hasNext())
      } //end of if statement (criteriaCollection != null)
      //End Criteria Searches

      // Build the relationship and get out the primary keys we need.
      if (criteriaCollection != null)
      {
        Iterator criteriaIterator = criteriaCollection.iterator();
        while (criteriaIterator.hasNext())
        {
          SearchCriteriaVO searchCriteria = (SearchCriteriaVO) criteriaIterator.next();
          Integer searchCriteriaId = new Integer(searchCriteria.getSearchCriteriaID());
          // See if its worth finding the relationship, etc.  If there is no
          // interum results then there will most definitely be no final results
          String ids = (String)interumResults.get(searchCriteriaId);
          String relationshipQuery = "";
          if (ids != null && ids.length() > 0 && !ids.equals("0")) // oh yeah, and if it equals 0, we can skip it too.
          {
            // if the table for this criteria is not the primary table, then we need to
            // get the relationship query from searchtablerelate, and run a query to
            // reduce back to the primarykey.
            if ((primaryTableID.intValue() != Integer.parseInt(searchCriteria.getTableID()))
                && !(searchCriteria.getTableID().equals(SearchVO.CUSTOM_FIELD_TABLEID)))
            {
              String tableString = "SELECT RelationshipQuery FROM searchtablerelate " +
                  "WHERE (LeftSearchTableID = ? AND RightSearchTableID = ?) " +
                  "OR (LeftSearchTableID = ? AND RightSearchTableID = ?)";
              cvdal.setSqlQuery(tableString);
              cvdal.setInt(1, primaryTableID.intValue());
              cvdal.setInt(2, Integer.parseInt(searchCriteria.getTableID()));
              cvdal.setInt(3, Integer.parseInt(searchCriteria.getTableID()));
              cvdal.setInt(4, primaryTableID.intValue());

              Collection relationCollection = cvdal.executeQuery();
              cvdal.setSqlQueryToNull();
              if (relationCollection != null)
              {
                Iterator relationIterator = relationCollection.iterator();
                if (relationIterator.hasNext())
                {
                  HashMap relationHashMap = (HashMap)relationIterator.next();
                  relationshipQuery = (String)relationHashMap.get("RelationshipQuery");
                  // build up a collection of all the tables referenced in the relationship
                  // query.
                  StringTokenizer relationshipTokens = new StringTokenizer(relationshipQuery, " ");
                  while (relationshipTokens.hasMoreTokens())
                  {
                    String thisToken = relationshipTokens.nextToken();
                    int index = thisToken.indexOf(".");
                    if (index > -1)
                    {
                      String tableName = thisToken.substring(0, index);
                      if (!allTables.contains(tableName))
                      {
                        allTables.add(tableName);
                      } //end of if statement (!tables.contains(tableName))
                    } //end of if statement (index > -1)
                  } //end of while loop (relationshipTokens.hasMoreTokens())
                } //end of if statement (relationIterator.hasNext())
              } //end of if statement (relationCollection != null)
              // Run another query for each criteria that now gets the
              // PrimaryTable.PrimaryKey for each set of interum results.
              // In this way the final query will be done to sort out the
              // ANDs, ORs and GROUPINGS
              if (!allTables.contains(primaryTable))
              {
                allTables.add(primaryTable);
              }
              StringBuffer primaryKeyQuery = new StringBuffer("SELECT ");
              primaryKeyQuery.append(primaryTable + "." + primaryKey);
              primaryKeyQuery.append(" FROM ");
              Iterator tableIterator = allTables.iterator();
              while (tableIterator.hasNext())
              {
                String thisTable = (String) tableIterator.next();
                primaryKeyQuery.append(thisTable);
                if (tableIterator.hasNext())
                {
                  primaryKeyQuery.append(",");
                } //end of if statement (tableIterator.hasNext())
                primaryKeyQuery.append(" ");
              } //end of while loop (tableIterator.hasNext())

              primaryKeyQuery.append(" WHERE ");
              if (!relationshipQuery.equals(""))
              {
                primaryKeyQuery.append(relationshipQuery);
                primaryKeyQuery.append(" AND ");
              }
              primaryKeyQuery.append((String)searchTables.get(new Integer(searchCriteria.getTableID())));
              primaryKeyQuery.append(".");
              primaryKeyQuery.append((String) searchFields.get(new Integer(searchCriteria.getFieldID())));
              primaryKeyQuery.append(" IN ( ");
              primaryKeyQuery.append(ids);
              primaryKeyQuery.append(") ");
              cvdal.setSqlQuery(primaryKeyQuery.toString());
              // This is the collection of primary ids based on the criteria query
              Collection primaryKeyCollection = cvdal.executeQuery();
              cvdal.setSqlQueryToNull();
              if (primaryKeyCollection != null)
              {
                StringBuffer commaDelimitedIDs = new StringBuffer("");
                Iterator resultsIterator = primaryKeyCollection.iterator();
                while (resultsIterator.hasNext())
                {
                  HashMap resultsHashMap = (HashMap)resultsIterator.next();
                  Number primaryID = (Number)resultsHashMap.get(primaryKey);
                  commaDelimitedIDs.append(primaryID.toString());
                  if (resultsIterator.hasNext())
                  {
                    commaDelimitedIDs.append(", ");
                  } //end of if statement (resultsIterator.hasNext())
                } //end of while loop (resultsIterator.hasNext())

                if (commaDelimitedIDs.length() < 1)
                {
                  commaDelimitedIDs.append("0");
                } //end of if statement (commaDelimitedIDs.length() < 1)
                // replace the existing iterumResults with the new improved interumResults
                // Which now are actual primaryKey ids, so the final query will have no
                // joins, only boolean logic and IN operators.
                interumResults.put(searchCriteriaId, commaDelimitedIDs.toString());
              }
              // clear this every time, as we will only be needing it for the preceding query
              allTables.clear();
            } //end if ((primaryTableID.intValue() != Integer.parseInt(searchCriteria.getTableID())) && !(searchCriteria.getTableID().equals(SearchVO.CUSTOM_FIELD_TABLEID)))
          } // end if (ids != null && ids.length() > 0 && !ids.equals("0"))
        } //end of while loop (criteriaIterator.hasNext())
      } //end of if statement (criteriaCollection != null)
      //End of build the relationship

      //Build the final query
      StringBuffer finalQuery = new StringBuffer();
      finalQuery.append("SELECT DISTINCT ");
      finalQuery.append(primaryTable + "." + primaryKey);
      finalQuery.append(" FROM ");
      finalQuery.append(primaryTable);
      finalQuery.append(" WHERE ");
      // count is the number of criteria stuck on the end of this query
      int count = 0;
      Integer[] groupIDs = searchVO.getGroupIDs();
      for (int i = 0; i < groupIDs.length; i++)
      {
        Collection groupCollection = null;
        // just do a seek and destroy mission.  This way is probably better, saves DB access
        // to get the collection of criteria for this group.
        groupCollection = this.getSearchCriteriaForGroup(groupIDs[i].intValue(), criteriaCollection);
        HashMap searchConditions = SearchVO.getConditionOptions();
        if (groupCollection != null)
        {
          boolean firstCriteria = true;
          Iterator groupIterator = groupCollection.iterator();
          while (groupIterator.hasNext())
          {
            SearchCriteriaVO searchCriteria = (SearchCriteriaVO) groupIterator.next();
            String ids = (String)interumResults.get(new Integer(searchCriteria.getSearchCriteriaID()));
            // Check to see if it is worth appending this critieria on the Query.
            // if the interumResults don't exist or there isn't at least 1 id then skip it.
            if (ids != null && ids.length() > 0)
            {
              count++;
              if (i != 0) // For all but first group.
              {
                finalQuery.append(" ");
                finalQuery.append(searchCriteria.getExpressionType().toUpperCase());
                finalQuery.append(" ");
              } //end of if statement (i != 0)

              if (!firstCriteria) // for all but first criteria in a group
              {
                finalQuery.append(" ");
                finalQuery.append(searchCriteria.getExpressionType().toUpperCase());
                finalQuery.append(" ");
              } //end of if statement (!firstCriteria)
              else // The first criteria in a group should open a parens.
              {
                finalQuery.append("(");
                firstCriteria = false;
              } //end of else statement (!firstCriteria)
              finalQuery.append("("); // every criteria is surrounded by individual parens
              finalQuery.append(primaryTable);
              finalQuery.append(".");
              finalQuery.append(primaryKey);
              finalQuery.append(" IN ( ");
              finalQuery.append(ids);
              finalQuery.append(") ");
              finalQuery.append(") ");
              if (!groupIterator.hasNext())
              {
                finalQuery.append(") ");
              } //end of if statement (groupIterator.hasNext())
            } //end of if statement (ids.length() > 0)
          } //end of while loop (groupIterator.hasNext())
        } //end of if statement (groupCollection != null)
      } //end of for loop (int i = 0; i < groupIDs.length; i++)
      //End of build the final query

      // If we haven't appended any criteria
      // we need to make sure we don't get anything from this query
      if (count == 0)
      {
        //Just so we don't get ALL!! of the results.
        finalQuery.append(" 1=0 ");
      }
      //Run the final query
      cvdal.setSqlQueryToNull();
      cvdal.setSqlQuery(finalQuery.toString());
      Collection rawResults = cvdal.executeQuery();
      if (rawResults != null)
      {
        Iterator rawIterator = rawResults.iterator();
        while (rawIterator.hasNext())
        {
          HashMap rawHashMap = (HashMap) rawIterator.next();
          Number primaryID = (Number) rawHashMap.get(primaryKey);
          resultsCollection.add(primaryID);
        } //end of while loop (rawIterator.hasNext())
      } //end of if statement (rawResults != null)
      //End of Run the final query

    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.performSearch: " + e);
      e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.destroy();
      cvdal = null;
    } //end of finally block
    return resultsCollection;
  } //end of performSearch method

  /**
   * Returns the Condition String based on the
   * value and the conditionID. This method can
   * currently handle the following conditions:
   * Begins With, Ends With, Equals, Greater Than,
   * Less Than, and Has (kind of).
   *
   * @param value The Value we are searching for
   *
   * @param field The Field to do the comparison on.
   * Note: only used for the <code>has</code> condition.
   *
   * @param table The Table to do the comparison on.
   * Note: only used for the <code>has</code> condition.
   *
   * @param conditionID The ConditionID.
   *
   * @return The SQL segment needed to run this
   * query. An empty String if an unknown condition
   * is used.
   *
   * @see com.centraview.advancedsearch.SearchVO#getConditionOptions()
   */
  private String getConditionString(String value, String field, String table, int conditionID)
  {
    HashMap conditions = SearchVO.getConditionOptions();
    Integer conditionIntID = new Integer(conditionID);
    String valueCondition = "";
    if (conditions.get(conditionIntID) != null)
    {
      String conditionString = (String) conditions.get(conditionIntID);
      if (conditionString.equals(SearchVO.BEGINS_WITH_STRING))
      {
        valueCondition = "LIKE '" + value + "%' ";
      } //end of if statement (conditionString.equals(...
      else if (conditionString.equals(SearchVO.ENDS_WITH_STRING))
      {
        valueCondition = "LIKE '%" + value + "' ";
      } //end of if statement (conditionString.equals(...
      else if (conditionString.equals(SearchVO.CONTAINS_STRING))
      {
        valueCondition = "LIKE '%" + value+ "%' ";
      } //end of if statement (conditionString.equals(...
      else if (conditionString.equals(SearchVO.EQUALS_STRING))
      {
        valueCondition = "= '" + value + "' ";
      } //end of if statement (conditionString.equals(...
      else if (conditionString.equals(SearchVO.GREATER_THAN_STRING))
      {
        valueCondition = "> '" + value + "' ";
      } //end of if statement (conditionString.equals(...
      else if (conditionString.equals(SearchVO.LESS_THAN_STRING))
      {
        valueCondition = "< '" + value + "' ";
      } //end of if statement (conditionString.equals(...
      else if (conditionString.equals(SearchVO.HAS_STRING))
      {
        //TODO: Figure out how to do do a has.
        valueCondition = "IS NOT NULL OR " + table + "."
          + field + " != 0 OR LENGTH(" + table + "." + field + ") > 0";
      } //end of if statement (conditionString.equals(SearchVO.HAS_STRING))
    } //end of if statement (conditions.get(conditionID) != null)

    return valueCondition;
  } //end of getConditionString method

  /**
   * Builds and returns the RecordPermission segment
   * of the SQL queries in the performSearch method.<p>
   *
   * This code could have been left there, but it would
   * have resulted in duplicate code (which is evil).
   *
   * @param individualID The ID of the individual
   * doing the search.
   *
   * @param realTableName The table we are checking
   * against.
   *
   * @param primaryKey The primary key name we are
   * checking against.
   *
   * @return The SQL segment needed to check the
   * record permissions in this query.
   */
  private String getRecordPermissionQuery(int individualID,
      String realTableName, String primaryKey, StringBuffer initialQuery,
      StringBuffer fieldCriterion, int moduleId, Collection allTabls)
  {
    String recordOwnerField = "";
    String recordOwnerTable = "";
    StringBuffer selectQuery = new StringBuffer();
    CVDal cvdal = new CVDal(this.dataSource);

    try
    {
      // opporunities are activities, so the owner field can be found there.
      if (realTableName.equalsIgnoreCase("opportunity") || realTableName.equalsIgnoreCase("literaturerequest") || realTableName.equalsIgnoreCase("task"))
      {
        selectQuery.append(", activity");
      } //end of if statement (thisRealTable.equalsIgnoreCase(...
      // we don't have the owner column for the Vendor, Employee, Glaccount
      else if (moduleId == 50 ||  moduleId == 54 ||  moduleId == 47){
	    }//end of if statement (moduleId == 50 || ...
      else
      {
        // get the owner field for the module
        String moduleOwnerString = "SELECT primarytable, ownerfield "
          + "FROM module WHERE moduleid = ?";
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery(moduleOwnerString);
        cvdal.setInt(1, moduleId);
        Collection ownerResults = cvdal.executeQuery();
        cvdal.setSqlQueryToNull();
        if (ownerResults != null)
        {
            Iterator ownerIterator = ownerResults.iterator();
            if (ownerIterator.hasNext())
            {
              HashMap ownerHashMap = (HashMap) ownerIterator.next();
              recordOwnerField = (String) ownerHashMap.get("ownerfield");
              recordOwnerTable = (String) ownerHashMap.get("primarytable");
            } //end of if statement (ownerIterator.hasNext())
        } //end of if statement (ownerResults != null)
      } //end of else statement (thisRealTable.equalsIgnoreCase(...

      if ((!allTabls.contains(recordOwnerTable))&&(!recordOwnerTable.equals("")))
        {
        allTabls.add(recordOwnerTable);
        selectQuery.append(", " + recordOwnerTable);
      }
      // Record Owner
      selectQuery.append(" WHERE ");
      if (realTableName.equalsIgnoreCase("opportunity"))
      {
        selectQuery.append("opportunity.activityid = activity.activityid AND ");
        selectQuery.append("opportunity.opportunityid = " + primaryKey);
        selectQuery.append(" AND activity.owner = " + individualID);
        selectQuery.append(" AND ");
      } //end of if statement (thisRealTable.equalsIgnoreCase(...
      else if (realTableName.equalsIgnoreCase("literaturerequest"))
      {
        selectQuery.append("literaturerequest.activityid = activity.activityid AND ");
        selectQuery.append("activity.owner = " + individualID);
        selectQuery.append(" AND ");
      } //end of if statement (thisRealTable.equalsIgnoreCase(...
      else if (realTableName.equalsIgnoreCase("task"))
      {
        selectQuery.append("task.activityid = activity.activityid AND ");
        selectQuery.append(" activity.owner = " + individualID);
        selectQuery.append(" AND ");
      } //end of if statement (thisRealTable.equalsIgnoreCase(...
      // we don't have the owner column for the Vendor, Employee, Glaccount
      else if (moduleId == 50 ||  moduleId == 54 ||  moduleId == 47)
      {
      } //end of if statement (moduleId == 50 || ...
      else
      {
        selectQuery.append(recordOwnerTable + ".");
        selectQuery.append(recordOwnerField + " = " + individualID);
        selectQuery.append(" AND ");
      } //end of else statement (thisRealTable.equalsIgnoreCase(...

      selectQuery.append(fieldCriterion);
      //End Record Owner

      // Public Record
      selectQuery.append(" UNION ");
      selectQuery.append(initialQuery);
      selectQuery.append(", publicrecords WHERE publicrecords.moduleid = ");
      selectQuery.append(moduleId);
      selectQuery.append(" AND publicrecords.recordid = ");
      selectQuery.append(realTableName + "." + primaryKey);
      selectQuery.append(" AND ");
      selectQuery.append(fieldCriterion);
      // End Public Record

      // Record Permissions
      selectQuery.append(" UNION ");
      selectQuery.append(initialQuery);
      selectQuery.append(", recordauthorisation WHERE recordauthorisation.recordid = ");
      selectQuery.append(realTableName + "." + primaryKey);
      selectQuery.append(" AND recordauthorisation.recordtypeid = ");
      selectQuery.append(moduleId);
      selectQuery.append(" AND recordauthorisation.privilegelevel > 0 AND ");
      selectQuery.append("recordauthorisation.privilegelevel < 40 AND ");
      selectQuery.append("recordauthorisation.individualid = " + individualID);
      selectQuery.append(" AND ");
      selectQuery.append(fieldCriterion);
      // End Record Permissions
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] AdvancedSearchEJB.getRecordPermissionString: "
        + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of try block

    return selectQuery.toString();
  } //end of getRecordPermissionString method

  /**
   * This method is used Exclusively by the Report Facade
   * to run a search and provide the requisite WHERE clause to
   * filter against matching results.
   * @param userId
   * @param search
   * @return
   */
  public String getWhereClauseForReport(int userId, SearchVO search, String alias)
  {
    StringBuffer where = new StringBuffer("");
    try
    {
      // Only perform the search if there actually is one to run.
      if (search != null && search.getSearchCriteria().size() > 0)
      {
        String primaryKey = "";
        String primaryTable = "";

        Collection col = this.performSearch(userId,search);
        primaryKey = ((HashMap)this.getPrimaryTableForModule(search.getModuleID())).get("TablePrimaryKey").toString();
        primaryTable = ((HashMap)this.getPrimaryTableForModule(search.getModuleID())).get("TableName").toString();
        // For some standard reports we had to hardcode the alias for the primary table.
        if(alias == null || alias.equals(""))
        {
          alias = primaryTable;
        }
        if (col.size() > 0)
        {
          where.append(" AND ");
          where.append(primaryTable + "." + primaryKey);
          where.append(" in (");
          boolean first = true;
          for (Iterator it = col.iterator(); it.hasNext(); ) {
            if (first) {
              first = false;
            }
            else {

              where.append(", ");
            }
            where.append(it.next().toString());
          }
          where.append(") ");
        } else { // nothing matched the search, so we don't get any results in our report.
          where.append( " AND 1=0 ");
        }
      } // end if (search.getSearchID() > 0)
    } catch (Exception e) {
      System.out.println("[Exception][ReportFacadeEJB.getQueryForResultReport] Exception Thrown: "+e);
    }
    return where.toString();
  }

 /**
  * This Method Iterates the passed in collection of SearchCriteriaVO
  * and returns Collection of criteria that belong to the group that was
  * pased in.
  * @param groupId
  * @param criteria
  * @return Collection of criteria for the group.
  */
 private Collection getSearchCriteriaForGroup(int groupId, Collection criteria)
 {
   ArrayList matchingCriteria = new ArrayList();
   if (criteria != null)
   {
     Iterator criteriaIterator = criteria.iterator();
     while (criteriaIterator.hasNext())
     {
       SearchCriteriaVO currentCriterion = (SearchCriteriaVO)criteriaIterator.next();
       if (currentCriterion.getGroupID().equals(String.valueOf(groupId)))
       {
         matchingCriteria.add(currentCriterion);
       }
     }
   }
   return matchingCriteria;
 }


  /**
   * Returns The ID of a simple search on basis of module.
   *
   * @param moduleID The module ID
   *
   * @return A Integer The ID of the search on basis of module.
   */
  public Integer getSearchID(int moduleID)
  {
	  Integer searchID = new Integer(0);
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      String sqlQuery = "SELECT SearchID FROM simplesearch WHERE ModuleID = ?";
      cvdal.setSqlQuery(sqlQuery);
      cvdal.setInt(1, moduleID);
      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null)
      {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext())
        {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          int searchId = ((Number) resultsHashMap.get("SearchID")).intValue();
          searchID = new Integer(searchId);
        } //end of if statement (resultsIterator.hasNext())
      } //end of if statement (resultsCollection != null)
    } //end of try block
    catch (Exception e)
    {
      logger.error("[getSearchID] Exception thrown.", e);
    } //end of catch block (Exception)
    finally
    {
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    } //end of finally block

    return searchID;
  } //end of getSearchID method

} //end of AdvancedSearchEJB class
