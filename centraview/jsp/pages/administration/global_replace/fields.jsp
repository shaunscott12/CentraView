<%--
 * $RCSfile: fields.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:53 $ - $Author: mcallist $
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
<%@ page import="com.centraview.administration.globalreplace.GlobalReplaceConstantKeys" %>
<html:form action="administration/global_replace_fields.do">
<html:hidden property="fieldName" styleId="fieldname" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.global.globalreplacecriteria"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.global.table"/>:</td>
    <td class="contentCell">
      <bean:write name="globalReplaceForm" property="tableName" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.global.field"/>:</td>
    <td class="contentCell">
      <html:select property="replaceFieldID" styleId="replaceFieldID" styleClass="inputBox" onchange="submitForm();">
        <html:optionsCollection  property="replaceFieldVec" value="strid" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.global.replacewith"/>:</td>
    <td class="contentCell">
      <c:if test="${globalReplaceForm.map.fieldType == '0'}">
        <html:text property="replaceValue" styleId="replaceValue" styleClass="inputBox"  size="35" />
      </c:if>
      <c:if test="${globalReplaceForm.map.fieldType == '6'}">
        <html:text property="replaceValue" styleId="replaceValue" styleClass="inputBox"  size="35" />
        &nbsp;EXT&nbsp;
        <html:text property="replaceExt" styleId="replaceExt" styleClass="inputBox"  size="4" />
      </c:if>
      <c:if test="${globalReplaceForm.map.fieldType == '8'}">
        <html:hidden property="replaceValue" styleId="replaceValue" />
        <html:select property="replaceID" styleId="replaceID" styleClass="inputBox" onchange ="fieldValue();">
          <html:optionsCollection  property="replaceVec" value="strid" label="name" />
        </html:select>
      </c:if>
      <c:if test="${globalReplaceForm.map.fieldType != '0' && globalReplaceForm.map.fieldType != '8' && globalReplaceForm.map.fieldType != '6'}">
        <html:hidden property="replaceID" styleId="replaceID" />
        <html:text property="replaceValue" styleId="replaceValue" styleClass="inputBox"  size="35" />
        <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="fnLookup();">
          <bean:message key="label.lookup"/>
        </app:cvbutton>
      </c:if>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow" align="right">
      <app:cvbutton property="next" styleId="next" styleClass="normalButton" onclick="nextForm();">
        <bean:message key="label.pages.administration.global.replace"/> &raquo;
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>
<%-- Taking the easy way out for now
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="pagingBarContainer"><span class="boldText">&nbsp;&nbsp;<bean:message key="label.pages.administration.global.replaceappliedto"/>:</span></td>
  </tr>
</table>
--%>
<script language="JavaScript" type="text/javascript">
<!--
	function populateFields()
  {
		var fieldname = document.forms.globalReplaceForm.replaceFieldID[document.forms.globalReplaceForm.replaceFieldID.selectedIndex].text ;
		document.forms.globalReplaceForm.fieldName.value= fieldname;
	}

	function fieldValue()
  {
		var fieldvalue = document.forms.globalReplaceForm.replaceID[document.forms.globalReplaceForm.replaceID.selectedIndex].text ;
		document.forms.globalReplaceForm.replaceValue.value= fieldvalue;
	}

	function submitForm()
	{
		populateFields();
		document.forms.globalReplaceForm.submit();
	}

	function nextForm()
	{
		populateFields();
		document.forms.globalReplaceForm.action = "<bean:message key='label.url.root' />/administration/global_replace_perform.do";
		document.forms.globalReplaceForm.submit();
	}

	function setEmployee(individualLookupValues)
  {
		firstName = individualLookupValues.firstName;
		id = individualLookupValues.individualID;
		middleName = individualLookupValues.middleName;
		lastName = individualLookupValues.lastName;
		title = individualLookupValues.title;

		document.globalReplaceForm.replaceID.value = id;
		document.globalReplaceForm.replaceValue.value = firstName + " " + middleName + " " + lastName;
	}

  function setIndividual(individualLookupValues)
  {
		firstName = individualLookupValues.firstName;
		id = individualLookupValues.individualID;
		middleName = individualLookupValues.middleName;
		lastName = individualLookupValues.lastName;
		title = individualLookupValues.title;

		document.globalReplaceForm.replaceID.value = id;
		document.globalReplaceForm.replaceValue.value = firstName + " " + middleName + " " + lastName;
  }

  function setGrp(name,id)
  {
    document.globalReplaceForm.replaceValue.value = name;
    document.globalReplaceForm.replaceID.value = id;
    idGrp = id;
  }

  function setSource(lookupValues)
  {
    document.globalReplaceForm.replaceID.value = lookupValues.id;
    document.globalReplaceForm.replaceValue.value = lookupValues.Name;
  }

	function setEntity(entityLookupValues)
  {
		name = entityLookupValues.entName;
		id = entityLookupValues.entID;
		acctmgrid = entityLookupValues.acctManagerID;
		acctmgrname = entityLookupValues.acctManager;
    document.globalReplaceForm.replaceValue.readonly = true;
    document.globalReplaceForm.replaceID.value = id;
    document.globalReplaceForm.replaceValue.value = name;
  }

  function fnLookup()
  {
    var lookupID = <c:out value="${globalReplaceForm.map.fieldType}"/>;
    if (lookupID == <%=GlobalReplaceConstantKeys.FIELD_TYPE_INDIVIDUAL%>) {
      c_lookup('Individual');
    }

    if (lookupID == <%=GlobalReplaceConstantKeys.FIELD_TYPE_GROUP%>) {
      c_lookup('Group');
    }

    if (lookupID == <%=GlobalReplaceConstantKeys.FIELD_TYPE_SOURCE%>) {
      c_lookup('Source');
    }

    if (lookupID == <%=GlobalReplaceConstantKeys.FIELD_TYPE_EMPLOYEE%>) {
      c_lookup('Employee');
    }

    if (lookupID == <%=GlobalReplaceConstantKeys.FIELD_TYPE_ENTITY%>) {
      c_lookup('Entity');
    }
  }

  function fnPrimary()
  {
    var tempId = document.globalReplaceForm.id1.value;
    var entityname = document.globalReplaceForm.name.value;
    var mychild = openWindow("<bean:message key='label.url.root' />/PrContactLookup.do?listId=<%=request.getParameter("listId")%>&entityId="+tempId+"&entityname="+entityname, '', 400, 400,'');
  }

  function fnInd()
  {
    var mychild = openWindow('<bean:message key='label.url.root' />/EmployeeLookup.do?listId=<%=request.getParameter("listId")%>', '', 400, 400,'');
  }

  function fnGrp()
  {
    var mychild = openWindow('<bean:message key='label.url.root' />/GroupLookup.do', '', 400, 400,'');
  }

-->
</script>