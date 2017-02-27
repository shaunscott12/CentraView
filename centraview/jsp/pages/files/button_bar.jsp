<%--
 * $RCSfile: button_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="com.centraview.file.FileConstantKeys" %>
<table border="0" cellpadding="3" width="100%">
  <tr>
    <td class="sectionHeader">
      <html:button property="saveandclose" styleClass="normalButton" onclick="return checkAddEditFile('close')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      &nbsp;
      <html:button property="saveandnew" styleClass="normalButton" onclick="return checkAddEditFile('new')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      &nbsp;
      <html:reset styleClass="normalButton">
        <bean:message key="label.resetfields"/>
      </html:reset>
      &nbsp;
      <html:cancel  styleClass="normalButton"  onclick="return winclose()">
        <bean:message key="label.cancel"/>
      </html:cancel>
    </td>
  </tr>
</table>

<script language="javascript">

<!-- close windowscreen -->
function winclose()	{
  window.close();
  return false;
}

<!-- check for add / edit -->
function checkAddEditFile(closeornew)	{
  var typeOfOperation = window.document.forms[0].TYPEOFOPERATION.value;
  var typeOfFile = window.document.forms[0].TYPEOFFILE.value;

  if (typeOfOperation == "<%=FileConstantKeys.ADD%>" &&
      typeOfFile == "<%=FileConstantKeys.FOLDER%>")	{
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_new_folder.do?closeornew="+closeornew+"&Button=yes";
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  } else 	if (typeOfOperation == "<%=FileConstantKeys.EDIT%>" &&
              typeOfFile == "<%=FileConstantKeys.FOLDER%>")	{
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_edit_folder.do?closeornew="+closeornew;
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  } else 	if (typeOfOperation == "<%=FileConstantKeys.ADD%>" &&
              typeOfFile == "<%=FileConstantKeys.FILE%>")	{
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_new_file.do?closeornew="+closeornew+"&Button=yes";
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  } else 	if (typeOfOperation == "<%=FileConstantKeys.DUPLICATE%>" &&
              typeOfFile == "<%=FileConstantKeys.FOLDER%>")	{
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_duplicate_folder.do?closeornew="+closeornew;
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  } else 	if (typeOfOperation == "<%=FileConstantKeys.DUPLICATE%>" &&
              typeOfFile == "<%=FileConstantKeys.FILE%>")	{
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_duplicate_file.do?closeornew="+closeornew;
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  } else {
    alert("<bean:message key='label.alert.invaliderror'/>");
  }
}

<%-- launch permission detail --%>
function launchPermission()	{
  var typeOfFile = window.document.forms[0].TYPEOFFILE.value;
  var typeOfOperation = window.document.forms[0].TYPEOFOPERATION.value;
  // select all items under permission list boxes
  if (typeOfFile == "<%=FileConstantKeys.FOLDER%>" &&
      typeOfOperation == "<%=FileConstantKeys.ADD%>")
  {
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_new_folder.do?Flag=CVFolder&Button=&closeornew=";
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  }
  if (typeOfFile == "<%=FileConstantKeys.FILE%>"
      && typeOfOperation == "<%=FileConstantKeys.ADD%>")
  {
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_new_file.do?Flag=File&Button=&closeornew=";
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  }
  if (typeOfOperation == "<%=FileConstantKeys.EDIT%>" &&
      typeOfFile == "<%=FileConstantKeys.FOLDER%>")
  {
    window.document.forms[0].action="<%=request.getContextPath()%>/files/save_edit_folder.do?Flag=CVFolder&Button=&closeornew=";
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return false;
  }
}
</script>
