/*
 * $RCSfile: ActivityFacade.java,v $    $Revision: 1.4 $  $Date: 2005/09/13 22:04:29 $ - $Author: mcallist $
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

package com.centraview.activity.activityfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.AuthorizationFailedException;

public interface ActivityFacade extends EJBObject {
  public int addActivity(ActivityVO activityvo, int userid) throws RemoteException,
      AuthorizationFailedException;

  public void deleteActivity(int activityID, int userid) throws RemoteException, EJBException,
      AuthorizationFailedException;

  public void updateActivity(ActivityVO activityvo, int userid) throws RemoteException,
      AuthorizationFailedException;

  public ActivityVO getActivity(int activityId, int userid) throws RemoteException,
      AuthorizationFailedException;

  public Vector getAllResources() throws RemoteException;

  public void updateStatus(int activityID, int attendeeID, String status) throws RemoteException;

  public int getActivityType(int activityId) throws RemoteException;

  /**
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;

  /**
   * Delete a individual from the attendee's List for an activity.
   * @param activityId int representing an activity Identification.
   * @param userId
   *          <code>individualId</code of the user who is deleting the record.
   * @return void
   */
  public void deleteIndividualFromAttendee(int activityId, int userId) throws RemoteException;

  /**
   * For use on the Home Page, get a list of tasks assigned to the individual
   * which are due today or were due in the past.
   * @param individualId
   * @return an ArrayList of TaskVOs
   */
  public ArrayList getTaskList(int individualId) throws RemoteException;
}
