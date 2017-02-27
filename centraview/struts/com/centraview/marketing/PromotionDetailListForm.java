/*
 * $RCSfile: PromotionDetailListForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:36:00 $ - $Author: mcallist $
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

package com.centraview.marketing;

// java import package
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.Validation;

public class PromotionDetailListForm extends org.apache.struts.action.ActionForm
{

	private String promotionid = "";
	private String pname = "";
	private String pdescription = "";
	private String startmonth = "";
	private String startday = "";
	private String startyear = "";
	private String endmonth = "";
	private String endday = "";
	private String endyear = "";
	private String pstatus = "";
	private String field1 = "";
	private String field2 = "";
	private String field3 = "";
	private String startdate = "";
	private String enddate = "";

	private String[] type = null;
	private String[] value = null;
	private String[] discountedprice={"0.00"};
	private String[] listprice={"0.00"};
	private String[] cost={"0.00"};


     /*
	 *	Stores notes
	 */
	private String notes = "";
	/*
	 *	Stores priceeach to item
	 */
	private String[] priceeach  = null;

	/*
	 *	Stores priceextended to item
	 */
	private String[] priceExtended = null;
	 /*
	 *	Stores sku related to item
	 */
	private String[] sku = null;


	/*
	 *	Stores itemid related to item
	 */
	private String[] itemid = null;

	/*
	 *	Stores item related to item
	 */
	private String[] item = null;

	/*
	 *	Stores description related to item
	 */
	private String[] description = null;

	/*
	 *	Stores quqntity related to item
	 */
	private String[] quantity = null;

	/*
	 *	Stores unit price  related to item
	 */
	private String[] unitprice = null;

	/*
	 *	Stores total price related to item
	 */
	private String[] totalprice = null;

	 /*
	  *	Stores unit tax related to item
	  */
	private String[] unittax = null;
	 /*
	  *	Stores taxrate related to item
	  */
	private String[] taxrate = null;
	  /*
	  *	Stores orderquantity related to item
	  */
	private String[] orderquantity = null;
	  /*
	  *	Stores pendingquantity related to item
	  */
	private String[] pendingquantity = null;

	  /*
	  *	Stores lineid related to item
	  */
	private String[] lineid = null;

	private String[] linestatus = null;


	private java.sql.Date invoiceDate;
	private ItemLines itemLines =  new ItemLines(); // this new ItemLines will be removed;

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

		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();

		try
		{
			// initialize validation
			Validation validation = new Validation();

			boolean itemPresent =false;
			if (this.itemid != null)
			{
				if (this.itemid.length>0)
				{
					itemPresent = true;
					//System.out.println("Item Present");
				}
				else
				{
					//System.out.println("Item is Not Present");
				}
			}

			if (!itemPresent)
			{
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Items"));
			}

			// title
			validation.checkForRequired("error.promotion.name", this.getPname(), "error.application.required", errors);
			validation.checkForMaxlength("error.promotion.name", this.getPname(), "error.application.maxlength", errors, 25);
			// detail
			validation.checkForRequired("label.promotion.desc", this.getPdescription(), "error.application.required", errors);
			validation.checkForMaxlength("error.promotion.desc", this.getPdescription(), "error.application.maxlength", errors, 100);

			validation.checkForRequired("error.promotion.startday", this.getStartday(), "error.application.required", errors);
			validation.checkForRequired("error.promotion.startyear", this.getStartyear(), "error.application.required", errors);
			validation.checkForRequired("error.promotion.startmonth", this.getStartmonth(), "error.application.required", errors);
			validation.checkForRequired("error.promotion.endyear", this.getEndyear(), "error.application.required", errors);
			validation.checkForRequired("error.promotion.endmonth", this.getEndmonth(), "error.application.required", errors);
			validation.checkForRequired("error.promotion.endday", this.getEndday(), "error.application.required", errors);

			if (
				(this.getStartyear() != null && this.getStartyear().length() != 0) ||
				(this.getStartmonth() != null && this.getStartmonth().length() != 0) ||
				(this.getStartday() != null && this.getStartday().length() != 0)
			)
			{
				// due date
				validation.checkForDate("error.promotion.startdate", this.getStartyear(), this.getStartmonth(), this.getStartday(), "error.application.date", errors);

				if (
					(this.getEndyear() != null && this.getEndyear().length() != 0) ||
					(this.getEndmonth() != null && this.getEndmonth().length() != 0) ||
					(this.getEndday() != null && this.getEndday().length() != 0)
				)
				{
					validation.checkForDate("error.promotion.enddate", this.getEndyear(), this.getEndmonth(), this.getEndday(), "error.application.date", errors);

					// comparison
					validation.checkForDateComparison(
						"error.promotion.enddate", this.getStartyear(), this.getStartmonth(), this.getStartday(), "00:00 AM",
						"error.promotion.startdate", this.getEndyear(), this.getEndmonth(), this.getEndday(), "00:00 AM",
						"error.promotion.datecomparison", errors, "error.promotion.enddate",
						"error.promotion.startdate");
				}
			}


			if (errors != null)
			{
				String body = request.getParameter("body");
				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
				request.setAttribute("body",body);


				//request.setAttribute("body",AccountConstantKeys.ADD );
				//request.setAttribute("body",AccountConstantKeys.EDIT);
				request.setAttribute("promotionlistform", this);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return errors;
	}

/*	public static InvoiceForm clearForm(InvoiceForm invoiceForm)
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
		invoiceForm.itemLines=null;
		return invoiceForm;
	}
*/
	public void	convertItemLines()
	{
	//	System.out.println(" call convertItemLines  "+itemid);
	  int count;
	  itemLines= new ItemLines();
	  String strDiscoountprice = "0";
	  //System.out.println("################ "+itemid+" ####################");


		if (itemid != null)
		{
			//System.out.println("################length : "+itemid.length+" ####################");
		  String type[] = new String[itemid.length];
		  for (int i=0;i<itemid.length;i++)
		  {
			ItemElement ie = new ItemElement();

			//System.out.println("lineid[i] :: "+lineid[i]);
			/*int lineid = 0;
			String id = lineid[i].toString();
			if (id != null)
			{
				if (!id.equals(""))
				{
					lineid = Integer.parseInt(id);
				}
			}*/

			//System.out.println("################ "+itemid[i]+" ####################");
			//System.out.println("################ "+discountedprice+" ####################");
			//System.out.println("################ iiii :: "+i+":: ####################");
			try
			{
			if (discountedprice != null)
			{
				//System.out.println("discountedprice[i] :: " +discountedprice.length);
				//System.out.println("discountedprice[i] :: " +discountedprice[i]);
				if (discountedprice[i] == null)
					strDiscoountprice = "0";
				//System.out.println("strDiscoountprice :: " +strDiscoountprice );
			}
			}catch(Exception exe)
			{
				strDiscoountprice = "0";
				//System.out.println("strDiscoountprice  :: " +strDiscoountprice );
			}

/*			if ((discountedprice == null) || (discountedprice.equals(""))  || (discountedprice.equals("null")))
			{
				discountedprice = new String[1];
				discountedprice[i] = "0.0";
			}
			else if (discountedprice[i] == null)
				discountedprice[i] = "0.0";
			System.out.println("################ "+discountedprice[i]+" ####################");
/*
			System.out.println("################ "+listprice+" ####################");
			if ((listprice == null) || (listprice.equals(""))  || (listprice.equals("")))
			{
				listprice = new String[1];
				listprice[i] = "0.0";
			}
			System.out.println("################ "+listprice[i]+" ####################");
			System.out.println("################ "+cost+" ####################");
			if ((cost == null) || (cost.equals(""))  || (cost.equals("")))
			{
				cost = new String[1];
				cost[i] = "0.0";
			}
			System.out.println("################ "+cost[i]+" ####################");
*/
			IntMember 		LineId = null;

			// Defaulting to costDollarLayer Coz We are not going to use it right now..
			// Remove this line and remove the commented block in /jsp/marketing/ItemList.jsp
			// String type[] = new String[itemid.length];
			type[i] = "costDollarLayer";


			if (!lineid[i].equals(""))
			LineId = new IntMember("LineId",Integer.parseInt(lineid[i]),10,"",'T',false,20);
			IntMember 		ItemId = new IntMember("ItemId",Integer.parseInt(itemid[i]),10,"",'T',false,20);
			StringMember 	SKU = new StringMember("SKU",sku[i],10,"",'T',false);
			StringMember 	Description = new StringMember("Description",description[i],10,"",'T',false);
			/*FloatMember 	Quantity = new FloatMember("Quantity",new Float(quantity[i]),10,"0",'T',false,20);
			FloatMember 	PriceEach = new FloatMember("PriceEach",new Float(priceeach[i]),10,"",'T',false,20);
			FloatMember 	PriceExtended = new FloatMember("PriceExtended",new Float(priceExtended[i]),10,"",'T',false,20);*/
			FloatMember 	UnitTax = new FloatMember("UnitTax",new Float(unittax[i]),10,"",'T',false,20);
			FloatMember 	TaxRate = new FloatMember("UnitTaxrate",new Float(taxrate[i]),10,"",'T',false,20);
			FloatMember 	OrderQuantity = new FloatMember("OrderQuantity",new Float(orderquantity[i]),10,"",'T',false,20);
			FloatMember 	PendingQuantity = new FloatMember("PendingQuantity",new Float(pendingquantity[i]),10,"",'T',false,20);
			IntMember 		Value = new IntMember("Value",Integer.parseInt(value[i]),10,"",'T',false,20);
			StringMember 	Type = new StringMember("Type",type[i],10,"",'T',false);
			FloatMember 	DiscountedPrice = new FloatMember("DiscountedPrice",new Float(strDiscoountprice),10,"",'T',false,20);
			/*DoubleMember 	ListPrice = new FloatMember("ListPrice",new Float(listprice[i]),10,"",'T',false,20);
			FloatMember 	Cost = new FloatMember("Cost",new Float(cost[i]),10,"",'T',false,20);
			*/

			FloatMember  price = new FloatMember( "Price"  ,new Float(listprice[i]) , 10 , "", 'T' , false , 10 );
			FloatMember  dcost = new FloatMember( "Cost"  ,new Float(cost[i]) , 10 , "", 'T' , false , 10 );
			//DoubleMember cost	= new DoubleMember( "Cost"  ,new Double(cost[i]) , 10 , "", 'T' , false , 10 );

			ie.put ("LineId",LineId);
			ie.put ("ItemId",ItemId);
			ie.put ("SKU",SKU);
			ie.put ("Description",Description);
			/*ie.put ("Quantity",Quantity);
			ie.put ("PriceEach",PriceEach);
			ie.put ("PriceExtended",PriceExtended);*/
			ie.put ("UnitTax",UnitTax);
			ie.put ("UnitTaxrate",TaxRate);
			ie.put ("OrderQuantity",OrderQuantity);
			ie.put ("PendingQuantity",PendingQuantity);
			ie.put ("Type",Type);
			ie.put ("Value",Value);
			ie.put ("DiscountedPrice",DiscountedPrice);
			ie.put ("ListPrice",price);
			ie.put ("Cost",dcost);

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



	public String[] getLinestatus()
	{
		return this.linestatus;
	}

	public void setLinestatus(String[] linestatus)
	{
		this.linestatus = linestatus;
	}




	public String getEnddate()
	{
		return this.enddate;
	}

	public void setEnddate(String enddate)
	{
		this.enddate = enddate;
	}


	public String getEndday()
	{
		return this.endday;
	}

	public void setEndday(String endday)
	{
		this.endday = endday;
	}


	public String getEndmonth()
	{
		return this.endmonth;
	}

	public void setEndmonth(String endmonth)
	{
		this.endmonth = endmonth;
	}


	/*public String getEndtime()
	{
		return this.endtime;
	}

	public void setEndtime(String endtime)
	{
		this.endtime = endtime;
	}*/


	public String getEndyear()
	{
		return this.endyear;
	}

	public void setEndyear(String endyear)
	{
		this.endyear = endyear;
	}


	public String getField1()
	{
		return this.field1;
	}

	public void setField1(String field1)
	{
		this.field1 = field1;
	}


	public String getField2()
	{
		return this.field2;
	}

	public void setField2(String field2)
	{
		this.field2 = field2;
	}


	public String getField3()
	{
		return this.field3;
	}

	public void setField3(String field3)
	{
		this.field3 = field3;
	}


	public String getPname()
	{
		return this.pname;
	}

	public void setPname(String pname)
	{
		this.pname = pname;
	}


	public String getPdescription()
	{
		return this.pdescription;
	}

	public void setPdescription(String pdescription)
	{
		this.pdescription = pdescription;
	}


	public String getPstatus()
	{
		return this.pstatus;
	}

	public void setPstatus(String pstatus)
	{
		this.pstatus = pstatus;
	}


	/*public String getSelendtime()
	{
		return this.selendtime;
	}

	public void setSelendtime(String selendtime)
	{
		this.selendtime = selendtime;
	}


	public String getSelstarttime()
	{
		return this.selstarttime;
	}

	public void setSelstarttime(String selstarttime)
	{
		this.selstarttime = selstarttime;
	}*/


	public String getStartdate()
	{
		return this.startdate;
	}

	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}


	public String getStartday()
	{
		return this.startday;
	}

	public void setStartday(String startday)
	{
		this.startday = startday;
	}


	public String getStartmonth()
	{
		return this.startmonth;
	}

	public void setStartmonth(String startmonth)
	{
		this.startmonth = startmonth;
	}


	/*public String getStarttime()
	{
		return this.starttime;
	}

	public void setStarttime(String starttime)
	{
		this.starttime = starttime;
	}*/


	public String getStartyear()
	{
		return this.startyear;
	}

	public void setStartyear(String startyear)
	{
		this.startyear = startyear;
	}


	public String[] getDiscountedprice()
	{
		return this.discountedprice;
	}

	public void setDiscountedprice(String[] discountedprice)
	{
		this.discountedprice = discountedprice;
	}


	public String[] getValue()
	{
		return this.value;
	}

	public void setValue(String[] value)
	{
		this.value = value;
	}


	public String[] getType()
	{
		return this.type;
	}

	public void setType(String[] type)
	{
		this.type = type;
	}


	public String getPromotionid()
	{
		return this.promotionid;
	}

	public void setPromotionid(String promotionid)
	{
		this.promotionid = promotionid;
	}


	public String[] getListprice()
	{
		return this.listprice;
	}

	public void setListprice(String[] listprice)
	{
		this.listprice = listprice;
	}


	public String[] getCost()
	{
		return this.cost;
	}

	public void setCost(String[] cost)
	{
		this.cost = cost;
	}
}














