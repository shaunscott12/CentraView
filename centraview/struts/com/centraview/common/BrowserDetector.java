/*
 * $RCSfile: BrowserDetector.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:55 $ - $Author: mking_cv $
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

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

public final class BrowserDetector implements Serializable
{
  private HttpServletRequest request = null;
  private String userAgent = null;
  private boolean netEnabled = false;
  private boolean ie = false;
  private boolean ns6 = false;
  private boolean ns4 = false;

  public void setRequest(HttpServletRequest req)
  {
    request = req;
    this.userAgent = request.getHeader("User-Agent");
    String user = this.userAgent.toLowerCase();
    if (user.indexOf("msie") != -1)
    {
      ie = true;
    }else if (user.indexOf("netscape6") != -1){
      ns6 = true;
    }else if (user.indexOf("mozilla") != -1){
      ns4 = true;
    }

    if (user.indexOf(".net clr") != -1)
    {
      netEnabled = true;
    }
  }

  public String getUserAgent()
  {
    return this.userAgent;
  }

  public boolean isNetEnabled()
  {
    return netEnabled;
  }

  public boolean isIE()
  {
    return ie;
  }

  public boolean isNS6()
  {
    return ns6;
  }

  public boolean isNS4()
  {
    return ns4;
  }
  
}   // end class definition

