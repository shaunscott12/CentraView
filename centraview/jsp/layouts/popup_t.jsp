<%--
 * $RCSfile: popup_t.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
  <title><c:out value="${requestScope.dynamicTitle}" default="Default Title" /></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
  <tiles:useAttribute name="js_includes" />
  <c:forEach var="file" items="${pageScope.js_includes}">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/"/><c:out value="${file}" />"></script>
  </c:forEach>
</head>
<body onload="c_setFocus('<tiles:getAsString name="focus_element"/>');">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr valign="top">
    <td class="mainHeader">
      <c:choose>
        <c:when test="${not empty requestScope.lookupType}">
          <span class="headerHighlight"><bean:message key="label.layouts.lookup"/>:</span> <c:out value="${requestScope.lookupType}"/>
        </c:when>
        <c:otherwise>
          <tiles:getAsString name="page_header"/>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</table>
<tiles:insert attribute="top_bar"/>
<html:errors/>
<tiles:insert attribute="main_content"/>
<tiles:insert attribute="bottom_bar"/>
</body>
</html:html>