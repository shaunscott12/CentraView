/*
 * $RCSfile: SupportSubReport2Builder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:59 $ - $Author: mking_cv $
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
 * <p>Title: SupportSubReportBuilder class </p>
 * <p>Description: Class for Activities standard child report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 02/08/04
 */

public class SupportSubReport2Builder extends ReportBuilder{

  private int reportId;
  private ArrayList bindArray; // of BindObjects


  /**
   * constructor
   *
   * @param reportId int
   * @param ds String
   * @param con CVDal
   * @exception   Exception
   *
   */

  public SupportSubReport2Builder(int reportId,
                                  String ds,
                                  CVDal con
                                  ) throws Exception {
    super(ds,con);

    if ( reportId != SUPPORT1_REPORT_ID
         && reportId != SUPPORT2_REPORT_ID
         && reportId != SUPPORT3_REPORT_ID ) {
       throw new Exception("SupportSubReport2Builder unknown reportId: "+reportId);
    }

    this.reportId = reportId;

    ReportColumn[] colName = new ReportColumn[3];
    colName[0]  = new ReportColumn("TicketId",false);
    colName[1]  = new ReportColumn("Ticket",true);
    colName[2]  = new ReportColumn("Ticket Count",true);
    setColumns(colName);

    addPriorSQL("DROP TABLE IF EXISTS repSupport3");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // ticket count by user
    addPriorSQL("CREATE TEMPORARY TABLE repSupport3 "+
                "SELECT reps1.TicketId, reps1.Title, "+
                "count(tickl.TicketId) AS Count "+
                "FROM ticketlink tickl, repSupport1 reps1 "+
                "WHERE tickl.ticketid=reps1.ticketId "+
                "GROUP BY tickl.TicketId");
    addPriorSQLBind(null);

    // report content (ticket count by user)
    addReportSQL("SELECT TicketId,Title,Count FROM repSupport3");
    addReportSQLBind(null);

    addPostSQL("DROP TABLE IF EXISTS repSupport3");
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

  public void processReport(int i,Vector res,ArrayList result){
    int size = 0;
    int rowSize = 0;
    ReportContentString contentRow = null;
    ArrayList row = null;
    ArrayList outputRow = null;
    Object column = null;
    ListElementMember element = null;
    int totalCount = 0;

    ReportColumn[] cols = getColumns();
    int colNumber = cols.length;

    switch (i) {
      case 0:
           size = res.size();

           // set header line
           result.add(getTableHeader());

           for (int j = 0; j<size; ++j) {

               row = (ArrayList)res.get(j);
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

                     if ( k == 2 ) {
                        // calculate total activities
                        try {
                          totalCount += Integer.parseInt(element.getDisplayString());
                        }
                        catch (Exception e) {}
                     }
                   }
               }

               contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
               contentRow.setReportRow(outputRow);
               result.add(contentRow);
           }

           contentRow = new ReportContentString();
           outputRow = new ArrayList();
           outputRow.add(outputObject("Total Tickets",getColumnName(1)));
           outputRow.add(outputObject(new Integer(totalCount), getColumnName(1)));
           contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
           contentRow.setReportRow(outputRow);
           result.add(contentRow);

           // add end table line
           result.add(getTableEnd());
    }
  }
}
