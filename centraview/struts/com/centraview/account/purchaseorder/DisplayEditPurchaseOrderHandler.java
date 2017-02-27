/*
 * $RCSfile: DisplayEditPurchaseOrderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:24 $ - $Author: mking_cv $
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

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.account.item.ItemList;
import com.centraview.common.CVUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DisplayEditPurchaseOrderHandler extends org.apache.struts.action.Action
{
	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_editpurchase = ".view.accounting.purchaseorder.edit";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;
	static int counter=0;


	private static Logger logger = Logger.getLogger(DisplayEditPurchaseOrderHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{
			PurchaseOrderForm purchaseForm = (PurchaseOrderForm)form;
			purchaseForm.convertItemLines();

			ItemLines itemLines = null;

			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PURCHASEORDER);

			request.setAttribute("body",AccountConstantKeys.EDIT);

			String typeOfOperation = request.getParameter(AccountConstantKeys.TYPEOFOPERATION);

			if ( typeOfOperation != null &&	typeOfOperation.equals("ShowPurchaseOrder"))
			{
				int purchaseID = 0;
				String purchaseIDStr = (String)request.getParameter("rowId");

				if(purchaseIDStr != null && !purchaseIDStr.equals(""))
					purchaseID  = Integer.parseInt(purchaseIDStr);

				// for getting data from EJB
				AccountFacade remote =(AccountFacade)accountFacadeHome.create();
				remote.setDataSource(dataSource);

				PurchaseOrderVO vo = remote.getPurchaseOrderVO(purchaseID,individualID);

				purchaseForm.setPurchaseOrderid(vo.getPurchaseOrderId()+"");
				purchaseForm.setVendorId(vo.getVendorId()+"");
				//			purchaseForm.setCustomerId(vo.getCustomerId()+"");

				purchaseForm.setBillto(vo.getBillToAddress()+"");
				purchaseForm.setBilltoID(vo.getBillToId()+"");

				purchaseForm.setShiptoID(vo.getShipToId()+"");
				purchaseForm.setShipto(vo.getShipToAddress()+"");

				purchaseForm.setStatusid(vo.getStatusId()+"");
				purchaseForm.setPurchaseOrderDate(vo.getPurchaseOrderDate());

				purchaseForm.setPoid(vo.getPurchaseOrderId()+"");
				purchaseForm.setTermid(vo.getTermId()+"");
				purchaseForm.setAccountmanagerid(vo.getAccountManagerId()+"");

				purchaseForm.setNotes(vo.getNotes());
				purchaseForm.setAccountmanagerName(vo.getAccountManagerName());
				purchaseForm.setItemLines(vo.getItemLines());
				itemLines = vo.getItemLines();

				purchaseForm.setVendorName(vo.getVendorName());
				java.sql.Date invDate = vo.getPurchaseOrderDate();

				if (invDate != null)
				{
					int month = invDate.getMonth();
					purchaseForm.setMonth(month+"");

					int date = invDate.getDate();
					purchaseForm.setDate(date+"");

					int year= invDate.getYear();
					purchaseForm.setYear(year+"");
				}//end of if (invDate != null)
				form = purchaseForm;
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION,"ShowPurchaseOrder");
			}//end of if ( typeOfOperation != null &&	typeOfOperation.equals("ShowPurchaseOrder"))
			else if ( typeOfOperation != null && typeOfOperation.equals("REMOVEITEM") )
			{
				String removeIDs = request.getParameter("removeID");
				StringTokenizer st;
				Iterator itr;
				Vector removeKeys = new Vector();

				itemLines = ((PurchaseOrderForm)form).getItemLines();
				if (itemLines != null)
				{
					st = new StringTokenizer(removeIDs, ",");
					while (st.hasMoreTokens())
					{
						String str = st.nextToken();
						int removeToken = Integer.parseInt(str);

						itr = itemLines.keySet().iterator();
						while (itr.hasNext())
						{
							Object obj = itr.next();
							ItemElement ILE = (ItemElement)itemLines.get(obj);
							IntMember ItemId = (IntMember)ILE.get("ItemId");
							Integer currItemId = (Integer)ItemId.getMemberValue();
							if ( currItemId.intValue() == removeToken)
							{
								String status = ILE.getLineStatus();
								if (status.equals("Active"))
								{
									ILE.setLineStatus("Deleted");
								}//end of if (status.equals("Active"))
								else if (status.equals("New"))
								{
									removeKeys.add(obj);
								}// end of else if (status.equals("New"))
							}//end of if ( currItemId.intValue() == removeToken)
						}//end of while (itr.hasNext())
					}//end of while (st.hasMoreTokens())

					for(int i=0; i<removeKeys.size(); i++)
					{
						itemLines.remove(removeKeys.get(i));
					}// end of for(int i=0; i<removeKeys.size(); i++)
				}// end of if (itemLines != null)

				((PurchaseOrderForm)form).setItemLines(itemLines);
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION,"REMOVEITEM");
			}//end of else if ( typeOfOperation != null && typeOfOperation.equals("REMOVEITEM") )
			/*ADD item*/
			else if ( typeOfOperation != null && typeOfOperation.equals("ADDITEM") )
			{

				String newItemID = request.getParameter("theitemid");
				ItemList IL = null ;
				ListGenerator lg = ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
				IL = (ItemList )lg.getItemList( individualID , 1, 10 , "" , "ItemID");//called when the request for the list is for first time

				StringTokenizer st;
				String token, nextItr;

				if (newItemID != null)
				{
					st = new StringTokenizer(newItemID, ",");
					itemLines = ((PurchaseOrderForm)form).getItemLines();

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

								ItemElement ie = new ItemElement(0);
								ie.put ("LineId",LineId);
								ie.put ("ItemId",ItemId);
								ie.put ("SKU",SKU);
								ie.put ("Description",Description);
								ie.put ("Quantity",Quantity);
								ie.put ("Price",PriceEach);
								ie.put ("PriceExtended",PriceExtended);

								ie.setLineStatus("New");
								counter += 1;
								itemLines.put(new Integer(counter), ie);
								break;
							}// end of if ( listItemid.intValue() == intToken )
						}// end of while (itr.hasNext())
					}// end of while (st.hasMoreTokens())
					((PurchaseOrderForm)form).setItemLines(itemLines);
				}// end of if(newItemID == null)

				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION,"ADDITEM");
			}// end of else if ( typeOfOperation != null && typeOfOperation.equals("ADDITEM") )
			request.setAttribute("ItemLines", itemLines);
			// forward to jsp page
			FORWARD_final = FORWARD_editpurchase;
		}
		catch (Exception e)
		{
			logger.error("[Exception] DisplayEditPurchaseOrderHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		request.setAttribute("moduleName", "PO");
		return (mapping.findForward(FORWARD_final));
	}
}
