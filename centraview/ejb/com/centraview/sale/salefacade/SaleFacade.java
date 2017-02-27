/*
 * $RCSfile: SaleFacade.java,v $    $Revision: 1.3 $  $Date: 2005/09/13 22:04:29 $ - $Author: mcallist $
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
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.proposal.ProposalListForm;
import com.centraview.sale.proposal.ProposalVO;

/**
 * remote interface for Email
 */
public interface SaleFacade extends EJBObject {

  public int deleteOpportunity(int userid, int elementid) throws RemoteException,
      AuthorizationFailedException;

  public Vector getAllSource() throws RemoteException;

  public Vector getAllStatus() throws RemoteException;

  public Vector getAllStage() throws RemoteException;

  public Vector getAllType() throws RemoteException;

  public Vector getAllProbability() throws RemoteException;

  public Vector getAllTerm() throws RemoteException;

  public int getProbability(int userId, int probabilityID) throws RemoteException,
      AuthorizationFailedException;

  public String getOpportunityID(int activityId) throws RemoteException;

  public int addOpportunity(int userId, OpportunityVO oVO) throws RemoteException,
      AuthorizationFailedException;

  public void updateOpportunity(int userId, OpportunityVO oVO) throws RemoteException,
      AuthorizationFailedException;

  public OpportunityVO getOpportunity(int userId, int opportunityId) throws RemoteException,
      AuthorizationFailedException;

  public HashMap getOpportunityRelatedInfo(int userId, int opportunityId) throws RemoteException;

  public int addProposal(int individualID, ProposalListForm proposallistform) throws Exception,
      RemoteException;

  public boolean deleteProposal(int individualID, int proposalID) throws RemoteException,
      AuthorizationFailedException;

  public boolean updateProposal(int userId, ProposalListForm proposallistform) throws Exception,
      RemoteException;

  public HashMap viewProposal(int individualID, int listid, ProposalListForm proposallistform)
      throws RemoteException, Exception;

  /**
   * Returns Tax in float for a given Tax Class & Jurisdiction
   * @return float tax
   */
  public float getTax(int taxClassId, int taxJurisdictionId) throws RemoteException;

  /**
   * Sets Tax in float for a given Tax Class & Jurisdiction
   * @return void
   */
  public void setTax(int taxClassId, int taxJurisdictionId, float tax) throws RemoteException;

  /**
   * Adds / calculates ItemLines after calculating Tax etc and returns the
   * updated ProposalVO
   * @return ProposalVO
   * @param int
   * @param ProposalVO
   * @param String
   */
  public ProposalVO calculateProposalItems(int userId, ProposalVO proposalVO, String newItemID)
      throws RemoteException;

  public void setOrderIsGenerated(boolean orderIsGenerated, int proposalID, int orderID)
      throws RemoteException;

  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;

  /**
   * This method returns Opportunity Name Of the Opportunity
   * @param OpportunityID The OpportunityID to collect the opportunity Title
   * @return OpportunityName The OpportunityName
   */
  public String getOpportunityName(int OpportunityID) throws RemoteException;

  /**
   * This method returns Proposal Name Of the Proposal
   * @param ProposalID The ProposalID to collect the Proposal Title
   * @return ProposalName The ProposalName
   */
  public String getProposalName(int ProposalID) throws RemoteException;

  /**
   * This method set the JurisdictionID for the Associated Address.
   * @param addressID The addressID is for the Address.
   * @param jurisdictionID The jurisdictionID is for the Address.
   * @return void.
   */
  public void setJurisdictionForAddress(int addressID, int jurisdictionID) throws RemoteException;

  /**
   * This method gets a list of opportunity VOs that meet the following criteria
   * has a due date of a.today b. the past c. no due date, where the status is
   * pending.
   * @param individualId
   * @return array list of opportunity VOs
   */
  public ArrayList getInterestingOpportunityList(int individualId) throws RemoteException;
}
