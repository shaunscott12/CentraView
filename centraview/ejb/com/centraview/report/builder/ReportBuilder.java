/*
 * $RCSfile: ReportBuilder.java,v $    $Revision: 1.2 $  $Date: 2005/08/19 20:19:10 $ - $Author: mcallist $
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

package com.centraview.report.builder;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.InitialContext;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.CustomFieldList;
import com.centraview.common.DDNameValue;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListElementMember;
import com.centraview.common.MoneyMember;
import com.centraview.common.PureDateMember;
import com.centraview.common.StringMember;
import com.centraview.customfield.CustomFieldLocal;
import com.centraview.customfield.CustomFieldLocalHome;
import com.centraview.report.valueobject.ReportContentString;

/**
 * <p>
 * Title: ReportBuilder class
 * </p>
 * <p>
 * Description: Base Class for standard report building
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public abstract class ReportBuilder {

  /** Constants - standard report IDs */
  public static final int CONTACTS1_REPORT_ID = 100; // Contacts by state
  public static final int CONTACTS2_REPORT_ID = 101; // Contacts by account
                                                      // manager
  public static final int HR1_REPORT_ID = 102; // Expenses by user
  public static final int HR2_REPORT_ID = 103; // Expenses by entity
  public static final int HR3_REPORT_ID = 104; // Timeslips by user
  public static final int HR4_REPORT_ID = 105; // Timeslips by entity
  public static final int ACTIVITIES1_REPORT_ID = 106; // Activities by user
  public static final int ACTIVITIES2_REPORT_ID = 107; // Activities by entity
  public static final int SALES1_REPORT_ID = 108; // Opportunitiess by user
  public static final int SALES2_REPORT_ID = 109; // Proposal by user
  public static final int SALES3_REPORT_ID = 110; // Proposal detail by user
  public static final int SALES4_REPORT_ID = 111; // Sales order report
  public static final int PROJECTS1_REPORT_ID = 112; // Project summary
  public static final int SUPPORT1_REPORT_ID = 114; // Support tickets per user
  public static final int SUPPORT2_REPORT_ID = 115; // Support tickets by entity
  public static final int SUPPORT3_REPORT_ID = 116; // Support tickets list
  public static final int INDIVIDUAL_NOTE_ID = 117; // Individual with notes.

  public abstract void processReport(int i, Vector res, ArrayList result);

  private CVDal dataAccessLayer = null;
  private String dataSource = null;

  // dates for report
  private java.sql.Date dateFrom = null;
  private java.sql.Date dateTo = null;

  // ArrayLists(of String) with statements
  private ArrayList priorSQL = null; // prior SQL statements needed for report
                                      // building
  private ArrayList reportSQL = null; // main report sql
  private ArrayList postSQL = null; // post SQL statements needed after report
                                    // building

  // ArrayLists(of ArrayLists of BindObjects) for corresponding statements
  private ArrayList priorSQLBind = null; // prior SQL statements needed for
                                          // report building
  private ArrayList reportSQLBind = null; // main report sql
  private ArrayList postSQLBind = null; // post SQL statements needed after
                                        // report building

  // String array of column names
  private ReportColumn[] columns = null;
  // ArrayList of custom field names
  ArrayList customFields = null;

  // index of leader Element
  private int leader = 0;

  // base report table name(for custom fields)
  private String tableName = "";

  /**
   * Init method is called by constructor
   */
  private void init()
  {
    priorSQL = new ArrayList();
    reportSQL = new ArrayList();
    postSQL = new ArrayList();
    priorSQLBind = new ArrayList();
    reportSQLBind = new ArrayList();
    postSQLBind = new ArrayList();
  }

  /**
   * constructor
   * @param ds String datasource
   * @exception Exception
   */
  public ReportBuilder(String ds) throws Exception {
    this.dataSource = ds;
    init();
    connect();
  }

  /**
   * Constructor
   * @param cvdl CVDal
   * @exception Exception
   */
  public ReportBuilder(CVDal cvdl) throws Exception {
    init();
    if (cvdl != null) {
      dataAccessLayer = cvdl;
    } else {
      connect();
    }
  }

  /**
   * Constructor
   * @param ds String datasource
   * @param cvdl CVDal
   * @exception Exception
   */
  public ReportBuilder(String ds, CVDal cvdl) throws Exception {
    init();
    if (ds != null && cvdl != null) {
      dataAccessLayer = cvdl;
      dataSource = ds;
    } else {
      connect();
    }
  }

  /**
   * Constructor
   * @param dateFrom java.sql.Date
   * @param dateTo java.sql.Date
   * @param clauseArray ArrayList
   * @param ds String
   */
  public ReportBuilder(java.sql.Date dateFrom, java.sql.Date dateTo, String ds) throws Exception {

    init();

    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.dataSource = ds;

    connect();
  }

  /**
   * Sets the database name and gets a connection on the Datasource
   */
  protected void connect() throws Exception
  {
    dataAccessLayer = new CVDal(this.dataSource);
  }

  /**
   * Closes Connection
   */
  public void closeConnection()
  {
    if (dataAccessLayer != null) {
      dataAccessLayer.destroy();
      dataAccessLayer = null;
    }
  }

  /**
   * Prepares a Query and returns PreparedSatement
   * @param query String
   * @param bindObjects ArrayList
   */
  protected void prepareQuery(String query, ArrayList bindObjects) throws Exception
  {
    dataAccessLayer.setSqlQuery(query);
    // get bind parameters size
    int size = (bindObjects == null) ? 0 : bindObjects.size();
    // set bind parameters for query
    Object bindObject = null;
    for (int i = 0; i < size; ++i) {
      bindObject = bindObjects.get(i);
      dataAccessLayer.setObject(i + 1, bindObject);
    }
  }

  /**
   * Executes a Query and returns Collection/null
   * @param query String
   * @param bindObjects ArrayList
   * @return Object
   */
  private Object executeSQL(String query, ArrayList bindObjects) throws Exception
  {
    try {
      String str = query.trim();
      String oper = "";
      int methodToCall = 0;
      StringTokenizer tok = new StringTokenizer(str, " ");
      if (tok.hasMoreTokens()) {
        oper = tok.nextToken();
      }
      if (oper.equalsIgnoreCase("SELECT")) {
        methodToCall = 1;
      } else if (oper.equalsIgnoreCase("CREATE") || oper.equalsIgnoreCase("DROP") || oper.equalsIgnoreCase("INSERT")
          || oper.equalsIgnoreCase("UPDATE")) {
        methodToCall = 2;
      }

      switch (methodToCall) {
        case 1: // query
          return executeQuery(query, bindObjects);
        case 2: // update
          executeUpdate(query, bindObjects);
          break;
      }
      return null;
    } catch (Exception e) {
      throw new Exception("ReportBuilder.executeSQL statement: '" + query + "' error: " + e.toString());
    }
  }

  /**
   * Executes a Query and returns Vector
   * @param query String
   * @param bindObjects ArrayList
   * @return Vector
   */
  private Vector executeQuery(String query, ArrayList bindObjects) throws Exception
  {
    try {
      prepareQuery(query, bindObjects);
      ResultSet resultSet = dataAccessLayer.executeQueryNonParsed();
      ResultSetMetaData metaData = resultSet.getMetaData();
      int size = metaData.getColumnCount();
      Vector result = new Vector();
      ArrayList resultItem = null;
      while (resultSet.next()) {
        resultItem = new ArrayList();
        for (int i = 0; i < size; ++i) {
          resultItem.add(resultSet.getObject(i + 1));
        }
        result.add(resultItem);
      }
      return result;
    } catch (SQLException e) {
      throw new Exception("ReportBuilder.executeQuery error: " + e.toString());
    }

  }

  /**
   * Executes a Query
   * @param query String
   * @param bindObjects ArrayList
   */
  private void executeUpdate(String query, ArrayList bindObjects) throws Exception
  {
    try {
      prepareQuery(query, bindObjects);
      dataAccessLayer.executeUpdate();
    } catch (SQLException e) {
      throw new Exception("ReportBuilder.executeUpdate error: " + e.toString());
    }

  }

  /**
   * Returns ListElementMember based on object class/name
   * @param object Object
   * @param name String
   * @return ListElementMember
   */
  public ListElementMember outputObject(Object object, String name)
  {
    ListElementMember output = null;
    try {

      if (object instanceof java.math.BigDecimal) {
        output = new IntMember(name, (object == null) ? 0 : ((java.math.BigDecimal) object).intValue(), 'r', "", 'T', false, 10);
      } else if (object instanceof Long) {
        output = new IntMember(name, (object == null) ? 0 : ((Long) object).intValue(), 'r', "", 'T', false, 10);
      } else if (object instanceof Integer) {
        output = new IntMember(name, (object == null) ? 0 : ((Integer) object).intValue(), 'r', "", 'T', false, 10);
      } else if (object instanceof Float) {
        output = new MoneyMember(name, new Float((object == null) ? 0 : ((Float) object).floatValue()), 'r', "", 'T', false, 10);
      } else if (object instanceof Double) {
        output = new MoneyMember(name, new Float((object == null) ? 0 : ((Double) object).floatValue()), 'r', "", 'T', false, 10);
      } else if (object instanceof Date) {
        output = new PureDateMember(name, (Date) object, 'r', "", 'T', false, 0, "EST");
      } else if (object instanceof Timestamp) {
        Timestamp ts = (object == null) ? new Timestamp(0) : (Timestamp) object;
        java.sql.Date dt = new java.sql.Date(ts.getTime());
        output = new PureDateMember(name, dt, 'r', "", 'T', false, 0, "EST");
      } else {
        output = new StringMember(name, new String((object == null) ? "" : object.toString()), 'r', "", 'T', false);
      }

    } catch (Exception e) {
      System.out.println("ReportBuilder.outputObject error: " + e.toString());
    }
    return output;
  }

  /**
   * Run reports sql and return report result
   * @return ArrayList
   */
  public ArrayList runReport() throws Exception
  {

    ArrayList result = new ArrayList();

    int size = 0;
    size = (priorSQL == null) ? 0 : priorSQL.size();
    try {
      for (int i = 0; i < size; ++i) {
        executeSQL((String) priorSQL.get(i), (ArrayList) priorSQLBind.get(i));
      }

      size = (reportSQL == null) ? 0 : reportSQL.size();
      Vector res = null;
      for (int i = 0; i < size; ++i) {
        res = (Vector) executeSQL((String) reportSQL.get(i), (ArrayList) reportSQLBind.get(i));
        processReport(i, res, result);
      }

      size = (postSQL == null) ? 0 : postSQL.size();
      for (int i = 0; i < size; ++i) {
        executeSQL((String) postSQL.get(i), (ArrayList) postSQLBind.get(i));
      }
    } catch (Exception e) {
      closeConnection();
      System.out.println(" Report Run Error  ! ");
      System.out.println(" Caused by : " + e);
    }
    return result;
  }

  public ArrayList getPostSQL()
  {
    return postSQL;
  }

  public ArrayList getPriorSQL()
  {
    return priorSQL;
  }

  public ArrayList getReportSQL()
  {
    return reportSQL;
  }

  public void setPostSQL(ArrayList postSQL)
  {
    this.postSQL = postSQL;
  }

  public void setPriorSQL(ArrayList priorSQL)
  {
    this.priorSQL = priorSQL;
  }

  public void setReportSQL(ArrayList reportSQL)
  {
    this.reportSQL = reportSQL;
  }

  public void addReportSQL(String stmt)
  {
    reportSQL.add(stmt);
  }

  public void clearReportSQL()
  {
    reportSQL.clear();
  }

  public void addPostSQL(String stmt)
  {
    postSQL.add(stmt);
  }

  public void addPriorSQL(String stmt)
  {
    priorSQL.add(stmt);
  }

  public ArrayList getPostSQLBind()
  {
    return postSQLBind;
  }

  public void setPostSQLBind(ArrayList postSQLBind)
  {
    this.postSQLBind = postSQLBind;
  }

  public ArrayList getPriorSQLBind()
  {
    return priorSQLBind;
  }

  public void setPriorSQLBind(ArrayList priorSQLBind)
  {
    this.priorSQLBind = priorSQLBind;
  }

  public ArrayList getReportSQLBind()
  {
    return reportSQLBind;
  }

  public void setReportSQLBind(ArrayList reportSQLBind)
  {
    this.reportSQLBind = reportSQLBind;
  }

  public void addReportSQLBind(ArrayList bindArrayObject)
  {
    reportSQLBind.add(bindArrayObject);
  }

  public void addPostSQLBind(ArrayList bindArrayObject)
  {
    postSQLBind.add(bindArrayObject);
  }

  public void addPriorSQLBind(ArrayList bindArrayObject)
  {
    priorSQLBind.add(bindArrayObject);
  }

  public ReportColumn[] getColumns()
  {
    return columns;
  }

  public void setColumns(ReportColumn[] columns)
  {
    this.columns = columns;
  }

  public void setColumnName(int i, String name)
  {
    if (columns != null && i > -1 && i < columns.length) {
      columns[i].colName = name;
    }
  }

  public void setColumnVisible(int i, boolean visible)
  {
    if (columns != null && i > -1 && i < columns.length) {
      columns[i].visible = visible;
    }
  }

  public String getColumnName(int i)
  {
    String name = "";
    if (columns != null && i > -1 && i < columns.length && columns[i].colName != null) {
      name = columns[i].colName;
    }
    return name;
  }

  public boolean isColumnVisible(int i)
  {
    boolean vis = false;
    if (columns != null && i > -1 && i < columns.length) {
      vis = columns[i].visible;
    }
    return vis;
  }

  public int getLeader()
  {
    return leader;
  }

  public void setLeader(int leader)
  {
    this.leader = leader;
  }

  public ReportContentString getTableHeader()
  {
    return getTableHeader(false);
  }

  public ReportContentString getTableSubHeader()
  {
    return getTableHeader(true);
  }

  /**
   * Returns table header based on report column name/visibility
   * @param subHeader boolean indicates type of table header
   * @return ReportContentString
   */
  private ReportContentString getTableHeader(boolean subHeader)
  {
    ReportContentString headerRow = new ReportContentString();
    ArrayList header = new ArrayList();
    int size = 0;
    headerRow.setShowType((subHeader) ? ReportContentString.SHOW_TYPE_TABLE_HEADER : ReportContentString.SHOW_TYPE_TABLE_SUBHEADER);

    ReportColumn[] colName = getColumns();
    // main fields
    size = (colName == null) ? 0 : colName.length;
    for (int i = 0; i < size; ++i) {
      if (colName[i].isVisible()) {
        header.add(new StringMember(colName[i].colName, colName[i].colName, 'r', "", 'T', false));
      }
    }
    // custom fields
    DDNameValue ddName = null;
    size = (customFields == null) ? 0 : customFields.size();
    for (int i = 0; i < size; ++i) {
      ddName = (DDNameValue) customFields.get(i);
      header.add(new StringMember(ddName.getName(), ddName.getName(), 'r', "", 'T', false));
    }

    headerRow.setReportRow(header);
    return headerRow;
  }

  /**
   * Returns empty table row
   * @return ReportContentString
   */
  public ReportContentString getEmptyTableRow()
  {
    ReportContentString headerRow = new ReportContentString();
    ArrayList header = new ArrayList();
    int size = 0;
    headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
    ReportColumn[] colName = getColumns();
    // main fields
    size = (colName == null) ? 0 : colName.length;
    for (int i = 0; i < size; ++i) {
      if (colName[i].isVisible()) {
        header.add(new StringMember(colName[i].colName, "", 'r', "", 'T', false));
      }
    }
    // custom fields
    DDNameValue ddName = null;
    size = (customFields == null) ? 0 : customFields.size();
    for (int i = 0; i < size; ++i) {
      ddName = (DDNameValue) customFields.get(i);
      header.add(new StringMember(ddName.getName(), "", 'r', "", 'T', false));
    }

    headerRow.setReportRow(header);
    return headerRow;
  }

  public CVDal getConnection()
  {
    return dataAccessLayer;
  }

  public java.sql.Date getDateFrom()
  {
    return dateFrom;
  }

  public java.sql.Date getDateTo()
  {
    return dateTo;
  }

  public void setDateFrom(java.sql.Date dateFrom)
  {
    this.dateFrom = dateFrom;
  }

  public void setDateTo(java.sql.Date dateTo)
  {
    this.dateTo = dateTo;
  }

  public String getDataSource()
  {
    return dataSource;
  }

  public void setDataAccessLayer(CVDal cvdl)
  {
    this.dataAccessLayer = cvdl;
  }

  public void setDataSource(String dataSource)
  {
    this.dataSource = dataSource;
  }

  /**
   * Returns table end row
   * @return ReportContentString
   */
  public ReportContentString getTableEnd()
  {
    ReportContentString headerRow = new ReportContentString();
    headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_END);
    return headerRow;
  }

  /**
   * Returns report text line
   * @param text String
   * @return ReportContentString
   */
  public ReportContentString getSimpleLine(String text)
  {
    ReportContentString contentRow = new ReportContentString();
    contentRow.setShowType(ReportContentString.SHOW_TYPE_LINE);
    ArrayList outputRow = new ArrayList();
    ListElementMember element = outputObject(text, text);
    outputRow.add(element);
    contentRow.setReportRow(outputRow);
    return contentRow;
  }

  /**
   * Returns report spacer
   * @return ReportContentString
   */
  public ReportContentString getSpacerLine()
  {
    ReportContentString row = new ReportContentString();
    row.setShowType(ReportContentString.SHOW_TYPE_SPACER);
    return row;
  }

  /**
   * Returns part of where clause
   * @param tables ArrayList
   * @return String
   */
  /*
   * public String prepareWhere( int reportId ) { StringBuffer where = new
   * StringBuffer(); CVDal dataAccessLayer = new CVDal(this.dataSource);
   * ResultSet resultSet = null; try { ReportFacadeLocal localReportFacade =
   * null; ReportFacadeLocalHome reportFacadeLocalHome =
   * (ReportFacadeLocalHome)CVUtility.getHomeObject("com.centraview.advancedsearch.ReportFacadeLocalHome",
   * "ReportFacade"); localReportFacade = reportFacadeLocalHome.create();
   * ReportVO report = (ReportVO)localReportFacade.getStandardReport(reportId);
   * SearchVO search = (SearchVO)report.getSearchVO(); AdvancedSearchLocal
   * localAdvancedSearch = null; AdvancedSearchLocalHome advancedSearchLocalHome =
   * (AdvancedSearchLocalHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchLocalHome",
   * "AdvancedSearch"); localAdvancedSearch = advancedSearchLocalHome.create();
   * String primaryKey = ""; Collection col =
   * localAdvancedSearch.performSearch(1,search); //primaryKey =
   * ((HashMap)localAdvancedSearch.getPrimaryTableForModule(report.getModuleId())).get("TablePrimaryKey").toString();
   * dataAccessLayer.setSqlQuery("select searchtable.TablePrimaryKey from
   * searchmodule left join searchtable on searchtable.searchtableid =
   * searchmodule.searchtableid where searchmodule.moduleid = "
   * +report.getSearchVO().getModuleID()+ " and searchmodule.isprimarytable =
   * 'Y'"); resultSet = dataAccessLayer.executeQueryNonParsed();
   * if(resultSet.next()) { primaryKey = resultSet.getString(1); }
   * where.append(primaryKey); where.append(" in ("); boolean first = true;
   * for(Iterator it = col.iterator();it.hasNext();) { if (first) { first =
   * false; } else { where.append(", "); } where.append(it.next().toString()); }
   * where.append(") "); }catch (Exception e) {
   * System.out.println("[Exception][ReportFacadeEJB.getQueryForResultReport]
   * Exception Thrown: "+e); } finally { if (resultSet != null) { try {
   * resultSet.close(); } catch (SQLException e) { // Not much we can do at this
   * point. } } dataAccessLayer.destroy(); dataAccessLayer = null; } return
   * where.toString(); }
   */

  /**
   * Sets custom fields based on report table
   */
  public void setCustomFieldNames() throws Exception
  {
    try {

      InitialContext ic = CVUtility.getInitialContext();
      CustomFieldLocalHome homeCustomField = (CustomFieldLocalHome) ic.lookup("local/CustomField");
      CustomFieldLocal remoteCustomField = homeCustomField.create();
      Collection customFieldNames = remoteCustomField.getCustomFieldImportData(tableName);

      if (customFieldNames != null) {
        HashMap hm = null;
        customFields = new ArrayList();
        for (Iterator iter = customFieldNames.iterator(); iter.hasNext();) {
          hm = (HashMap) iter.next();
          customFields.add(new DDNameValue(hm.get("customfieldid").toString(), hm.get("name").toString()));
        }
      }
    } catch (Exception e) {
      closeConnection();
      throw new Exception(e);
    }
  }

  /**
   * Adds report custom field values to ReportContentString
   * @param rep ReportContentString
   * @param recordId int
   */
  public void addCustomFieldValues(ReportContentString rep, int recordId) throws Exception
  {
    CustomFieldList customFieldValues = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      CustomFieldLocalHome homeCustomField = (CustomFieldLocalHome) ic.lookup("local/CustomField");
      CustomFieldLocal remoteCustomField = homeCustomField.create();
      customFieldValues = remoteCustomField.getCustomFieldList(tableName, recordId);

      // HashMap hm = customFieldValues.getColumnMap();
      DDNameValue fieldName = null;
      // CustomFieldListElement listElement = null;
      Set st = null;
      Iterator iter = null;
      StringMember fl = null;
      StringMember vl = null;
      boolean fieldPresent = false;
      int size = customFields.size();

      for (int i = 0; i < size; ++i) {
        fieldName = (DDNameValue) customFields.get(i);
        st = customFieldValues.keySet();
        iter = st.iterator();
        fieldPresent = false;
        while (iter.hasNext()) {
          String keyStr = (String) iter.next();
          ListElement ele = (ListElement) customFieldValues.get(keyStr);
          IntMember id = (IntMember) ele.get("CustomFieldID");

          if (fieldName.getStrid().equalsIgnoreCase(id.getDisplayString())) {

            fl = (StringMember) ele.get("Field");
            vl = (StringMember) ele.get("Value");
            System.out.println(keyStr + "; CustomFieldID = " + id.getDisplayString() + " ; Field = " + fl.getDisplayString() + "; Value = "
                + vl.getDisplayString());
            // current field
            if (vl != null) {
              rep.add(vl);
              fieldPresent = true;
            }

            break;
          }
        }
        if (!fieldPresent) {
          vl = new StringMember(fieldName.getName(), "", 'r', "", 'T', false);
          rep.add(vl);
        }

      }
    } catch (Exception e) {
      closeConnection();
      throw new Exception(e);
    }

  }

  public ArrayList getCustomFields()
  {
    return customFields;
  }

  public void setCustFields(ArrayList customFields)
  {
    this.customFields = customFields;
  }

  public String getTableName()
  {
    return tableName;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

}
