<%--
 * $RCSfile: csv_standard_report.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:58 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
--%>
<%@ page import = "com.centraview.report.valueobject.*, com.centraview.common.* , java.util.ArrayList,
                   com.centraview.report.web.ReportAction"%><%
    response.setContentType("application/download");
    response.setHeader("Content-disposition","inline; filename=StandardReport.csv");
    ReportResultVO report = (ReportResultVO)request.getAttribute("pagedata");
    ArrayList content = report.getResults();
    int resSize = content.size();
    int i = 0;
    int rowType = 0;
    int size = 0;
    boolean isTable = false;
	String str = "";
    ArrayList rowElements = null;
    for ( i = 0; i < resSize; i++)
    {
        ReportContentString row = (ReportContentString)content.get(i);
        rowType = row.getShowType();
        rowElements = row.getReportRow();
        if ( null != rowElements)
             size = rowElements.size();
        if ( rowType == ReportContentString.SHOW_TYPE_TABLE_HEADER ||
             rowType == ReportContentString.SHOW_TYPE_TABLE_SUBHEADER) {
			 str = "";
            for  (int j = 0; j < size; ++j ) { 

                String tmp = ReportAction.doCSVCorrect(((ListElementMember)rowElements.get(j)).getDisplayString());
                str += tmp + ((j < size - 1)? "," : "");
                tmp = null;
        }
%><%=str%>
        <%}
        if ( rowType == ReportContentString.SHOW_TYPE_TABLE_ROW ) {
            isTable = true;
			str = "";
            for ( int j = 0; j < size; j++) { 

                String tmp = ReportAction.doCSVCorrect(((ListElementMember)rowElements.get(j)).getDisplayString());
                str += tmp + ((j < size - 1)? "," : "");
                tmp = null;
         }
        %><%=str%>
<%
        }
        if ( rowType == ReportContentString.SHOW_TYPE_TABLE_END) {
            isTable = false;
			str = "";
        }
        if ( rowType == ReportContentString.SHOW_TYPE_LINE) {
			str = "";
            for  (int j = 0; j < size; ++j ) { 

                String tmp = ReportAction.doCSVCorrect(((ListElementMember)rowElements.get(j)).getDisplayString());
                str += tmp + ((j < size - 1)? "," : "");
                tmp = null;
         }
           %><%=str%>
<%}
}%>