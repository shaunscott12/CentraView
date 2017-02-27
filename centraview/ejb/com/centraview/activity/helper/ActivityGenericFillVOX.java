/*
 * $RCSfile: ActivityGenericFillVOX.java,v $    $Revision: 1.10 $  $Date: 2005/10/24 21:18:37 $ - $Author: mcallist $
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

package com.centraview.activity.helper;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.centraview.activity.ActivityForm;
import com.centraview.activity.ConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.file.CvFileVO;

public class ActivityGenericFillVOX {
  private static Logger logger = Logger.getLogger(ActivityGenericFillVOX.class);

  /**
   * Generates an ActivityForm object from a give ActivityVO object.
   * @param activity The source ActitivityVO object from which we will create
   *          the ActivityForm object
   * @param activityForm The newly created ActivityForm object which we are
   *          returning.
   * @return ActivityForm
   */
  public ActivityForm fillBasicForm(ActivityVO activity, ActivityForm activityForm)
  {
    try {
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, activityForm.getLocale());
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, activityForm.getLocale());

      activityForm.setActivityType(String.valueOf(activity.getActivityType()));
      activityForm.setActivityID(String.valueOf(activity.getActivityID()));
      activityForm.setActivityTitle(activity.getTitle());
      activityForm.setActivityPriority(String.valueOf(activity.getPriority()));

      // Set the RelatedField Information:
      activityForm.setActivityRelatedFieldID(Integer.toString(activity.getRelatedFieldID()));
      activityForm.setActivityRelatedFieldValue(activity.getRelatedFieldValue());
      activityForm.setActivityRelatedTypeID(Integer.toString(activity.getRelatedTypeID()));
      activityForm.setActivityRelatedTypeValue(activity.getRelatedTypeValue());

      if (activity.getNotes() != null) {
        activityForm.setActivityNotes(String.valueOf(activity.getNotes()));
      } else {
        activityForm.setActivityNotes("");
      }

      activityForm.setActivityCallType(String.valueOf(activity.getCallTypeId()));
      activityForm.setActivityID(String.valueOf(activity.getActivityID()));

      if (activity.getActivityDetails() != null) {
        activityForm.setActivityDetail(activity.getActivityDetails());
      }

      if (activity.getActivityStartDate() != null) {
        Timestamp startTimeStamp = activity.getActivityStartDate();
        Calendar start = new GregorianCalendar();
        start.setTime(startTimeStamp);
        activityForm.setActivityStartDate(df.format(start.getTime()));
        activityForm.setActivityStartTime(tf.format(start.getTime()));
      }

      if (activity.getActivityEndDate() != null) {
        Timestamp endTimeStamp = activity.getActivityEndDate();
        Calendar end = new GregorianCalendar();
        end.setTime(endTimeStamp);
        activityForm.setActivityEndDate(df.format(end.getTime()));
        activityForm.setActivityEndTime(tf.format(end.getTime()));
      }

      RecurrenceVO recurrenceVO = null;
      if (activity.getRecurrence() != null) {
        recurrenceVO = activity.getRecurrence();
        activityForm.setActivityRecurringFrequency(recurrenceVO.getTimePeriod());

        // if date is not null convert from timestamp to String
        if (recurrenceVO.getStartDate() != null) {
          Date recurStartDate = recurrenceVO.getStartDate();
          Calendar recurStart = new GregorianCalendar();
          recurStart.setTime(recurStartDate);
          activityForm.setActivityRecurStartDate(df.format(recurStart.getTime()));
          activityForm.setActivityRecurringStartTime(tf.format(recurStart.getTime()));
        }

        if (recurrenceVO.getUntil() != null) {
          Date recurEndDate = recurrenceVO.getUntil();
          Calendar recurEnd = new GregorianCalendar();
          recurEnd.setTime(recurEndDate);
          activityForm.setActivityRecurEndDate(df.format(recurEnd.getTime()));
        }

        HashMap mapRecurrence = recurrenceVO.getRecurrenceHM();
        if (mapRecurrence != null) {
          if (mapRecurrence.size() > 0) {
            String frequency = (String)mapRecurrence.get("FREQUENCY");
            String every = (String)mapRecurrence.get("EVERY");
            String on = (String)mapRecurrence.get("ON");
            activityForm.setActivityRecurringFrequency(frequency);

            if (frequency.equals("DAY")) {
              activityForm.setActivityRecurringDailyEvery("1");
              activityForm.setActivityRecurringDailyDays(every);
              if (Integer.parseInt(on) > 0) {
                activityForm.setActivityRecurringDailyWeekdays("1");
              }
            } else if (frequency.equals("WEEK")) {
              activityForm.setActivityRecurringWeeklyEvery(every);
              StringTokenizer st = new StringTokenizer(on, ",");
              String strweeklyon[] = new String[st.countTokens()];
              int i = 0;

              while (st.hasMoreTokens()) {
                String strToken = st.nextToken();
                if (strToken.equals("6")) {
                  strToken = "Su";
                } else if (strToken.equals("0")) {
                  strToken = "Mo";
                } else if (strToken.equals("1")) {
                  strToken = "Tu";
                } else if (strToken.equals("2")) {
                  strToken = "We";
                } else if (strToken.equals("3")) {
                  strToken = "Th";
                } else if (strToken.equals("4")) {
                  strToken = "Fr";
                } else if (strToken.equals("5")) {
                  strToken = "Sa";
                }
                strweeklyon[i] = strToken.trim();
                i++;
              }
              String selectedweek[] = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
              activityForm.setActivityRecurringWeeklyOn(strweeklyon);
              activityForm.setActivitySelectedWeek(selectedweek);
              activityForm.setActivityFindWeek(strweeklyon);
            } else if (frequency.equals("MONTH")) {
              String onstring = on;
              int inton = Integer.parseInt(onstring.substring(0, 1));
              activityForm.setActivityRecurringMonthlyEvery("" + inton);
              if (inton == 0) {
                String monthday = onstring.substring(2, onstring.length());
                activityForm.setActivityRecurringMonthlyEveryDay(monthday);
                activityForm.setActivityRecurringMonthlyOnWeek("-1");
                activityForm.setActivityRecurringMonthlyOnDay("-1");
              }

              if (inton == 1) {
                activityForm.setActivityRecurringMonthlyOnDay(onstring.substring(2, 3));
                activityForm.setActivityRecurringMonthlyOnWeek(onstring.substring(4, 5));
              }
            } else if (frequency.equals("YEAR")) {
              String onstring = on;
              int inton = Integer.parseInt(onstring.substring(0, 1));
              activityForm.setActivityRecurringYearlyEvery("" + inton);
              if (inton == 0) {
                activityForm.setActivityRecurringYearlyEveryMonth(every);
                String yearday = onstring.substring(2, onstring.length());
                activityForm.setActivityRecurringYearlyEveryDay(yearday);
                activityForm.setActivityRecurringYearlyOnWeek("-1");
                activityForm.setActivityRecurringYearlyOnDay("-1");
              }

              if (inton == 1) {
                activityForm.setActivityRecurringYearlyOnMonth(every);
                activityForm.setActivityRecurringYearlyOnWeek(onstring.substring(2, 3));
                activityForm.setActivityRecurringYearlyOnDay(onstring.substring(4, 5));
              }
            }// end of else if (frequency.equals("YEAR"))
          }// end of if (mapRecurrence.size()>0)
        }// end of if (mapRecurrence != null)
      }// end of if (actVo.getRecurrence() != null)

      Collection activityAction = null;
      if (activity.getActivityAction() != null) {
        activityAction = activity.getActivityAction();
        Iterator it = activityAction.iterator();
        while (it.hasNext()) {
          ActivityActionVO acVO = (ActivityActionVO)it.next();
          String strActionType = acVO.getActionType();
          if (strActionType.equals(ActivityActionVO.AA_ALERT)) {
            activityForm.setActivityReminder("on");
            if (acVO.getActionTime() != null) {
              Timestamp remindTimeStamp = acVO.getActionTime();
              Calendar remind = new GregorianCalendar();
              remind.setTime(remindTimeStamp);
              activityForm.setActivityRemindDate(df.format(remind.getTime()));
              activityForm.setActivityReminderTime(tf.format(remind.getTime()));
            }// end of if (acVO.getActionTime() != null)
          }// end of if (strActionType.equals(ActivityActionVO.AA_ALERT))
          else if (strActionType.equals(ActivityActionVO.AA_EMAIL)) {
            activityForm.setActivityEmailInvitation("on");
          }// end of else if (strActionType.equals(ActivityActionVO.AA_EMAIL))
        }// end of while (it.hasNext())
      }// end of if (actVo.getActivityAction() != null)
      activityForm.setActivityStatus(String.valueOf(activity.getStatus()));
      activityForm.setActivityVisibility(activity.getVisibility());
      if (activity.getAttachmentVec() != null) {
        Vector attachment = new Vector();
        Vector attachmentFile = activity.getAttachmentVec();
        String ddAttachment[] = new String[attachmentFile.size()];
        Iterator it = attachmentFile.iterator();
        int k = 0;
        while (it.hasNext()) {
          CvFileVO fVO = (CvFileVO)it.next();
          String name = fVO.getTitle();
          DDNameValue ddattach = new DDNameValue("" + fVO.getFileId() + "#" + name, name);
          ddAttachment[k] = "" + fVO.getFileId() + "#" + name;
          attachment.add(ddattach);
          k++;
        }// end of while (it.hasNext())
        activityForm.setActivityFilesList(ddAttachment);
        activityForm.setActivityFilesListVec(attachment);
      }// end of if (actVo.getAttachmentVec() != null)
      if (activity.getOwnerID() != 0) {
        activityForm.setActivityOwnerName(activity.getOwnerName());
        activityForm.setActivityOwnerID("" + activity.getOwnerID());
      }// end of if(actVo.getOwnerID() != 0)
      if (activity.getIndividualID() != 0) {
        activityForm.setActivityIndividualName(activity.getIndividualName());
        activityForm.setActivityIndividualID(activity.getIndividualID() + "");
        activityForm.setActivityIndividualPrimaryContact(activity.getIndividualPrimaryContact());
      }// end of if(actVo.getIndividualID() != 0)
      if (activity.getEntityID() != 0) {
        activityForm.setActivityEntityName(activity.getEntityName());
        activityForm.setActivityEntityID(activity.getEntityID() + "");
      }// end of if(actVo.getEntityID() != 0)
      Collection attendee = null;
      Vector optional = new Vector();
      Vector required = new Vector();
      if (activity.getAttendee() != null) {
        attendee = activity.getAttendee();
        Iterator it = attendee.iterator();
        while (it.hasNext()) {
          AttendeeVO aVO = (AttendeeVO)it.next();
          String name = aVO.getFirstName() + "  " + aVO.getLastName();
          String tag = null;
          if (aVO.getAttendeeStatus() == 1) {
            tag = ConstantKeys.TAG_ACCEPTED;
          }// end of if (aVO.getAttendeeStatus()==1)
          else if (aVO.getAttendeeStatus() == 2) {
            tag = ConstantKeys.TAG_TENTATIVE;
          }// end of else if (aVO.getAttendeeStatus()==2)
          else if (aVO.getAttendeeStatus() == 3) {
            tag = ConstantKeys.TAG_DECLINED;
          }// end of else if (aVO.getAttendeeStatus()==3)
          DDNameValue ddoptional = new DDNameValue("" + aVO.getContactID() + "#" + name, name, tag);
          if ((aVO.getAttendeeType()).equals(AttendeeVO.AT_OPTIONAL))
            optional.add(ddoptional);
          else
            required.add(ddoptional);
        }// end of while (it.hasNext())
        if (optional.size() > 0) {
          String optionalAttendee[] = new String[optional.size()];
          for (int i = 0; i < optional.size(); i++) {
            DDNameValue ddOptional = (DDNameValue)optional.get(i);
            optionalAttendee[i] = ddOptional.getStrid();
          }// end of for( int i=0 ; i < optional.size() ; i++)
          activityForm.setActivityAttendeesOptional(optionalAttendee);
        }// end of if(optional.size() > 0)
        if (required.size() > 0) {
          String requiredAttendee[] = new String[required.size()];
          for (int i = 0; i < required.size(); i++) {
            DDNameValue ddRequired = (DDNameValue)required.get(i);
            requiredAttendee[i] = ddRequired.getStrid();
          }// end of for( int i=0 ; i < required.size() ; i++)
          activityForm.setActivityAttendeesRequired(requiredAttendee);
        }// end of if(required.size() > 0)
        activityForm.setActivityAttendeeOptionalVector(optional);
        activityForm.setActivityAttendeeRequiredVector(required);
      }// end of if (actVo.getAttendee() != null )
      Collection resource = null;
      Vector vecResource = new Vector();
      if (activity.getResource() != null) {
        resource = activity.getResource();
        Iterator it = resource.iterator();
        String ddResource[] = new String[resource.size()];
        int k = 0;
        while (it.hasNext()) {
          ResourceVO rVO = (ResourceVO)it.next();
          DDNameValue ddreq = new DDNameValue("" + rVO.getResourceId() + "#"
              + rVO.getResourceName(), rVO.getResourceName());
          ddResource[k] = "" + rVO.getResourceId() + "#" + rVO.getResourceName();
          vecResource.add(ddreq);
          k++;
        }// end of while (it.hasNext())
        activityForm.setActivityResourceSelected(ddResource);
        activityForm.setActivityResourcevector(vecResource);
      }// end of if (actVo.getResource() != null)
    } catch (Exception e) {
      logger.error("[fillBasicForm]: Exception", e);
    }
    return activityForm;
  } // public ActivityForm fillBasicForm(ActivityVO actVo, ActivityForm

  /**
   * Generates an ActivityVO object from a give activityForm object.
   * @param activityForm The source ActivityForm object from which we will
   *          create the ActivityVO object
   * @param activity The newly created ActitivityVO object
   * @return void
   */
  public void fillBasic(ActivityVO activity, ActivityForm activityForm)
  {
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, activityForm.getLocale());
    DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, activityForm.getLocale());

    String id = activityForm.getActivityID();
    if ((id != null) && (id.length() > 0)) {
      activity.setActivityID(Integer.parseInt(id));
    }
    String activityType = activityForm.getActivityType();
    if ((activityType != null) && (activityType.length() > 0)) {
      activity.setActivityType(Integer.parseInt(activityForm.getActivityType()));
    }
    activity.setTitle(activityForm.getActivityTitle());
    activity.setActivityDetails(activityForm.getActivityDetail());
    if ((activityForm.getActivityRelatedFieldID() != null)
        && (activityForm.getActivityRelatedFieldID().length() > 0)
        && (activityForm.getActivityRelatedTypeID() != null)
        && (activityForm.getActivityRelatedTypeID().length() > 0)) {
      activity.setRelatedFieldID(Integer.parseInt(activityForm.getActivityRelatedFieldID()));
      activity.setRelatedFieldValue(activityForm.getActivityRelatedFieldValue());
      activity.setRelatedTypeID(Integer.parseInt(activityForm.getActivityRelatedTypeID()));
      activity.setRelatedTypeValue(activityForm.getActivityRelatedTypeValue());
    }
    if ((activityForm.getActivityEntityID() != null)
        && (activityForm.getActivityEntityID().length() > 0)) {
      String entity = activityForm.getActivityEntityID();
      activity.setEntityID(Integer.parseInt(entity));
    }
    if ((activityForm.getActivityIndividualID() != null)
        && (activityForm.getActivityIndividualID().length() > 0)) {
      String individual = activityForm.getActivityIndividualID();
      activity.setIndividualID(Integer.parseInt(individual));
    }
    if ((activityForm.getActivityCallType() != null)
        && (activityForm.getActivityCallType().length() > 0)) {
      String callType = activityForm.getActivityCallType();
      activity.setCallTypeId(Integer.parseInt(callType));
    }

    if (CVUtility.notEmpty(activityForm.getActivityStartDate())) {
      Calendar start = new GregorianCalendar();
      Calendar startTime = new GregorianCalendar();
      try {
        logger.debug("dateformat: "+df.toString()+", lenient?: "+df.isLenient()+", locale: "+activityForm.getLocale());
        start.setTime(df.parse(activityForm.getActivityStartDate()));
        startTime.setTime(tf.parse(activityForm.getActivityStartTime()));
        logger.debug("parsed start date: "+df.format(start.getTime()));
        logger.debug("parsed start time: "+tf.format(startTime.getTime()));
      } catch (ParseException e) {
        logger.error("[fillBasic]: Can't parse start date or time", e);
      }
      start.set(Calendar.HOUR, startTime.get(Calendar.HOUR));
      start.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
      start.set(Calendar.AM_PM, startTime.get(Calendar.AM_PM));
      activity.setActivityStartDate(new Timestamp(start.getTimeInMillis()));
    }

    if (CVUtility.notEmpty(activityForm.getActivityEndDate())) {
      Calendar end = new GregorianCalendar();
      Calendar endTime = new GregorianCalendar();
      try {
        end.setTime(df.parse(activityForm.getActivityEndDate()));
        endTime.setTime(tf.parse(activityForm.getActivityEndTime()));
        logger.debug("parsed end date: "+df.format(end.getTime()));
        logger.debug("parsed end time: "+tf.format(endTime.getTime()));
      } catch (ParseException e) {
        logger.error("[fillBasic]: Can't parse end date or time", e);
      }
      end.set(Calendar.HOUR, endTime.get(Calendar.HOUR));
      end.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
      end.set(Calendar.AM_PM, endTime.get(Calendar.AM_PM));
      activity.setActivityEndDate(new Timestamp(end.getTimeInMillis()));
    }

    if ((activityForm.getActivityOwnerID() != null)
        && (activityForm.getActivityOwnerID().length() > 0)) {
      activity.setOwner(Integer.parseInt(activityForm.getActivityOwnerID()));
    }
    try {
      String tmpPriority = activityForm.getActivityPriority();
      if ((tmpPriority != null) && (!tmpPriority.equals(""))) {
        activity.setPriority(Integer.parseInt(tmpPriority));
      } else {
        activity.setPriority(2);
      }
    } catch (Exception e) {
      activity.setPriority(2);
    }
    if ((activityForm.getActivityNotes() != null) && (activityForm.getActivityNotes().length() > 0)) {
      activity.setNotes(activityForm.getActivityNotes());
    }
    try {
      String tmpStatus = activityForm.getActivityStatus();
      if ((tmpStatus != null) && (!tmpStatus.equals(""))) {
        activity.setStatus(Integer.parseInt(tmpStatus));
      } else {
        activity.setStatus(2);
      }
    } catch (Exception e) {
      activity.setStatus(2);
    }
    activity.setVisibility(activityForm.getActivityVisibility());
    String reminder = activityForm.getActivityReminder();
    if (activityForm.getActivityReminder() != null && reminder.equals("on")) {
      ActivityActionVO action = new ActivityActionVO();
      action.setActionType(ActivityActionVO.AA_ALERT);
      // FIXME externalize String
      action.setMessage("Scheduled Alert: " + activity.getTitle());
      action.setInterval(0);
      action.setRepeat(0);
      if (CVUtility.notEmpty(activityForm.getActivityRemindDate())
          && CVUtility.notEmpty(activityForm.getActivityReminderTime())) {
        Calendar remind = new GregorianCalendar();
        Calendar remindTime = new GregorianCalendar();
        try {
          remind.setTime(df.parse(activityForm.getActivityRemindDate()));
          remindTime.setTime(tf.parse(activityForm.getActivityReminderTime()));
        } catch (ParseException e) {
          logger.error("[fillBasic]: Exception", e);
        }
        remind.set(Calendar.HOUR, remindTime.get(Calendar.HOUR));
        remind.set(Calendar.MINUTE, remindTime.get(Calendar.MINUTE));
        remind.set(Calendar.AM_PM, remindTime.get(Calendar.AM_PM));
        action.setActionTime(new Timestamp(remind.getTimeInMillis()));
      }
      action.setRecipient(activity.getOwner());
      activity.setActivityAction(action);
    } // end if (activityForm.getActivityReminder() != null &&
    // reminder.equals("on"))
    String emailinvitation = activityForm.getActivityEmailInvitation();
    if (activityForm.getActivityEmailInvitation() != null && emailinvitation.equals("on")) {
      ActivityActionVO activityactionvo = new ActivityActionVO();
      activityactionvo.setActionType(ActivityActionVO.AA_EMAIL);
      activityactionvo.setMessage("Invitation : " + activity.getTitle());
      activityactionvo.setInterval(0);
      activityactionvo.setRepeat(0);
      String[] attendeerequired = activityForm.getActivityAttendeesRequired();
      String[] attendeeoptional = activityForm.getActivityAttendeesOptional();
      if (attendeerequired != null) {
        for (int i = 0; i < attendeerequired.length; i++) {
          try {
            int indexOfHash = attendeerequired[i].indexOf("#");
            String idName = attendeerequired[i].substring(0, indexOfHash);
            activityactionvo.setRecipient(Integer.parseInt(idName));
          }// end of try block
          catch (Exception e) {}// end of catch block
        }// end of for( int i=0 ; i < attendeerequired.length ; i ++ )
      }// end of if (attendeerequired != null)
      if (attendeeoptional != null) {
        for (int j = 0; j < attendeeoptional.length; j++) {
          try {
            int indexOfHash = attendeeoptional[j].indexOf("#");
            String idName = attendeeoptional[j].substring(0, indexOfHash);
            activityactionvo.setRecipient(Integer.parseInt(idName));
          } // end of try block
          catch (Exception e) {}// end of catch block
        }// end of for( int j=0 ; j < attendeeoptional.length ; j ++ )
      }// end of if (attendeeoptional != null)
      activity.setActivityAction(activityactionvo);
    }// end of if (activityForm.getActivityEmailInvitation() != null &&.......)
    String freq = activityForm.getActivityRecurringFrequency();
    if ((freq != null) && (freq.length() > 0)
        && (CVUtility.notEmpty(activityForm.getActivityRecurStartDate()))) {
      RecurrenceVO recur = new RecurrenceVO();

      Calendar recurStart = new GregorianCalendar();
      try {
        recurStart.setTime(df.parse(activityForm.getActivityRecurStartDate()));
      } catch (ParseException e) {
        logger.error("[fillBasic]: Exception", e);
      }
      recur.setStartDate(new Date(recurStart.getTimeInMillis()));

      if (CVUtility.notEmpty(activityForm.getActivityRecurEndDate())) {
        Calendar recurEnd = new GregorianCalendar();
        try {
          recurEnd.setTime(df.parse(activityForm.getActivityRecurEndDate()));
        } catch (ParseException e) {
          logger.error("[fillBasic]: Exception", e);
        }
        recur.setUntil(new Date(recurEnd.getTimeInMillis()));
      }
      recur.setTimePeriod(activityForm.getActivityRecurringFrequency());
      HashMap mapRecurrence = new HashMap();
      mapRecurrence.put("FREQUENCY", activityForm.getActivityRecurringFrequency());
      boolean isRecurr = false;
      mapRecurrence.put("IsRecurrence", new Boolean(isRecurr));
      if (CVUtility.notEmpty(activityForm.getActivityRecurStartDate())) {
        isRecurr = true;
        mapRecurrence.put("IsRecurrence", new Boolean(isRecurr));
        // if Recurringfrequency is DAY
        if (activityForm.getActivityRecurringFrequency().equals("DAY")) {
          if (activityForm.getActivityRecurringDailyEvery() != null) {
            if (activityForm.getActivityRecurringDailyEvery() != null)
              mapRecurrence.put("EVERY", activityForm.getActivityRecurringDailyDays());
          } else {
            mapRecurrence.put("ON", "0");
          }
          if (activityForm.getActivityRecurringDailyEvery() != null
              && activityForm.getActivityRecurringDailyDays().equals("1")) {
            if (activityForm.getActivityRecurringDailyWeekdays() != null
                && activityForm.getActivityRecurringDailyWeekdays().equals("1")) {
              mapRecurrence.put("ON", "0,1,2,3,4");
              activityForm.setActivityRecurringFrequency("WEEK");
              mapRecurrence.remove("FREQUENCY");
              recur.setTimePeriod("WEEK");
              mapRecurrence.put("FREQUENCY", "WEEK");
            }// END OF if (activityForm.getActivityRecurringDailyWeekdays()
            // ...)
            else {
              mapRecurrence.put("ON", "0");
            }
          } else {
            if (activityForm.getActivityRecurringDailyWeekdays() != null
                && activityForm.getActivityRecurringDailyWeekdays().equals("1")) {
              mapRecurrence.put("ON", activityForm.getActivityRecurringDailyWeekdays());
            } else {
              mapRecurrence.put("ON", "0");
            }
          }
        } else if (activityForm.getActivityRecurringFrequency().equals("WEEK")) {
          if (activityForm.getActivityRecurringWeeklyEvery() != null)
            mapRecurrence.put("EVERY", activityForm.getActivityRecurringWeeklyEvery());
          else
            mapRecurrence.put("EVERY", "0");
          String strWeekOn[] = activityForm.getActivityFindWeek();
          StringBuffer buffer = new StringBuffer();
          for (int i = 0; i < strWeekOn.length; i++) {
            String strweek = strWeekOn[i];
            if (strweek.equals("Su"))
              strweek = "6";
            else if (strweek.equals("Mo"))
              strweek = "0";
            else if (strweek.equals("Tu"))
              strweek = "1";
            else if (strweek.equals("We"))
              strweek = "2";
            else if (strweek.equals("Th"))
              strweek = "3";
            else if (strweek.equals("Fr"))
              strweek = "4";
            else if (strweek.equals("Sa"))
              strweek = "5";
            if (i == (strWeekOn.length - 1))
              buffer.append(strweek);
            else
              buffer.append(strweek + ",");
          }// END OF for (int i=0; i<strWeekOn.length;i++)
          if (activityForm.getActivityFindWeek() != null)
            mapRecurrence.put("ON", buffer.toString());
          else
            mapRecurrence.put("ON", "0");
        } else if (activityForm.getActivityRecurringFrequency().equals("MONTH")) {
          if (activityForm.getActivityRecurringMonthlyEvery() != null) {
            if (activityForm.getActivityRecurringMonthlyEvery().equals("0")) {
              String strday = activityForm.getActivityRecurringMonthlyEveryDay();
              int intday = Integer.parseInt(strday);
              if (intday <= 9)
                strday = "0" + strday;
              mapRecurrence.put("ON", "0," + strday);
              mapRecurrence.put("EVERY", strday);
            }// END OF if
            // (activityForm.getActivityRecurringMonthlyEvery().equals("0"))
            else {
              mapRecurrence.put("ON", "1," + activityForm.getActivityRecurringMonthlyOnDay() + ","
                  + activityForm.getActivityRecurringMonthlyOnWeek());
              mapRecurrence.put("EVERY", "0");
            }// END OF ELSE BLOCK
          }// END OF if (activityForm.getActivityRecurringMonthlyEvery() !=
          // null)
        } else if (activityForm.getActivityRecurringFrequency().equals("YEAR")) {
          if (activityForm.getActivityRecurringYearlyEvery() != null) {
            if (activityForm.getActivityRecurringYearlyEvery().equals("0")) {
              String strday = activityForm.getActivityRecurringYearlyEveryDay();
              int intday = Integer.parseInt(strday);
              if (intday <= 9)
                strday = "0" + strday;
              mapRecurrence.put("ON", "0," + strday);
              mapRecurrence.put("EVERY", activityForm.getActivityRecurringYearlyEveryMonth());
            } else {
              mapRecurrence.put("ON", "1," + activityForm.getActivityRecurringYearlyOnWeek() + ","
                  + activityForm.getActivityRecurringYearlyOnDay());
              mapRecurrence.put("EVERY", activityForm.getActivityRecurringYearlyOnMonth());
            }// END OF ELSE BLOCK
          }// END OF if (activityForm.getActivityRecurringYearlyEvery() !=
          // null)
        }// END OF else if
        // (activityForm.getActivityRecurringFrequency().equals("YEAR"))
      }// END OF if (activityForm.getActivityRecurringStartMonth() != null)
      recur.hashMapfromHandler(mapRecurrence);
      activity.setRecurrence(recur);
    } // END OF if ((freq != null) && (freq.length() > 0)
    // &&(activityForm.getActivityRecurringStartMonth() != null)...)
    String[] attendeeoptional = activityForm.getActivityAttendeesOptional();
    String[] attendeerequired = activityForm.getActivityAttendeesRequired();
    if (attendeerequired != null) {
      for (int i = 0; i < attendeerequired.length; i++) {
        AttendeeVO av = new AttendeeVO();
        try {
          int indexOfHash = attendeerequired[i].indexOf("#");
          String idName = attendeerequired[i].substring(0, indexOfHash);
          av.setContactID(Integer.parseInt(idName));
        } // END OF TRY BLOCK
        catch (Exception e) {}// END OF CATCH BLOCK
        av.setAttendeeType(AttendeeVO.AT_REQUIRED);
        activity.setAttendee(av);
      }// END FOR for( int i=0 ; i < attendeerequired.length ; i ++ )
    }// END OF if (attendeerequired != null)
    if (attendeeoptional != null) {
      for (int j = 0; j < attendeeoptional.length; j++) {
        AttendeeVO av1 = new AttendeeVO();
        try {
          int indexOfHash = attendeeoptional[j].indexOf("#");
          String idName = attendeeoptional[j].substring(0, indexOfHash);
          av1.setContactID(Integer.parseInt(idName));
        }// END OF TRY BLOCK
        catch (Exception e) {
          e.printStackTrace();
        }// END OF CATCH BLOCK
        av1.setAttendeeType(AttendeeVO.AT_OPTIONAL);
        activity.setAttendee(av1);
      }// END OF for( int j=0 ; j < attendeeoptional.length ; j ++ )
    }// END OF if (attendeeoptional != null)
    String[] atachmentFileId = activityForm.getActivityFilesList();
    if (atachmentFileId != null) {
      for (int i = 0; i < atachmentFileId.length; i++) {
        try {
          int indexOfHash = atachmentFileId[i].indexOf("#");
          String idName = atachmentFileId[i].substring(0, indexOfHash);
          activity.setAttachmentId(new Integer(idName));
        } catch (Exception e) {
          logger.error("[fillBasic]: Exception", e);
        }
      }// END OF for( int i=0 ; i < atachmentFileId.length ; i ++ )
    }// END OF if (atachmentFileId != null)
    try {
      activity.setCompletedDate(new Timestamp(System.currentTimeMillis()));
    } catch (Exception e) {
      logger.error("[fillBasic]: Exception", e);
    }
    String[] resourceselected = activityForm.getActivityResourceSelected();
    if (resourceselected != null) {
      for (int k = 0; k < resourceselected.length; k++) {
        try {
          int indexOfHash = resourceselected[k].indexOf("#");
          String idName = resourceselected[k].substring(0, indexOfHash);
          activity.setResource(new ResourceVO(Integer.parseInt(idName)));
        } catch (Exception e) {
          logger.error("[fillBasic]: Exception", e);
        }
      }// end of for( int k=0 ; k < resourceselected.length ; k ++ )
    }// END OF if (resourceselected != null)
  }// END OF public void fillBasic(ActivityVO actVo, ActivityForm activityForm)
}
