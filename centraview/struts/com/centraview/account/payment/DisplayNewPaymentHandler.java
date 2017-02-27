/*
 * $RCSfile: DisplayNewPaymentHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:22 $ - $Author: mking_cv $
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

package com.centraview.account.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.PaymentLines;
import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class DisplayNewPaymentHandler extends org.apache.struts.action.Action
 {
    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";
    
    // Local Forwards
    private static final String FORWARD_newpayment = ".view.accounting.new_payment";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
	static int counter;



    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

     	try 
		{
			PaymentForm paymentForm = (PaymentForm)form;

			PaymentLines paymentLines = null;
			
			HttpSession session = request.getSession(true);			
			session.setAttribute("highlightmodule", "account");
			
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PAYMENT);
			request.setAttribute("body",AccountConstantKeys.ADD);
			
			int entityID = 0;
			
			if (paymentForm.getEntityId() != null) 
			{
				entityID = Integer.parseInt(paymentForm.getEntityId());
			}

			if (entityID != 0)
			{
				AccountHelperHome hm1 = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome","AccountHelper");
				AccountHelper accHelper = (AccountHelper)hm1.create();
				accHelper.setDataSource(dataSource);
		
				paymentLines = accHelper.getPaymentInvoices(entityID);		
				
			    paymentForm.setPaymentLines(paymentLines);
				request.setAttribute("PaymentLines", paymentLines);
					
				form = paymentForm;
			}else if(request.getParameter("actionType") == null){
                paymentForm.clearForm(paymentForm);
            }
	
		  // forward to jsp page			
			  
			FORWARD_final = FORWARD_newpayment;
			  
		}	
		catch (Exception e)	
		{
			System.out.println("[Exception][DisplayNewPaymentHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		return (mapping.findForward(FORWARD_final));
    }
}
