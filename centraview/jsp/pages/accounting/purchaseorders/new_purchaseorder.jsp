<%--
 * $RCSfile: new_purchaseorder.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:55 $ - $Author: mcallist $
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
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.account.common.*" %>
<%@ page import="com.centraview.account.purchaseorder.PurchaseOrderForm" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>

<%
	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
	GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
	Vector statusVec = (Vector)gml.get("AccountingStatus");
	pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
	Vector termVec   = (Vector)gml.get("AccountingTerms");
	pageContext.setAttribute("termVec", termVec, PageContext.PAGE_SCOPE);
%>
<html:form action="/accounting/new_purchaseorder.do" >
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="saveChanges('save');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="saveclose" styleId="saveclose" styleClass="normalButton" onclick="saveChanges('saveClose');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="savenew" styleId="savenew" styleClass="normalButton" onclick="saveChanges('saveNew');">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="saveChanges('cancel');">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td>
    	<span class="BoldText"><c:out value="${userobject.entityName}"/></span>
    </td>
    <td align="right"><span class="accountingPageHeader"><bean:message key="label.accounting.newpurchaseorder"/></span></td>
  </tr>
  <tr>
  	<td>
			<span class="PlainText"><c:out value="${userobject.addressVO.street1}"/></span>
		</td>
	</tr>
  <tr>
  	<td>
			<span class="PlainText"><c:out value="${userobject.addressVO.street2}"/></span>
		</td>
	</tr>
  <tr>
  	<td>
			<span class="PlainText">
				<c:out value="${userobject.addressVO.city}"/>,
				<c:out value="${userobject.addressVO.stateName}"/>
				<c:out value="${userobject.addressVO.countryName}"/>
				<c:out value="${userobject.addressVO.zip}"/>
			</span>
		</td>
	</tr>
  <tr>
  	<td>
			&nbsp;
  	</td>
  </tr>
  <tr>
    <td>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.accounting.vendor"/></td>
          <td class="contentCell">
            <html:text property="vendorName" styleId="customerName" styleClass="inputBox" size="40" readonly="true" />
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="c_lookup('Vendor');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td>
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td colspan="3" class="sectionHeader"><bean:message key="label.accounting.billto"/></td>
        </tr>
        <tr>
          <td colspan="3">
            <html:textarea property="billto" styleClass="inputBox" rows="4" cols="50" readonly="true"/>
          </td>
        </tr>
        <tr>
          <td align="center">
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="addressLookup('billto');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td align="right">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td class="sectionHeader"><bean:message key="label.accounting.shipto"/></td>
        </tr>
        <tr>
          <td>
            <html:textarea property="shipto" styleClass="inputBox" rows="4" cols="50" readonly="true"/>
          </td>
        </tr>
        <tr>
          <td align="center">
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="addressLookup('shipto');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td colspan="2" align="right">
      <table border="0" cellpadding="3" cellspacing="1" class="mediumTableBorders">
        <tr>
          <td class="sectionHeader"><bean:message key="label.accounting.status"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.date"/></td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <app:cvselect property="statusid" styleClass="inputBox" modulename="PurchaseOrder" fieldname="statusId">
              <app:cvoptions collection="statusVec" property="id" labelProperty="name" />
            </app:cvselect>
          </td>
          <td class="mediumTableBorders">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><app:cvtext property="month" readonly="true" styleClass="inputBox" size="2" modulename="PurchaseOrder" fieldname="purchaseorderDate"/></td>
                <td>/</td>
                <td><app:cvtext property="date" readonly="true" styleClass="inputBox" size="2" modulename="PurchaseOrder" fieldname="purchaseorderDate"/></td>
                <td>/</td>
                <td><app:cvtext property="year" readonly="true" styleClass="inputBox" size="5" modulename="PurchaseOrder" fieldname="purchaseorderDate"/></td>
                <td><a class="plainLink" onclick="openSelectDateTime()"><html:img page="/images/icon_calendar.gif" width="19" height="19" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td colspan="2" align="right">
      <table border="0" cellpadding="3" cellspacing="1" class="mediumTableBorders">
        <tr>
          <td class="sectionHeader"><bean:message key="label.accounting.terms"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.accountmanager"/></td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <app:cvselect property="termid" styleClass="inputBox" modulename="PurchaseOrder" fieldname="termId">
              <app:cvoptions collection="termVec" property="id" labelProperty="name" />
            </app:cvselect>
          </td>
          <td class="mediumTableBorders">
						<app:cvtext property="accountmanagerName" readonly="true" styleClass="inputBox" size="30" modulename="PurchaseOrder" fieldname="accountManagerId"/>
						<html:button property="lookup" styleClass="normalButton" onclick="c_lookup('Employee')">
              <bean:message key="label.lookup"/>
            </html:button>
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
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="saveChanges('save');">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="saveclose" styleId="saveclose" styleClass="normalButton" onclick="saveChanges('saveClose');">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvbutton property="savenew" styleId="savenew" styleClass="normalButton" onclick="saveChanges('saveNew');">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="saveChanges('cancel');">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<html:hidden property="vendorId"/>
<html:hidden property="billtoID"/>
<html:hidden property="shiptoID"/>
<html:hidden property="accountmanagerid"/>
<input type=hidden name="removeID" id="removeID">
<input type=hidden name="btnClicked" id="btnClicked" >
</html:form>

<script language="javascript">

function setVendor(vendorLookupValues){
	name = vendorLookupValues.Name;
	id = vendorLookupValues.idValue;
	acctmgrid = vendorLookupValues.acctManagerID;
	acctmgrname = vendorLookupValues.acctManager;

	document.purchaseForm.vendorName.disabled = false;
	document.purchaseForm.vendorName.value = name;
	document.purchaseForm.vendorId.value = id;
}

function addressLookup(toBox) {
	toWhere = toBox;
	vid = document.purchaseForm.vendorId.value;
	if(vid == 0) {
		alert("<bean:message key='label.alert.selectvendor'/>");
		return;
	}
	c_openWindow('/AddressLookup.do?contactID='+vid+'&ContactID=1', '', 400, 400,'');
}

function setAddress(addressLookupValues) {
	name = addressLookupValues.Name;
	id = addressLookupValues.ID;
	jurisdictionID = addressLookupValues.jurisdictionID;

	if (toWhere == "billto") {
		document.purchaseForm.billto.readonly = false;
		document.purchaseForm.billto.value = name;
		document.purchaseForm.billtoID.value = id;
		idAddress = id;
		document.purchaseForm.billto.readonly = true;
	} else if(toWhere == "shipto") {
		document.purchaseForm.shipto.readonly = false;
		document.purchaseForm.shipto.value = name;
		document.purchaseForm.shiptoID.value = id;
		idAddress = id;
		document.purchaseForm.shipto.readonly = true;
	}
}

function openSelectDateTime() {
	var startDate = document.purchaseForm.month.value + "/" + document.purchaseForm.date.value + "/" + document.purchaseForm.year.value;
	if (startDate == "//"){ startDate = ""; }
	c_openWindow('/calendar/select_date_time.do?dateTimeType=1&startDate='+startDate+'&endDate=&startTime=&endTime=', 'selectDateTime', 350, 500, '');
}

/**
 * Called by the /calendar/select_date_time.do popup, this function takes
 * input from that screen via the function parameters, and populates the
 * form fields on this JSP for the Estimated Close Date field.
 * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION!!!!
 */
function setDateTimeFromPopup(startDate, endDate, startTime, endTime) {
	var startDateArray = startDate.split("/");
	if (startDateArray == null || startDateArray.length < 3) {
		document.purchaseForm.month.value = "";
		document.purchaseForm.date.value = "";
		document.purchaseForm.year.value = "";
	} else {
		document.purchaseForm.month.value = startDateArray[0];
		document.purchaseForm.date.value = startDateArray[1];
		document.purchaseForm.year.value = startDateArray[2];
	}
	return(true);
}

function setEmployee(individualLookupValues) {
	firstName = individualLookupValues.firstName;
	id = individualLookupValues.individualID;
	middleName = individualLookupValues.middleName;
	lastName = individualLookupValues.lastName;
	title = individualLookupValues.title;

	document.purchaseForm.accountmanagerName.readonly = false;
	document.purchaseForm.accountmanagerid.value = id;
	document.purchaseForm.accountmanagerName.value = firstName+" "+middleName+" "+lastName;
	document.purchaseForm.accountmanagerName.readonly = true;
	idInd = id;
}

function fnItemLookup() {
	var hidden="";
	var str="";
	var k=0;
	for (i = 0; i < document.purchaseForm.elements.length; i++) {
		if (document.purchaseForm.elements[i].type == "checkbox") {
			k++;
		}
	}
	if (k > 1) {
		for (i = 0; i < document.purchaseForm.checkbox.length; i++) {
			hidden = document.purchaseForm.checkbox[i].value + ",";
			str = str + hidden;
			}
		str = (str.substring(0, str.length - 1));
	} else if (k == 1) {
		hidden = document.purchaseForm.checkbox.value + ",";
		str = str + hidden;
		str = (str.substring(0, str.length - 1));
	}
	c_lookup('Item',str);
}

function setItem(itemLookupValues) {
	newItemID = itemLookupValues.IDs;
	removeIDs = itemLookupValues.RemoveIDs;
	flag = itemLookupValues.Flag;
	document.getElementById('theitemid').value = newItemID;
	document.getElementById('itemname').value = '';
	document.getElementById('itemdesc').value = '';
	document.getElementById('removeID').value = removeIDs;
	if (flag) {
		document.purchaseForm.action = "<html:rewrite page="/accounting/new_purchaseorder.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>";
	} else {
		document.purchaseForm.action = "<html:rewrite page="/accounting/new_purchaseorder.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>";
	}
	document.purchaseForm.submit();
}

function fnCheckBox() {
	if (! checkCheckbox()) {
		alert("<bean:message key='label.alert.selectrecord'/>");
		return false;
	}
	var hidden = "";
	var str = "";
	var k = 0;
	if (document.purchaseForm.checkbox == null) {
		alert("<bean:message key='label.alert.noitemstoberemoved'/>.");
		return;
	}
	for (i = 0; i < document.purchaseForm.elements.length; i++) {
		if (document.purchaseForm.elements[i].type == "checkbox") {
			k++;
		}
	}
	if (k > 1) {
		for (i = 0; i < document.purchaseForm.checkbox.length; i++) {
			if (document.purchaseForm.checkbox[i].checked) {
				hidden = document.purchaseForm.checkbox[i].value + ",";
				str = str + hidden;
			}
		}
		str = (str.substring(0, str.length - 1));
	} else if (k == 1) {
		hidden = document.purchaseForm.checkbox.value + ",";
		str = str + hidden;
		str = (str.substring(0, str.length - 1));
	}
	flag = false;
	lookupValues = {IDs: "", RemoveIDs: str, Flag: false}
	setItem(lookupValues);
}

function checkCheckbox() {
	if (document.purchaseForm.checkbox.length) {
		for (i = 0; i < document.purchaseForm.checkbox.length; i++) {
			if (document.purchaseForm.checkbox[i].checked) {
				return true;
			}
		}
	} else {
		return document.purchaseForm.checkbox.checked;
	}
}

function saveChanges(act) {
	if (act == 'cancel') {
		window.document.purchaseForm.action = '<html:rewrite page="/accounting/purchaseorder_list.do"/>';
		window.document.purchaseForm.submit();
		return true;
	} else if (act == 'save') {
		window.document.purchaseForm.btnClicked.value = "save";
	} else if (act == 'saveClose') {
		window.document.purchaseForm.btnClicked.value = "saveClose";
	} else if (act == 'saveNew') {
		window.document.purchaseForm.btnClicked.value = "saveNew";
	}
	window.document.purchaseForm.action = '<html:rewrite page="/accounting/save_new_purchaseorder.do"/>';
	window.document.purchaseForm.submit();
	return true;
}
</script>