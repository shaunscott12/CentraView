<%--
 * $RCSfile: home_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:45 $ - $Author: mcallist $
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

  for (int i = 0; i < 60; i++) {
    ddObj = new DDNameValue(i, String.valueOf(i));
    minute_second.add(ddObj);
  }
%>
<script language="javascript">
  function addPortlet(leftList, rightList)
  {
    var currentIndex = leftList.selectedIndex;
    var len = document.getElementById('portletsRight').options.length;
    var currValue = document.getElementById('portletsLeft').options[currentIndex].value;
    var currText  = document.getElementById('portletsLeft').options[currentIndex].text;
    var selectedItem = new Option(currText, currValue, true, true);
    document.getElementById('portletsRight').options[len] = selectedItem;
    document.getElementById('portletsLeft').options[currentIndex] = null;
  }

  function removePortlet(leftList, rightList)
  {
    var currentIndex = rightList.selectedIndex;
    var len = document.getElementById('portletsLeft').options.length;
    var currValue = document.getElementById('portletsRight').options[currentIndex].value;
    var currText  = document.getElementById('portletsRight').options[currentIndex].text;
    var selectedItem = new Option(currText, currValue, true, true);
    document.getElementById('portletsLeft').options[len] = selectedItem;
    document.getElementById('portletsRight').options[currentIndex] = null;
  }

  function gotosave(param)
  {
    var viewportletsLeft = document.getElementById('portletsLeft').options.length;
    var viewportletsRight = document.getElementById('portletsRight').options.length;

    for (var i = 0; i < viewportletsLeft; i++) {
      document.getElementById('portletsLeft').options[i].selected = true;
    }

    for (var i = 0; i < viewportletsRight; i++) {
      document.getElementById('portletsRight').options[i].selected = true;
    }

    if (document.getElementById('minutes').value != null || document.getElementById('minutes').value != "") {
      document.getElementById('minutesValue').value = "REFRESH_MINUTES";
    }

    if (document.getElementById('seconds').value != null || document.getElementById('seconds').value != "") {
      document.getElementById('secondsValue').value = "REFRESH_SECONDS";
    }

    if (document.getElementById('portletsLeft').value != null || document.getElementById('portletsLeft').value != "") {
      document.getElementById('portletsLeftValue').value = "SELECTED";
    } else {
      document.getElementById('portletsLeftValue').value = "UNSELECTED";
    }

    window.document.homesettingsForm.action = "<html:rewrite page="/preference/save_home_settings.do" />?buttonpress=" + param;
    window.document.homesettingsForm.submit();
  }
</script>
<html:form action="/preference/home_settings.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.homepagesettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td class="labelCell">
      <span class="boldText"><bean:message key="label.preference.setrefreshrate"/>.</span>
    </td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.preference.refreshrate"/>:</td>
    <td class="contentCell">
      <% pageContext.setAttribute("minute_second", minute_second, PageContext.PAGE_SCOPE); %>
      <html:select property="minutes" styleId="minutes" styleClass="inputBox">
        <html:options collection="minute_second" property="id" labelProperty="name"/>
      </html:select>
      <html:hidden property="minutesValue" styleId="minutesValue" />
      <bean:message key="label.preference.minutes"/>

      <html:select property="seconds" styleId="seconds" styleClass="inputBox">
        <html:options collection="minute_second" property="id" labelProperty="name"/>
      </html:select>
      <html:hidden property="secondsValue" styleId="secondsValue" />
      <bean:message key="label.preference.seconds"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.preference.portlets"/>:</td>
    <td class="contentCell">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td>
            <span class="boldText"><bean:message key="label.preference.availableportlets"/></span><br/>
            <%
              Vector vecleft = new Vector();
              if (request.getAttribute("vecleft") != null) {
                vecleft = (Vector)request.getAttribute("vecleft");
              }
              pageContext.setAttribute("vecleft", vecleft, PageContext.PAGE_SCOPE);
            %>
            <html:select property="portletsLeft" styleId="portletsLeft" styleClass="inputBox" size="12" style="width:15em;" multiple="true">
              <html:options collection="vecleft" property="strid" labelProperty="name"/>
            </html:select>
            <html:hidden property="portletsLeftValue" styleId="portletsLeftValue" />
          </td>
          <td>
            <div align="center">
              <app:cvbutton property="add" styleClass="normalButton" styleId="add" onclick="addPortlet(portletsLeft, portletsRight);" style="width:5em;">
                <bean:message key="label.add"/> &raquo;
              </app:cvbutton>
              <br/><br/>
              <app:cvbutton property="remove" styleClass="normalButton" styleId="remove" onclick="removePortlet(portletsLeft, portletsRight);" style="width:5em;">
                &laquo; <bean:message key="label.remove"/>
              </app:cvbutton>
            </div>
          </td>
          <td>
            <span class="boldText"><bean:message key="label.preference.showportlets"/></span><br/>
            <%
              Vector vecright = new Vector();
              if (request.getAttribute("vecright") != null) {
                vecright = (Vector)request.getAttribute("vecright");
              }
              pageContext.setAttribute("vecright", vecright, PageContext.PAGE_SCOPE);
            %>
            <html:select property="portletsRight" styleId="portletsRight" styleClass="inputBox" size="12" style="width:15em;" multiple="true">
              <html:options collection="vecright" property="strid" labelProperty="name"/>
            </html:select>
            <html:hidden property="portletsRightValue" styleId="portletsRightValue" />
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="gotosave('save')">
        <bean:message key="label.save"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>