/*
 * $RCSfile: AccountListLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:23 $ - $Author: mking_cv $
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

import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import com.centraview.account.expense.ExpenseList;
import com.centraview.account.glaccount.GLAccountList;
import com.centraview.account.inventory.InventoryList;
import com.centraview.account.invoice.InvoiceList;
import com.centraview.account.item.ItemList;
import com.centraview.account.order.OrderList;
import com.centraview.account.payment.PaymentList;
import com.centraview.account.purchaseorder.PurchaseOrderList;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
* This interface AccountListLocal is local interface for
* other EJBs
*/
public interface AccountListLocal extends EJBLocalObject
{
	/** this method returns GLAccountsList
	*/
	public GLAccountList getGLAccountList( int userId, HashMap preference) throws AuthorizationFailedException;

	/**
	 * returns Inventory List
	 */
	public InventoryList getInventoryList( int userId, HashMap preference) throws AuthorizationFailedException;

	/**
	*	returns ItemList
	*/
	public ItemList getItemList( int userId, HashMap preference) throws AuthorizationFailedException;

	/**
	*	returns OrderList
	*/
	public OrderList getOrderList( int userId, HashMap preference) throws AuthorizationFailedException;

	/** returns Invoice List
	*/
	public InvoiceList getInvoiceList( int userId, HashMap preference) throws AuthorizationFailedException;

	/** returns Expense List
	*/
	public ExpenseList getExpenseList( int userId, HashMap preference) throws AuthorizationFailedException;

	/** returns PurchaseOrder List
	*/
	public PurchaseOrderList getPurchaseOrderList( int userId, HashMap preference) throws AuthorizationFailedException;

	/** returns Payment List
	*/
	public PaymentList getPaymentList( int userId, HashMap preference) throws AuthorizationFailedException;

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);

	/**
	 * Returns a ValueListVO representing a list of Invoice records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getInvoiceValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of Order records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getOrderValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of Payment records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getPaymentValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of Expenses records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getExpenseValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of Purchase Order records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getPurchaseOrderValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of Item records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getItemValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of GLAccount records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getGlAccountValueList(int individualId, ValueListParameters parameters);

	/**
	 * Returns a ValueListVO representing a list of Inventory records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getInventoryValueList(int individualId, ValueListParameters parameters);


}