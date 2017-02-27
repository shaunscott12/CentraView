/*
 * $RCSfile: RelatedInfoListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/02 15:14:35 $ - $Author: mking_cv $
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

package com.centraview.relatedinfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import com.centraview.valuelist.Button;

/**
 * This class is the Struts Action class handler for all Related Info
 * screens in the application. The class will get the URL parameters
 * from the RelatedInfoListForm object, then decide which action to
 * forward to. The action which this class forwards control to will
 * generate a DisplayList, then forward to the RelatedInfoList_c.jsp
 * file for display on the View layer. This class also sets up the
 * dropdown for display at the top of the Related Info screen, and
 * puts it in a request Attribute.
 */
public class RelatedInfoListHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(RelatedInfoListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String forwardPage = "relatedInfoBottom"; // by default, this is the page to forward to

    // get the URL parameters from the RelatedInfoListForm FormBean object
    DynaActionForm relatedInfoListForm = (DynaActionForm)form;
    HttpSession session = request.getSession(true);
    String sessionListFor = (String)session.getAttribute("sessionRelatedListFor");
    try {
      // listType is the type of the list being shown, ie "Address",
      // "Individual", "Notes" etc
      String riListType = (String)relatedInfoListForm.get("riListType");
      // listFor is the type of record in the top frame, ie "Entity",
      // "Individual", "Opportunity"
      String listFor = (String)relatedInfoListForm.get("listFor");
      // If we changed listfors we need to clear out whatever the
      // form gave us.  For now only if we are currently looking at
      // Entity or Individual, because otherwise it is set on the JSP
      session.setAttribute("sessionRelatedListFor", listFor);

      if (!listFor.equals(sessionListFor) && (listFor.equals("Entity") || listFor.equals("Individual"))) {
        riListType = "";
      }

      if (riListType.equals("")) {
        // set the type of list to show in the bottom frame, depending
        // on what type of record is in the top frame. Default to Individuals
        if (listFor.equals("Individual")) {
          riListType = "Activity";
        } else if (listFor.equals("Opportunity")){
          riListType = "Proposal";
        } else {
          riListType = "Individual";
        }
      } else if (riListType.equals("BottomIndividual")) {
        riListType = "Individual";
      }

      // After performing the logic in the DeleteHanlder, we are generate a new request for the list
      // So we will not be carrying the old error. So that we will try to collect the error from the Session variable
      // Then destory it after getting the Session value
      if (session.getAttribute("listErrorMessage") != null) {
        ActionErrors allErrors = (ActionErrors)session.getAttribute("listErrorMessage");
        saveErrors(request, allErrors);
        session.removeAttribute("listErrorMessage");
      }

      relatedInfoListForm.set("riListType", riListType);
      // recordID is the ID of the record in the top frame
      Integer recordID = (Integer)relatedInfoListForm.get("recordId");
      // record name is the title or name of the record in the top frame.
      // It is show in the header of the bottom frame
      String recordName = (String)relatedInfoListForm.get("recordName");
      // parentId is the ID of the parent record for Individual it is the EntityId for 
      // Entity it is the marketing list Id.
      Integer parentId = (Integer)relatedInfoListForm.get("parentId");
      // parent name is the entity name for individual it is invalid for entity
      String parentName = (String)relatedInfoListForm.get("parentName");
      // now that we got the information from the FormBean, set
      // each variable as a request attribute so that the handlers
      // can all access this data on the request
      request.setAttribute("listType", riListType);
      request.setAttribute("listFor", listFor);
      request.setAttribute("recordID", recordID);
      request.setAttribute("recordName", recordName);
      request.setAttribute("parentId", parentId);
      request.setAttribute("parentName", parentName);

      // generate the dropdown at the top of the frame, based on
      // a method in this class. Then set a request attribute
      // that will contain this data (as a HashMap)
      ArrayList dropdownCollection = this.getDropdownMap(listFor);
      relatedInfoListForm.set("dropdownCollection", dropdownCollection);

      ArrayList buttonList = this.setupButtons(listFor, riListType, recordID, recordName, parentId, parentName);
      request.setAttribute("buttonList", buttonList);
      
      // ok, now forward to the appripriate handler based on the
      // listType. Each handler will get the appropriate data and
      // create a DisplayList which it will place in the request
      // object as an attribute. The JSP will then obtain the
      // DisplayList from the attribute and display it.
      forwardPage = riListType;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    return (mapping.findForward(forwardPage));
  } // end execute() method

  /**
   * Returns a ArrayList representation of the dropdown to be displayed
   * at the top of the Related Info screen.  The ArrayList will contain
   * LabelValueBeans which work well with the struts jsp tags.
   * @param listFor   The <b>listFor</b> value of the current request;
   * determines which dropdown values to show.
   * @return  ArrayList representation of the dropdown to be displayed.
   */
  private ArrayList getDropdownMap(String listFor)
  {
    // this is the Treemap which will hold the info to show combo
    // the key is the string to be displayed to the user in the list,
    // and the value is the listType value.
    // DO NOT NAME THE "value" PARAMETER AS A PLURAL WORD!
    ArrayList relatedInfoList = new ArrayList();

    if (listFor.equals("Entity")) {
      relatedInfoList.add(new LabelValueBean("Individuals", "Individual"));
      relatedInfoList.add(new LabelValueBean("Addresses", "Address"));
      relatedInfoList.add(new LabelValueBean("Contact Methods", "ContactMethod"));
      relatedInfoList.add(new LabelValueBean("Custom Fields", "CustomField"));
      relatedInfoList.add(new LabelValueBean("Pending Activities", "Activity"));
      relatedInfoList.add(new LabelValueBean("Notes", "Note"));
      relatedInfoList.add(new LabelValueBean("Tickets", "Ticket"));
      relatedInfoList.add(new LabelValueBean("Proposals", "Proposal"));
      relatedInfoList.add(new LabelValueBean("Opportunities", "Opportunity"));
      relatedInfoList.add(new LabelValueBean("Projects", "Project"));
      relatedInfoList.add(new LabelValueBean("History", "History"));
      relatedInfoList.add(new LabelValueBean("Files", "File"));
      relatedInfoList.add(new LabelValueBean("Orders", "Order"));
      relatedInfoList.add(new LabelValueBean("Invoices", "Invoice"));
      relatedInfoList.add(new LabelValueBean("Payments", "Payment"));
      relatedInfoList.add(new LabelValueBean("Completed Activities", "CompletedActivity"));
      relatedInfoList.add(new LabelValueBean("Email History", "EmailHistory"));
    } else if (listFor.equals("Individual")) {
      relatedInfoList.add(new LabelValueBean("Addresses", "Address"));
      relatedInfoList.add(new LabelValueBean("Contact Methods", "ContactMethod"));
      relatedInfoList.add(new LabelValueBean("Custom Fields", "CustomField"));
      relatedInfoList.add(new LabelValueBean("Pending Activities", "Activity"));
      relatedInfoList.add(new LabelValueBean("Notes", "Note"));
      relatedInfoList.add(new LabelValueBean("Tickets", "Ticket"));
      relatedInfoList.add(new LabelValueBean("Projects", "Project"));
      relatedInfoList.add(new LabelValueBean("Opportunities", "Opportunity"));
      relatedInfoList.add(new LabelValueBean("Proposals", "Proposal"));
      relatedInfoList.add(new LabelValueBean("History", "History"));
      relatedInfoList.add(new LabelValueBean("Files", "File"));
      relatedInfoList.add(new LabelValueBean("Completed Activities", "CompletedActivity"));
      relatedInfoList.add(new LabelValueBean("Email History", "EmailHistory"));
    } else if (listFor.equals("Opportunity")) {
      relatedInfoList.add(new LabelValueBean("Proposals", "Proposal"));
      relatedInfoList.add(new LabelValueBean("Pending Activities", "Activity"));
      relatedInfoList.add(new LabelValueBean("Completed Activities", "CompletedActivity"));
      relatedInfoList.add(new LabelValueBean("Notes", "Note"));
      relatedInfoList.add(new LabelValueBean("Individuals", "Individual"));
      relatedInfoList.add(new LabelValueBean("Expenses", "Expenses")); // The instructions said NOT to name the value as a plural word!!!!!
      relatedInfoList.add(new LabelValueBean("Custom Fields", "CustomField"));
      relatedInfoList.add(new LabelValueBean("Account Team", "GroupMember"));
    } else if (listFor.equals("Project")) {
      relatedInfoList.add(new LabelValueBean("Contacts", "Contact"));
      relatedInfoList.add(new LabelValueBean("Custom Fields", "CustomField"));
      relatedInfoList.add(new LabelValueBean("Expenses", "Expenses")); // The instructions said NOT to name the value as a plural word!!!!!
      relatedInfoList.add(new LabelValueBean("Files", "File"));
      relatedInfoList.add(new LabelValueBean("Completed Activities", "CompletedActivity"));
      relatedInfoList.add(new LabelValueBean("History", "History"));
      relatedInfoList.add(new LabelValueBean("Invoices", "Invoice"));
      relatedInfoList.add(new LabelValueBean("Notes", "Note"));
      relatedInfoList.add(new LabelValueBean("Orders", "Order"));
      relatedInfoList.add(new LabelValueBean("Scheduled Activities", "Activity"));
      relatedInfoList.add(new LabelValueBean("Timeslips", "Timeslip"));
      relatedInfoList.add(new LabelValueBean("Tasks", "Task"));
    }
    return (relatedInfoList);
  } // end getDropdownMap() method
  
  private ArrayList setupButtons(String listFor, String listType, Integer recordId, String recordName, Integer parentId, String parentName) throws ServletException
  {
    String recordNameURL = "";
    String parentNameURL = "";
    try {
      recordNameURL = java.net.URLEncoder.encode(recordName, "ISO-8859-1");
      parentNameURL = java.net.URLEncoder.encode(parentName, "ISO-8859-1");
    } catch (UnsupportedEncodingException e) {
      // well I guess if ISO-8859-1 encoding isn't going to work we are in bad trouble
      logger.error("[setupButtons] Exception thrown.", e);
      throw new ServletException(e);
    }
    ArrayList buttonList = new ArrayList();
    String standardParams = "listType="+listType+"&listFor="+listFor+"&recordID="+recordId+"&recordName="+recordNameURL;
    String parameters = "";

    Button deleteButton = new Button("Delete", "delete", "vl_deleteList();", false);
    
    if (listType.equals("Activity") || listType.equals("CompletedActivity")) {
      if (listFor.equals("Entity"))
      {
        parameters = "?entityID="+recordId+"&entityName="+recordNameURL;
      } else if (listFor.equals("Individual")) {
        parameters = "?entityID="+parentId+"&entityName="+parentNameURL+"&individualID="+recordId+"&individualName="+recordNameURL;
      } else if (listFor.equals("Opportunity")) {
        parameters = "?entityID="+parentId+"&entityName="+parentNameURL+"oppID="+recordId+"oppName="+recordNameURL;
      } else if(listFor.equals("Project")) {
        parameters = "?ProjectID="+recordId+"&ProjectTitle="+recordNameURL+"&entityName="+parentNameURL+"&entityID="+parentId;
      }
      buttonList.add(new Button("New Activity", "new", "c_openWindow('/activities/activity_dispatch.do" + parameters.toString()+ "', 'sched_act', 780, 580, '');", false));
      buttonList.add(deleteButton);
    } else if (listType.equals("Individual")) {
      if(listFor.equals("Entity")) {
        parameters = "entityName="+recordNameURL+"&entityNo="+recordId;
      } else if(listFor.equals("Opportunities")) {
        parameters = "entityName="+parentNameURL+"&entityNo="+parentId;
      }
      buttonList.add(new Button("View", "view", "vl_viewList();", false));
      buttonList.add(new Button("New Individual", "new", "c_openWindow('/contacts/new_individual.do?"+parameters+"', 'newIndividual', 729, 301, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Address")) {
      buttonList.add(new Button("New Address", "new", "c_goTo('/contacts/new_related_address.do?"+standardParams+"');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("ContactMethod")) {
      buttonList.add(new Button("New Contact Method", "new", "c_goTo('/contacts/new_contact_method.do?"+standardParams+"');", false));
      buttonList.add(deleteButton);
    }else if(listType.equals("EmailHistory")) {
      buttonList.add(new Button("Compose", "new", "c_openWindow('/email/compose.do','',720,585,'');", false));
    } else if(listType.equals("Note")) {
      parameters = "?TYPEOFOPERATION=ADD&bottomFrame=true";
      if (listFor.equals("Opportunity")) {
        parameters += "&" + standardParams;
      } else if (listFor.equals("Individual")) {
        parameters += "&entityname="+parentNameURL+"&entityid="+parentId+"&individualid="+recordId+"&individualname="+recordNameURL+"&"+standardParams;
      } else if (listFor.equals("Entity")) {
        parameters += "&entityname="+recordNameURL+"&entityid="+recordId+"&"+standardParams;
      }
      if (!listFor.equals("Projects")) {  // for some reason currently you cannot create a new note on project details screen
        buttonList.add(new Button("New Note", "new", "c_goTo('/notes/new_note.do"+parameters+"&TYPEOFOPERATION=ADD');", false));
      }
      buttonList.add(deleteButton);
    } else if(listType.equals("Ticket")) {
      if (listFor.equals("Individual")) {
        parameters = "?individualname="+recordNameURL+"&individualid="+recordId+"&entityname="+parentNameURL+"&entityid="+parentId;
      } else if(listFor.equals("Entity")) {
        parameters = "?entityname="+recordNameURL+"&entityid="+recordId;
      }
      buttonList.add(new Button("New Ticket", "new", "c_openWindow('/support/new_ticket.do"+parameters+"','',715,445,'');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Proposal")) {
      if (listFor.equals("Individual")) {
        parameters = "?individual="+recordNameURL+"&individualid="+recordId;
      } else if (listFor.equals("Opportunities")) {
        parameters = "?oppTitle="+recordNameURL+"&oppId="+recordId;
      }else{
        // hmm... for some reason, the related Proposals list for Opportunity detail has a bum listFor value. 
        parameters = "?oppTitle=" + recordNameURL + "&oppId=" + recordId;
      }
      buttonList.add(new Button("New Proposal", "new", "c_openWindow('/sales/new_proposal.do"+parameters+"', '', 715, 445, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Opportunity")) {
      if (listFor.equals("Entity")) {
        parameters = "?entityname="+recordNameURL+"&entityid="+recordId;
      } else if(listFor.equals("Individual")) {
        parameters = "?individualname="+recordNameURL+"&individualid="+recordId+"&entityname="+parentNameURL+"&entityid="+parentId;
      }
      buttonList.add(new Button("New Opportunity", "new", "c_openWindow('/sales/new_opportunity.do"+parameters+"', '', 715, 445, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Project")) {
      if (listFor.equals("Entity")) {
        parameters = "?entityname="+recordNameURL+"&entityid="+recordId;
      } else if(listFor.equals("Individual")) {
        parameters = "?contact="+recordNameURL+"&contactID="+recordId+"&entityname="+parentNameURL+"&entityid="+parentId;
      }
      buttonList.add(new Button("New Project", "new", "c_openWindow('/projects/new_project.do"+parameters+"', '', 715, 445, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("File")) {
      parameters = "?BottomFile=true";
      if (listFor.equals("Entity")) {
        parameters += "&entityname="+recordNameURL+"&entityid="+recordId;
      } else if(listFor.equals("Individual")) {
        parameters += "&individualname="+recordNameURL+"&individualid="+recordId+"&entityname="+parentNameURL+"&entityid="+parentId;
      } else if(listFor.equals("Project")) {
        parameters += "&ProjectTitle="+recordNameURL+"&ProjectID="+recordId;
      }
      buttonList.add(new Button("New File", "new", "c_openWindow('/files/file_new.do"+parameters+"', '', 715, 445, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Task")) {
      parameters = "?projectname="+recordNameURL+"&projectid="+recordId;
      buttonList.add(new Button("New Task", "new", "c_openWindow('/projects/new_task.do"+parameters+"', '', 730, 360, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Timeslip")) {
      parameters = "?projectname="+recordNameURL+"&projectid="+recordId;
      buttonList.add(new Button("New Timeslip", "new", "c_openWindow('/projects/new_timeslip.do"+parameters+"', '', 730, 360, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Contact")) {
      parameters = "entityName="+parentNameURL+"&entityNo="+parentId;
      buttonList.add(new Button("New Individual", "new", "c_openWindow('/contacts/new_individual.do"+parameters+"', '', 729, 301, '');", false));
      buttonList.add(deleteButton);
    } else if(listType.equals("Order")) {
      buttonList.add(new Button("New Order", "new", "c_showMainWindow('/accounting/new_order.do', '', 729, 301, '');", false));
    }
    return buttonList;
  }
} // end class definition
