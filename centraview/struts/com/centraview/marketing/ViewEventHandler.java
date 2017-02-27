/*
 * $RCSfile: ViewEventHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:20 $ - $Author: mking_cv $
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
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.marketing.events.IndividualNotInvitedException;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class ViewEventHandler extends org.apache.struts.action.Action
{
	private static Logger logger = Logger.getLogger(ViewEventHandler.class);

	/**
	* Standard Struts Action class execute method, handles login requests for
	* the application. Either passes control forwarding to Home page, or
	* forwards to Login page with errors.
	*
	* @param mapping Struts ActionMapping passed from servlet
	* @param form Struts ActionForm passed in from servlet
	* @param request Struts HttpServletRequest passed in from servlet
	* @param response Struts HttpServletResponse passed in from servlet
	*
	* @return ActionForward: returns the ActionForward to the servlet controller
	*         to tell Struts where to pass control to.
	*
	* @throws IOException DOCUMENT ME!
	* @throws ServletException DOCUMENT ME!
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		
		String returnStatus = ".view.marketing.edit.event";
		try
		{
			HttpSession session = request.getSession(true);
			UserObject userObject = (UserObject)session.getAttribute("userobject");

			int individualId = userObject.getIndividualID();

			EventDetails eventDetails = new EventDetails();


			MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
			MarketingFacade remote = home.create();
			remote.setDataSource(dataSource);

			int eventid = 0;
			boolean duplicateEvent = false;

			if(request.getParameter(MarketingConstantKeys.TYPEOFOPERATION)!= null
			   && (request.getParameter(MarketingConstantKeys.TYPEOFOPERATION)).equals("DUPLICATE")){
			   	duplicateEvent = true;
			}
			
			DynaActionForm dynaForm = (DynaActionForm)form;

			String eventID = (String)dynaForm.get("eventid");
			if (eventID != null && !eventID.equals(""))
			{
				eventid = Integer.parseInt(eventID);
			}
			eventID = (String)request.getParameter("eventid");
			if (eventid == 0 &&  eventID != null && !eventID.equals(""))
			{
				eventid = Integer.parseInt(eventID);
			} //end of if statement  (eventid == 0)
			eventID = (String)request.getAttribute("eventid");
			if (eventid == 0 &&  eventID != null && !eventID.equals(""))
			{
				eventid = Integer.parseInt(eventID);
			}
			request.setAttribute("eventid",eventid+"");

			eventDetails = remote.getEventDetails(individualId, eventid);
			eventDetails.setEventid(eventid);
			dynaForm.set("eventid", "" + eventid);
			dynaForm.set("name", eventDetails.getName());
			dynaForm.set("description", eventDetails.getDetail());
			dynaForm.set("whoshouldattend", eventDetails.getWhoshouldattend());
			dynaForm.set("maxattendees", "" + eventDetails.getMaxattendees());
			dynaForm.set("moderatorid", "" + eventDetails.getModeratorid());
			dynaForm.set("moderatorname", eventDetails.getModeratorname());

			Calendar calendar = new GregorianCalendar();

			if (eventDetails.getStartdate() != null)
			{
				Timestamp date = (Timestamp)eventDetails.getStartdate();
				calendar.setTimeInMillis(date.getTime());

				int[] hhmm = new int[2];
				hhmm[0] = calendar.get(Calendar.HOUR_OF_DAY);
				hhmm[1] = calendar.get(Calendar.MINUTE);

				String strTime = CVUtility.convertTime24HrsFormatToStr(hhmm);
				dynaForm.set("startmonth", Integer.toString(calendar.get(Calendar.MONTH) + 1));
				dynaForm.set("startday", Integer.toString(calendar.get(Calendar.DATE)));
				dynaForm.set("startyear", Integer.toString(calendar.get(Calendar.YEAR)));
				dynaForm.set("starttime", strTime);
			}

			if (eventDetails.getEnddate() != null)
			{
				Timestamp date = (Timestamp)eventDetails.getEnddate();
				calendar.setTimeInMillis(date.getTime());

				int[] hhmm = new int[2];
				hhmm[0] = calendar.get(Calendar.HOUR_OF_DAY);
				hhmm[1] = calendar.get(Calendar.MINUTE);

				String strTime = CVUtility.convertTime24HrsFormatToStr(hhmm);
				dynaForm.set("endmonth", Integer.toString(calendar.get(Calendar.MONTH) + 1));
				dynaForm.set("endday", Integer.toString(calendar.get(Calendar.DATE)));
				dynaForm.set("endyear", Integer.toString(calendar.get(Calendar.YEAR)));
				dynaForm.set("endtime", strTime);
			}

			if (eventDetails.getCreateddate() != null)
			{
				Timestamp date = (Timestamp)eventDetails.getCreateddate();
				calendar.setTimeInMillis(date.getTime());

				String month = getCalenderMonth(calendar.get(Calendar.MONTH) + 1);

				dynaForm.set("createddate", month + " " + Integer.toString(calendar.get(Calendar.DATE)) + ", " + Integer.toString(calendar.get(Calendar.YEAR)));
			}

			if (eventDetails.getModifieddate() != null)
			{
				Timestamp date = (Timestamp)eventDetails.getModifieddate();

				calendar.setTimeInMillis(date.getTime());

				String month = getCalenderMonth(calendar.get(Calendar.MONTH) + 1);

				dynaForm.set("modifieddate", month + " " + Integer.toString(calendar.get(Calendar.DATE)) + ", " + Integer.toString(calendar.get(Calendar.YEAR)));
			}

        	if (!duplicateEvent)
        	{
				try
				{
					boolean accepted = remote.hasUserAcceptedEvent(eventid, individualId);
					request.setAttribute("showRegisterButton", new Boolean(true));
					request.setAttribute("hasAccepted", new Boolean(accepted));
				}
				catch (IndividualNotInvitedException inie)
				{
					request.setAttribute("showRegisterButton", new Boolean(false));
					request.setAttribute("hasAccepted", new Boolean(false));
				}
				this.buildSubList(eventDetails.getEventid(), userObject, request, session, dataSource);
			}
			//End of Get whether this user is registered or not

			if (duplicateEvent)
			{
				dynaForm.set("eventid", "");
				returnStatus = ".view.marketing.new.event";
				EventAtendeesList dl = new EventAtendeesList();
				dl.setStartAT(1);
				dl.setEndAT(100);
				dl.setSortType('A');
				dl.setListType("EventAtendees");
				
				request.setAttribute("displaylist", dl);
				session.setAttribute("displaylist", dl);				
			} //end of if statement (duplicateEvent)

			ArrayList attachedFilesList = eventDetails.getAttachedFiles();
			request.setAttribute("attachmentFiles", attachedFilesList);

			
			session.removeAttribute("AttachfileList");
			request.setAttribute("TypeOfOperation", "Event");
		} //end of try block
		catch (Exception e)
		{
			logger.error("[execute] ViewEventHandler execute thrown.", e);
		} //end of catch block (Exception)
		
		return (mapping.findForward(returnStatus));
	} //end of execute method

	/*
	* This method is used to quickly return the proper name of a month
	*/
	public String getCalenderMonth(int monthNumber)
	{
		String strReturn = "";

		switch (monthNumber)
		{
			case 1 :
				strReturn = "January";
				break;

			case 2 :
				strReturn = "February";
				break;

			case 3 :
				strReturn = "March";
				break;

			case 4 :
				strReturn = "April";
				break;

			case 5 :
				strReturn = "May";
				break;

			case 6 :
				strReturn = "June";
				break;

			case 7 :
				strReturn = "July";
				break;

			case 8 :
				strReturn = "August";
				break;

			case 9 :
				strReturn = "September";
				break;

			case 10 :
				strReturn = "October";
				break;

			case 11 :
				strReturn = "November";
				break;

			case 12 :
				strReturn = "December";
				break;
		}

		return strReturn;
	}

	/**
	* Builds the Sub List for the License Admin Detail Page.
	*
	* @param licenseID The licenseID to get the history for.
	* @param request So we can get some information about the request.
	*
	* @throws Exception Make the calling method deal
	*  with any exceptions thrown here.
	*/
	private void buildSubList(int eventID, UserObject userObject, HttpServletRequest request, HttpSession session, String dataSource) throws Exception
	{
	    long start = 0L;
	    if (logger.isDebugEnabled()) {
	      start = System.currentTimeMillis();
	    }
	    int individualId = userObject.getIndividualID();
	    ListPreference listPreference = userObject.getListPreference("EventAtendees");
	    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
	    ValueListParameters listParameters = null;
	    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
	    if (requestListParameters == null) // build up new Parameters
	    {
	      listParameters = new ValueListParameters(ValueListConstants.EVENTATTENDEE_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
	    } else { // paging or sorting or something, use the parameters from the
	             // request.
	      listParameters = requestListParameters;
	    }
	    if (listParameters.getSortColumn() == 0) {
	      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.eventAttendeeViewMap.get(listPreference.getSortElement());
	      listParameters.setSortColumn(sortField.getQueryIndex());
	      if (listPreference.getsortOrder()) {
	        listParameters.setSortDirection("ASC");
	      } else {
	        listParameters.setSortDirection("DESC");
	      }
	    }

	    String filter = null;
	    String filterParameter = request.getParameter("filter");
	    if (filterParameter != null) {
	    	filter = " SELECT * FROM eventregister where EventID = " + eventID;
	    	request.setAttribute("appliedSearch", filterParameter);
	    } else {
	    	filter = " SELECT * FROM eventregister where EventID = " + eventID;
	    	session.removeAttribute("listFilter");
	    }
	    listParameters.setFilter(filter);
	    
	    // TODO remove crappy map between old views and new views.
	    Vector viewColumns = view.getColumns();
	    ArrayList columns = new ArrayList();
	    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.EVENTATTENDEE_LIST_TYPE);
	    listParameters.setColumns(columns);
	    // Get the list!
	    ValueListHome valueListHome = (ValueListHome)CVUtility.getHomeObject("com.centraview.valuelist.ValueListHome", "ValueList");
	    ValueList valueList = null;
	    try {
	      valueList = valueListHome.create();
	    } catch (CreateException e) {
	      logger.error("[execute] Exception thrown.", e);
	      throw new ServletException(e);
	    }

	    valueList.setDataSource(dataSource);
	    listParameters.setExtraId(eventID);
	    ValueListVO listObject = valueList.getValueList(individualId, listParameters);
	    listObject.setCurrentPageParameters(ValueListConstants.AMP+"eventid="+eventID);
	    ArrayList buttonList = new ArrayList();

	    ValueListDisplay displayParameters = null;
	 	listObject.setMoveTo(false);
	   	buttonList.add(new Button("Add Attendees", "Add Attendees", "vl_addMembers();", false));
		buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
		displayParameters = new ValueListDisplay(buttonList, true, true);

	    displayParameters.setSortable(true);
	    displayParameters.setPagingBar(true);
	    displayParameters.setLink(true);
	    listObject.setDisplay(displayParameters);
	    // Stick the list on the request and then the custom list tag will handle
	    // it.
	    if (logger.isDebugEnabled()) {
	      long debugTime = (System.currentTimeMillis() - start);
	      logger.debug("[execute] End to End: " + debugTime + " ms");
	      listObject.getParameters().setDebugTime(debugTime);
	    }
	    request.setAttribute("valueList", listObject);
	    request.setAttribute("listType", "EventAtendees");
	} //end of buildSubList method
} //end of ViewEventHandler class
