/*
 * $RCSfile: ViewAdHocReportHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:39 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.ejb.session.SessionBeanFactory;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportResultVO;
import com.centraview.settings.Settings;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListRow;
import com.centraview.valuelist.ValueListVO;

/**
 * <code>ViewAdHocReportHandler</code> Handler handles run report and
 * view results actions for AdHoc Report.
 */

public class ViewAdHocReportHandler extends ReportAction
{
  private static Logger logger = Logger.getLogger(ViewAdHocReportHandler.class);
  /**
   * This is overridden method  from Action class
   *
   * @param actionMapping ActionMapping
   * @param actionForm ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ActionForward
   */
  public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    HttpSession session = request.getSession(true);
    String nextURL = "showadhocreportresult";
    DynaActionForm adHocReportForm = (DynaActionForm)actionForm;

    UserObject userobject = (UserObject)session.getAttribute("userobject");
    int userId = userobject.getIndividualID();
    int reportId = 0;
    int moduleId = getModuleId(request);

    reportId = getTheId("reportId", request);
    // if we are coming from one of the form pages, we should have
    // a searchVO on the form, if so pass it on.
    SearchVO search = getSearchVOFromForm(actionForm, request);

    ReportResultVO reportResults = getReportResults(userId, reportId, search);
    String action = request.getParameter("action");
    if (action != null) {
      if (action.equals("export")) {
        nextURL = "csvadhocreportresult";
        request.setAttribute("reportResults", reportResults);
      } else if (action.equals("print")) {
        nextURL = "printadhocreportresult";
      }
    }

    moduleId = reportResults.getModuleId();
    ArrayList columnHeaders = reportResults.getTitles();
    ArrayList columns = new ArrayList();
    ValueListParameters parameters = new ValueListParameters(0, reportResults.getResults().size(), 1);
    for (int i = 0; i < columnHeaders.size(); i++) {
      columns.add(new FieldDescriptor((String)columnHeaders.get(i), i+1, false, ""));
    }
    parameters.setColumns(columns);
    // results is an arraylist of arraylists, it needs to be converted to an arraylist of ValueListRows
    ArrayList results = reportResults.getResults();
    ArrayList list = new ArrayList();
    for (int j = 0; j < results.size(); j++) {
      list.add(new ValueListRow(j, (ArrayList)results.get(j)));
    }
    ValueListVO valueList = new ValueListVO(list, parameters);
    ValueListDisplay displayParameters = new ValueListDisplay(new ArrayList(), false, false, false, false, false, false);
    valueList.setDisplay(displayParameters);
    request.setAttribute("valueList", valueList);
    request.setAttribute("reportName", reportResults.getName());
    request.setAttribute("moduleId", String.valueOf(moduleId));
    session.setAttribute("moduleId", String.valueOf(moduleId));
    request.setAttribute("reportId", String.valueOf(reportId));
    session.setAttribute("reportId", String.valueOf(reportId));
    request.setAttribute("reportType", String.valueOf(ReportConstants.ADHOC_REPORT_CODE));
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

  /**
   * Helper method - gets the Report Results List
   *
   * @param userID int
   * @param startATparam int
   * @param EndAtparam int
   * @param sortColumn String
   * @param reportId int
   * @return ReportResultList
   */
  public ReportResultVO getReportResults(int userID, int reportId, SearchVO search) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ReportResultVO reportResult = null;
    try {
      ReportFacade remote = SessionBeanFactory.getReportFacade();
      remote.setDataSource(dataSource);
      reportResult = remote.getAdHocReportResult(userID, reportId, search);
    } catch (Exception e) {
      logger.error("[getReportResults] Exception thrown.", e);
      throw new ServletException(e);
    }
    return reportResult;
  }
}