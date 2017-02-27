/*
 * $RCSfile: DisplayEditItemHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:18 $ - $Author: mking_cv $
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

import java.util.Vector;

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

public class DisplayEditItemHandler extends org.apache.struts.action.Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_edititem = ".view.accounting.item_detail";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
 
  public ActionForward execute(ActionMapping mapping,
    ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {

      int itemId = 0;
      if (request.getParameter("rowId") != null)
      {  
        itemId = Integer.parseInt(request.getParameter("rowId"));
      } //end of if statement (request.getParameter("rowId") != null)

      if (itemId == 0 && request.getAttribute("itemId") != null)
      {  
        itemId = Integer.parseInt((String) request.getAttribute("itemId"));
      } //end of if statement (itemId == 0 && request.getAttribute("itemId") != null)

      ItemHome itemHome = (ItemHome) CVUtility.getHomeObject("com.centraview.account.item.ItemHome", "Item");
      Item itemRemote = itemHome.create();
      itemRemote.setDataSource(dataSource);

      ItemForm itemForm = (ItemForm) form;

      Vector itemTypeVec = itemRemote.getItemType();
      Vector glAcntVec = itemRemote.getGLAccountType();
      Vector taxClassVec = itemRemote.getTaxClassType();
      itemForm.setItemtypevec(itemTypeVec);
      itemForm.setGlaccountvec(glAcntVec);
      itemForm.setTaxclassvec(taxClassVec);

      HttpSession session = request.getSession();
      int userId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      ItemVO itemVO = itemRemote.getItem(userId, itemId);

      itemForm.setCost("" + itemVO.getCost());
      itemForm.setGlaccountid("" + itemVO.getGlAccountId());
      itemForm.setItemdesc(itemVO.getItemDesc());
      itemForm.setItemname(itemVO.getItemName());
      itemForm.setItemtypeid("" + itemVO.getItemTypeId());
      itemForm.setLinktoinventory(itemVO.getLinkToInventory());
      itemForm.setPrice("" + itemVO.getPrice());
      itemForm.setQtyonbackorder("" + itemVO.getQtyOnBackOrder());
      itemForm.setQtyonhand("" + itemVO.getQtyOnHand());
      itemForm.setQtyonorder("" + itemVO.getQtyOnOrder());
      itemForm.setSku(itemVO.getSku());
      itemForm.setSubitemid("" + itemVO.getSubItemOfId());
      itemForm.setTaxclassid("" + itemVO.getTaxClassId());
      itemForm.setManufacturerid("" + itemVO.getManufacturerid());
      itemForm.setManufacturername(itemVO.getManufacturerVO().getName());
      itemForm.setVendorid("" + itemVO.getVendorid());
      itemForm.setVendorname(itemVO.getVendorVO().getName());
      itemForm.setCreated("" + itemVO.getCreatedOn());
      itemForm.setModified("" + itemVO.getModifiedOn());

      //get the parent name
      if (itemVO.getSubItemOfId() != 0)
      {
        ItemVO parentItemVO = itemRemote.getItem(userId, itemVO.getSubItemOfId());
        itemForm.setSubitemname(parentItemVO.getItemName());
        parentItemVO = null;
      } //end of if statement (itemVO.getSubItemOfId() != 0)
      else
      {
        itemForm.setSubitemname(null);
      } //end of else statement (itemVO.getSubItemOfId() != 0)

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
      request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
      itemForm.setItemid("" + itemVO.getItemId());

      request.setAttribute("itemform", itemForm);

      // forward to jsp page
      FORWARD_final = FORWARD_edititem;

    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][DisplayEditItemHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    } //end of catch block
    return (mapping.findForward(FORWARD_final));
  } //end of execute method
} //end of DisplayEditItemHandler
