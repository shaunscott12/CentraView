/*
 * $RCSfile: Project2SubReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:57 $ - $Author: mking_cv $
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
import com.centraview.common.ListElementMember;
import com.centraview.report.valueobject.ReportContentString;

/**
 *
 * <p>Title: Project2SubReportBuilder class </p>
 * <p>Description: Class for Project standard child report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Project2SubReportBuilder
    extends ReportBuilder {

    private int reportId;
    private ArrayList bindArray; // of BindObjects

    /**
     * constructor
     *
     * @param reportId int
     * @param ds STring
     * @param con CVDal
     * @param bindArray ArrayList
     * @exception   Exception
     *
     */
    public Project2SubReportBuilder(int reportId,
                                    String ds,
                                    CVDal con,
                                    ArrayList bindArray
                                    ) throws Exception {
        super(ds, con);

        if ( reportId != PROJECTS1_REPORT_ID
//             && reportId != PROJECTS2_REPORT_ID
             ) {
            throw new Exception("Project2SubReportBuilder unknown reportId: " +
                                reportId);
        }

        this.reportId = reportId;
        this.bindArray = bindArray;

        ReportColumn[] colName = new ReportColumn[4];
        colName[0] = new ReportColumn("OwnerId", false);
        colName[1] = new ReportColumn("Resource", true);
        colName[2] = new ReportColumn("Time Committed", true);
        colName[3] = new ReportColumn("Time Completed", true);
        setColumns(colName);

        // select report rows
        addReportSQL(
            "SELECT OwnerId, OwnerName,CAST(Sum(TimeBudgeted) AS UNSIGNED),"+
            "CAST(Sum(TimeUsed)AS UNSIGNED) " +
            "FROM repProject1 GROUP BY OwnerId");
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
        ListElementMember element = null;
        int totalCommitted = 0;
        int totalCompleted = 0;
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

                        if (k < colNumber) {

                            column = row.get(k);
                            element = outputObject(column, getColumnName(k));
                            if (k == 2) {
                                // calculate total time committed
                                try {
                                    totalCommitted +=
                                        Integer.parseInt(element.getDisplayString());
                                }
                                catch (Exception e) {}
                            }
                            else if (k == 3) {
                                // calculate total time completed
                                try {
                                    totalCompleted +=
                                        Integer.parseInt(element.getDisplayString());
                                }
                                catch (Exception e) {}
                            }
                            if (cols[k].isVisible()) {
                                outputRow.add(element);
                            }
                        }
                    }

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // add Total times line
                if (size > 0) {
                    contentRow = new ReportContentString();
                    outputRow = new ArrayList();
                    outputRow.add(outputObject("Total", getColumnName(1)));
                    outputRow.add(outputObject(new Integer(totalCommitted),
                                               getColumnName(2)));
                    outputRow.add(outputObject(new Integer(totalCompleted),
                                               getColumnName(3)));
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
