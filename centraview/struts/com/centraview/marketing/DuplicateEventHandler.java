/*
 * $RCSfile: DuplicateEventHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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

package com.centraview.marketing;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

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
import com.centraview.email.Attachment;
import com.centraview.email.MailMessage;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

/**
* This class handles the duplication of a Marketing Event.
*
* @author Ryan Grier <ryan@centraview.com>
*/
public class DuplicateEventHandler extends Action
{

	/**
	* Standard Struts Action class execute method, handles login requests
	* for the application. Either passes control forwarding to Home page,
	* or forwards to Login page with errors.
	*
	* @param mapping   Struts ActionMapping passed from servlet
	* @param form      Struts ActionForm passed in from servlet
	* @param request   Struts HttpServletRequest passed in from servlet
	* @param response  Struts HttpServletResponse passed in from servlet
	*
	* @return  ActionForward: returns the ActionForward to the servlet controller to tell
	*  Struts where to pass control to.
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String returnStatus = "failure";
		String rowID = null;
		int newEventID = 0;
		try
		{
			HttpSession session = request.getSession();
			int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

			rowID = request.getParameter("eventid");
			if (request.getParameter("eventid") == null)
			{
				rowID = (String)request.getAttribute("eventid");
			} //end of if statement (request.getParameter("eventid") == null)

			if (rowID != null && !rowID.equals(""))
			{
				int eventID = Integer.parseInt(rowID);

				MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
				MarketingFacade remote = home.create();
				remote.setDataSource(dataSource);
				EventDetails oldEvent = remote.getEventDetails(individualId, eventID);

				if (oldEvent != null)
				{
					HashMap newEventHashMap = new HashMap();
					newEventHashMap.put("Creator", Integer.toString(individualId));
					newEventHashMap.put("Name", oldEvent.getName());
					newEventHashMap.put("Description", oldEvent.getDetail());
					newEventHashMap.put("WhoShouldAttend", oldEvent.getWhoshouldattend());
					newEventHashMap.put("MaxAttendees", Integer.toString(oldEvent.getMaxattendees()));
					newEventHashMap.put("Moderator", Integer.toString(oldEvent.getModeratorid()));
					//newEventHashMap.put("StartDate", (Timestamp) oldEvent.getStartdate());
					//newEventHashMap.put("EndDate", (Timestamp) oldEvent.getEnddate());
					Timestamp startTimestamp = (Timestamp)oldEvent.getStartdate();
					Timestamp endTimestamp = (Timestamp)oldEvent.getEnddate();

					//This section SHOULD NOT NEED TO BE DONE.
					//The way Timestamps are currently handled means I have to do this this way.
					Calendar tempCalendar = new GregorianCalendar();
					//fix the start time
					tempCalendar.setTimeInMillis(startTimestamp.getTime());
					tempCalendar.add(Calendar.YEAR, 1900);
					startTimestamp.setTime(tempCalendar.getTimeInMillis());

					//fix the end time
					tempCalendar.setTimeInMillis(endTimestamp.getTime());
					tempCalendar.add(Calendar.YEAR, 1900);
					endTimestamp.setTime(tempCalendar.getTimeInMillis());
					//End of This section SHOULD NOT NEED TO BE DONE.

					newEventHashMap.put("StartDate", startTimestamp);
					newEventHashMap.put("EndDate", endTimestamp);

					//Add the attachment here
					MailMessage mailmessage = null;
					EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
					EmailFacade facade = (EmailFacade)cfh.create();
					HashMap hm = new HashMap();
					hm.put("MessageID", new Integer(eventID));

					mailmessage = facade.getAttachment(individualId, hm);

					ArrayList attachments = mailmessage.getAttachmentID();
					HashMap newAttachments = new HashMap();
					if (attachments != null)
					{
						Iterator attachmentIterator = attachments.iterator();
						while (attachmentIterator.hasNext())
						{
							Attachment attachment = (Attachment)attachmentIterator.next();
							newAttachments.put(Integer.toString(attachment.getFileID()), attachment.getFilename());
						} //end of while loop (attachmentIterator.hasNext())
					} //end of if statement (attachments != null)

					newEventHashMap.put("Attachment", newAttachments);

					newEventID = remote.addEvent(newEventHashMap, individualId);
				} //end of if statement (eventDetails != null)

				Collection oldEventAttendees = remote.getAttendeesForEvent(individualId, eventID);
				if (oldEventAttendees != null)
				{
					HashMap map = new HashMap();
					Iterator iterator = oldEventAttendees.iterator();
					String[] individualIDs = new String[oldEventAttendees.size()];

					map.put("EventId", Integer.toString(newEventID));
					map.put("Accepted", "NO");
					int i = 0;

					while (iterator.hasNext())
					{
						EventAttendeeVO eventAttendeeVO = (EventAttendeeVO)iterator.next();
						individualIDs[i] = Integer.toString(eventAttendeeVO.getIndividualID());
						i++;
					} //end of while loop (iterator.hasNext())
					map.put("IndividualId", individualIDs);

					remote.addEventRegister(map, individualId);

				} //end of if statement (oldEventAttendees != null)

				request.setAttribute("eventid", Integer.toString(newEventID));

				request.setAttribute("duplicateEvent", "YES");
				// Copy over attachments.
				returnStatus = ".view.marketing.edit.event";
			} //end of if statement (rowID != null && !rowID.equals(""))
			//do some stuff
		} //end of try block
		catch (Exception ex)
		{
			System.out.println("[Exception] DuplicateEventHandler.execute: " + ex.toString());
			ex.printStackTrace();
			//catch some stuff
		} //end of catch block (Exception)
		return mapping.findForward(returnStatus);
	} //end of execute method
} //end of DuplicateEventHandler class
