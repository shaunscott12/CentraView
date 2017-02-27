/*
 * $RCSfile: PromotionListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:18 $ - $Author: mking_cv $
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
package com.centraview.marketing ;
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

import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;

/**
 * Handles list action for display of Promotion list.
 * Creation date: 28 Aug 2003
 * @author: Linesh
 */
public class PromotionListHandler extends org.apache.struts.action.Action {

	private static Logger logger = Logger.getLogger(PromotionListHandler.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)		throws IOException, ServletException, CommunicationException, NamingException
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		
		HttpSession session = request.getSession(true);
		com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
		int individualId = userobjectd.getIndividualID();
		ListPreference listpreference= userobjectd.getListPreference( "Promotion" );
		
		//System.out.println( "PromotionListHandler::listpreference " + listpreference );
		PromotionList displaylistSession = null;
		PromotionList displaylist = null;
		try
		{
			displaylistSession = ( PromotionList )session.getAttribute( "displaylist") ;
		}
		catch( ClassCastException e )
		{
			displaylistSession = null;
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
				
		try
		{
			displaylist = ( PromotionList )request.getAttribute( "displaylist") ;
		}
		catch( ClassCastException e )
		{
			displaylist = null;
		}
		
		
		PromotionList DL = null ;
		if( displaylist == null  )
		{
			 com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
			 int records = listpreference.getRecordsPerPage();
			 String sortelement = listpreference.getSortElement();
			 DL = ( PromotionList )lg.getPromotionList( individualId , 1, records , "" ,sortelement );
		}
		else if(displaylistSession !=null)
		{
		
			String searchSession = displaylistSession.getSearchString();
			String searchrequest = displaylist.getSearchString();
		
			if(searchSession == null)
				searchSession = "";
			if(searchrequest == null)
			searchrequest = "";
		
		
			 if (( (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) && (displaylist.getSortType()==(displaylistSession.getSortType()) ) &&  (searchSession.equals(searchrequest)) )|| displaylist.getAdvanceSearchFlag() == true)
			 {
				DL = ( PromotionList )displaylistSession;
				request.setAttribute("displaylist" , displaylistSession );
			 }else
			 {
		
				ListGenerator lg = ListGenerator.getListGenerator(dataSource);
				DL = ( PromotionList )lg.getPromotionList( individualId , displaylist );
			 }
		}
		
		
		//code to set the url for required cols
		DL = setLinksfunction( DL );
		session.setAttribute("displaylist" , DL );
		request.setAttribute("displaylist" , DL );
		request.setAttribute("TypeOfOperation","Promotions" );

		return ( mapping.findForward("showpromotionlist") );
	}

	/**
	@ 	uses
		This function sets links on members
	*/
	public PromotionList setLinksfunction( PromotionList DL )
	{

			Set listkey = DL.keySet();
		 	Iterator it =  listkey.iterator();
 	     	while( it.hasNext() )
         	{
				try
				{
					String str = ( String )it.next();
		        	ListElement ele  = ( ListElement )DL.get( str );
					StringMember sm  = ( StringMember )ele.get("title" );
					IntMember im  	 = ( IntMember )ele.get("promotionid" );
					int promotionID	 = (( Integer )im.getMemberValue()).intValue();


				 	sm.setRequestURL("goTo('ViewPromotion.do?promotionid="+ (( Integer )im.getMemberValue()).intValue() +"')");

					sm 	= ( StringMember )ele.get("ownername");
					im  = ( IntMember )ele.get("ownerid" );
					sm.setRequestURL("goTo('ViewIndividualDetail.do?rowid="+ (( Integer )im.getMemberValue()).intValue() +"')");
					}catch(Exception e )
				{
					System.out.println( "PromotionList:setLinksfunction"+ e );
				}
			}

			return DL;
	}



}
