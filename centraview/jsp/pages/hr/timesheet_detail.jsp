<%
/**
 * $RCSfile: timesheet_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.struts.util.*"%>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.hr.helper.TimeSheetVO" %>
<%@ page import="java.text.SimpleDateFormat" %>


<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%
response.setHeader("Pragma","no-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);

String timeSheetId = (String)request.getParameter("rowId");
if(timeSheetId != null){
	Integer intTimesheetID = new Integer(timeSheetId);
	request.setAttribute("timesheetID",intTimesheetID);
}
String sTypeOfOperation = request.getParameter("TYPEOFOPERATION");
session.setAttribute("TYPEOFOPERATION",sTypeOfOperation);
String addslipFlag = (String)request.getAttribute("addslip");
request.setAttribute("addslip",addslipFlag);

String sErrorsPresent = (String)session.getAttribute("ERRORSPRESENT");
%>
<html:errors/>
<script>
<!--
  var lookuptype = "";
  function popupCalendar()
  {
    var startDate = document.forms.NewTimeSheetForm.frommonth.value + "/" + document.forms.NewTimeSheetForm.fromday.value  + "/" + document.forms.NewTimeSheetForm.fromyear.value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = document.forms.NewTimeSheetForm.tomonth.value + "/" + document.forms.NewTimeSheetForm.today.value + "/" + document.forms.NewTimeSheetForm.toyear.value;
    if (endDate == "//"){ endDate = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate, 'selectDateTime', 350, 500, '');
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
      document.forms.NewTimeSheetForm.frommonth.value = "";
      document.forms.NewTimeSheetForm.fromday.value = "";
      document.forms.NewTimeSheetForm.fromyear.value = "";
    }else{
      document.forms.NewTimeSheetForm.frommonth.value = startDateArray[0];
      document.forms.NewTimeSheetForm.fromday.value = startDateArray[1];
      document.forms.NewTimeSheetForm.fromyear.value = startDateArray[2];
    }

    var endDateArray = endDate.split("/")
    if (endDateArray == null || endDateArray.length < 3){
      document.forms.NewTimeSheetForm.tomonth.value = "";
      document.forms.NewTimeSheetForm.today.value = "";
      document.forms.NewTimeSheetForm.toyear.value = "";
    }else{
      document.forms.NewTimeSheetForm.tomonth.value = endDateArray[0];
      document.forms.NewTimeSheetForm.today.value = endDateArray[1];
      document.forms.NewTimeSheetForm.toyear.value = endDateArray[2];
    }
    return(true);
  }

  function openIndividualLookUp(luType)
  {
    lookuptype  =  luType;
    c_lookup('Employee');
    return false;
  }

  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    if(lookuptype == 'individual')
    {
      document.NewTimeSheetForm.reportingTo.readonly = false;
      document.NewTimeSheetForm.reportingTo.value = firstName+" "+middleName+" "+lastName;
      document.NewTimeSheetForm.reportingToId.value = id;
    }
    else if(lookuptype == 'employee')
    {
      document.NewTimeSheetForm.employee.readonly = false;
      document.NewTimeSheetForm.employee.value = firstName+" "+middleName+" "+lastName;
      document.NewTimeSheetForm.employeeId.value = id;
    }
  }

  var flag = false;
  function setFlag(status)
  {
    flag = status;
  }

  function isValid()
  {
    if(document.NewTimeSheetForm.reportingTo.value== "" || document.NewTimeSheetForm.employee.value == ""
      || document.NewTimeSheetForm.fromday.value == "" || document.NewTimeSheetForm.frommonth.value == "" || document.NewTimeSheetForm.fromyear.value == ""
      || document.NewTimeSheetForm.today.value == "" || document.NewTimeSheetForm.tomonth.value == "" || document.NewTimeSheetForm.toyear.value== "" ){
      return false;
    }
    else
    {
      var sFromDate = document.NewTimeSheetForm.fromday.value + "/" + document.NewTimeSheetForm.frommonth.value + "/" + document.NewTimeSheetForm.fromyear.value;
      var sToDate   = document.NewTimeSheetForm.today.value + "/" + document.NewTimeSheetForm.tomonth.value + "/" + document.NewTimeSheetForm.toyear.value;
      var isValidDate = doDateCheck(sFromDate,sToDate);
      if(!isValidDate)
        alert("<bean:message key='label.alert.togreaterthanfromdate'/>");
      return isValidDate;
    }
  }


  function openNewTimeSlip()
  {
    var childWindow = false;
    var iIndex = 0;
    if(isValid())
    {
      if(flag == true)
      {
        document.NewTimeSheetForm.timeSlipFlag.value = true;
      }
    }
    return true;
  }

  function checkCheckbox()
  {
    if (document.NewTimeSheetForm.checkbox.length > 0)
    {
      for(i =0 ;i<document.NewTimeSheetForm.checkbox.length;i++)
      {
        if ( document.NewTimeSheetForm.checkbox[i].checked)
        {
          return true;
        }
      }
    }
    else
    {
      return document.NewTimeSheetForm.checkbox.checked;
    }
  }


  function deleteSlips()
  {
    var selectedtimeslipID = "";
    var strParam = "";
    if(checkCheckbox())
    {
      var k=0;
      for (i=0 ;i<document.NewTimeSheetForm.elements.length;i++)
      {
        if(document.NewTimeSheetForm.elements[i].type == "checkbox")
        {
          k++;
        }
      }
      if(k>1)
      {
        for(i =0 ; i<k ; i++)
        {
          if (document.NewTimeSheetForm.checkbox[i].checked)
          {
            selectedtimeslipID = document.NewTimeSheetForm.checkbox[i].value + ",";
            strParam = strParam + selectedtimeslipID;
          }
        }
        strParam = (strParam.substring(0,strParam.length-1));
      }
      else if (k==1)
      {
        selectedtimeslipID=document.NewTimeSheetForm.checkbox.value + ",";
        strParam=strParam+selectedtimeslipID;
        strParam = (strParam.substring(0,strParam.length-1));
      }
      c_goTo('/hr/delete_timeslip.do?rowId=<bean:write name="NewTimeSheetForm" property="timesheetID"/>&timeslipIds='+strParam);
    }
    else
    {
      alert("<bean:message key='label.alert.norecordsselected'/>");
    }
  }

  function addTimeSlip()
  {
    var typeOfOperation = '<%=request.getParameter("TYPEOFOPERATION")%>';
    var childWindow = false;
    if(typeOfOperation != null)
    {
      if(isValid())
        childWindow = c_openWindow("/projects/new_timeslip.do?timesheetID=<bean:write name="NewTimeSheetForm" property="timesheetID"/>&TYPEOFOPERATION="+typeOfOperation,'','toolbar=no,status=no,scrollbars=yes,location=no,menubar=no,directories=no');
    }
    else
      childWindow = c_openWindow("/projects/new_timeslip.do?timesheetID=<bean:write name="NewTimeSheetForm" property="timesheetID"/>",'','toolbar=no,status=no,scrollbars=yes,location=no,menubar=no,directories=no');
    return childWindow;
  }


  function openNewTimeSheet()
  {
    c_goTo('/hr/timesheet_new.do');
  }

  function deleteTimeSheet()
  {
    c_goTo('/hr/timesheet_delete.do?rowId=<bean:write name="NewTimeSheetForm" property="timesheetID"/>');
  }


  function cancelAction()
  {
    c_goTo('/hr/timesheet_list.do');
  }

  function doDateCheck(from, to)
  {
    if (Date.parse(from) <= Date.parse(to))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  function disableButtons()
  {
    var iStatus = document.NewTimeSheetForm.status1.value;

    if(iStatus == 0)
    {
      document.NewTimeSheetForm.newtimesheet.disabled = true;
    }
  }
 -->
</script>

<html:form action="/hr/save_timesheet.do"  onsubmit="return openNewTimeSlip();">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr height="17">
    <td class="sectionHeader" colspan="2" width="100%">
      <%
      TimeSheetVO timeSheetVO = (TimeSheetVO)session.getAttribute("timesheetvo");
      if((sErrorsPresent != null) || (request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("EDIT")||(request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("DUPLICATE"))))
      {
      %>
      <bean:message key="label.hr.timesheetformdetail"/>
      <%
      }
      else
      {
      %>
      <bean:message key="label.hr.newtimesheet"/>
      <%
      }
      %>
    </td>
  </tr>

  <tr>
    <td width="50%">
			<table  border="0" cellspacing="0" cellpadding="3" width="100%">
        <tr>
			    <td class="labelCell"><bean:message key="label.hrs.hr.ReportingTo" /></td>
			    <td class="contentCell">
				    <html:text property="reportingTo" styleClass="inputBox" size="40" readonly="true" />
				    <app:cvbutton property="lookup" styleClass="normalButton" style="width:5em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "" onclick="return openIndividualLookUp('individual')">
					  	<bean:message key="label.lookup"/>
				    </app:cvbutton>
				    <html:hidden property="reportingToId" styleClass="inputBox"/>
			     </td>
			  </tr>
			  <tr>
			    <td class="labelCell"><bean:message key="label.hr.employees"/>:</td>
          <td class="contentCell">
				    <html:text property="employee" styleClass="inputBox" size="40" readonly="true"/>
				     <app:cvbutton property="lookup" styleClass="normalButton" style="width:5em;" statuswindow="label.general.blank" tooltip="false"  onclick="return openIndividualLookUp('employee')">
						<bean:message key="label.lookup"/>
				     </app:cvbutton>
					<html:hidden property="employeeId" styleClass="inputBox"/>
			    </td>
			  </tr>
			  <tr>
			    <td class="labelCell"><bean:message key="label.hr.from"/>:</td>
          <td class="contentCell">
            <html:text property="frommonth" styleClass= "inputBox" size="2" maxlength="2"/>
            /
            <html:text property="fromday" styleClass= "inputBox" size="2" maxlength="2"/>
            /
            <html:text property="fromyear" styleClass= "inputBox" size="4" maxlength="4"/>
            <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" onclick="popupCalendar();"/>
			    </td>
			  </tr>
			  <tr>
			    <td class="labelCell"><bean:message key="label.hr.to"/>:</td>
          <td class="contentCell">
            <html:text property="tomonth" styleClass= "inputBox" size="2" maxlength="2"/>
            /
            <html:text property="today" styleClass= "inputBox" size="2" maxlength="2"/>
            /
            <html:text property="toyear" styleClass= "inputBox" size="4" maxlength="4"/>
            <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" onclick="popupCalendar();"/>
			    </td>
			  </tr>
			  <tr>
			    <td class="labelCell"><bean:message key="label.hr.description"/>:</td>
          <td class="contentCell">
            <html:textarea property="description" styleId="description" rows="4" cols="50" styleClass="inputBox"  />
			    </td>
			  </tr>
			</table>
    </td>
    <td valign="top" align="left" width="50%">
			<table width="100%" border="0" cellpadding="3" cellspacing="0">
			  <tr>
			    <td class="labelCell"><bean:message key="label.hr.notes"/>:</td>
          <td class="contentCell">
				    <html:textarea property="notes" rows="4" cols="50" styleClass="inputBox"  />
			    </td>
			  </tr>
        <%
        if((sErrorsPresent != null) || (request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("EDIT")))//||(request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("DUPLICATE"))))
        {
          %>
          <tr>
            <td class="labelCell"><bean:message key="label.hr.status"/>:</td>
            <td class="contentCell">
              <html:select property="status1" styleClass="inputBox">
                <html:option value="1"><bean:message key="label.hr.pending"/></html:option>
                <html:option value="0"><bean:message key="label.hr.approved"/></html:option>
              </html:select>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.hr.created"/>:</td>
            <td class="contentCell">
              <%
              SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - h:mm a");
              if(timeSheetVO != null)
              {
              %>
              <%=dateFormat.format(timeSheetVO.getCreatedDate())%> - <%=timeSheetVO.getCreatorName()%>
              <%
              }
              %>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.hr.modified"/>:</td>
            <td class="contentCell">
              <%
              if(timeSheetVO != null)
              {
              %>
                <%=dateFormat.format(timeSheetVO.getCreatedDate())%>  - <%=timeSheetVO.getCreatorName()%>
              <%
              }
              %>
            </td>
          </tr>
        <%
        }
        %>
      </table>
    </td>
  <tr>
    <td colspan="2" valign="top">
      <!-- Item -->
      <%
      DisplayList DL = (DisplayList)request.getAttribute("timesliplist");
      %>
      <table border="0" cellspacing="0" cellpadding="0"  width="100%">
        <tr>
          <td colspan="7" class="mainHeader"><bean:message key="label.hr.relatedtimeslipitems"/></td>
        </tr>
        <tr>
          <td colspan="7" class="detailButtonHeader">
            <%
            if((sErrorsPresent != null) || ((request.getAttribute("SAMETIMESHEET") == null) || (request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("EDIT")||request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("DUPLICATE"))))
            {
            %>
            <app:cvbutton property="addslip" styleClass="normalButton" style="width:8em;" statuswindow="button.hr.common.newslip" tooltip="false"  onclick="return addTimeSlip();">
            <bean:message key="button.hr.common.newslip"/>
            </app:cvbutton>
            <%
            }else
            {
            %>
            <app:cvsubmit property="addslip" styleClass="normalButton" style="width:8em;" statuswindow="button.hr.common.newslip" tooltip="false"  onclick="setFlag(true);">
            <bean:message key="button.hr.common.newslip"/>
            </app:cvsubmit>
            <%
            }
            %>
            <app:cvbutton property="deleteslip" styleClass="normalButton" style="width:9em;" statuswindow="button.hr.common.deleteslip" tooltip="false"  onclick="deleteSlips();">
            <bean:message key="button.hr.common.deleteslip"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="listHeader">
            <a class="listHeader" href="javascript:void(0);" onclick="vl_selectAllRows();">
              <html:img page="/images/icon_check.gif" title="Check All" alt="Check All" border="0" /> <bean:message key="label.hr.all"/>
            </a>
          </td>
          <td class="listHeader" ><span class="listHeader"><bean:message key="label.hr.timeslipid"/></span></td>
          <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.description"/></span></td>
          <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.starttime"/></span></td>
          <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.endtime"/></span></td>
          <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.duration"/></span></td>
          <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.date"/></span></td>
        </tr>
        <%

            int iIndex = 0;
            if(DL != null)
            {
                Set listkey = DL.keySet();
                Iterator it =  listkey.iterator();
                while( it.hasNext() )
                {
                String str = ( String )it.next();
                StringMember sm=null;
                IntMember im = null;
                StringMember sm1=null;
                //IntMember reference = null;
                PureDateMember pdm = null;
                PureTimeMember ptm = null;

                ListElement ele  = ( ListElement )DL.get( str );
                sm = (StringMember)ele.get("Description");
                String sDescription = sm.getDisplayString();
                im = (IntMember)ele.get("ID");

                sm = (StringMember)ele.get("Reference");
                String reference = "";
                if (sm != null){
                  reference = sm.getDisplayString();
                }

                //int id  =  ((Integer)im.getMemberValue()).intValue();
                String id = im.getDisplayString();

                ptm = (PureTimeMember)ele.get("StartTime");
                String sStartTime = ptm.getDisplayString();
                ptm = (PureTimeMember)ele.get("EndTime");
                String sEndTime = ptm.getDisplayString();
                pdm = (PureDateMember)ele.get("Date");
                String sDate = pdm.getDisplayString();
                sm = (StringMember)ele.get("Duration");
                String sDuration = sm.getDisplayString();

                String classTD = "tableRowOdd";
                if(iIndex % 2 != 0)
                {
                  classTD = "tableRowEven";
                }
                %>
                <tr>
                  <td class="<%=classTD%>">
                    <input type="checkbox" name="checkbox" value=<%=id%> onclick="vl_selectRow(this, false);">
                  </td>
                  <td class="<%=classTD%>"><a href="javascript:c_openWindow('/projects/view_timeslip.do?rowId=<%=id%>','Timeslip', 500, 500, '')"><%=id%></a></td>
                  <td class="<%=classTD%>"><span ><%=sDescription%><br><%=reference%></span></td>
                  <td class="<%=classTD%>"><%=sStartTime%></td>
                  <td class="<%=classTD%>"><%=sEndTime%></td>
                  <td class="<%=classTD%>"><%=sDuration%></td>
                  <td class="<%=classTD%>"><%=sDate%></td>
                </tr>
                <%
                iIndex++;
                }//End of while
          }//End of if condition
          else
          {
            %>
            <tr height="1">
              <td class="tableRowOdd" align="center" colspan="6"><span class="labelBold"><bean:message key="label.hr.noitemsfound"/></span></td>
            </tr>
            <%
          }
          %>
        <tr>
          <td colspan="3" class="itemsContainer">
            <div align="right"><strong><bean:message key="label.hr.totalitems"/>:</strong></div>
          </td>
          <td class="itemsContainer">
            <div align="center"><%=iIndex%></div>
          </td>
          <td class="itemsContainer">
            <div align="right"><strong><bean:message key="label.hr.timetotal"/>:</strong></div>
          </td>

          <%
          if(timeSheetVO != null)
          {
          %>
            <td class="itemsContainer"><%=timeSheetVO.getTotalDuration()%></td>
          <%
          }else
          {
          %>
            <td class="itemsContainer">0.0</td>
          <%
          }
          %>
          <td class="itemsContainer" nowrap>&nbsp;</td>
        </tr>
      </table>
      <!-- Item -->

		</td>
  </tr>
  <tr>
  	<td colspan="2" width="100%">
        <%
        if((sErrorsPresent != null) || (request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("EDIT")||(request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("DUPLICATE"))))
        {
        %>
        <app:cvbutton property="newtimesheet" styleClass="normalButton" style="width:10em;" statuswindow="button.hr.newtimesheet" tooltip="false" statuswindowarg= "label.general.blank" onclick="openNewTimeSheet();" >
        <bean:message key="button.hr.newtimesheet"/>
        </app:cvbutton>
        <%
        }//End of the TYPEOFOPERATION
        %>

        <app:cvsubmit property="saveclose" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false"  statuswindowarg= "label.general.blank">
          <bean:message key="label.saveandclose"/>
        </app:cvsubmit>

       <app:cvsubmit property="savenew" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" >
          <bean:message key="label.saveandnew"/>
        </app:cvsubmit>

       <%
        if((sErrorsPresent != null) || (request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("EDIT")||(request.getParameter("TYPEOFOPERATION") != null && ((String)request.getParameter("TYPEOFOPERATION")).equals("DUPLICATE"))))
        {
        %>
        <app:cvbutton property="deletetimesheet" styleClass="normalButton" style="width:8em;" statuswindow="button.hr.deletetimesheet" tooltip="false" statuswindowarg= "label.general.blank" onclick="deleteTimeSheet();">
        <bean:message key="button.hr.deletetimesheet"/>
        </app:cvbutton>
        <%
        }
        %>

        <app:cvbutton property="cancel" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="cancelAction();">
        <bean:message key="label.cancel"/>
        </app:cvbutton>
    </td>
  </tr>
</table>
<%
if(timeSheetId == null){
  timeSheetId="";
}
%>
<html:hidden property="timesheetID" />
<input type="hidden" name="timeSlipFlag" value="false">

</html:form>


<script>
<%
String timeSlipFlag = (String) request.getAttribute("timeSlipFlag");
if(timeSlipFlag != null && timeSlipFlag.equals("true") ){
%>
    flag= true;
<%
}
%>

if(flag){
  childWindow = c_openWindow("/projects/new_timeslip.do?timesheetID=<bean:write name="NewTimeSheetForm" property="timesheetID"/>",'','toolbar=no,status=no,scrollbars=yes,location=no,menubar=no,directories=no');
  flag=false;
}
</script>
