<%--
 * $RCSfile: merge_search_results.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ page import="com.centraview.administration.merge.MergeSearchResultVO" %>
<%@ page import="java.util.ArrayList" %>
<% 
	MergeSearchResultVO mergeSearchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult"); 
	int currentGrouping = 0;
	if (mergeSearchResult.size() == 0) {
		currentGrouping = 0;
	} else {
		currentGrouping = mergeSearchResult.getCursor() + 1;
	}
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.step"/> 2:<bean:message key="label.pages.administration.merge.selectrecords"/> </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <bean:message key="label.pages.administration.merge.selecttwoormorerecords"/>.
    </td>
  </tr>
</table>
<%-- If the display list is empty, print out a helpful message --%>
<c:if test="${empty requestScope.currentGroup}">
<table>
  <tr>
    <td valign="top" class="popupTableText">
      <b><bean:message key="label.pages.administration.merge.nomatchingrecords"/>.</b> <bean:message key="label.pages.administration.merge.adjustcriteria"/>.
    </td>
  </tr>
</table>
</c:if>
<c:if test="${not empty requestScope.currentGroup}">
<form name="listrenderer" id="listrenderer" method="post">
<table border="0" cellpadding="2" cellspacing="0" width="100%">
  <tr>
    <td class="pagingBarContainer" colspan="3">
      <input type="button" name="viewButton" id="viewButton" value="<bean:message key='label.value.merge'/>" class="normalButton" onclick="viewList();" />
    </td>
  </tr>
  <tr>
    <td class="listHeader">&nbsp;</td>
    <c:if test="${requestScope.mergeType == 1}">
      <td class="listHeader"><bean:message key="label.pages.administration.merge.entityname"/>:</td>
      <td class="listHeader"><bean:message key="label.pages.administration.merge.ownername"/>:</td>
    </c:if>
    <c:if test="${requestScope.mergeType == 2}">
      <td class="listHeader"><bean:message key="label.pages.administration.merge.name"/>:</td>
      <td class="listHeader"><bean:message key="label.pages.administration.merge.entityname"/>:</td>
    </c:if>
  </tr>
  <c:set var="count" value="0" />
  <c:forEach var="record" items="${requestScope.currentGroup}">
  <c:set var="css" value="tableRowOdd" />
  <c:set var="flag" value="false" />
  <c:if test="${count % 2 != 0}">
    <c:set var="css" value="tableRowEven" />
    <c:set var="flag" value="true" />
  </c:if>
  <c:set var="count" value="${count + 1}" />
  <tr>
    <td class="<c:out value="${css}"/>"><input type="checkbox" name="rowId" value="<c:out value="${record.id}" />" onclick="vl_selectRow(this, <c:out value="${flag}"/>);"/></td>
    <c:if test="${requestScope.mergeType == 1}">
    <td class="<c:out value="${css}"/>">
      <a class="plainLink" onclick="c_openPopup('/contacts/view_entity.do?rowId=<c:out value="${record.id}" />');"><c:out value="${record.title}" /></a>
    </td>
    <td class="<c:out value="${css}"/>"><c:out value="${record.owner}" /></td>
    </c:if>
    <c:if test="${requestScope.mergeType == 2}">
    <td class="<c:out value="${css}"/>">
      <a class="plainLink" onclick="c_openPopup('/contacts/view_individual.do?rowId=<c:out value="${record.id}" />');"><c:out value="${record.title}" /></a>
    </td>
    <td class="<c:out value="${css}"/>">
      <a class="plainLink" onclick="c_openPopup('/contacts/view_entity.do?rowId=<c:out value="${record.entityID}" />');"><c:out value="${record.entityName}" /></a>
    </td>
    </c:if>
  </tr>
  </c:forEach>
  <tr>
    <td class="pagingBarContainer" colspan="3">
      <input type="button" name="mergeButton" value="<bean:message key='label.value.merge'/>" class="normalButton" onclick="viewList();" />
    </td>
  </tr>
</table>
</form>
<p/>
</c:if>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.mergewizardstatus"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.merge.totalrecords"/>:</td>
    <td class="contentCell"><%= mergeSearchResult.getTotalRecords() %></td>
  </tr>
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.merge.currentgrouping"/>:</td>
    <td class="contentCell"><%= currentGrouping %>&nbsp;of:&nbsp;<%= mergeSearchResult.size() %></td>
  </tr>
</table>
<html:form action="/administration/merge_detail">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input type="button" value="&laquo; <bean:message key='label.value.back'/> " onClick="goToSearch();" class="normalButton" />
      <%-- Only display the Next button if there are records to merge --%>
      <c:if test="${!empty requestScope.currentGroup}">
        <input name="skip" type="button" class="normalButton" onClick="skipGroup();" value="<bean:message key='label.value.skip'/>" id="skip" title="Skip this group" />
        <input type="button" name="next" id="next" value="<bean:message key='label.value.next'/> &raquo;" onclick="viewList();" class="normalButton" />
      </c:if>
    </td>
  </tr>
</table>
</html:form>	   				
<script language="JavaScript" type="text/javascript">
<!--
	<%-- the merge button on display list will call viewList in the common jsp file --%>
	function viewList()
	{
		var mergeIds = "";
		var selectedRecords = 0;
		var flag = 0;
    var listrenderer = document.getElementById('listrenderer');
		for (var i = 0; i < listrenderer.rowId.length; i++) {
      if (listrenderer.rowId[i].checked == true) {
        if (flag == 1) {
          mergeIds = mergeIds + ",";
        }
        mergeIds = mergeIds + listrenderer.rowId[i].value;
        selectedRecords++;
        flag = 1;
      }
    }
    if (selectedRecords < 2) {
      alert("<bean:message key='label.alert.selectrecordstomerge'/>");
      return false;
    } else {
      document.getElementById('next').disabled = true;
			document.getElementById('viewButton').disabled = true;
			c_goTo('/administration/merge_detail.do?mergeIds='+mergeIds);
			return true;
		}
		return false;
	}
	
	function skipGroup()
	{
      c_goTo("/administration/display_merge_search_results.do?next=true");
	}
	function goToSearch()
	{
      c_goTo("/administration/merge_search.do?useSessionForm=true");
	}
//-->
</script>