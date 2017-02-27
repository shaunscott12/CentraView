/*
 * $RCSfile: AuthorizationLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:42 $ - $Author: mking_cv $
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


import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface AuthorizationLocal extends EJBLocalObject
{
  public int addSecurityProfile(String profileName, ModuleFieldRightMatrix mfrx) throws AuthorizationException;
  public void updateSecurityProfile(int profileId, String profileName, ModuleFieldRightMatrix mfrx) throws AuthorizationException;
  public HashMap getSecurityProfile(int profileId) throws AuthorizationException;
  public SecurityProfileList getSecurityProfileList(int indID,HashMap hashmap);
  public void saveCurrentDefaultPermission(String recordType, int recordId, int uid) throws AuthorizationException;
  public void saveMarketingRecordPermission(String recordType, int recordId, int view[], int modify[],int delete[], int publicFlag) throws AuthorizationException;
  public void saveRecordPermission(int uid, int flag, String recordType, int recordId, int view[], int modify[],int delete[]) throws AuthorizationException;
  public HashMap getRecordPermission(String recordType, int recordId) throws AuthorizationException;
  public int getRecordPermission(int indId, String moduleName, int recordId) throws AuthorizationException;
  public void deleteRecordsFromRecordAuthorization(String moduleName, int recordid) throws AuthorizationException;
  public void deleteMarketingMemberPublicRecords(int listID);
  public void setAuthorizationType(HashMap authFields);
  public HashMap getAuthorizationType();

  public void setUserDefaultPermissions(int uid, String value) throws AuthorizationException;
  public String getUserDefaultPermission(int uid) throws AuthorizationException;
  public void deleteUserDefaultPrivileges(int uid) throws AuthorizationException;
  public void setRecordToPublic(String moduleid, int recordid) throws AuthorizationException;
  public void deleteRecordFromPublic(String moduleName, int recordid) throws AuthorizationException;
  public String getRecordFromPublic(String moduleid, int recordid) throws AuthorizationException;


  public ModuleFieldRightMatrix getUserSecurityProfileMatrix(int individuialId) throws AuthorizationException;
  public boolean isModuleVisible(String moduleName, int individualId) throws AuthorizationException;
  public ModuleFieldRightMatrix getUserSecurityProfileMatrix(String moduleName, int individualId, boolean byListName) throws AuthorizationException;
  public boolean canPerformRecordOperation(int indId, String moduleName, int recordId, int privilegeLevel) throws AuthorizationException;

  public void updateOwner(String moduleName,int recordId,int indvidualId);
  public HashMap getOwner(String moduleName,int recordId);
  public HashMap getNoneRightFieldMethod(String moduleName, int individualId);
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);
  public ModuleFieldRightMatrix getBlankFieldRightMatrix(int defaultRights);

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
  public String getModuleIdByModuleName(String modulename) throws AuthorizationException;

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
  public String getModuleIdByPrimaryTable(String primaryTable) throws AuthorizationException;

  /**
   * Process the individualList and EntityList and Set the permission according to the member permission which are set by the user
   *
   * @param individualIDList The collection of new imported individual.
   *
   * @param entityIDList  The collection of new imported entity.
   *
   * @param listID The list which we are importing individual and entity.
   *
   */
  public void saveMarketingRecordPermission(ArrayList individualIDList, ArrayList entityIDList, int listID) throws AuthorizationException;
  public ValueListVO getSecurityProfileList(int individualId, ValueListParameters parameters);
}
