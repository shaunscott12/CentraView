<%--
 * $RCSfile: home.jsp,v $    $Revision: 1.2 $  $Date: 2005/09/13 22:05:03 $ - $Author: mcallist $
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
<%@ page import = "com.centraview.common.*,com.centraview.preference.*"%>
<%
  UserObject userobject =(UserObject) session.getAttribute("userobject");
  UserPrefererences up = (UserPrefererences) userobject.getUserPref();
%>
<!-- start home.jsp -->
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr valign="top">
    <td>
      <jsp:include page="/jsp/pages/home/email_portlet.jsp" />
      <% if (up.getTodaysCalendar().equals("YES")){ %>
      <jsp:include page="/jsp/pages/home/calendar_portlet.jsp" />
      <% } %>
      <% if (up.getUnscheduledActivities().equals("YES")){ %>
      <jsp:include page="/jsp/pages/home/unscheduled_portlet.jsp" />
      <% } %>
      <% if (up.getScheduledOpportunities().equals("YES")){ %>
      <jsp:include page="/jsp/pages/home/opportunity_portlet.jsp" />
      <% } %>
      <% if (up.getProjectTasks().equals("YES")){ %>
      <jsp:include page="/jsp/pages/home/project_portlet.jsp" />
      <% } %>
      <% if (up.getSupportTickets().equals("YES")){ %>
      <jsp:include page="/jsp/pages/home/ticket_portlet.jsp" />
      <% } %>
    </td> 
    <td>
      <% if (up.getCompanyNews().equals("YES")){ %>
      <jsp:include page="/jsp/pages/home/company_news_portlet.jsp" />
      <% } %>
      <jsp:include page="/jsp/pages/home/alerts_portlet.jsp" />
    </td> 
  </tr>
</table>
<!-- end home.jsp -->
