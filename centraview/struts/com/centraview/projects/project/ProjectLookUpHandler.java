/*
 * $RCSfile: ProjectLookUpHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:29 $ - $Author: mking_cv $
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
package com.centraview.projects.project;

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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.ProjectList;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * this class will get http request from new_timeslip_C JSP then do the
 * corresponding get data from database.
 */

public final class ProjectLookUpHandler extends Action
{

	private static Logger logger = Logger.getLogger(ProjectLookUpHandler.class);	
	
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
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws NamingException, CommunicationException  
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String returnStatus ="";
		HttpSession session = req.getSession();
		UserObject userObject = (UserObject)session.getAttribute("userobject");
		int userId = userObject.getIndividualID();

		ListPreference listpreference = userObject.getListPreference("Project");
		int recordsPerPage = listpreference.getRecordsPerPage();

		ListGenerator lg = ListGenerator.getListGenerator(dataSource);
		ProjectList DL = ( ProjectList )lg.getProjectList(userId , 0,0 , "" ,"Name");
		DL.setTotalNoOfRecords( DL.size() );

		String searchStr = req.getParameter("simpleSearch");
		String saveSearchStr = req.getParameter("savedSearch");

		if(searchStr != null && (searchStr.trim()).length() > 0)
		{
			searchStr = searchStr.trim();
			DL.setSearchString("SIMPLE :"+searchStr);
			DL.search();
		}

		DL.setTotalNoOfRecords(DL.size());
		DL.setStartAT(1);
		DL.setEndAT(recordsPerPage);
		DL.setRecordsPerPage(recordsPerPage);

		Set listkey = DL.keySet();
		Iterator it =  listkey.iterator();
		while( it.hasNext() )
		{
			String str = ( String )it.next();
			StringMember sm=null;
			ListElement ele  = ( ListElement )DL.get( str );
			sm = ( StringMember )ele.get("Name" );
			sm.setRequestURL("openPopup('ViewProjectDetail.do?rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");

			sm = ( StringMember )ele.get("Entity" );
			IntMember im = (IntMember) ele.get("EntityID");
			sm.setRequestURL("openPopup('ViewEntityDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
		}

		req.setAttribute("projectlookuplist" , DL );
		session.setAttribute("projectlookuplist", DL);
		req.setAttribute("lookupListName", "projectlookuplist");
		req.setAttribute("list" , "Project" );
		returnStatus = "successProject";
		return mapping.findForward(returnStatus);
	}
}

