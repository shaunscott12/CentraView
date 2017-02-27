/*
 * $RCSfile: MailAccountVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
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

package com.centraview.mail;

import java.io.Serializable;
import java.util.Date;

/**
 * This class holds the values for the mail
 * accounts.
 * 
 * @author Ryan Grier <ryan@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MailAccountVO implements Serializable
{
  
  /** The value for an imap account. */
  public static final String IMAP_TYPE = "imap";
  
  /** The value for an pop3 account. */
  public static final String POP3_TYPE = "pop3";
  
  /** The value for an smtp account. */
  public static final String SMTP_TYPE = "smtp";
  
  /** The Primary Key of the Mail Account. */
  private int accountID;

  /** The Primary Key of the Mail Account. */
  private String individualName;

  /** The individualID of the owner of this account. */
  private int ownerID;
  
  /** The name of the account. */
  private String accountName;
  
  /** The login name for the account. */
  private String login;
  
  /** The password for the account. */
  private String password;
  
  /** SMTP Server. */
  private String smtpServer;
  
  /** The SMTP Server Port. */
  private int smtpPort;
  
  /** The Address of the Mail Server. */
  private String mailServer;
  
  /** The Account Server Type. Either imap or pop3. */
  private String accountType;
  
  /** 
   * Whether or not to connect to try to connect securely to the server. 
   * If a secure connection, cannot be made, it will fall back on 
   * an insecure connection.
   */
  private boolean secureConnection;
  
  /** 
   * Whether or not to connect to force to connect securely to the server. 
   * If a secure connection cannot be made, the connection won't happen.
   */
  private boolean forceSecureConnection;
  
  /** Whether or not the SMTP Server requires authentication. */
  private boolean authenticationRequiredForSMTP;
  
  /** Whether or not the SMTP Server requires a pop beforehand. */
  private boolean popRequiredBeforeSMTP;
  
  /** Leave Messages on Server. True to leave them on, false to expunge them. */
  private boolean leaveMessagesOnServer;
  
  /** The email address for this account. */
  private String emailAddress;
  
  /** The reply to address for this account. */
  private String replyToAddress;
  
  /** The signature for this account. */
  private String signature;
  
  /** Default Email Account For this Individual. */
  private boolean defaultAccount;
  
  /** Is this account the designated support account for the system. */
  private boolean supportAccount;
  
  /** The number of message fetched during the last connection. */
  private int lastFetchedCount;
  
  /** The date of the last connection. */
  private Date lastFetchedDate;
  
  /** The UID of the last message recieved. */
  private String lastUID;
  
  /**
   * Returns the supportAccount.
   * 
   * @return Returns the supportAccount.
   */
  public boolean isSupportAccount()
  {
    return this.supportAccount;
  }
  
  /**
   * Sets the supportAccount.
   * 
   * @param supportAccount The supportAccount to set.
   */
  public void setSupportAccount(boolean supportAccount)
  {
    this.supportAccount = supportAccount;
  }
 
  /**
   * Returns the accountID.
   * 
   * @return Returns the accountID.
   */
  public int getAccountID()
  {
    return this.accountID;
  } //end of getAccountID method

  /**
   * Sets the accountID.
   * 
   * @param accountID The accountID to set.
   */
  public void setAccountID(int accountID)
  {
    this.accountID = accountID;
  } //end of setAccountID method

  /**
   * Returns the accountName.
   * 
   * @return Returns the accountName.
   */
  public String getAccountName()
  {
    return this.accountName;
  } //end of getAccountName method

  /**
   * Sets the accountName.
   * 
   * @param accountName The accountName to set.
   */
  public void setAccountName(String accountName)
  {
    this.accountName = accountName;
  } //end of setAccountName method

  /**
   * Returns the accountType.
   * 
   * @return Returns the accountType.
   */
  public String getAccountType()
  {
    return this.accountType;
  } //end of getAccountType method

  /**
   * Sets the accountType.
   * 
   * @param accountType The accountType to set.
   */
  public void setAccountType(String accountType)
  {
    this.accountType = accountType;
  } //end of setAccountType method

  /**
   * Returns the defaultAccount.
   * 
   * @return Returns the defaultAccount.
   */
  public boolean isDefaultAccount()
  {
    return this.defaultAccount;
  } //end of isDefaultAccount method

  /**
   * Sets the defaultAccount.
   * 
   * @param defaultAccount The defaultAccount to set.
   */
  public void setDefaultAccount(boolean defaultAccount)
  {
    this.defaultAccount = defaultAccount;
  } //end of setDefaultAccount method

  /**
   * Returns the emailAddress.
   * 
   * @return Returns the emailAddress.
   */
  public String getEmailAddress()
  {
    return this.emailAddress;
  } //end of getEmailAddress method

  /**
   * Sets the emailAddress.
   * 
   * @param emailAddress The emailAddress to set.
   */
  public void setEmailAddress(String emailAddress)
  {
    this.emailAddress = emailAddress;
  } //end of setEmailAddress method

  /**
   * Returns whether or not to connect to force to connect 
   * securely to the server. If a secure connection cannot 
   * be made, the connection won't happen.
   * 
   * @return Returns the forceSecureConnection.
   */
  public boolean isForceSecureConnection()
  {
    return this.forceSecureConnection;
  } //end of isForceSecureConnection method

  /**
   * Sets whether or not to connect to force to connect 
   * securely to the server. If a secure connection cannot 
   * be made, the connection won't happen.
   * 
   * @param forceSecureConnection The forceSecureConnection to set.
   */
  public void setForceSecureConnection(boolean forceSecureConnection)
  {
    this.forceSecureConnection = forceSecureConnection;
  } //end of setForceSecureConnection method

  /**
   * Returns the lastFetchedCount.
   * 
   * @return Returns the lastFetchedCount.
   */
  public int getLastFetchedCount()
  {
    return this.lastFetchedCount;
  } //end of getLastFetchedCount method

  /**
   * Sets the lastFetchedCount.
   * 
   * @param lastFetchedCount The lastFetchedCount to set.
   */
  public void setLastFetchedCount(int lastFetchedCount)
  {
    this.lastFetchedCount = lastFetchedCount;
  } //end of setLastFetchedCount method

  /**
   * Returns the lastFetchedDate.
   * 
   * @return Returns the lastFetchedDate.
   */
  public Date getLastFetchedDate()
  {
    return this.lastFetchedDate;
  } //end of getLastFetchedDate method

  /**
   * Sets the lastFetchedDate.
   * 
   * @param lastFetchedDate The lastFetchedDate to set.
   */
  public void setLastFetchedDate(Date lastFetchedDate)
  {
    this.lastFetchedDate = lastFetchedDate;
  } //end of setLastFetchedDate method

  /**
   * Returns the lastUID.
   * 
   * @return Returns the lastUID.
   */
  public String getLastUID()
  {
    return this.lastUID;
  } //end of getLastUID method

  /**
   * Sets the lastUID.
   * 
   * @param lastUID The lastUID to set.
   */
  public void setLastUID(String lastUID)
  {
    this.lastUID = lastUID;
  } //end of setLastUID method

  /**
   * Returns the leaveMessagesOnServer.
   * 
   * @return Returns the leaveMessagesOnServer.
   */
  public boolean isLeaveMessagesOnServer()
  {
    return this.leaveMessagesOnServer;
  } //end of isLeaveMessagesOnServer method

  /**
   * Sets the leaveMessagesOnServer.
   * 
   * @param leaveMessagesOnServer The leaveMessagesOnServer to set.
   */
  public void setLeaveMessagesOnServer(boolean leaveMessagesOnServer)
  {
    this.leaveMessagesOnServer = leaveMessagesOnServer;
  } //end of setLeaveMessagesOnServer method

  /**
   * Returns the login.
   * 
   * @return Returns the login.
   */
  public String getLogin()
  {
    return this.login;
  } //end of getLogin method

  /**
   * Sets the login.
   * 
   * @param login The login to set.
   */
  public void setLogin(String login)
  {
    this.login = login;
  } //end of setLogin method

  /**
   * Returns the mailServer.
   * 
   * @return Returns the mailServer.
   */
  public String getMailServer()
  {
    return this.mailServer;
  } //end of getMailServer method

  /**
   * Sets the mailServer.
   * 
   * @param mailServer The mailServer to set.
   */
  public void setMailServer(String mailServer)
  {
    this.mailServer = mailServer;
  } //end of setMailServer method

  /**
   * Returns the ownerID.
   * 
   * @return Returns the ownerID.
   */
  public int getOwnerID()
  {
    return this.ownerID;
  } //end of getOwnerID method

  /**
   * Sets the ownerID.
   * 
   * @param ownerID The ownerID to set.
   */
  public void setOwnerID(int ownerID)
  {
    this.ownerID = ownerID;
  } //end of setOwnerID method

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
   * Returns the replyToAddress.
   * 
   * @return Returns the replyToAddress.
   */
  public String getReplyToAddress()
  {
    return this.replyToAddress;
  } //end of getReplyToAddress method

  /**
   * Sets the replyToAddress.
   * 
   * @param replyToAddress The replyToAddress to set.
   */
  public void setReplyToAddress(String replyToAddress)
  {
    this.replyToAddress = replyToAddress;
  } //end of setReplyToAddress method

  /**
   * Returns whether or not to connect to try to connect securely to the server. 
   * If a secure connection, cannot be made, it will fall back on 
   * an insecure connection.
   * 
   * @return Returns the secureConnection.
   */
  public boolean isSecureConnection()
  {
    return this.secureConnection;
  } //end of isSecureConnection method

  /**
   * Sets whether or not to connect to try to connect securely to the server. 
   * If a secure connection, cannot be made, it will fall back on 
   * an insecure connection.
   * 
   * @param secureConnection The secureConnection to set.
   */
  public void setSecureConnection(boolean secureConnection)
  {
    this.secureConnection = secureConnection;
  } //end of setSecureConnection method

  /**
   * Returns the signature.
   * 
   * @return Returns the signature.
   */
  public String getSignature()
  {
    return this.signature;
  } //end of getSignature method

  /**
   * Sets the signature.
   * 
   * @param signature The signature to set.
   */
  public void setSignature(String signature)
  {
    this.signature = signature;
  } //end of setSignature method

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
   * Returns the popBeforeSMTP.
   * 
   * @return Returns the popBeforeSMTP.
   */
  public boolean isPopRequiredBeforeSMTP()
  {
    return this.popRequiredBeforeSMTP;
  }
  
  /**
   * Sets the popBeforeSMTP.
   * 
   * @param popBeforeSMTP The popBeforeSMTP to set.
   */
  public void setPopRequiredBeforeSMTP(boolean popRequiredBeforeSMTP)
  {
    this.popRequiredBeforeSMTP = popRequiredBeforeSMTP;
  }
  
  /**
   * Returns the smtpRequiresAuthentication.
   * 
   * @return Returns the smtpRequiresAuthentication.
   */
  public boolean isAuthenticationRequiredForSMTP()
  {
    return this.authenticationRequiredForSMTP;
  } //end of isSmtpRequiresAuthentication method
  
  /**
   * Sets the smtpRequiresAuthentication.
   * 
   * @param smtpRequiresAuthentication The smtpRequiresAuthentication to set.
   */
  public void setAuthenticationRequiredForSMTP(boolean authenticationRequiredForSMTP)
  {
    this.authenticationRequiredForSMTP = authenticationRequiredForSMTP;
  }
  
  public String getIndividualName()
  {
    return this.individualName;
  }
  
  public void setIndividualName(String newIndividualName)
  {
    this.individualName = newIndividualName;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("MailAccountVO = \n");
    sb.append("    accountID = [" + accountID + "]\n");
    sb.append("    ownerID = [" + ownerID + "]\n");
    sb.append("    accountName = [" + accountName + "]\n");
    sb.append("    login = [" + login + "]\n");
    sb.append("    password = [" + password + "]\n");
    sb.append("    smtpServer = [" + smtpServer + "]\n");
    sb.append("    smtpPort = [" + smtpPort + "]\n");
    sb.append("    mailServer = [" + mailServer + "]\n");
    sb.append("    accountType = [" + accountType + "]\n");
    return(sb.toString());
  }

} //end of MailAccountVO class

