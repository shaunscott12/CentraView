<%--
 * $RCSfile: listmanager_import_1.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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

<script language="javascript">
  function validateFields()
  {
    var fieldSep=window.document.forms[0].fieldseprator.value;
    var tab=window.document.forms[0].tab.checked;
    var lineSep=window.document.forms[0].lineseprator.value;
    var linereturn=window.document.forms[0].line.checked;
    var yesFlag=window.document.forms[0].headerrow[0].checked;
    var noFlag=window.document.forms[0].headerrow[1].checked;
    var file=window.document.forms[0].theFile.value;

    if(tab && !(fieldSep == "")){
      alert("<bean:message key='label.alert.fieldseparatorandtabbenotbothselected'/>");
      window.document.forms[0].tab.checked = false;
    }

    if(!yesFlag && !noFlag){
      alert("<bean:message key='label.alert.headerrowselected'/>");
    }

    if(linereturn  &&  !(lineSep == "")){
      alert("<bean:message key='label.alert.lineseparatorandreturnbenotbothselected'/>");
      window.document.forms[0].line.checked = false;
    }

    document.forms[0].next.disabled = true;
    if (fieldSep == "" && (!tab))
      alert("<bean:message key='label.alert.fieldseparatorandtabbenotblank'/>");
    else if (lineSep == "" && (!linereturn))
      alert("<bean:message key='label.alert.lineseparatorandreturnbenotblank'/>");
    else if(file == "")
      alert("<bean:message key='label.alert.specifyfilename'/>");
    else
      window.document.forms[0].submit();
  }
</script>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
<html:form action="/marketing/import_criteria.do" enctype="multipart/form-data"  styleClass="formTable">
  <tr>
    <td valign="top">
      <table width="70%" cellspacing="0" cellpadding="3" >
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.fieldseparator"/>:</td>
          <td align="left" class="contentCell">
            <html:text property="fieldseprator" styleClass="inputBox" maxlength="2"/>&nbsp;&nbsp;&nbsp;
            <html:checkbox property="tab" value="\\t"/>&nbsp;<bean:message key="label.marketing.tab"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.lineseparator"/>:</td>
          <td align="left" class="contentCell">
            <html:text property="lineseprator" styleClass="inputBox" maxlength="2"/>&nbsp;&nbsp;&nbsp;
            <html:checkbox property="line" value="\\n"/>&nbsp;<bean:message key="label.marketing.linereturn"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.headerrow"/>:</td>
          <td align="left" class="contentCell">
            <html:radio property="headerrow" value="Yes"/>
              <bean:message key="label.marketing.yes"/>
            <html:radio property="headerrow" value="No"/>
              <bean:message key="label.marketing.no"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.uploadfile"/>:</td>
          <td align="left" class="contentCell">
            <html:file property="theFile" styleClass="inputBox" />
          </td>
        </tr>
        <tr>
          <td colspan="2"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="4"></td>
        </tr>
       </table>
    </td>
    <td valign="top">
      <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="sectionHeader"><bean:message key="label.marketing.instructions"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            &nbsp;
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.marketing.importdata"/></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="sectionHeader" colspan="2" align="right">
       <input name="back" type="button" class="normalButton" value="&laquo; <bean:message key='label.marketing.back'/>" onClick="c_goTo('/marketing/new_listmanager.do')" />
        &nbsp;
       <input name="next" type="button" class="normalButton" value="<bean:message key='label.marketing.step2'/> &raquo;" onClick="return validateFields()">
       <html:img page="/images/popup_vert_divider.gif" height="17" width="1"/>
       <input name="cancel" type="button" class="normalButton" value="<bean:message key='label.marketing.cancel'/>" onClick="window.close();">
     </td>
  </tr>
</html:form>
</table>
</body>
</html>