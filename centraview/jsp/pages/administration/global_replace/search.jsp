<%--
 * $RCSfile: search.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:53 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="org.apache.struts.action.DynaActionForm" %>
<%
  String searchRadio = (String)(((DynaActionForm)session.getAttribute("globalReplaceForm")).get("searchRadio"));
  boolean savedSearchDisabled = false;
  if (searchRadio != null && searchRadio.equals("New Search")) {
    savedSearchDisabled = true;
  }
%>
<html:form action="administration/global_replace_search.do">
<html:hidden property="tableName" styleId="tableName" />
<html:hidden property="searchType" styleId="searchType" />
<html:hidden property="actionType" styleId="actionType" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.global.globalreplacesearch"/></td>
  </tr>
</table>
<p class="pageInstructions">
  <bean:message key="label.pages.administration.global.pageinstructions"/>.
</p>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.global.datasource"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.global.replacetype"/>:</td>
    <td class="contentCell">
      <html:select property="replaceTableID" styleId="replaceTableID" styleClass="inputBox" onchange="populateFields();">
        <html:optionsCollection property="replaceTableVec" value="strid" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell">List:</td>
    <td class="contentCell">
      <html:select property="listID" styleId="listID" styleClass="inputBox">
        <html:option value="0"><bean:message key="label.pages.administration.global.allrecords"/></html:option>
        <html:optionsCollection property="listVec" value="strid" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell">
      <html:radio property="searchRadio" styleId="searchRadio" value="Saved Search" onclick="populateCheckBox();"/>
      <bean:message key="label.pages.administration.global.savedsearch"/>:
    </td>
    <td class="contentCell">
      <html:select property="savedSearchID" styleId="savedSearchID" styleClass="inputBox" disabled="<%=savedSearchDisabled%>">
        <html:optionsCollection property="savedSearchVec" value="strid" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell">
      <html:radio property="searchRadio" styleId="searchRadio" value="New Search" onclick="populateCheckBox();" />
      <bean:message key="label.pages.administration.global.newsearch"/>
    </td>
    <td class="contentCell">&nbsp;</td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="pagingBarContainer"><span class="boldText">&nbsp;&nbsp;<bean:message key="label.pages.administration.global.searchbuilder"/></span></td>
  </tr>
</table>
<html:hidden property="moduleId" styleId="moduleId" />
<html:hidden property="addRow" styleId="addRow" value="false"/>
<html:hidden property="removeRow" styleId="removeRow" value="false"/>
<html:hidden property="createNew" styleId="createNew" value="false"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="listHeader"><html:img page="/images/spacer.gif" width="1" height="1" /><bean:message key="label.pages.administration.global.andor"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.global.recordtype"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.global.field"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.global.condition"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.global.criteria"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.global.logicalgrouping"/></td>
    <td class="listHeader">&nbsp;</td>
  </tr>
  <c:set var="counter" value="${0}" />
  <c:forEach var="searchCriteria" items="${globalReplaceForm.map.searchCriteria}">
    <c:set var="css" value="tableRowOdd" />
    <c:if test="${counter % 2 != 0}">
      <c:set var="css" value="tableRowEven" />
    </c:if>
    <td class="<c:out value="${css}"/>">
      <html:select indexed="true" name="searchCriteria" property="expressionType" styleId="expressionType" styleClass="inputBox">
        <html:option value="AND"><bean:message key="label.pages.administration.global.and"/></html:option>
        <html:option value="OR"><bean:message key="label.pages.administration.global.or"/></html:option>
      </html:select>
    </td>
    <td class="<c:out value="${css}"/>">
      <html:select indexed="true" name="searchCriteria" property="tableID" styleId="tableID" styleClass="inputBox" onchange="submitForm();">
        <html:optionsCollection property="tableList" value="value" label="label"/>
      </html:select>
    </td>
    <td class="<c:out value="${css}"/>">
      <%--
        Based on the tableID value get the fieldList from the allFields HashMap the +0
        below is a hack, that seems to make the EL parse the searchCriteria.tableID
        instead of trying to use it as the key.  Maybe a bug in the Jakarta EL but I
        couldn't get it to work any other way
      --%>
      <c:set var="fieldList" value="${globalReplaceForm.map.allFields[searchCriteria.tableID+0]}" />
      <html:select indexed="true" name="searchCriteria" property="fieldID" styleId="fieldID" styleClass="inputBox">
        <c:if test="${not empty fieldList}">
        <html:options collection="fieldList" property="value" labelProperty="label"/>
        </c:if>
      </html:select>
    </td>
    <td class="<c:out value="${css}"/>">
      <html:select indexed="true" name="searchCriteria" property="conditionID" styleId="conditionID" styleClass="inputBox">
        <html:optionsCollection property="conditionList" value="value" label="label"/>
      </html:select>
    </td>
    <td class="<c:out value="${css}"/>">
      <html:text indexed="true" name="searchCriteria" property="value" styleId="value" styleClass="inputBox" />
    </td>
    <td class="<c:out value="${css}"/>">
      <html:select indexed="true" name="searchCriteria" property="groupID" styleId="groupID" styleClass="inputBox">
        <html:option value="1">A</html:option>
        <html:option value="2">B</html:option>
      </html:select>
    </td>
    <td class="<c:out value="${css}"/>" nowrap>
      <input type="button" class="normalButton" name="removeRowButton" value="<bean:message key='label.value.removerow'/>" onclick="discardRow('<c:out value="${counter}" />');" />
    </td>
  </tr>
  <!-- End Criteria Line -->
  <c:set var="counter" value="${counter + 1}" />
  </c:forEach>
  <tr>
    <td class="pagingBarContainer" colspan="7">
      <input type=button class="normalButton" name="addRowButton" value="<bean:message key='label.value.addrow'/>" onclick="newRow();">
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow" align="right">
      <app:cvbutton property="next" styleId="next" styleClass="normalButton" tooltip="true" onclick="nextForm()">
        <bean:message key="label.next"/> &raquo;
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>
<script language="javascript" type="text/javascript">
<!--
	function populateFields()
  {
    var tablename = document.forms.globalReplaceForm.replaceTableID[document.forms.globalReplaceForm.replaceTableID.selectedIndex].text ;
		document.forms.globalReplaceForm.tableName.value= tablename;
	}

	function populateCheckBox()
	{
    j = document.forms.globalReplaceForm.searchRadio.length;
    for (i = 0; i < j; i++) {
      if (document.forms.globalReplaceForm.searchRadio[i].checked) {
        searchRadioValue = document.forms.globalReplaceForm.searchRadio[i].value;
        if (searchRadioValue != 'New Search') {
          document.forms.globalReplaceForm.savedSearchID.disabled = false;
        } else {
          document.forms.globalReplaceForm.savedSearchID.disabled = true;
        }
        document.forms.globalReplaceForm.searchType.value=searchRadioValue;
      }
    }
  }

  function submitForm()
  {
    document.forms.globalReplaceForm.searchType.value = "New Search";
    document.forms.globalReplaceForm.submit();
	}

	function nextForm()
	{
		populateFields();
		populateCheckBox();
		document.forms.globalReplaceForm.actionType.value = "Fields";
		document.forms.globalReplaceForm.submit();
	}

	function newRow()
	{
		document.forms.globalReplaceForm.addRow.value = "true";
		submitForm();
	}

	function discardRow(rowIndex)
	{
		var totalRows = <c:out value="${counter}"/>;
		if (totalRows < 2) {
			return false;
		}
		document.forms.globalReplaceForm.removeRow.value = rowIndex;
		submitForm();
	}

	function createNewAction()
	{
		document.forms.globalReplaceForm.createNew.value = "true";
		submitForm();
	}
//-->
</script>