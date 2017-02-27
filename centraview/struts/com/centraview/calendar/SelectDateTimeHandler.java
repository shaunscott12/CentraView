/*
 * $RCSfile: SelectDateTimeHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/21 16:32:51 $ - $Author: mcallist $
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

import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;

/**
 * Handles the request for the Select Date and Time popup window, allowing
 * current month/year to be selected.
 */
public class SelectDateTimeHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SelectDateTimeHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception {
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    UserPrefererences prefs = user.getUserPref();
    TimeZone tz = TimeZone.getTimeZone(prefs.getTimeZone());
    Locale locale = request.getLocale();
    
    String forward = ".view.calendar.datetime";
    // "dateTimeSelectForm" in mappings/calendar.xml
    DynaActionForm dateForm = (DynaActionForm) form;
    try {
      // get the the current system date and time and set it to the form
      GregorianCalendar currentDate = new GregorianCalendar(tz, locale);
      dateForm.set("currentDate", currentDate);
      request.setAttribute("currentDate", currentDate);
      // dateTimeType
      // Type 1 You can set the date.
      // Type 2 You can set the Date and Time both
      // Type 3 You can set the Start Date and end Date
      // Type 4 You can set the date, Start Time and End Time.
      // Type 5 You can set the Start DAte, Start time , End Date & End Time..
      Integer dateTimeType = (Integer) dateForm.get("dateTimeType");
      if (dateTimeType == null) {
        dateTimeType = new Integer(Constants.BothDateTime);
      }
      dateForm.set("dateTimeType", dateTimeType);

      Boolean showWeeklyColumn = (Boolean) dateForm.get("showWeeklyColumn");
      if (showWeeklyColumn == null) {
        request.setAttribute("showWeeklyColumn", new Boolean(false));
      } else {
        request.setAttribute("showWeeklyColumn", new Boolean(true));
      }

      dateForm.set("dateTimeType", dateTimeType);
      // get the month and year parameters from the request.
      // These will be used to set the selectedDate value.
      Integer month = (Integer) dateForm.get("month");
      if (month == null) {
        month = new Integer(currentDate.get(GregorianCalendar.MONTH) + 1);
      }

      Integer year = (Integer) dateForm.get("year");
      if (year == null) {
        year = new Integer(currentDate.get(GregorianCalendar.YEAR));
      }

      // This value will display the selected month in the mini calendar.
      GregorianCalendar selectedDate = new GregorianCalendar(year.intValue(), month.intValue() - 1, 1);
      dateForm.set("selectedDate", selectedDate);
      request.setAttribute("selectedDate", selectedDate);

      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
      // simple mode basicall is non-calendar/activities mode, because
      // the following behaviour is unexpected in sales.
      boolean simpleMode = ((Boolean)dateForm.get("simpleMode")).booleanValue();
      String startDate = (String) dateForm.get("startDate");
      String endDate = (String) dateForm.get("endDate");
      if (!simpleMode) {
        if ((startDate == null || startDate.length() < 1) && (endDate == null || endDate.length() < 1)) {
          // if startDate *AND* endDate are not already set, then
          // set them both to the current date in the format MM/dd/yyyy
          startDate = df.format(currentDate.getTime());
          endDate = startDate;
        }
      }
      dateForm.set("startDate", startDate);
      dateForm.set("endDate", endDate);

      // if startTime is given, but endTime is NOT, then set the
      // endTime = startTime + 1 hour
      String startTime = (String) dateForm.get("startTime");
      String endTime = (String) dateForm.get("endTime");
      if ((startTime != null && startTime.length() > 0) && (endTime == null || endTime.length() <= 0)) {
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        GregorianCalendar endTimeCal = new GregorianCalendar();
        // get a Calendar object generated from the given startTime String
        endTimeCal.setTime(tf.parse(startTime));
        // add one hour to the Calendar object
        endTimeCal.set(GregorianCalendar.HOUR_OF_DAY, endTimeCal.get(GregorianCalendar.HOUR_OF_DAY) + 1);
        // get the String representation of the Calendar object, using the
        // SimpleDateFormat
        String defaultEndTime = tf.format(endTimeCal.getTime());
        // set the new endTime String to the form
        dateForm.set("endTime", defaultEndTime);
      }
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return (mapping.findForward(forward));
  } // end execute() method
} // end class definition
