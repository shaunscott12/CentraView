<%--
 * $RCSfile: folder_lookup.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:57 $ - $Author: mcallist $
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
<%
String curFolderID = request.getParameter("curFolderID");
String tempcurFolderID ="";
if(curFolderID != null){
	tempcurFolderID="&curFolderID="+curFolderID;
}
%>
<script language="Javascript">
<!--
  function selectFolder()
  {
    var value = c_getSelectedRadioValue(document.forms[0].myFolder);
    window.opener.changeParentFolder(value);
    window.close();
  }
-->
</script>
<html:form action="/folder_lookup">
<table border="0" cellspacing="0" cellpadding="0" class="formTable">
  <tr valign="top">
    <td width="11">&nbsp;</td>
    <td>
      <strong><bean:message key="label.common.currentparentfolder"/>:</strong>
      <c:forEach var="folder" items="${folderLookupForm.map.fullPathList}">
        /
        <a href="<%=request.getContextPath()%>/folder_lookup.do?actionType=<bean:write name="folderLookupForm" property="actionType" />&folderID=<c:out value="${folder.folderID}" /><%=tempcurFolderID%>" class="plainLink">
          <c:out value="${folder.name}" />
        </a>
      </c:forEach>
    </td>
    <td  width="11">
      &nbsp;
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="formTable">
  <tr>
    <td class="pagingBarContainer" colspan="2">
      <html:button property="selectButton" styleClass="normalButton" onclick="selectFolder();"> <bean:message key="label.value.select"/></html:button>
    </td>
  </tr>
  <tr>
    <td class="listHeader" width="14">&nbsp;</td>
    <td class="listHeader"><bean:message key="label.common.foldername"/></td>
  </tr>
  <tr>
    <td class="tableRowOdd"><input type="radio" name="myFolder" value="<bean:write name="folderLookupForm" property="folderID" />" /></td>
    <td class="tableRowOdd"><bean:message key="label.common.selectcurrentfolder"/></td>
  </tr>
  <c:set var="count" value="0" />
  <c:set var="classTD" value="tableRowEven" />
  <c:forEach var="folder" items="${folderLookupForm.map.folderList}">
    <c:choose>
      <c:when test="${folder.folderID == folderLookupForm.map.folderID && folder.parentID != 0}">
        <c:choose>
          <c:when test="${count % 2 == 0}">
            <c:set var="classTD" value="tableRowEven" />
          </c:when>
          <c:when test="${count % 2 != 0}">
            <c:set var="classTD" value="tableRowOdd" />
          </c:when>
        </c:choose>
        <tr>
          <td class="<c:out value='${classTD}'/>">
            <html:img page="/images/spacer.gif" width="1" height="18" />
          </td>
          <td class="<c:out value='${classTD}'/>">
            <a href="<%=request.getContextPath()%>/folder_lookup.do?actionType=<bean:write name="folderLookupForm" property="actionType" />&folderID=<c:out value="${folder.parentID}" /><%=tempcurFolderID%>" class="plainLink">
              .. <bean:message key="label.common.gouponelevel"/>
            </a>
            </td>
        </tr>
      </c:when>
      <c:when test="${folder.folderID != folderLookupForm.map.parentID && folder.parentID != 0}">
        <c:choose>
          <c:when test="${count % 2 == 0}">
            <c:set var="classTD" value="tableRowEven" />
          </c:when>
          <c:when test="${count % 2 != 0}">
            <c:set var="classTD" value="tableRowOdd" />
          </c:when>
        </c:choose>
        <tr>
            <td class="<c:out value='${classTD}'/>">
              <input type="radio" name="myFolder" value="<c:out value="${folder.folderID}" />" />
            </td>
            <td class="<c:out value='${classTD}'/>">
              <a href="<%=request.getContextPath()%>/folder_lookup.do?actionType=<bean:write name="folderLookupForm" property="actionType" />&folderID=<c:out value="${folder.folderID}" /><%=tempcurFolderID%>" class="plainLink"><c:out value="${folder.folderName}" />
              </a>
            </td>
        </tr>
      </c:when>
    </c:choose>
  <c:set var="count" value="${count + 1}" />
  </c:forEach>
  <tr>
    <td class="pagingBarContainer" colspan="2">
      <html:button property="selectButton" styleClass="normalButton" onclick="selectFolder();"> <bean:message key="label.value.select"/></html:button>
      <html:hidden property="actionType" />
      <input type="hidden" name="curFolderID" value="<%=curFolderID%>" />
    </td>
  </tr>
</table>

</html:form>