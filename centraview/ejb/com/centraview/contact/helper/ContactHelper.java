/*
 * $RCSfile: ContactHelper.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:36:35 $ - $Author: mcallist $
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

package com.centraview.contact.helper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;

/**
 * @author CentraView, LLC.
 */
public interface ContactHelper extends EJBObject {
  public Collection getAllMOCForContact(int userId, int contactId, int contactType)
      throws java.rmi.RemoteException;

  public Collection getAllCustomFieldsForContact(int userId, int contactId, int contactType)
      throws java.rmi.RemoteException;

  public int addAddress(AddressVO address, int contactId, int contactType, int userId,
      boolean updateEntityOrIndividual) throws java.rmi.RemoteException;

  public int updateAddress(AddressVO address, int contactId, int userId,
      boolean updateEntityOrIndividual) throws java.rmi.RemoteException;

  public void updateRelateAddress(AddressVO addressVO, int contactType, int contactId)
      throws java.rmi.RemoteException;

  public void deleteAddress(int addressId, int contactId, int userId,
      boolean updateEntityOrIndividual) throws java.rmi.RemoteException,
      AuthorizationFailedException;

  public void addMOC(MethodOfContactVO moc, int contactId, int contactType, int userId,
      boolean updateEntityOrIndividual) throws java.rmi.RemoteException;

  public void updateMOC(MethodOfContactVO contactMethod, int contactId, int userId,
      boolean updateEntityOrIndividual) throws java.rmi.RemoteException;

  public void deleteMOC(int mocId, int contactId, int userId, boolean updateEntityOrIndividual)
      throws java.rmi.RemoteException, AuthorizationFailedException;

  public void updateModifiedBy(CustomFieldVO customField, int userId)
      throws java.rmi.RemoteException;

  public AddressVO getPrimaryAddressForContact(int userId, int contactId, int contactType)
      throws java.rmi.RemoteException;

  public Collection getPrimaryMOCForContact(int userId, int contactId, int contactType)
      throws java.rmi.RemoteException;

  public HashMap getEmployeeList() throws java.rmi.RemoteException;

  public HashMap getEntityList(int listId) throws java.rmi.RemoteException;

  public Vector getStates() throws java.rmi.RemoteException;

  public Vector getCountry() throws java.rmi.RemoteException;

  public Vector getMOCType() throws java.rmi.RemoteException;

  public Vector getSyncAs() throws java.rmi.RemoteException;

  public Vector getUsers() throws java.rmi.RemoteException;

  public Vector getGroups() throws java.rmi.RemoteException;

  public AddressVO getAddress(int addressId) throws java.rmi.RemoteException;

  public AddressVO getRelatedAddress(int addressId, int contactType, int contactID)
      throws java.rmi.RemoteException;

  public MethodOfContactVO getMoc(int mocId) throws java.rmi.RemoteException;

  public Vector getEmployeeIDs() throws java.rmi.RemoteException;

  public void entityMove(int individualId, int listId, String entityIDs[])
      throws java.rmi.RemoteException;

  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;

  /**
   * This method returns whether an individualID is also a user in the system.
   * @param individualID The individualID to check to see if it's a user.
   * @return true if the individual is also a user, false otherwise.
   */
  public boolean isIndividualAUser(int individualID) throws RemoteException;

  /**
   * This method returns concated FirstName and LastName Of the Individual
   * @param individualID The individualID to collect the Firstname and Lastname
   * @return individualName The individualName is the concatination of FirstName
   *         and LastName
   */
  public String getIndividualName(int individualID) throws RemoteException;

  /**
   * This method returns the Entity's Name
   * @param entityID The entityID to collect the Entity's Name
   * @return entityName The entityName is the Entity's Name
   */
  public String getEntityName(int entityID) throws RemoteException;

  public Vector getCustomerPrimaryAddressForContact(int userId, int contactId, int contactType)
      throws java.rmi.RemoteException;

  /**
   * This method returns the HashMap with EntityID and Associated IndividualID
   * @param emailAddress The emailAddress to collect the EntityID and
   *          IndividualID
   */
  public HashMap getContactInfoForEmailAddress(String emailAddress) throws RemoteException;

  /**
   * This method returns the ArrayList with collection of all the email address
   * belonging to the individuals.
   * @param individualIDs The individualIDs is collection od individualid
   *          seperated by comma.
   * @return toList The ArrayList with collection of all the email address
   *         belonging to the individuals.
   */
  public ArrayList getEmailContactForIndividuals(String individualIDs) throws RemoteException;

  /**
   * This method returns the entity ID for the associated Individual.
   * @param individualID The individualID is for the Individual.
   * @return toList The entityID for the Associated Individual.
   */
  public int getEntityIDForIndividual(int individualID) throws RemoteException;

  /**
   * Updates the modified date and modifiedby. When a contact method or address
   * is updated, it must alter the modified time of the individual or entity
   * record with which it is associated to the current time. If this does not
   * happen, sync is broken.
   */
  public void updateModified(String relateType, int contactId, int userId, int id)
      throws RemoteException;

  /**
   * This method set the JurisdictionID for the Associated Address.
   * @param addressID The addressID is for the Address.
   * @param jurisdictionID The jurisdictionID is for the Address.
   * @return void.
   */
  public void setJurisdictionForAddress(int addressID, int jurisdictionID) throws RemoteException;

  /**
   * This method returns the String with the email address belonging to the
   * individual/Entity.
   * @param contactID You can pass either EntityID or IndividualID and get their
   *          primary emailAddress.
   * @param contactType It will define wheather you are looking for the Entity
   *          or Individual EmailAddress.
   * @return emailAddress It returns the emailAddress belonging to the
   *         individual/Entity.
   */
  public String getPrimaryEmailAddress(int contactID, int contactType) throws RemoteException;

}
