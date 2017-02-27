/*
 * $RCSfile: OrderForm.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:07 $ - $Author: mcallist $
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

package com.centraview.account.order;

import java.sql.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;

public class OrderForm extends org.apache.struts.action.ActionForm
{
  private static Logger logger = Logger.getLogger(OrderForm.class);
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
	/**	Stores Customer id
	 */
	private String customerId;  // Entity from Contacts
	private int customerIdValue;
	private String customerName;


	/** Stores Address to which this Order has been Billed */
	private String billToAdd;
	private String billToAddId;
	private int billToAddIdValue;

	/** Stores Address where this Order is to be Shipped */
	private String shipToAdd;
	private String shipToAddId;
	private int shipToAddIdValue;

	/** Stores Current Status of the Order */
	private String status;

	private int statusIdValue;
	private String statusId;

	/** Stores Month on which this Order was Received */
	private String orderMonth;
	private int orderMonthValue;

	/** Stores Day on which this Order was Received */
	private String orderDay;
	private int orderDayValue;

	/** Stores Year on which this Order was Received */
	private String orderYear;
	private int orderYearValue;

	/** Stores Order Number of this Order */
	private String orderId;
	private int orderIdValue;

	/** Stores Purchase Order No for this Order */
	private String po;

	/** Stores Terms (if any ) for this Order */
	private String terms;

	private int termsIdValue;
	private String termsId;

	/** Stores Individual who is Account Manager for this Order */
	private String acctMgr;

	private String acctMgrId;
	private int acctMgrIdValue;

	/** Stores Name of the Project related with this Order */
	private String project;

	private String projectId;
	private int projectIdValue;

	/** Stores Additional Notes for this Order */
	private String notes;


	private java.sql.Date orderDate;

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

	/*
	*	Stores taxAmount related to item
	*/
	private String[] taxAmount;

	private String[] linestatus;

	private ItemLines itemLines;

	private boolean invoiceIsGenerated;

	/*
	*	Stores jurisdictionID
	*/
	private Integer jurisdictionID;

	/*
	*	Stores jurisdictionVec
	*/
	private Vector jurisdictionVec;

	/** The ID of the individual who is modifing the record. */
	private int modifiedBy;

	//private ItemLines itemLines =  new ItemLines(); // this new ItemLines will be removed;

	/**	Returns EntityId for this Order
	*	@return EntityId java.lang.String
	*/
	public String getCustomerId()  // Entity from Contacts
	{
		return this.customerId;
	}

	/**	Set Customer which is Entity Id for this Order
	*	@param createddate String
	*/
	public void setCustomerId(String CustomerId)
	{
		this.customerId = CustomerId;
	}

	public int getCustomerIdValue()
	{
		return this.customerIdValue;
	}

	public void setCustomerIdValue(int customerIdValue)
	{
		this.customerIdValue = customerIdValue;
	}

	/** Return Address to which this Order has been Billed.
	* @return BillToAddress java.lang.String
	*/
	public String getBillToAdd()
	{
		return this.billToAdd;
	}

	/** Sets the Address to which this Order has been Billed
	* @param BillToAdd java.lang.String
	*/
	public void setBillToAdd(String billToAdd)
	{
		this.billToAdd = billToAdd;
	}

	/** returns the Address to where this Order is to be shipped
	* @return ShipToAdd java.lang.String
	*/
	public String getShipToAdd()
	{
		return this.shipToAdd;
	}

	/** Sets the Address to where this Order is to be shipped
	* @param shipToAdd java.lang.String
	*/
	public void setShipToAdd(String shipToAdd)
	{
		this.shipToAdd = shipToAdd;
	}

	/** returns Status about this Order as Pending/Accepted/Rejected
	* @return Status java.lang.String
	*/
	public String getStatus()
	{
		return this.status;
	}

	/** Sets the Status of this Order
	* @param Status java.lang.String
	*/
	public void setStatus(String status)
	{
		this.status = status;
	}

	/** returns OrderMonth if this Order
	* @return OrderMonth java.lang.String
	*/
	public String getOrderMonth()
	{
		return this.orderMonth;
	}

	/** Sets the OrderMonth of this Order
	* @param OrderMonth java.lang.String
	*/
	public void setOrderMonth(String OrderMonth)
	{
		this.orderMonth = OrderMonth;
	}

	/** returns OrderDay if this Order
	* @return OrderDay java.lang.String
	*/
	public String getOrderDay()
	{
		return this.orderDay;
	}

	/** Sets the OrderDay of this Order
	* @param OrderDay java.lang.String
	*/
	public void setOrderDay(String OrderDay)
	{
		this.orderDay = OrderDay;
	}

	/** returns Year if this Order
	* @return Year java.lang.String
	*/
	public String getOrderYear()
	{
		return this.orderYear;
	}

	/** Sets the OrderYear of this Order
	* @param OrderYear java.lang.String
	*/
	public void setOrderYear(String OrderYear)
	{
		this.orderYear = OrderYear;
	}

	/** returns Order No. of this Order
	* @return orderno java.lang.String
	*/
	public String getOrderId()
	{
		return this.orderId;
	}

	/** Sets the Order Number of this order
	* @param orderNo java.lang.String
	*/
	public void setOrderId(String orderid)
	{
		this.orderId = orderid;
	}

	/** returns Purchase Order No associated with this Order
	* @return poNo java.lang.String
	*/
	public String getPo()
	{
		return this.po;
	}

	/** Sets the Purchase Order No associated with this Order
	* @param poNo java.lang.String
	*/
	public void setPo(String po)
	{
		this.po = po;
	}

	/** returns Terms for this Order
	* @return terms java.lang.String
	*/
	public String getTerms()
	{
		return this.terms;
	}

	/** Sets the Terms for this Order
	* @param terms java.lang.String
	*/
	public void setTerms(String terms)
	{
		this.terms = terms;
	}

	/** returns Account Manager for this Order
	* @return individual java.lang.String
	*/
	public String getAcctMgr()
	{
		return this.acctMgr;
	}

	/** Sets the Acount Manager which is a Individual, for this order
	* @param individual java.lang.String
	*/
	public void setAcctMgr(String individualName)  // acctMgr <==> Individual from Contacts
	{
		this.acctMgr = individualName;
	}

	/** returns Project associated with this Order
	* @return projectId java.lang.String
	*/
	public String getProjectId()
	{
		return this.projectId;
	}

	/** Sets the Project Id associated with this Order
	* @param java.lang.String
	*/
	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}

	/** returns
	* @return java.lang.String
	*/
	public String getNotes()
	{
		return this.notes;
	}

	/** Sets the
	* @param java.lang.String
	*/
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/** returns
	* @return java.lang.String
	*/
	public String [] getItemid()
	{
		return this.itemid;
	}
	/** Sets the
	* @param java.lang.String
	*/
	public void setItemid(String [] itemid)
	{
		this.itemid = itemid;
	}



	public int getAcctMgrIdValue()
	{
		return this.acctMgrIdValue;
	}

	public void setAcctMgrIdValue(int acctMgrIdValue)
	{
		this.acctMgrIdValue = acctMgrIdValue;
	}


	public int getBillToAddIdValue()
	{
		return this.billToAddIdValue;
	}

	public void setBillToAddIdValue(int billToAddIdValue)
	{
		this.billToAddIdValue = billToAddIdValue;
	}




	public int getProjectIdValue()
	{
		return this.projectIdValue;
	}

	public void setProjectIdValue(int projectIdValue)
	{
		this.projectIdValue = projectIdValue;
	}


	public int getShipToAddIdValue()
	{
		return this.shipToAddIdValue;
	}

	public void setShipToAddIdValue(int shipToAddIdValue)
	{
		this.shipToAddIdValue = shipToAddIdValue;
	}


	public int getStatusIdValue()
	{
		return this.statusIdValue;
	}

	public void setStatusIdValue(int statusIdValue)
	{
		this.statusIdValue = statusIdValue;
	}


	public int getTermsIdValue()
	{
		return this.termsIdValue;
	}

	public void setTermsIdValue(int termsIdValue)
	{
		this.termsIdValue = termsIdValue;
	}

	public Date getOrderDate()
	{
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}



	public String getCustomerName()
	{
		return this.customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}


	public String getBillToAddId()
	{
		return this.billToAddId;
	}

	public void setBillToAddId(String billToAddId)
	{
		this.billToAddId = billToAddId;
	}


	public String getShipToAddId()
	{
		return this.shipToAddId;
	}

	public void setShipToAddId(String shipToAddId)
	{
		this.shipToAddId = shipToAddId;
	}


	public String getStatusId()
	{
		return this.statusId;
	}

	public void setStatusId(String statusId)
	{
		this.statusId = statusId;
	}


	public String getTermsId()
	{
		return this.termsId;
	}

	public void setTermsId(String termsId)
	{
		this.termsId = termsId;
	}


	public String getAcctMgrId()
	{
		return this.acctMgrId;
	}

	public void setAcctMgrId(String acctMgrId)
	{
		this.acctMgrId = acctMgrId;
	}


	public String getProject()
	{
		return this.project;
	}

	public void setProject(String project)
	{
		this.project = project;
	}


	public int getOrderIdValue()
	{
		return this.orderIdValue;
	}

	public void setOrderIdValue(int orderIdValue)
	{
		this.orderIdValue = orderIdValue;
	}


	public ItemLines getItemLines()
	{
		return this.itemLines;
	}

	public void  setItemLines(ItemLines itemLines)
	{
		this.itemLines = itemLines;
	}

	public void setOrderDayValue(int orderDayValue)
	{
		this.orderDayValue = orderDayValue;
	}

	public int getOrderDayValue()
	{
		return this.orderDayValue;
	}

	public int getOrderMonthValue()
	{
		return this.orderMonthValue;
	}

	public void setOrderMonthValue(int orderMonthValue)
	{
		this.orderMonthValue = orderMonthValue;
	}

	public int getOrderYearValue()
	{
		return this.orderYearValue;
	}

	public void setOrderYearValue(int orderYearValue)
	{
		this.orderYearValue = orderYearValue;
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



    public String[] getLineid()
    {
    	return this.lineid;
    }

    public void setLineid(String[] lineid)
    {
    	this.lineid = lineid;
    }


    public String[] getLinestatus()
    {
    	return this.linestatus;
    }

    public void setLinestatus(String[] linestatus)
    {
    	this.linestatus = linestatus;
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


    public String[] getSku()
    {
    	return this.sku;
    }

    public void setSku(String[] sku)
    {
    	this.sku = sku;
    }


    public String[] getTaxrate()
    {
    	return this.taxrate;
    }

    public void setTaxrate(String[] taxrate)
    {
    	this.taxrate = taxrate;
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

		/**
		 * Sets the modifiedBy for this order.
		 * @param modifiedBy The new modifiedBy for this order.
		 */
		public void setModifiedBy(int modifiedBy)
		{
			this.modifiedBy = modifiedBy;
		} //end of setModifiedBy method

		/**
		 * @return The modifiedBy for this order.
		 */
		public int getModifiedBy()
		{
			return this.modifiedBy;
		} //end of getModifiedBy method

	/*
	 *	Returns string array of taxAmount
	 *	@return taxAmount String[]
	 */
	public String[] getTaxAmount()
	{
		return this.taxAmount;
	}

	/*
	 *	Set taxAmount string array
	 *	@param taxAmount String[]
	 */
	public void setTaxAmount(String[] taxAmount)
	{
		this.taxAmount = taxAmount;
	}

  /**
   * @return The jurisdiction ID.
   */
  public Integer getJurisdictionID()
  {
    return this.jurisdictionID;
  }

  /**
   * Set the Jurisdiction ID
   *
   * @param jurisdictionID
   */
  public void setJurisdictionID(Integer jurisdictionID)
  {
    this.jurisdictionID = jurisdictionID;
  }

  /**
   * @return The jurisdiction Vector.
   */
  public Vector getJurisdictionVec()
  {
    return this.jurisdictionVec;
  }

  /**
   * Set the jurisdiction Vector
   *
   * @param jurisdictionVec
   */
  public void setJurisdictionVec(Vector jurisdictionVec)
  {
    this.jurisdictionVec = jurisdictionVec;
  }

	public OrderForm clearForm(OrderForm orf)
	{
		orf.setAcctMgr("");
		orf.setAcctMgrId("");
		orf.setBillToAdd("");
		orf.setBillToAddId("");
		orf.setShipToAdd("");
		orf.setShipToAddId("");
		orf.setCustomerId("");
		orf.setCustomerName("");
		orf.setNotes("");
		orf.setOrderDay("");
		orf.setOrderMonth("");
		orf.setOrderYear("");
		orf.setOrderId("");
		orf.setProject("");
		orf.setProjectId("");
		orf.setStatus("");
		orf.setStatusId("");
		orf.setTerms("");
		orf.setTermsId("");
		orf.setPo("");
		orf.setJurisdictionID(new Integer(0));
		orf.itemLines = null;
		orf.description = null;
		orf.item = null;
		orf.itemid = null;
		orf.lineid = null;
		orf.linestatus = null;
		orf.orderquantity = null;
		orf.pendingquantity = null;
		orf.priceeach = null;
		orf.priceExtended = null;
		orf.quantity = null;
		orf.sku = null;
		orf.taxrate = null;
		orf.totalprice = null;
		orf.unitprice = null;
		orf.unittax = null;
		return orf;
	}

	public void convertItemLinesToArray()
	{
		if (itemLines != null)
		{
			for(int i = 0; i < itemLines.size(); i++)
			{
				ItemElement ie = (ItemElement)itemLines.get(new Integer(i));
				IntMember LineId = (IntMember)ie.get("LineId");
				IntMember Itemid = (IntMember)ie.get("ItemId");
				StringMember SKU = (StringMember)ie.get("SKU");
				StringMember Description = (StringMember)ie.get("Description");
				IntMember Quantity = (IntMember)ie.get("Quantity");
				FloatMember PriceEach = (FloatMember)ie.get("Price");
				FloatMember PriceExtended = (FloatMember)ie.get("PriceExtended");
				FloatMember TaxAmount = (FloatMember)ie.get("TaxAmount");

				lineid[i] = (String)LineId.getMemberValue();
				itemid[i] = (String)Itemid.getMemberValue();
				sku[i] = (String)SKU.getMemberValue();
				description[i] = (String)Description.getMemberValue();
				quantity[i] = (String)Quantity.getMemberValue();
				String priceEach = (String)PriceEach.getMemberValue();
				priceEach = priceEach.replaceAll(",","");
				priceeach[i] = (String)PriceEach.getMemberValue();
				priceExtended[i] = (String)PriceExtended.getMemberValue();
				taxAmount[i] = (String)TaxAmount.getMemberValue();
			}
		}
	}

	public void convertArrayToItemLines()
	{
		itemLines = new ItemLines();
		if(itemid != null)
		{
			for(int i = 0; i < itemid.length; i++)
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
				taxAmount[i] = taxAmount[i].replaceAll(",","");
				FloatMember	TaxAmount = new FloatMember("TaxAmount",new Float(taxAmount[i]),'D',"",'T',false,20);

				ie.put ("LineId",LineId);
				ie.put ("ItemId",ItemId);
				ie.put ("SKU",SKU);
				ie.put ("Description",Description);
				ie.put ("Quantity",Quantity);
				ie.put ("Price",PriceEach);
				ie.put ("PriceExtended",PriceExtended);
				ie.put ("TaxAmount",TaxAmount);

				String status = linestatus[i];
				if(status == null)
				status = "New";
				ie.setLineStatus(status);

				itemLines.put(new Integer(i),ie);
			}
		}
	}

	public void convertFormbeanToValueObject(String calledFrom)
	{
		try
		{
			if(customerId != null && !customerId.equals(""))
				customerIdValue = Integer.parseInt(customerId);

			if(billToAddId != null && !(billToAddId.trim().equals(""))){
				billToAddIdValue = Integer.parseInt(billToAddId);
			}

			if(shipToAddId != null && !(shipToAddId.trim().equals(""))){
				shipToAddIdValue = Integer.parseInt(shipToAddId);
			}

			if(statusId != null  && !statusId.equals(""))
					statusIdValue = Integer.parseInt(statusId);
			if(termsId != null && !termsId.equals(""))
					termsIdValue = Integer.parseInt(termsId);
			if(acctMgrId != null && !acctMgrId.equals(""))
			acctMgrIdValue = Integer.parseInt(acctMgrId);
			if(projectId != null && !projectId.equals(""))
			projectIdValue = Integer.parseInt(projectId);
			if (calledFrom == null || calledFrom.equals("ADD") || calledFrom.equals("add"))
			{
				orderId = "0";
				orderIdValue = 0;  // set to 0 because there will be no id for new record
			}
			else
			{
				if(orderId != null || !orderId.equals("")){
					orderIdValue = Integer.parseInt(orderId);
				}
			}

			if(orderDay != null && !orderDay.equals(""))
				orderDayValue = Integer.parseInt(orderDay);

			if(orderMonth != null && !orderMonth.equals(""))
					orderMonthValue = Integer.parseInt(orderMonth);

			if(orderYear != null && !orderYear.equals(""))
				orderYearValue = Integer.parseInt(orderYear);

			orderDate = new java.sql.Date(getOrderYearValue()-1900,getOrderMonthValue()-1,getOrderDayValue());

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void convertValueObjectToFormbean()
	{
		customerId = Integer.toString(customerIdValue);

		if(billToAddIdValue != 0){
			billToAddId = Integer.toString(billToAddIdValue);
		}
		if(shipToAddIdValue != 0){
			shipToAddId = Integer.toString(shipToAddIdValue);
		}
		statusId = Integer.toString(statusIdValue);
		termsId = Integer.toString(termsIdValue);
		acctMgrId = Integer.toString(acctMgrIdValue);
		projectId = Integer.toString(projectIdValue);
		orderId = Integer.toString(orderIdValue);


		orderDay = Integer.toString(orderDate.getDay());

		orderDayValue = orderDate.getDay();
		orderMonth = (orderDate.getMonth()+1)+"";
		orderMonthValue = orderDate.getMonth()+1;
		orderYear = orderDate.getYear()+"" ;

		orderYearValue = orderDate.getYear();

	}

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ActionErrors errors = new ActionErrors();
    try {
      try {
        AccountHelperHome accountHelperHome = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome","AccountHelper");
        AccountHelper accHelper = accountHelperHome.create();
        accHelper.setDataSource(dataSource);
        this.jurisdictionVec = accHelper.getTaxJurisdiction();
      } catch (Exception e) {
        this.jurisdictionVec = new Vector();
      }
      
      if (this.getCustomerName() == null || this.getCustomerName().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Customer"));
      }
      
      if (this.getOrderDay() == null || this.getOrderDay().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Date"));
      }
      
      boolean itemPresent =false;
      int counter = 0;
      if (this.itemid != null) {
        for (int i=0;i<this.linestatus.length;i++) {
          if (this.linestatus[i] != null && this.linestatus[i].equalsIgnoreCase("Deleted")) {
            counter++;
          }
        }
        
        if (this.itemid.length > 0 && this.linestatus.length != counter) {
          itemPresent = true;
        }
      }
      
      if (!itemPresent) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must add at least one item."));
      }
      
      if (! errors.isEmpty()) {
        String body = request.getParameter("body");
        
        request.setAttribute("TYPEOFSUBMODULE", "ORDER");
        request.setAttribute("body",body);
        request.setAttribute(AccountConstantKeys.TYPEOFOPERATION,"ShowOrder");
        
        this.convertArrayToItemLines();
        this.convertFormbeanToValueObject(body);
        // for add/edit as itemLines is not populated from JSP page-Form
        request.setAttribute("orderform", this);
        request.setAttribute("ItemLines",this.getItemLines());
      }
    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    }
    return errors;
  }

	public boolean getInvoiceIsGenerated()
	{
		return this.invoiceIsGenerated;
	}

	public void setInvoiceIsGenerated(boolean invoiceIsGenerated)
	{
		this.invoiceIsGenerated = invoiceIsGenerated;
	}

  public String toString()
  {
    StringBuffer sb = new StringBuffer("");
    sb.append("OrderForm = [");
    sb.append("status = {" + this.status + "}, ");
    sb.append("statusId = {" + this.statusId + "}, ");
    sb.append("statusIdValue = {" + this.statusIdValue + "}, ");
    sb.append("customerId = {" + this.customerId + "}, ");
    sb.append("customerIdValue = {" + this.customerIdValue + "}, ");
    sb.append("customerName = {" + this.customerName + "}, ");
    sb.append("billToAdd = {" + this.billToAdd + "}, ");
    sb.append("billToAddId = {" + this.billToAddId + "}, ");
    sb.append("billToAddIdValue = {" + this.billToAddIdValue + "}, ");
    sb.append("shipToAdd = {" + this.shipToAdd + "}, ");
    sb.append("shipToAddId = {" + this.shipToAddId + "}, ");
    sb.append("shipToAddIdValue = {" + this.shipToAddIdValue + "}, ");
    sb.append("orderMonth = {" + this.orderMonth + "}, ");
    sb.append("orderMonthValue = {" + this.orderMonthValue + "}, ");
    sb.append("orderDay = {" + this.orderDay + "}, ");
    sb.append("orderDayValue = {" + this.orderDayValue + "}, ");
    sb.append("orderYear = {" + this.orderYear + "}, ");
    sb.append("orderYearValue = {" + this.orderYearValue + "}, ");
    sb.append("orderId = {" + this.orderId + "}, ");
    sb.append("orderIdValue = {" + this.orderIdValue + "}, ");
    sb.append("po = {" + this.po + "}, ");
    sb.append("terms = {" + this.terms + "}, ");
    sb.append("termsId = {" + this.termsId + "}, ");
    sb.append("termsIdValue = {" + this.termsIdValue + "}, ");
    sb.append("acctMgr = {" + this.acctMgr + "}, ");
    sb.append("acctMgrId = {" + this.acctMgrId + "}, ");
    sb.append("acctMgrIdValue = {" + this.acctMgrIdValue + "}, ");
    sb.append("project = {" + this.project + "}, ");
    sb.append("projectId = {" + this.projectId + "}, ");
    sb.append("projectIdValue = {" + this.projectIdValue + "}, ");
    sb.append("notes = {" + this.notes + "}, ");
    sb.append("itemLines = {" + this.itemLines + "}, ");
    sb.append("invoiceIsGenerated = {" + this.invoiceIsGenerated + "}, ");
    sb.append("orderDate = {" + this.orderDate + "}, ");
    sb.append("priceeach = {" + this.priceeach + "}, ");
    sb.append("priceExtended = {" + this.priceExtended + "}, ");
    sb.append("sku = {" + this.sku + "}, ");
    sb.append("itemid = {" + this.itemid + "}, ");
    sb.append("item = {" + this.item + "}, ");
    sb.append("description = {" + this.description + "}, ");
    sb.append("quantity = {" + this.quantity + "}, ");
    sb.append("unitprice = {" + this.unitprice + "}, ");
    sb.append("totalprice = {" + this.totalprice + "}, ");
    sb.append("unittax = {" + this.unittax + "}, ");
    sb.append("taxrate = {" + this.taxrate + "}, ");
    sb.append("orderquantity = {" + this.orderquantity + "}, ");
    sb.append("pendingquantity = {" + this.pendingquantity + "}, ");
    sb.append("lineid = {" + this.lineid + "}, ");
    sb.append("taxAmount = {" + this.taxAmount + "}, ");
    sb.append("linestatus = {" + this.linestatus + "}, ");
    sb.append("]");
    return(sb.toString());
  }

}

