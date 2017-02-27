<%--
 * $RCSfile: webform_import_c.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheet/centraview.css">
	<script language="JavaScript1.2" src="<%=request.getContextPath()%>/stylesheet/scripts.js"></script>
</head>
<body marginheight="0" marginwidth="0" topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0" bgcolor="#345788" onLoad="window.resizeTo(780, 580);" id="body">
<%-- In the future, make this action dynamic, based on the importType field --%>
<html:form action="/mail/webimport/SaveContact">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr height="20">
    <td class="tabsOff"><html:img page="/images/spacer.gif" width="1" height="1" /></td>
  </tr>
  <tr height="11">
    <td class="mainTableBG">
      <table border="0" cellspacing="0" cellpadding="0" width="100%" height="100%">
        <tr height="11">
          <td class="mainTableBG" colspan="3"><html:img page="/images/spacer.gif" width="1" height="11" /></td>
        </tr>
        <tr valign="top">
          <td class="mainTableBG" width="10"><html:img page="/images/spacer.gif" width="10" height="1" /></td>
          <td>
            <html:errors />

            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr height="3">
                <td colspan="2" class="popupTableHeadShadow"><html:img page="/images/spacer.gif" width="4" height="3" /></td>
              </tr>
              <tr height="17">
                <td class="popupTableHead" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
                <td class="popupTableHead"><span class="popupTableHeadText">Webform Import</span></td>
              </tr>
            </table>
            <table border="0" cellspacing="0" cellpadding="2" width="100%">
              <tr class="popupTableRow">
                <td>
                  <table border="0" cellpadding="3" cellspacing="0">
                    <c:forEach var="field" items="${webformImportForm.map.importFields}">
                    <tr>
                      <td class="popupTableText" align="right"><c:out value="${field.key}" />:</td>
                      <td class="popupTableText"><input type="text" size="35" name="<c:out value="${field.fieldName}" />" value="<c:out value="${field.value}"/>" class="textInputWhiteBlueBorder"></td>
                    </tr>
                    </c:forEach>
                  </table>
                </td>
              </tr>
              <tr class="popupTableRow">
                <td colspan="2"><html:img page="/images/spacer.gif" width="1" height="8" /></td>
              </tr>
              <tr class="popupTableRow">
                <td colspan="2" align="center">
                  <html:submit property="saveButton" value="<bean:message key='label.value.save'/>" styleClass="popupButton" />
                  &nbsp;
                  <html:button property="cancelButton" value="<bean:message key='label.value.cancel'/>" styleClass="popupButton" onclick="window.close();" />
                </td>
              </tr>
              <tr class="popupTableRow">
                <td colspan="2"><html:img page="/images/spacer.gif" width="1" height="8" /></td>
              </tr>
            </table>
          </td>
          <td class="mainTableBG" width="10"><html:img page="/images/spacer.gif" width="10" height="1" /></td>
        </tr>
        <tr height="11">
          <td class="mainTableBG" colspan="3"><html:img page="/images/spacer.gif" width="1" height="11" /></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form>
</body>
</html>