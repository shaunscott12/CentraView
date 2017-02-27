/*
 * $RCSfile: ContactFacade.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:00 $ - $Author: mking_cv $
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


package com.centraview.contact.contactfacade;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import com.centraview.common.AddressList;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.EntityList;
import com.centraview.common.GroupList;
import com.centraview.common.IndividualList;
import com.centraview.common.MOCList;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.group.GroupVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;

public interface ContactFacade extends EJBObject
{
  public EntityVO getEntity(int entId) throws RemoteException;
  public IndividualVO getIndividual(int IndId) throws RemoteException;
  public int createEntity(EntityVO entityDetails, int ActionUserId) throws RemoteException;
  public int createIndividual(IndividualVO indDetail, int actionUSerId) throws RemoteException, AuthorizationFailedException;
  public HashMap getSourceList() throws RemoteException;
  public HashMap getEmployeeList() throws RemoteException;
  public HashMap getEntityList(int listId) throws RemoteException;
  public int createGroup(int userId, GroupVO groupDetail) throws RemoteException, AuthorizationFailedException;
  public GroupVO getGroup(int userId, int groupId) throws RemoteException;
  public void deleteGroup(int userId, int groupId) throws RemoteException,AuthorizationFailedException;
  public GroupList getAllGroupList(int userId, HashMap preference) throws RemoteException;
  public void updateGroup(int userId, GroupVO groupDetail) throws RemoteException, AuthorizationFailedException;
  public void addContactToGroup(int userId, int groupId, int[] contactId) throws RemoteException;
  public void updateEntity(EntityVO entityDetail, int individualID) throws RemoteException, AuthorizationFailedException;
  public void deleteEntity(int entityId, int individualID) throws RemoteException,AuthorizationFailedException;
  public void entityMove(int individualId, int listId, String entityIDs[]) throws RemoteException,AuthorizationFailedException;
  public void updateIndividual(IndividualVO individualDet, int indID) throws RemoteException,AuthorizationFailedException;
  public void deleteIndividual(int individualId, int invID) throws RemoveException, RemoteException,AuthorizationFailedException,NamingException;
  public void createAddress(int contactId, int contactType, AddressVO addressDet, int individualId) throws RemoteException;
  public void createMOC(int contactId, int contactType, MethodOfContactVO mocDet, int individualId) throws RemoteException;
  public AddressList getAllAddresses(int contactId, int contactType) throws RemoteException;
  public MOCList getAllMOC(int contactId, int contactType) throws RemoteException;
  public EntityList getAllEntityList(int userId, HashMap preference) throws RemoteException;
  public IndividualList getAllIndividualList(int userId, HashMap preference) throws RemoteException;
  public void updateAddress(int contactId, AddressVO addressDet) throws RemoteException;
  public void updateRelateAddress(AddressVO addressVO, int contactType, int contactId, int userId) throws RemoteException;
  public void deleteAddress(int addressId, int contactId, int userId) throws RemoteException,AuthorizationFailedException;
  public void deleteMOC(int addressId, int contactId, int userId) throws RemoteException,AuthorizationFailedException;
  public void updateMOC(MethodOfContactVO contactMethod, int contactId, int userId) throws RemoteException;
  public Vector getGroupMemberIDs(int userID, int groupId) throws RemoteException;
  public void deleteGroupMember(int groupMemberId, int groupId) throws RemoteException,AuthorizationFailedException;
  public Vector getStates() throws java.rmi.RemoteException;
  public Vector getCountry() throws java.rmi.RemoteException;
  public Vector getMOCType() throws java.rmi.RemoteException;
  public Vector getSyncAs() throws java.rmi.RemoteException;
  public AddressVO getAddress(int addressId) throws RemoteException;
  public AddressVO getRelatedAddress(int addressId, int contactType, int contactID) throws RemoteException;
  public MethodOfContactVO getMOC(int mocId) throws RemoteException;
  public Vector getUsers() throws java.rmi.RemoteException;
  public Vector getGroups() throws java.rmi.RemoteException;
  public Vector getDBList(int userId) throws RemoteException;
  public int duplicateGroup(int individualID, int groupID) throws RemoteException;
  /**
   * This method uses the CVUtility method getAllAccessible Records
   * to create a temporary table called entityaccess, which provides
   * all the entity Ids taht the passed individualId is allowed to access
   * based on the public record, record authorisation tables and the owner field.
   *
   * I believe the Entity Lists that are returned are already sufficiently
   * pared down based on this info so I am marking this as deprecated.
   *
   * @deprecated the ContactListEJB returns only the Entities which are accessible.
   * @param individualId
   * @return vector of Number Objects representing EntityIds.
   */
  public Vector getEntityAccessRecords(int individualId) throws RemoteException;
  public Vector getIndividualAccessRecords(int individualId) throws RemoteException;
  public Vector getGroupAccessRecords(int individualId) throws RemoteException;
  public HashMap getCustomerIndividual(int indId) throws RemoteException;
  public IndividualList getAllIndividualAndEntityEmailList(int userId, HashMap preference) throws RemoteException;

  public void updateModifiedBy(CustomFieldVO customFieldVO, int userID) throws RemoteException;
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
  public Collection getEmployeeListCollection(int individualID) throws RemoteException;
  /**
   * Method to return the entityid and name of the Entity related to a particular
   * individualId.  Much ligter weight than building a whole IndividualVO to get the info
   * This was originally written to support some relatedInfo stuff.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param individualId an int of the IndividualId
   * @return HashMap with two keys "entityid" and "name", if there wasn't a match it will be an empty hashmap.
   * @throws RemoteException per the EJB spec.
   */
  public HashMap getIndividualRelatedEntity(int individualId) throws RemoteException;
  /**
   * Method to return the marketingList id of the entity related to the entity that
   * is passed in.  If the EntityId passed in returns no results (doesn't exist in the DB)
   * the the Integer 1 will be returned.  This method is used mainly for ensuring Individuals
   * are created in the "correct" marketing list.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param entityId
   * @return Integer of the marketing list for the entityId, or 1 if something goes wrong.
   * @throws RemoteException
   */
  public Integer getEntityMarketingList(int entityId) throws RemoteException;
  /**
   * This method returns what you always wanted, a Collection
   * actually implemented as an ArrayList of
   * AddressVOs for the particular contactID passed in.
   *
   * The collection will be empty if something goes wrong.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param contactId the contact you want to join the address table against.
   * @param contactType the contact type of the contact.
   * @return a Collection, actually an ArrayList either empty or full of addressVOs.
   */
  public Collection getAllAddressVOs(int contactId, int contactType) throws RemoteException;
  /**
   * This method builds a Collection (ArrayList) of Individual VOs
   * where the Individuals related Entity is equal to the entityId.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param entityId
   * @return a Collection of IndividualVOs, all related to the passed entityId.
   */
  public Collection getAllIndividualVOs(int entityId) throws RemoteException;
  /**
   * Simply changes the mocrelate table to change a MOC from relating to
   * one contact and relate it to another.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param mocId the uniqueid of the moc you want to re-relate.
   * @param contactId the unique id of the contact you want to relate it to.
   * @param contactType the type of contact you want to relate it to (Entity = 1, Individual = 2)
   * @return an Int for the number or rows effected.  (the results from the DataAccessLayer)
   */
  public int changeMOCRelate(int mocId, int contactId, int contactType) throws RemoteException;
  /**
   * Simply changes the addressrelate table to change an  address from relating to
   * one contact and relate it to another.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param mocId the uniqueid of the address you want to re-relate.
   * @param contactId the unique id of the contact you want to relate it to.
   * @param contactType the type of contact you want to relate it to (Entity = 1, Individual = 2)
   * @return an Int for the number or rows effected.  (the results from the DataAccessLayer)
   */
  public int changeAddressRelate(int address, int contactId, int contactType) throws RemoteException;
  /**
   * This method differs from the getDBList method, in that it
   * doesn't provide any record rights, it is used in Administration.
   * It also differs in that it returns the raw values, an ArrayList (Collection)
   * of HashMaps.  The keys on the HashMap are listid and title.  This
   * way you won't have to pull it out of a DDNameValue if that isn't
   * your preferred data transfer vehicle.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @return A collection of HashMaps with "listid" and "title".
   */
  public Collection getAllMarketingList() throws RemoteException;

  /**
  * Deletes a given set of Entity's data which are belonging to the ListID
  *
  * @param listID The List ID which we are going to process and delete the Entity in that List
  *
  */
  public void deleteEntity(int listID) throws RemoteException,AuthorizationFailedException;

  /**
  * Deletes a given set of Individual's data which are belonging to the ListID
  *
  * @param listID The List ID which we are going to process and delete the individual in that List
  *
  */
  public void deleteIndividual(int listID) throws RemoteException,AuthorizationFailedException;

  /**
   * This method returns the entity ID for the associated Individual.
   *
   * @param individualID The individualID is for the Individual.
   * @return toList The entityID for the Associated Individual.
   *
   */
  public int getEntityIDForIndividual(int individualID) throws RemoteException;

  /**
   * This method returns whether an individualID is also
   * a user in the system.
   *
   * @param individualID The individualID to check to see if
   * it's a user.
   *
   * @return true if the individual is also a user, false otherwise.
   */
  public boolean isIndividualAUser(int individualID) throws RemoteException;

  /**
   * This method returns the String with the email address belonging to the individual/Entity.
   *
   * @param contactID You can pass either EntityID or IndividualID and get their primary emailAddress.
	 * @param contactType It will define wheather you are looking for the Entity or Individual EmailAddress.
   * @return emailAddress It returns the emailAddress belonging to the individual/Entity.
   *
   */
  public String getPrimaryEmailAddress(int contactID,int contactType) throws RemoteException;

  /**
   * Returns a list of user (Individual) records for display
   * in the permission screen.
   * @return Collection of sql results (each row being a HashMap)
   */
  public Collection getUserListCollection(int individualID) throws RemoteException;
  /**
   * This method just gets the employee list ready for display, in a labelvaluebean.
   * Used initially in permission screens, probably various other uses for it as well.
   * @return
   */
  public Collection getEmployeeListDisplay() throws RemoteException;
}
