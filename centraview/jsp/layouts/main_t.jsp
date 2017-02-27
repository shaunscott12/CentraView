<%--
 * $RCSfile: main_t.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ page import="com.centraview.mail.BackgroundMailCheck"%>
<!-- begin main_t.jsp -->
<%
	// We need this for the refresh stuff.
	StringBuffer requestURL = request.getRequestURL();
	String requestURI = requestURL.toString();
  boolean showRefresh = false;
  if (requestURI.matches(".*email_list.do.*")) {
   	showRefresh = true;
  }
  String queryParameters = request.getQueryString();
	if (queryParameters != null) {
		requestURL.append("?"+queryParameters);
  }else{
		requestURL.append("?");
  }
%>
<html:html>
<head>
  <title><c:out value="${requestScope.uiAttributes.pageTitle}" /><c:out value="${requestScope.dynamicTitle}" default="" /></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <link rel="shortcut icon" href="<html:rewrite page="/favicon.ico" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
  <tiles:useAttribute name="js_includes" />
  <c:forEach var="file" items="${pageScope.js_includes}">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/"/><c:out value="${file}" />"></script>
  </c:forEach>
  <%
    if (showRefresh) {
      BackgroundMailCheck mailDaemon = (BackgroundMailCheck)session.getAttribute("mailDaemon");
      if (mailDaemon == null || !mailDaemon.isAlive()) {
        String folderName = (String)request.getAttribute("folderName");
        mailDaemon = (BackgroundMailCheck)session.getAttribute(folderName + "MailCheck");
        if (mailDaemon != null && mailDaemon.isAlive()) {
          String checkedParam = (String)request.getParameter("checked");
          String checkedAttrib = (String)request.getAttribute("checked");
          if (checkedParam == null || !checkedParam.equals("true")) {
            if (checkedAttrib != null && checkedAttrib.equals("true")) {
              if (requestURL.indexOf("?") > -1) {
                requestURL.append("&checked=true");
              }else{
                requestURL.append("?checked=true");
              }
            }
          }
          out.println("<META HTTP-EQUIV=\"refresh\" content=\"10;URL=" + requestURL + "\">");
        }
      }else{
        out.println("<META HTTP-EQUIV=\"refresh\" content=\"10;URL=" + requestURL + "\">");
      }
    }
  %>
</head>
<body onload="c_setFocus('<tiles:getAsString name="focus_element"/>');">
<tiles:insert attribute="header" />
<tiles:insert attribute="main_nav" />
<table border="0" cellspacing="0" cellpadding="0" class="mainContentTable">
  <tr valign="top">
    <td class="leftNavContainer">
      <tiles:insert attribute="left_nav" />
    </td>
    <td class="mainContentArea">
      <tiles:insert attribute="search_bar" />
      <html:errors/>
      <tiles:insert attribute="folder_bar" />
      <tiles:insert attribute="main_content" />
    </td>
	</tr>
</table>
<tiles:insert attribute="footer" />
</body>
</html:html>
<!-- end main_t.jsp -->