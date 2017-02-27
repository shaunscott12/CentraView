<%--
 * $RCSfile: select_date_time.jsp,v $    $Revision: 1.5 $  $Date: 2005/10/24 21:11:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<script language="Javascript">
  function goToPrevMonth(currentYear, currentMonth)
  {
    document.forms.dateTimeSelectForm.year.value = currentYear;
    document.forms.dateTimeSelectForm.month.value = currentMonth;
    document.forms.dateTimeSelectForm.submit();
  }
  function goToNextMonth(currentYear, currentMonth)
  {
    document.forms.dateTimeSelectForm.year.value = currentYear;
    document.forms.dateTimeSelectForm.month.value = currentMonth;
    document.forms.dateTimeSelectForm.submit();
  }
  function mini_selectDate(dateString)
  {
    var startOrEnd = c_getSelectedRadioValue(document.forms.dateTimeSelectForm.startOrEnd);
    if (startOrEnd == "start") {
      document.forms.dateTimeSelectForm.startDate.value = dateString;
      var simpleMode = document.getElementById('simpleMode').value
      if (simpleMode == 'false') {
        document.forms.dateTimeSelectForm.endDate.value = dateString;
      }
    } else if(startOrEnd == "end") {
      document.forms.dateTimeSelectForm.endDate.value = dateString;
    } else {
      alert("<bean:message key='label.alert.checkstartorenddate'/>.");
    }
  }
  function updateTime()
  {
    var startOrEnd = c_getSelectedRadioValue(document.forms.dateTimeSelectForm.startOrEndTime);
    var hour = document.forms.dateTimeSelectForm.hours.value;
    var minute = document.forms.dateTimeSelectForm.minutes.value;
    var amPm = c_getSelectedRadioValue(document.forms.dateTimeSelectForm.amPm);
    
    if (amPm == null) { amPm = "AM"; }
    var time = hour + ":" + minute + " " + amPm;
    var endTime = addOneHour(hour, minute, amPm);

    if (startOrEnd == "start") {
      document.forms.dateTimeSelectForm.startTime.value = time;
      document.forms.dateTimeSelectForm.endTime.value = endTime;
    } else if(startOrEnd == "end") {
      document.forms.dateTimeSelectForm.endTime.value = time;
    } else {
      alert("<bean:message key='label.alert.checkstartorendtime'/>.");
    }
  }
  function addOneHour(hour, minute, amPm)
  {
    var endHour = eval(hour);
    if (endHour == 12) {
      endHour = 1;
    } else {
      if (endHour == 11) {
        amPm = toggleAmPm(amPm);
      }
      endHour = endHour + 1;
    }
    var endTime = endHour + ":" + minute + " " + amPm;
    return(endTime);
  }
  function toggleAmPm(amPm)
  {
    if (amPm == "AM"){
      return("PM");
    }else{
      return("AM");
    }
  }
  function finish()
  {
    <c:if test="${dateTimeSelectForm.map.dateTimeType == 1}">
    document.forms.dateTimeSelectForm.endDate.value = "";
    document.forms.dateTimeSelectForm.endTime.value = "";
    document.forms.dateTimeSelectForm.startTime.value = "";
    </c:if>
    <c:if test="${dateTimeSelectForm.map.dateTimeType == 2}">
    document.forms.dateTimeSelectForm.endDate.value = "";
    document.forms.dateTimeSelectForm.endTime.value = "";
    </c:if>
    <c:if test="${dateTimeSelectForm.map.dateTimeType == 3}">
    document.forms.dateTimeSelectForm.endTime.value = "";
    document.forms.dateTimeSelectForm.startTime.value = "";
    </c:if> 
    <c:if test="${dateTimeSelectForm.map.dateTimeType == 4}">
    document.forms.dateTimeSelectForm.endDate.value = "";
    </c:if>       
    var startDate = document.forms.dateTimeSelectForm.startDate.value;
    var endDate = document.forms.dateTimeSelectForm.endDate.value;
    var startTime = document.forms.dateTimeSelectForm.startTime.value;
    var endTime = document.forms.dateTimeSelectForm.endTime.value;
    if (opener) {
      var optionalFlag = false;
      <c:if test="${param.optionalFlag != null && param.optionalFlag != ''}">
      optionalFlag = "<c:out value="${param.optionalFlag}" default="false" />";
      </c:if>
      if (! opener.setDateTimeFromPopup(startDate, endDate, startTime, endTime, optionalFlag)){
        alert("<bean:message key='label.alert.couldnotsetdatestimewindow1'/>");
      }
    }else{
      alert("<bean:message key='label.alert.couldnotsetdatestimewindow2'/>");
    }
    window.close();
  }
  function clearTimeForms()
  {
    document.forms.dateTimeSelectForm.startTime.value = "";
    document.forms.dateTimeSelectForm.endTime.value = "";
  }
  function clearDateForms()
  {
    document.forms.dateTimeSelectForm.startDate.value = "";
    document.forms.dateTimeSelectForm.endDate.value = "";
  }
  function populateTimeFields()
  {
    var startTime = document.forms.dateTimeSelectForm.startTime.value;
    var endTime = document.forms.dateTimeSelectForm.endTime.value;
    if (startTime != null && startTime.length > 0)
    {
      var arrTimeParts = startTime.split(":");
      var hour = arrTimeParts[0];
      var arrMinAmPm = arrTimeParts[1].split(" ");
      var minute = arrMinAmPm[0];
      var amPm = arrMinAmPm[1];

      if (hour == "12"){
        document.forms.dateTimeSelectForm.hours.selectedIndex = 0;
      }else{
        document.forms.dateTimeSelectForm.hours.selectedIndex = eval(hour);
      }

      if (minute == "00"){
        document.forms.dateTimeSelectForm.minutes.selectedIndex = 0;
      }else if (minute == "15"){
        document.forms.dateTimeSelectForm.minutes.selectedIndex = 1;
      }else if (minute == "30"){
        document.forms.dateTimeSelectForm.minutes.selectedIndex = 2;
      }else if (minute == "45"){
        document.forms.dateTimeSelectForm.minutes.selectedIndex = 3;
      }


      if (amPm == "AM"){
        document.forms.dateTimeSelectForm.amPm.selectedIndex = 0;
      }else{
        document.forms.dateTimeSelectForm.amPm.selectedIndex = 1;
      }
      if (endTime == null || endTime.length <= 0)
      {
        updateTime();
      }
    }
  }
</script>
<html:form action="/calendar/select_date_time.do">
<html:hidden property="month" />
<html:hidden property="year" />
<html:hidden property="dateTimeType" />
<html:hidden property="simpleMode" styleId="simpleMode" />
<c:set var="date" value="Start Date:"/>
<c:set var="time" value="Start Time:"/>
<c:if test="${dateTimeSelectForm.map.dateTimeType == 1}">
  <c:set var="date" value="Date:"/>
  <html:hidden property="endDate" value="" />
  <html:hidden property="startTime" value="" />
  <html:hidden property="endTime" value="" />
</c:if>
<c:if test="${dateTimeSelectForm.map.dateTimeType == 2}">
  <c:set var="date" value="Date:"/>
  <c:set var="time" value="Time:"/>
  <html:hidden property="endDate" value="" />
  <html:hidden property="endTime" value="" />
</c:if>
<c:if test="${dateTimeSelectForm.map.dateTimeType == 3}">
  <html:hidden property="endTime" value="" />
  <html:hidden property="startTime" value="" />
</c:if>
<c:if test="${dateTimeSelectForm.map.dateTimeType == 4}">
  <c:set var="date" value="Date:"/>
  <html:hidden property="endDate" value="" />
</c:if>
<c:if test="${dateTimeSelectForm.map.dateTimeType == 6}">
  <html:hidden property="startDate" value="" />
  <html:hidden property="endDate" value="" />
</c:if>
<table border="0" cellpadding="2" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td align="center">
      <c:if test="${dateTimeSelectForm.map.dateTimeType != 6}">
        <jsp:include page="/jsp/tiles/calendar/mini_calendar.jsp" />
      </c:if>
    </td>
  </tr>
  <tr>
    <td class="labelCell" align="center">
      <table border="0" cellpadding="2" cellspacing="0">
        <c:if test="${dateTimeSelectForm.map.dateTimeType != 6 && (dateTimeSelectForm.map.dateTimeType >= 3 || dateTimeSelectForm.map.dateTimeType == 1 || dateTimeSelectForm.map.dateTimeType == 2)}">
        <tr>
          <td class="labelCell"><input type="radio" name="startOrEnd" value="start" checked="checked"/></td>
          <%-- If the request parameter "startDateTitle" is passed, then that value is used here. Else, the logic above dictates the label --%>
          <td class="labelCell"><c:out value="${param.startDateTitle}" default="${date}"/>: </td>
          <td class="labelCell"><html:text property="startDate" styleClass="inputBox" size="10" /></td>
        </tr>
        </c:if>
        <c:if test="${dateTimeSelectForm.map.dateTimeType == 5 || dateTimeSelectForm.map.dateTimeType == 3}">
        <tr>
          <td class="labelCell"><input type="radio" name="startOrEnd" value="end" /></td>
          <%-- If the request parameter "endDateTitle" is passed, then that value is used here. Else, "End Date" is the label --%>
          <td class="labelCell"><c:out value="${param.endDateTitle}" default="End Date" />: </td>
          <td class="labelCell"><html:text property="endDate" styleClass="inputBox" size="10" /></td>
        </tr>
        </c:if>
        <c:if test="${dateTimeSelectForm.map.dateTimeType != 6}">
        <tr>
          <td class="labelCell" colspan="3" align="center">
            <input type="button" name="clearDates" value="<bean:message key='label.pages.calendar.cleardates'/>" class="normalButton" onclick="clearDateForms();">
          </td>
        </tr>
        </c:if>
      </table>
    </td>
  </tr>
  <tr>
    <td class="labelCell" align="center"><hr width="85%" /></td>
  </tr>
  <c:if test="${dateTimeSelectForm.map.dateTimeType == 2 || dateTimeSelectForm.map.dateTimeType >= 4 }">
    <tr>
      <td class="labelCell" align="center">
        <table border="0" cellpadding="1" cellspacing="0">
          <tr>
            <td>
              <table border="0" cellpadding="0" cellspacing="0">
                <c:if test="${dateTimeSelectForm.map.dateTimeType >= 4 || dateTimeSelectForm.map.dateTimeType == 2 }">
                <tr>
                  <td class="labelCell"><input type="radio" name="startOrEndTime" value="start" checked="checked" /></td>
                  <td class="labelCell"><c:out value="${time}"/> </td>
                  <td class="labelCell"><html:text property="startTime" styleClass="inputBox" size="9" /></td>
                  <td class="labelCell" ></td>
                </tr>
                </c:if>
                <c:if test="${ dateTimeSelectForm.map.dateTimeType >= 4 }">
                <tr>
                  <td class="labelCell" valign="top"><input type="radio" name="startOrEndTime" value="end" /></td>
                  <td class="labelCell" valign="top"><bean:message key='label.pages.calendar.endtime'/>: </td>
                  <td class="labelCell" valign="top"><html:text property="endTime" styleClass="inputBox" size="9"/></td>
                  <td class="labelCell" ></td>
                </tr>
                </c:if>                      
                <tr>
                  <td class="labelCell" ></td>
                  <td class="labelCell" colspan="2" align="center"><input type="button" name="clearTime" value="<bean:message key='label.pages.calendar.cleartime'/>" class="normalButton" onclick="clearTimeForms();"></td>
                  <td class="labelCell" ></td>
                  <td class="labelCell" ></td>
                </tr>                                  
              </table>
            </td>
            <td class="labelCell" rowspan="3">
              <select name="hours" onchange="updateTime();" class="inputBox" size="6">
                <option value="12" selected>12</option>
                <option value="1"> 1</option>
                <option value="2"> 2</option>
                <option value="3"> 3</option>
                <option value="4"> 4</option>
                <option value="5"> 5</option>
                <option value="6"> 6</option>
                <option value="7"> 7</option>
                <option value="8"> 8</option>
                <option value="9"> 9</option>
                <option value="10">10</option>
                <option value="11">11</option>
              </select>
            </td>
            <td class="labelCell" rowspan="3">
              <select name="minutes" onchange="updateTime();" class="inputBox" size="6">
                <option value="00" selected>00</option>
                <option value="15">15</option>
                <option value="30">30</option>
                <option value="45">45</option>
              </select>
            </td>
            <td class="labelCell" rowspan="2" valign="top">
              <select name="amPm" onchange="updateTime();" class="inputBox" size="3">
                <option value="AM" selected>AM</option>
                <option value="PM">PM</option>
              </select>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td class="labelCell" align="center"><hr width="85%" /></td>
    </tr>
  </c:if>
  <tr>
    <td class="labelCell" align="center">
      <br/>
      <html:button property="closeWindow" styleClass="normalButton" onclick="finish();"><bean:message key='label.pages.calendar.save'/></html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="window.close();"><bean:message key='label.pages.calendar.cancel'/></html:button>
      <br/>
    </td>
  </tr>
</table>
</html:form>
