/*
 * $RCSfile: ActivityHandler.java,v $    $Revision: 1.6 $  $Date: 2005/10/24 16:19:45 $ - $Author: mcallist $
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * This ActivityHandler Initialize the PopulateForm Rest the whole Form with
 * default values Populate the Default values for the New Activity Form. Collect
 * the Type of Activity we are viewing. Set the Request Objects
 */
public class ActivityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(ActivityHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_New = ".view.activities.new_activity";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession();

      PopulateForm populateForm = new PopulateForm();
      // initialize the Activity form to have default blank values
      populateForm.resetForm(request, form);
      ActivityForm activityForm = (ActivityForm)form;
      // set up the date formatter default to the request locale.
      Locale locale = request.getLocale();
      activityForm.setLocale(locale);
      DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
      DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT, locale);

      int activityType = 1;

      String unScheduleActivity = "";
      if (request.getParameter("unScheduleActivity") != null) {
        unScheduleActivity = request.getParameter("unScheduleActivity").toString();
      }

      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      String userName = userObject.getfirstName() + " " + userObject.getlastName();

      if (request.getParameter("entityID") != null) {
        activityForm.setActivityEntityID(request.getParameter("entityID"));
      }

      if (request.getParameter("entityName") != null) {
        activityForm.setActivityEntityName(request.getParameter("entityName"));
      }

      if (request.getParameter("individualID") != null) {
        activityForm.setActivityIndividualID(request.getParameter("individualID"));
      }

      if (request.getParameter("individualName") != null) {
        activityForm.setActivityIndividualName(request.getParameter("individualName"));
      }

      if (request.getParameter("ProjectID") != null && request.getParameter("ProjectTitle") != null) {
        String projectID = request.getParameter("ProjectID");
        String projectTitle = request.getParameter("ProjectTitle");
        activityForm.setActivityRelatedFieldID(projectID);
        activityForm.setActivityRelatedFieldValue(projectTitle);
        activityForm.setActivityRelatedTypeValue("Project");
        activityForm.setActivityRelatedTypeID("36");
      }

      if (request.getParameter("oppID") != null) {
        String opportunityID = request.getParameter("oppID");
        activityForm.setActivityRelatedFieldID(opportunityID);
        activityForm.setActivityRelatedTypeValue("Opportunity");
        activityForm.setActivityRelatedTypeID("30");

        if (opportunityID != null && !(opportunityID.equals(""))) {
          int oppID = Integer.parseInt(opportunityID);
          SaleFacadeHome saleFacade = (SaleFacadeHome)CVUtility.getHomeObject(
              "com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
          SaleFacade remote = saleFacade.create();
          remote.setDataSource(dataSource);

          HashMap relatedOpportunityInfo = remote.getOpportunityRelatedInfo(individualID, oppID);

          if (relatedOpportunityInfo != null && relatedOpportunityInfo.size() != 0) {
            String title = (String)relatedOpportunityInfo.get("Title");
            if (title != null && !title.equals("") && !title.equals("NULL")) {
              activityForm.setActivityRelatedFieldValue(title);
            }

            if (relatedOpportunityInfo.get("EntityId") != null) {
              int entityid = ((Number)relatedOpportunityInfo.get("EntityId")).intValue();
              if (entityid != 0) {
                activityForm.setActivityEntityID(entityid + "");
                String entityName = (String)relatedOpportunityInfo.get("EntityName");
                if (entityName != null && !entityName.equals("") && !entityName.equals("NULL")) {
                  activityForm.setActivityEntityName(entityName);
                }
              }
            }

            if (relatedOpportunityInfo.get("IndividualId") != null) {
              int individualid = ((Number)relatedOpportunityInfo.get("IndividualId")).intValue();
              if (individualid != 0) {
                activityForm.setActivityIndividualID(individualid + "");
                String individualName = (String)relatedOpportunityInfo.get("individualname");
                if (individualName != null && !individualName.equals("")
                    && !individualName.equals("NULL")) {
                  activityForm.setActivityIndividualName(individualName);
                }
              }
            }
          } // end if (relatedOpportunityInfo != null &&
          // relatedOpportunityInfo.size() != 0)
        } // end if (opportunityID != null && !(opportunityID.equals("")))
      } // end if (request.getParameter("oppID") != null)

      if (request.getParameter("oppName") != null) {
        activityForm.setActivityRelatedFieldValue(request.getParameter("oppName"));
      }

      activityForm.setActivityOwnerID(String.valueOf(individualID));
      activityForm.setActivityOwnerName(userName);
      activityForm.setActivityPriority("2");
      activityForm.setActivityVisibility("PUBLIC");

      // All the following mess is to set a start and end time on the form.
      if ((request.getParameter("DAY") != null) && (request.getParameter("MONTH") != null)
          && (request.getParameter("YEAR") != null)) {
        // to digit
        String startMonthName = request.getParameter("MONTH");
        int startMonth = getCalendarMonth(startMonthName);
        String startDayString = request.getParameter("DAY");
        int startDay = Integer.parseInt(startDayString);
        String startYearString = request.getParameter("YEAR");
        int startYear = Integer.parseInt(startYearString);

        Calendar start = new GregorianCalendar(startYear, startMonth, startDay, 8, 0);
        String startTimeString = tf.format(start.getTime());
        if (CVUtility.notEmpty(request.getParameter("STIME"))) {
          startTimeString = request.getParameter("STIME");
        }
        Calendar passedTime = new GregorianCalendar();
        passedTime.setTime(tf.parse(startTimeString));
        start.set(Calendar.HOUR, passedTime.get(Calendar.HOUR));
        start.set(Calendar.MINUTE, passedTime.get(Calendar.MINUTE));
        start.set(Calendar.AM_PM, passedTime.get(Calendar.AM_PM));

        activityForm.setActivityStartDate(df.format(start.getTime()));
        activityForm.setActivityStartTime(tf.format(start.getTime()));
        // add an hour onto the start time for the default end time.
        Calendar end = new GregorianCalendar();
        end.setTime(start.getTime());
        end.add(Calendar.HOUR, 1);
        String endTimeString = tf.format(end.getTime());
        if (CVUtility.notEmpty(request.getParameter("ETIME"))) {
          endTimeString = request.getParameter("ETIME");
        }
        passedTime.setTime(tf.parse(endTimeString));
        end.set(Calendar.HOUR, passedTime.get(Calendar.HOUR));
        end.set(Calendar.MINUTE, passedTime.get(Calendar.MINUTE));
        end.set(Calendar.AM_PM, passedTime.get(Calendar.AM_PM));
        activityForm.setActivityEndDate(df.format(end.getTime()));
        activityForm.setActivityEndTime(tf.format(end.getTime()));
      } else if (unScheduleActivity != null && !(unScheduleActivity.equals("unSchedule"))) {
        // In the case the start time wasn't requested from the parent window,
        // set the start time to now() rounded up to the nearest hour, and the
        // end time to be the start time plus 1 hour.
        UserPrefererences userPref = userObject.getUserPref();
        String userTimeZoneId = userPref.getTimeZone();
        TimeZone userTimeZone = TimeZone.getTimeZone(userTimeZoneId);
        Calendar now = Calendar.getInstance(userTimeZone, locale);
        // round up the hour if we are at the 1 minute mark of the hour or
        // higher. just clear the second and millisecond fields, its only
        // scheduling not physics.
        now.clear(Calendar.SECOND);
        now.clear(Calendar.MILLISECOND);
        if (now.get(Calendar.MINUTE) > 0) {
          now.clear(Calendar.MINUTE);
          now.add(Calendar.HOUR, 1);
        }
        activityForm.setActivityStartDate(df.format(now.getTime()));
        activityForm.setActivityStartTime(tf.format(now.getTime()));
        // Increment now by 1 hour to get the end time.
        now.add(Calendar.HOUR, 1);
        activityForm.setActivityEndDate(df.format(now.getTime()));
        activityForm.setActivityEndTime(tf.format(now.getTime()));
      }

      String typeOfActivity = "";
      if (request.getParameter(ConstantKeys.TYPEOFACTIVITY) == null) {
        typeOfActivity = ConstantKeys.APPOINTMENT;
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY) != null) {
        if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals("MULTIACTIVITY")) {
          typeOfActivity = ConstantKeys.APPOINTMENT;
        } else {
          typeOfActivity = request.getParameter(ConstantKeys.TYPEOFACTIVITY).toUpperCase();
        }
      } else {
        typeOfActivity = ConstantKeys.APPOINTMENT;
      }

      if (typeOfActivity.equals(ConstantKeys.APPOINTMENT)) {
        activityType = 1;
      } else if (typeOfActivity.equals(ConstantKeys.CALL)) {
        activityType = 2;
      } else if (typeOfActivity.equals(ConstantKeys.MEETING)) {
        activityType = 5;
      } else if (typeOfActivity.equals(ConstantKeys.NEXTACTION)) {
        activityType = 7;
      } else if (typeOfActivity.equals(ConstantKeys.TODO)) {
        activityType = 6;
      }

      if (typeOfActivity.equals("NEXT ACTION")) {
        typeOfActivity = ConstantKeys.NEXTACTION;
        activityType = 7;
      } else if (typeOfActivity.equals("TO DO")) {
        typeOfActivity = ConstantKeys.TODO;
        activityType = 6;
      }

      request.setAttribute("actionName", "");
      activityForm.setActivityType(activityType + "");

      // set user as default attendee
      DDNameValue setUserAttendee = new DDNameValue("" + individualID + "#" + userName, userName);
      Vector att_required = new Vector();
      att_required.add(setUserAttendee);

      String[] attRequiredArray = new String[1];
      attRequiredArray[0] = "" + individualID + "#" + userName;
      activityForm.setActivityAttendeesRequired(attRequiredArray);
      activityForm.setActivityAttendeeRequiredVector(att_required);

      session.setAttribute("activityform", form);

      // set request to pass to jsp
      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, typeOfActivity);

      // set default opening window to detail sub-type activity
      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, ConstantKeys.DETAIL);
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, ConstantKeys.ADD);

      FORWARD_final = FORWARD_New;
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    } // end of catch statement (Exception)
    return (mapping.findForward(FORWARD_final));
  } // end of execute method.

  /**
   * Takes calendar Month name and returns the cannonical month number
   * @param monthName
   * @return
   */
  public int getCalendarMonth(String monthName)
  {
    int monthNumber = 0;
    if (monthName.equalsIgnoreCase("JAN") || monthName.equalsIgnoreCase("JANUARY")) {
      monthNumber = 0;
    } else if (monthName.equalsIgnoreCase("FEB") || monthName.equalsIgnoreCase("FEBRUARY")) {
      monthNumber = 1;
    } else if (monthName.equalsIgnoreCase("MAR") || monthName.equalsIgnoreCase("MARCH")) {
      monthNumber = 2;
    } else if (monthName.equalsIgnoreCase("APR") || monthName.equalsIgnoreCase("APRIL")) {
      monthNumber = 3;
    } else if (monthName.equalsIgnoreCase("MAY")) {
      monthNumber = 4;
    } else if (monthName.equalsIgnoreCase("JUN") || monthName.equalsIgnoreCase("JUNE")) {
      monthNumber = 5;
    } else if (monthName.equalsIgnoreCase("JUL") || monthName.equalsIgnoreCase("JULY")) {
      monthNumber = 6;
    } else if (monthName.equalsIgnoreCase("AUG") || monthName.equalsIgnoreCase("AUGUST")) {
      monthNumber = 7;
    } else if (monthName.equalsIgnoreCase("SEP") || monthName.equalsIgnoreCase("SEPTEMBER")) {
      monthNumber = 8;
    } else if ((monthName.equalsIgnoreCase("OCTOBER")) || monthName.equalsIgnoreCase("OCT")) {
      monthNumber = 9;
    } else if (monthName.equalsIgnoreCase("NOVEMBER") || monthName.equalsIgnoreCase("NOV")) {
      monthNumber = 10;
    } else if (monthName.equalsIgnoreCase("DECEMBER") || monthName.equalsIgnoreCase("DEC")) {
      monthNumber = 11;
    }
    return monthNumber;
  } // end of getCalendarMonth method
} // end of ActivityHandler class
