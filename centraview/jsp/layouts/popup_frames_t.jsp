<%--
 * $RCSfile: popup_frames_t.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:28 $ - $Author: mking_cv $
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
<html:html>
<head>
  <title><c:out value="${requestScope.uiAttributes.pageTitle}" /><c:out value="${requestScope.dynamicTitle}" default="" /></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
  <tiles:useAttribute name="js_includes" />
  <c:forEach var="file" items="${pageScope.js_includes}">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/"/><c:out value="${file}" />"></script>
  </c:forEach>
  <script language="javascript">
  <!--
  <c:if test="${not empty requestScope.closeWindow}">
      window.opener.location.reload();
    <c:if test="${requestScope.closeWindow == 'true'}">
      window.close();
    </c:if>
  </c:if>
  function changeRelatedInfo(listType)
  {
    var relatedInfoPage = '<html:rewrite page="/relatedinfo/list.do"/>?riListType=';
    var relatedInfo = document.getElementById('bottomFrame');
    relatedInfo.src = relatedInfoPage+listType;
  }
  //-->
  </script>
</head>
<body onload="c_setFocus('<tiles:getAsString name="focus_element"/>');">
<tiles:insert attribute="header"/>
<div name="detail" id="detail" style="display: block;">
<table border="0" cellspacing="0" cellpadding="0" width="100%" height="375">
  <tr valign="top"> 
    <td class="detailDarkBorder">
      <tiles:insert attribute="detail" />
    </td>
  </tr>
</table>
</div>
<iframe src="<html:rewrite page="/relatedinfo/list.do"/>?recordId=<c:out value="${requestScope.recordId}" />&recordName=<c:out value="${requestScope.recordName}" />&listFor=<c:out value="${requestScope.recordType}"/>&parentId=<c:out value="${requestScope.parentId}" />&parentName=<c:out value="${requestScope.parentName}" />" width="780" height="180" frameborder="0" name="bottomFrame" id="bottomFrame"></iframe>
</body>
</html:html>
