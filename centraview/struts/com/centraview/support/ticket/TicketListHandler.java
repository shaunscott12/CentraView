/*
 * $RCSfile: TicketListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:55 $ - $Author: mking_cv $
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

package com.centraview.support.ticket;

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
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;

public class TicketListHandler extends org.apache.struts.action.Action {

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

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		try 
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();



			HttpSession session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
      {
				session.setAttribute("highlightmodule", "support");//Highlights the note link on header.jsp
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
			
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();
			ListPreference listpreference= userobjectd.getListPreference("Ticket");

			TicketList displaylistSession = null;
			TicketList displaylist = null;
			try {
				displaylistSession = ( TicketList )session.getAttribute( "displaylist") ;//gets the list from session
			} catch( Exception e ) {
				displaylistSession = null;
			}
			try {
				displaylist = ( TicketList )request.getAttribute( "displaylist") ;//gets the list from request
			} catch( Exception e ) {
				displaylist = null;
			}
			TicketList DL = null ;
			if( displaylist == null  )
      {
				com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
				int records = listpreference.getRecordsPerPage();//gets the initial record per page to be displayed from listPreference
				String sortelement = listpreference.getSortElement();//gets the initial sort element from listPreference
				DL = (TicketList )lg.getTicketList( individualID , 1, records , "" ,sortelement);//called when the request for the list is for first time
				DL = setLinksfunction( DL );
			}
			else //if(displaylistSession !=null)
			{
				String searchSession = displaylistSession.getSearchString();
				String searchrequest = displaylist.getSearchString();
				if(searchSession == null)
        {
					searchSession = "";
        }
				if(searchrequest == null)
        {
					searchrequest = "";
        }
				if (( (displaylistSession.getListID() == displaylist.getListID() )
          && ( displaylist.getDirtyFlag() == false )
          && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() )
          && ( displaylist.getEndAT() <= displaylistSession.getEndIndex() )
          && (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) )
          && (displaylist.getSortType() == (displaylistSession.getSortType()))
          && (searchSession.equals(searchrequest)))
          || displaylist.getAdvanceSearchFlag() == true)
        {
          DL = (TicketList)displaylistSession;
				}else {
					ListGenerator lg = ListGenerator.getListGenerator(dataSource);
					DL = (TicketList)lg.getTicketList(individualID, displaylist);
				}
				DL = setLinksfunction(DL);
			}
			session.setAttribute("displaylist", DL);
			request.setAttribute("displaylist", DL);
			request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE,"Ticket");
		}
		catch (Exception e)
    {
      System.out.println("[Exception][TicketListHandler.execute] Exception Thrown: " + e);
			return (mapping.findForward("failure"));
		}
			return ( mapping.findForward("showticketlist") );
	}

	/**
		uses ListElement and various member classes
		This function sets links on members
		@return TicketList object
	*/
	public TicketList setLinksfunction( TicketList DL ) {

		Set listkey = DL.keySet();
		Iterator it =  listkey.iterator();
		while( it.hasNext() )
    {
			try
      {
				String str = ( String )it.next();
				StringMember sm = null;
				ListElement ele  = ( ListElement )DL.get( str );
				sm = ( StringMember )ele.get("Subject" );
				sm.setRequestURL("openWindow('/centraview/ViewTicket.do?TYPEOFOPERATION=EDIT&rowId="+ele.getElementID()+"&listId="+DL.getListID()+"', 'tv', 750, 550, '')");//sets the URL for this member
				sm = ( StringMember )ele.get("AssignedTo" );
				IntMember im = ( IntMember )ele.get("IndividualID" );
				Integer value = (Integer)im.getMemberValue();
				int IndividualID = value.intValue();
				sm.setRequestURL( "openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");//sets the URL for this member
				sm = ( StringMember )ele.get("Entity" );
				im = ( IntMember )ele.get("EntityID" );
				sm.setRequestURL( "openPopup('ViewEntityDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");//sets the URL for this member
			}
      catch(Exception e )
      {
				System.out.println("[Exception][TicketListHandler.setLinksfunction] Exception Thrown: " + e);
			}
		}
		return DL;
	}
}