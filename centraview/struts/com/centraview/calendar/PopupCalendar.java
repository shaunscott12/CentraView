/*
 * $RCSfile: PopupCalendar.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:53 $ - $Author: mking_cv $
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
package com.centraview.calendar;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PopupCalendar extends Action
{
  private static Logger logger = Logger.getLogger(PopupCalendar.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
  	  String Final_Forward = ".view.calendar.popup.calendar";
  	  
  	  TimeZone tz = TimeZone.getTimeZone("EST");
  	  GregorianCalendar startTimeUser = new GregorianCalendar(tz);
      startTimeUser.setFirstDayOfWeek(Calendar.MONDAY);
      startTimeUser.setTimeInMillis(System.currentTimeMillis());
      int currentDay = startTimeUser.get(Calendar.DATE);
      int currentMonth = startTimeUser.get(Calendar.MONTH);
      int currentYear = startTimeUser.get(Calendar.YEAR);
      request.setAttribute("currentDate", startTimeUser);

      String selectedDay = request.getParameter("selectedDay");
      String selectedMonthName = request.getParameter("selectedMonthName");
      String selectedYear = request.getParameter("selectedYear");
      int day = (selectedDay == null) ? currentDay : Integer.parseInt(selectedDay);
      int month = (selectedMonthName == null) ? currentMonth : CalendarUtil.getCalendarMonth(selectedMonthName);
      int year = (selectedYear == null) ? currentYear : Integer.parseInt(selectedYear);
      if(selectedMonthName == null){
      	selectedMonthName = CalendarUtil.getCalenderMonthName(currentMonth);
      }
      
      GregorianCalendar selectedDate = new GregorianCalendar(year, month, 1);
      request.setAttribute("selectedDate", selectedDate);
      request.setAttribute("showWeeklyColumn", new Boolean(true));
      return (mapping.findForward(Final_Forward));
  }   // end execute(...)

}
