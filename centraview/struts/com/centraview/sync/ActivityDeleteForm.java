/*
 * $RCSfile: ActivityDeleteForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:57 $ - $Author: mking_cv $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ActivityDeleteForm extends ActionForm {

  private String sessionID = null;
  private String activityID = null;

  public String getSessionID() {
    return this.sessionID;
  }   // end getSessionName()

  public void setSessionID(String sessionID) {
    this.sessionID = sessionID;
  }   // end setSessionName()
  
  public String getActivityID() {
    return this.activityID;
  }   // end getActivityName()

  public void setActivityID(String activityID) {
    this.activityID = activityID;
  }   // end setActivityName()

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.sessionID = null;
    this.activityID = null;
  }   // end reset()
  
}   // end class ActivityDeleteForm definition
