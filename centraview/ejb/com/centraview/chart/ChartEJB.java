/*
 * $RCSfile: ChartEJB.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 18:05:43 $ - $Author: mcallist $
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

package com.centraview.chart;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.centraview.common.CVDal;
import com.centraview.common.EJBUtil;
import com.centraview.valuelist.ValueListParameters;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ChartEJB implements SessionBean {
  /** The SessionContext of this SessionBean. */
  protected SessionContext ctx;

  /** The JNDI/DataSource name this EJB will be using. */
  private String dataSource;
  private static Logger logger = Logger.getLogger(ChartEJB.class);

  public void setSessionContext(SessionContext ctx) throws EJBException
  {
    this.ctx = ctx;
  }

  public void ejbActivate() throws EJBException
  {}

  public void ejbRemove() throws EJBException
  {}

  public void ejbPassivate() throws EJBException
  {}

  public void ejbCreate()
  {}

  /**
   * This simply sets the target datasource to be used for DB interaction.
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  } // end setDataSource() method

  /**
   * This method delegates to getOpportunityPieData and getOpportunityBarData
   * and packages the results into a HashMap which contains two keys and
   * associated objects the HashMap keys are "pie" and "bar". To find the keys
   * to the pie and bar results review the JavaDoc for the methods that do the
   * real work
   * @param individualId
   * @param paramters ValueListParameters for the list generated.
   * @return
   */
  public HashMap getOpportunityChartData(int individualId, ValueListParameters paramters)
  {
    // Possible optimizations exist by not recreating the same filter table
    // twice,
    // however I don't know how much that will save us, so for now we are going
    // the
    // road more travelled, and do them separately.
    HashMap returnMap = new HashMap();
    returnMap.put("pie", this.getOpportunityPieData(individualId, paramters));
    returnMap.put("bar", this.getOpportunityBarData(individualId, paramters));
    return returnMap;
  }

  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a pie graph on the
   * Sales dashboard. The fields available are "estimatedClose" "stageName"
   * "count" "forecastAmount" "actualAmount"
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getOpportunityPieData(int individualId, ValueListParameters parameters)
  {
    return this.getOpportunityData(individualId, parameters, "pie");
  }

  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a bar chart on the
   * Sales dashboard. The fields available are "stageName" "count"
   * "forecastAmount" "actualAmount"
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getOpportunityBarData(int individualId, ValueListParameters parameters)
  {
    return this.getOpportunityData(individualId, parameters, "bar");
  }

  /**
   * Method that parses the valueListParameters and gets a set of data back
   * based on the type if type is "pie" it will execute a query dynamically
   * generated to get the data for our pie chart on the dashboard, if it is
   * "bar" it will get the data we need for the bar chart.
   * @param individualId the individual Id for the permission queries.
   * @param parameters the valueList parameters to pull out the filter from.
   * @param type either "pie" or "bar"
   * @return The collection of data, really a vector of hashmaps, where each row
   *         is represented by a hashmap.
   */
  private Collection getOpportunityData(int individualId, ValueListParameters parameters,
      String type)
  {
    Collection opportunityData = null;
    Class[] methodParameters = { Boolean.TYPE, String.class };
    try {
      Class thisClass = this.getClass();
      Method buildOpportunityQuery = thisClass.getMethod("buildOpportunityQuery", methodParameters);
      opportunityData = this.getData(individualId, parameters, "opportunity", "opportunityId",
          "activity.owner", "activityId", this, buildOpportunityQuery, type, 30);
    } catch (NoSuchMethodException e) {
      logger.error("[getOpportunityData] Exception thrown.", e);
      throw new EJBException(e);
    }
    return opportunityData;
  } // end getOpportunityData

  /**
   * This method dynamically builds the query, actually it only selects between
   * two queries and the only dynamic part is if there is a filter to join
   * against or not.
   * @param applyFilter whether a listfilter table exists for us to join
   *          against.
   * @param type "pie" or "bar" actually, "pie" or any other string.
   * @return The generated query to hand to CVDal.
   */
  public String buildOpportunityQuery(boolean applyFilter, String type)
  {
    String select = null;
    StringBuffer from = null;
    StringBuffer where = null;
    String end = null;
    if (type.equals("pie")) {
      select = "SELECT ss.name AS stageName, COUNT(*) AS count, SUM(o.forecastAmmount) AS forecastAmount, SUM(o.actualAmount) AS actualAmount ";
      from = new StringBuffer("FROM  opportunity AS o, salesstage AS ss ");
      where = new StringBuffer("WHERE o.stage = ss.salesstageid ");
      end = "GROUP BY 1";
    } else { // must be bar
      select = "SELECT DATE_FORMAT(a.start, '%b %y') AS estimatedClose, ss.name AS stageName, COUNT(*) AS count, SUM(o.forecastAmmount) AS forecastAmount, SUM(o.actualAmount) AS actualAmount ";
      from = new StringBuffer("FROM opportunity AS o, salesstage AS ss, activity AS a ");
      where = new StringBuffer(
          "WHERE o.stage = ss.salesstageid AND a.activityid = o.activityid AND a.start IS NOT NULL ");
      end = "GROUP BY 1,2 ORDER BY a.start";
    }
    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter) {
      from.append(", listfilter AS lf ");
      where.append("AND o.opportunityId = lf.opportunityId ");
    }
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(end);
    return query.toString();
  }

  /**
   * This method delegates to getTicketPieData and getTicketBarData and packages
   * the results into a HashMap which contains two keys and associated objects
   * the HashMap keys are "pie" and "bar". To find the keys to the pie and bar
   * results review the JavaDoc for the methods that do the real work
   * @param individualId
   * @param paramters ValueListParameters for the list generated.
   * @return HashMap with two Keys "pie" and "bar" that contain the data for the
   *         charts.
   */
  public HashMap getTicketChartData(int individualId, ValueListParameters paramters)
  {
    // Possible optimizations exist by not recreating the same filter table
    // twice,
    // however I don't know how much that will save us, so for now we are going
    // the
    // road more travelled, and do them separately.
    HashMap returnMap = new HashMap();
    returnMap.put("pie", this.getTicketPieData(individualId, paramters));
    returnMap.put("bar", this.getTicketBarData(individualId, paramters));
    return returnMap;
  }

  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a pie graph on the
   * Support dashboard. The fields available are "name" and "count"
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getTicketPieData(int individualId, ValueListParameters parameters)
  {
    return this.getTicketData(individualId, parameters, "pie");
  }

  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a bar chart on the
   * Support dashboard. The fields available are "age" "name" and "count" The
   * age is an integer, from 0 - 9 using the MySQL interval function. the
   * intervals that the age in hours is compared to are: 1, 2, 4, 8, 24, 48, 72,
   * 96. So you would get 0 where age is < 1 hour old and 1 where 1 <= age < 2
   * and 3 where 2 <= age < 4, ..., 9 where age >= 120 hours or 5 days
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getTicketBarData(int individualId, ValueListParameters parameters)
  {
    return this.getTicketData(individualId, parameters, "bar");
  }

  /**
   * Method that parses the valueListParameters and gets a set of data back
   * based on the type if type is "pie" it will execute a query dynamically
   * generated to get the data for our pie chart on the dashboard, if it is
   * "bar" it will get the data we need for the bar chart.
   * @param individualId the individual Id for the permission queries.
   * @param parameters the valueList parameters to pull out the filter from.
   * @param type either "pie" or "bar"
   * @return The collection of data, really a vector of hashmaps, where each row
   *         is represented by a hashmap.
   */
  private Collection getTicketData(int individualId, ValueListParameters parameters, String type)
  {
    Collection opportunityData = null;
    Class[] methodParameters = { Boolean.TYPE, String.class };
    try {
      Class thisClass = this.getClass();
      Method buildTicketQuery = thisClass.getMethod("buildTicketQuery", methodParameters);
      opportunityData = this.getData(individualId, parameters, "ticket", "ticketId", "owner", null,
          this, buildTicketQuery, type, 39);
    } catch (NoSuchMethodException e) {
      logger.error("[getOpportunityData] Exception thrown.", e);
      throw new EJBException(e);
    }
    return opportunityData;
  } // end getOpportunityData

  /**
   * This method dynamically builds the query, actually it only selects between
   * two queries and the only dynamic part is if there is a filter to join
   * against or not.
   * @param applyFilter whether a listfilter table exists for us to join
   *          against.
   * @param type "pie" or "bar" actually, "pie" or any other string.
   * @return The generated query to hand to CVDal.
   */
  public String buildTicketQuery(boolean applyFilter, String type)
  {
    String select = null;
    StringBuffer from = null;
    String join = null;
    StringBuffer where = null;
    String end = null;
    if (type.equals("pie")) {
      select = "SELECT CONCAT(i.firstName, ' ', i.lastName) AS name, COUNT(*) AS count ";
      from = new StringBuffer("FROM ticket AS t ");
      join = "LEFT OUTER JOIN individual AS i ON t.assignedTo = i.individualId ";
      where = new StringBuffer("WHERE t.status <> 2 ");
      end = "GROUP BY 1";
    } else { // must be bar
      select = "SELECT INTERVAL((UNIX_TIMESTAMP()-UNIX_TIMESTAMP(t.created))/3600,1,2,4,8,24,48,72,96,120) AS age, CONCAT(i.firstName, ' ', i.lastName) AS name, COUNT(*) AS count ";
      from = new StringBuffer("FROM ticket AS t ");
      join = "LEFT OUTER JOIN individual AS i ON t.assignedTo = i.individualId ";
      where = new StringBuffer("WHERE t.status <> 2 ");
      end = "GROUP BY 1,2 ORDER BY 1";
    }
    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter) {
      from.insert(5, "listfilter AS lf, ");
      where.append("AND t.ticketId = lf.ticketId ");
    }
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(join);
    query.append(where);
    query.append(end);
    return query.toString();
  }

  /**
   * This generic method uses reflection to get the query, otherwise everything
   * else is basically the same This method (slightly modified) should probably
   * be put in the EJBUtil class, so all the ValueList things can use it,
   * maximizing our code reuse.
   * @param individualId
   * @param parameters the valuelist paramters for filters and what not.
   * @param primaryTable
   * @param primaryKey
   * @param ownerField
   * @param joinField
   * @param buildQueryObject
   * @param buildQueryMethod
   * @param type
   * @return
   */
  private Collection getData(int individualId, ValueListParameters parameters, String primaryTable,
      String primaryKey, String ownerField, String joinField, Object buildQueryObject,
      Method buildQueryMethod, String type, int moduleId)
  {
    // First: look at the parameters and pick out the right data
    // permissionSwitch turns the permission parts of the query on and off.
    // if individualID is less than zero then the list is requested without
    // limiting
    // rows based on record rights. If it is true than the rights are used.
    boolean permissionSwitch = individualId < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TEMPORARY TABLE permissionfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    // use the filter builder to get the right Ids
    if (applyFilter) {
      EJBUtil.buildListFilterTable(cvdl, "permissionfilter", individualId, moduleId, primaryTable,
          primaryKey, ownerField, joinField, permissionSwitch);
    } else if (permissionSwitch) {
      EJBUtil.buildListFilterTable(cvdl, null, individualId, moduleId, primaryTable, primaryKey,
          ownerField, joinField, permissionSwitch);
    }
    // then get all the columns we need for the Ids that we filter down to.
    Object[] dynamicParameters = { new Boolean(applyFilter || permissionSwitch), type };
    String query = null;
    try {
      query = (String)buildQueryMethod.invoke(buildQueryObject, dynamicParameters);
    } catch (Exception e) {
      logger.error("[getData] Exception thrown.", e);
      cvdl.destroy();
      cvdl = null;
    }
    cvdl.setSqlQuery(query);
    Collection queryData = cvdl.executeQuery();
    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE permissionfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return queryData;
  }
} // end ChartEJB class definition
