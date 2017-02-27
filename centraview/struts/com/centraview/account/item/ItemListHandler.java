/*
 * $RCSfile: ItemListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:19 $ - $Author: mking_cv $
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

package com.centraview.account.item;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;

public class ItemListHandler extends org.apache.struts.action.Action {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_showitemlist = "showitemlist";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;

    public ItemListHandler() {
        // TODO: Write constructor body
    }

		public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception		
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

			try
			{

				HttpSession session = request.getSession(true);
				session.setAttribute("highlightmodule", "account");

				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
				request.setAttribute("body", "list");

				// forward to jsp page
				FORWARD_final = FORWARD_showitemlist;

				// After performing the logic in the DeleteHanlder, we are generat a new request for the list
				// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
				// Then destory it after getting the Session value
				if (session.getAttribute("listErrorMessage") != null)
				{
					ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
					saveErrors(request, allErrors);
					session.removeAttribute("listErrorMessage");
				}//end of if (session.getAttribute("listErrorMessage") != null)
				
				com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
				int individualID = userobjectd.getIndividualID();
				ListPreference listpreference= userobjectd.getListPreference("Item");

				ItemList displaylistSession = null;
				ItemList displaylist = null;

				try
				{
					displaylistSession = ( ItemList )session.getAttribute( "displaylist") ;
				}
				catch( Exception e )
				{
					displaylistSession = null;
				}
				try
				{
					displaylist = ( ItemList )request.getAttribute( "displaylist") ;//gets the list from request
				}
				catch( Exception e )
				{
					displaylist = null;
				}


	      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
				ItemList DL = null ;

				if( displaylist == null  )
				{
					int records = listpreference.getRecordsPerPage();//gets the initial record per page to be displayed from listPreference
					String sortelement = listpreference.getSortElement();//gets the initial sort element from listPreference
					DL = (ItemList )lg.getItemList( individualID , 1, records , "" ,sortelement );//called when the request for the list is for first time
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

					if (( (displaylistSession.getListID() == displaylist.getListID() ) &&
								( displaylist.getDirtyFlag() == false ) &&
								( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) &&
								( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) &&
								(displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) &&
								(displaylist.getSortType()==(displaylistSession.getSortType()) &&
								(searchSession.equals(searchrequest))  ) ) ||
								displaylist.getAdvanceSearchFlag() == true)
					{
						DL = (ItemList) displaylistSession;
					}
					else
					{
						DL = ( ItemList )lg.getItemList( individualID , displaylist );
					}
					DL = setLinksfunction( DL );
				}
				session.setAttribute( "displaylist" , DL );
				request.setAttribute("displaylist" , DL );

			}
			catch (Exception e)
			{
				e.printStackTrace();
				FORWARD_final = GLOBAL_FORWARD_failure;
			}
			return (mapping.findForward(FORWARD_final));
		}


    public ItemList setLinksfunction( ItemList DL )
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
    			sm = ( StringMember )ele.get("SKU" );
    			sm.setRequestURL("goTo('/centraview/DisplayEditItem.do?TYPEOFOPERATION=EDIT&rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");//sets the URL for this member

    		}catch(Exception e )
    		{
    			System.out.println( "ItemList:setLinksfunction"+ e );
    		}

    	}

    	return DL;

    }


}
