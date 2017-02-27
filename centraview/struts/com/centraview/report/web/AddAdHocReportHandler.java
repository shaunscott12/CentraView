/*
 * $RCSfile: AddAdHocReportHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:37 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportVO;
import com.centraview.report.valueobject.SelectVO;
import com.centraview.settings.Settings;

/**
 * <code>AddAdHocReportHandler</code> Handler to insert data to database for
 * Ad-Hoc report.
 */
public class AddAdHocReportHandler extends ReportAction
{
  private static Logger logger = Logger.getLogger(AddAdHocReportHandler.class);
  /**
   * This is ovenidden method  from Action class
   * It is called to save ad-hoc report that has been created.
   * 
   * @param actionMapping ActionMapping
   * @param actionForm ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ActionForward
   */
  public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    HttpSession session = request.getSession(true);
    String nextURL = "addadhocreport";
    int moduleId = getModuleId(request);
    try {
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");//get the user object
      int userId = userobjectd.getIndividualID();
      int reportId = 0;
      String timeZone = userobjectd.getUserPref().getTimeZone();
      DynaActionForm reportForm = (DynaActionForm)actionForm;

      String action = request.getParameter("action");
      if (action == null) {
        // if action wasn't passed then we set the data from the EJB layer on the form
        setPageData(request, moduleId);
        nextURL = "addadhocreport";
      } else if (action.equals("new") || action.equals("close") || action.equals("run")) {
        // Validate the form
        ActionErrors errors = reportForm.validate(actionMapping, request);
        if (0 < errors.size()) {
          // if it doesn't pass muster show the wankers some errors.
          saveErrors(request, errors);
          setPageData(request, moduleId);
          nextURL = "addadhocreport";
        } else {
          // Insert a new report into the database.
          ReportVO reportVO = getAdHocReportVOFromForm(timeZone, reportForm);
          ReportFacade remote = getReportFacade();
          reportId = remote.addAdHocReport(userId, reportVO);
          if (action.equals("new")) {
            // if save&new then show a blank form. 
            reportForm.initialize(actionMapping);
            nextURL = "newaddhocreport";
          } else if (action.equals("run")) {
            reportForm.set("createNew", "true");
            request.setAttribute("reportId", String.valueOf(reportId));
            nextURL = "showadhocreportresult";
          } else {
            // save and close.
            reportForm.set("createNew", "true");
            nextURL = "showadhocreportlist";
          }
        }
      } else {
        // if it is some other value besides new, close, or run
        // reinitialize the form.
        reportForm.initialize(actionMapping);
        nextURL = "addadhocreport";
      }
      session.setAttribute("moduleId", String.valueOf(moduleId));
      // unless ViewAdHocReport.do needs the following whack it.
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

  /**
   * This is method to set PageData
   *
   * @param request HttpServletRequest
   * @param moduleId int
   */
  private void setPageData(HttpServletRequest request, int moduleId)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      ReportVO reportVO = new ReportVO();
      SelectVO selects = new SelectVO();
      ReportFacade remote = getReportFacade();
      selects = remote.getAdHocPageData(moduleId);
      reportVO.setSelect(selects);
      request.setAttribute("pagedata", reportVO);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}