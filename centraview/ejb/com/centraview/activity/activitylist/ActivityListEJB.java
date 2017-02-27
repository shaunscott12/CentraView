/*
 * $RCSfile: ActivityListEJB.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

package com.centraview.activity.activitylist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;
import com.centraview.common.DDNameValue;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This class is a Stateless Session Bean which returns Data for each call
 */
public class ActivityListEJB implements SessionBean {
  private String dataSource = "";
  SessionContext sc;

  public ActivityListEJB() {}

  public void ejbCreate()
  {}

  public void ejbRemove()
  {}

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public void setSessionContext(SessionContext sc)
  {
    this.sc = sc;
  }

  public Vector getAllResources()
  {
    Vector resources = new Vector();
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      cvdal.setSql("activity.getallresources");
      Collection results = cvdal.executeQuery();
      Iterator iter = results.iterator();
      while (iter.hasNext()) {
        HashMap row = (HashMap)iter.next();
        Number activityResourceID = (Number)row.get("ActivityResourceID");
        DDNameValue dnv = new DDNameValue(activityResourceID.toString() + "#"
            + (String)row.get("Name"), (String)row.get("Name"));
        dnv.setId(activityResourceID.intValue());
        resources.add(dnv);
      }
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return resources;
  } // end getAllResources() method

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
   * Returns a ValueListVO representing a list of Activity records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getActivityValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE activitylistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    String query = this.buildActivityListQuery(applyFilter, individualId, cvdl, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE allactivitylist");
    cvdl.executeUpdate();
    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE activitylistfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildActivityListQuery(boolean applyFilter, int individualId, CVDal cvdl,
      ValueListParameters parameters)
  {
    // Create table column definitions
    String create = "CREATE TEMPORARY TABLE allactivitylist ";
    String select = "SELECT a.ActivityID ActivityID, a.Title Title, at.Name Type, "
        + "i.IndividualId CreatorIndividualId, i.FirstName CreatorFirstName, "
        + "i.LastName CreatorLastName, i.FirstName AssocFirstName, i.LastName "
        + "AssocLastName, e.Name AssocEntityName, a.Owner OwnerId, i.FirstName "
        + "OwnerFirstName, i.LastName OwnerLastName, a.Notes Notes, a.Created "
        + "Created, a.Start Start, a.End End, a.Details Details, ap.Name Priority, "
        + "ast.Name Status, a.visibility Visibility, i.individualId "
        + "AssocIndividualId, e.entityId AssocEntityId, ct.name CallType ";
    String from = "FROM activity a, activitystatus ast, activitypriority ap, "
        + "activitytype at, individual i, entity e, calltype ct ";
    String where = "WHERE 1 = 0";
    String query = create + select + from + where;
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    // Populate all activities
    String insert = "INSERT INTO allactivitylist(ActivityID, Title, Created, Details, "
        + "Start, End, Visibility, OwnerId, Notes) ";
    select = "SELECT a.ActivityID, a.Title, a.Created, a.Details, a.Start, "
        + "a.End, a.visibility, a.Owner AS OwnerId, a.Notes ";
    from = "FROM activity a, attendee at ";
    where = "WHERE at.activityId = a.activityId and at.individualid = '" + individualId + "' ";
    if (applyFilter) {
      from += ", activitylistfilter alf ";
      where += " AND alf.ActivityId = a.ActivityId ";
    }
    query = insert + select + from + where + "UNION ";
    select = "SELECT a.ActivityID, a.Title, a.Created, a.Details, a.Start, "
        + "a.End, a.visibility, a.Owner AS OwnerId, a.Notes ";
    from = "FROM activity a ";
    where = "WHERE a.Owner = '" + individualId + "' ";
    if (applyFilter) {
      from += ", activitylistfilter alf ";
      where += " AND alf.ActivityId = a.ActivityId ";
    }
    query += select + from + where + "UNION ";
    select = "SELECT a.ActivityID, a.Title, a.Created, a.Details, a.Start, "
        + "a.End, a.visibility, a.Owner AS OwnerId, a.Notes ";
    from = "FROM activity a, taskassigned ta, task t ";
    where = "WHERE ta.taskid = t.activityid AND t.ActivityID = a.ActivityID AND "
        + "ta.AssignedTo = '" + individualId + "' ";
    if (applyFilter) {
      from += ", activitylistfilter alf ";
      where += " AND alf.ActivityId = a.ActivityId ";
    }
    query += select + from + where;
    // Query for opportunity table
    from = "FROM activity a, opportunity o ";
    where = "WHERE o.ActivityID = a.ActivityID ";
    if (applyFilter) {
      from += ", activitylistfilter alf ";
      where += " AND alf.ActivityId = a.ActivityId ";
    }
    query += " UNION " + select + from + where;
    cvdl.setSqlQuery(query);
    int count = cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    parameters.setTotalRecords(count);
    // Various updates
    cvdl.setSqlQuery("UPDATE allactivitylist aal, activity a, activitystatus "
        + "ast SET aal.Status = ast.Name WHERE ast.StatusID = a.Status AND "
        + "a.ActivityID = aal.ActivityID");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, activity a, "
        + "activitypriority ap SET aal.Priority = ap.Name WHERE ap.PriorityID = "
        + "a.Priority AND a.ActivityID = aal.ActivityID");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, activity a, activitytype at "
        + "SET aal.Type = at.Name WHERE at.TypeID = a.Type AND a.ActivityID = " + "aal.ActivityID");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, activity a, individual i "
        + "SET aal.CreatorIndividualId = i.IndividualId, aal.CreatorFirstName = "
        + "i.FirstName, aal.CreatorLastName = i.LastName WHERE i.IndividualID = "
        + "a.creator AND a.ActivityID = aal.ActivityID");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, activitylink al, individual i "
        + "SET aal.AssocFirstName = i.FirstName, aal.AssocLastName = i.LastName, "
        + "aal.AssocIndividualId = i.individualId WHERE aal.ActivityId = "
        + "al.ActivityId AND i.individualId = al.RecordId AND al.RecordTypeId = " + "'2'"); // 2 is
    // individual
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, activitylink al, entity e "
        + "SET aal.AssocEntityName = e.Name, aal.AssocEntityId = e.entityId "
        + "WHERE aal.ActivityId = al.ActivityId AND e.entityId = al.RecordId AND "
        + "al.RecordTypeId = '1'"); // 1 is entity
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, individual i SET "
        + "aal.OwnerFirstName = i.FirstName, aal.OwnerLastName = i.LastName "
        + "WHERE aal.OwnerId = i.IndividualId");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE allactivitylist aal, call c, calltype ct SET "
        + "aal.CallType = ct.Name WHERE aal.ActivityId = c.ActivityId AND c.CallType = "
        + "ct.CallTypeID");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    // Order and limit
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    // Finally, the query.
    query = "SELECT ActivityId, Title, Status, CreatorIndividualId, Created, Type, "
        + "Details, CONCAT(CreatorFirstName, ' ', CreatorLastName) AS CreatedBy, "
        + "Priority, Start, End, Visibility, CONCAT(AssocFirstName, ' ', "
        + "AssocLastName) AS AssocIndividualName, AssocEntityName, AssocIndividualId, "
        + "AssocEntityId, OwnerId, CONCAT(OwnerFirstName, ' ', OwnerLastName) AS "
        + "OwnerName, Notes, CallType FROM allactivitylist " + orderBy + limit;
    return (query);
  }
}