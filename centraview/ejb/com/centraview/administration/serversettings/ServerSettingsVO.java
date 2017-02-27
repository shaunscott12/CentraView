/*
 * $RCSfile: ServerSettingsVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:47 $ - $Author: mking_cv $
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

/*
 * ServerSettingsVO.java
 *
 * @author   Sandeep Joshi
 * @version  1.0
 *
 */

package com.centraview.administration.serversettings;

import java.io.Serializable;
import java.sql.Time;
/**
* ValueObject for serversettings storing data values of settings
* to be put in DataBase
*/
public class ServerSettingsVO implements Serializable
{
  private String hostName;
  private int sessionTimeout;
  private Time workingHoursFrom;
  private Time workingHoursTo;
  private int emailCheckInterval;
  private String fileSystemStoragePath;
  private String defaultTimeZone;

  public ServerSettingsVO getVO()
  {
    return this;
  }

  public String getDefaultTimeZone()
  {
    return this.defaultTimeZone;
  }

  public void setDefaultTimeZone(String defaultTimeZone)
  {
    this.defaultTimeZone = defaultTimeZone;
  }

  public int getEmailCheckInterval()
  {
    return this.emailCheckInterval;
  }

  public void setEmailCheckInterval(int emailCheckInterval)
  {
    this.emailCheckInterval = emailCheckInterval;
  }

  public String getFileSystemStoragePath()
  {
    return this.fileSystemStoragePath;
  }

  public void setFileSystemStoragePath(String fileSystemStoragePath)
  {
    this.fileSystemStoragePath = fileSystemStoragePath;
  }

  public String getHostName()
  {
    return this.hostName;
  }

  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  public int getSessionTimeout()
  {
    return this.sessionTimeout;
  }

  public void setSessionTimeout(int sessionTimeout)
  {
    this.sessionTimeout = sessionTimeout;
  }

  public Time getWorkingHoursFrom()
  {
    return this.workingHoursFrom;
  }

  public void setWorkingHoursFrom(Time workingHoursFrom)
  {
    this.workingHoursFrom = workingHoursFrom;
  }

  public Time getWorkingHoursTo()
  {
    return this.workingHoursTo;
  }

  public void setWorkingHoursTo(Time workingHoursTo)
  {
    this.workingHoursTo = workingHoursTo;
  }

}
