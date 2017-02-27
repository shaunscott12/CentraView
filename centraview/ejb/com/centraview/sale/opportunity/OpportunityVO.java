/*
 * $RCSfile: OpportunityVO.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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

import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVAudit;

/**
 * OpportunityVO.java
 */
public class OpportunityVO extends CVAudit {
  private int opportunityID;
  private String title;
  private String description;
  private int entityID;
  private String entityname;
  private int individualID;
  private String individualname;
  private int sourceID;
  private String source;
  private int statusID;
  private String status;
  private int stageID;
  private String stage;
  private int opportunityTypeID;
  private String opportunityType;
  private float forecastedAmount;
  private float actualAmount;
  private int probability;
  private float probableAmount;
  private Timestamp estimatedClose;
  private Timestamp actualclose;
  private int acctMgr;
  private String managerName;
  private int acctTeam;
  private String teamName;
  private Timestamp createddate;
  private Timestamp modifieddate;
  private String createdbyname;
  private String modifiedbyname;

  private int activityID;
  private ActivityVO activityVO;

  private boolean hasProposal = false;
  private boolean hasProposalInForcast = false;

  public int getAcctMgr()
  {
    return this.acctMgr;
  }

  public void setAcctMgr(int acctMgr)
  {
    this.acctMgr = acctMgr;
  }

  public int getAcctTeam()
  {
    return this.acctTeam;
  }

  public void setAcctTeam(int acctTeam)
  {
    this.acctTeam = acctTeam;
  }

  public Timestamp getActualclose()
  {
    return this.actualclose;
  }

  public void setActualclose(Timestamp actualclose)
  {
    this.actualclose = actualclose;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public int getEntityID()
  {
    return this.entityID;
  }

  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  }

  public String getEntityname()
  {
    return this.entityname;
  }

  public void setEntityname(String entityname)
  {
    this.entityname = entityname;
  }

  public Timestamp getEstimatedClose()
  {
    return this.estimatedClose;
  }

  public void setEstimatedClose(Timestamp estimatedClose)
  {
    this.estimatedClose = estimatedClose;
  }

  public float getForecastedAmount()
  {
    return this.forecastedAmount;
  }

  public void setForecastedAmount(float forecastedAmount)
  {
    this.forecastedAmount = forecastedAmount;
  }

  public int getIndividualID()
  {
    return this.individualID;
  }

  public void setIndividualID(int individualID)
  {
    this.individualID = individualID;
  }

  public String getIndividualname()
  {
    return this.individualname;
  }

  public void setIndividualname(String individualname)
  {
    this.individualname = individualname;
  }

  public String getManagerName()
  {
    return this.managerName;
  }

  public void setManagerName(String managerName)
  {
    this.managerName = managerName;
  }

  public int getOpportunityID()
  {
    return this.opportunityID;
  }

  public void setOpportunityID(int opportunityID)
  {
    this.opportunityID = opportunityID;
  }

  public String getOpportunityType()
  {
    return this.opportunityType;
  }

  public void setOpportunityType(String opportunityType)
  {
    this.opportunityType = opportunityType;
  }

  public int getOpportunityTypeID()
  {
    return this.opportunityTypeID;
  }

  public void setOpportunityTypeID(int opportunityTypeID)
  {
    this.opportunityTypeID = opportunityTypeID;
  }

  public int getProbability()
  {
    return this.probability;
  }

  public void setProbability(int probability)
  {
    this.probability = probability;
  }

  public float getProbableAmount()
  {
    return this.probableAmount;
  }

  public void setProbableAmount(float probableAmount)
  {
    this.probableAmount = probableAmount;
  }

  public String getSource()
  {
    return this.source;
  }

  public void setSource(String source)
  {
    this.source = source;
  }

  public int getSourceID()
  {
    return this.sourceID;
  }

  public void setSourceID(int sourceID)
  {
    this.sourceID = sourceID;
  }

  public String getStage()
  {
    return this.stage;
  }

  public void setStage(String stage)
  {
    this.stage = stage;
  }

  public int getStageID()
  {
    return this.stageID;
  }

  public void setStageID(int stageID)
  {
    this.stageID = stageID;
  }

  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public int getStatusID()
  {
    return this.statusID;
  }

  public void setStatusID(int statusID)
  {
    this.statusID = statusID;
  }

  public String getTeamName()
  {
    return this.teamName;
  }

  public void setTeamName(String teamName)
  {
    this.teamName = teamName;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public ActivityVO getActivityVO()
  {
    return this.activityVO;
  }

  public void setActivityVO(ActivityVO activityVO)
  {
    this.activityVO = activityVO;
  }

  public int getActivityID()
  {
    return this.activityID;
  }

  public void setActivityID(int activityID)
  {
    this.activityID = activityID;
  }

  public float getActualAmount()
  {
    return this.actualAmount;
  }

  public void setActualAmount(float actualAmount)
  {
    this.actualAmount = actualAmount;
  }

  public Timestamp getCreateddate()
  {
    return this.createddate;
  }

  public void setCreateddate(Timestamp createddate)
  {
    this.createddate = createddate;
  }

  public Timestamp getModifieddate()
  {
    return this.modifieddate;
  }

  public void setModifieddate(Timestamp modifieddate)
  {
    this.modifieddate = modifieddate;
  }

  public String getCreatedbyname()
  {
    return this.createdbyname;
  }

  public void setCreatedbyname(String createdbyname)
  {
    this.createdbyname = createdbyname;
  }

  public String getModifiedbyname()
  {
    return this.modifiedbyname;
  }

  public void setModifiedbyname(String modifiedbyname)
  {
    this.modifiedbyname = modifiedbyname;
  }

  public boolean getHasProposal()
  {
    return this.hasProposal;
  }

  public void setHasProposal(boolean hasProposal)
  {
    this.hasProposal = hasProposal;
  }

  /**
   * Returns whether this opportunity has any proposals included in the
   * opportunity total and forcast amounts.
   * @return Whether or not this opportunity has any proposals which should be
   *         included in the total/forcast amount.
   */
  public boolean getHasProposalInForcast()
  {
    return this.hasProposalInForcast;
  }

  /**
   * Sets whether this opportunity has any proposals included in the opportunity
   * total and forcast amounts.
   * @param hasProposalInForcast Whether or not this opportunity has any
   *          proposals which should be included in the total/forcast amount.
   */
  public void setHasProposalInForcast(boolean hasProposalInForcast)
  {
    this.hasProposalInForcast = hasProposalInForcast;
  }
}
