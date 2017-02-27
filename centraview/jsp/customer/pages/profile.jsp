<%--
 * $RCSfile: profile.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%
	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
	GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);

	Vector mocTypeList = new Vector();
	if (gml.get("mocTypeList") != null) {
    mocTypeList = (Vector) gml.get("mocTypeList");
  }
 	pageContext.setAttribute("mocTypeList", mocTypeList, PageContext.PAGE_SCOPE);
%>
<html:form action="/customer/update_profile.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="50%" valign="top" style="border-right: #cccccc 1px solid;">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.customerinfo"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.name"/>:</td>
          <td class="contentCell"><html:text property="entityName" styleId="entityName" styleClass="inputBox" size="35" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.website"/>:</td>
          <td class="contentCell"><html:text property="website" styleId="website" styleClass="inputBox" size="35" /></td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.primaryaddress"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.street1"/>:</td>
          <td class="contentCell"><html:text property="street1" styleId="street1" styleClass="inputBox" size="35" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.street2"/>:</td>
          <td class="contentCell"><html:text property="street2" styleId="street2" styleClass="inputBox" size="35" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.city"/>:</td>
          <td class="contentCell"><html:text property="city" styleId="city" styleClass="inputBox" size="35" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.state"/>:</td>
          <td class="contentCell"><html:text property="state" styleId="state" styleClass="inputBox" size="35" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.zipcode"/>:</td>
          <td class="contentCell"><html:text property="zipCode" styleId="zipCode" styleClass="inputBox" size="8" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.country"/>:</td>
          <td class="contentCell"><html:text property="country" styleId="country" styleClass="inputBox" size="35" /></td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.primarycontact"/></td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="3" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.firstname"/>:</td>
          <td class="contentCell"><html:text name="customerProfileForm" property="firstName" styleId="firstName" styleClass="inputBox" size="30" /></td>
        </tr>
        <tr>
          <td class="labelCell">MI:</td>
          <td class="contentCell"><html:text property="middleName" styleId="middleName" styleClass="inputBox" size="3" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.lastname"/>:</td>
          <td class="contentCell"><html:text property="lastName" styleId="lastName" styleClass="inputBox" size="30" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.customer.pages.title"/>:</td>
          <td class="contentCell"><html:text property="title" styleId="title" styleClass="inputBox" size="30" /></td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.contactmethods"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell">
            <html:select property="mocType1" styleId="mocType1" styleClass="inputBox">
              <html:options collection="mocTypeList" property="id" labelProperty="name" />
            </html:select>
          </td>
          <td class="contentCell"><html:text property="mocContent1" styleId="mocContent1" styleClass="inputBox" size="20"/></td>
          <td class="contentCell"><bean:message key="label.customer.pages.ext"/>:</td>
          <td class="contentCell"><html:text property="mocExt1" styleId="mocExt1" styleClass="inputBox" size="5"/></td>
        </tr>
        <tr>
          <td class="labelCell">
            <html:select property="mocType2" styleId="mocType2" styleClass="inputBox">
              <html:options collection="mocTypeList" property="id" labelProperty="name" />
            </html:select>
          </td>
          <td class="contentCell"><html:text property="mocContent2" styleId="mocContent2" styleClass="inputBox" size="20"/></td>
          <td class="contentCell"><bean:message key="label.customer.pages.ext"/>:</td>
          <td class="contentCell"><html:text property="mocExt2" styleId="mocExt2" styleClass="inputBox" size="5"/></td>
        </tr>
        <tr>
          <td class="labelCell">
            <html:select property="mocType3" styleId="mocType3" styleClass="inputBox">
              <html:options collection="mocTypeList" property="id" labelProperty="name" />
            </html:select>
          </td>
          <td class="contentCell"><html:text property="mocContent3" styleId="mocContent3" styleClass="inputBox" size="20"/></td>
          <td class="contentCell"><bean:message key="label.customer.pages.ext"/>:</td>
          <td class="contentCell"><html:text property="mocExt3" styleId="mocExt3" styleClass="inputBox" size="5"/></td>
        </tr>
        <tr>
          <td class="contentCell"><bean:message key="label.customer.pages.email"/>:</td>
          <td class="contentCell" colspan="3"><html:text property="email" styleId="email" styleClass="inputBox" size="34"/></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input type="submit" value="<bean:message key='label.value.requestchanges'/>" class="normalButton" />
    </td>
  </td>
</table>
</html:form>