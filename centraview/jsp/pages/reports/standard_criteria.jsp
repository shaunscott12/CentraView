<%--
 * $RCSfile: standard_criteria.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "com.centraview.report.valueobject.*,java.util.ArrayList "%>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.SearchRecord" %>
<%@ page import="com.centraview.common.SearchCollection" %>
<%@ page import="com.centraview.common.SearchObject" %>
<%@ page import="com.centraview.common.DataDictionary" %>
<%@ page import="com.centraview.common.ListGenerator" %>
<%@ page import="com.centraview.common.DisplayList" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
  int i = 0;
  int j = 0;
  ReportVO report = (ReportVO)request.getAttribute("pagedata");
  SelectVO selects  = report.getSelect();

  ArrayList topTables = (selects == null) ? new ArrayList() : selects.getTopTables();
  ArrayList searches = report.getSearchFields();

  int noOfSearches = (searches == null) ? 0 : searches.size();

  int showTopTables = 0;

  if (noOfSearches == 0) {
    // we need at least one row
    noOfSearches = 1;
    TheSearchItem test = new TheSearchItem();
    test.setAndOr("And");
    test.setConditionId(0);
    test.setCriteriaValue("");
    test.setFieldId(0);
    test.setTableId(0);
    searches.add(test);
  }

  ArrayList joinAll = new ArrayList();

  joinAll.add("And");
  joinAll.add( "Or");

  int noOfJoins = joinAll.size();
%>
<%@ include file="/jsp/pages/reports/report_search_header.inc" %>
<script language="Javascript">
  function closeOrCancel()
  {
    document.forms.reportForm.action = "<html:rewrite page="/reports/standard_report.do"/>?moduleId=" + reportForm.moduleId.value;
    document.forms.reportForm.submit();
    return false;
  }

  function checkAddReport(action)
  {
    document.forms.reportForm.action = "<html:rewrite page="/reports/standard_report.do"/>?action=" + action;
    document.forms.reportForm.submit();
    return false;
  }
</script>
<html:form action="/reports/standard_report.do" styleId="reportForm">
<html:hidden property="moduleId"></html:hidden>
<html:hidden property="reportId"></html:hidden>
<html:hidden property="searchFields"></html:hidden>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.reports.editstandard"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="4" class="formTable">
  <tr>
    <td class="labelCell">ID:</td>
    <td class="contentCell">
      <html:text property="reportId" styleClass="inputBox" disabled="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.reports.name"/>:</td>
    <td class="contentCell">
      <html:text property="name" styleClass="inputBox" disabled="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.reports.description"/>:</td>
    <td class="contentCell">
      <html:textarea property="description" styleClass="inputBox" cols="50" rows="3" disabled="true"/>
    </td>
  </tr>
</table>
<% if (showTopTables > 0) { %>
<table border="0" cellspacing="0" cellpadding="4" width="100%">
  <tr>
    <td class="sectionHeader" colspan="14"><bean:message key="label.reports.selectfields"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="4" class="formTable">
  <tr>
    <td class="contentCell" colspan="14">
      <select name="topTables" class="inputBox" onChange='setFields();'>
        <option value="0" selected>-- <bean:message key="label.reports.selecttable"/> --
        <%
          int size = topTables.size();
          for (i = 0; i < size; i++) {
            TheItem table = (TheItem)topTables.get(i);
            %><option value="<%=table.getId()%>"><%=table.getFullName()%><%
          }
        %>
      </select>
    </td>
  </tr>
  <tr>
    <td><span class="listHeader"><bean:message key="label.reports.availablefields"/>:</span></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td>&nbsp;</td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td><span class="listHeader"><bean:message key="label.reports.selectedfields"/>:</span></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td>&nbsp;</td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td><span class="listHeader"><bean:message key="label.reports.sortorder"/>:</span></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
  </tr>
  <tr>
    <td class="contentCell">
      <select name="topFields" size="10" class="inputBox" style="width:15em;">
        <option>-- <bean:message key="label.reports.selectatable"/> --</option>
      </select>
    </td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td class="contentCell">
      <input name="mover1" type="button" class="normalButton" value="&nbsp;&raquo;&nbsp;" onClick="javascript:moveField();">
      <br><br>
      <input name="movel1" type="button" class="normalButton" value="&nbsp;&laquo;&nbsp;" onClick="javascript:removeField();">
    </td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td class="contentCell">
      <select name="selectedTopFields" size="10" class="inputBox" style="width:15em;">
      </select>
    </td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td class="labelCell">
      <input name="moveUp1" type="button" class="normalButton" value="&nbsp;<bean:message key='label.value.up'/>&nbsp;" style="width:4em;" onClick="moveUp('selectedTopFields');">
      <br>
      <br>
      <input name="moveDown1" type="button" class="normalButton" value="<bean:message key='label.value.down'/>" style="width:4em;" onClick="moveDown('selectedTopFields');">
    </td>
    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
    <td>&nbsp;</td>
    <td class="labelCell">
      <input name="mover2" type="button" class="normalButton" value="&nbsp;&raquo;&nbsp;" onClick="javascript:moveToOrder();">
      <br>
      <br>
      <input name="movel2" type="button" class="normalButton" value="&nbsp;&laquo;&nbsp;"  onClick="javascript:removeFromOrder();">
    </td>
    <td>&nbsp;</td>
    <td valign="top" class="contentCell">
      <select name="sortOrder" size="6" class="inputBox" style="width:15em;">
      </select>
      <br><br>
      <input name="radiobutton" type="radio" value="radiobutton" checked><bean:message key="label.reports.ascending"/><br>
      <input type="radio" name="radiobutton" value="radiobutton"><bean:message key="label.reports.descending"/>
    </td>
    <td>&nbsp;</td>
    <td class="labelCell">
      <input name="moveUp2" type="button" class="normalButton" value="&nbsp;<bean:message key='label.value.up'/>&nbsp;" style="width:4em;"  onClick="moveUp('sortOrder');">
      <br>
      <br>
      <input name="moveDown2" type="button" class="normalButton" value="<bean:message key='label.value.down'/>" style="width:4em;"  onClick="moveDown('sortOrder');">
    </td>
  </tr>
</table>
<% }    // end if (showTopTables > 0)  %>
<!--
<table border="1" cellspacing="0" cellpadding="0">
  <tr>
    <td>
      <table border="1" cellspacing="0" cellpadding="0">
        <tr>
          <td width="37" class="labelCell">From:&nbsp;</td>
          <td width="14">
            <html:text property="startmonth" styleId="startmonth" styleClass="inputBox" size="2" />
          </td>
          <td width="5" class="labelCell">/</td>
          <td width="16" class="contentCell">
            <html:text property="startday" styleId="startday" styleClass="inputBox" size="2" />
          </td>
          <td width="5" class="labelCell">/</td>
          <td width="44" class="contentCell">
            <html:text property="startyear" styleId="startyear" styleClass="inputBox" size="4" />
          </td>
          <td width="4"><html:img page="/images/spacer.gif" height="1" width="4" /></td>
          <td width="17"><html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" onclick="openSelectDateTime();" /></td>
        </tr>
      </table>
    </td>
    <td><html:img page="/images/spacer.gif" height="1" width="30" /></td>
    <td>
      <table border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="37" class="labelCell">To:&nbsp;</td>
          <td width="14" class="contentCell">
            <html:text property="endmonth" styleId="endmonth" styleClass="inputBox" size="2" />
          </td>
          <td width="5" class="labelCell">/</td>
          <td width="16" class="contentCell">
            <html:text property="endday" styleId="endday" styleClass="inputBox" size="2" />
          </td>
          <td width="5" class="labelCell">/</td>
          <td width="44" class="contentCell">
            <html:text property="endyear" styleId="endyear" styleClass="inputBox" size="4" />
          </td>
          <td width="4"><html:img page="/images/spacer.gif" height="1" width="4" /></td>
          <td width="17"><html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" onclick="openSelectDateTime();" /></a></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
-->
<br/><br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.reports.searchbuilder"/></td>
  </tr>
</table>
<br/>
<html:hidden property="searchId" />
<html:hidden property="ownerId" />
<html:hidden property="addRow" value="false"/>
<html:hidden property="removeRow" value="false"/>
<html:hidden property="saveSearch" value="false"/>
<html:hidden property="applySearch" value="false"/>
<html:hidden property="createNew" value="false"/>
<html:hidden property="deleteSearch" value="false"/>
<html:hidden property="showFields" value="false"/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" id="search">
  <tr>
    <td class="listHeader"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
    <td class="listHeader"><bean:message key="label.reports.recordtype"/></td>
    <td class="listHeader"><bean:message key="label.reports.field"/></td>
    <td class="listHeader"><bean:message key="label.reports.condition"/></td>
    <td class="listHeader"><bean:message key="label.reports.criteria"/></td>
    <td class="listHeader"><bean:message key="label.reports.logicalgrouping"/></td>
    <td class="listHeader">&nbsp;</td>
  </tr>
  <c:set var="counter" value="${0}" />
  <c:forEach var="searchCriteria" items="${standardreportform.map.searchCriteria}">
  <!-- Begin Criteria Line -->
  <tr style="line-height:30px;" height="30">
    <c:choose>
      <c:when test="${(counter % 2) == 0}">
        <c:set var="tdClass" value="tableRowOdd"/>
      </c:when>
      <c:otherwise>
        <c:set var="tdClass" value="tableRowEven"/>
      </c:otherwise>
    </c:choose>
    <td class="<c:out value="${tdClass}"/>">
      <html:select indexed="true" name="searchCriteria" property="expressionType" styleClass="inputBox">
        <html:option value="AND"><bean:message key="label.reports.and"/></html:option>
        <html:option value="OR"><bean:message key="label.reports.or"/></html:option>
      </html:select>
    </td>
    <td class="<c:out value="${tdClass}"/>">
      <!-- onchange="submitForm();" -->
      <html:select indexed="true" name="searchCriteria" property="tableID" styleClass="inputBox" onchange="changeTable();">
        <html:optionsCollection property="tableList" value="value" label="label"/>
      </html:select>
    </td>
    <td class="<c:out value="${tdClass}"/>">
      <%-- Based on the tableID value get the fieldList from the allFields HashMap --%>
      <%-- the +0 below is a hack, that seems to make the EL parse the searchCriteria.tableID instead of
           Trying to use it as the key.  Maybe a bug in the Jakarta EL but I couldn't get it to work
           any other way --%>
      <c:set var="fieldList" value="${standardreportform.map.allFields[searchCriteria.tableID+0]}" />
      <html:select indexed="true" name="searchCriteria" property="fieldID" styleClass="inputBox">
        <c:if test="${not empty fieldList}">
          <html:options collection="fieldList" property="value" labelProperty="label"/>
        </c:if>
      </html:select>
    </td>
    <td class="<c:out value="${tdClass}"/>">
      <html:select indexed="true" name="searchCriteria" property="conditionID" styleClass="inputBox">
        <html:optionsCollection property="conditionList" value="value" label="label"/>
      </html:select>
    </td>
    <td class="<c:out value="${tdClass}"/>">
      <html:text indexed="true" name="searchCriteria" property="value" styleClass="inputBox" />
    </td>
    <td class="<c:out value="${tdClass}"/>">
      <html:select indexed="true" name="searchCriteria" property="groupID" styleClass="inputBox">
        <html:option value="1">A</html:option>
        <html:option value="2">B</html:option>
      </html:select>
    </td>
    <td class="<c:out value="${tdClass}"/>" nowrap>
      <html:img page="/images/spacer.gif" height="1" width="2" />
      <input type="button" class="normalButton" name="removeRowButton" value="<bean:message key='label.value.removerow'/>" onclick="discardRow('<c:out value="${counter}" />');" />
      <html:img page="/images/spacer.gif" height="1" width="2" />
    </td>
  </tr>
  <!-- End Criteria Line -->
  <c:set var="counter" value="${counter + 1}" />
  </c:forEach>
  <!-- Add Row Button -->
  <tr style="line-height:30px;" height="30">
    <td class="tableRowOdd">&nbsp;</td>
    <td class="tableRowOdd"><input type=button class="normalButton" name="addRowButton" value="<bean:message key='label.value.addrow'/>" onclick="newRow();"></td>
    <td class="tableRowOdd">&nbsp;</td>
    <td class="tableRowOdd">&nbsp;</td>
    <td class="tableRowOdd">&nbsp;</td>
    <td class="tableRowOdd">&nbsp;</td>
    <td class="tableRowOdd">&nbsp;</td>
  </tr>
  <!-- End Add Row -->
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow" align="right">
      <app:cvbutton property="run" styleClass="normalButton" style="width:8em;" onclick="return checkAddReport('run');">
        <bean:message key="label.reports.runreport"/> &raquo;
      </app:cvbutton>
      &nbsp;
      <input name="button2" type="button" class="normalButton" value="<bean:message key='label.value.cancel'/>" onclick="closeOrCancel();">
    </td>
  </tr>
</table>
</html:form>
<script language="JavaScript" type="text/javascript">
  function submitForm()
  {
    document.forms.standardreportform.submit();
  }

  function changeTable()
  {
    document.forms.standardreportform.showFields.value = "true";
    submitForm()
  }

  function newRow()
  {
    document.forms.standardreportform.addRow.value = "true";
    submitForm();
  }

  function discardRow(rowIndex)
  {
    var totalRows = <c:out value="${counter}"/>;
    if (totalRows < 2) {
      return false;
    }
    document.forms.standardreportform.removeRow.value = rowIndex;
    submitForm();
  }

  function saveSearchAction()
  {
    document.forms.standardreportform.saveSearch.value = "true";
    submitForm();
  }

  function applySearchAction()
  {
    document.forms.standardreportform.applySearch.value = "true";
    saveSearchAction();
  }

  function createNewAction()
  {
    document.forms.standardreportform.createNew.value = "true";
    submitForm();
  }

  function deleteAction()
  {
    document.forms.standardreportform.deleteSearch.value = "true";
    submitForm();
  }
</script>