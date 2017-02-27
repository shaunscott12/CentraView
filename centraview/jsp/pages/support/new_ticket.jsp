<%--
 * $RCSfile: new_ticket.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
%>
<script language="javascript">
  /**
   * Sets the Entity ID and name fields from the entity lookup.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
   * is called from the lookup screen, and must be general
   */
  function setEntity(lookupValues)
  {
    var entityIdField = document.getElementById('entityid');
    var entityNameField = document.getElementById('entityname');
    entityIdField.value = lookupValues.entID;
    entityNameField.value = lookupValues.entName;
  }
  /**
   * Sets the Individual ID and name fields from the individual lookup.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
   * is called from the lookup screen, and must be general
   */
  function setIndividual(lookupValues)
  {
    var individualIdField = document.getElementById('contactid');
    var individualNameField = document.getElementById('contact');
    individualIdField.value = lookupValues.individualID;
    individualNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
  }
  /**
   * Sets the Account Manager ID and name fields from the individual lookup.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
   * is called from the lookup screen, and must be general
   */
  function setEmployee(lookupValues, type)
  {
    if (type == 'manager') {
      var acctmgrIdField = document.getElementById('managerid');
      var acctmgrNameField = document.getElementById('managername');
    }else if (type == 'assignedTo'){
      var acctmgrIdField = document.getElementById('assignedtoid');
      var acctmgrNameField = document.getElementById('assignedto');
    }else{
      return;   // do nothing if type is not correct
    }
    acctmgrIdField.value = lookupValues.individualID;
    acctmgrNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
  }
  function checkAddEditTicket(closeornew)
  {
    window.document.forms[0].action = "<html:rewrite page="/support/save_new_ticket.do"/>?closeornew=" + closeornew;
    window.document.forms[0].submit();
  }
  <c:if test="${param.refreshParent == true}">
  window.opener.location.reload();    // reload parent if we did save and new
  </c:if>
</script>
<html:form action="/support/save_new_ticket.do">
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
          <td class="labelCell"><bean:message key="label.support.subject"/>:</td>
          <td class="contentCell">
          <html:text property="subject" styleId="subject" styleClass="inputBox" size="45" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.details"/>:</td>
          <td class="contentCell">
            <html:textarea property="detail" styleId="detail" styleClass="inputBox" rows="8" cols="44" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.status"/>:</td>
          <td class="contentCell">
            <%
              Vector statusVec = (Vector)gml.getAllStatus();
              pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="status" styleId="status" styleClass="inputBox">
              <html:options collection="statusVec" property="id" labelProperty="name"/>
            </html:select>
          </td>
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
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.support.entity"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.support.name"/>:</td>
          <td class="contentCell">
            <html:hidden property="entityid" styleId="entityid" />
            <html:text property="entityname" styleId="entityname" styleClass="inputBox" size="35" readonly="true" />
            <app:cvbutton property="namelookup" styleId="namelookup" styleClass="normalButton" onclick="c_lookup('Entity');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.contact"/>:</td>
          <td class="contentCell">
            <html:hidden property="contactid" styleId="contactid" />
            <html:text property="contact" styleId="contact" styleClass="inputBox" size="35" readonly="true" />
            <app:cvbutton property="contactlookup" styleId="contactlookup" styleClass="normalButton" onclick="c_lookup('Individual', document.getElementById('entityid').value)">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.support.responsibility"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.support.manager"/>:</td>
          <td class="contentCell">
            <html:hidden property="managerid" styleId="managerid" />
            <html:text property="managername" styleId="managername" styleClass="inputBox" size="35" readonly="true" />
            <app:cvbutton property="mgrlookup" styleId="mgrlookup" styleClass="normalButton" onclick="c_lookup('Employee', 'manager');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.assignedto"/>:</td>
          <td class="contentCell">
            <html:hidden property="assignedtoid" styleId="assignedtoid" />
            <html:text property="assignedto" styleId="assignedto" styleClass="inputBox" size="35" readonly="true" />
            <app:cvbutton property="assigntolookup" styleId="assigntolookup" styleClass="normalButton" onclick="c_lookup('Employee', 'assignedTo');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.support.customfields"/></td>
        </tr>
      </table>
      <jsp:include page="/jsp/pages/common/custom_field.jsp">
        <jsp:param name="Operation" value="Add"/>
        <jsp:param name="RecordType" value="Ticket"/>
      </jsp:include>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="savenclose" styleId="savenclose" styleClass="normalButton" onclick="checkAddEditTicket('close');">
        <bean:message key="label.saveandclose" />
      </app:cvbutton>
      <app:cvbutton property="savennew" styleId="savennew" styleClass="normalButton" onclick="checkAddEditTicket('new');">
        <bean:message key="label.saveandnew" />
      </app:cvbutton>
      <app:cvreset  styleClass="normalButton">
        <bean:message key="label.reset" />
      </app:cvreset>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel" />
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>