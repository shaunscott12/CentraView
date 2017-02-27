<%--
 * $RCSfile: view_thread.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<script language="javascript">
  function submitThreadForm(closeornew)
  {
    window.document.forms[0].action = "<html:rewrite page="/support/save_thread.do"/>?closeornew=" + closeornew;
    window.document.forms[0].submit();
	}

  function checkSendReply()
  {
    window.document.forms[0].action = "<html:rewrite page="/support/new_thread.do"/>?ticketId=<c:out value="${threadform.map.ticketnumber}" />";
    window.document.forms[0].submit();
  }
  <c:if test="${param.refreshParent == true}">
  window.opener.location.reload();    // reload parent if we did save and new
  </c:if>
</script>
<html:form action="/support/save_thread.do">
<input type="hidden" name="ticketId" id="ticketId" value="<c:out value="${param.ticketId}" />" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <c:if test="${requestScope.action == 'view'}">
      <app:cvbutton property="replybutton" styleId="replybutton" styleClass="normalButton" onclick="checkSendReply()">
        <bean:message key="label.support.reply"/>
      </app:cvbutton>
      </c:if>
      <c:if test="${requestScope.action == 'new'}">
      <app:cvbutton property="savenclosebutton" styleId="savenclosebutton" styleClass="normalButton" onclick="submitThreadForm('close');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="savennewbutton" styleId="savennewbutton" styleClass="normalButton" onclick="submitThreadForm('new');">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      </c:if>
      <app:cvbutton property="cancelbutton" styleId="cancelbutton" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td valign="top" width="50%" style="border-right: #cccccc 1px solid;">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.support.ticketdetails"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.support.ticketid"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.id}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.subject"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.subject}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.details"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.detail}" /></td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.support.entity"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.support.name"/>:</td>
          <td class="contentCell"><a class="plainLink" onclick="c_openPopup('/contacts/view_entity.do?rowId=<c:out value="${requestScope.ticketform.entityid}" />');"><c:out value="${requestScope.ticketform.entityname}" /></a></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.contact"/>:</td>
          <td class="contentCell"><a class="plainLink" onclick="c_openPopup('/contacts/view_individual.do?rowId=<c:out value="${requestScope.ticketform.contactid}" />');"><c:out value="${requestScope.ticketform.contact}" /></a></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.address"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.address}" escapeXml="false" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.website"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.website}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.phone"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.phone}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.email"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.email}" /></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.support.newthread"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.support.title"/>:</td>
    <td class="contentCell"><html:text property="title" styleId="title" styleClass="inputBox" size="45" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.support.detail"/>:</td>
    <td class="contentCell"><html:textarea property="threaddetail" styleId="threaddetail" styleClass="inputBox" rows="6" cols="44" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.support.priority"/>:</td>
    <td class="contentCell">
      <%
        Vector priorityVec = (Vector)gml.getAllPriorities();
        pageContext.setAttribute("priorityVec", priorityVec, PageContext.PAGE_SCOPE);
      %>
      <html:select property="priority" styleId="priority" styleClass="inputBox">
        <html:options collection="priorityVec" property="id" labelProperty="name"/>
      </html:select>
    </td>
  </tr>
  <c:if test="${requestScope.action == 'view'}">
  <tr>
    <td class="labelCell"><bean:message key="label.support.createdby"/>:</td>
    <td class="contentCell"><c:out value="${threadform.map.createdby}" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.support.createddate"/>:</td>
    <td class="contentCell"><c:out value="${threadform.map.createddate}" default="N/A" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.support.modifieddate"/>:</td>
    <td class="contentCell"><c:out value="${threadform.map.modifieddate}" default="N/A" /></td>
  </tr>
  </c:if>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <c:if test="${requestScope.action == 'view'}">
      <app:cvbutton property="replybutton" styleId="replybutton" styleClass="normalButton" onclick="checkSendReply()">
        <bean:message key="label.support.reply"/>
      </app:cvbutton>
      </c:if>
      <c:if test="${requestScope.action == 'new'}">
      <app:cvbutton property="savenclosebutton" styleId="savenclosebutton" styleClass="normalButton" onclick="submitThreadForm('close');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="savennewbutton" styleId="savennewbutton" styleClass="normalButton" onclick="submitThreadForm('new');">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      </c:if>
      <app:cvbutton property="cancelbutton" styleId="cancelbutton" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>