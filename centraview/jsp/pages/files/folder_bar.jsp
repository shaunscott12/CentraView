<%--
 * $RCSfile: folder_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<script language="javascript">
  function newFolder()
  {
    c_openWindow('/files/folder_new.do?folderId=<c:out value="${requestScope.folderId}"/>', 'NewFolderWindow', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=600, height=500');
  }

  function editFolder()
  {
    c_openWindow('/files/folder_view.do?folderId=<c:out value="${requestScope.folderId}"/>', 'EditFolderWindow', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=600, height=500');
  }
</script>
<table border="0" cellpadding="3" cellspacing="0" class="searchTable">
  <tr>
    <td>
      <html:button property="newfolder" styleClass="normalButton" onclick="newFolder()">
        <bean:message key="label.files.newfolder"/>
      </html:button>
    </td>
    <td>
      <html:button property="editfolder" styleClass="normalButton" onclick="editFolder()">
        <bean:message key="label.files.editfolder"/>
			</html:button>
    </td>
    <td>
      <span class="plainText"><bean:message key="label.files.youarein"/>:
        <c:forEach var="folder" items="${breadCrumbs}">/<a href="<%=request.getContextPath()%>/files/file_list.do?folderId=<c:out value="${folder.id}"/>&TYPEOFFILELIST=<c:out value="${fileTypeRequest}"/>" class="plainLink"><c:out value="${folder.name}" /></a></c:forEach>
      </span>
    </td>
  </tr>
</table>