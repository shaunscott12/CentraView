/*
 * $RCSfile: SupportFacade.java,v $    $Revision: 1.4 $  $Date: 2005/09/13 21:56:22 $ - $Author: mcallist $
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

package com.centraview.support.supportfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.support.faq.FAQList;
import com.centraview.support.faq.FaqException;
import com.centraview.support.faq.FaqVO;
import com.centraview.support.faq.QuestionList;
import com.centraview.support.faq.QuestionVO;
import com.centraview.support.knowledgebase.CategoryVO;
import com.centraview.support.knowledgebase.KBException;
import com.centraview.support.knowledgebase.KnowledgeVO;
import com.centraview.support.knowledgebase.KnowledgebaseList;
import com.centraview.support.thread.ThreadList;
import com.centraview.support.ticket.ThreadVO;
import com.centraview.support.ticket.TicketException;
import com.centraview.support.ticket.TicketList;
import com.centraview.support.ticket.TicketVO;

public interface SupportFacade extends EJBObject {
  public int addTicket(int userId, TicketVO tv) throws RemoteException;

  public TicketVO getTicketBasicRelations(int userId, int ticketId) throws RemoteException,
      AuthorizationFailedException, TicketException;

  public void updateTicket(int userId, TicketVO tv) throws RemoteException;

  public void delete(int userId, int ticketID) throws RemoteException, AuthorizationFailedException;

  public ThreadVO getThread(int userId, int threadId) throws TicketException, RemoteException;

  public TicketList getTicketList(int userID, HashMap hashmap) throws RemoteException;

  public FAQList getFAQList(int userID, HashMap hashmap) throws RemoteException;

  public Vector getStatusList() throws RemoteException;

  public Vector getPriorityList() throws RemoteException;

  public void duplicateTicket(int userId, TicketVO tv) throws RemoteException;

  public int addThread(int userId, ThreadVO thvo) throws TicketException, RemoteException;

  public TicketVO getTicketBasic(int userId, int ticketId) throws TicketException, RemoteException;

  public ThreadList getThreadList(int userID, int curTicketID) throws RemoteException;

  public QuestionList getQuestionList(int userID, int faqID) throws RemoteException;

  public KnowledgebaseList getKnowledgebaseList(int userID, int curCategoryID, HashMap hashmap)
      throws RemoteException;

  public int insertCategory(int userId, CategoryVO catinfo) throws RemoteException;

  public void updateCategory(int userId, CategoryVO catinfo) throws RemoteException,
      javax.transaction.SystemException, KBException;

  public int deleteCategory(int userId, int categoryId) throws RemoteException;

  public CategoryVO getCategory(int userId, int catId) throws RemoteException;

  public ArrayList getAllCategory(int userId) throws RemoteException;

  public void deleteAllKB(int userId) throws RemoteException;

  public void deleteKB(int userId, int knId) throws RemoteException;

  public void addFile(int ticketId, int[] fileId) throws RemoteException;

  public void addExpense(int ticketId, int[] expenseId) throws RemoteException;

  public void closeTicket(int userId, int ticketId) throws RemoteException, TicketException;

  public void reopenTicket(int userId, int ticketId) throws RemoteException, TicketException;

  public KnowledgeVO getKB(int userId, int kbId) throws RemoteException;

  public int insertKB(int userId, KnowledgeVO kbinfo) throws RemoteException;

  public void updateKB(int userId, KnowledgeVO kbinfo) throws RemoteException;

  public int duplicateCategory(int userID, CategoryVO catVO) throws RemoteException;

  public int addFaq(int userId, FaqVO fvo) throws RemoteException, FaqException;

  public FaqVO getFaq(int userId, int faqId) throws RemoteException, FaqException;

  public void updateFaq(int userId, FaqVO fvo) throws RemoteException, FaqException;

  public void deleteFaq(int userId, int faqId) throws RemoteException, FaqException;

  public int duplicateFaq(int userId, FaqVO fvo) throws RemoteException, FaqException;

  public int addQuestion(int userId, QuestionVO qvo) throws RemoteException, FaqException;

  public void updateQuestion(int userId, QuestionVO qvo) throws RemoteException, FaqException;

  public QuestionVO getQuestion(int userId, int questionId) throws RemoteException, FaqException;

  public void deleteQuestion(int userId, int questionId) throws RemoteException, FaqException;

  public Vector getCategoryRootPath(int userID, int categoryID) throws RemoteException;

  /**
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public void setDataSource(String ds) throws RemoteException;

  /**
   * Returns String representation of the Ticket Name for the given
   * <code>TicketID</code>
   * @param TicketID The TicketID to collect the Ticket Title
   * @return String representation of the Ticket Name
   */
  public String getTicketName(int TicketID) throws RemoteException;

}
