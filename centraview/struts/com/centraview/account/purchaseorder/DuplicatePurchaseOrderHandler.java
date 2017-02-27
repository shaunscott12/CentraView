/*
 * $RCSfile: DuplicatePurchaseOrderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:24 $ - $Author: mking_cv $
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

/**
* DuplicatePurchaseOrderHandler.java
*
* @Author Deepa Sarwate
*/
package com.centraview.account.purchaseorder;

import java.io.IOException;

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
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DuplicatePurchaseOrderHandler extends org.apache.struts.action.Action
{
	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_editpurchaseorder = "duplicatepurchase";


	private static Logger logger = Logger.getLogger(DuplicatePurchaseOrderHandler.class);
	private static String FORWARD_final = GLOBAL_FORWARD_failure;



	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{
			PurchaseOrderForm purchaseForm = (PurchaseOrderForm)form;

			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();
			session.setAttribute("highlightmodule", "account");

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PURCHASEORDER);
			request.setAttribute("body",AccountConstantKeys.ADD );

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
			purchaseForm.setVendorName(vo.getVendorName());

			purchaseForm.setBillto(vo.getBillToAddress()+"");
			purchaseForm.setBilltoID(vo.getBillToId()+"");

			purchaseForm.setShipto(vo.getShipToAddress()+"");
			purchaseForm.setShiptoID(vo.getShipToId()+"");

			purchaseForm.setStatusid(vo.getStatusId()+"");

			purchaseForm.setPoid(vo.getPurchaseOrderId()+"");
			purchaseForm.setTermid(vo.getTermId()+"");

			purchaseForm.setAccountmanagerid(vo.getAccountManagerId()+"");
			purchaseForm.setAccountmanagerName(vo.getAccountManagerName());

			purchaseForm.setNotes(vo.getNotes()+"");

			purchaseForm.setItemLines(vo.getItemLines());

			java.sql.Date invDate = vo.getPurchaseOrderDate();
			if (invDate != null)
			{
				int month = invDate.getMonth()+1;
				purchaseForm.setMonth(month+"");

				int date = invDate.getDate();
				purchaseForm.setDate(date+"");

				int year= invDate.getYear();
				year = year+1900;
				purchaseForm.setYear(year+"");
			}
			// forward to jsp page
			FORWARD_final = FORWARD_editpurchaseorder;
			form = purchaseForm;
		}
		catch (Exception e)
		{
			logger.error("[Exception] DuplicatePurchaseOrderHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		return (mapping.findForward(FORWARD_final));
	}
}
