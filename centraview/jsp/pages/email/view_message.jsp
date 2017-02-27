<%--
 * $RCSfile: view_message.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:form action="/email/view_message">
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td align="right" class="labelCellBold"><bean:message key="label.email.from"/> </td>
    <td class="contentCell"><a class="plainLink" onclick="c_openWindow('/email/compose.do', '', 720, 585, '');"><bean:write name="emailMessageForm" property="from" ignore="true" /></a></td>
  </tr>
  <tr valign="top">
    <td align="right" class="labelCellBold"><bean:message key="label.email.to"/> </td>
  	<td class="contentCell">
    <c:set var="count" value="0" />
    <c:forEach var="to" items="${emailMessageForm.map.toList}">
    	<c:choose>
    		<c:when test="${count == toSize-1}">
    			<a href="javascript:void(0)" class="plainLink" onclick="c_openWindow('/email/compose.do?to=<c:out value="${to}"/>', '', 720, 585, '');"><c:out value="${to}"/></a>
    		</c:when>
     		<c:when test="${count % 2 != 0}">
			    <a href="javascript:void(0)" class="plainLink" onclick="c_openWindow('/email/compose.do?to=<c:out value="${to}"/>', '', 720, 585, '');"><c:out value="${to}"/></a>,<br>
      	</c:when>
      	<c:when test="${count % 2 == 0}">
			    <a href="javascript:void(0)" class="plainLink" onclick="c_openWindow('/email/compose.do?to=<c:out value="${to}"/>', '', 720, 585, '');"><c:out value="${to}"/></a>,
 	     	</c:when>
  	  </c:choose>
    	<c:set var="count" value="${count + 1}"/>
    </c:forEach>
	  </td>
  </tr>
  <tr valign="top">
    <td align="right" class="labelCellBold"><bean:message key="label.email.cc"/> </td>
    <td class="contentCell">
    <c:set var="count" value="0" />
    <c:forEach var="cc" items="${emailMessageForm.map.ccList}">
    	<c:choose>
    		<c:when test="${count == ccSize-1}">
    		  <a href="javascript:void(0)" class="plainLink" onclick="c_openWindow('/email/compose.do?to=<c:out value="${cc}"/>', '', 720, 585, '');"><c:out value="${cc}"/></a>
    		</c:when>
     		<c:when test="${count % 2 != 0}">
			    <a href="javascript:void(0)" class="plainLink" onclick="c_openWindow('/email/compose.do?to=<c:out value="${cc}"/>', '', 720, 585, '');"><c:out value="${cc}"/></a>,<br>
      	</c:when>
      	<c:when test="${count % 2 == 0}">
			    <a href="javascript:void(0)" class="plainLink" onclick="c_openWindow('/email/compose.do?to=<c:out value="${cc}"/>', '', 720, 585, '');"><c:out value="${cc}"/></a>,
 	     	</c:when>
  	  </c:choose>
    	<c:set var="count" value="${count + 1}"/>
    </c:forEach>
    </td>
  </tr>
  <tr>
    <td align="right" class="labelCellBold"><bean:message key="label.email.date"/> </td>
    <td class="contentCell"><bean:write name="emailMessageForm" property="messageDate" ignore="true" format="MMMM dd, yyyy hh:mm a" /></td>
  </tr>
  <tr>
    <td align="right" class="labelCellBold"><bean:message key="label.email.subject"/> </td>
    <td class="contentCell"><bean:write name="emailMessageForm" property="subject" ignore="true" /></td>
  </tr>
  <tr valign="top">
    <td align="right" class="labelCellBold"><bean:message key="label.email.attachements"/>:</td>
    <td class="contentCell">
    <c:set var="count" value="0" />
    <c:forEach var="attachment" items="${emailMessageForm.map.attachmentList}">
	    <c:choose>
	    	<c:when test="${count == attachmentSize-1}">
    	    <a href="<%=request.getContextPath()%>/files/file_download.do?fileid=<c:out value="${attachment.fileId}" />&filename=<c:out value="${attachment.title}" />" class="contactTableLink"><c:out value="${attachment.title}"/></a>
        </c:when>
  	    <c:when test="${count % 2 != 0}">
    	    <a href="<%=request.getContextPath()%>/files/file_download.do?fileid=<c:out value="${attachment.fileId}" />&filename=<c:out value="${attachment.title}" />" class="contactTableLink"><c:out value="${attachment.title}"/></a>,<br>
        </c:when>
        <c:when test="${count % 2 == 0}">
    	    <a href="<%=request.getContextPath()%>/files/file_download.do?fileid=<c:out value="${attachment.fileId}" />&filename=<c:out value="${attachment.title}" />" class="contactTableLink"><c:out value="${attachment.title}"/></a>,
        </c:when>
			</c:choose>
      <c:set var="count" value="${count + 1}"/>
    </c:forEach>
    </td>
  </tr>
  <tr valign="middle">
		<td align="right" valign="top" class="labelCellBold"><bean:message key="label.email.private"/></td>
		<td valign="top" class="contentCell">
			<html:radio property="private" value="YES" onclick="javascript:privateList('YES');"/> <bean:message key="label.email.yes"/>
			<html:radio property="private" value="NO" onclick="javascript:privateList('NO');"/> <bean:message key="label.email.no"/>
		</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader">
      <table border="0" cellspacing="0" cellpadding="0">
        <tr valign="bottom">
          <td class="pagingButtonTD"><input type="button" name="reply" value="<bean:message key='label.value.reply'/>" onclick="openReply(false);" class="normalButton"></td>
          <td class="pagingButtonTD"><input type="button" name="replyall" value="<bean:message key='label.value.replytoall'/>" onclick="openReply(true);" class="normalButton"></td>
          <td class="pagingButtonTD"><input type="button" name="forward" value="<bean:message key='label.value.forward'/>" onclick="openForward();" class="normalButton"></td>
          <td class="pagingButtonTD">
	          <app:cvbutton property="delete" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg="label.general.blank" onclick="deleteEmail()">
              <bean:message key="label.email.delete"/>
    	      </app:cvbutton>
    	    </td>
          <td class="pagingButtonTD">
          	<select class="pagingDropDown" onchange="moveTo(this.value);">
          		<option value="-1"><bean:message key="label.email.moveto"/></option>
          	<c:forEach var="folder" items="${emailMessageForm.map.folderList}">
	        		<c:if test="${(folder.value != 'root') && (folder.key != emailMessageForm.map.folderID)}">
           			<option value="<c:out value="${folder.key}"/>"><c:out value="${folder.value}"/></option>
            	</c:if>
          	</c:forEach>
          </td>
          <td class="pagingButtonTD">
          	<c:if test="${emailMessageForm.map.previousMessage > 0}">
          		<html:link page="/email/view_message.do" styleClass="plainLink" paramId="messageID" paramName="emailMessageForm" paramProperty="previousMessage">
                <bean:message key="label.email.previous"/>
          		</html:link>
          		<c:if test="${emailMessageForm.map.nextMessage > 0}">|</c:if>
	          </c:if>
          </td>
          <td class="pagingButtonTD">
	          <c:if test="${emailMessageForm.map.nextMessage > 0}">
  	          <html:link page="/email/view_message.do" styleClass="plainLink" paramId="messageID" paramName="emailMessageForm" paramProperty="nextMessage">
                <bean:message key="label.email.next"/>
  	          </html:link>
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="emailInputBox" width="100%">
    	<table width="100%" border="0" cellpadding="3">
	      <tr>
  	      <td class="inputBox">
    	      <bean:write name="emailMessageForm" property="body" ignore="true" filter="false" />
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="sectionHeader">
      <table border="0" cellspacing="0" cellpadding="0">
        <tr valign="bottom">
          <td class="pagingButtonTD"><input type="button" name="reply" value="<bean:message key='label.value.reply'/>" onclick="openReply(false);" class="normalButton"></td>
          <td class="pagingButtonTD"><input type="button" name="replyall" value="<bean:message key='label.value.replytoall'/>" onclick="openReply(true);" class="normalButton"></td>
          <td class="pagingButtonTD"><input type="button" name="forward" value="<bean:message key='label.value.forward'/>" onclick="openForward();" class="normalButton"></td>
          <td class="pagingButtonTD">
	          <app:cvbutton property="delete" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg="label.general.blank" onclick="deleteEmail()">
              <bean:message key="label.email.delete"/>
    	      </app:cvbutton>
    	    </td>
          <td class="pagingButtonTD">
          	<select class="pagingDropDown" onchange="moveTo(this.value);">
          		<option value="-1"><bean:message key="label.email.moveto"/></option>
          	<c:forEach var="folder" items="${emailMessageForm.map.folderList}">
	        		<c:if test="${(folder.value != 'root') && (folder.key != emailMessageForm.map.folderID)}">
           			<option value="<c:out value="${folder.key}"/>"><c:out value="${folder.value}"/></option>
            	</c:if>
          	</c:forEach>
          </td>
          <td class="pagingButtonTD">
          	<c:if test="${emailMessageForm.map.previousMessage > 0}">
          		<html:link page="/email/view_message.do" styleClass="plainLink" paramId="messageID" paramName="emailMessageForm" paramProperty="previousMessage">
                <bean:message key="label.email.previous"/>
          		</html:link>
          		<c:if test="${emailMessageForm.map.nextMessage > 0}">|</c:if>
	          </c:if>
          </td>
          <td class="pagingButtonTD">
	          <c:if test="${emailMessageForm.map.nextMessage > 0}">
  	          <html:link page="/email/view_message.do" styleClass="plainLink" paramId="messageID" paramName="emailMessageForm" paramProperty="nextMessage">
                <bean:message key="label.email.next"/>
  	          </html:link>
            </c:if>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form>

<script language="Javascript">
	function privateList(privateType) {
		c_goTo('/email/private_message.do?privateType='+privateType+'&messageID=<bean:write name="emailMessageForm" property="messageID" ignore="true" />&folderID=<bean:write name="emailMessageForm" property="folderID" ignore="true"/>');
		return true;
	}

	function openReply(replyToAll) {
    if (replyToAll) {
      replyAll = "true";
    } else {
      replyAll = "false";
    }
    c_openWindow('/email/reply.do?messageID=<bean:write name="emailMessageForm" property="messageID" ignore="true" />&replyToAll=' + replyAll, '', 720, 585, '');
  }

  function openForward() {
  	c_openWindow('/email/forward.do?messageID=<bean:write name="emailMessageForm" property="messageID" ignore="true" />', '', 720, 585, '');
  }

  function deleteEmail() {
		c_goTo("/email/delete_message.do?rowId=<c:out value="${emailMessageForm.map.messageID}"/>&folderId=<bean:write name="emailMessageForm" property="folderID" ignore="true"/>");
	}

	function moveTo(newFolderID) {
    c_goTo('/email/move_message.do?rowId=<bean:write name="emailMessageForm" property="messageID" ignore="true" />&newFolderID=' + newFolderID + '&folderID=<bean:write name="emailMessageForm" property="folderID" ignore="true"/>');
  }
</script>