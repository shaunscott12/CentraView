/*
 * $RCSfile: Authorization.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:40 $ - $Author: mking_cv $
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


package com.centraview.administration.authorization;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
* This is  remote interface which is called from client
*/

public interface Authorization extends EJBObject
{
  public int addSecurityProfile(String profileName, ModuleFieldRightMatrix mfrx) throws AuthorizationException,RemoteException;
  public void updateSecurityProfile(int profileId, String profileName, ModuleFieldRightMatrix mfrx) throws AuthorizationException,RemoteException;
  public HashMap getSecurityProfile(int profileId) throws AuthorizationException,RemoteException;
  public HashMap getDefaultRecordPermission(int uid) throws AuthorizationException,java.rmi.RemoteException;

  public SecurityProfileList getSecurityProfileList(int indID,HashMap hashmap) throws RemoteException;
  public void saveMarketingRecordPermission(String recordType, int recordId, int view[], int modify[],int delete[], int publicFlag) throws AuthorizationException,RemoteException;
  public void saveRecordPermission(int uid,int flag, String recordType, int recordId, int view[], int modify[],int delete[]) throws AuthorizationException,RemoteException;
  public void saveCurrentDefaultPermission(String recordType, int recordId, int uid) throws AuthorizationException,RemoteException;
  public void saveDefaultPermissions(int uid, int ownerId, int view[], int modify[],int delete[]) throws AuthorizationException,RemoteException;
  public HashMap getRecordPermission(String recordType, int recordId) throws AuthorizationException,java.rmi.RemoteException;
  public int getRecordPermission(int indId, String moduleName, int recordId) throws AuthorizationException,java.rmi.RemoteException;
  public HashMap getDefaultPermissions(int ownerId) throws AuthorizationException,RemoteException;

  public void setUserDefaultPermissions(int uid, String value)throws AuthorizationException,RemoteException;
  public String getUserDefaultPermission(int uid)throws AuthorizationException,RemoteException;
  public void deleteUserDefaultPrivileges(int uid)throws AuthorizationException,RemoteException;
  public void setRecordToPublic(String moduleid, int recordid)throws AuthorizationException,RemoteException;
  public void deleteRecordFromPublic(String moduleName, int recordid)throws AuthorizationException,RemoteException;
  public String getRecordFromPublic(String moduleid, int recordid) throws AuthorizationException,RemoteException;
  public void deleteMarketingMemberPublicRecords(int listID) throws AuthorizationException,RemoteException;

  public void setAuthorizationType(HashMap authFields) throws  java.rmi.RemoteException;
  public HashMap getAuthorizationType() throws  java.rmi.RemoteException;

  public ModuleFieldRightMatrix getUserSecurityProfileMatrix(int individuialId) throws java.rmi.RemoteException,AuthorizationException;
  public boolean isModuleVisible(String moduleName, int individualId) throws java.rmi.RemoteException,AuthorizationException;
  public ModuleFieldRightMatrix getUserSecurityProfileMatrix(String moduleName, int individualId, boolean byListName) throws AuthorizationException,java.rmi.RemoteException;
  public boolean canPerformRecordOperation(int indId, String moduleName, int recordId, int privilegeLevel) throws AuthorizationException, java.rmi.RemoteException;

  public void updateOwner(String moduleName,int recordId,int indvidualId) throws java.rmi.RemoteException;
  public void updateMarketingRecordOwner(String moduleName, int listID, int individualID)  throws java.rmi.RemoteException;
  public HashMap getOwner(String moduleName,int recordId) throws java.rmi.RemoteException;

  public HashMap getNoneRightFieldMethod(String moduleName, int individualId)  throws java.rmi.RemoteException;
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
  public ModuleFieldRightMatrix getBlankFieldRightMatrix(int defaultRights) throws RemoteException;
  public void deleteSecurityProfile(int pID,int indvID) throws RemoteException,AuthorizationFailedException;

  /**
   * Finds and returns the moduleID (as a String) of the module name
   * passed to the method. If the method is not found,
   * 0 will be returned.
   *
   * @param modulename The name of the module being checked.
   *
   * @return If the module exists, the moduleID, otherwise, 0.
   *
   * @throws AuthorizationException Something went terribly wrong.
   */
  public String getModuleIdByModuleName(String modulename) throws AuthorizationException, RemoteException;

  /**
   * Finds and returns the moduleID (as a String) of the module primary table
   * passed to the method. If the module is not found,
   * 0 will be returned.
   *
   * @param primaryTable The primary table of the module being checked.
   *
   * @return If the module exists, the moduleID, otherwise, 0.
   *
   * @throws AuthorizationException Something went terribly wrong.
   */
  public String getModuleIdByPrimaryTable(String primaryTable) throws AuthorizationException, RemoteException;

  /**
   * Process the individualList and EntityList and Set the permission according to the member permission which are set by the user
   *
   * @param individualIDList The collection of new imported individual.
   *
   * @param entityIDList  The collection of new imported entity.
   *
   * @param listID The list which we are importing individual and entity.
   *
   * @throws AuthorizationException Something went terribly wrong.
   */
  public void saveMarketingRecordPermission(ArrayList individualIDList, ArrayList entityIDList, int listID) throws AuthorizationException, RemoteException;
  public ValueListVO getSecurityProfileList(int individualId, ValueListParameters parameters) throws RemoteException;
}

