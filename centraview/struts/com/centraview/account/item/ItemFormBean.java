/*
 * $RCSfile: ItemFormBean.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:19 $ - $Author: mking_cv $
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

package com.centraview.account.item;
import java.util.Date;

public class ItemFormBean
{
	private int itemID;
	private String itemName;
	private String itemDesc;		
	private String sku;			
	private int qty;	
	private float price;
	private float cost;	
	private int type;
	private String typeDesc;				
	private int taxClassID;
	private String taxClassDesc;				
	private int parentID;
	private Date created;
	private Date modified;			

	

	/**
	 *	Cost of Item
	 *
	 * @return     
	 */
	public float getCost()
	{
		return this.cost;
	}


	/**
	 *	Cost of Item
	 *	
	 * @param   cost  
	 */
	public void setCost(float cost)
	{
		this.cost = cost;
	}

	

	/**
	 *	Created Date
	 *
	 * @return     
	 */
	public Date getCreated()
	{
		return this.created;
	}


	/**
	 *	Created Date
	 *
	 * @param   created  
	 */
	public void setCreated(Date created)
	{
		this.created = created;
	}

	

	/**
	 *	Item Description
	 *
	 * @return     
	 */
	public String getItemDesc()
	{
		return this.itemDesc;
	}


	/**
	 *	Item Description
	 *
	 * @param   itemDesc  
	 */
	public void setItemDesc(String itemDesc)
	{
		this.itemDesc = itemDesc;
	}

	

	/**
	 *	Item ID
	 *
	 * @return     
	 */
	public int getItemID()
	{
		return this.itemID;
	}


	/**
	 *	Item ID	
	 *	
	 * @param   itemID  
	 */
	public void setItemID(int itemID)
	{
		this.itemID = itemID;
	}

	

	/**
	 *	Item Name
	 *
	 * @return     
	 */
	public String getItemName()
	{
		return this.itemName;
	}


	/**
	 *	Item Name
	 *	
	 * @param   itemName  
	 */
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	

	/**
	 *	Modified Date
	 *
	 * @return     
	 */
	public Date getModified()
	{
		return this.modified;
	}


	/**
	 *	Modified Date
	 *
	 * @param   modified  
	 */
	public void setModified(Date modified)
	{
		this.modified = modified;
	}

	

	/**
	 *	Parent Item ID
	 *
	 * @return     
	 */
	public int getParentID()
	{
		return this.parentID;
	}


	/**
	 *	Parent Item ID
	 *
	 * @param   parentID  
	 */
	public void setParentID(int parentID)
	{
		this.parentID = parentID;
	}

	

	/**
	 *	Price of the Item
	 *
	 * @return     
	 */
	public float getPrice()
	{
		return this.price;
	}


	/**
	 *	Price of the Item
	 *
	 * @param   price  
	 */
	public void setPrice(float price)
	{
		this.price = price;
	}

	

	/**
	 *	Qty. of Item
	 *
	 * @return     
	 */
	public int getQty()
	{
		return this.qty;
	}


	/**
	 * Qty. of Item
	 *
	 * @param   qty  
	 */
	public void setQty(int qty)
	{
		this.qty = qty;
	}

	

	/**
	 *	SKU
	 *
	 * @return     
	 */
	public String getSku()
	{
		return this.sku;
	}


	/**
	 *	SKU
	 *
	 * @param   sku  
	 */
	public void setSku(String sku)
	{
		this.sku = sku;
	}

	

	/**
	 *	Tax Class Description
	 *
	 * @return     
	 */
	public String getTaxClassDesc()
	{
		return this.taxClassDesc;
	}


	/**
	 *	Tax Class Description
	 *
	 * @param   taxClassDesc  
	 */
	public void setTaxClassDesc(String taxClassDesc)
	{
		this.taxClassDesc = taxClassDesc;
	}	

	

	/**
	 *	Tax Class ID
	 *
	 * @return     
	 */
	public int getTaxClassID()
	{
		return this.taxClassID;
	}


	/**
	 *	Tax Class ID
	 *	
	 * @param   taxClassID  
	 */
	public void setTaxClassID(int taxClassID)
	{
		this.taxClassID = taxClassID;
	}

	

	/**
	 *	
	 *
	 * @return     
	 */
	public int getType()
	{
		return this.type;
	}


	/**
	 *
	 *
	 * @param   type  
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	

	/**
	 *
	 *
	 * @return     
	 */
	public String getTypeDesc()
	{
		return this.typeDesc;
	}


	/**
	 *
	 *
	 * @param   typeDesc  
	 */
	public void setTypeDesc(String typeDesc)
	{
		this.typeDesc = typeDesc;
	}
}