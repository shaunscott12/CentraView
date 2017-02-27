<%--
 * $RCSfile: lookup.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <c:if test="${empty requestScope.hideSearch}" >
  <c:if test="${requestScope.showSearch}">
	<tr>
	  <td class="popupTableContent">
	    <input id="searchTextBox" name="searchTextBox" type="text" class="inputBox" value="">
	    <input value="<bean:message key='label.value.search'/>" type="button" class="normalButton"  onclick="lu_searchList()"/>
	  </td>
	</tr>
  </c:if>
	</c:if>
	<c:if test="${empty requestScope.hideMarketingList}" >
	<form name="lookupform">
	<tr>
	  <td class="popupTableContent">
			<select name="dbid" class="inputBox" onchange="return setdb()">
  			<option value="">-- <bean:message key="label.tiles.select"/>--</option>
	  		<c:forEach items="${AllDBList}" var="current">
	  			<c:choose>
	  				<c:when test="${current.strid == dbid}">
			  			<option selected value="<c:out value="${current.strid}"/>"><c:out value="${current.name}"/></option>
			  		</c:when>
			  		<c:otherwise>
			  			<option value="<c:out value="${current.strid}"/>"><c:out value="${current.name}"/></option>
			  		</c:otherwise>
			  	</c:choose>
	  		</c:forEach>
	    </select>
	  </td>
	</tr>
	</form>
	</c:if>
</table>
<c:if test="${empty requestScope.hideMarketingList}" >
<script>
// Sets the marketing list id on the form.
function setdb() {
	var id = document.lookupform.dbid.value;
	document.listrenderer.action="<%=request.getRequestURL()%>?dbid="+id;
	document.listrenderer.target="_self";
	document.listrenderer.submit();
}
</script>
</c:if>
<script>
function lu_searchList()
{
  var filter = document.getElementById('searchTextBox').value;
  var listType = "<c:out value="${requestScope.jsListType}" />";
  if (listType == "14") {
    c_goTo('/contacts/entity_list.do?actionType=lookup&filter=' + filter);
  } else if (listType == "15") {
    c_goTo('/contacts/individual_list.do?actionType=<%=request.getParameter("actionType")%>&filter=' + filter);
  }
}
</script>
<app:valuelist listObjectName="valueList" />