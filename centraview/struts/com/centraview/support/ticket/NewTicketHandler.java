/*
 * $RCSfile: NewTicketHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:52 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NewTicketHandler extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_newticket = ".view.support.new_ticket";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      HttpSession session = request.getSession();
      TicketForm ticketForm = (TicketForm)form;
      
      if (request.getParameter("entityName") != null) {
        ticketForm.setEntityname((String)request.getParameter("entityName"));
      }
      
      if (request.getParameter("entityID") != null) {
        ticketForm.setEntityid((String)request.getParameter("entityID"));
      }
      
      if (request.getParameter("individualname") != null) {
        ticketForm.setContact((String)request.getParameter("individualname"));
      }
      
      if (request.getParameter("individualid") != null) {
        ticketForm.setContactid((String)request.getParameter("individualid"));
      }
      
      request.setAttribute("ticketform",ticketForm);
      FORWARD_final = FORWARD_newticket;
    }catch(Exception e){
      System.out.println("[Exception][NewTicketHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;    
    }
    return mapping.findForward(FORWARD_final);
  }  
  
}

