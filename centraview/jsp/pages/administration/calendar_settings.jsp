<%--
 * $RCSfile: calendar_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="org.apache.struts.action.*, java.util.*" %>
<%
  String setting = request.getParameter("setting").toString();
  String submodule = request.getParameter("sourcefor").toString();
%>
<script language="javascript">
  function showTimeSelect(timeSelectLayerName, timeInputLayerName, toggleValue ,timeType)
  {
    var timeSelectLayer = document.getElementById(timeSelectLayerName);
    var timeInputLayer = document.getElementById(timeInputLayerName);

    if (toggleValue == 0) {
      timeSelectLayer.style.display  = "inline";
      timeInputLayer.style.display = "none";
    } else {
      timeSelectLayer.style.display  = "none";
      if (timeType == "timestart") {
        document.getElementById('startTime').readonly=false;
        document.getElementById('startTime').value = document.getElementById('selStartTime').options[document.getElementById('selStartTime').selectedIndex].value;
        document.getElementById('startTime').readonly=true;
      } else {
        document.getElementById('endTime').readonly=false;
        document.getElementById('endTime').value = document.getElementById('selEndTime').options[document.getElementById('selEndTime').selectedIndex].value;
        document.getElementById('endTime').readonly=true;
      }
      timeInputLayer.style.display = "inline";
    }
  }

  function gotoSave(param)
  {
    var sun,mon,tues,wed,thurs,fri,sat ='';
    var temp= '';
    if (document.getElementById('sun').checked == true && document.getElementById('sun').checked != 'undifined') {
      sun = document.getElementById('sun').value;
      temp = temp + sun+",";
    }
    if (document.getElementById('mon').checked == true && document.getElementById('mon').checked != 'undifined') {
      mon = document.getElementById('mon').value;
      temp = temp + mon+",";
    }
    if (document.getElementById('tues').checked == true && document.getElementById('tues').checked != 'undifined') {
      tues = document.getElementById('tues').value;
      temp = temp + tues+",";
    }
    if (document.getElementById('wed').checked == true && document.getElementById('wed').checked != 'undifined') {
      wed = document.getElementById('wed').value;
      temp = temp + wed+",";
    }
    if (document.getElementById('thurs').checked == true && document.getElementById('thurs').checked != 'undifined') {
      thurs = document.getElementById('thurs').value;
      temp = temp + thurs+",";
    }
    if (document.getElementById('fri').checked == true && document.getElementById('fri').checked != 'undifined') {
      fri = document.getElementById('fri').value;
      temp = temp + fri+",";
    }
    if (document.getElementById('sat').checked == true && document.getElementById('sat').checked != 'undifined') {
      sat = document.getElementById('sat').value;
      temp = temp + sat+",";
    }
    document.getElementById('workingdays').value = temp;
    document.masterdataform.action = "<html:rewrite page="/administration/save_calendar_settings.do"/>?buttonpress="+param;
    document.masterdataform.submit();
  }


</script>
<html:form action="/administration/calendar_settings">
<input type="hidden" name="setting" id="setting" value="<%=setting%>">
<input type="hidden" name="submodule" id="submodule" value="<%=submodule%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.calendermodulesettingsworkinghours"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <bean:message key="label.administration.selectstartendtime"/>.
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.starttime"/>:</td>
    <td class="contentCell">
      <div name="startTimeInputDiv" id="startTimeInputDiv" style="display:inline;">
        <html:text property="startTime" styleId="startTime" styleClass="inputBox" style="width:7em;" readonly="true"/>
      </div>
      <div name="startTimeSelectDiv" id="startTimeSelectDiv" style="display:none;">
        <html:select property="selStartTime" styleId="selStartTime" onchange="showTimeSelect('startTimeSelectDiv', 'startTimeInputDiv',1,'timestart')" styleClass="inputBox" style="width:8em;">
          <html:option value="1:00 AM" >&nbsp;1:00 </html:option>
          <html:option value="2:00 AM" >&nbsp;2:00 </html:option>
          <html:option value="3:00 AM" >&nbsp;3:00 </html:option>
          <html:option value="4:00 AM" >&nbsp;4:00 </html:option>
          <html:option value="5:00 AM" >&nbsp;5:00 </html:option>
          <html:option value="6:00 AM" >&nbsp;6:00 </html:option>
          <html:option value="7:00 AM" >&nbsp;7:00 </html:option>
          <html:option value="8:00 AM" >&nbsp;8:00 </html:option>
          <html:option value="9:00 AM" >&nbsp;9:00 </html:option>
          <html:option value="10:00 AM" >10:00 </html:option>
          <html:option value="11:00 AM" >11:00 </html:option>
          <html:option value="12:00 PM" >12:00 </html:option>
          <html:option value="1:00 PM" >&nbsp;13:00 </html:option>
          <html:option value="2:00 PM" >&nbsp;14:00 </html:option>
          <html:option value="3:00 PM" >&nbsp;15:00 </html:option>
          <html:option value="4:00 PM" >&nbsp;16:00 </html:option>
          <html:option value="5:00 PM" >&nbsp;17:00 </html:option>
          <html:option value="6:00 PM" >&nbsp;18:00 </html:option>
          <html:option value="7:00 PM" >&nbsp;19:00 </html:option>
          <html:option value="8:00 PM" >&nbsp;20:00 </html:option>
          <html:option value="9:00 PM" >&nbsp;21:00 </html:option>
          <html:option value="10:00 PM" >22:00 </html:option>
          <html:option value="11:00 PM" >23:00 </html:option>
          <html:option value="12:00 AM" >00:00 </html:option>
        </html:select>
      </div>
      <html:img page="/images/icon_reminder.gif" width="17" height="17" align="abstop" style="cursor:pointer;" onclick="showTimeSelect('startTimeSelectDiv', 'startTimeInputDiv', 0);" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.endtime"/>:</td>
    <td class="contentCell">
      <div name="endTimeInputDiv" id="endTimeInputDiv" style="display:inline;">
        <html:text property="endTime" styleId="endTime" styleClass="inputBox" style="width:7em;" readonly="true"/>
      </div>
      <div name="endTimeSelectDiv" id="endTimeSelectDiv" style="display:none;">
        <html:select property="selEndTime" styleId="selEndTime" onchange="showTimeSelect('endTimeSelectDiv', 'endTimeInputDiv',1,'timeend')" styleClass="inputBox" style="width:8em;">
          <html:option value="1:00 AM" >&nbsp;1:00 </html:option>
          <html:option value="2:00 AM" >&nbsp;2:00 </html:option>
          <html:option value="3:00 AM" >&nbsp;3:00 </html:option>
          <html:option value="4:00 AM" >&nbsp;4:00 </html:option>
          <html:option value="5:00 AM" >&nbsp;5:00 </html:option>
          <html:option value="6:00 AM" >&nbsp;6:00 </html:option>
          <html:option value="7:00 AM" >&nbsp;7:00 </html:option>
          <html:option value="8:00 AM" >&nbsp;8:00 </html:option>
          <html:option value="9:00 AM" >&nbsp;9:00 </html:option>
          <html:option value="10:00 AM" >10:00 </html:option>
          <html:option value="11:00 AM" >11:00 </html:option>
          <html:option value="12:00 PM" >12:00 </html:option>
          <html:option value="1:00 PM" >&nbsp;13:00</html:option>
          <html:option value="2:00 PM" >&nbsp;14:00 </html:option>
          <html:option value="3:00 PM" >&nbsp;15:00 </html:option>
          <html:option value="4:00 PM" >&nbsp;16:00 </html:option>
          <html:option value="5:00 PM" >&nbsp;17:00 </html:option>
          <html:option value="6:00 PM" >&nbsp;18:00 </html:option>
          <html:option value="7:00 PM" >&nbsp;19:00 </html:option>
          <html:option value="8:00 PM" >&nbsp;20:00 </html:option>
          <html:option value="9:00 PM" >&nbsp;21:00 </html:option>
          <html:option value="10:00 PM" >22:00 </html:option>
          <html:option value="11:00 PM" >23:00 </html:option>
          <html:option value="12:00 AM" >00:00 </html:option>
        </html:select>
      </div>
      <html:img page="/images/icon_reminder.gif" width="17" height="17" align="abstop" style="cursor:pointer;" onclick="showTimeSelect('endTimeSelectDiv', 'endTimeInputDiv', 0);" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.workingdays"/>:</td>
    <td class="contentCell">
      <input type="checkbox" name="sun" id="sun" value="sun"> <bean:message key="label.administration.sun"/>
      <input name="mon" id="mon" type="checkbox" value="mon"> <bean:message key="label.administration.mon"/>
      <input name="tues" id="tues" type="checkbox" value="tues"> <bean:message key="label.administration.tues"/>
      <input name="wed" id="wed" type="checkbox" value="wed"> <bean:message key="label.administration.wed"/>
      <input name="thurs" id="thurs" type="checkbox" value="thurs"> <bean:message key="label.administration.thurs"/>
      <input name="fri" id="fri" type="checkbox" value="fri"> <bean:message key="label.administration.fri"/>
      <input type="checkbox" name="sat" id="sat" value="sat"> <bean:message key="label.administration.sat"/></td>
      <html:hidden property="workingdays" styleId="workingdays"/>
      <script language="javascript">
      <%
        DynaActionForm dynaform = (DynaActionForm)request.getAttribute("dynaform");
        String workdays = dynaform.get("workingdays").toString();
        StringTokenizer st = new StringTokenizer(workdays,",");
        while (st.hasMoreTokens()) {
          String temp = st.nextToken();
          %>
          document.getElementById('<%=temp%>').checked = true;
          <%
        }
      %>
      </script>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="save" id="save" type="button" class="normalButton" value="<bean:message key='label.value.save'/>" onclick="gotoSave('save')"/>
      <input name="saveandclose" id="saveandclose" type="button" class="normalButton" value="<bean:message key='label.value.saveandclose'/>" onclick="gotoSave('saveandclose')" />
      <input name="cancel" id="cancel" type="button" class="normalButton" value="<bean:message key='label.value.cancel'/>" onclick="c_goTo('/administration/view_module_settings.do?typeofsubmodule=Calendar');" />
    </td>
  </tr>
</table>
</html:form>