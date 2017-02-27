<%--
 * $RCSfile: footer.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:28 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
<%@ page import="java.util.Date" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.centraview.common.CentraViewConfiguration" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
  GregorianCalendar calendar = new GregorianCalendar();
  long time = calendar.getTimeInMillis();
  Date date = new Date(time);
  SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEEEEEEEEE, MMMMM dd, yyyy - hh:mm aaa");
  String formattedDate = dateFormat.format(date);
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td><html:img page="/images/clear.gif" width="1" height="1" alt="" /></td>
  </tr>
  <tr>
    <td>
      <table border="0" cellspacing="0" cellpadding="0" class="footerTable">
        <tr valign="top">
          <td class="footerLeft">
            <span class="footerText">
              <%=formattedDate%>&nbsp;<%=(calendar.getTimeZone()).getDisplayName(true, java.util.TimeZone.SHORT)%>&nbsp;|&nbsp;IP: <strong><%=(String)request.getRemoteAddr()%></strong> - version <%=CentraViewConfiguration.getVersion()%>
            </span>
          </td>
          <td class="footerRight">
            <span class="footerText">
              Copyright &copy; 2004&nbsp;|&nbsp;Rights Reserved
            </span>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
