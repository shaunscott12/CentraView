/*
 * $RCSfile: SendMailEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.UserPrefererences;
import com.centraview.email.MailAddress;
import com.centraview.email.MailMessage;
import com.centraview.email.emailmanage.EmailManageLocal;
import com.centraview.email.emailmanage.EmailManageLocalHome;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.preference.PreferenceLocal;
import com.centraview.preference.PreferenceLocalHome;

/**
 * This class is a Stateless Session Bean which returns Data for each call
 *
 * @author   CentraView, LLC.
 */

public class SendMailEJB implements SessionBean
{

  /** The SessionContext interface for the instance. */
  private SessionContext sessionContext;

	private String dataSource = "MySqlDS";

  /**
   * Default constructor
   */
  public SendMailEJB()
  {
  } //end of SendMailEJB method

  /**
   * Called by the container to create a session bean instance. Its parameters
   * typically contain the information the client uses to customize the bean
   * instance for its use. It requires a matching pair in the bean class and its
   * home interface.
   */
  public void ejbCreate()
  {
  } //end of ejbCreate method

  /**
   * A container invokes this method before it ends the life of the session
   * object. This happens as a result of a client's invoking a remove operation,
   * or when a container decides to terminate the session object after a
   * timeout. This method is called with no transaction context.
   */
  public void ejbRemove()
  {
  } //end of ejbRemove method

  /**
   * The activate method is called when the instance is activated from its
   * 'passive' state. The instance should acquire any resource that it has
   * released earlier in the ejbPassivate() method. This method is called with
   * no transaction context.
   */
  public void ejbActivate()
  {
  } //end of ejbActivate method

  /**
   * The passivate method is called before the instance enters the 'passive'
   * state. The instance should release any resources that it can re-acquire
   * later in the ejbActivate() method. After the passivate method completes,
   * the instance must be in a state that allows the container to use the Java
   * Serialization protocol to externalize and store away the instance's state.
   * This method is called with no transaction context.
   */
  public void ejbPassivate()
  {
  } //end of ejbPassivate method

  /**
   * Set the associated session context. The container calls this method after the instance
   * creation. The enterprise Bean instance should store the reference to the context
   * object in an instance variable. This method is called with no transaction context.
   *
   * @param sessionContext  The new sessionContext value
   */
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  } //end of setSessionContext method


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
  public boolean sendMailMessageBasic(MailMessage mailmessage)
  {
    try
    {
      Properties props = System.getProperties();
      if (mailmessage.getSmtpserver() != null)
      {
        props.put("mail.smtp.host", mailmessage.getSmtpserver());
      }else{
        return(false);
      }

      Session session = Session.getDefaultInstance(props, null);

      MimeMessage message = new MimeMessage(session);

      ArrayList to = mailmessage.getTo();
      String arrayto[] = new String[to.size()];
      for (int i=0; i<arrayto.length; i++)
      {
        MailAddress ma = new MailAddress((String)to.get(i));
        arrayto[i] = ma.getAddress();
      }

      long date = (new java.util.Date()).getTime();
      java.sql.Timestamp messagedate = new java.sql.Timestamp(date);
      String subject = mailmessage.getSubject();
      String body = mailmessage.getBody();
      String from = mailmessage.getMailFrom();
      String replyTo = mailmessage.getReplyTo();

      if (replyTo != null && replyTo.length() >0)
      {
        InternetAddress[] ia = new InternetAddress[1];
        ia[0] = new InternetAddress(replyTo);
        message.setReplyTo(ia);
      }

      if (arrayto.length != 0)
      {
        for (int i = 0; i < arrayto.length; i++)
        {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrayto[i]));
        }
      }

      message.setFrom(new InternetAddress(from));
      message.setSubject(subject);
      BodyPart messageBodyPart        = new MimeBodyPart();
      String messageContext = "text/plain";
      messageBodyPart.setContent(body, messageContext);
      Multipart multipart             = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);
      messageBodyPart = new MimeBodyPart();
      message.setContent(multipart);
      Transport.send(message);
    }catch(Exception e){
      System.out.println("[Exception]: SendMailEJB.sendMailMessage:");
      e.printStackTrace();
    } //end of catch block (Exception)
    return(true);
  }   // end sendMailMessageBasic() method

  /**
   * Sends the mail message passed to the method. It also inserts
   * the ncessary records in the database link to
   * the userID passed to the method.
   *
   * @param userId       The user who is sending the email.
   * @param mailmessage  The email message to be send (and stored in the
   * database).
   */
  public boolean sendMailMessage(int userId, MailMessage mailmessage)
  {
    //Store messag into database get messageID from mailDeliverbean
    //this message id must be present in header
    //Code for MailDeliver
    int messageid  = 0;
    CVDal cvdl = new CVDal(this.dataSource);
    try
    {
      Properties props = System.getProperties();
      if (mailmessage.getSmtpserver() != null)
      {
        props.put("mail.smtp.host", mailmessage.getSmtpserver());
      }else{
        return(false);
      }
      Session session = Session.getDefaultInstance(props, null);
      MimeMessage message = new MimeMessage(session);

      int accountid = mailmessage.getAccountID();

      ArrayList bcc = mailmessage.getBcc();
      //System.out.println("bcc"+bcc);
      //System.out.println("bcc.size()"+bcc.size());
      String arraybcc[] = new String[bcc.size()];

      for (int i = 0; i < arraybcc.length; i++)
      {
        MailAddress ma  = (MailAddress) bcc.get(i);
        arraybcc[i] = ma.getAddress();
        //System.out.println("arraybcc["+i+"]"+arraybcc[i]);
      }

      ArrayList cc = mailmessage.getCc();
      //System.out.println("cc"+cc);
      //System.out.println("cc.size()"+cc.size());
      String arraycc[] = new String[cc.size()];
      for (int i = 0; i < arraycc.length; i++)
      {
        MailAddress ma  = (MailAddress) cc.get(i);
        arraycc[i] = ma.getAddress();
        //System.out.println("arraycc["+i+"]"+arraycc[i]);
      }

      ArrayList to = mailmessage.getTo();
     // System.out.println("to"+to);
      //System.out.println("to.size()"+to.size());
      String arrayto[] = new String[to.size()];
      for (int i = 0; i < arrayto.length; i++)
      {
        MailAddress ma = (MailAddress)to.get(i);
        arrayto[i] = ma.getAddress();
        //System.out.println("arrayto["+i+"]"+arrayto[i]);
      }

      long l = (new java.util.Date()).getTime();
      java.sql.Timestamp messagedate = (java.sql.Timestamp) new java.sql.Timestamp(l);
      String subject = mailmessage.getSubject();
      String body = mailmessage.getBody();
      String from = mailmessage.getMailFrom();
      String replyTo = mailmessage.getReplyTo();

      if (replyTo != null && replyTo.length() >0)
      {
        InternetAddress[] ia = new InternetAddress[1];
        ia[0] = new InternetAddress(replyTo);
        message.setReplyTo(ia);
      }

      // extra headers that may be used for some action in to CV
      // eg - Activity send email, Adding individual throough email
      HashMap hhm = mailmessage.getHeadersHM();
      String allHeaderString = null;
      if (hhm != null)
      {
        allHeaderString = new String();
        Iterator it  = hhm.keySet().iterator();
        while (it.hasNext())
        {
          String hmKey    = (String) it.next();
          String hmValue  = (String) hhm.get(hmKey);
          allHeaderString = allHeaderString.concat(hmKey + "=" + hmValue + "::");
         // message.addHeader(hmKey, hmValue);
        }
      }

      //cvdl = new CVDal(dataSource);
      cvdl.setSql("email.savedraft1");

      cvdl.setString(1, from);
      cvdl.setString(2, subject);
      cvdl.setString(3, replyTo);
      cvdl.setString(4, body);
      cvdl.setString(5, allHeaderString);
      cvdl.setInt(6, accountid);
      cvdl.setInt(7, userId);
      cvdl.executeUpdate();

      messageid = cvdl.getAutoGeneratedKey();
      message.addHeader("X-centraviewID", "" + messageid);

      cvdl.clearParameters();

      cvdl.setSql("email.savedraft4");
      cvdl.setInt(1, messageid);
      cvdl.setString(2, "NO");
      cvdl.setInt(3, accountid);
      cvdl.executeUpdate();
      cvdl.clearParameters();

      if (arrayto.length != 0)
      {
        for (int i = 0; i < arrayto.length; i++)
        {
          cvdl.setSql("email.savedraft3");
          cvdl.setInt(1, messageid);
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrayto[i]));
          cvdl.setString(2, arrayto[i]);
          cvdl.setString(3, "TO");
          cvdl.setString(4, "NO");
          cvdl.executeUpdate();
          cvdl.clearParameters();

        }
      }

      if (arraybcc.length != 0)
      {

        for (int i = 0; i < arraybcc.length; i++)
        {
          cvdl.setSql("email.savedraft3");
          cvdl.setInt(1, messageid);
          cvdl.setString(2, arraybcc[i]);
          message.addRecipient(Message.RecipientType.BCC, new InternetAddress(arraybcc[i]));
          cvdl.setString(3, "BCC");
          cvdl.setString(4, "NO");
          cvdl.executeUpdate();
          cvdl.clearParameters();
        }
      }

      if (arraycc.length != 0)
      {

        for (int i = 0; i < arraycc.length; i++)
        {
//System.out.println("arraycc[i]"+arraycc[i]        );
          cvdl.setSql("email.savedraft3");
          cvdl.setInt(1, messageid);
          cvdl.setString(2, arraycc[i]);
          message.addRecipient(Message.RecipientType.CC, new InternetAddress(arraycc[i]));
          cvdl.setString(3, "CC");
          cvdl.setString(4, "NO");
          cvdl.executeUpdate();
          cvdl.clearParameters();
        }
      }

      message.setFrom(new InternetAddress(from));

      message.setSubject(subject);
      BodyPart messageBodyPart        = new MimeBodyPart();

      //Added by Ryan Grier <ryan@centraview.com>
      //Need to send email as plain text if
      //it contains no html content.
      String messageContext = "text/plain";

// 	Modified by Deepa
//	User Preferences value will decide content type of the email. ie. Plain text or Html text

	InitialContext ic = CVUtility.getInitialContext();
	PreferenceLocalHome home  = (PreferenceLocalHome)ic.lookup("local/Preference");
	PreferenceLocal localPref =  home.create();
	localPref.setDataSource(this.dataSource);

	UserPrefererences userPref = localPref.getUserPreferences(userId);


	if(userPref != null)
	if (userPref.getContentType() != null && userPref.getContentType().equals("PLAIN"))
	{
		messageContext = "text/plain";
	}
	else if ( userPref.getContentType() != null && userPref.getContentType().equals("HTML"))
	{
		messageContext = "text/html";
	}

	if(mailmessage.getContentType() != null && mailmessage.getContentType().equals("HTML") ){
		messageContext = "text/html";
	}

/*    if ((body.indexOf("<") > -1) && (body.indexOf(">") > -1))
      {
        messageContext = "text/html";
      } //end of if statement ((body.indexOf("<") > -1) && (body.indexOf(">") > -1))
      else if ((body.indexOf("&lt;") > -1) && (body.indexOf("&gt;") > -1))
      {
        messageContext = "text/html";
      } //end of if statement ((body.indexOf("&lt;") > -1) && (body.indexOf("&gt;") > -1))
*/

      messageBodyPart.setContent(body, messageContext);

      Multipart multipart             = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      messageBodyPart = new MimeBodyPart();
      HashMap attchmentids            = mailmessage.getAttachFileIDs();

      if ((attchmentids != null) && (attchmentids.size() != 0))
      {

        Set col       = attchmentids.keySet();
        Iterator itt  = col.iterator();
        int i         = 0;
        while (itt.hasNext())
        {
          String fileid        = (String) itt.next();
          String name          = (String) attchmentids.get(fileid);
          cvdl.setSql("email.savedraftattchment");
          cvdl.setInt(1, messageid);
          cvdl.setString(2, name);
          cvdl.setInt(3, Integer.parseInt(fileid));
          cvdl.executeUpdate();
          cvdl.clearParameters();
          i++;

          CvFileFacade cvfile  = new CvFileFacade();
          CvFileVO cvfilevo    = cvfile.getEmailAttachment(userId, Integer.parseInt(fileid), this.dataSource);

          String path          = cvfilevo.getPhysicalFolderVO().getFullPath(null, true) + cvfilevo.getName();

          DataSource source    = new FileDataSource(path);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(name);
          multipart.addBodyPart(messageBodyPart);

        } //end of while loop(itt.hasNext())
      } //end of if statement ((attchmentids != null) && (attchmentids.size() != 0))
      // delete mail from  drafts
      if (mailmessage.getMessageID() != 0)
      {
        String mailIdList[]          = {"" + mailmessage.getMessageID()};

        InitialContext icPref          = CVUtility.getInitialContext();
        EmailManageLocalHome emailManageHome  = (EmailManageLocalHome) icPref.lookup("local/EmailManage");
        EmailManageLocal local     = emailManageHome.create();
        local.setDataSource(this.dataSource);

        int result                 = local.emailDeleteTrash(mailmessage.getFolder(), mailIdList);
      } //end of if statement (mailmessage.getMessageID() != 0)

      message.setContent(multipart);
      try
      {
        Transport.send(message);
      }catch(SendFailedException sendFailed){
        System.out.println("\n\n\nSendFailedException occurred!!!\n");
        System.out.println("Invalid unsent addresses: [" + sendFailed.getInvalidAddresses() + "]");
        System.out.println("Valid SENT addresses: [" + sendFailed.getValidSentAddresses() + "]");
        System.out.println("Valid UNsent addresses: [" + sendFailed.getValidUnsentAddresses() + "]\n");
        sendFailed.printStackTrace();
        System.out.println("\n\n\n");
      }
    }catch (Exception e){
      System.out.println("[Exception]: SendMailEJB.sendMailMessage:");
      e.printStackTrace();
      return(false);
    }finally{
      cvdl.clearParameters();
      cvdl.destroy();
      cvdl = null;
    }
    return(true);
  } //end of sendMailMessage method


  /**
   * This method send forrgotten password to user
   * @param mailmessage MailMessage
   * @author : Valery Kasinski
   */
  public void sendForgottenPasswordMail(MailMessage mailmessage)
  {
        int messageid  = 0;
        try
        {
          Properties props                = System.getProperties();
          if (mailmessage.getSmtpserver() != null)
          {
            props.put("mail.smtp.host", mailmessage.getSmtpserver());
          }
          else
          {
            // TODO HARDCODED SMTP HOST!!
            props.put("mail.smtp.host", "minsksrv");
          }
          Session session                 = Session.getDefaultInstance(props, null);
          MimeMessage message             = new MimeMessage(session);

          ArrayList to                    = mailmessage.getTo();
          String arrayto[]                  = new String[to.size()];
          for (int i = 0; i < arrayto.length; i++)
          {
            MailAddress ma  = (MailAddress) to.get(i);
            arrayto[i] = ma.getAddress();
          }

          long l                          = (new java.util.Date()).getTime();
          java.sql.Timestamp messagedate  = (java.sql.Timestamp) new java.sql.Timestamp(l);
          String subject                  = mailmessage.getSubject();
          String body                     = mailmessage.getBody();
          String from                     = mailmessage.getMailFrom();
              String replyTo                     = mailmessage.getReplyTo();

              if(replyTo != null && replyTo.length() >0)
              {
                    InternetAddress[] ia = new InternetAddress[1];
                    ia[0] = new InternetAddress(replyTo);
                    message.setReplyTo(ia);
              }

          //  extra headers that may be used for some action in to CV
          // eg - Activity send email, Adding individual throough email
          HashMap hhm                     = mailmessage.getHeadersHM();
          String allHeaderString          = null;
          if (hhm != null)
          {
            allHeaderString = new String();
            Iterator it  = hhm.keySet().iterator();
            while (it.hasNext())
            {
              String hmKey    = (String) it.next();
              String hmValue  = (String) hhm.get(hmKey);
              allHeaderString = allHeaderString.concat(hmKey + "=" + hmValue + "::");
   //           message.addHeader(hmKey, hmValue);

            }
          }
          message.addHeader("X-centraviewID", "" + messageid);

          if (arrayto.length != 0)
          {
            for (int i = 0; i < arrayto.length; i++)
            {
              message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrayto[i]));
            }
          }

          message.setFrom(new InternetAddress(from));
          message.setSubject(subject);
          BodyPart messageBodyPart        = new MimeBodyPart();
          String messageContext = "text/plain";
          messageBodyPart.setContent(body, messageContext);
          Multipart multipart             = new MimeMultipart();
          multipart.addBodyPart(messageBodyPart);
          messageBodyPart = new MimeBodyPart();
          message.setContent(multipart);
          Transport.send(message);
        }//end of try block
        catch (Exception e)
        {
          System.out.println("[Exception]: SendMailEJB.sendMailMessage:");
          e.printStackTrace();
        } //end of catch block (Exception)
  }

  // will be used in case of email header being used for other services
  // email invitaion of activity, adding individual through email
  // This methiod will update the given header. Will be typically called
  // to mark a task done. Like invitation accepted or individual added etc
  /**
   * Will be used in case of email header being used for other services
   * email invitaion of activity, adding individual through email
   * This methiod will update the given header. Will be typically called
   * to mark a task done. Like invitation accepted or individual added etc
   *
   * @param userId               The userID of the user calling this method
   * @param messageId            The messageID of the message header
   * @param headerName           The name of the header attribute
   * @param headerValue          The value of the header attribute
   * @exception EJBException  A EJBException has occurred.
   */
  public void updateHeader(int userId, int messageId, String headerName, String headerValue)
    throws EJBException
  {

    if (messageId == 0)
    {
      throw new EJBException("UpdateHeader: Message ID not specified");
    } //end of if statement (messageId == 0)

    if (headerName == null || headerName.length() == 0)
    {
      throw new EJBException("UpdateHeader: Header Name is required");
    } //end of if statement (headerName == null || headerName.length() == 0)

    if (headerValue == null || headerValue.length() == 0)
    {
      throw new EJBException("UpdateHeader: Header Value is required");
    } //end of if statement (headerValue == null || headerValue.length() == 0)

    CVDal dl  = new CVDal(dataSource);
    try
    {
      dl.setSql("email.getheader");
      dl.setInt(1, messageId);
      Collection col    = dl.executeQuery();

      if (col == null)
      {
        throw new EJBException("UpdateHeader: Message Not found");
      } //end of if statement (col == null)

      Iterator it       = col.iterator();

      if (!it.hasNext())
      {
        throw new EJBException("UpdateHeader: Message Not found");
      } //end of if statement (!it.hasNext())

      HashMap hm        = (HashMap) it.next();
      String allHeader  = (String) hm.get("Headers");


      int hi            = allHeader.indexOf(headerName);
      if (hi < 0)
      {
        throw new EJBException("UpdateHeader: HEader not found");
      } //end of if statement (hi < 0)

      int hd            = allHeader.indexOf(Constants.EH_HEADER_DELIMETER, hi);
      String orgHeader  = allHeader.substring(hi, hd);
      String newHeader  = headerName + Constants.EH_KEYVALUE_DELEMETER + headerValue;

      allHeader = allHeader.replaceAll(orgHeader, newHeader);

      dl.clearParameters();
      dl.setSql("email.updateheader");
      dl.setString(1, allHeader);
      dl.setInt(2, messageId);
      dl.executeUpdate();
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception]: Exception in SendMailEJB.updateHeader:");
      e.printStackTrace();
    } //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
  } //end of updateHeader method


	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

} //end of SendMailEJB class
