/*
 * $RCSfile: SetDefaultViewHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:47 $ - $Author: mking_cv $
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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewHome;


public class SetDefaultViewHandler  extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.administration.default_view";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  private static final String FORWARD_save = ".view.administration.default_view";
  private static final String FORWARD_saveandclose = ".view.administration.module_settings";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String returnStatus = "";
      String button = request.getParameter("buttonpress").toString();
      String setting = request.getParameter("setting").toString();
      String submodule = request.getParameter("submodule").toString();

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();

      // intialize the dynaForm
      DynaActionForm dynaform = (DynaActionForm) form;

      // set the View ID according to the Module name.
      HashMap defaultViews = new HashMap();

      if (submodule.equals("Contacts")) {
        String entityViewID = (String) dynaform.get("contactsEntityList");
        String individualViewID = (String) dynaform.get("contactsIndividualList");
        defaultViews.put("Entity",entityViewID);
        defaultViews.put("Individual",individualViewID);
      } else if(submodule.equals("Activities")) {
        String allActivityViewID = (String) dynaform.get("activitiesAllActivity");
        String appointmentViewID = (String) dynaform.get("activitiesAppointment");
        String callViewID = (String) dynaform.get("activitiesCalls");
        String meetingViewID = (String) dynaform.get("activitiesMeetings");
        String todoViewID = (String) dynaform.get("activitiesToDos");
        String nextActionViewID = (String) dynaform.get("activitiesNextActions");
        defaultViews.put("MultiActivity",allActivityViewID);
        defaultViews.put("Appointment",appointmentViewID);
        defaultViews.put("Call",callViewID);
        defaultViews.put("Meeting",meetingViewID);
        defaultViews.put("NextAction",nextActionViewID);
        defaultViews.put("ToDo",todoViewID);
      } else if(submodule.equals("Notes")) {
        String notesID = (String) dynaform.get("notes");
        defaultViews.put("Note",notesID);
      } else if(submodule.equals("File")) {
        String filesViewID = (String) dynaform.get("files");
        defaultViews.put("File",filesViewID);
      } else if(submodule.equals("Sales")) {
        String opportunityViewID = (String) dynaform.get("salesOpportunity");
        String proposalViewID = (String) dynaform.get("salesProposal");
        defaultViews.put("Opportunity",opportunityViewID);
        defaultViews.put("Proposal",proposalViewID);
      } else if(submodule.equals("Marketing")) {
        String marketingsViewID = (String) dynaform.get("marketings");
        String promotionsViewID = (String) dynaform.get("marketingsPromotions");
        String literatureFullfullmentViewID = (String) dynaform.get("marketingsLiteraturefulfillment");
        String eventsViewID = (String) dynaform.get("marketingsEvents");
        defaultViews.put("Marketing",marketingsViewID);
        defaultViews.put("Promotion",promotionsViewID);
        defaultViews.put("LiteratureFulfillment",literatureFullfullmentViewID);
        defaultViews.put("Event",eventsViewID);
      } else if(submodule.equals("Projects")) {
        String projectViewID = (String) dynaform.get("projectsProject");
        String taskViewID = (String) dynaform.get("projectsTask");
        String timeslipViewID = (String) dynaform.get("projectsTimeSlips");
        defaultViews.put("Project",projectViewID);
        defaultViews.put("Tasks",taskViewID);
        defaultViews.put("Timeslip",timeslipViewID);
      } else if(submodule.equals("Support")) {
        String ticketViewID = (String) dynaform.get("supportTicket");
        String faqViewID = (String) dynaform.get("supportFaq");
        String knowledgeViewID = (String) dynaform.get("supportKnowledgebase");
        defaultViews.put("Ticket",ticketViewID);
        defaultViews.put("FAQ",faqViewID);
        defaultViews.put("KnowledgeBase",knowledgeViewID);
      } else if(submodule.equals("Accounting")) {
        String orderViewID = (String) dynaform.get("accountingsOrderHistory");
        String invoiceViewID = (String) dynaform.get("accountingsInvoiceHistory");
        String paymentViewID = (String) dynaform.get("accountingsPayments");
        String expenseViewID = (String) dynaform.get("accountingsExpenses");
        String purchaseOrderViewID = (String) dynaform.get("accountingsPurchaseOrder");
        String itemsViewID = (String) dynaform.get("accountingsItems");
        String glAccountViewID = (String) dynaform.get("accountingsGLAccounting");
        String inventoryViewID = (String) dynaform.get("accountingsInventory");
        //String discountViewID = (String) dynaform.get("accountingsVolumeDiscount");
        String vendorViewID = (String) dynaform.get("accountingsVendors");
        defaultViews.put("Order",orderViewID);
        defaultViews.put("InvoiceHistory",invoiceViewID);
        defaultViews.put("Payment",paymentViewID);
        defaultViews.put("Expense",expenseViewID);
        defaultViews.put("PurchaseOrder",purchaseOrderViewID);
        defaultViews.put("Item",itemsViewID);
        defaultViews.put("GLAccount",glAccountViewID);
        defaultViews.put("Inventory",inventoryViewID);
        defaultViews.put("Vendor",vendorViewID);
      } else if(submodule.equals("HR")) {
        String expenseFormViewID = (String) dynaform.get("hrExpensesForm");
        String timeSheetViewID = (String) dynaform.get("hrTimeSheets");
        String employeeHandbookViewID = (String) dynaform.get("hrEmployeeHandbook");
        String employeeViewID = (String) dynaform.get("hrEmployee");
        defaultViews.put("Expenses",expenseFormViewID);
        defaultViews.put("TimeSheet",timeSheetViewID);
        defaultViews.put("EmployeeHandbook",employeeHandbookViewID);
        defaultViews.put("Employee",employeeViewID);
      }

      // Set the New set of view according to the Module which we selected.
      if (defaultViews != null && defaultViews.size() != 0){
        ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome","View");
        View remote =(View)viewHome.create();
        remote.setDataSource(dataSource);
        // Set the view
        remote.setDefaultView(defaultViews);
      }

      if (button.equals("save")) {
        returnStatus = "save";
        request.setAttribute("typeofsubmodule",submodule);
        FORWARD_final = FORWARD_save;
      } else {
        returnStatus = "saveandclose";
        request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS);
        request.setAttribute("typeofsubmodule",submodule);
        FORWARD_final = FORWARD_saveandclose;
      }
    } catch (Exception e) {
      System.out.println("[Exception][SetDefaultViewHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
    }
    return mapping.findForward(FORWARD_final);
  }
}
