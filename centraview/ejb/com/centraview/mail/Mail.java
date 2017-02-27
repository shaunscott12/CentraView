/*
 * $RCSfile: Mail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import com.centraview.common.DisplayList;
import com.centraview.file.CvFolderVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This interface is the remote interface
 * for the Mail EJB. All of the
 * public available (remotely) methods are defined
 * in this interface.
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public interface Mail extends EJBObject
{
  /**
   * Allows the client to set the private dataSource.
   * @param ds The cannonical JNDI name of the datasource.
   * @throws RemoteException This exception is defined in the
   * method signature to provide backward compatibility
   * for applications written for the EJB 1.0 specification.
   * Enterprise beans written for the EJB 1.1 specification
   * should throw the javax.ejb.EJBException instead of this
   * exception. Enterprise beans written for the EJB2.0 and
   * higher specifications must throw the javax.ejb.EJBException
   * instead of this exception.
   */
  public void setDataSource(String ds) throws RemoteException;

  /**
   * Returns the number of defined email accounts for the
   * given individualID. A return value of 0 or less indicates
   * that there is no account configured for the user.
   * @param individualID IndividualID of the user who we're asking about
   * @return (int) the number of accounts which  exist for the user
   */
  public int getNumberOfAccountsForUser(int individualID) throws EJBException, RemoteException;

  /**
   * Returns the accountID of the default email account for the
   * given individualID. A return value of 0 or less indicates
   * that there is no account configured for the user.
   * @param individualID IndividualID of the user who we're asking about.
   * @return (int) the accountID of the user's default email account.
   */
  public int getDefaultAccountID(int individualID) throws EJBException, RemoteException;

  /**
   * Returns the default MailFolderVO object of the
   * accountID passed into the method. If the
   * folder is not found, an empty MailFolderVO (with
   * a FolderID of -1) is returned. <p>
   * This method is looking for the name of the folder
   * used here: com.centraview.mail.MailFolderVO.DEFAULT_FOLDER_NAME
   * @param accountID The Account ID that the Mail Folder belongs to.
   * @return A populated MailFolderVO if the folder exists.
   * @see com.centraview.mail.MailFolderVO.DEFAULT_FOLDER_NAME
   */
  public MailFolderVO getPrimaryEmailFolder(int accountID) throws EJBException, RemoteException;

  /**
   * Gets the mail from all accounts configured for the given
   * individualID. This method actually gets the list of mail
   * accounts for the given user, and calls this.getMail for
   * each account.
   * @param individualID The individualID of the user whose
   * accounts we are to check.
   * @return void
   */
  public void checkAllAccountsForUser(int individualID, int folderID) throws Exception, RemoteException;

  /**
   * Returns a fully populated MailMessageVO object based
   * on the messageID. If the message is not found, a
   * MailMessageObject with a MesssageID of -1 will be returned.
   * @param messageID The ID of the Mail Message we are looking for.
   * @return A fully populated MailMessageVO.
   */
  public MailMessageVO getEmailMessageVO(int individualID, int messageID) throws EJBException, RemoteException;

  /**
   * Updates the emailmessage.MessageRead field in the database,
   * setting it to 'YES'.
   * @param individualID The individual who is changing the read status.
   * @param messageID The ID of the message whose status is to be changed.
   * @return boolean - true for success, false for failure.
   */
  public boolean markMessageRead(int individualID, int messageID) throws Exception, RemoteException;

  /**
   * Updates the emailmessage.MessageRead field in the database,
   * setting it to 'NO'.
   * @param individualID The individual who is changing the read status.
   * @param messageID The ID of the message whose status is to be changed.
   * @return boolean - true for success, false for failure.
   */
  public boolean markMessageUnread(int individualID, int messageID) throws Exception, RemoteException;

  /**
   * Returns an ArrayList of mail accountIDs for those
   * accounts which belong to the given individualID.
   * @param individualID The individualID of the user
   *        whose accounts we are looking for.
   * @return ArrayList of int's - the account ID for
   *         each account this user owns.
   */
  public ArrayList getUserAccountList(int individualID) throws Exception, RemoteException;

  /**
   * Returns an ArrayList of mail accountIDs for those
   * accounts which delegated to the given individualID.
   * @param individualID The individualID of the user
   *        whose delegated accounts we are looking for.
   * @return ArrayList of int's - the account ID for
   *         each delehated account this user owns.
   */
  public ArrayList getDelegatedAccountList(int individualID) throws Exception, RemoteException;

  /**
   * Retrieves the Account Information for a specific account ID.
   * @param accountID The ID of the account being looked up.
   * @return A populated MailAccountVO Object.
   */
  public MailAccountVO getMailAccountVO(int accountID) throws EJBException, RemoteException;

  /**
   * Sends an email message and saves it to the sent folder.
   * @param mailAccountVO The account information.
   * @param mailMessageVO The email message to send.
   * @return true if the message was sent, false otherwise.
   * @throws SendFailedException If the message could not be
   * sent to some or any of the recipients.
   * @throws IOException when we try to read the getContent method of the message then it will throw IOException.
   * @throws MessagingException when processing the message some of method throws the MessagingException.   
   * 
   */
  public boolean sendMessage(MailAccountVO mailAccountVO, MailMessageVO mailMessageVO) throws SendFailedException, IOException,MessagingException, RemoteException;

  /**
   * This method simply finds the serverType associated with
   * a given Message ID and delegates the rest of the delete
   * responsibility to a seperate Delete Message method.
   * @param messageId the unique ID for a particular message that will be deleted.
   * @param individualId the individual id of the user requesting the delete.
   * @return The number of messages affected by the delete. This number should always be 0 or 1.
   */
  public int deleteMessage(int messageId, int individualId) throws RemoteException;

  /**
   * Returns a MailFolderVO object based on the
   * emailFolderID passed into the method. If the
   * folder is not found, an empty MailFolderVO (with
   * a FolderID of -1) is returned.
   * @param emailFolderID The ID of the Mail Folder.
   * @return A populated MailFolderVO if the folder exists.
   */
  public MailFolderVO getEmailFolder(int emailFolderID) throws RemoteException;

  /**
   * Adds a new Folder to an account.
   * @param mailFolderVO The new MailFolderVO
   * @return The ID of the new Folder.
   */
  public int addEmailFolder(MailFolderVO mailFolderVO, boolean createOnRemoteServer) throws RemoteException;


  /**
   * Returns a HashMap of the folders available for
   * an account. <p>
   * The resulting HashMap contains the folderID
   * as the key and the name as the value.
   * @param accountID The AccountID to get the list of folders for.
   * @return A HashMap with FolderID/Name as the Key/Value pairs.
   */
  public HashMap getFolderList(int accountID) throws RemoteException;

  /**
   * Creates a new email account in the database, and returns the
   * account ID of the newly created email account.
   * @param mailAccountVO a MailAccountVO object containing the data
   *        for the new account. *ALL* fields must be supplied, so
   *        please do your validation at the Struts layer.
   * @return int account ID of the newly created email account.
   * @throws MessagingException If any of the account credentials are wrong then it will throw the error.
   */
  public int addEmailAccount(MailAccountVO mailAccountVO) throws RemoteException, MessagingException;

  /**
   * Returns an ArrayList that represents the full folder path
   * of the given folderID. The list will be keyed on folderID
   * with the value being the folder name. This list can be used
   * in the view layer to display a linked list of the full path.
   * @param folderID The ID of the folder whose path we're asking for.
   * @return ArrayList representing the full folder path.
   */
  public ArrayList getFolderFullPath(int folderID) throws RemoteException, Exception;

  /**
   * Adds a relationship between an email message and a new folder.
   * The old relationship is still there until it is cleaned up
   * by the getIMAPMail method in this EJB.
   * @param individualID The individual who is moving the folder.
   * @param messageID The message to be moved.
   * @param newFolderID The new folderID.
   * @return The number of messages moved (should always be 1 or 0).
   */
  public int moveMessageToFolder(int individualID, int messageID, int newFolderID) throws RemoteException;

  /**
   * Returns a MailFolderVO object based on the accountID and
   * folderName passed into the method. If the folder is not found,
   * an empty MailFolderVO (with a FolderID of -1) is returned.
   * @param accountID The Account ID that the Mail Folder belongs to.
   * @param folderName The name of the Mail Folder being searched for.
   * @return A populated MailFolderVO if the folder exists.
   */
  public MailFolderVO getEmailFolderByName(int accountID, String folderName) throws RemoteException;

  /**
   * Saves a draft message in the database and returns the ID
   * of that message.
   * @param individualID The user who is adding the draft message.
   * @param messageVO The MailMessageVO object representing the draft message.
   * @return int - the messageID of the new draft message or -1 for error.
   */
  public int saveDraft(int individualID, MailMessageVO messageVO) throws RemoteException, Exception;

  /**
   * Updates an Email Account based on the MailAccountVO
   * passed into this method.
   * @param mailAccountVO The new contents of the Mail Account.
   * @throws MessagingException If any of the account credentials are wrong then it will throw the error.
   */
  public void updateEmailAccount(MailAccountVO mailAccountVO) throws RemoteException,MessagingException;

  /**
   * Deletes the given email account from the database, but *NOT*
   * the messages or folders associated with this email account.
   * @param individualID The user who is deleting the account.
   * @param accountID The account ID of the account to be deleted.
   * @return boolean true for success, false for failure.
   */
  public boolean deleteEmailAccount(int individualID, int accountID) throws RemoteException;

  /**
   * Returns a HashMap containing two Integer elements; "previousMessage"
   * which holds the messageID of the previous email message in the list,
   * and "nextMessage" which holds the messageID of the next message in
   * the list. If either the previous or next message cannot be found
   * (or do not exist), then the corresponding value in the HashMap is
   * set to -1. Also, if an error occurs, then the values will be -1.
   * @param individualID The individualID of the user who is asking for
   *        this information.
   * @param messageID The messageID of the email message for which we
   *        are getting the previous and next messages.
   * @return HashMap containing "previousMessage" and "nextMessage" elements.
   */
  public HashMap getPreviousNextLinks(int individualID, int messageID) throws RemoteException;

  /**
   * Returns an ArrayList which contains HashMap objects representing
   * the email folders which are a sub-folder of the given <code>parentID
   * parameter.
   * @param individualID The individualID of the user who is asking for the list.
   * @param parentID The folderID of the folder for which we want the list of sub-folders.
   * @return ArrayList of HashMaps representing the sub-folders.
   */
  public ArrayList getSubFolderList(int individualID, int parentID) throws RemoteException;

  /**
   * Updates the record for a given folder in the database, taking
   * the information from the given MailFolderVO object. Currently,
   * we're only allowing folder Name and Parent ID to be updated,
   * all other fields will be disregarded.
   * @param individualID The individualID of the user who is changing the folder.
   * @param folderVO A MailFolderVO object containing the details of the
   *        changed folder. folderID, folderName and parentID are required.
   * @return int - the number of rows affected by the update.
   */
  public int editFolder(int individualID, MailFolderVO folderVO) throws RemoteException;

  /**
   * Returns a DisplayList of Rules.
   * @param individualID The individualID of the user asking for the list.
   * @param accountID The accountID of the email account whose rules will be returned.
   * @param preference The preference its a collection of information to Sort in Ascending/Descending Order, Sort on particular column & searchstring
   * @return A Display List of email rules casted to RuleList.
   */
  public DisplayList getRuleList(int individualID, int accountID, HashMap preference) throws RemoteException;


  /**
   * Returns an Vector of mail accountIDs for those
   * accounts.
   *
   * @return ArrayList of DDNameValue which will hold the account ID and Account email address.
   *
   */
  public Vector getAccountList() throws RemoteException, Exception;

  /**
   * This method is to help cut down on processing time on the Home page
   * which originally made at least 4 EJB remote calls from the JSP and then would loop
   * on the list of folders.  This will simply return the folderVOs of the desired folders
   * or it will be empty if there is nothing to show.
   * @param individualId
   * @return
   */
  public ArrayList getHomeFolderList(int individualId) throws RemoteException;

  /**
   * Adds a rule (and the rule criteria) to the database. The new
   * ruleID is returned. If the rule is not created/saved, -1 will be
   * returned.
   * @param individualID The individualID of the user who is creating this rule.
   * @param ruleVO The new RuleVO to add to the database.
   * @return The new RuleID.
   */
  public int addRule(int individualID, RuleVO ruleVO) throws RemoteException;

  /**
   * Returns a fully populated RuleVO object based on the given ruleID.
   * If the rule is not found, a RuleVO with a RuleID of -1 will be returned.
   * @param individualID The individualID of the user asking for this rule.
   * @param ruleID The ID of the Rule we are looking for.
   * @return A fully populated RuleVO.
   */
  public RuleVO getRule(int ruleID) throws RemoteException;

  /**
   * Deletes an existing rule (and the rule criteria)
   * from the database based on the ruleID.
   * The number of rules deleted will be returned.
   * @param ruleID The ID of the Rule to be deleted.
   * @return The number of rules deleted. This
   * number should always be 0 or 1.
   */
  public int deleteRule(int ruleID) throws RemoteException;

  /**
   * Updates the emailrule.enabled field in the database, setting it to 'YES'.
   * @param individualID The individual who is changing the enabled status.
   * @param ruleID The ID of the rule whose status is to be changed.
   * @param status If true, the enabled field will be set to true, else will be set to false.
   * @return boolean - true for success, false for failure.
   */
  public boolean enableRule(int individualID, int ruleID, boolean status) throws RemoteException;

  /**
   * Updates an existing rule (and the rule criteria) in the database.
   * @param individualID The individualID of the user who is updating the rule.
   * @param ruleVO The new RuleVO to add to the database. The ruleID
   *        must be set on this object in order to update it.
   * @return int the number of rules updated (should always be 1 or 0)
   */
  public int editRule(int individualID, RuleVO ruleVO) throws RemoteException;

  /**
   * Gets and returns the Attachment Folder for a given user.
   * If this folder does not exist, a CVFolderVO with a FolderID
   * of <code>-1</code> will be returned.
   * @param individualID The User to find the attachment folder for.
   * @return A populated attachment folder CvFolderVO.
   */
  public CvFolderVO getAttachmentFolder(int individualID) throws RemoteException;


  /**
   * Sends a message. This method provides the basic functionality for sending a message.
   * @param individualID The ID of the Individual who is sending the message.
   * @param mailMessageVO The built mailMessageVO. This cannot be <code>null</code>.
   * @return true if the message was sent, false if it wasn't.
   * @throws SendFailedException The message could not be sent for whatever reason.
   *         Check the Exception for a detailed reason.
   * @throws MessagingException when processing the message some of method throws the MessagingException.   
   */
  public boolean simpleMessage(int individualID, MailMessageVO message) throws SendFailedException,MessagingException, RemoteException;

  /**
   * Gets a list of all the accounts which are marked as "Support"
   * accounts, and calls this.getMail() for each account.
   * @return void
   */
  public void checkAllSupportAccounts() throws RemoteException;

  /**
   * Deletes folder and any messages inside that folder.
   * @param individualID The individual deleting the folder.
   * @param accountID The account of the folder being deleted.
   * @param folderID The folder ID of the folder being deleted.
   * @return The number of folders deleted (should always be 0 or 1).
   */
  public int deleteFolder(int individualID, int accountID, int folderID) throws RemoteException;

  /**
   * Returns a ArrayList of the folderID available for an account.
   * The resulting ArrayList contains list of folderID.
   * @param accountID The AccountID to get the list of folders for.
   * @return A ArrayList with a list of FolderID as Key pairs.
   */
  public ArrayList getFolderIDs(int accountID) throws RemoteException;

  /**
   * Set the Private flag value to Message. Message IDS which are selected by the user
   * will be marked as private or public according to user's selection. 
   * 
   * @param individualID The individualID of the user asking for the list.
   * @param messageIDs Its a collection of messageID with comma seperated values.
   * @param privateType If yes then it will make the selected messageid to private or else public
   * @return
   */
  public boolean privateMessage(int individualID, String messageIDs, String privateType) throws RemoteException;

  /**
   * Returns an ArrayList of HashMap objects, each representing a
   * message in the given accountID's "Templates" folder.
   * @param individualID The individualID of the user requesting the Template list.
   * @param accountID The email accountID for which to return templates.
   * @return ArrayList of HashMaps representing Templates.
   */
  public ArrayList getTemplateList(int individualID, int accountID) throws RemoteException;
  
  /**
   * Returns a ValueListVO representing a list of email Message records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getEmailValueList(int individualID, ValueListParameters parameters) throws RemoteException;

  /**
   * Returns a ValueListVO representing a list of rule which are applied Message records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getRuleValueList(int individualID, ValueListParameters parameters) throws RemoteException;

} //end of Mail interface

