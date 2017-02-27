/*
 * $RCSfile: SaveNewEventHandler.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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

import java.sql.Timestamp;
import java.util.HashMap;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DateUtility;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class SaveNewEventHandler extends Action {
  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** Local Forwards for redirecting to jsp. */
  private static final String FORWARD_SHOWCLOSE = ".view.marketing.events.list";
  private static final String FORWARD_SHOWNEW = ".view.marketing.new.event";
  private static final String FORWARD_SHOWSAVE = ".view.marketing.edit.event";
  private static Logger logger = Logger.getLogger(SaveNewEventHandler.class);

  /** Redirect constant. */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   * Executes initialization of required parameters and open window for entry of
   * events returns ActionForward
   * @throws NamingException
   * @throws CommunicationException
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws CommunicationException, NamingException
  {
    HttpSession session = request.getSession(true);
    int eventid = this.saveEvent(form, request);

    ((DynaActionForm)form).set("eventid", eventid + "");
    request.setAttribute("eventid", eventid + "");

    if (request.getParameter("closeornew") != null
        && request.getParameter("closeornew").equals("close")) {
      FORWARD_final = FORWARD_SHOWCLOSE;
    } else if (request.getParameter("closeornew") != null
        && request.getParameter("closeornew").equals("new")) {
      FORWARD_final = FORWARD_SHOWNEW;
    } else if (request.getParameter("closeornew") != null
        && request.getParameter("closeornew").equals("save")) {

      String attendeeLookup = request.getParameter("FromAttendee").toString();
      if (attendeeLookup != null && attendeeLookup.equals("YES")) {
        request.setAttribute("attendeeLookup", "YES");
      }
      FORWARD_final = FORWARD_SHOWSAVE;
    }
    session.removeAttribute("AttachfileList");
    return mapping.findForward(FORWARD_final);
  }

  public int saveEvent(ActionForm form, HttpServletRequest request) throws CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int eventid = 0;
    MarketingFacadeHome mfh = (MarketingFacadeHome)CVUtility.getHomeObject(
        "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
    try {
      DynaActionForm dynaForm = (DynaActionForm)form;
      String strEventid = (String)dynaForm.get("eventid");
      String strName = (String)dynaForm.get("name");
      String strDescription = (String)dynaForm.get("description");
      String strWhoShouldAttend = (String)dynaForm.get("whoshouldattend");
      String strMaxAttendee = "0";

      if ((dynaForm.get("maxattendees") != null) && (!(dynaForm.get("maxattendees").equals("")))) {
        strMaxAttendee = (String)dynaForm.get("maxattendees");
      }

      String strModerator = (String)dynaForm.get("moderatorid");

      // initialize hashmap
      HashMap mapEvent = new HashMap();
      mapEvent.put("Name", strName);
      mapEvent.put("Description", strDescription);
      mapEvent.put("WhoShouldAttend", strWhoShouldAttend);
      mapEvent.put("MaxAttendees", strMaxAttendee);
      mapEvent.put("Moderator", strModerator);

      if ((dynaForm.get("startmonth") != null) && (dynaForm.get("startday") != null)
          && (dynaForm.get("startyear") != null)) {
        String startmonth = (String)dynaForm.get("startmonth");
        String startday = (String)dynaForm.get("startday");
        String startyear = (String)dynaForm.get("startyear");
        String startTime = (String)dynaForm.get("starttime");

        try {
          Timestamp start = DateUtility.createTimestamp(startyear, startmonth, startday, startTime);
          mapEvent.put("StartDate", start);
        } catch (Exception e) {
          logger.error("[execute] SaveNewEventHandler saveEvent thrown.", e);
        }// end of catch block
      }// end of if ((dynaForm.get("startmonth") != null)...

      if ((dynaForm.get("endmonth") != null) && (dynaForm.get("endday") != null)
          && (dynaForm.get("endyear") != null)) {
        String endmonth = (String)dynaForm.get("endmonth");
        String endday = (String)dynaForm.get("endday");
        String endyear = (String)dynaForm.get("endyear");
        String endTime = (String)dynaForm.get("endtime");

        try {
          Timestamp end = DateUtility.createTimestamp(endyear, endmonth, endday, endTime);
          mapEvent.put("EndDate", end);
        } catch (Exception e) {
          logger.error("[execute] SaveNewEventHandler saveEvent thrown.", e);
        }// end of catch block
      }// end of if ((dynaForm.get("endmonth") != null) ....

      HttpSession session = request.getSession();
      int individualID = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      mapEvent.put("Creator", "" + individualID);

      // call to marketing facade
      MarketingFacade remote = mfh.create();
      remote.setDataSource(dataSource);

      String[] attchmentids = request.getParameterValues("attachfile");
      mapEvent.put("Attachment", attchmentids);

      eventid = remote.addEvent(mapEvent, individualID);
      request.setAttribute("TypeOfOperation", "Event");
    }// end of try block
    catch (Exception exe) {
      logger.error("[execute] SaveNewEventHandler saveEvent thrown.", exe);
    }// end of catch block
    return eventid;
  }
}
