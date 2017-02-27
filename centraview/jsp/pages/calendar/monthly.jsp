<%--
 * $RCSfile: monthly.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import = "java.text.DateFormat"%>
<%@ page import = "java.text.SimpleDateFormat"%>

<%@ page import = "java.util.Calendar"%>
<%@ page import = "java.util.Collection"%>
<%@ page import = "java.util.Date"%>
<%@ page import = "java.util.GregorianCalendar"%>
<%@ page import = "java.util.Iterator"%>
<%@ page import = "java.util.Set"%>
<%@ page import = "org.apache.struts.action.DynaActionForm"%>
<%@ page import = "com.centraview.calendar.CalendarActivityObject"%>
<%@ page import = "com.centraview.calendar.CalendarList"%>
<%@ page import = "com.centraview.calendar.CalendarListElement"%>
<%@ page import = "com.centraview.calendar.CalendarMember"%>
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
  // /jsp/calendar/select_date_time.jsp for examples of how to
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

  SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMMM yyyy");
  String dateHeader = dateFormatter.format(selectedDate.getTime());

  CalendarList calList = (CalendarList) request.getAttribute("displaylist");
%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<script language="JavaScript" src="<html:rewrite page="/stylesheet/overlib.js"/>"><!-- overLIB (c) Erik Bosrup --></script>
<table border="0" cellpadding="0" cellspacing="1" width="100%" class="mediumTableBorders">
  <tr>
    <td class="bigCalHeadNav">&nbsp;</td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.sunday"/></td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.monday"/></td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.tuesday"/></td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.wednesday"/></td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.thursday"/></td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.friday"/></td>
    <td class="bigCalHeader"><bean:message key="label.pages.calendar.saturday"/></td>
  </tr>
  <%
    for (int i=1; i<=no_weeks; i++)
    {
      // loop through the weeks of the month
      %>
      <tr>
      <c:if test="${requestScope.showWeeklyColumn}">
        <td class="bigCalNav">
          <a class="plainLink" onclick="goToWeeklyView(<%=year%>, <%=(month+1)%>, <%=curr_day%>);">
            &raquo;
          </a>
        </td>
      </c:if>
      <%
      for (int j=1; j<=7; j++)
      {
        // loop through 7 days of a week
        if (curr_day <= no_days)
        {
          if (day_count < offset)
          {
            // This is a day in the PREVIOUS month (a blank cell)
            %>
            <td class="bigCalBlank" >
              &nbsp;
            </td>
            <%
          }else{
            // This is a day in the current month (a numbered cell)
            String bigCalClass = "bigCalNormalDay";
            if ((curr_day == day_of_month) && (currentMonth == month) && (currentYear == year))
            {
              bigCalClass = "bigCalCurrentDay";
            }
            CalendarListElement calListElement = (CalendarListElement)calList.get(new Integer(curr_day-1));
              %>
                <td class="<%=bigCalClass%>">
                  <table border="0" cellpadding="0" cellspacing="0" width="100%" class="bigCalDayTop">
                    <tr>
                      <td valign="top">
                        <div class="bigCalDayNew"><a onclick="ScheduleActivity(<c:out value="${calendarForm.map.selectedYear}" />, '<c:out value="${calendarForm.map.selectedMonthName}" />', <%=curr_day%>, '','');" title="<bean:message key='label.pages.calendar.schedulenewactivity'/>" class="plainLink"><bean:message key="label.pages.calendar.new"/></a></div>
                      </td>
                      <td valign="top">
                        <div class="bigCalDayNumber"><a href="javascript:mini_selectDate(<%=year%>, <%=(month+1)%>, <%=curr_day%>);" title="Go to Daily View" class="plainLink"><%=curr_day%></a></div>
                      </td>
                    </tr>
                  </table>
                  <%
                    if (calListElement != null && calListElement.size() != 0) {
                      Set setElement = calListElement.keySet();
                      Iterator it =  setElement.iterator();
                      // if activities are present for this day display them
                      while (it.hasNext()) {
                        CalendarMember calMember = (CalendarMember)calListElement.get(it.next());
                        CalendarActivityObject calActivity= (CalendarActivityObject) (calMember.getActivityobject());


                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy h:mm a");

                        String activityDetails ="";
                        String activityTitle = "";
                        String overlibTitle = "";
                        String overlibDetails = "";
                        String ststring = "";

                        if (calActivity != null) {
                          Calendar start = (Calendar)calActivity.getStartTime();
                          Calendar end = (Calendar)calActivity.getEndTime();

                          String activityType = calActivity.getActivityType();
                          String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                          if (requestActivityType == null) {
                            requestActivityType = "All";
                          }

                          //activities filtered by activityType drop down
                          //Clumsy because this logic has to go in daily/weekly/monthly/yearly.jsp
                          //should be refactored out into the EJB layer in the future
                          if (! requestActivityType.equals("All")) {
                            if (! activityType.equals(requestActivityType)) {
                              continue;
                            }
                          }

                          dateFormat.setCalendar(start);
                          dateFormat.setCalendar(end);
                          Date startDate= start.getTime();
                          Date endDate= end.getTime();
                          ststring = dateFormat.format(startDate) + " - " +dateFormat.format(endDate);
                          calMember.getRequestURL();

                          activityTitle = calActivity.getActivity();
                          activityTitle = activityTitle.replaceAll("\r","");
                          activityTitle = activityTitle.replaceAll("\n","<br/>");
                          overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                          activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                          activityDetails = activityDetails.replaceAll("\r","");
                          activityDetails = activityDetails.replaceAll("\n","<br />");
                          overlibDetails = activityDetails.replaceAll("\'","\\\\\'");
                        }

                        if (calMember.getIcon() != null) {
                          %>
                          &nbsp;<img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                          <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br/><%=overlibDetails%>', CAPTION, '<%=ststring%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();"><%=activityTitle%></a><br>
                          <%
                        }
                      }
                    }
                  %>
                </td>
              <%

            curr_day++;
          }
        }else{
          // This is a day in the NEXT month (a blank cell)
          %>
          <td class="bigCalBlank">
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