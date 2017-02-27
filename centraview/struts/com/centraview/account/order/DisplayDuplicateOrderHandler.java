/*
 * $RCSfile: DisplayDuplicateOrderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:20 $ - $Author: mking_cv $
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

package com.centraview.account.order;

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

public class DisplayDuplicateOrderHandler  extends org.apache.struts.action.Action
{

	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_editorder = ".view.accounting.new_order";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;


	private static Logger logger = Logger.getLogger(DisplayDuplicateOrderHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{
			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();
			session.setAttribute("highlightmodule", "account");
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ORDER);
			request.setAttribute("body",AccountConstantKeys.ADD );
			request.setAttribute(AccountConstantKeys.TYPEOFOPERATION,"ShowOrder");
			int orderID = 0;
			String orderIDStr = (String)request.getParameter("rowId");
			if(orderIDStr != null )
			if ( !orderIDStr.equals(""))
			orderID  = Integer.parseInt(orderIDStr);

			AccountFacade remote =(AccountFacade)accountFacadeHome.create();
			remote.setDataSource(dataSource);
			Vector taxJurisdiction = remote.getTaxJurisdiction();
			OrderForm oForm = remote.getOrderForm(orderID,individualID);
			FORWARD_final = FORWARD_editorder;
			oForm.convertValueObjectToFormbean();
			oForm.setJurisdictionVec(taxJurisdiction);
			request.setAttribute("orderform",oForm);
			request.setAttribute("ItemLines",oForm.getItemLines());
		}//end of try block
		catch (Exception e)
		{
			logger.error("[Exception] DisplayDuplicateOrderHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}//end of catch block
		return (mapping.findForward(FORWARD_final));
	}
}
