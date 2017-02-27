/*
 * $RCSfile: CalendarMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:51 $ - $Author: mking_cv $
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

public class CalendarMember
{
  /** All the information about the activity */
  CalendarActivityObject activityobject;
  /** I don't know what the purpose of this field is */
  int spanserialId;
  /** How many rows this activity consumes useful only in daily view */
  int totalspans;
  /** The URL that displays this activity */
  String URL;
  /** The image URI for the icon */
  String IconURL;

  public CalendarMember(CalendarActivityObject activityobject, int spanserialId, int totalspans)
  {
    this.activityobject = activityobject;
    this.spanserialId = spanserialId;
    this.totalspans = totalspans;
  }

  public CalendarActivityObject getActivityobject()
  {
    return activityobject;
  }

  public int getSpanserialId()
  {
    return spanserialId;
  }

  public int getTotalspans()
  {
    return totalspans;
  }

  public void setRequestURL(String URL)
  {
    this.URL = URL;
  }

  public String getRequestURL()
  {
    return URL;
  }

  public void setIcon(String IconURL)
  {
    this.IconURL = IconURL;
  }

  public String getIcon()
  {
    return IconURL;
  }
}
