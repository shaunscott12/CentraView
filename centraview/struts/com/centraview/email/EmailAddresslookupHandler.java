/*
 * $RCSfile: EmailAddresslookupHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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

package com.centraview.email;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.IndividualList;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
* this class will get http request from AddIndividual JSP then do
* the corresponding get data from database.
*/

public final class EmailAddresslookupHandler extends Action
{



	/**
	* Process the specified HTTP request, and create the corresponding HTTP
	* response (or forward to another web component that will create it).
	* Return an <code>ActionForward</code> instance describing where and how
	* control should be forwarded, or <code>null</code> if the response has
	* already been completed.
	*
	* @param mapping The ActionMapping used to select this instance
	* @param form The optional ActionForm bean for this request (if any)
	* @param req The HTTP request we are processing
	* @param res The HTTP response we are creating
	*
	* @exception IOException if an input/output error occurs
	* @exception ServletException if a servlet exception occurs
	*/
	public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest req,
	HttpServletResponse res)
	throws IOException, ServletException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String returnStatus ="";
		req.setAttribute("idsTo",req.getParameter("idsTo"));
		req.setAttribute("idsCc",req.getParameter("idsCc"));
		req.setAttribute("idsBcc",req.getParameter("idsBcc"));
		HttpSession session = req.getSession();
		UserObject userObject = (UserObject)session.getAttribute("userobject");
		int individualID = userObject.getIndividualID();
		int currentPage = 1;
		try
		{

			ContactFacadeHome contactFacadeHome = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
			ContactFacade remote = contactFacadeHome.create();
			remote.setDataSource(dataSource);
			int listID = 1;

			if (req.getParameter("dbid") != null) {
				listID = Integer.parseInt(req.getParameter("dbid"));
			}
			else if (session.getAttribute("dbid") != null) {
				listID = Integer.parseInt((String)session.getAttribute("dbid"));
			}

			Vector allDBList = remote.getDBList(individualID);
			req.setAttribute("AllDBList", allDBList);
			session.setAttribute("dbid", "" + listID);
			req.setAttribute("dbid", "" + listID);

			ListPreference listpreference = userObject.getListPreference("Email");
			int recordsPerPage = 100;

			if (req.getParameter("currentPage") != null)
			{
				currentPage = Integer.parseInt(req.getParameter("currentPage"));
			} //end of if statement (req.getParameter("currentPage") != null)
			else if (req.getAttribute("currentPage") != null)
			{
				currentPage = Integer.parseInt((String)req.getAttribute("currentPage"));
			} //end of else if statement (req.getAttribute("currentPage") != null)


			String searchStr = req.getParameter("search");
			ListGenerator lg = ListGenerator.getListGenerator(dataSource);
			IndividualList DL = ( IndividualList )lg.getIndividualAndEntityEmailList(individualID , 0,0 , "" ,"Name",listID);

			if ((searchStr != null) && ((searchStr.trim()).length() > 0))
			{
				searchStr = "SIMPLE: " + searchStr.trim();
				DL.setSearchString(searchStr);
				DL.search();
			}

			DL.setTotalNoOfRecords(DL.size());
			Set listkey = DL.keySet();
			Iterator it =  listkey.iterator();
			while( it.hasNext() )
			{
				String str = ( String )it.next();
				StringMember sm=null;
				ListElement ele  = ( ListElement )DL.get( str );
				sm = ( StringMember )ele.get("Name" );
				sm = ( StringMember )ele.get("Email" );
			}


			int startingAT = 1;
			int endingAt = recordsPerPage;

			if (currentPage != 1)
			{
				startingAT = currentPage  * recordsPerPage;
				endingAt = startingAT + recordsPerPage;
			}

			DL.setStartAT(startingAT);
			DL.setEndAT(endingAt);
			DL.setRecordsPerPage(recordsPerPage);

			req.setAttribute("emailAddresslookuplist" , DL );
			session.setAttribute("emailAddresslookuplist", DL);
			req.setAttribute("lookupListName", "emailAddresslookuplist");
			req.setAttribute("list" , "EmailAddress" );
			returnStatus = "displayemailaddress";

		}catch(Exception e)
		{
			returnStatus = "failure";
		}
		return mapping.findForward(returnStatus);
	}

}

