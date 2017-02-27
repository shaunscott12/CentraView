/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
 * Original Code created by CentraView are Copyright (c) 2003 - 2005 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */

package com.centraview.common;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.centraview.settings.Settings;
import com.centraview.settings.SiteInfo;

public class SessionAlive implements HttpSessionBindingListener, Serializable {

  public static final String IS_ALIVE = "isAlive";

  /**
   * checks if the isAlive is bound to the session
   */
  public void valueBound(HttpSessionBindingEvent event)
  {
    SiteInfo siteInfo = Settings.getInstance().getSiteInfo(CVUtility.getHostName(event.getSession().getServletContext()));
    incrementConcurrentUser(siteInfo);
  }

  /**
   * checks if the isAlive is unbound from the session when session is
   * invalidated
   */
  public void valueUnbound(HttpSessionBindingEvent event)
  {
    SiteInfo siteInfo = Settings.getInstance().getSiteInfo(CVUtility.getHostName(event.getSession().getServletContext()));
    decrementConcurrentUser(siteInfo);
  }

  private synchronized void incrementConcurrentUser(SiteInfo siteInfo)
  {
    int userCount = siteInfo.getUserCount();
    userCount++;
    siteInfo.setUserCount(userCount);
  }

  private synchronized void decrementConcurrentUser(SiteInfo siteInfo)
  {
    int userCount = siteInfo.getUserCount();
    userCount--;
    if (userCount < 0) {
      userCount = 0;
    }
    siteInfo.setUserCount(userCount);
  }
}
