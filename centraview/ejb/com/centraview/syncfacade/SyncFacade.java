/*
 * $RCSfile: SyncFacade.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:30 $ - $Author: mking_cv $
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


package com.centraview.syncfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.note.NoteVO;

/**
 * This provides the EJB Remote 
 * interface for the SyncFacade EJB.
 *
 * @version 1.0
 */
public interface SyncFacade extends EJBObject
{
  public Collection getNotesList(int individualID) throws RemoteException;
  public int addNote(int individualID, NoteVO noteVO) throws RemoteException,AuthorizationFailedException;
  public boolean editNote(NoteVO noteVO) throws RemoteException;
  public boolean deleteNote(int noteID, int individualID) throws RemoteException, AuthorizationFailedException;
  public HashMap getRelatedActivityInfo(int activityID) throws RemoteException;
  public int findCompanyNameMatch(String companyName, int individualID) throws RemoteException;
  public HashMap getRecurringInfo(int activityID) throws RemoteException;
  public boolean setRecurringFields(int activityID, String recurrenceType, int every, int recurrOn, Date startDate, Date endDate) throws RemoteException;
  public boolean updateRecurringFields(int activityID, String recurrenceType, int every, int recurrOn, Date startDate, Date endDate) throws RemoteException;

  /**
   * Takes a list of record IDs and a module ID, and deletes
   * all records in "recordauthorisation" and "publicrecords"
   * tables that match both the moduleID and recordID. This
   * is used when the user's preference is to sync all records
   * as private, because our EJB layer will originally create
   * new records with the user's default privileges, so we'll
   * have to modify those privileges in order to make the records
   * private.
   * @param moduleID The moduleID of the list of records being deleted.
   * @param recordIDs An ArrayList of Integer objects representing 
   *        the records to be deleted.
   * @return void
   */
  public void markRecordsPrivate(int moduleID, ArrayList recordIDs) throws RemoteException;

  /**
   * Returns a Collection of HashMaps, each representing a fully-populated
   * Individual record for use by the Sync API. 
   * @param individualID The individualID of the user requesting the list.
   * @return Collection of HashMaps representing the individual list.
   */
  public Collection getIndividualList(int individualID) throws RemoteException;

  /**
   * Returns a Collection of HashMaps, each representing a fully-populated
   * Activity record for use by the Sync API. 
   * @param individualID The individualID of the user requesting the list.
   * @return Collection of HashMaps representing the activity list.
   */
  public Collection getActivityList(int individualID) throws RemoteException;

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
} //end of SyncFacade interface
