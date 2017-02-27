/*
 * $RCSfile: DisplayEditPaymentHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:22 $ - $Author: mking_cv $
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


public class DisplayEditPaymentHandler extends org.apache.struts.action.Action
 {
    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_editpayment = ".view.accounting.view_payment";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
    static int counter=0;



    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

     	try
		{
			PaymentForm paymentForm = (PaymentForm)form;
			paymentForm.convertPaymentLines();
			paymentForm.convertFormbeanToValueObject();

			PaymentLines paymentLines = null;
			PaymentLines paymentAppliedLines = null;

			HttpSession session = request.getSession(true);
			int individualID= ((UserObject)session.getAttribute("userobject")).getIndividualID();
			session.setAttribute("highlightmodule", "account");

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PAYMENT);
			request.setAttribute("body",AccountConstantKeys.EDIT);

			int paymentID = 0;

			if( request.getParameter("rowId") != null)
			{
				paymentID = Integer.parseInt((String)request.getParameter("rowId"));
				paymentForm.setPmntID(paymentID+"");
			}
			else if( request.getAttribute("paymentid") != null)
			{
				paymentID = Integer.parseInt((String)request.getAttribute("paymentid"));
				paymentForm.setPmntID(paymentID+"");
			}
			else
			{
				paymentID = paymentForm.getPmntIDValue();
			}


			if ( paymentID != 0)
			{
				// for getting data from EJB
				AccountFacadeHome hm = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
				AccountFacade remote = (AccountFacade)hm.create();
				remote.setDataSource(dataSource);

				PaymentVO paymentVO = remote.getPaymentVO(paymentID,individualID);

				// ----------set values in the form bean--------------


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

				paymentAppliedLines = paymentVO.getPaymentAppliedLines();

				paymentForm.setPaymentAppliedLines(paymentAppliedLines);

				//paymentForm.setPaymentLines(paymentLines);
				request.setAttribute("PaymentLines", paymentLines);

			form = paymentForm;
			}


			// forward to jsp page
			FORWARD_final = FORWARD_editpayment;

		}
		catch (Exception e)
		{
			System.out.println("[Exception][DisplayEditPaymentHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
