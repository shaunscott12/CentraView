/*
 * $RCSfile: Hr2ReportBuilder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:57 $ - $Author: mking_cv $
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


/**
 *
 * <p>Title: Hr2ReportBuilder class </p>
 * <p>Description: Class for Hr2 standard report building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class Hr2ReportBuilder
    extends Hr1ReportBuilder {

    /**
     * constructor
     *
     * @param ds String datasource
     * @exception   Exception
     *
     */

    public Hr2ReportBuilder(String ds) throws Exception {
      super(ds);
    }

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

    public Hr2ReportBuilder(java.sql.Date dateFrom,
                            java.sql.Date dateTo,
                            String ds,
                            String whereClause) throws Exception {
        super(dateFrom, dateTo, ds, whereClause);

        setColumnVisible(5, false);
        setColumnVisible(7, true);
        setLeader(5);

        // report SQL
        clearReportSQL();
        // report SQL
        addReportSQL("SELECT ExpenseId,StartDate,EndDate,Status,Amount,EntityName, " +
                     "IndividualID, EmployeeName,RelatedTo,Reference FROM repHr1 "+
                     "ORDER BY EntityId,IndividualID, Status");

    }
}
