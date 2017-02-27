/*
 * $RCSfile: ActivityActionVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:34 $ - $Author: mking_cv $
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
import java.sql.Timestamp;
import java.util.ArrayList;

public class ActivityActionVO implements Serializable
{
  public static final String AA_EMAIL = "EMAIL";
  public static final String AA_ALERT = "ALERT";
  private int actionId;
  private String actionType;
  private Timestamp actionTime;
  private int repeat;
  private int interval;
  //private int recipient;
  private String message;
  private ArrayList recipient;
  public ActivityActionVO()
  {}
  /**
   * @roseuid 3ED75420025B
   */
  public ActivityActionVO(String actionType, Timestamp actionTime, int repeat, int interval, int recipient, String message)
  {
    this.actionType = actionType;
    this.actionTime = actionTime;
    this.repeat = repeat;
    this.interval = interval;
    this.recipient = new ArrayList();
    this.recipient.add(new Integer(recipient));
    this.message = message;
  }
  public Timestamp getActionTime()
  {
    return this.actionTime;
  }
  public void setActionTime(Timestamp actionTime)
  {
    this.actionTime = actionTime;
  }
  public String getActionType()
  {
    return this.actionType;
  }
  public void setActionType(String actionType)
  {
    this.actionType = actionType;
  }
  public int getInterval()
  {
    return this.interval;
  }
  public void setInterval(int interval)
  {
    this.interval = interval;
  }
  public String getMessage()
  {
    return this.message;
  }
  public void setMessage(String message)
  {
    this.message = message;
  }
  public ArrayList getRecipient()
  {
    return this.recipient;
  }
  public void setRecipient(int recipient)
  {
    if (this.recipient == null)
      this.recipient = new ArrayList();
    this.recipient.add(new Integer(recipient));
  }
  public int getRepeat()
  {
    return this.repeat;
  }
  public void setRepeat(int repeat)
  {
    this.repeat = repeat;
  }
  public int getActionId()
  {
    return this.actionId;
  }
  public void setActionId(int actionId)
  {
    this.actionId = actionId;
  }
}