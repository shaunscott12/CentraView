/*
 * $RCSfile: SettingsInterface.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:46 $ - $Author: mking_cv $
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

import java.util.Timer;
import java.util.TimerTask;

import javax.naming.InitialContext;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public interface SettingsInterface 
{
  /**
   * @return initCtx the InitialContext created from the properties in CentraView.properties
   */
  public abstract InitialContext getInitCtx();
  /**
   * @param context This should only be set from the SettingsPlugin.
   */
  public abstract void setInitCtx(InitialContext context);
  /**
   * @return Returns the MACAddress.
   */
  public abstract String getMACAddress();
  /**
   * @param address The MACAddress to set.
   */
  public abstract void setMACAddress(String address);
  public abstract void setSiteInfo(String siteName, SiteInfo siteInfo);
  public abstract SiteInfo getSiteInfo(String siteName);
  public abstract void setSupportEmailTimer(Timer supportEmailTimer);
  public abstract void setSupportEmailTask(TimerTask suportTask);
  public abstract Timer getSupportEmailTimer();
  public abstract TimerTask getSupportEmailTask();

}

