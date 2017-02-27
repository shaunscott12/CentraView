/*
 * $RCSfile: UpdateAutoCheckHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.preference.mail;

import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.preference.Preference;
import com.centraview.preference.PreferenceHome;
import com.centraview.preference.PreferenceVO;
import com.centraview.settings.Settings;

/**
 * Handles the request for /preference/mail/UpdateAutoCheck.do which
 * updates the user's preference for how often mail should be
 * automatically checked, and forwards back to /preference/mail/AutoCheck.do
 */
public class UpdateAutoCheckHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = ".forward.preference.mail.auto_check_settings";

    // "mailCheckForm" defined in struts-config-preference.xml
    DynaActionForm mailForm = (DynaActionForm)form;
    
    try {
      Integer newValue = (Integer)mailForm.get("mailCheckInterval");
      if (newValue != null) {
        PreferenceVO prefVO = new PreferenceVO();
        
        prefVO.setModuleId(2);
        prefVO.setPreferenceName("emailCheckInterval");
        prefVO.setPreferenceValue(newValue.toString());
        
        Vector prefsVector = new Vector();
        prefsVector.addElement(prefVO);
        
        PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
        Preference prefRemote = prefHome.create();
        prefRemote.setDataSource(dataSource);
        
        prefRemote.updateUserPreference(individualID, prefsVector);
        
        UserPrefererences userPrefs = userObject.getUserPref();
        userPrefs.setEmailCheckInterval(newValue.intValue());
        userObject.setUserPref(userPrefs);
        session.setAttribute("userobject", userObject);
      }
    } catch (Exception e) {
      System.out.println("[Exception][UpdateAutoCheckHandler] Exception thrown in execute(): " + e);
    }
    return mapping.findForward(forward);
  }   // end execute() method

}   // end class definition

