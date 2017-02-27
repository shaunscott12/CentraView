<%--
 * $RCSfile: calendar_nav_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table  cellspacing="0" cellpadding="0" border="0" width="100%" class="formTable">
  <tr>
    <td class="headerBG" valign="middle">
		<c:set var="rowCount" value="0" />
		<c:forEach var="calendarNavBar1" items="${calendarForm.map.calendarNavBar}">
			<a href="javascript:<bean:write name='calendarNavBar1' property='name' ignore='true' />" class="plainLink">
				<bean:write name='calendarNavBar1' property='strid' ignore='true' />
			</a>
			<c:if test="${rowCount != 2}">
				&nbsp;|&nbsp;
			</c:if>
			<c:if test="${rowCount == 2}">
				&nbsp;
			</c:if>
			<c:set var="rowCount" value="${rowCount + 1}" />
		</c:forEach>
		<input name="selectDate" type="text" class="inputBox" size="15" value="MM/DD/YYYY">
		<input class="normalButton" type="button" value="<bean:message key='label.value.go'/>" onclick="javascript:changeDate();">
		<html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" align="abstop" onclick="openCalendar();" />
    </td>
  </tr>
</table>