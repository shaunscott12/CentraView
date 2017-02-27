/*
 * $RCSfile: LogoutForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:01 $ - $Author: mking_cv $
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

public class LogoutForm extends ActionForm 
{
  private String userName = null;
  private String password = null;

  public String getUserName() {
    return this.userName;
  }   // end getUserName()

  public String getPassword() {
    return this.password;
  }   // end getPassword()

  public void setUserName(String userName) {
    this.userName = userName;
  }   // end setUserName()

  public void setPassword(String password) {
    this.password = password;
  }   // end setPassword()

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.userName = null;
    this.password = null;
  }   // end reset()
  
}   // end class definition

