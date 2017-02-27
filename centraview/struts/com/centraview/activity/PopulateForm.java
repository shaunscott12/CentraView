/*
 * $RCSfile: PopulateForm.java,v $    $Revision: 1.3 $  $Date: 2005/09/20 20:22:18 $ - $Author: mcallist $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import com.centraview.common.DDNameValue;

public class PopulateForm {
  private static Logger logger = Logger.getLogger(PopulateForm.class);

  /**
   * Populates the activity form.
   * @param request The HttpServletRequest request of the ActionForm.
   * @param form The form of the ActionForm.
   */
  public void setForm(HttpServletRequest request, ActionForm form)
  {
    try {
      // get last action of the Activity.
      String previousSubActivity = request.getParameter(ConstantKeys.TYPEOFSUBACTIVITY);

      HttpSession session = request.getSession();

      // Type Cast the ActionForm
      ActivityForm previousForm = (ActivityForm)form;

      // get the ActivityForm from the Session.
      ActivityForm sessionForm = (ActivityForm)session.getAttribute("activityform");

      if (ConstantKeys.DETAIL.equals(previousSubActivity)) {

        if (request.getParameter("remindcheckbox") != null) {
          previousForm.setActivityReminder(request.getParameter("remindcheckbox"));
        }

        if (request.getParameter("emailcheckbox") != null) {
          previousForm.setActivityEmailInvitation(request.getParameter("emailcheckbox"));
        }

        if (sessionForm.getActivityID() != null) {
          previousForm.setActivityID(sessionForm.getActivityID());
        }

        sessionForm.setActivityTitle(previousForm.getActivityTitle());
        sessionForm.setActivityDetail(previousForm.getActivityDetail());
        sessionForm.setActivityEntityID(previousForm.getActivityEntityID());
        sessionForm.setActivityIndividualID(previousForm.getActivityIndividualID());
        sessionForm.setActivityStartTime(previousForm.getActivityStartTime());
        sessionForm.setActivityEndTime(previousForm.getActivityEndTime());
        sessionForm.setActivityOwnerID(previousForm.getActivityOwnerID());
        sessionForm.setActivityReminderTime(previousForm.getActivityReminderTime());
        sessionForm.setActivityPriority(previousForm.getActivityPriority());
        sessionForm.setActivityEntityName(previousForm.getActivityEntityName());
        sessionForm.setActivityIndividualName(previousForm.getActivityIndividualName());
        sessionForm.setActivityOwnerName(previousForm.getActivityOwnerName());
        sessionForm.setActivityReminder(previousForm.getActivityReminder());
        sessionForm.setActivityEmailInvitation(previousForm.getActivityEmailInvitation());
        sessionForm.setActivityNotes(previousForm.getActivityNotes());
        sessionForm.setActivityStatus(previousForm.getActivityStatus());
        sessionForm.setActivityVisibility(previousForm.getActivityVisibility());
        sessionForm.setActivityCallType(previousForm.getActivityCallType());
        sessionForm.setActivityRelatedFieldID(previousForm.getActivityRelatedFieldID());
        sessionForm.setActivityRelatedFieldValue(previousForm.getActivityRelatedFieldValue());
        sessionForm.setActivityRelatedTypeID(previousForm.getActivityRelatedTypeID());
        sessionForm.setActivityRelatedTypeValue(previousForm.getActivityRelatedTypeValue());
      } else if (ConstantKeys.ATTENDEE.equals(previousSubActivity)) {
        String[] attendee = previousForm.getActivityAttendeesRequired();
        Vector attendeeRequired = new Vector();
        DDNameValue attendeeListBox = null;
        String displayName = "";

        if (previousForm.getActivityAttendeesRequired() != null) {
          int sizeOfListBox = attendee.length;
          for (int i = 0; i < sizeOfListBox; i++) {
            int indexOfHash = attendee[i].indexOf("#");
            displayName = attendee[i].substring(indexOfHash + 1, attendee[i].length());
            attendeeListBox = new DDNameValue(attendee[i], displayName);
            attendeeRequired.add(attendeeListBox);
          }
          previousForm.setActivityAttendeeRequiredVector(attendeeRequired);
        }

        String[] optional = previousForm.getActivityAttendeesOptional();
        Vector attendeeOptional = new Vector();
        attendeeListBox = null;
        displayName = "";

        if (previousForm.getActivityAttendeesOptional() != null) {
          int sizeOfListBox = optional.length;
          for (int i = 0; i < sizeOfListBox; i++) {
            int indexOfHash = optional[i].indexOf("#");
            displayName = optional[i].substring(indexOfHash + 1, optional[i].length());
            attendeeListBox = new DDNameValue(optional[i], displayName);
            attendeeOptional.add(attendeeListBox);
          }
          previousForm.setActivityAttendeeOptionalVector(attendeeOptional);
        }

        sessionForm.setActivityAttendeeSearch(previousForm.getActivityAttendeeSearch());
        sessionForm.setActivityAttendeesAll(previousForm.getActivityAttendeesAll());
        sessionForm.setActivityAttendeesRequired(previousForm.getActivityAttendeesRequired());
        sessionForm.setActivityAttendeesOptional(previousForm.getActivityAttendeesOptional());
        sessionForm.setActivityAttendeesType(previousForm.getActivityAttendeesType());
        sessionForm.setActivityAttendeeOptionalVector(previousForm
            .getActivityAttendeeOptionalVector());
        sessionForm.setActivityAttendeeRequiredVector(previousForm
            .getActivityAttendeeRequiredVector());
      } else if (ConstantKeys.RESOURCE.equals(previousSubActivity)) {
        String[] resource = previousForm.getActivityResourceSelected();
        Vector resourceSelected = new Vector();
        DDNameValue resourceListBox = null;
        String displayName = "";

        if (previousForm.getActivityResourceSelected() != null) {
          int sizeOfListBox = resource.length;
          for (int i = 0; i < sizeOfListBox; i++) {
            int indexOfHash = resource[i].indexOf("#");
            displayName = resource[i].substring(indexOfHash + 1, resource[i].length());
            resourceListBox = new DDNameValue(resource[i], displayName);
            resourceSelected.add(resourceListBox);
          }
          previousForm.setActivityResourcevector(resourceSelected);
        }

        sessionForm.setActivityResourceAll(previousForm.getActivityResourceAll());
        sessionForm.setActivityResourceSelected(previousForm.getActivityResourceSelected());
        sessionForm.setActivityResourcevector(previousForm.getActivityResourcevector());
      } else if (ConstantKeys.AVAILABILITY.equals(previousSubActivity)) {
        sessionForm.setActivityStartDate(previousForm.getActivityStartDate());
        sessionForm.setActivityStartTime(previousForm.getActivityStartTime());
        sessionForm.setActivityEndDate(previousForm.getActivityEndDate());
        sessionForm.setActivityEndTime(previousForm.getActivityEndTime());
      } else if (ConstantKeys.RECURRING.equals(previousSubActivity)) {
        sessionForm.setActivityRecurStartDate(previousForm.getActivityRecurStartDate());
        sessionForm.setActivityRecurEndDate(previousForm.getActivityRecurEndDate());
        sessionForm.setActivityRecurringFrequency(previousForm.getActivityRecurringFrequency());
        sessionForm.setActivityRecurringDailyEvery(previousForm.getActivityRecurringDailyEvery());
        sessionForm.setActivityRecurringDailyDays(previousForm.getActivityRecurringDailyDays());
        sessionForm.setActivityRecurringDailyWeekdays(previousForm
            .getActivityRecurringDailyWeekdays());
        sessionForm.setActivityRecurringWeeklyEvery(previousForm.getActivityRecurringWeeklyEvery());
        sessionForm.setActivityRecurringWeeklyOn(previousForm.getActivityRecurringWeeklyOn());
        sessionForm.setActivityFindWeek(previousForm.getActivityFindWeek());
        sessionForm.setActivitySelectedWeek(previousForm.getActivitySelectedWeek());
        sessionForm.setActivityRecurringMonthlyEvery(previousForm
            .getActivityRecurringMonthlyEvery());
        sessionForm.setActivityRecurringMonthlyEveryDay(previousForm
            .getActivityRecurringMonthlyEveryDay());
        sessionForm.setActivityRecurringMonthlyOnDay(previousForm
            .getActivityRecurringMonthlyOnDay());
        sessionForm.setActivityRecurringMonthlyOnWeek(previousForm
            .getActivityRecurringMonthlyOnWeek());
        sessionForm.setActivityRecurringYearlyEvery(previousForm.getActivityRecurringYearlyEvery());
        sessionForm.setActivityRecurringYearlyEveryMonth(previousForm
            .getActivityRecurringYearlyEveryMonth());
        sessionForm.setActivityRecurringYearlyEveryDay(previousForm
            .getActivityRecurringYearlyEveryDay());
        sessionForm.setActivityRecurringYearlyOnDay(previousForm.getActivityRecurringYearlyOnDay());
        sessionForm.setActivityRecurringYearlyOnWeek(previousForm
            .getActivityRecurringYearlyOnWeek());
        sessionForm.setActivityRecurringYearlyOnMonth(previousForm
            .getActivityRecurringYearlyOnMonth());
      } else if (ConstantKeys.ATTACHMENT.equals(previousSubActivity)) {
        Vector fileListVector = new Vector();
        if (previousForm.getActivityFilesList() != null) {
          String[] fileList = previousForm.getActivityFilesList();
          int sizeOfListBox = fileList.length;
          for (int i = 0; i < sizeOfListBox; i++) {
            int indexOfHash = fileList[i].indexOf("#");
            String displayName = fileList[i].substring(indexOfHash + 1, fileList[i].length());
            DDNameValue fileListBox = new DDNameValue(fileList[i], displayName);
            fileListVector.add(fileListBox);
          }
          previousForm.setActivityFilesListVec(fileListVector);
        }
        sessionForm.setActivityAttachedCentraviewFile(previousForm
            .getActivityAttachedCentraviewFile());
        sessionForm.setActivityAttachedFileID(previousForm.getActivityAttachedFileID());
        sessionForm.setActivityFile(previousForm.getActivityFile());
        sessionForm.setActivityFilesList(previousForm.getActivityFilesList());
        sessionForm.setActivityFilesListVec(previousForm.getActivityFilesListVec());
      }
      session.setAttribute("activityform", sessionForm);
    } catch (Exception e) {
      logger.error("[setForm]: Exception", e);
    }
  } // end setForm() method

  /**
   * Method getForm is used for get the form values from the session and reset
   * to the current ActionForm
   * @param request The HttpServletRequest request of the ActionForm.
   * @param form The form of the ActionForm.
   * @return form The form of the ActionForm.
   */
  public ActionForm getForm(HttpServletRequest request, ActionForm form,
      String currentSubActivity)
  {
    ActivityForm currentForm = (ActivityForm)form;
    try {
      HttpSession session = request.getSession();

      ActivityForm sessionForm = null;
      sessionForm = (ActivityForm)session.getAttribute("activityform");

      if (sessionForm.getActivityID() != null)
        currentForm.setActivityID(sessionForm.getActivityID());

      currentForm.setActivityTitle(sessionForm.getActivityTitle());
      currentForm.setActivityDetail(sessionForm.getActivityDetail());
      currentForm.setActivityEntityID(sessionForm.getActivityEntityID());
      currentForm.setActivityIndividualID(sessionForm.getActivityIndividualID());
      currentForm.setActivityStartTime(sessionForm.getActivityStartTime());
      currentForm.setActivityEndTime(sessionForm.getActivityEndTime());
      currentForm.setActivityOwnerID(sessionForm.getActivityOwnerID());
      currentForm.setActivityReminderTime(sessionForm.getActivityReminderTime());
      currentForm.setActivityPriority(sessionForm.getActivityPriority());
      currentForm.setActivityEntityName(sessionForm.getActivityEntityName());
      currentForm.setActivityIndividualName(sessionForm.getActivityIndividualName());
      currentForm.setActivityOwnerName(sessionForm.getActivityOwnerName());
      currentForm.setActivityReminder(sessionForm.getActivityReminder());
      currentForm.setActivityEmailInvitation(sessionForm.getActivityEmailInvitation());
      currentForm.setActivityNotes(sessionForm.getActivityNotes());
      currentForm.setActivityStatus(sessionForm.getActivityStatus());
      currentForm.setActivityVisibility(sessionForm.getActivityVisibility());
      currentForm.setActivityCallType(sessionForm.getActivityCallType());
      currentForm.setActivityRelatedFieldID(sessionForm.getActivityRelatedFieldID());
      currentForm.setActivityRelatedFieldValue(sessionForm.getActivityRelatedFieldValue());
      currentForm.setActivityRelatedTypeID(sessionForm.getActivityRelatedTypeID());
      currentForm.setActivityRelatedTypeValue(sessionForm.getActivityRelatedTypeValue());

      currentForm.setActivityAttendeeSearch(sessionForm.getActivityAttendeeSearch());
      currentForm.setActivityAttendeesAll(sessionForm.getActivityAttendeesAll());
      currentForm.setActivityAttendeesRequired(sessionForm.getActivityAttendeesRequired());
      currentForm.setActivityAttendeesOptional(sessionForm.getActivityAttendeesOptional());
      currentForm.setActivityAttendeesType(sessionForm.getActivityAttendeesType());
      currentForm
          .setActivityAttendeeOptionalVector(sessionForm.getActivityAttendeeOptionalVector());
      currentForm
          .setActivityAttendeeRequiredVector(sessionForm.getActivityAttendeeRequiredVector());

      currentForm.setActivityResourceAll(sessionForm.getActivityResourceAll());
      currentForm.setActivityResourceSelected(sessionForm.getActivityResourceSelected());
      currentForm.setActivityResourcevector(sessionForm.getActivityResourcevector());

      currentForm.setActivityRecurringFrequency(sessionForm.getActivityRecurringFrequency());
      currentForm.setActivityRecurringDailyEvery(sessionForm.getActivityRecurringDailyEvery());
      currentForm.setActivityRecurringDailyDays(sessionForm.getActivityRecurringDailyDays());
      currentForm
          .setActivityRecurringDailyWeekdays(sessionForm.getActivityRecurringDailyWeekdays());
      currentForm.setActivityRecurringWeeklyEvery(sessionForm.getActivityRecurringWeeklyEvery());
      currentForm.setActivityRecurringWeeklyOn(sessionForm.getActivityRecurringWeeklyOn());
      currentForm.setActivityFindWeek(sessionForm.getActivityFindWeek());
      currentForm.setActivitySelectedWeek(sessionForm.getActivitySelectedWeek());
      currentForm.setActivityRecurringMonthlyEvery(sessionForm.getActivityRecurringMonthlyEvery());
      currentForm.setActivityRecurringMonthlyEveryDay(sessionForm
          .getActivityRecurringMonthlyEveryDay());
      currentForm.setActivityRecurringMonthlyOnDay(sessionForm.getActivityRecurringMonthlyOnDay());
      currentForm
          .setActivityRecurringMonthlyOnWeek(sessionForm.getActivityRecurringMonthlyOnWeek());
      currentForm.setActivityRecurringYearlyEvery(sessionForm.getActivityRecurringYearlyEvery());
      currentForm.setActivityRecurringYearlyEveryMonth(sessionForm
          .getActivityRecurringYearlyEveryMonth());
      currentForm.setActivityRecurringYearlyEveryDay(sessionForm
          .getActivityRecurringYearlyEveryDay());
      currentForm.setActivityRecurringYearlyOnDay(sessionForm.getActivityRecurringYearlyOnDay());
      currentForm.setActivityRecurringYearlyOnWeek(sessionForm.getActivityRecurringYearlyOnWeek());
      currentForm
          .setActivityRecurringYearlyOnMonth(sessionForm.getActivityRecurringYearlyOnMonth());

      currentForm
          .setActivityAttachedCentraviewFile(sessionForm.getActivityAttachedCentraviewFile());
      currentForm.setActivityAttachedFileID(sessionForm.getActivityAttachedFileID());
      currentForm.setActivityFile(sessionForm.getActivityFile());
      currentForm.setActivityFilesList(sessionForm.getActivityFilesList());
      currentForm.setActivityFilesListVec(sessionForm.getActivityFilesListVec());
      
      currentForm.setActivityStartDate(sessionForm.getActivityStartDate());
      currentForm.setActivityEndDate(sessionForm.getActivityEndDate());
      currentForm.setActivityRecurStartDate(sessionForm.getActivityRecurStartDate());
      currentForm.setActivityRecurEndDate(sessionForm.getActivityRecurEndDate());
      currentForm.setActivityRemindDate(sessionForm.getActivityRemindDate());

      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, request
          .getParameter(ConstantKeys.TYPEOFACTIVITY));
      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, currentSubActivity);
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, request
          .getParameter(ConstantKeys.TYPEOFOPERATION));

      request.setAttribute("activityform", currentForm);
      session.setAttribute("activityform", currentForm);
    }// end of try block
    catch (Exception e) {
      logger.error("[getForm]: Exception", e);
    }// end of catch block
    return currentForm;
  }// end of getForm (HttpServletRequest request, HttpServletResponse response,
    // ActionForm form, String currentSubActivity)

  /**
   * Method resetForm is used for reseting the form variables.
   * @param request The HttpServletRequest request of the ActionForm.
   * @param form The form of the ActionForm.
   */
  public void resetForm(HttpServletRequest request, ActionForm form)
  {
    try {
      HttpSession session = request.getSession();

      ActivityForm currentForm = (ActivityForm)form;

      currentForm.setActivityType("");
      currentForm.setActivityTypeOfOperation("");
      currentForm.setActivityAction("");
      currentForm.setActivityID("");
      currentForm.setActivityTitle("");
      currentForm.setActivityDetail("");
      String closeornew = request.getParameter("closeornew");
      if (closeornew != null && closeornew.equals("new")) {} else {
        currentForm.setActivityEntityID("");
        currentForm.setActivityEntityName("");
        currentForm.setActivityIndividualID("");
        currentForm.setActivityIndividualName("");
      }
      currentForm.setActivityRelatedFieldID("");
      currentForm.setActivityRelatedFieldValue("");
      currentForm.setActivityRelatedTypeID("");
      currentForm.setActivityRelatedTypeValue("");
      currentForm.setActivityOwnerID("");
      currentForm.setActivityOwnerName("");
      currentForm.setActivityStartDate("");
      currentForm.setActivityStartTime("");
      currentForm.setActivityEndDate("");
      currentForm.setActivityEndTime("");
      currentForm.setActivityPriority("2");
      currentForm.setActivityNotes("");
      currentForm.setActivityStatus("1");
      currentForm.setActivityVisibility("PUBLIC");
      currentForm.setActivityCallType("1");
      currentForm.setActivityReminder("off");
      currentForm.setActivityReminderTime("");
      currentForm.setActivityEmailInvitation("off");

      // Attendee Tab Field
      currentForm.setActivityAttendeesType(null);
      currentForm.setActivityAttendeesAll(null);
      currentForm.setActivityAttendeesOptional(null);
      currentForm.setActivityAttendeesRequired(null);
      currentForm.setActivityAttendeeSearch("");
      currentForm.setActivityAttendeeOptionalVector(new Vector());
      currentForm.setActivityAttendeeRequiredVector(new Vector());

      // Resource Tab
      currentForm.setActivityResourceAll(null);
      currentForm.setActivityResourceSelected(null);
      currentForm.setActivityResourcevector(new Vector());

      // Availability Tab
      currentForm.setActivityAvailabilityStartMonth("");
      currentForm.setActivityAvailabilityStartDay("");
      currentForm.setActivityAvailabilityStartYear("");
      currentForm.setActivityAvailabilityStartTime("");
      currentForm.setActivityAvailabilityEndMonth("");
      currentForm.setActivityAvailabilityEndDay("");
      currentForm.setActivityAvailabilityEndYear("");
      currentForm.setActivityAvailabilityEndTime("");

      // Recurring Tab
      currentForm.setActivityRecurStartDate("");
      currentForm.setActivityRecurringStartTime("");
      currentForm.setActivityRecurEndDate("");
      currentForm.setActivityRecurringEveryDays("");
      String findWeek[] = { "Mo" };
      currentForm.setActivityFindWeek(findWeek);
      currentForm.setActivityRecurringFrequency("");

      // Daily
      currentForm.setActivityRecurringDailyEvery("1");
      currentForm.setActivityRecurringDailyDays("");
      currentForm.setActivityRecurringDailyWeekdays("");

      // Weekly
      currentForm.setActivityRecurringWeeklyEvery("");
      currentForm.setActivityRecurringWeeklyOn(null);

      // Monthly
      currentForm.setActivityRecurringMonthlyEvery("1");
      currentForm.setActivityRecurringMonthlyEveryDay("");
      currentForm.setActivityRecurringMonthlyOnDay("");
      currentForm.setActivityRecurringMonthlyOnWeek("1");

      // Yearly
      currentForm.setActivityRecurringYearlyEvery("1");
      currentForm.setActivityRecurringYearlyEveryMonth("");
      currentForm.setActivityRecurringYearlyEveryDay("");
      currentForm.setActivityRecurringYearlyOnDay("");
      currentForm.setActivityRecurringYearlyOnWeek("1");
      currentForm.setActivityRecurringYearlyOnMonth("");

      // Attachment Tab
      currentForm.setActivityAttachedCentraviewFile("");
      currentForm.setActivityAttachedFileID("");
      currentForm.setActivityAttachedFileName("");
      currentForm.setActivityFile(null);
      currentForm.setActivityFilesList(null);
      currentForm.setActivityFilesListVec(new Vector());
      
      currentForm.setActivityRemindDate("");

      request.setAttribute("activityform", currentForm);
      session.setAttribute("activityform", currentForm);
    }// end of try block
    catch (Exception e) {
      logger.error("[resetForm]: Exception", e);
    } // end of catch block
  } // end of resetForm (HttpServletRequest request, HttpServletResponse
    // response, ActionForm form)
} // end of PopulateForm class
