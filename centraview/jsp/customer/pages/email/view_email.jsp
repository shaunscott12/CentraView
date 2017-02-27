<%--
 * $RCSfile: view_email.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:49 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 1.0.0 (the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.email.viewemail"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCellBold"><bean:message key="label.customer.pages.email.from"/>:</td>
    <td class="contentCell"><bean:write name="customerEmailForm" property="from" ignore="true" /></td>
  </tr>
  <tr>
    <td class="labelCellBold"><bean:message key="label.customer.pages.email.to"/>:</td>
    <td class="contentCell">
      <c:set var="count" value="0" />
      <c:forEach var="to" items="${customerEmailForm.map.toList}">
        <c:choose>
          <c:when test="${count % 2 != 0}">
            <c:out value="${to}, " /><br/>
          </c:when>
          <c:when test="${count % 2 == 0}">
            <c:out value="${to}, " />
          </c:when>
        </c:choose>
        <c:set var="count" value="${count + 1}" />
      </c:forEach>
    </td>
  </tr>
  <tr>
    <td class="labelCellBold">Cc:</td>
    <td class="contentCell">
      <c:set var="count" value="0" />
      <c:forEach var="cc" items="${customerEmailForm.map.ccList}">
        <c:choose>
          <c:when test="${count % 2 != 0}">
            <c:out value="${cc}, " /><br/>
          </c:when>
          <c:when test="${count % 2 == 0}">
            <c:out value="${cc}, " />
          </c:when>
        </c:choose>
        <c:set var="count" value="${count + 1}" />
      </c:forEach>
    </td>
  </tr>
  <tr>
    <td class="labelCellBold"><bean:message key="label.customer.pages.email.date"/>:</td>
    <td class="contentCell"><bean:write name="customerEmailForm" property="date" ignore="true" /></td>
  </tr>
  <tr>
    <td class="labelCellBold"><bean:message key="label.customer.pages.email.subject"/>:</td>
    <td class="contentCell"><bean:write name="customerEmailForm" property="subject" ignore="true" /></td>
  </tr>
</table>
<table width="100%" border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td>
      <c:out value="${customerEmailForm.map.body}" escapeXml="false" default="uh-oh" />
    </td>
  </tr>
</table>