/*
 * $RCSfile: EmailSettingsVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:43 $ - $Author: mking_cv $
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


package com.centraview.administration.emailsettings;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class holds the values for the Email Template for System.
 *
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class EmailSettingsVO implements Serializable
{

  /** The smtpServer of this EmailSettings. */
  private String smtpServer;

  /** The smtpPort of this EmailSettings. */
  private int smtpPort;

  /** The username of this EmailSettings. */
  private String username;

  /** The password of this EmailSettings. */
  private String password;

  /** The authentication of this EmailSettings. */
  private boolean authentication;

  /** The emailTemplateList of this EmailSettings. */
  private ArrayList emailTemplateList;

  /** Default Constructor. */
  public EmailSettingsVO()
  {
    this.emailTemplateList = new ArrayList();
  } //end of EmailSettingsVO constructor

  public EmailSettingsVO getVO()
  {
    return this;
  }

  /**
   * Returns the smtpServer.
   *
   * @return Returns the smtpServer.
   */
  public String getSmtpServer()
  {
    return this.smtpServer;
  } //end of getSmtpServer method

  /**
   * Sets the smtpServer.
   *
   * @param smtpServer The smtpServer to set.
   */
  public void setSmtpServer(String smtpServer)
  {
    this.smtpServer = smtpServer;
  } //end of setSmtpServer method

  /**
   * Returns the smtpPort.
   *
   * @return Returns the smtpPort.
   */
  public int getSmtpPort()
  {
    return this.smtpPort;
  } //end of getSmtpPort method

  /**
   * Sets the smtpPort.
   *
   * @param smtpPort The smtpPort to set.
   */
  public void setSmtpPort(int smtpPort)
  {
    this.smtpPort = smtpPort;
  } //end of setSmtpPort method

  /**
   * Returns the username.
   *
   * @return Returns the username.
   */
  public String getUsername()
  {
    return this.username;
  } //end of getLogin method

  /**
   * Sets the username.
   *
   * @param username The username to set.
   */
  public void setUsername(String username)
  {
    this.username = username;
  } //end of setUsername method

  /**
   * Returns the password.
   *
   * @return Returns the password.
   */
  public String getPassword()
  {
    return this.password;
  } //end of getPassword method

  /**
   * Sets the password.
   *
   * @param password The password to set.
   */
  public void setPassword(String password)
  {
    this.password = password;
  } //end of setPassword method

  /**
   * Returns the authentication.
   *
   * @return Returns the authentication.
   */
  public boolean getAuthentication()
  {
    return this.authentication;
  } //end of isAuthentication method

  /**
   * Sets the authentication.
   *
   * @param authentication The authentication to set.
   */
  public void setAuthentication(boolean authentication)
  {
    this.authentication = authentication;
  } //end of setAuthentication method

  /**
   * Sets the emailTemplateList.
   *
   * @param emailTemplateList The emailTemplateList to set.
   */
  public void setEmailTemplateList(ArrayList emailTemplateList)
  {
    this.emailTemplateList = emailTemplateList;
  } //end of setEmailTemplateList method

  /**
   * Returns the emailTemplateList.
   *
   * @return Returns the emailTemplateList.
   */
  public ArrayList getEmailTemplateList()
  {
    return this.emailTemplateList;
  } //end of getEmailTemplateList method

  public String toString()
  {
    StringBuffer sb = new StringBuffer("EmailTEmplateForm = \n");
    sb.append("    smtpPort = [" + smtpPort + "]\n");
    sb.append("    smtpServer = [" + smtpServer + "]\n");
    sb.append("    username = [" + username + "]\n");
    sb.append("    password = [" + password + "]\n");
    sb.append("    authentication = [" + authentication + "]\n");
    sb.append("    emailTemplateList = [" + emailTemplateList + "]\n");
    sb.append("\n\n");
    return(sb.toString());
  }

}
