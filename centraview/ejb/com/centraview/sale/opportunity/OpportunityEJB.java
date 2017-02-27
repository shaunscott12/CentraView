/*
 * $RCSfile: OpportunityEJB.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.activity.helper.ActivityVO;
import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.helper.CommonHelperLocal;
import com.centraview.common.helper.CommonHelperLocalHome;

/*
 * This Ejb is Staleless SessionBean Used for Add , Edit ,Delete ,Get
 * Opportunity details. Date 8th Sep 2003 @author Linesh
 * @version 1.0
 */

public class OpportunityEJB implements SessionBean {
  protected SessionContext context;
  protected Context ctx;
  private String dataSource = "";
  private static Logger logger = Logger.getLogger(OpportunityEJB.class);

  /**
   * This method sets the context for this Bean
   */
  public void setSessionContext(SessionContext context)
  {
    this.context = context;
  }

  public void ejbPassivate()
  {}

  public void ejbActivate()
  {}

  public void ejbRemove()
  {}

  public void ejbCreate()
  {}

  /*
   * Used to deleteOpportunity @param opportunityID int @return int
   */
  public int deleteOpportunity(int userId, int opportunityID) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", opportunityID,
        ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - deleteOpportunity");

    int result = 0;

    CVDal cvdal = new CVDal(dataSource);
    try {

      cvdal.setSql("sale.opportunity.hasproposal");
      cvdal.setInt(1, opportunityID);
      Collection col = cvdal.executeQuery();

      if (col != null && col.iterator().hasNext()) {
        cvdal.destroy();
        cvdal = null;
        result = -2;
        return -2;

      }

      int activityid = 0;

      cvdal.setSql("sale.getactivityidfromopportunityid");
      cvdal.setInt(1, opportunityID);
      Collection v = cvdal.executeQuery();
      cvdal.clearParameters();

      Iterator it = v.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();

        activityid = ((Long)hm.get("activityid")).intValue();
        break;
      }

      cvdal.setSql("sale.deleteassociatedopportunitylinks");
      cvdal.setInt(1, opportunityID);
      cvdal.executeUpdate();
      cvdal.clearParameters();

      cvdal.setSql("sale.deleteopportunity");
      cvdal.setInt(1, opportunityID);
      cvdal.executeUpdate();
      cvdal.clearParameters();

      // Delete the History entry from the ActivityID
      // CVUtility.deleteHistoryRecord(activityid, 14, 5, dataSource);
      // CVUtility.deleteHistoryRecord(activityid, 15, 5, dataSource);

      cvdal.setSql("sale.deleteactivity");
      cvdal.setInt(1, activityid);
      cvdal.executeUpdate();
      cvdal.clearParameters();
      cvdal.destroy();

    } catch (Exception exe) {
      exe.printStackTrace();
      result = 1;
    }
    return result;
  }

  /**
   * This method adds a opportunity
   * @param activityId(int)
   * @return OpportunityId(int)
   * @exception none
   */
  public String getOpportunityID(int activityId)
  {
    String opportunityID = "0";
    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.setSql("sale.opportunity.getopportunityid");
      cvdal.setInt(1, activityId);
      Collection col = cvdal.executeQuery();
      cvdal.clearParameters();
      if (col != null) {
        Iterator it = col.iterator();
        HashMap hm = (HashMap)it.next();
        opportunityID = ((Number)hm.get("OpportunityID")).intValue() + "";
      }

      cvdal.destroy();
    } catch (Exception exe) {
      exe.printStackTrace();
    }
    return opportunityID;
  }

  /**
   * This method adds a opportunity
   * @param userId(int)
   * @param oVO(OpportunityVO)
   * @return OpportunityId(int)
   * @exception none
   */
  public int addOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Opportunities", userId, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - addOpportunity");

    int opportunityID = -1;
    CVDal cvdal = new CVDal(dataSource);

    try {

      InitialContext ic = CVUtility.getInitialContext();

      ActivityVO aVO = oVO.getActivityVO();
      cvdal.setSql("sale.opportunity.addactivity");

      cvdal.setInt(1, ActivityVO.AT_FORCASTED_SALES);
      cvdal.setString(2, aVO.getTitle());
      cvdal.setTimestamp(3, oVO.getEstimatedClose());
      cvdal.setString(4, aVO.getActivityDetails());
      cvdal.setInt(5, aVO.getOwner());
      cvdal.setInt(6, aVO.getOwner());
      cvdal.setInt(7, aVO.getOwner());
      // cvdal.setInt(8,2);

      cvdal.executeUpdate();

      int activityID = cvdal.getAutoGeneratedKey();
      cvdal.clearParameters();

      cvdal.setSql("sale.opportunity.addopportunity");

      // add a new opportunity...
      cvdal.setInt(1, activityID);
      cvdal.setString(2, oVO.getTitle());
      cvdal.setString(3, oVO.getDescription());
      cvdal.setInt(4, oVO.getEntityID());
      cvdal.setInt(5, oVO.getIndividualID());
      cvdal.setInt(6, oVO.getOpportunityTypeID());
      cvdal.setInt(7, oVO.getStatusID());
      cvdal.setInt(8, oVO.getStageID());
      cvdal.setFloat(9, oVO.getForecastedAmount());
      cvdal.setFloat(10, oVO.getActualAmount());
      cvdal.setInt(11, oVO.getProbability());

      int sourceID = 0;

      if (oVO.getSourceID() > 0) {
        sourceID = oVO.getSourceID();
      }// end of if (oVO.getSourceID() > 0)
      else {
        String sourceName = oVO.getSource();
        if (sourceName != null && !sourceName.equals("")) {
          try {
            CommonHelperLocalHome helperHome = (CommonHelperLocalHome)ic
                .lookup("local/CommonHelper");
            CommonHelperLocal helperRemote = helperHome.create();
            sourceID = helperRemote.getSourceID(sourceName);
          }// end of try block
          catch (Exception e) {
            logger.error("[addOpportunity]: Exception", e);
          }// end of catch Block
        }// end of if (sourceName != null && !sourceName.equals(""))
      } // end of else for if (oVO.getSourceID() > 0)

      cvdal.setInt(12, sourceID);
      int accountManagerID = oVO.getAcctMgr();
      if (accountManagerID <= 0) {
        accountManagerID = userId;
      }
      cvdal.setInt(13, accountManagerID);
      cvdal.setInt(14, oVO.getAcctTeam());

      cvdal.executeUpdate();

      opportunityID = cvdal.getAutoGeneratedKey();
      cvdal.setSql("sale.opportunity.linkopportunity");
      cvdal.setInt(1, opportunityID);
      cvdal.setInt(2, 21);
      cvdal.setInt(3, activityID);
      cvdal.executeUpdate();

      cvdal.clearParameters();
      cvdal.destroy();

      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic
          .lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(dataSource);
      authorizationLocal.saveCurrentDefaultPermission("Opportunities", opportunityID, userId);

      // update the forcast amount
      this.updateForcastAmount(oVO.getActualAmount(), opportunityID);
    } catch (Exception exe) {
      logger.error("[addOpportunity]: Exception", exe);
    } finally {
      cvdal.destroy();
    }
    return opportunityID;
  }

  /**
   * This method updates a opportunity.
   * @param userId The userID of the user updating the opportunity
   * @param oVO The values of the Opportunity to update.
   */
  public void updateOpportunity(int userId, OpportunityVO oVO) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", oVO.getOpportunityID(),
        ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - updateOpportunity");

    CVDal cvdal = new CVDal(dataSource);
    try {
      ActivityVO aVO = oVO.getActivityVO();
      float totalAmount = (float)0.0;
      OpportunityVO oppDBVO = new OpportunityVO();
      oppDBVO = getOpportunity(userId, oVO.getOpportunityID());
      oVO = (OpportunityVO)CVUtility.replaceVO(oppDBVO, oVO, "Opportunities", userId,
          this.dataSource);

      // Update the Activity
      cvdal.setSql("sale.opportunity.updateactivity");
      cvdal.setInt(1, ActivityVO.AT_FORCASTED_SALES);
      cvdal.setInt(2, oVO.getStatusID());
      cvdal.setString(3, aVO.getTitle());
      cvdal.setTimestamp(4, oVO.getEstimatedClose());
      cvdal.setTimestamp(5, oVO.getActualclose());
      cvdal.setString(6, aVO.getActivityDetails());
      cvdal.setInt(7, aVO.getOwner());
      cvdal.setInt(8, aVO.getActivityID());
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      // Update the Opportunity
      cvdal.setSql("sale.opportunity.updateopportunity");
      cvdal.setString(1, oVO.getTitle());
      cvdal.setString(2, oVO.getDescription());
      cvdal.setInt(3, oVO.getEntityID());
      cvdal.setInt(4, oVO.getIndividualID());
      cvdal.setInt(5, oVO.getOpportunityTypeID());
      cvdal.setInt(6, oVO.getStatusID());
      cvdal.setInt(7, oVO.getStageID());
      cvdal.setFloat(8, oVO.getForecastedAmount());

      // calculate the total amount here
      totalAmount = oVO.getActualAmount();
      cvdal.setFloat(9, totalAmount);
      cvdal.setInt(10, oVO.getProbability());

      int sourceID = 0;
      if (oVO.getSourceID() > 0) {
        sourceID = oVO.getSourceID();
      } else {
        String sourceName = oVO.getSource();
        if (sourceName != null && !sourceName.equals("")) {
          try {
            InitialContext ic = CVUtility.getInitialContext();
            CommonHelperLocalHome helperHome = (CommonHelperLocalHome)ic
                .lookup("local/CommonHelper");
            CommonHelperLocal helperRemote = helperHome.create();
            sourceID = helperRemote.getSourceID(sourceName);
          } catch (Exception e) {
            logger.error("[updateOpportunity]: Exception", e);
          }// end of catch Block
        }// end of if (sourceName != null && !sourceName.equals(""))
      } // end of else for if (oVO.getSourceID() > 0)

      cvdal.setInt(11, sourceID);
      int accountManagerID = oVO.getAcctMgr();
      if (accountManagerID <= 0) {
        accountManagerID = userId;
      }
      cvdal.setInt(12, accountManagerID);
      cvdal.setInt(13, oVO.getAcctTeam());
      cvdal.setInt(14, oVO.getOpportunityID());
      cvdal.executeUpdate();
      // update the total amount
      if (this.hasLinkedProposalInForcast(oVO.getOpportunityID())) {
        totalAmount = this.updateTotalAmount(oVO.getOpportunityID());
      }
      this.updateForcastAmount(totalAmount, oVO.getOpportunityID());
    } catch (Exception exe) {
      logger.error("[updateOpportunity] Exception thrown.", exe);
    } finally {
      cvdal.destroy();
      cvdal = null;
    } // end of finally block
  } // end of updateOpportunity method

  public HashMap getOpportunityRelatedInfo(int userId, int opportunityId)
      throws AuthorizationFailedException
  {

    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", opportunityId,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - getOpportunity");

    HashMap relatedOpportunityInfo = new HashMap();
    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.setSql("sale.opportunity.relatedopportunityinfo");
      cvdal.setInt(1, opportunityId);
      Collection col = cvdal.executeQuery();
      cvdal.clearParameters();
      if (col != null) {
        Iterator it = col.iterator();
        relatedOpportunityInfo = (HashMap)it.next();
      }

    } // end of try block
    catch (Exception exe) {
      exe.printStackTrace();
    } // end of catch block (Exception)
    finally {
      cvdal.destroy();
      cvdal = null;
    } // end of finally block
    return relatedOpportunityInfo;
  }

  public OpportunityVO getOpportunity(int userId, int opportunityId)
      throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Opportunities", opportunityId,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - getOpportunity");

    OpportunityVO oVO = null;
    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.setSql("sale.opportunity.getopportunity");
      cvdal.setInt(1, opportunityId);
      Collection col = cvdal.executeQuery();
      cvdal.clearParameters();
      boolean hasRecord = false;

      if (col.size() == 0) {
        cvdal.setSql("sale.opportunity.getopportunityactivity");
        cvdal.setInt(1, opportunityId);
        col = cvdal.executeQuery();
        cvdal.clearParameters();
      }

      if (col != null && col.iterator().hasNext()) {
        HashMap hm = (HashMap)col.iterator().next();
        oVO = new OpportunityVO();
        oVO.setOpportunityID(((Number)hm.get("opportunityid")).intValue());
        oVO.setActivityID(((Number)hm.get("activityid")).intValue());
        oVO.setTitle((String)hm.get("title"));
        oVO.setDescription((String)hm.get("description"));
        oVO.setEntityID(((Number)hm.get("entityid")).intValue());
        oVO.setIndividualID(((Number)hm.get("individualid")).intValue());
        oVO.setOpportunityTypeID(((Number)hm.get("typeid")).intValue());
        oVO.setOpportunityType((String)hm.get("typename"));
        oVO.setStatusID(((Number)hm.get("status")).intValue());
        oVO.setStatus((String)hm.get("statusname"));
        oVO.setStageID(((Number)hm.get("stage")).intValue());
        oVO.setStage((String)hm.get("stagename"));
        oVO.setForecastedAmount(((Number)hm.get("forecastammount")).floatValue());
        oVO.setActualAmount(((Number)hm.get("actualamount")).floatValue());
        oVO.setProbability(((Number)hm.get("probability")).intValue());
        oVO.setSourceID(((Number)hm.get("source")).intValue());
        oVO.setSource((String)hm.get("sourcename"));
        oVO.setAcctMgr(((Number)hm.get("accountmanager")).intValue());
        oVO.setAcctTeam(((Number)hm.get("accountteam")).intValue());
        oVO.setEstimatedClose((Timestamp)hm.get("start"));
        oVO.setActualclose((Timestamp)hm.get("completeddate"));
        oVO.setIndividualname((String)hm.get("individualname"));
        oVO.setManagerName((String)hm.get("managername"));
        oVO.setEntityname((String)hm.get("entityname"));
        oVO.setTeamName((String)hm.get("accteamname"));
        oVO.setCreateddate((Timestamp)hm.get("created"));
        oVO.setModifieddate((Timestamp)hm.get("modified"));
        oVO.setCreatedbyname((String)hm.get("createdbyname"));
        oVO.setModifiedbyname((String)hm.get("modifiedbyname"));

        hasRecord = true;

      }
      col = null;

      if (hasRecord) {
        oVO.setHasProposal(this.hasLinkedProposal(opportunityId));
        oVO.setHasProposalInForcast(this.hasLinkedProposalInForcast(opportunityId));
      } // end of if statement (hasRecord)
    } // end of try block
    catch (Exception e) {
      logger.error("[getOpportunity] Exception thrown.", e);
    } // end of catch block (Exception)
    finally {
      cvdal.destroy();
      cvdal = null;
    } // end of finally block
    return oVO;
  } // end of getOpportunity method

  /**
   * Does nothing at this point in time other than return 0.
   * @param userId
   * @param oVO
   * @return Returns 0.
   */
  public int duplicateOpportunity(int userId, OpportunityVO oVO)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Opportunities", userId, this.dataSource))
      throw new AuthorizationFailedException("Opportunities - duplicateOpportunity");
    return 0;
  } // end of duplicateOpportunity method

  /**
   * Links an object to an opportunity
   * @param userId The UserID of the user linking an object to an opportunity.
   * @param opportunityId The Opportunity ID to link to the User.
   * @param tableId The Table ID of the data to link this opportunity to.
   * @param rowIds I'm not quite sure.
   */
  public void linkOpportunity(int userId, int opportunityId, int tableId, int[] rowIds)
  {
    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.setSql("sale.opportunity.linkopportunity");
      cvdal.setInt(1, opportunityId);
      cvdal.setInt(2, tableId);
      for (int i = 0; i < rowIds.length; i++) {
        cvdal.setInt(3, rowIds[i]);
        cvdal.executeUpdate();
      } // end of for loop (int i=0; i < rowIds.length; i++)
    } // end of try block
    catch (Exception exe) {
      logger.error("[linkOpportunity] Exception thrown.", exe);
    } // end of catch block
    finally {
      cvdal.destroy();
      cvdal = null;
    } // end of finally block
  } // end of linkOpportunity method

  /**
   * This method will calculate the new total for the opportunity, save it to
   * the database and return the new total.
   * @param opportunityID The ID of the opportunity which the total needs to be
   *          calculated.
   * @return The new total value
   */
  public float updateTotalAmount(int opportunityID)
  {
    double newTotal = (double)0.0;
    CVDal dataConnection = new CVDal(dataSource);
    try {
      dataConnection.setSql("sale.opportunity.getForcastFromProposalInForcast");
      dataConnection.setInt(1, opportunityID);
      Collection resultsCollection = dataConnection.executeQuery();
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        while (resultsIterator.hasNext()) {
          HashMap hashMap = (HashMap)resultsIterator.next();
          newTotal += Double.parseDouble((hashMap.get("ForecastAmmount")).toString());
        } // end of while loop (resultsIterator.hasNext())
      } // end of if statement (resultsCollection != null)

      dataConnection.setSql("sale.opportunity.updateActualAmount");
      dataConnection.setDouble(1, newTotal);
      dataConnection.setInt(2, opportunityID);
      dataConnection.executeUpdate();
    } // end of try block
    catch (Exception exception) {
      logger.error("[updateTotalAmount] Exception thrown.", exception);
    } // end of catch block (Exception)
    finally {
      dataConnection.destroy();
      dataConnection = null;
    } // end of finally block
    // obtain the proposals linked to this opportunity
    return (float)newTotal;
  } // end of updateTotalAmount method

  /**
   * This method will calculate the new total for the opportunity, save it to
   * the database and return the new total.
   * @param opportunityID The ID of the opportunity which the total needs to be
   *          calculated.
   * @return The new total value
   */
  public float updateForcastAmount(float totalAmount, int opportunityID)
  {
    double newForcast = (double)0.0;
    double probability = (double)0.0;
    CVDal dataConnection = new CVDal(dataSource);
    try {
      // get the probability
      dataConnection.setSql("sale.opportunity.getProbabilityPercentage");
      dataConnection.setFloat(1, opportunityID);
      Collection resultsCollection = dataConnection.executeQuery();
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator.hasNext()) {
          HashMap hashMap = (HashMap)resultsIterator.next();
          probability = Double.parseDouble((hashMap.get("Probability")).toString());
          probability *= (double)0.01;
        } // end of if statement (resultsIterator.hasNext())
      } // end of if statement (resultsCollection != null)

      // do the calculation
      newForcast = totalAmount * probability;

      dataConnection.setSql("sale.opportunity.updateForcastAmount");
      dataConnection.setDouble(1, newForcast);
      dataConnection.setInt(2, opportunityID);
      dataConnection.executeUpdate();
    } // end of try block
    catch (Exception exception) {
      logger.error("[updateForcastAmount] Exception thrown.", exception);
    } // end of catch block (Exception)
    finally {
      dataConnection.destroy();
      dataConnection = null;
    } // end of finally block
    // obtain the proposals linked to this opportunity
    return (float)newForcast;
  } // end of updateForcastAmount method

  /**
   * Returns whether the Opportunity with the ID which is passed to the method
   * has any proposals (included in the forecast) linked to it.
   * @param opportunityID The ID of the Opportunity to look up.
   * @return Whether or not this Opportunity has a proposal (included in the
   *         forecast) linked to it.
   */
  private boolean hasLinkedProposalInForcast(int opportunityID)
  {
    boolean returnValue = false;
    CVDal dataConnection = new CVDal(dataSource);
    try {
      dataConnection.setSql("sale.opportunity.getForcastFromProposalInForcast");
      dataConnection.setInt(1, opportunityID);
      Collection resultsCollection = dataConnection.executeQuery();
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator != null && resultsIterator.hasNext()) {
          returnValue = true;
        } // end of if statement (resultsIterator != null &&
          // resultsIterator.hasNext())
      } // end of if statement (resultsCollection != null)
    } // end of try block
    catch (Exception exception) {
      logger.error("[hasLinkedProposalInForcast] Exception thrown.", exception);
    } // end of catch block (Exception)
    finally {
      dataConnection.destroy();
      dataConnection = null;
    } // end of finally block
    return returnValue;
  } // end of hasLinkedProposalInForcast method

  /**
   * Returns whether the Opportunity with the ID which is passed to the method
   * has any proposals linked to it.
   * @param opportunityID The ID of the Opportunity to look up.
   * @return Whether or not this Opportunity has a proposal linked to it.
   */
  private boolean hasLinkedProposal(int opportunityID)
  {
    boolean returnValue = false;
    CVDal dataConnection = new CVDal(dataSource);
    try {
      dataConnection.setSql("sale.opportunity.hasproposal");
      dataConnection.setInt(1, opportunityID);
      Collection resultsCollection = dataConnection.executeQuery();
      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();
        if (resultsIterator != null && resultsIterator.hasNext()) {
          returnValue = true;
        } // end of if statement (resultsIterator != null &&
          // resultsIterator.hasNext())
      } // end of if statement (resultsCollection != null)
    } // end of try block
    catch (Exception exception) {
      logger.error("[hasLinkedProposal] Exception thrown.", exception);
    } // end of catch block (Exception)
    finally {
      dataConnection.destroy();
      dataConnection = null;
    } // end of finally block
    return returnValue;
  } // end of hasLinkedProposalInForcast method

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
    String OpportunityName = "";
    CVDal dl = new CVDal(dataSource);
    try {
      String OpportunityQuery = "select OpportunityID, title  from opportunity where OpportunityID = ?";
      dl.setSqlQuery(OpportunityQuery);
      dl.setInt(1, OpportunityID);
      Collection col = dl.executeQuery();

      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          OpportunityName = (String)hm.get("title");
        }// end of while (it.hasNext())
      }// end of if (col != null)
    } catch (Exception e) {
      e.printStackTrace();
    } // end of catch block (Exception)
    finally {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    } // end of finally block
    return OpportunityName;
  }

} // end of OpportunityEJB class.
