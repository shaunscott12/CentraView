/*
 * $RCSfile: Sales3SubReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:58 $ - $Author: mking_cv $
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
 * <p>Title: Sales3SubReportBuilder class </p>
 * <p>Description: Class for Sales3 standard report child building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 02/07/04
 */

public class Sales3SubReportBuilder
    extends ReportBuilder {

    private int reportId;

    /**
     * constructor
     *
     * @param reportId int
     * @param ds String
     * @param con CVDal
     * @exception   Exception
     *
     */
    public Sales3SubReportBuilder(int reportId,
                                  String ds,
                                  CVDal con
                                  ) throws Exception {
        super(ds, con);

        if ( reportId != SALES3_REPORT_ID ) {
            throw new Exception("Sales3SubReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;

        ReportColumn[] colName = new ReportColumn[9];
        colName[0] = new ReportColumn("ItemId", false);
        colName[1] = new ReportColumn("Item", true);
        colName[2] = new ReportColumn("Description", true);
        colName[3] = new ReportColumn("Quantity", true);
        colName[4] = new ReportColumn("Cost", true);
        colName[5] = new ReportColumn("Extended Cost", true);
        colName[6] = new ReportColumn("Average Price", true);
        colName[7] = new ReportColumn("Actual Extended Price", true);
        colName[8] = new ReportColumn("Actual Gross Profit", true);
        setColumns(colName);

        // select report rows
        addReportSQL("SELECT ID,Title AS Item, Description, Sum(Quantity) AS Quantity, "+
                     "Cost, ExtCost,Avg(Price) AS AveragePrice,"+
                     "Sum(ExtPrice) AS ExtPrice,"+
                     "Sum(ExtPrice - ExtCost) AS GrossProfit "+
                     "FROM repSales3 GROUP BY ID");
        // bind ArrayList is empty for this sql
        addReportSQLBind(null);

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

        ReportColumn[] cols = getColumns();
        int colNumber = cols.length;

        switch (i) {
            case 0:
                size = res.size();

                // set header line
                result.add(getTableSubHeader());

                for (int j = 0; j < size; ++j) {

                    row = (ArrayList) res.get(j);
                    contentRow = new ReportContentString();
                    outputRow = new ArrayList();
                    rowSize = row.size();
                    for (int k = 0; k < rowSize; ++k) {

                        if ( k < colNumber ) {

                            column = row.get(k);

                            if (k == 3) {
                                // quantiy
                               column = new String(column.toString());
                            }

                            if ( cols[k].isVisible() ) {
                                outputRow.add(outputObject(column, getColumnName(k)));
                            }

                        }
                    }

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
