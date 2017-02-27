/*
 * $RCSfile: ActionUtil.java,v $    $Revision: 1.4 $  $Date: 2005/07/27 20:17:30 $ - $Author: mcallist $
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

package com.centraview.valuelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.ListPreference;
import com.centraview.common.ListView;

/**
 * This class will have utility methods for the ValueList actions.  Sorting, paging, etc.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ActionUtil
{
  // TODO We should probably find a way to use reflection to make these easier.
  /**
   * This method simply selects the right actionForward based on the valueList type
   * and the mapping in the struts config.  This way these mappings are centralized
   * and should aid in maintenance.
   * @param listType int representing a listType as defined in the ValueListConstants. 
   * @param mapping the ActionMapping.
   * @return ActionForward.
   */
  public static ActionForward listForward(int listType, ActionMapping mapping)
  {
    switch (listType) {
      case ValueListConstants.ENTITY_LIST_TYPE: return(mapping.findForward("entityList"));
      case ValueListConstants.INDIVIDUAL_LIST_TYPE: return(mapping.findForward("individualList"));
      case ValueListConstants.GROUP_LIST_TYPE: return(mapping.findForward("groupList"));
      case ValueListConstants.OPPORTUNITY_LIST_TYPE: return(mapping.findForward("opportunityList"));
      case ValueListConstants.PROPOSALS_LIST_TYPE: return(mapping.findForward("proposalsList"));
      case ValueListConstants.TEMPLATE_LIST_TYPE: return(mapping.findForward("templateList"));      
      case ValueListConstants.ACTIVITY_LIST_TYPE: return(mapping.findForward("activityList"));
      case ValueListConstants.APPOINTMENT_LIST_TYPE: return(mapping.findForward("appointmentList"));
      case ValueListConstants.CALL_LIST_TYPE: return(mapping.findForward("callList"));
      case ValueListConstants.FORECASTSALES_LIST_TYPE: return(mapping.findForward("forecastSalesList"));
      case ValueListConstants.LITERATUREREQUEST_LIST_TYPE: return(mapping.findForward("literatureRequestList"));
      case ValueListConstants.MEETING_LIST_TYPE: return(mapping.findForward("meetingList"));
      case ValueListConstants.NEXTACTION_LIST_TYPE: return(mapping.findForward("nextActionList"));
      case ValueListConstants.TODO_LIST_TYPE: return(mapping.findForward("todoList"));
      case ValueListConstants.TASK_LIST_TYPE: return(mapping.findForward("taskList"));
      case ValueListConstants.TICKET_LIST_TYPE: return(mapping.findForward("ticketList"));
      case ValueListConstants.EMAIL_LIST_TYPE: return(mapping.findForward("emailList"));
      case ValueListConstants.RULE_LIST_TYPE: return(mapping.findForward("rulesList"));
      case ValueListConstants.NOTE_LIST_TYPE: return(mapping.findForward("noteList"));
      case ValueListConstants.LISTMANAGER_LIST_TYPE: return(mapping.findForward("listManagerList"));
      case ValueListConstants.PROMOTION_LIST_TYPE: return(mapping.findForward("promotionList"));
      case ValueListConstants.LITERATUREFULFILLMENT_LIST_TYPE: return(mapping.findForward("literatureFulfillmentList"));
      case ValueListConstants.EVENT_LIST_TYPE: return(mapping.findForward("eventList"));
      case ValueListConstants.PROJECT_LIST_TYPE: return(mapping.findForward("projectsList"));
      case ValueListConstants.TASKS_LIST_TYPE: return(mapping.findForward("tasksList"));
      case ValueListConstants.TIMESLIPS_LIST_TYPE: return(mapping.findForward("timeslipsList"));
      case ValueListConstants.FAQ_LIST_TYPE: return(mapping.findForward("FAQList"));
      case ValueListConstants.KNOWLEDGEBASE_LIST_TYPE: return(mapping.findForward("knowledgeBaseList"));
      case ValueListConstants.ORDER_LIST_TYPE: return(mapping.findForward("orderList"));
      case ValueListConstants.INVOICE_LIST_TYPE: return(mapping.findForward("invoiceList"));
      case ValueListConstants.PAYMENT_LIST_TYPE: return(mapping.findForward("paymentList"));
      case ValueListConstants.EXPENSES_LIST_TYPE: return(mapping.findForward("expensesList"));
      case ValueListConstants.PURCHASEORDER_LIST_TYPE: return(mapping.findForward("purchaseorderList"));
      case ValueListConstants.ITEM_LIST_TYPE: return(mapping.findForward("itemList"));
      case ValueListConstants.GLACCOUNT_LIST_TYPE: return(mapping.findForward("glaccountList"));
      case ValueListConstants.INVENTORY_LIST_TYPE: return(mapping.findForward("inventoryList"));
      case ValueListConstants.VENDOR_LIST_TYPE: return(mapping.findForward("vendorList"));
      case ValueListConstants.FILE_LIST_TYPE: return(mapping.findForward("fileList"));
      case ValueListConstants.EXPENSEFORM_LIST_TYPE: return(mapping.findForward("expenseFormList"));
      case ValueListConstants.TIMESHEET_LIST_TYPE: return(mapping.findForward("timeSheetList"));
      case ValueListConstants.EMPLOYEE_HANDBOOK_LIST_TYPE: return(mapping.findForward("employeeHandbookList"));
      case ValueListConstants.EMPLOYEE_LIST_TYPE: return(mapping.findForward("employeeList"));
      case ValueListConstants.LISTMEMBER_LIST_TYPE: return(mapping.findForward("viewListManager"));
      case ValueListConstants.RELATEDNOTES_LIST_TYPE: return(mapping.findForward("relatedInfoList"));
      case ValueListConstants.EVENTATTENDEE_LIST_TYPE: return(mapping.findForward("viewEventList"));
      case ValueListConstants.GROUP_MEMBER_LIST_TYPE: return(mapping.findForward("groupMemberList"));
      case ValueListConstants.GROUP_LOOKUP_LIST_TYPE: return(mapping.findForward("groupLookupList"));
      case ValueListConstants.USER_LIST_TYPE: return(mapping.findForward("userList"));
      case ValueListConstants.CUSTOMER_EMAIL_LIST_TYPE: return(mapping.findForward("customerEmailList"));
      case ValueListConstants.CUSTOMER_ORDER_LIST_TYPE: return(mapping.findForward("customerOrderList"));
      case ValueListConstants.CUSTOMER_INVOICE_LIST_TYPE: return(mapping.findForward("customerInvoiceList"));
      case ValueListConstants.CUSTOMER_PAYMENT_LIST_TYPE: return(mapping.findForward("customerPaymentList"));
      case ValueListConstants.CUSTOMER_TICKET_LIST_TYPE: return(mapping.findForward("customerTicketList"));
      case ValueListConstants.CUSTOMER_FAQ_LIST_TYPE: return(mapping.findForward("customerFaqList"));
      case ValueListConstants.CUSTOMER_EVENT_LIST_TYPE: return(mapping.findForward("customerEventList"));
      case ValueListConstants.CUSTOMER_USER_LIST_TYPE: return(mapping.findForward("customerUserList"));
      case ValueListConstants.CUSTOMER_FILE_LIST_TYPE: return(mapping.findForward("customerFileList"));
      case ValueListConstants.STANDARD_REPORT_LIST_TYPE: return(mapping.findForward("standardReportList"));
      case ValueListConstants.LITERATURE_LIST_TYPE: return(mapping.findForward("literatureList"));
      case ValueListConstants.CUSTOM_VIEW_LIST_TYPE: return(mapping.findForward("customViewList"));
      case ValueListConstants.GARBAGE_LIST_TYPE: return(mapping.findForward("garbageList"));
      case ValueListConstants.ATTIC_LIST_TYPE: return(mapping.findForward("atticList"));
      case ValueListConstants.ADHOC_REPORT_LIST_TYPE: return(mapping.findForward("adhocReportList"));
      case ValueListConstants.DATA_HISTORY_LIST_TYPE: return(mapping.findForward("historyList"));
      case ValueListConstants.SECURITY_PROFILE_LIST_TYPE: return(mapping.findForward("securityProfileList"));
      case ValueListConstants.BOTTOM_INDIVIDUAL_LIST_TYPE: return(mapping.findForward("individualList"));
      case ValueListConstants.ADDRESSLOOKUP_LIST_TYPE: return(mapping.findForward("addresslookupList"));
      case ValueListConstants.EMAIL_LOOKUP_LIST_TYPE: return(mapping.findForward("emailAddressLookupList"));
      default: return(mapping.findForward("global_error"));
    }
  }
  
  public static void mapOldView(ArrayList columns, Vector viewColumnNames, int type)
  {
    for (int i = 0; i < viewColumnNames.size(); i++) {
      String columnName = (String)viewColumnNames.get(i);
      switch(type) {
        case ValueListConstants.ENTITY_LIST_TYPE: columns.add(ValueListConstants.entityViewMap.get(columnName)); break;
        case ValueListConstants.INDIVIDUAL_LIST_TYPE: columns.add(ValueListConstants.individualViewMap.get(columnName)); break;
        case ValueListConstants.GROUP_LIST_TYPE: columns.add(ValueListConstants.groupViewMap.get(columnName)); break;
        case ValueListConstants.OPPORTUNITY_LIST_TYPE: columns.add(ValueListConstants.opportunityViewMap.get(columnName)); break;
        case ValueListConstants.PROPOSALS_LIST_TYPE: columns.add(ValueListConstants.proposalsViewMap.get(columnName)); break;
        case ValueListConstants.TEMPLATE_LIST_TYPE: columns.add(ValueListConstants.templateViewMap.get(columnName)); break;
        case ValueListConstants.ACTIVITY_LIST_TYPE: columns.add(ValueListConstants.activityViewMap.get(columnName)); break;
        case ValueListConstants.APPOINTMENT_LIST_TYPE: columns.add(ValueListConstants.appointmentViewMap.get(columnName)); break;
        case ValueListConstants.CALL_LIST_TYPE: columns.add(ValueListConstants.callViewMap.get(columnName)); break;
        case ValueListConstants.FORECASTSALES_LIST_TYPE: columns.add(ValueListConstants.forecastSalesViewMap.get(columnName)); break;
        case ValueListConstants.LITERATUREREQUEST_LIST_TYPE: columns.add(ValueListConstants.literatureRequestViewMap.get(columnName)); break;
        case ValueListConstants.MEETING_LIST_TYPE: columns.add(ValueListConstants.meetingViewMap.get(columnName)); break;
        case ValueListConstants.NEXTACTION_LIST_TYPE: columns.add(ValueListConstants.nextActionViewMap.get(columnName)); break;
        case ValueListConstants.TODO_LIST_TYPE: columns.add(ValueListConstants.todoViewMap.get(columnName)); break;
        case ValueListConstants.TASK_LIST_TYPE: columns.add(ValueListConstants.taskViewMap.get(columnName)); break;
        case ValueListConstants.TICKET_LIST_TYPE: columns.add(ValueListConstants.ticketViewMap.get(columnName)); break;
        case ValueListConstants.EMAIL_LIST_TYPE: columns.add(ValueListConstants.emailViewMap.get(columnName)); break;
        case ValueListConstants.RULE_LIST_TYPE: columns.add(ValueListConstants.ruleViewMap.get(columnName)); break;
        case ValueListConstants.NOTE_LIST_TYPE: columns.add(ValueListConstants.noteViewMap.get(columnName)); break;
        case ValueListConstants.LISTMANAGER_LIST_TYPE: columns.add(ValueListConstants.listManagerViewMap.get(columnName)); break;
        case ValueListConstants.PROMOTION_LIST_TYPE: columns.add(ValueListConstants.promotionViewMap.get(columnName)); break;
        case ValueListConstants.LITERATUREFULFILLMENT_LIST_TYPE: columns.add(ValueListConstants.literatureRequestViewMap.get(columnName)); break;
        case ValueListConstants.EVENT_LIST_TYPE: columns.add(ValueListConstants.eventViewMap.get(columnName)); break;
        case ValueListConstants.PROJECT_LIST_TYPE: columns.add(ValueListConstants.projectViewMap.get(columnName)); break;
        case ValueListConstants.TASKS_LIST_TYPE: columns.add(ValueListConstants.tasksViewMap.get(columnName)); break;
        case ValueListConstants.TIMESLIPS_LIST_TYPE: columns.add(ValueListConstants.timeslipsViewMap.get(columnName)); break;
        case ValueListConstants.FAQ_LIST_TYPE: columns.add(ValueListConstants.FAQViewMap.get(columnName)); break;
        case ValueListConstants.ORDER_LIST_TYPE: columns.add(ValueListConstants.orderViewMap.get(columnName)); break;
        case ValueListConstants.INVOICE_LIST_TYPE: columns.add(ValueListConstants.invoiceViewMap.get(columnName)); break;
        case ValueListConstants.PAYMENT_LIST_TYPE: columns.add(ValueListConstants.paymentViewMap.get(columnName)); break;
        case ValueListConstants.EXPENSES_LIST_TYPE: columns.add(ValueListConstants.expensesViewMap.get(columnName)); break;
        case ValueListConstants.PURCHASEORDER_LIST_TYPE: columns.add(ValueListConstants.purchaseorderViewMap.get(columnName)); break;
        case ValueListConstants.ITEM_LIST_TYPE: columns.add(ValueListConstants.itemViewMap.get(columnName)); break;
        case ValueListConstants.GLACCOUNT_LIST_TYPE: columns.add(ValueListConstants.glaccountViewMap.get(columnName)); break;
        case ValueListConstants.INVENTORY_LIST_TYPE: columns.add(ValueListConstants.inventoryViewMap.get(columnName)); break;
        case ValueListConstants.VENDOR_LIST_TYPE: columns.add(ValueListConstants.vendorViewMap.get(columnName)); break;
        case ValueListConstants.KNOWLEDGEBASE_LIST_TYPE: columns.add(ValueListConstants.knowledgeBaseViewMap.get(columnName)); break;
        case ValueListConstants.FILE_LIST_TYPE: columns.add(ValueListConstants.fileViewMap.get(columnName)); break;
        case ValueListConstants.EXPENSEFORM_LIST_TYPE: columns.add(ValueListConstants.expenseFormViewMap.get(columnName)); break;
        case ValueListConstants.MOC_LIST_TYPE: columns.add(ValueListConstants.mocViewMap.get(columnName)); break;
        case ValueListConstants.ADDRESS_LIST_TYPE: columns.add(ValueListConstants.addressViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOM_FIELD_RI_LIST_TYPE: columns.add(ValueListConstants.customFieldRIViewMap.get(columnName)); break;
        case ValueListConstants.HISTORY_LIST_TYPE: columns.add(ValueListConstants.historyViewMap.get(columnName)); break;
        case ValueListConstants.TIMESHEET_LIST_TYPE: columns.add(ValueListConstants.timeSheetViewMap.get(columnName)); break;
        case ValueListConstants.EMPLOYEE_HANDBOOK_LIST_TYPE: columns.add(ValueListConstants.employeeHandbookViewMap.get(columnName)); break;
        case ValueListConstants.EMPLOYEE_LIST_TYPE: columns.add(ValueListConstants.employeeListViewMap.get(columnName)); break;
        case ValueListConstants.LISTMEMBER_LIST_TYPE: columns.add(ValueListConstants.listMemberViewMap.get(columnName)); break;
        case ValueListConstants.RELATEDNOTES_LIST_TYPE: columns.add(ValueListConstants.noteViewMap.get(columnName)); break;
        case ValueListConstants.EMAIL_LOOKUP_LIST_TYPE: columns.add(ValueListConstants.emailLookupViewMap.get(columnName)); break;
        case ValueListConstants.EVENTATTENDEE_LIST_TYPE: columns.add(ValueListConstants.eventAttendeeViewMap.get(columnName)); break;
        case ValueListConstants.SOURCE_LIST_TYPE: columns.add(ValueListConstants.sourceViewMap.get(columnName)); break;
        case ValueListConstants.GROUP_LOOKUP_LIST_TYPE: columns.add(ValueListConstants.groupLookupViewMap.get(columnName)); break;
        case ValueListConstants.USER_LIST_TYPE: columns.add(ValueListConstants.userViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_EMAIL_LIST_TYPE: columns.add(ValueListConstants.customerEmailViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_ORDER_LIST_TYPE: columns.add(ValueListConstants.customerOrderViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_INVOICE_LIST_TYPE: columns.add(ValueListConstants.customerInvoiceViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_PAYMENT_LIST_TYPE: columns.add(ValueListConstants.customerPaymentViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_TICKET_LIST_TYPE: columns.add(ValueListConstants.customerTicketViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_FAQ_LIST_TYPE: columns.add(ValueListConstants.customerFaqViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_EVENT_LIST_TYPE: columns.add(ValueListConstants.customerEventViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_USER_LIST_TYPE: columns.add(ValueListConstants.customerUserViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOMER_FILE_LIST_TYPE: columns.add(ValueListConstants.customerFileViewMap.get(columnName)); break;
        case ValueListConstants.STANDARD_REPORT_LIST_TYPE: columns.add(ValueListConstants.standardReportViewMap.get(columnName)); break;
        case ValueListConstants.LITERATURE_LIST_TYPE: columns.add(ValueListConstants.literatureListViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOM_VIEW_LIST_TYPE: columns.add(ValueListConstants.customViewListViewMap.get(columnName)); break;
        case ValueListConstants.GARBAGE_LIST_TYPE: columns.add(ValueListConstants.garbageListViewMap.get(columnName)); break;
        case ValueListConstants.ATTIC_LIST_TYPE: columns.add(ValueListConstants.atticListViewMap.get(columnName)); break;
        case ValueListConstants.ADHOC_REPORT_LIST_TYPE: columns.add(ValueListConstants.adhocReportViewMap.get(columnName)); break;
        case ValueListConstants.DATA_HISTORY_LIST_TYPE: columns.add(ValueListConstants.historyListViewMap.get(columnName)); break;
        case ValueListConstants.GROUP_MEMBER_LIST_TYPE: columns.add(ValueListConstants.individualViewMap.get(columnName)); break;
        case ValueListConstants.SECURITY_PROFILE_LIST_TYPE: columns.add(ValueListConstants.securityProfileListViewMap.get(columnName)); break;
        case ValueListConstants.BOTTOM_INDIVIDUAL_LIST_TYPE: columns.add(ValueListConstants.bottomIndividualViewMap.get(columnName)); break;
        case ValueListConstants.CUSTOM_FIELDS_LIST_TYPE: columns.add(ValueListConstants.customFieldsListViewMap.get(columnName)); break;
        case ValueListConstants.ADDRESSLOOKUP_LIST_TYPE: columns.add(ValueListConstants.addressLookupViewMap.get(columnName)); break;        
      }
    }
  }
  
  /**
   * This method will simplify the list handlers greatly by doing the generic sort, 
   * column selection, and search stuff.  Any stuff that is really custom to the particular list
   * should remain in that handler.
   * 
   * @param listPreference the listpreference object that is on the userObject.
   * @param request the HttpRequest to look for the right values
   * @param listType an Integer mapped in ValueListConstants.
   * @param viewMap a hashmap that maps up field descriptor objects to column names.
   * @param doFilter a boolean to determine wether or not to do search and filter.
   * @return a ValueListParameters object.
   */
  public static ValueListParameters valueListParametersSetUp(ListPreference listPreference, HttpServletRequest request, int listType, HashMap viewMap, boolean doFilter)
  {
    HttpSession session = request.getSession(true);
    // Handle Paging
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) {
      // build up new Parameters
      listParameters = new ValueListParameters(listType, listPreference.getRecordsPerPage(), 1);
    } else {
      // paging or sorting or something, use the parameters from the request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)viewMap.get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }
    // Handle Searches
    if (doFilter) {
      // Instead of adding a new method, we'll just F this one up.
      String filter = null;
      String filterParameter = request.getParameter("filter"); 
      if (filterParameter != null) {
        filter = (String)session.getAttribute("listFilter");
        request.setAttribute("appliedSearch", filterParameter);
      } else {
        session.removeAttribute("listFilter");
      }
      listParameters.setFilter(filter);
    }
    // Handle columns from the view.
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    ActionUtil.mapOldView(columns, viewColumns, listType);
    listParameters.setColumns(columns);
    return listParameters;
  }

}

