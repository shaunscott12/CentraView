<%--
 * $RCSfile: related_address.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<html:form action="/contacts/save_related_address">
<html:hidden property="operation" />
<html:hidden property="recordID" />
<html:hidden property="recordName" />
<html:hidden property="listType" />
<html:hidden property="listFor" />
<html:hidden property="addressid" />
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.street1"/></td>
    <td class="contentCell"><html:text property="street1" styleId="street1" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.street2"/>:</td>
    <td class="contentCell"><html:text property="street2" styleId="street2" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.city"/></td>
    <td class="contentCell"><html:text property="city" styleId="city" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.state"/>:</td>
    <td class="contentCell"><html:text property="state" styleId="state" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.zipcode"/>:</td>
    <td class="contentCell"><html:text property="zipcode" styleId="zipcode" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.country"/>:</td>
    <td class="contentCell"><html:text property="country" styleId="country" styleClass="inputBox" size="40" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.primaryaddress"/>:</td>
    <td class="contentCell">
      <html:radio property="isPrimary" value="YES"/><bean:message key="label.contacts.yes"/>
      <html:radio property="isPrimary" value="NO"/><bean:message key="label.contacts.no"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.taxjuris"/></td>
    <td class="contentCell">
      <html:select property="jurisdictionID" styleId="jurisdictionID" styleClass="inputBox" size="1">
        <html:option value="0">-- <bean:message key="label.contacts.select"/> --</html:option>
        <html:optionsCollection property="jurisdictionVec" value="id" label="name"/>
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
        <bean:message key="label.saveandnew"/>
      </html:submit>
      <html:reset styleClass="normalButton">
        <bean:message key="label.reset"/>
      </html:reset>
      <html:submit styleClass="normalButton" property="Cancel">
        <bean:message key="label.cancel"/>
      </html:submit>
    </td>
  </tr>
</table>
</html:form>