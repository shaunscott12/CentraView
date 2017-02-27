<%--
 * $RCSfile: unscheduled_portlet.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/13 22:05:03 $ - $Author: mcallist $
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
<!--  Unscheduled Activities -->
<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader"><bean:message key="label.home.unscheduledactivities"/></td>
    <td align="right" class="sectionHeader">
      <input class="normalButton" type="button" value="<bean:message key='label.home.addnew'/>" id="button7" name="button7" onclick="c_openWindow('/activities/activity_dispatch.do?TYPEOFACTIVITY=APPOINTMENT&unScheduleActivity=unSchedule','', 780, 522, '');">
    </td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
  <c:choose>
    <c:when test="${not empty requestScope.unscheduledDisplayList}">
      <c:set var="counter" value="${0}" />
      <c:forEach var="activity" items="${requestScope.unscheduledDisplayList}">
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
            <img src="<html:rewrite page="/images/"/><c:out value="${activity.icon}"/>" align="absmiddle">&nbsp;
            <a href="javascript:c_openPopup('<c:out value="${activity.url}"/>');" class="plainLink">
              <c:out value="${activity.title}"/>
            </a>
            <c:if test="${not empty activity.relatedTitle}">
              &nbsp;(<a class="plainLink" onclick="c_openPopup('/contacts/view_entity.do?rowId=<c:out value="${activity.relatedId}"/>');">
                <c:out value="${activity.relatedTitle}"/>
              </a>)
            </c:if>
          </td>
        </tr>
        <c:set var="counter" value="${counter + 1}" />
      </c:forEach>
    </c:when>
    <c:otherwise>
      <tr>
        <td class="tableRowOdd">
          <bean:message key="label.value.nounscheduledactivities" />.
        </td>
      </tr>
    </c:otherwise>
  </c:choose>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>
<!-- end unscheduled activities -->