/*
 * $RCSfile: MailLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:36 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJBLocalObject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This interface is the local interface
 * for the Mail EJB. All of the
 * public available (locally) methods are defined
 * in this interface.
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public interface MailLocal extends EJBLocalObject
{

  /**
   * Allows the client to set the private dataSource.
   *
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  /**
   * Retrieves the Account Information for a specific account ID.
   * @param accountID The ID of the account being looked up.
   * @return A populated MailAccountVO Object.
   */
  public MailAccountVO getMailAccountVO(int accountID);

  /**
   * Sends an email message and saves it to the sent folder.
   * @param mailAccountVO The account information.
   * @param mailMessageVO The email message to send.
   * @return true if the message was sent, false otherwise.
   * @throws SendFailedException If the message could not be
   * sent to some or any of the recipients.
   * @throws IOException when we try to read the getContent method of the message then it will throw IOException.
   * @throws MessagingException when processing the message some of method throws the MessagingException.   
   * this method.
   */
  public boolean sendMessage(MailAccountVO mailAccountVO, MailMessageVO mailMessageVO) throws SendFailedException,IOException,MessagingException;

  /**
   * Returns the accountID of the default email account for the
   * given individualID. A return value of 0 or less indicates
   * that there is no account configured for the user.
   * @param individualID IndividualID of the user who we're asking about.
   * @return (int) the accountID of the user's default email account.
   */
  public int getDefaultAccountID(int individualID);

  /**
   * Sends a message. This method provides the basic functionality for sending a message.
   * @param message The built message. This cannot be <code>null</code>.
   * @param smtpServer The address of the smtp server. If you plan on relaying,
   *        this can be <code>null</code>. This can be <code>null</code> only if
   * <code>smtpAuthenticationRequired</code> are false.
   * @param username The username of the smtp server (and pop server if pop is
   *        required before smtp). This can be <code>null</code> only if both
   *        <code>connectToPopFirst</code> AND <code>smtpAuthenticationRequired</code>
   *        are false.
   * @param password The password of the smtp server (and pop server if pop is
   *        required before smtp). This can be <code>null</code> only if both
   *        <code>connectToPopFirst</code> AND <code>smtpAuthenticationRequired</code>
   *        are false.
   * @param smtpPort The is the port to connect to the smtp server on. The default
   *        should be 25. If you pass in 0 or -1, 25 will be used.
   * @param connectToPopFirst A boolean to say whether we need to connect to the
   *        pop/imap server before connecting to the smtp server.
   * @param smtpAuthenticationRequired Whether or not smtp authentication is required.
   * @param serverType The mail server type. This is only needed if
   * <code>connectToPopFirst</code> is true. If <code>connectToPopFirst</code>
   * is false, this can be <code>null</code>.
   * @param serverAddress The mail server address. This is only needed if
   * <code>connectToPopFirst</code> is true. If <code>connectToPopFirst</code>
   * is false, this can be <code>null</code>.
   * @return true if the message was sent, false if it wasn't.
   * @throws SendFailedException The message could not be sent for whatever reason.
   *         Check the Exception for a detailed reason.
   * @throws IOException when we try to read the getContent method of the message then it will throw IOException.
   * @throws MessagingException when processing the message some of method throws the MessagingException.   
   */
  public boolean sendSimpleMessage(Message message, String smtpServer, String username, String password, int smtpPort, boolean connectToPopFirst, boolean smtpAuthenticationRequired, String serverType, String serverAddress) throws SendFailedException,IOException,MessagingException;

  /**
   * Sends a message. This method provides the basic functionality for sending a message.
   * @param individualID The ID of the Individual who is sending the message.
   * @param mailMessageVO The built mailMessageVO. This cannot be <code>null</code>.
   * @return true if the message was sent, false if it wasn't.
   * @throws SendFailedException The message could not be sent for whatever reason.
   *         Check the Exception for a detailed reason.
   * @throws MessagingException when processing the message some of method throws the MessagingException.   
   */
  public boolean simpleMessage(int individualID, MailMessageVO message) throws SendFailedException,MessagingException;
  
  /**
   * Gets a list of all the accounts which are marked as "Support"
   * accounts, and calls this.getMail() for each account.
   * @return void
   */
  public void checkAllSupportAccounts();

  /**
   * Deletes folder and any messages inside that folder.
   * @param individualID The individual deleting the folder.
   * @param accountID The account of the folder being deleted.
   * @param folderID The folder ID of the folder being deleted.
   * @return The number of folders deleted (should always be 0 or 1).
   */
  public int deleteFolder(int individualID, int accountID, int folderID);

  /**
   * Returns a ArrayList of the folderID available for an account.
   * The resulting ArrayList contains list of folderID.
   * @param accountID The AccountID to get the list of folders for.
   * @return A ArrayList with a list of FolderID as Key pairs.
   */
  public ArrayList getFolderIDs(int accountID);

  /**
   * Returns an ArrayList of HashMap objects, each representing a
   * message in the given accountID's "Templates" folder.
   * @param individualID The individualID of the user requesting the Template list.
   * @param accountID The email accountID for which to return templates.
   * @return ArrayList of HashMaps representing Templates.
   */
  public ArrayList getTemplateList(int individualID, int accountID);

  /**
   * Returns a ValueListVO representing a list of email Message records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getEmailValueList(int individualID, ValueListParameters parameters);

  /**
   * Returns a ValueListVO representing a list of rule which are applied Message records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getRuleValueList(int individualID, ValueListParameters parameters);

} //end of MailLocal interface

