/*
 * $RCSfile: ProposalListForm.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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


import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.Validation;
/**
 * This file is used for storing user input data
 * during adding or editing of proposallist.
 *
 * @author Sandip Wadkar
 */
public class ProposalListForm extends ActionForm
{

	private String prodescription;
	private String opportunity; // = "1";
	private String opportunityid="0";
	private String proposal;
	private String individual;
	private String individualid="0";
	private String billingaddress;
	private String shippingaddress;
	private String billingaddressid="0";
	private String shippingaddressid="0";
	private String statuslist="0";
	private String stage="0";
	private String proposaltype="0";
	private String probability="0";
	private String estimatedclose;
	private String actualclose;
	private String terms="0";
	private String specialinstructions;
	private String[] attachFileIds;
	private Vector attachFileValues;
	private String proposalid="0";
	private String ecmon;
	private String ecday;
	private String ecyear;
	private String acmon;
	private String acday;
	private String acyear;
	private String createdDate;
	private String modifyDate;
	private String createdBy;
	private String modifyBy;
	private String typeid="0";
	private String forecastinc;
	private boolean orderIsGenerated;

  /** Holds the Forcast Amount for the Proposal. */
  private String forcastAmount;

	private ProposalVO proposalVO;

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


	private int statusIDValue;
	/*
	*	Stores statusid
	*/
	private String statusid;

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

	private String[] linestatus;

	private ItemLines itemLines ;//=  new ItemLines(); // this new ItemLines will be removed;


	/*
	*	Stores taxAmount related to Proposal
	*/
	private String[] taxAmount;

	/*
	*	Stores jurisdictionID
	*/
	private int jurisdictionID;

	/*
	*	Stores jurisdictionVec
	*/
	private Vector jurisdictionVec;

	/*
	*	Stores Entity Name
	*/
	private String entity;

	/*
	*	Stores Entity Identification
	*/
	private int entityID;

	/*
	*	Stores Order Identification
	*/
	private int orderID;

	/*
	 *	Return proposalid string
	 *	@return proposalid String
	 */
	public String getProposalid()
	{
		return this.proposalid;
	}

	/*
	 *	Set proposalid string
	 *	@param proposalid String
	 */
	public void setProposalid(String proposalid)
	{
		this.proposalid = proposalid;
	}


  public String getProdescription(){
		return this.prodescription;
	}
	public void setProdescription(String prodescription){
		this.prodescription = prodescription;
	}

	public String getOpportunity(){
		return this.opportunity;
	}
	public void setOpportunity(String opportunity){
		this.opportunity = opportunity;
	}

	public String getOpportunityid(){
		return this.opportunityid;
	}
	public void setOpportunityid(String opportunityid){
		this.opportunityid = opportunityid;
	}

	public String getProposal(){
		return this.proposal;
	}
	public void setProposal(String proposal){
		this.proposal = proposal;
	}

	public String getIndividual(){
		return this.individual;
	}
	public void setIndividual(String individual){
		this.individual = individual;
	}

	public String getIndividualid(){
		return this.individualid;
	}
	public void setIndividualid(String individualid){
		this.individualid = individualid;
	}

	public String getBillingaddress(){
		return this.billingaddress;
	}
	public void setBillingaddress(String billingaddress){
		this.billingaddress = billingaddress;
	}

	public String getShippingaddress(){
		return this.shippingaddress;
	}
	public void setShippingaddress(String shippingaddress){
		this.shippingaddress = shippingaddress;
	}

	public String getBillingaddressid(){
		return this.billingaddressid;
	}
	public void setBillingaddressid(String billingaddressid){
		this.billingaddressid = billingaddressid;
	}

	public String getShippingaddressid(){
		return this.shippingaddressid;
	}
	public void setShippingaddressid(String shippingaddressid){
		this.shippingaddressid = shippingaddressid;
	}

	public String getStatuslist(){
		return this.statuslist;
	}
	public void setStatuslist(String statuslist){
		this.statuslist = statuslist;
	}

	public String getStage(){
		return this.stage;
	}
	public void setStage(String stage){
		this.stage = stage;
	}

	public String getProposaltype(){
		return this.proposaltype;
	}
	public void setProposaltype(String proposaltype){
		this.proposaltype = proposaltype;
	}

	public String getProbability(){
		return this.probability;
	}
	public void setProbability(String probability){
		this.probability = probability;
	}

	public String getEstimatedclose(){
		return this.estimatedclose;
	}
	public void setEstimatedclose(String estimatedclose){
		this.estimatedclose = estimatedclose;
	}

	public String getActualclose(){
		return this.actualclose;
	}
	public void setActualclose(String actualclose){
		this.actualclose = actualclose;
	}

	public String getTerms(){
		return this.terms;
	}
	public void setTerms(String terms){
		this.terms = terms;
	}

	public String getSpecialinstructions(){
		return this.specialinstructions;
	}
	public void setSpecialinstructions(String specialinstructions){
		this.specialinstructions = specialinstructions;
	}

	public String[] getAttachFileIds(){
		return this.attachFileIds;
	}
	public void setAttachFileIds(String[] attachFileIds){
		this.attachFileIds = attachFileIds;
	}

	public Vector getAttachFileValues()
	{
		return this.attachFileValues;
	}

	public void setAttachFileValues(Vector attachFileValues)
	{
		this.attachFileValues = attachFileValues;
	}

	public String getEcmon(){
		return this.ecmon;
	}
	public void setEcmon(String ecmon){
		this.ecmon = ecmon;
	}

	public String getEcday(){
		return this.ecday;
	}
	public void setEcday(String ecday){
		this.ecday = ecday;
	}

	public String getEcyear(){
		return this.ecyear;
	}
	public void setEcyear(String ecyear){
		this.ecyear = ecyear;
	}

	public String getAcmon(){
		return this.acmon;
	}
	public void setAcmon(String acmon){
		this.acmon = acmon;
	}

	public String getAcday(){
		return this.acday;
	}
	public void setAcday(String acday){
		this.acday = acday;
	}

	public String getAcyear(){
		return this.acyear;
	}
	public void setAcyear(String acyear){
		this.acyear = acyear;
	}

	public String getCreatedDate(){
		return this.createdDate;
	}
	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedBy(){
		return this.createdBy;
	}
	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getModifyDate(){
		return this.modifyDate;
	}
	public void setModifyDate(String modifyDate){
		this.modifyDate = modifyDate;
	}

	public String getModifyBy(){
		return this.modifyBy;
	}
	public void setModifyBy(String modifyBy){
		this.modifyBy = modifyBy;
	}

	public String getTypeid(){
		return this.typeid;
	}
	public void setTypeid(String typeid){
		this.typeid = typeid;
	}


	public String getForecastinc(){
		return this.forecastinc;
	}
	public void setForecastinc(String forecastinc){
		this.forecastinc = forecastinc;
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

	/**
   * This method converts the LineItems of Proposal
   * into the proper List Items.
   */
  public void	convertItemLines()
  {
    int count;
    itemLines = new ItemLines();

    if (itemid != null)
    {
      for (int i =0; i < itemid.length; i++)
      {


				ItemElement ie = new ItemElement();
				IntMember LineId = new IntMember("LineId",Integer.parseInt(lineid[i]),'D',"",'T',false,20);
				IntMember ItemId = new IntMember("ItemId",Integer.parseInt(itemid[i]),'D',"",'T',false,20);
				quantity[i] = quantity[i].replaceAll(",","");
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
      }// end of for loop (int i =0; i < itemid.length; i++)
      itemLines.calculate();
    } //end of if statement (itemid != null)
  } //end of convertItemLines method


	public static ProposalListForm clearForm(ProposalListForm proposallistform)
	{
		proposallistform.itemLines=null;
		proposallistform.description = null;
		proposallistform.item = null;
		proposallistform.itemid = null;
		proposallistform.lineid = null;
		proposallistform.linestatus = null;
		proposallistform.priceeach = null;
		proposallistform.priceExtended = null;
		proposallistform.quantity = null;
		proposallistform.sku = null;
		proposallistform.taxrate = null;
		proposallistform.totalprice = null;
		proposallistform.unitprice = null;
		proposallistform.unittax = null;
		return proposallistform;
	}


	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		try {
			Validation validation = new Validation();

			// title
      if (this.getOpportunity() == null || this.getOpportunity().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Opportunity"));
      }
      
      if (this.getProposal() == null || this.getProposal().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Proposal"));
      }

			if (errors != null) {
				request.setAttribute("proposallistform", this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errors;
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



	public String[] getLinestatus()
	{
		return this.linestatus;
	}

	public void setLinestatus(String[] linestatus)
	{
		this.linestatus = linestatus;
	}

	public ProposalVO getVO()
	{
		ProposalVO proposalVO = new ProposalVO();
		proposalVO.setShippingaddressid(this.shippingaddressid);
		proposalVO.setItemLines(this.itemLines);
		return proposalVO;
	}

	public void setVO(ProposalVO proposalVO)
	{
		this.proposalVO = proposalVO;
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

  /**
   * Returns the forcastAmount in the Form.
   *
   * @return The Forcast Amount for this proposal.
   */
  public String getForcastAmount()
  {
    return this.forcastAmount;
  } //end of getForcastAmount method

  /**
   * Sets the forcastAmount in the Form.
   *
   * @param forcastAmount The new Forcast Amount for this proposal.
   */
  public void setForcastAmount(String forcastAmount)
  {
    this.forcastAmount = forcastAmount;
  } //end of setForcastAmount method


	public boolean getOrderIsGenerated()
	{
		return this.orderIsGenerated;
	}

	public void setOrderIsGenerated(boolean orderIsGenerated)
	{
		this.orderIsGenerated = orderIsGenerated;
	}

  public String toString()
  {
    StringBuffer sb = new StringBuffer("ProposalListForm = \n");
    sb.append("    prodescription = [" + prodescription + "]\n");
    sb.append("    opportunity = [" + opportunity + "]\n");
    sb.append("    opportunityid = [" + opportunityid + "]\n");
    sb.append("    proposal = [" + proposal + "]\n");
    sb.append("    individual = [" + individual + "]\n");
    sb.append("    individualid = [" + individualid + "]\n");
    sb.append("    billingaddress = [" + billingaddress + "]\n");
    sb.append("    shippingaddress = [" + shippingaddress + "]\n");
    sb.append("    billingaddressid = [" + billingaddressid + "]\n");
    sb.append("    shippingaddressid = [" + shippingaddressid + "]\n");
    sb.append("    statuslist = [" + statuslist + "]\n");
    sb.append("    stage = [" + stage + "]\n");
    sb.append("    proposaltype = [" + proposaltype + "]\n");
    sb.append("    probability = [" + probability + "]\n");
    sb.append("    estimatedclose = [" + estimatedclose + "]\n");
    sb.append("    actualclose = [" + actualclose + "]\n");
    sb.append("    terms = [" + terms + "]\n");
    sb.append("    specialinstructions = [" + specialinstructions + "]\n");
    sb.append("    attachFileIds = [" + attachFileIds + "]\n");
    sb.append("    attachFileValues = [" + attachFileValues + "]\n");
    sb.append("    proposalid = [" + proposalid + "]\n");
    sb.append("    ecmon = [" + ecmon + "]\n");
    sb.append("    ecday = [" + ecday + "]\n");
    sb.append("    ecyear = [" + ecyear + "]\n");
    sb.append("    acmon = [" + acmon + "]\n");
    sb.append("    acday = [" + acday + "]\n");
    sb.append("    acyear = [" + acyear + "]\n");
    sb.append("    createdDate = [" + createdDate + "]\n");
    sb.append("    modifyDate = [" + modifyDate + "]\n");
    sb.append("    createdBy = [" + createdBy + "]\n");
    sb.append("    modifyBy = [" + modifyBy + "]\n");
    sb.append("    typeid = [" + typeid + "]\n");
    sb.append("    forecastinc = [" + forecastinc + "]\n");
    sb.append("    orderIsGenerated = [" + orderIsGenerated + "]\n");
    return(sb.toString());
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

  /**
   * @return The jurisdiction ID.
   */
  public int getJurisdictionID()
  {
    return this.jurisdictionID;
  }

  /**
   * Set the Jurisdiction ID
   *
   * @param jurisdictionID
   */
  public void setJurisdictionID(int jurisdictionID)
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

  /**
   * @return The Entity
   */
  public String getEntity()
  {
    return this.entity;
  }

  /**
   * Set the Entity
   *
   * @param entity
   */
  public void setEntity(String entity)
  {
    this.entity = entity;
  }

  /**
   * @return The Entity ID.
   */
  public int getEntityID()
  {
    return this.entityID;
  }

  /**
   * Set the Entity ID
   *
   * @param entityID
   */
  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  }

  /**
   * @return The Order ID.
   */
  public int getOrderID()
  {
    return this.orderID;
  }

  /**
   * Set the Order ID
   *
   * @param orderID
   */
  public void setOrderID(int orderID)
  {
    this.orderID = orderID;
  }

} //end of ProposalListForm class
