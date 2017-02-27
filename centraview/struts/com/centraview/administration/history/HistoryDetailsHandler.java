/*
 * $RCSfile: HistoryDetailsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:42 $ - $Author: mking_cv $
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
 * HistoryDetailsHandler.java
 *  
 * Handles list action for display of HistoryList.
 * Creation date: 11th Dec 2003
 * @author: Pravin Kadam
 */

package com.centraview.administration.history;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class HistoryDetailsHandler extends org.apache.struts.action.Action {

	private static String FORWARD_final = "failure";

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
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		
		
		try {
			
			HttpSession session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "admin");//Highlights the admin link on header.jsp
				
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();

			String action= request.getParameter("action");
			int recordID = Integer.parseInt(request.getParameter("rowId"));
			
			HistoryHome historyHome = (HistoryHome)CVUtility.getHomeObject("com.centraview.administration.history.HistoryHome","History");
			History remoteHistory = historyHome.create(); 
			remoteHistory.setDataSource(dataSource);
			//If record is deleted  
			if(remoteHistory.isDeletedRecord(action,recordID,AdministrationConstantKeys.DELETED,AdministrationConstantKeys.RESTORED)){
				FORWARD_final = "recordDeleted";
			}else if(action.equals(AdministrationConstantKeys.INDIVIDUAL)){
				//Forward the page to Individual View if record is of Individual
				FORWARD_final = "viewindividual";	
			}else if(action.equals(AdministrationConstantKeys.ENTITY)){
				//Forward the page to Entity View if record is of Entity
				FORWARD_final = "viewentity";	
			} else {
				//IFf nothing is specified forword request to list again stating record is not available
				FORWARD_final = "viewlist";
			}

		}catch(Exception e) {
			System.out.println("[Exception][HistoryDetailsHandler.execute] Exception Thrown: "+e);
			FORWARD_final = "failure";
			e.printStackTrace();		
		}
		return ( mapping.findForward(FORWARD_final) );
	}
}
