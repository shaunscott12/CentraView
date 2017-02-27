/*
 * $RCSfile: ListGenerator.java,v $    $Revision: 1.6 $  $Date: 2005/09/13 22:01:34 $ - $Author: mcallist $
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import com.centraview.account.accountlist.AccountList;
import com.centraview.account.accountlist.AccountListHome;
import com.centraview.account.expense.ExpenseList;
import com.centraview.account.glaccount.GLAccountList;
import com.centraview.account.inventory.InventoryList;
import com.centraview.account.invoice.InvoiceList;
import com.centraview.account.item.ItemList;
import com.centraview.account.order.OrderList;
import com.centraview.account.payment.PaymentList;
import com.centraview.account.purchaseorder.PurchaseOrderList;
import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.SecurityProfileList;
import com.centraview.administration.modulesettings.LiteratureList;
import com.centraview.administration.user.User;
import com.centraview.administration.user.UserHome;
import com.centraview.administration.user.UserList;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.email.EmailList;
import com.centraview.email.RuleList;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.file.CvFile;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileHome;
import com.centraview.file.FileList;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;
import com.centraview.marketing.EventAtendeesList;
import com.centraview.marketing.EventList;
import com.centraview.marketing.MarketingList;
import com.centraview.marketing.MarketingListMemberList;
import com.centraview.marketing.PromotionList;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.note.Note;
import com.centraview.note.NoteHome;
import com.centraview.note.NoteList;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.support.faq.FAQList;
import com.centraview.support.knowledgebase.KnowledgebaseList;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;
import com.centraview.support.ticket.TicketList;

/**
 * ListGenerator is a SingletonRegistry.
 * @author Crappy Programmers at IMI
 * @deletor Kevin McAllister
 */
public class ListGenerator {
  private static Logger logger = Logger.getLogger(ListGenerator.class);
  private static HashMap listGenerators = new HashMap();
  private HashMap globalLists;
  private HashMap displayLists;
  private ListGenerator LG;
  public final static int DEFAULT_RECORDS_PER_PAGE = 100;
  private String dataSource = null;

  /** A Placeholder for the next available list ID. */
  private long globalListID;

  private ListGenerator(String dataSource) {
    globalLists = new HashMap();
    displayLists = new HashMap();
    this.dataSource = dataSource;
  }

  /**
   * This method allows Synchronized access to the globalListID. Using direct
   * access to this field is a bit dangerous. Using this method is much safer.
   * @return The next global List ID.
   */
  public final synchronized long getNextListID()
  {
    // returns the next id in the queue.
    return ++globalListID;
  } // end of getNextListID method

  // **************************Start EMAIL *******************

  public EmailList getEmailList(int userID, int startATparam, int EndAtparam, String searchString,
      String sortColumn, char sortType, int folderid, boolean adminUserFlag)
  {
    EmailList returnDL = null;
    try {
      HashMap hm = new HashMap();
      hm.put("startATparam", new Integer(startATparam));
      hm.put("EndAtparam", new Integer(EndAtparam));
      hm.put("searchString", searchString);
      hm.put("sortmem", sortColumn);
      hm.put("sortType", new Character(sortType));
      hm.put("folderid", new Integer(folderid));
      hm.put("adminUserFlag", new Boolean(adminUserFlag));
      try {
        EmailFacadeHome aa = (EmailFacadeHome)CVUtility.getHomeObject(
            "com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
        EmailFacade remote = (EmailFacade)aa.create();
        remote.setDataSource(this.dataSource);
        returnDL = remote.getEmailList(userID, hm);
      } catch (Exception e) {
        logger.error("[getEmailList] Exception thrown.", e);
      }

      if (searchString != null && searchString.startsWith("SIMPLE :")) {
        returnDL.setSearchString(searchString);
        returnDL.search();
      }

      returnDL.setListType("Email");
      returnDL.setTotalNoOfRecords(returnDL.size());
      long currentListID = this.getNextListID();
      returnDL.setListID(currentListID);
      returnDL.setStartAT(startATparam);
      returnDL.setEndAT(EndAtparam);
      returnDL.setSortMember(sortColumn);
      returnDL.setSortType(sortType);
      returnDL.setPrimaryTable("emailmessage");

      EmailList emptyDL = createEmptyObject(returnDL);
      emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
      emptyDL.setListID(currentListID);
      emptyDL.setListType("Email");
      emptyDL.setStartAT(returnDL.getStartAT());
      emptyDL.setEndAT(returnDL.getEndAT());
      displayLists.put(new Long(currentListID), emptyDL);

    } catch (Exception e) {
      logger.error("[getEmailList] Exception thrown.", e);
    }
    return returnDL;
  }// end of method

  public EmailList createEmptyObject(EmailList list)
  {
    EmailList dummy = new EmailList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  public EmailList getEmailList(int userid, DisplayList DLparam, int folderid, boolean adminUserFlag)
  {

    EmailList returnDL = null;
    EmailList paramDL = (EmailList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));
    hm.put("folderid", new Integer(folderid));
    hm.put("adminUserFlag", new Boolean(adminUserFlag));

    try {
      EmailFacadeHome aa = (EmailFacadeHome)CVUtility.getHomeObject(
          "com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getEmailList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception][ListGenerator] Error in getEmailList(): " + e);
      // e.printStackTrace();
    }

    if (searchString != null && searchString.startsWith("SIMPLE :")) {
      returnDL.setSearchString(searchString);
      returnDL.search();
    }

    returnDL.setListType("Email");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setSortMember(DLparam.getSortMember());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setPrimaryTable("emailmessage");
    returnDL.setSearchString(searchString);
    returnDL.setSortType(DLparam.getSortType());
    return returnDL;
  }

  public EmailList getRelatedEmailList(HashMap searchCondition, int individualID)
      throws CommunicationException, NamingException
  {

    EmailList returnDL = null;
    EmailFacadeHome aa = (EmailFacadeHome)CVUtility.getHomeObject(
        "com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
    try {
      EmailFacade remote = (EmailFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getRelatedEmailList(searchCondition, individualID);
    } catch (Exception e) {
      System.out.println("[Exception][ListGenerator] Error in getEmailList(): " + e);
      // e.printStackTrace();
    }

    returnDL.setListType("Email");
    long currentListID = this.getNextListID();
    returnDL.setListID(currentListID);

    EmailList emptyDL = createEmptyObject(returnDL);
    emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
    emptyDL.setListID(currentListID);
    emptyDL.setListType("Email");
    emptyDL.setStartAT(returnDL.getStartAT());
    emptyDL.setEndAT(returnDL.getEndAT());
    displayLists.put(new Long(currentListID), emptyDL);

    return returnDL;
  }

  // **************************End EMAIL *******************

  public DisplayList getDisplayList(long listid)
  {
    DisplayList DL = (DisplayList)displayLists.get(new Long(listid));
    return DL;
  }

  public void putDisplayList(long listID, DisplayList dl)
  {
    this.displayLists.put(new Long(listID), dl);
  }

  public void makeListDirty(String listType)
  {
    DisplayList displayList = (DisplayList)this.globalLists.get(listType);
    if (displayList != null) {
      displayList.setDirtyFlag(true);
    }
  } // end makeListDirty() method

  /**
   * retruns only one instance of this class
   */
  public synchronized static ListGenerator getListGenerator(String dataSource)
  {
    ListGenerator lg = (ListGenerator)listGenerators.get(dataSource);
    if (lg == null) {
      lg = new ListGenerator(dataSource);
      listGenerators.put(dataSource, lg);
    }
    return (lg);
  }

  public GroupMemberList getGroupMemberList(int userID, int startAt, int EndAt,
      String searchString, String sortColumn, int groupID) throws CommunicationException,
      NamingException
  {
    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    boolean flag = checkListPresentInGlobalList("Individual");

    IndividualList individualList = new IndividualList();

    if (flag == true) {
      individualList = (IndividualList)globalLists.get("Individual");
      if (individualList.getDirtyFlag()) {
        globalLists.remove("Individual");
        flag = false;
      }
    }

    if (flag == false) {
      try {
        HashMap hm = new HashMap();
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);

        individualList = remote.getAllIndividualList(userID, hm);
        individualList.setIndividualId(userID);

        individualList.setSortMember(sortColumn);
        individualList.setListType("Individual");
        individualList.setDirtyFlag(false);

        if (individualList.getTotalNoOfRecords() == individualList.getEndIndex()) {
          globalLists.put("Individual", individualList);
        }
      } catch (Exception e) {
        System.out
            .println("[ListGenerator] Exception thrown in getGroupMemberList() (first try block)): "
                + e);
        // e.printStackTrace();
      }

      flag = true;
    } // end if (flag == false)

    GroupMemberList groupList = new GroupMemberList();
    groupList.setListType("GroupMember");
    groupList.setSortMember(sortColumn);
    groupList.setStartAT(startAt);
    groupList.setEndAT(EndAt);
    groupList.setSearchString(searchString);

    int beginIndex = 0;
    int endIndex = 0;
    int totalRecords = individualList.size();

    // get Group MemberIDs
    Vector IDVector = null;
    try {
      ContactFacade remote = (ContactFacade)aa.create();
      IDVector = remote.getGroupMemberIDs(userID, groupID);
    } catch (Exception e) {
      System.out
          .println("[ListGenerator] Exception thrown in getGroupMemberList() (second try block): "
              + e);
      e.printStackTrace();
    }

    if (IDVector.size() != 0) {
      Set s = individualList.keySet();
      Iterator it = s.iterator();

      while (it.hasNext()) {
        String lem = (String)it.next();
        ListElement ele = (ListElement)individualList.get(lem);
        if (IDVector.contains(new Long(ele.getElementID()))) {
          ListElementMember sm = (ListElementMember)ele.get("Name");
          String IndividualName = (String)sm.getMemberValue();
          groupList.put(IndividualName + ele.getElementID(), ele);
        }
      } // end while ()

      /* **** sort function *** */
      // DL1 = ( GroupMemberList )Sort( DL1 , DL );
      groupList.setTotalNoOfRecords(groupList.size());

      // getFilteredList( DL1.getBeginIndex() , DL1.getEndIndex() , DL1 );
    } // end if (IDVector.size() != 0)

    long currentListID = ++globalListID;
    groupList.setListID(currentListID);
    groupList.setBeginIndex(1);
    groupList.setEndIndex(groupList.getTotalNoOfRecords());
    groupList.setGroupId(groupID);

    GroupMemberList DL2 = this.createEmptyObject(groupList);

    DL2.setListID(currentListID);
    DL2.setListType("GroupMember");
    DL2.setStartAT(startAt);
    DL2.setEndAT(EndAt);
    DL2.setGroupId(groupID);
    DL2.setTotalNoOfRecords(groupList.size());
    displayLists.put(new Long(currentListID), DL2);

    return groupList;
  } // end getGroupMemberList() method

  // Add by shirish 27 Jun

  public GroupMemberList getGroupMemberList(DisplayList DLparam)
  {

    boolean flag = checkListPresentInGlobalList("Individual");
    IndividualList DL = null;
    GroupMemberList DL2 = (GroupMemberList)DLparam;

    if (flag == true) {
      DL = (IndividualList)globalLists.get("Individual");
      // check if its dirty or not
      if (DL.getDirtyFlag()) {
        globalLists.remove("Individual");
        flag = false;
      }
    }

    if (flag == false) {
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        HashMap hm = new HashMap();

        if (DLparam.getSortMember() != null) {
          hm.put("sortColumn", DLparam.getSortMember());
        }
        hm.put("sortDirection", DLparam.getSortType() + "");

        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getAllIndividualList(DLparam.getIndividualId(), hm);
        DL.setListType("Individual");
        DL.setTotalNoOfRecords(DL.size());

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Individual", DL);
        }
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getGroupMemberList: " + e.toString());
        // e.printStackTrace();
      }
      flag = true;
    }

    GroupMemberList DL1 = new GroupMemberList();
    DL1.setGroupId(DL2.getGroupId());
    DL1.setSortMember(DL2.getSortMember());
    DL1.setSortType(DL2.getSortType());
    DL1.setStartAT(DL2.getStartAT());
    DL1.setEndAT(DL2.getEndAT());
    DL1.setSearchString(DL2.getSearchString());
    DL1.setListType("GroupMember");

    // getGroupMemberIDs
    Vector idvector = null;
    try {
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      idvector = remote.getGroupMemberIDs(DLparam.getIndividualId(), DL1.getGroupId());

    } catch (Exception e) {
      System.out.println("[Exception] [ListGenerator.getGroupMemberList]: " + e.toString());
      // e.printStackTrace();
    }

    if (idvector.size() != 0) {
      Set s = DL.keySet();
      Iterator it = s.iterator();

      while (it.hasNext()) {
        String lem = (String)it.next();
        ListElement ele = (ListElement)DL.get(lem);
        if (idvector.contains(new Long(ele.getElementID()))) {
          ListElementMember sm = (ListElementMember)ele.get("Name");
          String IndividualName = (String)sm.getMemberValue();
          DL1.put(IndividualName + ele.getElementID(), ele);
        } else {
          it.remove();
        }
      }
    }

    DL1 = (GroupMemberList)Sort(DL1, DL);
    int beginIndex = 0;
    int endIndex = 0;
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();

    int totalRecords = DL1.size();

    DL1.setBeginIndex(1);
    DL1.setEndIndex(totalRecords);

    DL2.setBeginIndex(0);
    DL2.setEndIndex(0);
    DL1.setTotalNoOfRecords(totalRecords);
    DL1.setListID(DLparam.getListID());
    return DL1;
  }

  /**
   * This method returns CustomFieldList
   */
  public CustomFieldList getCustomFieldList(int userID, int startAT, int EndAt, String sortColumn,
      int contactType, int contactID, String recordType) throws CommunicationException,
      NamingException
  {
    CustomFieldHome aa = (CustomFieldHome)CVUtility.getHomeObject(
        "com.centraview.customfield.CustomFieldHome", "CustomField");

    CustomFieldList DL = null;
    long currentListID = this.getNextListID();
    int tnorec;

    try {
      CustomField remote = (CustomField)aa.create();
      remote.setDataSource(this.dataSource);

      DL = remote.getCustomFieldList(recordType, contactID);

      tnorec = DL.size();

      DL.setListID(currentListID);
      DL.setContactId(contactID);
      DL.setContactType(contactType);
      DL.setSortMember(sortColumn);
      DL.setListType("CustomField");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(tnorec);
      DL.setStartAT(startAT);
      DL.setEndAT(EndAt);
      DL.setBeginIndex(1);
      DL.setEndIndex(tnorec);
      DL.setRecordType(recordType);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getCustomFieldList: " + e.toString());
      // e.printStackTrace();
      return null;
    }

    CustomFieldList emptyAl = new CustomFieldList();

    emptyAl.setListID(currentListID);
    emptyAl.setContactId(contactID);
    emptyAl.setContactType(contactType);
    emptyAl.setListType("CustomField");
    emptyAl.setSortMember(sortColumn);
    emptyAl.setStartAT(startAT);
    emptyAl.setEndAT(EndAt);
    emptyAl.setTotalNoOfRecords(tnorec);
    emptyAl.setTotalNoOfRecords(tnorec);
    emptyAl.setRecordType(recordType);

    displayLists.put(new Long(currentListID), emptyAl);

    return DL;
  } // end getCustomFieldList()

  public CustomFieldList getCustomFieldList(DisplayList DLParam) throws CommunicationException,
      NamingException
  {
    CustomFieldList DL2 = (CustomFieldList)DLParam;
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();
    String sortColumn = DL2.getSortMember();
    char sortType = DL2.getSortType();
    int contactType = DL2.getContactType();
    int contactID = DL2.getContactId();
    long currentListID = DL2.getListID();
    String recordType = DL2.getRecordType();

    CustomFieldHome aa = (CustomFieldHome)CVUtility.getHomeObject(
        "com.centraview.customfield.CustomFieldHome", "CustomField");

    CustomFieldList DL = null;
    CustomFieldList toReturn = new CustomFieldList();
    toReturn.setListID(DL2.getListID());
    toReturn.setContactId(contactID);
    toReturn.setContactType(contactType);
    toReturn.setSortMember(sortColumn);
    toReturn.setSortType(sortType);
    toReturn.setListType("CustomField");
    toReturn.setDirtyFlag(false);
    toReturn.setStartAT(startAT);
    toReturn.setEndAT(EndAt);
    toReturn.setRecordType(recordType);

    int tnorec;
    try {
      CustomField remote = (CustomField)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getCustomFieldList(recordType, contactID);

      tnorec = DL.size();

      DL.setListID(currentListID);
      DL.setContactId(contactID);
      DL.setContactType(contactType);
      DL.setSortMember(sortColumn);
      DL.setListType("CustomField");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(tnorec);

      toReturn.setTotalNoOfRecords(tnorec);

      DL.setStartAT(startAT);
      DL.setEndAT(EndAt);
      DL.setBeginIndex(1);
      DL.setEndIndex(tnorec);
      DL.setRecordType(recordType);

      toReturn = (CustomFieldList)this.Sort(toReturn, DL);

      toReturn.setContactId(contactID);
      toReturn.setContactType(contactType);
      toReturn.setListType("CustomField");
      toReturn.setSortMember(sortColumn);
      toReturn.setRecordType(recordType);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getCustomFieldList: " + e.toString());
      // e.printStackTrace();
      return null;
    }

    DL2.setListID(currentListID);
    DL2.setContactId(contactID);
    DL2.setContactType(contactType);
    DL2.setListType("CustomField");
    DL2.setSortMember(sortColumn);
    DL2.setSortType(sortType);
    toReturn.setSortType(sortType);
    DL2.setRecordType(recordType);

    toReturn = (CustomFieldList)this.Sort(toReturn, DL);

    toReturn.setContactId(contactID);
    toReturn.setContactType(contactType);
    toReturn.setListType("CustomField");
    toReturn.setSortMember(sortColumn);
    toReturn.setBeginIndex(1);
    toReturn.setEndIndex(tnorec);
    toReturn.setRecordType(recordType);

    DL = toReturn;
    DL2.setTotalNoOfRecords(tnorec);
    toReturn = null;

    displayLists.put(new Long(currentListID), DL2);

    return DL;
  }// end of public CustomFieldList getCustomFieldList(DisplayList DLParam )

  // End Add by shirish
  /*
   * contactType contextID = entity or IndividualID
   */
  public AddressList getAddressList(int userID, int startAT, int EndAt, String sortColumn,
      int contactType, int contactID) throws CommunicationException, NamingException
  {
    AddressList DL = new AddressList();
    long currentListID = this.getNextListID();
    int tnorec;
    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    try {

      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllAddresses(contactID, contactType);

      tnorec = DL.size();
      DL.setListID(currentListID);
      DL.setContactId(contactID);
      DL.setContactType(contactType);

      DL.setSortMember(sortColumn);
      DL.setListType("Address");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(tnorec);

      DL.setStartAT(startAT);
      DL.setEndAT(EndAt);
      DL.setBeginIndex(1);
      DL.setEndIndex(tnorec);

    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getAddressList: " + e.toString());
      // e.printStackTrace();
      return null;
    }

    AddressList emptyAl = new AddressList();

    emptyAl.setListID(currentListID);
    emptyAl.setContactId(contactID);
    emptyAl.setContactType(contactType);
    emptyAl.setListType("Address");
    emptyAl.setSortMember(sortColumn);
    emptyAl.setStartAT(startAT);
    emptyAl.setEndAT(EndAt);
    emptyAl.setTotalNoOfRecords(tnorec);

    emptyAl.setTotalNoOfRecords(tnorec);
    displayLists.put(new Long(currentListID), emptyAl);

    return DL;
  }

  // added by Sameer for getting address Liast from the skeleton

  public AddressList getAddressList(DisplayList DLParam) throws CommunicationException,
      NamingException
  {
    /*
     * Parameters required for address list int userID , int startAT , int EndAt
     * ,String sortColumn , int contactType , int contactID
     */
    AddressList DL2 = (AddressList)DLParam;
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();
    String sortColumn = DL2.getSortMember();
    char sortType = DL2.getSortType();
    int contactType = DL2.getContactType();
    int contactID = DL2.getContactId();
    long currentListID = DL2.getListID();

    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

    AddressList DL = null;
    AddressList toReturn = new AddressList();
    toReturn.setListID(DL2.getListID());
    toReturn.setContactId(contactID);
    toReturn.setContactType(contactType);
    toReturn.setSortMember(sortColumn);
    toReturn.setSortType(sortType);
    toReturn.setListType("Address");
    toReturn.setDirtyFlag(false);
    toReturn.setStartAT(startAT);
    toReturn.setEndAT(EndAt);

    // long currentListID = this.getNextListID();
    int tnorec;
    try {
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllAddresses(contactID, contactType);

      tnorec = DL.size();

      DL.setListID(currentListID);
      DL.setContactId(contactID);
      DL.setContactType(contactType);
      DL.setSortMember(sortColumn);
      DL.setListType("Address");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(tnorec);

      toReturn.setTotalNoOfRecords(tnorec);

      DL.setStartAT(startAT);
      DL.setEndAT(EndAt);
      DL.setBeginIndex(1);
      DL.setEndIndex(tnorec);

      toReturn = (AddressList)this.Sort(toReturn, DL);

      toReturn.setContactId(contactID);
      toReturn.setContactType(contactType);
      toReturn.setListType("Address");
      toReturn.setSortMember(sortColumn);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getAddressList: " + e.toString());
      // e.printStackTrace();
      return null;
    }

    // AddressList emptyAl = new AddressList();

    DL2.setListID(currentListID);
    DL2.setContactId(contactID);
    DL2.setContactType(contactType);
    DL2.setListType("Address");
    DL2.setSortMember(sortColumn);
    DL2.setSortType(sortType);
    toReturn.setSortType(sortType);

    toReturn = (AddressList)this.Sort(toReturn, DL);

    toReturn.setContactId(contactID);
    toReturn.setContactType(contactType);
    toReturn.setListType("Address");
    toReturn.setSortMember(sortColumn);
    toReturn.setBeginIndex(1);
    toReturn.setEndIndex(tnorec);

    DL = toReturn;
    DL2.setTotalNoOfRecords(tnorec);
    toReturn = null;

    // IQ added. Khoka
    displayLists.put(new Long(currentListID), DL2);

    // Making Khoka
    // IQ commented. cause this is khoka
    // return toReturn;

    return DL;
  }// end of public AddressList getAddressList(DisplayList DLParam )

  public MOCList getMOCList(int userID, int startAT, int EndAt, String sortColumn, int contactType,
      int contactID) throws CommunicationException, NamingException
  {
    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    MOCList DL = null;
    long currentListID = this.getNextListID();
    int tnorec;
    try {
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllMOC(contactID, contactType);

      tnorec = DL.size();

      DL.setListID(currentListID);
      DL.setContactId(contactID);
      DL.setContactType(contactType);

      DL.setSortMember(sortColumn);
      DL.setListType("MOC");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(tnorec);

      DL.setStartAT(startAT);
      DL.setEndAT(EndAt);
      DL.setBeginIndex(1);
      DL.setEndIndex(tnorec);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getMOCList: " + e.toString());
      // e.printStackTrace();
      return null;
    }

    MOCList emptyAl = new MOCList();
    emptyAl.setListID(currentListID);
    emptyAl.setContactId(contactID);
    emptyAl.setContactType(contactType);
    emptyAl.setListType("MOC");
    emptyAl.setSortMember(sortColumn);
    emptyAl.setStartAT(startAT);
    emptyAl.setEndAT(EndAt);
    emptyAl.setTotalNoOfRecords(tnorec);
    emptyAl.setTotalNoOfRecords(tnorec);
    displayLists.put(new Long(currentListID), emptyAl);

    return DL;
  }

  // added by Sameer for getting MOC List from skeleton
  // MOC list is not part of global lists. so it is received from EJB layer
  // everytime

  public MOCList getMOCList(DisplayList DLParam) throws CommunicationException, NamingException
  {

    MOCList DL2 = (MOCList)DLParam;
    int startAT = DL2.getStartAT();
    int EndAT = DL2.getEndAT();
    String sortColumn = DL2.getSortMember();
    char sortType = DL2.getSortType();
    int contactType = DL2.getContactType();
    int contactID = DL2.getContactId();
    long currentListID = DL2.getListID();

    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    MOCList DL = null;
    MOCList toReturn = new MOCList();
    toReturn.setListID(DL2.getListID());
    toReturn.setContactId(contactID);
    toReturn.setContactType(contactType);
    toReturn.setSortMember(sortColumn);
    toReturn.setSortType(sortType);
    toReturn.setListType("MOC");
    toReturn.setDirtyFlag(false);
    toReturn.setStartAT(startAT);
    toReturn.setEndAT(EndAT);

    int tnorec;
    try {
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllMOC(contactID, contactType);

      tnorec = DL.size();

      DL.setListID(DL2.getListID());
      DL.setContactId(contactID);
      DL.setContactType(contactType);
      DL.setSortMember(sortColumn);
      DL.setListType("MOC");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(tnorec);
      toReturn.setTotalNoOfRecords(tnorec);

      DL.setStartAT(startAT);
      DL.setEndAT(EndAT);
      DL.setBeginIndex(1);
      DL.setEndIndex(tnorec);
      toReturn = (MOCList)this.Sort(toReturn, DL);

      toReturn.setContactId(contactID);
      toReturn.setContactType(contactType);
      toReturn.setListType("MOC");
      toReturn.setSortMember(sortColumn);

    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getMOCList: " + e.toString());
      // e.printStackTrace();
      return null;
    }
    DL2.setListID(currentListID);
    DL2.setContactId(contactID);
    DL2.setContactType(contactType);
    DL2.setListType("MOC");
    DL2.setSortMember(sortColumn);
    DL2.setSortType(sortType);
    toReturn.setSortType(sortType);

    toReturn = (MOCList)this.Sort(toReturn, DL);

    toReturn.setContactId(contactID);
    toReturn.setContactType(contactType);
    toReturn.setListType("MOC");
    toReturn.setSortMember(sortColumn);
    toReturn.setBeginIndex(1);
    toReturn.setEndIndex(tnorec);
    DL = toReturn;
    DL2.setTotalNoOfRecords(tnorec);
    toReturn = null;
    displayLists.put(new Long(currentListID), DL2);

    return DL;
  }// end of public MOCList getMOCList( DisplayList DLParam )

  public IndividualList getIndividualForEntity(int individualId, int startAT, int recordsPerPage,
      int entityId, int dbID) throws ServletException
  {
    IndividualList DL = null;
    HashMap listParameters = new HashMap();
    listParameters.put("dbID", new Integer(dbID));
    // Instead of getting *ALL* the individuals and then looping through
    // them and discarding all but the ones related to the entity I want
    // I think I'll just ask the database to just give me the right ones
    // right away. mmmKay?
    String searchString = "ADVANCE: SELECT individual.individualId FROM individual WHERE individual.entity = "
        + entityId;
    listParameters.put("ADVANCESEARCHSTRING", searchString);
    try {
      ContactFacade contactFacade = (ContactFacade)CVUtility.setupEJB("ContactFacade",
          "com.centraview.contact.contactfacade.ContactFacadeHome", this.dataSource);
      DL = contactFacade.getAllIndividualList(individualId, listParameters);
    } catch (Exception e) {
      logger.error("[getIndividualForEntity] Exception thrown.", e);
      throw new ServletException(e);
    }
    // I wonder, is all the following really necessary?
    DL.setIndividualId(individualId);
    DL.setDirtyFlag(false);
    long currentListId = this.getNextListID();
    DL.setListType("BottomIndividual");
    DL.setSortMember("Name");
    DL.setSortType('A');
    DL.setListID(currentListId);
    DL.setEntityId(entityId);
    DL.setTotalNoOfRecords(DL.size());
    DL.setStartAT(startAT);
    DL.setRecordsPerPage(recordsPerPage);
    DL.setEndAT(DL.size());
    DL.setSearchString(searchString);
    // I guess it can't hurt terribly to throw this list up there. for sorting
    // and what not.
    displayLists.put(new Long(currentListId), DL.clone());
    return DL;
  } // end of Individuals for Entity

  public IndividualList getIndividualForEntity(DisplayList DLParam1, int userID, int EntityID,
      int dbID)
  {
    IndividualList DLParam = (IndividualList)DLParam1;
    // int EntityID = DLParam1.getEntityId();
    // System.out.println("EntityID"+EntityID);
    boolean flag = checkListPresentInGlobalList("Individual");
    IndividualList DL = null;

    // System.out.println("flag"+flag);
    if (flag == true) {
      DL = (IndividualList)globalLists.get("Individual");

      if (DL.getDirtyFlag()) {
        flag = false;
      }
    }

    if (flag == false) {
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        HashMap hm = new HashMap();
        if (DLParam1.getSortMember() != null) {
          hm.put("sortColumn", DLParam1.getSortMember());
        }
        hm.put("sortDirection", DLParam1.getSortType() + "");

        hm.put("dbID", new Integer(dbID));
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getAllIndividualList(userID, hm);
        DL.setSortMember(DLParam.getSortMember());
        DL.setListType("Individual");
        DL.setDirtyFlag(false);

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Individual", DL);
        }
        flag = true;

      } catch (Exception e) {
        // System.out.println("ListGenerator::getIndividualLookup" + e );
      }
    }

    // IndividualList DL1 = ( IndividualList ) globalLists.get( "Individual" );
    IndividualList DL1 = new IndividualList();
    Set s = DL.keySet();
    int i = 0;
    Iterator it = s.iterator();
    while (it.hasNext()) {
      i++;
      String lem = (String)it.next();
      IndividualListElement ele = (IndividualListElement)DL.get(lem);
      ListElementMember sm = (ListElementMember)ele.get("EntityID");
      Integer str = (Integer)sm.getMemberValue();
      int ID = str.intValue();

      if (ID == EntityID) {
        DL1.put(lem, ele);
      } else {
        it.remove();
      }

    }

    DL1.setListType("BottomIndividual");
    DL1.setSortMember(DLParam.getSortMember());
    DL1.setStartAT(DLParam.getStartAT());
    DL1.setEndAT(DLParam.getEndAT());
    DL1.setSearchString(DLParam.getSearchString());

    int beginIndex = 1;
    int endIndex = (DL1.size());

    int tnorec = DL1.size();

    DL1.setListID(DLParam.getListID());
    DL1.setSortType(DLParam.getSortType());
    DL1.setEntityId(DLParam.getEntityId());
    DL1.setSortMember(DLParam.getSortMember());
    DL1.setSortType(DLParam.getSortType());
    DL1.setRecordsPerPage(DLParam.getrecordsPerPage());
    DL1.setTotalNoOfRecords(tnorec);

    DL1 = (IndividualList)Sort(DL1, DL);

    /*
     * DL1= null; DL1= DL2; DL2 = new IndividualList();
     * DL2.setSortType(DLParam.getSortType());
     * DL2.setSortMember(DLParam.getSortMember());
     * DL2.setRecordsPerPage(DLParam.getrecordsPerPage());
     * DL2.setTotalNoOfRecords(tnorec); DL2.setListType( "BottomIndividual" );
     * DL2.setStartAT( DLParam.getStartAT() ); DL2.setEndAT ( DLParam.getEndAT() );
     * //DL2 = (IndividualList) Sort( DL2 , DL1 ); DL1 = DL2;
     */

    // DL2 = null;
    IndividualList DL2 = new IndividualList();
    DL2.setStartAT(DL1.getStartAT());
    DL2.setEndAT(DL1.getEndAT());
    DL2.setSearchString(DL1.getSearchString());
    DL2.setListType("BottomIndividual");
    DL2.setSortType(DL1.getSortType());
    DL2.setEntityId(EntityID);
    DL2.setRecordsPerPage(DL1.getrecordsPerPage());
    DL2.setTotalNoOfRecords(tnorec);
    DL2.setSortMember(DL1.getSortMember());

    displayLists.put(new Long(DLParam.getListID()), DL2);
    return DL1;
  }// end of Individuals for Entity

  /**
   * Called when a method wants a list of just a few entities identified by
   * primary Id in the Vector v. Relies on the ADVANCE search string.
   * @param individualID
   * @param v
   * @param startAt
   * @param endAt
   * @return EntityList with the requested entityIds.
   * @throws CommunicationException
   * @throws NamingException
   */
  public EntityList getEntityList(int individualID, Vector v, int startAt, int endAt)
      throws CommunicationException, NamingException
  {
    // I like arraylists better than vectors... :)
    ArrayList entityIds = new ArrayList();
    entityIds.addAll(v);
    StringBuffer primaryIDs = new StringBuffer();
    boolean flag = true;
    for (int i = 0; i < entityIds.size(); i++) {
      if (flag) {
        flag = false;
      } else {
        primaryIDs.append(", ");
      }
      primaryIDs.append(entityIds.get(i));
    }
    StringBuffer queryString = new StringBuffer();
    queryString.append("ADVANCE: SELECT entity.entityId FROM entity WHERE entity.entityId IN (");
    queryString.append(primaryIDs);
    queryString.append(")");
    EntityList DL = new EntityList();
    DL.setIndividualId(individualID);
    DL.setStartAT(startAt);
    DL.setEndAT(endAt);
    DL.setSearchString(queryString.toString());
    DL.setListType("Entity");
    DL.setSortMember("Name");
    DL.setSortType('A');
    long currentListID = this.getNextListID();
    DL.setListID(currentListID);
    return this.getEntityList(DL);
  }

  /**
   * Returns an IndividualList object (sub-class of DisplayList) for display in
   * the Struts (Control) layer.
   */
  public IndividualList getIndividualList(int individualID, Vector v, int startAt, int endAt)
  {
    boolean flag = this.checkListPresentInGlobalList("Individual");
    IndividualList DL = null;

    if (flag == true) {
      DL = (IndividualList)this.globalLists.get("Individual");
      if (DL.getDirtyFlag()) {
        flag = false;
      }
    }

    if (flag == false) {
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        HashMap hm = new HashMap();
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getAllIndividualList(individualID, hm);

        DL.setIndividualId(individualID);
        DL.setListType("Individual");
        DL.setDirtyFlag(false);
        DL.setTotalNoOfRecords(DL.size());
        DL.setIndividualId(individualID);

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Individual", DL);
        }
      } catch (Exception e) {
        logger.error("[getIndividualList] Exception thrown.", e);
      }
      flag = true;
    }

    IndividualList toReturn = new IndividualList();
    toReturn.setListType("Individual");

    int beginIndex = 0;
    int endIndex = 0;
    int tnorec = DL.size();

    if (tnorec > endAt) {
      beginIndex = startAt - 5;
      if (beginIndex < 1) {
        beginIndex = 1;
      }
      endIndex = endAt + 5;
    } else {
      beginIndex = 1;
      endIndex = endAt;
    }

    toReturn.setStartAT(startAt);
    toReturn.setEndAT(endAt);
    toReturn.setBeginIndex(beginIndex);
    toReturn.setEndIndex(endIndex);
    toReturn.setIndividualId(individualID);

    Set s = DL.keySet();
    Iterator it = s.iterator();
    int i = 0;

    while (it.hasNext()) {
      String lem = (String)it.next();
      ListElement ele = (ListElement)DL.get(lem);

      if (v.contains((new Integer(ele.getElementID())).toString())) {
        toReturn.put(lem, ele);
      }
    }

    toReturn.setTotalNoOfRecords(DL.size());

    long currentListID = this.getNextListID();
    toReturn.setListID(currentListID);

    // here the empty DisplayList will get created
    IndividualList DL2 = createEmptyObject(toReturn);
    DL2.setTotalNoOfRecords(DL.size());
    DL2.setListID(currentListID);
    DL2.setListType("Individual");
    DL2.setStartAT(startAt);
    DL2.setEndAT(endAt);

    DL2.setIndividualId(individualID);

    displayLists.put(new Long(currentListID), DL2);

    toReturn.setListID(currentListID);

    return toReturn;
  } // end getIndividualList() method

  public EntityList getEntityList(int individualID, int startAT, int EndAt, String searchString,
      String sortColumn) throws CommunicationException, NamingException
  {
    EntityList dl = new EntityList();
    dl.setStartAT(startAT);
    dl.setEndAT(EndAt);
    dl.setSortMember(sortColumn);
    dl.setSortType('A');
    dl.setSearchString(searchString);
    dl.setListType("Entity");
    dl.setIndividualId(individualID);
    long currentListID = this.getNextListID();
    dl.setListID(currentListID);
    return this.getEntityList(dl);
  }

  /**
   * This method called by EntityListHandler
   */
  public EntityList getEntityList(int individualID, int startAT, int EndAt, String searchString,
      String sortColumn, int dbID) throws CommunicationException, NamingException
  {
    EntityList dl = new EntityList();
    dl.setStartAT(startAT);
    dl.setEndAT(EndAt);
    dl.setSortMember(sortColumn);
    dl.setSortType('A');
    dl.setSearchString(searchString);
    dl.setListType("Entity");
    dl.setDbID(dbID);
    dl.setIndividualId(individualID);
    long currentListID = this.getNextListID();
    dl.setListID(currentListID);
    return this.getEntityList(dl);
  }

  /**
   * This method will be called for returning Entity List with in the range of
   * StartAT and endAT
   */
  public IndividualList getIndividualList(DisplayList DLparam) throws CommunicationException,
      NamingException
  {
    IndividualList DL = null;
    IndividualList displayListParameters = (IndividualList)DLparam;
    String searchString = DLparam.getSearchString() == null ? "" : DLparam.getSearchString();
    // This will hold the parameters being passed to the EJB
    // primarily just the searchString if any.
    HashMap parameters = new HashMap();
    // Get the EJB stuff ready
    ContactFacade contactFacade = null;
    ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    try {
      contactFacade = (ContactFacade)contactFacadeHome.create();
      contactFacade.setDataSource(this.dataSource);
    } catch (Exception e) {
      logger.error("[getIndividualList] Exception thrown.", e);
    }
    // This will do Simple search
    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {
      try {
        // If it is a simple search I don't send in anything as a parameter. I
        // just want an unaltered list. And I think the marketing list will
        // default to 1 which sucks too.
        DL = contactFacade.getAllIndividualList(DLparam.getIndividualId(), parameters);
      } catch (RemoteException e) {
        logger.error("[getIndividualList] Exception thrown.", e);
      }
      DL.setTotalNoOfRecords(DL.size());
      DL.setListType("Individual");
      DL.setSortMember(displayListParameters.getSortMember());
      DL.setDirtyFlag(false);
      if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
        globalLists.put("Individual", DL.clone());
      }
      IndividualList DL1 = new IndividualList();
      DL.setSortMember(displayListParameters.getSortMember());
      DL.setSortType(displayListParameters.getSortType());
      DL.setSearchString(searchString);
      DL.setListType("Individual");
      DL.setIndividualId(displayListParameters.getIndividualId());
      DL = (IndividualList)this.Sort(DL, DL);
      if (!searchString.equals("")) {
        DL.search();
      }
      DL.setTotalNoOfRecords(DL.size());
      DL.setListID(DLparam.getListID());
      return DL1;
    } else { // for ADVANCE:
      String powerString = DLparam.getPowerString();
      try {
        parameters.put("ADVANCESEARCHSTRING", searchString);
        DL = contactFacade.getAllIndividualList(DLparam.getIndividualId(), parameters);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Individual");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        logger.error("[getIndividualList] Exception thrown.", e);
      }
      DL.setSortMember(displayListParameters.getSortMember());
      DL.setSortType(displayListParameters.getSortType());
      DL.setStartAT(displayListParameters.getStartAT());
      DL.setEndAT(displayListParameters.getEndAT());
      DL.setSearchString(searchString);
      DL.setPowerString(powerString);
      DL.setListType("Individual");
      DL.setIndividualId(displayListParameters.getIndividualId());
      DL = (IndividualList)this.Sort(DL, DL);
      DL.setListID(DLparam.getListID());
      DL.setIndividualId(DL.getIndividualId());
      displayLists.put(new Long(DLparam.getListID()), DL.clone());
      return DL;
    }
  }// end of public IndividualList getIndividualList( DisplayList DLparam )

  /** **************This method is called by IndividualListHandler */

  public IndividualList getIndividualList(DisplayList DLparam, int dbID)
      throws CommunicationException, NamingException
  {
    IndividualList DL = null;
    IndividualList DL2 = (IndividualList)DLparam;
    String searchString = DLparam.getSearchString();

    if (searchString == null) {
      searchString = "";
    }

    // This will do Simple search
    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      try {

        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);

        HashMap hm = new HashMap();

        if (DLparam.getSortMember() != null) {
          hm.put("sortColumn", DLparam.getSortMember());
        }
        hm.put("sortDirection", DLparam.getSortType() + "");

        if (dbID != 0) {
          hm.put("ListID", new Integer(dbID));
        }
        hm.put("dbID", new Integer(dbID));

        DL = remote.getAllIndividualList(DLparam.getIndividualId(), hm);

        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Individual");
        DL.setSortMember(DL2.getSortMember());
        DL.setDirtyFlag(false);
        DL.setDbID(dbID);

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Individual", DL);
        }
      } catch (Exception e) {
        System.out
            .println("[Exception][ListGenerator] Exception thrown in getIndividualList(DisplayList, int):2304 :"
                + e);
        e.printStackTrace();
      }

      DL.setSortType(DL2.getSortType());
      DL.setStartAT(DL2.getStartAT());
      DL.setEndAT(DL2.getEndAT());
      DL.setDbID(DL2.getDbID());
      DL.setSearchString(searchString);
      DL.setListType("Individual");

      int beginIndex = 0;
      int endIndex = 0;
      int startAT = DL2.getStartAT();
      int EndAt = DL2.getEndAT();
      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100) {
          beginIndex = startAT - 100;
        }
        if (beginIndex < 1) {
          beginIndex = 1;

        }
        endIndex = EndAt + 100;
      } else {
        beginIndex = 1;
        endIndex = EndAt;
      }

      DL.setBeginIndex(beginIndex);
      DL.setEndIndex(endIndex);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);

      DL.setSearchString(searchString);
      if (!searchString.equals("")) {
        DL.search("Name");
      }

      DL.setTotalNoOfRecords(DL.size());
      DL.setListID(DLparam.getListID());

      return DL;
    }// end of if SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        HashMap hm = new HashMap();

        if (DLparam.getSortMember() != null) {
          hm.put("sortColumn", DLparam.getSortMember());
        }
        hm.put("sortDirection", DLparam.getSortType() + "");

        hm.put("ADVANCESEARCHSTRING", searchString);
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getAllIndividualList(DLparam.getIndividualId(), hm);

        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Individual");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getIndividualListt" + e);
      }

      IndividualList DL1 = new IndividualList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);
      DL1.setDbID(DL2.getDbID());
      DL1.setListType("Individual");

      int beginIndex = 1;
      int endIndex = DL.size();
      int startAT = DL2.getStartAT();
      int EndAt = DL2.getEndAT();
      int tnorec = DL.size();
      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      Set s = DL.keySet();
      Iterator it = s.iterator();
      int i = 0;
      while (it.hasNext()) {
        String lem = (String)it.next();
        ListElement ele = (ListElement)DL.get(lem);

        IntMember im = (IntMember)ele.get("DbID");
        Integer value = (Integer)im.getMemberValue();
        int dbid = value.intValue();

        if (dbid == dbID) {
          DL1.put(lem, ele);
        } else {
          it.remove();
        }
      }

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);
      DL1.setDbID(DL2.getDbID());

      DL1 = (IndividualList)Sort(DL1, DL);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL1);
      return DL1;

    }
  }// end of public IndividualList getIndividualList( DisplayList DLparam )

  /**
   * This method will be called for returning Entity List with in the range of
   * StartAT and endAT
   */
  public GroupList getGroupList(DisplayList DLparam) throws CommunicationException, NamingException
  {
    boolean flag = checkListPresentInGlobalList("Group");
    GroupList DL = null;
    GroupList DL2 = (GroupList)DLparam;
    String searchString = DLparam.getSearchString();
    if (searchString == null)
      searchString = "";

    // This will do Simple search
    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {
      if (flag == true) {
        DL = (GroupList)globalLists.get("Group");
        // check if its dirty or not
        if (DL.getDirtyFlag()) {
          globalLists.remove("Group");
          flag = false;
        }
      }

      if (flag == false) {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        try {
          HashMap hm = new HashMap();
          ContactFacade remote = (ContactFacade)aa.create();
          remote.setDataSource(this.dataSource);
          DL = remote.getAllGroupList(DLparam.getIndividualId(), hm);
          DL.setListType("Group");
          DL.setTotalNoOfRecords(DL.size());

          if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {

            globalLists.put("Group", DL);
          }
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getGroupList: " + e.toString());
        }
        flag = true;
      }

      // end synchronized

      GroupList DL1 = new GroupList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setListType("Group");

      // write the logic for begin index, endindex for this specific list type
      // here

      int beginIndex = 0;
      int endIndex = 0;
      int startAT = DL2.getStartAT();
      int EndAt = DL2.getEndAT();

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);

      // search String

      // check for serach string simple or in multiple tables
      // and if the global list is complete
      // if simple search in same GL and make DL1
      // sort

      DL1 = (GroupList)Sort(DL1, DL);

      DL1.setSearchString(searchString);
      if (!searchString.equals(""))
        DL1.search();

      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setListID(DLparam.getListID());

      return DL1;
    }// end of else SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        HashMap hm = new HashMap();
        hm.put("ADVANCESEARCHSTRING", searchString);
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getAllGroupList(DLparam.getIndividualId(), hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Group");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getGroupList: " + e.toString());
      }

      GroupList DL1 = new GroupList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL1.setListType("Group");

      int beginIndex = 1;
      int endIndex = DL.size();
      int startAT = DL2.getStartAT();
      int EndAt = DL2.getEndAT();

      int tnorec = DL.size();

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      DL1 = (GroupList)this.Sort(DL1, DL);

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }
  }

  /**
   * This method will be called for returning Entity List with in the range of
   * StartAT and endAT
   */
  public EntityList getEntityList(DisplayList DLparam) throws CommunicationException,
      NamingException
  {
    EntityList DL = new EntityList();
    EntityList DL2 = (EntityList)DLparam;
    int marketingListId = DL2.getDbID();
    String searchString = DL2.getSearchString();
    if (searchString == null) {
      searchString = "";
    }
    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    ContactFacade remote = null;
    try {
      remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
    } catch (Exception e) {
      logger.error("[getEntityList] Exception thrown.", e);
    }
    HashMap hm = new HashMap();
    if (marketingListId > 0) {
      hm.put("ListID", new Integer(marketingListId));
    }
    if (searchString.startsWith("ADVANCE:")) {
      hm.put("ADVANCESEARCHSTRING", searchString);
    }
    try {
      DL = remote.getAllEntityList(DL2.getIndividualId(), hm);
    } catch (RemoteException e) {
      logger.error("[getEntityList] Exception thrown.", e);
    }
    DL.setDbID(marketingListId);
    DL.setTotalNoOfRecords(DL.size());
    DL.setDirtyFlag(false);
    DL.setSortMember(DL2.getSortMember());
    DL.setSortType(DL2.getSortType());
    DL.setStartAT(DL2.getStartAT());
    DL.setEndAT(DL2.getEndAT());
    DL.setSearchString(searchString);
    DL.setListType("Entity");
    DL.setSearchString(searchString);
    DL.setPowerString(DL2.getPowerString());
    if (searchString.equals("SIMPLE :")) {
      DL.search();
    }
    DL.setListID(DL2.getListID());
    DL.setTotalNoOfRecords(DL.size()); // size may have changed with search.
    DL.setBeginIndex(1);
    DL.setEndIndex(DL.size());
    this.Sort(DL, DL);
    displayLists.put(new Long(DL.getListID()), DL.clone());
    return DL;
  } // end of getEntityList( DisplayList DLparam )

  /**
   * This method is called by EntityList Handler
   */
  public EntityList getEntityList(DisplayList DLparam, int dbID) throws CommunicationException,
      NamingException
  {
    ((EntityList)DLparam).setDbID(dbID);
    return this.getEntityList(DLparam);
  } // end of getEntityList( DisplayList DLparam )

  /**
   * This function will add complete list to global List
   */
  public void addToGlobalLists(String str, DisplayList el)
  {
    globalListID++;
    globalLists.put(str, el);
  }

  /**
   * This function will add displayList list to displayLists HashMap
   */
  private void addToDisplayLists()
  {}

  /**
   * check in global List that List exists in it or not
   */
  public boolean checkListPresentInGlobalList(String str)
  {
    return globalLists.containsKey(str);
  }

  /**
   * This funtion will create new the Entity list for BegainIndex to EndIndex.
   */
  public DisplayList getFilteredList(int beginIndex, int endIndex, DisplayList DL)
  {
    Set c = DL.keySet();
    Iterator it = c.iterator();
    int i = 0;
    while (it.hasNext()) {
      i++;
      Object ob = it.next();
      if (i >= beginIndex && i <= endIndex) {

      } else {
        DL.remove(ob);
      }
    }
    return DL;
  }

  /**
   * This funtion will sort any DisplayList by a specific column either
   * ascending or descending.
   */
  public DisplayList Sort(DisplayList targetList, DisplayList sourceList)
  {
    char sortType = targetList.getSortType();
    TreeMap intermediate = null;
    if (sortType == 'D') {
      Comparator reverse = Collections.reverseOrder();
      intermediate = new TreeMap(reverse);
    } else {
      intermediate = new TreeMap();
    }
    String sortMember = (String)targetList.getSortMember();
    Set c = sourceList.keySet();
    Iterator it = c.iterator();
    while (it.hasNext()) {
      String key = (String)it.next();
      ListElement element = (ListElement)sourceList.get(key);
      int id = element.getElementID();
      ListElementMember elementMember = (ListElementMember)element.get(sortMember);
      String keyPart = (String)elementMember.getSortString();
      if (keyPart == null || keyPart.equals("null")) {
        keyPart = "";
      }
      String newkey = keyPart + id;
      intermediate.put(newkey, element);
    }
    targetList.clear();
    // if descending we need to recreate keys that match the targetList
    // comparator. Otherwise the Keys made above will work for ascending
    if (sortType == 'D') {
      Collection sortedElements = intermediate.values();
      Iterator iterator = sortedElements.iterator();
      long i = 0;
      while (iterator.hasNext()) {
        Object Element = iterator.next();
        String key = CVUtility.leftZeroPad(i++, 10);
        targetList.put(key, Element);
      }
    } else {
      targetList.putAll(intermediate);
    }
    return targetList;
  } // end of sort

  public EntityList createEmptyObject(EntityList itsEntityList)
  {

    EntityList dummy = new EntityList();
    dummy.setListType(itsEntityList.getListType());
    dummy.setPrimaryMemberType(itsEntityList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsEntityList.getPrimaryTable());
    dummy.setSortMember(itsEntityList.getSortMember());
    dummy.setSortType(itsEntityList.getSortType());
    dummy.setPrimaryMember(itsEntityList.getPrimaryMember());
    dummy.setStartAT(itsEntityList.getStartAT());
    dummy.setEndAT(itsEntityList.getEndAT());
    dummy.setDbID(itsEntityList.getDbID());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  public GroupList createEmptyObject(GroupList itsEntityList)
  {

    GroupList dummy = new GroupList();
    dummy.setListType(itsEntityList.getListType());
    dummy.setPrimaryMemberType(itsEntityList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsEntityList.getPrimaryTable());
    dummy.setSortMember(itsEntityList.getSortMember());
    dummy.setSortType(itsEntityList.getSortType());
    dummy.setPrimaryMember(itsEntityList.getPrimaryMember());
    dummy.setStartAT(itsEntityList.getStartAT());
    dummy.setEndAT(itsEntityList.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    /*
     * getCompleteFlag() getDirtyFlag() getSearchString() getTotNoOfRecords()
     * getRecordsInMemory()
     */
    return dummy;

  }

  public IndividualList createEmptyObject(IndividualList itsEntityList)
  {

    IndividualList dummy = new IndividualList();
    dummy.setListType(itsEntityList.getListType());
    dummy.setPrimaryMemberType(itsEntityList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsEntityList.getPrimaryTable());
    dummy.setSortMember(itsEntityList.getSortMember());
    dummy.setSortType(itsEntityList.getSortType());
    dummy.setPrimaryMember(itsEntityList.getPrimaryMember());
    dummy.setStartAT(itsEntityList.getStartAT());
    dummy.setEndAT(itsEntityList.getEndAT());
    dummy.setDbID(itsEntityList.getDbID());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    /*
     * getCompleteFlag() getDirtyFlag() getSearchString() getTotNoOfRecords()
     * getRecordsInMemory()
     */
    return dummy;

  }

  public GroupMemberList createEmptyObject(GroupMemberList itsEntityList)
  {
    GroupMemberList dummy = new GroupMemberList();
    dummy.setListType(itsEntityList.getListType());
    dummy.setPrimaryMemberType(itsEntityList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsEntityList.getPrimaryTable());
    dummy.setSortMember(itsEntityList.getSortMember());
    dummy.setSortType(itsEntityList.getSortType());
    dummy.setPrimaryMember(itsEntityList.getPrimaryMember());
    dummy.setStartAT(itsEntityList.getStartAT());
    dummy.setEndAT(itsEntityList.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  /**
   * return GroupList from Display List or from EJB Layer got request check that
   * list is present in DisplayLists if ( present ) { fill that list with data
   * and return ; }else { call EJB layer to getDisplayList and return ; add it
   * to displayList if complete add it to global List }
   */
  public GroupList getGroupList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      GroupList dl = new GroupList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Group");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getGroupList(dl);

    } else {

      boolean flag = checkListPresentInGlobalList("Group");
      GroupList DL = null;
      if (flag == true) {
        DL = (GroupList)globalLists.get("Group");
        if (DL.getDirtyFlag()) {
          globalLists.remove("Group");
          flag = false;
        }

      }

      if (flag == false) {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        try {
          HashMap hm = new HashMap();
          ContactFacade remote = (ContactFacade)aa.create();
          remote.setDataSource(this.dataSource);
          DL = remote.getAllGroupList(userID, hm);
          DL.setIndividualId(userID);
          DL.setSortMember(sortColumn);
          DL.setListType("Group");
          DL.setDirtyFlag(false);
          if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
            globalLists.put("Group", DL);
          }

        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }
        flag = true;
      }

      GroupList DL1 = new GroupList();
      DL1.setListType("Group");
      DL1.setSortMember(sortColumn);
      DL1.setStartAT(startAT);
      DL1.setEndAT(EndAt);
      DL1.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();

      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;

      } else {
        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      /** *** sort function *** */
      DL1 = (GroupList)Sort(DL1, DL);
      DL1.setTotalNoOfRecords(DL.size());

      // getFilteredList( DL1.getBeginIndex() , DL1.getEndIndex() , DL1 );

      long currentListID = this.getNextListID();
      DL1.setListID(currentListID);

      GroupList DL2 = createEmptyObject(DL1);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setListID(currentListID);
      DL2.setSortMember(sortColumn);
      DL2.setListType("Group");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);

      Set s = DL.keySet();
      Iterator it = s.iterator();
      int i = 0;

      // IQ adde to filter only those records which the user has right to see
      Vector allAccRec = new Vector();
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);
        allAccRec = remote.getGroupAccessRecords(userID);
      } catch (Exception e) {}

      while (it.hasNext()) {
        String lem = (String)it.next();
        ListElement ele = (ListElement)DL.get(lem);

        IntMember gid = (IntMember)ele.get("GroupID");
        Integer lstGrpid = (Integer)gid.getMemberValue();

        if (allAccRec.contains(lstGrpid)) {

          DL1.remove(lem);
        }
      }

      return DL1;
    }
  }

  public IndividualList getIndividualList(int individualId, int startAT, int EndAt,
      String searchString, String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      IndividualList dl = new IndividualList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Individual");
      dl.setIndividualId(individualId);
      long currentListID = this.getNextListID();
      dl.setListID(currentListID);
      displayLists.put(new Long(currentListID), dl);
      return this.getIndividualList(dl);
    } else {
      IndividualList DL = null;
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      try {
        HashMap hm = new HashMap();
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);
        hm.put("sortDirection", "A");
        DL = remote.getAllIndividualList(individualId, hm);
        DL.setIndividualId(individualId);
        DL.setSortMember(sortColumn);
        DL.setListType("Individual");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        logger.error("[getIndividualList] Exception thrown.", e);
      }
      IndividualList DL1 = new IndividualList();
      DL1.setListType("Individual");
      DL1.setSortMember(sortColumn);
      DL1.setStartAT(startAT);
      DL1.setEndAT(EndAt);
      DL1.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();
      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1) {
          beginIndex = 1;
        }
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;
      }
      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);
      DL1 = (IndividualList)Sort(DL1, DL);
      DL1.setTotalNoOfRecords(DL.size());
      long currentListID = this.getNextListID();
      DL1.setListID(currentListID);
      IndividualList DL2 = createEmptyObject(DL1);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setListID(currentListID);
      DL2.setSortMember(sortColumn);
      DL2.setListType("Individual");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);
      return DL1;
    }
  }

  public IndividualList getIndividualList(int individualID, int startAT, int EndAt,
      String searchString, String sortColumn, int dbID) throws CommunicationException,
      NamingException
  {

    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      IndividualList dl = new IndividualList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Individual");
      dl.setDbID(dbID);
      // added by pravink
      dl.setIndividualId(individualID);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getIndividualList(dl);

    } else {
      IndividualList DL = null;

      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      try {
        HashMap hm = new HashMap();
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);
        hm.put("dbID", new Integer(dbID));
        DL = remote.getAllIndividualList(individualID, hm);
        DL.setIndividualId(individualID);
        DL.setSortMember(sortColumn);
        DL.setListType("Individual");
        DL.setDirtyFlag(false);
        DL.setDbID(dbID);
        // added by pravink
        DL.setIndividualId(individualID);

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Individual", DL);
        }

      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getIndividualList: " + e.toString());
      }

      DL.setSortMember(sortColumn);
      DL.setStartAT(startAT);
      DL.setEndAT(EndAt);
      DL.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();

      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;
      }

      DL.setBeginIndex(beginIndex);
      DL.setEndIndex(endIndex);
      DL.setTotalNoOfRecords(DL.size());
      DL.setDbID(dbID);
      long currentListID = this.getNextListID();
      DL.setListID(currentListID);

      IndividualList DL2 = createEmptyObject(DL);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setListID(currentListID);
      DL2.setSortMember(sortColumn);
      DL2.setListType("Individual");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      DL2.setDbID(dbID);
      DL2.setIndividualId(individualID);

      displayLists.put(new Long(currentListID), DL2);

      return DL;
    }
  }

  /**
   * This will return the Entity lookup list to request
   */
  public EntityList getEntityLookup(int individualID)
  {
    EntityList DL = null;
    try {
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      HashMap hm = new HashMap();
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllEntityList(individualID, hm);
      DL.setIndividualId(individualID);
      DL.setListType("Entity");

      if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
        globalLists.put("Entity", DL);
      }

    } catch (Exception e) {
      System.out.println("ListGenerator::getEntityLookup" + e);
    }

    DL = (EntityList)globalLists.get("Entity");
    return DL;
  }

  /**
   * This will return the Individual lookup list to request
   */
  public IndividualList getIndividualLookup(int userID, int EntityID, int dbID)
  {
    IndividualList DL = null;
    try {
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      HashMap hm = new HashMap();
      hm.put("dbID", new Integer(dbID));
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllIndividualList(userID, hm);
      DL.setIndividualId(userID);
      // DL.setSortMember( sortColumn );
      DL.setListType("Individual");
      DL.setDirtyFlag(false);

      if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
        globalLists.put("Individual", DL);
      }

    } catch (Exception e) {
      System.out.println("ListGenerator::getIndividualLookup" + e);
    }

    IndividualList DL1 = (IndividualList)globalLists.get("Individual");
    IndividualList DL2 = new IndividualList();
    Set s = DL1.keySet();
    int i = 0;
    Iterator it = s.iterator();
    while (it.hasNext()) {
      i++;
      String lem = (String)it.next();
      IndividualListElement ele = (IndividualListElement)DL1.get(lem);
      ListElementMember sm = (ListElementMember)ele.get("EntityID");
      Integer str = (Integer)sm.getMemberValue();
      int ID = str.intValue();
      if ((ID == EntityID) || (EntityID == 0))
        DL2.put(lem, ele);
    }
    return DL2;
  }

  /**
   * This will return the Group lookup list to request
   */
  public GroupList getGroupLookup(int userID)
  {

    boolean flag = checkListPresentInGlobalList("Group");
    GroupList DL = null;

    if (flag == true) {
      DL = (GroupList)globalLists.get("Group");
      if (DL.getDirtyFlag()) {
        flag = false;
      }

    }

    if (flag == false) {
      try {
        ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        HashMap hm = new HashMap();
        ContactFacade remote = (ContactFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getAllGroupList(userID, hm);
        DL.setIndividualId(userID);
        DL.setListType("Group");
        DL.setDirtyFlag(false);

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Group", DL);
        }
        flag = true;
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getGroupLookup: " + e.toString());
      }
    }
    GroupList DL1 = (GroupList)globalLists.get("Group");
    return DL1;
  }

  // start of File List

  /** ****************** FileList ************************** */

  /**
   * This method will be called first time a File list is requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @param fileTypeRequest
   * @param folderID
   * @return FileList
   */
  public FileList getEntityFileList(int entityId, int userId, int startATparam, int EndAtparam,
      String searchString, String sortColumn, String fileTypeRequest, int folderID,
      boolean SystemIncludeFlag)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {

      FileList dl = new FileList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("File");
      dl.setSystemIncludeFlag(SystemIncludeFlag);
      dl.setCurrentFolderID(folderID);
      dl.setFileTypeRequest(fileTypeRequest);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getEntityFileList(entityId, userId, folderID, dl);
      // return dl;

    } else {
      FileList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("fileTypeRequest", fileTypeRequest);
        hm.put("curFolderID", new Integer(folderID));

        hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));

        HashMap listMap = null;

        try {
          CvFileHome aa = (CvFileHome)CVUtility.getHomeObject("com.centraview.file.CvFileHome",
              "CvFile");
          CvFile remote = (CvFile)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getEntityFiles(entityId, userId, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getEntityFileList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setFileTypeRequest(fileTypeRequest);
        returnDL.setSystemIncludeFlag(SystemIncludeFlag);
        returnDL.setListType("File");
        returnDL.setTotalNoOfRecords(returnDL.size());
        returnDL.setCurrentFolderID(folderID);

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        FileList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("File");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setFileTypeRequest(fileTypeRequest);
        emptyDL.setSystemIncludeFlag(SystemIncludeFlag);
        emptyDL.setCurrentFolderID(folderID);

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getEntityFileList

  public FileList getFileList(int userID, int startATparam, int EndAtparam, String searchString,
      String sortColumn, String fileTypeRequest, int folderID, boolean SystemIncludeFlag)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      FileList dl = new FileList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("File");
      dl.setSystemIncludeFlag(SystemIncludeFlag);
      dl.setCurrentFolderID(folderID);
      dl.setFileTypeRequest(fileTypeRequest);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getFileList(userID, folderID, dl);

    } else {
      FileList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("fileTypeRequest", fileTypeRequest);
        hm.put("curFolderID", new Integer(folderID));

        hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));

        HashMap listMap = null;

        CvFileFacade cvFFacacde = new CvFileFacade();

        try {
          returnDL = cvFFacacde.getAllFiles(userID, hm, this.dataSource);
        } catch (Exception e) {
          System.out.println("[Exception][ListGenerator.getFileList] Exception Thrown: " + e);
        }

        returnDL.setFileTypeRequest(fileTypeRequest);
        returnDL.setSystemIncludeFlag(SystemIncludeFlag);
        returnDL.setListType("File");
        returnDL.setTotalNoOfRecords(returnDL.size());
        returnDL.setCurrentFolderID(folderID);

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        FileList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("File");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setFileTypeRequest(fileTypeRequest);
        emptyDL.setSystemIncludeFlag(SystemIncludeFlag);
        emptyDL.setCurrentFolderID(folderID);

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        System.out.println("[Exception][ListGenerator.getFileList] Exception Thrown: " + e);
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getFileList

  /**
   * This method is called with empty List
   * @param userid
   * @param folderID
   * @param DLparam
   * @return
   */
  public FileList getFileList(int userid, int folderID, DisplayList DLparam)
  {

    FileList returnDL = null;
    FileList paramDL = (FileList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();
    String fileTypeRequest = paramDL.getFileTypeRequest();

    boolean SystemIncludeFlag = paramDL.getSystemIncludeFlag();

    int curFolderID = paramDL.getCurrentFolderID();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    hm.put("fileTypeRequest", fileTypeRequest); // all my
    hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));
    hm.put("curFolderID", new Integer(curFolderID));

    try {
      CvFileFacade cvFFacacde = new CvFileFacade();
      returnDL = cvFFacacde.getAllFiles(userid, hm, this.dataSource);
    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setFileTypeRequest(fileTypeRequest);
    returnDL.setListType("File");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    returnDL.setSystemIncludeFlag(SystemIncludeFlag);
    returnDL.setCurrentFolderID(curFolderID);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * This method is called with empty List
   * @param userid
   * @param folderID
   * @param DLparam
   * @return
   */
  public FileList getEntityFileList(int entityId, int userId, int folderID, DisplayList DLparam)
  {

    FileList returnDL = null;
    FileList paramDL = (FileList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();
    String fileTypeRequest = paramDL.getFileTypeRequest();

    boolean SystemIncludeFlag = paramDL.getSystemIncludeFlag();

    int curFolderID = paramDL.getCurrentFolderID();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    hm.put("fileTypeRequest", fileTypeRequest); // all my
    hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));
    hm.put("curFolderID", new Integer(curFolderID));

    try {
      CvFileHome aa = (CvFileHome)CVUtility.getHomeObject("com.centraview.file.CvFileHome",
          "CvFile");
      CvFile remote = (CvFile)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getEntityFiles(entityId, userId, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getEntityFileList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setFileTypeRequest(fileTypeRequest);
    returnDL.setListType("File");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    returnDL.setSystemIncludeFlag(SystemIncludeFlag);
    returnDL.setCurrentFolderID(curFolderID);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // end of getAppointmentList( DisplayList DLparam )
  public ArrayList getCompanyNews()
  {
    ArrayList returnCN = null;
    try {
      CvFileHome aa = (CvFileHome)CVUtility.getHomeObject("com.centraview.file.CvFileHome",
          "CvFile");
      CvFile remote = (CvFile)aa.create();
      remote.setDataSource(this.dataSource);
      returnCN = remote.getCompanyNews();
    } catch (Exception e) {
      logger.error("[getCompanyNews] Exception thrown.", e);
    }
    return returnCN;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public FileList createEmptyObject(FileList list)
  {
    FileList dummy = new FileList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    dummy.setSystemIncludeFlag(list.getSystemIncludeFlag());
    dummy.setFileTypeRequest(list.getFileTypeRequest());
    dummy.setCurrentFolderID(list.getCurrentFolderID());

    return dummy;
  }// end of createEmptyObject

  public RuleList getRuleList(int userID, int accountID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, char sortType)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      RuleList dl = new RuleList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType(sortType);
      dl.setSearchString(searchString);
      dl.setListType("Rule");
      dl.setSortMember(sortColumn);
      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getRuleList(userID, accountID, dl);
    } else {
      RuleList returnDL = null;

      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character(sortType));
        HashMap listMap = null;

        try {
          MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
          Mail remote = (Mail)home.create();
          remote.setDataSource(this.dataSource);

          returnDL = (RuleList)remote.getRuleList(userID, accountID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getRuleList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Rule");
        returnDL.setTotalNoOfRecords(returnDL.size());
        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setSortType(sortType);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);
        returnDL.setPrimaryTable("emailrule");
        returnDL.setSortMember(sortColumn);

        if (searchString != null && searchString.startsWith("SIMPLE :")) {
          returnDL.setSearchString(searchString);
          returnDL.search();
        }

        RuleList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Rule");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        e.printStackTrace();
      }
      return returnDL;
    }
  } // end getRuleList(int,int,int,int,String,String)

  public RuleList getRuleList(int userid, int accountID, DisplayList DLparam)
  {
    RuleList returnDL = null;
    RuleList paramDL = (RuleList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(this.dataSource);
      returnDL = (RuleList)remote.getRuleList(userid, accountID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getRuleList: " + e.toString());
      // e.printStackTrace();
    }
    // long currentListID = this.getNextListID();
    // returnDL.setListID(currentListID);
    returnDL.setListType("Rule");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSortMember(paramDL.getSortMember());

    if (searchString != null && searchString.startsWith("SIMPLE :")) {
      returnDL.setSearchString(searchString);
      returnDL.search();
    }
    returnDL.setSearchString(searchString);
    returnDL.setPrimaryTable("emailrule");

    return returnDL;
  }

  // end of getRuleList( DisplayList DLparam )

  /**
   * this method creates empty Object
   */
  public RuleList createEmptyObject(RuleList list)
  {
    RuleList dummy = new RuleList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  // end of createEmptyObject

  /**
   * This method is called when request for Note List is made for the first time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @param noteType
   * @return
   */
  public NoteList getNoteList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, String noteType)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      NoteList dl = new NoteList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('D');
      dl.setSearchString(searchString);
      dl.setListType("Note");
      dl.setNoteType(noteType);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getNoteList(individualID, dl);

    } else {
      NoteList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('D'));
        hm.put("notetype", noteType);

        try {
          NoteHome aa = (NoteHome)CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
          Note remote = (Note)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getNoteList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getNoteList: " + e.toString());
          // e.printStackTrace();
        }
        returnDL.setNoteType(noteType);
        returnDL.setListType("Note");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);
        returnDL.setNoteType(noteType);

        NoteList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Note");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setNoteType(noteType);
        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * This function is used for the Entity Detail bottom listing of notes related
   * to the entity
   * @param entityId The current entity id value
   * @param startATparam Start row to display
   * @param EndAtparam Ending row to display
   * @param searchString String value containing any extra search criteria
   * @param sortColumn String value for the column name to sort by
   * @param noteType Note type
   */
  public NoteList getEntityNoteList(int entityId, int startATparam, int EndAtparam,
      String searchString, String sortColumn, String noteType)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      NoteList dl = new NoteList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Note");
      dl.setNoteType(noteType);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);
      return this.getEntityNoteList(entityId, dl);
    } else {
      NoteList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("notetype", noteType);

        try {
          NoteHome aa = (NoteHome)CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
          Note remote = (Note)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getEntityNoteList(entityId, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getEntityNoteList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setNoteType(noteType);
        returnDL.setListType("Note");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);
        returnDL.setNoteType(noteType);

        NoteList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Note");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setNoteType(noteType);
        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    } // end if ((searchString != null) && ((searchString.trim()).length() > 0
      // ))
  } // end getEntityNoteList() method

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public NoteList createEmptyObject(NoteList list)
  {
    NoteList dummy = new NoteList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    dummy.setNoteType(list.getNoteType());
    return dummy;
  }// end of createEmptyObject

  /**
   * This method is called when list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public NoteList getNoteList(int individualID, DisplayList DLparam)
  {

    NoteList returnDL = null;
    NoteList paramDL = (NoteList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();
    String noteType = paramDL.getNoteType();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));
    hm.put("notetype", noteType);

    try {
      NoteHome aa = (NoteHome)CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
      Note remote = (Note)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getNoteList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getNoteList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Note");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setNoteType(noteType);
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * This function is used for the Entity Detail bottom listing of notes related
   * to the entity
   * @param entityId The current entity id value
   * @param DLparam Display list paramters
   */
  public NoteList getEntityNoteList(int entityId, DisplayList DLparam)
  {
    NoteList returnDL = null;
    NoteList paramDL = (NoteList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();
    String noteType = paramDL.getNoteType();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));
    hm.put("notetype", noteType);

    try {
      NoteHome aa = (NoteHome)CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
      Note remote = (Note)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getEntityNoteList(entityId, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getEntityNoteList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Note");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setNoteType(noteType);
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :")) {
      returnDL.search();
    }

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /** ****************** EventsList ************************** */
  /**
   * This method will be called first time
   */

  public EventList getEventList(int userID, int startATparam, int EndAtparam, String searchString,
      String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      EventList dl = new EventList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Event");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getEventList(userID, dl);

    } else {
      EventList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        HashMap listMap = null;

        try {
          MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
              "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
          MarketingFacade remote = (MarketingFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getEventList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getEventList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Event");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        EventList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Event");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getEventList

  /**
   * This method is called with empty List
   */

  public EventList getEventList(int userid, DisplayList DLparam)
  {

    EventList returnDL = null;
    EventList paramDL = (EventList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    try {
      // HashMap listMap = new HashMap();
      MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getEventList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getEventList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Event");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    return returnDL;
  }

  // end of getEventList

  /**
   * this method creates empty Object
   */
  public EventList createEmptyObject(EventList list)
  {
    EventList dummy = new EventList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  // end of createEmptyObject
  // --------- End of EventList-------------

  /** ****************** PromotionsList ************************** */
  /**
   * This method will be called first time
   */

  public PromotionList getPromotionList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      PromotionList dl = new PromotionList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Promotion");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getPromotionList(userID, dl);

    } else {
      PromotionList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        HashMap listMap = null;
        MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
            "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
        try {
          MarketingFacade remote = (MarketingFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getPromotionList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getPromotionList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Promotion");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        PromotionList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Promotion");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getPromotionList

  /**
   * This method is called with empty List
   */

  public PromotionList getPromotionList(int userid, DisplayList DLparam)
      throws CommunicationException, NamingException
  {

    PromotionList returnDL = null;
    PromotionList paramDL = (PromotionList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
        "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
    try {
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getPromotionList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getPromotionList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Promotion");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    return returnDL;
  }

  // end of getPromotionList

  /**
   * this method creates empty Object
   */
  public PromotionList createEmptyObject(PromotionList list)
  {
    PromotionList dummy = new PromotionList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  public MarketingListMemberList getMarketingListMemberList(int individualID, int marketingListID,
      int startATparam, int EndAtparam, String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      MarketingListMemberList dl = new MarketingListMemberList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("MarketingListMembers");
      dl.setIndividualId(individualID);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getMarketingListMemberList(individualID, marketingListID, dl);
    } else {
      MarketingListMemberList returnDL = null;

      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        HashMap listMap = null;

        try {
          MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
              "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
          MarketingFacade remote = (MarketingFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getMarketingListMemberList(individualID, marketingListID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getMarketingListMemberList: "
              + e.toString());
          e.printStackTrace();
        }

        returnDL.setListType("MarketingListMembers");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);
        returnDL.setIndividualId(individualID);

        MarketingListMemberList emptyDL = MarketingListMemberList.createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("MarketingListMembers");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setIndividualId(individualID);

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getMarketingListMemberList: " + e.toString());
        // e.printStackTrace();
      }
      return returnDL;
    } // end if ((searchString != null) && ((searchString.trim()).length() > 0
      // ))
  } // end getMarketingListMemberList() method

  // argument userID changed to individualID - pravink
  public MarketingListMemberList getMarketingListMemberList(int individualID, int marketingListID,
      DisplayList DLparam)
  {
    MarketingListMemberList returnDL = null;
    MarketingListMemberList paramDL = (MarketingListMemberList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    // hm.put( "eventId" , new Integer( eventId ));

    try {
      MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getMarketingListMemberList(individualID, marketingListID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getMarketingListMemberList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("MarketingListMembers");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    // added by pravink
    returnDL.setIndividualId(individualID);

    return returnDL;
  } // end of getMarketingListMemberList method

  /** ****************** EventAtendeessList ************************** */
  /**
   * This method will be called first time
   */

  public EventAtendeesList getEventAtendeesList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, int eventId)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      EventAtendeesList dl = new EventAtendeesList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("EventAtendees");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getEventAtendeesList(userID, dl, eventId);

    } else {
      EventAtendeesList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("eventId", new Integer(eventId));
        HashMap listMap = null;

        try {
          MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
              "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
          MarketingFacade remote = (MarketingFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getEventAtendeesList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getEventAtendeesList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("EventAtendees");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        EventAtendeesList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("EventAtendees");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getEventAtendeesList

  /**
   * This method is called with empty List
   */

  public EventAtendeesList getEventAtendeesList(int userid, DisplayList DLparam, int eventId)
  {

    EventAtendeesList returnDL = null;
    EventAtendeesList paramDL = (EventAtendeesList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    hm.put("eventId", new Integer(eventId));

    try {
      // HashMap listMap = new HashMap();
      MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getEventAtendeesList(userid, hm);

    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getEventAtendeesList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("EventAtendees");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    return returnDL;
  }

  // end of getEventAtendeesList

  /**
   * this method creates empty Object
   */
  public EventAtendeesList createEmptyObject(EventAtendeesList list)
  {
    EventAtendeesList dummy = new EventAtendeesList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  // end of createEmptyObject
  // --------- End of EventAtendeesList-------------

  // ------------------marketing module ends---------------------------------

  public EntityList getHomeEntityList(int UserID, int startat1, int endat, String searchstring,
      String sortElement, char sortType)
  {
    EntityList el = new EntityList();
    el.setStartAT(startat1);
    el.setEndAT(endat);
    el.setSearchString(searchstring);
    el.setSortMember(sortElement);
    el.setSortType(sortType);
    el.setRecordsPerPage(endat);
    el.setDirtyFlag(true);
    el.setListType("Entity");
    long currentListID = this.getNextListID();
    el.setListID(currentListID);

    el.setBeginIndex(0);
    el.setEndIndex(0);
    el.setTotalNoOfRecords(100);

    displayLists.put(new Long(currentListID), el);

    return el;
  }// end of getHomeEntityList

  /**
   * This method will be called first time a Ticket list is requested.
   * @param userID user who is requesting the list
   * @param startATparam the starting index
   * @param EndAtparam the ending index
   * @param searchString any search criteria for limiting the list
   * @param sortColumn the name of the column to sort the list by
   * @return TicketList object containing the list of ticket records
   */
  public TicketList getTicketList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      TicketList dl = new TicketList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Ticket");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getTicketList(userID, dl);

    } else {
      TicketList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        HashMap listMap = null;

        SupportFacadeHome aa = (SupportFacadeHome)CVUtility.getHomeObject(
            "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
        try {

          SupportFacade remote = (SupportFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getTicketList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getTicketList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Ticket");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        TicketList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Ticket");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getTicketList

  /**
   * This method is called with empty List
   */
  public TicketList getTicketList(int userid, DisplayList DLparam) throws CommunicationException,
      NamingException
  {
    TicketList returnDL = null;
    TicketList paramDL = (TicketList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    SupportFacadeHome aa = (SupportFacadeHome)CVUtility.getHomeObject(
        "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
    try {
      SupportFacade remote = (SupportFacade)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getTicketList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getTicketList: " + e.toString());
      // e.printStackTrace();
    }
    returnDL.setListType("Ticket");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :")) {
      returnDL.search();
    }
    returnDL.setTotalNoOfRecords(returnDL.size());
    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return TicketList
   */
  public TicketList createEmptyObject(TicketList list)
  {
    TicketList dummy = new TicketList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }// end of createEmptyObject

  // end of Ticket List

  // *********

  // start of FAQ List

  /** ****************** FAQ List ************************** */

  /**
   * This method will be called first time a FAQ list is requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return FAQList
   */
  public FAQList getFAQList(int userID, int startATparam, int EndAtparam, String searchString,
      String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      FAQList dl = new FAQList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("FAQ");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getFAQList(userID, dl);

    } else {
      FAQList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        HashMap listMap = null;

        SupportFacadeHome aa = (SupportFacadeHome)CVUtility.getHomeObject(
            "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
        try {
          SupportFacade remote = (SupportFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getFAQList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getFAQList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("FAQ");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        FAQList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("FAQ");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getFAQList

  /**
   * This method is called with empty List
   * @param userid
   * @param DLparam
   * @return FAQList
   */
  public FAQList getFAQList(int userid, DisplayList DLparam) throws CommunicationException,
      NamingException
  {

    FAQList returnDL = null;
    FAQList paramDL = (FAQList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    SupportFacadeHome aa = (SupportFacadeHome)CVUtility.getHomeObject(
        "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
    try {

      SupportFacade remote = (SupportFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getFAQList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getFAQList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("FAQ");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // end of getFAQList( DisplayList DLparam )

  /**
   * this method creates empty Object
   * @param list
   * @return FAQList
   */
  public FAQList createEmptyObject(FAQList list)
  {
    FAQList dummy = new FAQList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }// end of createEmptyObject

  // end of FAQ List

  // start of knowledge base*********

  /** ****************** KnowledgeBaseList ************************** */

  /**
   * This method will be called first time a Knowledgebase list is requested
   * @param userID The individualID of the user requesting the list.
   * @param startATparam The starting index of the list to be requested (for
   *          paging)
   * @param EndAtparam The ending index of the list to be requested (for paging)
   * @param searchString An optional advanced search string for limiting results
   *          (SQL)
   * @param sortColumn The name of the column to sort results by
   * @param categoryID The category ID for which we want results
   * @param customerViewFlag The customerViewFlag If true then calling from
   *          Customer View List handler
   * @return KnowledgebaseList Object
   */
  public KnowledgebaseList getKnowledgebaseList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, int categoryID, boolean customerViewFlag)
      throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      KnowledgebaseList dl = new KnowledgebaseList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Knowledgebase");
      dl.setCurrentCategoryID(categoryID);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return (this.getKnowledgebaseList(userID, categoryID, dl));
    } else {
      KnowledgebaseList returnDL = null;

      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("curCategoryID", new Integer(categoryID));
        hm.put("customerViewFlag", new Boolean(customerViewFlag));

        HashMap listMap = null;
        SupportFacadeHome aa = (SupportFacadeHome)CVUtility.getHomeObject(
            "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
        try {
          SupportFacade remote = (SupportFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getKnowledgebaseList(userID, categoryID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getKnowledgebaseList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Knowledgebase");
        returnDL.setTotalNoOfRecords(returnDL.size());
        returnDL.setCurrentCategoryID(categoryID);

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        KnowledgebaseList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Knowledgebase");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setCurrentCategoryID(categoryID);

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getKnowledgebaseList: " + e.toString());
        // e.printStackTrace();
      }
      return (returnDL);
    }
  } // end getKnowledgeBaseList() method

  /**
   * This method is called with empty List
   * @param userid
   * @param categoryID
   * @param DLparam
   * @return
   */
  public KnowledgebaseList getKnowledgebaseList(int userid, int categoryID, DisplayList DLparam)
      throws CommunicationException, NamingException
  {

    KnowledgebaseList returnDL = null;
    KnowledgebaseList paramDL = (KnowledgebaseList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    int curCategoryID = paramDL.getCurrentCategoryID();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    hm.put("curCategoryID", new Integer(curCategoryID));
    hm.put("customerViewFlag", new Boolean(paramDL.getCustomerViewFlag()));
    SupportFacadeHome aa = (SupportFacadeHome)CVUtility.getHomeObject(
        "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
    try {
      SupportFacade remote = (SupportFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getKnowledgebaseList(userid, categoryID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getKnowledgebaseList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Knowledgebase");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);
    returnDL.setCurrentCategoryID(curCategoryID);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public KnowledgebaseList createEmptyObject(KnowledgebaseList list)
  {
    KnowledgebaseList dummy = new KnowledgebaseList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    dummy.setCurrentCategoryID(list.getCurrentCategoryID());

    return dummy;
  }// end of createEmptyObject

  // End of Knowledage Base List

  /*
   * ======================= start of Item
   * list===================================
   */
  /**
   * This method is called when request for ItemList is made for the first time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @param noteType
   * @return
   */
  public ItemList getItemList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      ItemList dl = new ItemList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Item");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getItemList(individualID, dl);

    } else {
      ItemList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        try {
          AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
              "com.centraview.account.accountlist.AccountListHome", "AccountList");
          AccountList remote = (AccountList)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getItemList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getItemList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Item");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        ItemList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Item");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public ItemList createEmptyObject(ItemList list)
  {
    ItemList dummy = new ItemList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }// end of createEmptyObject

  /**
   * This method is called when list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public ItemList getItemList(int individualID, DisplayList DLparam)
  {

    ItemList returnDL = null;
    ItemList paramDL = (ItemList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));

    try {
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getItemList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getItemList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Item");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /* ======================= end of Itemlist=================================== */

  /*
   * ======================= start of order
   * list===================================
   */
  /**
   * This method is called when request for OrderList is made for the first time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public OrderList getOrderList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      OrderList dl = new OrderList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Order");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getOrderList(individualID, dl);

    } else {
      OrderList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        try {
          AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
              "com.centraview.account.accountlist.AccountListHome", "AccountList");
          AccountList remote = (AccountList)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getOrderList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getOrderList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Order");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        OrderList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Order");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public OrderList createEmptyObject(OrderList list)
  {
    OrderList dummy = new OrderList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }// end of createEmptyObject

  /**
   * This method is called when list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public OrderList getOrderList(int individualID, DisplayList DLparam)
  {

    OrderList returnDL = null;
    OrderList paramDL = (OrderList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));

    try {
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getOrderList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getOrderList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Order");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /*
   * ======================= end of order
   * list===================================
   */

  /**
   * This method is called when request for GLAccount List is made for the first
   * time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public GLAccountList getGLAccountList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      GLAccountList dl = new GLAccountList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("GLAccount");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getGLAccountList(individualID, dl);

    } else {
      GLAccountList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        try {
          AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
              "com.centraview.account.accountlist.AccountListHome", "AccountList");
          AccountList remote = (AccountList)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getGLAccountList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getGLAccountList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("GLAccount");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        GLAccountList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("GLAccount");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * This method is called when GLAccount list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public GLAccountList getGLAccountList(int individualID, DisplayList DLparam)
  {

    GLAccountList returnDL = new GLAccountList();
    GLAccountList paramDL = (GLAccountList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));

    try {
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getGLAccountList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getGLAccountList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("GLAccount");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public GLAccountList createEmptyObject(GLAccountList list)
  {
    GLAccountList dummy = new GLAccountList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }

  // end of createEmptyObject

  /**
   * This method is called when request for Inventory List is made for the first
   * time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public InventoryList getInventoryList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      InventoryList dl = new InventoryList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Inventory");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getInventoryList(individualID, dl);

    } else {
      InventoryList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        try {
          AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
              "com.centraview.account.accountlist.AccountListHome", "AccountList");
          AccountList remote = (AccountList)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getInventoryList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getInventoryList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Inventory");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        InventoryList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Inventory");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * This method is called when Inventory list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public InventoryList getInventoryList(int individualID, DisplayList DLparam)
  {

    InventoryList returnDL = null;
    InventoryList paramDL = (InventoryList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));

    try {
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getInventoryList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getInventoryList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("Inventory");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public InventoryList createEmptyObject(InventoryList list)
  {
    InventoryList dummy = new InventoryList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }// end of createEmptyObject

  // added by shirish

  /**
   * Returns list of Marketing Lists.
   */
  public MarketingList getMarketingList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      MarketingList dl = new MarketingList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Marketing");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return (this.getMarketingList(userID, dl));
    } else {
      MarketingList returnDL = null;

      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        HashMap listMap = null;

        try {
          MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
              "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
          MarketingFacade remote = (MarketingFacade)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getMarketingList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getMarketingList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("Marketing");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        MarketingList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Marketing");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getMarketingList: " + e.toString());
        // e.printStackTrace();
      }
      return returnDL;
    } // end if ((searchString != null) && ((searchString.trim()).length() > 0))
  } // end getMarketingList() method

  /**
   * This method is called with empty List
   */

  public MarketingList getMarketingList(int userid, DisplayList DLparam)
  {

    MarketingList returnDL = null;
    MarketingList paramDL = (MarketingList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    try {
      MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getMarketingList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getMarketingList: " + e.toString());
      // e.printStackTrace();
    }

    if (searchString != null && searchString.startsWith("SIMPLE :")) {
      returnDL.setSearchString(searchString);
      returnDL.search();
    }

    returnDL.setListType("Marketing");
    returnDL.setTotalNoOfRecords(returnDL.size());
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    return returnDL;
  }

  // end of getRuleList( DisplayList DLparam )

  /**
   * this method creates empty Object
   */
  public MarketingList createEmptyObject(MarketingList list)
  {
    MarketingList dummy = new MarketingList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  // end of createEmptyObject

  // end added by shirish

  /**
   * This method is called when request for Invoice List is made for the first
   * time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public InvoiceList getInvoiceList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, long entityID, long orderID)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      InvoiceList dl = new InvoiceList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("InvoiceHistory");
      dl.setEntityID(entityID);
      dl.setOrderID(orderID);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getInvoiceList(individualID, dl);

    } else {
      InvoiceList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("EntityID", new Long(entityID));
        hm.put("OrderID", new Long(orderID));

        try {
          AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
              "com.centraview.account.accountlist.AccountListHome", "AccountList");
          AccountList remote = (AccountList)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getInvoiceList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception] ListGenerator.getInvoiceList: " + e.toString());
          // e.printStackTrace();
        }

        returnDL.setListType("InvoiceHistory");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);
        returnDL.setEntityID(entityID);
        returnDL.setOrderID(orderID);

        InvoiceList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("InvoiceHistory");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setEntityID(returnDL.getEntityID());
        emptyDL.setOrderID(returnDL.getOrderID());

        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * This method is called when Invoice list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public InvoiceList getInvoiceList(int individualID, DisplayList DLparam)
  {

    InvoiceList returnDL = null;
    InvoiceList paramDL = (InvoiceList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    long entityID = paramDL.getEntityID();
    long orderID = paramDL.getOrderID();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));
    hm.put("EntityID", new Long(entityID));
    hm.put("OrderID", new Long(orderID));

    try {
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getInvoiceList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getInvoiceList: " + e.toString());
      // e.printStackTrace();
    }

    returnDL.setListType("InvoiceHistory");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);
    returnDL.setEntityID(entityID);
    returnDL.setOrderID(orderID);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public InvoiceList createEmptyObject(InvoiceList list)
  {
    InvoiceList dummy = new InvoiceList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    dummy.setEntityID(list.getEntityID());
    dummy.setOrderID(list.getOrderID());

    return dummy;
  }// end of createEmptyObject


  public ProjectList createEmptyObject(ProjectList itsProjectList)
  {

    ProjectList dummy = new ProjectList();
    dummy.setListType(itsProjectList.getListType());
    dummy.setPrimaryMemberType(itsProjectList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsProjectList.getPrimaryTable());
    dummy.setSortMember(itsProjectList.getSortMember());
    dummy.setSortType(itsProjectList.getSortType());
    dummy.setPrimaryMember(itsProjectList.getPrimaryMember());
    dummy.setStartAT(itsProjectList.getStartAT());
    dummy.setEndAT(itsProjectList.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  /**
   * return project List from Display List or from EJB Layer got request check
   * that list is present in DisplayLists if ( present ) { fill that list with
   * data and return ; }else { call EJB layer to getDisplayList and return ; add
   * it to displayList if complete add it to global List }
   */
  public ProjectList getProjectList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn)
  {
    return getProjectList(userID, startAT, EndAt, searchString, sortColumn, -1);
  }

  /**
   * cwang added entityID filter. When filter=-1, ProjectList is not filtered
   * i.e.,you get the whole list, otherwise you only get projectlist with the
   * given entityid
   * @param DLparam
   * @return
   */
  public ProjectList getProjectList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn, int entityID)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      ProjectList dl = new ProjectList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Project");
      dl.setEntityID(entityID);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getProjectList(userID, dl);

    } else {
      ProjectList DL = null;

      try {
        ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        ProjectFacade remote = (ProjectFacade)aa.create();
        remote.setDataSource(this.dataSource);
        HashMap hm = new HashMap();

        hm.put("startATparam", new Integer(startAT));
        hm.put("EndAtparam", new Integer(EndAt));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("entityID", new Integer(entityID));// cwang

        DL = remote.getProjectList(userID, hm);
        DL.setSortMember(sortColumn);
        DL.setListType("Project");
        DL.setDirtyFlag(false);
        DL.setTotalNoOfRecords(DL.size());
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getProjectList: " + e.toString());
        // e.printStackTrace();
      }
      // end synchronized
      ProjectList toReturn = new ProjectList();
      toReturn.setListType("Project");
      toReturn.setSortMember(sortColumn);
      toReturn.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();
      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;

      }

      toReturn.setStartAT(startAT);
      toReturn.setEndAT(EndAt);
      toReturn.setBeginIndex(beginIndex);
      toReturn.setEndIndex(endIndex);

      toReturn = (ProjectList)this.Sort(toReturn, DL);
      toReturn.setTotalNoOfRecords(DL.size());

      // getFilteredList( toReturn.getBeginIndex() , toReturn.getEndIndex() ,
      // toReturn );

      long currentListID = this.getNextListID();
      toReturn.setListID(currentListID);

      // here the empty DisplayList will get created
      ProjectList DL2 = createEmptyObject(toReturn);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setSortMember(sortColumn);
      DL2.setListID(currentListID);
      DL2.setListType("Project");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);

      toReturn.setListID(currentListID);

      return toReturn;
    }
  }

  /**
   * This method will be called for returning Project List with in the range of
   * StartAT and endAT
   */
  public ProjectList getProjectList(int userID, DisplayList DLparam)
  {
    ProjectList DL = null;
    ProjectList DL2 = (ProjectList)DLparam;
    int listSize = DL2.getTotalNoOfRecords();
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();
    int entityID = DL2.getEntityID();// cwang

    HashMap hm = new HashMap();
    String searchString = DL2.getSearchString();
    if (searchString == null)
      searchString = "";
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", DL2.getSortMember());
    hm.put("sortType", new Character(DL2.getSortType()));
    hm.put("entityID", new Integer(DL2.getEntityID()));// cxw

    /*
     * String searchString = DLparam.getSearchString(); if(searchString == null)
     * searchString = "";
     */
    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {
      try {
        ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        ProjectFacade remote = (ProjectFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getProjectList(userID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Project");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getProjectList: " + e.toString());
        // e.printStackTrace();
      }
      ProjectList DL1 = new ProjectList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setListType("Project");

      int beginIndex = 0;
      int endIndex = 0;

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // search String

      // ****** sort
      DL1 = (ProjectList)this.Sort(DL1, DL);

      DL1.setSearchString(searchString);
      if (!searchString.equals(""))
        DL1.search();

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of "" or SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        hm.put("ADVANCESEARCHSTRING", searchString);
        ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        ProjectFacade remote = (ProjectFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getProjectList(userID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Project");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("[Exception] ListGenerator.getProjectList: " + e.toString());
        // e.printStackTrace();
      }

      ProjectList DL1 = new ProjectList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);
      DL1.setListType("Project");

      int beginIndex = 1;
      int endIndex = DL.size();
      /*
       * int startAT = DL2.getStartAT(); int EndAt = DL2.getEndAT();
       */

      int tnorec = DL.size();

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      DL1 = (ProjectList)this.Sort(DL1, DL);

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of ADVANCE:
  }// end of getProjectList( DisplayList DLparam )

  public TimeSlipList createEmptyObject(TimeSlipList itsTimeSlipList)
  {

    TimeSlipList dummy = new TimeSlipList();
    dummy.setListType(itsTimeSlipList.getListType());
    dummy.setPrimaryMemberType(itsTimeSlipList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsTimeSlipList.getPrimaryTable());
    dummy.setSortMember(itsTimeSlipList.getSortMember());
    dummy.setSortType(itsTimeSlipList.getSortType());
    dummy.setPrimaryMember(itsTimeSlipList.getPrimaryMember());
    dummy.setStartAT(itsTimeSlipList.getStartAT());
    dummy.setEndAT(itsTimeSlipList.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  /**
   * return timeSlip List from Display List or from EJB Layer got request check
   * that list is present in DisplayLists if ( present ) { fill that list with
   * data and return ; }else { call EJB layer to getDisplayList and return ; add
   * it to displayList if complete add it to global List }
   */
  public TimeSlipList getTimeSlipList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      TimeSlipList dl = new TimeSlipList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Timeslip");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getTimeSlipList(userID, dl);

    } else {

      TimeSlipList DL = null;

      try {
        ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        HashMap hm = new HashMap();

        hm.put("startATparam", new Integer(startAT));
        hm.put("EndAtparam", new Integer(EndAt));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        ProjectFacade remote = (ProjectFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getAllTimeSlipList(userID, hm);
        DL.setSortMember(sortColumn);
        DL.setListType("Timeslip");
        DL.setDirtyFlag(false);
        // DL.setTotalNoOfRecords( DL.size() );

      } catch (Exception e) {
        System.out.println("ListGenerator::getTimeSlipList" + e);
      }

      // end synchronized
      TimeSlipList toReturn = new TimeSlipList();
      toReturn.setListType("Timeslip");
      toReturn.setSortMember(sortColumn);
      toReturn.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();
      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;

      }

      toReturn.setStartAT(startAT);
      toReturn.setEndAT(EndAt);
      toReturn.setBeginIndex(beginIndex);
      toReturn.setEndIndex(endIndex);

      // getting exception...need to uncomment again
      toReturn = (TimeSlipList)this.Sort(toReturn, DL);
      // toReturn = DL;//added
      toReturn.setTotalNoOfRecords(DL.size());

      // getFilteredList( toReturn.getBeginIndex() , toReturn.getEndIndex() ,
      // toReturn );

      long currentListID = this.getNextListID();
      toReturn.setListID(currentListID);

      // here the empty DisplayList will get created
      TimeSlipList DL2 = createEmptyObject(toReturn);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setSortMember(sortColumn);
      DL2.setListID(currentListID);
      DL2.setListType("Timeslip");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);

      toReturn.setListID(currentListID);

      return toReturn;
    }
  }

  /**
   * This method will be called for returning TimeSlip List with in the range of
   * StartAT and endAT
   */
  public TimeSlipList getTimeSlipList(int userID, DisplayList DLparam)
  {

    TimeSlipList DL = null;
    TimeSlipList DL2 = (TimeSlipList)DLparam;
    int listSize = DL2.getTotalNoOfRecords();
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();
    HashMap hm = new HashMap();
    String searchString = DL2.getSearchString();
    if (searchString == null)
      searchString = "";
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", DL2.getSortMember());
    hm.put("sortType", new Character(DL2.getSortType()));

    /*
     * String searchString = DLparam.getSearchString(); if(searchString == null)
     * searchString = "";
     */
    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {

      try {
        // ProjectFacadeHome aa =
        // (ProjectFacadeHome)CVUtility.getHomeObject("com.centraview.projects.project.ProjectFacadeHome","ProjectFacade");
        ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        ProjectFacade remote = (ProjectFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getAllTimeSlipList(userID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Timeslip");
        DL.setDirtyFlag(false);

      } catch (Exception e) {
        System.out.println("ListGenerator::getTimeSlipList" + e);
      }

      // end synchronized

      TimeSlipList DL1 = new TimeSlipList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setListType("Timeslip");

      int beginIndex = 0;
      int endIndex = 0;
      /*
       * int startAT = DL2.getStartAT(); int EndAt = DL2.getEndAT();
       */
      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      DL1 = (TimeSlipList)this.Sort(DL1, DL);

      DL1.setSearchString(searchString);
      if (!searchString.equals(""))
        DL1.search();

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of "" or SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        hm.put("ADVANCESEARCHSTRING", searchString);
        ProjectFacade remote = (ProjectFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getAllTimeSlipList(userID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Timeslip");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getProjectList" + e);
      }

      TimeSlipList DL1 = new TimeSlipList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);
      DL1.setListType("Timeslip");

      int beginIndex = 1;
      int endIndex = DL.size();
      /*
       * int startAT = DL2.getStartAT(); int EndAt = DL2.getEndAT();
       */
      int tnorec = DL.size();

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      DL1 = (TimeSlipList)this.Sort(DL1, DL);

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of ADVANCE:
  }// end of getTimeSlipList( DisplayList DLparam )


  public IndividualList getContactsProjectList(int userID, int startAT, int EndAt,
      String searchString, String sortColumn, String typeOfList) throws CommunicationException,
      NamingException
  {
    IndividualList list = getIndividualList(userID, startAT, EndAt, searchString, sortColumn);
    list.setListType("BottomContacts");
    // list.setTypeoflist( typeOfList );
    IndividualList dl1 = (IndividualList)displayLists.get(new Long(list.getListID()));
    dl1.setListType("BottomContacts");
    // dl1.setTypeoflist( typeOfList );

    return list;
  }

  public IndividualList getContactsProjectList(int userID, DisplayList DLparam)
      throws CommunicationException, NamingException
  {
    IndividualList list = getIndividualList(DLparam);
    list.setListType("BottomContacts");
    IndividualList dl1 = (IndividualList)displayLists.get(new Long(list.getListID()));
    dl1.setListType("BottomContacts");
    return list;
  }

  public FileList getFilesProjectList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn, String fileTypeRequest, int folderID, boolean SystemIncludeFlag)
  {
    FileList list = getFileList(userID, startAT, EndAt, searchString, sortColumn, fileTypeRequest,
        folderID, SystemIncludeFlag);
    list.setListType("BottomFiles");
    // list.setTypeoflist( typeOfList );
    FileList dl1 = (FileList)displayLists.get(new Long(list.getListID()));
    dl1.setListType("BottomFiles");
    // dl1.setTypeoflist( typeOfList );

    return list;
  }

  public FileList getFilesProjectList(int userID, int folderID, DisplayList DLparam)
  {
    FileList list = getFileList(userID, folderID, (FileList)DLparam);
    list.setListType("BottomFiles");
    FileList dl1 = (FileList)displayLists.get(new Long(list.getListID()));
    dl1.setListType("BottomFiles");
    return list;
  }

  public NoteList getNotesProjectList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn, String typeOfList)
  {
    NoteList list = getNoteList(userID, startAT, EndAt, searchString, sortColumn, typeOfList);
    list.setListType("BottomNotes");
    // list.setTypeoflist( typeOfList );
    NoteList dl1 = (NoteList)displayLists.get(new Long(list.getListID()));
    dl1.setListType("BottomNotes");
    // dl1.setTypeoflist( typeOfList );

    return list;
  }

  public NoteList getNotesProjectList(int userID, DisplayList DLparam)
  {
    NoteList list = getNoteList(userID, (NoteList)DLparam);
    list.setListType("BottomNotes");
    NoteList dl1 = (NoteList)displayLists.get(new Long(list.getListID()));
    dl1.setListType("BottomNotes");
    return list;
  }

  /**
   * This will return the Individual lookup list to request
   */
  public TimeSlipList getTimeSlipListForProject(int userID, int projectId)
  {
    TimeSlipList DL;
    try {
      ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
          "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
      ProjectFacade remote = (ProjectFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getTimeSlipListForProject(userID, projectId);
      long currentListID = this.getNextListID();
      DL.setStartAT(1);
      DL.setEndAT(10);
      DL.setRecordsPerPage(10);
      DL.setSortMember("ID");
      DL.setListType("Timeslip");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(DL.size());
      DL.setListID(currentListID);

      int beginIndex = 0;
      int endIndex = 0;
      int startAT = DL.getStartAT();
      int EndAt = DL.getEndAT();

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL.setBeginIndex(beginIndex);
      DL.setEndIndex(endIndex);

      displayLists.put(new Long(currentListID), DL);
      return DL;
    } catch (Exception e) {
      e.printStackTrace();

    }

    return null;

  }

  /**
   * This will return the Individual lookup list to request
   */
  public TimeSlipList getTimeSlipListForProject(int projectId, DisplayList DLparam, int individualID)
  {

    TimeSlipList DL = null;
    TimeSlipList DL2 = (TimeSlipList)DLparam;
    try {
      ProjectFacadeHome aa = (ProjectFacadeHome)CVUtility.getHomeObject(
          "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
      ProjectFacade remote = (ProjectFacade)aa.create();
      remote.setDataSource(this.dataSource);
      HashMap hm = new HashMap();
      hm.put("sortmem", DLparam.getSortMember());
      hm.put("sortType", new Character(DLparam.getSortType()));

      DL = remote.getTimeSlipListForProject(individualID, projectId, hm);

      // DL = remote.getTimeSlipListForProject(userID, projectId);
      long currentListID = this.getNextListID();
      DL.setStartAT(1);
      DL.setEndAT(10);
      DL.setRecordsPerPage(10);
      // DL.setSortMember( "ID" );
      DL.setSortMember(DL2.getSortMember());
      // DL.setSortType(new Character(DL2.getSortType()));
      DL.setSortType(DL2.getSortType());

      DL.setListType("Timeslip");
      DL.setDirtyFlag(false);
      DL.setTotalNoOfRecords(DL.size());
      DL.setListID(currentListID);

      int beginIndex = 0;
      int endIndex = 0;
      int startAT = DL.getStartAT();
      int EndAt = DL.getEndAT();

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL.setBeginIndex(beginIndex);
      DL.setEndIndex(endIndex);

      displayLists.put(new Long(currentListID), DL);

    } catch (Exception e) {
      e.printStackTrace();

    }

    return DL;

  }

  /**
   * This method is called when request for Expense List is made for the first
   * time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public ExpenseList getExpenseList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      ExpenseList dl = new ExpenseList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Expense");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getExpenseList(individualID, dl);

    } else {
      ExpenseList returnDL = null;
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");

      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        AccountList remote = (AccountList)aa.create();
        remote.setDataSource(this.dataSource);

        try {
          returnDL = remote.getExpenseList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setListType("Expense");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        ExpenseList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("Expense");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * This method is called when Expense list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public ExpenseList getExpenseList(int individualID, DisplayList DLparam)
      throws CommunicationException, NamingException
  {

    ExpenseList returnDL = null;
    ExpenseList paramDL = (ExpenseList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));
    AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
        "com.centraview.account.accountlist.AccountListHome", "AccountList");

    try {
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getExpenseList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("Expense");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public ExpenseList createEmptyObject(ExpenseList list)
  {
    ExpenseList dummy = new ExpenseList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }// end of createEmptyObject

  /* ================= EXPENSE ==================== */

  /* ================= PURCHASE ORDER ==================== */

  /**
   * This method is called when request for PurchaseOrder List is made for the
   * first time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public PurchaseOrderList getPurchaseOrderList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      PurchaseOrderList dl = new PurchaseOrderList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("PurchaseOrder");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getPurchaseOrderList(individualID, dl);

    } else {
      PurchaseOrderList returnDL = null;
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        AccountList remote = (AccountList)aa.create();
        remote.setDataSource(this.dataSource);

        try {
          returnDL = remote.getPurchaseOrderList(individualID, hm);
        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setListType("PurchaseOrder");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        PurchaseOrderList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("PurchaseOrder");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        displayLists.put(new Long(currentListID), emptyDL);

      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  /**
   * This method is called when PurchaseOrder List is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public PurchaseOrderList getPurchaseOrderList(int individualID, DisplayList DLparam)
      throws CommunicationException, NamingException
  {

    PurchaseOrderList returnDL = null;
    PurchaseOrderList paramDL = (PurchaseOrderList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));

    AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
        "com.centraview.account.accountlist.AccountListHome", "AccountList");
    try {

      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getPurchaseOrderList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("PurchaseOrder");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public PurchaseOrderList createEmptyObject(PurchaseOrderList list)
  {
    PurchaseOrderList dummy = new PurchaseOrderList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }// end of createEmptyObject

  /* ================= PURCHASE ORDER ==================== */

  // start of CustomFields List
  /** ****************** CustomField List ************************** */

  /**
   * This method will be called first time a CustomFields list is requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return CustomFieldsList
   */
  public CustomFieldList getCustomFieldList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      CustomFieldList dl = new CustomFieldList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("CustomFields");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getCustomFieldList(userID, dl);

    } else {
      CustomFieldList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        HashMap listMap = null;

        try {
          CustomFieldHome aa = (CustomFieldHome)CVUtility.getHomeObject(
              "com.centraview.customfield.CustomFieldHome", "CustomField");
          CustomField remote = (CustomField)aa.create();
          remote.setDataSource(this.dataSource);

          returnDL = remote.getCustomFieldList(userID, hm);

        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setListType("CustomFields");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        CustomFieldList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("CustomFields");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getCustomFieldList

  /**
   * This method is called with empty List
   * @param userid
   * @param DLparam
   * @return CustomFieldList
   */
  public CustomFieldList getCustomFieldList(int userid, DisplayList DLparam)
  {

    CustomFieldList returnDL = null;
    CustomFieldList paramDL = (CustomFieldList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    try {
      CustomFieldHome aa = (CustomFieldHome)CVUtility.getHomeObject(
          "com.centraview.customfield.CustomFieldHome", "CustomField");
      CustomField remote = (CustomField)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getCustomFieldList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("CustomFields");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // end of getCustomFieldList( DisplayList DLparam )

  /**
   * this method creates empty Object
   * @param list
   * @return CustomFieldList
   */
  /*
   * public CustomFieldList createEmptyObject( CustomFieldList list) {
   * CustomFieldList dummy = new CustomFieldList(); dummy.setListType(
   * list.getListType() ); dummy.setPrimaryMemberType (
   * list.getPrimaryMemberType() ); dummy.setPrimaryTable(
   * list.getPrimaryTable() ); dummy.setSortMember ( list.getSortMember() );
   * dummy.setSortType( list.getSortType() ); dummy.setPrimaryMember (
   * list.getPrimaryMember() ); dummy.setStartAT ( list.getStartAT() );
   * dummy.setEndAT ( list.getEndAT() ); dummy.setBeginIndex ( 0 );
   * dummy.setEndIndex ( 0 ); return dummy; }// end of createEmptyObject
   */
  // end of CustomFieldList

  /** ****************** CustomField List ************************** */

  /**
   * This method will be called first time a CustomFields list is requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return CustomFieldsList
   */
  // Changed by Atul Jaysingpure for each module
  public CustomFieldList getCustomFieldList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, String moduleName)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      CustomFieldList dl = new CustomFieldList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("CustomFields");

      long currentListID = ++globalListID;
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getCustomFieldList(userID, dl, moduleName);

    } else {
      CustomFieldList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("module", moduleName);

        HashMap listMap = null;

        try {
          CustomFieldHome aa = (CustomFieldHome)CVUtility.getHomeObject(
              "com.centraview.customfield.CustomFieldHome", "CustomField");
          CustomField remote = (CustomField)aa.create();
          remote.setDataSource(this.dataSource);
          returnDL = remote.getCustomFieldList(userID, hm);

        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setListType("CustomFields");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = ++globalListID;
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);
        returnDL.setModuleName(moduleName);

        CustomFieldList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("CustomFields");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setModuleName(moduleName);
        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getCustomFieldList

  /**
   * This method is called with empty List
   * @param userid
   * @param DLparam
   * @return CustomFieldList
   */
  public CustomFieldList getCustomFieldList(int userid, DisplayList DLparam, String moduleName)
  {
    CustomFieldList returnDL = null;
    CustomFieldList paramDL = (CustomFieldList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    hm.put("module", moduleName);

    try {
      CustomFieldHome aa = (CustomFieldHome)CVUtility.getHomeObject(
          "com.centraview.customfield.CustomFieldHome", "CustomField");
      CustomField remote = (CustomField)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getCustomFieldList(userid, hm);

    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("CustomFields");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setModuleName(moduleName);
    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // end of getCustomFieldList( DisplayList DLparam )

  /**
   * this method creates empty Object
   * @param list
   * @return CustomFieldList
   */
  public CustomFieldList createEmptyObject(CustomFieldList list)
  {
    CustomFieldList dummy = new CustomFieldList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setModuleName(list.getModuleName());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }// end of createEmptyObject

  // end of CustomFieldList

  /* ================= PAYMENT STARTS HERE ==================== */

  /**
   * This method is called when request for Payment List is made for the first
   * time
   * @param individualID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return
   */
  public PaymentList getPaymentList(int individualID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, int invoiceID)
  {
    PaymentList returnDL = null;
    try {
      HashMap hm = new HashMap();
      hm.put("startATparam", new Integer(startATparam));
      hm.put("EndAtparam", new Integer(EndAtparam));
      hm.put("searchString", searchString);
      hm.put("sortmem", sortColumn);
      hm.put("sortType", new Character('A'));
      hm.put("InvoiceID", new Long(invoiceID));

      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);

      try {
        returnDL = remote.getPaymentList(individualID, hm);
      } catch (Exception e) {
        System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
      }

      returnDL.setListType("Payment");
      returnDL.setTotalNoOfRecords(returnDL.size());

      long currentListID = this.getNextListID();
      returnDL.setListID(currentListID);
      returnDL.setStartAT(startATparam);
      returnDL.setEndAT(EndAtparam);

      PaymentList emptyDL = createEmptyObject(returnDL);
      emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
      emptyDL.setListID(currentListID);
      emptyDL.setListType("Payment");
      emptyDL.setStartAT(returnDL.getStartAT());
      emptyDL.setEndAT(returnDL.getEndAT());
      displayLists.put(new Long(currentListID), emptyDL);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return returnDL;

  }

  /**
   * This method is called when Payment list is already created
   * @param individualID
   * @param DLparam
   * @return
   */
  public PaymentList getPaymentList(int individualID, DisplayList DLparam)
  {

    PaymentList returnDL = new PaymentList();
    PaymentList paramDL = (PaymentList)DLparam;
    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();
    int invoiceID = paramDL.getInvoiceID();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(startAT));
    hm.put("EndAtparam", new Integer(EndAt));
    hm.put("searchString", searchString);
    hm.put("sortmem", DLparam.getSortMember());
    hm.put("sortType", new Character(DLparam.getSortType()));
    hm.put("InvoiceID", new Long(invoiceID));

    try {
      AccountListHome aa = (AccountListHome)CVUtility.getHomeObject(
          "com.centraview.account.accountlist.AccountListHome", "AccountList");
      AccountList remote = (AccountList)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getPaymentList(individualID, hm);
    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("Payment");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);
    returnDL.setPowerString(powerString);

    // if(searchString != null && searchString.startsWith("SIMPLE :"))
    // returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  /**
   * this method creates empty Object
   * @param list
   * @return
   */
  public PaymentList createEmptyObject(PaymentList list)
  {
    PaymentList dummy = new PaymentList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }// end of createEmptyObject

  /* ================= PAYMENT ENDS HERE ==================== */

  /* ================== Hr Module ======================== */

  /* ================== Hr Module ======================== */
  /*
   * <P>Added For EmployeeList <P>
   */

  public EmployeeList createEmptyObject(EmployeeList itsEntityList)
  {

    // IndividualList dummy = new IndividualList();
    EmployeeList dummy = new EmployeeList();
    dummy.setListType(itsEntityList.getListType());
    dummy.setPrimaryMemberType(itsEntityList.getPrimaryMemberType());
    dummy.setPrimaryTable(itsEntityList.getPrimaryTable());
    dummy.setSortMember(itsEntityList.getSortMember());
    dummy.setSortType(itsEntityList.getSortType());
    dummy.setPrimaryMember(itsEntityList.getPrimaryMember());
    dummy.setStartAT(itsEntityList.getStartAT());
    dummy.setEndAT(itsEntityList.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    /*
     * getCompleteFlag() getDirtyFlag() getSearchString() getTotNoOfRecords()
     * getRecordsInMemory()
     */
    return dummy;

  }

  public EmployeeList getEmployeeList(DisplayList DLparam)
  {
    EmployeeList DL = null;
    EmployeeList DL2 = (EmployeeList)DLparam;

    String searchString = DLparam.getSearchString();
    if (searchString == null)
      searchString = "";
    // This will do Simple search
    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {

      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        HashMap hm = new HashMap();

        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);
        // TODO: We need to remove this hard-coded value FOR USER ID!!!!!
        DL = remote.getEmployeeDetailList(1, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Employee");
        DL.setSortMember(DL2.getSortMember());
        DL.setDirtyFlag(false);

      } catch (Exception e) {
        System.out.println("ListGenerator::getIndividualList" + e);
      }

      // end synchronized

      // IndividualList DL1 = new IndividualList() ;
      EmployeeList DL1 = new EmployeeList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setListType("Employee");
      DL1.setPrimaryTable("employee");
      // write the logic for begin index, endindex for this specific list type
      // here

      int beginIndex = 0;
      int endIndex = 0;
      int startAT = DL2.getStartAT();
      int EndAt = DL2.getEndAT();

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);

      // search String

      // check for serach string simple or in multiple tables
      // and if the global list is complete
      // if simple search in same GL and make DL1

      // sort

      DL1 = (EmployeeList)this.Sort(DL1, DL);

      DL1.setSearchString(searchString);
      if (!searchString.equals(""))
        DL1.search();

      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setListID(DLparam.getListID());

      // this call will give u sublist from startAT to endAT
      // DL1 = ( EntityList )getFilteredList( DL1.getBeginIndex() ,
      // DL1.getEndIndex() , DL1 );

      return DL1;
    }// end of if SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        HashMap hm = new HashMap();
        hm.put("ADVANCESEARCHSTRING", searchString);
        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);

        // TODO: We need to remove this hard-coded value
        DL = remote.getEmployeeDetailList(1, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Employee");
        DL.setPrimaryTable("employee");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getIndividualListt" + e);
      }

      // IndividualList DL1 = new IndividualList();
      EmployeeList DL1 = new EmployeeList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL1.setListType("Employee");
      DL1.setPrimaryTable("employee");

      int beginIndex = 1;
      int endIndex = DL.size();
      int startAT = DL2.getStartAT();
      int EndAt = DL2.getEndAT();

      int tnorec = DL.size();

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      // DL1 = (IndividualList) this.Sort( DL1 , DL );
      DL1 = (EmployeeList)this.Sort(DL1, DL);

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }
  }// end of public IndividualList getIndividualList( DisplayList DLparam )

  public EmployeeList getEmployeeList(int userID, int startAT, int EndAt, String searchString,
      String sortColumn) throws CommunicationException, NamingException
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      EmployeeList dl = new EmployeeList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Employee");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getEmployeeList(dl);

    } else {
      EmployeeList DL = null;

      HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
          "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
      try {
        HashMap hm = new HashMap();
        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getEmployeeDetailList(userID, hm);

        DL.setSortMember(sortColumn);
        DL.setListType("Employee");
        DL.setDirtyFlag(false);

        if (DL.getTotalNoOfRecords() == DL.getEndIndex()) {
          globalLists.put("Employee", DL);
        }

      } catch (Exception e) {
        System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
      }

      EmployeeList DL1 = new EmployeeList();
      DL1.setListType("Employee");
      DL1.setSortMember(sortColumn);
      DL1.setStartAT(startAT);
      DL1.setEndAT(EndAt);
      DL1.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;

      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      /** *****Sort function*** */
      DL1 = (EmployeeList)Sort(DL1, DL);

      DL1.setTotalNoOfRecords(DL.size());
      // getFilteredList( DL1.getBeginIndex() , DL1.getEndIndex() , DL1 );
      long currentListID = this.getNextListID();
      DL1.setListID(currentListID);

      EmployeeList DL2 = createEmptyObject(DL1);

      DL2.setTotalNoOfRecords(DL.size());
      DL2.setListID(currentListID);
      DL2.setSortMember(sortColumn);
      DL2.setListType("Employee");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);

      return DL1;
    }
  }

  public TimeSheetList getTimeSheetList(int individualID, int startAT, int EndAt,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      TimeSheetList dl = new TimeSheetList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("TimeSheet");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getTimeSheetList(individualID, dl);

    } else {

      TimeSheetList DL = null;

      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        HashMap hm = new HashMap();

        hm.put("startATparam", new Integer(startAT));
        hm.put("EndAtparam", new Integer(EndAt));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getTimeSheetList(individualID, hm);
        DL.setSortMember(sortColumn);
        DL.setListType("TimeSheet");
        DL.setDirtyFlag(false);
        DL.setTotalNoOfRecords(DL.size());
      } catch (Exception e) {
        System.out.println("ListGenerator::getTimeSheetList" + e);
      }
      // end synchronized
      TimeSheetList toReturn = new TimeSheetList();
      toReturn.setListType("TimeSheet");
      toReturn.setSortMember(sortColumn);
      toReturn.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();
      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;

      }

      toReturn.setStartAT(startAT);
      toReturn.setEndAT(EndAt);
      toReturn.setBeginIndex(beginIndex);
      toReturn.setEndIndex(endIndex);

      toReturn = (TimeSheetList)this.Sort(toReturn, DL);
      toReturn.setTotalNoOfRecords(DL.size());

      // getFilteredList( toReturn.getBeginIndex() , toReturn.getEndIndex() ,
      // toReturn );

      long currentListID = this.getNextListID();
      toReturn.setListID(currentListID);

      // here the empty DisplayList will get created
      TimeSheetList DL2 = createEmptyObject(toReturn);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setSortMember(sortColumn);
      DL2.setListID(currentListID);
      DL2.setListType("TimeSheet");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);

      toReturn.setListID(currentListID);

      return toReturn;
    }
  }

  public TimeSheetList getTimeSheetList(int individualID, DisplayList DLparam)
  {
    TimeSheetList DL = null;
    TimeSheetList DL2 = (TimeSheetList)DLparam;
    int listSize = DL2.getTotalNoOfRecords();
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();
    HashMap hm = new HashMap();
    String searchString = DL2.getSearchString();
    if (searchString == null)
      searchString = "";
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", DL2.getSortMember());
    hm.put("sortType", new Character(DL2.getSortType()));

    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {
      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");

        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getTimeSheetList(individualID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("TimeSheet");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getTimeSheetList" + e);
      }
      TimeSheetList DL1 = new TimeSheetList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setListType("TimeSheet");

      int beginIndex = 0;
      int endIndex = 0;

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // search String

      // ****** sort
      DL1 = (TimeSheetList)this.Sort(DL1, DL);

      DL1.setSearchString(searchString);
      if (!searchString.equals(""))
        DL1.search();

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of "" or SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        hm.put("ADVANCESEARCHSTRING", searchString);
        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getTimeSheetList(individualID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("TimeSheet");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getTimeSheetList" + e);
      }

      TimeSheetList DL1 = new TimeSheetList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);
      DL1.setListType("TimeSheet");

      int beginIndex = 1;
      int endIndex = DL.size();
      /*
       * int startAT = DL2.getStartAT(); int EndAt = DL2.getEndAT();
       */

      int tnorec = DL.size();

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      DL1 = (TimeSheetList)this.Sort(DL1, DL);

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of ADVANCE:
  }

  public TimeSheetList createEmptyObject(TimeSheetList list)
  {
    TimeSheetList dummy = new TimeSheetList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  // ////////////////////////////////////////////
  public ExpenseFormList getExpensesList(int individualID, int startAT, int EndAt,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      ExpenseFormList dl = new ExpenseFormList();
      dl.setStartAT(startAT);
      dl.setEndAT(EndAt);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("Expenses");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getExpensesList(individualID, dl);

    } else {

      ExpenseFormList DL = null;

      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        HashMap hm = new HashMap();

        hm.put("startATparam", new Integer(startAT));
        hm.put("EndAtparam", new Integer(EndAt));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getExpenseFormList(individualID, hm);
        DL.setSortMember(sortColumn);
        DL.setListType("Expenses");
        DL.setDirtyFlag(false);
        DL.setTotalNoOfRecords(DL.size());
      } catch (Exception e) {
        System.out.println("ListGenerator::getExpensesList" + e);
      }
      // end synchronized
      ExpenseFormList toReturn = new ExpenseFormList();
      toReturn.setListType("Expenses");
      toReturn.setSortMember(sortColumn);
      toReturn.setSearchString(searchString);

      int beginIndex = 0;
      int endIndex = 0;
      int tnorec = DL.size();
      if (tnorec > EndAt) {
        beginIndex = startAT - 5;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 5;
      } else {
        beginIndex = 1;
        endIndex = EndAt;

      }

      toReturn.setStartAT(startAT);
      toReturn.setEndAT(EndAt);
      toReturn.setBeginIndex(beginIndex);
      toReturn.setEndIndex(endIndex);

      toReturn = (ExpenseFormList)this.Sort(toReturn, DL);
      toReturn.setTotalNoOfRecords(DL.size());

      // getFilteredList( toReturn.getBeginIndex() , toReturn.getEndIndex() ,
      // toReturn );

      long currentListID = this.getNextListID();
      toReturn.setListID(currentListID);

      // here the empty DisplayList will get created
      ExpenseFormList DL2 = createEmptyObject(toReturn);
      DL2.setTotalNoOfRecords(DL.size());
      DL2.setSortMember(sortColumn);
      DL2.setListID(currentListID);
      DL2.setListType("Expenses");
      DL2.setStartAT(startAT);
      DL2.setEndAT(EndAt);
      displayLists.put(new Long(currentListID), DL2);

      toReturn.setListID(currentListID);

      return toReturn;
    }
  }

  public ExpenseFormList getExpensesList(int individualID, DisplayList DLparam)
  {
    ExpenseFormList DL = null;
    ExpenseFormList DL2 = (ExpenseFormList)DLparam;
    int listSize = DL2.getTotalNoOfRecords();
    int startAT = DL2.getStartAT();
    int EndAt = DL2.getEndAT();
    HashMap hm = new HashMap();
    String searchString = DL2.getSearchString();
    if (searchString == null)
      searchString = "";
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", DL2.getSortMember());
    hm.put("sortType", new Character(DL2.getSortType()));

    if (searchString.equals("") || searchString.startsWith("SIMPLE :")) {
      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");

        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);
        DL = remote.getExpenseFormList(individualID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Expenses");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getExpensesList" + e);
      }
      ExpenseFormList DL1 = new ExpenseFormList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setListType("Expenses");

      int beginIndex = 0;
      int endIndex = 0;

      int tnorec = DL.size();

      if (tnorec > EndAt) {
        if (tnorec > EndAt + 100)
          beginIndex = startAT - 100;
        if (beginIndex < 1)
          beginIndex = 1;
        endIndex = EndAt + 100;
      } else {

        beginIndex = 1;
        endIndex = EndAt;
      }

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // search String

      // ****** sort
      DL1 = (ExpenseFormList)this.Sort(DL1, DL);

      DL1.setSearchString(searchString);
      if (!searchString.equals(""))
        DL1.search();

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of "" or SIMPLE :
    else // for ADVANCE:
    {
      String powerString = DLparam.getPowerString();
      try {
        HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject(
            "com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        hm.put("ADVANCESEARCHSTRING", searchString);
        HrFacade remote = (HrFacade)aa.create();
        remote.setDataSource(this.dataSource);

        DL = remote.getExpenseFormList(individualID, hm);
        DL.setTotalNoOfRecords(DL.size());
        DL.setListType("Expenses");
        DL.setDirtyFlag(false);
      } catch (Exception e) {
        System.out.println("ListGenerator::getExpensesList" + e);
      }

      ExpenseFormList DL1 = new ExpenseFormList();
      DL1.setSortMember(DL2.getSortMember());
      DL1.setSortType(DL2.getSortType());
      DL1.setStartAT(DL2.getStartAT());
      DL1.setEndAT(DL2.getEndAT());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);
      DL1.setListType("Expenses");

      int beginIndex = 1;
      int endIndex = DL.size();
      /*
       * int startAT = DL2.getStartAT(); int EndAt = DL2.getEndAT();
       */

      int tnorec = DL.size();

      DL1.setBeginIndex(beginIndex);
      DL1.setEndIndex(endIndex);

      // ****** sort
      DL1 = (ExpenseFormList)this.Sort(DL1, DL);

      DL1.setListID(DLparam.getListID());
      DL1.setTotalNoOfRecords(DL1.size());
      DL1.setBeginIndex(1);
      DL1.setEndIndex(DL1.size());
      DL1.setSearchString(searchString);
      DL1.setPowerString(powerString);

      DL2.setBeginIndex(0);
      DL2.setEndIndex(0);
      DL2.setSearchString(searchString);
      DL2.setPowerString(powerString);

      displayLists.put(new Long(DLparam.getListID()), DL2);
      return DL1;
    }// end of ADVANCE:
  }

  public ExpenseFormList createEmptyObject(ExpenseFormList list)
  {
    ExpenseFormList dummy = new ExpenseFormList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);
    return dummy;
  }

  // ***** EmployeeHandbookList
  /**
   * This method will be called first time a File list for EmployeeHandbook is
   * requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @param fileTypeRequest
   * @param folderID
   * @return FileList
   */
  public FileList getEmployeeHandbookList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, String fileTypeRequest, int folderID,
      boolean SystemIncludeFlag)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      FileList dl = new FileList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("File");
      dl.setSystemIncludeFlag(SystemIncludeFlag);
      dl.setCurrentFolderID(folderID);
      dl.setFileTypeRequest(fileTypeRequest);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getFileList(userID, folderID, dl);

    } else {
      FileList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("fileTypeRequest", fileTypeRequest);
        hm.put("curFolderID", new Integer(folderID));

        hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));

        HashMap listMap = null;

        CvFileFacade cvFFacacde = new CvFileFacade();

        try {
          returnDL = cvFFacacde.getAllFiles(userID, hm, this.dataSource);
        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setFileTypeRequest(fileTypeRequest);
        returnDL.setSystemIncludeFlag(SystemIncludeFlag);
        returnDL.setListType("EmployeeHandbook");
        returnDL.setPrimaryTable("employeehandbook");
        returnDL.setTotalNoOfRecords(returnDL.size());
        returnDL.setCurrentFolderID(folderID);

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        FileList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("EmployeeHandbook");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setFileTypeRequest(fileTypeRequest);
        emptyDL.setSystemIncludeFlag(SystemIncludeFlag);
        emptyDL.setCurrentFolderID(folderID);

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        // System.out.println("[Exception][MarketingListEJB] Exception thrown in
        // x: " + e);
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getFileList

  /**
   * This method is called with empty List
   * @param userid
   * @param folderID
   * @param DLparam
   * @return
   */
  public FileList getEmployeeHandbookList(int userid, int folderID, DisplayList DLparam)
  {

    FileList returnDL = null;
    FileList paramDL = (FileList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();
    String fileTypeRequest = paramDL.getFileTypeRequest();

    boolean SystemIncludeFlag = paramDL.getSystemIncludeFlag();

    int curFolderID = paramDL.getCurrentFolderID();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));
    hm.put("fileTypeRequest", fileTypeRequest); // all my
    hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));
    hm.put("curFolderID", new Integer(curFolderID));
    try {

      CvFileFacade cvFFacacde = new CvFileFacade();

      returnDL = cvFFacacde.getAllFiles(userid, hm, this.dataSource);

    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setFileTypeRequest(fileTypeRequest);
    returnDL.setListType("EmployeeHandbook");
    returnDL.setPrimaryTable("employeehandbook");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    returnDL.setSystemIncludeFlag(SystemIncludeFlag);
    returnDL.setCurrentFolderID(curFolderID);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // ***** EmployeeHandbookList Ends Here
  /** * HR module end*** */

  // User Methods
  /**
   * This method will be called first time a USER list is requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return UserList
   */
  public UserList getUserList(int userID, int startATparam, int EndAtparam, String searchString,
      String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      UserList dl = new UserList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("USER");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getUserList(userID, dl);

    } else {
      UserList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));

        HashMap listMap = null;

        try {
          UserHome aa = (UserHome)CVUtility.getHomeObject(
              "com.centraview.administration.user.UserHome", "User");
          User remote = (User)aa.create();
          remote.setDataSource(this.dataSource);

          returnDL = remote.getUserList(userID, hm);
        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setListType("USER");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        UserList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("USER");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getUserList

  /**
   * This method is called with empty List
   * @param userid
   * @param DLparam
   * @return UserList
   */
  public UserList getUserList(int userid, DisplayList DLparam)
  {

    UserList returnDL = null;
    UserList paramDL = (UserList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    try {
      UserHome aa = (UserHome)CVUtility.getHomeObject(
          "com.centraview.administration.user.UserHome", "User");
      User remote = (User)aa.create();
      remote.setDataSource(this.dataSource);

      returnDL = remote.getUserList(userid, hm);
    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("USER");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // end of getFAQList( DisplayList DLparam )

  /**
   * this method creates empty Object
   * @param list
   * @return UserList
   */
  public UserList createEmptyObject(UserList list)
  {
    UserList dummy = new UserList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }// end of createEmptyObject

  // ******************************** Security Profile ****************

  /**
   * This method will be called first time a SecurityProfile list is requested
   * @param userID
   * @param startATparam
   * @param EndAtparam
   * @param searchString
   * @param sortColumn
   * @return CustomViewList
   */

  public SecurityProfileList getSecurityProfileList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      SecurityProfileList dl = new SecurityProfileList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("SecurityProfile");

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getSecurityProfileList(userID, dl);

    } else {
      SecurityProfileList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        HashMap listMap = null;

        try {

          AuthorizationHome aa = (AuthorizationHome)CVUtility.getHomeObject(
              "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
          Authorization remote = (Authorization)aa.create();
          remote.setDataSource(this.dataSource);

          returnDL = remote.getSecurityProfileList(userID, hm);

        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setListType("SecurityProfile");
        returnDL.setTotalNoOfRecords(returnDL.size());

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        SecurityProfileList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("SecurityProfile");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return returnDL;
    }
  }// end of method getSecurityProfileList

  /**
   * This method is called with empty List
   * @param userid
   * @param DLparam
   * @return CustomViewList
   */
  public SecurityProfileList getSecurityProfileList(int userid, DisplayList DLparam)
  {

    SecurityProfileList returnDL = null;
    SecurityProfileList paramDL = (SecurityProfileList)DLparam;

    int listSize = paramDL.getTotalNoOfRecords();
    int startAT = paramDL.getStartAT();
    int EndAt = paramDL.getEndAT();

    String powerString = paramDL.getPowerString();
    String searchString = paramDL.getSearchString();
    String sortColumn = paramDL.getSortMember();

    HashMap hm = new HashMap();
    hm.put("startATparam", new Integer(Math.max(startAT - 100, 1)));
    hm.put("EndAtparam", new Integer(Math.min(EndAt + 100, listSize)));
    hm.put("searchString", searchString);
    hm.put("sortmem", paramDL.getSortMember());
    hm.put("sortType", new Character(paramDL.getSortType()));

    try {
      AuthorizationHome aa = (AuthorizationHome)CVUtility.getHomeObject(
          "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
      Authorization remote = (Authorization)aa.create();
      remote.setDataSource(this.dataSource);
      returnDL = remote.getSecurityProfileList(userid, hm);

    } catch (Exception e) {
      System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
    }

    returnDL.setListType("SecurityProfile");
    returnDL.setListID(paramDL.getListID());
    returnDL.setStartAT(paramDL.getStartAT());
    returnDL.setEndAT(paramDL.getEndAT());
    returnDL.setSortType(paramDL.getSortType());
    returnDL.setSearchString(searchString);

    returnDL.setPowerString(powerString);

    if (searchString != null && searchString.startsWith("SIMPLE :"))
      returnDL.search();

    returnDL.setTotalNoOfRecords(returnDL.size());

    return returnDL;
  }

  // end of SecurityProfileList( DisplayList DLparam )

  /**
   * this method creates empty Object
   * @param list
   * @return CustomViewList
   */
  public SecurityProfileList createEmptyObject(SecurityProfileList list)
  {
    SecurityProfileList dummy = new SecurityProfileList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }// end of createEmptyObject*/

  // For the Customer View [START]

  public FileList getCustomerFileList(int userID, int startATparam, int EndAtparam,
      String searchString, String sortColumn, String fileTypeRequest, int folderID,
      boolean SystemIncludeFlag)
  {
    if ((searchString != null) && ((searchString.trim()).length() > 0)) {
      FileList dl = new FileList();
      dl.setStartAT(startATparam);
      dl.setEndAT(EndAtparam);
      dl.setSortMember(sortColumn);
      dl.setSortType('A');
      dl.setSearchString(searchString);
      dl.setListType("File");
      dl.setSystemIncludeFlag(SystemIncludeFlag);
      dl.setCurrentFolderID(folderID);
      dl.setFileTypeRequest(fileTypeRequest);

      long currentListID = this.getNextListID();
      dl.setListID(currentListID);

      displayLists.put(new Long(currentListID), dl);

      return this.getFileList(userID, folderID, dl);

    } else {
      FileList returnDL = null;
      try {
        HashMap hm = new HashMap();
        hm.put("startATparam", new Integer(startATparam));
        hm.put("EndAtparam", new Integer(EndAtparam));
        hm.put("searchString", searchString);
        hm.put("sortmem", sortColumn);
        hm.put("sortType", new Character('A'));
        hm.put("fileTypeRequest", fileTypeRequest);
        hm.put("curFolderID", new Integer(folderID));

        hm.put("SystemIncludeFlag", new Boolean(SystemIncludeFlag));

        HashMap listMap = null;

        CvFileFacade cvFFacacde = new CvFileFacade();

        try {
          returnDL = cvFFacacde.getAllCustomerFiles(userID, hm, this.dataSource);
        } catch (Exception e) {
          System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        }

        returnDL.setFileTypeRequest(fileTypeRequest);
        returnDL.setSystemIncludeFlag(SystemIncludeFlag);
        returnDL.setListType("File");
        returnDL.setTotalNoOfRecords(returnDL.size());
        returnDL.setCurrentFolderID(folderID);

        long currentListID = this.getNextListID();
        returnDL.setListID(currentListID);
        returnDL.setStartAT(startATparam);
        returnDL.setEndAT(EndAtparam);

        FileList emptyDL = createEmptyObject(returnDL);
        emptyDL.setTotalNoOfRecords(returnDL.getTotalNoOfRecords());
        emptyDL.setListID(currentListID);
        emptyDL.setListType("File");
        emptyDL.setStartAT(returnDL.getStartAT());
        emptyDL.setEndAT(returnDL.getEndAT());
        emptyDL.setFileTypeRequest(fileTypeRequest);
        emptyDL.setSystemIncludeFlag(SystemIncludeFlag);
        emptyDL.setCurrentFolderID(folderID);

        displayLists.put(new Long(currentListID), emptyDL);
      } catch (Exception e) {
        System.out.println("[Exception][MarketingListEJB] Exception thrown in x: " + e);
        e.printStackTrace();
      }
      return returnDL;
    }

  }

  public HashMap getDisplayLists()
  {
    return this.displayLists;
  }

  public IndividualList getIndividualAndEntityEmailList(int userID, int startAT, int EndAt,
      String searchString, String sortColumn, int dbID) throws CommunicationException,
      NamingException
  {

    IndividualList DL = null;

    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    try {
      HashMap hm = new HashMap();
      hm.put("sortColumn", sortColumn);
      hm.put("dbID", new Integer(dbID));
      ContactFacade remote = (ContactFacade)aa.create();
      remote.setDataSource(this.dataSource);
      DL = remote.getAllIndividualAndEntityEmailList(userID, hm);
      DL.setIndividualId(userID);
      DL.setSortMember(sortColumn);
      DL.setListType("Individual");
      DL.setDirtyFlag(false);
    } catch (Exception e) {
      System.out.println("[Exception] ListGenerator.getIndividualList: " + e.toString());
    }

    IndividualList DL1 = new IndividualList();
    DL1.setListType("Individual");
    DL1.setSortMember(sortColumn);
    DL1.setStartAT(startAT);
    DL1.setEndAT(EndAt);
    DL1.setSearchString(searchString);

    int beginIndex = 0;
    int endIndex = 0;
    int tnorec = DL.size();

    if (tnorec > EndAt) {
      beginIndex = startAT - 5;
      if (beginIndex < 1)
        beginIndex = 1;
      endIndex = EndAt + 5;
    } else {
      beginIndex = 1;
      endIndex = EndAt;
    }

    DL1.setBeginIndex(beginIndex);
    DL1.setEndIndex(endIndex);

    // DL1 = ( IndividualList ) Sort( DL1 , DL );
    DL1 = DL;
    DL1.setTotalNoOfRecords(DL.size());
    long currentListID = this.getNextListID();
    DL1.setListID(currentListID);

    IndividualList DL2 = createEmptyObject(DL1);
    DL2.setTotalNoOfRecords(DL.size());
    DL2.setListID(currentListID);
    DL2.setSortMember(sortColumn);
    DL2.setListType("Individual");
    DL2.setStartAT(startAT);
    DL2.setEndAT(EndAt);
    displayLists.put(new Long(currentListID), DL2);

    return DL1;
  }

  public LiteratureList createEmptyObject(LiteratureList list)
  {
    LiteratureList dummy = new LiteratureList();
    dummy.setListType(list.getListType());
    dummy.setPrimaryMemberType(list.getPrimaryMemberType());
    dummy.setPrimaryTable(list.getPrimaryTable());
    dummy.setSortMember(list.getSortMember());
    dummy.setSortType(list.getSortType());
    dummy.setPrimaryMember(list.getPrimaryMember());
    dummy.setStartAT(list.getStartAT());
    dummy.setEndAT(list.getEndAT());
    dummy.setBeginIndex(0);
    dummy.setEndIndex(0);

    return dummy;
  }// end of createEmptyObject*/

  /** ****************** END OF LiteratureList ***************** */

}
