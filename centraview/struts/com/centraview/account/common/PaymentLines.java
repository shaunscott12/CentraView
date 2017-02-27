/*
 * $RCSfile: PaymentLines.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:12 $ - $Author: mking_cv $
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

/*
----------------------------------------------------------------------------
Date  : 25 Sept 2003

Author: Deepa Sarwate

This class is used in payment sub-module for refering to unpaid invoices
-----------------------------------------------------------------------------
*/

package com.centraview.account.common ;

import java.util.HashMap;
import java.util.Vector;
 
public class PaymentLines extends AccountingLines
{
	String sortMember = "InvoiceNum";
	String primaryMemberType = "InvoiceId";
	String primaryTable = "invoiceNum";
	String primaryMember = "LineId";

	public String paymentID;

	public PaymentLines()
	{
		viewVector = new Vector(); 
		this.columnMap = new HashMap();

		columnMap.put("InvoiceId", new Integer(50) );
		columnMap.put("InvoiceNum", new Integer(200) );
		columnMap.put("Date", new Integer(200) );
		columnMap.put("Total", new Integer(150) ) ;
		columnMap.put("AmountDue", new Integer(200) ) ;
		columnMap.put("AmountApplied", new Integer(200) );
				
		viewVector.add("Invoice #");
		viewVector.add("Date");
		viewVector.add("Total");
		viewVector.add("Amount Due");
		viewVector.add("Payment");

		this.sort();
	}
		     
	public String getPrimaryMember()
	{
		return this.primaryMember;
	}

	public void setPrimaryMember(String primaryMember)
	{
		this.primaryMember = primaryMember;
	}

	public String getPrimaryMemberType()
	{
		return this.primaryMemberType;
	}

	public void setPrimaryMemberType(String primaryMemberType)
	{
		this.primaryMemberType = primaryMemberType;
	}


	public String getPrimaryTable()
	{
		return this.primaryTable;
	}

	public void setPrimaryTable(String primaryTable)
	{
		this.primaryTable = primaryTable;
	}


	public String getSortMember()
	{
		return this.sortMember;
	}

	public void setSortMember(String sortMember)
	{
		this.sortMember = sortMember;
	}


	public void addLine(AccountingLineElement ale) 
	{
	}//addline


	public void removeLine (int lineID) 
	{
	}

	public void removeLine (int[] lineID) 
	{
	}

	public void calculate() 
	{
	}

}//PaymentLines