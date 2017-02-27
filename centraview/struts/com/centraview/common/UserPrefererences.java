/*
 * $RCSfile: UserPrefererences.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:18 $ - $Author: mking_cv $
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
package com.centraview.common;

import java.util.HashMap;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;

public class UserPrefererences implements java.io.Serializable
{
  private int IndividualID;
  private int[] moduleID;
  private String EmailVisibility;
  private int AcknowledgedDays;
  private String AcknowledgedVisibility;
  private HashMap otherPref;
  private boolean opportunityVisible;
  private boolean TaskVisible;
  private boolean supportVisible;
  private int EmailPortletAccountID;
  private int CalenderPortletActivityType;
  private int ActivityPortletAccountType;
  private boolean NewPortletVisible;
  private int NewPortletDays;
  private HashMap listPreferences;

  private String timeZone = "EST";
  private int timeSpanCalandar;
  private HashMap moduleAuth;
  private String CalendarDefaultView;
  private String CalendarRefreshMin;
  private String CalendarRefreshSec;
  private String HomeRefreshMin;
  private String HomeRefreshSec;
  private String Email;
  private String ContentType;
  private String TodaysCalendar;
  private String UnscheduledActivities;
  private String ScheduledOpportunities;
  private String ProjectTasks;
  private String SupportTickets;
  private String CompanyNews;
  private ModuleFieldRightMatrix mfrm;
  private String dateFormat = "MMM d, yyyy h:mm a";
  private int defaultFolderID;
  private String allRecordsPublic;
  private String syncAsPrivate;
  private int emailCheckInterval;
  private boolean showTicketCharts = false;
  private boolean showSalesCharts = false;

  public UserPrefererences()
  {
    listPreferences = new HashMap();
  }

  public String getSyncAsPrivate()
  {
    return(this.syncAsPrivate);
  }

  public void setSyncAsPrivate(String newSyncAsPrivate)
  {
    this.syncAsPrivate = newSyncAsPrivate;
  }

  public String getAllRecordsPublic()
  {
    return(this.allRecordsPublic);
  }

  public void setAllRecordsPublic(String newAllRecordsPublic)
  {
    this.allRecordsPublic = newAllRecordsPublic;
  }


  public void setTimeZone(String timeZone)
  {
    this.timeZone = timeZone;
  }

  public String getTimeZone()
  {
    return this.timeZone;
  }

  public void setTimeSpanCalandar(int timeSpanCalandar)
  {
    this.timeSpanCalandar = timeSpanCalandar;
  }

  public int getTimeSpanCalandar()
  {
    return this.timeSpanCalandar;
  }

  public ListPreference getlistPreferences(String listid)
  {
    return (ListPreference) listPreferences.get(listid);
  }

  public void addListPreferences(ListPreference lp)
  {
    listPreferences.put(lp.getLPName(), lp);
  }

  public void setIndividualID(int value)
  {
    IndividualID = value;
  }

  public void setEmailVisibility(String value)
  {
    EmailVisibility = value;
  }

  public void setAcknowledgedDays(int value)
  {
    AcknowledgedDays = value;
  }

  public void setAcknowledgedVisibility(String value)
  {
    AcknowledgedVisibility = value;
  }

  public void setOpportunityVisible(boolean value)
  {
    opportunityVisible = value;
  }

  public void setTaskVisible(boolean value)
  {
    TaskVisible = value;
  }

  public void setSupportVisible(boolean value)
  {
    supportVisible = value;
  }

  public void setEmailPortletAccountID(int value)
  {
    EmailPortletAccountID = value;
  }

  public void setCalenderPortletActivityType(int value)
  {
    CalenderPortletActivityType = value;
  }

  public void setActivityPortletAccountType(int value)
  {
    ActivityPortletAccountType = value;
  }

  public void setNewPortletDays(int value)
  {
    NewPortletDays = value;
  }

  public int getIndividualID()
  {
    return IndividualID;
  }

  public String getEmailVisibility()
  {
    return EmailVisibility;
  }

  public int getAcknowledgedDays()
  {
    return AcknowledgedDays;
  }

  public String getAcknowledgedVisibility()
  {
    return AcknowledgedVisibility;
  }

  public boolean getOpportunityVisible()
  {
    return opportunityVisible;
  }

  public boolean getTaskVisible()
  {
    return TaskVisible;
  }

  public boolean getSupportVisible()
  {
    return supportVisible;
  }

  public int getEmailPortletAccountID()
  {
    return EmailPortletAccountID;
  }

  public int getCalenderPortletActivityType()
  {
    return CalenderPortletActivityType;
  }

  public int getActivityPortletAccountType()
  {
    return ActivityPortletAccountType;
  }

  public int getNewPortletDays()
  {
    return NewPortletDays;
  }

  public int[] getModuleID()
  {
    return this.moduleID;
  }

  public void setModuleID(int[] moduleID)
  {
    this.moduleID = moduleID;
  }

  public String getDateFormat()
  {
    return this.dateFormat;
  }

  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
  }

  public HashMap getModuleAuth()
  {
    return this.moduleAuth;
  }

  public void setModuleAuth(HashMap moduleAuth)
  {
    this.moduleAuth = moduleAuth;
  }

  public int getDefaultFolderID()
  {
    return this.defaultFolderID;
  }

  public void setDefaultFolderID(int defaultFolderID)
  {
    this.defaultFolderID = defaultFolderID;
  }

  public String getCalendarDefaultView()
  {
    return this.CalendarDefaultView;
  }

  public void setCalendarDefaultView(String CalendarDefaultView)
  {
    this.CalendarDefaultView = CalendarDefaultView;
  }

  public String getCalendarRefreshMin()
  {
    return this.CalendarRefreshMin;
  }

  public void setCalendarRefreshMin(String CalendarRefreshMin)
  {
    this.CalendarRefreshMin = CalendarRefreshMin;
  }

  public String getCalendarRefreshSec()
  {
    return this.CalendarRefreshSec;
  }

  public void setCalendarRefreshSec(String CalRefreshSec)
  {
    this.CalendarRefreshSec = CalRefreshSec;
  }

  public String getHomeRefreshMin()
  {
    return this.HomeRefreshMin;
  }

  public void setHomeRefreshMin(String HomeRefreshMin)
  {
    this.HomeRefreshMin = HomeRefreshMin;
  }

  public String getHomeRefreshSec()
  {
    return this.HomeRefreshSec;
  }

  public void setHomeRefreshSec(String HomeRefreshSec)
  {
    this.HomeRefreshSec = HomeRefreshSec;
  }

  public String getTodaysCalendar()
  {
    return this.TodaysCalendar;
  }

  public void setTodaysCalendar(String TodaysCalendar)
  {
    this.TodaysCalendar = TodaysCalendar;
  }

  public String getEmail()
  {
    return this.Email;
  }

  public void setEmail(String Email)
  {
    this.Email = Email;
  }

  public String getUnscheduledActivities()
  {
    return this.UnscheduledActivities;
  }

  public void setUnscheduledActivities(String UnscheduledActivities)
  {
    this.UnscheduledActivities = UnscheduledActivities;
  }

  public String getScheduledOpportunities()
  {
    return this.ScheduledOpportunities;
  }

  public void setScheduledOpportunities(String ScheduledOpportunities)
  {
    this.ScheduledOpportunities = ScheduledOpportunities;
  }

  public String getProjectTasks()
  {
    return this.ProjectTasks;
  }

  public void setProjectTasks(String ProjectTasks)
  {
    this.ProjectTasks = ProjectTasks;
  }

  public String getSupportTickets()
  {
    return this.SupportTickets;
  }

  public void setSupportTickets(String SupportTickets)
  {
    this.SupportTickets = SupportTickets;
  }

  public String getCompanyNews()
  {
    return this.CompanyNews;
  }

  public void setCompanyNews(String CompanyNews)
  {
    this.CompanyNews = CompanyNews;
  }

  /**
    Content type decides the mail text type
    viz. Text ot HTML
    added by Deepa
  */
  public String getContentType()
  {
    return this.ContentType;
  }

  public void setContentType(String ContentType)
  {
    this.ContentType = ContentType;
  }

  public ModuleFieldRightMatrix getModuleAuthorizationMatrix()
  {
    return this.mfrm;
  }

  public void setModuleAuthorizationMatrix(ModuleFieldRightMatrix mfrm)
  {
    this.mfrm = mfrm;
  }
  
  public HashMap getAllListPreferences()
  {
    return this.listPreferences;
  }

  public void setEmailCheckInterval(int minutes)
  {
    // default to 5 if no valid value is given
    this.emailCheckInterval = (minutes >= 0) ? minutes : 10;
  }

  public int getEmailCheckInterval()
  {
    return(this.emailCheckInterval);
  }
  
  /**
   * @return Returns the showSalesCharts.
   */
  public final boolean isShowSalesCharts()
  {
    return showSalesCharts;
  }
  /**
   * @param showSalesCharts The showSalesCharts to set.
   */
  public final void setShowSalesCharts(boolean showSalesCharts)
  {
    this.showSalesCharts = showSalesCharts;
  }
  /**
   * @return Returns the showTicketCharts.
   */
  public final boolean isShowTicketCharts()
  {
    return showTicketCharts;
  }
  /**
   * @param showTicketCharts The showTicketCharts to set.
   */
  public final void setShowTicketCharts(boolean showTicketCharts)
  {
    this.showTicketCharts = showTicketCharts;
  }
}

