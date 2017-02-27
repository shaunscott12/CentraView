/*
 * $RCSfile: TimeSlipListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:32 $ - $Author: mking_cv $
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
 * TimeSlipListHandler Handler for displaying TimeSlip List
 *
 * @date   : 24-08-03
 * @version: 1.0
 */

package com.centraview.projects.timeslip;

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
import com.centraview.common.TimeSlipList;
import com.centraview.settings.Settings;

public class TimeSlipListHandler extends org.apache.struts.action.Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException	
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();


		try
		{
			HttpSession session = request.getSession(true);

			// After performing the logic in the DeleteHanlder, we are generat a new request for the list
			// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
			// Then destory it after getting the Session value
			if (session.getAttribute("listErrorMessage") != null)
			{
				ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
				saveErrors(request, allErrors);
				session.removeAttribute("listErrorMessage");
			}//end of if (session.getAttribute("listErrorMessage") != null)
			
			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "project");

			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );


            ListPreference listpreference= userobjectd.getListPreference("Timeslip");


			DisplayList displaylistSession = ( DisplayList )session.getAttribute( "displaylist") ;

			DisplayList displaylist = ( DisplayList )request.getAttribute( "displaylist") ;

			TimeSlipList DL= null;
			if( displaylist == null  )
			{
				 ListGenerator lg = ListGenerator.getListGenerator(dataSource);

				 DL = ( TimeSlipList )lg.getTimeSlipList( userobjectd.getIndividualID() , 1,listpreference.getRecordsPerPage() , "" ,listpreference.getSortElement());

			}
			else
			{

				String searchSession = displaylistSession.getSearchString();
				String searchrequest = displaylist.getSearchString();


				if(searchSession == null)
					searchSession = "";
				if(searchrequest == null)
					searchrequest = "";

				//if ( (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember()) && (displaylist.getSortType()==(displaylistSession.getSortType()) ) ))
				if (( (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember()) && (displaylist.getSortType()==(displaylistSession.getSortType())) && (searchSession.equals(searchrequest)) ))|| displaylist.getAdvanceSearchFlag() == true)
				 {
					DL = (TimeSlipList)displaylistSession;
					request.setAttribute("displaylist" , displaylistSession );
				 }
				 else
				 {

					ListGenerator lg = ListGenerator.getListGenerator(dataSource);
					DL = ( TimeSlipList )lg.getTimeSlipList( userobjectd.getIndividualID(),displaylist );
				 }

			}

			setLinks(DL);
			session.setAttribute( "displaylist" , DL );

			request.setAttribute("displaylist" , DL );
			request.setAttribute("list" , "Timeslip" );



		}
		catch(Exception e)
		{
			e.printStackTrace();
			return (mapping.findForward("failure"));
		}

		return mapping.findForward("showtimesliplist");
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

			IntMember im = (IntMember) ele.get("ID");
			im.setRequestURL("openWindow('ViewTimeSlipDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"'"+",'', 740,  335,'scrollbars=yes')");
			sm = ( StringMember )ele.get("Project" );
			if ( sm != null)
			{
				im = (IntMember) ele.get("ProjectID");
				sm.setRequestURL("openPopup('ViewProjectDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
			}

			sm = ( StringMember )ele.get("CreatedBy" );
			if ( sm != null)
			{
				im = (IntMember) ele.get("Creator");
				sm.setRequestURL("openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
			}

			sm = ( StringMember )ele.get("Task" );
			if ( sm != null)
			{
				im = (IntMember) ele.get("TaskID");
				sm.setRequestURL("openPopup('ViewProjectTaskDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
			}
         }
	}
}
