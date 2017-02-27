/*
 * $RCSfile: ActivityForm.java,v $    $Revision: 1.7 $  $Date: 2005/10/24 21:13:01 $ - $Author: mcallist $
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

import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.centraview.common.DDNameValue;
import com.centraview.common.Validation;

/**
 * All fields related to activity are defined in this class. Validation is done
 * at the time of save and edit of activity.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class ActivityForm extends ActionForm {
  private static Logger logger = Logger.getLogger(ActivityForm.class);

  /**
   * set the default locale to US to avoid NPE the action class that processes
   * the form should set the locale.
   */
  private Locale locale = Locale.US;

  // Detail Tab Fields
  private String activityType;
  private String activityTypeOfOperation;
  private String activityAction;
  private String activityID = "0";
  private String activityTitle;
  private String activityDetail;
  private String activityEntityID;
  private String activityEntityName;
  private String activityIndividualID;
  private String activityIndividualName;
  private String activityIndividualPrimaryContact;
  private String activityRelatedFieldID;
  private String activityRelatedFieldValue;
  private String activityRelatedTypeID;
  private String activityRelatedTypeValue;
  private String activityOwnerID;
  private String activityOwnerName;
  // Dates
  private String activityStartDate;
  private String activityStartTime;
  private String activityEndDate;
  private String activityEndTime;
  private String activityRemindDate;
  private String activityReminderTime;
  private String selStartTime;
  private String selEndTime;
  private String selRemindTime;

  private String activityPriority = "2";
  private String activityNotes;
  private String activityStatus = "1";
  private String activityVisibility = "PUBLIC";
  private String activityCallType = "1";
  private String activityReminder = "off";
  private String activityEmailInvitation = "off";

  // Attendee Tab Field
  private String activityAttendeesType;
  private String activityAttendeesAll[];
  private String activityAttendeesOptional[];
  private String activityAttendeesRequired[];
  private String activityAttendeeSearch;
  private Vector activityAttendeeOptionalVector;
  private Vector activityAttendeeRequiredVector;

  // Resource Tab
  private String activityResourceAll[];
  private String activityResourceSelected[];
  private Vector activityResourcevector;

  // Availability Tab
  private String activityAvailabilityStartMonth;
  private String activityAvailabilityStartDay;
  private String activityAvailabilityStartYear;
  private String activityAvailabilityStartTime;
  private String activityAvailabilityEndMonth;
  private String activityAvailabilityEndDay;
  private String activityAvailabilityEndYear;
  private String activityAvailabilityEndTime;

  // Recurring Tab
  private String activityRecurStartDate;
  private String activityRecurringStartTime;
  private String activityRecurEndDate;
  private String activityRecurringEveryDays;
  private String activityFindWeek[] = { "Mo" };
  private String activitySelectedWeek[] = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
  private String activityRecurringFrequency;

  // Daily
  private String activityRecurringDailyEvery = "1";
  private String activityRecurringDailyDays;
  private String activityRecurringDailyWeekdays;

  // Weekly
  private String activityRecurringWeeklyEvery;
  private String activityRecurringWeeklyOn[];

  // Monthly
  private String activityRecurringMonthlyEvery = "1";
  private String activityRecurringMonthlyEveryDay;
  private String activityRecurringMonthlyOnDay;
  private String activityRecurringMonthlyOnWeek = "1";

  // Yearly
  private String activityRecurringYearlyEvery = "1";
  private String activityRecurringYearlyEveryMonth;
  private String activityRecurringYearlyEveryDay;
  private String activityRecurringYearlyOnDay;
  private String activityRecurringYearlyOnWeek = "1";
  private String activityRecurringYearlyOnMonth;

  // Attachment Tab
  private String activityAttachedCentraviewFile;
  private String activityAttachedFileID;
  private String activityAttachedFileName;
  private FormFile activityFile;
  private String activityFilesList[];
  private Vector activityFilesListVec;

  // Additional Field
  private String linkCompany;
  private String linkContact;

  // Detail Tab Fields
  /**
   * Returns the activityType for this Activity.
   * @return this.The activityType for the Activity.
   */
  public String getActivityType()
  {
    return this.activityType;
  }// end of getActivityType()

  /**
   * Sets the activityType for this Activity Form.
   * @param activityType The new activityType for the Activity Form.
   */
  public void setActivityType(String activityType)
  {
    this.activityType = activityType;
  }// end of setActivityType(String activityType)

  /**
   * Returns the activityTypeOfOperation for this Activity.
   * @return this.The activityTypeOfOperation for the Activity.
   */
  public String getActivityTypeOfOperation()
  {
    return this.activityTypeOfOperation;
  }// end of getActivityTypeOfOperation()

  /**
   * Sets the activityTypeOfOperation for this Activity Form.
   * @param activityTypeOfOperation The new activityTypeOfOperation for the
   *          Activity Form.
   */
  public void setActivityTypeOfOperation(String activityTypeOfOperation)
  {
    this.activityTypeOfOperation = activityTypeOfOperation;
  }// end of setActivityTypeOfOperation(String activityTypeOfOperation)

  /**
   * Returns the activityAction for this Activity.
   * @return this.The activityAction for the Activity.
   */
  public String getActivityAction()
  {
    return this.activityAction;
  }// end of getActivityAction()

  /**
   * Sets the activityAction for this Activity Form.
   * @param activityType The new activityAction for the Activity Form.
   */
  public void setActivityAction(String activityAction)
  {
    this.activityAction = activityAction;
  }// end of setActivityAction(String activityAction)

  /**
   * Returns the activityID for this Activity.
   * @return this.The activityID for the Activity.
   */
  public String getActivityID()
  {
    return this.activityID;
  }// end of getActivityID()

  /**
   * Sets the activityID for this Activity Form.
   * @param activityID The new activityID for the Activity Form.
   */
  public void setActivityID(String activityID)
  {
    this.activityID = activityID;
  }// end of setActivityID(String activityID)

  /**
   * Returns the activityTitle for this Activity.
   * @return this.The activityTitle for the Activity.
   */
  public String getActivityTitle()
  {
    return this.activityTitle;
  }// end of getActivityTitle()

  /**
   * Sets the activityTitle for this Activity Form.
   * @param activityType The new activityTitle for the Activity Form.
   */
  public void setActivityTitle(String activityTitle)
  {
    this.activityTitle = activityTitle;
  }// end of setActivityTitle(String activityTitle)

  /**
   * Returns the activityDetail for this Activity.
   * @return this.The activityDetail for the Activity.
   */
  public String getActivityDetail()
  {
    return this.activityDetail;
  }// end of getActivityDetail()

  /**
   * Sets the activityDetail for this Activity Form.
   * @param activityDetail The new activityDetail for the Activity Form.
   */
  public void setActivityDetail(String activityDetail)
  {
    this.activityDetail = activityDetail;
  }// end of setActivityDetail(String activityDetail)

  /**
   * Returns the activityEntityID for this Activity.
   * @return this.The activityEntityID for the Activity.
   */
  public String getActivityEntityID()
  {
    return this.activityEntityID;
  }// end of getActivityEntityID()

  /**
   * Sets the activityEntityID for this Activity Form.
   * @param activityEntityID The new activityEntityID for the Activity Form.
   */
  public void setActivityEntityID(String activityEntityID)
  {
    this.activityEntityID = activityEntityID;
  }// end of setActivityEntityID(String activityEntityID)

  /**
   * Returns the activityEntityName for this Activity.
   * @return this.The activityEntityName for the Activity.
   */
  public String getActivityEntityName()
  {
    return this.activityEntityName;
  }// end of getActivityEntityName()

  /**
   * Sets the activityEntityName for this Activity Form.
   * @param activityEntityName The new activityEntityName for the Activity Form.
   */
  public void setActivityEntityName(String activityEntityName)
  {
    this.activityEntityName = activityEntityName;
  }// end of setActivityEntityName(String activityEntityName)

  /**
   * Returns the activityIndividualID for this Activity.
   * @return this.The activityIndividualID for the Activity.
   */
  public String getActivityIndividualID()
  {
    return this.activityIndividualID;
  }// end of getActivityIndividualID()

  /**
   * Sets the activityIndividualID for this Activity Form.
   * @param activityIndividualID The new activityIndividualID for the Activity
   *          Form.
   */
  public void setActivityIndividualID(String activityIndividualID)
  {
    this.activityIndividualID = activityIndividualID;
  }// end of setActivityIndividualID(String activityIndividualID)

  /**
   * Returns the activityIndividualName for this Activity.
   * @return this.The activityIndividualName for the Activity.
   */
  public String getActivityIndividualName()
  {
    return this.activityIndividualName;
  }// end of getActivityIndividualName()

  /**
   * Sets the activityIndividualName for this Activity Form.
   * @param activityIndividualName The new activityIndividualName for the
   *          Activity Form.
   */
  public void setActivityIndividualName(String activityIndividualName)
  {
    this.activityIndividualName = activityIndividualName;
  }// end of setActivityIndividualName(String activityIndividualName)

  /**
   * Returns the activityIndividualPrimaryContact for this Activity.
   * @return this.The activityIndividualName for the Activity.
   */
  public String getActivityIndividualPrimaryContact()
  {
    return this.activityIndividualPrimaryContact;
  }// end of getActivityIndividualName()

  /**
   * Sets the activityIndividualName for this Activity Form.
   * @param activityIndividualName The new activityIndividualName for the
   *          Activity Form.
   */
  public void setActivityIndividualPrimaryContact(String activityIndividualPrimaryContact)
  {
    this.activityIndividualPrimaryContact = activityIndividualPrimaryContact;
  }// end of setActivityIndividualName(String activityIndividualName)

  /**
   * Returns the activityRelatedFieldID for this Activity.
   * @return this.The activityRelatedFieldID for the Activity.
   */
  public String getActivityRelatedFieldID()
  {
    return this.activityRelatedFieldID;
  }// end of getActivityRelatedFieldID()

  /**
   * Sets the activityRelatedFieldID for this Activity Form.
   * @param activityRelatedFieldID The new activityRelatedFieldID for the
   *          Activity Form.
   */
  public void setActivityRelatedFieldID(String activityRelatedFieldID)
  {
    this.activityRelatedFieldID = activityRelatedFieldID;
  }// end of setActivityRelatedFieldID(String activityRelatedFieldID)

  /**
   * Returns the activityRelatedFieldValue for this Activity.
   * @return this.The activityRelatedFieldValue for the Activity.
   */
  public String getActivityRelatedFieldValue()
  {
    return this.activityRelatedFieldValue;
  }// end of getActivityRelatedFieldValue()

  /**
   * Sets the activityRelatedFieldValue for this Activity Form.
   * @param activityRelatedFieldValue The new activityRelatedFieldValue for the
   *          Activity Form.
   */
  public void setActivityRelatedFieldValue(String activityRelatedFieldValue)
  {
    this.activityRelatedFieldValue = activityRelatedFieldValue;
  }// end of setActivityRelatedFieldValue(String activityRelatedFieldValue)

  /**
   * Returns the activityRelatedTypeID for this Activity.
   * @return this.The activityRelatedTypeID for the Activity.
   */
  public String getActivityRelatedTypeID()
  {
    return this.activityRelatedTypeID;
  }// end of getActivityRelatedTypeID()

  /**
   * Sets the activityRelatedTypeID for this Activity Form.
   * @param activityRelatedTypeID The new activityRelatedTypeID for the Activity
   *          Form.
   */
  public void setActivityRelatedTypeID(String activityRelatedTypeID)
  {
    this.activityRelatedTypeID = activityRelatedTypeID;
  }// end of setActivityRelatedTypeID(String activityRelatedTypeID)

  /**
   * Returns the activityRelatedTypeValue for this Activity.
   * @return this.The activityRelatedTypeValue for the Activity.
   */
  public String getActivityRelatedTypeValue()
  {
    return this.activityRelatedTypeValue;
  }// end of getActivityRelatedTypeValue()

  /**
   * Sets the activityRelatedTypeValue for this Activity Form.
   * @param activityRelatedTypeValue The new activityRelatedTypeValue for the
   *          Activity Form.
   */
  public void setActivityRelatedTypeValue(String activityRelatedTypeValue)
  {
    this.activityRelatedTypeValue = activityRelatedTypeValue;
  }// end of setActivityRelatedTypeValue(String activityRelatedTypeValue)

  /**
   * Returns the activityOwnerID for this Activity.
   * @return this.The activityOwnerID for the Activity.
   */
  public String getActivityOwnerID()
  {
    return this.activityOwnerID;
  }// end of getActivityOwnerID()

  /**
   * Sets the activityOwnerID for this Activity Form.
   * @param activityOwnerID The new activityOwnerID for the Activity Form.
   */
  public void setActivityOwnerID(String activityOwnerID)
  {
    this.activityOwnerID = activityOwnerID;
  }// end of setActivityOwnerID(String activityOwnerID)

  /**
   * Returns the activityOwnerName for this Activity.
   * @return this.The activityOwnerName for the Activity.
   */
  public String getActivityOwnerName()
  {
    return this.activityOwnerName;
  }// end of getActivityOwnerName()

  /**
   * Sets the activityOwnerName for this Activity Form.
   * @param activityOwnerName The new activityOwnerName for the Activity Form.
   */
  public void setActivityOwnerName(String activityOwnerName)
  {
    this.activityOwnerName = activityOwnerName;
  }// end of setActivityOwnerName(String activityOwnerName)

  /**
   * Returns the activityStartTime for this Activity.
   * @return this.The activityStartTime for the Activity.
   */
  public String getActivityStartTime()
  {
    return this.activityStartTime;
  }// end of getActivityStartTime()

  /**
   * Sets the activityStartTime for this Activity Form.
   * @param activityStartTime The new activityStartTime for the Activity Form.
   */
  public void setActivityStartTime(String activityStartTime)
  {
    this.activityStartTime = activityStartTime;
  }// end of setActivityStartTime(String activityStartTime)

  /**
   * Returns the activityEndTime for this Activity.
   * @return this.The activityEndTime for the Activity.
   */
  public String getActivityEndTime()
  {
    return this.activityEndTime;
  }// end of getActivityEndTime()

  /**
   * Sets the activityEndTime for this Activity Form.
   * @param activityEndTime The new activityEndTime for the Activity Form.
   */
  public void setActivityEndTime(String activityEndTime)
  {
    this.activityEndTime = activityEndTime;
  }// end of setActivityEndTime(String activityEndTime)

  /**
   * Returns the activityPriority for this Activity.
   * @return this.The activityPriority for the Activity.
   */
  public String getActivityPriority()
  {
    return this.activityPriority;
  }// end of getActivityPriority()

  /**
   * Sets the activityPriority for this Activity Form.
   * @param activityPriority The new activityPriority for the Activity Form.
   */
  public void setActivityPriority(String activityPriority)
  {
    this.activityPriority = activityPriority;
  }// end of setActivityPriority(String activityPriority)

  /**
   * Returns the activityNotes for this Activity.
   * @return this.The activityNotes for the Activity.
   */
  public String getActivityNotes()
  {
    return this.activityNotes;
  }// end of getActivityNotes()

  /**
   * Sets the activityNotes for this Activity Form.
   * @param activityNotes The new activityNotes for the Activity Form.
   */
  public void setActivityNotes(String activityNotes)
  {
    this.activityNotes = activityNotes;
  }// end of setActivityNotes(String activityNotes)

  /**
   * Returns the activityStatus for this Activity.
   * @return this.The activityStatus for the Activity.
   */
  public String getActivityStatus()
  {
    return this.activityStatus;
  }// end of getActivityStatus()

  /**
   * Sets the activityStatus for this Activity Form.
   * @param activityStatus The new activityStatus for the Activity Form.
   */
  public void setActivityStatus(String activityStatus)
  {
    this.activityStatus = activityStatus;
  }// end of setActivityStatus(String activityStatus)

  /**
   * Returns the activityVisibility for this Activity.
   * @return this.The activityVisibility for the Activity.
   */
  public String getActivityVisibility()
  {
    return this.activityVisibility;
  }// end of getActivityVisibility()

  /**
   * Sets the activityType for this Activity Form.
   * @param activityVisibility The new activityVisibility for the Activity Form.
   */
  public void setActivityVisibility(String activityVisibility)
  {
    this.activityVisibility = activityVisibility;
  }// end of setActivityVisibility(String activityVisibility)

  /**
   * Returns the activityCallType for this Activity.
   * @return this.The activityCallType for the Activity.
   */
  public String getActivityCallType()
  {
    return this.activityCallType;
  }// end of getActivityCallType()

  /**
   * Sets the activityCallType for this Activity Form.
   * @param activityCallType The new activityCallType for the Activity Form.
   */
  public void setActivityCallType(String activityCallType)
  {
    this.activityCallType = activityCallType;
  }// end of setActivityCallType(String activityCallType)

  /**
   * Returns the activityReminder for this Activity.
   * @return this.The activityReminder for the Activity.
   */
  public String getActivityReminder()
  {
    return this.activityReminder;
  }// end of getActivityReminder()

  /**
   * Sets the activityReminder for this Activity Form.
   * @param activityReminder The new activityReminder for the Activity Form.
   */
  public void setActivityReminder(String activityReminder)
  {
    this.activityReminder = activityReminder;
  }// end of setActivityReminder(String activityReminder)

  /**
   * Returns the companyName for this Activity.
   * @return this.The companyName for the Activity.
   */
  public String getLinkCompany()
  {
    return this.linkCompany;
  }// end of getLinkCompany()

  /**
   * Returns the linkContact for this Activity.
   * @return this.The linkContact for the Activity.
   */
  public String getLinkContact()
  {
    return this.linkContact;
  }// end of getLinkContact()

  /**
   * Returns the activityReminderTime for this Activity.
   * @return this.The activityReminderTime for the Activity.
   */
  public String getActivityReminderTime()
  {
    return this.activityReminderTime;
  }// end of getActivityReminderTime()

  /**
   * Sets the activityReminderTime for this Activity Form.
   * @param activityReminderTime The new activityReminderTime for the Activity
   *          Form.
   */
  public void setActivityReminderTime(String activityReminderTime)
  {
    this.activityReminderTime = activityReminderTime;
  }// end of setActivityReminderTime(String activityReminderTime)

  /**
   * Returns the activityEmailInvitation for this Activity.
   * @return this.The activityEmailInvitation for the Activity.
   */
  public String getActivityEmailInvitation()
  {
    return this.activityEmailInvitation;
  }// end of getActivityEmailInvitation()

  /**
   * Sets the activityEmailInvitation for this Activity Form.
   * @param activityEmailInvitation The new activityEmailInvitation for the
   *          Activity Form.
   */
  public void setActivityEmailInvitation(String activityEmailInvitation)
  {
    this.activityEmailInvitation = activityEmailInvitation;
  }// end of setActivityEmailInvitation(String activityEmailInvitation)

  /**
   * Returns the selStartTime for this Activity.
   * @return this.The selStartTime for the Activity.
   */
  public String getSelStartTime()
  {
    return this.selStartTime;
  }// end of getSelStartTime()

  /**
   * Sets the selStartTime for this Activity Form.
   * @param selStartTime The new selStartTime for the Activity Form.
   */
  public void setSelStartTime(String selStartTime)
  {
    this.selStartTime = selStartTime;
  }// end of setSelStartTime(String selStartTime)

  /**
   * Returns the selEndTime for this Activity.
   * @return this.The selEndTime for the Activity.
   */
  public String getSelEndTime()
  {
    return this.selEndTime;
  }// end of getSelEndTime()

  /**
   * Sets the selEndTime for this Activity Form.
   * @param selEndTime The new selEndTime for the Activity Form.
   */
  public void setSelEndTime(String selEndTime)
  {
    this.selEndTime = selEndTime;
  }// end of setSelEndTime(String selEndTime)

  /**
   * Returns the selRemindTime for this Activity.
   * @return this.The selRemindTime for the Activity.
   */
  public String getSelRemindTime()
  {
    return this.selRemindTime;
  }// end of getSelRemindTime()

  /**
   * Sets the selRemindTime for this Activity Form.
   * @param selRemindTime The new selRemindTime for the Activity Form.
   */
  public void setSelRemindTime(String selRemindTime)
  {
    this.selRemindTime = selRemindTime;
  }// end of setSelRemindTime(String selRemindTime)

  // Attendee Tab Field

  /**
   * Returns the activityAttendeesType for this Activity.
   * @return this.The activityAttendeesType for the Activity.
   */
  public String getActivityAttendeesType()
  {
    return this.activityAttendeesType;
  }// end of getActivityAttendeesType()

  /**
   * Sets the activityAttendeesType for this Activity Form.
   * @param activityAttendeesType The new activityAttendeesType for the Activity
   *          Form.
   */
  public void setActivityAttendeesType(String activityAttendeesType)
  {
    this.activityAttendeesType = activityAttendeesType;
  }// end of setActivityAttendeesType(String activityAttendeesType)

  /**
   * Returns the activityAttendeesAll for this Activity.
   * @return this.The activityAttendeesAll for the Activity.
   */
  public String[] getActivityAttendeesAll()
  {
    return this.activityAttendeesAll;
  }// end of getActivityAttendeesAll()

  /**
   * Sets the activityAttendeesAll for this Activity Form.
   * @param activityAttendeesAll The new activityAttendeesAll for the Activity
   *          Form.
   */
  public void setActivityAttendeesAll(String[] activityAttendeesAll)
  {
    this.activityAttendeesAll = activityAttendeesAll;
  }// end of setActivityAttendeesAll(String[] activityAttendeesAll)

  /**
   * Returns the activityType for this Activity.
   * @return this.The activityType for the Activity.
   */
  public String[] getActivityAttendeesOptional()
  {
    return this.activityAttendeesOptional;
  }// end of getActivityAttendeesOptional()

  /**
   * Sets the activityAttendeesOptional for this Activity Form.
   * @param activityAttendeesOptional The new activityAttendeesOptional for the
   *          Activity Form.
   */
  public void setActivityAttendeesOptional(String[] activityAttendeesOptional)
  {
    this.activityAttendeesOptional = activityAttendeesOptional;
  }// end of setActivityAttendeesOptional(String[] activityAttendeesOptional)

  /**
   * Returns the activityAttendeesRequired for this Activity.
   * @return this.The activityAttendeesRequired for the Activity.
   */
  public String[] getActivityAttendeesRequired()
  {
    return this.activityAttendeesRequired;
  }// end of getActivityAttendeesRequired()

  /**
   * Sets the activityAttendeesRequired for this Activity Form.
   * @param activityAttendeesRequired The new activityAttendeesRequired for the
   *          Activity Form.
   */
  public void setActivityAttendeesRequired(String[] activityAttendeesRequired)
  {
    this.activityAttendeesRequired = activityAttendeesRequired;
  }// end of setActivityAttendeesRequired(String[] activityAttendeesRequired)

  /**
   * Returns the activityAttendeeSearch for this Activity.
   * @return this.The activityAttendeeSearch for the Activity.
   */
  public String getActivityAttendeeSearch()
  {
    return this.activityAttendeeSearch;
  }// end of getActivityAttendeeSearch()

  /**
   * Sets the activityAttendeeSearch for this Activity Form.
   * @param activityAttendeeSearch The new activityAttendeeSearch for the
   *          Activity Form.
   */
  public void setActivityAttendeeSearch(String activityAttendeeSearch)
  {
    this.activityAttendeeSearch = activityAttendeeSearch;
  }// end of setActivityAttendeeSearch(String activityAttendeeSearch)

  /**
   * Returns the activityAttendeeOptionalVector for this Activity.
   * @return this.The activityAttendeeOptionalVector for the Activity.
   */
  public Vector getActivityAttendeeOptionalVector()
  {
    return this.activityAttendeeOptionalVector;
  }// end of getActivityAttendeeOptionalVector()

  /**
   * Sets the activityAttendeeOptionalVector for this Activity Form.
   * @param activityAttendeeOptionalVector The new
   *          activityAttendeeOptionalVector for the Activity Form.
   */
  public void setActivityAttendeeOptionalVector(Vector activityAttendeeOptionalVector)
  {
    this.activityAttendeeOptionalVector = activityAttendeeOptionalVector;
  }// end of setActivityAttendeeOptionalVector(Vector

  // activityAttendeeOptionalVector)

  /**
   * Returns the activityAttendeeRequiredVector for this Activity.
   * @return this.The activityAttendeeRequiredVector for the Activity.
   */
  public Vector getActivityAttendeeRequiredVector()
  {
    return this.activityAttendeeRequiredVector;
  }// end of getActivityAttendeeRequiredVector()

  /**
   * Sets the activityAttendeeRequiredVector for this Activity Form.
   * @param activityAttendeeRequiredVector The new
   *          activityAttendeeRequiredVector for the Activity Form.
   */
  public void setActivityAttendeeRequiredVector(Vector activityAttendeeRequiredVector)
  {
    this.activityAttendeeRequiredVector = activityAttendeeRequiredVector;
  }// end of setActivityAttendeeRequiredVector(Vector

  // activityAttendeeRequiredVector)

  // Resource Tab

  /**
   * Returns the activityResourceAll for this Activity.
   * @return this.The activityResourceAll for the Activity.
   */
  public String[] getActivityResourceAll()
  {
    return this.activityResourceAll;
  }// end of getActivityResourceAll()

  /**
   * Sets the activityResourceAll for this Activity Form.
   * @param activityResourceAll The new activityResourceAll for the Activity
   *          Form.
   */
  public void setActivityResourceAll(String[] activityResourceAll)
  {
    this.activityResourceAll = activityResourceAll;
  }// end of setActivityResourceAll(String[] activityResourceAll)

  /**
   * Returns the activityResourceSelected for this Activity.
   * @return this.The activityResourceSelected for the Activity.
   */
  public String[] getActivityResourceSelected()
  {
    return this.activityResourceSelected;
  }// end of getActivityResourceSelected()

  /**
   * Sets the activityResourceSelected for this Activity Form.
   * @param activityResourceSelected The new activityResourceSelected for the
   *          Activity Form.
   */
  public void setActivityResourceSelected(String[] activityResourceSelected)
  {
    this.activityResourceSelected = activityResourceSelected;
  }// end of setActivityResourceSelected(String[] activityResourceSelected)

  /**
   * Returns the activityResourcevector for this Activity.
   * @return this.The activityResourcevector for the Activity.
   */
  public Vector getActivityResourcevector()
  {
    return this.activityResourcevector;
  }// end of getActivityResourcevector()

  /**
   * Sets the activityResourcevector for this Activity Form.
   * @param activityResourcevector The new activityResourcevector for the
   *          Activity Form.
   */
  public void setActivityResourcevector(Vector activityResourcevector)
  {
    this.activityResourcevector = activityResourcevector;
  }// end of setActivityResourcevector(Vector activityResourcevector)

  // Availability Tab

  /**
   * Returns the activityAvailabilityStartMonth for this Activity.
   * @return this.The activityAvailabilityStartMonth for the Activity.
   */
  public String getActivityAvailabilityStartMonth()
  {
    return this.activityAvailabilityStartMonth;
  }// end of getActivityAvailabilityStartMonth()

  /**
   * Sets the activityAvailabilityStartMonth for this Activity Form.
   * @param activityAvailabilityStartMonth The new
   *          activityAvailabilityStartMonth for the Activity Form.
   */
  public void setActivityAvailabilityStartMonth(String activityAvailabilityStartMonth)
  {
    this.activityAvailabilityStartMonth = activityAvailabilityStartMonth;
  }// end of setActivityAvailabilityStartMonth(String

  // activityAvailabilityStartMonth)

  /**
   * Returns the activityAvailabilityStartDay for this Activity.
   * @return this.The activityAvailabilityStartDay for the Activity.
   */
  public String getActivityAvailabilityStartDay()
  {
    return this.activityAvailabilityStartDay;
  }// end of getActivityAvailabilityStartDay()

  /**
   * Sets the activityAvailabilityStartDay for this Activity Form.
   * @param activityAvailabilityStartDay The new activityAvailabilityStartDay
   *          for the Activity Form.
   */
  public void setActivityAvailabilityStartDay(String activityAvailabilityStartDay)
  {
    this.activityAvailabilityStartDay = activityAvailabilityStartDay;
  }// end of setActivityAvailabilityStartDay(String

  // activityAvailabilityStartDay)

  /**
   * Returns the activityAvailabilityStartYear for this Activity.
   * @return this.The activityAvailabilityStartYear for the Activity.
   */
  public String getActivityAvailabilityStartYear()
  {
    return this.activityAvailabilityStartYear;
  }// end of getActivityAvailabilityStartYear()

  /**
   * Sets the activityAvailabilityStartYear for this Activity Form.
   * @param activityAvailabilityStartYear The new activityAvailabilityStartYear
   *          for the Activity Form.
   */
  public void setActivityAvailabilityStartYear(String activityAvailabilityStartYear)
  {
    this.activityAvailabilityStartYear = activityAvailabilityStartYear;
  }// end of setActivityAvailabilityStartYear(String

  // activityAvailabilityStartYear)

  /**
   * Returns the activityAvailabilityStartTime for this Activity.
   * @return this.The activityAvailabilityStartTime for the Activity.
   */
  public String getActivityAvailabilityStartTime()
  {
    return this.activityAvailabilityStartTime;
  }// end of getActivityAvailabilityStartTime()

  /**
   * Sets the activityAvailabilityStartTime for this Activity Form.
   * @param activityAvailabilityStartTime The new activityAvailabilityStartTime
   *          for the Activity Form.
   */
  public void setActivityAvailabilityStartTime(String activityAvailabilityStartTime)
  {
    this.activityAvailabilityStartTime = activityAvailabilityStartTime;
  }// end of setActivityAvailabilityStartTime(String

  // activityAvailabilityStartTime)

  /**
   * Returns the activityAvailabilityEndMonth for this Activity.
   * @return this.The activityAvailabilityEndMonth for the Activity.
   */

  public String getActivityAvailabilityEndMonth()
  {
    return this.activityAvailabilityEndMonth;
  }// end of getActivityAvailabilityEndMonth()

  /**
   * Sets the activityAvailabilityEndMonth for this Activity Form.
   * @param activityAvailabilityEndMonth The new activityAvailabilityEndMonth
   *          for the Activity Form.
   */
  public void setActivityAvailabilityEndMonth(String activityAvailabilityEndMonth)
  {
    this.activityAvailabilityEndMonth = activityAvailabilityEndMonth;
  }// end of setActivityAvailabilityEndMonth(String

  // activityAvailabilityEndMonth)

  /**
   * Returns the activityAvailabilityEndDay for this Activity.
   * @return this.The activityAvailabilityEndDay for the Activity.
   */
  public String getActivityAvailabilityEndDay()
  {
    return this.activityAvailabilityEndDay;
  }// end of getActivityAvailabilityEndDay()

  /**
   * Sets the activityAvailabilityEndDay for this Activity Form.
   * @param activityAvailabilityEndDay The new activityAvailabilityEndDay for
   *          the Activity Form.
   */
  public void setActivityAvailabilityEndDay(String activityAvailabilityEndDay)
  {
    this.activityAvailabilityEndDay = activityAvailabilityEndDay;
  }// end of setActivityAvailabilityEndDay(String activityAvailabilityEndDay)

  /**
   * Returns the activityAvailabilityEndYear for this Activity.
   * @return this.The activityAvailabilityEndYear for the Activity.
   */
  public String getActivityAvailabilityEndYear()
  {
    return this.activityAvailabilityEndYear;
  }// end of getActivityAvailabilityEndYear()

  /**
   * Sets the activityAvailabilityEndYear for this Activity Form.
   * @param activityAvailabilityEndYear The new activityAvailabilityEndYear for
   *          the Activity Form.
   */
  public void setActivityAvailabilityEndYear(String activityAvailabilityEndYear)
  {
    this.activityAvailabilityEndYear = activityAvailabilityEndYear;
  }// end of setActivityAvailabilityEndYear(String activityAvailabilityEndYear)

  /**
   * Returns the activityAvailabilityEndTime for this Activity.
   * @return this.The activityAvailabilityEndTime for the Activity.
   */
  public String getActivityAvailabilityEndTime()
  {
    return this.activityAvailabilityEndTime;
  }// end of getActivityAvailabilityEndTime()

  /**
   * Sets the activityAvailabilityEndTime for this Activity Form.
   * @param activityAvailabilityEndTime The new activityAvailabilityEndTime for
   *          the Activity Form.
   */
  public void setActivityAvailabilityEndTime(String activityAvailabilityEndTime)
  {
    this.activityAvailabilityEndTime = activityAvailabilityEndTime;
  }// end of setActivityAvailabilityEndTime(String activityAvailabilityEndTime)

  // Recurring Tab

  /**
   * Returns the activityRecurringStartTime for this Activity.
   * @return this.The activityRecurringStartTime for the Activity.
   */
  public String getActivityRecurringStartTime()
  {
    return this.activityRecurringStartTime;
  }// end of getActivityRecurringStartTime()

  /**
   * Sets the activityRecurringStartTime for this Activity Form.
   * @param activityRecurringStartTime The new activityRecurringStartTime for
   *          the Activity Form.
   */
  public void setActivityRecurringStartTime(String activityRecurringStartTime)
  {
    this.activityRecurringStartTime = activityRecurringStartTime;
  }// end of setActivityRecurringStartTime(String activityRecurringStartTime)

  /**
   * Returns the activityRecurringEveryDays for this Activity.
   * @return this.The activityRecurringEveryDays for the Activity.
   */
  public String getActivityRecurringEveryDays()
  {
    return this.activityRecurringEveryDays;
  }// end of getActivityRecurringEveryDays()

  /**
   * Sets the activityRecurringEveryDays for this Activity Form.
   * @param activityRecurringEveryDays The new activityRecurringEveryDays for
   *          the Activity Form.
   */
  public void setActivityRecurringEveryDays(String activityRecurringEveryDays)
  {
    this.activityRecurringEveryDays = activityRecurringEveryDays;
  }// end of setActivityRecurringEveryDays(String activityRecurringEveryDays)

  /**
   * Returns the activityFindWeek for this Activity.
   * @return this.The activityFindWeek for the Activity.
   */
  public String[] getActivityFindWeek()
  {
    return this.activityFindWeek;
  }// end of getActivityFindWeek()

  /**
   * Sets the activityFindWeek for this Activity Form.
   * @param activityFindWeek The new activityFindWeek for the Activity Form.
   */
  public void setActivityFindWeek(String[] activityFindWeek)
  {
    this.activityFindWeek = activityFindWeek;
  }// end of setActivityFindWeek(String[] activityFindWeek)

  /**
   * Returns the activitySelectedWeek for this Activity.
   * @return this.The activitySelectedWeek for the Activity.
   */
  public String[] getActivitySelectedWeek()
  {
    return this.activitySelectedWeek;
  }// end of getActivitySelectedWeek()

  /**
   * Sets the activitySelectedWeek for this Activity Form.
   * @param activitySelectedWeek The new activitySelectedWeek for the Activity
   *          Form.
   */
  public void setActivitySelectedWeek(String[] activitySelectedWeek)
  {
    this.activitySelectedWeek = activitySelectedWeek;
  }// end of setActivitySelectedWeek(String[] activitySelectedWeek)

  /**
   * Returns the activityRecurringFrequency for this Activity.
   * @return this.The activityRecurringFrequency for the Activity.
   */
  public String getActivityRecurringFrequency()
  {
    return this.activityRecurringFrequency;
  }// end of getActivityRecurringFrequency()

  /**
   * Sets the activityRecurringFrequency for this Activity Form.
   * @param activityRecurringFrequency The new activityRecurringFrequency for
   *          the Activity Form.
   */
  public void setActivityRecurringFrequency(String activityRecurringFrequency)
  {
    this.activityRecurringFrequency = activityRecurringFrequency;
  }// end of setActivityRecurringFrequency(String activityRecurringFrequency)

  // Daily

  /**
   * Returns the activityRecurringDailyEvery for this Activity.
   * @return this.The activityRecurringDailyEvery for the Activity.
   */
  public String getActivityRecurringDailyEvery()
  {
    return this.activityRecurringDailyEvery;
  }// end of getActivityRecurringDailyEvery()

  /**
   * Sets the activityRecurringDailyEvery for this Activity Form.
   * @param activityRecurringDailyEvery The new activityRecurringDailyEvery for
   *          the Activity Form.
   */
  public void setActivityRecurringDailyEvery(String activityRecurringDailyEvery)
  {
    this.activityRecurringDailyEvery = activityRecurringDailyEvery;
  }// end of setActivityRecurringDailyEvery(String activityRecurringDailyEvery)

  /**
   * Returns the activityRecurringDailyDays for this Activity.
   * @return this.The activityRecurringDailyDays for the Activity.
   */
  public String getActivityRecurringDailyDays()
  {
    return this.activityRecurringDailyDays;
  }// end of getActivityRecurringDailyDays()

  /**
   * Sets the activityRecurringDailyDays for this Activity Form.
   * @param activityRecurringDailyDays The new activityRecurringDailyDays for
   *          the Activity Form.
   */
  public void setActivityRecurringDailyDays(String activityRecurringDailyDays)
  {
    this.activityRecurringDailyDays = activityRecurringDailyDays;
  }// end of setActivityRecurringDailyDays(String activityRecurringDailyDays)

  /**
   * Returns the activityRecurringDailyWeekdays for this Activity.
   * @return this.The activityRecurringDailyWeekdays for the Activity.
   */
  public String getActivityRecurringDailyWeekdays()
  {
    return this.activityRecurringDailyWeekdays;
  }// end of getActivityRecurringDailyWeekdays()

  /**
   * Sets the activityRecurringDailyWeekdays for this Activity Form.
   * @param activityRecurringDailyWeekdays The new
   *          activityRecurringDailyWeekdays for the Activity Form.
   */
  public void setActivityRecurringDailyWeekdays(String activityRecurringDailyWeekdays)
  {
    this.activityRecurringDailyWeekdays = activityRecurringDailyWeekdays;
  }// end of setActivityRecurringDailyWeekdays(String

  // activityRecurringDailyWeekdays)

  // Weekly

  /**
   * Returns the activityRecurringWeeklyEvery for this Activity.
   * @return this.The activityRecurringWeeklyEvery for the Activity.
   */
  public String getActivityRecurringWeeklyEvery()
  {
    return this.activityRecurringWeeklyEvery;
  }// end of getActivityRecurringWeeklyEvery()

  /**
   * Sets the activityRecurringWeeklyEvery for this Activity Form.
   * @param activityRecurringWeeklyEvery The new activityRecurringWeeklyEvery
   *          for the Activity Form.
   */
  public void setActivityRecurringWeeklyEvery(String activityRecurringWeeklyEvery)
  {
    this.activityRecurringWeeklyEvery = activityRecurringWeeklyEvery;
  }// end of setActivityRecurringWeeklyEvery(String

  // activityRecurringWeeklyEvery)

  /**
   * Returns the activityRecurringWeeklyOn for this Activity.
   * @return this.The activityRecurringWeeklyOn for the Activity.
   */
  public String[] getActivityRecurringWeeklyOn()
  {
    return this.activityRecurringWeeklyOn;
  }// end of getActivityRecurringWeeklyOn()

  /**
   * Sets the activityRecurringWeeklyOn for this Activity Form.
   * @param activityRecurringWeeklyOn The new activityRecurringWeeklyOn for the
   *          Activity Form.
   */
  public void setActivityRecurringWeeklyOn(String[] activityRecurringWeeklyOn)
  {
    this.activityRecurringWeeklyOn = activityRecurringWeeklyOn;
  }// end of setActivityRecurringWeeklyOn(String[] activityRecurringWeeklyOn)

  // Monthly

  /**
   * Returns the activityRecurringMonthlyEvery for this Activity.
   * @return this.The activityRecurringMonthlyEvery for the Activity.
   */
  public String getActivityRecurringMonthlyEvery()
  {
    return this.activityRecurringMonthlyEvery;
  }// end of getActivityRecurringMonthlyEvery()

  /**
   * Sets the activityRecurringMonthlyEvery for this Activity Form.
   * @param activityRecurringMonthlyEvery The new activityRecurringMonthlyEvery
   *          for the Activity Form.
   */
  public void setActivityRecurringMonthlyEvery(String activityRecurringMonthlyEvery)
  {
    this.activityRecurringMonthlyEvery = activityRecurringMonthlyEvery;
  }// end of setActivityRecurringMonthlyEvery(String

  // activityRecurringMonthlyEvery)

  /**
   * Returns the activityRecurringMonthlyEveryDay for this Activity.
   * @return this.The activityRecurringMonthlyEveryDay for the Activity.
   */
  public String getActivityRecurringMonthlyEveryDay()
  {
    return this.activityRecurringMonthlyEveryDay;
  }// end of getActivityRecurringMonthlyEveryDay()

  /**
   * Sets the activityRecurringMonthlyEveryDay for this Activity Form.
   * @param activityRecurringMonthlyEveryDay The new
   *          activityRecurringMonthlyEveryDay for the Activity Form.
   */
  public void setActivityRecurringMonthlyEveryDay(String activityRecurringMonthlyEveryDay)
  {
    this.activityRecurringMonthlyEveryDay = activityRecurringMonthlyEveryDay;
  }// end of setActivityRecurringMonthlyEveryDay(String

  // activityRecurringMonthlyEveryDay)

  /**
   * Returns the activityRecurringMonthlyOnDay for this Activity.
   * @return this.The activityRecurringMonthlyOnDay for the Activity.
   */
  public String getActivityRecurringMonthlyOnDay()
  {
    return this.activityRecurringMonthlyOnDay;
  }// end of getActivityRecurringMonthlyOnDay()

  /**
   * Sets the activityRecurringMonthlyOnDay for this Activity Form.
   * @param activityRecurringMonthlyOnDay The new activityRecurringMonthlyOnDay
   *          for the Activity Form.
   */
  public void setActivityRecurringMonthlyOnDay(String activityRecurringMonthlyOnDay)
  {
    this.activityRecurringMonthlyOnDay = activityRecurringMonthlyOnDay;
  }// end of setActivityRecurringMonthlyOnDay(String

  // activityRecurringMonthlyOnDay)

  /**
   * Returns the activityRecurringMonthlyOnWeek for this Activity.
   * @return this.The activityRecurringMonthlyOnWeek for the Activity.
   */
  public String getActivityRecurringMonthlyOnWeek()
  {
    return this.activityRecurringMonthlyOnWeek;
  }// end of getActivityRecurringMonthlyOnWeek()

  /**
   * Sets the activityRecurringMonthlyOnWeek for this Activity Form.
   * @param activityRecurringMonthlyOnWeek The new
   *          activityRecurringMonthlyOnWeek for the Activity Form.
   */
  public void setActivityRecurringMonthlyOnWeek(String activityRecurringMonthlyOnWeek)
  {
    this.activityRecurringMonthlyOnWeek = activityRecurringMonthlyOnWeek;
  }// end of setActivityRecurringMonthlyOnWeek(String

  // activityRecurringMonthlyOnWeek)

  // Yearly

  /**
   * Returns the activityRecurringYearlyEvery for this Activity.
   * @return this.The activityRecurringYearlyEvery for the Activity.
   */
  public String getActivityRecurringYearlyEvery()
  {
    return this.activityRecurringYearlyEvery;
  }// end of getActivityRecurringYearlyEvery()

  /**
   * Sets the activityRecurringYearlyEvery for this Activity Form.
   * @param activityRecurringYearlyEvery The new activityRecurringYearlyEvery
   *          for the Activity Form.
   */
  public void setActivityRecurringYearlyEvery(String activityRecurringYearlyEvery)
  {
    this.activityRecurringYearlyEvery = activityRecurringYearlyEvery;
  }// end of setActivityRecurringYearlyEvery(String

  // activityRecurringYearlyEvery)

  /**
   * Returns the activityRecurringYearlyEveryMonth for this Activity.
   * @return this.The activityRecurringYearlyEveryMonth for the Activity.
   */
  public String getActivityRecurringYearlyEveryMonth()
  {
    return this.activityRecurringYearlyEveryMonth;
  }// end of getActivityRecurringYearlyEveryMonth()

  /**
   * Sets the activityRecurringYearlyEveryMonth for this Activity Form.
   * @param activityRecurringYearlyEveryMonth The new
   *          activityRecurringYearlyEveryMonth for the Activity Form.
   */
  public void setActivityRecurringYearlyEveryMonth(String activityRecurringYearlyEveryMonth)
  {
    this.activityRecurringYearlyEveryMonth = activityRecurringYearlyEveryMonth;
  }// end of setActivityRecurringYearlyEveryMonth(String

  // activityRecurringYearlyEveryMonth)

  /**
   * Returns the activityRecurringYearlyEveryDay for this Activity.
   * @return this.The activityRecurringYearlyEveryDay for the Activity.
   */
  public String getActivityRecurringYearlyEveryDay()
  {
    return this.activityRecurringYearlyEveryDay;
  }// end of getActivityRecurringYearlyEveryDay()

  /**
   * Sets the activityRecurringYearlyEveryDay for this Activity Form.
   * @param activityRecurringYearlyEveryDay The new
   *          activityRecurringYearlyEveryDay for the Activity Form.
   */
  public void setActivityRecurringYearlyEveryDay(String activityRecurringYearlyEveryDay)
  {
    this.activityRecurringYearlyEveryDay = activityRecurringYearlyEveryDay;
  }// end of setActivityRecurringYearlyEveryDay(String

  // activityRecurringYearlyEveryDay)

  /**
   * Returns the activityRecurringYearlyOnDay for this Activity.
   * @return this.The activityRecurringYearlyOnDay for the Activity.
   */
  public String getActivityRecurringYearlyOnDay()
  {
    return this.activityRecurringYearlyOnDay;
  }// end of getActivityRecurringYearlyOnDay()

  /**
   * Sets the activityRecurringYearlyOnDay for this Activity Form.
   * @param activityRecurringYearlyOnDay The new activityRecurringYearlyOnDay
   *          for the Activity Form.
   */
  public void setActivityRecurringYearlyOnDay(String activityRecurringYearlyOnDay)
  {
    this.activityRecurringYearlyOnDay = activityRecurringYearlyOnDay;
  }// end of setActivityRecurringYearlyOnDay(String

  // activityRecurringYearlyOnDay)

  /**
   * Returns the activityRecurringYearlyOnWeek for this Activity.
   * @return this.The activityRecurringYearlyOnWeek for the Activity.
   */
  public String getActivityRecurringYearlyOnWeek()
  {
    return this.activityRecurringYearlyOnWeek;
  }// end of getActivityRecurringYearlyOnWeek()

  /**
   * Sets the activityRecurringYearlyOnWeek for this Activity Form.
   * @param activityRecurringYearlyOnWeek The new activityRecurringYearlyOnWeek
   *          for the Activity Form.
   */
  public void setActivityRecurringYearlyOnWeek(String activityRecurringYearlyOnWeek)
  {
    this.activityRecurringYearlyOnWeek = activityRecurringYearlyOnWeek;
  }// end of setActivityRecurringYearlyOnWeek(String

  // activityRecurringYearlyOnWeek)

  /**
   * Returns the activityRecurringYearlyOnMonth for this Activity.
   * @return this.The activityRecurringYearlyOnMonth for the Activity.
   */
  public String getActivityRecurringYearlyOnMonth()
  {
    return this.activityRecurringYearlyOnMonth;
  }// end of getActivityRecurringYearlyOnMonth()

  /**
   * Sets the activityRecurringYearlyOnMonth for this Activity Form.
   * @param activityRecurringYearlyOnMonth The new
   *          activityRecurringYearlyOnMonth for the Activity Form.
   */
  public void setActivityRecurringYearlyOnMonth(String activityRecurringYearlyOnMonth)
  {
    this.activityRecurringYearlyOnMonth = activityRecurringYearlyOnMonth;
  }// end of setActivityRecurringYearlyOnMonth(String

  // activityRecurringYearlyOnMonth)

  // Attachment Tab

  /**
   * Returns the activityAttachedCentraviewFile for this Activity.
   * @return this.The activityAttachedCentraviewFile for the Activity.
   */
  public String getActivityAttachedCentraviewFile()
  {
    return this.activityAttachedCentraviewFile;
  }// end of getActivityAttachedCentraviewFile()

  /**
   * Sets the activityAttachedCentraviewFile for this Activity Form.
   * @param activityAttachedCentraviewFile The new
   *          activityAttachedCentraviewFile for the Activity Form.
   */
  public void setActivityAttachedCentraviewFile(String activityAttachedCentraviewFile)
  {
    this.activityAttachedCentraviewFile = activityAttachedCentraviewFile;
  }// end of setActivityAttachedCentraviewFile(String

  // activityAttachedCentraviewFile)

  /**
   * Returns the activityAttachedFileID for this Activity.
   * @return this.The activityAttachedFileID for the Activity.
   */
  public String getActivityAttachedFileID()
  {
    return this.activityAttachedFileID;
  }// end of getActivityAttachedFileID()

  /**
   * Sets the activityAttachedFileID for this Activity Form.
   * @param activityAttachedFileID The new activityAttachedFileID for the
   *          Activity Form.
   */
  public void setActivityAttachedFileID(String activityAttachedFileID)
  {
    this.activityAttachedFileID = activityAttachedFileID;
  }// end of setActivityAttachedFileID(String activityAttachedFileID)

  /**
   * Returns the activityAttachedFileName for this Activity.
   * @return this.The activityAttachedFileName for the Activity.
   */
  public String getActivityAttachedFileName()
  {
    return this.activityAttachedFileName;
  }// end of getActivityAttachedFileName()

  /**
   * Sets the activityAttachedFileName for this Activity Form.
   * @param activityAttachedFileName The new activityAttachedFileName for the
   *          Activity Form.
   */
  public void setActivityAttachedFileName(String activityAttachedFileName)
  {
    this.activityAttachedFileName = activityAttachedFileName;
  }// end of setActivityAttachedFileName(String activityAttachedFileName)

  /**
   * Returns the activityFile for this Activity.
   * @return this.The activityFile for the Activity.
   */
  public FormFile getActivityFile()
  {
    return this.activityFile;
  }// end of getActivityFile()

  /**
   * Sets the activityFile for this Activity Form.
   * @param activityFile The new activityFile for the Activity Form.
   */
  public void setActivityFile(FormFile activityFile)
  {
    this.activityFile = activityFile;
  }// end of setActivityFile(FormFile activityFile)

  /**
   * Returns the activityFilesList for this Activity.
   * @return this.The activityFilesList for the Activity.
   */
  public String[] getActivityFilesList()
  {
    return this.activityFilesList;
  }// end of getActivityFilesList()

  /**
   * Sets the activityFilesList for this Activity Form.
   * @param activityFilesList The new activityFilesList for the Activity Form.
   */
  public void setActivityFilesList(String[] activityFilesList)
  {
    this.activityFilesList = activityFilesList;
  }// end of setActivityFilesList(String[] activityFilesList)

  /**
   * Returns the activityFilesListVec for this Activity.
   * @return this.The activityFilesListVec for the Activity.
   */
  public Vector getActivityFilesListVec()
  {
    return this.activityFilesListVec;
  }// end of getActivityFilesListVec()

  /**
   * Sets the activityFilesListVec for this Activity Form.
   * @param activityFilesListVec The new activityFilesListVec for the Activity
   *          Form.
   */
  public void setActivityFilesListVec(Vector activityFilesListVec)
  {
    this.activityFilesListVec = activityFilesListVec;
  }// end of setActivityFilesListVec(Vector activityFilesListVec)

  /**
   * Sets the linkCompany for this Activity Form.
   * @param linkCompany The new linkCompany for the Activity Form.
   */
  public void setLinkCompany(String linkCompany)
  {
    this.linkCompany = linkCompany;
  }// end of setLinkCompany(String linkCompany)

  /**
   * Sets the linkContact for this Activity Form.
   * @param linkContact The new linkContact for the Activity Form.
   */
  public void setLinkContact(String linkContact)
  {
    this.linkContact = linkContact;
  }// end of setLinkContact(String linkContact)

  public String getActivityEndDate()
  {
    return this.activityEndDate;
  }

  public void setActivityEndDate(String activityEnd)
  {
    this.activityEndDate = activityEnd;
  }

  public String getActivityStartDate()
  {
    return this.activityStartDate;
  }

  public void setActivityStartDate(String activityStart)
  {
    this.activityStartDate = activityStart;
  }

  public String getActivityRemindDate()
  {
    return this.activityRemindDate;
  }

  public void setActivityRemindDate(String activityRemind)
  {
    this.activityRemindDate = activityRemind;
  }

  public String getActivityRecurEndDate()
  {
    return this.activityRecurEndDate;
  }

  public void setActivityRecurEndDate(String activityRecurEnd)
  {
    this.activityRecurEndDate = activityRecurEnd;
  }

  public String getActivityRecurStartDate()
  {
    return this.activityRecurStartDate;
  }

  public void setActivityRecurStartDate(String activityRecurStart)
  {
    this.activityRecurStartDate = activityRecurStart;
  }

  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  public Locale getLocale()
  {
    return this.locale;
  }

  /**
   * Validation done at the time of saving, editing the form values.
   * <ul>
   * Validation is done on following user input
   * <li> Required fileds
   * <li> Maxlength
   * <li> Stringeger
   * <li> Date
   * <li> Time
   * <li> Date comparison
   * </ul>
   * @param mapping The ActionMapping of the activity form.
   * @param request The new HttpServletRequest of the activity form.
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    // initialize new actionerror object
    ActionErrors errors = new ActionErrors();
    try {

      // get from session the form associated with windowid
      HttpSession session = request.getSession();
      ActivityForm sessionForm = (ActivityForm)session.getAttribute("activityform");

      // The Following if block will be used when we are creating a new activity
      // and just enter the values
      // in the detail form and try to save the form. it will set the value in
      // the sessionForm and validate the inputs
      if ((ConstantKeys.DETAIL).equals(request.getParameter(ConstantKeys.TYPEOFSUBACTIVITY)
          .toString())) {
        sessionForm.setActivityTitle(this.getActivityTitle());
        sessionForm.setActivityDetail(this.getActivityDetail());
        sessionForm.setActivityEntityID(this.getActivityEntityID());
        sessionForm.setActivityIndividualID(this.getActivityIndividualID());
        sessionForm.setActivityStartTime(this.getActivityStartTime());
        sessionForm.setActivityEndTime(this.getActivityEndTime());
        sessionForm.setActivityOwnerID(this.getActivityOwnerID());
        sessionForm.setActivityReminderTime(this.getActivityReminderTime());
        sessionForm.setActivityPriority(this.getActivityPriority());
        sessionForm.setActivityEntityName(this.getActivityEntityName());
        sessionForm.setActivityIndividualName(this.getActivityIndividualName());
        sessionForm.setActivityOwnerName(this.getActivityOwnerName());
        sessionForm.setActivityReminder(this.getActivityReminder());
        sessionForm.setActivityEmailInvitation(this.getActivityEmailInvitation());
        sessionForm.setActivityNotes(this.getActivityNotes());
        sessionForm.setActivityStatus(this.getActivityStatus());
        sessionForm.setActivityVisibility(this.getActivityVisibility());
        sessionForm.setActivityCallType(this.getActivityCallType());
        sessionForm.setActivityRelatedFieldID(this.getActivityRelatedFieldID());
        sessionForm.setActivityRelatedFieldValue(this.getActivityRelatedFieldValue());
        sessionForm.setActivityRelatedTypeID(this.getActivityRelatedTypeID());
        sessionForm.setActivityRelatedTypeValue(this.getActivityRelatedTypeValue());
        sessionForm.setActivityStartDate(this.getActivityStartDate());
        sessionForm.setActivityEndDate(this.getActivityEndDate());
        sessionForm.setActivityRecurStartDate(this.getActivityRecurStartDate());
        sessionForm.setActivityRecurEndDate(this.getActivityRecurEndDate());
        sessionForm.setActivityRemindDate(this.getActivityRemindDate());
      }
      if ((ConstantKeys.RECURRING).equals(request.getParameter(ConstantKeys.TYPEOFSUBACTIVITY)
          .toString())) {
        sessionForm.setActivityRecurringFrequency(this.getActivityRecurringFrequency());
        sessionForm.setActivityRecurringDailyDays(this.getActivityRecurringDailyDays());
        sessionForm.setActivityRecurringWeeklyEvery(this.getActivityRecurringWeeklyEvery());
        sessionForm.setActivityRecurringMonthlyEvery(this.getActivityRecurringMonthlyEvery());
        sessionForm.setActivityRecurringMonthlyEveryDay(this.getActivityRecurringMonthlyEveryDay());
        sessionForm.setActivityRecurringYearlyEvery(this.getActivityRecurringYearlyEvery());
        sessionForm.setActivityRecurringYearlyEveryDay(this.getActivityRecurringYearlyEveryDay());
      }
      if ((ConstantKeys.ATTACHMENT).equals(request.getParameter(ConstantKeys.TYPEOFSUBACTIVITY)
          .toString())) {

        Vector fileListVector = new Vector();
        if (this.getActivityFilesList() != null) {
          String[] fileList = this.getActivityFilesList();
          int sizeOfListBox = fileList.length;
          for (int i = 0; i < sizeOfListBox; i++) {
            int indexOfHash = fileList[i].indexOf("#");
            String displayName = fileList[i].substring(indexOfHash + 1, fileList[i].length());
            DDNameValue fileListBox = new DDNameValue(fileList[i], displayName);
            fileListVector.add(fileListBox);
          } // end of for (int i=0;i<sizeOfListBox;i++)
          sessionForm.setActivityFilesListVec(fileListVector);
          this.setActivityFilesListVec(fileListVector);
        }// end of (this.getActivityFilesList() != null)

        sessionForm.setActivityFile(this.getActivityFile());
        sessionForm.setActivityFilesList(this.getActivityFilesList());
      }
      // initialize validation
      Validation validation = new Validation();

      validation.checkForRequired("error.activity.detail.title", sessionForm.getActivityTitle(),
          "error.application.required", errors);
      validation.checkForMaxlength("error.activity.detail.title", sessionForm.getActivityTitle(),
          "error.application.maxlength", errors, 255);

      if (sessionForm.getActivityRecurringFrequency() != null) {
        if (sessionForm.getActivityRecurringFrequency().equals("DAY")) {
          if (sessionForm.getActivityRecurringDailyDays() != null
              && sessionForm.getActivityRecurringDailyDays().equals("")) {
            validation.checkForRequired("label.activity.recurring.everyday", sessionForm
                .getActivityRecurringDailyDays(), "error.application.required", errors);
          }// end of if(sessionForm.getActivityRecurringDailyDays() != null &&
          // sessionForm.getActivityRecurringDailyDays().equals(""))
          if (sessionForm.getActivityRecurringDailyDays() != null
              && sessionForm.getActivityRecurringDailyDays().trim().equals("0")) {
            validation.checkForRequired("label.activity.recurring.everydayzero", sessionForm
                .getActivityRecurringDailyDays(), "error.application.required", errors);
          }// end of if(sessionForm.getActivityRecurringDailyDays() != null &&
          // sessionForm.getActivityRecurringDailyDays().trim().equals("0"))
        }// end of if
        // (sessionForm.getActivityRecurringFrequency().equals("DAY"))

        if (sessionForm.getActivityRecurringFrequency().equals("WEEK")) {
          if (sessionForm.getActivityRecurringWeeklyEvery() != null
              && sessionForm.getActivityRecurringWeeklyEvery().equals("")) {
            validation.checkForRequired("label.activity.recurring.everyday", sessionForm
                .getActivityRecurringWeeklyEvery(), "error.application.required", errors);
          }// end of if(sessionForm.getActivityRecurringWeeklyEvery() != null
          // && sessionForm.getActivityRecurringWeeklyEvery().equals(""))

          if (sessionForm.getActivityRecurringWeeklyEvery() != null
              && sessionForm.getActivityRecurringWeeklyEvery().trim().equals("0")) {
            validation.checkForRequired("label.activity.recurring.everydayzero", sessionForm
                .getActivityRecurringWeeklyEvery(), "error.application.required", errors);
          }// end of if(sessionForm.getActivityRecurringWeeklyEvery() != null
          // &&
          // sessionForm.getActivityRecurringWeeklyEvery().trim().equals("0"))
        }// end of if
        // (sessionForm.getActivityRecurringFrequency().equals("WEEK"))

        if (sessionForm.getActivityRecurringFrequency().equals("MONTH")) {
          if (sessionForm.getActivityRecurringMonthlyEvery().equals("0")) {
            if (sessionForm.getActivityRecurringMonthlyEveryDay() != null
                && sessionForm.getActivityRecurringMonthlyEveryDay().equals("")) {
              validation.checkForRequired("label.activity.recurring.everymonth", sessionForm
                  .getActivityRecurringMonthlyEveryDay(), "error.application.required", errors);
            }// end of if(sessionForm.getActivityRecurringMonthlyEveryDay() !=
            // null &&
            // sessionForm.getActivityRecurringMonthlyEveryDay().equals(""))
            if (sessionForm.getActivityRecurringMonthlyEveryDay() != null
                && sessionForm.getActivityRecurringMonthlyEveryDay().trim().equals("0")) {
              validation.checkForRequired("label.activity.recurring.everymonthzero", sessionForm
                  .getActivityRecurringMonthlyEveryDay(), "error.application.required", errors);
            }// end of if(sessionForm.getActivityRecurringMonthlyEveryDay() !=
            // null &&
            // sessionForm.getActivityRecurringMonthlyEveryDay().trim().equals("0"))
          }// end of if
          // (sessionForm.getActivityRecurringMonthlyEvery().equals("0"))
        }// end of if
        // (sessionForm.getActivityRecurringFrequency().equals("MONTH"))

        if (sessionForm.getActivityRecurringFrequency().equals("YEAR")) {
          if (sessionForm.getActivityRecurringYearlyEvery().equals("0")) {
            if (sessionForm.getActivityRecurringYearlyEveryDay() != null
                && sessionForm.getActivityRecurringYearlyEveryDay().equals("")) {
              validation.checkForRequired("label.activity.recurring.everymonth", sessionForm
                  .getActivityRecurringYearlyEveryDay(), "error.application.required", errors);
            }// end of if(sessionForm.getActivityRecurringYearlyEveryDay() !=
            // null &&
            // sessionForm.getActivityRecurringYearlyEveryDay().equals(""))

            if (sessionForm.getActivityRecurringYearlyEveryDay() != null
                && sessionForm.getActivityRecurringYearlyEveryDay().trim().equals("0")) {
              validation.checkForRequired("label.activity.recurring.everymonthzero", sessionForm
                  .getActivityRecurringYearlyEveryDay(), "error.application.required", errors);
            }// end of if(sessionForm.getActivityRecurringYearlyEveryDay() !=
            // null &&
            // sessionForm.getActivityRecurringYearlyEveryDay().trim().equals("0"))
          }// end of if
          // (sessionForm.getActivityRecurringYearlyEvery().equals("0"))
        }// end of if
        // (sessionForm.getActivityRecurringFrequency().equals("YEAR"))

      }// end of if (sessionForm.getActivityRecurringFrequency() != null)

      // if user have entered any data, then check for valid date
      if (sessionForm.getActivityStartDate() != null
          && sessionForm.getActivityStartDate().length() != 0) {
        validation.validateDateString("label.activity.detail.startdate", sessionForm
            .getActivityStartDate(), sessionForm.getLocale(), "error.application.date", errors);
      }
      if (sessionForm.getActivityStartTime() != null
          && sessionForm.getActivityStartTime().length() != 0) {
        validation.checkForTime("label.activity.detail.starttime", sessionForm
            .getActivityStartTime(), "error.application.time", errors, sessionForm.getLocale());
      }
      if (sessionForm.getActivityEndDate() != null
          && sessionForm.getActivityEndDate().length() != 0) {
        validation.validateDateString("label.activity.detail.enddate", sessionForm
            .getActivityEndDate(), sessionForm.getLocale(), "error.application.date", errors);
      }
      if (sessionForm.getActivityEndTime() != null && sessionForm.getActivityEndTime().length() != 0) {
        validation.checkForTime("label.activity.detail.endtime", sessionForm.getActivityEndTime(),
            "error.application.time", errors, sessionForm.getLocale());
      }
      // TODO verify END date/time is after Start date/time
      // remind date time
      if (sessionForm.getActivityReminder() != null
          && sessionForm.getActivityReminder().equals("ALERT")) {

        if (sessionForm.getActivityRemindDate() != null && sessionForm.getActivityRemindDate()
            .length() != 0) {
          validation.validateDateString("label.activity.detailremind.startdate", sessionForm
              .getActivityRemindDate(), sessionForm.getLocale(), "error.application.date", errors);
        }
        if (sessionForm.getActivityReminderTime() != null && sessionForm
                .getActivityReminderTime().length() != 0) {
          validation.checkForTime("label.activity.detailremind.starttime", sessionForm
              .getActivityReminderTime(), "error.application.time", errors, sessionForm.getLocale());
        }
      }// end of if (sessionForm.getActivityReminder() != null && ...)

      // check recur date/time validity
      if (sessionForm.getActivityRecurStartDate() != null && sessionForm
          .getActivityRecurStartDate().length() != 0) {
        validation.validateDateString("label.activity.recurring.startdate", sessionForm
            .getActivityRecurStartDate(), sessionForm.getLocale(), "error.application.date", errors);
      }

      if (sessionForm.getActivityRecurEndDate() != null && sessionForm
          .getActivityRecurEndDate().length() != 0) {
        validation.validateDateString("label.activity.recurring.enddate", sessionForm
            .getActivityRecurEndDate(), sessionForm.getLocale(), "error.application.date", errors);
      }
      // TODO verify recurring END date/time is after Start date/time
    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    } finally {
      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, request
          .getParameter(ConstantKeys.TYPEOFACTIVITY));
      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, request
          .getParameter(ConstantKeys.TYPEOFSUBACTIVITY));
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, request
          .getParameter(ConstantKeys.TYPEOFOPERATION));
    }
    return errors;
  } // end of validate (ActionMapping mapping, HttpServletRequest request)

  public String toString()
  {
    StringBuffer sb = new StringBuffer("activity form = \n");
    sb.append("  activityType = [" + activityType + "]\n");
    sb.append("  activityTypeOfOperation = [" + activityTypeOfOperation + "]\n");
    sb.append("  activityAction = [" + activityAction + "]\n");
    sb.append("  activityID = [" + activityID + "]\n");
    sb.append("  activityTitle = [" + activityTitle + "]\n");
    sb.append("  activityDetail = [" + activityDetail + "]\n");
    sb.append("  activityEntityID = [" + activityEntityID + "]\n");
    sb.append("  activityEntityName = [" + activityEntityName + "]\n");
    sb.append("  activityIndividualID = [" + activityIndividualID + "]\n");
    sb.append("  activityIndividualName = [" + activityIndividualName + "]\n");
    sb.append("  activityRelatedFieldID = [" + activityRelatedFieldID + "]\n");
    sb.append("  activityRelatedFieldValue = [" + activityRelatedFieldValue + "]\n");
    sb.append("  activityRelatedTypeID = [" + activityRelatedTypeID + "]\n");
    sb.append("  activityRelatedTypeValue = [" + activityRelatedTypeValue + "]\n");
    sb.append("  activityOwnerID = [" + activityOwnerID + "]\n");
    sb.append("  activityOwnerName = [" + activityOwnerName + "]\n");
    sb.append("  activityStartDate = [" + activityStartDate + "]\n");
    sb.append("  activityStartTime = [" + activityStartTime + "]\n");
    sb.append("  activityEndDate = [" + activityEndDate + "]\n");
    sb.append("  activityEndTime = [" + activityEndTime + "]\n");
    sb.append("  activityPriority = [" + activityPriority + "]\n");
    sb.append("  activityNotes = [" + activityNotes + "]\n");
    sb.append("  activityStatus = [" + activityStatus + "]\n");
    sb.append("  activityVisibility = [" + activityVisibility + "]\n");
    sb.append("  activityCallType = [" + activityCallType + "]\n");
    sb.append("  activityReminder = [" + activityReminder + "]\n");
    sb.append("  activityRemindDate = [" + activityRemindDate + "]\n");
    sb.append("  activityReminderTime = [" + activityReminderTime + "]\n");
    sb.append("  activityEmailInvitation = [" + activityEmailInvitation + "]\n");
    sb.append("  selStartTime = [" + selStartTime + "]\n");
    sb.append("  selEndTime = [" + selEndTime + "]\n");
    sb.append("  selRemindTime = [" + selRemindTime + "]\n");
    sb.append("  activityAttendeesType = [" + activityAttendeesType + "]\n");
    sb.append("  activityAttendeesAll = [" + activityAttendeesAll + "]\n");
    sb.append("  activityAttendeesOptional = [" + activityAttendeesOptional + "]\n");
    sb.append("  activityAttendeesRequired = [" + activityAttendeesRequired + "]\n");
    sb.append("  activityAttendeeSearch = [" + activityAttendeeSearch + "]\n");
    sb.append("  activityAttendeeOptionalVector = [" + activityAttendeeOptionalVector + "]\n");
    sb.append("  activityAttendeeRequiredVector = [" + activityAttendeeRequiredVector + "]\n");
    sb.append("  activityResourceAll = [" + activityResourceAll + "]\n");
    sb.append("  activityResourceSelected = [" + activityResourceSelected + "]\n");
    sb.append("  activityResourcevector = [" + activityResourcevector + "]\n");
    sb.append("  activityAvailabilityStartMonth = [" + activityAvailabilityStartMonth + "]\n");
    sb.append("  activityAvailabilityStartDay = [" + activityAvailabilityStartDay + "]\n");
    sb.append("  activityAvailabilityStartYear = [" + activityAvailabilityStartYear + "]\n");
    sb.append("  activityAvailabilityStartTime = [" + activityAvailabilityStartTime + "]\n");
    sb.append("  activityAvailabilityEndMonth = [" + activityAvailabilityEndMonth + "]\n");
    sb.append("  activityAvailabilityEndDay = [" + activityAvailabilityEndDay + "]\n");
    sb.append("  activityAvailabilityEndYear = [" + activityAvailabilityEndYear + "]\n");
    sb.append("  activityAvailabilityEndTime = [" + activityAvailabilityEndTime + "]\n");
    sb.append("  activityRecurringStartTime = [" + activityRecurringStartTime + "]\n");
    sb.append("  activityRecurStartDate = [" + activityRecurStartDate+ "]\n");
    sb.append("  activityRecurEndDate = [" + activityRecurEndDate + "]\n");
    sb.append("  activityRecurringEveryDays = [" + activityRecurringEveryDays + "]\n");
    sb.append("  activityFindWeek = [" + activityFindWeek + "]\n");
    sb.append("  activitySelectedWeek = [" + activitySelectedWeek + "]\n");
    sb.append("  activityRecurringFrequency = [" + activityRecurringFrequency + "]\n");
    sb.append("  activityRecurringDailyEvery = [" + activityRecurringDailyEvery + "]\n");
    sb.append("  activityRecurringDailyDays = [" + activityRecurringDailyDays + "]\n");
    sb.append("  activityRecurringDailyWeekdays = [" + activityRecurringDailyWeekdays + "]\n");
    sb.append("  activityRecurringWeeklyEvery = [" + activityRecurringWeeklyEvery + "]\n");
    sb.append("  activityRecurringWeeklyOn = [" + activityRecurringWeeklyOn + "]\n");
    sb.append("  activityRecurringMonthlyEvery = [" + activityRecurringMonthlyEvery + "]\n");
    sb.append("  activityRecurringMonthlyEveryDay = [" + activityRecurringMonthlyEveryDay + "]\n");
    sb.append("  activityRecurringMonthlyOnDay = [" + activityRecurringMonthlyOnDay + "]\n");
    sb.append("  activityRecurringMonthlyOnWeek = [" + activityRecurringMonthlyOnWeek + "]\n");
    sb.append("  activityRecurringYearlyEvery = [" + activityRecurringYearlyEvery + "]\n");
    sb
        .append("  activityRecurringYearlyEveryMonth = [" + activityRecurringYearlyEveryMonth
            + "]\n");
    sb.append("  activityRecurringYearlyEveryDay = [" + activityRecurringYearlyEveryDay + "]\n");
    sb.append("  activityRecurringYearlyOnDay = [" + activityRecurringYearlyOnDay + "]\n");
    sb.append("  activityRecurringYearlyOnWeek = [" + activityRecurringYearlyOnWeek + "]\n");
    sb.append("  activityRecurringYearlyOnMonth = [" + activityRecurringYearlyOnMonth + "]\n");
    sb.append("  activityAttachedCentraviewFile = [" + activityAttachedCentraviewFile + "]\n");
    sb.append("  activityAttachedFileID = [" + activityAttachedFileID + "]\n");
    sb.append("  activityAttachedFileName = [" + activityAttachedFileName + "]\n");
    sb.append("  activityFilesList = [" + activityFilesList + "]\n");
    sb.append("  activityFilesListVec = [" + activityFilesListVec + "]\n");
    sb.append("  linkCompany = [" + linkCompany + "]\n");
    sb.append("  linkContact = [" + linkContact + "]\n");
    return (sb.toString());
  } // end toString()

}// end of //end of ActivityForm class

