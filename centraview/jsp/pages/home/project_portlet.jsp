<%--
 * $RCSfile: project_portlet.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/13 22:05:03 $ - $Author: mcallist $
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
<!-- Project Tasks -->
<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader"><bean:message key="label.home.projecttasks"/></td>
    <td align="right" class="sectionHeader">
      <input class="normalButton" type="button" value="<bean:message key='label.home.newtask'/>" id="button7" name="button7" onclick="c_openWindow('/projects/new_task.do', 'nt', 750, 350, '');"/>
      <input name="button7" type="button" class="normalButton" id="button7" onclick="c_openWindow('/projects/new_project.do', 'np', 730, 307, '');" value="<bean:message key="label.home.newproject"/>" />
    </td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
  <c:choose>
    <c:when test="${not empty requestScope.taskDisplayList}">
      <c:set var="counter" value="${0}" />
      <c:forEach var="activity" items="${requestScope.taskDisplayList}">
        <c:choose>
          <c:when test="${(counter % 2) == 0}">
            <c:set var="rowClass" value="tableRowOdd"/>
          </c:when>
          <c:otherwise>
            <c:set var="rowClass" value="tableRowEven"/>
          </c:otherwise>
        </c:choose>
        <tr>
          <td class="<c:out value="${rowClass}"/>">
            <a href="javascript:c_openWindow('<c:out value="${activity.url}"/>','',780,580,'scrollbars=yes');" class="plainLink">
              <c:out value="${activity.title}"/>
            </a>
          </td>
        </tr>
        <c:set var="counter" value="${counter + 1}" />
      </c:forEach>
    </c:when>
    <c:otherwise>
      <tr>
        <td class="tableRowOdd">
          <bean:message key="label.home.noprojecttask" />.
        </td>
      </tr>
    </c:otherwise>
  </c:choose>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>
<!-- end project task portlet -->