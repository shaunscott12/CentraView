/*
 * $RCSfile: NoteListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:22 $ - $Author: mking_cv $
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
 * NoteListHandler.java
 *
 * Handles list action for display of Note list.
 * Creation date: 14 July 2003
 * @author: Amit Gandhe
 */



package com.centraview.note;


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
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;


public class NoteListHandler extends org.apache.struts.action.Action {

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
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException	
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String noteType  =  NoteConstantKeys.MYNOTELIST;
		try
		{
		 	noteType  = (String)request.getParameter(NoteConstantKeys.TYPEOFNOTE) ;

			if(noteType == null)
				noteType = NoteConstantKeys.MYNOTELIST;




			HttpSession session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "note");//Highlights the note link on header.jsp

            com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();
		    ListPreference listpreference= userobjectd.getListPreference("Note");

			NoteList displaylistSession = null;
			NoteList displaylist = null;

			try
			{
				displaylistSession = ( NoteList )session.getAttribute( "displaylist") ;//gets the list from session

			}
			catch( Exception e )
			{
				displaylistSession = null;
			}
			try
			{
				displaylist = ( NoteList )request.getAttribute( "displaylist") ;//gets the list from request
			}
			catch( Exception e )
			{
				displaylist = null;
			}


			if ( noteType == null )
			{
				noteType = NoteConstantKeys.ALLNOTELIST;//if note Type is null set the default value to All Notes
			//	typeoflist = displaylist.getTypeoflist();
				System.out.println( "*******noteType******"+noteType );
			}

			// After performing the logic in the DeleteHanlder, we are generat a new request for the list
			// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
			// Then destory it after getting the Session value
			if (session.getAttribute("listErrorMessage") != null)
			{
				ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
				saveErrors(request, allErrors);
				session.removeAttribute("listErrorMessage");
			}//end of if (session.getAttribute("listErrorMessage") != null)
			
			NoteList DL = null ;

			if( displaylist == null  )
         	{

				 com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
				 int records = listpreference.getRecordsPerPage();//gets the initial record per page to be displayed from listPreference
				 String sortelement = listpreference.getSortElement();//gets the initial sort element from listPreference
				 DL = (NoteList )lg.getNoteList( individualID , 1, records , "" ,sortelement, noteType  );//called when the request for the list is for first time

				 request.setAttribute(NoteConstantKeys.TYPEOFNOTE,DL.getNoteType());

				 DL = setLinksfunction( DL );


				 session.setAttribute( "displaylist" , DL );
				 request.setAttribute("displaylist" , DL );

			}
			else //if(displaylistSession !=null)
			{
				String searchSession = displaylistSession.getSearchString();
				String searchrequest = displaylist.getSearchString();
				System.out.println("searchSession"+searchSession);
				System.out.println("searchrequest"+searchrequest);
				if(searchSession == null)
					searchSession = "";
				if(searchrequest == null)
					searchrequest = "";

				// if ( ( displaylistSession.getNoteType().equals(  displaylist.getNoteType() ) ) && (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) && (displaylist.getSortType()==(displaylistSession.getSortType()) ) )
				 if (( ( displaylistSession.getNoteType().equals(  displaylist.getNoteType() ) ) && (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) && (displaylist.getSortType()==(displaylistSession.getSortType()) && (searchSession.equals(searchrequest))  ) ) || displaylist.getAdvanceSearchFlag() == true)
				 {
					//session.setAttribute( "displaylist" , displaylistSession );
					request.setAttribute(NoteConstantKeys.TYPEOFNOTE,displaylist.getNoteType());

					request.setAttribute("displaylist" , displaylistSession );
				 }else
				 {

					ListGenerator lg = ListGenerator.getListGenerator(dataSource);

					//System.out.println( displaylist );
					DL = ( NoteList )lg.getNoteList( individualID , displaylist );

					request.setAttribute(NoteConstantKeys.TYPEOFNOTE,DL.getNoteType());


					DL = setLinksfunction( DL );
					session.setAttribute( "displaylist" , DL );
					request.setAttribute("displaylist" , DL );
				 }
			}








 		}
 		catch (Exception e)
		{
			e.printStackTrace();
			return (mapping.findForward("failure"));
		}
			return ( mapping.findForward("show"+noteType+"notelist") );
	}

	/**
	@ 	uses ListElement and various member classes
		This function sets links on members
	@ returns NoteList object
	*/


	public NoteList setLinksfunction( NoteList DL )
	{

		Set listkey = DL.keySet();
		Iterator it =  listkey.iterator();
		while( it.hasNext() )
		{
			try
			{
				String str = ( String )it.next();
				StringMember sm = null;
				ListElement ele  = ( ListElement )DL.get( str );
				sm = ( StringMember )ele.get("Title" );
				sm.setRequestURL("goTo('/centraview/ViewNote.do?TYPEOFOPERATION=EDIT&rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");//sets the URL for this member
				sm = ( StringMember )ele.get("CreatedBy" );

				IntMember im = ( IntMember )ele.get("IndividualID" );
				Integer value = (Integer)im.getMemberValue();
				int IndividualID = value.intValue();
				sm.setRequestURL( "openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");//sets the URL for this member

			}catch(Exception e )
			{
				System.out.println( "NoteList:setLinksfunction"+ e );
			}

		}

		return DL;

	}


}
