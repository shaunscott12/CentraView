/*
 * $RCSfile: AccountFacade.java,v $    $Revision: 1.3 $  $Date: 2005/09/06 16:41:28 $ - $Author: mcallist $
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

package com.centraview.account.accountfacade;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;
import javax.naming.NamingException;

import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.inventory.InventoryException;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.item.ItemException;
import com.centraview.account.order.OrderForm;
import com.centraview.account.payment.PaymentVO;
import com.centraview.account.purchaseorder.PurchaseOrderVO;
import com.centraview.common.AuthorizationFailedException;

/**
 * This is remote interface which is called from client
 */

public interface AccountFacade extends EJBObject {
  public OrderForm createOrder(OrderForm orderDetail, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void updateOrder(OrderForm orderDetail, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException;

  public void deleteOrder(int orderId, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException;

  public OrderForm getOrderForm(int orderId, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public InvoiceVO createInvoice(InvoiceVO invoiceDetail, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void updateInvoice(InvoiceVO invoiceDetail, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void deleteInvoice(int invoiceId, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException;

  public InvoiceVO getInvoiceVO(int invoiceId, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public PurchaseOrderVO createPurchaseOrder(PurchaseOrderVO poDetail, int userID)
      throws RemoteException, AuthorizationFailedException;

  public void updatePurchaseOrder(PurchaseOrderVO poDetail, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void deletePurchaseOrder(int poId, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException;

  public PurchaseOrderVO getPurchaseOrderVO(int poId, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public ExpenseVO createExpense(ExpenseVO expenseVO, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void updateExpense(ExpenseVO expenseVO, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void deleteExpense(int expenseID, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException;

  public ExpenseVO getExpenseVO(int expenseID, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public PaymentVO createPayment(PaymentVO paymentVO, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void updatePayment(PaymentVO paymentVO, int userID) throws RemoteException,
      AuthorizationFailedException;

  public void deletePayment(int paymentID, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException;

  public PaymentVO getPaymentVO(int paymentID, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public void deleteVendor(int entityID, int userID) throws RemoteException,
      AuthorizationFailedException, NamingException, CreateException;

  public void deleteItem(int individualID, int itemId) throws RemoteException, ItemException,
      AuthorizationFailedException, NamingException, CreateException;

  public void deleteInventory(int individualID, int itemId) throws RemoteException,
      InventoryException, AuthorizationFailedException, NamingException, CreateException;

  public void setDataSource(String ds) throws RemoteException;

  /**
   * We will get the Map for the TaxMatrix with key value (For Example: J1C1 its
   * nothing but following number after character "J" is Jurisdiction ID
   * following number after character "C" is Class ID) Value Object will hold
   * the value for the TaxRate
   * @return taxMatrix Its a Map for the TaxMatrix with key value (For Example:
   *         J1C1 its nothing but following number after character "J" is
   *         Jurisdiction ID following number after character "C" is Class ID)
   *         Value Object will hold the value for the TaxRate
   */
  public HashMap getTaxMartix() throws RemoteException;

  /**
   * This method will create a bean class with information of JurisdictionID and
   * JurisdictionName. Then store the Object in a Collection.
   * @return Vector Its a collection of JurisdictionID and JurisdictionName.
   */
  public Vector getTaxJurisdiction() throws RemoteException;

  /**
   * This method set the JurisdictionID for the Associated Address.
   * @param addressID The addressID is for the Address.
   * @param jurisdictionID The jurisdictionID is for the Address.
   * @return void.
   */
  public void setJurisdictionForAddress(int addressID, int jurisdictionID) throws RemoteException;

  /**
   * This method will get the parentItemID for the Item.
   * @return integer The parentItemID Associated to the Item.
   */
  public int getParentItemID(int itemID) throws RemoteException;
}
