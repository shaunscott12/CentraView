/*
 * $RCSfile: ActivityDeleteHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:57 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;

/**
 * Serves as the handler for the ActivityDelete command request coming from
 * the CompanionLink-Centraview client. All members are the HTTP <b>POST</b>
 * values. Methods are getters and setters.
 *
 * @author  Ryan Grier <ryan@centraview.com>
 */

public class ActivityDeleteHandler extends ActionForm
{
  // members - these are the request parameters from 
  // the CompanionLink client
  private String sessionID = null;
  private String activityID = null;
 
  /**
   * Gets the value of the sessionID HTTP POST parameter to the ActivityDelete.do command.
   * @return  the value of the sessionID HTTP POST parameter to the ActivityDelete.do command.
   */
  public String getSessionID()
  {
    return(this.sessionID);
  }

  /**
   * Sets the value of the sessionID HTTP POST parameter of the ActivityAdd.do command.
   * Called by Struts.
   * @return void
   */
  public void setSessionID(String sessionID)
  {
    this.sessionID = sessionID;
  }
	
	
	/**
   * Gets the value of the activityID HTTP POST parameter to the ActivityDelete.do command.
   * @return  the value of the activityID HTTP POST parameter to the ActivityDelete.do command.
   */
  public String getActivityID()
  {
    return(this.activityID);
  }

  /**
   * Sets the value of the sessionID HTTP POST parameter of the ActivityDelete.do command.
   * Called by Struts.
   * @return void
   */
  public void setActivityID(String activityID)
  {
    this.activityID = activityID;
  }
}
