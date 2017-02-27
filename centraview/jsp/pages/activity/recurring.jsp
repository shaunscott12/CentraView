<%--
 * $RCSfile: recurring.jsp,v $    $Revision: 1.4 $  $Date: 2005/09/21 16:36:39 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.centraview.common.*"%>
<%@ page import="com.centraview.activity.ActivityForm"%>
<%@ page import="com.centraview.activity.ConstantKeys" %>
<%
  ActivityForm activityform = (ActivityForm)request.getAttribute("activityform");
  String strWeekOn[] = activityform.getActivityRecurringWeeklyOn();
  String stractivityFindWeek[] = activityform.getActivityFindWeek();
  String strSelectedweek[] = activityform.getActivitySelectedWeek();
  String recurringfreq = "DAY";
  if (activityform.getActivityRecurringFrequency() != null) {
    recurringfreq = activityform.getActivityRecurringFrequency();
  }
%>
<script language="Javascript">
  /*
    Opens the Select Date and Time popup window, with the dates
    populated to the current values of those set on this form.
   */
  function recur_openSelectDateTime()
  {
    var startMonth = document.getElementById("activityRecurringStartMonth");
    var startDay = document.getElementById("activityRecurringStartDay");
    var startYear = document.getElementById("activityRecurringStartYear");
    var endMonth = document.getElementById("activityRecurringEndMonth");
    var endDay = document.getElementById("activityRecurringEndDay");
    var endYear = document.getElementById("activityRecurringEndYear");

    var startDate = startMonth.value + "/" + startDay.value  + "/" + startYear.value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = endMonth.value + "/" + endDay.value + "/" + endYear.value;
    if (endDate == "//"){ endDate = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate, 'selectDateTime', 350, 500, '');
  }

  /*
    This function is called from the select_date_time.do popup, which passes
    the date and time information from that popup back to this JSP. The code
    within this function can be modified to do whatever you need to do with
    the data on this page, including munging it, setting form properties, etc.
    BUT YOU SHOULD NOT CHANGE THE SIGNATURE OF THIS FUNCTION, WHATSOEVER!
  */
  function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
  {
    var recurStartDate = document.getElementById("activityRecurStartDate");
    var recurEndDate = document.getElementById("activityRecurEndDate");

    if (startDate == null || startDate.length < 1) {
      recurStartDate.value = "";
    } else {
      recurStartDate.value = startDate;
    }

    if (endDate == null || endDate.length < 1) {
      recurEndMonth.value = "";
    } else {
      recurEndDate.value = endDate;
    }
    return(true);
  }
</script>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="50%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.startdate"/></td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text property="activityRecurStartDate" styleId="activityRecurStartDate" styleClass="inputBox" maxlength="10" size="10" /></td>
                <td><a class="plainLink" onclick="recur_openSelectDateTime();"><html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.enddate"/></td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text property="activityRecurEndDate" styleId="activityRecurEndDate" styleClass="inputBox" maxlength="10" size="10" /></td>
                <td><a class="plainLink" onclick="recur_openSelectDateTime();"><html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold">
            <bean:message key="label.activity.frequency"/>:&nbsp;&nbsp;
            <%
              Collection recfreqvec = new ArrayList();
              recfreqvec.add(new DDNameValue("", "-Select-"));
              recfreqvec.add(new DDNameValue("DAY", "Daily"));
              recfreqvec.add(new DDNameValue("WEEK", "Weekly"));
              recfreqvec.add(new DDNameValue("MONTH", "Monthly"));
              recfreqvec.add(new DDNameValue("YEAR", "Yearly"));
              pageContext.setAttribute("recfreqvec", recfreqvec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityRecurringFrequency" styleId="activityRecurringFrequency" styleClass="inputBox" onchange="act_showLayer(this);">
              <html:options collection="recfreqvec" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <% String dayStyle = (recurringfreq.equals("DAY") || recurringfreq.equals("")) ? "block" : "none"; %>
            <div id="DAY" name="DAY" class="dailyLayer" style="display:<%=dayStyle%>;">
              <table border="0" cellpadding="0" cellspacing="0" class="formTable">
                <tr>
                  <td class="contentCell">
                    <html:radio property="activityRecurringDailyEvery" styleId="activityRecurringDailyEvery" value="1"/>
                    <bean:message key="label.activity.every"/> <html:text property="activityRecurringDailyDays" styleId="activityRecurringDailyDays" styleClass="inputBox" size="3"/> <bean:message key="label.activity.days"/>
                  </td>
                </tr>
                <tr>
                  <td class="contentCell">
                    <html:checkbox property="activityRecurringDailyWeekdays" styleId="activityRecurringDailyWeekdays" styleClass="checkBox" value="1"/>
                    <bean:message key="label.activity.weekdaysonly"/>
                  </td>
                </tr>
              </table>
            </div>
            <% String weekStyle = (recurringfreq.equals("WEEK")) ? "block" : "none"; %>
            <div id="WEEK" name="WEEK" class="weeklyLayer"  style="display:<%=weekStyle%>">
              <table width="100%" cellpadding="2" cellspacing="0" class="formTable">
                <tr>
                  <td class="contentCell">
                    <bean:message key="label.activity.every"/> <html:text property="activityRecurringWeeklyEvery" styleId="activityRecurringWeeklyEvery" styleClass="inputBox" size="3"/> <bean:message key="label.activity.weeks"/>:
                  </td>
                </tr>
                <tr>
                  <td class="contentCell">
                    <logic:iterate name="activityform" property="activitySelectedWeek" id="element" >
                      <html:multibox property="activityFindWeek" styleClass="checkBox">
                        <bean:write name="element"/>
                      </html:multibox>
                      <bean:write name="element"/>
                    </logic:iterate>
                  </td>
                </tr>
              </table>
            </div>
            <% String monthStyle = (recurringfreq.equals("MONTH")) ? "block" : "none"; %>
            <div id="MONTH" name="MONTH" class="monthlyLayer" style="display:<%=monthStyle%>;">
              <table width="100%" cellpadding="2" cellspacing="0" class="formTable">
                <tr>
                  <td class="contentCell">
                    <html:radio property="activityRecurringMonthlyEvery" styleId="activityRecurringMonthlyEvery" value="0"/>
                    <html:text property="activityRecurringMonthlyEveryDay" styleId="activityRecurringMonthlyEveryDay" styleClass="inputBox" size="3"/>
                    <bean:message key="label.activity.dayofmonth"/>
                  </td>
                </tr>
                <tr>
                  <td class="contentCell">
                    <html:radio property="activityRecurringMonthlyEvery" styleId="activityRecurringMonthlyEvery" value="1"/>
                    <%
                      Collection colMonthlyweek = new ArrayList();
                      colMonthlyweek.add(new DDNameValue(1, "First"));
                      colMonthlyweek.add(new DDNameValue(2, "Second"));
                      colMonthlyweek.add(new DDNameValue(3, "Third"));
                      colMonthlyweek.add(new DDNameValue(4, "Fourth"));
                      colMonthlyweek.add(new DDNameValue(5, "Last"));
                      pageContext.setAttribute("colMonthlyweek", colMonthlyweek, PageContext.PAGE_SCOPE);
                    %>
                    <html:select property="activityRecurringMonthlyOnDay" styleId="activityRecurringMonthlyOnDay" size="6" styleClass="inputBox">
                      <html:options collection="colMonthlyweek" property="id" labelProperty="name"/>
                    </html:select>
                    <%
                      Collection colMonthlyday = new ArrayList();
                      colMonthlyday.add(new DDNameValue(0, "Monday"));
                      colMonthlyday.add(new DDNameValue(1, "Tuesday"));
                      colMonthlyday.add(new DDNameValue(2, "Wednesday"));
                      colMonthlyday.add(new DDNameValue(3, "Thursday"));
                      colMonthlyday.add(new DDNameValue(4, "Friday"));
                      colMonthlyday.add(new DDNameValue(5, "Saturday"));
                      colMonthlyday.add(new DDNameValue(6, "Sunday"));
                      pageContext.setAttribute("colMonthlyday", colMonthlyday, PageContext.PAGE_SCOPE);
                    %>
                    <html:select property="activityRecurringMonthlyOnWeek" styleId="activityRecurringMonthlyOnWeek" size="7" styleClass="inputBox">
                      <html:options collection="colMonthlyday" property="id" labelProperty="name"/>
                    </html:select>
                    <bean:message key="label.activity.ofeverymonth"/>
                  </td>
                </tr>
              </table>
            </div>
            <% String yearStyle = (recurringfreq.equals("YEAR")) ? "block" : "none"; %>
            <div id="YEAR" name="YEAR" class="yearlyLayer"  style="display:<%=yearStyle%>;">
              <table width="100%" cellpadding="2" cellspacing="0" class="formTable">
                <tr>
                  <td class="contentCell">
                    <html:radio property="activityRecurringYearlyEvery" styleId="activityRecurringYearlyEvery" value="0"/> <bean:message key="label.activity.every"/>
                    <%
                      Collection colYearlyeverymonth = new ArrayList();
                      colYearlyeverymonth.add(new DDNameValue(0, "January"));
                      colYearlyeverymonth.add(new DDNameValue(1, "February"));
                      colYearlyeverymonth.add(new DDNameValue(2, "March"));
                      colYearlyeverymonth.add(new DDNameValue(3, "April"));
                      colYearlyeverymonth.add(new DDNameValue(4, "May"));
                      colYearlyeverymonth.add(new DDNameValue(5, "June"));
                      colYearlyeverymonth.add(new DDNameValue(6, "July"));
                      colYearlyeverymonth.add(new DDNameValue(7, "August"));
                      colYearlyeverymonth.add(new DDNameValue(8, "September"));
                      colYearlyeverymonth.add(new DDNameValue(9, "October"));
                      colYearlyeverymonth.add(new DDNameValue(10, "November"));
                      colYearlyeverymonth.add(new DDNameValue(11, "December"));
                      pageContext.setAttribute("colYearlyeverymonth", colYearlyeverymonth, PageContext.PAGE_SCOPE);
                    %>
                    <html:select property="activityRecurringYearlyEveryMonth" styleId="activityRecurringYearlyEveryMonth" styleClass="inputBox">
                      <html:options collection="colYearlyeverymonth" property="id" labelProperty="name"/>
                    </html:select>
                    <html:text property="activityRecurringYearlyEveryDay" styleId="activityRecurringYearlyEveryDay" styleClass="inputBox" size="3" />
                  </td>
                </tr>
                <tr>
                  <td class="contentCell">
                    <html:radio property="activityRecurringYearlyEvery" value="1"/> The
                    <%
                      Collection colYearlyonweek = new ArrayList();
                      colYearlyonweek.add(new DDNameValue(1, "First"));
                      colYearlyonweek.add(new DDNameValue(2, "Second"));
                      colYearlyonweek.add(new DDNameValue(3, "Third"));
                      colYearlyonweek.add(new DDNameValue(4, "Fourth"));
                      colYearlyonweek.add(new DDNameValue(5, "Last"));
                      pageContext.setAttribute("colYearlyonweek", colYearlyonweek, PageContext.PAGE_SCOPE);
                    %>
                    <html:select property="activityRecurringYearlyOnWeek" styleId="activityRecurringYearlyOnWeek" size="6" styleClass="inputBox">
                      <html:options collection="colYearlyonweek" property="id" labelProperty="name"/>
                    </html:select>
                    <%
                      Collection colYearlyonday = new ArrayList();
                      colYearlyonday.add(new DDNameValue(0, "Monday"));
                      colYearlyonday.add(new DDNameValue(1, "Tuesday"));
                      colYearlyonday.add(new DDNameValue(2, "Wednesday"));
                      colYearlyonday.add(new DDNameValue(3, "Thursday"));
                      colYearlyonday.add(new DDNameValue(4, "Friday"));
                      colYearlyonday.add(new DDNameValue(5, "Saturday"));
                      colYearlyonday.add(new DDNameValue(6, "Sunday"));
                      pageContext.setAttribute("colYearlyonday", colYearlyonday, PageContext.PAGE_SCOPE);
                    %>
                    <html:select property="activityRecurringYearlyOnDay" styleId="activityRecurringYearlyOnDay" size="7" styleClass="inputBox">
                      <html:options collection="colYearlyonday" property="id" labelProperty="name"/>
                    </html:select>
                    <bean:message key="label.activity.of"/>
                    <%
                      Collection colYearlyonmonth = new ArrayList();
                      colYearlyonmonth.add(new DDNameValue(0, "January"));
                      colYearlyonmonth.add(new DDNameValue(1, "February"));
                      colYearlyonmonth.add(new DDNameValue(2, "March"));
                      colYearlyonmonth.add(new DDNameValue(3, "April"));
                      colYearlyonmonth.add(new DDNameValue(4, "May"));
                      colYearlyonmonth.add(new DDNameValue(5, "June"));
                      colYearlyonmonth.add(new DDNameValue(6, "July"));
                      colYearlyonmonth.add(new DDNameValue(7, "August"));
                      colYearlyonmonth.add(new DDNameValue(8, "September"));
                      colYearlyonmonth.add(new DDNameValue(9, "October"));
                      colYearlyonmonth.add(new DDNameValue(10, "November"));
                      colYearlyonmonth.add(new DDNameValue(11, "December"));
                      pageContext.setAttribute("colYearlyonmonth", colYearlyonmonth, PageContext.PAGE_SCOPE);
                    %>
                    <html:select property="activityRecurringYearlyOnMonth" styleId="activityRecurringYearlyOnMonth" styleClass="inputBox">
                      <html:options collection="colYearlyonmonth" property="id" labelProperty="name"/>
                    </html:select>
                  </td>
                </tr>
              </table>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>