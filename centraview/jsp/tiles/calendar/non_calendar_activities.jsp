<%--
 * $RCSfile: non_calendar_activities.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table width="210" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td class="sectionHeader"><bean:message key="label.tiles.calendar.noncalendaractivities"/></td>
  </tr>
	<c:set var="rowCount" value="0" />
	<c:forEach var="nonCalendarActivity" items="${calendarForm.map.nonCalendarActivity}">
		<c:choose>
			<c:when test="${rowCount % 2 != 0}">
				<tr>
					<td class="tableRowEven">
						<img src="<html:rewrite page="/images" />/<c:out value='${nonCalendarActivity["activityIcon"]}' />"/>
						<a href="javascript:c_openPopup('<c:out value='${nonCalendarActivity["activityLink"]}' />');" class="plainLink"><c:out value='${nonCalendarActivity["activityTitle"]}' /></a>
					</td>
				</tr>
			</c:when>
			<c:when test="${rowCount % 2 == 0}">
				<tr>
					<td class="tableRowOdd">
						<img src="<html:rewrite page="/images" />/<c:out value='${nonCalendarActivity["activityIcon"]}' />"/>
						<a href="javascript:c_openPopup('<c:out value='${nonCalendarActivity["activityLink"]}' />');" class="plainLink"><c:out value='${nonCalendarActivity["activityTitle"]}' /></a>
					</td>
				</tr>
			</c:when>
		</c:choose>
		<c:set var="rowCount" value="${rowCount + 1}" />
	</c:forEach>
</table>