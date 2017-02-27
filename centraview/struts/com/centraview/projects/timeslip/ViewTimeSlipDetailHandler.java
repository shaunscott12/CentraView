/*
 * $RCSfile: ViewTimeSlipDetailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:33 $ - $Author: mking_cv $
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

package com.centraview.projects.timeslip;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.projects.helper.TimeSlipVO;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;

public class ViewTimeSlipDetailHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    int startHrs = 0;
    int startMins = 0;
    int endHrs = 0;
    int endMins = 0;
    Time tStartTime;
    Time tEndTime;
    String returnStatus = "failure";

    String rowId = request.getParameter("rowId");

    HttpSession session = request.getSession(true);
    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();
    TimeSlipForm dynaForm = (TimeSlipForm) form;

    TimeSlipVO timeSlipVO = null;

    //AddressVO primaryAdd = null;
    ProjectFacadeHome pfh = (ProjectFacadeHome) 
      CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");

    try
    {
      ProjectFacade remote = (ProjectFacade) pfh.create();
      remote.setDataSource(dataSource);
      timeSlipVO = remote.getTimeSlip(Integer.parseInt(rowId), individualID);
      dynaForm.setTimeSlipID(new Integer(timeSlipVO.getTimeSlipID()).toString());

      if (timeSlipVO.getProjectID() != 0)
      {
        dynaForm.setProjectID(Integer.toString(timeSlipVO.getProjectID()));
      }

      if (timeSlipVO.getProjectTitle() != null)
      {
        dynaForm.setProject(timeSlipVO.getProjectTitle());
        dynaForm.setReference(timeSlipVO.getProjectTitle());
        if(!timeSlipVO.getProjectTitle().equals("")){
        	dynaForm.setLookupList("1");
        }
      }

      if (timeSlipVO.getTaskTitle() != null)
      {
        dynaForm.setTask(timeSlipVO.getTaskTitle());
      }

      if (timeSlipVO.getTaskID() != 0)
      {
        dynaForm.setActivityID(Integer.toString(timeSlipVO.getTaskID()));
      }

      if (timeSlipVO.getDescription() != null)
      {
        dynaForm.setDescription(timeSlipVO.getDescription());
      }

      if (timeSlipVO.getTicketID() != 0)
      {
        dynaForm.setTicketID(Integer.toString(timeSlipVO.getTicketID()));
      }
      else
      {
        dynaForm.setTicketID("0");
      }

      if (timeSlipVO.getTicket() != null)
      {
        dynaForm.setTicket(timeSlipVO.getTicket());
        if(!timeSlipVO.getTicket().equals("")){
        	dynaForm.setReference(timeSlipVO.getTicket());
        	dynaForm.setLookupList("2");
        }
      }

      if (timeSlipVO.getDate() != null)
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeSlipVO.getDate());
        dynaForm.setDay(new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString());
        dynaForm.setMonth(new Integer(calendar.get(Calendar.MONTH) + 1).toString());
        dynaForm.setYear(new Integer(calendar.get(Calendar.YEAR)).toString());
      }

      if (timeSlipVO.getStart() != null)
      {
        tStartTime = (Time) timeSlipVO.getStart();

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(tStartTime);

        startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);
        startMins = calendarStart.get(Calendar.MINUTE);

        DateFormat df = new SimpleDateFormat("h:mm a");

        dynaForm.setStartTime((df.format(timeSlipVO.getStart())).toString());
      }

      if (timeSlipVO.getEnd() != null)
      {
        tEndTime = (Time) timeSlipVO.getEnd();

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(tEndTime);

        endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);
        endMins = calendarEnd.get(Calendar.MINUTE);

        DateFormat df = new SimpleDateFormat("h:mm a");

        dynaForm.setEndTime((df.format(timeSlipVO.getEnd())).toString());
      }

      if (timeSlipVO.getBreakTime() != -1)
      {
        Float BreakTime = new Float(timeSlipVO.getBreakTime());
        int iHours = BreakTime.intValue();
        float breakTime = (BreakTime.floatValue() - iHours) * 60;
        Float Mins = new Float(breakTime);
        int iMins = Mins.intValue();

        dynaForm.setBreakHours(new Integer(iHours).toString());
        dynaForm.setBreakMinutes(new Integer(iMins).toString());

        float fBreakTime = timeSlipVO.getBreakTime();
        float remMin = 0;

        if ((startMins != 0) && (endMins != 0) && (startMins != endMins))
        {
          if (endMins > startMins)
          {
            remMin = 60 / (endMins - startMins);
          }
          else
          {
            remMin = 60 / (startMins - endMins);
          }
        }

        Float Duration = new Float(((endHrs - startHrs) + remMin) - fBreakTime);
        iHours = Duration.intValue();

        float fMins = (Duration.floatValue() - iHours) * 60;
        Mins = new Float(fMins);
        iMins = Mins.intValue();

        dynaForm.setDuration(new Integer(iHours).toString() + " hours, "
          + new Integer(Math.round(fMins)).toString() + " minutes ");
      }
      returnStatus = ".view.projects.edit.timeslip";
    }
    catch (Exception e)
    {
      System.out.println("[Exception] ViewTimeSlipDetailHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    return (mapping.findForward(returnStatus));
  }
}
