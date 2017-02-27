<%--
 * $RCSfile: field_settings.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.projects.helper.*" %>
<%@ page import="com.centraview.projects.common.ProjectsHelperList" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  //AccountingTerms
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);

  String setting = request.getAttribute("settingfor").toString();
  String submodule = request.getAttribute("typeofsubmodule").toString();

  String oppor="";
  String acterms="";

  if ((submodule.equals("Sales")) && (setting.equals("Source"))) {
    oppor = "Opportunity";
  }
  if ((submodule.equals("Sales")) && (setting.equals("Type")) || (setting.equals("Stage"))) {
    oppor = "Opportunity and Proposal";
  }
  if ((submodule.equals("Sales")) && (setting.equals("Term"))) {
    oppor = "Proposal";
  }
  if ((submodule.equals("Accounting")) && (setting.equals("InvoiceStatus"))) {
    oppor = "Order and Invoice";
  }
  if ((submodule.equals("Accounting")) && (setting.equals("OrderStatus"))) {
    oppor = "Order";
  }
  if ((submodule.equals("Accounting")) && (setting.equals("ACTerms"))) {
    oppor = "Invoice";
  }
  ProjectsHelperList helperList = ProjectsHelperList.getProjectsHelperList(dataSource);
%>
<script language="javascript">
  var indexCount = 0;
  function addValue()
  {
    var indexCount = document.getElementById('valueList').length;
    var newValueWidget = document.getElementById('newvalue').value;
    var myNewOption = new Option(newValueWidget, "-1");
    document.getElementById('valueList').options[indexCount] = myNewOption;
    indexCount++;
    document.getElementById('newvalue').value = "";
  }

  function gotocancel()
  {
    document.getElementById('fieldSettingsForm').action = "<html:rewrite page="/administration/view_module_settings.do" />?typeofsubmodule=<%=submodule%>";
    document.getElementById('fieldSettingsForm').submit();
    return true;
  }

  function removeValue()
  {
    for (i = 0; i < document.getElementById('valueList').options.length; i++) {
      if (document.getElementById('valueList').options[i].selected == true) {
        document.getElementById('valueList').options[i] = null;
      }
    }
    document.getElementById('newvalue').value = "";
    return true;
  }

  function editValue()
  {
    if (document.getElementById('newvalue').value != "") {
      for (i = 0; i < document.getElementById('valueList').options.length; i++) {
        if (document.getElementById('valueList').options[i].selected == true) {
          document.getElementById('valueList').options[i].text = document.getElementById('newvalue').value;
          i = document.getElementById('valueList').options.length;
          break;
        }
      }
      document.getElementById('newvalue').value = "";
    }
    return true;
  }

  function setText()
  {
    for (i = 0; i < document.getElementById('valueList').options.length; i++) {
      if (document.getElementById('valueList').options[i].selected == true) {
        document.getElementById('newvalue').value = document.getElementById('valueList').options[i].text;
        i = document.getElementById('valueList').options.length;
        break;
      }
    }
    return true;
  }

  function gotosave(param)
  {
    if (document.getElementById('valueList').length > 0) {
      for (i = 0; i < document.getElementById('valueList').length; i++) {
        var idOption = new Option(document.getElementById('valueList').options[i].value,  document.getElementById('valueList').options[i].value);
        document.getElementById('valueIds').options[document.getElementById('valueIds').length] = idOption;
        document.getElementById('valueIds').options[i].selected = "true";
        document.getElementById('valueList').options[i].selected = "true";
        document.getElementById('valueList').options[i].value = document.getElementById('valueList').options[i].text;
      }
    }
    document.getElementById('fieldSettingsForm').action = "<html:rewrite page="/administration/save_field_settings.do"/>?buttonpress="+param;
    document.getElementById('fieldSettingsForm').submit();
    return false;
  }
</script>
<html:form action="/administration/save_field_settings.do" styleId="fieldSettingsForm">
<input type="hidden" name="setting" id="setting" value="<%=setting%>">
<input type="hidden" name="submodule" id="submodule" value="<%=submodule%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader">
      <%=submodule%><bean:message key="label.administration.modulesettings"/>: <% if (! (setting.equals("ACTerms"))){ %><%=setting%>'s<% } else { %> Terms <% } %>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell" colspan="2">
      <% if (! setting.equals("Resource")) { %>
       <bean:message key="label.administration.createeditmodifyterms"/><% if (! setting.equals("ACTerms")){ %><%=setting%><% }
else { %> <bean:message key="label.administration.terms"/> <% } %>&quot;
        <bean:message key="label.administration.fieldon"/><% if (! submodule.equals("Sales")) { %> <%=submodule%>
<% } %><%=oppor%> <bean:message key="label.administration.records"/>.
       <% } else { %>
       <bean:message key="label.administration.createeditmodifyressources"/>
       <% } %>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.newvalue"/>:</td>
    <td class="contentCell"><html:text property="newvalue" styleId="newvalue" styleClass="inputBox" size="30"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.existingvalues"/>:</td>
    <td class="contentCell">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td>
            <%
              if (! setting.equals("Status")) {
                Vector sourceVec = new Vector();

                if (setting.equals("Literature")) {
                  sourceVec = (Vector)request.getAttribute("literaturelist");
                }

                if (! setting.equals("Literature")) {
                  if (gml.get("AllSale" + setting) != null) {
                    sourceVec = (Vector)gml.get("AllSale"+setting);
                  }

                  if (setting.equals("Resource")) {
                    sourceVec = (Vector)gml.getAllResources();
                  }

                  if (setting.equals("Source")) {
                    sourceVec = (Vector) gml.get("AllSource");
                  }

                  if (setting.equalsIgnoreCase("ACTerms")) {
                    sourceVec = (Vector)gml.get("AccountingTerms");
                  }

                  if (setting.equals("InvoiceStatus") || setting.equals("OrderStatus")) {
                    sourceVec = (Vector)gml.get("AccountingStatus");
                  }

                  if (setting.equals("Locations")) {
                    sourceVec = (Vector)gml.get("AccountingLocations");
                  }

                  for (int i = 0; i < sourceVec.size(); i++) {
                    DDNameValue dn =(DDNameValue)sourceVec.elementAt(i);
                    if (dn.getName().startsWith("-")) {
                      sourceVec.remove(dn);
                    }
                  }
                }
                pageContext.setAttribute("sourceVec", sourceVec, PageContext.PAGE_SCOPE);

                %>
                <html:select property="valueList" styleId="valueList" multiple="true" styleClass="inputBox" style="width:17em;" size="8" onchange="setText();">
                  <html:options collection="sourceVec" property="id" labelProperty="name"/>
                </html:select>
                <%-- hidden field, DO NOT DELETE!!! --%>
                <select name="valueIds" id="valueIds" multiple="multiple" style="display:none"></select>
                <%
              } else {
                Vector stateVec = (helperList.get("ProjectStatus") != null) ? (Vector)helperList.get("ProjectStatus"): new Vector();
                pageContext.setAttribute("stateVec", stateVec, PageContext.PAGE_SCOPE);
                %>
                <html:select property="valueList" styleId="valueList" multiple="true" styleClass="inputBox" style="width:17em;" size="8" onchange="setText();">
                  <html:options collection="stateVec" property="statusID" labelProperty="title"/>
                </html:select>
                <%-- hidden field, DO NOT DELETE!!! --%>
                <select name="valueIds" id="valueIds" multiple="multiple" style="display:none"></select>
                <%
              }
            %>
          </td>
          <td>
            <input name="newValueButton" id="newValueButton" type="button" class="normalButton" value="<bean:message key='label.value.addvalue'/>" onclick="addValue();" /><br/><br/>
            <input name="newValueButton4" id="newValueButton4" type="button" class="normalButton" value="<bean:message key='label.value.editvalue'/>" onclick="editValue();" /><br/><br/>
            <input name="newValueButton2" id="newValueButton2" type="button" class="normalButton" value="<bean:message key='label.value.remove'/>" onclick="removeValue();" />
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="save" id="save" type="button" class="normalButton" value="<bean:message key='label.value.save'/>" onClick="gotosave('save')"/>
      <input name="savaandclose" id="savaandclose" type="button" class="normalButton" value="<bean:message key='label.value.saveandclose'/>" onClick="gotosave('saveandclose')"/>
      <input name="cancel" id="cancel" type="button" class="normalButton" value="<bean:message key='label.value.cancel'/>" onClick="gotocancel();"/>
    </td>
  </tr>
</table>
</html:form>