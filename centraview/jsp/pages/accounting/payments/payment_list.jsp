<%
/**
 * $RCSfile: payment_list.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:52 $ - $Author: mcallist $
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
<%@ page import = "java.util.*" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import = "com.centraview.contact.helper.*" %>
<%@ page import = "com.centraview.account.common.*" %>
<%@ page import = "com.centraview.account.payment.*" %>
<%
String appliedPayment = (String) request.getAttribute("appliedPayment");
if (appliedPayment == null){
  appliedPayment="1";
}

PaymentLines al = (PaymentLines)request.getAttribute("PaymentLines");
if(al == null)
{
  al = new PaymentLines();
}

Vector listcolumns = al.getViewVector();
Enumeration listcolumns_enumeration = listcolumns.elements();
Set listkey = al.keySet();
Iterator it =  listkey.iterator();
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="pagingBarContainer" colspan="5">
    <%
    if (appliedPayment != null && appliedPayment.equals("1")){
    %>
     <span class="labelBold"><bean:message key="label.accounting.relatedunpaidinvoices"/></span>
    <%
    }
    else{
    %>
      <span class="labelBold"><bean:message key="label.accounting.invoicespaymentapplied"/></span>
    <%
    }
    %>
    </td>
  </tr>
  <tr>
    <%
    if (appliedPayment != null && appliedPayment.equals("1")){
      while(listcolumns_enumeration.hasMoreElements())
      {
        String listcolumnName = (String)listcolumns_enumeration.nextElement();
        %>
          <td nowrap class="listHeader">
            <span class="listHeader">
              <%=listcolumnName%>
            </span>
          </td>
        <%
      }
    }
    else{
      while(listcolumns_enumeration.hasMoreElements())
      {
        String listcolumnName = (String)listcolumns_enumeration.nextElement();
        if (listcolumnName.equals("Payment")){
        %>
          <td nowrap class="listHeader">
            <span class="listHeader">
              <bean:message key="label.accounting.amountapplied"/>
            </span>
          </td>
         <%
        }
        else{
        %>
          <td nowrap class="listHeader">
            <span class="listHeader">
              <%=listcolumnName%>
            </span>
          </td>

        <%
        }
      }
    }
    %>
  </tr>
  <%
  // get data and display in table
  int i=1;

  while (it.hasNext())
  {
    Enumeration enum =  listcolumns.elements();
    ItemElement ele  = (ItemElement)al.get(it.next());
    String lineStatus = ele.getLineStatus();

    if(lineStatus == null)
      lineStatus = "";

    IntMember invId  = (IntMember)ele.get("InvoiceId");

    String invNumStr = "";
    if(ele.get("InvoiceNum") != null)
    {
      StringMember invNum = (StringMember)ele.get("InvoiceNum");
      invNumStr = invNum.getDisplayString();
    }
    DateMember invDate  = (DateMember)ele.get("Date");
    DoubleMember invTotal = (DoubleMember)ele.get("Total");
    DoubleMember amtDue = (DoubleMember)ele.get("AmountDue");
    DoubleMember amtAppl = (DoubleMember)ele.get("AmountApplied");

    String cellclassstr;
    String tableclasstr;

    if(i%2 == 0)
    {
      tableclasstr="tableRowEven";
    }
    else
    {
      tableclasstr="tableRowOdd";
    }
    String invDateStr = "";
    if (invDate  != null){
      invDateStr = invDate.getDisplayString();
    }
    if (appliedPayment != null && appliedPayment.equals("1")){
    %>
      <tr>
        <td class="<%=tableclasstr%>">
          <%=invNumStr%>
          <input type="hidden" name="invoiceID" value="<%=invId.getDisplayString()%>">
          <html:hidden property="invoiceNum" value="<%=invNumStr%>"/>
          <html:hidden property="linestatus" />
        </td>
        <td class="<%=tableclasstr%>">
          <%=invDateStr%>
          <input type="hidden" name="invDateStr" value="<%=invDateStr%>">
        </td>
        <td class="<%=tableclasstr%>">
          <div align="right"><%=invTotal.getDisplayString()%></div>
          <html:hidden property="total"/>
        </td>
        <td class="<%=tableclasstr%>">
          <div align="right"><%=amtDue.getDisplayString()%></div>
          <html:hidden property="amtDue"/>
        </td>
        <td class="<%=tableclasstr%>">
          <div align="right">
            <html:text property="payment" styleClass="inputBox" size="6" value="0.00"/>
          </div>
        </td>
      </tr>
    <%
    }
    else{
    %>
      <tr>
        <td class="<%=tableclasstr%>"><%=invNumStr%></td>
        <td class="<%=tableclasstr%>"><%=invDateStr%></td>
        <td class="<%=tableclasstr%>"><div align="right"><%=invTotal.getDisplayString()%></div></td>
        <td class="<%=tableclasstr%>"><div align="right"><%=amtDue.getDisplayString()%></div></td>
        <td class="<%=tableclasstr%>"><div align="right"><%=amtAppl.getDisplayString()%></div></td>
      </tr>
    <%
    }
    i++;
  } //end of while
  %>
</table>
