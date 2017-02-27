/*
 * $RCSfile: ItemElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:13 $ - $Author: mking_cv $
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

Author: Sameer.

LastUpdated Date :

-----------------------------------------------------------------------------
*/

package com.centraview.marketing ;
import com.centraview.common.DoubleMember;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElementMember;
import com.centraview.common.StringMember;


public class ItemElement extends AccountingLineElement
{
	private int elementId;

	public ItemElement()
	{
	}
	public ItemElement(int id)
	{
		this.elementId = id;
	}
	
	public void addMember(String memberName, ListElementMember lem )
	{
		this.put(memberName, lem);
	}
	
	public ListElementMember getMember( String memberName )
	{
		try
		{
		return (ListElementMember)this.get(memberName);
		} catch (Exception e)
		{
			System.out.println("Exception Cought in InvoiceElement:getMember");
		//	System.out.println(e.stackTrace());
		return null;
		}
	}
	
	
	public int getElementId()
	{
		return this.elementId;
	}

	public void setElementId(int elementId)
	{
		this.elementId = elementId;
		IntMember lineIdMember = (IntMember)this.get("LineId");
		if(lineIdMember != null) lineIdMember.setMemberValue(elementId);
	}
	
	public void calculateLine()
	{
		System.out.println("InvoiceElement:CaculateLine ");
		/*float priceEach = ((Float)((FloatMember)this.get("PriceEach")).getMemberValue()).floatValue();
		float qty = ((Float)((FloatMember)this.get("Quantity")).getMemberValue()).floatValue();
		float priceExtended = (float)(priceEach*qty) ;
		float unitTaxRate =	((Float)((FloatMember)this.get("UnitTaxrate")).getMemberValue()).floatValue();
		float unitTax = (float)(unitTaxRate*priceExtended)	;
		
		((FloatMember)this.get("PriceExtended")).setMemberValue(priceExtended);
		((FloatMember)this.get("UnitTax")).setMemberValue(unitTax);*/
		
		System.out.println("ListPrice :: "+this.get("ListPrice"));
		System.out.println("Cost :: "+this.get("Cost"));
		System.out.println("Value :: "+this.get("Value"));
		System.out.println("Type :: "+this.get("Type"));
		
		double listprice = 0.0d;
		double cost =  0.0d;
		float costvalue = 0.0f ;
		float pricevalue = 0.0f;
		
		/*if (this.get("ListPrice") == null )
			this.get("ListPrice") = new FloatMember("ListPrice",new Float(0.0f),'D',"",'T',false,20) ;
		if (this.get("Cost") == null )
			this.get("Cost") = new FloatMember("Cost",new Float(0.0f),'D',"",'T',false,20) ;
		*/
		
		if (this.get("ListPrice") != null )
		{
			listprice = ((Double)((DoubleMember)this.get("ListPrice")).getMemberValue()).doubleValue();
			DoubleMember smPrice  = (DoubleMember)this.get("ListPrice");
			Double dp = (Double)smPrice.getMemberValue();
			pricevalue = dp.floatValue();
		}
		
		System.out.println("listprice ::"+listprice );
			
		if (this.get("Cost") != null )	
		{
			cost = ((Double)((DoubleMember)this.get("Cost")).getMemberValue()).doubleValue();
			//costvalue = ((Float)((DoubleMember)this.get("Cost")).getMemberValue()).floatValue();
			DoubleMember smCost  = (DoubleMember)this.get("Cost");
			Double dc = (Double)smCost.getMemberValue();
			costvalue = dc.floatValue();
		}
		
		System.out.println("cost ::"+cost );
			
		int qty = ((Integer)((IntMember)this.get("Value")).getMemberValue()).intValue();
		
		System.out.println("qty ::"+qty );
		
		String type = (String)((StringMember)this.get("Type")).getMemberValue();
		
		System.out.println("type ::"+type );
		
		System.out.println("listprice ::"+listprice );
		System.out.println("cost ::"+cost );
		System.out.println("qty ::"+qty );
		System.out.println("type ::"+type );
		
		float discountedPrice = 0f;
		if (type.equals("costDollarLayer"))
		{
			discountedPrice = costvalue+qty;
		}
		else if (type.equals("costPercentLayer"))
		{
			discountedPrice = costvalue - (costvalue*(qty/100));
		}
		else if (type.equals("listDollarLayer"))
		{
			discountedPrice = pricevalue-qty;
		}
		else if (type.equals("listPercentLayer"))
		{
			discountedPrice = pricevalue - (pricevalue*(qty/100));
		}
		else if (type.equals("fixedCostLayer"))
		{
			discountedPrice = qty;
		}
		
		System.out.println("discountedPrice ::"+discountedPrice );
		((FloatMember)this.get("DiscountedPrice")).setMemberValue(discountedPrice);
	}
}