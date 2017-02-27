<%--
 * $RCSfile: ticket_detail.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/16 12:38:40 $ - $Author: mcallist $
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
<%@ page import="com.centraview.support.ticket.TicketForm" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%@ page import="com.centraview.support.ticket.TicketConstantKeys" %>
<%
  String url = request.getParameter("rowId");
  
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  session.setAttribute("rowID",url);
  TicketForm ticketForm  = (TicketForm) request.getAttribute("ticketform");
  
  String strTicketID = (String)ticketForm.getId();
  int ticketID = 0;
  if (strTicketID != null && !strTicketID.equals("") && !strTicketID.equals("null")) {
    ticketID = Integer.parseInt(strTicketID);
  }
  String ocStatus = "";
  if (request.getAttribute("OCStatus") != null) {
    ocStatus = (String)request.getAttribute("OCStatus");
  }

%>
<script language="javascript">
  
  function checkAddEditTicket(closeornew, closeTicket)
  {
    window.document.forms[0].action = "<html:rewrite page="/support/save_edit_ticket.do"/>?closeornew=" + closeornew + "&closeTicket=" + closeTicket;
    window.document.forms[0].submit();
  }
  
  function deleteTicket() 
  {
    var condition=window.confirm("Are you sure you want to \npermanently delete this record?");
    if (condition) {
      document.forms[0].action = "<html:rewrite page="/support/ticket_delete.do"/>?windowType=Popup";
      document.forms[0].submit();
    }
  } 
  
  function ticketAction(closeorreopen)
  {
    var ticketID = document.getElementById('ticketId').value;
    var ocStatus = "";
    <% if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_OPEN)) { %>
    ocStatus = '<%=TicketConstantKeys.TK_OCSTATUS_CLOSE%>';
    <% }else if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_CLOSE)) { %>
    ocStatus = '<%=TicketConstantKeys.TK_OCSTATUS_OPEN%>';
    <% } %>
    window.document.forms[0].action = "<html:rewrite page="/support/oc_action.do"/>?closeorreopen=" + ocStatus + "&ticketID=" + ticketID;
    window.document.forms[0].submit();
  }
  
  function gotoPermission()
  {
    c_openWindow('/common/record_permission.do?filepermissionedit=1&modulename=Ticket', '', 'toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=no,width=550,height=360');
	}
  
  function newThread()
  {
    var ticketId = document.getElementById('ticketId').value;
    c_openWindow('/support/new_thread.do?ticketId='+ticketId,'new_thread', 715, 445, '');
  }

  function newTimeslip()
  {
    var ticketId = document.getElementById('ticketId').value;
    var subject = '<c:out value="${requestScope.ticketform.subject}" />';
    c_openWindow('/projects/new_timeslip.do?ticketId=' + ticketId + '&subject=' + subject, 'new_timeslip', 740, 335, '');
  }

  /**
   * Opens the common attachments screen. That screen has
   * javascript that calls back to the three functions below.
   */
  function callAttachFile()
  {
    var ticketId = document.getElementById('ticketId').value;
    c_openWindow("/email/attachment_lookup.do?ticketId=" + ticketId, '', 725, 189, '');
  }

  // local variable to store a string of comma-separated file
  // IDs that will be passed to another Action.
  var fileIds = '';

  /** 
   * Called by the attachments window, clears out the fileIds
   * string to initialize the attachment sequence.
   */
  function removeAttachmentsContent()
  {
    fileIds = '';
  }

  /**
   * Called by the attachements window, once for each attachment
   * that was selected. Gets the file ID, and appends it to the
   * comma-separated string "fileIds".
   */
  function updateAttachments(fileName, fileIDstring)
  {
    fileIDArray = fileIDstring.split("#");
    fileID = fileIDArray[0];
    fileIds = fileIds + fileID + ',';
  }

  /**
   * Called by the attachments window, gets the comma-separated string
   * of file IDs and passes it to the attach_files.do action which will
   * link each file ID to the current ticket, then return to this screen.
   */
  function finishAttachments()
  {
    var ticketId = document.getElementById('ticketId').value;
    c_goTo('/support/attach_files.do?fileIds=' + fileIds + '&ticketId=' + ticketId);
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
  function printTicket() {
  	var ticketId = document.getElementById('ticketId').value;
	c_openWindow("/support/print_ticket.do?ticketID=" + ticketId, '', 725, 189, '');
  }
  <c:if test="${param.refreshParent == true}">
  window.opener.location.reload();    // reload parent if we did save and new
  </c:if>

</script>
<html:form action="/support/view_ticket.do">
<input type="hidden" name="individualtype" id="individualtype" >
<input type="hidden" name="TYPEOFOPERATION" id="TYPEOFOPERATION"  value="<%=request.getParameter("TYPEOFOPERATION")%>"/>
<input type="hidden" name="ocStatus" id="ocStatus"  value="<%=request.getAttribute("OCStatus")%>"/>
<input type="hidden" name="ticketId" id="ticketId"  value="<%=((TicketForm) request.getAttribute("ticketform")).getId()%>"/>
<html:hidden property="id" styleId="id"  value= "<%=((TicketForm) request.getAttribute("ticketform")).getId()%>"/>
<html:hidden property="rowId" styleId="rowId"  value= "<%=((TicketForm) request.getAttribute("ticketform")).getId()%>"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="newticketbutton" styleId="newticketbutton" styleClass="normalButton" onclick="c_openPopup('/support/new_ticket.do');">
        <bean:message key="label.new"/> <bean:message key="label.ticket"/>
      </app:cvbutton>
      <app:cvbutton property="savebutton" styleId="savebutton" styleClass="normalButton" onclick="checkAddEditTicket('save', 'false');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="savennewbutton" styleId="savennewbutton" styleClass="normalButton" onclick="checkAddEditTicket('new', 'false');">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      <app:cvbutton property="deleteticketbutton" styleId="deleteticketbutton" styleClass="normalButton" onclick="deleteTicket();" recordID="<%=ticketID%>" modulename="Ticket" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/>
      </app:cvbutton>
      <app:cvbutton property="closebutton" styleId="closebutton" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.close.window"/>
      </app:cvbutton>
      <% if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_OPEN)) { %>
      <app:cvbutton property="saveclosebutton" styleId="saveclosebutton" styleClass="normalButton" onclick="checkAddEditTicket('close', 'true');">
        <bean:message key="label.ticket"/> <bean:message key="label.saveandclose"/> 
      </app:cvbutton>
      <app:cvbutton property="closeticketbutton" styleId="closeticketbutton" styleClass="normalButton" onclick="ticketAction();">
        <bean:message key="label.close.ticket"/>
      </app:cvbutton>
      <% }else if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_CLOSE)){ %>
      <app:cvbutton property="reopenticketbutton" styleId="reopenticketbutton" styleClass="normalButton" onclick="ticketAction();">
        <bean:message key="label.reopen.ticket"/> 
      </app:cvbutton>
      <% } %>
      <app:cvbutton property="propertyindividual" styleId="propertyindividual" styleClass="normalButton" onclick="gotoPermission();">
        <bean:message key="label.permissions"/>
      </app:cvbutton>
      <html:img page="/images/button_print.gif" onclick="printTicket();" width="19" height="17" align="middle" alt="" />
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
          <td class="labelCellBold"><bean:message key="label.support.ticketid"/>:</td>
          <td class="contentCell"><span class="boldText"><c:out value="${requestScope.ticketform.id}" /></span></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.subject"/>:</td>
          <td class="contentCell"><html:text property="subject" styleId="subject" styleClass="inputBox" size="45" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.details"/>:</td>
          <td class="contentCell"><html:textarea property="detail" styleId="detail" styleClass="inputBox" cols="44" rows="8" /></td>
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
            <html:text property="entityname" styleId="entityname" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.getElementById('entityid').value);" size="35" readonly="true" />
            <app:cvbutton property="namelookupbutton" styleId="namelookupbutton" styleClass="normalButton" onclick="c_lookup('Entity');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.contact"/>:</td>
          <td class="contentCell">
            <html:hidden property="contactid" styleId="contactid" />
            <html:text property="contact" styleId="contact" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('contactid').value);" size="35" readonly="true" />
            <app:cvbutton property="contactlookup" styleId="contactlookup" styleClass="normalButton" onclick="c_lookup('Individual', document.getElementById('entityid').value);">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.address"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.address}" escapeXml="false" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.website"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.website}" escapeXml="false" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.phone"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.phone}" escapeXml="false" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.email"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.ticketform.email}" escapeXml="false" /></td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%" style="border-right: #cccccc 1px solid;">
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
            <html:text property="managername" styleId="managername" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('managerid').value);" size="35" readonly="true" />
            <app:cvbutton property="managerlookupbutton" styleId="managerlookupbutton" styleClass="normalButton" onclick="c_lookup('Employee','manager');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.assignedto"/>:</td>
          <td class="contentCell">
            <html:hidden property="assignedtoid" styleId="assignedtoid" />
            <html:text property="assignedto" styleId="assignedto" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('assignedtoid').value);" size="35" readonly="true" />
            <app:cvbutton property="assigntolookupbutton" styleId="assigntolookupbutton" styleClass="normalButton" onclick="c_lookup('Employee','assignedTo');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.opened"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.openDate}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.closed"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.closeDate}" default="N/A" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.support.modified"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.modifyDate}" /></td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.support.customfields"/></td>
        </tr>
      </table>
      <jsp:include page="/jsp/pages/common/custom_field.jsp">
        <jsp:param name="Operation" value="Edit"/>
        <jsp:param name="RecordType" value="Ticket"/>
        <jsp:param name="RecordId" value="<%=ticketID%>"/>
      </jsp:include>
    </td>
  </tr>
</table>
<jsp:include page="/jsp/pages/support/thread_list.jsp" />
<br/>
<jsp:include page="/jsp/pages/support/timeslip_list.jsp" />
<br/>
<jsp:include page="/jsp/pages/support/files_list.jsp" />
<br/>
<jsp:include page="/jsp/pages/support/expense_list.jsp" />
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="newticketbuttonbottom" styleId="newticketbuttonbottom" styleClass="normalButton" onclick="c_openPopup('/support/new_ticket.do');">
        <bean:message key="label.new"/> <bean:message key="label.ticket"/>
      </app:cvbutton>
      <app:cvbutton property="savebuttonbottom" styleId="savebuttonbottom" styleClass="normalButton" onclick="checkAddEditTicket('save', 'false');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="savennewbuttonbottom" styleId="savennewbuttonbottom" styleClass="normalButton" onclick="checkAddEditTicket('new', 'false');">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      <app:cvbutton property="deleteticketbuttonbottom" styleId="deleteticketbuttonbottom" styleClass="normalButton" onclick="deleteTicket();" recordID="<%=ticketID%>" modulename="Ticket" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/>
      </app:cvbutton>
      <app:cvbutton property="closebutton" styleId="closebutton" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.close.window"/>
      </app:cvbutton>
      <% if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_OPEN)) { %>
      <app:cvbutton property="saveclosebuttonbottom" styleId="saveclosebutton" styleClass="normalButton" onclick="checkAddEditTicket('close', 'true');">
        <bean:message key="label.ticket"/> <bean:message key="label.saveandclose"/> 
      </app:cvbutton>
      <app:cvbutton property="closeticketbutton" styleId="closeticketbutton" styleClass="normalButton" onclick="ticketAction();">
        <bean:message key="label.close.ticket"/>
      </app:cvbutton>
      <% }else if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_CLOSE)){ %>
      <app:cvbutton property="reopenticketbutton" styleId="reopenticketbutton" styleClass="normalButton" onclick="ticketAction();">
        <bean:message key="label.reopen.ticket"/>
      </app:cvbutton>
      <% } %>
      <app:cvbutton property="propertyindividual" styleId="propertyindividual" styleClass="normalButton" onclick="gotoPermission();">
        <bean:message key="label.permissions"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<script language="javascript">
  <% if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_OPEN)) { %>
  document.getElementById('newticketbutton').enabled = "true";
  document.getElementById('savebutton').enabled = "true";
  document.getElementById('savennewbutton').enabled = "true";
  document.getElementById('deleteticketbutton').enabled = "true";
  document.getElementById('newticketbuttonbottom').enabled = "true";
  document.getElementById('savebuttonbottom').enabled = "true";
  document.getElementById('savennewbuttonbottom').enabled = "true";
  document.getElementById('deleteticketbuttonbottom').enabled = "true";
  <% }else if (ocStatus.equals(TicketConstantKeys.TK_OCSTATUS_CLOSE)) { %>
  document.getElementById('newticketbutton').disabled = "true";
  document.getElementById('savebutton').disabled = "true";
  document.getElementById('savennewbutton').disabled = "true";
  document.getElementById('deleteticketbutton').disabled = "true";
  document.getElementById('newticketbuttonbottom').disabled = "true";
  document.getElementById('savebuttonbottom').disabled = "true";
  document.getElementById('savennewbuttonbottom').disabled = "true";
  document.getElementById('deleteticketbuttonbottom').disabled = "true";
  document.getElementById('newticketbutton').className = "disabledButton";
  document.getElementById('savebutton').className = "disabledButton";
  document.getElementById('savennewbutton').className = "disabledButton";
  document.getElementById('deleteticketbutton').className = "disabledButton";
  document.getElementById('newticketbuttonbottom').className = "disabledButton";
  document.getElementById('savebuttonbottom').className = "disabledButton";
  document.getElementById('savennewbuttonbottom').className = "disabledButton";
  document.getElementById('deleteticketbuttonbottom').className = "disabledButton";
  <% } %>
</script>
</html:form>
