<%--
 * $RCSfile: search_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
<html:form action="/customer/Search.do">
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
    <td><input type="button" value="<bean:message key='label.value.newticket'/>" onClick="goTo('NewTicket.do');" class="searchBarButton" /></td>
    <td width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
    <td><html:text property="searchTerms" styleClass="searchBarTextBox" size="20" /></td>
    <td width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
    <td><html:submit value="<bean:message key='label.value.search'/>" styleClass="searchBarButton" /></td>
    <td width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
    <td><html:img page="/images/separator_white_back.gif" width="1" height="17" /></td>
    <td width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
    <td><html:link href="#" onclick="print();"><html:img page="/images/button_print.gif" width="19" height="17" /></html:link></td>
  </tr>
</table>
</html:form>