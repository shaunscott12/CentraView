/*
 * $RCSfile: UserSettingsForm.java,v $    $Revision: 1.3 $  $Date: 2005/09/23 11:03:14 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.centraview.administration.common.AdministrationConstantKeys;

/**
 *  This file is used for storing user input data
 *  during adding or editing of User Settings.
 */
public class UserSettingsForm extends ActionForm
{
  private static Logger logger = Logger.getLogger(UserSettingsForm.class);
  /** To store name. */
  private String name = null;

  /** To store contactID. */
  private int contactID = 0;

  /** To store the userID. */
  private int userID = 0;

  /** To store userName. */
  private String userName = null;

  /** To store password. */
  private String password = null;

  /** To store confirmPassword. */
  private String confirmPassword = null;

  /** To store enabled. */
  private String enabled = "ENABLED";

  /** To stores securityprofiles. */
  private Vector securityProfiles;

  /** To store profiles. */
  private String[] profiles = null;

  /** To store typeofuser. */
  private String userType = null;

  /** To store security. */
  private String security = null;

  /** To store entityID. */
  private int entityID = 0;

  /** To store userTypeList. */
  private Vector userTypeList;

  /**
   * Retruns the Confirmation Password
   * @return confirmationPassword
   */
  public String getConfirmPassword()
  {
    return confirmPassword;
  }

  /**
   * Returns the conactID
   * @return contactID
   */
  public int getContactID()
  {
    return contactID;
  }

  /**
   * Returns the userID.
   * @return userID
   */
  public int getUserID()
  {
    return this.userID;
  }

  /**
   * Returns the enabled status
   * @return enabled
   */
  public String getEnabled()
  {
    return enabled;
  }

  /**
   * Returns the Name
   * @return name
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the Password
   * @return password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns the Profiles
   * @return profiles
   */
  public String[] getProfiles()
  {
    return profiles;
  }

  /**
   * Return the security profiles
   * @return security Profile
   */
  public String getSecurity()
  {
    return security;
  }

  /**
   * Returns the security Profiles
   * @return securityProfiles
   */
  public Vector getSecurityProfiles()
  {
    securityProfiles = new Vector();
    setSecurityProfiles(this.profiles);

    return securityProfiles;
  }

  /**
   * Returns the  UserName
   * @return userName
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * Return the UserType
   * @return userType
   */
  public String getUserType()
  {
    return userType;
  }

  /**
   * Returns the entityID
   * @return entityID
   */
  public int getEntityID()
  {
    return entityID;
  }

  /**
   * Returns the user Type List
   * @return userTypeList
   */
  public Vector getUserTypeList()
  {
    return userTypeList;
  }

  /**
   * Sets the Password
   * @param string
   */
  public void setConfirmPassword(String string)
  {
    confirmPassword = string;
  }

  /**
   * Sets the Contact ID
   * @param contactID
   */
  public void setContactID(int contactID)
  {
    this.contactID = contactID;
  }

  /**
   * Sets the User ID
   * @param userID
   */
  public void setUserID(int userID)
  {
    this.userID = userID;
  }

  /**
   * Sets the Enabled Status
   * @param status
   */
  public void setEnabled(String status)
  {
    enabled = status;
  }

  /**
   * Sets the name
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Sets the password
   * @param Password
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Sets the Profiles
   * @param profiles
   */
  public void setProfiles(String[] profiles)
  {
    this.profiles = profiles;
  }

  /**
   * Sets the Security
   * @param security
   */
  public void setSecurity(String security)
  {
    this.security = security;
  }

  /**
   * Sets the security Profiles
   * @param securityProfiles
   */
  public void setSecurityProfiles(String[] profile)
  {
    if (profile != null) {
      for (int i = 0; i < profile.length; i++) {
        this.securityProfiles.addElement(Integer.valueOf(profile[i]));
      }
    }
  }

  /**
   * Sets the UserName
   * @param UserName
   */
  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  /**
   * Sets the UserType
   * @param userType
   */
  public void setUserType(String userType)
  {
    this.userType = userType;
  }

  /**
   * Sets the entityID
   * @param entityID
   */
  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  }

  /**
   * Sets the userTypeList
   * @param Vector
   */
  public void setUserTypeList(Vector userTypeList)
  {
    this.userTypeList = userTypeList;
  }

  /**
   * Validate method to validate the password and confirm password
   * @param ActionMapping mapping
   * @param HttpServletRequest request
   * @return ActionErrors
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();
    boolean existingUser = false;

    try {
      request.setAttribute("typeofmodule", AdministrationConstantKeys.USERADMINISTRATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.USERADMINISTRATION_USER);
      if (this.userID != 0) {
        existingUser = true;
      } //end of if statement (this.userID != 0)
      //If Customer ID is not properly selected using look up throw the error
      if (!(contactID > 0)) {
        ActionMessage error = new ActionMessage("error.administration.newuser.id.required", new ActionMessage("label.admin.useradmin.name"));
        errors.add("error.administration.newuser.id.required", error);
      }
      //If Name is not properly selected throw the error
      if ((name == null) || !(name.length() > 0)) {
        ActionMessage error = new ActionMessage("error.administration.newuser.name.required", new ActionMessage("label.admin.useradmin.name"));
        errors.add("error.administration.newuser.name.required", error);
      }
      //If UserName is not properly selected throw the error
      if ((userName == null) || !(userName.length() > 0)) {
        ActionMessage error = new ActionMessage("error.administration.newuser.username.required", new ActionMessage("label.admin.useradmin.username"));
        errors.add("error.administration.newuser.username.required", error);
      }
      if (!(existingUser && password.length() <= 0 && confirmPassword.length() <= 0)) {
        //If Customer Password is not properly selected throw the error
        if ((password == null) || !(password.length() > 0)) {
          ActionMessage error = new ActionMessage("error.administration.newuser.password.required", new ActionMessage("label.admin.useradmin.newpassword"));
          errors.add("error.administration.newuser.password.required", error);
        }
        // If new password and confirm password not matching throw the error.
        if (!(password.equals(confirmPassword))) {
          ActionMessage error = new ActionMessage("error.administration.newuser.passwords.notmatched", new ActionMessage("label.admin.useradmin.newpassword"));
          errors.add("error.administration.newuser.passwords.notmatched", error);
        }
      }
      //Check whether security Profile is selcted or not
      //(profiles.length !=0) && !(profiles.length > 0)
      if (profiles != null && profiles.length == 0) {
        ActionMessage error = new ActionMessage("error.administration.newuser.securityprofiles.required", new ActionMessage("label.admin.useradmin.securityprofiles"));
        errors.add("error.administration.newuser.securityprofiles.required", error);
      }
    } catch (Exception e) {
      logger.error("[validate] Exception thrown.", e);
    }
    return errors;
  }
  // end of validate
}