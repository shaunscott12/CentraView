<%--
 * $RCSfile: edit_custom_view.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<script language="javascript">
</script>
<html:form action="/administration/edit_custom_view.do" styleId="viewform">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.editcustomview"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="4" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.viewname"/>:</td>
    <td class="contentCell"><html:text property="viewName" styleId="viewName" styleClass="inputBox" size="45"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.module"/>:</td>
    <td class="contentCell">
      <c:out value="${customViewForm.map.moduleName}" default="" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.recordtype"/>:</td>
    <td class="contentCell">
      <c:out value="${customViewForm.map.recordType}" default="" />
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.viewdetails"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell">
      <span class="boldText"><bean:message key="label.administration.availablefields"/>:</span><br/>
      <html:select property="availableFieldsValue" styleId="availableFieldsValue" size="10" styleClass="inputBox" style="width:20em;" multiple="true">
        <html:optionsCollection property="availableFields" value="fieldId" label="fieldName"/>
      </html:select>
    </td>
    <td>
      <app:cvbutton property="cmpadd" styleId="cmpadd" styleClass="normalButton" onclick="addToSelected();" style="width:6em;">
        <bean:message key="label.add"/> &raquo;
      </app:cvbutton>
      <br/><br/>
      <app:cvbutton property="cmpremove" styleId="cmpremove" styleClass="normalButton" onclick="remove();" style="width:6em;">
        &laquo; <bean:message key="label.remove"/>
      </app:cvbutton>
     </td>
     <td>
      <span class="boldText"><bean:message key="label.administration.selectedfields"/>:</span><br/>
      <html:select property="selectedFieldsValue" styleId="selectedFieldsValue" size="10" styleClass="inputBox" style="width:20em;" multiple="true" value="">
        <html:optionsCollection property="selectedFields" value="fieldId" label="fieldName"/>
      </html:select>
    </td>
    <td>
      <app:cvbutton property="cmpmoveup" styleId="cmpmoveup" styleClass="normalButton" onclick="moveup();" style="width:6em;">
        <bean:message key="label.moveup"/>
      </app:cvbutton>
      <br/><br/>
      <app:cvbutton property="cmpmovedown" styleId="cmpmovedown" styleClass="normalButton" onclick="movedown()" style="width:6em;">
        <bean:message key="label.movedown"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration."/>:</td>
    <td class="contentCell">
      <html:select property="sortColumn" styleId="sortColumn" styleClass="inputBox">
        <html:optionsCollection property="allFields" value="fieldId" label="fieldName"/>
      </html:select>
      &nbsp;&nbsp;
      <span class="popupTableTextBold">
        <html:radio property="sortDirection" styleId="sortDirection" value="A"/> <bean:message key="label.ascending"/>
        <html:radio property="sortDirection" styleId="sortDirection" value="D"/> <bean:message key="label.descending"/>
      </span>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.recordsperpage"/>:</td>
    <td class="contentCell">
      <html:select property="recordsPerPage" styleId="recordsPerPage" styleClass="inputBox">
        <html:option value="10">  10 </html:option>
        <html:option value="20">  20 </html:option>
        <html:option value="50">  50 </html:option>
        <html:option value="100"> 100 </html:option>
        <html:option value="All"> <bean:message key="label.administration.all"/> </html:option>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.defaultsavedsearch"/>:</td>
    <td class="contentCell">
      <html:select property="defaultSearch" styleId="defaultSearch" styleClass="inputBox">
        <html:option value="0"> --<bean:message key="label.administration.selectsavedsearch"/>-- </html:option>
        <html:optionsCollection property="searchList" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="saveapply" id="saveapply" type="button" class="normalButton" value="<bean:message key='label.administration.saveandclose'/>" onClick="saveview('saveapply');" />
      <input name="reset" id="reset" type="button" class="normalButton" value="<bean:message key='label.administration.resetfields'/>">
      <input name="cancel" id="cancel" type="button" class="normalButton" value="<bean:message key='label.administration.cancel'/>" onClick="gotocancel();" />
    </td>
  </tr>
</table>
</html:form>