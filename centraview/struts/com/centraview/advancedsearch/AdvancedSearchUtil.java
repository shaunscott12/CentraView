/*
 * $RCSfile: AdvancedSearchUtil.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.common.CVUtility;

/**
 * This class will provide static methods for Advanced Search to be able to get
 * stuff to populate the searchBar. Eventually the goal is to deprecate it, by
 * sticking a bean on the request that the searchbar jsp will parse and print
 * out data.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class AdvancedSearchUtil {
  private static Logger logger = Logger.getLogger(AdvancedSearchUtil.class);

  public static String getModuleId(String moduleName, String dataSource) throws Exception
  {
    Authorization remoteAuthorization = null;
    try {
      AuthorizationHome AuthorizationHome = (AuthorizationHome) CVUtility.getHomeObject(
          "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
      remoteAuthorization = AuthorizationHome.create();
      remoteAuthorization.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[getModuleId]: Exception getting EJB connection.", e);
      throw new Exception(e);
    }

    String moduleId = remoteAuthorization.getModuleIdByPrimaryTable(moduleName);
    return moduleId;
  } // end getModuleId

  public static ArrayList getSavedSearchList(int individualId, int moduleId, String dataSource) throws Exception
  {
    ArrayList searchList = new ArrayList();
    AdvancedSearch remoteAdvancedSearch = null;
    try {
      AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome) CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome",
          "AdvancedSearch");
      remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[getSavedSearchList]: Exception thrown getting EJB connection.", e);
      throw new Exception(e);
    }
    HashMap savedSearches = remoteAdvancedSearch.getSavedSearchList(individualId, moduleId);
    Set savedSearchIds = savedSearches.keySet();
    Iterator idIterator = savedSearchIds.iterator();
    while (idIterator.hasNext()) {
      Number searchId = (Number) idIterator.next();
      String searchName = (String) savedSearches.get(searchId);
      searchList.add(new LabelValueBean(searchName, searchId.toString()));
    }
    return searchList;
  } // getSavedSearchList(int individualId, int moduleId)

  /**
   * Takes a hashmap iterates the keys and builds an ArrayList of
   * LabelValueBeans to use in an optioncollection.
   * @param map
   * @return ArrayList of <code>LabelValueBean</code> Where the Label is the
   *         Value from the HashMap and the Value is the corresponding Key from
   *         the HashMap.
   */
  public static ArrayList buildSelectOptionList(HashMap map)
  {
    ArrayList optionList = new ArrayList();
    TreeSet keySet = new TreeSet(map.keySet());
    Iterator keyIterator = keySet.iterator();
    // prepend an empty 0 label to the top of each OptionList
    optionList.add(new LabelValueBean("-- Select --", "0"));
    while (keyIterator.hasNext()) {
      Number key = (Number) keyIterator.next();
      String value = (String) map.get(key);
      LabelValueBean option = new LabelValueBean(value, key.toString());
      optionList.add(option);
    }
    return optionList;
  } // end buildSelectOptionList(HashMap map)

  public static SearchCriteriaVO[] addRow(SearchCriteriaVO[] currentCriteria)
  {
    SearchCriteriaVO[] tmpCriteria = new SearchCriteriaVO[currentCriteria.length + 1];
    // populate the newest one, copy in the rest.
    tmpCriteria[tmpCriteria.length - 1] = new SearchCriteriaVO();
    System.arraycopy(currentCriteria, 0, tmpCriteria, 0, currentCriteria.length);
    return tmpCriteria;
  } // end addRow()

  public static SearchCriteriaVO[] removeRow(SearchCriteriaVO[] currentCriteria, String rowIndexString)
  {
    int rowIndex = Integer.parseInt(rowIndexString);
    SearchCriteriaVO[] tmpCriteria = new SearchCriteriaVO[currentCriteria.length - 1];

    // squeeze all the elements down.
    int numberMoved = currentCriteria.length - rowIndex - 1;
    if (numberMoved > 0) {
      System.arraycopy(currentCriteria, rowIndex + 1, currentCriteria, rowIndex, numberMoved);
    }
    // Then copy length-1 elements the squeezed down array into a more
    // appropriately sized array.
    System.arraycopy(currentCriteria, 0, tmpCriteria, 0, tmpCriteria.length);
    return tmpCriteria;
  } // end removeRow()

  /**
   * This method builds the Advanced search query based on the moduleID and the
   * primaryIDs. <br>
   * The primaryIDs MUST be a list of comma seperated IDs. If not, the search
   * will not work.
   * @param moduleID The moduleID we are searching in.
   * @param primaryIDs A comma seperated list of IDs.
   * @return The Advanced Query String which is used by the ListGenerator to get
   *         the list of objects.
   * @throws ServletException There was a problem connecting to the EJB layer.
   */
  protected static String buildAdvancedSearchQuery(int moduleID, String primaryIDs, String dataSource) throws ServletException
  {
    StringBuffer queryString = new StringBuffer();

    try {
      AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome) CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome",
          "AdvancedSearch");
      AdvancedSearch remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);

      HashMap primaryTableInformation = remoteAdvancedSearch.getPrimaryTableForModule(moduleID);

      String primaryTable = (String) primaryTableInformation.get("TableName");
      String primaryKey = (String) primaryTableInformation.get("TablePrimaryKey");

      if (primaryTable == null || primaryKey == null) {
        throw new Exception("Could not retrieve the Primary Table and Primary Key for this module.");
      } // end of if statement (primaryTable == null || primaryKey == null)

      queryString.append("ADVANCE: SELECT ");
      queryString.append(primaryTable + "." + primaryKey);
      queryString.append(" FROM ");
      queryString.append(primaryTable);
      queryString.append(" WHERE ");
      queryString.append(primaryTable + "." + primaryKey);
      queryString.append(" IN (");
      queryString.append(primaryIDs);
      queryString.append(")");

    } // end of try block
    catch (Exception e) {
      logger.error("[Exception] AdvancedSearchPerform.buildAdvancedSearchQuery ", e);
      throw new ServletException(e);
    } // end of catch block (Exception)

    return queryString.toString();
  }

  /**
   * Simply takes an array list of ids and converts to a single string of comma
   * separated results.
   * @param results
   * @param primaryIds
   */
  public static void parseResults(ArrayList results, StringBuffer primaryIds)
  {
    for (int i = 0; i < results.size(); i++) {
      if (i > 0) {
        primaryIds.append(", ");
      } // end of if statement (i > 0)
      primaryIds.append(results.get(i));
    } // end for (int i = 0; i < results.size(); i++)

    // If there are no ids, add a 0, so the query still works.
    if (primaryIds.length() < 1) {
      primaryIds.append("0");
    } // end of if statement (primaryIds.length() < 1)
  }
} // end class AdvancedSearchUtil
