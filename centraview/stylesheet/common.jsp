<%--
 * $RCSfile: common.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<% response.setContentType("text/javascript"); %>
<!--
<%--
// Before adding to this file,ask your self this question:
// "Is this function going to be used by more than 75% of
// the pages in the application?" If the answer to that
// question is "No", think harder about where you want to
// place your function.
--%>
function c_setFocus(widgetName)
{
  if (widgetName != '' && document.getElementById(widgetName) != undefined) {
    document.getElementById(widgetName).focus();
  }
}
function c_searchList()
{
  // TODO: complete searchList() function
  searchTextBox = window.document.searchbar.searchTextBox.value;
}

<%-- Called by "clickableInputBox"es. Opens an Entity or Individual
     details popup, but first validates the recordID --%>
function c_openPopup_FCI(type, recordID)
{
  if (recordID > 0) {
    var url = '';
    if (type == 'Entity') {
      url = '/contacts/view_entity.do?rowId=' + recordID;
    }else if (type == 'Individual'){
      url = '/contacts/view_individual.do?rowId=' + recordID;
    }
    if (url != '') {
      c_openPopup(url);
    }
  }
}

function c_openPopup(url)
{
  window.open("<%=request.getContextPath()%>" + url, "", "width=780,height=580");
}

function c_newButton(moduleName, moduleId) {
  if (moduleName == "Entity") {
  	c_openPopup("/contacts/new_entity.do");
  }
  if (moduleName == "Individual") {
		c_openPopup("/contacts/new_individual.do");
  }
	if(moduleId == 30){
		window.open("<html:rewrite page="/sales/new_opportunity.do" />", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385');
		return false;
	}
	if(moduleId == 36){
			window.open("<html:rewrite page="/projects/new_project.do" />", 'NewProjectWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=730,height=360');
			return false;
	}
	if(moduleId == 37){
			window.open("<html:rewrite page="/projects/new_task.do" />", 'NewTaskWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=no,width=730,height=360');
			return false;
	}
}

function c_goTo(url)
{
  location.href = "<%=request.getContextPath()%>" + url;
}

function c_openWindow(page, name, width, height, windowOptions){
  if (windowOptions == ''){
    windowOptions = 'scrollbars=yes,resizable=yes';
  }
  window.open("<%=request.getContextPath()%>" + page, name, "width="+width+",height="+height+","+windowOptions);
}

function c_hideDetail(btnEl){
  var el = document.getElementById("detail");
  var bottomFrame = document.getElementById("bottomFrame");
  if (el) {
    el.style.display = (el.style.display == "none") ? "block" : "none";
    btnEl.value = (btnEl.value == "Minimize -") ? "Maximize +" : "Minimize -";
    bottomFrame.height = (el.style.display == "none") ? 555 : 180;
  }
}

function c_lookup(lookupType)
{
  var width = 460;
  var height = 500;
  var name = '';
  var page;
  var options = '';
  if (lookupType == 'Employee') {
    if (arguments[1] != undefined){
      page = '/contacts/employee_lookup.do?lookupFlag=' + arguments[1];
    }else{
      page = '/contacts/employee_lookup.do';
    }
  } else if (lookupType == 'PrimaryContact') {
    page='/contacts/individual_lookup.do?entityId=' + arguments[1];
  } else if (lookupType == 'Individual') {
    if (arguments[1] != undefined) {
      page='/contacts/individual_lookup.do?entityId=' + arguments[1];
    } else {
      page='/contacts/individual_lookup.do'
    }
  } else if (lookupType == 'Entity') {
    page='/contacts/entity_lookup.do';
  } else if (lookupType == 'Opportunity') {
    page = '/sales/opportunity_lookup.do';
	if (arguments[1] != undefined) {
       page += '?entityId=' + arguments[1];
	}
  } else if (lookupType == 'Ticket') {
    page = '/support/ticket_lookup.do';
  } else if (lookupType == 'Source') {
    page = '/common/source_lookup.do';
  } else if (lookupType == 'attendee_lookup') {
    page = '/contacts/attendee_lookup.do';
  } else if (lookupType == 'Group') {
    page = '/contacts/group_lookup.do';
  } else if (lookupType == 'Project') {
    page = '/projects/project_lookup.do';
  } else if (lookupType == 'Tasks') {
     taskid = 0;
 		 if(arguments[2] != undefined){
	 		 taskid = arguments[2];
 		 }
     page = '/projects/task_lookup.do?projectid='+arguments[1]+'&taskid='+taskid;
  } else if (lookupType == 'Order') {
    page = '/accounting/order_lookup.do';
  } else if (lookupType == 'Vendor') {
    page = '/accounting/vendor_lookup.do';
  } else if (lookupType == 'Location') {
    page = '/common/location_lookup.do';
  } else if (lookupType == 'Item') {
    itemId = arguments[1];
    if(arguments[1] == undefined){
      itemId=0;
    }
    page = '/accounting/item_lookup.do?presentID='+itemId;
  } else if (lookupType == 'SubItem') {
    itemId = arguments[1];
    if(arguments[1] == undefined){
      itemId=0;
    }
    page = '/accounting/sub_item_lookup.do?itemid='+itemId;
  }
  c_openWindow(page, name, width, height, options);
}

function c_getSelectedRadio(buttonGroup)
{
  // returns the array number of the selected radio button or -1 if no button is selected
  if (buttonGroup[0]){
    // if the button group is an array (one button is not an array)
    for (var i=0; i<buttonGroup.length; i++){
      if (buttonGroup[i].checked){
        return i
      }
    }
  } else {
    if (buttonGroup.checked) { return 0; } // if the one button is checked, return zero
  }
  // if we get to this point, no radio button is selected
  return -1;
} // Ends the "c_getSelectedRadio" function

function c_getSelectedRadioValue(buttonGroup)
{
  // returns the value of the selected radio button or "" if no button is selected
  var i = c_getSelectedRadio(buttonGroup);
  if (i == -1) {
    return buttonGroup.value;
    //return "";
  } else {
    if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
     return buttonGroup[i].value;
    } else { // The button group is just the one button, and it is checked
     return buttonGroup.value;
    }
  }
} // Ends the "c_getSelectedRadioValue" function

function c_getMonthName(month){
  monthName = "";
  switch (month)
  {
    case 1:
      monthName = "January";
      break;
    case 2:
      monthName = "February";
      break;
    case 3:
      monthName = "March";
      break;
    case 4:
      monthName = "April";
      break;
    case 5:
      monthName = "May";
      break;
    case 6:
      monthName = "June";
      break;
    case 7:
      monthName = "July";
      break;
    case 8:
      monthName = "August";
      break;
    case 9:
      monthName = "September";
      break;
    case 10:
      monthName = "October";
      break;
    case 11:
      monthName = "November";
      break;
    case 12:
      monthName = "December";
      break;
  }
  return monthName;
}

function c_openPermission(moduleName, recordId, recordName)
{
  var page = '/common/record_permission.do?modulename='+moduleName+'&rowID='+recordId+'&recordName='+recordName;
  var name = '';
  var width = 615;
  var height = 461;
  options = '';
  c_openWindow(page, name, width, height, options);
}

// Used by the Addtional Menu to forward the the control to appropriate
// forward based on selection in the additional Menu.
function c_additionalMenu(value)
{
  var url = "/additional_menu.do?UIParam=0&forward=" + value;
  if (value == '--') {
    // do nothing
  } else if (value == 'help') {
    c_openWindow(url, 'help', 500, 750, '');
  }else{
    c_goTo(url);
  }
}

function c_setListId(moduleId)
{
  var selvalue = document.getElementById('dbid').value;
  if (selvalue == "") {
    alert("<bean:message key='label.alert.makevalidselection'/>.");
    return false;
  }
  if (moduleId == 14) {
    c_goTo('/contacts/entity_list.do?dbid='+selvalue);
  } else {
    c_goTo('/contacts/individual_list.do?dbid='+selvalue);
  }
}

function c_openInParentFrame(url)
{
  parent.location.href = "<%=request.getContextPath()%>" + url;
}
//-->