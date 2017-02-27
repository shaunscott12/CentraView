<%--
 * $RCSfile: merge_search.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/28 11:46:12 $ - $Author: mcallist $
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
<html:form action="/administration/merge_search_results">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.mergeandpurgesearch"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <bean:message key="label.pages.administration.merge.findrecordstobemerged"/>.
    </td>
  </tr>
  <tr>
    <td class="contentCell">
      <bean:message key="label.pages.administration.merge.resourceintensiveaction"/>.
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.merge"/>:</td>
    <td class="contentCell">
      <html:select property="mergeType" styleId="mergeType" styleClass="inputBox" onchange="changeMergeType();">
        <html:optionsCollection property="mergeTypeCollection" value="strid" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.datasource"/>:</td>
    <td class="contentCell">
      <html:select property="searchDomain" styleId="searchDomain" styleClass="inputBox">
        <html:optionsCollection property="searchDomainCollection" value="strid" label="name"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.totalvalue"/>:</td>
    <td class="contentCell"><html:text property="threshhold" styleId="threshhold" styleClass="inputBox" size="5" /></td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="pagingBarContainer"><span class="boldText"><bean:message key="label.pages.administration.merge.searchcriteria"/></span></td>
  </tr>
</table>
<table border="0" cellpadding="2" cellspacing="0" width="100%" id="search">
  <tr>
    <td class="listHeader"><bean:message key="label.pages.administration.merge.field"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.merge.searchtype"/></td>
    <td class="listHeader"><bean:message key="label.pages.administration.merge.lineitemvalue"/></td>
  </tr>
  <c:set var="count" value="0" />
  <c:forEach var="criteriaLine" items="${mergeSearchForm.map.criteriaLine}">
  <c:set var="css" value="tableRowOdd" />
  <c:if test="${count % 2 != 0}">
    <c:set var="css" value="tableRowEven" />
  </c:if>
  <c:set var="count" value="${count + 1}" />
  <tr>
    <td class="<c:out value="${css}"/>">
      <html:select indexed="true" name="criteriaLine" property="fieldIndex" styleClass="inputBox">
        <html:optionsCollection property="fieldListCollection" value="strid" label="name"/>
      </html:select>
    </td>
    <td class="<c:out value="${css}"/>">
      <html:select indexed="true" name="criteriaLine" property="searchTypeIndex" styleClass="inputBox">
        <html:optionsCollection property="searchTypeCollection" value="strid" label="name" />
      </html:select>
    </td>
    <td align="center" nowrap class="<c:out value="${css}"/>">
      <html:text indexed="true" name="criteriaLine" property="matchValue" styleClass="inputBox" size="5" />
    </td>
  </tr>
  </c:forEach>
  <tr>
    <td class="pagingBarContainer" colspan="3">
      <input name="newRow" type="button" class="normalButton" value="<bean:message key='label.value.addrow'/>" onClick="newrow();"/>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="resetButton" type="button" class="normalButton" title="Reset the Search Form" onClick="resetSearch()" value="<bean:message key='label.value.reset'/>" />&nbsp;
      <input name="nextButton" type="button" value="<bean:message key='label.value.next'/> &raquo;" class="normalButton" title="Submit the Search Request" onClick="submitRequest();" id="submitButton" />
    </td>
  </tr>
</table>
<html:hidden property="addRow" />
</html:form>
<script language="JavaScript" type="text/javascript">
<!--
	function newrow() 
	{
		document.forms.mergeSearchForm.addRow.value = "true";
		document.forms.mergeSearchForm.action = "<html:rewrite page="/administration/merge_search.do?useSessionForm=true"/>";
		document.forms.mergeSearchForm.submit();
	}

	function changeMergeType() 
	{
		document.forms.mergeSearchForm.action = "<html:rewrite page="/administration/merge_search.do"/>";
		document.forms.mergeSearchForm.submit();
	}

	function submitRequest()
	{
	  var button = document.getElementById('submitButton');
	  button.disabled = true;
	  document.forms.mergeSearchForm.submit();
	}
	
  function resetSearch() {      
    c_goTo("/administration/merge_search.do");
  }
//-->
</script>
