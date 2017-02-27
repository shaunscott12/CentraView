<%--
 * $RCSfile: email_portlet.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/13 22:05:03 $ - $Author: mcallist $
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

<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader"><bean:message key="label.home.email"/></td>
    <td align="right" class="sectionHeader">
      <input class="normalButton" type="button" value="<bean:message key='label.home.compose'/>" id="button7" name="button7" onclick="c_openWindow('/email/compose.do', '', 750, 450, '');" <c:out value="${requestScope.emailButtonModifier}"/> />
    </td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="tableRowOdd">
      <c:choose>
        <c:when test="${not empty requestScope.emailFolders}">
          <c:forEach var="folder" items="${requestScope.emailFolders}">
            <html:img page="/images/icon_folder.gif" align="absmiddle" width="12" height="10" alt="" />
            <a href="<html:rewrite page="/email/email_list.do"/>?folderID=<c:out value="${folder.folderID}"/>" class="plainLink"><c:out value="${folder.folderName}"/></a>
            <c:out value="${folder.countReadMessage}"/> <bean:message key="label.home.unread"/>, <c:out value="${folder.totalMessage}"/> <bean:message key="label.home.total"/><br>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <bean:message key="label.home.emailprofile1"/><html:link action="/preference/mail/new_account.do" styleClass="plainLink">&nbsp;<bean:message key="label.home.emailprofile2"/>&nbsp;</html:link><bean:message key="label.home.emailprofile3"/>.
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>