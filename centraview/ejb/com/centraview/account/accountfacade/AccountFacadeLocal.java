/*
 * $RCSfile: AccountFacadeLocal.java,v $    $Revision: 1.2 $  $Date: 2005/09/06 16:41:28 $ - $Author: mcallist $
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

import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;
import javax.naming.NamingException;

import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.order.OrderForm;
import com.centraview.account.payment.PaymentVO;
import com.centraview.account.purchaseorder.PurchaseOrderVO;
import com.centraview.common.AuthorizationFailedException;

/**
 * This is local interface which is called from client
 */
public interface AccountFacadeLocal extends EJBLocalObject {
  public OrderForm createOrder(OrderForm orderDetail, int userID)
      throws AuthorizationFailedException;

  public void updateOrder(OrderForm orderDetail, int userID) throws AuthorizationFailedException,
      NamingException;

  public void deleteOrder(int orderId, int userID) throws AuthorizationFailedException,
      NamingException;

  public OrderForm getOrderForm(int orderId, int individualID) throws AuthorizationFailedException;

  public InvoiceVO createInvoice(InvoiceVO invoiceDetail, int userID)
      throws AuthorizationFailedException;

  public void updateInvoice(InvoiceVO invoiceDetail, int userID)
      throws AuthorizationFailedException;

  public void deleteInvoice(int invoiceId, int userID) throws AuthorizationFailedException,
      NamingException;

  public InvoiceVO getInvoiceVO(int invoiceId, int individualID)
      throws AuthorizationFailedException;

  public PurchaseOrderVO createPurchaseOrder(PurchaseOrderVO poDetail, int userID)
      throws AuthorizationFailedException;

  public void updatePurchaseOrder(PurchaseOrderVO poDetail, int userID)
      throws AuthorizationFailedException;

  public void deletePurchaseOrder(int poId, int userID) throws AuthorizationFailedException,
      NamingException;

  public PurchaseOrderVO getPurchaseOrderVO(int poId, int individualID)
      throws AuthorizationFailedException;

  public ExpenseVO createExpense(ExpenseVO expenseVO, int userID)
      throws AuthorizationFailedException;

  public void updateExpense(ExpenseVO expenseVO, int userID) throws AuthorizationFailedException;

  public void deleteExpense(int expenseID, int userID) throws AuthorizationFailedException,
      NamingException;

  public ExpenseVO getExpenseVO(int expenseID, int individualID)
      throws AuthorizationFailedException;

  public PaymentVO createPayment(PaymentVO paymentVO, int userID)
      throws AuthorizationFailedException;

  public void updatePayment(PaymentVO paymentVO, int userID) throws AuthorizationFailedException;

  public void deletePayment(int paymentID, int userID) throws AuthorizationFailedException,
      NamingException;

  public PaymentVO getPaymentVO(int paymentID, int individualID)
      throws AuthorizationFailedException;

  public void setDataSource(String ds);

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
  public HashMap getTaxMartix();

  /**
   * This method will create a bean class with information of JurisdictionID and
   * JurisdictionName. Then store the Object in a Collection.
   * @return Vector Its a collection of JurisdictionID and JurisdictionName.
   */
  public Vector getTaxJurisdiction();

  /**
   * This method set the JurisdictionID for the Associated Address.
   * @param addressID The addressID is for the Address.
   * @param jurisdictionID The jurisdictionID is for the Address.
   * @return void.
   */
  public void setJurisdictionForAddress(int addressID, int jurisdictionID);

} // end class definition

