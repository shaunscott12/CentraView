/*
 * $RCSfile: RegisterEventHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:18 $ - $Author: mking_cv $
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

package com.centraview.marketing;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class RegisterEventHandler extends org.apache.struts.action.Action
{

  // Global Forwards

  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_displayViewEvent = ".view.marketing.edit.event";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   * Creates a new RegisterEventHandler object.
   */
  public RegisterEventHandler()
  {
    // Not Implemented
  }

  /**
   * Standard Struts Action class execute method, handles login requests
   * for the application. Either passes control forwarding to Home page,
   * or forwards to Login page with errors.
   *
   * @param mapping   Struts ActionMapping passed from servlet
   * @param form      Struts ActionForm passed in from servlet
   * @param request   Struts HttpServletRequest passed in from servlet
   * @param response  Struts HttpServletResponse passed in from servlet
   *
   * @return  ActionForward: returns the ActionForward to the servlet controller to tell Struts where to pass control to.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualid = userobjectd.getIndividualID();
      int eventid = Integer.parseInt(request.getParameter("eventid").toString());

      HashMap mapEvent = new HashMap();
      mapEvent.put("eventid", "" + eventid);
      mapEvent.put("individualid", "" + individualid);

      MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = home.create();
      remote.setDataSource(dataSource);
      boolean isRegistered = remote.hasUserAcceptedEvent(eventid, individualid);

      String newAcceptedString = (!isRegistered) ? "YES" : "NO";

      mapEvent.put("accepted", newAcceptedString);

      remote.registerAttendee(mapEvent);

      request.setAttribute("eventid", "" + eventid);

      FORWARD_final = FORWARD_displayViewEvent;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] RegisterEventHandler.execute: " + e.toString());
      //e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
