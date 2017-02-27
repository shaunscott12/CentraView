/*
 * $RCSfile: Hr3ReportBuilder.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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

import com.centraview.common.DateUtility;
import com.centraview.common.ListElementMember;
import com.centraview.common.StringMember;
import com.centraview.report.valueobject.ReportContentString;

/**
 *
 * <p>Title: HR3ReportBuilder class </p>
 * <p>Description: Class for HR3/HH4 standard reports building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Hr3ReportBuilder  extends ReportBuilder {

    private int reportId;
    private int leaderBind; // index of bind for leader

    private String dateClause = "";
    ArrayList dateBind = null;

    /**
     * constructor
     *
     * @param ds String datasource
     * @exception   Exception
     *
     */
    public Hr3ReportBuilder(String ds) throws Exception {
      super(ds);
    }

    /**
     * constructor
     *
     * @param reportId int
     * @param dateFrom java.sql.Date
     * @param dateTo java.sql.Date
     * @param clause ArrayList
     * @param ds String
     * @exception   Exception
     *
     */
    public Hr3ReportBuilder(int reportId,
                               java.sql.Date dateFrom,
                               java.sql.Date dateTo,
                               String ds,
                               String whereClause) throws Exception {
        super(dateFrom, dateTo, ds);

        if ( reportId != HR3_REPORT_ID && reportId != HR4_REPORT_ID ) {
            throw new Exception("HR3ReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = new ReportColumn[16];
        colName[0] = new ReportColumn("ID", true);
        colName[1] = new ReportColumn("Date", true);
        colName[2] = new ReportColumn("Start Time", true);
        colName[3] = new ReportColumn("End Time", true);
        colName[4] = new ReportColumn("Duration", true); // calculate sum
        colName[5] = new ReportColumn("Break Duration", true);// calculate sum
        colName[6] = new ReportColumn("Net Duration", true);// calculate sum
        colName[7] = new ReportColumn("EntityId", false);
        colName[8] = new ReportColumn("Entity", (reportId == HR3_REPORT_ID)?true:false);
        colName[9] = new ReportColumn("UserId", false);
        colName[10] = new ReportColumn("User", (reportId == HR3_REPORT_ID)?false:true);
        colName[11] = new ReportColumn("ActivityId", false);
        colName[12] = new ReportColumn("TicketId", false);
        colName[13] = new ReportColumn("ProjectId", false);
        colName[14] = new ReportColumn("Reference Type", true);
        colName[15] = new ReportColumn("Reference", true);

        setLeader((reportId == HR3_REPORT_ID)?10:8);

        setColumns(colName);

        // drop temp table
        addPriorSQL("DROP TABLE IF EXISTS repHR3");
        addPriorSQLBind(null);

        java.sql.Date from = (getDateFrom() == null)? null :
            new java.sql.Date(getDateFrom().getTime());
        java.sql.Date to   = (getDateTo() == null)? null :
            new java.sql.Date(getDateTo().getTime()+24*60*60*1000);

        // dateClause is used in subreport
        dateClause = ((from != null) ? "AND (tsl.Date >= ? OR tsl.Date IS NULL) " : "") +
                     ((to   != null) ? "AND (tsl.Date < ? OR tsl.Date IS NULL) " : "");
        // dateBind is used in subreport
        dateBind = (from != null || to != null) ? new ArrayList() : null;
        if (from != null) {
            dateBind.add(from);
        }
        if (to != null) {
            dateBind.add(to);
        }

        // create temp table for report content
        addPriorSQL("CREATE TEMPORARY TABLE repHR3 " +
                    "SELECT tsl.TimeSlipID AS ID, tsl.Date AS Date, "+
                    "tsl.Start AS StartTime, tsl.End AS EndTime, "+
                    "TIME_TO_SEC(tsl.End) - TIME_TO_SEC(tsl.Start) AS Duration, "+
                    "CAST(((tsl.BreakTime+abs(tsl.BreakTime))/2)*60*60 AS UNSIGNED) AS BreakDuration, "+
                    "TIME_TO_SEC(tsl.End) - TIME_TO_SEC(tsl.Start)- "+
                    "CAST(((tsl.BreakTime+abs(tsl.BreakTime))/2)*60*60 AS UNSIGNED) AS NetDuration, "+
                    "tsl.ProjectID AS ProjectId, tsl.ActivityID AS ActivityId,  "+
                    "ent.EntityId AS EntityId, ent.Name AS EntityName,"+
                    "tsl.TicketID AS TicketId, tsl.CreatedBy AS UserId,  "+
                    "concat(indv.FirstName,' ',indv.LastName) AS UserName, "+
                    "CASE  "+
                    "WHEN tsl.TicketID IS NOT NULL AND tsl.TicketID>0  "+
                    "THEN 'Ticket' "+
                    "WHEN tsl.ProjectID IS NOT NULL AND tsl.ProjectID>0 "+
                    "THEN 'Project' "+
                    "END AS ReferenceType, "+
                    "CASE  "+
                    "WHEN tsl.TicketID IS NOT NULL AND tsl.TicketID>0  "+
                    "THEN tick.subject  "+
                    "WHEN tsl.ProjectID IS NOT NULL AND tsl.ProjectID>0 "+
                    "THEN prj.ProjectTitle  "+
                    "END AS Reference  "+
                    "FROM timeslip tsl "+
                    "LEFT JOIN activity act ON act.activityId = tsl.activityId "+
                    "LEFT JOIN activitylink actl ON actl.activityId = tsl.activityid "+
                    "AND actl.recordtypeid=1 "+
                    "LEFT JOIN project prj ON prj.projectid=tsl.projectid "+
                    "LEFT JOIN ticket tick ON tick.ticketid=tsl.ticketid "+
                    "LEFT JOIN individual indv ON indv.individualid=tsl.createdBy "+
                    "LEFT JOIN projectlink prjlink ON prjlink.projectId=tsl.projectid "+
                    "AND prjlink.recordtypeid=1  "+
                    "LEFT JOIN entity ent ON ent.entityid = tick.entityid OR "+
                    "ent.entityid = prjlink.recordid OR ent.entityid = actl.recordid "+
                    "WHERE 1=1 "
                   + whereClause+dateClause);

        addPriorSQLBind(dateBind);

        // select report content
        addReportSQL("SELECT ID, Date, StartTime, EndTime, Duration, " +
                     "BreakDuration, NetDuration,  " +
                     "EntityId, EntityName, UserId, UserName, "+
                     "ActivityId, TicketId, ProjectId, ReferenceType, Reference "+
                     "FROM repHR3 ORDER BY " +
                     (( reportId == HR3_REPORT_ID ) ?
                     "UserId,EntityId" :
                     "EntityId,UserId")
                     );
        addReportSQLBind(null);

        // drop temp table
        addPostSQL("DROP TABLE IF EXISTS repHR3");
        addPostSQLBind(null);

    }

    /**
     * processReport
     *
     * @param i int
     * @param res Vector
     * @param result ArrayList
     */
    public void processReport(int i, Vector res, ArrayList result) {

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

        // current row durations
        long currDuration = 0;
        long currBreakDuration = 0;
        long currNetDuration = 0;

        // current leader total durations
        long duration = 0;
        long breakDuration = 0;
        long netDuration = 0;

        // report total durations
        long repDuration = 0;
        long repBreakDuration = 0;
        long repNetDuration = 0;

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
                        if ( k < colNumber ) {
                            column = row.get(k);

                            if (k == getLeader()) {
                                // get leader column value
                                currentLeaderValue = column.toString();
                            }
                            if (k == 4) {
                                // duration
                                currDuration = ((Long)column).longValue();
                                column = new String(DateUtility.secsToString(currDuration));
                            }
                            else if (k == 5) {
                                // break duration
                                currBreakDuration = ((java.math.BigDecimal)column).intValue();
                                column = new String(DateUtility.secsToString(currBreakDuration));
                            }
                            else if (k == 6) {
                                // net duration
                                currNetDuration = ((java.math.BigDecimal)column).intValue();
                                column = new String(DateUtility.secsToString(currNetDuration));
                            }
                            element = outputObject(column, getColumnName(k));
                            if ( cols[k].isVisible() ) {
                                outputRow.add(element);
                            }

                        }

                    }

                    if (previousLeaderValue == null ||
                        !previousLeaderValue.equals(currentLeaderValue)) {

                        // leader changed, now we need form header for

                        if (leaderCount > 0) {
                            // add header line for previous leader(leader name and Record Count)
                            result.add(leaderHeaderIndex,
                                       getLeaderHeader(previousLeaderValue));

                            // add total amount line
                            result.add(getTotals(duration, breakDuration, netDuration,false));

                            // add end table line
                            result.add(getTableEnd());

                            // add table header line for new leader
                            result.add(getTableHeader());

                        }

                        previousLeaderValue = currentLeaderValue; // set new leader

                        leaderCount = 0; // reset leaderCount
                        leaderHeaderIndex = result.size() - 1; // set new header index

                        duration = 0;
                        breakDuration = 0;
                        netDuration = 0;
                    }
                    ++leaderCount;

                    duration += currDuration;
                    repDuration += currDuration;
                    breakDuration += currBreakDuration;
                    repBreakDuration += currBreakDuration;
                    netDuration += currNetDuration;
                    repNetDuration += currNetDuration;

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // actions for last leader
                if (size > 0) {

                    // add header line for last leader
                    result.add(leaderHeaderIndex,
                               getLeaderHeader(previousLeaderValue));

                    // add total durations line
                    result.add(getTotals(duration, breakDuration, netDuration,false));

                }

                // add empty row
                result.add(getEmptyTableRow());

                // add report total durations line
                result.add(getTotals(repDuration, repBreakDuration, repNetDuration,true));

                // add end table line
                result.add(getTableEnd());
        }
    }

    /**
     * Gets output line
     *
     * @param leaderValue String
     * @return ReportContentString
     *
     */
    private ReportContentString getLeaderHeader(String leaderValue) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();
        headerRow.setShowType(ReportContentString.SHOW_TYPE_LINE);

        header.add(new StringMember(getColumnName(getLeader()),
                                    getColumnName(getLeader()) + ":",
                                    'r', "", 'T', false));

        header.add(new StringMember(getColumnName(getLeader()),
                                    leaderValue, 'r', "",'T', false));

        headerRow.setReportRow(header);

        return headerRow;
    }

    /**
     * Gets output line for total amounts
     *
     * @param duration long
     * @param breakDuration long
     * @param netDuration long
     * @param report boolean
     * @return ReportContentString
     *
     */
    private ReportContentString getTotals(long duration,
                                          long breakDuration,
                                          long netDuration,
                                          boolean report) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();
        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);

        ReportColumn[] cols = getColumns();
        int colNumber = (cols == null ) ? 0 : cols.length;

        int j = 4;
        if (report) {
            header.add(new StringMember("", "Report Totals", 'r', "", 'T', false));
            --j;
        }

        for (int i = 0; i < j && i < colNumber; ++i) {
            if (cols[i].isVisible()) {
                header.add(new StringMember("", "", 'r', "", 'T', false));
            }
        }

        header.add(new StringMember("Duration",
                                    DateUtility.secsToString(duration),
                                    'r', "", 'T', false));

        header.add(new StringMember("BreakDuration",
                                    DateUtility.secsToString(breakDuration),
                                    'r', "", 'T', false));

        header.add(new StringMember("NetDuration",
                                    DateUtility.secsToString(netDuration),
                                    'r', "", 'T', false));

        for (int i = 7; i < colNumber; ++i) {
            if (cols[i].isVisible()) {
                header.add(new StringMember("", "", 'r', "", 'T', false));
            }
        }

        headerRow.setReportRow(header);

        return headerRow;
    }

}
