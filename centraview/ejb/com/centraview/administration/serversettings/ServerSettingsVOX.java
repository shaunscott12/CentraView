/*
 * $RCSfile: ServerSettingsVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:47 $ - $Author: mking_cv $
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

package com.centraview.administration.serversettings;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;

/**
 * ValueObject for serversettings storing data values of settings
 * to be put in DataBase.
 *
 * @author   Sandeep Joshi
 */
public class ServerSettingsVOX extends ServerSettingsVO
{
  public ServerSettingsVOX()
  {
    super();
  }

  public ServerSettingsVOX(ActionForm actionForm)
  {
    DynaActionForm form = (DynaActionForm) actionForm;
    String temp = (String)form.get("sessionTimeout");
    if (temp != null || !temp.equals(""))
    {
      this.setSessionTimeout(Integer.parseInt(temp));
    }
    temp = (String)form.get("emailCheckInterval");
    if (temp != null || temp.equals(""))
    {
      this.setEmailCheckInterval(Integer.parseInt(temp));
    }

    this.setFileSystemStoragePath((String)form.get("fileSystemStoragePath"));
    this.setDefaultTimeZone((String)form.get("defaultTimeZone"));
    String StartTime, EndTime = null;
    StartTime = (String)form.get("detailStartTime");
    EndTime = (String)form.get("detailEndTime");
    if (StartTime == null)
    {
      StartTime = "0:00 AM";
    }
    else if (StartTime.length() == 0)
    {
      StartTime = "0:00 AM";
    }
    int startHrsmm[] = CVUtility.convertTimeTo24HrsFormat(StartTime);
    int startHrs = startHrsmm[0];
    int startMins = startHrsmm[1];
    if (EndTime == null)
    {
      EndTime = "0:00 AM";
    }
    else if (EndTime.length() == 0)
    {
      EndTime = "0:00 AM";
    }
    int endHrsmm[] = CVUtility.convertTimeTo24HrsFormat(EndTime);
    int endHrs = endHrsmm[0];
    int endMins = endHrsmm[1];
    this.setWorkingHoursFrom(new java.sql.Time(startHrs, startMins, 0));
    this.setWorkingHoursTo(new java.sql.Time(endHrs, endMins, 0));

  }

  public ServerSettingsVO getVO()
  {
    return super.getVO();
  }

}