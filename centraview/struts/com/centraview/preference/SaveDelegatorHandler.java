/*
 * $RCSfile: SaveDelegatorHandler.java,v $    $Revision: 1.4 $  $Date: 2005/09/01 15:31:04 $ - $Author: mcallist $
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

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

public class SaveDelegatorHandler extends Action
{

  public static String globalForward = "failure";
  public static String successForward = ".forward.preference.mail.delegation_settings";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);
      UserObject userObj = (UserObject)session.getAttribute("userobject");

      int individualId = userObj.getIndividualID();

      String typeofmodule = (String)request.getParameter("TYPEOFMODULE");

      if (typeofmodule == null) {
    	  typeofmodule = (String)request.getAttribute("TYPEOFMODULE");
      }
      if (typeofmodule == null) {
    	  typeofmodule = Constants.EMAILMODULE;
      }
      
      String moduleName = "";
      if (typeofmodule != null && typeofmodule.equals(Constants.ACTIVITYMODULE))
      {
    	  successForward = ".forward.preference.calendar.delegation_settings";
      }
      else if (typeofmodule != null && typeofmodule.equals(Constants.EMAILMODULE))
      {
    	  successForward = ".forward.preference.mail.delegation_settings";
      }
      
      String send[] = null;
      String schedule[] = null;
      String viewschedule[] = null;
      String view[] = null;
      Vector vecSend = new Vector();
      Vector vecSchedule = new Vector();
      Vector vecViewSchedule = new Vector();
      Vector vecView = new Vector();
      
      if ((String[])request.getParameterValues("send") != null) {
        send = (String[])request.getParameterValues("send");
        for (int i = 0; i < send.length; i++) {
          vecSend.addElement(new Integer(send[i]));
        }
      }

      if ((String[])request.getParameterValues("schedule") != null) {
    	  schedule = (String[])request.getParameterValues("schedule");
          for (int i = 0; i < schedule.length; i++) {
		   	System.out.println("schedule[i]"+schedule[i]);
          	vecSchedule.addElement(new Integer(schedule[i]));
          }
      }

      if ((String[])request.getParameterValues("view") != null) {
    	  view = (String[])request.getParameterValues("view");
          System.out.println("view[i]"+view);
          for (int i = 0; i < view.length; i++) {
          	System.out.println("view[i]"+view[i]);
          	vecView.addElement(new Integer(view[i]));
          }
      }
      
      if ((String[])request.getParameterValues("viewschedule") != null) {
    	  viewschedule = (String[])request.getParameterValues("viewschedule");
          System.out.println("viewschedule[i]"+viewschedule);
          for (int i = 0; i < viewschedule.length; i++) {
          	System.out.println("viewschedule[i]"+viewschedule[i]);
          	vecViewSchedule.addElement(new Integer(viewschedule[i]));
          }
      }      
      HashMap hm = new HashMap();

      PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
      Preference prefRemote = (Preference)prefHome.create();
      prefRemote.setDataSource(dataSource);
      
      if (typeofmodule != null)
      {
        if (typeofmodule.equals(Constants.ACTIVITYMODULE))
        {
          hm.put(Constants.VIEWSCHEDULEACTIVITY, vecViewSchedule);
          hm.put(Constants.VIEW, vecView);
          hm.put(Constants.SCHEDULEACTIVITY, vecSchedule);
          moduleName = Constants.ACTIVITYMODULE;
          prefRemote.updateUserDelegation(individualId, moduleName, hm);
        }
        else if (typeofmodule.equals(Constants.EMAILMODULE))
        {
            hm.put(Constants.SENDEMAIL, vecSend);
            moduleName = Constants.EMAILMODULE;
            prefRemote.updateEmailDelegation(individualId, vecSend);
        }
      }

      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "ACTIVITYDELEGATION");
      request.setAttribute("TYPEOFMODULE", typeofmodule);
      globalForward = successForward;
    }
    catch (Exception e)
    {
      System.out.println("Error in SaveDelegatorHandler " + e);
      e.printStackTrace();
    }
    return mapping.findForward(globalForward);
  }
}