/*
 * $RCSfile: ReportContentString.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

package com.centraview.report.valueobject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * <p>Title: ReportContentString class </p>
 * <p>Description: Class for standard reports building</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */


public class ReportContentString
    implements Serializable {

    /** Constants
     *
     */

    /** header type constant    */
    public static final int SHOW_TYPE_HEADER = 0;
    /** table header type constant    */
    public static final int SHOW_TYPE_TABLE_HEADER = 1;
    /** table cub header type constant    */
    public static final int SHOW_TYPE_TABLE_SUBHEADER = 2;
    /** table row type constant    */
    public static final int SHOW_TYPE_TABLE_ROW = 3;
    /** simple line  type constant    */
    public static final int SHOW_TYPE_LINE = 4;
    /** empty line(spacer) type constant    */
    public static final int SHOW_TYPE_SPACER = 5;
    /** table end type constant    */
    public static final int SHOW_TYPE_TABLE_END = 6;

    private int showType; // one of SHOW_TYPE constant
    ArrayList reportRow; // of ListElementMembers

    /**
     * constructor
     */
    public ReportContentString() {
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getShowType() {
        return showType;
    }

    public ArrayList getReportRow() {
        return reportRow;
    }

    public void setReportRow(ArrayList reportRow) {
        this.reportRow = reportRow;
    }

    public void add(Object obj) {
        if (reportRow == null) {
            reportRow = new ArrayList();
        }
        reportRow.add(obj);
    }
}
