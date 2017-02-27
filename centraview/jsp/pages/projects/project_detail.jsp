<%
/**
 * $RCSfile: project_detail.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "com.centraview.projects.project.ProjectForm"%>
<%
ProjectForm projectForm = (ProjectForm)request.getAttribute("projectForm");
%>
<script language="JavaScript1.2">
<!--
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
      alert('You must select an Entity before selecting an Individual.');
      return false;
    }else{
      entityID = document.projectForm.entityid.value;
      c_lookup('Individual', entityID);
    }
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

  function detailNewWindow()
  {
    c_openPopup('/projects/view_project.do?rowId=<c:out value="${projectForm.projectid}" />');
  }
  function newProject()
  {
    c_openWindow('/projects/new_project.do', '', 780, 307, '');
  }
  function deleteProject()
  {
    var projectID = '<c:out value="${projectForm.projectid}" />';
    window.document.projectForm.action = "<html:rewrite page="/projects/delete_projectlist.do"/>?rowId=" + projectID;
    window.document.projectForm.submit();
  }
  function duplicate()
  {
    var projectID = '<c:out value="${projectForm.projectid}" />';
    c_openPopup("/projects/duplicate_project.do?rowId=" + projectID);
    return false;
  }
  function openPermissions()
  {
    c_openPermission('Projects', '<c:out value="${projectForm.projectid}" />', '<c:out value="${projectForm.title}" />');
  }

-->
</script>
<html:form action="/projects/save_edit_project" >
<html:hidden property="createdOn"/>
<html:hidden property="creatorName"/>
<html:hidden property="modifiedOn"/>
<html:hidden property="modifierName"/>
<html:hidden property="available"/>
<html:hidden property="usedHours"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="detailButtonHeader">
      <app:cvbutton property="cmpProject" styleClass="normalButton" onclick="newProject();">
        <bean:message key="label.new"/>
      </app:cvbutton>
      <app:cvbutton property="cmpdelete" styleClass="normalButton" onclick="deleteProject();">
        <bean:message key="label.delete"/>
      </app:cvbutton>
      <app:cvbutton property="cmpduplicate" styleClass="normalButton" onclick="duplicate();">
        <bean:message key="label.duplicate"/>
      </app:cvbutton>
      <app:cvbutton property="cmpproperties" styleClass="normalButton" onclick="openPermissions();">
        <bean:message key="label.properties"/>
      </app:cvbutton >
      <app:cvbutton property="cmphelp" styleClass="normalButton" onclick="c_openWindow('/help/centraview.htm', 'help', 700, 580, '');">
        <bean:message key="label.question"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<html:errors/>
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="321">
  <tr>
    <td valign="top" width="50%" style="border-right: #cccccc 1px solid;">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="detailHighlightHeader"><bean:message key="label.project.project"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="detailHighlightSection">
        <tr>
          <td class="labelCellBold"><bean:message key="label.project.title"/>:</td>
          <td class="contentCell">
            <html:text property="title" styleId="title" styleClass= "inputBox" size="44" />
            <html:hidden property="projectid" styleClass= "inputBox"/>
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.project.description"/>:</td>
          <td class="contentCell">
            <html:textarea property="description" rows="5" cols="44" styleClass="inputBox"/>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.project.entity"/>:</td>
          <td class="contentCell">
            <html:text property="entity"  styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.projectForm.entityid.value )" size="40" readonly="true"/>
            <app:cvbutton property="entityLookupButton" styleClass="normalButton"  statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Entity')">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
            <html:hidden property="entityid" styleClass= "inputBox"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.contact"/>:</td>
          <td class="contentCell">
            <html:text property="contact" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.projectForm.contactID.value )"  size="40" readonly="true"/>
            <app:cvbutton property="contactLookupButton" styleClass="normalButton" onclick="return openLookup()">
            <bean:message key="label.lookup"/>
            </app:cvbutton>
            <html:hidden property="contactID" styleClass= "inputBox"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.manager"/>:</td>
          <td class="contentCell">
            <html:text property="manager" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.projectForm.managerID.value )"  size="40" readonly="true"/>
            <app:cvbutton property="managerLookupButton" styleClass="normalButton"  onclick="c_lookup('Employee')">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
            <html:hidden property="managerID" styleClass= "inputBox"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.team"/>:</td>
          <td class="contentCell">
            <html:text property="team" styleClass= "inputBox" size="40" readonly="true"/>
            <app:cvbutton property="teamLookupButton" styleClass="normalButton" onclick="c_lookup('Group')">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
            <html:hidden property="teamID" styleClass= "inputBox"/>
         </td>
        </tr>
        <tr>
          <td  class="labelCell"> <bean:message key="label.project.created"/>:</td>
          <td class="contentCell">
            <c:out value="${projectForm.createdOn}"/>&nbsp;<c:out value="${projectForm.creatorName}"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"> <bean:message key="label.project.modified"/>:</td>
          <td class="contentCell">
            <c:out value="${projectForm.modifiedOn}"/>&nbsp;<c:out value="${projectForm.modifierName}"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell" colspan="2"><br><br><br></td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%">
      <table border="0" cellspacing="0" cellpadding="3" width="100%">
        <tr>
          <td class="sectionHeader" colspan="2"><bean:message key="label.project.projectstartendtime"/></td>
        </tr>
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
          <td class="labelCell"> <bean:message key="label.project.enddate"/>:</td>
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
          <td class="sectionHeader" colspan="2"><bean:message key="label.project.hours"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.budgeted"/>:</td>
          <td class="contentCell">
            <html:text property="budhr" styleClass="inputBox" size="8" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.used"/>:</td>
          <td class="contentCell">
            <c:out value="${projectForm.usedHours}"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.available"/>:</td>
          <td class="contentCell">
            <c:out value="${projectForm.available}"/>
          </td>
        </tr>
        <tr>
          <td class="sectionHeader"><bean:message key="label.project.customfields"/>
          </td>
          <td class="sectionHeader" align="right">
            <input name="button3" type="button" class="normalButton" onClick="changeRelatedInfo('CustomField');" value="<bean:message key='label.value.viewall'/>">
          </td>
        </tr>
      </table>
      <jsp:include page="/jsp/pages/common/custom_field.jsp">
        <jsp:param name="Operation" value="Edit"/>
        <jsp:param name="RecordType" value="Project"/>
        <jsp:param name="RecordId" value="<%=projectForm.getProjectid()%>"/>
      </jsp:include>
    </td>
  </tr>
  <tr>
    <td colspan="2"  class="sectionHeader" align="right">
      <app:cvsubmit property="save" styleClass="normalButton" statuswindow="label.save" tooltip="true" statuswindowarg= "label.general.blank">
        <bean:message key="label.save"/>
      </app:cvsubmit>
      <app:cvsubmit property="saveandclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvsubmit  property="saveandnew" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank">
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