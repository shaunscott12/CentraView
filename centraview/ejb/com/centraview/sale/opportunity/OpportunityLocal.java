/*
 * $RCSfile: OpportunityLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:05 $ - $Author: mking_cv $
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

package com.centraview.sale.opportunity;

import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;

/**
 *	Used for Add , Edit ,Delete ,Get Opportunity details.
 *	Date 8th Sep 2003
 * @author  Linesh
 */
public interface OpportunityLocal extends EJBLocalObject
{
	public int deleteOpportunity(int userId, int opportunityID) throws AuthorizationFailedException;
	public int addOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException;
	public void updateOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException;
	public OpportunityVO getOpportunity(int userId, int opportunityId) throws AuthorizationFailedException;

	public HashMap getOpportunityRelatedInfo(int userId, int opportunityId) throws AuthorizationFailedException;

	//public void deleteOpportunity(int userId, int opportunityId);
	public int duplicateOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException;
	public void linkOpportunity(int userId,int opportunityId,int tableId, int[] rowIds);
	public float updateForcastAmount(float totalAmount, int opportunityID);
	public float updateTotalAmount(int opportunityID);

	public String getOpportunityID(int activityId);
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);

  /**
   * This method returns Opportunity Name Of the Opportunity
   *
   * @param OpportunityID The OpportunityID to collect the opportunity Title
   *
   * @return OpportunityName The OpportunityName
   */
  public String getOpportunityName(int OpportunityID);
}
