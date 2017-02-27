/*
 * $RCSfile: DisplayDuplicateInvoiceHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:16 $ - $Author: mking_cv $
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
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DisplayDuplicateInvoiceHandler  extends org.apache.struts.action.Action
{

	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";


	// Local Forwards
	private static final String FORWARD_editinvoice = ".view.accounting.new_invoice";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;


	private static Logger logger = Logger.getLogger(DisplayDuplicateInvoiceHandler.class);


	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{
			InvoiceForm invoiceForm = (InvoiceForm)form;

			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();
			session.setAttribute("highlightmodule", "account");

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
			request.setAttribute("body",AccountConstantKeys.ADD );

			int invoiceID = 0;
			String invoiceIDStr = (String)request.getParameter("rowId");
			if(invoiceIDStr != null && !invoiceIDStr.equals(""))
				invoiceID  = Integer.parseInt(invoiceIDStr);

			// for getting data from EJB
			AccountFacade remote =(AccountFacade)accountFacadeHome.create();
			remote.setDataSource(dataSource);

			Vector taxJurisdiction = remote.getTaxJurisdiction();

			InvoiceVO vo = remote.getInvoiceVO(invoiceID,individualID);
			invoiceForm.setInvoiceid(vo.getInvoiceId()+"");
			invoiceForm.setOrderid(vo.getOrderId()+"");
			invoiceForm.setCustomerId(vo.getCustomerId()+"");

			invoiceForm.setBillto(vo.getBillToAddress()+"");
			invoiceForm.setBilltoID(vo.getBillToId()+"");

			invoiceForm.setShiptoID(vo.getShipToId()+"");
			invoiceForm.setShipto(vo.getShipToAddress()+"");

			invoiceForm.setStatusid(vo.getStatusId()+"");
			invoiceForm.setInvoiceDate(vo.getInvoiceDate());
			//					invoiceForm.setExternalid(String externalid);

			invoiceForm.setPoid(vo.getPo());
			invoiceForm.setTermid(vo.getTermId()+"");
			invoiceForm.setAccountmanagerid(vo.getAccountManagerId()+"");

			invoiceForm.setProjectid(vo.getProjectId()+"");
			invoiceForm.setNotes(vo.getDescription()+"");
			invoiceForm.setProjectName(vo.getProjectName());
			invoiceForm.setAccountmanagerName(vo.getAccountManagerName());
			invoiceForm.setItemLines(vo.getItemLines());
			invoiceForm.setOrdername(vo.getCustomerName());
			invoiceForm.setJurisdictionID(vo.getJurisdictionID());
			invoiceForm.setJurisdictionVec(taxJurisdiction);

			java.sql.Date invDate = vo.getInvoiceDate();
			if (invDate != null)
			{
				int month = invDate.getMonth();
				invoiceForm.setMonth(month+"");

				int date = invDate.getDate();
				invoiceForm.setDate(date+"");

				int year= invDate.getYear();
				year = year+1900;
				invoiceForm.setYear(year+"");
			}// end of if (invDate != null)

			// forward to jsp page
			FORWARD_final = FORWARD_editinvoice;
			form = invoiceForm;
		}// end of try block
		catch (Exception e)
		{
			logger.error("[Exception] DisplayDuplicateInvoiceHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}//end of catch block
		return (mapping.findForward(FORWARD_final));
	}
}
