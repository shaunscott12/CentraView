/*
 * $RCSfile: SaleFacadeEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/13 22:04:29 $ - $Author: mcallist $
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

package com.centraview.sale.salefacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.helper.CommonHelperLocal;
import com.centraview.common.helper.CommonHelperLocalHome;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.sale.helper.SaleHelperLocal;
import com.centraview.sale.helper.SaleHelperLocalHome;
import com.centraview.sale.opportunity.OpportunityLocal;
import com.centraview.sale.opportunity.OpportunityLocalHome;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.proposal.ProposalHome;
import com.centraview.sale.proposal.ProposalListForm;
import com.centraview.sale.proposal.ProposalLocal;
import com.centraview.sale.proposal.ProposalVO;

/**
 * This class is a Statefull session Bean which acts as a Interface for Sale
 * Module
 */
public class SaleFacadeEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(SaleFacadeEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  /**
   * This method sets the context for this Bean
   */
  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  /**
   * These are life cycle methods from EJB
   */
  public void ejbActivate() throws RemoteException
  {}

  public void ejbPassivate() throws RemoteException
  {}

  public void ejbRemove() throws RemoteException
  {}

  public void ejbCreate()
  {}

  /**
   * @param userId
   * @param elementid
   * @return
   * @exception Deletes the Opportunity
   */
  public int deleteOpportunity(int userid, int elementid) throws AuthorizationFailedException
  {
    int result = 0;
    try {

      // System.out.println("deleteOpportunity---1");
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal local = home.create();
      local.setDataSource(this.dataSource);
      result = local.deleteOpportunity(userid, elementid);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return result;
  }

  /**
   * Description - gets all source
   * @param none
   * @return Vector
   * @exception none
   */

  public Vector getAllSource()
  {
    Vector allSource = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      CommonHelperLocalHome home = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
      CommonHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      allSource = local.getAllSource();
    } catch (Exception e) {
      System.out.println("Failed in getting All Sale Source");
      e.printStackTrace();
    }
    return allSource;
  }

  /**
   * Description - gets all status
   * @param none
   * @return Vector
   * @exception none
   */

  public Vector getAllStatus()
  {
    Vector allStatus = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      allStatus = local.getAllStatus();
    } catch (Exception e) {
      System.out.println("Failed in getting All Sale Status");
      e.printStackTrace();
    }
    return allStatus;
  }

  /**
   * Description - gets all sale stages
   * @param none
   * @return Vector
   * @exception none
   */

  public Vector getAllStage()
  {
    Vector allStage = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      allStage = local.getAllStage();
    } catch (Exception e) {
      System.out.println("Failed in getting All Sale Stage");
      e.printStackTrace();
    }
    return allStage;
  }

  /**
   * Description - gets all sale type
   * @param none
   * @return Vector
   * @exception none
   */

  public Vector getAllType()
  {
    Vector allType = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      allType = local.getAllType();
    } catch (Exception e) {
      System.out.println("Failed in getting All sale type");
      e.printStackTrace();
    }
    return allType;
  }

  /**
   * Description - gets all Probability type
   * @param none
   * @return Vector
   * @exception none
   */

  public Vector getAllProbability()
  {
    Vector allType = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      allType = local.getAllProbability();
    } catch (Exception e) {
      System.out.println("Failed in getting All Probability type");
      e.printStackTrace();
    }
    return allType;
  }

  public int getProbability(int userId, int probabilityID) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", probabilityID,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - getProbability");

    int returnValue = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      returnValue = remote.getProbabilityPercent(userId, probabilityID);
    } catch (Exception e) {
      System.out.println("Failed in getting the probability value: " + e.toString());
      // e.printStackTrace();
    }
    return returnValue;
  }

  public Vector getAllTerm()
  {
    Vector allType = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      allType = local.getAllTerm();
    } catch (Exception e) {
      System.out.println("Failed in getting All Terms");
      e.printStackTrace();
    }
    return allType;
  }

  public int addOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Opportunities", userId, this.dataSource)) {
      throw new AuthorizationFailedException("Opportunities - addOpportunity");
    }

    int rowID = 0;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      rowID = remote.addOpportunity(userId, oVO);
      ctx.getUserTransaction().commit();
    } catch (Exception e) {
      System.out.println("Failed in SaleFacade adding Opportunity");
      e.printStackTrace();
    }
    return rowID;
  }

  public void updateOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", oVO.getOpportunityID(),
        ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - updateOpportunity");

    try {
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      ctx.getUserTransaction().begin();
      remote.updateOpportunity(userId, oVO);
      ctx.getUserTransaction().commit();

    } catch (Exception e) {
      System.out.println("Failed in SaleFacade adding Opportunity");
      e.printStackTrace();
    }
  }

  public HashMap getOpportunityRelatedInfo(int userId, int opportunityId)
  {
    HashMap opportunityRelatedInfo = new HashMap();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      opportunityRelatedInfo = remote.getOpportunityRelatedInfo(userId, opportunityId);
    } catch (Exception e) {
      System.out.println("Failed in SaleFacade geting Opportunity Related Info");
      e.printStackTrace();
    }
    return opportunityRelatedInfo;
  }

  public String getOpportunityID(int activityId)
  {
    String opportunityID = "0";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      opportunityID = remote.getOpportunityID(activityId);
    } catch (Exception e) {
      System.out.println("Failed in SaleFacade geting Opportunity ID when we pass ActivityID ");
      e.printStackTrace();
    }
    return opportunityID;
  }

  public OpportunityVO getOpportunity(int userId, int opportunityId)
      throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", opportunityId,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - getOpportunity");

    OpportunityVO oVO = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      oVO = remote.getOpportunity(userId, opportunityId);

    } catch (Exception e) {
      System.out.println("Failed in SaleFacade geting Opportunity");
      e.printStackTrace();
    }
    return oVO;
  }

  public int addProposal(int individualID, ProposalListForm proposallistform) throws Exception
  {
    if (!CVUtility.isModuleVisible("Opportunities", individualID, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - addOpportunity");

    int listid = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      listid = remote.addProposal(individualID, proposallistform);
    } catch (Exception e) {
      System.out.println("Failed in getting EmailList");
      e.printStackTrace();
      return 0;
    }
    return listid;
  }

  public boolean deleteProposal(int individualID, int proposalID)
      throws AuthorizationFailedException
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.deleteProposal(individualID, proposalID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return true;
  }

  public boolean updateProposal(int individualID, ProposalListForm proposallistform)
      throws Exception
  {
    if (!CVUtility.canPerformRecordOperation(individualID, "Proposals", Integer
        .parseInt(proposallistform.getProposalid()), ModuleFieldRightMatrix.UPDATE_RIGHT,
        this.dataSource))
      throw new AuthorizationFailedException("Proposals - updateProposal");

    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.updateProposal(individualID, proposallistform);
    } catch (Exception e) {
      System.out.println("[Exception] SalesFacadeEJB.updateProposal: " + e.toString());
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public HashMap viewProposal(int individualID, int listid, ProposalListForm info) throws Exception
  {
    if (!CVUtility.isModuleVisible("Proposals", individualID, this.dataSource))
      throw new AuthorizationFailedException("Proposals - viewProposal");

    HashMap dyna = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      dyna = remote.viewProposal(individualID, listid, info);

    } catch (Exception e) {
      System.out.println("[Exception] SalesFacadeEJB.viewProposal: " + e.toString());
      e.printStackTrace();
      // return dyna;
    }
    return dyna;
  }

  // added by shilpa
  /**
   * Returns Tax in float for a given Tax Class & Jurisdiction
   * @return float tax
   */
  public float getTax(int taxClassId, int taxJurisdictionId)
  {
    float tax = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      tax = local.getTax(taxClassId, taxJurisdictionId);
    } catch (Exception e) {
      System.out.println("Failed in getting tax");
      e.printStackTrace();
    }
    return tax;
  }

  /**
   * Sets Tax in float for a given Tax Class & Jurisdiction
   * @return void
   */
  public void setTax(int taxClassId, int taxJurisdictionId, float tax)
  {
    SaleHelperLocal local = null;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      local = home.create();
      local.setDataSource(this.dataSource);
      local.setTax(taxClassId, taxJurisdictionId, tax);
    } catch (Exception e) {
      System.out.println("Failed in getting tax");
      e.printStackTrace();
    } finally {
      local.setTax(taxClassId, taxJurisdictionId, 0.0f);
    }
  }

  /**
   * Adds / calculates ItemLines after calculating Tax etc and returns the
   * updated ProposalVO
   * @return ProposalVO
   * @param int
   * @param ProposalVO
   * @param String
   */

  public ProposalVO calculateProposalItems(int userId, ProposalVO proposalVO, String newItemID)
  {
    ProposalVO vo = new ProposalVO();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      SaleHelperLocalHome home = (SaleHelperLocalHome)ic.lookup("local/SaleHelper");
      SaleHelperLocal local = home.create();
      local.setDataSource(this.dataSource);
      vo = local.calculateProposalItems(userId, proposalVO, newItemID);
    } catch (Exception e) {
      System.out.println("Failed in getting tax");
      e.printStackTrace();
    } finally {
      vo = proposalVO;
    }

    return vo;

  }

  // added by shilpa ends here

  // added by sandeepj - for setting orderGenerated Flag in Proposal
  public void setOrderIsGenerated(boolean orderIsGenerated, int proposalID, int orderID)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.setOrderIsGenerated(orderIsGenerated, proposalID, orderID);
    } catch (Exception e) {
      System.out.println("Errorin Salefacade in setOrderIsGenerated...:" + e);
      e.printStackTrace();
    }
  }

  // end of added by sandeepj
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
   * This method returns Opportunity Name Of the Opportunity
   * @param OpportunityID The OpportunityID to collect the opportunity Title
   * @return OpportunityName The OpportunityName
   */
  public String getOpportunityName(int OpportunityID)
  {
    String opportunityName = "";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
      OpportunityLocal local = home.create();
      local.setDataSource(this.dataSource);
      opportunityName = local.getOpportunityName(OpportunityID);
    } catch (Exception exe) {
      exe.printStackTrace();
    }
    return opportunityName;
  }

  /**
   * This method returns Proposal Name Of the Proposal
   * @param ProposalID The ProposalID to collect the Proposal Title
   * @return ProposalName The ProposalName
   */
  public String getProposalName(int ProposalID)
  {
    String proposalName = "";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
      ProposalLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      proposalName = remote.getProposalName(ProposalID);
    } catch (Exception e) {
      System.out.println("Errorin Salefacade in setOrderIsGenerated...:" + e);
      e.printStackTrace();
    }
    return proposalName;
  }

  /**
   * This method set the JurisdictionID for the Associated Address.
   * @param addressID The addressID is for the Address.
   * @param jurisdictionID The jurisdictionID is for the Address.
   * @return void.
   */
  public void setJurisdictionForAddress(int addressID, int jurisdictionID)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.setJurisdictionForAddress(addressID, jurisdictionID);
    } catch (Exception e) {
      System.out
          .println("[Exception][AccountFacadeEJB.setJurisdictionForAddress] Exception Thrown: " + e);
      // e.printStackTrace();
    }
  }

  /**
   * This method gets a list of opportunity VOs that meet the following criteria
   * has a due date of a.today b. the past c. no due date, where the status is
   * pending.
   * @param individualId
   * @return array list of opportunity VOs
   */
  public ArrayList getInterestingOpportunityList(int individualId)
  {
    ArrayList opportunityList = new ArrayList();
    // Run a simple Query to get the IDs of all the interesting opportunites
    // that are assigned to the individual
    StringBuffer query = new StringBuffer();
    // note:estimated close date in opportunity is saved in activity.start
    // column
    query
        .append("SELECT a.activityId AS oppId FROM activity AS a, opportunity AS o, salesstatus AS s ");
    query.append("WHERE o.activityId = a.activityId AND o.status = s.salesstatusid ");
    query.append("AND s.name='Pending' AND a.owner = ? ");
    query.append("AND (a.start <= NOW() OR a.start IS NULL)");
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      cvdal.setSqlQuery(query.toString());
      cvdal.setInt(1, individualId);
      Collection result = cvdal.executeQuery();
      if (result != null) {
        // if we have any results from the query set up the connection
        // to the local Opportunity session EJB
        InitialContext ctx = new InitialContext();
        OpportunityLocalHome opportunityHome = (OpportunityLocalHome)ctx
            .lookup("local/Opportunity");
        OpportunityLocal opportunityBean = opportunityHome.create();
        opportunityBean.setDataSource(dataSource);
        // the iterate through the results of the query grabbing an
        // OpportunityVO for each id.
        Iterator i = result.iterator();
        while (i.hasNext()) {
          HashMap resultRow = (HashMap)i.next();
          Number opportunityId = (Number)resultRow.get("oppId");
          try {
            OpportunityVO opportunity = opportunityBean.getOpportunity(individualId, opportunityId
                .intValue());
            opportunityList.add(opportunity);
          } catch (AuthorizationFailedException afe) {
            // no reason to completely fail here, it will just be one less thing
            // on our list. dump it in the logs, because it is indicative of an
            // underlying problem
            logger.warn("[getTaskList]: Tried to get unautorized Opportunity, continuing.");
          }
        }
      }
    } catch (Exception e) {
      logger.error("[getTaskList]: Exception", e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }
    return opportunityList;
  }
}
