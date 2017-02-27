/*
 * $RCSfile: Chart.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:53 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.valuelist.ValueListParameters;

/**
 * ValueList interface provides remote access to retrieve
 * lists of data, by passing in a list request (ViewParameters)
 * and a ValueListPage is returned.
 *
 * @author Kevin McAllister <kevin@centraview.com>
 */
public interface Chart extends EJBObject
{
  /**
   * Allows the client to set the private dataSource.
   *
   * @param ds The cannonical JNDI name of the datasource.
   *
   * @throws RemoteException This exception is defined in the
   * method signature to provide backward compatibility
   * for applications written for the EJB 1.0 specification.
   * Enterprise beans written for the EJB 1.1 specification
   * should throw the javax.ejb.EJBException instead of this
   * exception. Enterprise beans written for the EJB2.0 and
   * higher specifications must throw the javax.ejb.EJBException
   * instead of this exception.
   */
  public void setDataSource(String ds) throws RemoteException;
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
  public HashMap getOpportunityChartData(int individualId, ValueListParameters paramters) throws RemoteException;
  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a pie chart
   * on the Sales dashboard. The fields available are "estimatedClose"
   * "stageName" "count" "forecastAmmount" "actualAmmount"
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getOpportunityPieData(int individualId, ValueListParameters parameters) throws RemoteException;
  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a bar chart on the
   * Sales dashboard. The fields available are "stageName" "count"
   * "forecastAmmount" "actualAmmount"
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getOpportunityBarData(int individualId, ValueListParameters parameters) throws RemoteException;
  /**
   * This method delegates to getTicketPieData and getTicketBarData
   * and packages the results into a HashMap which contains two keys and
   * associated objects the HashMap keys are "pie" and "bar". To find the keys
   * to the pie and bar results review the JavaDoc for the methods that do the
   * real work
   * @param individualId
   * @param paramters ValueListParameters for the list generated.
   * @return HashMap with two Keys "pie" and "bar" that contain the data for the charts.
   */
  public HashMap getTicketChartData(int individualId, ValueListParameters paramters) throws RemoteException;
  /**
   * This method returns the results of a query, a Collection of HashMaps
   * (Rows). Which provides the data necessary for building a pie graph
   * on the Support dashboard. The fields available are "name" and "count"
   * @param individualId the individualId of the user, for permissions.
   * @param parameters the ValueListParameters object.
   * @return a Collection of HashMaps.
   */
  public Collection getTicketPieData(int individualId, ValueListParameters parameters) throws RemoteException;
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
  public Collection getTicketBarData(int individualId, ValueListParameters parameters) throws RemoteException;

} //end of ValueList interface