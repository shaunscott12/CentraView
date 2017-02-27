/*
 * $RCSfile: ViewItemHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:20 $ - $Author: mking_cv $
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

package com.centraview.account.item;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class ViewItemHandler extends org.apache.struts.action.Action
{
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_viewitem = "viewitem";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      //System.out.println("[DEBUG] [ViewItemHandler]: entry");

      ItemHome itemHome = (ItemHome) CVUtility.getHomeObject("com.centraview.account.item.ItemHome", "Item");
      Item itemRemote = itemHome.create();
      itemRemote.setDataSource(dataSource);

      ItemForm itemForm = (ItemForm) form;

      //itemForm.setSku(itemVO.getSku());

      HttpSession session = request.getSession();
      int userId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      ItemVO itemVO = itemRemote.getItem(userId, Integer.parseInt(itemForm.getItemid()));

      itemForm.setCost(itemVO.getCost() + "");
      //		itemForm.setCreated(itemVO.getCreated());
      itemForm.setGlaccountid(itemVO.getGlAccountId() + ""); // returns int
      //		itemForm.setGlaccountvec(itemVO.getGlaccountvec());
      itemForm.setItemdesc(itemVO.getItemDesc());
      itemForm.setItemid(itemVO.getItemId() + ""); // returns int
      itemForm.setItemname(itemVO.getItemName());
      itemForm.setItemtypeid(itemVO.getItemTypeId() + ""); // returns int
      //		itemForm.setItemtypevec(itemVO.getItemtypevec()+"");
      itemForm.setLinktoinventory(itemVO.getLinkToInventory());
      //		itemForm.setModified(itemVO.getModified());
      itemForm.setPrice(itemVO.getPrice() + ""); // returns double
      itemForm.setQtyonbackorder(itemVO.getQtyOnBackOrder() + ""); // returns int
      itemForm.setQtyonhand(itemVO.getQtyOnHand() + ""); // returns int
      itemForm.setQtyonorder(itemVO.getQtyOnOrder() + ""); // returns int
      itemForm.setSku(itemVO.getSku());
      itemForm.setSubitemid(itemVO.getSubItemOfId() + ""); // returns int
      itemForm.setTaxclassid(itemVO.getTaxClassId() + ""); // returns int
      //		itemForm.setTaxclassvec(itemVO.getTaxclassVec());
      itemForm.setManufacturerid(itemVO.getManufacturerid() + ""); // returns int
      itemForm.setManufacturername(itemVO.getManufacturerVO() + ""); // returns ItemVO
      itemForm.setVendorid(itemVO.getVendorid() + ""); // returns int
      itemForm.setVendorname(itemVO.getVendorVO() + ""); // returns EntityVO
      itemForm.setVendorid(itemVO.getVendorid() + ""); // returns int
      //itemForm.setSubitemname(itemVO.getItemName());
	  itemForm.setLinktoinventory(itemVO.getLinkToInventory());

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
      // request.setAttribute("body", AccountConstantKeys.VIEW);

      // forward to jsp page
      FORWARD_final = FORWARD_viewitem;

    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] ViewItemHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    } //end of catch block (Exception)
    //System.out.println("[DEBUG] [ViewItemHandler]: exit");
    return (mapping.findForward(FORWARD_final));
  } //end of execute method
} //end of ViewItemHandler class
