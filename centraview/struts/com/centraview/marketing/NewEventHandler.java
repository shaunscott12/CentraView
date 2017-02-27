/*
 * $RCSfile: NewEventHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;


public class NewEventHandler extends org.apache.struts.action.Action
{
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_newevents = ".view.marketing.new.event";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
    * Standard Struts Action class execute method, handles login requests for
    * the application. Either passes control forwarding to Home page, or
    * forwards to Login page with errors.
    *
    * @param mapping Struts ActionMapping passed from servlet
    * @param form Struts ActionForm passed in from servlet
    * @param request Struts HttpServletRequest passed in from servlet
    * @param response Struts HttpServletResponse passed in from servlet
    *
    * @return ActionForward: returns the ActionForward to the servlet controller
    *         to tell Struts where to pass control to.
    */
  public ActionForward execute(ActionMapping mapping, ActionForm form,    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try
    {
      HttpSession session = request.getSession(true);
      request.setAttribute("TypeOfOperation", "Event");
      //have this so an empty list shows up at the bottom.
      DynaActionForm dynaForm = (DynaActionForm) form;
      dynaForm.initialize(mapping);

      UserObject userObject = (UserObject)session.getAttribute("userobject");

      int individualId = userObject.getIndividualID();

      ListPreference listpreference = userObject.getListPreference("EventAtendees");
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      int records = listpreference.getRecordsPerPage();
      String sortelement = listpreference.getSortElement();

			EventAtendeesList dl = new EventAtendeesList();
			dl.setStartAT(1);
			dl.setEndAT(records);
			dl.setSortType('A');
			dl.setListType("EventAtendees");

      request.setAttribute("displaylist", dl);
      session.setAttribute("displaylist", dl);
      session.removeAttribute("AttachfileList");
      FORWARD_final = FORWARD_newevents;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] NewEventHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    return mapping.findForward(FORWARD_final);
  }
}
