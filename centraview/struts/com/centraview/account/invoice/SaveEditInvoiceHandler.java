/*
 * $RCSfile: SaveEditInvoiceHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:18 $ - $Author: mking_cv $
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

public class SaveEditInvoiceHandler  extends org.apache.struts.action.Action
{

	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_save = ".view.accounting.view_invoice";
	private static final String FORWARD_savenew = ".view.accounting.new_invoice";
	private static final String FORWARD_saveclose = ".view.accounting.invoicelist";

	private static String FORWARD_final = GLOBAL_FORWARD_failure;
	private static Logger logger = Logger.getLogger(SaveEditInvoiceHandler.class);




	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{

			HttpSession session = request.getSession();
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

			InvoiceForm invoiceForm = (InvoiceForm) form;
			invoiceForm.convertItemLines();
			invoiceForm.setModifiedBy(userId);

			String typeOfSave = "saveclose";

			if (request.getParameter("buttonpress") != null)
				typeOfSave = request.getParameter("buttonpress");


			AccountFacade remote =(AccountFacade)accountFacadeHome.create();
			remote.setDataSource(dataSource);

			int jurisdictionID = 0;
			int billingAddressID = 0;
			if(invoiceForm.getJurisdictionID() != null && !((invoiceForm.getJurisdictionID()).equals(""))){
				jurisdictionID = (invoiceForm.getJurisdictionID()).intValue();
			}//end of if(invoiceForm.getJurisdictionID() != null && !((invoiceForm.getJurisdictionID()).equals("")))
			if(invoiceForm.getBilltoID() != null && !((invoiceForm.getBilltoID()).equals(""))){
				billingAddressID = Integer.parseInt(invoiceForm.getBilltoID());
			}//end of if(invoiceForm.getBilltoID() != null && !((invoiceForm.getBilltoID()).equals("")))
			if(billingAddressID != 0 && jurisdictionID != 0){
				remote.setJurisdictionForAddress(billingAddressID,jurisdictionID);
			}//end of if(billingAddressID != 0 && jurisdictionID != 0)

			InvoiceVOX vox  = new InvoiceVOX(invoiceForm);
			InvoiceVO invoiceVO = vox.getVO();
			if (typeOfSave.equals("save"))
			{
				InvoiceForm newinvoiceform = new InvoiceForm();
				remote.updateInvoice(invoiceVO,userId);
				invoiceVO  = remote.getInvoiceVO(invoiceVO.getInvoiceId(),userId);

				newinvoiceform.setInvoiceid(invoiceVO.getInvoiceId()+"");
				newinvoiceform.setOrderid(invoiceVO.getOrderId()+"");
				newinvoiceform.setCustomerId(invoiceVO.getCustomerId()+"");

				newinvoiceform.setBillto(invoiceVO.getBillToAddress()+"");
				newinvoiceform.setBilltoID(invoiceVO.getBillToId()+"");

				newinvoiceform.setShiptoID(invoiceVO.getShipToId()+"");
				newinvoiceform.setShipto(invoiceVO.getShipToAddress()+"");

				newinvoiceform.setStatusid(invoiceVO.getStatusId()+"");
				newinvoiceform.setInvoiceDate(invoiceVO.getInvoiceDate());

				newinvoiceform.setPoid(invoiceVO.getPo());
				newinvoiceform.setTermid(invoiceVO.getTermId()+"");
				newinvoiceform.setAccountmanagerid(invoiceVO.getAccountManagerId()+"");

				newinvoiceform.setProjectid(invoiceVO.getProjectId()+"");
				newinvoiceform.setNotes(invoiceVO.getDescription()+"");
				newinvoiceform.setProjectName(invoiceVO.getProjectName());
				newinvoiceform.setAccountmanagerName(invoiceVO.getAccountManagerName());
				newinvoiceform.setItemLines(invoiceVO.getItemLines());
				newinvoiceform.setOrdername(invoiceVO.getCustomerName());
				newinvoiceform.setExternalid(invoiceVO.getExternalId());
				newinvoiceform.setJurisdictionID(invoiceVO.getJurisdictionID());


				java.sql.Date invDate = invoiceVO.getInvoiceDate();

				if (invDate != null)
				{
					int month = invDate.getMonth();
					month=month+1;
					newinvoiceform.setMonth(month+"");

					int date = invDate.getDate();
					newinvoiceform.setDate(date+"");

					int year= invDate.getYear();
					newinvoiceform.setYear(year+"");
				}// end of if (invDate != null)
				request.setAttribute("newinvoiceform",newinvoiceform);

				form = invoiceForm;

				FORWARD_final = FORWARD_save;
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
			}// end of if (typeOfSave.equals("save"))
			else if (typeOfSave.equals("savenew"))
			{
				remote.updateInvoice(invoiceVO,userId);

				Vector taxJurisdiction = remote.getTaxJurisdiction();
				invoiceForm.setJurisdictionVec(taxJurisdiction);

				FORWARD_final = FORWARD_savenew;
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
				request.setAttribute("invoiceform",invoiceForm);
				request.setAttribute("clearform","true");
			}// end of else if (typeOfSave.equals("savenew"))
			else if (typeOfSave.equals("saveclose"))
			{
				remote.updateInvoice(invoiceVO,userId);
				FORWARD_final = FORWARD_saveclose;
				request.setAttribute("body", "list");
			}// end of else if (typeOfSave.equals("saveclose"))
			else if (typeOfSave.equals("delete"))
			{
				remote.deleteInvoice(invoiceVO.getInvoiceId(),userId);
				FORWARD_final = FORWARD_saveclose;
				request.setAttribute("body", "list");
			}// end of else if (typeOfSave.equals("delete"))
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
		} // end of try block
		catch (Exception e)
		{
			logger.error("[Exception] SaveEditInvoiceHandler.Execute Handler ", e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}//end of catch block
		return (mapping.findForward(FORWARD_final));
	}// end of execute method

}// end of class
