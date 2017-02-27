<%--
 * $RCSfile: view_order.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:45 $ - $Author: mcallist $
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
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.account.order.OrderForm" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  UserObject userobject = (UserObject)session.getAttribute("userobject");
  AddressVO addVO = userobject.getAddressVO();
  if (addVO == null) { addVO = new AddressVO(); }
  pageContext.setAttribute("addressVO", addVO);

  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  Vector statusVector = (Vector)gml.get("AccountingStatus");
  Vector termsVector = (Vector)gml.get("AccountingTerms");
  if (statusVector != null && termsVector != null) {
    pageContext.setAttribute("statusVector", statusVector, PageContext.PAGE_SCOPE);
    pageContext.setAttribute("termsVector", termsVector, PageContext.PAGE_SCOPE);
  }

  OrderForm oform = (OrderForm) request.getAttribute("orderform");
  java.sql.Date d = oform.getOrderDate();
  SimpleDateFormat sdf = new SimpleDateFormat("MMM d , yyyy");
  String odate = sdf.format(d);

  String orderID = oform.getOrderId();
  int recordID = 0;
  if (orderID != null && ! orderID.equals("")) {
    recordID = Integer.parseInt(orderID);
  }

  String showInPopupx = (String)request.getParameter("showInPopup");
  boolean showInPopup = (showInPopupx != null && showInPopupx.equals("true")) ? true : false;
%>
<script language="javascript">
  function fnItemLookup()
  {
    var hidden="";
    var str="";
    var k=0;
    for (i = 0; i < document.orderform.elements.length; i++) {
      if (document.orderform.elements[i].type == "checkbox") {
        k++;
      }
    }

    if (k > 1) {
      for (i = 0; i < document.orderform.checkbox.length; i++) {
        hidden = document.orderform.checkbox[i].value + ",";
        str = str + hidden;
      }
      str = (str.substring(0, str.length - 1));
    } else if (k == 1) {
      hidden = document.orderform.checkbox.value + ",";
      str = str + hidden;
      str = (str.substring(0, str.length - 1));
    }
    c_lookup('Item',str);
  }

  /**
   * Sets the Entity ID and name fields from the entity lookup.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
   * is called from the lookup screen, and must be general
   */
  function setEntity(lookupValues)
  {
    var entityIdField = document.getElementById('customerId');
    var entityNameField = document.getElementById('customerName');
    entityIdField.value = lookupValues.entID;
    entityNameField.value = lookupValues.entName;
  }
  /**
   * Sets the Account Manager ID and name fields from the employee lookup.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
   * is called from the lookup screen, and must be general
   */
  function setEmployee(lookupValues)
  {
    var managerIdField = document.getElementById('acctMgrId');
    var managerNameField = document.getElementById('acctMgr');
    managerIdField.value = lookupValues.individualID;
    managerNameField.value = lookupValues.firstName + ' ' + lookupValues.lastName;
  }

  function setProject(lookupValues)
  {
    document.getElementById('project').value = lookupValues.Name;
    document.getElementById('projectId').value = lookupValues.idValue;
  }

  var toWhere = "billto";  // 'billto' is just taken as default value
  function callAddressLookup(toBox)
  {
    var customerId = document.getElementById('customerId').value;
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
      document.getElementById('billToAdd').readonly = false;
      document.getElementById('billToAdd').value = name;
      document.getElementById('billToAddId').value = id;
      document.getElementById('billToAdd').readonly = true;
    }else{
      document.getElementById('shipToAdd').readonly = false;
      document.getElementById('shipToAdd').value = name;
      document.getElementById('shipToAddId').value = id;
      document.getElementById('shipToAdd').readonly = true;
    }
  }


  function saveChanges(act)
  {
    if (act == 'cancel') {
      window.document.orderform.action = '<html:rewrite page="/accounting/order_list.do"/>';
      window.document.orderform.submit();
      return true;
    } else if (act == 'deleteOrder') {
      window.document.orderform.action = '<html:rewrite page="/accounting/delete_order.do"/>?deleteOrderId=' + document.getElementById('orderId').value;
      window.document.orderform.submit();
      return true;
    } else if (act == 'save') {
      window.document.orderform.btnClicked.value = "save";
    } else if (act == 'saveClose') {
      window.document.orderform.btnClicked.value = "saveClose";
    } else if (act == 'saveNew') {
      window.document.orderform.btnClicked.value = "saveNew";
    }
    window.document.orderform.action = '<html:rewrite page="/accounting/save_edit_order.do"/>';
    window.document.orderform.submit();
    return true;
  }

  function setItem(itemLookupValues)
  {
    newItemID = itemLookupValues.IDs;
    removeIDs = itemLookupValues.RemoveIDs;
    flag = itemLookupValues.Flag;
    document.getElementById('theitemid').value = newItemID
    document.getElementById('itemname').value = '';
    document.getElementById('itemdesc').value = '';
    document.getElementById('removeID').value = removeIDs;
    if (flag) {
      document.orderform.action = '<html:rewrite page="/accounting/view_order.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>';
    } else {
      document.orderform.action = '<html:rewrite page="/accounting/view_order.do"/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>';
    }
    document.orderform.submit();
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
    if (document.orderform.checkbox == null) {
      alert("<bean:message key='label.alert.noitemestoberemoved'/>.");
      return;
    }
    for (i = 0; i < document.orderform.elements.length; i++) {
      if (document.orderform.elements[i].type == "checkbox") {
        k++;
      }
    }

    if (k > 1) {
      for (i = 0; i < document.orderform.checkbox.length; i++) {
        if (document.orderform.checkbox[i].checked) {
          hidden = document.orderform.checkbox[i].value + ",";
          str = str + hidden;
        }
      }
      str = (str.substring(0, str.length - 1));
    } else if (k == 1) {
      hidden = document.orderform.checkbox.value + ",";
      str = str + hidden;
      str = (str.substring(0, str.length - 1));
    }
    flag = false;
    lookupValues = {IDs: "", RemoveIDs: str, Flag: false}
    setItem(lookupValues);
  }

  function checkCheckbox()
  {
    if (document.orderform.checkbox.length) {
      for (i = 0; i < document.orderform.checkbox.length; i++) {
        if (document.orderform.checkbox[i].checked) {
          return true;
        }
      }
    } else {
      return document.orderform.checkbox.checked;
    }
  }

  function callViewInvoice()
  {
    c_goTo('/accounting/invoice_list.do?OrderID=' + document.getElementById('orderId').value + '&EntityID=' + document.getElementById('customerId').value);
  }

  function callNewInvoice()
  {
    window.document.orderform.action = "<html:rewrite page="/accounting/auto_generate_invoice.do"/>?OrderID=" + document.getElementById('orderId').value;
    window.document.orderform.submit();
  }


</script>
<html:form action="/accounting/view_order.do" >
<html:hidden property="orderId" styleId="orderId" />
<html:hidden property="orderDay" styleId="orderDay" />
<html:hidden property="orderYear" styleId="orderYear" />
<html:hidden property="orderMonth" styleId="orderMonth" />
<input type=hidden name="btnClicked" id="btnClicked" >
<input type=hidden name="removeID" id="removeID">
<input type=hidden name="body" id="body" value="EDIT" >
<% if (! showInPopup) { %>
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
      <app:cvsubmit property="deleteorder" styleId="deleteorder" styleClass="normalButton" onclick="saveChanges('deleteOrder');" recordID="<%=recordID%>" modulename="OrderHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/> <bean:message key="label.accounting.order"/>
      </app:cvsubmit>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="saveChanges('cancel');">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
      <div align="right" style="display:inline;float:right;">
      <span class="boldText"><bean:message key="label.accounting.relatedinvoices"/>:</span>
      <app:cvbutton property="view" styleId="view" styleClass="normalButton" onclick="callViewInvoice();">
        <bean:message key="label.view"/>
      </app:cvbutton>
      <%
      if (oform.getInvoiceIsGenerated()) { %>
      <app:cvbutton property="new" styleId="new" styleClass="normalButton" onclick="callNewInvoice();" disabled="true" recordID="<%=recordID%>" modulename="OrderHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.new"/>
      </app:cvbutton>
      <%}else {%>
      <app:cvbutton property="new" styleId="new" styleClass="normalButton" onclick="callNewInvoice();" recordID="<%=recordID%>" modulename="OrderHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.new"/>
      </app:cvbutton>
      <% } %>
      </div>
    </td>
  </tr>
</table>
<br/>
<% }  // end if (! showInPopup) %>
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
    <td align="right" valign="top"><span class="accountingPageHeader"><bean:message key="label.accounting.orderdetails"/>:</span></td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="3" cellspacing="0">
  <tr>
    <td class="labelCellBold"><bean:message key="label.accounting.entity"/>:</td>
    <td class="contentCell">
      <html:hidden property="customerId" styleId="customerId" />
      <html:text property="customerName" styleId="customerName" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.orderform.customerId.value);"  size="40" readonly="true" />
      <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="c_lookup('Entity');">
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
            <html:hidden property="billToAddId" styleId="billToAddId" />
            <html:textarea property="billToAdd" styleId="billToAdd" styleClass="inputBox" rows="4" cols="50" readonly="true" />
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
            <html:hidden property="shipToAddId" styleId="shipToAddId" />
            <html:textarea property="shipToAdd" styleId="shipToAdd" styleClass="inputBox" rows="4" cols="50" readonly="true" />
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
          <td class="sectionHeader" align="center"><bean:message key="label.accounting.orderid"/>:</td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <app:cvselect property="statusId" styleId="statusId" styleClass="inputBox" modulename="OrderHistory" fieldname="statusIdValue">
              <app:cvoptions collection="statusVector" property="id" labelProperty="name" />
            </app:cvselect>
          </td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><%=odate%></div></td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><%=recordID%></div></td>
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
          <td class="sectionHeader"><bean:message key="label.accounting.po"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.terms"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.accountmanager"/></td>
          <td class="sectionHeader"><bean:message key="label.accounting.project"/></td>
        </tr>
        <tr>
          <td class="mediumTableBorders"><html:text property="po" styleId="po" styleClass="inputBox" size="5" /></td>
          <td class="mediumTableBorders">
            <app:cvselect property="termsId" styleId="termsId" styleClass="inputBox" modulename="OrderHistory" fieldname="termsIdValue">
              <app:cvoptions collection="termsVector" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
          <td class="mediumTableBorders">
            <html:hidden property="acctMgrId" styleId="acctMgrId" />
            <app:cvtext readonly="true" property="acctMgr" styleId="acctMgr" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.orderform.acctMgrId.value);" size="30" modulename="OrderHistory" fieldname="acctMgr" />
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" modulename="OrderHistory" fieldname="acctMgr" onclick="c_lookup('Employee');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
          <td class="mediumTableBorders">
            <html:hidden property="projectId" styleId="projectId" />
            <app:cvtext readonly="true" property="project" styleId="project" styleClass="inputBox" size="30" modulename="OrderHistory" fieldname="project" />
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" modulename="OrderHistory" fieldname="project" onclick="c_lookup('Project');">
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
<% if (! showInPopup) { %>
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
      <app:cvsubmit property="deleteorder" styleId="deleteorder" styleClass="normalButton" onclick="saveChanges('deleteOrder');" recordID="<%=recordID%>" modulename="OrderHistory" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/><bean:message key="label.accounting.order"/>
      </app:cvsubmit>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="saveChanges('cancel');">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<br/>
<% }  // end if (! showInPopup) %>
</html:form>