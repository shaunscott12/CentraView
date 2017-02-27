/*
 * $RCSfile: Constants.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:53 $ - $Author: mking_cv $
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class Constants
{
  public static HashSet freqValues;
  public static HashSet weekDayValues;

  public static ArrayList allMonths = new ArrayList();
  public static ArrayList allYearDays = new ArrayList();
  public static ArrayList allMonthDays = new ArrayList();
  public static ArrayList allWeekDays = new ArrayList();
  public static ArrayList allYearWeeks = new ArrayList();
  public static ArrayList allHours = new ArrayList();
  public static ArrayList allMinutes = new ArrayList();
  public static ArrayList allSeconds = new ArrayList();

  public static final int Date = 1;
  public static final int DateTime = 2;
  public static final int BothDate = 3;
  public static final int StartDateTimeAndEndTime = 4;
  public static final int BothDateTime = 5;

  static{
    //--- valid frequencies
    freqValues = new HashSet();
    freqValues.add("SECONDLY");
    freqValues.add("MINUTELY");
    freqValues.add("HOURLY");
    freqValues.add("DAILY");
    freqValues.add("WEEKLY");
    freqValues.add("MONTHLY");
    freqValues.add("YEARLY");

    //--- valid days of the week
    weekDayValues = new HashSet();
    weekDayValues.add("SU");
    weekDayValues.add("MO");
    weekDayValues.add("TU");
    weekDayValues.add("WE");
    weekDayValues.add("TH");
    weekDayValues.add("FR");
    weekDayValues.add("SA");

    int ctr;

    for(ctr=0;ctr<12;ctr++) allMonths.add( new Integer(ctr) );  // months are irritating
    for(ctr=1;ctr<367;ctr++) allYearDays.add( new Integer(ctr) ); // 366 days possible!
    for(ctr=1;ctr<32;ctr++) allMonthDays.add( new Integer(ctr) );
    for(ctr=1;ctr<8;ctr++) allWeekDays.add( new Integer(ctr) );
    for(ctr=1;ctr<54;ctr++) allYearWeeks.add( new Integer(ctr) ); // week 53 possible!
    for(ctr=0;ctr<24;ctr++) allHours.add( new Integer(ctr) );
    for(ctr=0;ctr<60;ctr++) allMinutes.add( new Integer(ctr) );
    for(ctr=0;ctr<60;ctr++) allSeconds.add( new Integer(ctr) );

  }

  public static final int FREQ_SECONDLY = 1;
  public static final int FREQ_MINUTELY = 2;
  public static final int FREQ_HOURLY   = 3;
  public static final int FREQ_DAILY    = 4;
  public static final int FREQ_WEEKLY   = 5;
  public static final int FREQ_MONTHLY  = 6;
  public static final int FREQ_YEARLY   = 7;

  public static int convertFreqNameToFreqNumber(String freq)
  {
    if("SECONDLY".equals(freq)){return FREQ_SECONDLY;}
    if("MINUTELY".equals(freq)){return FREQ_MINUTELY;}
    if("HOURLY".equals(freq)){return FREQ_HOURLY;}
    if("DAILY".equals(freq)){return FREQ_DAILY;}
    if("WEEKLY".equals(freq)){return FREQ_WEEKLY;}
    if("MONTHLY".equals(freq)){return FREQ_MONTHLY;}
    if("YEARLY".equals(freq)){return FREQ_YEARLY;}
    throw new IllegalArgumentException("  ERROR: "+freq+" is not a valid FREQ type");
  }

  public static int convertFreqNameToCalendarField(String freq)
  {
    if("SECONDLY".equals(freq)){return Calendar.SECOND;}
    if("MINUTELY".equals(freq)){return Calendar.MINUTE;}
    if("HOURLY".equals(freq)){return Calendar.HOUR;}
    if("DAILY".equals(freq)){return Calendar.DAY_OF_MONTH;}
    if("WEEKLY".equals(freq)){return Calendar.WEEK_OF_YEAR;}
    if("MONTHLY".equals(freq)){return Calendar.MONTH;}
    if("YEARLY".equals(freq)){return Calendar.YEAR;}
    throw new IllegalArgumentException("  ERROR: "+freq+" is not a valid FREQ type");
  }

  public static int convertDayNameToDayNumber(String day)
  {
    if("SU".equals(day)){return Calendar.SUNDAY;}
    if("MO".equals(day)){return Calendar.MONDAY;}
    if("TU".equals(day)){return Calendar.TUESDAY;}
    if("WE".equals(day)){return Calendar.WEDNESDAY;}
    if("TH".equals(day)){return Calendar.THURSDAY;}
    if("FR".equals(day)){return Calendar.FRIDAY;}
    if("SA".equals(day)){return Calendar.SATURDAY;}
    throw new IllegalArgumentException("  ERROR: "+day+" is not a valid week day");
  }

  public static String convertDayNumberToDayName(int day)
  {
    if(Calendar.SUNDAY==day){return "SU";}
    if(Calendar.MONDAY==day){return "MO";}
    if(Calendar.TUESDAY==day){return "TU";}
    if(Calendar.WEDNESDAY==day){return "WE";}
    if(Calendar.THURSDAY==day){return "TH";}
    if(Calendar.FRIDAY==day){return "FR";}
    if(Calendar.SATURDAY==day){return "SA";}
    throw new IllegalArgumentException("  ERROR: "+day+" is not a valid week day number");
  }
}


