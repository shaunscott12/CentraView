<%--
 * $RCSfile: bottom_contact_method.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
%>
<html:form action="/contacts/save_contact_method">
<html:hidden property="operation" />
<html:hidden property="recordID" />
<html:hidden property="recordName" />
<html:hidden property="listType" />
<html:hidden property="listFor" />
<html:hidden property="mocid" />
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.type"/>:</td>
    <td class="contentCell">
      <%
        Vector mocVec = (Vector)gml.get("MOC");
        pageContext.setAttribute("mocVec", mocVec, PageContext.PAGE_SCOPE);
      %>
      <html:select property="select" styleId="select" styleClass="inputBox"  onchange="checkEmail();">
        <html:options collection="mocVec" property="id" labelProperty="name"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.content"/>:</td>
    <td class="contentCell"><html:text property="text3" styleId="text3" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.notes"/>:</td>
    <td class="contentCell"><html:text property="text4" styleId="text4" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.syncas"/>:</td>
    <td class="contentCell">
      <%
        Vector syncAsVec = (Vector)gml.get("SyncAs");
        pageContext.setAttribute("syncAsVec", syncAsVec, PageContext.PAGE_SCOPE);
      %>
      <html:select property="syncas" styleId="syncas" styleClass="inputBox" onchange="checkSelect();">
        <html:options collection="syncAsVec" property="strid" labelProperty="name"/>
      </html:select>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="headerBG">
      <html:submit styleClass="normalButton" property="saveandclose">
        <bean:message key="label.saveandclose" />
      </html:submit>
      <html:submit styleClass="normalButton" property="saveandnew">
        <bean:message key="label.saveandnew" />
      </html:submit>
      <html:reset styleClass="normalButton">
        <bean:message key="label.reset" />
      </html:reset>
      <html:submit styleClass="normalButton" property="cancel" onclick="goToPreviousPage();">
        <bean:message key="label.cancel" />
      </html:submit>
    </td>
  </tr>
</table>
</html:form>