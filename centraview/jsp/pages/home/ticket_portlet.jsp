<%--
 * $RCSfile: ticket_portlet.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="com.centraview.support.common.*,com.centraview.support.ticket.*,com.centraview.support.faq.*" %>
<%@ page import="com.centraview.calendar.*, java.util.*, java.text.*" %>
<%@ page import="com.centraview.activity.*, com.centraview.common.*" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<!-- Support Ticket -->
<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader"><bean:message key="label.home.supportticket"/></td>
    <td align="right" class="sectionHeader">
      <input class="normalButton" type="button" value="<bean:message key='label.home.newticket'/>" id="button7" name="button7" onclick="c_openWindow('/support/new_ticket.do', 'nti', 750, 350, '');" />
    </td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
<%
  UserObject userobject =(UserObject) session.getAttribute("userobject");
  int indvID = userobject.getIndividualID();
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
  // Any Open Tickets assigned to the logged in User
  String query = "ADVANCE: select ticketid,subject from ticket,supportstatus where ticket.status =  supportstatus.statusid and supportstatus.name = 'Open' AND assignedto = "+indvID;
  TicketList tl = lg.getTicketList(indvID, 0, Integer.MAX_VALUE, query, "ticketid");
  if (tl.size() > 0) {
    Set listkey = tl.keySet();
    Iterator it = listkey.iterator();
    String rowClass = "tableRowOdd";
    int count = 0;
    while (it.hasNext()) {
      count++;
      rowClass = (count % 2 == 0) ? "tableRowEven" : "tableRowOdd";
      %><tr><td class="<%=rowClass%>"><%
      try {
        String str = (String)it.next();
        StringMember sm = null;
        ListElement ele = (ListElement)tl.get(str);
        sm = (StringMember)ele.get("Subject");
        int rowID = ((Integer)((IntMember)ele.get("TicketID")).getMemberValue()).intValue();
        %><a href="javascript:c_openWindow('/support/view_ticket.do?TYPEOFOPERATION=EDIT&rowId=<%=rowID%>&listId=<%=tl.getListID()%>','Ticket',750,550,'scrollbars=yes,resizable=yes')" class="plainLink"><%=sm.getDisplayString()%></a><%
      }catch(Exception e){
        %><span class="errorText"><bean:message key="label.home.errorretreivingprojecttaskitem"/>.</span><%
        e.printStackTrace();
      }
      %></td></tr><%
    }
  }else{
    %><tr><td class="tableRowOdd"><bean:message key="label.home.nooutstandingtickets"/>.</td></tr><%
  }
%>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>