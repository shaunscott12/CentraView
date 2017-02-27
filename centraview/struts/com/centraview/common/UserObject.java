/*
 * $RCSfile: UserObject.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:18 $ - $Author: mking_cv $
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

package com.centraview.common;

import com.centraview.contact.helper.AddressVO;

public class UserObject implements java.io.Serializable
{

  private String firstName;
  private String lastName;
  private String loginName;
  private UserPrefererences userPref;
  private int individualID;

  private String entityName;
  private AddressVO addressVO;
  private String sessionID;
  private int entityId;
  private String userType;

  // Additional Constructor created used by Customer view.
  public UserObject(int individualID, String firstName, String lastName, UserPrefererences userPref, String userType)
  {
    this.individualID = individualID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.userPref = userPref;
    this.userType = userType;
  }

  public String getfirstName()
  {
    return firstName;
  }

  public String getlastName()
  {
    return lastName;
  }

  public void setfirstName(String value)
  {
    firstName = value;
  }

  public void setlastName(String value)
  {
    lastName = value;
  }

  public ListPreference getListPreference(String str)
  {

    return userPref.getlistPreferences(str);
  }

  public UserPrefererences getUserPref()
  {
    return this.userPref;
  }

  public void setUserPref(UserPrefererences userPref)
  {
    this.userPref = userPref;
  }

  public String getLoginName()
  {
    return this.loginName;
  }

  public void setLoginName(String loginName)
  {
    this.loginName = loginName;
  }

  public int getIndividualID()
  {
    return this.individualID;
  }

  public void setIndividualID(int individualID)
  {
    this.individualID = individualID;
  }
  //end}

  public String getEntityName()
  {
    return this.entityName;
  }

  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  public AddressVO getAddressVO()
  {
    return this.addressVO;
  }

  public void setAddressVO(AddressVO addressVO)
  {
    this.addressVO = addressVO;
  }

  public void setSessionID(String sessionID)
  {
    this.sessionID = sessionID;
  }

  public String getSessionID()
  {
    return (this.sessionID);
  }

  // methods used by CustomerView
  public void setUserType(String userType)
  {
    this.userType = userType;
  }

  public String getUserType()
  {
    return (this.userType);
  }

  public int getEntityId()
  {
    return this.entityId;
  }

  public void setEntityId(int entityId)
  {
    this.entityId = entityId;
  }
}
