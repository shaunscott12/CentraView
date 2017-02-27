<%--
 * $RCSfile: entity_merge_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:form action="/administration/entity_merge_confirm">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.step"/> 3: <bean:message key="label.pages.administration.merge.mergefields"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.entityname"/>:</td>
    <td class="contentCell">
      <!-- Name Radio buttons -->
      <logic:iterate id="name" property="nameCollection" name="mergeEntityDetails">
        <html:radio property="nameRadio" value="value" idName="name"/> <bean:write name="name" property="label"/><br/>
      </logic:iterate>
      <!-- Custom Name Radio button -->
      <html:radio property="nameRadio" value="custom"/>
      <html:text property="customName" size="25" styleClass="inputBox"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.targetmarketinglist"/>:</td>
    <td class="contentCell">
      <html:select property="marketingListSelect" styleClass="inputBox" >
        <html:optionsCollection property="marketingListCollection" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.primaryid"/>:</td>
    <td class="contentCell">
      <html:select property="idSelect" styleClass="inputBox" >
        <html:optionsCollection property="idCollection" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell">ID2 (<bean:message key="label.pages.administration.merge.externalid"/>):</td>
    <td class="contentCell">
      <!-- External ID Radio Buttons -->
      <logic:iterate id="id2" property="id2Collection" name="mergeEntityDetails">
        <html:radio property="id2Radio" value="value" idName="id2"/> <bean:write name="id2" property="label"/><br/>
      </logic:iterate>
      <!-- Custom External ID Radio button -->
      <html:radio property="id2Radio" value="custom"/>
      <html:text property="customId2" size="4" styleClass="inputBox"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.primarycontact"/>:</td>
    <td class="contentCell">
      <!-- Primary Contact Select -->
      <html:select property="primaryContactSelect" styleClass="inputBox" >
        <html:optionsCollection property="individualNameCollection" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.source"/>:</td>
    <td class="contentCell">
      <!-- Source Radio Buttons -->
      <logic:iterate id="source" property="sourceCollection" name="mergeEntityDetails">
        <html:radio property="sourceRadio" value="value" idName="source"/> <bean:write name="source" property="label"/><br/>
      </logic:iterate>
      <!-- Custom Source Radio button -->
      <%--
        The value on the "sourceRadio" will always get set to sourceID even
        when using lookup That will serve as the hidden field that is typically used
      --%>
      <html:radio property="sourceRadio" value="custom" />
      <html:text property="customSourceName"  styleClass="inputBox" size="25" onchange="javascript:setSourceID();" />
      <!-- Lookup Button -->
      <html:button property="sourceButton" styleClass="normalButton" onclick="sourceLookup();">
        <bean:message key="label.lookup"/>
      </html:button>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.accountmanager"/>:</td>
    <td class="contentCell">
      <!-- Account Manager Radio Buttons -->
      <logic:iterate id="accountManager" property="accountManagerCollection" name="mergeEntityDetails">
        <html:radio property="accountManagerRadio" value="value" idName="accountManager"/> <bean:write name="accountManager" property="label"/><br/>
      </logic:iterate>
      <!-- Custom Account Manager Radio button -->
      <html:radio property="accountManagerRadio" value="custom" />
      <html:text property="customAccountManagerName"  styleClass="inputBox" size="25" readonly="true" />
      <!-- Lookup Button -->
      <html:button property="accountManagerButton" styleClass="normalButton" onclick="accountManagerLookup();">
        <bean:message key="label.lookup"/>
      </html:button>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.pages.administration.merge.accountteam"/>:</td>
    <td class="contentCell">
      <!-- Account Team Radio Buttons -->
      <logic:iterate id="accountTeam" property="accountTeamCollection" name="mergeEntityDetails">
        <html:radio property="accountTeamRadio" value="value" idName="accountTeam"/> <bean:write name="accountTeam" property="label"/><br/>
      </logic:iterate>
      <!-- Custom Account Team Radio button -->
      <html:radio property="accountTeamRadio" value="custom" />
      <html:text property="customAccountTeamName" styleClass="inputBox" size="25" readonly="true" />
      <!-- Lookup Button -->
      <html:button property="accountManagerButton" styleClass="normalButton" onclick="accountTeamLookup();">
        <bean:message key="label.lookup"/>
      </html:button>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.addresses"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <!-- Address List -->
  <logic:iterate id="address" property="addressCollection" name="mergeEntityDetails">
  <tr>
    <td class="labelCell">
      <html:multibox property="addressCheckBox"> <bean:write name="address" property="value"/> </html:multibox>
    </td>
    <td class="contentCell">
      <%-- The filter is set to false as a couple <br> are placed in the label --%>
      <bean:write name="address" property="label" filter="false" />
    </td>
  </tr>
  </logic:iterate>
</table>
<p/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.contactmethods"/></td>
  </tr>
</table>
<table border="0" cellpadding="5" cellspacing="0" class="formTable">
  <!-- Method of Contact List -->
  <logic:iterate id="moc" property="methodOfContactCollection" name="mergeEntityDetails">
  <tr>
    <td class="labelCell">
      <html:multibox property="methodOfContactCheckBox"> <bean:write name="moc" property="value"/> </html:multibox>
    </td>
    <td class="contentCell">
      <%-- The filter is set to false as a nbsp is placed in the label --%>
      <bean:write name="moc" property="label" filter="false" />
    </td>
  </tr>
  </logic:iterate>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader">
      <input name="button" type="button" class="normalButton" onClick="goToList()" value="&laquo; <bean:message key='label.value.back'/> " />
      <html:submit styleClass="normalButton" title="Submit the Merge Request"><bean:message key='label.value.next'/> &raquo;</html:submit>
    </td>
  </tr>
</table>
</html:form>
<script language="JavaScript" type="text/javascript">
	function setSource(name, id)
	{
		document.forms[0].sourceRadio.value = id;
		document.forms[0].customSourceName.value = name;
	}
	function setSourceID(){
		document.forms[0].sourceRadio.value = "-1";
	}

	function sourceLookup()
	{
		var mychild = c_openWindow('/SourceLookup.do', '', 400, 400,'');
	}

	<%-- Called by Account Manager Lookup --%>
	function setEmployee(individualLookupValues){
		firstName = individualLookupValues.firstName;
		id = individualLookupValues.individualID;
		middleName = individualLookupValues.middleName;
		lastName = individualLookupValues.lastName;
		title = individualLookupValues.title;

		document.forms[0].customAccountManagerName.readonly = false;
		document.forms[0].accountManagerRadio.value = id;
		document.forms[0].customAccountManagerName.value = firstName+" "+middleName+" "+lastName;
		document.forms[0].customAccountManagerName.readonly = true;
	}

	<%-- Called by Account Team Lookup --%>
	function setGrp(name,id)
	{
		document.forms[0].customAccountTeamName.disabled = false;
	    document.forms[0].accountTeamRadio.value = id;
    	document.forms[0].customAccountTeamName.value = name;
	}

	function accountManagerLookup()
	{
		var mychild = c_openWindow('/EmployeeLookup.do', '', 400, 400,'');
	}

	function accountTeamLookup()
	{
		var mychild = c_openWindow('/GroupLookup.do', '', 400, 400,'');
	}

	function goToList()
	{
		c_goTo('/administration/display_merge_search_results.do');
	}
</script>