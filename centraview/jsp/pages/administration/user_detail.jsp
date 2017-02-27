<%--
/*
 * $RCSfile: user_detail.jsp,v $    $Revision: 1.6 $  $Date: 2005/09/23 11:02:15 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.administration.user.UserSettingsForm" %>
<%
  UserSettingsForm userSettingsForm = (UserSettingsForm)request.getAttribute("userform");
  Vector securityProfiles = new Vector();
  Vector securityProfileVect = (Vector)session.getAttribute("securityProfileVect");
%>
<script language="javascript">
  function winclose()
  {
    window.close();
    return false;
  }
  function checkAddUser(saveclosenew)
  {
    var profiles = document.getElementById('securityProfiles');
    <%-- //Select all the values present in the profile list --%>
    if (profiles.length > 0)
    {
      for (i=0; i<profiles.length; i++)
      {
        profiles.options[i].selected = "true";
      }
    }
    var form = document.getElementById('userForm');
    form.action = "<html:rewrite page="/administration/save_user.do"/>?saveclosenew=" + saveclosenew +"<c:if test="${requestScope.newUser}">&newUser=true</c:if>";
    form.submit();
    return false;
  }
  function gotoCancel()
  {
    c_goTo('/administration/user_list.do');
  }
  function gotoDefaultPermissions()
  {
    var form = document.getElementById('userForm');
    form.action = "<html:rewrite page="/administration/default_preferences.do"/>";
    form.submit();
  }
  function personLookup()
  {
    var userType = document.getElementById('userType');
    if (userType.value == 'EMPLOYEE' || userType.value == 'ADMINISTRATOR') {
      c_lookup('Employee');
    } else {
      c_lookup('Individual');
    }
  }
  function setIndividual(lookupValues)
  {
    // pass off to setEmployee on details.js
    setEmployee(lookupValues);
  }
  function addToParentList(sourceList)
  {
    /* <%--
     * the "sourceList" parameter is the list of checkboxes
     * that are passed FROM the lookup window back to this
     * method. The "securityProfileList" variable below is
     * the object on this screen which holds the currently
     * selected profiles in a <select> box. --%>
     */
    securityProfileList = document.getElementById('securityProfiles');

    var len = securityProfileList.length;

    /* <%--
     * sourceList will either be an array of checkbox objects
     * or one single checkbox, depending on how many defined
     * security profiles exist. So we need to be careful of
     * how we treat this variable. --%>
     */
    if (sourceList.length > 0) {
      // <%-- sourceList is an array of checkboxes --%>
      for (var i=0; i<sourceList.length; i++) {
        if (sourceList[i].checked) {
          var sourceListArray = sourceList[i].value.split("|");

          var found = false;

          for (var count=0; count<len; count++) {
            if (securityProfileList.options[count] != null) {
              if (sourceListArray[0] == securityProfileList.options[count].value) {
                found = true;
                break;
              }
            }
          }

          if (found != true) {
            securityProfileList.options[len] = new Option(sourceListArray[1]);
            securityProfileList.options[len].value = sourceListArray[0];
            len++;
          }
        }
      }   // end for
    }else{
      // <%-- sourceList is a single checkbox object --%>
      if (sourceList.checked) {
        var sourceListArray = sourceList.value.split("|");
        var found = false;

        for (var count=0; count<len; count++) {
          if (securityProfileList.options[count] != null) {
            if (sourceListArray[0] == securityProfileList.options[count].value) {
              found = true;
              break;
            }
          }
        }

        if (found != true) {
          securityProfileList.options[len] = new Option(sourceListArray[1]);
          securityProfileList.options[len].value = sourceListArray[0];
          len++;
        }
      }
    }   // end if (sourceList.length > 0)
  }

  // <%-- Deletes the selected items of supplied list. --%>
  function deleteSelectedItemsFromList(sourceList)
  {
    var maxCnt = sourceList.options.length;
    for (var i=maxCnt-1; i>="0"; i--) {
      if ((sourceList.options[i] != null) && (sourceList.options[i].selected == true)) {
        sourceList.options[i] = null;
      }
    }
  }

</script>
<html:form action="/administration/save_user" styleId="userForm">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr height="17">
    <td class="sectionHeader"><bean:message key="label.administration.usersettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="5" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell" nowrap><bean:message key="label.admin.useradmin.type"/>:</td>
    <td align="left" class="contentCell" valign="top">
      <html:select property="userType" styleClass="inputBox" styleId="userType">
        <html:option value="EMPLOYEE"><bean:message key="label.administration.employee"/></html:option>
        <html:option value="CUSTOMER"><bean:message key="label.administration.customer"/></html:option>
        <html:option value="ADMINISTRATOR"><bean:message key="label.administration.administrator"/></html:option>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top" width="9%"><bean:message key="label.administration.name"/>: </td>
    <td align="left" class="contentCell" valign="top" width="91%">
      <html:text property="name" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('employeeId').value);" styleId="employeeName" />
      <input type="button" name="lookup" value="<bean:message key='label.value.change'/>" class="normalButton" onclick="personLookup();">
      <html:hidden property="contactID" styleId="employeeId" />
      <html:hidden property="userID"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.admin.useradmin.username"/>:</td>
    <td align="left" class="contentCell" valign="top" width="91%"><html:text property="userName" styleClass="inputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.admin.useradmin.newpassword"/>:</td>
    <td align="left" class="contentCell" valign="top"><html:password property="password" styleClass="inputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell" nowrap><bean:message key="label.admin.useradmin.confirmpassword"/>:</td>
    <td align="left" class="contentCell" valign="top"><html:password property="confirmPassword" styleClass="inputBox"/></td>
  </tr>
  <tr>
    <td class="labelCell" nowrap><bean:message key="label.admin.useradmin.securityprofiles"/>:</td>
    <td align="left" class="contentCell" style="vertical-align:middle;">
      <div style="float:left; padding:3px;">
        <html:select property="profiles" size="6" multiple="true" style="width:150px;" styleClass="inputBox" styleId="securityProfiles">
        <% if (securityProfileVect != null) { %>
          <html:options collection="securityProfileVect" property="id" labelProperty="name"/>
        <% } %>
        </html:select>
      </div>
      <div style="vertical-align:middle; padding:3px;">
        <input class="normalButton" name="lookup2" onclick="c_openWindow('/administration/security_profile_lookup.do', 'lui', 400, 400, '');" style="font-size:100%;vertical-align:middle" type="button" value="<bean:message key="button.admin.useradmin.lookup"/>"><br/><br/>
        <input name="remove" type="button" class="normalButton" value="<bean:message key='button.admin.useradmin.remove'/>" style="font-size:100%;vertical-align:middle;" onclick="deleteSelectedItemsFromList(profiles)"/>
      </div>
    </td>
  </tr>
  <tr>
    <td class="labelCell" nowrap><bean:message key="label.administration.enabled"/>:</td>
    <td align="left" class="contentCell" valign="top">
      <html:radio property="enabled" value="ENABLED"/> <bean:message key="label.admin.useradmin.enabled.yes"/>
      <html:radio property="enabled" value="DISABLED" /> <bean:message key="label.admin.useradmin.enabled.no"/>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <c:if test="${!requestScope.newUser}">
      <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddUser('save');">
        <bean:message key="button.admin.useradmin.save"/>
      </app:cvbutton>
      </c:if>
      <app:cvbutton property="savenclose" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddUser('close');">
        <bean:message key="button.admin.useradmin.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="savennew" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddUser('new');">
        <bean:message key="button.admin.useradmin.saveandnew"/>
      </app:cvbutton>
      <app:cvbutton property="cancel" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="gotoCancel();">
        <bean:message key="button.admin.useradmin.cancel"/>
      </app:cvbutton>
      <c:if test="${!requestScope.newUser}">
      <app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="gotoDefaultPermissions();">
        <bean:message key="button.admin.useradmin.defaultprivileges"/>
      </app:cvbutton>
      </c:if>
    </td>
  </tr>
</table>
</html:form>