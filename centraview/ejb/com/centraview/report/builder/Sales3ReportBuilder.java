/*
 * $RCSfile: Sales3ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:58 $ - $Author: mking_cv $
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
 * <p>Title: Sales3ReportBuilder class </p>
 * <p>Description: Class for Sales3 'Proposal detail by user'
 * standard report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Sales3ReportBuilder  extends ReportBuilder {

    private int reportId;
    private int leaderBind; // index of bind for leader

    private String dateClause = "";
    ArrayList dateBind = null;

    /**
     * constructor
     *
     * @exception   Exception
     *
     */
    public Sales3ReportBuilder(String ds) throws Exception {
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
    public Sales3ReportBuilder(int reportId,
                               java.sql.Date dateFrom,
                               java.sql.Date dateTo,
                               String ds,
                               String whereClause) throws Exception {
        super(dateFrom, dateTo, ds);

        if ( reportId != SALES3_REPORT_ID ) {
            throw new Exception("Sales3ReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = new ReportColumn[12];
        colName[0] = new ReportColumn("ID", false);
        colName[1] = new ReportColumn("Item", true);
        colName[2] = new ReportColumn("Description", true);
        colName[3] = new ReportColumn("Proposal ID", true);
        colName[4] = new ReportColumn("Quantity", true);
        colName[5] = new ReportColumn("Cost", true);
        colName[6] = new ReportColumn("Extended cost", true);
        colName[7] = new ReportColumn("Price", true);
        colName[8] = new ReportColumn("Extended price", true);
        colName[9] = new ReportColumn("Gross Profit", true);
        colName[10] = new ReportColumn("SalePersonId", false);
        colName[11] = new ReportColumn("Sales Person:", false); // leader

        setLeader(11);
        leaderBind = 10;

        setColumns(colName);

        // drop temp table
        addPriorSQL("DROP TABLE IF EXISTS repSales3");
        addPriorSQLBind(null);

        // create temp table for report content
        addPriorSQL("CREATE TEMPORARY TABLE repSales3 " +
                    "SELECT it.ItemID AS ID, it.Title, it.Description, pi.ProposalID, " +
                    "pi.Quantity, it.Cost, pi.Quantity*it.Cost AS ExtCost, "+
                    "pi.Price, pi.Quantity*pi.Price AS ExtPrice," +
                    "p.Owner AS SalePersonID, CONCAT(i.firstname, ' ', i.lastname) " +
                    "AS SalePersonName, op.opportunityId AS OpportunityId, ent.entityId AS EntityId, "+
                    " p.AccountManager " +
                    "FROM proposalitem pi "+
                    "LEFT OUTER JOIN proposal p ON (pi.ProposalID=p.ProposalID) " +
                    "LEFT OUTER JOIN item it ON (pi.ItemID=it.ItemID) " +
                    "LEFT OUTER JOIN individual i ON (i.individualID=p.Owner) " +
                    "LEFT OUTER JOIN opportunity op ON (op.opportunityId=p.opportunityId) " +
                    "LEFT OUTER JOIN entity ent ON (ent.entityId=op.EntityId) WHERE 1=1 "
                    + whereClause);

        addPriorSQLBind(null);

        // select report content
        addReportSQL("SELECT ID, Title, Description, ProposalID, Quantity, " +
                     "Cost, ExtCost, Price, ExtPrice, "+
                     "ExtPrice - ExtCost, " +
                     "SalePersonId, SalePersonName " +
                     "FROM repSales3 " +
                     "ORDER BY SalePersonId, EntityId, AccountManager");
        addReportSQLBind(null);

        // drop temp table
        addPostSQL("DROP TABLE IF EXISTS repSales3");
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

        float currTotal1Amount = 0;
        float currTotal2Amount = 0;
        float currTotal3Amount = 0;
        float total1Amount = 0;
        float total2Amount = 0;
        float total3Amount = 0;
        float repTotal1Amount = 0;
        float repTotal2Amount = 0;
        float repTotal3Amount = 0;
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

                            if (k == 4) {
                                // quantiy
                               column = new String(column.toString());
                            }

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

                            if (k == 6) {
                                // total amount sum 1
                                if (column instanceof Float) {
                                    currTotal1Amount = ( (Float) column).floatValue();
                                }
                                else if (column instanceof Double) {
                                    currTotal1Amount = ( (Double) column).floatValue();
                                }

                            }
                            else if (k == 8) {
                                // total amount sum 2
                                if (column instanceof Float) {
                                    currTotal2Amount = ( (Float) column).floatValue();
                                }
                                else if (column instanceof Double) {
                                    currTotal2Amount = ( (Double) column).floatValue();
                                }
                            }
                            else if (k == 9) {
                                // total amount sum 3
                                if (column instanceof Float) {
                                    currTotal3Amount = ( (Float) column).floatValue();
                                }
                                else if (column instanceof Double) {
                                    currTotal3Amount = ( (Double) column).floatValue();
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
                            result.add(getTotals(total1Amount, total2Amount, total3Amount,false));

                            // add end table line
                            result.add(getTableEnd());

                            // add table header line for new leader
                            result.add(getTableHeader());

                        }

                        previousLeaderValue = currentLeaderValue; // set new leader
                        prevBindObject = currBindObject; // set new bind object
                        prevBindValue = currBindValue; // set new bind value

                        leaderCount = 0; // reset leaderCount
                        leaderHeaderIndex = result.size() - 1; // set new header index

                        total1Amount = 0;
                        total2Amount = 0;
                        total3Amount = 0;
                    }
                    ++leaderCount;

                    total1Amount += currTotal1Amount;
                    repTotal1Amount += currTotal1Amount;
                    total2Amount += currTotal2Amount;
                    repTotal2Amount += currTotal2Amount;
                    total3Amount += currTotal3Amount;
                    repTotal3Amount += currTotal3Amount;

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
                    result.add(getTotals(total1Amount,
                                         total2Amount, total3Amount,false));

                    // add empty row
                    result.add(getEmptyTableRow());

                    // add report total amount line
                    result.add(getTotals(repTotal1Amount,
                                         repTotal2Amount, repTotal3Amount,true));

                    // add end table line
                    result.add(getTableEnd());

                    // run subreport
                    try {
                        Sales3SubReportBuilder sub =
                            new Sales3SubReportBuilder(reportId,
                                                       getDataSource(),
                                                       getConnection());
                        result.addAll(sub.runReport());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    // add empty row
                    result.add(getEmptyTableRow());

                    // add report total amount line
                    result.add(getTotals(repTotal1Amount,
                                         repTotal2Amount, repTotal3Amount,true));

                    // add end table line
                    result.add(getTableEnd());
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
                                    leaderValue, 'r', "",'T', false));

        headerRow.setReportRow(header);

        return headerRow;
    }

    /**
     * Gets output line for total amounts
     *
     * @param total1Amount float
     * @param total2Amount float
     * @param total3Amount float
     * @param report boolean
     * @return ReportContentString
     *
     */
    private ReportContentString getTotals(float total1Amount,
                                          float total2Amount,
                                          float total3Amount,
                                          boolean report) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();
        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
        ReportColumn[] colName = getColumns();

         for (int i = 0; i < 6; ++i) {

            if (colName[i].isVisible()) {

                if ( i == 1 ) {

                    header.add(new StringMember("Report Total",
                                                (report)?"Report Totals":"Totals",
                                                'r', "", 'T', false));
                } else {

                    header.add(new StringMember("", "", 'r', "", 'T', false));
                }
            }
        }

        header.add(new MoneyMember("Total1 Amount", new Float(total1Amount),
                                   'r', "", 'T', false, 10));

        header.add(new StringMember("", "", 'r', "", 'T', false));

        header.add(new MoneyMember("Total2 Amount", new Float(total2Amount),
                                   'r', "", 'T', false, 10));

        header.add(new MoneyMember("Total3 Amount", new Float(total3Amount),
                                   'r', "", 'T', false, 10));

        headerRow.setReportRow(header);

        return headerRow;
    }

}
