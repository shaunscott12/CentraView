<%--
 * $RCSfile: value_list_js.jsp,v $    $Revision: 1.5 $  $Date: 2005/09/02 14:46:35 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<% response.setContentType("text/javascript"); %>
/**
 * Highlights an entire table row in the value list with
 * the correct style; also un-highlights (toggles) if the
 * row was already selected;
 */
function vl_selectRow(widget, isOdd)
{
  var isSelected = widget.checked;    // is the row currently selected?
  var parentRow = widget.parentNode.parentNode;   // this is the TR element
  var childNodes = parentRow.childNodes;    // the list of all TD elements in this row
  var className = "tableRowOdd";

  // figure out which class to use
  if (isSelected) {
    className = "tableRowSelected";
  }else{
    if (isOdd) {
      className = "tableRowEven";
    }else{
      className = "tableRowOdd";
    }
  }

  // apply the style class to all TDs in this row
  for (var i=0; i<childNodes.length; i++) {
    childNodes[i].className = className;
  }
}   // end selectRow()

/**
 * Highlights or unhighlights all the rows in the value list,
 * determining which style class to apply.
 */
var vl_allCheck = true;
function vl_selectAllRows()
{
  // get all INPUT elements
  var cbElements = document.getElementsByTagName("input");
  // loop the INPUTs...
  var count = 0;
  for (i=0; i<cbElements.length; i++) {
    // if it's a checkbox...
    if (cbElements[i].getAttribute("type") == "checkbox") {
      // ...check or uncheck the checkbox...
      cbElements[i].checked = vl_allCheck;
      // ...call vl_selectRow() on it
      vl_selectRow(cbElements[i], count%2);   // i%2-1 == isOdd true/false
      count++;
    }
  }
  // toggle the vl_allCheck var
  if (vl_allCheck == true) {
    vl_allCheck = false;
  }else{
    vl_allCheck = true;
  }
}   // end vl_selectAllRows()

/**
 * Returns true if there is at least one checkbox selected
 * in the ValueList.
 */
function vl_checkForCheckBox()
{
  var k = 0;
  for (i=0; i<document.listrenderer.elements.length; i++) {
    if (document.listrenderer.elements[i].type == "checkbox") {
      k++;
    }
  }

  if (k >= 1) {
    for (i=0 ;i<document.listrenderer.elements.length; i++) {
      if (document.listrenderer.elements[i].type == "checkbox") {
        if ((document.listrenderer.elements[i].checked)) {
          return true;
        }
      }
    }
    alert("<bean:message key='label.alert.selectatleastonerecord'/>.");
    return false;
  }else{
    alert("<bean:message key='label.alert.norecordstoviewdeleteduplicate'/>");
    return false;
  }
}

/**
 * Returns the number of rows selected in the ValueList
 */
function vl_checkForRowSelected()
{
  var totalRow = document.listrenderer.rowId.length;
  var rowSelected = 0;
  if (totalRow == undefined) {
    if (document.listrenderer.rowId.checked == true) {
      return ++rowSelected;
    }
  }else{
    for (i=0; i<document.listrenderer.rowId.length; i++) {
      if (document.listrenderer.rowId[i].checked == true) {
        ++rowSelected;
      }
    }
    return rowSelected;
  }
  return rowSelected;
}

function vl_privateList() {
  if (!vl_checkForCheckBox()) {
    return false;
  }
  window.document.listrenderer.action="<%=request.getContextPath()%>/email/private_message.do?privateType=YES&folderID=<bean:write name="emailListForm" property="folderID" ignore="true" />";
  window.document.listrenderer.method="post";
  window.document.listrenderer.target="_self";
  window.document.listrenderer.submit();

}


function vl_readList() {
  if (!vl_checkForCheckBox()) {
    return false;
  }

  status = "read";
  var folderID = "<c:out value="${param.folderID}"/>";
  if (folderID == "null")
  {
    folderID="";
  }
  window.document.listrenderer.action="<%=request.getContextPath()%>/email/read_message.do?status=" + status + "&folderID=" + folderID;
  window.document.listrenderer.method="post";
  window.document.listrenderer.target="_self";
  window.document.listrenderer.submit();
  return true;
}

function vl_unReadList() {
  if (!vl_checkForCheckBox()) {
    return false;
  }

  status = "unread";

  var folderID = "<c:out value="${param.folderID}"/>";
  if (folderID == "null") {
    folderID="";
  }
  window.document.listrenderer.action="<%=request.getContextPath()%>/email/read_message.do?status=" + status + "&folderID=" + folderID;
  window.document.listrenderer.method="post";
  window.document.listrenderer.target="_self";
  window.document.listrenderer.submit();
  return true;
}

function vl_ruleList(accountID) {
  c_goTo("/email/rules_list.do?accountID="+accountID);
}

function vl_moveTo(moveToID){
  if (!vl_checkForCheckBox())
  {
    return false;
  } //end of if statement (!vl_checkForCheckBox())
  listTypeId = document.listrenderer.listTypeId.value;

  // Entity Type
  if (listTypeId == 1){
    if (confirm("Record owners or Administrators may move Entities with related Individuals between lists. Please click \"OK\" to move your records to the new list."))
    {
      document.listrenderer.target="_self";
      document.listrenderer.action="<%=request.getContextPath()%>/contacts/move_entity.do?sourceListId="+moveToID;
      document.listrenderer.submit();
      return true;
    }
    else
    {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          var cbElements = document.getElementsByTagName("input");
          for (i=0; i<cbElements.length; i++) {
            if (cbElements[i].getAttribute("type") == "checkbox") {
              cbElements[i].checked = false;
              selectRow(cbElements[i], i%2);
            }
          }
        }
      }
      return false;
    }
  }

  // Email Type
  if (listTypeId == 16){
    var folderID = "<c:out value="${param.folderID}"/>";
      if (folderID == "null") {
        folderID="";
    }

    window.document.listrenderer.action="<%=request.getContextPath()%>/email/move_message.do?newFolderID=" + moveToID + "&folderID=" + folderID;
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return true;
  }
}

function vl_rppGoTo(recordsPerPageURL){
  c_goTo(recordsPerPageURL);
}

function vl_viewList()
{
  if (! vl_checkForCheckBox()) {
    return false;
  }

  listTypeId = document.listrenderer.listTypeId.value;

  var selectedRows = vl_checkForRowSelected();
  if (selectedRows == 0) {
    return false;
  }
  var arrayRowSelected = new Array(selectedRows);

  // Entity Type
  if (listTypeId == 1) {
    // if only one record is selected
    if (selectedRows == 1)  {
      // if only one record is present and that only is selected
      if (document.listrenderer.noofrecord.value == 1)  {
        arrayRowSelected[0] = document.listrenderer.rowId.value;
        c_openPopup('/contacts/view_entity.do?rowId='+arrayRowSelected[0]);
      }
    }
    // if multiple record are present and only one is selected ||
    // if out of multiple record multiple are selected
    if ((selectedRows == 1 && document.listrenderer.noofrecord.value > 1) || selectedRows > 1)  {
      j = 0;
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          arrayRowSelected[j] = window.document.listrenderer.rowId[i].value;
          j++
        }
      }
      c_openPopup('/contacts/view_entity.do?rowId='+arrayRowSelected+'&MultiEntity=true');
    }
    return  false;
  }//end of if (listTypeId == 1)

  // Individual Type
  if (listTypeId == 2 || listTypeId == 73){
      // if only one record is selected
      if (selectedRows == 1)  {
        // if only one record is present and that only is selected
        if (document.listrenderer.noofrecord.value == 1)  {
          arrayRowSelected[0] = document.listrenderer.rowId.value;
          if (listTypeId == 73) {
            parent.location.href = '<%=request.getContextPath()%>/contacts/view_individual.do?rowId=' + arrayRowSelected[0];
          } else {
            c_openPopup('/contacts/view_individual.do?rowId='+arrayRowSelected[0]);
          }
        }
      }
      // if multiple record are present and only one is selected ||
      // if out of multiple record multiple are selected
      if ((selectedRows == 1 && document.listrenderer.noofrecord.value > 1) || selectedRows > 1)  {
        j = 0;
        for (i=0;i<document.listrenderer.rowId.length;i++) {
          if(document.listrenderer.rowId[i].checked == true) {
            arrayRowSelected[j] =window.document.listrenderer.rowId[i].value;
            j++
          }
        }
        if (listTypeId == 73) {
          parent.location.href = '<%=request.getContextPath()%>/contacts/view_individual.do?rowId=' + arrayRowSelected;
        } else {
          c_openPopup('/contacts/view_individual.do?rowId='+arrayRowSelected);
        }
      }
      return  false;
  }//end of if (listTypeId == 2)

  // Group Type
  if (listTypeId == 3){
    window.document.listrenderer.action="<%=request.getContextPath()%>/contacts/view_group.do";
    window.document.listrenderer.target="_parent";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 3)

  // Activity Type
  if (listTypeId >= 6 && listTypeId <= 14){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true)
      c_openPopup('/activities/view_activity.do?rowId='+document.listrenderer.rowId.value)
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openPopup('/activities/view_activity.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }//end of if (listTypeId >= 6 && listTypeId <= 14)

  // Notes Type
  if (listTypeId == 19){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        window.document.listrenderer.action="<%=request.getContextPath()%>/notes/view_note.do?TYPEOFOPERATION=EDIT&rowId="+document.listrenderer.rowId.value;
        window.document.listrenderer.method="post";
        window.document.listrenderer.target="_self";
        window.document.listrenderer.submit();
        return false;
      }
    }
    else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          window.document.listrenderer.action="<%=request.getContextPath()%>/notes/view_note.do?TYPEOFOPERATION=EDIT&rowId="+document.listrenderer.rowId[i].value;
          window.document.listrenderer.method="post";
          window.document.listrenderer.target="_self";
          window.document.listrenderer.submit();
          return false;
        }
      }
    }
    return false;
  }//end of if (listTypeId == 19)

  // Opportunity Type
  if (listTypeId == 4){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true)
        c_openPopup('/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&OPPORTUNITYID='+document.listrenderer.rowId.value)
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openPopup('/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&OPPORTUNITYID='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }//end of if (listTypeId == 4)

  // Proposal Type
  if (listTypeId == 5){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openPopup('/sales/view_proposal.do?TYPEOFOPERATION=EDIT&eventid='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openPopup('/sales/view_proposal.do?TYPEOFOPERATION=EDIT&eventid='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }//end of if (listTypeId == 5)

  // List Manager Type
  if (listTypeId == 20){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/marketing/view_listmanager.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_goTo('/marketing/view_listmanager.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // Promotions Type
  if (listTypeId == 21){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/marketing/view_promotion.do?promotionid='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_goTo('/marketing/view_promotion.do?promotionid='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // Literature Fulfillment Type
  if (listTypeId == 26){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openPopup('/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=EDIT&activityid='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openPopup('/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=EDIT&activityid='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // Event Type
  if (listTypeId == 22){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/marketing/view_event.do?TYPEOFOPERATION=EDIT&FromAttendee=NO&eventid='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_goTo('/marketing/view_event.do?TYPEOFOPERATION=EDIT&FromAttendee=NO&eventid='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  //Project Type
  if (listTypeId == 23){
  }//end of if (listTypeId == 23)

  //Task Type
  if (listTypeId == 24){
  }//end of if (listTypeId == 24)

  //TimeSlip Type
  if (listTypeId == 25){
  }//end of if (listTypeId == 25)

  // Ticket Type
  if (listTypeId == 15){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openPopup('/support/view_ticket.do?TYPEOFOPERATION=EDIT&rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openPopup('/support/view_ticket.do?TYPEOFOPERATION=EDIT&rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // File Type
  if (listTypeId == 38){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/files/file_dispatch.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_goTo('/files/file_dispatch.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

    // Employee List Type
  if (listTypeId == 46){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openPopup('/contacts/view_individual.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openPopup('/contacts/view_individual.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }
}

function vl_addMembers()
{
  listTypeId = document.listrenderer.listTypeId.value;
  if (listTypeId == 47){
    window.open("<html:rewrite page="/marketing/new_listmanager.do" />?listId="+document.listFormBean.listid.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=600,height=405');
    return false;
  }
  if (listTypeId == 50){
    c_lookup('attendee_lookup');
    return false;
  }
}

function vl_deleteList(deleteID)
{
  if (!vl_checkForCheckBox()) {
    return false;
  } //end of if statement (!vl_checkForCheckBox())

  listTypeId = document.listrenderer.listTypeId.value;

  // Entity Type
  if (listTypeId == 1 || listTypeId == 47) {
    var deletingDefaultEntity = false;
    <%--
    the first part of this if statement is to handle the event
    where there is only one checkbox in the list.
    --%>
    if (window.document.listrenderer.rowId.length == undefined)
    {
      if (window.document.listrenderer.rowId.checked == true && window.document.listrenderer.rowId.value == "1")
      {
        deletingDefaultEntity = true;
      } //end of if statement window.document.listrenderer.rowId.checked == true
    } else {
      for (i = 0; i < window.document.listrenderer.rowId.length; i++)
      {
        if (window.document.listrenderer.rowId[i].checked == true  && window.document.listrenderer.rowId[i].value == "1")
        {
          deletingDefaultEntity = true;
          break;
        } //end of if statement (window.document.listrenderer.rowId[i].checked == true
      } //end of for loop (i = 0; i < window.document.listrenderer.rowId.length; i++)
    } //end of else statement  (window.document.listrenderer.rowId.length == undefined)

    if (deletingDefaultEntity)
    {
      alert("<bean:message key='label.alert.cannotdeletedefaultentity'/>.");
      return false;
    } //end of if statement (deletingDefaultEntity)

    var condition=window.confirm("Are you sure you want to \n" + "permanently delete the record(s)?");
    if (condition)
    {
      window.document.listrenderer.action="<%=request.getContextPath()%>/contacts/delete_entitylist.do";
      window.document.listrenderer.target="_self";
      window.document.listrenderer.submit();
      return false;
    } //end of if statement (condition)
  }//end of if (listTypeId == 1 || listTypeId == 47)

  // Individual Type
  if (listTypeId == 2 || listTypeId == 73){
    var condition=window.confirm("Are you sure you want to \n" + "permanently delete the record(s)?");
    if (condition)
    {
      window.document.listrenderer.action="<%=request.getContextPath()%>/contacts/delete_individuallist.do";
      window.document.listrenderer.target="_self";
      window.document.listrenderer.submit();
    }
  }//end of if (listTypeId == 2 )

  // Group Type
  if (listTypeId == 3){
    window.document.listrenderer.action="<%=request.getContextPath()%>/contacts/delete_grouplist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 3)

  //Activities Type
  if (listTypeId == 6){
    window.document.listrenderer.action="<%=request.getContextPath()%>/activities/del_dup_activity.do?op=del";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }//end of if (listTypeId == 6)

  //Email Type
  if (listTypeId == 16){
    window.document.listrenderer.action="<%=request.getContextPath()%>/email/delete_message.do?folderId=<bean:write name="emailListForm" property="folderID" ignore="true" />";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return true;
  }//end of if (listTypeId == 16)

  //Notes Type
  if (listTypeId == 19 || listTypeId == 48){
    window.document.listrenderer.action="<%=request.getContextPath()%>/notes/delete_notelist.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }//end of if (listTypeId == 19 || listTypeId == 48)

  // Opportunity Type
  if (listTypeId == 4){
    window.document.listrenderer.action="<%=request.getContextPath()%>/sales/delete_opportunitylist.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }//end of if (listTypeId == 4)

  // Proposal Type
  if (listTypeId == 5){
    window.document.listrenderer.action="<%=request.getContextPath()%>/sales/delete_proposallist.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }//end of if (listTypeId == 5)

  //Notes Type
  if (listTypeId == 17){
    window.document.listrenderer.action="<%=request.getContextPath()%>/email/delete_rule.do?accountID="+deleteID;
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();

  }//end of if (listTypeId == 17)

  // List Manager Type
  if (listTypeId == 20){
    window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/delete_listmanager.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }

  // Promotions Type
  if (listTypeId == 21){
    window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/delete_promotionlist.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }

  // Literature Fulfillment Type
  if (listTypeId == 26 || listTypeId == 10) {
    window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/delete_literaturefulfillmentlist.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }
  // Event Type
  if (listTypeId == 22) {
    window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/delete_eventlist.do";
    window.document.listrenderer.method="post";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
  }

  //Project Type
  if (listTypeId == 23){
    window.document.listrenderer.action="<%=request.getContextPath()%>/projects/delete_projectlist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 23)

  //Task Type
  if (listTypeId == 24){
    window.document.listrenderer.action="<%=request.getContextPath()%>/projects/delete_tasklist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 24)

  //TimeSlip Type
  if (listTypeId == 25){
    window.document.listrenderer.action="<%=request.getContextPath()%>/projects/delete_timesliplist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 25)

  //Order Type
  if (listTypeId == 28){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_orderlist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 28)

  //Invoice Type
  if (listTypeId == 29){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_invoicelist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 29)

  //Payment Type
  if (listTypeId == 30){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_paymentlist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 30)

  //Expense Type
  if (listTypeId == 31){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_expenselist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 31)

  //Purchase Order Type
  if (listTypeId == 32){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_purchaseorderlist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 32)

  //Item Type
  if (listTypeId == 33){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_itemlist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 33)

  //Inventory Type
  if (listTypeId == 35){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_inventorylist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 35)

  //Vendor Type
  if (listTypeId == 36){
    window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/delete_vendorlist.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }//end of if (listTypeId == 36)

  // Knowledgebase Type
  if (listTypeId == 37){
    window.document.listrenderer.action="<%=request.getContextPath()%>/support/knowledgebase_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // Ticket Type
  if (listTypeId == 15){
    window.document.listrenderer.action="<%=request.getContextPath()%>/support/ticket_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // FAQ Type
  if (listTypeId == 27){
    window.document.listrenderer.action="<%=request.getContextPath()%>/support/faq_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // File Type
  if (listTypeId == 38){
    window.document.listrenderer.action="<%=request.getContextPath()%>/files/file_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // Expense Form Type
  if (listTypeId == 41){
    window.document.listrenderer.action="<%=request.getContextPath()%>/hr/expenseform_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // Time Sheet Type
  if (listTypeId == 44){
    window.document.listrenderer.action="<%=request.getContextPath()%>/hr/timesheet_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // Employee Handbook Type
  if (listTypeId == 45){
    window.document.listrenderer.action="<%=request.getContextPath()%>/hr/employeehandbook_delete.do";
    window.document.listrenderer.target="_self";
    window.document.listrenderer.submit();
    return false;
  }

  // Event Attendee Type
  if (listTypeId == 50){
    window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/delete_event_attendees.do";
    window.document.listrenderer.submit();
    return false;
  }

  // Contact Methods
  if (listTypeId == 39) {
    window.document.listrenderer.action="<%=request.getContextPath()%>/contacts/delete_contact_methods.do";
    window.document.listrenderer.submit();
    return false;
  }

  // User List
  if (listTypeId == 54) {
    if (window.confirm("Are you sure you want to \n" + "permanently delete the record(s)?")) {
      window.document.listrenderer.action = "<%=request.getContextPath()%>/administration/delete_user.do";
      window.document.listrenderer.submit();
    }
    return false;
  }

  // Attic List
  if (listTypeId == 69) {
    if (window.confirm("Are you sure you want to \n" + "permanently delete the record(s)?")) {
      window.document.listrenderer.action = "<%=request.getContextPath()%>/administration/delete_attic.do";
      window.document.listrenderer.submit();
    }
    return false;
  }

  // adhoc reports
  if (listTypeId == 70) {
    window.document.listrenderer.action="<%=request.getContextPath()%>/reports/delete_adhoc.do";
  window.document.listrenderer.submit();
  }
  return false;
}

function vl_duplicateList(){
  if (!vl_checkForCheckBox())
  {
    return false;
  } //end of if statement (!vl_checkForCheckBox())

  listTypeId = document.listrenderer.listTypeId.value;

  // Activity Type
  if (listTypeId >= 6 && listTypeId <= 14){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true)
        window.open('<%=request.getContextPath()%>/activities/del_dup_activity.do?op=dup&rowId='+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385')

    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          window.open('<%=request.getContextPath()%>/activities/del_dup_activity.do?op=dup&rowId='+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385')
        }
      }
    }
    return false;
  }//end of  if (listTypeId >= 6 && listTypeId <= 14)

  //Notes Type
  if (listTypeId == 19){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        window.open("<%=request.getContextPath()%>/notes/view_note.do?TYPEOFOPERATION=ADD&rowId="+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');
      }
    }
    else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          window.open("<%=request.getContextPath()%>/notes/view_note.do?TYPEOFOPERATION=ADD&rowId="+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');
        }
      }
    }
    return false;
  }//end of if (listTypeId == 19)

  // Opportunity Type
  if (listTypeId == 4){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true)
        window.open('<%=request.getContextPath()%>/sales/view_opportunity.do?Duplicate=true&TYPEOFOPERATION=ADD&OPPORTUNITYID='+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385')
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true)
          window.open('<%=request.getContextPath()%>/sales/view_opportunity.do?Duplicate=true&TYPEOFOPERATION=ADD&OPPORTUNITYID='+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385')
      }
    }
    return false;
  }//end of if (listTypeId == 4)

  // Proposal Type
  if (listTypeId == 5){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        window.open("<%=request.getContextPath()%>/sales/duplicate_proposal.do?eventid="+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          rowSelected =rowSelected+1;
        }
      }
      if(rowSelected == 1){
        for (i=0;i<document.listrenderer.rowId.length;i++) {
          if(document.listrenderer.rowId[i].checked == true) {
            window.open("<%=request.getContextPath()%>/sales/duplicate_proposal.do?eventid="+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');
          }
        }
      } else {
        alert("<bean:message key='label.alert.selectonlyonerecord'/>.");
      }
    }
    return false;
  }//end of if (listTypeId == 5)

  // Literature Fulfillment Type
  if (listTypeId == 26) {
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true)
        c_openPopup('/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=ADD&activityid='+document.listrenderer.rowId.value);
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true)
          c_openPopup('/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=ADD&activityid='+document.listrenderer.rowId[i].value);
      }
    }
    return false;
  }

  // Promotions Type
  if (listTypeId == 21) {
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true)
        c_goTo('/marketing/dup_promotion.do?TYPEOFOPERATION=ADDITEM&duplicateId='+document.listrenderer.rowId.value);
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true)
        c_goTo('/marketing/dup_promotion.do?TYPEOFOPERATION=ADDITEM&duplicateId='+document.listrenderer.rowId[i].value);
      }
    }
    return false;
  }

  // Event Type
  if (listTypeId == 22){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/marketing/view_event.do?TYPEOFOPERATION=DUPLICATE&FromAttendee=NO&eventid='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_goTo('/marketing/view_event.do?TYPEOFOPERATION=DUPLICATE&FromAttendee=NO&eventid='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  //Project Type
  if (listTypeId == 23){

      var totalRow=document.listrenderer.noofrecord.value;
      var rowSelected = 0;
      if (totalRow == 1) {
        if(document.listrenderer.rowId.checked == true) {
          window.open("<%=request.getContextPath()%>/projects/duplicate_project.do?rowId="+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=no,width=745,height=345');
        }
      }
      else
      {

        for (i=0;i<document.listrenderer.rowId.length;i++)
        {
          if(document.listrenderer.rowId[i].checked == true)
          {
            if(rowSelected == 3)
              return false;
            window.open("<%=request.getContextPath()%>/projects/duplicate_project.do?rowId="+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=no,width=745,height=345');
            rowSelected++;
          }
        }
        return false;
      }
  }//end of if (listTypeId == 23)

  //Task Type
  if (listTypeId == 24){
      var totalRow=document.listrenderer.noofrecord.value;
      var rowSelected = 0;
      if (totalRow == 1) {
        if(document.listrenderer.rowId.checked == true) {
          window.open("<%=request.getContextPath()%>/projects/duplicate_task.do?rowId="+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=no,width=745,height=345');
        }
      }
      else
      {

        for (i=0;i<document.listrenderer.rowId.length;i++)
        {
          if(document.listrenderer.rowId[i].checked == true)
          {
            if(rowSelected == 3)
              return false;
            window.open("<%=request.getContextPath()%>/projects/duplicate_task.do?rowId="+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=no,width=745,height=345');
            rowSelected++;
          }
        }
        return false;
      }

  }//end of if (listTypeId == 24)

  //TimeSlip Type
  if (listTypeId == 25){
      var totalRow=document.listrenderer.noofrecord.value;
      var rowSelected = 0;
      if (totalRow == 1) {
        if(document.listrenderer.rowId.checked == true) {
          window.open("<%=request.getContextPath()%>/projects/duplicate_timeslip.do?rowId="+document.listrenderer.rowId.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=no,width=745,height=345');
        }
      }
      else
      {

        for (i=0;i<document.listrenderer.rowId.length;i++)
        {
          if(document.listrenderer.rowId[i].checked == true)
          {
            if(rowSelected == 3)
              return false;
            window.open("<%=request.getContextPath()%>/projects/duplicate_timeslip.do?rowId="+document.listrenderer.rowId[i].value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=no,width=745,height=345');
            rowSelected++;
          }
        }
        return false;
      }

  }//end of if (listTypeId == 25)

  //Order Type
  if (listTypeId == 28){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/duplicate_order.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }
  }//end of if (listTypeId == 28)

  //Invoice Type
  if (listTypeId == 29){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/duplicate_invoice.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }

  }//end of if (listTypeId == 29)

  //Payment Type
  if (listTypeId == 30){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/duplicate_payment.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }

  }//end of if (listTypeId == 30)

  //Expense Type
  if (listTypeId == 31){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/duplicate_expense.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }

  }//end of if (listTypeId == 31)

  //Purchase Order Type
  if (listTypeId == 32){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/duplicate_purchaseorder.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }

  }//end of if (listTypeId == 32)

  //Item Type
  if (listTypeId == 33){

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        window.document.listrenderer.action = "<%=request.getContextPath()%>/accounting/view_item.do?TYPEOFOPERATION=ADD&rowId="+document.listrenderer.rowId.value;
        window.document.listrenderer.target="_parent";
        window.document.listrenderer.submit();
      }
    }
    else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          window.document.listrenderer.action = "<%=request.getContextPath()%>/accounting/view_item.do?TYPEOFOPERATION=ADD&rowId="+document.listrenderer.rowId[i].value;
          window.document.listrenderer.target="_parent";
          window.document.listrenderer.submit();
        }
      }
    }
  }//end of if (listTypeId == 33)

  //Inventory Type
  if (listTypeId == 35){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/duplicate_inventory.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }

  }//end of if (listTypeId == 35)

  //Vendor Type
  if (listTypeId == 36){
    var i  = 0;
    var count = 0;

    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        count++;
      }
    }
    else{
      for (i=0 ;i<document.listrenderer.elements.length;i++)
      {
        if (document.listrenderer.elements[i].type=="checkbox")
        {
          if ((document.listrenderer.elements[i].checked)
          && document.listrenderer.elements[i].value != "on")
          {
            count ++;
          }
        }
      }
    }

    if(count == 1)
    {
     window.document.listrenderer.action="<%=request.getContextPath()%>/contacts/duplicate_entity.do";
     window.document.listrenderer.target="_parent";
     window.document.listrenderer.submit();
     return true;
    }
    else if(count > 1)
    {
     alert("<bean:message key='label.alert.selectoneitemforduplicate'/>!");
     return false;
    }
  }//end of if (listTypeId == 36)

  // Knowledgebase Type
  if (listTypeId == 37){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openWindow('/support/knowledgebase_dup.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
          c_openWindow('/support/knowledgebase_dup.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // Ticket Type
  if (listTypeId == 15){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openWindow('/support/view_ticket.do?TYPEOFOPERATION=DUPLICATE&rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
            c_openWindow('/support/view_ticket.do?TYPEOFOPERATION=DUPLICATE&rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // FAQ Type
  if (listTypeId == 27){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/support/faq_view.do?typeofoperation=DUPLICATE&rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
            c_goTo('/support/faq_view.do?typeofoperation=DUPLICATE&rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // File Type
  if (listTypeId == 38){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openPopup('/files/dup_dispatch.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
            c_openPopup('/files/dup_dispatch.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // Expense Form Type
  if (listTypeId == 41){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_goTo('/hr/expenseform_dup.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
            c_goTo('/hr/expenseform_dup.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }

  // Expense Form Type
  if (listTypeId == 45){
    var totalRow=document.listrenderer.noofrecord.value;
    var rowSelected = 0;
    if (totalRow == 1) {
      if(document.listrenderer.rowId.checked == true) {
        c_openPopup('/hr/employeehandbook_dup.do?rowId='+document.listrenderer.rowId.value)
      }
    } else {
      for (i=0;i<document.listrenderer.rowId.length;i++) {
        if(document.listrenderer.rowId[i].checked == true) {
            c_openPopup('/hr/employeehandbook_dup.do?rowId='+document.listrenderer.rowId[i].value)
        }
      }
    }
    return false;
  }
}


function vl_disableList(accountID)
{
  if (!vl_checkForCheckBox()){
    return false;
  }

  var ruleIDs = "";
  if (document.forms.listrenderer.rowId.length == undefined){
    if (document.forms.listrenderer.rowId.checked == true){
      ruleIDs = document.forms.listrenderer.rowId.value;
  }
  }else{
    var flag = 0;
    for (var i=0; i<document.forms.listrenderer.rowId.length; i++){
      if (document.forms.listrenderer.rowId[i].checked == true){
        if (flag == 1){
          // Stick a comma between checked ids.
          ruleIDs = ruleIDs + ",";
        }
        ruleIDs = ruleIDs + document.forms.listrenderer.rowId[i].value;
        flag = 1;
      }
    }
  }
  c_goTo('/email/enable_rule.do?ruleIDs='+ruleIDs+'&accountID='+accountID+'&status=disable');
  return true;
}

function vl_enableList(accountID)
{
  if (!vl_checkForCheckBox()){
    return false;
  }

  var ruleIDs = "";
  if (document.forms.listrenderer.rowId.length == undefined){
    if (document.forms.listrenderer.rowId.checked == true){
      ruleIDs = document.forms.listrenderer.rowId.value;
    }
  }else{
    var flag = 0;
    for (var i=0; i<document.forms.listrenderer.rowId.length; i++){
      if (document.forms.listrenderer.rowId[i].checked == true){
        if (flag == 1){
          // Stick a comma between checked ids.
          ruleIDs = ruleIDs + ",";
        }
        ruleIDs = ruleIDs + document.forms.listrenderer.rowId[i].value;
        flag = 1;
      }
    }
  }
  c_goTo('/email/enable_rule.do?ruleIDs='+ruleIDs+'&accountID='+accountID+'&status=enable');
  return true;
}

function vl_moveToAttic()
{
  if (vl_checkForCheckBox()) {
    document.listrenderer.action = "<html:rewrite page="/administration/attic_restore.do"/>?method=moveToAttic";
    document.listrenderer.submit();
  }
}

function vl_viewCustomFields()
{
  if (vl_checkForRowSelected() != 1) {
    alert("<bean:message key='label.alert.selectonerecordtoview'/>.");
    return false;
  }

  var fieldid = 0;

  for (var i=0; i<document.forms.listrenderer.rowId.length; i++) {
    if (document.forms.listrenderer.rowId[i].checked == true) {
      fieldid = document.forms.listrenderer.rowId[i].value;
      break;
    }
  }

  if (fieldid > 0) {
    c_goTo('/administration/view_custom_field.do?fieldid=' + fieldid);
  }
}