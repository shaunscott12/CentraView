<%--
 * $RCSfile: weekly_columns.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import = "java.text.DateFormat"%>
<%@ page import = "java.text.SimpleDateFormat"%>
<%@ page import = "java.util.Calendar"%>
<%@ page import = "java.util.Date"%>
<%@ page import = "java.util.Iterator"%>
<%@ page import = "java.util.Set"%>
<%@ page import = "org.apache.struts.action.DynaActionForm"%>
<%@ page import = "com.centraview.calendar.CalendarActivityObject"%>
<%@ page import = "com.centraview.calendar.CalendarList"%>
<%@ page import = "com.centraview.calendar.CalendarListElement"%>
<%@ page import = "com.centraview.calendar.CalendarMember"%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<script language="JavaScript" src="<html:rewrite page="/stylesheet/overlib.js"/>"><!-- overLIB (c) Erik Bosrup --></script>
<table border="0" cellpadding="0" cellspacing="1" class="calendarWeekly">
  <tr>
    <td class="calendarWeeklyHead">&nbsp;</td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["0"]}' />" class="plainLink"><bean:message key="label.pages.calendar.mon"/> <c:out value='${calendarForm.map.headerList["0"]}' /></a>
    </td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["1"]}' />" class="plainLink"><bean:message key="label.pages.calendar.tue"/> <c:out value='${calendarForm.map.headerList["1"]}' /></a>
    </td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["2"]}' />" class="plainLink"><bean:message key="label.pages.calendar.wed"/> <c:out value='${calendarForm.map.headerList["2"]}' /></a>
    </td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["3"]}' />" class="plainLink"><bean:message key="label.pages.calendar.thu"/> <c:out value='${calendarForm.map.headerList["3"]}' /></a>
    </td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["4"]}' />" class="plainLink"><bean:message key="label.pages.calendar.fri"/> <c:out value='${calendarForm.map.headerList["4"]}' /></a>
    </td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["5"]}' />" class="plainLink"><bean:message key="label.pages.calendar.sat"/> <c:out value='${calendarForm.map.headerList["5"]}' /></a>
    </td>
    <td class="calendarWeeklyHead">
    	<a href="javascript:<c:out value='${calendarForm.map.headerLinkList["6"]}' />" class="plainLink"><bean:message key="label.pages.calendar.sun"/> <c:out value='${calendarForm.map.headerList["6"]}' /></a>
    </td>
  </tr>
	<%
    int startHour = 9;
    int endHour = 6;
    Calendar calendarWeekColumn = Calendar.getInstance();
    calendarWeekColumn.set(Calendar.HOUR, 0);
    calendarWeekColumn.set(Calendar.MINUTE, 0);
    calendarWeekColumn.set(Calendar.AM_PM, Calendar.AM);

    Calendar endWeekColumn = Calendar.getInstance();
    endWeekColumn.set(Calendar.HOUR, 1);
    endWeekColumn.set(Calendar.MINUTE, 0);
    endWeekColumn.set(Calendar.AM_PM, Calendar.AM);

    int i = 0;
    while (i < 24) {
      int hour = calendarWeekColumn.get(Calendar.HOUR);
      int minute = calendarWeekColumn.get(Calendar.MINUTE);

      SimpleDateFormat hourFormat = new SimpleDateFormat("h a");
      String hourValue = hourFormat.format(calendarWeekColumn.getTime());

      SimpleDateFormat am_pmFormat = new SimpleDateFormat("a");
      String activityHMPM = am_pmFormat.format(calendarWeekColumn.getTime());

      SimpleDateFormat hourTimeFormat = new SimpleDateFormat("h:mm a");
      String displayStartHour = hourTimeFormat.format(calendarWeekColumn.getTime());
      String displayEndHour = hourTimeFormat.format(endWeekColumn.getTime());

      String classTDHead = "Normal";
      if ((hour >= startHour && activityHMPM.equalsIgnoreCase("AM")) ||
          (hour < (endHour+1) && activityHMPM.equalsIgnoreCase("PM"))) {
        classTDHead = "Working";
      }
    %>
    <tr>
      <td class="calendarWeeklyTime"><%=hourValue%></td>
			<%
        CalendarList calList = (CalendarList)request.getAttribute("displaylist");
        for (int j = 0; j < 7; j++) {
          if (j > 4) {
            classTDHead = "Normal";
          }

          CalendarListElement calListElement = (CalendarListElement)calList.get(new Integer( (j * 24) + i));

          if (calListElement == null || calListElement.size() == 0) {
            %>
            <td style="cursor:pointer" class="calendarWeekly<%=classTDHead%>" onclick="ScheduleActivity(<c:out value="${calendarForm.map.selectedYear}" />, '<c:out value="${calendarForm.map.selectedMonthName}" />', <c:out value="${calendarForm.map.startDayOfWeek}" /> + <%=j%>, '<%=displayStartHour%>', '<%=displayEndHour%>');">
              &nbsp;
            </td>
            <%
          }else{
            Set setElement = calListElement.keySet();
            Iterator it = setElement.iterator();

            %><td class="calendarWeekly<%=classTDHead%>"><%

            while (it.hasNext()) {
              CalendarMember calMember = (CalendarMember)calListElement.get(it.next());
              CalendarActivityObject calActivity= (CalendarActivityObject)(calMember.getActivityobject());

              String activityType = calActivity.getActivityType();
              String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

              if (requestActivityType == null) {
                requestActivityType = "All";
              }

              // activities filtered by activityType drop down
              // TODO: should be refactored out into the EJB layer in the future
              if (! requestActivityType.equals("All")) {
                if (! activityType.equals(requestActivityType)) {
                  continue;
                }
              }

              if (calMember.getSpanserialId() != 0) {
                continue;
              }

              DateFormat dfstart = new SimpleDateFormat("MM/dd/yy h:mm a");
              DateFormat dfend = new SimpleDateFormat("MM/dd/yy h:mm a");
              Calendar start = (Calendar)calActivity.getStartTime();
              Calendar end = (Calendar)calActivity.getEndTime();

              dfstart.setCalendar(start);
              dfend.setCalendar(end);
              Date sdate = start.getTime();
              Date edate = end.getTime();

              String activityTitle = calActivity.getActivity();
              activityTitle = activityTitle.replaceAll("\r","");
              activityTitle = activityTitle.replaceAll("\n","<br/>");
              String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

              String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
              activityDetails = activityDetails.replaceAll("\r","");
              activityDetails = activityDetails.replaceAll("\n","<br/>");
              String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

              if (calMember.getIcon() != null) {
                %>
                <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dfstart.format(sdate)).trim()%> - <%=(dfend.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                  <%=activityTitle%>
                </a>
                </br>
                <%
              }
            }

            %></td><%
          }
        }

      %>
    </tr>
    <%

    i++;
    calendarWeekColumn.add(Calendar.HOUR, 1);
    endWeekColumn.add(Calendar.HOUR, 1);
  }
%>
</table>