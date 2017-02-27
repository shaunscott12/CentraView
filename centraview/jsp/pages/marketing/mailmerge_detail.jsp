<%--
/**
 * $RCSfile: mailmerge_detail.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
 */
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/fckeditor/fckeditor.js"></script>
<script language="Javascript1.2">
<!--
  window.onload = function()
  {
    var oFCKeditor = new FCKeditor('templateData', '100%', '400', 'cvDefault', '');
    oFCKeditor.ReplaceTextarea();
  }


  function changeTemplate()
  {
    window.document.printtemplate.action = "<%=request.getContextPath()%>/marketing/mailmerge_detail.do";
    window.document.printtemplate.target = "_self";
    window.document.printtemplate.submit();
  }
  function attachmentLookup()
	{
    window.open('<%=request.getContextPath()%>/email/attachment_lookup.do', 'Attachments','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=715,height=200');
	}

	function removeAttachmentsContent()
	{
    var attachWidget = document.printtemplate.attachments;
		var newIndex = attachWidget.length ;
		var k = 0;
		while(k < newIndex)
		{
      attachWidget.options[k] = null;
			k++;
		}
	}

	function removeFile()
	{
		if (document.printtemplate.attachments.selectedIndex < 0){
				alert( "<bean:message key='label.alert.selectfile'/>!" );
				return;
		}
		else{
			var filename = document.printtemplate.attachments[document.printtemplate.attachments.selectedIndex].text ;
			document.printtemplate.attachments.options[document.printtemplate.attachments.selectedIndex]=null;
		}
	}

  function updateAttachments(strAttach, strAttachID)
	{
		var option0 = new Option(strAttach, strAttachID);
    var attachWidget = document.printtemplate.attachments;
		var newIndex = attachWidget.length;
		attachWidget.options[newIndex] = option0;
	}
  function populateAttachment(){
		if (document.printtemplate.attachments != null)	{
			var lengthSelected = window.document.printtemplate.attachments.options.length;
			for (var i=0;i<lengthSelected;i++)	{
				window.document.printtemplate.attachments.options[i].selected=true;
			}
		}
	}
//-->
</script>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr height="3">
    <td colspan="2" class="sectionHeader"><bean:message key="label.marketing.mailmergeselecttemp"/></td>
  </tr>
  <tr>
    <td colspan="2" class="labelCell" width="550">
     <bean:message key="label.marketing.selecttemplate"/>.
      <c:choose>
        <c:when test="${sessionScope.userobject.userType == 'ADMINISTRATOR'}">
          <bean:message key="label.marketing.templatepermanentchanges"/>
          <a class="plainLink" href="<html:rewrite page='/administration/template_list.do'/>"><bean:message key="label.marketing.adminmodulesettings"/></a>.
        </c:when>
        <c:otherwise>
          <bean:message key="label.marketing.changeorcreatetemplate"/>.
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</table>
<html:form action="/marketing/mailmerge_preview.do" onsubmit="populateAttachment()" styleClass="formTable">
  <table border="0" width="100%" class="formTable">
      <tr>
        <td class="labelCell" colspan="2">
        <bean:message key="label.marketing.template"/>:
          <html:select property="id" styleId="id" styleClass="inputBox" onchange="changeTemplate();">
            <html:optionsCollection property="templateList" value="value" label="label"/>
          </html:select>
        </td>
      </tr>
      <c:if test="${printtemplate.map.mergetype == 'EMAIL'}">
        <tr>
           <td colspan="2" align="left" class="labelCell">
              <table border="0" width="100%">
                <tr>
                  <td align="left" width="40%" class="labelCell">
                    <table  border="0" width="100%">
                      <tr>
                        <td align="left" class="labelCell">
                            <bean:message key="label.marketing.from"/>:
                            <html:select property="templatefrom" styleClass="inputBox">
                              <html:optionsCollection property="accountList" value="value" label="label"/>
                            </html:select>
                        </td>
                      </tr>
                      <tr>
                        <td align="left" class="labelCell">
                          <bean:message key="label.marketing.subject"/>: &nbsp;<html:text property="templatesubject" styleClass="inputBox" />
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td align="left" width="20%" >
                   <input type="button" name="attachButton" value="<bean:message key='label.marketing.attachfile'/>" class="normalButton" onclick="attachmentLookup()" />
                    <br>
                    <input type="button" name="removeButton" value="<bean:message key='label.marketing.removefile'/>" class="normalButton" onclick="removeFile()" />
                  </td>
                  <td align="left" width="40%" class="labelCell">
                      <html:select property="attachments" styleClass="inputBox" multiple="true" size="5" style="width:20em;">
                          <html:optionsCollection  property="attachmentList" value="strid" label="name" />
                      </html:select>
                  </td>
                </tr>
              </table>
           </td>
        </tr>
      </c:if>
      <tr>
        <td colspan="2">
          <html:textarea property="templateData" styleId="templateData" cols="82" rows="24" style="width:100%;"/>
        </td>
      </tr>
      <tr>
        <td align="left" colspan="2" class="sectionHeader">
          <html:button property="cancelButton" styleClass="normalButton" onclick="c_goTo('/marketing/mailmerge.do');">
            &laquo;&nbsp;<bean:message key="label.marketing.back"/>
          </html:button>
          <html:submit styleClass="normalButton">
            <bean:message key="label.marketing.next"/>&nbsp;&raquo;
          </html:submit>
        </td>
      </tr>
  </table>
</html:form>
