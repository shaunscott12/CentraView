/*
 * $RCSfile: AutoGenerateInvoiceHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:20 $ - $Author: mking_cv $
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

package com.centraview.account.order;

import java.io.IOException;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.invoice.InvoiceForm;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class AutoGenerateInvoiceHandler  extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_editorder = ".view.accounting.new_invoice";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  private static Logger logger = Logger.getLogger(AutoGenerateInvoiceHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
    
    try {
      String listId = (String)request.getAttribute("listId");
      HttpSession session = request.getSession(true);
      UserObject  userobject = (UserObject)session.getAttribute("userobject");
      int individualID = userobject.getIndividualID();
      
      AccountFacade remote = (AccountFacade)accountFacadeHome.create();
      remote.setDataSource(dataSource);
      
      Vector taxJurisdiction = remote.getTaxJurisdiction();
      
      String OrderID = (String)request.getParameter("OrderID");
      int  orderId  = 0;
      if (OrderID != null) {
        orderId = Integer.parseInt(OrderID);
      }

      OrderForm of = remote.getOrderForm(orderId, individualID);
      of.convertValueObjectToFormbean();

      InvoiceForm invoiceform = (InvoiceForm)form;
      invoiceform.convertItemLines();

      invoiceform.setOrderid(orderId + "");
      invoiceform.setCustomerId(of.getCustomerId() + "");
      
      if (of.getBillToAdd() != null) {
        invoiceform.setBillto(of.getBillToAdd()+"");
      }else{
        invoiceform.setBillto("");
      }
      invoiceform.setBilltoID(of.getBillToAddIdValue() + "");
      invoiceform.setShiptoID(of.getShipToAddIdValue() + "");
      
      if (of.getShipToAdd() != null) {
        invoiceform.setShipto(of.getShipToAdd() + "");
      }else{
        invoiceform.setShipto("");
      }
      invoiceform.setJurisdictionVec(taxJurisdiction);
      invoiceform.setJurisdictionID(of.getJurisdictionID());
      invoiceform.setStatusid(of.getStatusIdValue()+"");
      invoiceform.setInvoiceDate(of.getOrderDate());
      invoiceform.setPoid(of.getPo());
      invoiceform.setTermid(of.getTermsIdValue()+"");
      invoiceform.setAccountmanagerid(of.getAcctMgrIdValue()+"");
      invoiceform.setProjectid(of.getProjectIdValue()+"");
      invoiceform.setNotes(of.getNotes()+"");
      invoiceform.setProjectName(of.getProject());
      invoiceform.setAccountmanagerName(of.getAcctMgr());
      invoiceform.setItemLines(of.getItemLines());
      invoiceform.setOrdername(of.getCustomerName());
      invoiceform.setExternalid("");
      invoiceform.setTaxAmount(of.getTaxAmount());

      java.sql.Date invDate = of.getOrderDate();
      if (invDate != null) {
        int month = invDate.getMonth();
        month = month + 1;
        invoiceform.setMonth(month + "");
        
        int date = invDate.getDate();
        invoiceform.setDate(date + "");
        
        int year= invDate.getYear();
        invoiceform.setYear(year + "");
      }
      form = invoiceform;

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
      request.setAttribute("body", AccountConstantKeys.ADD );
      request.setAttribute("newinvoiceform",form);
      request.setAttribute("invoiceform",form);
      FORWARD_final = FORWARD_editorder;
    }catch (Exception e){
      logger.error("[Exception] AutoGenerateInvoiceHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }
  
}

