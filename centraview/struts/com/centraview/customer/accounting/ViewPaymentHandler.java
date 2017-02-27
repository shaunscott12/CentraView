/*
 * $RCSfile: ViewPaymentHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.customer.accounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.common.ItemElement;
import com.centraview.account.common.PaymentLines;
import com.centraview.account.payment.PaymentVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.menu.MenuBean;
import com.centraview.common.menu.MenuItem;
import com.centraview.settings.Settings;

/**
 * Handles the request for the Customer View Payment Details
 * screen. Displays Payment only, no modification allowed.
 */
public class ViewPaymentHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "customerPaymentDetail";
    // set up the left nav menu
    MenuItem orderMenuItem = new MenuItem("Order History", "OrderList.do", false);
    MenuItem invoiceMenuItem = new MenuItem("Invoice History", "InvoiceList.do", false);
    MenuItem paymentMenuItem = new MenuItem("Payment History", "PaymentList.do", true);
    ArrayList menuList = new ArrayList();
    menuList.add(orderMenuItem);
    menuList.add(invoiceMenuItem);
    menuList.add(paymentMenuItem);
    MenuBean paymentMenu = new MenuBean("Accounting", menuList);
    request.setAttribute("cvMenu", paymentMenu);

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerPaymentForm", defined in cv-struts-config.xml
    DynaActionForm paymentForm = (DynaActionForm)form;

    try
    {
      // get the payment ID from the form bean
      Integer formPaymentID = (Integer)paymentForm.get("paymentID");
      // create an int to hold the payment ID value
      int paymentID = 0;

      // now, check the payment ID on the form...
      if (formPaymentID == null)
      {
        // if payment ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Payment ID"));
        return(mapping.findForward(forward));
      }else{
        // if payment ID is set on the form properly, then set
        // the int representation for use in the code below
        paymentID = formPaymentID.intValue();
      }
      
      AccountFacadeHome home = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
      AccountFacade remote = (AccountFacade)home.create();
      remote.setDataSource(dataSource);
      
      PaymentVO paymentVO = remote.getPaymentVO(paymentID, individualID);

      paymentForm.set("entityName", paymentVO.getEntity());
      paymentForm.set("paymentAmount", new Float(paymentVO.getPaymentAmount()));
      paymentForm.set("reference", paymentVO.getDescription());
      paymentForm.set("paymentMethodID", new Integer(paymentVO.getPaymentMethodID()));
      paymentForm.set("paymentMethodName", "");
      paymentForm.set("cardType", paymentVO.getCardType());
      paymentForm.set("cardNumber", paymentVO.getCardNumber());
      paymentForm.set("cardExpiration", "");
      paymentForm.set("checkNumber", paymentVO.getCheckNumber());

      ArrayList invoiceList = new ArrayList();
      PaymentLines paymentLines = paymentVO.getPaymentLines();
      if (paymentLines != null)
      {
        Set itemSet = paymentLines.keySet();
        Iterator iter = itemSet.iterator();
        while (iter.hasNext())
        {
          HashMap formInvoiceInfo = new HashMap();
          String key = (String)iter.next();
          ItemElement myItem = (ItemElement)paymentLines.get(key);

          String invoiceNumber = (String)myItem.getMember("InvoiceNum").getMemberValue();
          formInvoiceInfo.put("invoiceNumber", new Integer(invoiceNumber));

          formInvoiceInfo.put("date", myItem.getMember("Date").getMemberValue());

          Number total = (Number)myItem.getMember("Total").getMemberValue();
          formInvoiceInfo.put("total", new Float(total.floatValue()));

          Number amountDue = (Number)myItem.getMember("AmountDue").getMemberValue();
          formInvoiceInfo.put("amountDue", new Float(amountDue.floatValue()));

          Number amountApplied = (Number)myItem.getMember("AmountApplied").getMemberValue();
          formInvoiceInfo.put("amountApplied", new Float(amountApplied.floatValue()));

          invoiceList.add(formInvoiceInfo);
        }
      }
      
      paymentForm.set("invoiceList", invoiceList);

    }catch(Exception e){
      System.out.println("[Exception][CV ViewpaymentHandler] Exception thrown in execute(): " + e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

