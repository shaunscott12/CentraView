<%--
 * $RCSfile: group_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<script language="javascript">
<!--
<c:if test="${not empty param.launchAddMember}">
c_lookup('attendee_lookup');
</c:if>
function saveToAddMembers()
{
  var form = document.getElementById('groupForm');
  form.action = '<html:rewrite page="/contacts/save_group.do?button3=true"/>';
  form.submit();
}
function setView(individualLookupValues)
{
  dataValues = individualLookupValues.idValue;
  // the dataValues is a comma separated list of individualIds
  var groupId = document.getElementById('groupId');
  c_goTo('/contacts/add_group_member.do?memberId='+dataValues+'&groupId='+groupId.value);
}
function removeMember()
{
  var dataValues = "";
  if (!vl_checkForCheckBox()) {
    return false;
  }
  i = 0;
  if (document.forms.listrenderer.rowId.length == undefined) {
    if (document.forms.listrenderer.rowId.checked) {
      var value = window.document.forms.listrenderer.rowId.value;
      dataValues = dataValues + value;
    }
  }else{
    while (i<document.forms.listrenderer.rowId.length) {
      if (document.forms.listrenderer.rowId[i].checked) {
        var value = window.document.forms.listrenderer.rowId[i].value;
        if (dataValues=="") {
          dataValues = value;
        }else{
          dataValues = dataValues + ","+value;
        }
        i++;
      }else{
        i++;
      }
    }
  }
  var groupId = document.getElementById('groupId');
  c_goTo('/contacts/remove_group_member.do?memberId='+dataValues+'&groupId='+groupId.value);
}
//-->
</script>
<html:form action="/contacts/save_group" styleId="groupForm">
<html:hidden property="groupid" styleId="groupId" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.contacts.groupdetails"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell" width="20"><bean:message key="label.contacts.groupname"/>: </td>
    <td class="contentCell"><html:text property="groupname" styleClass="inputBox" size="34" /></td>
  </tr>
  <tr>
    <td class="labelCell" width="20"><bean:message key="label.contacts.description"/>: </td>
    <td class="contentCell"><html:textarea property="groupdescription" cols="31" rows="4" styleClass="inputBox" /></td>
  </tr>
  <c:if test="${groupform.map.create != '0'}">
  <tr>
    <td class="labelCell" width="20"><bean:message key="label.contacts.created"/>: </td>
    <td class="contentCell"><c:out value="${groupform.map.create}"/></td>
  </tr>
  </c:if>
  <c:if test="${groupform.map.modify != '0'}">
  <tr>
    <td class="labelCell" width="20"><bean:message key="label.contacts.modified"/>: </td>
    <td class="contentCell"><c:out value="${groupform.map.modify}"/></td>
  </tr>
  </c:if>
  <tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:submit property="save_new" styleClass="normalButton"><bean:message key='label.contacts.saveandnew'/></html:submit>
      <html:submit property="save_close" styleClass="normalButton"><bean:message key='label.contacts.saveandclose'/></html:submit>
      <input type="button" name="cancel" value="<bean:message key='label.contacts.cancel'/>" class="normalButton" onclick="c_goTo('/contacts/group_list.do');">
      <c:if test="${not empty param.new}">
      <input type="button" name="properties" value="<bean:message key='label.contacts.properties'/>" class="normalButton" onclick="c_openPermission('<c:out value="${requestScope.recordType}"/>', <c:out value="${requestScope.recordId}"/>, '<c:out value="${requestScope.recordName}"/>');">
      </c:if>
    </td>
  </tr>
</table>
<p/>
<html:hidden property="owner" />
<input type=hidden name="listId" value="<c:out value="${param.listId}"/>">
</html:form>
<app:valuelist listObjectName="valueList" />
