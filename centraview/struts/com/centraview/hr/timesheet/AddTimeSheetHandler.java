/*
 * $RCSfile: AddTimeSheetHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:02 $ - $Author: mking_cv $
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
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.PureDateMember;
import com.centraview.common.PureTimeMember;
import com.centraview.common.StringMember;
import com.centraview.common.TimeSheetList;
import com.centraview.common.UserObject;
import com.centraview.hr.common.HrConstantKeys;
import com.centraview.hr.helper.TimeSheetVO;
import com.centraview.hr.helper.TimeSheetVOX;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.projects.helper.TimeSlipVO;
import com.centraview.settings.Settings;


public class AddTimeSheetHandler extends org.apache.struts.action.Action
{

	private com.centraview.common.UserObject userobjectd;
	private org.apache.struts.validator.DynaValidatorForm dynaValidatorForm;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String returnStatus = "failure";
		try
		{
			dynaValidatorForm = (org.apache.struts.validator.DynaValidatorForm) form;

			String saveandclose = (String) dynaValidatorForm.get("saveclose");
			String save = (String) dynaValidatorForm.get("save");

			String saveandnew = (String) dynaValidatorForm.get("savenew");
			String addslip = (String) dynaValidatorForm.get("addslip");
			HttpSession session = request.getSession(true);

			String sTypeOfOperation = request.getParameter("TYPEOFOPERATION");
			if (sTypeOfOperation == null)
			{
				sTypeOfOperation = (String) session.getAttribute("TYPEOFOPERATION");
			}



			if (session.getAttribute("highlightmodule") != null)
			{
				session.setAttribute("highlightmodule", "hr");
			}

			userobjectd = (UserObject) session.getAttribute("userobject");

			if (sTypeOfOperation == null)
			{
				if ((addslip != null) && addslip.equalsIgnoreCase("New Slip"))
				{
					int tsID = saveForm();

						Integer ID = new Integer(tsID);
						request.setAttribute("timesheetID", ID);

					String timeSlipFlag = (String) request.getParameter("timeSlipFlag");
					request.setAttribute("timeSlipFlag", timeSlipFlag);
					returnStatus = "addTimeSlip";
				}
				else if ((save != null) && save.equalsIgnoreCase("Save"))
				{
					TimeSheetVOX timeSheetVOX = new TimeSheetVOX(userobjectd,
					dynaValidatorForm);
					TimeSheetVO timeSheetVO = timeSheetVOX.getTimeSheetVO();

					String addslipflag = (String) session.getAttribute("addslip");

					//Condition that instructs that the NewSlip button is clicked  and the timesheet is already added to the database
					if (addslipflag == null)
					{
						saveForm();
					}

					ListPreference listpreference = userobjectd.getListPreference("TimeSheet");
					DisplayList displaylistSession = (DisplayList) session.getAttribute("displaylist");

					DisplayList displaylist = (DisplayList) request.getAttribute("displaylist");

					TimeSheetList DL = null;

					if (displaylist == null)
					{
						ListGenerator lg = ListGenerator.getListGenerator(dataSource);
						DL = (TimeSheetList) lg.getTimeSheetList(userobjectd.getIndividualID(),
						1, listpreference.getRecordsPerPage(), "", listpreference.getSortElement());
					}
					else
					{
						DL = (TimeSheetList) displaylistSession;
					}

					request.setAttribute("HrExpenseForm", form);
					session.setAttribute("HrExpenseForm", form);

					session.setAttribute("displaylist", DL);
					session.setAttribute("timesheetvo", timeSheetVO);
					request.setAttribute("displaylist", DL);
					request.setAttribute("list", "TimeSheet");
					request.setAttribute(HrConstantKeys.TYPEOFSUBMODULE, HrConstantKeys.TIMESHEET);

					returnStatus = ".view.hr.timesheet.edit";
				}
				else if ((saveandclose != null) &&
				   saveandclose.equalsIgnoreCase("Save & Close"))
				{
					TimeSheetVOX timeSheetVOX = new TimeSheetVOX(userobjectd,
					dynaValidatorForm);
					TimeSheetVO timeSheetVO = timeSheetVOX.getTimeSheetVO();

					String addslipflag = (String) session.getAttribute("addslip");

					//Condition that instructs that the NewSlip button is clicked  and the timesheet is already added to the database
					if (addslipflag == null)
					{
						saveForm();
					}
					else
					{
						session.setAttribute("addslip", null);
					}

					ListPreference listpreference = userobjectd.getListPreference("TimeSheet");
					DisplayList displaylistSession = (DisplayList) session.getAttribute("displaylist");

					DisplayList displaylist = (DisplayList) request.getAttribute("displaylist");

					TimeSheetList DL = null;

					if (displaylist == null)
					{
						ListGenerator lg = ListGenerator.getListGenerator(dataSource);
						DL = (TimeSheetList) lg.getTimeSheetList(userobjectd.getIndividualID(),
						1, listpreference.getRecordsPerPage(), "", listpreference.getSortElement());
					}
					else
					{
						DL = (TimeSheetList) displaylistSession;
					}

					session.setAttribute("displaylist", DL);
					request.setAttribute("displaylist", DL);
					request.setAttribute("list", "TimeSheet");
					request.setAttribute(HrConstantKeys.TYPEOFSUBMODULE, HrConstantKeys.TIMESHEET);

					returnStatus = ".view.hr.timesheet.list";
				}
				else if ((saveandnew != null) &&
				saveandnew.equalsIgnoreCase("Save & New"))
				{
					String addslipflag = (String) session.getAttribute("addslip");

					if (addslipflag == null)
					{
						saveForm();
					}
					else
					{
						session.setAttribute("addslip", null);
					}

					dynaValidatorForm.initialize(mapping);
					session.setAttribute("timesliplist", null);
					session.setAttribute("savenewflag", "true");
					returnStatus = ".view.hr.timesheet.new";
				}
			}
			else if (sTypeOfOperation.equalsIgnoreCase("EDIT"))
			{
				//Here we will be doing the editing of the timesheet records
				//i.e. the update query will be set
				String sTimeSheetID = (String) request.getParameter("timesheetID");

				int timesheetId = 0;
				if (sTimeSheetID != null)
				{
					Integer iTimeSheetId = new Integer(sTimeSheetID);
					timesheetId = iTimeSheetId.intValue();
				}
				else
				{
					Integer iTimeSheetId = (Integer) request.getAttribute("timesheetID");
					timesheetId = iTimeSheetId.intValue();
				}

				updateForm(timesheetId);

				if ((save != null) && save.equals("Save"))
				{
					//The record is updated in the DB and the new timesheet screen is shown.
					returnStatus = ".view.hr.timesheet.edit";
					dynaValidatorForm.initialize(mapping);
				}

				if ((saveandclose != null) && saveandclose.equals("Save & Close"))
				{
					//The record will be updated in the DB and the page is closed.
					returnStatus = ".view.hr.timesheet.list";
				}
				else if ((saveandnew != null) && saveandnew.equals("Save & New"))
				{
					//The record is updated in the DB and the new timesheet screen is shown.
					session.setAttribute("savenewflag", "true");
					returnStatus = ".view.hr.timesheet.new";
					dynaValidatorForm.initialize(mapping);
				}

				session.setAttribute("TYPEOFOPERATION", sTypeOfOperation);
			}
			else if (sTypeOfOperation.equalsIgnoreCase("DUPLICATE"))
			{

				String sTimeSheetID = (String) request.getParameter("timesheetID");
				Integer iTimeSheetId = new Integer(0);
				if (sTimeSheetID != null){
					iTimeSheetId = new Integer(sTimeSheetID);
				}
				else if(request.getAttribute("timesheetID") != null){
					iTimeSheetId = (Integer) request.getAttribute("timesheetID");
				}


				if (iTimeSheetId != null)
				{
					int timesheetId = iTimeSheetId.intValue();
					int iNewTimeSheetId = saveForm();
					TimeSheetVO timeSheetVO=null;
					try
					{
						HrFacadeHome aa = (HrFacadeHome)
						CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");

						HrFacade remote = (HrFacade) aa.create();
						remote.setDataSource(dataSource);
						timeSheetVO = remote.getTimeSheet(iNewTimeSheetId);
					}
					catch (Exception e)
					{
						System.out.println("[Exception] AddTimeSheetHandler.execute: " + e.toString());
						e.printStackTrace();
					}

					DisplayList DL = null;

					if (timeSheetVO != null)
					{
						DL = (DisplayList) timeSheetVO.getTimeSlipList();
					}
					if(DL != null){
						duplicateTimeSlipRecords(iNewTimeSheetId, DL);
					}
					Integer ID = new Integer(iNewTimeSheetId);
					request.setAttribute("timesheetID", ID);
				}
				if ((save != null) && save.equals("Save"))
				{
					returnStatus = ".view.hr.timesheet.edit";
				}

				if ((saveandclose != null) && saveandclose.equals("Save & Close"))
				{
					returnStatus = ".view.hr.timesheet.list";
				}
				else if ((saveandnew != null) && saveandnew.equals("savenew"))
				{
					//The record is updated in the DB and the new timesheet screen is shown.
					session.setAttribute("savenewflag", "true");
					returnStatus = ".view.hr.timesheet.list";
					dynaValidatorForm.initialize(mapping);
				}
				session.setAttribute("TYPEOFOPERATION", sTypeOfOperation);
			}
		}
		catch (Exception e)
		{
			System.out.println("[Exception] AddTimeSheetHandler.execute: " + e.toString());
			e.printStackTrace();
			returnStatus = "failure";
		}

		return mapping.findForward(returnStatus);
	}

	private int saveForm()
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		int timesheetID = 0;
		try
		{
			TimeSheetVOX timeSheetVOX = new TimeSheetVOX(userobjectd, dynaValidatorForm);
			TimeSheetVO timeSheetVO = timeSheetVOX.getTimeSheetVO();

			HrFacadeHome aa = (HrFacadeHome)
			CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
			HrFacade remote = (HrFacade) aa.create();
			remote.setDataSource(dataSource);
			int indvID = userobjectd.getIndividualID();
			timesheetID = remote.addTimeSheet(indvID, timeSheetVO);
		}
		catch (Exception e)
		{
			System.out.println("[Exception] AddTimeSheetHandler.saveForm: " + e.toString());
			e.printStackTrace();
		}
		return timesheetID;
	}

	private void updateForm(int AiTimeSheetId)
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		TimeSheetVOX timeSheetVOX = new TimeSheetVOX(userobjectd, dynaValidatorForm);
		TimeSheetVO timeSheetVO = timeSheetVOX.getTimeSheetVO();
		int timesheetID = 0;

		try
		{
			HrFacadeHome aa = (HrFacadeHome)
			CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
			HrFacade remote = (HrFacade) aa.create();
			remote.setDataSource(dataSource);

			int indvID = userobjectd.getIndividualID();
			remote.updateTimeSheet(indvID, timeSheetVO);
		}
		catch (Exception e)
		{
			System.out.println("[Exception] AddTimeSheetHandler.updateForm: " + e.toString());
			e.printStackTrace();
		}
	}


	private void duplicateTimeSlipRecords(int timeslipID, DisplayList DL)
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		if (DL != null)
		{
			Set listkey = DL.keySet();
			Iterator it = listkey.iterator();

			while (it.hasNext())
			{
				TimeSlipVO hrtsvo = new TimeSlipVO();
				hrtsvo.setTimesheetID(timeslipID);

				String str = (String) it.next();

				StringMember sm = null;
				IntMember im = null;
				PureDateMember pdm = null;
				PureTimeMember ptm = null;

				ListElement ele = (ListElement) DL.get(str);
				sm = (StringMember) ele.get("Description");

				String sDescription = sm.getDisplayString();
				hrtsvo.setDescription(sDescription);
				im = (IntMember) ele.get("ID");

				String id = im.getDisplayString();
				ptm = (PureTimeMember) ele.get("StartTime");

				String sStartTime = ptm.getDisplayString();

				Vector vStime = getTime(sStartTime);
				String shr = (String) vStime.get(0);
				int hr = Integer.parseInt(shr);
				String smin = (String) vStime.get(1);
				int min = Integer.parseInt(smin);
				hrtsvo.setStart(new java.sql.Time(hr, min, 0));

				//Convert to time
				ptm = (PureTimeMember) ele.get("EndTime");

				String sEndTime = ptm.getDisplayString();

				//Convert to time
				Vector vEtime = getTime(sEndTime);
				String sEhr = (String) vEtime.get(0);
				int iEhr = Integer.parseInt(sEhr);
				String sEmin = (String) vEtime.get(1);
				int iEmin = Integer.parseInt(sEmin);
				hrtsvo.setEnd(new java.sql.Time(iEhr, iEmin, 0));

				//Convert to time
				pdm = (PureDateMember) ele.get("Date");

				String sDate = pdm.getDisplayString();

				//Convert to date
				Vector vecDate = getDate(sDate);
				String sMon = (String) vecDate.get(0);

				int iMon = Integer.parseInt(sMon);
				String sDay = (String) vecDate.get(1);

				int iDay = Integer.parseInt(sDay);
				String sYear = ((String) vecDate.get(2)).trim();

				int iYear = Integer.parseInt(sYear);
				hrtsvo.setDate(new java.sql.Date(iYear, iMon, iDay));

				//Convert to date
				sm = (StringMember) ele.get("Duration");

				String sDuration = sm.getDisplayString();

				try
				{
					HrFacadeHome aa = (HrFacadeHome)
					CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
					HrFacade remote = (HrFacade) aa.create();
					remote.setDataSource(dataSource);
					int indvID = userobjectd.getIndividualID();
					int iNewTimeSlipId = remote.addTimeSlip(indvID, hrtsvo);
				}
				catch (Exception e)
				{
					System.out.println("[Exception] AddTimeSheetHandler.duplicateTimeSlipRecords: " + e.toString());
					e.printStackTrace();
				}
			}
		}

	}

	private Vector getDate(String sfromDate)
	{
		Vector vecDate1 = new Vector();
		Vector vecDate2 = new Vector();
		Vector vecFin = new Vector();
		StringTokenizer stok1 = new StringTokenizer(sfromDate, ",");

		while (stok1.hasMoreTokens())
		{
			String sToken = stok1.nextToken();
			vecDate1.add(sToken);
		}

		String stok2 = (String) vecDate1.get(0);
		StringTokenizer sTokens1 = new StringTokenizer(stok2, " ");

		while (sTokens1.hasMoreTokens())
		{
			String sToken = sTokens1.nextToken();
			vecDate2.add(sToken.trim());
		}

		vecFin.add(getMonth(vecDate2.get(0))); //month
		vecFin.add(vecDate2.get(1)); //day
		vecFin.add(vecDate1.get(1)); //year

		return vecFin;
	}

	private String getMonth(Object Amonth)
	{
		String sMonth = (String) Amonth;

		String month = "0";

		if (sMonth.trim().equalsIgnoreCase("Jan"))
		{
			month = "1";
		}
		else if (sMonth.trim().equalsIgnoreCase("Feb"))
		{
			month = "2";
		}
		else if (sMonth.trim().equalsIgnoreCase("Mar"))
		{
			month = "3";
		}
		else if (sMonth.trim().equalsIgnoreCase("Apr"))
		{
			month = "4";
		}
		else if (sMonth.trim().equalsIgnoreCase("May"))
		{
			month = "5";
		}
		else if (sMonth.trim().equalsIgnoreCase("June"))
		{
			month = "6";
		}
		else if (sMonth.trim().equalsIgnoreCase("July"))
		{
			month = "7";
		}
		else if (sMonth.trim().equalsIgnoreCase("Aug"))
		{
			month = "8";
		}
		else if (sMonth.trim().equalsIgnoreCase("Sep"))
		{
			month = "9";
		}
		else if (sMonth.trim().equalsIgnoreCase("Oct"))
		{
			month = "10";
		}
		else if (sMonth.trim().equalsIgnoreCase("Nov"))
		{
			month = "11";
		}
		else if (sMonth.trim().equalsIgnoreCase("Dec"))
		{
			month = "12";
		}

		return month;
	}

	private Vector getTime(String sfromDate)
	{
		StringTokenizer stok = new StringTokenizer(sfromDate, " ");
		int i = 0;
		Vector vecTime1 = new Vector();
		Vector vecTime2 = new Vector();

		while (stok.hasMoreTokens())
		{
			String sToken = stok.nextToken();
			vecTime1.add(sToken);
		}

		String stok1 = (String) vecTime1.get(0);
		StringTokenizer sTokens1 = new StringTokenizer(stok1, ":");

		while (sTokens1.hasMoreTokens())
		{
			String sToken = sTokens1.nextToken();
			vecTime2.add(sToken);
		}
		return vecTime2;
	}


}
