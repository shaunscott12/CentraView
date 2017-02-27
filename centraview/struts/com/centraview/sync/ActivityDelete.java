/*
 * $RCSfile: ActivityDelete.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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


package com.centraview.sync;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
This class for Deleting Activity from Database.
*/

public class ActivityDelete extends Action {

  /**
  This is overridden method form Action class
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException,CommunicationException, NamingException {

		// let's test printing directly to stdout
		response.setContentType("text/plain"); // or text/xml
		PrintWriter writer = response.getWriter();
		
		HttpSession session = request.getSession();
		String sessionID = session.getId();
		ActivityDeleteHandler requestForm = (ActivityDeleteHandler) form;
		UserObject userobject = ( UserObject ) session.getAttribute("userobject" );
		int individualID = userobject.getIndividualID();
		
		String activityID = requestForm.getActivityID(); //request.getParameter( "activityid" );
		String sessionid = requestForm.getSessionID(); //request.getParameter( "sessionid" );

		//session facade object created
		SyncFacade syncfacade = new SyncFacade();
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		syncfacade.setDataSource(dataSource);
		boolean adminstratorUserFlag = false;
		String userType = userobject.getUserType();
		
		if(userType != null && userType.equalsIgnoreCase("ADMINISTRATOR")){
			adminstratorUserFlag = true;
		}
		
		// check session matching
		if (userobject.getSessionID().equals(sessionid))
		{
				String result = syncfacade.deleteActivity(individualID , activityID, adminstratorUserFlag);
				writer.print(result);
		} //end of if statement
		else
		{
			System.out.println("[Sync] Sync failed because sessionID is not valid");
			writer.print("FAIL");
		} //end of else statement
		
		return(null);
  }   // end execute()

}   // end class Login definition

