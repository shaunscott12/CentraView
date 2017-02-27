/*
 * $RCSfile: DisplaySearchForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:51 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.menu.LeftNavigation;
import com.centraview.common.menu.NestedMenuItem;
import com.centraview.settings.Settings;

/**
 * This extention of Action will generate the Advanced Search
 * Form so the user can edit it and perform or save the search.
 * It also generates the LeftNav we may want to abstract out the left nav
 * generation in the future.
 *
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class DisplaySearchForm extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    // Get the datasource to be passed on to the EJB layer so we
    // all are talking about the same database.
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    // Need the userobject so we know who we are.
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    DynaActionForm advancedSearchForm = (DynaActionForm)form;
    // moduleId should be set on the request by the Dispatch.
    String moduleIdString = (String)request.getAttribute(AdvancedSearchDispatch.MODULEID);
    // if moduleId isn't passed in, use the one on the form bean which defaults
    // to Entities
    int moduleId = moduleIdString != null ? Integer.parseInt(moduleIdString) : ((Integer)advancedSearchForm.get("moduleId")).intValue();
    // make sure the one we finally decided on, is on the form
    advancedSearchForm.set("moduleId", new Integer(moduleId));

    // set the listScope parameter back to the list
    String listScope = request.getParameter("listScope");
    if (listScope != null) {
      advancedSearchForm.set("listScope", listScope);
    }

    // New value list or not
    String vl = request.getParameter("valueList");
    if ((vl != null) && (vl.equals("true"))) {
      advancedSearchForm.set("valueList", vl);
    }

    // Get Saved searches from the EJB
    AdvancedSearch remoteAdvancedSearch = null;
    try {
      AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
      remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);
    } catch (Exception e) {
      System.out.println("[Exception][SearchForm.execute] Exception Thrown getting EJB connection: " + e);
      throw new ServletException(e);
    }
    HashMap savedSearches = remoteAdvancedSearch.getSavedSearchList(individualId, moduleId);
    // check the parameters to see if a particular searchId was passed in
    String searchIdParameter = (String)request.getAttribute(AdvancedSearchDispatch.SEARCHID);
    String selectedSearchId = searchIdParameter != null ? searchIdParameter : "0";

    // Do we want a fresh one?
    String createNew = (String)advancedSearchForm.get("createNew");
    if (createNew.equals("true")) {
      advancedSearchForm.initialize(mapping);
      selectedSearchId = "0";
      advancedSearchForm.set("moduleId", new Integer(moduleId));
    }

    // I now have the list of saved searches, lets populate the menu bean.
    // The Ids are stored as Numbers on the hashmap
    NestedMenuItem primaryMenuItem = new NestedMenuItem("Saved Searches", "/advancedsearch/dispatch.do?resetForm=true", true);
    ArrayList secondaryMenuItems = new ArrayList();
    Set savedSearchIds = savedSearches.keySet();
    Iterator idIterator = savedSearchIds.iterator();
    while (idIterator.hasNext()) {
      Number searchId = (Number)idIterator.next();
      String searchName = (String)savedSearches.get(searchId);
      boolean selected = false;
      if (selectedSearchId.equals(searchId.toString())) {
        selected = true;
      }
      secondaryMenuItems.add(new NestedMenuItem(searchName, "/advancedsearch/dispatch.do?searchId=" + searchId.toString(), selected));
    }
    primaryMenuItem.setItems(secondaryMenuItems);
    ArrayList primaryMenuItems = new ArrayList();
    primaryMenuItems.add(primaryMenuItem);
    LeftNavigation leftNav = new LeftNavigation("Advanced Search", primaryMenuItems);
    request.setAttribute("leftNav", leftNav);

    // Alright, the leftnav is good to go.
    // Now for the real meat of it.

    // If a search was selected then get the search from the EJB layer
    SearchVO savedSearch = null;
    if (!(selectedSearchId.equals("0") || selectedSearchId.equals(""))) {
      savedSearch = remoteAdvancedSearch.getSavedSearch(Integer.parseInt(selectedSearchId), "ADVANCE", null);
    }

    // if we have a SearchVO then populate the formbean with its contents.
    // Otherwise we will show a blank form.  A clean slate.  A new beginning.
    SearchCriteriaVO[] searchCriteria = (SearchCriteriaVO[])advancedSearchForm.get("searchCriteria");
    if (savedSearch != null) {
      Collection searchCriteriaCollection = savedSearch.getSearchCriteria();
      searchCriteria = (SearchCriteriaVO[])searchCriteriaCollection.toArray(new SearchCriteriaVO[searchCriteriaCollection.size()]);
      advancedSearchForm.set("searchId", String.valueOf(savedSearch.getSearchID()));
      advancedSearchForm.set("searchName", savedSearch.getName());
      advancedSearchForm.set("ownerId", String.valueOf(savedSearch.getOwnerID()));
    }

    HashMap allFields = (HashMap)advancedSearchForm.get("allFields");
    // Only repopulate all the lists if it is empty
    if (allFields == null || allFields.isEmpty()) {
      // Build dem drop downs
      HashMap tableMap = remoteAdvancedSearch.getSearchTablesForModule(individualId, moduleId);
      HashMap conditionMap = SearchVO.getConditionOptions();
      // Build an ArrayList of LabelValueBeans
      ArrayList tableList = AdvancedSearchUtil.buildSelectOptionList(tableMap);
      ArrayList conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
      advancedSearchForm.set("tableList", tableList);
      advancedSearchForm.set("conditionList", conditionList);

      // Get all the appropriate field lists and stick them on the formbean
      // The fieldList (ArrayList of LabelValueBean) will be stored in a
      // HashMap with the key being the (Number)tableId
      TreeSet keySet = new TreeSet(tableMap.keySet());
      Iterator keyIterator = keySet.iterator();
      allFields = new HashMap();
      while (keyIterator.hasNext()) {
        Number key = (Number)keyIterator.next();
        // iterate the tables and get all the field lists
        // stick them in a hashmap of arraylists on the form bean.
        HashMap tableFields = remoteAdvancedSearch.getSearchFieldsForTable(individualId, key.intValue(), moduleId);
        ArrayList tableFieldList = AdvancedSearchUtil.buildSelectOptionList(tableFields);
        allFields.put(key, tableFieldList);
      } // end while(keyIterator.hasNext())
      advancedSearchForm.set("allFields", allFields);
    } // end if (allFields == null || allFields.isEmpty())

    // See if we should add a row.
    String addRow = (String)advancedSearchForm.get("addRow");
    if (addRow.equals("true")) {
      searchCriteria = AdvancedSearchUtil.addRow(searchCriteria);
      advancedSearchForm.set("addRow", "false");
    }
    String removeRow = (String)advancedSearchForm.get("removeRow");
    if (!removeRow.equals("false")) {
      searchCriteria = AdvancedSearchUtil.removeRow(searchCriteria, removeRow);
      advancedSearchForm.set("removeRow", "false");
    }

    advancedSearchForm.set("searchCriteria", searchCriteria);
    String forwardName = null;
    switch(moduleId) {
      case 14: 
      case 15:
      case 16:
        forwardName = ".view.advancedsearch.contacts"; break;
      case 2:
      case 79:
        forwardName = ".view.advancedsearch.email"; break;
      case 3: forwardName = ".view.advancedsearch.activities"; break;
      case 6: forwardName = ".view.advancedsearch.files"; break;
      case 5: forwardName = ".view.advancedsearch.notes"; break;
      case 30:
      case 31:
        forwardName = ".view.advancedsearch.sales"; break;
      case 32:
      case 33:
      case 34:
      case 35:
        forwardName = ".view.advancedsearch.marketing"; break;
      case 36:
      case 37:
      case 38:
        forwardName = ".view.advancedsearch.projects"; break;
      case 39:
      case 40:
      case 41:
        forwardName = ".view.advancedsearch.support"; break;
      case 42:
      case 56:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 50:
        forwardName = ".view.advancedsearch.accounting"; break;
      case 51:
      case 52:
      case 53:
      case 54:
        forwardName = ".view.advancedsearch.hr"; break;
      default: 
        forwardName = ".view.advancedsearch.contacts"; break;
    }
    String forwardPath = mapping.findForward("searchForm").getPath();
    ActionForward forward = new ActionForward(forwardName, forwardPath, false);
    return forward;
  } // end execute()
} // end DisplaySearchForm
