/*
 * $RCSfile: EmailEJB.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:30 $ - $Author: mking_cv $
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


package com.centraview.email.emailList;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;
import com.centraview.common.DateMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.email.EmailList;
import com.centraview.email.EmailListElement;
import com.centraview.email.RuleList;
import com.centraview.email.RuleListElement;

/**
 * This EJB class adds functionality to recieve the email list.
 *
 * @author   CentraView, LLC.
 */
public class EmailEJB implements SessionBean
{
  /**
   * The SessionContext interface for the instance.
   */
  private SessionContext sessionContext;
  	private String dataSource = "MySqlDS";

  //protected Context environment;

  /**
   * Set the associated session context. The container calls this method after
   * the instance creation. <p>
   *
   * The enterprise Bean instance should store the reference to the context
   * object in an instance variable. <P>
   *
   * This method is called with no transaction context.
   *
   * @param sessionContext    The new sessionContext value
   * @throws EJBException     Thrown by the method to indicate a failure caused
   *      by a system-level error.
   * @throws RemoteException  - This exception is defined in the method
   *      signature to provide backward compatibility for applications written
   *      for the EJB 1.0 specification. Enterprise beans written for the EJB
   *      1.1 specification should throw the javax.ejb.EJBException instead of
   *      this exception. Enterprise beans written for the EJB2.0 and higher
   *      specifications must throw the javax.ejb.EJBException instead of this
   *      exception.
   */
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  /**
   * Unsets the SessionContext.
   */
  public void unsetSessionContext()
  {
    sessionContext = null;
  }

  /**
   * The activate method is called when the instance is activated from its
   * "passive" state. The instance should acquire any resource that it has
   * released earlier in the ejbPassivate() method. <p>
   *
   * This method is called with no transaction context.
   *
   * @throws EJBException     Thrown by the method to indicate a failure caused
   *      by a system-level error.
   * @throws RemoteException  - This exception is defined in the method
   *      signature to provide backward compatibility for applications written
   *      for the EJB 1.0 specification. Enterprise beans written for the EJB
   *      1.1 specification should throw the javax.ejb.EJBException instead of
   *      this exception. Enterprise beans written for the EJB2.0 and higher
   *      specifications must throw the javax.ejb.EJBException instead of this
   *      exception.
   */
  public void ejbActivate()
  {
  }

  /**
   * The passivate method is called before the instance enters the "passive"
   * state. The instance should release any resources that it can re-acquire
   * later in the ejbActivate() method. <p>
   *
   * After the passivate method completes, the instance must be in a state that
   * allows the container to use the Java Serialization protocol to externalize
   * and store away the instance's state. <p>
   *
   * This method is called with no transaction context.
   *
   * @throws EJBException     Thrown by the method to indicate a failure caused
   *      by a system-level error.
   * @throws RemoteException  - This exception is defined in the method
   *      signature to provide backward compatibility for applications written
   *      for the EJB 1.0 specification. Enterprise beans written for the EJB
   *      1.1 specification should throw the javax.ejb.EJBException instead of
   *      this exception. Enterprise beans written for the EJB2.0 and higher
   *      specifications must throw the javax.ejb.EJBException instead of this
   *      exception.
   */
  public void ejbPassivate()
  {
  }

  /**
   * A container invokes this method before it ends the life of the session
   * object. This happens as a result of a client's invoking a remove operation,
   * or when a container decides to terminate the session object after a
   * timeout. <p>
   *
   * This method is called with no transaction context.
   *
   * @throws EJBException     Thrown by the method to indicate a failure caused
   *      by a system-level error.
   * @throws RemoteException  - This exception is defined in the method
   *      signature to provide backward compatibility for applications written
   *      for the EJB 1.0 specification. Enterprise beans written for the EJB
   *      1.1 specification should throw the javax.ejb.EJBException instead of
   *      this exception. Enterprise beans written for the EJB2.0 and higher
   *      specifications must throw the javax.ejb.EJBException instead of this
   *      exception.
   */
  public void ejbRemove()
  {
  }

  /**
   * EJB Create - needed for the EJB Spec.
   */
  public void ejbCreate()
  {
  }


  /**
   * Returns the desired EmailList based on the User ID, and a HashMap of misc
   * information. <p>
   *
   * The following fields looked at in the HashMap during the creation of this
   * list. These are:
   * <ul>
   *   <li> startATparam:
   *   <li> EndAtparam:
   *   <li> searchString:
   *   <li> sortmem:
   *   <li> sortType:
   * </ul>
   *
   *
   * @param userId  The ID of the user to get the email list for.
   * @param info    A HashMap with information about the list.
   * @return        An Email list with the correct properties.
   */
  public EmailList getEmailList(int userId, HashMap info)
  {
    EmailList emailList  = new EmailList();
    CVDal cvdl = new CVDal(dataSource);

    try
    {
      Integer startATparam  = (Integer) info.get("startATparam");
      Integer EndAtparam    = (Integer) info.get("EndAtparam");
      String searchString   = (String) info.get("searchString");
      String sortmem        = (String) info.get("sortmem");
      Character chr         = (Character) info.get("sortType");
      boolean adminUserFlag = ((Boolean) info.get("adminUserFlag")).booleanValue();
      char sorttype  = chr.charValue();
      int startat    = startATparam.intValue();
      int endat      = EndAtparam.intValue();
      int beginindex = Math.max(startat - 100, 1);
      int endindex   = endat + 100;


      emailList.setSortMember(sortmem);

      Integer folderID = (Integer)info.get("folderid");
      Collection v     = null;

      if (sortmem.equals("From")){
        sortmem = "emailmessage.mailfrom";
      }

      if (sortmem.equals("To")){
        sortmem = "emailmessage.replyto";
      }

      if (sortmem.equals("Received")){
        sortmem = "emailmessage.messagedate";
      }

      String appendStr = "";
      String sortStr = "";

      if (sorttype != 'A'){
        sortStr = " DESC ";
      }
      String privateString = "";
      System.out.println(adminUserFlag+"adminUserFlag");
      if(!adminUserFlag){
      	privateString = " AND (emailmessage.private ='NO' OR emailmessage.owner="+userId+" ) ";
      }
	  
      System.out.println(privateString+"privateString");
      String folderString = "";
      if (folderID.intValue() != 0){
        folderString = " AND es.emailfolder=" + folderID.intValue();
      }

      String strSQL = "SELECT emailmessage.messageid, emailmessage.messagedate, emailmessage.mailfrom, emailmessage.replyto, emailmessage.subject, emailmessage.size, " +
                      "emailmessage.Priority, emailmessage.messageRead AS readstatus, es.folderID AS EmailFolder, " +
                      "COUNT(at.attachmentid) AS noofattachments " +
                      "FROM emailmessage LEFT JOIN emailmessagefolder es ON (emailmessage.messageid=es.messageID) " +
                      "LEFT JOIN attachment at ON (emailmessage.messageID=at.messageid) " +
                      "WHERE emailmessage.locallyDeleted = 'NO' "+privateString+" AND (emailmessage.MovedToFolder=" + folderID.intValue() +
                      " OR emailmessage.MovedToFolder=0) AND es.folderid=" + folderID.intValue() + "  GROUP BY emailmessage.messageid " +
                      "ORDER BY " + sortmem + sortStr + " ";

      if ((null != searchString) && (searchString.startsWith("ADVANCE:")))
      {
        int searchIndex = (searchString.toUpperCase()).indexOf("WHERE");
      	strSQL = "SELECT emailmessage.messageid, emailmessage.messagedate, emailmessage.mailfrom, emailmessage.replyto, emailmessage.subject, emailmessage.size, " +
				  "emailmessage.Priority, emailmessage.messageRead AS readstatus, es.folderID AS EmailFolder, " +
				  "COUNT(at.attachmentid) AS noofattachments " +
				  "FROM emailmessage LEFT JOIN emailmessagefolder es ON (emailmessage.messageid=es.messageID) " +
				  "LEFT JOIN attachment at ON (emailmessage.messageID=at.messageid) " +
				  "WHERE emailmessage.locallyDeleted = 'NO' "+privateString+" AND "+ searchString.substring((searchIndex+5),searchString.length())+"  GROUP BY emailmessage.messageid " +
				  "ORDER BY " + sortmem + sortStr + " ";
      }

      if ((null != searchString) && (searchString.startsWith("SIMPLE :")))
      {
        searchString = searchString.substring(8);
        appendStr = " where 1=1 AND (emailmessage.messageid like '%" + searchString + "%' OR emailmessage.messagedate like '%" + searchString + "%' OR emailmessage.mailfrom like '%" + searchString + "%' OR emailmessage.subject like '%h%' OR emailmessage.size like '%" + searchString + "%') and ";
        strSQL = strSQL.replaceFirst("where ", appendStr);
      }


      // ATTENTION!!!!! THE FOLLOWING CODE IS A **HACK* IN ORDER
      // TO GET RELATED INFO EMAIL HISTORY TO WORK!!! THIS IS NO
      // WAY THE BEST METHOD FOR COMPLETING THIS FUNCTIONALITY,
      // BUT TIME AND RESOURCES NECESSITATE THIS TYPE OF "GUERILLA"
      // PROGRAMMING. PLEASE DO NOT ATTEMPT TO EXTEND THE FUNCTIONALITY
      // OF THE FOLLOWING CODE; INSTEAD, SPEAK WITH YOUR PROJECT
      // MANAGER AND TELL THEM THIS PIECE OF FUNCTIONALITY NEEDS
      // TO BE RE-WRITTEN IN A MORE MANAGE-ABLE AND SCALABLE MANNER.
      if ((searchString != null) && (searchString.startsWith("ADVANCED-RELATED:")))
      {
        searchString = searchString.substring(17);
        strSQL = "SELECT emailmessage.messageid, emailmessage.messagedate, emailmessage.mailfrom, " +
                 "emailmessage.replyto, emailmessage.subject, emailmessage.size, emailmessage.Priority, " +
                 "emailmessage.messageRead AS readstatus, es.folderID AS EmailFolder, COUNT(at.attachmentid) AS noofattachments " +
                 "FROM emailmessagefolder es LEFT JOIN emailmessage  ON " +
                 "(es.messageid=emailmessage.messageid) LEFT OUTER JOIN " +
                 "attachment at ON (emailmessage.messageid=at.messageid) WHERE " +
                 searchString + " AND emailmessage.locallyDeleted = 'NO' "+privateString+" AND (emailmessage.MovedToFolder=" + folderID.intValue() +
                 " OR emailmessage.MovedToFolder=0) GROUP BY emailmessage.messageid ORDER BY " +
                 sortmem + sortStr + " ";
                 //+ " LIMIT " + (beginindex - 1) + " ," + (endindex + 1);
      }
      // END REALLY BAD METHOD FOR ACHEIVING FUNCTIONALITY


      cvdl.setSqlQuery(strSQL);

      Collection col = cvdl.executeQuery();
      Iterator it = col.iterator();
      int i = 0;

      while (it.hasNext())
      {
        i++;
        HashMap hm = (HashMap)it.next();
        int messageID = ((Long)hm.get("messageid")).intValue();
        StringMember subject;
        StringMember fromAddress;
        StringMember toAddress;
        StringMember messageSize;
        StringMember attachmentNumber;
        StringMember readIndication;
        StringMember priority;
        DateMember dateReceived;
        IntMember intmem = new IntMember("messageid", messageID, 10, "URL", 'T', false, 10);
        int emailFolderIDx = ((Long)hm.get("EmailFolder")).intValue();
        IntMember emailFolderID = new IntMember("EmailFolder", emailFolderIDx, 10, "URL", 'T', false, 10);

        if (hm.get("subject") != null)
        {
          subject = new StringMember("Subject", (String) hm.get("subject"), 10, "", 'T', true);
        }else{
          subject = new StringMember("Subject", "", 10, "", 'T', true);
        }

        if (hm.get("mailfrom") != null)
        {
          String formattedAddress = (String)hm.get("mailfrom");
          formattedAddress = formattedAddress.replaceAll("<", "&lt;");
          formattedAddress = formattedAddress.replaceAll(">", "&gt;");
          fromAddress = new StringMember("From", formattedAddress, 10, "URL", 'T', true);
        }else{
          fromAddress = new StringMember("From", "", 10, "URL", 'T', true);
        }

        toAddress = new StringMember("To", getTOList(messageID), 10, "URL", 'T', true);
        if (hm.get("messagedate") != null)
        {
          dateReceived = new DateMember("Received", (java.util.Date) hm.get("messagedate"), 10, "URL", 'T', false, 100, "EST");
        }else{
          dateReceived = new DateMember("Received", (new java.util.Date()), 10, "URL", 'T', false, 100, "EST");
        }

        /*
         * Determine KB, MB, or GB for email size.
         */
        if (hm.get("size") != null)
        {
          float size = ((Integer)hm.get("size")).floatValue();
          DecimalFormat df = new DecimalFormat("###.#");
          Integer s = new Integer((int)size);
          String sizeStr = s.toString();
          if (size > 1000000000)
          {
            size /= 1073741824;					// 1GB
            sizeStr = df.format(size) + "GB";
          } else if (size > 1000000) {
            size /= 1048576;						// 1MB
            sizeStr = df.format(size) + "MB";
          } else if (size > 1000) {
            size /= 1024;               // 1KB
            sizeStr = df.format(size) + "KB";
          }
          messageSize = new StringMember("Size", sizeStr, 10, "URL", 'T', false);
        }else{
          messageSize = new StringMember("Size", "1KB", 10, "URL", 'T', false);
        }

        if (hm.get("noofattachments") != null)
        {
          attachmentNumber = new StringMember("AttIndication", "" + ((Number)hm.get("noofattachments")).intValue(), 10, "URL", 'T', false);
        }else{
          attachmentNumber = new StringMember("AttIndication", "", 10, "URL", 'T', false);
        }

        if (hm.get("readstatus") != null)
        {
          readIndication = new StringMember("ReadIndication", (String) hm.get("readstatus"), 10, "URL", 'T', true);
        }else{
          readIndication = new StringMember("ReadIndication", "", 10, "URL", 'T', true);
        }

        if (hm.get("Priority") != null)
        {
          priority = new StringMember("Priority", (String) hm.get("Priority"), 10, "URL", 'T', true);
        }else{
          priority = new StringMember("Priority", "MEDIUM", 10, "URL", 'T', true);
        }


        EmailListElement ele = new EmailListElement(messageID);
        ele.put("MessageID", intmem);
        ele.put("Subject", subject);
        ele.put("From", fromAddress);
        ele.put("To", toAddress);
        ele.put("Received", dateReceived);
        ele.put("Size", messageSize);
        ele.put("AttIndication", attachmentNumber);
        ele.put("ReadIndication", readIndication);
        ele.put("Priority", priority);
        ele.put("EmailFolder", emailFolderID);

        StringBuffer sb = new StringBuffer("00000000000");
        sb.setLength(11);
        String str = (new Integer(i)).toString();
        sb.replace((sb.length() - str.length()), (sb.length()), str);
        String newOrd = sb.toString();

        emailList.put(newOrd, ele);
        emailList.setTotalNoOfRecords(emailList.size());
        emailList.setListType("Email");
        emailList.setBeginIndex(beginindex);
        emailList.setEndIndex(endindex);
      }   // end while(it.hasNext())
      cvdl.clearParameters();
    }catch(Exception e){
    	System.out.println("[Exception][EmailEJB.getEmailList] Exception Thrown: "+e);
      e.printStackTrace();
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    if (emailList == null)
    {
      emailList = new EmailList();
    }
    return emailList;
  }   // end getEmailList() method

  /**
   * Gets the tOList attribute of the EmailEJB object
   *
   * @param id  The EmailID of the email to get the TO addresses.
   * @return    The tOList value
   */
  private String getTOList(int id)
  {
    CVDal cvdlto    = new CVDal(dataSource);
    Collection vto  = null;
    cvdlto.setSql("email.getmailaddressTO");
    cvdlto.setInt(1, id);

    vto = cvdlto.executeQuery();
    cvdlto.clearParameters();
    cvdlto.destroy();
    Iterator itto   = vto.iterator();

    String addto    = null;
    while (itto.hasNext())
    {
      HashMap hmto  = (HashMap) itto.next();
      addto = (String)hmto.get("Address");
      addto = addto.replaceAll("<", "&lt;");
      addto = addto.replaceAll(">", "&gt;");
    }
    return addto;
  }


  /**
   * This method gives RuleList from Database.
   * The hashmap looks for these elements:
   * <ul>
   *   <li> startATparam:
   *   <li> EndAtparam:
   *   <li> searchString:
   *   <li> sortmem:
   *   <li> sortType:
   * </ul>
   *
   * @param userId      The userID (owner) of the fule
   * @param preference  A HashMap with the above elements.
   * @return            The ruleList value
   */
  public RuleList getRuleList(int userId, HashMap preference)
  {

    Integer startATparam  = (Integer) preference.get("startATparam");
    Integer EndAtparam    = (Integer) preference.get("EndAtparam");
    String searchString   = (String) preference.get("searchString");
    String sortmem        = (String) preference.get("sortmem");
    Character chr         = (Character) preference.get("sortType");
    //String typeoflist =	(String)preference.get( "typeoflist" );

    String timezoneid     = "IST";

    char sorttype         = chr.charValue();
    int startat           = startATparam.intValue();
    int endat             = EndAtparam.intValue();
    int begainindex       = Math.max(startat - 100, 1);
    int endindex          = endat + 100;

    RuleList DL           = new RuleList();
    DL.setSortMember(sortmem);
    CVDal cvdl            = new CVDal(dataSource);

    if (searchString == null)
    {
      searchString = "";
    }



    String appendStr      = "";
    if (searchString.startsWith("SIMPLE :"))
    {
      searchString = searchString.substring(8);
      appendStr = " WHERE emailrules.ruleid like '%" + searchString + "%'   OR rulename like '%" + searchString + "%' OR description like  '%" + searchString + "%' OR enabledstatus like  '%" + searchString + "%' ";
    }

	if ((null != searchString) && (searchString.startsWith("ADVANCE:")))
	{
		int searchIndex = (searchString.toUpperCase()).indexOf("WHERE");
		appendStr = " AND "+ searchString.substring((searchIndex+5),searchString.length())+" ";
	}

    if (sorttype == 'A')
    {
      String str  = "Select ruleid , rulename, description , enabledstatus from emailrules ,emailaccount where emailrules.accountid=emailaccount.accountid and emailaccount.owner="+userId+" " + appendStr + " ORDER BY " + sortmem + " LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";
      cvdl.setSqlQuery(str);
    }
    else
    {
      String str1  = "Select ruleid , rulename, description , enabledstatus from emailrules ,emailaccount where emailrules.accountid=emailaccount.accountid and emailaccount.owner="+userId+" " + appendStr + " ORDER BY " + sortmem + " DESC LIMIT " + (begainindex - 1) + " , " + (endindex + 1) + ";";
      cvdl.setSqlQuery(str1);
    }

    Collection v = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.destroy();

    Iterator it           = v.iterator();
    TimeZone tz           = TimeZone.getTimeZone("IST");
    int i                 = 0;

    while (it.hasNext())
    {
      i++;
      HashMap hm           = (HashMap) it.next();

      int RuleID           = ((Long) hm.get("ruleid")).intValue();
      IntMember intmem     = new IntMember("RuleID", RuleID, 10, "", 'T', true, 10);
      StringMember one     = new StringMember("RuleName", (String) hm.get("rulename"), 10, "", 'T', true);
      StringMember two     = new StringMember("Description", (String) hm.get("description"), 10, "", 'T', false);
      StringMember three   = new StringMember("EnabledStatus", (String) hm.get("enabledstatus"), 10, "", 'T', false);

      //BooleanMember three= new BooleanMember( "Enabled", true ,10 , "" , 'T' , false ,20  );

      RuleListElement ele  = new RuleListElement(RuleID);

      ele.put("RuleID", intmem);
      ele.put("RuleName", one);
      ele.put("Description", two);
      ele.put("EnabledStatus", three);

      //added by Sameer for generating fixed length sort key from i
      StringBuffer sb      = new StringBuffer("00000000000");
      sb.setLength(11);
      String str           = (new Integer(i)).toString();
      sb.replace((sb.length() - str.length()), (sb.length()), str);
      String newOrd        = sb.toString();

      DL.put(newOrd, ele);
      DL.setTotalNoOfRecords(DL.size());
      DL.setListType("Rule");
      DL.setBeginIndex(begainindex);
      DL.setEndIndex(endindex);
    }
    return DL;
  }


  /**
   * this method enableordisableRule
   *
   * @param userid     userID of the owner of the rule
   * @param elementid  The rule to enable/diable
   * @param status     true to enable the rule/false to disable
   * @return           Returns 0.
   */
  public int enableordisableRule(int userid, int elementid, boolean status)
  {
    try
    {
      CVDal cvdl  = new CVDal(dataSource);
      cvdl.setSql("email.enablerule");

      if (status == true)
      {
        cvdl.setString(1, "YES");
      }
      else
      {
        cvdl.setString(1, "NO");
      }

      cvdl.setInt(2, elementid);
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.destroy();
    }
    catch (Exception e)
    {
    	System.out.println("[Exception][EmailEJB.enableordisableRule] Exception Thrown: "+e);
      e.printStackTrace();
    }
    return 0;
  }


  /**
   * this method delete rule
   *
   * @param userid     The userID (owner) of the rule to be deleted.
   * @param elementid  The ID of the rule to be deleted.
   * @return           Returns 0.
   */
  public int deleteRule(int userid, int elementid)
  {
    try
    {
      CVDal cvdl  = new CVDal(dataSource);
      cvdl.setSql("email.deleterule");
      cvdl.setInt(1, elementid);
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.destroy();
    }
    catch (Exception e)
    {
    	System.out.println("[Exception][EmailEJB.deleteRule] Exception Thrown: "+e);
      e.printStackTrace();
    }
    return 0;
  }

  /**
   * Returns a list of emails for display in the Related Info
   * Email module. Will return all emails which have a To: or
   * From: field which matches <strong>any</strong> address
   * related to the given <em>entityID</em>, including all
   * addresses related to any Individual related to the Entity.
   * @param searchCondition The searchCondition of Entity Or Individuals for which we are retreiving related emails.
   * @param individualID The individualID of the user retreiving the list.
   * @return EmailList object (DisplayList) containing records found.
   */
  public EmailList getRelatedEmailList(HashMap searchCondition, int individualID)
  {
    EmailList emailList = new EmailList();
    CVDal cvdl = new CVDal(this.dataSource);

    try
    {
	  String sortmember = (String)searchCondition.get("sortmem");
      String sortmem = (String)searchCondition.get("sortmem");
      Character chr = (Character)searchCondition.get("sortType");
      String contactType = (String)searchCondition.get("contactType");
      String contactID = (String)searchCondition.get("contactID");

      if (sortmem.equals("From"))
      {
        sortmem = "em.MailFrom";
      }

      if (sortmem.equals("To"))
      {
        sortmem = "em.ReplyTo";
      }

      if (sortmem.equals("Received"))
      {
        sortmem = "em.MessageID";
      }

      if (sortmem.equals("Subject"))
      {
        sortmem = "em.Subject";
      }

      char sorttype  = chr.charValue();
      String sortType = "";
      if (sorttype == 'D')
      {
        sortType = " DESC ";
      }

      String sqlQuery = "";
      if (contactType != null && contactType.equals("1"))
      {
        sqlQuery = "SELECT em.MessageID, em.Subject, em.MessageDate, em.MailFrom, em.ReplyTo, " +
                   "em.Size, em.Priority, em.MessageRead AS readstatus, es.FolderID AS EmailFolder FROM emailmessage em " +
                   "LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID) LEFT JOIN  " +
                   "emailmessagefolder es ON (em.MessageID = es.MessageID) LEFT JOIN  " +
                   "methodofcontact moc ON (er.Address like concat('%',moc.Content,'%')) LEFT JOIN  " +
                   "mocrelate mr ON (moc.MOCID=mr.MOCID) LEFT JOIN individual i  " +
                   "ON (mr.ContactID=i.IndividualID) WHERE mr.ContactType=2  " +
                   "AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND i.Entity="+contactID+" UNION  " +
                   "SELECT em.MessageID, em.Subject, em.MessageDate, em.MailFrom,  " +
                   "em.ReplyTo, em.Size, em.Priority, em.MessageRead AS readstatus,  " +
                   "es.FolderID AS EmailFolder FROM emailmessage em  " +
                   "LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID)  " +
                   "LEFT JOIN emailmessagefolder es ON (em.MessageID = es.MessageID)  " +
                   "LEFT JOIN methodofcontact moc ON (er.Address like concat('%',moc.Content,'%'))  " +
                   "LEFT JOIN mocrelate mr ON (moc.MOCID=mr.MOCID)  " +
                   "WHERE mr.ContactType=1 AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND " +
                   "mr.ContactID="+contactID+" order by "+sortmem+sortType;
      }else{
        sqlQuery = "SELECT em.MessageID, em.Subject, em.MessageDate, em.MailFrom, " +
                   "em.ReplyTo, em.Size, em.Priority, em.MessageRead AS readstatus, es.FolderID AS EmailFolder " +
                   "FROM emailmessage em LEFT JOIN emailrecipient er ON (em.MessageID=er.MessageID) " +
                   "LEFT JOIN emailmessagefolder es ON (em.MessageID = es.MessageID) " +
                   "LEFT JOIN methodofcontact moc ON (er.Address like concat('%',moc.Content,'%')) " +
                   "LEFT JOIN mocrelate mr ON (moc.MOCID=mr.MOCID) " +
                   "WHERE mr.ContactType="+contactType+" AND moc.MOCType=1 AND es.FolderID IS NOT NULL AND " +
                   "mr.ContactID="+contactID+" order by "+sortmem+sortType;
      }

      cvdl.setSqlQuery(sqlQuery);

      Collection sqlResults = cvdl.executeQuery();

      if (sqlResults != null)
      {
        Iterator iter = sqlResults.iterator();
        int i = 0;
        while (iter.hasNext())
        {
          i++;
          HashMap sqlRow = (HashMap)iter.next();

          int messageID = ((Long)sqlRow.get("MessageID")).intValue();

          IntMember intmem = new IntMember("messageid", messageID, 10, "URL", 'T', false, 10);
          int emailFolderIDx = ((Long)sqlRow.get("EmailFolder")).intValue();
          IntMember emailFolderID = new IntMember("EmailFolder", emailFolderIDx, 10, "URL", 'T', false, 10);

          StringMember subject = new StringMember("Subject", (String) sqlRow.get("Subject"), 10, "", 'T', true);
          StringMember fromAddress = new StringMember("From", (String) sqlRow.get("MailFrom"), 10, "URL", 'T', true);
          StringMember toAddress = new StringMember("To", getTOList(messageID), 10, "URL", 'T', true);
          DateMember dateReceived = new DateMember("Received", (java.util.Date) sqlRow.get("MessageDate"), 10, "URL", 'T', false, 100, "EST");

          StringMember messageSize = null;
          if (sqlRow.get("Size") != null)
          {
            messageSize = new StringMember("Size", ((Number)sqlRow.get("Size")).toString() + " kb", 10, "URL", 'T', false);
          }else{
            messageSize = new StringMember("Size", "", 10, "URL", 'T', false);
          }
          StringMember attachmentNumber = new StringMember("AttIndication", "0", 10, "URL", 'T', false);
          StringMember readIndication = new StringMember("ReadIndication", (String) sqlRow.get("readstatus"), 10, "URL", 'T', true);
          StringMember priority = new StringMember("Priority", (String) sqlRow.get("Priority"), 10, "URL", 'T', true);

			  EmailListElement ele = new EmailListElement(messageID);

			  ele.put("MessageID", intmem);
			  ele.put("Subject", subject);
			  ele.put("From", fromAddress);
			  ele.put("To", toAddress);
			  ele.put("Received", dateReceived);
			  ele.put("Size", messageSize);
			  ele.put("AttIndication", attachmentNumber);
			  ele.put("ReadIndication", readIndication);
			  ele.put("Priority", priority);
			  ele.put("EmailFolder", emailFolderID);

			  StringBuffer sb = new StringBuffer("00000000000");
			  sb.setLength(11);
			  String str = (new Integer(i)).toString();
			  sb.replace((sb.length() - str.length()), (sb.length()), str);
			  String newOrd = sb.toString();

			  emailList.put(newOrd, ele);
			}   // end while (iter.hasNext())
			emailList.setTotalNoOfRecords(emailList.size());
			emailList.setListType("Email");
			emailList.setBeginIndex(1);
			emailList.setEndIndex(emailList.size());
			emailList.setStartAT(1);
			emailList.setEndAT(emailList.size());
			emailList.setSortMember(sortmember);
			emailList.setSortType(sorttype);
		}   // end if (sqlResults != null)
    }catch(Exception e){
      e.printStackTrace();    // TODO: remove stacktrace
    }finally{
      cvdl.clearParameters();
      cvdl.destroy();
      cvdl = null;
    }
    return(emailList);
  }   // end getRelatedEmailList() method



	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

}

