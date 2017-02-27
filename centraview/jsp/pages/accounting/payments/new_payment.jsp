<%
/**
 * $RCSfile: new_payment.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:52 $ - $Author: mcallist $
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
<%@ page import="java.util.*" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.account.common.*" %>
<%@ page import="com.centraview.account.payment.PaymentForm" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>

<%
PaymentForm paymentForm = (PaymentForm)request.getAttribute("paymentForm");
String typeofop = (String) request.getParameter(AccountConstantKeys.TYPEOFOPERATION);

if (request.getAttribute("clearform") != null && request.getAttribute("clearform").equals("true") )
{
  if (typeofop == null )
  {
    paymentForm = paymentForm.clearForm(paymentForm);
  }
}

String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
Vector pmtMethod = (Vector)gml.get("PaymentMethods");
pageContext.setAttribute("pmtMethod", pmtMethod, PageContext.PAGE_SCOPE);
%>

<script language="javascript">
<!--

  function getObj(name)
  {
    if (document.getElementById) {
      // Netscape 6.x, IE 5.x, Opera 5.x
      return document.getElementById(name).style;
    }else if (document.all){
      // Explorer 4.x
      return document.all[name].style;
    }else if (document.layers){
      // Netscape 4.x
      return document.layers[name];
    }
  }

  function validate(appliedPayment, amountDue, i)
  {
    if(eval(appliedPayment) > eval(amountDue) )
    {
      alert("<bean:message key='label.alert.appliedpaymentnomorethanamountdue'/>");
      window.document.paymentForm.payment[i].select();
    }
  }


  function showLayer()
  {
    var selectedID = window.document.paymentForm.paymentMethodID.selectedIndex;
    var layerName = window.document.paymentForm.paymentMethodID.options[selectedID].text;
    if (layerName == 'Check')
    {
      window.checkLayer.style.display = "block";
      window.creditCardLayer.style.display = "none";
      window.cashLayer.style.display = "none";
    }
    else if(layerName == 'Credit Card')
    {
      window.checkLayer.style.display = "none";
      window.creditCardLayer.style.display = "block";
      window.cashLayer.style.display = "none";
    }
    else if(layerName == 'Cash')
    {
      window.checkLayer.style.display = "none";
      window.creditCardLayer.style.display = "none";
      window.cashLayer.style.display = "block";
    }
  }


  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;
    document.paymentForm.entityName.value = name;
    document.paymentForm.entityId.value = id;
    var EntityID = document.paymentForm.entityId.value;
    var EntityName = document.paymentForm.entityName.value;
    submitForm(EntityName, EntityID);
  }

  function submitForm(EntityName, EntityID)
  {
    var flag = "false";
    if(EntityName != null & EntityID != null)
      flag = "true";
    document.paymentForm.action = "<html:rewrite page="/accounting/new_payment.do" />";
    document.paymentForm.submit();
  }


  function gotocancel()
  {
    window.document.paymentForm.action="<html:rewrite page="/accounting/payment_list.do" />";
    window.document.paymentForm.target="_parent";
    window.document.paymentForm.submit();
    return true;
  }
  function gotosave(param)
  {
    window.document.paymentForm.action="<html:rewrite page="/accounting/save_new_payment.do" />?buttonpress="+param;
    window.document.paymentForm.target="_parent";
    window.document.paymentForm.submit();
    return false;
  }
-->
</script>

<html:form action="/accounting/new_payment.do">
<table width="100%" height="100%" border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td colspan="2" class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="gotosave('save');">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="gotosave('saveclose');">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="gotosave('savenew');">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" class="sectionHeader"><bean:message key="label.pages.accounting.payments.paymentreceiptsummary"/></td>
  </tr>
  <tr>
    <td valign="top" align="left" width="50%">
      <table border="0" cellspacing="0" cellpadding="3" width="100%">
        <tr>
          <td class="labelCell"><bean:message key="label.pages.accounting.payments.entity"/>: </td>
          <td class="contentCell">
            <html:text property="entityName" readonly="true" styleClass="inputBox" size="40"/>
            <html:hidden property="entityId"/>
            <html:button property="lookup" styleClass="normalButton" onclick="c_lookup('Entity')">
              <bean:message key="label.lookup"/>
            </html:button>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.pages.accounting.payments.paymentamount"/>: </td>
          <td class="contentCell">
            <html:text property="paymentAmount" styleClass="inputBox" size="20"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.pages.accounting.payments.reference"/>: </td>
          <td class="contentCell">
            <app:cvtext property="reference" styleClass="inputBox" size="20" modulename="Payment" fieldname="reference" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.pages.accounting.payments.description"/>: </td>
          <td class="contentCell">
            <app:cvtextarea property="description" cols="50" rows="6" styleClass="inputBox" modulename="Payment" fieldname="description"/>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top" align="left" width="50%">
      <table border="0" cellspacing="0" cellpadding="3" width="100%">
        <tr>
          <td class="labelCell"><bean:message key="label.pages.accounting.payments.paymentmethod"/>: </td>
          <td class="contentCell" align="left">
            <html:select property="paymentMethodID" styleClass="inputBox" onchange="showLayer();">
            <html:options collection="pmtMethod" property="id" labelProperty="name"/>
          </html:select>
        </tr>
        <tr>
          <td colspan="2">
            <div id="checkLayer" name="checkLayer" style="display:none;">
              <table width="100%" cellpadding="3" cellspacing="0">
                <tr>
                  <td class="labelCell"><bean:message key="label.pages.accounting.payments.checknumber"/>: </td>
                  <td class="contentCell">
                    <html:text property="checkNumber"  styleClass="inputBox" size="35"/>
                  </td>
                </tr>
              </table>
            </div>
            <div id="creditCardLayer" name="creditCardLayer" style="display:block;">
              <table width="100%" cellpadding="3" cellspacing="0">
                <tr>
                  <td class="labelCell"><bean:message key="label.pages.accounting.payments.cardtype"/>: </td>
                  <td class="contentCell">
                    <html:text property="cardType"  styleClass="inputBox" size="35"/>
                  </td>
                </tr>
                <tr>
                  <td class="labelCell"><bean:message key="label.pages.accounting.payments.cardnumber"/>: </td>
                  <td class="contentCell">
                    <html:text property="cardNumber"  styleClass="inputBox" size="35"/>
                  </td>
                </tr>
                <tr>
                  <td class="labelCell"><bean:message key="label.pages.accounting.payments.expirationdate"/>: </td>
                  <td class="contentCell">
                    <html:text property="cardDateStr"  styleClass="inputBox" size="35"/>
                  </td>
                </tr>
              </table>
            </div>
            <div id="cashLayer" name="cashLayer" style="display:none;">
              <table width="100%" cellpadding="0" cellspacing="0">
                <tr>
                  <td colspan="2" class="labelCell"><bean:message key="label.pages.accounting.payments.enteramountbelow"/></td>
                </tr>
              </table>
            </div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" align="left" valign="top">
      <%
        PaymentLines paymentLines = paymentForm.getPaymentLines();
        request.setAttribute("PaymentLines", paymentLines);
        request.setAttribute("appliedPayment","1");
      %>
      <jsp:include page="payment_list.jsp"/>
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="gotosave('save');">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="gotosave('saveclose');">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="gotosave('savenew');">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="gotocancel();">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
</table>
</html:form>