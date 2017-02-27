<!--
/*
 * $RCSfile: lookup.js,v $    $Revision: 1.2 $  $Date: 2005/06/14 21:12:01 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.centraview.com/opensource/
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
// This object is set by lu_radioSelect and then selected.
var lookupValues;
var dataValues = "";
var idValues = "";

function lu_selectList(lookupName)
{
  if (lookupName == "address") {
    window.opener.setAddress(lookupValues);
  }
  if (lookupName == "individual") {
    window.opener.setInd(lookupValues);
  }
  if (lookupName == "Entity"){
    window.opener.setEntity(lookupValues);
  }
  if (lookupName == "group"){
    window.opener.setGrp(lookupValues);
  }
  if (lookupName == "location"){
    window.opener.setLocation(lookupValues);
  }
  if (lookupName == "project"){
    window.opener.setProject(lookupValues);
  }
  if (lookupName == "projecttask"){
    window.opener.setParentTask(lookupValues);  
  }
  if (lookupName == "opportunity"){
    window.opener.setOpp(lookupValues);
  }
  if (lookupName == "Individual"){
    window.opener.setIndividual(lookupValues);
  }
  if (lookupName == "Employee") {
    if (arguments[1] != undefined) {
      window.opener.setEmployee(lookupValues, arguments[1]);    
    }else{
    window.opener.setEmployee(lookupValues);    
  }
  }
  if (lookupName == "Opportunity") {
    window.opener.setOpp(lookupValues);
  }
  if (lookupName == "lookup_attendee") {
    setupDataValues();
    window.opener.setView(lookupValues);
  }
  if (lookupName == "Source") {
    window.opener.setSource(lookupValues);
  }
  if (lookupName == "File") {
    window.opener.setData(lookupValues);
  }  
  if (lookupName == "Project") {
    window.opener.setProject(lookupValues);
  } 
  if (lookupName == "Order") {
    window.opener.setOrder(lookupValues);
  } 
  if (lookupName == "Tasks") {
    window.opener.setParentTask(lookupValues);
  } 
  if (lookupName == "Item") {
    setupItemDataValues();
    window.opener.setItem(lookupValues);
  }
  if (lookupName == "SubItem") {
    window.opener.setSubItem(lookupValues);
  }
  if (lookupName == "Vendor") {
    window.opener.setVendor(lookupValues);
  }
  if (lookupName == "Location") {
    window.opener.setLocation(lookupValues);
  }
  if (lookupName == "Ticket") {
      window.opener.setTicket(lookupValues);
  }
  window.close();
}
function lu_backList(lookupType, lookupParamID, presentID){
  if(lookupType == 'Item'){
	  c_goTo('/accounting/item_lookup.do?itemid='+lookupParamID+'&presentID='+presentID);
  }
}
// This function relies on the functions arguments array
// So it will take many arguments and then based on
// they lookupType passed set the local variables. 
// reference the Core JavaScript guide
function lu_radioSelect(lookupType)
{
  if (lookupType == "address") {
     lookupValues = {Name: arguments[1], ID: arguments[2], jurisdictionID: arguments[3]}
  } else if (lookupType == "Entity") {
    accountManagerId = arguments[4];
    if (accountManagerId == ""){
      accountManagerId = 0;
    }
    lookupValues = {entName: arguments[1], entID: arguments[2], acctManager: arguments[3], acctManagerID: accountManagerId}
  } else if (lookupType == "opportunity") {
    lookupValues = {entityName: arguments[1], entityID: arguments[2], Name: arguments[3], ID: arguments[4]}
  } else if (lookupType == "Individual" || lookupType == "Employee") {
    lookupValues = {firstName: arguments[1], individualID: arguments[2], middleName: arguments[3], lastName: arguments[4], title: arguments[5]}
  } else if (lookupType == "Opportunity") {
    lookupValues = {Name: arguments[1], ID: arguments[2], entityName: arguments[4], entityID: arguments[3]}
  } else if (lookupType == "lookup_attendee") {
    ; // do nothing in this case, the select button will do the work.
  } else if (lookupType == "Order") {
    lookupValues = {ID: arguments[1], Name: arguments[2], entityID: arguments[3]}
  } else if (lookupType == "SubItem") {
    lookupValues = {ID: arguments[2], SKU: arguments[1], Name: arguments[3], RemoveIDs: 0, Flag: true, vendorID: arguments[4], vendorName: arguments[5], manufacturerID: arguments[6],manufacturerName: arguments[7]}
  } else if (lookupType == "Ticket") {
    lookupValues = {ID: arguments[1], Name: arguments[2]}
  }else{
    lookupValues = {Name: arguments[1], idValue: arguments[2]}
  }
}

// This is for email lookups only, takes arguments in the form of <rowId>*<emailAddress> 
// e.g. 3*me@centraview.com
function lu_submitAddrs() {
  var dataTo = "";
  var dataCc = "";
  var dataBcc = "";
  
  var toBoxes = document.forms.listrenderer.chkto;
  if (toBoxes.length > 0) {
    dataTo = processMultiCheckboxes(toBoxes);
  } else {
    dataTo = processOneCheckbox(toBoxes);
  }

  var ccBoxes = document.forms.listrenderer.chkcc;
  if (ccBoxes.length > 0) {
    dataCc = processMultiCheckboxes(ccBoxes);
  } else {
    dataCc = processOneCheckbox(ccBoxes);
  }
  
  var bccBoxes = document.forms.listrenderer.chkbcc;
  if (bccBoxes.length > 0) {
    dataBcc = processMultiCheckboxes(bccBoxes);
  } else {
    dataBcc = processOneCheckbox(bccBoxes);
  }
  
  window.opener.setData(dataTo,dataCc,dataBcc);
  window.close();
}

function processMultiCheckboxes(checkboxes) {
  var flag = "";
  var data = "";
  var i = 0;
  
  while (i < checkboxes.length) {
    if (checkboxes[i].checked) {
      var value = checkboxes[i].value;
      if (value != "") {
        starPosition = value.indexOf("*");
      }
      
      var name = value.substring(starPosition + 1, value.length);
      if (flag == "") {
        data = data + name;
        flag = "checked";
      } else {
        data = data + ", " + name;
      }
    }
    i++;
  }
  return(data);
}

function processOneCheckbox(checkbox) {
  var name = "";
  if (checkbox.checked) {
    var value = checkbox.value;
    if (value != "") {
      starPosition = value.indexOf("*");
    }
    var id = value.substring(0, starPosition);
    name = value.substring(startPosition + 1, value.length);
  }
  return(name);
}

function setupItemDataValues()
{
  if (!vl_checkForCheckBox()) {
    return false;
  }
  i = 0;
  
  if (document.forms.listrenderer.rowId.length == undefined) {
    if (document.forms.listrenderer.rowId.checked) {
      var value = window.document.forms.listrenderer.rowId.value;
      idValues = value;
    }
  }else{
    while (i < document.forms.listrenderer.rowId.length) {
      if (document.forms.listrenderer.rowId[i].checked) {
        var value = window.document.forms.listrenderer.rowId[i].value;
        if (idValues=="") {
          idValues = value;
        }else{
          idValues = idValues + "," + value;
        }
      }
      i++;
    }  
  }
  lookupValues = {IDs: idValues, RemoveIDs: idValues, Flag: true}
}

function setupDataValues()
{
  if (!vl_checkForCheckBox()) {
    return false;
  }
  i = 0;
  if (document.forms.listrenderer.rowId.length == undefined) {
    if (document.forms.listrenderer.rowId.checked) {
      var value = window.document.forms.listrenderer.rowId.value;
      dataValueArray = value.split("&");
      dataValues = dataValues + value;
      idValues = idValues + dataValueArray[0];
    }
  }else{
    while (i < document.forms.listrenderer.rowId.length) {
      if (document.forms.listrenderer.rowId[i].checked) {
        var value = window.document.forms.listrenderer.rowId[i].value;
        dataValueArray = value.split("&");
        if (dataValues=="") {
          dataValues = value;
          idValues = dataValueArray[0];
        }else{
          dataValues = dataValues + "," + value;
          idValues = idValues + "," + dataValueArray[0];
        }
      }
      i++;
    }  
    lookupValues = {NameIDValue: dataValues, idValue: idValues }
  }
}

function lu_addSelectedItemsToParent()
{
  self.opener.addToParentList(document.forms.listrenderer.rowId);
  window.close();
}
//-->
