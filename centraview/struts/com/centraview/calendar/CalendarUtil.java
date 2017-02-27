/*
 * $RCSfile: CalendarUtil.java,v $    $Revision: 1.3 $  $Date: 2005/09/13 21:59:11 $ - $Author: mcallist $
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;

/**
 * This class will provide static methods for Calendar to be able to get stuff
 * to populate the calendarFilterBar etc.,
 * @author Naresh Patel <npatel@centraview.com>
 */
public final class CalendarUtil {
  private static Logger logger = Logger.getLogger(CalendarUtil.class);
  public static int getCalendarMonth(String monthName)
  {
    int monthNumber = 0;
    if ((monthName.equals("January")) | (monthName.equals("Jan")) | (monthName.equals("JAN")) | (monthName.equals("JANUARY"))) {
      monthNumber = Calendar.JANUARY;
    }
    if ((monthName.equals("February")) | (monthName.equals("Feb")) | (monthName.equals("FEB")) | (monthName.equals("FEBRUARY"))) {
      monthNumber = Calendar.FEBRUARY;
    }
    if ((monthName.equals("March")) | (monthName.equals("Mar")) | (monthName.equals("MAR")) | (monthName.equals("MARCH"))) {
      monthNumber = Calendar.MARCH;
    }
    if ((monthName.equals("April")) | (monthName.equals("Apr")) | (monthName.equals("APR")) | (monthName.equals("APRIL"))) {
      monthNumber = Calendar.APRIL;
    }
    if ((monthName.equals("May")) | (monthName.equals("May")) | (monthName.equals("MAY"))) {
      monthNumber = Calendar.MAY;
    }
    if ((monthName.equals("June")) | (monthName.equals("Jun")) | (monthName.equals("JUN")) | (monthName.equals("JUNE"))) {
      monthNumber = Calendar.JUNE;
    }
    if ((monthName.equals("July")) | (monthName.equals("Jul")) | (monthName.equals("JUL")) | (monthName.equals("JULY"))) {
      monthNumber = Calendar.JULY;
    }
    if ((monthName.equals("August")) | (monthName.equals("Aug")) | (monthName.equals("AUG")) | (monthName.equals("AUGUST"))) {
      monthNumber = Calendar.AUGUST;
    }
    if ((monthName.equals("September")) | (monthName.equals("Sep")) | (monthName.equals("SEP")) | (monthName.equals("SEPTEMBER"))) {
      monthNumber = Calendar.SEPTEMBER;
    }
    if ((monthName.equals("October")) | (monthName.equals("Oct")) | (monthName.equals("OCT")) | (monthName.equals("OCTOBER"))) {
      monthNumber = Calendar.OCTOBER;
    }
    if ((monthName.equals("November")) | (monthName.equals("Nov")) | (monthName.equals("NOV")) | (monthName.equals("NOVEMBER"))) {
      monthNumber = Calendar.NOVEMBER;
    }
    if ((monthName.equals("December")) | (monthName.equals("Dec")) | (monthName.equals("DEC")) | (monthName.equals("DECEMBER"))) {
      monthNumber = Calendar.DECEMBER;
    }
    return monthNumber;
  }

  public static String getCalenderMonthName(int monthNumber)
  {
    String monthName = "";
    switch (monthNumber) {
      case 0:
        monthName = "January";
        break;
      case 1:
        monthName = "February";
        break;
      case 2:
        monthName = "March";
        break;
      case 3:
        monthName = "April";
        break;
      case 4:
        monthName = "May";
        break;
      case 5:
        monthName = "June";
        break;
      case 6:
        monthName = "July";
        break;
      case 7:
        monthName = "August";
        break;
      case 8:
        monthName = "September";
        break;
      case 9:
        monthName = "October";
        break;
      case 10:
        monthName = "November";
        break;
      case 11:
        monthName = "December";
        break;
    }
    return monthName;
  }

  public static String getActivityIconFileName(String activityType)
  {
    String iconFileName = "icon_file.gif";
    if (activityType.equalsIgnoreCase("Appointment")) {
      iconFileName = "icon_invitation.gif";
    } else if (activityType.equalsIgnoreCase("Meeting")) {
      iconFileName = "icon_invitation.gif";
    } else if (activityType.equalsIgnoreCase("Call")) {
      iconFileName = "icon_call.gif";
    } else if (activityType.equalsIgnoreCase("Task")) {
      iconFileName = "icon_tasks.gif";
    } else if (activityType.equalsIgnoreCase("To Do")) {
      iconFileName = "icon_tasks.gif";
    } else if (activityType.equalsIgnoreCase("Next Action")) {
      iconFileName = "icon_tasks.gif";
    } else if (activityType.equalsIgnoreCase("Literature Request")) {
      iconFileName = "icon_file.gif";
    }
    return iconFileName;
  }

  public static GregorianCalendar setToStartOfWeek(GregorianCalendar start)
  {
    int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
    // Subtracting it with 2 coz in the Calendar Starting Day is Monday.
    int offset = dayOfWeek - 2;
    if (offset < 0) {
      int daysInWeek = 7;
      // if we are before the current day of the week, we need to fall all the
      // way
      // back to the previous beginning of the week. calculating our offset in
      // this
      // way will give us the right num of days to the previous start.
      // this makes it easy to just add it to the currentDate.
      offset = daysInWeek + offset;
    }
    int currentDate = start.get(Calendar.DATE);
    start.set(Calendar.DATE, currentDate - offset);
    return start;

  }

  public static HashMap setCalendarNavBar(int currentYear, int currentMonth, int currentDay, String calendarType)
  {
    HashMap calendarNavBarMap = new HashMap();
    HashMap headerList = new HashMap();
    HashMap headerLinkList = new HashMap();
    HashMap dateList = new HashMap();

    ArrayList calendarNavBarList = new ArrayList();
    Calendar currentCalendar = Calendar.getInstance();
    int thisDayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);
    int thisMonthOfYear = currentCalendar.get(Calendar.MONTH);
    String thisMonth = getCalenderMonthName(thisMonthOfYear);
    int thisYear = currentCalendar.get(Calendar.YEAR);

    if (calendarType != null && (calendarType.equals("DAILY") || calendarType.equals(""))) {
      int previousDailyDay = 0;
      String previousDailyMonth = "";
      int previousDailyYear = 0;

      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar previousCalendarWeek = Calendar.getInstance();
        previousCalendarWeek.set(currentYear, currentMonth, currentDay);
        previousCalendarWeek.add(Calendar.DATE, -1);
        previousDailyDay = previousCalendarWeek.get(Calendar.DATE);
        int intYearOfMonth = previousCalendarWeek.get(Calendar.MONTH);
        previousDailyMonth = getCalenderMonthName(intYearOfMonth);
        previousDailyYear = previousCalendarWeek.get(Calendar.YEAR);
      }
      int nextDailyDay = 0;
      String nextDailyMonth = "";
      int nextDailyYear = 0;
      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar nextCalendarWeek = Calendar.getInstance();
        nextCalendarWeek.set(currentYear, currentMonth, currentDay);
        nextCalendarWeek.add(Calendar.DATE, +1);
        nextDailyDay = nextCalendarWeek.get(Calendar.DATE);
        int intYearOfMonth = nextCalendarWeek.get(Calendar.MONTH);
        nextDailyYear = nextCalendarWeek.get(Calendar.YEAR);
        nextDailyMonth = getCalenderMonthName(intYearOfMonth);
      }

      StringBuffer previous = new StringBuffer();
      previous.append("c_goTo('/calendar.do?Type=DAILY&selectedDay=" + previousDailyDay);
      previous.append("&selectedMonthName=" + previousDailyMonth + "&selectedYear=" + previousDailyYear + "');");

      StringBuffer current = new StringBuffer();
      current.append("c_goTo('/calendar.do?Type=DAILY&selectedDay=" + thisDayOfMonth);
      current.append("&selectedMonthName=" + thisMonth + "&selectedYear=" + thisYear + "');");

      StringBuffer next = new StringBuffer();
      next.append("c_goTo('/calendar.do?Type=DAILY&selectedDay=" + nextDailyDay);
      next.append("&selectedMonthName=" + nextDailyMonth + "&selectedYear=" + nextDailyYear + "');");

      calendarNavBarList.add(new DDNameValue("Previous", previous.toString()));
      calendarNavBarList.add(new DDNameValue("Today", current.toString()));
      calendarNavBarList.add(new DDNameValue("Next", next.toString()));
    } else if (calendarType != null && (calendarType.equals("WEEKLY") || calendarType.equals("WEEKLYCOLUMNS"))) {
      int previousWeekDate = 0;
      String previousWeekMonth = "";
      int previousWeekYear = 0;
      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar previousCalendarWeek = Calendar.getInstance();
        previousCalendarWeek.set(currentYear, currentMonth, currentDay);
        previousCalendarWeek.add(Calendar.DATE, -7);
        previousWeekDate = previousCalendarWeek.get(Calendar.DATE);
        int intYearOfMonth = previousCalendarWeek.get(Calendar.MONTH);
        previousWeekMonth = getCalenderMonthName(intYearOfMonth);
        previousWeekYear = previousCalendarWeek.get(Calendar.YEAR);
      }
      int nextWeekDay = 0;
      String nextWeekMonth = "";
      int nextWeekYear = 0;

      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar nextCalendarWeek = Calendar.getInstance();
        nextCalendarWeek.set(currentYear, currentMonth, currentDay);
        nextCalendarWeek.add(Calendar.DATE, +7);
        nextWeekDay = nextCalendarWeek.get(Calendar.DATE);
        int intYearOfMonth = nextCalendarWeek.get(Calendar.MONTH);
        nextWeekYear = nextCalendarWeek.get(Calendar.YEAR);
        nextWeekMonth = getCalenderMonthName(intYearOfMonth);
      }

      StringBuffer previous = new StringBuffer();
      previous.append("c_goTo('/calendar.do?Type=" + calendarType + "&selectedDay=" + previousWeekDate);
      previous.append("&selectedMonthName=" + previousWeekMonth + "&selectedYear=" + previousWeekYear + "');");

      StringBuffer current = new StringBuffer();
      current.append("c_goTo('/calendar.do?Type=" + calendarType + "&selectedDay=" + thisDayOfMonth);
      current.append("&selectedMonthName=" + thisMonth + "&selectedYear=" + thisYear + "');");

      StringBuffer next = new StringBuffer();
      next.append("c_goTo('/calendar.do?Type=" + calendarType + "&selectedDay=" + nextWeekDay);
      next.append("&selectedMonthName=" + nextWeekMonth + "&selectedYear=" + nextWeekYear + "');");

      calendarNavBarList.add(new DDNameValue("Previous", previous.toString()));
      calendarNavBarList.add(new DDNameValue("Current Week", current.toString()));
      calendarNavBarList.add(new DDNameValue("Next", next.toString()));
      SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");

      String dailyGo = "c_goTo('/calendar.do?Type=DAILY";

      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar headerCalendarWeek = Calendar.getInstance();
        headerCalendarWeek.set(currentYear, currentMonth, currentDay);

        int dayOfWeek = headerCalendarWeek.get(Calendar.DAY_OF_WEEK);
        // Subtracting it with 2 coz in the Calendar Starting Day is Monday.
        int offset = dayOfWeek - 2;
        if (offset < 0) {
          int daysInWeek = 7;
          // if we are before the current day of the week, we need to fall all
          // the way back to the previous beginning of the week. calculating our
          // offset in this way will give us the right num of days to the
          // previous start. this makes it easy to just add it to the
          // currentDate.
          offset = daysInWeek + offset;
        }
        int currentDate = headerCalendarWeek.get(Calendar.DATE);
        headerCalendarWeek.set(Calendar.DATE, currentDate - offset);

        int headerDayOfMonth = headerCalendarWeek.get(Calendar.DATE);
        int headerMonthOfYear = headerCalendarWeek.get(Calendar.MONTH);
        int headerYear = headerCalendarWeek.get(Calendar.YEAR);

        try {
          // This is used in the columnar weekly view for calculating the 
          // date to be filled in when you click on a empty cell.
          calendarNavBarMap.put("startDayOfWeek", new Integer(headerDayOfMonth));
        } catch (NumberFormatException nfe) {
          logger.info("[setCalendarNavBar]: The offset for the clickable weekly view boxes will probably be broken.");
          calendarNavBarMap.put("startDayOfWeek", new Integer(0));
        }

        String dateString = headerYear + ",'" + getCalenderMonthName(headerMonthOfYear) + "'," + headerDayOfMonth;
        String headerDate = formatter.format(headerCalendarWeek.getTime());
        String dailyDate = "&selectedDay=" + headerDayOfMonth + "&selectedMonthName=" + getCalenderMonthName(headerMonthOfYear) + "&selectedYear="
            + headerYear;

        String action = "ScheduleActivity(" + dateString + ",'','');";
        dateList.put(String.valueOf(0), action);
        headerLinkList.put(String.valueOf(0), dailyGo + dailyDate + "');");
        headerList.put(String.valueOf(0), headerDate);
        int count = 1;
        for (int i = 0; i < 6; i++) {
          headerCalendarWeek.add(Calendar.DATE, 1);
          headerDayOfMonth = headerCalendarWeek.get(Calendar.DAY_OF_MONTH);
          headerMonthOfYear = headerCalendarWeek.get(Calendar.MONTH);
          headerYear = headerCalendarWeek.get(Calendar.YEAR);
          dateString = headerYear + ",'" + getCalenderMonthName(headerMonthOfYear) + "'," + headerDayOfMonth;
          action = "ScheduleActivity(" + dateString + ",'','');";
          dateList.put(String.valueOf(count), action);
          headerDate = formatter.format(headerCalendarWeek.getTime());
          headerList.put(String.valueOf(count), headerDate);
          dailyDate = "&selectedDay=" + headerDayOfMonth + "&selectedMonthName=" + getCalenderMonthName(headerMonthOfYear) + "&selectedYear="
              + headerYear;
          headerLinkList.put(String.valueOf(count), dailyGo + dailyDate + "');");
          count++;
        }
      }
    } else if (calendarType != null && calendarType.equals("MONTHLY")) {
      int previousMonthlyDay = 0;
      String previousMonthlyMonth = "";
      int previousMonthlyYear = 0;

      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar previousCalendarWeek = Calendar.getInstance();
        previousCalendarWeek.set(currentYear, currentMonth, currentDay);
        previousCalendarWeek.add(Calendar.MONTH, -1);
        previousMonthlyDay = previousCalendarWeek.get(Calendar.DATE);
        int intYearOfMonth = previousCalendarWeek.get(Calendar.MONTH);
        previousMonthlyMonth = getCalenderMonthName(intYearOfMonth);
        previousMonthlyYear = previousCalendarWeek.get(Calendar.YEAR);
      }

      int nextMonthlyDay = 0;
      String nextMonthlyMonth = "";
      int nextMonthlyYear = 0;

      if (currentDay != 0 && currentMonth >= 0 && currentYear != 0) {
        Calendar nextCalendarWeek = Calendar.getInstance();
        nextCalendarWeek.set(currentYear, currentMonth, currentDay);
        nextCalendarWeek.add(Calendar.MONTH, 1);
        nextMonthlyDay = nextCalendarWeek.get(Calendar.DATE);
        int intYearOfMonth = nextCalendarWeek.get(Calendar.MONTH);
        nextMonthlyYear = nextCalendarWeek.get(Calendar.YEAR);
        nextMonthlyMonth = getCalenderMonthName(intYearOfMonth);
      }
      StringBuffer previous = new StringBuffer();
      previous.append("c_goTo('/calendar.do?Type=MONTHLY&selectedDay=" + previousMonthlyDay);
      previous.append("&selectedMonthName=" + previousMonthlyMonth + "&selectedYear=" + previousMonthlyYear + "');");

      StringBuffer current = new StringBuffer();
      current.append("c_goTo('/calendar.do?Type=MONTHLY&selectedDay=" + thisDayOfMonth);
      current.append("&selectedMonthName=" + thisMonth + "&selectedYear=" + thisYear + "');");

      StringBuffer next = new StringBuffer();
      next.append("c_goTo('/calendar.do?Type=MONTHLY&selectedDay=" + nextMonthlyDay);
      next.append("&selectedMonthName=" + nextMonthlyMonth + "&selectedYear=" + nextMonthlyYear + "');");

      calendarNavBarList.add(new DDNameValue("Previous", previous.toString()));
      calendarNavBarList.add(new DDNameValue("Current Month", current.toString()));
      calendarNavBarList.add(new DDNameValue("Next", next.toString()));
    } else if (calendarType != null && calendarType.equals("YEARLY")) {
      int previousYearlyYear = 0;
      previousYearlyYear = currentYear - 1;
      int nextYearlyYear = 0;
      nextYearlyYear = currentYear + 1;

      StringBuffer previous = new StringBuffer();
      previous.append("c_goTo('/calendar.do?Type=YEARLY&selectedYear=" + previousYearlyYear + "');");

      StringBuffer current = new StringBuffer();
      current.append("c_goTo('/calendar.do?Type=YEARLY&selectedYear=" + thisYear + "');");

      StringBuffer next = new StringBuffer();
      next.append("c_goTo('/calendar.do?Type=YEARLY&selectedYear=" + nextYearlyYear + "');");

      calendarNavBarList.add(new DDNameValue("Previous", previous.toString()));
      calendarNavBarList.add(new DDNameValue("Current Year", current.toString()));
      calendarNavBarList.add(new DDNameValue("Next", next.toString()));
    }
    calendarNavBarMap.put("calendarNavBar", calendarNavBarList);
    calendarNavBarMap.put("headerList", headerList);
    calendarNavBarMap.put("headerLinkList", headerLinkList);
    calendarNavBarMap.put("dateList", dateList);

    return calendarNavBarMap;
  }

  /**
   * This method returns a hashmap that contains three more maps within. yes,
   * this is bad, and it should be fixed, because it is unecessary complexity
   * and an exercise for your memory. And this method is complex enough without
   * Making it hard to figure out. So the maps within are unscheduledactivity
   * and scheduledactivity maps and an allday map which is a tree map to allow
   * it to be in a predictible order.
   * 
   * The maps are returned basically from the EJB layer, the rest of the code here
   * is to help break up the results for display on the screen depending on what
   * type of calendar we are showing.
   * @param starttimeuser
   * @param endTimeuser
   * @param tz
   * @param userID
   * @param timespan
   * @param search
   * @param dataSource
   * @return
   */
  public static final HashMap getCalendarCollection(GregorianCalendar starttimeuser,
      GregorianCalendar endTimeuser, TimeZone tz, int userID, int timespan, String search,
      String dataSource)
  {
    // TODO instead of hiding things away in maps, create objects.
    // The return hashmap has two hashmaps and a calendar list on it
    // The unscheduledMap and the scheduledMap and calendarlist, which
    // has the actual events for the meat of the calendar.
    HashMap returnmap = new HashMap(3);
    Vector calactivityVector = null;
    Vector unscheduledactivity = null;
    Vector scheduledactivitydaily = null;
    HashMap unsheduledMap = new HashMap();
    HashMap scheduledMap = new HashMap();
    TreeMap allDayMap = new TreeMap();
  
    try {
      CvCalendarHome cvCalendarHome = (CvCalendarHome)CVUtility.getHomeObject(
          "com.centraview.calendar.CvCalendarHome", "CvCalendar");
      CvCalendar cvCalendar = cvCalendarHome.create();
      cvCalendar.setDataSource(dataSource);
  
      HashMap cvCalendarMap = cvCalendar.getAllCalendar(userID, userID, starttimeuser, endTimeuser,
          search);
      calactivityVector = (Vector)cvCalendarMap.get("scheduleactivityvector");
      unscheduledactivity = (Vector)cvCalendarMap.get("unscheduleactivityvector");
      scheduledactivitydaily = (Vector)cvCalendarMap.get("scheduleactivityvectordaily");
    } catch (Exception e) {
      logger.error("[Exception][CalendarValueList.getCalendarList] Exception Thrown: ", e);
    }
  
    CalendarList calendarlist = new CalendarList();
    int totalnumberofrows = 0;
  
    long timeInMillisStart = starttimeuser.getTimeInMillis();
    long timeInMillisEnd = endTimeuser.getTimeInMillis();
  
    // We must have to do the ZoneOffSet and DayLight Saving to Offset.
    // off zone time values we must have to add to TimeInMillis
    double differenceTimeInMills = ((timeInMillisEnd + endTimeuser.get(Calendar.ZONE_OFFSET) + endTimeuser
        .get(Calendar.DST_OFFSET)) - (timeInMillisStart + starttimeuser.get(Calendar.ZONE_OFFSET) + starttimeuser
        .get(Calendar.DST_OFFSET)));
  
    // noOfDiff is the number of timespan (in minutes) length blocks
    // that make up the entire endtime-starttime.
    // daily view will be one of: 1440/60 = 24, 1440/30 = 48, 1440/15 = 96
    // weekly (columnar) will be: 10080/60 = 168 (24 rows * 7columns)
    // weekly (non-columnar) will be: 10080/1440 = 7 (7 days)
    // monthly will be between: 40320/1440 = 28 and 44640/1440 = 31
    // 1440 is the number of minutes in one day. (24*60 = 1440)
    // actually it appears that timespan is minutes per HTML block.
    // 60000 is used for getting the number of milliseconds per minute out of
    // there.
    // so you can simply divide minutes per block into the time difference.
    double noOfDiff = Math.ceil(differenceTimeInMills / (timespan * 60000));
    // The Math.ceil function is unecessary and probably just a holdover from
    // when the
    // original authors couldn't figure out how to use the Gregorian calendar
    // and thus
    // often found themselves with numbers that didn't come out even.
    totalnumberofrows = (int)noOfDiff;
    // The following loop goes through and for each row, inserts
    // a blank CalendarListElement
    // Not sure what this accomplishes.
    long start = 0, end = 0;
    // TDColumn simply is used to represent how many elements in a row
    // on the displayed table. It is really only useful in one of the daily
    // views.
    TreeMap TDColumn = calendarlist.getTDIndicesTreeMap();
    for (int i = 0; i < totalnumberofrows; i++) {
      // Fill it up with a bunch-a-blank'uns.
      start = timeInMillisStart + ((i) * timespan * 60000);
      end = timeInMillisStart + ((i + 1) * timespan * 60000);
      calendarlist.put(new Integer(i), new CalendarListElement(start, end));
      // pre-populate the row to contain zero items.
      TDColumn.put(new Integer(i), new Integer(0));
    }
  
    GregorianCalendar listRenderCalendar = new GregorianCalendar(tz);
    listRenderCalendar.setTime(starttimeuser.getTime());
  
    int counthour = 0;
    for (int i = 0; i < totalnumberofrows; i++) {
      // The Calendar fields will be used to track the current place in
      // the displayed calendar, we will increment through the blocks
      // and compare the activites returned from the list.
      int calendarDate = listRenderCalendar.get(Calendar.DATE);
      int calendarMonth = listRenderCalendar.get(Calendar.MONTH);
      int calendarYear = listRenderCalendar.get(Calendar.YEAR);
      Iterator activityIterator = calactivityVector.iterator();
      int uniqueId = 0;
      while (activityIterator.hasNext()) {
        uniqueId++;
        CalendarActivityObject activityobject = (CalendarActivityObject)activityIterator.next();
        GregorianCalendar starttime = activityobject.getStartTime();
        GregorianCalendar endtime = activityobject.getEndTime();
  
        // If somehow we squeezed an illegally formed activity in we should skip
        // it.
        if ((starttime.getTimeInMillis() > endTimeuser.getTimeInMillis())) {
          continue;
        }
        // calendarStartingBlock is the starting block for an activity on the
        // displayed table.
        int calendarStartingBlock = 0;
        // The critical info on the currently selected activity.
        int activityDate = starttime.get(Calendar.DATE);
        int activityMonth = starttime.get(Calendar.MONTH);
        int activityYear = starttime.get(Calendar.YEAR);
        int activityHour = starttime.get(Calendar.HOUR);
        int activityMinute = starttime.get(Calendar.MINUTE);
        int activityAMPM = starttime.get(Calendar.AM_PM);
        if (activityAMPM == 1) {
          activityHour = activityHour + 12;
        }
        boolean flag = false;
  
        // If the selected activity has the same month, date, and year
        // and we are looking at the Daily View.
        if (activityDate == calendarDate && activityMonth == calendarMonth
            && activityYear == calendarYear
            && (totalnumberofrows == 24 || totalnumberofrows == 48 || totalnumberofrows == 96)) {
          // if it is daily hourly blocks then the starting block is the
          // starting hour
          calendarStartingBlock = (activityHour);
          // otherwise we need calculate the starting block because the number
          // of blocks may
          // have doubled or quadrupled (30 or 15 minute intervals)
          // It needs to be the floor function of the division also.
          if (totalnumberofrows == 48) {
            int diffMinute = (int)Math.floor(activityMinute / 30.0);
            calendarStartingBlock = (calendarStartingBlock * 2) + diffMinute;
          } else if (totalnumberofrows == 96) {
            int diffMinute = (int)Math.floor(activityMinute / 15.0);
            calendarStartingBlock = (calendarStartingBlock * 4) + diffMinute;
          }
          flag = true;
        } else if (activityDate == calendarDate && activityMonth == calendarMonth
            && activityYear == calendarYear) {
          // it is still the same, but we are not one of the daily views.
          flag = true; // no setup required for numberof rows = 28 through 31
                        // ??
  
          calendarStartingBlock = (activityDate - 1);
          if (totalnumberofrows == 168) // if weekly columnar
          {
            calendarStartingBlock = starttime.get(Calendar.DAY_OF_WEEK);
            if (calendarStartingBlock == Calendar.SUNDAY) // if SUNDAY
            {
              calendarStartingBlock = 6; // then move to the end.
            } else {
              // else normalize monday to be zero (coupled to UI)                                                           // normalize
              calendarStartingBlock = calendarStartingBlock - Calendar.MONDAY; 
            }
            calendarStartingBlock = (calendarStartingBlock * 24) + (activityHour);
  
            // Display as an allday activity on top
            if (activityobject.getAllDayEvent()) {
              activityobject.setAllDayTime();
              flag = false;
              allDayMap.put(activityobject.getActivityID(), activityobject);
            }
          }
  
          if (totalnumberofrows == 7) {
            // if weekly non-columnar
            calendarStartingBlock = starttime.get(GregorianCalendar.DAY_OF_WEEK);
            if (calendarStartingBlock == Calendar.SUNDAY) // if SUNDAY
            {
              calendarStartingBlock = 6; // then move to the end.
            } else {
              // else normalize monday to be zero (coupled to UI)                                                           // normalize
              calendarStartingBlock = calendarStartingBlock - Calendar.MONDAY;
            }
          }
        }
  
        if (flag) // It matches one of our calendar blocks, we need to display
                  // it
        {
          // Get the blank element we stuck in there before as a placeholder
          CalendarListElement element = (CalendarListElement)calendarlist.get(new Integer(
              calendarStartingBlock));
          // monthly will only use the element at 0 on calendarlist (I guess the
          // rest is designed for that)
          if (element != null) // if the element is null then our
                                // calendarStartingBlock is totally out of
                                // whack.
          {
            int numberOfRows = 1; // assume it only takes one row
            // We probably should take account if the activity spans to a
            // different day.
            // right now this doesn't seem to accomodate that.
            int activityEndHour = endtime.get(Calendar.HOUR);
            int activityEndAMPM = endtime.get(Calendar.AM_PM);
            if (activityEndAMPM == 1) {
              activityEndHour = activityEndHour + 12;
            }
  
            // calculate the number of rows the activity spans, if it is one of
            // the daily views
            // use ceiling function, so that any activity that impinges on the
            // above activity
            // will fill that row also.
            if (totalnumberofrows == 24 || totalnumberofrows == 48 || totalnumberofrows == 96) {
              // We must have to do the ZoneOffSet and DayLight Saving to
              // Offset.
              // off zone time values we must have to add to TimeInMillis
              long startTimeValue = starttime.getTimeInMillis()
                  + starttime.get(Calendar.ZONE_OFFSET) + starttime.get(Calendar.DST_OFFSET);
              long endTimeValue = endtime.getTimeInMillis() + endtime.get(Calendar.ZONE_OFFSET)
                  + endtime.get(Calendar.DST_OFFSET);
  
              double activityDuration = endTimeValue - startTimeValue;
              numberOfRows = (int)Math.ceil(activityDuration / (timespan * 60000));
  
              // When we have Scheduling and activity for two whole day event
              // then our numberOfRows Calculation will return us "48"
              // We can show only 24 hours in the Daily view. so reseting it
              // back to 24.
              if (totalnumberofrows == 24) {
                if (numberOfRows >= 24) {
                  numberOfRows = 24;
                }
              } // end if (totalnumberofrows == 24)
            } // end if (totalnumberofrows == 24 || totalnumberofrows == 48 ||
              // totalnumberofrows == 96)
  
            if (activityobject.getAllDayEvent()) {
              activityobject.setAllDayTime();
            }
  
            CalendarMember calendarItem = new CalendarMember(activityobject, 0, numberOfRows);
            // activity is the key to the element HashMap, it must be unique
            // between returned
            // activities, other wise activities with same start time and name
            // will only show
            // up once on a calendar. So we are using the activityId which must
            // be unique.
            Integer activity = new Integer(uniqueId);
            // at a minimum now this element which exists at the
            // starting row (calendarStartingBlock)
            // has two values, the blank one we prepopulated before.
            // And the one we are adding now
            element.put(activity, calendarItem);
            // we need to increment the rowItem counter for each row that this
            // activity inpinges
            // on. So if the nuOfrows at least 1, then we need to iterate
            // through
            // and increment the counter for it.
            if (numberOfRows >= 1) {
              for (int row = calendarStartingBlock; row < (calendarStartingBlock + numberOfRows); row++) {
                Integer rowInteger = new Integer(row);
                if (TDColumn.get(rowInteger) != null) {
                  int totalRowItems = ((Integer)TDColumn.get(rowInteger)).intValue();
                  totalRowItems++;
                  TDColumn.put(rowInteger, new Integer(totalRowItems));
                }
              }
            }
          } // end if (element != null)
          activityIterator.remove();
          flag = false;
        } // end if (flag)
      } // end while (activityenum.hasNext())
  
      // Increment the calendar based on our timeframe.
      // Daily Views
      if (totalnumberofrows == 24) {
        listRenderCalendar.add(Calendar.HOUR, 1);
      } else if (totalnumberofrows == 48) {
        listRenderCalendar.add(Calendar.MINUTE, 30);
      } else if (totalnumberofrows == 96) {
        listRenderCalendar.add(Calendar.MINUTE, 15);
      } else if (totalnumberofrows == 168) {
        // If we have 168 rows that is weekly columnar, so we will have to
        // increment hours, unless we are at 24 then we have to increment
        // DATE and reset hours. (I believe we are handling midnight
        // incorrectly)
        listRenderCalendar.add(Calendar.HOUR, 1);
        if (counthour == 24) {
          counthour = 0;
          listRenderCalendar.add(Calendar.DATE, 1);
        }
        counthour++;
      } else {
        // else we are simply weekly (non-column) or monthly
        listRenderCalendar.add(Calendar.DATE, 1);
      }
    } // end for (int i=0; i<totalnumberofrows; i++)
  
    returnmap.put("calendarlist", calendarlist);
    // get the unscheduledactivities and scheduledactivities and shove them onto
    // the return.
    Enumeration unsheduledenum = unscheduledactivity.elements();
    while (unsheduledenum.hasMoreElements()) {
      CalendarActivityObject activityobject = (CalendarActivityObject)unsheduledenum.nextElement();
      unsheduledMap.put(activityobject.getActivityID(), activityobject);
    }
  
    Enumeration scheduledenum = scheduledactivitydaily.elements();
    while (scheduledenum.hasMoreElements()) {
      CalendarActivityObject activityobject = (CalendarActivityObject)scheduledenum.nextElement();
      scheduledMap.put(activityobject.getActivityID(), activityobject);
    }
  
    returnmap.put("unscheduledactivity", unsheduledMap);
    returnmap.put("scheduledactivity", scheduledMap);
    returnmap.put("allDayActivity", allDayMap);
  
    return returnmap;
  } // end getCalendarList() method
} // end class CalendarUtil
