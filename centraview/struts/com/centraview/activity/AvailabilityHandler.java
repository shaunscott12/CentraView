/*
 * $RCSfile: AvailabilityHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/20 20:22:18 $ - $Author: mcallist $
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

package com.centraview.activity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.calendar.CalendarActivityObject;
import com.centraview.calendar.CvCalendar;
import com.centraview.calendar.CvCalendarHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;

public class AvailabilityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(AvailabilityHandler.class);
  /**
   * checks the user and activities for the user and sends the request hashmap
   * as response to the availabe_frame.jsp page
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    UserPrefererences prefs = userObject.getUserPref();
    TimeZone tz = TimeZone.getTimeZone(prefs.getTimeZone());
    Locale locale = request.getLocale();
    
    PopulateForm populateForm = new PopulateForm();
    // set the form elements
    populateForm.setForm(request, form);
    // set form with respect to new opening page
    form = populateForm.getForm(request, form, ConstantKeys.AVAILABILITY);
    ActivityForm activityForm = (ActivityForm)form;

    String yearParam = request.getParameter("YEAR");
    String monthParam = request.getParameter("MONTH");
    String dayParam = request.getParameter("DAY");

    GregorianCalendar startDate = new GregorianCalendar(tz, locale);
    GregorianCalendar endDate = null;
    String startDateString = activityForm.getActivityStartDate();
    if (CVUtility.empty(startDateString)) {
      startDate.setTime(Calendar.getInstance(tz, locale).getTime());
      startDate.set(Calendar.HOUR, 0);
      startDate.set(Calendar.MINUTE, 0);
      startDate.set(Calendar.SECOND, 0);
      startDate.set(Calendar.MILLISECOND, 0);
    } else if (CVUtility.notEmpty(yearParam) && CVUtility.notEmpty(monthParam) && CVUtility.notEmpty(dayParam)) {
      startDate.set(Calendar.MONTH, Integer.parseInt(monthParam) - 1);
      startDate.set(Calendar.DATE, Integer.parseInt(dayParam));
      startDate.set(Calendar.YEAR, Integer.parseInt(yearParam));
    } else {
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
      startDate.setTime(df.parse(startDateString));
    }
    endDate = new GregorianCalendar(tz, locale);
    endDate.setTime(startDate.getTime());
    endDate.add(Calendar.HOUR, 24);

    // user activities hashmap
    HashMap userActivities = null;
    // contains user name and id taken from string[]s from formbean
    HashMap userIdNames = new HashMap();
    // time span for hours
    int timespan = 60;

    // get the attendeerequired from the formbean
    String[] attendeesRequired = activityForm.getActivityAttendeesRequired();

    // get the attendeOptional from the formbean
    String[] attendeeOptional = activityForm.getActivityAttendeesOptional();

    // check for the optional and required users .if exist check for their
    // activities
    if ((attendeeOptional != null) || (attendeesRequired != null)) {
      Vector vec = new Vector();
      if (attendeeOptional != null) {
        for (int count = 0; count < attendeeOptional.length; count++) {
          int indexOfHash = attendeeOptional[count].indexOf("#");
          String idName = attendeeOptional[count].substring(0, indexOfHash);
          vec.add(idName);
          String displayName = attendeeOptional[count].substring(indexOfHash + 1,
              attendeeOptional[count].length());
          userIdNames.put(new Integer(idName), new AvailableList(Integer.parseInt(idName),
              displayName));
        }
      }

      if (attendeesRequired != null) {
        for (int reqCount = 0; reqCount < attendeesRequired.length; reqCount++) {
          int indexOfHash = attendeesRequired[reqCount].indexOf("#");
          String name = attendeesRequired[reqCount].substring(0, indexOfHash);
          vec.add(name);
          String dispName = attendeesRequired[reqCount].substring(indexOfHash + 1,
              attendeesRequired[reqCount].length());
          userIdNames.put(new Integer(name), new AvailableList(Integer.parseInt(name), dispName));
        }
      }

      int attendeeSize = userIdNames.size();
      int userId[] = new int[attendeeSize];

      Set availElement = userIdNames.keySet();
      Iterator userID = availElement.iterator();
      int toArr = 0;

      while (userID.hasNext()) {
        userId[toArr] = ((Integer)userID.next()).intValue();
        toArr++;
      }

      try {
        CvCalendarHome ch = (CvCalendarHome)CVUtility.getHomeObject(
            "com.centraview.calendar.CvCalendarHome", "CvCalendar");
        CvCalendar cr = ch.create();
        cr.setDataSource(dataSource);
        userActivities = cr.getAvailibility(1, userId, startDate, endDate);
      } catch (Exception e) {
        logger.error("[execute]: Exception", e);
      }

      // check for the users in the hashmap and get the vector of activities
      Set setElement = userActivities.keySet();
      Iterator allUsers = setElement.iterator();

      // check for all users
      while (allUsers.hasNext()) {
        // get user id
        Integer user = (Integer)allUsers.next();
        Vector activityObjects = (Vector)userActivities.get(user);
        AvailableList avlList = (AvailableList)userIdNames.get(user);
        boolean activityArray[] = avlList.getAvailable();
        Enumeration activityEnum = activityObjects.elements();

        while (activityEnum.hasMoreElements()) {
          CalendarActivityObject activityobject = (CalendarActivityObject)activityEnum
              .nextElement();

          GregorianCalendar starttime = activityobject.getStartTime();
          GregorianCalendar endtime = activityobject.getEndTime();

          GregorianCalendar endTimeuser = endDate;
          GregorianCalendar starttimeuser = startDate;

          if ((starttime.getTimeInMillis() > endTimeuser.getTimeInMillis())
              || (endtime.getTimeInMillis() < starttimeuser.getTimeInMillis())) {
            continue;
          }

          long timeInMillisStart = starttime.getTimeInMillis()
              + starttime.get(Calendar.ZONE_OFFSET) + starttime.get(Calendar.DST_OFFSET);
          long timeInMillisEnd = endtime.getTimeInMillis() + endtime.get(Calendar.ZONE_OFFSET)
              + endtime.get(Calendar.DST_OFFSET);

          long timeInMillisStartUser = starttimeuser.getTimeInMillis()
              + starttimeuser.get(Calendar.ZONE_OFFSET) + starttimeuser.get(Calendar.DST_OFFSET);
          long timeInMillisEndUser = endTimeuser.getTimeInMillis()
              + endTimeuser.get(Calendar.ZONE_OFFSET) + endTimeuser.get(Calendar.DST_OFFSET);

          int diffin_min = (int)(Math.min(timeInMillisEnd, timeInMillisEndUser) - Math.max(
              timeInMillisStart, timeInMillisStartUser)) / 60000;
          double dnumrows = (double)diffin_min / (double)timespan;
          int nuOfrows = (int)Math.ceil(dnumrows);

          diffin_min = (int)((Math.max(timeInMillisStart, timeInMillisStartUser) - timeInMillisStartUser) / 60000);
          double memeberfrom = (double)diffin_min / (double)timespan;

          int insertmemeberfrom = (int)Math.ceil(memeberfrom);

          // insert rows from
          for (int i = 0; i < nuOfrows; i++) {
            activityArray[insertmemeberfrom] = true;
            insertmemeberfrom++;
          }
        }
      }
      request.setAttribute("availabilityList", userIdNames);
    } else {
      request.setAttribute("availabilityList", userIdNames);
    }
    request.setAttribute("refreshWindow", "true");
    request.setAttribute("startDate", startDate);
    String typeOper = request.getParameter(ConstantKeys.TYPEOFOPERATION);
    request.setAttribute(ConstantKeys.TYPEOFOPERATION, typeOper);
    return (mapping.findForward(".view.activities.availability"));
  }

}
