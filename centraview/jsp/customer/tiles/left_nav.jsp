<%--
 * $RCSfile: left_nav.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/27 19:23:27 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.common.UIAttributes" %>
<p class="leftNav"><c:out value="${requestScope.uiAttributes.leftNav.leftHeader}"/></p>
<table border="0" cellspacing="1" cellpadding="0" class="menuContainer">
  <c:forEach var="menuItem" items="${requestScope.uiAttributes.leftNav.elements}">
    <c:set var="primaryClass" value="menuPrimary"/>
    <c:if test="${requestScope.uiAttributes.leftSelected[0] == menuItem.id}">
      <c:set var="primaryClass" value="menuPrimaryOn"/>
    </c:if>
    <c:set var="primaryIcon" value="+"/>
    <c:set var="showSecondary" value="false"/>
		<c:forEach var="subItem" items="${menuItem.items}">
			<c:set var="secondaryClass" value="menuSecondary"/>
			<c:if test="${requestScope.uiAttributes.leftSelected[1] == subItem.id}">
				<c:set var="showSecondary" value="true"/>
				<c:set var="primaryIcon" value="-"/>
		    </c:if>
		</c:forEach>

    <tr>
      <td class="<c:out value="${primaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${menuItem.URL}"/>" class="<c:out value="${primaryClass}"/>"><c:out value="${primaryIcon}"/>  <c:out value="${menuItem.title}"/></a></td>
    </tr>
      <c:if test="${showSecondary}">
      <c:forEach var="subItem" items="${menuItem.items}">
        <c:set var="secondaryClass" value="menuSecondary"/>
        <c:if test="${requestScope.uiAttributes.leftSelected[1] == subItem.id}">
          <c:set var="secondaryClass" value="menuSecondaryOn"/>
        </c:if>
        <c:set var="secondaryIcon" value="+"/>
        <c:if test="${not empty subItem.items}">
          <c:set var="showTertiary" value="true"/>
          <c:set var="secondaryIcon" value="-"/>
        </c:if>
        <tr>
          <td class="<c:out value="${secondaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${subItem.URL}"/>" class="<c:out value="${secondaryClass}"/>"><c:out value="${secondaryIcon}"/> <c:out value="${subItem.title}"/></a></td>
        </tr>
        <c:if test="${showTertiary}">
          <c:forEach var="subSubItem" items="${subItem.items}">
          <c:set var="tertiaryClass" value="menuTertiary"/>
          <c:if test="${subSubItem.isSelected}">
            <c:set var="tertiaryClass" value="menuTertiaryOn"/>
          </c:if>
            <tr>
              <td class="<c:out value="${tertiaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${subSubItem.URL}"/>" class="<c:out value="${tertiaryClass}"/>">- <c:out value="${subSubItem.title}"/></a></td>
            </tr>
          </c:forEach>
        </c:if>
      </c:forEach>
    </c:if>
  </c:forEach>
</table>
<c:if test="${requestScope.hideLoginButton != 'true'}">
<input type="button" name="logout" value="<bean:message key='label.value.logout'/>" class="normalButton" onclick="c_goTo('/customer/logout.do');" />
</c:if>