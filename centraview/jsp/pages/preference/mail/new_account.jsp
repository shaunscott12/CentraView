<%--
 * $RCSfile: new_account.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/28 17:22:19 $ - $Author: mcallist $
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
<script language="javascript">
  function createAccount(button)
  {
    button.disabled = true;
    document.mailAccountForm.submit();
  }
</script>
<html:form action="/preference/mail/save_new_account.do">
<html:hidden property="accountID" styleId="accountID" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.mail.newemailaccount"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.accountname"/>:</td>
    <td class="contentCell"><html:text property="accountName" styleId="accountName" styleClass="inputBox" size="45"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.emailaddress"/>:</td>
    <td class="contentCell"><html:text property="emailAddress" styleId="emailAddress" styleClass="inputBox" size="45"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.replytoaddress"/>:</td>
    <td class="contentCell"><html:text property="replyTo" styleId="replyTo" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.servertype"/>:</td>
    <td class="contentCell">
      <html:select property="serverType" styleId="serverType" styleClass="inputBox" >
        <html:option value="pop3">POP</html:option>
        <html:option value="imap">IMAP</html:option>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.serveraddress"/>:</td>
    <td class="contentCell"><html:text property="mailServer" styleId="mailServer" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.username"/>:</td>
    <td class="contentCell"><html:text property="username" styleId="username" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.password"/>:</td>
    <td class="contentCell"><html:password property="password" styleId="password" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell">SMTP (<bean:message key="label.activity.outgoing"/>) Server:</td>
    <td class="contentCell"><html:text property="smtpServer" styleId="smtpServer" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.authenticationrequired"/>:</td>
    <td class="contentCell">
      <html:checkbox property="authenticationRequiredForSMTP" styleId="authenticationRequiredForSMTP" styleClass="checkbox" />
      <span class="plainText"><bean:message key="label.preference.mail.emailisprequireusernameandpassword"/>.</span>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.smtpport"/>:</td>
    <td class="contentCell">
      <html:text property="port" styleId="port" styleClass="inputBox" size="6"/>
      <span class="plainText"><bean:message key="label.preference.mail.defaultport"/>.</span>
    </td>
  </tr>
  <tr>
    <td class="labelCell">&nbsp;</td>
    <td class="contentCell"><html:checkbox property="leaveOnServer" styleId="leaveOnServer" value="yes" /><bean:message key="label.preference.mail.leavemsgonserver"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.mail.signature"/>:</td>
    <td class="contentCell"><html:textarea property="signature" styleId="signature" styleClass="inputBox" cols="72" rows="5" /></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:submit property="saveclose" styleId="saveclose" styleClass="normalButton" onclick="createAccount(this);">
        <bean:message key="label.saveandclose"/>
      </html:submit>
      <html:button property="cmpsave" styleId="cmpsave" styleClass="normalButton" onclick="c_goTo('/preference/mail/account_list.do');">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
</table>
</html:form>