<%--
 * $RCSfile: individual_merge_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<html:form action="/administration/individual_merge_confirm">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.step"/> 3: <bean:message key="label.pages.administration.merge.mergefields"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.pages.administration.merge.individualname"/>:</td>
    <td class="contentCell">
      <!-- Name Radio buttons -->
      <logic:iterate id="name" property="nameCollection" name="mergeIndividualDetails">
        <html:radio property="nameRadio" value="value" idName="name"/> <bean:write name="name" property="label"/><br/>
      </logic:iterate>
      <!-- Custom Name Radio button -->
      <html:radio property="nameRadio" value="custom"/>
      <html:text property="customFirst" styleClass="inputBox" size="25"/>&nbsp;
      <html:text  styleClass="inputBox" property="customMiddle" size="2"/>&nbsp;
      <html:text  styleClass="inputBox" property="customLast" size="25"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.pages.administration.merge.targetmarketinglist"/>:</td>
    <td class="contentCell">
      <html:select property="marketingListSelect" styleClass="inputBox">
        <html:optionsCollection property="marketingListCollection" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.pages.administration.merge.primaryid"/>:</td>
    <td class="contentCell">
      <html:select property="idSelect" styleClass="inputBox">
        <html:optionsCollection property="idCollection" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top">ID2 (<bean:message key="label.pages.administration.merge.externalid"/>):</td>
    <td class="contentCell">
      <!-- External ID Radio Buttons -->
      <logic:iterate id="id2" property="id2Collection" name="mergeIndividualDetails">
        <html:radio property="id2Radio" value="value" idName="id2"/> <bean:write name="id2" property="label"/><br/>
      </logic:iterate>
      <!-- Custom External ID Radio button -->
      <html:radio property="id2Radio" value="custom"/>
      <html:text property="customId2" styleClass="inputBox" size="4"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.pages.administration.merge.associatedentity"/>:</td>
    <td class="contentCell">
      <!-- Primary Contact Select -->
      <html:select property="entitySelect" styleClass="inputBox">
        <html:optionsCollection property="entityNameCollection" value="value" label="label"/>
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="labelCell" valign="top"><bean:message key="label.pages.administration.merge.source"/>:</td>
    <td class="contentCell">
      <!-- Source Radio Buttons -->
      <logic:iterate id="source" property="sourceCollection" name="mergeIndividualDetails">
        <html:radio property="sourceRadio" value="value" idName="source"/> <bean:write name="source" property="label"/><br/>
      </logic:iterate>
      <!-- Custom Source Radio button -->
      <%--
        The value on the "sourceRadio" will always get set to sourceID even
        when using lookup That will serve as the hidden field that is typically used
      --%>
      <html:radio property="sourceRadio" value="custom" />
      <html:text property="customSourceName" styleId="customSourceName" size="25"  styleClass="inputBox" />
      <!-- Lookup Button -->
      <html:button property="sourceButton" styleClass="normalButton" onclick="sourceLookup();">
        <bean:message key="label.lookup"/>
      </html:button>
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
  <logic:iterate id="address" property="addressCollection" name="mergeIndividualDetails">
  <tr>
    <td class="labelCell" valign="top">
      <html:multibox property="addressCheckBox"> <bean:write name="address" property="value"/> </html:multibox>
    </td>
    <td class="contentCell">
      <%-- The filter is set to false as a couple <br> are placed in the label --%>
      <bean:write name="address" property="label" filter="false" />
    </td>
  </tr>
  </logic:iterate>
  <c:if test="${empty addressCollection}">
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.merge.noaddressestomerge"/>.</td>
  </tr>
  </c:if>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.contactmethods"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <!-- Method of Contact List -->
  <logic:iterate id="moc" property="methodOfContactCollection" name="mergeIndividualDetails">
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
  <c:if test="${empty methodOfContactCollection}">
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.merge.nocontactmethodstomerge"/>.</td>
  </tr>
  </c:if>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="button" type="button" class="normalButton" onClick="goToList()" value="&laquo; <bean:message key='label.value.back'/> " />
      <html:submit styleClass="normalButton" title="Submit the Merge Request"> <bean:message key='label.value.next'/> &raquo;</html:submit>
    </td>
  </tr>
</table>
</html:form>
<script language="JavaScript" type="text/javascript">
  function setSource(lookupValues)
  {
    document.getElementById('customSourceName').disabled = false;
    document.mergeIndividualDetails.sourceRadio.value = lookupValues.idValue;
    document.getElementById('customSourceName').value = lookupValues.Name;
  }

  function sourceLookup()
  {
    c_lookup('Source');
  }

  function goToList()
  {
    c_goTo('/administration/display_merge_search_results.do');
  }
</script>