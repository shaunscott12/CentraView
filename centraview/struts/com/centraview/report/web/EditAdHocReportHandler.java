/*
 * $RCSfile: EditAdHocReportHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:38 $ - $Author: mking_cv $
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportVO;

/**
 * <code>EditAdHocReportHandler</code> Handler to save data to database and run
 * edited Ad-Hoc report.
 */

public class EditAdHocReportHandler extends ReportAction
{
  private static Logger logger = Logger.getLogger(EditAdHocReportHandler.class);
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
    String nextURL = "editadhocreport";
    int moduleId = getModuleId(request);
    try {
      HttpSession session = request.getSession();
      UserObject userobject = (UserObject)session.getAttribute("userobject");
      String timeZone = userobject.getUserPref().getTimeZone();
      int userId = userobject.getIndividualID();
      int reportId = getTheId("reportId", request);
      DynaActionForm adHocReportForm = (DynaActionForm)form;
      ReportVO reportVO = null;

      ReportFacade remote = getReportFacade();

      // searchCriteria should always have at least one row.
      SearchCriteriaVO[] searchCriteria = (SearchCriteriaVO[])adHocReportForm.get("searchCriteria");

      String addRow = (String)adHocReportForm.get("addRow");
      String removeRow = (String)adHocReportForm.get("removeRow");
      // we may just be doing search criteria row manipulation.
      if (addRow.equals("true")) {
        searchCriteria = AdvancedSearchUtil.addRow(searchCriteria);
        adHocReportForm.set("addRow", "false");
        adHocReportForm.set("searchCriteria", searchCriteria);
        reportVO = remote.getAdHocReport(userId, reportId);
        reportVO.setSelectedFields(getSelectedFieldsWithNames((String)adHocReportForm.get("contentFields"), (String)adHocReportForm.get("contentOrders"), (String)adHocReportForm.get("contentFieldNames")));
        request.setAttribute("pagedata", reportVO);
      } else if (!removeRow.equals("false")) {
        searchCriteria = AdvancedSearchUtil.removeRow(searchCriteria, removeRow);
        adHocReportForm.set("removeRow", "false");
        adHocReportForm.set("searchCriteria", searchCriteria);
        reportVO = remote.getAdHocReport(userId, reportId);
        reportVO.setSelectedFields(getSelectedFieldsWithNames((String)adHocReportForm.get("contentFields"), (String)adHocReportForm.get("contentOrders"), (String)adHocReportForm.get("contentFieldNames")));
        request.setAttribute("pagedata", reportVO);
      } else { // So it isn't search criteria row manipulation.
        // if action is null, it means we are displaying the form for editing, 
        // otherwise we are going to save and possibly run the report.
        String action = request.getParameter("action");
        if (action == null) {
          String showFields = (String)adHocReportForm.get("showFields");
          String createNew = (String)adHocReportForm.get("createNew");
          if (createNew != null && createNew.equals("true")) {
            adHocReportForm.initialize(mapping);
          }
          reportVO = remote.getAdHocReport(userId, reportId);
          // I guess showFields is used to determine if the user edited any of the fields, that are on the form
          // but not yet in the database.  So we know to pull the report stuff from the DB
          // and then change the selected fields based on the current form.
          // it is initially "false" in the struts config
          if (!showFields.equals("true")) {
            // if it isn't true we update the form with the info from the reportVO from the database.
            adHocReportForm = (DynaActionForm)getAdHocReportFormFromReportVO(reportVO, adHocReportForm, request);
          } else {
            // otherwise we analyze the fields from the form and show that.
            reportVO.setSelectedFields(getSelectedFieldsWithNames((String)adHocReportForm.get("contentFields"), (String)adHocReportForm.get("contentOrders"), (String)adHocReportForm.get("contentFieldNames")));
          }
          moduleId = reportVO.getModuleId();
          request.setAttribute("pagedata", reportVO);
        } else { // action is telling us to do something for real, check for errors.
          ActionErrors errors = adHocReportForm.validate(mapping, request);
          if (0 < errors.size()) {
            saveErrors(request, errors);
            reportVO = remote.getAdHocReport(userId, reportId);
            moduleId = reportVO.getModuleId();
            request.setAttribute("pagedata", reportVO);
          } else { // the form passed validation
            // So we need to build a reportVO based on the form.
            // and either save a new one to the database (if we are duplicate)
            // or update the existing one in the database.
            reportVO = getAdHocReportVOFromForm(timeZone, adHocReportForm);
            // I guess "copy" is used for duplicate?  So if copy is true we will
            // be creating a new one, otherwise we will be updating.
            if (((Boolean)adHocReportForm.get("copy")).booleanValue()) {
              reportId = remote.addAdHocReport(userId, reportVO);
              reportVO.setReportId(reportId);
            } else {
              remote.updateAdHocReport(userId, reportVO);
            }
            if (action.equals("save")) {
              moduleId = reportVO.getModuleId();
              // It may be better to actually store of the Selects and
              // whatever else outside the VO on the formbean, so we can
              // not have to round trip here.  But for now we need to get the
              // reportVO from the EJB layer because it has the field definitions in it. 
              reportVO = remote.getAdHocReport(userId, reportId);
              request.setAttribute("pagedata", reportVO);
            } else if (action.equals("close")) {
              adHocReportForm.set("createNew", "true");
              nextURL = "showadhocreportlist";
            } else if (action.equals("run")) {
              nextURL = "showadhocreportresult";
            }
          } // end else for if (0 < errors.size())
        } // end else for if (action == null)
      } // end else for if (addRow.equals("true"))
      adHocReportForm.set("moduleId", new Integer(moduleId));
      request.setAttribute("reportId", String.valueOf(reportId));
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
    String path = mapping.findForward(nextURL).getPath();
    ActionForward forward = new ActionForward(forwardName, path, false);
    return forward;
  }
}