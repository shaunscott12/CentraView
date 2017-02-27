<%--
 * $RCSfile: new_custom_field.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "com.centraview.common.*,org.apache.struts.action.DynaActionForm"%>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%@ page import="com.centraview.common.BrowserDetector" %>
<%
  DynaActionForm cf= (DynaActionForm)request.getAttribute("Dyna");
  Vector vec = new Vector();
  if (request.getAttribute("valuelist") != null) {
    vec = (Vector)request.getAttribute("valuelist");
  }

  String module = request.getParameter("module").toString();
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  String tableName = null;
  int tableId = 0;
  if (module.equals("Contacts")) {
    tableName = "entity";
  } else if (module.equals("Sales")) {
    tableName = "opportunity";
  } else if (module.equals("Marketing")) {
    tableName = "marketinglist";
  } else if (module.equals("Projects")) {
    tableName = "project";
  } else if (module.equals("Support")) {
    tableName = "ticket";
  } else if (module.equals("Accounting")) {
      tableName = "order";
  } else if (module.equals("HR")) {
    tableName = "expenseform";
  }
%>
<script language="javascript">
  var indexCount = 0;

  function addValue() {
    var indexCount = document.getElementById('valuelist').length;
    var newValue;
    var valuelist;
    newValue = document.getElementById('newValue').value;
    var myNewOption = new Option(newValue, newValue);
    document.getElementById('valuelist').options[indexCount] = myNewOption;
    indexCount++;
    document.getElementById('newValue').value = '';
    document.getElementById('newValue').focus();
  }

  function showLayer()
  {
    var multipleLayer = document.getElementById('multipleLayer');
    <%
      // support crippled browsers that don't support standards
      BrowserDetector detector = new BrowserDetector();
      detector.setRequest(request);
      if (detector.isIE()) {
        %>multipleLayer.style.display = "block";<%
      } else {
        // "table-row" is the correct value for the "display" attribute of a <TR>
        %>multipleLayer.style.display = "table-row";<%
      }
    %>
  }

  function hideLayer()
  {
    var multipleLayer = document.getElementById('multipleLayer');
    multipleLayer.style.display = "none";
    // TODO: remove values from the multiple value inputs
  }

  function gotocancel()
  {
    c_goTo('/administration/list_custom_fields.do?module=<%=module%>');
  }

  function gotosave(param)
  {
    var table = document.getElementById('table').value;
    if (document.getElementById('valuelist').length > 0) {
      for (i = 0; i < document.getElementById('valuelist').length; i++) {
        document.getElementById('valuelist').options[i].selected = "true";
      }
    }
    document.customfield.action = "<html:rewrite page="/administration/save_new_custom_field.do"/>?buttonpress=" + param;
    document.customfield.submit();
    return false;
  }

  function changeAsPer()
  {
    var recordIndex = document.getElementById('recordtype').selectedIndex;
    document.getElementById('recordtype').options[recordIndex].value = document.getElementById('recordtype').options[recordIndex].text;
    var recordtype = document.getElementById('recordtype').options[recordIndex].value.toLowerCase();
    document.getElementById('table').value = recordtype;
  }
</script>
<html:form action="/administration/new_custom_field.do">
<input type="hidden" name="module" id="module" value="<%=module%>"/>
<input type="hidden" name="table" id="table" value="<%=tableName%>"/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.newcustomfield"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.fieldname"/>:</td>
    <td class="contentCell"><html:text property="fieldname" styleId="fieldname" styleClass="inputBox" size="45"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.module"/>:</td>
    <td class="contentCell">
      <select class="inputBox" name="select" id="select">
        <option selected><%=module%></option>
      </select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.recordtype"/>:</td>
    <td class="contentCell">
      <%
        Vector vrecord =  new Vector();
        if (module.equals("Contacts")) { vrecord = (Vector)gml.getRecordType("Contacts"); }
        if (module.equals("Activities")) { vrecord = (Vector)gml.getRecordType("Activities"); }
        if (module.equals("Notes")) { vrecord = (Vector)gml.getRecordType("Notes"); }
        if (module.equals("File")) { vrecord = (Vector)gml.getRecordType("File"); }
        if (module.equals("Sales")) { vrecord = (Vector)gml.getRecordType("Sales"); }
        if (module.equals("Marketing")) { vrecord = (Vector)gml.getRecordType("Marketing"); }
        if (module.equals("Support")) { vrecord = (Vector)gml.getRecordType("Support"); }
        if (module.equals("Projects")) { vrecord = (Vector)gml.getRecordType("Projects"); }
        if (module.equals("Accounting")) { vrecord = (Vector)gml.getRecordType("Accounting"); }
        if (module.equals("HR")) { vrecord = (Vector)gml.getRecordType("HumanResources"); }
        pageContext.setAttribute("vrecord", vrecord, PageContext.PAGE_SCOPE);
      %>
      <select name="recordtype" id="recordtype" class="inputBox" onchange="changeAsPer()">
        <%
          for (int i = 0; i < vrecord.size(); i++) {
            String temp = vrecord.elementAt(i).toString();
            %><option value=""><%=temp%></option><%
          }
        %>
      </select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.fieldtype"/>:</td>
    <td class="contentCell">
      <html:radio property="layerSwitch" styleId="layerSwitch" onclick="hideLayer();" value="SCALAR" /> <bean:message key='label.value.single'/>
      <html:radio property="layerSwitch" styleId="layerSwitch" onclick="showLayer();" value="MULTIPLE" />   <bean:message key='label.value.multiple'/>
    </td>
  </tr>
  <tr id="multipleLayer" name="multipleLayer" style="display:none;">
    <td class="labelCell"><bean:message key="label.administration.values"/>:</td>
    <td class="contentCell">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td class="contentCell">
            <html:text property="newValue" styleId="newValue" styleClass="inputBox" size="25"/>
            <app:cvbutton property="addvalue" styleId="addvalue" styleClass="normalButton" onclick="addValue();">
              <bean:message key="label.administration.addvalue"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <% if (request.getAttribute("valuelist") != null){ %>
            <script>showLayer(multipleLayer);</script>
            <select name="valuelist" id="valuelist" multiple="true" size="8" class="inputBox" style="width:20em">
              <%
                if (vec != null) {
                  for (int i = 0; i < vec.size(); i++) {
                    String name = (String) vec.get(i);
                    %><option value="<%=name%>"><%=name%></option><%
                  }
                }
              %>
            </select>
            <% } else { %>
            <html:select multiple ="true" styleClass="inputBox" property="valuelist" styleId="valuelist" size="8" style="WIDTH: 20em">
            </html:select>
            <% } %>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="saveandclose" styleId="saveandclose" styleClass="normalButton" onclick="gotosave('saveclose');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="saveandnew" styleId="saveandnew" styleClass="normalButton" onclick="gotosave('savenew')">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>