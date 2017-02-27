<%--
 * $RCSfile: view_folder.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:if test="${mailFolderForm.map.closeWindow}">
	<script language="Javascript">
		window.opener.location.href = window.opener.location.href;
    window.close();
	</script>
</c:if>

<html:form action="/email/view_folder.do">
<html:errors/>
<table border="0" cellPadding="3" cellSpacing="0" class="formTable">
  <tr>
    <td class="labelCell"> <bean:message key="label.email.foldername"/>: </td>
    <td>
      <html:text property="folderName" styleClass="inputBox"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell">  <bean:message key="label.email.parentfolder"/>: </td>
    <td>
      <html:text readonly="true" styleClass="inputBox" property="fullPath"/>
      <html:button property="change" styleClass="normalButton" onclick="openFolderLookup()">
        <bean:message key="label.email.change"/>
      </html:button>
    </td>
  </tr>
</table>
<table border="0" cellpadding="3" width="100%">
  <tr>
    <td class="sectionHeader">
			<c:choose>
				<c:when test="${mailFolderForm.map.folderType != 'SYSTEM'}">
					<html:submit styleClass="normalButton">
						<bean:message key="label.saveandclose"/>
					</html:submit>
				</c:when>
				<c:when test="${mailFolderForm.map.folderType == 'SYSTEM'}">
					<html:submit styleClass="normalButton" disabled="true">
						<bean:message key="label.saveandclose"/>
					</html:submit>
				</c:when>
			</c:choose>
      &nbsp;
			<html:button property="deleteFolder" styleClass="normalButton" onclick="deleteMailFolder();">
        <bean:message key="label.email.deletefolder"/>
			</html:button>
			&nbsp;
      <html:cancel  styleClass="normalButton"  onclick="window.close();">
        <bean:message key="label.cancel"/>
      </html:cancel>
    </td>
  </tr>
</table>
<html:hidden property="parentID" />
<html:hidden property="folderID" />
</html:form>

<script language="javascript">
function openFolderLookup() {
	c_openWindow('/folder_lookup.do?actionType=EMAIL&folderID=<bean:write name="mailFolderForm" property="parentID" ignore="true" />', 'lookup_folder', 250, 400, '');
}

function changeParentFolder(newFolderID) {
	document.forms.mailFolderForm.parentID.value = newFolderID;
	document.forms.mailFolderForm.action = "<%=request.getContextPath()%>/email/new_folder.do";
	document.forms.mailFolderForm.submit();
}

<%-- // if we're closing the window, don't print this function, as it gives a JS error --%>

function deleteMailFolder() {
	if (<bean:write name="mailFolderForm" property="totalMessages" /> == 0 && ! <bean:write name="mailFolderForm" property="hasSubFolders" ignore="true" />) {
		c_goTo('/email/delete_folder.do?folderID=<bean:write name="mailFolderForm" property="folderID" ignore="true" />');
		return true;
	} else {
		alert("<bean:message key='label.alert.foldertobedeletedbeempty'/>.");
		return false;
	}
}

</script>
