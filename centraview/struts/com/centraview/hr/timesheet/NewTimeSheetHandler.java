/*
 * $RCSfile: NewTimeSheetHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:03 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.DisplayList;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.StringMember;
import com.centraview.hr.common.HrConstantKeys;
/*
Author: Nilesh Ghorpade
*/

public class NewTimeSheetHandler extends org.apache.struts.action.Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String returnStatus = "failure";
		HttpSession session = null;
		try
		{
			session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "hr");
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );

			String sParam = (String)request.getParameter("fillDetails");
			if(sParam != null && sParam.equals("true"))
				returnStatus = ".view.hr.timesheet.detail";
			else
				returnStatus = ".view.hr.timesheet.detail";
			request.setAttribute(HrConstantKeys.TYPEOFSUBMODULE,HrConstantKeys.TIMESHEET);
			//((DynaValidatorForm)form).set("status1",new Long(2));
			String sFirstName = userobjectd.getfirstName();
			String sLastName = userobjectd.getlastName();
			String sName = sFirstName + " " + sLastName;

			((DynaValidatorForm)form).set("employee",sName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("In HR View HANDLER Returning failure ");
			return (mapping.findForward("failure"));
		}
		finally
		{
			DisplayList DL = (DisplayList)request.getAttribute("displaylist");
			DisplayList DLSession = (DisplayList)session.getAttribute("displaylist");
			session.setAttribute("displaylist",DLSession);
			request.setAttribute("displaylist",DL);
			request.setAttribute("SAMETIMESHEET","no");
		}
    return mapping.findForward(returnStatus);
	}

	/*
	@ uses
	This function sets links on members
	*/
	public void setLinks(DisplayList DL )
	{
		Set listkey = DL.keySet();
	 	Iterator it =  listkey.iterator();
	    while( it.hasNext() )
     	{
		 	String str = ( String )it.next();
			StringMember sm=null;
	        ListElement ele  = ( ListElement )DL.get( str );
			sm = ( StringMember )ele.get("Name" );
			sm.setRequestURL("openPopup('ViewProjectDetail.do?rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");

			sm = ( StringMember )ele.get("Entity" );
			IntMember im = (IntMember) ele.get("EntityID");
			sm.setRequestURL("openPopup('ViewEntityDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
     	}

	}
}