<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.common.UIAttributes" %>
<table border="0" cellspacing="0" cellpadding="0" class="tabsContainer">
  <tr>
    <td class="tabsContainerTopRow">
      <table border="0" cellspacing="0" cellpadding="0" class="tabsTable">
        <tr>
          <td class="firstTabOff"><html:img page="/images/spacer.gif" width="15" height="11" alt="" /></td>
          <c:forEach var="tab" items="${requestScope.uiAttributes.mainNav.elements}">
          <c:set var="class" value="tabsOff" />
          <c:if test="${requestScope.uiAttributes.mainSelected == tab.id}" ><c:set var="class" value="tabsOn" /></c:if>
          <td class="<c:out value="${class}"/>" nowrap><a href="<html:rewrite page=""/><c:out value="${tab.URL}"/>" class="<c:out value="${class}"/>"><c:out value="${tab.title}"/></a></td>
          </c:forEach>
          <td class="lastTabOff"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
        </tr>		
      </table>
    </td>
  </tr>
  <tr>
    <td class="tabsBottomBar"><html:img page="/images/clear.gif" width="1" height="6" alt="" /></td>
  </tr>
</table>
