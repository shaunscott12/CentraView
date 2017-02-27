/*
 * $RCSfile: SaveCalendarHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/02 14:21:26 $ - $Author: mking_cv $
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
/**
	* DisplayCalendarHandler.java
* @version 	1.0
* @author Atul Jaysingpure
* 21 Nov 2003 
*/

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
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

/**
* This class gets home page settings from remote objects. 
* The current logged in user's preferred settingds,
* like refresh rate, view options etc.
*/

public class SaveCalendarHandler extends Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_save = "save";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "";
    try
    {

      DynaActionForm dynaForm = (DynaActionForm)form;

      HttpSession session = request.getSession(true);

      String typeOfSave = "save";
      int individualID = 0;
      UserObject userObject = (UserObject)session.getAttribute("userobject");

	  UserPrefererences userPrefererences = userObject.getUserPref();

      // no need to check for null on userobjectd here, because if it is null
      // we should fail now! instead of making an EJB call with bogus info.     
      individualID = userObject.getIndividualID();

      if (request.getParameter("buttonpress") != null)
      {
        typeOfSave = request.getParameter("buttonpress");
        String minutes = (String) (dynaForm.get("minutes"));
        String seconds = (String) (dynaForm.get("seconds"));
        Vector vec = new Vector();
        PreferenceVO pvo1 = new PreferenceVO();
        PreferenceVO pvo2 = new PreferenceVO();
        PreferenceVO pvo3 = new PreferenceVO();
        pvo1.setModuleId(4);
        pvo1.setPreferenceName("calendarrefreshmin");
        pvo1.setPreferenceValue(minutes);
        pvo2.setModuleId(4);
        pvo2.setPreferenceName("calendarrefreshsec");
        pvo2.setPreferenceValue(seconds);
        pvo3.setModuleId(4);
        pvo3.setPreferenceName("caldefaultview");
        pvo3.setPreferenceValue((String) (dynaForm.get("radio")));

        if(minutes != null && !minutes.equals("") && !minutes.equals("null")
            	&& seconds != null && !seconds.equals("") && !seconds.equals("null"))
        {
    	  	  userPrefererences.setCalendarRefreshMin(minutes);
    		  userPrefererences.setCalendarRefreshSec(seconds);
    		  userObject.setUserPref(userPrefererences);
    		  session.setAttribute("userobject",userObject);
        }
        
        if(dynaForm.get("radio") != null)
        {
        	userPrefererences.setCalendarDefaultView((String) dynaForm.get("radio"));
        	userObject.setUserPref(userPrefererences);
        	session.setAttribute("userobject",userObject);
        }
        
        vec.addElement(pvo1);
        vec.addElement(pvo2);
        vec.addElement(pvo3);
        PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
        Preference prefRemote = prefHome.create();
        prefRemote.setDataSource(dataSource);
        prefRemote.updateUserPreference(individualID, vec);
        FORWARD_final = FORWARD_save;
        request.setAttribute("TYPEOFOPERATION", "EDIT");
        session.setAttribute("userobject", userObject);

      }

      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "CALENDAR");
      FORWARD_final = FORWARD_save;
    }
    catch (Exception e)
    {
      System.out.println("[Exception][SaveCalendarHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      returnStatus = "failure";
    }
    return (mapping.findForward(FORWARD_final));
  }
}