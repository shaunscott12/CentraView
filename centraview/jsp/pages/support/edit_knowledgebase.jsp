<%--
 * $RCSfile: edit_knowledgebase.jsp,v $ $Revision: 1.3 $ $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
 * The developer of the Original Code is CentraView. Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved. The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script language="javascript">
<c:if test="${closeWindow == 'true'}">
	window.opener.location.reload();
	window.close();
</c:if>
<c:if test="${refreshWindow == 'true'}">
	window.opener.location.reload();
</c:if>
</script>	

<html:form action="/support/edit_knowledgebase.do">
<html:errors/>
<table border="0" cellspacing="0" cellpadding="2" class="formTable">
	<tr>
		<td class="labelCell"><bean:message key="label.support.title"/>: </td>
		<td class="contentCell">
			<html:text property="title" styleClass="inputBox" size="40"/>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.detail"/>: </td>
		<td class="contentCell">
			<html:textarea property="detail" rows="12" styleClass="inputBox" style="width:400;"/>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.status"/>: </td>
		<td class="contentCell">
			<html:select property="status" styleClass="inputBox">
				<html:option value="DRAFT"><bean:message key='label.value.draft'/></html:option>
				<html:option value="PUBLISH"><bean:message key='label.value.publish'/></html:option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.publishtocustomerview"/>: </td>
		<td class="contentCell"><html:checkbox property="publishToCustomerView"/></td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.category"/>: </td>
		<td class="contentCell">
			<html:select property="category" styleClass="inputBox">
				<html:options collection="CATEGORYVOARRAY" property="id" labelProperty="name" />
			</html:select>
		</td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" width="100%">
	<tr>
		<td class="headerBG">
			<app:cvbutton property="saveandclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg="label.general.blank" onclick="return addEditKB('close')">
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="saveandnew" styleClass="normalButton" statuswindow="label.saveandnew" tooltip="true" statuswindowarg="label.general.blank" onclick="return addEditKB('new')">
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<c:if test="${showPermissionButton}">
				<app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
          <bean:message key="label.support.permissions"/>
				</app:cvbutton>
			</c:if>
			<html:cancel styleClass="normalButton" onclick="window.close();">
				<bean:message key="label.cancel"/>
			</html:cancel>
		</td>
	</tr>
</table>
<html:hidden property="rowId"/>
</html:form>

<script language="javascript">
function addEditKB(closeornew)	{
	window.document.forms[0].action="<bean:message key='label.url.root' />/support/edit_knowledgebase.do?closeornew="+closeornew;
	window.document.forms[0].submit();
	return false;			
}

function gotoPermission() {
	window.open("<bean:message key='label.url.root' />/common/record_permission.do?modulename=Knowledgebase&rowID=<bean:write name="kbform" property="rowId"/>&name=<bean:write name="kbform" property="title" ignore="true"/>", '', 'toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=no,width=550,height=360');
}
</script>
