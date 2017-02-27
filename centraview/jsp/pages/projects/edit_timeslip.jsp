<%
/**
 * $RCSfile: edit_timeslip.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
 */
%>


<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" %>

<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
	  response.setHeader("Pragma","no-cache");
	  response.setHeader("Cache-Control","no-cache");
	  response.setDateHeader("Expires",0);

	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
	  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
%>
<script language="JavaScript1.2">


  function popupCalendar()
  {
    var startDate = document.forms.timeSlipForm.month.value + "/" + document.forms.timeSlipForm.day.value  + "/" + document.forms.timeSlipForm.year.value;
    if (startDate == "//"){ startDate = ""; }
    var startTime = document.forms.timeSlipForm.startTime.value;
    var endTime = document.forms.timeSlipForm.endTime.value;
    c_openWindow('/calendar/select_date_time.do?dateTimeType=4&startDate='+startDate+'&startTime='+startTime+'&endTime='+endTime, 'selectDateTime', 350, 500, '');
  }

  /*
    This function is called from the SelectDateTime.do popup, which passes
    the date and time information from that popup back to this JSP. The code
    within this function can be modified to do whatever you need to do with
    the data on this page, including munging it, setting form properties, etc.
    BUT YOU SHOULD NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, WHATSOEVER!
  */
  function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
  {
    var startDateArray = startDate.split("/")
    if (startDateArray == null || startDateArray.length < 3){
      document.forms.timeSlipForm.month.value = "";
      document.forms.timeSlipForm.day.value = "";
      document.forms.timeSlipForm.year.value = "";
    }else{
      document.forms.timeSlipForm.month.value = startDateArray[0];
      document.forms.timeSlipForm.day.value = startDateArray[1];
      document.forms.timeSlipForm.year.value = startDateArray[2];
    }

    document.forms.timeSlipForm.startTime.value = startTime;
    document.forms.timeSlipForm.endTime.value = endTime;
    return(true);
  }

  function fnTask()
  {
    c_lookup('Tasks',document.timeSlipForm.projectID.value,0);
  }

  function setProject(projectLookupValues)
  {
    name = projectLookupValues.Name;
    id = projectLookupValues.idValue;
    document.timeSlipForm.project.value = name;
    document.timeSlipForm.projectID.value = id;
    document.timeSlipForm.reference.value = name;
    document.timeSlipForm.reference.readonly = true;
    document.timeSlipForm.task.value = "";
    document.timeSlipForm.activityID.value = "0";
    document.timeSlipForm.ticket.value = "";
    document.timeSlipForm.ticketID.value = "0";
    document.timeSlipForm.TaskLookupButton.disabled = false;
  }

  function setParentTask(taskLookupValues)
  {
    name = taskLookupValues.Name;
    id = taskLookupValues.idValue;
    document.timeSlipForm.task.value = name;
    document.timeSlipForm.activityID.value = id;
  }

  function setTicket(ticketLookupValues)
  {
    name = ticketLookupValues.Name;
    id = ticketLookupValues.idValue;
    document.timeSlipForm.ticketID.value = id;
    document.timeSlipForm.ticket.value = name;
    document.timeSlipForm.reference.value = name;
    document.timeSlipForm.reference.readonly = true;
    document.timeSlipForm.project.value = "";
    document.timeSlipForm.projectID.value = "0";
    document.timeSlipForm.task.value = "";
    document.timeSlipForm.activityID.value = "0";
  }

  function onChangeReference(){
    document.timeSlipForm.reference.value = "";
    document.timeSlipForm.reference.readonly = true;
    document.timeSlipForm.task.value = "";
    document.timeSlipForm.activityID.value = "0";
    document.timeSlipForm.project.value = "";
    document.timeSlipForm.projectID.value = "0";
    document.timeSlipForm.TaskLookupButton.disabled = true;
    document.timeSlipForm.ticket.value = "";
    document.timeSlipForm.ticketID.value = "0";
  }

  function fnLookup(){

    lookupValue = document.timeSlipForm.lookupList.value;
    if (lookupValue == "3"){
      alert ("<bean:message key='label.alert.selectreferencetype'/>");
    }
    if (lookupValue == "1"){
      c_lookup('Project');
    }
    if (lookupValue == "2"){
      c_lookup('Ticket');
    }

  }

  function gotoPermission()
  {
    var id = document.timeSlipForm.timeSlipID.value;
    var description = document.timeSlipForm.description.value;
    c_openPermission('Timeslips', id, description)
  }
-->
</script>
<html:form action="/projects/save_edit_timeslip">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td valign="top" width="50%">
      <table cellspacing="0" cellpadding="3">
        <tr>
          <td class="labelCell"><bean:message key="label.project.referencetype"/>:</td>
          <td class="contentCell">
            <html:select property="lookupList" styleId="lookupList" styleClass="inputBox" onclick="onChangeReference()">
              <html:option value="3">-- <bean:message key="label.project.select"/> --</html:option>
              <html:option value="1"><bean:message key="label.project.project"/></html:option>
              <html:option value="2"><bean:message key="label.project.ticket"/></html:option>
            </html:select>
            <html:hidden property="ticketID" />
            <html:hidden property="ticket" />
            <html:hidden property="project" />
            <html:hidden property="projectID" />
            <html:hidden property="timeSlipID"/>
            <html:hidden property="timesheetID" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.reference"/>:</td>
          <td class="contentCell">
            <html:text property="reference" styleClass="inputBox" size="30" readonly="true"/>
            <app:cvbutton property="LookupButton" styleClass="normalButton" onclick="fnLookup()" disabled="false"><bean:message key='label.project.lookup'/>
            </app:cvbutton>
        </td>
        </tr>

        <tr>
          <td class="labelCell"><bean:message key="label.project.task"/>:</td>
          <td class="contentCell">
            <html:text property="task" styleClass="inputBox" size="30" readonly="true"/>
            <app:cvbutton property="TaskLookupButton" styleClass="normalButton" onclick="fnTask()" disabled="false"><bean:message key='label.project.lookup'/>
            </app:cvbutton>
            <html:hidden property="activityID" styleClass="inputBox"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.description"/>:</td>
          <td class="contentCell"><html:textarea property="description" cols="38" rows="5" styleClass="inputBox"></html:textarea></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.duration"/>:</td>
          <td class="contentCell"><html:text property="duration" styleClass="inputBox" size="25" readonly="true"></html:text></td>
        </tr>

      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td class="labelCell"><bean:message key="label.project.date"/>:</td>
          <td class="contentCell">
              <html:text property="month" styleClass="inputBox" size="2" />
              /
              <html:text property="day" styleClass="inputBox" size="2" />
              /
              <html:text property="year" styleClass="inputBox" size="4" />
              &nbsp;
              <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" style="cursor:pointer;" onclick="popupCalendar()"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.starttime"/>:</td>
          <td class="contentCell">
            <html:text property="startTime" styleClass="inputBox" style="width:7em;" />
            <html:img page="/images/icon_reminder.gif" width="17" height="17" align="absmiddle" style="cursor:pointer;" onclick="popupCalendar();"/>
         </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.endtime"/>:</td>
          <td class="contentCell">
           <html:text property="endTime" styleClass="inputBox" style="width:7em;" />
           <html:img page="/images/icon_reminder.gif" width="17" height="17" align="absmiddle" style="cursor:pointer;" onclick="popupCalendar();"/>
          </td>
        </tr>
        <tr>
          <td valign="top" class="labelCell"><bean:message key="label.project.totalbreaktime"/>:</td>
          <td valign="top" class="contentCell">
            <html:select property="breakHours" styleClass="inputBox">
              <html:option value="0">0</html:option>
              <html:option value="1">1</html:option>
              <html:option value="2">2</html:option>
              <html:option value="3">3</html:option>
              <html:option value="4">4</html:option>
              <html:option value="5">5</html:option>
              <html:option value="6">6</html:option>
              <html:option value="7">7</html:option>
              <html:option value="8">8</html:option>
            </html:select>
            <bean:message key="label.project.hours"/>
            <html:select property="breakMinutes" styleClass="inputBox">
              <html:option value="0">0</html:option>
              <html:option value="5">5</html:option>
              <html:option value="10">10</html:option>
              <html:option value="15">15</html:option>
              <html:option value="20">20</html:option>
              <html:option value="25">25</html:option>
              <html:option value="30">30</html:option>
              <html:option value="35">35</html:option>
              <html:option value="40">40</html:option>
              <html:option value="45">45</html:option>
              <html:option value="50">50</html:option>
              <html:option value="55">55</html:option>
            </html:select>
            <bean:message key="label.project.minutes"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="sectionHeader" colspan="2" align="left">
      <app:cvsubmit property="saveandclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="false" statuswindowarg= "View TimeSlip" >
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>

      <app:cvsubmit property="saveandnew" styleClass="normalButton"  statuswindow="label.saveandnew" tooltip="false" statuswindowarg= "New TimeSlip" >
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>

      <app:cvreset  styleClass="normalButton"  statuswindow="label.resetfields" tooltip="false" statuswindowarg= "New TimeSlip">
        <bean:message key="label.resetfields"/>
      </app:cvreset>

      <app:cvsubmit  styleClass="normalButton"  onclick="window.close();" statuswindow="label.cancel" tooltip="false" statuswindowarg= "New TimeSlip">
        <bean:message key="label.cancel"/>
      </app:cvsubmit>

      <app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
        <bean:message key="label.project.permissions"/>
      </app:cvbutton>

    </td>
  </tr>
</table>
<app:TitleWindow moduleName="<bean:message key='label.modulename.projectsedittimeslip'/>"/>
</html:form>