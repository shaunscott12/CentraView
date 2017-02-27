<%--
 * $RCSfile: license_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<script language="JavaScript" type="text/javascript">
<!--
	function gotosave(param)
	{
		window.document.forms[1].action="/centraview/administration/save_license.do?buttonpress="+param;
		window.document.forms[1].submit();
	}
//-->
</script>

<html:form action="/administration/license.do">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr valign="top">
<td colspan=2 bgcolor="#345788"><html:errors/></td>
</tr>
</table>

<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr height="10" valign="top">
	<td colspan="3">
	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
		  <td width="11" colspan="3"><html:img page="/images/spacer.gif" width="1" height="11" /></td>
		</tr>
		<tr>
		  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /><html:img page="/images/spacer.gif" width="1" height="1" /></td>
		<td width="100%">
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
			  <tr height="17">
				<td class="popupTableHead" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
				<td class="sectionHeader"><bean:message key="label.administration.licenseinfo"/></td>
			  </tr>
			</table>
			<table width="100%" border="0" cellpadding="5" cellspacing="0">
			  <tr>
				<td width="9%" valign="top" class="labelCell"><bean:message key='label.value.expiration'/>:</td>
				<td width="91%" align="left" valign="top" class="contentCell"><c:out value="${licenseForm.map.expirationDate}" /></td>
			  </tr>
			  <tr>
				<td class="labelCell"><bean:message key="label.administration.currentkey"/>:</td>
				<td class="contentCell"><c:out value="${licenseForm.map.licenseKey}" /></td>
			  </tr>
			  <tr>
				<td class="labelCell"><bean:message key="label.administration.currentstatus"/>:</td>
				<td class="contentCell"><c:out value="${licenseForm.map.licenseStatus}" /></td>
			  </tr>
			  <tr>
				<td class="labelCell"><bean:message key="label.administration.numberofusers"/>:</td>
				<td class="contentCell"><c:out value="${licenseForm.map.numberOfUsers}" /> <bean:message key="label.administration.concurrent"/></td>
			  </tr>
			  <tr>
				<td class="labelCell"><bean:message key="label.administration.lastverified"/>:</td>
				<td class="contentCell"><c:out value="${licenseForm.map.lastVerifiedDate}" />
				<app:cvbutton property="verify" styleClass="normalButton" onclick="gotosave('verify')">
          <bean:message key="label.administration.verifynow"/>
				</app:cvbutton>
				</td>
			  </tr>
			</table>
			<table border="0" cellspacing="0" cellpadding="0" width="100%">
			  <tr height="17">
				<td width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
				<td class="sectionHeader"><bean:message key="label.administration.licensesettings"/></td>
			  </tr>
			</table>
			<table width="100%" border="0" cellpadding="5" cellspacing="0">
			  <tr>
				<td width="9%" valign="top" class="labelCell"><bean:message key="label.administration.key"/>:</td>
				<td width="91%" align="left" valign="top" class="contentCell">
					<html:text property="licenseKey" styleClass="inputBox" size="32"/>
				</td>
			  </tr>
			  <tr>
				<td class="buttonRow">&nbsp;</td>
				<td class="buttonRow">
				<app:cvbutton property="save" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="gotosave('save')">
					<bean:message key="label.save"/>
				</app:cvbutton>
				<input name="Submit2" type="button" class="normalButton" value="<bean:message key='label.value.cancel'/>"></td>
			  </tr>
			</table></td>
		  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /><html:img page="/images/spacer.gif" width="1" height="1" /></td>
		</tr>
		<tr>
		  <td width="11" colspan="3"><html:img page="/images/spacer.gif" width="1" height="11" /></td>
		</tr>
	  </table>
	</td>
  </tr>
</table>
<html:hidden property="licenseID"/>
<html:hidden property="lastVerifiedDate"/>
<html:hidden property="licenseKey"/>
<html:hidden property="licenseVerification"/>
</html:form>