<%--
 * $RCSfile: threads_list.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 1.0.0 (the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%! public MessageResources messages = MessageResources.getMessageResources("ApplicationResources"); %>
<%@ page import="java.util.*,com.centraview.common.*,com.centraview.support.supportfacade.*,com.centraview.support.thread.*,org.apache.struts.util.MessageResources" %>
<% String tempTicketId = request.getParameter("ticketId"); %>
<script language="JavaScript1.2">
  function showNewThread()
  {
    c_goTo('/customer/new_thread.do?ticketId=<%=tempTicketId%>');
  }

  function showviewthread(ticketid)
  {
    window.document.forms[0].action="<bean:message key='label.url.root' />/CV_ViewThread.do?TICKETID=211";
    window.document.forms[0].target="_self";
    window.document.forms[0].submit();
    return(false);
  }
</script>
<% ThreadList DL = (ThreadList)request.getAttribute("threadlist"); %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="mainHeader"><bean:message key="label.customer.pages.support.threads"/></td>
    <td class="mainHeader" align="right">
      <app:cvbutton property="newthreadbutton" styleClass="normalButton" onclick="showNewThread();" >
        <bean:message key="label.customer.pages.support.newthread"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <%
      Vector vecColumns = DL.getAllColumns();
      for (int i=0; i<vecColumns.size(); i++) {
        %><td nowrap class="listHeader"><span class="listHeader"><%=(String)vecColumns.elementAt(i)%></span></td><%
      }
    %>
  </tr>
  <%
    int i = 0 ;
    if (DL.size() > 0) {
      Set listkey = DL.keySet();
      Iterator iter = listkey.iterator();
      String str = null;

      while (iter.hasNext()) {
        %><tr><%
        i++;
        str = (String)iter.next();
        ListElement ele  = (ListElement)DL.get(str);

        Enumeration enum =  vecColumns.elements();

        String css = "tableRowOdd";
        if (i % 2 == 0) {
          css = "tableRowEven";
        }else{
          css = "tableRowOdd";
        }


        while (enum.hasMoreElements()) {
          %><td class="<%=css%>"><%
          String strr = (String)enum.nextElement();
          ListElementMember sm = (ListElementMember)ele.get(strr);

          if (sm != null) {
            if (sm.getLinkEnabled())  {
              %><a href="<html:rewrite page="<%=sm.getRequestURL()%>"/>" class="plainLink"><%=sm.getDisplayString()%></a>&nbsp;<%
            }else{
              %><%=sm.getDisplayString()%>&nbsp;<%
            }
          }else{
            %>&nbsp;<%
          }
        }
        %></tr><%
      }   // end while (iter.hasNext())
    }else{
      %>
      <tr>
        <td class="tableRowOdd" colspan="5" align="center"><strong><bean:message key="label.customer.pages.support.norecordsfound"/>.</strong></td>
      </tr>
      <%
    }   // end if (DL.size() > 0)
  %>
  </tr>
</table>