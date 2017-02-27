/*
 * $RCSfile: EditStandardReportHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:38 $ - $Author: mking_cv $
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportResultVO;
import com.centraview.report.valueobject.ReportVO;

/**
 * <code>EditStandardReportHandler</code> Handler to save data to database and run
 * edited Standard report.
 */
public class EditStandardReportHandler extends ReportAction
{
  private static Logger logger = Logger.getLogger(EditStandardReportHandler.class);
  /**
   * This is overridden method  from Action class
   * This action is called when viewing choosing a standard report to run.
   * 
   * @param actionMapping ActionMapping
   * @param actionForm ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ActionForward
   */
  public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String nextURL = "editStandard";
    ActionForward forward = null;
    try
    {
      HttpSession session = request.getSession();
      UserObject userobject = (UserObject)session.getAttribute("userobject");
      String timeZone = userobject.getUserPref().getTimeZone();
      int userId = userobject.getIndividualID();
      int reportId = getTheId("reportId", request);
      int moduleId = getModuleId(request);
      DynaActionForm reportForm = (DynaActionForm)actionForm;
      String createNew = (String)reportForm.get("createNew");
      if (createNew.equals("true"))
      {
        reportForm.initialize(actionMapping);
        reportForm.set("moduleId", new Integer(moduleId));
      }
      ReportVO reportVO = null;
      ReportFacade remote = getReportFacade();
      String action = request.getParameter("action");
      String showFields = (String)((DynaActionForm)actionForm).get("showFields");
      if (action == null) // means we are probably coming here from the list view.
                          // or have resubmitted to manipulate search criteria.
      {
        reportVO = remote.getStandardReport(userId, reportId);
        moduleId = reportVO.getModuleId();
        // See if we should add a row.
        SearchCriteriaVO[] searchCriteria = (SearchCriteriaVO[])reportForm.get("searchCriteria");
        String addRow = (String)reportForm.get("addRow");
        String removeRow = (String)reportForm.get("removeRow");
        if (addRow.equals("true"))
        {
          searchCriteria = AdvancedSearchUtil.addRow(searchCriteria);
          reportForm.set("addRow", "false");
          reportForm.set("searchCriteria", searchCriteria);
        } else if (!removeRow.equals("false")) {
          searchCriteria = AdvancedSearchUtil.removeRow(searchCriteria, removeRow);
          reportForm.set("removeRow", "false");
          reportForm.set("searchCriteria", searchCriteria);
        } else if (!showFields.equals("true")) {
          // showfield probably means we have changed some fields on the form, and we need to parse
          // the form and update the VO that we got from the database to reflect those changes.
          reportForm = (DynaActionForm)getStandardReportFormFromReportVO(reportVO, actionForm, request);
        }
        moduleId = reportVO.getModuleId();
        request.setAttribute("pagedata", reportVO);
      } else if (action.equals("save")) {
        reportVO = getStandardReportVOFromForm(timeZone, actionForm);
        remote.updateStandardReport(userId, reportVO, false);
        // save is save and close (although there is nothing we can update)
        nextURL = "showstandardreportlist";
      } else if (action.equals("run") || action.equals("export") || action.equals("print")) {
        if (action.equals("run"))
        {
          reportVO = getStandardReportVOFromForm(timeZone, actionForm);
          remote.updateStandardReport(userId, reportVO, false);
          reportForm.set("createNew", "true");
          nextURL = "showstandardreportresult";
        } else if (action.equals("export")) {
          reportForm.set("createNew", "true");
          nextURL = "csvstandardreportresult";
        } else if (action.equals("print")) {
          reportForm.set("createNew", "true");
          nextURL = "printstandardreportresult";
        }
        // Extract the SearchVO from the form
        SearchVO search = this.getSearchVOFromForm(actionForm, request);
        if (action.equals("run")){
          session.removeAttribute("searchVO");
          session.setAttribute("searchVO",search);
        }
        // Run the report and get the results.
        if (action.equals("export") || action.equals("print")){
          search = (SearchVO)session.getAttribute("searchVO");
        }
        ReportResultVO reportResultVO = remote.getStandardReportResult(userId, reportId, search);
        moduleId = reportResultVO.getModuleId();
        request.setAttribute("pagedata", reportResultVO);
      }
      request.setAttribute("reportId", String.valueOf(reportId));
      request.setAttribute("moduleId", String.valueOf(moduleId));
      session.setAttribute("moduleId", String.valueOf(moduleId));
      request.setAttribute("reportType", String.valueOf(ReportConstants.STANDARD_REPORT_CODE));
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
      String path = actionMapping.findForward(nextURL).getPath();
      forward = new ActionForward(forwardName, path, false);
      request.setAttribute("hideListDropDown", new Boolean(true));
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    return forward;
  }
}
