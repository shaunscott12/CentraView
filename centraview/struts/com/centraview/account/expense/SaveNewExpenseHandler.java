/*
 * $RCSfile: SaveNewExpenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:14 $ - $Author: mking_cv $
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

public class SaveNewExpenseHandler  extends org.apache.struts.action.Action
{

	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_savenew = "savenew";
	private static final String FORWARD_saveclose = "saveclose";
	private static final String FORWARD_save = "save";

	private static String FORWARD_final = FORWARD_savenew;


	private static Logger logger = Logger.getLogger(SaveNewExpenseHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
			HttpSession session = request.getSession(true);
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.EXPENSE);

			request.setAttribute("body", "EDIT");

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

			String typeOfSave = null;

			if(request.getParameter("typeOfSave") != null)
				typeOfSave = (String)request.getParameter("typeOfSave");

			ExpenseForm expenseForm = (ExpenseForm) request.getAttribute("expenseform");

			int expenseID = 0;

			if(expenseForm != null)
				expenseID = expenseForm.getExpenseID();


			//Convert all strings to int
			expenseForm.convertFormbeanToValueObject();

			//Put all item to ItemLines
			expenseForm.convertItemLines();

			ExpenseVOX vox  = new ExpenseVOX(expenseForm);
			ExpenseVO expenseVO = vox.getVO();

			AccountFacadeHome hm = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
			AccountFacade remote =(AccountFacade)hm.create();
			remote.setDataSource(dataSource);

			expenseVO = remote.createExpense(expenseVO,individualID);
			expenseForm.setExpenseID(expenseVO.getExpenseID());
			java.sql.Date date = expenseVO.getDateEntered();
			expenseForm.setDateEntered(dForm.format(date));

			expenseForm.setItemLines(expenseVO.getItemLines());
			request.setAttribute("ItemLines", expenseVO.getItemLines());

			if(typeOfSave != null && typeOfSave.equals("savenew"))
			{
				// forward to jsp page
				FORWARD_final = FORWARD_savenew;
			}//end of if(typeOfSave != null && typeOfSave.equals("savenew"))
			if(typeOfSave != null && typeOfSave.equals("saveclose"))
			{
				// forward to jsp page
				FORWARD_final = FORWARD_saveclose;
				request.setAttribute("body", "list");
			}//end of if(typeOfSave != null && typeOfSave.equals("saveclose"))

			if(typeOfSave != null && typeOfSave.equals("save"))
			{
				// forward to jsp page
				FORWARD_final = FORWARD_save;
				request.setAttribute("body", "EDIT");
				if (expenseID > 0)
				{
					expenseForm.setDateEntered((expenseVO.getDateEntered()).toString());
				}//end of if (expenseID > 0)
				request.setAttribute("expenseform",expenseForm);
			}//end of if(typeOfSave != null && typeOfSave.equals("save"))
			request.setAttribute("moduleName", "Expense");
		} catch (Exception e)
		{
			logger.error("[Exception] SaveNewExpenseHandler.Execute Handler ", e);
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		return (mapping.findForward(FORWARD_final));
	}
}