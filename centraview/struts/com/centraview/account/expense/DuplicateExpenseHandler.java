/*
 * $RCSfile: DuplicateExpenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:12 $ - $Author: mking_cv $
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
  * File Name:	DuplicateExpenseHandler.java
  * Purpose :	Handler-Servlet for DuplicateInventory Action
  * Author :	Shilpa Patil
  * Date :		17 Sep 2003
  * Change History:
	By		Version		Date		Purpose
  */
package com.centraview.account.expense;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;

public class DuplicateExpenseHandler  extends org.apache.struts.action.Action
{

	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_editexpense = "ExpenseDetails";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;


	private static Logger logger = Logger.getLogger(DuplicateExpenseHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
		try
		{
			HttpSession session = request.getSession(true);
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.EXPENSE);

			request.setAttribute("body", "ADD");

			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();

			UserPrefererences userPref = userobjectd.getUserPref();
			String dateFormat	= userPref.getDateFormat();

			dateFormat= "M/d/yyyy";
			String timeZone		= userPref.getTimeZone();
			if(timeZone == null)
				timeZone = "EST";

			GregorianCalendar gCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
			SimpleDateFormat dForm = new SimpleDateFormat(dateFormat);
			dForm.setCalendar(gCal);

			int expenseID = Integer.parseInt((String)request.getParameter("rowId"));

			ExpenseForm expenseForm =  new ExpenseForm();

			AccountFacade remote =(AccountFacade)accountFacadeHome.create();
			remote.setDataSource(dataSource);

			ExpenseVO expenseVO = remote.getExpenseVO(expenseID,individualID);

			expenseForm.setExpenseID(0);

			expenseForm.setGlAccountID(""+expenseVO.getGlAccountIDValue());

			expenseForm.setAmount(expenseVO.getAmount());
			expenseForm.setTitle(expenseVO.getTitle());
			expenseForm.setExpenseDescription(expenseVO.getExpenseDescription());

			expenseForm.setEntity(expenseVO.getEntity());
			expenseForm.setEntityID(""+expenseVO.getEntityIDValue());

			java.sql.Date date = expenseVO.getDateEntered();

			expenseForm.setDateEntered(dForm.format(date));

			expenseForm.setStatusID(""+expenseVO.getStatusIDValue());

			expenseForm.setEmployee(expenseVO.getEmployee());
			expenseForm.setEmployeeID(""+expenseVO.getEmployeeIDValue());

			expenseForm.setProject(expenseVO.getProject());
			expenseForm.setProjectID(""+expenseVO.getProjectIDValue());

			expenseForm.setOpportunity(expenseVO.getOpportunity());
			expenseForm.setOpportunityID(""+expenseVO.getOpportunityIDValue());

			expenseForm.setSupportTicket(expenseVO.getSupportTicket());
			expenseForm.setSupportTicketID(""+expenseVO.getSupportTicketIDValue());

			expenseForm.setNotes(expenseVO.getNotes());

			expenseForm.setItemLines(expenseVO.getItemLines());

			request.setAttribute("expenseform",expenseForm);

			// forward to jsp page
			FORWARD_final = FORWARD_editexpense;
		}//end of try block
		catch (Exception e)
		{
			logger.error("[Exception] DuplicateExpenseHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}//end of catch block
		return (mapping.findForward(FORWARD_final));
	}
}
