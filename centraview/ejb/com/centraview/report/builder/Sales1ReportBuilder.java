/*
 * $RCSfile: Sales1ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:58 $ - $Author: mking_cv $
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
import com.centraview.common.MoneyMember;
import com.centraview.common.StringMember;
import com.centraview.report.valueobject.ReportContentString;

/**
 *
 * <p>Title: Sales1ReportBuilder class </p>
 * <p>Description: Class for Sales1 standard report building</p>
 */

public class Sales1ReportBuilder  extends ReportBuilder {

    private int reportId;
    private int leaderBind; // index of bind for leader

    private String dateClause = "";
    ArrayList dateBind = null;

    /**
     * constructor
     *
     * @param ds String
     * @exception   Exception
     *
     */
    public Sales1ReportBuilder(String ds) throws Exception {
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
    public Sales1ReportBuilder(int reportId,
                               java.sql.Date dateFrom,
                               java.sql.Date dateTo,
                               String ds,
                               String whereClause) throws Exception {
        super(dateFrom, dateTo, ds);

        if ( reportId != SALES1_REPORT_ID && reportId != SALES2_REPORT_ID ) {
            throw new Exception("Sales1ReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = new ReportColumn[15];
        colName[0] = new ReportColumn("ID", false);
        colName[1] = new ReportColumn("Date Created", true);
        colName[2] = new ReportColumn("Title", true);
        colName[3] = new ReportColumn("EntityId", false);
        colName[4] = new ReportColumn("Entity", true);
        colName[5] = new ReportColumn("IndividualId", false);
        colName[6] = new ReportColumn("Individual", true);
        colName[7] = new ReportColumn("Status", true);
        colName[8] = new ReportColumn("Stage", true);
        colName[9] = new ReportColumn("EstimatedCloseDate", true);
        colName[10] = new ReportColumn("Total Amount", true); // calculate sum
        colName[11] = new ReportColumn("Probability", true);
        colName[12] = new ReportColumn("Forcasted Amount", true); // calculate sum
        colName[13] = new ReportColumn("SalePersonId", false);
        colName[14] = new ReportColumn("Sales Person:", false); // leader

        setLeader(14);
        leaderBind = 13;

        setColumns(colName);

        // drop temp table
        addPriorSQL("DROP TABLE IF EXISTS repSales1");
        addPriorSQLBind(null);

        // create temp table for report content
        addPriorSQL("CREATE TEMPORARY TABLE repSales1 " +

                    ( (reportId == SALES1_REPORT_ID) ?

                     "SELECT op.OpportunityID AS ID, ac.Created AS DateCreated, "+
                     "op.Title, op.ActualAmount, op.AccountManager, op.EntityID, "+
                     "op.probability AS ProbabilityId," +
                     "en.name AS Entity, op.individualid AS IndividualId, " +
                     "concat(ind2.firstname,  '  ' , ind2.lastname) AS IndividualName, " +
                     "st.Name AS Type, ss.Name AS Stage, sp.Title AS Probability, " +
                     "sst.Name AS Status, ac.start AS EstimatedCloseDate, " +
                     "op.ForecastAmmount, ac.activityid AS ActivityID, ac.owner AS SalePersonID, " +
                     "concat(ind.firstname,  '  ' , ind.lastname) AS SalePersonName " +
                     "FROM opportunity op, activity ac, entity en LEFT JOIN" +
                     " salestype st ON op.TypeID = st.SalesTypeID LEFT JOIN  salesstage ss ON " +
                     "op.Stage = ss.SalesStageID  LEFT JOIN  salesprobability sp ON " +
                     "op.Probability = sp.ProbabilityID LEFT JOIN salesstatus sst ON " +
                     "op.Status = sst.SalesStatusID "+
                     "LEFT JOIN individual ind ON ac.owner = ind.individualID "+
                     "LEFT JOIN individual ind2 ON op.individualid = ind2.individualID "+
                     "WHERE op.activityid = ac.activityid AND " +
                     "op.EntityID = en.EntityID  " :

                     "SELECT p.ProposalID AS ID, p.Created AS DateCreated, p.Title, "+
                     "p.Description, p.probability AS ProbabilityId,"+
                     "ss.Name AS Stage, sp.Title AS Probability, stat.Name AS Status, " +
                     "p.EstimatedCloseDate, p.ActualAmount, p.ForecastAmmount, " +
                     "p.Owner AS SalePersonID, CONCAT(i.firstname, ' ', i.lastname) " +
                     "AS SalePersonName, op.opportunityId AS OpportunityId, ent.entityId AS EntityId, " +
                     "ent.name AS Entity, p.AccountManager AS IndividualId," +
                     "CONCAT(ind2.firstname, ' ', ind2.lastname) AS IndividualName " +
                     "FROM proposal p LEFT OUTER JOIN salesstage ss ON (ss.SalesStageID=p.Stage) " +
                     "LEFT OUTER JOIN salesprobability sp ON (sp.ProbabilityID=p.Probability) " +
                     "LEFT OUTER JOIN salesstatus stat ON (stat.SalesStatusID=p.Status) " +
                     "LEFT OUTER JOIN individual i ON (i.individualID=p.Owner) " +
                     "LEFT OUTER JOIN opportunity op ON (op.opportunityId=p.opportunityId) " +
                     "LEFT OUTER JOIN entity ent ON (ent.entityId=op.EntityId) " +
                     "LEFT OUTER JOIN individual ind2 ON (p.AccountManager=ind2.individualId) WHERE 1=1 ")
                   + whereClause);

        addPriorSQLBind(null);

        java.sql.Timestamp from = (getDateFrom() == null)? null :
                             new java.sql.Timestamp(getDateFrom().getTime());
        java.sql.Timestamp to   = (getDateTo() == null)? null :
                             new java.sql.Timestamp(getDateTo().getTime()+24*60*60*1000);

        // dateClause is used in subreport
        dateClause = ((from != null) ? " AND (DateCreated >= ? OR DateCreated IS NULL) " : "") +
                     ((to   != null) ? " AND (DateCreated < ? OR DateCreated IS NULL) " : "");
        // dateBind is used in subreport
        dateBind = (from != null || to != null) ? new ArrayList() : null;
        if (from != null) {
            dateBind.add(from);
        }
        if (to != null) {
            dateBind.add(to);
        }

        // select report content
        addReportSQL("SELECT ID, DateCreated, Title, EntityId, Entity, " +
                     "IndividualId, IndividualName, Status, Stage, EstimatedCloseDate, " +
                     "ActualAmount, Probability, ForecastAmmount, SalePersonId, SalePersonName " +
                     "FROM repSales1 WHERE 1=1 " +
                     dateClause +
                     "ORDER BY SalePersonId, EntityId, IndividualId");
        addReportSQLBind(dateBind);

        // drop temp table
        addPostSQL("DROP TABLE IF EXISTS repSales1");
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
        Object prevBindObject = null;
        Object currBindObject = null;
        ListElementMember element = null;
        int leaderCount = 0;
        String previousLeaderValue = null;
        String currentLeaderValue = "";
        int leaderHeaderIndex = 0;

        ReportColumn[] cols = getColumns();
        int colNumber = cols.length;

        float currTotalAmount = 0;
        float currForAmount = 0;
        float totalAmount = 0;
        float forecastAmount = 0;
        float repTotalAmount = 0;
        float repForecastAmount = 0;
        String prevBindValue = null;
        String currBindValue = "";


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
                            if ( cols[k].isVisible() ) {
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

                            if (k == 10) {
                                // total amount sum
                                if (column  instanceof Float) {
                                    currTotalAmount = ( (Float) column).floatValue();
                                }
                                else if (column  instanceof Double) {
                                    currTotalAmount = ( (Double) column).floatValue();
                                }

                            }
                            else if (k == 12) {
                                // forecast amount sum
                                if (column  instanceof Float) {
                                    currForAmount = ( (Float) column).floatValue();
                                }
                                else if (column  instanceof Double) {
                                    currForAmount = ( (Double) column).floatValue();
                                }
                            }

                        }

                    }

                    if (prevBindValue == null ||
                       !prevBindValue.endsWith(currBindValue)) {

                        // leader changed, now we need form header for

                        if (leaderCount > 0) {
                            // add header line for previous leader(leader name and Record Count)
                            result.add(leaderHeaderIndex,
                                       getLeaderHeader(previousLeaderValue));

                            // add total amount line
                            result.add(getTotals(totalAmount, forecastAmount));

                            // add end table line
                            result.add(getTableEnd());

                            // run subreport
                            try {
                                ArrayList bind = new ArrayList();
                                bind.add(prevBindObject);
                                SalesSubReportBuilder sub =
                                    new SalesSubReportBuilder(reportId,
                                                              getDataSource(),
                                                              getConnection(),
                                                              dateClause,
                                                              dateBind,
                                                              bind);
                                result.addAll(sub.runReport());
                            }
                            catch (Exception e) {
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

                        totalAmount = 0;
                        forecastAmount = 0;
                    }
                    ++leaderCount;

                    totalAmount += currTotalAmount;
                    repTotalAmount += currTotalAmount;
                    forecastAmount += currForAmount;
                    repForecastAmount += currForAmount;

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

                    // add total amount line
                    result.add(getTotals(totalAmount, forecastAmount));

                    // add end table line
                    result.add(getTableEnd());

                    // run subreport
                    try {
                        ArrayList bind = new ArrayList();
                        bind.add(currBindObject);
                        SalesSubReportBuilder sub =
                            new SalesSubReportBuilder(reportId,
                                                      getDataSource(),
                                                      getConnection(),
                                                      dateClause,
                                                      dateBind,
                                                      bind);
                        result.addAll(sub.runReport());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {

                    // add end table line
                    result.add(getTableEnd());
                }

                // add report total amount line
                result.addAll(getReportTotals(repTotalAmount, repForecastAmount));

                // run subreport for totals
                try {
                    SalesSubReportBuilder sub =
                        new SalesSubReportBuilder(reportId,
                                                  getDataSource(),
                                                  getConnection(),
                                                  dateClause,
                                                  dateBind,
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
                                    getColumnName(getLeader()),
                                    'r', "", 'T', false));

        header.add(new StringMember(getColumnName(getLeader()),
                                    leaderValue, 'r', "",'T', false));

        headerRow.setReportRow(header);

        return headerRow;
    }

    /**
     * Gets output line for total amounts
     *
     * @param totalAmount float
     * @param forecastAmount float
     * @return ReportContentString
     *
     */
    private ReportContentString getTotals(float totalAmount,
                                          float forecastAmount) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();

        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);

        for (int i = 0; i < 7; ++i) {
            header.add(new StringMember("", "", 'r', "", 'T', false));
        }

        header.add(new MoneyMember("Total Amount",
                                   new Float(totalAmount), 'r', "",
                                   'T', false, 10));

        header.add(new StringMember("", "", 'r', "", 'T', false));

        header.add(new MoneyMember("Forecast Amount", new Float(forecastAmount),
                                   'r', "", 'T', false, 10));

        headerRow.setReportRow(header);

        return headerRow;
    }

    /**
     * Gets output lines for report total amounts
     *
     * @param totalAmount float
     * @param forecastAmount float
     * @return ArrayList (of ReportContentString)
     *
     */
    private ArrayList getReportTotals(float totalAmount,
                                      float forecastAmount) {
        ArrayList repTotal = new ArrayList();
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();

        // totals table header
        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_HEADER);

        header.add(new StringMember("", "Report Totals", 'r', "", 'T', false));
        header.add(new StringMember("", "Total Amount", 'r', "", 'T', false));
        header.add(new StringMember("", "Forecast Amount", 'r', "", 'T', false));

        headerRow.setReportRow(header);

        repTotal.add(headerRow);

        // totals table row
        headerRow = new ReportContentString();
        header = new ArrayList();
        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);

        header.add(new StringMember("", "", 'r', "", 'T', false));

        header.add(new MoneyMember("Total Amount",
                                   new Float(totalAmount), 'r', "",
                                   'T', false, 10));

        header.add(new MoneyMember("Forecast Amount", new Float(forecastAmount),
                                   'r', "", 'T', false, 10));

        headerRow.setReportRow(header);
        repTotal.add(headerRow);

        // totals table end
        repTotal.add(getTableEnd());

        return repTotal;
    }

}
