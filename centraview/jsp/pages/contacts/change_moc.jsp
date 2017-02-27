<%--
 * $RCSfile: change_moc.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import = "com.centraview.activity.ConstantKeys"%>
<%@ page import = "com.centraview.common.*"%>
<%@ page import = "org.apache.struts.action.*" %>
<% request.setAttribute("detailName", request.getAttribute("name")); %>
<%
  UserObject userobject =(UserObject)session.getAttribute("userobject");
  String thisContactID = (String)session.getAttribute("ContactID");
  String entityString = (String)((DynaActionForm)request.getAttribute("changeMOCForm")).get("EntityType");
  boolean isEntity = entityString.equalsIgnoreCase("TRUE");
%>
<script language="javascript">
<!--
  function moveup()
  {
    var n = document.changeMOCForm.primaryPhoneNumbers.options.length;
    if (document.changeMOCForm.primaryPhoneNumbers.selectedIndex == -1)
    {
      alert("<bean:message key='label.alert.selectphonenumberup'/>.");
    }else{
      var index = parseInt(document.changeMOCForm.primaryPhoneNumbers.selectedIndex);
      if (index != 0)
      {
        var previousIndex = parseInt(index-1);
        var previousValue = document.changeMOCForm.primaryPhoneNumbers.options[previousIndex].text;
        var currentValue = document.changeMOCForm.primaryPhoneNumbers.options[index].text;
        var previousID = document.changeMOCForm.primaryPhoneNumbers.options[previousIndex].value;
        var currentID = document.changeMOCForm.primaryPhoneNumbers.options[index].value;
        document.changeMOCForm.primaryPhoneNumbers.options[previousIndex] = new Option(currentValue,currentID,"false","false");
        document.changeMOCForm.primaryPhoneNumbers.options[index] = new Option(previousValue,previousID,"false","false");
        document.changeMOCForm.primaryPhoneNumbers.options[index].selected = false;
      }
    }
  }

  function movedown()
  {
    var n = document.changeMOCForm.primaryPhoneNumbers.options.length;
    if (document.changeMOCForm.primaryPhoneNumbers.selectedIndex == -1)
    {
      alert("<bean:message key='label.alert.selectphonenumberdown'/>.");
    }else{
      var index = parseInt(document.changeMOCForm.primaryPhoneNumbers.selectedIndex);
      if (index < (n-1))
      {
        var nextIndex = parseInt(index+1);
        var nextValue = document.changeMOCForm.primaryPhoneNumbers.options[nextIndex].text;
        var currentValue = document.changeMOCForm.primaryPhoneNumbers.options[index].text;
        var nextID = document.changeMOCForm.primaryPhoneNumbers.options[nextIndex].value;
        var currentID = document.changeMOCForm.primaryPhoneNumbers.options[index].value;
        document.changeMOCForm.primaryPhoneNumbers.options[nextIndex] = new Option(currentValue, currentID,"false","false");
        document.changeMOCForm.primaryPhoneNumbers.options[index] = new Option(nextValue,nextID,"false","false");
        document.changeMOCForm.primaryPhoneNumbers.options[index].selected = false;
      }
    }
  }

  function cancelEntityChangeMOC()
  {
    <% if (isEntity) { %>
    c_goTo('/contacts/view_entity.do?rowId=<%=thisContactID%>');
    <% } else { %>
    c_goTo('/contacts/view_individual.do?rowId=<%=thisContactID%>');
    <% } %>
  }

  function selectAll()
  {
    for (i = 0; i < document.changeMOCForm.primaryPhoneNumbers.options.length; i++)
    {
      document.changeMOCForm.primaryPhoneNumbers.options[i].selected = true;
    }
    return true;
  }

  function checkProceed()
  {
    <% if (request.getAttribute("refreshWindow") != null && request.getAttribute("refreshWindow").equals("true")){ %>
    window.opener.location.reload();
    <% } %>
    <% if (request.getAttribute("closeWindow") != null && request.getAttribute("closeWindow").equals("true")){ %>
    cancelEntityChangeMOC();
    <% }else{%>
    return true;
    <% } %>
  }
  checkProceed();
//-->
</script>
<% session.setAttribute("companyNameForBottomLists",request.getAttribute("name")); %>
<html:form action="/contacts/save_change_moc">
<html:hidden property="ContactID" />
<html:hidden property="EntityType" />
<table width="540" height="325" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="detailHighlightHeader">
            <% if (isEntity) {%><bean:message key="label.contacts.entity"/><%} else {%><bean:message key="label.contacts.individual"/><%}%>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="1" cellspacing="0" width="100%" class="detailHighlightSection">
        <tr>
          <td class="labelCellBold" width="55"><bean:message key="label.contacts.name"/>:</td>
          <td class="contentCell">
            <c:out value="${requestScope.recordName}"/>
          </td>
        </tr>
        <tr>
          <td class="labelCellBold" width="55"><bean:message key="label.contacts.id"/>:</td>
          <td class="contentCell">
            <c:out value="${requestScope.recordId}"/>
          </td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="8">
        <tr>
          <td colspan="3" class="labelCellBold"><bean:message key="label.contacts.primaryphone"/></td>
        </tr>
        <tr>
          <td colspan="2">
            <html:select property="primaryPhoneNumbers" styleClass="inputBox" style="height:12em;width:150px;" multiple="true" size="6">
              <html:optionsCollection property="phoneMOCOrder" value="mocID" label="content"/>
            </html:select>
          </td>
          <td>
            <app:cvbutton property="cmpmoveup" styleClass="normalButton" onclick="moveup();" style="width:100px;">
              <bean:message key="label.moveup"/>
            </app:cvbutton>
            <br>
            <br>
            <app:cvbutton property="cmpmovedown" styleClass="normalButton" onclick="movedown();" style="width:100px;">
              <bean:message key="label.movedown"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td colspan="3" class="labelCellBold">* <bean:message key="label.contacts.top3"/></td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="8">
        <tr>
          <td colspan="3" class="labelCellBold"><bean:message key="label.contacts.primaryemail"/></td>
        </tr>
        <tr>
          <td colspan="2">
            <html:select property="primaryEmailAddress" styleClass="inputBox" style="width:150px;">
              <html:optionsCollection property="emailMOCOrder" value="mocID" label="content"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr align="right">
    <td class="buttonRow">
      <table border="0" cellspacing="0" cellpadding="2">
        <tr>
          <td>
            <app:cvsubmit property="savechanges" styleClass="normalButton" onclick="return selectAll();">
              <bean:message key="label.savechanges"/>
            </app:cvsubmit>
          </td>
          <td>
            <app:cvsubmit property="saveandclose" styleClass="normalButton" onclick="return selectAll();">
              <bean:message key="label.saveandclose"/>
            </app:cvsubmit>
          </td>
          <td>
            <app:cvreset property="reset" styleClass="normalButton">
              <bean:message key="label.resetfields"/>
            </app:cvreset>
          </td>
          <td>
            <app:cvbutton property="cancel" styleClass="normalButton"  onclick="return cancelEntityChangeMOC();">
              <bean:message key="label.cancel"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type="hidden" value="<%=request.getParameter("listId")%>" name="listId"/>
</html:form>