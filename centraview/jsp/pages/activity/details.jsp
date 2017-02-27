<%--
 * $RCSfile: details.jsp,v $    $Revision: 1.7 $  $Date: 2005/09/21 16:32:51 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.activity.ConstantKeys" %>
<%@ page import="com.centraview.activity.helper.ActivityVO" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.activity.ActivityForm" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/fckeditor/fckeditor.js"></script>
<%
  String activityModuleName = "";
  //anyway ForecastSales and Tasks are not there in Activity-user rights implementation
  if (request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.CALL)) {
    activityModuleName = "Calls";
  }else if(request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.APPOINTMENT)){
    activityModuleName = "Appointments";
  }else if(request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.FORECASTSALE)){
    activityModuleName = "ForecastSales";
  }else if(request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.MEETING)){
    activityModuleName = "Meetings";
  }else if(request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.NEXTACTION)){
    activityModuleName = "NextActions";
  }else if(request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TODO)){
    activityModuleName = "ToDos";
  }else if(request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TASK)){
    activityModuleName = "Tasks";
  }
%>
<script language="javascript">
/**
 * Loads the FCKEditor
 */
window.onload = function()
{
  window.resizeTo(780, 660);
  var oFCKeditor = new FCKeditor('activityDetail', '100%', '230', 'cvActivity', '');
  oFCKeditor.ReplaceTextarea();
}

/**
 * Sets the Entity ID and name fields from the entity lookup.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
 * is called from the lookup screen, and must be general
 */
function setEntity(lookupValues)
{
  var entityIdField = document.getElementById('activityEntityID');
  var entityNameField = document.getElementById('activityEntityName');
  entityIdField.value = lookupValues.entID;
  entityNameField.value = lookupValues.entName;
}
/**
 * Sets the Opportunity AND Entity ID AND name fields from the
 * opportunity lookup. DO NOT CHANGE THE NAME OR SIGNATURE OF
 * THIS FUNCTION, as it is called from the lookup screen.
 */
function setOpp(lookupValues)
{
  document.getElementById('activityRelatedFieldID').value = lookupValues.ID;
  document.getElementById('activityRelatedFieldValue').value = lookupValues.Name;
}
function setProject(lookupValues)
{
  document.getElementById('activityRelatedFieldID').value = lookupValues.idValue;
  document.getElementById('activityRelatedFieldValue').value = lookupValues.Name;
}
/**
 * Sets the Individual ID and name fields from the individual lookup.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
 * is called from the lookup screen, and must be general
 */
function setIndividual(lookupValues)
{
  var individualIdField = document.getElementById('activityIndividualID');
  var individualNameField = document.getElementById('activityIndividualName');
  individualIdField.value = lookupValues.individualID;
  individualNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
}
/**
 * Sets the Owner ID and name fields from the employee lookup.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
 * is called from the lookup screen, and must be general
 */
function setEmployee(lookupValues)
{
  var ownerIdField = document.getElementById('activityOwnerID');
  var ownerNameField = document.getElementById('activityOwnerName');
  ownerIdField.value = lookupValues.individualID;
  ownerNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
}
/*
  This function is called from the SelectDateTime.do popup, which passes
  the date and time information from that popup back to this JSP. The code
  within this function can be modified to do whatever you need to do with
  the data on this page, including munging it, setting form properties, etc.
  BUT YOU SHOULD NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, WHATSOEVER!
*/
function setDateTimeFromPopup(startDate, endDate, startTime, endTime, optionalFlag)
{
  if (optionalFlag == 'remindMe') {
    // this sets the remind me date and time - otherwise we do start and end date/time
    if (startDate == null || startDate.length < 1){
      document.forms.activityform.activityReminderDate.value = "";
    } else {
      document.forms.activityform.activityReminderDate.value = startDate;
    }
    document.forms.activityform.activityReminderTime.value = startTime;
  } else {
    // set start and end date/time
    if (startDate == null || startDate.length < 1) {
      document.forms.activityform.activityStartDate.value = "";
    } else {
      document.forms.activityform.activityStartDate.value = startDate;
    }
    if (endDate == null || endDate.length < 1) {
      document.forms.activityform.activityEndDate.value = "";
    } else {
      document.forms.activityform.activityEndDate.value = endDate;
    }
    document.forms.activityform.activityStartTime.value = startTime;
    document.forms.activityform.activityEndTime.value = endTime;
    act_updateReminder();
  }
  return(true);
}
</script>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="50%" align="left" valign="top">
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.activity.title"/>:</td>
          <td class="contentCell"><html:text property="activityTitle" styleId="activityTitle" size="32" styleClass="inputBox" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.entity"/>:</td>
          <td class="contentCell" nowrap>
            <html:hidden property="activityEntityID" styleId="activityEntityID" />
            <app:cvtext property="activityEntityName" styleId="activityEntityName" styleClass="clickableInputBox" size="32" readonly="true" modulename="<%=activityModuleName%>" fieldname="entityVO" onclick="c_openPopup_FCI('Entity', document.getElementById('activityEntityID').value);" title="Open Entity Details" />
            <app:cvbutton property="changeLookup1" styleClass="normalButton" onclick="c_lookup('Entity');" modulename="<%=activityModuleName%>" fieldname="entityVO">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.individual"/>:</td>
          <td class="contentCell" nowrap>
            <html:hidden property="activityIndividualID" styleId="activityIndividualID" />
            <app:cvtext property="activityIndividualName" styleId="activityIndividualName" styleClass="clickableInputBox" size="32" readonly="true" modulename="<%=activityModuleName%>" fieldname="individualVO" onclick="c_openPopup_FCI('Individual', document.getElementById('activityIndividualID').value);" title="Open Individual Details" />
            <app:cvbutton property="changeLookup1" styleClass="normalButton" onclick="act_openIndividualLookup();" modulename="<%=activityModuleName%>" fieldname="individualVO">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.primarycontact"/>:</td>
          <td class="contentCell" nowrap>
            <app:cvtext property="activityIndividualPrimaryContact" styleId="activityIndividualPrimaryContact" styleClass="clickableInputBox" size="32" readonly="true" modulename="<%=activityModuleName%>" fieldname="individualprimarycontactVO"  title="Individual Primary Contact" />
            </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.relatedtype"/>:</td>
          <td class="contentCell" nowrap>
            <html:hidden property="activityRelatedTypeID" styleId="activityRelatedTypeID" />
            <html:select property="activityRelatedTypeValue" styleId="activityRelatedTypeValue" styleClass="inputBox" onchange="act_updateRelatedTypeID();">
              <html:option value="" >-- <bean:message key="label.activity.select"/> --</html:option>
              <html:option value="Opportunity"><bean:message key="label.activity.opportunity"/></html:option>
              <html:option value="Project"><bean:message key="label.activity.project"/></html:option>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.relatedfield"/>:</td>
          <td class="contentCell" nowrap>
            <html:hidden property="activityRelatedFieldID" styleId="activityRelatedFieldID" />
            <html:text property="activityRelatedFieldValue" styleId="activityRelatedFieldValue" styleClass="inputBox" size="32" readonly="true"/>
            <app:cvbutton property="changeLookup1" styleClass="normalButton" onclick="act_openRelatedFieldList();">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.owner"/>:</td>
          <td class="contentCell" nowrap>
            <html:hidden property="activityOwnerID" styleId="activityOwnerID" />
            <html:text property="activityOwnerName" styleId="activityOwnerName" styleClass="clickableInputBox" size="32" readonly="true" onclick="c_openPopup_FCI('Individual', document.getElementById('activityOwnerID').value);" title="View Owner Details" />
            <app:cvbutton property="detaillookup" styleClass="normalButton"  onclick="c_lookup('Employee');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" align="left" valign="top">
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.activity.start"/>:</td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td>
                  <html:text property="activityStartDate" styleId="activityStartDate" styleClass="inputBox" maxlength="10" size="10" onchange="act_updateReminder();"/>
                </td>
                <td>
                  <a class="plainLink" onclick="act_openSelectDateTime();">
                    <html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" />
                  </a>
                </td>
                <td>
                  <app:cvtext property="activityStartTime" styleId="activityStartTime" styleClass="inputBox" size="8" maxlength="8" modulename="<%=activityModuleName%>" fieldname="activityStartDate" onchange="act_updateReminder();"/>
                  <a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_reminder.gif" width="19" height="19" border="0" alt="" align="absmiddle" /></a>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.end"/>:</td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td>
                  <html:text property="activityEndDate" styleId="activityEndDate" styleClass="inputBox" maxlength="10" size="10" onchange="act_updateReminder();"/>
                </td>
                <td><a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" /></a></td>
                <td>
                  <app:cvtext property="activityEndTime" styleId="activityEndTime" styleClass="inputBox" size="8" maxlength="8" modulename="<%=activityModuleName%>" fieldname="activityEndDate" onchange="act_updateReminder();"/>
                  <a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_reminder.gif" width="19" height="19" border="0" alt="" align="absmiddle" /></a>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.priority"/>:</td>
          <td class="contentCell">
            <html:radio property="activityPriority" value="3"/> <bean:message key="label.activity.low"/> &nbsp;&nbsp;&nbsp;
            <html:radio property="activityPriority" value="2"/> <bean:message key="label.activity.medium"/> &nbsp;&nbsp;&nbsp;
            <html:radio property="activityPriority" value="1"/> <bean:message key="label.activity.high"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.visibility"/>:</td>
          <td class="contentCell">
            <html:radio property="activityVisibility" value="PRIVATE"/> <bean:message key="label.activity.private"/> &nbsp;&nbsp;&nbsp;
            <html:radio property="activityVisibility" value="PUBLIC"/> <bean:message key="label.activity.public"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.status"/>:</td>
          <td class="contentCell">
            <%
              Collection status = new ArrayList();
              status.add(new DDNameValue("1", "Pending"));
              status.add(new DDNameValue("2", "Completed"));
              pageContext.setAttribute("status", status, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="activityStatus" styleClass="inputBox" modulename="<%=activityModuleName%>" fieldname="status">
              <app:cvoptions collection="status" property="strid" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
      </table>
      <!-- Dont't forget that there's javascript to show/hide this
           TR row based on if the user selects "Call" or not -->
      <%
        Collection calltype = new ArrayList();
        calltype.add(new DDNameValue("1", "Incoming"));
        calltype.add(new DDNameValue("2", "Outgoing"));
        pageContext.setAttribute("calltype", calltype, PageContext.PAGE_SCOPE);

        String showHideCall = "none";
        if (activityModuleName != null && activityModuleName.equals("Calls")) {
          showHideCall = "block";
        }
      %>
      <table name="callBox" id="callBox" style="display:<%=showHideCall%>;">
        <tr>
          <td class="labelCell" width="71"><bean:message key="label.activity.calltype"/>:</td>
          <td class="contentCell">
            <html:select property="activityCallType" styleClass="inputBox">
              <html:options collection="calltype" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.activity.notes"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable" width="100%">
  <tr>
    <td class="contentCell">
      <app:cvtextarea property="activityDetail" rows="12" cols="120" styleClass="inputBox" modulename="<%=activityModuleName%>" fieldname="activityDetails" style="width:670px;"/>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.activity.options"/></td>
  </tr>
</table>
<input type="hidden" name="remindcheckbox" id="remindcheckbox">
<input type="hidden" name="emailcheckbox" id="emailcheckbox">
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell">
      <html:checkbox property="activityReminder" styleClass="checkBox" onclick="act_updateReminder();"/> <bean:message key="label.activity.remindme"/>
    </td>
    <td class="contentCell">
      <table border="0" cellspacing="0" cellpadding="1">
        <tr>
          <td>
            <html:text property="activityRemindDate" styleClass="inputBox" size="10" maxlength="10"/>
          </td>
          <td><a class="plainLink" onclick="act_openSelectDateTime('remindMe');"><html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" /></a></td>
          <td>
            <html:text property="activityReminderTime" styleClass="inputBox" size="8" maxlength="8"/>
            <a class="plainLink" onclick="act_openSelectDateTime('remindMe');"><html:img page="/images/icon_reminder.gif" width="19" height="19" border="0" alt="" align="absmiddle" /></a>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <% if (! request.getAttribute(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TODO)){ %>
  <tr>
    <td class="labelCell">
      <html:checkbox property="activityEmailInvitation"/> <bean:message key="label.activity.sendemailinvitations"/>
    </td>
    <td class="contentCell">&nbsp;</td>
  </tr>
  <% } %>
</table>
<script>
<%
  String oppName = request.getParameter("oppName");
  String oppID = request.getParameter("oppID");

  if (oppID != null && oppName != null) {
    %>
    document.forms[0].activityRelatedFieldID.value =  <%=oppID%>;
    document.forms[0].activityRelatedFieldValue.value = <%=oppName%>;
    document.forms[0].activityRelatedTypeValue.selectedIndex = 1;
    document.forms[0].activityRelatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_OPPORTUNITY%>;
    <%
  }
%>
</script>