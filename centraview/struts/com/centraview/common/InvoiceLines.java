/*
 * $RCSfile: InvoiceLines.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:03 $ - $Author: mking_cv $
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
Date  : 01-09-2003

Author: Atul

LastUpdated Date :

-----------------------------------------------------------------------------
*/

package com.centraview.common ;

import java.util.HashMap;
import java.util.Vector;
 
 class InvoiceLines extends AccountingLines
{
	String sortMember = "SKU";
	String primaryMemberType = "LineId";
	String primaryTable = "invoiceitems";
	String primaryMember = "LineId";
	
	
	
	
		 
  public InvoiceLines()
  {	
  		viewVector = new Vector(); 
		this.columnMap = new HashMap();

		columnMap.put( "LineId" , new Integer(100) );
		columnMap.put( "ItemId" , new Integer(100) );				
		columnMap.put( "SKU" , new Integer(140) );
		columnMap.put( "Description" , new Integer(130) );
		columnMap.put( "Quantity" , new Integer(100) ) ;
		columnMap.put( "PriceEach" , new Integer(100) ) ;
		columnMap.put( "PriceExtended" , new Integer(100) );
		
		columnMap.put( "UnitTax" , new Integer(100) ) ;
		columnMap.put( "UnitTaxrate" , new Integer(100) );
		columnMap.put( "OrderQuantity" , new Integer(100) );
		columnMap.put( "PendingQuantity" , new Integer(100) );

		viewVector.add("SKU");
		viewVector.add("Description");
		viewVector.add("Quantity");
		viewVector.add("PriceEach");
		viewVector.add("PriceExtended");
		
		
  }//CONSTRUCTOR
  
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
	
	
	public void addLine (AccountingLineElement ale) {
	
		System.out.println("InvoiceLines:addLine()");
	
	}//addline
	
	
	public void removeLine (int lineID) {
		System.out.println("InvoiceLines:removeLine(int)");	
	}
	
	public void removeLine (int[] lineID) {
		System.out.println("InvoiceLines:removeLine(int[])");
	}
	
	public void calculate() {
		System.out.println("InvoiceLines:calculate()");
	}
	
}//InvoiceLines