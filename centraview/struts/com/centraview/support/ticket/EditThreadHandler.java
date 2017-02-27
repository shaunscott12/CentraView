/*
 * $RCSfile: EditThreadHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:51 $ - $Author: mking_cv $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * EditThreadHandler is used to edit thread entry.
 *
 * @author : sandip Wadkar
 */
public class EditThreadHandler extends Action
{
  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** To forward to jsp. */
  private static final String FORWARD_saveeditthread = "saveeditthread";

  /** To forward to jsp call list handler. */
  private static final String FORWARD_savecloseeditthread = "savecloseeditthread";

  /** To forward to jsp call list handler. */
  private static final String FORWARD_canceleditthread = "canceleditthread";

  /** Redirect constant. */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;


  /** Default Constructor. */
  public EditThreadHandler()
  {
    // emptyconstructor body
  }

  /**
   *  Executes initialization of required parameters and open window for entry of thread
   *  returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      if (request.getParameter("saveType").equals("close"))
      {
        request.setAttribute("bodycontent", null);
        FORWARD_final = FORWARD_canceleditthread;

        return mapping.findForward(FORWARD_final);
      }

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      ThreadVO threadVO = new ThreadVO();
      threadVO.setTitle(((ThreadForm) form).getTitle());
      threadVO.setDetail(((ThreadForm) form).getDetail());
      threadVO.setPriorityId(((ThreadForm) form).getPriorityId());
      threadVO.setPriorityName(((ThreadForm) form).getPriorityName());

      if (request.getParameter("TICKETID") != null)
      {
        threadVO.setTicketId(Integer.parseInt(request.getParameter("TICKETID")));
      }

      threadVO.setCreatedBy(individualID);
      threadVO.setOwner(individualID);

      // pass vo to ejb layer
      TicketHome ticketHome = (TicketHome) 
        CVUtility.getHomeObject("com.centraview.support.ticket.TicketHome", "Ticket");
      Ticket ticketRemote = ticketHome.create();
      ticketRemote.setDataSource(dataSource);
      ticketRemote.updateThread(individualID, threadVO);

      // set attribute back to jsp pages
      if (request.getParameter("saveType").equals("save"))
      {
        request.setAttribute("bodycontent", "editthread");
        FORWARD_final = FORWARD_saveeditthread;
      }
      else if (request.getParameter("saveType").equals("saveclose"))
      {
        request.setAttribute("bodycontent", null);
        FORWARD_final = FORWARD_savecloseeditthread;
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception] EditThreadHandler.execute: " + e.toString());
      //e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
