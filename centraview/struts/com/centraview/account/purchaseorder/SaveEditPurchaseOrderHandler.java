/*
 * $RCSfile: SaveEditPurchaseOrderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:26 $ - $Author: mking_cv $
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
 * SaveEditPurchaseOrderHandler.java
 *
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

public class SaveEditPurchaseOrderHandler  extends org.apache.struts.action.Action
{
	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_save 	= "save";
	private static final String FORWARD_savenew = "savenew";
	private static final String FORWARD_saveclose 	= "saveclose";
	private static final String FORWARD_delete 		= "delete";

	private static String FORWARD_final = GLOBAL_FORWARD_failure;


	private static Logger logger = Logger.getLogger(SaveEditPurchaseOrderHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{
			HttpSession session = request.getSession();
			int IndividualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

			PurchaseOrderForm purchaseForm = (PurchaseOrderForm)form;
			purchaseForm.convertItemLines();
			purchaseForm.setModifiedBy(IndividualId);

			String typeOfSave = "saveclose";

			if (request.getParameter("buttonpress") != null)
				typeOfSave = request.getParameter("buttonpress");

			AccountFacade remote = (AccountFacade)accountFacadeHome.create();
			remote.setDataSource(dataSource);

			PurchaseOrderVOX vox  = new PurchaseOrderVOX(purchaseForm);
			PurchaseOrderVO vo = vox.getVO();

			if (typeOfSave != null && typeOfSave.equals("save"))
			{
				remote.updatePurchaseOrder(vo,IndividualId);
				FORWARD_final = FORWARD_save;
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
			}// end of if (typeOfSave != null && typeOfSave.equals("save"))
			else if (typeOfSave != null && typeOfSave.equals("savenew"))
			{
				remote.updatePurchaseOrder(vo,IndividualId);
				FORWARD_final = FORWARD_savenew;
				request.setAttribute("clearform", "true");
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
			}// end of else if (typeOfSave != null && typeOfSave.equals("savenew"))
			else if (typeOfSave != null && typeOfSave.equals("saveclose"))
			{
				remote.updatePurchaseOrder(vo,IndividualId);
				FORWARD_final = FORWARD_saveclose;
				request.setAttribute("body", "list");
			}// end of else if (typeOfSave != null && typeOfSave.equals("saveclose"))
			else if (typeOfSave != null && typeOfSave.equals("delete"))
			{
				remote.deletePurchaseOrder(vo.getPurchaseOrderId(),IndividualId);
				FORWARD_final = FORWARD_saveclose;
				request.setAttribute("body", "list");
			}// end of else if (typeOfSave != null && typeOfSave.equals("delete"))

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PURCHASEORDER);
		}
		catch (Exception e)
		{
			logger.error("[Exception] SaveEditPurchaseOrderHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		return (mapping.findForward(FORWARD_final));
	}
}
