<%--
 * $RCSfile: edit_adhoc_report.jsp,v $ $Revision: 1.4 $ $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import = "com.centraview.report.valueobject.*,
                   java.util.ArrayList "%>

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
<%
    int i = 0;
    int j = 0;
    ReportVO report = (ReportVO)request.getAttribute("pagedata");
    SelectVO selects  = report.getSelect();
    ArrayList topTables = selects.getTopTables();
    ArrayList selectedFields = report.getSelectedFields();
    ArrayList searches = report.getSearchFields();

    ReportContentVO fieldc = null;
    int n = selectedFields.size();
    int[] fieldTableIds = new int[n];
    int[] fieldIds = new int[n];
    String[] fieldNames = new String[n];
    byte[] sortIds = new byte[n];
    String[] sortOrder = new String[n];

    for ( i = 0; i < n; i++) {
        fieldc = (ReportContentVO)selectedFields.get(i);
        fieldTableIds[i] = fieldc.getTableId();
        fieldIds[i] = fieldc.getFieldId();
        fieldNames[i] = fieldc.getName();
        if (fieldc.getSortSeq() != null && fieldc.getSortSeq().byteValue() > 0) {
            sortIds[i] = fieldc.getSortSeq().byteValue();
            if (fieldc.getSortOrder().equals(ReportConstants.SORTORDER_DESC))
                    sortOrder[i] = "D";
            else
                    sortOrder[i] = "A";
        }
    }

    int noOfSearches = (searches==null)? 0:searches.size();

    if (noOfSearches == 0 ) {
            noOfSearches = 1; // for debug
            TheSearchItem test = new TheSearchItem();
            test.setAndOr("Or");
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


<%@include file="/jsp/pages/reports/report_search_header.inc"%>

<script>
</script>

<!-- For Error Window-->
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr valign="top">
<td colspan=2 bgcolor="#345788"><html:errors/></td>
</tr>
</table>
<!-- For Error Window ends here-->

<html:form action="/reports/adhoc_report.do" styleId="reportForm">
<html:hidden property="moduleId"></html:hidden>
<html:hidden property="reportId"></html:hidden>
<html:hidden property="contentFields"></html:hidden>
<html:hidden property="contentOrders"></html:hidden>
<html:hidden property="contentFieldNames"></html:hidden>
<html:hidden property="searchFields"></html:hidden>
<html:hidden property="copy"></html:hidden>

<table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
  <tr valign="top">
    <td>
      <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td valign="top">
            <table border="0" cellspacing="0" cellpadding="0">
              <tr height="10" valign="top">
                <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
                </tr>
              <tr>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
                  <tr height="17">
                    <td colspan="2" class="sectionHeader"><bean:message key="label.reports.editadhoc"/></td>
                  </tr>
                </table></td>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
              </tr>
              <tr>
                <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td>
              </tr>
              <tr>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                <td valign="top">
          <table width="100%" border="0" cellspacing="0" cellpadding="2">
                    <tr>
                      <td width="17%" class="labelCell">
                      <bean:message key="label.reports.ID"/>:</td>
                      <td width="83%" class="contentCell">
                      <bean:write name="adhocreportform" property="reportId"/></td>
                    </tr>
                    <tr>
                      <td width="17%" class="labelCell">
                      <bean:message key="label.reports.name"/>:</td>
                      <td width="83%" class="contentCell">
                 <html:text property="name" styleClass="inputBox" />
                    </tr>
                    <tr>
                      <td valign="top" class="labelCell">
                      <bean:message key="label.reports.description"/>:</td>
                      <td valign="top" class="contentCell">
                      <html:textarea property="description" styleClass="inputBox" cols="39" rows="4"/>
                    </tr>
                  </table></td>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
              </tr>
              <tr height="7">
                <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td>
              </tr>
              <tr>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                <td class="activityDivider"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
              </tr>
              <tr height="7">
                <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td>
              </tr>
        <tr>
          <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
          <td><table border="0" cellspacing="0" cellpadding="2">
                  <tr>
                    <td class="popupTableText">
                    <select name="topTables" class="inputBox" onChange='setFields();'>
                      <option value="0" selected>-- <bean:message key="label.reports.selecttable"/> --
                      <%
                      int size = topTables.size();
                      for (i = 0; i < size; i++)
                      {
                          TheItem table = (TheItem)topTables.get(i);
                      %>
                      <option value="<%=table.getId()%>"><%=table.getFullName()%>
                      <%
                      }
                      %>
                      </select></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td>&nbsp;</td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="labelCell"><strong><bean:message key="label.reports.available"/>:</strong></td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td>&nbsp;</td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td><strong><bean:message key="label.reports.selected"/>:</strong></td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td>&nbsp;</td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td><strong><bean:message key="label.reports.sortorder"/>:</strong></td>
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
                    <td class="labelCell">
                      <input name="mover1" type="button" class="normalButton" value="&nbsp;&raquo;&nbsp;" onClick="javascript:moveField();">
                      <br><br>
                      <input name="movel1" type="button" class="normalButton" value="&nbsp;&laquo;&nbsp;" onClick="javascript:removeField();">
                    </td>
                    <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                    <td class="contentCell">
                          <select name="selectedTopFields" size="10" class="inputBox" style="width:15em;">
                      <%
                      for (i = 0; i < fieldIds.length; i++)
                      {
                      %>
                      <option value="<%=fieldTableIds[i]%>:<%=fieldIds[i]%>"><%=fieldNames[i]%>
                      <%
                      }
                      %>
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
                      <select name="sortOrder" size="6" class="inputBox" style="width:15em;" onChange="showRadio();">
                      <%
                      byte  max = Byte.MAX_VALUE;
                      byte  min = 0;
                      int curi = 0;
                      for (i = 0; i < sortIds.length; i++)
                      {
                              max = Byte.MAX_VALUE;
                              if (sortIds[i] <= 0) continue;
                              for (j = 0; j < sortIds.length; j++)
                              {
                                   if (sortIds[j] != 0) {
                                       if ( sortIds[j] > min && sortIds[j] < max ) {
                                           max = sortIds[j];
                                           curi = j;
                                       }
                                   }
                              }
                              min = max;
                      %>
                      <option value="<%=sortOrder[curi]%><%=fieldTableIds[i]%>:<%=fieldIds[curi]%>"><%=fieldNames[curi]%>
                      <%
                      }
                      %>
                      </select><br><br>
                      <input name="radiobutton" type="radio" value="radiobutton" checked onClick="setRadio();">
<bean:message key="label.reports.ascending"/><br>
                      <input type="radio" name="radiobutton" value="radiobutton" onClick="setRadio();">
<bean:message key="label.reports.descending"/> </td>
                    <td>&nbsp;</td>
                    <td class="labelCell">
                      <input name="moveUp2" type="button" class="normalButton" value="&nbsp;<bean:message key='label.value.up'/>&nbsp;" style="width:4em;"  onClick="moveUp('sortOrder');">
                      <br>
                      <br>
                      <input name="moveDown2" type="button" class="normalButton" value="<bean:message key='label.value.down'/>" style="width:4em;"  onClick="moveDown('sortOrder');">
                    </td>
                  </tr>
                </table></td>
          <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
        </tr>
              <tr height="7">
                <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td>
              </tr>
              <tr>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
              </tr>
              <tr height="7">
                <td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td>
              </tr>
<!-- search -->
  <html:hidden property="searchId" />
  <html:hidden property="ownerId" />
  <html:hidden property="addRow" value="false"/>
  <html:hidden property="removeRow" value="false"/>
  <html:hidden property="saveSearch" value="false"/>
  <html:hidden property="applySearch" value="false"/>
  <html:hidden property="createNew" value="false"/>
  <html:hidden property="deleteSearch" value="false"/>
    <html:hidden property="showFields" value="false"/>    <tr><td colspan="3"><html:errors/></td></tr>
    <tr>
      <td colspan="3"><html:img page="/images/spacer.gif" height="11" width="11" /></td>
    </tr>
    <tr>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
      <td class="sectionHeader"><bean:message key="label.reports.searchbuilder"/></td>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
    </tr>
    <tr>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
      <!-- begin search builder -->
      <td width="100%">
        <table border="0" cellspacing="0" cellpadding="0" width="100%" id="search">
          <tr>
            <td class="listHeader"><html:img page="/images/spacer.gif" width="1" height="1" /><bean:message key="label.reports.andor"/></td>
            <td class="listHeader"><bean:message key="label.reports.recordtype"/></td>
            <td class="listHeader"><bean:message key="label.reports.field"/></td>
            <td class="listHeader"><bean:message key="label.reports.condition"/></td>
            <td class="listHeader"><bean:message key="label.reports.criteria"/></td>
            <td class="listHeader"><bean:message key="label.reports.logicalgrouping"/></td>
            <td class="listHeader">&nbsp;</td>
          </tr>
          <c:set var="counter" value="${0}" />
          <c:forEach var="searchCriteria" items="${adhocreportform.map.searchCriteria}">
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
                           <html:select indexed="true" name="searchCriteria" property="tableID" styleClass="inputBox" onchange="changeTable();">                  <html:optionsCollection property="tableList" value="value" label="label"/>
                </html:select>
              </td>
              <td class="<c:out value="${tdClass}"/>">
                           <%-- Based on the tableID value get the fieldList from the allFields HashMap --%>
                           <%-- the +0 below is a hack, that seems to make the EL parse the searchCriteria.tableID instead of
                              Trying to use it as the key.  Maybe a bug in the Jakarta EL but I couldn't get it to work
                              any other way --%>
                <c:set var="fieldList" value="${adhocreportform.map.allFields[searchCriteria.tableID+0]}" />
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
                <html:text indexed="true" name="searchCriteria" property="value" styleClass="TextInputWhiteBlueBorder" />
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
          <tr class="contactTableRowOdd" style="line-height:30px;" height="30">
            <td class="svdSrchTableOddBegin">&nbsp;</td>
            <td class="<c:out value="${tdClass}"/>">
              <input type=button class="normalButton" name ="addRowButton" value="<bean:message key='label.value.addrow'/>" onclick="newRow();">
            </td>
            <td class="tableRowOdd">&nbsp;</td>
            <td class="tableRowOdd">&nbsp;</td>
            <td class="tableRowOdd">&nbsp;</td>
            <td class="tableRowOdd">&nbsp;</td>
            <td class="tableRowOdd">&nbsp;</td>
          </tr>
          <!-- End Add Row -->
        </table>
      </td>
      <!-- end search builder -->
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
    </tr>
    <tr><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="11" /></td></tr>
    <tr>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
      <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
    </tr>
    <tr><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
    <tr>
      <td colspan="3" height="100%"><html:img page="/images/spacer.gif" height="11" width="11" /></td>
    </tr>
              <tr>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                <td align="right">
                  <table border="0" cellspacing="0" cellpadding="0">
                    <tr>
              <td>
               <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddReport('save');">
            <bean:message key="label.save"/>
             </app:cvbutton>
                      </td>
              <td>
                      <td width="13"><html:img page="/images/spacer.gif" width="13" height="1" /></td>
                   <td><app:cvbutton property="saveandclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddReport('close');">
            <bean:message key="label.saveandclose"/>
             </app:cvbutton>
                      </td>
                      <td width="13"><html:img page="/images/spacer.gif" width="13" height="1" /></td>
                <td><app:cvbutton property="run" styleClass="normalButton"  style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddReport('run');">
            <bean:message key="label.reports.saveandrun"/> &raquo;
             </app:cvbutton>
                      </td>
                      <td width="1"><html:img page="/images/popup_vert_divider.gif" height="17" width="1" /></td>
                      <td width="13"><html:img page="/images/spacer.gif" width="13" height="1" /></td>
                 <td><app:cvbutton property="cancel" styleClass="normalButton"  style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank"
                            onclick="closeOrCancel();">
                   <bean:message key="label.cancel"/>
            </app:cvbutton>
                      </td>
                    </tr>
                  </table>
                </td>
                <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
              </tr>
              <tr height="100%">
                <td colspan="3" height="100%"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form>
<script language="javascript">
<!--
  function changeTable()

  {
    document.forms.adhocreportform.showFields.value = "true";
    submitForm()
  }

  function newRow()
  {
    document.forms.adhocreportform.addRow.value = "true";
    submitForm();
  }

  function discardRow(rowIndex)
  {
    var totalRows = <c:out value="${counter}"/>;
    if (totalRows < 2) {
      return false;
    }
    document.forms.adhocreportform.removeRow.value = rowIndex;
    submitForm();
  }

  function saveSearchAction()
  {
    document.forms.adhocreportform.saveSearch.value = "true";
    submitForm();
  }

  function applySearchAction()
  {
    document.forms.adhocreportform.applySearch.value = "true";
    saveSearchAction();
  }

  function createNewAction()
  {
    document.forms.adhocreportform.createNew.value = "true";
    submitForm();
  }

  function deleteAction()
  {
    document.forms.adhocreportform.deleteSearch.value = "true";
    submitForm();
  }

  function saveContent()
  {
    var selectedTopFields  = eval("document.forms[1].selectedTopFields");
    var sortOrder = eval("document.forms[1].sortOrder");
    var contentFieldsStr="";
    var contentFieldNamesStr="";
    var contentOrderStr="";
    for ( i = 0; i < selectedTopFields.length; i++) {
      contentFieldsStr += selectedTopFields.options[i].value + ";";
      contentFieldNamesStr += selectedTopFields.options[i].text + ";";
    }
    document.forms[1].contentFields.value = contentFieldsStr;
    document.forms[1].contentFieldNames.value = contentFieldNamesStr;
    for ( i = 0; i < sortOrder.length; i++) {
      contentOrderStr += sortOrder.options[i].value + ";";
    }
    document.forms[1].contentOrders.value = contentOrderStr;
  }

  function closeOrCancel()
  {
    var reportForm = document.getElementById("reportForm");
    reportForm.action="<html:rewrite page="/reports/adhoc_list.do"/>?moduleId="+reportForm.moduleId.value;
    reportForm.submit();
    return false;
  }

  function checkAddReport(action)
  {
    saveContent();
    var reportForm = document.getElementById("reportForm");
    reportForm.action="<html:rewrite page="/reports/adhoc_report.do"/>?action="+action;
    reportForm.submit();
    return false;
  }
  function submitForm()
  {
    saveContent();
    document.forms.adhocreportform.submit();
  }
//-->
</script>
