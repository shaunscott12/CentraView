<%--
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
</head>
<body marginheight="0" marginwidth="0" topmargin="0" bottommargin="0" leftmargin="0" rightmargin="0" bgcolor="#FFFFFF" onLoad="resizeTo(715, 445);">
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
        <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
        <td class="leftNavTitle" align="left"><%=request.getParameter("listType")%> List<img src="/centraview/images/spacer.gif" height="1" width="6"></td>
        <td align="right"><a href="javascript:window.print()"><img src="/centraview/images/button_print.gif" alt="Print Button" border="0" width="19" height="17"></a><img src="/centraview/images/spacer.gif" height="1" width="11"></td>
  	</tr>
  	<tr>
    	<td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
    </tr>
    <tr>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
      <td>
        <app:valuelist listObjectName="valueList"/>
      </td>
      <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
    </tr>
    <tr height="11">
      <td colspan="3"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
    </tr>
  </table>
</body>
</html>