<%
/*
* $RCSfile: literaturefulfillment_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
*/
%>

<%-- tld reference --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<html:form action="/marketing/save_literaturefulfillment" >
<html:errors/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="formTable">
  <tr valign="top">
    <td valign="top">
      <table cellspacing="0" cellpadding="3" >
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.title"/>: </td>
          <td class="contentCell">
            <app:cvtext property="title" styleId="title" styleClass="inputBox" modulename="literature" fieldname="title" size="21"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.detail"/>: </td>
          <td class="contentCell">
            <app:cvtextarea property="detail" rows="4" styleClass="inputBox" fieldname="detail" modulename="literature" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.literature"/>: </td>
          <td class="contentCell">
            <app:cvselect property="literaturename" multiple="true" styleClass="inputBox"  style="width:15em;height:5em;" modulename="literature" fieldname="literaturename">
              <c:forEach var="literatureOptions" items="${literatureform.literaturenamevec}">
                <option name="<c:out value='${literatureOptions.id}'/>"><c:out value='${literatureOptions.name}'/></option>
              </c:forEach>
            </app:cvselect>
            <html:button property="cmpaddliterature" styleClass="normalButton" onclick="addLiterature()">
              <bean:message key="label.marketing.addliterature"/>
            </html:button>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top" >
      <table cellspacing="0" cellpadding="3" >
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.entity"/>: </td>
          <td class="contentCell">
            <html:hidden property="entityid" styleClass="popupTextBox" />
            <app:cvtext property="entityname"   styleClass="clickableInputBox"  onclick="c_openPopup_FCI('Entity',document.literatureform.entityid.value);"  readonly="true"    modulename="literature" fieldname="title" style="width: 17em;"/>
            <html:button property="entitylookup" styleClass="normalButton" onclick="c_lookup('Entity');">
              <bean:message key="label.lookup"/>
            </html:button>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.individual"/>:</td>
          <td class="contentCell">
            <html:hidden property="individualid" styleClass="popupTextBox"  />
            <app:cvtext property="individualname"  styleClass="clickableInputBox"  onclick="c_openPopup_FCI('Individual',document.literatureform.individualid.value);" modulename="literature" fieldname="title" style="width: 17em;"/>
            <html:button property="individuallookup" styleClass="normalButton" onclick="literatureInd()">
              <bean:message key="label.lookup"/>
            </html:button>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.assignedto"/>: </td>
          <td class="contentCell">
            <html:hidden property="assignedtoid" styleClass="popupTextBox"  />
            <app:cvtext property="assignedtoname" styleClass="clickableInputBox"  onclick="c_openPopup_FCI('Individual',document.literatureform.assignedtoid.value);" modulename="literature" fieldname="title" style="width: 17em;"/>
            <html:button property="assignedTolookup" styleClass="normalButton" onclick="c_lookup('Employee');">
              <bean:message key="label.lookup"/>
            </html:button>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.deliverymethod"/>: </td>
          <td class="contentCell">
            <app:cvselect property="deliverymethodid" styleClass="inputBox" modulename="literature" fieldname="deliverymethodid">
              <app:cvoptions collection="deliverymethodlist" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.status"/>: </td>
          <td class="contentCell">
          <app:cvselect property="statusid" styleClass="inputBox" modulename="literature" fieldname="statusid">
            <app:cvoptions collection="activitystatuslist" property="id" labelProperty="name"/>
          </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.datedue"/>: </td>
          <td class="contentCell">
             <app:cvtext property="duebymonth" styleClass="inputBox" modulename="literature" fieldname="title" style="width: 2em;"/>
             /
             <app:cvtext property="duebyday" styleClass="inputBox" modulename="literature" fieldname="title" style="width: 2em;"/>
             /
             <app:cvtext property="duebyyear" styleClass="inputBox" modulename="literature" fieldname="title" style="width: 4em;"/>
             &nbsp;
             <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" style="curosr:hand;" onclick="popupCalendar('DUEBY')"/>
             &nbsp;
             <app:cvtext property="duebytime" styleClass="inputBox" size="8" maxlength="8"  modulename="literature" fieldname="title"/>
             <html:img page="/images/icon_reminder.gif" width="17" height="17" align="absmiddle" onclick="popupCalendar('DUEBY')"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<html:hidden property="literatureid" />
<html:hidden property="currentday" value=""/>
<html:hidden property="currentmonth" value=""/>
<html:hidden property="currentyear" value=""/>
<html:hidden property="names" />
<input type="hidden" name="activityid" value="<c:out value='${param.activityid}'/>" />

<input type="hidden" name="TYPEOFOPERATION" value="<c:out value='${requestScope.TYPEOFOPERATION}'/>"/>
</html:form>


<script language="javascript">
<!--
  today = new Date();
  var yy=today.getYear();
  var mm=(parseInt(today.getMonth()))+1;
  var dd =today.getDate();
  if (mm<10)
  {
   mm ="0"+mm;
  }
  if (dd<10)
  {
    dd="0"+dd;
  }

  document.literatureform.currentday.value = dd;
  document.literatureform.currentmonth.value = mm;
  document.literatureform.currentyear.value = yy;

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.literatureform.entityname.readonly = false;
    document.literatureform.entityname.value = name;
    document.literatureform.entityid.value = id;
    idEntity = id;
    document.literatureform.entityname.readonly = true;
    document.literatureform.individualid.value = "";
    document.literatureform.individualname.readonly = false;
    document.literatureform.individualname.value = "";
    document.literatureform.individualname.readonly = true;
  }

  var individualId = 0;

  function setIndividual(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.literatureform.individualid.value = id;
    document.literatureform.individualname.readonly = false;
    document.literatureform.individualname.value = firstName+" "+middleName+" "+lastName;
    document.literatureform.individualname.readonly = true;
  }

  var indId = 0;
  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.literatureform.assignedtoid.value = id;
    document.literatureform.assignedtoname.value = firstName+" "+middleName+" "+lastName;
  }


  function literatureInd() {
    var entityID = document.literatureform.entityid.value;
    if (entityID <= 0) {
      alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.");
    }else{
      c_lookup('Individual', entityID);
    }
  }


  function popupCalendar(calendarId){
    var startDate = document.forms.literatureform.duebymonth.value + "/" + document.forms.literatureform.duebyday.value + "/" + document.forms.literatureform.duebyyear.value;
    if (startDate == "//"){ startDate = ""; }
    var startTime = document.forms.literatureform.duebytime.value;
    c_openWindow('/calendar/select_date_time.do?dateTimeType=2&startDate='+startDate+'&startTime='+startTime, 'selectDateTime', 350, 500, '');
  }

  /*
    This function is called from the SelectDateTime.do popup, which passes
    the date and time information from that popup back to this JSP. The code
    within this function can be modified to do whatever you need to do with
    the data on this page, including munging it, setting form properties, etc.
    BUT YOU SHOULD NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, WHATSOEVER!
  */
  function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
  {
    var startDateArray = startDate.split("/")
    if (startDateArray == null || startDateArray.length < 3){
      document.forms.literatureform.duebymonth.value = "";
      document.forms.literatureform.duebyday.value = "";
      document.forms.literatureform.duebyyear.value = "";
    }else{
      document.forms.literatureform.duebymonth.value = startDateArray[0];
      document.forms.literatureform.duebyday.value = startDateArray[1];
      document.forms.literatureform.duebyyear.value = startDateArray[2];
    }
    document.forms.literatureform.duebytime.value = startTime;
    return(true);
  }

  // script related to add/edit page
  function save(closeornew)
  {
    populateResource()  ;
    document.literatureform.action="<%=request.getContextPath()%>/marketing/save_literaturefulfillment.do?closeornew="+closeornew;
    document.literatureform.target="_self";
    document.literatureform.submit();
  }


  function populateResource() {
    if (document.literatureform.literaturename != null) {
      var lengthSelected = window.document.literatureform.literaturename.options.length;
      for (var i=0;i<lengthSelected;i++)  {
        window.document.literatureform.literaturename.options[i].selected=true;
      }
    }
  }

  function addLiterature()
  {
    var id = document.literatureform.literatureid.value;
    c_openWindow('/marketing/literature_lookup.do?literatureid='+id, 'LiteratureLookup', 400, 400, '');
  }

  function setData(forwardId,forwardValue)
  {
    document.literatureform.names.value=forwardValue;
    document.literatureform.literatureid.value=forwardId;
    var len=0;
    var forwardId_array = forwardId.split(",");
    var forwardValue_array = forwardValue.split(",");

    for (var loop=0; loop < document.literatureform.literaturename.length; loop++)
    {
      if(forwardId_array[loop] != ""){
        document.literatureform.literaturename.options[len] = null;
        len = len+1;
      }
    }
    var len=0;
    for (var loop=0; loop < forwardId_array.length; loop++)
    {
      if(forwardId_array[loop] != ""){
        document.literatureform.literaturename.options[len] = new Option(forwardValue_array[loop],forwardId_array[0],"false","false");
        len = len+1;
      }
    }
  }
-->
</script>
