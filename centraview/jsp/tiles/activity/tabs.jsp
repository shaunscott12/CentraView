<%--
 * $RCSfile: tabs.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:53 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="com.centraview.activity.ConstantKeys" %>
<% String selectedTab = (String)request.getAttribute(ConstantKeys.TYPEOFSUBACTIVITY); %>
<table border="0" cellspacing="0" cellpadding="0" class="tabsContainer">
  <tr>
    <td class="tabsContainerTopRow">
      <table border="0" cellspacing="0" cellpadding="2" class="tabsTable">
        <tr>
					<td class="firstActivityTabOff">
            <span class="tabsText"><bean:message key="label.tiles.activity.schedule"/>:</span>&nbsp;
            <html:select property="activityType" onchange="act_TypeChange(this)" styleClass="inputBox">
              <html:option value="1"><bean:message key="label.tiles.activity.appointment"/>  </html:option>
              <html:option value="2"><bean:message key="label.tiles.activity.call"/>  </html:option>
              <html:option value="5"><bean:message key="label.tiles.activity.meeting"/>  </html:option>
              <html:option value="7"><bean:message key="label.tiles.activity.nextaction"/></html:option>
              <html:option value="6"><bean:message key="label.tiles.activity.todo"/></html:option>
              <html:option value="/sales/new_opportunity.do"><bean:message key="label.tiles.activity.forecastsale"/>  </html:option>
              <html:option value="/marketing/new_literaturefulfillment.do"> <bean:message key="label.tiles.activity.literaturerequest"/></html:option>
            </html:select>
          </td>
          <% String tabClass = "tabsOff"; %>
          <% if (selectedTab.equals("DETAIL")){ tabClass = "tabsOn"; }else{ tabClass = "tabsoff"; } %>
          <td class="<%=tabClass%>"><a href="#" onclick="act_launchDetail();" class="<%=tabClass%>"><bean:message key="label.tiles.activity.details"/></a></td>
          <% if (selectedTab.equals("ATTENDEE")){ tabClass = "tabsOn"; }else{ tabClass = "tabsoff"; } %>
          <td class="<%=tabClass%>"><a href="#" onclick="act_launchAttendee();" class="<%=tabClass%>"><bean:message key="label.tiles.activity.attendees"/></a></td>
          <% if (selectedTab.equals("RESOURCE")){ tabClass = "tabsOn"; }else{ tabClass = "tabsoff"; } %>
          <td class="<%=tabClass%>"><a href="#" onclick="act_launchResource();" class="<%=tabClass%>"><bean:message key="label.tiles.activity.resources"/></a></td>
          <% if (selectedTab.equals("AVAILABILITY")){ tabClass = "tabsOn"; }else{ tabClass = "tabsoff"; } %>
          <td class="<%=tabClass%>"><a href="#" onclick="act_launchAvailability();" class="<%=tabClass%>"><bean:message key="label.tiles.activity.availability"/></a></td>
          <% if (selectedTab.equals("RECURRING")){ tabClass = "tabsOn"; }else{ tabClass = "tabsoff"; } %>
          <td class="<%=tabClass%>"><a href="#" onclick="act_launchRecurring();" class="<%=tabClass%>"><bean:message key="label.tiles.activity.recurring"/></a></td>
          <% if (selectedTab.equals("ATTACHMENT")){ tabClass = "tabsOn"; }else{ tabClass = "tabsoff"; } %>
          <td class="<%=tabClass%>"><a href="#" onclick="act_launchAttachment();" class="<%=tabClass%>"><bean:message key="label.tiles.activity.attachments"/></a></td>
          <td class="lastTabOff"><html:img page="/images/spacer.gif" width="1" height="1" border="0" alt="" /></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="tabsBottomBar"><html:img page="/images/clear.gif" width="1" height="6" border="0" alt="" /></td>
  </tr>
</table>
<html:hidden property="<%=ConstantKeys.TYPEOFACTIVITY%>" value="<%=request.getAttribute(ConstantKeys.TYPEOFACTIVITY).toString()%>"/>
<html:hidden property="<%=ConstantKeys.TYPEOFSUBACTIVITY%>" value="<%=request.getAttribute(ConstantKeys.TYPEOFSUBACTIVITY).toString()%>"/>
<html:hidden property="<%=ConstantKeys.TYPEOFOPERATION%>" value="<%=request.getAttribute(ConstantKeys.TYPEOFOPERATION).toString()%>"/>
<html:hidden property="activityID"/>
<html:errors/>