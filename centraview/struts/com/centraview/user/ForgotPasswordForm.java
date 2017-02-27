/*
 * $RCSfile: ForgotPasswordForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:06 $ - $Author: mking_cv $
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

package com.centraview.user;

public class ForgotPasswordForm extends org.apache.struts.action.ActionForm {
  //store username
  private String username;
  //store email
  private String email;
  //getUsername
  public String getUsername() {
    return this.username;
  }
  //setUsername
  public void setUsername(String username) {
    this.username = username;
  }
  //getEmail
  public String getEmail() {
    return this.email;
  }
  //setEmail
  public void setEmail(String email) {
    this.email = email;
  }
}
