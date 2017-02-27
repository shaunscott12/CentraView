<%--
 * $RCSfile: contact_top_buttons.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<script language="javascript">
<!--
  function scheduleActivity(activityType)
  {
    var parameters = '?TYPEOFACTIVITY='+activityType.value;
   	<c:choose>
  	  <c:when test="${requestScope.recordType == 'Individual'}">
  	    parameters += '&entityID=<c:out value="${requestScope.parentId}"/>&entityName=<c:out value="${requestScope.parentName}" />&individualID=<c:out value="${requestScope.recordId}"/>&individualName=<c:out value="${requestScope.recordName}"/>';
  	  </c:when>
  	  <c:otherwise>
    	  parameters += '&entityID=<c:out value="${requestScope.recordId}"/>&entityName=<c:out value="${requestScope.recordName}"/>';
  	  </c:otherwise>
  	</c:choose>
    c_openWindow('/activities/activity_dispatch.do'+parameters, 'sched_act', 780, 580, '');
  }

  function newContact()
  {
    var parameters;
   	<c:choose>
  	  <c:when test="${requestScope.recordType == 'Individual'}">
  	    parameters = "entityName=<c:out value="${requestScope.parentName}"/>&entityNo=<c:out value="${requestScope.parentId}"/>&marketingList=<c:out value="${requestScope.marketingList}"/>";
  	    c_openWindow('/contacts/new_individual.do?'+parameters, 'newIndividual', 729, 301, '');
  	  </c:when>
  	  <c:otherwise>
  	    parameters = "marketingList=<c:out value="${requestScope.marketingList}"/>";
    	  c_openWindow('/contacts/new_entity.do?'+parameters, 'newEntity', 729, 301, '');
  	  </c:otherwise>
  	</c:choose>
  }

  function deleteContact()
  {
   	<c:choose>
  	  <c:when test="${requestScope.recordType == 'Individual'}">
  	    c_goTo('/contacts/delete_individual.do?rowId=<c:out value="${requestScope.recordId}"/>');
  	  </c:when>
  	  <c:otherwise>
  	    c_goTo('/contacts/delete_entity.do?rowId=<c:out value="${requestScope.recordId}"/>');
  	  </c:otherwise>
  	</c:choose>
  }

  function duplicateContact()
  {
   	<c:choose>
  	  <c:when test="${requestScope.recordType == 'Individual'}">
  	    c_openWindow('/common/duplicate.do?typeOfContact=individual&listType=Individual&marketingListId=<c:out value="${requestScope.marketingList}"/>&selectId=<c:out value="${requestScope.recordId}"/>', '', 729, 301, '');
  	  </c:when>
  	  <c:otherwise>
  	    c_openWindow('/common/duplicate.do?typeOfContact=entity&listType=Entity&marketingListId=<c:out value="${requestScope.marketingList}"/>&selectId=<c:out value="${requestScope.recordId}"/>', '', 729, 301, '');
  	  </c:otherwise>
  	</c:choose>
  }

  function copyTo()
  {
   	<c:choose>
  	  <c:when test="${requestScope.recordType == 'Individual'}">
  	    c_openWindow('/contacts/view_individual.do?rowId=<c:out value="${requestScope.recordId}"/>&copyTo=true','',340,300,'');
  	  </c:when>
  	  <c:otherwise>
  	    c_openWindow('/contacts/view_entity.do?rowId=<c:out value="${requestScope.recordId}"/>&copyTo=true','',340,300,'');
  	  </c:otherwise>
  	</c:choose>
  }
//-->
</script>
<tr>
  <td class="detailButtonHeader">
    <input type="button" name="newContact" value="<bean:message key='label.value.new'/>" class="normalButton" onclick="newContact();">
    <input type="submit" name="deleteContact" value="<bean:message key='label.value.delete'/>" class="normalButton" onclick="deleteContact();">
    <input type="submit" name="duplicateContact" value="<bean:message key='label.value.duplicate'/>" class="normalButton" onclick="duplicateContact();">
    <input type="button" name="copyContact" value="<bean:message key='label.value.copyto'/>" class="normalButton" onclick="copyTo();">
    <input type="button" name="properties" value="<bean:message key='label.value.properties'/>" class="normalButton" onclick="c_openPermission('<c:out value="${requestScope.recordType}"/>', <c:out value="${requestScope.recordId}"/>, '<c:out value="${requestScope.recordName}"/>');">
    <input type="button" value=" ? " class="normalButton"  onclick="c_goHelp();">
  </td>
  <td align="right" class="detailButtonHeader" nowrap>
    <a href="javascript:void(0);" class="plainLink" onclick="goForPrint();"><html:img page="/images/button_print.gif" height="17" width="19" alt="" border="0" align="absbottom"/></a>
    <select name="select2" onchange="scheduleActivity(this)" class="styledDropdown">
      <option value=""><bean:message key="label.tiles.scheduleactivity"/></option>
      <option value="APPOINTMENT"><bean:message key="label.tiles.appointment"/></option>
      <option value="CALL"><bean:message key="label.tiles.call"/></option>
      <option value="MEETING"><bean:message key="label.tiles.meeting"/></option>
      <option value="NEXTACTION"><bean:message key="label.tiles.nextaction"/></option>
      <option value="TODO"><bean:message key="label.tiles.todo"/></option>
    </select>
  </td>
</tr>
</table>