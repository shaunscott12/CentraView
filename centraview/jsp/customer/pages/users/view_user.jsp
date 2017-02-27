<%--
 * $RCSfile: view_user.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:45 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
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
<html:form action="/customer/user_change_request">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.users.userdetails"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.name"/>:</td>
    <td class="contentCell">
    	<html:text property="individualName" styleClass="inputBox" size="35"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.title"/>:</td>
    <td class="contentCell">
    	<html:text property="title" styleClass="inputBox" size="35"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.address1"/>:</td>
    <td class="contentCell">
    	<html:text property="street1" styleClass="inputBox" size="35"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.address2"/>:</td>
    <td class="contentCell">
    	<html:text property="street2" styleClass="inputBox" size="35"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.city"/>:</td>
    <td class="contentCell">
    	<html:text property="city" styleClass="inputBox" size="35"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.state"/>:</td>
    <td class="contentCell">
      <html:text property="state" styleClass="inputBox"  size="35"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.zipcode"/>:</td>
    <td class="contentCell">
      <html:text property="zipCode" styleClass="inputBox" size="5"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.country"/>:</td>
    <td class="contentCell">
	    <html:text property="country" styleClass="inputBox" size="35"/>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.users.contactmethods"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
	<tr>
		<td class="labelCell">
			<html:select property="mocType1" styleClass="textBoxinputBox">
				<html:options collection="mocTypeList" property="id" labelProperty="name"/>
			</html:select>
		</td>
		<td class="contentCell"><html:text property="mocContent1" styleClass="inputBox" size="20"/></td>
		<td class="labelCell"><bean:message key="label.customer.pages.users.ext"/>:</td>
		<td class="contentCell"><html:text property="mocExt1" styleClass="inputBox" size="5"/></td>
	</tr>
	<tr>
		<td class="labelCell">
			<html:select property="mocType2" styleClass="textBoxinputBox">
				<html:options collection="mocTypeList" property="id" labelProperty="name"/>
			</html:select>
		</td>
		<td class="contentCell"><html:text property="mocContent2" styleClass="inputBox" size="20"/></td>
		<td class="labelCell"><bean:message key="label.customer.pages.users."/><bean:message key="label.customer.pages.users.ext"/>:</td>
		<td class="contentCell"><html:text property="mocExt2" styleClass="inputBox" size="5"/></td>
	</tr>
	<tr>
		<td class="labelCell">
			<html:select property="mocType3" styleClass="textBoxinputBox">
				<html:options collection="mocTypeList" property="id" labelProperty="name"/>
			</html:select>
		</td>
		<td class="contentCell"><html:text property="mocContent3" styleClass="inputBox" size="20"/></td>
		<td class="labelCell"><bean:message key="label.customer.pages.users.ext"/>:</td>
		<td class="contentCell"><html:text property="mocExt3" styleClass="inputBox" size="5"/></td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.customer.pages.users.email"/>:</td>
		<td class="contentCell" colspan="3"><html:text property="email" styleClass="inputBox" size="34"/></td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.users.logininfo"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.username"/>:</td>
    <td class="contentCell">
    	<html:text property="username" styleClass="inputBox" size="35"/>
    	<html:hidden property="userID" />
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.oldpassword"/>:</td>
    <td class="contentCell"><html:password property="oldPassword" styleClass="inputBox" size="35"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.newpassword"/>:</td>
    <td class="contentCell"><html:password property="newPassword" styleClass="inputBox" size="35"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.customer.pages.users.confirmpassword"/> :</td>
    <td class="contentCell"><html:password property="newPasswordConf" styleClass="inputBox" size="35"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input type="submit" name="Submit" value="<bean:message key='label.value.requestchanges'/>" class="normalButton" />
      <input type="button" name="Submit2" value="<bean:message key='label.value.cancel'/>" class="normalButton" onclick="c_goTo('/customer/user_list.do');" />
    </td>
  </tr>
</table>
</html:form>