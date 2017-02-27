<%--
 * $RCSfile: view_event.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html:form action="/customer/event_register">
<html:hidden name="customerEventForm" property="eventID" />
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td valign="top" width="50%" style="border-right: #cccccc 1px solid;">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.events.eventdetails"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.name"/> :</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="eventName" ignore="true"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.description"/> :</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="description" ignore="true"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.start"/> :</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="startDate" format="MMMM dd, yyyy hh:mm a" ignore="true" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.end"/> :</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="endDate" format="MMMM dd, yyyy hh:mm a" ignore="true" /></td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader">&nbsp;</td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.whoattends"/> :</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="whoAttends" ignore="true" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.maxattendees"/>:</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="maxAttendees" ignore="true" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.events.moderator"/>:</td>
          <td class="contentCell"><bean:write name="customerEventForm" property="moderator" ignore="true" /></td>
        </tr>
        <c:choose>
          <c:when test="${customerEventForm.map.isRegistered}">
            <html:hidden name="customerEventForm" property="registerAction" value="false" />
            <tr>
              <td class="labelCell"><bean:message key="label.customer.pages.events.registered"/>:</td>
              <td class="contentCell"><html:submit value="<bean:message key='label.value.unregister'/>" styleClass="normalButton" /></td>
            </tr>
          </c:when>
          <c:otherwise>
            <html:hidden name="customerEventForm" property="registerAction" value="true" />
            <tr>
              <td class="labelCell"><bean:message key="label.customer.pages.events.registerforevent"/>:</td>
              <td class="contentCell"><html:submit value="<bean:message key='label.value.register'/>" styleClass="normalButton" /></td>
            </tr>
          </c:otherwise>
        </c:choose>
      </table>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="mainHeader"><bean:message key="label.customer.pages.events.attachedfiles"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="listHeader"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.customer.pages.events.name"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.customer.pages.events.description"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.customer.pages.events.created"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.customer.pages.events.updated"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.customer.pages.events.createdby"/></span></td>
  </tr>
  <c:set var="rowCount" value="0" />
  <c:forEach var="attachment" items="${customerEventForm.map.fileList}">
    <c:choose>
      <c:when test="${rowCount % 2 == 0}">
        <tr class="tableRowOdd">
          <td align="center" class="tableRowOdd"><a href="<html:rewrite page="/files/file_download.do"/>?fileid=<c:out value="${attachment.fileid}" />" class="plainLink"><html:img page="/images/icon_download.gif" width="13" height="13" /></a></td>
          <td class="tableRowOdd"><a href="<html:rewrite page="/files/file_download.do"/>?fileid=<c:out value="${attachment.fileid}" />" class="plainLink"><bean:write name="attachment" property="title" ignore="true" /></a>&nbsp;</td>
          <td class="tableRowOdd" nowrap><bean:write name="attachment" property="description" ignore="true" />&nbsp;</td>
          <td class="tableRowOdd" nowrap><bean:write name="attachment" property="dateCreated" ignore="true" />&nbsp;</td>
          <td class="tableRowOdd" nowrap><bean:write name="attachment" property="dateUpdated" ignore="true" />&nbsp;</td>
          <td class="tableRowOdd" nowrap><bean:write name="attachment" property="createdBy" ignore="true" />&nbsp;</td>
        </tr>
      </c:when>
      <c:when test="${rowCount % 2 == 0}">
        <tr class="tableRowEven">
          <td align="center" class="tableRowEven"><a href="<html:rewrite page="/files/file_download.do"/>?fileid=<c:out value="${attachment.fileid}" />" class="plainLink"><html:img page="/images/icon_download.gif" width="13" height="13" /></a></td>
          <td class="tableRowEven"><a href="<html:rewrite page="/files/file_download.do"/>?fileid=<c:out value="${attachment.fileid}" />" class="plainLink"><bean:write name="attachment" property="title" ignore="true" /></a>&nbsp;</td>
          <td class="tableRowEven" nowrap><bean:write name="attachment" property="description" ignore="true" />&nbsp;</td>
          <td class="tableRowEven" nowrap><bean:write name="attachment" property="dateCreated" ignore="true" />&nbsp;</td>
          <td class="tableRowEven" nowrap><bean:write name="attachment" property="dateUpdated" ignore="true" />&nbsp;</td>
          <td class="tableRowEven" nowrap><bean:write name="attachment" property="createdBy" ignore="true" />&nbsp;</td>
        </tr>
      </c:when>
    </c:choose>
    <c:set var="rowCount" value="${rowCount + 1}" />
  </c:forEach>
</table>
</html:form>