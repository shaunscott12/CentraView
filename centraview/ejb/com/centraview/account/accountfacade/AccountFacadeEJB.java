/*
 * $RCSfile: AccountFacadeEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/06 16:41:28 $ - $Author: mcallist $
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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.account.expense.ExpenseLocal;
import com.centraview.account.expense.ExpenseLocalHome;
import com.centraview.account.expense.ExpensePK;
import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.helper.AccountHelperLocal;
import com.centraview.account.helper.AccountHelperLocalHome;
import com.centraview.account.inventory.InventoryException;
import com.centraview.account.inventory.InventoryLocal;
import com.centraview.account.inventory.InventoryLocalHome;
import com.centraview.account.invoice.InvoiceLocal;
import com.centraview.account.invoice.InvoiceLocalHome;
import com.centraview.account.invoice.InvoicePK;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.item.ItemException;
import com.centraview.account.item.ItemLocal;
import com.centraview.account.item.ItemLocalHome;
import com.centraview.account.order.OrderForm;
import com.centraview.account.order.OrderLocal;
import com.centraview.account.order.OrderLocalHome;
import com.centraview.account.order.OrderPK;
import com.centraview.account.payment.PaymentLocal;
import com.centraview.account.payment.PaymentLocalHome;
import com.centraview.account.payment.PaymentPK;
import com.centraview.account.payment.PaymentVO;
import com.centraview.account.purchaseorder.PurchaseOrderLocal;
import com.centraview.account.purchaseorder.PurchaseOrderLocalHome;
import com.centraview.account.purchaseorder.PurchaseOrderPK;
import com.centraview.account.purchaseorder.PurchaseOrderVO;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;

public class AccountFacadeEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(AccountFacadeEJB.class);
  protected SessionContext ctx;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  public void ejbCreate()
  {}

  public void ejbRemove()
  {}

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public OrderForm createOrder(OrderForm orderDetail, int userID)
      throws AuthorizationFailedException
  {

    // Check if the User is having the Permission to view the Proposal then he
    // can create and order.
    boolean flagProposal = CVUtility.isModuleVisible("Proposals", userID, this.dataSource);
    boolean flagOrder = CVUtility.isModuleVisible("OrderHistory", userID, this.dataSource);

    if (!flagProposal && !flagOrder) {
      throw new AuthorizationFailedException(
          "We didn't have privilege for viewing either Proposal or Order module ");
    }

    OrderForm returnForm = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OrderLocalHome home = (OrderLocalHome) ic.lookup("local/Order");
      OrderLocal order = home.create(orderDetail, userID, this.dataSource);
      order.setDataSource(this.dataSource);
      returnForm = order.getOrderForm();
    } catch (Exception e) {
      logger.error("[createOrder]: Exception", e);
    }
    return returnForm;
  }// end of createOrder

  public void deleteOrder(int orderId, int userID) throws NamingException,
      AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("OrderHistory", userID, this.dataSource))
      throw new AuthorizationFailedException("Order- deleteOrder");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OrderLocalHome home = (OrderLocalHome) ic.lookup("local/Order");
      OrderLocal order = home.findByPrimaryKey(new OrderPK(orderId, this.dataSource));
      order.setDataSource(this.dataSource);
      order.remove();
    } catch (RemoveException re) {
      throw new EJBException(re);
    } catch (FinderException fe) {
      throw new EJBException(fe);
    }

  }// end of deleteOrder

  public OrderForm getOrderForm(int orderId, int individualID) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("OrderHistory", individualID, this.dataSource)) {
      throw new AuthorizationFailedException("Order- getOrder");
    }

    OrderForm orderForm = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OrderLocalHome home = (OrderLocalHome) ic.lookup("local/Order");
      OrderLocal order = home.findByPrimaryKey(new OrderPK(orderId, this.dataSource));
      order.setDataSource(this.dataSource);
      orderForm = order.getOrderForm();
    } catch (Exception e) {
      logger.error("[getOrderForm]: Exception", e);
    }
    return orderForm;
  }// end of getOrderForm

  public void updateOrder(OrderForm orderform, int userID) throws NamingException,
      AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("OrderHistory", userID, this.dataSource))
      throw new AuthorizationFailedException("Order- updateOrder");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OrderLocalHome home = (OrderLocalHome) ic.lookup("local/Order");
      OrderLocal order = home.findByPrimaryKey(new OrderPK(orderform.getOrderIdValue(),
          this.dataSource));
      order.setDataSource(this.dataSource);
      order.setOrderForm(orderform, userID);
    } catch (FinderException fe) {
      logger.error("[updateOrder]: Exception", fe);
    }

  }// end of deleteOrder

  public InvoiceVO createInvoice(InvoiceVO invoiceDetail, int userID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("InvoiceHistory", userID, this.dataSource))
      throw new AuthorizationFailedException("Invoice- createInvoice");
    InvoiceVO returnVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      InvoiceLocalHome home = (InvoiceLocalHome) ic.lookup("local/Invoice");
      InvoiceLocal remote = home.create(invoiceDetail, userID, this.dataSource);
      remote.setDataSource(this.dataSource);
      returnVO = remote.getInvoiceVO();
    } catch (Exception e) {
      logger.error("[createInvoice]: Exception", e);
    }
    return returnVO;
  }// end of createInvoice

  public void updateInvoice(InvoiceVO invoiceDetail, int userID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("InvoiceHistory", userID, this.dataSource))
      throw new AuthorizationFailedException("Invoice- updateInvoice");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      InvoiceLocalHome home = (InvoiceLocalHome) ic.lookup("local/Invoice");
      InvoiceLocal invoice = home.findByPrimaryKey(new InvoicePK(invoiceDetail.getInvoiceId(),
          this.dataSource));
      invoice.setDataSource(this.dataSource);
      invoice.setInvoiceVO(invoiceDetail, userID);
    } catch (Exception e) {
      logger.error("[updateInvoice]: Exception", e);
    }
  }// end of updateInvoice

  public void deleteInvoice(int invoiceId, int userID) throws NamingException,
      AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("InvoiceHistory", userID, this.dataSource))
      throw new AuthorizationFailedException("Invoice- deleteInvoice");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      InvoiceLocalHome home = (InvoiceLocalHome) ic.lookup("local/Invoice");
      InvoiceLocal invoice = home.findByPrimaryKey(new InvoicePK(invoiceId, this.dataSource));
      invoice.setDataSource(this.dataSource);
      invoice.remove();
    } catch (RemoveException re) {
      throw new EJBException(re);
    } catch (FinderException fe) {
      throw new EJBException(fe);
    }
  }// end of deleteInvoice

  public InvoiceVO getInvoiceVO(int invoiceId, int individualID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("InvoiceHistory", individualID, this.dataSource))
      throw new AuthorizationFailedException("Invoice- getInvoiceVO");
    InvoiceVO invoiceVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      InvoiceLocalHome home = (InvoiceLocalHome) ic.lookup("local/Invoice");
      InvoiceLocal invoice = home.findByPrimaryKey(new InvoicePK(invoiceId, this.dataSource));
      invoice.setDataSource(this.dataSource);
      invoiceVO = invoice.getInvoiceVO();
    } catch (Exception e) {
      logger.error("[getInvoiceVO]: Exception", e);
    }
    return invoiceVO;
  }// end of getInvoiceForm

  public PurchaseOrderVO createPurchaseOrder(PurchaseOrderVO poDetail, int userID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("PurchaseOrder", userID, this.dataSource))
      throw new AuthorizationFailedException("PurchaseOrder- createPurchaseOrder");
    PurchaseOrderVO returnVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PurchaseOrderLocalHome home = (PurchaseOrderLocalHome) ic.lookup("local/PurchaseOrder");
      PurchaseOrderLocal remote = home.create(poDetail, userID, this.dataSource);
      remote.setDataSource(this.dataSource);
      returnVO = remote.getPurchaseOrderVO();
    } catch (Exception e) {
      logger.error("[createPurchaseOrder]: Exception", e);
    }
    return returnVO;
  }// end of createPurchaseOrder

  public void updatePurchaseOrder(PurchaseOrderVO poDetail, int userID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("PurchaseOrder", userID, this.dataSource))
      throw new AuthorizationFailedException("PurchaseOrder- updatePurchaseOrder");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PurchaseOrderLocalHome home = (PurchaseOrderLocalHome) ic.lookup("local/PurchaseOrder");
      PurchaseOrderLocal invoice = home.findByPrimaryKey(new PurchaseOrderPK(poDetail
          .getPurchaseOrderId(), this.dataSource));
      invoice.setDataSource(this.dataSource);
      invoice.setPurchaseOrderVO(poDetail, userID);
    } catch (Exception e) {
      logger.error("[updatePurchaseOrder]: Exception", e);
    }
  }// end of updatePurchaseOrder

  public void deletePurchaseOrder(int poID, int userID) throws NamingException,
      AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("PurchaseOrder", userID, this.dataSource))
      throw new AuthorizationFailedException("PurchaseOrder- deletePurchaseOrder");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PurchaseOrderLocalHome home = (PurchaseOrderLocalHome) ic.lookup("local/PurchaseOrder");
      PurchaseOrderLocal invoice = home
          .findByPrimaryKey(new PurchaseOrderPK(poID, this.dataSource));
      invoice.setDataSource(this.dataSource);
      invoice.remove();
    } catch (RemoveException re) {
      throw new EJBException(re);
    } catch (FinderException fe) {
      throw new EJBException(fe);
    }
  }// end of deletePurchaseOrder

  public PurchaseOrderVO getPurchaseOrderVO(int poID, int individualID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("PurchaseOrder", individualID, this.dataSource))
      throw new AuthorizationFailedException("PurchaseOrder- getPurchaseOrder");
    PurchaseOrderVO invoiceVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PurchaseOrderLocalHome home = (PurchaseOrderLocalHome) ic.lookup("local/PurchaseOrder");
      PurchaseOrderLocal invoice = home
          .findByPrimaryKey(new PurchaseOrderPK(poID, this.dataSource));
      invoice.setDataSource(this.dataSource);
      invoiceVO = invoice.getPurchaseOrderVO();
    } catch (Exception e) {
      logger.error("[getPurchaseOrderVO]: Exception", e);
    }
    return invoiceVO;
  }// end of getPurchaseOrderForm

  /** ******* EXPENSE STARTS HERE********* */

  public ExpenseVO createExpense(ExpenseVO expenseVO, int userID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Expense", userID, this.dataSource))
      throw new AuthorizationFailedException("Expense- createExpense");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ExpenseLocalHome home = (ExpenseLocalHome) ic.lookup("local/Expense");
      ExpenseLocal expense = home.create(expenseVO, userID, this.dataSource);
      expense.setDataSource(this.dataSource);
      expenseVO = expense.getExpenseVO();
    } catch (Exception e) {
      logger.error("[createExpense]: Exception", e);
    }
    return expenseVO;
  }

  public void deleteExpense(int expenseID, int userID) throws NamingException,
      AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Expense", userID, this.dataSource))
      throw new AuthorizationFailedException("Expense- deleteExpense");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ExpenseLocalHome home = (ExpenseLocalHome) ic.lookup("local/Expense");
      ExpenseLocal expense = home.findByPrimaryKey(new ExpensePK(expenseID, this.dataSource));
      expense.setDataSource(this.dataSource);
      expense.remove();
    } catch (RemoveException re) {
      throw new EJBException(re);
    } catch (FinderException fe) {
      throw new EJBException(fe);
    }
  }

  public ExpenseVO getExpenseVO(int expenseID, int userID) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Expense", userID, this.dataSource))
      throw new AuthorizationFailedException("Expense- getExpense");
    ExpenseVO expenseVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ExpenseLocalHome home = (ExpenseLocalHome) ic.lookup("local/Expense");
      ExpenseLocal expense = home.findByPrimaryKey(new ExpensePK(expenseID, this.dataSource));
      expense.setDataSource(this.dataSource);
      expenseVO = expense.getExpenseVO();
    } catch (Exception e) {
      logger.error("[getExpenseVO]: Exception", e);
    }
    return expenseVO;
  }

  public void updateExpense(ExpenseVO expenseVO, int userID) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Expense", userID, this.dataSource))
      throw new AuthorizationFailedException("Expense- updateExpense");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ExpenseLocalHome home = (ExpenseLocalHome) ic.lookup("local/Expense");
      ExpenseLocal expense = home.findByPrimaryKey(new ExpensePK(expenseVO.getExpenseID(),
          this.dataSource));
      expense.setDataSource(this.dataSource);
      expense.setExpenseVO(expenseVO, userID);
    } catch (Exception e) {
      logger.error("[updateExpense]: Exception", e);
    }
  }

  /** ******** EXPENSE ENDS HERE ********** */

  /** ******* PAYMENT STARTS HERE********* */

  public PaymentVO createPayment(PaymentVO paymentVO, int userID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Payment", userID, this.dataSource))
      throw new AuthorizationFailedException("Payment- createPayment");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PaymentLocalHome home = (PaymentLocalHome) ic.lookup("local/Payment");
      PaymentLocal payment = home.create(paymentVO, userID, this.dataSource);
      payment.setDataSource(this.dataSource);
      paymentVO = payment.getPaymentVO();
    } catch (Exception e) {
      logger.error("[createPayment]: Exception", e);
    }
    return paymentVO;
  }

  public void deletePayment(int paymentID, int userID) throws NamingException,
      AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Payment", userID, this.dataSource))
      throw new AuthorizationFailedException("Payment- deletePayment");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PaymentLocalHome home = (PaymentLocalHome) ic.lookup("local/Payment");
      PaymentLocal payment = home.findByPrimaryKey(new PaymentPK(paymentID, this.dataSource));
      payment.setDataSource(this.dataSource);
      payment.remove();
    } catch (RemoveException re) {
      throw new EJBException(re);
    } catch (FinderException fe) {
      throw new EJBException(fe);
    }
  }

  public PaymentVO getPaymentVO(int paymentID, int userID) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Payment", userID, this.dataSource))
      throw new AuthorizationFailedException("Payment- getPaymentVO");
    PaymentVO paymentVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PaymentLocalHome home = (PaymentLocalHome) ic.lookup("local/Payment");
      PaymentLocal payment = home.findByPrimaryKey(new PaymentPK(paymentID, this.dataSource));
      payment.setDataSource(this.dataSource);
      paymentVO = payment.getPaymentVO();
    } catch (Exception e) {
      logger.error("[getPaymentVO]: Exception", e);
    }
    return paymentVO;
  }

  public void updatePayment(PaymentVO paymentVO, int userID) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Payment", userID, this.dataSource))
      throw new AuthorizationFailedException("Payment- updatePayment");
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PaymentLocalHome home = (PaymentLocalHome) ic.lookup("local/Payment");
      PaymentLocal payment = home.findByPrimaryKey(new PaymentPK(paymentVO.getPaymentID(),
          this.dataSource));
      payment.setDataSource(this.dataSource);
      payment.setPaymentVO(paymentVO, userID);
    } catch (Exception e) {
      logger.error("[updatePayment]: Exception", e);
    }
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical name of the datasource.
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

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
  public HashMap getTaxMartix()
  {
    HashMap taxMartix = new HashMap();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AccountHelperLocalHome home = (AccountHelperLocalHome) ic.lookup("local/AccountHelper");
      AccountHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      taxMartix = remote.getTaxMartix();
    } catch (Exception e) {
      logger.error("[getTaxMartix]: Exception", e);
    }
    return taxMartix;
  }

  /**
   * This method will create a bean class with information of JurisdictionID and
   * JurisdictionName. Then store the Object in a Collection.
   * @return Vector Its a collection of JurisdictionID and JurisdictionName.
   */
  public Vector getTaxJurisdiction()
  {
    Vector taxJurisdiction = new Vector();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AccountHelperLocalHome home = (AccountHelperLocalHome) ic.lookup("local/AccountHelper");
      AccountHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      taxJurisdiction = remote.getTaxJurisdiction();
    } catch (Exception e) {
      logger.error("[getTaxJurisdiction]: Exception", e);
    }
    return taxJurisdiction;
  }

  /**
   * This method set the JurisdictionID for the Associated Address.
   * @param addressID The addressID is for the Address.
   * @param jurisdictionID The jurisdictionID is for the Address.
   * @return void.
   */
  public void setJurisdictionForAddress(int addressID, int jurisdictionID)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome) ic.lookup("local/ContactHelper");
      ContactHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.setJurisdictionForAddress(addressID, jurisdictionID);
    } catch (Exception e) {
      logger.error("[setJurisdictionForAddress]: Exception", e);
    }
  }

  public void deleteInventory(int individualID, int itemId) throws InventoryException,
      AuthorizationFailedException, NamingException, CreateException
  {
    InitialContext ic = CVUtility.getInitialContext();
    InventoryLocalHome home = (InventoryLocalHome) ic.lookup("local/Inventory");
    InventoryLocal invoice = home.create();
    invoice.setDataSource(this.dataSource);
    invoice.deleteInventory(individualID, itemId);
  }

  public void deleteItem(int individualID, int itemId) throws ItemException, NamingException,
      CreateException
  {
    InitialContext ic = CVUtility.getInitialContext();
    ItemLocalHome home = (ItemLocalHome) ic.lookup("local/Item");
    ItemLocal invoice = home.create();
    invoice.setDataSource(this.dataSource);
    invoice.deleteItem(individualID, itemId);
  }

  public void deleteVendor(int entityID, int userID) throws NamingException, CreateException
  {
    InitialContext ic = CVUtility.getInitialContext();
    AccountHelperLocalHome home = (AccountHelperLocalHome) ic.lookup("local/AccountHelper");
    AccountHelperLocal remote = home.create();
    remote.setDataSource(this.dataSource);
    remote.deleteVendor(entityID, userID);
  }

  /**
   * This method will get the parentItemID for the Item.
   * @return integer The parentItemID Associated to the Item.
   */
  public int getParentItemID(int itemID)
  {
    int parentItemID = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      AccountHelperLocalHome home = (AccountHelperLocalHome) ic.lookup("local/AccountHelper");
      AccountHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      parentItemID = remote.getParentItemID(itemID);
    } catch (Exception e) {
      logger.error("[getParentItemID]: Exception", e);
    }
    return parentItemID;
  }
}// end of class
