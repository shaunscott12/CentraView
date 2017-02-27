/*
 * $RCSfile: OpportunityForm.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 19:35:56 $ - $Author: mcallist $
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

package com.centraview.sale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.Validation;

public class OpportunityForm extends ActionForm {
  private static Logger logger = Logger.getLogger(OpportunityForm.class);
  private String opportunityid;
  private String activityid;
  private String title;
  private String description;
  private String entityid;
  private String entityname;
  private String individualid;
  private String individualname;
  private String sourceid = "0";
  private String sourcename;
  private String statusid;
  private String statusname;
  private String stageid;
  private String stagename;
  private String opportunitytypeid;
  private String opportunitytypename;
  private String totalamount = "0.00";
  // Since the requirement for new Opportunity to select 100% probability thats
  // why defaulting to 12 which will internally pick 100%
  private String probabilityid = "12";
  private String probabilityname;
  private String forecastedamount = "0.00";
  private String estimatedcloseday;
  private String estimatedclosemonth;
  private String estimatedcloseyear;
  private java.sql.Date estimatedclose;
  private java.sql.Date actualclose;
  private String actualcloseday;
  private String actualclosemonth;
  private String actualcloseyear;
  private String acctmgrid = "0";
  private String acctmgrname;
  private String acctteamid;
  private String acctteamname;
  private String createddate;
  private String modifieddate;
  private String createdby;
  private String modifiedby;
  private String createdbyname;
  private String modifiedbyname;

  public String getAcctmgrid()
  {
    return this.acctmgrid;
  }

  public void setAcctmgrid(String acctmgrid)
  {
    this.acctmgrid = acctmgrid;
  }

  public String getAcctmgrname()
  {
    return this.acctmgrname;
  }

  public void setAcctmgrname(String acctmgrname)
  {
    this.acctmgrname = acctmgrname;
  }

  public String getAcctteamid()
  {
    return this.acctteamid;
  }

  public void setAcctteamid(String acctteamid)
  {
    this.acctteamid = acctteamid;
  }

  public String getAcctteamname()
  {
    return this.acctteamname;
  }

  public void setAcctteamname(String acctteamname)
  {
    this.acctteamname = acctteamname;
  }

  public java.sql.Date getActualclose()
  {
    return this.actualclose;
  }

  public void setActualclose(java.sql.Date actualclose)
  {
    this.actualclose = actualclose;
  }

  public String getActualcloseday()
  {
    return this.actualcloseday;
  }

  public void setActualcloseday(String actualcloseday)
  {
    this.actualcloseday = actualcloseday;
  }

  public String getActualclosemonth()
  {
    return this.actualclosemonth;
  }

  public void setActualclosemonth(String actualclosemonth)
  {
    this.actualclosemonth = actualclosemonth;
  }

  public String getActualcloseyear()
  {
    return this.actualcloseyear;
  }

  public void setActualcloseyear(String actualcloseyear)
  {
    this.actualcloseyear = actualcloseyear;
  }

  public String getCreateddate()
  {
    return this.createddate;
  }

  public void setCreateddate(String createddate)
  {
    this.createddate = createddate;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getEntityid()
  {
    return this.entityid;
  }

  public void setEntityid(String entityid)
  {
    this.entityid = entityid;
  }

  public String getEntityname()
  {
    return this.entityname;
  }

  public void setEntityname(String entityname)
  {
    this.entityname = entityname;
  }

  public java.sql.Date getEstimatedclose()
  {
    return this.estimatedclose;
  }

  public void setEstimatedclose(java.sql.Date estimatedclose)
  {
    this.estimatedclose = estimatedclose;
  }

  public String getEstimatedcloseday()
  {
    return this.estimatedcloseday;
  }

  public void setEstimatedcloseday(String estimatedcloseday)
  {
    this.estimatedcloseday = estimatedcloseday;
  }

  public String getEstimatedclosemonth()
  {
    return this.estimatedclosemonth;
  }

  public void setEstimatedclosemonth(String estimatedclosemonth)
  {
    this.estimatedclosemonth = estimatedclosemonth;
  }

  public String getEstimatedcloseyear()
  {
    return this.estimatedcloseyear;
  }

  public void setEstimatedcloseyear(String estimatedcloseyear)
  {
    this.estimatedcloseyear = estimatedcloseyear;
  }

  public String getForecastedamount()
  {
    return this.forecastedamount;
  }

  public void setForecastedamount(String forecastedamount)
  {
    this.forecastedamount = forecastedamount;
  }

  public String getIndividualid()
  {
    return this.individualid;
  }

  public void setIndividualid(String individualid)
  {
    this.individualid = individualid;
  }

  public String getIndividualname()
  {
    return this.individualname;
  }

  public void setIndividualname(String individualname)
  {
    this.individualname = individualname;
  }

  public String getModifieddate()
  {
    return this.modifieddate;
  }

  public void setModifieddate(String modifieddate)
  {
    this.modifieddate = modifieddate;
  }

  public String getOpportunityid()
  {
    return this.opportunityid;
  }

  public void setOpportunityid(String opportunityid)
  {
    this.opportunityid = opportunityid;
  }

  public String getOpportunitytypeid()
  {
    return this.opportunitytypeid;
  }

  public void setOpportunitytypeid(String opportunitytypeid)
  {
    this.opportunitytypeid = opportunitytypeid;
  }

  public String getOpportunitytypename()
  {
    return this.opportunitytypename;
  }

  public void setOpportunitytypename(String opportunitytypename)
  {
    this.opportunitytypename = opportunitytypename;
  }

  public String getProbabilityid()
  {
    return this.probabilityid;
  }

  public void setProbabilityid(String probabilityid)
  {
    this.probabilityid = probabilityid;
  }

  public String getProbabilityname()
  {
    return this.probabilityname;
  }

  public void setProbabilityname(String probabilityname)
  {
    this.probabilityname = probabilityname;
  }

  public String getSourceid()
  {
    return this.sourceid;
  }

  public void setSourceid(String sourceid)
  {
    this.sourceid = sourceid;
  }

  public String getSourcename()
  {
    return this.sourcename;
  }

  public void setSourcename(String sourcename)
  {
    this.sourcename = sourcename;
  }

  public String getStageid()
  {
    return this.stageid;
  }

  public void setStageid(String stageid)
  {
    this.stageid = stageid;
  }

  public String getStagename()
  {
    return this.stagename;
  }

  public void setStagename(String stagename)
  {
    this.stagename = stagename;
  }

  public String getStatusid()
  {
    return this.statusid;
  }

  public void setStatusid(String statusid)
  {
    this.statusid = statusid;
  }

  public String getStatusname()
  {
    return this.statusname;
  }

  public void setStatusname(String statusname)
  {
    this.statusname = statusname;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getTotalamount()
  {
    return this.totalamount;
  }

  public void setTotalamount(String totalamount)
  {
    this.totalamount = totalamount;
  }

  public String getCreatedby()
  {
    return this.createdby;
  }

  public void setCreatedby(String createdby)
  {
    this.createdby = createdby;
  }

  public String getModifiedby()
  {
    return this.modifiedby;
  }

  public void setModifiedby(String modifiedby)
  {
    this.modifiedby = modifiedby;
  }

  /**
   * Validates user input data.
   * @param mapping ActionMapping
   * @param request HttpServletRequest
   * @return ActionErrors
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();

    try {
      Validation validation = new Validation();
      // title
      if (this.getTitle() == null || this.getTitle().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }
      // entity
      if (this.getEntityname() == null || this.getEntityname().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Entity"));
      }
      // total amount
      if ((this.getTotalamount() != null) && !this.getTotalamount().trim().equals("")) {
        String Totalamount = this.getTotalamount();
        Totalamount = Totalamount.replaceAll(",", "");
      }

      // forecasted amount
      if ((this.getForecastedamount() != null) && !this.getForecastedamount().trim().equals("")) {
        String ForecastedAmount = this.getForecastedamount();
        ForecastedAmount = ForecastedAmount.replaceAll(",", "");
      }
      // redirect to jsp if errors present
      if (errors != null) {
        request.setAttribute(SaleConstantKeys.CURRENTTAB, SaleConstantKeys.DETAIL);
        request.setAttribute(SaleConstantKeys.TYPEOFOPERATION, request.getParameter(SaleConstantKeys.TYPEOFOPERATION));
        request.setAttribute(SaleConstantKeys.WINDOWID, request.getParameter(SaleConstantKeys.WINDOWID));
        // set request parameter as set in viewhandler and newhandler
      }

      request.setAttribute("opportunityeform", this);
    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    }
    return errors;
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

  public String getActivityid()
  {
    return this.activityid;
  }

  public void setActivityid(String activityid)
  {
    this.activityid = activityid;
  }
} // end of OpportunityForm class
