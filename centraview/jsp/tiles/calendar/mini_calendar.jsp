<%--
 * $RCSfile: mini_calendar.jsp,v $    $Revision: 1.5 $  $Date: 2005/10/24 21:11:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.GregorianCalendar,java.util.Calendar,java.text.DateFormat,java.util.Locale" %>
<%
  // In order to use this JSP, it must be called from a Handler
  // which has set two GregorianCalendar objects as request
  // attributes. The first should be named "selectedDate", and
  // should represent the year and month which the user has
  // selected to view. The second should be named "currentDate"
  // and should represent the current system time. The handler
  // should also accept the request paramters "year" and "month"
  // which will change the value of the "selectedDate" object.

  // This JSP file *MUST* be included by a parent JSP file. The
  // parent JSP file should have three Javascript functions defined:
  // "goToPrevMonth()", which should tell the handler to change
  //       the "selectedDate" parameter to the previous month.
  // "goToNextMonth()", which should tell the handler to change
  //       the "selectDate" parameter to the next month.
  // Please see com.centraview.calendar.SelectDateTime and
  // /jsp/pages/calendar/select_date_time.jsp for examples of how to
  // use this utility.
  GregorianCalendar selectedDate = (GregorianCalendar)request.getAttribute("selectedDate");
  GregorianCalendar currentDate = (GregorianCalendar)request.getAttribute("currentDate");

  int month = selectedDate.get(GregorianCalendar.MONTH);    // selected month of year
  int year = selectedDate.get(GregorianCalendar.YEAR);      // selected year
  int no_days = selectedDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);    // number of days in selected month
  int no_weeks = selectedDate.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);  // number of weeks in selected month

  int currentMonth = currentDate.get(GregorianCalendar.MONTH);    // selected month of year
  int currentYear = currentDate.get(GregorianCalendar.YEAR);      // selected year
  int currentDay = currentDate.get(GregorianCalendar.DATE);      // selected year

  int day_of_month = 32;    // "There aren't 32 days in any month!?!" (keep reading, slacker...)
  if (currentDate.get(GregorianCalendar.YEAR) == year && currentDate.get(GregorianCalendar.MONTH) == month)
  {
    // if the selected month is the current month, then
    // highlight today, else the value will be 32, and
    // nothing will be highlighted
    day_of_month = currentDate.get(GregorianCalendar.DAY_OF_MONTH);    // numeric day of the month
  }

  int dayOfWeek = currentDate.get(GregorianCalendar.DAY_OF_WEEK);
  // Subtracting it with 2 coz in the Calendar Starting Day is Monday.
  int offsetWeek = dayOfWeek - 2;
  if (offsetWeek < 0)
  {
    int daysInWeek = 7;
    // if we are before the current day of the week, we need to fall all the way
    // back to the previous beginning of the week.  calculating our offset in this
    // way will give us the right num of days to the previous start.
    // this makes it easy to just add it to the currentDate.
    offsetWeek = daysInWeek + offsetWeek;
  }
  currentDate.set(GregorianCalendar.DATE, currentDay - offsetWeek);
  int startDateOfWeek = currentDate.get(GregorianCalendar.DATE);
  currentDate.add(GregorianCalendar.DATE, 6);
  int endDateOfWeek = currentDate.get(GregorianCalendar.DATE);

  GregorianCalendar first = new GregorianCalendar(year, month, 1);
  int offset = first.get(GregorianCalendar.DAY_OF_WEEK);    // day of week for the first day of the selected month

  int day_count = 1;    // intialize counters
  int curr_day = 1;

  DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, request.getLocale());
  String dateHeader = df.format(selectedDate.getTime());

  Calendar previousCalendarWeek = Calendar.getInstance();
  previousCalendarWeek.set(year, month,no_days);
  previousCalendarWeek.add(Calendar.MONTH, -1);
  int prevmonth = previousCalendarWeek.get(Calendar.MONTH);
  int prevyear = previousCalendarWeek.get(Calendar.YEAR);

  Calendar nextCalendarWeek = Calendar.getInstance();
  nextCalendarWeek.set(year, month,no_days);
  nextCalendarWeek.add(Calendar.MONTH, +1);
  int nextmonth = nextCalendarWeek.get(Calendar.MONTH);
  int nextyear = nextCalendarWeek.get(Calendar.YEAR);
%>

<table border="0" cellpadding="0" cellspacing="0" class="miniCalHeader">
  <tr>
    <td>
      <a class="plainLink" onclick="goToPrevMonth(<%=prevyear%>, <%=(prevmonth+1)%>);"><strong>&laquo;&minus;</strong></a>&nbsp;
      <span class="labelBold"><%=dateHeader%></span>&nbsp;
      <a class="plainLink" onclick="goToNextMonth(<%=nextyear%>, <%=(nextmonth+1)%>);"><strong> &minus;&raquo;</strong></a>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" class="miniCalContainer">
  <tr>
    <td>
      <table border="0" cellpadding="0" cellspacing="1" class="miniCalendar">
        <tr>
          <c:if test="${requestScope.showWeeklyColumn}">
            <td class="miniCalHeadNav">&nbsp;</td>
          </c:if>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.su"/></td>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.mo"/></td>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.tu"/></td>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.we"/></td>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.th"/></td>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.fr"/></td>
          <td class="miniCalHeader"><bean:message key="label.tiles.calendar.sa"/></td>
        </tr>
        <%
          for (int i=1; i<=no_weeks; i++)
          {
            // loop through the weeks of the month
            %>
            <tr>
            <c:if test="${requestScope.showWeeklyColumn}">
              <td class="miniCalNav">
                <a class="plainLink" onclick="goToWeeklyView(<%=year%>, <%=(month+1)%>, <%=curr_day%>);">
                  &raquo;
                </a>
              </td>
            </c:if>
            <%
            for (int j=1; j<=7; j++)
            {
              // loop through 7 days of a week
              Calendar day = new GregorianCalendar(year, month, curr_day);
              if (curr_day <= no_days)
              {
                if (day_count < offset)
                {
                  // This is a day in the PREVIOUS month (a blank cell)
                  %>
                  <td class="miniCalBlank" >
                    &nbsp;
                  </td>
                  <%
                } else {
                  // This is a day in the current month (a numbered cell)
                  if ((curr_day >= startDateOfWeek) && (curr_day <= endDateOfWeek)  && (currentMonth == month) && (currentYear == year))
                  {
                    String currentWeekStyle = "miniCalCurrentWeek";
                    //currentDay
                    if((curr_day == day_of_month) && (currentMonth == month) && (currentYear == year)){
                      currentWeekStyle = "miniCalCurrentDay";
                    }
                    // this is TODAY, highlight it
                    %>
                     <td class="<%=currentWeekStyle%>">
                      <a class="plainLink" onclick="mini_selectDate('<%=df.format(day.getTime())%>');">
                        <%=curr_day%>
                      </a>
                     </td>
                     <%
                  } else {
                    // this is all other days of the current month (no highlight)
                    %>
                    <td class="miniCalNormalDay" >
                      <a class="plainLink" onclick="mini_selectDate('<%=df.format(day.getTime())%>');">
                        <%=curr_day%>
                      </a>
                    </td>
                    <%
                  }
                  curr_day++;
                }
              }else{
                // This is a day in the NEXT month (a blank cell)
                %>
                <td class="miniCalBlank">
                &nbsp;
                </td><%
              }
              day_count++;
            }
            %>
            </tr>
            <%
          }
        %>
      </table>
    </td>
  </tr>
</table>