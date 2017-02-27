/*
 * $RCSfile: ItemElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:44 $ - $Author: mking_cv $
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
package com.centraview.sale.proposal;

import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElementMember;

/**
 * @author Sameer
 */
public class ItemElement extends AccountingLineElement
{
  /** The Element ID for this element. */
	private int elementId;

  /** Empty Constructor. */
	public ItemElement()
	{
    //Not Implemented
	} //end of ItemElement Constructor

  /**
   * Constructs a new ItemElement with the corresponding ElementID.
   *
   * @param id The ID for the new ItemElement.
   */
	public ItemElement(int id)
	{
		this.elementId = id;
	} //end of ItemElement Constructor

  /**
   * Returns the Element ID.
   *
   * @return The ID of this element.
   */
	public int getElementId()
	{
		return this.elementId;
	} //end of getElementId method

  /**
   * Sets the Element ID.
   *
   * @param elementId The new ID of this element.
   */
  public void setElementId(int elementId)
  {
    this.elementId = elementId;
    IntMember lineIdMember = (IntMember)this.get("LineId");
    if (lineIdMember != null)
    {
      lineIdMember.setMemberValue(elementId);
    } //end of if statement (lineIdMember != null)
  } //end of setElementId method

  /**
   * Adds a new ListElementMember with a name.
   *
   * @param memberName The name of the new ListElementMember.
   * @param listElementMember The listElementMember to add.
   */
	public void addMember(String memberName, ListElementMember listElementMember)
	{
		this.put(memberName, listElementMember);
	} //end of addMember method

  /**
   * Returns the ListElementMember based on the name requested.
   * If this element does not exist, <code>null</code> is
   * returned.
   *
   * @param memberName The name of the ListElementMember to return
   *
   * @return The ListElementMember requested,
   * <code>null</code> if it doesn't exist.
   */
	public ListElementMember getMember(String memberName)
	{
    ListElementMember listElementMember = null;
		try
		{
		  listElementMember = (ListElementMember) this.get(memberName);
		} //end of try block
    catch (Exception e)
		{
			System.out.println("[Exception]: ItemElement.getMember: " + e.toString());
		  e.printStackTrace();
		} //end of catch block
    return listElementMember;
	} //end of getMember method

  /**
   * Calculates the information for this line.
   * <ul>
   * <li> Price Extended
   * <li> Unit Tax
   * </ul>
   */
  public void calculateLine()
  {
    float priceEach = ((Float)((FloatMember)this.get("Price")).getMemberValue()).floatValue();
		int qty = ((Integer)((IntMember)this.get("Quantity")).getMemberValue()).intValue();
    float priceExtended = (float)(priceEach * qty) ;
    ((FloatMember)this.get("PriceExtended")).setMemberValue(priceExtended);
  } //end of calculateLine method
} //end of ItemElement class