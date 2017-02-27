/*
 * $RCSfile: EmailSettingsEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:43 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.mail.MailLocal;
import com.centraview.mail.MailLocalHome;
import com.centraview.mail.MailMessageVO;

/**
 * This EJB handles all Database related
 * issues with the EmailSettings client in CentraView.
 *
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class EmailSettingsEJB implements SessionBean
{
  protected SessionContext ctx;
  private String dataSource = "MySqlDS";
  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  /**
   * The required ejbCreate() method.
   * @throws CreateException An instance of the EJB could not
   * be created.
   */
  public void ejbCreate()
  {
    //Not Implemented
  } //end of ejbActivate method

  /**
   * A container invokes this method before it ends the
   * life of the session object. This happens as a result
   * of a client's invoking a remove operation, or when a
   * container decides to terminate the session object after
   * a timeout. <p>
   * This method is called with no transaction context.
   * @throws EJBException Thrown by the method to indicate a
   * failure caused by a system-level error.
   * @throws RemoteException This exception is defined in the
   * method signature to provide backward compatibility
   * for applications written for the EJB 1.0 specification.
   * Enterprise beans written for the EJB 1.1 specification
   * should throw the javax.ejb.EJBException instead of this
   * exception. Enterprise beans written for the EJB2.0 and
   * higher specifications must throw the javax.ejb.EJBException
   * instead of this exception.
   */
  public void ejbRemove()
  {
    //Not Implemented
  } //end of ejbActivate method


	/**
	* The activate method is called when the instance is
	* activated from its "passive" state. The instance
	* should acquire any resource that it has released
	* earlier in the ejbPassivate() method. <p>
	* This method is called with no transaction context.
	* @throws EJBException Thrown by the method to indicate a
	* failure caused by a system-level error.
	* @throws RemoteException This exception is defined in the
	* method signature to provide backward compatibility
	* for applications written for the EJB 1.0 specification.
	* Enterprise beans written for the EJB 1.1 specification
	* should throw the javax.ejb.EJBException instead of this
	* exception. Enterprise beans written for the EJB2.0 and
	* higher specifications must throw the javax.ejb.EJBException
	* instead of this exception.
	*/
	public void ejbActivate()
	{
		//Not Implemented
	} //end of ejbActivate method

  /**
   * The passivate method is called before the instance
   * enters the "passive" state. The instance should
   * release any resources that it can re-acquire later
   * in the ejbActivate() method. <p>
   * After the passivate method completes, the instance
   * must be in a state that allows the container to use the
   * Java Serialization protocol to externalize and store
   * away the instance's state. <p>
   * This method is called with no transaction context.
   * @throws EJBException Thrown by the method to indicate a
   * failure caused by a system-level error.
   * @throws RemoteException This exception is defined in the
   * method signature to provide backward compatibility
   * for applications written for the EJB 1.0 specification.
   * Enterprise beans written for the EJB 1.1 specification
   * should throw the javax.ejb.EJBException instead of this
   * exception. Enterprise beans written for the EJB2.0 and
   * higher specifications must throw the javax.ejb.EJBException
   * instead of this exception.
   */
  public void ejbPassivate()
  {
    //Not Implemented
  } //end of ejbActivate method


  /**
   * Update the System Email Setting and just return.
   *
   * @param emailSettingsVO The Email Setting information such as
   * username, password, smtpserver, etc...
   */
  public void updateEmailSettings(EmailSettingsVO emailSettingsVO)
  {
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQuery("update `emailsettings` set smtpserver = ? , username = ? , password = ?, authentication = ? , smtpport = ?");
      dl.setString(1, emailSettingsVO.getSmtpServer());
      dl.setString(2, emailSettingsVO.getUsername());
      dl.setString(3, emailSettingsVO.getPassword());
      if (emailSettingsVO.getAuthentication())
      {
        dl.setString(4, "YES");
      } // end of if (emailSettingsVO.getAuthentication())
      else {
        dl.setString(4, "NO");
      }// end of else block
      dl.setString(5, String.valueOf(emailSettingsVO.getSmtpPort()));
      dl.executeUpdate();
    }// end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][EmailSettingsEJB.updateEmailSettings] Exception Thrown: " + e);
      e.printStackTrace();
    }// end of catch Block
    finally
    {
      dl.destroy();
      dl = null;
    }// end of finally block
  } // end public void updateEmailSettings(EmailSettingsVO emailSettingsVO)

  /**
   * Get the System Email Setting for the Application and return the SettingVO Object.
   *
   * @return emailSettingsVO The Email Setting information such as
   * username, password, smtpserver, etc...
   */
  public EmailSettingsVO getEmailSettings()
  {
    EmailSettingsVO emailSettingsVO = new EmailSettingsVO();
    CVDal dl = null;
    try
    {
      dl = new CVDal(dataSource);
      dl.setSqlQuery("select * from `emailsettings`");
      Collection col = dl.executeQuery();
      if (col != null){
				Iterator it = col.iterator();
				HashMap hm = null;
				if (it.hasNext()){
					hm = (HashMap)it.next();
				}// end of if (it.hasNext())
				if (hm != null){
					emailSettingsVO.setSmtpPort(((Number)hm.get("smtpport")).intValue());
					emailSettingsVO.setSmtpServer((String)hm.get("smtpserver"));
					emailSettingsVO.setUsername((String)hm.get("username"));
					emailSettingsVO.setPassword((String)hm.get("password"));
					String authenticationString = (String)hm.get("authentication");
					boolean authentication = (authenticationString == null) ? false : authenticationString.equalsIgnoreCase("YES");
					emailSettingsVO.setAuthentication(authentication);
				}// end of if (hm != null)
				ArrayList emailTemplateList = this.getEmailTemplateList();
				emailSettingsVO.setEmailTemplateList(emailTemplateList);
			}// end of if (col != null)
    }// end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][EmailSettingsEJB.getEmailSettings] Exception Thrown: " + e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.destroy();
      dl = null;
    }// end of finally block
    return emailSettingsVO;
  }// end of public EmailSettingsVOX getEmailSettings()

  /**
   * Gets all the list of all the System Email Template.
   *
   * @return emailTemplateList The collection of the emailTemplate in a form of HashMap,
   * Intern HashMap Contains the information of the templateID, Name, Description.
   */
  private ArrayList getEmailTemplateList()
  {
    ArrayList emailTemplateList = new ArrayList();
    CVDal dl = null;
    try
    {
      dl = new CVDal(dataSource);
      dl.setSqlQuery("select templateID,name,description from emailtemplate");
      Collection col = dl.executeQuery();
      if (col != null){
				Iterator it = col.iterator();
				HashMap hm = null;
				while (it.hasNext()){
					hm = (HashMap)it.next();
					emailTemplateList.add(hm);
				}// end of if (it.hasNext())
			}// end of if (col != null)
    }// end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][EmailSettingsEJB.getEmailTemplateList] Exception Thrown: " + e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.destroy();
      dl = null;
    }// end of finally block
    return emailTemplateList;
	}// end of method getEmailTemplateList()

  /**
   * Gets the associated Email Template information .
   * return the EmailTemplateForm Object according to the template Selected by user.
   *
   * @return emailTemplateFrom the EmailTemplateForm Object according to the template Selected by user.
	 *
   */
  public EmailTemplateForm getEmailTemplate(int emailTemplateID)
  {
    EmailTemplateForm emailTemplateFrom = new EmailTemplateForm();
    CVDal dl = null;
    try
    {
      dl = new CVDal(dataSource);
      dl.setSqlQuery("select * from emailtemplate where templateID="+emailTemplateID);
      Collection col = dl.executeQuery();
      if (col != null){
				Iterator it = col.iterator();
				HashMap hm = null;
				if (it.hasNext()){
					hm = (HashMap)it.next();

					emailTemplateFrom.setTemplateID(((Number)hm.get("templateID")).intValue());
					String name = (String)hm.get("name");
					if ( name == null){
						name = "";
					}// end of if ( name == null)
					emailTemplateFrom.setName(name);

					String description = (String)hm.get("description");
					if ( description == null){
						description = "";
					}// end of if ( description == null)
					emailTemplateFrom.setDescription(description);

					String toAddress = (String)hm.get("toAddress");
					if ( toAddress == null){
						toAddress = "";
					}// end of if ( toAddress == null)
					emailTemplateFrom.setToAddress(toAddress);

					String fromAddress = (String)hm.get("fromAddress");
					if ( fromAddress == null){
						fromAddress = "";
					}// end of if ( fromAddress == null)
					emailTemplateFrom.setFromAddress(fromAddress);

					String replyTo = (String)hm.get("replyTo");
					if ( replyTo == null){
						replyTo = "";
					}// end of if ( replyTo == null)
					emailTemplateFrom.setReplyTo(replyTo);

					String subject = (String)hm.get("subject");
					if ( subject == null){
						subject = "";
					}// end of if ( subject == null)
					emailTemplateFrom.setSubject(subject);

					String body = (String)hm.get("body");
					if ( body == null){
						body = "";
					}// end of if ( body == null)
					emailTemplateFrom.setBody(body);

					String requiredToAddressString = (String)hm.get("requiredToAddress");
					boolean requiredToAddress = (requiredToAddressString == null) ? false : requiredToAddressString.equalsIgnoreCase("YES");

					String requiredFromAddressString = (String)hm.get("requiredFromAddress");
					boolean requiredFromAddress = (requiredFromAddressString == null) ? false : requiredFromAddressString.equalsIgnoreCase("YES");

					String requiredReplyToString = (String)hm.get("requiredReplyTo");
					boolean requiredReplyTo = (requiredReplyToString == null) ? false : requiredReplyToString.equalsIgnoreCase("YES");

					String requiredSubjectString = (String)hm.get("requiredSubject");
					boolean requiredSubject = (requiredSubjectString == null) ? false : requiredSubjectString.equalsIgnoreCase("YES");

					String requiredBodyString = (String)hm.get("requiredBody");
					boolean requiredBody = (requiredBodyString == null) ? false : requiredBodyString.equalsIgnoreCase("YES");

					emailTemplateFrom.setRequiredToAddress(requiredToAddress);
					emailTemplateFrom.setRequiredFromAddress(requiredFromAddress);
					emailTemplateFrom.setRequiredReplyTo(requiredReplyTo);
					emailTemplateFrom.setRequiredSubject(requiredSubject);
					emailTemplateFrom.setRequiredBody(requiredBody);

				}// end of if (it.hasNext())
			}// end of if (col != null)
    }// end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][EmailSettingsEJB.getEmailTemplate] Exception Thrown: " + e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.destroy();
      dl = null;
    }// end of finally block
    return emailTemplateFrom;
	}// end of method getEmailTemplate()


  /**
   * Update Email Template for particular Module. We should update the information
   * according to the template which is selected.
   * All the update permission are defined by the requiredcolumn they are (requiredtoAddress, requiredFromAddress ,etc.,)
   * on basis of the rights that textBox will be shown to the User. So we will set those Values.
   * other column will be update by the null value. It doen't return any nothing.
   *
   */
	public void updateEmailTemplate(EmailTemplateForm emailTemplateFrom)
  {
    CVDal dl = new CVDal(dataSource);
    try
    {
			dl.setSqlQuery("update `emailtemplate` set name = ? ,description = ? ,toAddress = ? , "+
				 "fromAddress = ? ,replyTo = ? ,subject = ? ,body = ? where templateID = ? ");

			dl.setString(1,emailTemplateFrom.getName());
			dl.setString(2,emailTemplateFrom.getDescription());
			dl.setString(3,emailTemplateFrom.getToAddress());
			dl.setString(4,emailTemplateFrom.getFromAddress());
			dl.setString(5,emailTemplateFrom.getReplyTo());
			dl.setString(6,emailTemplateFrom.getSubject());
			dl.setString(7,emailTemplateFrom.getBody());
			dl.setInt(8,emailTemplateFrom.getTemplateID());
			dl.executeUpdate();
    }// end of try block
    catch (Exception e)
    {
      System.out.println("[Exception][EmailSettingsEJB.updateEmailTemplate] Exception Thrown: " + e);
      e.printStackTrace();
    }// end of catch Block
    finally
    {
      dl.destroy();
      dl = null;
    }// end of finally block
  } // end public void updateEmailSettings(EmailSettingsVOX emailTemplateFrom)


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
   * 
   */
  public boolean simpleMessage(int individualID, MailMessageVO mailMessageVO) throws NamingException,CreateException, SendFailedException, MessagingException
  {
			InitialContext ic = CVUtility.getInitialContext();
			MailLocalHome home = (MailLocalHome)ic.lookup("local/Mail");
			MailLocal remote = (MailLocal)home.create();
			remote.setDataSource(dataSource);
			boolean sendFlag = remote.simpleMessage(individualID,mailMessageVO);
			return sendFlag;
  } // end public

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }// end of setDataSource(String ds)

}// end of class EmailSettingsEJB