<%--
 * $RCSfile: daily.jsp,v $    $Revision: 1.2 $  $Date: 2005/05/11 15:33:17 $ - $Author: mking_cv $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import = "java.text.DateFormat"%>
<%@ page import = "java.text.SimpleDateFormat"%>
<%@ page import = "java.util.Calendar"%>
<%@ page import = "java.util.Collection"%>
<%@ page import = "java.util.Date"%>
<%@ page import = "java.util.GregorianCalendar"%>
<%@ page import = "java.util.Iterator"%>
<%@ page import = "java.util.Set"%>
<%@ page import = "java.util.TimeZone"%>
<%@ page import = "java.util.TreeMap"%>
<%@ page import = "org.apache.struts.action.DynaActionForm"%>
<%@ page import = "com.centraview.calendar.CalendarActivityObject"%>
<%@ page import = "com.centraview.calendar.CalendarList"%>
<%@ page import = "com.centraview.calendar.CalendarListElement"%>
<%@ page import = "com.centraview.calendar.CalendarMember"%>
<html:form action="/calendar.do">
<%

  CalendarList calList = (CalendarList)request.getAttribute("displaylist");

  // get the count of TD's
  int numberofTD = 0;
  TreeMap tdPosition = (TreeMap)calList.getTDIndicesTreeMap();
  if (tdPosition != null) {
    Collection rowItems = tdPosition.values();
    if (rowItems != null) {
      Iterator rowItemsIterator = rowItems.iterator();
      while (rowItemsIterator.hasNext()) {
        int totalRowItems = ((Integer)rowItemsIterator.next()).intValue();
        if (totalRowItems > numberofTD) {
          numberofTD = totalRowItems;
        }
      }
    }
  }

  // set the TimeZone
  TimeZone timeZone = null;
  if (request.getAttribute("TZ") != null) {
    timeZone =  (TimeZone)request.getAttribute("TZ");
  }

  //Check for CalendarList is not null
  if (calList != null) {
    Set setElement = calList.keySet();
    Iterator it =  setElement.iterator();
    
    //Daily Column Head
    Calendar calendarDailyColumn = Calendar.getInstance();
    calendarDailyColumn.set(Calendar.HOUR, 0);
    calendarDailyColumn.set(Calendar.MINUTE, 0);
    calendarDailyColumn.set(Calendar.AM_PM, Calendar.AM);
    
    %>
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
      <tr>
        <td class="headerBG">&nbsp;</td>
        <td align="right" class="headerBG">
          <app:cvselect property="timeZone" styleClass="inputBox" onchange="document.calendarForm.submit();">
            <app:cvoptions collection="timeZoneVec" property="strid" labelProperty="name"/>
          </app:cvselect>
        </td>
      </tr>
    </table>
    <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
    <script language="JavaScript" src="<html:rewrite page="/stylesheet/overlib.js"/>"><!-- overLIB (c) Erik Bosrup --></script>
    <table border="0" cellpadding="1" cellspacing="0" width="100%">
      <%
      int k = 0;
      while (it.hasNext()) {
        // THIS LOOP PRINTS ROWS (<tr>'s) THAT REPRESENT THE TIMESPANS
        // THAT THE USER SELECTED (HOURS OF THE DAY BY DEFAULT)
        Object calenderkey = (Integer)it.next();
        CalendarListElement ele = (CalendarListElement)calList.get(calenderkey);

        GregorianCalendar spanStart = new GregorianCalendar();
        GregorianCalendar spanEnd = new GregorianCalendar();
        long timespanlong = 0;

        int startHour = 9;
        int endHour = 6;
        timespanlong = (long)ele.getStartTime();
        spanStart.setTimeZone(timeZone);
        spanStart.setTimeInMillis(timespanlong);  // the starting time of this activity.

        timespanlong = (long)ele.getEndTime();
        spanEnd.setTimeZone(timeZone);
        spanEnd.setTimeInMillis(timespanlong); // the ending time of this activity.

        int hour = spanStart.get(GregorianCalendar.HOUR);
        int minute = spanStart.get(GregorianCalendar.MINUTE);

        SimpleDateFormat hourFormat = new SimpleDateFormat("hh");
        String activityHour = hourFormat.format(calendarDailyColumn.getTime());

        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        String activityMinute = minuteFormat.format(calendarDailyColumn.getTime());

        SimpleDateFormat am_pmFormat = new SimpleDateFormat("a");
        String activityHMPM = am_pmFormat.format(calendarDailyColumn.getTime());

        SimpleDateFormat minuteAM_PMFormat = new SimpleDateFormat("h:mm a");
        String activityStartTime = minuteAM_PMFormat.format(spanStart.getTime());
        String activityEndTime = minuteAM_PMFormat.format(spanEnd.getTime());

        String classTDOddOrEven = "Odd";
        if ((hour % 2) == 0) {
          classTDOddOrEven = "Even";
        }

        String classTDHead = "calendar";
        if ((hour >= startHour && activityHMPM.equalsIgnoreCase("AM")) ||
            (hour < (endHour + 1) && activityHMPM.equalsIgnoreCase("PM"))) {
          classTDHead = "working";
        }

        %>
        <tr>
          <% if ((k % 2) == 0) { %>
          <td class="<%=classTDHead%>Hour<%=classTDOddOrEven%>" rowspan="2" align="right" width="10"><%=activityHour%>:</td>
          <% } %>
          <td nowrap class="<%=classTDHead%>Half<%=classTDOddOrEven%>Top" width="10">
            <a onclick="c_openWindow('/activities/new_activity.do?subScope=All&DAY=<c:out value="${calendarForm.map.selectedDay}" />&MONTH=<c:out value="${calendarForm.map.selectedMonthName}" />&YEAR=<c:out value="${calendarForm.map.selectedYear}" />&STIME=<%=activityStartTime%>&ETIME=<%=activityEndTime%>', 'sched_act', 750, 525, '')"  class="plainLink">
            <%
              if (minute == 0) {
                out.print(activityHMPM);
              } else {
                out.print(activityMinute);
              }
            %>
            </a>
          </td>
          <%
          
            Set setmember = ele.keySet();
            Iterator itMember =  setmember.iterator();
            int internalnumberofTD = numberofTD + 1; // use to fill remaining td
            k++;
            int numberPosition = ((Integer)tdPosition.get(calenderkey)).intValue();
            if (numberPosition != 0) {
              internalnumberofTD -= numberPosition;
            }

            while (itMember.hasNext()) {
              DateFormat dateTimestart = new SimpleDateFormat("MM/dd/yy h:mm a");
              Object keyValue = itMember.next();
              CalendarMember elemember = (CalendarMember)ele.get(keyValue);
              CalendarActivityObject calActivity = (CalendarActivityObject)(elemember.getActivityobject());

              String activityType = calActivity.getActivityType();
              String requestActivityType = (String)((DynaActionForm)request.getAttribute("calendarForm")).get("activityType");
             
              if (requestActivityType == null) {
                requestActivityType = "All";
              }
              
              // activities filtered by activityType drop down
              // TODO: Should be refactored out into the EJB layer in the future
              if (! requestActivityType.equals("All")) {
                if (! activityType.equals(requestActivityType)) {
                  continue;
                }
              }
              	
              String activityTitle = calActivity.getActivity();
              activityTitle = activityTitle.replaceAll("\r","");
              activityTitle = activityTitle.replaceAll("\n","<br/>");
              String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");
              
              String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
              activityDetails = activityDetails.replaceAll("\r","");
              activityDetails = activityDetails.replaceAll("\n","<br />");
              String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");
              
              	
              // If we decide to limit the amount of text shown in the
              // box on the daily view screen, use "shortTitle" variable
              // to display in the HTML below.
              // THERE'S A BUG HERE! MAKE SURE TO CHECK THE SIZE OF THE
              // STRING BEFORE CROPPING IT!!!
              // String shortTitle = activityTitle.substring(0, 25) + "...";
              
              if (elemember.getSpanserialId() == 0) {
                Calendar start = (Calendar)calActivity.getStartTime();
                Calendar end = (Calendar)calActivity.getEndTime();
                dateTimestart.setCalendar(start);
                dateTimestart.setCalendar(end);
                Date sdate= start.getTime();
                Date edate= end.getTime();
                
                if (elemember.getIcon() != null) {
                  int height = elemember.getTotalspans() * 16;
                  %>
                  <td class="<%=classTDHead%>Hour<%=classTDOddOrEven%>" rowspan="<%=elemember.getTotalspans()%>" height="100%">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%" style="border:#3366ff 1px solid;">
                      <tr>
                        <td style="background-color:#ffffff;" valign="top" >
                          <table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
                            <tr>
                              <td class="border" style="background-color:#345788;" width="8"><html:img page="/images/spacer.gif" width="8" /></td>
                              <td>
                                <img src="<html:rewrite page="/images" />/<%=elemember.getIcon()%>"/>
                                <a class="plainLink" href="javascript:<%=elemember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=dateTimestart.format(sdate)%> - <%=dateTimestart.format(edate)%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();"> 
                                <%=activityTitle%>
                                </a>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                  <%
                }
              }
            }
            
            // build the dummy TD's

            // set the dummy TDs to all be the same width (% of 100)
            // eg: 3 columns == 33% each; 4 columns == 25% each; etc...
            int width = 100;
            if ((internalnumberofTD - 1) > 0) {
              // make sure we don't divide by zero!!!
              width = 100 / (internalnumberofTD - 1);
            }

            for (int i = 0 ; i < internalnumberofTD; i++) {
              %>
              <td class="<%=classTDHead%><%=classTDOddOrEven%>Top" width="<%=width%>%"><html:img page="/images/spacer.gif" width="1" height="1" border="0" alt=""/></td>
              <%
            }
          %>
        </tr>
        <%
        calendarDailyColumn.add(Calendar.MINUTE, 30);
      } // end while (it.hasNext())
    %>
    </table>
    <%
  } //  end of if(calList != null)
%>
</html:form>
