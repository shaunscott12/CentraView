/*
 * $RCSfile: DisplayNewInvoiceHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:16 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.account.item.ItemList;
import com.centraview.common.CVUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DisplayNewInvoiceHandler extends org.apache.struts.action.Action
{
	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_newinvoice = ".view.accounting.new_invoice";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;
	static int counter=0;
	long entityID = 0;
	long orderID = 0;

	private static Logger logger = Logger.getLogger(DisplayNewInvoiceHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		AccountHelperHome accountHelperHome = (AccountHelperHome) CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome","AccountHelper");
		try
		{
			InvoiceForm invoiceform = (InvoiceForm)form;
			invoiceform.convertItemLines();
			ItemLines itemLines=null;

			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();
			session.setAttribute("highlightmodule", "account");

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
			request.setAttribute("body", AccountConstantKeys.ADD );
			String ordernameStr = request.getParameter("OrderName");
			String entityidStr = request.getParameter("EntityID");
			String orderidStr = request.getParameter("OrderID");

			if ((ordernameStr != null) && (entityidStr != null) && (orderidStr !=null)) {
				((InvoiceForm)form).setOrderid(orderidStr);
				((InvoiceForm)form).setCustomerId(entityidStr);
				((InvoiceForm)form).setOrdername(ordernameStr);
			}// end of if ((ordernameStr != null) && (entityidStr != null) && (orderidStr !=null))

			//Call EJb  Account Helper to set ItemLines
			AccountHelper accHelper = (AccountHelper) accountHelperHome.create();
			accHelper.setDataSource(dataSource);

			Vector taxJurisdiction = accHelper.getTaxJurisdiction();
			((InvoiceForm)form).setJurisdictionVec(taxJurisdiction);

			if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null) {
				/*	Remove item	*/
				if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.REMOVEITEM)) {
					String removeIDs = request.getParameter("removeID");
					StringTokenizer st;
					Iterator itr;
					Vector removeKeys = new Vector();
					itemLines = ((InvoiceForm)form).getItemLines();
					if (itemLines != null) {

						st = new StringTokenizer(removeIDs, ",");
						while (st.hasMoreTokens()) {
							String str = st.nextToken();
							int removeToken = Integer.parseInt(str);
							itr = itemLines.keySet().iterator();
							while (itr.hasNext()) {

								Object obj = itr.next();
								ItemElement ILE = (ItemElement)itemLines.get(obj);
								IntMember ItemId = (IntMember)ILE.get("ItemId");
								Integer currItemId = (Integer)ItemId.getMemberValue();
								if ( currItemId.intValue() == removeToken) {

									String status = ILE.getLineStatus();
									if (status.equals("Active")) {
										ILE.setLineStatus("Deleted");
									}//end of if (status.equals("Active"))
									else  if (status.equals("New")) {
										removeKeys.add(obj);
									}//end of else  if (status.equals("New"))
								}//end of if ( currItemId.intValue() == removeToken)
							}//end of while (itr.hasNext())
						}//end of while (st.hasMoreTokens())
						for(int i=0; i<removeKeys.size(); i++) {
							itemLines.remove(removeKeys.get(i));
						}
					}//end of if (itemLines != null)
					invoiceform.convertItemLines();
					((InvoiceForm)form).setItemLines(itemLines);
				}// end of if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.REMOVEITEM))
				/* 	ADD item */
				else if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.ADDITEM)) {

					String newItemID = request.getParameter("theitemid");
					ItemList IL = null ;
					ListGenerator lg = ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
					IL = (ItemList )lg.getItemList( individualID , 1, 10 , "" , "ItemID");//called when the request for the list is for first time

					StringTokenizer st;
					String token, nextItr;
					if (newItemID != null)
					{
						st = new StringTokenizer(newItemID, ",");
						itemLines = ((InvoiceForm)form).getItemLines();

						if(itemLines == null)
							itemLines = new ItemLines();
						int counter = itemLines.size();
						while (st.hasMoreTokens())
						{
							token   = (String)st.nextToken();
							int intToken = Integer.parseInt(token);

							Iterator itr = IL.keySet().iterator();
							while (itr.hasNext())
							{
								nextItr = (String)itr.next();
								ListElement ile = (ListElement)IL.get(nextItr);
								IntMember smid = (IntMember)ile.get("ItemID");
								Integer listItemid = (Integer)smid.getMemberValue();

								if ( listItemid.intValue() == intToken )
								{

									StringMember smName = (StringMember)ile.get("Name"); // name = description
									String name = (String) smName.getMemberValue();

									StringMember smSku  = (StringMember) ile.get("SKU");
									String sku = (String)smSku.getMemberValue();
									FloatMember dmprice  = (FloatMember) ile.get("Price");

									float price = Float.parseFloat((dmprice.getMemberValue()).toString());

									int id = ile.getElementID();

									IntMember LineId = new IntMember("LineId",0,'D',"",'T',false,20);
									IntMember ItemId = new IntMember("ItemId",id,'D',"",'T',false,20);
									IntMember  Quantity = new IntMember("Quantity",1,'D',"",'T',false,20);
									FloatMember  PriceEach = new FloatMember("Price",new Float(price),'D',"",'T',false,20);
									StringMember SKU = new StringMember("SKU",sku,'D',"",'T',false);
									StringMember Description = new StringMember("Description",name,'D',"",'T',false);
									FloatMember  PriceExtended = new FloatMember("PriceExtended",new Float(0.0f),'D',"",'T',false,20);
									FloatMember  TaxAmount = new FloatMember("TaxAmount",new Float(0.0f),'D',"",'T',false,20);

									ItemElement ie = new ItemElement(0);
									ie.put ("LineId",LineId);
									ie.put ("ItemId",ItemId);
									ie.put ("SKU",SKU);
									ie.put ("Description",Description);
									ie.put ("Quantity",Quantity);
									ie.put ("Price",PriceEach);
									ie.put ("PriceExtended",PriceExtended);
									ie.put ("TaxAmount",TaxAmount);

									ie.setLineStatus("New");
									counter += 1;
									itemLines.put(new Integer(counter), ie);
									break;
								}// end of if ( listItemid.intValue() == intToken )
							}// end of while (itr.hasNext())
						}// end of while (st.hasMoreTokens())
						((InvoiceForm)form).setJurisdictionVec(taxJurisdiction);
						((InvoiceForm)form).setItemLines(itemLines);
					}// end of if(newItemID != null)

				}// end of else if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.ADDITEM))
			}//end of if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null)
			else if(request.getParameter("actionType") == null) {
				form = null;
				InvoiceForm invForm =  new InvoiceForm();
				invForm.setJurisdictionVec(taxJurisdiction);
				request.setAttribute("invoiceform", invForm);
			}//end of else for if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null)
			// forward to jsp page
			FORWARD_final = FORWARD_newinvoice;
		} //end of try block
		catch (Exception e) {
			logger.error("[Exception] DisplayNewInvoiceHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}// end of catch block
		return (mapping.findForward(FORWARD_final));
	}// end of execute method
}// end of class
