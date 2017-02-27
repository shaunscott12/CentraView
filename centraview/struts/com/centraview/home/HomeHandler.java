/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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

package com.centraview.home;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.centraview.activity.ConstantKeys;
import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.calendar.CalendarActivityObject;
import com.centraview.calendar.CalendarList;
import com.centraview.calendar.CalendarListElement;
import com.centraview.calendar.CalendarMember;
import com.centraview.calendar.CalendarUtil;
import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;
import com.centraview.projects.helper.TaskVO;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.settings.Settings;
import com.centraview.settings.SettingsInterface;
import com.centraview.settings.SiteInfo;

public class HomeHandler extends org.apache.struts.action.Action {
  // TODO centralize the magic words for the image icon names.
  private static Logger logger = Logger.getLogger(HomeHandler.class);

  /**
   * Gets all the stuff together for the portlets on the home page, and sticks
   * them in requestScope variables for display. email portlet
   * (emailButtonModifier and emailFolders) today's calendar
   * (calendarDisplayList: an arraylist of HomeDisplayItems)
   * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm,
   *      javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws RuntimeException, Exception
  {
    String host = CVUtility.getHostName(super.getServlet().getServletContext());
    SettingsInterface settings = Settings.getInstance();
    SiteInfo siteInfo = settings.getSiteInfo(host);
    final String dataSource = siteInfo.getDataSource();

    // Set up search bar
    request.setAttribute("showAdvancedSearch", new Boolean(false));
    request.setAttribute("showCustomViews", new Boolean(false));
    request.setAttribute("showComposeButton", new Boolean(false));
    request.setAttribute("showNewButton", new Boolean(false));
    request.setAttribute("showPrintButton", new Boolean(false));
    // Description must allow i18n
    Locale locale = request.getLocale();
    MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
    String searchButtonDescription = messages.getMessage(locale,
        "label.home.searchbutton.description");
    request.setAttribute("searchButtonDescription", searchButtonDescription);
    request.setAttribute("hideListDropdown", new Boolean(true));

    // Set the moduleId to Entity so search knows where we want to look.
    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null) {
      moduleList = (HashMap)globalMasterLists.get("moduleList");
    }
    String moduleID = (String)moduleList.get("Entity");
    request.setAttribute("moduleId", moduleID);

    // Now actually get all the stuff that used to be retrieved
    // through the portlet JSPs, and let the JSPs just worry about display

    // First setup for all the work
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    int individualId = user.getIndividualID();
    TimeZone tz = TimeZone.getTimeZone((user.getUserPref()).getTimeZone());

    //UserPrefererences preferences = user.getUserPref();

    GregorianCalendar startTime = new GregorianCalendar(tz, locale);
    startTime.setTime(Calendar.getInstance().getTime());
    startTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime
        .get(Calendar.DATE), 0, 0);

    GregorianCalendar endTime = new GregorianCalendar(tz, locale);
    endTime.setTime(Calendar.getInstance().getTime());
    endTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime
        .get(Calendar.DATE), 0, 0);
    endTime.add(Calendar.DATE, 1);
    // The calendar map has a bunch of stuff in it, it will be used for a few of
    // the portlets
    // set up below.
    HashMap calendarMap = CalendarUtil.getCalendarCollection(startTime, endTime, tz, individualId,
        60, "", dataSource);
    // use default formatters for locale
    DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
    DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);

    // email portlet
    MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
    Mail remote = home.create();
    remote.setDataSource(dataSource);
    ArrayList emailFolders = remote.getHomeFolderList(individualId);
    // this modifier gets stuck right in the html of the "Compose" button
    String emailButtonModifier = "";
    if ((emailFolders == null) || (emailFolders.size() < 1)) {
      emailButtonModifier = "disabled=\"true\"";
    }
    request.setAttribute("emailFolders", emailFolders);
    request.setAttribute("emailButtonModifier", emailButtonModifier);

    // calendar portlet

    // for every activity on the calendar list, create a display object, and
    // append it to the list. The list will be iterated and displayed on the JSP
    ArrayList calendarDisplayList = new ArrayList();
    CalendarList calendarList = (CalendarList)calendarMap.get("calendarlist");
    Set listkey = calendarList.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext()) {
      Integer key = (Integer)it.next();
      CalendarListElement ele = (CalendarListElement)calendarList.get(key);
      Set elekey = ele.keySet();
      Iterator eleit = elekey.iterator();
      while (eleit.hasNext()) {
        CalendarMember calmember = (CalendarMember)ele.get(eleit.next());
        CalendarActivityObject activity = calmember.getActivityobject();
        Calendar start = activity.getStartTime();
        start.setTimeZone(tz);
        Calendar end = activity.getEndTime();
        end.setTimeZone(tz);
        String timeRange = dateTimeFormat.format(start.getTime()) + " - "
            + dateTimeFormat.format(end.getTime());
        String activityStartTime = timeFormat.format(start.getTime());
        // type is that stored in the activitytype database table.
        String activityType = activity.getActivityType();
        String icon = this.determineActivityIcon(activityType);
        activityType = this.convertActivityType(activityType);

        ArrayList activitiesAttendeesList = activity.getActivityAttendee();
        if ("PRIVATE".equals(activity.getActivityVisibility())
            && activity.getActivityOwnerId() != individualId
            && !activitiesAttendeesList.contains(String.valueOf(individualId))) {
          activity.setActivity("Private");
          activity.setActivityDetail("Private");
        }

        String requestURL = "";
        if ((activity.getActivity() != null) && (!activity.getActivity().equals("Private"))) {
          requestURL = "c_openPopup('/activities/view_activity.do?rowId="
              + activity.getActivityID() + "&" + ConstantKeys.TYPEOFACTIVITY + "=" + activityType
              + "&activityAction=Home')";
        }
        String activityTitle = activity.getActivity();
        String overLibTitle = CVUtility.cleanStringJS(activityTitle);
        String activityDetails = (activity.getActivityDetail() != null) ? activity
            .getActivityDetail() : "";
        String overLibDetail = CVUtility.cleanStringJS(activityDetails);

        HomeDisplayItem item = new HomeDisplayItem();
        item.setTitle(activityTitle);
        item.setUrl(requestURL);
        item.setOlTitle(overLibTitle);
        item.setOlDescription(overLibDetail);
        item.setOlCaption(timeRange);
        item.setSubTitle(activityStartTime);
        item.setIcon(icon);
        item.setRelatedTitle(activity.getEntityName());
        item.setRelatedId(activity.getEntityID());
        calendarDisplayList.add(item);
      }
    } // end while scheduled activity iterator
    request.setAttribute("calendarDisplayList", calendarDisplayList);

    // unscheduled activities portlet

    ArrayList unscheduledDisplayList = new ArrayList();
    // pull out the unscheduled activities from that stupid map retreived
    // earlier
    HashMap unscheduledActivityMap = (HashMap)calendarMap.get("unscheduledactivity");
    Set unscheduledKeys = unscheduledActivityMap.keySet();
    it = unscheduledKeys.iterator();
    while (it.hasNext()) {
      CalendarActivityObject activity = (CalendarActivityObject)unscheduledActivityMap.get(it
          .next());
      String activityType = activity.getActivityType();
      String icon = this.determineActivityIcon(activityType);
      activityType = this.convertActivityType(activityType);
      // determine requestURL based on the type
      // TODO make view_activity.do do this for me (maybe a dispatcher action
      // first)
      String requestURL;
      if (activityType.equals(ConstantKeys.FORECASTSALE)) {
        requestURL = "/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&activityId=";
        requestURL += activity.getActivityID();
      } else if (activityType.equals(ConstantKeys.LITERATUREREQUEST)) {
        requestURL = "/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=EDIT&activityid=";
        requestURL += activity.getActivityID();
      } else {
        requestURL = "/activities/view_activity.do?rowId=";
        requestURL += activity.getActivityID();
        requestURL += "&activityAction=Home&TYPEOFACTIVITY=";
        requestURL += activityType;
      }
      HomeDisplayItem item = new HomeDisplayItem();
      item.setTitle(activity.getActivity());
      item.setIcon(icon);
      item.setUrl(requestURL);
      item.setRelatedTitle(activity.getEntityName());
      item.setRelatedId(activity.getEntityID());
      unscheduledDisplayList.add(item);
    } // end while unscheduled activity iterator
    request.setAttribute("unscheduledDisplayList", unscheduledDisplayList);

    // project tasks
    
    ArrayList taskDisplayList = new ArrayList();
    // ask the EJB layer to give us a list of relevant TASK VO's
    ActivityFacade activityFacade = null;
    try {
      activityFacade = (ActivityFacade)CVUtility.setupEJB("ActivityFacade",
          "com.centraview.activity.activityfacade.ActivityFacadeHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    ArrayList taskList = activityFacade.getTaskList(individualId);

    for (int i = 0; i < taskList.size(); i++) {
      TaskVO task = (TaskVO)taskList.get(i);
      String requestURL = "/projects/view_task.do?rowId=";
      requestURL += task.getTaskid();
      HomeDisplayItem item = new HomeDisplayItem();
      item.setTitle(task.getTitle());
      item.setUrl(requestURL);
      taskDisplayList.add(item);
    }
    request.setAttribute("taskDisplayList", taskDisplayList);
    
    // opportunity

    ArrayList opportunityDisplayList = new ArrayList();
    // ask the EJB layer to give us a list of relevant TASK VO's
    SaleFacade saleFacade = null;
    try {
      saleFacade = (SaleFacade)CVUtility.setupEJB("SaleFacade",
          "com.centraview.sale.salefacade.SaleFacadeHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    ArrayList opportunityList = saleFacade.getInterestingOpportunityList(individualId);

    for (int i = 0; i < opportunityList.size(); i++) {
      OpportunityVO opportunity = (OpportunityVO)opportunityList.get(i);
      // somehow a null opportunity can make it's way onto this list, so
      // quick and dirty fix is to check that here.
      if (CVUtility.notEmpty(opportunity)) {
        String requestURL = "/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&rowId=";
        requestURL += opportunity.getOpportunityID();
        HomeDisplayItem item = new HomeDisplayItem();
        item.setTitle(opportunity.getTitle());
        item.setUrl(requestURL);
        item.setRelatedTitle(opportunity.getEntityname());
        item.setRelatedId(opportunity.getEntityID());
        item.setIcon("icon_sale.gif");
        opportunityDisplayList.add(item);
      }
    }
    request.setAttribute("opportunityDisplayList", opportunityDisplayList);

    // tickets
    
    // Any Open Tickets assigned to the logged in User
    //String query = "ADVANCE: select ticketid,subject from ticket,supportstatus where ticket.status =  supportstatus.statusid and supportstatus.name = 'Open' AND assignedto = "+indvID;

    // company news
    // alerts
    return (mapping.findForward(".view.home"));
  }

  /**
   * Returns the name of the icon file that represents the passed in activity
   * type. The type should be the text from the activityType table in the
   * database.
   * @param activityType
   * @return icon name
   */
  private String determineActivityIcon(String activityType)
  {
    String icon = "icon_file.gif";
    if (activityType.equals(ConstantKeys.DB_APPOINTMENT)) {
      icon = "icon_invitation.gif";
      activityType = ConstantKeys.APPOINTMENT;
    } else if (activityType.equals(ConstantKeys.DB_CALL)) {
      icon = "icon_call.gif";
      activityType = ConstantKeys.CALL;
    } else if (activityType.equals(ConstantKeys.DB_LITERATURE_REQUEST)) {
      icon = "icon_file.gif";
      activityType = ConstantKeys.LITERATUREREQUEST;
    } else if (activityType.equals(ConstantKeys.DB_MEETING)) {
      icon = "icon_invitation.gif";
      activityType = ConstantKeys.MEETING;
    } else if (activityType.equals(ConstantKeys.DB_NEXT_ACTION)) {
      icon = "icon_tasks.gif";
      activityType = ConstantKeys.NEXTACTION;
    } else if (activityType.equals(ConstantKeys.DB_TASK)) {
      icon = "icon_tasks.gif";
      activityType = ConstantKeys.TASK;
    } else if (activityType.equals(ConstantKeys.DB_TODO)) {
      icon = "icon_tasks.gif";
      activityType = ConstantKeys.TODO;
    }
    return icon;
  }

  /**
   * Takes the activity type provided from the database and returns the type in
   * a form that is expected by the struts actions.
   * @param activityType
   * @return the activity type from ConstantKeys
   */
  private String convertActivityType(String activityType)
  {
    String activityReturnType = ConstantKeys.APPOINTMENT;
    if (activityType.equals(ConstantKeys.DB_APPOINTMENT)) {
      activityReturnType = ConstantKeys.APPOINTMENT;
    } else if (activityType.equals(ConstantKeys.DB_CALL)) {
      activityReturnType = ConstantKeys.CALL;
    } else if (activityType.equals(ConstantKeys.DB_LITERATURE_REQUEST)) {
      activityReturnType = ConstantKeys.LITERATUREREQUEST;
    } else if (activityType.equals(ConstantKeys.DB_MEETING)) {
      activityType = ConstantKeys.MEETING;
    } else if (activityType.equals(ConstantKeys.DB_NEXT_ACTION)) {
      activityReturnType = ConstantKeys.NEXTACTION;
    } else if (activityType.equals(ConstantKeys.DB_TASK)) {
      activityReturnType = ConstantKeys.TASK;
    } else if (activityType.equals(ConstantKeys.DB_TODO)) {
      activityReturnType = ConstantKeys.TODO;
    }
    return activityReturnType;
  }
}
