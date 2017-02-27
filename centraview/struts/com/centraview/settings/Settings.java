/*
 * $RCSfile: Settings.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:46 $ - $Author: mking_cv $
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

package com.centraview.settings;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * The Settings Singleton is intended to hold the initialContext
 * and the dataSource information for the web tier of CentraView
 * It should be initialized by the SettingsPlugin, and everyone
 * else can just get the desired information.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class Settings implements SettingsInterface
{
  private static Logger logger = Logger.getLogger(Settings.class);

  /**
   * The internal instance of the singleton.
   */
  static private SettingsInterface _instance = new Settings();

  /**
   * The initialContext created based on the JNDI properties in CentraView.properties
   */
  private InitialContext initCtx = null;

  /**
   * The MACAddress for this server, set by the SettingsPlugin
   */
  private String MACAddress = null;
  
  /**
   * A HashMap of SiteInfo Objects keyed by the sitename defined in the
   * server.xml file.
   */
  private HashMap siteInfoMap = new HashMap();
  
  /**
   * The scheduler which can be used for scheduling any jobs
   * that need to be done, primarily checking of email accounts.
   */
  private Timer supportEmailTimer = null;
  
  private TimerTask supportEmailTask = null;
 
  /**
   * @return Returns the supportEmailTask.
   */
  public synchronized final TimerTask getSupportEmailTask()
  {
    return supportEmailTask;
  }
  
  /**
   * @param supportEmailTask The supportEmailTask to set.
   */
  public synchronized final void setSupportEmailTask(TimerTask supportEmailTask)
  {
    this.supportEmailTask = supportEmailTask;
  }
  
  /**
   * @return Returns the supportEmailTimer.
   */
  public synchronized final Timer getSupportEmailTimer()
  {
    return supportEmailTimer;
  }
  
  /**
   * @param supportEmailTimer The supportEmailTimer to set.
   */
  public synchronized final void setSupportEmailTimer(Timer supportEmailTimer)
  {
    this.supportEmailTimer = supportEmailTimer;
  }

  /**
   * Declare constructor private so others can't create this.
   * Could make it protected to allow subclassing, but I doubt
   * that matters much here. Don't use lazy loading since it
   * will be a wasted check every time this thing better be setup.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  private Settings() { }

  static public synchronized final SettingsInterface getInstance()
  {
    return _instance;
  }

  /**
   * @return initCtx the InitialContext created from the properties in
   *         CentraView.properties
   */
  public synchronized final InitialContext getInitCtx()
  {
    return initCtx;
  } // end of getInitCtx

  /**
   * @param context This should only be set from the SettingsPlugin.
   */
  public synchronized final void setInitCtx(InitialContext context)
  {
    initCtx = context;
  } // end of setInitCtx

  public String toString()
  {
    return "InitialContext: " + initCtx + " MACAddress: " + MACAddress;
  }
  
  /**
   * @return Returns the MACAddress.
   */
  public synchronized final String getMACAddress()
  {
    return MACAddress;
  }
  
  /**
   * @param address The MACAddress to set.
   */
  public synchronized final void setMACAddress(String address)
  {
    MACAddress = address;
  }
  
  public synchronized final void setSiteInfo(String siteName, SiteInfo siteInfo)
  {
    siteInfoMap.put(siteName, siteInfo);
  }
  
  public synchronized final SiteInfo getSiteInfo(String siteName)
  {
    return (SiteInfo)siteInfoMap.get(siteName);
  }
  
} // end of Settings Class

