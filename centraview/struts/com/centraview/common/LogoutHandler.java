/*
 * $RCSfile: LogoutHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:13 $ - $Author: mking_cv $
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

package com.centraview.common;

import java.util.Timer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.settings.Settings;
import com.centraview.settings.SiteInfo;


public class LogoutHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(LogoutHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException,Exception
  {
    SiteInfo siteInfo = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext()));
    try 
    {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      
      int individualID = userObject.getIndividualID();    // logged in user
      Timer userTimer = siteInfo.getUserTimer(individualID);
      if (userTimer != null)
      {
        userTimer.cancel();
      }
      if (session != null) 
      {
        session.invalidate();
      }
    } catch(java.lang.IllegalStateException e) {
      // ok, if an exception occurs, there's nothing we can really do
      // so just print an error to the log and redirect to the login page
      logger.error("[execute] Exception thrown.", e);
    }
    return (mapping.findForward(".view.login"));
  }   // end execute() method
}   // end class defintion

