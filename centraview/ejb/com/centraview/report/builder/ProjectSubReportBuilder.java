/*
 * $RCSfile: ProjectSubReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:57 $ - $Author: mking_cv $
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
 * <p>Title: ProjectSubReportBuilder class </p>
 * <p>Description: Class for Project standard child report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class ProjectSubReportBuilder extends ReportBuilder{

  private int reportId;
  private ArrayList bindArray; // of BindObjects

  /**
   * constructor
   *
   * @param reportId int
   * @param ds String
   * @param con CVDal
   * @param bindArray ArrayList
   * @exception   Exception
   *
   */
  public ProjectSubReportBuilder(int reportId,
                                 String ds,
                                 CVDal con,
                                 ArrayList bindArray
                                 ) throws Exception {
    super(ds,con);

    if ( reportId != PROJECTS1_REPORT_ID
//         && reportId != PROJECTS2_REPORT_ID
         ) {
       throw new Exception("ProjectSubReportBuilder unknown reportId: "+reportId);
    }

    this.reportId = reportId;
    this.bindArray = bindArray;

    ReportColumn[] colName = new ReportColumn[3];
    colName[0]  = new ReportColumn("Activity Id",false);
    colName[1]  = new ReportColumn("Activity Types",true);
    colName[2]  = new ReportColumn("Activity Count",true);
    setColumns(colName);

    addPriorSQL("DROP TABLE IF EXISTS repSubPrj1");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // create temp table for report content
    addPriorSQL("CREATE TEMPORARY TABLE repSubPrj1 SELECT act.typeId TypeId, "+
                "act.name AS Name,0 AS Count FROM activitytype act "+
                // except Forecast Sale and Literature Request activities
                "WHERE act.typeId<>3 AND act.typeId<>4");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    addPriorSQL("DROP TABLE IF EXISTS repSubPrj2");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(null);

    // create temp table for report content
    String  where = "";
    // in case of total count
    if (this.bindArray != null) {
      where ="WHERE act."+((reportId == PROJECTS1_REPORT_ID)?"ProjectId":"OwnerId")+"=? ";
    }
    addPriorSQL("CREATE TEMPORARY TABLE repSubPrj2 SELECT act.typeId AS TypeId, "+
                "count(act.typeId) AS Count FROM repProject1 act "+
                where+
                "GROUP BY act.TypeId");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(this.bindArray);

    addPriorSQL("UPDATE repSubPrj1,repSubPrj2 SET repSubPrj1.Count="+
                "repSubPrj2.Count WHERE repSubPrj1.typeId=repSubPrj2.typeId");
    // bind ArrayList is empty for this sql
    addPriorSQLBind(new ArrayList());

    // select report rows
    addReportSQL("SELECT TypeId,Name,Count FROM repSubPrj1");
    // bind ArrayList is empty for this sql
    addReportSQLBind(new ArrayList());

    addPostSQL("DROP TABLE IF EXISTS repSubPrj1");
    // bind ArrayList is empty for this sql
    addPostSQLBind(new ArrayList());

    addPostSQL("DROP TABLE IF EXISTS repSubPrj2");
    // bind ArrayList is empty for this sql
    addPostSQLBind(new ArrayList());

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
               for (int k=0;k<rowSize;++k) {

                   if (k<colNumber && cols[k].isVisible()) {

                     column = row.get(k);
                     element = outputObject(column, getColumnName(k));
                     if (k==2) {
                        // calculate total activities
                        try {
                          totalCount += Integer.parseInt(element.getDisplayString());
                        }
                        catch (Exception e) {}
                     }
                     outputRow.add(element);
                   }
               }

               contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
               contentRow.setReportRow(outputRow);
               result.add(contentRow);
           }
           // add Total activities line
           if (size>0) {
             contentRow = new ReportContentString();
             outputRow = new ArrayList();
             outputRow.add(outputObject("Total Activities",getColumnName(1)));
             outputRow.add(outputObject(new Integer(totalCount), getColumnName(1)));
             contentRow.setShowType(ReportContentString.SHOW_TYPE_TABLE_ROW);
             contentRow.setReportRow(outputRow);
             result.add(contentRow);
           }
           // add end table line
           result.add(getTableEnd());
    }
  }
}
