<%--
 * $RCSfile: view_knowledgebase.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/21 17:42:05 $ - $Author: mcallist $
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
 * The developer of the Original Code is CentraView.  Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:form action="/support/knowledgebase_view.do">
<table border="0" cellpadding="3" cellspacing="0" class="formTable" width="100%">
  <tr>
    <td width="10%" class="labelCellBold"><c:out value="${kbform.title}"/></td>
    <c:if test="${showEditRecordButton}">
      <td align="left" class="pagingButtonTD"><input type="button" name="edit" value="<bean:message key='label.value.editrecord'/>" onclick="c_openWindow('/support/knowledgebase_view.do?typeofoperation=editkb&rowId=<c:out value="${kbform.rowId}"/>', 'editkb', 600, 360, '');" class="normalButton"></td>
    </c:if>
  </tr>
  <tr>
    <td colspan="2" class="contentCell"><c:out value="${kbform.detail}" escapeXml="false"/></td>
  </tr>
</table>
<input type="hidden" name="rowId" value="<c:out value="${kbform.rowId}"/>"/>
</html:form>