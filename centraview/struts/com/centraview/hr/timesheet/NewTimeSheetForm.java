/*
 * $RCSfile: NewTimeSheetForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.hr.timesheet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.Validation;

/**
 * @author Nilesh Ghorpade
 *
 */
public class NewTimeSheetForm extends DynaValidatorForm
{

	//private		TimeSlipLines	timeslipLines	=	null;

	/*
	*<P>
	*Validation is done when the
	*</P>
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();
		HttpSession session = null;
		try
		{
			// initialize validation
			Validation validation = new Validation();

			validation.checkForRequired("label.hr.newtimesheet.reportingto", ((String)get("reportingTo")), "error.application.required", errors);
			validation.checkForRequired("label.hr.newtimesheet.employee", ((String)get("employee")), "error.application.required", errors);
			validation.checkForRequired("label.hr.newtimesheet.fromdate", ((String)get("fromday")), "error.application.required", errors);
			validation.checkForRequired("label.hr.newtimesheet.todate", ((String)get("toyear")), "error.application.required", errors);

			if (
				((String)get("fromday") != null && ((String)get("fromday")).length() != 0) ||
				 ((String)get("fromyear") != null && ((String)get("fromyear")).length() != 0) ||
				 ((String)get("frommonth") != null && ((String)get("frommonth")).length() != 0)
			   )
			{
				validation.checkForDate("label.hr.newtimesheet.fromdate", ((String)get("fromyear")), ((String)get("frommonth")), ((String)get("fromday")), "error.application.date", errors);
			}
			if (
				 (((String)get("today")) != null && ((String)get("today")).length() !=0) ||
				 (((String)get("toyear")) != null && ((String)get("toyear")).length() !=0) ||
				 (((String)get("tomonth")) != null && ((String)get("tomonth")).length() !=0)
				)
			  {
				validation.checkForDate("label.hr.newtimesheet.todate", ((String)get("toyear")), ((String)get("tomonth")), ((String)get("today")), "error.application.date", errors);
			}

			if(((String)get("fromday")) != null && ((String)get("fromday")).length() != 0 && ((String)get("today")) != null && ((String)get("today")).length() !=0)
				validation.checkForDateComparison ("label.hr.newtimesheet.fromdate", ((String)get("fromyear")), ((String)get("frommonth")),((String)get("fromday")), "label.hr.newtimesheet.todate", ((String)get("toyear")), ((String)get("tomonth")), ((String)get("today")), "error.application.datecomparison", errors);

			session = request.getSession(true);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			String sAddSlipFlag = (String)request.getAttribute("addslip");
			request.setAttribute("addslip",sAddSlipFlag);
			request.setAttribute("timesliplist",session.getAttribute("timesliplist"));
			String sAction = (String)session.getAttribute("TYPEOFOPERATION");
			if(!errors.isEmpty() && (sAction != null && sAction.equalsIgnoreCase("EDIT")))
				session.setAttribute("ERRORSPRESENT","EDIT");
		}
    return errors;
	}

}
