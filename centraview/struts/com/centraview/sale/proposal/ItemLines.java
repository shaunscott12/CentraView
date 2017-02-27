/*
 * $RCSfile: ItemLines.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:44 $ - $Author: mking_cv $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElementMember;

/**
 * @author Atul
 */
public class ItemLines extends AccountingLines
{
  /** The ItemLines List Sort Member. */
  String sortMember = "SKU";

  /** The ItemLines List Primary Member Type. */
  String primaryMemberType = "LineId";

  /** The ItemLines List Primary Table. */
  String primaryTable = "proposalitem";

  /** The ItemLines List Primary Member. */
  String primaryMember = "LineId";

  /** The ItemLines List New ID. */
  public String newItemID;

	/** Default Constructor. */
  public ItemLines()
  {
    viewVector = new Vector();
    this.columnMap = new HashMap();

    columnMap.put("LineId" , new Integer(100));
    columnMap.put("ItemId" , new Integer(100));
    columnMap.put("SKU" , new Integer(110));
    columnMap.put("Description" , new Integer(139));
    columnMap.put("Quantity" , new Integer(110));
    columnMap.put("Price" , new Integer(170));
    columnMap.put("PriceExtended" , new Integer(180));
    columnMap.put("TaxAmount", new Integer(100));

    viewVector.add("Item");
    viewVector.add("Description");
    viewVector.add("Quantity");
    viewVector.add("Price Each");
		viewVector.add("Tax Amount");
    viewVector.add("Price Extended");
    this.calculate();
    this.sort();
    this.setNotes("Notes in Test Data");
  } //end of ItemLines Constructor

	/**
   * Returns the Primary Member of this List.
   *
   * @return The primary member of this list.
   */
  public String getPrimaryMember()
  {
    return this.primaryMember;
  } //end of getPrimaryMember method

  /**
   * Sets the Primary Member of this List.
   *
   * @param primaryMember The primary member of this list.
   */
  public void setPrimaryMember(String primaryMember)
  {
    this.primaryMember = primaryMember;
  } //end of setPrimaryMember method

  /**
   * Returns the Primary Member Type of this List.
   *
   * @return The primary member type of this list.
   */
  public String getPrimaryMemberType()
  {
    return this.primaryMemberType;
  } //end of getPrimaryMemberType method

  /**
   * Sets the Primary Member Type of this List.
   *
   * @param primaryMemberType The primary member type of this list.
   */
  public void setPrimaryMemberType(String primaryMemberType)
  {
    this.primaryMemberType = primaryMemberType;
  } //end of setPrimaryMemberType method


  /**
   * Returns the Primary Table for this List.
   *
   * @return The primary Table for this list.
   */
  public String getPrimaryTable()
  {
    return this.primaryTable;
  } //end of getPrimaryTable method

  /**
   * Sets the Primary Table for this List.
   *
   * @param primaryTable The primary table for this list.
   */
  public void setPrimaryTable(String primaryTable)
  {
    this.primaryTable = primaryTable;
  } //end of setPrimaryTable method

  /**
   * Returns the Sort Member for this List.
   *
   * @return The Sort Member for this list.
   */
  public String getSortMember()
  {
    return this.sortMember;
  } //end of getSortMember method

  /**
   * Sets the Sort Member for this List.
   *
   * @param sortMember The Sort Member for this list.
   */
  public void setSortMember(String sortMember)
  {
    this.sortMember = sortMember;
  } //end of setSortMember method

  /**
   * Adds a new AccountLineElement to the List.
   *
   * @param ale The new AccountLineElement to be added to the list.
   */
  public void addLine(AccountingLineElement ale)
  {
    ItemElement ie = null;
    try
    {
      ie = (ItemElement) ale;

      //System.out.println("[DEBUG] [ItemLines]: InvoiceLines:addLine()");
      int maxIndex = 0;
      maxIndex = this.getMaxLineId();
      maxIndex += 1;
      ie.setElementId(maxIndex);
      String newKey = ((ListElementMember) ie.get(this.getSortMember())).getSortString() + maxIndex;
      this.put(newKey, ie);
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception]: ItemLines.addLine: " + e.toString());
      e.printStackTrace();
    } //end of catch block (Exception)
  } //end of addLine method

	/**
   * Removes the Line specified from the List.
   *
   * @param lineID The line ID to be removed from the list.
   */
  public void removeLine(int lineID)
  {
    //System.out.println("[DEBUG] [ItemLines]: InvoiceLines:removeLine(int)");

    Set s = this.keySet();
    Iterator itr = s.iterator();
    Vector v = new Vector();
    while (itr.hasNext())
    {
      Object o = itr.next();
      AccountingLineElement ale = (AccountingLineElement) this.get(o);
      int currid = ale.getElementId();
      if (currid == lineID)
      {
        if (!ale.getLineStatus().equals("New"))
        {
          ale.setLineStatus("Deleted");
        } //end of if statement (!ale.getLineStatus().equals("New"))
        else
        {
          v.add(o);
        } //end of else statement (!ale.getLineStatus().equals("New"))
      } //end of if	statement (currid == lineID)
    } //end of while loop (itr.hasNext())
    if (v.size() != 0)
    {
      for (int i = 0; i < v.size(); i++)
      {
        Object o = v.get(i);
        this.remove(o);
      } //end of for loop (int i = 0; i < v.size(); i++)
    } //end of if statement (v.size() != 0)
  } //end of removeLine method

  /**
   * Removes the Lines specified from the List.
   *
   * @param lineID The array of line IDs to be removed from the list.
   */
  public void removeLine(int[] lineID)
  {
    //System.out.println("[DEBUG] [ItemLines]: InvoiceLines:removeLine(int[])");
    for (int i = 0; i < lineID.length; i++)
    {
      this.removeLine(lineID[i]);
    } //end of for loop (int i = 0; i < lineID.length; i++)
  } //end of removeLine method

  /**
   * This method calculates the following items:
   * <ul>
   * <li> Total Quantity
   * <li> Subtotal
   * <li> Total
   * <li> Tax
   * </ul>
   */
  public void calculate()
  {

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
			String lineStatus = ie.getLineStatus();
			if(lineStatus == null)
				lineStatus = "";

			//This is for if we remove item the no need to calculate and refresh the list
			if (!lineStatus.equals("Deleted"))
			{
				ie.calculateLine();
				//	float unitTaxRate =	((Float)((FloatMember)this.get("UnitTaxrate")).getMemberValue()).floatValue();
				if(ie.get("TaxAmount") != null){
					tax += ((Float)((FloatMember)ie.get("TaxAmount")).getMemberValue()).floatValue();
				}
				totalQty += ((Integer)((IntMember)ie.get("Quantity")).getMemberValue()).intValue();
				subtotal += ((Float)((FloatMember)ie.get("PriceExtended")).getMemberValue()).floatValue();
			}
		}//while
		total =  (float)(subtotal + tax + shipping - discount);
		this.setTax(tax);
		this.setTotal(total);
		this.setSubtotal(subtotal);
		this.setTotalItems(totalQty);
  } //end of calculate method
} //end of ItemLines class