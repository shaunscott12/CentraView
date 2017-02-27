<%--
 * $RCSfile: standard_report_results.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:00 $ - $Author: mking_cv $
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

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "com.centraview.report.valueobject.*,
                   com.centraview.common.* ,
                   java.util.ArrayList "%>

<%
    ReportResultVO report = (ReportResultVO)request.getAttribute("pagedata");
    ArrayList content = report.getResults();
    int resSize = content.size();
    int i = 0;
%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
      <%
          int rowType = 0;
          int size = 0;
          boolean isTable = false;
          String oddOrEven = "Odd";     // CSS class should be "odd"
          ArrayList rowElements = null;
          for ( i = 0; i < resSize; i++)
          {
              ReportContentString row = (ReportContentString)content.get(i);
              rowType = row.getShowType();
              rowElements = row.getReportRow();
              if (null != rowElements)
                   size = rowElements.size();

              if ( rowType == ReportContentString.SHOW_TYPE_TABLE_HEADER ||
                   rowType == ReportContentString.SHOW_TYPE_TABLE_SUBHEADER) {
       %>
      <tr height="1">
      <td width="100%">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <!-- Logic Start -->
        <!-- Col name -->
        <tr>
          <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
        <%
                 for  (int j = 0; j < size; ++j ) {
                    ListElementMember column = (ListElementMember)rowElements.get(j);
         %>
          <td class="listHeader" nowrap>
            <%=column.getDisplayString() %>
          </td>
        <%
                 }
        %>
          <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
        </tr>
        <%
                 oddOrEven = "Odd";
              }
              if ( rowType == ReportContentString.SHOW_TYPE_TABLE_ROW ) {
                 if (oddOrEven.equals("Odd"))
                        oddOrEven = "Even";
                 else
                        oddOrEven = "Odd";
                 isTable = true;
        %>
                <tr>
                  <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
        <%
                  int columnCounter = 0;    // current counter of the column list/no recor
                  String align = "right";
                  for ( int j = 0; j < size; j++) {
                     ListElementMember column = (ListElementMember)rowElements.get(j);
                     columnCounter++;
                     Object memberObj = column.getMemberValue();
                     if (memberObj instanceof Number) {
                         align = "right";
                     } else  {
                         align = "left";
                     }
                     String display = column.getDisplayString();
                     if (display == null || display.equals("")) {
                       display = "&nbsp";
                     }
        %>
                  <td class="tableRow<%=oddOrEven%>" align=<%=align%>><%=display%></td>
                  <%
                  }
                  %>
                 <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
              </tr>
        <%
              }

              if (rowType == ReportContentString.SHOW_TYPE_TABLE_END) {
                  isTable = false;
        %>
    </table>
    </td>
    </tr>
    <tr><td height="10"><html:img page="/images/spacer.gif" height="1" width="11" /></td></tr>
        <%
             }
             if (rowType == ReportContentString.SHOW_TYPE_LINE) {
        %>
        <tr>
        <td width="100%"><b><span class="labelCell">
        <%
                 for (int j = 0; j < size; ++j ) {
                   ListElementMember column = (ListElementMember)rowElements.get(j);
        %>                    
          &nbsp;<%=column.getDisplayString() %>&nbsp;
        <%
                 }
        %>
        </span></b></td>
        </tr>
        <tr><td height="3"><html:img page="/images/spacer.gif" height="1" width="11" /></td></tr>
        <%

             }
        %>              
        <%
        } // end of main loop by i
        %>
       </tr>
   </table>