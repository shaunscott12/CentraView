<%--
 * $RCSfile: default_preferences.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*"%>
<script language="javascript">
  function gotoCancel()
  {
    c_goTo("/administration/user_list.do");
  }

  function searchEmployee()
  {
    var searchStr = document.getElementById('search').value;
    document.forms.permissionform.action = "<html:rewrite page="/administration/default_preferences.do" />?search=" + searchStr;
    document.forms.permissionform.submit();
  }

  function add(Listbox)
  {
    var ctr;
    ctr = 0;
    var employeeList = document.getElementById('ListMain');
    while (ctr < employeeList.length) {
      if (employeeList.options[ctr].selected) {
        var selectedOption = employeeList.options[ctr];
        var selectedOptionText = selectedOption.text;
        var selectedOptionValue = selectedOption.value;

        if (! IsPresent(selectedOptionValue, Listbox)) {
          var option0 = new Option(selectedOptionText, selectedOptionValue);
          var newIndex = Listbox.length;
          Listbox.options[newIndex] = option0;
          employeeList.options[ctr] = null;
        }
      } else {
        ctr = ctr + 1;
      }
    }
  }

  function RemoveSelect(Listbox)
  {
    var j;
    j = 0;
    while (j < Listbox.length) {
      if (Listbox.options[j].selected) {
        var selectedOption = Listbox.options[j];
        var selectedOptionText = selectedOption.text;
        var selectedOptionValue = selectedOption.value;

        var employeeList = document.getElementById('ListMain');

        if (IsPresent(selectedOptionValue, employeeList) == false) {
          var option0 = new Option(selectedOptionText, selectedOptionValue);
          var newIndex = employeeList.length;
          employeeList.options[newIndex] = option0;
        }
        Listbox.options[j] = null;
      } else {
        j = j + 1;
      }
    }
  }

  function IsPresent(strValue,Listbox)
  {
    var i;
    i = 0;

    while (i < Listbox.length) {
      if (Listbox.options[i].value == strValue) {
        return true;
      }
      i = i + 1;
    }
    return false;
  }

  function submitForm(closeornew)
  {
    var viewBox = document.getElementById('view');
    var modifyBox = document.getElementById('modify');
    var deleteBox = document.getElementById('deleten');

    for (var i = 0; i < viewBox.options.length; i++) {
      viewBox.options[i].selected = true;
    }

    for (var i = 0; i < modifyBox.options.length; i++) {
      modifyBox.options[i].selected = true;
    }

    for (var i = 0; i < deleteBox.options.length; i++) {
      deleteBox.options[i].selected = true;
    }

    document.forms.permissionform.action = "<html:rewrite page="/administration/save_default_preferences.do"/>?closeornew=" + closeornew + "&TYPEOFACTIVITY=BUTTON";
    document.forms.permissionform.submit();
    return false;
  }

  function clearList(listBox)
  {
    var j = listBox.length - 1;

    while (j >= 0) {
      var theOption = listBox.options[j];
      var optionText = theOption.text;
      var optionValue = theOption.value;
      var newOption = new Option(optionText, optionValue);
      var newIndex = document.getElementById('ListMain').length;
      document.getElementById('ListMain').options[newIndex] = newOption;
      listBox.options[j--] = null;
    }
  }

  function checkClicked()
  {
    if (document.getElementById('isPublic').checked) {
      clearList(document.getElementById('view'));
      clearList(document.getElementById('modify'));
      clearList(document.getElementById('deleten'));
    }

    document.getElementById('addViewButton').disabled = document.getElementById('addModifyButton').disabled = document.getElementById('addDeleteButton').disabled = document.getElementById('isPublic').checked;
  }
</script>
<%
  int uid = Integer.parseInt(request.getAttribute("userID").toString());
  Vector view = new Vector();
  Vector modify = new Vector();
  Vector delete = new Vector();

  Collection colview = (Collection)request.getAttribute("colview");
  Collection colmodify = (Collection)request.getAttribute("colmodify");
  Collection coldelete = (Collection)request.getAttribute("coldelete");

  pageContext.setAttribute("colview", colview, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("colmodify", colmodify, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("coldelete", coldelete, PageContext.PAGE_SCOPE);

  if ((Vector)request.getAttribute("view") != null) {
    view = (Vector)request.getAttribute("view");
  }

  if ((Vector)request.getAttribute("modify") != null) {
    modify = (Vector)request.getAttribute("modify");
  }

  if ((Vector)request.getAttribute("delete") != null) {
    delete = (Vector)request.getAttribute("delete");
  }

  pageContext.setAttribute("view", view, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("modify", modify, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("deleten", delete, PageContext.PAGE_SCOPE);

  DisplayList DL = (DisplayList)request.getAttribute("employeelist");
%>
<html:form action="/administration/save_default_preferences.do">
<input type="hidden" name="userID" value="<%=uid%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.userdefaultprivileges"/></td>
  </tr>
</table>
<table border="0" cellpadding="5" cellspacing="0" class="formTable">
  <tr>
    <td>
      <table border="0" cellspacing="0" cellpadding="3">
        <tr>
          <td class="labelCell"><input name="search" id="search" type="text" class="inputBox" size="30"></td>
          <td class="contentCell"><input name="searchButton" type="button" class="normalButton" value="<bean:message key='label.value.search'/>" onclick="searchEmployee();" /></td>
        </tr>
        <tr>
          <td colspan="2" class="labelCell">
            <select name="ListMain" id="ListMain" size="20" multiple class="inputBox" style="width:23em;">
            <%
              if (DL.size() > 0) {
                Set s = DL.keySet();
                Iterator iter = s.iterator();
                while (iter.hasNext()) {
                  String sKey = (String)iter.next();
                  ListElement lem = (ListElement)DL.get(sKey);
                  int empID = lem.getElementID();
                  StringMember sm = (StringMember)lem.get("Name");
                  String name = (String)sm.getMemberValue();

                  if (! view.contains(new Long(empID)) &&
                      ! modify.contains(new Long(empID)) &&
                      ! delete.contains(new Long(empID)) &&
                      empID != uid) {
                    %><option value="<%=empID %>"><%=sm.getDisplayString()%><%
                  }
                }
              }
            %>
            </select>
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.administration.public"/>: <input type="checkbox" name="isPublic" id="isPublic" onclick="checkClicked();" <logic:present name="isPublic"><bean:message key="label.administration.checked"/></logic:present>></td>
        </tr>
      </table>
    </td>
    <td>
      <table border="0" cellpadding="4" cellspacing="0">
        <tr>
          <td>&nbsp;</td>
          <td class="labelCellBold"><bean:message key="label.administration.view"/></td>
        </tr>
        <tr>
          <td>
            <input type="button" name="addViewButton" id="addViewButton" value=" <bean:message key='label.value.add'/> &raquo;" onclick="add(view);" class="normalButton" style="width:7em;"><br/><br/>
            <input type="button" name="removeViewButton" id="removeViewButton" value="&laquo; <bean:message key='label.value.remove'/> " onclick="RemoveSelect(view);" class="normalButton" style="width:7em;">
          </td>
          <td class="contentCell">
            <html:select property="view" styleId="view" multiple="true" size="5" styleClass="inputBox" style="width:20em;">
              <html:options collection="colview" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td class="labelCellBold"><bean:message key="label.administration.view"/> &amp;<bean:message key="label.administration.modify"/></td>
        </tr>
        <tr>
          <td>
            <input type="button" name="addModifyButton" id="addModifyButton" value=" <bean:message key='label.value.add'/> &raquo;" onclick="add(modify);" class="normalButton" style="width:7em;"><br/><br/>
            <input type="button" name="removeModifyButton" id="removeModifyButton" value="&laquo; <bean:message key='label.value.remove'/> " onclick="RemoveSelect(modify);" class="normalButton" style="width:7em;">
          </td>
          <td class="contentCell">
            <html:select property="modify" styleId="modify" multiple="true" size="5" styleClass="inputBox" style="width:20em;">
              <html:options collection="colmodify" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td class="labelCellBold"><bean:message key="label.administration.view"/> &amp; <bean:message key="label.administration.modify"/> &amp; <bean:message key="label.administration.delete"/></td>
        </tr>
        <tr>
          <td>
            <input type="button" name="addDeleteButton" id="addDeleteButton" value=" <bean:message key='label.value.add'/> &raquo;" onclick="add(deleten);" class="normalButton" style="width:7em;"><br/><br/>
            <input type="button" name="removeDeleteButton" id="removeDeleteButton" value="&laquo; <bean:message key='label.value.remove'/> " onclick="RemoveSelect(deleten);" class="normalButton" style="width:7em;">
          </td>
          <td class="contentCell">
            <html:select property="deleten" styleId="deleten" multiple="true" size="5" styleClass="inputBox" style="width:20em;">
              <html:options collection="coldelete" property="strid" labelProperty="name"/>
            </html:select>
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
      <app:cvbutton property="saveandclose" styleId="saveandclose" styleClass="normalButton" onclick="return submitForm('close');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <html:reset styleClass="normalButton">
        <bean:message key="label.reset"/>
      </html:reset>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="gotoCancel();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>
<script language="javascript">
  checkClicked();
</script>