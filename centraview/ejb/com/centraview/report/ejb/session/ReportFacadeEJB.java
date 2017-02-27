/*
 * $RCSfile: ReportFacadeEJB.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 19:37:19 $ - $Author: mcallist $
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

package com.centraview.report.ejb.session;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.advancedsearch.AdvancedSearchLocal;
import com.centraview.advancedsearch.AdvancedSearchLocalHome;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVDal;
import com.centraview.common.ListElementMember;
import com.centraview.report.builder.Activities1ReportBuilder;
import com.centraview.report.builder.Contacts1ReportBuilder;
import com.centraview.report.builder.Hr1ReportBuilder;
import com.centraview.report.builder.Hr2ReportBuilder;
import com.centraview.report.builder.Hr3ReportBuilder;
import com.centraview.report.builder.IndividualNoteStandardReport;
import com.centraview.report.builder.Project1ReportBuilder;
import com.centraview.report.builder.ReportBuilder;
import com.centraview.report.builder.Sales1ReportBuilder;
import com.centraview.report.builder.Sales3ReportBuilder;
import com.centraview.report.builder.Sales4ReportBuilder;
import com.centraview.report.builder.Support1ReportBuilder;
import com.centraview.report.ejb.entity.EntityHomeFactory;
import com.centraview.report.ejb.entity.ReportContentLocal;
import com.centraview.report.ejb.entity.ReportContentLocalHome;
import com.centraview.report.ejb.entity.ReportLocal;
import com.centraview.report.ejb.entity.ReportLocalHome;
import com.centraview.report.ejb.entity.ReportPK;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportContentString;
import com.centraview.report.valueobject.ReportContentVO;
import com.centraview.report.valueobject.ReportResultVO;
import com.centraview.report.valueobject.ReportVO;
import com.centraview.report.valueobject.SelectVO;
import com.centraview.report.valueobject.TheItem;
import com.centraview.report.valueobject.TheTable;
import com.centraview.report.valueobject.WhereClauseVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * <p>
 * Title: ReportFacadeEJB session bean
 * </p>
 * <p>
 * Description: The ReportFacadeEJB class is a session Enterprise JavaBean (EJB)
 * for manipulation of Report data. Honors the methods declared in the
 * ReportFacade interface.
 * </p>
 * <p>
 * The deployment descriptor should mark this bean as a stateless session EJB.
 * <p>
 * @see com.centraview.report.ejb.session.ReportFacade
 * @see com.centraview.report.ejb.session.ReportFacadeHome
 * @see com.centraview.report.ejb.session.ReportFacadeLocal
 * @see com.centraview.report.ejb.session.ReportFacadeLocalHome
 */
public class ReportFacadeEJB extends CVSessionBean {
  private String dataSource = "MySqlDS";

  private static Logger logger = Logger.getLogger(ReportFacadeEJB.class);

  /**
   * Accessor method for getting Standart reports list.
   * @param individualID int
   * @param hashmap HashMap
   * @return ReportList
   */
  public ValueListVO getStandardReportList(int individualId, ValueListParameters parameters)
  {
    try {
      return getReportList(parameters, ReportConstants.STANDARD_REPORT_CODE);
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  public ValueListVO getAdHocReportList(int individualId, ValueListParameters parameters)
  {
    try {
      return getReportList(parameters, ReportConstants.ADHOC_REPORT_CODE);
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  public ValueListVO getReportList(ValueListParameters parameters, int reportType)
  {
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      ArrayList list = new ArrayList();
      StringBuffer query = new StringBuffer();
      query.append("SELECT reportid, name, description, 'View' FROM report WHERE reporttypeid = ? AND moduleid = ?");
      query.append(" ORDER BY " + parameters.getSortColumn() + " " + parameters.getSortDirection());
      query.append(parameters.getLimitParam());
      cvdl.setSqlQuery(query.toString());
      cvdl.setInt(1, reportType);
      cvdl.setInt(2, parameters.getExtraId());
      list = cvdl.executeQueryList(1);
      return new ValueListVO(list, parameters);
    } catch (Exception e) {
      logger.error("[getReportList] Exception thrown.", e);
      throw new EJBException(e);
    } finally {
      cvdl.destroy();
    }
  }

  /**
   * Method to delete reports.
   * @param reportIds int[] array of report ids
   */
  public void delete(int[] reportIds)
  {
    try {
      ReportLocalHome reportHome = EntityHomeFactory.getReportLocalHome();
      ReportLocal report = null;
      int index = 0;
      try {
        for (int i = 0; i < reportIds.length; i++) {
          index = reportIds[i];
          report = reportHome.findByPrimaryKey(new ReportPK(index, this.dataSource));
          report.remove();
        }
      } catch (ObjectNotFoundException e) {
        throw new EJBException("Report object not found, id = " + index);
      }
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method for getting initial AdHoc reports page data.
   * @param moduleId int
   * @return SelectVO
   */
  public SelectVO getAdHocPageData(int moduleId)
  {
    SelectVO selects = new SelectVO();
    try {
      selects.setTopTables(getTables(moduleId));
      return selects;
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method for getting Standart report data.
   * @param reportId int
   * @return ReportVO
   */
  public ReportVO getStandardReport(int userId, int reportId)
  {
    try {
      ReportVO reportVO = new ReportVO();
      ReportLocalHome reportHome = EntityHomeFactory.getReportLocalHome();
      ReportContentLocalHome reportContentHome = EntityHomeFactory.getReportContentLocalHome();
      ReportLocal reportLocal = reportHome.findByPrimaryKey(new ReportPK(reportId, this.dataSource));

      reportVO.setDescription(reportLocal.getDescription());
      reportVO.setModuleId(reportLocal.getModuleId());
      reportVO.setName(reportLocal.getName());
      reportVO.setReportId(reportId);
      reportVO.setFrom(reportLocal.getDateFrom());
      reportVO.setTo(reportLocal.getDateTo());
      reportVO.setSelect(getAdHocPageData(reportLocal.getModuleId()));
      return reportVO;
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method to updatege Standart report.
   * @param userId int
   * @param reportVO ReportVO
   * @return int
   */
  public int updateStandardReport(int userId, ReportVO reportVO, boolean saveName)
  {
    try {
      int reportId = 0;
      ReportLocalHome reportHome = EntityHomeFactory.getReportLocalHome();
      ReportLocal reportLocal = reportHome.findByPrimaryKey(new ReportPK(reportVO.getReportId(), this.dataSource));

      if (saveName) {
        reportLocal.setDescription(reportVO.getDescription());
        reportLocal.setModuleId(reportVO.getModuleId());
        reportLocal.setName(reportVO.getName());
      }

      reportLocal.setDateFrom(reportVO.getFrom());
      reportLocal.setDateTo(reportVO.getTo());

      reportId = reportLocal.getReportId();
      return reportId;
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method to add AdHoc report.
   * @param userId int
   * @param reportVO ReportVO
   * @return int
   */
  public int addAdHocReport(int userId, ReportVO reportVO)
  {
    try {
      ReportLocalHome reportHome = EntityHomeFactory.getReportLocalHome();
      ReportContentLocalHome reportContentHome = EntityHomeFactory.getReportContentLocalHome();
      ReportContentLocal reportContentLocal = null;
      ReportContentVO field = null;
      int n = 0;
      int reportId = 0;
      short seqNumber = 0;
      Byte sortSeq = null;

      Timestamp today = new Timestamp(System.currentTimeMillis());
      ReportLocal reportLocal = reportHome.create(today, reportVO.getFrom(), reportVO.getTo(), reportVO.getDescription(), new Integer(userId), null,
          reportVO.getModuleId(), reportVO.getName(), ReportConstants.ADHOC_REPORT_CODE, null, this.dataSource);
      reportId = reportLocal.getReportId();
      ArrayList selectedFields = reportVO.getSelectedFields();
      n = selectedFields.size();
      for (int i = 0; i < n; i++) {
        field = (ReportContentVO) selectedFields.get(i);
        reportContentLocal = reportContentHome.create(field.getFieldId(), field.getTableId(), reportId, ++seqNumber, field.getSortOrder(), field
            .getSortSeq(), this.dataSource);
      }
      return reportId;
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method for getting AdHoc report.
   * @param reportId int
   * @return ReportVO
   */
  public ReportVO getAdHocReport(int userId, int reportId)
  {
    try {
      int fieldId = 0;
      int tableId = 0;
      String fieldName = null;
      ArrayList selectedTopFields = new ArrayList();

      ReportVO reportVO = getStandardReport(userId, reportId);

      ReportContentLocalHome reportContentHome = EntityHomeFactory.getReportContentLocalHome();
      ReportContentLocal reportContent = null;
      Collection reportContents = reportContentHome.findByReport(reportId, this.dataSource);
      for (Iterator iterator = reportContents.iterator(); iterator.hasNext();) {
        reportContent = (ReportContentLocal) iterator.next();
        tableId = reportContent.getTableId();
        fieldId = reportContent.getFieldId();
        fieldName = getFieldName(fieldId, tableId);
        selectedTopFields.add(new ReportContentVO(fieldId, tableId, fieldName, reportContent.getSortOrderSequence(), reportContent.getSortOrder()));
      }
      reportVO.setSelectedFields(selectedTopFields);
      return reportVO;
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method to updatege AdHoc report data .
   * @param userId int
   * @param reportVO ReportVO
   * @return int
   */
  public int updateAdHocReport(int userId, ReportVO reportVO)
  {
    try {
      int n = 0;
      int reportId = 0;
      short seqNumber = 0;
      ReportContentVO field = null;
      ReportContentLocal reportContentLocal = null;
      ReportContentLocalHome reportContentHome = EntityHomeFactory.getReportContentLocalHome();

      reportId = updateStandardReport(userId, reportVO, true);

      for (Iterator iterator = reportContentHome.findByReport(reportId, this.dataSource).iterator(); iterator.hasNext();) {
        reportContentLocal = (ReportContentLocal) iterator.next();
        reportContentLocal.remove();
      }

      ArrayList selectedFields = reportVO.getSelectedFields();
      n = selectedFields.size();
      for (int i = 0; i < n; i++) {
        field = (ReportContentVO) selectedFields.get(i);
        reportContentLocal = reportContentHome.create(field.getFieldId(), field.getTableId(), reportId, ++seqNumber, field.getSortOrder(), field
            .getSortSeq(), this.dataSource);
      }
      return reportId;
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Accessor method for getting AdHoc report result for view.
   * @param reportId int
   * @return ReportResultVO
   */
  public ReportResultVO getAdHocReportResult(int userId, int reportId, SearchVO search)
  {
    ReportResultVO reportResult = getReportResult(userId, reportId, search);
    return reportResult;
  }

  /**
   * Accessor method for getting a report result for view.
   * @param reportId int
   * @return ReportResultVO
   */
  protected ReportResultVO getReportResult(int userId, int reportId, SearchVO search)
  {
    ReportResultVO reportResult = new ReportResultVO();
    ArrayList results = new ArrayList();
    ArrayList resultLine = null;
    CVDal dataAccessLayer = new CVDal(this.dataSource);
    ResultSet resultSet = null;
    try {
      ReportLocalHome reportHome = EntityHomeFactory.getReportLocalHome();
      ReportLocal reportLocal = reportHome.findByPrimaryKey(new ReportPK(reportId, this.dataSource));
      reportResult.setReportId(reportLocal.getReportId());
      reportResult.setModuleId(reportLocal.getModuleId());
      reportResult.setName(reportLocal.getName());
      reportResult.setSearchCriteria(getSearchCriteriaString(userId, reportId));
      Date dateFrom = reportLocal.getDateFrom();
      Date dateTo = reportLocal.getDateTo();

      try {
        String query = getQueryForResultReport(userId, reportId, search, dataAccessLayer);

        dataAccessLayer.setSqlQuery(query);
        resultSet = dataAccessLayer.executeQueryNonParsed();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int cols = metaData.getColumnCount();
        int i = 0;
        boolean exclude = false;
        boolean[] isDate = new boolean[cols + 1];
        for (i = 1; i <= cols; i++) {
          isDate[i] = false;
        }
        for (i = 1; i <= cols; i++) {
          if ((Types.DATE == metaData.getColumnType(i) || Types.TIMESTAMP == metaData.getColumnType(i))
              && !metaData.getColumnName(i).equalsIgnoreCase("Modified") && !metaData.getColumnName(i).equalsIgnoreCase("Created")) {
            isDate[i] = true;
          }
        } // end for ( i = 1; i <= cols; i++)

        while (resultSet.next()) {
          resultLine = new ArrayList();
          exclude = false;
          for (i = 1; i <= cols; i++) {
            if (isDate[i]) {
              Date dateField = resultSet.getDate(i);
              if (dateField != null) {
                if (dateFrom != null && dateFrom.after(dateField)) {
                  exclude = true;
                }
                if (dateTo != null && dateTo.before(dateField)) {
                  exclude = true;
                }
              } // end if (dateField != null)
            } // end if (isDate[i])
          } // end for (i = 1; i <= cols; i++)

          if (exclude) {
            continue;
          }

          for (i = 1; i <= cols; i++) {
            resultLine.add(resultSet.getString(i));
          }
          results.add(resultLine);
        }
      } catch (SQLException se) {
        logger.error("[Exception][ReportFacadeEJB.getReportResult] Exception Thrown: " + se);
        logger.error("> Caused by: " + se);
        throw new EJBException(se);
      }
      reportResult.setTitles(getTitles(reportId));
      reportResult.setResults(results);
      return reportResult;
    } catch (Exception e) {
      throw new EJBException(e);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          // Not much we can do at this point.
        }
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
  }

  /**
   * Accessor method for getting field name.
   * @param fieldId int
   * @return String
   */
  private String getFieldName(int fieldId, int tableId)
  {
    String name = "";
    CVDal dataAccessLayer = new CVDal(this.dataSource);
    ResultSet resultSet = null;
    try {
      if (SearchVO.CUSTOM_FIELD_TABLEID.equals(String.valueOf(tableId))) {
        dataAccessLayer.setSqlQuery("SELECT 'Custom Fields', 'intentionallyblank', name FROM customfield WHERE customFieldId = ?");
      } else {
        // select searchtable.tablename, searchfield.fieldname,
        // searchfield.displayname from searchtable, searchfield where
        // searchfield.searchfieldid=? and searchfield.searchtableid =
        // searchtable.searchtableid
        dataAccessLayer.setSql("reports.getfieldname");
      }
      dataAccessLayer.setInt(1, fieldId);
      resultSet = dataAccessLayer.executeQueryNonParsed();
      while (resultSet.next()) {
        name = resultSet.getString(1) + "." + resultSet.getString(3);
      }
    } catch (SQLException e) {
      logger.error("[Exception][ReportFacadeEJB.getFieldName] Exception Thrown: " + e);
      throw new EJBException("Error executing SQL : " + e.toString());
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
    return name;
  }

  /**
   * Accessor method for getting column titles for report result.
   * @param reportId int
   * @return ArrayList
   */
  public ArrayList getTitles(int reportId)
  {
    ArrayList titles = new ArrayList();
    CVDal dataAccessLayer = new CVDal(this.dataSource);
    ResultSet resultSet = null;
    ResultSet customFieldNameRS = null;
    try {
      // select searchfield.displayname, reportcontent.tableid,
      // reportcontent.fieldid
      // from reportcontent, searchfield
      // where reportcontent.reportid = ? and
      // reportcontent.fieldid=searchfield.searchfieldid
      dataAccessLayer.setSql("reports.gettitles");
      dataAccessLayer.setInt(1, reportId);
      resultSet = dataAccessLayer.executeQueryNonParsed();
      String tableId = "";
      int fieldId;
      while (resultSet.next()) {
        tableId = resultSet.getString(2);
        fieldId = resultSet.getInt(3);
        if (SearchVO.CUSTOM_FIELD_TABLEID.equals(tableId)) {
          // We need to get the customfield name from the
          // custom field table.
          CVDal cvdal = new CVDal(this.dataSource);
          try {
            cvdal.setSqlQuery("SELECT name FROM customfield WHERE customFieldId = ?");
            // the third thing returned on the result set is the fieldId
            // in this case the customfieldid, to get the name.
            cvdal.setInt(1, fieldId);
            customFieldNameRS = cvdal.executeQueryNonParsed();
            if (customFieldNameRS.next()) {
              titles.add(customFieldNameRS.getString(1));
            } else {
              titles.add("Result Column");
            }
          } finally {
            if (customFieldNameRS != null) {
              try {
                customFieldNameRS.close();
              } catch (SQLException e) {}
            }
            cvdal.destroy();
            cvdal = null;
          }
        } else {
          titles.add(resultSet.getString(1));
        }
      }
      return titles;
    } catch (Exception e) {
      throw new EJBException(e);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
  }

  /**
   * This method takes the resultset from the queryCollection query
   * reports.selectclause and stuffs it in an ArrayList of WhereClauseVOs It
   * also must be smart when dealing with customfields.
   * @param rs
   * @return
   */
  private ArrayList convertRStoWhereClauseVO(ResultSet rs)
  {
    ArrayList whereClauses = new ArrayList();
    WhereClauseVO wcvo = null;
    int cfCounter = 0;
    try {
      while (rs.next()) {
        String tableId = rs.getString(4);
        int fieldId = rs.getInt(8);
        wcvo = new WhereClauseVO();
        // if it is a custom field rig up the WhereClauseVO
        if (tableId.equals(SearchVO.CUSTOM_FIELD_TABLEID)) {
          cfCounter++;
          // We are a custom field criteria
          // Find out if we are scalar or multiple.
          CVDal cvdal = new CVDal(this.dataSource);
          try {
            String customFieldTypeQuery = "SELECT fieldType, recordType, name FROM customfield WHERE customFieldId = ?";
            cvdal.setSqlQuery(customFieldTypeQuery);
            cvdal.setInt(1, fieldId);
            Collection typeResults = cvdal.executeQuery();
            cvdal.setSqlQueryToNull();
            // RecordType will help us build the relationship query
            String customFieldType = null;
            Number recordType = null;
            String customFieldName = null;
            if (typeResults != null) {
              Iterator typeIterator = typeResults.iterator();
              if (typeIterator.hasNext()) {
                HashMap type = (HashMap) typeIterator.next();
                customFieldType = (String) type.get("fieldType");
                recordType = (Number) type.get("recordType");
                customFieldName = (String) type.get("name");
              } // end if (typeResults != null)
            } // end if (typeIterator.hasNext())
            // get the moduleId from the customfield recordtype
            cvdal.setSqlQueryToNull();
            cvdal.setSqlQuery("SELECT moduleid FROM cvtable WHERE tableid = ?");
            cvdal.setInt(1, recordType.intValue());
            Collection moduleCollection = cvdal.executeQuery();
            Iterator moduleIterator = moduleCollection.iterator();
            HashMap moduleMap = (HashMap) moduleIterator.next();
            Number moduleId = (Number) moduleMap.get("moduleid");
            // Use moduleId to get the primary table and primarykey field to
            // join against.
            InitialContext ctx = new InitialContext();
            Object oh = ctx.lookup("local/AdvancedSearch");
            AdvancedSearchLocalHome cfh = (AdvancedSearchLocalHome) javax.rmi.PortableRemoteObject.narrow(oh,
                com.centraview.advancedsearch.AdvancedSearchLocalHome.class);
            AdvancedSearchLocal localAdvancedSearch = (AdvancedSearchLocal) cfh.create();
            localAdvancedSearch.setDataSource(this.dataSource);
            HashMap primaryTableMap = localAdvancedSearch.getPrimaryTableForModule(moduleId.intValue());
            if (customFieldType.equals("SCALAR")) {
              wcvo.setFieldName("customfieldscalar" + cfCounter + ".value");
              wcvo.setRelationshipQuery("customfieldscalar" + cfCounter + ".customfieldId = " + rs.getInt(8) + " AND customfieldscalar" + cfCounter
                  + ".recordId = " + primaryTableMap.get("TableName") + "." + primaryTableMap.get("TablePrimaryKey"));
              wcvo.setRealTableName("customfieldscalar customfieldscalar" + cfCounter);
            } else {
              wcvo.setFieldName("customfieldvalue" + cfCounter + ".value");
              wcvo.setRelationshipQuery("customfieldmultiple" + cfCounter + ".customfieldId = " + rs.getInt(8) + " AND customfieldmultiple"
                  + cfCounter + ".recordid = " + primaryTableMap.get("TableName") + "." + primaryTableMap.get("TablePrimaryKey")
                  + " AND customfieldvalue" + cfCounter + ".valueid = customfieldmultiple" + cfCounter + ".valueid");
              wcvo.setRealTableName("customfieldvalue customfieldvalue" + cfCounter + ", customfieldmultiple customfieldmultiple" + cfCounter);
            }
            wcvo.setTableName(rs.getString(1));
            wcvo.setSequenceNumber(rs.getInt(3));
            wcvo.setSearchTableId(rs.getInt(4));
            wcvo.setIsOnTable("N");
            wcvo.setDisplayName(rs.getString(9));
            wcvo.setSubRelationShipQuery(rs.getString(10));
          } finally {
            cvdal.destroy();
            cvdal = null;
          }
        } else { // otherwise just build from the query
          wcvo.setTableName(rs.getString(1));
          wcvo.setFieldName(rs.getString(2));
          wcvo.setSequenceNumber(rs.getInt(3));
          wcvo.setSearchTableId(rs.getInt(4));
          wcvo.setIsOnTable(rs.getString(5));
          wcvo.setRealTableName(rs.getString(6));
          wcvo.setRelationshipQuery(rs.getString(7));
          wcvo.setDisplayName(rs.getString(9));
          wcvo.setSubRelationShipQuery(rs.getString(10));
        }
        wcvo.setTableId(Integer.parseInt(tableId));
        wcvo.setFieldId(fieldId);
        whereClauses.add(wcvo);
      }
    } catch (Exception e) {
      logger.error("[Exception][ReportFacadeEJB.convertRStoWhereClauseVO] Exception Thrown: " + e);
      throw new EJBException(e);
    }
    return whereClauses;
  }

  /**
   * Accessor method for generating Query String for report resultand returning
   * the resultSet of Query
   * @param reportId int
   * @return ResultSet
   */
  public String getQueryForResultReport(int userId, int reportId, SearchVO search, CVDal dataAccessLayer)
  {
    ArrayList whereClauseCollection = new ArrayList();
    ArrayList dates = new ArrayList();

    // It will be used for building the temporary table
    StringBuffer strQuery = new StringBuffer();

    // It will be used for building the final Report table
    StringBuffer finalQuery = new StringBuffer();

    // We are going to perform the query in this odered criteria
    StringBuffer orderClause = new StringBuffer();

    StringBuffer clause = new StringBuffer();

    ResultSet resultSet = null;
    WhereClauseVO wcvo = null;

    // When we are building creating a non table field then we must have to make
    // the list for those field
    // then add it to the create table back
    StringBuffer nonTableFieldQuery = new StringBuffer();

    // It a collection of query which we have to run after creating the table
    // we must have to build this query by joining the field relation ship with
    // the primary table
    // then update the column in the temporary table.
    ArrayList updateQueryList = new ArrayList();

    ReportVO report = new ReportVO();
    report = this.getStandardReport(userId, reportId);
    try {
      InitialContext ctx = new InitialContext();
      Object oh = ctx.lookup("local/AdvancedSearch");
      AdvancedSearchLocalHome cfh = (AdvancedSearchLocalHome) javax.rmi.PortableRemoteObject.narrow(oh,
          com.centraview.advancedsearch.AdvancedSearchLocalHome.class);
      AdvancedSearchLocal localAdvancedSearch = (AdvancedSearchLocal) cfh.create();
      localAdvancedSearch.setDataSource(this.dataSource);

      // Here is where the advanced Search is actually performed and the results
      // effect the
      // results of the report query.
      String whereClause = localAdvancedSearch.getWhereClauseForReport(userId, search, "");

      HashMap primaryTableMap = localAdvancedSearch.getPrimaryTableForModule(report.getModuleId());
      // get the primary Table Name and Table Primary Key incase If the user
      // doesn't selects the primary key
      // You must have to add a hidden field in your report criteria coz primary
      // key only link to all other tables
      String primaryKey = primaryTableMap.get("TablePrimaryKey").toString();
      String primaryTable = primaryTableMap.get("TableName").toString();

      ArrayList tableIds = new ArrayList();
      int tableId = ((Number) primaryTableMap.get("TableID")).intValue();

      // add select statement to query string to create the temporary Table
      strQuery.append("SELECT DISTINCT ");

      // add select statement to final query string to generate Report Query
      finalQuery.append("SELECT DISTINCT ");

      // select searchtable.tablename, searchfield.fieldname,
      // reportcontent.sequencenumber, searchtable.searchtableid,
      // searchfield.isontable, searchfield.realtablename,
      // searchfield.relationshipquery, reportcontent.fieldid from searchtable,
      // searchfield,
      // reportcontent where reportcontent.reportid = ? and
      // searchfield.searchfieldid = reportcontent.fieldid and
      // searchtable.searchtableid = reportcontent.tableId order by
      // reportcontent.sequencenumber
      dataAccessLayer.setSql("reports.selectclause");
      dataAccessLayer.setInt(1, reportId);
      resultSet = dataAccessLayer.executeQueryNonParsed();

      whereClauseCollection = this.convertRStoWhereClauseVO(resultSet);

      // we are going to reuse the resultSet reference so clean up here.
      try {
        resultSet.close();
        resultSet = null;
      } catch (SQLException se) {}

      // making select clause
      // oddly from the Where Clause VO, apparently it can double as a Select
      // Clause VO ;-)
      boolean first = true;

      // Build the Select QueryFields List
      Collection selectQueryList = new ArrayList();

      // Build the Select non Table Fields List
      Collection nonTableFieldList = new ArrayList();

      // Build the Select final Query Fields List
      Collection finalQueryList = new ArrayList();

      // If the user as not selected the primary key then we must have to add
      // that key in the table
      boolean primaryKeyFlag = true;

      // Iterate the Where claues and get the information of the Field.
      // If field is in on the table then add it to the selectQueryList
      // If field is not on the table then add it to the nonTableFieldList
      Iterator it = whereClauseCollection.iterator();
      while (it.hasNext()) {
        wcvo = (WhereClauseVO) it.next();

        if (wcvo.getIsOnTable().equals("Y")) {
          // we must have to check wheather the user wants to view the primary
          // key column or not
          // If not then we must have to add the primary key in the temporary
          // Table
          if (wcvo.getFieldName() != null && (wcvo.getFieldName().trim()).equalsIgnoreCase(primaryKey.trim())) {
            primaryKeyFlag = false;
          }// end of if(wcvo.getFieldName() != null &&
            // (wcvo.getFieldName().trim()).equalsIgnoreCase(primaryKey.trim()))

          // Add on table field to the query list
          selectQueryList.add(wcvo.getTableName() + "." + wcvo.getFieldName() + " ");

          // user wants to see the column so we have to add in the final query
          // also
          finalQueryList.add(" `" + wcvo.getFieldName() + "` ");
        }// end of if (wcvo.getIsOnTable().equals("Y"))
        else {

          // create a unique name for the non table field and then we must have
          // to add the field id to it..
          String nonTableField = "adHocField";
          int customTableID = 0;

          // we will check wheather the current field is belonging to
          // customfield table
          if (SearchVO.CUSTOM_FIELD_TABLEID != null && !SearchVO.CUSTOM_FIELD_TABLEID.equals("")) {
            customTableID = Integer.parseInt(SearchVO.CUSTOM_FIELD_TABLEID);
          }

          // If its belonging to custom field table then we must have to concat
          // the "CF"
          // to the uniquie field name why we have to do that since user might
          // select the fieldID 1 belonging to the searchtable
          // for customfield can also have field id 1
          // It will get conflicted and break the report
          if (wcvo.getSearchTableId() == customTableID) {
            nonTableField += "CF";
          }

          // add the non Table field in the list
          nonTableFieldList.add(" `" + nonTableField + wcvo.getFieldId() + "`   Text NOT NULL default '' ");

          // add the user selected field in the final query
          finalQueryList.add(nonTableField + wcvo.getFieldId() + " ");

          // build the update script for the non table fields
          StringBuffer updateQuery = new StringBuffer();
          updateQuery.append("UPDATE tempAdHocReport ");
          String relationShipQuery = wcvo.getRelationshipQuery();
          String realTableName = wcvo.getRealTableName();

          // alias determination
          Collection alias = new ArrayList();
          StringTokenizer aliasCommaTokens = new StringTokenizer(realTableName, ",");
          while (aliasCommaTokens.hasMoreTokens()) {
            String aliasRealTable = aliasCommaTokens.nextToken();
            StringTokenizer aliasTokens = new StringTokenizer(aliasRealTable, " ");
            String tempTableName = aliasTokens.nextToken();
            if (aliasTokens.hasMoreTokens()) {
              alias.add(aliasTokens.nextToken());
            }// end of if(aliasTokens.hasMoreTokens())
          }// end of while(aliasCommaTokens.hasMoreTokens())

          if (realTableName != null) {
            updateQuery.append(" , " + realTableName);
            String thisRelationship = "";
            if (relationShipQuery != null) {
              StringTokenizer relationshipTokens = new StringTokenizer(relationShipQuery, " ");
              while (relationshipTokens.hasMoreTokens()) {
                String thisToken = relationshipTokens.nextToken();
                int index = thisToken.indexOf(".");
                if (index > -1) {
                  String tableName = thisToken.substring(0, index);
                  // Incase if you added the new line to the Query then before
                  // check for the occurance. We will eliminate the new line
                  // return character.
                  tableName = tableName.replaceAll("\n", "");
                  if (!(realTableName.indexOf(tableName) != -1) && (!alias.contains(tableName))) {
                    updateQuery.append(", " + tableName);
                    alias.add(tableName);
                  } // end of if statement (!tables.contains(tableName))
                } // end of if statement (index > -1)
              } // end of while loop (relationshipTokens.hasMoreTokens())

              updateQuery.append(" SET `" + nonTableField + wcvo.getFieldId() + "` = " + wcvo.getFieldName() + " ");
              updateQuery.append(" WHERE " + primaryTable + "." + primaryKey + " = tempAdHocReport." + primaryKey + " " + whereClause);
              thisRelationship = " AND " + relationShipQuery;

              // if the sub relationShip column is empty then we must have to
              // add the value to the update query
              String subRelationShipQuery = wcvo.getSubRelationShipQuery();
              if (subRelationShipQuery != null && !subRelationShipQuery.equals("")) {
                thisRelationship += subRelationShipQuery;
              }// end of if(subRelationShipQuery != null &&
                // !subRelationShipQuery.equals(""))
            }// end of if (relationShipQuery != null)
            updateQuery.append(thisRelationship);
          }// end of if(realTableName != null)

          updateQueryList.add(updateQuery.toString());

        }// end of else for if (wcvo.getIsOnTable().equals("Y"))
      }// end of while (it.hasNext())

      Iterator thisTableIterator = selectQueryList.iterator();
      while (thisTableIterator.hasNext()) {
        String currentTable = (String) thisTableIterator.next();
        strQuery.append(currentTable);
        if (thisTableIterator.hasNext()) {
          strQuery.append(", ");
        } // end of if statement (thisTableIterator.hasNext())
      } // end of while loop (thisTableIterator.hasNext())

      // if the user as not selected the primary key then we must have to add
      // that field
      // so that we don't break the association for the update script
      if (primaryKeyFlag) {
        strQuery.append(", " + primaryTable + "." + primaryKey);
      }

      thisTableIterator = finalQueryList.iterator();
      while (thisTableIterator.hasNext()) {
        String currentTable = (String) thisTableIterator.next();
        finalQuery.append(currentTable);
        if (thisTableIterator.hasNext()) {
          finalQuery.append(", ");
        } // end of if statement (thisTableIterator.hasNext())
      } // end of while loop (thisTableIterator.hasNext())

      finalQuery.append(" FROM `tempAdHocReport` ");

      thisTableIterator = nonTableFieldList.iterator();
      if (thisTableIterator.hasNext()) {
        nonTableFieldQuery.append("(");
        while (thisTableIterator.hasNext()) {
          String currentTable = (String) thisTableIterator.next();
          nonTableFieldQuery.append(currentTable);
          if (thisTableIterator.hasNext()) {
            nonTableFieldQuery.append(", ");
          } // end of if statement (thisTableIterator.hasNext())
        } // end of while loop (thisTableIterator.hasNext())
        nonTableFieldQuery.append(")");
      }
      // getting table ids
      it = whereClauseCollection.iterator();
      while (it.hasNext()) {
        wcvo = (WhereClauseVO) it.next();
        if ((tableId != wcvo.getSearchTableId()) && (!tableIds.contains(new Integer(wcvo.getSearchTableId())))) {
          tableIds.add(new Integer(wcvo.getSearchTableId()));
        }// end of if ((tableId != wcvo.getSearchTableId()) &&
          // (!tableIds.contains(new Integer(wcvo.getSearchTableId()))))
      }// end of while (it.hasNext())

      ArrayList fromTables = new ArrayList();

      dataAccessLayer.clearParameters();
      dataAccessLayer.setSql("reports.fromclause");
      dataAccessLayer.setInt(1, reportId);
      resultSet = dataAccessLayer.executeQueryNonParsed();

      // geting tables for from statement
      while (resultSet.next()) {
        String tableName = resultSet.getString(1);
        // Make sure we aren't adding the uncessesary customfield
        // table in to the from clause.
        if (!tableName.equals("customfield")) {
          fromTables.add(tableName);
        }// end of if (!tableName.equals("customfield"))
      }// end of while (resultSet.next())
      // we are going to reuse the resultSet reference so clean up here.
      try {
        resultSet.close();
        resultSet = null;
      } catch (SQLException se) {}

      first = true;

      strQuery.append(" FROM ");

      // making all from tables
      String table = "";
      it = fromTables.iterator();
      while (it.hasNext()) {
        table = it.next().toString();
        if (first) {
          first = false;
          strQuery.append(" " + table);
        } else {
          strQuery.append(", " + table);
        }
      }

      strQuery.append(" WHERE 1=1 ");
      strQuery.append(whereClause);

      // add order by statement to query string
      dataAccessLayer.setSqlQueryToNull();
      dataAccessLayer.setSql("reports.orderbyclause");
      dataAccessLayer.setInt(1, reportId);
      resultSet = dataAccessLayer.executeQueryNonParsed();

      // build the order by clause and the sorting which ever is selected by the
      // user
      first = true;
      while (resultSet.next()) {
        if (first) {
          finalQuery.append(" ORDER BY ");
          first = false;
        } else {
          finalQuery.append(", ");
        }
        // Loop through the whereClauseCollection and find the right field name
        // to
        // stick in the ORDER BY Statement. Maybe there could be a smarter way
        // then looping
        // each time, but on average there shouldn't be more than 3 sorts
        // anyway.
        it = whereClauseCollection.iterator();
        while (it.hasNext()) {
          int orderTableId = resultSet.getInt(1);
          int orderFieldId = resultSet.getInt(2);
          WhereClauseVO current = (WhereClauseVO) it.next();
          // if the tableId and fieldId from the orderbyclause query match, then
          // throw in the
          // same name we used in the SELECT part of the query.
          if (current.getTableId() == orderTableId && current.getFieldId() == orderFieldId) {
            if (current.getIsOnTable().equals("Y")) {
              finalQuery.append(current.getFieldName() + " ");
            }// end of if (current.getIsOnTable().equals("Y")
            else {
              String nonTableField = "adHocField";
              int customTableID = 0;
              if (SearchVO.CUSTOM_FIELD_TABLEID != null && !SearchVO.CUSTOM_FIELD_TABLEID.equals("")) {
                customTableID = Integer.parseInt(SearchVO.CUSTOM_FIELD_TABLEID);
              }
              if (current.getSearchTableId() == customTableID) {
                nonTableField += "CF";
              }
              finalQuery.append(nonTableField + current.getFieldId() + " ");
            }// end of else the if (current.getIsOnTable().equals("Y")
          }// end of if (current.getTableId() == orderTableId &&
            // current.getFieldId() == orderFieldId)
        } // end while (it.hasNext())
        // Stick the ASC or DESC in there.
        finalQuery.append(resultSet.getString(3));
      }// end of while (resultSet.next())

      dataAccessLayer.setSqlQueryToNull();
      dataAccessLayer.setSqlQuery("DROP TABLE IF EXISTS `tempAdHocReport`");
      dataAccessLayer.executeUpdate();

      // create the temporary table
      dataAccessLayer.setSqlQueryToNull();
      dataAccessLayer.setSqlQuery("CREATE TEMPORARY TABLE `tempAdHocReport` " + nonTableFieldQuery.toString() + " " + strQuery.toString());
      dataAccessLayer.executeUpdate();
      dataAccessLayer.setSqlQueryToNull();

      // execute the update's Batch script
      String whereClauseString = whereClause.toString();

      try {
        // If your advance search doesn't find any result then it will return "
        // AND 1=0 "
        // We must not have to perform the update script coz it will be throw
        // SQL Exception
        if (whereClauseString != null && !whereClauseString.equals(" AND 1=0 ")) {
          int[] batchResult = dataAccessLayer.batchProcess(updateQueryList);
          dataAccessLayer.clearParameters();
        }
      } catch (Exception e) {
        logger.error("[getQueryForResultReport] Exception thrown.", e);
      }
    } catch (Exception e) {
      logger.error("[Exception][ReportFacadeEJB.getQueryForResultReport] Exception Thrown: " + e);
      throw new EJBException(e);
    }
    return finalQuery.toString();
  }

  /**
   * Accessor method for getting table id by field id.
   * @param fieldId int
   * @return int
   */
  private int getTableId(int fieldId)
  {

    CVDal dataAccessLayer = new CVDal(this.dataSource);
    ResultSet resultSet = null;
    int tableId = 0;
    try {
      dataAccessLayer.setSql("reports.gettableid");

      dataAccessLayer.setInt(1, fieldId);
      resultSet = dataAccessLayer.executeQueryNonParsed();

      while (resultSet.next()) {

        tableId = resultSet.getInt(1);
      }
    } catch (SQLException e) {
      logger.error("[Exception][ReportFacadeEJB.getTableId] Exception Thrown: " + e);
      throw new EJBException("Error executing SQL : " + e.toString());
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          // Not much we can do at this point.
        }
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }

    return tableId;
  }

  /**
   * Accessor method for getting tables.
   * @param moduleId int
   * @param appearance String
   * @return ArrayList
   */
  protected ArrayList getTables(int moduleId) throws NamingException, CreateException
  {

    ArrayList tables = new ArrayList();

    CVDal dataAccessLayer = new CVDal(this.dataSource);
    ResultSet resultSet = null;
    int tableId = 0;
    String tableName;
    String tableFullName;
    try {
      // select searchtable.searchtableid, searchtable.tablename,
      // searchtable.displayname from reportmodule, searchtable where
      // searchtable.searchtableid=reportmodule.searchtableid and
      // reportmodule.moduleid=?

      dataAccessLayer.setSqlQuery("select searchtable.searchtableid, searchtable.tablename, "
          + " searchtable.displayname from reportmodule, searchtable " + " where searchtable.searchtableid=reportmodule.searchtableid "
          + " and reportmodule.moduleid=? ");
      dataAccessLayer.setInt(1, moduleId);
      resultSet = dataAccessLayer.executeQueryNonParsed();

      while (resultSet.next()) {
        tableId = resultSet.getInt(1);
        tableName = resultSet.getString(2);
        tableFullName = resultSet.getString(3);
        TheTable table = new TheTable(tableId, tableFullName);
        table.setFields(getFields(tableId, moduleId));
        tables.add(table);
      }
    } catch (SQLException e) {
      logger.error("[Exception][ReportFacadeEJB.getTables] Exception Thrown: " + e);
      throw new EJBException("Error executing SQL : " + e.toString());
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          // Not much we can do at this point.
        }
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
    return tables;
  }

  /**
   * Accessor method for getting fields.
   * @param tableId int
   * @param appearance String
   * @return ArrayList
   */
  protected ArrayList getFields(int tableId, int moduleId)
  {

    ArrayList fields = new ArrayList();

    CVDal dataAccessLayer = new CVDal(this.dataSource);

    ResultSet resultSet = null;
    int fieldId = 0;
    String fieldName;
    String fieldFullName;
    try {

      if (!String.valueOf(tableId).equals(SearchVO.CUSTOM_FIELD_TABLEID)) {
        dataAccessLayer.setSql("reports.getfields");
        dataAccessLayer.setInt(1, tableId);
        resultSet = dataAccessLayer.executeQueryNonParsed();
      } else {
        // we need to get the custom fields, first find the id for cvtable
        // based on the moduleId.
        Number cvTableId = null;
        String sqlString = "SELECT cv.tableid AS id FROM cvtable AS cv, module "
            + "WHERE module.moduleid = ? AND UPPER(cv.name) = UPPER(module.name)";
        dataAccessLayer.setSqlQuery(sqlString);
        dataAccessLayer.setInt(1, moduleId);
        Collection tableIds = dataAccessLayer.executeQuery();
        if (tableIds != null) {
          Iterator tableIdsIterator = tableIds.iterator();
          if (tableIdsIterator.hasNext()) {
            HashMap tableIdMap = (HashMap) tableIdsIterator.next();
            cvTableId = (Number) tableIdMap.get("id");
          } // end if (tableIdsIterator.hasNext())
        } // end if (tableIds != null)
        // now get the list of Fields.
        sqlString = "SELECT customfieldid AS SearchFieldID, name AS DisplayName FROM customfield " + "WHERE recordtype = ?";
        dataAccessLayer.setSqlQueryToNull();
        dataAccessLayer.setSqlQuery(sqlString);
        dataAccessLayer.setInt(1, cvTableId.intValue());
        resultSet = dataAccessLayer.executeQueryNonParsed();
      }
      while (resultSet.next()) {
        fieldId = resultSet.getInt(1);
        fieldFullName = resultSet.getString(2);
        TheItem field = new TheItem(fieldId, fieldFullName);
        field.setParentId(tableId);
        fields.add(field);
      }
    } catch (SQLException e) {
      logger.error("[Exception][ReportFacadeEJB.getFields] Exception Thrown: " + e);
      throw new EJBException("Error executing SQL: " + e.toString());
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {}
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
    return fields;
  }

  /**
   * Accessor method for getting count of reports in list.
   * @return long
   */
  private long getCountOfReports()
  {
    long returnValue = -1;
    Statement statement = null;
    ResultSet resultSet = null;
    CVDal dataAccessLayer = new CVDal(this.dataSource);
    try {

      String sqlString = "SELECT count(*) FROM report";
      dataAccessLayer.setSqlQuery(sqlString);
      resultSet = dataAccessLayer.executeQueryNonParsed();
      while (resultSet.next()) {

        returnValue = resultSet.getLong(1);
      }

    } catch (SQLException sqlException) {
      logger.error("Error :" + sqlException);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          // Not much we can do at this point.
        }
      }
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
    return returnValue;
  }

  /**
   * Accessor method for getting search criteria string.
   * @param reportId int
   * @return ReportVO
   */
  protected String getSearchCriteriaString(int userId, int reportId)
  {

    StringBuffer clause = new StringBuffer();
    boolean first = true;

    if (0 == clause.length())
      clause.append(" FIXME!!!! ");
    return clause.toString();
  }

  /**
   * Accessor method for getting Standard report result for view.
   * @param reportId int
   * @return ReportResultVO
   */

  public ReportResultVO getStandardReportResult(int userId, int reportId, SearchVO search)
  {
    ReportResultVO resultVO = new ReportResultVO();
    try {
      InitialContext ctx = new InitialContext();
      Object oh = ctx.lookup("local/AdvancedSearch");
      AdvancedSearchLocalHome cfh = (AdvancedSearchLocalHome) javax.rmi.PortableRemoteObject.narrow(oh,
          com.centraview.advancedsearch.AdvancedSearchLocalHome.class);
      AdvancedSearchLocal localAdvancedSearch = (AdvancedSearchLocal) cfh.create();
      localAdvancedSearch.setDataSource(this.dataSource);

      ReportLocalHome reportHome = EntityHomeFactory.getReportLocalHome();
      ReportLocal reportLocal = reportHome.findByPrimaryKey(new ReportPK(reportId, this.dataSource));
      resultVO.setReportId(reportLocal.getReportId());
      resultVO.setModuleId(reportLocal.getModuleId());
      resultVO.setName(reportLocal.getName());
      resultVO.setSearchCriteria(getSearchCriteriaString(userId, reportId));
      // get report dates
      java.sql.Date fromDate = reportLocal.getDateFrom();
      java.sql.Date toDate = reportLocal.getDateTo();

      resultVO.setDateRange(fromDate, toDate);

      String where = localAdvancedSearch.getWhereClauseForReport(userId, search, "");
      ReportBuilder reportbuilder = null;
      switch (reportId) {
        case ReportBuilder.CONTACTS1_REPORT_ID: // Contacts1
        case ReportBuilder.CONTACTS2_REPORT_ID: // Contacts2
        {
          reportbuilder = new Contacts1ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.HR1_REPORT_ID: // Hr1
        {
          reportbuilder = new Hr1ReportBuilder(fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.HR2_REPORT_ID: // Hr2
        {
          reportbuilder = new Hr2ReportBuilder(fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.HR3_REPORT_ID: // Hr3
        case ReportBuilder.HR4_REPORT_ID: // Hr4
        {

          where = localAdvancedSearch.getWhereClauseForReport(userId, search, "tsl");

          reportbuilder = new Hr3ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }

        case ReportBuilder.ACTIVITIES1_REPORT_ID: // Activities1
        case ReportBuilder.ACTIVITIES2_REPORT_ID: // Activities2
        {

          where = localAdvancedSearch.getWhereClauseForReport(userId, search, "act");

          reportbuilder = new Activities1ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.SALES1_REPORT_ID: // Sales1
        case ReportBuilder.SALES2_REPORT_ID: // Sales2
        {

          where = localAdvancedSearch.getWhereClauseForReport(userId, search, "op");

          reportbuilder = new Sales1ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.SALES3_REPORT_ID: // Sales3
        {

          where = localAdvancedSearch.getWhereClauseForReport(userId, search, "pi");

          reportbuilder = new Sales3ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.SALES4_REPORT_ID: // Sales4
        {

          // where =
          // localAdvancedSearch.getWhereClauseByReportIdWithAlias(userId,
          // reportId, reportLocal.getModuleId(),"cvord");

          reportbuilder = new Sales4ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.PROJECTS1_REPORT_ID: // Project1
        {

          where = localAdvancedSearch.getWhereClauseForReport(userId, search, "task");

          reportbuilder = new Project1ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.SUPPORT1_REPORT_ID: // Support1
        case ReportBuilder.SUPPORT2_REPORT_ID: // Support2
        case ReportBuilder.SUPPORT3_REPORT_ID: // Support3
        {

          where = localAdvancedSearch.getWhereClauseForReport(userId, search, "tick");

          reportbuilder = new Support1ReportBuilder(reportId, fromDate, toDate, this.dataSource, where);
          break;
        }
        case ReportBuilder.INDIVIDUAL_NOTE_ID:
       {
         reportbuilder = new IndividualNoteStandardReport(fromDate, toDate, this.dataSource, where);
       } 

      }

      if (reportbuilder != null) {
        resultVO.setResults(reportbuilder.runReport());
        reportbuilder.closeConnection();
      }

      // this.debugStandardReport(resultVO);

    } catch (Exception e) {
      throw new EJBException(e);
    }
    return resultVO;
  }

  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  private void debugStandardReport(ReportResultVO resultVO)
  {
    ArrayList content = resultVO.getResults();
    String str = "";
    int size = (content == null) ? 0 : content.size();
    for (int i = 0; i < size; ++i) {
      ReportContentString row = (ReportContentString) content.get(i);
      int rowType = row.getShowType();
      ArrayList rowElements = row.getReportRow();
      // print row number
      logger.error("row # " + i + " ");
      // print row type
      switch (rowType) {
        case ReportContentString.SHOW_TYPE_HEADER:
          logger.error("HEADER");
          break;
        case ReportContentString.SHOW_TYPE_TABLE_HEADER:
          logger.error("TABLE_HEADER");
          break;
        case ReportContentString.SHOW_TYPE_TABLE_SUBHEADER:
          logger.error("TABLE_SUBHEADER");
          break;
        case ReportContentString.SHOW_TYPE_TABLE_ROW:
          logger.error("TABLE_ROW");
          break;
        case ReportContentString.SHOW_TYPE_LINE:
          logger.error("LINE");
          break;
        case ReportContentString.SHOW_TYPE_SPACER:
          logger.error("SPACER");
          break;
        case ReportContentString.SHOW_TYPE_TABLE_END:
          logger.error("TABLE_END");
          break;
        default:
          logger.error("UNKNOWN");
          break;
      }
      str = "";
      int size1 = (rowElements == null) ? 0 : rowElements.size();
      for (int j = 0; j < size1; ++j) {
        ListElementMember column = (ListElementMember) rowElements.get(j);
        str += column.getDisplayString() + ";";
      }
      logger.error(str);
      logger.error("==========================================");
    }
  }
}
