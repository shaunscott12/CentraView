<%--
 * $RCSfile: copyto_individual.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<p><bean:message key="label.contacts.selecttext"/>.</p>
<p>
<html:form action="/contacts/save_individual.do">
<c:out value="${individualForm.map.firstName}"/> <c:out value="${individualForm.map.lastName}"/><br/>
<c:out value="${individualForm.map.entityName}"/>
<c:out value="${individualForm.map.street1}"/><br />
<c:out value="${individualForm.map.street2}"/><br />
<c:out value="${individualForm.map.city}"/>, <c:out value="${individualForm.map.state}"/> <c:out value="${individualForm.map.zip}"/><br />
<c:out value="${individualForm.map.country}"/><br />
<c:out value="${individualForm.map.mocContent1}"/><br />
<c:out value="${individualForm.map.email}"/><br />
</p>
<input type="button" class="normalButton" onclick="window.close();" value="<bean:message key='label.contacts.closewindow'/>">
</html:form>