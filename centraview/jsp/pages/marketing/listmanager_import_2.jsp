<%--
 * $RCSfile: listmanager_import_2.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script>
  function disableNext(){
    document.importListForm.nextButton.disabled = true;
    document.importListForm.close.disabled = true;
    document.importListForm.back.disabled = true;
    document.importListForm.submit();
  }
</script>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="formTable">
  <html:form action="/marketing/import_members" >
  <tr class="popupTableRow" valign="top">
    <!-- Left content area -->
    <td valign="top">
      <table cellspacing="0" cellpadding="3" width="345">
        <tr>
          <td class="labelCellBold"><bean:message key="label.marketing.fieldname"/></td>
          <td class="labelCellBold"><bean:message key="label.marketing.databasefield"/></td>
          <td class="labelCellBold"><bean:message key="label.marketing.defaultvalue"/></td>
        </tr>
        <tr>
          <td colspan="3" align="center">
            &nbsp;
          </td>
        </tr>
        <c:set var="rowCount" value="0" />
        <c:forEach var="headerOptions" items="${importListForm.headervector}">
          <tr>
            <td class="labelCell"><c:out value='${headerOptions.name}'/> :</td>
            <td class="contentCell">
              <select name="<c:out value='${headerOptions.name}'/><c:out value='${rowCount}'/>" class="inputBox">
                <c:forEach var="importOptions" items="${requestScope.mapImport}">
                  <option name="<c:out value='${importOptions.name}'/>"><c:out value='${importOptions.name}'/></option>
                </c:forEach>
              </select>
            </td>
            <td align="center" valign="top" class="labelCell">
              <input name="<c:out value='${headerOptions.name}'/>DefaultValue<c:out value='${rowCount}'/>" type="text" class="inputBox">
            </td>
          </tr>
          <c:set var="rowCount" value="${rowCount + 1}" />
        </c:forEach>
        <tr>
          <td colspan="3">&nbsp;</td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="sectionHeader"><bean:message key="label.marketing.instructions"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            &nbsp;
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.marketing.importdata"/></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="sectionHeader" colspan="2" align="right">
       <input name="back" type="button" class="normalButton" value="&laquo; <bean:message key='label.marketing.back'/>" onClick="c_goTo('/marketing/import_listmanager_1.do')" />
        &nbsp;
       <input name="nextButton" type="button" class="normalButton" value="<bean:message key='label.marketing.next'/>" &raquo;" onClick="disableNext();">
        &nbsp;
       <input name="close" type="button" class="normalButton" value="<bean:message key='label.marketing.cancel'/>" onClick="window.close();">
    </td>
  </tr>
  </html:form>
</table>