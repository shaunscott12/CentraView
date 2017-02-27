<%--
 * $RCSfile: attendees.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ page import="com.centraview.activity.ConstantKeys" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.activity.AttendeeList" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.activity.ActivityForm" %>
<%@ page import="com.centraview.common.UserObject" %>
<%
  ActivityForm activityform = (ActivityForm)request.getAttribute("activityform");

  Vector attendeeList = new Vector();
  if (request.getAttribute("attendeeList") != null) {
    attendeeList = (Vector) request.getAttribute("attendeeList");
  }

  Vector att_optional = new Vector();
  Vector att_required = new Vector();

  if (activityform.getActivityAttendeeOptionalVector() != null) {
    att_optional = (Vector)activityform.getActivityAttendeeOptionalVector();
  }

  if (activityform.getActivityAttendeeRequiredVector() != null) {
    att_required = (Vector)activityform.getActivityAttendeeRequiredVector();
  }

  if (att_required.size() == 0) {
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    String userName = userObject.getfirstName() + " " +
    userObject.getlastName();
    // set user as default attendee
    DDNameValue setUserAttendee = new DDNameValue("" + individualId + "#" + userName, userName);
    att_required.add(setUserAttendee);
  }

%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="49%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.individuals"/></td>
        </tr>
        <tr>
          <td class="contentCell">
          <html:text property="activityAttendeeSearch" styleId="activityAttendeeSearch" styleClass="inputBox" size="30"/>
          <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" onclick="act_searchAttendees();">
            <bean:message key="label.search"/>
          </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              Vector individualList = (Vector) request.getAttribute("individualList");
              if (individualList == null) { individualList = new Vector(); }
              pageContext.setAttribute("individualList", individualList, PageContext.PAGE_SCOPE);
            %>
            <%-- TODO: finish the groups dropdown --%>
            <html:select property="activityAttendeesType" styleId="activityAttendeesType" styleClass="inputBox" onchange="act_changeAttendeesType();">
              <html:options collection="individualList" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              if (attendeeList == null) {
                attendeeList = new Vector();
              }
              pageContext.setAttribute("attendeeList", attendeeList, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityAttendeesAll" styleId="activityAttendeesAll" size="16" multiple="true" styleClass="inputBox" style="width:200px;">
              <html:options collection="attendeeList" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
    <td width="2%" align="center" valign="middle">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td>
            <app:cvbutton property="changeLookup2" styleId="changeLookup2" styleClass="normalButton" style="width:65px;" onclick="act_addAttendee('required');">
              <bean:message key="label.add"/> &raquo;
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td>
            <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" style="width:65px;" onclick="act_removeAttendee('required');">
              &laquo; <bean:message key="label.remove"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td style="height:100px;"><html:img page="/images/spacer.gif" width="1" height="1" border="0" alt=""/></td>
        </tr>
        <tr>
          <td>
            <app:cvbutton property="changeLookup2" styleId="changeLookup2" styleClass="normalButton" style="width:65px;" onclick="act_addAttendee('optional');">
              <bean:message key="label.add"/> &raquo;
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td>
            <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" style="width:65px;" onclick="act_removeAttendee('optional')">
              &laquo; <bean:message key="label.remove"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td width="49%" align="center" valign="top">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.requiredattendees"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              if (att_required == null) {
                att_required = new Vector();
              }
              pageContext.setAttribute("att_required", att_required, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityAttendeesRequired" styleId="activityAttendeesRequired" size="9" multiple="true" style="width:200px;" styleClass="inputBox">
              <html:options collection="att_required" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.optionalattendees"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              if (att_optional == null) {
                att_optional = new Vector();
              }
              pageContext.setAttribute("att_optional", att_optional, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityAttendeesOptional" styleId="activityAttendeesOptional" size="9" multiple="true" style="width:200px;" styleClass="inputBox">
              <html:options collection="att_optional" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>