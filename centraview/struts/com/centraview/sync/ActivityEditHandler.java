/*
 * $RCSfile: ActivityEditHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:58 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;

/**
 * Serves as the handler for the ActivityEdit command request coming from
 * the CompanionLink-Centraview client. All members are the HTTP <b>POST</b>
 * values. Methods are getters and setters.
 *
 * @author  Ryan Grier <ryan@centraview.com>
 */

public class ActivityEditHandler extends ActionForm
{
  // members - these are the request parameters from
  // the CompanionLink client
  private String sessionID = null;
	private String activityID = null;
  private String priority = null;
  private String status = null;
  private String title = null;
  private String description = null;
  private String dueDate = null;
  private String startDateTime = null;
  private String endDateTime = null;
  private String type = null;
  private String created = null;
  private String createdBy = null;
  private String interval = null;
  private String alarmDateTime = null;
  private String untimed = null;
  private String completed = null;
  private String recurrenceType = null;
  private String dayOfWeek = null;
  private String dayOfMonth = null;
  private String monthOfYear = null;
  private String instance = null;
  private String recurrenceStartDate = null;
  private String recurrenceEndDate  = null;
  private int every = 0;
  private int on = 0;


  /**
   * Gets the value of the sessionID HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the sessionID HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getSessionID()
  {
    return(this.sessionID);
  }

  /**
   * Sets the value of the sessionID HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setSessionID(String sessionID)
  {
    this.sessionID = sessionID;
  }

	/**
   * Gets the value of the activityID HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the activityID HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getActivityID()
  {
    return(this.activityID);
  }

  /**
   * Sets the value of the activityID HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setActivityID(String activityID)
  {
    this.activityID = activityID;
  }

  /**
   * Gets the value of the priority HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the priority HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getPriority()
  {
    return(this.priority);
  }

  /**
   * Sets the value of the priority HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setPriority(String priority)
  {
    this.priority = priority;
  }


  /**
   * Gets the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getStatus()
  {
    return(this.status);
  }

  /**
   * Sets the value of the status HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setStatus(String status)
  {
    this.status = status;
  }


  /**
   * Gets the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getTitle()
  {
    return(this.title);
  }

  /**
   * Sets the value of the title HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setTitle(String title)
  {
    this.title = title;
  }


  /**
   * Gets the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getDescription()
  {
    return(this.description);
  }

  /**
   * Sets the value of the description HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setDescription(String description)
  {
    this.description = description;
  }


  /**
   * Gets the value of the dueDate HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the dueDate HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getDueDate()
  {
    return(this.dueDate);
  }

  /**
   * Sets the value of the dueDate HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setDueDate(String dueDate)
  {
    this.dueDate = dueDate;
  }


  /**
   * Gets the value of the startDateTime HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the startDateTime HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getStartDateTime()
  {
    return(this.startDateTime);
  }

  /**
   * Sets the value of the startDateTime HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setStartDateTime(String startDateTime)
  {
    this.startDateTime = startDateTime;
  }


  /**
   * Gets the value of the endDateTime HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the endDateTime HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getEndDateTime()
  {
    return(this.endDateTime);
  }

  /**
   * Sets the value of the endDateTime HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setEndDateTime(String endDateTime)
  {
    this.endDateTime = endDateTime;
  }


  /**
   * Gets the value of the type HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the type HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getType()
  {
System.out.println("[mking] - ActivityEditHandler.getType() called - returning [" + this.type + "]");
    return(this.type);
  }

  /**
   * Sets the value of the type HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setType(String type)
  {
System.out.println("[mking] ActivityEditHandler.setType() called - setting = [" + type + "]");
    this.type = type;
  }


  /**
   * Gets the value of the created HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the created HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getCreated()
  {
    return(this.created);
  }

  /**
   * Sets the value of the created HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setCreated(String created)
  {
    this.created = created;
  }


  /**
   * Gets the value of the createdBy HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the createdBy HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getCreatedBy()
  {
    return(this.createdBy);
  }

  /**
   * Sets the value of the createdBy HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }


  /**
   * Gets the value of the interval HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the interval HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getInterval()
  {
    return(this.interval);
  }

  /**
   * Sets the value of the interval HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setInterval(String interval)
  {
    this.interval = interval;
  }


  /**
   * Gets the value of the alarmDateTime HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the alarmDateTime HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getAlarmDateTime()
  {
    return(this.alarmDateTime);
  }

  /**
   * Sets the value of the alarmDateTime HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setAlarmDateTime(String alarmDateTime)
  {
    this.alarmDateTime = alarmDateTime;
  }


  /**
   * Gets the value of the untimed HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the untimed HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getUntimed()
  {
    return(this.untimed);
  }

  /**
   * Sets the value of the untimed HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setUntimed(String untimed)
  {
    this.untimed = untimed;
  }


  /**
   * Gets the value of the completed HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the completed HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getCompleted()
  {
    return(this.completed);
  }

  /**
   * Sets the value of the completed HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setCompleted(String completed)
  {
    this.completed = completed;
  }


  /**
   * Gets the value of the recurrenceType HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the recurrenceType HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getRecurrenceType()
  {
    return(this.recurrenceType);
  }

  /**
   * Sets the value of the recurrenceType HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setRecurrenceType(String recurrenceType)
  {
    this.recurrenceType = recurrenceType;
  }


  /**
   * Gets the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the dayOfWeek HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getDayOfWeek()
  {
    return(this.dayOfWeek);
  }

  /**
   * Sets the value of the dayOfWeek HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setDayOfWeek(String dayOfWeek)
  {
    this.dayOfWeek = dayOfWeek;
  }


  /**
   * Gets the value of the dayOfMonth HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the dayOfMonth HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getDayOfMonth()
  {
    return(this.dayOfMonth);
  }

  /**
   * Sets the value of the dayOfMonth HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setDayOfMonth(String dayOfMonth)
  {
    this.dayOfMonth = dayOfMonth;
  }


  /**
   * Gets the value of the monthOfYear HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the monthOfYear HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getMonthOfYear()
  {
    return(this.monthOfYear);
  }

  /**
   * Sets the value of the monthOfYear HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setMonthOfYear(String monthOfYear)
  {
    this.monthOfYear = monthOfYear;
  }


  /**
   * Gets the value of the instance HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the instance HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getInstance()
  {
    return(this.instance);
  }

  /**
   * Sets the value of the instance HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setInstance(String instance)
  {
    this.instance = instance;
  }


  /**
   * Gets the value of the recurrenceStartDate HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the recurrenceStartDate HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getRecurrenceStartDate()
  {
    return(this.recurrenceStartDate);
  }

  /**
   * Sets the value of the recurrenceStartDate HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setRecurrenceStartDate(String recurrenceStartDate)
  {
    this.recurrenceStartDate = recurrenceStartDate;
  }


  /**
   * Gets the value of the recurrenceEndDate HTTP POST parameter to the ActivityEdit.do command.
   * @return  the value of the recurrenceEndDate HTTP POST parameter to the ActivityEdit.do command.
   */
  public String getRecurrenceEndDate()
  {
    return(this.recurrenceEndDate);
  }

  /**
   * Sets the value of the recurrenceEndDate HTTP POST parameter of the ActivityEdit.do command.
   * Called by Struts.
   * @return void
   */
  public void setRecurrenceEndDate(String recurrenceEndDate)
  {
    this.recurrenceEndDate = recurrenceEndDate;
  }

  public void setEvery(int every)
  {
    this.every = every;
  }

  public int getEvery()
  {
    return(this.every);
  }

  public void setOn(int on)
  {
    this.on = on;
  }

  public int getOn()
  {
    return(this.on);
  }

    public String toString()
    {
      StringBuffer sb = new StringBuffer("");
      sb.append("ActivityAddHandler = [");
      sb.append("sessionID = {" + this.sessionID + "}, ");
      sb.append("priority = {" + this.priority + "}, ");
      sb.append("status = {" + this.status + "}, ");
      sb.append("title = {" + this.title + "}, ");
      sb.append("description = {" + this.description + "}, ");
      sb.append("dueDate = {" + this.dueDate + "}, ");
      sb.append("startDateTime = {" + this.startDateTime + "}, ");
      sb.append("endDateTime = {" + this.endDateTime + "}, ");
      sb.append("type = {" + this.type + "}, ");
      sb.append("created = {" + this.created + "}, ");
      sb.append("createdBy = {" + this.createdBy + "}, ");
      sb.append("interval = {" + this.interval + "}, ");
      sb.append("alarmDateTime = {" + this.alarmDateTime + "}, ");
      sb.append("untimed = {" + this.untimed + "}, ");
      sb.append("completed = {" + this.completed + "}, ");
      sb.append("recurrenceType = {" + this.recurrenceType + "}, ");
      sb.append("dayOfWeek = {" + this.dayOfWeek + "}, ");
      sb.append("dayOfMonth = {" + this.dayOfMonth + "}, ");
      sb.append("monthOfYear = {" + this.monthOfYear + "}, ");
      sb.append("instance = {" + this.instance + "}, ");
      sb.append("recurrenceStartDate = {" + this.recurrenceStartDate + "}, ");
      sb.append("recurrenceEndDate = {" + this.recurrenceEndDate + "}, ");
      sb.append("every = {" + this.every + "}, ");
      sb.append("on = {" + this.on + "}, ");
      sb.append("]");
      return(sb.toString());
  }
}

