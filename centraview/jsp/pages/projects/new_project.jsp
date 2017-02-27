<%--
 * $RCSfile: new_project.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:errors/>
<script language="JavaScript1.2">
  function popupCalendar()
  {
    var startDate = document.forms.projectForm.startmonth.value + "/" + document.forms.projectForm.startday.value  + "/" + document.forms.projectForm.startyear.value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = document.forms.projectForm.endmonth.value + "/" + document.forms.projectForm.endday.value + "/" + document.forms.projectForm.endyear.value;
    if (endDate == "//"){ endDate = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate, 'selectDateTime', 350, 500, '');
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
      document.forms.projectForm.startmonth.value = "";
      document.forms.projectForm.startday.value = "";
      document.forms.projectForm.startyear.value = "";
    }else{
      document.forms.projectForm.startmonth.value = startDateArray[0];
      document.forms.projectForm.startday.value = startDateArray[1];
      document.forms.projectForm.startyear.value = startDateArray[2];
    }

    var endDateArray = endDate.split("/")
    if (endDateArray == null || endDateArray.length < 3){
      document.forms.projectForm.endmonth.value = "";
      document.forms.projectForm.endday.value = "";
      document.forms.projectForm.endyear.value = "";
    }else{
      document.forms.projectForm.endmonth.value = endDateArray[0];
      document.forms.projectForm.endday.value = endDateArray[1];
      document.forms.projectForm.endyear.value = endDateArray[2];
    }
    return(true);
  }

  function openLookup(typeOfLookup)
  {
    if (document.projectForm.entityid.value == "" && document.projectForm.entityid.value <= 0)
    {
      alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.");
      return false;
    }else{
      entityID = document.projectForm.entityid.value;
      c_lookup('Individual', entityID);
    }
  }

  function setIndividual(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.projectForm.contactID.value = id;
    document.projectForm.contact.readonly = false;
    document.projectForm.contact.value = firstName+" "+middleName+" "+lastName;
    document.projectForm.contact.readonly = true;
  }

  function setGrp(groupLookupValues)
  {
	name = groupLookupValues.Name;
	id = groupLookupValues.idValue;
    document.projectForm.team.readonly = false;
    document.projectForm.team.value = name;
    document.projectForm.teamID.value = id;
    document.projectForm.team.readonly = true;
  }

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.projectForm.entity.readonly = false;
    document.projectForm.entity.value = name;
    document.projectForm.entityid.value = id;
    document.projectForm.entity.readonly = true;
  }

  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.projectForm.manager.readonly = false;
    document.projectForm.managerID.value = id;
    document.projectForm.manager.value = firstName+" "+middleName+" "+lastName;
    document.projectForm.manager.readonly = true;
  }

  <%
  if (request.getAttribute("refreshWindow") != null && request.getAttribute("refreshWindow").equals("true"))
  {
  %>
    window.opener.location.href = window.opener.location.href;
  <%
  }

  if (request.getAttribute("closeWindow") != null && request.getAttribute("closeWindow").equals("true"))
  {
  %>
    window.close();
  <%
  }
  %>
</script>

<app:TitleWindow moduleName="<bean:message key='label.modulename.projectsnewproject'/>"/>
<html:form action="/projects/save_new_project">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <!-- Left content area -->
    <td>
      <table width="100%" border="0">
        <tr>
          <td>
            <table width="100%" border="0" cellpadding="0">
              <tr>
                <td class="labelCell"><bean:message key="label.project.title"/>:</td>
                <td class="contentCell">
                  <html:text property="title" styleId="title" styleClass="inputBox"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.description"/>:</td>
                <td class="contentCell">
                  <html:textarea property="description" styleClass="inputBox" cols="49" rows="10"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.entity"/>:</td>
                <td class="contentCell">
                  <html:hidden property="entityid" />
                  <html:text property="entity" readonly="true" styleClass="inputBox"/>
                  <app:cvbutton property="entityLookupButton" styleClass="normalButton"  statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Entity');">
                    <bean:message key="label.lookup"/>
                  </app:cvbutton>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.contact"/>:</td>
                <td class="contentCell">
                  <html:hidden property="contactID" styleClass="inputBox"/>
                  <html:text property="contact" readonly="true" styleClass="inputBox"/>
                  <app:cvbutton property="contactLookupButton" styleClass="normalButton" onclick="return openLookup('Individual')">
                    <bean:message key="label.lookup"/>
                  </app:cvbutton>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.manager"/>:</td>
                <td class="contentCell">
                <html:hidden property="managerID" />
                <html:text property="manager" readonly="true" styleClass="inputBox"/>
                <app:cvbutton property="managerLookupButton" styleClass="normalButton"  onclick="c_lookup('Employee');">
                  <bean:message key="label.lookup"/>
                </app:cvbutton>

                </td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.team"/>:</td>
                <td class="contentCell">
                  <html:text property="team" readonly="true" styleClass="inputBox"/>
                  <app:cvbutton property="teamLookupButton" styleClass="normalButton" onclick="c_lookup('Group');">
                    <bean:message key="label.lookup"/>
                  </app:cvbutton>
                  <html:hidden property="teamID" styleClass="inputBox"/>
                </td>
              </tr>
            </table>
          </td>
          <td valign="top" align="left" width="50%">
            <table width="100%" border="0" cellpadding="0">
              <tr>
                <td class="labelCell"><bean:message key="label.project.startdate"/>:</td>
                <td class="contentCell">
                   <html:text property="startmonth" styleClass="inputBox" size="2" maxlength="2"/>
                   /
                   <html:text property="startday" styleClass="inputBox" size="2" maxlength="2"/>
                   /
                   <html:text property="startyear" styleClass="inputBox" size="4" maxlength="4"/>
                   <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" onclick="popupCalendar();"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.enddate"/>:</td>
                <td class="contentCell">
                  <html:text property="endmonth" styleClass="inputBox" size="2" maxlength="2"/>
                  /
                  <html:text property="endday" styleClass="inputBox" size="2" maxlength="2"/>
                  /
                  <html:text property="endyear" styleClass="inputBox" size="4" maxlength="4"/>
                  <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" onclick="popupCalendar();"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.status"/>:</td>
                <td class="contentCell">
                    <html:select property="status" styleClass="inputBox" >
                      <html:option value="">-- <bean:message key="label.project.select"/> --</html:option>
                      <html:optionsCollection  property="projectStatusVec" value="statusID" label="title" />
                    </html:select>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.project.budgetedhours"/>:</td>
                <td class="contentCell">
                  <html:text property="budhr" styleClass="inputBox" maxlength="5"/>
                </td>
              </tr>
            </table>
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr height="17">
                <td class="sectionHeader"><bean:message key="label.project.customfields"/></td>
                </tr>
            </table>
            <table width="100%" border="0" cellpadding="0">
              <jsp:include page="CustomField_c.jsp">
              <jsp:param name="Operation" value="Add"/>
              <jsp:param name="RecordType" value="Project"/>
              </jsp:include>
            </table>
        </td>
      </tr>
    </table>
    </td>
  </tr>
  <tr>
    <td class="sectionHeader">
        <app:cvsubmit property="saveandclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" >
          <bean:message key="label.saveandclose"/>
        </app:cvsubmit>

        <app:cvsubmit  property="saveandnew" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" >
          <bean:message key="label.saveandnew"/>
        </app:cvsubmit>

        <app:cvreset  styleClass="normalButton"  statuswindow="label.resetfields" tooltip="true" statuswindowarg= "label.general.blank">
          <bean:message key="label.resetfields"/>
        </app:cvreset>

        <html:cancel  styleClass="normalButton" onclick="window.close();" >
          <bean:message key="label.cancel"/>
        </html:cancel>
    </td>
  </tr>
</table>
</html:form>