/*
 * $RCSfile: EventRegisterHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.customer.events;

import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

/**
 * Handles the request for the Customer View Event Details
 * screen. Displays Event only, no modification allowed.
 */
public class EventRegisterHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "customerEventDetail";

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerEventForm", defined in cv-struts-config.xml
    DynaActionForm eventForm = (DynaActionForm)form;

    try {
      // get the event ID from the form bean
      Integer formEventID = (Integer)eventForm.get("eventID");
      // create an int to hold the event ID value
      int eventID = 0;

      // now, check the event ID on the form...
      if (formEventID == null) {
        // if event ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Event ID"));
        return(mapping.findForward(forward));
      }else{
        // if event ID is set on the form properly, then set
        // the int representation for use in the code below
        eventID = formEventID.intValue();
      }
      
      MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = home.create();
      remote.setDataSource(dataSource);

      Boolean registerAction = (Boolean)eventForm.get("registerAction");
      
      HashMap mapEvent = new HashMap();
      
      if (registerAction.booleanValue() == true) {
        boolean accepted = remote.hasUserAcceptedEvent(eventID, individualID);

        if (! accepted) {
          mapEvent.put("eventid", String.valueOf(eventID));
          mapEvent.put("individualid", String.valueOf(individualID));
          mapEvent.put("accepted", "YES");
        }
      }else{
        mapEvent.put("eventid", String.valueOf(eventID));
        mapEvent.put("individualid", String.valueOf(individualID));
        mapEvent.put("accepted", "NO");
      }
      
      int registrationID = remote.registerAttendee(mapEvent);
      
    }catch(Exception e){
      System.out.println("[Exception][CV ViewEventHandler] Exception thrown in execute(): " + e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

