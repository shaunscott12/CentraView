/*
 * $RCSfile: Globals.java,v $    $Revision: 1.14 $  $Date: 2005/09/27 19:27:41 $ - $Author: mcallist $
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

package com.centraview.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.util.LabelValueBean;

import com.centraview.common.menu.LeftNavigation;
import com.centraview.common.menu.MainNavigation;
import com.centraview.common.menu.MenuItem;
import com.centraview.common.menu.NestedMenuItem;

/**
 * This class will just contain static globals to be accessed in
 * the Tomcat4 VM.
 * @author CentraView, LLC <info@centraview.com>
 */
public class Globals
{
  static public String DEBUG = "com.centraview.debug";
  /** the moc types */
  static public ArrayList MOC_TYPE = new ArrayList();
  /**
   * The UIATTRIBUTES simply maps the name of an ActionForward
   * to the associated UIAttributes object.  This object is used
   * by the Tiles layouts and tiles to write the title in the HTML
   * and the main and secondary navigations.
   * 
   * This is populated by a bunch of objects that simply put
   * objects on the map.  The different objects are separated to ease
   * contention in the development environment.
   * 
   * The objects can be found under com.centraview.common.ui.*
   * They are named the same way the struts and tiles config files
   * are, separated by module.
   */
  static public HashMap UIATTRIBUTES = new HashMap();
  
  /**
   * A set of default attributes for times when the key is busted. 
   */
  static public UIAttributes DEFAULT_ATTRIBUTES;
  static public MainNavigation DEFAULT_TABS;
  static public MainNavigation CUSTOMER_TABS;
  static public LeftNavigation DEFAULT_MENU;

  /**
   * The following are indexes for the main menu.
   */
  static public int HOME_TAB = 1;
  static public int CONTACTS_TAB = 2;
  static public int EMAIL_TAB = 3;
  static public int CALENDAR_TAB = 4;
  static public int ACTIVITIES_TAB = 5;
  static public int NOTES_TAB = 6;
  static public int FILES_TAB = 7;
  static public int SALES_TAB = 8;
  static public int MARKETING_TAB = 9;
  static public int PROJECTS_TAB = 10;
  static public int SUPPORT_TAB = 11;
  static public int ACCOUNTING_TAB = 12;
  static public int HR_TAB = 13;
  static public int ADDITIONAL_TAB = 14;
  static public int CUST_HOME_TAB = 15;
  static public int CUST_PROFILE_TAB = 16;
  static public int CUST_EMAIL_TAB = 17;
  static public int CUST_ACCOUNT_TAB = 18;
  static public int CUST_SUPPORT_TAB = 19;
  static public int CUST_EVENTS_TAB = 20;
  static public int CUST_USERS_TAB = 21;
  static public int CUST_FILES_TAB = 22;
  static public int CUST_CONTACT_TAB = 23;

  public static HashMap tabsMap = new HashMap();

  static
  {
    // These values map the index of a tab to a String that is used as
    // a parameter to ModuleFieldRightsMatrix.isModuleVisible(). Enables
    // us to determine whether to print the tab or not
    tabsMap.put("Home", new Integer(HOME_TAB));
    tabsMap.put("Contacts", new Integer(CONTACTS_TAB));
    tabsMap.put("Email", new Integer(EMAIL_TAB));
    tabsMap.put("Calendar", new Integer(CALENDAR_TAB));
    tabsMap.put("Activities", new Integer(ACTIVITIES_TAB));
    tabsMap.put("Notes", new Integer(NOTES_TAB));
    tabsMap.put("File", new Integer(FILES_TAB));
    tabsMap.put("Sales", new Integer(SALES_TAB));
    tabsMap.put("Marketing", new Integer(MARKETING_TAB));
    tabsMap.put("Projects", new Integer(PROJECTS_TAB));
    tabsMap.put("Support", new Integer(SUPPORT_TAB));
    tabsMap.put("Accounting", new Integer(ACCOUNTING_TAB));
    tabsMap.put("HumanResources", new Integer(HR_TAB));


    MOC_TYPE.add(new LabelValueBean("Fax", "2"));
    MOC_TYPE.add(new LabelValueBean("Mobile", "3"));
    MOC_TYPE.add(new LabelValueBean("Main", "4"));
    MOC_TYPE.add(new LabelValueBean("Home", "5"));
    MOC_TYPE.add(new LabelValueBean("Other", "6"));
    MOC_TYPE.add(new LabelValueBean("Pager", "7"));
    MOC_TYPE.add(new LabelValueBean("Work", "8"));

    // Setup the default tabs
    ArrayList tabList = new ArrayList();
    tabList.add(new MenuItem("Home", "/home.do", HOME_TAB,"label.menu.home"));
    tabList.add(new MenuItem("Contacts", "/contacts/entity_list.do", CONTACTS_TAB,"label.menu.contacts"));
    tabList.add(new MenuItem("Email", "/email/email_list.do", EMAIL_TAB,"label.menu.email"));
    tabList.add(new MenuItem("Calendar", "/calendar.do", CALENDAR_TAB,"label.menu.calendar"));
    tabList.add(new MenuItem("Activities", "/activities/activity_list.do", ACTIVITIES_TAB,"label.menu.activities"));
    tabList.add(new MenuItem("Notes", "/notes/note_list.do", NOTES_TAB,"label.menu.notes"));
    tabList.add(new MenuItem("Files", "/files/file_list.do", FILES_TAB,"label.menu.files"));
    tabList.add(new MenuItem("Sales", "/sales/opportunity_list.do", SALES_TAB,"label.menu.sales"));
    tabList.add(new MenuItem("Marketing", "/marketing/listmanager_list.do", MARKETING_TAB,"label.menu.marketing"));
    tabList.add(new MenuItem("Projects", "/projects/project_list.do", PROJECTS_TAB,"label.menu.projects"));
    tabList.add(new MenuItem("Support", "/support/ticket_list.do", SUPPORT_TAB,"label.menu.support"));
    tabList.add(new MenuItem("Accounting", "/accounting/order_list.do", ACCOUNTING_TAB,"label.menu.accounting"));
    tabList.add(new MenuItem("HR", "/hr/expenseform_list.do", HR_TAB,"label.menu.hr"));

    // Setup the customer view tabs
    ArrayList customerTabs = new ArrayList();
    customerTabs.add(new MenuItem("Home", "/customer/home.do", CUST_HOME_TAB));
    customerTabs.add(new MenuItem("Customer Profile", "/customer/profile.do", CUST_PROFILE_TAB));
    customerTabs.add(new MenuItem("Email", "/customer/email_list.do", CUST_EMAIL_TAB));
    customerTabs.add(new MenuItem("Accounting", "/customer/order_list.do", CUST_ACCOUNT_TAB));
    customerTabs.add(new MenuItem("Support", "/customer/ticket_list.do", CUST_SUPPORT_TAB));
    customerTabs.add(new MenuItem("Events", "/customer/event_list.do", CUST_EVENTS_TAB));
    customerTabs.add(new MenuItem("Users", "/customer/user_list.do", CUST_USERS_TAB));
    customerTabs.add(new MenuItem("Files", "/customer/file_list.do", CUST_FILES_TAB));
    customerTabs.add(new MenuItem("Contact", "/customer/contact_us.do", CUST_CONTACT_TAB));

    Globals.DEFAULT_TABS = new MainNavigation(tabList);
    Globals.CUSTOMER_TABS = new MainNavigation(customerTabs);

    // Setup the default menu
    Globals.DEFAULT_MENU = new LeftNavigation("Empty Default Menu", new ArrayList());
    DEFAULT_ATTRIBUTES = new UIAttributes("CentraView: Default Title", 1, Globals.DEFAULT_MENU);
    
    // Login
    LeftNavigation loginMenu = new LeftNavigation("Login", new ArrayList());

    // customer login
    LeftNavigation customerLoginMenu = new LeftNavigation("Login", new ArrayList());
    
    // Forgot Password
    LeftNavigation forgotMenu = new LeftNavigation("Forgot Password", new ArrayList());

    // Home
    LeftNavigation homeMenu = new LeftNavigation("Home", new ArrayList());
    
    // Contacts
    ArrayList contactsItems = new ArrayList();
    contactsItems.add(new NestedMenuItem("Entities", "/contacts/entity_list.do", 1,"label.menu.entities"));
    contactsItems.add(new NestedMenuItem("Individuals", "/contacts/individual_list.do", 2,"label.menu.individuals"));
    contactsItems.add(new NestedMenuItem("Groups", "/contacts/group_list.do", 3,"label.menu.groups"));
    LeftNavigation contactMenu = new LeftNavigation("Contacts", contactsItems,"label.menu.contacts");

    //Calendar
    ArrayList calendarItems = new ArrayList();
    calendarItems.add(new NestedMenuItem("Daily View", "/calendar.do?Type=DAILY", 1,"label.menu.dailyview"));
    calendarItems.add(new NestedMenuItem("Weekly View (Columnar)", "/calendar.do?Type=WEEKLYCOLUMNS", 2,"label.menu.weeklyviewcolumnar"));
    calendarItems.add(new NestedMenuItem("Weekly View", "/calendar.do?Type=WEEKLY", 3,"label.menu.weeklyview"));
    calendarItems.add(new NestedMenuItem("Monthly View", "/calendar.do?Type=MONTHLY", 4,"label.menu.monthlyview"));
    calendarItems.add(new NestedMenuItem("Yearly View", "/calendar.do?Type=YEARLY", 5,"label.menu.yearlyview"));
    LeftNavigation calendarMenu = new LeftNavigation("Calendar", calendarItems,"label.menu.calendar");

    // Notes
    ArrayList noteItems = new ArrayList();
    noteItems.add(new NestedMenuItem("My Notes", "/notes/note_list.do?listScope=my", 1,"label.menu.mynotes"));
    noteItems.add(new NestedMenuItem("All Notes", "/notes/note_list.do?listScope=all", 2,"label.menu.allnotes"));
    LeftNavigation noteMenu = new LeftNavigation("Notes", noteItems,"label.menu.notes");
   
    //Activities
    ArrayList activitiesItems = new ArrayList();
    
    NestedMenuItem myActivitiesNestedItems = new NestedMenuItem("My Activities", "/activities/activity_list.do",1,"label.menu.myactivities");
    ArrayList myActivitiesItems = new ArrayList();
    myActivitiesItems.add(new NestedMenuItem("All My Activities", "/activities/activity_list.do?subScope=All", 1,"label.menu.allmyactivities"));
    myActivitiesItems.add(new NestedMenuItem("Appointments", "/activities/activity_list.do?subScope=Appointment", 2,"label.menu.appointments"));
    myActivitiesItems.add(new NestedMenuItem("Calls", "/activities/activity_list.do?subScope=Call", 3,"label.menu.calls"));
    myActivitiesItems.add(new NestedMenuItem("Forecast Sales", "/activities/activity_list.do?subScope=ForecastSales", 4,"label.menu.forecastsales"));
    myActivitiesItems.add(new NestedMenuItem("Literature Requests", "/activities/activity_list.do?subScope=LiteratureFulfillment", 5,"label.menu.literaturerequests"));
    myActivitiesItems.add(new NestedMenuItem("Meetings", "/activities/activity_list.do?subScope=Meeting", 6,"label.menu.meetings"));
    myActivitiesItems.add(new NestedMenuItem("Next Actions", "/activities/activity_list.do?subScope=NextAction", 7,"label.menu.nextactions"));
    myActivitiesItems.add(new NestedMenuItem("To Do's", "/activities/activity_list.do?subScope=ToDo", 8,"label.menu.todos"));
    myActivitiesItems.add(new NestedMenuItem("Tasks", "/activities/activity_list.do?subScope=ActivityTask", 9,"label.menu.tasks"));
    myActivitiesNestedItems.setItems(myActivitiesItems);
  
    NestedMenuItem allActivitiesNestedItems = new NestedMenuItem("All Activities", "/activities/activity_list.do?superScope=All", 2,"label.menu.allactivities");
    ArrayList allActivitiesItems = new ArrayList();
    allActivitiesItems.add(new NestedMenuItem("All Activities", "/activities/activity_list.do?superScope=All&subScope=All", 10,"label.menu.allactivities"));
    allActivitiesItems.add(new NestedMenuItem("Appointments", "/activities/activity_list.do?superScope=All&subScope=Appointment", 11,"label.menu.appointments"));
    allActivitiesItems.add(new NestedMenuItem("Calls", "/activities/activity_list.do?superScope=All&subScope=Call", 12,"label.menu.calls"));
    allActivitiesItems.add(new NestedMenuItem("Forecast Sales", "/activities/activity_list.do?superScope=All&subScope=ForecastSales", 13,"label.menu.forecastsales"));
    allActivitiesItems.add(new NestedMenuItem("Literature Requests", "/activities/activity_list.do?superScope=All&subScope=LiteratureRequest", 14,"label.menu.literaturerequests"));
    allActivitiesItems.add(new NestedMenuItem("Meetings", "/activities/activity_list.do?superScope=All&subScope=Meeting", 15,"label.menu.meetings"));
    allActivitiesItems.add(new NestedMenuItem("Next Actions", "/activities/activity_list.do?superScope=All&subScope=NextAction", 16,"label.menu.nextactions"));
    allActivitiesItems.add(new NestedMenuItem("To Do's", "/activities/activity_list.do?superScope=All&subScope=ToDo", 17,"label.menu.todos"));
    allActivitiesItems.add(new NestedMenuItem("Tasks", "/activities/activity_list.do?superScope=All&subScope=ActivityTask", 18,"label.menu.tasks"));
    allActivitiesNestedItems.setItems(allActivitiesItems);
    
    activitiesItems.add(myActivitiesNestedItems);
    activitiesItems.add(allActivitiesNestedItems);
    LeftNavigation activitiesMenu = new LeftNavigation("Activities", activitiesItems,"label.menu.activities");

    // Sales
    NestedMenuItem opportunityItem = new NestedMenuItem("Opportunities", "/sales/opportunity_list.do", 1,"label.menu.opportunities");
    ArrayList opportunitySubItems = new ArrayList();
    opportunitySubItems.add(new NestedMenuItem("My Opportunities", "/sales/opportunity_list.do?listScope=my", 1,"label.menu.myopportunities"));
    opportunitySubItems.add(new NestedMenuItem("All Opportunities", "/sales/opportunity_list.do?listScope=all", 2,"label.menu.allopportunities"));
    opportunityItem.setItems(opportunitySubItems);
    ArrayList saleItems = new ArrayList();
    saleItems.add(opportunityItem);
    saleItems.add(new NestedMenuItem("Proposals", "/sales/proposal_list.do", 2,"label.menu.proposals"));
    LeftNavigation saleMenu = new LeftNavigation("Sales", saleItems,"label.menu.sales");
    
    // Marketing
    ArrayList marketingItems = new ArrayList();
    marketingItems.add(new NestedMenuItem("List Manager", "/marketing/listmanager_list.do", 1,"label.menu.listmanager"));
    marketingItems.add(new NestedMenuItem("Promotions", "/marketing/promotions_list.do", 2,"label.menu.promotions"));
    marketingItems.add(new NestedMenuItem("Literature Fulfillment", "/marketing/literaturefulfillment_list.do", 3,"label.menu.literaturefulfillment"));
    marketingItems.add(new NestedMenuItem("Events", "/marketing/events_list.do", 4,"label.menu.events"));
    marketingItems.add(new NestedMenuItem("Mail Merge", "/marketing/mailmerge.do", 5,"label.menu.mailmerge"));
    LeftNavigation marketingMenu = new LeftNavigation("Marketing", marketingItems,"label.menu.marketing");

    // Projects
    ArrayList projectsItems = new ArrayList();
    projectsItems.add(new NestedMenuItem("Project", "/projects/project_list.do", 1,"label.menu.projects"));
    projectsItems.add(new NestedMenuItem("Tasks", "/projects/task_list.do", 2,"label.menu.tasks"));
    projectsItems.add(new NestedMenuItem("Time Slips", "/projects/timeslip_list.do", 3,"label.menu.timeslips"));
    LeftNavigation projectMenu = new LeftNavigation("Projects", projectsItems,"label.menu.projects");

    // Support
    NestedMenuItem ticketItem = new NestedMenuItem("Tickets", "/support/ticket_list.do", 1,"label.menu.tickets");
    ArrayList ticketSubItems = new ArrayList();
    ticketSubItems.add(new NestedMenuItem("My Tickets", "/support/ticket_list.do?listScope=MY", 1,"label.menu.mytickets"));
    ticketSubItems.add(new NestedMenuItem("All Tickets", "/support/ticket_list.do?listScope=ALL", 2,"label.menu.alltickets"));
    ticketItem.setItems(ticketSubItems);
    ArrayList supportItems = new ArrayList();
    supportItems.add(ticketItem);
    supportItems.add(new NestedMenuItem("FAQ's", "/support/faq_list.do", 2,"label.menu.faqs"));
    supportItems.add(new NestedMenuItem("Knowledgebase", "/support/knowledgebase_list.do", 3,"label.menu.knowledgebase"));
    LeftNavigation supportMenu = new LeftNavigation("Support", supportItems,"label.menu.support");

    //Accounting
    ArrayList accountingItems = new ArrayList();
    accountingItems.add(new NestedMenuItem("Order History", "/accounting/order_list.do", 1,"label.menu.orderhistory"));
    accountingItems.add(new NestedMenuItem("Invoice History", "/accounting/invoice_list.do", 2,"label.menu.invoicehistory"));
    accountingItems.add(new NestedMenuItem("Payments", "/accounting/payment_list.do", 3,"label.menu.payments"));
    accountingItems.add(new NestedMenuItem("Expenses", "/accounting/expense_list.do", 4,"label.menu.expenses"));
    accountingItems.add(new NestedMenuItem("Purchase Orders", "/accounting/purchaseorder_list.do", 5,"label.menu.purchaseorders"));
    accountingItems.add(new NestedMenuItem("Items", "/accounting/item_list.do", 6,"label.menu.items"));
    accountingItems.add(new NestedMenuItem("Gl Accounts", "/accounting/glaccount_list.do", 7,"label.menu.glaccounts"));
    accountingItems.add(new NestedMenuItem("Inventory", "/accounting/inventory_list.do", 8,"label.menu.inventory"));
    accountingItems.add(new NestedMenuItem("Vendors", "/accounting/vendor_list.do", 9,"label.menu.vendors"));
    LeftNavigation accountingMenu = new LeftNavigation("Accounting", accountingItems,"label.menu.accounting");

    // HR
    ArrayList hrItems = new ArrayList();
    hrItems.add(new NestedMenuItem("Expense Forms", "/hr/expenseform_list.do", 1,"label.menu.expenseforms"));
    hrItems.add(new NestedMenuItem("Time Sheets", "/hr/timesheet_list.do", 2,"label.menu.timesheets"));
    hrItems.add(new NestedMenuItem("Employee Handbook", "/hr/employeehandbook_list.do", 3,"label.menu.employeehandbook"));
    hrItems.add(new NestedMenuItem("Employee List", "/hr/employeelist_list.do", 4,"label.menu.employeelist"));
    hrItems.add(new NestedMenuItem("Suggestion Box", "/hr/suggestionbox_list.do", 5,"label.menu.suggestionbox"));
    LeftNavigation hrMenu = new LeftNavigation("HR", hrItems,"label.menu.hr");
    
    // Preference
    ArrayList preferenceItems = new ArrayList();
    NestedMenuItem preferenceGeneral = new NestedMenuItem("General", "/preference/user_profile.do", 1,"label.menu.general");
    ArrayList generalItems = new ArrayList();
    generalItems.add(new NestedMenuItem("User Profile", "/preference/user_profile.do", 5,"label.menu.userprofile"));
    generalItems.add(new NestedMenuItem("Synchronize", "/preference/display_sync.do", 12,"label.menu.synchronize"));
    preferenceGeneral.setItems(generalItems);
    NestedMenuItem preferenceHome = new NestedMenuItem("Home", "/preference/home_settings.do", 2,"label.menu.home");
    NestedMenuItem preferenceEmail = new NestedMenuItem("Email", "/preference/mail/account_list.do", 3,"label.menu.email");
    ArrayList prefEmailItems = new ArrayList();
    prefEmailItems.add(new NestedMenuItem("Accounts", "/preference/mail/account_list.do", 6,"label.menu.accounts"));
    prefEmailItems.add(new NestedMenuItem("Composition", "/preference/mail/composition.do", 7,"label.menu.composition"));
    prefEmailItems.add(new NestedMenuItem("Automatic Mail Check", "/preference/mail/auto_email_check.do", 8,"label.menu.automaticmailcheck"));
    prefEmailItems.add(new NestedMenuItem("Delegation", "/preference/mail/delegation.do", 9,"label.menu.delegation"));
    preferenceEmail.setItems(prefEmailItems);
    NestedMenuItem preferenceCalendar = new NestedMenuItem("Calendar", "/preference/calendar_settings.do", 4,"label.menu.calendar");
    ArrayList prefCalendarItems = new ArrayList();
    prefCalendarItems.add(new NestedMenuItem("General", "/preference/calendar_settings.do", 10,"label.menu.general"));
    prefCalendarItems.add(new NestedMenuItem("Delegation", "/preference/calendar_delegation.do?TYPEOFMODULE=Activities", 11,"label.menu.delegation"));
    preferenceCalendar.setItems(prefCalendarItems);
    preferenceItems.add(preferenceGeneral);
    preferenceItems.add(preferenceHome);
    preferenceItems.add(preferenceEmail);
    preferenceItems.add(preferenceCalendar);
    LeftNavigation preferenceMenu = new LeftNavigation("Preference", preferenceItems,"label.menu.preference");
    
   
    // Administration
    ArrayList adminItems = new ArrayList();
    NestedMenuItem userAdministration = new NestedMenuItem("User Administration", "/administration/user_list.do", 1,"label.menu.useradministration");
    ArrayList userItems = new ArrayList();
    userItems.add(new NestedMenuItem("Users", "/administration/user_list.do", 1,"label.menu.users"));
    userItems.add(new NestedMenuItem("Security Profiles", "/administration/security_profile_list.do",2,"label.menu.securityprofiles"));
    userAdministration.setItems(userItems);
    NestedMenuItem config = new NestedMenuItem("Configuration", "/administration/server_settings.do", 2,"label.menu.configuration");
    ArrayList configItems = new ArrayList();
    configItems.add(new NestedMenuItem("Server Settings", "/administration/server_settings.do", 3,"label.menu.serversettings"));
    configItems.add(new NestedMenuItem("Email Settings", "/administration/email_settings.do", 5,"label.menu.emailsettings"));
    configItems.add(new NestedMenuItem("Customer Logo", "/administration/customer_logo.do", 6,"label.menu.customerlogo"));
    config.setItems(configItems);
    NestedMenuItem dataAdmin = new NestedMenuItem("Data Administration", "/administration/garbage_list.do", 3,"label.menu.dataadministration");
    ArrayList dataItems = new ArrayList();
    dataItems.add(new NestedMenuItem("Garbage", "/administration/garbage_list.do", 7,"label.menu.garbage"));
    dataItems.add(new NestedMenuItem("Attic", "/administration/attic_list.do", 8,"label.menu.attic"));
    dataItems.add(new NestedMenuItem("Merge / Purge", "/administration/merge_search.do", 9,"label.menu.mergepurge"));
    dataItems.add(new NestedMenuItem("Global Replace", "/administration/global_replace.do", 10,"label.menu.globalreplace"));
    dataItems.add(new NestedMenuItem("History", "/administration/history_list.do", 11,"label.menu.history"));
    dataAdmin.setItems(dataItems);
    NestedMenuItem moduleSettings = new NestedMenuItem("Module Settings", "/administration/view_module_settings.do?typeofsubmodule=Contacts", 4,"label.menu.modulesettings");
    ArrayList moduleItems = new ArrayList();
    moduleItems.add(new NestedMenuItem("Contacts", "/administration/view_module_settings.do?typeofsubmodule=Contacts", 12,"label.menu.contacts"));
    moduleItems.add(new NestedMenuItem("Calendar", "/administration/view_module_settings.do?typeofsubmodule=Calendar", 13,"label.menu.calendar"));
    moduleItems.add(new NestedMenuItem("Activities", "/administration/view_module_settings.do?typeofsubmodule=Activities", 14,"label.menu.activities"));
    moduleItems.add(new NestedMenuItem("Notes", "/administration/view_module_settings.do?typeofsubmodule=Notes", 15,"label.menu.notes"));
    moduleItems.add(new NestedMenuItem("Files", "/administration/view_module_settings.do?typeofsubmodule=File", 16,"label.menu.files"));
    moduleItems.add(new NestedMenuItem("Sales", "/administration/view_module_settings.do?typeofsubmodule=Sales", 17,"label.menu.sales"));
    moduleItems.add(new NestedMenuItem("Marketing", "/administration/view_module_settings.do?typeofsubmodule=Marketing", 18,"label.menu.marketing"));
    moduleItems.add(new NestedMenuItem("Projects", "/administration/view_module_settings.do?typeofsubmodule=Projects", 19,"label.menu.projects"));
    moduleItems.add(new NestedMenuItem("Support", "/administration/view_module_settings.do?typeofsubmodule=Support", 20,"label.menu.support"));
    moduleItems.add(new NestedMenuItem("Accounting", "/administration/view_module_settings.do?typeofsubmodule=Accounting", 21,"label.menu.accounting"));
    moduleItems.add(new NestedMenuItem("HR", "/administration/view_module_settings.do?typeofsubmodule=HR", 22,"label.menu.hr"));
    moduleItems.add(new NestedMenuItem("Template Management", "/administration/template_list.do", 23,"label.menu.templatemanagement"));
    moduleSettings.setItems(moduleItems);
    
    adminItems.add(userAdministration);
    adminItems.add(config);
    adminItems.add(dataAdmin);
    adminItems.add(moduleSettings);
    LeftNavigation adminMenu = new LeftNavigation("Administration", adminItems,"label.menu.administration");

    ArrayList reportItems = new ArrayList();
    NestedMenuItem contactReports = new NestedMenuItem("Contacts", "/reports/standard_list.do?moduleId=14", 1,"label.menu.contacts");
    ArrayList contactItems = new ArrayList();
    contactItems.add(new NestedMenuItem("Standard (Entity)", "/reports/standard_list.do?moduleId=14", 1,"label.menu.standardentity"));
    contactItems.add(new NestedMenuItem("Standard (Individual)", "/reports/standard_list.do?moduleId=15", 22,"label.menu.standardindividual"));
    contactItems.add(new NestedMenuItem("Ad-Hoc (Entity)", "/reports/adhoc_list.do?moduleId=14", 2,"label.menu.adhocentity"));
    contactItems.add(new NestedMenuItem("Ad-Hoc (Individual)", "/reports/adhoc_list.do?moduleId=15", 3,"label.menu.adhocindividual"));
    contactReports.setItems(contactItems);
    NestedMenuItem activitiesReport = new NestedMenuItem("Activities", "/reports/standard_list.do?moduleId=3", 2,"label.menu.activities");
    ArrayList activitiesReportItems = new ArrayList();
    activitiesReportItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=3", 4,"label.menu.standard"));
    activitiesReportItems.add(new NestedMenuItem("Ad-Hoc", "/reports/adhoc_list.do?moduleId=3", 5,"label.menu.adhoc"));
    activitiesReport.setItems(activitiesReportItems);
    NestedMenuItem salesReport = new NestedMenuItem("Sales", "/reports/standard_list.do?moduleId=30", 3,"label.menu.sales");
    ArrayList salesItems = new ArrayList();
    salesItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=30", 6,"label.menu.standard"));
    salesItems.add(new NestedMenuItem("Ad-Hoc (Opportunities)", "/reports/adhoc_list.do?moduleId=30", 7,"label.menu.adhocopportunities"));
    salesItems.add(new NestedMenuItem("Ad-Hoc (Proposals)", "/reports/adhoc_list.do?moduleId=31", 8,"label.menu.adhocproposals"));
    salesReport.setItems(salesItems);
    NestedMenuItem marketingReport = new NestedMenuItem("Marketing", "/reports/standard_list.do?moduleId=33", 4,"label.menu.marketing");
    ArrayList marketingReportItems = new ArrayList();
    marketingReportItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=33", 9,"label.menu.standard"));
    marketingReportItems.add(new NestedMenuItem("Ad-Hoc (Promotion)", "/reports/adhoc_list.do?moduleId=33", 10,"label.menu.adhocpromotion"));
    marketingReport.setItems(marketingReportItems);
    NestedMenuItem projectReport = new NestedMenuItem("Projects", "/reports/standard_list.do?moduleId=36", 5,"label.menu.projects");
    ArrayList projectReportItems = new ArrayList();
    projectReportItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=36", 11,"label.menu.standard"));
    projectReportItems.add(new NestedMenuItem("Ad-Hoc (Project)", "/reports/adhoc_list.do?moduleId=36", 12,"label.menu.adhocproject"));
    projectReportItems.add(new NestedMenuItem("Ad-Hoc (Task)", "/reports/adhoc_list.do?moduleId=37", 13,"label.menu.adhoctask"));
    projectReportItems.add(new NestedMenuItem("Ad-Hoc (Timeslip)", "/reports/adhoc_list.do?moduleId=38", 14,"label.menu.adhoctimeslip"));
    projectReport.setItems(projectReportItems);
    NestedMenuItem supportReport = new NestedMenuItem("Support", "/reports/standard_list.do?moduleId=39", 6,"label.menu.support");
    ArrayList supportReportItems = new ArrayList();
    supportReportItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=39", 15,"label.menu.standard"));
    supportReportItems.add(new NestedMenuItem("Ad-Hoc (Ticket)", "/reports/adhoc_list.do?moduleId=39", 16,"label.menu.adhocticket"));
    supportReport.setItems(supportReportItems);
    NestedMenuItem accountingReport = new NestedMenuItem("Accounting", "/reports/standard_list.do?moduleId=42", 7,"label.menu.accounting");
    ArrayList accountingReportItems = new ArrayList();
    accountingReportItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=42", 17,"label.menu.standard"));
    accountingReportItems.add(new NestedMenuItem("Ad-Hoc (Order)", "/reports/adhoc_list.do?moduleId=42", 18,"label.menu.adhocorder"));
    accountingReportItems.add(new NestedMenuItem("Ad-Hoc (Inventory)", "/reports/adhoc_list.do?moduleId=48", 19,"label.menu.adhocinventory"));
    accountingReport.setItems(accountingReportItems);
    NestedMenuItem hrReport = new NestedMenuItem("HR", "/reports/standard_list.do?moduleId=52", 8,"label.menu.hr");
    ArrayList hrReportItems = new ArrayList();
    hrReportItems.add(new NestedMenuItem("Standard", "/reports/standard_list.do?moduleId=52", 20,"label.menu.standard"));
    hrReportItems.add(new NestedMenuItem("Ad-Hoc (TimeSheet)", "/reports/adhoc_list.do?moduleId=52", 21,"label.menu.adhoctimesheet"));
    hrReport.setItems(hrReportItems);
    reportItems.add(contactReports);
    reportItems.add(activitiesReport);
    reportItems.add(salesReport);
    reportItems.add(marketingReport);
    reportItems.add(projectReport);
    reportItems.add(supportReport);
    reportItems.add(accountingReport);
    reportItems.add(hrReport);
    LeftNavigation reportsMenu = new LeftNavigation("Reports", reportItems,"label.menu.reports");
    
    // mappings
    int loginSelected[] = {1,0};
    UIATTRIBUTES.put(".view.login", new UIAttributes("Login", HOME_TAB, loginMenu, loginSelected));
    UIATTRIBUTES.put(".view.login.failure", new UIAttributes("Login", HOME_TAB, loginMenu, loginSelected));
    UIATTRIBUTES.put(".view.forgot", new UIAttributes("Forgot Passsword", HOME_TAB, forgotMenu, loginSelected));
    
    // Home page
    UIATTRIBUTES.put(".view.home", new UIAttributes("Home", HOME_TAB, homeMenu));

    
    // Contacts
    int entitySelected[] = {1, 0};
    UIATTRIBUTES.put(".view.contacts.entitylist", new UIAttributes("Entities List", CONTACTS_TAB, contactMenu, entitySelected));
    int individualSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.contacts.individuallist", new UIAttributes("Individuals List", CONTACTS_TAB, contactMenu, individualSelected));
    int groupSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.contacts.grouplist", new UIAttributes("Group List", CONTACTS_TAB, contactMenu, groupSelected));
    UIATTRIBUTES.put(".view.contact.group_detail", new UIAttributes("Group Details", CONTACTS_TAB, contactMenu, groupSelected));
    UIATTRIBUTES.put(".view.contact.individualdetails", new UIAttributes("Individual Detail: ", CONTACTS_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.contacts.new_entity", new UIAttributes("New Entity", CONTACTS_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.contacts.new_individual", new UIAttributes("New Individual", CONTACTS_TAB, DEFAULT_MENU));

    // Email
    UIATTRIBUTES.put(".view.email.valuelist", new UIAttributes("Email", EMAIL_TAB, null, null));
    UIATTRIBUTES.put(".view.email.compose", new UIAttributes("Compose Message", 0, null, null));
    UIATTRIBUTES.put(".view.email.view_message", new UIAttributes("View Message", EMAIL_TAB, null, null));
    UIATTRIBUTES.put(".view.email.lookup.list", new UIAttributes("Lookup Email Address", 0, null, null));
    UIATTRIBUTES.put(".view.email.attachmentlookup", new UIAttributes("Attach Files", 0, null, null));
    UIATTRIBUTES.put(".view.email.newfolder", new UIAttributes("New Folder", 0, null, null));
    UIATTRIBUTES.put(".view.email.editfolder", new UIAttributes("Edit Folder", 0, null, null));
    UIATTRIBUTES.put(".view.email.commoncompose", new UIAttributes("Compose Message", 0, null, null));
    UIATTRIBUTES.put(".view.email.setup", new UIAttributes("Setup Account", EMAIL_TAB, null, null));

    // Rules
    UIATTRIBUTES.put(".view.email.rules.list", new UIAttributes("Email Rules", EMAIL_TAB, null, null));
    UIATTRIBUTES.put(".view.email.rules.new", new UIAttributes("New Rule", EMAIL_TAB, null, null));
    
    //Calendar
    int dailyViewSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.calendar.dailylist", new UIAttributes("Daily View", CALENDAR_TAB, calendarMenu, dailyViewSelected));
    int weeklyViewColumnarSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.calendar.weeklycolumnarlist", new UIAttributes("Weekly View (Columnar)", CALENDAR_TAB, calendarMenu, weeklyViewColumnarSelected));
    int weeklyViewSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.calendar.weeklylist", new UIAttributes("Weekly View", CALENDAR_TAB, calendarMenu, weeklyViewSelected));    
    int monthlyViewSelected[] = {4, 0};
    UIATTRIBUTES.put(".view.calendar.monthlylist", new UIAttributes("Monthly View", CALENDAR_TAB, calendarMenu, monthlyViewSelected));    
    int yearlyViewSelected[] = {5, 0};
    UIATTRIBUTES.put(".view.calendar.yearlylist", new UIAttributes("Yearly View", CALENDAR_TAB, calendarMenu, yearlyViewSelected));    

    //Activities
    int myActvitiySelected[] = {0, 1};
    UIATTRIBUTES.put(".view.activities.myactivitylist", new UIAttributes("All My Acitivities List", ACTIVITIES_TAB, activitiesMenu, myActvitiySelected));
    int myApointmentsSelected[] = {0, 2};
    UIATTRIBUTES.put(".view.activities.myappointmentslist", new UIAttributes("Appointments List", ACTIVITIES_TAB, activitiesMenu, myApointmentsSelected));    
    int myCallsSelected[] = {0, 3};
    UIATTRIBUTES.put(".view.activities.mycallslist", new UIAttributes("Calls List", ACTIVITIES_TAB, activitiesMenu, myCallsSelected));
    int myForecastSalesSelected[] = {0, 4};
    UIATTRIBUTES.put(".view.activities.myforecastsaleslist", new UIAttributes("Forecast Sales List", ACTIVITIES_TAB, activitiesMenu, myForecastSalesSelected));
    int myLiteratureRequestsSelected[] = {0, 5};
    UIATTRIBUTES.put(".view.activities.myliteraturerequestslist", new UIAttributes("Literature Requests List", ACTIVITIES_TAB, activitiesMenu, myLiteratureRequestsSelected));
    int myMeetingsSelected[] = {0, 6};
    UIATTRIBUTES.put(".view.activities.mymeetingslist", new UIAttributes("Meetings List", ACTIVITIES_TAB, activitiesMenu, myMeetingsSelected));
    int myNextActionsSelected[] = {0, 7};
    UIATTRIBUTES.put(".view.activities.mynextactionslist", new UIAttributes("Next Actions List", ACTIVITIES_TAB, activitiesMenu, myNextActionsSelected));
    int myToDosSelected[] = {0, 8};
    UIATTRIBUTES.put(".view.activities.mytodoslist", new UIAttributes("To Do's List", ACTIVITIES_TAB, activitiesMenu, myToDosSelected));
    int myTasksSelected[] = {0, 9};
    UIATTRIBUTES.put(".view.activities.mytaskslist", new UIAttributes("Tasks List", ACTIVITIES_TAB, activitiesMenu, myTasksSelected));

    int allActvitiySelected[] = {0, 10};
    UIATTRIBUTES.put(".view.activities.allactivitylist", new UIAttributes("All Acitivities List", ACTIVITIES_TAB, activitiesMenu, allActvitiySelected));
    int allApointmentsSelected[] = {0, 11};
    UIATTRIBUTES.put(".view.activities.allappointmentslist", new UIAttributes("Appointments List", ACTIVITIES_TAB, activitiesMenu, allApointmentsSelected));    
    int allCallsSelected[] = {0, 12};
    UIATTRIBUTES.put(".view.activities.allcallslist", new UIAttributes("Calls List", ACTIVITIES_TAB, activitiesMenu, allCallsSelected));
    int allForecastSalesSelected[] = {0, 13};
    UIATTRIBUTES.put(".view.activities.allforecastsaleslist", new UIAttributes("Forecast Sales List", ACTIVITIES_TAB, activitiesMenu, allForecastSalesSelected));
    int allLiteratureRequestsSelected[] = {0, 14};
    UIATTRIBUTES.put(".view.activities.allliteraturerequestslist", new UIAttributes("Literature Requests List", ACTIVITIES_TAB, activitiesMenu, allLiteratureRequestsSelected));
    int allMeetingsSelected[] = {0, 15};
    UIATTRIBUTES.put(".view.activities.allmeetingslist", new UIAttributes("Meetings List", ACTIVITIES_TAB, activitiesMenu, allMeetingsSelected));
    int allNextActionsSelected[] = {0, 16};
    UIATTRIBUTES.put(".view.activities.allnextactionslist", new UIAttributes("Next Actions List", ACTIVITIES_TAB, activitiesMenu, allNextActionsSelected));
    int allToDosSelected[] = {0, 17};
    UIATTRIBUTES.put(".view.activities.alltodoslist", new UIAttributes("To Do's List", ACTIVITIES_TAB, activitiesMenu, allToDosSelected));
    int allTasksSelected[] = {0, 18};
    UIATTRIBUTES.put(".view.activities.alltaskslist", new UIAttributes("Tasks List", ACTIVITIES_TAB, activitiesMenu, allTasksSelected));

    // Activity Popup Screens
    UIATTRIBUTES.put(".view.activities.new_activity", new UIAttributes("Schedule New Activity", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.activities.details", new UIAttributes("Schedule an Activity", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.activities.attendees", new UIAttributes("Activity: Select Attendees", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.activities.resources", new UIAttributes("Activity: Select Resources", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.activities.availability", new UIAttributes("Activity: View Availability", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.activities.recurring", new UIAttributes("Activity: Selet Recurrence", ACTIVITIES_TAB, DEFAULT_MENU));

    // Notes
    int noteMySelected[] = {1, 0};
    UIATTRIBUTES.put(".view.notes.list.my", new UIAttributes("Notes List", NOTES_TAB, noteMenu, noteMySelected));
    UIATTRIBUTES.put(".view.notes.editnote.my", new UIAttributes("Notes List", NOTES_TAB, noteMenu, noteMySelected));
    int noteAllSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.notes.list.all", new UIAttributes("Notes List", NOTES_TAB, noteMenu, noteAllSelected));
    UIATTRIBUTES.put(".view.notes.editnote.all", new UIAttributes("Notes List", NOTES_TAB, noteMenu, noteAllSelected));
    
    // Lookup
    UIATTRIBUTES.put(".view.lookup", new UIAttributes("Look Up: ", CONTACTS_TAB, DEFAULT_MENU));
    // Sales
    int opportunitySelected[] = {1, 0};
    UIATTRIBUTES.put(".view.sales.opportunity.list", new UIAttributes("Opportunities List", SALES_TAB, saleMenu, opportunitySelected));
    int opportunityMySelected[] = {0, 1};
    UIATTRIBUTES.put(".view.sales.opportunity.list.my", new UIAttributes("Opportunities List", SALES_TAB, saleMenu, opportunityMySelected));
    int opportunityAllSelected[] = {0, 2};
    UIATTRIBUTES.put(".view.sales.opportunity.list.all", new UIAttributes("Opportunities List", SALES_TAB, saleMenu, opportunityAllSelected));
    int proposalSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.sales.proposal.list", new UIAttributes("Proposals List", SALES_TAB, saleMenu, proposalSelected));
    UIATTRIBUTES.put(".view.sales.new_opportunity", new UIAttributes("Sales: New Opportunity", SALES_TAB, DEFAULT_MENU));


    //Marketing
    int listManagerSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.marketing.listmanager.list", new UIAttributes("List Manager", MARKETING_TAB, marketingMenu, listManagerSelected));
    UIATTRIBUTES.put(".view.marketing.editlistmanager", new UIAttributes("View List", MARKETING_TAB, marketingMenu, listManagerSelected));
    
    int promotionsSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.marketing.promotions.list", new UIAttributes("Promotions List", MARKETING_TAB, marketingMenu, promotionsSelected));
    UIATTRIBUTES.put(".view.marketing.new.promotion", new UIAttributes("New Promotions", MARKETING_TAB, marketingMenu, promotionsSelected));
    UIATTRIBUTES.put(".view.marketing.edit.promotion", new UIAttributes("Edit Promotions", MARKETING_TAB, marketingMenu, promotionsSelected));
        
    int literatureFulfillmentSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.marketing.literaturefulfillment.list", new UIAttributes("Literature Fulfillment", MARKETING_TAB, marketingMenu, literatureFulfillmentSelected));
    int eventsSelected[] = {4, 0};
    UIATTRIBUTES.put(".view.marketing.events.list", new UIAttributes("Events List", MARKETING_TAB, marketingMenu, eventsSelected));
    UIATTRIBUTES.put(".view.marketing.new.event", new UIAttributes("New Events", MARKETING_TAB, marketingMenu, eventsSelected));
    UIATTRIBUTES.put(".view.marketing.edit.event", new UIAttributes("Edit Events", MARKETING_TAB, marketingMenu, eventsSelected));
        int mailMergeSelected[] = {5, 0};
    UIATTRIBUTES.put(".view.marketing.mailmerge", new UIAttributes("Mail Merge", MARKETING_TAB, marketingMenu, mailMergeSelected));

    //Projects
    int projectSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.projects.project.list", new UIAttributes("Project", PROJECTS_TAB, projectMenu, projectSelected));
    int taskSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.projects.tasks.list", new UIAttributes("Tasks", PROJECTS_TAB, projectMenu, taskSelected));
    int timeslipSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.projects.timeslip.list", new UIAttributes("Time Slips", PROJECTS_TAB, projectMenu, timeslipSelected));    

    // Support
    int ticketsSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.support.tickets.list", new UIAttributes("Tickets List", SUPPORT_TAB, supportMenu, ticketsSelected));
    int ticketsMySelected[] = {0, 1};
    UIATTRIBUTES.put(".view.support.tickets.my.list", new UIAttributes("Tickets List", SUPPORT_TAB, supportMenu, ticketsMySelected));
    int ticketsAllSelected[] = {0, 2};
    UIATTRIBUTES.put(".view.support.tickets.all.list", new UIAttributes("Tickets List", SUPPORT_TAB, supportMenu, ticketsAllSelected));
    int faqSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.support.faq.list", new UIAttributes("FAQ List", SUPPORT_TAB, supportMenu, faqSelected));
    int knowledgeBaseSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.support.knowledgebase.list", new UIAttributes("Knowledgebase List", SUPPORT_TAB, supportMenu, knowledgeBaseSelected));
    
    UIATTRIBUTES.put(".view.support.knowledgebase.new", new UIAttributes("New Knowledgebase Entry", 0, null, null));
    UIATTRIBUTES.put(".view.support.knowledgebase.view", new UIAttributes("View Knowledgebase Entry", SUPPORT_TAB, supportMenu, knowledgeBaseSelected));
    UIATTRIBUTES.put(".view.support.knowledgebase.edit", new UIAttributes("Edit Knowledgebase Entry", 0, null, null));
    UIATTRIBUTES.put(".view.support.knowledgebase.newcat", new UIAttributes("New Knowledgebase Category", 0, null, null));
    UIATTRIBUTES.put(".view.support.knowledgebase.editcat", new UIAttributes("Edit Knowledgebase Category", 0, null, null));

    UIATTRIBUTES.put(".view.support.faq.view", new UIAttributes("View FAQ", SUPPORT_TAB, supportMenu, faqSelected));
    UIATTRIBUTES.put(".view.support.faq.edit", new UIAttributes("Edit FAQ", SUPPORT_TAB, supportMenu, faqSelected));
    UIATTRIBUTES.put(".view.support.faq.new", new UIAttributes("New FAQ", SUPPORT_TAB, supportMenu, faqSelected));
    UIATTRIBUTES.put(".view.support.faq.question.new", new UIAttributes("New Question", 0, null, null));    
    UIATTRIBUTES.put(".view.support.faq.question.edit", new UIAttributes("Edit Question", 0, null, null));    
    
    //Accounting
    int orderSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.accounting.orderlist", new UIAttributes("Order History", ACCOUNTING_TAB, accountingMenu, orderSelected));
    UIATTRIBUTES.put(".view.accounting.new_order", new UIAttributes("New Order", ACCOUNTING_TAB, accountingMenu, orderSelected));
    UIATTRIBUTES.put(".view.accounting.view_order", new UIAttributes("Order Details", ACCOUNTING_TAB, accountingMenu, orderSelected));
    int invoiceSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.accounting.invoicelist", new UIAttributes("Invoice History", ACCOUNTING_TAB, accountingMenu, invoiceSelected));
    UIATTRIBUTES.put(".view.accounting.view_invoice", new UIAttributes("Edit Invoice", ACCOUNTING_TAB, accountingMenu, invoiceSelected));
    UIATTRIBUTES.put(".view.accounting.new_invoice", new UIAttributes("New Invoice", ACCOUNTING_TAB, accountingMenu, invoiceSelected));    
    
    int paymentSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.accounting.paymentlist", new UIAttributes("Payments", ACCOUNTING_TAB, accountingMenu, paymentSelected));
    UIATTRIBUTES.put(".view.accounting.view_payment", new UIAttributes("Edit Payment", ACCOUNTING_TAB, accountingMenu, paymentSelected));
    UIATTRIBUTES.put(".view.accounting.new_payment", new UIAttributes("New Payment", ACCOUNTING_TAB, accountingMenu, paymentSelected));    
    
    int expenseSelected[] = {4, 0};
    UIATTRIBUTES.put(".view.accounting.expenselist", new UIAttributes("Expenses", ACCOUNTING_TAB, accountingMenu, expenseSelected));
    UIATTRIBUTES.put(".view.accounting.expenses.new", new UIAttributes("New Expense", ACCOUNTING_TAB, accountingMenu, expenseSelected));    
    UIATTRIBUTES.put(".view.accounting.expenses.edit", new UIAttributes("Edit Expense", ACCOUNTING_TAB, accountingMenu, expenseSelected));    
    int purchaseorderSelected[] = {5, 0};
    UIATTRIBUTES.put(".view.accounting.purchaseorderlist", new UIAttributes("Purchase Orders", ACCOUNTING_TAB, accountingMenu, purchaseorderSelected));
    UIATTRIBUTES.put(".view.accounting.purchaseorder.new", new UIAttributes("New Purchase Order", ACCOUNTING_TAB, accountingMenu, purchaseorderSelected));    
    UIATTRIBUTES.put(".view.accounting.purchaseorder.edit", new UIAttributes("Edit Purchase Order", ACCOUNTING_TAB, accountingMenu, purchaseorderSelected));    
    int itemSelected[] = {6, 0};
    UIATTRIBUTES.put(".view.accounting.itemlist", new UIAttributes("Items", ACCOUNTING_TAB, accountingMenu, itemSelected));
    UIATTRIBUTES.put(".view.accounting.item_detail", new UIAttributes("Item Detail", ACCOUNTING_TAB, accountingMenu, itemSelected));
    int glaccountSelected[] = {7, 0};
    UIATTRIBUTES.put(".view.accounting.glaccountlist", new UIAttributes("Gl Accounts", ACCOUNTING_TAB, accountingMenu, glaccountSelected));
    int inventorySelected[] = {8, 0};
    UIATTRIBUTES.put(".view.accounting.inventorylist", new UIAttributes("Inventory", ACCOUNTING_TAB, accountingMenu, inventorySelected));
    UIATTRIBUTES.put(".view.accounting.inventory_detail", new UIAttributes("Inventory Details", ACCOUNTING_TAB, accountingMenu, inventorySelected));
    
    int vendorSelected[] = {9, 0};
    UIATTRIBUTES.put(".view.accounting.vendorlist", new UIAttributes("Vendors", ACCOUNTING_TAB, accountingMenu, vendorSelected));    

    // Files
    UIATTRIBUTES.put(".view.files.list", new UIAttributes("Files", FILES_TAB, null, null));
    UIATTRIBUTES.put(".view.files.editfile", new UIAttributes("View Files", FILES_TAB, null, null));
    
    // HR
    int expenseFormSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.hr.expenseform.list", new UIAttributes("Expense Forms", HR_TAB, hrMenu, expenseFormSelected));
    UIATTRIBUTES.put(".view.hr.save.expenseform", new UIAttributes("Expense Forms Details", HR_TAB, hrMenu, expenseFormSelected));
    UIATTRIBUTES.put(".view.hr.savenew.expenseform", new UIAttributes("Expense Forms Details", HR_TAB, hrMenu, expenseFormSelected));
    UIATTRIBUTES.put(".view.hr.expenseform.detail", new UIAttributes("Expense Forms Details", HR_TAB, hrMenu, expenseFormSelected));
    
    int timeSheetSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.hr.timesheet.list", new UIAttributes("Time Sheets", HR_TAB, hrMenu, timeSheetSelected));
    UIATTRIBUTES.put(".view.hr.timesheet.detail", new UIAttributes("Time Sheets Detail", HR_TAB, hrMenu, timeSheetSelected));
    UIATTRIBUTES.put(".view.hr.timesheet.edit", new UIAttributes("Edit Time Sheets", HR_TAB, hrMenu, timeSheetSelected));
    UIATTRIBUTES.put(".view.hr.timesheet.new", new UIAttributes("New Time Sheets", HR_TAB, hrMenu, timeSheetSelected));
        
    int employeeHandbookSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.hr.employeehandbook.list", new UIAttributes("Employee Handbook", HR_TAB, hrMenu, employeeHandbookSelected));
    int employeeListSelected[] = {4, 0};
    UIATTRIBUTES.put(".view.hr.employeelist.list", new UIAttributes("Employee List", HR_TAB, hrMenu, employeeListSelected));
    int suggestionBoxSelected[] = {5, 0};
    UIATTRIBUTES.put(".view.hr.suggestionbox", new UIAttributes("Suggestion Box", HR_TAB, hrMenu, suggestionBoxSelected));
    
    // Permission screen
    UIATTRIBUTES.put(".view.permission", new UIAttributes("Record Permission", HOME_TAB, DEFAULT_MENU));

    // Preference
    
    //User Profile
    int userProfileSelected[] = {0, 5};
    UIATTRIBUTES.put(".view.preference.user_profile", new UIAttributes("User Profile", ADDITIONAL_TAB, preferenceMenu, userProfileSelected));

    // Home Settings
    int homeSettingsSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.preference.home_settings", new UIAttributes("Preferences: Home Page Settings", ADDITIONAL_TAB, preferenceMenu, homeSettingsSelected));
    
    // Email Account List
    int mailAccountListSelected[] = {0, 6};
    UIATTRIBUTES.put(".view.preference.mail.account_list", new UIAttributes("Preferences: Email Accounts", ADDITIONAL_TAB, preferenceMenu, mailAccountListSelected));
    UIATTRIBUTES.put(".view.preference.mail.new_account", new UIAttributes("Preferences: New Email Account", ADDITIONAL_TAB, preferenceMenu, mailAccountListSelected));
    UIATTRIBUTES.put(".view.preference.mail.view_account", new UIAttributes("Preferences: Edit Email Account", ADDITIONAL_TAB, preferenceMenu, mailAccountListSelected));

    int mailComposePrefsSelected[] = {0, 7};
    UIATTRIBUTES.put(".view.preference.mail.compose_settings", new UIAttributes("Preferences: Email Composition Settings", ADDITIONAL_TAB, preferenceMenu, mailComposePrefsSelected));

    int mailAutoCheckSelected[] = {0, 8};
    UIATTRIBUTES.put(".view.preference.mail.auto_check_settings", new UIAttributes("Preferences: Email Auto Check Settings", ADDITIONAL_TAB, preferenceMenu, mailAutoCheckSelected));
    
    int mailDelegationSelected[] = {0, 9};
    UIATTRIBUTES.put(".view.preference.mail.delegation_settings", new UIAttributes("Preferences: Email Delegation Settings", ADDITIONAL_TAB, preferenceMenu, mailDelegationSelected));

    int calendarSettingsSelected[] = {0, 10};
    UIATTRIBUTES.put(".view.preference.calendar_settings", new UIAttributes("Preferences: Calendar Settings", ADDITIONAL_TAB, preferenceMenu, calendarSettingsSelected));
    
    int calendarDelegationSelected[] = {0, 11};
    UIATTRIBUTES.put(".view.preference.calendar.delegation_settings", new UIAttributes("Preferences: Calendar Delegation Settings", ADDITIONAL_TAB, preferenceMenu, calendarDelegationSelected));

    int syncSettingsSelected[] = {0, 12};
    UIATTRIBUTES.put(".view.preference.sync_prefs", new UIAttributes("Preferences: Synchronization Settings", ADDITIONAL_TAB, preferenceMenu, syncSettingsSelected));


    // Administration
    // User List
    int userListSelected[] = {0, 1};
    UIATTRIBUTES.put(".view.administration.user_list", new UIAttributes("User List", ADDITIONAL_TAB, adminMenu, userListSelected));
    UIATTRIBUTES.put(".view.administration.user_detail", new UIAttributes("User Detail", ADDITIONAL_TAB, adminMenu, userListSelected));
    UIATTRIBUTES.put(".view.administration.user_detail", new UIAttributes("User List", ADDITIONAL_TAB, adminMenu, userListSelected));
    UIATTRIBUTES.put(".view.administration.default_preferences", new UIAttributes("Administration: Users: Default Privileges", ADDITIONAL_TAB, adminMenu, userListSelected));
    // admin - security profiles
    int securityProfileSelected[] = {0, 2};
    UIATTRIBUTES.put(".view.administration.security_profile_list", new UIAttributes("Security Profiles", ADDITIONAL_TAB, adminMenu, securityProfileSelected));
    UIATTRIBUTES.put(".view.administration.security_profile", new UIAttributes("Administration: Security Profile Details", ADDITIONAL_TAB, adminMenu, securityProfileSelected));
    // admin - configuration
    int serverSettingsSelected[] = {0, 3};
    UIATTRIBUTES.put(".view.administration.server_settings", new UIAttributes("Server Settings", ADDITIONAL_TAB, adminMenu, serverSettingsSelected));
    int emailSettingsSelected[] = {0, 5};
    UIATTRIBUTES.put(".view.administration.email_settings", new UIAttributes("Email Settings", ADDITIONAL_TAB, adminMenu, emailSettingsSelected));
    UIATTRIBUTES.put(".view.administration.view_email_template", new UIAttributes("Email Template Details", ADDITIONAL_TAB, adminMenu, emailSettingsSelected));
    int customerLogoSelected[] = {0, 6};
    UIATTRIBUTES.put(".view.administration.customer_logo", new UIAttributes("Customer Logo Settings", ADDITIONAL_TAB, adminMenu, customerLogoSelected));
    int licenseSettingsSelected[] = {0, 24};
    UIATTRIBUTES.put(".view.administration.license", new UIAttributes("License Settings", ADDITIONAL_TAB, adminMenu, licenseSettingsSelected));

    // admin - data administration
    int garbageSelected[] = {0, 7};
    UIATTRIBUTES.put(".view.administration.garbage_list", new UIAttributes("Administration: Garbage", ADDITIONAL_TAB, adminMenu, garbageSelected));
    int atticSelected[] = {0, 8};
    UIATTRIBUTES.put(".view.administration.attic_list", new UIAttributes("Administration: Attic", ADDITIONAL_TAB, adminMenu, atticSelected));
    int mergeSelected[] = {0, 9};
    UIATTRIBUTES.put(".view.administration.merge_search", new UIAttributes("Administration: Merge and Purge", ADDITIONAL_TAB, adminMenu, mergeSelected));
    UIATTRIBUTES.put(".view.administration.merge_search_results", new UIAttributes("Administration: Merge and Purge", ADDITIONAL_TAB, adminMenu, mergeSelected));
    UIATTRIBUTES.put(".view.administration.entity_merge_detail", new UIAttributes("Administration: Merge and Purge", ADDITIONAL_TAB, adminMenu, mergeSelected));
    UIATTRIBUTES.put(".view.administration.entity_merge_confirm", new UIAttributes("Administration: Merge and Purge", ADDITIONAL_TAB, adminMenu, mergeSelected));
    UIATTRIBUTES.put(".view.administration.merge_complete", new UIAttributes("Administration: Merge and Purge: Complete!", ADDITIONAL_TAB, adminMenu, mergeSelected));
    UIATTRIBUTES.put(".view.administration.individual_merge_detail", new UIAttributes("Administration: Merge and Purge", ADDITIONAL_TAB, adminMenu, mergeSelected));
    UIATTRIBUTES.put(".view.administration.individual_merge_confirm", new UIAttributes("Administration: Merge and Purge", ADDITIONAL_TAB, adminMenu, mergeSelected));
    int globalReplaceSelected[] = {0, 10};
    UIATTRIBUTES.put(".view.administration.global_replace.search", new UIAttributes("Administration: Global Replace", ADDITIONAL_TAB, adminMenu, globalReplaceSelected));
    UIATTRIBUTES.put(".view.administration.global_replace.fields", new UIAttributes("Administration: Global Replace", ADDITIONAL_TAB, adminMenu, globalReplaceSelected));
    UIATTRIBUTES.put(".view.administration.global_replace.confirm", new UIAttributes("Administration: Global Replace", ADDITIONAL_TAB, adminMenu, globalReplaceSelected));


    int historySelected[] = {0, 11};
    UIATTRIBUTES.put(".view.administration.history_list", new UIAttributes("Administration: History", ADDITIONAL_TAB, adminMenu, historySelected));

// TODO: figure out how to make the left nav for module setting dynamic?!??!
    int moduleSettingsSelected[] = {0, 12};
    UIATTRIBUTES.put(".view.administration.module_settings", new UIAttributes("Module Settings", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.default_view", new UIAttributes("Module Settings: Default View Settings", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.edit_custom_view", new UIAttributes("Module Settings: Edit Custom View", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.list_custom_view", new UIAttributes("Module Settings: Custom Views", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.list_custom_fields", new UIAttributes("Module Settings: Custom Fields", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.view_custom_field", new UIAttributes("Module Settings: View Custom Field", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.new_custom_field", new UIAttributes("Module Settings: New Custom Field", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.customview.new", new UIAttributes("Module Settings: New Custom View", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.field_settings", new UIAttributes("Module Settings: General Field Settings", ADDITIONAL_TAB, adminMenu, moduleSettingsSelected));
    int calendarModuleSettingsSelected[] = {1, 13};
    UIATTRIBUTES.put(".view.administration.calendar_settings", new UIAttributes("Module Settings: Calendar Settings", ADDITIONAL_TAB, adminMenu, calendarModuleSettingsSelected));
    int marketingModuleSettingsSelected[] = {0, 18};
    UIATTRIBUTES.put(".view.administration.new_literature", new UIAttributes("Module Settings: New Marketing Literature", ADDITIONAL_TAB, adminMenu, marketingModuleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.literature_list", new UIAttributes("Module Settings: Marketing Literature", ADDITIONAL_TAB, adminMenu, marketingModuleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.view_literature", new UIAttributes("Module Settings: View Marketing Literature", ADDITIONAL_TAB, adminMenu, marketingModuleSettingsSelected));
    int supportModuleSettingsSelected[] = {0, 20};
    UIATTRIBUTES.put(".view.administration.support_inbox", new UIAttributes("Module Settings: Support Inbox", ADDITIONAL_TAB, adminMenu, supportModuleSettingsSelected));
    int accountModuleSettingsSelected[] = {0, 21};
    UIATTRIBUTES.put(".view.administration.tax_settings", new UIAttributes("Module Settings: Accounting: Tax Settings", ADDITIONAL_TAB, adminMenu, accountModuleSettingsSelected));
    int templateModuleSettingsSelected[] = {0, 23};
    UIATTRIBUTES.put(".view.administration.template_list", new UIAttributes("Module Settings: Template List", ADDITIONAL_TAB, adminMenu, templateModuleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.view_template", new UIAttributes("Module Settings: Template Detail", ADDITIONAL_TAB, adminMenu, templateModuleSettingsSelected));
    UIATTRIBUTES.put(".view.administration.new_template", new UIAttributes("Module Settings: New Template", ADDITIONAL_TAB, adminMenu, templateModuleSettingsSelected));

    // Reports
    int contactsStandard[] = {0, 1};
    int contactsStandardIndividual[] = {0, 22};
    int activitiesStandard[] = {0, 4};
    int salesStandard[] = {0, 6};
    int marketingStandard[] = {0, 9};
    int projectStandard[] = {0, 11};
    int supportStandard[] = {0, 15};
    int accountingStandard[] = {0, 17};
    int hrStandard[] = {0, 20};
    UIATTRIBUTES.put(".view.reports.contacts.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, contactsStandard));
    UIATTRIBUTES.put(".view.reports.contacts.standardIndividual", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, contactsStandardIndividual));
    UIATTRIBUTES.put(".view.reports.activities.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, activitiesStandard));
    UIATTRIBUTES.put(".view.reports.sales.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, salesStandard));
    UIATTRIBUTES.put(".view.reports.marketing.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, marketingStandard));
    UIATTRIBUTES.put(".view.reports.project.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, projectStandard));
    UIATTRIBUTES.put(".view.reports.support.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, supportStandard));
    UIATTRIBUTES.put(".view.reports.accounting.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, accountingStandard));
    UIATTRIBUTES.put(".view.reports.hr.standard", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, hrStandard));

    int entitiesAdHoc[] = {0, 2};
    int individualsAdHoc[] = {0, 3};
    int activitiesAdHoc[] = {0, 5};
    int opportunitiesAdHoc[] = {0, 7};
    int proposalsAdHoc[] = {0, 8};
    int promotionAdHoc[] = {0, 10};
    int projectAdHoc[] = {0, 12};
    int taskAdHoc[] = {0, 13};
    int timeslipAdHoc[] = {0, 14};
    int ticketAdHoc[] = {0, 16};
    int orderAdHoc[] = {0, 18};
    int inventoryAdHoc[] = {0, 19};
    int timesheetAdHoc[] = {0, 21};
    UIATTRIBUTES.put(".view.reports.entities.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, entitiesAdHoc));
    UIATTRIBUTES.put(".view.reports.individuals.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, individualsAdHoc));
    UIATTRIBUTES.put(".view.reports.activities.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, activitiesAdHoc));
    UIATTRIBUTES.put(".view.reports.opportunities.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, opportunitiesAdHoc));
    UIATTRIBUTES.put(".view.reports.proposals.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, proposalsAdHoc));
    UIATTRIBUTES.put(".view.reports.promotion.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, promotionAdHoc));
    UIATTRIBUTES.put(".view.reports.project.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, projectAdHoc));
    UIATTRIBUTES.put(".view.reports.task.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, taskAdHoc));
    UIATTRIBUTES.put(".view.reports.timeslip.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, timeslipAdHoc));
    UIATTRIBUTES.put(".view.reports.ticket.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, ticketAdHoc));
    UIATTRIBUTES.put(".view.reports.order.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, orderAdHoc));
    UIATTRIBUTES.put(".view.reports.inventory.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, inventoryAdHoc));
    UIATTRIBUTES.put(".view.reports.timesheet.adhoc", new UIAttributes("Reports", ADDITIONAL_TAB, reportsMenu, timesheetAdHoc));

    // advanced search
    UIATTRIBUTES.put(".view.advancedsearch.contacts", new UIAttributes("Advanced Search", CONTACTS_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.email", new UIAttributes("Advanced Search", EMAIL_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.activities", new UIAttributes("Advanced Search", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.notes", new UIAttributes("Advanced Search", NOTES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.files", new UIAttributes("Advanced Search", FILES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.sales", new UIAttributes("Advanced Search", SALES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.marketing", new UIAttributes("Advanced Search", MARKETING_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.projects", new UIAttributes("Advanced Search", PROJECTS_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.support", new UIAttributes("Advanced Search", SUPPORT_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.accounting", new UIAttributes("Advanced Search", ACCOUNTING_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.advancedsearch.hr", new UIAttributes("Advanced Search", HR_TAB, DEFAULT_MENU));
    // custom views
    UIATTRIBUTES.put(".view.customview.contacts", new UIAttributes("Custom View", CONTACTS_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.email", new UIAttributes("Custom View", EMAIL_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.activities", new UIAttributes("Custom View", ACTIVITIES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.notes", new UIAttributes("Custom View", NOTES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.files", new UIAttributes("Custom View", FILES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.sales", new UIAttributes("Custom View", SALES_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.marketing", new UIAttributes("Custom View", MARKETING_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.projects", new UIAttributes("Custom View", PROJECTS_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.support", new UIAttributes("Custom View", SUPPORT_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.accounting", new UIAttributes("Custom View", ACCOUNTING_TAB, DEFAULT_MENU));
    UIATTRIBUTES.put(".view.customview.hr", new UIAttributes("Custom View", HR_TAB, DEFAULT_MENU));

    // customer view starts here
    LeftNavigation custProfileMenu = new LeftNavigation("Customer Info", new ArrayList());
    LeftNavigation custEmailMenu = new LeftNavigation("Email", new ArrayList());
    
    ArrayList custAccountItems = new ArrayList();
    custAccountItems.add(new NestedMenuItem("Order History", "/customer/order_list.do", 1));
    custAccountItems.add(new NestedMenuItem("Invoice History", "/customer/invoice_list.do", 2));
    custAccountItems.add(new NestedMenuItem("Payments History", "/customer/payment_list.do", 3));
    LeftNavigation custAccountMenu = new LeftNavigation("Accounting", custAccountItems);
    
    ArrayList custSupportItems = new ArrayList();
    custSupportItems.add(new NestedMenuItem("Tickets", "/customer/ticket_list.do", 1));
    custSupportItems.add(new NestedMenuItem("FAQ", "/customer/faq_list.do", 2));
    LeftNavigation custSupportMenu = new LeftNavigation("Support", custSupportItems);

    LeftNavigation custEventMenu = new LeftNavigation("Events", new ArrayList());
    LeftNavigation custUserMenu = new LeftNavigation("Users", new ArrayList());
    LeftNavigation custFileMenu = new LeftNavigation("Files", new ArrayList());
    LeftNavigation custContactMenu = new LeftNavigation("Contact Us", new ArrayList());

    int custDefaultSelected[] = {0, 0};
    UIATTRIBUTES.put(".view.customer.login", new UIAttributes("Login", HOME_TAB, customerLoginMenu, loginSelected));
    UIATTRIBUTES.put(".view.customer.home", new UIAttributes("Home", CUSTOMER_TABS, CUST_HOME_TAB, homeMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.profile", new UIAttributes("Customer Profile", CUSTOMER_TABS, CUST_PROFILE_TAB, custProfileMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.profile_confirm", new UIAttributes("Customer Profile - Change Confirmation", CUSTOMER_TABS, CUST_PROFILE_TAB, custProfileMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.email_list", new UIAttributes("Email List", CUSTOMER_TABS, CUST_EMAIL_TAB, custEmailMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.view_email", new UIAttributes("View Email", CUSTOMER_TABS, CUST_EMAIL_TAB, custEmailMenu, custDefaultSelected));

    int custAccountSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.customer.order_list", new UIAttributes("Order History", CUSTOMER_TABS, CUST_ACCOUNT_TAB, custAccountMenu, custAccountSelected));
    UIATTRIBUTES.put(".view.customer.view_order", new UIAttributes("Order History Details", CUSTOMER_TABS, CUST_ACCOUNT_TAB, custAccountMenu, custAccountSelected));
    int custInvoiceSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.customer.invoice_list", new UIAttributes("Invoice History", CUSTOMER_TABS, CUST_ACCOUNT_TAB, custAccountMenu, custInvoiceSelected));
    int custPaymentSelected[] = {3, 0};
    UIATTRIBUTES.put(".view.customer.payment_list", new UIAttributes("Payments History", CUSTOMER_TABS, CUST_ACCOUNT_TAB, custAccountMenu, custPaymentSelected));
        
    int custTicketSelected[] = {1, 0};
    UIATTRIBUTES.put(".view.customer.ticket_list", new UIAttributes("Tickets List", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custTicketSelected));
    UIATTRIBUTES.put(".view.customer.view_ticket", new UIAttributes("Ticket Details", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custTicketSelected));
    UIATTRIBUTES.put(".view.customer.new_ticket", new UIAttributes("New Ticket", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custTicketSelected));
    UIATTRIBUTES.put(".view.customer.view_thread", new UIAttributes("Thread Details", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custTicketSelected));
    UIATTRIBUTES.put(".view.customer.new_thread", new UIAttributes("New Thread", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custTicketSelected));
    int custFaqSelected[] = {2, 0};
    UIATTRIBUTES.put(".view.customer.faq_list", new UIAttributes("Frequently Asked Questions", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custFaqSelected));
    UIATTRIBUTES.put(".view.customer.view_faq", new UIAttributes("Frequently Asked Question - Details", CUSTOMER_TABS, CUST_SUPPORT_TAB, custSupportMenu, custFaqSelected));

    UIATTRIBUTES.put(".view.customer.event_list", new UIAttributes("Events List", CUSTOMER_TABS, CUST_EVENTS_TAB, custEventMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.view_event", new UIAttributes("Event Details", CUSTOMER_TABS, CUST_EVENTS_TAB, custEventMenu, custDefaultSelected));

    UIATTRIBUTES.put(".view.customer.user_list", new UIAttributes("Users List", CUSTOMER_TABS, CUST_USERS_TAB, custUserMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.view_user", new UIAttributes("User Details", CUSTOMER_TABS, CUST_USERS_TAB, custUserMenu, custDefaultSelected));
    UIATTRIBUTES.put(".view.customer.user_change_confirm", new UIAttributes("User Change Request Submitted", CUSTOMER_TABS, CUST_USERS_TAB, custUserMenu, custDefaultSelected));

    UIATTRIBUTES.put(".view.customer.file_list", new UIAttributes("Files List", CUSTOMER_TABS, CUST_FILES_TAB, custFileMenu, custDefaultSelected));

    UIATTRIBUTES.put(".view.customer.contact_us", new UIAttributes("Contact Us", CUSTOMER_TABS, CUST_CONTACT_TAB, custContactMenu, custDefaultSelected));
  }
}
