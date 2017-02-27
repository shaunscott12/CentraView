<%--
 * $RCSfile: compose_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<script language="javascript">
  function saveSettings(button)
  {
    button.diabled = true;
    button.className = "disabledButton";
    document.emailSettings.submit();
  }
</script>
<html:form action="/preference/mail/save_composition.do">
<html:hidden property="emailCompositionFlag"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.mail.emailcompsettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell">
      <span class="boldText">
      <bean:message key="label.preference.mail.centraviewemailsettings"/>
      </span>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCellBold" valign="top"><bean:message key="label.preference.mail.composein"/>:</td>
    <td class="contentCell">
      <html:radio property="composeType" styleId="emailCompositionFlag" value="PLAIN" />
      <bean:message key="label.preference.mail.plaintext"/><br>
      <html:radio property="composeType" styleId="emailCompositionFlag" value="HTML" />
      <bean:message key="label.preference.mail.advancedformatting"/>
    </td>
  </tr>
</table>
<p/>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="saveSettings(this);">
        <bean:message key="label.save" />
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>