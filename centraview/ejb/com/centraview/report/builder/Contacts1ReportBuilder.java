/*
 * $RCSfile: Contacts1ReportBuilder.java,v $    $Revision: 1.2 $  $Date: 2005/08/19 20:19:10 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.Vector;

import com.centraview.common.IntMember;
import com.centraview.common.ListElementMember;
import com.centraview.common.StringMember;
import com.centraview.report.valueobject.ReportContentString;

/**
 * <p>
 * Title: Contacts1ReportBuilder class
 * </p>
 * <p>
 * Description: Class for Contacts1 standard report building
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Contacts1ReportBuilder extends ReportBuilder {

  private int reportId;

  /**
   * constructor
   * @param ds String datasource
   * @exception Exception
   */

  public Contacts1ReportBuilder(String ds) throws Exception {
    super(ds);
  }

  /**
   * constructor
   * @param reportId int
   * @param dateFrom java.sql.Date
   * @param dateTo java.sql.Date
   * @param clause ArrayList
   * @param ds String
   * @exception Exception
   */

  public Contacts1ReportBuilder(int reportId, java.sql.Date dateFrom, java.sql.Date dateTo, String ds, String whereClause) throws Exception {
    super(dateFrom, dateTo, ds);

    if (reportId != CONTACTS1_REPORT_ID && reportId != CONTACTS2_REPORT_ID) {
      throw new Exception("Contacts1ReportBuilder unknown reportId: " + reportId);
    }

    this.reportId = reportId;

    ReportColumn[] colName = null;

    if (reportId == CONTACTS1_REPORT_ID) {
      colName = new ReportColumn[15];
      colName[0] = new ReportColumn("EntityId", true);
      colName[1] = new ReportColumn("EntityId2", true);
      colName[2] = new ReportColumn("Entity", true);
      colName[3] = new ReportColumn("Primary Contact", true);
      colName[4] = new ReportColumn("Phone", true);
      colName[5] = new ReportColumn("EMail", true);
      colName[6] = new ReportColumn("Address", true);
      colName[7] = new ReportColumn("Address2", true);
      colName[8] = new ReportColumn("City", true);
      colName[9] = new ReportColumn("State", true);
      colName[10] = new ReportColumn("Zip", true);
      colName[11] = new ReportColumn("Source", true);
      colName[12] = new ReportColumn("Account Manager", true);
      colName[13] = new ReportColumn("IndividualID", false);
      colName[14] = new ReportColumn("AccountManagerID", false);
      setLeader(9);
    } else {
      colName = new ReportColumn[7];
      colName[0] = new ReportColumn("EntityId", true);
      colName[1] = new ReportColumn("EntityId2", true);
      colName[2] = new ReportColumn("Entity", true);
      colName[3] = new ReportColumn("Primary Contact", true);
      colName[4] = new ReportColumn("Account Manager", true);
      colName[5] = new ReportColumn("IndividualID", false);
      colName[6] = new ReportColumn("AccountManagerID", false);
      setLeader(4);

      // set custom fields
      setTableName("entity");
      setCustomFieldNames();

    }

    setColumns(colName);

    // drop temp table repContacts1
    addPriorSQL("DROP TABLE IF EXISTS repContacts1");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // create temp table for report content
    addPriorSQL("CREATE TEMPORARY TABLE repContacts1 SELECT DISTINCT "
        +

        ((reportId == CONTACTS1_REPORT_ID) ?

        "entity.entityID, entity.ExternalId AS EntityID2, " + "entity.name AS EntityName," +
        // -- primary contact
            "indv.FirstName AS FirstName, indv.LastName AS LastName, " + "indv.IndividualID AS IndividualID, " +
            // -- phone
            "moc1.Content AS Phone," +
            // -- email
            "moc2.Content AS Email," +
            // -- address
            "addr.Street1 AS Address, addr.Street2 AS Address2, " + "addr.City AS City," + "addr.Zip AS Zip, addr.State AS State,"
            + "entity.Source AS SourceId, source.Name AS SourceName," +
            // --account manager
            "entity.accountManagerID AS AccountManagerID," + "indv1.FirstName AS AMFirstName,indv1.LastName AS AMLastName " + "FROM entity " +
            // -- contacttype1
            "LEFT JOIN contacttype contt1 ON contt1.Name = 'Entity' " +
            // -- contacttype2
            "LEFT JOIN contacttype contt2 ON contt2.Name = 'Entity' " +
            // -- contacttype3
            "LEFT JOIN contacttype contt3 ON contt3.Name = 'Entity' " +
            // -- mocrelate1
            "LEFT JOIN mocrelate mocr1 ON entity.entityID=mocr1.ContactID " + "AND contt1.contacttypeid=mocr1.ContactType AND "
            + "mocr1.isPrimary = 'YES' " +
            // -- mocrelate2
            "LEFT JOIN mocrelate mocr2 ON entity.entityID=mocr2.ContactID " + "AND contt2.contacttypeid = mocr2.ContactType AND "
            + "mocr2.isPrimary = 'YES' " +
            // -- addressrelate
            "LEFT JOIN addressrelate adrel ON entity.entityID=adrel.Contact " + "AND contt3.contacttypeid = adrel.ContactType " +
            // -- Primary Contact
            "LEFT JOIN individual indv ON entity.EntityID=indv.Entity " + "AND indv.PrimaryContact = 'YES' " +
            // -- Phone
            "LEFT JOIN methodofcontact moc1 ON mocr1.MOCID=moc1.MOCID " + "AND moc1.MOCType = 0 " +
            // -- Email
            "LEFT JOIN methodofcontact moc2 ON mocr2.MOCID=moc2.MOCID " + "AND moc2.MOCType = 0 " +
            // -- Address
            "LEFT JOIN address addr ON adrel.Address=addr.AddressID " +
            // -- Account Manager
            "LEFT JOIN individual indv1 ON entity.AccountManagerID=indv1.individualId " +
            // -- Source
            "LEFT JOIN source source ON entity.source=source.sourceId"

        :

        "entity.entityID, entity.ExternalId AS EntityID2, entity.name AS EntityName," +
        // -- primary contact
            "indv.FirstName AS FirstName, indv.LastName AS LastName, " + "indv.IndividualID AS IndividualID, " +
            // --account manager
            "entity.accountManagerID AS AccountManagerID," + "indv1.FirstName AS AMFirstName,indv1.LastName AS AMLastName " + "FROM entity " +
            // -- Primary Contact
            "LEFT JOIN individual indv ON entity.EntityID = indv.Entity " + "AND indv.PrimaryContact = 'YES' " +
            // -- Account Manager
            "LEFT JOIN individual indv1 ON entity.AccountManagerID = indv1.individualId") + " WHERE 1=1 " + whereClause);

    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // Add Phone to the reportList
    addPriorSQL("UPDATE repContacts1 AS rcl, methodofcontact AS moc, mocrelate AS mr SET rcl.phone = moc.content WHERE rcl.entityID = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 4 AND mr.contactType = 1 AND mr.isPrimary='YES'");

    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // Add Email to the reportList
    addPriorSQL("UPDATE repContacts1 AS rcl, methodofcontact AS moc, mocrelate AS mr SET rcl.email = moc.content WHERE rcl.entityID = mr.contactId AND moc.mocId = mr.mocId AND moc.mocType = 1 AND mr.contactType = 1 AND mr.isPrimary='YES'");

    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // select report
    addReportSQL((reportId == CONTACTS1_REPORT_ID) ?

    "SELECT EntityID, EntityID2, EntityName, concat(FirstName ,' '" + ", LastName) Name,Phone ,Email, Address, Address2, City, "
        + "State, Zip, SourceName, " + "concat(AMFirstName,' ',AMLastName) AMName," + "IndividualID, AccountManagerID FROM repContacts1 ORDER BY "
        + "State,Zip,City"

    :

    "SELECT EntityID, EntityID2, EntityName, concat(FirstName ,' '" + ", LastName) Name,concat(AMFirstName,' ',AMLastName) AMName,"
        + "IndividualID, AccountManagerID FROM repContacts1 ORDER BY " + "AccountManagerID, EntityId"

    );

    // bind ArrayList is empty for this sql
    addReportSQLBind(null);

    // drop temp table repContacts1
    addPostSQL("DROP TABLE IF EXISTS repContacts1");

    // bind ArrayList is empty for this sql
    addPostSQLBind(null);

  }

  /**
   * Processes report content for output
   * @param i int report number
   * @param res Vector report content
   * @param result ArrayList report content for output
   */

  public void processReport(int i, Vector res, ArrayList result)
  {
    int size = 0;
    int rowSize = 0;
    ReportContentString contentRow = null;
    ArrayList row = null;
    ArrayList outputRow = null;
    Object column = null;
    ListElementMember element = null;
    int leaderCount = 0;
    String previousLeaderValue = null;
    String currentLeaderValue = "";
    int leaderHeaderIndex = 0;

    ReportColumn[] cols = getColumns();
    int colNumber = cols.length;
    int rowId = 0;

    switch (i) {
      case 0:
        size = res.size();

        // set leader line index
        leaderHeaderIndex = result.size();

        // set header line
        result.add(getTableHeader());

        for (int j = 0; j < size; ++j) {
          row = (ArrayList) res.get(j);
          contentRow = new ReportContentString();
          outputRow = new ArrayList();
          rowSize = row.size();
          for (int k = 0; k < rowSize; ++k) {
            if (k < colNumber) {
              column = row.get(k);
              element = outputObject(column, getColumnName(k));
              if (cols[k].isVisible()) {
                outputRow.add(element);
              }
              if (k == getLeader()) {
                // get leader column value
                currentLeaderValue = element.getDisplayString();
              }
              if (k == 0) {
                rowId = Integer.parseInt(column.toString());
              }
            }

          }
          if (previousLeaderValue == null || !previousLeaderValue.equals(currentLeaderValue)) {
            // leader changed, now we need form header for

            if (leaderCount > 0) {
              // add header line for previous leader(leader name and Record
              // Count)
              result.add(leaderHeaderIndex, getLeaderHeader(previousLeaderValue, leaderCount));
              // add end table line
              result.add(getTableEnd());
              // add table header line for new leader
              result.add(getTableHeader());
            }

            previousLeaderValue = currentLeaderValue; // set new leader

            leaderCount = 0; // reset leaderCount
            leaderHeaderIndex = result.size() - 1; // set new header index
          }
          ++leaderCount;

          contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);

          contentRow.setReportRow(outputRow);

          if (reportId == CONTACTS2_REPORT_ID) {
            try {
              addCustomFieldValues(contentRow, rowId);
            } catch (Exception e) {}
          }

          result.add(contentRow);
        }

        // add header line for last leader
        if (size > 0) {
          result.add(leaderHeaderIndex, getLeaderHeader(previousLeaderValue, leaderCount));
        }

        // add table end
        result.add(getTableEnd());

    }
  }

  /**
   * Gets output header line
   * @param leaderValue String
   * @param leaderCount int
   * @return ReportContentString
   */
  private ReportContentString getLeaderHeader(String leaderValue, int leaderCount)
  {
    ReportContentString headerRow = new ReportContentString();
    ArrayList header = new ArrayList();
    headerRow.setShowType(ReportContentString.SHOW_TYPE_LINE);

    header.add(new StringMember(getColumnName(getLeader()), getColumnName(getLeader()) + ":", 'r', "", 'T', false));

    header.add(new StringMember(getColumnName(getLeader()), leaderValue, 'r', "", 'T', false));

    header.add(new StringMember("Record Count", "Record Count" + ":", 'r', "", 'T', false));

    header.add(new IntMember("Record Count", leaderCount, 'r', "", 'T', false, 10));

    headerRow.setReportRow(header);

    return headerRow;
  }

}
