<%--
 * $RCSfile: attachments.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:form action="/email/attachment_lookup.do" enctype="multipart/form-data">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="49%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.email.attachfilefromcw"/></td>
        </tr>
        <tr>
          <td class="contentCell">
	          <html:text property="fileNameFromCent" styleClass="inputBox" size="35"/>
            <input name="button801922" type="button" class="normalButton" value="<bean:message key='label.value.lookup'/>" onClick="c_openWindow('/files/file_lookup.do', '', 400, 400,'');">
          </td>
        </tr>
        <tr>
          <td class="contentCell" style="height:25px;"><html:img page="/images/spacer.gif" width="1" height="1" border="0" alt="" /></td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.email.attachfilefromdesktop"/></td>
        </tr>
        <tr>
          <td class="contentCell">
						<html:file property="file" size="35"/>
          </td>
        </tr>
      </table>
    </td>
    <td width="2%" align="center" valign="middle">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td>
						<app:cvbutton property="attach" styleClass="normalButton" style="width:65px;" onclick="attachFile()">
              <bean:message key="label.email.attach"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td>
						<app:cvbutton property="remove" styleClass="normalButton" style="width:65px;" onclick="removeFile()">
              <bean:message key="label.email.remove"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td width="49%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.email.attachedfiles"/></td>
        </tr>
        <tr>
          <td class="contentCell">
	          <html:select property="fileList" size="7" multiple="true" style="width:250px;" styleClass="inputBox">
				      <c:forEach items="${fileList}" var="current">
				      	<option value="<c:out value="${current.strid}"/>"><c:out value="${current.name}"/></option>
				      </c:forEach>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type="hidden" name="fileId">
</html:form>

<script language="javascript">
function setData(lookupValues) {
  document.forms[0].fileId.value = lookupValues.idValue;
  document.forms[0].fileNameFromCent.value = lookupValues.Name;
}
function attachFile() {
	populateAttachment();
	var filename = document.forms[0].file.value;
	var fileNameFromCent = document.forms[0].fileNameFromCent.value;

	if (filename == "" && fileNameFromCent == "") {
		alert( "<bean:message key='label.alert.selectfile'/>!" );
		return;
	}
	var index;
	var file;

	if (filename != "") {
		file = filename.substring( filename.lastIndexOf("\\")+1  );
	} else {
		file = fileNameFromCent;
	}

	var m = document.forms[0].fileList.options.length;

	if (filename != "") {
		document.forms[0].action="<%=request.getContextPath()%>/email/attachment_lookup.do?From=Local";
	} else if (fileNameFromCent != null) {
		document.forms[0].action="<%=request.getContextPath()%>/email/attachment_lookup.do?From=Server&FileID="+document.forms[0].fileId.value+"&fileName="+file;
	}
  document.forms[0].submit();
  m = m+1;
}

function removeFile() {
	if (document.forms[0].fileList.selectedIndex < 0) {
		alert( "<bean:message key='label.alert.selectfile'/>!" );
		return;
	} else {
		var filename = document.forms[0].fileList[document.forms[0].fileList.selectedIndex].text ;
		document.forms[0].fileList.options[document.forms[0].fileList.selectedIndex]=null;
	}
}

function populateAttachment( ){
	if (document.forms[0].fileList != null)	{
		var lengthSelected = window.document.forms[0].fileList.options.length;
		for (var i=0;i<lengthSelected;i++)	{
			window.document.forms[0].fileList.options[i].selected=true;
		}
	}
}

function closeAttachWindow() {
	opener.removeAttachmentsContent();
	var i=0;
	while(i < document.forms[0].fileList.length) {
		var strAttach	= document.forms[0].fileList[i].text
		var strAttachID = document.forms[0].fileList[i].value
		opener.updateAttachments(strAttach, strAttachID);
		i++;
	}
  if ( opener.finishAttachments) {
    opener.finishAttachments();
  }
  window.close();
}

</script>