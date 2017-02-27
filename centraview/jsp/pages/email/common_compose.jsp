<%--
 * $RCSfile: common_compose.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%-- BEGIN FCKEditor CODE --%>
<c:if test="${composeMailForm.map.composeInHTML}">
<script type="text/javascript" src="<%=request.getContextPath()%>/fckeditor/fckeditor.js"></script>
<script language="Javascript1.2">
<!--
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor('body', '100%', '230', 'cvDefault', '');
    oFCKeditor.ReplaceTextarea();
  }
-->
</script>
</c:if>
<%-- END FCKEditor CODE --%>
<html:form action="/email/common_send.do" onsubmit="populateAttachment()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
	<html:errors/>
	<td class="popupTextBox">
		<html:messages id="msg" message="true">
			<b><c:out value="${msg}"/></b>
	</html:messages>
	</td>
</tr>
<tr>
  <td class="headerBG">
    <input type="button" name="send" value="<bean:message key='label.value.send'/>" class="normalButton" onclick="disableSend()">
    <input type="button" name="cancel" value="<bean:message key='label.cancel'/>" class="normalButton" onclick="window.close();">
  </td>
</tr>
</table>
<table border="0" cellspacing="1" cellpadding="3" class="mediumTableBorders" width="100%">
  <tr>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="2" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td colspan="2">&nbsp;</td>
          <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td class="labelCellBold" align="right"><bean:message key="label.email.from"/> </td>
          <td class="contentCell"><html:text property="from" styleClass="inputBox" size="45"/></td>
        </tr>
        <tr>
          <td class="labelCellBold" align="right"><bean:message key="label.email.replyto"/></td>
          <td class="contentCell"><html:text property="replyTo" styleClass="inputBox" size="45"/></td>
        </tr>
        <tr>
          <td class="labelCellBold" align="right"><bean:message key='label.value.subject'/>:</td>
          <td class="contentCell"><html:text property="subject" styleClass="inputBox" size="45"/></td>
        </tr>
      </table>
    </td>
    <td width="50%" class="mediumTableBorders" align="center">
      <table border="0" cellspacing="3" cellpadding="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key='label.value.attachments'/>:</td>
        </tr>
        <tr>
          <td class="contentCell">
          	<html:select property="attachments" styleClass="inputBox" multiple="true" size="5" style="width:20em;">
           		<html:optionsCollection property="attachmentList" value="strid" label="name" />
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <input type="button" class="normalButton" name="attachButton" value="<bean:message key='label.value.attachfile'/>" onclick="attachmentLookup()"/>
            <input type="button" class="normalButton" name="removeButton" value="<bean:message key='label.value.removefile'/>" onclick="removeFile()"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td align="center">
      <html:textarea property="body" cols="82" rows="18" style="width:675px;"></html:textarea>
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
  <td class="headerBG">
    <input type="button" name="send" value="<bean:message key='label.value.send'/>" class="normalButton" onclick="disableSend()">
    <input type="button" name="cancel" value="<bean:message key='label.cancel'/>" class="normalButton" onclick="window.close();">
  </td>
</tr>
</table>
<html:hidden property="savedDraftID"/>
<html:hidden property="composeInHTML"/>
<html:hidden property="to"/>
</html:form>

<script language="Javascript">
	function populateAttachment(){
		if (document.composeMailForm.attachments != null)	{
			var lengthSelected = window.document.composeMailForm.attachments.options.length;
			for (var i=0;i<lengthSelected;i++)	{
				window.document.composeMailForm.attachments.options[i].selected=true;
			}
		}
  }

  function disableSend() {
	  populateAttachment();
		document.composeMailForm.send[0].disabled = true;
		document.composeMailForm.send[1].disabled = true;
		document.composeMailForm.submit();
  }

	<%-- Open the add attachments window --%>
  function attachmentLookup() {
		window.open('<%=request.getContextPath()%>/email/attachment_lookup.do', 'Attachments','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=715,height=200');
	}

  <%-- Called by the attachment window, this function
       removes all data from the attachments select box --%>
	function removeAttachmentsContent()	{
    var attachWidget = document.composeMailForm.attachments;
		var newIndex = attachWidget.length ;
		var k = 0;
		while(k < newIndex) {
      attachWidget.options[k] = null;
			k++;
		}
	}

	function removeFile()	{
		if (document.composeMailForm.attachments.selectedIndex < 0) {
			alert( "Please Select File!" );
			return;
		} else {
			var filename = document.composeMailForm.attachments[document.composeMailForm.attachments.selectedIndex].text ;
			document.composeMailForm.attachments.options[document.composeMailForm.attachments.selectedIndex]=null;
		}
	}

  <%-- Called by the attachment window, this function adds
       one attachment to the attachments select box --%>
  function updateAttachments(strAttach, strAttachID) {
		var option0 = new Option(strAttach, strAttachID);
    var attachWidget = document.composeMailForm.attachments;
		var newIndex = attachWidget.length;
		attachWidget.options[newIndex] = option0;
	}

	<%-- Saves the current form as a Draft, does not clear form --%>
	function saveDraft() {
	  populateAttachment();
    window.document.composeMailForm.action = "<%=request.getContextPath()%>/email/draft_save.do";
    window.document.composeMailForm.target = "_self";
    window.document.composeMailForm.submit();
	}

</script>