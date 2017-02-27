/*
 * $RCSfile: MailMessageVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:37 $ - $Author: mking_cv $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.centraview.file.CvFileVO;

/**
 * This class holds the values for the mail
 * messages. 
 * 
 * @author Ryan Grier <ryan@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class MailMessageVO implements Serializable
{
  /** Plain/Text Message Type. */
  public final static String PLAIN_TEXT_TYPE = "text/plain";
  
  /** HTML/Text Message Type. */
  public final static String HTML_TEXT_TYPE = "text/html";

  /** The Primary Key of this message. */
  private int messageID;
  
  /** The Email Account ID of this message. */
  private int emailAccountID;
  
  /** The Message UID. */ 
  private String messageUID;
  
  /** The Email Folder ID of this message. */
  private int emailFolderID;
  
  /** The date the message was recieved. */
  private Date receivedDate;
  
  /** The From Address in the Email Message. */
  private String fromAddress;
  
  /** The Reply To Address in the Email Message. */
  private String replyTo;
  
  /** The subject of the email message. */
  private String subject;
  
  /** The body of the email message. */
  private String body;
  
  /** The headers of the email message. Newline seperated. */
  private String headers;
  
  /** The ownerID of this email message. */
  private int ownerID;
  
  /** The individual ID of the sender of this email message (if known). */
  private int fromIndividualID;
  
  /** The size of the email message. */
  private int size;
  
  /** The priority of this email message. */
  private String priority;
  
  /** The Importance of this email message. */
  private String importance;
  
  /** A Collection of files attached to this email message. */
  private Collection attachedFiles;
  
  /** Whether or not the message has been read. */
  private boolean messageRead;
  
  /** Whether or not the message has been locally modified. */
  private boolean locallyModified;
  
  /** Whether or not the message has been locally deleted. */
  private boolean locallyDeleted;
  
  /** Whether or not to move this message to another folder. 0 if not moving.*/
  private int moveToFolder;
  
  /** Whether or not to copy this message to another folder. 0 if not copying.*/
  private int copyToFolder;
  
  /** The content type of the message. */
  private String contentType;
  
  /** A Collection of to address Strings. */
  private Collection toList;
  
  /** A Collection of cc address Strings. */
  private Collection ccList;
  
  /** A Collection of bcc address Strings. */  
  private Collection bccList;

  /** A String of privateMessage holds Yes Or no. */  
  private String privateMessage;
  
  /** Default Constructor. */
  public MailMessageVO()
  {
    this.attachedFiles = new ArrayList();
    this.toList = new ArrayList();
    this.ccList = new ArrayList();
    this.bccList = new ArrayList();
  } //end of MailMessageVO constructor
  
  /**
   * Returns the copyToFolder.
   * 
   * @return Returns the copyToFolder.
   */
  public int getCopyToFolder()
  {
    return this.copyToFolder;
  } //end of getCopyToFolder method

  /**
   * Sets the copyToFolder.
   * 
   * @param copyToFolder The copyToFolder to set.
   */
  public void setCopyToFolder(int copyToFolder)
  {
    this.copyToFolder = copyToFolder;
  } //end of setCopyToFolder method

  /**
   * Returns the moveToFolder.
   * 
   * @return Returns the moveToFolder.
   */
  public int getMoveToFolder()
  {
    return this.moveToFolder;
  } //end of getMoveToFolder method

  /**
   * Sets the moveToFolder.
   * 
   * @param moveToFolder The moveToFolder to set.
   */
  public void setMoveToFolder(int moveToFolder)
  {
    this.moveToFolder = moveToFolder;
  } //end of setMoveToFolder method
    
  /**
   * Returns A Collection of CvFileVO objects.
   * 
   * @return Returns the attachedFiles.
   */
  public Collection getAttachedFiles()
  {
    if (attachedFiles == null)
    {
      attachedFiles = new ArrayList();
    } //end of if statement (attachedFiles == null)
    return this.attachedFiles;
  } //end of getAttachedFiles method

  /**
   * Sets the attachedFiles.
   * 
   * @param attachedFiles The attachedFiles to set.
   */
  public void setAttachedFiles(Collection attachedFiles)
  {
    this.attachedFiles = attachedFiles;
  } //end of setAttachedFiles method
  
  /**
   * Adds a new CvFileVO to the Collection of attachments.
   * @param attachedFiles The attachedFiles to set.
   */
  public void addAttachedFiles(CvFileVO fileAttachment)
  {
    if (attachedFiles == null)
    {
      attachedFiles = new ArrayList();
    } //end of if statement (attachedFiles == null)
    this.attachedFiles.add(fileAttachment);
  } //end of addAttachedFiles method

  /**
   * Returns the bccList.
   * 
   * @return Returns the bccList.
   */
  public Collection getBccList()
  {
    if (bccList == null)
    {
      bccList = new ArrayList();
    } //end of if statement (bccList == null)
    return this.bccList;
  } //end of getBccList method
  
  /**
   * Sets the bccList.
   * 
   * @param bccList The bccList to set.
   */
  public void setBccList(Collection bccList)
  {
    this.bccList = bccList;
  } //end of setBccList method
  
  /**
   * Adds a new BCC address to the Collection of BCC addresses.
   * If the BCC address already exists, it will not be added.
   * 
   * @param bccAddress The BCC Address String to add.
   */
  public void addBccAddress(String bccAddress)
  {
    if (bccList == null)
    {
      bccList = new ArrayList();
    } //end of if statement (bccList == null)
    if (!bccList.contains(bccAddress))
    {
      this.bccList.add(bccAddress);
    } //end of if statement (!bccList.contains(bccAddress)) 
  } //end of addBccAddress method
  
  /**
   * Returns the ccList.
   * 
   * @return Returns the ccList.
   */
  public Collection getCcList()
  {
    if (ccList == null)
    {
      ccList = new ArrayList();
    } //end of if statement (ccList == null)
    return this.ccList;
  } //end of getCcList method
  
  /**
   * Sets the ccList.
   * 
   * @param ccList The ccList to set.
   */
  public void setCcList(Collection ccList)
  {
    this.ccList = ccList;
  } //end of setCcList method
  
  /**
   * Adds a new CC address to the Collection of CC addresses.
   * If the CC address already exists, it will not be added.
   * 
   * @param ccAddress The CC Address String to add.
   */
  public void addCcAddress(String ccAddress)
  {
    if (ccList == null)
    {
      ccList = new ArrayList();
    } //end of if statement (ccList == null)
    if (!ccList.contains(ccAddress))
    {
      this.ccList.add(ccAddress);
    } //end of if statement (!ccList.contains(ccAddress)) 
  } //end of addCcAddress method
  
  /**
   * Returns the toList.
   * 
   * @return Returns the toList.
   */
  public Collection getToList()
  {
    if (toList == null)
    {
      toList = new ArrayList();
    } //end of if statement (toList == null)
    return this.toList;
  } //end of getToList method
  
  /**
   * Sets the toList.
   * 
   * @param toList The toList to set.
   */
  public void setToList(Collection toList)
  {
    this.toList = toList;
  } //end of setToList method
  
  /**
   * Adds a new TO address to the Collection of TO addresses.
   * If the TO address already exists, it will not be added.
   * 
   * @param toAddress The TO Address String to add.
   */
  public void addToAddress(String toAddress)
  {
    if (toList == null)
    {
      toList = new ArrayList();
    } //end of if statement (toList == null)
    if (!toList.contains(toAddress))
    {
      this.toList.add(toAddress);
    } //end of if statement (!toList.contains(toAddress)) 
  } //end of addToAddress method
  
  /**
   * Returns the body.
   * 
   * @return Returns the body.
   */
  public String getBody()
  {
    return this.body;
  } //end of getBody method

  /**
   * Sets the body.
   * 
   * @param body The body to set.
   */
  public void setBody(String body)
  {
    this.body = body;
  } //end of setBody method

  /**
   * Returns the emailAccountID.
   * 
   * @return Returns the emailAccountID.
   */
  public int getEmailAccountID()
  {
    return this.emailAccountID;
  } //end of getEmailAccountID method

  /**
   * Sets the emailAccountID.
   * 
   * @param emailAccountID The emailAccountID to set.
   */
  public void setEmailAccountID(int emailAccountID)
  {
    this.emailAccountID = emailAccountID;
  } //end of setEmailAccountID method
  
  /**
   * Returns the emailFolderID.
   * 
   * @return Returns the emailFolderID.
   */
  public int getEmailFolderID()
  {
    return this.emailFolderID;
  } //end of getEmailFolderID method

  /**
   * Sets the emailFolderID.
   * 
   * @param emailFolderID The emailFolderID to set.
   */
  public void setEmailFolderID(int emailFolderID)
  {
    this.emailFolderID = emailFolderID;
  } //end of setEmailFolderID method

  /**
   * Returns the fromAddress.
   * 
   * @return Returns the fromAddress.
   */
  public String getFromAddress()
  {
    return this.fromAddress;
  } //end of getFromAddress method

  /**
   * Sets the fromAddress.
   * 
   * @param fromAddress The fromAddress to set.
   */
  public void setFromAddress(String fromAddress)
  {
    this.fromAddress = fromAddress;
  } //end of setFromAddress method

  /**
   * Returns the fromIndividualID.
   * 
   * @return Returns the fromIndividualID.
   */
  public int getFromIndividualID()
  {
    return this.fromIndividualID;
  } //end of getFromIndividualID method

  /**
   * Sets the fromIndividualID.
   * 
   * @param fromIndividualID The fromIndividualID to set.
   */
  public void setFromIndividualID(int fromIndividualID)
  {
    this.fromIndividualID = fromIndividualID;
  } //end of setFromIndividualID method

  /**
   * Returns the headers.
   * 
   * @return Returns the headers.
   */
  public String getHeaders()
  {
    return this.headers;
  } //end of getHeaders method

  /**
   * Sets the headers.
   * 
   * @param headers The headers to set.
   */
  public void setHeaders(String headers)
  {
    this.headers = headers;
  } //end of setHeaders method

  /**
   * Returns the importance.
   * 
   * @return Returns the importance.
   */
  public String getImportance()
  {
    return this.importance;
  } //end of getImportance method

  /**
   * Sets the importance.
   * 
   * @param importance The importance to set.
   */
  public void setImportance(String importance)
  {
    this.importance = importance;
  } //end of setImportance method

  /**
   * Returns the locallyDeleted.
   * 
   * @return Returns the locallyDeleted.
   */
  public boolean isLocallyDeleted()
  {
    return this.locallyDeleted;
  } //end of isLocallyDeleted method

  /**
   * Sets the locallyDeleted.
   * 
   * @param locallyDeleted The locallyDeleted to set.
   */
  public void setLocallyDeleted(boolean locallyDeleted)
  {
    this.locallyDeleted = locallyDeleted;
  } //end of setLocallyDeleted method

  /**
   * Returns the locallyModified.
   * 
   * @return Returns the locallyModified.
   */
  public boolean isLocallyModified()
  {
    return this.locallyModified;
  } //end of isLocallyModified method

  /**
   * Sets the locallyModified.
   * 
   * @param locallyModified The locallyModified to set.
   */
  public void setLocallyModified(boolean locallyModified)
  {
    this.locallyModified = locallyModified;
  } //end of setLocallyModified method

  /**
   * Returns the messageID.
   * 
   * @return Returns the messageID.
   */
  public int getMessageID()
  {
    return this.messageID;
  } //end of getMessageID method

  /**
   * Sets the messageID.
   * 
   * @param messageID The messageID to set.
   */
  public void setMessageID(int messageID)
  {
    this.messageID = messageID;
  } //end of setMessageID method

  /**
   * Returns the messageRead.
   * 
   * @return Returns the messageRead.
   */
  public boolean isMessageRead()
  {
    return this.messageRead;
  } //end of isMessageRead method

  /**
   * Sets the messageRead.
   * 
   * @param messageRead The messageRead to set.
   */
  public void setMessageRead(boolean messageRead)
  {
    this.messageRead = messageRead;
  } //end of setMessageRead method

  /**
   * Returns the messageUID.
   * 
   * @return Returns the messageUID.
   */
  public String getMessageUID()
  {
    return this.messageUID;
  } //end of getMessageUID method

  /**
   * Sets the messageUID.
   * 
   * @param messageUID The messageUID to set.
   */
  public void setMessageUID(String messageUID)
  {
    this.messageUID = messageUID;
  } //end of setMessageUID method

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
   * Returns the priority.
   * 
   * @return Returns the priority.
   */
  public String getPriority()
  {
    return this.priority;
  } //end of getPriority method

  /**
   * Sets the priority.
   * 
   * @param priority The priority to set.
   */
  public void setPriority(String priority)
  {
    this.priority = priority;
  } //end of setPriority method

  /**
   * Returns the receivedDate.
   * 
   * @return Returns the receivedDate.
   */
  public Date getReceivedDate()
  {
    return this.receivedDate;
  } //end of getReceivedDate method

  /**
   * Sets the receivedDate.
   * 
   * @param receivedDate The receivedDate to set.
   */
  public void setReceivedDate(Date receivedDate)
  {
    this.receivedDate = receivedDate;
  } //end of setReceivedDate method

  /**
   * Returns the replyTo.
   * 
   * @return Returns the replyTo.
   */
  public String getReplyTo()
  {
    return this.replyTo;
  } //end of getReplyTo method

  /**
   * Sets the replyTo.
   * 
   * @param replyTo The replyTo to set.
   */
  public void setReplyTo(String replyTo)
  {
    this.replyTo = replyTo;
  } //end of setReplyTo method

  /**
   * Returns the size.
   * 
   * @return Returns the size.
   */
  public int getSize()
  {
    return this.size;
  } //end of getSize method

  /**
   * Sets the size.
   * 
   * @param size The size to set.
   */
  public void setSize(int size)
  {
    this.size = size;
  } //end of setSize method

  /**
   * Returns the subject.
   * 
   * @return Returns the subject.
   */
  public String getSubject()
  {
    return this.subject;
  } //end of getSubject method

  /**
   * Sets the subject.
   * 
   * @param subject The subject to set.
   */
  public void setSubject(String subject)
  {
    this.subject = subject;
  } //end of setSubject method
  
  /**
   * Returns the contentType.
   * 
   * @return Returns the contentType.
   */
  public String getContentType()
  {
    return this.contentType;
  } //end of getContentType method
  
  /**
   * Sets the contentType.
   * 
   * @param contentType The contentType to set.
   */
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  } //end of setContentType method


  public String toString()
  {
    StringBuffer sb = new StringBuffer("MailMessageVO = \n");
    sb.append("    messageID = [" + messageID + "]\n");
    sb.append("    emailAccountID = [" + emailAccountID + "]\n");
    sb.append("    messageUID = [" + messageUID + "]\n");
    sb.append("    emailFolderID = [" + emailFolderID + "]\n");
    sb.append("    receivedDate = [" + receivedDate + "]\n");
    sb.append("    fromAddress = [" + fromAddress + "]\n");
    sb.append("    replyTo = [" + replyTo + "]\n");
    sb.append("    subject = [" + subject + "]\n");
    sb.append("    body = [" + body + "]\n");
    sb.append("    headers = [" + headers + "]\n");
    sb.append("    ownerID = [" + ownerID + "]\n");
    sb.append("    fromIndividualID = [" + fromIndividualID + "]\n");
    sb.append("    size = [" + size + "]\n");
    sb.append("    priority = [" + priority + "]\n");
    sb.append("    importance = [" + importance + "]\n");
    sb.append("    attachedFiles = [" + attachedFiles + "]\n");
    sb.append("    messageRead = [" + messageRead + "]\n");
    sb.append("    locallyModified = [" + locallyModified + "]\n");
    sb.append("    locallyDeleted = [" + locallyDeleted + "]\n");
    sb.append("    moveToFolder = [" + moveToFolder + "]\n");
    sb.append("    copyToFolder = [" + copyToFolder + "]\n");
    sb.append("    contentType = [" + contentType + "]\n");
    sb.append("    toList = [" + toList + "]\n");
    sb.append("    ccList = [" + ccList + "]\n");
    sb.append("    bccList = [" + bccList + "]\n");
    sb.append("    privateMessage = [" + privateMessage + "]\n");
    sb.append("\n\n");
    return(sb.toString());
  }

  /**
   * Returns the privateMessage.
   * 
   * @return Returns the privateMessage.
   */
  public String getPrivate()
  {
    return this.privateMessage;
  } //end of getPrivate() method

  /**
   * Sets the privateMessage.
   * 
   * @param privateMessage The privateMessage to set.
   */
  public void setPrivate(String privateMessage)
  {
    this.privateMessage = privateMessage;
  } //end of setPrivate method
 
} //end of MailMessageVO class
