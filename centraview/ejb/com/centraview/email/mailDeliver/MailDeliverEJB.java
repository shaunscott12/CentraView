/*
 * $RCSfile: MailDeliverEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:17 $ - $Author: mking_cv $
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

package com.centraview.email.mailDeliver;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;

import com.centraview.common.CVDal;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;

/**
 *  This class is a Stateless Session Bean
 *  which returns Data for each call
 *
 * @author CentraView, LLC.
 */
public class MailDeliverEJB implements SessionBean
{
  private String dataSource = "MySqlDS";
  SessionContext sc;

  /**
   * Constructor currently has no functionality.
   */
  public MailDeliverEJB()
  {}

  /**
   * Called by the container to create a session bean instance. Its parameters typically
   * contain the information the client uses to customize the bean instance for its use.
   * It requires a matching pair in the bean class and its home interface.
   *
   * @return void
   */
  public void ejbCreate()
  {}

  /**
   * A container invokes this method before it ends the life of the session object. This
   * happens as a result of a client's invoking a remove operation, or when a container
   * decides to terminate the session object after a timeout. This method is called with
   * no transaction context.
   *
   * @return void
   */
  public void ejbRemove()
  {}

  /**
   * The activate method is called when the instance is activated from its 'passive' state.
   * The instance should acquire any resource that it has released earlier in the ejbPassivate()
   * method. This method is called with no transaction context.
   *
   * @return void
   */
  public void ejbActivate()
  {}

  /**
   * The passivate method is called before the instance enters the 'passive' state. The
   * instance should release any resources that it can re-acquire later in the ejbActivate()
   * method. After the passivate method completes, the instance must be in a state that
   * allows the container to use the Java Serialization protocol to externalize and store
   * away the instance's state. This method is called with no transaction context.
   *
   * @return void
   */
  public void ejbPassivate()
  {}

  /**
   * Set the associated session context. The container calls this method after the instance
   * creation. The enterprise Bean instance should store the reference to the context
   * object in an instance variable. This method is called with no transaction context.
   *
   * @param sc  SessionContext
   * @return void
   */
  public void setSessionContext(SessionContext sc)
  {
    this.sc = sc;
  }

  /**
   * mailDeliverMessage()
   *
   * @param mailMessage  A HashMap containing the details of the email message to be devliered.
   * @return int: the new messageID from the database
   */
  public int mailDeliverMessage(HashMap mailMessage)
  {
    // This code is use afte rules from database
    // This is one row (1 rule)
    int newMessageID = 0;

    try
    {
      // extract the RFC-822 message from the HashMap
      Message mesage[] = (Message[])mailMessage.get("message");

      // get the acocunt info from the HashMap and store it in another HashMap accountInfo
      HashMap accountInfo = (HashMap)mailMessage.get("account");

      String lastuidUpdate = (String)mailMessage.get("lastuidUpdate");

      if (lastuidUpdate != null && !(lastuidUpdate.equals("")))
      {
        CVDal cvdl = new CVDal(dataSource);
        cvdl.setSqlQuery(lastuidUpdate);
        cvdl.executeUpdate();
        cvdl.destroy();
        cvdl = null;
      }

      // get the UserID and AccountID from the accountInfo hashmap
      int userID = ((Number)accountInfo.get("owner")).intValue();
      int accountID = ((Long)accountInfo.get("accountid")).intValue();

      ArrayList rv = this.getRuleContainer(accountID);
      for (int i = 0; i < mesage.length; i++)
      {
        newMessageID = this.applyRule(rv, mesage[i], accountID, userID);

        /*** Modified By Shilpa to create/update a ticket out of an email***/

        Address from[] = mesage[i].getFrom();
        String strFrom = from[0].toString();

        String body = null;

        // get body part
        Object content = mesage[i].getContent();

        if (content instanceof Multipart)
        {
          Multipart multipart = (Multipart)content;

          for (int j = 0, n = multipart.getCount(); j < n; j++)
          {
            Part part = (Part)multipart.getBodyPart(j);
            String contentType = part.getContentType();
            String disposition = part.getDisposition();

            if (disposition == null)
            {
              if (((contentType.length() >= 10) && (contentType.toLowerCase().substring(0, 10).equals("text/plain")))
                || ((contentType.length() >= 9) && (contentType.toLowerCase().substring(0, 9).equals("text/html"))))
              {
                body = (String) (part.getContent()).toString();
              }
            } // end if (disposition == nul)
          } // end for loop
        }else{
          body = (String) (mesage[i].getContent()).toString();
        }
        //get body ends here.

        // Get Application settings straight from the EJB and not from the ApplicationCache that is
        // running in a different JVM.  And virtualization will not support the way it was accessed before.

/*
		boolean isTicketDone = false;
        boolean isNewTkt = false;

        InitialContext ic = CVUtility.getInitialContext();
        AppSettingsLocalHome appSettingsLocalHome = (AppSettingsLocalHome)ic.lookup("local/AppSettings");
        AppSettingsLocal appSettingsLocal = (AppSettingsLocal)appSettingsLocalHome.create();
        appSettingsLocal.setDataSource(dataSource);

        //get all support email ids
        ArrayList arlEmailDetails = appSettingsLocal.getSupportEmailDetails();
        Vector validAddForTkt = appSettingsLocal.getAllSupportEmailIds();

        String replyBody = "";
        String replySubject = "";
        if (arlEmailDetails.size() > 1)
        {
          replyBody = (String)arlEmailDetails.get(1);
          replySubject = (String)arlEmailDetails.get(0);
        }

        Address allRecpts[] = mesage[i].getAllRecipients();
        int noOfRecpts = allRecpts.length;

        ArrayList toArl = new ArrayList();
        toArl.add(new MailAddress(strFrom));



        int folderID = 0;
        int tktID = 0;

        for (int kk = 0; kk < noOfRecpts; kk++)
        {
          String strRecpt = allRecpts[kk].toString();

          //Check if 'To Address' is among the list or not.
          if (validAddForTkt.contains(strRecpt) && isTicketDone == false)
          {
            isTicketDone = true;

            //Query to get individual id from 'From Add' of mail.
            int indvId = 0;

            EmailHelperLocalHome emailHelper = (EmailHelperLocalHome)ic.lookup("local/EmailHelper");
            EmailHelperLocal rmt = (EmailHelperLocal)emailHelper.create();
            rmt.setDataSource(this.dataSource);

            indvId = rmt.getIndividualID(strRecpt);
            folderID = rmt.getFolderIDForAccount(accountID, "Inbox", "SYSTEM");

            String subject = mesage[i].getSubject();

            //check if its new ticket or update ticket
            int indexForHash = subject.indexOf("#");
            String tktNum = null;

            if (indexForHash != -1)
              tktNum = subject.substring(indexForHash + 1, subject.length());

            Vector allTktsVec = null;

            if (tktNum != null && tktNum.length() != 0)
            {
              //check if this ticket exits or not
              SupportHelperLocalHome home = (SupportHelperLocalHome)ic.lookup("local/SupportHelper");
              SupportHelperLocal remote = (SupportHelperLocal)home.create();
              remote.setDataSource(this.dataSource);
              allTktsVec = remote.getAllTicketIDs();
            }

            if (allTktsVec != null && allTktsVec.contains(new Long(tktNum)))
            {
              //Add new thread to ticket

              ThreadVO tVO = new ThreadVO();

              tVO.setTitle(subject);
              tVO.setDetail(body);
              tVO.setTicketId(Integer.parseInt(tktNum));
              tVO.setPriorityId(4);
              tVO.setCreatedBy(indvId);

              TicketLocalHome home = (TicketLocalHome)ic.lookup("local/Ticket");
              TicketLocal remote = (TicketLocal)home.create();
              remote.setDataSource(this.dataSource);
              remote.addThread(indvId, tVO);
            }
            else
            {
              //int owner = ApplicationSettingCache.defaultTicketOwener();
              // TODO HARDCODED: put default ticketOwner in the database and get it here via the EJB
              int owner = 2;
              isNewTkt = true;

              TicketVO ticketVO = new TicketVO();

              TicketLocalHome home = (TicketLocalHome)ic.lookup("local/Ticket");
              TicketLocal remote = (TicketLocal)home.create();
              remote.setDataSource(this.dataSource);

              // set the ticketVO

              ticketVO.setTitle(subject);
              ticketVO.setDetail(body);
              ticketVO.setPriorityId(4);
              ticketVO.setStatusId(1);
              ticketVO.setAssignedToId(owner);
              ticketVO.setCreatedBy(indvId);
              ticketVO.setOwner(owner);
              ticketVO.setCustomField(new Vector());

              //Create a new ticket.
              tktID = remote.addTicket(indvId, ticketVO);
            }

          } //If matches with admint email list for support.

        } //for loop ends here

        if (isTicketDone == true && isNewTkt == true)
        {
          //Now send a reply to all recipents

          MailMessage mailmessagae = new MailMessage();
          mailmessagae.setMessageID(newMessageID);
          mailmessagae.setAccountID(accountID);
          mailmessagae.setFolder(folderID);
          mailmessagae.setMailFrom(strFrom);
          mailmessagae.setTo(toArl);
          mailmessagae.setCc(new ArrayList());
          mailmessagae.setBcc(new ArrayList());
          mailmessagae.setSubject(replySubject + tktID);
          mailmessagae.setBody(replyBody);
          //mailmessagae.setAttachFileIDs(new HashMap());

          SendMailLocalHome home = (SendMailLocalHome)ic.lookup("local/SendMail");
          SendMailLocal remote = home.create();
          remote.setDataSource(this.dataSource);
          remote.sendMailMessage(userID, mailmessagae);
        }
*/
        /*** Modified By Shilpa ends here***/

      }
    }
    catch (Exception e)
    {
      System.out.println("[MailDeliverEJB] Exception thrown in mailDeliverMessage(): " + e);
      //e.printStackTrace();
      newMessageID = 0;
    }
    return newMessageID;
  } // end mailDeliverMessage() method

  /**
   * mailDeliverMessage()
   *
   * @param mailMessage  A HashMap containing the details of the email message to be devliered.
   * @return void
   */
  private int applyRule(ArrayList rv, Message message, int accountID, int userID) throws Exception
  {
    CVDal cvdl = new CVDal(dataSource);

    MimeMessage mimeMessage = (MimeMessage)message;

    int messageID = 0;

    try
    {
      Address from[] = message.getFrom();

      // get the message's date and time
      long messageTime = (message.getSentDate()).getTime();
      java.sql.Timestamp messageDate = (java.sql.Timestamp)new java.sql.Timestamp(messageTime);

      cvdl.setSqlQuery("INSERT INTO emailmessage(MessageDate, MailFrom, Subject, Body, accountID , Priority, Headers, owner,Size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

      String mesgFrom = from[0].toString();
      mesgFrom = mesgFrom.replaceAll("\""," ");
      mesgFrom = mesgFrom.replaceAll("'"," ");
      mesgFrom = mesgFrom.trim();

      String[] DateHeader = message.getHeader("Date");
      Date receiveDate = new Date(DateHeader[0]);
      SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
      String receivedDate = formatter.format(receiveDate);

      if (receivedDate != null && !(receivedDate.equals("")))
      {
        cvdl.setString(1, receivedDate);
      }else{
        cvdl.setString(1, "CONCAT(CURRENT_DATE, CURRENT_TIME)");
      }

      cvdl.setString(2, mesgFrom);
      cvdl.setString(3, message.getSubject());

      String MsgBody = null;
      StringBuffer msgBody = new StringBuffer();


      Object o = message.getContent();
      if (o instanceof String)
      {
        msgBody.append((String) o);
      }else if(o instanceof Multipart){
        Multipart mp = (Multipart)o;
        int countMultipart = mp.getCount();

        for (int j = 0; j < countMultipart; j++)
        {
          // Part are numbered starting at 0
          BodyPart b = mp.getBodyPart(j);
          String mimeType2 = b.getContentType();
          String disposition2 = b.getDisposition();

          Object o2 = b.getContent();

          if (o2 instanceof String)
          {
            if (mimeType2 != null &&
                (mimeType2.toLowerCase().indexOf("text/plain") != -1 || mimeType2.toLowerCase().indexOf("text/html") != -1) &&
                (disposition2 == null ||(disposition2!=null && !disposition2.equals(Part.ATTACHMENT))))
            {
              if (mimeType2.toLowerCase().indexOf("text/html")>=0)
              {
                // HTML message body
                msgBody.append(b.getContent().toString());
              }else{
                // plain text part
                msgBody.append(b.getContent().toString());
              }
              if (! msgBody.toString().equals(""))
              {
                break;
              }
            }
          }else if(o2 instanceof Multipart){
            //"**This BodyPart is a nested Multipart.  ");
            Multipart mp2 = (Multipart)o2;
            int countMultipart1 = mp2.getCount();

            for (int k = 0; k < countMultipart1; k++)
            {
              // Part are numbered starting at 0
              BodyPart b2 = mp2.getBodyPart(k);
              String mimeType3 = b2.getContentType();
              String disposition3 = b2.getDisposition();

              Object o3 = b2.getContent();

              if (o3 instanceof String)
              {
                if (mimeType3 != null &&
                    (mimeType3.toLowerCase().indexOf("text/plain") != -1 || mimeType3.toLowerCase().indexOf("text/html") != -1) &&
                    (disposition3==null ||( disposition3!=null && !disposition3.equals(Part.ATTACHMENT))))
                {
                  if (mimeType3.toLowerCase().indexOf("text/html") >= 0)
                  {
                    // HTML message body
                    msgBody.append(b2.getContent().toString());
                  }else{
                    // plain text part
                    msgBody.append(b2.getContent().toString());
                  }
                }

                if (! msgBody.toString().equals(""))
                {
                  break;
                }
              }   // end if (o3 instanceof String)
            }   // end for (int k = 0; k < countMultipart1; k++)
          }   // end if(o2 instanceof Multipart)
        }   // end for (int j = 0; j < countMultipart; j++)
      }   // end if (o instanceof String)

      MsgBody = msgBody.toString();

      if (MsgBody != null)
      {
        cvdl.setString(4,MsgBody);
      }else{
        cvdl.setString(4,"");
      }

      cvdl.setInt(5, accountID);

      // set priority field based on message X-Priority header.
      // if the X-Priority header is not set, or is unknown, then
      // set our database priority field to medium.
      if (mimeMessage.getHeader("X-Priority", "") != null)
      {
        if ((mimeMessage.getHeader("X-Priority", "")).equals("1"))
        {
          cvdl.setString(6, "HIGH");
        }else if ((mimeMessage.getHeader("X-Priority", "")).equals("5")){
          cvdl.setString(6, "LOW");
        }else{
          cvdl.setString(6, "MEDIUM");
        }
      }else{
        cvdl.setString(6, "MEDIUM");
      }

      // added for taking the header also
      Enumeration em = mimeMessage.getAllHeaderLines();

      StringBuffer ah = new StringBuffer();

      // loop through all the headers
      while (em.hasMoreElements())
      {
        String ch = (String)em.nextElement();
        ah.append(ch);
      }   // end while (em.hasMoreElements())

      if (ah.length() > 0)
      {
        cvdl.setString(7, ah.toString());
      }else{
        cvdl.setString(7, null);
      }

      cvdl.setInt(8, userID);

      long messageSize = mimeMessage.getSize();
      if (messageSize > 1024)
      {
        messageSize = messageSize / 1024;
      }else{
        messageSize = 1;
      }

      cvdl.setString(9, messageSize+"");

      cvdl.executeUpdate();

      messageID = cvdl.getAutoGeneratedKey();

      cvdl.clearParameters();

      if (messageID != 0)
      {
        Address arrayTo[] = message.getRecipients(Message.RecipientType.TO);
        Address arrayCc[] = message.getRecipients(Message.RecipientType.CC);
        Address arrayBcc[] = message.getRecipients(Message.RecipientType.BCC);

        if ((arrayTo != null) && (arrayTo.length != 0))
        {
          for (int i = 0; i < arrayTo.length; i++)
          {
            String mesgTo = arrayTo[i].toString();
            mesgTo = mesgTo.replaceAll("\""," ");
            mesgTo = mesgTo.replaceAll("'"," ");
            mesgTo = mesgTo.trim();

            cvdl.setSql("email.savedraft3");
            cvdl.setInt(1, messageID); // messageID
            cvdl.setString(2, mesgTo ); // To: Address
            cvdl.setString(3, "TO"); // RecipientType
            cvdl.setString(4, "NO"); // RecipientIsGroup
            cvdl.executeUpdate();
            cvdl.clearParameters();
          }
        }   // end if ((arrayTo != null) && (arrayTo.length != 0))

        if ((arrayBcc != null) && (arrayBcc.length != 0))
        {
          for (int i = 0; i < arrayBcc.length; i++)
          {
            String mesgBcc = arrayBcc[i].toString();
            mesgBcc = mesgBcc.replaceAll("\""," ");
            mesgBcc = mesgBcc.replaceAll("'"," ");
            mesgBcc = mesgBcc.trim();

            cvdl.setSql("email.savedraft3");
            cvdl.setInt(1, messageID); // messageID
            cvdl.setString(2, mesgBcc); // To: Address
            cvdl.setString(3, "BCC"); // RecipientType
            cvdl.setString(4, "NO"); // RecipientIsGroup
            cvdl.executeUpdate();
            cvdl.clearParameters();
          }
        }   // end if ((arrayBcc != null) && (arrayBcc.length != 0))

        if ((arrayCc != null) && (arrayCc.length != 0))
        {
          for (int i = 0; i < arrayCc.length; i++)
          {
            String mesgCC = arrayCc[i].toString();
            mesgCC = mesgCC.replaceAll("\""," ");
            mesgCC = mesgCC.replaceAll("'"," ");
            mesgCC = mesgCC.trim();

            cvdl.setSql("email.savedraft3");
            cvdl.setInt(1, messageID); // messageID
            cvdl.setString(2, mesgCC); // To: Address
            cvdl.setString(3, "CC"); // RecipientType
            cvdl.setString(4, "NO"); // RecipientIsGroup
            cvdl.executeUpdate();
            cvdl.clearParameters();
          }
        }

        boolean nonRuleApplied = true;

        if (rv != null && rv.size() != 0)
        {
          for (int ruleCount = 0 ; ruleCount <  rv.size() ; ruleCount ++)
          {
            com.centraview.email.Rule rules = (com.centraview.email.Rule) rv.get(ruleCount);
            Vector ruleConditions = rules.getActionConditions();
            int actiontype = rules.getActiontype();
            int actionFolderId = rules.getActionFolderID();

            if (ruleConditions != null && ruleConditions.size() != 0)
            {
              String Query = "select em.MessageID from emailmessage em, emailrecipient er where er.messageid=em.messageid AND em.messageid="+messageID+" AND ";

              for (int ruleCondition = 0 ; ruleCondition < ruleConditions.size(); ruleCondition ++)
              {
                com.centraview.email.RuleObj ruleObj = (com.centraview.email.RuleObj)ruleConditions.get(ruleCondition);

                HashMap searchOn = new HashMap();
                searchOn.put("0", "( em.Subject ");
                searchOn.put("1", "( em.MailFrom ");
                searchOn.put("2", "( er.recipienttype='TO' and er.Address ");
                searchOn.put("3", "( er.Body ");
                searchOn.put("4", "( er.MessageDate ");

                int andOrCondition = ruleObj.getJoin();
                int fieldCondition = ruleObj.getField();
                int Condition = ruleObj.getCondition();
                String Criteria = ruleObj.getCriteria();

                if (ruleCondition != 0)
                {
                  if (andOrCondition == 0)
                  {
                    Query += " OR ";
                  }

                  if (andOrCondition == 1)
                  {
                    Query += " AND ";
                  }
                }

                Query += " " + searchOn.get(fieldCondition+"") + " ";

                if (Condition == 0)
                {
                  Query += " like  '"+Criteria+"%' )";
                }

                if (Condition == 1)
                {
                  Query += " like  '%"+Criteria+"' )";
                }

                if (Condition == 2)
                {
                  Query += " != '"+Criteria+"' )";
                }

                if (Condition == 3)
                {
                  Query += " = '"+Criteria+"' )";
                }

                if (Condition == 4)
                {
                  Query += " NOT like  '%"+Criteria+"%' )";
                }

                if (Condition == 5)
                {
                  Query += " like  '%"+Criteria+"%' )";
                }
              }

              cvdl.setSqlQuery(Query);
              Collection v = cvdl.executeQuery();
              cvdl.clearParameters();
              Iterator it = v.iterator();
              boolean flag= false;

              if (it.hasNext())
              {
                nonRuleApplied = false;
                cvdl.setSql("email.selectemailaction");
                cvdl.setInt(1, actiontype);
                Collection emailAction = cvdl.executeQuery();
                cvdl.clearParameters();
                Iterator itEmailAction = emailAction.iterator();

                boolean move = false;
                String  read = "NO";
                boolean delete = false;

                while (itEmailAction.hasNext())
                {
                  HashMap hmEmailAction = ( HashMap  )itEmailAction.next();
                  String actionName = (String) hmEmailAction.get( "ActionName" ) ;

                  if (actionName != null && actionName.equals("MOVE"))
                  {
                    move = true;
                  }

                  if (actionName != null && actionName.equals("DELETE"))
                  {
                    delete = true;
                  }

                  if (actionName != null && actionName.equals("MARK_AS_READ"))
                  {
                    read = "YES";
                  }
                }

                if (! delete)
                {
                  if (! move)
                  {
                    cvdl.setSql("email.savedraft5");
                    cvdl.setInt(1, messageID);
                    cvdl.setString(2, read);
                    cvdl.setInt(3, accountID);
                    cvdl.executeUpdate();
                    cvdl.clearParameters();
                  }else{
                    cvdl.setSql("email.savedraft2");
                    cvdl.setInt(1, actionFolderId);
                    cvdl.setInt(2, messageID);
                    cvdl.setString(3,read);
                    cvdl.executeUpdate();
                    cvdl.clearParameters();
                  }   // end if (! move)
                }else{
                  cvdl.setSql("email.savedraft6");
                  cvdl.setInt(1, messageID);
                  cvdl.setString(2, read);
                  cvdl.setInt(3, accountID);
                  cvdl.executeUpdate();
                  cvdl.clearParameters();
                }   // end if (! delete)
              }   // end if (it.hasNext())
            }   // end if (ruleConditions != null && ruleConditions.size() != 0)

            if (! nonRuleApplied)
            {
              break;
            }
          }   // end for (int ruleCount = 0 ; ruleCount <  rv.size() ; ruleCount ++)
        }   // end if (rv != null && rv.size() != 0)

        if (nonRuleApplied)
        {
          cvdl.setSql("email.savedraft5");
          cvdl.setInt(1, messageID);
          cvdl.setString(2, "NO");
          cvdl.setInt(3, accountID);
          cvdl.executeUpdate();
          cvdl.clearParameters();
        }

	      // Handle Attachments
	      Object contentattch = message.getContent();
System.out.println("[-apply-rule-] contentattach = [" + contentattch + "]");
        if (contentattch instanceof Multipart)
        {
System.out.println("[-apply-rule-] contentattch instanceof Multipart... call handleMultipart()");
          this.handleMultipart((Multipart)contentattch, userID, cvdl, messageID);
        }else{
System.out.println("[-apply-rule-] contentattch NOT instanceof Multipart... call handlePart()");
          this.handlePart(message, userID, cvdl, messageID);
        }
      }   // end if (messageID != 0)
      //return(messageID);
    }catch(Exception e){
      System.out.println("[Exception] MailDeliverEJB.applyRule: " + e.toString());
      e.printStackTrace();
      //return(0);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return messageID;
  }   // end applyRule() method

  public void handleMultipart(Multipart multipart, int userid, CVDal cvdl, int messageid) throws Exception
  {
    for (int i = 0, n = multipart.getCount(); i < n; i++)
    {
      handlePart(multipart.getBodyPart(i), userid, cvdl, messageid);
    }
  }

  public void handlePart(Part part, int userid, CVDal cvdl, int messageid) throws Exception
  {
    String disposition = part.getDisposition();
    // Basically the code that VZ delivered said if disposition != null write this part out to disk.
    // I don't think this is correct behaviour.  But I have removed a bunch of useless checks.
    // and factored the code down to what it was really doing, and not flooding the logs without
    // filling the logs with the email messages.
    // Probably if we are not the first part we should be written out to disk.
    // But that information doesn't seem to be captured in this methodology.
    if (disposition != null)
    {
      this.saveFile(part.getFileName(), part.getInputStream(), userid, cvdl, messageid);
    }
  }

  public void saveFile(String filename, InputStream input, int userid, CVDal cvdl, int messageid) throws Exception
  {

    //System.out.println("Saving File");

    CvFileFacade cvfile = new CvFileFacade();
    CvFileVO flvo = new CvFileVO();
    flvo.setTitle("EmailAttachment"); //file name
    flvo.setName(filename);
    flvo.setCreatedBy(userid);

    int attchmentid = cvfile.addEmailAttachment(userid, flvo, input, this.dataSource);

    cvdl.setSql("email.savedraftattchment");
    cvdl.setInt(1, messageid);
    cvdl.setString(2, filename);
    cvdl.setInt(3, attchmentid);
    cvdl.executeUpdate();
    cvdl.clearParameters();
  }

  // Attachment

  private ArrayList getRuleContainer(int accountid)
  {

    ArrayList rv = new ArrayList();
    CVDal cvdl = null;

    try
    {
      cvdl = new CVDal(dataSource);
      cvdl.setSql("email.selectAllemailrule");
      //System.out.println("accountid"+accountid);
      cvdl.setInt(1, accountid);
      Collection col = cvdl.executeQuery();
      Iterator it = col.iterator();

      while (it.hasNext())
      {
        HashMap hm = (HashMap)it.next();
        String rule = (String)hm.get("rulestatement");
        String action = ((Number) hm.get( "ruleid" )).toString();
        int folderid = ( (Number) hm.get( "TargetID" )).intValue();

        //System.out.println("Rule "+rule);
        //System.out.println("Action "+action);

        if (action.equals("MOVE"))
          action = "0";
        if (action.equals("MARK_AS_READ"))
          action = "1";
        if (action.equals("DELETE"))
          action = "2";

        action = action + "<" + folderid;
        com.centraview.email.Rule r = new com.centraview.email.Rule(rule, action);

        rv.add(r);

      }

      //System.out.println("Rule Container size "+rv.size());
    }
    catch (Exception e)
    {
      System.out.println("[Exception] MailDeliverEJB.getRuleContainer: " + e.toString());
      e.printStackTrace();
    }
    finally
    {
      if (cvdl != null)
      {
        cvdl.clearParameters();
        cvdl.destroy();
        cvdl = null;
      }
    }
    return rv;

  }

  private boolean checkCondition(String field, int condition, String criteria)
  {
    boolean flag = false;
    switch (condition)
    {
      case 0 :
        flag = field.endsWith(criteria);
        break;
      case 1 :
        flag = field.startsWith(criteria);
        break;
      case 2 :
        flag = !(field.equals(criteria));
        break;
      case 3 :
        flag = field.equals(criteria);
        break;
      case 4 :
        if (field.indexOf(criteria) == -1);
        flag = true;
        break;
      case 5 :
        if (field.indexOf(criteria) != -1);
        flag = true;
        break;

    }
    return flag;
  } // check condition

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

}
