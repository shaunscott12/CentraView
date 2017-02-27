/*
 * $RCSfile: DisplayHomeSettingsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:22 $ - $Author: mking_cv $
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

package com.centraview.preference;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

/**
 * This class gets home page settings from remote objects. 
 * The current logged in user's preferred settingds,
 * like refresh rate, view options etc.
 */
public class DisplayHomeSettingsHandler extends Action
{

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_homesettingsprofile = ".view.preference.home_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "";
    try
    {
      DynaActionForm dynaForm = (DynaActionForm)form;
      HttpSession session = request.getSession();
      session.setAttribute("highlightmodule", "preferences");

      int individualID = 0;
      UserObject userObject = (UserObject)session.getAttribute("userobject");

      if (userObject != null)
      {
        // get current user's individualID
        individualID = userObject.getIndividualID();
      }

      Vector vecleft = new Vector();
      Vector vecright = new Vector();

      PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
      Preference prefRemote = prefHome.create();
      prefRemote.setDataSource(dataSource);
      UserPrefererences up = prefRemote.getUserPreferences(individualID);

      if (up.getEmail().equals("YES"))
        vecright.addElement(new DDNameValue("email", AdminConstantKeys.EMAIL));
      else
        vecleft.addElement(new DDNameValue("email", AdminConstantKeys.EMAIL));
      if (up.getTodaysCalendar().equals("YES"))
        vecright.addElement(new DDNameValue("todayscalendar", AdminConstantKeys.TODAYSCALENDAR));
      else
        vecleft.addElement(new DDNameValue("todayscalendar", AdminConstantKeys.TODAYSCALENDAR));
      if (up.getUnscheduledActivities().equals("YES"))
        vecright.addElement(new DDNameValue("unscheduledactivities", AdminConstantKeys.UNSCHEDULEDACTIVITIES));
      else
        vecleft.addElement(new DDNameValue("unscheduledactivities", AdminConstantKeys.UNSCHEDULEDACTIVITIES));
      if (up.getScheduledOpportunities().equals("YES"))
        vecright.addElement(new DDNameValue("scheduledopportunities", AdminConstantKeys.SCHEDULEDOPPORTUNUITIES));
      else
        vecleft.addElement(new DDNameValue("scheduledopportunities", AdminConstantKeys.SCHEDULEDOPPORTUNUITIES));
      if (up.getProjectTasks().equals("YES"))
        vecright.addElement(new DDNameValue("projecttasks", AdminConstantKeys.PROJECTTASKS));
      else
        vecleft.addElement(new DDNameValue("projecttasks", AdminConstantKeys.PROJECTTASKS));
      if (up.getSupportTickets().equals("YES"))
        vecright.addElement(new DDNameValue("supporttickets", AdminConstantKeys.SUPPORTTICKETS));
      else
        vecleft.addElement(new DDNameValue("supporttickets", AdminConstantKeys.SUPPORTTICKETS));
      if (up.getCompanyNews().equals("YES"))
        vecright.addElement(new DDNameValue("companynews", AdminConstantKeys.COMPANYNEWS));
      else
        vecleft.addElement(new DDNameValue("companynews", AdminConstantKeys.COMPANYNEWS));

      request.setAttribute("vecleft", vecleft);
      request.setAttribute("vecright", vecright);
      dynaForm.set("minutes", up.getHomeRefreshMin());
      dynaForm.set("seconds", up.getHomeRefreshSec());

      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "HOME");
      FORWARD_final = FORWARD_homesettingsprofile;
    }
    catch (Exception e)
    {
      System.out.println("[Exception][DisplayHomeSettingsHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      returnStatus = "failure";
    }
    return (mapping.findForward(FORWARD_final));
  }
}
