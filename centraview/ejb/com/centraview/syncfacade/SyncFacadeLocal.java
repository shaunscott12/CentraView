/*
 * $RCSfile: SyncFacadeLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:16 $ - $Author: mking_cv $
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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.note.NoteVO;

public interface SyncFacadeLocal extends EJBLocalObject
{
  public Collection getNotesList(int individualID);
  public int addNote(int individualID, NoteVO noteVO)  throws AuthorizationFailedException;
  public boolean editNote(NoteVO noteVO);
  public boolean deleteNote(int noteID, int individualID) throws AuthorizationFailedException;
  public HashMap getRecurringInfo(int activityID);
  public boolean setRecurringFields(int activityID, String recurrenceType, int every, int recurrOn, Date startDate, Date endDate);
  public boolean updateRecurringFields(int activityID, String recurrenceType, int every, int recurrOn, Date startDate, Date endDate);
  
  /**
   * Returns a Collection of HashMaps, each representing a fully-populated
   * Individual record for use by the Sync API. 
   * @param individualID The individualID of the user requesting the list.
   * @return Collection of HashMaps representing the individual list.
   */
  public Collection getIndividualList(int individualID);
  
  /**
   * Returns a Collection of HashMaps, each representing a fully-populated
   * Activity record for use by the Sync API. 
   * @param individualID The individualID of the user requesting the list.
   * @return Collection of HashMaps representing the activity list.
   */
  public Collection getActivityList(int individualID);

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);
}

