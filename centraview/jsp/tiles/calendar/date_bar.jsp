<%--
 * $RCSfile: date_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
String type = (String)request.getParameter("Type");
if(type == null){
type="";
}
%>
<table border="0" cellpadding="2" cellspacing="0" class="searchTable">
  <tr>
    <td><span class="headerTextBold"><bean:message key="label.tiles.calendar.activitiesfor"/>  <c:out value="${calendarForm.map.selectedDate}" /></span></td>
    <td>
		<html:select property="delegatorName" styleClass="inputBox" style="width:10em" onchange="document.calendarForm.submit();">
			<html:options collection="delegatorNameVec" property="strid" labelProperty="name"/>
		</html:select>
    </td>
    <td>
		<html:select property="activityType" styleClass="inputBox" style="width:10em" onchange="document.calendarForm.submit();">
			<html:options collection="activityTypeVec" property="strid" labelProperty="name"/>
		</html:select>
		<input type="hidden" name="Type" value="<%=type%>"/>
    </td>
  </tr>
</table>