/*
 * $RCSfile: SaveCalSettingsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:46 $ - $Author: mking_cv $
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
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveCalSettingsHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.administration.calendar_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
  private static final String FORWARD_save = ".view.administration.calendar_settings";
  private static final String FORWARD_saveandclose = ".view.administration.module_settings";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String returnStatus = "";
      String button = request.getParameter("buttonpress").toString();
      String setting = request.getParameter("setting").toString();

      String submodule = request.getParameter("submodule").toString();
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();
      DynaActionForm dynaform = (DynaActionForm) form;

      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
      AppSettings appRemote = appHome.create();
      appRemote.setDataSource(dataSource);
      
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
      Time startTime;
      Time endTime;
      try {
        Date tempDate = simpleDateFormat.parse(dynaform.get("startTime").toString());
        startTime = new Time(tempDate.getTime());
      } catch (Exception e) {
        Date tempDate = simpleDateFormat.parse("8:00 AM");
        startTime = new Time(tempDate.getTime());
      }
      
      try {
        Date tempDate = simpleDateFormat.parse(dynaform.get("endTime").toString());
        endTime = new Time(tempDate.getTime());
      } catch (Exception e) {
        Date tempDate = simpleDateFormat.parse("5:00 PM");
        endTime = new Time(tempDate.getTime());
      }
      
      appRemote.updateCalendarSettings(startTime.toString(), endTime.toString(),  dynaform.get("workingdays").toString());

      if (button.equals("save")) {
        returnStatus = "save";
        FORWARD_final = FORWARD_save;
      } else if (button.equals("saveandclose")) {
        returnStatus = "saveandclose";
        request.setAttribute("typeofmodule",  AdministrationConstantKeys.MODULESETTINGS);
        request.setAttribute("typeofsubmodule", submodule);
        FORWARD_final = FORWARD_saveandclose;
      }
    } catch (Exception e) {
      System.out.println("[Exception] SaveCalSettingsHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }

    return mapping.findForward(FORWARD_final);
  }
}
