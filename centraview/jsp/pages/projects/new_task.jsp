<%--
 * $RCSfile: new_task.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%
String taskID = (String)request.getAttribute("taskid");
%>
<script language="javascript">

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
      alert("<bean:message key='label.alert.selectcontacttodelete'/>!");
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


  function assigned()
  {
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
      c_lookup('Tasks',document.taskForm.projectid.value,0);
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
    alert(data);
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
            alert(dataValues[1]+" <bean:message key='label.alert.alreadyadded'/>");
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
				alert(name+"<bean:message key='label.alert.alreadyadded'/>");
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

</script>
<html:form action="/projects/save_new_task">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr>
    <td valign="top" width="50%">
      <table border="0" cellspacing="0" cellpadding="2" width="100%">
          <tr>
            <td class="labelCell"><bean:message key="label.project.title"/>: </td>
            <td class="contentCell">
              <html:text property="title" styleId="title" styleClass= "inputBox" />
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.description"/>: </td>
            <td class="contentCell">
             <app:cvtextarea property="description" styleClass="inputBox" cols="44" rows="5" modulename="Tasks" fieldname="description"/>
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
            <td class="labelCell"><bean:message key="label.project.parenttask"/>: </td>
            <td class="contentCell">
              <html:hidden property="parenttaskid" />
              <html:text property="parentTask" readonly="true" styleClass="inputBox" />
              <app:cvbutton property="parentTaskLookup" styleClass="normalButton" style="width:60px;" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('parenttask')">
                <bean:message key="label.lookup"/>
              </app:cvbutton>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.project.milestone"/>: </td>
            <td class="contentCell">
              <app:cvradio property="milestone" value="Yes" modulename="Tasks" fieldname="milestone"/>
              <bean:message key="label.project.yes"/>&nbsp;&nbsp;&nbsp;
              <app:cvradio property="milestone" value="No" modulename="Tasks" fieldname="milestone"/>
              <bean:message key="label.project.no"/>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="labelCell">
                <table border="0" cellpadding="2" cellspacing="0" width="100%">
                  <tr>
                    <td class="labelCell"><bean:message key="label.project.sendalert"/>: </td>
                    <td colspan="2" class="contentCell">
                      <html:radio property="sendAlert" value="Yes"/>
                      <bean:message key="label.project.yes"/> &nbsp;&nbsp;
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
                      <html:select property="sendTo" multiple="true" style="width:13em;height:5em" styleClass="inputBox" >
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
           </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.project.manager"/>: </td>
          <td class="contentCell">
            <html:hidden property="managerID" />
            <app:cvtext property="manager" readonly="true"   styleClass="inputBox"  modulename="Tasks" fieldname="manager"/>
            <app:cvbutton property="managerLookupButton" styleClass="normalButton" style="width:60px;" statuswindow="label.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('employee', 'manager');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top" align="left" width="50%">
      <table width="100%" border="0" cellpadding="2" cellspacing="0">
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
        <jsp:param name="Operation" value="Add"/>
        <jsp:param name="RecordType" value="Task"/>
        </jsp:include>

    </td>
  </tr>
  <tr>
    <td  colspan="2" class="sectionHeader">

      <app:cvsubmit property="saveandclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="assigned()">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>

      <app:cvsubmit  property="saveandnew" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="assigned()">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>

      <app:cvreset styleClass="normalButton"  statuswindow="label.resetfields" tooltip="true" statuswindowarg= "label.general.blank" onclick="resetSendTo()">
        <bean:message key="label.resetfields"/>
      </app:cvreset>

      <html:cancel  styleClass="normalButton" onclick="window.close();" >
        <bean:message key="label.cancel"/>
      </html:cancel>
    </td>
  </tr>
</table>
</html:form>