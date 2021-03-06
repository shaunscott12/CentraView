<%--
 * $RCSfile: new_literature.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.administration.user.UserSettingsForm" %>
<script language="javascript">
  function submitForm(closeorsave)
  {
    document.forms.newliteratureformadmin.action = "<html:rewrite page="/administration/save_new_literature.do"/>?closeorsave="+closeorsave;
    document.forms.newliteratureformadmin.submit();
	}
</script>
<html:form action ="/administration/save_new_literature" enctype="multipart/form-data">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.marketingmodulesettingsnewliterature"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
<bean:message key="label.administration.createeditdeletelistliterature"/>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.name"/>:</td>
    <td class="contentCell">
      <html:text property="literatureName" styleId="literatureName" styleClass="inputBox" size="45"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.file"/>:</td>
    <td class="contentCell">
      <html:file property="file" styleId="file" size="46"/>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="submitForm('save');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="savenclose" styleId="savenclose" styleClass="normalButton" onclick="submitForm('close');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <input type="button" name="cancel" id="cancel" value="<bean:message key='label.value.cancel'/>" class="normalButton" onclick="c_goTo('<html:rewrite page="administation/view_module_settings.do"/>?typeofsubmodule=Marketing');" />
    </td>
  </tr>
</table>
</html:form>