<%--
 * $RCSfile: search.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:55 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html:form action="/advancedsearch/dispatch">
  <html:hidden property="searchId" />
  <html:hidden property="moduleId" />
  <html:hidden property="ownerId" />
  <html:hidden property="listScope" />
  <html:hidden property="addRow" value="false"/>
  <html:hidden property="removeRow" value="false"/>
  <html:hidden property="saveSearch" value="false"/>
  <html:hidden property="applySearch" value="false"/>
  <html:hidden property="createNew" value="false"/>
  <html:hidden property="deleteSearch" value="false"/>
  <table border="0" cellspacing="0" cellpadding="0" height="100%" width="100%">
    <tr><td><html:errors/></td></tr>
    <tr>
      <td>
        <table border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="labelCell"><bean:message key="label.advsearch.searchname"/>:</td>
            <td width="3"><html:img page="/images/spacer.gif" width="3" /></td>
            <td class="contentCell">
              <html:text styleClass="inputBox" property="searchName" />
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr><td><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
    <tr>
      <td class="sectionHeader"><bean:message key="label.advsearch.searchbuilder"/></td>
    </tr>
    <tr><td><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
    <tr>
      <!-- begin search builder -->
      <td width="100%">
        <table border="0" cellspacing="0" cellpadding="0" width="100%" id="search">
          <tr>
            <td class="listHeader"><html:img page="/images/spacer.gif" width="1" height="1" /><bean:message key="label.advsearch.andor"/></td>
            <td class="listHeader"><bean:message key="label.advsearch.recordtype"/></td>
            <td class="listHeader"><bean:message key="label.advsearch.field"/></td>
            <td class="listHeader"><bean:message key="label.advsearch.condition"/></td>
            <td class="listHeader"><bean:message key="label.advsearch.criteria"/></td>
            <td class="listHeader"><bean:message key="label.advsearch.logicalgrouping"/></td>
            <td class="listHeader">&nbsp;</td>
          </tr>
          <c:set var="counter" value="${0}" />
          <c:forEach var="searchCriteria" items="${advancedSearchForm.map.searchCriteria}">
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
                  <html:option value="AND"><bean:message key="label.advsearch.and"/></html:option>
                  <html:option value="OR"><bean:message key="label.advsearch.or"/></html:option>
                </html:select>
              </td>
              <td class="<c:out value="${tdClass}"/>">
                           <html:select indexed="true" name="searchCriteria" property="tableID" styleClass="inputBox" onchange="submitForm();">
                  <html:optionsCollection property="tableList" value="value" label="label"/>
                </html:select>
              </td>
              <td class="<c:out value="${tdClass}"/>">
                           <%-- Based on the tableID value get the fieldList from the allFields HashMap --%>
                           <%-- the +0 below is a hack, that seems to make the EL parse the searchCriteria.tableID instead of
                              Trying to use it as the key.  Maybe a bug in the Jakarta EL but I couldn't get it to work
                              any other way --%>
                <c:set var="fieldList" value="${advancedSearchForm.map.allFields[searchCriteria.tableID+0]}" />
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
          <tr class="contactTableRowOdd" style="line-height:30px;" height="30">
            <td class="svdSrchTableOddBegin">&nbsp;</td>
            <td class="tableRowOdd">
              <input type=button class="normalButton" name="addRowButton" value="<bean:message key='label.value.addrow'/>" onclick="newRow();">
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
    </tr>
    <tr><html:img page="/images/spacer.gif" width="1" height="11" /></td></tr>
    <tr class="popupTableRow"><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
    <!-- button row -->
    <tr>
          <td class="buttonRow">
            <input type=button class="normalButton" name ="saveApply" value="<bean:message key='label.value.saveandapply'/>" onclick="applySearchAction();" />
            <input type=button class="normalButton" value="<bean:message key='label.value.save'/>" onclick="saveSearchAction();" />
            <input type=button class="normalButton" name="delete" value="<bean:message key='label.value.delete'/>" onclick="deleteAction();">
      <%-- Cancel should somehow smartly direct you back to your module based on the right stuff.  For now I am commenting it out --%>
            <%-- <input class="normalButton" type="button" value="<bean:message key='label.value.cancel'/>" /> --%>
            <input class="normalButton" type="button" value="<bean:message key='label.value.createnew'/>" onclick="createNewAction();" />
          </td>
        </tr>
  </table>
</html:form>
<script language="JavaScript" type="text/javascript">
<!--
  function submitForm()
  {
    document.forms.advancedSearchForm.submit();
  }

  function newRow()
  {
    document.forms.advancedSearchForm.addRow.value = "true";
    submitForm();
  }

  function discardRow(rowIndex)
  {
    var totalRows = <c:out value="${counter}"/>;
    if (totalRows < 2)
    {
      return false;
    }
    document.forms.advancedSearchForm.removeRow.value = rowIndex;
    submitForm();
  }

  function saveSearchAction()
  {
    document.forms.advancedSearchForm.saveSearch.value = "true";
    submitForm();
  }

  function applySearchAction()
  {
    document.forms.advancedSearchForm.applySearch.value = "true";
    saveSearchAction();
  }

  function createNewAction()
  {
    document.forms.advancedSearchForm.createNew.value = "true";
    submitForm();
  }

  function deleteAction()
  {
    document.forms.advancedSearchForm.deleteSearch.value = "true";
    submitForm();
  }
//-->
</script>