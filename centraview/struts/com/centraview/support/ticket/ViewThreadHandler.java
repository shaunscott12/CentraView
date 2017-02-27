/*
 * $RCSfile: ViewThreadHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:55 $ - $Author: mking_cv $
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

import java.util.Collection;
import java.util.Iterator;

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
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.settings.Settings;
/**
 * This handler is used to add new thread entry.
 *
 * @author Prasanta Sinha
 */
public class ViewThreadHandler extends Action
{
  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** To forward to jsp add_thread_c.jsp. */
  private static final String FORWARD_editthread = "viewthread";

  /** Redirect constant. */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;


  /** Default Constructor. */
  public ViewThreadHandler()
  {
    //empty constructor body
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
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      TicketHome ticketHome = (TicketHome)
        CVUtility.getHomeObject("com.centraview.support.ticket.TicketHome", "Ticket");
      Ticket ticketRemote = ticketHome.create();
      ticketRemote.setDataSource(dataSource);

      //TODO: We need to remove this hard-coded value
      ThreadVO tVO = ticketRemote.getThread(individualID, 1);

      DynaActionForm dynaForm = (DynaActionForm) form;
      dynaForm.set("title", tVO.getTitle());
      dynaForm.set("detail", tVO.getDetail());

      //dynaForm.set("ticketnumber",new Integer(tVO.getId()).toString());
      request.setAttribute("threadform", dynaForm);
      TicketForm ticketForm = new TicketForm();

      //TODO: We need to remove this hard-coded value
      TicketVO ticketVO = ticketRemote.getTicketBasicRelations(individualID, 14);
      ticketForm.setSubject(ticketVO.getTitle());
      ticketForm.setDetail(ticketVO.getDetail());
      ticketForm.setId(new Integer(ticketVO.getId()).toString());


	  ticketForm.setEntityid(new Integer(ticketVO.getRefEntityId()).toString());
	  ticketForm.setContactid(new Integer(ticketVO.getRefIndividualId()).toString());

	  ticketForm.setContact(ticketVO.getRefIndividualName());
	  EntityVO entityVO = ticketVO.getEntityVO();
	  if (entityVO != null)
	  {
		String entityName = entityVO.getName();
		ticketForm.setEntityname(entityName);
		Collection mocList = entityVO.getMOC();
		Iterator iterator = mocList.iterator();
		while (iterator.hasNext())
		{
			MethodOfContactVO moc  = (MethodOfContactVO) iterator.next();
			if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) // this is for email
			{
				ticketForm.setEmail(moc.getContent());
			} //end of if statement (moc.getMocType() == 1)
			else if (moc.getMocType() == 4)
			{
				ticketForm.setPhone(moc.getContent());
			} //end of else statement (moc.getMocType() == 1)
		}// end of while
	  }

      request.setAttribute("ticketform", ticketForm);
      FORWARD_final = FORWARD_editthread;
    }

    // exception handling
    catch (Exception e)
    {
      System.out.println("[Exception] ViewThreadHandler.execute: " + e.toString());
      //e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
