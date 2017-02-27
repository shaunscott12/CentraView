<%--
 * $RCSfile: view_custom_field.jsp,v $    $Revision: 1.5 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Vector" %>
<%
  Vector vec = new Vector();
  if (request.getAttribute("valuelist") != null) {
    vec = (Vector) request.getAttribute("valuelist");
  }

  HashMap vecIds = new HashMap();
  if (request.getAttribute("valueidslist") != null) {
    vecIds = (HashMap) request.getAttribute("valueidslist");
  }
%>
<script language="javascript">
  function addValue()
  {
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

  function removeValue()
  {
    for (i = 0; i < document.getElementById('valuelist').options.length; i++) {
      if (document.getElementById('valuelist').options[i].selected == true) {
        document.getElementById('valuelist').options[i] = null;
      }
    }
    document.getElementById('newValue').value = "";
    return true;
  }   // end removeValue method

  function gotocancel()
  {
    c_goTo('/administration/list_custom_fields.do?module=<c:out value="${customfield.map.module}" />');
  }

  function gotosave(param)
  {
    if (document.getElementById('layerSwitch').value == "MULTIPLE") {
      if (document.getElementById('valuelist').length > 0) {
        for (i = 0; i < document.getElementById('valuelist').length; i++) {
          document.getElementById('valuelist').options[i].selected = "true";
        }
      }
    }
    document.customfield.action = "<html:rewrite page="/administration/save_edit_custom_field.do"/>?buttonpress="+param;
    document.customfield.submit();
    return false;
  }

  function deleteCustomField()
  {
    document.customfield.action = "<html:rewrite page="/administration/delete_custom_field.do"/>";
    document.customfield.submit();
    return false; 
  }
  
  function setText()
  {
    for (i = 0; i < document.getElementById('valuelist').options.length; i++) {
      if (document.getElementById('valuelist').options[i].selected == true) {
        document.getElementById('newValue').value = document.getElementById('valuelist').options[i].value;
        i = document.getElementById('valuelist').options.length;
        break;
      }
    }
    return true;
  }

  function editValue()
  {
    if (document.getElementById('newValue').value != "") {
      for (i = 0; i < document.getElementById('valuelist').options.length; i++) {
        if (document.getElementById('valuelist').options[i].selected == true) {
          document.getElementById('valuelist').options[i].value = document.getElementById('newValue').value;
          document.getElementById('valuelist').options[i].text = document.getElementById('newValue').value;
          i = document.getElementById('valuelist').options.length;
          break;
        }
      }
      document.getElementById('newValue').value = "";
    }
    return true;
  }
</script>
<html:form action="/administration/view_custom_field.do">
<html:hidden property="fieldid" styleId="fieldid" />
<html:hidden property="layerSwitch" styleId="layerSwitch" />
<html:hidden property="module" styleId="module" />
<html:hidden property="recordTypeId" styleId="recordTypeId" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.customfieldsdetails"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.fieldname"/>:</td>
    <td class="contentCell"><html:text property="fieldname" styleId="fieldname"  styleClass="inputBox" size="45"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.module"/>:</td>
    <td class="contentCell"><c:out value="${customfield.map.module}" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.recordtype"/>:</td>
    <td class="contentCell"><c:out value="${customfield.map.recordtype}" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.fieldtype"/>:</td>
    <td class="contentCell">
    <c:choose>
      <c:when test="${customfield.map.layerSwitch == 'SCALAR'}"><bean:message key='label.value.single'/> </c:when>
      <c:otherwise><bean:message key='label.value.multiple'/> </c:otherwise>
    </c:choose>
    </td>
  </tr>
  <c:if test="${customfield.map.layerSwitch == 'MULTIPLE'}">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.values"/>:</td>
    <td class="contentCell">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td colspan="2" class="contentCell">
            <html:text property="newValue" styleId="newValue" styleClass="inputBox" size="30" style="width:200px;" />
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <select name="valuelist" id="valuelist" multiple="true" style="width:200px;" size="8" class="inputBox" onchange="setText();">
            <%
              String hiddenFields = "";
              if (vec != null) {
                for (int i=0; i<vec.size(); i++) {
                  String name = (String)vec.get(i);
                  Long id = (Long) vecIds.get(name);
                  hiddenFields = hiddenFields + "<input type=\"hidden\" name=\"optionName" + name + "\"  id=\"optionName" + name + "\"  value=\"" + id + "\">";
                  %><option value="<%=name%>" ><%=name%></option><%
                }
              }
            %>
            </select>
            <%=hiddenFields%>
          </td>
          <td class="contentCell">
            <html:button property="addvalue" styleId="addvalue" styleClass="normalButton" onclick="addValue();" style="width: 7em;">
              <bean:message key="label.administration.addvalue"/>
            </html:button><br/><br/>
            <html:button property="addvalue" styleId="addvalue" styleClass="normalButton" onclick="editValue();" style="width: 7em;">
              <bean:message key="label.administration.editvalue"/>
            </html:button><br/><br/>
            <html:button property="addvalue" styleId="addvalue" styleClass="normalButton" onclick="removeValue();" style="width: 7em;">
              <bean:message key="label.administration.removevalue"/>
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  </c:if>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:button property="saveandclose" styleId="saveandclose" styleClass="normalButton" onclick="gotosave('saveclose');">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="saveandnew" styleId="saveandnew" styleClass="normalButton" onclick="gotosave('savenew')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="delete" styleId="delete" styleClass="normalButton" onclick="deleteCustomField();">
        <bean:message key="label.delete"/>
      </html:button>  
      <html:button property="cancel" styleId="cancel" styleClass="normalButton" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
</table>
</html:form>
