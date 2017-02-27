<%
/**
 * $RCSfile: view_invoice.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:54 $ - $Author: mcallist $
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
<%@ page import = "com.centraview.account.common.* ,java.util.*,com.centraview.common.*,com.centraview.contact.*"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import="com.centraview.account.invoice.InvoiceForm" %>
<%@ page import = "java.sql.Date"%>
<%@ page import = "java.text.*"%>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>


<%
InvoiceForm invoiceForm = null;
InvoiceForm newinvoiceform = null;
if (request.getAttribute("invoiceform") != null)
	invoiceForm = (InvoiceForm)request.getAttribute("invoiceform");
else
{
	invoiceForm = new InvoiceForm();
}
if (request.getAttribute("newinvoiceform") != null)
	invoiceForm = (InvoiceForm)request.getAttribute("newinvoiceform");

if(newinvoiceform!=null)
	invoiceForm=newinvoiceform;

String invoiceID = invoiceForm.getInvoiceid();
int recordID = 0;
if(invoiceID != null && !invoiceID.equals("")){
	recordID = Integer.parseInt(invoiceID);
}

Date invoiceDate =invoiceForm.getInvoiceDate();
SimpleDateFormat sdf =new SimpleDateFormat("MMM d, yyyy");
String datestr= sdf.format(invoiceDate);

response.setHeader("Pragma","no-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);

String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
Vector statusVec = (Vector)gml.get("AccountingStatus");
Vector termVec  = (Vector)gml.get("AccountingTerms");
pageContext.setAttribute("termVec", termVec, PageContext.PAGE_SCOPE);
pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);

AddressVO addVO = null;
UserObject userobject =(UserObject) session.getAttribute("userobject");
if (userobject.getAddressVO() != null)
  addVO = userobject.getAddressVO();
else
  addVO = new AddressVO();

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

  function callViewPayment()
  {
    window.document.invoiceform.action="<html:rewrite page="/accounting/payment_list.do"/>?invoiceid="+document.invoiceform.invoiceid.value;
    window.document.invoiceform.submit();
    return true;
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
    window.document.invoiceform.action="<html:rewrite page="/accounting/save_edit_invoice.do"/>?buttonpress="+param;
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
      document.invoiceform.action = "<html:rewrite page="/accounting/view_invoice.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>";
    }
    else
    {
      document.invoiceform.action = "<html:rewrite page="/accounting/view_invoice.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>";
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
-->
</script>

<html:form action="/accounting/view_invoice.do">
<input type=hidden name="body" value="EDIT" >
<input type=hidden name="accountmgrid" value="0">
<input type=hidden name="invoiceid" value="<%=recordID%>">
<input type=hidden name="invoiceDate" value="<%=invoiceForm.getInvoiceDate()%>">
<input type=hidden name="year" value="<%=invoiceForm.getYear()%>" >

<html:hidden property="customerId" />
<html:hidden property="orderid"/>
<html:hidden property="month"/>
<html:hidden property="date"/>
<input type=hidden name="removeID">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvsubmit property="save" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('save');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
      <bean:message key="label.save"/>
      </app:cvsubmit>
      <app:cvsubmit property="save&close" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('saveclose');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
      <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvsubmit property="save&new" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('savenew');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
      <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <app:cvsubmit property="delete" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('delete');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
      <bean:message key="label.delete"/>
      </app:cvsubmit>
      <app:cvbutton property="cancel" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="gotocancel();">
      <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
    <td class="buttonRow">
      <span class="boldText"><bean:message key="label.accounting.relatedpayments"/>:</span>
      <app:cvbutton property="view" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="callViewPayment();" >
        <bean:message key="label.view"/>
      </app:cvbutton>
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
    <td align="right" valign="top"><span class="accountingPageHeader"><bean:message key="label.accounting.invoicedetails"/>:</span></td>
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
          <td class="labelCell"><bean:message key="label.accounting.taxjuris"/>:</td>
          <td class="contentCell">
            <html:select property="jurisdictionID" styleId="jurisdictionID" styleClass="textBoxWhiteBlueBorder" size="1" onchange="return setJurisdiction()">
              <html:option value="0">-- <bean:message key="label.accounting.select"/> --</html:option>
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
          <td class="sectionHeader" align="center"><bean:message key="label.accounting.invoicenumber"/>:</td>
          <td class="sectionHeader" align="center"><bean:message key="label.accounting.ordernumber"/>:</td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <app:cvselect property="statusid" styleClass="inputBox" modulename="InvoiceHistory" fieldname="statusId">
              <app:cvoptions collection="statusVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><%=datestr%></div></td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><%=recordID%></div></td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><%=invoiceForm.getOrderid()%></div></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br>
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
            <app:cvtext readonly="true" property="accountmanagerName" styleId="accountmanagerName" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.invoiceform.accountmanagerid.value);" size="30" modulename="InvoiceHistory" fieldname="accountManagerId" />
            <app:cvbutton property="lookup" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" modulename="InvoiceHistory" fieldname="accountManagerId" statuswindowarg= "label.general.blank" onclick="c_lookup('Employee')"  recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
          <td class="mediumTableBorders">
            <app:cvtext property="projectName" readonly="true" styleClass="inputBox" size="30" modulename="InvoiceHistory" fieldname="projectId"/>
            <html:hidden property="projectid"/>
            <app:cvbutton property="lookup" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" modulename="InvoiceHistory" fieldname="projectId" statuswindowarg= "label.general.blank" onclick="c_lookup('Project');"  recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
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
request.setAttribute("recordID", recordID+"");
%>
<jsp:include page="/jsp/pages/accounting/items_list.jsp" />
<br>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvsubmit property="save" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('save');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.save"/>
      </app:cvsubmit>
      <app:cvsubmit property="save&close" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('saveclose');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvsubmit property="save&new" styleClass="normalButton" style="width:8em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('savenew');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <app:cvsubmit property="delete" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="saveChanges('delete');" recordID="<%=recordID%>" modulename="InvoiceHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/>
      </app:cvsubmit>
      <app:cvbutton property="cancel" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
    <td class="buttonRow">
      <span class="boldText"><bean:message key="label.accounting.relatedpayments"/>:</span>
      <app:cvbutton property="view" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="callViewPayment();" >
        <bean:message key="label.view"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>