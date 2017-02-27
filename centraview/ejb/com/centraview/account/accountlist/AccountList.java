/*
 * $RCSfile: AccountList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:20 $ - $Author: mking_cv $
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


package com.centraview.account.accountlist;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.account.expense.ExpenseList;
import com.centraview.account.glaccount.GLAccountList;
import com.centraview.account.inventory.InventoryList;
import com.centraview.account.invoice.InvoiceList;
import com.centraview.account.item.ItemList;
import com.centraview.account.order.OrderList;
import com.centraview.account.payment.PaymentList;
import com.centraview.account.purchaseorder.PurchaseOrderList;
import com.centraview.common.AuthorizationFailedException;

/**
* This is  remote interface which is called from client
*/

public interface AccountList extends EJBObject
{

  /** returns GLAccounts List
  */
  public GLAccountList getGLAccountList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /**
   * returns Inventory List
   */
  public InventoryList getInventoryList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /**
  *	returns ItemList
  */
  public ItemList getItemList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /**
  *	returns LocationList
  */
  public ArrayList getLocationList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /**
  *	returns OrderList
  */
  public OrderList getOrderList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /** returns Invoice List
  */
  public InvoiceList getInvoiceList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /** returns Expense List
  */
  public ExpenseList getExpenseList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /** returns PurchaseOrder List
  */
  public PurchaseOrderList getPurchaseOrderList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;

  /** returns Payment List
  */
  public PaymentList getPaymentList(int userId, HashMap preference) throws RemoteException,AuthorizationFailedException;
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;

}
