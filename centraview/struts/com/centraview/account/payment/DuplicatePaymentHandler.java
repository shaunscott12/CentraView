/*
 * $RCSfile: DuplicatePaymentHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:22 $ - $Author: mking_cv $
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

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.PaymentLines;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;


public class DuplicatePaymentHandler extends org.apache.struts.action.Action
 {
    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_editpayment = ".view.accounting.new_payment";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;



    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

     	try
		{
			PaymentForm paymentForm = (PaymentForm)form;
			PaymentLines paymentLines = null;

			HttpSession session = request.getSession(true);
			int individualID= ((UserObject)session.getAttribute("userobject")).getIndividualID();
			session.setAttribute("highlightmodule", "account");

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PAYMENT);
			request.setAttribute("body",AccountConstantKeys.ADD );

			int paymentID = 0;
			String paymentIDStr = (String)request.getParameter("rowId");
			if(paymentIDStr != null && !paymentIDStr.equals(""))
				paymentID  = Integer.parseInt(paymentIDStr);

				// for getting data from EJB
			AccountFacadeHome yy = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
			AccountFacade remote =(AccountFacade)yy.create();
			remote.setDataSource(dataSource);

			PaymentVO paymentVO = remote.getPaymentVO(paymentID,individualID);
			paymentForm.setReference(paymentVO.getReference());

			paymentForm.setCardNumber(paymentVO.getCardNumber());

			paymentForm.setCardType(paymentVO.getCardType());

			paymentForm.setCheckNumber(paymentVO.getCheckNumber());

			paymentForm.setDescription(paymentVO.getDescription());

			paymentForm.setEntityName(paymentVO.getEntity());

			paymentForm.setEntityId(paymentVO.getEntityID()+"");

//				paymentForm.setCardExpiry(paymentVO.getExpirationDate());
			paymentForm.setCardExpiry(new java.sql.Date(2003, 8, 28));

			paymentForm.setPaymentAmount(paymentVO.getPaymentAmount()+"");

			paymentForm.setPaymentMethodID(paymentVO.getPaymentMethodID()+"");

			paymentForm.setPmntID(paymentVO.getPaymentID()+"");

			paymentLines = paymentVO.getPaymentLines();

			paymentForm.setPaymentLines(paymentLines);

//				java.sql.Date invDate = paymentVO.getPaymentDate();
/*				if (invDate != null)
			{
				int month = invDate.getMonth()+1;
				paymentForm.setMonth(month+"");

				int date = invDate.getDate();
				paymentForm.setDate(date+"");

				int year= invDate.getYear();
				year = year+1900;
				paymentForm.setYear(year+"");
			}
*/			// forward to jsp page
			FORWARD_final = FORWARD_editpayment;
		 	form = paymentForm;
		}
		 catch (Exception e)
		{
			System.out.println("[Exception][DuplicatePaymentHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
