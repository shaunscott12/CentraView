/*
 * $RCSfile: EventListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:13 $ - $Author: mking_cv $
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
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;


/**
 * Handles list action for display of Event list.
 * Creation date: 28 Aug 2003
 * @author: Linesh
 */
public class EventListHandler extends Action
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
   * @return  ActionForward: returns the ActionForward to the servlet controller to tell Struts where to pass control to.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String returnStatus = "failure";
    //System.out.println("[DEBUG] [EventListHandler]: enter");
    boolean createdNewList = false;

    try
    {
      HttpSession session = request.getSession(true);
      com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject) session.getAttribute(
          "userobject");
      int individualID = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("Event");

      //System.out.println("[DEBUG] [EventListHandler]: listpreference " +
        //listpreference);

      EventList displaylistSession = null;
      EventList displaylist = null;

	  // After performing the logic in the DeleteHanlder, we are generat a new request for the list
	  // So we will not be carrying the old error. So that we will try to collect the error from the Session variable
	  // Then destory it after getting the Session value
	  if (session.getAttribute("listErrorMessage") != null)
	  {
		  ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
		  saveErrors(request, allErrors);
		  session.removeAttribute("listErrorMessage");
	  }//end of if (session.getAttribute("listErrorMessage") != null)
	  
      try
      {
        displaylistSession = (EventList) session.getAttribute("displaylist");
      }
      catch (ClassCastException e)
      {
        displaylistSession = null;
        //System.out.println("[DEBUG] [EventListHandler]: ClassCastException in displaylistSession.");
      }

      try
      {
        displaylist = (EventList) request.getAttribute("displaylist");
      }
      catch (ClassCastException e)
      {
        displaylist = null;
        //System.out.println("[DEBUG] [EventListHandler]: ClassCastException in displaylist.");
      }

      EventList DL = null;

      if (displaylist == null)
      {
        //System.out.println("[DEBUG] [EventListHandler]: displaylist was null.");
        com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
        int records = listpreference.getRecordsPerPage();
        String sortelement = listpreference.getSortElement();
        DL = (EventList) lg.getEventList(individualID, 1, records, "", sortelement);

        //System.out.println("[DEBUG] [EventListHandler]: List Size " + DL.size());
        createdNewList = true;
      }
      else
      {
        //System.out.println("[DEBUG] [EventListHandler]: displaylist was NOT null.");
        String searchSession = displaylistSession.getSearchString();
        String searchrequest = displaylist.getSearchString();

        if (searchSession == null)
        {
          searchSession = "";
        }

        if (searchrequest == null)
        {
          searchrequest = "";
        }

        if (((displaylistSession.getListID() == displaylist.getListID()) &&
            (displaylist.getDirtyFlag() == false) &&
            (displaylist.getStartAT() >= displaylistSession.getBeginIndex()) &&
            (displaylist.getEndAT() <= displaylistSession.getEndIndex()) &&
            (displaylist.getSortMember().equals(displaylistSession.getSortMember())) &&
            (displaylist.getSortType() == (displaylistSession.getSortType())) &&
            (searchSession.equals(searchrequest)))|| displaylist.getAdvanceSearchFlag() == true)
        {
          //System.out.println("[DEBUG] [EventListHandler]: displaylist == displaylistSession.");
          DL = ( EventList )displaylistSession;
          request.setAttribute("displaylist", displaylistSession);
          createdNewList = true;
        }
        else
        {
          //System.out.println("[DEBUG] [EventListHandler]: displaylist != displaylistSession.");
          ListGenerator lg = ListGenerator.getListGenerator(dataSource);
          DL = (EventList) lg.getEventList(individualID, displaylist);
          createdNewList = true;
        }
      }

      if (createdNewList)
      {
        //System.out.println("[DEBUG] [EventListHandler]: DL value: " + DL);

        DL = setLinksfunction(DL);
        session.setAttribute("displaylist", DL);
        request.setAttribute("displaylist", DL);
        request.setAttribute("list", "Event");
      } //end of if statement (createdNewList)

      request.setAttribute("TypeOfOperation", "Event");
      returnStatus= "showeventlist";
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception]: EventListHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    return (mapping.findForward(returnStatus));
  }

  /**
  @   uses
    This function sets links on members
  */
  public EventList setLinksfunction(EventList DL)
  {
    //code to set the url for required cols
    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();

    while (it.hasNext())
    {
      try
      {
        String str = (String) it.next();
        //System.out.println("[DEBUG] [EventListHandler]: " + str);

        ListElement ele = (ListElement) DL.get(str);
        StringMember sm = (StringMember) ele.get("title");
        IntMember im = (IntMember) ele.get("eventid");
        int eventID = ((Integer) im.getMemberValue()).intValue();

        sm.setRequestURL(
          "goTo('ViewEvent.do?TYPEOFOPERATION=EDIT&FromAttendee=NO&eventid=" +
          ((Integer) im.getMemberValue()).intValue() + "')");

        sm = (StringMember) ele.get("ownername");
        im = (IntMember) ele.get("ownerid");
        sm.setRequestURL("openPopup('ViewIndividualDetail.do?rowId=" +
          ((Integer) im.getMemberValue()).intValue() + "')");
      }
      catch (Exception e)
      {
        System.out.println("[Exception]: EventListHandler.setLinksfunction: " + e.toString());
        //e.printStackTrace();
      }
    }

    return DL;
  }
}
