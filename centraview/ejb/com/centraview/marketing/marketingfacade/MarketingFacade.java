/*
 * $RCSfile: MarketingFacade.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 12:22:24 $ - $Author: mcallist $
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.marketing.EventAtendeesList;
import com.centraview.marketing.EventDetails;
import com.centraview.marketing.EventList;
import com.centraview.marketing.LiteratureDetails;
import com.centraview.marketing.MarketingList;
import com.centraview.marketing.MarketingListMemberList;
import com.centraview.marketing.PromotionList;
import com.centraview.marketing.List.ListVO;
import com.centraview.marketing.events.IndividualNotInvitedException;
import com.centraview.marketing.promotion.PromotionVO;

/**
 * remote interface for Email
 */
public interface MarketingFacade extends EJBObject {

  public void deleteLiteratureFulfillment(int userid, int elementid) throws RemoteException,
      AuthorizationFailedException;

  public int addLiterature(HashMap mapLiterature) throws CreateException, RemoteException;

  public int editLiterature(HashMap mapLiterature) throws RemoteException;

  public int deleteLiterature(int activityid, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public Vector getAllLiterature() throws RemoteException;

  public Vector getAllDeliveryMethod() throws RemoteException;

  public Vector getAllActivityStatus() throws RemoteException;

  public LiteratureDetails getLiteratureDetails(int activityID, int individualID)
      throws RemoteException;

  // Event
  public EventList getEventList(int userId, HashMap preference) throws RemoteException,
      AuthorizationFailedException;

  public EventAtendeesList getEventAtendeesList(int userId, HashMap preference)
      throws RemoteException, AuthorizationFailedException;

  public int addEvent(HashMap mapEvent, int userID) throws CreateException, RemoteException,
      AuthorizationFailedException;

  public int addEventRegister(HashMap mapEventRegister, int userID) throws CreateException,
      RemoteException, AuthorizationFailedException;

  public int editEvent(HashMap mapEvent, int userID) throws RemoteException;

  public int deleteEvent(int eventid) throws RemoteException, AuthorizationFailedException;

  public int deleteEventRegister(HashMap mapEvent) throws RemoteException,
      AuthorizationFailedException;

  public EventDetails getEventDetails(int userID, int EventID) throws RemoteException,
      AuthorizationFailedException;

  public int registerAttendee(HashMap mapEvent) throws RemoteException;

  public boolean hasUserAcceptedEvent(int eventID, int userID) throws RemoteException,
      IndividualNotInvitedException;

  public String getEventAttendeesForMail(int eventid, int userId) throws RemoteException;

  public Collection getAttendeesForEvent(int userID, int eventID) throws RemoteException,
      AuthorizationFailedException;

  public MarketingListMemberList getMarketingListMemberList(int userID, int marketingListID,
      HashMap preferences) throws RemoteException, AuthorizationFailedException;

  // promotion
  public PromotionList getPromotionList(int userId, HashMap preference) throws RemoteException,
      AuthorizationFailedException;

  public Collection getAllList() throws Exception, RemoteException;

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
  public HashMap getImportList(Vector importList, int headRow, int listid, int indvID,
      Collection CustomEntList, Collection CustomIndList, String tabDelimiter,
      String lineDelimiter, String headLine) throws Exception, RemoteException;

  // marketing added by shirish
  public MarketingList getMarketingList(int userId, HashMap info) throws RemoteException, Exception;

  public int addList(int userId, ListVO listVO) throws Exception, RemoteException;

  public boolean deleteList(int i) throws RemoteException;

  public boolean updateList(int userId, ListVO listVO) throws Exception, RemoteException;

  public ListVO viewList(int listid) throws Exception, RemoteException;

  // 
  public PromotionVO getPromotion(int userId, HashMap data) throws RemoteException, Exception;

  public String addPromotion(int userId, HashMap data) throws RemoteException, Exception;

  public String deletePromotion(int userId, int elementID) throws RemoteException,
      AuthorizationFailedException;

  public String updatePromotion(int userId, HashMap data) throws RemoteException, Exception;

  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;

  // Customer Event
  public EventDetails getCustomerEventDetails(int EventID) throws RemoteException;
}
