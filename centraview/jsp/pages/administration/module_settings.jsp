<%--
 * $RCSfile: module_settings.jsp,v $    $Revision: 1.5 $  $Date: 2005/09/01 18:34:43 $ - $author$
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.centraview.com/opensource/license.html
 *
 * Software distributed under the License is distributed on an "aS IS"
 * basis, WITHOUT WaRRaNTY OF aNY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is: CentraView Open Source.
 *
 * The developer of the Original Code is CentraView.  Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; all Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.administration.common.AdministrationConstantKeys" %>
<%
  String submodule = (request.getAttribute("typeofsubmodule") != "") ? (String)request.getAttribute("typeofsubmodule") : "Contacts";
  String oppor = (submodule.equals("Sales")) ? "Opportunity" : "";
%>
<input type="hidden" name="submodule" value="<%=submodule%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><%=submodule%> <bean:message key="label.pages.administration.modulesettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formtable">
  <tr>
    <td class="labelCell" colspan="2">
      <span class="boldText">
       <%=submodule%> <bean:message key="label.pages.administration.modulesettingsedit"/> <bean:message key="label.pages.administration.modulesettingsedit2"/>
      </span>
    </td>
  </tr>
  <%
    if ((submodule.equals("Contacts")) || (submodule.equals("Activities")) || (submodule.equals("Notes")) ||
        (submodule.equals("File")) || (submodule.equals("Sales")) || (submodule.equals("Marketing")) ||
        (submodule.equals("Projects")) || (submodule.equals("Support")) || (submodule.equals("Accounting")) ||
        (submodule.equals("HR")) ){
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/default_view.do"/>?sourcefor=<%=submodule%>&setting=Default View">
<bean:message key="label.pages.administration.defaultviews"/></a></td>
        <td class="contentCell">
<bean:message key="label.pages.administration.setdefaultview"/><%=submodule%>
<bean:message key="label.pages.administration.module"/> .
<bean:message key="label.pages.administration.overridesetting"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/list_custom_view.do" />?module=<%=submodule%>"><bean:message key="label.pages.administration.customviews"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.managecustomviews"/><%=submodule%>
<bean:message key="label.pages.administration.module"/> .</td>
      </tr>
      <%

      if (! ((submodule.equals("Activities")) || (submodule.equals("Notes")) || (submodule.equals("File")) )) {
        %>
        <tr>
          <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/list_custom_fields.do" />?module=<%=submodule%>"><bean:message key="label.pages.administration.customfields"/></a></td>
          <td class="contentCell"><bean:message key="label.pages.administration.managecustomfields"/> <%=submodule%> <bean:message key="label.pages.administration.module"/>.</td>
        </tr>
        <%
      }

      if (! ((submodule.equals("Activities")) || (submodule.equals("Notes")) || (submodule.equals("File")) ||
            (submodule.equals("Marketing")) || (submodule.equals("Projects")) || (submodule.equals("Support")) ||
            (submodule.equals("Accounting")) || (submodule.equals("HR")))) {
        %>
        <tr>
          <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Source"><bean:message key="label.pages.administration.sources"/></a></td>
          <td class="contentCell">
            <bean:message key="label.pages.administration.createeditdeletelistofvalues"/>
            <% if (! submodule.equals("Sales")) { out.println(submodule); } %><%=oppor%><bean:message key="label.pages.administration.records"/>
          </td>
        </tr>
        <%
      }
    }

    if (submodule.equals("Calendar")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/calendar_settings.do" />?sourcefor=<%=submodule%>&setting=Calendar"><bean:message key="label.pages.administration.workinghours"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.modifyhow"/> <%=submodule%> <bean:message key="label.pages.administration.screensdisplayhours"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("Activities")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Resource"><bean:message key="label.pages.administration.resources"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeleteresources"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("Sales")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Type"><bean:message key="label.pages.administration.types"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletetypevalues"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Stage"><bean:message key="label.pages.administration.stages"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletestagevalues"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Term"><bean:message key="label.pages.administration.terms"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletetermsvalues"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("Marketing")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/literature_list.do" />"><bean:message key="label.pages.administration.literature"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeleteliterature"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("Projects")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Status"><bean:message key="label.pages.administration.status"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.definemanagestatusvalues"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("Support")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/support_inbox.do" />?sourcefor=<%=submodule%>&setting=Support Inbox"><bean:message key="label.pages.administration.supportinbox"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.manageemailaccounts"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("Accounting")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=aCTerms"><bean:message key="label.pages.administration.terms"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletetermsoninvoice"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=InvoiceStatus"><bean:message key="label.pages.administration.invoicestatus"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletestatusoninvoice"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=OrderStatus"><bean:message key="label.pages.administration.orderstatus"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletestatusonorder"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_field_settings.do" />?sourcefor=<%=submodule%>&setting=Locations"><bean:message key="label.pages.administration.locations"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createeditdeletelocationonitem"/>.</td>
      </tr>
      <tr>
        <td class="labelCell"><html:link page="/administration/tax_settings.do" styleClass="plainLink"><bean:message key="label.pages.administration.taxsettings"/></html:link></td>
        <td class="contentCell"><bean:message key="label.pages.administration.createmanagetaxsettings"/>.</td>
      </tr>
      <%
    }

    if (submodule.equals("HR")) {
      %>
      <tr>
        <td class="labelCell"><a class="plainLink" href="<html:rewrite page="/administration/view_email_template.do" />?templateID=<%=AdministrationConstantKeys.EMAIL_TEMPLATE_SUGGESTIONBOX%>"><bean:message key="label.pages.administration.suggestionbox"/></a></td>
        <td class="contentCell"><bean:message key="label.pages.administration.modifysuggestionboxsettings"/>.</td>
      </tr>
      <%
    }

  %>
</table>