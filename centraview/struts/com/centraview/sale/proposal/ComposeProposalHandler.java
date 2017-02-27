/*
 * $RCSfile: ComposeProposalHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:43 $ - $Author: mking_cv $
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

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.printtemplate.PrintTemplate;
import com.centraview.printtemplate.PrintTemplateHome;
import com.centraview.printtemplate.PrintTemplateVO;
import com.centraview.sale.OpportunityForm;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;


public class ComposeProposalHandler extends Action
{


	public static final String GLOBAL_FORWARD_failure = "failure";
	private static final String FORWARD_viewproposal = "composeproposal";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
	private DecimalFormat currencyFormat = new DecimalFormat("###,###,##0.00");

	/**
   * Executes initialization of required parameters and
   * open window for entry of proposal returns ActionForward
   */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
			HttpSession session = request.getSession();
			UserObject userObject = (UserObject) session.getAttribute("userobject");
			int individualID = userObject.getIndividualID();

      DynaActionForm composeForm = (DynaActionForm)form;

			ProposalListForm proposallistform = new ProposalListForm();
			proposallistform.convertItemLines();
			int row = 0;
			try
			{
				row = Integer.parseInt(request.getParameter("eventid"));
			}catch (Exception ex){
				row = Integer.parseInt(proposallistform.getProposalid());
			}

			SaleFacadeHome sfh = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome","SaleFacade");
			SaleFacade remote =(SaleFacade) sfh.create();
			remote.setDataSource(dataSource);

			HashMap hm  = (HashMap) remote.viewProposal(individualID, row , proposallistform);

			String Items = "";


			String subTotal = "0.00";
			String sTax = "0.00";
			String total = "0.00";

			ItemLines itemLines = (ItemLines) hm.get("itemLines");

			if (itemLines != null)
			{
				itemLines.calculate();
				DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
				DecimalFormat rawCurrencyFormat = new DecimalFormat("#0.00");
				DecimalFormat wholeNumberFormat = new DecimalFormat("#0");

				java.util.Set listkey = itemLines.keySet();
				java.util.Iterator it =  listkey.iterator();

				int i=1;
				int itemNum = 0;

				if(!it.hasNext()){
					Items = Items + "<tr>\n";
					Items = Items + "<td colspan=\"5\" align=\"center\">No items found</td>\n";
					Items = Items + "</tr>\n";
				}
				while (it.hasNext())
				{
					ItemElement ele  = (ItemElement)itemLines.get(it.next());
					String lineStatus = ele.getLineStatus();
					if (lineStatus == null)
					{
						lineStatus = "";
					} //end of if statement (lineStatus == null)

               if (!lineStatus.equals("Deleted")) {

					IntMember lineid = (IntMember)ele.get("LineId");
					IntMember itemid = (IntMember)ele.get("ItemId");
					StringMember sku = (StringMember)ele.get("SKU");
					IntMember qty	 = (IntMember)ele.get("Quantity");
					FloatMember priceEach = (FloatMember)ele.get("Price");
					FloatMember priceExe = (FloatMember)ele.get("PriceExtended");

					StringMember desc  = (StringMember)ele.get("Description");
					FloatMember taxAmount = (FloatMember)ele.get("TaxAmount");

					String quantity;
					String priceEachString;
					try
					{
						float tempFloat = (currencyFormat.parse(qty.getDisplayString())).floatValue();
						quantity = wholeNumberFormat.format(new Float(tempFloat));
					} //end of try block
					catch (Exception exception)
					{
						quantity = "0";
					} //end of catch block (Exception)

					try
					{
						float priceEachValue = ((Float) priceEach.getMemberValue()).floatValue();
						priceEachString = rawCurrencyFormat.format(new Float(priceEachValue));
					} //end of try block
					catch (Exception exception)
					{
						priceEachString = "0.00";
					} //end of catch block (Exception)


					Items = Items + "<tr>\n";
					Items = Items + "<td width=\"15%\">&nbsp;"+sku.getDisplayString()+"</td>\n";
					Items = Items + "<td width=\"40%\">&nbsp;"+desc.getDisplayString()+"</td>\n";
					Items = Items + "<td width=\"15%\">&nbsp;"+quantity+"</td>\n";
					Items = Items + "<td width=\"15%\" align=\"right\">&nbsp;$"+priceEachString+"</td>\n";
					Items = Items + "<td width=\"15%\" align=\"right\">&nbsp;$"+priceExe.getDisplayString()+"</td>\n";
					Items = Items + "</tr>\n";
               }
				}//end of while


				String totalItems;
				try
				{
				  totalItems = wholeNumberFormat.format(new Float(itemLines.getTotalItems()));
				} //end of try block
				catch (Exception exception)
				{
				  totalItems = "0";
				}

				try
				{
					subTotal = currencyFormat.format(new Float(itemLines.getSubtotal()));
				} //end of try block
				catch (Exception exception)
				{
					subTotal = "0.00";
				} //end of catch block (Exception)


				try
				{
					sTax = currencyFormat.format(new Float(itemLines.getTax()));
				} //end of try block
				catch (Exception exception)
				{
					sTax = "0.00";
				} //end of catch block (Exception)


				try
				{
					total = currencyFormat.format(new Float(itemLines.getTotal()));
				} //end of try block
				catch (Exception exception)
				{
					total = "0.00";
				} //end of catch block (Exception)

			}




			proposallistform = (ProposalListForm) hm.get("dyna");
			if (hm.get("itemLines") != null){
				proposallistform.setItemLines((ItemLines)hm.get("itemLines"));
			}
			int iOpportunityId;
			iOpportunityId = Integer.parseInt(proposallistform.getOpportunityid());

			OpportunityVO opportunityVO	= remote.getOpportunity(individualID, iOpportunityId);
			OpportunityForm opportunityForm = new OpportunityForm();

			PrintTemplateHome PTHome = (PrintTemplateHome)CVUtility.getHomeObject("com.centraview.printtemplate.PrintTemplateHome", "Printtemplate");
			PrintTemplate PTRemote = PTHome.create();
			PTRemote.setDataSource(dataSource);
      
			PrintTemplateVO ptVO = (PrintTemplateVO)PTRemote.getPrintTemplate(2);

			String proposalCompose = ptVO.getPtData();

			//Setting the Proposal Name
			String title = proposallistform.getProposal();
			if (title == null || title.equals("null"))
      {
				title = "";
			}

			//Setting the Entity Name
			String Company = "";
			if (opportunityVO != null)
      {
				Company = opportunityVO.getEntityname();
				if (Company == null || Company.equals("null"))
        {
					Company = "";
				}
			}

			GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);

			Vector termVec = new Vector();
			if (gml.get("AllSaleTerm") != null)
      {
			  termVec = (Vector)gml.get("AllSaleTerm");
			}

			Iterator itTerms = termVec.iterator();

			String PaymentTerms = "";

			String paymentTerms = proposallistform.getTerms();
			int termsID = 0 ;

			if (paymentTerms != null )
      {
				termsID = Integer.parseInt(paymentTerms);
			}

			while (itTerms.hasNext())
			{
			  DDNameValue nameValue = (DDNameValue) itTerms.next();
			  // if name matches, then...
			  if (termsID == nameValue.getId())
			  {
          // ... get the ID
          PaymentTerms = nameValue.getName();
			  }
			}


			String AccountManager = "";
			if (opportunityVO != null)
      {
				AccountManager = opportunityVO.getManagerName();
				if (AccountManager == null || AccountManager.equals("null"))
        {
					AccountManager = "";
				}
			}

			String PurchaseOrder = "";

			Date currentDate  = new Date(System.currentTimeMillis());
			String date = (currentDate.getYear()+1900)+ "/" + (currentDate.getMonth() + 1) + "/" + currentDate.getDate();


			//Setting the Individual Name
			String Individual = proposallistform.getIndividual();
			if (Individual == null || Individual.equals("null"))
      {
				Individual = "";
			}

			//Setting the Address
			ContactFacadeHome cfHome  = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
			ContactFacade cfRemote  = (ContactFacade) cfHome.create();
			cfRemote.setDataSource(dataSource);

			Vector stateList = cfRemote.getStates();
			Vector countryList = cfRemote.getCountry();

			String OwningEntity="";

			EntityVO entityVO = cfRemote.getEntity(1);



			if (entityVO	!= null)
      {
				AddressVO primaryAdd       = entityVO.getPrimaryAddress();
				OwningEntity= OwningEntity + "<p><font face=\"Arial, Helvetica, sans-serif\"><strong>" +entityVO.getName()+"</strong><br>\n";
				if (primaryAdd != null)
				{
					if (primaryAdd.getStreet1() != null)
					{
						OwningEntity= OwningEntity+ primaryAdd.getStreet1()+"<BR>\n";
					}
          
					if (primaryAdd.getStreet2() != null)
					{
						OwningEntity= OwningEntity+ primaryAdd.getStreet2()+"<BR>\n";
					}
          
					if (primaryAdd.getCity() != null)
					{
						OwningEntity=OwningEntity + primaryAdd.getCity()+", ";
					}


					String state = primaryAdd.getStateName();
                    if (state != null){
					    OwningEntity=OwningEntity + state+" ";
                    }

					if (primaryAdd.getZip() != null)
					{
						OwningEntity=OwningEntity + primaryAdd.getZip()+"<BR>";
					}

					String country = primaryAdd.getCountryName();

                    if (country != null)
					{
					    OwningEntity= OwningEntity + country+"<BR>";
                    }

					String Email = "";

					String Phone = "";

					Collection mocList = entityVO.getMOC();
					Iterator iterator = mocList.iterator();
					int count = 1;
					while (iterator.hasNext())
					{
						MethodOfContactVO moc = (MethodOfContactVO)iterator.next();
						if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) // this is for email
						{
							Email=moc.getContent()+"<BR>";
						}else if (moc.getMocType() != 1){
							if(moc.getContent() != null)
              {
								Phone=Phone+ moc.getContent()+" ";
							}
						}
					}
					OwningEntity = OwningEntity + Email+"<BR>";
					OwningEntity = OwningEntity+ Phone+"<BR>";

					if (primaryAdd.getWebsite() != null)
					{
						OwningEntity = OwningEntity + primaryAdd.getWebsite()+"<BR>";
					}
				}
			}
			OwningEntity = OwningEntity +"</font></p>";

			String BillTo = "<p><font face=\"Arial, Helvetica, sans-serif\">" +
                      "<strong>" + Company + "</strong><br>" +
                      "<strong>" + Individual + "</strong><br>";

			String billingAddress = proposallistform.getBillingaddress();
			String BillingAddress[] = new String[5];

			if (billingAddress != null && !billingAddress.equals(""))
      {
				StringTokenizer st = new StringTokenizer(billingAddress, ",");
				int shipIndex = 0;
				while (st.hasMoreTokens())
				{
				  BillingAddress[shipIndex] = (String) st.nextToken();
				  shipIndex++;
        }
        
        if (BillingAddress[0] == null || BillingAddress[0].equals("null"))
        {
          BillingAddress[0] = "";
        }
        
        if (BillingAddress[1] == null || BillingAddress[1].equals("null"))
        {
          BillingAddress[1] = "";
        }
        
        if (BillingAddress[2] == null || BillingAddress[2].equals("null"))
        {
          BillingAddress[2] = "";
        }
        
        if (BillingAddress[3] == null || BillingAddress[3].equals("null"))
        {
          BillingAddress[3] = "";
        }
        
        if (BillingAddress[4] == null || BillingAddress[4].equals("null"))
        {
          BillingAddress[4] = "";
        }
        
        BillTo = BillTo + BillingAddress[0] + "<BR>";
        BillTo = BillTo + BillingAddress[1] + "," + BillingAddress[2] + "  " + BillingAddress[3] + "<br>";
        BillTo = BillTo + BillingAddress[4] + "<BR>";
			}

			BillTo = BillTo + "</font></p>";


			String ShipTo = "<p><font face=\"Arial, Helvetica, sans-serif\">" +
                      "<strong>" + Company + "</strong><br>" +
                      "<strong>" + Individual + "</strong><br>";

			String shippingAddress = proposallistform.getShippingaddress();
			String ShippingAddress[] = new String[5];

			if (shippingAddress != null && !shippingAddress.equals(""))
      {
				StringTokenizer st = new StringTokenizer(shippingAddress, ",");
				int shipIndex = 0;
				while (st.hasMoreTokens())
				{
          ShippingAddress[shipIndex] = (String) st.nextToken();
          shipIndex++;
        }
        
        if (ShippingAddress[0] == null || ShippingAddress[0].equals("null"))
        {
          ShippingAddress[0] = "";
        }
        
        if (ShippingAddress[1] == null || ShippingAddress[1].equals("null"))
        {
          ShippingAddress[1] = "";
        }
        
        if (ShippingAddress[2] == null || ShippingAddress[2].equals("null"))
        {
          ShippingAddress[2] = "";
        }
        
        if (ShippingAddress[3] == null || ShippingAddress[3].equals("null"))
        {
          ShippingAddress[3] = "";
        }
        
        if (ShippingAddress[4] == null || ShippingAddress[4].equals("null"))
        {
          ShippingAddress[4] = "";
        }
        
        ShipTo = ShipTo + ShippingAddress[0] + "<BR>";
        ShipTo = ShipTo + ShippingAddress[1] + "," + ShippingAddress[2] + "  " + ShippingAddress[3] + "<br>";
        ShipTo = ShipTo + ShippingAddress[4] + "<BR>";
			}
			ShipTo = ShipTo +"</font></p>";

			String Notes = proposallistform.getSpecialinstructions();

			if (Notes == null || Notes.equals("null")){
				Notes = "";
			}

      proposalCompose = proposalCompose.replaceAll("<AccountManager/>", AccountManager);
			proposalCompose = proposalCompose.replaceAll("<PurchaseOrder/>", PurchaseOrder);
			proposalCompose = proposalCompose.replaceAll("<PaymentTerms/>", PaymentTerms);
			proposalCompose = proposalCompose.replaceAll("<Date/>", date);


			proposalCompose = proposalCompose.replaceAll("<OwningEntity/>", OwningEntity);
			proposalCompose = proposalCompose.replaceAll("<BillTo/>", BillTo);
			proposalCompose = proposalCompose.replaceAll("<ShipTo/>", ShipTo);
			proposalCompose = proposalCompose.replaceAll("<Notes/>", Notes);
			proposalCompose = proposalCompose.replaceAll("<SubTotal/>", subTotal);
			proposalCompose = proposalCompose.replaceAll("<Tax/>", sTax);
			proposalCompose = proposalCompose.replaceAll("<Total/>", total);


			proposalCompose = proposalCompose.replaceAll("&lt;AccountManager/&gt;", AccountManager);
			proposalCompose = proposalCompose.replaceAll("&lt;PurchaseOrder/&gt;", PurchaseOrder);
			proposalCompose = proposalCompose.replaceAll("&lt;PaymentTerms/&gt;", PaymentTerms);
			proposalCompose = proposalCompose.replaceAll("&lt;Date/&gt;", date);


			proposalCompose = proposalCompose.replaceAll("&lt;OwningEntity/&gt;", OwningEntity);
			proposalCompose = proposalCompose.replaceAll("&lt;BillTo/&gt;", BillTo);
			proposalCompose = proposalCompose.replaceAll("&lt;ShipTo/&gt;", ShipTo);
			proposalCompose = proposalCompose.replaceAll("&lt;OwningEntity/&gt;", OwningEntity);
			proposalCompose = proposalCompose.replaceAll("&lt;Notes/&gt;", Notes);
			proposalCompose = proposalCompose.replaceAll("&lt;SubTotal/&gt;", subTotal);
			proposalCompose = proposalCompose.replaceAll("&lt;Tax/&gt;", sTax);
			proposalCompose = proposalCompose.replaceAll("&lt;Total/&gt;", total);

			StringBuffer ProposalMessage = new StringBuffer();
			String fieldName = "&lt;ProposalDetails/&gt;";

			int fieldLength = fieldName.length();
			int fieldIndex= proposalCompose.indexOf("&lt;ProposalDetails/&gt;");
			if (fieldIndex < 0){
				fieldName = "<ProposalDetails/>";
				fieldIndex = proposalCompose.indexOf("<ProposalDetails/>");
				fieldLength = fieldName.length();
			}

			if(fieldIndex != 0){
				ProposalMessage.append(proposalCompose.substring(0,fieldIndex));
				ProposalMessage.append(Items);
				ProposalMessage.append(proposalCompose.substring(fieldIndex+fieldLength,proposalCompose.length()));
			}

			//request.setAttribute("message", ProposalMessage.toString());
			//request.setAttribute("composeType","Proposal");
      composeForm.set("body", ProposalMessage.toString());
			FORWARD_final = FORWARD_viewproposal;

		} //end of try block
		catch (Exception e)
		{
		  System.out.println("[Exception]: ComposeProposalHandler.execute: " + e.toString());
		  e.printStackTrace();
		  FORWARD_final = GLOBAL_FORWARD_failure;
		} //end of catch block (Exception)
		return mapping.findForward(FORWARD_final);
	} //end of execute method
} //end of ComposeProposalHandler class
