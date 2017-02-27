/*
 * $RCSfile: ItemLines.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:14 $ - $Author: mking_cv $
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

package com.centraview.marketing ;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.centraview.common.ListElementMember;

public class ItemLines extends AccountingLines
{
	String sortMember = "SKU";
	String primaryMemberType = "LineId";
	String primaryTable = "invoiceitems";
	String primaryMember = "LineId";

	public String newItemID;


  public ItemLines()
  {

  		viewVector = new Vector();
		this.columnMap = new HashMap();

		columnMap.put( "LineId" , new Integer(100) );
		columnMap.put( "ItemId" , new Integer(100) );
		columnMap.put( "SKU" , new Integer(110) );
		columnMap.put( "Description" , new Integer(139) );
		columnMap.put( "Quantity" , new Integer(110) ) ;
		columnMap.put( "PriceEach" , new Integer(170) ) ;
		columnMap.put( "PriceExtended" , new Integer(180) );

		columnMap.put( "UnitTax", new Integer(100) ) ;
		columnMap.put( "UnitTaxrate", new Integer(100) );
		columnMap.put( "OrderQuantity", new Integer(100) );
		columnMap.put( "PendingQuantity", new Integer(100) );

		columnMap.put( "Type", new Integer(100) );
		columnMap.put( "Value", new Integer(100) );
		columnMap.put( "DiscountedPrice", new Integer(180) );

		viewVector.add("Item");
		viewVector.add("Description");
		viewVector.add("Price");
		viewVector.add("Cost");
		viewVector.add("Value");
		viewVector.add("Discounted Price");
		this.calculate();
		this.sort();
		this.setNotes("Notes in Test Data");
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


	public void addLine (AccountingLineElement ale) {

		ItemElement ie = null;
		try
			{
			ie = (ItemElement)ale;
			}catch (Exception e)
					{
					System.out.println("ItemLines:addLine: Exception caught: Cannot add this Line");
					return;
					}//catch

		System.out.println("InvoiceLines:addLine()");
		int maxIndex = 0;
		maxIndex = this.getMaxLineId();
		maxIndex +=1;
		ie.setElementId(maxIndex);
		String newKey = ""+((ListElementMember)ie.get(this.getSortMember())).getSortString()+maxIndex ;
		this.put(newKey, ie);

	}//addline


	public void removeLine (int lineID) {
		System.out.println("InvoiceLines:removeLine(int)");

		Set s = this.keySet();
		Iterator itr = s.iterator();
		Vector v = new Vector();
		while (itr.hasNext())
		{
			Object o = itr.next();
			AccountingLineElement ale = (AccountingLineElement) this.get(o);
			int currid = ale.getElementId();
			if (currid == lineID) ale.setLineStatus("Deleted");
			{
				if (!ale.getLineStatus().equals("New"))
					{
						ale.setLineStatus("Deleted");ale.setLineStatus("Deleted");
					}else
					v.add(o);
			}//if

		}//while
		if (v.size()!=0)
		{
			for(int i =0; i<v.size(); i++)
			{
				Object o = v.get(i);
				this.remove(o);
			}//for
		}//if vector size != 0

	}//removeLiine(int)

	public void removeLine (int[] lineID) {
		System.out.println("InvoiceLines:removeLine(int[])");

			Set s = this.keySet();
		Iterator itr = s.iterator();

		while (itr.hasNext())
		{
			AccountingLineElement ale = (AccountingLineElement) this.get(itr.next());
			int currid = ale.getElementId();
			if (Arrays.binarySearch(lineID,currid) != -1) ale.setLineStatus("Deleted");

		}//while


	}//removeLiine(int[])

	public void calculate() {
		System.out.println("InvoiceLines:calculate()");
		float tax = 0;
		float totalQty = 0 ;
		float subtotal = 0;
		float total = 0;
		float shipping = 0;
		float discount = 0;

		Set s = this.keySet();
		Iterator itr = s.iterator();

		while (itr.hasNext())
		{
			ItemElement ie = (ItemElement)this.get(itr.next());
			ie.calculateLine();
			//	float unitTaxRate =	((Float)((FloatMember)this.get("UnitTaxrate")).getMemberValue()).floatValue();
			/*tax += ((Float)((FloatMember)ie.get("UnitTax")).getMemberValue()).floatValue();
			totalQty += ((Float)((FloatMember)ie.get("Quantity")).getMemberValue()).floatValue();
			subtotal += ((Float)((FloatMember)ie.get("PriceExtended")).getMemberValue()).floatValue();
			*/

	}//while
	total =  (float)(subtotal + tax + shipping - discount);
	this.setTax(tax);
	this.setTotal(total);
	this.setSubtotal(subtotal);
	this.setTotalItems(totalQty);
	}//calculate

}//InvoiceLines