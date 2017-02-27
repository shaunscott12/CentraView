<%--
 * $RCSfile: default_view.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%
   String setting = request.getAttribute("settingfor").toString();
   String submodule = request.getAttribute("typeofsubmodule").toString();
%>
<script language="javascript">
  function gotosave(param)
  {
    document.getElementById('masterdataform').action = "<html:rewrite page="/administration/set_default_view.do" />?buttonpress=" + param;
    document.getElementById('masterdataform').submit();
    return false;
  }
  function gotocancel()
  {
    document.getElementById('masterdataform').action = "<html:rewrite page="/administration/view_module_settings.do" />?typeofsubmodule=Contacts";
    document.getElementById('masterdataform').submit();
    return true;
  }
</script>
<html:form action="/administration/set_default_view.do" styleId="masterdataform">
<input type="hidden" name="setting" value="<%=setting%>">
<input type="hidden" name="submodule" value="<%=submodule%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><%=submodule%> <bean:message key="label.administration.modulesettingsdefaultviewssettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell" colspan="2">
      <span class="boldText">
        <bean:message key="label.administration.selectdefaultview"/>.
      </span>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <% if (submodule.equals("Contacts")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.entities"/>:</td>
    <td class="contentCell">
      <html:select property="contactsEntityList" styleClass="inputBox">
        <html:optionsCollection  property="entityListVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.individuals"/>:</td>
    <td class="contentCell">
      <html:select property="contactsIndividualList" styleClass="inputBox">
        <html:optionsCollection  property="individualListVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Activities")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.allactivities"/></td>
    <td class="contentCell">
      <html:select property="activitiesAllActivity" styleClass="inputBox">
        <html:optionsCollection  property="multiActivityVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.appointments"/>:</td>
    <td class="contentCell">
      <html:select property="activitiesAppointment" styleClass="inputBox">
        <html:optionsCollection  property="appointmentVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.calls"/>:</td>
    <td class="contentCell">
      <html:select property="activitiesCalls" styleClass="inputBox">
        <html:optionsCollection  property="callsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.meetings"/>:</td>
    <td class="contentCell">
      <html:select property="activitiesMeetings" styleClass="inputBox">
        <html:optionsCollection  property="meetingsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.todos"/>:</td>
    <td class="contentCell">
      <html:select property="activitiesToDos" styleClass="inputBox">
        <html:optionsCollection  property="toDosVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.nextactions"/>:</td>
    <td class="contentCell">
      <html:select property="activitiesNextActions" styleClass="inputBox">
        <html:optionsCollection  property="nextActionsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Notes")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.notes"/>:</td>
    <td class="contentCell">
      <html:select property="notes" styleClass="inputBox">
        <html:optionsCollection  property="notesVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("File")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.files"/>:</td>
    <td class="contentCell">
      <html:select property="files" styleClass="inputBox">
        <html:optionsCollection  property="filesVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Sales")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.opportunities"/>:</td>
    <td class="contentCell">
      <html:select property="salesOpportunity" styleClass="inputBox">
        <html:optionsCollection  property="opportunityVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.proposals"/>:</td>
    <td class="contentCell">
      <html:select property="salesProposal" styleClass="inputBox">
        <html:optionsCollection  property="proposalVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Marketing")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.listmanager"/>:</td>
    <td class="contentCell">
      <html:select property="marketings" styleClass="inputBox">
        <html:optionsCollection  property="marketingsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.promotions"/>:</td>
    <td class="contentCell">
      <html:select property="marketingsPromotions" styleClass="inputBox">
        <html:optionsCollection  property="promotionsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.literaturefulfillments"/>:</td>
    <td class="contentCell">
      <html:select property="marketingsLiteraturefulfillment" styleClass="inputBox">
        <html:optionsCollection  property="literaturefulfillmentVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.events"/>:</td>
    <td class="contentCell">
      <html:select property="marketingsEvents" styleClass="inputBox">
        <html:optionsCollection  property="eventsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Projects")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.projects"/>:</td>
    <td class="contentCell">
      <html:select property="projectsProject" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="projectVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.tasks"/>:</td>
    <td class="contentCell">
      <html:select property="projectsTask" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="taskVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.timeslips"/>:</td>
    <td class="contentCell">
      <html:select property="projectsTimeSlips" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="timeSlipsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Support")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.tickets"/>:</td>
    <td class="contentCell">
      <html:select property="supportTicket" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="ticketVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.faqs"/>:</td>
    <td class="contentCell">
      <html:select property="supportFaq" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="faqVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.knowledgebase"/>:</td>
    <td class="contentCell">
      <html:select property="supportKnowledgebase" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="knowledgebaseVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("Accounting")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.orderhistory"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsOrderHistory" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="orderHistoryVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.invoicehistory"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsInvoiceHistory" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="invoiceHistoryVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.payments"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsPayments" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="paymentsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.expenses"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsExpenses" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="expensesVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.purchaseorder"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsPurchaseOrder" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="purchaseOrderVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.items"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsItems" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="itemsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.glaccounts"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsGLAccounting" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="gLAccountingVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.inventory"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsInventory" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="inventoryVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.vendors"/>:</td>
    <td class="contentCell">
      <html:select property="accountingsVendors" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="vendorsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } else if (submodule.equals("HR")) { %>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.expensesforms"/>:</td>
    <td class="contentCell">
      <html:select property="hrExpensesForm" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="expensesFormVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.timesheets"/>:</td>
    <td class="contentCell">
      <html:select property="hrTimeSheets" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="timeSheetsVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.employeehandbook"/>:</td>
    <td class="contentCell">
      <html:select property="hrEmployeeHandbook" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="employeeHandbookVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.employeelist"/>:</td>
    <td class="contentCell">
      <html:select property="hrEmployee" styleClass="inputBox" multiple="true" size="5">
        <html:optionsCollection  property="employeeVec" value="id" label="name" />
      </html:select>
    </td>
  </tr>
  <% } %>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="save" type="button" class="normalButton" value="<bean:message key='label.value.save'/>" onclick="gotosave('save');"/>
      <input name="savaandclose" type="button" class="normalButton" value="<bean:message key='label.value.saveandclose'/>" onclick="gotosave('saveandclose');" />
      <input name="cancel" type="button" class="normalButton" value="<bean:message key='label.value.cancel'/>" onclick="gotocancel();"/>
    </td>
  </tr>
</table>
</html:form>