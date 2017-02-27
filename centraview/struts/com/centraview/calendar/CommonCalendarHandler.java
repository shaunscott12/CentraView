/*
 * $RCSfile: CommonCalendarHandler.java,v $    $Revision: 1.6 $  $Date: 2005/09/13 22:02:59 $ - $Author: mcallist $
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.activity.ConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.Delegator;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;

public class CommonCalendarHandler extends Action {
  private static Logger logger = Logger.getLogger(CommonCalendarHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException, CommunicationException, NamingException
  {
    String Final_Forward = ".view.calendar.dailylist";
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String type = null;
    CalendarList DL = null;
    HashMap unscheduledactivity = null;
    HashMap scheduledactivity = null;

    DynaActionForm calendardyna = (DynaActionForm) form;

    try {
      HttpSession session = request.getSession(true);
      UserObject userobject = (UserObject) session.getAttribute("userobject");
      int userID = userobject.getIndividualID();
      int individualID = userobject.getIndividualID();

      // Delegation
      if (session.getAttribute("delegatorName") != null) {
        userID = Integer.parseInt((String) session.getAttribute("delegatorName"));
      }
      if (request.getParameter("delegatorName") != null) {
        userID = Integer.parseInt(request.getParameter("delegatorName"));
      }

      session.setAttribute("delegatorName", String.valueOf(userID));
      calendardyna.set("delegatorName", session.getAttribute("delegatorName"));

      Vector userColl = Delegator.getCalendarDelegatorIds(individualID, com.centraview.common.Constants.ACTIVITYMODULE,
          com.centraview.common.Constants.VIEW, dataSource);
      DDNameValue mydd = new DDNameValue(String.valueOf(individualID), (userobject.getfirstName() + ' ' + userobject.getlastName()));
      userColl.add(mydd);
      request.setAttribute("delegatorNameVec", userColl);

      // Selected Acitivity Type
      String activityType = request.getParameter("activityType");

      if (session.getAttribute("activityType") == null) {
        session.setAttribute("activityType", "All");
      }

      if (activityType != null) {
        session.setAttribute("activityType", activityType);
      }

      activityType = (String) session.getAttribute("activityType");
      calendardyna.set("activityType", activityType);

      Vector activityTypeVec = new Vector();

      activityTypeVec.add(new DDNameValue("All", "All Activities"));
      activityTypeVec.add(new DDNameValue("Appointment", "Appointment"));
      activityTypeVec.add(new DDNameValue("Meeting", "Meeting"));
      activityTypeVec.add(new DDNameValue("Call", "Call"));
      activityTypeVec.add(new DDNameValue("NextAction", "NextAction"));
      activityTypeVec.add(new DDNameValue("OpportunityInfo", "OpportunityInfo"));
      activityTypeVec.add(new DDNameValue("ProjectInfo", "ProjectInfo"));
      activityTypeVec.add(new DDNameValue("Event", "Event"));
      activityTypeVec.add(new DDNameValue("Task", "Task"));
      request.setAttribute("activityTypeVec", activityTypeVec);

      // What the hell??? what is the purpose of the default values here?
      int timespan = 30;
      TimeZone tz = null;

      // get time zone from parameter or from preferences; worst case default to
      // EST.
      try {
        if (request.getParameter("timeZone") != null) {
          tz = TimeZone.getTimeZone(request.getParameter("timeZone"));
        } else {
          tz = TimeZone.getTimeZone((userobject.getUserPref()).getTimeZone());
        }
      } catch (Exception e) {
        tz = TimeZone.getTimeZone("EST");
      }
      request.setAttribute("TZ", tz);

      GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector timeZoneVec = (Vector) globalMasterLists.get("TimeZone");
      request.setAttribute("timeZoneVec", timeZoneVec);
      String timeZoneID = tz.getID();
      calendardyna.set("timeZone", timeZoneID);

      request.setAttribute("showAdvancedSearch", new Boolean(false));
      request.setAttribute("showCustomViews", new Boolean(false));
      request.setAttribute("showComposeButton", new Boolean(false));
      request.setAttribute("showPrintButton", new Boolean(false));
      request.setAttribute("searchButtonDescription", "Perform a quick search for activities information.");
      request.setAttribute("newButtonValue", "Schedule");
      HashMap moduleList = new HashMap();

      if (globalMasterLists.get("moduleList") != null) {
        moduleList = (HashMap) globalMasterLists.get("moduleList");
      }

      String moduleID = (String) moduleList.get("Activities");
      request.setAttribute("moduleId", moduleID);

      // if the type wasn't set on the parameter
      // use the user preferences defaulting to daily view.
      type = request.getParameter("Type");
      if (type == null || type.length() <= 0) {
        // Get the preferred view from the Preferences
        UserPrefererences up = userobject.getUserPref();
        String prefView = (up.getCalendarDefaultView() == null) ? "DAILY" : up.getCalendarDefaultView(); // default
                                                                                                          // it
                                                                                                          // to
                                                                                                          // DAILY
        if (prefView.equals("DAILY")) {
          type = "DAILY";
        } else if (prefView.equals("MONTHLY")) {
          type = "MONTHLY";
        } else if (prefView.equals("WEEKLY")) {
          type = "WEEKLY";
        } else if (prefView.equals("WEEKLYCOLUMNS")) {
          type = "WEEKLYCOLUMNS";
        } else if (prefView.equals("YEARLY")) {
          type = "YEARLY";
        }
        request.setAttribute("Type", type);
      }

      // Default start and end time. are today, 0:00 through tomorrow 0:00
      GregorianCalendar startTimeUser = new GregorianCalendar(tz);

      // Set the default starting day of week to Monday.
      // TODO: make a preference for the starting day of the week, and use it
      // here.
      startTimeUser.setFirstDayOfWeek(Calendar.MONDAY);
      startTimeUser.setTimeInMillis(System.currentTimeMillis());
      int currentDay = startTimeUser.get(Calendar.DATE);
      int currentMonth = startTimeUser.get(Calendar.MONTH);
      int currentYear = startTimeUser.get(Calendar.YEAR);

      GregorianCalendar currentDateCalendar = new GregorianCalendar(currentYear, currentMonth, currentDay);
      request.setAttribute("currentDate", currentDateCalendar);

      String monthName = CalendarUtil.getCalenderMonthName(currentMonth);
      calendardyna.set("currentDate", monthName + " " + currentDay + ", " + currentYear);
      calendardyna.set("currentDay", new Integer(currentDay));
      calendardyna.set("currentMonth", new Integer(currentMonth));
      calendardyna.set("currentMonthName", monthName);
      calendardyna.set("currentYear", new Integer(currentYear));

      // the sDay, sMonth and sYear is the date set by the navigation or
      // selection
      String selectedDay = request.getParameter("selectedDay");
      String selectedMonthName = request.getParameter("selectedMonthName");
      String selectedYear = request.getParameter("selectedYear");

      // Check to see is session exists, if not set to default
      if (session.getAttribute("selectedDay") == null) {
        session.setAttribute("selectedDay", (new Integer(currentDay)).toString());
      }

      if (session.getAttribute("selectedMonthName") == null) {
        session.setAttribute("selectedMonthName", monthName);
      }

      if (session.getAttribute("selectedYear") == null) {
        session.setAttribute("selectedYear", (new Integer(currentYear)).toString());
      }

      // If request passes in selected day, set session to new value
      if (selectedDay != null) {
        session.setAttribute("selectedDay", selectedDay);
      }

      if (selectedMonthName != null) {
        session.setAttribute("selectedMonthName", selectedMonthName);
      }

      if (selectedYear != null) {
        session.setAttribute("selectedYear", selectedYear);
      }

      // set local objects to value stored in session
      selectedDay = (String) session.getAttribute("selectedDay");
      selectedMonthName = (String) session.getAttribute("selectedMonthName");
      selectedYear = (String) session.getAttribute("selectedYear");

      int day = Integer.parseInt(selectedDay);
      int month = (selectedMonthName == null) ? currentMonth : CalendarUtil.getCalendarMonth(selectedMonthName);
      int year = (selectedYear == null) ? currentYear : Integer.parseInt(selectedYear);
      if (selectedMonthName == null) {
        selectedMonthName = CalendarUtil.getCalenderMonthName(currentMonth);
      }

      GregorianCalendar selectedDate = new GregorianCalendar(year, month, 1);
      request.setAttribute("selectedDate", selectedDate);
      request.setAttribute("showWeeklyColumn", new Boolean(true));
      calendardyna.set("selectedDate", selectedMonthName + " " + day + ", " + year);
      calendardyna.set("selectedDay", String.valueOf(day));
      calendardyna.set("selectedYear", String.valueOf(year));
      calendardyna.set("selectedMonthName", selectedMonthName);

      // Selected Day midnight
      startTimeUser.clear();
      startTimeUser.set(year, month, day);

      HashMap calendarNavBar = CalendarUtil.setCalendarNavBar(year, month, day, type);
      calendardyna.set("calendarNavBar", calendarNavBar.get("calendarNavBar"));
      calendardyna.set("headerList", calendarNavBar.get("headerList"));
      calendardyna.set("headerLinkList", calendarNavBar.get("headerLinkList"));
      calendardyna.set("dateList", calendarNavBar.get("dateList"));
      calendardyna.set("startDayOfWeek", calendarNavBar.get("startDayOfWeek"));

      GregorianCalendar endTimeUser = new GregorianCalendar(tz);
      endTimeUser.setTime(startTimeUser.getTime());
      endTimeUser.add(Calendar.DATE, 1); // Selected Day+1 midnight

      if (type.equals("DAILY")) {
        // Daily view, set the timespan to 15, 30 or 60
        // minutes per table row, based on the dropdown.
        if (request.getParameter("timespan") != null) {
          timespan = Integer.parseInt(request.getParameter("timespan"));
        }
        startTimeUser.set(year, month, day, 0, 0);
        endTimeUser.setTime(startTimeUser.getTime());
        endTimeUser.add(Calendar.DATE, 1);
        Final_Forward = ".view.calendar.dailylist";
      } else if (type.equals("MONTHLY")) {
        // Monthly View
        // That means timespan is 1440 for some reason. I guess
        // the math works out that way .. ?!
        timespan = 1440;
        // start is first of the month
        startTimeUser.set(year, month, 1, 0, 0);
        // end is first of NEXT month
        endTimeUser.setTime(startTimeUser.getTime());
        endTimeUser.add(Calendar.MONTH, 1);
        Final_Forward = ".view.calendar.monthlylist";
      } else if (type.equals("WEEKLY")) {
        // Weekly (NON-columnar) view
        // That means timespan is 1440 for some reason. I guess
        // the math works out that way .. ?!
        timespan = 1440;
        // in the non-columnar view we always start the week on Monday.
        startTimeUser.setFirstDayOfWeek(Calendar.MONDAY);
        startTimeUser = CalendarUtil.setToStartOfWeek(startTimeUser);
        endTimeUser.setTime(startTimeUser.getTime());
        endTimeUser.add(Calendar.DAY_OF_MONTH, 7);
        Final_Forward = ".view.calendar.weeklylist";
      } else if (type.equals("WEEKLYCOLUMNS")) {
        timespan = 60;
        startTimeUser.set(Calendar.DAY_OF_WEEK, startTimeUser.getFirstDayOfWeek());
        startTimeUser = CalendarUtil.setToStartOfWeek(startTimeUser);
        endTimeUser.setTime(startTimeUser.getTime());
        endTimeUser.add(Calendar.DAY_OF_MONTH, 7);
        Final_Forward = ".view.calendar.weeklycolumnarlist";
      } else if (type.equals("YEARLY")) {
        Final_Forward = ".view.calendar.yearlylist";
        return (mapping.findForward(Final_Forward));
      }

      request.setAttribute("Month", String.valueOf(month));

      HashMap lgmap = null;
      lgmap = CalendarUtil.getCalendarCollection(startTimeUser, endTimeUser, tz, userID, timespan, "", dataSource);
      DL = (CalendarList) lgmap.get("calendarlist");

      unscheduledactivity = (HashMap) lgmap.get("unscheduledactivity");
      scheduledactivity = (HashMap) lgmap.get("scheduledactivity");
      TreeMap allDayActivity = (TreeMap) lgmap.get("allDayActivity");

      Iterator arrIt = lgmap.keySet().iterator();
      while (arrIt.hasNext()) {
        String key = (String) arrIt.next();
        if (key.equals("calendarlist")) {
          CalendarList cl = (CalendarList) lgmap.get(key);
          Iterator it = cl.keySet().iterator();
          while (it.hasNext()) {
            Object o = cl.get(it.next());
            if (o instanceof CalendarListElement) {
              CalendarListElement ele = (CalendarListElement) o;
              Set setmember = ele.keySet();
              Iterator itMember = setmember.iterator();
              while (itMember.hasNext()) {
                Object keyValue = itMember.next();
                CalendarMember elemember = (CalendarMember) ele.get(keyValue);
                CalendarActivityObject calActivity = elemember.getActivityobject();
                ArrayList activitiesAttendeesList = calActivity.getActivityAttendee();
                if ("PRIVATE".equals(calActivity.getActivityVisibility()) && calActivity.getActivityOwnerId() != individualID
                    && !activitiesAttendeesList.contains(individualID + "")) {
                  calActivity.setActivity("Private");
                  calActivity.setActivityDetail("Private");
                }
              }
            }
          }
          continue;
        }

        Map map = (Map) lgmap.get(key);
        Iterator it = map.keySet().iterator();

        while (it.hasNext()) {
          CalendarActivityObject cao = (CalendarActivityObject) map.get(it.next());
          ArrayList activitiesAttendeesList = cao.getActivityAttendee();
          if ("PRIVATE".equals(cao.getActivityVisibility()) && cao.getActivityOwnerId() != individualID
              && !activitiesAttendeesList.contains(individualID + "")) {
            cao.setActivity("Private");
            cao.setActivityDetail("Private");
          }
        }
      }

      DL.setSearchString(request.getParameter("searchTextBox"));
      request.setAttribute("allDayActivity", allDayActivity);

      ArrayList nonCalendarActivityList = new ArrayList();
      if (scheduledactivity != null) {
        Set scheduleSet = scheduledactivity.keySet();
        Iterator scheduleIt = scheduleSet.iterator();
        while (scheduleIt.hasNext()) {
          CalendarActivityObject ele = (CalendarActivityObject) scheduledactivity.get(scheduleIt.next());
          String activitytype = ele.getActivityType();
          String iconFileName = CalendarUtil.getActivityIconFileName(activitytype);
          String activityTitle = ele.getActivity();
          StringBuffer displayLink = new StringBuffer("/activities/view_activity.do?rowId=");
          displayLink.append(ele.getActivityID());
          HashMap nonCalenderActivityMap = new HashMap();
          nonCalenderActivityMap.put("activityIcon", iconFileName);
          nonCalenderActivityMap.put("activityLink", displayLink.toString());
          nonCalenderActivityMap.put("activityTitle", activityTitle);
          nonCalendarActivityList.add(nonCalenderActivityMap);
        }// end while (it.hasNext())
      }
      if (unscheduledactivity != null) {
        Set unScheduleSet = unscheduledactivity.keySet();
        Iterator unScheduleIt = unScheduleSet.iterator();
        while (unScheduleIt.hasNext()) {
          CalendarActivityObject ele = (CalendarActivityObject) unscheduledactivity.get(unScheduleIt.next());
          String activitytype = ele.getActivityType();
          String iconFileName = CalendarUtil.getActivityIconFileName(activitytype);
          StringBuffer displayLink = new StringBuffer("/activities/view_activity.do?rowId=");
          displayLink.append(ele.getActivityID());
          String activityTitle = ele.getActivity();
          HashMap nonCalenderActivityMap = new HashMap();
          nonCalenderActivityMap.put("activityIcon", iconFileName);
          nonCalenderActivityMap.put("activityLink", displayLink.toString());
          nonCalenderActivityMap.put("activityTitle", activityTitle);
          nonCalendarActivityList.add(nonCalenderActivityMap);
        }
      }
      calendardyna.set("nonCalendarActivity", nonCalendarActivityList);

      // set the Icon and the links on the calendar elements.
      Set listkey = DL.keySet();
      Iterator it = listkey.iterator();
      while (it.hasNext()) {
        CalendarListElement ele = (CalendarListElement) DL.get(it.next());
        Set elekey = ele.keySet();
        Iterator eleit = elekey.iterator();
        while (eleit.hasNext()) {
          CalendarMember calmember = (CalendarMember) ele.get(eleit.next());
          CalendarActivityObject calActivity = calmember.getActivityobject();

          String activitytype = calActivity.getActivityType();
          String iconFileName = CalendarUtil.getActivityIconFileName(activitytype);
          calmember.setIcon(iconFileName);

          if ((calActivity.getActivity() != null) && (!calActivity.getActivity().equals("Private"))) {
            calmember.setRequestURL("c_openPopup('/activities/view_activity.do?rowId=" + (calActivity.getActivityID()).intValue() + "&"
                + ConstantKeys.TYPEOFACTIVITY + "=" + activitytype + "')");
          } else {
            calmember.setRequestURL("void(0);");
          }
        }
      } // end while (eleit.hasNext())
      request.setAttribute("displaylist", DL);
    } catch (Exception e) {
      logger.error("[Exception][CommonCalendarHandler.execute] Exception Thrown: ", e);
      e.printStackTrace();
      return (mapping.findForward("failure"));
    }
    calendardyna.set("selectView", type);
    request.setAttribute("calendarForm", calendardyna);
    return (mapping.findForward(Final_Forward));
  } // end execute(...)

}
