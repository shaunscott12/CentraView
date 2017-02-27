/*
 * $RCSfile: ValueListConstants.java,v $    $Revision: 1.8 $  $Date: 2005/08/19 20:20:27 $ - $Author: mcallist $
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

/**
 * This class contains a number of static fields to serve as constants in the
 * ValueListCode. For every list type there should be a constant entry in here
 * and a listTypeMapping entry.  It also serves to store the HTML tag elements
 * for building our display table.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ValueListConstants
{
  // the index in the entityList
  public static final int ICON_INDEX = 0;
  public static final int ICON_INDEX_FOLDER = -1;
  public static final int ICON_INDEX_EMAIL_TEMPLATES = -2;

  public static final int INVOICE_AMOUNT = 8;
  
  public static final int ITEM_CHILD_COUNT_INDEX = 14;
  public static final int ORDER_INDEX = 1;

  public static final int ACTIVITY_TITLE_INDEX = 2;
  public static final int ACTIVITY_STATUS_INDEX = 3;
  public static final int KB_FILE_EMAIL_HACK_INDEX = 2;
  
  /** Email Icon Index Value to identify wheather message is read or not */ 
  public static final int EMAIL_ICON = 8;
  /** Priority Icon Index Value */
  public static final int PRIORITY_ICON = 7;
  /** Attachment Icon Index Value */
  public static final int ATTACHMENT_ICON = 10;

  /** Download icon index value */
  public static final int DOWNLOAD_ICON = 2;
  /** Download file link */
  public static final String DOWNLOAD_LINK = "/files/file_download.do?fileid=";
  /** Download file column id */
  public static final int DOWNLOAD_COLUMN = 1;
  
  // These are for email lookups
  public static final int TO_INDEX = -1;
  public static final int CC_INDEX = -2;
  public static final int BCC_INDEX = -3;
  
  // Yet Another Special Case (YASC): Security Profile Lookup
  public static final int SECURITY_PROFILE = -4;

  // File Lookup
  public static final int FILE_INDEX = -5;
  
  //Special Case for Attendee's Lookup Special Index Value
  public static final ArrayList radioSelectedIndexAttendee = new ArrayList();
  public static final int ATTENDEEROWID = 1;
  public static final int ATTENDEEFIRSTNAME = 4;
  public static final int ATTENDEELASTNAME = 6;
  
  
  // The following constants map up Names to ListIds
  public static final int ENTITY_LIST_TYPE = 1;
  public static final int INDIVIDUAL_LIST_TYPE = 2;
  public static final int GROUP_LIST_TYPE = 3;
  public static final int OPPORTUNITY_LIST_TYPE = 4;
  public static final int PROPOSALS_LIST_TYPE = 5;
  public static final int ACTIVITY_LIST_TYPE = 6;
  public static final int APPOINTMENT_LIST_TYPE = 7;
  public static final int CALL_LIST_TYPE = 8;
  public static final int FORECASTSALES_LIST_TYPE = 9;
  public static final int LITERATUREREQUEST_LIST_TYPE = 10;
  public static final int MEETING_LIST_TYPE = 11;
  public static final int NEXTACTION_LIST_TYPE = 12;
  public static final int TODO_LIST_TYPE = 13;
  public static final int TASK_LIST_TYPE = 14;
  public static final int TICKET_LIST_TYPE = 15;
  public static final int EMAIL_LIST_TYPE = 16;
  public static final int RULE_LIST_TYPE = 17;
  public static final int TEMPLATE_LIST_TYPE = 18;
  public static final int NOTE_LIST_TYPE = 19;  
  public static final int LISTMANAGER_LIST_TYPE = 20;  
  public static final int PROMOTION_LIST_TYPE = 21;  
  public static final int EVENT_LIST_TYPE = 22;
  public static final int PROJECT_LIST_TYPE = 23;
  public static final int TASKS_LIST_TYPE = 24;  
  public static final int TIMESLIPS_LIST_TYPE = 25;  
  public static final int LITERATUREFULFILLMENT_LIST_TYPE = 26;
  public static final int FAQ_LIST_TYPE = 27;
  public static final int ORDER_LIST_TYPE = 28;
  public static final int INVOICE_LIST_TYPE = 29;
  public static final int PAYMENT_LIST_TYPE = 30;
  public static final int EXPENSES_LIST_TYPE = 31;
  public static final int PURCHASEORDER_LIST_TYPE = 32;
  public static final int ITEM_LIST_TYPE = 33;
  public static final int GLACCOUNT_LIST_TYPE = 34;
  public static final int INVENTORY_LIST_TYPE = 35;
  public static final int VENDOR_LIST_TYPE = 36;
  public static final int KNOWLEDGEBASE_LIST_TYPE = 37;
  public static final int FILE_LIST_TYPE = 38;
  public static final int MOC_LIST_TYPE = 39;
  public static final int ADDRESS_LIST_TYPE = 40;
  public static final int EXPENSEFORM_LIST_TYPE = 41;
  public static final int CUSTOM_FIELD_RI_LIST_TYPE = 42;
  public static final int HISTORY_LIST_TYPE = 43;
  public static final int TIMESHEET_LIST_TYPE = 44;
  public static final int EMPLOYEE_HANDBOOK_LIST_TYPE = 45;
  public static final int EMPLOYEE_LIST_TYPE = 46;
  public static final int LISTMEMBER_LIST_TYPE = 47;
  public static final int RELATEDNOTES_LIST_TYPE = 48;
  public static final int EMAIL_LOOKUP_LIST_TYPE = 49;
  public static final int EVENTATTENDEE_LIST_TYPE = 50;
  public static final int SOURCE_LIST_TYPE = 51;
  /** this list is a duplicate of individual list.  Just using this for paging mappings */
  public static final int GROUP_MEMBER_LIST_TYPE = 52;
  public static final int GROUP_LOOKUP_LIST_TYPE = 53;
  public static final int USER_LIST_TYPE = 54;
  public static final int CUSTOMER_EMAIL_LIST_TYPE = 55;
  public static final int CUSTOMER_ORDER_LIST_TYPE = 56;
  public static final int CUSTOMER_INVOICE_LIST_TYPE = 57;
  public static final int CUSTOMER_PAYMENT_LIST_TYPE = 58;
  public static final int CUSTOMER_TICKET_LIST_TYPE = 59;
  public static final int CUSTOMER_FAQ_LIST_TYPE = 60;
  public static final int CUSTOMER_EVENT_LIST_TYPE = 61;
  public static final int CUSTOMER_USER_LIST_TYPE = 62;
  public static final int CUSTOMER_FILE_LIST_TYPE = 63;
  public static final int LOCATION_LIST_TYPE = 64;
  public static final int STANDARD_REPORT_LIST_TYPE = 65;
  public static final int LITERATURE_LIST_TYPE = 66;
  public static final int CUSTOM_VIEW_LIST_TYPE = 67;
  public static final int GARBAGE_LIST_TYPE = 68;
  public static final int ATTIC_LIST_TYPE = 69;
  public static final int ADHOC_REPORT_LIST_TYPE = 70;
  public static final int DATA_HISTORY_LIST_TYPE = 71;
  public static final int SECURITY_PROFILE_LIST_TYPE = 72;
  public static final int BOTTOM_INDIVIDUAL_LIST_TYPE = 73;
  public static final int CUSTOM_FIELDS_LIST_TYPE = 74;
  public static final int ADDRESSLOOKUP_LIST_TYPE = 75;
  
  // List decorator Definitions
  public static final HashMap decoratorData = new HashMap();
  /** Mapping from Name to FieldDescriptor */
  public static final HashMap entityViewMap = new HashMap();
  public static final HashMap individualViewMap = new HashMap();
  public static final HashMap groupViewMap = new HashMap();
  public static final HashMap templateViewMap = new HashMap();
  public static final HashMap opportunityViewMap = new HashMap();
  public static final HashMap proposalsViewMap = new HashMap();
  public static final HashMap activityViewMap = new HashMap();
  public static final HashMap appointmentViewMap = new HashMap();
  public static final HashMap callViewMap = new HashMap();
  public static final HashMap forecastSalesViewMap = new HashMap();
  public static final HashMap literatureRequestViewMap = new HashMap();
  public static final HashMap meetingViewMap = new HashMap();
  public static final HashMap nextActionViewMap = new HashMap();
  public static final HashMap todoViewMap = new HashMap();
  public static final HashMap taskViewMap = new HashMap();
  public static final HashMap moduleIdMap = new HashMap();
  public static final HashMap listTypeMap = new HashMap();
  public static final HashMap ticketViewMap = new HashMap();
  public static final HashMap emailViewMap = new HashMap();
  public static final HashMap ruleViewMap = new HashMap();
  public static final HashMap noteViewMap = new HashMap();
  public static final HashMap listManagerViewMap = new HashMap();
  public static final HashMap promotionViewMap = new HashMap();
  public static final HashMap literatureFulfillmentViewMap = literatureRequestViewMap;
  public static final HashMap eventViewMap = new HashMap();
  public static final HashMap projectViewMap = new HashMap();
  public static final HashMap tasksViewMap = new HashMap();
  public static final HashMap timeslipsViewMap = new HashMap();
  public static final HashMap FAQViewMap = new HashMap();
  public static final HashMap knowledgeBaseViewMap = new HashMap();
  public static final HashMap orderViewMap = new HashMap();
  public static final HashMap invoiceViewMap = new HashMap();
  public static final HashMap paymentViewMap = new HashMap();
  public static final HashMap expensesViewMap = new HashMap();
  public static final HashMap purchaseorderViewMap = new HashMap();
  public static final HashMap itemViewMap = new HashMap();
  public static final HashMap glaccountViewMap = new HashMap();
  public static final HashMap inventoryViewMap = new HashMap();
  public static final HashMap vendorViewMap = new HashMap();
  public static final HashMap fileViewMap = new HashMap();
  public static final HashMap mocViewMap = new HashMap();
  public static final HashMap addressViewMap = new HashMap();
  public static final HashMap expenseFormViewMap = new HashMap();
  public static final HashMap customFieldRIViewMap = new HashMap();
  public static final HashMap historyViewMap = new HashMap();
  public static final HashMap timeSheetViewMap = new HashMap();
  public static final HashMap employeeHandbookViewMap = new HashMap();
  public static final HashMap employeeListViewMap = new HashMap();
  public static final HashMap listMemberViewMap = new HashMap();
  public static final HashMap emailLookupViewMap = new HashMap();
  public static final HashMap eventAttendeeViewMap = new HashMap();
  public static final HashMap sourceViewMap = new HashMap();
  public static final HashMap groupLookupViewMap = groupViewMap;
  public static final HashMap userViewMap = new HashMap();
  public static final HashMap customerEmailViewMap = emailViewMap;
  public static final HashMap customerOrderViewMap = orderViewMap;
  public static final HashMap customerInvoiceViewMap = invoiceViewMap;
  public static final HashMap customerPaymentViewMap = paymentViewMap;
  public static final HashMap customerTicketViewMap = ticketViewMap;
  public static final HashMap customerFaqViewMap = FAQViewMap;
  public static final HashMap customerEventViewMap = eventViewMap;
  public static final HashMap customerUserViewMap = userViewMap;
  public static final HashMap customerFileViewMap = fileViewMap;
  public static final HashMap locationViewMap = new HashMap();
  public static final HashMap standardReportViewMap = new HashMap();
  public static final HashMap literatureListViewMap = new HashMap();
  public static final HashMap customViewListViewMap = new HashMap();
  public static final HashMap garbageListViewMap = new HashMap();
  public static final HashMap atticListViewMap = new HashMap();
  public static final HashMap adhocReportViewMap = new HashMap();
  public static final HashMap historyListViewMap = new HashMap();
  public static final HashMap securityProfileListViewMap = new HashMap();
  public static final HashMap bottomIndividualViewMap = new HashMap();
  public static final HashMap customFieldsListViewMap = new HashMap();
  public static final HashMap addressLookupViewMap = new HashMap();
  
  // Calculation of Duration column in timeSlip List
  public static final HashMap timeslipDurationCalc = new HashMap();
  
  public static final HashMap itemLookupMap = new HashMap();
  /** Different emailIcon according to value in column */
  public static final HashMap emailIconMap = new HashMap();
  
  //Mapping from select column 
  public static final HashMap RADIO_SELECTION_MAP = new HashMap();
  static
  {
    HashMap entityList = new HashMap();
    entityList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 1, FieldDecoration.urlType));
    entityList.put(new Integer(2), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 1, FieldDecoration.urlType));
    entityList.put(new Integer(7), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 6, FieldDecoration.urlType));
    entityList.put(new Integer(9), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','',720,585,'');", 9, FieldDecoration.urlType));
    entityList.put(new Integer(10), new FieldDecoration("c_openWindowWithTool('", "');", 10, FieldDecoration.urlType));
    decoratorData.put(new Integer(ENTITY_LIST_TYPE), entityList);
    decoratorData.put(new Integer(LISTMEMBER_LIST_TYPE), entityList);
    
    HashMap individualList = new HashMap();
    individualList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    individualList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    individualList.put(new Integer(9), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 8, FieldDecoration.urlType));
    individualList.put(new Integer(18), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','',720,585,'');", 18, FieldDecoration.urlType));
    decoratorData.put(new Integer(INDIVIDUAL_LIST_TYPE), individualList);
    decoratorData.put(new Integer(GROUP_MEMBER_LIST_TYPE), individualList);

    HashMap bottomIndividualList = new HashMap();
    bottomIndividualList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openInParentFrame('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    bottomIndividualList.put(new Integer(3), new FieldDecoration("c_openInParentFrame('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    bottomIndividualList.put(new Integer(9), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 8, FieldDecoration.urlType));
    bottomIndividualList.put(new Integer(18), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','',720,585,'');", 18, FieldDecoration.urlType));
    decoratorData.put(new Integer(BOTTOM_INDIVIDUAL_LIST_TYPE), bottomIndividualList);


    HashMap groupList = new HashMap();
    groupList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/contacts/view_group.do?rowId=", "');", 1, FieldDecoration.urlType));
    groupList.put(new Integer(2), new FieldDecoration("c_goTo('/contacts/view_group.do?rowId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(GROUP_LIST_TYPE), groupList);
    decoratorData.put(new Integer(GROUP_LOOKUP_LIST_TYPE), new HashMap());

    HashMap opportunityList = new HashMap();
    opportunityList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&OPPORTUNITYID=", "');", 1, FieldDecoration.urlType));
    opportunityList.put(new Integer(2), new FieldDecoration("c_openPopup('/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&OPPORTUNITYID=", "');", 1, FieldDecoration.urlType));
    opportunityList.put(new Integer(7), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 6, FieldDecoration.urlType));
    opportunityList.put(new Integer(16), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 15, FieldDecoration.urlType));
    opportunityList.put(new Integer(12), new FieldDecoration("", "", 12, FieldDecoration.dateType));
    opportunityList.put(new Integer(13), new FieldDecoration("", "", 13, FieldDecoration.moneyType));
    decoratorData.put(new Integer(OPPORTUNITY_LIST_TYPE), opportunityList);

    HashMap templateListDecorator = new HashMap();
    templateListDecorator.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/administration/template_detail.do?templateId=", "');", 1, FieldDecoration.urlType));
    templateListDecorator.put(new Integer(2), new FieldDecoration("c_goTo('/administration/template_detail.do?templateId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(TEMPLATE_LIST_TYPE), templateListDecorator);
    
    HashMap proposalsList = new HashMap();
    proposalsList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openWindow('/sales/view_proposal.do?TYPEOFOPERATION=EDIT&eventid=", "', 'Proposal', 780, 580, 'scrollbars=yes, resizable=yes');", 1, FieldDecoration.urlType));
    proposalsList.put(new Integer(2), new FieldDecoration("c_openWindow('/sales/view_proposal.do?TYPEOFOPERATION=EDIT&eventid=", "', 'Proposal', 780, 580, 'scrollbars=yes, resizable=yes');", 1, FieldDecoration.urlType));
    proposalsList.put(new Integer(11), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 10, FieldDecoration.urlType));
    proposalsList.put(new Integer(8), new FieldDecoration("", "", 8, FieldDecoration.dateType));
    decoratorData.put(new Integer(PROPOSALS_LIST_TYPE), proposalsList);

    HashMap activityList = new HashMap();
    activityList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/activities/view_activity.do?rowId=", "');", 1, FieldDecoration.urlType));
    activityList.put(new Integer(ACTIVITY_TITLE_INDEX), new FieldDecoration("c_openPopup('/activities/view_activity.do?rowId=", "');", 1, FieldDecoration.urlType));
    activityList.put(new Integer(8), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 4, FieldDecoration.urlType));
    activityList.put(new Integer(14), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 16, FieldDecoration.urlType));
    activityList.put(new Integer(13), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 15, FieldDecoration.urlType));
    activityList.put(new Integer(18), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 17, FieldDecoration.urlType));
    activityList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.dateTimeType));
    activityList.put(new Integer(10), new FieldDecoration("", "", 10, FieldDecoration.dateTimeType));
    activityList.put(new Integer(11), new FieldDecoration("", "", 11, FieldDecoration.dateTimeType));
    activityList.put(new Integer(7), new FieldDecoration(7, 65, FieldDecoration.limitCharsType));   // field index, number of characters, decorator type
    decoratorData.put(new Integer(ACTIVITY_LIST_TYPE), activityList);
    decoratorData.put(new Integer(APPOINTMENT_LIST_TYPE), activityList);
    decoratorData.put(new Integer(CALL_LIST_TYPE), activityList);
    decoratorData.put(new Integer(MEETING_LIST_TYPE), activityList);
    decoratorData.put(new Integer(NEXTACTION_LIST_TYPE), activityList);
    decoratorData.put(new Integer(TODO_LIST_TYPE), activityList);
    
    HashMap forecastSalesList = new HashMap();
    forecastSalesList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/activities/view_activity.do?rowId=", "');", 14, FieldDecoration.urlType));
    forecastSalesList.put(new Integer(ACTIVITY_TITLE_INDEX), new FieldDecoration("c_openPopup('/activities/view_activity.do?rowId=", "');", 14, FieldDecoration.urlType));
    forecastSalesList.put(new Integer(16), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 15, FieldDecoration.urlType));
    forecastSalesList.put(new Integer(12), new FieldDecoration("", "", 12, FieldDecoration.dateType));
    forecastSalesList.put(new Integer(13), new FieldDecoration("", "", 13, FieldDecoration.moneyType));    
    decoratorData.put(new Integer(FORECASTSALES_LIST_TYPE), forecastSalesList);
    
    HashMap literatureRequestList = new HashMap();
    literatureRequestList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=EDIT&activityid=", "');", 1, FieldDecoration.urlType));
    literatureRequestList.put(new Integer(ACTIVITY_TITLE_INDEX), new FieldDecoration("c_openPopup('/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=EDIT&activityid=", "');", 1, FieldDecoration.urlType));
    literatureRequestList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 4, FieldDecoration.urlType));
    literatureRequestList.put(new Integer(8), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 9, FieldDecoration.urlType));
    literatureRequestList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(LITERATUREREQUEST_LIST_TYPE), literatureRequestList);

    HashMap taskList = new HashMap();
    taskList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/activities/view_activity.do?rowId=", "');", 1, FieldDecoration.urlType));
    taskList.put(new Integer(ACTIVITY_TITLE_INDEX), new FieldDecoration("c_openPopup('/activities/view_activity.do?rowId=", "');", 1, FieldDecoration.urlType));
    taskList.put(new Integer(14), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 13, FieldDecoration.urlType));
    taskList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.dateType));
    taskList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.dateType));
    taskList.put(new Integer(15), new FieldDecoration("", "", 15, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(TASK_LIST_TYPE), taskList);

    HashMap ticketList = new HashMap();
    ticketList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openWindow('/support/view_ticket.do?TYPEOFOPERATION=EDIT&rowId=", "', '', 745, 580, '');", 1, FieldDecoration.urlType));
    ticketList.put(new Integer(2), new FieldDecoration("c_openWindow('/support/view_ticket.do?TYPEOFOPERATION=EDIT&rowId=", "', '', 745, 580, '');", 1, FieldDecoration.urlType));
    ticketList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 4, FieldDecoration.urlType));
    ticketList.put(new Integer(10), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 7, FieldDecoration.urlType));
    decoratorData.put(new Integer(TICKET_LIST_TYPE), ticketList);

    HashMap emailList = new HashMap();
    emailList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/email/view_message.do?messageID=", "');", 1, FieldDecoration.urlType));
    emailList.put(new Integer(ICON_INDEX_FOLDER), new FieldDecoration("c_openWindow('/email/view_draft.do?messageID=", "','', 'edit_draft', 720, 585, '');", 1, FieldDecoration.urlType));
    emailList.put(new Integer(ICON_INDEX_EMAIL_TEMPLATES), new FieldDecoration("c_openWindow('/email/view_template.do?messageID=", "','', 720, 585,'');", 1, FieldDecoration.urlType));
    emailList.put(new Integer(2), new FieldDecoration("c_goTo('/email/view_message.do?messageID=", "');", 1, FieldDecoration.urlType));
    emailList.put(new Integer(4), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','', 720, 585,'');", 4, FieldDecoration.urlType));
    emailList.put(new Integer(11), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','', 720, 585,'');", 11, FieldDecoration.urlType));
    emailList.put(new Integer(3), new FieldDecoration("", "", 3, FieldDecoration.dateTimeType));
    emailList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.fileSizeType));
    decoratorData.put(new Integer(EMAIL_LIST_TYPE), emailList);

    HashMap ruleList = new HashMap();
    ruleList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/email/view_rule.do?ruleID=", "');", 1, FieldDecoration.urlType));
    ruleList.put(new Integer(2), new FieldDecoration("c_goTo('/email/view_rule.do?ruleID=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(RULE_LIST_TYPE), ruleList);

    HashMap noteList = new HashMap();
    noteList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/notes/view_note.do?TYPEOFOPERATION=EDIT&rowId=", "');", 1, FieldDecoration.urlType));
    noteList.put(new Integer(2), new FieldDecoration("c_goTo('/notes/view_note.do?TYPEOFOPERATION=EDIT&rowId=", "');", 1, FieldDecoration.urlType));
    noteList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "')", 7, FieldDecoration.urlType));
    noteList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(NOTE_LIST_TYPE), noteList);

    HashMap relatedNoteList = new HashMap();
    relatedNoteList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/notes/view_note.do?TYPEOFOPERATION=EDIT&bottomFrame=true&rowId=", "');", 1, FieldDecoration.urlType));
    relatedNoteList.put(new Integer(2), new FieldDecoration("c_goTo('/notes/view_note.do?TYPEOFOPERATION=EDIT&bottomFrame=true&rowId=", "');", 1, FieldDecoration.urlType));
    relatedNoteList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "')", 7, FieldDecoration.urlType));
    relatedNoteList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(RELATEDNOTES_LIST_TYPE), relatedNoteList);

    HashMap listManagerList = new HashMap();
    listManagerList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/marketing/view_listmanager.do?rowId=", "');", 1, FieldDecoration.urlType));
    listManagerList.put(new Integer(2), new FieldDecoration("c_goTo('/marketing/view_listmanager.do?rowId=", "');", 1, FieldDecoration.urlType));
    listManagerList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.moneyType));
    listManagerList.put(new Integer(9), new FieldDecoration("", "", 9, FieldDecoration.moneyType));
    decoratorData.put(new Integer(LISTMANAGER_LIST_TYPE), listManagerList);
    
    HashMap promotionList = new HashMap();
    promotionList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/marketing/view_promotion.do?promotionid=", "');", 1, FieldDecoration.urlType));
    promotionList.put(new Integer(2), new FieldDecoration("c_goTo('/marketing/view_promotion.do?promotionid=", "');", 1, FieldDecoration.urlType));
    promotionList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateType));
    promotionList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.dateType));
    decoratorData.put(new Integer(PROMOTION_LIST_TYPE), promotionList);

    HashMap literatureFulfillmentList = literatureRequestList;
    decoratorData.put(new Integer(LITERATUREFULFILLMENT_LIST_TYPE), literatureFulfillmentList);
    
    HashMap eventList = new HashMap();
    eventList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/marketing/view_event.do?TYPEOFOPERATION=EDIT&FromAttendee=NO&eventid=", "');", 1, FieldDecoration.urlType));
    eventList.put(new Integer(2), new FieldDecoration("c_goTo('/marketing/view_event.do?TYPEOFOPERATION=EDIT&FromAttendee=NO&eventid=", "');", 1, FieldDecoration.urlType));
    eventList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 7, FieldDecoration.urlType));
    decoratorData.put(new Integer(EVENT_LIST_TYPE), eventList);
    
    HashMap projectList = new HashMap();
    projectList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/projects/view_project.do?rowId=", "');", 1, FieldDecoration.urlType));
    projectList.put(new Integer(2), new FieldDecoration("c_openPopup('/projects/view_project.do?rowId=", "');", 1, FieldDecoration.urlType));
    projectList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 3, FieldDecoration.urlType));
    decoratorData.put(new Integer(PROJECT_LIST_TYPE), projectList);
    
    HashMap tasksList = new HashMap();
    tasksList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/projects/view_task.do?rowId=", "');", 1, FieldDecoration.urlType));
    tasksList.put(new Integer(2), new FieldDecoration("c_openPopup('/projects/view_task.do?rowId=", "');", 1, FieldDecoration.urlType));
    tasksList.put(new Integer(9), new FieldDecoration("c_openPopup('/projects/view_project.do?rowId=", "');", 8, FieldDecoration.urlType));
    tasksList.put(new Integer(16), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 17, FieldDecoration.urlType));
    tasksList.put(new Integer(14), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 13, FieldDecoration.urlType));
    tasksList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.dateType));
    tasksList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.dateType));
    tasksList.put(new Integer(15), new FieldDecoration("", "", 15, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(TASKS_LIST_TYPE), tasksList);

    HashMap timeslipsList = new HashMap();
    timeslipsList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/projects/view_timeslip.do?rowId=", "');", 1, FieldDecoration.urlType));
    timeslipsList.put(new Integer(2), new FieldDecoration("c_openPopup('/projects/view_timeslip.do?rowId=", "');", 1, FieldDecoration.urlType));
    timeslipsList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 5, FieldDecoration.urlType));
    timeslipsList.put(new Integer(9), new FieldDecoration("", "", 3, FieldDecoration.hourType));
    timeslipsList.put(new Integer(10), new FieldDecoration("", "", 3, FieldDecoration.dateType));
    timeslipsList.put(new Integer(11), new FieldDecoration("", "", 3, FieldDecoration.timeType));
    timeslipsList.put(new Integer(12), new FieldDecoration("", "", 3, FieldDecoration.timeType));  

    timeslipDurationCalc.put("Start",new Integer(11));
    timeslipDurationCalc.put("End",new Integer(12));
	
    decoratorData.put(new Integer(TIMESLIPS_LIST_TYPE), timeslipsList);    

    HashMap FAQList = new HashMap();
    FAQList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/support/faq_view.do?typeofoperation=VIEW&rowId=", "');", 1, FieldDecoration.urlType));
    FAQList.put(new Integer(2), new FieldDecoration("c_goTo('/support/faq_view.do?typeofoperation=VIEW&rowId=", "');", 1, FieldDecoration.urlType));
    FAQList.put(new Integer(3), new FieldDecoration("", "", 3, FieldDecoration.dateTimeType));
    FAQList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(FAQ_LIST_TYPE), FAQList);
    
    HashMap knowledgeBaseList = new HashMap();
    knowledgeBaseList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/support/knowledgebase_dispatch.do?rowId=", "');", 1, FieldDecoration.urlType));
    knowledgeBaseList.put(new Integer(3), new FieldDecoration("c_goTo('/support/knowledgebase_dispatch.do?rowId=", "');", 1, FieldDecoration.urlType));
    knowledgeBaseList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
    knowledgeBaseList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(KNOWLEDGEBASE_LIST_TYPE), knowledgeBaseList);


    HashMap orderList = new HashMap();
    orderList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_order.do?TYPEOFOPERATION=ShowOrder&rowId=", "');", 1, FieldDecoration.urlType));
    orderList.put(new Integer(1), new FieldDecoration("c_goTo('/accounting/view_order.do?TYPEOFOPERATION=ShowOrder&rowId=", "');", 1, FieldDecoration.urlType));
    orderList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 2, FieldDecoration.urlType));
    orderList.put(new Integer(7), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 6, FieldDecoration.urlType));
    orderList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
    orderList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.moneyType));
    decoratorData.put(new Integer(ORDER_LIST_TYPE), orderList);

    HashMap invoiceList = new HashMap();
    invoiceList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_invoice.do?TYPEOFOPERATION=ShowInvoice&rowId=", "');", 1, FieldDecoration.urlType));
    invoiceList.put(new Integer(1), new FieldDecoration("c_goTo('/accounting/view_invoice.do?TYPEOFOPERATION=ShowInvoice&rowId=", "');", 1, FieldDecoration.urlType));
    invoiceList.put(new Integer(2), new FieldDecoration("c_goTo('/accounting/view_order.do?TYPEOFOPERATION=ShowOrder&rowId=", "');", 2, FieldDecoration.urlType));
    invoiceList.put(new Integer(4), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 3, FieldDecoration.urlType));
    invoiceList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 5, FieldDecoration.urlType));
    invoiceList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.dateType));
    invoiceList.put(new Integer(8), new FieldDecoration("", "", 8, FieldDecoration.moneyType));
    invoiceList.put(new Integer(9), new FieldDecoration("", "", 9, FieldDecoration.paidType));  
    decoratorData.put(new Integer(INVOICE_LIST_TYPE), invoiceList);
    
    HashMap paymentList = new HashMap();
    paymentList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_payment.do?rowId=", "');", 1, FieldDecoration.urlType));
    paymentList.put(new Integer(1), new FieldDecoration("c_goTo('/accounting/view_payment.do?rowId=", "');", 1, FieldDecoration.urlType));
    paymentList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 2, FieldDecoration.urlType));
    paymentList.put(new Integer(11), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 10, FieldDecoration.urlType));
    paymentList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.dateTimeType));
    paymentList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.moneyType));
    paymentList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.moneyType));
    paymentList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.moneyType));
    decoratorData.put(new Integer(PAYMENT_LIST_TYPE), paymentList);

    HashMap expenseList = new HashMap();
    expenseList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_expense.do?rowId=", "');", 1, FieldDecoration.urlType));
    expenseList.put(new Integer(1), new FieldDecoration("c_goTo('/accounting/view_expense.do?rowId=", "');", 1, FieldDecoration.urlType));    
    expenseList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 5, FieldDecoration.urlType));
    expenseList.put(new Integer(4), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 3, FieldDecoration.urlType));
    expenseList.put(new Integer(8), new FieldDecoration("", "", 8, FieldDecoration.dateTimeType));
    expenseList.put(new Integer(2), new FieldDecoration("", "", 2, FieldDecoration.moneyType));
    decoratorData.put(new Integer(EXPENSES_LIST_TYPE), expenseList);

    HashMap purchaseOrderList = new HashMap();
    purchaseOrderList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_purchaseorder.do?TYPEOFOPERATION=ShowPurchaseOrder&rowId=", "');", 1, FieldDecoration.urlType));
    purchaseOrderList.put(new Integer(1), new FieldDecoration("c_goTo('/accounting/view_purchaseorder.do?TYPEOFOPERATION=ShowPurchaseOrder&rowId=", "');", 1, FieldDecoration.urlType));
    purchaseOrderList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 5, FieldDecoration.urlType));
    purchaseOrderList.put(new Integer(4), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 3, FieldDecoration.urlType));
    purchaseOrderList.put(new Integer(2), new FieldDecoration("", "", 2, FieldDecoration.dateType));
    purchaseOrderList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.moneyType));
    purchaseOrderList.put(new Integer(8), new FieldDecoration("", "", 8, FieldDecoration.moneyType));
    purchaseOrderList.put(new Integer(9), new FieldDecoration("", "", 9, FieldDecoration.moneyType));    
    decoratorData.put(new Integer(PURCHASEORDER_LIST_TYPE), purchaseOrderList);

    HashMap itemList = new HashMap();
    itemList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_item.do?rowId=", "');", 1, FieldDecoration.urlType));
    itemList.put(new Integer(2), new FieldDecoration("c_goTo('/accounting/view_item.do?rowId=", "');", 1, FieldDecoration.urlType));
    itemList.put(new Integer(11), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 10, FieldDecoration.urlType));
    itemList.put(new Integer(13), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 12, FieldDecoration.urlType));
    itemList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.moneyType));
    itemList.put(new Integer(9), new FieldDecoration("", "", 9, FieldDecoration.moneyType));
    decoratorData.put(new Integer(ITEM_LIST_TYPE), itemList);

    HashMap glaccountList = new HashMap();
    glaccountList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.moneyType));
    decoratorData.put(new Integer(GLACCOUNT_LIST_TYPE), glaccountList);
    
    HashMap inventoryList = new HashMap();
    inventoryList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/view_inventory.do?rowId=", "');", 1, FieldDecoration.urlType));
    inventoryList.put(new Integer(1), new FieldDecoration("c_goTo('/accounting/view_inventory.do?rowId=", "');", 1, FieldDecoration.urlType));
    inventoryList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 4, FieldDecoration.urlType));
    inventoryList.put(new Integer(7), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 6, FieldDecoration.urlType));
    decoratorData.put(new Integer(INVENTORY_LIST_TYPE), inventoryList);

    HashMap vendorList = new HashMap();
    vendorList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 1, FieldDecoration.urlType));
    vendorList.put(new Integer(2), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 1, FieldDecoration.urlType));
    vendorList.put(new Integer(9), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','',720,585,'');", 9, FieldDecoration.urlType));
    vendorList.put(new Integer(10), new FieldDecoration("c_openWindowWithTool('", "');", 10, FieldDecoration.urlType));
    decoratorData.put(new Integer(VENDOR_LIST_TYPE), vendorList);

    HashMap fileList = new HashMap();
    fileList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/files/file_dispatch.do?rowId=", "');", 1, FieldDecoration.urlType));
    fileList.put(new Integer(4), new FieldDecoration("c_goTo('/files/file_dispatch.do?rowId=", "');", 1, FieldDecoration.urlType));
    fileList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.dateTimeType));
    fileList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.dateTimeType));
    fileList.put(new Integer(8), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 3, FieldDecoration.urlType));
    decoratorData.put(new Integer(FILE_LIST_TYPE), fileList);

    HashMap mocList = new HashMap();
    mocList.put(new Integer(ICON_INDEX), new FieldDecoration("editMOC(", ");", 1, FieldDecoration.urlType));
    mocList.put(new Integer(2), new FieldDecoration("editMOC(", ");", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(MOC_LIST_TYPE), mocList);

    HashMap addressList = new HashMap();
    addressList.put(new Integer(ICON_INDEX), new FieldDecoration("editAddress(", ");", 1, FieldDecoration.urlType));
    addressList.put(new Integer(2), new FieldDecoration("editAddress(", ");", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(ADDRESS_LIST_TYPE), addressList);
    
    HashMap expenseFormList = new HashMap();
    expenseFormList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/hr/expenseform_new.do?expenseFormID=", "');", 1, FieldDecoration.urlType));
    expenseFormList.put(new Integer(1), new FieldDecoration("c_goTo('/hr/expenseform_new.do?expenseFormID=", "');", 1, FieldDecoration.urlType));    
    expenseFormList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 2, FieldDecoration.urlType));
    expenseFormList.put(new Integer(8), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 9, FieldDecoration.urlType));
//    expenseFormList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
//    expenseFormList.put(new Integer(5), new FieldDecoration("", "", 5, FieldDecoration.dateTimeType));
    expenseFormList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.moneyType));
    decoratorData.put(new Integer(EXPENSEFORM_LIST_TYPE), expenseFormList);
    
    HashMap customFieldRIList = new HashMap();
    customFieldRIList.put(new Integer(ICON_INDEX), new FieldDecoration("viewCustomField(", ");", 1, FieldDecoration.urlType));
    customFieldRIList.put(new Integer(2), new FieldDecoration("viewCustomField(", ");", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOM_FIELD_RI_LIST_TYPE), customFieldRIList);
    
    HashMap customFieldList = new HashMap();
    customFieldList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/administration/view_custom_field.do?fieldid=", "');", 1, FieldDecoration.urlType));
    customFieldList.put(new Integer(2), new FieldDecoration("c_goTo('/administration/view_custom_field.do?fieldid=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOM_FIELDS_LIST_TYPE), customFieldList);
    
    HashMap historyListMap = new HashMap();
    historyListMap.put(new Integer(2), new FieldDecoration("", "", 2, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(HISTORY_LIST_TYPE), historyListMap);
    
    HashMap timeSheetList = new HashMap();
    timeSheetList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/hr/timesheet_view.do?TYPEOFOPERATION=EDIT&rowId=", "');", 1, FieldDecoration.urlType));
    timeSheetList.put(new Integer(1), new FieldDecoration("c_goTo('/hr/timesheet_view.do?TYPEOFOPERATION=EDIT&rowId=", "');", 1, FieldDecoration.urlType));    
    timeSheetList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 2, FieldDecoration.urlType));    
    timeSheetList.put(new Integer(7), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 8, FieldDecoration.urlType));    
    decoratorData.put(new Integer(TIMESHEET_LIST_TYPE), timeSheetList);

    HashMap employeeHandbookList = new HashMap();
    employeeHandbookList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/files/file_download.do?fileid=", "');", 1, FieldDecoration.urlType));
    employeeHandbookList.put(new Integer(4), new FieldDecoration("c_goTo('/files/file_download.do?fileid=", "');", 1, FieldDecoration.urlType));
    employeeHandbookList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.dateTimeType));
    employeeHandbookList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.dateTimeType));
    employeeHandbookList.put(new Integer(8), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 3, FieldDecoration.urlType));
    decoratorData.put(new Integer(EMPLOYEE_HANDBOOK_LIST_TYPE), employeeHandbookList);
    
    HashMap employeeList = new HashMap();
    employeeList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    employeeList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    employeeList.put(new Integer(9), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 8, FieldDecoration.urlType));
    employeeList.put(new Integer(18), new FieldDecoration("c_openWindow('/email/compose.do?to=", "','',720,585,'');", 18, FieldDecoration.urlType));
    decoratorData.put(new Integer(EMPLOYEE_LIST_TYPE), employeeList);

    HashMap emailLookupList = new HashMap();
    emailLookupList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(EMAIL_LOOKUP_LIST_TYPE), emailLookupList);

    HashMap eventAttendeeList = new HashMap();
    eventAttendeeList.put(new Integer(ICON_INDEX), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    eventAttendeeList.put(new Integer(2), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(EVENTATTENDEE_LIST_TYPE), eventAttendeeList);
    
    decoratorData.put(new Integer(SOURCE_LIST_TYPE), new HashMap());
    
    HashMap userList = new HashMap();
    userList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/administration/user_detail.do?rowId=", "');", 1, FieldDecoration.urlType));
    userList.put(new Integer(2), new FieldDecoration("c_goTo('/administration/user_detail.do?rowId=", "');", 1, FieldDecoration.urlType));
    userList.put(new Integer(3), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 4, FieldDecoration.urlType));
    userList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_entity.do?rowId=", "');", 6, FieldDecoration.urlType));
    decoratorData.put(new Integer(USER_LIST_TYPE), userList);
    
    HashMap customerEmailList = new HashMap();
    customerEmailList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_email.do?emailID=", "');", 1, FieldDecoration.urlType));
    customerEmailList.put(new Integer(2), new FieldDecoration("c_goTo('/customer/view_email.do?emailID=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_EMAIL_LIST_TYPE), customerEmailList);

    HashMap customerOrderList = new HashMap();
    customerOrderList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_order.do?orderID=", "');", 1, FieldDecoration.urlType));
    customerOrderList.put(new Integer(1), new FieldDecoration("c_goTo('/customer/view_order.do?orderID=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_ORDER_LIST_TYPE), customerOrderList);

    HashMap customerInvoiceList = new HashMap();
    customerInvoiceList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_invoice.do?invoiceID=", "');", 1, FieldDecoration.urlType));
    customerInvoiceList.put(new Integer(1), new FieldDecoration("c_goTo('/customer/view_invoice.do?invoiceID=", "');", 1, FieldDecoration.urlType));
    customerInvoiceList.put(new Integer(2), new FieldDecoration("c_goTo('/customer/view_order.do?orderID=", "');", 2, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_INVOICE_LIST_TYPE), customerInvoiceList);

    HashMap customerPaymentList = new HashMap();
    customerPaymentList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_payment.do?paymentID=", "');", 1, FieldDecoration.urlType));
    customerPaymentList.put(new Integer(1), new FieldDecoration("c_goTo('/customer/view_payment.do?paymentID=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_PAYMENT_LIST_TYPE), customerPaymentList);

    HashMap customerTicketList = new HashMap();
    customerTicketList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_ticket.do?ticketId=", "');", 1, FieldDecoration.urlType));
    customerTicketList.put(new Integer(2), new FieldDecoration("c_goTo('/customer/view_ticket.do?ticketId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_TICKET_LIST_TYPE), customerTicketList);

    HashMap customerFAQList = new HashMap();
    customerFAQList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_faq.do?faqID=", "');", 1, FieldDecoration.urlType));
    customerFAQList.put(new Integer(2), new FieldDecoration("c_goTo('/customer/view_faq.do?faqID=", "');", 1, FieldDecoration.urlType));
    customerFAQList.put(new Integer(3), new FieldDecoration("", "", 3, FieldDecoration.dateTimeType));
    customerFAQList.put(new Integer(4), new FieldDecoration("", "", 4, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(CUSTOMER_FAQ_LIST_TYPE), customerFAQList);

    HashMap customerEventList = new HashMap();
    customerEventList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_event.do?eventID=", "');", 1, FieldDecoration.urlType));
    customerEventList.put(new Integer(2), new FieldDecoration("c_goTo('/customer/view_event.do?eventID=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_EVENT_LIST_TYPE), customerEventList);

    HashMap customerUserList = new HashMap();
    customerUserList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/view_user.do?userID=", "');", 4, FieldDecoration.urlType));
    customerUserList.put(new Integer(2), new FieldDecoration("c_goTo('/customer/view_user.do?userID=", "');", 4, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOMER_USER_LIST_TYPE), customerUserList);

    HashMap customerFileList = new HashMap();
    customerFileList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/customer/file_dedtail.do?fileID=", "');", 1, FieldDecoration.urlType));
    customerFileList.put(new Integer(4), new FieldDecoration("c_goTo('/customer/file_detail.do?fileID=", "');", 1, FieldDecoration.urlType));
    customerFileList.put(new Integer(6), new FieldDecoration("", "", 6, FieldDecoration.dateTimeType));
    customerFileList.put(new Integer(7), new FieldDecoration("", "", 7, FieldDecoration.dateTimeType));
    decoratorData.put(new Integer(CUSTOMER_FILE_LIST_TYPE), customerFileList);

    itemLookupMap.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/accounting/item_lookup.do?actionType=lookup&itemid=", "');", 1, FieldDecoration.urlType));

    decoratorData.put(new Integer(LOCATION_LIST_TYPE), new HashMap());
    
    HashMap standardReportList = new HashMap();
    standardReportList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/reports/standard_report.do?reportId=","');", 1, FieldDecoration.urlType));
    standardReportList.put(new Integer(1), new FieldDecoration("c_goTo('/reports/standard_report.do?reportId=","');", 1, FieldDecoration.urlType));
    standardReportList.put(new Integer(4), new FieldDecoration("c_goTo('/reports/view_standard_report.do?reportId=","');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(STANDARD_REPORT_LIST_TYPE), standardReportList);

    HashMap literatureList = new HashMap();
    literatureList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/administration/view_literature.do?rowId=", "');", 1, FieldDecoration.urlType));
    literatureList.put(new Integer(2), new FieldDecoration("c_goTo('/administration/view_literature.do?rowId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(LITERATURE_LIST_TYPE), literatureList);

    HashMap customViewList = new HashMap();
    customViewList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/administration/edit_custom_view.do?actionType=edit&viewId=", "');", 1, FieldDecoration.urlType));
    customViewList.put(new Integer(2), new FieldDecoration("c_goTo('/administration/edit_custom_view.do?actionType=edit&viewId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(CUSTOM_VIEW_LIST_TYPE), customViewList);

    HashMap garbageViewList = new HashMap();
    garbageViewList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    garbageViewList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(GARBAGE_LIST_TYPE), garbageViewList);

    HashMap atticViewList = new HashMap();
    atticViewList.put(new Integer(5), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    atticViewList.put(new Integer(6), new FieldDecoration("c_openPopup('/contacts/view_individual.do?rowId=", "');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(ATTIC_LIST_TYPE), atticViewList);

    HashMap adhocReportList = new HashMap();
    adhocReportList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/reports/adhoc_report.do?reportId=","');", 1, FieldDecoration.urlType));
    adhocReportList.put(new Integer(1), new FieldDecoration("c_goTo('/reports/adhoc_report.do?reportId=","');", 1, FieldDecoration.urlType));
    adhocReportList.put(new Integer(4), new FieldDecoration("c_goTo('/reports/view_adhoc_report.do?reportId=","');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(ADHOC_REPORT_LIST_TYPE), adhocReportList);
    
    HashMap securityProfileList = new HashMap();
    securityProfileList.put(new Integer(ICON_INDEX), new FieldDecoration("c_goTo('/administration/view_profile.do?typeofoperation=EDIT&rowId=","');", 1, FieldDecoration.urlType));
    securityProfileList.put(new Integer(2), new FieldDecoration("c_goTo('/administration/view_profile.do?typeofoperation=EDIT&rowId=","');", 1, FieldDecoration.urlType));
    decoratorData.put(new Integer(SECURITY_PROFILE_LIST_TYPE), securityProfileList);

    // Mappings
    entityViewMap.put("EntityID", new FieldDescriptor("Entity ID", 1, false, "label.value.header.entityid"));
    entityViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    entityViewMap.put("PrimaryContact", new FieldDescriptor("Primary Contact", 7, false, "label.value.header.primarycontact"));
    entityViewMap.put("Phone", new FieldDescriptor("Phone", 8, false, "label.value.header.phone"));
    entityViewMap.put("Email", new FieldDescriptor("Email", 9, false, "label.value.header.email"));
    entityViewMap.put("Address", new FieldDescriptor("Address", 17, false, "label.value.header.address"));
    entityViewMap.put("Street1", new FieldDescriptor("Street 1", 11, false, "label.value.header.street1"));
    entityViewMap.put("Street2", new FieldDescriptor("Street 2", 12, false, "label.value.header.street2"));
    entityViewMap.put("City", new FieldDescriptor("City", 13, false, "label.value.header.city"));
    entityViewMap.put("State", new FieldDescriptor("State", 14, false, "label.value.header.state"));
    entityViewMap.put("Zip", new FieldDescriptor("Zip Code", 15, false, "label.value.header.zipcode"));
    entityViewMap.put("Country", new FieldDescriptor("Country", 16, false, "label.value.header.country"));
    entityViewMap.put("ListID", new FieldDescriptor("Marketing List ID", 3, false, "label.value.header.listid"));
    entityViewMap.put("Website", new FieldDescriptor("Website", 10, false, "label.value.header.website"));
    entityViewMap.put("AccountManager", new FieldDescriptor("Account Manager", 5, false, "label.value.header.accountmanager"));
    entityViewMap.put("Fax", new FieldDescriptor("Fax", 18, false, "label.value.header.fax"));    

    individualViewMap.put("Name", new FieldDescriptor("Name", 3, false, "label.value.header.name"));
    individualViewMap.put("Title", new FieldDescriptor("Title", 7, false, "label.value.header.title"));
    individualViewMap.put("Entity", new FieldDescriptor("Entity", 9, false, "label.value.header.entity"));
    individualViewMap.put("Phone", new FieldDescriptor("Phone", 17, false, "label.value.header.phone"));
    individualViewMap.put("Fax", new FieldDescriptor("Fax", 19, false, "label.value.header.fax"));
    individualViewMap.put("Email", new FieldDescriptor("Email", 18, false, "label.value.header.email"));

    bottomIndividualViewMap.put("Name", new FieldDescriptor("Name", 3, false, "label.value.header.name"));
    bottomIndividualViewMap.put("Title", new FieldDescriptor("Title", 7, false, "label.value.header.title"));
    bottomIndividualViewMap.put("Entity", new FieldDescriptor("Entity", 9, false, "label.value.header.entity"));
    bottomIndividualViewMap.put("Phone", new FieldDescriptor("Phone", 17, false, "label.value.header.phone"));
    bottomIndividualViewMap.put("Fax", new FieldDescriptor("Fax", 19, false, "label.value.header.fax"));
    bottomIndividualViewMap.put("Email", new FieldDescriptor("Email", 18, false, "label.value.header.email"));

    groupViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    groupViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    groupViewMap.put("NOOfMembers", new FieldDescriptor("# of Members", 4, false, "label.value.header.noofmembers"));

    opportunityViewMap.put("OpportunityID", new FieldDescriptor("Opportunity ID", 1, false, "label.value.header.opportunityid"));
    opportunityViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    opportunityViewMap.put("Description", new FieldDescriptor("Description", 5, false, "label.value.header.description"));
    opportunityViewMap.put("EntityID", new FieldDescriptor("Entity ID", 6, false, "label.value.header.entityid"));
    opportunityViewMap.put("Entity", new FieldDescriptor("Entity", 7, false, "label.value.header.entity"));
    opportunityViewMap.put("Type", new FieldDescriptor("Type", 8, false, "label.value.header.type"));
    opportunityViewMap.put("Stage", new FieldDescriptor("Stage", 9, false, "label.value.header.stage"));
    opportunityViewMap.put("Probability", new FieldDescriptor("Probability", 10, false, "label.value.header.probability"));
    opportunityViewMap.put("Status", new FieldDescriptor("Status", 11, false, "label.value.header.status"));
    opportunityViewMap.put("EstimatedCloseDate", new FieldDescriptor("Est. Close Date", 12, false, "label.value.header.estclosedate"));
    opportunityViewMap.put("ForecastAmmount", new FieldDescriptor("Forecast Amnt.", 13, false, "label.value.header.forecastamount"));
    opportunityViewMap.put("ActualAmount", new FieldDescriptor("Total Amnt.", 4, false, "label.value.header.totalamount"));
    opportunityViewMap.put("SalePersonName", new FieldDescriptor("Act. Manager", 16, false, "label.value.header.accountmanager"));

    proposalsViewMap.put("ProposalID", new FieldDescriptor("Proposal ID", 1, false, "label.value.header.proposalid"));
    proposalsViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    proposalsViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    proposalsViewMap.put("Type", new FieldDescriptor("Type", 4, false, "label.value.header.type"));
    proposalsViewMap.put("Stage", new FieldDescriptor("Stage", 5, false, "label.value.header.stage"));
    proposalsViewMap.put("Probability", new FieldDescriptor("Probability", 6, false, "label.value.header.probability"));
    proposalsViewMap.put("Status", new FieldDescriptor("Status", 7, false, "label.value.header.status"));
    proposalsViewMap.put("EstimatedCloseDate", new FieldDescriptor("Est. Close Date", 8, false, "label.value.header.estclosedate"));
    proposalsViewMap.put("ForecastAmmount", new FieldDescriptor("Forecast Amnt.", 9, false, "label.value.header.forecastamount"));
    proposalsViewMap.put("SalePersonID", new FieldDescriptor("Act. Manager ID", 10, false, "label.value.header.accountmanagerid"));
    proposalsViewMap.put("SalePersonName", new FieldDescriptor("Act. Manager", 11, false, "label.value.header.accountmanager"));
    
    activityViewMap.put("Type", new FieldDescriptor("Type", 6, false, "label.value.header.type"));
    activityViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    activityViewMap.put("Created", new FieldDescriptor("Created", 5, false, "label.value.header.created"));
    activityViewMap.put("Details", new FieldDescriptor("Details", 7, false, "value.label.header.details"));
    activityViewMap.put("Priority", new FieldDescriptor("Priority", 9, false, "value.label.header.priority"));
    activityViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "value.label.header.createdby"));
    activityViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    activityViewMap.put("Start", new FieldDescriptor("Start", 10, false, "label.value.header.start"));
    activityViewMap.put("End", new FieldDescriptor("End", 11, false, "label.value.header.end"));
    activityViewMap.put("Entity", new FieldDescriptor("Entity", 14, false, "label.value.header.entity"));
    activityViewMap.put("Individual", new FieldDescriptor("Individual", 13, false, "label.value.header.individual"));
    activityViewMap.put("Owner", new FieldDescriptor("Owner", 18, false, "label.value.header.owner"));
    activityViewMap.put("Notes", new FieldDescriptor("Notes", 19, false, "label.value.header.notes"));

    appointmentViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    appointmentViewMap.put("Created", new FieldDescriptor("Created", 5, false, "label.value.header.created"));
    appointmentViewMap.put("Details", new FieldDescriptor("Details", 7, false, "value.label.header.details"));
    appointmentViewMap.put("Priority", new FieldDescriptor("Priority", 9, false, "value.label.header.priority"));
    appointmentViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "value.label.header.createdby"));
    appointmentViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    appointmentViewMap.put("Start", new FieldDescriptor("Start", 10, false, "label.value.header.start"));
    appointmentViewMap.put("End", new FieldDescriptor("End", 11, false, "label.value.header.end"));
    appointmentViewMap.put("Entity", new FieldDescriptor("Entity", 14, false, "label.value.header.entity"));
    appointmentViewMap.put("Individual", new FieldDescriptor("Individual", 13, false, "label.value.header.individual"));
    appointmentViewMap.put("Owner", new FieldDescriptor("Owner", 18, false, "label.value.header.owner"));
    appointmentViewMap.put("Notes", new FieldDescriptor("Notes", 19, false, "label.value.header.notes"));
    
    callViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    callViewMap.put("Created", new FieldDescriptor("Created", 5, false, "label.value.header.created"));
    callViewMap.put("Details", new FieldDescriptor("Details", 7, false, "value.label.header.details"));
    callViewMap.put("Priority", new FieldDescriptor("Priority", 9, false, "value.label.header.priority"));
    callViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "value.label.header.createdby"));
    callViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    callViewMap.put("CallType", new FieldDescriptor("Call Type", 20, false, "label.value.header.calltype"));
    callViewMap.put("Start", new FieldDescriptor("Start", 10, false, "label.value.header.start"));
    callViewMap.put("End", new FieldDescriptor("End", 11, false, "label.value.header.end"));
    callViewMap.put("Entity", new FieldDescriptor("Entity", 14, false, "label.value.header.entity"));
    callViewMap.put("Individual", new FieldDescriptor("Individual", 13, false, "label.value.header.individual"));
    callViewMap.put("Owner", new FieldDescriptor("Owner", 18, false, "label.value.header.owner"));
    callViewMap.put("Notes", new FieldDescriptor("Notes", 19, false, "label.value.header.notes"));
    
    forecastSalesViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    forecastSalesViewMap.put("Description", new FieldDescriptor("Description", 5, false, "label.value.header.description"));
    forecastSalesViewMap.put("Entity", new FieldDescriptor("Entity", 7, false, "label.value.header.entity"));
    forecastSalesViewMap.put("Type", new FieldDescriptor("Type", 8, false, "label.value.header.type"));
    forecastSalesViewMap.put("Stage", new FieldDescriptor("Stage", 9, false, "label.value.header.stage"));
    forecastSalesViewMap.put("Probability", new FieldDescriptor("Probability", 10, false, "label.value.header.probability"));
    forecastSalesViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    forecastSalesViewMap.put("EstimatedCloseDate", new FieldDescriptor("Est. Close Date", 12, false, "label.value.header.estclosedate"));
    forecastSalesViewMap.put("ForecastAmount", new FieldDescriptor("Forecast Amnt.", 13, false, "label.value.header.forecastamount"));
    forecastSalesViewMap.put("ActualAmount", new FieldDescriptor("Total Amnt.", 4, false, "label.value.header.totalamount"));
    forecastSalesViewMap.put("SalesPersonName", new FieldDescriptor("Act. Manager", 16, false, "label.value.header.accountmanager"));

    literatureRequestViewMap.put("LiteratureRequested", new FieldDescriptor("Literature Requested", 2, false, "label.value.header.literaturerequested"));
    literatureRequestViewMap.put("WhoRequested", new FieldDescriptor("Who Requested", 5, false, "label.value.header.whorequested"));
    literatureRequestViewMap.put("DateRequested", new FieldDescriptor("Date Requested", 6, false, "label.value.header.daterequested"));
    literatureRequestViewMap.put("DeliveryMethod", new FieldDescriptor("Delivery Method", 7, false, "label.value.header.deliverymethod"));
    literatureRequestViewMap.put("Entity", new FieldDescriptor("Entity", 8, false, "label.value.header.entity"));

    meetingViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    meetingViewMap.put("Created", new FieldDescriptor("Created", 5, false, "label.value.header.created"));
    meetingViewMap.put("Details", new FieldDescriptor("Details", 7, false, "value.label.header.details"));
    meetingViewMap.put("Priority", new FieldDescriptor("Priority", 9, false, "value.label.header.priority"));
    meetingViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "value.label.header.createdby"));
    meetingViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    meetingViewMap.put("Start", new FieldDescriptor("Start", 10, false, "label.value.header.start"));
    meetingViewMap.put("End", new FieldDescriptor("End", 11, false, "label.value.header.end"));
    meetingViewMap.put("Entity", new FieldDescriptor("Entity", 14, false, "label.value.header.entity"));
    meetingViewMap.put("Individual", new FieldDescriptor("Individual", 13, false, "label.value.header.individual"));
    meetingViewMap.put("Owner", new FieldDescriptor("Owner", 18, false, "label.value.header.owner"));
    meetingViewMap.put("Notes", new FieldDescriptor("Notes", 19, false, "label.value.header.notes"));

    nextActionViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    nextActionViewMap.put("Created", new FieldDescriptor("Created", 5, false, "label.value.header.created"));
    nextActionViewMap.put("Details", new FieldDescriptor("Details", 7, false, "value.label.header.details"));
    nextActionViewMap.put("Priority", new FieldDescriptor("Priority", 9, false, "value.label.header.priority"));
    nextActionViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "value.label.header.createdby"));
    nextActionViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    nextActionViewMap.put("Start", new FieldDescriptor("Start", 10, false, "label.value.header.start"));
    nextActionViewMap.put("End", new FieldDescriptor("End", 11, false, "label.value.header.end"));
    nextActionViewMap.put("Entity", new FieldDescriptor("Entity", 14, false, "label.value.header.entity"));
    nextActionViewMap.put("Individual", new FieldDescriptor("Individual", 13, false, "label.value.header.individual"));
    nextActionViewMap.put("Owner", new FieldDescriptor("Owner", 18, false, "label.value.header.owner"));
    nextActionViewMap.put("Notes", new FieldDescriptor("Notes", 19, false, "label.value.header.notes"));

    todoViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    todoViewMap.put("Created", new FieldDescriptor("Created", 5, false, "label.value.header.created"));
    todoViewMap.put("Details", new FieldDescriptor("Details", 7, false, "value.label.header.details"));
    todoViewMap.put("Priority", new FieldDescriptor("Priority", 9, false, "value.label.header.priority"));
    todoViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "value.label.header.createdby"));
    todoViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    todoViewMap.put("Start", new FieldDescriptor("Start", 10, false, "label.value.header.start"));
    todoViewMap.put("End", new FieldDescriptor("End", 11, false, "label.value.header.end"));
    todoViewMap.put("Entity", new FieldDescriptor("Entity", 14, false, "label.value.header.entity"));
    todoViewMap.put("Individual", new FieldDescriptor("Individual", 13, false, "label.value.header.individual"));
    todoViewMap.put("Owner", new FieldDescriptor("Owner", 18, false, "label.value.header.owner"));
    todoViewMap.put("Notes", new FieldDescriptor("Notes", 19, false, "label.value.header.notes"));

    taskViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    taskViewMap.put("CreatedBy", new FieldDescriptor("Created By", 14, false, "value.label.header.createdby"));
    taskViewMap.put("Created", new FieldDescriptor("Created", 15, false, "label.value.header.created"));
    taskViewMap.put("DueDate", new FieldDescriptor("Due Date", 6, false, "label.value.header.duedate"));
    taskViewMap.put("Priority", new FieldDescriptor("Priority", 12, false, "value.label.header.priority"));
    taskViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));
    
    // view mappings for the Template list
    // Maps column names that are currently stored in the database to
    // FieldDescriptor objects which will be there in the future.
    templateViewMap.put("TemplateID", new FieldDescriptor("ID", 1, false, "label.value.header.id"));
    templateViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    templateViewMap.put("Category", new FieldDescriptor("Type", 3, false, "label.value.header.type"));
    
    ticketViewMap.put("Number", new FieldDescriptor("Ticket ID", 1, false, "label.value.header.ticketid"));
    ticketViewMap.put("Subject", new FieldDescriptor("Subject", 2, false, "label.value.header.subject"));
    ticketViewMap.put("Entity", new FieldDescriptor("Entity", 3, false, "label.value.header.entity"));
    ticketViewMap.put("Status", new FieldDescriptor("Status", 8, false, "label.value.header.status"));
    ticketViewMap.put("DateOpened", new FieldDescriptor("DateOpened", 6, false, "label.value.header.dateopened"));
    ticketViewMap.put("DateClosed", new FieldDescriptor("DateClosed", 9, false, "label.value.header.dateclosed"));
    ticketViewMap.put("AssignedTo", new FieldDescriptor("AssignedTo", 10, false, "label.value.header.assignedto"));

    emailViewMap.put("Subject", new FieldDescriptor("Subject", 2, false, "label.value.header.subject"));
    emailViewMap.put("From", new FieldDescriptor("From", 4, false, "label.value.header.from"));
    emailViewMap.put("To", new FieldDescriptor("To", 11, false, "label.value.header.to"));
    emailViewMap.put("Received", new FieldDescriptor("Received", 3, false, "label.value.header.recieved"));
    emailViewMap.put("Size", new FieldDescriptor("Size", 6, false, "label.value.header.size"));

    ruleViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    ruleViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    ruleViewMap.put("Enabled", new FieldDescriptor("Enabled", 4, false, "label.value.header.enabled"));

    emailIconMap.put("YES","icon_email_read.gif");
    emailIconMap.put("NO","icon_email_unread.gif");
    emailIconMap.put("HIGH","icon_high_priority.gif");
    emailIconMap.put("MEDIUM","");
    emailIconMap.put("LOW","icon_low_priority.gif");

    noteViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    noteViewMap.put("Date", new FieldDescriptor("Date Created", 4, false, "label.value.header.datecreated"));
    noteViewMap.put("CreatedBy", new FieldDescriptor("Created By", 5, false, "value.label.header.createdby"));
    noteViewMap.put("Detail", new FieldDescriptor("Detail", 3, false, "value.label.header.details"));

    listManagerViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    listManagerViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    listManagerViewMap.put("NumberOfRecords", new FieldDescriptor("Records", 4, false, "label.value.header.noofrecords"));
    listManagerViewMap.put("OrderCount", new FieldDescriptor("Orders", 6, false, "label.value.header.orders"));
    listManagerViewMap.put("OrderValue", new FieldDescriptor("Order Value", 7, false, "label.value.header.ordervalue"));
    listManagerViewMap.put("OpportunityCount", new FieldDescriptor("Opportunities", 8, false, "label.value.header.opportunities"));
    listManagerViewMap.put("OpportunityValue", new FieldDescriptor("Opportunity Value", 9, false, "label.value.header.opportunityvalue"));

    promotionViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    promotionViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    promotionViewMap.put("StartDate", new FieldDescriptor("Start Date", 4, false, "label.value.header.startdate"));
    promotionViewMap.put("EndDate", new FieldDescriptor("End Date", 5, false, "label.value.header.enddate"));
    promotionViewMap.put("Status", new FieldDescriptor("Status", 6, false, "label.value.header.status"));
    promotionViewMap.put("NoOfOrders", new FieldDescriptor("No Of Orders", 7, false, "label.value.header.nooforders"));

    eventViewMap.put("EventID", new FieldDescriptor("Event ID", 1, false, "label.value.header.eventid"));
    eventViewMap.put("Title", new FieldDescriptor("Event Name", 2, false, "label.value.header.eventname"));
    eventViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    eventViewMap.put("Start", new FieldDescriptor("From", 4, false, "label.value.header.from"));
    eventViewMap.put("End", new FieldDescriptor("To", 5, false, "label.value.header.to"));
    eventViewMap.put("RegisteredAttendees", new FieldDescriptor("Registered Attendees", 8, false, "label.value.header.registeredattendees"));
    eventViewMap.put("OwnerName", new FieldDescriptor("Owner", 6, false, "label.value.header.ownername"));

    projectViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    projectViewMap.put("Entity", new FieldDescriptor("Entity Name", 4, false, "label.value.header.entityname"));
    projectViewMap.put("Status", new FieldDescriptor("Status", 5, false, "label.value.header.status"));
    projectViewMap.put("DueDate", new FieldDescriptor("End Date", 6, false, "label.value.header.enddate"));

    tasksViewMap.put("Title", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    tasksViewMap.put("Project", new FieldDescriptor("Project Name", 9, false, "label.value.header.projectname"));
    tasksViewMap.put("Milestone", new FieldDescriptor("Milestone", 4, false, "label.value.header.milestone"));
    tasksViewMap.put("Manager", new FieldDescriptor("Manager", 16, false, "label.value.header.manager"));
    tasksViewMap.put("StartDate", new FieldDescriptor("Start Date", 5, false, "label.value.header.startdate"));
    tasksViewMap.put("DueDate", new FieldDescriptor("Due Date", 6, false, "label.value.header.duedate"));
    tasksViewMap.put("Complete", new FieldDescriptor("Complete", 7, false, "label.value.header.complete"));
    tasksViewMap.put("Status", new FieldDescriptor("Status", 3, false, "label.value.header.status"));

    timeslipsViewMap.put("TimeSlipID", new FieldDescriptor("TimeSlipID", 1, false, "label.value.header.timeslipid"));
    timeslipsViewMap.put("Description", new FieldDescriptor("Description", 2, false, "label.value.header.description"));
    timeslipsViewMap.put("Project", new FieldDescriptor("Project Title", 4, false, "label.value.header.projectname"));
    timeslipsViewMap.put("Task", new FieldDescriptor("Task", 8, false, "label.value.header.task"));
    timeslipsViewMap.put("Duration", new FieldDescriptor("Duration", 9, false, "label.value.header.duration"));
    timeslipsViewMap.put("CreatedBy", new FieldDescriptor("CreatedBy", 6, false, "value.label.header.createdby"));
    timeslipsViewMap.put("Date", new FieldDescriptor("Date", 10, false, "value.label.header.date"));
    timeslipsViewMap.put("StartTime", new FieldDescriptor("StartTime", 11, false, "value.label.header.starttime"));
    timeslipsViewMap.put("EndTime", new FieldDescriptor("EndTime", 12, false, "value.label.header.endtime"));

    FAQViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    FAQViewMap.put("Created", new FieldDescriptor("Created", 3, false, "label.value.header.created"));
    FAQViewMap.put("Updated", new FieldDescriptor("Updated", 4, false, "label.value.header.updated"));

    knowledgeBaseViewMap.put("Name", new FieldDescriptor("Title", 3, false, "label.value.header.title"));
    knowledgeBaseViewMap.put("DateCreated", new FieldDescriptor("Created", 4, false, "label.value.header.created"));
    knowledgeBaseViewMap.put("DateUpdated", new FieldDescriptor("Updated", 5, false, "label.value.header.updated"));

    orderViewMap.put("OrderNO", new FieldDescriptor("Order Number", 1, false, "label.value.header.orderno"));
    orderViewMap.put("Entity", new FieldDescriptor("Entity Name", 3, false, "label.value.header.entityname"));
    orderViewMap.put("Date", new FieldDescriptor("Order Date", 4, false, "label.value.header.orderdate"));
    orderViewMap.put("Total", new FieldDescriptor("Total", 5, false, "label.value.header.total"));
    orderViewMap.put("Status", new FieldDescriptor("Status", 8, false, "label.value.header.status"));
    orderViewMap.put("SalesRep", new FieldDescriptor("Act. Manager", 7, false, "label.value.header.accountmanager"));
	
    invoiceViewMap.put("InvoiceID", new FieldDescriptor("InvoiceID", 1, false, "label.value.header.invoiceid"));
    invoiceViewMap.put("OrderID", new FieldDescriptor("OrderID", 2, false, "label.value.header.orderID"));
    invoiceViewMap.put("Customer", new FieldDescriptor("Customer", 4, false, "label.value.header.customer"));
    invoiceViewMap.put("InvoiceDate", new FieldDescriptor("InvoiceDate", 7, false, "label.value.header.invoicedate"));
    invoiceViewMap.put("Total", new FieldDescriptor("Total", 8, false, "label.value.header.total"));
    invoiceViewMap.put("Paid", new FieldDescriptor("Paid", 9, false, "label.value.header.paid"));
    invoiceViewMap.put("Creator", new FieldDescriptor("Creator", 6, false, "value.label.header.createdby"));
    
    paymentViewMap.put("PaymentID", new FieldDescriptor("PaymentID", 1, false, "label.value.header.paymentid"));
    paymentViewMap.put("Entity", new FieldDescriptor("Entity", 3, false, "label.value.header.entity"));
    paymentViewMap.put("AmountPaid", new FieldDescriptor("Amount Paid", 4, false, "label.value.header.amountpaid"));
    paymentViewMap.put("AppliedAmount", new FieldDescriptor("Applied Amount", 5, false, "label.value.header.appliedamount"));
    paymentViewMap.put("UnAppliedAmount", new FieldDescriptor("UnApplied Amount", 6, false, "label.value.header.unappliedamount"));
    paymentViewMap.put("PaymentDate", new FieldDescriptor("Payment Date", 7, false, "label.value.header.paymentdate"));
    paymentViewMap.put("PaymentMethod", new FieldDescriptor("Payment Method", 8, false, "label.value.header.paymentmethod"));
    paymentViewMap.put("Reference", new FieldDescriptor("Reference", 9, false, "label.value.header.reference"));
    paymentViewMap.put("CreatedBy", new FieldDescriptor("Created By", 11, false, "value.label.header.createdby"));

    expensesViewMap.put("ExpenseID", new FieldDescriptor("ExpenseID", 1, false, "label.value.header.expenseid"));
    expensesViewMap.put("Amount", new FieldDescriptor("Amount", 2, false, "label.value.header.amount"));
    expensesViewMap.put("Created", new FieldDescriptor("Created", 8, false, "label.value.header.created"));
    expensesViewMap.put("EntityName", new FieldDescriptor("Entity Name", 6, false, "label.value.header.entityname"));
    expensesViewMap.put("Status", new FieldDescriptor("Status", 7, false, "label.value.header.status"));
    expensesViewMap.put("Creator", new FieldDescriptor("Creator", 4, false, "value.label.header.createdby"));

    purchaseorderViewMap.put("PurchaseOrderID", new FieldDescriptor("PurchaseOrderID # ", 1, false, "value.label.header.purchaseorderid"));
    purchaseorderViewMap.put("Created", new FieldDescriptor("Created", 2, false, "label.value.header.created"));
    purchaseorderViewMap.put("Creator", new FieldDescriptor("Creator", 4, false, "value.label.header.createdby"));
    purchaseorderViewMap.put("Entity", new FieldDescriptor("Entity Name", 6, false, "label.value.header.entityname"));
    purchaseorderViewMap.put("SubTotal", new FieldDescriptor("SubTotal", 7, false, "label.value.header.subtotal"));
    purchaseorderViewMap.put("Tax", new FieldDescriptor("Tax", 8, false, "label.value.header.tax"));
    purchaseorderViewMap.put("Total", new FieldDescriptor("Total", 9, false, "label.value.header.total"));
    purchaseorderViewMap.put("Status", new FieldDescriptor("Status", 10, false, "label.value.header.status"));

    itemViewMap.put("SKU", new FieldDescriptor("SKU", 2, false, "label.value.header.sku"));
    itemViewMap.put("Name", new FieldDescriptor("Name", 3, false, "label.value.header.name"));
    itemViewMap.put("Type", new FieldDescriptor("Type", 4, false, "label.value.header.type"));
    itemViewMap.put("Price", new FieldDescriptor("Price", 5, false, "label.value.header.price"));
    itemViewMap.put("OnHand", new FieldDescriptor("OnHand", 6, false, "label.value.header.onhand"));
    itemViewMap.put("Tax", new FieldDescriptor("Tax", 8, false, "label.value.header.tax"));
    itemViewMap.put("Cost", new FieldDescriptor("Cost", 9, false, "label.value.header.cost"));
    itemViewMap.put("Vendor", new FieldDescriptor("Vendor", 11, false, "label.value.header.vendor"));
    itemViewMap.put("Manufacturer", new FieldDescriptor("Manufacturer", 13, false, "label.value.header.manufacturer"));

    glaccountViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    glaccountViewMap.put("Type", new FieldDescriptor("Type", 3, false, "label.value.header.type"));
    glaccountViewMap.put("Balance", new FieldDescriptor("Balance", 4, false, "label.value.header.balance"));
    glaccountViewMap.put("ParentAccount", new FieldDescriptor("ParentAccount", 5, false, "label.value.header.parentaccount"));

    inventoryViewMap.put("InventoryID", new FieldDescriptor("InventoryID", 1, false, "label.value.header.inventoryid"));
    inventoryViewMap.put("ItemName", new FieldDescriptor("ItemName", 2, false, "label.value.header.itemname"));
    inventoryViewMap.put("Identifier", new FieldDescriptor("Identifier", 3, false, "label.value.header.identifier"));
    inventoryViewMap.put("Manufacturer", new FieldDescriptor("Manufacturer", 5, false, "label.value.header.manufacturer"));
    inventoryViewMap.put("Vendor", new FieldDescriptor("Vendor", 7, false, "label.value.header.vendor"));

    vendorViewMap.put("Name", new FieldDescriptor("Vendor Name", 2, false, "label.value.header.vendorname"));
    vendorViewMap.put("PrimaryContact", new FieldDescriptor("Primary Contact", 7, false, "label.value.header.primarycontact"));
    vendorViewMap.put("Phone", new FieldDescriptor("Phone", 8, false, "label.value.header.phone"));
    vendorViewMap.put("Email", new FieldDescriptor("Email", 9, false, "label.value.header.email"));
    vendorViewMap.put("Address", new FieldDescriptor("Address", 17, false, "label.value.header.address"));
    vendorViewMap.put("Website", new FieldDescriptor("Website", 10, false, "label.value.header.website"));

    fileViewMap.put("Name", new FieldDescriptor("Name", 4, false, "label.value.header.name"));
    fileViewMap.put("Description", new FieldDescriptor("Description", 5, false, "label.value.header.description"));
    fileViewMap.put("Created", new FieldDescriptor("Created", 6, false, "label.value.header.created"));
    fileViewMap.put("Updated", new FieldDescriptor("Updated", 7, false, "label.value.header.updated"));
    fileViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "label.value.header.createdby"));
    fileViewMap.put("FolderName", new FieldDescriptor("Folder", 10, false, "label.value.header.folder"));
    
    mocViewMap.put("Type", new FieldDescriptor("Type", 2, false, "label.value.header.type"));
    mocViewMap.put("Content", new FieldDescriptor("Content", 3, false, "label.value.header.content"));
    mocViewMap.put("Notes", new FieldDescriptor("Notes", 4, false, "label.value.header.notes"));
    mocViewMap.put("SyncAs", new FieldDescriptor("Sync As", 5, false, "label.value.header.syncas"));
    
    addressViewMap.put("Street1", new FieldDescriptor("Address 1", 2, false, "label.value.header.address1"));
    addressViewMap.put("Street2", new FieldDescriptor("Address 2", 3, false, "label.value.header.address2"));
    addressViewMap.put("City", new FieldDescriptor("City", 4, false, "label.value.header.city"));
    addressViewMap.put("State", new FieldDescriptor("State", 5, false, "label.value.header.state"));
    addressViewMap.put("ZipCode", new FieldDescriptor("Zip Code", 6, false, "label.value.header.zipcode"));
    addressViewMap.put("Country", new FieldDescriptor("Country", 7, false, "label.value.header.country"));

    expenseFormViewMap.put("ID", new FieldDescriptor("ID", 1, false, "label.value.header.id"));
    expenseFormViewMap.put("Employee", new FieldDescriptor("Employee", 3, false, "label.value.header.employee"));
    expenseFormViewMap.put("StartDate", new FieldDescriptor("Start Date", 4, false, "label.value.header.startdate"));
    expenseFormViewMap.put("EndDate", new FieldDescriptor("End Date", 5, false, "label.value.header.endate"));
    expenseFormViewMap.put("Amount", new FieldDescriptor("Amount", 6, false, "label.value.header.amount"));
    expenseFormViewMap.put("Status", new FieldDescriptor("Status", 7, false, "label.value.header.status"));
    expenseFormViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "label.value.header.createdby"));
    
    customFieldRIViewMap.put("Field", new FieldDescriptor("Field Name", 2, false, "label.value.header.fieldname"));
    customFieldRIViewMap.put("Value", new FieldDescriptor("Value", 4, false, "label.value.header.value"));
    
    historyViewMap.put("Date", new FieldDescriptor("Date", 2, false, "label.value.header.date"));
    historyViewMap.put("User", new FieldDescriptor("User", 3, false, "label.value.header.user"));
    historyViewMap.put("Action", new FieldDescriptor("Action", 4, false, "label.value.header.action"));
    historyViewMap.put("Type", new FieldDescriptor("Type", 5, false, "label.value.header.type"));
    historyViewMap.put("RecordName", new FieldDescriptor("Name", 6, false, "label.value.header.name"));

    timeSheetViewMap.put("ID", new FieldDescriptor("ID", 1, false, "label.value.header.id"));
    timeSheetViewMap.put("Employee", new FieldDescriptor("Employee", 3, false, "label.value.header.employee"));
    timeSheetViewMap.put("StartDate", new FieldDescriptor("Start Date", 4, false, "label.value.header.startdate"));
    timeSheetViewMap.put("EndDate", new FieldDescriptor("End Date", 5, false, "label.value.header.enddate"));
    timeSheetViewMap.put("Duration", new FieldDescriptor("Duration", 6, false, "label.value.header.dauer"));
    timeSheetViewMap.put("CreatedBy", new FieldDescriptor("Created By", 7, false, "label.value.header.createdby"));
    
    employeeHandbookViewMap.put("Name", new FieldDescriptor("Name", 4, false, "label.value.header.name"));
    employeeHandbookViewMap.put("Description", new FieldDescriptor("Description", 5, false, "label.value.header.description"));
    employeeHandbookViewMap.put("Created", new FieldDescriptor("Created", 6, false, "label.value.header.created"));
    employeeHandbookViewMap.put("Updated", new FieldDescriptor("Updated", 7, false, "label.value.header.updated"));
    employeeHandbookViewMap.put("CreatedBy", new FieldDescriptor("Created By", 8, false, "label.value.header.createdby"));
    
    employeeListViewMap.put("Name", new FieldDescriptor("Name", 3, false, "label.value.header.name"));
    employeeListViewMap.put("Title", new FieldDescriptor("Title", 7, false, "label.value.header.title"));
    employeeListViewMap.put("Entity", new FieldDescriptor("Entity", 9, false, "label.value.header.entity"));
    employeeListViewMap.put("Phone", new FieldDescriptor("Phone", 17, false, "label.value.header.phone"));
    employeeListViewMap.put("Fax", new FieldDescriptor("Fax", 19, false, "label.value.header.fax"));
    employeeListViewMap.put("Email", new FieldDescriptor("Email", 18, false, "label.value.header.email"));
    
    listMemberViewMap.put("Entity", new FieldDescriptor("Entity Name", 2, false, "label.value.header.entityname"));
    listMemberViewMap.put("Individual", new FieldDescriptor("Primary Contact", 7, false, "label.value.header.primarycontact"));
    listMemberViewMap.put("PhoneNumber", new FieldDescriptor("Phone", 8, false, "label.value.header.phone"));
    listMemberViewMap.put("Email", new FieldDescriptor("Email", 9, false, "label.value.header.email"));

    emailLookupViewMap.put("To", new FieldDescriptor("To", TO_INDEX, false, "label.value.header.to"));
    emailLookupViewMap.put("Cc", new FieldDescriptor("Cc", CC_INDEX, false, "label.value.header.cc"));
    emailLookupViewMap.put("Bcc", new FieldDescriptor("Bcc", BCC_INDEX, false, "label.value.header.bcc"));
    emailLookupViewMap.put("Address", new FieldDescriptor("Address", 2, false, "label.value.header.address"));
    emailLookupViewMap.put("Name", new FieldDescriptor("Name", 3, false, "label.value.header.name"));

    eventAttendeeViewMap.put("individualname", new FieldDescriptor("Individual Name", 2, false, "label.value.header.individualname"));
    eventAttendeeViewMap.put("email", new FieldDescriptor("Email", 3, false, "label.value.header.email"));
    eventAttendeeViewMap.put("accepted", new FieldDescriptor("Accepted", 4, false, "label.value.header.accepted"));
    
    sourceViewMap.put("id", new FieldDescriptor("ID", 1, false, "label.value.header.id"));
    sourceViewMap.put("source", new FieldDescriptor("Source Name", 2, false, "label.value.header.sourcename"));
    
    locationViewMap.put("id", new FieldDescriptor("ID", 1, false, "label.value.header.id"));
    locationViewMap.put("location", new FieldDescriptor("Location Name", 2, false, "label.value.header.locationname"));
	
    userViewMap.put("UserName", new FieldDescriptor("User Name", 2, false, "label.value.header.username"));
    userViewMap.put("Name", new FieldDescriptor("Name", 3, false, "label.value.header.name"));
    userViewMap.put("Entity", new FieldDescriptor("Entity", 5, false, "label.value.header.entity"));
    userViewMap.put("Enabled", new FieldDescriptor("Enabled", 7, false, "label.value.header.enabled"));
    
    standardReportViewMap.put("ReportID", new FieldDescriptor("Report ID", 1, false, "label.value.header.reportid"));
    standardReportViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    standardReportViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    standardReportViewMap.put("View", new FieldDescriptor("Action", 4, false, "label.value.header.action"));

    // "NameOfField", ("Display Name", position in query, false, "key for resource bundle")
    literatureListViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    literatureListViewMap.put("File", new FieldDescriptor("File", 3, false, "label.value.header.file"));

    customViewListViewMap.put("ViewName", new FieldDescriptor("View Name", 2, false, "label.value.header.viewname"));
    customViewListViewMap.put("Module", new FieldDescriptor("Module", 3, false, "label.value.header.module"));
    customViewListViewMap.put("Record", new FieldDescriptor("Record", 4, false, "label.value.header.record"));

    garbageListViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    garbageListViewMap.put("Module", new FieldDescriptor("Module", 3, false, "label.value.header.module"));
    garbageListViewMap.put("Record", new FieldDescriptor("Record", 4, false, "label.value.header.record"));
    garbageListViewMap.put("Owner", new FieldDescriptor("Owner", 5, false, "label.value.header.owner"));
    garbageListViewMap.put("Deleted", new FieldDescriptor("Deleted By", 6, false, "label.value.header.deletedby"));

    atticListViewMap.put("Title", new FieldDescriptor("Title", 2, false, "label.value.header.title"));
    atticListViewMap.put("Module", new FieldDescriptor("Module", 3, false, "label.value.header.module"));
    atticListViewMap.put("Record", new FieldDescriptor("Record", 4, false, "label.value.header.datensatz"));
    atticListViewMap.put("Owner", new FieldDescriptor("Owner", 5, false, "label.value.header.owner"));
    atticListViewMap.put("Deleted", new FieldDescriptor("Deleted By", 6, false, "label.value.header.deletedby"));

    adhocReportViewMap.put("ReportID", new FieldDescriptor("Report ID", 1, false, "label.value.header.reportid"));
    adhocReportViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    adhocReportViewMap.put("Description", new FieldDescriptor("Description", 3, false, "label.value.header.description"));
    adhocReportViewMap.put("View", new FieldDescriptor("Action", 4, false, "label.value.header.action"));

    historyListViewMap.put("Date", new FieldDescriptor("Date", 2, false, "label.value.header.date"));
    historyListViewMap.put("User", new FieldDescriptor("User", 3, false, "label.value.header.user"));
    historyListViewMap.put("Action", new FieldDescriptor("Action", 4, false, "label.value.header.action"));
    historyListViewMap.put("Type", new FieldDescriptor("Type", 5, false, "label.value.header.type"));
    historyListViewMap.put("RecordName", new FieldDescriptor("Record Name", 6, false, "label.value.header.recordname"));
    
    securityProfileListViewMap.put("ProfileName", new FieldDescriptor("Profile Name", 2, false, "label.value.header.profilename"));
    securityProfileListViewMap.put("NoOfUsers", new FieldDescriptor("Number of Users", 3, false, "label.value.header.noofusers"));
    
    //Custom Fields mapping
    customFieldsListViewMap.put("Name", new FieldDescriptor("Name", 2, false, "label.value.header.name"));
    customFieldsListViewMap.put("Type", new FieldDescriptor("Type", 3, false, "label.value.header.type"));
    customFieldsListViewMap.put("Module", new FieldDescriptor("Module", 4, false, "label.value.header.module"));
    customFieldsListViewMap.put("Record", new FieldDescriptor("Record", 5, false, "label.value.header.record"));
    
    addressLookupViewMap.put("Address", new FieldDescriptor("Address", 2, false, "label.value.header.address"));
    
    radioSelectedIndexAttendee.add(new Integer(ATTENDEEROWID));
    radioSelectedIndexAttendee.add(new Integer(ATTENDEEFIRSTNAME));
    radioSelectedIndexAttendee.add(new Integer(ATTENDEELASTNAME));

    
    //define the index for the radio selection value  
    ArrayList radioSelectedIndexEntity = new ArrayList();
    radioSelectedIndexEntity.add("Entity");
    radioSelectedIndexEntity.add(new Integer(2));
    radioSelectedIndexEntity.add(new Integer(1));
    radioSelectedIndexEntity.add(new Integer(5));
    radioSelectedIndexEntity.add(new Integer(4));

    ArrayList radioSelectedIndexIndividual = new ArrayList();
    radioSelectedIndexIndividual.add("Individual");
    radioSelectedIndexIndividual.add(new Integer(4));
    radioSelectedIndexIndividual.add(new Integer(1));
    radioSelectedIndexIndividual.add(new Integer(5));
    radioSelectedIndexIndividual.add(new Integer(6));
    radioSelectedIndexIndividual.add(new Integer(7));
    radioSelectedIndexIndividual.add(new Integer(8));

    ArrayList radioSelectedIndexOpportunity = new ArrayList();
    radioSelectedIndexOpportunity.add("Opportunity");
    radioSelectedIndexOpportunity.add(new Integer(2));
    radioSelectedIndexOpportunity.add(new Integer(1));
    radioSelectedIndexOpportunity.add(new Integer(6));
    radioSelectedIndexOpportunity.add(new Integer(7));

    ArrayList radioSelectedIndexTicket = new ArrayList();
    radioSelectedIndexTicket.add("Ticket");
    radioSelectedIndexTicket.add(new Integer(1));
    radioSelectedIndexTicket.add(new Integer(2));

    ArrayList radioSelectedIndexSource = new ArrayList();
    radioSelectedIndexSource.add("Source");
    radioSelectedIndexSource.add(new Integer(2));
    radioSelectedIndexSource.add(new Integer(1));

    ArrayList radioSelectedIndexfile = new ArrayList();
    radioSelectedIndexfile.add("File");
    radioSelectedIndexfile.add(new Integer(4));
    radioSelectedIndexfile.add(new Integer(1));
    
    ArrayList radioSelectedIndexGroup = new ArrayList();
    radioSelectedIndexGroup.add("Group");
    radioSelectedIndexGroup.add(new Integer(2));
    radioSelectedIndexGroup.add(new Integer(1));

    ArrayList radioSelectedIndexProject = new ArrayList();
    radioSelectedIndexProject.add("Project");
    radioSelectedIndexProject.add(new Integer(2));
    radioSelectedIndexProject.add(new Integer(1));

    ArrayList radioSelectedIndexTask = new ArrayList();
    radioSelectedIndexTask.add("Tasks");
    radioSelectedIndexTask.add(new Integer(2));
    radioSelectedIndexTask.add(new Integer(1));

    ArrayList radioSelectedIndexItem = new ArrayList();
    radioSelectedIndexItem.add("Item");
    radioSelectedIndexItem.add(new Integer(2));
    radioSelectedIndexItem.add(new Integer(1));
    radioSelectedIndexItem.add(new Integer(3));
    radioSelectedIndexItem.add(new Integer(10));
    radioSelectedIndexItem.add(new Integer(11));
    radioSelectedIndexItem.add(new Integer(12));
    radioSelectedIndexItem.add(new Integer(13));
    
    ArrayList radioSelectedIndexOrder = new ArrayList();
    radioSelectedIndexOrder.add("Order");
    radioSelectedIndexOrder.add(new Integer(1));
    radioSelectedIndexOrder.add(new Integer(3));
    radioSelectedIndexOrder.add(new Integer(2));

    ArrayList radioSelectedIndexVendor = new ArrayList();
    radioSelectedIndexVendor.add("Vendor");
    radioSelectedIndexVendor.add(new Integer(2));
    radioSelectedIndexVendor.add(new Integer(1));
    
    ArrayList radioSelectedIndexLocation = new ArrayList();
    radioSelectedIndexLocation.add("Location");
    radioSelectedIndexLocation.add(new Integer(2));
    radioSelectedIndexLocation.add(new Integer(1));

    ArrayList radioSelectedIndexAddress = new ArrayList();
    radioSelectedIndexAddress.add("address");
    radioSelectedIndexAddress.add(new Integer(2));
    radioSelectedIndexAddress.add(new Integer(1));
    radioSelectedIndexAddress.add(new Integer(3));
    
    
    //We will assign the RADIO_SELECTION_MAP then assign the index to the key
    RADIO_SELECTION_MAP.put(new Integer(ENTITY_LIST_TYPE), radioSelectedIndexEntity);
    RADIO_SELECTION_MAP.put(new Integer(INDIVIDUAL_LIST_TYPE), radioSelectedIndexIndividual);
    RADIO_SELECTION_MAP.put(new Integer(OPPORTUNITY_LIST_TYPE), radioSelectedIndexOpportunity);
    RADIO_SELECTION_MAP.put(new Integer(TICKET_LIST_TYPE), radioSelectedIndexTicket);
    RADIO_SELECTION_MAP.put(new Integer(SOURCE_LIST_TYPE), radioSelectedIndexSource);
    RADIO_SELECTION_MAP.put(new Integer(FILE_LIST_TYPE), radioSelectedIndexfile);
    RADIO_SELECTION_MAP.put(new Integer(GROUP_LOOKUP_LIST_TYPE), radioSelectedIndexGroup);
    RADIO_SELECTION_MAP.put(new Integer(PROJECT_LIST_TYPE), radioSelectedIndexProject);
    RADIO_SELECTION_MAP.put(new Integer(TASKS_LIST_TYPE), radioSelectedIndexTask);
    RADIO_SELECTION_MAP.put(new Integer(ITEM_LIST_TYPE), radioSelectedIndexItem);
    RADIO_SELECTION_MAP.put(new Integer(ORDER_LIST_TYPE), radioSelectedIndexOrder);
    RADIO_SELECTION_MAP.put(new Integer(VENDOR_LIST_TYPE), radioSelectedIndexVendor);
    RADIO_SELECTION_MAP.put(new Integer(LOCATION_LIST_TYPE), radioSelectedIndexLocation);
    RADIO_SELECTION_MAP.put(new Integer(ADDRESSLOOKUP_LIST_TYPE), radioSelectedIndexAddress);    
    
    
    // Module id mappings, like the variable says...
    moduleIdMap.put("14", "/contacts/entity_list.do");
    moduleIdMap.put("15", "/contacts/individual_list.do");
    moduleIdMap.put("16", "/contacts/group_list.do");
    moduleIdMap.put("30", "/sales/opportunity_list.do");
    moduleIdMap.put("31", "/sales/proposal_list.do");
    moduleIdMap.put("3", "/activities/activity_list.do");
    moduleIdMap.put("39", "/support/ticket_list.do");
    moduleIdMap.put("2", "/email/email_list.do");
    moduleIdMap.put("5", "/notes/note_list.do");
    moduleIdMap.put("32", "/marketing/listmanager_list.do");
    moduleIdMap.put("33", "/marketing/promotions_list.do");
    moduleIdMap.put("34", "/marketing/literaturefulfillment_list.do");
    moduleIdMap.put("35", "/marketing/events_list.do");
    moduleIdMap.put("36", "/projects/project_list.do");
    moduleIdMap.put("37", "/projects/task_list.do");
    moduleIdMap.put("38", "/projects/timeslip_list.do");
    moduleIdMap.put("40", "/support/faq_list.do");
    moduleIdMap.put("41", "/support/knowledgebase_list.do");
    moduleIdMap.put("42", "/accounting/order_list.do");
    moduleIdMap.put("56", "/accounting/invoice_list.do");
    moduleIdMap.put("43", "/accounting/payment_list.do");
    moduleIdMap.put("44", "/accounting/expense_list.do");
    moduleIdMap.put("45", "/accounting/purchaseorder_list.do");
    moduleIdMap.put("46", "/accounting/item_list.do");
    moduleIdMap.put("47", "/accounting/glaccount_list.do");
    moduleIdMap.put("48", "/accounting/inventory_list.do");
    moduleIdMap.put("50", "/accounting/vendor_list.do");
    moduleIdMap.put("6", "/files/file_list.do");
    moduleIdMap.put("52", "/hr/timesheet_list.do");
    moduleIdMap.put("53", "/hr/employeehandbook_list.do");
    moduleIdMap.put("54", "/hr/employeelist_list.do");
    moduleIdMap.put("65", "/adminitration/user_list.do");
    moduleIdMap.put("68", "/adminitration/security_profile_list.do");
    
    // List types
    listTypeMap.put("Entity", new Integer(ENTITY_LIST_TYPE));
    listTypeMap.put("Individual", new Integer(INDIVIDUAL_LIST_TYPE));    
    listTypeMap.put("Group", new Integer(GROUP_LIST_TYPE));    
    listTypeMap.put("Opportunity", new Integer(OPPORTUNITY_LIST_TYPE));
    listTypeMap.put("Ticket", new Integer(TICKET_LIST_TYPE));
    listTypeMap.put("Email", new Integer(EMAIL_LIST_TYPE));
    listTypeMap.put("Rule", new Integer(RULE_LIST_TYPE));
    listTypeMap.put("Proposal", new Integer(PROPOSALS_LIST_TYPE));
    listTypeMap.put("Template", new Integer(TEMPLATE_LIST_TYPE)); 
    listTypeMap.put("MultiActivity", new Integer(ACTIVITY_LIST_TYPE)); 
    listTypeMap.put("Appointment", new Integer(APPOINTMENT_LIST_TYPE)); 
    listTypeMap.put("Call", new Integer(CALL_LIST_TYPE)); 
    listTypeMap.put("ForecastSales", new Integer(FORECASTSALES_LIST_TYPE)); 
    listTypeMap.put("LiteratureRequest", new Integer(LITERATUREREQUEST_LIST_TYPE)); 
    listTypeMap.put("Meeting", new Integer(MEETING_LIST_TYPE)); 
    listTypeMap.put("NextAction", new Integer(NEXTACTION_LIST_TYPE)); 
    listTypeMap.put("ToDo", new Integer(TODO_LIST_TYPE)); 
    listTypeMap.put("ActivityTask", new Integer(TASK_LIST_TYPE)); 
    listTypeMap.put("Note", new Integer(NOTE_LIST_TYPE)); 
    listTypeMap.put("Marketing", new Integer(LISTMANAGER_LIST_TYPE)); 
    listTypeMap.put("Promotion", new Integer(PROMOTION_LIST_TYPE));
    listTypeMap.put("LiteratureFulfillment", new Integer(LITERATUREFULFILLMENT_LIST_TYPE)); 
    listTypeMap.put("Event", new Integer(EVENT_LIST_TYPE));
    listTypeMap.put("Project", new Integer(PROJECT_LIST_TYPE));
    listTypeMap.put("Tasks", new Integer(TASKS_LIST_TYPE));
    listTypeMap.put("Timeslip", new Integer(TIMESLIPS_LIST_TYPE));
    listTypeMap.put("FAQ", new Integer(FAQ_LIST_TYPE));
    listTypeMap.put("Knowledgebase", new Integer(KNOWLEDGEBASE_LIST_TYPE));
    listTypeMap.put("Order", new Integer(ORDER_LIST_TYPE));
    listTypeMap.put("InvoiceHistory", new Integer(INVOICE_LIST_TYPE));
    listTypeMap.put("Payment", new Integer(PAYMENT_LIST_TYPE));
    listTypeMap.put("Expense", new Integer(EXPENSES_LIST_TYPE));
    listTypeMap.put("PurchaseOrder", new Integer(PURCHASEORDER_LIST_TYPE));
    listTypeMap.put("Item", new Integer(ITEM_LIST_TYPE));
    listTypeMap.put("Glaccount", new Integer(GLACCOUNT_LIST_TYPE));
    listTypeMap.put("Inventory", new Integer(INVENTORY_LIST_TYPE));
    listTypeMap.put("Vendor", new Integer(VENDOR_LIST_TYPE));
    listTypeMap.put("File", new Integer(FILE_LIST_TYPE));
    listTypeMap.put("MOC", new Integer(MOC_LIST_TYPE));
    listTypeMap.put("Address", new Integer(ADDRESS_LIST_TYPE));
    listTypeMap.put("ExpenseForm", new Integer(EXPENSEFORM_LIST_TYPE));
    listTypeMap.put("CustomField", new Integer(CUSTOM_FIELD_RI_LIST_TYPE));
    listTypeMap.put("History", new Integer(HISTORY_LIST_TYPE));
    listTypeMap.put("TimeSheet", new Integer(TIMESHEET_LIST_TYPE));
    listTypeMap.put("EmployeeHandbook", new Integer(EMPLOYEE_HANDBOOK_LIST_TYPE));
    listTypeMap.put("EmployeeList", new Integer(EMPLOYEE_LIST_TYPE));
    listTypeMap.put("GroupMember", new Integer(GROUP_MEMBER_LIST_TYPE));
    listTypeMap.put("User", new Integer(USER_LIST_TYPE));
    listTypeMap.put("Order", new Integer(ORDER_LIST_TYPE));
    listTypeMap.put("StandardReport", new Integer(STANDARD_REPORT_LIST_TYPE));
    listTypeMap.put("Literature", new Integer(LITERATURE_LIST_TYPE));
    listTypeMap.put("CustomView", new Integer(CUSTOM_VIEW_LIST_TYPE));
    listTypeMap.put("CVGarbage", new Integer(GARBAGE_LIST_TYPE));
    listTypeMap.put("CVAttic", new Integer(ATTIC_LIST_TYPE));
    listTypeMap.put("AdHocReport", new Integer(ADHOC_REPORT_LIST_TYPE));
    listTypeMap.put("History", new Integer(DATA_HISTORY_LIST_TYPE));
    listTypeMap.put("SecurityProfile", new Integer(SECURITY_PROFILE_LIST_TYPE));
    listTypeMap.put("bottomContacts", new Integer(BOTTOM_INDIVIDUAL_LIST_TYPE));
    listTypeMap.put("CustomFields", new Integer(CUSTOM_FIELDS_LIST_TYPE));
    listTypeMap.put("AddressLookup", new Integer(ADDRESSLOOKUP_LIST_TYPE));
    
  }

  // This array stores the appropriate ValueListClassMapping at the index matching
  // the above constants.
  // The ValueList EJB uses this and Reflection to call the appropriate EJB
  // to get a ValueList back.
  // THE VALUES OF THIS ARRAY MUST BE IN THE CORRECT ORDER!!! ADD YOUR NEW ENTRY AT THE END!!!
  public static final ValueListClassMapping[] listTypeMapping = 
  {
    null, // empty place holder at listTypeMapping[0] -- (yeah, it's really ghetto)
    new ValueListClassMapping("local/ContactList", "getEntityValueList"),
    new ValueListClassMapping("local/ContactList", "getIndividualValueList"),
    new ValueListClassMapping("local/ContactList", "getGroupValueList"),
    new ValueListClassMapping("local/SaleList", "getOpportunityValueList"),
    new ValueListClassMapping("local/SaleList", "getProposalsValueList"),
    new ValueListClassMapping("local/ActivityList", "getActivityValueList"),
    new ValueListClassMapping("local/ActivityList", "getActivityValueList"),
    new ValueListClassMapping("local/ActivityList", "getActivityValueList"),
    new ValueListClassMapping("local/SaleList", "getOpportunityValueList"),
    new ValueListClassMapping("local/MarketingList", "getLitValueList"),
    new ValueListClassMapping("local/ActivityList", "getActivityValueList"),
    new ValueListClassMapping("local/ActivityList", "getActivityValueList"),
    new ValueListClassMapping("local/ActivityList", "getActivityValueList"),
    new ValueListClassMapping("local/ProjectLists", "getTaskValueList"),
    new ValueListClassMapping("local/SupportList", "getTicketValueList"),
    new ValueListClassMapping("local/Mail", "getEmailValueList"),
    new ValueListClassMapping("local/Mail", "getRuleValueList"),
    new ValueListClassMapping("local/Printtemplate", "getTemplateValueList"),
    new ValueListClassMapping("local/Note", "getNoteValueList"),
    new ValueListClassMapping("local/MarketingList", "getListManagerValueList"),
    new ValueListClassMapping("local/MarketingList", "getPromotionValueList"),
    new ValueListClassMapping("local/MarketingList", "getEventValueList"),
    new ValueListClassMapping("local/ProjectLists", "getProjectValueList"),
    new ValueListClassMapping("local/ProjectLists", "getTaskValueList"),
    new ValueListClassMapping("local/ProjectLists", "getTimeslipsValueList"),
    new ValueListClassMapping("local/MarketingList", "getLitValueList"),
    new ValueListClassMapping("local/SupportList", "getFAQValueList"),
    new ValueListClassMapping("local/AccountList", "getOrderValueList"),
    new ValueListClassMapping("local/AccountList", "getInvoiceValueList"),
    new ValueListClassMapping("local/AccountList", "getPaymentValueList"),
    new ValueListClassMapping("local/AccountList", "getExpenseValueList"),
    new ValueListClassMapping("local/AccountList", "getPurchaseOrderValueList"),
    new ValueListClassMapping("local/AccountList", "getItemValueList"),
    new ValueListClassMapping("local/AccountList", "getGlAccountValueList"),
    new ValueListClassMapping("local/AccountList", "getInventoryValueList"),
    new ValueListClassMapping("local/ContactList", "getEntityValueList"),
    new ValueListClassMapping("local/SupportList", "getKnowledgeBaseValueList"),
    new ValueListClassMapping("local/CvFile", "getFileValueList"),
    new ValueListClassMapping("local/ContactList", "getMOCValueList"),
    new ValueListClassMapping("local/ContactList", "getAddressValueList"),
    new ValueListClassMapping("local/HrLists", "getExpenseFormValueList"),
    new ValueListClassMapping("local/CustomField", "getCustomFieldValueList"),
    new ValueListClassMapping("local/AdminList", "getHistoryValueList"),
    new ValueListClassMapping("local/HrLists", "getTimeSheetValueList"),
    new ValueListClassMapping("local/CvFile", "getFileValueList"),
    new ValueListClassMapping("local/ContactList", "getIndividualValueList"),
    new ValueListClassMapping("local/ContactList", "getEntityValueList"),
    new ValueListClassMapping("local/Note", "getNoteValueList"),
    new ValueListClassMapping("local/ContactList", "getEmailLookupValueList"),
    new ValueListClassMapping("local/MarketingList", "getEventAttendeeValueList"),
    new ValueListClassMapping("local/CommonHelper", "getSourceValueList"),
    new ValueListClassMapping("local/ContactList", "getIndividualValueList"),
    new ValueListClassMapping("local/ContactList", "getGroupValueList"),
    new ValueListClassMapping("local/User", "getUserValueList"),
    new ValueListClassMapping("local/Mail", "getEmailValueList"),
    new ValueListClassMapping("local/AccountList", "getOrderValueList"),
    new ValueListClassMapping("local/AccountList", "getInvoiceValueList"),
    new ValueListClassMapping("local/AccountList", "getPaymentValueList"),
    new ValueListClassMapping("local/SupportList", "getTicketValueList"),
    new ValueListClassMapping("local/SupportList", "getFAQValueList"),
    new ValueListClassMapping("local/MarketingList", "getEventValueList"),
    new ValueListClassMapping("local/User", "getUserValueList"),
    new ValueListClassMapping("local/CvFile", "getFileValueList"),
    new ValueListClassMapping("local/CommonHelper", "getLocationValueList"),
    new ValueListClassMapping("report/ejb/session/ReportFacadeLocal", "getStandardReportList"),
    new ValueListClassMapping("local/AdminList", "getLiteratureList"),
    new ValueListClassMapping("local/AdminList", "getCustomViewList"),
    new ValueListClassMapping("local/AdminList", "getGarbageList"),
    new ValueListClassMapping("local/AdminList", "getAtticList"),
    new ValueListClassMapping("report/ejb/session/ReportFacadeLocal", "getAdHocReportList"),
    new ValueListClassMapping("local/AdminList", "getHistoryList"),
    new ValueListClassMapping("local/Authorization", "getSecurityProfileList"),
    new ValueListClassMapping("local/ContactList", "getIndividualValueList"),
    new ValueListClassMapping("local/CustomField", "getCustomFieldsValueList"),
    new ValueListClassMapping("local/ContactList", "getAddressLookupValueList"),
  };
  
  // The display elements.
  public static final String AMP = "&amp;";
  public static final String OPEN = "<";
  public static final String CLOSE = ">";
  public static final String CLOSING_OPEN = "</";
  public static final String CLOSING_CLOSE = "/>";
  public static final String TABLE = "table";
  public static final String COLUMN_HEAD = "th";
  public static final String ROW = "tr";
  public static final String COLUMN = "td";
  public static final String TABLE_HEAD = "thead";
  public static final String TABLE_BODY = "tbody";
  public static final String TABLE_FOOT = "tfoot";
  public static final String SPAN = "span";
  public static final String TABLE_OPEN = OPEN + TABLE + CLOSE + "\n";
  public static final String TABLE_PART_OPEN = OPEN + TABLE + " ";
  public static final String TABLE_CLOSE = CLOSING_OPEN + TABLE + CLOSE + "\n";
  public static final String HEAD_OPEN = OPEN + TABLE_HEAD + CLOSE;
  public static final String HEAD_CLOSE = CLOSING_OPEN + TABLE_HEAD + CLOSE + "\n";
  public static final String BODY_OPEN = OPEN + TABLE_BODY + CLOSE + "\n";
  public static final String BODY_CLOSE = CLOSING_OPEN + TABLE_BODY + CLOSE + "\n";
  public static final String FOOT_OPEN = OPEN + TABLE_FOOT + CLOSE;
  public static final String FOOT_CLOSE = CLOSING_OPEN + TABLE_FOOT + CLOSE + "\n";
  public static final String ROW_OPEN = OPEN + ROW + CLOSE;
  public static final String EVEN_ROW_OPEN = OPEN + ROW + " class=\"contactTableRowEven\"" + CLOSE;
  public static final String ODD_ROW_OPEN = OPEN + ROW + " class=\"contactTableRowOdd\"" + CLOSE;
  public static final String ROW_CLOSE = CLOSING_OPEN + ROW + CLOSE + "\n";
  public static final String COLUMN_OPEN = OPEN + COLUMN + CLOSE;
  public static final String COLUMN_PART_OPEN = OPEN + COLUMN + " ";
  public static final String COLUMN_CLOSE = CLOSING_OPEN + COLUMN + CLOSE;
  public static final String COLUMN_HEAD_OPEN = OPEN + COLUMN_HEAD + CLOSE;
  public static final String COLUMN_HEAD_PART_OPEN = OPEN + COLUMN_HEAD + " ";
  public static final String COLUMN_HEAD_CLOSE = CLOSING_OPEN + COLUMN_HEAD + CLOSE;
  public static final String ANCHOR_PART_OPEN = OPEN + "a ";
  public static final String ANCHOR_CLOSE = CLOSING_OPEN + "a" + CLOSE;
  public static final String SPAN_PART_OPEN = OPEN + SPAN + " ";
  public static final String SPAN_CLOSE = CLOSING_OPEN + SPAN + CLOSE;
  public static final String INPUT_PART_OPEN = OPEN + "input ";
  /** hardcoded stuff for the icon column, the check all, and the checkbox column. */
  public static final String CHECKBOX_FIRST_HALF = OPEN + "input type=\"checkbox\" class=\"checkBox\" name=\"";
  public static final String CHECKBOX_SECOND_HALF = "\" value=\"%\" onclick=\"vl_selectRow(this, #);\" " + CLOSING_CLOSE;
  // hardcoded stuff for radio column.
  public static final String RADIO = OPEN + "input type=\"@\" name=\"rowId\" value=\"%\" onClick=\"javascript:lu_radioSelect(#);\" " + CLOSING_CLOSE;
  /** listRenderer Form */
  public static final String FORM_OPEN = "<form name=\"listrenderer\" method=\"post\">";
  public static final String FORM_CLOSE = "</form>";
  // button type mappings
  public static final int CREATE_BUTTON = 0;
  public static final int VIEW_BUTTON = 1;
  public static final int DELETE_BUTTON = 2;
}
