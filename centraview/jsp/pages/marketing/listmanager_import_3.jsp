<%--
 * $RCSfile: listmanager_import_3.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ page import="java.util.HashMap"%>
<%@ page import="com.centraview.marketing.BackgroundImportMembers"%>
<%
  // Find out where we are, so we can get back there in the
  // event we need to refresh.
  StringBuffer requestURL = request.getRequestURL();
  String requestURI = requestURL.toString();
  boolean showRefresh = false;
  if (requestURI.matches(".*import_listmanager_3.do.*")) {
    // only refresh the screen if it's import_listmanager_3.do
    showRefresh = true;
  }
%>
<%-- The following code forces the import page to refresh every 30 seconds if we are currently importing --%>
<% if (showRefresh) { %>
<c:if test="${not empty sessionScope.backgroundImportMembers and sessionScope.backgroundImportMembers.alive }">
  <META HTTP-EQUIV="refresh" content="10;URL=<%=requestURL%>">
</c:if>
<% } %>
<form>
<table border="0" cellspacing="0" cellpadding="4" width="100%" class="formTable">
  <tr>
    <td width="70%" valign="top" class="contentCell">
    <c:choose>
      <c:when test="${not empty sessionScope.backgroundImportMembers and sessionScope.backgroundImportMembers.alive}">
        <span style="font-weight:bold;font-size:200%;color:#FF0000;">
          <bean:message key="label.marketing.importprocess"/>...
        </span>
        <br/><br/>
        <bean:message key="label.marketing.donotcloseimportwindow"/>.
      </c:when>
      <c:otherwise>
      <%
        BackgroundImportMembers backgroundImportMembers = (BackgroundImportMembers) session.getAttribute("backgroundImportMembers");
        HashMap importMessageMap = backgroundImportMembers.getImportedMessages();

        if (importMessageMap != null && importMessageMap.size() != 0) {
          if (importMessageMap.get("importMessage") != null) {
            %>
            <span style="font-weight:bold;font-size:200%;color:#FF0000;">
              <%=importMessageMap.get("importMessage")%>
            </span>
            <%
          }

          if (importMessageMap.get("fileName") != null && importMessageMap.get("fileID") != null) {
            %>
            <bean:message key="label.marketing.errorlogfile"/>:
            <a href='<html:rewrite page="/files/file_download.do"/>?fileid=<%=importMessageMap.get("fileID")%>' class="plainLink"><%=importMessageMap.get("fileName")%></a>
            <%
          }

          if (importMessageMap.get("lineErrorMessage") != null) {
            %>
            <bean:message key="label.marketing.errorlog"/>:
            <table cellspacing="0" cellpading="0" border="0" width="100%">
              <tr>
                <td class="popupTableText"> <b><bean:message key="label.marketing.linenumber"/>&nbsp;</b></td>
                <td class="popupTableText"> <b><bean:message key="label.marketing.linecontent"/></b> </td>
                <td class="popupTableText"> <b><bean:message key="label.marketing.errormessage"/></b> </td>
              </tr>
            </table>
            <pre>
              <%=importMessageMap.get("lineErrorMessage")%>
            </pre>
            <%
          }
        }
      %>
      </c:otherwise>
    </c:choose>
    </td>
    <td width="30%">
      <table border="0" cellpadding="0" cellspacing="0">
        <tr height="20">
          <td class="sectionHeader">
            <bean:message key="label.marketing.helpfultips"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell">&nbsp;</td>
        </tr>
        <tr>
          <td class="labelCellBold">
            <bean:message key="label.marketing.anyerrors"/>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <bean:message key="label.marketing.errorreportdownload"/>.
          </td>
        </tr>
        <tr>
          <td colspan="3" class="labelCell">
            &nbsp;
          </td>
        </tr>
        <tr>
          <td class="labelCellBold">
            <bean:message key="label.marketing.tofinish"/>:
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <bean:message key="label.marketing.closeafterfinish"/>.
          </td>
        </tr>
      </table>
   </td>
  </tr>
  <tr>
    <td colspan="2" class="sectionHeader" align="right">
       <input name="Close" type="button" class="normalButton" value="<bean:message key='label.marketing.close'/>" onclick="window.opener.location.href='<%=request.getContextPath()%>/marketing/listmanager_list.do';window.close();" />
      &nbsp;
       <input name="Cancel" type="button" class="normalButton" value="<bean:message key='label.marketing.cancel'/>" onclick="window.close();">
    </td>
  </tr>
</table>
</form>
<c:choose>
  <c:when test="${not empty sessionScope.backgroundImportMembers and sessionScope.backgroundImportMembers.alive}">
    <script>
      document.forms[0].Close.disabled = true;
      document.forms[0].Cancel.disabled = true;
    </script>
  </c:when>
  <c:otherwise>
    <script>
      document.forms[0].Close.disabled = false;
      document.forms[0].Cancel.disabled = false;
    </script>
  </c:otherwise>
</c:choose>