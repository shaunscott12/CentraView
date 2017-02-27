/*
 * $RCSfile: DisplayDefaultViewHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:45 $ - $Author: mking_cv $
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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewHome;

public class DisplayDefaultViewHandler extends org.apache.struts.action.Action
{

  public static final String GLOBAL_FORWARD_failure = ".view.administration.default_view";
  private static final String FORWARD_settings = ".view.administration.default_view";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String submodule = request.getParameter("sourcefor");
      if (submodule == "" || submodule == null ||submodule.length() == 0) {
        submodule = request.getAttribute("typeofsubmodule").toString();
      }
      
      String setting = request.getParameter("setting");
      
      if (submodule == null) {
        submodule = "Contacts";
      }

      ArrayList listTypeTable = new ArrayList();
      DynaActionForm dynaform = (DynaActionForm) form;

      ViewHome viewHome = (ViewHome) CVUtility.getHomeObject("com.centraview.view.ViewHome", "View");
      View remote = (View) viewHome.create();
      remote.setDataSource(dataSource);

      if (submodule.equals("Contacts")) {
        listTypeTable.add("Entity");
        listTypeTable.add("Individual");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        //Set Entity
        HashMap entityMap = (HashMap) viewTypeList.get("Entity");
        String entityViewID = (String) entityMap.get("DefaultViewID");
        Vector entityVec = (Vector) entityMap.get("ViewList");
        dynaform.set("contactsEntityList",entityViewID);
        dynaform.set("entityListVec",entityVec);

        //Set Individual
        HashMap individualMap = (HashMap) viewTypeList.get("Individual");
        String individualViewID = (String) individualMap.get("DefaultViewID");
        Vector individualVec = (Vector) individualMap.get("ViewList");
        dynaform.set("contactsIndividualList",individualViewID);
        dynaform.set("individualListVec",individualVec);

      } else if(submodule.equals("Activities")) {

        listTypeTable.add("MultiActivity");
        listTypeTable.add("Appointment");
        listTypeTable.add("Call");
        listTypeTable.add("Meeting");
        listTypeTable.add("NextAction");
        listTypeTable.add("ToDo");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // set MultiActivity
        HashMap multiActivityMap = (HashMap) viewTypeList.get("MultiActivity");
        String multiActivityViewID = (String) multiActivityMap.get("DefaultViewID");
        Vector multiActivityVec = (Vector) multiActivityMap.get("ViewList");
        dynaform.set("activitiesAllActivity",multiActivityViewID);
        dynaform.set("multiActivityVec",multiActivityVec);

        // set Appointment
        HashMap appointmentMap = (HashMap) viewTypeList.get("Appointment");
        String appointmentViewID = (String) appointmentMap.get("DefaultViewID");
        Vector appointmentVec = (Vector) appointmentMap.get("ViewList");
        dynaform.set("activitiesAppointment",appointmentViewID);
        dynaform.set("appointmentVec",appointmentVec);

        // set Call
        HashMap callMap = (HashMap) viewTypeList.get("Call");
        String callViewID = (String) callMap.get("DefaultViewID");
        Vector callsVec = (Vector) callMap.get("ViewList");
        dynaform.set("activitiesCalls",callViewID);
        dynaform.set("callsVec",callsVec);

        // set Meeting
        HashMap meetingMap = (HashMap) viewTypeList.get("Meeting");
        String meetingViewID = (String) meetingMap.get("DefaultViewID");
        Vector meetingsVec = (Vector) meetingMap.get("ViewList");
        dynaform.set("activitiesMeetings",meetingViewID);
        dynaform.set("meetingsVec",meetingsVec);

        // set ToDo
        HashMap toDoMap = (HashMap) viewTypeList.get("ToDo");
        String toDoViewID = (String) toDoMap.get("DefaultViewID");
        Vector toDosVec = (Vector) toDoMap.get("ViewList");
        dynaform.set("activitiesToDos",toDoViewID);
        dynaform.set("toDosVec",toDosVec);

        // set NextAction
        HashMap nextActionMap = (HashMap) viewTypeList.get("NextAction");
        String nextActionViewID = (String) nextActionMap.get("DefaultViewID");
        Vector nextActionsVec = (Vector) nextActionMap.get("ViewList");
        dynaform.set("activitiesNextActions",nextActionViewID);
        dynaform.set("nextActionsVec",nextActionsVec);

      } else if(submodule.equals("Notes")) {

        listTypeTable.add("Note");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // Set Note
        HashMap notesMap = (HashMap) viewTypeList.get("Note");
        String notesViewID = (String) notesMap.get("DefaultViewID");
        Vector notesVec = (Vector) notesMap.get("ViewList");
        dynaform.set("notes",notesViewID);
        dynaform.set("notesVec",notesVec);

      } else if(submodule.equals("File")) {
        listTypeTable.add("File");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // Set File
        HashMap filesMap = (HashMap) viewTypeList.get("File");
        String filesViewID = (String) filesMap.get("DefaultViewID");
        Vector filesVec = (Vector) filesMap.get("ViewList");
        dynaform.set("files",filesViewID);
        dynaform.set("filesVec",filesVec);

      } else if(submodule.equals("Sales")) {

        listTypeTable.add("Opportunity");
        listTypeTable.add("Proposal");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        //Set Opportunity
        HashMap opportunityMap = (HashMap) viewTypeList.get("Opportunity");
        String opportunityViewID = (String) opportunityMap.get("DefaultViewID");
        Vector opportunityVec = (Vector) opportunityMap.get("ViewList");
        dynaform.set("salesOpportunity",opportunityViewID);
        dynaform.set("opportunityVec",opportunityVec);

        //Set Proposal
        HashMap proposalMap = (HashMap) viewTypeList.get("Proposal");
        String proposalViewID = (String) proposalMap.get("DefaultViewID");
        Vector proposalVec = (Vector) proposalMap.get("ViewList");
        dynaform.set("salesProposal",proposalViewID);
        dynaform.set("proposalVec",proposalVec);

      } else if(submodule.equals("Marketing")) {

        listTypeTable.add("Marketing");
        listTypeTable.add("Promotion");
        listTypeTable.add("LiteratureFulfillment");
        listTypeTable.add("Event");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // set Marketing
        HashMap marketingMap = (HashMap) viewTypeList.get("Marketing");
        String marketingViewID = (String) marketingMap.get("DefaultViewID");
        Vector marketingVec = (Vector) marketingMap.get("ViewList");
        dynaform.set("marketings",marketingViewID);
        dynaform.set("marketingsVec",marketingVec);

        // set Promotion
        HashMap promotionMap = (HashMap) viewTypeList.get("Promotion");
        String promotionViewID = (String) promotionMap.get("DefaultViewID");
        Vector promotionVec = (Vector) promotionMap.get("ViewList");
        dynaform.set("marketingsPromotions",promotionViewID);
        dynaform.set("promotionsVec",promotionVec);

        // set LiteratureFulfillment
        HashMap literatureFullillmentMap = (HashMap) viewTypeList.get("LiteratureFulfillment");
        String literatureFullillmentViewID = (String) literatureFullillmentMap.get("DefaultViewID");
        Vector literatureFullillmentsVec = (Vector) literatureFullillmentMap.get("ViewList");
        dynaform.set("marketingsLiteraturefulfillment",literatureFullillmentViewID);
        dynaform.set("literaturefulfillmentVec",literatureFullillmentsVec);

        // set Event
        HashMap eventMap = (HashMap) viewTypeList.get("Event");
        String eventViewID = (String) eventMap.get("DefaultViewID");
        Vector eventsVec = (Vector) eventMap.get("ViewList");
        dynaform.set("marketingsEvents",eventViewID);
        dynaform.set("eventsVec",eventsVec);

      } else if(submodule.equals("Projects")) {

        listTypeTable.add("Project");
        listTypeTable.add("Appointment");
        listTypeTable.add("Call");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // set Project
        HashMap projectMap = (HashMap) viewTypeList.get("Project");
        String projectViewID = (String) projectMap.get("DefaultViewID");
        Vector projectVec = (Vector) projectMap.get("ViewList");
        dynaform.set("projectsProject",projectViewID);
        dynaform.set("projectVec",projectVec);

        // set Tasks
        HashMap taskMap = (HashMap) viewTypeList.get("Tasks");
        String taskViewID = (String) taskMap.get("DefaultViewID");
        Vector taskVec = (Vector) taskMap.get("ViewList");
        dynaform.set("projectsTask",taskViewID);
        dynaform.set("taskVec",taskVec);

        // set Timeslip
        HashMap timeslipMap = (HashMap) viewTypeList.get("Timeslip");
        String timeslipViewID = (String) timeslipMap.get("DefaultViewID");
        Vector timeslipsVec = (Vector) timeslipMap.get("ViewList");
        dynaform.set("projectsTimeSlips",timeslipViewID);
        dynaform.set("timeSlipsVec",timeslipsVec);

      } else if(submodule.equals("Support")) {

        listTypeTable.add("Ticket");
        listTypeTable.add("FAQ");
        listTypeTable.add("KnowledgeBase");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // set Ticket
        HashMap ticketMap = (HashMap) viewTypeList.get("Ticket");
        String ticketViewID = (String) ticketMap.get("DefaultViewID");
        Vector ticketVec = (Vector) ticketMap.get("ViewList");
        dynaform.set("supportTicket",ticketViewID);
        dynaform.set("ticketVec",ticketVec);

        // set FAQ
        HashMap faqMap = (HashMap) viewTypeList.get("FAQ");
        String faqViewID = (String) faqMap.get("DefaultViewID");
        Vector faqVec = (Vector) faqMap.get("ViewList");
        dynaform.set("supportFaq",faqViewID);
        dynaform.set("faqVec",faqVec);

        // set KnowledgeBase
        HashMap knowledgebaseMap = (HashMap) viewTypeList.get("KnowledgeBase");
        String knowledgebaseViewID = (String) knowledgebaseMap.get("DefaultViewID");
        Vector knowledgebasesVec = (Vector) knowledgebaseMap.get("ViewList");
        dynaform.set("supportKnowledgebase",knowledgebaseViewID);
        dynaform.set("knowledgebaseVec",knowledgebasesVec);

      } else if(submodule.equals("Accounting")) {

        listTypeTable.add("Order");
        listTypeTable.add("InvoiceHistory");
        listTypeTable.add("Payment");
        listTypeTable.add("Expense");
        listTypeTable.add("PurchaseOrder");
        listTypeTable.add("Item");
        listTypeTable.add("GLAccount");
        listTypeTable.add("Inventory");
        listTypeTable.add("Vendor");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // set Order
        HashMap orderMap = (HashMap) viewTypeList.get("Order");
        String orderViewID = (String) orderMap.get("DefaultViewID");
        Vector orderVec = (Vector) orderMap.get("ViewList");
        dynaform.set("accountingsOrderHistory",orderViewID);
        dynaform.set("orderHistoryVec",orderVec);

        // set InvoiceHistory
        HashMap invoiceMap = (HashMap) viewTypeList.get("InvoiceHistory");
        String invoiceViewID = (String) invoiceMap.get("DefaultViewID");
        Vector invoiceVec = (Vector) invoiceMap.get("ViewList");
        dynaform.set("accountingsInvoiceHistory",invoiceViewID);
        dynaform.set("invoiceHistoryVec",invoiceVec);

        // set Payment
        HashMap paymentMap = (HashMap) viewTypeList.get("Payment");
        String paymentViewID = (String) paymentMap.get("DefaultViewID");
        Vector paymentsVec = (Vector) paymentMap.get("ViewList");
        dynaform.set("accountingsPayments",paymentViewID);
        dynaform.set("paymentsVec",paymentsVec);

        // set Expense
        HashMap expenseMap = (HashMap) viewTypeList.get("Expense");
        String expenseViewID = (String) expenseMap.get("DefaultViewID");
        Vector expensesVec = (Vector) expenseMap.get("ViewList");
        dynaform.set("accountingsExpenses",expenseViewID);
        dynaform.set("expensesVec",expensesVec);

        // set PurchaseOrder
        HashMap purchaseOrderMap = (HashMap) viewTypeList.get("PurchaseOrder");
        String purchaseOrderViewID = (String) purchaseOrderMap.get("DefaultViewID");
        Vector purchaseOrdersVec = (Vector) purchaseOrderMap.get("ViewList");
        dynaform.set("accountingsPurchaseOrder",purchaseOrderViewID);
        dynaform.set("purchaseOrderVec",purchaseOrdersVec);

        // set Item
        HashMap itemMap = (HashMap) viewTypeList.get("Item");
        String itemViewID = (String) itemMap.get("DefaultViewID");
        Vector itemsVec = (Vector) itemMap.get("ViewList");
        dynaform.set("accountingsItems",itemViewID);
        dynaform.set("itemsVec",itemsVec);

        // set GLAccount
        HashMap glAccountMap = (HashMap) viewTypeList.get("GLAccount");
        String glAccountViewID = (String) glAccountMap.get("DefaultViewID");
        Vector glAccountsVec = (Vector) glAccountMap.get("ViewList");
        dynaform.set("accountingsGLAccounting",glAccountViewID);
        dynaform.set("gLAccountingVec",glAccountsVec);

        // set Inventory
        HashMap inventoryMap = (HashMap) viewTypeList.get("Inventory");
        String inventoryViewID = (String) inventoryMap.get("DefaultViewID");
        Vector inventorysVec = (Vector) inventoryMap.get("ViewList");
        dynaform.set("accountingsInventory",inventoryViewID);
        dynaform.set("inventoryVec",inventorysVec);

        // set Vendor
        HashMap vendorMap = (HashMap) viewTypeList.get("Vendor");
        String vendorViewID = (String) vendorMap.get("DefaultViewID");
        Vector vendorsVec = (Vector) vendorMap.get("ViewList");
        dynaform.set("accountingsVendors",vendorViewID);
        dynaform.set("vendorsVec",vendorsVec);

      } else if(submodule.equals("HR")) {

        listTypeTable.add("Expenses");
        listTypeTable.add("TimeSheet");
        listTypeTable.add("EmployeeHandbook");
        listTypeTable.add("Employee");

        HashMap viewTypeList = remote.getDefaultViews(listTypeTable);

        // set Expenses
        HashMap expensesMap = (HashMap) viewTypeList.get("Expenses");
        String expensesViewID = (String) expensesMap.get("DefaultViewID");
        Vector expensesVec = (Vector) expensesMap.get("ViewList");
        dynaform.set("hrExpensesForm",expensesViewID);
        dynaform.set("expensesFormVec",expensesVec);

        // set TimeSheet
        HashMap timesheetMap = (HashMap) viewTypeList.get("TimeSheet");
        String timesheetViewID = (String) timesheetMap.get("DefaultViewID");
        Vector timesheetVec = (Vector) timesheetMap.get("ViewList");
        dynaform.set("hrTimeSheets",timesheetViewID);
        dynaform.set("timeSheetsVec",timesheetVec);

        // set EmployeeHandbook
        HashMap employeeHandbookMap = (HashMap) viewTypeList.get("EmployeeHandbook");
        String employeeHandbookViewID = (String) employeeHandbookMap.get("DefaultViewID");
        Vector employeeHandbooksVec = (Vector) employeeHandbookMap.get("ViewList");
        dynaform.set("hrEmployeeHandbook",employeeHandbookViewID);
        dynaform.set("employeeHandbookVec",employeeHandbooksVec);

        // set Employee
        HashMap employeeMap = (HashMap) viewTypeList.get("Employee");
        String employeeViewID = (String) employeeMap.get("DefaultViewID");
        Vector employeesVec = (Vector) employeeMap.get("ViewList");
        dynaform.set("hrEmployee",employeeViewID);
        dynaform.set("employeeVec",employeesVec);

      }

      request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS);
      request.setAttribute("typeofsubmodule",submodule);
      request.setAttribute("settingfor",setting);

      FORWARD_final = FORWARD_settings;
    } catch (Exception e) {
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}

