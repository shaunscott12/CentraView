/*
 * $RCSfile: ProposalLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:07 $ - $Author: mking_cv $
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

package com.centraview.sale.proposal;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;

/**
 * Local Interface for the Proposal EJB.
 *
 * @author  shirish d
 */
public interface ProposalLocal extends EJBLocalObject
{
  public int addProposal(int individualID, ProposalListForm proposallistform) throws AuthorizationFailedException;

  public boolean deleteProposal(int userId) throws AuthorizationFailedException;

  public boolean updateProposal(int userId, ProposalListForm proposallistform) throws AuthorizationFailedException;

  public java.util.HashMap viewProposal(int individualID, int listid, ProposalListForm proposallistform) throws AuthorizationFailedException;

  public int deleteProposal(int userId, int proposalID)throws AuthorizationFailedException;

  public int getProbabilityPercent(int userId, int probability);
  //added by sandeepj
  public void setOrderIsGenerated(boolean orderIsGenerated,int proposalID,int orderID);
  //end of added by sandeepj
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);

  /**
   * This method returns Proposal Name Of the Proposal
   *
   * @param ProposalID The ProposalID to collect the Proposal Title
   *
   * @return ProposalName The ProposalName
   */
  public String getProposalName(int ProposalID);
}
