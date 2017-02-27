/*
 * $RCSfile: GLAccountListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:14 $ - $Author: mking_cv $
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
 * GLAccountListHandler.java
 *
 * Handles list action for display of GLAccounts list.
 * Creation date: 14 July 2003
 * @author: Shilpa Patil
 */


package com.centraview.account.glaccount;

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

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class GLAccountListHandler extends org.apache.struts.action.Action {

	// Global Forwards
   	public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
   	private static final String FORWARD_showinventorylist = "showglaccountlist";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;

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


		request.setAttribute("showNewButton", Boolean.FALSE);

		try
		{

			HttpSession session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "account");//Highlights the Accounting link on header.jsp

			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();
			ListPreference listpreference= userobjectd.getListPreference("GLAccount");

			GLAccountList displaylistSession = new GLAccountList();
			GLAccountList displaylist = new GLAccountList();

			try
			{
				displaylistSession = ( GLAccountList )session.getAttribute( "displaylist") ;//gets the list from session
			}
			catch( Exception e )
			{
				displaylistSession = null;
			}
			try
			{
				displaylist = ( GLAccountList )request.getAttribute( "displaylist") ;//gets the list from request
			}
			catch( Exception e )
			{
				displaylist = null;
			}


			GLAccountList DL = new GLAccountList() ;

			if( displaylist == null  )
			{

				com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
				int records = listpreference.getRecordsPerPage();//gets the initial record per page to be displayed from listPreference
				String sortelement = listpreference.getSortElement();//gets the initial sort element from listPreference
				DL = (GLAccountList)lg.getGLAccountList( individualID , 1, records , "" ,sortelement);//called when the request for the list is for first time
				DL = setLinksfunction( DL );
			}// end of if( displaylist == null  )
			else
			{
				String searchSession = displaylistSession.getSearchString();
				String searchrequest = displaylist.getSearchString();

				if(searchSession == null)
					searchSession = "";
				if(searchrequest == null)
					searchrequest = "";

				if ((( displaylistSession.getListID() == displaylist.getListID() )  &&
						 (displaylist.getDirtyFlag() == false ) &&
						 ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) &&
						 ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) &&
						 (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) &&
						 (displaylist.getSortType()==(displaylistSession.getSortType()) &&
						 (searchSession.equals(searchrequest))  ) )
						 || displaylist.getAdvanceSearchFlag() == true)
				{
					DL = (GLAccountList) displaylistSession;
				}
				else
				{
					ListGenerator lg = ListGenerator.getListGenerator(dataSource);
					DL = ( GLAccountList )lg.getGLAccountList( individualID , displaylist );
				}
				DL = setLinksfunction( DL );
			}// end of else for if( displaylist == null  )

			session.setAttribute( "displaylist" , DL );
			request.setAttribute("displaylist" , DL );

			FORWARD_final = FORWARD_showinventorylist;
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.GLACCOUNT);
			request.setAttribute("body", "list");
		}// end of try block
		catch (Exception e)
		{
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}// end of catch (Exception e)
		return ( mapping.findForward(FORWARD_final) );
	}

	/**
		@uses ListElement and various member classes
		This function sets links on members
	    @ returns GLAccountList object
	*/


	public GLAccountList setLinksfunction( GLAccountList DL )
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
				sm = ( StringMember )ele.get("Name" );
            sm.setRequestURL("goTo('#')");//sets the URL for this member

				sm = ( StringMember )ele.get("ParentAccount" );
            sm.setRequestURL("goTo('#')");//sets the URL for this member

			}catch(Exception e )
			{
				System.out.println( "GLAccountList:setLinksfunction"+ e );
			}
		}
		return DL;
	}


}
