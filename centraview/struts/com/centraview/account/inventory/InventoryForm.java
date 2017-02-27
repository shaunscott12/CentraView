/*
 * $RCSfile: InventoryForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:15 $ - $Author: mking_cv $
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
File Name:	InventoryForm.java
Purpose :	This file is used for storing Inventory Item Details
Author :	Shilpa Patil
Date :		5th Sept 2003
Change History:
	By		Version		Date		Purpose

*/

package com.centraview.account.inventory;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.centraview.common.DDNameValue;

public class InventoryForm extends org.apache.struts.action.ActionForm
{
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
	//As hidden param
	private int inventoryID;
	private String formType = "new";
	
	/**	Stores Item id
	 */
	 
	private String itemID; 
	private int itemIDValue;
	private String itemName;
	
	private String identifier;
	
	private String locationID; 
	private int locationIDValue;
	private String locationName;
	
	private String manufactureID; 
	private int manufactureIDValue;
	private String manufacturer;
	
	private String vendorID;
	private int vendorIDValue;
	private String vendorName;
	
	private String description;
	
	private String soldToID;
	private int soldToIDValue;
	private String soldToName;
	
	private String status;
	private String statusID;
	private int statusIDValue;
	private Vector statusVec;
	
	private String created;
	private String modified;
	
	private Vector customFieldsVec;
	
	private int qty;


	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	
	public String getIdentifier()
	{
		return this.identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	
	public String getItemID()
	{
		return this.itemID;
	}

	public void setItemID(String itemID)
	{
		this.itemID = itemID;
	}

	
	public int getItemIDValue()
	{
		return this.itemIDValue;
	}

	public void setItemIDValue(int itemIDValue)
	{
		this.itemIDValue = itemIDValue;
	}

	
	public String getItemName()
	{
		return this.itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	
	public String getLocationID()
	{
		return this.locationID;
	}

	public void setLocationID(String locationID)
	{
		this.locationID = locationID;
	}

	
	public int getLocationIDValue()
	{
		return this.locationIDValue;
	}

	public void setLocationIDValue(int locationIDValue)
	{
		this.locationIDValue = locationIDValue;
	}

	
	public String getLocationName()
	{
		return this.locationName;
	}

	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}

	
	public String getManufacturer()
	{
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	
	public String getSoldToID()
	{
		return this.soldToID;
	}

	public void setSoldToID(String soldToID)
	{
		this.soldToID = soldToID;
	}

	
	public int getSoldToIDValue()
	{
		return this.soldToIDValue;
	}

	public void setSoldToIDValue(int soldToIDValue)
	{
		this.soldToIDValue = soldToIDValue;
	}

	
	public String getSoldToName()
	{
		return this.soldToName;
	}

	public void setSoldToName(String soldToName)
	{
		this.soldToName = soldToName;
	}

	
	public String getVendorID()
	{
		return this.vendorID;
	}

	public void setVendorID(String vendorID)
	{
		this.vendorID = vendorID;
	}

	
	public int getVendorIDValue()
	{
		return this.vendorIDValue;
	}

	public void setVendorIDValue(int vendorIDValue)
	{
		this.vendorIDValue = vendorIDValue;
	}

	
	public String getVendorName()
	{
		return this.vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}
	
	
	public void convertFormbeanToValueObject()
	{
		try
		{
			if(itemID != null && itemID.length() != 0)
				this.itemIDValue = Integer.parseInt(itemID);
				
			if(locationID != null && locationID.length() != 0)
				this.locationIDValue = Integer.parseInt(locationID);
			
			if(vendorID != null && vendorID.length() != 0)
				this.vendorIDValue = Integer.parseInt(vendorID);
				
			if(soldToID != null && soldToID.length() != 0)
				this.soldToIDValue = Integer.parseInt(soldToID);
			
			if(manufactureID != null && manufactureID.length() != 0)
				this.manufactureIDValue = Integer.parseInt(manufactureID);
			
			if(statusID != null && statusID.length() != 0)	
				this.statusIDValue = Integer.parseInt(statusID);
		}
		catch(Exception e)
		{
			System.out.println("Error while converting strings to int"+e);
		}
			
	}	
			

	public Vector getStatusVec()
	{
		Vector vec =  new Vector();
		
		vec.add(new DDNameValue(1,"Available"));
		vec.add(new DDNameValue(2,"BackOrdered"));
		vec.add(new DDNameValue(3,"Not Available"));
		
		statusVec = vec;
		
		return this.statusVec;
	}

	public void setStatusVec(Vector statusVec)
	{
		this.statusVec = statusVec;
	}

	
	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	
	public String getStatusID()
	{
		return this.statusID;
	}

	public void setStatusID(String statusID)
	{
		this.statusID = statusID;
	}

	
	public String getCreated()
	{
		return this.created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	
	public String getModified()
	{
		return this.modified;
	}

	public void setModified(String modified)
	{
		this.modified = modified;
	}

	
	public int getInventoryID()
	{
		return this.inventoryID;
	}

	public void setInventoryID(int intInventoryID)
	{
		this.inventoryID = intInventoryID;
	}

	
	public String getFormType()
	{
		return this.formType;
	}

	public void setFormType(String formType)
	{
		this.formType = formType;
	}

	
	public int getQty()
	{
		return this.qty;
	}

	public void setQty(int qty)
	{
		this.qty = qty;
	}

	
	public int getManufactureIDValue()
	{
		return this.manufactureIDValue;
	}

	public void setManufactureIDValue(int manufactureIDValue)
	{
		this.manufactureIDValue = manufactureIDValue;
	}

	
	public String getManufactureID()
	{
		return this.manufactureID;
	}

	public void setManufactureID(String manufactureID)
	{
		this.manufactureID = manufactureID;
	}

	
	public int getStatusIDValue()
	{
		return this.statusIDValue;
	}

	public void setStatusIDValue(int statusIDValue)
	{
		this.statusIDValue = statusIDValue;
	}

	
	public Vector getCustomFieldsVec()
	{
		return this.customFieldsVec;
	}

	public void setCustomFieldsVec(Vector customFieldsVec)
	{
		this.customFieldsVec = customFieldsVec;
	}
	
	
	/* For Form Validation  */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
	    ActionErrors errors = new ActionErrors();
	    return errors;
	}
	
	
}
