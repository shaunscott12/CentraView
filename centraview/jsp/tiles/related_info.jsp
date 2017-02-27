<%--
 * $RCSfile: related_info.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html>
<head>
  <title><bean:message key="label.tiles.relatedinfo"/></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
  <script language="Javascript" src="<html:rewrite page="/stylesheet/value_list_js.jsp"/>"></script>
  <script language="javascript">
  <!--
  <c:if test="${requestScope.refreshParent == 'true'}">
    window.parent.window.location.reload();
  </c:if>
    var standardParameters = '&listType=<c:out value="${requestScope.listType}"/>&listFor=<c:out value="${requestScope.listFor}"/>&recordID=<c:out value="${requestScope.recordID}"/>&recordName=<c:out value="${requestScope.recordName}"/>';
    function editAddress(addressId)
    {
      c_goTo('/contacts/view_related_address.do?rowId='+addressId+standardParameters);
    }
    function editMOC(mocId)
    {
      c_goTo('/contacts/view_contact_method.do?rowId='+mocId+standardParameters);
    }
    function viewCustomField(fieldId)
    {
      c_goTo('/contacts/view_custom_field.do?rowId=' + fieldId + standardParameters);
    }
  //-->
  </script>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<html:form method="GET" action="/relatedinfo/list.do" styleId="relatedForm">
  <tr>
    <td class="mainHeader" width="35%">
      <span class="headerHighlight"><bean:message key="label.tiles.relatedinfo"/>:</span> <c:out value="${requestScope.recordName}" escapeXml="false" />
    </td>
    <td class="mainHeader">
      <span class="relatedInfoText">
        <html:select property="riListType" styleClass="inputBox" onchange="javascript:document.getElementById('relatedForm').submit();">
          <html:optionsCollection property="dropdownCollection" value="value" label="label"/>
        </html:select>
        &laquo; <bean:message key="label.tiles.selectview"/>
      </span>
    </td>
  </tr>
</html:form>
</table>
<app:valuelist listObjectName="valueList" />
</body>
</html>