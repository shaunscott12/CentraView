/*
 * $RCSfile: SaveEditItemHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:20 $ - $Author: mking_cv $
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

import java.text.DecimalFormat;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveEditItemHandler extends Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_save = ".view.accounting.item.save";
  private static final String FORWARD_savenew = ".view.accounting.item.savenew";
  private static final String FORWARD_saveclose = ".view.accounting.item.saveclose";
  private static final String FORWARD_cancel = "cancel";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;



  /**
   *  ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = GLOBAL_FORWARD_failure;
    try
    {
      String typeOfSave = "cancel";
      if (request.getParameter("typeofsave") != null)
      {
        typeOfSave = request.getParameter("typeofsave");
      } //end of if statement (request.getParameter("typeofsave") != null)

      if (typeOfSave.equals("cancel"))
      {
        FORWARD_final = FORWARD_saveclose;
        request.setAttribute("body", "list");
        return (mapping.findForward(FORWARD_final));
      } //end of if statement (typeOfSave.equals("cancel"))

      ItemHome itemHome = (ItemHome) CVUtility.getHomeObject("com.centraview.account.item.ItemHome", "Item");
      Item itemRemote = itemHome.create();
      itemRemote.setDataSource(dataSource);

      ItemVO itemVO = new ItemVO();
      ItemForm itemForm = (ItemForm) form;

      itemVO.setItemId(Integer.parseInt(itemForm.getItemid()));
      itemVO.setSku(itemForm.getSku());
      itemVO.setItemName(itemForm.getItemname());
      if (itemForm.getItemtypeid() != null)
      {
        itemVO.setItemTypeId(Integer.parseInt(itemForm.getItemtypeid()));
      } //end of if statement (itemForm.getItemtypeid() != null)
      itemVO.setItemDesc(itemForm.getItemdesc());
      if (itemForm.getGlaccountid() != null)
      {
        itemVO.setGlAccountId(Integer.parseInt(itemForm.getGlaccountid()));
      } //end of if statement (itemForm.getGlaccountid() != null)
      if (itemForm.getTaxclassid() != null)
      {
        itemVO.setTaxClassId(Integer.parseInt(itemForm.getTaxclassid()));
      } //end of if statement (itemForm.getTaxclassid() != null)
      if (itemForm.getSubitemid() != null && (itemForm.getSubitemid().length() > 0))
      {
        itemVO.setSubItemOfId(Integer.parseInt(itemForm.getSubitemid()));
      } //end of if statement (itemForm.getSubitemid() != null && (itemForm.getSubitemid().length() > 0))
      if (itemForm.getManufacturerid() != null && itemForm.getManufacturerid().length() > 0)
      {
        itemVO.setManufacturerid(Integer.parseInt(itemForm.getManufacturerid()));
      } //end of if statement (itemForm.getManufacturerid() != null && itemForm.getManufacturerid().length() > 0)
      if (itemForm.getVendorid() != null && itemForm.getVendorid().length() > 0)
      {
        itemVO.setVendorid(Integer.parseInt(itemForm.getVendorid()));
      } //end of if statement (itemForm.getVendorid() != null && itemForm.getVendorid().length() > 0)

      DecimalFormat nf = (DecimalFormat)DecimalFormat.getInstance();
      nf.setDecimalSeparatorAlwaysShown(false);
      if (itemForm.getPrice() != null && itemForm.getPrice().length() > 0)
      {
        double price = (nf.parse(itemForm.getPrice())).doubleValue();
        itemVO.setPrice(price);
      } //end of if statement (itemForm.getPrice() != null && itemForm.getPrice().length() > 0)
      if (itemForm.getCost() != null && itemForm.getCost().length() > 0)
      {
        double cost = (nf.parse(itemForm.getCost())).doubleValue();
        itemVO.setCost(cost);
      } //end of if statement (itemForm.getCost() != null && itemForm.getCost().length() > 0)
      if (itemForm.getLinktoinventory() != null && itemForm.getLinktoinventory().length() > 0)
      {
        itemVO.setLinkToInventory(itemForm.getLinktoinventory());
      } //end of if statement (itemForm.getLinktoinventory() != null && itemForm.getLinktoinventory().length() > 0)
      if (itemForm.getQtyonorder() != null && itemForm.getQtyonorder().length() > 0)
      {
        itemVO.setQtyOnOrder(Integer.parseInt(itemForm.getQtyonorder()));
      } //end of if statement (itemForm.getQtyonorder() != null && itemForm.getQtyonorder().length() > 0)
      if (itemForm.getQtyonbackorder() != null && itemForm.getQtyonbackorder().length() > 0)
      {
        itemVO.setQtyOnBackOrder(Integer.parseInt(itemForm.getQtyonbackorder()));
      } //end of if statement (itemForm.getQtyonbackorder() != null && itemForm.getQtyonbackorder().length() > 0)

      HttpSession session = request.getSession();
      int userId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      itemVO.setCreatedBy(userId);
      itemRemote.updateItem(userId, itemVO);

      Vector itemTypeVec = itemRemote.getItemType();
      Vector glAcntVec = itemRemote.getGLAccountType();
      Vector taxClassVec = itemRemote.getTaxClassType();
      ((ItemForm) form).setItemtypevec(itemTypeVec);
      ((ItemForm) form).setGlaccountvec(glAcntVec);
      ((ItemForm) form).setTaxclassvec(taxClassVec);

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);

      if (typeOfSave.equals("save"))
      {
        returnStatus = FORWARD_save;
        request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
        request.setAttribute("itemId", itemForm.getItemid());
      } //end of if statement (typeOfSave.equals("save"))
      else if (typeOfSave.equals("savenew"))
      {
        returnStatus = FORWARD_savenew;
        request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
        request.removeAttribute("itemform");
        request.setAttribute("body", "new");

      } //end of else if statement (typeOfSave.equals("savenew"))
      else if (typeOfSave.equals("saveclose"))
      {
        returnStatus = FORWARD_saveclose;
        request.removeAttribute("itemform");
        request.setAttribute("body", "list");
      } //end of else if statement (typeOfSave.equals("saveclose"))
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][SaveEditItemHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
    } //end of catch block (Exception)
    return (mapping.findForward(returnStatus));
  } //end of execute method
} //end of SaveEditItemHandler class
