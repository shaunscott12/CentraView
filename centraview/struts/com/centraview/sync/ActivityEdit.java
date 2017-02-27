/*
 * $RCSfile: ActivityEdit.java,v $    $Revision: 1.4 $  $Date: 2005/09/20 20:22:23 $ - $Author: mcallist $
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

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
import com.centraview.activity.helper.ActivityGenericFillVOX;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;
import com.centraview.syncfacade.SyncFacadeHome;

/**
 * Handles the http request for updating an Activity record via the
 * CompanionLink Sync API.
 * @author CentraView, LLC.
 */
public class ActivityEdit extends Action {
  private static Logger logger = Logger.getLogger(ActivityEdit.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // we print directly to the browser, so we need to set the content type
    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();
    String myOutput = new String();

    ActivityEditHandler requestForm = (ActivityEditHandler)form;

    String sessionID = requestForm.getSessionID();
    HttpSession session = request.getSession();
    UserObject userobjectd = (UserObject)session.getAttribute("userobject");
    UserPrefererences userPrefs = userobjectd.getUserPref();
    TimeZone tz = TimeZone.getTimeZone(userPrefs.getTimeZone());
    Locale locale = request.getLocale();
    int individualID = userobjectd.getIndividualID();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // date format for writing to the form should be in the format based on
    // locale
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

    int activitytype = 0;

    int activityid = Integer.parseInt(requestForm.getActivityID());
    String result = "";

    // created session facade to call EJB methods (SyncFacade is a local class,
    // not an EJB!)
    SyncFacade syncfacade = new SyncFacade();
    syncfacade.setDataSource(dataSource);

    // if session is correct
    if (userobjectd.getSessionID().equals(sessionID)) {
      // A cheap way to check for NullPointerExceptions.
      try {
        ActivityForm activityForm = new ActivityForm();
        activityForm.setLocale(locale);
        activityForm.setActivityID(Integer.toString(activityid));

        String detailpriority = requestForm.getPriority();
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

        // call to getting activity details
        ActivityVO vo = null;
        activitytype = syncfacade.getActivityType(activityid);
        vo = syncfacade.getActivity(activityid, individualID);

        ActivityGenericFillVOX agf = new ActivityGenericFillVOX();
        activityForm.setLocale(request.getLocale());
        agf.fillBasicForm(vo, activityForm);

        // if user wants to edit any field , that will not be null , so
        // that field is set to ActivityForm

        if (detailpriority != null) {
          activityForm.setActivityPriority(detailpriority);
        }

        if (detailstatus != null) {
          activityForm.setActivityStatus(detailstatus);
        }

        if (detailtitle != null) {
          activityForm.setActivityTitle(detailtitle);
        }

        if (detaildetail != null) {
          activityForm.setActivityDetail(detaildetail);
        }

        // startdate
        String activitystartdate = requestForm.getStartDateTime();
        Calendar start = new GregorianCalendar(tz, locale);
        if (activitystartdate != null) {
          try {
            Date dd = simpleDateFormat.parse(activitystartdate);
            start.setTime(dd);
          } catch (Exception e) {
            logger.error("[execute]: Exception", e);
          }
        }
        activityForm.setActivityStartDate(df.format(start.getTime()));
        activityForm.setActivityStartTime(tf.format(start.getTime()));

        String activityenddate = requestForm.getEndDateTime();
        Calendar end = new GregorianCalendar(tz, locale);
        if (activityenddate != null) {
          try {
            Date dd = simpleDateFormat.parse(activityenddate);
            end.setTime(dd);
          } catch (Exception e) {
            logger.error("[execute]: Exception", e);
          }
        }
        activityForm.setActivityEndDate(df.format(end.getTime()));
        activityForm.setActivityEndTime(tf.format(end.getTime()));

        // alarm date time
        String alarmDateTime = requestForm.getAlarmDateTime();
        Calendar remind = new GregorianCalendar(tz, locale);
        // if alarmDateTime is not null , so set it to ActivityForm
        if (alarmDateTime != null) {
          try {
            Date d = simpleDateFormat.parse(alarmDateTime);
            remind.setTime(d);
          } catch (Exception e) {
            logger.error("[execute]: Exception", e);
          }
        }
        activityForm.setActivityRemindDate(df.format(remind));
        activityForm.setActivityReminderTime(tf.format(remind));

        activityForm.setActivityType(String.valueOf(activitytype));

        result = syncfacade.updateActivity(activityForm, individualID);

        String recurrenceType = requestForm.getRecurrenceType();

        if (recurrenceType != null && !recurrenceType.equals("")) {
          // This is a recurring activity - update the recurring info
          int every = requestForm.getEvery();
          int recurrOn = requestForm.getOn();
          String recurringStartDateString = requestForm.getStartDateTime();
          String recurringEndDateString = requestForm.getRecurrenceEndDate();

          Date recurringStartDate = null;
          Date recurringEndDate = null;

          try {
            recurringStartDate = simpleDateFormat.parse(recurringStartDateString);
          } catch (Exception dateException) {
            recurringStartDate = null;
          }

          try {
            recurringEndDate = simpleDateFormat.parse(recurringEndDateString);
          } catch (Exception dateException) {
            recurringEndDate = null;
          }

          try {
            SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject(
                "com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
            com.centraview.syncfacade.SyncFacade sfremote = syncHome.create();
            sfremote.setDataSource(dataSource);

            boolean updateResult = sfremote.updateRecurringFields(activityid, recurrenceType,
                every, recurrOn, recurringStartDate, recurringEndDate);

            if (updateResult != true) {
              writer.print("FAIL");
              return (null);
            }
          } catch (Exception de) {
            logger.error("[execute]: Exception", de);
          }
        } // end if (recurrenceType != null && ! recurrenceType.equals("")) {

        // finish up! print that data to the client!

        myOutput = myOutput + result;
        // another temporary fix
        if (result.equals("OK")) {
          writer.print(requestForm.getActivityID());
        } else {
          writer.print("FAIL");
        }

      } catch (Exception e) {
        logger.error("[execute]: Exception", e);
        writer.print("FAIL");
      }
    } else {
      logger.error("[execute]: Sync failed because sessionID is not valid");
      writer.print("FAIL");
    }
    // we're not forwarding to a jsp, so return null
    return (null);
  } // end execute()

} // end class ActivityEdit definition

