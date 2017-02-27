<%--
 * $RCSfile: availability.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/20 20:22:23 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td colspan="2" style="height:200px;" valign="top">
      <iframe src="<html:rewrite page="/activities/available_list.do"/>?TYPEOFOPERATION=<%=request.getAttribute("TYPEOFOPERATION")%>.toString()%>" width="100%" frameborder="no"></iframe>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td valign="top" width="75%">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.startdate"/></td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text property="activityStartDate" styleId="activityStartDate" styleClass="inputBox" size="10" maxlength="10"/></td>
                <td><a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" /></a></td>
                <td>
                  <html:text property="activityStartTime" styleId="activityStartTime" styleClass="inputBox" size="8" maxlength="8"/>
                  <a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_reminder.gif" width="19" height="19" border="0" alt="" align="absmiddle"/></a>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.enddate"/></td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text property="activityEndDate" styleId="activityEndDate" styleClass="inputBox" size="10" maxlength="10"/></td>
                <td><a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" /></a></td>
                <td>
                  <html:text property="activityEndTime" styleId="activityEndTime" styleClass="inputBox" size="8" maxlength="8"/>
                  <a class="plainLink" onclick="act_openSelectDateTime();"><html:img page="/images/icon_reminder.gif" width="19" height="19" border="0" alt="" align="absmiddle" /></a>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
    <td width="25%" valign="top">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader" colspan="2"><bean:message key="label.activity.key"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.useravailable"/></td>
          <td class="availCellOdd" width="25">&nbsp;</td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.userbusy"/></td>
          <td class="availBusyCell" width="25">&nbsp;</td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.activity.timeactivity"/></td>
          <td class="availScheduledCell" width="25">&nbsp;</td>
        </tr>
      </table>
    </td>
  </tr>
</table>