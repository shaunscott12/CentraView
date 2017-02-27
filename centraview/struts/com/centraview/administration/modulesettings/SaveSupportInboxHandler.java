/*
 * $RCSfile: SaveSupportInboxHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:47 $ - $Author: mking_cv $
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

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.common.CVUtility;
import com.centraview.jobs.SupportEmailCheck;
import com.centraview.settings.Settings;
import com.centraview.settings.SettingsInterface;
import com.centraview.support.helper.SupportHelper;
import com.centraview.support.helper.SupportHelperHome;

public class SaveSupportInboxHandler extends Action
{
  private static Logger logger = Logger.getLogger(SaveSupportInboxHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    ActionErrors requiredFieldErrors = new ActionErrors();
    DynaActionForm dynaForm = (DynaActionForm)form;

    try {
      String submodule = request.getParameter("sourcefor");
      String setting = request.getParameter("setting");
      String ownerid = request.getParameter("employeeID");

      String send[] = new String[100];
      Vector vecSend = new Vector();

      if ((String[])request.getParameterValues("viewsend") != null) {
        send = (String[])request.getParameterValues("viewsend");
        for (int i = 0; i < send.length; i++) {
          if (send[i] != null && !send[i].equals("")) {
            vecSend.addElement(new Integer(send[i]));
          }
        }
      }

      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
      AppSettings appRemote = (AppSettings)appHome.create();
      appRemote.setDataSource(dataSource);

      appRemote.addSupportMailId(vecSend);

      HashMap supportMessages = new HashMap();

      appRemote.updateApplicationSettings("DEFAULTOWNER", ownerid);

      // Now handle the "emailCheckInterval" setting...
      {
        Integer formInterval = (Integer)dynaForm.get("emailCheckInterval");
        // call EJB support helper to update DB
        SupportHelperHome supportHome = (SupportHelperHome)CVUtility.getHomeObject("com.centraview.support.helper.SupportHelperHome", "SupportHelper");
        try {
          // this updates the "supportEmailCheckInterval"
          // setting in the "systemsettings" table
          SupportHelper supportRemote = (SupportHelper)supportHome.create();
          supportRemote.setDataSource(dataSource);
          supportRemote.setSupportEmailCheckInterval(formInterval.intValue());
        } catch (Exception e) {
          // "Oh we're not gonna take it... No, we ain't gonna take it... Oh we're not gonna take it anymore..."
        }
        SettingsInterface settings = Settings.getInstance();
        // re-schedule JOB because it is currently running and
        // we have changed the interval at which it executes
        TimerTask supportEmailTask = settings.getSupportEmailTask();
        Timer supportEmailTimer = settings.getSupportEmailTimer();
        // cancel the existing timer.
        if (supportEmailTask != null) {
          supportEmailTask.cancel();
        }
        if (supportEmailTimer != null) {
          supportEmailTimer.cancel();
        }
        // only re-schedule the job if the interval is greater than zero
        // a zero value means do not check support email ever
        if (formInterval.intValue() > 0) {
          // minutes to seconds, seconds to miliseconds
          Integer interval = new Integer(formInterval.intValue() * 60 * 1000);
          // then create a new Timer.
          Timer newSupportEmailTimer = new Timer(true);
          // wait a full two minutes for the next execution.
          TimerTask newSupportEmailTask = new SupportEmailCheck(dataSource);
          newSupportEmailTimer.schedule(newSupportEmailTask, 120000L, interval.longValue());
          // And store our reference.
          settings.setSupportEmailTimer(newSupportEmailTimer);
          settings.setSupportEmailTask(newSupportEmailTask);
        }
      } // end of "emailCheckInterval" setting

    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    return mapping.findForward(".view.administration.support_inbox");
  }
}
