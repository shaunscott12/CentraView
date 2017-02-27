<%--
 * $RCSfile: opportunity_details.jsp,v $    $Revision: 1.6 $  $Date: 2005/09/30 15:56:01 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
%>
<script language="javascript">
function detailNewWindow()
{
  c_openPopup('/sales/view_opportunity.do?rowId=' + document.getElementById('opportunityid').value);
}
function newOpportunity()
{
  c_openWindow('/sales/new_opportunity.do', 'new_opp', 780, 250, '');
}
function deleteOpportunity()
{
  var opportunityId = document.getElementById('opportunityid').value;
  window.document.opportunityform.action = "<html:rewrite page="/sales/delete_opportunity.do"/>?closeornew=close&opportunityid=" + opportunityId;
  window.document.opportunityform.submit();
}
function duplicate()
{
  var opportunityId = document.getElementById('opportunityid').value;
  c_openPopup("/sales/view_opportunity.do?Duplicate=true&OPPORTUNITYID=" + opportunityId);
  return false;
}
function openPermissions()
{
  c_openPermission('Opportunities', document.getElementById('opportunityid').value, '<c:out value="${opportunityform.title}" />');
}
/**
 * Sets the Entity ID and name fields from the entity lookup.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
 * is called from the lookup screen, and must be general
 */
function setEntity(lookupValues)
{
  var entityIdField = document.getElementById('entityid');
  var entityNameField = document.getElementById('entityname');
  entityIdField.value = lookupValues.entID;
  entityNameField.value = lookupValues.entName;
}
/**
 * Sets the Individual ID and name fields from the individual lookup.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
 * is called from the lookup screen, and must be general
 */
function setIndividual(lookupValues)
{
  var individualIdField = document.getElementById('individualid');
  var individualNameField = document.getElementById('individualname');
  individualIdField.value = lookupValues.individualID;
  individualNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
}
/**
 * Sets the Account Manager ID and name fields from the individual lookup.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
 * is called from the lookup screen, and must be general
 */
function setEmployee(lookupValues)
{
  var acctmgrIdField = document.getElementById('acctmgrid');
  var acctmgrNameField = document.getElementById('acctmgrname');
  acctmgrIdField.value = lookupValues.individualID;
  acctmgrNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
}

function setSource(lookupValues)
{
  var sourceId = document.getElementById('sourceid');
  var sourceName = document.getElementById('sourcename');
  sourceId.value = lookupValues.idValue;
  sourceName.value = lookupValues.Name;
}
function openSelectDateTime()
{
  var startDate = document.getElementById('estimatedclosemonth').value + "/" + document.getElementById('estimatedcloseday').value + "/" + document.getElementById('estimatedcloseyear').value;
  if (startDate == "//"){ startDate = ""; }
  var endDate = document.getElementById('actualclosemonth').value + "/" + document.getElementById('actualcloseday').value + "/" + document.getElementById('actualcloseyear').value;
  if (endDate == "//"){ endDate = ""; }
  c_openWindow('/calendar/select_date_time.do?dateTimeType=3&simpleMode=true&startDate='+startDate+'&endDate='+endDate+'&startTime=&endTime=&endDateTitle=Actual+Close+Date&startDateTitle=Estimated+Close+Date', 'selectDateTime', 350, 500, '');
}
/**
 * Called by the /calendar/select_date_time.do popup, this function takes
 * input from that screen via the function parameters, and populates the
 * form fields on this JSP for the Estimated Close Date field.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION!!!!
 */
function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
{
  var startDateArray = startDate.split("/");
  if (startDateArray == null || startDateArray.length < 3) {
    document.getElementById('estimatedclosemonth').value = "";
    document.getElementById('estimatedcloseday').value = "";
    document.getElementById('estimatedcloseyear').value = "";
  }else{
    document.getElementById('estimatedclosemonth').value = startDateArray[0];
    document.getElementById('estimatedcloseday').value = startDateArray[1];
    document.getElementById('estimatedcloseyear').value = startDateArray[2];
  }
  var endDateArray = endDate.split("/");
  if (endDateArray == null || endDateArray.length < 3) {
    document.getElementById('actualclosemonth').value = "";
    document.getElementById('actualcloseday').value = "";
    document.getElementById('actualcloseyear').value = "";
  }else{
    document.getElementById('actualclosemonth').value = endDateArray[0];
    document.getElementById('actualcloseday').value = endDateArray[1];
    document.getElementById('actualcloseyear').value = endDateArray[2];
  }
  return(true);
}

function save(closeornew)
{

  var opportunityId = document.getElementById('opportunityid').value;
  document.opportunityform.action = "<html:rewrite page="/sales/update_opportunity.do"/>?TYPEOFOPERATION=EDIT&opportunityid=" + opportunityId + "&closeornew=" + closeornew + "&hasproposal=false";
  document.opportunityform.submit();
}
</script>
<html:form action="/sales/update_opportunity.do">
<html:hidden property="opportunityid" styleId="opportunityid" />
<html:hidden property="activityid" styleId="activityid" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="detailButtonHeader">
      <app:cvbutton property="cmpnewopportunity" styleId="cmpnewopportunity" styleClass="normalButton" onclick="newOpportunity();">
        <bean:message key="label.new"/>
      </app:cvbutton>
      <app:cvbutton property="cmpdelete" styleId="cmpdelete" styleClass="normalButton" onclick="deleteOpportunity();" buttonoperationtype="40">
        <bean:message key="label.delete"/>
      </app:cvbutton>
      <app:cvbutton property="cmpduplicate" styleId="cmpduplicate" styleClass="normalButton" onclick="duplicate();">
        <bean:message key="label.duplicate"/>
      </app:cvbutton>
      <app:cvbutton property="cmpproperties" styleId="cmpproperties" styleClass="normalButton" onclick="openPermissions();">
        <bean:message key="label.properties"/>
      </app:cvbutton >
      <app:cvbutton property="cmpnewopportunity" styleId="cmpnewopportunity" styleClass="normalButton" onclick="c_openWindow('/help/centraview.htm', 'help', 700, 580, '');">
        <bean:message key="label.question"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="321">
  <tr>
    <td valign="top" width="50%" style="border-right: #cccccc 1px solid;">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="detailHighlightHeader"><bean:message key="label.sales.opportunity"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="detailHighlightSection">
        <tr>
          <td class="labelCellBold"><bean:message key="label.sales.name"/>:</td>
          <td class="contentCell">
            <html:text property="title" styleId="title" styleClass="inputBox" size="45" />
          </td>
        </tr>
        <tr>
          <td class="labelCellBold" valign="top"><bean:message key="label.sales.description"/>:</td>
          <td class="contentCell">
            <app:cvtextarea property="description" styleId="description" rows="5" cols="44" styleClass="inputBox" fieldname="description" modulename="<bean:message key='label.sales.opportunities'/>"/>
          </td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell" width="62"><bean:message key="label.sales.entity"/>:</td>
          <td class="contentCell">
            <html:hidden property="entityid" styleId="entityid" />
            <app:cvtext property="entityname" styleId="entityname" readonly="true" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.getElementById('entityid').value);" size="35" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="entityname"/>
            <app:cvbutton property="entitylookup" styleId="entitylookup" styleClass="normalButton" onclick="c_lookup('Entity');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.contact"/>:</td>
          <td class="contentCell">
            <html:hidden property="individualid" styleId="individualid"  />
            <html:text property="individualname" styleId="individualname" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('individualid').value);" size="35" />
            <app:cvbutton property="individuallookup" styleId="individuallookup" styleClass="normalButton" onclick="c_lookup('Individual', document.getElementById('entityid').value);">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.source"/>:</td>
          <td class="contentCell">
            <html:hidden property="sourceid" styleId="sourceid" />
            <app:cvtext property="sourcename" styleId="sourcename" styleClass="inputBox" size="35" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="sourcename" onchange="setSourceID()"/>
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="c_lookup('Source')">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.created"/>:</td>
          <td class="contentCell">
            <c:out value="${requestScope.opportunityform.createddate}" />
            <c:out value="${requestScope.opportunityform.createdbyname}" />
            <html:hidden property="createddate" />
            <html:hidden property="modifieddate" />
            <html:hidden property="createdbyname" />
            <html:hidden property="modifiedbyname" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.modified"/>:</td>
          <td class="contentCell">
            <c:out value="${requestScope.opportunityform.modifieddate}" />
            <c:out value="${requestScope.opportunityform.modifiedbyname}" />
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.sales.details"/></td>
        </tr>
      </table>
      <table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.sales.status"/>:</td>
          <td class="contentCell">
            <%
              Vector statusVec = (gml.get("AllSaleStatus") != null) ? (Vector)gml.get("AllSaleStatus") : new Vector();
              pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="statusid" styleId="statusid" styleClass="inputBox" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="statusid">
              <app:cvoptions collection="statusVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.stage"/>:</td>
          <td class="contentCell">
            <%
              Vector stageVec = (gml.get("AllSaleStage") != null) ? (Vector)gml.get("AllSaleStage") : new Vector();
              pageContext.setAttribute("stageVec", stageVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="stageid" styleId="stageid" styleClass="inputBox" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="stageid">
              <app:cvoptions collection="stageVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.type"/>:</td>
          <td class="contentCell">
            <%
              Vector typeVec = (gml.get("AllSaleType") != null) ? (Vector)gml.get("AllSaleType") : new Vector();
              pageContext.setAttribute("typeVec", typeVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="opportunitytypeid" styleId="opportunitytypeid" styleClass="inputBox" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="opportunitytypeid">
              <app:cvoptions collection="typeVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.totalamount"/>:</td>
          <td class="contentCell">
            <bean:message key="currency.symbol"/>&nbsp;
            <c:choose>
              <c:when test="${requestScope.hasProposalInForcast}">
              <html:text property="totalamount" styleId="totalamount" styleClass="inputBox" size="16" disabled="false" readonly="true" />
              </c:when>
              <c:otherwise>
              <html:text property="totalamount" styleId="totalamount" styleClass="inputBox" size="16" disabled="false" />
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.probability"/>:</td>
          <td class="contentCell">
            <%
              Vector probabilityVec = (gml.get("AllSaleProbability") != null) ? (Vector)gml.get("AllSaleProbability") : new Vector();
              pageContext.setAttribute("probabilityVec", probabilityVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="probabilityid" styleId="probabilityid" styleClass="inputBox" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="probabilityid">
              <app:cvoptions collection="probabilityVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.forecastamount"/>:</td>
          <td class="contentCell">
            <bean:message key="currency.symbol"/>&nbsp;
            <app:cvtext property="forecastedamount" styleId="forecastedamount" styleClass="inputBox" size="18" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="forecastedamount" readonly="true" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.estimatedclose"/>:</td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><app:cvtext property="estimatedclosemonth" styleId="estimatedclosemonth" readonly="true" styleClass="inputBox" size="2" maxlength="2" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="estimatedclosemonth"/></td>
                <td>/</td>
                <td><app:cvtext property="estimatedcloseday" styleId="estimatedcloseday" readonly="true" styleClass="inputBox" size="2" maxlength="2" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="estimatedcloseday"/></td>
                <td>/</td>
                <td><app:cvtext property="estimatedcloseyear" styleId="estimatedcloseyear" readonly="true" styleClass="inputBox" size="5" maxlength="4" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="estimatedcloseyear"/></td>
                <td><a class="plainLink" onClick="openSelectDateTime();"><html:img page="/images/icon_calendar.gif" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.actualclose"/>:</td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><app:cvtext property="actualclosemonth" styleId="actualclosemonth" readonly="true" styleClass="inputBox" size="2" maxlength="2" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="actualclosemonth"/></td>
                <td>/</td>
                <td><app:cvtext property="actualcloseday" styleId="actualcloseday" readonly="true" styleClass="inputBox" size="2" maxlength="2" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="actualcloseday"/></td>
                <td>/</td>
                <td><app:cvtext property="actualcloseyear" styleId="actualcloseyear" readonly="true" styleClass="inputBox" size="5" maxlength="4" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="actualcloseyear"/></td>
                <td><a class="plainLink" onClick="openSelectDateTime();"><html:img page="/images/icon_calendar.gif" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.accountmanager"/>:</td>
          <td class="contentCell">
            <html:hidden property="acctmgrid" styleId="acctmgrid" />
            <app:cvtext property="acctmgrname" styleId="acctmgrname" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('acctmgrid').value);" size="30" readonly="true" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="acctmgrname"/>
            <app:cvbutton property="acctteamlookup" styleId="acctteamlookup" styleClass="normalButton" onclick="c_lookup('Employee');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.accountteam"/>:</td>
          <td class="contentCell">
            <html:hidden property="acctteamid" styleId="acctteamid" />
            <app:cvtext property="acctteamname" styleId="acctteamname" styleClass="inputBox" size="30" readonly="true" modulename="<bean:message key='label.sales.opportunities'/>" fieldname="acctteamname"/>
            <app:cvbutton property="acctteamlookup" styleId="acctteamlookup" styleClass="normalButton" onclick="c_lookup('Group');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="2" cellspacing="0" width="100%">
  <tr>
    <td class="headerBG" align="right">
      <app:cvbutton property="cmpsave" styleId="cmpsave" styleClass="normalButton" onclick="save('savechanges');"  buttonoperationtype="20">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="saveclose" styleId="saveclose" styleClass="normalButton" onclick="save('close');" buttonoperationtype="20">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvreset styleClass="normalButton">
        <bean:message key="label.reset"/>
      </app:cvreset>
      <html:cancel styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel"/>
      </html:cancel>
    </td>
  </tr>
</table>
</html:form>