<%--
 * $RCSfile: view_template.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%-- BEGIN FCKEditor CODE --%>
<c:if test="${composeMailForm.map.composeInHTML}">
<script type="text/javascript" src="<%=request.getContextPath()%>/fckeditor/fckeditor.js"></script>
<script language="Javascript1.2">
<!--
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor('templateData', '100%', '230', 'cvDefault', '');
    oFCKeditor.ReplaceTextarea();
  }
-->
</script>
</c:if>
<%-- END FCKEditor CODE --%>
<script language="JavaScript1.2" defer>
<!--

  function subjectVisible(category)
  {
    field = document.getElementById('subjectField');
    if (category == '2') {
      field.style.display = 'table-row';
    } else {
      field.style.display = 'none';
    }
  }
//-->
</script>
<html:form action="/administration/save_template.do">
<html:hidden property="id" styleId="id"/>
<html:hidden property="artifactId" styleId="artifactId"/>
<html:hidden property="isDefault" styleId="isDefault"/>
<html:hidden property="userId" styleId="userId"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.modulesettingstemplatemanagement"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.templatename"/>:</td>
    <td class="contentCell"><html:text property="templateName" styleId="templateName" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.templateid"/>:</td>
    <td class="contentCell"><c:out value="${printTemplateForm.map.id}"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.templatetype"/>:</td>
    <td class="contentCell">
      <html:select property="categoryId" styleId="categoryId" styleClass="inputBox" onchange="subjectVisible(this.value);">
        <html:optionsCollection property="typeList" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr name="subjectField" id="subjectField" style="display:none;">
    <td class="labelCell"><bean:message key="label.administration.subject"/>:</td>
    <td class="contentCell"><html:text property="subject" styleId="subject" styleClass="inputBox" size="45" /></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable" width="100%">
  <tr>
    <td class="contentCell">
      <html:textarea property="templateData" styleId="templateData" styleClass="inputBox" cols="82" rows="24" style="width:100%;"></html:textarea>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:submit styleClass="normalButton">
        <bean:message key="label.save"/>
      </html:submit>
      <html:button property="cancelButton" styleClass="normalButton" onclick="c_goTo('/administration/template_list.do');">
        <bean:message key="label.cancel" />
      </html:button>
    </td>
  </tr>
</table>
</html:form>
<script>
  subjectVisible('<c:out value="${printTemplateForm.map.categoryId}"/>');
</script>