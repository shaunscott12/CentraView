/*
 * $RCSfile: InvoiceForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.account.invoice;

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
import com.centraview.common.Validation;
import com.centraview.settings.Settings;


public class InvoiceForm extends org.apache.struts.action.ActionForm
{
	private static Logger logger = Logger.getLogger(InvoiceForm.class);


	private int customerIdValue;
	/*
	*	Stores invoice id
	*/
	private String customerId;

	/*
	*	Stores invoice id
	*/
	private String invoiceid;

	/*
	*	Stores orderid
	*/
	private String orderid;

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

	/*
	*	Stores orderno
	*/

	private String orderno;

	/*
	*	Stores ordername
	*/
	private String ordername;

	private String billtoID;

	/*
	*	Stores billto
	*/
	private String billto;

	private String shiptoID;
	private int shiptoIDValue;
	/*
	*	Stores shipto
	*/
	private String shipto;

	private int statusIDValue;
	/*
	*	Stores statusid
	*/
	private String statusid;


	private String invoicedate;
	/*
	*	Stores invoicedate
	*/
	private String externalid;
	/*
	*	Stores externalid
	*/

	/*
	*	Stores poid
	*/
	private String poid;
	/*
	*	Stores poname
	*/
	private String poname;


	private String termid;
	private int termidValue;

	private String accountmanagerName;

	private int accountmanageridValue;
	/*
	*	Stores accountmanagerid
	*/
	private String accountmanagerid;

	/*
	*	Stores projectid
	*/
	private String projectid;
	private int projectidValue;
	private String projectName;



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
	*	Stores unit tax Class related to item
	*/
	private String[] taxAmount;

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

	private String[] linestatus;


	private java.sql.Date invoiceDate;
	private ItemLines itemLines ;//=  new ItemLines(); // this new ItemLines will be removed;

	private int modifiedBy;

	/*
	*	Stores jurisdictionID
	*/
	private Integer jurisdictionID;

	/*
	*	Stores jurisdictionVec
	*/
	private Vector jurisdictionVec;

	/*
	 *	Return invoiceid string
	 *	@return invoiceid String
	 */
	public String getInvoiceid()
	{
		return this.invoiceid;
	}

	/*
	 *	Set invoiceid string
	 *	@param invoiceid String
	 */
	public void setInvoiceid(String invoiceid)
	{
		this.invoiceid = invoiceid;
	}


	/*
	 *	Return orderid string
	 *	@return orderid String
	 */
	public String getOrderid()
	{
		return this.orderid;
	}

	/*
	 *	Set orderid string
	 *	@param orderid String
	 */
	public void setOrderid(String orderid)
	{
		this.orderid = orderid;
	}


	/*
	 *	Return orderno string
	 *	@return orderno String
	 */
	public String getOrderno()
	{
		return this.orderno;
	}

	/*
	 *	Set orderno string
	 *	@param orderno String
	 */
	public void setOrderno(String orderno)
	{
		this.orderno = orderno;
	}

	/*
	 *	Return ordername string
	 *	@return ordername String
	 */
	public String getOrdername()
	{
		return this.ordername;
	}

	/*
	 *	Set ordername string
	 *	@param ordername String
	 */
	public void setOrdername(String ordername)
	{
		this.ordername = ordername;
	}

	/*
	 *	Return billto string
	 *	@return billto String
	 */
	public String getBillto()
	{
		return this.billto;
	}

	/*
	 *	Set billto string
	 *	@param billto String
	 */
	public void setBillto(String billto)
	{
		this.billto = billto;
	}

	/*
	 *	Return shipto string
	 *	@return shipto String
	 */
	public String getShipto()
	{
		return this.shipto;
	}

	/*
	 *	Set shipto string
	 *	@param shipto String
	 */
	public void setShipto(String shipto)
	{
		this.shipto = shipto;
	}

	/*
	 *	Return statusid string
	 *	@return statusid String
	 */
	public String getStatusid()
	{
		return this.statusid;
	}

	/*
	 *	Set statusid string
	 *	@param statusid String
	 */
	public void setStatusid(String statusid)
	{
		this.statusid = statusid;
	}

	/*
	 *	Return invoicedate string
	 *	@return invoicedate String
	 */
	public String getInvoicedate()
	{

		return this.invoicedate;
	}

	/*
	 *	Set invoicedate string
	 *	@param invoicedate String
	 */
	public void setInvoicedate(String invoicedate)
	{

		this.invoicedate = invoicedate;
	}

	/*
	 *	Return externalid string
	 *	@return externalid String
	 */
	public String getExternalid()
	{
		if(externalid == null || externalid.equals("null")){
			externalid = "";
		}
		return this.externalid;
	}

	/*
	 *	Set externalid string
	 *	@param externalid String
	 */
	public void setExternalid(String externalid)
	{
		this.externalid = externalid;
	}

	/*
	 *	Return poid string
	 *	@return poid String
	 */
	public String getPoid()
	{
		return this.poid;
	}

	/*
	 *	Set poid string
	 *	@param poid String
	 */
	public void setPoid(String poid)
	{
		this.poid = poid;
	}

	/*
	 *	Return termid string
	 *	@return termid String
	 */
	public String getTermid()
	{
		return this.termid;
	}

	/*
	 *	Set termid string
	 *	@param termid String
	 */
	public void setTermid(String termid)
	{
		this.termid = termid;
	}

	/*
	 *	Return accountmanagerid string
	 *	@return accountmanagerid String
	 */
	public String getAccountmanagerid()
	{
		return this.accountmanagerid;
	}

	/*
	 *	Set accountmanagerid string
	 *	@param accountmanagerid String
	 */
	public void setAccountmanagerid(String accountmanagerid)
	{
		this.accountmanagerid = accountmanagerid;
	}

	/*
	 *	Return projectid string
	 *	@return projectid String
	 */
	public String getProjectid()
	{
		return this.projectid;
	}

	/*
	 *	Set projectid string
	 *	@param projectid String
	 */
	public void setProjectid(String projectid)
	{
		this.projectid = projectid;
	}

	/*
	 *	Return notes string
	 *	@return notes String
	 */
	public String getNotes()
	{
		return this.notes;
	}

	/*
	 *	Set notes string
	 *	@param notes String
	 */
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/*
	 *	Returns string array of itemid
	 *	@return itemid String[]
	 */
	public String[] getItemid()
	{
		return this.itemid;
	}

	/*
	 *	Set itemid string array
	 *	@param itemid String[]
	 */
	public void setItemid(String[] itemid)
	{
		this.itemid = itemid;
	}

	/*
	 *	Returns string array of item
	 *	@return item String[]
	 */
	public String[] getItem()
	{
		return this.item;
	}

	/*
	 *	Set item string array
	 *	@param item String[]
	 */
	public void setItem(String[] item)
	{
		this.item = item;
	}

	/*
	 *	Stores subtotal related to item
	 */
	private String[] subTotal;

	/*
	 *	Returns string array of description
	 *	@return description String[]
	 */

	public String[] getDescription()
	{
		return this.description;
	}

	/*
	 *	Set description string array
	 *	@param description String[]
	 */
	public void setDescription(String[] description)
	{
		this.description = description;
	}

	/*
	 *	Returns string array of quantity
	 *	@return quantity String[]
	 */
	public String[] getQuantity()
	{
		return this.quantity;
	}

	/*
	 *	Set quantity string array
	 *	@param quantity String[]
	 */
	public void setQuantity(String[] quantity)
	{
		this.quantity = quantity;
	}

	/*
	 *	Returns string array of unitprice
	 *	@return unitprice String[]
	 */
	public String[] getUnitprice()
	{
		return this.unitprice;
	}

	/*
	 *	Set unitprice string array
	 *	@param unitprice String[]
	 */
	public void setUnitprice(String[] unitprice)
	{
		this.unitprice = unitprice;
	}

	/*
	 *	Returns string array of totalprice
	 *	@return totalprice String[]
	 */
	public String[] getTotalprice()
	{
		return this.totalprice;
	}

	/*
	 *	Set totalprice string array
	 *	@param totalprice String[]
	 */
	public void setTotalprice(String[] totalprice)
	{
		this.totalprice = totalprice;
	}

	/*
	 *	Returns string array of unittax
	 *	@return unittax String[]
	 */
	public String[] getUnittax()
	{
		return this.unittax;
	}

	/*
	 *	Set unittax string array
	 *	@param unittax String[]
	 */
	public void setUnittax(String[] unittax)
	{
		this.unittax = unittax;
	}

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

	public int getAccountmanageridValue()
	{
		return this.accountmanageridValue;
	}

	public void setAccountmanageridValue(int accountmanageridValue)
	{
		this.accountmanageridValue = accountmanageridValue;
	}


	public String getAccountmanagerName()
	{
		return this.accountmanagerName;
	}

	public void setAccountmanagerName(String accountmanagerName)
	{
		this.accountmanagerName = accountmanagerName;
	}







	public int getProjectidValue()
	{
		return this.projectidValue;
	}

	public void setProjectidValue(int projectidValue)
	{
		this.projectidValue = projectidValue;
	}



	public String getProjectName()
	{
		return this.projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}


	public int getStatusIDValue()
	{
		return this.statusIDValue;
	}

	public void setStatusIDValue(int statusIDValue)
	{
		this.statusIDValue = statusIDValue;
	}


	public int getShiptoIDValue()
	{
		return this.shiptoIDValue;
	}

	public void setShiptoIDValue(int shiptoIDValue)
	{
		this.shiptoIDValue = shiptoIDValue;
	}





	public String getShiptoID()
	{
		return this.shiptoID;
	}

	public void setShiptoID(String shiptoID)
	{
		this.shiptoID = shiptoID;
	}


	public String getCustomerId()
	{
		return this.customerId;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}


	public int getCustomerIdValue()
	{
		return this.customerIdValue;
	}

	public void setCustomerIdValue(int customerIdValue)
	{
		this.customerIdValue = customerIdValue;
	}


	public int getTermidValue()
	{
		return this.termidValue;
	}

	public void setTermidValue(int termidValue)
	{
		this.termidValue = termidValue;
	}


	public java.sql.Date getInvoiceDate()
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

		invoiceDate = new java.sql.Date(year,month-1,date);


		return this.invoiceDate;
	}

	public void setInvoiceDate(java.sql.Date invoiceDate)
	{
		this.invoiceDate = invoiceDate;
	}


	public ItemLines getItemLines()
	{
		return this.itemLines;
	}

	public void setItemLines(ItemLines itemLines)
	{
		this.itemLines = itemLines;
	}



	public String[] getSku()
	{
		return this.sku;
	}

	public void setSku(String[] sku)
	{
		this.sku = sku;
	}


	public String[] getPriceExtended()
	{
		return this.priceExtended;
	}

	public void setPriceExtended(String[] priceExtended)
	{
		this.priceExtended = priceExtended;
	}


	public String[] getSubTotal()
	{
		return this.subTotal;
	}

	public void setSubTotal(String[] subTotal)
	{
		this.subTotal = subTotal;
	}
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();
		convertItemLines();

		//Incase if the Form is having some error then we must have to carry the jurisdiction Vec
		try{
			AccountHelperHome accountHelperHome = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome","AccountHelper");
			AccountHelper accHelper =(AccountHelper)accountHelperHome.create();
			accHelper.setDataSource(dataSource);
			this.jurisdictionVec = accHelper.getTaxJurisdiction();
		}catch(Exception e){
			this.jurisdictionVec = new Vector();
		}

		try
		{
			// initialize validation
			Validation validation = new Validation();

			// invoice
      if (this.getOrderid() == null || this.getOrderid().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Order"));
      }
      
      if (this.getDate() == null || this.getDate().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Date"));
      }
      
			boolean itemPresent =false;
			int counter = 0;

			if (this.itemid != null)
			{
				ItemLines lines = this.getItemLines();

				for (int i=0;i<this.linestatus.length;i++)
				{
					if (this.linestatus[i] != null &&
							this.linestatus[i].equalsIgnoreCase("Deleted"))
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
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requierdField", "Items"));
			}



			if (errors != null)
			{
				String body = request.getParameter("body");
				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
				request.setAttribute("body",body);

				request.setAttribute("clearform", "false");
					//this.convertItemLines();
				request.setAttribute("invoiceform", this);
				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);

			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return errors;
	}

	public static InvoiceForm clearForm(InvoiceForm invoiceForm)
	{
		invoiceForm.setAccountmanagerid("");
		invoiceForm.setAccountmanagerName("");
		invoiceForm.setBillto("");
		invoiceForm.setBilltoID("");
		invoiceForm.setCustomerId("");
		invoiceForm.setDate("");
		invoiceForm.setExternalid("");
		invoiceForm.setInvoicedate("");
		invoiceForm.setInvoiceid("");
		invoiceForm.setMonth("");
		invoiceForm.setNotes("");
		invoiceForm.setOrderid("");
		invoiceForm.setOrdername("");
		invoiceForm.setOrderno("");
		invoiceForm.setPoid("");
		invoiceForm.setPoname("");
		invoiceForm.setProjectid("");
		invoiceForm.setProjectName("");
		invoiceForm.setShipto("");
		invoiceForm.setShiptoID("");
		invoiceForm.setStatusid("");
		invoiceForm.setTermid("");
		invoiceForm.setYear("");
		invoiceForm.setJurisdictionID(new Integer(0));
		invoiceForm.itemLines=null;
		invoiceForm.description = null;
		invoiceForm.item = null;
		invoiceForm.itemid = null;
		invoiceForm.lineid = null;
		invoiceForm.linestatus = null;
		invoiceForm.orderquantity = null;
		invoiceForm.pendingquantity = null;
		invoiceForm.priceeach = null;
		invoiceForm.priceExtended = null;
		invoiceForm.quantity = null;
		invoiceForm.sku = null;
		invoiceForm.taxrate = null;
		invoiceForm.totalprice = null;
		invoiceForm.unitprice = null;
		invoiceForm.unittax = null;
		invoiceForm.taxAmount = null;

		return invoiceForm;
	}

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
				if(taxAmount[i] != null){
					taxAmount[i] = taxAmount[i].replaceAll(",","");
				}
				else{
					taxAmount[i] = "0";
				}
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

			}// end of for
		}
	}


	public String[] getLineid()
	{
		return this.lineid;
	}

	public void setLineid(String[] lineid)
	{
		this.lineid = lineid;
	}


	public String[] getTaxrate()
	{
		return this.taxrate;
	}

	public void setTaxrate(String[] taxrate)
	{
		this.taxrate = taxrate;
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


	public String getMonth()
	{
		return this.month;
	}

	public void setMonth(String month)
	{
		this.month = month;
	}


	public String getDate()
	{
		return this.date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}


	public String getYear()
	{
		return this.year;
	}

	public void setYear(String year)
	{
		this.year = year;
	}


	public String getPoname()
	{
		return this.poname;
	}

	public void setPoname(String poname)
	{
		this.poname = poname;
	}


	public String getBilltoID()
	{
		return this.billtoID;
	}

	public void setBilltoID(String billtoID)
	{
		this.billtoID = billtoID;
	}

	public String[] getLinestatus()
	{
		return this.linestatus;
	}

	public void setLinestatus(String[] linestatus)
	{
		this.linestatus = linestatus;
	}

	/**
	 * Sets the modifiedBy for this invoice.
	 * @param modifiedBy The new modifiedBy for this invoice.
	 */
	public void setModifiedBy(int modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	} //end of setModifiedBy method

	/**
	 * @return The modifiedBy for this invoice.
	 */
	public int getModifiedBy()
	{
		return this.modifiedBy;
	} //end of getModifiedBy method

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
}














