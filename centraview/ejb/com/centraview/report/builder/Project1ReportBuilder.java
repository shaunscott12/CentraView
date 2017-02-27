/*
 * $RCSfile: Project1ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:57 $ - $Author: mking_cv $
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

public class Project1ReportBuilder
    extends ReportBuilder {

    private int reportId;

    private String dateClause = "";
    ArrayList dateBind = null;

    /**
     * constructor
     *
     * @param ds String datasource
     * @exception   Exception
     *
     */

    public Project1ReportBuilder(String ds) throws Exception {
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

    public Project1ReportBuilder(int reportId,
                                 java.sql.Date dateFrom,
                                 java.sql.Date dateTo,
                                 String ds,
                                 String whereClause) throws Exception {
        super(dateFrom, dateTo, ds);

        if ( reportId != PROJECTS1_REPORT_ID
//             && reportId != PROJECTS2_REPORT_ID
             ) {
            throw new Exception("Project1ReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = new ReportColumn[12];
        colName[0] = new ReportColumn("TaskId", true);
        colName[1] = new ReportColumn("Task", true);
        colName[2] = new ReportColumn("ParentTask", true);
        colName[3] = new ReportColumn("Status", true);
        colName[4] = new ReportColumn("StartDate", true);
        colName[5] = new ReportColumn("EndDate", true);
        colName[6] = new ReportColumn("TimeBudgeted", true);
        colName[7] = new ReportColumn("TimeUsed", true);
        colName[8] = new ReportColumn("TimeLeft", true);
        colName[9] = new ReportColumn("PercentageComplete", true);
        colName[10] = new ReportColumn("ProjectId", false);
        colName[11] = new ReportColumn("ProjectName", false);

        setLeader(11);

        setColumns(colName);

        // drop temp table
        addPriorSQL("DROP TABLE IF EXISTS repProject1");
        addPriorSQLBind(null);

        java.sql.Timestamp from = (getDateFrom() == null)? null :
            new java.sql.Timestamp(getDateFrom().getTime());
        java.sql.Timestamp to   = (getDateTo() == null)? null :
            new java.sql.Timestamp(getDateTo().getTime()+24*60*60*1000);

        // dateClause is used in subreport
        dateClause = ((from != null) ? " AND (act.Start >= ? OR act.Start IS NULL) "+
                                       "AND (act.End >= ? OR act.End IS NULL)" : "") +
            ((to   != null) ? " AND (act.Start < ? OR act.Start IS NULL) "+
                              "AND (act.End < ? OR act.End IS NULL)" : "");
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
        addPriorSQL("CREATE TEMPORARY TABLE repProject1 " +

                    ( (reportId == PROJECTS1_REPORT_ID) ?

                     "SELECT task.activityid AS TaskId, act.title AS Task, " +
                     "act.Type AS TypeId, act2.title AS ParentTask, " +
                     "task.projectId AS ProjectId, prj.ProjectTitle AS ProjectName, " +
                     "prj.Owner AS OwnerId, " +
                     "concat(indv.FirstName,' ',indv.LastName) AS OwnerName, " +
                     "act.start AS StartDate, act.end AS EndDate, " +
                     "task.percentComplete AS PercentageComplete, " +
                     "prj.BudgetedHours AS TimeBudgeted, prj.HoursUsed AS TimeUsed, " +
                     "prj.BudgetedHours - prj.HoursUsed AS TimeLeft, " +
                     "acts.name AS Status " +
                     "FROM task task " +
                     "LEFT OUTER JOIN activity act ON act.activityId=task.activityid " +
                     "LEFT OUTER JOIN activity act2 ON act2.activityId=task.parent " +
                     "LEFT OUTER JOIN project prj ON prj.projectId = task.projectId " +
                     "LEFT OUTER JOIN individual indv ON indv.individualid=prj.Owner " +
                     "LEFT OUTER JOIN activitystatus acts ON acts.statusid=act.status"
                     :

                     "")+
                   " WHERE 1=1 "+ whereClause+dateClause );

        addPriorSQLBind(dateBind);

        // select report content
        addReportSQL(
            "SELECT TaskId, Task, ParentTask, Status, StartDate, EndDate, " +
            "TimeBudgeted, TimeUsed, TimeLeft, PercentageComplete, " +
            "ProjectId, ProjectName FROM repProject1 " +
            "ORDER BY ProjectId, StartDate, EndDate");
        addReportSQLBind(null);

        // drop temp table
        addPostSQL("DROP TABLE IF EXISTS repProject1");
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
                            element = outputObject(column, getColumnName(k));

                            // Id value
                            if (k == getLeader()) {
                                // get leader column value
                                currentLeaderValue = element.getDisplayString();
                            }

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

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // add end table line
                result.add(getTableEnd());

                // actions for last leader
                if (size > 0) {

                    // add header line for last leader
                    result.add(leaderHeaderIndex,
                               getLeaderHeader(previousLeaderValue));
                }

                // run subreport 1
                try {
                    ProjectSubReportBuilder sub =
                        new ProjectSubReportBuilder(reportId,
                                                    getDataSource(),
                                                    getConnection(),
                                                    null);
                    result.addAll(sub.runReport());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                // run subreport 2
                try {
                    Project2SubReportBuilder sub =
                        new Project2SubReportBuilder(reportId,
                                                     getDataSource(),
                                                     getConnection(),
                                                     null);
                    result.addAll(sub.runReport());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

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
                                    leaderValue, 'r', "", 'T', false));

        headerRow.setReportRow(header);

        return headerRow;
    }

}
