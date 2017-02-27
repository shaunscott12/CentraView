<%--
 * $RCSfile: support_inbox.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "java.util.Vector"%>
<%@ page import = "java.util.ArrayList"%>
<%@ page import = "java.util.Collection"%>
<%@ page import = "com.centraview.common.ViewForm"%>
<%@ page import = "com.centraview.common.DDNameValue"%>
<%@ page import = "com.centraview.common.* ,java.util.*"%>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.administration.common.AdministrationConstantKeys" %>
<script language="javascript">
  function adddel(typeofselect)
  {
    var n = document.getElementById('emailaccountlist').options.length;
    var m = document.getElementById('viewsend').options.length;
    if (document.getElementById('emailaccountlist').selectedIndex == -1) {
      alert("<bean:message key='label.alert.selectaddtag'/>");
    } else {
      i = 0;
      while (i < n) {
        if (document.getElementById('emailaccountlist').options[i].selected) {
          document.getElementById('viewsend').options[m] = new Option(document.getElementById('emailaccountlist').options[i].text, document.getElementById('emailaccountlist').options[i].value, "false", "false");
          document.getElementById('emailaccountlist').options[i] = null;
          m = m + 1;
          n = n - 1;
          i = 0;
        } else {
          i++;
        }
      }
    }
  }

  function removedel(typeofselect)
  {
    var m = document.getElementById('emailaccountlist').options.length;
    var n = document.getElementById('viewsend').options.length;

    if (document.getElementById('viewsend').selectedIndex == -1) {
      alert("<bean:message key='label.alert.selectremovetag'/>");
    } else {
      i = 0;
      while (i < n) {
        if (document.getElementById('viewsend').options[i].selected) {
          document.getElementById('emailaccountlist').options[m] = new Option(document.getElementById('viewsend').options[i].text, document.getElementById('viewsend').options[i].value, "false", "false");
          m = m + 1;
        }
        i++;
      }

      i = 0;
      while (i < n) {
        if (document.getElementById('viewsend').options[i].selected) {
          document.getElementById('viewsend').options[i] = null;
          n = n - 1;
        } else {
          i++;
        }
      }
    }
  }

  function delegationsearch()
  {
    var searchStr = document.getElementById('search').value;
    document.forms.masterdataform.action = "<html:rewrite page="/administration/support_inbox.do"/>?search=" + searchStr;
    document.forms.masterdataform.submit();
  }

  function savedelegation()
  {
    var viewsenddel = document.getElementById('viewsend').options.length;
    for (var i = 0; i < viewsenddel; i++) {
      document.getElementById('viewsend').options[i].selected = true;
    }

    document.forms.masterdataform.action = "<html:rewrite page="/administration/save_support_inbox.do"/>";
    document.forms.masterdataform.submit();
  }

  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.getElementById('employee').readonly = false;
    document.getElementById('employeeID').value = id;
    document.getElementById('employee').value = firstName+" "+middleName+" "+lastName;
    document.getElementById('employee').readonly = true;
  }
</script>
<%
  String employeeID = null;
  Vector colviewsend = null;
  Vector emailAccountList = null;
  String defaultMessageBody = "";

  if (request.getAttribute("colsend") != null)
  {
    colviewsend = (Vector)request.getAttribute("colsend");
  }
  pageContext.setAttribute("colviewsend", colviewsend, PageContext.PAGE_SCOPE);

  if (request.getAttribute("emailAccountList") != null)
  {
    emailAccountList = (Vector)request.getAttribute("emailAccountList");
  }
  pageContext.setAttribute("emailAccountList", emailAccountList, PageContext.PAGE_SCOPE);

  String typeofmodule = request.getAttribute("typeofmodule").toString();
  String typeofsubmodule = request.getAttribute("typeofsubmodule").toString();
  String settingfor = request.getAttribute("settingfor").toString();

  String employee  = (String)request.getAttribute("employee");
  employee = (employee != null) ? employee : "";

  if (request.getAttribute("employeeID") != null)
  {
    employeeID = request.getAttribute("employeeID").toString();
  }

%>
<html:form action="/administration/support_inbox.do">
<input type="hidden" name="typeofmodule" value="<%=typeofmodule%>">
<input type="hidden" name="sourcefor" value="<%=typeofsubmodule%>">
<input type="hidden" name="setting" value="<%=settingfor%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.supportmodulesettingssupportinbox"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <bean:message key="label.administration.supportinboxexplication"/>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell" colspan="3">
      <html:text property="search" styleId="search" styleClass="inputBox" size="31"/>
      <input type="button" name="searchButton" id="searchButton" class="normalButton" value="<bean:message key='label.value.search'/>" onclick="delegationsearch();" />
    </td>
  </tr>
  <tr>
    <td class="contentCell">
      <span class="boldText"><bean:message key="label.administration.definedemailaccounts"/></span><br/>
      <html:select property="emailaccountlist" styleId="emailaccountlist" styleClass="inputBox" multiple="true" size="12" style="width:18em;">
        <html:options collection="emailAccountList" property="id" labelProperty="name"/>
      </html:select>
    </td>
    <td class="contentCell">
      <app:cvbutton property="viewadd" styleId="viewadd" styleClass="normalButton" onclick="adddel('send');" style="width:6em;">
        <bean:message key="label.add"/> &raquo;
      </app:cvbutton>
      <br/><br/>
      <app:cvbutton property="viewremvove" styleId="viewremvove" styleClass="normalButton" onclick="removedel('viewsend');" style="width:6em;">
        &laquo; <bean:message key="label.remove"/>
      </app:cvbutton>
    </td>
    <td class="contentCell">
      <span class="boldText"><bean:message key="label.administration.selectedsupportemailaccounts"/></span><br/>
      <html:select property="viewsend" styleId="viewsend" styleClass="inputBox" multiple="true" size="12" style="width:18em;">
        <html:options collection="colviewsend" property="strid" labelProperty="name"/>
      </html:select>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.checkemailevery"/>:</td>
    <td class="contentCell">
      <html:select property="emailCheckInterval" styleId="emailCheckInterval" styleClass="inputBox">
        <html:option value="0"> <bean:message key="label.administration.never"/> </html:option>
        <html:option value="5"> &nbsp;5 Min </html:option>
        <html:option value="10"> 10 Min </html:option>
        <html:option value="15"> 15 Min </html:option>
        <html:option value="20"> 20 Min </html:option>
        <html:option value="25"> 25 Min </html:option>
        <html:option value="30"> 30 Min </html:option>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.defaultowneroftickets"/>:</td>
    <td class="contentCell">
      <input type="hidden" name="employeeID" id="employeeID" value="<%=employeeID%>"/>
      <input type="text" name="employee" id="employee" value="<%=employee%>" class="inputBox" readonly="true" size="30"/>
      <input type="button" name="lookup" id="lookup" value="<bean:message key='label.value.lookup'/>" class="normalButton" onclick="c_lookup('Employee');" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.autoresponsenewtickets"/>:</td>
    <td class="contentCell">
      <a href="<html:rewrite page="/administration/view_email_template.do"/>?templateID=<%=AdministrationConstantKeys.EMAIL_TEMPLATE_SUPPORTTICKET%>" class="plainLink"><bean:message key="label.value.edit"/></a>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.autoresponseopentickets"/>A:</td>
    <td class="contentCell">
      <a href="<html:rewrite page="/administration/view_email_template.do"/>?templateID=<%=AdministrationConstantKeys.EMAIL_TEMPLATE_SUPPORTTHREAD%>" class="plainLink"><bean:message key="label.value.edit"/></a>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.autoresponseerror"/>:</td>
    <td class="contentCell">
      <a href="<html:rewrite page="/administration/view_email_template.do"/>?templateID=<%=AdministrationConstantKeys.EMAIL_TEMPLATE_SUPPORTERROR%>" class="plainLink"><bean:message key="label.value.edit"/></a>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input type="button" name="save" value="<bean:message key='label.value.save'/>" class="normalButton" onclick="savedelegation();" />
    </td>
  </tr>
</table>
</html:form>