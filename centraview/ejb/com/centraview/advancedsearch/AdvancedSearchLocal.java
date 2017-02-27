/*
 * $RCSfile: AdvancedSearchLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:51 $ - $Author: mking_cv $
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

import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBLocalObject;

/**
 * This interface is the local interface
 * for the Advanced Search EJB. All of the
 * public available (locally) methods are defined
 * in this interface.
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public interface AdvancedSearchLocal extends EJBLocalObject
{

  /**
   * Allows the client to set the private dataSource.
   *
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

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
  public HashMap getPrimaryTableForModule(int moduleID);

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
  public HashMap getSearchTablesForModule(int individualID, int moduleID);

  /**
   * Returns a HashMap of fields available
   * for this tableID. The Key of the HashMap
   * is the fieldID. The value is the Display
   * Name of the table.
   *
   * @param tableID The tableID of the table.
   *
   * @return A HashMap of fields available
   * for this tableID.
   */
  public HashMap getSearchFieldsForTable(int individualID, int tableID, int moduleId);

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
  public SearchVO getSavedSearch(int searchID, String searchType, String searchOn);

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
  public int deleteSearchCriteria(int searchCriteriaID);

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
  public int deleteSearch(int searchID);

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
  public int updateSavedSearch(int individualID, SearchVO searchVO);

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
  public int addNewSearch(int individualID, SearchVO searchVO);

  /**
   * Returns a HashMap of Saved Searches owned
   * by a person for a particular module.<p>
   *
   * The Key of the HashMap is the ID of the
   * Saved Search. The value associated
   * to that key is the name of the saved search.
   *
   * @param individualID The Individual ID (owner)
   * of the Searches to look for.
   *
   * @param moduleID The module ID of the module
   * to find the searches for.
   *
   * @return A HashMap of ID/Name value pairs.
   */
  public HashMap getSavedSearchList(int individualID, int moduleID);

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
  public Collection performSearch(int individualID, int searchID, String searchType, String searchOn);

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
  public Collection performSearch(int individualID, SearchVO searchVO);

  public String getWhereClauseForReport(int userId, SearchVO search, String alias);

  /**
   * Returns The ID of the search on basis of module.
   *
   * @param moduleID The module ID
   *
   * @return A Integer The ID of the search on basis of module.
   */
  public Integer getSearchID(int moduleID);

} //end of AdvancedSearchLocal interface
