/*
 * $RCSfile: EJBUtil.java,v $    $Revision: 1.3 $  $Date: 2005/08/04 15:15:47 $ - $Author: mcallist $
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

package com.centraview.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

/**
 * Utility class for our EJB layer. Created: Sep 8, 2004
 * @author CentraView, LLC.
 */
public class EJBUtil {
  private static Logger logger = Logger.getLogger(EJBUtil.class);

  /**
   * Creates a table to join against for permissions. Some notes: If ownerField
   * is in the form of "table.field" then it will be split on '.' and the
   * primaryTable will be joined against "table" to get the owner. Also, if the
   * above is true, joinField must not be null. It should contain the name of
   * the field in the secordary table to match against. This is a hack to get
   * Opportunities working, hopefully it will be useful elsewhere.
   * @param cvdal
   * @param filterTable
   * @param individualId
   * @param moduleId
   * @param primaryTable
   * @param primaryKey
   * @param ownerField
   * @param joinField
   * @param permissionSwitch
   * @return Number of records
   */

  public static int buildListFilterTable(CVDal cvdal, String filterTable, int individualId, int moduleId, String primaryTable, String primaryKey,
      String ownerField, String joinField, boolean permissionSwitch)
  {
    boolean applyFilter = (filterTable != null);

    cvdal.clearParameters();
    // get the user type of the given individual id.
    cvdal.setSql("user.getUserType");
    cvdal.setInt(1, individualId);
    Collection userResults = cvdal.executeQuery();

    // We have to check wheather the Logged in user is an admin User
    // IF logged in user is ADMINSTRATOR
    // then we have to assign a false value to permissionSwitch
    // Else we will not change the value for permissionSwitch
    String userType = new String("");
    if (userResults != null) {
      Iterator userIter = userResults.iterator();
      while (userIter.hasNext()) {
        HashMap userRow = (HashMap) userIter.next();
        if (userRow != null) {
          userType = (String) userRow.get("usertype");
          if (userType != null && userType.equals("ADMINISTRATOR")) {
            permissionSwitch = false;
          }
          break;
        }
      }
    }

    String secondaryTable = null;
    if (ownerField.indexOf('.') > 0) {
      String ar[] = ownerField.split("\\.", 2);
      secondaryTable = ar[0];
      ownerField = ar[1];
    }
    cvdal.setSqlQueryToNull();
    String select = "SELECT " + "pt." + primaryKey;
    String from = " FROM " + primaryTable + " AS pt";
    String where = " WHERE 1 = 1 ";

    if (secondaryTable != null && joinField != null) {
      from += ", " + secondaryTable + " AS st";
      where += "AND pt." + joinField + " = " + "st." + joinField + " ";
    }

    if (applyFilter) {
      from += ", " + filterTable + " AS ft";
      where += "AND pt." + primaryKey + " = ft." + primaryKey;
    }
    StringBuffer query = new StringBuffer();
    query.append("CREATE TABLE listfilter ");
    query.append(select);
    query.append(from);
    query.append(where);
    if (permissionSwitch) {
      String ownerWhere = " AND pt." + ownerField + " = " + individualId;
      if (secondaryTable != null) {
        ownerWhere = " AND st." + ownerField + " = " + individualId;
      }
      String prFrom = ", publicrecords AS pub";
      String prWhere = " AND pub.recordID = pt." + primaryKey + " AND pub.moduleID = " + moduleId;
      String raFrom = ", recordauthorisation AS auth";
      String raWhere = " AND auth.recordID = pt." + primaryKey + " AND auth.recordTypeID = " + moduleId + " AND auth.individualID = " + individualId
          + " AND auth.privilegeLevel < 40 AND auth.privilegeLevel > 0";
      query.append(ownerWhere);
      query.append(" UNION ");
      query.append(select);
      query.append(from);
      query.append(prFrom);
      query.append(where);
      query.append(prWhere);
      query.append(" UNION ");
      query.append(select);
      query.append(from);
      query.append(raFrom);
      query.append(where);
      query.append(raWhere);
    }
    cvdal.setSqlQuery(query.toString());
    int count = cvdal.executeUpdate();
    cvdal.setSqlQueryToNull();
    return count;
  }

  /**
   * This method takes a field to count and a table to count on a valid CVDal
   * object (preferably a non-destroyed one) and runs the simple query pulling
   * out the result and returning it. This method calls setSqlQuerytoNull before
   * and after doing anything else.
   * @param cvdal non-destroyed CVDal object.
   * @param field the field to count, * is valid
   * @param table the table to count field on.
   * @return the number of rows.
   * @throws EJBException If I can't 'SELECT count(*) ON TABLE' then things are
   *           in a bad way.
   */
  public static int getRowCount(CVDal cvdal, String field, String table) throws EJBException
  {
    int numberOfRecords = 0;
    cvdal.setSqlQueryToNull();
    String query = "SELECT COUNT(" + field + ") FROM " + table;
    cvdal.setSqlQuery(query);
    ResultSet countRS = cvdal.executeQueryNonParsed();
    try {
      if (countRS.next()) {
        numberOfRecords = countRS.getInt(1);
      }
    } catch (SQLException se) {
      logger.error("[getTemplateValueList] Exception getting Template count.", se);
      // well if I can't get a count of records, I sure as hell won't be able
      // to get the records themselves. Lets just call it a day.
      throw new EJBException(se);
    } finally {
      // close the result set.
      try {
        countRS.close();
      } catch (SQLException e) {
        // Some PC BS comment about how we are helpless here, but it's cool
        // anyway.
      }
      cvdal.setSqlQueryToNull();
    }
    return numberOfRecords;
  } // end getRowCount

  /**
   * Creates a table to join against for permissions. Some notes: If ownerField
   * is in the form of "table.field" then it will be split on '.' and the
   * primaryTable will be joined against "table" to get the owner. Also, if the
   * above is true, joinField must not be null. It should contain the name of
   * the field in the secordary table to match against. This is a hack to get
   * Opportunities working, hopefully it will be useful elsewhere. We will just
   * build the query which should apply on the table and get us the correct
   * activity ID
   * @param individualId to identify which user is logged in
   * @param moduleId
   * @param primaryTable
   * @param primaryKey
   * @param ownerField
   * @param joinField
   * @return build the query to apply the permission
   */
  public static String buildListFilterQuery(int individualId, int moduleId, String primaryTable, String primaryKey, String ownerField,
      String joinField)
  {

    String secondaryTable = null;
    if (ownerField.indexOf('.') > 0) {
      String ar[] = ownerField.split("\\.", 2);
      secondaryTable = ar[0];
      ownerField = ar[1];
    }
    String select = "SELECT " + "pt." + joinField;
    String from = " FROM " + primaryTable + " AS pt";
    String where = " WHERE 1 = 1 ";

    if (secondaryTable != null && joinField != null) {
      from += ", " + secondaryTable + " AS st";
      where += "AND pt." + joinField + " = " + "st." + joinField + " ";
    }

    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    String ownerWhere = " AND pt." + ownerField + " = " + individualId;
    if (secondaryTable != null) {
      ownerWhere = " AND st." + ownerField + " = " + individualId;
    }
    String prFrom = ", publicrecords AS pub";
    String prWhere = " AND pub.recordID = pt." + primaryKey + " AND pub.moduleID = " + moduleId;
    String raFrom = ", recordauthorisation AS auth";
    String raWhere = " AND auth.recordID = pt." + primaryKey + " AND auth.recordTypeID = " + moduleId + " AND auth.individualID = " + individualId
        + " AND auth.privilegeLevel < 40 AND auth.privilegeLevel > 0";
    query.append(ownerWhere);
    query.append(" UNION ");
    query.append(select);
    query.append(from);
    query.append(prFrom);
    query.append(where);
    query.append(prWhere);
    query.append(" UNION ");
    query.append(select);
    query.append(from);
    query.append(raFrom);
    query.append(where);
    query.append(raWhere);
    return query.toString();
  }
}
