<!--
/*
 * $RCSfile: detail.js,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:40 $ - $Author: mking_cv $
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

function setEmployee(lookupValues)
{
  // for employee lookups, lookupValues is a javascript object that looks like this:
  // {firstName, individualID, middleName, lastName, title}
  var employeeName = document.getElementById('employeeName');
  var employeeId = document.getElementById('employeeId');
  employeeName.readonly = false;
  employeeName.value = lookupValues.firstName + " " + lookupValues.lastName;
  employeeName.readonly = true;
  employeeId.value = lookupValues.individualID;
}

function setIndividual(lookupValues)
{
  // for Individuals lookupValues is a javascript object:
  // {firstName, individualID, middleName, lastName, title}
  var iIndividualId = document.getElementById('iIndividualId');
  var iFirstName = document.getElementById('iFirstName');
  var iMiddleInitial = document.getElementById('iMiddleInitial');
  var iLastName = document.getElementById('iLastName');
  var iTitle = document.getElementById('iTitle');
  iIndividualId.value = lookupValues.individualID;
  iFirstName.value = lookupValues.firstName;
  iMiddleInitial.value = lookupValues.middleName;
  iLastName.value = lookupValues.lastName;
  iTitle.value = lookupValues.title;
}

function setEntity(lookupValues)
{
  // for entities lookupValues is a javascript object:
  // {entName, entID, acctManager, acctManagerID}
  var entityId = document.getElementById('entityId');
  var contactForm = document.getElementById('contactForm');
  entityId.value = lookupValues.entID;
  contactForm.submit();
}

function setSource(lookupValues)
{
  var sourceId = document.getElementById('sourceId');
  var sourceName = document.getElementById('sourceName');
  sourceId.value = lookupValues.idValue;
  sourceName.value = lookupValues.Name;
}

function saveAndNew()
{
  var contactForm = document.getElementById('contactForm');
  contactForm.action = contactForm.action + '?new=true';
  contactForm.submit();
}
function setGrp(lookupValues)
{
  var groupId = document.getElementById('groupId');
  var groupName = document.getElementById('groupName');
  groupId.value = lookupValues.idValue;
  groupName.readonly = false;
  groupName.value = lookupValues.Name;
  groupName.readonly = true;
}
//-->
