<%--
 * $RCSfile: thread_list.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.support.supportfacade.*" %>
<%@ page import="com.centraview.support.thread.*" %>
<%@ page import="org.apache.struts.util.MessageResources" %>
<%! public MessageResources messages = MessageResources.getMessageResources("ApplicationResources"); %>
<% ThreadList DL = (ThreadList)request.getAttribute("threadlist"); %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="mainHeader"><span style="font-size: 8pt;"><bean:message key="label.support.threads"/></span></td>
    <td class="mainHeader" align="right">
      <app:cvbutton property="newthreadbutton" styleId="newthreadbutton" styleClass="normalButton" onclick="newThread();">
        <bean:message key="label.support.newthread"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <%
      Vector vecColumns = DL.getAllColumns();
      for (int i=0; i<vecColumns.size(); i++) {
        %><td class="listHeader"><span class="listHeader"><%= messages.getMessage( "ThreadList."+vecColumns.elementAt(i) ) %></span></td><%
      }
    %>
  </tr>
  <%
    int i = 0;
    if (DL.size() > 0) {
      Set listkey = DL.keySet();
      Iterator iter = listkey.iterator();
      String str = null;

      while (iter.hasNext()) {
        %><tr><%
        i++;
        str = (String)iter.next();
        ListElement ele = (ListElement)DL.get(str);
        Enumeration enum = vecColumns.elements();

        String rowClass = "tableRowOdd";
        if (i % 2 == 0) {
          rowClass = "tableRowEven";
        }else{
          rowClass = "tableRowOdd";
        }

        int colNum = 0;
        while (enum.hasMoreElements()) {
          %><td class="<%=rowClass%>"><%
          String strr = (String)enum.nextElement();
          ListElementMember sm = (ListElementMember)ele.get(strr);
          if (sm != null)  {

            if (sm.getLinkEnabled()) {
              %><a href="javascript:<%=sm.getRequestURL()%>" class="plainLink"><%=sm.getDisplayString()%></a><%
            }else{
              out.println(sm.getDisplayString());
            }
          } else {
            %>&nbsp;<%
          }
          %></td><%
        }
        %></tr><%
      }   // end while (iter.hasNext())
    }else{
      %>
      <tr>
        <td class="tableRowOdd" colspan="5" align="center"><strong><bean:message key="label.support.norecordsfound"/>.</strong></td>
      </tr>
      <%
    }
  %>
</table>