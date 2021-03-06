/*
 * $RCSfile: TimeSheetListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:03 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.TimeSheetList;
import com.centraview.hr.common.HrConstantKeys;
import com.centraview.settings.Settings;
/*
Author: Nilesh Ghorpade
*/

public class TimeSheetListHandler extends org.apache.struts.action.Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException	
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
			HttpSession session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "hr");

			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );

			ListPreference listpreference= userobjectd.getListPreference("TimeSheet");

			DisplayList displaylistSession = ( DisplayList )session.getAttribute( "displaylist") ;

			// After performing the logic in the DeleteHanlder, we are generat a new request for the list
			// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
			// Then destory it after getting the Session value
			if (session.getAttribute("listErrorMessage") != null)
			{
				ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
				saveErrors(request, allErrors);
				session.removeAttribute("listErrorMessage");
			}//end of if (session.getAttribute("listErrorMessage") != null)
			
			DisplayList displaylist = ( DisplayList )request.getAttribute( "displaylist") ;
			TimeSheetList DL= null;
			ListGenerator lg = ListGenerator.getListGenerator(dataSource);
			if( displaylist == null  )
			{
				DL = ( TimeSheetList )lg.getTimeSheetList( userobjectd.getIndividualID() , 1,listpreference.getRecordsPerPage() , "" ,listpreference.getSortElement());
			}
			else//when display list is not null
			{
				String searchSession = displaylistSession.getSearchString();
				String searchrequest = displaylist.getSearchString();

				if(searchSession == null)
					searchSession = "";
				if(searchrequest == null)
					searchrequest = "";

				if (( (displaylistSession.getListID() == displaylist.getListID() ) &&
							( displaylist.getDirtyFlag() == false ) &&
							( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) &&
							( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) &&
							(displaylist.getSortMember().equals(displaylistSession.getSortMember()) &&
							(displaylist.getSortType()==(displaylistSession.getSortType())) &&
							(searchSession.equals(searchrequest)) )) ||
							displaylist.getAdvanceSearchFlag() == true)
				{
					DL = (TimeSheetList)displaylistSession;
					request.setAttribute("displaylist" , displaylistSession );
				}
				else
				{
					DL = ( TimeSheetList )lg.getTimeSheetList( userobjectd.getIndividualID(), displaylist );
				}

			}

			setLinks(DL);
			session.setAttribute( "displaylist" , DL );
			request.setAttribute("displaylist" , DL );
			request.setAttribute("list" , "TimeSheet" );
			request.setAttribute(HrConstantKeys.TYPEOFSUBMODULE,HrConstantKeys.TIMESHEET);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[Exception] TimeSheetListHandler.execute: " + e.toString());
			return (mapping.findForward("failure"));
		}

		return mapping.findForward("showHRTimeSheet");
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
			IntMember im = ( IntMember )ele.get("ID" );

			im.setRequestURL("goTo('ViewTimeSheetDetail.do?TYPEOFOPERATION=EDIT&rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");

			sm = ( StringMember )ele.get("Employee" );
			im = (IntMember) ele.get("EmployeeID");
			sm.setRequestURL("openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");

			sm = ( StringMember )ele.get("CreatedBy" );
			im = (IntMember) ele.get("Creator");
			sm.setRequestURL("openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");

     	}

	}
}