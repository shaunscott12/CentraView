/*
 * $RCSfile: SaleListEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

package com.centraview.sale.salelist;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.CVDal;
import com.centraview.common.EJBUtil;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This class is a Statefull session Bean which acts as a Interface for Sale
 * Module
 */
public class SaleListEJB implements SessionBean {
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public void ejbRemove()
  {}

  public void ejbCreate()
  {}
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
   * Returns a ValueListVO representing a list of Opportunity records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getOpportunityValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE opportunitylistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }

    int numberOfRecords = 0;
    if (applyFilter) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "opportunitylistfilter", individualID,
          30, "opportunity", "OpportunityId", "activity.owner", "activityId", permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 30, "opportunity",
          "OpportunityId", "activity.owner", "activityId", permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);

    String query = this.buildOpportunityListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE opportunitylistfilter");
      cvdl.executeUpdate();
    }

    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildOpportunityListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = "SELECT o.OpportunityID, o.title, sst.Name AStatus, o.ActualAmount, "
        + "o.Description, o.EntityID, e.name AS Entity, st.Name AS Type, ss.Name AS "
        + "Stage, sp.Title AS Probability, sst.Name AS Status, a.start AS "
        + "EstimatedCloseDate, o.ForecastAmmount, a.activityid AS ActivityID, "
        + "o.Accountmanager AS SalePersonID, concat(i.firstname,  ' ' , i.lastname) "
        + "AS SalePersonName, o.IndividualID ";

    String joinConditions = "LEFT OUTER JOIN individual AS i ON o.AccountManager = i.IndividualID "
        + "LEFT OUTER JOIN salestype AS st ON o.TypeID = st.SalesTypeID "
        + "LEFT OUTER JOIN salesstage AS ss ON o.Stage = ss.SalesStageID "
        + "LEFT OUTER JOIN salesprobability AS sp ON o.Probability = sp.ProbabilityID "
        + "LEFT OUTER JOIN salesstatus AS sst ON o.Status = sst.SalesStatusID ";

    StringBuffer from = new StringBuffer("FROM opportunity o, activity a, entity e ");
    StringBuffer where = new StringBuffer(
        "WHERE o.activityid = a.activityid AND o.EntityID = e.EntityID ");
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter lf ");
      where.append("AND o.opportunityId = lf.opportunityId ");
    }

    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  /**
   * Returns a ValueListVO representing a list of Proposals records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getProposalsValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean permissionSwitch = individualID < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE proposalslistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;

    if (applyFilter) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "proposalslistfilter", individualID, 30,
          "proposal", "ProposalId", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 30, "proposal",
          "ProposalId", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildProposalsListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE proposalslistfilter");
      cvdl.executeUpdate();
    }

    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }

    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildProposalsListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = "SELECT p.ProposalID, p.Title, p.Description, st.Name AS Type, "
        + "ss.Name AS Stage, sp.Title AS Probability, stat.Name AS Status, "
        + "p.EstimatedCloseDate, p.ForecastAmmount, p.owner AS SalePersonID, "
        + "CONCAT(i.firstname, ' ', i.lastname) AS SalePersonName ";

    String joinConditions = "LEFT OUTER JOIN salestype st ON (st.SalesTypeID = p.TypeID) "
        + "LEFT OUTER JOIN salesstage ss ON (ss.SalesStageID = p.Stage) "
        + "LEFT OUTER JOIN salesprobability sp ON (sp.ProbabilityID = p.Probability) "
        + "LEFT OUTER JOIN salesstatus stat ON (stat.SalesStatusID = p.Status) "
        + "LEFT OUTER JOIN individual i ON (i.individualID = p.owner) ";

    StringBuffer from = new StringBuffer("FROM proposal p ");
    StringBuffer where = new StringBuffer("WHERE 1 =1 ");
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND p.ProposalId = lf.ProposalId ");
    }

    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }
}
