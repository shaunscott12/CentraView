<%
/**
 * $RCSfile: new_invoice.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/13 22:01:57 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import = "com.centraview.account.common.* ,java.util.*,com.centraview.common.*"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import="com.centraview.account.invoice.InvoiceForm" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>

<%
InvoiceForm invoiceForm = (InvoiceForm)request.getAttribute("invoiceform");
String top = (String) request.getParameter(AccountConstantKeys.TYPEOFOPERATION);
if(request.getAttribute("clearform")!=null)
if ( request.getAttribute("clearform").equals("true") )
{
	if ( top == null )
	{
		invoiceForm = invoiceForm.clearForm(invoiceForm);
		request.setAttribute("ItemLines",null);
	}
}


response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", -1);

String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
Vector statusVec = (Vector)gml.get("AccountingStatus");
pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
Vector termVec  = (Vector)gml.get("AccountingTerms");
pageContext.setAttribute("termVec", termVec, PageContext.PAGE_SCOPE);
UserObject userobject = (UserObject) session.getAttribute("userobject");
AddressVO addVO = userobject.getAddressVO();
%>

<script language="javascript1.2">
<!--
  function fnItemLookup()
  {
    var hidden="";
    var str="";
    var k=0;
    for (i = 0; i < document.invoiceform.elements.length; i++) {
      if (document.invoiceform.elements[i].type == "checkbox") {
        k++;
      }
    }

    if (k > 1) {
      for (i = 0; i < document.invoiceform.checkbox.length; i++) {
        hidden = document.invoiceform.checkbox[i].value + ",";
        str = str + hidden;
      }
      str = (str.substring(0, str.length - 1));
    } else if (k == 1) {
      hidden = document.invoiceform.checkbox.value + ",";
      str = str + hidden;
      str = (str.substring(0, str.length - 1));
    }
    c_lookup('Item',str);
  }

  function callOrderLookup()
  {
    c_lookup('Order');
  }

  function setOrder(lookupValues)
  {
    id = lookupValues.ID;
    name = lookupValues.Name;
    entityID = lookupValues.entityID;
    document.invoiceform.ordername.readonly = false;
    document.invoiceform.ordername.value = id;
    document.invoiceform.orderid.value = id;
    document.invoiceform.ordername.readonly= true;
    document.invoiceform.customerId.value = entityID;
    window.document.invoiceform.action="/centraview/accounting/auto_generate_invoice.do?OrderID="+id;
    window.document.invoiceform.submit();
  }


  function gotocancel()
  {
    window.document.invoiceform.action="<html:rewrite page="/accounting/invoice_list.do"/>";
    window.document.invoiceform.target="_parent";
    window.document.invoiceform.submit();
    return true;
  }

  function saveChanges(param)
  {
    window.document.invoiceform.action="<html:rewrite page="/accounting/save_new_invoice.do"/>?buttonpress="+param;
    window.document.invoiceform.target="_parent";
    window.document.invoiceform.submit();
    return false;
  }

  function setProject(lookupValues)
  {
    name = lookupValues.Name;
    id = lookupValues.idValue;
    document.invoiceform.projectName.readonly = false;
    document.invoiceform.projectName.value = name;
    document.invoiceform.projectid.value = id;
    document.invoiceform.projectName.readonly= true;
  }

  var toWhere = "billto";  // 'billto' is just taken as default value
  function callAddressLookup(toBox)
  {
    var customerId = document.invoiceform.customerId.value;
    if (customerId == '') {
      alert("<bean:message key='label.alert.selectcustomerabove'/>.");
      return;
    }
    toWhere = toBox;
    // 1 sent as hardcoded because contactType = entity/Individual and for entity = 1
    c_openWindow('/AddressLookup.do?contactID=' + customerId + '&contactType=1', '', 400, 400);
  }

  function setAddress(addressLookupValues)
  {
    name = addressLookupValues.Name;
    id = addressLookupValues.ID;
    jurisdictionID = addressLookupValues.jurisdictionID;

    if (toWhere == 'billto') {
      if (jurisdictionID != '0') {
        var attachWidget = document.getElementById('jurisdictionID');
        var newIndex = attachWidget.length ;
        var k = 0;
        while (k < newIndex) {
          if (attachWidget.options[k].value == jurisdictionID) {
            attachWidget.options[k].selected = true;
          }
          k++;
        }
      }
      document.invoiceform.billto.readonly = false;
      document.invoiceform.billto.value = name;
      document.invoiceform.billtoID.value = id;
      document.invoiceform.billto.readonly = true;
    }else{
      document.invoiceform.shipto.readonly = false;
      document.invoiceform.shipto.value = name;
      document.invoiceform.shiptoID.value = id;
      document.invoiceform.shipto.readonly = true;
    }
  }

  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.invoiceform.accountmanagerName.readonly = false;
    document.invoiceform.accountmanagerid.value = id;
    idInd = id;
    document.invoiceform.accountmanagerName.value = firstName+" "+middleName+" "+lastName;
    document.invoiceform.accountmanagerName.readonly = true;
  }

  function setJurisdiction(){
    if ( document.invoiceform.billtoID.value == '' || document.invoiceform.billtoID.value == '0')
    {
      alert("<bean:message key='label.alert.selectbillingaddress'/>");
      return;
    }else{
      saveChanges('save');
    }
  }

  //This is for add item
  function setItem(itemLookupValues)
  {
    newItemID = itemLookupValues.IDs;
    removeIDs = itemLookupValues.RemoveIDs;
    flag = itemLookupValues.Flag;

    document.invoiceform.theitemid.value = newItemID;
    document.invoiceform.itemname.value = '';
    document.invoiceform.itemdesc.value = '';
    document.invoiceform.removeID.value = removeIDs;
    if(flag)
    {
      document.invoiceform.action = "<html:rewrite page="/accounting/new_invoice.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>";
    }
    else
    {
      document.invoiceform.action = "<html:rewrite page="/accounting/new_invoice.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>";
    }
    document.invoiceform.submit();
  }

  function fnCheckBox()
  {
    if (! checkCheckbox()) {
      alert("<bean:message key='label.alert.selectrecord'/>");
      return false;
    }

    var hidden = "";
    var str = "";
    var k = 0;
    if (document.invoiceform.checkbox == null) {
      alert("<bean:message key='label.alert.noitemstoberemoved'/>.");
      return;
    }
    for (i = 0; i < document.invoiceform.elements.length; i++) {
      if (document.invoiceform.elements[i].type == "checkbox") {
        k++;
      }
    }

    if (k > 1) {
      for (i = 0; i < document.invoiceform.checkbox.length; i++) {
        if (document.invoiceform.checkbox[i].checked) {
          hidden = document.invoiceform.checkbox[i].value + ",";
          str = str + hidden;
        }
      }
      str = (str.substring(0, str.length - 1));
    } else if (k == 1) {
      hidden = document.invoiceform.checkbox.value + ",";
      str = str + hidden;
      str = (str.substring(0, str.length - 1));
    }
    flag = false;
    lookupValues = {IDs: "", RemoveIDs: str, Flag: false}
    setItem(lookupValues);
  }

  function checkCheckbox()
  {
    if (document.invoiceform.checkbox.length) {
      for (i = 0; i < document.invoiceform.checkbox.length; i++) {
        if (document.invoiceform.checkbox[i].checked) {
          return true;
        }
      }
    } else {
      return document.invoiceform.checkbox.checked;
    }
  }

  function openSelectDateTime()
  {
    var startDate = document.getElementById('month').value + "/" + document.getElementById('date').value + "/" + document.getElementById('year').value;
    if (startDate == "//"){ startDate = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=1&startDate='+startDate+'&endDate=&startTime=&endTime=', 'selectDateTime', 350, 500, '');
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
      document.getElementById('month').value = "";
      document.getElementById('date').value = "";
      document.getElementById('year').value = "";
    }else{
      document.getElementById('month').value = startDateArray[0];
      document.getElementById('date').value = startDateArray[1];
      document.getElementById('year').value = startDateArray[2];
    }
    return(true);
  }
-->
</script>


<body>
<html:form action="/accounting/new_invoice.do">
<html:hidden property="customerId"/>
<input type=hidden name="removeID">
<input type=hidden name="body" value="ADD" >
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="saveChanges('save');">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="saveChanges('saveclose');">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="saveChanges('savenew');">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
</table>
<br>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="boldText">
      <%
        if (userobject.getEntityName() != null) {
          out.println(userobject.getEntityName());
        }
      %></span><br/>
      <span class="plainText">
      <c:out value="${pageScope.addressVO.street1}" /><br/>
      <c:out value="${pageScope.addressVO.street2}" /><br/>
      <c:out value="${pageScope.addressVO.city}" /> <c:out value="${pageScope.addressVO.stateName}" /> <c:out value="${pageScope.addressVO.zip}" /><br/>
      <c:out value="${pageScope.addressVO.countryName}" />
      </span>
    </td>
    <td align="right" valign="top"><span class="accountingPageHeader"><bean:message key="label.accounting.newinvoice"/></span></td>
  </tr>
</table>
<br>
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="calendarYearlyMonthHead"><bean:message key="label.accounting.order"/></td>
    <td nowrap class="popupTableText">
      <html:text property="ordername" readonly="true" styleClass="inputBox" size="40"/>
      <html:hidden property="orderid"/>
      <app:cvbutton property="lookup" styleClass="normalButton" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="callOrderLookup()">
        <bean:message key="label.lookup"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td>
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td colspan="3" class="sectionHeader"><bean:message key="label.accounting.billto"/>:</td>
        </tr>
        <tr>
          <td colspan="3">
            <html:hidden property="billtoID" styleId="billtoID" />
            <html:textarea property="billto" styleId="billto" styleClass="inputBox" rows="4" cols="50" readonly="true" />
          </td>
        </tr>
        <tr>
          <td align="center">
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="callAddressLookup('billto');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
          <td class="labelCell"><bean:message key="label.accounting.taxjuris"/></td>
          <td class="contentCell">
            <html:select property="jurisdictionID" styleId="jurisdictionID" styleClass="textBoxWhiteBlueBorder" size="1" onchange="return setJurisdiction()">
              <html:option value="0">--<bean:message key="label.accounting.select"/> --</html:option>
              <html:optionsCollection property="jurisdictionVec" value="id" label="name"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
    <td align="right">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td class="sectionHeader"><bean:message key="label.accounting.shipto"/>:</td>
        </tr>
        <tr>
          <td>
            <html:hidden property="shiptoID" styleId="shiptoID" />
            <html:textarea property="shipto" styleId="shipto" styleClass="inputBox" rows="4" cols="50" readonly="true" />
          </td>
        </tr>
        <tr>
          <td align="center">
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="callAddressLookup('shipto');">
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
          <td class="sectionHeader" align="center"><bean:message key="label.accounting.status"/>:</td>
          <td class="sectionHeader" align="center"><bean:message key="label.accounting.date"/>:</td>
          <!--td class="sectionHeader" align="center">External ID</td-->
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <app:cvselect property="statusid" styleClass="inputBox" modulename="InvoiceHistory" fieldname="statusId">
              <app:cvoptions collection="statusVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
          <td class="mediumTableBorders">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text readonly="true" property="month" styleId="month" styleClass="inputBox" size="2" /></td>
                <td>/</td>
                <td><html:text readonly="true" property="date" styleId="date" styleClass="inputBox" size="2" /></td>
                <td>/</td>
                <td><html:text readonly="true" property="year" styleId="year" styleClass="inputBox" size="5" /></td>
                <td><a class="plainLink" onclick="openSelectDateTime()"><html:img page="/images/icon_calendar.gif" width="19" height="19" alt="" /></a></td>
              </tr>
            </table>
          </td>
          <!--td class="mediumTableBorders"></td-->
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td colspan="2" align="right">
      <table border="0" cellpadding="3" cellspacing="1" class="mediumTableBorders">
        <tr>
          <td class="sectionHeader"><bean:message key="label.accounting.po"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.terms"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.accountmanager"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.project"/></td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <app:cvtext property="poid" styleClass="inputBox" size="5" modulename="InvoiceHistory" fieldname="po"/>
          </td>
          <td class="mediumTableBorders">
            <app:cvselect property="termid" styleClass="inputBox" modulename="InvoiceHistory" fieldname="termId" >
              <app:cvoptions collection="termVec" property="id" labelProperty="name"/>
            </app:cvselect>

          </td>
          <td class="mediumTableBorders">
            <html:hidden property="accountmanagerid" styleId="accountmanagerid" />
            <app:cvtext readonly="true" property="accountmanagerName" styleId="accountmanagerName" styleClass="inputBox" size="30" modulename="InvoiceHistory" fieldname="accountManagerId" />
            <app:cvbutton property="lookup" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" modulename="InvoiceHistory" fieldname="accountManagerId" statuswindowarg= "label.general.blank" onclick="c_lookup('Employee')" modulename="InvoiceHistory">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
          <td class="mediumTableBorders">
            <app:cvtext property="projectName" readonly="true" styleClass="inputBox" size="30" modulename="InvoiceHistory" fieldname="projectId"/>
            <html:hidden property="projectid"/>
            <app:cvbutton property="lookup" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" modulename="InvoiceHistory" fieldname="projectId" statuswindowarg= "label.general.blank" onclick="c_lookup('Project');"  modulename="InvoiceHistory" >
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br>
<%
ItemLines itemLines = invoiceForm.getItemLines();
request.setAttribute("ItemLines", itemLines);
%>
<jsp:include page="/jsp/pages/accounting/items_list.jsp" />

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="saveChanges('save');">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="saveChanges('saveclose');">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="saveChanges('savenew');">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </html:button>
     <td>
  </tr>
</table>
</html:form>
<script>
<%
java.util.Date currDate = new java.util.Date(System.currentTimeMillis());
%>
document.invoiceform.month.value = <%=(currDate.getMonth() + 1)%>
document.invoiceform.date.value = <%=currDate.getDate()%>
document.invoiceform.year.value = <%=(currDate.getYear()+1900)%>

</script>