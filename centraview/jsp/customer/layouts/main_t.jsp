<%--
 * $RCSfile: main_t.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/01 17:28:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html:html>
<head>
  <title><c:out value="${requestScope.uiAttributes.pageTitle}" /><c:out value="${requestScope.dynamicTitle}" default="" /></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/customer.css" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
  <tiles:useAttribute name="js_includes" />
  <c:forEach var="file" items="${pageScope.js_includes}">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/"/><c:out value="${file}" />"></script>
  </c:forEach>
</head>
<body>
<tiles:insert attribute="header" />
<tiles:insert attribute="main_nav" />
<table border="0" cellspacing="0" cellpadding="0" class="mainContentTable">
  <tr valign="top">
    <td>
      <tiles:insert attribute="left_nav" />
    </td>
    <td class="mainContentArea">
      <html:errors/>
      <tiles:insert attribute="main_content" />
    </td>
	</tr>
</table>
<tiles:insert attribute="footer" />
</body>
</html:html>