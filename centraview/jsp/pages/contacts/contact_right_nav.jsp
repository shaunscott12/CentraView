<%--
 * $RCSfile: contact_right_nav.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
  <title><bean:message key="label.contacts.titlecontacts"/></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
</head>
<body style="background-color: #cccccc;">
<table border="0" cellspacing="0" cellpadding="0" width="100%" height="376">
  <tr>
    <td class="contactNavHeader" height="5"><bean:message key="label.contacts.selectrecord"/>.</td>
  </tr>
  <tr>
    <td class="pagingBarContainer" height="27">
      <input type="button" name="firstlist" class="normalButton" value="|&laquo;" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=1;'">
      <input type="button" name="previous" class="normalButton" value="&laquo;" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=<c:out value="${param.previous}"/>;'">
      <span class="plainText"><bean:message key="label.contacts.page"/>: <c:out value="${param.current}"/>/<c:out value="${param.total}"/></span>
      <input type="button" name="next" class="normalButton" value="&raquo;" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=<c:out value="${param.next}"/>';">
      <input type="button" name="last" class="normalButton" value="&raquo;|" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=<c:out value="${param.last}"/>;'">
    </td>
  </tr>
  <tr>
    <%-- The following Exercise in JSTL complexity is brought to you by your friends at CentraView --%>
    <c:set var="URL" value="/contacts/view_entity.do"/>
    <c:if test="${param.recordType == 'Individual'}">
      <c:set var="URL" value="/contacts/view_individual.do"/>
    </c:if>
    <td valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <c:choose>
          <c:when test="${empty sessionScope.selectedContacts.list}">
            <span class="plainText"><bean:message key="label.contacts.norecords"/>.</span>
          </c:when>
          <c:otherwise>
            <c:set var="columnIndex" value="${sessionScope.selectedContacts.parameters.columns[0].queryIndex}" />
            <c:set var="counter" value="${0}" />
            <c:forEach var="valueListRow" items="${sessionScope.selectedContacts.list}">
              <tr>
              <c:choose>
                <c:when test="${(counter % 2) == 0}">
                  <td class="tableRowOdd">
                </c:when>
                <c:otherwise>
                  <td class="tableRowEven">
                </c:otherwise>
              </c:choose>
                  <a href="javascript:window.parent.window.location = '<html:rewrite page=""/><c:out value="${URL}"/>?rowId=<c:out value="${valueListRow.rowId}" />,<c:out value="${sessionScope.selectedIds}" />&current=<c:out value="${param.current}"/>';" class="plainLink">
                    <c:out value="${valueListRow.rowData[columnIndex-1]}"/> <%-- subtract 1 because columnIndex is 1 based --%>
                  </a>
                </td>
              </tr>
              <c:set var="counter" value="${counter + 1}" />
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </table>
    </td>
  </tr>
  <tr>
    <td class="pagingBarContainer" height="27">
      <input type="button" name="firstlist" class="normalButton" value="|&laquo;" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=1';">
      <input type="button" name="previous" class="normalButton" value="&laquo;" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=<c:out value="${param.previous}"/>';">
      <span class="plainText"><bean:message key="label.contacts.page"/>:<c:out value="${param.current}"/>/<c:out value="${param.total}"/></span>
      <input type="button" name="next" class="normalButton" value="&raquo;" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=<c:out value="${param.next}"/>';">
      <input type="button" name="last" class="normalButton" value="&raquo;|" onclick="javascript:window.parent.window.location = window.parent.window.location + '&current=<c:out value="${param.last}"/>';">
    </td>
  </tr>
</table>
</form>
</body>
</html>