<%--
 * $RCSfile: popup_calendar.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:42 $ - $Author: mking_cv $
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

<html:form action="/calendar/popup_calendar.do">
<table border="0" cellpadding="0" cellspacing="1" width="100%" >
  <tr>  
	  <td align="center">
	      <jsp:include page="/jsp/tiles/calendar/mini_calendar.jsp" />
	  </td>
  </tr>	  
</table>
</html:form>
<script>
<!--
function goToPrevMonth(currentYear, currentMonth)
{
  monthName = c_getMonthName(currentMonth);
  var searchDate = "<html:rewrite page="/calendar/popup_calendar.do" />?selectedMonthName="+monthName+"&selectedYear="+currentYear;
  document.calendarForm.action = searchDate; 
  document.calendarForm.submit();
}
function goToNextMonth(currentYear, currentMonth)
{
  monthName = c_getMonthName(currentMonth);
  var searchDate = "<html:rewrite page="/calendar/popup_calendar.do" />?selectedMonthName="+monthName+"&selectedYear="+currentYear;
  document.calendarForm.action = searchDate; 
  document.calendarForm.submit();
}
function selectDate(year, month, day)
{
  monthName = c_getMonthName(month);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=DAILY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
  window.opener.location.href = searchDate; 
  window.close();
}
function mini_selectDate(year, month, day)
{
  monthName = c_getMonthName(month);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=DAILY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
  window.opener.location.href = searchDate; 
  window.close();
}
function goToWeeklyView(year, month, day)
{
  monthName = c_getMonthName(month);
  var searchDate = "<html:rewrite page="/calendar.do" />?Type=WEEKLY&selectedDay="+day+"&selectedMonthName="+monthName+"&selectedYear="+year;
  window.opener.location.href = searchDate; 
  window.close();
}
-->
</script>