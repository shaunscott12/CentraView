<%--
 * $RCSfile: calendar_portlet.jsp,v $    $Revision: 1.4 $  $Date: 2005/10/24 16:41:16 $ - $Author: mcallist $
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
<!-- Start calendar portlet -->
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<%-- This overlib stuff should probably be in the top JSP --%>
<script language="JavaScript" src="<html:rewrite page="/stylesheet/overlib.js"/>"><!-- overLIB (c) Erik Bosrup --></script>
<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader">
      <bean:message key="label.home.todayscalender"/></td>
    <td align="right" class="sectionHeader">
      <input class="normalButton" type="button" value="<bean:message key='label.home.schedule'/>" id="button2" name="button2" onClick="c_openWindow('/activities/activity_dispatch.do?TYPEOFACTIVITY=APPOINTMENT', 'schedule_window', 780, 522, '');">
    </td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
  <c:choose>
    <c:when test="${not empty requestScope.calendarDisplayList}">
      <c:forEach var="activity" items="${requestScope.calendarDisplayList}">
        <tr>
          <td class="tableRowOdd" width="20%"><c:out value="${activity.subTitle}"/></td>
          <td class="tableRowOdd" width="80%">
            <img src="<html:rewrite page="/images/"/><c:out value="${activity.icon}"/>" align="absmiddle">
            <a href="javascript:<c:out value="${activity.url}"/>" class="plainLink" onmouseover="return overlib('<b><c:out value="${activity.olTitle}"/></b><br/><c:out value="${activity.olDescription}"/>', CAPTION, '<c:out value="${activity.olCaption}"/>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
              <c:out value="${activity.title}"/>
            </a>
            <c:if test="${not empty activity.relatedTitle}">
              (<a class="plainLink" onclick="c_openPopup('/contacts/view_entity.do?rowId=<c:out value="${activity.relatedId}"/>');">
                <c:out value="${activity.relatedTitle}"/>
              </a>)
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <tr>
        <td class="tableRowOdd">
          <bean:message key="label.home.noscheduledactivities"/>.
        </td>
      </tr>
    </c:otherwise>
  </c:choose>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>
<!-- end calendar portlet -->