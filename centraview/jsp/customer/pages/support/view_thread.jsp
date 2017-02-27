<%--
 * $RCSfile: view_thread.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/06 16:02:05 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 1.0.0 (the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="org.apache.struts.action.*" %>
<%@ page import="com.centraview.support.ticket.TicketForm" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  DynaActionForm dynaForm  = (DynaActionForm) request.getAttribute("cvthreadform");
  TicketForm ticketForm = (TicketForm)request.getAttribute("ticketform");
%>
<script language="javascript">
  function sendReply()
  {
    var ticketId = document.getElementById('ticketnumber').value;
    c_goTo('/customer/new_thread.do?ticketId=' + ticketId);
  }
</script>
<html:form action="/customer/view_thread.do">
<html:hidden property="ticketnumber" styleId="ticketnumber"/>
<html:hidden property="threadid"/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="replybutton" styleClass="normalButton" onclick="sendReply();">
        <bean:message key="label.customer.pages.support.reply"/>
      </app:cvbutton>
      <app:cvbutton property="cancelbutton" styleClass="normalButton" onclick="history.go(-1);">
        <bean:message key="label.cancel" />
      </app:cvbutton>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.support.ticketdetails"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.ticketnumber"/>:</td>
    <td class="contentCell"><c:out value="${requestScope.ticketform.id}" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.subject"/>:</td>
    <td class="contentCell"><c:out value="${requestScope.ticketform.subject}" /></td>
  </tr>
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.customer.pages.support.details"/>:</td>
    <td class="contentCell"><pre><c:out value="${requestScope.ticketform.detail}" /></pre></td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.support.threaddetails"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.title"/>:</td>
    <td class="contentCell"><c:out value="${cvthreadform.map.title}" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.detail"/>:</td>
    <td class="contentCell"><c:out value="${cvthreadform.map.threaddetail}" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.priority"/>:</td>
    <td class="contentCell"><%=getPriorityName((String)dynaForm.get("priority"))%></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.createdby"/>:</td>
    <td class="contentCell"><a class="plainLink" onclick="c_openPopup('/customer/view_individual.do?rowId=<c:out value="${cvthreadform.map.createdbyid}" />');"><c:out value="${cvthreadform.map.createdby}" /></a></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.created"/>:</td>
    <td class="contentCell"><c:out value="${cvthreadform.map.createddate}" /></td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="replybutton" styleClass="normalButton" onclick="checkSendReply();">
        <bean:message key="label.customer.pages.support.reply"/>
      </app:cvbutton>
      <app:cvbutton property="cancelbutton" styleClass="normalButton" onclick="history.go(-1);">
        <bean:message key="label.cancel" />
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>
<%!
  public String getPriorityName(String level)
  {
    if ("1".equals(level)) {
      return "Low";
    } else if ("2".equals(level)) {
      return "Medium";
    } else if ("3".equals(level)) {
      return "High";
    } else if ("4".equals(level)) {
      return "No Priority";
    } else if ("5".equals(level)) {
      return "Critical";
    } else {
      return "Medium";
    }
  }

%>