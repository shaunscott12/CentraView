/*
 * $RCSfile: PreferenceLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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


package com.centraview.preference;

import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.UserPrefererences;

/**
 * This is  remote interface which is called from client
 */

public interface PreferenceLocal extends EJBLocalObject
{
  public UserPrefererences getUserPreferences(int indiviudalId);
  public void updateUserPreference(int individualId, Vector vecPref);
  
  public Vector getDelegatorIDs(int userID,String moduleName,String typeofoperation);
  public Vector getCalendarDelegatorIds(int userID,String moduleName,String typeofoperation);
  public HashMap getUserDelegation(int userID,String moduleName);
  
  public Vector getEmailDelegation(int individualID)throws AuthorizationFailedException;
  public void updateEmailDelegation(int individualID, Vector delegatorIDs)throws AuthorizationFailedException;
  
  public void updateUserDelegation(int userID,String moduleName,HashMap hm);
  
  /**
   * Updates the <code>preference_value</code> field in the <code>userpreference</code>
   * table for the record that matches the given <code>individualID</code> and where
   * <code>preference_name</code> is "syncAsPrivate". The updated value is the given
   * <code>preferenceValue</code>.
   * @param individualID The individualID of the user whose preference we are updating.
   * @param preferenceValue The String that will be set as the value of preference_value.
   * @return int The number of records updated (should always be 1 or 0)
   */
  public int updateSyncAsPrivatePref(int individualID, String preferenceValue);

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);
  
}   // end class definition

