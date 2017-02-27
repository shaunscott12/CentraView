/*
 * $RCSfile: ExpenseListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:13 $ - $Author: mking_cv $
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
import java.util.Iterator;
import java.util.Set;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class ExpenseListHandler extends org.apache.struts.action.Action {

	// Global Forwards
	public static final String GLOBAL_FORWARD_failure = "failure";

	// Local Forwards
	private static final String FORWARD_showexpenselist = "showexpenselist";
	private static String FORWARD_final = GLOBAL_FORWARD_failure;
	private static Logger logger = Logger.getLogger(ExpenseListHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)		throws IOException, ServletException, CommunicationException, NamingException
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		HttpSession session = request.getSession(true);
		session.setAttribute("highlightmodule", "account");

		request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.EXPENSE);
		request.setAttribute("body", "list");

		UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );//get the user object
		int individualID = userobjectd.getIndividualID();
		ListPreference listpreference= userobjectd.getListPreference("Expense");

		ExpenseList displaylistSession = null;
		ExpenseList displaylist = null;

		
		// After performing the logic in the DeleteHanlder, we are generat a new request for the list
		// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
		// Then destory it after getting the Session value
		if (session.getAttribute("listErrorMessage") != null)
		{
			ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
			saveErrors(request, allErrors);
			session.removeAttribute("listErrorMessage");
		}//end of if (session.getAttribute("listErrorMessage") != null)
		try
		{
			displaylistSession = ( ExpenseList )session.getAttribute( "displaylist") ;//gets the list from session
		}
		catch( Exception e )
		{
			displaylistSession = null;
		}
		try
		{
			displaylist = ( ExpenseList )request.getAttribute( "displaylist") ;//gets the list from request
		}
		catch( Exception e )
		{
			displaylist = null;
		}

		ListGenerator lg = ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
		ExpenseList DL = null ;

		if( displaylist == null  )
		{
			int records = listpreference.getRecordsPerPage();//gets the initial record per page to be displayed from listPreference
			String sortelement = listpreference.getSortElement();//gets the initial sort element from listPreference
			DL = (ExpenseList)lg.getExpenseList( individualID , 1, records , "" ,sortelement);//called when the request for the list is for first time
			DL = setLinksfunction( DL );
		}
		else
		{
			String searchSession = displaylistSession.getSearchString();
			String searchrequest = displaylist.getSearchString();
			if(searchSession == null)
				searchSession = "";
			if(searchrequest == null)
				searchrequest = "";

			if (((displaylistSession.getListID() == displaylist.getListID() )  &&
					( displaylist.getDirtyFlag() == false ) &&
					( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) &&
					( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) &&
					(displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) &&
					(displaylist.getSortType()==(displaylistSession.getSortType()) &&
					(searchSession.equals(searchrequest))  ) ) ||
					displaylist.getAdvanceSearchFlag() == true)
			{
				DL = (ExpenseList) displaylistSession;
			}
			else
			{
				DL = ( ExpenseList )lg.getExpenseList( individualID , displaylist );
			}// end of else
			DL = setLinksfunction( DL );
		}

		session.setAttribute( "displaylist" , DL );
		request.setAttribute("displaylist" , DL );

		// forward to jsp page
		FORWARD_final = FORWARD_showexpenselist;
		return (mapping.findForward(FORWARD_final));
	}


	/**
	@ 	uses ListElement and various member classes
		This function sets links on members
	@ returns InventoryList object
	*/
	public ExpenseList setLinksfunction( ExpenseList DL )
	{
		String url = null;
		Set listkey = DL.keySet();
		Iterator it =  listkey.iterator();
		while( it.hasNext() )
		{
			try
			{
				String str = ( String )it.next();

				StringMember sm = null;
				ListElement ele  = ( ListElement )DL.get( str );

				IntMember intExpenseID = (IntMember)ele.get("ExpenseID");
				int expID = 0;

				if(intExpenseID != null)
				expID = ((Integer)intExpenseID.getMemberValue()).intValue();

				//sets the URL for this member
				url = "goTo('/centraview/DisplayExpense.do?action=edit&expenseid="+expID+"')";
				intExpenseID.setRequestURL(url);

				Integer intIndividualID = (Integer)((IntMember)ele.get("IndividualID")).getMemberValue();
				//sets the URL for this member
				sm = ( StringMember )ele.get("Creator");

				if (intIndividualID != null)
				{
					url = "openPopup('ViewIndividualDetail.do?rowId="+intIndividualID.intValue()+"')";
					sm.setRequestURL(url);
				}
			}catch(Exception e )
			{
				logger.error("[Exception] ExpenseListHandler.setLinksfunction ", e);
			}
		}
		return DL;
	}
}
