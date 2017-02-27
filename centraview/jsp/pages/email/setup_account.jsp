<%--
 * $RCSfile: setup_account.jsp,v $ $Revision: 1.2 $ $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td class="contentCell">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td class="sectionHeader">
			<bean:message key="label.email.email"/>
		</td>
	</tr>
	<tr>
		<td class="contentCell">
			&nbsp;<bean:message key="label.email.emailprofilesetup"/>. <br>
			&nbsp;<bean:message key="label.email.pleaseklick"/> <a href="<%=request.getContextPath()%>/preference/mail/new_account.do" class="plainLink"> <bean:message key="label.email.here"/> </a>
			<bean:message key="label.email.enteremailsettings"/>.
		</td>
	</tr>
</table>
