/*
 * $RCSfile: SaveHomeSettingsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:24 $ - $Author: mking_cv $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

public class SaveHomeSettingsHandler extends org.apache.struts.action.Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_save = "save";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      DynaActionForm dynaForm = (DynaActionForm)form;
      HttpSession session = request.getSession();
	  UserObject userobject =(UserObject) session.getAttribute("userobject"); 
	  UserPrefererences userPrefererences = userobject.getUserPref();
      
      String typeOfSave = "save";
      int individualID = 0;
      String[] leftportlet = (String[])request.getParameterValues("portletsLeft");
      String[] rightportlet = (String[])request.getParameterValues("portletsRight");
      Vector vec = new Vector();

      if (rightportlet != null)
      {
        for (int i = 0; i < rightportlet.length; i++)
        {
          PreferenceVO pvo3 = new PreferenceVO();
          pvo3.setModuleId(4);
          pvo3.setPreferenceName(rightportlet[i]);
          pvo3.setPreferenceValue("YES");

          vec.addElement(pvo3);

        }
      }
      if (leftportlet != null)
      {

        for (int i = 0; i < leftportlet.length; i++)
        {
          PreferenceVO pvo4 = new PreferenceVO();
          pvo4.setModuleId(4);
          pvo4.setPreferenceName(leftportlet[i]);
          pvo4.setPreferenceValue("NO");

          vec.addElement(pvo4);
        }
      }
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      // might as well null pointer now before we do any real harm
      individualID = userObject.getIndividualID();
      if (request.getParameter("buttonpress") != null)
      {
        typeOfSave = request.getParameter("buttonpress");

        PreferenceVO pvo1 = new PreferenceVO();
        PreferenceVO pvo2 = new PreferenceVO();

        pvo1.setModuleId(4);
        pvo1.setPreferenceName("homesettingrefreshmin");
        String minutes = (String) (dynaForm.get("minutes"));
        pvo1.setPreferenceValue(minutes);
        pvo2.setModuleId(4);
        pvo2.setPreferenceName("homesettingrefreshsec");
        String seconds = (String) (dynaForm.get("seconds"));
        pvo2.setPreferenceValue(seconds);
        vec.addElement(pvo1);
        vec.addElement(pvo2);
        
        PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
        Preference prefRemote = prefHome.create();
        prefRemote.setDataSource(dataSource);
        prefRemote.updateUserPreference(individualID, vec);

        updatePrefs(prefRemote.getUserPreferences(individualID),
                    userObject.getUserPref());

        if(minutes != null && !minutes.equals("") && !minutes.equals("null")
            	&& seconds != null && !seconds.equals("") && !seconds.equals("null"))
        {
    	  	  userPrefererences.setHomeRefreshMin(minutes);
    		  userPrefererences.setHomeRefreshSec(seconds);
    		  userobject.setUserPref(userPrefererences);
    		  session.setAttribute("userobject",userobject);
        }
        
        FORWARD_final = FORWARD_save;
        request.setAttribute("TYPEOFOPERATION", "EDIT");
        session.setAttribute("userobject", userObject);

      }

      request.setAttribute("TYPEOFSUBMODULE", AdminConstantKeys.PREFERENCEPAGE);
    }
    catch (Exception e)
    {
      System.out.println("[Exception][SaveHomeSettingsHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }

    return (mapping.findForward(FORWARD_final));
  }

  private void updatePrefs(UserPrefererences source, UserPrefererences dest) {
     if (source != null && dest != null) {
        dest.setEmail(source.getEmail());
        dest.setTodaysCalendar(source.getTodaysCalendar());
        dest.setUnscheduledActivities(source.getUnscheduledActivities());
        dest.setScheduledOpportunities(source.getScheduledOpportunities());
        dest.setProjectTasks(source.getProjectTasks());
        dest.setSupportTickets(source.getSupportTickets());
        dest.setCompanyNews(source.getCompanyNews());
}
  }
}
