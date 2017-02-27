/*
 * $RCSfile: DuplicateHrExpenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:00 $ - $Author: mking_cv $
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

package com.centraview.hr.expenses;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserPrefererences;
import com.centraview.hr.helper.ExpenseFormVO;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;

public class DuplicateHrExpenseHandler  extends org.apache.struts.action.Action
 {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_editexpense = ".view.hr.expenseform.detail";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
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


			int expenseFormID = Integer.parseInt((String)request.getParameter("rowId"));
			HrExpenseForm hrexpenseForm= new HrExpenseForm();
			
			HrFacadeHome hm = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome","HrFacade");
			HrFacade remote =(HrFacade)hm.create();
      remote.setDataSource(dataSource);
			
			ExpenseFormVO expenseFormVO = remote.getExpenseFormVO(expenseFormID);
			hrexpenseForm.setExpenseFormID(0);


			hrexpenseForm.setFormDescription(expenseFormVO.getDescription());

			hrexpenseForm.setEmployee(expenseFormVO.getEmployee());
			hrexpenseForm.setEmployeeID(expenseFormVO.getEmployeeId());
			hrexpenseForm.setReportto(expenseFormVO.getReportingTo());
			hrexpenseForm.setReporttoID(expenseFormVO.getReportingId());

			hrexpenseForm.setNotes(expenseFormVO.getNotes());

			if (expenseFormVO.getFrom() != null )
			{
				java.sql.Date fromDate = expenseFormVO.getFrom();
				String sfromDate =  fromDate.toString();
				Vector fromDateSplitUp = getDate(sfromDate);

				hrexpenseForm.setFromyear((String)fromDateSplitUp.get(0));
				hrexpenseForm.setFrommonth((String)fromDateSplitUp.get(1));
				hrexpenseForm.setFromday((String)fromDateSplitUp.get(2));
			}

			if (expenseFormVO.getTo() != null )
			{
				java.sql.Date toDate = expenseFormVO.getTo();
				String stoDate =  toDate.toString();
				//String[] sTokenizedDate = getDate(stoDate);
				Vector toDateSplitUp = getDate(stoDate);
				hrexpenseForm.setToyear((String)toDateSplitUp.get(0));
				hrexpenseForm.setTomonth((String)toDateSplitUp.get(1));
				hrexpenseForm.setToday((String)toDateSplitUp.get(2));
			}
			
			hrexpenseForm.setItemLines(expenseFormVO.getHrExpenseLines());

			request.setAttribute("ExpenseFormVO",expenseFormVO);
			session.setAttribute("ExpenseFormVO",expenseFormVO);
			
			//Added by Rohit to check 
			request.setAttribute("HrExpenseForm",hrexpenseForm);
			session.setAttribute("HrExpenseForm",hrexpenseForm);
			//Added ends here 

			// forward to jsp page
			FORWARD_final = FORWARD_editexpense;

		} catch (Exception e)
		{
			System.out.println("[Exception][DuplicateHrExpenseHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		request.setAttribute("DUPLICATE","true");
	    return mapping.findForward(FORWARD_final); 		
    }
	
	
	
	private  Vector getDate(String sfromDate)
	{
		StringTokenizer stok = new StringTokenizer(sfromDate,"-");
		String[] sTokens = new String[]{};
		int i = 0;
		Vector  vecDate = new Vector();
		while (stok.hasMoreTokens())
		{
			String sToken = stok.nextToken();
			vecDate.add(sToken);
		}
		return vecDate;
	}	
}
