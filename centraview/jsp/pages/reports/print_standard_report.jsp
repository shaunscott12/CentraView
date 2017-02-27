<%--
 * $RCSfile: print_standard_report.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
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
<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
	<tr valign="top">
	  <td>
<table border="0" cellspacing="0" cellpadding="0">
  <tr height="10">
    <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
  </tr>
      <tr height="10">
        <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
      </tr>
      <tr height="10"><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="1" /></td></tr>
      <tr>
          <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
          <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
             <tr height="17">
               <td colspan="2" class="sectionHeader"><bean:message key="label.reports.results"/>: <%=report.getName()%></td>
             </tr>
          </table></td>
          <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
      </tr>
      <tr height="10"><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="1" /></td></tr>
      <tr>
        <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
            <td width="100%">
              <jsp:include page="standard_report_results.jsp"/>
            </td>
        <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
      </tr>
  </table>
          </td>
	</tr>
  </table>
<script language="javascript">
window.print();
</script>