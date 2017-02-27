<%--
 * $RCSfile: permission_body.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*"%>
<%
  String modulename = request.getAttribute("modulename").toString();
  String rowID = request.getAttribute("rowID").toString();
  session.setAttribute("modulename",modulename);
  session.setAttribute("rowID",rowID);
  com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
  int uid = userobjectd.getIndividualID();
  request.setAttribute("userID", new Integer(uid));

  Vector view=new Vector();
  Vector modify=new Vector();
  Vector delete=new Vector();

  Collection colview=(Collection)request.getAttribute("colview");
  Collection colmodify=(Collection)request.getAttribute("colmodify");
  Collection coldelete=(Collection)request.getAttribute("coldelete");

  pageContext.setAttribute("colview", colview, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("colmodify", colmodify, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("coldelete", coldelete, PageContext.PAGE_SCOPE);

  if ((Vector)request.getAttribute("view") != null) {
    view=(Vector)request.getAttribute("view");
  }
  if ((Vector)request.getAttribute("modify") != null) {
    modify=(Vector)request.getAttribute("modify");
  }
  if ((Vector)request.getAttribute("delete")!=null) {
    delete = (Vector)request.getAttribute("delete");
  }

  pageContext.setAttribute("view", view, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("modify", modify, PageContext.PAGE_SCOPE);
  pageContext.setAttribute("deleten", delete, PageContext.PAGE_SCOPE);
  // put employeeList on the page context?
%>
<script language="javascript">
<!--
  function add(Listbox)
  {
    var listLength; listLength = document.forms[0].employeeSelect.length;
    ctr = 0 ;
    while (ctr < document.forms[0].employeeSelect.length)
    {
      if (document.forms[0].employeeSelect.options[ctr].selected) {
        var selectedOption = document.forms[0].employeeSelect.options[ctr];
        var selectedOptionText = selectedOption.text;
        var selectedOptionValue = selectedOption.value;
        if (IsPresent(selectedOptionValue,Listbox) == false) {
          var option0 = new Option(selectedOptionText, selectedOptionValue);
          var newIndex = Listbox.length;
          Listbox.options[newIndex] = option0;
          document.forms[0].employeeSelect.options[ctr] = null;
          listLength = document.forms[0].employeeSelect.length;
          ctr = 0;
        } else {
          ctr++;
        }
      } else {
        ctr++;
      }
    }
  }

  function RemoveSelect(Listbox)
  {
    var j;
    j = 0;
    while (j<Listbox.length) {
      if (Listbox.options[j].selected) {
        var selectedOption = Listbox.options[j];
        var selectedOptionText = selectedOption.text;
        var selectedOptionValue = selectedOption.value;
        if (IsPresent(selectedOptionValue,document.forms[0].employeeSelect) == false) {
          var option0 = new Option(selectedOptionText, selectedOptionValue);
          var newIndex = document.forms[0].employeeSelect.length;
          document.forms[0].employeeSelect.options[newIndex] = option0;
          Listbox.options[j] = null;
          j = 0;
        } else {
          j++;
        }
      } else {
       j++;
      }
    }
  }

  function IsPresent(strValue,Listbox)
  {
    var i;
    i = 0;
    while (i<Listbox.length) {
      if (Listbox.options[i].value == strValue) {
        return true;
      }
      i = i + 1;
    }
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
         var newIndex = document.forms[0].employeeSelect.length;
         document.forms[0].employeeSelect.options[newIndex] = newOption;
         listBox.options[j--] = null;
      }
   }

  function checkClicked()
  {
     if (document.forms[0].isPublic.checked) {
        clearList(document.forms[0].view);
        clearList(document.forms[0].modify);
        clearList(document.forms[0].deleten);
     }
     document.forms[0].add1.disabled = document.forms[0].add2.disabled = document.forms[0].add3.disabled = document.forms[0].isPublic.checked;
  }

  function savePermissions()
  {
    var view=document.forms[0].view.options.length;
    var modify=document.forms[0].modify.options.length;
    var deleten=document.forms[0].deleten.options.length;
    for (var i=0; i < view; i++) {
      document.forms[0].view.options[i].selected = true;
    }
    for(var i=0;i<modify;i++) {
      document.forms[0].modify.options[i].selected = true;
    }
    for(var i=0;i<deleten;i++) {
      document.forms[0].deleten.options[i].selected = true;
    }
    window.document.forms[0].action="<html:rewrite page="/common/save_record_permission.do?closeornew=close&TYPEOFACTIVITY=BUTTON"/>";
    window.document.forms[0].method = "post";
    window.document.forms[0].submit();
    return(false);
  }
//-->
</script>
<html:form action="/common/save_record_permission.do">
<input type="hidden" value="<c:out value="${requestScope.id}"/>" id="employeeId">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.tiles.permissions"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td>
       <table border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td>
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td class="labelCellBold"><bean:message key="label.tiles.public"/></td>
                <td><input type="checkbox" name="isPublic" onclick="checkClicked()" <logic:present name="isPublic"><bean:message key="label.tiles.checked"/></logic:present> id="publicCheck"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <html:select property="employeeSelect" size="15" styleClass="inputBox" style="width:20em;" multiple="true">
              <html:optionsCollection property="employeeList" label="label" value="value"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
    <td>
      <table border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td>
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><input name="addView" type="button" class="normalButton" value=" <bean:message key='label.value.add'/> &raquo; "style="width:7em;" onclick="add(view);" id="add1"></td>
              </tr>
              <tr>
                <td><input name="removeView" type="button" class="normalButton" value="&laquo; <bean:message key='label.value.remove'/>"style="width:7em;" onclick="RemoveSelect(view);"></td>
              </tr>
            </table>
          </td>
          <td style="padding:5px;">
            <table border="0" cellpadding="0" cellspacing="0">
              <tr><td class="labelCellBold"><bean:message key="label.tiles.view"/><br></tr>
              <tr>
                <td>
                  <html:select property="view" size="5" styleClass="inputBox" multiple="true" style="width:20em;">
                    <html:options collection="colview" property="value" labelProperty="label"/>
                  </html:select>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td>
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><input name="addModify" type="button" class="normalButton" value=" <bean:message key='label.value.add'/> &raquo; "style="width:7em;" onclick="add(modify);" id="add2"></td>
              </tr>
              <tr>
                <td><input name="removeModify" type="button" class="normalButton" value="&laquo; <bean:message key='label.value.remove'/>"style="width:7em;" onclick="RemoveSelect(modify);"></td>
              </tr>
            </table>
          </td>
          <td style="padding:5px;">
            <table border="0" cellpadding="0" cellspacing="0">
              <tr><td class="labelCellBold"><bean:message key="label.tiles.viewmodify"/><br></tr>
              <tr>
                <td>
                  <html:select property="modify" size="5" styleClass="inputBox" multiple="true" style="width:20em;">
                    <html:options collection="colmodify" property="value" labelProperty="label"/>
                  </html:select>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td>
            <table border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td><input name="addDelete" type="button" class="normalButton" value=" <bean:message key='label.value.add'/> &raquo; "style="width:7em;" onclick="add(deleten);" id="add3"></td>
              </tr>
              <tr>
                <td><input name="removeDelete" type="button" class="normalButton" value="&laquo; <bean:message key='label.value.remove'/>"style="width:7em;" onclick="RemoveSelect(deleten);"></td>
              </tr>
            </table>
          </td>
          <td style="padding:5px;">
            <table border="0" cellpadding="0" cellspacing="0">
              <tr><td class="labelCellBold"><bean:message key="label.tiles.viewmodifydelete"/></td></tr>
              <tr>
                <td>
                  <html:select property="deleten" size="5" styleClass="inputBox" multiple="true" style="width:20em;">
                    <html:options collection="coldelete" property="value" labelProperty="label"/>
                  </html:select>
                </td>
              <tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader">
      <input type="button" name="saveandclose" value="<bean:message key='label.value.saveandclose'/>" class="normalButton" onclick="savePermissions();">
      <input type="submit" name="cancel" value="<bean:message key='label.value.cancel'/>" class="normalButton" onclick="window.close();">
    </td>
  </tr>
</table>
</html:form>
<script language="javascript">
<!--
  checkClicked();
//-->
</script>