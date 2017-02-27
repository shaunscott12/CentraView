<%--
 * $RCSfile: calendar_js.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<% response.setContentType("text/javascript"); %>
<!--
function ScheduleActivity(year,month,day,stime,etime)
{
  c_openWindow('/activities/new_activity.do?subScope=All&DAY='+day+'&MONTH='+month+'&YEAR='+year+'&STIME='+stime+'&ETIME='+etime, '', 750, 525, '');
}
function changeDate()
{
  var selectDate = document.calendarForm.selectDate.value;
  if (selectDate == "MM/DD/YYYY"){
    alert ("<bean:message key='label.alert.enterdate'/>");
    return false;
  }

  var selectDates = selectDate.split("/");
  if(selectDates != null && selectDates.length == 3){
    month = selectDates[0];
    day = selectDates[1];
    year = selectDates[2];

    if (year.length != 4){
      alert ("<bean:message key='label.alert.yearformat'/>");
      return false;
    }

    monthName = c_getMonthName(month);
    var searchDate = "<html:rewrite page="/calendar.do" />?Type=DAILY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
    document.calendarForm.action = searchDate;
    document.calendarForm.submit();
  }
  else{
    alert ("<bean:message key='label.alert.dateformat'/>'");
    return false;
  }
}

function openCalendar()
{
  c_openWindow('/calendar/popup_calendar.do','', 250, 210, 'scrollbars=no,resizable=no');
}

function goToPrevMonth(currentYear, currentMonth)
{
  monthName = c_getMonthName(currentMonth);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=MONTHLY&selectedMonthName="+monthName+"&selectedYear="+currentYear;
  document.calendarForm.action = searchDate;
  document.calendarForm.submit();
}
function goToNextMonth(currentYear, currentMonth)
{
  monthName = c_getMonthName(currentMonth);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=MONTHLY&selectedMonthName="+monthName+"&selectedYear="+currentYear;
  document.calendarForm.action = searchDate;
  document.calendarForm.submit();
}
function selectDate(year, month, day)
{
  monthName = c_getMonthName(month);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=DAILY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
  document.calendarForm.action = searchDate;
  document.calendarForm.submit();
}
function mini_selectDate(year, month, day)
{
  monthName = c_getMonthName(month);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=DAILY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
  document.calendarForm.action = searchDate;
  document.calendarForm.submit();
}
function goToWeeklyView(year, month, day)
{
  monthName = c_getMonthName(month);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=WEEKLY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
  document.calendarForm.action = searchDate;
  document.calendarForm.submit();
}
//-->