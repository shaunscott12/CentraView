/*
 * $RCSfile: DisplaySyncHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;

/**
 * Handles the request for /preference/sync/DisplaySync.do which
 * gets the logged in user's "syncAsPrivate" preference and displays
 * that setting to the end user. The user can then change that
 * setting via the /preference/sync/UpdatePrefs.do Action.
 */
public class DisplaySyncHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String forward = ".view.preference.sync_prefs";
    
    try {
      HttpSession session = request.getSession();

      // get the user's sync prefs from the EJB layer
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      UserPrefererences userPrefs = userObject.getUserPref();
      String prefValue = (String)userPrefs.getSyncAsPrivate();
      Boolean syncAsPrivate = (prefValue != null && prefValue.equals("YES")) ? new Boolean(true) : new Boolean(false);

      // populate the syncPreferencesForm bean
      DynaActionForm syncPrefsForm = (DynaActionForm)form;
      syncPrefsForm.set("syncAsPrivate", syncAsPrivate);
		}catch(Exception e){
      System.out.println("[Exception][DisplaySyncHandler] execute(): " + e);
    }
    return (mapping.findForward(forward));
  }   // end execute() method
  
}   // end class definition

