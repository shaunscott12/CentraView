<%
/**
 * $RCSfile: user_profile.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:45 $ - $Author: mcallist $
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
 */
%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*"%>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%
	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
	GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
	Vector mocTypeList = new Vector();
	if (gml.get("mocTypeList") != null)
	mocTypeList = (Vector) gml.get("mocTypeList");
 	pageContext.setAttribute("mocTypeList", mocTypeList, PageContext.PAGE_SCOPE);
%>
<script>
<!--
	function gotosave(param)
	{
		window.document.individualForm.action="<html:rewrite page='/preference/save_user_profile.do' />?buttonpress="+param;
		window.document.individualForm.target="_parent";
		window.document.individualForm.submit();
	}
	function setSource(lookupValues)
	{
	  var sourceId = document.getElementById('sourceId');
	  var sourceName = document.getElementById('sourceName');
	  sourceId.value = lookupValues.idValue;
	  sourceName.value = lookupValues.Name;
	}
	function goHome()
	{
		window.location.href="<%=request.getContextPath()%>/home.do";
	}
-->
</script>
<html:form action="/preference/user_profile.do">
<table width="100%" height="100%" border="0" align="center" cellpadding="1" cellspacing="1">
	 <tr>
	   <td colspan="2" class="sectionHeader"><bean:message key="label.preference.userprofile"/></td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.name"/>: </td>
  	 <td class="contentCell">
			<html:text property="firstName" size="10" styleClass="inputBox"/>
			<html:text property="middleInitial" size="2" styleClass="inputBox"/>
			<html:text property="lastName" size="10" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.title"/>: </td>
  	 <td class="contentCell">
			<html:text property="title" size="30" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.address1"/>: </td>
	   <td class="contentCell">
			<html:text property="street1" size="30" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.address2"/>: </td>
	   <td class="contentCell">
	   	<html:text property="street2" size="30" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.city"/>: </td>
	   <td class="contentCell">
		 	<html:text property="city" size="30" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.state"/>: </td>
	   <td class="contentCell">
		 	<html:text property="state" size="30" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
	   <td class="labelCell"><bean:message key="label.preference.zipcode"/>: </td>
	   <td class="contentCell">
		 	<html:text property="zip" size="8" styleClass="inputBox"/>
	   </td>
	 </tr>
	 <tr>
		 <td class="labelCell"><bean:message key="label.preference.country"/>: </td>
		 <td class="contentCell">
			<html:text property="country" size="30" styleClass="inputBox"/>
		 </td>
	 </tr>
   <tr>
      <td class="labelCell"><bean:message key="label.preference.source"/>:</td>
      <td class="contentCell">
        <html:hidden property="sourceId" styleId="sourceId"/>
        <html:text property="sourceName" styleId="sourceName" styleClass="inputBox" size="17" />
        <input type="button" name="lookup" value="<bean:message key='label.value.lookup'/>" class="normalButton" onclick="c_lookup('Source')">
    </td>
    </tr>
	 <tr>
	   <td colspan="2" class="sectionHeader"><bean:message key="label.preference.contactmethods"/></td>
	 </tr>
	 <tr>
	   <td colspan="2">
		   <table border="0" cellspacing="0" cellpadding="0" width="350">
					 <tr>
						 <td class="labelCell">
							<html:select property="mocType1" styleClass="inputBox">
								<html:options collection="mocTypeList" property="id" labelProperty="name"/>
							</html:select>
				   	 </td>
				 		<td class="contentCell"><html:text property="mocContent1" styleClass="inputBox" size="20"/></td>
						<td class="labelCell"><bean:message key="label.preference.ext"/>:</td>
						<td class="contentCell"><html:text property="mocExt1" styleClass="inputBox" size="5"/></td>
					</tr>
					<tr>
						<td class="labelCell">
							<html:select property="mocType2" styleClass="inputBox">
								<html:options collection="mocTypeList" property="id" labelProperty="name"/>
							</html:select>
						</td>
						<td class="contentCell"><html:text property="mocContent2" styleClass="inputBox" size="20"/></td>
						<td class="labelCell"><bean:message key="label.preference.ext"/>:</td>
						<td class="contentCell"><html:text property="mocExt2" styleClass="inputBox" size="5"/></td>
					</tr>
					<tr>
						<td class="labelCell">
							<html:select property="mocType3" styleClass="inputBox">
								<html:options collection="mocTypeList" property="id" labelProperty="name"/>
							</html:select>
						</td>
						<td class="contentCell"><html:text property="mocContent3" styleClass="inputBox" size="20"/></td>
						<td class="labelCell"><bean:message key="label.preference.ext"/>:</td>
						<td class="contentCell"><html:text property="mocExt3" styleClass="inputBox" size="5"/></td>
					</tr>
					<tr>
						<td class="labelCell"><bean:message key="label.preference.email"/>:</td>
						<td class="contentCell" colspan="3"><html:text property="email" styleClass="inputBox" size="34"/></td>
					</tr>
				</table>
		 </td>
	 </tr>
	 <tr>
		   <td colspan="2">&nbsp;</td>
	 </tr>
	 <tr>
		   <td colspan="2"><bean:message key="label.preference.logininfo"/></td>
	 </tr>
	 <tr class="labelCell">
		   <td width="17%"><bean:message key="label.preference.username"/>: </td>
		   <td>
			   <html:text property="userName" size="25" styleClass="inputBox" readonly="true"/>
		   </td>
	 </tr>
	 <tr class="labelCell">
	   <td><bean:message key="label.preference.oldpassword"/>: </td>
	   <td>
			 <html:password property="oldpassword" styleClass="inputBox" size="25"/>
       <bean:message key="label.preference.passwordchange"/>.
		 </td>
	 </tr>
	 <tr class="labelCell">
		   <td><bean:message key="label.preference.newpassword"/>: </td>
		   <td>
		     <html:password property="newpassword" styleClass="inputBox" size="25"/>
		   </td>
	 </tr>
	 <tr class="labelCell">
		   <td><bean:message key="label.preference.confirmpassword"/>: </td>
		   <td>
				<html:password property="confirmpassword" styleClass="inputBox" size="25"/>
		   </td>
	 </tr>
	 <tr height="11">
		 <td colspan="2" class="buttonRow">
				<app:cvbutton property="save" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="gotosave('save')">
					<bean:message key="label.save"/>
				</app:cvbutton>
				<app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="goHome()">
					<bean:message key="label.cancel"/>
				</app:cvbutton>
		 </td>
	 </tr>
</table>
<html:hidden property="addressId"/>
<html:hidden property="individualId"/>
<html:hidden property="mocId1" />
<html:hidden property="mocId2"/>
<html:hidden property="mocId3"/>
</html:form>
