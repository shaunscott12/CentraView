/*
 * $RCSfile: MailEJB.java,v $    $Revision: 1.9 $  $Date: 2005/10/17 18:48:49 $ - $Author: mcallist $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.administration.applicationsettings.AppSettingsLocal;
import com.centraview.administration.applicationsettings.AppSettingsLocalHome;
import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.emailsettings.EmailSettingsLocal;
import com.centraview.administration.emailsettings.EmailSettingsLocalHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.CentraViewConfiguration;
import com.centraview.common.DDNameValue;
import com.centraview.common.DisplayList;
import com.centraview.common.EJBUtil;
import com.centraview.common.Html2Text;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.helper.CommonHelperLocal;
import com.centraview.common.helper.CommonHelperLocalHome;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.email.RuleList;
import com.centraview.email.RuleListElement;
import com.centraview.file.CvFileException;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileLocal;
import com.centraview.file.CvFileLocalHome;
import com.centraview.file.CvFileVO;
import com.centraview.file.CvFolderVO;
import com.centraview.support.ticket.ThreadVO;
import com.centraview.support.ticket.TicketLocal;
import com.centraview.support.ticket.TicketLocalHome;
import com.centraview.support.ticket.TicketVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Folder;

/**
 * This EJB handles all Database/JavaMail related issues with the Mail
 * client in CentraView. There is really no need to have 5 EJBs to
 * handle the Mail related operations. This one EJB should do it with
 * much less overhead.
 * @since 1.0.8
 * @author CentraView, LLC.
 */
public class MailEJB implements SessionBean
{
  /** The SessionContext of this SessionBean. */
  protected SessionContext ctx;
  private String dataSource = "";
  private static Logger logger = Logger.getLogger(MailEJB.class);

  public void setSessionContext(SessionContext ctx) throws EJBException
  {
    this.ctx = ctx;
  }

  public void ejbActivate() {}
  public void ejbRemove() {}
  public void ejbPassivate() {}
  public void ejbCreate() {}

  /**
   * This simply sets the target datasource to be used
   * for DB interaction.
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  } //end of setDataSource method

  /**
   * Returns a MailFolderVO object based on the
   * emailFolderID passed into the method. If the
   * folder is not found, an empty MailFolderVO (with
   * a FolderID of -1) is returned.
   * @param emailFolderID The ID of the Mail Folder.
   * @return A populated MailFolderVO if the folder exists.
   */
  public MailFolderVO getEmailFolder(int emailFolderID)
  {
    MailFolderVO mailFolderVO = new MailFolderVO();
    mailFolderVO.setFolderID(-1);
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      mailFolderVO = this.getEmailFolder(emailFolderID, cvdal);
    }catch(Exception e){
      logger.error("[getEmailFolder]: Exception", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return mailFolderVO;
  }   // end getEmailFolder(int) method

  /**
   * Returns a MailFolderVO object based on the
   * emailFolderID passed into the method. If the
   * folder is not found, an empty MailFolderVO (with
   * a FolderID of -1) is returned. <p>
   * @param emailFolderID The ID of the Mail Folder.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A populated MailFolderVO if the folder exists.
   * @throws Exception Something went wrong during execution.
   */
  private MailFolderVO getEmailFolder(int emailFolderID, CVDal cvdal)
  {
    MailFolderVO mailFolderVO = new MailFolderVO();
    mailFolderVO.setFolderID(-1);

    try {
      String queryString = "SELECT ef.FolderID, ef.Parent, ef.AccountID, ef.Name, ef.Ftype, ef.FullName, " +
                           "count(em.MessageID) TotalMessage,count(emc.MessageID) ReadMessage " +
                           "FROM emailfolder ef " +
                           "LEFT JOIN emailmessagefolder emf  ON ef.FolderID=emf.FolderID " +
                           "LEFT JOIN emailmessage em ON emf.MessageID=em.MessageID " +
                           "LEFT JOIN emailmessage emc ON (emf.MessageID=emc.MessageID and emc.messageread='NO') " +
                           "WHERE ef.FolderID=? group by ef.FolderID ";

      cvdal.setSqlQuery(queryString);
      cvdal.setInt(1, emailFolderID);
      Collection resultsCollection = cvdal.executeQuery();
      cvdal.clearParameters();

      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number accountID = (Number) resultsHashMap.get("AccountID");
          Number parentID = (Number) resultsHashMap.get("Parent");
          Number totalMessage = (Number) resultsHashMap.get("TotalMessage");
          Number readMessage = (Number) resultsHashMap.get("ReadMessage");

          // find out if there are sub-folders
          boolean hasSubFolders = false;
          cvdal.setSqlQuery("SELECT folderID from emailfolder WHERE parent=?");
          cvdal.setInt(1, emailFolderID);
          Collection emptyResults = cvdal.executeQuery();
          if (emptyResults != null) {
            Iterator emptyIter = emptyResults.iterator();
            if (emptyIter.hasNext()) {
              hasSubFolders = true;
            }
          }
          cvdal.clearParameters();

          mailFolderVO.setEmailAccountID(accountID.intValue());
          mailFolderVO.setFolderID(emailFolderID);
          mailFolderVO.setParentID(parentID.intValue());
          mailFolderVO.setFolderName((String) resultsHashMap.get("Name"));
          mailFolderVO.setFolderType((String) resultsHashMap.get("Ftype"));
          mailFolderVO.setFolderFullName((String) resultsHashMap.get("FullName"));
          mailFolderVO.setCountReadMessage(readMessage.intValue());
          mailFolderVO.setTotalMessage(totalMessage.intValue());
          mailFolderVO.setSubFolders(hasSubFolders);
        }
      }
    } finally {
      cvdal.setSqlQueryToNull();
    }
    return mailFolderVO;
  }

  /**
   * Returns a MailFolderVO object based on the
   * accountID and folderName passed into the method. If the
   * folder is not found, an empty MailFolderVO (with
   * a FolderID of -1) is returned.
   * @param accountID The Account ID that the Mail Folder belongs to.
   * @param folderName The name of the Mail Folder being searched for.
   * @return A populated MailFolderVO if the folder exists.
   */
  public MailFolderVO getEmailFolderByName(int accountID, String folderName)
  {
    MailFolderVO mailFolderVO = new MailFolderVO();
    mailFolderVO.setFolderID(-1);
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      mailFolderVO = this.getEmailFolderByName(accountID, folderName, cvdal);
    }catch (Exception e){
      logger.error("[getEmailFolderByName]: Exception", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return mailFolderVO;
  }

  /**
   * Returns a MailFolderVO object based on the
   * accountID and folderName passed into the method. If the
   * folder is not found, an empty MailFolderVO (with
   * a FolderID of -1) is returned. <p>
   * @param accountID The Account ID that the Mail Folder belongs to.
   * @param folderName The name of the Mail Folder being searched for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A populated MailFolderVO if the folder exists.
   * @throws Exception Something went wrong during execution.
   */
  private MailFolderVO getEmailFolderByName(int accountID, String folderName, CVDal cvdal) throws Exception
  {
    MailFolderVO mailFolderVO = new MailFolderVO();
    mailFolderVO.setFolderID(-1);

    try {
      String queryString = "SELECT FolderID, Parent, AccountID, Name, Ftype, FullName FROM emailfolder " +
          "WHERE AccountID=? AND Name=?";
      cvdal.setSqlQuery(queryString);
      cvdal.setInt(1, accountID);
      cvdal.setString(2, folderName);
      Collection resultsCollection = cvdal.executeQuery();

      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number folderID = (Number) resultsHashMap.get("FolderID");
          Number parentID = (Number) resultsHashMap.get("Parent");

          mailFolderVO.setEmailAccountID(accountID);
          mailFolderVO.setFolderID(folderID.intValue());
          mailFolderVO.setParentID(parentID.intValue());
          mailFolderVO.setFolderName((String) resultsHashMap.get("Name"));
          mailFolderVO.setFolderType((String) resultsHashMap.get("Ftype"));
          mailFolderVO.setFolderFullName((String) resultsHashMap.get("FullName"));
        }
      }
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return mailFolderVO;
  }

  /**
   * Returns a ArrayList of the folderID available for an account.
   * The resulting ArrayList contains list of folderID.
   * @param accountID The AccountID to get the list of folders for.
   * @return A ArrayList with a list of FolderID as Key pairs.
   */
  public ArrayList getFolderIDs(int accountID)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    ArrayList folderList = new ArrayList();
    try {
      String selectQuery = "SELECT FolderID FROM emailfolder WHERE AccountID=? ORDER BY Parent, Name, FolderID";
      cvdal.setSqlQuery(selectQuery);
      cvdal.setInt(1, accountID);
      Collection results = cvdal.executeQuery();
      
      
      if (results != null) {
        Iterator resultsIterator = results.iterator();
        while (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number folderID = (Number) resultsHashMap.get("FolderID");
          folderList.add(folderID);
        }
      }
    } finally {
      cvdal.destroy();
    }
    return folderList;
  }   // end getFolderList(int,CVDal) method


  /**
   * Returns a HashMap of the folders available for an account.
   * The resulting HashMap contains the folderID as the key and
   * the name as the value.
   * @param accountID The AccountID to get the list of folders for.
   * @return A HashMap with FolderID/Name as the Key/Value pairs.
   */
  public HashMap getFolderList(int accountID)
  {
    HashMap folderList = new HashMap();
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      folderList = this.getFolderList(accountID, cvdal);
    }catch(Exception e){
      logger.error("[getFolderList] Exception thrown.", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return folderList;
  }

  /**
   * Returns a HashMap of the folders available for an account.
   * The resulting HashMap contains the folderID as the key and
   * the name as the value.
   * @param accountID The AccountID to get the list of folders for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A HashMap with FolderID/Name as the Key/Value pairs.
   * @throws Exception Something went wrong during the execution of this method. Catch it!
   */
  private HashMap getFolderList(int accountID, CVDal cvdal) throws Exception
  {
    HashMap folderList = new HashMap();

    String selectQuery = "SELECT FolderID, Name FROM emailfolder WHERE AccountID=? ORDER BY Parent, Name, FolderID";
    cvdal.setSqlQuery(selectQuery);
    cvdal.setInt(1, accountID);
    Collection results = cvdal.executeQuery();
    if (results != null) {
      Iterator resultsIterator = results.iterator();
      while (resultsIterator.hasNext()) {
        HashMap resultsHashMap = (HashMap) resultsIterator.next();
        Number folderID = (Number) resultsHashMap.get("FolderID");
        String folderName = (String) resultsHashMap.get("Name");

        folderList.put(folderID, folderName);
      }
    }
    return folderList;
  }

  /**
   * Deletes folder and any messages inside that folder.
   * @param individualID The individual deleting the folder.
   * @param accountID The account of the folder being deleted.
   * @param folderID The folder ID of the folder being deleted.
   * @return The number of folders deleted (should always be 0 or 1).
   */
  public int deleteFolder(int individualID, int accountID, int folderID)
  {
    int numberDeleted = -1;
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      numberDeleted = this.deleteFolder(individualID, accountID, folderID, cvdal);
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.deleteFolder: " + e.toString());
      //e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return numberDeleted;
  }   // end deleteFolder(int,int,int) method

  /**
   * Deletes folder and any messages inside that folder.
   * @param individualID The individual deleting the folder.
   * @param accountID The account of the folder being deleted.
   * @param folderID The folder ID of the folder being deleted.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of folders deleted (should always be 0 or 1).
   * @throws Exception
   */
  private int deleteFolder(int individualID, int accountID, int folderID, CVDal cvdal) throws Exception
  {
    int numberDeleted = -1;

    try {
      // first, if this is an IMAP folder, delete the folder from the remote server
      MailAccountVO accountVO = this.getMailAccountVO(accountID);
      String accountType = accountVO.getAccountType();

      // if the account type is IMAP, then we need to change the folder remotely, too
      boolean imapFolderDeleted = true;
      if (accountType != null && accountType.equals(MailAccountVO.IMAP_TYPE)) {
        imapFolderDeleted = this.deleteImapFolder(this.getEmailFolder(folderID), accountVO, cvdal);
      }

      // only continue if the remote folder was successfully removed...
      if (! imapFolderDeleted) {
        return numberDeleted;
      }

      String deleteFolderString = "DELETE FROM emailfolder WHERE FolderID=?";
      cvdal.setSqlQuery(deleteFolderString);
      cvdal.setInt(1, folderID);
      numberDeleted = cvdal.executeUpdate();
    }finally{
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();
    }
    return numberDeleted;
  }   // end deleteFolder(in,int,int,CVDal) method


  /**
   * Deletes the specified IMAP folder from the remote server. The info
   * in <code>accountVO</code> is used to connect to the remote server.
   * @param folderVO A fully populated MailFolderVO object representing the
   *        folder to be deleted.
   * @param accountVO A MailAccountVO object with details of the mail server
   *        that we'll connect to.
   * @param cvdal An <strong>open</strong> database connection.
   * @return boolean True if the remote folder is deleted, false otherwise.
   */
  private boolean deleteImapFolder(MailFolderVO folderVO, MailAccountVO accountVO, CVDal cvdal)
  {
    boolean folderDeleted = false;
    Store store = null;
    Folder remoteFolder = null;

    try {
      // open the remote mail server...
      Properties properties = System.getProperties();
      Session session = Session.getDefaultInstance(properties);
      store = session.getStore(accountVO.getAccountType());
      store.connect(accountVO.getMailServer(), accountVO.getLogin(), accountVO.getPassword());

      // get the full path to the folder
      int parentID = folderVO.getParentID();
      String parentFolderName = this.getFolderFullPathString(parentID, false, cvdal); // false == don't include "/root"

      // get the remote folder (that hasn't changed yet). We'll need
      // a handle to the remote folder in order to delete it..
      parentFolderName = parentFolderName.substring(1, parentFolderName.length());    // strip the leading "/"
      remoteFolder = store.getFolder(parentFolderName + "/" + folderVO.getFolderName());

      if (remoteFolder != null && remoteFolder.exists()) {
        // we can only delete the remote folder if it exists on the remote server...
        folderDeleted = remoteFolder.delete(false);   // false == do NOT recursively delete
      }
    }catch(MessagingException me){
      // TODO: handle the MessagingException
      me.printStackTrace();
    }finally{
      // Clean up all open handles...
      if (remoteFolder != null && remoteFolder.isOpen()){
        try { remoteFolder.close(false); } catch (Exception e) {}
      }
      if (store != null && store.isConnected()){
        try { store.close(); } catch (Exception e){}
      }
    }
    return folderDeleted;
  }


  /**
   * Deletes folder and any messages inside that folder from the IMAP
   * Server. This method does not do anything to the local server.
   * @param mailAccountVO The mail account information such as
   * username, password, server, etc...
   * @param folderName The name of the folder to delete.
   * @return True if the folder was deleted, false if it wasn't.
   */
  public boolean deleteFolderFromIMAPServer(MailAccountVO mailAccountVO, String folderName)
  {
    boolean deletedFromServer = false;

    Store store = null;
    Folder folder = null;

    try {
      Properties properties = System.getProperties();

      //TODO: Add the secure IMAP connection stuff in here.
      //Look at http://www.javaworld.com/javatips/jw-javatip115_p.html
      Session session = Session.getDefaultInstance(properties);
      store = session.getStore(mailAccountVO.getAccountType());

      store.connect(mailAccountVO.getMailServer(), mailAccountVO.getLogin(), mailAccountVO.getPassword());

      folder = store.getFolder(folderName);

      if (folder.exists()) {
        if (folder.isSubscribed()) {
          folder.setSubscribed(false);
        }
        deletedFromServer = folder.delete(true);
      }   // end if (folder.exists())
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] deleteFolderFromIMAPServer(MailAccountVO,String): " + e);
      //e.printStackTrace();
    }finally{
      if (folder != null) {
        try {
          folder.close(false);
        }catch(Exception e){
          //Can't really do anything at this point
        }
      }   // end if (folder != null)

      if (store != null) {
        try {
          store.close();
        }catch(Exception e){
          //Can't really do anything at this point
        }
      }   // end if (store != null)
    }   // end finally block
    return deletedFromServer;
  }   // end deleteFolderFromIMAPServer(MailAccountVO,String) method

  /**
   * Adds a relationship between an email message and a new folder.
   * @param individualID The individual who is moving the folder.
   * @param messageID The message to be moved.
   * @param newFolderID The new folderID.
   * @return The number of messages moved (should always be 1 or 0).
   */
  public int moveMessageToFolder(int individualID, int messageID, int newFolderID)
  {
    int numberMoved = 0;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      // get old folder ID and the server type of the email message
      MailMessageVO messageVO = this.getEmailMessageVO(individualID, messageID);
      MailAccountVO accountVO = this.getMailAccountVO(messageVO.getEmailAccountID());

      int oldFolderID = messageVO.getEmailFolderID();
      String serverType = accountVO.getAccountType();

      if (serverType.equalsIgnoreCase(MailAccountVO.IMAP_TYPE)) {
        try {
          Session session = null;
          IMAPStore store = this.setupIMAPConnection(accountVO, session);
          MailFolderVO toFolderVO = getEmailFolder(newFolderID);
          MailFolderVO fromFolderVO = getEmailFolder(oldFolderID);

          String name = toFolderVO.getFolderFullName();
          if (name == null) {
            name = toFolderVO.getFolderName();
          }
          IMAPFolder toFolder = (IMAPFolder)store.getFolder(name);

          name = fromFolderVO.getFolderFullName();
          if (name == null) {
            name = fromFolderVO.getFolderName();
          }
          IMAPFolder fromFolder = (IMAPFolder)store.getFolder(name);

          fromFolder.open(IMAPFolder.READ_WRITE);
          Message msg = fromFolder.getMessageByUID(Long.parseLong(messageVO.getMessageUID()));
          fromFolder.copyMessages(new Message[] {msg}, toFolder);
          msg.setFlag(Flags.Flag.DELETED, true); //??
          fromFolder.close(true);
        } catch(Exception e) {
          System.out.println("Exception thrown in moveMessageToFolder()");
          e.printStackTrace();
          return(0);
        }
      }

      String insertQuery = "INSERT INTO emailmessagefolder (MessageID, FolderID) VALUES (?, ?)";
      cvdal.setSqlQuery(insertQuery);
      cvdal.setInt(1, messageID);
      cvdal.setInt(2, newFolderID);
      numberMoved = cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      String deleteQuery = "DELETE FROM emailmessagefolder WHERE MessageID = ? AND FolderID = ?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, messageID);
      cvdal.setInt(2, oldFolderID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in moveMessageToFolder(): " + e);
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return numberMoved;
  }

  /**
   * Adds a relationship between an email message and a new folder.
   * @param messageID The message to be copied.
   * @param folderID The new folderID.
   * @return The number of messages copied (should always be 1 or 0).
   */
  public int copyMessageToFolder(int individualID, int messageID, int newFolderID)
  {
    int numberCopied = 0;
    CVDal cvdal = new CVDal(this.dataSource);

    MailMessageVO messageVO = this.getEmailMessageVO(individualID, messageID);
    MailAccountVO accountVO = this.getMailAccountVO(messageVO.getEmailAccountID());

    int oldFolderID = messageVO.getEmailFolderID();
    String serverType = accountVO.getAccountType();

    if (serverType.equalsIgnoreCase(MailAccountVO.IMAP_TYPE)) {
      try {
        Session session = null;
        IMAPStore store = this.setupIMAPConnection(accountVO, session);
        MailFolderVO toFolderVO = getEmailFolder(newFolderID);
        MailFolderVO fromFolderVO = getEmailFolder(oldFolderID);

        String name = toFolderVO.getFolderFullName();
        if (name == null) {
          name = toFolderVO.getFolderName();
        }
        IMAPFolder toFolder = (IMAPFolder)store.getFolder(name);

        name = fromFolderVO.getFolderFullName();
        if (name == null) {
          name = fromFolderVO.getFolderName();
        }
        IMAPFolder fromFolder = (IMAPFolder)store.getFolder(name);

        fromFolder.open(IMAPFolder.READ_ONLY);
        Message msg = fromFolder.getMessageByUID(Long.parseLong(messageVO.getMessageUID()));
        fromFolder.copyMessages(new Message[] {msg}, toFolder);
        fromFolder.close(false);
      } catch(Exception e) {
        System.out.println("Exception thrown in copyMessageToFolder()");
        e.printStackTrace();
        return(0);
      }
    }

    try {
      String insertQuery = "INSERT INTO emailmessagefolder (MessageID, FolderID) VALUES (?, ?)";
      cvdal.setSqlQuery(insertQuery);
      cvdal.setInt(1, messageID);
      cvdal.setInt(2, newFolderID);
      numberCopied = cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      String updateQuery = "UPDATE emailmessage SET LocallyModified='YES', CopiedToFolder=? WHERE MessageID=?";
      cvdal.setSqlQuery(updateQuery);
      cvdal.setInt(1, newFolderID);
      cvdal.setInt(2, messageID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();
    }catch (Exception e){
      logger.error("[copyMessageToFolder]: Exception", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return numberCopied;
  }

  /**
   * Deletes the reference between an email and the folder
   * it reside(s/d) in. This method is used after a message has
   * been moved to another folder. This method cannot be called
   * until during the sync with the remote server, otherwise
   * the email message will probably be downloaded again.
   * @param messageID The ID of the message to remove the reference from.
   * @param folderID The ID of the folder to remove the reference from.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of references deleted (which should always be either 0 or 1).
   */
  private int deleteFolderReference(int messageID, int folderID, CVDal cvdal)
  {
    int numberDeleted = 0;

    try {
      String deleteQuery = "DELETE FROM emailmessagefolder WHERE MessageID = ? AND FolderID = ?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, messageID);
      cvdal.setInt(2, folderID);
      numberDeleted = cvdal.executeUpdate();
    }finally{
      cvdal.setSqlQueryToNull();
    }

    return numberDeleted;
  }   // end deleteFolderReference(int,int,CVDal) method

  /**
   * Returns the default MailFolderVO object of the accountID
   * passed into the method. If the folder is not found, an
   * empty MailFolderVO (with a FolderID of -1) is returned. <p>
   * This method is looking for the name of the folder used
   * here: com.centraview.mail.MailFolderVO.DEFAULT_FOLDER_NAME
   * @param accountID The Account ID that the Mail Folder belongs to.
   * @return A populated MailFolderVO if the folder exists.
   * @see com.centraview.mail.MailFolderVO.DEFAULT_FOLDER_NAME
   */
  public MailFolderVO getPrimaryEmailFolder(int accountID)
  {
    return this.getEmailFolderByName(accountID, MailFolderVO.DEFAULT_FOLDER_NAME);
  }   // end getPrimaryEmailFolder(int) method

  /**
   * Returns the default MailFolderVO object of the accountID
   * passed into the method. If the folder is not found, an
   * empty MailFolderVO (with a FolderID of -1) is returned. <p>
   * This method is looking for the name of the folder used
   * here: com.centraview.mail.MailFolderVO.DEFAULT_FOLDER_NAME
   * @param accountID The Account ID that the Mail Folder belongs to.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A populated MailFolderVO if the folder exists.
   * @throws Exception Something went wrong during execution.
   * @see com.centraview.mail.MailFolderVO.DEFAULT_FOLDER_NAME
   */
  private MailFolderVO getPrimaryEmailFolder(int accountID, CVDal cvdal) throws Exception
  {
    return this.getEmailFolderByName(accountID, MailFolderVO.DEFAULT_FOLDER_NAME, cvdal);
  }   // end getPrimaryEmailFolder(int,CVDal) method

  /**
   * Returns whether or not the folder exists in the database
   * for this account.<p>
   * This method should execute quickly because the raw result
   * set is used rather the the Collection from CVDal.
   * @param accountID The Account ID to check for.
   * @param folderName The Name of the folder to check for.
   * @return true if the folder exists, false otherwise.
   */
  public boolean emailFolderExists(int accountID, String folderName)
  {
    // TODO: make emailFolderExists() require the full path instead of the folder name.
    boolean exists = false;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      exists = this.emailFolderExists(accountID, folderName, cvdal);
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.emailFolderExists: "  + e.toString());
      //e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(exists);
  }

  /**
   * Returns whether or not the folder exists in the database for this account.
   * @param accountID The Account ID to check for.
   * @param folderName The Name of the folder to check for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return true if the folder exists, false otherwise.
   * @throws Exception Something went wrong during execution.
   */
  private boolean emailFolderExists(int accountID, String folderName, CVDal cvdal) throws Exception
  {
    boolean exists = false;
    ResultSet resultSet = null;

    try {
      String existsQuery = "SELECT FolderID FROM emailfolder WHERE AccountID = ? AND Name = ?";
      cvdal.setSqlQuery(existsQuery);
      cvdal.setInt(1, accountID);
      cvdal.setString(2, folderName);

      resultSet = cvdal.executeQueryNonParsed();
      if (resultSet.next()) {
        exists = true;
      }
    }finally{
      if (resultSet != null) {
        resultSet.close();
      }
      cvdal.setSqlQueryToNull();
    }
    return(exists);
  }

  /**
   * Returns a fully populated MailMessageVO object based on the
   * messageID. If the message is not found, a MailMessageObject
   * with a MesssageID of -1 will be returned.
   * @param individualID The individualID of the user requesting the message VO.
   * @param messageID The ID of the Mail Message we are looking for.
   * @return A fully populated MailMessageVO.
   */
  public MailMessageVO getEmailMessageVO(int individualID, int messageID)
  {
    MailMessageVO mailMessageVO = new MailMessageVO();
    mailMessageVO.setMessageID(-1);
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      mailMessageVO = this.getEmailMessageVO(individualID, messageID, cvdal);
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.getEmailMessageVO: " + e.toString());
      //e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(mailMessageVO);
  }   // end getEmailMessageVO(int,int) method

  /**
   * Returns a fully populated MailMessageVO object based on the
   * messageID. If the message is not found, a MailMessageObject
   * with a MesssageID of -1 will be returned.
   * @param individualID The individualID of the user requesting the message VO.
   * @param messageID The ID of the Mail Message we are  looking for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A fully populated MailMessageVO.
   * @throws Exception Something went wrong during execution.
   */
  private MailMessageVO getEmailMessageVO(int individualID, int messageID, CVDal cvdal) throws Exception
  {
    MailMessageVO mailMessageVO = new MailMessageVO();
    mailMessageVO.setMessageID(-1);

    try {
      String queryString = "SELECT em.MessageID, em.MessageDate, em.MailFrom, " +
                           "em.FromIndividual, em.ReplyTo, em.Subject, em.private, em.Headers, em.Owner, em.size, " +
                           "em.Priority, em.Importance, em.Body, em.AccountID, em.UID, " +
                           "em.LocallyDeleted, em.LocallyModified, em.MessageRead, emf.FolderID, em.contentType " +
                           "FROM emailmessage em, emailmessagefolder emf WHERE " +
                           "em.MessageID = emf.MessageID AND em.MessageID = ?";

      cvdal.setSqlQuery(queryString);
      cvdal.setInt(1, messageID);
      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number messageNumberID = (Number) resultsHashMap.get("MessageID");
          Number accountID = (Number) resultsHashMap.get("AccountID");
          Number messageFolderID = (Number) resultsHashMap.get("FolderID");
          Number messageSize = (Number) resultsHashMap.get("size");
          Number ownerID = (Number) resultsHashMap.get("Owner");
          String privateMessage = (String) resultsHashMap.get("private");
          Number fromIndividual = (Number) resultsHashMap.get("FromIndividual");

          java.sql.Timestamp messageSQLDate = (java.sql.Timestamp) resultsHashMap.get("MessageDate");

          Date messageDate = new Date(-1);
          if (messageSQLDate != null) {
            messageDate.setTime(messageSQLDate.getTime());
          }

          mailMessageVO.setMessageID(messageNumberID.intValue());
          mailMessageVO.setEmailAccountID(accountID.intValue());
          mailMessageVO.setEmailFolderID(messageFolderID.intValue());
          mailMessageVO.setSize(messageSize.intValue());
          mailMessageVO.setOwnerID(ownerID.intValue());
          mailMessageVO.setPrivate(privateMessage);
          mailMessageVO.setReceivedDate(messageDate);
          if (fromIndividual == null) {
            fromIndividual = new Integer(0);
          }
          mailMessageVO.setFromIndividualID(fromIndividual.intValue());

          mailMessageVO.setFromAddress((String) resultsHashMap.get("MailFrom"));
          mailMessageVO.setReplyTo((String) resultsHashMap.get("ReplyTo"));
          mailMessageVO.setSubject((String) resultsHashMap.get("Subject"));
          mailMessageVO.setHeaders((String) resultsHashMap.get("Headers"));
          mailMessageVO.setPriority((String) resultsHashMap.get("Priority"));
          mailMessageVO.setImportance((String) resultsHashMap.get("Importance"));
          mailMessageVO.setBody((String) resultsHashMap.get("Body"));
          mailMessageVO.setContentType((String)resultsHashMap.get("contentType"));
          mailMessageVO.setMessageUID((String) resultsHashMap.get("UID"));

          String locallyDeletedString = (String) resultsHashMap.get("LocallyDeleted");
          String locallyModifiedString = (String) resultsHashMap.get("LocallyModified");
          String messageReadString = (String) resultsHashMap.get("MessageRead");

          boolean locallyDeleted = (locallyDeletedString == null) ? false : locallyDeletedString.equalsIgnoreCase("YES");
          boolean locallyModified = (locallyModifiedString == null) ? false : locallyModifiedString.equalsIgnoreCase("YES");
          boolean messageRead = (messageReadString == null) ? false : messageReadString.equalsIgnoreCase("YES");

          mailMessageVO.setLocallyDeleted(locallyDeleted);
          mailMessageVO.setLocallyModified(locallyModified);
          mailMessageVO.setMessageRead(messageRead);

          // Get list of To:, Cc:, and Bcc: addresses
          ArrayList toList = new ArrayList();
          ArrayList ccList = new ArrayList();
          ArrayList bccList = new ArrayList();
          String recipientsQuery = "SELECT Address, RecipientType FROM emailrecipient WHERE MessageID=?";
          cvdal.setSqlQueryToNull();
          cvdal.setSqlQuery(recipientsQuery);
          cvdal.setInt(1, messageNumberID.intValue());
          Collection recipientsResults = cvdal.executeQuery();
          if (recipientsResults != null) {
            Iterator recipientsIter = recipientsResults.iterator();
            while (recipientsIter.hasNext()) {
              HashMap row = (HashMap)recipientsIter.next();
              if (row != null) {
                // check the RecipientType and add to the proper list
                if (((String)row.get("RecipientType")).equals("TO")) {
                  // To: address
                  try{
                    toList.add(new InternetAddress((String)row.get("Address")));
                  } catch (AddressException ae) {
                    toList.add(row.get("Address"));
                  }
                }else if (((String)row.get("RecipientType")).equals("CC")){
                  // Cc: address
                  try{
                    ccList.add(new InternetAddress((String)row.get("Address")));
                  }catch(AddressException ae){
                    ccList.add(row.get("Address"));
                  }
                }else if (((String)row.get("RecipientType")).equals("BCC")){
                  // Bcc: address
                  try{
                    bccList.add(new InternetAddress((String)row.get("Address")));
                  }catch(AddressException ae){
                    bccList.add(row.get("Address"));
                  }
                }
              }
            }
          }
          mailMessageVO.setToList(toList);
          mailMessageVO.setCcList(ccList);
          mailMessageVO.setBccList(bccList);


          //Get Any Attachments
          String attachmentQuery = "SELECT FileID FROM attachment WHERE MessageID = ?";
          cvdal.setSqlQueryToNull();
          cvdal.setSqlQuery(attachmentQuery);
          cvdal.setInt(1, messageNumberID.intValue());
          Collection attachmentResults = cvdal.executeQuery();
          if (attachmentResults != null) {
            Iterator attachmentIterator = attachmentResults.iterator();
            CvFileFacade fileFacade = new CvFileFacade();

            while (attachmentIterator.hasNext()) {
              HashMap attachmentHashMap = (HashMap) attachmentIterator.next();
              Number attachmentID = (Number) attachmentHashMap.get("FileID");
              // Reason for providing -13 because we don't want to carry out the Authorization Step
              // We don't have to do the authorization check from here..
              // Thats why passing -13. So that we will avoid the authorization..

              CvFileVO attachment = fileFacade.getFile(-13, attachmentID.intValue(), this.dataSource);
              mailMessageVO.addAttachedFiles(attachment);
            }
          }   // end while (attachmentIterator.hasNext())
        }   // end if (resultsIterator.hasNext())
      }  // end if (resultsCollection != null)
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(mailMessageVO);
  }   // end getEmailMessageVO(int,int,CVDal) method

  /**
   * This method only populates and returns a few things about an email
   * message. This is so this method can be as fast as possible (for
   * comparing messages against the server).<p>Only, MessageID, MovedToFolder,
   * CopiedToFolder, LocallyDeleted, LocallyModified and MessageRead are
   * populated. Nothing else is very important in order to compare local
   * email messages to the remote server.</p>
   * @param accountID The Account ID of the Account 'owning' this message.
   * @param folderID The folder ID of the folder holding this message.
   * @param messageUID The message UID to find the message.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A partially populated MailMessageVO object.
   * @throws Exception Something went wrong during execution.
   */
  private MailMessageVO getVLWEmailMessageVO(int accountID, int folderID, String messageUID, CVDal cvdal)
  {
    MailMessageVO mailMessageVO = new MailMessageVO();
    mailMessageVO.setMessageID(-1);
    ResultSet resultSet = null;

    try {
      String queryString = "SELECT em.MessageID, em.MovedToFolder, em.CopiedToFolder, " +
                           "em.LocallyDeleted, em.LocallyModified, em.MessageRead " +
                           "FROM emailmessage em, emailmessagefolder emf WHERE " +
                           "em.MessageID = emf.MessageID AND em.AccountID = ? " +
                           "AND em.UID = ? AND emf.FolderID = ?";

      cvdal.setSqlQuery(queryString);
      cvdal.setInt(1, accountID);
      cvdal.setString(2, messageUID);
      cvdal.setInt(3, folderID);

      resultSet = cvdal.executeQueryNonParsed();
      if (resultSet != null && resultSet.next()) {
        Number messageID = (Number) resultSet.getObject(1);
        Number moveToFolderID = (Number) resultSet.getObject(2);
        Number copyToFolderID = (Number) resultSet.getObject(3);
        String locallyDeletedString = (String) resultSet.getObject(4);
        String locallyModifiedString = (String) resultSet.getObject(5);
        String messageReadString = (String) resultSet.getObject(6);

        boolean locallyDeleted = (locallyDeletedString == null) ? false : locallyDeletedString.equalsIgnoreCase("YES");
        boolean locallyModified = (locallyModifiedString == null) ? false : locallyModifiedString.equalsIgnoreCase("YES");
        boolean messageRead = (messageReadString == null) ? false : messageReadString.equalsIgnoreCase("YES");

        mailMessageVO.setMessageID(messageID.intValue());
        mailMessageVO.setCopyToFolder(copyToFolderID.intValue());
        mailMessageVO.setMoveToFolder(moveToFolderID.intValue());
        mailMessageVO.setLocallyDeleted(locallyDeleted);
        mailMessageVO.setLocallyModified(locallyModified);
        mailMessageVO.setMessageRead(messageRead);
      }
    } catch (SQLException se) {
      logger.error("Exception in getVLWEmailMessageVO");
      se.printStackTrace();
    } finally{
      if (resultSet != null) {
        try {resultSet.close();} catch(SQLException se2) {}
      }
      cvdal.setSqlQueryToNull();
    }
    return mailMessageVO;
  }

  /**
   * This method saves certain parts of the lite-weight Mail message
   * to the databse, all other changes are ignored. This is so this
   * method can be as fast as possible (for comparing messages against
   * the server). <p>Only, MovedToFolder (to 0), CopiedToFolder (to 0),
   * LocallyDeleted (to NO), LocallyModified (to NO) and MessageRead
   * (to YES/NO) are saved. All other changes are ignored.</p>
   * @param mailMessageVO The Mail Message object to be saved.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of records updated by this method (should always be 0 or 1).
   */
  private int saveVLWEmailMessageVO(MailMessageVO mailMessageVO, CVDal cvdal)
  {
    int numberChanged = 0;

    try {
      String queryString = "UPDATE emailmessage SET MovedToFolder=0, " +
                           "CopiedToFolder=0, MessageRead=?, LocallyDeleted='NO', " +
                           "LocallyModified='NO' WHERE MessageID=?";
      cvdal.setSqlQuery(queryString);
      cvdal.setString(1, mailMessageVO.isMessageRead()?"YES":"NO");
      cvdal.setInt(2, mailMessageVO.getMessageID());

      numberChanged = cvdal.executeUpdate();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return numberChanged;
  }   // end saveVLWEmailMessageVO(MailMessageVO,CVDal) method

  /**
   * Returns whether or not the email message exists in
   * the database in a particular folder for an account.
   * The Message UID must also match up.
   * @param accountID The Account ID to check for.
   * @param folderID The folder to look in for the message.
   * @param messageUID The Message UID to compare to.
   * @return true if the message exists, false otherwise.
   */
  public boolean emailMessageExists(int accountID, int folderID, String messageUID)
  {
    boolean exists = false;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      exists = this.emailMessageExists(accountID, folderID, messageUID, cvdal);
    }catch(Exception e){
      logger.error("[emailMessageExists] Exception thrown.", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return exists;
  }   // end emailMessageExists(int,int,String) method

  /**
   * Returns whether or not the email message exists in
   * the database in a particular folder for an account.
   * The Message UID must also match up.
   * @param accountID The Account ID to check for.
   * @param folderID The folder to look in for the message.
   * @param messageUID The Message UID to compare to.
   * @param cvdal An <strong>open</strong> database connection.
   * @return true if the message exists, false otherwise.
   * @throws Exception Something went wrong during execution.
   */
  private boolean emailMessageExists(int accountID, int folderID, String messageUID, CVDal cvdal) throws Exception
  {
    boolean exists = false;
    ResultSet resultSet = null;
    try {
      String existsQuery = "SELECT em.MessageID FROM emailmessage em, emailmessagefolder emf " +
                           "WHERE em.MessageID = emf.MessageID AND " +
                           "em.AccountID = ? AND emf.FolderID = ? AND em.UID = ?";
      cvdal.setSqlQuery(existsQuery);
      cvdal.setInt(1, accountID);
      cvdal.setInt(2, folderID);
      cvdal.setString(3, messageUID);

      resultSet = cvdal.executeQueryNonParsed();
      if (resultSet.next()) {
        exists = true;
      }
    }finally{
      if (resultSet != null) {
        resultSet.close();
      }
      cvdal.setSqlQueryToNull();
    }
    return(exists);
  }   // end emailMessageExists(int,int,String,CVDal) method


  /**
   * Creates a new folder on a remote IMAP server with the given
   * folderName. Also subscribes to the created folder.
   * @param mailAccountVO The account information.
   * @param folderName The new name of the folder.
   * @return boolean true if 1) the folder was successfully created
   * on the remote server <strong>OR</strong> 2) a folder with
   * the given folderName already exists on the remote server.
   * Returns false if 1) the folder could not be created on
   * the remote server <strong>AND</strong> 2) the folder does
   * not already exist on the remote server.
   */
  public boolean addEmailFolderToIMAPServer(MailAccountVO mailAccountVO, MailFolderVO newFolderVO, CVDal cvdal)
  {
    boolean folderCreated = false;
    Store store = null;
    Folder newFolder = null;
    Folder parentFolder = null;

    try {
      String newFolderName = newFolderVO.getFolderName();
      // don't create "root" on the remote server!
      // also, Inbox will always exist by default, no need to create it remotely.
      if (newFolderName == null || newFolderName.equals("root") || newFolderName.equals("Inbox")) {
        return(true);
      }

      Properties properties = System.getProperties();

      //TODO: Add the secure IMAP connection stuff in here.
      //Look at http://www.javaworld.com/javatips/jw-javatip115_p.html
      Session session = Session.getDefaultInstance(properties);
      store = session.getStore(mailAccountVO.getAccountType());

      store.connect(mailAccountVO.getMailServer(), mailAccountVO.getLogin(), mailAccountVO.getPassword());

      int parentID = newFolderVO.getParentID();

      // get the full path to the new folder (don't include "/root")
      String parentFolderName = this.getFolderFullPathString(parentID, false, cvdal);

      boolean parentHoldsFolders = true;
      if (parentFolderName.length() > 0) {
        // if this is not a root-level folder, we need to get the properties
        // of the parent folder
        // strip the leading "/"
        parentFolderName = parentFolderName.substring(1, parentFolderName.length());
        parentFolder = store.getFolder(parentFolderName);

        int parentType = parentFolder.getType();

        if (parentType != Folder.HOLDS_FOLDERS && parentType != 3) {
          // 3 == Folder.HOLDS_BOTH
          // we can't create folders here, so fail
          parentHoldsFolders = false;
          // but not before cleaning up... (see below)
        }

        if (parentFolder.isOpen()) {
          parentFolder.close(false);
        }

        // if we're able to create this folder, we'll need to
        // give the full path (which equals parentFolder/newFolder)
        newFolderName = parentFolderName + "/" + newFolderName;
      }

      if (! parentHoldsFolders) {
        return false;
      }

      newFolder = store.getFolder(newFolderName);

      if (! newFolder.exists()) {
        try {
          // first try to create the folder as HOLDS_BOTH. Note that Folder doesn't
          // have a static member for HOLDS_BOTH, so we're guessing that it's "3".
          folderCreated = newFolder.create(3);    // 3 == Folder.HOLDS_BOTH);
        }catch(MessagingException foo){
          try {
            // if "3" didn't work, then trying HOLDS_MESSAGES. This will at least
            // give the user the ability to create folders that hold messages, and
            // SOME mail servers will also allow them to create subfolders because
            // they don't obey the folder type.
            System.out.println("\n\n\nCOULD NOT CREATE NEW FOLDER!!!! Trying again as HOLDS_MESSAGES...\n\n\n");
            folderCreated = newFolder.create(Folder.HOLDS_MESSAGES);
          }catch(MessagingException me){
            // if all that failed, we're screwed :-(
            System.out.println("\n\n\nCOULD NOT CREATE NEW FOLDER!!!! Giving Up!!!!!!\n\n\n");
            System.out.println(me);
            me.printStackTrace();
          }
        }
      }else{
        // if the folder already existed on the remote server,
        // act like we just created it anyway, so that the
        // calling methods know to create the folder locally too.
        folderCreated = true;
      }

      if (folderCreated) {
        if (! newFolder.isSubscribed()) {
          newFolder.setSubscribed(true);
        }
      }

      if (newFolder.isOpen()) {
        newFolder.close(false);
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] addEmailFolderToIMAPServer(MailAccountVO,String): " + e);
      // e.printStackTrace();
    }finally{
      if (newFolder != null){
        try{ newFolder.close(false); }catch(Exception e){ /*Can't really do anything at this point*/ }
      }

      if (parentFolder != null){
        try{ parentFolder.close(false); }catch(Exception e){ /*Can't really do anything at this point*/ }
      }

      if (store != null){
        try{ store.close(); }catch(Exception e){ /*Can't really do anything at this point*/ }
      }

    }   // end finally
    return folderCreated;
  }   // end addEmailFolderToIMAPServer(MailFolderVO,String) method

  /**
   * Adds a new Folder to an account, and will also add the new folder
   * to the remote IMAP server if createOnRemoteServer is true.
   * @param mailFolderVO The new MailFolderVO
   * @param createOnRemoteServer If true, then this folder will be created
   *        on the remote IMAP server as well as in our local database.
   * @return The ID of the new Folder.
   */
  public int addEmailFolder(MailFolderVO mailFolderVO, boolean createOnRemoteServer)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    int newFolderID = -1;

    try {
      newFolderID = this.addEmailFolder(mailFolderVO, createOnRemoteServer, cvdal);
    }catch (Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in addEmailFolder(MailFolderVO): " + e);
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }

    return newFolderID;
  }

  /**
   * Adds a new Folder to an account.<p>
   * @param mailFolderVO The new MailFolderVO.
   * @param createOnRemoteServer Flag to indicate whether this
   *        folder should be created remotely <strong>IF</strong>
   *        the server type is IMAP.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The ID of the new Folder.
   * @throws Exception Something went wrong during execution.
   */
  private int addEmailFolder(MailFolderVO mailFolderVO, boolean createOnRemoteServer, CVDal cvdal) throws Exception
  {
    int newFolderID = -1;

    try {
      // if the account we are creating this folder for, is an
      // IMAP type account, then we need to try creating the
      // folder on the server *BEFORE* creating it locally. That
      // way, if there is an error or permissions problem, we
      // can let the user know, and it won't cause problems at
      // at later time (such as when checking mail).
      int accountID = mailFolderVO.getEmailAccountID();
      MailAccountVO accountVO = this.getMailAccountVO(accountID);
      String serverType = accountVO.getAccountType();

      boolean imapFolderCreated = true;
      if (serverType != null && serverType.equals(MailAccountVO.IMAP_TYPE) && createOnRemoteServer) {
        imapFolderCreated = this.addEmailFolderToIMAPServer(accountVO, mailFolderVO, cvdal);
        }

      if (! imapFolderCreated) {
        // if the folder wasn't created remotely, don't create
        // it locally. Just quit now.
        return -1;
      }

      boolean createFolderFlag = true;
      cvdal.setSqlQuery("SELECT FolderID from emailfolder where AccountID = ? AND Name = ? AND Ftype = ? ");
      cvdal.setInt(1, mailFolderVO.getEmailAccountID());
      cvdal.setString(2, mailFolderVO.getFolderName());
      cvdal.setString(3, mailFolderVO.getFolderType());
      Collection results = cvdal.executeQuery();
      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          newFolderID = ((Number) row.get("FolderID")).intValue();
          createFolderFlag = false;
        }
      }

      cvdal.setSqlQueryToNull();
      cvdal.clearParameters();

      if(createFolderFlag){
      String insertString = "INSERT INTO emailfolder (Parent, AccountID, Name, Ftype) VALUES (?, ?, ?, ?)";
      cvdal.setSqlQuery(insertString);

      cvdal.setInt(1, mailFolderVO.getParentID());
      cvdal.setInt(2, mailFolderVO.getEmailAccountID());
      cvdal.setString(3, mailFolderVO.getFolderName());
      cvdal.setString(4, mailFolderVO.getFolderType());

      cvdal.executeUpdate();

      newFolderID = cvdal.getAutoGeneratedKey();
      }
    }finally{
      cvdal.setSqlQueryToNull();
    }

    return newFolderID;
  }

  /**
   * Retrieves the Account Information for a specific account ID.
   * @param accountID The ID of the account being looked up.
   * @return A populated MailAccountVO Object.
   */
  public MailAccountVO getMailAccountVO(int accountID)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    MailAccountVO mailAccountVO = new MailAccountVO();

    //TODO: Add code to set the setSupportAccount method on
    //the MailAccountVO object. This can be obtained by
    //seeing if the accountID is in the supportemailaccount table.

    try {
      String searchQuery =
        "SELECT AccountID, e.Owner, Name, Login, Password, SMTPServer, ServerType, LeaveOnServer, " +
        "Address, ReplyTo, Signature, `Default`, mailserver, lastfetchedcount, lastfetcheddate, lastuid, " +
        "Port, SecureConnectionIfPossible, ForceSecureConnection, SMTPRequiresAuthentication, POPBeforeSMTP, " +
        "concat(firstname,' ',lastname) AS individualname FROM emailaccount e, individual i " +
        "WHERE AccountID = ? AND e.owner=i.individualid";

      cvdal.setSqlQuery(searchQuery);
      cvdal.setInt(1, accountID);

      Collection resultsSet = cvdal.executeQuery();

      if (resultsSet != null) {
        Iterator resultsIterator = resultsSet.iterator();
        if (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number emailAccountID = (Number) resultsHashMap.get("AccountID");
          Number ownerID = (Number) resultsHashMap.get("Owner");
          Number smtpPort = (Number) resultsHashMap.get("Port");
          Number lastFetchedCount = (Number) resultsHashMap.get("lastfetchedcount");

          boolean leaveOnServer = false;
          boolean defaultAccount = false;
          boolean secureConnection = false;
          boolean forceSecureConnection = false;
          boolean smptAuthorization = false;
          boolean popBeforeSmtp = false;

          String accountName = (String) resultsHashMap.get("Name");
          String individualname = (String) resultsHashMap.get("individualname");
          String login = (String) resultsHashMap.get("Login");
          String password = (String) resultsHashMap.get("Password");
          String smtpServerAddress = (String) resultsHashMap.get("SMTPServer");
          String emailAddress = (String) resultsHashMap.get("Address");
          String replyToAddress = (String) resultsHashMap.get("ReplyTo");
          String signature = (String) resultsHashMap.get("Signature");
          String mailServerAddress = (String) resultsHashMap.get("mailserver");
          String serverType = (String) resultsHashMap.get("ServerType");
          String lastUID = (String) resultsHashMap.get("lastuid");

          String leaveOnServerString = (String) resultsHashMap.get("LeaveOnServer");
          String defaultAccountString = (String) resultsHashMap.get("Default");
          String secureConnectionString = (String) resultsHashMap.get("SecureConnectionIfPossible");
          String forceSecureConnectionString = (String) resultsHashMap.get("ForceSecureConnection");
          String smtpRequireAuthString = (String) resultsHashMap.get("SMTPRequiresAuthentication");
          String popBeforeSMTP = (String) resultsHashMap.get("POPBeforeSMTP");

          leaveOnServer = (leaveOnServerString == null) ? false : leaveOnServerString.equalsIgnoreCase("YES");
          defaultAccount = (defaultAccountString == null) ? false : defaultAccountString.equalsIgnoreCase("YES");

          secureConnection = (secureConnectionString == null) ? false : secureConnectionString.equalsIgnoreCase("YES");
          forceSecureConnection = (forceSecureConnectionString == null) ? false : forceSecureConnectionString.equalsIgnoreCase("YES");

          smptAuthorization = (smtpRequireAuthString == null) ? false : smtpRequireAuthString.equalsIgnoreCase("YES");
          popBeforeSmtp = (popBeforeSMTP == null) ? false : popBeforeSMTP.equalsIgnoreCase("YES");

          java.sql.Timestamp lastFetchSQLDate = (java.sql.Timestamp)resultsHashMap.get("lastfetcheddate");

          //Means the mail has never been checked.
          Date lastFetchedDate = new Date(-1);
          if (lastFetchSQLDate != null) {
            lastFetchedDate.setTime(lastFetchSQLDate.getTime());
          }

          if (lastFetchedCount == null) {
            lastFetchedCount = new Integer(0);
          }

          mailAccountVO.setIndividualName(individualname);
          mailAccountVO.setAccountID(emailAccountID.intValue());
          mailAccountVO.setAccountName(accountName);
          mailAccountVO.setAccountType(serverType);
          mailAccountVO.setDefaultAccount(defaultAccount);
          mailAccountVO.setEmailAddress(emailAddress);
          mailAccountVO.setForceSecureConnection(forceSecureConnection);
          mailAccountVO.setLastFetchedCount(lastFetchedCount.intValue());
          mailAccountVO.setLastFetchedDate(lastFetchedDate);
          mailAccountVO.setLastUID(lastUID);
          mailAccountVO.setLeaveMessagesOnServer(leaveOnServer);
          mailAccountVO.setLogin(login);
          mailAccountVO.setMailServer(mailServerAddress);
          mailAccountVO.setOwnerID(ownerID.intValue());
          mailAccountVO.setPassword(password);
          mailAccountVO.setReplyToAddress(replyToAddress);
          mailAccountVO.setSecureConnection(secureConnection);
          mailAccountVO.setSignature(signature);
          mailAccountVO.setSmtpPort(smtpPort.intValue());
          mailAccountVO.setSmtpServer(smtpServerAddress);
          mailAccountVO.setPopRequiredBeforeSMTP(popBeforeSmtp);
          mailAccountVO.setAuthenticationRequiredForSMTP(smptAuthorization);
        }
      }
    }catch(Exception e){
      logger.error("[getMailAccountVO] Exception thrown.", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(mailAccountVO);
  }

  /**
   * Updates an Email Account based on the MailAccountVO
   * passed into this method.
   * @param mailAccountVO The new contents of the Mail Account.
   * @throws MessagingException If any of the account credentials are wrong then it will throw the error.
   */
  public void updateEmailAccount(MailAccountVO mailAccountVO) throws MessagingException
  {
    CVDal cvdal = new CVDal(this.dataSource);

  String searchQuery = "SELECT ServerType FROM emailaccount WHERE AccountID = ?";
  cvdal.setSqlQuery(searchQuery);
  cvdal.setInt(1, mailAccountVO.getAccountID());

  Collection resultsSet = cvdal.executeQuery();
  String serverType = null;
    if (resultsSet != null) {
    Iterator resultsIterator = resultsSet.iterator();
      if (resultsIterator.hasNext()) {
      HashMap resultsHashMap = (HashMap) resultsIterator.next();
      serverType = (String) resultsHashMap.get("ServerType");
      mailAccountVO.setAccountType(serverType);
    }
    }

  // check for the account credentials
  this.checkEmailCredentials(mailAccountVO);

    try {
      String updateString = "UPDATE emailaccount SET Name=?, Login=?, Password=?, " +
                            "SMTPServer=?, LeaveOnServer=?, " +
                            "Address=?, ReplyTo=?, Signature=?, `Default`=?, mailserver=?, " +
                            "Port=?, SecureConnectionIfPossible=?, ForceSecureConnection=?, " +
                            "SMTPRequiresAuthentication=?, POPBeforeSMTP=? WHERE AccountID=?";

      cvdal.setSqlQuery(updateString);

      cvdal.setString(1, mailAccountVO.getAccountName());
      cvdal.setString(2, mailAccountVO.getLogin());
      cvdal.setString(3, mailAccountVO.getPassword());
      cvdal.setString(4, mailAccountVO.getSmtpServer());
      cvdal.setString(5, mailAccountVO.isLeaveMessagesOnServer()?"YES":"NO");
      cvdal.setString(6, mailAccountVO.getEmailAddress());
      cvdal.setString(7, mailAccountVO.getReplyToAddress());
      cvdal.setString(8, mailAccountVO.getSignature());
      cvdal.setString(9, mailAccountVO.isDefaultAccount()?"YES":"NO");
      cvdal.setString(10, mailAccountVO.getMailServer());
      cvdal.setInt(11, mailAccountVO.getSmtpPort());
      cvdal.setString(12, mailAccountVO.isSecureConnection()?"YES":"NO");
      cvdal.setString(13, mailAccountVO.isForceSecureConnection()?"YES":"NO");
      cvdal.setString(14, mailAccountVO.isAuthenticationRequiredForSMTP()?"YES":"NO");
      cvdal.setString(15, mailAccountVO.isPopRequiredBeforeSMTP()?"YES":"NO");
      cvdal.setInt(16, mailAccountVO.getAccountID());

      cvdal.executeUpdate();
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in updateEmailAccount(): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
  }   // end updateEmailAccount(MailAccountVO) method

  /**
   * Deletes the given email account from the database, but *NOT*
   * the messages or folders associated with this email account.
   * @param individualID The user who is deleting the account.
   * @param accountID The account ID of the account to be deleted.
   * @return boolean true for success, false for failure.
   */
  public boolean deleteEmailAccount(int individualID, int accountID)
  {
    boolean returnStatus = true;

    if (accountID <= 0) {
      return(false);
    }

    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String deleteSql = "DELETE FROM emailaccount WHERE accountID=?";
      cvdal.setSqlQuery(deleteSql);
      cvdal.setInt(1, accountID);

      cvdal.executeUpdate();

      // Be careful. The business requirement is that messages should
      // not be deleted, so that they continue to show up in the
      // related info screen after the account has been deleted. There
      // is currently no way to do this, so we're not deleting the messages
      // only the account from emailaccount table.
      //String folderSql = "DELETE FROM emailfolder WHERE AccountID=?";
      int defaultAccount = this.getDefaultAccountID(individualID);

      if (defaultAccount == 0) {
        ArrayList accountIDs = this.getUserAccountList(individualID);
        if (accountIDs != null && accountIDs.size() != 0) {
          if (accountIDs.get(0) != null) {
            accountID = ((Number) accountIDs.get(0)).intValue();
            String updatePrefs = "UPDATE emailaccount SET `Default`=? WHERE AccountID=?";
            cvdal.setSqlQueryToNull();
            cvdal.clearParameters();
            cvdal.setSqlQuery(updatePrefs);
            cvdal.setString(1, "YES");
            cvdal.setInt(2, accountID);
            cvdal.executeUpdate();
          }
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in updateEmailAccount(): " + e);
      // e.printStackTrace();
      returnStatus = false;
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return returnStatus;
  }   // end deleteEmailAccount(int,int) method

  /**
   * Creates a new email account in the database, and returns the
   * account ID of the newly created email account.
   * @param accountVO a MailAccountVO object containing the data
   *        for the new account. *ALL* fields must be supplied, so
   *        please do your validation at the Struts layer.
   * @return int account ID of the newly created email account.
   * @throws MessagingException If any of the account credentials are wrong then it will throw the error.
   */
  public int addEmailAccount(MailAccountVO accountVO) throws MessagingException
  {
    int newAccountID = -1;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      newAccountID = this.addEmailAccount(accountVO, cvdal);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return newAccountID;
  }

  /**
   * Creates a new email account in the database, and returns the
   * account ID of the newly created email account.
   * @param accountVO a MailAccountVO object containing the data
   *        for the new account. *ALL* fields must be supplied, so
   *        please do your validation at the Struts layer.
   * @param cvdal An <strong>open</strong> database connection.
   * @return int account ID of the newly created email account.
   * @throws MessagingException If any of the account credentials are wrong then it will throw the error.
   */
  public int addEmailAccount(MailAccountVO accountVO, CVDal cvdal) throws MessagingException
  {
    int newAccountID = -1;
    
    // check for the account credentials
    this.checkEmailCredentials(accountVO);

    try {
      String insertQuery = "INSERT INTO emailaccount (Owner, Name, Login, Password, SMTPServer, " +
                           "ServerType, LeaveOnServer, Address, ReplyTo, Signature, `Default`, " +
                           "mailserver, lastfetchedcount, lastfetcheddate, lastuid, Port, " +
                           "SecureConnectionIfPossible, ForceSecureConnection, SMTPRequiresAuthentication, " +
                           "POPBeforeSMTP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      cvdal.setSqlQuery(insertQuery);
      cvdal.setInt(1, accountVO.getOwnerID());
      cvdal.setString(2, accountVO.getAccountName());
      cvdal.setString(3, accountVO.getLogin());
      cvdal.setString(4, accountVO.getPassword());
      cvdal.setString(5, accountVO.getSmtpServer());

      cvdal.setString(6, accountVO.getAccountType());

      if (accountVO.isLeaveMessagesOnServer()) {
        cvdal.setString(7, "YES");
      }else{
        cvdal.setString(7, "NO");
      }

      cvdal.setString(8, accountVO.getEmailAddress());
      cvdal.setString(9, accountVO.getReplyToAddress());
      cvdal.setString(10, accountVO.getSignature());

      if (accountVO.isDefaultAccount()) {
        cvdal.setString(11, "YES");
      }else{
        cvdal.setString(11, "NO");
      }

      cvdal.setString(12, accountVO.getMailServer());
      cvdal.setInt(13, accountVO.getLastFetchedCount());
      cvdal.setRealDate(14, new java.sql.Date(accountVO.getLastFetchedDate().getTime()));
      cvdal.setString(15, accountVO.getLastUID());
      cvdal.setInt(16, accountVO.getSmtpPort());

      if (accountVO.isSecureConnection()) {
        cvdal.setString(17, "YES");
      }else{
        cvdal.setString(17, "NO");
      }

      if (accountVO.isForceSecureConnection()) {
        cvdal.setString(18, "YES");
      }else{
        cvdal.setString(18, "NO");
      }

      if (accountVO.isAuthenticationRequiredForSMTP()) {
        cvdal.setString(19, "YES");
      }else{
        cvdal.setString(19, "NO");
      }

      if (accountVO.isPopRequiredBeforeSMTP()) {
        cvdal.setString(20, "YES");
      }else{
        cvdal.setString(20, "NO");
      }

      cvdal.executeUpdate();
      newAccountID = cvdal.getAutoGeneratedKey();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

      accountVO.setAccountID(newAccountID);

      this.createDefaultFolders(accountVO, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getEmailFolder(MailAccountVO, CVDal): " + e);
      e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return newAccountID;
  }

  /**
   * When creating an email account, this method is called to also
   * create the default set of folders which are needed for the new
   * email account. This method should *only* be called from
   * MailEJB.addEmailAccount() in a private manner.
   */
  private boolean createDefaultFolders(MailAccountVO accountVO, CVDal cvdal)
  {
    boolean result = true;

    try {
      MailFolderVO rootFolder = new MailFolderVO();
      rootFolder.setParentID(0);
      rootFolder.setEmailAccountID(accountVO.getAccountID());
      rootFolder.setFolderName("root");
      rootFolder.setFolderType(MailFolderVO.SYSTEM_FOLDER_TYPE);
      rootFolder.setTotalMessage(0);
      rootFolder.setCountReadMessage(0);

      int rootFolderID = this.addEmailFolder(rootFolder, true, cvdal);

      // These are the default system folders.
      String[] folders = {
                           MailFolderVO.DEFAULT_FOLDER_NAME,
                           MailFolderVO.SENT_FOLDER_NAME,
                           MailFolderVO.DRAFTS_FOLDER_NAME,
                           MailFolderVO.TRASH_FOLDER_NAME,
                           MailFolderVO.TEMPLATES_FOLDER_NAME
                         };

      for (int i=0; i<folders.length; i++) {
        MailFolderVO newFolder = new MailFolderVO();
        newFolder.setParentID(rootFolderID);
        newFolder.setEmailAccountID(accountVO.getAccountID());
        newFolder.setFolderName(folders[i]);
        newFolder.setFolderType(MailFolderVO.SYSTEM_FOLDER_TYPE);
        newFolder.setTotalMessage(0);
        newFolder.setCountReadMessage(0);

        this.addEmailFolder(newFolder, true, cvdal);
      }
    }catch(Exception e){
      logger.error("[createDefaultFolders]: Exception", e);
      result = false;
    }
    return(result);
  }

  /**
   * Gets the mail for a given account.
   * @param mailAccountVO The VO of the  Email Account.
   * @throws Exception If there was a problem
   * getting the email from the server. This is
   * thrown so an error message can be shown to the
   * user.
   */
  public void getMail(MailAccountVO mailAccountVO, int folderID) throws Exception
  {
    if (mailAccountVO.getAccountType().equalsIgnoreCase(MailAccountVO.POP3_TYPE)) {
      this.getPOPMail(mailAccountVO);
    } else if (mailAccountVO.getAccountType().equalsIgnoreCase(MailAccountVO.IMAP_TYPE)) {
      if (folderID > 0) {
        this.checkIMAPFolder(folderID, mailAccountVO);
      } else {
        this.newIMAP(mailAccountVO);
//        this.getIMAPMail(mailAccountVO);
//        mailAccountVO.setBeenChecked(true);
//        this.updateAccountCheck(mailAccountVO);
      }
    }
  }

  /**
   * Gets the mail from all accounts configured for the given
   * individualID. This method actually gets the list of mail
   * accounts for the given user, and calls this.getMail for
   * each account.
   * @param individualID The individualID of the user whose
   * accounts we are to check.
   * @return void
   */
  public void checkAllAccountsForUser(int individualID, int folderID) throws Exception
  {
    // get the list of accounts for the given user
    ArrayList accountList = this.getUserAccountList(individualID);
    if (accountList != null) {
      // loop through the list of accounts, and check each one
      Iterator iter = accountList.iterator();
      while (iter.hasNext()) {
        Number accountID = (Number)iter.next();

        MailAccountVO acountVO = this.getMailAccountVO(accountID.intValue());
        this.getMail(acountVO, folderID);
      }
    }
  }

  /**
   * Gets the mail for a given account from a IMAP Account.
   * This method will try it's best to sync with the remote IMAP server.
   * It will call this.processImapFolderRecursively() in order to
   * process the entire folder tree and all the messges in those folders.
   * @param mailAccountVO The VO of the Email Account.
   * @param attachmentFolderID The folderID of the attachments folder.
   * @return The number of changed messages recieved from the IMAP Server.
   * @throws Exception If there was a problem  getting the email from the
   *         server. This is thrown so an error message can be shown to the user.
   */

  public void getIMAPMail(MailAccountVO mailAccountVO) throws Exception
  {
    CVDal cvdal = new CVDal(this.dataSource);

    Properties properties = System.getProperties();
    properties.put("mail.debug", "true");

    // TODO: Add the secure connection stuff to the IMAP fetchmail
    // Look at http://www.javaworld.com/javatips/jw-javatip115_p.html
    Session session = Session.getDefaultInstance(properties);
    IMAPStore store = (IMAPStore)session.getStore(mailAccountVO.getAccountType());
    store.connect(mailAccountVO.getMailServer(), mailAccountVO.getLogin(), mailAccountVO.getPassword());

    Folder defaultIMAPFolder =  null;
    IMAPFolder[] folders = null;
    try {
      defaultIMAPFolder = store.getDefaultFolder();
      // This gets ALL folders in the user's namespace. This will return a "flat"
      // list of all folders and subfolders, which we can process with a simple
      // loop instead of using recursion.
      folders = (IMAPFolder[])defaultIMAPFolder.list("*");
    } catch (MessagingException e1) {
      store.close();
    }

    // Check wheather the Account is view as a Support Email Account
    boolean isSupportEmailAccount = false;
    isSupportEmailAccount = this.isSupportEmailAccount(mailAccountVO.getAccountID(),cvdal);

    // When we have moved a message locally, we create a case where
    // the local system and the server get out of sync. So we'll save
    // the parent folder in this temporary ArrayList, and we'll
    // RE-process that folder again after finishing the loop the first time.
    ArrayList reprocessFolders = new ArrayList();
    // List of valid message IDs.
    Collection validIDs = new ArrayList();
    ArrayList messageIDs = new ArrayList();

    try {
      // Since the list of folders is returned from the server in
      // alphabetical order, there is the possibility that we'll get
      // a child folder before the parent folder. That causes a case
      // where we can't create that folder locally, so we'll stick
      // that folder in this temporary ArrayList, and process it again
      // after looping through all the folders the first time.
      ArrayList problemFolders = new ArrayList();

      // process each folder in the "folders" array, and *possibly* the List "reprocessFolders"
      for (int i=0,k=0; (i < folders.length) || (k != 0) ; i++) {
        // get the folder for this iteration...
        IMAPFolder currentFolder = null;
        if ((k != 0) && (i >= folders.length)) {
          // in this case, we have finished processing the "folders" array,
          // and we are now processing the "reprocessFolders" List. Get the
          // currentFolder from the IMAP server ("store").
          k--;
          String newFolderPath = (String)reprocessFolders.get(k);
          currentFolder = (IMAPFolder)store.getFolder(newFolderPath);
        }else{
          // in this case, we're looping through the "folders" array for the
          // first time, so get the currentFolder from the "folders" array.
          currentFolder = folders[i];
        }

        String currentFolderName = currentFolder.getName();

        if (! currentFolder.isSubscribed() && (! currentFolderName.equals("INBOX"))) {
          continue;
        }

        // folderFullPath should contain the PATH to the folder, *EXcluding*
        // current folder name. (if the current folder is /Inbox/Personal/Jokes,
        // then folderFullPath should be /Inbox/Personal). This will be used
        // to get the parent ID of the local folder so that we can create the
        // new folder locally.
        String folderFullPath = "/root/";
        String remoteFullName = currentFolder.getFullName();
        folderFullPath += remoteFullName;
        folderFullPath = folderFullPath.replaceAll(currentFolderName+"$", "");
        folderFullPath = folderFullPath.replaceAll("/$", "");

        String folderFullName = remoteFullName.replaceAll("/$", "");

        if (k != 0) {
          // If we are finished processing the "folders" array, and are
          // now currently processing the "reprocessFolders" ArrayList,
          // we must remove the object from reprocessFolders, so that we
          // won't process that folder yet again
          if (reprocessFolders.contains(folderFullName)) {
            reprocessFolders.remove(folderFullName);
            k--;
          }
        }

        try {
          currentFolder.open(Folder.READ_WRITE);
        }catch(MessagingException me){
          System.out.println("[MailEJB][Exception] A messaging exception was caught while opening [" + folderFullName + "].");
          System.out.println("[MailEJB][Exception] skipping this folder, and continuing to process other folders.");
          me.printStackTrace();
          continue;
        }

        int folderID = -1;
        MailFolderVO currentFolderVO = this.getEmailFolderByName(mailAccountVO.getAccountID(), currentFolder.getName(), cvdal);

        // Get the remote folders, check if they exist locally, and CRUD them if necessary
        // TODO: when checking if IMAP folder exists locally, we need to pass full path, not just folder name
        if (this.emailFolderExists(mailAccountVO.getAccountID(), currentFolder.getName(), cvdal)) {
          // folder exists locally, just get the ID
          folderID = currentFolderVO.getFolderID();
        }else{
          // folder does not exist locally, create a new one and get the new ID
          // THIS CODE NEEDS TO BE ABSTRACTED OUT INTO A NEW METHOD, BECAUSE IT
          // IS IDENTICAL TO SOME CODE DOWN BELOW AFTER THE FOLDERS LOOP.
          int parentFolderID = this.getFolderIDFromFullPath(folderFullPath, mailAccountVO.getAccountID(), cvdal);

          if (parentFolderID > 0) {
            MailFolderVO newFolderVO = new MailFolderVO();
            newFolderVO.setFolderName(currentFolder.getName());
            newFolderVO.setFolderType(MailFolderVO.USER_FOLDER_TYPE);
            newFolderVO.setEmailAccountID(mailAccountVO.getAccountID());
            newFolderVO.setParentID(parentFolderID);
            folderID = this.addEmailFolder(newFolderVO, false, cvdal);
          }else{
            // if the parent folder did not exist locally, then we need
            // to add this folder to the ArrayList problemFolders. After
            // we are finished processing the "folders" array once, we'll
            // then process the folders in "problemFolders" to try and
            // make sure we successfully sync all folders.
            HashMap probFolder = new HashMap();
            probFolder.put("fullName", remoteFullName);
            probFolder.put("name", currentFolder.getName());
            problemFolders.add(probFolder);
            continue;
          }
        }

        // Now get the messages from the server and store them locally
        Message[] messageArray = currentFolder.getMessages();

        FetchProfile profile = new FetchProfile();
        // These make us supa fast!
        profile.add(FetchProfile.Item.FLAGS);
        profile.add(UIDFolder.FetchProfileItem.UID);
        //profile.add(FetchProfile.Item.ENVELOPE);
        //profile.add(FetchProfile.Item.HEADERS);
        currentFolder.fetch(messageArray, profile);

        ArrayList transport = new ArrayList();
        transport.add(reprocessFolders);
        transport.add(validIDs);
        transport.add(messageIDs);
        // k += this.handleIMAPMessageArray(messageArray, currentFolder, folderID, mailAccountVO, transport, store, cvdal);
        reprocessFolders = (ArrayList)transport.get(0);
        validIDs = (ArrayList)transport.get(1);
        messageIDs = (ArrayList)transport.get(2);
      }

      // perhaps get the list of folders that couldn't be created,
      // and try creating them again? THIS CODE NEEDS TO BE ABSTRACTED
      // OUT INTO A METHOD, BECAUSE IT IS IDENTICAL TO SOME CODE AT
      // THE TOP OF THE ABOVE LOOP.
      Iterator problemIter = problemFolders.iterator();
      while (problemIter.hasNext()) {
        HashMap probFolder = (HashMap)problemIter.next();
        String folderFullPath = "/root/";
        folderFullPath += (String)probFolder.get("fullName");
        folderFullPath = folderFullPath.replaceAll((String)probFolder.get("name")+"$", "");
        folderFullPath = folderFullPath.replaceAll("/$", "");

        IMAPFolder newFolder = (IMAPFolder)store.getFolder(folderFullPath);

        int parentFolderID = this.getFolderIDFromFullPath(folderFullPath, mailAccountVO.getAccountID(), cvdal);

        if (parentFolderID > 0) {
          MailFolderVO newFolderVO = new MailFolderVO();
          newFolderVO.setFolderName((String)probFolder.get("name"));
          newFolderVO.setFolderType(MailFolderVO.USER_FOLDER_TYPE);
          newFolderVO.setEmailAccountID(mailAccountVO.getAccountID());
          newFolderVO.setParentID(parentFolderID);
          int folderID = this.addEmailFolder(newFolderVO, false, cvdal);
        }
      }
      // Delete any message that weren't on the server (which means they
      // were deleted elsewhere (or copied somewhere).
      this.deleteInvalidMessages(mailAccountVO.getOwnerID(), mailAccountVO.getAccountID(), validIDs, -1, cvdal);

      if (messageIDs.size() > 0) {
        this.applyRules(mailAccountVO.getAccountID(), messageIDs, cvdal);
      }
    }catch(Exception e){
      logger.error("[getIMAPMail]: Exception", e);
    }finally{
      //make sure each folder has been closed
      try {
        for (int i=0; i<folders.length; i++) {
          if (folders[i].isOpen()) {
            folders[i].close(true);
          }
        }
        store.close();
      } catch (Exception e) { }
      cvdal.destroy();
      cvdal = null;
    }

    if (isSupportEmailAccount) {
      this.supportEmail(messageIDs,mailAccountVO.getOwnerID());
    }
  }

  /**
   * Gets the mail for a given account from a POP3 Account.
   * @param mailAccountVO The VO of the Email Account.
   * @param attachmentFolderID The folderID of the attachments folder.
   * @return The number of new messages recieved from the POP3 Server.
   * @throws Exception If there was a problem  getting the email
   *         from the server. This is  thrown so an error message
   *         can be shown to the user.
   */
  public int getPOPMail(MailAccountVO mailAccountVO) throws Exception
  {
    int newMessages = 0;
    MailFolderVO defaultFolder = this.getPrimaryEmailFolder(mailAccountVO.getAccountID());
    int attachmentFolderID = this.getAttachmentFolder(mailAccountVO.getOwnerID()).getFolderId();
    Properties properties = System.getProperties();
    //properties.put("mail.debug", "true");
    //TODO: Add the secure POP connection stuff in here.
    //Look at http://www.javaworld.com/javatips/jw-javatip115_p.html
    Session session = Session.getDefaultInstance(properties);
    Store store = session.getStore(mailAccountVO.getAccountType());

    store.connect(mailAccountVO.getMailServer(), mailAccountVO.getLogin(), mailAccountVO.getPassword());

    POP3Folder folder = null;
    Message[] messageArray = null;
    try {
      folder = (POP3Folder)store.getFolder("INBOX");

      // open the remote folder for reading (and writing)
      folder.open(Folder.READ_WRITE);

      // get all the messages from the remote folder
      messageArray = folder.getMessages();
    }catch(MessagingException e){
      try {folder.close(mailAccountVO.isLeaveMessagesOnServer());} catch (Exception ex) {}
      store.close();
    }
    CVDal cvdal = new CVDal(this.dataSource);

    ArrayList messageIDs = new ArrayList();

    // Check wheather the Account is view as a Support Email Account
    boolean isSupportEmailAccount = false;
    isSupportEmailAccount = this.isSupportEmailAccount(mailAccountVO.getAccountID(), cvdal);

    try {
      // Get list of UID's from database for this account
      ArrayList uidList = this.getUidList(mailAccountVO.getAccountID(), cvdal);

      // this will hold all the UIDs that are left on the server, so
      // that we can store them and not download them next time
      ArrayList serverUIDs = new ArrayList();

      // loop through the array of Message objects, and process each
      for (int i=0; i<messageArray.length; i++) {
        // get the unique ID of the message
        String messageUID = folder.getUID(messageArray[i]);

        HashMap embededImageMap = new HashMap();
        // if the uid List in the database does not contain the UID
        // of the current message, then we need to download this message.
        // Note that if uid list is empty, we'll download all messages
        if (! uidList.contains(messageUID)) {
          // save message in database, then save attachments
          int newMessageID = this.addMessage(messageArray[i], messageUID, mailAccountVO.getAccountID(), mailAccountVO.getOwnerID(), defaultFolder.getFolderID(), cvdal);
          this.saveAttachments(this.getPartContent(messageArray[i]), newMessageID, mailAccountVO.getAccountID(), mailAccountVO.getOwnerID(), attachmentFolderID, cvdal, embededImageMap);
          this.addBody(messageArray[i], newMessageID, embededImageMap, cvdal);

          embededImageMap = null;


          // Add the newMessageID to the messageIDs Collection which will
          // be used later by the Rules framework and the Support framework
          messageIDs.add(new Integer(newMessageID));
          newMessages++;
        }   // end if (addMessage)

        if (mailAccountVO.isLeaveMessagesOnServer()) {
          // need to add this UID to the local database, so that
          // we don't download it next time. No need to do so if
          // we're expunging messages from the server.
          serverUIDs.add(messageUID);
          messageArray[i].setFlag(Flags.Flag.SEEN, true);
        }else{
          // need to delete this message from the remote server
          messageArray[i].setFlag(Flags.Flag.DELETED, true);
        }
      }   // end for(int i=0; i<messageArray.length; i++)

      // cleanup uid list in database, adding new uids or deleting the ones that aren't on server anymore
      this.removeInvalidUIDs(mailAccountVO.getAccountID(), serverUIDs, cvdal);

      // if we have just downloaded any messages, we need to apply
      // any rules to the messages that we just downloaded. We'll
      // pass the list of messageIDs (from our database) to
      // this.applyRules() which will do all the rules stuff.
      if (messageIDs != null && messageIDs.size() > 0) {
        this.applyRules(mailAccountVO.getAccountID(), messageIDs, cvdal);
      }
    }finally{
      //Be a good programmer and clean up your connections.
      try { folder.close(! mailAccountVO.isLeaveMessagesOnServer());} catch (Exception e) {}
      try { store.close(); } catch (Exception e) {}
      cvdal.destroy();
      cvdal = null;
    }

    // if the account we just POPped was marked as a support email account,
    // then process all messages as though they are support tickets.
    if (isSupportEmailAccount) {
    this.supportEmail(messageIDs,mailAccountVO.getOwnerID());
  }

    return(newMessages);
  }

  /**
   * Returns an ArrayList of String objects, each String representing
   * a UID for the given accountID. This allows us to know all the
   * currently stored UID's for the given POP account in our database.
   * @param accountID The accountID of the email account we need the UIDs for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return ArrayList of String objects representing the list of UIDs.
   */
  private ArrayList getUidList(int accountID, CVDal cvdal)
  {
    ArrayList uidList = new ArrayList();
    try {
      cvdal.setSqlQuery("SELECT uid FROM uidlist WHERE accountID=?");
      cvdal.setInt(1, accountID);
      Collection results = cvdal.executeQuery();

      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          uidList.add(row.get("uid"));
        }
      }
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return uidList;
  }   // end getUidList(int,CVDal) method

  /**
   * Deletes all UIDs from the uidlist table in the database that are
   * <strong>NOT</strong> contained withing the <code>serverUIDs</code>
   * ArrayList. Actually, we're deleting <strong>all</strong> UIDs from
   * the database, then inserting all the UIDs from the <code>serverUIDs</code>
   * ArrayList back into the database. This is easier and faster than
   * finding the intersection of old and new UIDs.
   * @param accountID The accountID of the email account for which we're purging UIDs.
   * @param serverUIDs The list of UIDs that are <strong>NOT</strong> to be deleted.
   *        The uidlist table in the database should contain the exact same list as
   *        this ArrayList when this method has finished. No more, no less.
   * @param cvdal An <strong>open</strong> database connection.
   * @return void
   */
  private void removeInvalidUIDs(int accountID, ArrayList serverUIDs, CVDal cvdal)
  {
    try {
      // delete and add new UIDs in a single transaction, to potentially avoid
      // duplication issues.  A thought exercise led me to the fact that should be
      // the only case where we would end up with duplications, to avoid it totally
      // we should probably open the transaction before adding any messages and
      // rollback on any exceptions, otherwise commit.  However as a first step
      // I am introducting transactions here, at least the only duplicates would be
      // new ones.
      boolean autoCommit = cvdal.getAutoCommit();
      cvdal.setAutoCommit(false);
      // first, delete all records from the uidlist table for the given accountID
      cvdal.setSqlQuery("DELETE FROM uidlist WHERE accountID=?");
      cvdal.setInt(1, accountID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      // next, add each UID from serverUIDs to the uidlist table
      if (serverUIDs != null && serverUIDs.size() > 0) {
        Iterator iter = serverUIDs.iterator();
        while (iter.hasNext()) {
          String uid = (String)iter.next();
          String query = "INSERT INTO uidlist (accountID, uid) VALUES (?, ?)";
          cvdal.setSqlQuery(query);
          cvdal.setInt(1, accountID);
          cvdal.setString(2, uid);
          cvdal.executeUpdate();
        }
      }
      // close out the transaction.
      cvdal.commit();
      cvdal.setAutoCommit(autoCommit);
    } catch (SQLException e) {
      logger.error("[removeInvalidUIDs] SQL Exception doing fancy transaction stuff.", e);
      // not sure what I can do, I probably should just throw EJBException at this point.
      try {cvdal.rollback();} catch (SQLException se) {} // Give up. Retire.  Start Training, Make a comeback!
      throw new EJBException(e);
    }finally{
      cvdal.setSqlQueryToNull();
    }
  }

  /**
   * This method finds out if the message is currently in the "Trash" folder
   * if so, it is deleted, otherwise it will be moved to the trash folder.<p>
   * It currently implements its own queries to get the information it needs
   * To make it cleaner more readable code, it should probably just get the VOs
   * for the Message, Account and Folder, Then make its decision, then another method
   * should exist that finds the right trash folderid.  As it is, it is probably much
   * more efficient.  If it is anticipated that data structure will change it is probably
   * a good idea.
   * @param messageId the unique ID for a particular message that will be deleted.
   * @param individualId the individual id of the user requesting the delete.
   * @return The number of messages affected by the delete.
   *         This number should always be 0 or 1.
   */
  public int deleteMessage(int messageId, int individualId)
  {
    // find out the serverType for this message, then rely on
    // the more specific method to delete it.
    // Also get the folder name and type, if it is "Trash" of type System
    // then delete it, else move it to the Trash folder for this account.
    CVDal cvdal = new CVDal(this.dataSource);
    int operationResult = 0;
    ResultSet results = null;
    Number accountId = null;
    String serverType = null;
    String folderName = null;
    String folderType = null;
    Number trashFolderId = null;

    try {
      String messageQuery = "SELECT MovedToFolder FROM emailmessage WHERE messageId = ?";
      cvdal.setSqlQuery(messageQuery);
      cvdal.setInt(1,messageId);
      Collection resultsCollection = cvdal.executeQuery();
      cvdal.clearParameters();
      int folderId = 0;
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        while (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          Number movedToFolderID = (Number) resultsHashMap.get("MovedToFolder");
          folderId = movedToFolderID.intValue();
        }
      }

      if (folderId != 0) {
        String query = "SELECT ea.accountId, ea.serverType, emf.Name, emf.Ftype " +
        "FROM emailaccount AS ea, emailmessage AS em, " +
        "emailmessagefolder AS emmf, emailfolder AS emf " +
        "WHERE ea.accountId = em.accountId " +
        "AND emf.folderId = emmf.folderId AND emmf.messageId = em.messageId " +
        "AND em.messageId = ? and emmf.folderId = ?";
        cvdal.setSqlQuery(query);
        cvdal.setInt(1,messageId);
        cvdal.setInt(2,folderId);
        results = cvdal.executeQueryNonParsed();
      }else{
        String query = "SELECT ea.accountId, ea.serverType, emf.Name, emf.Ftype " +
        "FROM emailaccount AS ea, emailmessage AS em, " +
        "emailmessagefolder AS emmf, emailfolder AS emf " +
        "WHERE ea.accountId = em.accountId " +
        "AND emf.folderId = emmf.folderId AND emmf.messageId = em.messageId " +
        "AND em.messageId = ?";
        cvdal.setSqlQuery(query);
        cvdal.setInt(1,messageId);
        results = cvdal.executeQueryNonParsed();
      }

      results.next(); // do it once, we should only have one result.
      accountId = new Integer(results.getInt(1));
      serverType = results.getString(2);
      folderName = results.getString(3);
      folderType = results.getString(4);

      // if it's already in the trash, delete it, otherwise move it to the trash.
      if ((folderName.equals("Trash") || folderName.equals("Drafts")) && folderType.equals("SYSTEM")) {
        operationResult = this.deleteMessage(individualId, messageId, serverType);
      } else {
        // First we gotta figure out which folder id is the trash
        cvdal.setSqlQueryToNull();
        try {results.close();} catch (Exception e) { }
        String query = "SELECT folderId FROM emailfolder WHERE name = 'Trash' AND ftype = 'SYSTEM' AND accountID = ?";
        cvdal.setSqlQuery(query);
        cvdal.setInt(1, accountId.intValue());
        results = cvdal.executeQueryNonParsed();
        results.next(); // do it once, we should only have one result.
        trashFolderId = new Integer(results.getInt(1));
        // now that we know where to move, it simply move it there
        operationResult = this.moveMessageToFolder(individualId, messageId, trashFolderId.intValue());
      }
    }catch (SQLException e){
      System.out.println("[Exception][MailEJB.deleteMessage] Exception Thrown: " + e);
      throw new EJBException(e);
    }finally{
      try {results.close();} catch (Exception e) { }
      cvdal.destroy();
    }
    return operationResult;
  }

  /**
   * If the Mail Account is IMAP, this method marks a message as deleted
   * based on the message ID. The message won't actually be deleted
   * until getIMAPMail is called.<p> If the Mail Account is POP3, the
   * message (and any attachments) will be deleted right away.<p> This
   * method also deletes any related attachments to the email message
   * from both the database and file system.
   * @param individualID The ID of the person deleting this message.
   * @param messageID The ID of the message to be deleted.
   * @param serverType imap - marks messages to be deleted. pop3 -
   *        deletes the messages immediatly.
   * @return The number of messages affected by the delete.
   *         This number should always be 0 or 1.
   */
  private int deleteMessage(int individualID, int messageID, String serverType)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    int numberDeleted = 0;

        try {
      if (serverType.equalsIgnoreCase(MailAccountVO.IMAP_TYPE)) {
        try {
          MailMessageVO messageVO = this.getEmailMessageVO(individualID, messageID);
          MailAccountVO accountVO = this.getMailAccountVO(messageVO.getEmailAccountID());
          int folderID = messageVO.getEmailFolderID();

          Session session = null;
          IMAPStore store = this.setupIMAPConnection(accountVO, session);
          MailFolderVO folderVO = getEmailFolder(folderID);

          String name = folderVO.getFolderFullName();
          if (name == null) {
            name = folderVO.getFolderName();
          }
          IMAPFolder folder = (IMAPFolder)store.getFolder(name);

          folder.open(IMAPFolder.READ_WRITE);
          Message msg = folder.getMessageByUID(Long.parseLong(messageVO.getMessageUID()));
          msg.setFlag(Flags.Flag.DELETED, true);
          folder.close(true);
          numberDeleted = this.deleteMessageLocally(individualID, messageID, cvdal);
        } catch(Exception e) {
          System.out.println("Exception thrown in deleteMessage()");
          e.printStackTrace();
          return(0);
        }
      } else if (serverType.equalsIgnoreCase(MailAccountVO.POP3_TYPE)) {
        numberDeleted = this.deleteMessageLocally(individualID, messageID, cvdal);
      }
    }catch (Exception e){
      System.out.println("[Exception][MailEJB]Exception throw in deleteMessage(int,int,String): " + e);
      e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }

    return numberDeleted;
  }

  /**
   * This method marks a message as deleted based on the message ID.
   * The message won't actually be deleted until getIMAPMail is called.
   * @param individualID The ID of the person deleting this message.
   * @param messageID The ID of the message to be marked as deleted.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of messages marked to be deleted.
   * This number should always be 0 or 1.
   * @throws Exception There may have been a problem deleting
   * the email.
   */
  private int markIMAPMessageDeleted(int individualID, int messageID, CVDal cvdal) throws Exception
  {
    int numberDeleted = 0;

    try {
      String deleteQuery = "UPDATE emailmessage SET LocallyModified = 'YES', " +
                           "LocallyDeleted = 'YES' WHERE MessageID = ?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, messageID);
      numberDeleted = cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();
    }finally{
      cvdal.setSqlQueryToNull();
    }

    return numberDeleted;
  } //end of deleteMessage method

  /**
   * Deletes a message from the database based on the message ID.<p>
   * This method also deletes any related attachments
   * to the email message from both the database and file system.<p>
   * @param individualID The ID of the person deleting this message.
   * @param messageID The ID of the message to be deleted.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of messages affected by the delete.
   *         This number should always be 0 or 1.
   * @throws Exception There may have been a problem deleting the email.
   */
  private int deleteMessageLocally(int individualID, int messageID, CVDal cvdal)
  {
    int numberDeleted = 0;

    try {
      String deleteQuery = "DELETE FROM emailmessage WHERE MessageID = ?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, messageID);
      numberDeleted = cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      deleteQuery = "DELETE FROM emailmessagefolder WHERE MessageID = ?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, messageID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      this.deleteMessageAttachments(individualID, messageID, cvdal);
    }finally{
      cvdal.setSqlQueryToNull();
    }

    return numberDeleted;
  }

  /**
   * This method deletes any attachments related this the specific
   * messageID passed to the method.
   * @param individualID The person deleting the attachments.
   * @param messageID The message to delete any related attachments with it.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of attachments deleted during the execution of this method.
   * @throws Exception Anything that may have gone awry,
   */
  private int deleteMessageAttachments(int individualID, int messageID, CVDal cvdal)
  {
    int numberDeleted = 0;
    CvFileFacade fileFacade = new CvFileFacade();
    CvFolderVO attachmentFolder = getAttachmentFolder(individualID);

    String selectQuery = "SELECT AttachmentID, FileID FROM attachment WHERE MessageID = ?";
    cvdal.setSqlQuery(selectQuery);
    cvdal.setInt(1, messageID);

    Collection resultsCollection = cvdal.executeQuery();
    cvdal.setSqlQueryToNull();

    String deleteQuery = "DELETE FROM attachment WHERE AttachmentID = ?";
    cvdal.setSqlQuery(deleteQuery);

    if (resultsCollection != null) {
      Iterator resultsIterator = resultsCollection.iterator();
      while (resultsIterator.hasNext()) {
        HashMap resultsHashMap = (HashMap) resultsIterator.next();
        Number fileID = (Number) resultsHashMap.get("FileID");
        Number attachmentID = (Number) resultsHashMap.get("AttachmentID");
        try {
          fileFacade.deleteFile(individualID, fileID.intValue(), attachmentFolder.getFolderId(), this.dataSource);
        } catch (Exception e) {
          logger.error("Exception in deleteMessageAttachments");
          e.printStackTrace();
        }

        cvdal.clearParameters();
        cvdal.setInt(1, attachmentID.intValue());
        numberDeleted += cvdal.executeUpdate();
      }
    }

    cvdal.setSqlQueryToNull();

    return numberDeleted;
  }

  /**
   * This method takes in a list of messages that shouldn't be deleted.
   * It then gets any messages that are no longer valid in the current
   * system. It then loops through each non-valid message and deletes it
   * (and any related attachments). <p>This method should execute quickly
   * because the raw result set is used rather the the Collection from CVDal.
   * @param individualID The individual deleting the messages.
   * @param accountID The account to delete the messages from.
   * @param validMessageIDs A collection of messages that SHOULDN'T be deleted.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of messages that were deleted.
   * @throws Exception Something went wrong during the execution of this method.
   */
  private int deleteInvalidMessages(int individualID, int accountID, Collection validMessageIDs, int folderID, CVDal cvdal)
  {
    int numberDeleted = 0;
    ResultSet resultSet = null;
    try {
      StringBuffer sb = new StringBuffer();
      if (validMessageIDs != null) {
        Iterator it = validMessageIDs.iterator();
        while (it.hasNext()) {
          sb.append((String) it.next());
          if (it.hasNext()) {
            sb.append(", ");
          }
        }
      }

      Random rand = new Random();
      StringBuffer tablename = new StringBuffer("folderfilter");
      for (int n = 0; n < 4; n++) {
        tablename.append(rand.nextInt(10));
      }
      if (folderID > -1) {
        String query = "CREATE TEMPORARY TABLE " + tablename + " " +
            "SELECT MessageID FROM emailmessagefolder WHERE FolderID = " + folderID;
        cvdal.setSqlQuery(query);
        cvdal.executeUpdate();
        cvdal.setSqlQueryToNull();
      }

      if (sb.length() > 0) {
        String select = "SELECT em.MessageID ";
        String from = "FROM emailmessage em ";
        if (folderID > -1) {
          from = from.concat(", " + tablename +" ff ");
        }
        String where = "WHERE em.AccountID = ? AND em.MessageID NOT IN (" + sb.toString() + ")";
        if (folderID > -1) {
          where = where.concat(" AND em.MessageID = ff.MessageID");
        }
        cvdal.setSqlQuery(select + from + where);
        cvdal.setInt(1, accountID);
        resultSet = cvdal.executeQueryNonParsed();
        while (resultSet.next()) {
          Number messageID = (Number) resultSet.getObject(1);
          numberDeleted += this.deleteMessageLocally(individualID, messageID.intValue(), cvdal);
        }
        cvdal.setSqlQueryToNull();

        if (folderID > -1) {
          cvdal.setSqlQuery("DROP TABLE " + tablename);
          cvdal.executeUpdate();
          cvdal.setSqlQueryToNull();
        }
      }
    } catch (SQLException se) {
      logger.error("[deleteInvalidMessages] Exception thrown.", se);
    } finally{
      if (resultSet != null) {
        try {resultSet.close();} catch (SQLException se) {}
      }
      cvdal.setSqlQueryToNull();
    }
    return numberDeleted;
  }

  /**
   * This method takes a Message object and update the body and contentType
   * it's contents to the database.
   * <p> If there is a problem inserting the new record </P>
   *
   * @param messageObject The raw Message object to be saved to the database.
   * @param messageID The message ID for the message which we are processing.
   * @param embededImageMap An Collection of contentid for embeded image should be replace with new path
   * @param cvdal An <strong>open</strong> database connection.
   * @return void.
   * @throws MessagingException when processing the message some of method throws the MessagingException.
   *
   */
  private void addBody(Message messageObject, int messageID, HashMap embededImageMap, CVDal cvdal) throws MessagingException
  {
    // Newly Created Message. we will update the contentType and Body of the message.
    String updateMessage = "UPDATE emailmessage SET contentType = ?, Body = ?  WHERE MessageID = ? ";
    
    cvdal.setSqlQuery(updateMessage);
    
    // Start figuring out ContentType stuff...
    HashMap bodyContentMap = this.getMessageBody(this.getPartContent(messageObject));
    String bodyContent = (String)bodyContentMap.get("messageContent");
    String bodyContentType = (String)bodyContentMap.get("contentType");
    Boolean isMultipart = (Boolean)bodyContentMap.get("isMultipart");
    
    if (isMultipart.booleanValue() == true) {
      // if this is a multipart message, then we figured out the
      // content type for the Body part inside of this.getMessageBody(),
      // so just use that value (check for nulls)
      cvdal.setString(1, (bodyContentType == null) ? MailMessageVO.PLAIN_TEXT_TYPE : bodyContentType);
    }else{
      // if this is NOT a multipart message, check to see if the
      // messageObject.getContentType() is text/plain. If so, then
      // set content type in db appropriately. If not, set it
      // to text/html
      String messageContentType = messageObject.getContentType();
      if (messageContentType != null &&  messageContentType.toLowerCase().indexOf("text/plain") > -1) {
        cvdal.setString(1, MailMessageVO.PLAIN_TEXT_TYPE);
      }else{
        cvdal.setString(1, MailMessageVO.HTML_TEXT_TYPE);
      }
    }
    
    // We will get the key and look for the cid:image001.gif with /imageServlet?fileID=12
    // we will replace it with body.
    if (embededImageMap != null && embededImageMap.size() != 0) {
      Set imageSet = embededImageMap.keySet();
      Iterator imageIterator = imageSet.iterator();
      while (imageIterator.hasNext()) {
        String imageKey = (String) imageIterator.next();
        String imageKeyValue = (String) embededImageMap.get(imageKey);
        bodyContent = bodyContent.replaceAll(imageKey,imageKeyValue);
      }
    }
    
    cvdal.setString(2, bodyContent);
    cvdal.setInt(3, messageID);
    cvdal.executeUpdate();
    cvdal.setSqlQueryToNull();
  }   // end addBody() method

  /**
   * This method takes a Message object and saved it's contents to the
   * database (except for attachments). <p>The new MessageID from the
   * Database is returned. If there is a problem inserting the new record,
   * <code>-1</code> will be returned and an exception will probably be thrown.
   * @param messageObject The raw Message object to be saved to the database.
   * @param messageUID The Unique ID from the message server.
   * @param accountID The Email Account ID where this email message will be stored.
   * @param emailFolderID The ID of the message folder to store this email message.
   * @param ownerID The owner of the Email Account (who will also be the owner of this message).
   * @param cvdal An <strong>open</strong> database connection.
   * @return The new message ID if the message was created in the database, otherwise -1 is returned.
   * @throws IOException when we try to read the getContent method of the message then it will throw IOException.
   * @throws MessagingException when processing the message some of method throws the MessagingException.
   *
   */
  private int addMessage(Message messageObject, String messageUID, int accountID, int ownerID, int emailFolderID, CVDal cvdal) throws MessagingException
  {
    int newMessageID = -1;

    // We could build the MailMessageVO Object and
    // pass the object to a method to insert the message,
    // but that will mean (re)opening an CVDal connection
    // everytime we add a message, which can be SLOW...
    // So, let's just do it here.
    String insertMessage =
      "INSERT INTO emailmessage (MessageDate, MailFrom, FromIndividual, ReplyTo, " +
      "Subject, Headers, Owner, size, Priority, Importance, AccountID, UID, " +
      "LocallyDeleted, LocallyModified, MessageRead, ToList) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    cvdal.setSqlQuery(insertMessage);

    // Collect the SeenFlag before calling the getContent()
    boolean seenFlag = messageObject.isSet(Flags.Flag.SEEN);

    Date sentDate = messageObject.getSentDate();
    if (messageObject.getSentDate() == null) {
      sentDate = messageObject.getReceivedDate();
      if (sentDate == null) {
        sentDate = new Date();
        sentDate.setTime(System.currentTimeMillis());
      }
    }

    java.sql.Timestamp myTs = new java.sql.Timestamp(sentDate.getTime());
    cvdal.setRealTimestamp(1, myTs);

    String fromAddress = "";
    try {
      Address fromAddresses[] = messageObject.getFrom();
      if (fromAddresses != null && fromAddresses.length > 0) {
        fromAddress = fromAddresses[0].toString();
      }
    } catch (AddressException ae) {
      String fromAddresses[] = messageObject.getHeader("From");
      if (fromAddresses != null && fromAddresses.length > 0) {
        fromAddress = fromAddresses[0].toString();
    }
    }
    if (fromAddress != null && fromAddress.equals("null")) {
      fromAddress = "";
    }
    cvdal.setString(2, fromAddress);

    cvdal.setInt(3, 0);
    cvdal.setString(4, fromAddress);
    cvdal.setString(5, messageObject.getSubject());

    cvdal.setString(6, this.getMessageHeaders(messageObject.getAllHeaders()));

    cvdal.setInt(7, ownerID);
    cvdal.setInt(8, this.getMessageSize(messageObject));

    String priority = "MEDIUM";
    String priorityIndex[] = messageObject.getHeader("X-Priority");
    // Get the Header Value to find the importance of message.
    if (priorityIndex != null ) {
      for (int i = 0; i < priorityIndex.length; i++ ) {
        String tempPriority = priorityIndex[0];
        // If the header is containing Priority value "1" || "High"
        // Means its a high priority mail
        if (tempPriority != null && (tempPriority.indexOf("1") != -1 || tempPriority.indexOf("High") != -1)) {
          priority = "HIGH";
        }
        // If the header is containing Low value "3" || "Low"
        // Means its a low priority mail
        if (tempPriority != null && (tempPriority.indexOf("3") != -1 || tempPriority.indexOf("Low") != -1)) {
          priority = "LOW";
        }
      }
    }

    cvdal.setString(9, priority);
    cvdal.setString(10, "NO");

    cvdal.setInt(11, accountID);
    cvdal.setString(12, messageUID);

    String to[] = messageObject.getHeader("To");
    //  TODO: Handle array better, although there should never be more than one To: header.
    String toAddress = "";

    if (to != null && to.length > 0) {
      if (to[0] != null) {
        toAddress = to[0];
      }
    }
    cvdal.setString(16, toAddress);
    //Since these messages are new to the system,
    //they are not read or locally modified.
    cvdal.setString(13, "NO");
    cvdal.setString(14, "NO");

    // if seenFlag is true then message is seen on the server
    cvdal.setString(15, seenFlag ? "YES" : "NO");

    cvdal.executeUpdate();
    newMessageID = cvdal.getAutoGeneratedKey();
    cvdal.setSqlQueryToNull();

    if (newMessageID > -1) {
      insertMessage = "INSERT INTO emailmessagefolder (MessageID, FolderID) VALUES(?, ?)";
      cvdal.setSqlQuery(insertMessage);
      cvdal.setInt(1, newMessageID);
      cvdal.setInt(2, emailFolderID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      // stick the lastUID in the database for the account.
      insertMessage = "UPDATE emailaccount SET lastUID = ? WHERE accountId = ?";
      cvdal.setSqlQuery(insertMessage);
      cvdal.setString(1, messageUID);
      cvdal.setInt(2, accountID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();
    } //end of if statement (newMessageID > -1)

    // While parsing (TO/CC/BCC) address
    // for example "test@example.com,,"
    // 1) IMAP account does following stuff
    //        a) It will replace "" blank Address with "UNEXPECTED_DATA_AFTER_ADDRESS@.SYNTAX-ERROR."
    //           its because IMAP uses another Address class which is IMAPAddress
    //           thats why it never throws AddressException when we have blank address.
    // 2) POP Account does following stuff
    //        a) it uses the normal Address class and not able to restore the blank address with some dummy address
    //           thats why it throws the AddressException.
    //        b) we must have to use the parseRecipients method and replace all the ",," with a single ","
    //        c) we must have also look for String ending with "," should be replaced by ""
    try {
      // Add recipient information to the emailrecipient table
      Address[] toRecipients = messageObject.getRecipients(Message.RecipientType.TO);
      this.addRecipients(toRecipients, newMessageID, "TO", cvdal);
    }catch(AddressException e){
      String tempToList[] = messageObject.getHeader("To");
      this.parseRecipients(tempToList, newMessageID, "TO", cvdal);
    }

    try {
      Address[] ccRecipients = messageObject.getRecipients(Message.RecipientType.CC);
      this.addRecipients(ccRecipients, newMessageID, "CC", cvdal);
    }catch(AddressException e){
      String tempToList[] = messageObject.getHeader("Cc");
      this.parseRecipients(tempToList, newMessageID, "CC", cvdal);
    }

    try {
      Address[] bccRecipients = messageObject.getRecipients(Message.RecipientType.BCC);
      this.addRecipients(bccRecipients, newMessageID, "BCC", cvdal);
    }catch(AddressException e){
      String tempToList[] = messageObject.getHeader("Bcc");
      this.parseRecipients(tempToList, newMessageID, "BCC", cvdal);
    }
    return newMessageID;
  }

  /**
   * Saves a draft message in the database and returns the ID of that message.
   * <strong>This is also called by Save Template, since the same exact logic
   * is applied with the exception of where the message is stored.</strong>
   * @param individualID The user who is adding the draft message.
   * @param messageVO The MailMessageVO object representing the draft message.
   * @return int - the messageID of the new draft message or -1 for error.
   */
  public int saveDraft(int individualID, MailMessageVO messageVO) throws Exception
  {
    int messageID = messageVO.getMessageID();
    CVDal cvdal = new CVDal(this.dataSource);
    
    try {
      if (messageID <= 0) {
        String insertMessage = "INSERT INTO emailmessage (MessageDate, MailFrom, FromIndividual, " +
                               "ReplyTo, Subject, Headers, Owner, size, Priority, Importance, " +
                               "Body, AccountID, UID, LocallyDeleted, LocallyModified, MessageRead) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        cvdal.setSqlQuery(insertMessage);

        Date receivedDate = messageVO.getReceivedDate();
        if (receivedDate == null) {
          receivedDate = new Date();
        }
        
        java.sql.Timestamp myTs = new java.sql.Timestamp(receivedDate.getTime());
        cvdal.setRealTimestamp(1, myTs);
        cvdal.setString(2, messageVO.getFromAddress());
        cvdal.setInt(3, 0);
        cvdal.setString(4, messageVO.getReplyTo());
        cvdal.setString(5, messageVO.getSubject());
        cvdal.setString(6, messageVO.getHeaders());
        cvdal.setInt(7, individualID);
        cvdal.setInt(8, 0);
        cvdal.setString(9, "MEDIUM");
        cvdal.setString(10, "NO");
        cvdal.setString(11, messageVO.getBody());
        cvdal.setInt(12, messageVO.getEmailAccountID());
        cvdal.setString(13, "");
        cvdal.setString(14, "NO");
        cvdal.setString(15, "NO");
        cvdal.setString(16, "NO");

        cvdal.executeUpdate();
        messageID = cvdal.getAutoGeneratedKey();
        cvdal.clearParameters();
        cvdal.setSqlQueryToNull();

        if (messageID > -1) {
          insertMessage = "INSERT INTO emailmessagefolder (MessageID, FolderID) VALUES(?, ?)";
          cvdal.setSqlQuery(insertMessage);
          cvdal.setInt(1, messageID);
          cvdal.setInt(2, messageVO.getEmailFolderID());
          cvdal.executeUpdate();
          cvdal.clearParameters();
          cvdal.setSqlQueryToNull();
        }
        
        cvdal.setSqlQuery("DELETE FROM attachment WHERE MessageID = ?");
        cvdal.setInt(1, messageID);
        cvdal.executeUpdate();
        cvdal.clearParameters();
        cvdal.setSqlQueryToNull();
      } else {
        String updateMessage = "UPDATE emailmessage SET MessageDate = ?, " +
                               "MailFrom = ?, Subject = ?, Body = ?, AccountID = ? " +
                               " WHERE messageID = ?";

        cvdal.setSqlQuery(updateMessage);
        
        Date receivedDate = messageVO.getReceivedDate();
        if (receivedDate == null) {
          receivedDate = new Date();
        }
        
        java.sql.Timestamp myTs = new java.sql.Timestamp(receivedDate.getTime());
        cvdal.setRealTimestamp(1, myTs);
        cvdal.setString(2, messageVO.getFromAddress());
        cvdal.setString(3, messageVO.getSubject());
        cvdal.setString(4, messageVO.getBody());
        cvdal.setInt(5, messageVO.getEmailAccountID());
        cvdal.setInt(6, messageID);
        cvdal.executeUpdate();
        cvdal.clearParameters();
        cvdal.setSqlQueryToNull();
        
        cvdal.setSqlQuery("DELETE FROM emailrecipient WHERE MessageID = ?");
        cvdal.setInt(1, messageID);
        cvdal.executeUpdate();
        cvdal.clearParameters();
        cvdal.setSqlQueryToNull();
        
        cvdal.setSqlQuery("DELETE FROM attachment WHERE MessageID = ?");
        cvdal.setInt(1, messageID);
        cvdal.executeUpdate();
        cvdal.clearParameters();
        cvdal.setSqlQueryToNull();
      }
      
      // Handle attachments
      Collection attachments = messageVO.getAttachedFiles();
      if (attachments != null) {
        Iterator attachmentIterator = attachments.iterator();
        while (attachmentIterator.hasNext()) {
          CvFileVO thisAttachment = (CvFileVO) attachmentIterator.next();
          String path = thisAttachment.getPhysicalFolderVO().getFullPath(null, true) + thisAttachment.getName();
          String insertQuery = "INSERT INTO attachment (MessageID, FileName, FileID) VALUES(?, ?, ?)";
          cvdal.setSqlQuery(insertQuery);
          cvdal.setInt(1, messageID);
          cvdal.setString(2, thisAttachment.getName());
          cvdal.setInt(3, thisAttachment.getFileId());
          cvdal.executeUpdate();
          cvdal.clearParameters();
          cvdal.setSqlQueryToNull();
        }
      }
      
      ArrayList toList = (ArrayList)messageVO.getToList();
      Address[] toRecipients = new Address[toList.size()];
      Iterator toIter = toList.iterator();
      int count = 0;
      while (toIter.hasNext()) {
        String address = (String)toIter.next();
        toRecipients[count] = (Address)(new InternetAddress(address));
        count++;
      }
      
      this.addRecipients(toRecipients, messageID, "TO", cvdal);
      
      ArrayList ccList = (ArrayList)messageVO.getCcList();
      Address[] ccRecipients = new Address[ccList.size()];
      Iterator ccIter = ccList.iterator();
      count = 0;
      while (ccIter.hasNext()) {
        String address = (String)ccIter.next();
        ccRecipients[count] = (Address)(new InternetAddress(address));
        count++;
      }
      
      this.addRecipients(ccRecipients, messageID, "CC", cvdal);
      
      ArrayList bccList = (ArrayList)messageVO.getBccList();
      Address[] bccRecipients = new Address[bccList.size()];
      Iterator bccIter = bccList.iterator();
      count = 0;
      while (bccIter.hasNext()) {
        String address = (String)bccIter.next();
        bccRecipients[count] = (Address)(new InternetAddress(address));
        count++;
      }
      this.addRecipients(bccRecipients, messageID, "BCC", cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in saveDraft(int,MailMessageVO): " + e);
      e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return messageID;
  }   // end saveDraft(int,MailMessageVO) method

  /**
   * Gets and returns the Attachment Folder for a given user.
   * If this folder does not exist, a CVFolderVO with a FolderID
   * of <code>-1</code> will be returned.
   * @param individualID The User to find the attachment folder for.
   * @return A populated attachment folder CvFolderVO.
   */
  public CvFolderVO getAttachmentFolder(int individualID)
  {
    CvFolderVO attachmentFolderVO = new CvFolderVO();
    

    try {
      InitialContext ic = new InitialContext();
      CvFileLocalHome home = (CvFileLocalHome)ic.lookup("local/CvFile");
      CvFileLocal remote =  home.create();
      remote.setDataSource(this.dataSource);

      CvFolderVO homeFolderVO = remote.getHomeFolder(individualID);

      try {
        attachmentFolderVO = remote.getFolderByName(individualID, homeFolderVO.getFolderId(), CvFolderVO.EMAIL_ATTACHMENT_FOLDER);
      }catch(CvFileException cfe){
        //If the Attachment folder is not created, create it.
        int newFolderID = this.createAttachmentFolder(individualID);
        attachmentFolderVO = remote.getFolder(individualID, newFolderID);
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] getAttachmentFolder(int): " + e);
      //e.printStackTrace();
    }
    return attachmentFolderVO;
  }   // end getAttachmentFolder(int) method

  /**
   * Creates an attachment folder for a user. This should only be called if
   * the email attachment folder does not exist. If a new folder was created,
   * the ID of that new folder will be returned.
   * @param individualID The individual for whome we are creating the attachments folder.
   * @return int the folderID of the newly created attachments folder.
   */
  public int createAttachmentFolder(int individualID)
  {
    int newFolderID = -1;
    try {
      CvFileFacade fileFacade = new CvFileFacade();
      CvFolderVO homeFolderVO = fileFacade.getHomeFolder(individualID, this.dataSource);

      CvFolderVO attachmentFolderVO = new CvFolderVO();
      attachmentFolderVO.setCreatedBy(individualID);
      attachmentFolderVO.setDescription("Email Attachments are kept here.");
      attachmentFolderVO.setOwner(individualID);
      attachmentFolderVO.setName(CvFolderVO.EMAIL_ATTACHMENT_FOLDER);
      attachmentFolderVO.setParent(homeFolderVO.getFolderId());

      try {
        newFolderID = fileFacade.addFolder(individualID, attachmentFolderVO, this.dataSource);
      }catch(CvFileException cfe){
        cfe.printStackTrace();
        // TODO: what the hell do we do if we could not create the attachments folder?
      }
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.createAttachmentFolder: " + e);
      e.printStackTrace();
    }

    if (newFolderID == 0) {
      newFolderID = -1;
    }

    return newFolderID;
  }   // end createAttachmentFolder(int) method

  /**
   * Gets and returns the message body from content of a message.
   * @param messageContent The Message content of the Message.
   * @return A HashMap containing three elements:
   * <ul><li>contentType - The content type of the message content</li>
   * <li>messageContent - The message body as a string.</li>
   * <li>isMultipart - Boolean representing if the message is multipart or not.</li></ul>
   */
  private HashMap getMessageBody(Object messageContent)
  {
    HashMap messageMap = new HashMap();
    String messageBody = "";
    String contentType = "";
    Boolean isMultipart = new Boolean(false);

    try {
      if (messageContent instanceof Multipart) {
        // if the Content-Type of the message is multipart, then
        // we need to search for the appropriate part to save as
        // the Body in our database. For our purposes, we ideally
        // want the HTML Part, falling back on Plain Text if there
        // is no suitable HTML Part.
        Multipart multipart = (Multipart)messageContent;
        isMultipart = new Boolean(true);

        boolean htmlPartFound = false;
        boolean textPartFound = false;

        // loop through each Part of this message, looking for the ideal Part to save.
        for (int i = 0; i < multipart.getCount(); i++) {
          MimeBodyPart part = (MimeBodyPart)multipart.getBodyPart(i);
          String partContentType = part.getContentType();
          String disposition = part.getDisposition();

          if (disposition == null || disposition.toLowerCase().equals("inline")) {
            // do not scan the part if the disposition is "attachment"
            if ((partContentType.toLowerCase().indexOf("text/plain") > -1)) {
              // this Part is text/plain.
              if (! textPartFound) {
                // if we have NOT already found a plain text part, then *temporarily*
                // save the content of this part to be used as the Body in our database.
                // We'll then continue to loop through the Parts, looking for an HTML
                // Part to be used. If we HAVE already found a plain text part, then
                // we're not going to process any more plain text parts, we'll always
                // use the first plain text Part we find unless we find an HTML Part.
                messageBody = this.getBodyContentFromPart(part);
                contentType = MailMessageVO.PLAIN_TEXT_TYPE;
                textPartFound = true;
              }
            }else if (partContentType.toLowerCase().indexOf("text/html") > -1){
              // This Part is text/html, let's use its contents as the Body of
              // our message in the database. We'll set a flag to tell us to stop
              // processing Parts, because we've found a suitable Body for our needs.
              messageBody = this.getBodyContentFromPart(part);
              contentType = MailMessageVO.HTML_TEXT_TYPE;
              htmlPartFound = true;
            }else if (partContentType.toLowerCase().indexOf("multipart") > -1){
              // if the content type of this Part is "multipart/xxx" (where "xxx" is
              // anything), then we'll call this method recursively, passing in the
              // content of this Part. It will be processed accordingly, passing back
              // the content and content type of any text or html sub-Parts found
              // within. (ideally, an HTML Part will be found, but plain text is cool).
              HashMap subMessageMap = this.getMessageBody(this.getPartContent(part));
              contentType = (String)subMessageMap.get("contentType");
              messageBody = (String)subMessageMap.get("messageContent");
              isMultipart = (Boolean)subMessageMap.get("isMultipart");

              // now, tell our current execution of this method if we've found an
              // HTML or TEXT Part yet (so we can either keep looking or stop).
              // Note that if "contentType" is not set to HTML or TEXT, then
              // the current execution of this method will keep looking (looping)
              if (contentType.equals(MailMessageVO.PLAIN_TEXT_TYPE))
              {
                textPartFound = true;
              }else if (contentType.equals(MailMessageVO.HTML_TEXT_TYPE)){
                htmlPartFound = true;
              }
            }

            // don't quit looping until we've either found the text/html part,
            // or we've finished checking ALL the Parts. We ideally want to
            // save the HTML part as the Body in our database, but if we can't
            // find it, we'll save the text/plain part.
            if (htmlPartFound){ break; }
          }
        }
      }else if (messageContent instanceof InputStream){
        // The content of this part is formatted as a raw InputStream.
        // We need to decode the bytes of that stream, saving it one
        // character at a time and appending each character to a StringBuffer.
        // This means this message is not a multipart, it must be a text part
        // Set contentType = plain text for now. In this.addMessage(), we'll get
        // the content type from the MimeMessage itself and act appropriately
        StringBuffer buffer = new StringBuffer();
        int n;
        while ((n = ((InputStream)messageContent).read()) != -1) {
          buffer.append((char)n);
        }
        messageBody = (String)buffer.toString();
        contentType = MailMessageVO.PLAIN_TEXT_TYPE;
      }else{
        // ok, if we're dealing with a NON-multipart message, then
        // set contentType = plain text for now. In this.addMessage(),
        // we'll get the content type from the MimeMessage itself and
        // act appropriately
        messageBody = (String)messageContent.toString();
        contentType = MailMessageVO.PLAIN_TEXT_TYPE;
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] getMessageBody(Object,String): " + e);
      e.printStackTrace();
    }
    messageMap.put("contentType", contentType);
    messageMap.put("messageContent", messageBody);
    messageMap.put("isMultipart", isMultipart);
    return messageMap;
  }   // end getMessageBody(Object,String) method

  /**
   * Takes a MimeBodyPart, and returns a String representation of the
   * getContent() of that MimeBodyPart. In most cases, getContent()
   * will return a String, but it may also return some subclass of
   * InputStream, which we will have to decode and turn into a String.
   * <strong>BodyParts whose disposition is "attachment" should never be
   * given to this method!</strong> Disposition = null or "inline" is fine.
   * @param part A MimeBodyPart object for which we want to get a String represenation.
   * @return String representation of the getContent() of the MimeBodyPart given to the method.
   */
  private String getBodyContentFromPart(MimeBodyPart part) throws Exception
  {
    String messageBody = "";
    Object partContent = this.getPartContent(part);

    if (partContent instanceof String) {
      // the content is already a String object, just use it
      messageBody = (String)partContent.toString();
    }else if (partContent instanceof Multipart){
      messageBody = this.getBodyContentFromPart((MimeBodyPart)partContent);
    }else{
      // the content of this part is formatted as a raw InputStream.
      // We need to decode the bytes of that stream, saving it one
      // character at a time and appending each character to a StringBuffer.
      StringBuffer buffer = new StringBuffer();
      int n;
      while ((n = ((InputStream)partContent).read()) != -1) {
        buffer.append((char)n);
      }
      messageBody = (String)buffer.toString();
    }
    return messageBody;
  }   // end getBodyContentFromPart(MimeBodyPart)


  /**
   * Returns a <code>newline</code> delimited
   * String of all of the headers in the Message object.<p>
   * Thie method requires an Enumeration because when you call
   * message.getAllHeaders() an Enumeration is returned.
   * @param headerEnumeration An Enumeration of the Message headers.
   * @return A <code>newline</code> delimited String with the headers.
   */
  private String getMessageHeaders(Enumeration headerEnumeration)
  {
    StringBuffer headers = new StringBuffer();

    while (headerEnumeration.hasMoreElements()) {
      Header header = (Header)headerEnumeration.nextElement();
      headers.append(header.getName() + ": " + header.getValue());

      //Add a newline on
      if (headerEnumeration.hasMoreElements()) {
        headers.append(System.getProperty("line.separator", "\n"));
      }
    }   // end while (headerEnumeration.hasMoreElements())
    return headers.toString();
  } //end of getMessageHeaders method




  /**
   * Goes through a message and saves off any Attachments
   * (inline or otherwise) to the EMAIL_ATTACHMENTS
   * directory in a user's folder.<p>
   * The number of attachments saved is returned from this method.
   * @param messageContent The actual message object.
   * @param messageID The MessageID from the database.
   * @param accountID The Email Account ID.
   * @param ownerID The Individual ID of the receiver.
   * @param attachmentFolderID The database ID of the
   *        user's attachment folder.
   * @param cvdal An <strong>open</strong> database connection.
   * @param embededImageMap An Collection of contentid for embeded image should be replace with new path
   * @return The number of attachments added for this message.
   */
  private int saveAttachments(Object messageContent, int messageID, int accountID, int ownerID, int attachmentFolderID, CVDal cvdal, HashMap embededImageMap)
  {
    int numberOfAttachments = 0;
    CvFileFacade fileFacade = new CvFileFacade();

    try {
      if (messageContent instanceof Multipart) {
        // we only process the attachments from Multipart messages
        Multipart multipart = (Multipart) messageContent;

        String insertQuery = "INSERT INTO attachment (MessageID, FileName, FileID) VALUES(?, ?, ?)";
        cvdal.setSqlQuery(insertQuery);

        for (int i = 0; i < multipart.getCount(); i++) {
          // loop through the parts, processing each one...
          Part part = (Part) multipart.getBodyPart(i);
          String contentType = part.getContentType();
          String disposition = part.getDisposition();

          if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE))) {
            // by SPEC, we should only save those parts whose disposition is "attachment" or "inline".
            // disposition == null means that it is a content part (message body, readable content)
            CvFileVO attachment = new CvFileVO();

            String filename = (part.getFileName() != null) ? (String)part.getFileName() : "Part-" + i;

            // prepend human readable date to the fileName. This is for uniqueness.
            SimpleDateFormat df = new SimpleDateFormat("MMMM_dd_yyyy_hh_mm_ss_S");
            String prependDate = df.format(new Date());
            attachment.setName(prependDate + "-" + filename);
            attachment.setTitle(filename);
            attachment.setOwner(ownerID);
            attachment.setAuthorId(ownerID);
            attachment.setCreatedBy(ownerID);
            attachment.setDescription("Email Attachment: " + filename);
            attachment.setPhysical(CvFileVO.FP_PHYSICAL);

            if (attachment.getTitle() == null || (attachment.getTitle()).length() <= 0) {
              // this is not good, and we should never reach this point, because I
              // have added some fail-proof default content for filename above
              return -1;
            }

            int newFileID = fileFacade.addFile(ownerID, attachmentFolderID, attachment, part.getInputStream(), this.dataSource);

            if (newFileID > 0) {
              cvdal.clearParameters();
              cvdal.setInt(1, messageID);
              cvdal.setString(2, attachment.getTitle());
              cvdal.setInt(3, newFileID);
              int rowsAffected = cvdal.executeUpdate();
            }

            numberOfAttachments++;
          }else if(contentType != null && contentType.toLowerCase().indexOf("rfc822") > -1){
            // The attachment is itself an rfc822 message. Therefore, we need to
            // create a new MailMessage object, and process all its parts too
            this.saveAttachments(this.getPartContent(part), messageID, accountID, ownerID, attachmentFolderID, cvdal, embededImageMap);
          } else if (contentType != null && (contentType.length() >= 10) && ((contentType.toLowerCase().substring(0, 10)).indexOf("image") != -1)) {
            // The attachment is itself an rfc2387 message. Therefore, we need to
            // create a new MailMessage object, and process all the embeded image parts too
            CvFileVO attachment = new CvFileVO();

            String contentID = null;
      String partContentID[] = part.getHeader("Content-ID");
      if(partContentID != null && partContentID.length != 0){
        contentID = partContentID[0];
      }
            String filename = (part.getFileName() != null) ? (String)part.getFileName() : "Part-" + i;

            // prepend human readable date to the fileName. This is for uniqueness.
            SimpleDateFormat df = new SimpleDateFormat("MMMM_dd_yyyy_hh_mm_ss_S");
            String prependDate = df.format(new Date());
            String storedFileName = prependDate + "-" + filename;
            attachment.setName(storedFileName);
            attachment.setTitle(filename);
      attachment.setOwner(ownerID);
            attachment.setAuthorId(ownerID);
            attachment.setCreatedBy(ownerID);
            attachment.setDescription("Email Attachment: " + filename);
            attachment.setPhysical(CvFileVO.FP_PHYSICAL);

            if (attachment.getTitle() == null || (attachment.getTitle()).length() <= 0) {
              // this is not good, and we should never reach this point, because I
              // have added some fail-proof default content for filename above
              return -1;
            }

            int newFileID = fileFacade.addFile(ownerID, attachmentFolderID, attachment, part.getInputStream(), this.dataSource);

            if (newFileID > 0) {
              //According to rfc. Some email client will not add the less than and greather than in the content-ID
              //We will replace lessthen and greaterthen with blank
              contentID = contentID.replaceAll("<","");
              contentID = contentID.replaceAll(">","");
              embededImageMap.put("cid:"+contentID,"/centraview/imageServlet?fileID="+newFileID);
            }//end of if (newFileID > 0)
          }   // end if (contentType != null ...
        }   // end for (int i = 0; i < multipart.getCount(); i++)
      }   // end if (messageContent instanceof Multipart)
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] saveAttachments(Object,int,int,int,int,CVDal): " + e);
      //e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(numberOfAttachments);
  }   // end saveAttachments(Object,int,int,int,int,CVDal) method

  /**
   * Sends an email message and saves it to the sent folder.
   * @param mailAccountVO The account information.
   * @param mailMessageVO The email message to send.
   * @return true if the message was sent, false otherwise.
   * @throws SendFailedException If the message could not be
   * sent to some or any of the recipients.
   * @throws IOException when we try to read the getContent method of the message then it will throw IOException.
   * @throws MessagingException when processing the message some of method throws the MessagingException.
   */
  public boolean sendMessage(MailAccountVO mailAccountVO, MailMessageVO mailMessageVO) throws MessagingException
  {
    boolean messageSent = false;
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      // I'm assuming that the sent folder is called Sent.
      // big assumption, I know.
      MailFolderVO sentFolder = this.getEmailFolderByName(mailAccountVO.getAccountID(), MailFolderVO.SENT_FOLDER_NAME);

      CvFolderVO attachmentFolder = this.getAttachmentFolder(mailAccountVO.getOwnerID());

      Address arrayBcc[] = new Address[0];
      Address arrayCc[] = new Address[0];
      Address arrayTo[] = new Address[0];

      //Build the JavaMail message
      Properties props = System.getProperties();
      if (mailAccountVO.getSmtpServer() != null) {
        props.put("mail.smtp.host", mailAccountVO.getSmtpServer());
      } else {
        logger.error("[sendMessage]: The accountVO doesn't have a smtpserver setup: "+mailAccountVO);
        throw new SendFailedException("<error>The SMTP Server has not been setup.</error>");
      }

      Session session = Session.getDefaultInstance(props, null);
      MimeMessage message = new MimeMessage(session);

      Collection bccList = mailMessageVO.getBccList();
      Collection ccList = mailMessageVO.getCcList();
      Collection toList = mailMessageVO.getToList();
      Collection attachments = mailMessageVO.getAttachedFiles();
      String subject = mailMessageVO.getSubject();
      String body = mailMessageVO.getBody();
      String fromAddress = mailMessageVO.getFromAddress();
      String replyToAddress = mailMessageVO.getReplyTo();
      String headers = mailMessageVO.getHeaders();
      String messageType = mailMessageVO.getContentType();

      arrayBcc = this.getAddressAddress(bccList);
      arrayCc = this.getAddressAddress(ccList);
      arrayTo = this.getAddressAddress(toList);

      message.addRecipients(Message.RecipientType.TO, arrayTo);
      message.addRecipients(Message.RecipientType.CC, arrayCc);
      message.addRecipients(Message.RecipientType.BCC, arrayBcc);

      message.setFrom(new InternetAddress(fromAddress));
      // So we don't require a Reply-To address
      if (replyToAddress != null && replyToAddress.length() != 0) {
        message.setReplyTo(new Address[] {new InternetAddress(replyToAddress)});
      }
      //Add raw headers to message object
      StringTokenizer tokenizer = new StringTokenizer(headers, System.getProperty("line.separator", "\n"));
      while (tokenizer.hasMoreTokens()) {
        message.addHeaderLine(tokenizer.nextToken());
      }

      //Most email clients add this line with the name of
      //their software and the version
      message.addHeader("X-Mailer", "Centraview v. " + CentraViewConfiguration.getVersion());

      message.setSubject(subject);
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(body, messageType);

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      // Handle attachments
      if (attachments != null) {
        message.setContent(multipart);
        Iterator attachmentIterator = attachments.iterator();
        while (attachmentIterator.hasNext()) {
          messageBodyPart = new MimeBodyPart();
          CvFileVO thisAttachment = (CvFileVO) attachmentIterator.next();
          String path = thisAttachment.getPhysicalFolderVO().getFullPath(null, true) + thisAttachment.getName();
          DataSource source = new FileDataSource(path);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(thisAttachment.getTitle());
          multipart.addBodyPart(messageBodyPart);
        }
      }

      message.setSentDate(new Date());
      //End of Build The JavaMail message

      messageSent = this.sendSimpleMessage(message, mailAccountVO.getSmtpServer(),
        mailAccountVO.getLogin(), mailAccountVO.getPassword(), mailAccountVO.getSmtpPort(),
        mailAccountVO.isPopRequiredBeforeSMTP(), mailAccountVO.isAuthenticationRequiredForSMTP(),
        mailAccountVO.getAccountType(), mailAccountVO.getMailServer());

      //Save Message to Sent Folder
      if (messageSent) {
        logger.error("[sendMessage]["+dataSource+"]: Mail Message Successfully sent.");
        int messageID = this.addMessage(message, "", mailAccountVO.getAccountID(), mailAccountVO.getOwnerID(), sentFolder.getFolderID(), cvdal);
        HashMap embededImageMap = new HashMap();
        this.saveAttachments(this.getPartContent(message), messageID, mailAccountVO.getAccountID(), mailAccountVO.getOwnerID(), attachmentFolder.getFolderId(), cvdal,embededImageMap);
        this.addBody(message, messageID, embededImageMap, cvdal);
        embededImageMap = null;
      }
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return messageSent;
  }   // end sendMessage method

  /**
   * Sends a message. This method provides the basic functionality for sending a message.
   * @param message The built message. This cannot be <code>null</code>.
   * @param smtpServer The address of the smtp server. If you plan on relaying,
   *        this can be <code>null</code>. This can be <code>null</code> only if
   * <code>smtpAuthenticationRequired</code> are false.
   * @param username The username of the smtp server (and pop server if pop is
   *        required before smtp). This can be <code>null</code> only if both
   *        <code>connectToPopFirst</code> AND <code>smtpAuthenticationRequired</code>
   *        are false.
   * @param password The password of the smtp server (and pop server if pop is
   *        required before smtp). This can be <code>null</code> only if both
   *        <code>connectToPopFirst</code> AND <code>smtpAuthenticationRequired</code>
   *        are false.
   * @param smtpPort The is the port to connect to the smtp server on. The default
   *        should be 25. If you pass in 0 or -1, 25 will be used.
   * @param connectToPopFirst A boolean to say whether we need to connect to the
   *        pop/imap server before connecting to the smtp server.
   * @param smtpAuthenticationRequired Whether or not smtp authentication is required.
   * @param serverType The mail server type. This is only needed if
   * <code>connectToPopFirst</code> is true. If <code>connectToPopFirst</code>
   * is false, this can be <code>null</code>.
   * @param serverAddress The mail server address. This is only needed if
   * <code>connectToPopFirst</code> is true. If <code>connectToPopFirst</code>
   * is false, this can be <code>null</code>.
   * @return true if the message was sent, false if it wasn't.
   * @throws SendFailedException The message could not be sent for whatever reason.
   *         Check the Exception for a detailed reason.
   * @throws MessagingException when processing the message some of method throws the MessagingException.
   */
  public boolean sendSimpleMessage(Message message, String smtpServer, String username, String password, int smtpPort, boolean connectToPopFirst, boolean smtpAuthenticationRequired, String serverType, String serverAddress) throws SendFailedException,MessagingException
  {
    boolean messageSent = false;
    Transport transport = null;

    if (message == null) {
      logger.debug("["+dataSource+"]: failed sending message, it's null.");
      throw new SendFailedException("<error>Message cannot be null. Please set up the message.</error>");
    }

    Properties props = System.getProperties();
    if (smtpServer != null) {
      props.put("mail.smtp.host", smtpServer);
      props.put("mail.transport.protocol", "smtp");
    } else {
      logger.debug("["+dataSource+"]: failed sending message, SMTP server is null.");
      throw new SendFailedException("<error>The SMTP Server has not been setup.</error>");
    }
    
    if (smtpAuthenticationRequired) {
      // do SMTP Authentication if required
      props.put("mail.smtp.auth", "true");
    }

    Session session = Session.getDefaultInstance(props, null);

    try {
      if (connectToPopFirst) {
        // POP before SMTP
        if (serverAddress == null || username == null || password == null || serverType == null) {
          throw new SendFailedException ("<error>serverAddress, username, password and servertype cannot be null if connectToPopFirst is true.</error>");
        }
        //perform a quick connection to mail server
        this.performFastConnection(serverAddress, username, password, serverType);
      }

      if (smtpAuthenticationRequired) {
        // let's not relay and say we did ;)
        if (smtpServer == null || username == null || password == null) {
          throw new SendFailedException ("<error>serverAddress, username, password and servertype cannot be null if smtpAuthenticationRequired is true.</error>");
        }

        if (smtpPort == 0 || smtpPort == -1) {
          smtpPort = 25;
        }
        
        transport = session.getTransport(MailAccountVO.SMTP_TYPE);
        transport.connect(smtpServer, smtpPort, username, password);
        message.saveChanges();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        messageSent = true;
      } else {
        //let's relay!
        Transport.send(message);
        messageSent = true;
      }
    } catch (SendFailedException sfe) {
      // if we catch an SFE, wrap it in the <error> tag and rethrow so we can show to the user
      String eMessage = sfe.getNextException().getMessage();

      // also, show them all the addresses that mail was not sent to...

      String notSentTo = "";
      boolean showNotSent = false;
      Address[] validUnsentAddresses = sfe.getValidUnsentAddresses();
      if (validUnsentAddresses != null ) {
        for (int i = 0; i < validUnsentAddresses.length; i++) {
          Address address = validUnsentAddresses[i];
          notSentTo = notSentTo + address.toString() + "<br/>";
          showNotSent = true;
        }
      }
      
      Address[] invalidUnsentAddresses = sfe.getValidUnsentAddresses();
      if (validUnsentAddresses != null ) {
        for (int i = 0; i < invalidUnsentAddresses.length; i++) {
          Address address = invalidUnsentAddresses[i];
          notSentTo = notSentTo + address.toString() + "<br/>";
          showNotSent = true;
        }
      }

      if (showNotSent) {
        eMessage = eMessage + "<br/>Mail not sent to:<br/>" + notSentTo;
      }
      logger.error("[sendSimpleMessage]: The UI error message: "+eMessage);
      logger.error("[sendSimpleMessage]: Exception", sfe);

      throw new SendFailedException("<error>" + eMessage + "</error>");
      
    } finally {
      if (transport != null){
        if (transport.isConnected()){
          try{
            transport.close();
          }catch (Exception e){
            //We must ignore this, because
            //we can't do anything...
          }
        }
      }
    }
    return messageSent;
  }   // end sendSimpleMessage(Message,String,String,String,int,boolean,boolean,String,String) method

  /**
   * This method is provided if your account needs to
   * connect to the Pop server every so often before
   * a message can be sent to the smtp server. <p>
   * This method simply connects to server and returns
   * whether or not a connection has been made.
   * @param mailAccountVO The account information.
   * @return true if a connection was made, false otherwise.
   */
  private boolean performFastConnection(String serverAddress, String username, String password, String serverType)
  {
    boolean gotConnection = false;
    Properties properties = System.getProperties();
    Store store = null;

    try {
      Session session = Session.getDefaultInstance(properties);
      store = session.getStore(serverType);
      store.connect(serverAddress, username, password);
      gotConnection = store.isConnected();
    }catch (Exception e){
      System.out.println("[Exception][MailEJB] performFastConnection(String,String,String,String): " + e);
      //e.printStackTrace();
    }finally{
      if (store != null){
        try { store.close(); } catch (Exception e) { /* Sorry, we can't do anything at this point. */ }
        }
      }
    return gotConnection;
  }   // end performFastConnection(String,String,String,String) method

  /**
   * Converts a collection of Strings into an Address Array.
   * @param addressCollection A Collection of Strings which are the addresses.
   * @return An Address array based on the Collection passed in.
   * @throws AddressException If the parse failed
   */
  private Address[] getAddressAddress(Collection addressCollection) throws AddressException
  {
    Address addressArray[] = new Address[0];

    if (addressCollection != null) {
      addressArray = new Address[addressCollection.size()];

      Iterator addressIterator = addressCollection.iterator();
      int count = 0;

      while (addressIterator.hasNext()) {
        Address address  = new InternetAddress((String) addressIterator.next());
        addressArray[count++] = address;
      }
    }   // end if (addressCollection != null)

    return addressArray;
  }   // end getAddressAddress(Collection) method

  /**
   * Adds a rule (and the rule criteria) to the database. The new ruleID
   * is returned. If the rule is not created/saved, -1 will be returned.
   * @param individualID The individualID of the user who is creating this rule.
   * @param ruleVO The new RuleVO to add to the database.
   * @return The new RuleID.
   */
  public int addRule(int individualID, RuleVO ruleVO)
  {
    int newRuleID = -1;
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String insertQuery = "INSERT INTO emailrule (accountID, name, description, enabled) VALUES (?, ?, ?, ?)";
      cvdal.setSqlQuery(insertQuery);
      cvdal.setInt(1, ruleVO.getAccountID());
      cvdal.setString(2, ruleVO.getRuleName());
      cvdal.setString(3, ruleVO.getDescription());
      cvdal.setString(4, ruleVO.isEnabled()?"YES":"NO");

      cvdal.executeUpdate();
      newRuleID = cvdal.getAutoGeneratedKey();

      cvdal.setSqlQueryToNull();

      if (newRuleID > 0) {
        ArrayList batchQueries = new ArrayList();

        if (ruleVO.isMoveMessage()) {
          batchQueries.add("INSERT INTO emailruleaction (ruleID, actionType, folderID) VALUES (" + newRuleID + ", 'MOVE', " + ruleVO.getFolderID() + ")");
        }

        if (ruleVO.isMarkMessageRead()) {
          batchQueries.add("INSERT INTO emailruleaction (ruleID, actionType) VALUES (" + newRuleID + ", 'MARK')");
        }

        if (ruleVO.isDeleteMessage()) {
          batchQueries.add("INSERT INTO emailruleaction (ruleID, actionType) VALUES (" + newRuleID + ", 'DEL')");
        }
        int[] batchResults = cvdal.batchProcess(batchQueries);


        ArrayList criteriaCollection = (ArrayList)ruleVO.getRuleCriteria();

        if (criteriaCollection != null) {
        Iterator criteriaIterator = criteriaCollection.iterator();
          while (criteriaIterator.hasNext()) {
          RuleCriteriaVO ruleCriteriaVO = (RuleCriteriaVO) criteriaIterator.next();
          this.addRuleCriteria(newRuleID, ruleCriteriaVO, cvdal);
          }
        }   // end if (criteriaCollection != null)
      }   // end if (newRuleID > 0)
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.addRule: " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return newRuleID;
  }   // end addRule(int,RuleVO) method

  /**
   * Updates an existing rule (and the rule criteria) in the database.
   * @param individualID The individualID of the user who is updating the rule.
   * @param ruleVO The new RuleVO to add to the database. The ruleID
   *        must be set on this object in order to update it.
   * @return int the number of rules updated (should always be 1 or 0)
   */
  public int editRule(int individualID, RuleVO ruleVO)
  {
    int numberChanged = 0;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      numberChanged = this.editRule(individualID, ruleVO, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] editRule(): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return numberChanged;
  }   // end editRule(int,RuleVO) method


  /**
   * Updates an existing rule (and the rule criteria) in the database.
   * @param individualID The individualID of the user who is updating the rule.
   * @param ruleVO The new RuleVO to add to the database. The ruleID
   *        must be set on this object in order to update it.
   * @return int the number of rules updated (should always be 1 or 0)
   */
  private int editRule(int individualID, RuleVO ruleVO, CVDal cvdal)
  {
    int numberChanged = 0;

    try {
      int ruleID = ruleVO.getRuleID();

      // first, update the rule record in emailrule table
      String updateQuery = "UPDATE emailrule SET name=?, description=?, enabled=?, accountID=? WHERE ruleID=?";
      cvdal.setSqlQuery(updateQuery);
      cvdal.setString(1, ruleVO.getRuleName());
      cvdal.setString(2, ruleVO.getDescription());
      cvdal.setString(3, ruleVO.isEnabled()?"YES":"NO");
      cvdal.setInt(4, ruleVO.getAccountID());
      cvdal.setInt(5, ruleID);

      numberChanged = cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      // next, delete all the criteria and actions for this rule
      String deleteQuery = "DELETE emailrulecriteria, emailruleaction FROM emailrulecriteria " +
                           "LEFT JOIN emailruleaction ON (emailrulecriteria.ruleID=emailruleaction.ruleID) " +
                           "WHERE emailrulecriteria.ruleID=?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, ruleID);
      int deleted = cvdal.executeUpdate();

      // next, add back all the new (or old) actions
      ArrayList batchQueries = new ArrayList();

      if (ruleVO.isMoveMessage()){
        batchQueries.add("INSERT INTO emailruleaction (ruleID, actionType, folderID) VALUES (" + ruleID + ", 'MOVE', " + ruleVO.getFolderID() + ")");
      }

      if (ruleVO.isMarkMessageRead()){
        batchQueries.add("INSERT INTO emailruleaction (ruleID, actionType) VALUES (" + ruleID + ", 'MARK')");
      }

      if (ruleVO.isDeleteMessage()){
        batchQueries.add("INSERT INTO emailruleaction (ruleID, actionType) VALUES (" + ruleID + ", 'DEL')");
      }
      int[] batchResults = cvdal.batchProcess(batchQueries);

      // next, insert all the new (or unmodified) criteria from
      // the RuleVO (instead of using logic to determine which
      // criteria have been modified versus which haven't, just
      // delete the old ones and insert new ones)
      Collection criteriaCollection = ruleVO.getRuleCriteria();
      if (criteriaCollection != null) {
        Iterator criteriaIterator = criteriaCollection.iterator();
        while (criteriaIterator.hasNext()) {
          RuleCriteriaVO ruleCriteriaVO = (RuleCriteriaVO) criteriaIterator.next();
          int criteriaID = this.addRuleCriteria(ruleID, ruleCriteriaVO, cvdal);
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] editRule(int,RuleVO,CVDal): " + e);
      // e.printStackTrace();
    }finally{
        cvdal.setSqlQueryToNull();
    }
    return numberChanged;
  }   // end editRule(int,RuleVO,CVDal) method

  /**
   * Deletes an existing rule (and the rule criteria) from the database
   * based on the ruleID. The number of rules deleted will be returned.
   * @param ruleID The ID of the Rule to be deleted.
   * @return The number of rules deleted. This number should always be 0 or 1.
   */
  public int deleteRule(int ruleID)
  {
    int numberDeleted = 0;
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String deleteQuery = "DELETE FROM emailrule WHERE ruleID = ?";
      cvdal.setSqlQuery(deleteQuery);
      cvdal.setInt(1, ruleID);

      numberDeleted = cvdal.executeUpdate();
      //No need to explicitly delete the rule criteria
      //they are deleted by the cascade delete foreign
      //key constraint on the database.
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in deleteRule(int): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return numberDeleted;
  }   // end deleteRule(int) method


  /**
   * Returns a fully populated RuleVO object based on the given ruleID.
   * If the rule is not found, a RuleVO with a RuleID of -1 will be returned.
   * @param ruleID The ID of the Rule we are looking for.
   * @return A fully populated RuleVO.
   */
  public RuleVO getRule(int ruleID)
    {
    RuleVO ruleVO = new RuleVO();
    ruleVO.setRuleID(-1);
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      ruleVO = this.getRule(ruleID, cvdal);
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return(ruleVO);
  }

  /**
   * Returns a fully populated RuleVO object based on the given ruleID.
   * If the rule is not found, a RuleVO with a RuleID of -1 will be returned.
   * @param ruleID The ID of the Rule we are looking for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return A fully populated RuleVO.
   */
  private RuleVO getRule(int ruleID, CVDal cvdal)
  {
    RuleVO ruleVO = new RuleVO();
    ruleVO.setRuleID(-1);

    if (ruleID <= 0) {
      return(ruleVO);
    }

    try {
      String selectQuery = "SELECT ruleID, accountID, name, description, enabled FROM emailrule WHERE ruleID=?";
      cvdal.setSqlQuery(selectQuery);
      cvdal.setInt(1, ruleID);

      Collection resultsCollection = cvdal.executeQuery();
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext()) {
          HashMap resultsHashMap = (HashMap) resultsIterator.next();
          ruleVO.setRuleID(ruleID);
          ruleVO.setAccountID(((Number)resultsHashMap.get("accountID")).intValue());
          ruleVO.setRuleName((String)resultsHashMap.get("name"));
          ruleVO.setDescription((String) resultsHashMap.get("description"));
          String enabledString = (String)resultsHashMap.get("enabled");
          boolean enabled = (enabledString == null)?false:enabledString.equalsIgnoreCase("YES");
          ruleVO.setEnabled(enabled);

          cvdal.setSqlQueryToNull();

          String criteriaQuery = "SELECT ruleID, orderID, expressionType, fieldID, conditionID, value FROM emailrulecriteria WHERE ruleID=?";
          cvdal.setSqlQuery(criteriaQuery);
          cvdal.setInt(1, ruleID);

          Collection criteriaCollection = cvdal.executeQuery();
          if (criteriaCollection != null) {
            Iterator criteriaIterator = criteriaCollection.iterator();
            while (criteriaIterator.hasNext()) {
              RuleCriteriaVO criteriaVO = new RuleCriteriaVO();
              HashMap criteriaInfo = (HashMap)criteriaIterator.next();

              // generate a RuleCriteriaVO for each row in the result set,
              // and add that RuleCriteriaVO to the RuleVO object
              criteriaVO.setRuleID(ruleID);
              criteriaVO.setOrderID(((Number)criteriaInfo.get("orderID")).intValue());
              criteriaVO.setExpressionType((String)criteriaInfo.get("expressionType"));
              criteriaVO.setFieldID(((Number)criteriaInfo.get("fieldID")).intValue());
              criteriaVO.setConditionID(((Number)criteriaInfo.get("conditionID")).intValue());
              criteriaVO.setValue((String)criteriaInfo.get("value"));

              ruleVO.addRuleCriteria(criteriaVO);
            }   // end while (criteriaIterator.hasNext())
          }   // end if (criteriaCollection != null)
        }   // end if (resultsIterator.hasNext())
      }   // end if (resultsCollection != null)

      // Next, get the list of actions associated with this rule. Loop
      // through each action, and set the appropriate value on the RuleVO
      // based on the values of that action
      String actionQuery = "SELECT ruleID, actionType, folderID FROM emailruleaction WHERE ruleID=?";
      cvdal.setSqlQuery(actionQuery);
      cvdal.setInt(1, ruleID);

      Collection actionResults = cvdal.executeQuery();
      if (actionResults != null) {
        Iterator actionIter = actionResults.iterator();
        while (actionIter.hasNext()) {
          HashMap actionInfo = (HashMap)actionIter.next();
          String actionType = (String)actionInfo.get("actionType");
          if (actionType != null) {
            if (actionType.equals("MOVE")) {
              Number folderID = (Number)actionInfo.get("folderID");
              ruleVO.setMoveMessage(true);
              ruleVO.setFolderID(folderID.intValue());
            }else if (actionType.equals("DEL")){
              ruleVO.setDeleteMessage(true);
            }else if (actionType.equals("MARK")){
              ruleVO.setMarkMessageRead(true);
            }
          }
        }   // end while (actionIter.hasNext())
      }   // end if (actionResults != null)
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.getRule: " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return ruleVO;
  }   // end getRule(int,int) method

  /**
   * Adds a rule criteria to the database and returns the new ruleCriteriaID.
   * If the rule criteria is not created/saved, -1 will be returned. <p>
   * @param ruleID The ID of the Rule that this criteria is related to.
   * @param ruleCriteriaVO The Rule Criteria Object.
   * @param cvdal An <strong>open</strong> database connection.
   * @return The new RuleCriteriaID.
   */
  private int addRuleCriteria(int ruleID, RuleCriteriaVO ruleCriteriaVO, CVDal cvdal) throws Exception
  {
    int newRuleCriteriaID = -1;

    try {
      String insertQuery = "INSERT INTO emailrulecriteria (ruleID, orderID, expressionType, " +
                           "FieldID, conditionID, value) VALUES (?, ?, ?, ?, ?, ?)";
      cvdal.setSqlQuery(insertQuery);
      cvdal.setInt(1, ruleID);
      cvdal.setInt(2, ruleCriteriaVO.getOrderID());
      cvdal.setString(3, ruleCriteriaVO.getExpressionType());
      cvdal.setInt(4, ruleCriteriaVO.getFieldID());
      cvdal.setInt(5, ruleCriteriaVO.getConditionID());
      cvdal.setString(6, ruleCriteriaVO.getValue());

      cvdal.executeUpdate();
      newRuleCriteriaID = cvdal.getAutoGeneratedKey();
    }finally{
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();
    }
    return newRuleCriteriaID;
  }   // end addRuleCriteria(int,RuleCriteriaVO,CVDal) method

  /**
   * Applies all rules for the given accountID, and returns the number of messages affected.
   * @param accountID The account which we want to apply rules for.
   * @param messageIDs An ArrayList of message to which rules will be applied (new messages only).
   * @param cvdal An <strong>open</strong> database connection.
   * @return The number of messages affected by the rule.
   */
  private int applyRules(int accountID, ArrayList messageIDs, CVDal cvdal)
  {
    int numberChanged = 0;

    if (messageIDs == null || messageIDs.size() < 1) {
      // don't bother processing rules if we haven't downloaded messages...
      return(numberChanged);
    }

    try {
      // loop through messageIDs, and for each ID, get the MessageVO
      // and stick it in a collection for use later. This will help
      // with performance, by not requiring that we get this message
      // from the database multiple times as we loop through the rules list
      ArrayList messageList = new ArrayList();
      Iterator messageIDIter = messageIDs.iterator();
      while (messageIDIter.hasNext()) {
        Integer messageID = (Integer)messageIDIter.next();
        // TODO: individualID is HARDCODED!!! HELP! What do we do here????
        MailMessageVO messageVO = this.getEmailMessageVO(1, messageID.intValue());
        messageList.add(messageVO);
      }

      // get the rules list for the given account ID
      ArrayList rulesList = (ArrayList)this.getRulesForAccount(accountID, cvdal);
      if (rulesList != null) {
        Iterator rulesIter = rulesList.iterator();
        // loop through rules list
        while (rulesIter.hasNext()) {
          // get the RuleVO
          RuleVO ruleVO = (RuleVO)rulesIter.next();

          // loop through messageIDs
          Iterator messageIter = messageList.iterator();
          while (messageIter.hasNext()) {
            MailMessageVO messageVO = (MailMessageVO)messageIter.next();

            // now that we've got the RuleVO and the MailMessageVO,
            // we need to compare and see if this message matches the
            // criteria for this rule. If so, we'll need to execute the
            // actions of this rule on this message, which is done in
            // applyRuleToMessage()
            boolean messageModified = this.applyRuleToMessage(ruleVO, messageVO, cvdal);
          }   // end while (messageIter.hasNext())
        }   // end while (rulesIter.hasNext())
      }   // end if (rulesList != null)
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in applyRules(int,ArrayList,CVDal): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return numberChanged;
  } //end of applyRule method

  /**
   * Given a RuleVO and a MailMessageVO, checks whether the message matches the
   * rule, then performs any actions of the rule on the message.
   * @param ruleVO A RuleVO object representing a rule which we'll see if a
   *          message matches.
   * @param messageVO A MailMessageVO which we'll match against the rule to see
   *          if there's a match.
   * @param cvdal An <strong>open </strong> datbase connection.
   * @param boolean true for match, false for no match.
   */
  private boolean applyRuleToMessage(RuleVO ruleVO, MailMessageVO messageVO, CVDal cvdal)
  {
    boolean messageModified = false;

    try {
      ArrayList criteriaList = (ArrayList)ruleVO.getRuleCriteria();
      boolean ruleMatches = true;
      if (criteriaList != null && criteriaList.size() > 0) {
        Iterator iter = criteriaList.iterator();
        int i = 0;
        while (iter.hasNext()) {
          RuleCriteriaVO criteria = (RuleCriteriaVO)iter.next();
          String expressionType = criteria.getExpressionType();
          // if "ruleMatches" comes back true, than the message matches
          // this single criteria of this rule (this iteration)
          boolean criteriaMatches = this.doesCriteriaMatchMessage(criteria, messageVO);

          if (i > 0) {
            if (expressionType.equals("AND")) {
              ruleMatches = ruleMatches && criteriaMatches;
            } else if (expressionType.equals("OR")) {
              ruleMatches = ruleMatches || criteriaMatches;
            }
          } else {
            ruleMatches = criteriaMatches;
            i++;
          } // end if (i > 0)
        } // end while (iter.hasNext())
      } // end if (criteriaList != null && criteriaList.size() > 0)

      if (ruleMatches == true) {
        // ok, the message satisfied all the criteria for this rule,
        // so we need to perform the action
        // VERY IMPORTANT!!!!! MAKE SURE YOU DO NOT CHANGE THE ORDER
        // IN WHICH THESE ACTIONS ARE APPLIED. DELETING A MESSAGE
        // BEFORE MARKING IT READ OR MOVING IT COULD HAVE DISASTROUS
        // RESULTS!!!! THINK ABOUT IT!!!
        if (ruleVO.isMarkMessageRead()) {
          // TODO: HARDCODED INDIVIDUALID!!! WHAT DO WE DO HERE???
          this.markMessageRead(1, messageVO.getMessageID(), cvdal);
        }

        if (ruleVO.isMoveMessage()) {
          // TODO: HARDCODED INDIVIDUALID!!! WHAT DO WE DO HERE???
          this.moveMessageToFolder(1, messageVO.getMessageID(), ruleVO.getFolderID());
        }

        if (ruleVO.isDeleteMessage()) {
          // TODO: HARDCODED INDIVIDUALID!!! WHAT DO WE DO HERE???
          this.deleteMessage(messageVO.getMessageID(), 1);
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][MailEJB] Exception thrown in applyRuleToMessage(RuleVO,MailMessageVO,CVDal): " + e);
      // e.printStackTrace();
    } finally {
      cvdal.setSqlQueryToNull();
    }
    return (messageModified);
  } // end applyRuleToMessage(RuleVO,MailMessageVO,CVDal) method

  /**
   * Given a RuleCriteriaVO and a MailMessageVO, returns true if the
   * message matches the given criteria.
   * @param criteriaVO A RuleCriteriaVO object representing a single
   *        criteria which we will compare with a single message.
   * @param messageVO A MailMessageVO object which will be compared to
   *        the rule criteria to see if there is a match.
   * @return boolean true for match, false for no match.
   */
  private boolean doesCriteriaMatchMessage(RuleCriteriaVO criteriaVO, MailMessageVO messageVO)
  {
    boolean matches = false;
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      int fieldID = criteriaVO.getFieldID();
      int conditionID = criteriaVO.getConditionID();
      String ruleValue = (String)criteriaVO.getValue();
      HashMap emailFields = new HashMap();
      // Hardcode email table id
      final int tableID = 33;
      // Select all the email fields to search on and decide based on that.
      cvdal.setSqlQuery("SELECT SearchFieldID, DisplayName " +
          "FROM searchfield WHERE SearchTableID = ?");
      cvdal.setInt(1, tableID);
      Collection results = cvdal.executeQuery();
      if (results != null) {
        Iterator itera = results.iterator();
        while (itera.hasNext()) {
          HashMap row = (HashMap)itera.next();
          emailFields.put(row.get("DisplayName"), row.get("SearchFieldID"));
        }

        // If we ever what to search on anything else, there must be a case
        // for it here...
        if (fieldID == ((Number)emailFields.get("Body")).intValue()) {
          String body = messageVO.getBody();
          matches = this.doConditionalStringMatch(ruleValue, body, conditionID);
        } else if (fieldID == ((Number)emailFields.get("Subject")).intValue()) {
          String subject = messageVO.getSubject();
          matches = this.doConditionalStringMatch(ruleValue, subject, conditionID);
        } else if (fieldID == ((Number)emailFields.get("Sender")).intValue()) {
          String sender = messageVO.getFromAddress();
          matches = this.doConditionalStringMatch(ruleValue, sender, conditionID);
        } else if (fieldID == ((Number)emailFields.get("Recipient")).intValue()) {
          // get the To: recipients
          ArrayList toList = (ArrayList)messageVO.getToList();
          if (toList == null) {
            toList = new ArrayList();
          }
          // get the Cc: recipients
          ArrayList ccList = (ArrayList)messageVO.getCcList();
          if (ccList != null && ccList.size() > 0) {
            // append the Cc: recipients to the list of To: recipients
            // so that we only need to iterate one list
            toList.addAll(ccList);
          }
          if (toList.size() > 0) {
            Iterator iter = toList.iterator();
            while (iter.hasNext()) {
              String address = (String)iter.next();
              matches = this.doConditionalStringMatch(ruleValue, address, conditionID) ? true : matches;
            }
          }
        } else if (fieldID == ((Number)emailFields.get("Message Date")).intValue()) {
          // TODO: at this time, dates in rules are not functional
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][MailEJB] Exception thrown in doesCriteriaMatchMessage(RuleCriteriaVO,MailMessageVO): " + e);
      // e.printStackTrace();
    } finally {
      cvdal.destroy();
    }
    return (matches);
  } // end doesCriteriaMatchMessage(RuleCriteriaVO,MailMessageVO)

  /**
   * Returns true if <code>ruleValue</code> matches <code>messageValue</code>
   * based on the given <code>conditionID</code>. Used by the rules framework
   * for determining if a rule criteria matches a given field of a message.
   * @param ruleValue The String value of a rule criteria that is matched
   *          against a given email message field.
   * @param messageValue The String value of a given field of a given email
   *          message which is matched against the rule criteria value.
   * @param conditionID int value representing a specific condition on which to
   *          match the two values (such as "equals", "begins with", etc). The
   *          map of conditionIDs -> conditions is defined in SearchVO.
   * @return boolean true if the values match, false otherwise.
   */
  private boolean doConditionalStringMatch(String ruleValue, String messageValue, int conditionID)
  {
    boolean matches = false;

    // lowercase both strings for better matching
    // TODO: perhaps strip punctuation when matching rule values against emails?
    ruleValue = ruleValue.toLowerCase();
    messageValue = messageValue.toLowerCase();

    HashMap conditions = SearchVO.getConditionOptions();
    Integer conditionIntID = new Integer(conditionID);
    if (conditions.get(conditionIntID) != null) {
      String conditionString = (String) conditions.get(conditionIntID);
      if (conditionString.equals(SearchVO.BEGINS_WITH_STRING)) {
        matches = messageValue.startsWith(ruleValue);
      }else if (conditionString.equals(SearchVO.ENDS_WITH_STRING)){
        matches = messageValue.endsWith(ruleValue);
      }else if (conditionString.equals(SearchVO.CONTAINS_STRING)){
        matches = messageValue.matches(".*" + ruleValue + ".*");
      }else if (conditionString.equals(SearchVO.EQUALS_STRING)){
        matches = messageValue.equals(ruleValue);
      }else if (conditionString.equals(SearchVO.GREATER_THAN_STRING)){
        // Please see the Javadoc for String.compareTo() to
        // understand how this logic works.
        if (messageValue.compareTo(ruleValue) > 0){
          matches = true;
        }
      }else if (conditionString.equals(SearchVO.LESS_THAN_STRING)){
        // Please see the Javadoc for String.compareTo() to
        // understand how this logic works.
        if (messageValue.compareTo(ruleValue) < 0){
          matches = true;
        }
      }else if (conditionString.equals(SearchVO.HAS_STRING)){
        //TODO: Figure out how to do a "HAS". Possibly used for "is sender in my contact list"??
      }
    }
    return(matches);
  }

  /**
   * Set the Private flag value to Message. Message IDS which are selected by the user
   * will be marked as private or public according to user's selection.
   *
   * @param individualID The individualID of the user asking for the list.
   * @param messageIDs Its a collection of messageID with comma seperated values.
   * @param privateType If yes then it will make the selected messageid to private or else public
   * @return
   */
  public boolean privateMessage(int individualID, String messageIDs, String privateType)
  {
    boolean flagPrivate = false;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
        String selectQuery = "UPDATE emailmessage SET private='"+privateType+"' WHERE MessageID in ("+messageIDs+")";
        cvdal.setSqlQuery(selectQuery);
        cvdal.executeUpdate();
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in privateMessage(): " + e.toString());
      flagPrivate = false;
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return flagPrivate;
  }

  /**
   * Returns a DisplayList of Rules.
   * @param individualID The individualID of the user asking for the list.
   * @param accountID The accountID of the email account whose rules will be returned.
   * @param preference The preference its a collection of information to Sort in Ascending/Descending Order, Sort on particular column & searchstring
   * @return A Display List of email rules casted to RuleList.
   */
  public DisplayList getRuleList(int individualID, int accountID, HashMap preference)
  {
    RuleList rulesList = new RuleList();
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      rulesList = (RuleList)this.getRuleList(individualID, accountID, preference, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getNumberOfAccountsForUser(): " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }

    return rulesList;
  }   // end getRuleList(int,int) method

  /**
   * Returns a DisplayList of Rules.
   * @param individualID The individualID of the user asking for the list.
   * @param accountID The accountID of the email account whose rules will be returned.
   * @param preference The preference its a collection of information to Sort in Ascending/Descending Order, Sort on particular column & searchstring
   * @return A Display List of email rules casted to RuleList.
   */
  public DisplayList getRuleList(int individualID, int accountID, HashMap preference, CVDal cvdal) throws Exception
  {

    String searchString = (String) preference.get("searchString");
    String sortmem = (String) preference.get("sortmem");
    Character sortON = (Character) preference.get("sortType");

    char sort = sortON.charValue();

    String sortType = "";
    if (sort == 'D'){
    sortType = " DESC";
  }

    RuleList rulesList = new RuleList();

    try {
    String appendQueryCondition = "";
      String query = "SELECT ruleID, name, description, enabled FROM emailrule WHERE accountID=? "+appendQueryCondition+" ORDER BY " + sortmem + sortType;
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, accountID);

      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        int i = 0;
        while (iter.hasNext()) {
          i++;
          HashMap row = (HashMap)iter.next();
          int ruleID = ((Number)row.get("ruleID")).intValue();
          IntMember ruleIDfield = new IntMember("RuleID", ruleID, 10, "", 'T', true, 10);
          StringMember name = new StringMember("RuleName", (String)row.get("name"), 10, "", 'T', true);
          StringMember description = new StringMember("Description", (String)row.get("description"), 10, "", 'T', false);
          StringMember enabled = new StringMember("EnabledStatus", (String)row.get("enabled"), 10, "", 'T', false);

          RuleListElement ele  = new RuleListElement(ruleID);

          ele.put("RuleID", ruleIDfield);
          ele.put("Name", name);
          ele.put("Description", description);
          ele.put("Enabled", enabled);

          StringBuffer sb = new StringBuffer("00000000000");
          sb.setLength(11);
          String str = (new Integer(i)).toString();
          sb.replace((sb.length() - str.length()), (sb.length()), str);
          String newOrd = sb.toString();

          rulesList.put(newOrd, ele);
        }
        rulesList.setTotalNoOfRecords(rulesList.size());
        rulesList.setListType("Rule");
      }   // end if (result != null)
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getNumberOfAccountsForUser(): " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return rulesList;
  }   // end getRuleList(int,int,CVDal) method


  /**
   * Returns the number of defined email accounts for the
   * given individualID. A return value of 0 or less indicates
   * that there is no account configured for the user.
   * @param individualID IndividualID of the user who we're asking about
   * @return (int) the number of accounts which  exist for the user
   */
  public int getNumberOfAccountsForUser(int individualID)
  {
    int numberOfAccounts = 0;

    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String selectQuery = "SELECT COUNT(*) AS numberAccounts FROM emailaccount WHERE Owner=?";
      cvdal.setSqlQuery(selectQuery);
      cvdal.setInt(1, individualID);

      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap resultRow = (HashMap)iter.next();
          Number accounts = (Number)resultRow.get("numberAccounts");
          numberOfAccounts = accounts.intValue();
          break;
        }
      }   // end if (resultsSet != null)
    }catch(Exception e){
      logger.error("[getNumberOfAccountsForUser] Exception thrown.", e);
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return(numberOfAccounts);
  }   // end getNumberOfAccountsForUser(int) method

  /**
   * Returns the accountID of the default email account for the
   * given individualID. A return value of 0 or less indicates
   * that there is no account configured for the user.
   * @param individualID IndividualID of the user who we're asking about.
   * @return (int) the accountID of the user's default email account.
   */
  public int getDefaultAccountID(int individualID)
  {
    int defaultAccountID = 0;

    CVDal cvdal = new CVDal(this.dataSource);

    try {
    String selectQuery = "SELECT `AccountID` from emailaccount  WHERE Owner=? and `Default`='YES'";
      cvdal.setSqlQuery(selectQuery);
      cvdal.setInt(1, individualID);

      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        // Assume that we will only get one defaultAccount back. If not,
        // the first returned default account is as good as the last one.
        if (iter.hasNext()) {
          HashMap resultRow = (HashMap)iter.next();
          Number accountID = (Number)resultRow.get("AccountID");
          try {
            defaultAccountID = accountID.intValue();
          }catch(NumberFormatException nfe){
            // we already set a default value, so no need to
            // handle this exception. All other exceptions
            // should be thrown, but not this one.
          }
        }
      }   // end if (resultsSet != null)
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getDefaultAccountID(): " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(defaultAccountID);
  }   // end getDefaultAccountID(int) method

  /**
   * Returns an ArrayList of mail accountIDs for those
   * accounts which belong to the given individualID.
   * @param individualID The individualID of the user
   *        whose accounts we are looking for.
   * @return ArrayList of int's - the account ID for
   *         each account this user owns.
   */
  public ArrayList getUserAccountList(int individualID) throws Exception
  {
    ArrayList accountList = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String selectQuery = "SELECT accountID from emailaccount where Owner=?";
      cvdal.setSqlQuery(selectQuery);
      cvdal.setInt(1, individualID);

      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap resultRow = (HashMap)iter.next();
          Number accountID = (Number)resultRow.get("accountID");
          accountList.add(accountID);
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getUserAccountList(): " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return(accountList);
  }   // end getUserAccountList(int) method

  /**
   * Returns an ArrayList of mail accountIDs for those
   * accounts which delegated to the given individualID.
   * @param individualID The individualID of the user
   *        whose delegated accounts we are looking for.
   * @return ArrayList of int's - the account ID for
   *         each delehated account this user owns.
   */
  public ArrayList getDelegatedAccountList(int individualID) throws Exception
  {
    ArrayList accountList = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      String selectQuery = "SELECT individualID FROM emaildelegation WHERE delegatorID=?";
      cvdal.setSqlQuery(selectQuery);
      cvdal.setInt(1, individualID);

      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap resultRow = (HashMap)iter.next();
          Number delegatorID = (Number)resultRow.get("individualID");
          accountList.addAll(this.getUserAccountList(delegatorID.intValue()));
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getDelegatedAccountList: " + e);
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
      cvdal.destroy();
      cvdal = null;
    }
    return(accountList);
  }   // end getDelegatedAccountList(int) method


  /**
   * Inserts a list of recipients into the emailrecipient table
   * for a given email message. You must supply an array of Address
   * objects, the message these recipients are attached to, the
   * type of recipients they are (To, Cc, Bcc).
   * @param recipients Array of Address objects, each being a
   *        recipient of the given email message.
   * @param messageID The ID of the message which these recipients
   *        are to be associated with in the database.
   * @param type String representation of the type of recipient
   *        list. Can be one of "TO", "CC", or "BCC".
   * @param cvdal An <strong>open</strong> database connection.
   * @return void
   */
  private void addRecipients(Address[] recipients, int messageID, String type, CVDal cvdal)
  {
    if (recipients == null || recipients.length <= 0 || messageID < 1 || type == null || type.equals("")) {
      return;
    }

    try {
    ArrayList addressList = new ArrayList();
      for (int i=0; i<recipients.length; i++) {
      Address address = recipients[i];
      String addressString = address.toString();
      if(addressString != null && !addressString.equals("UNEXPECTED_DATA_AFTER_ADDRESS@.SYNTAX-ERROR.")){
        addressList.add(addressString);
      }
    }
      int[] batchResults = cvdal.addRecipients(addressList,messageID,type);

    }catch(Exception e){
      System.out.println("[Exception][MailEJB] addRecipients(Address[],int,String,CVDal): " + e);
      //e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return;
  }   // end addRecipients(Address[],int,String,CVDal) method

  /**
   * Updates the emailmessage.MessageRead field in the database, setting it to 'YES'.
   * @param individualID The individual who is changing the read status.
   * @param messageID The ID of the message whose status is to be changed.
   * @return boolean - true for success, false for failure.
   */
  public boolean markMessageRead(int individualID, int messageID) throws Exception
  {
    boolean returnValue = false;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      returnValue = this.markMessageRead(individualID, messageID, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] markMessageRead(): " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(returnValue);
  }   // end markMessageRead(int,int) method

  /**
   * Updates the emailmessage.MessageRead field in the database, setting it to 'YES'.
   * @param individualID The individual who is changing the read status.
   * @param messageID The ID of the message whose status is to be changed.
   * @param cvdal An <strong>open</strong> database connection.
   * @return boolean - true for success, false for failure.
   */
  private boolean markMessageRead(int individualID, int messageID, CVDal cvdal) throws Exception
  {
    boolean returnStatus = true;
    try {
      if (messageID <= 0){ return(false); }
      String query = "UPDATE emailmessage SET MessageRead='YES', LocallyModified='YES' WHERE MessageID=?";
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, messageID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();
    }catch(Exception e){
      returnStatus = false;
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(returnStatus);
  }   // end markMessageRead(int,int,CVDal) method


  /**
   * Updates the emailmessage.MessageRead field in the database, setting it to 'NO'.
   * @param individualID The individual who is changing the read status.
   * @param messageID The ID of the message whose status is to be changed.
   * @return boolean - true for success, false for failure.
   */
  public boolean markMessageUnread(int individualID, int messageID) throws Exception
  {
    boolean returnValue = false;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      returnValue = this.markMessageUnread(individualID, messageID, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in markMessageUnread(int,int): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(returnValue);
  }   // end markMessageUnread(int,int) method

  /**
   * Updates the emailmessage.MessageRead field in the database, setting it to 'NO'.
   * @param individualID The individual who is changing the read status.
   * @param messageID The ID of the message whose status is to be changed.
   * @param cvdal An <strong>open</strong> database connection.
   * @return boolean - true for success, false for failure.
   */
  private boolean markMessageUnread(int individualID, int messageID, CVDal cvdal) throws Exception
  {
    boolean returnStatus = true;
    try {
      if (messageID <= 0){ return(false); }
      String query = "UPDATE emailmessage SET MessageRead='NO', LocallyModified='YES' WHERE MessageID=?";
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, messageID);
      cvdal.executeUpdate();
    }catch(Exception e){
      returnStatus = false;
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(returnStatus);
  }

  /**
   * Returns an ArrayList that represents the full folder path
   * of the given folderID. The list will be keyed on folderID
   * with the value being the folder name. This list can be used
   * in the view layer to display a linked list of the full path.
   * @param folderID The ID of the folder whose path we're asking for.
   * @return ArrayList representing the full folder path.
   */
  public ArrayList getFolderFullPath(int folderID) throws Exception
  {
    ArrayList folderPathList = new ArrayList();
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      CommonHelperLocalHome contactHelperHome = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
      CommonHelperLocal contactHelperRemote =  contactHelperHome.create();
      contactHelperRemote.setDataSource(this.dataSource);
      folderPathList = contactHelperRemote.getFolderFullPath(folderID, "emailfolder");
    }catch(Exception e){
      logger.error("[getFolderFullPath] Exception thrown.", e);
    }
    return(folderPathList);
  }


  /**
   * Returns a String representation of the full path to the given folderID.
   * @param folderID The folder for which we want the full path.
   * @param includeRoot Flag to indicate whether we want the "root"
   *        folder included in the full path.
   * @param cvdal An <strong>open</strong> database connection.
   * @return String representation of the full path of the folder.
   */
  private String getFolderFullPathString(int folderID, boolean includeRoot, CVDal cvdal)
  {
    StringBuffer fullPath = new StringBuffer("");
    try {
      ArrayList fullPathList = this.getFolderFullPath(folderID);
      Iterator iter = fullPathList.iterator();
      while (iter.hasNext()) {
        HashMap folderInfo = (HashMap)iter.next();
        // if the calling method does not want the root folder
        // included in the full path, we need to check to see
        // if this iteration is the "root" folder. If it is,
        // just skip to the next iteration. Note that "root"'s
        // parentID is always 0
        if (! includeRoot) {
          if (((Number)folderInfo.get("parentID")).intValue() <= 0) {
            continue;
          }
        }
        fullPath.append("/" + (String)folderInfo.get("name"));
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getFolderFullPath(int,CVDal): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return fullPath.toString();
  }   // end getFolderFullPath(int)


  /**
   * Returns a HashMap containing two Integer elements; "previousMessage"
   * which holds the messageID of the previous email message in the list,
   * and "nextMessage" which holds the messageID of the next message in
   * the list. If either the previous or next message cannot be found
   * (or do not exist), then the corresponding value in the HashMap is
   * set to -1. Also, if an error occurs, then the values will be -1.
   * @param individualID The individualID of the user who is asking for
   *        this information.
   * @param messageID The messageID of the email message for which we
   *        are getting the previous and next messages.
   * @return HashMap containing "previousMessage" and "nextMessage" elements.
   */
  public HashMap getPreviousNextLinks(int individualID, int messageID)
  {
    // Previous and Next are always defined by Recieved Date of the email message,
    // and NOT any sorting the user has done in the email list;
    HashMap prevNextMap = new HashMap();
    prevNextMap.put("previousMessage", new Integer(-1));
    prevNextMap.put("nextMessage", new Integer(-1));

    if (messageID <= 0){ return prevNextMap; }

    CVDal cvdal = new CVDal(this.dataSource);

    try {
      MailMessageVO messageVO = this.getEmailMessageVO(individualID, messageID);
      int folderID = messageVO.getEmailFolderID();
      java.util.Date messageDate = messageVO.getReceivedDate();
      java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(messageDate.getTime());

      String prevQuery = "SELECT em.MessageID FROM emailmessage em LEFT JOIN emailmessagefolder emf " +
                         "ON (em.MessageID=emf.MessageID) WHERE emf.folderID=? AND " +
                         "em.MessageDate>=? AND em.MessageID!=? ORDER BY em.MessageDate ASC LIMIT 1";


      cvdal.setSqlQuery(prevQuery);
      cvdal.setInt(1, messageVO.getEmailFolderID());
      cvdal.setRealTimestamp(2, sqlTimestamp);
      cvdal.setInt(3, messageID);
      Collection prevResults = cvdal.executeQuery();
      int previousID = 0;
      if (prevResults != null) {
        Iterator prevIter = prevResults.iterator();
        while (prevIter.hasNext()) {
          HashMap prevRow = (HashMap)prevIter.next();
          Number prevID = (Number)prevRow.get("MessageID");
          previousID = prevID.intValue();
          prevNextMap.put("previousMessage", new Integer(prevID.intValue()));
          break;
        }
      }
      cvdal.setSqlQueryToNull();

      String nextQuery = "SELECT em.MessageID FROM emailmessage em LEFT JOIN emailmessagefolder emf " +
                         "ON (em.MessageID=emf.MessageID) WHERE emf.FolderID=? AND " +
                         "em.MessageDate<=? AND em.MessageID!=? ORDER BY em.MessageDate DESC LIMIT 2";

      cvdal.setSqlQuery(nextQuery);
      cvdal.setInt(1, messageVO.getEmailFolderID());
      cvdal.setRealTimestamp(2, sqlTimestamp);
      cvdal.setInt(3, messageID);
      Collection nextResults = cvdal.executeQuery();
      if (nextResults != null) {
        Iterator nextIter = nextResults.iterator();
        while (nextIter.hasNext()) {
          HashMap nextRow = (HashMap)nextIter.next();
          Number nextID = (Number)nextRow.get("MessageID");
          if (nextID.intValue() != previousID) {
            prevNextMap.put("nextMessage", new Integer(nextID.intValue()));
            break;
          }
        }
      }
      cvdal.setSqlQueryToNull();
    }catch(Exception e){
      logger.error("[getPreviousNextLinks]: Exception", e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return prevNextMap;
  }   // end getPreviousNextLinks(int,int) method

  /**
   * Returns an ArrayList which contains HashMap objects representing the email
   * folders which are a sub-folder of the given <code>parentID</code> parameter.
   * @param individualID The individualID of the user who is asking for the list.
   * @param parentID The folderID of the folder for which we want the list of sub-folders.
   * @return ArrayList of HashMaps representing the sub-folders.
   */
  public ArrayList getSubFolderList(int individualID, int parentID)
  {

    ArrayList folderList = new ArrayList();

    if (parentID <= 0){ return folderList; }

    try {
    InitialContext ic = CVUtility.getInitialContext();
    CommonHelperLocalHome commonHelperHome = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
    CommonHelperLocal commonHelperRemote =  commonHelperHome.create();
    commonHelperRemote.setDataSource(this.dataSource);
    folderList = commonHelperRemote.getSubFolderList(individualID,parentID, "emailfolder");
    }catch(Exception e){
      logger.error("[getSubFolderList]: Exception", e);
    }
    return(folderList);
  }


  /**
   * Updates the record for a given folder in the database, taking
   * the information from the given MailFolderVO object. Currently,
   * we're only allowing folder Name and Parent ID to be updated,
   * all other fields will be disregarded.
   * @param individualID The individualID of the user who is changing the folder.
   * @param folderVO A MailFolderVO object containing the details of the
   *        changed folder. folderID, folderName and parentID are required.
   * @return int - the number of rows affected by the update.
   */
  public int editFolder(int individualID, MailFolderVO folderVO)
  {
    int rowsAffected = -1;

    CVDal cvdal = new CVDal(this.dataSource);

    try {
      rowsAffected = this.editFolder(individualID, folderVO, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in editFolder(int,MailFolderVO): " + e);
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return rowsAffected;
  }


  /**
   * Updates the record for a given folder in the database, taking
   * the information from the given MailFolderVO object. Currently,
   * we're only allowing folder Name and Parent ID to be updated,
   * all other fields will be disregarded.
   * @param individualID The individualID of the user who is changing the folder.
   * @param folderVO A MailFolderVO object containing the details of the
   *        changed folder. folderID, folderName and parentID are required.
   * @param cvdal An <strong>open</strong> database connection.
   * @return int - the number of rows affected by the update.
   */
  private int editFolder(int individualID, MailFolderVO folderVO, CVDal cvdal) throws Exception
  {
    int rowsAffected = -1;
    try {
      // first, check to see if this folder is on an IMAP account
      int accountID = folderVO.getEmailAccountID();
      MailAccountVO accountVO = this.getMailAccountVO(accountID);
      String accountType = accountVO.getAccountType();

      // if the account type is IMAP, then we need to change the folder remotely, too
      boolean imapFolderModified = true;
      if (accountType != null && accountType.equals(MailAccountVO.IMAP_TYPE)) {
        imapFolderModified = this.editImapFolder(folderVO, accountVO, cvdal);
      }

      if (imapFolderModified) {
        // should be true unless editImapFolder() returned false...

        String query = "UPDATE emailfolder SET Name=?, Parent=? WHERE FolderID=?";
        cvdal.setSqlQuery(query);
        cvdal.setString(1, folderVO.getFolderName());
        cvdal.setInt(2, folderVO.getParentID());
        cvdal.setInt(3, folderVO.getFolderID());
        rowsAffected = cvdal.executeUpdate();
      }
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return rowsAffected;
  }

  /**
   * Updates a remote IMAP folder with the information in <code>folderVO</code>.
   * This method opens the remote copy of the folder specified by
   * <code>folderVO.getFolderID()</code>, then updates that remove folder
   * with the rest of the information from <code>folderVO</code>. The info
   * in <code>accountVO</code> is used to connect to the remote server.
   * @param folderVO A fully populated MailFolderVO object representing the
   *        <strong>new</strong> folder object.
   * @param accountVO A MailAccountVO object with details of the mail server
   *        that we'll connect to.
   * @param cvdal An <strong>open</strong> database connection.
   * @return boolean True if the remote folder is updated, false otherwise.
   */
  private boolean editImapFolder(MailFolderVO folderVO, MailAccountVO accountVO, CVDal cvdal)
  {
    boolean folderUpdated = false;
    Store store = null;
    Folder remoteFolder = null;
    Folder newFolder = null;

    try {
      // open the remote mail server...
      Properties properties = System.getProperties();
      Session session = Session.getDefaultInstance(properties);
      store = session.getStore(accountVO.getAccountType());
      store.connect(accountVO.getMailServer(), accountVO.getLogin(), accountVO.getPassword());

      // get the existing folder info from the database... (before it's changed)
      MailFolderVO oldFolderVO = this.getEmailFolder(folderVO.getFolderID());

      // get the full path to the old folder (don't include "/root")
      int parentID = oldFolderVO.getParentID();
      String parentFolderName = this.getFolderFullPathString(parentID, false, cvdal);
      if (parentFolderName != null && parentFolderName.length() > 0) {
        parentFolderName = parentFolderName.substring(1, parentFolderName.length()) + "/";    // strip the leading "/" and put one at the end...
      }


      // get the remote folder (that hasn't changed yet). We'll need
      // a handle to the remote folder in order to update it..
      remoteFolder = (Folder)store.getFolder(parentFolderName + oldFolderVO.getFolderName());

      if (remoteFolder != null && remoteFolder.exists()) {
        // we can only update the remote folder if it exists on the remote server...
        // Now, get the info for the new folder (updated info). Get the full path, etc...
        int newParentID = folderVO.getParentID();
        String newParentFolderName = this.getFolderFullPathString(newParentID, false, cvdal);
        newParentFolderName = newParentFolderName.substring(1, newParentFolderName.length());

        // open the "new" folder on the remote server (a handle to an non-existent object)....
        newFolder = store.getFolder(newParentFolderName + "/" + folderVO.getFolderName());

        if (! newFolder.exists()) {
          // if the handle we just opened already exists on the remote
          // server, we can't update the folder (a folder with the same
          // path/name exists). Therefore, if the folder does NOT exist
          // on the remote server, update it....
          try {
            folderUpdated = remoteFolder.renameTo(newFolder);
          }catch(Exception e){
            e.printStackTrace();
          }
        }
      }
    }catch(MessagingException me){
      // TODO: handle the MessagingException
      me.printStackTrace();
    }finally{
      // Clean up all open handles...
      if (remoteFolder != null && remoteFolder.isOpen()){
        try{ remoteFolder.close(false); }catch(Exception e){/*"I feel stupid, and contagious..."*/}
      }
      if (newFolder != null && newFolder.isOpen()){
        try{ newFolder.close(false); }catch(Exception e){/*"I've got more rhymes than the bible's got psalms..."*/}
      }
      if (store != null && store.isConnected()){
        try{ store.close(); }catch(Exception e){/*"Don't call me 'daughter'..."*/}
      }
    }
    return folderUpdated;
  }


  private int getFolderIDFromFullPath(String fullPath, int accountID, CVDal cvdal)
  {
    int foundFolderID = -1;
    try {
      // get list of folder IDs for accountID
      HashMap folderMap = (HashMap)this.getFolderList(accountID, cvdal);
      Set keySet = folderMap.keySet();
      Iterator iter = keySet.iterator();
      while (iter.hasNext()) {
        Number folderID = (Number)iter.next();
        String tmpFullPath = this.getFolderFullPathString(folderID.intValue(), true, cvdal);
        if (tmpFullPath != null && (tmpFullPath.toLowerCase()).equals(fullPath.toLowerCase())) {
          return folderID.intValue();
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] getMailFolderVOFromFullPath(): " + e);
      e.printStackTrace();
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(foundFolderID);
  }

  /**
   * Returns an ArrayList of mail accountIDs for those accounts.
   * @return ArrayList of DDNameValue which will hold the account ID and Account email address.
   */
  public Vector getAccountList()
  {
    Vector accountList = new Vector();
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      String selectQuery = "SELECT accountID,Address from emailaccount";
      cvdal.setSqlQuery(selectQuery);
      Collection results = cvdal.executeQuery();

      if (results != null) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap resultRow = (HashMap)iter.next();
          Number accountID = (Number)resultRow.get("accountID");
          String emailAddress = (String)resultRow.get("Address");
          accountList.add(new DDNameValue(accountID.intValue(),emailAddress));
        }
      }// end if (results != null)
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] Exception thrown in getAccountList(): " + e.toString());
      // e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(accountList);
  }   // end getAccountList() method

  /**
  * This method takes an ArrayList message IDs, and processes each as
  * though it was a incoming support ticket or reply to a support ticket
  * thread.
  * @param messageIDs The collection of message IDs
  * @param ownerID The owner of the Email Account (who
  * will also be the owner of this message).
  */
  private void supportEmail(ArrayList messageIDs, int ownerID)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();

      TicketLocalHome ticketHome = (TicketLocalHome)ic.lookup("local/Ticket");
      TicketLocal ticketRemote = (TicketLocal) ticketHome.create();
      ticketRemote.setDataSource(this.dataSource);

      // Used for getting the Default Owner ID.
      AppSettingsLocalHome appHome=(AppSettingsLocalHome)ic.lookup("local/AppSettings");
      AppSettingsLocal appRemote=(AppSettingsLocal)appHome.create();
      appRemote.setDataSource(this.dataSource);
      String strAssignedID = appRemote.getApplicationSettings("DEFAULTOWNER");
      appRemote = null;
      appHome = null;

      // Used to get the Entity information when passing the EmailAddress
      ContactHelperLocalHome contactHelperHome = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal contactHelperRemote =  contactHelperHome.create();
      contactHelperRemote.setDataSource(this.dataSource);

      // Setting the Record Permission for newly Created record.
      AuthorizationLocalHome authHome=(AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal authRemote=(AuthorizationLocal)authHome.create();
      authRemote.setDataSource(this.dataSource);

      // get the Default Error Message Template for the Support replying emails
      EmailSettingsLocalHome emailSettingsHome=(EmailSettingsLocalHome)ic.lookup("local/EmailSettings");
      EmailSettingsLocal emailSettingsRemote=(EmailSettingsLocal)emailSettingsHome.create();
      emailSettingsRemote.setDataSource(this.dataSource);

      // Its a predefined Template for the replying message for the newly created Ticket
      EmailTemplateForm ticketTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_SUPPORTTICKET);

      // Its a predefined Template for the replying message for the new created Thread
      EmailTemplateForm threadTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_SUPPORTTHREAD);

      // Its a predefined Template for the replying for Error Message
      EmailTemplateForm errorTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_SUPPORTERROR);

      emailSettingsRemote = null;
      emailSettingsHome = null;

      String defaultMessageBody = "";

      for (int i=0; i < messageIDs.size() ; i++) {
        int messageID = ((Integer) messageIDs.get(i)).intValue();
        if (messageID != -1) {
          MailMessageVO mailMessageVO = this.getEmailMessageVO(ownerID, messageID);
          String messageFrom = mailMessageVO.getFromAddress();

          InternetAddress emailAddress = new InternetAddress(messageFrom);
          messageFrom = emailAddress.getAddress();

          String messageSubject = mailMessageVO.getSubject();
          String messageBody = mailMessageVO.getBody();
          Html2Text plainMessageBody = new Html2Text(messageBody);

          int accountID = mailMessageVO.getEmailAccountID();
          int ticketID = 0;

          int startIndex = messageSubject.indexOf("TICKET#");
          int endIndex = -1;
          if (startIndex >0) {
            endIndex = messageSubject.indexOf("#",(startIndex + 7));
          }

          if (startIndex >0 && endIndex > startIndex) {
            startIndex = startIndex + 7;
            String tempTicketID =messageSubject.substring(startIndex,endIndex);
            if (tempTicketID != null && !tempTicketID.equals("")) {
              ticketID = Integer.parseInt(tempTicketID);
            }
          }

          if (ticketID == 0) {
            TicketVO ticketVO = new TicketVO();
            ticketVO.setTitle(messageSubject);
            ticketVO.setDetail(plainMessageBody.getPlainText());
            ticketVO.setPriorityId(1);
            ticketVO.setStatusId(1);
            ticketVO.setCreatedBy(ownerID);

            HashMap contactInfo = contactHelperRemote.getContactInfoForEmailAddress(messageFrom);

            int assignedId = 0;
            int entityId = 0;
            int individualId = 0;

            if (strAssignedID != null && !strAssignedID.equals("")) {
              try {
                assignedId = Integer.parseInt(strAssignedID);
              } catch(NumberFormatException nfe){
                // already set a default value
              }
            }

            if (contactInfo != null && contactInfo.size() != 0) {
              entityId = ((Number) contactInfo.get("EntityID")).intValue();
              individualId = ((Number) contactInfo.get("IndividualID")).intValue();
            }else{
              // initialize thread vo
              ThreadVO tVO = new ThreadVO();
              tVO.setTitle(messageFrom+": "+messageSubject);
              tVO.setDetail("");
              tVO.setPriorityId(1);
              tVO.setCreatedBy(ownerID);
              tVO.setOwner(ownerID);

              ticketVO.setThreadVO(tVO);
            } // end of else if (contactInfo != null && contactInfo.size() != 0)

            ticketVO.setAssignedToId(assignedId);
            ticketVO.setRefEntityId(entityId);
            ticketVO.setRefIndividualId(individualId);

            defaultMessageBody = ticketTemplateForm.getBody();
            String ticketSubject = ticketTemplateForm.getSubject();

            try {
              ticketID = ticketRemote.addTicket(ownerID, ticketVO);
            }catch(Exception e){
              defaultMessageBody = errorTemplateForm.getBody();
            }// end of catch block

            messageSubject = ticketSubject+"-["+messageSubject+"] TICKET#"+ticketID+"#";
            int arrBlank[] = {};
            authRemote.saveRecordPermission(ownerID,-1,"Ticket",ticketID,arrBlank,arrBlank,arrBlank);
          }else{
            // initialize thread vo
            ThreadVO tVO = new ThreadVO();
            tVO.setTitle(messageSubject);
            tVO.setDetail(plainMessageBody.getPlainText());
            tVO.setTicketId(ticketID);
            tVO.setPriorityId(1);
            tVO.setCreatedBy(ownerID);
            tVO.setOwner(ownerID);

            defaultMessageBody = threadTemplateForm.getBody();

            try {
              ticketRemote.addThread(ownerID, tVO);
            }catch(Exception e){
              defaultMessageBody = errorTemplateForm.getBody();
            }// end of catch block

            messageSubject = "RE: "+messageSubject;
          }// end of else for if (ticketID == 0)

          // create a MailMessageVO
          MailMessageVO messageVO = new MailMessageVO();
          messageVO.setEmailAccountID(accountID);

          MailAccountVO accountVO = this.getMailAccountVO(accountID);
          if (accountVO == null) {
            accountVO = new MailAccountVO();
          }

          InternetAddress fromAddress = new InternetAddress(accountVO.getEmailAddress(), accountVO.getAccountName());
          messageVO.setFromAddress(fromAddress.toString());
          messageVO.setReplyTo(accountVO.getReplyToAddress());

          ArrayList toList = new ArrayList();
          toList.add(messageFrom);
          messageVO.setToList(toList);
          messageVO.setSubject(messageSubject);
          messageVO.setBody(defaultMessageBody);
          messageVO.setHeaders("");
          messageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);
          messageVO.setReceivedDate(new java.util.Date());

          try {
            // send the message
            boolean messageSent = this.sendMessage(accountVO, messageVO);
          }catch(SendFailedException sfe){
            sfe.printStackTrace();
          }
        }   // end if (messageID != -1)
      }   // end for (int i=0; i<messageIDs.size(); i++)
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] supportEmail(): " + e);
      // e.printStackTrace();
    }
  } // end supportEmail(ArrayList,int) method

  /**
  * Returns an boolean true if the account supports Ticket otherwise false
  * @return boolean of isSupportEmailAccount.
  * @param int of emailAccountID.
  */
  public boolean isSupportEmailAccount(int emailAccountID, CVDal cvdal)
  {
    boolean isSupportEmailAccount = false;
    try {
      cvdal.setSqlQuery("SELECT EmailAccountID FROM supportemailaccount WHERE EmailAccountID=?");
      cvdal.setInt(1, emailAccountID);
      Collection col = cvdal.executeQuery();
      cvdal.clearParameters();
      if (col != null) {
        Iterator it = col.iterator();
        if (it.hasNext()) {
          isSupportEmailAccount = true;
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] isSupportEmailAccount(): " + e);
      // e.printStackTrace();
    }
    return isSupportEmailAccount;
  }   // end isSupportEmailAccount(int,CVDal) method

  /**
   * This method is to help cut down on processing time on the Home page
   * which originally made at least 4 EJB remote calls from the JSP and
   * then would loop on the list of folders.  This will simply return the
   * folderVOs of the desired folders or it will be empty if there is
   * nothing to show.
   * @param individualId The individualID of the user whose folder list we're getting.
   * @return ArrayList of MailFolderVOs
   */
  public ArrayList getHomeFolderList(int individualId)
  {
    ArrayList folders = new ArrayList();
    int defaultAccount = this.getDefaultAccountID(individualId);
    if (defaultAccount > 0) {
      MailAccountVO account = this.getMailAccountVO(defaultAccount);
      if (account.getAccountID() > 0) {
        CVDal cvdl = new CVDal(this.dataSource);
        try {
          String query = "SELECT FolderID FROM emailfolder WHERE AccountID=? AND ftype='system' AND (name='trash' OR name='inbox' OR name='drafts') ORDER BY folderId, parent";
          cvdl.setSqlQuery(query);
          cvdl.setInt(1,account.getAccountID());
          Collection results = cvdl.executeQuery();
          if (null != results) {
            Iterator iter = results.iterator();
            while(iter.hasNext()) {
              HashMap folderResult = (HashMap)iter.next();
              Number folderId = (Number)folderResult.get("FolderID");
              folders.add(this.getEmailFolder(folderId.intValue()));
            }
          } // end if (null != results)
        } finally {
          cvdl.destroy();
          cvdl = null;
        }
      } // end if (account.getAccountID() > 0)
    } // end if (defaultAccount > 0)
    return folders;
  }   // end getHomeFolderList(int) method

  /**
   * Returns an ArrayList of RuleVO objects that area associated with
   * the given <code>accountID</code>.
   * @param accountID The accountID which we want the list of rules for.
   * @param cvdal An <strong>open</strong> database connection.
   * @return ArrayList of AccountVO objects.
   */
  private ArrayList getRulesForAccount(int accountID, CVDal cvdal)
  {
    ArrayList rulesList = new ArrayList();
    try {
      String query = "SELECT ruleID FROM emailrule WHERE accountID=?";
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, accountID);
      Collection results = cvdal.executeQuery();
      if (results != null) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          Number ruleID = (Number)row.get("ruleID");
          RuleVO ruleVO = this.getRule(ruleID.intValue(), cvdal);
          rulesList.add(ruleVO);
  }
      }
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(rulesList);
  }   // end getRulesForAccount(int,CVDal) method

  /**
   * Updates the emailrule.enabled field in the database, setting it to 'YES'.
   * @param individualID The individual who is changing the enabled status.
   * @param ruleID The ID of the rule whose status is to be changed.
   * @param status If true, the enabled field will be set to true, else will be set to false.
   * @return boolean - true for success, false for failure.
   */
  public boolean enableRule(int individualID, int ruleID, boolean status)
  {
    boolean returnValue = false;
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      returnValue = this.enableRule(individualID, ruleID, status, cvdal);
    }catch(Exception e){
      System.out.println("[Exception][MailEJB] enableRule(int,int): " + e);
      // e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return(returnValue);
  }   // end enableRule(int,int) method

  /**
   * Updates the emailrule.enabled field in the database, setting it to 'YES'.
   * @param individualID The individual who is changing the enabled status.
   * @param ruleID The ID of the rule whose status is to be changed.
   * @param status If true, the enabled field will be set to true, else will be set to false.
   * @param cvdal An <strong>open</strong> database connection.
   * @return boolean - true for success, false for failure.
   */
  private boolean enableRule(int individualID, int ruleID, boolean status, CVDal cvdal) throws Exception
  {
    boolean returnStatus = true;
    try {
      if (ruleID <= 0){ return(false); }
      String query = "UPDATE emailrule SET enabled=? WHERE ruleID=?";
      cvdal.setSqlQuery(query);
      cvdal.setString(1, (status == true) ? "YES" : "NO");
      cvdal.setInt(2, ruleID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();
    }catch(Exception e){
      returnStatus = false;
    }finally{
      cvdal.setSqlQueryToNull();
    }
    return(returnStatus);
  }   // end enableRule(int,int,CVDal) method


  /**
   * Sends a message. This method provides the basic functionality for sending a message.
   * @param individualID The ID of the Individual who is sending the message.
   * @param mailMessageVO The built mailMessageVO. This cannot be <code>null</code>.
   * @return true if the message was sent, false if it wasn't.
   * @throws SendFailedException The message could not be sent for whatever reason.
   *         Check the Exception for a detailed reason.
   * @throws MessagingException when processing the message some of method throws the MessagingException.
   */
  public boolean simpleMessage(int individualID, MailMessageVO mailMessageVO) throws SendFailedException, MessagingException
  {

    boolean messageSent = false;
    if (mailMessageVO == null) {
      throw new SendFailedException("<error>Message cannot be null. Please set up the message.</error>");
    }// end if (mailMessageVO == null)

    CVDal cvdal = new CVDal(this.dataSource);
    try{

      String smtpServer = null;
      String username = null;
      String password = null;
      int smtpPort = 25;
      boolean connectToPopFirst = false;
      boolean smtpAuthenticationRequired = false;
      String serverType = null;
      String serverAddress = null;
      String adminEmailAddress = "";

      String query = "SELECT smtpserver, username, password, authentication, smtpport FROM emailsettings";
      cvdal.setSqlQuery(query);
      Collection results = cvdal.executeQuery();
      if (results != null) {
        Iterator iter = results.iterator();
        if (iter.hasNext()) {
          HashMap folderInfo = (HashMap)iter.next();
          smtpPort = ((Number)folderInfo.get("smtpport")).intValue();
          smtpServer = (String)folderInfo.get("smtpserver");
          username = (String)folderInfo.get("username");
          password = (String)folderInfo.get("password");
          String authentication = (String)folderInfo.get("authentication");
          if (authentication != null && authentication.equals("YES")){
            smtpAuthenticationRequired = true;
          }
          adminEmailAddress = (String)folderInfo.get("adminemailaddress");
        }
      }

      Address arrayBcc[] = new Address[0];
      Address arrayCc[] = new Address[0];
      Address arrayTo[] = new Address[0];

      //Build the JavaMail message
      Properties props = System.getProperties();
      if (smtpServer != null) {
        props.put("mail.smtp.host", smtpServer);
      }else{
        throw new SendFailedException("<error>The SMTP Server has not been setup.</error>");
      }

      Session session = Session.getDefaultInstance(props, null);
      MimeMessage message = new MimeMessage(session);

      Collection bccList = mailMessageVO.getBccList();
      Collection ccList = mailMessageVO.getCcList();
      Collection toList = mailMessageVO.getToList();
      Collection attachments = mailMessageVO.getAttachedFiles();

      String subject = mailMessageVO.getSubject();
      String body = mailMessageVO.getBody();
      String fromAddress = mailMessageVO.getFromAddress();
      String replyToAddress = mailMessageVO.getReplyTo();
      String headers = mailMessageVO.getHeaders();
      String messageType = mailMessageVO.getContentType();

      //if you don't specify the from email address we will enter the administrator email address
      if(fromAddress == null){
        fromAddress =  adminEmailAddress;
      }
      message.setFrom(new InternetAddress(fromAddress));

      if (replyToAddress != null && !replyToAddress.equals("")){
        message.setReplyTo(new Address[] {new InternetAddress(replyToAddress)});
      }

      //Add raw headers to message object
      StringTokenizer tokenizer = new StringTokenizer(headers, System.getProperty("line.separator", "\n"));
      while (tokenizer.hasMoreTokens()) {
        message.addHeaderLine(tokenizer.nextToken());
      }

      //Most email clients add this line with the name of
      //their software and the version
      message.addHeader("X-Mailer", "Centraview v. " + CentraViewConfiguration.getVersion());

      message.setSubject(subject);
      BodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(body, messageType);

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      // Handle attachments
      if (attachments != null) {
        message.setContent(multipart);
        Iterator attachmentIterator = attachments.iterator();
        while (attachmentIterator.hasNext()) {
          messageBodyPart = new MimeBodyPart();
          CvFileVO thisAttachment = (CvFileVO) attachmentIterator.next();
          String path = thisAttachment.getPhysicalFolderVO().getFullPath(null, true) + thisAttachment.getName();
          DataSource source = new FileDataSource(path);
          messageBodyPart.setDataHandler(new DataHandler(source));
          messageBodyPart.setFileName(thisAttachment.getTitle());
          multipart.addBodyPart(messageBodyPart);
        }
      }

      message.setSentDate(new Date());

      String mailSendList = "";
      String mailFailedList = "";

      //End of Build The JavaMail message

      // We must have to send seperate message to individual.
      // We must have to keep track of message sent to list and failed while send message to particular individual.
      if (toList != null) {
        Iterator toIterator = toList.iterator();
        int count = 0;
        while (toIterator.hasNext()) {
          String toAddress = (String) toIterator.next();
          message.setRecipients(Message.RecipientType.TO, toAddress);
          try{
            messageSent = this.sendSimpleMessage(message, smtpServer, username, password, smtpPort, connectToPopFirst, smtpAuthenticationRequired, serverType, serverAddress);
          } catch(SendFailedException sfe) {
            // we will catch the invalid Address and by this way we will know that recipient will not receive the mail and we add individual in the failing list.
            Address[] invalidAddress = sfe.getInvalidAddresses();
            if (invalidAddress != null){
              mailFailedList += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ toAddress + "<br>";
              messageSent = false;
            } else {
              throw new SendFailedException(sfe.getMessage());
            }
          }

          if (messageSent){
            // if the message is sent successfully to individual then add to the send list
            mailSendList += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ toAddress + "<br>";
          }
        }
      }

      // If suppose the mailFailedList is not blank. then we have to throw error stating the succesfully send list and failed list
      if ((mailFailedList != null && !mailFailedList.equals(""))){
        //Format the message for failed user and successfully sent individual's
        // TODO: remove this HTML from the EJB layer!!!!!!! *sigh*
        String errorMessage = "<failed> <font color=\"#008000\"> Successfully Sent to : <br>"+mailSendList
                                +"</font>"
                                +"<font color=\"#FF0000\"><br> Failed : <br>"+mailFailedList
                                +"</failed></font>";
        throw new SendFailedException(errorMessage);
      }
    } finally {
      cvdal.destroy();
    }
    return messageSent;
  }// end of boolean simpleMessage(int individualID, Message message)

  /**
   * Gets a list of all the accounts which are marked as "Support"
   * accounts, and calls this.getMail() for each account.
   * @return void
   */
  public void checkAllSupportAccounts()
  {
    CVDal cvdal = new CVDal(this.dataSource);

    try {
      cvdal.setSqlQuery("SELECT EmailAccountID AS accountID FROM supportemailaccount");
      Collection results = cvdal.executeQuery();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();
      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          Number accountID = (Number)row.get("accountID");
          this.getMail(this.getMailAccountVO(accountID.intValue()), 0);
        }
      }
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.getEmailFolder: " + e.toString());
      e.printStackTrace();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
  }

  /**
   * We will get the parameter value of (TO/CC/BCC) Address from the Header of Message.
   * then we will look for a standard error of extra delimiter in the to String which will cause for an error while parsing
   * we will look for occurance of delimiter (for example: ",," or ";;"  and replace it by either "," or ";"
   * we will also look for condition like address are ending with ";" and ",". we will remove that last character from the list
   *
   * @param tempToList A String array of emailAddress spearated by some wrong delimiter
   * @param messageID The ID of the message which these recipients
   *        are to be associated with in the database.
   * @param type String representation of the type of recipient
   *        list. Can be one of "TO", "CC", or "BCC".
   * @param cvdal An <strong>open</strong> database connection.
   * @return void
   */
  private void parseRecipients(String tempToList[],int messageID, String type, CVDal cvdal)
  {
  // We will check for two condition when we get the AddressException
  // 1) We will look for occurance of ",," and replace it by "," check with ";" also
  // 2) We should check for wheather its not ending with either "," or ";"
  // 3) When we pass the final string with Internet Address accept only comma delimited values.
    String toListString = "";

    try {
      if (tempToList != null) {
        for (int i=0; i<tempToList.length; i++) {
          String toList = tempToList[i];
          if (toList != null) {
            toList = toList.replaceAll(",,",",");
            toList = toList.replaceAll(";;",";");
            toList = toList.replaceAll(";",",");
            if (toList.endsWith(",")) {
              int len = toList.length();
              toList = toList.substring(0,(len-1));
            }
            toListString = toListString + toList;
          }
        }
      }

      try{
        Address  recipients[] = (Address[])InternetAddress.parse(toListString);
        this.addRecipients(recipients, messageID, type, cvdal);
      } catch(AddressException ae) {
        // We can't lose information, In any cost.
        // Some subcription email will not have the list of all address
        // it will be in form of " BLAH BLAH "
        // We can't lose this information.
    String toRecipientArray[] = toListString.split(",");
    List toRecipientList = Arrays.asList(toRecipientArray);
    ArrayList addressList = new ArrayList(toRecipientList);
    int[] batchResults = cvdal.addRecipients(addressList,messageID,type);
      }
    }catch(Exception e){
      System.out.println("[Exception] MailEJB.parseRecipients: " + e.toString());
    }
  }   // end parseRecipients() method


  /**
   * Verifies email server and login information, and throws a
   * <code>MessagingException</code> if the information is not valid.
   * Checks server type, smtp server address, and login and password info.
   * Attempts to make a remote connection to the SMTP server with the
   * given credentials; throws an exception if no connection can be made.
   * @param accountVO A MailAccountVO object with details of the mail server that we'll connect to.
   * @return void
   * @throws MessagingException If any of the account credentials are wrong then it will throw the error.
   */
  private void checkEmailCredentials(MailAccountVO accountVO) throws MessagingException
  {
    try {
      Properties properties = System.getProperties();

      //TODO: Add the secure IMAP connection stuff in here.
      //Look at http://www.javaworld.com/javatips/jw-javatip115_p.html
      Session session = Session.getDefaultInstance(properties);
      Store store = session.getStore(accountVO.getAccountType());
      store.connect(accountVO.getMailServer(), accountVO.getLogin(), accountVO.getPassword());
      store.close();
      store = null;
      session = null;
    }catch(MessagingException me){
      if (me instanceof AuthenticationFailedException) {
        throw new MessagingException(me.getMessage()+" Information.");
      }

      if (me instanceof NoSuchProviderException) {
        throw new MessagingException(me.getMessage());
      }

      Exception e = me.getNextException();
      if (e instanceof UnknownHostException) {
        throw new MessagingException("Unknown host " + e.getMessage() + ".");
      }
    }
  }   // end checkEmailCredentials() method

  /**
   * Calculate total message size.  Try our best to deal with Javamail and JAF.
   *
   * @param messageObject Mail message object.
   * @return Size of message as int.
   * @throws MessagingException
   */
  private int getMessageSize(Object messageObject)
  {
    int size = 0;
    Class messageClass = messageObject.getClass();
    InputStream inputStream = null;
    Exception unhandledException = null;
    try {
      Method getHeadersMethod = messageClass.getMethod("getAllHeaderLines", null);
      Enumeration enum = (Enumeration)getHeadersMethod.invoke(messageObject, null);
      while (enum.hasMoreElements()) {
        String header = (String)enum.nextElement();
        size += header.length();
      }
      Method getInputStream = messageClass.getMethod("getInputStream", null);
      inputStream = (InputStream)getInputStream.invoke(messageObject, null);
    } catch(InvocationTargetException ite) {
      try {
        Object o = ite.getCause();
        if ((o instanceof UnsupportedEncodingException) || (o instanceof IOException)) {
          Method getRawStream = messageClass.getMethod("getRawInputStream", null);
          inputStream = (InputStream)getRawStream.invoke(messageObject, null);
        } else {
          unhandledException = ite;
        }
      } catch (Exception e) {
        unhandledException = e;
      }
    } catch (Exception e) {
      unhandledException = e;
    }
    try {
      while (inputStream.read() > -1){ size++; }
    } catch (Exception e) {
      unhandledException = e;
    } finally {
      if (unhandledException != null) {
        logger.error("Exception thrown in getMessageSize()");
        unhandledException.printStackTrace();
      }
    }
    return(size);
  }

  /**
   * Returns an ArrayList of HashMap objects, each representing a
   * message in the given accountID's "Templates" folder.
   * @param individualID The individualID of the user requesting the Template list.
   * @param accountID The email accountID for which to return templates.
   * @return ArrayList of HashMaps representing Templates.
   */
  public ArrayList getTemplateList(int individualID, int accountID)
  {
    ArrayList templateList = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      String sql = "SELECT em.messageID, em.subject FROM emailmessage em " +
                   "LEFT JOIN emailmessagefolder emf ON (em.messageID=emf.messageID) " +
                   "LEFT JOIN emailfolder ef ON (emf.folderID=ef.folderID) " +
                   "WHERE em.accountID=? AND ef.Ftype='SYSTEM' AND ef.Name=?";

      cvdal.setSqlQuery(sql);
      cvdal.setInt(1, accountID);
      cvdal.setString(2, MailFolderVO.TEMPLATES_FOLDER_NAME);
      Collection results = cvdal.executeQuery();

      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          templateList.add(row);
        }
      }
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return templateList;
  }   // end getTemplateList(int,int) method

  /**
   * Returns a ValueListVO representing a list of email Message records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getEmailValueList(int individualID, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort options of the final query should be built using
    //    data from the parameters object.
    // 3. Add limit to the final select query.
    // 4. The columns from each row of the query will be stuffed into an arraylist
    //    Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();

    // permissionSwitch turns the permission parts of the query on and off.
    // if individualID is less than zero then the list is requested without limiting
    // rows based on record rights.  If it is true than the rights are used.
    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    try {
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE emaillistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }
      // We can't use the numberOfRecords in calculation we will get total message count
      // independent of the folder..
      int numberOfRecords = 0;
      if (applyFilter) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "emaillistfilter", individualID, 2, "emailmessage", "MessageID", "Owner", null, permissionSwitch);
      } else if (permissionSwitch) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 2, "emailmessage", "MessageID", "Owner", null, permissionSwitch);
      }

      // Create a temporary table with out applying the Limit on the search.
      String query = this.buildEmailListQuery(applyFilter, permissionSwitch, parameters);
      cvdl.setSqlQuery("CREATE TEMPORARY TABLE emaillist "+query);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      //get the limit criteria, concat the string with final query.
      String limit = parameters.getLimitParam();

      // Now, Finally, we can just select the email list and populate value List
      cvdl.setSqlQuery("SELECT * FROM emaillist " + limit);
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();

      //We know that emailList table is having the total message.
      //make a count on table and assign to parameter.
      cvdl.setSqlQuery("SELECT count(MessageID) AS numberOfRecords FROM emaillist");
      Collection results = cvdl.executeQuery();
      cvdl.clearParameters();
      cvdl.setSqlQueryToNull();
      numberOfRecords = 0;
      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          numberOfRecords = ((Number) row.get("numberOfRecords")).intValue();
        }
      }

      // Assign the numberOfRecords to the paramters
      parameters.setTotalRecords(numberOfRecords);

      // drop emaillist table
      cvdl.setSqlQuery("DROP TABLE emaillist");
      cvdl.executeUpdate();
      // throw away the temp filter table, if necessary.
      if (applyFilter) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE emaillistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
    }
    finally{
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }

  private String buildEmailListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    String select =
        " SELECT emailmessage.MessageID, emailmessage.Subject, emailmessage.MessageDate, emailmessage.MailFrom, emailmessage.ReplyTo, emailmessage.size, " +
      "emailmessage.Priority, emailmessage.messageRead AS readStatus, es.FolderID, " +
      "COUNT(at.AttachmentID) AS noOfAttachment, ToList ";

      String joinConditions =
       " LEFT JOIN emailmessagefolder es ON (emailmessage.MessageID=es.MessageID) " +
      " LEFT JOIN attachment at ON (emailmessage.MessageID=at.MessageID) ";

   StringBuffer from = new StringBuffer(" FROM emailmessage ");
   StringBuffer where = new StringBuffer(" WHERE emailmessage.LocallyDeleted = 'NO' "
        + " AND ( emailmessage.MovedToFolder=" + parameters.getFolderID() + " OR emailmessage.MovedToFolder=0 ) ");
   if(parameters.getFolderID() != 0) {
     where.append(" AND es.folderid=" + parameters.getFolderID());
   }
   String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
   String groupBy = " GROUP BY emailmessage.MessageID ";

   // If a filter is applied we need to do an additional join against the
   // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
       from.append(", listfilter AS lf ");
       where.append(" AND emailmessage.MessageID = lf.MessageID ");
   }
   // IMPORTANT : We haven't applied the limit at this stage.
   // because we need to get the total records.

   // Build up the actual query using all the different permissions.
   // Where owner = passed individualId
   StringBuffer query = new StringBuffer();
   query.append(select);
   query.append(from);
   query.append(joinConditions);
   query.append(where);
   query.append(groupBy);
   query.append(orderBy);

   return query.toString();
  }

  /**
   * Returns a ValueListVO representing a list of rule which are applied Message records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getRuleValueList(int individualID, ValueListParameters parameters)
  {

    ArrayList list = new ArrayList();

    // Since Rules Doen't have the moduleID. We can't use the Advance Search
    // filter will have the direct String which we have to search..
    String filter = parameters.getFilter();

    int accountID = parameters.getAccountID();

    boolean applyFilter = false;

    CVDal cvdl = new CVDal(this.dataSource);
    try {
      StringBuffer whereClause = new StringBuffer();
      StringBuffer fromClause = new StringBuffer();

     String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    //Since Rule Doesn't have ModuleID
    //apply the filter value on each column of emailrule table.
      if (filter != null && filter.length() > 0) {
           String queryFilter = "CREATE TEMPORARY TABLE rulelistfilter SELECT ruleID from emailrule " +
             " WHERE ruleID LIKE '%"+filter+"%' OR "+
         " accountID LIKE '%"+filter+"%' OR name LIKE '%"+filter+"%' OR "+
         " description LIKE '%"+filter+"%' OR enabled LIKE '%"+filter+"%'";
           cvdl.setSqlQuery(queryFilter);
           cvdl.executeUpdate();
           cvdl.setSqlQueryToNull();

           applyFilter = true;
           whereClause.append(" AND emailrule.ruleID = rlf.ruleID ");
           fromClause.append(" , rulelistfilter rlf ");
      }

      StringBuffer query = new StringBuffer();
      query.append("SELECT emailrule.ruleID, emailrule.name , ");
      query.append(" emailrule.description, emailrule.enabled ");
      query.append(" FROM emailrule " + fromClause.toString() + " WHERE emailrule.accountID="+accountID+" ");
      query.append(whereClause.toString());
      query.append(orderBy);

      cvdl.setSqlQuery("CREATE TEMPORARY TABLE rulelist "+query.toString());
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      // Now, Finally, we can just select the rule list and populate value List
      cvdl.setSqlQuery("SELECT * FROM rulelist "+limit);
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();

      //We know that ruleList table is having all rule associate to account .
      //make a count on table and assign to parameter.
      cvdl.setSqlQuery("SELECT count(ruleID) AS numberOfRecords FROM rulelist ");
      Collection results = cvdl.executeQuery();
      cvdl.clearParameters();
      cvdl.setSqlQueryToNull();
      int numberOfRecords = 0;
      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          numberOfRecords = ((Number) row.get("numberOfRecords")).intValue();
        }
      }
      parameters.setTotalRecords(numberOfRecords);
      // drop rulelist table
      cvdl.setSqlQuery("DROP TABLE rulelist");
      cvdl.executeUpdate();
      // throw away the temp filter table, if necessary.
      if (applyFilter) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE rulelistfilter");
        cvdl.executeUpdate();
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }

  /**
   * Tries various methods of getting the content from the message.  Javamail,
   * and more so JAF, don't handle unknown content type and encodings very well.
   *
   * @param msg Message to extract content from.
   * @return content Part or message content.
   * @throws MessagingException
   */

  private Object getPartContent(Object msg) throws MessagingException
  {
    Object content = null;
    Class partClass = msg.getClass();
    Exception unhandledException = null;
    try {
      Method getContentMethod = partClass.getMethod("getContent", null);
      content = getContentMethod.invoke(msg, null);
    } catch (InvocationTargetException ite) {
      Object o = ite.getCause();
      if (o == null) {
        logger.error("Reflection has failed us...");
      }

      if ((o instanceof UnsupportedEncodingException) || (o instanceof IOException)) {
        try {
          Method getRawMethod = partClass.getMethod("getRawInputStream", null);
          content = getRawMethod.invoke(msg, null);
        } catch (Exception e) {
          unhandledException = e;
        }
      } else {
        unhandledException = ite;
      }
    } catch (Exception e) {
      unhandledException = e;
    } finally {
      if (unhandledException != null) {
        logger.error("Exception thrown in getPartContent()");
        unhandledException.printStackTrace();
      }
    }
    return(content);
  }

  /**
   * This method checks a single IMAP folder for mail.
   * @param folderID
   * @param accountVO
   */
  private void checkIMAPFolder(int folderID, MailAccountVO accountVO)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    IMAPStore store = null;
    IMAPFolder folder = null;
    try {
      Session session = null;
      store = this.setupIMAPConnection(accountVO, session);
      MailFolderVO folderVO = this.getEmailFolder(folderID, cvdal);

      String name = folderVO.getFolderFullName();
      if (name == null) {
        name = folderVO.getFolderName();
      }
      folder = (IMAPFolder)store.getFolder(name);

      int type = folder.getType();
      if ((type & IMAPFolder.HOLDS_MESSAGES) != 0) {
        folder.open(Folder.READ_WRITE);
        Message msgs[] = folder.getMessages();
        FetchProfile fp = new FetchProfile();
        fp.add(IMAPFolder.FetchProfileItem.FLAGS);
        fp.add(UIDFolder.FetchProfileItem.UID);
        folder.fetch(msgs, fp);
        this.handleIMAPMessageArray(msgs, folder, folderID, accountVO, store, cvdal);
      }
    } catch (Exception e) {
      logger.error("[checkIMAPFolder] Exception thrown.", e);
    } finally {
      try {
        if (folder.isOpen()) {
          folder.close(false);
        }
      } catch (Exception e) {}
      try { store.close(); } catch(MessagingException e) {}
      cvdal.destroy();
    }
  }

  /**
   * This method handles an IMAP message array and sync the two ends accordingly.
   *
   * @param msgs
   * @param folder
   * @param folderID
   * @param accountVO
   * @param store
   * @param cvdal
   * @throws MessagingException
   * @throws IOException
   */
  private void handleIMAPMessageArray(Message msgs[], IMAPFolder folder, int folderID, MailAccountVO accountVO, IMAPStore store, CVDal cvdal) throws MessagingException, IOException
  {
    int accountID = accountVO.getAccountID();
    Collection validIDs = new ArrayList();
    ArrayList messageIDs = new ArrayList();

    boolean isSupportEmailAccount = false;
    isSupportEmailAccount = this.isSupportEmailAccount(accountVO.getAccountID(), cvdal);

    for (int i = 0; i < msgs.length; i++) {
      String uid = Long.toString(folder.getUID(msgs[i]));
      // use the uid of this message to get a copy of the local message and update the read flag
      MailMessageVO lwMsg = this.getVLWEmailMessageVO(accountID, folderID, uid, cvdal);
      if (this.emailMessageExists(accountID, folderID, uid)) {
        // Message exists locally, just check for changes.
        validIDs.add(Integer.toString(lwMsg.getMessageID()));
        // First check if it's been modified on our side.
        if (lwMsg.isLocallyModified()) {
          // Seen flag has changed
          boolean readFlag = lwMsg.isMessageRead();
          msgs[i].setFlag(Flags.Flag.SEEN, readFlag);
          lwMsg.setLocallyModified(false);
          this.saveVLWEmailMessageVO(lwMsg, cvdal);
        } else {
          // Make remote modified checks
          boolean save = false;
          boolean readServerFlag = msgs[i].isSet(Flags.Flag.SEEN);
          boolean readApplicationFlag = lwMsg.isMessageRead();

          if (readServerFlag && !readApplicationFlag) {
            // Read remotely
            lwMsg.setMessageRead(true);
            save = true;
          } else if (!readServerFlag && readApplicationFlag) {
            // Unread remotely
            lwMsg.setMessageRead(false);
            save = true;
          }
          if (msgs[i].isSet(Flags.Flag.DELETED)) {
            // Deleted remotely
            this.deleteMessageLocally(accountVO.getOwnerID(), lwMsg.getMessageID(), cvdal);
          } else if (save) {
            // We've modified the local message, so save it locally
            this.saveVLWEmailMessageVO(lwMsg, cvdal);
          }
        }
      } else { // New message
        // We need to preserve the status of the SEEN flag across addMessage.
        boolean msgReadUnReadFlag = msgs[i].isSet(Flags.Flag.SEEN);
        int newMessageID = this.addMessage(msgs[i], uid, accountVO.getAccountID(), accountVO.getOwnerID(), folderID, cvdal);

        HashMap embeddedImageMap = new HashMap();
        this.saveAttachments(this.getPartContent(msgs[i]), newMessageID, accountVO.getAccountID(), accountVO.getOwnerID(), this.getAttachmentFolder(accountVO.getOwnerID()).getFolderId(), cvdal, embeddedImageMap );
        this.addBody(msgs[i], newMessageID, embeddedImageMap, cvdal);

        embeddedImageMap = null;

        // set the SEEN Flag back to what it was initially.
        msgs[i].setFlag(Flags.Flag.SEEN, msgReadUnReadFlag);

        validIDs.add(Integer.toString(newMessageID));
        messageIDs.add(new Integer(newMessageID));
      }
    }

    //  Delete any message that weren't on the server (which means they
    //  were deleted elsewhere (or copied somewhere).
    this.deleteInvalidMessages(accountVO.getOwnerID(), accountVO.getAccountID(), validIDs, folderID, cvdal);

    // Process rules
    if (messageIDs.size() > 0) {
      this.applyRules(accountVO.getAccountID(), messageIDs, cvdal);
    }

    // Support
    if (isSupportEmailAccount) {
      this.supportEmail(messageIDs,accountVO.getOwnerID());
  }
  }

  /**
   * Drop in for new IMAP check all.
   */
  private void newIMAP(MailAccountVO accountVO)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    IMAPStore store = null;
    try { //take this try/catch away
      Session session = null;
      store = this.setupIMAPConnection(accountVO, session);

      IMAPFolder root = (IMAPFolder)store.getDefaultFolder();
      IMAPFolder[] allFolders = (IMAPFolder[])root.list("*");
      HashMap folders = new HashMap();

      for (int i = 0; i < allFolders.length; i++) {
        if (allFolders[i].isSubscribed() || allFolders[i].getName().equals("INBOX")) {
          folders.put(allFolders[i].getFullName(), allFolders[i]);
        }
      }

      this.processIMAPFolders(folders, accountVO, cvdal);

      Iterator iter = folders.values().iterator();
      while (iter.hasNext()) {
        IMAPFolder current = (IMAPFolder)iter.next();
        MailFolderVO folderVO = this.getEmailFolderByName(accountVO.getAccountID(), current.getFullName(), cvdal);
        int folderID = folderVO.getFolderID();
        if (folderID == -1) {
          folderVO = this.getEmailFolderByName(accountVO.getAccountID(), current.getName(), cvdal);
          folderID = folderVO.getFolderID();
        }

        int type = current.getType();
        if ((type & IMAPFolder.HOLDS_MESSAGES) != 0) {
          current.open(Folder.READ_WRITE);
          Message msgs[] = current.getMessages();
          FetchProfile fp = new FetchProfile();
          fp.add(IMAPFolder.FetchProfileItem.FLAGS);
          fp.add(UIDFolder.FetchProfileItem.UID);
          current.fetch(msgs, fp);
          this.handleIMAPMessageArray(msgs, current, folderID, accountVO, store, cvdal);
        }
        if (current.isOpen()) {
          current.close(false);
        }
      }
    } catch (Exception e) {
      logger.error("[newIMAP] Exception thrown.", e);
    } finally {
      try {
        store.close();
      } catch (Exception e) {}
      cvdal.destroy();
    }
  }
  /**
   * This looks at the the folders returned by the IMAP list * command
   * and splits them on the hierarchy separator character to determine parent child relationships
   * and eventually will create any folders that need to be created
   *
   * @param folders
   * @param accountVO
   * @param cvdal
   */
  private void processIMAPFolders(HashMap folders, MailAccountVO accountVO, CVDal cvdal)
  {
    Set fullPathKeys = folders.keySet();
    try { //remove
      Iterator iter = fullPathKeys.iterator();
      while (iter.hasNext()) {
        String key = (String)iter.next();
        IMAPFolder current = (IMAPFolder)folders.get(key);
        char separator = current.getSeparator();
        key = key.replaceFirst("^" + separator, "");
        String elements[] = key.split(String.valueOf(separator));
        int folderID = 0;
        int parentID = this.getRootFolderId(accountVO.getAccountID(), cvdal);
        for (int i = 0; i < elements.length; i++) {
          if (parentID == 1 && elements[i].equals("INBOX")) {
            continue;
          }
          // get the folderID if it already exists.
          folderID = this.IMAPFolderExists(elements[i], parentID, accountVO.getAccountID(), cvdal);
          if (folderID == 0) {
            MailFolderVO newFolderVO = new MailFolderVO();
            newFolderVO.setFolderName(elements[i]);
            newFolderVO.setFolderType(MailFolderVO.USER_FOLDER_TYPE);
            newFolderVO.setEmailAccountID(accountVO.getAccountID());
            newFolderVO.setParentID(parentID);
            StringBuffer fullName = new StringBuffer("");
            for (int n = i; n >= 0; n--) {
              fullName.insert(0, elements[n] + "/");
            }
            newFolderVO.setFolderFullName(fullName.toString().substring(0, fullName.length()-1));
            folderID = this.addIMAPFolder(newFolderVO, cvdal);
          }
          parentID = folderID;
        }
      }
    } catch(Exception e) {
      logger.error("[processIMAPFolders] Exception thrown.", e);
    }
  }

  /**
   * Checks to see if there is a folder defined in the database for this account
   * with this name and parentID.  If yes it returns the folderId int otherwise
   * it returns 0
   * @param folderName
   * @param parentID
   * @param accountID
   * @param cvdal
   * @return
   */
  private int IMAPFolderExists(String folderName, int parentID, int accountID, CVDal cvdal)
  {
    int folderID = 0;
    try {
      String query =
        "SELECT FolderID FROM emailfolder WHERE Name = ? AND AccountID = ? AND Parent = ?";
      cvdal.setSqlQuery(query);
      cvdal.setString(1, folderName);
      cvdal.setInt(2, accountID);
      cvdal.setInt(3, parentID);
      Collection results = cvdal.executeQuery();
      if (results != null) {
        Iterator iter = results.iterator();
        if (iter.hasNext()) {
          HashMap hm = (HashMap)iter.next();
          folderID = ((Number)hm.get("FolderID")).intValue();
        }
      }
    } catch(Exception e) {
      logger.error("[IMAPFolderExists] Exception thrown.", e);
    } finally {
      cvdal.setSqlQueryToNull();
    }
    return(folderID);
  }

  private int addIMAPFolder(MailFolderVO folderVO, CVDal cvdal)
  {
    int newID;
    String insert = "INSERT INTO emailfolder (Parent, AccountID, Name, Ftype, FullName) VALUES (?, ?, ?, ?, ?)";
    cvdal.setSqlQuery(insert);
    cvdal.setInt(1, folderVO.getParentID());
    cvdal.setInt(2, folderVO.getEmailAccountID());
    cvdal.setString(3, folderVO.getFolderName());
    cvdal.setString(4, folderVO.getFolderType());
    cvdal.setString(5, folderVO.getFolderFullName());
    cvdal.executeUpdate();
    newID = cvdal.getAutoGeneratedKey();
    cvdal.setSqlQueryToNull();
    return(newID);
  }


  private IMAPStore setupIMAPConnection(MailAccountVO accountVO, Session session) throws NoSuchProviderException, MessagingException
  {
    Properties props = new Properties();
    //props.put("mail.debug", "true");
    session = Session.getDefaultInstance(props, null);
    IMAPStore store = (IMAPStore)session.getStore(accountVO.getAccountType());
    store.connect(accountVO.getMailServer(), accountVO.getLogin(), accountVO.getPassword());
    return(store);
  }

  private int getRootFolderId(int accountId, CVDal cvdal)
  {
    int folderID = 0;
    try {
      String query = "SELECT FolderID FROM emailfolder WHERE Name = 'root' AND AccountID = ?";
      cvdal.setSqlQuery(query);
      cvdal.setInt(1, accountId);

      Collection results = cvdal.executeQuery();
      if (results != null) {
        Iterator iter = results.iterator();
        if (iter.hasNext()) {
          HashMap hm = (HashMap)iter.next();
          folderID = ((Number)hm.get("FolderID")).intValue();
        }
      }
    } catch(Exception e) {
      logger.error("[getRootFolderId] Exception thrown.", e);
    } finally {
      cvdal.setSqlQueryToNull();
    }
    return(folderID);
  }
}
