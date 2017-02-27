<%--
 * $RCSfile: left_nav.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/26 20:30:06 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.UIAttributes" %>
<%@ page import="com.centraview.common.menu.NestedMenuItem" %>
<%
  // loop through the nav items and set the locale :-/
  // total of three levels to go down.
  // recursion may be cleaner here, but for now this is what it is.
  Locale locale = pageContext.getRequest().getLocale();
  UIAttributes attributes = (UIAttributes)pageContext.getRequest().getAttribute("uiAttributes");
  ArrayList elements = attributes.getLeftNav().getElements();
  for (int i = 0; i < elements.size(); i++) {
    NestedMenuItem element = (NestedMenuItem)elements.get(i);
    element.setLocale(locale);
    ArrayList subItems = element.getItems();
    for (int j = 0; j < subItems.size(); j++) {
      NestedMenuItem subItem = (NestedMenuItem)subItems.get(j);
      subItem.setLocale(locale);
      ArrayList subSubItems = subItem.getItems();
      for (int k = 0; k < subSubItems.size(); k++) {
        NestedMenuItem subSubItem = (NestedMenuItem)subSubItems.get(k);
        subSubItem.setLocale(locale);
      }
    }
  }
  // end ugly i18n hack :)
%>
<div class="leftNavContainer">
<p class="leftNav"><c:out value="${requestScope.uiAttributes.leftNav.label}"/></p>
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
      <td class="<c:out value="${primaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${menuItem.URL}"/>" class="<c:out value="${primaryClass}"/>"><c:out value="${primaryIcon}"/>  <c:out value="${menuItem.label}"/></a></td>
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
          <td class="<c:out value="${secondaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${subItem.URL}"/>" class="<c:out value="${secondaryClass}"/>"><c:out value="${secondaryIcon}"/> <c:out value="${subItem.label}"/></a></td>
        </tr>
        <c:if test="${showTertiary}">
          <c:forEach var="subSubItem" items="${subItem.items}">
          <c:set var="tertiaryClass" value="menuTertiary"/>
          <c:if test="${subSubItem.isSelected}">
            <c:set var="tertiaryClass" value="menuTertiaryOn"/>
          </c:if>
            <tr>
              <td class="<c:out value="${tertiaryClass}"/>"><a href="<html:rewrite page=""/><c:out value="${subSubItem.URL}"/>" class="<c:out value="${tertiaryClass}"/>">- <c:out value="${subSubItem.label}"/></a></td>
            </tr>
          </c:forEach>
        </c:if>
      </c:forEach>
    </c:if>
  </c:forEach>
</table>
<c:if test="${(requestScope.moduleId == 14 || requestScope.moduleId == 15) && requestScope.hideListDropdown != true}">
<select name="dbid" id="dbid" class="inputBox" onchange="c_setListId(<c:out value="${requestScope.moduleId}"/>);" style="width: 150px;">
  <%
    Vector vecDb = new Vector();
    if (request.getAttribute("AllDBList") != null) {
      vecDb = (Vector)request.getAttribute("AllDBList");
    }
    pageContext.setAttribute("vecDb", vecDb, PageContext.PAGE_SCOPE);

    int dbid = 1;
    if (request.getAttribute("dbid") != null) {
      dbid = Integer.parseInt((String)request.getAttribute("dbid"));
    } else if (session.getAttribute("dbid") != null) {
      dbid = Integer.parseInt((String)session.getAttribute("dbid"));
    }

    for (int i = 0; i < vecDb.size(); i++) {
      DDNameValue ddname = (DDNameValue)vecDb.elementAt(i);
      if (dbid == Integer.parseInt(ddname.getStrid())) {
        %><option value=<%=ddname.getStrid()%> selected><%=ddname.getName()%></option><%
      }else{
        %><option value=<%=ddname.getStrid()%>><%=ddname.getName()%></option><%
      }
    }
  %>
</select><br/><br/>
</c:if>
<html:img page="/customerlogo" border="0" alt="" />
</div>