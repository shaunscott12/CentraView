/*
 * $RCSfile: EditTicketHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/05 19:25:55 $ - $Author: mcallist $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class EditTicketHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(EditTicketHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  public static final String FORWARD_editsavefile = ".view.support.ticket_detail";
  String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      TicketVO ticketVO = new TicketVO();
      TicketForm ticketForm = (TicketForm) form;

      SupportFacadeHome supportFacade = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
          "SupportFacade");
      SupportFacade remote = supportFacade.create();
      remote.setDataSource(dataSource);

      ticketVO.setId(Integer.parseInt(ticketForm.getId()));
      ticketVO.setTitle(ticketForm.getSubject());
      ticketVO.setDetail(ticketForm.getDetail());
      ticketVO.setPriorityId(Integer.parseInt(ticketForm.getPriority()));
      ticketVO.setStatusId(Integer.parseInt(ticketForm.getStatus()));

      if (ticketForm.getId() != null) {
        int ticketID = Integer.parseInt(ticketForm.getId());
        int statusID = ticketVO.getStatusId();
        String closeTicketParam = request.getParameter("closeTicket");
        // If the Status select was set to 2 (MAGIC NUMBER for closed) or
        // closeTicket=true was appended to the request then ...
        boolean closeTicket = ((statusID == 2) || (closeTicketParam != null && closeTicketParam.equals("true")));
        if (closeTicket) {
          ticketVO.setStatusId(2);
          remote.closeTicket(individualID, ticketID);
          request.setAttribute("OCStatus", TicketConstantKeys.TK_OCSTATUS_CLOSE);
        }
      }

      if ((ticketForm.getManagerid() != null) || (ticketForm.getManagerid().length() != 0)) {
        if (!(ticketForm.getManagerid().equals(""))) {
          ticketVO.setManagerId(Integer.parseInt(ticketForm.getManagerid()));
        }
      }

      if ((ticketForm.getAssignedtoid() != null) || (ticketForm.getAssignedtoid().length() != 0)) {
        if (!(ticketForm.getAssignedtoid().equals(""))) {
          ticketVO.setAssignedToId(Integer.parseInt(ticketForm.getAssignedtoid()));
        }
      }

      if ((ticketForm.getEntityid() != null) || (ticketForm.getEntityid().length() != 0)) {
        if (!(ticketForm.getEntityid().equals(""))) {
          ticketVO.setRefEntityId(Integer.parseInt(ticketForm.getEntityid()));
        }
      }

      if ((ticketForm.getContactid() != null) || (ticketForm.getContactid().length() != 0)) {
        if (!(ticketForm.getContactid().equals(""))) {
          ticketVO.setRefIndividualId(Integer.parseInt(ticketForm.getContactid()));
        }
      }

      ticketVO.setModifiedBy(individualID);
      ticketVO.setCustomField(getCustomFieldVO(request));
      String operationType = request.getParameter("TYPEOFOPERATION");

      if (operationType.equalsIgnoreCase("EDIT")) {
        remote.updateTicket(individualID, ticketVO);
      } else if (operationType.equalsIgnoreCase("DUPLICATE")) {
        remote.duplicateTicket(individualID, ticketVO);
      }
      FORWARD_final = FORWARD_editsavefile;
      String closeornew = request.getParameter("closeornew");

      if (closeornew.equals("close")) {
        FORWARD_final = "closeWindow";
      } else if (closeornew.equals("new")) {
        ticketForm.setSubject("");
        ticketForm.setDetail("");
        ticketForm.setEntityname("");
        ticketForm.setAssignedto("");
        ticketForm.setContact("");
        ticketForm.setManagername("");
        FORWARD_final = ".view.support.new_ticket";
      }
      request.setAttribute("refreshWindow", "true");
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

  public Vector getCustomFieldVO(HttpServletRequest request)
  {
    Vector vec = new Vector();
    String sizeOfCustomField = request.getParameter("sizeOfCustomField");
    int endCount = 4;

    // For the Ticket Module. We must have to Set all the Custom Fields.
    // Collecting the Number of CustomFields.
    if (sizeOfCustomField != null && !(sizeOfCustomField.equals(""))) {
      endCount = Integer.parseInt(sizeOfCustomField);
    }
    for (int i = 1; i < endCount; i++) {
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
  } // end getCustomFieldVO() method
} // end class definition

