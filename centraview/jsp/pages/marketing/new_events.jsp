<%--
 * $RCSfile: new_events.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
<%-- tld reference --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script language="javascript">
<!--
  function save(closeornew)
  {
    populateAttachment();
    if (document.eventform.attachfile != null)	{
      var lengthSelected = window.document.eventform.attachfile.options.length;
      for (var i=0;i<lengthSelected;i++)	{
        window.document.eventform.attachfile.options[i].selected=true;
      }
    }
    document.eventform.action="<%=request.getContextPath()%>/marketing/save_new_event.do?TYPEOFREQUEST=ADD&FromAttendee=NO&closeornew="+closeornew;
    document.eventform.target="_self";
    document.eventform.submit();
  }

  function closewindow()
  {
    document.eventform.action="<%=request.getContextPath()%>/marketing/events_list.do";
  }

  function addAttendees()
  {
    document.eventform.action="<%=request.getContextPath()%>/marketing/save_new_event.do?TYPEOFREQUEST=ADD&FromAttendee=YES&closeornew=save";
    document.eventform.submit();
  }

  function event_openSelectDateTime()
  {
    var startDate = document.eventform.startmonth.value + "/" + document.eventform.startday.value + "/" + document.eventform.startyear.value;
    if (startDate == "//"){ startDate = ""; }
      var endDate = document.eventform.endmonth.value + "/" + document.eventform.endday.value + "/" + document.eventform.endyear.value;
    if (endDate == "//"){ endDate = ""; }
      var startTime = document.eventform.starttime.value;
    var endTime = document.eventform.endtime.value;
    c_openWindow('/calendar/select_date_time.do?dateTimeType=5&startDate='+startDate+'&endDate='+endDate+'&startTime='+startTime+'&endTime='+endTime, 'selectDateTime', 350, 500, '');
  }

  <%--
  This function is called from the SelectDateTime.do popup, which passes
  the date and time information from that popup back to this JSP. The code
  within this function can be modified to do whatever you need to do with
  the data on this page, including munging it, setting form properties, etc.
  But you SHOULD NOT change the name or signature of this function, WHATSOEVER!
  --%>
  function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
  {
    var startDateArray = startDate.split("/")
    if (startDateArray == null || startDateArray.length < 3){
      document.forms.eventform.startmonth.value = "";
      document.forms.eventform.startday.value = "";
      document.forms.eventform.startyear.value = "";
    }else{
      document.forms.eventform.startmonth.value = startDateArray[0];
      document.forms.eventform.startday.value = startDateArray[1];
      document.forms.eventform.startyear.value = startDateArray[2];
    }

    var endDateArray = endDate.split("/")
    if (endDateArray == null || endDateArray.length < 3){
      document.forms.eventform.endmonth.value = "";
      document.forms.eventform.endday.value = "";
      document.forms.eventform.endyear.value = "";
    }else{
      document.forms.eventform.endmonth.value = endDateArray[0];
      document.forms.eventform.endday.value = endDateArray[1];
      document.forms.eventform.endyear.value = endDateArray[2];
    }
    document.forms.eventform.starttime.value = startTime;
    document.forms.eventform.endtime.value = endTime;
    return(true);
  }

  function downloadFile()
  {
    if (document.eventform.attachfile.selectedIndex != -1) {
      fileid = document.eventform.attachfile[document.eventform.attachfile.selectedIndex].value;
      if (fileid != "") {
        location.href = "<html:rewrite page="/files/file_download.do"/>?fileid=" + fileid[0];
      }
    }else{
      alert("<bean:message key='label.alert.selectfiletodownload'/>");
    }
  }

  function attachmentlookup()
  {
    window.open('<html:rewrite page="/email/attachment_lookup.do"/>', '', 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=750,height=585' );
  }

  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.eventform.moderatorid.value = id;
    document.eventform.moderatorname.readonly = false;
    document.eventform.moderatorname.value = firstName+" "+middleName+" "+lastName;
    document.eventform.moderatorname.readonly = true;
  }

  function updateAttachmentBlank() {
  }

  function updateAttachments(strAttach, strAttachID)
  {
    fileIDArray = strAttachID.split("#");
    fileID = fileIDArray[0];
    var option0 = new Option( strAttach , fileID );
    var newIndex = document.eventform.attachfile.length ;
    document.eventform.attachfile.options[newIndex]=option0 ;
  }

  function populateAttachment(){
    if (document.eventform.attachfile != null)  {
      var lengthSelected = window.document.eventform.attachfile.options.length;
      for (var i=0;i<lengthSelected;i++)  {
        window.document.eventform.attachfile.options[i].selected=true;
      }
    }
  }

  function removeAttachment() {
    var length = document.forms[0].attachfile.options.length - 1;
    for (var i = length; i >= 0 ; i--) {
      if (document.forms[0].attachfile.options[i].selected) {
        document.forms[0].attachfile.options[i] = null;
      }
    }
  }

  <%-- Called by the attachment window, this function
       removes all data from the attachments select box --%>
  function removeAttachmentsContent() {
    var length = document.eventform.attachfile.options.length - 1;
    for (var i = length; i >= 0 ; i--) {
      if (document.eventform.attachfile.options[i].selected) {
        document.eventform.attachfile.options[i] = null;
      }
    }
  }
-->
</script>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr height="17">
    <td class="sectionHeader"><bean:message key="label.marketing.eventdetail"/></td>
  </tr>
</table>
<html:form action="/marketing/save_new_event.do">
<html:errors/>
<!-- BEGIN main content area -->
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="formTable">
  <tr>
    <td>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <!-- left area -->
          <td valign="top" width="100%">
            <table width="100%" border="0" cellpadding="0">
              <tr>
                <td class="labelCell"><bean:message key="label.marketing.name"/>: </td>
                <td class="contentCell">
                  <html:text property="name" styleId="name" styleClass="inputBox" size="45"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.marketing.description"/>: </td>
                <td class="contentCell">
                  <html:textarea property="description" styleClass="inputBox" cols="44" rows="5"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.marketing.start"/>: </td>
                <td class="contentCell">
                  <html:text property="startmonth" styleClass="inputBox" size="2" maxlength="2" onchange="setstartdate()"/>
                  /
                  <html:text property="startday" styleClass="inputBox" size="2" maxlength="2" onchange="setstartdate()"/>
                  /
                  <html:text property="startyear" styleClass="inputBox" size="4" maxlength="4" onchange="setstartdate()"/>
                  &nbsp;
                  <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" style="curosr:hand;" onclick="event_openSelectDateTime()"/>
                  &nbsp;
                  <html:text property="starttime" styleClass="inputBox" maxlength="8"  />
                  <html:img page="/images/icon_reminder.gif" width="17" height="17" align="absmiddle" onclick="event_openSelectDateTime()"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.marketing.end"/>: </td>
                <td class="contentCell">
                  <html:text property="endmonth" styleClass="inputBox" size="2" maxlength="2" onchange="setstartdate()"/>
                  /
                  <html:text property="endday" styleClass="inputBox" size="2" maxlength="2" onchange="setstartdate()"/>
                  /
                  <html:text property="endyear" styleClass="inputBox" size="4" maxlength="4" onchange="setstartdate()"/>
                  &nbsp;
                  <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" style="curosr:hand;" onclick="event_openSelectDateTime()"/>
                  &nbsp;
                  <html:text property="endtime" styleClass="inputBox" maxlength="8"  />
                  <html:img page="/images/icon_reminder.gif" width="17" height="17" align="absmiddle" onclick="event_openSelectDateTime()"/>
                </td>
              </tr>
           </table>
         </td>
        </tr>
      </table>
    </td>
    <!-- right area -->
    <td valign="top" align="left" width="50%">
      <table width="100%" border="0" cellpadding="0">
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.whoshouldattend"/>: </td>
          <td class="contentCell">
            <html:textarea property="whoshouldattend" rows="5" cols="45"  styleClass="inputBox" style="width:25em;" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.maxattendees"/>: </td>
          <td class="contentCell">
            <html:text property="maxattendees" styleClass="inputBox" size="7" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.marketing.moderator"/>: </td>
          <td class="contentCell">
            <html:hidden property="moderatorid" />
            <html:text property="moderatorname" styleClass="inputBox" readonly="true" size="30" />
            <html:button property="cmplookup" styleClass="normalButton" onclick="c_lookup('Employee');">
              <bean:message key="label.lookup"/>
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td valign="top" >
      <table width="48%" border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td valign="top" class="labelCell"><bean:message key="label.marketing.attachments"/></td>
          <td class="contentCell">
            <html:select property="attachfile" styleClass="inputBox" multiple= "true" style="width:20em;height:10em;">
              <c:forEach var="attachmentFile" items="${requestScope.attachmentFiles}">
                 <option value="<c:out value='${attachmentFile["fileid"]}' />" >
                  <c:out value='${attachmentFile["title"]}' />
                 </option>
              </c:forEach>
            </html:select>
          </td>
          <td align="center" valign="middle">
            <table border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td>
                  <app:cvbutton property="cmpattach" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="attachmentlookup()">
                    <bean:message key="label.marketing.attachfile"/>
                  </app:cvbutton>
                </td>
              </tr>
              <tr>
                <td>
                  <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" onclick="downloadFile()">
                    <bean:message key="label.marketing.download"/>
                  </app:cvbutton>
                </td>
              </tr>
              <tr>
                <td>
                  <app:cvbutton property="removefile" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="removeAttachment()">
                    <bean:message key="label.marketing.removefile"/>
                  </app:cvbutton>
                </td>
              </tr>
            </table>
          </td>
      </tr>
    </table>
  </td>
  <td valign="top" >
  </td>
 </tr>
 <tr>
    <td class="sectionHeader" colspan="2">
      <html:button property="cmpsave" styleClass="normalButton" onclick="save('save')">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="cmpsaveclose" styleClass="normalButton" onclick="save('close')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="cmpsavenew" styleClass="normalButton" onclick="save('new')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:cancel  styleClass="normalButton"  onclick="closewindow()">
        <bean:message key="label.cancel"/>
      </html:cancel>
    </td>
  </tr>

  <html:hidden property="eventid" />
  <html:hidden property="startdate" value=""/>
  <html:hidden property="enddate" value=""/>
</table>
<!-- END main content area -->
</html:form>