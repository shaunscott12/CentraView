<%--
 * $RCSfile: entity_merge_confirm.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.step"/> 4: <bean:message key="label.pages.administration.merge.confirmrecord"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.entityname"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="entityName" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.marketinglist"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="marketingList" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.primaryid"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="id" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell">ID2 (<bean:message key="label.pages.administration.merge.externalid"/>):</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="id2" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.primarycontact"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="primaryContactName" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.source"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="sourceName" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.accountmanager"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="accountManagerName" scope="request" ignore="true"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.accountteam"/>:</td>
    <td class="contentCell">
      <bean:write name="mergedEntity" property="accountTeamName" scope="request" ignore="true"/>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.addresses"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <!-- Address List -->
  <c:forEach var="address" items="${requestScope.mergedEntity.addressCollection}" >
  <tr>
    <td class="contentCell">
      <c:out value="${address}" escapeXml="false" default="&nbsp;" />
    </td>
  </tr>
  </c:forEach>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.contactmethods"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <!-- Method of Contact List -->
  <c:forEach var="moc" items="${requestScope.mergedEntity.methodOfContactCollection}" >
  <tr>
    <td class="contentCell">
      <c:out value="${moc}" escapeXml="false" default="&nbsp;" />
    </td>
  </tr>
  </c:forEach>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader">
      <input name="button" type="button" class="normalButton" onClick="goToList();" value="&laquo; <bean:message key='label.value.back'/> " />
      <input name="accept" type="button" class="normalButton" value="<bean:message key='label.value.accept'/>" onClick="acceptMerge();" />
    </td>
  </tr>
</table>
<script language="JavaScript" type="text/javascript">
	function acceptMerge()
	{
		c_goTo('/administration/entity_merge.do');
	}

	function goToList()
	{
		c_goTo('/administration/entity_merge_detail.do');
	}
</script>