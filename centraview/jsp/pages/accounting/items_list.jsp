<%--
 * $RCSfile: items_list.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:55 $ - $Author: mcallist $
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
<%@ page import="com.centraview.account.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.contact.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.account.invoice.InvoiceForm" %>
<%@ page import="com.centraview.account.common.AccountingLines" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%
  String showInPopupx = (String)request.getParameter("showInPopup");
  boolean showInPopup = (showInPopupx != null && showInPopupx.equals("true")) ? true : false;

  int recordID = 0;
  String buttonOperation = "";
  String moduleName = null;
  String temp = request.getParameter("moduleName");
  if (temp == null || temp.equals("")) {
    moduleName = (String)request.getAttribute("moduleName");
   	if (moduleName == null)
   		moduleName = "";
	} else {
  	moduleName = temp;
  }
  String ID = (String)request.getAttribute("recordID");
  if (ID != null && !ID.equals("")) {
    recordID = Integer.parseInt(ID);
  }
  int buttonRight = 0;
  if (request.getParameter("buttonOperation") != null) {
    buttonOperation = request.getParameter("buttonOperation");
    if (buttonOperation != null && buttonOperation.equals("EDIT")) {
      buttonRight = ModuleFieldRightMatrix.UPDATE_RIGHT;
    }
  }

  ItemLines al = (ItemLines)request.getAttribute("ItemLines");
  float taxValue = 0;

  if (al == null) {
    al = new ItemLines();
  }
  al.calculate();
  Vector listcolumns = al.getViewVector();
  Enumeration listcolumns_enumeration = listcolumns.elements();
  Set listkey = al.keySet();
  Iterator it = listkey.iterator();
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">&nbsp;</td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="listHeader">
      <a class="listHeader" oncick="vl_selectAllRows(null, true);"><html:img page="/images/icon_check.gif" width="8" height="8" border="0" alt="" />&nbsp;<bean:message key="label.pages.accounting.all"/></a>
    </td>
    <%
      while (listcolumns_enumeration.hasMoreElements()) {
        String listcolumnName = (String)listcolumns_enumeration.nextElement();
        if (listcolumnName.equals("Tax Amount") && (moduleName.equals("Expense") || moduleName.equals("PO")))
        	continue;
        %><td class="listHeader"><span class="listHeader"><%=listcolumnName%></span></td><%
      }
    %>
  </tr>
  <% if (! it.hasNext()) { %>
  <tr>
    <td class="tableRowOdd" colspan="<%=listcolumns.size() + 1%>" align="center"><span class="boldText"><bean:message key="label.pages.accounting.noitemsfound"/>.</span>&nbsp;<a class="plainLink" onclick="fnItemLookup();"><bean:message key="label.pages.accounting.additem"/></a></td>
  </tr>
  <% } %>
  <%
    int i = 1;
    int itemNum = 0;
    while (it.hasNext()) {
      %><tr><%
      Enumeration enum = listcolumns.elements();
      ItemElement ele = (ItemElement)al.get(it.next());
      String lineStatus = ele.getLineStatus();
      if (lineStatus == null) { lineStatus = ""; }

      IntMember lineid = (IntMember)ele.get("LineId");
      IntMember itemid = (IntMember)ele.get("ItemId");
      IntMember qty = (IntMember)ele.get("Quantity");
      FloatMember priceEach = (FloatMember)ele.get("Price");
      StringMember sku = (StringMember)ele.get("SKU");
      StringMember desc = (StringMember)ele.get("Description");
      FloatMember priceExe = (FloatMember)ele.get("PriceExtended");
	    FloatMember TaxAmount = null;
			if (!moduleName.equals("Expense") && !moduleName.equals("PO"))
	     	TaxAmount = (FloatMember)ele.get("TaxAmount");
      String css = "tableRowOdd";

      if (! lineStatus.equals("Deleted")) {
        if (i % 2 == 0) {
          css = "tableRowEven";
        }else{
          css = "tableRowOdd";
        }

        %>
        <td nowrap class="<%=css%>"><input type="checkbox" name="checkbox" id="checkbox" value="<%=itemid.getDisplayString()%>"></td>
        <td nowrap class="<%=css%>"><%=sku.getDisplayString()%></td>
        <td class="<%=css%>"><html:text property="description" styleId="description" styleClass="inputBox" value="<%=desc.getDisplayString()%>" size="50"/></td>
        <td nowrap class="<%=css%>"><div align="right"><input type="text" name="quantity" id="quantity" class="inputBox" value="<%=qty.getDisplayString()%>" size="6"/></div></td>
        <td nowrap class="<%=css%>"><div align="right">$<input type="text" name="priceeach" id="priceeach" class="inputBox" value="<%=priceEach.getDisplayString()%>" size="6" /></div></td>
				<c:if test = "${moduleName != 'Expense' && moduleName != 'PO'}">
        <td class="<%=css%>" nowrap><div align="right" id="calculatedprice<%=itemNum%>"><%=TaxAmount.getDisplayString()%></div></td>
				</c:if>
        <td class="<%=css%>" nowrap><div align="right" id="calculatedprice<%=itemNum%>"><%=priceExe.getDisplayString()%></div></td>
        <%
        itemNum++;// = itemNum + 1;
        i++;
      }else{
        %>
        <input type="hidden" name="description" id="description" value="<%=desc.getDisplayString()%>">
        <input type="hidden" name="quantity" id="quantity" value="<%=qty.getDisplayString()%>">
        <input type="hidden" name="priceeach" id="priceeach" value="<%=priceEach.getDisplayString()%>">
        <%
      }
      %>
      <input type="hidden" name="sku" id="sku" value="<%=sku.getDisplayString()%>">
      <input type="hidden" name="lineid" id="lineid" value="<%=lineid.getMemberValue()%>">
      <input type="hidden" name="itemid" id="itemid" value="<%=itemid.getMemberValue()%>">
      <input type="hidden" name="priceExtended" id="priceExtended" value="<%=priceExe.getMemberValue()%>">
			<c:if test = "${moduleName != 'Expense' && moduleName != 'PO'}">
      <input type="hidden" name="taxAmount" id="taxAmount" value="<%=TaxAmount.getMemberValue()%>">
			</c:if>
      <input type="hidden" name="linestatus" id="linestatus" value="<%=ele.getLineStatus()%>">
      </tr>
      <%
    } //end of while
  %>
</table>
<%
  // used to format currency
  NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

  // used to format "natural" numbers
  NumberFormat numFormat = NumberFormat.getInstance();
  numFormat.setParseIntegerOnly(true);

  String totalItems = "0";
  totalItems = numFormat.format(al.getTotalItems());


  String subTotal = "$0.00";
  subTotal = currencyFormat.format(al.getSubtotal());


  String taxTotal = "$0.00";
  if (!moduleName.equals("Expense") && !moduleName.equals("PO"))
	  taxTotal = currencyFormat.format(al.getTax());

  String orderTotal = "$0.00";
  orderTotal = currencyFormat.format(al.getTotal());
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="itemsContainer" valign="top">
      <% if (! showInPopup) { %>
      <app:cvbutton property="additem" styleId="additem" styleClass="normalButton" onclick="fnItemLookup()" recordID="<%=recordID%>" modulename="<%=moduleName%>" buttonoperationtype="<%=buttonRight%>">
        <bean:message key="label.pages.accounting.additem"/>
      </app:cvbutton>
      <app:cvbutton property="removeitem" styleId="removeitem" styleClass="normalButton" onclick="fnCheckBox()" recordID="<%=recordID%>" modulename="<%=moduleName%>" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.pages.accounting.removeitem"/>
      </app:cvbutton>
      <app:cvbutton property="save" styleId="save" styleClass="normalButton" onclick="saveChanges('save');" recordID="<%=recordID%>" modulename="<%=moduleName%>" buttonoperationtype="<%=buttonRight%>">
        <bean:message key="label.pages.accounting.savechanges"/>
      </app:cvbutton>
      <br/>
      <% } %>
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.pages.accounting.notes"/>:</td>
        </tr>
        <tr>
          <td class="contentCell"><html:textarea property="notes" styleId="notes" styleClass="inputBox" cols="50" rows="3"/></td>
        </tr>
      </table>
    </td>
    <td class="itemsContainer" align="right">
      <table border="0" cellpadding="4" cellspacing="0">
        <tr>
          <td><span class="boldText"><bean:message key="label.pages.accounting.totalitems"/>:</span></td>
          <td align="right"><span class="boldText"><%=totalItems%></span></td>
        </tr>
        <c:choose>
        	<c:when test = "${moduleName == 'Expense' || moduleName == 'PO'}">
        <tr>
          <td colspan="2">&nbsp;</td>
        </tr>
				<tr>
          <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td><span class="boldText"><bean:message key="label.pages.accounting.ordertotal"/>:</span></td>
          <td align="right"><span class="boldText"><%=orderTotal%></span></td>
        </tr>
        	</c:when>
        	<c:otherwise>
				<tr>
          <td><span class="boldText"><bean:message key="label.pages.accounting.subtotal"/>:</span></td>
          <td align="right"><span class="boldText"><%=subTotal%></span></td>
        </tr>
        <tr>
          <td><span class="boldText"><bean:message key="label.pages.accounting.tax"/>:</span></td>
          <td align="right"><span class="boldText"><%=taxTotal%></span></td>
        </tr>
        <tr>
          <td class="itemTotalHR"><span class="boldText"><bean:message key="label.pages.accounting.ordertotal"/>:</span></td>
          <td class="itemTotalHR" align="right"><span class="boldText"><%=orderTotal%></span></td>
        </tr>
        	</c:otherwise>
        </c:choose>
      </table>
    </td>
  </tr>
</table>
<input type="hidden" name="theitemid" id="theitemid">
<input type="hidden" name="itemname" id="itemname">
<input type="hidden" name="itemdesc" id="itemdesc">