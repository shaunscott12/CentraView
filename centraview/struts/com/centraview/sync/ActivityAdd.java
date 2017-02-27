/*
 * $RCSfile: ActivityAdd.java,v $    $Revision: 1.3 $  $Date: 2005/09/20 20:22:23 $ - $Author: mcallist $
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

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.activity.ActivityForm;
import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;
import com.centraview.syncfacade.SyncFacadeHome;

/**
 * This class is for adding new Acitivity through the CompanionLink Sync API.
 */
public class ActivityAdd extends Action {
  private static Logger logger = Logger.getLogger(ActivityAdd.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    final String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();
    // getting user object from session
    HttpSession session = request.getSession();
    UserObject userobjectd = (UserObject)session.getAttribute("userobject");

    // get the user's preference for sync'ing as private or default privileges
    UserPrefererences userPrefs = userobjectd.getUserPref();
    TimeZone tz = TimeZone.getTimeZone(userPrefs.getTimeZone());
    Locale locale = request.getLocale();
    String prefValue = userPrefs.getSyncAsPrivate();
    boolean syncAsPrivate = (prefValue != null && prefValue.equals("YES")) ? true : false;

    // date format for incoming dates: 2004-01-13 15:00:00
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // date format for writing to the form should be in the format based on
    // locale
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

    ActivityAddHandler requestForm = (ActivityAddHandler)form;
    String sessionID = requestForm.getSessionID();

    // check sessionid is matching or not
    if (userobjectd.getSessionID().equals(sessionID)) {
      ActivityForm activityForm = new ActivityForm();
      activityForm.setLocale(locale);
      String priority = requestForm.getPriority();

      // this is the string that we'll pass to the activityform
      // set to 2 (medium) by default
      String detailpriority = "2";

      if (priority != null) {
        // set the String integer value based on the String string value
        // (confused yet?)
        if (priority.equals("High")) {
          detailpriority = "1";
        } else if (priority.equals("Low")) {
          detailpriority = "3";
        }
      }

      String detailstatus = requestForm.getStatus();

      if (detailstatus == null) {
        detailstatus = "1";
      }

      if (detailstatus != null && !detailstatus.equals("")) {
        if (detailstatus.equals("Pending")) {
          detailstatus = "1";
        } else if (detailstatus.equals("Completed")) {
          detailstatus = "2";
        }
      }

      String detailtitle = requestForm.getTitle();
      String detaildetail = requestForm.getDescription();

      // startdate
      Calendar start = new GregorianCalendar(tz, locale);
      String activityStartDate = requestForm.getStartDateTime();
      if (activityStartDate != null && (!activityStartDate.equals(""))) {
        try {
          Date dd = simpleDateFormat.parse(activityStartDate);
          start.setTime(dd);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }
      activityForm.setActivityStartDate(df.format(start.getTime()));
      activityForm.setActivityStartTime(tf.format(start.getTime()));

      // enddate
      Calendar end = new GregorianCalendar(tz, locale);
      String activityenddate = requestForm.getEndDateTime();
      if (activityenddate != null && (!activityenddate.equals(""))) {
        try {
          Date dd = simpleDateFormat.parse(activityenddate);
          end.setTime(dd);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }
      activityForm.setActivityEndDate(df.format(end.getTime()));
      activityForm.setActivityEndTime(tf.format(end.getTime()));

      activityForm.setActivityPriority(detailpriority);
      activityForm.setActivityStatus(detailstatus);
      activityForm.setActivityTitle(detailtitle);
      activityForm.setActivityDetail(detaildetail);

      // alarm date time
      String alarmDateTime = requestForm.getAlarmDateTime(); // alarmDateTime
      // ="01/09/2003";
      if (alarmDateTime != null && (!alarmDateTime.equals(""))) {
        try {
          Date d = simpleDateFormat.parse(alarmDateTime);
          GregorianCalendar remind = new GregorianCalendar();
          remind.setTime(d);
          activityForm.setActivityRemindDate(df.format(remind.getTime()));
          activityForm.setActivityReminder("on");
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }

      // now, add the Activity to the database using our SyncFacade bean
      String linkCompany = requestForm.getLinkCompany();
      if (linkCompany == null) {
        activityForm.setLinkCompany(linkCompany);
      }

      // create an instance of our SyncFacade EJB
      SyncFacade syncfacade = new SyncFacade();
      syncfacade.setDataSource(dataSource);

      // now, add the Activity to the database using our SyncFacade bean
      String activitytype = requestForm.getType();
      if (activitytype == null) {
        activitytype = "Appointment";
      }
      String result = "";

      int individualID = userobjectd.getIndividualID();
      // Appointment
      if (activitytype.equals("Appointment")) {
        activityForm.setActivityType("1");
      }

      // Call
      if (activitytype.equals("Call")) {
        activityForm.setActivityType("2");
      }

      // Meeting
      if (activitytype.equals("Meeting")) {
        activityForm.setActivityType("5");
      }

      // ToDo
      if (activitytype.equals("To Do")) {
        activityForm.setActivityType("6");
      }

      // NextAction
      if (activitytype.equals("Next Action")) {
        activityForm.setActivityType("7");
      }

      result = syncfacade.addActivity(activityForm, individualID);

      com.centraview.syncfacade.SyncFacade sfremote = null;
      try {
        SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject(
            "com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
        sfremote = syncHome.create();
        sfremote.setDataSource(dataSource);
      } catch (Exception e) {
        logger.error("[execute]: Exception", e);
        writer.print("FAIL");
        return (null);
      }

      // Check to see if the user's preference is to create sync'ed
      // records as private. If so, delete all records from recordauthorisation
      // and publicrecords tables that link to the newly created records.
      if (syncAsPrivate) {
        ArrayList recordIDs = new ArrayList();
        try {
          recordIDs.add(new Integer(result));
        } catch (NumberFormatException nfe) {
          // don't need to do anything, because we obviously didn't add an
          // activity successfully.
          System.out.println("\n\n\nCAUGHT A NumberFormatException!!!!\n\n\n");
        }
        sfremote.markRecordsPrivate(3, recordIDs);
      }

      // we need to make all the Activities lists dirty, so that the next time
      // they are viewed, they are refreshed and contain the record we just
      // added
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("MultiActivity");
      lg.makeListDirty("AllActivity");
      lg.makeListDirty("Appointment");
      lg.makeListDirty("Call");
      lg.makeListDirty("Meeting");
      lg.makeListDirty("NextAction");
      lg.makeListDirty("ToDo");

      // Now we check to see if this is a recurring activity.
      // Check the "recurrenceType" field. If it is not null,
      // then process the other recurring fields "every" and "on".
      // Then call the SyncFacadeEJB to add the recurring data
      // to the recurrence table.
      String recurrenceType = requestForm.getRecurrenceType();

      if (result != null && !result.equals("")) {
        // only do this if we actually created an activity above
        if (recurrenceType != null && !recurrenceType.equals("")) {
          // this is a recurring activity, get the other recurring fields
          String every = requestForm.getEvery();
          String recurrOn = requestForm.getOn();

          // NOTE: we do not care about recurringStartDate anymore, only copy
          // startDateTime into recurringStartDate
          String recurringStartDateString = requestForm.getStartDateTime();
          String recurringEndDateString = requestForm.getRecurrenceEndDate();

          Date recurringStartDate = null;
          Date recurringEndDate = null;

          try {
            recurringStartDate = simpleDateFormat.parse(recurringStartDateString);
            if (recurringEndDateString != null) {
              recurringEndDate = simpleDateFormat.parse(recurringEndDateString);
            } else {
              recurringEndDate = simpleDateFormat.parse("2099-12-31 00:00:00");
            }

            // NOTE: There is a bug in the CompanionLink Client API, that is
            // sending us the day of the month in the "every" field, instead of
            // the month of the year; when the recurring type is Yearly. To fix
            // this issue, we now get the month of the year from the start date,
            // and set every equal to that value. When CompanionLink addresses
            // this issue correctly, we can remove this hack.
            if (recurrenceType.equals("YEAR")) {
              Calendar recurStart = new GregorianCalendar(tz, locale);
              recurStart.setTime(recurringStartDate);
              every = String.valueOf(recurStart.get(Calendar.MONTH));
            }
            // END HACK for yearly bug from CompanionLink

            // next line returns boolean, but there's no real reason to check.
            // (since we can't ROLLBACK)
            sfremote.setRecurringFields(new Integer(result).intValue(), recurrenceType,
                new Integer(every).intValue(), new Integer(recurrOn).intValue(),
                recurringStartDate, recurringEndDate);
          } catch (Exception de) {
            logger.error("[Exception][(Sync)ActivityAdd]", de);
          }
        }
      }

      writer.print(result);
    } else {
      logger.error("Broken Session on sync.");
      writer.print("FAIL");
    }
    return (null);
  } // end execute() method
} // end class ActivityAdd definition

