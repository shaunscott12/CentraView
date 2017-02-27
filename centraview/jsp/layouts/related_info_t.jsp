<%--
 * $RCSfile: related_info_t.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<html>
<head>
  <title><bean:message key="label.layouts.relatedinfo"/></title>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css" />">
  <script language="Javascript" src="<html:rewrite page="/stylesheet/common.jsp"/>"></script>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="mainHeader">
      <span class="headerHighlight"><tiles:getAsString name="title"/></span>
    </td>
  </tr>
</table>
<tiles:insert attribute="main_content" />
</body>
</html>