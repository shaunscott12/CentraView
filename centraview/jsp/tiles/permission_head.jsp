<%--
 * $RCSfile: permission_head.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><c:out value="${param.modulename}"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="labelCell"><bean:message key="label.tiles.name"/>: </td>
    <td class="contentCell"><c:out value="${param.recordName}"/></td>
  </tr>
  <tr>
    <td class="labelCell">ID: </td>
    <td class="contentCell"><c:out value="${param.rowID}"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.tiles.recordowner"/>: </td>
    <td class="contentCell">
      <input name="ownerField" type="text" span class="inputBox" size="25" id="employeeName" readonly="readonly" value="<c:out value="${requestScope.ownerName}"/>">
      <input type="button" name="lookup" value="<bean:message key='label.value.change'/>" class="normalButton" onclick="c_lookup('Employee')">
    </td>
  </tr>
</table>