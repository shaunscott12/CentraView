/*
 * $RCSfile: DeleteReportHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:37 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.report.ejb.session.ReportFacade;

/**
 * <code>DeleteReportHandler</code> Handler to delete data from database for
 * the reports.
 */

public class DeleteReportHandler extends ReportAction
{
  private static Logger logger = Logger.getLogger(DeleteReportHandler.class);
  /**
   * This is overridden method  from Action class
   *
   * @param mapping ActionMapping
   * @param form ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws IOException
   * @throws ServletException
   * @return ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    try {
      String rowId[] = null;
      HttpSession session = request.getSession(true);
      rowId = request.getParameterValues("rowId");
      if (rowId != null) {
        int[] reportIds = new int[rowId.length];
        int elementID = 0;
        for (int i = 0; i < rowId.length; i++) {
          reportIds[i] = Integer.parseInt(rowId[i]);
        }
        ReportFacade remote = getReportFacade();
        remote.delete(reportIds);
      }
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    String nextURL = "showadhocreportlist";
    int moduleId = getModuleId(request);
    request.setAttribute("moduleId", String.valueOf(moduleId));
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
    String path = mapping.findForward(nextURL).getPath();
    // path is anticipated to be "blah.do?moduleId=" so simply appending moduleId will go where we want.
    ActionForward forward = new ActionForward(forwardName, path+moduleId, true);
    return forward;
  }
}