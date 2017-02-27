<%--
 * $RCSfile: auto_check_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<script language="javascript">
  function updateSettings(button)
  {
    button.disabled = true;
    button.className = "disabledButton";
    document.mailCheckForm.submit();
  }
</script>
<html:form action="/preference/mail/update_auto_check.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.mail.automailchecksettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <p>
      <bean:message key="label.preference.mail.centraviewautomailcheck"/>.
      </p>
      <br/>
    </td>
  </tr>
  <tr>
    <td class="contentCell">
      <bean:message key="label.preference.mail.interval"/>:&nbsp;
      <html:select property="mailCheckInterval" styleClass="inputBox">
        <html:option value="0"><bean:message key="label.preference.mail.never"/></html:option>
        <html:option value="5"> 5 Min </html:option>
        <html:option value="10"> 10 Min </html:option>
        <html:option value="15"> 15 Min </html:option>
        <html:option value="20"> 20 Min </html:option>
        <html:option value="25"> 25 Min </html:option>
        <html:option value="30"> 30 Min </html:option>
      </html:select>
      <br/>
      <br/>
      <br/>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:button property="update" styleId="update" styleClass="normalButton" onclick="updateSettings(this);"><bean:message key="label.value.update"/></html:button>
    </td>
  </tr>
</table>
</html:form>