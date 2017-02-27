/*
 * $RCSfile: DisplayEmailComposition.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:22 $ - $Author: mking_cv $
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
public class DisplayEmailComposition extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_emailcompositionpage = ".view.preference.mail.compose_settings";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "";
    
    try {
      HttpSession session = request.getSession();
      session.setAttribute("highlightmodule", "preference");

      PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
      Preference prefRemote = prefHome.create();
      prefRemote.setDataSource(dataSource);
      
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      
      int individualID = 0;

      if (userObject != null) {
        individualID = userObject.getIndividualID();
      }
      
      DynaActionForm composeForm = (DynaActionForm)form;

      UserPrefererences userPref = prefRemote.getUserPreferences(individualID);
      request.setAttribute("emailComposition", userPref.getContentType());

      composeForm.set("composeType", userPref.getContentType());

      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "EMAILCOMPOSITION");
      FORWARD_final = FORWARD_emailcompositionpage;
    } catch (Exception e) {
      System.out.println("[Exception][DisplayEmailComposition.execute] Exception Thrown: " + e);
      e.printStackTrace();
      returnStatus = "failure";
    }
    return mapping.findForward(FORWARD_final);
  }
}
