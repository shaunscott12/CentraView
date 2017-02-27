<%--
 * $RCSfile: weekly.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ page import="java.util.Calendar"%>
<%@ page import = "java.util.Date"%>
<%@ page import = "java.util.Iterator"%>
<%@ page import = "java.util.Set"%>
<%@ page import = "org.apache.struts.action.DynaActionForm"%>
<%@ page import = "com.centraview.calendar.CalendarActivityObject"%>
<%@ page import = "com.centraview.calendar.CalendarList"%>
<%@ page import = "com.centraview.calendar.CalendarListElement"%>
<%@ page import = "com.centraview.calendar.CalendarMember"%>
<%
  CalendarList calList = (CalendarList) request.getAttribute("displaylist");
  CalendarListElement monCalListElement = (CalendarListElement)calList.get(new Integer(0));
  CalendarListElement thuCalListElement = (CalendarListElement)calList.get(new Integer(3));
  CalendarListElement tueCalListElement = (CalendarListElement)calList.get(new Integer(1));
  CalendarListElement friCalListElement = (CalendarListElement)calList.get(new Integer(4));
  CalendarListElement wedCalListElement = (CalendarListElement)calList.get(new Integer(2));
  CalendarListElement satCalListElement = (CalendarListElement)calList.get(new Integer(5));
  CalendarListElement sunCalListElement = (CalendarListElement)calList.get(new Integer(6));
  DateFormat timeFormat = new SimpleDateFormat("h:mm a") ;
  DateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yy h:mm a") ;
%>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
<script language="JavaScript" src="<html:rewrite page="/stylesheet/overlib.js"/>"><!-- overLIB (c) Erik Bosrup --></script>
<table border="0" cellpadding="3" cellspacing="1" width="100%">
  <tr>
    <td width="50%" valign="top">
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["0"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.monday"/> (<c:out value='${calendarForm.map.headerList["0"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["0"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN MONDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(monCalListElement != null){
                    Set setElement = monCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)monCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      //activities filtered by activityType drop down
                      //should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");


                      if (calMember.getIcon() != null)
                      {
                      %>
                                  <tr>
                                    <td valign="top">
                                      <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                      <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                        <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                      </a>
                                    </td>
                                  </tr>

                      <%
                      }
                    }

                }
                if (monCalListElement == null || monCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["0"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END MONDAY --%>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["1"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.tuesday"/> (<c:out value='${calendarForm.map.headerList["1"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["1"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN TUESDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(tueCalListElement != null){
                    Set setElement = tueCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)tueCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());


                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      //activities filtered by activityType drop down
                      //should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      if (calMember.getIcon() != null)
                      {
                      %>
                                  <tr>
                                    <td valign="top">
                                      <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                      <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                        <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                      </a>
                                    </td>
                                  </tr>
                      <%
                      }
                    }

                }
                if (tueCalListElement == null || tueCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["1"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END TUESDAY --%>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["2"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.wednesday"/> (<c:out value='${calendarForm.map.headerList["2"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["2"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN WEDNESDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(wedCalListElement != null){
                    Set setElement = wedCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)wedCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      //activities filtered by activityType drop down
                      //should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

                      if (calMember.getIcon() != null)
                      {
                      %>
                                  <tr>
                                    <td valign="top">
                                      <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                      <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                        <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                      </a>
                                    </td>
                                  </tr>
                      <%
                      }
                    }

                }
                if (wedCalListElement == null || wedCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["2"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END WEDNESDAY --%>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["3"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.thursday"/> (<c:out value='${calendarForm.map.headerList["3"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["3"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN THURSDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(thuCalListElement != null){
                    Set setElement = thuCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)thuCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());


                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      //activities filtered by activityType drop down
                      //should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

                      if (calMember.getIcon() != null)
                      {
                      %>
                                  <tr>
                                    <td valign="top">
                                      <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                      <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                        <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                      </a>
                                    </td>
                                  </tr>
                      <%
                      }
                    }

                }
                if (thuCalListElement == null || thuCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["3"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END THURSDAY --%>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["4"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.friday"/> (<c:out value='${calendarForm.map.headerList["4"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["4"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN FRIDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(friCalListElement != null){
                    Set setElement = friCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)friCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());

                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      //activities filtered by activityType drop down
                      //should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

                      if (calMember.getIcon() != null)
                      {
                      %>
                                  <tr>
                                    <td valign="top">
                                      <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                      <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                        <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                      </a>
                                    </td>
                                  </tr>
                      <%
                      }
                    }

                }
                if (friCalListElement == null || friCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["4"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END MONDAY --%>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["5"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.saturday"/> (<c:out value='${calendarForm.map.headerList["5"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["5"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN SATURDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(satCalListElement != null){
                    Set setElement = satCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)satCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      //activities filtered by activityType drop down
                      //should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

                      if (calMember.getIcon() != null)
                      {
                      %>
                                  <tr>
                                    <td valign="top">
                                      <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                      <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                        <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                      </a>
                                    </td>
                                  </tr>
                      <%
                      }
                    }

                }
                if (satCalListElement == null || satCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["5"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END SATURDAY --%>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="0" cellspacing="0" width="100%" class="calendarWeeklyDay">
        <tr>
          <td class="sectionHeader">
            <a href="javascript:<c:out value='${calendarForm.map.headerLinkList["6"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.sunday"/> (<c:out value='${calendarForm.map.headerList["6"]}' />)
            </a> -
            <a href="javascript:<c:out value='${calendarForm.map.dateList["6"]}' />" class="plainLink">
              <bean:message key="label.pages.calendar.schedule"/>
            </a>
          </td>
        </tr>
        <tr>
          <td>
              <!-- BEGIN SUNDAY -->
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <%
                if(sunCalListElement != null){
                    Set setElement = sunCalListElement.keySet();
                    Iterator it = setElement.iterator();
                    while (it.hasNext())
                    {
                      CalendarMember calMember = (CalendarMember)sunCalListElement.get(it.next());
                      CalendarActivityObject calActivity = (CalendarActivityObject)(calMember.getActivityobject());

                      String activityType = calActivity.getActivityType();
                      String requestActivityType = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("activityType");

                      if (requestActivityType == null) {
                        requestActivityType = "All";
                      }

                      // activities filtered by activityType drop down
                      // should be refactored out into the EJB layer in the future
                      if (! requestActivityType.equals("All")) {
                        if (! activityType.equals(requestActivityType)) {
                          continue;
                        }
                      }

                      Calendar start = (Calendar)calActivity.getStartTime();
                      Calendar end = (Calendar)calActivity.getEndTime();

                      dateTimeFormat.setCalendar(start);
                      dateTimeFormat.setCalendar(end);
                      Date sdate= start.getTime();
                      Date edate= end.getTime();

                      String activityTitle = calActivity.getActivity();
                      activityTitle = activityTitle.replaceAll("\r","");
                      activityTitle = activityTitle.replaceAll("\n","<br/>");
                      String overlibTitle = activityTitle.replaceAll("\'","\\\\\'");

                      String activityDetails = (calActivity.getActivityDetail() != null) ? calActivity.getActivityDetail() : "";
                      activityDetails = activityDetails.replaceAll("\r","");
                      activityDetails = activityDetails.replaceAll("\n","<br />");
                      String overlibDetails = activityDetails.replaceAll("\'","\\\\\'");

                      if (calMember.getIcon() != null)
                      {
                      %>
                      <tr>
                        <td valign="top">
                          <img src="<html:rewrite page="/images" />/<%=calMember.getIcon()%>"/>
                                        <a class="plainLink" href="javascript:<%=calMember.getRequestURL()%>" onmouseover="return overlib('<b><%=overlibTitle%></b><br /><%=overlibDetails%>', CAPTION, '<%=(dateTimeFormat.format(sdate)).trim()%> - <%=(dateTimeFormat.format(edate)).trim()%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();">
                                          <%=(timeFormat.format(sdate)).trim()%> <%=activityTitle%>
                                        </a>
                        </td>
                      </tr>
                      <%
                      }
                    }

                }
                if (sunCalListElement == null || sunCalListElement.size() == 0)
                {
                %>
                  <tr>
                      <td style="cursor:pointer" onclick="javascript:<c:out value='${calendarForm.map.dateList["6"]}' />">
                        &nbsp;
                      </td>
                  </tr>
                <%
                }
                %>
              </table>
              <%-- END SUNDAY --%>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>