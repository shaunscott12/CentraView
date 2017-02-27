/*
 * $RCSfile: ProposalVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:07 $ - $Author: mking_cv $
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

import com.centraview.file.CvFileVO;

/**
 *  The Proposal Value Object. This file is used for storing user input data
 *  during adding or editing of proposal.
 * 
 * @author Sandip Wadkar
 */
public class ProposalVO implements java.io.Serializable
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
	private String attachment;
	private String select2;
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
  
  /** Vector of files associated to this proposal. */
  private Vector attachmentVec;
  /** Vector of file IDs (Integer) associated to this proposal. */
  private Vector attachmentId;

	private ItemLines itemLines ;//=  new ItemLines(); // this new ItemLines will be removed;

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

	public String getAttachment(){
		return this.attachment;
	}
	public void setAttachment(String attachment){
		this.attachment = attachment;
	}

	public String getSelect2(){
		return this.select2;
	}
	public void setSelect2(String select2){
		this.select2 = select2;
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
		return this;
	}



	/**
	 * returns true if Order is generated for this Proposal
	 * only 1 order can be generated per proposal
	 * @return boolean true/false
	 */
	public boolean getOrderIsGenerated()
	{
		return this.orderIsGenerated;
	}

	/**
	 * sets whether order is generated for this proposal or not
	 * only 1 order can be generated per proposal
	 * @param boolean orderIsGenerated
	 */
	public void setOrderIsGenerated(boolean orderIsGenerated)
	{
		this.orderIsGenerated = orderIsGenerated;
	}
  
  /**
   * Returns the Collection of attachmentIDs for this Proposal.
   * 
   * @return The Collection of attachmentIDs for this Proposal.
   */
  public Vector getAttachmentId()
  {
    return this.attachmentId;
  } //end of setAttachmentId method
  
  /**
   * Adds an ID of a file attachment to the list of attachmentIDs for this Proposal.
   * 
   * @param attachment The ID of a file attachment to the 
   *  list of attachmentIDs for this Proposal.
   */
  public void setAttachmentId(Integer attachmentId)
  {
    if (this.attachmentId == null)
    {
      this.attachmentId = new Vector();
    } //end of if statement (this.attachmentId == null)
    this.attachmentId.add(attachmentId);
  } //end of setAttachmentId method
  
  
  
  /**
   * Returns the Collection of attachments for this Proposal.
   * 
   * @return The Collection of attachments for this Proposal.
   */
  public Vector getAttachmentVec()
  {
    return this.attachmentVec;
  } //end of getAttachmentVec method
  
  /**
   * Adds a FileVO to the list of attachments for this Proposal.
   * 
   * @param attachment The FileVO to be added the list 
   *  of attachments for this Proposal.
   */
  public void setAttachmentVec(CvFileVO attachment)
  {
    if (this.attachmentVec == null)
    {
      this.attachmentVec = new Vector();
    } //end of if statement (this.attachmentVec == null)
    this.attachmentVec.add(attachment);
  } //end of setAttachmentVec method

}
