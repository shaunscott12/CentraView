<%--
 * $RCSfile: alternate_left_nav.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<div class="leftNavContainer">
<p class="leftNav"><c:out value="${requestScope.leftNav.leftHeader}"/></p>
<table border="0" cellspacing="1" cellpadding="0" class="menuContainer">
  <c:forEach var="menuItem" items="${requestScope.leftNav.elements}">
    <tr>
      <td class="menuPrimary"><a href="<html:rewrite page=""/><c:out value="${menuItem.URL}"/>" class="menuPrimary">- <c:out value="${menuItem.title}"/></a></td>
    </tr>
    <c:forEach var="subItem" items="${menuItem.items}">
      <c:set var="secondaryClass" value="menuSecondary"/>
      <c:if test="${subItem.isSelected}">
        <c:set var="secondaryClass" value="menuSecondaryOn"/>
      </c:if>
      <c:set var="secondaryIcon" value="+"/>
      <tr>
        <td class="<c:out value="${secondaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${subItem.URL}" />" class="<c:out value="${secondaryClass}"/>"><c:out value="${secondaryIcon}"/> <c:out value="${subItem.title}"/></a></td>
      </tr>
    </c:forEach>
  </c:forEach>
</table>
<html:img page="/customerlogo" border="0" alt="" />
</div>