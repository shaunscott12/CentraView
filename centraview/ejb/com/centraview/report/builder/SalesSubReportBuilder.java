/*
 * $RCSfile: SalesSubReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:59 $ - $Author: mking_cv $
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

import com.centraview.common.CVDal;
import com.centraview.report.valueobject.ReportContentString;

/**
 *
 * <p>Title: SalesSubReportBuilder class </p>
 * <p>Description: Class for Sales1 standard report child building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class SalesSubReportBuilder
    extends ReportBuilder {

    private int reportId;
    private ArrayList bindArray; // of BindObjects


    /**
     * constructor
     *
     * @param reportId int
     * @param ds String
     * @param con CVDal
     * @param dateBind ArrayList
     * @param bindArray ArrayList
     * @exception   Exception
     *
     */
    public SalesSubReportBuilder(int reportId,
                                 String ds,
                                 CVDal con,
                                 String dateClause,
                                 ArrayList dateBind,
                                 ArrayList bindArray
                                 ) throws Exception {
        super(ds, con);

        if ( reportId != SALES1_REPORT_ID && reportId != SALES2_REPORT_ID ) {
            throw new Exception("SalesSubReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;
        this.bindArray = bindArray;

        ReportColumn[] colName = new ReportColumn[5];
        colName[0] = new ReportColumn("ProbabilityId", false);
        colName[1] = new ReportColumn("Probability", true);
        colName[2] = new ReportColumn("Opportunity Count", true);
        colName[3] = new ReportColumn("Total Amount", true);
        colName[4] = new ReportColumn("Forcasted Amount", true);
        setColumns(colName);

        addPriorSQL("DROP TABLE IF EXISTS repSubSales1");
        // bind ArrayList is empty for this sql
        addPriorSQLBind(null);

        addPriorSQL("DROP TABLE IF EXISTS repSubSales2");
        // bind ArrayList is empty for this sql
        addPriorSQLBind(null);

        // create temp table for report content
        addPriorSQL("CREATE TEMPORARY TABLE repSubSales1 SELECT " +
                    "prob.probabilityId AS ProbabilityId, " +
                    "prob.Title AS Probability, 0 AS Count, 0.0 AS totAmount, " +
                    "0.0 AS forAmount FROM salesprobability prob");
        // bind ArrayList is empty for this sql
        addPriorSQLBind(null);

        addPriorSQL("DROP TABLE IF EXISTS repSubSales2");
        // bind ArrayList is empty for this sql
        addPriorSQLBind(null);

        // create temp table for report content
        String where = dateClause;
        // in case of total count
        if (this.bindArray != null) {
            where += " AND " +
                ( (reportId == SALES1_REPORT_ID || reportId == SALES2_REPORT_ID) ?
                  "SalePersonId" : "Entity") + "=? ";
        }
        addPriorSQL(
            "CREATE TEMPORARY TABLE repSubSales2 SELECT probabilityId, " +
            "COUNT(probabilityId) AS Count, " +
            "SUM(ActualAmount) AS totAmount," +
            "SUM(ForecastAmmount) AS forAmount " +
            "FROM repSales1 WHERE 1=1 " +
            where +
            "GROUP BY probabilityId");
        ArrayList bind = new ArrayList();
        // bind for this sql
        if (dateBind != null) {
            bind.addAll(dateBind);
        }
        if (this.bindArray != null) {
            bind.addAll(this.bindArray);
        }
        addPriorSQLBind(bind);

        addPriorSQL("UPDATE repSubSales1,repSubSales2 SET repSubSales1.Count=" +
                    "repSubSales2.Count, repSubSales1.totAmount=" +
                    "repSubSales2.totAmount, repSubSales1.forAmount=" +
                    "repSubSales2.forAmount " +
                    "WHERE repSubSales1.probabilityId=repSubSales2.probabilityId");
        // bind ArrayList is empty for this sql
        addPriorSQLBind(null);

        // select report rows
        addReportSQL("SELECT ProbabilityId,Probability,Count, " +
                     "totAmount, forAmount FROM repSubSales1 ORDER BY Probability");
        // bind ArrayList is empty for this sql
        addReportSQLBind(null);

        addPostSQL("DROP TABLE IF EXISTS repSubSales1");
        // bind ArrayList is empty for this sql
        addPostSQLBind(null);

        addPostSQL("DROP TABLE IF EXISTS repSubSales2");
        // bind ArrayList is empty for this sql
        addPostSQLBind(null);

    }


    /**
     * Processes report content for output
     *
     * @param i int report number
     * @param res Vector report content
     * @param result ArrayList report content for output
     *
     */
    public void processReport(int i, Vector res, ArrayList result) {
        int size = 0;
        int rowSize = 0;
        ReportContentString contentRow = null;
        ArrayList row = null;
        ArrayList outputRow = null;
        Object column = null;
        float totAmount = 0; // sum of report total amount
        float forAmount = 0; // sum of report forecased amount

        ReportColumn[] cols = getColumns();
        int colNumber = cols.length;

        switch (i) {
            case 0:
                size = res.size();

                // set header line
                result.add(getTableHeader());

                for (int j = 0; j < size; ++j) {

                    row = (ArrayList) res.get(j);
                    contentRow = new ReportContentString();
                    outputRow = new ArrayList();
                    rowSize = row.size();
                    for (int k = 0; k < rowSize; ++k) {

                        if ( k < colNumber ) {

                            column = row.get(k);

                            if (k == 3) {
                                // calculate total amount
                                try {
                                    totAmount +=
                                        Float.parseFloat(column.toString());
                                }
                                catch (Exception e) {}
                            }
                            else if (k == 4) {
                                // calculate forecast amount
                                try {
                                    forAmount +=
                                        Float.parseFloat(column.toString());
                                }
                                catch (Exception e) {}
                            }

                            if (cols[k].isVisible()) {
                                outputRow.add(outputObject(column, getColumnName(k)));
                            }
                        }
                    }

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // add sum of amounts line
                if (size > 0) {
                    contentRow = new ReportContentString();
                    outputRow = new ArrayList();

                    // two empty columns
                    outputRow.add(outputObject("", getColumnName(1)));

                    outputRow.add(outputObject("", getColumnName(2)));

                    // total amount sum
                    outputRow.add(outputObject(new Float(totAmount),getColumnName(3)));

                    // forecast amount sum
                    outputRow.add(outputObject(new Float(forAmount),getColumnName(4)));

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // add end table line
                result.add(getTableEnd());
        }
    }
}
