/*
 * $RCSfile: TimeSlipVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:47 $ - $Author: mking_cv $
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

package com.centraview.projects.helper;


import org.apache.struts.action.ActionForm;

import com.centraview.common.CVUtility;
import com.centraview.projects.timeslip.TimeSlipForm;

/*
 * @author  Gunjan Tiwari
 * @version 1.0
 */
public class TimeSlipVOX extends TimeSlipVO
{

	/**
	 *	In this constructor populate the TimeSlipVO Object.
	 *
	 * @param   form  ActionForm
	 */
	public TimeSlipVOX(String TimeZone, ActionForm form)
	{
		try
		{

		TimeSlipForm dynaForm = (TimeSlipForm)form;

		//get all form values in local variable
		//to be removed after debug and change for long

		String Project = (String)dynaForm.getProject();
		String ProjectID = (String)dynaForm.getProjectID();
		String ticketID	= (String)dynaForm.getTicketID();
		String ticket	= (String)dynaForm.getTicket();
		String TimeSlipID	= (String)dynaForm.getTimeSlipID();
		String Task	= (String)dynaForm.getTask();
		String Description = (String)dynaForm.getDescription();
		String Month = (String)dynaForm.getMonth();
		String Day = (String)dynaForm.getDay();
		String Year = (String)dynaForm.getYear();
		String StartTime = (String)dynaForm.getStartTime();
		String EndTime = (String)dynaForm.getEndTime();
		String BreakHours = (String)dynaForm.getBreakHours();
		String BreakMinutes	= (String)dynaForm.getBreakMinutes();
		String TaskID = (String)dynaForm.getActivityID();
		String timesheetID = (String)dynaForm.getTimesheetID();
		float TotBreakTime			=  Float.parseFloat(BreakHours) + Float.parseFloat(BreakMinutes)/60;

		if(timesheetID == null || timesheetID.equals(""))
		{
			timesheetID = "0";
		}
		if (timesheetID != null && !timesheetID.equals(""))
		{
			setTimesheetID(Integer.parseInt(timesheetID));
		}

		if(ticket == null||ticket.equals(""))
		{
			ticketID="0";
		}
		if(Project == null||Project.equals(""))
		{
			ProjectID = "0";
		}

		if(Task == null||Task.equals(""))
		{
			TaskID = "0";
		}

		if (ticketID != null && !ticketID.equals(""))
		{
			setTicketID(Integer.parseInt(ticketID));
		}
		if (ProjectID != null && !ProjectID.equals(""))
		{
			setProjectID(Integer.parseInt(ProjectID))	;
		}
		if( TimeSlipID != null && !TimeSlipID.equals("") ){
			setTimeSlipID(Integer.parseInt(TimeSlipID))	;
		}
		setDescription(Description);
		setDate(new java.sql.Date(Integer.parseInt(Year),(Integer.parseInt(Month)-1),Integer.parseInt(Day)));

		int startHrsmm[] = CVUtility.convertTimeTo24HrsFormat(StartTime);
		int startHrs = startHrsmm[0];
		int startMins = startHrsmm[1];

		int endHrsmm[] = CVUtility.convertTimeTo24HrsFormat(EndTime);
		int endHrs = endHrsmm[0];
		int endMins = endHrsmm[1];

		setStart(new java.sql.Time(startHrs, startMins,0));
 		setEnd(new java.sql.Time(endHrs, endMins,0));

		setBreakTime(TotBreakTime);
		setProjectTitle(Project);
		setTicket(ticket);
		setTaskTitle(Task);
		if(TaskID != null)
			setTaskID(Integer.parseInt(TaskID));

			Float BreakTime = new Float(TotBreakTime);
				int iHours =  BreakTime.intValue();
				float breakTime = (BreakTime.floatValue() - iHours)*60;
				Float Mins = new Float(breakTime);
				int iMins =  Mins.intValue();

				//dynaForm.setBreakHours(new Integer(iHours).toString());
				//dynaForm.setBreakMinutes(new Integer(iMins).toString());

				float fBreakTime = getBreakTime() ;
				float remMin = 0;

				if ( startMins != 0 && endMins != 0 )
				{
					if(endMins > startMins)
						remMin = 60/(endMins - startMins);
					else
						remMin = 60/(startMins - endMins);
				}

				Float Duration = new Float((endHrs - startHrs) + remMin - fBreakTime) ;
				iHours =  Duration.intValue();
				float fMins = (Duration.floatValue() - iHours)*60;
				Mins = new Float(fMins);
				iMins =  Mins.intValue();
				setHours(Duration.floatValue());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 *
	 *
	 * @return  The CustomField value Object.
	 */
	public TimeSlipVO getVO()
	{
		return super.getVO();
	}
}
