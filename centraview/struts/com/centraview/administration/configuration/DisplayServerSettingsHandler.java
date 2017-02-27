/*
 * $RCSfile: DisplayServerSettingsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:39 $ - $Author: mking_cv $
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

package com.centraview.administration.configuration;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.serversettings.ServerSettings;
import com.centraview.administration.serversettings.ServerSettingsHome;
import com.centraview.administration.serversettings.ServerSettingsVOX;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DisplayServerSettingsHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_page = ".view.administration.server_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  private static Logger logger = Logger.getLogger(DisplayServerSettingsHandler.class);

  public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      
      DynaActionForm dynaForm = (DynaActionForm)form;
      
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      
      ServerSettingsHome lh = (ServerSettingsHome)CVUtility.getHomeObject("com.centraview.administration.serversettings.ServerSettingsHome","ServerSettings");
      ServerSettings remote = (ServerSettings)lh.create();
      remote.setDataSource(dataSource);
      
      ServerSettingsVOX svo = remote.getServerSettings();
      
      dynaForm = setForm(dynaForm, svo);
      
      request.setAttribute("serverSettingsForm" , dynaForm);
      request.setAttribute("typeofmodule", AdministrationConstantKeys.CONFIGURATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.SERVERSETTINGS);
      request.setAttribute(AdministrationConstantKeys.TYPEOFSUBMODULE, AdministrationConstantKeys.SERVERSETTINGS);
      request.setAttribute(AdministrationConstantKeys.TYPEOFMODULE, AdministrationConstantKeys.CONFIGURATION);
      
      FORWARD_final = FORWARD_page;
    } catch (Exception e) {
      logger.error("[Exception] DisplayServerSettingsHandler.Execute Handler ", e);
    }
    return (mapping.findForward(FORWARD_final));
  }   // end exceute() method

  private DynaActionForm setForm(DynaActionForm form,ServerSettingsVOX svo)
  {
    int startHrs = 0;
    int startMins = 0;
    int endHrs = 0;
    int endMins = 0;
    Time tStartTime, tEndTime = null;

    form.set("hostName", svo.getHostName());
    form.set("sessionTimeout", String.valueOf(svo.getSessionTimeout()));
    form.set("emailCheckInterval", String.valueOf(svo.getEmailCheckInterval()));
    form.set("fileSystemStoragePath", svo.getFileSystemStoragePath());
    form.set("defaultTimeZone", svo.getDefaultTimeZone());

    tStartTime = svo.getWorkingHoursFrom();
    tEndTime = svo.getWorkingHoursTo();

    if (tStartTime != null) {
      Calendar calendarStart = Calendar.getInstance();
      calendarStart.setTime(tStartTime);
      startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);
      startMins = calendarStart.get(Calendar.MINUTE);
      DateFormat df= new SimpleDateFormat("h:mm a");
      form.set("detailStartTime",(df.format(tStartTime)).toString());
    }
    
    if (tEndTime != null) {
      Calendar calendarEnd = Calendar.getInstance();
      calendarEnd.setTime(tEndTime);
      endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);
      endMins = calendarEnd.get(Calendar.MINUTE);
      DateFormat df= new SimpleDateFormat("h:mm a");
      form.set("detailEndTime",( df.format(tEndTime)).toString());
    }
    return form;
  }

}

