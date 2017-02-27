<%--
 * $RCSfile: new_expense.jsp,v $ $Revision: 1.4 $ $Date: 2005/08/10 13:31:55 $ - $Author: mcallist $
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
 * The developer of the Original Code is CentraView. Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved. The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.account.expense.ExpenseForm" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import = "com.centraview.account.common.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>

<%
	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
	ExpenseForm expenseForm = (ExpenseForm) request.getAttribute("expenseform");
	int expenseID = expenseForm.getExpenseID();

	Vector glAccountVec = expenseForm.getGlAccountVec(dataSource);
	pageContext.setAttribute("glAccountVec", glAccountVec, PageContext.PAGE_SCOPE);

	Vector statusVec = expenseForm.getStatusVec();
	pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
%>

<html:form action="/accounting/new_expense.do">
<html:errors/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td>
			<app:cvbutton property="save" styleClass="normalButton" style="width:5em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="saveExpense('save')" >
				<bean:message key="label.save"/>
			</app:cvbutton>
			<app:cvbutton property="saveclose" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="saveExpense('saveclose')" >
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="savenew" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="saveExpense('savenew')" >
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="cancelExpense()">
				<bean:message key="label.cancel"/>
			</app:cvbutton>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader">
			<bean:message key="label.account.expense.heading"/>
		</td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td>
			<table border="0" cellpadding="3" cellspacing="0">
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.glaccount"/>
					</td>
					<td class=contentCell>
						<html:select property="glAccountID" styleClass="inputBox">
							<html:options collection="glAccountVec" property="id" labelProperty="name" styleClass="inputBox"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.title"/>
					</td>
					<td class=contentCell>
						<html:text property="title" styleClass="inputBox" size="30"/>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.description"/>
					</td>
					<td class=contentCell>
						<app:cvtextarea  property="expenseDescription" styleClass="inputBox" modulename="Expense" fieldname="expenseDescription"/>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.entity"/>
					</td>
					<td class=contentCell>
						<html:text property="entity" styleClass="inputBox" readonly="true"  size="30"/>
					</td>
					<td class=contentCell>
						<app:cvbutton property="lookup" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Entity');" >
							<bean:message key="label.lookup"/>
						</app:cvbutton>
		  		</td>
				</tr>
			</table>
		</td>
		<td>
			<table border="0" cellpadding="3" cellspacing="0">
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.status"/>
					</td>
					<td class=contentCell>
						<app:cvselect property="statusID" styleClass="inputBox" modulename="Expense" fieldname="statusIDValue">
							<app:cvoptions collection="statusVec" property="id" labelProperty="name"/>
						</app:cvselect>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.employee"/>
					</td>
					<td class=contentCell>
						<html:text property="employee" styleClass="inputBox" readonly="true" size="30"/>
					</td>
					<td class=contentCell>
						<app:cvbutton property="lookup" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Employee');" >
							<bean:message key="label.lookup"/>
						</app:cvbutton>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.project"/>
					</td>
					<td class=contentCell>
						<html:text property="project" styleClass="inputBox" readonly="true" size="30"/>
					</td>
					<td class=contentCell>
						<app:cvbutton property="lookup" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Project');" >
							<bean:message key="label.lookup"/>
						</app:cvbutton>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.opportunity"/>
					</td>
					<td class=contentCell>
						<html:text property="opportunity" styleClass="inputBox" readonly="true" size="30"/>
					</td>
					<td class=contentCell>
						<app:cvbutton property="lookup" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Opportunity');" >
							<bean:message key="label.lookup"/>
						</app:cvbutton>
					</td>
				</tr>
				<tr>
					<td class=contentCell>
						<bean:message key="label.account.expense.supportticket"/>
					</td>
					<td class=contentCell>
						<html:text property="supportTicket" styleClass="inputBox" readonly="true" size="30"/>
					</td>
					<td class=contentCell>
						<app:cvbutton property="lookup" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="c_lookup('Ticket')" >
							<bean:message key="label.lookup"/>
						</app:cvbutton>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br/>
<jsp:include page="/jsp/pages/accounting/items_list.jsp" />
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
			<app:cvbutton property="save" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="saveExpense('save')" >
				<bean:message key="label.save"/>
			</app:cvbutton>
			<app:cvbutton property="saveclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="saveExpense('saveclose')" >
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="savenew" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="saveExpense('savenew')" >
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="cancelExpense()">
				<bean:message key="label.cancel"/>
			</app:cvbutton>
    </td>
  </tr>
</table>

<html:hidden property="expenseID"/>
<input type=hidden name="removeID" id="removeID">
<html:hidden property="entityID"/>
<html:hidden property="employeeID"/>
<html:hidden property="projectID"/>
<html:hidden property="opportunityID"/>
<html:hidden property="supportTicketID"/>
</html:form>

<script language = "javascript">
function setEntity(entityLookupValues) {
	name = entityLookupValues.entName;
	id = entityLookupValues.entID;
	acctmgrid = entityLookupValues.acctManagerID;
	acctmgrname = entityLookupValues.acctManager;

	document.expenseform.entity.disabled = false;
	document.expenseform.entityID.value = id;
	document.expenseform.entity.value = name;
}

function setEmployee(individualLookupValues) {
	firstName = individualLookupValues.firstName;
	id = individualLookupValues.individualID;
	middleName = individualLookupValues.middleName;
	lastName = individualLookupValues.lastName;
	title = individualLookupValues.title;

	document.expenseform.employee.readonly = false;
	document.expenseform.employeeID.value = id;
	document.expenseform.employee.value = firstName+" "+middleName+" "+lastName;
	document.expenseform.employee.readonly = true;
}

function setProject(lookupValues) {
	document.expenseform.project.disabled = false;
	document.expenseform.projectID.value = lookupValues.idValue;
	document.expenseform.project.value = lookupValues.Name;
}

function setOpp(lookupValues) {
	name = lookupValues.Name;
	id = lookupValues.idValue;

	document.expenseform.opportunity.disabled = false;
	document.expenseform.opportunityID.value = id;
	document.expenseform.opportunity.value = name;
}

function setTicket(lookupValues) {
	name = lookupValues.Name;
	id = lookupValues.idValue;
	document.expenseform.supportTicket.readonly = false;
	document.expenseform.supportTicket.value = name;
	document.expenseform.supportTicketID.value = id;
}

function fnItemLookup() {
	var hidden="";
	var str="";
	var k=0;
	for (i = 0; i < document.expenseform.elements.length; i++) {
		if (document.expenseform.elements[i].type == "checkbox") {
			k++;
		}
	}
	if (k > 1) {
		for (i = 0; i < document.expenseform.checkbox.length; i++) {
			hidden = document.expenseform.checkbox[i].value + ",";
			str = str + hidden;
		}
		str = (str.substring(0, str.length - 1));
	} else if (k == 1) {
		hidden = document.expenseform.checkbox.value + ",";
		str = str + hidden;
		str = (str.substring(0, str.length - 1));
	}
    c_lookup('Item',str);
}

  function setItem(itemLookupValues)
  {
    newItemID = itemLookupValues.IDs;
    removeIDs = itemLookupValues.RemoveIDs;
    flag = itemLookupValues.Flag;
	document.getElementById('theitemid').value = newItemID;
	document.getElementById('itemname').value = '';
	document.getElementById('itemdesc').value = '';
	document.getElementById('removeID').value = removeIDs;
	if (flag) {
		document.expenseform.action = "<bean:message key='label.url.root' />/accounting/new_expense.do?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>";
	} else {
		document.expenseform.action = "<bean:message key='label.url.root' />/accounting/new_expense.do?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>";
	}
	document.expenseform.submit();
}

function fnCheckBox() {
	var hidden="";
	var str="";
	var k=0;

	for (i=0 ;i<document.expenseform.elements.length;i++) {
		if (document.expenseform.elements[i].type=="checkbox") {
			k++;
		}
	}
	if (k > 1) {
		for(i=0;i<document.expenseform.checkbox.length;i++) {
			if ( document.expenseform.checkbox[i].checked) {
				hidden=document.expenseform.checkbox[i].value + ",";
				str=str+hidden;
			}
		}
		str=(str.substring(0,str.length-1));
	} else if (k==1) {
		if (document.expenseform.checkbox == null) {
			alert("<bean:message key='label.alert.noitemavailable'/>");
		}
		hidden=document.expenseform.checkbox.value + ",";
		str = str+hidden;
		str = (str.substring(0,str.length-1));
	}
	if(!checkCheckbox()) {
		alert("<bean:message key='label.alert.selectrecord'/>");
		return false;
	}
	flag = false;
    lookupValues = {IDs: "", RemoveIDs: str, Flag: false}
    setItem(lookupValues);
}

function checkCheckbox() {
	if (document.expenseform.checkbox.length) {
		for(i=0;i<document.expenseform.checkbox.length;i++) {
			if (document.expenseform.checkbox[i].checked) {
				return true;
			}
		}
	} else {
		return document.expenseform.checkbox.checked;
	}
}

function saveChanges(act) {
	document.expenseform.action="<bean:message key='label.url.root' />/accounting/save_new_expense.do?typeOfSave=" + act;
	document.expenseform.submit();
	return false;
}

function saveExpense(typeOfSave) {
	document.expenseform.action="<bean:message key='label.url.root' />/accounting/save_new_expense.do?typeOfSave=" + typeOfSave;
	document.expenseform.submit();
	return false;
}

function cancelExpense() {
	document.expenseform.action="<bean:message key='label.url.root' />/accounting/expense_list.do";
	document.expenseform.submit();
	return false;
}
</script>