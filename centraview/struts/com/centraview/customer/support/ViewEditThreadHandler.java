/*
 * $RCSfile: ViewEditThreadHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:30 $ - $Author: mking_cv $
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

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;
import com.centraview.support.ticket.ThreadVO;
import com.centraview.support.ticket.TicketForm;
import com.centraview.support.ticket.TicketVO;


public class ViewEditThreadHandler extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_vieweditthread = ".view.customer.view_thread";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession();
      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      SupportFacadeHome sfh = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = (SupportFacade)sfh.create();
      remote.setDataSource(dataSource);
      ThreadVO tVO = null;
      
      if (request.getParameter("rowId") != null) {
        tVO = remote.getThread(individualId, Integer.parseInt(request.getParameter("rowId")));
      } else {
        tVO = remote.getThread(individualId, Integer.parseInt(request.getAttribute("rowId").toString()));
      }

      if (tVO != null) {
        TicketVO ticketVO = remote.getTicketBasicRelations(individualId, tVO.getTicketId());

        if (ticketVO != null) {
          TicketForm ticketForm = new TicketForm();
          ticketForm.setSubject(ticketVO.getTitle());
          ticketForm.setDetail(ticketVO.getDetail());
          ticketForm.setId(new Integer(ticketVO.getId()).toString());
          ticketForm.setEntityid(new Integer(ticketVO.getRefEntityId()).toString());
          ticketForm.setContactid(new Integer(ticketVO.getRefIndividualId()).toString());
          ticketForm.setContact(ticketVO.getRefIndividualName());
          
          EntityVO entityVO = ticketVO.getEntityVO();
          if (entityVO != null) {
            String entityName = entityVO.getName();
            ticketForm.setEntityname(entityName);
            
            AddressVO primaryAdd = entityVO.getPrimaryAddress();
            
            if (primaryAdd != null) {
              String address = "";
              if (primaryAdd.getStreet1() != null && !primaryAdd.getStreet1().equals("")) {
                address += primaryAdd.getStreet1();
              }
              if (primaryAdd.getStreet2() != null && !primaryAdd.getStreet2().equals("")) {
                address +=", "+ primaryAdd.getStreet2()+"\n";
              } else {
                address += "\n";
              }
              if (primaryAdd.getCity() != null && !primaryAdd.getCity().equals("")) {
                address += primaryAdd.getCity();
              }
              if (primaryAdd.getStateName() != null && !primaryAdd.getStateName().equals("")) {
                address += ", "+ primaryAdd.getStateName();
              }
              if (primaryAdd.getZip() != null && !primaryAdd.getZip().equals("")) {
                address += " "+ primaryAdd.getZip();
              }
              if (primaryAdd.getCountryName() != null && !primaryAdd.getCountryName().equals("")) {
                address += ", "+ primaryAdd.getCountryName();
              }
              ticketForm.setAddress(address);
              if (primaryAdd.getWebsite() != null) {
                ticketForm.setWebsite(primaryAdd.getWebsite());
              }
            }
            
            Collection mocList = entityVO.getMOC();
            Iterator iterator = mocList.iterator();
            while (iterator.hasNext()) {
              MethodOfContactVO moc  = (MethodOfContactVO) iterator.next();
              if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) {
                ticketForm.setEmail(moc.getContent());
              } else if (moc.getMocType() == 4) {
                ticketForm.setPhone(moc.getContent());
              }
            }
          }
          request.setAttribute("ticketform", ticketForm);
          
          DynaActionForm dynaForm = (DynaActionForm)form;
          
          if (tVO != null) {
            dynaForm.set("threadid", new Integer(tVO.getId()).toString());
            dynaForm.set("ticketnumber", new Integer(ticketVO.getId()).toString());
            dynaForm.set("title", tVO.getTitle());
            dynaForm.set("threaddetail", tVO.getDetail());
            dynaForm.set("priority", new Integer(tVO.getPriorityId()).toString());

            if (tVO.getCreatedByVO() != null) {
              dynaForm.set("createdby", tVO.getCreatedByVO().getFirstName() + " " + tVO.getCreatedByVO().getLastName());
            }
            dynaForm.set("createdbyid", new Integer(tVO.getCreatedBy()).toString());
            dynaForm.set("createddate", tVO.getCreatedOn().toString());

            if (tVO.getModifiedOn() != null) {
              dynaForm.set("modifieddate", tVO.getModifiedOn().toString());
            }
          }
          request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE, "Ticket");
          FORWARD_final = FORWARD_vieweditthread;
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][ViewEditThreadHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
  
}

