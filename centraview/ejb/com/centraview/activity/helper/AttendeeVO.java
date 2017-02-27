/*
 * $RCSfile: AttendeeVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:37 $ - $Author: mking_cv $
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

import java.io.Serializable;

import com.centraview.contact.individual.IndividualVO;

public class AttendeeVO extends IndividualVO implements Serializable
{
  public static final String AT_REQUIRED = "REQUIRED";
  public static final String AT_OPTIONAL = "OPTIONAL";
  public static final int ATS_NEW = 1;
  public static final int ATS_CONFIRMED = 2;
  public static final int ATS_DECLINED = 3;
  private int activityId;
  private String attendeeType; //"REQUIRED", "OPTIONAL"
  private int attendeeStatus;
  /**
   * @roseuid 3EFC315C0224
   */
  public AttendeeVO()
  {
    //this.attendee = attendee ;
    this.attendeeStatus = AttendeeVO.ATS_NEW;
  }
  public int getActivityId()
  {
    return this.activityId;
  }
  public void setActivityId(int activityId)
  {
    this.activityId = activityId;
  }
  public int getAttendeeStatus()
  {
    return this.attendeeStatus;
  }
  public void setAttendeeStatus(int attendeeStatus)
  {
    this.attendeeStatus = attendeeStatus;
  }
  public String getAttendeeType()
  {
    return this.attendeeType;
  }
  public void setAttendeeType(String attendeeType)
  {
    this.attendeeType = attendeeType;
  }
}