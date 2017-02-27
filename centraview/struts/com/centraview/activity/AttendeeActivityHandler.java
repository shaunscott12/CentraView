/*
 * $RCSfile: AttendeeActivityHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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

package com.centraview.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

public class AttendeeActivityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(AttendeeActivityHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_attendee = ".view.activities.attendees";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      // populate form bean for previous sub-activity
      PopulateForm populateForm = new PopulateForm();

      // set the form elements
      populateForm.setForm(request, form);

      // set form with respect to new opening page
      form = populateForm.getForm(request, form, ConstantKeys.ATTENDEE);

      ActivityForm af = (ActivityForm)form;
      Vector vecOptional = af.getActivityAttendeeOptionalVector();

      if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.APPOINTMENT)) {
        request.setAttribute("actionName", "title.contact.appointment");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.CALL)) {
        request.setAttribute("actionName", "title.contact.call");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.MEETING)) {
        request.setAttribute("actionName", "title.contact.meeting");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.NEXTACTION)) {
        request.setAttribute("actionName", "title.contact.nextaction");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TASK)) {
        request.setAttribute("actionName", "title.contact.task");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TODO)) {
        request.setAttribute("actionName", "title.contact.todo");
      }

      HttpSession session = request.getSession();

      int individualID = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      String searchIndividual = af.getActivityAttendeeSearch();
      AttendeeList gal = AttendeeList.getAttendeeList(individualID, "group", 0, dataSource);

      Vector individualList = new Vector();
      individualList.addElement(new DDNameValue("0#employee", "Employee"));
      individualList.addElement(new DDNameValue("0#allindividual", "All Individual"));

      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = cfh.create();
      remote.setDataSource(dataSource);
      Vector allDBList = remote.getDBList(individualID);
      if (allDBList != null) {
        for (int dbcount = 0; dbcount < allDBList.size(); dbcount++) {
          DDNameValue dbInfo = (DDNameValue)allDBList.get(dbcount);
          String id = dbInfo.getStrid();
          String name = dbInfo.getName();
          individualList.addElement(new DDNameValue(id + "#individual", name));
        }
      }
      request.setAttribute("individualList", individualList);

      DDNameValue dbIDName = null;
      String dbID = "0";
      String tempDBID = "0";
      String dbName = "0";
      if (individualList != null && individualList.size() > 0) {
        dbIDName = (DDNameValue)individualList.get(0);
      }

      if (request.getParameter("dbID") == null
          && ((ActivityForm)form).getActivityAttendeesType() == null) {
        tempDBID = dbIDName.getStrid();
        if (tempDBID != null && tempDBID.length() > 0) {
          dbID = tempDBID.substring(0, tempDBID.indexOf("#"));
          dbName = tempDBID.substring(tempDBID.indexOf("#") + 1, tempDBID.length());
        }
      } else if (((ActivityForm)form).getActivityAttendeesType() != null) {
        tempDBID = ((ActivityForm)form).getActivityAttendeesType();
        if (tempDBID != null && tempDBID.length() > 0) {
          dbID = tempDBID.substring(0, tempDBID.indexOf("#"));
          dbName = tempDBID.substring(tempDBID.indexOf("#") + 1, tempDBID.length());
        }
      }

      Vector attendeeVec = new Vector();
      if (dbName != null && dbName.equals("employee")) {
        attendeeVec = new Vector();

        Collection sqlResults = remote.getEmployeeListCollection(individualID);

        // ok, now that we have the results from the database,
        // we need to generate a DisplayList object to pass
        // to the List View code (List.jsp)
        Iterator iter = sqlResults.iterator();

        while (iter.hasNext()) {
          HashMap sqlRow = (HashMap)iter.next();
          String indvName = (String)sqlRow.get("Name");
          int indvID = ((Number)sqlRow.get("IndividualID")).intValue();
          attendeeVec.addElement(new DDNameValue("" + indvID + "#" + indvName, indvName));
        }
      } else if (dbName != null && dbName.equals("individual")) {
        gal = AttendeeList.getAttendeeList(individualID, "individual", Integer.parseInt(dbID),
            dataSource);
        attendeeVec = (Vector)gal.get("attendee");
      } else {
        gal = AttendeeList.getAttendeeList(individualID, "allindividual", 0, dataSource);
        attendeeVec = (Vector)gal.get("allattendee");
      }

      Vector att_required = new Vector();
      if (af.getActivityAttendeeRequiredVector() != null) {
        att_required = af.getActivityAttendeeRequiredVector();
      }

      Vector att_optional = new Vector();
      if (af.getActivityAttendeeOptionalVector() != null) {
        att_optional = af.getActivityAttendeeOptionalVector();
      }

      int sizeOfAttendeeList = attendeeVec.size();
      int sizeOfRequiredAttendee = att_required.size();
      int sizeOfOptionalAttendee = att_optional.size();
      int i = 0;
      int j = 0;
      int k = 0;
      String idOfAttendeeList = "";
      String idOfRequiredAttendee = "";
      String idOfOptionalAttendee = "";
      DDNameValue ddAttendeeListInfo = null;
      DDNameValue ddRequiredAttendeeInfo = null;
      DDNameValue ddOptionalAttendeeInfo = null;
      boolean removed = false;

      while (i < sizeOfAttendeeList) {
        removed = false;
        ddAttendeeListInfo = (DDNameValue)attendeeVec.get(i);
        idOfAttendeeList = ddAttendeeListInfo.getStrid();

        while (j < sizeOfRequiredAttendee) {
          ddRequiredAttendeeInfo = (DDNameValue)att_required.get(j);
          idOfRequiredAttendee = ddRequiredAttendeeInfo.getStrid();

          if (idOfAttendeeList.equals(idOfRequiredAttendee)) {
            attendeeVec.remove(i);
            removed = true;
            sizeOfAttendeeList--;
            break;
          }
          j++;
          ddRequiredAttendeeInfo = null;
        }

        while (k < sizeOfOptionalAttendee) {
          ddOptionalAttendeeInfo = (DDNameValue)att_optional.get(k);
          idOfOptionalAttendee = ddOptionalAttendeeInfo.getStrid();

          if (idOfAttendeeList.equals(idOfOptionalAttendee)) {
            attendeeVec.remove(i);
            removed = true;
            sizeOfAttendeeList--;
            break;
          }
          k++;
          ddOptionalAttendeeInfo = null;
        }
        j = 0;
        k = 0;

        if (!removed) {
          i++;
        }
        ddAttendeeListInfo = null;
        removed = false;
      }

      ((ActivityForm)form).setActivityAttendeeRequiredVector(att_required);
      ((ActivityForm)form).setActivityAttendeeOptionalVector(att_optional);

      if (searchIndividual != null && !searchIndividual.equals("")) {
        if (attendeeVec != null) {
          int attendeeSize = attendeeVec.size();
          i = 0;

          while (i < attendeeSize) {
            DDNameValue tempAttendeeListInfo = (DDNameValue)attendeeVec.elementAt(i);
            String valueAttendee = tempAttendeeListInfo.getName();
            String tempValueAttendee = valueAttendee.toUpperCase();
            String tempSearchIndividual = searchIndividual.toUpperCase();
            int occuranceVALUESearch = (tempValueAttendee).indexOf(tempSearchIndividual);

            if (occuranceVALUESearch == -1) {
              attendeeVec.remove(i);
              attendeeSize--;
            } else {
              i++;
            }
          }
        }
      }

      request.setAttribute("attendeeList", attendeeVec);
      request.setAttribute("optional", vecOptional);
      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, ConstantKeys.ATTENDEE);
      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, request
          .getParameter(ConstantKeys.TYPEOFACTIVITY));
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, request
          .getParameter(ConstantKeys.TYPEOFOPERATION));
      FORWARD_final = FORWARD_attendee;
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }

}
