/*
 * $RCSfile: AdHocReportListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/19 20:20:58 $ - $Author: mcallist $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.settings.Settings;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.ValueListConstants;

/**
 * <code>AdHocdReportListHandler</code> Handler handles list action for
 * display of Ad-Hoc report list.
 */
public class AdHocReportListHandler extends ReportListHandler
{
  private static Logger logger = Logger.getLogger(AdHocReportListHandler.class);
  /**
   * This is overridden method  from Action class
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
    int moduleId = getModuleId(request);
    try {
      String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
      UserObject user = (UserObject)session.getAttribute("userobject");//get the user object
      int individualID = user.getIndividualID();
      ListPreference listpreference = user.getListPreference(ReportConstants.ADHOC_REPORT_LIST);
      ArrayList buttonList = new ArrayList();
      buttonList.add(new Button("New Report", "new", "c_goTo('/reports/new_adhoc.do?moduleId="+moduleId+"');", false));
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
      getReportList(request, dataSource, "StandardReport", ValueListConstants.ADHOC_REPORT_LIST_TYPE, ValueListConstants.adhocReportViewMap, moduleId, buttonList);
      session.removeAttribute("standardreportform");
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    request.setAttribute("moduleId", String.valueOf(moduleId));
    session.setAttribute("moduleId", String.valueOf(moduleId));
    // based on the module ID pick the forward name this name is used to set the selections on the left nav.
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
    String path = actionMapping.findForward("valuelist").getPath();
    ActionForward forward = new ActionForward(forwardName, path, false);
    return forward;
  }
}