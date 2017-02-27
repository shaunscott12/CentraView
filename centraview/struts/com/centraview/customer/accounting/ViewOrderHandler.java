/*
 * $RCSfile: ViewOrderHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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
import com.centraview.account.order.OrderForm;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Handles the request for the Customer View Order Details
 * screen. Displays Order only, no modification allowed.
 */
public class ViewOrderHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = ".view.customer.view_order";

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerOrderForm", defined in cv-struts-config.xml
    DynaActionForm orderForm = (DynaActionForm)form;

    try {
      // get the order ID from the form bean
      Integer formOrderID = (Integer)orderForm.get("orderID");
      // create an int to hold the order ID value
      int orderID = 0;

      // now, check the order ID on the form...
      if (formOrderID == null) {
        // if order ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Order ID"));
        return(mapping.findForward(forward));
      } else {
        // if order ID is set on the form properly, then set
        // the int representation for use in the code below
        orderID = formOrderID.intValue();
      }

      AccountFacadeHome home = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
      AccountFacade remote = (AccountFacade)home.create();
      remote.setDataSource(dataSource);
      
      OrderForm cvOrderForm = (OrderForm)remote.getOrderForm(orderID, individualID);

      orderForm.set("entityName", cvOrderForm.getCustomerName());
      orderForm.set("billingAddress", cvOrderForm.getBillToAdd());
      orderForm.set("shippingAddress", cvOrderForm.getShipToAdd());
      orderForm.set("status", cvOrderForm.getStatus());

      Date orderDate = (Date)cvOrderForm.getOrderDate();
      String formattedDate = "";
      orderForm.set("date", formattedDate);

      orderForm.set("terms", cvOrderForm.getTerms());
      orderForm.set("accountManager", cvOrderForm.getAcctMgr());

      ItemLines itemLines = (ItemLines)cvOrderForm.getItemLines();
      ArrayList orderItems = new ArrayList();
      
      int totalItems = 0;
      Float orderSubTotal = new Float(0.00);
      if (itemLines != null) {
        Set itemSet = itemLines.keySet();
        Iterator iter = itemSet.iterator();
        while (iter.hasNext()) {
          HashMap formItemInfo = new HashMap();
          String treeMapKey = (String)iter.next();
          ItemElement myItem = (ItemElement)itemLines.get(treeMapKey);

          formItemInfo.put("itemSku", (String)(myItem.getMember("SKU")).getMemberValue());
          formItemInfo.put("description", (String)(myItem.getMember("Description")).getMemberValue());

          Number lineQuantityFloat = (Number)(myItem.getMember("Quantity")).getMemberValue();
          totalItems += lineQuantityFloat.intValue();
          formItemInfo.put("quantity", new Integer(lineQuantityFloat.intValue()));
          
          if (myItem.getMember("Price") != null) {
            Number priceEach = (Number)(myItem.getMember("Price")).getMemberValue();
            formItemInfo.put("priceEach", priceEach);
          } else {
            formItemInfo.put("priceEach", new Float(0));
          }

          Number lineSubTotal = new Float(0);
          if (myItem.getMember("PriceExtended") != null) {
            lineSubTotal = (Number)(myItem.getMember("PriceExtended")).getMemberValue();
          }
          formItemInfo.put("price", lineSubTotal);
          
          orderSubTotal = new Float(orderSubTotal.floatValue() + lineSubTotal.floatValue());

          // add the hashmap of item info to the arraylist of items
          orderItems.add(formItemInfo);
        }
      }
      orderForm.set("itemList", orderItems);
      orderForm.set("subTotal", orderSubTotal);
      orderForm.set("totalItems", new Integer(totalItems));
      
      // at this time, there is no way to calculate tax
      Float tax = new Float(0);
      orderForm.set("tax", tax);
      
      // at this time, there is no way to calculate shipping
      Float shipping = new Float(0);
      orderForm.set("shipping", shipping);
      
      // at this time, orderTotal is equal to subTotal since
      // we cannot calculate tax or shipping costs
      orderForm.set("orderTotal", orderSubTotal);
      
    }catch(Exception e){
      System.out.println("[Exception][CV ViewOrderHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

