/*
 * $RCSfile: ActivityList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:58 $ - $Author: mking_cv $
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
import java.sql.Timestamp;
import java.util.Collection;
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
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.syncfacade.SyncFacadeHome;

public class ActivityList extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // CompanionLink expects plain text, and we're printing 
    // directly to STDOUT, so set the content type
    response.setContentType("text/plain");
    PrintWriter writer = response.getWriter();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    // "activtyListForm" defined in struts-config-sync.xml
    DynaActionForm activityForm = (DynaActionForm)form;

    String sessionID = (String)activityForm.get("sessionID");
    SyncUtils syncUtils = new SyncUtils();

    try
    {
      // check to see if CompanionLink Agent has signed in
      if (syncUtils.checkSession(userObject, activityForm) == false)
      {
        writer.print("FAIL: You are not logged in.");
        return(null);
      }

      // decode all characters that are encoded by CompanionLink Agent
      activityForm = syncUtils.parseSpecialChars(activityForm);

      // get an EJB connection
      SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject("com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
      com.centraview.syncfacade.SyncFacade sfremote = (com.centraview.syncfacade.SyncFacade)syncHome.create();
      sfremote.setDataSource(dataSource);

      // get the list of individuals from the ejb layer
      Collection activityList = sfremote.getActivityList(individualID);

      // get the logged-in user's field-level privileges and 
      // store for use when printing fields later.
      ModuleFieldRightMatrix rightsMatrix = userObject.getUserPref().getModuleAuthorizationMatrix();
      HashMap fieldRights = rightsMatrix.getFieldRights("Activities");

      // activityList shouldn't be null. If it is, there's a problem
      // SyncFacadeEJB should always return a valid Collection (an 
      // empty Collection is still a valid Collection, but null is not)
      if (activityList != null)
      {
        // print the header row, even if there are not results returned...
        StringBuffer headers = new StringBuffer();
        headers.append("activityID\t");
        headers.append("type\t");
        headers.append("title\t");
        headers.append("lastModified\t");
        headers.append("dueDate\t");
        headers.append("priority\t");
        headers.append("status\t");
        headers.append("createdBy\t");
        headers.append("startDateTime\t");
        headers.append("endDateTime\t");
        headers.append("description\t");
        headers.append("private\t");
        headers.append("recurrenceStartDate\t");
        headers.append("recurrenceEndDate\t");
        headers.append("recurrenceType\t");
        headers.append("every\t");
        headers.append("on\t");
        headers.append("alarmDateTime\t");
        headers.append("LinkCompany\t");
        headers.append("LinkContact");

        writer.print(headers.toString() + "\n");

        // we successfully got the Activities list, let's process it :-)
        if (activityList.size() > 0)
        {
          Iterator it = activityList.iterator();
          
          while (it.hasNext())
          {
            HashMap activityDetails = (HashMap)it.next();
            
            String activityType = (String)activityDetails.get("activityType");
            if (activityType != null && (activityType.equals("Forecast Sale") || activityType.equals("Task") || activityType.equals("Literature Request")))
            {
              // We do not send Opportunity, Task, or Literture Request
              // records to CompanionLink, so skip this iteration of the
              // while loop.
              continue;
            }

            // This hashmap will temporarily hold all field values until
            // we are ready to print them out. It will be helpful to us
            // when we need to encode special characters.
            HashMap record = new HashMap();
            
            // activityID
            int activityID = ((Number)activityDetails.get("activityID")).intValue();
            record.put("activityID", String.valueOf(activityID));

            // type
            record.put("type", activityType);
            
            // First check field rights privileges (of certain fields),
            // if user has sufficient privilege, print field, if user
            // does not have sufficient privilege, print a "-".
            
            // ***IMPORTANT!!*** If the user does not have privilege to
            // a given field, CompanionLink expects a "-". Sending a null
            // or blank string will cause CompanionLink to believe this
            // is the CONTENT of the field, and will update the record
            // with that information. It is extremely important to make
            // sure this is done properly.

            // ***IMPORTANT!!*** It is also extremely important to note
            // that we can never set any field to the java NULL value. This
            // will cause "null" to be printed out to the CompanionLink
            // client, in which case CompanionLink will evaluate the String
            // "null" as the content of the field. Therefore, ALWAYS CHECK
            // FOR NULL VALUES BEFORE PRINTING!!! (or adding to our temp
            // HashMap)...
            
            // Title
            if (((Integer)fieldRights.get("title")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              // user has privilege to View this field (or greater)
              String title = (String)activityDetails.get("title");
              record.put("title", (title != null) ? title : "");
            }else{
              // user has None privilege to this field
              record.put("title", "-");
            }
            
            // lastModified
            record.put("lastModified", syncUtils.formatDate((Timestamp)activityDetails.get("lastModified")));

            // dueDate
            record.put("dueDate", syncUtils.formatDate((Timestamp)activityDetails.get("dueDate")));
            
            // Priority
            if (((Integer)fieldRights.get("priority")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String priority = (String)activityDetails.get("priority");
              record.put("priority", (priority != null) ? priority : "");
            }else{
              record.put("priority", "-");
            }
            
            // Status
            if (((Integer)fieldRights.get("status")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String status = (String)activityDetails.get("status");
              record.put("status", (status != null) ? status : "");
            }else{
              record.put("status", "-");
            }

            // CreatedBy
            if (((Integer)fieldRights.get("creator")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String createdBy = (String)activityDetails.get("createdBy");
              record.put("createdBy", (createdBy != null) ? createdBy : "");
            }else{
              record.put("createdBy", "-");
            }
            
            // startDateTime
            if (((Integer)fieldRights.get("start")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              record.put("startDateTime", syncUtils.formatDate((Timestamp)activityDetails.get("startDateTime")));
            }else{
              record.put("startDateTime", "-");
            }


            // endDateTime
            if (((Integer)fieldRights.get("end")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              record.put("endDateTime", syncUtils.formatDate((Timestamp)activityDetails.get("endDateTime")));
            }else{
              record.put("endDateTime", "-");
            }
            
            // description
            if (((Integer)fieldRights.get("details")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String description = (String)activityDetails.get("description");
              record.put("description", (description != null) ? description : "");
            }else{
              record.put("description", "-");
            }
            
            // private
            if (((Integer)fieldRights.get("visibility")).intValue() < ModuleFieldRightMatrix.NONE_RIGHT)
            {
              String privateStatus = (String)activityDetails.get("private");
              if (privateStatus != null && privateStatus.equals("PRIVATE"))
              {
                record.put("private", "Yes");
              }else{
                record.put("private", "No");
              }
            }else{
              record.put("private", "-");
            }
            
            // recurrenceStartDate
            java.sql.Date recurrStartDate = (java.sql.Date)activityDetails.get("recurrenceStartDate");
            if (recurrStartDate != null)
            {
              Timestamp recurrStartTimestamp = new Timestamp(recurrStartDate.getTime());
              record.put("recurrenceStartDate", syncUtils.formatDate(recurrStartTimestamp));
            }else{
              record.put("recurrenceStartDate", "");
            }

            // recurrenceEndDate
            java.sql.Date recurrEndDate = (java.sql.Date)activityDetails.get("recurrenceEndDate");
            if (recurrEndDate != null)
            {
              Timestamp recurrEndTimestamp = new Timestamp(recurrEndDate.getTime());
              record.put("recurrenceEndDate", syncUtils.formatDate(recurrEndTimestamp));
            }else{
              record.put("recurrenceEndDate", "");
            }
            
            // recurrenceType
            String recurrenceType = (String)activityDetails.get("recurrenceType");
            record.put("recurrenceType", (recurrenceType != null) ? recurrenceType : "");

            // every
            Number every = (Number)activityDetails.get("every");
            if (every != null)
            {
              record.put("every", every.toString());
            }else{
              record.put("every", "");
            }

            // on
            Number on = (Number)activityDetails.get("recurrOn");
            if (on != null)
            {
              record.put("on", on.toString());
            }else{
              record.put("on", "");
            }
            
            // alarmDateTime
            record.put("alarmDateTime", syncUtils.formatDate((Timestamp)activityDetails.get("alarmDateTime")));
            
            // linkCompany
            String linkCompany = (String)activityDetails.get("linkCompany");
            record.put("LinkCompany", (linkCompany != null) ? linkCompany : "");

            // linkContact
            String linkContact = (String)activityDetails.get("linkContact");
            record.put("LinkContact", (linkContact != null) ? linkContact : "");


            // now encode all strings in the record properly
            record = syncUtils.encodeRecord(record);

            // time to print out the record and move on
            // DO NOT CHANGE THE ORDER OF THESE FIELDS!
            // NOTE that we did not add the "\t" delimiter
            // to each field, but instead we're adding it here
            // as we print out each field. That is because we
            // encode the "record" HashMap as a whole, and part
            // of that encoding encodes tabs into a non-printable
            // character. If we had added the tab to our fields,
            // then we would lose our field delimiter...
            writer.print(record.get("activityID") + "\t");
            writer.print(record.get("type") + "\t");
            writer.print(record.get("title") + "\t");
            writer.print(record.get("lastModified") + "\t");
            writer.print(record.get("dueDate") + "\t");
            writer.print(record.get("priority") + "\t");
            writer.print(record.get("status") + "\t");
            writer.print(record.get("createdBy") + "\t");
            writer.print(record.get("startDateTime") + "\t");
            writer.print(record.get("endDateTime") + "\t");
            writer.print(record.get("description") + "\t");
            writer.print(record.get("private") + "\t");
            writer.print(record.get("recurrenceStartDate") + "\t");
            writer.print(record.get("recurrenceEndDate") + "\t");
            writer.print(record.get("recurrenceType") + "\t");
            writer.print(record.get("every") + "\t");
            writer.print(record.get("on") + "\t");
            writer.print(record.get("alarmDateTime") + "\t");
            writer.print(record.get("LinkCompany") + "\t");
            writer.print(record.get("LinkContact") + "\n");  // NOTE THE NEWLINE HERE!!!
          }   // end while(it.hasNext()) (activityList)
        }
      }else{
        // activity list was null. Something must have gone wrong.
        writer.print("FAIL: An unknown error occurred.");
        return(null);
      }
    }catch(Exception e){
      System.out.println("[Exception][Sync][ActivityList] Exception thrown in execute(): " + e);
      // TODO: remove stack trace
      e.printStackTrace();
    }

    // we're not forwarding to a jsp, so return null
    return(null);
  }   // end execute()

}   // end class Login definition

