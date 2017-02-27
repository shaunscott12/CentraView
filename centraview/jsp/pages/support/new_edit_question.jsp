<%--
 * $RCSfile: new_edit_question.jsp,v $ $Revision: 1.4 $ $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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
<%@ page import="com.centraview.common.Constants" %>

<script language="javascript">
<c:if test="${closeWindow == 'true'}">
	window.opener.location.reload();
	window.close();
</c:if>
<c:if test="${refreshWindow == 'true'}">
	window.opener.location.reload();
</c:if>
</script>

<html:form action="/support/save_question.do">
<html:errors/>
<table border="0" cellspacing="0" cellpadding="2" class="formTable">
	<tr>
		<td class="labelCell"><bean:message key="label.support.question"/>: </td>
		<td class="contentCell">
			<html:text property="title" styleClass="inputBox" size="40"/>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.answer"/>: </td>
		<td class="contentCell">
			<html:textarea property="answer" rows="10" styleClass="inputBox" style="width:300;"/>
		</td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" width="100%">
	<tr>
		<td class="headerBG">
			<app:cvbutton property="savenclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditQuestion('close');">
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="savennew" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditQuestion('new');">
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="window.close();">
				<bean:message key="label.cancel"/>
			</app:cvbutton>
		</td>
	</tr>
</table>
<html:hidden property="faqid"/>
</html:form>

<script language="javascript">
function checkAddEditQuestion(closeornew)	{
	var url = "<bean:message key='label.url.root'/>/support/save_question.do?<%=Constants.TYPEOFOPERATION%>=<%=request.getParameter(Constants.TYPEOFOPERATION)%>&questionid=<%=request.getParameter("rowId")%>&closeornew="+closeornew;
	window.document.forms[0].action=url;
	window.document.forms[0].submit();
	return false;
}
</script>