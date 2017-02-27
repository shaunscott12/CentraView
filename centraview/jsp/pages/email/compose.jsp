<%--
 * $RCSfile: compose.jsp,v $    $Revision: 1.5 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<html:form action="/email/send" onsubmit="populateAttachment()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
	<td class="popupTextBox">
		<html:messages id="msg" message="true">
			<b><c:out value="${msg}"/></b>
	</html:messages>
	</td>
</tr>
<tr>
  <td class="headerBG">
    <input type="button" name="send" value="<bean:message key='label.value.send'/>" class="normalButton" onclick="disableSend()">
    <input type="button" name="savedraft" value="<bean:message key='label.value.savedraft'/>" class="normalButton" onclick="saveDraft()">
	  <input type="button" name="savetemplate" value="<bean:message key="label.save"/> <bean:message key="label.email.template"/>" class="normalButton" onclick="saveTemplate()">
    <input type="button" name="cancel" value="<bean:message key="label.cancel"/>" class="normalButton" onclick="window.close();">
    <html:select property="loadTemplate" styleClass="inputBox" onchange="templateLoad(this.value);">
    	<option value="-1"><bean:message key='label.value.loadtemplate'/></option>
    	<html:optionsCollection property="templateList" value="messageID" label="subject"/>
    </html:select>
  </td>
</tr>
</table>
<table border="0" cellspacing="1" cellpadding="3" class="mediumTableBorders" width="100%">
  <tr>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="2" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCellBold" align="right"><bean:message key="label.email.from"/> </td>
          <td class="contentCell">
            <html:select property="accountID" styleClass="inputBox">
	            <html:optionsCollection  property="accountList" value="accountID" label="accountName" />
  	        </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell" align="right"><input type="button" class="normalButton" name="entitylookup" value="<bean:message key='label.value.to'/>" onclick="lookupAddress();"/></td>
          <td class="contentCell"><html:text property="to" styleClass="inputBox" size="45"/></td>
        </tr>
        <tr>
          <td class="labelCell" align="right"><input type="button" class="normalButton" name="entitylookup" value="Cc" onclick="lookupAddress();"/></td>
          <td class="contentCell"><html:text property="cc" styleClass="inputBox" size="45"/></td>
        </tr>
        <tr>
          <td class="labelCell" align="right"><input type="button" class="normalButton" name="entitylookup" value="Bcc" onclick="lookupAddress();"/></td>
          <td class="contentCell"><html:text property="bcc" styleClass="inputBox" size="45"/></td>
        </tr>
        <tr>
          <td class="labelCellBold" align="right"><bean:message key='label.value.subject'/>:</td>
          <td class="contentCell"><html:text property="subject" styleClass="inputBox" size="45" /> </td>
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
            <input type="button" class="normalButton" name="entitylookup" value="<bean:message key='label.value.attachfile'/>" onclick="attachmentLookup()"/>
            <input type="button" class="normalButton" name="entitylookup" value="<bean:message key='label.value.removefile'/>" onclick="removeFile()"/>
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
    <input type="button" name="savedraft" value="<bean:message key='label.value.savedraft'/>" class="normalButton" onclick="saveDraft()">
	  <input type="button" name="savetemplate" value="<bean:message key="label.save"/> <bean:message key="label.email.template"/>" class="normalButton" onclick="saveTemplate()">
    <input type="button" name="cancel" value="<bean:message key='label.value.cancel'/>" class="normalButton" onclick="window.close();">
    <html:select property="loadTemplate" styleClass="inputBox" onchange="templateLoad(this.value);">
    	<option value="-1"><bean:message key='label.value.loadtemplate'/></option>
    	<html:optionsCollection property="templateList" value="messageID" label="subject"/>
    </html:select>
  </td>
</tr>
</table>
<html:hidden property="templateID"/>
<html:hidden property="savedDraftID"/>
<html:hidden property="composeInHTML"/>
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

	<%-- Open the email address lookup window --%>
  function lookupAddress() {
    c_openWindow('/email/address_lookup.do', '','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=500,height=550')
	}

	<%-- Add the addresses selected in the address lookup
       window to the To:, Cc:, and Bcc: fields. --%>
  function setData(dataTo, dataCc, dataBcc) {
    appendData(document.forms.composeMailForm.to, dataTo);
    appendData(document.forms.composeMailForm.cc, dataCc);
    appendData(document.forms.composeMailForm.bcc, dataBcc);
	}

	<%-- Append the data from "data" to the end of the
       form input specified by "formInput" --%>
  function appendData(formInput, data) {
    var oldData = formInput.value;
    var newData = "";
    if (trim(oldData) == "") {
      newData = data;
    } else {
      if (trim(data) != "") {
        newData = oldData + ', ' + data;
      } else {
        newData = oldData;
      }
    }
    formInput.value = newData;
  }

  <%-- Trim whitespace from both ends of "passedVal"
       and return the trimmed string --%>
  function trim(passedVal) {
		passedVal = passedVal.replace(/\s*$/, "");
		passedVal = passedVal.replace(/^\s*/, "")
		return(passedVal);
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

  <%-- Saves the current form as a Template, does not clear form --%>
  function saveTemplate() {
	  populateAttachment();
    window.document.composeMailForm.action = "<html:rewrite page="/email/template_save.do" />";
    window.document.composeMailForm.target = "_self";
    window.document.composeMailForm.submit();
  }

  <%-- Loads the information for a given template into the form --%>
  function templateLoad(templateID) {
    if (templateID > 0) {
     	window.open('<html:rewrite page="/email/view_template.do"/>?messageID=' + templateID, '','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=750,height=585')
    }
  }
</script>
