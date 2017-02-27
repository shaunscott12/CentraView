/*
 * $RCSfile: Activities1ReportBuilder.java,v $    $Revision: 1.2 $  $Date: 2005/08/19 20:19:10 $ - $Author: mcallist $
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

import com.centraview.common.ListElementMember;
import com.centraview.common.StringMember;
import com.centraview.report.valueobject.ReportContentString;

/**
 * <p>
 * Title: Activities1ReportBuilder class
 * </p>
 * <p>
 * Description: Class for Activities1 standard report building
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Activities1ReportBuilder extends ReportBuilder {

  private int reportId;
  private int leaderBind; // index of bind for leader

  private String dateClause = "";
  ArrayList dateBind = null;

  /**
   * constructor
   * @param ds String
   * @exception Exception
   */
  public Activities1ReportBuilder(String ds) throws Exception {
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
  public Activities1ReportBuilder(int reportId, java.sql.Date dateFrom, java.sql.Date dateTo, String ds, String whereClause) throws Exception {

    super(dateFrom, dateTo, ds);

    if (reportId != ACTIVITIES1_REPORT_ID && reportId != ACTIVITIES2_REPORT_ID) {
      throw new Exception("ActivitiesReportBuilder unknown reportId: " + reportId);
    }

    this.reportId = reportId;

    ReportColumn[] colName = new ReportColumn[14];
    colName[0] = new ReportColumn("ActivityId", false);
    colName[1] = new ReportColumn("Start Date", true);
    colName[2] = new ReportColumn("End Date", true);
    colName[3] = new ReportColumn("Activity Type", true);
    colName[4] = new ReportColumn("Title", true);
    colName[5] = new ReportColumn("EntityId", false);
    colName[6] = new ReportColumn("Entity", (reportId == 106) ? true : false);
    colName[7] = new ReportColumn("IndividualId", false);
    colName[8] = new ReportColumn("Individual", true);
    colName[9] = new ReportColumn("Status", true);
    colName[10] = new ReportColumn("Notes", true);
    colName[11] = new ReportColumn("Owner or Attendee", true);
    colName[12] = new ReportColumn("Sales Person", false); // Creator
    colName[13] = new ReportColumn("CreatorId", false); // CreatorId

    setLeader((reportId == ACTIVITIES1_REPORT_ID) ? 12 : 6);
    leaderBind = (reportId == ACTIVITIES1_REPORT_ID) ? 13 : 5;

    setColumns(colName);

    // drop temp table for report content
    addPriorSQL("DROP TABLE IF EXISTS repActivities0");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    java.sql.Timestamp from = (getDateFrom() == null) ? null : new java.sql.Timestamp(getDateFrom().getTime());
    java.sql.Timestamp to = (getDateTo() == null) ? null : new java.sql.Timestamp(getDateTo().getTime() + 24 * 60 * 60 * 1000);

    // dateClause is used in subreport
    dateClause = ((from != null) ? " AND (act.Start >= ? OR act.Start IS NULL) " + "AND (act.End >= ? OR act.End IS NULL)" : "")
        + ((to != null) ? " AND (act.Start < ? OR act.Start IS NULL) " + "AND (act.End < ? OR act.End IS NULL)" : "");

    // dateBind is used in subreport
    dateBind = (from != null || to != null) ? new ArrayList() : null;
    if (from != null) {
      dateBind.add(from);
      dateBind.add(from);
    }
    if (to != null) {
      dateBind.add(to);
      dateBind.add(to);
    }

    // create temp table for report content
    addPriorSQL("CREATE TEMPORARY TABLE repActivities0 SELECT distinct act.ActivityID ActivityID, "
        + "act.Start StartDate, act.End EndDate, act.Type TypeId,  " + "act.Title Title,at.Name Type, entity.entityId EntityId, "
        + "entity.name EntityName,indv.individualid CreatorId,  " + "indv.FirstName CreatorFirstN, indv.LastName CreatorLastN,   "
        + "as1.Name Status, act.notes Notes,  " + "indv2.individualid IndividualId,  "
        + "indv2.FirstName IndividualFirstN, indv2.LastName IndividualLastN,  " + "attendee.individualId AttendeeId, indv3.FirstName AttFirstN,  "
        + "indv3.LastName AttLastN,  " + "indv4.individualId OwnerId, indv4.FirstName OwnerFirstN,  " + "indv4.LastName OwnerLastN   "
        + "FROM activity act   " + "LEFT JOIN activitylink actl1 ON actl1.activityId = act.activityid AND actl1.recordtypeid=2 "
        + "LEFT JOIN activitylink actl2 ON actl2.activityId = act.activityid AND actl2.recordtypeid=1 "
        + "LEFT JOIN activitystatus as1 ON as1.StatusID = act.Status  " + "LEFT JOIN activitytype at ON at.TypeID = act.Type  "
        + "LEFT JOIN individual indv ON indv.IndividualID = act.creator  " + "LEFT JOIN individual indv2 ON actl1.recordid=indv2.individualid  "
        + "LEFT JOIN attendee attendee ON attendee.ActivityID=act.ActivityID  "
        + "LEFT JOIN individual indv3 ON indv3.IndividualID = attendee.individualId   "
        + "LEFT JOIN individual indv4 ON indv4.IndividualID = act.owner   " + "LEFT JOIN entity entity ON actl2.recordid=entity.entityid "
        + "WHERE 1=1 " + whereClause + dateClause);
    // bind ArrayList is empty for this sql
    addPriorSQLBind(dateBind);

    // create another temp table to remove duplicated records
    addPriorSQL("DROP TABLE IF EXISTS repActivities1");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    addPriorSQL("CREATE TEMPORARY TABLE repActivities1 SELECT * FROM repActivities0 WHERE 1=0");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    addPriorSQL("INSERT INTO repActivities1 (activityId) SELECT distinct activityId FROM repActivities0");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    addPriorSQL("UPDATE repActivities1 r2, repActivities0 r1 SET " + "r2.StartDate=r1.StartDate,  " + "r2.EndDate=r1.EndDate,  "
        + "r2.TypeId=r1.TypeId,  " + "r2.Title=r1.Title, " + "r2.Type=r1.Type,  " + "r2.EntityId=r1.EntityId,  " + "r2.EntityName=r1.EntityName, "
        + "r2.CreatorId=r1.CreatorId,  " + "r2.CreatorFirstN=r1.CreatorFirstN,  " + "r2.CreatorLastN=r1.CreatorLastN,   " + "r2.Status=r1.Status,  "
        + "r2.Notes=r1.Notes,  " + "r2.IndividualId=r1.IndividualId,  " + "r2.IndividualFirstN=r1.IndividualFirstN,  "
        + "r2.IndividualLastN=r1.IndividualLastN,  " + "r2.AttendeeId=r1.AttendeeId,  " + "r2.AttFirstN=r1.AttFirstN,  "
        + "r2.AttLastN=r1.AttLastN,  " + "r2.OwnerId=r1.OwnerId,  " + "r2.OwnerFirstN=r1.OwnerFirstN, " + "r2.OwnerLastN=r1.OwnerLastN "
        + "WHERE r1.activityId = r2.activityId ");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // select report rows
    addReportSQL("SELECT ActivityId,StartDate,EndDate,Type,Title,EntityId,EntityName,"
        + "IndividualId, concat(IndividualFirstN,' ',IndividualLastN), Status, Notes, "
        + "concat(OwnerFirstN,' ',OwnerLastN,' ',AttFirstN,' ',AttLastN), " + "concat(CreatorFirstN,' ',CreatorLastN), CreatorId "
        + "FROM repActivities1 ORDER BY " + ((reportId == ACTIVITIES1_REPORT_ID) ? "CreatorId" : "EntityId") + ", ActivityId");
    // bind ArrayList is empty for this sql
    addReportSQLBind(null);

    addPostSQL("DROP TABLE IF EXISTS repActivities0");
    // bind ArrayList is empty for this sql
    addPostSQLBind(null);

    addPostSQL("DROP TABLE IF EXISTS repActivities1");
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
    Object prevBindObject = null;
    Object currBindObject = null;
    ListElementMember element = null;
    int leaderCount = 0;
    String previousLeaderValue = null;
    String currentLeaderValue = "";
    String prevBindValue = null;
    String currBindValue = "";

    int leaderHeaderIndex = 0;

    ReportColumn[] cols = getColumns();
    int colNumber = cols.length;

    switch (i) {
      case 0:
        size = res.size();

        // set leader line index
        leaderHeaderIndex = result.size();

        // add table header line for new leader
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
              if (k == leaderBind) {
                // save leader object for binding
                currBindObject = column;
                currBindValue = element.getDisplayString();
              }
            }
          }

          if (prevBindValue == null || !prevBindValue.equals(currBindValue)) {
            // leader changed, now we need form header for

            if (leaderCount > 0) {
              // add header line for previous leader(leader name and Record
              // Count)
              result.add(leaderHeaderIndex, getLeaderHeader(previousLeaderValue));

              // add end table line
              result.add(getTableEnd());

              // run subreport
              try {
                ArrayList bind = new ArrayList();
                bind.add(prevBindObject);
                ActivitiesSubReportBuilder sub = new ActivitiesSubReportBuilder(reportId, getDataSource(), getConnection(), bind);
                result.addAll(sub.runReport());
              } catch (Exception e) {
                e.printStackTrace();
              }

              // add table header line for new leader
              result.add(getTableHeader());

            }

            previousLeaderValue = currentLeaderValue; // set new leader
            prevBindObject = currBindObject; // set new bind object
            prevBindValue = currBindValue; // set new bind value

            leaderCount = 0; // reset leaderCount
            leaderHeaderIndex = result.size() - 1; // set new header index

          }
          ++leaderCount;

          contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
          contentRow.setReportRow(outputRow);
          result.add(contentRow);
        }

        // add end table line
        result.add(getTableEnd());

        // actions for last leader
        if (size > 0) {

          // add header line for last leader
          result.add(leaderHeaderIndex, getLeaderHeader(previousLeaderValue));

          // run subreport
          try {
            ArrayList bind = new ArrayList();
            bind.add(currBindObject);
            ActivitiesSubReportBuilder sub = new ActivitiesSubReportBuilder(reportId, getDataSource(), getConnection(), bind);
            result.addAll(sub.runReport());
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        // add Report Totals line
        result.add(getSimpleLine("Report Totals"));

        // run subreport for totals
        try {
          ActivitiesSubReportBuilder sub = new ActivitiesSubReportBuilder(reportId, getDataSource(), getConnection(), null);
          result.addAll(sub.runReport());
        } catch (Exception e) {
          e.printStackTrace();
        }
        try {
          ActivitiesSubReport2Builder sub = new ActivitiesSubReport2Builder(reportId, getDataSource(), getConnection(), null);
          result.addAll(sub.runReport());
        } catch (Exception e) {
          e.printStackTrace();
        }

    }
  }

  /**
   * Gets output line
   * @param leaderValue String
   * @return ReportContentString
   */
  private ReportContentString getLeaderHeader(String leaderValue)
  {
    ReportContentString headerRow = new ReportContentString();
    ArrayList header = new ArrayList();
    headerRow.setShowType(ReportContentString.SHOW_TYPE_LINE);

    header.add(new StringMember(getColumnName(getLeader()), getColumnName(getLeader()) + ":", 'r', "", 'T', false));

    header.add(new StringMember(getColumnName(getLeader()), leaderValue, 'r', "", 'T', false));

    headerRow.setReportRow(header);

    return headerRow;
  }

}
