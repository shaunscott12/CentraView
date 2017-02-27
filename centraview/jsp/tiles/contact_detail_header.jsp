<%--
 * $RCSfile: contact_detail_header.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td align="left" valign="top" class="mainHeader">
      <span class="headerHighlight"><c:out value="${requestScope.recordType}" default="requestScope.recordType!"/>: </span><c:out value="${requestScope.recordName}" default="requestScope.recordName!"/>
    </td>
    <td align="right" valign="top" class="mainHeader">
      <%-- TODO: probably a Javascript that opens a new window or this fixed size, pointing at a handler that does the logic based on record type and record id --%>
      <input type="button" name="newwindow" value="<bean:message key='label.value.newwindow'/>"  class="normalButton" onclick="detailNewWindow();">
      <input type="button" name="minmaxwin" value="<bean:message key='label.value.minimize'/> -" class="normalButton" onclick="c_hideDetail(this);">
    </td>
  </tr>
</table>