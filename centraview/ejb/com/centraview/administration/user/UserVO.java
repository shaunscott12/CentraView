/*
 * $RCSfile: UserVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:48 $ - $Author: mking_cv $
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

package com.centraview.administration.user;

import java.util.Vector;

import com.centraview.contact.individual.IndividualVO;

/**
 * This class stores the properties of User.
 *
 * @author   Iqbal Khan
 */
public class UserVO extends IndividualVO
{
  public static final String US_ENABLED = "ENABLED";
  public static final String US_DISABLED = "DISABLED";
  public static final String UT_EMPLOYEE = "EMPLOYEE";
  public static final String UT_CUSTOMER = "CUSTOMER";
  public static final String UT_ADMINISTRATOR = "ADMINISTRATOR";
  private int userId;
  private String loginName;
  private String userStatus;
  private String userType;
  private String password;
  
  /**  DDNameValue with name as Value and id as Key. */
  private Vector userSecurityProfile; 
  
  /** Vector of integer, used by UI specially. */
  private Vector userSecurityProfileId; 

  /** Default Constructor. */
  public UserVO()
  {
    super();
  }

  /**
   * gets the UerID
   *
   * @return  int
   */
  public int getUserId()
  {
    return this.userId;
  }

  /**
   * set User ID
   *
   * @param   int userId
   */
  public void setUserId(int userId)
  {
    this.userId = userId;
  }

  public String getUserStatus()
  {
    return this.userStatus;
  }

  public void setUserStatus(String userStatus)
  {
    if (!(userStatus.equals(US_ENABLED) || userStatus.equals(US_DISABLED)))
    {
      userStatus = US_DISABLED;
    }
    else
    {
      this.userStatus = userStatus;
    }
  }

  public String getUserType()
  {
    return this.userType;
  }

  public void setUserType(String userType)
  {
    if (!(userType.equals(UT_EMPLOYEE) || userType.equals(UT_CUSTOMER)
        || userType.equals(UT_ADMINISTRATOR)))
    {
      this.userType = UT_EMPLOYEE;
    }
    else
    {
      this.userType = userType;
    }
  }

  public String getLoginName()
  {
    return this.loginName;
  }

  public void setLoginName(String loginName)
  {
    this.loginName = loginName;
  }

  public String getPassword()
  {
    return this.password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public Vector getUserSecurityProfile()
  {
    return this.userSecurityProfile;
  }

  public void setUserSecurityProfile(Vector userSecurityProfile)
  {
    this.userSecurityProfile = userSecurityProfile;
  }

  public Vector getUserSecurityProfileId()
  {
    return this.userSecurityProfileId;
  }

  public void setUserSecurityProfileId(Vector userSecurityProfileId)
  {
    this.userSecurityProfileId = userSecurityProfileId;
  }
}
