<%--
/*
 * $RCSfile: header.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/21 17:41:33 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.common.UserObject" %>
<%@ page import="com.centraview.common.UserPrefererences" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix"%>
<%
  UserObject uo = (UserObject)session.getAttribute("userobject");
  ModuleFieldRightMatrix mfrm = new ModuleFieldRightMatrix();
  String name ="";
  String loginName ="";
  if (uo != null) {
    name = uo.getfirstName() + " " + uo.getlastName();
    loginName = "(" + uo.getLoginName() + ")";
    UserPrefererences userPrefs = uo.getUserPref();
    if (userPrefs != null) {
      mfrm = userPrefs.getModuleAuthorizationMatrix();
  }

  }
%>

<table border="0" cellspacing="0" cellpadding="0" class="headerTable">
  <tr>
    <td class="headerLeft">
      <html:link action="/home.do"><html:img page="/images/logo_centraview.jpg" width="182" height="65" border="0" alt="Logo" title="CentraView" /></html:link>
    </td>
    <td class="headerRight">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="headerRightTop">
            <table border="0" cellspacing="0" cellpadding="0" >
              <tr>
                <td class="headerButtons" nowrap><html:img page="/images/w-top-icon-help.gif" width="17" height="17" border="0" align="absmiddle" alt="?" title="Help"/><a class="tabsOff" onclick="c_openWindow('/help/centraview.htm', 'help', 600, 580, '');"><bean:message key="label.tiles.help"/></a></td>
                <c:if test="${sessionScope.userobject.userType == 'ADMINISTRATOR'}"><td class="headerButtons" nowrap><html:img page="/images/w-top-icon-admin.gif" width="17" height="17" border="0" align="absmiddle" alt="" title="Administration" /><html:link action="/administration/user_list.do" styleClass="tabsOff"><bean:message key="label.tiles.administration"/></html:link></td></c:if>
                <td class="headerButtons" nowrap><html:img page="/images/w-top-icon-sync.gif" width="17" height="17" border="0" align="absmiddle" alt="" title="Synchronize" /><html:link action="/preference/display_sync.do" styleClass="tabsOff"><bean:message key="label.tiles.synchronize"/></html:link></td>
                <td class="headerButtons" nowrap><html:img page="/images/w-top-icon-prefs.gif" width="17" height="17" border="0" align="absmiddle" alt="" title="Preferences" /><html:link action="/preference/user_profile.do" styleClass="tabsOff"><bean:message key="label.tiles.preferences"/></html:link></td>
                <td class="headerButtons" nowrap><html:img page="/images/w-top-icon-reports.gif" width="17" height="17" border="0" align="absmiddle" alt="" title="Reports" /><html:link action="/reports/standard_list.do" styleClass="tabsOff"><bean:message key="label.tiles.reports"/></html:link></td>
                <td class="headerButtonsEnd"><html:img page="/images/spacer.gif" height="1" width="1" alt="" /></td>
                <td class="headerButtonSpacer"><html:img page="/images/spacer.gif" height="1" width="40" alt="" /></td>
                <td class="headerButtons" nowrap><html:link page="/logout.do" styleClass="tabsOff"><bean:message key="label.tiles.logout"/></html:link></td>
                <td class="headerButtonsEnd"><html:img page="/images/spacer.gif" height="1" width="1" alt="" /></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="headerRightBottom">
          <div class="headerTextContainer">
            <div class="headerWelcomeText">
              <span class="headerTextBold"><bean:message key="label.tiles.welcome"/> <%=name%> <%=loginName%></span>
              <%-- NOTE: the alert text and the welcome text never show at the same time --%>
              <%-- <span class="headerAlertText"><bean:message key="label.tiles.alerthere"/></span> --%>
            </div>
            <div class="headerPoweredBy">
              <span class="headerTextSmall"><bean:message key="label.tiles.poweredby"/> <html:link href="http://www.centraview.com/" target="_blank" styleClass="headerTextSmall">CentraView</html:link></span>
            </div>
          </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>