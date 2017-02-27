<%--
 * $RCSfile: kblist_renderer.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import = "com.centraview.common.* ,java.util.*"%>
<script language="JavaScript1.2">

	function firstList()
	{
    window.document.listrenderer.action="<bean:message key='label.url.root' />/CV_FirstPageHandler.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }

	function previousList()
	{
	  window.document.listrenderer.action="<bean:message key='label.url.root' />/CV_PreviousPageHandler.do";
	  window.document.listrenderer.target="_self";
	  window.document.listrenderer.submit();
	}

	function nextList()
	{
    window.document.listrenderer.action="<bean:message key='label.url.root' />/CV_NextPageHandler.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }

	function lastList()
	{
    window.document.listrenderer.action="<bean:message key='label.url.root' />/CV_LastPageHandler.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }

  function recordsperpage(rpp)
  {
    window.document.listrenderer.target="_self";
    window.document.listrenderer.action="<bean:message key='label.url.root' />/CV_RecordsPerPageHandler.do?recordperpage="+rpp;
    window.document.listrenderer.submit();
	}
</script>
<%
  boolean showSearchBar = false;
  if (request.getAttribute("showTicketSearch") != null)
  {
    if (((String)request.getAttribute("showTicketSearch")).equals("true"))
    {
      showSearchBar = true;
    }
  }
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <% if (showSearchBar){ %>
  <tr>
    <td class="searchBar">
      <jsp:include page="/jsp/customer/support/search_bar.jsp" />
    </td>
  </tr>
  <% } %>
  <%
    UserObject userobject = (UserObject)session.getAttribute("userobject");
    DisplayList displaylist = (DisplayList)request.getAttribute("displaylist");

    if (userobject != null || displaylist!= null)
    {
      %>
      <tr height="10">
        <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
      </tr>
      <tr>
        <% request.setAttribute("pagingBarPageOccurance", "1"); %>
        <td class="topPagingBar" width="100%">
          <jsp:include page="/jsp/customer/common/paging_bar.jsp"/>
        </td>
      </tr>
      <tr>
        <td>
          <jsp:include page="/jsp/customer/support/KBList.jsp" />
        </td>
      </tr>
      <tr>
        <% request.setAttribute("pagingBarPageOccurance", "2"); %>
        <td class="bottomPagingBar" width="100%">
          <jsp:include page="/jsp/customer/common/paging_bar.jsp"/>
        </td>
      </tr>
      <%
    }else{
      %>
      <tr height="11">
        <td><bean:message key="label.customer.support.norecordsfound"/></td>
      </tr>
      <%
    }   // end if (userobject != null || displaylist!= null)
  %>
  <tr height="11">
    <td colspan="3"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
  </tr>
</table>