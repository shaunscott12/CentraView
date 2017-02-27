/*
 * $RCSfile: Support1ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:59 $ - $Author: mking_cv $
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
 *
 * <p>Title: Support1ReportBuilder class </p>
 * <p>Description: Class for Support1 standard report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Support1ReportBuilder  extends ReportBuilder {

    private int reportId;

    private String dateClause = "";
    ArrayList dateBind = null;

    /**
     * constructor
     *
     * @param ds String
     * @exception   Exception
     *
     */
    public Support1ReportBuilder(String ds) throws Exception {
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
    public Support1ReportBuilder(int reportId,
                                 java.sql.Date dateFrom,
                                 java.sql.Date dateTo,
                                 String ds,
                                 String whereClause) throws Exception {
        super(dateFrom, dateTo, ds);

        if (reportId != SUPPORT1_REPORT_ID &&
            reportId != SUPPORT2_REPORT_ID &&
            reportId != SUPPORT3_REPORT_ID) {
            throw new Exception("Support1ReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = null;

        switch (this.reportId) {
            case SUPPORT1_REPORT_ID :

                colName = new ReportColumn[16];
                colName[0] = new ReportColumn("TicketId", true);
                colName[1] = new ReportColumn("Title", true);
                colName[2] = new ReportColumn("Description", true);
                colName[3] = new ReportColumn("EntityId", false);
                colName[4] = new ReportColumn("Entity", true);
                colName[5] = new ReportColumn("IndividualId", false);
                colName[6] = new ReportColumn("Individual", true);
                colName[7] = new ReportColumn("Status", true);
                colName[8] = new ReportColumn("StartDate", true);
                colName[9] = new ReportColumn("EndDate", true);
                colName[10] = new ReportColumn("AssignedId", false);
                colName[11] = new ReportColumn("AssignedTo", true);
                colName[12] = new ReportColumn("Manager", true);
                colName[13] = new ReportColumn("Reference", true);
                colName[14] = new ReportColumn("Reference Detail", true);
                colName[15] = new ReportColumn("Owner:", false);

                setLeader(15);
                break;
            case SUPPORT2_REPORT_ID :

                colName = new ReportColumn[14];
                colName[0] = new ReportColumn("TicketId", true);
                colName[1] = new ReportColumn("Title", true);
                colName[2] = new ReportColumn("Description", true);
                colName[3] = new ReportColumn("Id:", false);
                colName[4] = new ReportColumn("Id2:", false);
                colName[5] = new ReportColumn("Entity Name and Id:", false);
                colName[6] = new ReportColumn("IndividualId", false);
                colName[7] = new ReportColumn("Individual", false);
                colName[8] = new ReportColumn("Status", true);
                colName[9] = new ReportColumn("StartDate", true);
                colName[10] = new ReportColumn("EndDate", true);
                colName[11] = new ReportColumn("AssignedTo", true);
                colName[12] = new ReportColumn("Manager", true);
                colName[13] = new ReportColumn("OwnerName", false);

                setLeader(5);

                // set custom fields
                setTableName("ticket");
                setCustomFieldNames();

                break;
            case SUPPORT3_REPORT_ID :
                colName = new ReportColumn[13];
                colName[0] = new ReportColumn("TicketId", true);
                colName[1] = new ReportColumn("Title", true);
                colName[2] = new ReportColumn("Description", true);
                colName[3] = new ReportColumn("EntityId", false);
                colName[4] = new ReportColumn("Entity", true);
                colName[5] = new ReportColumn("IndividualId", false);
                colName[6] = new ReportColumn("Individual", false);
                colName[7] = new ReportColumn("Status", true);
                colName[8] = new ReportColumn("StartDate", true);
                colName[9] = new ReportColumn("EndDate", true);
                colName[10] = new ReportColumn("AssignedTo", true);
                colName[11] = new ReportColumn("Manager", true);
                colName[12] = new ReportColumn("OwnerName", false);

                setLeader(-1);

                // set custom fields
                setTableName("ticket");
                setCustomFieldNames();

                break;

        }

        setColumns(colName);

        // drop temp table
        addPriorSQL("DROP TABLE IF EXISTS repSupport1");
        addPriorSQLBind(null);

        java.sql.Timestamp from = (getDateFrom() == null)? null :
            new java.sql.Timestamp(getDateFrom().getTime());
        java.sql.Timestamp to   = (getDateTo() == null)? null :
            new java.sql.Timestamp(getDateTo().getTime()+24*60*60*1000);

        // dateClause is used in subreport
        dateClause = ((from != null) ? " AND (tick.created >= ? OR tick.created IS NULL) "+
                                       "AND (tick.dateclosed >= ? OR tick.dateclosed IS NULL)" : "") +
                     ((to   != null) ? " AND (tick.created < ? OR tick.created IS NULL) "+
                                       "AND (tick.dateclosed < ? OR tick.dateclosed IS NULL)" : "");
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
        addPriorSQL("CREATE TEMPORARY TABLE repSupport1 " +

                    ( (reportId == SUPPORT1_REPORT_ID) ?

                     "SELECT tick.ticketid AS TicketId,tick.subject AS Title, " +
                     "tick.description AS Description, " +
                     "en.name AS Entity, tick.entityid AS EntityId, " +
                     "tick.owner AS OwnerId," +
                     "concat(indv3.FirstName,' ',indv3.LastName) AS OwnerName, " +
                     "tick.individualid AS IndividualId, " +
                     "concat(indv1.FirstName,' ',indv1.LastName) AS IndividualName, " +
                     "tick.created AS StartDate," +
                     "tick.assignedto AS AssignedId, supstat.name AS Status, " +
                     "tick.dateclosed AS EndDate, " +
                     "concat(indv.FirstName,' ',indv.LastName) AS AssignedTo, " +
                     "tick.manager AS ManagerId, " +
                     "concat(indv2.FirstName,' ',indv2.LastName) AS Manager, " +
                     "thread.title AS Reference, thread.detail AS ReferenceDetail " +
                     "FROM ticket tick " +
                     "LEFT OUTER JOIN entity en ON en.entityid=tick.entityid " +
                     "LEFT OUTER JOIN individual indv3 ON indv3.individualid=tick.owner " +
                     "LEFT OUTER JOIN individual indv1 ON indv1.individualid=tick.individualId " +
                     "LEFT OUTER JOIN individual indv ON indv.individualid=tick.assignedto " +
                     "LEFT OUTER JOIN supportstatus supstat ON tick.status=supstat.statusid " +
                     "LEFT OUTER JOIN individual indv2 ON indv2.individualId = tick.Manager " +
                     "LEFT OUTER JOIN thread thread ON thread.ticketid = tick.ticketid"

                     :

                     "SELECT tick.ticketid AS TicketId,tick.subject AS Title, "+
                     "tick.description AS Description, "+
                     "en.name AS Entity, tick.entityid AS EntityId, "+
                     "en.ExternalId AS EntityId2,"+
                     "tick.owner AS OwnerId,"+
                     "concat(indv3.FirstName,' ',indv3.LastName) AS OwnerName, "+
                     "tick.individualid AS IndividualId, "+
                     "concat(indv1.FirstName,' ',indv1.LastName) AS IndividualName, "+
                     "tick.created AS StartDate,"+
                     "tick.assignedto AS AssignedId, supstat.name AS Status, "+
                     "tick.dateclosed AS EndDate, "+
                     "concat(indv.FirstName,' ',indv.LastName) AS AssignedTo, "+
                     "tick.manager AS ManagerId, "+
                     "concat(indv2.FirstName,' ',indv2.LastName) AS Manager "+
                     "FROM ticket tick "+
                     "LEFT OUTER JOIN entity en ON en.entityid=tick.entityid "+
                     "LEFT OUTER JOIN individual indv3 ON indv3.individualid=tick.owner "+
                     "LEFT OUTER JOIN individual indv1 ON indv1.individualid=tick.individualId "+
                     "LEFT OUTER JOIN individual indv ON indv.individualid=tick.assignedto "+
                     "LEFT OUTER JOIN supportstatus supstat ON tick.status=supstat.statusid "+
                     "LEFT OUTER JOIN individual indv2 ON indv2.individualId = tick.Manager"
                     )

                   +" WHERE 1=1 "+ whereClause+dateClause );

        addPriorSQLBind(dateBind);

        // select report content
        addReportSQL( (reportId == SUPPORT1_REPORT_ID) ?
                     "SELECT TicketID, Title, Description, EntityId, Entity, " +
                     "IndividualId, IndividualName, Status, StartDate, EndDate, " +
                     "AssignedId,AssignedTo, Manager, Reference, ReferenceDetail, OwnerName " +
                     "FROM repSupport1 " +
                     "ORDER BY OwnerId, EntityId"

                     :
                     ((reportId == SUPPORT2_REPORT_ID) ?
                     "SELECT TicketID, Title, Description, EntityId,EntityId2,Entity, " +
                     "IndividualId, IndividualName, Status, StartDate, EndDate, " +
                     "AssignedTo, Manager, OwnerName " +
                     "FROM repSupport1 " +
                     "ORDER BY EntityId"
                     :
                     "SELECT TicketID, Title, Description, EntityId, Entity, " +
                     "IndividualId, IndividualName, Status, StartDate, EndDate, " +
                     "AssignedTo, Manager, OwnerName " +
                     "FROM repSupport1 " +
                     "ORDER BY Entity")

                     );
        addReportSQLBind(null);

        // drop temp table
        addPostSQL("DROP TABLE IF EXISTS repSupport1");
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
        String currentLeaderValue2 = "";
        String currentLeaderValue3 = "";
        int leaderHeaderIndex = 0;

        ReportColumn[] cols = getColumns();
        int colNumber = cols.length;
        int rowId = 0;

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
                            if ( reportId == SUPPORT2_REPORT_ID) {
                                if (k == 3) {
                                    // get leader Id column value
                                    currentLeaderValue2 = element.getDisplayString();
                                }
                                if (k == 4) {
                                    // get leader Id2 column value
                                    currentLeaderValue3 = element.getDisplayString();
                                }
                            }
                            if (k == getLeader()) {
                                // get leader column value
                                currentLeaderValue = element.getDisplayString();
                            }
                            if (k == 0 && reportId != SUPPORT1_REPORT_ID) {
                                rowId = Integer.parseInt(column.toString());
                            }
                        }
                    }

                    if (previousLeaderValue == null ||
                        !previousLeaderValue.equals(currentLeaderValue)) {
                        // leader changed, now we need form header for

                        if (leaderCount > 0 && reportId != SUPPORT3_REPORT_ID) {
                            // add header line for previous leader(leader name and Record Count)
                            result.add(leaderHeaderIndex,
                                       (reportId == SUPPORT1_REPORT_ID) ?
                                       getLeaderHeader(previousLeaderValue)
                                       :
                                       getLeaderHeader(previousLeaderValue,3,currentLeaderValue2,4,currentLeaderValue3)
                                       );

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

                    // add custom field values
                    if (reportId != SUPPORT1_REPORT_ID) {
                        try {
                            addCustomFieldValues(contentRow, rowId);
                        }
                        catch (Exception e) {
                        }
                    }

                    result.add(contentRow);
                }

                // add end table line
                result.add(getTableEnd());

                // actions for last leader
                if (size > 0 && reportId != SUPPORT3_REPORT_ID) {

                    // add header line for last leader
                    result.add(leaderHeaderIndex,
                               (reportId == SUPPORT1_REPORT_ID) ?
                               getLeaderHeader(previousLeaderValue)
                               :
                               getLeaderHeader(previousLeaderValue,3,currentLeaderValue2,4,currentLeaderValue3)
                               );
                }

                // add Report Totals line
                result.add(getSimpleLine("Report Totals"));

                // run subreport for totals
                try {
                    SupportSubReport2Builder sub =
                        new SupportSubReport2Builder(reportId,
                                                     getDataSource(),
                                                     getConnection());
                    result.addAll(sub.runReport());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                // run subreport for totals
                try {
                    SupportSubReportBuilder sub =
                        new SupportSubReportBuilder(reportId,
                                                    getDataSource(),
                                                    getConnection());
                    result.addAll(sub.runReport());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                break;
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
                                    getColumnName(getLeader())+" "+leaderValue,
                                    'r', "", 'T', false));

        headerRow.setReportRow(header);

        return headerRow;
    }

    /**
     * Gets output line
     *
     * @param leaderValue1 String
     * @param leaderCol2 int
     * @param leaderValue2 String
     * @param leaderCol3 int
     * @param leaderValue3 String
     * @return ReportContentString
     *
     */
    private ReportContentString getLeaderHeader(String leaderValue1,
                                                int leaderCol2,
                                                String leaderValue2,
                                                int leaderCol3,
                                                String leaderValue3) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();
        headerRow.setShowType(ReportContentString.SHOW_TYPE_LINE);

        header.add(new StringMember(getColumnName(getLeader()),
                                    getColumnName(getLeader())+" "+leaderValue1,
                                    'r', "", 'T', false));

        header.add(new StringMember(getColumnName(leaderCol2),
                                    getColumnName(leaderCol2)+" "+leaderValue2,
                                    'r', "", 'T', false));

        header.add(new StringMember(getColumnName(leaderCol3),
                                    getColumnName(leaderCol3)+" "+leaderValue3,
                                    'r', "", 'T', false));

        headerRow.setReportRow(header);

        return headerRow;
    }

}
