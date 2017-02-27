<%--
 * $RCSfile: view_ticket.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.support.ticket.TicketForm" %>
<%@ page import="com.centraview.support.ticket.TicketConstantKeys" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  String ocStatus = "";
  if (request.getAttribute("OCStatus") != null) {
    ocStatus = (String)request.getAttribute("OCStatus");
  }

  String ticketId = ((TicketForm) request.getAttribute("cvticketform")).getId();
  session.setAttribute("ticketId", ticketId);
  session.setAttribute("listId", request.getParameter("listId"));
  session.setAttribute("rowId", request.getParameter("rowId"));

  String ticketIdStr = (String)(((TicketForm) request.getAttribute("cvticketform")).getId());
  if (ticketIdStr == null){
    ticketIdStr = "-1";
  }
%>
<script language="javascript">
  function checkAddEditTicket(closeornew)
  {
    window.document.forms[0].action = "<html:rewrite page="/customer/save_ticket.do"/>?closeornew="+closeornew;
    window.document.forms[0].submit();
    return false;
  }
</script>
<html:form action="/customer/view_ticket.do">
<html:hidden property="entityid"/>
<html:hidden property="contactid"/>
<html:hidden property="managerid"/>
<html:hidden property="assignedtoid"/>
<input type="hidden" name="individualtype">
<input type="hidden" name="TYPEOFOPERATION" value="<%=request.getParameter("TYPEOFOPERATION")%>"/>
<input type="hidden" name="ocStatus" value="<%=request.getAttribute("OCStatus")%>"/>
<input type="hidden" name="ticketId" value="<%=((TicketForm) request.getAttribute("cvticketform")).getId()%>"/>
<input type="hidden" name="priority" value="<%=((TicketForm) request.getAttribute("cvticketform")).getPriority()%>"/>
<html:hidden property="id" value= "<%=((TicketForm) request.getAttribute("cvticketform")).getId()%>"/>
<html:hidden property="rowId" value= "<%=((TicketForm) request.getAttribute("cvticketform")).getId()%>"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="newticketbutton" styleClass="normalButton" onclick="c_goTo('/customer/new_ticket.do');">
        <bean:message key="label.customer.pages.support.newticket"/>
      </app:cvbutton>
      <app:cvbutton property="savenclosebutton" styleClass="normalButton" onclick="checkAddEditTicket('close');" >
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="cancelbutton" styleClass="normalButton" onclick="c_goTo('/customer/ticket_list.do');" >
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td valign="top" width="50%" style="border-right: #cccccc 1px soild;">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.support.ticketdetails"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.ticketnumber"/>:</td>
          <td class="contentCell"><%=((TicketForm) request.getAttribute("cvticketform")).getId()%></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.status"/>:</td>
          <td class="contentCell">
            <%
              Vector statusVec = (Vector)gml.getAllStatus();
              pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="status" styleClass="inputBox">
              <html:options collection="statusVec" property="id" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.subject"/>:</td>
          <td class="contentCell"><html:text property="subject" styleClass="inputBox" size="45"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.details"/>:</td>
          <td class="contentCell"><html:textarea property="detail" cols="44" rows="8" styleClass="inputBox" /></td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader">&nbsp;</td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.opened"/>:</td>
          <td class="contentCell"><%out.print(request.getAttribute("openDate"));%></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.closed"/>:</td>
          <td class="contentCell">
            <%
              if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_CLOSE)) {
                out.print(request.getAttribute("closeDate"));
              }else{
                out.print("N/A");
              }
            %>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.support.lastmodified"/>:</td>
          <td class="contentCell">
            <%
              if (request.getAttribute("modifyDate") != null) {
                out.print(request.getAttribute("modifyDate"));
              }else{
                out.print(request.getAttribute("openDate"));
              }
            %>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<jsp:include page="/jsp/customer/pages/support/threads_list.jsp" />
<br/>
<jsp:include page="/jsp/customer/pages/support/files_list.jsp" />
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="newticketbutton" styleClass="normalButton" onclick="c_goTo('/customer/new_ticket.do');">
        <bean:message key="label.customer.pages.support.newticket"/>
      </app:cvbutton>
      <app:cvbutton property="savenclosebutton" styleClass="normalButton" onclick="checkAddEditTicket('close');" >
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="cancelbutton" styleClass="normalButton" onclick="c_goTo('/customer/ticket_list.do');" >
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>