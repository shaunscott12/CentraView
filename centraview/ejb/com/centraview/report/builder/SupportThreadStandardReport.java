/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
import java.util.ArrayList;
import java.util.Vector;

import com.centraview.common.ListElementMember;
import com.centraview.common.StringMember;
import com.centraview.report.valueobject.ReportContentString;

/**
 * 
 * @author mcallist
 */
public class SupportThreadStandardReport extends ReportBuilder {
  /**
   * @param ds
   * @throws Exception
   */
  public SupportThreadStandardReport(String ds) throws Exception {
    super(ds);

  }

  /**
   * Builds up the query for the report and defines the columns.
   * @param dateFrom
   * @param dateTo
   * @param ds
   * @param whereClause
   * @throws Exception
   */
  public SupportThreadStandardReport(Date dateFrom, Date dateTo, String ds, String whereClause) throws Exception {
    super(dateFrom, dateTo, ds);
    
    // Create the column headers
    ReportColumn[] colName = null;
    colName = new ReportColumn[6];
    colName[0] = new ReportColumn("Individual", false);
    colName[1] = new ReportColumn("Entity", false);
    colName[2] = new ReportColumn("Account Manager", false);
    colName[3] = new ReportColumn("Date Created", true);
    colName[4] = new ReportColumn("Note Title", true);
    colName[5] = new ReportColumn("Note Detail", true);
    
    setColumns(colName);

    String tableName = "reportIndividualNote";
    // drop temp table repContacts1
    addPriorSQL("DROP TABLE IF EXISTS "+tableName);
    addPriorSQLBind(null);

    // create temp table for report content
    StringBuffer buildQuery = new StringBuffer("CREATE TEMPORARY TABLE ");
    buildQuery.append(tableName);
    buildQuery.append(" SELECT DISTINCT ");
    buildQuery.append("CONCAT(i.firstName, ' ', i.lastName) AS Individual, ");
    buildQuery.append("e.name AS Entity, ");
    buildQuery.append("CONCAT(am.firstName, ' ', am.lastName) AS AccountManager, ");
    buildQuery.append("n.dateCreated AS Created, ");
    buildQuery.append("n.title AS Title, ");
    buildQuery.append("n.detail AS Detail");
    buildQuery.append(" FROM ");
    buildQuery.append("individual AS i ");
    buildQuery.append("LEFT OUTER JOIN entity AS e ON (i.entity = e.entityId) ");
    buildQuery.append("LEFT OUTER JOIN individual AS am ON (e.accountManagerId = am.individualId) ");
    buildQuery.append("INNER JOIN note AS n ON (i.individualId = n.relateIndividual) ");
    buildQuery.append("WHERE 1=1 ");
    buildQuery.append(whereClause);
    addPriorSQL(buildQuery.toString());
    addPriorSQLBind(null);

    // select report
    StringBuffer selectQuery = new StringBuffer();
    selectQuery.append("SELECT Individual, Entity, AccountManager, Created, Title, Detail");
    selectQuery.append(" FROM ");
    selectQuery.append(tableName);
    selectQuery.append(" ORDER BY AccountManager");
    addReportSQL(selectQuery.toString());
    addReportSQLBind(null);

    // drop temp table repContacts1
    addPostSQL("DROP TABLE IF EXISTS "+tableName);
    addPostSQLBind(null);
  }

  /**
   * @see com.centraview.report.builder.ReportBuilder#processReport(int, java.util.Vector, java.util.ArrayList)
   */
  public void processReport(int i, Vector queryResults, ArrayList reportOutput)
  {
    int numberOfRows = 0;
    int numberOfColumns = 0;
    ReportContentString contentRow = null;
    ArrayList row = null;
    ArrayList outputRow = null;
    Object column = null;
    ListElementMember element = null;
    String previousIndividualName = null;
    String currentIndividualName = "";
    int individualIndex = 0;
    String entityName = "";
    int entityIndex = 1;
    String accountManagerName = "";
    int amIndex = 2;
    int leaderHeaderIndex = 0;
    
    ReportColumn[] cols = getColumns();
    int colNumber = cols.length;
    
    numberOfRows = queryResults.size();
    // set leader line index
    leaderHeaderIndex = reportOutput.size();
    // set header line
    reportOutput.add(getTableHeader());
    // process each row.
    for (int j = 0; j < numberOfRows; ++j) {
      row = (ArrayList) queryResults.get(j);
      contentRow = new ReportContentString();
      outputRow = new ArrayList();
      numberOfColumns = row.size();
      // process each column
      for (int k = 0; k < numberOfColumns; ++k) {
        if (k < colNumber) {
          column = row.get(k);
          element = outputObject(column, getColumnName(k));
          if (cols[k].isVisible()) {
            outputRow.add(element);
          }
          if (k == individualIndex) {
            currentIndividualName = element.getDisplayString();
          }
          if (k == entityIndex) {
            entityName = element.getDisplayString();
          }
          if (k == amIndex) {
            accountManagerName = element.getDisplayString();
          }
        }
      }
      // update the leader info.
      if (previousIndividualName == null || !previousIndividualName.equals(currentIndividualName)) {
        // leader changed, now we need form header for
        reportOutput.add(leaderHeaderIndex, getLeaderHeader(previousIndividualName, entityName, accountManagerName));
        // add end table line
        reportOutput.add(getTableEnd());
        // add table header line for new leader
        reportOutput.add(getTableHeader());
        previousIndividualName = currentIndividualName; // set new leader
        leaderHeaderIndex = reportOutput.size() - 1; // set new header index
      }
      contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
      contentRow.setReportRow(outputRow);
      reportOutput.add(contentRow);
    }
    // add header line for last leader
    if (numberOfRows > 0) {
      reportOutput.add(leaderHeaderIndex, getLeaderHeader(previousIndividualName, entityName, accountManagerName));
    }
    // add table end
    reportOutput.add(getTableEnd());
  }

  private ReportContentString getLeaderHeader(String individualName, String entityName, String accountManager) {
    ReportContentString headerRow = new ReportContentString();
    headerRow.setShowType(ReportContentString.SHOW_TYPE_LINE);
    ArrayList header = new ArrayList();
    header.add(new StringMember("Individual", "Individual:", 'r', "", 'T', false));
    header.add(new StringMember("Individual", individualName, 'r', "", 'T', false));
    header.add(new StringMember("Entity", "Entity:", 'r', "", 'T', false));
    header.add(new StringMember("Entity", entityName, 'r', "", 'T', false));
    header.add(new StringMember("AccountManager", "Account Manager:", 'r', "", 'T', false));
    header.add(new StringMember("AccountManager", accountManager, 'r', "", 'T', false));
    headerRow.setReportRow(header);
    return headerRow;
  }
}
