/*
 * $RCSfile: AdminListEJB.java,v $    $Revision: 1.4 $  $Date: 2005/07/18 21:04:54 $ - $Author: mcallist $
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

package com.centraview.administration.adminlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;
import com.centraview.common.EJBUtil;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This is the EJB class for User
 * The Logic for methods defined in Remote interface
 * is defined in this class
 *
 *@author CentraView, LLC.
 */
public class AdminListEJB implements SessionBean
{
  protected SessionContext ctx;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  public void ejbCreate() { }
  public void ejbRemove() { }
  public void ejbActivate() { }
  public void ejbPassivate() { }

  public ValueListVO getCustomViewList(int individualId, ValueListParameters parameters)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      ArrayList list = new ArrayList();
      
      // permissionSwitch turns the permission parts of the query on and off.
      // if individualID is less than zero then the list is requested without limiting
      // rows based on record rights.  If it is true than the rights are used.
      boolean permissionSwitch = false;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE customviewlistfilter " + filter;
        cvdal.setSqlQuery(str);
        cvdal.executeUpdate();
        cvdal.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      if (applyFilter) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdal, "customviewlistfilter", individualId, 67, "listviews", "viewid", "ownerid", null, permissionSwitch);
      }
      parameters.setTotalRecords(numberOfRecords);


      StringBuffer query = new StringBuffer("");
      query.append("SELECT lv.viewid AS viewid, lv.viewname AS viewname, m2.name AS module, lv.listtype AS record ");
      query.append("FROM listviews lv, defaultviews df, listtypes lt, module m1, module m2 ");
      if (applyFilter) { query.append(", customviewlistfilter AS lf "); }
      query.append("WHERE df.viewid != lv.viewid AND lv.listtype = df.listtype AND ");
      query.append("lt.typename = lv.listtype AND lt.moduleid = m1.moduleid AND ");
      query.append("m1.parentid = m2.moduleid ");
      if (applyFilter) { query.append("AND m2.moduleid = lf.moduleid "); }
      query.append("UNION ");
      query.append("SELECT lv.viewid AS viewid, lv.viewname AS viewname, m2.name AS module, lv.listtype AS record ");
      query.append("FROM listviews lv, defaultviews df, listtypes lt, module m2 ");
      if (applyFilter) { query.append(", customviewlistfilter AS lf "); }
      query.append("WHERE df.viewid != lv.viewid AND lv.listtype=df.listtype AND ");
      query.append("lt.typename = lv.listtype AND lt.moduleid = m2.moduleid ");
      if (applyFilter) { query.append("AND m2.moduleid = lf.moduleid "); }
      query.append("ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection()));
      query.append(parameters.getLimitParam());

      cvdal.setSqlQuery(query.toString());
      list = cvdal.executeQueryList(1);
      cvdal.setSqlQueryToNull();
      
      if (applyFilter){
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery("DROP TABLE customviewlistfilter");
        cvdal.executeUpdate();
      }
      if (applyFilter || permissionSwitch) {
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery("DROP TABLE listfilter");
        cvdal.executeUpdate();
      }
      return new ValueListVO(list, parameters);
    } catch (Exception e) {
      System.out.println("[getReportList] Exception thrown."+ e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }

  }   // end getCustomViewList() method

  
  public ValueListVO getAtticList(int individualId, ValueListParameters parameters)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      ArrayList list = new ArrayList();
      
      // permissionSwitch turns the permission parts of the query on and off.
      // if individualID is less than zero then the list is requested without limiting
      // rows based on record rights.  If it is true than the rights are used.
      boolean permissionSwitch = false;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE atticlistfilter " + filter;
        cvdal.setSqlQuery(str);
        cvdal.executeUpdate();
        cvdal.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      parameters.setTotalRecords(numberOfRecords);

      StringBuffer query = new StringBuffer("");

      query.append("SELECT at.atticid AS atticid, at.recordtitle AS title, m.name AS module, ");
      query.append("cv.name AS  record, CONCAT(owner.FirstName, ' ', owner.LastName) AS Owner, ");
      query.append("CONCAT(deletor.FirstName, ' ', deletor.LastName) AS deletedby, at.owner AS ownerid, ");
      query.append("at.deletedby AS deletorid, owner.individualid AS individualid ");
      query.append("FROM attic at LEFT OUTER JOIN individual owner ON (owner.individualid = at.owner), ");
      query.append("module m, cvtable cv LEFT OUTER JOIN individual deletor ON (deletor.individualid = at.deletedby) ");
      if (applyFilter){ query.append(", atticlistfilter alf "); }
      query.append("WHERE at.moduleid = m.moduleid AND at.record = cv.tableid AND at.dumpType='CV_ATTIC' ");
      if (applyFilter) { query.append("AND at.atticid = alf.atticid "); }
      query.append("ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection()));
      query.append(parameters.getLimitParam());

      cvdal.setSqlQuery(query.toString());
      list = cvdal.executeQueryList(1);
      cvdal.setSqlQueryToNull();
      
      if (applyFilter){
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery("DROP TABLE atticlistfilter");
        cvdal.executeUpdate();
      }
      return new ValueListVO(list, parameters);
    } catch (Exception e) {
      System.out.println("[getReportList] Exception thrown."+ e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }
  }   // end getAtticList() method


  public ValueListVO getGarbageList(int individualId, ValueListParameters parameters)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      ArrayList list = new ArrayList();
      
      // permissionSwitch turns the permission parts of the query on and off.
      // if individualID is less than zero then the list is requested without limiting
      // rows based on record rights.  If it is true than the rights are used.
      boolean permissionSwitch = false;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE garbagelistfilter " + filter;
        cvdal.setSqlQuery(str);
        cvdal.executeUpdate();
        cvdal.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;

      StringBuffer query = new StringBuffer("");
      query.append("CREATE TEMPORARY TABLE garbagelist SELECT at.atticid AS atticid, at.recordtitle AS title, m.name AS module, ");
      query.append("cv.name AS record, CONCAT(indv.FirstName, ' ', indv.LastName) AS Owner, ");
      query.append("CONCAT(indv1.FirstName, ' ', indv1.LastName) AS deletedby, at.owner AS ownerid, ");
      query.append("at.deletedby AS deletorid, indv.individualid AS individualid ");
      query.append("FROM attic at LEFT OUTER JOIN individual indv ON (indv.individualid = at.owner), ");
      query.append("module m, cvtable cv LEFT OUTER JOIN individual indv1 ON (indv1.individualid = at.deletedby) ");
      if (applyFilter){ query.append(", garbagelistfilter glf "); }
      query.append("WHERE at.moduleid = m.moduleid AND at.record = cv.tableid AND at.dumpType='CV_GARBAGE' ");
      if (applyFilter) { query.append("AND at.atticid = glf.atticid "); }
      query.append("ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection()));
      query.append(parameters.getLimitParam());

      cvdal.setSqlQuery(query.toString());
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();
      
      cvdal.setSqlQuery("SELECT * from garbagelist");
      list = cvdal.executeQueryList(1);
      cvdal.setSqlQueryToNull();

      cvdal.setSqlQuery("SELECT count(*) AS NoOfRecords FROM garbagelist");
      Collection colList = cvdal.executeQuery();
      cvdal.clearParameters();
      cvdal.setSqlQueryToNull();

	  if (colList != null)
	  {
	   Iterator it = colList.iterator();
	   if (it.hasNext())
	   {
	     HashMap hm = (HashMap)it.next();
	     numberOfRecords = ((Number)hm.get("NoOfRecords")).intValue();
	   }//end of if (it.hasNext())
	  }//end of if (colList != null)
	 
      parameters.setTotalRecords(numberOfRecords);

      cvdal.setSqlQueryToNull();
      cvdal.setSqlQuery("DROP TABLE garbagelist");
      cvdal.executeUpdate();

      if (applyFilter){
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery("DROP TABLE garbagelistfilter");
        cvdal.executeUpdate();
      }
      return new ValueListVO(list, parameters);
    } catch (Exception e) {
      System.out.println("[getReportList] Exception thrown."+ e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }
  }   // end getGarbageList() method


  public ValueListVO getHistoryList(int individualId, ValueListParameters parameters)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      ArrayList list = new ArrayList();
      
      // permissionSwitch turns the permission parts of the query on and off.
      // if individualID is less than zero then the list is requested without limiting
      // rows based on record rights.  If it is true than the rights are used.
      boolean permissionSwitch = false;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE historylistfilter " + filter;
        cvdal.setSqlQuery(str);
        cvdal.executeUpdate();
        cvdal.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      parameters.setTotalRecords(numberOfRecords);

      StringBuffer query = new StringBuffer("");
      
      query.append("SELECT h.historyid, h.date, CONCAT(u.firstName, ' ', u.lastName) AS user, ");
      query.append("ht.historytype AS action, m.name as recordtype, h.recordName ");
      query.append("FROM history h LEFT JOIN historytype ht ON (h.operation = ht.historytypeid) ");
      query.append("LEFT JOIN individual u ON (h.individualid = u.individualid) ");
      query.append("LEFT JOIN module m ON (h.recordtypeid = m.moduleid) ");
      if (applyFilter){ query.append(", historylistfilter hlf "); }
      query.append("ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection()));
      query.append(parameters.getLimitParam());

      cvdal.setSqlQuery(query.toString());
      list = cvdal.executeQueryList(1);
      cvdal.setSqlQueryToNull();
      
      if (applyFilter){
        cvdal.setSqlQueryToNull();
        cvdal.setSqlQuery("DROP TABLE historylistfilter");
        cvdal.executeUpdate();
      }
      return new ValueListVO(list, parameters);
    } catch (Exception e) {
      System.out.println("[getReportList] Exception thrown."+ e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }
  }   // end getHistoryList() method

  
  public ValueListVO getHistoryValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE historylistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
    }
    int numberOfRecords = 0;
    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "historylistfilter", individualId, 0, "history", "historyId", "owner", null, false);
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildReportListQuery(parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE historylistfilter");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE listfilter");
    cvdl.executeUpdate();
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildReportListQuery(ValueListParameters parameters)
  {
    StringBuffer query = new StringBuffer();
    query.append("SELECT h.historyId, h.date, CONCAT(i.firstName, ' ', i.lastName) AS user, ht.historyType AS action, m.name AS recordType, h.recordName ");
    query.append("FROM history AS h, historytype AS ht, individual AS i, module AS m, listfilter AS lf ");
    query.append("WHERE m.moduleId = h.recordTypeId AND ht.historyTypeId = h.operation AND h.historyId = lf.historyId AND h.individualid=i.individualid ");
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }


  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  public ValueListVO getLiteratureList(int individualId, ValueListParameters parameters)
  {
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      ArrayList list = new ArrayList();
      StringBuffer query = new StringBuffer();

      query.append("SELECT l.LiteratureID as literatureId, l.Title AS title, f.Title AS file FROM literature l LEFT JOIN cvfile f ON (l.FileID=f.FileID)");
      query.append(" ORDER BY " + parameters.getSortColumn() + " " + parameters.getSortDirection());
      query.append(parameters.getLimitParam());

      cvdl.setSqlQuery(query.toString());
      list = cvdl.executeQueryList(1);

      cvdl.setSqlQueryToNull();

      Number count = new Integer(0);
      cvdl.setSqlQuery("SELECT COUNT(*) AS count FROM literature");
      Collection results = (Collection)cvdl.executeQuery();
      if (results != null && results.size() > 0) {
        Iterator iter = results.iterator();
        while (iter.hasNext()) {
          HashMap row = (HashMap)iter.next();
          count = (Number)row.get("count");
          break;
        }
      } else {
        try {
          count = new Integer(list.size());
        } catch (NumberFormatException nfe) { /* whatever */ }
      }
      parameters.setTotalRecords(count.intValue());
      return new ValueListVO(list, parameters);
    } catch (Exception e) {
      System.out.println("[getReportList] Exception thrown."+ e);
      throw new EJBException(e);
    } finally {
      cvdl.destroy();
    }

  }   // end getLiteratureList method

}
