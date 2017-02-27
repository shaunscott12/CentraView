<%--
 * $RCSfile: new_rule.jsp,v $ $Revision: 1.3 $ $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
 * The developer of the Original Code is CentraView. Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved. The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<script language="Javascript">
  function goToRulesList()
  {
    c_goTo('/email/rules_list.do?accountID=<bean:write name="ruleForm" property="accountID" ignore="true" />');
  }

	function newRow()
	{
		document.forms.ruleForm.action = "<%=request.getContextPath()%>/email/new_rule.do";
		document.forms.ruleForm.addRow.value = "true";
		submitForm();
	}

	function submitForm()
	{
		document.forms.ruleForm.submit();
	}
</script>

<html:form action="/email/save_new_rule.do">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.mail.newemailrule"/></td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="4" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.mail.rulename"/>: </td>
    <td class="contentCell"><html:text property="name" styleClass="inputBox" size="35"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.mail.description"/>: </td>
    <td class="contentCell"><html:textarea property="description" cols="34" rows="4" styleClass="InputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.mail.enabled"/> </td>
    <td class="contentCell">
      <html:select property="enabled" styleClass="inputBox">
        <html:option value="Yes"><bean:message key="label.mail.yes"/></html:option>
        <html:option value="No"><bean:message key="label.mail.no"/></html:option>
      </html:select>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td class="sectionHeader">&nbsp;</td>
		<td class="sectionHeader"><bean:message key="label.mail.field"/></td>
		<td class="sectionHeader"><bean:message key="label.mail.condition"/></td>
		<td class="sectionHeader"><bean:message key="label.mail.criteria"/></td>
		<td class="sectionHeader">&nbsp;</td>
	</tr>
  <tr>
  <%-- Start Looping through criteria here --%>
  <c:set var="counter" value="${0}" />
  <c:set var="css" value="tableRowOdd" />
  <c:forEach var="searchCriteria" items="${ruleForm.map.searchCriteria}">
    <c:choose>
      <c:when test="${(counter % 2) == 0}">
        <c:set var="css" value="tableRowOdd" />
      </c:when>
      <c:otherwise>
        <c:set var="css" value="tableRowEven" />
      </c:otherwise>
    </c:choose>
    <td class="<c:out value="${css}" />">
      <html:select indexed="true" name="searchCriteria" property="expressionType" styleClass="inputBox">
        <html:option value="AND"><bean:message key="label.mail.and"/></html:option>
        <html:option value="OR"><bean:message key="label.mail.or"/></html:option>
      </html:select>
    </td>
    <%-- Table list dropdown here? We only have "Email" --%>
    <td class="<c:out value="${css}" />">
      <html:select indexed="true" name="searchCriteria" property="fieldID" styleClass="inputBox">
        <html:optionsCollection property="fieldList" label="label" value="value"/>
      </html:select>
      <%-- Subject Sender Recipient Body Date --%>
    </td>
    <td class="<c:out value="${css}" />">
      <html:select indexed="true" name="searchCriteria" property="conditionID" styleClass="inputBox">
        <html:optionsCollection property="conditionList" value="value" label="label"/>
      </html:select>
      <%-- Contains, Doesn't Contain, Equals, Does not equal, Begins with, Ends with --%>
    </td>
    <td class="<c:out value="${css}" />">
      <html:text indexed="true" name="searchCriteria" property="value" styleClass="inputBox" />
    </td>
    <td class="<c:out value="${css}" />">
      <html:img page="/images/spacer.gif" height="1" width="2" />
      <input type="button" class="normalButton" name="removeRowButton" value="<bean:message key='label.value.removerow'/>" onclick="discardRow('<c:out value="${counter}" />');" />
      <html:img page="/images/spacer.gif" height="1" width="2" />
     </td>
  </tr>
  <c:set var="counter" value="${counter + 1}" />
  </c:forEach>
  <%-- Stop looping through criteria here --%>
  <tr>
    <td class="listHeader" colspan="5">
    <input type=button class="normalButton" name ="addRowButton" value="<bean:message key='label.value.addrow'/>" onclick="newRow();">
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.mail.performactions"/>:</td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td><html:checkbox property="moveMessage" value="true" /></td>
    <td class="labelCell"><bean:message key="label.mail.movemessage"/>:</td>
    <td class="contentCell">
      <html:select property="folderID" styleClass="inputBox">
        <html:option value="0">-- <bean:message key="label.mail.select"/> --</html:option>
        <html:optionsCollection property="folderList" label="value" value="key"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td><html:checkbox property="markMessageRead" value="true" /></td>
    <td class="labelCell"><bean:message key="label.mail.markas"/> &quot;<bean:message key="label.mail.read"/>&quot;</td>
    <td class="contentCell">&nbsp; </td>
  </tr>
  <tr>
    <td><html:checkbox property="deleteMessage" value="true" /></td>
    <td class="labelCell"><bean:message key="label.mail.deletemessage"/> </td>
    <td class="contentCell">&nbsp; </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="4" width="100%">
	<tr>
		<td class="buttonRow">
			<html:submit styleClass="normalButton">
				<bean:message key="label.saveandclose" />
			</html:submit>
			<html:button property="cancel" styleClass="normalButton" onclick="goToRulesList();">
				<bean:message key="label.cancel" />
			</html:button>
		</td>
	</tr>
</table>
<html:hidden property="accountID" />
<html:hidden property="addRow" value="false"/>
<html:hidden property="removeRow" value="false"/>
</html:form>