<%--
 * $RCSfile: activity.jsp,v $    $Revision: 1.5 $  $Date: 2005/09/21 16:32:42 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="com.centraview.activity.helper.ActivityVO" %>
<%@ page import="com.centraview.activity.ConstantKeys" %>
<% response.setContentType("text/javascript"); %>
function act_openIndividualLookup()
{
  var entityID = document.getElementById('activityEntityID').value;
  if (entityID <= 0) {
    alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.");
  }else{
    c_lookup('Individual', entityID);
  }
}

function act_updateRelatedTypeID()
{
  var typeIdField = document.getElementById('activityRelatedTypeID');
  var typeValueField = document.getElementById('activityRelatedTypeValue');

  var fieldIdField = document.getElementById('activityRelatedFieldID');
  var fieldValueField = document.getElementById('activityRelatedFieldVaule');

  var relatedType = typeValueField.options[typeValueField.selectedIndex].value;
  if (relatedType == "Opportunity") {
    typeIdField.value = <%=ActivityVO.ACTIVITY_LINK_OPPORTUNITY%>;
  }else if (relatedType == "Project"){
    typeIdField.value = <%=ActivityVO.ACTIVITY_LINK_PROJECT%>;
  }else{
    typeIdField.value = 0;
  }

  fieldIdField.value = "";
  fieldValueField.value = "";
}

function act_openRelatedFieldList()
{
  var relatedTypeValueField = document.getElementById('activityRelatedTypeValue');
  var relatedType = relatedTypeValueField.options[relatedTypeValueField.selectedIndex].value;
  var entityID = document.getElementById('activityEntityID').value;
  if (entityID == "") {
    entityID = "-1";    //so the action class will not do filtering
  }
  if (relatedType == "Opportunity") {
    c_lookup('Opportunity', entityID);
  }else if (relatedType == "Project"){
    c_lookup('Project', entityID);
  }else{
    alert('<bean:message key='label.alert.selectrelatedrecord'/>.');
  }
}

function act_updateReminder()
{
  if (document.forms.activityform.activityReminder.checked)
  {
    //check if user haven't changed reminder manually
    startMonth = document.forms.activityform.activityStartMonth.value * 1;
    startDay = document.forms.activityform.activityStartDay.value * 1;
    startYear = document.forms.activityform.activityStartYear.value * 1;
    startTime = document.forms.activityform.activityStartTime.value;

    tmparr = startTime.split(" ");
    timePart = tmparr[0];
    amPm = tmparr[1];

    tmparr = timePart.split(":");
    minutes = tmparr[1]*1;
    hours = tmparr[0]*1;

    if (hours == 12 && amPm == "AM") {
      hours = 0;
    }else if (hours < 12 && amPm == "PM"){
      hours = hours + 12;
    }

    var sdate = new Date(startYear, startMonth, startDay, hours, minutes, 0);
    sdate.setTime(sdate.getTime() - 900000);

    document.forms.activityform.activityReminderMonth.value = sdate.getMonth();
    document.forms.activityform.activityReminderDay.value = sdate.getDate();
    var tempYear = sdate.getYear();
    if (tempYear < 1900) {
      tempYear = tempYear + 1900;
    }
    document.forms.activityform.activityReminderYear.value = tempYear;
    document.forms.activityform.activityReminderTime.value = act_formatTime(sdate.getHours(), sdate.getMinutes());
  }
}

function act_openSelectDateTime(optionalFlag)
{
  var startDate = document.getElementById('activityStartDate').value;
  var endDate = document.getElementById('activityEndDate').value;
  var startTime = document.forms.activityform.activityStartTime.value;
  var endTime = document.forms.activityform.activityEndTime.value;
  if (optionalFlag == 'remindMe') {
    c_openWindow('/calendar/select_date_time.do?dateTimeType=2&startDate='+startDate+'&endDate='+endDate+'&startTime='+startTime+'&endTime='+endTime+'&optionalFlag=remindMe', 'selectDateTime', 385, 500, '');
  }else{
    c_openWindow('/calendar/select_date_time.do?dateTimeType=5&startDate='+startDate+'&endDate='+endDate+'&startTime='+startTime+'&endTime='+endTime, 'selectDateTime', 385, 500, '');
  }
}

function act_formatTime(h, m)
{
  return dateControlHours(h) + ":" + m + " " + dateControlAmPm(h);
}

function act_checkAddEditSchedule(closeornew)
{
  act_populateAllListBox();
  var typeOfOperation = window.document.activityform.TYPEOFOPERATION.value;

  if (typeOfOperation == "<%=ConstantKeys.ADD%>") {
    // quick-fix of sending all hidden param
    document.activityform.action = "<html:rewrite page="/activities/save_new_activity.do"/>?closeornew=" + closeornew;
    document.activityform.target = "_self";
    document.activityform.enctype = "multipart/form-data";
    document.activityform.submit();
    return false;
  } else if (typeOfOperation == "<%=ConstantKeys.EDIT%>") {
    var activityid = document.activityform.activityID.value
    // quick-fix of sending all hidden param
    var passing = "<html:rewrite page="/activities/edit_activity.do"/>?closeornew="+closeornew+"&activityid="+activityid;
    document.activityform.action = passing;
    document.activityform.target="_self";
    document.activityform.enctype="multipart/form-data";
    document.activityform.submit();
    return false;
  } else{
    alert('<bean:message key='label.alert.invaliderror'/>');
  }
}

function act_deleteActivity()
{
  var rowID = document.activityform.activityID.value
  document.activityform.action = "<html:rewrite page="/activities/delete_activitylist.do"/>?windowType=Popup&rowId=" + rowID;
  document.activityform.submit();
  return false;
}

function act_populateAllListBox()
{
  act_populateAttendee();
  act_populateResource();
  act_populateAttachment();
  act_populateCheckBox();
}

function act_populateAttendee()
{
  if (window.document.activityform.attendeerequired != null) {
    var lengthRequired = window.document.activityform.attendeerequired.options.length;
    for (var i=0; i<lengthRequired; i++) {
      window.document.activityform.attendeerequired.options[i].selected=true;
    }
  }
  if (window.document.activityform.attendeeoptional != null) {
    var lengthOptional = window.document.activityform.attendeeoptional.options.length;
    for (var i=0; i<lengthOptional; i++) {
      window.document.activityform.attendeeoptional.options[i].selected=true;
    }
  }
}

function act_populateResource()
{
  if (window.document.activityform.resourceselected != null) {
    var lengthSelected = window.document.activityform.resourceselected.options.length;
    for (var i=0; i<lengthSelected; i++) {
      window.document.activityform.resourceselected.options[i].selected = true;
    }
  }
}

function act_populateAttachment()
{
  if (window.document.activityform.filelist != null) {
    var lengthSelected = window.document.activityform.filelist.options.length;
    for (var i=0; i<lengthSelected; i++) {
      window.document.activityform.filelist.options[i].selected = true;
    }
  }
}

function act_populateCheckBox()
{
  // pasted here instead of separate function
  // all options / value used to avoid risk
  // for detail checkbox
  if (window.document.activityform.activityReminder != null) {
    if (window.document.activityform.activityReminder.checked) {
      window.document.activityform.activityReminder.value = "on";
      window.document.activityform.activityReminder.checked = true;
      window.document.activityform.remindcheckbox.value = "on";
    }else{
      window.document.activityform.activityReminder.value = "off";
      window.document.activityform.activityReminder.checked = false;
      window.document.activityform.remindcheckbox.value = "off";
    }
  }

  // for email checkbox
  if (window.document.activityform.activityEmailInvitation != null) {
    if (window.document.activityform.activityEmailInvitation.checked) {
      window.document.activityform.activityEmailInvitation.value = "on";
      window.document.activityform.activityEmailInvitation.checked = true;
      window.document.activityform.emailcheckbox.value = "on";
    }else{
      window.document.activityform.activityEmailInvitation.value = "off";
      window.document.activityform.activityEmailInvitation.checked = false;
      window.document.activityform.emailcheckbox.value = "off";
    }
  }
}

function act_launchDetail() {
  act_populateAllListBox();
  document.activityform.action = "<html:rewrite page="/activities/view_activity_details.do"/>";
  document.activityform.submit();
  return false;
}

function act_launchAttendee() {
  act_populateResource();
  act_populateAttachment();
  act_populateCheckBox();
  document.activityform.action = "<html:rewrite page="/activities/view_activity_attendees.do"/>";
  document.activityform.submit();
  return false;
}

function act_launchResource() {
  act_populateAttendee();
  act_populateAttachment();
  act_populateCheckBox();
  document.activityform.action = "<html:rewrite page="/activities/view_activity_resources.do"/>";
  document.activityform.submit();
  return false;
}

function act_launchAvailability() {
  act_populateAllListBox();
  document.activityform.action = "<html:rewrite page="/activities/view_activity_availability.do"/>";
  document.activityform.submit();
  return false;
}

function act_launchRecurring() {
  act_populateAllListBox();
  document.activityform.action = "<html:rewrite page="/activities/view_activity_recurring.do"/>";
  document.activityform.submit();
  return false;
}

function act_launchAttachment(){
  act_populateAttendee();
  act_populateResource();
  act_populateCheckBox();
  document.activityform.action = "<html:rewrite page="/activities/view_activity_attachments.do"/>";
  document.activityform.submit();
  return false;
}

function act_addResource()
{
  var n = document.forms[0].activityResourceAll.options.length;
  var m = document.forms[0].activityResourceSelected.options.length;
  if (document.forms[0].activityResourceAll.selectedIndex == -1) {
    alert("<bean:message key='label.alert.selectresource'/>.");
  }else{
    i = 0;
    while (i < n) {
      if (document.forms[0].activityResourceAll.options[i].selected) {
        document.forms[0].activityResourceSelected.options[m] = new Option(document.forms[0].activityResourceAll.options[i].text,document.forms[0].activityResourceAll.options[i].value,"false","false");
        document.forms[0].activityResourceAll.options[i] = null;
        n = n - 1;
        m = m + 1;
        i = 0;
      }else{
        i++;
      }
    }
	}
}

function act_removeResource()
{
  var m = document.forms[0].activityResourceAll.options.length;
  var n = document.forms[0].activityResourceSelected.options.length;

  if (document.forms[0].activityResourceSelected.selectedIndex == -1)	{
    alert("<bean:message key='label.alert.selectresourcetoremove'/>.");
	}else{
    i = 0;
    while (i < n) {
      if (document.forms[0].activityResourceSelected.options[i].selected) {
        document.forms[0].activityResourceAll.options[m] = new Option(document.forms[0].activityResourceSelected.options[i].text,document.forms[0].activityResourceSelected.options[i].value,"false","false");
        m = m+1;
      }
      i++;
    }
    i = 0;
    while (i < n)	{
      if (document.forms[0].activityResourceSelected.options[i].selected) {
        document.forms[0].activityResourceSelected.options[i] = null;
        n = n - 1;
      }else{
        i++;
      }
    }
  }
}

function act_showLayer(selectElement)
{
  var layerName = selectElement.value;
  var dailyLayer = document.getElementById('DAY');
  var weeklyLayer = document.getElementById('WEEK');
  var monthlyLayer = document.getElementById('MONTH');
  var yearlyLayer = document.getElementById('YEAR');

  if (layerName == 'DAY'){
    dailyLayer.style.display   = "block";
    weeklyLayer.style.display  = "none";
    monthlyLayer.style.display = "none";
    yearlyLayer.style.display  = "none";
  }else if (layerName == 'WEEK'){
    dailyLayer.style.display   = "none";
    weeklyLayer.style.display  = "block";
    monthlyLayer.style.display = "none";
    yearlyLayer.style.display  = "none";
  }else if (layerName == 'MONTH'){
    dailyLayer.style.display   = "none";
    weeklyLayer.style.display  = "none";
    monthlyLayer.style.display = "block";
    yearlyLayer.style.display  = "none";
  }else if (layerName == 'YEAR'){
    dailyLayer.style.display   = "none";
    weeklyLayer.style.display  = "none";
    monthlyLayer.style.display = "none";
    yearlyLayer.style.display  = "block";
  }
}

function act_attachFile()
{
  var filename = document.getElementById('activityFile').value;
  var activityAttachedCentraviewFile = document.getElementById('activityAttachedCentraviewFile').value;

  if (filename == "" && activityAttachedCentraviewFile == "") {
    alert( "<bean:message key='label.alert.selectfiletoupload'/>" );
    return;
  }
  var index ;
  var file ;
  var m = document.getElementById('activityFilesList').options.length;

  if (filename != "")	{
    file = filename.substring( filename.lastIndexOf("\\") + 1);
  }else{
    file = activityAttachedCentraviewFile;
  }

  //document.getElementById('activityFilesList').options[m] = new Option(file, document.getElementById('activityAttachedFileID').value + "#" + file, "false", "false");

  if (filename != "")	{
    document.activityform.action = "<html:rewrite page="/activities/activity_file_attachment.do"/>?From=Local";
  }else if (activityAttachedCentraviewFile != null){
    document.activityform.action = "<html:rewrite page="/activities/activity_file_attachment.do"/>?From=Server&fileName=" + file;
  }
  document.activityform.submit();
  return false;
}

function act_removeFile()
{
  if (document.activityform.activityFilesList.selectedIndex != -1) {

    document.activityform.listSelectedFileId.value = document.activityform.activityFilesList[document.activityform.activityFilesList.selectedIndex].value;
    act_populateAttachment();
    document.activityform.action = "<html:rewrite page="/activities/activity_remove_file_attachment.do"/>";
    document.activityform.submit();
  }else{
    alert( "<bean:message key='label.alert.selectfiletoremove'/>.");
  }
}

function act_downloadFile()
{
  if (document.activityform.activityFilesList.selectedIndex != -1) {
    fileDetail = document.activityform.activityFilesList[document.activityform.activityFilesList.selectedIndex].value;
    if (fileDetail != "") {
      fileid = fileDetail.split("#");
      location.href = "<html:rewrite page="/files/file_download.do"/>?fileid=" + fileid[0];
    }
  }else{
    alert("<bean:message key='label.alert.selectfiletodownload'/>.");
  }
}

function act_searchAttendees()
{
  window.document.activityform.action = "<html:rewrite page="/activities/view_activity_attendees.do"/>";
  window.document.activityform.submit();
}

function act_changeAttendeesType()
{
  var groupId = document.activityform.activityAttendeesType.options[document.activityform.activityAttendeesType.selectedIndex].value;
  groupId = groupId.substring(0, groupId.indexOf("#"));
  window.document.activityform.action = "<html:rewrite page="/activities/view_activity_attendees.do"/>?groupId="+groupId;
  window.document.activityform.submit();
  return false;
}


function act_removeAttendee(type)
{
  var leftBox = document.getElementById('activityAttendeesAll');
  if (type == 'required') {
    var rightBox = document.getElementById('activityAttendeesRequired');
  }else if (type == 'optional'){
    var rightBox = document.getElementById('activityAttendeesOptional');
  }

  var allLength = leftBox.options.length
	var selectedLength = rightBox.options.length;

  if (rightBox.selectedIndex == -1) {
    alert("<bean:message key='label.alert.selectattendeetoremove'/>.");
	}

  if (rightBox.selectedIndex >= 0){
    for (var i=0; i<selectedLength; i++) {
      if (rightBox.options[i].selected) {
        leftBox.options[allLength] = new Option(rightBox.options[i].text, rightBox.options[i].value, "false", "false");
				allLength++;
			}
		}
	}
	i = 0;
	while (i < selectedLength) {
    if (rightBox.options[i].selected) {
      rightBox.options[i] = null;
      selectedLength = selectedLength - 1;
    }else{
      i++;
    }
  }
}

function act_addAttendee(type)
{
  var leftBox = document.getElementById('activityAttendeesAll');
  var requiredBox = document.getElementById('activityAttendeesRequired');
  var optionalBox = document.getElementById('activityAttendeesOptional');

  var allLength = leftBox.options.length;
  var optionalLength = optionalBox.options.length;
  var requiredLength = requiredBox.options.length;

  if (leftBox.selectedIndex == -1) {
    alert("<bean:message key='label.alert.selectattendeetoadd'/>.");
  }else{
    for (var i=0; i<allLength; i++) {
      var flag = 0;
      var flag1 = 0;

      if (leftBox.options[i].selected) {
        for (var k=0; k<requiredLength; k++)	{
          if (requiredBox.options[k].value ==  leftBox.options[i].value) {
            alert("<bean:message key='label.alert.memberalreadyselected'/>.");
            flag1 = 1;
          }
        }
      }

      if (leftBox.options[i].selected) {
        for (var k=0; k<optionalLength; k++) {
          if (optionalBox.options[k].text ==  leftBox.options[i].text) {
            alert("<bean:message key='label.alert.memberalreadyselected'/>.");
            flag = 1;
          }
        }

        if (flag == 0 && flag1 == 0) {
          if (type == "optional") {
            optionalBox.options[optionalLength] = new Option(leftBox.options[i].text, leftBox.options[i].value, "false", "false");
          }else{
            requiredBox.options[requiredLength] = new Option(leftBox.options[i].text, leftBox.options[i].value, "false", "false");
          }
          optionalLength = optionalLength + 1;
        }
      }
    }
    i = 0;
    while (i < allLength) {
      if (leftBox.options[i].selected) {
        leftBox.options[i] = null;
        allLength = allLength - 1;
      }else{
        i++;
      }
    }
  }
}

function act_TypeChange(form)
{
  var selectedValue = form.options[form.selectedIndex].value;
  var callLayer  = document.getElementById('callBox');
  if (selectedValue != "" && selectedValue.length > 2) {
    document.location.href  = '<html:rewrite page="/"/>' + selectedValue;
  }
  if (selectedValue == "2" ) {
    if (callLayer != null) {
      callLayer.style.display = "block";
    }
  } else {
    if (callLayer != null) {
      callLayer.style.display = "none";
    }
  }
}