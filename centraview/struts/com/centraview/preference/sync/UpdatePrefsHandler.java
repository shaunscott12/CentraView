/*
 * $RCSfile: UpdatePrefsHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:31 $ - $Author: mking_cv $
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

package com.centraview.preference.sync;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.preference.Preference;
import com.centraview.preference.PreferenceHome;
import com.centraview.settings.Settings;

/**
 * Handles the request for /preference/sync/UpdatePrefs.do which
 * updates the "syncAsPrivate" user preference for the logged in
 * user.
 */
public class UpdatePrefsHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = "showSyncPrefs";
    
    // "syncPreferencesForm", defined in struts-config-preference.xml
    DynaActionForm syncForm = (DynaActionForm)form;

    try
    {
      Boolean syncAsPrivate = (Boolean)syncForm.get("syncAsPrivate");
      String prefValue = (syncAsPrivate != null && syncAsPrivate.booleanValue() == true) ? "YES" : "NO";

      PreferenceHome home = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
      Preference remote = home.create();
      remote.setDataSource(dataSource);

      int recordsChanged = remote.updateSyncAsPrivatePref(individualID, prefValue);

      // if we successfully updated the preference in the database, now
      // we need to update the preference in the user's UserPrefererence
      // object in the UserObject in the session.
      if (recordsChanged > 0)
      {
        userObject.getUserPref().setSyncAsPrivate(prefValue);
      }
    }catch(Exception e){
      System.out.println("[Exception][UpdatePrefsHandler] Exception thrown in execute(): " + e);
      // e.printStackTrace();
		}
    return(mapping.findForward(forward));
  }   // end execute() method
  
}   // end class definition

