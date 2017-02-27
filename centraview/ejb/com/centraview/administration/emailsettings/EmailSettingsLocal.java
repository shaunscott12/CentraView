/*
 * $RCSfile: EmailSettingsLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:43 $ - $Author: mking_cv $
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

import javax.ejb.CreateException;
import javax.ejb.EJBLocalObject;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.naming.NamingException;

import com.centraview.mail.MailMessageVO;

/**
 * This interface is the remote interface
 * for the EmailSettings EJB. All of the
 * public available (remotely) methods are defined
 * in this interface.
 *
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public interface EmailSettingsLocal extends EJBLocalObject
{
  /**
   * Update the System Email Setting for the Application and return nothing.
   *
   * @param emailSettingsVO The Email Setting information such as
   * username, password, smtpserver, etc...
   */
	public void updateEmailSettings(EmailSettingsVO ssVO);

	/**
	 * Get the System Email Setting for the Application and return the SettingVO Object.
	 *
	 * @return emailSettingsVO The Email Setting information such as
	 * username, password, smtpserver, etc...
	 */
	public EmailSettingsVO getEmailSettings();

  /**
   * Update Email Template for particular Module. We should update the information
   * according to the template which is selected.
   * All the update permission are defined by the requiredcolumn they are (requiredtoAddress, requiredFromAddress ,etc.,)
   * on basis of the rights that textBox will be shown to the User. So we will set those Values.
   * other column will be update by the null value. It doen't return any nothing.
   *
   */
  public void updateEmailTemplate(EmailTemplateForm emailTemplateFrom);

  /**
   * Gets the associated Email Template information .
   * return the EmailTemplateForm Object according to the template Selected by user.
   *
   * @return emailTemplateFrom the EmailTemplateForm Object according to the template Selected by user.
	 *
   */
  public EmailTemplateForm getEmailTemplate(int emailTemplateID);

  /**
   * Sends a message. This method provides the basic functionality for sending a message.
   * @param individualID The ID of the Individual who is sending the message.
   * @param mailMessageVO The built mailMessageVO. This cannot be <code>null</code>.
   * @return true if the message was sent, false if it wasn't.
   * @throws SendFailedException The message could not be sent for whatever reason.
   *         Check the Exception for a detailed reason.
   * @throws NamingException There will be thrown when JNDI Name not bounded to the server.
   * @throws CreateException There will be thrown when we not able to create the EJB.
   * @throws MessagingException when processing the message some of method throws the MessagingException.
   */
  public boolean simpleMessage(int individualID, MailMessageVO mailMessageVO) throws SendFailedException,NamingException, CreateException,MessagingException;

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);
}// end of interface EmailSettingsLocal
