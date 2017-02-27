<%--
 * $RCSfile: new_thread.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
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
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.support.ticket.TicketForm" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
%>
<html:form action="/customer/save_thread.do">
<input type="hidden" name="ticketId" value="<c:out value="${param.ticketId}" />" />
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvsubmit property="savenclosebutton" styleClass="normalButton">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvbutton property="cancelbutton" styleClass="normalButton" onclick="history.go(-1);">
        <bean:message key="label.cancel"/>
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
    <td class="sectionHeader"><bean:message key="label.customer.pages.support.newthread"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.title"/>:</td>
    <td class="contentCell">
      <html:text property="title" styleClass="inputBox" size="45" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.detail"/>:</td>
    <td class="contentCell">
      <html:textarea property="threaddetail" rows="6" cols="44" styleClass="inputBox" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.support.priority"/>:</td>
    <td class="contentCell">
      <%
        Vector priorityVec = (Vector)gml.getAllPriorities();
        pageContext.setAttribute("priorityVec", priorityVec, PageContext.PAGE_SCOPE);
      %>
      <html:select property="priority" styleClass="inputBox">
        <html:options collection="priorityVec" property="id" labelProperty="name"/>
      </html:select>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvsubmit property="savenclosebutton" styleClass="normalButton">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvbutton property="cancelbutton" styleClass="normalButton" onclick="history.go(-1);">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>