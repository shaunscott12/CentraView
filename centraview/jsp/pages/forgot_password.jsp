<%--
 * $RCSfile: forgot_password.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:49 $ - $Author: mcallist $
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

<html:form action="/forgot">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.forgotyourpassword"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="3">
	<tr>
		<td>
			<p class="plainText">
			<c:if test = "${action == 'display'}">
	        <bean:message key="label.pages.usernameandpassword"/>
			</c:if>
			<c:if test = "${action == 'fail'}">
				<bean:message key="label.pages.nomatchinglogininfo"/>.
			</c:if>
			<c:if test = "${action == 'succeed'}">
				<bean:message key="label.pages.logininfoemailed"/>.
			</c:if>
      </p>
     </td>
	</tr>
</table>
<c:if test = "${action == 'display'}">
	<table border="0" cellspacing="0" cellpadding="3">
		<tr>
			<td><span class="plainText"><bean:message key="label.pages.username"/>:</span></td>
			<td><html:text property="username" styleClass="inputBox"/></td>
		</tr>
		<tr>
			<td><span class="plainText"><bean:message key="label.pages.email"/>:</span></td>
			<td><html:text property="email" styleClass="inputBox" /></td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<html:submit property="remindme" styleClass="normalButton">
         <bean:message key="label.pages.remindme"/>
				</html:submit>
			</td>
		</tr>
	</table>
</c:if>
<c:if test = "${action == 'fail' || action == 'succeed'}">
	<table border="0" cellspacing="0" cellpadding="3">
		<tr>
			<td>
				<span class="plainText"><bean:message key="label.pages.backtologin"/>:
					<a href="<%=request.getContextPath()%>/start.do" class="plainLink"><bean:message key="label.pages.clickhere"/>.</a>
				</span>
			</td>
		</tr>
	</table>
</c:if>
</html:form>
