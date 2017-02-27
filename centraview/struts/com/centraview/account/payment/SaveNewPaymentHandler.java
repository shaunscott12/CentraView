/*
 * $RCSfile: SaveNewPaymentHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:24 $ - $Author: mking_cv $
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
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveNewPaymentHandler extends org.apache.struts.action.Action
 {
    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_save = ".view.accounting.view_payment";
	private static final String FORWARD_savenew = ".view.accounting.new_payment";
	private static final String FORWARD_saveclose = ".view.accounting.paymentlist";

    private static String FORWARD_final = GLOBAL_FORWARD_failure;



    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
     	try
		{
			PaymentForm paymentForm = (PaymentForm)form;
			paymentForm.convertPaymentLines();
			paymentForm.convertFormbeanToValueObject();

			//DoubleMember 	AmountApplied = new DoubleMember("AmountApplied",new Double(payment[i]),'D',"",'T',false,20);

			HttpSession session = request.getSession();
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();


			String typeOfSave = "saveclose";

			if (request.getParameter("buttonpress") != null)
				typeOfSave = request.getParameter("buttonpress");


			AccountFacadeHome yy = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
			AccountFacade remote = (AccountFacade)yy.create();
			remote.setDataSource(dataSource);

			PaymentVOX vox  = new PaymentVOX(paymentForm);
			PaymentVO vo = vox.getVO();


			vo = remote.createPayment(vo,userId);

			request.setAttribute("paymentid",vo.getPaymentID()+"");

			if (typeOfSave.equals("save"))
			{
				FORWARD_final = FORWARD_save;
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
			} else if (typeOfSave.equals("savenew"))
			{
				FORWARD_final = FORWARD_savenew;
				request.setAttribute("clearform", "true");
				request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
			}
			else if (typeOfSave.equals("saveclose"))
			{
				FORWARD_final = FORWARD_saveclose;
				request.setAttribute("body", "list");
			}

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PAYMENT);

		} catch (Exception e)
		{
			System.out.println("[Exception][SaveNewPaymentHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
