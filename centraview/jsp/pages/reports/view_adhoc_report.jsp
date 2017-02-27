<%--
 * $RCSfile: view_adhoc_report.jsp,v $ $Revision: 1.2 $ $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input type="button" name="button1" class="normalButton" value="&laquo; <bean:message key='label.value.edit'/>&nbsp;" onclick="c_goTo('/reports/adhoc_report.do?reportId=<c:out value="${requestScope.reportId}"/>');">&nbsp;
      <input type="button" name="button2" class="normalButton" value="<bean:message key='label.value.export'/>" onclick="c_goTo('/reports/view_adhoc.do?action=export&reportId=<c:out value="${requestScope.reportId}"/>');">&nbsp;
      <input type="button" name="button3" class="normalButton" value="<bean:message key='label.value.print'/>" onclick="javascript:callPrint();">
    </td>
  </tr>
</table>
<p/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.reports.results"/>: <c:out value="${requestScope.reportName}"/></td>
  </tr>
</table>
<p/>
<app:valuelist listObjectName="valueList" />
<script language="javascript">
<!--
function callPrint()
{
  c_openWindow("/reports/view_adhoc.do?action=print&reportId=<c:out value="${requestScope.reportId}"/>", '', 725, 400,'');
}
//-->
</script>