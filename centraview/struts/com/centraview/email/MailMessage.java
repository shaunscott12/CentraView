/*
 * $RCSfile: MailMessage.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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
package com.centraview.email;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;




public class MailMessage implements Serializable
{

   /**
   Members of this class
   */
   private int messageID;
   private Timestamp messageDate;
   private int fromIndividual;
   private String subject;
   private String headers;

   // IQ added to set multiple headers as collection
   // required for activity send email, adding individual from email
   // and other such kind of services
   // added new method so as to not to disturb the currently working headers functionality
   HashMap headersHM = null;
   // IQ end

   private String body;

   private ArrayList attachmentID;

   private ArrayList to	;
   private String contenttype = "";
   private ArrayList cc;
   private ArrayList bcc;


   private int folder;
   private String status;
   private String importance;
   private String priority;
   private int accountID;
   public MailAccount theMailAccount;
   private String mailFrom;

   private HashMap attachfileids;

   private String smtpserver ;
   private String replyTo; //added by cwang to accomend need in sending emails by Market and Grop module.


   /**
	constructor
    */
   public MailMessage()
   {
		to = new ArrayList();
		cc = new ArrayList();
		bcc = new ArrayList();
		attachmentID = new ArrayList();
		attachfileids = new HashMap();
   }

   /**
   @return String
 	returs contenttype
    */
   public String getContentType()
   {
  		return contenttype ;
   }

   /**
   @param contenttype
 	sets account id
    */
   public void setContentType(String contenttype)
   {
    	this.contenttype = contenttype ;
   }


   /**
   @return int
 	returs account id
    */
   public int getAccountID()
   {
  		return accountID ;
   }

   /**
   @param accountID
 	sets account id
    */
   public void setAccountID(int accountID)
   {
    	this.accountID = accountID ;
   }

   /**
   @return int
   returns attachment id
    */
   public ArrayList getAttachmentID()
   {
    return attachmentID;
   }

   /**
   @param attachmentID
 	sets attachment id
    */
   public void setAttachmentID( ArrayList attachmentID)
   {
     this.attachmentID = attachmentID ;
   }



   /**
   @return java.lang.String
   reutns body of a message
   */
   public String getBody()
   {
   		return body;
   }

   /**
   @param body
   sets body for the message
   */
   public void setBody(String body)
   {
   		this.body = body ;
   }


   /**
   @return int
   returns  the folder id
   */
   public int getFolder()
   {
    return folder;
   }

   /**
   @param folder
	 sets the folder id
   */
   public void setFolder(int folder)
   {
    this.folder = folder;
   }

   /**
   @return int
    returns from individual ID
   */
   public int getFromIndividual()
   {
    return fromIndividual;
   }

   /**
   @param fromIndividual
    sets fromIndividual Id
   */
   public void setFromIndividual(int fromIndividual)
   {
    this.fromIndividual = fromIndividual;
   }

   /**
   @return java.lang.String

   */
   public String getHeaders()
   {
    return headers;
   }

   /**
   @param headers

   */
   public void setHeaders(String headers)
   {
    this.headers = headers;
   }

   /**
   @return java.lang.String

   */
   public String getImportance()
   {
    return importance;
   }

   /**
   @param importance

   */
   public void setImportance(String importance)
   {
    this.importance = importance;
   }

   /**
   @return Date

   */
   public Timestamp getMessageDate()
   {
    return messageDate;
   }

   /**
   @param messageDate

   */
   public void setMessageDate(Timestamp messageDate)
   {
   		this.messageDate = messageDate;
   }

   /**
   @return int

   */
   public int getMessageID()
   {
   		return messageID;
   }

   /**
   @param messageID

   */
   public void setMessageID(int messageID)
   {
   	 this.messageID = messageID;
   }

   /**
   @return java.lang.String

   */
   public String getPriority()
   {
    return priority;
   }

   /**
   @param priority

   */
   public void setPriority(String priority)
   {
     this.priority =priority ;
   }

   /**
   @return java.lang.String
   @roseuid 3F127B0E01DB
    */
   public String getStatus()
   {
    return status;
   }

   /**
   @param status
   @roseuid 3F127B0E0249
    */
   public void setStatus(String status)
   {
    this.status = status;
   }

   /**
   @return java.lang.String
   @roseuid 3F127B0E034D
    */
   public String getSubject()
   {
    return subject;
   }

   /**
   @param subject
   @roseuid 3F127B0E03BB
    */
   public void setSubject(String subject)
   {
	    this.subject = subject;
   }

   /**
   @return com.centraview.email.MailAccount
   @roseuid 3F127B0F00D8
    */
   public MailAccount getTheMailAccount()
   {
    return theMailAccount;
   }

   /**
   @param theMailAccount

   */
   public void setTheMailAccount( MailAccount theMailAccount)
   {
    this.theMailAccount = theMailAccount;
   }

   /**
   @param
  	returns  the mail from
   */

	public String getMailFrom()
	{
		return this.mailFrom;
	}

	/**
	@param
	sets the mail from
	*/
	public void setMailFrom(String mailFrom)
	{
		this.mailFrom = mailFrom;
	}

  /**
  @param
  returns  the mail bcc
  */
	public ArrayList getBcc()
	{
		return this.bcc;
	}

  /**
  @param
  sets the mail BCC
  */
	public void setBcc(ArrayList bcc)
	{
		this.bcc = bcc;
	}

	/**
   @param
  	reutns the mail cc
   */

	public ArrayList getCc()
	{
		return this.cc;
	}

   /**
   @param
   sets the mail cc
   */

	public void setCc(ArrayList cc)
	{
		this.cc = cc;
	}

	 /**
   @param
  	returns ArrayList of to
   */
	public ArrayList getTo()
	{
		return this.to;
	}

   /**
   @param
   sets the ArrayList of to
   */
	public void setTo(ArrayList to)
	{
		this.to = to;
	}

	public void setAttachFileIDs( HashMap hm )
	{
		attachfileids = hm ;
	}


	public HashMap getAttachFileIDs( )
	{
		return attachfileids  ;
	}




	public String getSmtpserver()
	{
		return this.smtpserver;
	}

	public void setSmtpserver(String smtpserver)
	{
		this.smtpserver = smtpserver;
	}


	// IQ added to set multiple headers as collection
	// required for activity send email, adding individual from email
	// and other such kind of services
	// added new method so as to not to disturb the currently working headers functionality
	public HashMap getHeadersHM()
	{
		return this.headersHM;
	}

	public void setHeadersHM(String headerName, String headerValue) throws Exception
	{
		if(headerName == null || headerName.length() == 0)
			throw new Exception("Header Name is not provided");

		if(headerValue == null || headerValue.length() == 0)
			throw new Exception("Header Value is not provided");

		if(this.headersHM == null)
			this.headersHM = new HashMap();

		this.headersHM.put(headerName,headerValue);
	}
	public String getReplyTo()
	{
		return replyTo;
	}
	public void setReplyTo(String replyTo)
	{
		this.replyTo = replyTo;
	}
	// IQ END
}
