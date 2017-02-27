/*
 * $RCSfile: MailAccount.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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

/**
This class stores Accout Information
*/

public class MailAccount implements Serializable
{

   /** member variables of this class
   */
   private int accountID;
   private int owner;
   private String ownerName;
   private String name;
   private String login;
   private String password;
   private String smtpserver;
   private String mailserver;
   private String servertype;
   private String leaveonserver;
   private String address;
   private String replyto;
   private String signature;
   private String port;

   /**
  	* default constructor
    */
   public MailAccount()
   {

   }

   /**
   @return int
   returns account ID
   */
   public int getAccountID()
   {
    return accountID;
   }

   /**
   @param accountID
	sets account ID
    */
   public void setAccountID(int accountID)
   {
     this.accountID =  accountID ;
   }

   /**
   @return java.lang.String
   retuns email address
    */
   public String getAddress()
   {
    return address;
   }

   /**
   @param address
   sets email address
   */
   public void setAddress(String address)
   {
    this.address = address;
   }

   /**
   @return java.lang.String
   returns leaveon server
   */
   public String getLeaveonserver()
   {
    return leaveonserver;
   }

   /**
   @param leaveonserver
   sets leave on server
    */
   public void setLeaveonserver(String leaveonserver)
   {
    this.leaveonserver = leaveonserver;
   }

   /**
   @return java.lang.String
   returns login name
    */
   public String getLogin()
   {
    return login;
   }

   /**
   @param login
   sets login name
   */
   public void setLogin(String login)
   {
	 this.login = login ;
   }

   /**
   @return java.lang.String
   returns mail server ip
   */
   public String getMailserver()
   {
   	return this.mailserver;
   }

   /**
   @param mailserver
   sets mailserver ip
    */
   public void setMailserver(String mailserver)
   {
   	this.mailserver = mailserver;
   }

   /**
   @return java.lang.String
   retunrs name of user
   */
   public String getName()
   {
   	return this.name;
   }

   /**
   @param name
   sets name of user
   */
   public void setName(String name)
   {
   	this.name = name ;
   }

   /**
   @return int
   retunrs owner id as userid
   */
   public int getOwner()
   {
    return owner;
   }

   /**
   @param owner

    */
   public void setOwner(int owner)
   {
    this.owner =owner;
   }

   /**
   @return java.lang.String

    */
   public String getOwnerName()
   {
    return ownerName;
   }

   /**
   @param ownerName

    */
   public void setOwnerName(String ownerName)
   {
    this.ownerName = ownerName;
   }

   /**
   @return java.lang.String

    */
   public String getPassword()
   {
    return password;
   }

   /**
   @param password

    */
   public void setPassword(String password)
   {
	this.password = password;
   }

   /**
   @return java.lang.String

    */
   public String getReplyto()
   {
    return replyto;
   }

   /**
   @param replyto

    */
   public void setReplyto(String replyto)
   {
	  this.replyto = replyto ;
   }

   /**
   @return java.lang.String

    */
   public String getServertype()
   {
    return this.servertype;
   }

   /**
   @param servertype

    */
   public void setServertype(String servertype)
   {
    this.servertype = servertype;
   }

   /**
   @return java.lang.String

    */
   public String getSignature()
   {
    return signature;
   }

   /**
   @param signature

    */
   public void setSignature(String signature)
   {
    this.signature = signature;
   }

   /**
   @return java.lang.String

    */
   public String getSmtpserver()
   {
    return smtpserver;
   }

   /**
   @param smtpserver

    */
   public void setSmtpserver(String smtpserver)
   {
    this.smtpserver = smtpserver;
   }
   /**
   @return java.lang.String

    */
   public String getPort()
   {
    return this.port;
   }

   /**
   @param port

    */
   public void setPort(String port)
   {
    this.port = port;
   }

}
