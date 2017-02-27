/*
 * $RCSfile: ReadLicense.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:04 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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

import com.centraview.license.LicenseUtil;

/**
 * This class reads the license from the filesystem.  It implements Job
 * and is called by the quartz scheduler according to the Trigger setup
 * in the SettingsPlugin.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ReadLicense extends TimerTask
{
  private static Logger logger = Logger.getLogger(ReadLicense.class);
  private String dataSource = null;
  public ReadLicense(String dataSource)
  {
    this.dataSource = dataSource;
  }
  
  public void run()
  {
    logger.debug("Executing ReadLicense TimerTask.");
    LicenseUtil.getLicenseFile(this.dataSource);
  }
}
