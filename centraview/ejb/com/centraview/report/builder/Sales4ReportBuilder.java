/*
 * $RCSfile: Sales4ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:58 $ - $Author: mking_cv $
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
 * <p>Title: Sales4ReportBuilder class </p>
 * <p>Description: Class for Sales4 'Sales order report'
 * standard report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Sales4ReportBuilder  extends ReportBuilder {

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
    public Sales4ReportBuilder(String ds) throws Exception {
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
    public Sales4ReportBuilder(int reportId,
                               java.sql.Date dateFrom,
                               java.sql.Date dateTo,
                               String ds,
                               String whereClause) throws Exception {
        super(dateFrom, dateTo, ds);

        if ( reportId != SALES4_REPORT_ID ) {
            throw new Exception("Sales4ReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = new ReportColumn[11];
        colName[0] = new ReportColumn("OrderId", false);
        colName[1] = new ReportColumn("DateOrder", true);
        colName[2] = new ReportColumn("EntityId", false);
        colName[3] = new ReportColumn("Entity", true);
        colName[4] = new ReportColumn("IndividualId", false);
        colName[5] = new ReportColumn("Individual", true);
        colName[6] = new ReportColumn("Account Manager", true);
        colName[7] = new ReportColumn("Status", true);
        colName[8] = new ReportColumn("Cost", true);
        colName[9] = new ReportColumn("Order Amount", true);
        colName[10] = new ReportColumn("Gross Profit", true);

        setLeader(-1); // no leader

        setColumns(colName);

        // drop temp table
        addPriorSQL("DROP TABLE IF EXISTS repSales4");
        addPriorSQLBind(null);

        java.sql.Timestamp from = (getDateFrom() == null)? null :
                             new java.sql.Timestamp(getDateFrom().getTime());
        java.sql.Timestamp to   = (getDateTo() == null)? null :
                             new java.sql.Timestamp(getDateTo().getTime()+24*60*60*1000);

        // dateClause is used in subreport
        dateClause = ((from != null) ? "AND (cvord.orderdate >= ? OR cvord.orderdate IS NULL) " : "") +
                     ((to   != null) ? "AND (cvord.orderdate < ? OR cvord.orderdate IS NULL) " : "");
        // dateBind is used in subreport
        dateBind = (from != null || to != null) ? new ArrayList() : null;
        if (from != null) {
            dateBind.add(from);
        }
        if (to != null) {
            dateBind.add(to);
        }


        // create temp table for report content
        addPriorSQL("CREATE TEMPORARY TABLE repSales4 " +

                    "SELECT cvord.orderid AS OrderId,cvord.entityid AS EntityId,"+
                    "ent.name AS EntityName, "+
                    "cvord.status AS StatusId,accstat.title AS status, "+
                    "cvord.orderdate AS DateOrder, "+
                    "cvord.accountmgr AS AccManagerId,"+
                    "concat(indv.firstname,' ',indv.lastname) AS AccManager, "+
                    "cvord.billindividual AS IndividualId, "+
                    "concat(indv2.firstname,' ',indv2.lastname) AS Individual, "+
                    "cvord.shipping AS Cost, cvord.total AS Amount   "+
                    "FROM cvorder cvord  "+
                    "LEFT JOIN entity ent ON cvord.entityid = ent.entityid "+
                    "LEFT JOIN accountingstatus accstat ON cvord.status = accstat.statusid "+
                    "LEFT JOIN individual indv ON cvord.accountmgr= indv.individualid "+
                    "LEFT JOIN individual indv2 ON cvord.billindividual= indv.individualid "

                    + dateClause + whereClause);

        addPriorSQLBind(dateBind);

        // select report content
        addReportSQL("SELECT OrderId, DateOrder, EntityId, EntityName, IndividualId, " +
                     "Individual, AccManagerId , AccManager, Status, Cost, Amount, "+
                     "Amount - Cost AS GrossProfit " +
                     "FROM repSales4 " +
                     "ORDER BY EntityId, AccManagerId");
        addReportSQLBind(null);

        // drop temp table
        addPostSQL("DROP TABLE IF EXISTS repSales4");
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

        float currCost = 0;
        float currAmount = 0;
        float currProfit = 0;
        float repTotalCost = 0;
        float repTotalAmount = 0;
        float repTotalProfit = 0;

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
                            if (k == 8) {
                                // cost
                                if (column instanceof Float) {
                                    currCost = ( (Float) column).floatValue();
                                }
                                else if (column instanceof Double) {
                                    currCost = ( (Double) column).floatValue();
                                }

                            }
                            else if (k == 9) {
                                //  amount
                                if (column instanceof Float) {
                                    currAmount = ( (Float) column).floatValue();
                                }
                                else if (column instanceof Double) {
                                    currAmount = ( (Double) column).floatValue();
                                }
                            }
                            else if (k == 10) {
                                // profit
                                if (column instanceof Float) {
                                    currProfit = ( (Float) column).floatValue();
                                }
                                else if (column instanceof Double) {
                                    currProfit = ( (Double) column).floatValue();
                                }
                            }

                        }

                    }

                    repTotalCost += currCost;
                    repTotalAmount += currAmount;
                    repTotalProfit += currProfit;

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // add report total amount line
                result.add(getTotals(repTotalCost, repTotalAmount, repTotalProfit));

                // add end table line
                result.add(getTableEnd());
        }
    }

    /**
     * Gets output line for total amounts
     *
     * @param totalCost   float
     * @param totalAmount float
     * @param totalProfit float
     * @return ReportContentString
     *
     */
    private ReportContentString getTotals(float totalCost,
                                          float totalAmount,
                                          float totalProfit) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();
        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
        ReportColumn[] colName = getColumns();

         for (int i = 0; i < 8; ++i) {

            if (colName[i].isVisible()) {
                header.add(new StringMember("", "", 'r', "", 'T', false));
            }
        }

        header.add(new MoneyMember("Total Cost",new Float(totalCost),
                                   'r',"",'T',false, 10));

        header.add(new MoneyMember("Total Amount", new Float(totalAmount),
                                   'r', "", 'T', false, 10));

        header.add(new MoneyMember("Total Profit", new Float(totalProfit),
                                   'r', "", 'T', false, 10));

        headerRow.setReportRow(header);

        return headerRow;
    }

}
