<%--
/*
 * $RCSfile: header.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
 */
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="com.centraview.common.UserObject" %>
<!-- Start header.jsp -->
<%
  UserObject uo = (UserObject)session.getAttribute("userobject");
  String name ="";
  String loginName ="";
  if (uo != null)
  {
    name = uo.getfirstName() + " " + uo.getlastName();
    loginName = "(" + uo.getLoginName() + ")";
  }
%>
<table border="0" cellspacing="0" cellpadding="0" class="headerTable">
  <tr>
    <td class="headerLeft">
      <html:link action="/home.do"><html:img page="/images/logo_centraview.jpg" width="182" height="65" border="0" alt="Logo" title="CentraView" /></html:link>
    </td>
    <td class="headerRight">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="headerRightTop">
          </td>
        </tr>
        <tr>
          <td class="headerRightBottom">
          <div class="headerTextContainer">
            <div class="headerPoweredBy">
              <span class="headerTextBold"><bean:message key="label.customer.tiles.welcome"/><%=name%> <%=loginName%></span>
            </div>
          </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>