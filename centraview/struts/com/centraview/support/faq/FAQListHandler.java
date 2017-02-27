/*
 * $RCSfile: FAQListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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
 * FaqListHandler.java
 *
 * Handles list action for display of Faq list.
 * Creation date: 14 July 2003
 * @author: Amit Gandhe
 */



package com.centraview.support.faq;


import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;


public class FAQListHandler extends org.apache.struts.action.Action {

	/**
	 * This is overridden method  from Action class
	 *
	 * @param   mapping
	 * @param   form
	 * @param   request
	 * @param   response
	 * @return
	 * @exception   IOException
	 * @exception   ServletException
	 * @exception   CommunicationException
	 * @exception   NamingException
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException	
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();


		HttpSession session = request.getSession(true);

		if (session.getAttribute("highlightmodule") != null)
			session.setAttribute("highlightmodule", "support");//Highlights the note link on header.jsp

		com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
		int individualID = userobjectd.getIndividualID();
		ListPreference listpreference= userobjectd.getListPreference("FAQ");

			FAQList displaylistSession = null;
		FAQList displaylist = null;

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
			displaylistSession = ( FAQList )session.getAttribute( "displaylist") ;//gets the list from session

		}
		catch( Exception e )
		{
			displaylistSession = null;
		}
		try
		{
			displaylist = ( FAQList )request.getAttribute( "displaylist") ;//gets the list from request
		}
		catch( Exception e )
		{
			displaylist = null;
		}



		FAQList DL = null ;
		ListGenerator lg = ListGenerator.getListGenerator(dataSource);

		if( displaylist == null  ) {
			int records = listpreference.getRecordsPerPage();//gets the initial record per page to be displayed from listPreference
			String sortelement = listpreference.getSortElement();//gets the initial sort element from listPreference
			DL = (FAQList )lg.getFAQList( individualID , 1, records , "" ,sortelement);//called when the request for the list is for first time
			DL = setLinksfunction( DL );
		}
		else //if(displaylistSession !=null)
		{
			String searchSession = displaylistSession.getSearchString();
			String searchrequest = displaylist.getSearchString();

			if(searchSession == null)
				searchSession = "";
			if(searchrequest == null)
				searchrequest = "";

			if (((displaylistSession.getListID() == displaylist.getListID() )
					&& ( displaylist.getDirtyFlag() == false )
					&& ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() )
					&& ( displaylist.getEndAT()<=displaylistSession.getEndIndex() )
					&& (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) )
					&& (displaylist.getSortType()==(displaylistSession.getSortType())
					&& (searchSession.equals(searchrequest))))
					|| displaylist.getAdvanceSearchFlag() == true)
			{
				DL = (FAQList) displaylistSession;
			}else {
				DL = ( FAQList )lg.getFAQList( individualID , displaylist );
			}
			DL = setLinksfunction( DL );
		}
		session.setAttribute( "displaylist" , DL );
		request.setAttribute("displaylist" , DL );
		request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE,"FAQ");
		return ( mapping.findForward("showfaqlist") );
	}

	/**
	@ 	uses ListElement and various member classes
	This function sets links on members
	@ returns FAQList object
	*/


	public FAQList setLinksfunction( FAQList DL ) {

		Set listkey = DL.keySet();
		Iterator it =  listkey.iterator();
		while( it.hasNext() ) {
			try {
				String str = ( String )it.next();
				StringMember sm = null;
				ListElement ele  = ( ListElement )DL.get( str );
				sm = ( StringMember )ele.get("Title" );
				sm.setRequestURL("goTo('/centraview/ViewFaq.do?"+Constants.TYPEOFOPERATION+"=EDIT&rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");//sets the URL for this member


			}catch(Exception e ) {
				System.out.println( "NoteList:setLinksfunction"+ e );
			}

		}

		return DL;

	}


}
