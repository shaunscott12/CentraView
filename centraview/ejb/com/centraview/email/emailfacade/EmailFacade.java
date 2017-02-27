/*
 * $RCSfile: EmailFacade.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:14 $ - $Author: mking_cv $
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


package com.centraview.email.emailfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.email.EmailList;
import com.centraview.email.FolderList;
import com.centraview.email.MailAccount;
import com.centraview.email.MailMessage;
import com.centraview.email.RuleDetails;
import com.centraview.email.RuleList;

/**
 * Remote interface for EmailFacadeBean
 */
public interface EmailFacade extends EJBObject
{
  public boolean checkEmailAccount(int userId) throws RemoteException,Exception;
  public EmailList getEmailList(int userId , HashMap info ) throws RemoteException,Exception;
  public MailMessage getMailMessage( int userId, HashMap preference) throws RemoteException,Exception;
  public MailMessage getAttachment( int userId, HashMap preference) throws RemoteException,Exception;
  public int saveDraft( int userId, MailMessage mailmessage ) throws RemoteException,Exception;
  public int editDraft( int userId, MailMessage mailmessage ) throws RemoteException,Exception;
  public FolderList getFolderList(int userId  ) throws RemoteException,Exception;
  public int addFolder( int userId, HashMap preference) throws RemoteException,Exception;
  public int editFolder( int userId, HashMap preference)  throws RemoteException,Exception;
  public int checkFoldersPresence( int userId, HashMap preference  ) throws RemoteException,Exception;
  public RuleList getRuleList( int userId, HashMap preference ) throws RemoteException,Exception;
  public int deleteRule( int userid , int elementid )  throws RemoteException,AuthorizationFailedException;
  public int enableordisableRule(  int userid , int elementid , boolean status   )  throws RemoteException,Exception;
  public int createNewEmailAccount ( int userId, MailAccount mailaccount )  throws RemoteException,Exception;
  public int editEmailAccount ( int userId, MailAccount mailaccount )  throws RemoteException,Exception;
  public int deleteEmailAccount ( int userId, int AccountID )   throws RemoteException,Exception;
  public MailAccount getAccountDetails( int userid  , HashMap preference )   throws RemoteException,Exception;
  public boolean sendMailMessage( int userId, MailMessage mailmessage ) throws RemoteException,Exception;
  public void mailDeliverMessage( HashMap userId) throws RemoteException,Exception;
  public void sendForgottenPasswordMail(MailMessage mailmessage) throws RemoteException,Exception;
  public int emailMoveTo(int sourceId , int destId, String mailIdList[])throws RemoteException,Exception;
  public int emailDelete(int sourceId , int accountId, String mailIdList[])throws RemoteException,Exception;
  public int emailMarkasRead(int sourceId ,  int readflag, String mailIdList[])throws RemoteException,Exception;
  public int removeFolder(int sourceId , int trashfolderId) throws RemoteException,Exception;
  public RuleDetails getRuleDetails(int ruleid) throws RemoteException,Exception;
  public int addRule(HashMap preference) throws RemoteException,Exception;
	public int editRule(HashMap preference) throws RemoteException,Exception;
	public int deleteRule(HashMap preference) throws RemoteException,Exception;
	public void updateHeader(int userId, int messageId, String hearderName, String headerValue) throws RemoteException, AuthorizationFailedException;
	public int addEmail( HashMap messageData ) throws RemoteException,Exception;
  public boolean editEmailFunction(HashMap editDetails) throws RemoteException, AuthorizationFailedException;
  public int getDefaultAccountID(int individualId)  throws RemoteException,Exception;
  public String getRelatedEmailList(int individualID, int contactID, int contactType) throws RemoteException,AuthorizationFailedException;
  public HashMap getSystemEmailInfo(int systemEmailID) throws RemoteException, Exception;
  public boolean sendMailMessageBasic(MailMessage mailmessage) throws RemoteException, Exception;
  public ArrayList getEmailsFrom(String mailIdList[]) throws RemoteException, Exception;
  public int getRuleId(int accountid) throws RemoteException, Exception;
  public EmailList getRelatedEmailList(HashMap searchCondition, int individualID) throws RemoteException;
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;
}
