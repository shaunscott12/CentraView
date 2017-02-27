<%--
 * $RCSfile: email_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.common.*,com.centraview.administration.configuration.*"%>
<%@ page import="com.centraview.administration.common.AdministrationConstantKeys" %>
<%@ page import="com.centraview.administration.authorization.*" %>
<html:form action="/administration/save_email_settings.do" >
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.systememailsettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.stmpserver"/>:</td>
    <td class="contentCell"><html:text property="smtpserver" styleId="smtpserver" size="40" styleClass="inputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.stmpport"/>:</td>
    <td class="contentCell">
      <html:text property="port" styleId="port" size="6" styleClass="inputBox" />
      <span class="boldText">* <bean:message key="label.administration.defaultstmpserver"/>.</span>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.login"/>:</td>
    <td class="contentCell"><html:text property="username" styleId="username" size="40" styleClass="inputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.password"/>:</td>
    <td class="contentCell"><html:password property="password" styleId="password" size="40" styleClass="inputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.authentication"/>:</td>
    <td class="contentCell"><html:checkbox property="authentication" styleId="authentication" value="yes" /> <bean:message key="label.administration.serverrequires"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.emailtemplates"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
<c:forEach var="emailTemplateList" items="${emailSettingsForm.map.emailTemplateList}">
  <tr>
    <td class="labelCell">
      <a href="<html:rewrite page="/administration/view_email_template.do"/>?templateID=<c:out value="${emailTemplateList.templateID}"/>" class="plainLink"><c:out value="${emailTemplateList.name}"/></a>
    </td>
    <td class="contentCell">
      <c:out value="${emailTemplateList.description}"/>
    </td>
  </tr>
</c:forEach>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvsubmit property="save" styleClass="normalButton">
        <bean:message key="label.save" />
      </app:cvsubmit>
      <app:cvreset property="reset" styleClass="normalButton">
        <bean:message key="label.reset" />
      </app:cvreset>
    </td>
  </tr>
</table>
</html:form>
