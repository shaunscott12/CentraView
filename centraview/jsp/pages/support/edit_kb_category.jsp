<%--
 * $RCSfile: edit_kb_category.jsp,v $ $Revision: 1.2 $ $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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

<html:form action="/support/knowledgebase_editcategory.do">
<html:errors/>
<table border="0" cellspacing="0" cellpadding="2" class="formTable">
	<tr>
		<td class="labelCell"><bean:message key="label.support.categoryname"/>: </td>
		<td class="contentCell">
			<html:text property="categoryname" styleClass="inputBox" size="45"/>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.parentcategory"/>: </td>
		<td class="contentCell">
			<html:select property="parentcategory" styleClass="inputBox">
				<c:forEach items="${CATEGORYVOARRAY}" var="current">
					<option value="<c:out value="${current.id}"/>"><c:out value="${current.name}"/></option>
				</c:forEach>
			</html:select>
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
		<td class="contentCell"><html:checkbox property="publishToCustomerView" onclick="checkPublish()"/></td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.owner"/>: </td>
		<td class="contentCell"><a class="plainLink" href="javascript:c_openPopup('/contacts/view_individual.do?rowId=<c:out value="${catform.ownerid}"/>');"><c:out value="${catform.owner}"/></a></td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.created"/>: </td>
		<td class="contentCell"><c:out value="${catform.created}"/></td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.modified"/>: </td>
		<td class="contentCell"><c:out value="${catform.modified}"/></td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" width="100%">
	<tr>
		<td class="headerBG">
			<app:cvbutton property="saveandclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg="label.general.blank" onclick="return checkAddEditCategory('close');">
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="saveandnew" styleClass="normalButton" statuswindow="label.saveandnew" tooltip="true" statuswindowarg="label.general.blank" onclick="return checkAddEditCategory('new');">
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<html:cancel styleClass="normalButton" onclick="window.close();">
				<bean:message key="label.cancel"/>
			</html:cancel>
		</td>
	</tr>
</table>
<html:hidden property="TYPEOFOPERATION" value="EDIT"/>
<input type="hidden" name="rowId" value="<c:out value="${rowId}"/>"/>
</html:form>

<script language="javascript">
function checkPublish()	{
	if (window.document.forms[0].publishToCustomerView != null)	{
		if (window.document.forms[0].publishToCustomerView.checked)	{
			alert("<bean:message key='label.alert.allparentcategorypublished'/>." );
		}
	}
}

function checkAddEditCategory(closeornew)	{
	window.document.forms[0].action="<bean:message key='label.url.root' />/support/update_category.do?closeornew="+closeornew;
	window.document.forms[0].submit();
	return false;
}
</script>