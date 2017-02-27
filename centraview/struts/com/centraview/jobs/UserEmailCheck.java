/*
 * $RCSfile: UserEmailCheck.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:31 $ - $Author: mking_cv $
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

package com.centraview.jobs;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.centraview.mail.BackgroundMailCheck;
import com.centraview.settings.Settings;
import com.centraview.settings.SiteInfo;

/**
 * Executes the BackgroundMailCheck daemon in order to
 * check the user's email accounts on a scheduled time.
 * This is an extention of TimerTask which is scheduled in the
 * LoginHandler, and executes every x minutes, where x
 * is defined by the user (minimum of 5 minutes). This
 * class will also unschedule this Job when the user's
 * session times out.
 * 
 * @since CentraView v1.0.15
 */
public class UserEmailCheck extends TimerTask
{
  private static Logger logger = Logger.getLogger(UserEmailCheck.class);
  private int individualId = 0;
  private HttpSession session = null;
  private String dataSource = null;
  private String host = null;
  
  public UserEmailCheck(int individualId, HttpSession session, String dataSource, String host) 
  {
    this.individualId = individualId;
    this.session = session;
    this.dataSource = dataSource;
    this.host = host;
  }
  
  public void run()
  {
    try
    {
      logger.debug("[run] Starting BackgroundMailCheck.");
      // start a mail check, and stick it on the session
      BackgroundMailCheck currentDaemon = (BackgroundMailCheck)session.getAttribute("mailDaemon");
      if (currentDaemon == null || !currentDaemon.isAlive())
      {
        logger.debug("[run] No current living Daemon, start another.");
        BackgroundMailCheck mailDaemon = new BackgroundMailCheck("mailDaemon" + individualId, individualId, this.dataSource);
        mailDaemon.setDaemon(true);
        mailDaemon.start();
        session.setAttribute("mailDaemon", mailDaemon);
      }
    } catch(IllegalStateException ise) {
      // kill this job because the session is not
      // valid, so the user must not be logged in
      logger.debug("[run] Session is invalid, user must have logged out.  Remove the mail check timer.");
      SiteInfo siteInfo = Settings.getInstance().getSiteInfo(host);
      Timer currentTimer = siteInfo.getUserTimer(this.individualId);
      if (currentTimer != null)
      {
        logger.debug("[run] Calling cancel on the timer.");
        currentTimer.cancel();
      }
    }
  }   // end run() method
}   // end class definition
