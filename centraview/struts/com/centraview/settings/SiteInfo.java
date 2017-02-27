/*
 * $RCSfile: SiteInfo.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:11:48 $ - $Author: mcallist $
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

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class SiteInfo
{
  /**
   * The centraview.datasource defined in CentraView.properties
   */
  private String dataSource = null;

  /**
   * The scheduler which can be used for scheduling any jobs
   * that need to be done, primarily checking of email accounts.
   */
  private Timer supportEmailTimer = null;
  
  private TimerTask supportEmailTask = null;
  
  /** Stores the customerLogo image File */
  private File customerLogo;
  
  /** Stores a cache of the File bytes in memory to save disk access */
  private byte[] customerLogoData;
  
  /** Tells whether the cache is dirty and needs to be refreshed from disk */
  private boolean customerLogoDirty = true;
  /**
   * The licenseInstance contains all the license information
   * which the login handler uses/displays
   */
  private Object licenseInstance = null;
  
  /**
   * A HashMap which holds Timers for an Individual's automated email check.
   */
  private HashMap userTimerRegistry = new HashMap();
  /**
   * the user coutnt is the number of active logged in users for this site.
   */
  private int userCount = 0;
  
  /**
   * @return Returns the customerLogoData.
   */
  public synchronized final byte[] getCustomerLogoData()
  {
    return customerLogoData;
  }

  /**
   * @param customerLogoData The customerLogoData to set.
   */
  public synchronized final void setCustomerLogoData(byte[] customerLogoData)
  {
    this.customerLogoData = customerLogoData;
  }

  /**
   * @return Returns the customerLogoDirty.
   */
  public synchronized final boolean isCustomerLogoDirty()
  {
    return customerLogoDirty;
  }

  /**
   * @param customerLogoDirty The customerLogoDirty to set.
   */
  public synchronized final void setCustomerLogoDirty(boolean customerLogoDirty)
  {
    this.customerLogoDirty = customerLogoDirty;
  }

  /**
   * @return Returns the customerLogo.
   */
  public synchronized final File getCustomerLogo()
  {
    return customerLogo;
  }
  
  /**
   * @param customerLogo The customerLogo to set.
   */
  public synchronized final void setCustomerLogo(File customerLogo)
  {
    this.customerLogo = customerLogo;
  }

  /**
   * @return Returns the dataSource.
   */
  public synchronized final String getDataSource()
  {
    return dataSource;
  }
  
  /**
   * @param dataSource The dataSource to set.
   */
  public synchronized final void setDataSource(String dataSource)
  {
    this.dataSource = dataSource;
  }
  
  /**
   * @return Returns the licenseInstance.
   */
  public synchronized final Object getLicenseInstance()
  {
    return licenseInstance;
  }
  
  /**
   * @param licenseInstance The licenseInstance to set.
   */
  public synchronized final void setLicenseInstance(Object licenseInstance)
  {
    this.licenseInstance = licenseInstance;
  }
  
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
   * @return Returns the userTimer, given the individualId.
   */
  public synchronized final Timer getUserTimer(int individualId)
  {
    return (Timer)userTimerRegistry.get("Timer-"+individualId);
  }
  
  public synchronized final TimerTask getUserTask(int individualId)
  {
    return (TimerTask)userTimerRegistry.get("TimerTask-"+individualId);
  }

  /**
   * @param individualId the individual to register the timer under.
   * @param userTimer the new timer to set.
   */
  public synchronized final void setUserTimer(int individualId, Timer userTimer, TimerTask userTask)
  {
    this.userTimerRegistry.put("Timer-"+individualId, userTimer);
    this.userTimerRegistry.put("TimerTask-"+individualId,userTask);
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("SiteInfo {dataSource: "+this.dataSource);
    buffer.append(", licenseInstance: "+licenseInstance);
    buffer.append("}");
    return buffer.toString();
  }

  public int getUserCount()
  {
    return this.userCount;
  }

  public void setUserCount(int userCount)
  {
    this.userCount = userCount;
  }
}

