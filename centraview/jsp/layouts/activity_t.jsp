<%--
 * $RCSfile: activity_t.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:28 $ - $Author: mking_cv $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<!-- begin activity_t.jsp -->
<html:html>
<head>
  <title><c:out value="${requestScope.uiAttributes.pageTitle}" /><c:out value="${requestScope.dynamicTitle}" default="" /></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
  <tiles:useAttribute name="js_includes" />
  <c:forEach var="file" items="${pageScope.js_includes}">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/"/><c:out value="${file}" />"></script>
  </c:forEach>
  <% if (request.getAttribute("refreshWindow") != null && request.getAttribute("refreshWindow").equals("true")){ %>
  <script language="javascript">window.opener.location.reload();</script>
  <% } %>
  <% if (request.getAttribute("closeWindow") != null && request.getAttribute("closeWindow").equals("true")){ %>
  <script language="javascript">window.close();</script>
  <% }%>
</head>
<body onload="c_setFocus('<tiles:getAsString name="focus_element"/>');">
<html:form action="/activities/save_new_activity.do" enctype="multipart/form-data">
<tiles:insert attribute="header"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><tiles:getAsString name="subtitle"/></td>
  </tr>
</table>
<tiles:insert attribute="main_content"/>
<tiles:insert attribute="footer"/>
</html:form>
</body>
</html:html>
<!-- end activtiy_t.jsp -->
