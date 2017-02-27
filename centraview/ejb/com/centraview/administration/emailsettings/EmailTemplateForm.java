/*
 * $RCSfile: EmailTemplateForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:44 $ - $Author: mking_cv $
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


/**
 * This class holds the values for the Email Template for System.
 *
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class EmailTemplateForm extends org.apache.struts.action.ActionForm
{

  /** The Primary Key of this message. */
  private int templateID;

  /** The name of the Email Template. */
  private String name;

  /** The description of the Email Template. */
  private String description;

  /** The To Address in the Email Template. */
  private String toAddress;

  /** The From Address in the Email Template. */
  private String fromAddress;

  /** The Reply To Address in the Email Template. */
  private String replyTo;

  /** The subject of the Email Template. */
  private String subject;

  /** The body of the Email Template. */
  private String body;

  /** The requiredToAddress in the Email Template. */
  private boolean requiredToAddress;

  /** The requiredFromAddress in the Email Template. */
  private boolean requiredFromAddress;

  /** The requiredReplyTo in the Email Template. */
  private boolean requiredReplyTo;

  /** The requiredSubject of the Email Template. */
  private boolean requiredSubject;

  /** The requiredBody of the Email Template. */
  private boolean requiredBody;

  /**
   * Returns the templateID.
   *
   * @return Returns the templateID.
   */
  public int getTemplateID()
  {
    return this.templateID;
  } //end of getTemplateID method

  /**
   * Sets the templateID.
   *
   * @param templateID The templateID to set.
   */
  public void setTemplateID(int templateID)
  {
    this.templateID = templateID;
  } //end of setTemplateID method

  /**
   * Returns the name.
   *
   * @return Returns the name.
   */
  public String getName()
  {
    return this.name;
  } //end of getName method

  /**
   * Sets the name.
   *
   * @param name The name to set.
   */
  public void setName(String name)
  {
    this.name = name;
  } //end of setName method


  /**
   * Returns the description.
   *
   * @return Returns the description.
   */
  public String getDescription()
  {
    return this.description;
  } //end of getDescription method

  /**
   * Sets the description.
   *
   * @param description The description to set.
   */
  public void setDescription(String description)
  {
    this.description = description;
  } //end of setDescription method

  /**
   * Returns the toAddress.
   *
   * @return Returns the toAddress.
   */
  public String getToAddress()
  {
    return this.toAddress;
  } //end of getToAddress method

  /**
   * Sets the toAddress.
   *
   * @param toAddress The toAddress to set.
   */
  public void setToAddress(String toAddress)
  {
    this.toAddress = toAddress;
  } //end of setToAddress method

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
   * Returns the requiredToAddress.
   *
   * @return Returns the requiredToAddress.
   */
  public boolean getRequiredToAddress()
  {
    return this.requiredToAddress;
  } //end of requiredToAddress method

  /**
   * Sets the requiredToAddress.
   *
   * @param requiredToAddress The requiredToAddress to set.
   */
  public void setRequiredToAddress(boolean requiredToAddress)
  {
    this.requiredToAddress = requiredToAddress;
  } //end of setRequiredToAddress method

  /**
   * Returns the requiredFromAddress.
   *
   * @return Returns the requiredFromAddress.
   */
  public boolean getRequiredFromAddress()
  {
    return this.requiredFromAddress;
  } //end of requiredFromAddress method

  /**
   * Sets the requiredFromAddress.
   *
   * @param requiredFromAddress The requiredFromAddress to set.
   */
  public void setRequiredFromAddress(boolean requiredFromAddress)
  {
    this.requiredFromAddress = requiredFromAddress;
  } //end of setRequiredFromAddress method


  /**
   * Returns the requiredReplyTo.
   *
   * @return Returns the requiredReplyTo.
   */
  public boolean getRequiredReplyTo()
  {
    return this.requiredReplyTo;
  } //end of requiredReplyTo method

  /**
   * Sets the requiredReplyTo.
   *
   * @param requiredReplyTo The requiredReplyTo to set.
   */
  public void setRequiredReplyTo(boolean requiredReplyTo)
  {
    this.requiredReplyTo = requiredReplyTo;
  } //end of setRequiredReplyTo method

  /**
   * Returns the requiredSubject.
   *
   * @return Returns the requiredSubject.
   */
  public boolean getRequiredSubject()
  {
    return this.requiredSubject;
  } //end of requiredSubject method

  /**
   * Sets the requiredSubject.
   *
   * @param requiredSubject The requiredSubject to set.
   */
  public void setRequiredSubject(boolean requiredSubject)
  {
    this.requiredSubject = requiredSubject;
  } //end of setRequiredSubject method

  /**
   * Returns the requiredBody.
   *
   * @return Returns the requiredBody.
   */
  public boolean getRequiredBody()
  {
    return this.requiredBody;
  } //end of requiredBody method

  /**
   * Sets the requiredBody.
   *
   * @param requiredBody The requiredBody to set.
   */
  public void setRequiredBody(boolean requiredBody)
  {
    this.requiredBody = requiredBody;
  } //end of setRequiredBody method

  public String toString()
  {
    StringBuffer sb = new StringBuffer("EmailTEmplateForm = \n");
    sb.append("    templateID = [" + templateID + "]\n");
    sb.append("    name = [" + name + "]\n");
    sb.append("    description = [" + description + "]\n");
    sb.append("    toAddress = [" + toAddress + "]\n");
    sb.append("    fromAddress = [" + fromAddress + "]\n");
    sb.append("    replyTo = [" + replyTo + "]\n");
    sb.append("    subject = [" + subject + "]\n");
    sb.append("    body = [" + body + "]\n");
    sb.append("    requiredToAddress = [" + requiredToAddress + "]\n");
    sb.append("    requiredFromAddress = [" + requiredFromAddress + "]\n");
    sb.append("    requiredReplyTo = [" + requiredReplyTo + "]\n");
    sb.append("    requiredSubject = [" + requiredSubject + "]\n");
    sb.append("    requiredBody = [" + requiredBody + "]\n");
    sb.append("\n\n");
    return(sb.toString());
  }// end of toString()

} //end of MailMessageVO class
