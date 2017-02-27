/*
 * $RCSfile: SupportFacadeEJB.java,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:05:46 $ - $Author: mcallist $
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

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.support.faq.FAQList;
import com.centraview.support.faq.FaqLocal;
import com.centraview.support.faq.FaqLocalHome;
import com.centraview.support.faq.FaqVO;
import com.centraview.support.faq.QuestionList;
import com.centraview.support.faq.QuestionVO;
import com.centraview.support.helper.SupportHelperLocal;
import com.centraview.support.helper.SupportHelperLocalHome;
import com.centraview.support.knowledgebase.CategoryVO;
import com.centraview.support.knowledgebase.KBException;
import com.centraview.support.knowledgebase.KnowledgeBaseLocal;
import com.centraview.support.knowledgebase.KnowledgeBaseLocalHome;
import com.centraview.support.knowledgebase.KnowledgeVO;
import com.centraview.support.knowledgebase.KnowledgebaseList;
import com.centraview.support.supportlist.SupportListLocal;
import com.centraview.support.supportlist.SupportListLocalHome;
import com.centraview.support.thread.ThreadList;
import com.centraview.support.ticket.ThreadVO;
import com.centraview.support.ticket.TicketException;
import com.centraview.support.ticket.TicketList;
import com.centraview.support.ticket.TicketLocal;
import com.centraview.support.ticket.TicketLocalHome;
import com.centraview.support.ticket.TicketVO;

public class SupportFacadeEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(SupportFacadeEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "";

  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  public void ejbActivate() throws RemoteException
  {}

  public void ejbPassivate() throws RemoteException
  {}

  public void ejbRemove() throws RemoteException
  {}

  public void ejbCreate()
  {}

  public void duplicateTicket(int userId, TicketVO tv)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      remote.duplicateTicket(userId, tv);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[duplicateTicket]: Exception", e);
    }
  }

  public int addTicket(int userId, TicketVO tv)
  {
    int tktID = 0;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      tktID = remote.addTicket(userId, tv);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[addTicket]: Exception", e);
    }
    return tktID;
  }

  public int addThread(int userId, ThreadVO thvo)
  {
    int returnCode = 1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.addThread(userId, thvo);
      returnCode = 0;
    } catch (Exception e) {
      logger.error("[addThread]: Exception", e);
    }
    return returnCode;
  }

  public ThreadVO getThread(int userId, int threadId)
  {
    ThreadVO thVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      ctx.getUserTransaction().begin();
      remote.setDataSource(this.dataSource);
      thVO = remote.getThread(userId, threadId);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[getThread]: Exception", e);
    }
    return thVO;
  }

  public TicketVO getTicketBasicRelations(int userId, int ticketId) throws TicketException, AuthorizationFailedException
  {
    TicketVO tv = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      tv = remote.getTicketBasicRelations(userId, ticketId);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return tv;
  }

  public void updateTicket(int userId, TicketVO tv)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      remote.updateTicket(userId, tv);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[updateTicket]: Exception", e);
    }
  }

  public TicketVO getTicketBasic(int userId, int ticketId)
  {
    TicketVO tv = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      tv = remote.getTicketBasic(userId, ticketId);
    } catch (Exception e) {
      logger.error("[getTicketBasic]: Exception", e);
    }
    return tv;
  }

  public void delete(int userId, int ticketID) throws AuthorizationFailedException
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.deleteTicket(userId, ticketID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
  }

  public TicketList getTicketList(int userID, HashMap hashmap)
  {
    TicketList ticketlist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportListLocalHome home = (SupportListLocalHome) ic.lookup("local/SupportList");
      SupportListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ticketlist = remote.getTicketList(userID, hashmap);
    } catch (Exception e) {
      logger.error("[getTicketList]: Exception", e);
    }
    return ticketlist;
  }

  public FAQList getFAQList(int userID, HashMap hashmap)
  {
    FAQList faqlist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportListLocalHome home = (SupportListLocalHome) ic.lookup("local/SupportList");
      SupportListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      faqlist = remote.getFAQList(userID, hashmap);
    } catch (Exception e) {
      logger.error("[getFAQList]: Exception", e);
    }
    return faqlist;
  }

  public ThreadList getThreadList(int userID, int curTicketID)
  {
    ThreadList threadlist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportListLocalHome home = (SupportListLocalHome) ic.lookup("local/SupportList");
     SupportListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      threadlist = remote.getThreadList(userID, curTicketID);
    } catch (Exception e) {
      logger.error("[getThreadList]: Exception", e);
    }
    return threadlist;
  }

  public QuestionList getQuestionList(int userID, int curFaqID)
  {
    QuestionList questionList = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportListLocalHome home = (SupportListLocalHome) ic.lookup("local/SupportList");
      SupportListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      questionList = remote.getQuestionList(userID, curFaqID);
    } catch (Exception e) {
      logger.error("[getQuestionList]: Exception", e);
    }
    return questionList;
  }

  public KnowledgebaseList getKnowledgebaseList(int userID, int curCategoryID, HashMap hashmap)
  {
    KnowledgebaseList kblist = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportListLocalHome home = (SupportListLocalHome) ic.lookup("local/SupportList");
      SupportListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      kblist = remote.getKnowledgebaseList(userID, curCategoryID, hashmap);
    } catch (Exception e) {
      logger.error("[getKnowledgebaseList]: Exception", e);
    }
    return kblist;
  }

  public Vector getStatusList()
  {
    Vector vec = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportHelperLocalHome home = (SupportHelperLocalHome) ic.lookup("local/SupportHelper");
      SupportHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      vec = remote.getAllStatus();
    } catch (Exception e) {
      logger.error("[getStatusList]: Exception", e);
    }
    return vec;
  }

  public Vector getPriorityList()
  {
    Vector vec = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportHelperLocalHome home = (SupportHelperLocalHome) ic.lookup("local/SupportHelper");
      SupportHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      vec = remote.getAllPriorities();
    } catch (Exception e) {
      logger.error("[getPriorityList]: Exception", e);
    }
    return vec;
  }

  public int insertCategory(int userId, CategoryVO catinfo)
  {
    int cat = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      cat = remote.insertCategory(userId, catinfo);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[insertCategory]: Exception", e);
    }
    return cat;
  }

  public void updateCategory(int userId, CategoryVO catinfo) throws KBException
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.updateCategory(userId, catinfo);
    } catch (KBException kbe) {
      throw kbe;
    } catch (Exception e) {
      logger.error("[updateCategory]: Exception", e);
    }
  }

  public int deleteCategory(int userId, int categoryId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      remote.deleteCategory(userId, categoryId);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[deleteCategory]: Exception", e);
      return 0;
    }
    return 1;
  }

  public CategoryVO getCategory(int userId, int catId)
  {
    CategoryVO catVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      catVO = remote.getCategory(userId, catId);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[getCategory]: Exception", e);
    }
    return catVO;
  }

  public ArrayList getAllCategory(int userId)
  {
    ArrayList categoryList = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal local = home.create();
      local.setDataSource(this.dataSource);
      categoryList = local.getAllCategory(userId);
    } catch (Exception e) {
      logger.error("[getAllCategory]: Exception", e);
    }
    return categoryList;
  }

  public void deleteAllKB(int userId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal local = home.create();
      local.setDataSource(this.dataSource);
      local.deleteAllKB(userId);
    } catch (Exception e) {
      logger.error("[deleteAllKB]: Exception", e);
    }

  }

  public void deleteKB(int userId, int knId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal local = home.create();
      local.setDataSource(this.dataSource);
      local.deleteKB(userId, knId);
    } catch (Exception e) {
      logger.error("[deleteKB]: Exception", e);
    }
  }

  public void addFile(int ticketId, int[] fileId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.addTicketLink(ticketId, fileId, 33);
    } catch (Exception e) {
      logger.error("[addFile]: Exception", e);
    }
  }

  public void addExpense(int ticketId, int[] expenseId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.addTicketLink(ticketId, expenseId, 89);
    } catch (Exception e) {
      logger.error("[addExpense]: Exception", e);
    }
  }

  public void closeTicket(int userId, int ticketId)
  {
    try {

      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.closeTicket(userId, ticketId);
    } catch (Exception e) {
      logger.error("[closeTicket]: Exception", e);
    }
  }

  public void reopenTicket(int userId, int ticketId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.reopenTicket(userId, ticketId);
    } catch (Exception e) {
      logger.error("[reopenTicket]: Exception", e);    
    }
  }

  public KnowledgeVO getKB(int userId, int kbId)
  {
    KnowledgeVO kVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      kVO = remote.getKB(userId, kbId);
    } catch (Exception e) {
      logger.error("[getKB]: Exception", e);
    }
    return kVO;
  }

  public int insertKB(int userId, KnowledgeVO kbinfo)
  {
    int kbid = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      kbid = remote.insertKB(userId, kbinfo);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[insertKB]: Exception", e);
      return 1;
    }
    return kbid;
  }

  public void updateKB(int userId, KnowledgeVO kbinfo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      remote.updateKB(userId, kbinfo);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      logger.error("[updateKB]: Exception", e);
    }
  }

  /**
   * ******Creates a duplicate category with all its subcategory and
   * knowledgebase
   * @param userID int
   * @param curCategoryID - to be dulicated int
   */
  public int duplicateCategory(int userID, CategoryVO catVO)
  {
    int catId = -1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      KnowledgeBaseLocalHome home = (KnowledgeBaseLocalHome) ic.lookup("local/KnowledgeBase");
      KnowledgeBaseLocal local = home.create();
      local.setDataSource(this.dataSource);
      // current category's parent is not to be changed so the parent parameter
      // is -1
      catId = local.duplicateCategory(userID, catVO);
    } catch (Exception e) {
      logger.error("[duplicateCategory]: Exception", e);
    }
    return catId;
  }

  public int addFaq(int userId, FaqVO fvo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      return remote.addFaq(userId, fvo);
    } catch (Exception e) {
      logger.error("[addFaq]: Exception", e);
    }
    return 0;
  }

  public FaqVO getFaq(int userId, int faqId)
  {
    FaqVO faqVo = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      faqVo = remote.getFaq(userId, faqId);
    } catch (Exception e) {
      logger.error("[getFaq]: Exception", e);
    }
    return faqVo;
  }

  public void updateFaq(int userId, FaqVO fvo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.updateFaq(userId, fvo);
    } catch (Exception e) {
      logger.error("[updateFaq]: Exception", e);
    }
  }

  public void deleteFaq(int userId, int faqId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.deleteFaq(userId, faqId);
    } catch (Exception e) {
      logger.error("[deleteFaq]: Exception", e);
    }
  }

  public int duplicateFaq(int userId, FaqVO fvo)
  {
    int faqId = -1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      faqId = remote.duplicateFaq(userId, fvo);
    } catch (Exception e) {
      logger.error("[duplicateFaq]: Exception", e);
    }
    return faqId;
  }

  public int addQuestion(int userId, QuestionVO qvo)
  {
    int qId = -1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal local = home.create();
      local.setDataSource(this.dataSource);
      qId = local.addQuestion(userId, qvo);
    } catch (Exception e) {
      logger.error("[addQuestion]: Exception", e);
    }
    return qId;
  }

  public void updateQuestion(int userId, QuestionVO qvo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal local = home.create();
      local.setDataSource(this.dataSource);
      local.updateQuestion(userId, qvo);
    } catch (Exception e) {
      logger.error("[updateQuestion]: Exception", e);
    }
  }

  public QuestionVO getQuestion(int userId, int questionId)
  {
    QuestionVO qvo = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal local = home.create();
      local.setDataSource(this.dataSource);
      qvo = local.getQuestion(userId, questionId);
    } catch (Exception e) {
      logger.error("[getQuestion]: Exception", e);
    }
    return qvo;
  }

  public void deleteQuestion(int userId, int questionId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      FaqLocalHome home = (FaqLocalHome) ic.lookup("local/Faq");
      FaqLocal local = home.create();
      local.setDataSource(this.dataSource);
      local.deleteQuestion(userId, questionId);
    } catch (Exception e) {
      logger.error("[deleteQuestion]: Exception", e);
    }
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * This method returns Ticket Name Of the Ticket
   * @param TicketID The TicketID to collect the Ticket Title
   * @return TicketName The TicketName
   */
  public String getTicketName(int TicketID)
  {
    String ticketSubject = "";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TicketLocalHome home = (TicketLocalHome) ic.lookup("local/Ticket");
      TicketLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ticketSubject = remote.getTicketName(TicketID);
    } catch (Exception e) {
      logger.error("[getTicketName]: Exception", e);
    }
    return ticketSubject;
  }

  public Vector getCategoryRootPath(int userID, int categoryID)
  {
    Vector path = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SupportListLocalHome home = (SupportListLocalHome) ic.lookup("local/SupportList");
      SupportListLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      path = remote.getCategoryRootPath(userID, categoryID);
    } catch (Exception e) {
      logger.error("[getCategoryRootPath]: Exception", e);
    }
    return path;
  }
}
