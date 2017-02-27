<%--
 * $RCSfile: avail_frame.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:35 $ - $Author: mking_cv $
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
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.activity.AvailableList" %>
<%@ page import="com.centraview.calendar.*, java.util.*, java.text.*" %>
<%
  // get the startdate from the request 
  GregorianCalendar startDate = (GregorianCalendar)request.getAttribute("startDate");
  int cmonth = startDate.get(Calendar.MONTH);     
  String cyear = new Integer(startDate.get(Calendar.YEAR)).toString();
  String cday = new Integer(startDate.get(Calendar.DATE)).toString();       
  
  String dayArr[] = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
  
  TimeZone tz = null;
  
  if (session.getAttribute("userobject") != null) {
    com.centraview.common.UserObject uobj = (com.centraview.common.UserObject)session.getAttribute("userobject");
    int userId = uobj.getIndividualID();
    
    try {
      if (request.getParameter("tz") != null) {
        tz = TimeZone.getTimeZone(request.getParameter("tz"));
      }else{
        tz= TimeZone.getTimeZone((uobj.getUserPref()).getTimeZone());
      }
    }catch(Exception e){
      tz = TimeZone.getTimeZone("EST");
    }
  }  
  
  GregorianCalendar prevDate = new GregorianCalendar(tz);
  prevDate.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DATE), 0, 0);
  prevDate.add(Calendar.DATE, -1);
  
  int prevyear = prevDate.get(Calendar.YEAR);    
  int prevmonth = prevDate.get(Calendar.MONTH);
  int prevday = prevDate.get(Calendar.DATE);
  
  String pyear = new Integer(prevyear).toString();
  String pmonth = new Integer(prevmonth).toString();
  String pday = new Integer(prevday).toString();
  
  startDate.add(Calendar.DATE, 1);
  
  int startyear = startDate.get(Calendar.YEAR);    
  int startmonth = startDate.get(Calendar.MONTH);
  int startday = startDate.get(Calendar.DATE);
  
  String year = new Integer(startyear).toString();
  String month = new Integer(startmonth).toString();
  String day = new Integer(startday).toString();
  
  HashMap availList = (HashMap)request.getAttribute("availabilityList");
  
  Iterator userID = null; 
  boolean userAvailability[] = new boolean[25];
  Set availElement = availList.keySet();
  userID = availElement.iterator(); 
  
  int counterDisplayOnce = 0;
  int counterOddOrEvenUser = 0;
  int counterOddOrEvenData = 0;
%>
<html>
<head>
  <link rel="stylesheet" type="text/css" href="<html:rewrite page="/stylesheet/main.css"/>">
</head>
<body>
<table border="0" cellpadding="2" cellspacing="0" width="100%">
  <tr>
    <td class="availHeadCell"><span class="labelBold"><%=dayArr[cmonth] + " " + cday + ", " + cyear%></span></td>
    <td class="availHeadCell"><img src="images/spacer.gif" width="1" height="1" border="0" alt=""></td>
    <td class="availHeadCell">12</TD>
    <td class="availHeadCell">1</TD>
    <td class="availHeadCell">2</TD>
    <td class="availHeadCell">3</TD>
    <td class="availHeadCell">4</TD>
    <td class="availHeadCell">5</TD>
    <td class="availHeadCell">6</TD>
    <td class="availHeadCell">7</TD>
    <td class="availHeadCell">8</TD>
    <td class="availHeadCell">9</TD>
    <td class="availHeadCell">10</TD>
    <td class="availHeadCell">11</TD>
    <td class="availHeadCell">12</td>
    <td class="availHeadCell">1</td>
    <td class="availHeadCell">2</TD>
    <td class="availHeadCell">3</TD>
    <td class="availHeadCell">4</TD>
    <td class="availHeadCell">5</TD>
    <td class="availHeadCell">6</TD>
    <td class="availHeadCell">7</TD>
    <td class="availHeadCell">8</TD>
    <td class="availHeadCell">9</TD>
    <td class="availHeadCell">10</TD>
    <td class="availHeadCell">11</TD>
    <td class="availHeadCell"><img src="images/spacer.gif" width="1" height="1" border="0" alt=""></td>
  </tr>
  <%
    counterDisplayOnce =0;
    userID = availElement.iterator(); 
    while (userID.hasNext()) {
      AvailableList avail = (AvailableList)availList.get((Integer)userID.next());
      userAvailability = avail.getAvailable(); 
      
      %>
      <tr>
        <td class="availCellOdd"><%=avail.getUserName()%></td>
        <td class="availCellOdd"><a class="plainLink" href="<html:rewrite page="/activities/available_list.do"/>?YEAR=<%=pyear%>&MONTH=<%=pmonth%>&DAY=<%=pday%>">&laquo;</a></td>
        <%
          for (int count=0; count < 24; count++) {	
            if (userAvailability[count]) { 
              %><td class="availBusyCell">&nbsp;</td><%
            }else{
              if (count == 12) {
                %><td class="availScheduledCell">&nbsp;</td><%
              }else{
                %><td class="availCellOdd">&nbsp;</td><%
              }
            }
          }
        %>
        <td class="availCellOdd"><a class="plainLink" href="<html:rewrite page="/activities/available_list.do"/>?YEAR=<%=year%>&MONTH=<%=month%>&DAY=<%=day%>">&raquo;</a></td>
      </tr> 
      <%
    }   // end while loop
  %>
</table>
</body>
</html>
