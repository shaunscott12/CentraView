/*
 * $RCSfile: ViewCalSettingsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:48 $ - $Author: mking_cv $
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

package com.centraview.administration.modulesettings;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class ViewCalSettingsHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String submodule = request.getParameter("sourcefor");
      String setting = request.getParameter("setting");

      if (submodule == null) {
        submodule = "";
      }

      DynaActionForm dynaform = (DynaActionForm)form;

      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
      AppSettings appRemote = appHome.create();
      appRemote.setDataSource(dataSource);

      HashMap hm = (HashMap)appRemote.getCalendarSettings();

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
      Time startTime = (Time)hm.get("starttime");
      Time endTime = (Time)hm.get("endtime");
      
      try {
        dynaform.set("startTime", simpleDateFormat.format(startTime));
      } catch (Exception e) {
        dynaform.set("startTime", "8:00 AM");        
      }
      
      try {
        dynaform.set("endTime", simpleDateFormat.format(endTime));
      } catch (Exception e) {
        dynaform.set("endTime", "5:00 PM");        
      }
      
      dynaform.set("workingdays", hm.get("workingdays").toString());
      request.setAttribute("dynaform", dynaform);
      request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
      request.setAttribute("typeofsubmodule", submodule);
      request.setAttribute("settingfor", setting);
    } catch (Exception e) {
      System.out.println("[Exception] ViewCalSettingsHandler.execute: " + e.toString());
      e.printStackTrace();
    }

    return mapping.findForward(".view.administration.calendar_settings");
  }
}
