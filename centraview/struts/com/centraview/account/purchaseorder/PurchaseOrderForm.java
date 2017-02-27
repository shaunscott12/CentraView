/*
 * $RCSfile: PurchaseOrderForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.account.purchaseorder;

// java import package
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.Validation;

public class PurchaseOrderForm extends org.apache.struts.action.ActionForm
{

	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

	private String[] linestatus;

	/*
	* Save vendor id and name
	*/
	private String vendorId;
	private String vendorName;

	/*
	*	Stores bill to and ship to addresses and ids
	*/
	private String billto;
	private String shipto;

	private String billtoID;
	private String shiptoID;

	/*
	*	Stores statusid and value
	*/
	private String statusid;
	private String statusValue;

	/*
	*	Stores month
	*/
	private String month;
	/*
	*	Stores date
	*/
	private String date;
	/*
	*	Stores year
	*/
	private String year;

	private java.sql.Date purchaseOrderDate;

	/*
	*	Stores poid and poname
	*/
	private String poid;
	private String poname;
	private String purchaseOrderid;

	/*
	*	Stores terms id and value
	*/
	private String termid;
	private String termValue;

	/*
	*	Stores account manager id and value
	*/
	private String accountmanagerid;
	private String accountmanagerName;

	// Variables for the item list follow:
	/*
	*	Stores notes
	*/
	private String notes;

	/*
	*	Stores priceeach to item
	*/
	private String[] priceeach;

	/*
	*	Stores priceextended to item
	*/
	private String[] priceExtended;

	/*
	*	Stores sku related to item
	*/
	private String[] sku;

	/*
	*	Stores itemid related to item
	*/
	private String[] itemid;

	/*
	*	Stores item related to item
	*/
	private String[] item;

	/*
	*	Stores description related to item
	*/
	private String[] description;

	/*
	*	Stores quqntity related to item
	*/
	private String[] quantity;

	/*
	*	Stores unit price  related to item
	*/
	private String[] unitprice;

	/*
	*	Stores total price related to item
	*/
	private String[] totalprice;

	/*
	*	Stores unit tax related to item
	*/
	private String[] unittax;
	/*
	*	Stores taxrate related to item
	*/
	private String[] taxrate;
	/*
	*	Stores orderquantity related to item
	*/
	private String[] orderquantity;
	/*
	*	Stores pendingquantity related to item
	*/
	private String[] pendingquantity;

	/*
	*	Stores lineid related to item
	*/
	private String[] lineid;

	private ItemLines itemLines ;

	private int modifiedBy;

	public void	convertItemLines()
	{
	  int count;
	  itemLines= new ItemLines();

		if (itemid != null)
		{
		  for (int i=0;i<itemid.length;i++)
		  {

				ItemElement ie = new ItemElement();
				IntMember LineId = new IntMember("LineId",Integer.parseInt(lineid[i]),'D',"",'T',false,20);
				IntMember ItemId = new IntMember("ItemId",Integer.parseInt(itemid[i]),'D',"",'T',false,20);
				IntMember Quantity = new IntMember("Quantity",Integer.parseInt(quantity[i]),'D',"",'T',false,20);
				priceeach[i] = priceeach[i].replaceAll(",","");
				FloatMember	PriceEach = new FloatMember("Price",new Float(priceeach[i]),'D',"",'T',false,20);
				StringMember SKU = new StringMember("SKU",sku[i],'D',"",'T',false);
				StringMember Description = new StringMember("Description",description[i],'D',"",'T',false);
				if(priceExtended[i] != null){
					priceExtended[i] = priceExtended[i].replaceAll(",","");
				}else{
					priceExtended[i] = "";
				}
				FloatMember	PriceExtended = new FloatMember("PriceExtended",new Float(priceExtended[i]),'D',"",'T',false,20);

				ie.put ("LineId",LineId);
				ie.put ("ItemId",ItemId);
				ie.put ("SKU",SKU);
				ie.put ("Description",Description);
				ie.put ("Quantity",Quantity);
				ie.put ("Price",PriceEach);
				ie.put ("PriceExtended",PriceExtended);

				String status = linestatus[i];
				if(status == null)
					status = "New";
				ie.setLineStatus(status);

				itemLines.put(new Integer(i),ie);
		  }// end of for (int i=0;i<itemid.length;i++)
		}// end of if (itemid != null)
	}// end of public void	convertItemLines()





	public String getAccountmanagerid()
	{
		return this.accountmanagerid;
	}

	public void setAccountmanagerid(String accountmanagerid)
	{
		this.accountmanagerid = accountmanagerid;
	}


	public String getAccountmanagerName()
	{
		return this.accountmanagerName;
	}

	public void setAccountmanagerName(String accountmanagerName)
	{
		this.accountmanagerName = accountmanagerName;
	}


	public String getBillto()
	{
		return this.billto;
	}

	public void setBillto(String billto)
	{
		this.billto = billto;
	}


	public String getDate()
	{
		return this.date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}


	public String[] getDescription()
	{
		return this.description;
	}

	public void setDescription(String[] description)
	{
		this.description = description;
	}


	public String[] getItem()
	{
		return this.item;
	}

	public void setItem(String[] item)
	{
		this.item = item;
	}


	public String[] getItemid()
	{
		return this.itemid;
	}

	public void setItemid(String[] itemid)
	{
		this.itemid = itemid;
	}


	public ItemLines getItemLines()
	{
		return this.itemLines;
	}

	public void setItemLines(ItemLines itemLines)
	{
		this.itemLines = itemLines;
	}


	public String[] getLineid()
	{
		return this.lineid;
	}

	public void setLineid(String[] lineid)
	{
		this.lineid = lineid;
	}


	public String getMonth()
	{
		return this.month;
	}

	public void setMonth(String month)
	{
		this.month = month;
	}


	public String getNotes()
	{
		return this.notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}


	public String[] getOrderquantity()
	{
		return this.orderquantity;
	}

	public void setOrderquantity(String[] orderquantity)
	{
		this.orderquantity = orderquantity;
	}


	public String[] getPendingquantity()
	{
		return this.pendingquantity;
	}

	public void setPendingquantity(String[] pendingquantity)
	{
		this.pendingquantity = pendingquantity;
	}


	public String getPoid()
	{
		return this.poid;
	}

	public void setPoid(String poid)
	{
		this.poid = poid;
	}


	public String getPoname()
	{
		return this.poname;
	}

	public void setPoname(String poname)
	{
		this.poname = poname;
	}


	public String[] getPriceeach()
	{
		return this.priceeach;
	}

	public void setPriceeach(String[] priceeach)
	{
		this.priceeach = priceeach;
	}


	public String[] getPriceExtended()
	{
		return this.priceExtended;
	}

	public void setPriceExtended(String[] priceExtended)
	{
		this.priceExtended = priceExtended;
	}


	public String[] getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(String[] quantity)
	{
		this.quantity = quantity;
	}


	public String getShipto()
	{
		return this.shipto;
	}

	public void setShipto(String shipto)
	{
		this.shipto = shipto;
	}


	public String[] getSku()
	{
		return this.sku;
	}

	public void setSku(String[] sku)
	{
		this.sku = sku;
	}


	public String getStatusid()
	{
		return this.statusid;
	}

	public void setStatusid(String statusid)
	{
		this.statusid = statusid;
	}


	public String getStatusValue()
	{
		return this.statusValue;
	}

	public void setStatusValue(String statusValue)
	{
		this.statusValue = statusValue;
	}


	public String[] getTaxrate()
	{
		return this.taxrate;
	}

	public void setTaxrate(String[] taxrate)
	{
		this.taxrate = taxrate;
	}


	public String getTermid()
	{
		return this.termid;
	}

	public void setTermid(String termid)
	{
		this.termid = termid;
	}


	public String getTermValue()
	{
		return this.termValue;
	}

	public void setTermValue(String termValue)
	{
		this.termValue = termValue;
	}


	public String[] getTotalprice()
	{
		return this.totalprice;
	}

	public void setTotalprice(String[] totalprice)
	{
		this.totalprice = totalprice;
	}


	public String[] getUnitprice()
	{
		return this.unitprice;
	}

	public void setUnitprice(String[] unitprice)
	{
		this.unitprice = unitprice;
	}


	public String[] getUnittax()
	{
		return this.unittax;
	}

	public void setUnittax(String[] unittax)
	{
		this.unittax = unittax;
	}


	public String getVendorId()
	{
		return this.vendorId;
	}

	public void setVendorId(String vendorId)
	{
		this.vendorId = vendorId;
	}


	public String getVendorName()
	{
		return this.vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}


	public String getYear()
	{
		return this.year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}


	public String getBilltoID()
	{
		return this.billtoID;
	}

	public void setBilltoID(String billtoID)
	{
		this.billtoID = billtoID;
	}


	public String getShiptoID()
	{
		return this.shiptoID;
	}

	public void setShiptoID(String shiptoID)
	{
		this.shiptoID = shiptoID;
	}



	public String getPurchaseOrderid()
	{
		return this.purchaseOrderid;
	}

	public void setPurchaseOrderid(String purchaseOrderid)
	{
		this.purchaseOrderid = purchaseOrderid;
	}



	public java.sql.Date getPurchaseOrderDate()
	{
		int month = 0;
		if(this.getMonth() != null && !((this.getMonth()).equals("")))
			month = Integer.parseInt(this.getMonth());

		int date = 0;
		if(this.getDate() != null && !((this.getDate()).equals("")))
			date = Integer.parseInt(this.getDate());

		int year = 0;
		if(this.getYear() != null && !((this.getYear()).equals("")))
			year = Integer.parseInt(this.getYear());

		purchaseOrderDate = new java.sql.Date(year,month,date);

		return this.purchaseOrderDate;
	}

	public void setPurchaseOrderDate(java.sql.Date purchaseOrderDate)
	{
		this.purchaseOrderDate = purchaseOrderDate;
	}



	public String[] getLinestatus()
	{
		return this.linestatus;
	}

	public void setLinestatus(String[] linestatus)
	{
		this.linestatus = linestatus;
	}

	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();

		try
		{
			// initialize validation
			Validation validation = new Validation();

			this.convertItemLines();

      if (this.getVendorId() == null || this.getVendorId().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Vendor"));
      }

      if (this.getBilltoID() == null || this.getBilltoID().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Billing Address"));
      }
      
      if (this.getShiptoID() == null || this.getShiptoID().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Shipping Address"));
      }

			boolean itemPresent = false;
			int counter = 0;

			if (this.itemid != null)
			{
				ItemLines lines = this.getItemLines();

				for (int i=0;i<this.linestatus.length;i++)
				{
					if (this.linestatus[i] != null && this.linestatus[i].equalsIgnoreCase("Deleted"))
					{
						counter++;
					}
				}

				if (this.itemid.length > 0 && this.linestatus.length != counter)
				{
					itemPresent = true;
				}
			}

			if (itemPresent == false)
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Items"));
			}

			if (errors != null)
			{
				request.setAttribute("body", "ADD");
				request.setAttribute("clearform", "false");
				request.setAttribute("purchaseForm", this);
				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PURCHASEORDER);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return errors;
	}

	/**
	 * Sets the modifiedBy for this PurchaseOrder.
	 * @param modifiedBy The new modifiedBy for this PurchaseOrder.
	 */
	public void setModifiedBy(int modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	} //end of setModifiedBy method

	/**
	 * @return The modifiedBy for this PurchaseOrder.
	 */
	public int getModifiedBy()
	{
		return this.modifiedBy;
	} //end of getModifiedBy method

	public static PurchaseOrderForm clearForm(PurchaseOrderForm purchaseForm)
	{
		purchaseForm.setAccountmanagerid("");
		purchaseForm.setAccountmanagerName("");
		purchaseForm.setBillto("");
		purchaseForm.setBilltoID("");
		purchaseForm.setDate("");
		purchaseForm.setPurchaseOrderDate(null);
		purchaseForm.setPurchaseOrderid("");
		purchaseForm.setMonth("");
		purchaseForm.setNotes("");
		purchaseForm.setVendorId("");
		purchaseForm.setVendorName("");
		purchaseForm.setPoname("");
		purchaseForm.setShipto("");
		purchaseForm.setShiptoID("");
		purchaseForm.setStatusid("");
		purchaseForm.setStatusValue("");
		purchaseForm.setTermid("");
		purchaseForm.setYear("");
		purchaseForm.itemLines = null;
		return purchaseForm;
	}
}
