/*
 * $RCSfile: DisplayCustomViewHandler.java,v $    $Revision: 1.3 $  $Date: 2005/07/25 13:37:48 $ - $Author: mcallist $
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

package com.centraview.administration.modulesettings;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;


public class DisplayCustomViewHandler extends org.apache.struts.action.Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    // "customViewSelectForm", defined in administration.xml
    DynaActionForm viewForm = (DynaActionForm)form;

    String moduleName = (request.getParameter("moduleName") != null) ? ((String)request.getParameter("moduleName")) : "Contacts";

    ArrayList linkList = new ArrayList();

    // yes, this is ghetto. but it works - sorry about the magic numbers
    if (moduleName.equals("Contacts")) {
      linkList.add(this.createLink("Entities", "Entity", "14"));
      linkList.add(this.createLink("Individuals", "Individual", "15"));
      linkList.add(this.createLink("Groups", "Group", "16"));
    }

    if (moduleName.equals("Activities")) {
      linkList.add(this.createLink("All Activities", "MultiActivity", "3"));
      linkList.add(this.createLink("Appointments", "Appointment", "3"));
      linkList.add(this.createLink("Calls", "Call", "3"));
      linkList.add(this.createLink("Forecast Sales", "ForecastSales", "30"));
      linkList.add(this.createLink("Literature Requests", "LiteratureFulFillment", "34"));
      linkList.add(this.createLink("Mettings", "Meeting", "3"));
      linkList.add(this.createLink("Next Actions", "NextAction", "3"));
      linkList.add(this.createLink("To Do's", "ToDo", "3"));
      linkList.add(this.createLink("Tasks", "ActivityTask", "37"));
    }
    
    if (moduleName.equals("Notes")) {
      linkList.add(this.createLink("Notes", "Note", "5"));
    }

    if (moduleName.equals("File")) {
      linkList.add(this.createLink("Files", "File", "6"));
    }

    if (moduleName.equals("Sales")) {
      linkList.add(this.createLink("Opportunities", "Opportunity", "30"));
      linkList.add(this.createLink("Proposals", "Proposal", "31"));
    }

    if (moduleName.equals("Marketing")) {
      linkList.add(this.createLink("Marketing Lists", "Marketing", "32"));
      linkList.add(this.createLink("Promotions", "Promotion", "33"));
      linkList.add(this.createLink("Literature FulFillment", "LiteratureFulFillment", "34"));
      linkList.add(this.createLink("Events", "Event", "35"));
    }

    if (moduleName.equals("Projects")) {
      linkList.add(this.createLink("Projects", "Project", "36"));
      linkList.add(this.createLink("Tasks", "Tasks", "37"));
      linkList.add(this.createLink("Timeslips", "Timeslip", "38"));
    }
    
    if (moduleName.equals("Support")) {
      linkList.add(this.createLink("Tickets", "Ticket", "39"));
      linkList.add(this.createLink("FAQ's", "FAQ", "40"));
      linkList.add(this.createLink("Knowledgebase", "Knowledgebase", "41"));
    }
    
    if (moduleName.equals("Accounting")) {
      linkList.add(this.createLink("Order History", "Order", "42"));
      linkList.add(this.createLink("Invoice History", "InvoiceHistory", "56"));
      linkList.add(this.createLink("Payments", "Payment", "43"));
      linkList.add(this.createLink("Expenses", "Expense", "44"));
      linkList.add(this.createLink("Purchase Orders", "PurchaseOrder", "45"));
      linkList.add(this.createLink("Items", "Item", "46"));
      linkList.add(this.createLink("GL Accounts", "GLAccount", "47"));
      linkList.add(this.createLink("Inventory", "Inventory", "48"));
      linkList.add(this.createLink("Vendors", "Vendor", "50"));
    }

    if (moduleName.equals("HR")) {
      linkList.add(this.createLink("Expense Forms", "ExpenseForm", "51"));
      linkList.add(this.createLink("Notes", "TimeSheet", "52"));
      linkList.add(this.createLink("Notes", "EmployeeHandbook", "53"));
      linkList.add(this.createLink("Notes", "Employee", "0"));    // I don't know either
    }

    

    viewForm.set("linkList", linkList);
    
    return mapping.findForward(".view.administration.customview.new");
  }

  private HashMap createLink(String name, String listType, String moduleId) {
    HashMap linkMap = new HashMap();
    linkMap.put("name", name);
    linkMap.put("listType", listType);
    linkMap.put("moduleId", moduleId);
    return linkMap;
  }
  
}

