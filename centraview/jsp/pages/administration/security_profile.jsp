<%--
 * $RCSfile: security_profile.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "com.centraview.common.*" %>
<%@ page import = "org.apache.struts.action.DynaActionForm"%>
<%
  String action = "";
  String rowId = "";

  if (request.getParameter("typeofoperation") != null) {
    action = request.getParameter("typeofoperation").toString();
  }
  if (request.getParameter("rowId") != null) {
    rowId = request.getParameter("rowId").toString();
  }
%>
<script language="javascript">

  function showBranch(branch,currbranchstate,nextbranchstate)
  {
    var objBranch = document.getElementById(branch).style;
    var objCurrBranchState = document.getElementById(currbranchstate).style;
    var objNextBranchState = document.getElementById(nextbranchstate).style;
    if (objBranch.display == "block" || objBranch.display == "") {
      objBranch.display = "none";
      objCurrBranchState.display = "none";
      objNextBranchState.display = "block";
    } else {
      objBranch.display = "block";
      objCurrBranchState.display = "block";
      objNextBranchState.display = "none";
    }
  }

  function SubmitProfileForm(param)
  {
    document.forms.securityprofile.action = "<html:rewrite page="/administration/update_profile.do?"/>action=" + param;
    document.forms.securityprofile.submit();
  }

  function checkSubModules(clickedChecBox)
  {

    modules = document.forms.securityprofile.moduleright;
    subModules = modulesTree[clickedChecBox.value]

    for (i = 0; i < subModules.length; i++) {
      for (j = 0; j < modules.length; j++) {
        if (modules[j].value == subModules[i]) {
          modules[j].checked = clickedChecBox.checked;
        }
      }
    }
  }
</script>
<html:form action="/administration/update_profile.do">
<input type="hidden" name="actiontype" id="actiontype" value="<%=action%>" />
<html:hidden property="profileid" styleId="profileid" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.profilesettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="5" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.profilename"/>:</td>
    <td class="contentCell">
      <html:text property="profilename" styleId="profilename" styleClass="inputBox"/>
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="SubmitProfileForm('save');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <input name="button22222" type="button" class="normalButton" value="<bean:message key='label.value.saveandclose'/>" onclick="SubmitProfileForm('saveandclose')" />
      <input name="button22232" type="button" class="normalButton" value="<bean:message key='label.value.saveandnew'/>" onclick="SubmitProfileForm('saveandnew')" />
      <input name="button2242" type="button" class="normalButton" value="<bean:message key='label.value.cancel'/>" onclick="cancel('/centraview/administration/security_profile_list.do')" />
    </td>
  </tr>
</table>
<table border="0" cellpadding="5" cellspacing="0">
  <tr>
    <td>
      <% String html = (String) request.getAttribute("dynamicHTML"); %>
      <%=html%>
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="SubmitProfileForm('save');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <input name="button22222" type="button" class="normalButton" value="<bean:message key='label.value.saveandclose'/>" onclick="SubmitProfileForm('saveandclose')" />
      <input name="button22232" type="button" class="normalButton" value="<bean:message key='label.value.saveandnew'/>" onclick="SubmitProfileForm('saveandnew')" />
      <input name="button2242" type="button" class="normalButton" value="Cancel" onclick="<bean:message key='label.value.cancel'/>('/centraview/administration/security_profile_list.do')" />
    </td>
  </tr>
</table>
</html:form>