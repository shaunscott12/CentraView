/*
 * $RCSfile: UpdateActivityStatusHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;


/**
 * This handler is used for Updating Rule.
 * 
 * @author Vivek T
 */
public final class UpdateActivityStatusHandler extends Action
{


	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	throws IOException, ServletException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String forwardTo = "failure";
		try
		{
			request.setAttribute("refreshWindow", "true");
			HttpSession session = request.getSession(true);
			
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
			String rowId = (String)request.getParameter("rowId");
			request.setAttribute("rowId",rowId);
			String index = (String)request.getParameter("index");
			request.setAttribute("index",index);
			String folderid = (String)request.getParameter("folderid");
			request.setAttribute("folderid",folderid);
			String listId = (String)request.getParameter("listId");
			request.setAttribute("listId",listId);
			int individualID = userobjectd.getIndividualID();
			int messageId = Integer.parseInt(request.getParameter("MessageID"));
		
			int activityId = Integer.parseInt(request.getParameter("activityId"));
	
			String activityStatus = request.getParameter("status");
			//System.out.println("activityStatus"+activityStatus);
			updateStatus(individualID, messageId, activityId, activityStatus, individualID);
			request.setAttribute("header",activityStatus);
			forwardTo = "displayemailmeaasge";		
		}
		catch(Exception exe)
		{
			forwardTo  = "failure";
      System.out.println("[Exception] UpdateActivityStatusHandler.execute: " + exe.toString());
			//exe.printStackTrace();
		}
		return (mapping.findForward(forwardTo));
	}

	public void updateStatus(int userId,int messageId,int activityId,String status,int individualId)
	{
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		try
		{
			//System.out.println("Email : updateStatusHandler");
			EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome","EmailFacade");
			EmailFacade remote =( EmailFacade )cfh.create();
      remote.setDataSource(dataSource);
			String headerName = Constants.EH_ATTENDEE_STATUS_KEY;
			String headerValue = status;
			//ejb call to update header	
			remote.updateHeader(userId,messageId,headerName,headerValue);
			remote = null;
			//ejb call to update activity
			ActivityFacadeHome afh = (ActivityFacadeHome)CVUtility.getHomeObject("com.centraview.activity.activityfacade.ActivityFacadeHome","ActivityFacade");
			ActivityFacade af = afh.create();
      af.setDataSource(dataSource);
			af.updateStatus(activityId,individualId,headerValue);
		}
		catch(Exception e)
		{
      System.out.println("[Exception] UpdateActivityStatusHandler.updateStatus: " + e.toString());
			//e.printStackTrace();
		}
	}
}
