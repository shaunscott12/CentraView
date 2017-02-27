/*
 * $RCSfile: ActivityEditForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:58 $ - $Author: mking_cv $
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


package com.centraview.sync;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ActivityEditForm extends ActionForm {

  private String sessionID = null;
  private String activityID = null; 
  private String priority = null;
  private String status = null;
  private String title = null;
  private String description = null;
  private String dueDate = null;
  private String startDateTime = null;
  private String endDateTime = null;
  private String alarmDateTime = null;
  private String untimed  = null;
  private String completed = null;
  private String recurrenceType = null;
  private String dayOfWeek = null;
  private String dayOfMonth = null;
  private String monthOfYear = null;
  private String interval = null;
  private String instance = null;
  private String recurrenceStartDate = null;
  private String recurrenceEndDate = null;

  public String getSessionID() {
    return this.sessionID;
  }   // end getSessionID()

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }   // end setSessionID()

  public String getActivityID() {
    return this.activityID;
  }   // end getActivityID()

  public void setActivityID(String activityID) {
    this.activityID = activityID;
  }   // end setActivityID()

  public String getPriority() {
    return this.priority;
  }   // end getPriority()

  public void setPriority(String priority) {
    this.priority = priority;
  }   // end setPriority()

  public String getStatus() {
    return this.status;
  }   // end getStatus()

  public void setStatus(String status) {
    this.status = status;
  }   // end setStatus()

  public String getTitle() {
    return this.title;
  }   // end getTitle()

  public void setTitle(String title) {
    this.title = title;
  }   // end setTitle()

  public String getDescription() {
    return this.description;
  }   // end getDescription()

  public void setDescription(String description) {
    this.description = description;
  }   // end setDescription()

  public String getDueDate() {
    return this.dueDate;
  }   // end getDueDate()

  public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
  }   // end setDueDate()

  public String getStartDateTime() {
    return this.startDateTime;
  }   // end getStartDateTime()

  public void setStartDateTime(String startDateTime) {
    this.startDateTime = startDateTime;
  }   // end setStartDateTime()

  public String getEndDateTime() {
    return this.endDateTime;
  }   // end getEndDateTime()

  public void setEndDateTime(String endDateTime) {
    this.endDateTime = endDateTime;
  }   // end setEndDateTime()

  public String getAlarmDateTime() {
    return this.alarmDateTime;
  }   // end getAlarmDateTime()

  public void setAlarmDateTime(String alarmDateTime) {
    this.alarmDateTime = alarmDateTime;
  }   // end setAlarmDateTime()

  public String getUntimed() {
    return this.untimed;
  }   // end getUntimed()

  public void setUntimed(String untimed) {
    this.untimed = untimed;
  }   // end setUntimed()

  public String getCompleted() {
    return this.completed;
  }   // end getCompleted()

  public void setCompleted(String completed) {
    this.completed = completed;
  }   // end setCompleted()

  public String getRecurrenceType() {
    return this.recurrenceType;
  }   // end getRecurrenceType()

  public void setRecurrenceType(String recurrenceType) {
    this.recurrenceType = recurrenceType;
  }   // end setRecurrenceType()

  public String getDayOfWeek() {
    return this.dayOfWeek;
  }   // end getDayOfWeek()

  public void setDayOfWeek(String dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }   // end setDayOfWeek()

  public String getDayOfMonth() {
    return this.dayOfMonth;
  }   // end getDayOfMonth()

  public void setDayOfMonth(String dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
  }   // end setDayOfMonth()

  public String getMonthOfYear() {
    return this.monthOfYear;
  }   // end getMonthOfYear()

  public void setMonthOfYear(String monthOfYear) {
    this.monthOfYear = monthOfYear;
  }   // end setMonthOfYear()

  public String getInterval() {
    return this.interval;
  }   // end getInterval()

  public void setInterval(String interval) {
    this.interval = interval;
  }   // end setInterval()

  public String getInstance() {
    return this.instance;
  }   // end getInstance()

  public void setInstance(String instance) {
    this.instance = instance;
  }   // end setInstance()

  public String getRecurrenceStartDate() {
    return this.recurrenceStartDate;
  }   // end getRecurrenceStartDate()

  public void setRecurrenceStartDate(String recurrenceStartDate) {
    this.recurrenceStartDate = recurrenceStartDate;
  }   // end setRecurrenceStartDate()

  public String getRecurrenceEndDate() {
    return this.recurrenceEndDate;
  }   // end getRecurrenceEndDate()

  public void setRecurrenceEndDate(String recurrenceEndDate) {
    this.recurrenceEndDate = recurrenceEndDate;
  }   // end setRecurrenceEndDate()

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.sessionID = null;
    this.activityID = null; 
    this.priority = null;
    this.status = null;
    this.title = null;
    this.description = null;
    this.dueDate = null;
    this.startDateTime = null;
    this.endDateTime = null;
    this.alarmDateTime = null;
    this.untimed  = null;
    this.completed = null;
    this.recurrenceType = null;
    this.dayOfWeek = null;
    this.dayOfMonth = null;
    this.monthOfYear = null;
    this.interval = null;
    this.instance = null;
    this.recurrenceStartDate = null;
    this.recurrenceEndDate = null;
  }   // end reset()
  
}   // end class ActivityEditForm definition
