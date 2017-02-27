<%--
 * $RCSfile: server_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.common.*,com.centraview.administration.configuration.*"%>
<%@ page import="com.centraview.administration.common.AdministrationConstantKeys" %>
<%@ page import="com.centraview.administration.authorization.*" %>
<script language="javascript">
  function openSelectDateTime()
  {
    var startTime = document.getElementById('detailStartTime').value;
    if (startTime == ""){ startTime = ""; }
    var endTime = document.getElementById('detailEndTime').value;
    if (endTime == ""){ endTime = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=6&startDate=&endDate=&startTime='+startTime+'&endTime='+endTime, 'selectDateTime', 350, 500, '');
  }
  /**
   * Called by the /calendar/select_date_time.do popup, this function takes
   * input from that screen via the function parameters, and populates the
   * form fields on this JSP for the Estimated Close Date field.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION!!!!
   */
  function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
  {
    if (startTime != null && startTime != "") {
      document.getElementById('detailStartTime').value = startTime;
    }
    if (endTime != null && endTime != "") {
      document.getElementById('detailEndTime').value = endTime;
    }
    return(true);
  }
</script>
<html:form action="/administration/save_server_settings.do" >
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.serversettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.hostname"/>:</td>
    <td class="contentCell"><html:text property="hostName" styleId="hostName" styleClass="inputBox" size="45" /></td>
  </tr>
  <html:hidden property="sessionTimeout" />
  <%--  // session timeout can only be changed by a Tomcat restart
  <tr>
    <td class="labelCell"><bean:message key="label.administration.sessiontimeout"/>:</td>
    <td class="contentCell"><html:text property="sessionTimeout" styleId="sessionTimeout" styleClass="inputBox" size="6" /></td>
  </tr>
  --%>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.workinghours"/>:</td>
    <td class="contentCell">
      <bean:message key="label.administration.from"/>:
      <html:text property="detailStartTime" styleId="detailStartTime" styleClass="inputBox" size="9" maxlength="8"/>
      <a class="plainLink" onclick="openSelectDateTime();"><html:img page="/images/icon_reminder.gif" width="17" height="17" align="abstop" /></a>
      <bean:message key="label.administration.to2"/>:
      <html:text property="detailEndTime" styleId="detailEndTime" styleClass="inputBox" size="9" maxlength="8"/>
      <a class="plainLink" onclick="openSelectDateTime();"><html:img page="/images/icon_reminder.gif" width="17" height="17" align="abstop" /></a>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.emailcheckinterval"/>:</td>
    <td class="contentCell"><html:text property="emailCheckInterval" styleId="emailCheckInterval" styleClass="inputBox" size="6" /> <bean:message key="label.administration.minutes"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.filesystemstoragepath"/>:</td>
    <td class="contentCell"><html:text property="fileSystemStoragePath" styleId="fileSystemStoragePath" styleClass="inputBox" size="45"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.defaulttimezone"/>:</td>
    <td class="contentCell">
      <html:select property="defaultTimeZone" styleId="defaultTimeZone" styleClass="inputBox">
        <html:option value="EST"><bean:message key="label.administration.esttimezone"/></html:option>
        <html:option value="MST"><bean:message key="label.administration.msttimezone"/></html:option>
        <html:option value="PST"><bean:message key="label.administration.psttimezone"/></html:option>
      </html:select>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvsubmit property="save" styleId="save" styleClass="normalButton">
        <bean:message key="label.save" />
      </app:cvsubmit >
      <app:cvreset property="reset" styleId="reset" styleClass="normalButton">
        <bean:message key="label.reset" />
      </app:cvreset >
    </td>
  </tr>
</table>
</html:form>