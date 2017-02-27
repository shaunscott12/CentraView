/*
 * $RCSfile: Hr1ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:57 $ - $Author: mking_cv $
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
 * <p>Title: Hr1ReportBuilder class </p>
 * <p>Description: Class for Hr1 standard report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Hr1ReportBuilder
    extends ReportBuilder {

    /**
     * constructor
     *
     * @param ds String datasource
     * @exception   Exception
     *
     */

    public Hr1ReportBuilder(String ds) throws Exception {super(ds);}

    private String dateClause = "";
    ArrayList dateBind = null;


    /**
     * constructor
     *
     * @param dateFrom java.sql.Date
     * @param dateTo java.sql.Date
     * @param clause ArrayList
     * @param ds String
     * @exception   Exception
     *
     */

    public Hr1ReportBuilder(java.sql.Date dateFrom,
                            java.sql.Date dateTo,
                            String ds,
                            String whereClause) throws Exception {

        super(dateFrom, dateTo, ds);

        ReportColumn[] colName = new ReportColumn[10];
        colName[0] = new ReportColumn("ID", true);
        colName[1] = new ReportColumn("StartDate", true);
        colName[2] = new ReportColumn("EndDate", true);
        colName[3] = new ReportColumn("Expense Type", true); //Status
        colName[4] = new ReportColumn("Amount", true);
        colName[5] = new ReportColumn("Entity", true);
        colName[6] = new ReportColumn("IndividualId", false);
        colName[7] = new ReportColumn("User", false);
        colName[8] = new ReportColumn("RelatedTo", true); // externalid
        colName[9] = new ReportColumn("Reference", true); // Reference
        setColumns(colName);

        setLeader(7);

        addPriorSQL("DROP TABLE IF EXISTS repHr1");
        // bind ArrayList is empty for this sql
        addPriorSQLBind(null);

        java.sql.Date from = (getDateFrom() == null)? null :
                             new java.sql.Date(getDateFrom().getTime());
        java.sql.Date to   = (getDateTo() == null)? null :
                             new java.sql.Date(getDateTo().getTime()+24*60*60*1000);

        dateClause = ((from != null) ? " AND (expf.FromDate >= ? OR expf.FromDate IS NULL) "+
                                       "AND (expf.ToDate >= ? OR expf.ToDate IS NULL)" : "") +
                      ((to   != null) ? " AND (expf.FromDate < ? OR expf.FromDate IS NULL) "+
                                        "AND (expf.ToDate < ? OR expf.ToDate IS NULL)" : "");

        // dateBind
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
        addPriorSQL("CREATE  TEMPORARY TABLE repHr1 "+
                    "SELECT expense.expenseid,expense.Amount,expense.Status,"+
                    "entity.EntityID,entity.name AS EntityName, indv.IndividualID,"+
                    "concat(indv.firstname ,' ',indv.lastname) AS EmployeeName,"+
                    "prj.ProjectID,opp.OpportunityID,tick.TicketID,"+
                    "CASE  "+
                    "WHEN expense.Project IS NOT NULL AND expense.Project>0  "+
                    "THEN 'Project' "+
                    "WHEN expense.Ticket IS NOT NULL AND expense.Ticket>0 "+
                    "THEN 'Ticket' "+
                    "WHEN expense.Opportunity IS NOT NULL AND expense.Opportunity>0 "+
                    "THEN 'Opportunity' "+
                    "END AS RelatedTo, "+
                    "CASE  "+
                    "WHEN expense.Ticket IS NOT NULL AND expense.Ticket>0  "+
                    "THEN tick.subject  "+
                    "WHEN expense.Project IS NOT NULL AND expense.Project>0 "+
                    "THEN prj.ProjectTitle  "+
                    "WHEN expense.Opportunity IS NOT NULL AND expense.Opportunity>0 "+
                    "THEN opp.Title  "+
                    "END AS Reference,  "+
                    "expf.FromDate AS StartDate, expf.ToDate AS EndDate "+
                    "FROM expense "+
                    "LEFT JOIN entity ON expense.entityid = entity.entityid "+
                    "LEFT JOIN individual indv ON expense.owner = indv.individualid "+
                    "LEFT JOIN project prj ON expense.project = prj.projectid "+
                    "LEFT JOIN opportunity opp ON expense.opportunity = opp.opportunityid "+
                    "LEFT JOIN ticket tick ON expense.ticket = tick.ticketid "+
                    "LEFT JOIN expenseform expf ON expf.expenseformId = expense.expenseformid"+
                    " WHERE 1=1 "+ whereClause+dateClause);

        // bind ArrayList is empty for this sql
        addPriorSQLBind(dateBind);

        // report SQL
        addReportSQL("SELECT ExpenseId,StartDate,EndDate,Status,Amount,EntityName, " +
                     "IndividualID, EmployeeName,RelatedTo,Reference FROM repHr1 "+
                     "ORDER BY IndividualID, Status");

        // bind ArrayList is empty for this sql
        addReportSQLBind(null);

        // post SQL
        addPostSQL("DROP TABLE IF EXISTS repHr1");

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
        ListElementMember element = null;
        int leaderCount = 0;
        String previousLeaderValue = null;
        String currentLeaderValue = "";
        int leaderHeaderIndex = 0;

        // current row Amount
        float currAmount = 0;

        // current leader total Amount
        float totalAmount = 0;

        // report total Amount
        float repAmount = 0;

        ReportColumn[] cols = getColumns();
        int colNumber = cols.length;

        switch (i) {
            case 0:
                size = res.size();

                // set leader line index
                leaderHeaderIndex = result.size();

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
                            element = outputObject(column, getColumnName(k));

                            if ( cols[k].isVisible() ) {
                                outputRow.add(element);
                            }

                            if ( k == getLeader() ) {
                                // get leader column value
                                currentLeaderValue = element.getDisplayString();
                            }
                            if ( k == 4 ) {
                                // total amount
                                if (column instanceof Float) {
                                    currAmount = ( (Float) column).floatValue();
                                }
                                else if (column  instanceof Double) {
                                    currAmount = ( (Double) column).floatValue();
                                }
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
                            result.add(getTotals(totalAmount,false));

                            // add end table line
                            result.add(getTableEnd());
                            // add table header line for new leader
                            result.add(getTableHeader());

                        }
                        previousLeaderValue = currentLeaderValue; // set new leader

                        leaderCount = 0; // reset leaderCount
                        leaderHeaderIndex = result.size() - 1; // set new header index

                        totalAmount = 0;
                    }
                    ++leaderCount;

                    totalAmount += currAmount;
                    repAmount += currAmount;

                    contentRow.setShowType(ReportContentString.
                                           SHOW_TYPE_TABLE_ROW);
                    contentRow.setReportRow(outputRow);
                    result.add(contentRow);
                }

                // add header line for last leader
                if (size > 0) {
                    result.add(leaderHeaderIndex,
                               getLeaderHeader(previousLeaderValue));

                    // add total amount line
                    result.add(getTotals(totalAmount,false));

                }

                // add empty row
                result.add(getEmptyTableRow());

                // add report total amount line
                result.add(getTotals(repAmount,true));

                // add table end
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
     * Gets output line for total amount
     *
     * @param totalAmount float
     * @param report boolean
     * @return ReportContentString
     *
     */
    private ReportContentString getTotals(float totalAmount,
                                          boolean report) {
        ReportContentString headerRow = new ReportContentString();
        ArrayList header = new ArrayList();

        headerRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);

        ReportColumn[] cols = getColumns();
        int colNumber = (cols == null ) ? 0 : cols.length;
        int i = 0;

        if ( report ) {
            header.add(new StringMember("", "Report Total", 'r', "", 'T', false));
            ++i;
        }

        for ( ; i < 4 && i < colNumber; ++i) {
            if (cols[i].isVisible()) {
                header.add(new StringMember("", "", 'r', "", 'T', false));
            }
        }

        header.add(new MoneyMember("Total Amount",
                                   new Float(totalAmount), 'r', "",
                                   'T', false, 10));

        for ( ++i; i < colNumber; ++i) {
            if (cols[i].isVisible()) {
                header.add(new StringMember("", "", 'r', "", 'T', false));
            }
        }

        headerRow.setReportRow(header);

        return headerRow;
    }

}
