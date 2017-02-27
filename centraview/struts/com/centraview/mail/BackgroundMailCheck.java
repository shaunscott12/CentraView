/*
 * $RCSfile: BackgroundMailCheck.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:05 $ - $Author: mking_cv $
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

package com.centraview.mail;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;

/**
 * This class will be a spawned thread that is run on a recurring
 * schedule maintained by Quartz. It is used because IMAP checking
 * sometimes takes a long time.  
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class BackgroundMailCheck extends Thread implements java.io.Serializable
{
  private int individualID = 0;
  private String dataSource = null;
  private static Logger logger = Logger.getLogger(BackgroundMailCheck.class);
  private int folderID = 0;
  
  public BackgroundMailCheck(String threadName, int individualID, String dataSource)
  {
    super(threadName);
    this.individualID = individualID;
    this.dataSource = dataSource;
  }

  public BackgroundMailCheck(String threadName, int individualID, String dataSource, int folderID)
  {
    this(threadName, individualID, dataSource);
    this.folderID = folderID;
  }
  
  public void run() 
  {
    // Check user's email accounts
    // die after a single check.
    try
    {
      MailHome mailhome = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail mailremote = (Mail)mailhome.create();
      mailremote.setDataSource(this.dataSource);
      mailremote.checkAllAccountsForUser(this.individualID, this.folderID);
    } catch (Exception e) {
      logger.error("[run] Exception thrown.", e);
    }
  }
}
