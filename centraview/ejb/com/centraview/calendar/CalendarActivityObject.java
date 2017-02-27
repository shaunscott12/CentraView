/*
 * $RCSfile: CalendarActivityObject.java,v $    $Revision: 1.2 $  $Date: 2005/09/13 21:57:42 $ - $Author: mcallist $
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

package com.centraview.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class CalendarActivityObject implements Serializable {
  GregorianCalendar start, end;
  Integer activityid;
  String activity;
  String activityDetail = "";
  String activityType;
  String individualName;
  String visibility;
  int userID;
  int individualID;
  int activitySequence;
  int activityOwnerId;
  String entityName;
  int entityID;
  ArrayList activityAttendee;

  boolean allDayEvent;
  GregorianCalendar allDayEndGMT;
  GregorianCalendar allDayStartGMT;

  public CalendarActivityObject(GregorianCalendar startGMT, GregorianCalendar endGMT,
      Integer activityid, String activity, String activityType, int userID, int individualID,
      String individualName, boolean allDayEvent, GregorianCalendar allDayStartGMT,
      GregorianCalendar allDayEndGMT) {
    this.start = startGMT;
    this.end = endGMT;
    this.activityid = activityid;
    this.activity = activity;
    this.activityType = activityType;
    this.userID = userID;
    this.individualID = individualID;
    this.individualName = individualName;
    this.allDayEvent = allDayEvent;
    this.allDayEndGMT = allDayEndGMT;
    this.allDayStartGMT = allDayStartGMT;
    this.activityAttendee = new ArrayList();
  }

  public GregorianCalendar getEndTime()
  {
    return end;
  }

  public GregorianCalendar getStartTime()
  {
    return start;
  }

  public Integer getActivityID()
  {
    return activityid;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity(String activity)
  {
    this.activity = activity;
    // strip out and html encode a bunch of stuff
    // as it causes javascript errors.
    this.activity = this.activity.replaceAll("\"", "&quot;");
    this.activity = this.activity.replaceAll("'", "&#39;");
    this.activity = this.activity.replaceAll("\'", "\\\\\'");
    this.activity = this.activity.replaceAll("&", "&amp;");
    this.activity = this.activity.replaceAll("<", "&lt;");
    this.activity = this.activity.replaceAll(">", "&gt;");
    this.activity = this.activity.replaceAll("\r", "");
    this.activity = this.activity.replaceAll("\n", "<br />");
  }

  public String getActivityType()
  {
    return activityType;
  }

  public String getIndividualName()
  {
    return individualName;
  }

  public int getUserID()
  {
    return userID;
  }

  public int getIndividualID()
  {
    return individualID;
  }

  public int getActivitySequence()
  {
    return this.activitySequence;
  }

  public void setActivitySequence(int activitySequence)
  {
    this.activitySequence = activitySequence;
  }

  public boolean search(String search)
  {
    return ((activityType.indexOf(search) != -1) || (individualName.indexOf(search) != -1)
        || (activity.indexOf(search) != -1) || (("" + userID).indexOf(search) != -1)
        || (("" + individualID).indexOf(search) != -1) || (("" + activityid.intValue())
        .indexOf(search) != -1));
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("CalendarActivityObject = {");
    sb.append("start=[" + (this.start).getTime() + "], ");
    sb.append("end=[" + (this.end).getTime() + "], ");
    sb.append("activityid=[" + this.activityid + "], ");
    sb.append("activity=[" + this.activity + "], ");
    sb.append("activityType=[" + this.activityType + "], ");
    sb.append("individualName=[" + this.individualName + "], ");
    sb.append("userID=[" + this.userID + "], ");
    sb.append("individualID=[" + this.individualID + "], ");
    sb.append("visibility=[" + this.visibility + "], ");
    sb.append("activitySequence=[" + this.activitySequence + "} ");
    sb.append("entityName=[" + this.entityName + "} ");
    sb.append("entityID=[" + this.entityID + "} ");
    return (sb.toString());
  }

  /**
   * @return Returns the activityDetail.
   */
  public String getActivityDetail()
  {
    return this.activityDetail;
  }

  /**
   * @param activityDetail The activityDetail to set.
   */
  public void setActivityDetail(String activityDetail)
  {
    // only set it if we aren't making it null
    if (activityDetail != null) {
      // if for some reason the detail field is massive
      // chunk it down to 251 plus an elipses.
      if (activityDetail.length() > 255) {
        // &#8230 is U2026: Horizontal Ellipsis
        this.activityDetail = activityDetail.substring(0, 253) + " &#8230;";
      } else {
        this.activityDetail = activityDetail;
      }
      // strip out and html encode a bunch of stuff
      // as it causes javascript errors.
      this.activityDetail = this.activityDetail.replaceAll("\"", "&quot;");
      this.activityDetail = this.activityDetail.replaceAll("'", "&#39;");
      this.activityDetail = this.activityDetail.replaceAll("&", "&amp;");
      this.activityDetail = this.activityDetail.replaceAll("<", "&lt;");
      this.activityDetail = this.activityDetail.replaceAll(">", "&gt;");
      this.activityDetail = this.activityDetail.replaceAll("\r", "");
      this.activityDetail = this.activityDetail.replaceAll("\n", "<br />");
    }
  }

  public String getEntityName()
  {
    return entityName;
  }

  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  public void setEntityID(int newEntityID)
  {
    this.entityID = newEntityID;
  }

  public int getEntityID()
  {
    return this.entityID;
  }

  public void setAllDayTime()
  {
    this.end = this.allDayEndGMT;
    this.start = this.allDayStartGMT;
  }

  public boolean getAllDayEvent()
  {
    return allDayEvent;
  }

  public String getActivityVisibility()
  {
    return this.visibility;
  }

  public void setActivityVisibility(String visibility)
  {
    this.visibility = visibility;
  }

  public int getActivityOwnerId()
  {
    return activityOwnerId;
  }

  public void setActivityOwnerId(int activityOwnerId)
  {
    this.activityOwnerId = activityOwnerId;
  }

  public ArrayList getActivityAttendee()
  {
    return activityAttendee;
  }

  public void setActivityAttendee(ArrayList activityAttendee)
  {
    this.activityAttendee = activityAttendee;
  }

}
