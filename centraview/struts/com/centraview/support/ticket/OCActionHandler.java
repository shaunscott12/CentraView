/*
 * $RCSfile: OCActionHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/05 19:25:55 $ - $Author: mcallist $
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

package com.centraview.support.ticket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * This Struts Action handler is used to close or re-open a support ticket. It
 * is linked to the "Close" or "Re-open" button at the top of the ticket details
 * screen.
 * @author CentraView, LLC.
 */
public class OCActionHandler extends Action {
  private static Logger logger = Logger.getLogger(OCActionHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   * Serves as the handler for the Ticket detail screen "Close Ticket" and
   * "Re-open Ticket" buttons
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      String ticketStatus = request.getParameter("closeorreopen");
      int ticketID = Integer.parseInt(request.getParameter("ticketID"));
      SupportFacadeHome supFacadeHome = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
          "SupportFacade");
      SupportFacade supFacadeRemote = supFacadeHome.create();
      supFacadeRemote.setDataSource(dataSource);
      if (ticketStatus.equals(TicketConstantKeys.TK_OCSTATUS_CLOSE)) {
        supFacadeRemote.closeTicket(individualID, ticketID);
        request.setAttribute("OCStatus", TicketConstantKeys.TK_OCSTATUS_CLOSE);
      } else if (ticketStatus.equals(TicketConstantKeys.TK_OCSTATUS_OPEN)) {
        supFacadeRemote.reopenTicket(individualID, ticketID);
        request.setAttribute("OCStatus", TicketConstantKeys.TK_OCSTATUS_OPEN);
      }
      request.setAttribute("ticketID", new Integer(ticketID));
      request.setAttribute("refreshWindow", "true");
      FORWARD_final = ".view.support.ticket_detail";
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    } // end try block
    return mapping.findForward(FORWARD_final);
  } // end execute() method
}
