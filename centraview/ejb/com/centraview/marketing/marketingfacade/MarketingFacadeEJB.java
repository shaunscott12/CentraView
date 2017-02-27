/*
 * $RCSfile: MarketingFacadeEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 12:22:24 $ - $Author: mcallist $
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

package com.centraview.marketing.marketingfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.marketing.EventAtendeesList;
import com.centraview.marketing.EventDetails;
import com.centraview.marketing.EventList;
import com.centraview.marketing.LiteratureDetails;
import com.centraview.marketing.MarketingList;
import com.centraview.marketing.MarketingListMemberList;
import com.centraview.marketing.PromotionList;
import com.centraview.marketing.List.ListLocal;
import com.centraview.marketing.List.ListLocalHome;
import com.centraview.marketing.List.ListVO;
import com.centraview.marketing.events.EventsLocal;
import com.centraview.marketing.events.EventsLocalHome;
import com.centraview.marketing.events.IndividualNotInvitedException;
import com.centraview.marketing.literature.LiteratureLocal;
import com.centraview.marketing.literature.LiteratureLocalHome;
import com.centraview.marketing.marketinglist.MarketingListLocal;
import com.centraview.marketing.marketinglist.MarketingListLocalHome;
import com.centraview.marketing.promotion.PromotionLocal;
import com.centraview.marketing.promotion.PromotionLocalHome;
import com.centraview.marketing.promotion.PromotionVO;

/**
 * This class is a Statefull session Bean which acts as a Interface for
 * Marketing Module
 */

public class MarketingFacadeEJB implements SessionBean {
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  /**
   * This method sets the context for this Bean
   */
  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  /**
   * These are life cycle methods from EJB
   */
  public void ejbActivate() throws RemoteException
  {}

  public void ejbPassivate() throws RemoteException
  {}

  public void ejbRemove() throws RemoteException
  {}

  public void ejbCreate()
  {}

  public void deleteLiteratureFulfillment(int userid, int elementid)
      throws AuthorizationFailedException
  {
    int result = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.deleteLiterature(elementid, userid);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }

  }

  /**
   * This method returns int from local Bean
   */
  public int addLiterature(HashMap mapLiterature)
  {
    int result = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.addLiterature(mapLiterature);
    } catch (Exception exe) {
      exe.printStackTrace();
      result = 1;
    }
    return result;
  }

  /**
   * This method returns all literatures from local Bean
   */
  public Vector getAllLiterature()
  {
    Vector result = new Vector();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.getAllLiterature();
    } catch (Exception exe) {
      exe.printStackTrace();
      return null;
    }
    return result;
  }

  /**
   * This method returns all deliverymethod from local Bean
   */
  public Vector getAllDeliveryMethod()
  {
    Vector result = new Vector();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.getAllDeliveryMethod();
    } catch (Exception exe) {
      exe.printStackTrace();
      return null;
    }
    return result;
  }

  /**
   * This method returns all activitystatus from local Bean
   */
  public Vector getAllActivityStatus()
  {
    Vector result = new Vector();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.getAllActivityStatus();
    } catch (Exception exe) {
      exe.printStackTrace();
      return null;
    }
    return result;
  }

  /**
   * This method returns literature details from local Bean
   */
  public LiteratureDetails getLiteratureDetails(int activityID, int individualID)
  {
    LiteratureDetails literatureDetails = new LiteratureDetails();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      literatureDetails = local.getLiteratureDetails(activityID, individualID);
    } catch (Exception exe) {
      exe.printStackTrace();
      return null;
    }
    return literatureDetails;
  }

  /**
   * This method returns int from local Bean
   */
  public int editLiterature(HashMap mapLiterature)
  {
    int result = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.editLiterature(mapLiterature);
    } catch (Exception exe) {
      exe.printStackTrace();
      result = 1;
    }
    return result;

  }

  /**
   * This method returns int from local Bean
   */
  public int deleteLiterature(int activityid, int individualID) throws AuthorizationFailedException
  {
    int result = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      LiteratureLocalHome home = (LiteratureLocalHome)ic.lookup("local/Literature");
      LiteratureLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.deleteLiterature(activityid, individualID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return result;

  }

  /**
   * This method returns int from local Bean
   */
  public int addEvent(HashMap mapEvent, int userId) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Events", userId, this.dataSource))
      throw new AuthorizationFailedException("Event - getEventDetails");
    int result = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.addEvent(mapEvent, userId);
    } catch (Exception exe) {
      exe.printStackTrace();
      result = 1;
    }
    return result;
  }

  /**
   * This method returns int from local Bean
   */
  public int addEventRegister(HashMap mapEventRegister, int userId)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Events", userId, this.dataSource))
      throw new AuthorizationFailedException("Events - addEvents");

    int result = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.addEventRegister(mapEventRegister, userId);
    } catch (Exception exe) {
      exe.printStackTrace();
      result = 1;
    }
    return result;
  }

  /**
   * This method returns event details from local Bean
   */
  public EventDetails getEventDetails(int userId, int eventID) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Events", eventID,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Events - getEvents");

    EventDetails eventDetails = new EventDetails();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      eventDetails = local.getEventDetails(userId, eventID);

    } catch (Exception exe) {
      exe.printStackTrace();
    }
    return eventDetails;
  }

  /**
   * Thismethod returns EventList from local Bean
   */
  public EventList getEventList(int userId, HashMap info) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Events", userId, this.dataSource))
      throw new AuthorizationFailedException("Events - getEventList");

    EventList eventlist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      MarketingListLocalHome home = (MarketingListLocalHome)ic.lookup("local/MarketingList");
      MarketingListLocal local = home.create();
      local.setDataSource(this.dataSource);
      eventlist = (EventList)local.getEventList(userId, info);
    } catch (Exception e) {
      System.out.println("Failed in getting EventList");
      e.printStackTrace();
      return null;
    }
    return eventlist;
  }

  /**
   * Thismethod returns PromotionList from local Bean
   */
  public PromotionList getPromotionList(int userId, HashMap info)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Promotion", userId, this.dataSource))
      throw new AuthorizationFailedException("Promotion - getPromotionList");

    PromotionList promotionlist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      MarketingListLocalHome home = (MarketingListLocalHome)ic.lookup("local/MarketingList");
      MarketingListLocal local = home.create();
      local.setDataSource(this.dataSource);
      promotionlist = (PromotionList)local.getPromotionList(userId, info);
    } catch (Exception e) {
      System.out.println("Failed in getting PromotionList");
      e.printStackTrace();
      return null;
    }
    return promotionlist;
  }

  /**
   * Thismethod returns EventAtendeesList from local Bean
   */
  public EventAtendeesList getEventAtendeesList(int userId, HashMap info)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Events", userId, this.dataSource))
      throw new AuthorizationFailedException("Events - getEvents");

    EventAtendeesList EventAtendeeslist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      MarketingListLocalHome home = (MarketingListLocalHome)ic.lookup("local/MarketingList");
      MarketingListLocal local = home.create();
      local.setDataSource(this.dataSource);
      EventAtendeeslist = (EventAtendeesList)local.getEventAtendeesList(userId, info);
    } catch (Exception e) {
      System.out.println("Failed in getting EventAtendeesList");
      e.printStackTrace();
      return null;
    }
    return EventAtendeeslist;
  }

  /**
   * @param userId
   * @param info
   * @return
   */
  public MarketingListMemberList getMarketingListMemberList(int userID, int marketingListID,
      HashMap preferences) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userID, "ListManager", marketingListID,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("MarketingList - getEventDetails");
    }

    MarketingListMemberList marketingListMemberList = null;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      MarketingListLocalHome home = (MarketingListLocalHome)ic.lookup("local/MarketingList");
      MarketingListLocal local = home.create();
      local.setDataSource(this.dataSource);
      marketingListMemberList = (MarketingListMemberList)local.getMarketingListMemberList(userID,
          marketingListID, preferences);
    } catch (Exception e) {
      System.out.println("[Exception] MarketingFacadeEJB.getMarketingListMemberList:" + e);
      e.printStackTrace();
      return null;
    }
    return marketingListMemberList;
  } // end getMarketingListMemberList() method

  /**
   * This method returns a Collection of EventAttendeeVO objects.
   * @param eventID The eventID to get the Event Attendees from.
   * @return A Collection of EventAttendeeVO objects.
   */
  public Collection getAttendeesForEvent(int userID, int eventID)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Events", userID, this.dataSource))
      throw new AuthorizationFailedException("Events - getEvents");

    Collection returnCollection = new ArrayList();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      returnCollection.addAll(local.getAttendeesForEvent(userID, eventID));
    } // end of try block
    catch (Exception exe) {
      exe.printStackTrace();
    } // end of catch block (Exception)
    return returnCollection;
  } // end of getAttendeesForEvent method

  /**
   * This method returns int from local Bean
   */
  public int editEvent(HashMap mapEvent, int userID)
  {
    int result = 1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.editEvent(mapEvent, userID);
    } catch (Exception exe) {
      exe.printStackTrace();
      result = -1;
    }
    return result;
  }

  /**
   * This method returns int from local Bean
   */
  public int deleteEvent(int eventid)
  {
    int result = 1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.deleteEvent(eventid);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return result;
  }

  /**
   * This method returns int from local Bean
   */
  public int deleteEventRegister(HashMap mapEvent)
  {
    int result = 1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.deleteEventRegister(mapEvent);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return result;
  }

  /**
   * Returns list of marketing lists.
   * @author CentraView, LLC.
   */
  public MarketingList getMarketingList(int userId, HashMap info)
  {
    MarketingList marketinglist = null;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      MarketingListLocalHome home = (MarketingListLocalHome)ic.lookup("local/MarketingList");
      MarketingListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      marketinglist = (MarketingList)remote.getMarketingList(userId, info);
    } catch (Exception e) {
      System.out.println("Failed in getting EmailList");
      // e.printStackTrace();
      return (null);
    }
    return (marketinglist);
  } // end getMarketingList() method

  /**
   * This method Parse the information column by column and stores the
   * information in the database and returns a String of Message objects.
   * @param Vector The importList to get the Import row and Column.
   * @param int The headerRow to get the head row.
   * @param int The listID Identify we are are importing records into which
   *          list.
   * @param int The individualID Identify who is logged in an performing the
   *          task.
   * @param Collection The CustomEntList to get the list of Custom Entity.
   * @param Collection The CustomIndList to get the list of Custom Individual.
   * @param String The tabDelimiter to identify the column seperation delimiter.
   * @param String The lineDelimiter to identify the row seperation delimiter.
   * @param String The headLine its a header line.
   * @return HashMap of Message objects.
   */
  public HashMap getImportList(Vector importList, int headerRow, int listid, int indvID,
      Collection CustomEntList, Collection CustomIndList, String tabDelimiter,
      String lineDelimiter, String headLine)
  {
    HashMap messageMap = new HashMap();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ListLocalHome home = (ListLocalHome)ic.lookup("local/List");
      ListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      messageMap = remote.getImportList(importList, headerRow, listid, indvID, CustomEntList,
          CustomIndList, tabDelimiter, lineDelimiter, headLine);
    } catch (Exception e) {
      System.out.println("Failed in getting EventList");
      e.printStackTrace();
      return null;
    }
    return messageMap;
  }

  public Collection getAllList() throws Exception
  {
    Collection col = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ListLocalHome home = (ListLocalHome)ic.lookup("local/List");
      ListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      col = remote.getAllList();
    } catch (Exception e) {
      System.out.println("Failed in getting EmailList");
      e.printStackTrace();
      return col;
    }
    return col;

  }

  public int addList(int userId, ListVO listVO) throws Exception
  {
    int listid = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ListLocalHome home = (ListLocalHome)ic.lookup("local/List");
      ListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      listid = remote.addList(userId, listVO);
    } catch (Exception e) {
      System.out.println("Failed in getting EmailList");
      // e.printStackTrace();
      return (0);
    }
    return (listid);
  } // end addList() method

  public boolean deleteList(int i)
  {
    try {
      InitialContext initialcontext = CVUtility.getInitialContext();
      ListLocalHome listlocalhome = (ListLocalHome)initialcontext.lookup("local/List");
      ListLocal listlocal = listlocalhome.create();
      listlocal.setDataSource(this.dataSource);
      listlocal.deleteList(i);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return true;
  }

  public boolean updateList(int userId, ListVO listVO) throws Exception
  {
    boolean listid = false;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ListLocalHome home = (ListLocalHome)ic.lookup("local/List");
      ListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      listid = remote.updateList(userId, listVO);
    } catch (Exception e) {
      System.out.println("Failed in getting EmailList");
      e.printStackTrace();
      return listid;
    }
    return listid;
  }

  public ListVO viewList(int listid) throws Exception
  {
    ListVO listVO = new ListVO();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ListLocalHome home = (ListLocalHome)ic.lookup("local/List");
      ListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      listVO = remote.viewList(listid);
    } catch (Exception e) {
      System.out.println("MarketingFacadeEJB.viewList" + e);
      e.printStackTrace();
      return listVO;
    }
    return listVO;

  }

  /**
   * This method returns int from local Bean
   */
  public int registerAttendee(HashMap mapEvent)
  {
    int result = 1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.registerAttendee(mapEvent);
    } catch (Exception exe) {
      exe.printStackTrace();
      result = -1;
    }
    return result;
  }

  public boolean hasUserAcceptedEvent(int eventID, int userID) throws IndividualNotInvitedException
  {
    boolean result = false;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.hasUserAcceptedEvent(eventID, userID);
    } catch (IndividualNotInvitedException inie) {
      throw inie;
    } catch (Exception exe) {
      exe.printStackTrace();
    }
    return result;
  }

  /**
   * This method returns String from local Bean
   */
  public String getEventAttendeesForMail(int eventid, int userID)
  {
    String result = "";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.getEventAttendeesForMail(eventid, userID);
    } catch (Exception exe) {
      exe.printStackTrace();
    }
    return result;

  }

  // ************************************ n**************************/
  /**
   * This method returns String from local Bean
   */
  public PromotionVO getPromotion(int userId, HashMap data)
  {
    PromotionVO promotionVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PromotionLocalHome home = (PromotionLocalHome)ic.lookup("local/Promotion");
      PromotionLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      promotionVO = (PromotionVO)remote.getPromotion(userId, data);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return promotionVO;

  }

  /**
   * This method returns String from local Bean
   */
  public String addPromotion(int userId, HashMap data)
  {
    String result = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PromotionLocalHome home = (PromotionLocalHome)ic.lookup("local/Promotion");
      PromotionLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      result = (String)remote.addPromotion(userId, data);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return result;
  }

  /**
   * This method returns String from local Bean
   */
  public String deletePromotion(int userId, int elementID) throws AuthorizationFailedException
  {
    String result = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PromotionLocalHome home = (PromotionLocalHome)ic.lookup("local/Promotion");
      PromotionLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      result = (String)remote.deletePromotion(userId, elementID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return result;

  }

  /**
   * This method returns String from local Bean
   */
  public String updatePromotion(int userId, HashMap data)
  {
    String result = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      PromotionLocalHome home = (PromotionLocalHome)ic.lookup("local/Promotion");
      PromotionLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      result = (String)remote.updatePromotion(userId, data);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return result;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  // For the Customer View [START]

  public EventDetails getCustomerEventDetails(int EventID)
  {

    EventDetails eventDetails = new EventDetails();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      EventsLocalHome home = (EventsLocalHome)ic.lookup("local/Events");
      EventsLocal local = home.create();
      local.setDataSource(this.dataSource);
      eventDetails = local.getCustomerEventDetails(EventID);

    } catch (Exception exe) {
      exe.printStackTrace();
    }
    return eventDetails;

  }
}
