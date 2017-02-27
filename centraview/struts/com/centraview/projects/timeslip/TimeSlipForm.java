/*
 * $RCSfile: TimeSlipForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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

package com.centraview.projects.timeslip;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.CVUtility;

public class TimeSlipForm extends ActionForm {
  private static Logger logger = Logger.getLogger(TimeSlipForm.class);
  private String lookupList;
  private String project;
  private String projectID;
  private String ticketID;
  private String ticket;
  private String timeSlipID;
  private String projectLookUpButton;
  private String task;
  private String description;
  private String month;
  private String day;
  private String reference;
  private String year;
  private String startTime;
  private String selStartTime;
  private String endTime;
  private String selEndTime;
  private String breakHours;
  private String breakMinutes;
  private String duration;
  private String activityID;
  private String timesheetID = "0";

  public String getLookupList()
  {
    return lookupList;
  }

  public void setLookupList(String sel)
  {
    lookupList = sel;
  }

  /**
   * Returns the breakHours.
   * @return String
   */
  public String getBreakHours()
  {
    return breakHours;
  }

  /**
   * Returns the breakMinutes.
   * @return String
   */
  public String getBreakMinutes()
  {
    return breakMinutes;
  }

  /**
   * Returns the day.
   * @return String
   */
  public String getDay()
  {
    return day;
  }

  /**
   * Returns the description.
   * @return String
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the duration.
   * @return String
   */
  public String getDuration()
  {
    return duration;
  }

  /**
   * Returns the endTime.
   * @return String
   */
  public String getEndTime()
  {
    return endTime;
  }

  /**
   * Returns the iD.
   * @return String
   */
  public String getTimeSlipID()
  {
    return timeSlipID;
  }

  /**
   * Returns the month.
   * @return String
   */
  public String getMonth()
  {
    return month;
  }

  /**
   * Returns the reference.
   * @return String
   */
  public String getReference()
  {
    return reference;
  }

  /**
   * Returns the reference.
   * @return String
   */
  public String getTicket()
  {
    return ticket;
  }

  /**
   * Returns the project.
   * @return String
   */
  public String getProject()
  {
    return project;
  }

  /**
   * Returns the projectID.
   * @return String
   */
  public String getProjectID()
  {
    return projectID;
  }

  public String getTicketID()
  {
    return ticketID;
  }

  /**
   * Returns the projectLookUpButton.
   * @return String
   */
  public String getProjectLookUpButton()
  {
    return projectLookUpButton;
  }

  /**
   * Returns the selEndTime.
   * @return String
   */
  public String getSelEndTime()
  {
    return selEndTime;
  }

  /**
   * Returns the selStartTime.
   * @return String
   */
  public String getSelStartTime()
  {
    return selStartTime;
  }

  /**
   * Returns the startTime.
   * @return String
   */
  public String getStartTime()
  {
    return startTime;
  }

  /**
   * Returns the task.
   * @return String
   */
  public String getTask()
  {
    return task;
  }

  /**
   * Returns the year.
   * @return String
   */
  public String getYear()
  {
    return year;
  }

  public String getActivityID()
  {
    return activityID;
  }

  public void setActivityID(String activityID)
  {
    this.activityID = activityID;
  }

  /**
   * Sets the breakHours.
   * @param breakHours The breakHours to set
   */
  public void setBreakHours(String breakHours)
  {
    this.breakHours = breakHours;
  }

  /**
   * Sets the breakMinutes.
   * @param breakMinutes The breakMinutes to set
   */
  public void setBreakMinutes(String breakMinutes)
  {
    this.breakMinutes = breakMinutes;
  }

  /**
   * Sets the day.
   * @param day The day to set
   */
  public void setDay(String day)
  {
    this.day = day;
  }

  /**
   * Sets the description.
   * @param description The description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Sets the duration.
   * @param duration The duration to set
   */
  public void setDuration(String duration)
  {
    this.duration = duration;
  }

  /**
   * Sets the endTime.
   * @param endTime The endTime to set
   */
  public void setEndTime(String endTime)
  {
    this.endTime = endTime;
  }

  /**
   * Sets the timeSlipID.
   * @param timeSlipID The timeSlipID to set
   */
  public void setTimeSlipID(String timeSlipID)
  {
    this.timeSlipID = timeSlipID;
  }

  /**
   * Sets the month.
   * @param month The month to set
   */
  public void setMonth(String month)
  {
    this.month = month;
  }

  /**
   * Sets the project.
   * @param project The project to set
   */
  public void setProject(String project)
  {
    this.project = project;
  }

  /**
   * Sets the reference.
   * @param reference The reference to set
   */
  public void setReference(String reference)
  {
    this.reference = reference;
  }

  /**
   * Sets the ticket.
   * @param ticket The reference to set
   */
  public void setTicket(String ticket)
  {
    this.ticket = ticket;
  }

  /**
   * Sets the projectID.
   * @param projectID The projectID to set
   */
  public void setProjectID(String projectID)
  {
    this.projectID = projectID;
  }

  public void setTicketID(String ticketID)
  {
    this.ticketID = ticketID;
  }

  /**
   * Sets the projectLookUpButton.
   * @param projectLookUpButton The projectLookUpButton to set
   */
  public void setProjectLookUpButton(String projectLookUpButton)
  {
    this.projectLookUpButton = projectLookUpButton;
  }

  /**
   * Sets the selEndTime.
   * @param selEndTime The selEndTime to set
   */
  public void setSelEndTime(String selEndTime)
  {
    this.selEndTime = selEndTime;
  }

  /**
   * Sets the selStartTime.
   * @param selStartTime The selStartTime to set
   */
  public void setSelStartTime(String selStartTime)
  {
    this.selStartTime = selStartTime;
  }

  /**
   * Sets the startTime.
   * @param startTime The startTime to set
   */
  public void setStartTime(String startTime)
  {
    this.startTime = startTime;
  }

  /**
   * Sets the task.
   * @param task The task to set
   */
  public void setTask(String task)
  {
    this.task = task;
  }

  /**
   * Sets the year.
   * @param year The year to set
   */
  public void setYear(String year)
  {
    this.year = year;
  }

  public void setTimesheetID(String timesheetID)
  {
    this.timesheetID = timesheetID;
  }

  public String getTimesheetID()
  {
    return timesheetID;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();

    try {
      if (request.getParameter("timeslip") != null) {
        if (request.getParameter("timeslip").equalsIgnoreCase("support")) {
          if (getDescription() == null || getDescription().trim().length() <= 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.general.requiredField", "Description"));
          }
          // validation.checkForDate("BottomTimeslipList.Date", getYear(),
          // getMonth(), getDay(), "error.application.date", "", errors);
        }
      } else {
        if (getProject() != null && (!getProject().equals(""))) {
          if (getReference() == null || getReference().trim().length() <= 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.general.requiredField", "Reference"));
          }
          if (getTask() == null || getTask().trim().length() <= 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.general.requiredField", "Task"));
          }
        }

        if (getDescription() == null || getDescription().trim().length() <= 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
              "error.general.requiredField", "Description"));
        }
        // validation.checkForDate("BottomTimeslipList.Date", getYear(),
        // getMonth(), getDay(), "error.application.date", "", errors);
      }

      int startHrs = 0, startMins = 0, endHrs = 0, endMins = 0;

      if (getStartTime() != null && !getStartTime().equals("")) {
        // convert time to 24 hrs// convert time to 24 hrs
        int[] startTime = CVUtility.convertTimeTo24HrsFormat(getStartTime());
        startHrs = startTime[0];
        startMins = startTime[1];
      }

      if (getEndTime() != null && !getEndTime().equals("")) {
        // convert time to 24 hrs
        int[] endTime = CVUtility.convertTimeTo24HrsFormat(getEndTime());
        endHrs = endTime[0];
        endMins = endTime[1];
      }

      if (startHrs > endHrs) {
        ActionMessage error = new ActionMessage("error.application.timecomparison", "EndTime",
            "StartTime");
        errors.add("error.application.timecomparison", error);
      } else if (startHrs == endHrs && startMins > endMins) {
        ActionMessage error = new ActionMessage("error.application.timecomparison", "EndTime",
            "StartTime");
        errors.add("error.application.timecomparison", error);
      }

      float fBreakHrs = Float.parseFloat(getBreakHours());
      float fBreakMins = Float.parseFloat(getBreakMinutes());
      float fBreakTime = 0;

      if (fBreakMins != 0) {
        fBreakTime = fBreakHrs + fBreakMins / 60;
      } else {
        fBreakTime = fBreakHrs;
      }

      float remMin = 0;
      if (startMins != 0 && endMins != 0) {
        if (endMins > startMins) {
          remMin = 60 / (endMins - startMins);
        } else {
          remMin = 60 / (startMins - endMins);
        }
      }

      Float Duration = new Float((endHrs - startHrs) + remMin - fBreakTime);
      if (Duration.floatValue() <= 0.0) {
        ActionMessage error = new ActionMessage("error.projects.timeslip.totaltimediff",
            "Start and End Time difference", "BreakTime");
        errors.add("error.projects.timeslip.totaltimediff", error);
      }
    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    }
    return errors;
  }

  public void clearForm()
  {
    this.setProjectID("0");
    this.setTicketID("0");
    this.setTicket("");
    this.setProject("");
    this.setTimeSlipID("");
    this.setYear("");
    this.setMonth("");
    this.setDay("");
    this.setTask("");
    this.setStartTime("");
    this.setEndTime("");
    this.setDescription("");
    this.setBreakHours("");
    this.setBreakMinutes("");
    this.setReference("");
    this.setLookupList("");
  }
}
