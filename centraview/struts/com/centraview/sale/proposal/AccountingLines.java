/*
 * $RCSfile: AccountingLines.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:43 $ - $Author: mking_cv $
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

Author: Sameer 

LastUpdated Date :

-----------------------------------------------------------------------------
*/

package com.centraview.sale.proposal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import com.centraview.common.ListElementMember;

abstract public class AccountingLines extends TreeMap
{

	protected HashMap columnMap ;
	protected String listType ;
	protected Vector viewVector;
	protected float tax = 0;
	protected float subtotal;
	protected float discount = 0;
	protected float shipping = 0;
	protected float totalItems = 0;
	protected float total = 0;
	protected String notes;
	
	

	protected char sortType = 'A';
		
	public  AccountingLines()
	{
	}

	abstract public  String getSortMember();
	abstract public void setSortMember( String value );
	abstract public  String getPrimaryMemberType();
	abstract public  void setPrimaryMemberType(String s);
	abstract public  String getPrimaryTable();
	abstract public  void setPrimaryTable(String s);
	abstract public  String getPrimaryMember();
	abstract void addLine (AccountingLineElement ale);
	abstract void removeLine (int lineID);
	abstract void removeLine (int[] lineID);
	abstract void calculate();
	

	public int getMaxLineId()
	{
		Set s = this.keySet();
		Iterator itr = s.iterator();
		int maxId =0;
		while (itr.hasNext())
		{
			AccountingLineElement ale = (AccountingLineElement) this.get(itr.next());
			int currid = ale.getElementId();
			maxId = Math.max(maxId,currid);			
		
		}//while
		return maxId;
	}//getMaxLineId

	public void sort()
	{
		TreeMap tmp = new TreeMap();
		Set s = this.keySet();
		Iterator itr = s.iterator();
		String sortMember = this.getSortMember();
		AccountingLineElement curr = null;
		while(itr.hasNext())
		{
			try
				{
					curr = (AccountingLineElement)this.get(itr.next());
					String newSortStr = ((ListElementMember)curr.get(sortMember)).getSortString() + curr.getElementId();
					tmp.put(newSortStr,curr);
				}//try
			catch(Exception e)
				{
					System.out.println("AccountingLines:Exception cought in while");
				}//catch
		}//while
		this.clear();
		this.putAll(tmp);
	}//sort
	
	
	
	public char getSortType()
	{
		return this.sortType;
	}

	public void setSortType(char sortType)
	{
		this.sortType = sortType;
	}
	

	
	public String getListType()
	{
		return this.listType;
	}

	public void setListType(String listType)
	{
		this.listType = listType;
	}

	
	public HashMap getColumnMap()
	{
		return this.columnMap;
	}

	public void setColumnMap(HashMap columnMap)
	{
		this.columnMap = columnMap;
	}

	
	public float getDiscount()
	{
		return this.discount;
	}

	public void setDiscount(float discount)
	{
		this.discount = discount;
	}

	
	public String getNotes()
	{
		return this.notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	
	public float getShipping()
	{
		return this.shipping;
	}

	public void setShipping(float shipping)
	{
		this.shipping = shipping;
	}

	
	public float getSubtotal()
	{
		return this.subtotal;
	}

	public void setSubtotal(float subtotal)
	{
		this.subtotal = subtotal;
	}

	
	public float getTax()
	{
		return this.tax;
	}

	public void setTax(float tax)
	{
		this.tax = tax;
	}

	
	public float getTotal()
	{
		return this.total;
	}

	public void setTotal(float total)
	{
		this.total = total;
	}

	
	public float getTotalItems()
	{
		return this.totalItems;
	}

	public void setTotalItems(float totalItems)
	{
		this.totalItems = totalItems;
	}

	
	public Vector getViewVector()
	{
		return this.viewVector;
	}

	public void setViewVector(Vector viewVector)
	{
		this.viewVector = viewVector;
	}
}//AccountingLines

