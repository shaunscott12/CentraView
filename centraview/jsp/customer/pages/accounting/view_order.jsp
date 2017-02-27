<%--
 * $RCSfile: view_order.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:47 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 1.0.0 (the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html:form action="/customer/view_order">
<table width="100%" cellspacing="0" cellpadding="0" border="0">
  <tr>
    <td>
      <span class="boldText"><bean:message key="label.customer.pages.accounting.companyname"/></span><br/>
      <span class="plainText">
      495 Main Street<br/>
      Suite 110<br/>
      Anytown, PA 19000<br/>
      USA
      </span>
    </td>
    <td align="right" valign="top"><span class="accountingPageHeader"><bean:message key="label.customer.pages.accounting.order"/></span></td>
  </tr>
</table>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td valign="top">
      <table border="0" cellpadding="3" cellspacing="1" width="250" class="mediumTableBorders">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.accounting.billto"/></td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <span class="plainText"><bean:write name="customerOrderForm" property="billingAddress" /></span>
          </td>
        </tr>
      </table>
    </td>
    <td align="right" valign="top">
      <table width="250" border="0" cellpadding="3" cellspacing="1" class="mediumTableBorders">
        <tr>
          <td class="sectionHeader"><bean:message key="label.customer.pages.accounting.shipto"/></td>
        </tr>
        <tr>
          <td class="mediumTableBorders">
            <span class="plainText">
              <bean:write name="customerOrderForm" property="shippingAddress" />
            </span>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="right">
      <table border="0" cellpadding="3" cellspacing="1" class="mediumTableBorders" width="300">
        <tr>
          <td class="sectionHeader"><div align="center"><bean:message key="label.customer.pages.accounting.status"/></div></td>
          <td class="sectionHeader"><div align="center"><bean:message key="label.customer.pages.accounting.date"/></div></td>
          <td class="sectionHeader"><div align="center"><bean:message key="label.customer.pages.accounting.ordernumber"/></div></td>
        </tr>
        <tr>
          <td class="mediumTableBorders"><div class="plainText" align="center"><bean:write name="customerOrderForm" property="status" /></div></td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><bean:write name="customerOrderForm" property="date" /></div></td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><bean:write name="customerOrderForm" property="orderID" /></div></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</br>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td align="right">
      <table width="300" border="0" cellpadding="3" cellspacing="1" class="mediumTableBorders">
        <tr>
          <td class="sectionHeader"><div align="center"><bean:message key="label.customer.pages.accounting.terms"/></div></td>
          <td class="sectionHeader"><div align="center"><bean:message key="label.customer.pages.accounting.accountmanager"/>r</div></td>
        </tr>
        <tr>
          <td class="mediumTableBorders"><div class="plainText" align="center"><bean:write name="customerOrderForm" property="terms" /></div></td>
          <td class="mediumTableBorders"><div class="plainText" align="center"><bean:write name="customerOrderForm" property="accountManager" /></div></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="listHeader" nowrap><span class="listHeader"><bean:message key="label.customer.pages.accounting.item"/></span></td>
    <td class="listHeader" nowrap><span class="listHeader"><bean:message key="label.customer.pages.accounting.description"/></span></td>
    <td class="listHeader" align="right" nowrap><span class="listHeader"><bean:message key="label.customer.pages.accounting.quantity"/></span></td>
    <td class="listHeader" align="right" nowrap><span class="listHeader"><bean:message key="label.customer.pages.accounting.priceeach"/></span></td>
    <td class="listHeader" align="right" nowrap><span class="listHeader"><bean:message key="label.customer.pages.accounting.priceextended"/></span></td>
  </tr>
  <c:set var="rowCount" value="0" />
  <c:forEach var="item" items="${customerOrderForm.map.itemList}">
    <c:choose>
      <c:when test="${rowCount % 2 == 0}">
        <tr>
          <td class="tableRowEven"><bean:write name="item" property="itemSku" ignore="true" /></td>
          <td class="tableRowEven"><bean:write name="item" property="description" ignore="true" /></td>
          <td class="tableRowEven" align="right" nowrap><bean:write name="item" property="quantity" ignore="true" /></td>
          <td class="tableRowEven" align="right" nowrap><bean:message key="currency.symbol" /><bean:write name="item" property="priceEach" ignore="true" /></td>
          <td align="right" nowrap class="tableRowEven"><bean:message key="currency.symbol" /><bean:write name="item" property="price" ignore="true" /></td>
        </tr>
      </c:when>
      <c:when test="${rowCount % 2 != 0}">
        <tr>
          <td class="tableRowOdd"><bean:write name="item" property="itemSku" ignore="true" /></td>
          <td class="tableRowOdd"><bean:write name="item" property="description" ignore="true" /></td>
          <td class="tableRowOdd" align="right" nowrap><bean:write name="item" property="quantity" ignore="true" /></td>
          <td class="tableRowOdd" align="right" nowrap><bean:message key="currency.symbol" /><bean:write name="item" property="priceEach" ignore="true" /></td>
          <td align="right" nowrap class="tableRowOdd"><bean:message key="currency.symbol" /><bean:write name="item" property="price" ignore="true" /></td>
        </tr>
      </c:when>
    </c:choose>
    <c:set var="rowCount" value="${rowCount + 1}" />
  </c:forEach>
  <tr>
    <td class="itemsContainer">&nbsp;</td>
    <td align="right" class="itemsContainer"><strong><bean:message key="label.customer.pages.accounting.totalitems"/>:</strong></td>
    <td nowrap align="right" class="itemsContainer"><bean:write name="customerOrderForm" property="totalItems" /></td>
    <td align="right" class="itemsContainer" nowrap><strong><bean:message key="label.customer.pages.accounting.subtotal"/>:</strong></td>
    <td align="right" class="itemsContainer" nowrap><bean:message key="currency.symbol" /><bean:write name="customerOrderForm" property="subTotal" format="#,##0.00" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td align="right" nowrap class="itemsContainer"><bean:message key="label.customer.pages.accounting.tax"/>:</td>
    <td align="right" nowrap class="itemsContainer"><bean:message key="currency.symbol" /><bean:write name="customerOrderForm" property="tax" format="#,##0.00" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td align="right" nowrap class="itemsContainer"><bean:message key="label.customer.pages.accounting.shipping"/>:</td>
    <td align="right" nowrap class="itemsContainer"><bean:message key="currency.symbol" /><bean:write name="customerOrderForm" property="shipping" format="#,##0.00" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td align="right" nowrap class="itemsContainer"><strong><bean:message key="label.customer.pages.accounting.ordertotal"/>:</strong></td>
    <td align="right" nowrap class="itemsContainer"><bean:message key="currency.symbol" /><bean:write name="customerOrderForm" property="orderTotal" format="#,##0.00" /></td>
  </tr>
</table>
</html:form>