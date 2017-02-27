<%--
 * $RCSfile: error_page.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:49 $ - $Author: mcallist $
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

<%@ page language="java" isErrorPage="true" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="label.pages.applicationerror"/></h2>
<%-- Error Messages --%>
<logic:messagesPresent>
  <html:messages id="error">
    <bean:write name="error" filter="false"/><br/>
  </html:messages>
</logic:messagesPresent>
<pre>
  <% if (exception != null) { %>
    <%exception.printStackTrace(new java.io.PrintWriter(out));%>
  <% } else if ((Exception)request.getAttribute("javax.servlet.error.exception") != null) {
    ((Exception)request.getAttribute("javax.servlet.error.exception")).printStackTrace(new java.io.PrintWriter(out));
  } else { %>
    <bean:define id="exception2" name="org.apache.struts.action.EXCEPTION" type="java.lang.Exception"/>
    <logic:notEmpty name="exception2">
      <% exception2.printStackTrace(new java.io.PrintWriter(out));%>
    </logic:notEmpty>
</pre>
    <%-- only show this if no error messages present --%>
    <logic:messagesNotPresent>
      <bean:message key="errors.none"/>
    </logic:messagesNotPresent>
<%}%>