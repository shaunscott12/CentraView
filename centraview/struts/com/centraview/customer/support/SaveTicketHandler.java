/*
 * $RCSfile: SaveTicketHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:29 $ - $Author: mking_cv $
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

package com.centraview.customer.support;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;
import com.centraview.support.ticket.TicketForm;
import com.centraview.support.ticket.TicketVO;

public class SaveTicketHandler extends org.apache.struts.action.Action
{
  final String GLOBAL_FORWARD_failure = "failure";
  final String FORWARD_editsavefile = ".forward.customer.ticket_list";
  public String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession();
      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      TicketVO ticketVO = new TicketVO();
      TicketForm ticketForm = (TicketForm)form;

      SupportFacadeHome supportFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = (SupportFacade)supportFacade.create();
      remote.setDataSource(dataSource);

      if (ticketForm.getSubject() != null) {
        ticketVO.setTitle(ticketForm.getSubject());
      }
      
      if (ticketForm.getDetail() != null) {
        ticketVO.setDetail(ticketForm.getDetail());
      }

      if (ticketForm.getPriority() != null) {
        ticketVO.setPriorityId(Integer.parseInt(ticketForm.getPriority()));
      }else{
        ticketVO.setPriorityId(Integer.parseInt(request.getParameter("priority")));
      }
      
      if (ticketForm.getStatus() != null) {
        ticketVO.setStatusId(Integer.parseInt(ticketForm.getStatus()));
      }

      if ((ticketForm.getManagerid() != null) && (!ticketForm.getManagerid().equals(""))) {
        ticketVO.setManagerId(Integer.parseInt(ticketForm.getManagerid()));
      }
      
      if ((ticketForm.getAssignedtoid() != null) && (!ticketForm.getAssignedtoid().equals(""))) {
        ticketVO.setAssignedToId(Integer.parseInt(ticketForm.getAssignedtoid()));
      }

      if ((ticketForm.getEntityid() != null) && (!ticketForm.getEntityid().equals(""))) {
        ticketVO.setRefEntityId(Integer.parseInt(ticketForm.getEntityid()));
      }

      if ((ticketForm.getContactid() != null) && (!ticketForm.getContactid().equals(""))) {
        ticketVO.setRefIndividualId(Integer.parseInt(ticketForm.getContactid()));
      }
      ticketVO.setCreatedBy(individualId);
      ticketVO.setCustomField(getCustomFieldVO(request, response));

      remote.addTicket(individualId, ticketVO);

      FORWARD_final = FORWARD_editsavefile;
    } catch (Exception e) {
      System.out.println("[Exception][SaveTicketHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

  public Vector getCustomFieldVO(HttpServletRequest request, HttpServletResponse response)
  {
    Vector vec = new Vector();

    for (int i = 1; i < 4; i++) {
      String fieldid = request.getParameter("fieldid" + i);
      String fieldType = request.getParameter("fieldType" + i);
      String textValue = request.getParameter("text" + i);

      if (fieldid == null) {
        fieldid = "0";
      }
      int intfieldId = Integer.parseInt(fieldid);
      CustomFieldVO cfvo = new CustomFieldVO();
      cfvo.setFieldID(intfieldId);
      cfvo.setFieldType(fieldType);
      cfvo.setValue(textValue);

      if (intfieldId != 0) {
        vec.add(cfvo);
      }
    }
    return vec;
  } // end of getCustomFieldVO
}
