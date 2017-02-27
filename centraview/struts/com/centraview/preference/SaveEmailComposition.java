/*
 * $RCSfile: SaveEmailComposition.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:04 $ - $Author: mcallist $
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
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

public class SaveEmailComposition extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.preference.mail.compose_settings";
  private static final String FORWARD_save = ".view.preference.mail.compose_settings";

  private static String FORWARD_final = FORWARD_save;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession(true);

      DynaActionForm dynaForm = (DynaActionForm)form;

      String composeType = (String)dynaForm.get("composeType");
      if (composeType.equalsIgnoreCase("HTML")) {
        session.setAttribute(Constants.EMAILTYPEFLAG, "HTML");
      } else {
        session.setAttribute(Constants.EMAILTYPEFLAG, "PLAIN");
      }

      int individualID = 0;
      UserObject userObject = (UserObject)session.getAttribute("userobject");

      individualID = userObject.getIndividualID();

      Vector vec = new Vector();
      PreferenceVO pvo1 = new PreferenceVO();

      pvo1.setModuleId(4);
      pvo1.setPreferenceName("contenttype");
      pvo1.setPreferenceValue(composeType);

      vec.addElement(pvo1);
      PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
      Preference prefRemote = prefHome.create();
      prefRemote.setDataSource(dataSource);
      prefRemote.updateUserPreference(individualID, vec);
      
      request.setAttribute("emailComposition", composeType);
      
      UserPrefererences userPref= userObject.getUserPref();
      userPref.setContentType(composeType);
      userObject.setUserPref(userPref);
      session.setAttribute("userobject", userObject);
      
      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "EMAILCOMPOSITION");
    } catch (Exception e) {
      System.out.println("[Exception][SaveEmailComposition.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
