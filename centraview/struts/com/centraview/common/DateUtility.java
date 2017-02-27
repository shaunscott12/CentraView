/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
package com.centraview.common;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.centraview.preference.Preference;
import com.centraview.preference.PreferenceHome;

/**
 * This class centralizes utility methods for dealing with dates. In this way
 * timezone issues and duplicated date handling code can become a thing of the
 * past.
 */
public class DateUtility {
  private static Logger logger = Logger.getLogger(DateUtility.class);

  /**
   * using the locale and timezone get a calendar containing the current time or
   * an empty calendar.
   * @param request The HttpServletRequest to obtain the timezone and locale
   * @param current if true return current time, if false return empty calendar
   * @return a calendar object for the locale and timezone
   */
  public static Calendar getCalendar(HttpServletRequest request, boolean current)
  {
    Calendar calendar;
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    UserPrefererences prefs = user.getUserPref();
    TimeZone tz = TimeZone.getTimeZone(prefs.getTimeZone());
    Locale locale = request.getLocale();
    // If current time requested get the system instance
    if (current) {
      calendar = Calendar.getInstance(tz, locale);
    } else {
      calendar = new GregorianCalendar(tz, locale);
    }
    return calendar;
  }

  /**
   * This method takes the individualId and looks up the timezone in the
   * database. Eventually the locale should be looked up there as well. returns
   * a calendar that is either the current time or empty, based on the boolean
   * value of current
   * @param individualId
   * @param current
   * @return
   */
  public static Calendar getCalendar(int individualId, String dataSource, boolean current)
  {
    Calendar calendar;
    TimeZone tz = null;
    try {
      PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject(
          "com.centraview.preference.PreferenceHome", "Preference");
      Preference prefRemote = prefHome.create();
      prefRemote.setDataSource(dataSource);
      UserPrefererences prefs = prefRemote.getUserPreferences(individualId);
      tz = TimeZone.getTimeZone(prefs.getTimeZone());
    } catch (Exception e) {
      logger.error("[getCalendar]: Exception", e);
    }
    if (tz != null) {
      if (current) {
        calendar = Calendar.getInstance(tz);
      } else {
        calendar = new GregorianCalendar(tz);
      }
    } else {
      if (current) {
        calendar = Calendar.getInstance();
      } else {
        calendar = new GregorianCalendar();
      }
    }
    return calendar;
  }

  /**
   * Given String representation of the year, month and day create a Calendar
   * (ignoring TZ and locale) and return a java.sql.Timestamp.
   * @param year year in string form
   * @param month month in string form (1 based month)
   * @param day day in string from
   * @return
   */
  public static Timestamp createTimestamp(String year, String month, String day)
  {
    return DateUtility.createTimestamp(year, month, day, "0", "0");
  }
  
  /**
   * Create a java.sql.Timestamp given year, month, day, and a time string that will
   * be recongnized and parsed by CVUtility.convertTimeTo24HrsFormat
   * @param year
   * @param month
   * @param day
   * @param time
   * @return
   */
  public static Timestamp createTimestamp(String year, String month, String day, String time)
  {
    int[] time24 = CVUtility.convertTimeTo24HrsFormat(time);
    return DateUtility.createTimestamp(year, month, day, String.valueOf(time24[0]), String.valueOf(time24[1]));
  }
  
  /**
   * create a java.sql.Timestamp given year, month, day, hour, minute.
   * @param year
   * @param month
   * @param day
   * @param hour
   * @param min
   * @return
   */
  public static Timestamp createTimestamp(String year, String month, String day, String hour, String min)
  {
    Calendar calendar = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1,
        Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min));
    return new Timestamp(calendar.getTimeInMillis());
  }

  public static Date createDate(String year, String month, String day)
  {
    Timestamp ts = DateUtility.createTimestamp(year, month, day);
    return new Date(ts.getTime());
  }

  /**
   * converts long(in secs) to HH:mm:ss String returns String
   * @param long
   */
  public static String secsToString(long secs)
  {
    int seconds = (int)(secs % 60);
    int minutes = (int)((secs / 60) % 60);
    int hours = (int)(secs / 3600);
    String secondsStr = (seconds < 10 ? "0" : "") + seconds;
    String minutesStr = (minutes < 10 ? "0" : "") + minutes;
    String hoursStr = (hours < 10 ? "0" : "") + hours;
    return new String(hoursStr + ":" + minutesStr + ":" + secondsStr);
  }
}
