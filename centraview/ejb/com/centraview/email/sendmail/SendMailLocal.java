/*
 * $RCSfile: SendMailLocal.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:30 $ - $Author: mking_cv $
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

 
package com.centraview.email.sendmail;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;

import com.centraview.email.MailMessage;

/**
 * The local interface for SendMailEJB
 */
public interface SendMailLocal extends EJBLocalObject
{
  /**
   * this method returns MailMessage
   */
  public boolean sendMailMessage(int userId, MailMessage mailmessage);
  
  /**
   * this method send forgotten password to user
   * @param mailmessage MailMessage
   * @author CentraView, LLC.
   */
  public void sendForgottenPasswordMail(MailMessage mailmessage);
  
  public void updateHeader(int userId, int emailId, String headerName, String headerValue) throws EJBException;
  
  /**
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public void setDataSource(String ds);

  /**
   * Sends an email that is generated from the given
   * MailMessage object. This method does not do anything
   * other than send the email. All code in CentraView
   * should call this method for the transmission of email.
   * Any information required to send a valid email is
   * required to be passed to this method via the mailmessage
   * parameter, including To:, From:, ReplyTo:, Subject:,
   * Body, Attachments, etc.
   * @param mailmessage The MailMessage
   * @return boolean True for success, False for failure
   */
  public boolean sendMailMessageBasic(MailMessage mailmessage);

}
