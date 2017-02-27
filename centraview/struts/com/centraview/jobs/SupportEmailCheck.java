/*
 * $RCSfile: SupportEmailCheck.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:40:24 $ - $Author: mcallist $
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

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;

public class SupportEmailCheck extends TimerTask
{
  private static Logger logger = Logger.getLogger(SupportEmailCheck.class);
  private String dataSource = null;
  
  public SupportEmailCheck(String dataSource) 
  {
    this.dataSource = dataSource;
  }
  
  public void run()
  {
    logger.debug("[execute]: Starting SupportMailCheck thread.");
    // Get the mailHome and the dataSource from the JobDataMap
    // start a mail check.
    try
    {
      MailHome mailHome = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail mailremote = mailHome.create();
      mailremote.setDataSource(this.dataSource);
      mailremote.checkAllSupportAccounts();
    } catch (Exception e) {
      logger.error("[run] Exception thrown.", e);
    }
  }
}
