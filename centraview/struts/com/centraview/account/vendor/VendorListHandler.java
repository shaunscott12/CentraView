/*
 * $RCSfile: VendorListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:26 $ - $Author: mking_cv $
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
package com.centraview.account.vendor;

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
import com.centraview.common.DisplayList;
import com.centraview.common.EntityList;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;

public class VendorListHandler extends org.apache.struts.action.Action {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_showvendorlist = "showvendorlist";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
		public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

			try {

				HttpSession session = request.getSession(true);
				session.setAttribute("highlightmodule", "account");

				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.VENDOR);
				request.setAttribute("body", "list");

				com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
				ListPreference listpreference= userobjectd.getListPreference("Entity");

				// After performing the logic in the DeleteHanlder, we are generat a new request for the list
				// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
				// Then destory it after getting the Session value
				if (session.getAttribute("listErrorMessage") != null)
				{
					ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
					saveErrors(request, allErrors);
					session.removeAttribute("listErrorMessage");
				}//end of if (session.getAttribute("listErrorMessage") != null)

				DisplayList displaylistSession = ( DisplayList )session.getAttribute( "displaylist") ;

				DisplayList displaylist = ( DisplayList )request.getAttribute( "displaylist") ;

				EntityList DL = new EntityList();
				ListGenerator lg = ListGenerator.getListGenerator(dataSource);
				if( displaylist == null  )
				{

					String searchString = "ADVANCE:SELECT entityid FROM vendor  ";
					DL = ( EntityList )lg.getEntityList( userobjectd.getIndividualID(), 1,listpreference.getRecordsPerPage() , searchString ,listpreference.getSortElement());
					if (DL == null)
					{
						DL = new EntityList();
					}

					DisplayList DL1 = lg.getDisplayList(DL.getListID() );
					DL1.setListType("Vendor");
					DL.setListType("Vendor");
					DL.setPrimaryTable("vendor");
					DL1.setTotalNoOfRecords(DL.size());
					DL.setTotalNoOfRecords(DL.size());
				}
				else
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
						DL = (EntityList)displaylistSession;
						DL.setListType("Vendor");
						DL.setPrimaryTable("vendor");
						DL.setTotalNoOfRecords(DL.size());
						request.setAttribute("displaylist" , displaylistSession );
					}
					else
					{
						DL = ( EntityList )lg.getEntityList( displaylist );
						DisplayList DL1 = lg.getDisplayList(DL.getListID() );
						DL1.setListType("Vendor");
						DL.setListType("Vendor");
						DL.setPrimaryTable("vendor");
						DL1.setTotalNoOfRecords(DL.size());
						DL.setTotalNoOfRecords(DL.size());
					}
				}


				Set listkey = DL.keySet();
				Iterator it =  listkey.iterator();
				while( it.hasNext() )
				{
					String str = ( String )it.next();

					StringMember sm=null;
					ListElement ele  = ( ListElement )DL.get( str );
					sm = ( StringMember )ele.get("Name" );
					sm.setRequestURL("openPopup('ViewEntityDetail.do?rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");
					sm = ( StringMember )ele.get("PrimaryContact" );
					IntMember im = (IntMember) ele.get("IndividualID");
					sm.setRequestURL("openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
					sm = ( StringMember )ele.get("Website");
					sm.setRequestURL("openWindowWithTool('"+sm.getDisplayString()+"')");
					sm = ( StringMember )ele.get("Email" );
					sm.setRequestURL("openWindows('mail/Compose.do?to="+sm.getDisplayString()+"',720,585,'')");
				}

				session.setAttribute( "displaylist" , DL );
				request.setAttribute("displaylist" , DL );

				// forward to jsp page
				FORWARD_final = FORWARD_showvendorlist;

			} catch (Exception e)
			{
				e.printStackTrace();
				FORWARD_final = GLOBAL_FORWARD_failure;
			}
			return (mapping.findForward(FORWARD_final));
		}
}
