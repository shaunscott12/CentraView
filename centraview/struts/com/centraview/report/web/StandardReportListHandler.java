/*
 * $RCSfile: StandardReportListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/19 20:20:58 $ - $Author: mcallist $
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
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListConstants;

/**
 * <code>StandardReportListHandler</code> Handler handles list action for
 * display of standard report list.
 */
public class StandardReportListHandler extends ReportListHandler
{
  private static Logger logger = Logger.getLogger(StandardReportListHandler.class);
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
      getReportList(request, dataSource, "StandardReport", ValueListConstants.STANDARD_REPORT_LIST_TYPE, ValueListConstants.standardReportViewMap, moduleId, new ArrayList());
      session.removeAttribute("standardreportform");
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    // based on the module ID pick the forward name this name is used to set the selections on the left nav.
    String forwardName = "";
    switch (moduleId) {
      case 14: forwardName = ".view.reports.contacts.standard"; break;
      case 3: forwardName = ".view.reports.activities.standard"; break;
      case 30: forwardName = ".view.reports.sales.standard"; break;
      case 33: forwardName = ".view.reports.marketing.standard"; break;
      case 36: forwardName = ".view.reports.project.standard"; break;
      case 39: forwardName = ".view.reports.support.standard"; break;
      case 42: forwardName = ".view.reports.accounting.standard"; break;
      case 52: forwardName = ".view.reports.hr.standard"; break;
      default: forwardName = ".view.reports.contacts.standard"; break;
    }
    String path = actionMapping.findForward("valuelist").getPath();
    ActionForward forward = new ActionForward(forwardName, path, false);
    return forward;
  }
}