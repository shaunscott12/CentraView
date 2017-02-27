/*
 * $RCSfile: ViewInvoiceHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

import java.sql.Date;
import java.text.SimpleDateFormat;
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
import com.centraview.account.common.ItemLines;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.menu.MenuBean;
import com.centraview.common.menu.MenuItem;
import com.centraview.settings.Settings;

/**
 * Handles the request for the Customer View Invoice Details
 * screen. Displays Invoice only, no modification allowed.
 */
public class ViewInvoiceHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "customerInvoiceDetails";
    // set up the left nav menu
    MenuItem orderMenuItem = new MenuItem("Order History", "OrderList.do", false);
    MenuItem invoiceMenuItem = new MenuItem("Invoice History", "InvoiceList.do", true);
    MenuItem paymentMenuItem = new MenuItem("Payment History", "PaymentList.do", false);
    ArrayList menuList = new ArrayList();
    menuList.add(orderMenuItem);
    menuList.add(invoiceMenuItem);
    menuList.add(paymentMenuItem);
    MenuBean invoiceMenu = new MenuBean("Accounting", menuList);
    request.setAttribute("cvMenu", invoiceMenu);

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerInvoiceForm", defined in cv-struts-config.xml
    DynaActionForm invoiceForm = (DynaActionForm)form;

    try
    {
      // get the Invoice ID from the form bean
      Integer formInvoiceID = (Integer)invoiceForm.get("invoiceID");
      // create an int to hold the Invoice ID value
      int invoiceID = 0;

      // now, check the invoice ID on the form...
      if (formInvoiceID == null)
      {
        // if Invoice ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Invoice ID"));
        return(mapping.findForward(forward));
      }else{
        // if Invoice ID is set on the form properly, then set
        // the int representation for use in the code below
        invoiceID = formInvoiceID.intValue();
      }
      
      AccountFacadeHome home = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
      AccountFacade remote = (AccountFacade)home.create();
      remote.setDataSource(dataSource);
      
      InvoiceVO invoiceVO = remote.getInvoiceVO(invoiceID, individualID);

      invoiceForm.set("orderID", new Integer(invoiceVO.getOrderId()));

      invoiceForm.set("entityName", invoiceVO.getCustomerName());
      invoiceForm.set("billingAddress", invoiceVO.getBillToAddress());
      invoiceForm.set("shippingAddress", invoiceVO.getShipToAddress());
      invoiceForm.set("status", invoiceVO.getStatusName());

      Date invoiceDate = (Date)invoiceVO.getInvoiceDate();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMMM dd, yyyy");
      String formattedDate = dateFormatter.format(invoiceDate);
      invoiceForm.set("date", formattedDate);

      int termID = invoiceVO.getTermId();
      invoiceForm.set("terms", termID+"");
      invoiceForm.set("accountManager", invoiceVO.getAccountManagerName());

      ItemLines itemLines = (ItemLines)invoiceVO.getItemLines();
      ArrayList invoiceItems = new ArrayList();
      
      int totalItems = 0;
      Float invoiceSubTotal = new Float(0.00);
      if (itemLines != null)
      {
        Set itemSet = itemLines.keySet();
        Iterator iter = itemSet.iterator();
        while (iter.hasNext())
        {
          HashMap formItemInfo = new HashMap();
          String treeMapKey = (String)iter.next();
          ItemElement myItem = (ItemElement)itemLines.get(treeMapKey);

          formItemInfo.put("itemSku", (String)(myItem.getMember("SKU")).getMemberValue());
          formItemInfo.put("description", (String)(myItem.getMember("Description")).getMemberValue());
          
          Float lineQuantityFloat = (Float)(myItem.getMember("Quantity")).getMemberValue();
          totalItems += lineQuantityFloat.intValue();
          formItemInfo.put("quantity", new Integer(lineQuantityFloat.intValue()));
          
          Float priceEach = (Float)(myItem.getMember("PriceEach")).getMemberValue();
          formItemInfo.put("priceEach", priceEach);
          
          Float lineSubTotal = (Float)(myItem.getMember("PriceExtended")).getMemberValue();
          formItemInfo.put("price", lineSubTotal);
          
          invoiceSubTotal = new Float(invoiceSubTotal.floatValue() + lineSubTotal.floatValue());

          // add the hashmap of item info to the arraylist of items
          invoiceItems.add(formItemInfo);
        }
      }
      invoiceForm.set("itemList", invoiceItems);
      invoiceForm.set("subTotal", invoiceSubTotal);
      invoiceForm.set("totalItems", new Integer(totalItems));
      
      // at this time, there is no way to calculate tax
      Float tax = new Float(0);
      invoiceForm.set("tax", tax);
      
      // at this time, there is no way to calculate shipping
      Float shipping = new Float(0);
      invoiceForm.set("shipping", shipping);
      
      // at this time, invoiceTotal is equal to subTotal since
      // we cannot calculate tax or shipping costs
      invoiceForm.set("invoiceTotal", invoiceSubTotal);
System.out.println("\n\n\ninvoiceForm = [" + invoiceForm + "]\n\n\n");      
    }catch(Exception e){
      System.out.println("[Exception][CV ViewInvoiceHandler] Exception thrown in execute(): " + e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

