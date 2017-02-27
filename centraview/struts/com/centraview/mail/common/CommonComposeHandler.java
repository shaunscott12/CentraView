/*
 * $RCSfile: CommonComposeHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/28 14:32:20 $ - $Author: mking_cv $
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

package com.centraview.mail.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class CommonComposeHandler extends Action
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
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();


    String forward = ".view.email.commoncompose";
		try
		{
      HttpSession session = request.getSession(true);
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

	    DynaActionForm emailForm = (DynaActionForm)form;

      // figure out composition settings
      UserPrefererences userPref= userObject.getUserPref();
      boolean composeInHTML = false;
      if ((userPref.getContentType()).equals("HTML")) {
        composeInHTML = true;
      }
      emailForm.set("composeInHTML", new Boolean(composeInHTML));

			// Parameter type is to categorized that this compose handler is for particular module
			// and According to selection we will popolate the MailFormbean
			String type = request.getParameter("type");

			// We will collect the to Information from all the Attendees associated to the Event.
			if (type != null && type.equals("EVENT")){
				String eventID = (String) request.getParameter("eventid");
				if(eventID != null && !eventID.equals("")){
					MarketingFacadeHome cfh = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
					MarketingFacade remote = (MarketingFacade)cfh.create();
					remote.setDataSource(dataSource);
					int eventId = Integer.parseInt(eventID);
					String toList = remote.getEventAttendeesForMail(eventId, individualID);
					emailForm.set("to", toList);
					emailForm.set("subject", "EVENT");
				}// end of if(eventID != null && !eventID.equals(""))
			}// end of if (type != null && type.equals("EVENT"))

		}
		catch (Exception e)
		{
			System.out.println("[Exception][CommonComposeHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
		}// end of catch Block
		return (mapping.findForward(forward));
	}// end of excute method
}// end of class
