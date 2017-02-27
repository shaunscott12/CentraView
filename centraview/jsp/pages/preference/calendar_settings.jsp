<%--
 * $RCSfile: calendar_settings.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:45 $ - $Author: mcallist $
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
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*"%>
<%@ page import="com.centraview.common.DDNameValue" %>
<%
  Vector minute_second = new Vector();
  DDNameValue ddObj = new DDNameValue(0, "0");
  for (int i = 1; i < 61; i++) {
    ddObj = new DDNameValue(i, i+"");
    minute_second.add(ddObj);
  }
%>
<script language="javascript">
  function saveSettings(button)
  {
    button.disabled = true;
    button.className = "disabledButton";
    document.prefCalendarForm.action = "<html:rewrite page="/preference/save_calendar_settings.do" />?buttonpress="+button.value;
    document.prefCalendarForm.submit();
  }
</script>
<html:form action="/preference/save_calendar_settings.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.calendarsettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.preference.pagerefresh"/>:</td>
    <td class="contentCell">
      <%  pageContext.setAttribute("minute_second", minute_second, PageContext.PAGE_SCOPE); %>
      <html:select property="minutes" styleId="minutes" styleClass="inputBox">
        <html:options collection="minute_second" property="id" labelProperty="name"/>
      </html:select>
      <bean:message key="label.preference.minutes"/>
      <html:select property="seconds" styleId="seconds" styleClass="inputBox">
        <html:options collection="minute_second" property="id" labelProperty="name"/>
      </html:select>
      <bean:message key="label.preference.seconds"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.preference.defaultview"/>:</td>
    <td class="contentCell">
      <html:radio property="radio" value="DAILY" /><bean:message key="label.preference.daily"/><br/>
			<html:radio property="radio" value="WEEKLY" /><bean:message key="label.preference.weekly"/><br/>
			<html:radio property="radio" value="WEEKLYCOLUMNS" /><bean:message key="label.preference.weeklycolumns"/><br/>
      <html:radio property="radio" value="MONTHLY" /><bean:message key="label.preference.monthly"/><br/>
			<html:radio property="radio" value="YEARLY" /><bean:message key="label.preference.yearly"/><br/>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="Save" styleId="Save" styleClass="normalButton" onclick="saveSettings(this);">
        <bean:message key="label.save"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>