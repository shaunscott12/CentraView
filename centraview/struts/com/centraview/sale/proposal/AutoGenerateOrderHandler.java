/*
 * $RCSfile: AutoGenerateOrderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:43 $ - $Author: mking_cv $
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
package com.centraview.sale.proposal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.order.OrderForm;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

public class AutoGenerateOrderHandler extends Action
{
  private static Logger logger = Logger.getLogger(AutoGenerateOrderHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,  HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject) session.getAttribute("userobject");
    int individualID =userObject.getIndividualID();
    ProposalListForm proposallistform = (ProposalListForm) form;

    OrderForm of = new OrderForm();

    int proposalID = 0;

    try {
      proposalID = Integer.parseInt(request.getParameter("eventid"));
    } catch (Exception ex) {
      proposalID = Integer.parseInt(proposallistform.getProposalid());
    }
    String entityid = request.getParameter("entityID");

    int entityID = 0;
    if (entityid != null) {
      entityID = Integer.parseInt(entityid);
    } else {
      entityID = proposallistform.getEntityID();
    }

    AccountFacadeHome afh = (AccountFacadeHome)  CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
    SaleFacadeHome sfh = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome","SaleFacade");
    
    try  {
      SaleFacade saleRemote =(SaleFacade) sfh.create();
      saleRemote.setDataSource(dataSource);
      
      AccountFacade orderRemote = (AccountFacade) afh.create();
      orderRemote.setDataSource(dataSource);
      HashMap hm  = (HashMap) saleRemote.viewProposal(individualID, proposalID , proposallistform);
      ItemLines itemLines = (ItemLines)hm.get("itemLines");
      proposallistform = (ProposalListForm) hm.get("dyna");
      
      com.centraview.account.common.ItemLines accItemLines = new com.centraview.account.common.ItemLines();
      
      if (itemLines != null) {
        Set s = itemLines.keySet();
        Iterator itr = s.iterator();
        int id = 0;
        int counter = 0;
        
        while (itr.hasNext()) {
          ItemElement ie2 = (ItemElement)itemLines.get(itr.next());
          IntMember LineId = (IntMember)ie2.get("LineId");
          IntMember ItemId = (IntMember)ie2.get("ItemId");
          StringMember SKU = (StringMember)ie2.get("SKU");
          StringMember Description = (StringMember)ie2.get("Description");
          IntMember Quantity = (IntMember)ie2.get("Quantity");
          FloatMember PriceEach = (FloatMember)ie2.get("Price");
          FloatMember PriceExtended = (FloatMember)ie2.get("PriceExtended");
          FloatMember taxAmount = (FloatMember)ie2.get("TaxAmount");
          com.centraview.account.common.ItemElement ie = new com.centraview.account.common.ItemElement(counter);
          ie.put("LineId", LineId);
          ie.put("ItemId", ItemId);
          ie.put("SKU", SKU);
          ie.put("Description", Description);
          ie.put("Quantity", Quantity);
          ie.put("Price", PriceEach);
          ie.put("PriceExtended", PriceExtended);
          ie.put("TaxAmount", taxAmount);
          accItemLines.put(new Integer(counter), ie);
          counter++;
        }
      }
      
      of.setItemLines(accItemLines);
      
      int billingID = 0;
      int shippingID = 0;
      if (proposallistform.getBillingaddressid() != null && !proposallistform.getBillingaddressid().equals("")) {
        billingID = Integer.parseInt(proposallistform.getBillingaddressid());
      }
      
      if (proposallistform.getShippingaddressid() != null && !proposallistform.getShippingaddressid().equals("")) {
        shippingID = Integer.parseInt(proposallistform.getShippingaddressid());
      }
      
      of.setShipToAddIdValue(shippingID);
      of.setBillToAddIdValue(billingID);
      of.setCustomerIdValue(entityID);
      of.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
      of.setJurisdictionID(new Integer(proposallistform.getJurisdictionID()));
      of.setNotes(proposallistform.getProposal() + "-" + proposallistform.getProdescription());
      
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector statusVector = null;
      statusVector = (Vector)gml.get("AccountingStatus");
      
      for (int i = 0; i < statusVector.size(); i++) {
        if (((DDNameValue) statusVector.get(i)).getName().equals("Pending")) {
          of.setStatusIdValue(((DDNameValue) statusVector.get(i)).getId());
          break;
        }
      }
      
      // hardcoded to today() as orderDate will be today when order is created
      // set after calling convert.. as that method sets date from day month yr var.s
      // and that date will be null
      OrderForm createdForm = orderRemote.createOrder(of, individualID);
      int orderID = createdForm.getOrderIdValue();
      request.setAttribute("GeneratedOrderId",new Integer(orderID));
      
      if (orderID != 0) {
        saleRemote.setOrderIsGenerated(true,proposalID,orderID);
      }
    } catch (Exception e) {
      logger.error("[Exception] AutoGenerateOrderHandler.Execute Handler ", e);
    }
    // even if this is failure or success ViewProposal only is to be called
    // so keeping that result in request and calling with single fwd no matter whats the case
    return mapping.findForward(".view.proposal.editproposal");
  }

}

