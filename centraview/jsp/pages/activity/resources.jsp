<%--
 * $RCSfile: resources.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.activity.ActivityForm" %>
<%
  ActivityForm activityform = (ActivityForm)request.getAttribute("activityform");
  Vector vecResource = (Vector)activityform.getActivityResourcevector();
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="49%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.availableressources"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              Vector resourceVec = new Vector();
              if (request.getAttribute("resourceVec") != null) {
                resourceVec = (Vector) request.getAttribute("resourceVec");
              }
              pageContext.setAttribute("resourceVec", resourceVec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityResourceAll" styleId="activityResourceAll" size="16" multiple="true" style="width:200px;" styleClass="inputBox">
              <html:options collection="resourceVec" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
    <td width="2%" align="center" valign="middle">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td>
            <app:cvbutton property="changeLookup2" styleId="changeLookup2" styleClass="normalButton" style="width:65px;" onclick="act_addResource()">
              <bean:message key="label.add"/> &raquo;
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td>
            <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" style="width:65px;" onclick="act_removeResource()">
              &laquo; <bean:message key="label.remove"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td width="49%" align="center" valign="top">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.selectedressources"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              if (vecResource == null) {
                vecResource = new Vector();
              }
              pageContext.setAttribute("vecResource", vecResource, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityResourceSelected" styleId="activityResourceSelected" size="16" multiple="true" style="width:200px;" styleClass="inputBox">
              <html:options collection="vecResource" property="strid" labelProperty="name"/>
            </html:select>
            </select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>