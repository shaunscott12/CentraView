<%--
 * $RCSfile: category_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
  function createCategory()
  {
    c_openWindow('/support/knowledgebase_newcategory.do', 'NewCategoryWindow', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=460, height=160');
  }

  function editCategory()
  {
    c_openWindow('/support/knowledgebase_editcategory.do?rowId=<c:out value="${param.rowId}"/>', 'EditCategoryWindow', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, width=460, height=210');
  }
</script>
<table border="0" cellpadding="3" cellspacing="0" class="searchTable">
  <tr>
    <td>
      <html:button property="newfile" styleClass="normalButton" onclick="createCategory()">
        <bean:message key="label.support.newcategory"/>
      </html:button>
    </td>
    <td>
      <html:button property="newfolder" styleClass="normalButton" onclick="editCategory()">
        <bean:message key="label.support.editcategory"/>
			</html:button>
    </td>
    <td>
      <span class="plainText"><bean:message key="label.support.youarein"/>:
        <c:forEach var="category" items="${breadCrumbs}">/<a href="<%=request.getContextPath()%>/support/knowledgebase_list.do?rowId=<c:out value="${category.id}"/>" class="plainLink"><c:out value="${category.name}" /></a></c:forEach>
      </span>
    </td>
  </tr>
</table>