<%--
 * $RCSfile: csv_adhoc_report.jsp,v $ $Revision: 1.1.1.1 $ $Date: 2005/04/28 20:25:58 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
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
 * The developer of the Original Code is CentraView. Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved. The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>
<%@ page import = "com.centraview.report.valueobject.*,java.util.ArrayList,com.centraview.report.web.ReportAction "%><%
    response.setContentType("application/download");
    response.setHeader("Content-disposition","inline; filename=AdHocReport.csv");
    ReportResultVO report = (ReportResultVO)request.getAttribute("reportResults");
    ArrayList titles = report.getTitles();
    ArrayList results = report.getResults();
    int size = titles.size();
    int resSize = results.size(); int i = 0;
	String str = "";
    for ( i = 0; i < size; i++) {
      String tmp = ReportAction.doCSVCorrect(titles.get(i) + "");
      str += tmp + ((i < size-1)?",":"");}%><%=str%>
<%
		for ( i = 0; i < resSize; i++) {
        ArrayList line = (ArrayList)results.get(i);
		size = line.size();
        for ( int j = 0; j < size; j++) {

         String tmp = ReportAction.doCSVCorrect(line.get(j) + "");
      %><%=tmp%><%if (j < size-1){%>,<%}
           tmp = null;
        }%>
<%}%>