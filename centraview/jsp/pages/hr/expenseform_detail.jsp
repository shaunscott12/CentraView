<%
/**
 * $RCSfile: expenseform_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page language="java" %>
<%@ page import="java.text.*" %>
<%@ page import="java.net.*" %>
<%@ page import="com.centraview.common.UserObject" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.hr.expenses.HrExpenseLines" %>
<%@ page import="com.centraview.hr.expenses.HrExpenseForm" %>
<%@ page import="com.centraview.hr.helper.ExpenseFormVO" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<script language="JavaScript1.2">
	var flag = true;

	function submitForm(newName, newSku, newItemID,removeIDs  )
	{
		document.HrExpenseForm.theitemid.value = newItemID;//newItemID.substring(0,newItemID.length-1);
		document.HrExpenseForm.itemname.value = newSku.substring(0,newSku.length-1);
		document.HrExpenseForm.itemdesc.value = newName.substring(0,newName.length-1);
		document.HrExpenseForm.removeID.value = removeIDs;

		if(flag)
		{
			document.HrExpenseForm.action = "<html:rewrite page="/hr/expenseform_new.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>&theitemid="+newItemID + "&reporttoID="+document.HrExpenseForm.reporttoID.value;
		}else{
      document.HrExpenseForm.action = "<html:rewrite page="/hr/expenseform_new.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>";
		}
		document.HrExpenseForm.submit();
	}

	function newExpense()	{
		c_goTo("/hr/expenseform_new.do?expenseFormID=0");
		return false;
	}

	function saveExpense(typeOfSave)
	{
		var sFromDate = document.HrExpenseForm.fromday.value + "/" + document.HrExpenseForm.frommonth.value + "/" + document.HrExpenseForm.fromyear.value;
    var sToDate   = document.HrExpenseForm.today.value + "/" + document.HrExpenseForm.tomonth.value + "/" + document.HrExpenseForm.toyear.value;
		doDateCheck(sFromDate,sToDate);

    <% if (request.getAttribute("DUPLICATE") != null){ %>
      document.HrExpenseForm.action="<html:rewrite page="/hr/save_expenseform.do"/>?DUPLICATE=TRUE&typeOfSave=" + typeOfSave ;
    <% }else{ %>
      document.HrExpenseForm.action="<html:rewrite page="/hr/save_expenseform.do"/>?typeOfSave=" + typeOfSave ;
    <% } %>
    document.HrExpenseForm.submit();
    return false;
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


	function deleteHRExpense()
	{
	  c_goTo('/hr/expenseform_delete.do?rowId=<%=request.getParameter("expenseFormID")%>');
	}

	function cancelExpense()
	{
		c_goTo("/hr/expenseform_list.do");
		return false;
	}

  function popupCalendar()
  {
    var startDate = document.forms.HrExpenseForm.frommonth.value + "/" + document.forms.HrExpenseForm.fromday.value  + "/" + document.forms.HrExpenseForm.fromyear.value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = document.forms.HrExpenseForm.tomonth.value + "/" + document.forms.HrExpenseForm.today.value + "/" + document.forms.HrExpenseForm.toyear.value;
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
      document.forms.HrExpenseForm.frommonth.value = "";
      document.forms.HrExpenseForm.fromday.value = "";
      document.forms.HrExpenseForm.fromyear.value = "";
    }else{
      document.forms.HrExpenseForm.frommonth.value = startDateArray[0];
      document.forms.HrExpenseForm.fromday.value = startDateArray[1];
      document.forms.HrExpenseForm.fromyear.value = startDateArray[2];
    }

    var endDateArray = endDate.split("/")
    if (endDateArray == null || endDateArray.length < 3){
      document.forms.HrExpenseForm.tomonth.value = "";
      document.forms.HrExpenseForm.today.value = "";
      document.forms.HrExpenseForm.toyear.value = "";
    }else{
      document.forms.HrExpenseForm.tomonth.value = endDateArray[0];
      document.forms.HrExpenseForm.today.value = endDateArray[1];
      document.forms.HrExpenseForm.toyear.value = endDateArray[2];
    }
    return(true);
  }


	var lookuptype = "";
	function openIndividualLookUp(luType)
	{
    lookuptype  =  luType;
    c_lookup('Employee');
	}

	function setEmployee(individualLookupValues){
		firstName = individualLookupValues.firstName;
		id = individualLookupValues.individualID;
		middleName = individualLookupValues.middleName;
		lastName = individualLookupValues.lastName;
		title = individualLookupValues.title;

    if(lookuptype == 'reportto')
    {
			document.HrExpenseForm.reportto.value = firstName+" "+middleName+" "+lastName;
			document.HrExpenseForm.reporttoID.value = id
			document.HrExpenseForm.reportto.readonly = false;
    }
    else if(lookuptype == 'employee')
    {
			document.HrExpenseForm.employee.value = firstName+" "+middleName+" "+lastName;
			document.HrExpenseForm.employeeID.value = id
			document.HrExpenseForm.employee.readonly = false;
    }
	}

</Script>
<html:errors/>
<html:form action="/hr/expenseform_new.do">
<app:TitleWindow moduleName="HrExpenseForm:New ExpenseForm"/>
<%
	com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)request.getAttribute( "userobject" );//get the user object
	if(userobjectd != null)
	{
		int individualID = userobjectd.getIndividualID();
	}

	int expenseFormID  = 0;
	String status = "Unpaid";

	ExpenseFormVO expenseformVo = (ExpenseFormVO) session.getAttribute("ExpenseFormVO");
	if(expenseformVo != null)
	{
		status = expenseformVo.getStatus();
		expenseFormID = expenseformVo.getExpenseFormID();
		request.setAttribute("ExpenseFormVO",expenseformVo);
		session.setAttribute("ExpenseFormVO",expenseformVo);
	}

	HrExpenseForm hrExpenseForm = (HrExpenseForm) session.getAttribute("HrExpenseForm");
	if(hrExpenseForm  != null)
  {
    expenseFormID = hrExpenseForm.getExpenseFormID();
    status = hrExpenseForm.getStatus();
  }else if(request.getParameter("expenseFormID") != null){
    Long expenseFormIDLong = new Long((String)request.getParameter("expenseFormID"));
    expenseFormID = expenseFormIDLong.intValue();
  }else{
    expenseFormID = 0;
  }
%>
<html:hidden property="reporttoID" ></html:hidden>
<html:hidden property="expenseFormID" ></html:hidden>
<html:hidden property="employeeID" ></html:hidden>
<html:hidden property="theitemid" ></html:hidden>
<%
UserObject userobject =(UserObject) session.getAttribute("userobject");
GregorianCalendar gc = new GregorianCalendar();
TimeZone tz;
if ( (userobject !=null) && ((userobject.getUserPref()).getTimeZone() != null) )
{
tz = TimeZone.getTimeZone((userobject.getUserPref()).getTimeZone());
}
else {
	tz = TimeZone.getTimeZone("EST");
}

gc.setTimeZone(tz);
long  time = gc.getTimeInMillis();
Date dd = new Date(time);
DateFormat   dayMonth = new SimpleDateFormat("EEEEEEEEEEEE   MMMMM");
DateFormat   date = new SimpleDateFormat("dd");
DateFormat   yearTime = new SimpleDateFormat("yyyy - hh:mm aaa") ;
%>
<html:hidden property="createdDate" value='<%=dd.toString()%>'></html:hidden>
<html:hidden property="modifiedDate" value='<%=dd.toString()%>'></html:hidden>
<html:hidden property="removeID"></html:hidden>
<table border="0" cellspacing="0" cellpadding="2" width="100%" class="formTable">
  <tr>
    <td class="sectionHeader" colspan="2" width="100%">
      <% if(expenseFormID > 0)
      {
      %>
        <bean:message key="label.hrs.hr.editexpense"/>
      <%
      }
      else
      {
      %>
        <bean:message key="label.hrs.hr.newhr"/>
      <%
      }
      %>
    </td>
  </tr>
  <tr>
    <td width="50%" valign="top">
      <table width="100%" border="0" cellpadding="3">
        <tr>
          <td class="labelCell"><bean:message key="label.hrs.hr.ReportingTo"/></td>
          <td class="contentCell">
            <html:text property="reportto" styleClass= "inputBox" size="35" readonly="true"/>
            <app:cvbutton property="reporttoLookupButton" styleClass="normalButton" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="return openIndividualLookUp('reportto')">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.hrs.hr.Employee"/></td>
          <td class="contentCell">
            <html:text property="employee" styleClass= "inputBox" size="35" readonly="true"/>
            <app:cvbutton property="employeeLookupButton" styleClass="normalButton" statuswindow="lable.hrs.common.lookup" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openIndividualLookUp('employee')">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.hrs.hr.From"/></td>
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
          <td class="labelCell"><bean:message key="label.hrs.hr.To"/></td>
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
          <td class="labelCell"><bean:message key="label.hrs.hr.Description"/></td>
          <td class="contentCell">
            <html:textarea property="formDescription" styleId="formDescription" styleClass= "inputBox" cols="42" rows="4"/>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top">
      <table width="100%" border="0" cellpadding="3">
        <tr>
          <td class="labelCell"><bean:message key="label.hrs.hr.Notes"/></td>
          <td class="contentCell">
            <html:textarea property="notes" styleClass= "inputBox" cols="50" rows="4"/>
          </td>
        </tr>
        <%
          if(expenseFormID > 0)
          {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - h:mm a");
          %>
            <tr>
              <td class="labelCell"><bean:message key="label.hr.status"/>:</td>
              <td class="contentCell">
                <html:select property="status" styleClass="inputBox">
                <html:option value="Paid"><bean:message key="label.hr.paid"/></html:option>
                <html:option value="Unpaid"><bean:message key="label.hr.unpaid"/></html:option>
                </html:select>
              </td>
            </tr>
            <tr>
              <td class="labelCell"><bean:message key="label.hr.created"/>:</td>
              <td class="contentCell">
                <%=dateFormat.format(expenseformVo.getCreatedDate())%>  - <%=expenseformVo.getCreatorName()%>
              </td>
            </tr>
            <tr>
              <td class="labelCell"><bean:message key="label.hr.modified"/>:</td>
              <td class="contentCell">
                <%=dateFormat.format(expenseformVo.getModifiedDate())%> - <%=expenseformVo.getModifiedByName()%>
              </td>
            </tr>
          <%
          }
        %>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" >
      <table border="0" cellspacing="0" cellpadding="3" width="100%">
        <%
          HrExpenseLines hrExpenseLines = hrExpenseForm.getItemLines();
          request.setAttribute("HrExpenseLines", hrExpenseLines);
        %>
        <jsp:include page="expenseform_item_list.jsp" />
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" >
      <%
      if(expenseFormID > 0)
      {
      %>
        <app:cvbutton property="newexpenseform" styleClass="normalButton" statuswindow="hr.expenseform.label.newexpenseform" tooltip="true" statuswindowarg= "label.general.blank" style="width:12em;" onclick="newExpense()">
          <bean:message key="hr.expenseform.label.newexpenseform"/>
        </app:cvbutton>
      <%
      }
      %>
      <!--cw-->
      <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "" onclick="saveExpense('save')">
        <bean:message key="label.save"/>
      </app:cvbutton>

      <app:cvbutton property="saveandclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "" onclick="saveExpense('saveandclose')">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>

      <app:cvbutton property="saveandnew" styleClass="normalButton" statuswindow="label.saveandnew" tooltip="true" statuswindowarg= "" onclick="saveExpense('savenew')">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>

      <% if(expenseFormID > 0)
      {
      %>
        <app:cvbutton property="deleteform" styleClass="normalButton" statuswindow="hr.expenseform.label.deleteform" tooltip="true" statuswindowarg= "" onclick="deleteHRExpense();">
          <bean:message key="hr.expenseform.label.deleteform"/>
        </app:cvbutton>
      <%
      }
      %>

      <app:cvbutton  property="cancel" styleClass="normalButton" onclick="cancelExpense();" >
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>
