/*
 * $RCSfile: InvoiceElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:11 $ - $Author: mking_cv $
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

package com.centraview.account.common ;
import com.centraview.common.ListElementMember;


public class InvoiceElement extends AccountingLineElement
{
	private int elementId;
	public InvoiceElement(int id)
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
	}
	
	public void calculateLine()
	{
		System.out.println("InvoiceElement:CaculateLine ");
	}
	
}//Invoiceelement