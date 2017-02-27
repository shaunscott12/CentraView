<%--
 * $RCSfile: new_custom_view.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.step1selectmodule"/></td>
  </tr>
</table>
<table border="0" cellpadding="5" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.clicklinktocreatecustomview"/>.</td>
  </tr>
  <tr>
    <td class="contentCell">
      <ul>
        <c:forEach var="link" items="${customViewSelectForm.map.linkList}">
        <li><a href="<%=request.getContextPath()%>/common/new_view.do?listType=<c:out value="${link.listType}" />&moduleId=<c:out value="${link.moduleId}" />" class="plainLink"><c:out value="${link.name}" /></a></li>
        </c:forEach>
      </ul>
    </td>
  </tr>
</table>