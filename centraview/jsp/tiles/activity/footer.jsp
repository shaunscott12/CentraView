<%--
 * $RCSfile: footer.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:53 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.activity.ActivityForm" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%
  String strActivityID = (String)((ActivityForm) request.getAttribute("activityform")).getActivityID();
  int activityID = 0;
  if (strActivityID != null && !strActivityID.equals("") && !strActivityID.equals("null")) {
    activityID = Integer.parseInt(strActivityID);
  }
%>
<table border="0" cellspacing="0" cellpadding="3" width="100%">
  <tr>
    <td class="mainHeader">
      <app:cvsubmit property="saveandclose" styleClass="normalButton" onclick="act_checkAddEditSchedule('close');">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvsubmit property="saveandnew" styleClass="normalButton" onclick="act_checkAddEditSchedule('new');">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <input type="reset" value="<bean:message key='label.administration.resetfields'/>" class="normalButton">
      <html:cancel  styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel"/>
      </html:cancel>
      <% if (activityID != 0){ %>
      <app:cvsubmit property="delete" styleClass="normalButton" onclick="act_deleteActivity();" recordID="<%=activityID%>" modulename="Activities" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/>
      </app:cvsubmit>
      <% } %>
    </td>
  </tr>
</table>