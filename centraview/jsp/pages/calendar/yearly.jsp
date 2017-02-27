<%--
 * $RCSfile: yearly.jsp,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:30:06 $ - $Author: mcallist $
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
<%@ page import="java.util.GregorianCalendar"%>
<%@ page import="org.apache.struts.action.DynaActionForm"%>
<table border="0" cellpadding="3" cellspacing="1" width="100%">
  <tr>
      <%
      String yearValue = (String)((DynaActionForm) request.getAttribute("calendarForm")).get("selectedYear");
      int count = 0;
      if(yearValue != null && yearValue.length() != 0){
        int year = Integer.parseInt(yearValue);
        for(int i=0; i<12;i++){
          GregorianCalendar currentDateCalendar = new GregorianCalendar(year, i, 1);
          request.setAttribute("selectedDate", currentDateCalendar);
        %>
        <td>
          <jsp:include page="/jsp/tiles/calendar/mini_calendar.jsp" />
        </td>
        <%
          count ++;
          if(count == 3){
            count = 0;
            %>
            </tr>
            <tr>
            <%
          }
        }
      }
      %>
  </tr>
</table>
