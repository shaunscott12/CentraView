/*
 * $RCSfile: Validation.java,v $    $Revision: 1.5 $  $Date: 2005/10/24 21:22:46 $ - $Author: mcallist $
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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

public class Validation {

  // message property file
  protected static MessageResources messages = MessageResources
      .getMessageResources("ApplicationResources");

  /*
   * Ensures that the field is not null
   */
  public ActionErrors checkForRequired(String fieldLabel, String fieldValue, String errorKey,
      ActionErrors actionErrors)
  {
    if (fieldValue == null || fieldValue.trim().length() == 0) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /*
   * Ensures that the array field is not null
   */
  public ActionErrors checkForArrayRequired(String fieldLabel, String[] fieldValue,
      String errorKey, ActionErrors actionErrors)
  {
    if (fieldValue == null || fieldValue.length == 0) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /*
   * Ensures that the field is not exceeding max length
   */
  public ActionErrors checkForMaxlength(String fieldLabel, String fieldValue, String errorKey,
      ActionErrors actionErrors, int maxLength)
  {
    if (fieldValue != null && fieldValue.trim().length() > maxLength) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel), ""
          + maxLength);
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /*
   * Ensures that the field is integer
   */
  public ActionErrors checkForInteger(String fieldLabel, String fieldValue, String errorKey,
      ActionErrors actionErrors)
  {
    try {
      Integer.valueOf(fieldValue);
    } catch (Exception e) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /*
   * Ensures that the field is float
   */
  public ActionErrors checkForFloat(String fieldLabel, String fieldValue, String errorKey,
      ActionErrors actionErrors)
  {
    try {
      Float.valueOf(fieldValue);
    } catch (Exception e) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /**
   * Ensures that the field is valid date
   */
  public ActionErrors checkForDate(String fieldLabel, String yearValue, String monthValue,
      String dayValue, String errorKey, ActionErrors actionErrors)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setLenient(false);
    try {
      calendar.set(Integer.parseInt(yearValue), Integer.parseInt(monthValue), Integer.parseInt(dayValue));
    } catch (Exception e) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }
  /**
   * Checks a string date if it is valid using the dateformat parser
   * @param fieldLabel The label of the field for error messages.
   * @param date The date in string form passed from the user
   * @param locale the locale.
   * @return
   */
  public ActionErrors validateDateString(String fieldLabel, String date, Locale locale, String errorKey, ActionErrors actionErrors)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.setLenient(false);
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    try {
      Date parsedDate = df.parse(date);
      calendar.setTime(parsedDate);
    } catch (ParseException e) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    } catch (Exception e) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /**
   * Ensures that the field is valid time using the time parser
   */
  public ActionErrors checkForTime(String fieldLabel, String fieldValue, String errorKey,
      ActionErrors actionErrors, Locale locale)
  {
    boolean isError = true;
    try {
      if (fieldValue != null) {
        fieldValue = fieldValue.trim();
      } else {
        isError = true;
      }
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
      tf.parse(fieldValue);
      isError = false;
    } catch (Exception e) {
      isError = true;
    }
    if (isError) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /*
   * Ensures that the two date comparison is valid
   */
  public ActionErrors checkForDateComparison(String fieldStartDateLabel, String yearStartValue,
      String monthStartValue, String dayStartValue, String timeStartValue,
      String fieldEndDateLabel, String yearEndValue, String monthEndValue, String dayEndValue,
      String timeEndValue, String errorKey, ActionErrors actionErrors, String fieldStartTimeLabel,
      String fieldEndTimeLabel)
  {

    boolean isError = true;
    // parse into format hh:mm am/pm
    try {
      // parse start time
      // convert time to 24 hrs
      int[] startTime = CVUtility.convertTimeTo24HrsFormat(timeStartValue);
      String startHour = String.valueOf(startTime[0]);
      String startMinute = String.valueOf(startTime[1]);
      if (startMinute != null && startMinute.length() == 1)
        startMinute = "0" + startMinute;

      // parse end time
      int[] endTime = CVUtility.convertTimeTo24HrsFormat(timeEndValue);
      String endHour = String.valueOf(endTime[0]);
      String endMinute = String.valueOf(endTime[1]);
      if (endMinute != null && endMinute.length() == 1) {
        endMinute = "0" + endMinute;
      }

      // need to modify for locale support in future - Ashwin
      // get start calendar
      GregorianCalendar startCalendar = new GregorianCalendar(Integer.parseInt(yearStartValue
          .trim()), Integer.parseInt(monthStartValue.trim()), Integer
          .parseInt(dayStartValue.trim()), Integer.parseInt(startHour.trim()), Integer
          .parseInt(startMinute.trim()));
      // get end calendar
      GregorianCalendar endCalendar = new GregorianCalendar(Integer.parseInt(yearEndValue.trim()),
          Integer.parseInt(monthEndValue.trim()), Integer.parseInt(dayEndValue.trim()), Integer
              .parseInt(endHour.trim()), Integer.parseInt(endMinute.trim()));

      // compare start date and end date
      if (endCalendar.before(startCalendar)) {
        isError = true;
      } else {
        isError = false;
      }

      // check for date error or time error
      if (isError) {
        // check if date is proper by having time = 00:00 AM
        // get start calendar
        startCalendar = new GregorianCalendar(Integer.parseInt(yearStartValue.trim()), Integer
            .parseInt(monthStartValue.trim()), Integer.parseInt(dayStartValue.trim()), 00, 00);
        // get end calendar
        endCalendar = new GregorianCalendar(Integer.parseInt(yearEndValue.trim()), Integer
            .parseInt(monthEndValue.trim()), Integer.parseInt(dayEndValue.trim()), 00, 00);

        // compare start date and end date
        if (endCalendar.before(startCalendar)) {
          // if error then error is in date
          isError = true;
          ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldEndDateLabel),
              messages.getMessage(fieldStartDateLabel));
          actionErrors.add(errorKey, error);
        } else {
          // error is in time as date is proper
          isError = true;
          ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldEndTimeLabel),
              messages.getMessage(fieldStartTimeLabel));
          errorKey = "error.application.timecomparison";
          actionErrors.add(errorKey, error);
        }
      }

    } catch (Exception e) {
      isError = true;
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldEndDateLabel),
          messages.getMessage(fieldStartDateLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

  /*
   * Ensures that the two date comparison is valid when no time is present
   */
  public ActionErrors checkForDateComparison(String fieldStartLabel, String yearStartValue,
      String monthStartValue, String dayStartValue, String fieldEndLabel, String yearEndValue,
      String monthEndValue, String dayEndValue, String errorKey, ActionErrors actionErrors)
  {

    boolean isError = true;
    try {
      // need to modify for locale support in future - Ashwin
      // get start calendar
      GregorianCalendar startCalendar = new GregorianCalendar(Integer.parseInt(yearStartValue),
          Integer.parseInt(monthStartValue), Integer.parseInt(dayStartValue));
      // get end calendar
      GregorianCalendar endCalendar = new GregorianCalendar(Integer.parseInt(yearEndValue), Integer
          .parseInt(monthEndValue), Integer.parseInt(dayEndValue));

      // compare start date and end date
      if (endCalendar.before(startCalendar)) {
        isError = true;
      } else {
        isError = false;
      }
    } catch (Exception e) {
      isError = true;
    }
    // initialize error object if error
    if (isError) {
      ActionMessage error = new ActionMessage(errorKey, messages.getMessage(fieldEndLabel),
          messages.getMessage(fieldStartLabel));
      actionErrors.add(errorKey, error);
    }
    return actionErrors;
  }

}
