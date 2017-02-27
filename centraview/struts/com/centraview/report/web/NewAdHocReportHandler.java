/*
 * $RCSfile: NewAdHocReportHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:38 $ - $Author: mking_cv $
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
 
package com.centraview.report.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportVO;
import com.centraview.report.valueobject.SelectVO;
import com.centraview.settings.Settings;

/**
 * <code>NewAdHocReportHandler</code> Handler to forward to new_adhoc_report
 * JSP It will be called when a user clicks on the New button from the Ad-Hoc
 * Report List.
 */
public class NewAdHocReportHandler extends ReportAction
{
  private static Logger logger = Logger.getLogger(NewAdHocReportHandler.class);
  /**
   * This is overridden method from Action class
   * This handler is posted against every time actions with the Saved
   * search form are performed, including manipulating the rows, etc.
   * 
   * @param actionMapping ActionMapping
   * @param actionForm ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ActionForward
   */
  public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {

    String nextURL = "shownewadhocreport";
    int moduleId = getModuleId(request);
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualId = userObject.getIndividualID();
      String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
      DynaActionForm adhocReportForm = (DynaActionForm)actionForm;

      SelectVO selects = new SelectVO();
      ReportFacade remote = getReportFacade();
      // Get the table list from the reportFacade
      // The table list is an ArrayList of TheTable objects, which contain
      // an ID an table name and the Display name.
      selects = remote.getAdHocPageData(moduleId);

      // createNew is a string attribute that tells this handler if it should clear
      // the form.
      String createNew = (String)adhocReportForm.get("createNew");
      if (createNew.equals("true")) {
        adhocReportForm.initialize(actionMapping);
      }
      // make sure the right moduleId is on the form.
      adhocReportForm.set("moduleId", new Integer(moduleId));

      AdvancedSearch remoteAdvancedSearch = null;
      try {
        AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
        remoteAdvancedSearch = advancedSearchHome.create();
        remoteAdvancedSearch.setDataSource(dataSource);
      } catch (Exception e) {
        System.out.println("[Exception][SearchForm.execute] Exception Thrown getting EJB connection: " + e);
        throw new ServletException(e);
      }
      // because we can get reposted get the current criteria from the form.
      // As we may be manipulating the searchCriteria, adding rows, and such. 
      SearchCriteriaVO[] searchCriteria = (SearchCriteriaVO[])adhocReportForm.get("searchCriteria");

      // Build dem drop downs
      // if necessary.
      HashMap allFields = (HashMap)adhocReportForm.get("allFields");
      ArrayList tableList = (ArrayList)adhocReportForm.get("tableList");
      ArrayList conditionList = (ArrayList)adhocReportForm.get("conditionList");
      if (conditionList == null || conditionList.isEmpty()) {
        // This will get us the possible search conditions.
        HashMap conditionMap = SearchVO.getConditionOptions();
        // Build an ArrayList of LabelValueBeans
        conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
        // stick it on the form
        adhocReportForm.set("conditionList", conditionList);
      }
      if (tableList == null || tableList.isEmpty()) {
        HashMap tableMap = remoteAdvancedSearch.getSearchTablesForModule(individualId, moduleId);
        // Build an ArrayList of LabelValueBeans
        tableList = AdvancedSearchUtil.buildSelectOptionList(tableMap);
        adhocReportForm.set("tableList", tableList);

        // if we had to add get the tableList we more than likely
        // have to build the allFields map, so its probably worth the zillion
        // cpu cycles we will have to waste.

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
        adhocReportForm.set("allFields", allFields);
      }

      // See if we need to add or remove rows from the search criteria array.
      // and do it.
      String addRow = (String)adhocReportForm.get("addRow");
      if (addRow.equals("true")) {
        searchCriteria = AdvancedSearchUtil.addRow(searchCriteria);
        adhocReportForm.set("addRow", "false");
      }
      String removeRow = (String)adhocReportForm.get("removeRow");
      if (!removeRow.equals("false")) {
        searchCriteria = AdvancedSearchUtil.removeRow(searchCriteria, removeRow);
        adhocReportForm.set("removeRow", "false");
      }
      // now that the criteria is ready stick it on the formbean.
      adhocReportForm.set("searchCriteria", searchCriteria);

      ReportVO reportVO = new ReportVO();
      reportVO.setSelect(selects);
      // This parses the fields from the form bean and builds up an arraylist of 
      // ReportContentVOs which represent what is selected.  This is then stuck on the
      // reportVO which is stuck on the request
      reportVO.setSelectedFields(getSelectedFieldsWithNames((String)adhocReportForm.get("contentFields"), (String)adhocReportForm.get("contentOrders"), (String)adhocReportForm.get("contentFieldNames")));
      request.setAttribute("pagedata", reportVO);
      // I think this should probably live on the adhocreportform, rather than worrying
      // about something on the request.  But here it is for now.
      request.setAttribute("reportType", String.valueOf(ReportConstants.ADHOC_REPORT_CODE));
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    String forwardName = "";
    switch (moduleId) {
      case 14: forwardName = ".view.reports.entities.adhoc"; break;
      case 15: forwardName = ".view.reports.individuals.adhoc"; break;
      case 3: forwardName = ".view.reports.activities.adhoc"; break;
      case 30: forwardName = ".view.reports.opportunities.adhoc"; break;
      case 31: forwardName = ".view.reports.proposals.adhoc"; break;
      case 33: forwardName = ".view.reports.promotion.adhoc"; break;
      case 36: forwardName = ".view.reports.project.adhoc"; break;
      case 37: forwardName = ".view.reports.task.adhoc"; break;
      case 38: forwardName = ".view.reports.timeslip.adhoc"; break;
      case 39: forwardName = ".view.reports.ticket.adhoc"; break;
      case 42: forwardName = ".view.reports.order.adhoc"; break;
      case 48: forwardName = ".view.reports.inventory.adhoc"; break;
      case 52: forwardName = ".view.reports.timesheet.adhoc"; break;
      default: forwardName = ".view.reports.entities.adhoc"; break;
    }
    String path = actionMapping.findForward(nextURL).getPath();
    ActionForward forward = new ActionForward(forwardName, path, false);
    return forward;
  }
}