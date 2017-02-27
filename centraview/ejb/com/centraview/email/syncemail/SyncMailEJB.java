/*
 * $RCSfile: SyncMailEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:19 $ - $Author: mking_cv $
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


package com.centraview.email.syncemail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.email.mailDeliver.MailDeliverLocal;
import com.centraview.email.mailDeliver.MailDeliverLocalHome;


/**
 * Used by the CompanionLink Sync API, this is EJB takes data from the
 * EmailFacade and manipulates email data.
 */



public class SyncMailEJB implements SessionBean
{
  private SessionContext sessionCtx;
	private String dataSource = "MySqlDS";  
  public void ejbCreate()
  {
  }
  
  public void ejbLoad()
  {
  }
  
  public void ejbRemove()
  {
  }
  
  public void ejbStore()
  {
  }
  
  public void setSessionContext(SessionContext context)
  {
    sessionCtx = context;
  }
  
  public void unsetSessionContext()
  {
    sessionCtx = null;
  }
  
  public void ejbActivate()
  {
  }
  
  public void ejbPassivate()
  {
  }


  /**
   * Creates a new mail message in the database. 
   * @param messageMap A HashMap containing the details of the email message
   * @return int: eventually, this will return the new email messageID.
   */	
  public int addEmail( HashMap messageMap )
  {
    try
    {
      Properties props = System.getProperties();
// DEBUG - THIS NEEDS TO BE FIXED! vvvvvvvvvvvvvvvvvvvvv
System.out.println("\n\n\n\n");
System.out.println("  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
System.out.println("  !! A HARDCODED mail.smtp.host value is  !!\n");
System.out.println("  !! being used in SyncMailEJB.addEmail() !!\n");
System.out.println("  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
System.out.println("\n\n\n\n");
      props.put("mail.smtp.host","x");
// DEBUG - THIS NEEDS TO BE FIXED! ^^^^^^^^^^^^^^^^^^^^^
      Session session = Session.getDefaultInstance(props, null);
      
      byte b[] = (byte[])messageMap.get("inputstream");
      InputStream fis = new ByteArrayInputStream( b );
      
      MimeMessage messagefile = new MimeMessage( session , fis );
      Message mm [] = new Message[1];
      mm[0] = messagefile;
      messageMap.put( "message" , mm );
      
      InitialContext ic			= CVUtility.getInitialContext();
      MailDeliverLocalHome home	= (MailDeliverLocalHome)ic.lookup("local/MailDeliver");
      MailDeliverLocal remote		= home.create();
      remote.setDataSource(this.dataSource);
      // add message to db
      int newMessageID = remote.mailDeliverMessage( messageMap );
      return(newMessageID);
    }catch(Exception e){
      System.out.println("[SyncEmailEJB] Failed in sending map to Delivery bean:" + e);
      //e.printStackTrace();
    }
    return(0);
  }    // end addEmail() method
  
  
  /**
   * Edits an existing mail message in the database. 
   * @param editDetails A HashMap containing the details of the email message
   * @return boolean: true for success, false for failure
   */	
  public boolean editEmailFunction(HashMap editDetails)
  {
    try
		{
      int accountID = Integer.parseInt((String)editDetails.get("AccountID"));
      int messageID = Integer.parseInt( (String) editDetails.get( "MessageID"  ) );
      int userID = ((Long)editDetails.get("owner")).intValue();
      int read =  ((Integer)editDetails.get("readFlag")).intValue();
      String folder = (String)editDetails.get("folder");
      
      CVDal cvdl = new CVDal(dataSource);
      
      // update the "read" status of the message
      if (read == 1)
      {
        // mark message as "read"
        cvdl.setSqlQuery("UPDATE emailstore SET ReadStatus='YES' WHERE messageid=?");
      }else{
        // mark message as "unread"
        cvdl.setSqlQuery("UPDATE emailstore SET ReadStatus='NO' WHERE messageid=?");
      }
      cvdl.setInt(1, messageID);
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.destroy();
      // end set read status

      // move message into a different folder
      if (folder != null && ! folder.equals(""))
      {
        boolean moveResult = this.moveMessage(messageID, accountID, folder);
        if (moveResult != true)
        {
          // we failed
          return(false);
        }
      }
    }catch(Exception  e){
      System.out.println("[SyncEmailEJB] Exception thrown: " + e);
      //e.printStackTrace() ;
      return(false);
    }
    return(true);
  }   // end editEmailFunction() method


  /**
   * Moves a given email message to a give folder. The message to be moved is passed
   * as a messageID. The destination folder is passed as a string in the form
   * /My Folders/Personal/Jokes. (No trailing "/"!!!). 
   *
   * @param messageID   int: the message which is to be moved.
   * @param accountID   int: the accountID of the message which is to be moved.
   * @param folderPath  String: the folder to which the message should be moved.
   * @return boolean: true for success, false for failure
   */
  private boolean moveMessage(int messageID, int accountID, String folderPath)
  {
    // we need to get the folderID of the folderPath given to us from CompanionLink
    // matchFolderPath is a recursive method that will loop through that user's list of
    // folders and find a match for the folderPath parameter
    int destinationFolderID = this.matchFolderPath(accountID, 0, "", folderPath);

    // now update the database
    CVDal cvdl = new CVDal(dataSource);
    try
    {
      cvdl.setSqlQuery("UPDATE emailstore set EmailFolder=? WHERE MessageID=?");
      cvdl.setInt(1, destinationFolderID);
      cvdl.setInt(2, messageID);
      cvdl.executeUpdate();
    }catch(Exception e){
      System.out.println("[SyncMailEJB] Exception thrown in moveMessage(): " + e);
      return(false);
    }finally{
      cvdl.clearParameters();
      cvdl.destroy();
    }
    return(true);
  }   // end moveMessage() method


  /**
   * This recursive method loops through the user's list of email folders and
   * matches a given folder path in order to return a folderID of the match.
   *
   * @param accountID     int: the user's email accountID
   * @param parentID      int: the parentID of the folder list to get on this iterationi
   * @param currentPath   String: the current folder path string being built by this recursive method
   * @param pathToMatch   String: the folder path we need to match to get the folderID for
   * @return  int: the folderID which matches the pathToMatch or 0 for failure
   */
  private int matchFolderPath(int accountID, int parentID, String currentPath, String pathToMatch)
  {
    CVDal cvdl = new CVDal(dataSource);
    
    cvdl.setSqlQuery("SELECT FolderID, Name FROM emailfolder WHERE AccountID=? AND Parent=?");
    cvdl.setInt(1, accountID);
    cvdl.setInt(2, parentID);
    Collection sqlResults = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.destroy();
    
    Iterator iter = sqlResults.iterator();

    while (iter.hasNext())
    {
      HashMap folderInfo = (HashMap)iter.next();
      int folderID = ((Long)folderInfo.get("FolderID")).intValue();
      String folderName = (String)folderInfo.get("Name");

      currentPath = currentPath + "/" + folderName;

      if (currentPath.equalsIgnoreCase(pathToMatch))
      {
        // we've found the match, return the folderID
       return(folderID);
      }else{
        // doesn't match, continue looping recursively.
        return(this.matchFolderPath(accountID, folderID, currentPath, pathToMatch));
      }
    }
    // if this point was reached, then we faled. Return 0.
    return(0);
  }   // end matchFolderPath

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

}   // end class definition
