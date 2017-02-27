<%--
 * $RCSfile: email_template.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/01 17:28:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<script>
  function cancelButton()
  {
    document.emailTemplateForm.action = "<html:rewrite page="/administration/email_settings.do" />";
    document.emailTemplateForm.submit();
  }
</script>
<html:form action="/administration/save_email_template.do" >
<html:hidden property="templateID" styleId="templateID" />
<html:hidden property="name" styleId="name" />
<html:hidden property="description" styleId="description" />
<html:hidden property="requiredToAddress" styleId="requiredToAddress" />
<html:hidden property="requiredFromAddress" styleId="requiredFromAddress" />
<html:hidden property="requiredSubject" styleId="requiredSubject" />
<html:hidden property="requiredBody" styleId="requiredBody" />
<html:hidden property="requiredReplyTo" styleId="requiredReplyTo" />
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.emailtemplatedetails"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.name"/>:</td>
    <td class="contentCell"><c:out value="${emailTemplateForm.name}" default="" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.description"/>:</td>
    <td class="contentCell"><c:out value="${emailTemplateForm.description}" default="" /></td>
  </tr>
  <c:if test='${emailTemplateForm.requiredToAddress != "false"}'>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.to"/>:</td>
    <td class="contentCell"><html:text property="toAddress" styleId="toAddress" styleClass="inputBox" size="50" /></td>
  </tr>
  </c:if>
  <c:if test='${emailTemplateForm.requiredFromAddress != "false"}'>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.from"/>:</td>
    <td class="contentCell"><html:text property="fromAddress" styleId="fromAddress" styleClass="inputBox" size="50" /></td>
  </tr>
  </c:if>
  <c:if test='${emailTemplateForm.requiredReplyTo != "false"}'>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.replyto"/>:</td>
    <td class="contentCell"><html:text property="replyTo" styleId="replyTo" styleClass="inputBox" size="50" /></td>
  </tr>
  </c:if>
  <c:if test='${emailTemplateForm.requiredSubject != "false"}'>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.subject"/>:</td>
    <td class="contentCell"><html:text property="subject" styleId="subject" styleClass="inputBox" size="50" /></td>
  </tr>
  </c:if>
  <c:if test='${emailTemplateForm.requiredBody != "false"}'>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.body"/>:</td>
    <td class="contentCell"><html:textarea property="body" styleId="body" cols="72" rows="15" styleClass="inputBox" /></td>
  </tr>
  </c:if>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:submit styleClass="normalButton"><bean:message key="label.value.save"/></html:submit>
      <html:button property="cancel" styleClass="normalButton" onclick="c_goTo('/administration/email_settings.do');" >
        <bean:message key="label.cancel" />
      </html:button>
    </td>
  </tr>
</table>
</html:form>