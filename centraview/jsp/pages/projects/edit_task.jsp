<%--
 * $RCSfile: edit_task.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.*, com.centraview.projects.task.*, com.centraview.contact.individual.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.centraview.common.*" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%
String taskID = (String)request.getAttribute("taskid");
%>
<script language="javascript">
<!--
  function gotoPermission()
  {
    c_openPermission('Tasks', '<c:out value="${taskForm.taskid}"/>', '<c:out value="${taskForm.title}"/>');
  }

  function deletetask()
  {
    var id = <c:out value="${taskForm.taskid}"/>;
    window.document.taskForm.action="<html:rewrite page='/projects/delete_tasklist.do'/>?rowId="+id;
    window.document.taskForm.submit();
    window.opener.location.reload();
    window.self.close();
    return false;
  }

  function opensubtaskwindow()
  {
    taskid = <c:out value="${taskForm.taskid}"/>;
    taskname = document.taskForm.title.value;
    projectid = document.taskForm.projectid.value;
    projectname = document.taskForm.project.value;
    window.location.href="<html:rewrite page='/projects/new_task.do'/>?taskid="+taskid+ "&taskname="+ taskname + "&projectid=" + projectid + "&projectname=" + projectname+ "&listId="+taskid
    return false;
  }


  function popupCalendar()
  {
    var startDate = document.forms.taskForm.startmonth.value + "/" + document.forms.taskForm.startday.value  + "/" + document.forms.taskForm.startyear.value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = document.forms.taskForm.endmonth.value + "/" + document.forms.taskForm.endday.value + "/" + document.forms.taskForm.endyear.value;
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
      document.forms.taskForm.startmonth.value = "";
      document.forms.taskForm.startday.value = "";
      document.forms.taskForm.startyear.value = "";
    }else{
      document.forms.taskForm.startmonth.value = startDateArray[0];
      document.forms.taskForm.startday.value = startDateArray[1];
      document.forms.taskForm.startyear.value = startDateArray[2];
    }

    var endDateArray = endDate.split("/")
    if (endDateArray == null || endDateArray.length < 3){
      document.forms.taskForm.endmonth.value = "";
      document.forms.taskForm.endday.value = "";
      document.forms.taskForm.endyear.value = "";
    }else{
      document.forms.taskForm.endmonth.value = endDateArray[0];
      document.forms.taskForm.endday.value = endDateArray[1];
      document.forms.taskForm.endyear.value = endDateArray[2];
    }
    return(true);
  }

  var lookuptype = "";

  function deleteAssignedTo()
  {
    var len = document.taskForm.assignedTo.options.length;
    if(len==0)
    {
      alert("<bean:message key='label.alert.nothingtodelete'/>");
      return false;
    }

    var counter =0;
    for(i=0;i<document.taskForm.assignedTo.options.length;i++)
    {
      if(document.taskForm.assignedTo.options[i].selected)
      {
        counter++;
      }
    }
    if(counter==0)
    {
      alert("<bean:message key='label.alert.selectmembertodelete'/>!");
      return false;
    }

    for(j=0;j<document.taskForm.assignedTo.options.length;j++)
    {
      if(document.taskForm.assignedTo.options[j].selected)
      {
        document.taskForm.assignedTo.options[j] = null;
        j--;
      }
    }
  }

  function deleteSentTo()
  {

    var len = document.taskForm.sendTo.options.length;
    if(len==0)
    {
      alert("<bean:message key='label.alert.nothingtodelete'/>");
      return false;
    }

    var counter =0;
    for(i=0;i<document.taskForm.sendTo.options.length;i++)
    {
      if(document.taskForm.sendTo.options[i].selected)
      {
        counter++;
      }
    }
    if(counter==0)
    {
      alert("<bean:message key='label.alert.selectcontacttodelet'/>!");
      return false;
    }

    for(i=0;i<document.taskForm.sendTo.options.length;i++)
    {
      if(document.taskForm.sendTo.options[i].selected)
      {
        document.taskForm.sendTo.options[i] = null;
        i--;
      }
    }
  }

  function resetSendTo()
  {
    for(j=0;j<document.taskForm.assignedTo.options.length;j++)
    {
      document.taskForm.assignedTo.options[j].selected=true;
      document.taskForm.assignedTo.options[j] = null;
    }
    for(i=0;i<document.taskForm.sendTo.options.length;i++)
    {
      document.taskForm.sendTo.options[i] = null;
    }
  }

  function populateCheckBox() {
    if (window.document.forms[0].alertTypeEmail != null)  {
      if (window.document.forms[0].alertTypeEmail.checked)  {
        window.document.forms[0].alertTypeEmail.value = "on";
        window.document.forms[0].alertTypeEmail.checked = true;
        window.document.forms[0].alertTypeEmail.value = "on";
      }
      else  {
        window.document.forms[0].alertTypeEmail.value = "off";
        window.document.forms[0].alertTypeEmail.checked = false;
        window.document.forms[0].alertTypeEmail.value = "off";
      }
    }
    if (window.document.forms[0].alertTypeAlert != null)  {
      if (window.document.forms[0].alertTypeAlert.checked)  {
        window.document.forms[0].alertTypeAlert.value = "on";
        window.document.forms[0].alertTypeAlert.checked = true;
        window.document.forms[0].alertTypeAlert.value = "on";
      }
      else  {
        window.document.forms[0].alertTypeAlert.value = "off";
        window.document.forms[0].alertTypeAlert.checked = false;
        window.document.forms[0].alertTypeAlert.value = "off";
      }
    }
  }

  function assigned()
  {
    populateCheckBox();
    for(i=0;i<document.taskForm.assignedTo.options.length;i++)
    {
      document.taskForm.assignedTo.options[i].readonly=true;
      document.taskForm.assignedTo.options[i].selected=true;
      document.taskForm.assignedTodummy.value += document.taskForm.assignedTo.options[i].value+','+document.taskForm.assignedTo.options[i].text+',';
    }
    for(i=0;i<document.taskForm.sendTo.options.length;i++)
    {
      document.taskForm.sendTo.options[i].readonly=true;
      document.taskForm.sendTo.options[i].selected=true;
      document.taskForm.sendtodummy.value += document.taskForm.sendTo.options[i].value+','+document.taskForm.sendTo.options[i].text+',';
    }
  }

  function openLookup(typeOfLookup, type)
  {
    lookuptype = typeOfLookup;
    if (typeOfLookup == 'projectid')
    {
       c_lookup('Project');
    }
    else if (typeOfLookup == 'parenttask')
    {
      if (document.taskForm.projectid.value == "" && document.taskForm.projectid.value <= 0)
      {
        alert("<bean:message key='label.alert.selectproject'/>");
        return false;
      }
      c_lookup('Tasks',document.taskForm.projectid.value,<c:out value="${taskForm.taskid}"/>);
    }
    else if (typeOfLookup == 'individual')
    {
      c_openWindow('contacts/individual_lookup.do', '', 460, 500,'');
    }
    else if (typeOfLookup == 'addmembers')
    {
      c_lookup('attendee_lookup');
    }
    else if(typeOfLookup == 'addcontact')
    {
      c_lookup('attendee_lookup');
    }
    else if(typeOfLookup == 'employee')
    {
      c_lookup('Employee', type);
    }
    return false;
  }

  function setView(individualLookupValues)
  {
    data = individualLookupValues.NameIDValue;
    commaDataValues = data.split(",");
    for(i=0; i < commaDataValues.length; i++){
        dataValues = commaDataValues[i].split("&");
        if (lookuptype == 'addmembers')
        {
          if(checkAssinedto(dataValues[0]))
          {
            document.taskForm.assignedTo.options[document.taskForm.assignedTo.options.length] = new Option(dataValues[1],dataValues[0])
          }
          else
          {
            alert(dataValues[1]+"<bean:message key='label.alert.alreadyadded'/>");
          }
        }
        if(lookuptype == 'addcontact')
        {
          if(checkSendTo(dataValues[0]))
          {
            document.taskForm.sendTo.options[document.taskForm.sendTo.options.length] = new Option(dataValues[1],dataValues[0])
          }
          else
          {
            alert(dataValues[1]+"<bean:message key='label.alert.alreadyadded'/>");
          }
        }

    }

  }

	function setEmployee(lookupValues, type) {
		var id = lookupValues.individualID;
		var name = lookupValues.firstName+" "+lookupValues.middleName+" "+lookupValues.lastName;
		var title = lookupValues.title;

		if (type == 'manager') {
			document.taskForm.manager.readonly = false;
			document.taskForm.manager.value = name;
			document.taskForm.managerID.value = id;
			document.taskForm.manager.readonly = true;
		} else if (type == 'assignedTo') {
			if (checkAssinedto(id)) {
				document.taskForm.assignedTo.options[document.taskForm.assignedTo.options.length] = new Option(name, id)
			} else {
				alert(name+" <bean:message key='label.alert.alreadyadded'/>");
			}
		} else {
			return(false);
		}

  }

  function checkSendTo(id)
  {
    for(i=0;i<document.taskForm.sendTo.options.length;i++)
    {
      if(document.taskForm.sendTo.options[i].value == id)
      {
        return false;
      }
    }
    return true;
  }

  function checkAssinedto(id)
  {
    for(j=0;j<document.taskForm.assignedTo.options.length;j++)
    {
      if(document.taskForm.assignedTo.options[j].value == id)
      {
        return false;
      }
    }
    return true;
  }

  function setProject(projectLookupValues)
  {
	name = projectLookupValues.Name;
	id = projectLookupValues.idValue;
    document.taskForm.project.readonly = false;
    document.taskForm.project.value = name;
    document.taskForm.projectid.value = id;
    document.taskForm.project.readonly = true;
  }

  function setParentTask(taskLookupValues)
  {
  	name = taskLookupValues.Name;
	id = taskLookupValues.idValue;
    document.taskForm.parentTask.readonly = false;
    document.taskForm.parentTask.value = name;
    document.taskForm.parenttaskid.value = id;
    document.taskForm.parentTask.readonly = true;
  }
-->
</script>

<html:form action="/projects/save_edit_task">
<html:hidden property="taskid"/>

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr>
    <td valign="top" colspan="2" width="100%">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.project.tasksummary"/></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td valign="top" width="50%">
      <table border="0" cellspacing="0" cellpadding="2" width="100%">
          <tr>
            <td class="labelCell"><bean:message key="label.project.title"/>:</td>
            <td class="contentCell">
              <html:text property="title"  styleId="title" styleClass= "inputBox" />
              <html:hidden property="taskid" />
            </td>
          </tr>
          <tr>
            <td class="labelCell">
              <bean:message key="label.project.id"/>:
            </td>
            <td class="contentCell"><%=request.getAttribute("projecttaskcount")%></td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.description"/>: </td>
            <td class="contentCell">
             <app:cvtextarea property="description" styleClass= "inputBox" cols="44" rows="5" modulename="Tasks" fieldname="description"/>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.project"/>: </td>
            <td class="contentCell">
              <html:hidden property="projectid" />
              <html:text property="project" readonly="false" styleClass="inputBox"/>
              <app:cvbutton property="projectLookup" styleClass="normalButton" style="width:60px;" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('projectid')">
               <bean:message key="label.lookup"/>
              </app:cvbutton>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.parenttasks"/>: </td>
            <td class="contentCell">
              <html:hidden property="parenttaskid" />
              <html:text property="parentTask" readonly="true" styleClass="inputBox" />
              <app:cvbutton property="parentTaskLookup" styleClass="normalButton" style="width:60px;" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('parenttask')">
                <bean:message key="label.lookup"/>
              </app:cvbutton>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.crumbs"/>:</td>
            <td class="contentCell">
              <a href="<html:rewrite page='/projects/view_project.do'/>?rowId=<c:out value='${taskForm.projectid}' />&listId" class="plainLink"><c:out value='${taskForm.project}' /></a>

              <c:forEach var="crumbs" items="${requestScope.crumbs}">
                : <a href="<html:rewrite page='/projects/view_task.do'/>?rowId=<c:out value='${crumbs.key}'/>" class="plainLink"><c:out value='${crumbs.value}'/></a>
              </c:forEach>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.milestone"/>: </td>
            <td class="contentCell">
              <app:cvradio property="milestone" onclick="showLayer(this);" value="Yes" modulename="Tasks" fieldname="milestone"/>
              <bean:message key="label.project.yes"/>&nbsp;&nbsp;&nbsp;
              <app:cvradio property="milestone" value="No" modulename="Tasks" fieldname="milestone"/>
              <bean:message key="label.project.no"/>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="labelCell">
              <div id="milestoneAlerts" name="milestoneAlerts" style="display:block;">
                <table border="0" cellpadding="2" cellspacing="0" width="100%">
                  <tr>
                    <td class="labelCell"><bean:message key="label.project.sendalert"/>: </td>
                    <td colspan="2" class="contentCell">
                      <html:radio property="sendAlert" value="Yes"/>
                      <bean:message key="label.project.yes"/>&nbsp;&nbsp;
                      <html:radio property="sendAlert" value="No"/>
                      <bean:message key="label.project.no"/>
                    </td>
                  </tr>
                  <tr>
                    <td class="labelCell"><bean:message key="label.project.alerttype"/>: </td>
                    <td colspan="2" class="contentCell">
                      <html:checkbox property="alertTypeAlert" />
                      <bean:message key="label.project.alert"/>
                      <html:checkbox property="alertTypeEmail" />
                      <bean:message key="label.project.email"/>
                    </td>
                  </tr>
                  <tr>
                    <td class="labelCell"><bean:message key="label.project.sendto"/>: </td>
                    <td class="contentCell">
                      <html:hidden property="sendtodummy" />
                      <html:select property="sendTo" multiple="true" style="width:13em;height:5em" styleClass="inputBox" onkeydown="deleteSentTo()">
                        <c:forEach var="sendTo" items="${requestScope.sendTo}">
                          <option value="<c:out value='${sendTo.key}'/>"><c:out value='${sendTo.value}'/></option>
                        </c:forEach>
                      </html:select>
                    </td>
                    <td class="contentCell">
                      <app:cvbutton property="addContactLookup" styleClass="normalButton" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('addcontact')">
                        <bean:message key="label.project.addcontact"/>
                      </app:cvbutton>
                      <br/>
                      <br/>
                      <app:cvbutton property="deleteContact" styleClass="normalButton" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="deleteSentTo()">
                        <bean:message key="label.project.deletecontact"/>
                      </app:cvbutton>
                    </td>
                  </tr>
                </table>
              </div>
           </td>
        </tr>
        <tr>
          <td  class="labelCell"> <bean:message key="label.project.created"/>:</td>
          <td class="contentCell">
            <c:out value="${taskForm.createdOn}"/>&nbsp;
            <a class="plainLink" href="javascript:c_openPopup_FCI('Individual', <c:out value='${taskForm.createdbyid}'/>);">
            <c:out value="${taskForm.created}"/>
            </a>
          </td>
        </tr>
        <tr>
          <td class="labelCell"> <bean:message key="label.project.modified"/>:</td>
          <td class="contentCell">
            <c:out value="${taskForm.modifiedOn}"/>&nbsp;
            <a class="plainLink" href="javascript:c_openPopup_FCI('Individual', <c:out value='${taskForm.createdbyid}'/>);">
              <c:out value="${taskForm.modified}"/>
            </a>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top" align="left" width="50%">
      <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr>
          <td class="labelCell"><bean:message key="label.project.manager"/>: </td>
          <td class="contentCell">
            <html:hidden property="managerID" />
            <app:cvtext property="manager" readonly="true"   styleClass="clickableInputBox"  onclick="c_openPopup_FCI('Individual', document.taskForm.managerID.value);"  modulename="Tasks" fieldname="manager"/>
            <app:cvbutton property="managerLookupButton" styleClass="normalButton" style="width:60px;" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('employee', 'manager');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.startdate"/>: </td>
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
          <td class="labelCell"><bean:message key="label.project.duedate"/>:</td>
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
            <html:select property="status" styleClass="inputBox">
              <c:forEach var="statusid" items="${requestScope.statusid}">
                <c:if test="${statusid.key == taskForm.status}">
                  <option value="<c:out value='${statusid.key}'/>" selected="selected"><c:out value='${statusid.value}'/></option>
                </c:if>
                <c:if test="${statusid.key != taskForm.status}">
                  <option value="<c:out value='${statusid.key}'/>"><c:out value='${statusid.value}'/></option>
                </c:if>
              </c:forEach>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.percentcomplete"/>:</td>
          <td class="contentCell">
            <html:select property="percentComplete" styleClass="inputBox">
              <html:option value="0%"/>
              <html:option value="10%"/>
              <html:option value="20%"/>
              <html:option value="25%"/>
              <html:option value="30%"/>
              <html:option value="40%"/>
              <html:option value="50%"/>
              <html:option value="60%"/>
              <html:option value="70%"/>
              <html:option value="80%"/>
              <html:option value="90%"/>
              <html:option value="100%"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell">
            <bean:message key="label.project.assignedto"/>:
          </td>
          <td class="contentCell">
            <table>
              <tr>
                <td>
                  <html:hidden property="assignedTodummy" />
                  <app:cvselect property="assignedTo" size="5" multiple="true" style="width:13em;height:10em" styleClass="inputBox" modulename="Tasks" fieldname="assignedTo">
                    <c:forEach var="assignedTo" items="${requestScope.assignedTo}">
                      <option value="<c:out value='${assignedTo.key}'/>"><c:out value='${assignedTo.value}'/></option>
                    </c:forEach>
                  </app:cvselect>
                </td>
                <td>
                  <app:cvbutton property="addMemberLookup" styleClass="normalButton"  statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('employee', 'assignedTo')">
                    <bean:message key="label.project.addmembers"/>
                  </app:cvbutton>
                  <br>
                  <br>
                  <app:cvbutton property="deleteMember" styleClass="normalButton"  statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="deleteAssignedTo()">
                    <bean:message key="label.project.deletemembers"/>
                  </app:cvbutton>
                </td>
               </tr>
             </table>
          </td>
        </tr>
        <tr>
          <td colspan="2" class="sectionHeader"><bean:message key="label.project.customfields"/></td>
        </tr>
      </table>
        <jsp:include page="/jsp/pages/common/custom_field.jsp">
        <jsp:param name="Operation" value="Edit"/>
        <jsp:param name="RecordType" value="Task"/>
        <jsp:param name="RecordId" value="<%=taskID%>"/>
        </jsp:include>

    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="2">
        <%
          DisplayList displaylist = (DisplayList) session.getAttribute("DisplayList");
          if (displaylist != null)
          {
            Set listkey = displaylist.keySet();
            int startAt = displaylist.getStartAT();
            int endAt = displaylist.getEndAT();
            int beginindexAt = displaylist.getBeginIndex();
            int endindexAt = displaylist.getEndIndex();
            String str = null;
            Iterator ite = listkey.iterator();

            %>
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr>
                <td colspan="7" class="pagingBarContainer"><span class="labelBold"><bean:message key="label.project.subtasks"/></span></td>
              </tr>
              <tr>
                <td nowrap class="listHeader"><span class="listHeader">ID</span></td>
                <td nowrap class="listHeader"><a class="listHeader" href="<html:rewrite page='/projects/view_task.do' />?sortmember=Title&taskid=<%=taskID%>" onclick="document.submit()"><bean:message key="label.project.task"/>
                  <%
                    if(displaylist.getSortMember().equals("Title"))
                    {
                      if (displaylist.getSortType() != 'A')
                      {
                      %>
                        <html:img page="/images/icon_sort_descending.gif" width="7" height="4" border="0" />
                      <%
                      }
                      else{
                      %>
                        <html:img page="/images/icon_sort_ascending.gif" width="7" height="4" border="0" />
                 <%
                      }
                    }
                  %>
                </a>
                </td>
                <td nowrap class="listHeader"> <span class="listHeader"><bean:message key="label.project.milestone"/>?</span></td>
                <td nowrap class="listHeader">
                  <a class="listHeader" href="<html:rewrite page='/projects/view_task.do' />?sortmember=Owner&taskid=<%=taskID%> ">
                    <bean:message key="label.project.manager"/>
                    <%
                    if(displaylist.getSortMember().equals("Owner")){
                        if (displaylist.getSortType() != 'A')
                        {
                        %>
                          <html:img page="/images/icon_sort_descending.gif" width="7" height="4" border="0" />
                        <%
                        }
                        else{
                        %>
                          <html:img page="/images/icon_sort_ascending.gif" width="7" height="4" border="0" />
                   <%
                        }

                    } %>
                  </a>
                </td>
                <td nowrap class="listHeader"><span class="listHeader"><bean:message key="label.project.startdate"/></span></td>
                <td nowrap class="listHeader"><span class="listHeader"><bean:message key="label.project.duedate"/></span></td>
                <td nowrap class="listHeader"><span class="listHeader"><bean:message key="label.project.percentcomplete"/></span></td>
              </tr>
              <%
                boolean oddevenflag = true;
                String rowType = "";

                if(!ite.hasNext()){
                %>
                  <tr height="1">
                    <td class="tableRowOdd" colspan="7" align="center">&nbsp;<bean:message key="label.project.norecordsfound"/>.</td>
                  </tr>
                <%
                }
                while (ite.hasNext())
                {
                  str = ( String )ite.next();
                  ListElement ele  = ( ListElement )displaylist.get( str );

                  if (oddevenflag)
                  {
                    oddevenflag = false;
                    rowType="tableRowOdd";
                  }else{
                    oddevenflag = true;
                    rowType="tableRowEven";
                  }

                  %>
                  <tr>
                    <%
                      ListElementMember sm = ( ListElementMember )ele.get( "ActivityID" );
                      if (sm !=null)
                      {
                        %><td class="<%=rowType%>">&nbsp;<%=sm.getDisplayString()%></td><%
                      }

                      sm = ( ListElementMember )ele.get( "Title" );
                      %><td class="<%=rowType%>">&nbsp;<a class="plainLink" href="javascript:<%=sm.getRequestURL()%>" target="_parent"><%=sm.getDisplayString()%></a></td><%

                      sm = ( ListElementMember )ele.get( "Milestone" );
                      %><td class="<%=rowType%>" nowrap>&nbsp;<%=sm.getDisplayString()%></td><%

                      sm = ( ListElementMember )ele.get( "FirstName" );
                      String firstName = null;
                      if (sm != null)
                      {
                        firstName = sm.getDisplayString();
                      }

                      sm = ( ListElementMember )ele.get( "LastName" );

                      String lastName = null;
                      if (sm != null)
                      {
                        lastName = sm.getDisplayString();
                      }

                      String owner = (firstName != null ? firstName + " " : "") + (lastName != null ? lastName : "");
                      sm = ( ListElementMember )ele.get( "Owner" );
                      owner = sm.getDisplayString();
                      %><td nowrap class="<%=rowType%>">&nbsp;<a class="plainLink" href="javascript:<%=sm.getRequestURL()%>"><%=owner%></a></td><%

                      DateFormat df = new SimpleDateFormat("MMM d, yyyy") ;
                      String startdate = "";
                      DateMember dm = null;

                      if (ele.get( "StartDate" )!= null)
                      {
                        dm = (DateMember)ele.get( "StartDate" );
                        if (dm != null)
                        {
                          startdate  = dm.getDisplayString();
                        }
                      }
                      %><td class="<%=rowType%>" nowrap>&nbsp;<%=startdate%></td><%

                      df = new SimpleDateFormat("MMM d, yyyy") ;

                      String enddate = "";

                      if (ele.get("DueDate") != null)
                      {
                        dm = (DateMember)ele.get( "DueDate" );
                        enddate = dm.getDisplayString();
                      }

                      %><td class="<%=rowType%>" nowrap>&nbsp;<%=enddate%></td><%

                      String complete = "0";
                      sm = (ListElementMember)ele.get( "Complete" );
                      if (sm != null)
                      {
                        complete = sm.getDisplayString();
                      }
                      %><td class="<%=rowType%>" nowrap>&nbsp;<%=complete%>%</td><%
                    %>
                  </tr>
                  <%
                }   // end while (ite.hasNext())
              }

              %>
            </table>

    </td>
  </tr>
  <tr>
    <td  colspan="2" class="sectionHeader">
      <html:button property="newsubtask" styleClass="normalButton" onclick="return opensubtaskwindow()">
        <bean:message key="label.project.newsubtask"/>
      </html:button>

      <html:submit property="saveandclose" styleClass="normalButton" onclick="assigned()">
        <bean:message key="label.saveandclose"/>
      </html:submit>

      <html:submit property="saveandnew" styleClass="normalButton" onclick="assigned()">
        <bean:message key="label.saveandnew"/>
      </html:submit>

      <app:cvbutton property="deletetask" styleClass="normalButton" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="deletetask()">
        <bean:message key="label.project.deletetask"/>
      </app:cvbutton>

      <html:cancel  styleClass="normalButton" onclick="window.close();" >
        <bean:message key="label.cancel"/>
      </html:cancel>

      <app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
        <bean:message key="label.project.permissions"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>