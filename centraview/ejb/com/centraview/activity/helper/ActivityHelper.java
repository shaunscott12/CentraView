/*
 * $RCSfile: ActivityHelper.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:35 $ - $Author: mking_cv $
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


package com.centraview.activity.helper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;

public interface ActivityHelper extends EJBObject
{

	public int  addActivity( ActivityVO activityvo , int userid ) throws RemoteException;
	public void  updateActivity( ActivityVO actvo, int userid ) throws RemoteException;
	public void deleteActivity(int activityId,int userId) throws RemoteException,AuthorizationFailedException;
	public ActivityVO getActivity(int activityId, int userid) throws RemoteException;
	public String getTypeOfActivity(int activityId) throws RemoteException;
	public void  updateStatus( int activityID,int attendeeID,String status ) throws RemoteException;

  /**
   * Fills in the activityLinkInfo for an Activity. This method fills in the
   * following links:
   * <ul>
   * <li> Entity Name & Entity ID
   * </ul>
   * We can uncomment the for collecting the following information
   * Individual Name  & Individual ID
   * (Project Title & Project ID) or (Opportunity Name & Opportunity ID)
   *
   *
   * @param activityId The Activity ID to get the Activity Links for.
   * @return activityLinkInfo The activityLinkInfo its a Map for
   * Entity Name & Entity ID
   */
  public HashMap getActivityLink(int activityId) throws RemoteException;

  /**
   * Fills in the activityAttendeeInfo for an Activity. This method is a collection of individual ID
   * those are called as and attendee.
   *
   * @param activityId The Activity ID to get the Activity Links for.
   * @return activityAttendeeInfo The activityAttendeeInfo its a collection of Individual ID
   *															who are attending the activity.
   *
   */
  public ArrayList getActivityAttendee(int activityId) throws RemoteException;
	  
  /**
	* @author Kevin McAllister <kevin@centraview.com>
	* Allows the client to set the private dataSource
	* @param ds The cannonical JNDI name of the datasource.
	*/
  public void setDataSource(String ds) throws RemoteException;

}
