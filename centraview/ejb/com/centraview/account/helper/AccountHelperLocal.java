/*
 * $RCSfile: AccountHelperLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:26 $ - $Author: mking_cv $
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


package com.centraview.account.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.account.common.PaymentLines;
import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.order.OrderVO;
import com.centraview.account.purchaseorder.PurchaseOrderVO;

/**
This interface AccountHelperLocal is local interface for
other EJBs
*/


public interface AccountHelperLocal extends EJBLocalObject
{
  /** returns Item Types in vector
    * @return Vector
    */
  public Vector getItemTypes();

  public Vector getLocations();
  public String deleteObjects(String modulename, ArrayList arl);
  public Vector getNewPaymentMethods();
  public Vector getNewGLAccounts();
  /** returns GLAccounts in vector
    * @return Vector
    */
  public Vector getGLAccounts();
  /** Add the GLAccount in DataBase
  * Note: This method is kept for to be Majorly used from
  *    Syncronization module only.
  *    Users are not supposed to Add or Modify any
  *    Account directly as it can affect the whole
  *    System in dangerous manner.
  */
  public void addGLAccount(GLAccountVO glaVO);

  /** Update the GLAccount in DataBase
  * Note: This method is kept for to be Majorly used from
  *    Syncronization module only.
  *    Users are not supposed to Add or Modify any
  *    Account directly as it can affect the whole
  *    System in dangerous manner.
  */
  public void updateGLAccount(GLAccountVO glaVO);

  /**
  *
  */
  public void addPaymentMethod(PaymentMethodVO pmVO);

  /**
  *
  */
  public void savePaymentMethod(PaymentMethodVO pmVO);


  /**
  *
  */
  public void updatePaymentMethod(PaymentMethodVO pmVO);

  /** returns Tax Classes in vector
    * @return Vector
    */
  public Vector getTaxClasses();

  /** returns Account Status in vector
    * @return Vector
    */
  public Vector getAccountingStatus();

  /** returns Accounting Terms in vector
    * @return Vector
    */
  public Vector getAccountingTerms();

  /** returns Payment Methods in vector
    * @return Vector
    */
  public Vector getPaymentMethods();
  public void addVendor(int entityID,int userID);
  public void deleteVendor(int entityID,int userID);

  /** returns all Invoices for the given entity
    * @return PaymentLines
    */
  public PaymentLines getPaymentInvoices(int entityID);

  /** Returns Tax in float for a given Tax Class & Jurisdiction
    * @return float tax
    */
  public float getTax(int taxClassId, int taxJurisdictionId)  ;



  /** Adds ItemLines after calculating Tax etc and returns the updated InvoiceVO
    * @return InvoiceVO
    * @param int
    * @param InvoiceVO
    * @param String
    */

  public InvoiceVO calculateInvoiceItems(int userId,InvoiceVO invoiceVO, String newItemID) ;

  public OrderVO        calculateOrderItems(int userId,OrderVO orderVO, String newItemID);
  public PurchaseOrderVO calculatePurchaseOrderItems(int userId,PurchaseOrderVO purchaseOrderVO, String newItemID);
  public ExpenseVO      calculateExpenseItems(int userId,ExpenseVO expenseVO, String newItemID);

  /**
    * This method will create a bean class with information of JurisdictionID and JurisdictionName. Then store the Object in a Collection.
    *
    * @return Vector  Its a collection of JurisdictionID and JurisdictionName.
    */
  public Vector getTaxJurisdiction();

  /**
    * We will get the Map for the TaxMatrix with key value
    * (For Example: J1C1 its nothing but following number after character "J" is Jurisdiction ID
    * following number after character "C" is Class ID)
    * Value Object will hold the value for the TaxRate
    *
    * @return taxMatrix Its a Map for the TaxMatrix with key value
    * (For Example: J1C1 its nothing but following number after character "J" is Jurisdiction ID
    * following number after character "C" is Class ID)
    * Value Object will hold the value for the TaxRate
    */
  public HashMap getTaxMartix();

  /**
    * This method will insert the taxClass or taxJurisdiotion according the Parmater typeClassOrJurisdiction.
    * we will insert the name in either taxClass or taxJurisdiction depending the type of operation which we are performing.
    *
    * @param taxClassOrJurisdiction A String will represent the Name for the either taxClass or taxJurisdiction
    * @param typeClassOrJurisdiction A String will represent are we going to insert the name in either taxClass or taxJurisdiction
    * @return void
    */
  public void insertTaxClassOrJurisdiction(String taxClassOrJurisdiction, String typeClassOrJurisdiction);

  /**
    * We will set the Tax Matrix values
    *
    * @param taxMatrix Its a collection for the TaxMartixForm bean, Which will hold the information of the Tax like ClassID, JurisdictionID & taxRate
    * @return void
    */
  public void setTaxMatrix(ArrayList taxMatrix);

  /**
    * This method will delete a entry from either taxClass or taxJurisdiotion according the Parmater typeClassOrJurisdiction.
    * We will delete a entry on bais Identity for either taxClass or TaxJurisdiction on baisis of the type of operation.
    * We can delete a jurisdiction directly, but before deleting the taxClass. We must have to make sure that its not used by any other item in the Application.
    *
    * @param taxClassOrJurisdictionID A integer will represent the Identification for either taxClass or TaxJurisdiction on baisis of the type of operation which we are carrying out.
    * @param typeClassOrJurisdiction A String will represent are we going to insert the name in either taxClass or taxJurisdiction
    * @return void
    */
  public void removeTaxClassOrJurisdiction(int taxClassOrJurisdictionID, String typeClassOrJurisdiction);

  /**
    * This method will get the TaxClassID for the Item.
    *
    * @return integer  The taxClassID Associated to the Item.
    */
  public int getTaxClassID(int itemID) ;

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  /**
   * This method will get the parentItemID for the Item.
   *
   * @return integer  The parentItemID Associated to the Item.
   */
   public int getParentItemID(int itemID);

}
