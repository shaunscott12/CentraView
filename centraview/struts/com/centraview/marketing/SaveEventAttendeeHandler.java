/*
 * $RCSfile: SaveEventAttendeeHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:18 $ - $Author: mking_cv $
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
import java.util.StringTokenizer;

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
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class SaveEventAttendeeHandler extends Action
{
   
  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** Local Forwards for redirecting to jsp. */
  private static final String FORWARD_displayAddAttendee = ".view.marketing.edit.event";

  /** Redirect constant. */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   *  Executes initialization of required parameters and open window for entry of events
   *  returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      int eventid = this.saveEventRegister(form, request);

      if ((request.getAttribute("eventid") != null) &&
          (((String) request.getAttribute("eventid")).length() > 0))
      {
        eventid = Integer.parseInt(request.getAttribute("eventid").toString());
      }
      else if ((request.getParameter("eventid") != null) &&
          (request.getParameter("eventid").length() > 0))
      {
        eventid = Integer.parseInt(request.getParameter("eventid").toString());
      }

      //request.setAttribute("eventid",""+eventid);
      DynaActionForm dynaForm = (DynaActionForm) form;
      dynaForm.initialize(mapping);

      /*ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Event");*/
      //return (mapping.findForward("addattendees"));
      FORWARD_final = FORWARD_displayAddAttendee;

      //FORWARD_final = FORWARD_displayevent;
      request.setAttribute("eventid", "" + eventid);
      request.setAttribute("TypeOfOperation", "Event");
      request.setAttribute("refreshWindow", "true");
    }
    catch (Exception e)
    {
      System.out.println("[Exception] SaveEventAttendeeHandler.execute: " + e.toString());
      //e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

  public int saveEventRegister(ActionForm form, HttpServletRequest request)
  {
    int eventid = 0;
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try
    {
      DynaActionForm dynaForm = (DynaActionForm) form;

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      String strEventid = (String) dynaForm.get("eventid");
      strEventid = (String) request.getParameter("eventid");

      String strIndividualId = (String) request.getParameter("Individualid");

      if (request.getParameter("Individualid") != null)
      {
        strIndividualId = (String) request.getParameter("Individualid");
      }
      else
      {
        strIndividualId = (String) request.getParameter("Individualid");
      }

      StringTokenizer st = new StringTokenizer(strIndividualId, ",");

      String[] strIndividuals = new String[st.countTokens()];
      int count = 0;

      while (st.hasMoreTokens())
      {
        strIndividuals[count] = st.nextToken();
        count++;
      }

      // call ejb to insert record
      // initialize hashmap
      HashMap mapEvent = new HashMap();

      mapEvent.put("EventId", strEventid);
      mapEvent.put("Accepted", "NO");
      mapEvent.put("IndividualId", strIndividuals);

      // call to marketing facade
      MarketingFacadeHome mfh = (MarketingFacadeHome) 
        CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade) mfh.create();
      remote.setDataSource(dataSource);

      // add record to db
      eventid = remote.addEventRegister(mapEvent, individualID);
    }
    catch (Exception exe)
    {
      System.out.println("[Exception] SaveEventAttendeeHandler.saveEventRegister: " + exe.toString());
      //exe.printStackTrace();
    }
    return eventid;
  }
}
