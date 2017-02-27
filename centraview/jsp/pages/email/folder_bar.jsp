<%--
 * $RCSfile: folder_bar.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/28 17:23:01 $ - $Author: mcallist $
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
<%@ page import="com.centraview.mail.BackgroundMailCheck"%>
<script language="javascript">
  function createFolder()
  {
    c_openWindow('/email/new_folder.do?parentID=<bean:write name="emailListForm" property="folderID" ignore="true" />&accountID=<bean:write name="emailListForm" property="accountID" ignore="true" />', 'NewFolderWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=520,height=200');
  }

  function editFolder()
  {
    c_openWindow('/email/view_folder.do?folderID=<bean:write name="emailListForm" property="folderID" ignore="true" />', 'EditFolderWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=520,height=200');
  }

  function checkEmail()
  {
    document.listrenderer.action = "<%=request.getContextPath()%>/email/check_mail.do?folderID=<bean:write name="emailListForm" property="folderID" ignore="true" />";
    document.listrenderer.submit();
  }
</script>
<table border="0" cellpadding="3" cellspacing="0" class="searchTable">
  <tr>
    <td>
      <html:button property="createnewfolder" styleClass="normalButton" onclick="createFolder();">
        <bean:message key="label.email.newfolder"/>
      </html:button>
    </td>
    <c:if test="${emailListForm.map.folderType != 'SYSTEM'}">
    <td>
      <html:button property="editfolder" value="<bean:message key='label.value.editfolder'/>" styleClass="normalButton" onclick="editFolder();" />
    </td>
    </c:if>
    <td>
    <%
      BackgroundMailCheck mailDaemon = (BackgroundMailCheck)session.getAttribute("mailDaemon");
      if (mailDaemon == null || !mailDaemon.isAlive()) {
        String folderName = (String)request.getAttribute("folderName");
        mailDaemon = (BackgroundMailCheck)session.getAttribute(folderName + "MailCheck");
        if (mailDaemon != null && mailDaemon.isAlive()) {
          %><span style="font-family: verdana,arial,sans-serif;font-weight:bold;font-size:65%;color:#FF0000;"><bean:message key='label.value.checkingemail'/> ...</span><%
        }else{
          %><input type="button" name="createnewfolder" value="<bean:message key='label.value.checkemail'/>" class="normalButton" onclick="checkEmail();"><%
        }
      }else{
        %><span style="font-family: verdana,arial,sans-serif;font-weight:bold;font-size:65%;color:#FF0000;"><bean:message key='label.value.checkingemail'/> ...</span><%
      }
    %>
    </td>
    <td>
      <span class="plainText"><bean:message key="label.email.youarein"/>:
        <c:forEach var="folder" items="${emailListForm.map.folderPathList}"><c:if test="${folder.name != 'root'}">/<a href="<%=request.getContextPath()%>/email/email_list.do?folderID=<c:out value="${folder.folderID}"/>&clicked=true" class="plainLink"><c:out value="${folder.name}" /></a></c:if></c:forEach>
      </span>
    </td>
  </tr>
</table>