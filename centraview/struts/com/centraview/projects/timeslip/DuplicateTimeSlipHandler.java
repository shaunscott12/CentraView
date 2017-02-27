/*
 * $RCSfile: DuplicateTimeSlipHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:32 $ - $Author: mking_cv $
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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



public class DuplicateTimeSlipHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    try
    {
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();

      ProjectFacadeHome pfh = (ProjectFacadeHome) 
        CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome","ProjectFacade");
      ProjectFacade remote = (ProjectFacade) pfh.create();
      remote.setDataSource(dataSource);
      String[] rowId = null;

      if (request.getParameterValues("rowId") != null)
      {
        rowId = request.getParameterValues("rowId");
      }

      TimeSlipVO tvo = remote.getTimeSlip(Integer.parseInt(rowId[0]), individualID);
      TimeSlipForm tsForm = (TimeSlipForm) form;

      if (tvo.getProjectTitle() != null)
      {
        tsForm.setProjectID("" + tvo.getProjectID());
        tsForm.setProjectID("" + tvo.getProjectID());
        tsForm.setProject("" + tvo.getProjectTitle());
        tsForm.setReference(tvo.getProjectTitle());
        tsForm.setLookupList("1");
      }

      if (tvo.getTicket() != null)
      {
      	tsForm.setTicketID(Integer.toString(tvo.getTicketID()));
      	tsForm.setTicket(tvo.getTicket());
      	tsForm.setReference(tvo.getTicket());
      	tsForm.setLookupList("2");
      }
      
	      tsForm.setReference(tvo.getProjectTitle());
	      tsForm.setLookupList("1");      
      if (tvo.getTaskTitle() != null)
      {
        tsForm.setActivityID("" + tvo.getTaskID());
        tsForm.setTask("" + tvo.getTaskTitle());
      }

      tsForm.setDescription("" + tvo.getDescription());

      if (tvo.getDate() != null)
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tvo.getDate());
        tsForm.setDay(new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString());
        tsForm.setMonth(new Integer(calendar.get(Calendar.MONTH) + 1).toString());
        tsForm.setYear(new Integer(calendar.get(Calendar.YEAR)).toString());
      }

      Calendar calendarStart = Calendar.getInstance();
      calendarStart.setTime(tvo.getStart());

      int startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);
      int startMins = calendarStart.get(Calendar.MINUTE);
      DateFormat df = new SimpleDateFormat("h:mm a");

      tsForm.setStartTime((df.format(tvo.getStart())).toString());

      Calendar calendarEnd = Calendar.getInstance();
      calendarEnd.setTime(tvo.getEnd());

      int endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);
      int endMins = calendarEnd.get(Calendar.MINUTE);
      tsForm.setEndTime((df.format(tvo.getEnd())).toString());

      Float BreakTime = new Float(tvo.getBreakTime());
      int iHours = BreakTime.intValue();
      float breakTime = (BreakTime.floatValue() - iHours) * 60;
      Float Mins = new Float(breakTime);
      int iMins = Mins.intValue();

      tsForm.setBreakHours(new Integer(iHours).toString());
      tsForm.setBreakMinutes(new Integer(iMins).toString());

      returnStatus = ".view.projects.new.timeslip";
    }
    catch (Exception e)
    {
      System.out.println("[Exception] DuplicateTimeSlipHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    return (mapping.findForward(returnStatus));
  } //end of execute method
} //end of DuplicateTimeSlipHandler class
