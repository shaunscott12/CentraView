/*
 * $RCSfile: HrExpenseLines.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:02 $ - $Author: mking_cv $
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
package com.centraview.hr.expenses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.centraview.account.common.AccountingLineElement;
import com.centraview.account.common.AccountingLines;
import com.centraview.account.common.ItemElement;
import com.centraview.common.FloatMember;
import com.centraview.common.ListElementMember;

public class HrExpenseLines extends AccountingLines
{
	String sortMember = "ExpenseItem";
	String primaryMemberType = "LineId";
	String primaryTable = "expense";
	String primaryMember = "LineId";

	public String newItemID;


  public HrExpenseLines()
  {

		viewVector = new Vector(); //columns desplayed on the form
		this.columnMap = new HashMap();

		//this is from expenseitem table
		columnMap.put( "LineId" , new Integer(100) );
		columnMap.put( "ExpenseItemId" , new Integer(100) );
		columnMap.put( "SKU" , new Integer(110) );
		columnMap.put( "Description" , new Integer(139) );
		columnMap.put( "Quantity" , new Integer(110) ) ;
		columnMap.put( "PriceEach" , new Integer(170) ) ;
		columnMap.put( "PriceExtended" , new Integer(180) );

		//fields from expense table
		columnMap.put( "Title" , new Integer(100) );
		columnMap.put( "Description" , new Integer(170) );
		columnMap.put( "EntityID" , new Integer(110) );
		columnMap.put( "Status" , new Integer(139) );
		columnMap.put( "Project" , new Integer(110) ) ;
		columnMap.put( "Ticket" , new Integer(100) ) ;
		columnMap.put( "Opportunity" , new Integer(100) );

		columnMap.put( "Owner", new Integer(100) ) ;
		columnMap.put( "Creator", new Integer(100) );
		columnMap.put( "ModifiedBy", new Integer(100) );
		columnMap.put( "Created", new Integer(100) );
		columnMap.put( "Modified", new Integer(100) );
		columnMap.put( "LineID", new Integer(100) );
		columnMap.put( "LineStatus", new Integer(100) );
		columnMap.put( "Amount", new Integer(100) );
		columnMap.put( "GLAccountsID", new Integer(100) );
		columnMap.put( "Notes", new Integer(100) );
		columnMap.put( "ExpenseFormID", new Integer(100) );

		//Columns displayed on the form
		viewVector.add("ExpenseItem");
		viewVector.add("Description");
		viewVector.add("Quantity");
		viewVector.add("ReferenceType");
		viewVector.add("Reference");
		viewVector.add("Amount");
		viewVector.add("ExtendedAmount");

		this.calculate();// to do the tax calculation & all
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

		int maxIndex = 0;
		maxIndex = this.getMaxLineId();
		maxIndex +=1;
		ie.setElementId(maxIndex);
		String newKey = ""+((ListElementMember)ie.get(this.getSortMember())).getSortString()+maxIndex ;
		this.put(newKey, ie);

	}//addline


	public void removeLine (int lineID) {
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

		float total = 0;
		Set s = this.keySet();
		Iterator itr = s.iterator();

		while (itr.hasNext())
		{
			
			HrExpenseLineElement ie = (HrExpenseLineElement)this.get(itr.next());
			String lineStatus = ie.getLineStatus();
			if (!lineStatus.equals("Deleted"))
			{
				ie.calculateLine();
				total+=((Float)((FloatMember)ie.get("PriceExtended")).getMemberValue()).floatValue();
			}
		}//while
		this.setTotal(total);
	}//calculate
}//ExpenseLines