<%--
 * $RCSfile: footer.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/25 17:18:22 $ - $Author: mcallist $
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
<%@ page import="com.centraview.common.CentraViewConfiguration" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<!-- start footer.jsp -->
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td><html:img page="/images/clear.gif" width="1" height="1" alt="" /></td>
  </tr>
  <tr>
    <td>
      <table border="0" cellspacing="0" cellpadding="0" class="footerTable">
        <tr valign="top">
          <td class="footerLeft">
            <span class="footerText">
              <jsp:useBean id="now" class="java.util.Date" />
              <fmt:formatDate value="${now}" type="both" dateStyle="full" timeStyle="full" />. &nbsp;|&nbsp;IP: <strong><%=(String)request.getRemoteAddr()%></strong>
            </span>
          </td>
          <td class="footerRight">
            <span class="footerText">
              <bean:message key="label.tiles.version"/> <%=CentraViewConfiguration.getVersion()%>
            </span>
          </td>
        </tr>
      </table>
      <div class="footerText" align="center">
       Copyright &copy; 2003 - 2005&nbsp;<html:link href="http://www.centraview.com/" styleClass="plainLink">CentraView, LLC</html:link>&nbsp;<bean:message key="label.tiles.rightsreserved"/><br/>
        <html:link href="http://www.centraview.com/" styleClass="plainLink">CentraView</html:link> <bean:message key="label.tiles.softwarelicense"/></div>
    </td>
  </tr>
</table>
<!-- end footer.jsp -->