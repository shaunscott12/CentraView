/*
 * $RCSfile: ViewTimeSheetDetailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:03 $ - $Author: mking_cv $
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

package com.centraview.hr.timesheet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.hr.helper.TimeSheetVO;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;

public class ViewTimeSheetDetailHandler extends org.apache.struts.action.Action
{

  private TimeSheetVO timeSheetVO = null;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession(true);
    com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject");
    //String sortElement = request.getParamter("sortElement");
    String returnStatus = "NewTimeSheet";

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - h:mm a");

    String ele = request.getParameter("rowId");
    Integer timesheetID = null;
    timesheetID = (Integer)request.getAttribute("timesheetID");
    int timeSheetId = 0;
    if (ele != null)
    {
      timesheetID = new Integer(ele);
    }
    timeSheetId = timesheetID.intValue();

    request.setAttribute("timesheetID", timesheetID);
    //get type of operation
    String sOperationType = request.getParameter("TYPEOFOPERATION");

    try
    {
      int indvID = userobjectd.getIndividualID();
      HrFacadeHome hrh = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
      HrFacade remote = (HrFacade)hrh.create();
      remote.setDataSource(dataSource);
      TimeSheetVO timeSheetVO = null;
      if (timeSheetId != 0)
        timeSheetVO = remote.getTimeSheet(timeSheetId);

      DynaValidatorForm dynaForm = (DynaValidatorForm)form;
      dynaForm.reset(mapping, request);
      if (timeSheetVO.getReportingTo() != null)
      {
        dynaForm.set("reportingTo", timeSheetVO.getReportingTo());
      }
      if (timeSheetVO.getReportingToId() > 0)
      {
        int reportingToId = timeSheetVO.getReportingToId();
        Integer iReportingToId = new Integer(reportingToId);
        String sReportingToId = iReportingToId.toString();
        dynaForm.set("reportingToId", sReportingToId);
      }
      if (timeSheetVO.getOwnerName() != null)
      {
        dynaForm.set("employee", timeSheetVO.getOwnerName());
      }
      if (timeSheetVO.getOwner() > 0)
      {
        int employeeId = timeSheetVO.getOwner();
        Integer iemployeeId = new Integer(employeeId);
        String semployeeId = iemployeeId.toString();
        dynaForm.set("employeeId", semployeeId);
      }
      if (timeSheetVO.getDescription() != null)
      {
        dynaForm.set("description", timeSheetVO.getDescription());
      }
      if (timeSheetVO.getStatus() >= 0)
      {
        Long lStatus = new Long(timeSheetVO.getStatus());
        if (lStatus.intValue() == 1)
          request.setAttribute("DISABLED", "TRUE");
        dynaForm.set("status1", lStatus);
      }
      if (timeSheetVO.getNotes() != null)
      {
        dynaForm.set("notes", timeSheetVO.getNotes());
      }
      if (timeSheetVO.getCreatedDate() != null)
      {
        java.sql.Timestamp createdDate = timeSheetVO.getCreatedDate();
        String sCreatedDate = createdDate.toString();
        //dynaForm.set("createdDate",sCreatedDate);
      }
      if (timeSheetVO.getModifiedDate() != null)
      {
        //java.sql.Date modifiedDate = timeSheetVO.getModifiedDate();
        java.sql.Timestamp modifiedDate = timeSheetVO.getModifiedDate();
        String sModifiedDate = modifiedDate.toString();
        //dynaForm.set("modifiedDate",sModifiedDate);
      }
      if (timeSheetVO.getFromDate() != null)
      {
        java.sql.Date fromDate = timeSheetVO.getFromDate();
        String sfromDate = fromDate.toString();
        Vector fromDateSplitUp = getDate(sfromDate);
        dynaForm.set("fromyear", (String)fromDateSplitUp.get(0));
        dynaForm.set("frommonth", (String)fromDateSplitUp.get(1));
        dynaForm.set("fromday", (String)fromDateSplitUp.get(2));
      }
      if (timeSheetVO.getToDate() != null)
      {
        java.sql.Date toDate = timeSheetVO.getToDate();
        String stoDate = toDate.toString();
        //String[] sTokenizedDate = getDate(stoDate);
        Vector toDateSplitUp = getDate(stoDate);
        dynaForm.set("toyear", (String)toDateSplitUp.get(0));
        dynaForm.set("tomonth", (String)toDateSplitUp.get(1));
        dynaForm.set("today", (String)toDateSplitUp.get(2));
      }
	  dynaForm.set("timesheetID",timeSheetId+"");

      DisplayList displaylist = (DisplayList)timeSheetVO.getTimeSlipList();

      //Setting the timesliplist attribute
      session.setAttribute("timesliplist", displaylist);
      session.setAttribute("timesheetvo", timeSheetVO);
      request.setAttribute("timesliplist", displaylist);
      returnStatus = ".view.hr.timesheet.detail";
      if (sOperationType != null && sOperationType.equals("EDIT"))
      {
        request.setAttribute("TYPEOFOPERATION","EDIT");
      }
      else if (sOperationType != null && sOperationType.equals("DUPLICATE")){
      	request.setAttribute("TYPEOFOPERATION","DUPLICATE");
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception][ViewTimeSheetDetailHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    String addslipflag = (String)request.getParameter("addslipflag");
    request.setAttribute("addslip", addslipflag);
    session.setAttribute("addslip", addslipflag);
    return mapping.findForward(returnStatus);
  }

  private Vector getDate(String sfromDate)
  {
    StringTokenizer stok = new StringTokenizer(sfromDate, "-");
    String[] sTokens = new String[] {};
    int i = 0;
    Vector vecDate = new Vector();
    while (stok.hasMoreTokens())
    {
      String sToken = stok.nextToken();
      vecDate.add(sToken);
    }
    return vecDate;
  }
}
