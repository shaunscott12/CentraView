<%--
 * $RCSfile: new_proposal.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
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
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.common.FloatMember" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<%@ page import="com.centraview.common.IntMember" %>
<%@ page import="com.centraview.common.StringMember" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%@ page import="com.centraview.sale.proposal.ProposalListForm" %>
<%@ page import="com.centraview.sale.proposal.ItemLines" %>
<%@ page import="com.centraview.sale.proposal.ItemElement" %>
<%
  ProposalListForm proposallistform = (ProposalListForm)request.getAttribute("proposallistform");
  session.setAttribute("proposallistform" , proposallistform);
  session.setAttribute("AttachfileList", new HashMap());
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  String sStatus = (String)request.getAttribute("returnStatus");
  String sSaveandNew = (String)request.getAttribute("saveandnew");

  if ((sStatus != null) || (sSaveandNew != null)) {
    proposallistform.setProposal("");
    proposallistform.setProdescription("");
    proposallistform.setBillingaddress("");
    proposallistform.setBillingaddressid("");
    proposallistform.setShippingaddress("");
    proposallistform.setShippingaddressid("");
    proposallistform.setStatuslist("");
    proposallistform.setStage("");
    proposallistform.setProposaltype("");
    proposallistform.setTypeid("");
    proposallistform.setProbability("");
    proposallistform.setEcday("");
    proposallistform.setEcmon("");
    proposallistform.setEcyear("");
    proposallistform.setAcday("");
    proposallistform.setAcmon("");
    proposallistform.setAcyear("");
    proposallistform.setTerms("");
    proposallistform.setSpecialinstructions("");
    proposallistform.setAttachFileValues(new Vector());
    proposallistform = proposallistform.clearForm(proposallistform);
  }

  if (! (proposallistform.getEcyear()== null || proposallistform.getAcyear() == null)) {
    if (proposallistform.getEcyear().length() != 0 || proposallistform.getAcyear().length() != 0) {
      if (proposallistform.getEcyear().length() ==3) {
        int s = Integer.parseInt(proposallistform.getEcyear()) + 1900;
        String i = String.valueOf(s);
        proposallistform.setEcyear(""+i);
        if (proposallistform.getAcyear().length() != 0) {
          proposallistform.setAcyear(""+String.valueOf((Integer.parseInt(proposallistform.getAcyear()) + 1900)));
        }else{
          proposallistform.setAcyear("");
        }
        if (proposallistform.getEcmon().length() != 0) {
          proposallistform.setEcmon(""+String.valueOf((Integer.parseInt(proposallistform.getEcmon()) + 1)));
        }else{
          proposallistform.setEcmon("");
        }
        if (proposallistform.getAcmon().length() != 0) {
          proposallistform.setAcmon(""+String.valueOf((Integer.parseInt(proposallistform.getAcmon()) + 1)));
        }else{
          proposallistform.setAcmon("");
        }
      }
    }
  }

  boolean newProposal = (request.getAttribute("newProposal") == null) ? false : ((Boolean)request.getAttribute("newProposal")).booleanValue();
  boolean openAddItem = (request.getAttribute("openAddItem") == null) ? false: ((Boolean)request.getAttribute("openAddItem")).booleanValue();

%>
<script language="javascript">
  /**
   * Sets the Opportunity AND Entity ID AND name fields from the
   * opportunity lookup. DO NOT CHANGE THE NAME OR SIGNATURE OF
   * THIS FUNCTION, as it is called from the lookup screen.
   */
  function setOpp(lookupValues)
  {
    document.getElementById('opportunityid').value = lookupValues.ID;
    document.getElementById('opportunity').value = lookupValues.Name;
    document.getElementById('entityID').value = lookupValues.entityID;
    document.getElementById('entity').value = lookupValues.entityName;
  }
  /**
   * Sets the Individual ID and name fields from the individual lookup.
   * DO NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, as it
   * is called from the lookup screen, and must be general
   */
  function setIndividual(lookupValues)
  {
    document.getElementById('individualid').value = lookupValues.individualID;
    document.getElementById('individual').value = lookupValues.firstName + ' ' + lookupValues.lastName;
  }
  var toWhere;
  function addressLookup(toBox)
  {
    toWhere = toBox;
    entityID = document.getElementById('entityID').value;
    if (entityID == 0) {
      alert("<bean:message key='label.alert.selectentity'/>")
      return;
    }
    c_openWindow('/AddressLookup.do?contactID=' + entityID + '&contactType=1', '', 400, 400,'');
  }
  function setAddress(addressLookupValues)
  {
    name = addressLookupValues.Name;
    id = addressLookupValues.ID;
    jurisdictionID = addressLookupValues.jurisdictionID;

    if (toWhere == "billto") {
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
      document.getElementById('billingaddress').value = name;
      document.getElementById('billingaddressid').value = id;
      idAddress = id;
    }else if (toWhere == "shipto"){
      document.getElementById('shippingaddress').value = name;
      document.getElementById('shippingaddressid').value = id;
      idAddress = id;
    }
  }
  function openSelectDateTime()
  {
    var startDate = document.getElementById('ecmon').value + "/" + document.getElementById('ecday').value + "/" + document.getElementById('ecyear').value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = document.getElementById('acmon').value + "/" + document.getElementById('acday').value + "/" + document.getElementById('acyear').value;
    if (endDate == "//"){ endDate = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate+'&startTime=&endTime=&endDateTitle=Actual+Close+Date&startDateTitle=Estimated+Close+Date', 'selectDateTime', 350, 500, '');
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
      document.getElementById('ecmon').value = "";
      document.getElementById('ecday').value = "";
      document.getElementById('ecyear').value = "";
    }else{
      document.getElementById('ecmon').value = startDateArray[0];
      document.getElementById('ecday').value = startDateArray[1];
      document.getElementById('ecyear').value = startDateArray[2];
    }
    var endDateArray = endDate.split("/");
    if (endDateArray == null || endDateArray.length < 3) {
      document.getElementById('acmon').value = "";
      document.getElementById('acday').value = "";
      document.getElementById('acyear').value = "";
    }else{
      document.getElementById('acmon').value = endDateArray[0];
      document.getElementById('acday').value = endDateArray[1];
      document.getElementById('acyear').value = endDateArray[2];
    }
    return(true);
  }
  function fnItemLookup()
  {
    <% if (newProposal) { %>
    window.document.forms[0].submit();
    <% } else { %>
    var hidden = "";
    var str = "";
    var k = 0;
    for (i=0; i < document.forms[0].elements.length; i++) {
      if (document.forms[0].elements[i].type == "checkbox") {
        k++;
      }
    }
    if (k > 1) {
      for (i=0; i < document.forms[0].checkbox.length; i++) {
        hidden = document.forms[0].checkbox[i].value + ",";
        str = str + hidden;
      }
      str = (str.substring(0, str.length - 1));
    }else if (k == 1){
      hidden = document.forms[0].checkbox.value + ",";
      str = str + hidden;
      str = (str.substring(0, str.length - 1));
    }
    c_lookup('Item',str);
    <% } %>
  }

</script>
<html:form action="/sales/add_proposal">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="detailButtonHeader">
      <app:cvsubmit property="save" styleId="save" styleClass="normalButton" onclick="return selectAll();" buttonoperationtype="20">
        <bean:message key="label.save"/>
      </app:cvsubmit>
      <app:cvsubmit property="save_close" styleId="save_close" styleClass="normalButton" onclick="return selectAll();" buttonoperationtype="20">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <app:cvsubmit property="save_new" styleId="save_new" styleClass="normalButton" onclick="return selectAll();" buttonoperationtype="20">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.sales.proposaldetails"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="1" width="100%" class="mediumTableBorders">
  <tr>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.sales.opportunity"/>:</td>
          <td class="contentCell">
            <html:hidden property="opportunityid" styleId="opportunityid" />
            <html:text property="opportunity" styleId="opportunity" size="35" readonly="true" styleClass="inputBox"/>
            <input name="oppLookup" type="button" class="normalButton" value="<bean:message key='label.sales.lookup'/>" onclick="c_lookup('Opportunity', document.getElementById('entityID').value);" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.proposal"/>:</td>
          <td class="contentCell">
            <html:text property="proposal" styleId="proposal" styleClass="inputBox" size="45"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.description"/>:</td>
          <td class="contentCell">
            <app:cvtextarea property="prodescription" styleId="prodescription" rows="5" cols="44" styleClass="inputBox" fieldname="prodescription" modulename="Proposals"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.entity"/>:</td>
          <td class="contentCell">
            <html:text property="entity" styleId="entity" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.getElementById('entityID').value);" size="45" readonly="true"/>
            <html:hidden property="entityID" styleId="entityID"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.individual"/>:</td>
          <td class="contentCell">
            <html:hidden property="individualid" styleId="individualid"/>
            <app:cvtext property="individual" styleId="individual" styleClass="clickableInputBox" size="35" onclick="c_openPopup_FCI('Individual', document.getElementById('individualid').value);" readonly="true" modulename="Proposals" fieldname="individual" />
            <input type="button" name="lookup" value="<bean:message key='label.sales.lookup'/>" class="normalButton" onclick="c_lookup('Individual', document.getElementById('entityID').value)" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.billingaddress"/>:</td>
          <td class="contentCell">
            <html:hidden property ="billingaddressid" styleId="billingaddressid" />
            <app:cvtext property="billingaddress" styleId="billingaddress" styleClass="inputBox" size="35" readonly="true" modulename="Proposals" fieldname="billingaddress" />
            <input type="button" name="lookup" value="<bean:message key='label.sales.lookup'/>" class="normalButton" onclick="addressLookup('billto')" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.taxjurisdiction"/>:</td>
          <td class="contentCell">
            <html:select property="jurisdictionID" styleId="jurisdictionID" styleClass="inputBox">
              <html:option value="0">-- <bean:message key="label.sales.select"/> --</html:option>
              <html:optionsCollection property="jurisdictionVec" value="id" label="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.shippingaddress"/>:</td>
          <td class="contentCell">
            <html:hidden property="shippingaddressid"styleId="shippingaddressid" />
            <app:cvtext property="shippingaddress" styleId="shippingaddress" styleClass="inputBox" size="35" readonly="true" modulename="Proposals" fieldname="shippingaddress" />
            <input type="button" name="lookup" value="<bean:message key='label.sales.lookup'/>" class="normalButton" onclick="addressLookup('shipto')" />
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.sales.status"/>:</td>
          <td class="contentCell">
            <%
              Vector statusVec = new Vector();
              if (gml.get("AllSaleStatus") != null) {
                statusVec = (Vector)gml.get("AllSaleStatus");
              }
              pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="statuslist" styleId="statuslist" styleClass="inputBox" modulename="Proposals" fieldname="statuslist">
              <app:cvoptions collection="statusVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.stage"/>:</td>
          <td class="contentCell">
            <%
              Vector stageVec = new Vector();
              if (gml.get("AllSaleStage") != null) {
                stageVec = (Vector)gml.get("AllSaleStage");
              }
              pageContext.setAttribute("stageVec", stageVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="stage" styleId="stage" styleClass="inputBox" modulename="Proposals" fieldname="stage">
              <app:cvoptions collection="stageVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.type"/>:</td>
          <td class="contentCell">
            <%
              Vector typeVec = new Vector();
              if (gml.get("AllSaleType") != null) {
                typeVec = (Vector)gml.get("AllSaleType");
              }
              pageContext.setAttribute("typeVec", typeVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="typeid" styleId="typeid" styleClass="inputBox" modulename="Proposals" fieldname="typeid">
              <app:cvoptions collection="typeVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.probability"/>:</td>
          <td class="contentCell">
            <%
              Vector probabilityVec = new Vector();
              if (gml.get("AllSaleProbability") != null) {
                probabilityVec = (Vector)gml.get("AllSaleProbability");
              }
              pageContext.setAttribute("probabilityVec", probabilityVec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="probability" styleId="probability" styleClass="inputBox">
              <html:options collection="probabilityVec" property="id" labelProperty="name" />
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.estimatedclose"/>:</td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text property="ecmon" styleId="ecmon" styleClass="inputBox" maxlength="2" size="2" readonly="true"/></td>
                <td>/</td>
                <td><html:text property="ecday" styleId="ecday" styleClass="inputBox" maxlength="2" size="2" readonly="true" /></td>
                <td>/</td>
                <td><html:text property="ecyear" styleId="ecyear" styleClass="inputBox" maxlength="4" size="4" readonly="true" /></td>
                <td><a class="plainLink" onClick="openSelectDateTime();"><html:img page="/images/icon_calendar.gif" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.actualclose"/>:</td>
          <td class="contentCell">
            <table border="0" cellspacing="0" cellpadding="1">
              <tr>
                <td><html:text property="acmon" styleId="acmon" styleClass="inputBox" maxlength="2" size="2" readonly="true"/></td>
                <td>/</td>
                <td><html:text property="acday" styleId="acday" styleClass="inputBox" maxlength="2" size="2" readonly="true" /></td>
                <td>/</td>
                <td><html:text property="acyear" styleId="acyear" styleClass="inputBox" maxlength="4" size="4" readonly="true" /></td>
                <td><a class="plainLink" onClick="openSelectDateTime();"><html:img page="/images/icon_calendar.gif" alt="" /></a></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.terms"/>:</td>
          <td class="contentCell">
            <%
              Vector termVec = new Vector();
              if (gml.get("AllSaleTerm") != null) {
                termVec = (Vector)gml.get("AllSaleTerm");
              }
              pageContext.setAttribute("termVec", termVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="terms" styleId="terms" styleClass="inputBox" modulename="Proposals" fieldname="terms">
              <app:cvoptions collection="termVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="headerBG"><span class="plainText"><bean:message key="label.sales.forecastcalculation"/></span></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="mainHeader"><bean:message key="label.sales.proposalitems"/></td>
  </tr>
</table>
<%
  DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
  DecimalFormat rawCurrencyFormat = new DecimalFormat("#0.00");
  DecimalFormat wholeNumberFormat = new DecimalFormat("#0");
  NumberFormat percentageFormat = NumberFormat.getPercentInstance();

  int recordID = 0;
  String moduleName = null;
  String buttonOperation = "";
  if (request.getParameter("moduleName") != null) {
    moduleName = request.getParameter("moduleName");
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

  ItemLines al = proposallistform.getItemLines();
  if (al == null) { al = new ItemLines(); }
  al.calculate();
  Vector listcolumns =  al.getViewVector();
  Enumeration listcolumns_enumeration = listcolumns.elements();
  Set listkey = al.keySet();
  Iterator it =  listkey.iterator();

%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="detailButtonHeader">
      <app:cvbutton property="additem" styleId="additem" styleClass="normalButton" onclick="fnItemLookup()" recordID="<%=recordID%>" modulename="<%=moduleName%>" buttonoperationtype="<%=buttonRight%>">
        <bean:message key="label.add" /> <bean:message key="label.sales.item"/>
      </app:cvbutton>
      <app:cvbutton property="removeitem" styleId="removeitem" styleClass="normalButton" onclick="fnCheckBox()" recordID="<%=recordID%>" modulename="<%=moduleName%>" buttonoperationtype="<%=buttonRight%>">
        <bean:message key="label.remove"/> <bean:message key="label.sales.items"/>
      </app:cvbutton>
      <app:cvsubmit property="save_line_items" styleId="save_line_items" styleClass="normalButton" recordID="<%=recordID%>" modulename="<%=moduleName%>" buttonoperationtype="<%=buttonRight%>">
        <bean:message key="label.update"/>
      </app:cvsubmit>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="listHeader"><a class="listHeader" href="javascript:void(0);" onclick="selectAll(null, true);"><html:img page="/images/icon_check.gif" width="8" height="8" border="0" alt="" /> All</a></td>
    <%
      while (listcolumns_enumeration.hasMoreElements()){
        String listcolumnName = (String)listcolumns_enumeration.nextElement();
        %><td class="listHeader"><span class="listHeader"><%=listcolumnName%></span></td><%
      }
    %>
  </tr>
  <tr>
    <%
      if (! it.hasNext()) {
        %><td class="tableRowOdd" colspan="7" align="center"><a href="#" class="plainLink" onclick="fnItemLookup();"><bean:message key="label.sales.additem"/></a></td><%
      }

      int i = 1;
      int itemNum = 0;
      while (it.hasNext()) {
        ItemElement ele  = (ItemElement)al.get(it.next());
        String lineStatus = ele.getLineStatus();
        if (lineStatus == null) {
          lineStatus = "";
        }
        IntMember lineid = (IntMember)ele.get("LineId");
        IntMember itemid = (IntMember)ele.get("ItemId");
        StringMember sku = (StringMember)ele.get("SKU");
        IntMember qty	 = (IntMember)ele.get("Quantity");
        FloatMember priceEach = (FloatMember)ele.get("Price");
        FloatMember priceExe = (FloatMember)ele.get("PriceExtended");
        StringMember desc  = (StringMember)ele.get("Description");
        FloatMember taxAmount = (FloatMember)ele.get("TaxAmount");

        String rowClass = "tableRowOdd";
        String quantity = "";

        // This is for if status is deleted then do not show
        if (! lineStatus.equals("Deleted")) {
          if (i % 2 == 0) {
            rowClass = "tableRowEven";
					}else{
						rowClass = "tableRowOdd";
					}

          String priceEachString;
          try	{
            float priceEachValue = ((Float)priceEach.getMemberValue()).floatValue();
            priceEachString = rawCurrencyFormat.format(new Float(priceEachValue));
          }catch (Exception exception){
            priceEachString = "0.00";
          }

          %>
          <td class="<%=rowClass%>"><input type="checkbox" name="checkbox" value="<%=itemid.getDisplayString()%>" onClick="selectRow(this, true);"></td>
          <td nowrap class="<%=rowClass%>"><%=sku.getDisplayString()%></td>
					<td class="<%=rowClass%>"><html:text property="description" styleClass="textInputWhiteBlueBorder" value="<%=desc.getDisplayString()%>" size="50"/></td>
          <td nowrap class="<%=rowClass%>"><div align="right"><input type="text" name="quantity" class="textInputWhiteBlueBorder" value="<%=qty.getDisplayString()%>" size="6"/></div></td>
					<td nowrap class="<%=rowClass%>"><div align="right">$<html:text property="priceeach" styleClass="textInputWhiteBlueBorder" value="<%=priceEachString%>" size="6"/></div></td>
          <td align="right" nowrap class="<%=rowClass%>"><%=taxAmount.getMemberValue()%>%</td>
          <td class="<%=rowClass%>" nowrap><div align="right">$<input type="text"  name="calculatedprice" class="textInputWhiteBlueBorder" value="<%=priceExe.getDisplayString()%>" style="width:8em;font-size:100%;" dir="rtl" size="6" readonly></div></td>
          <%
          i++;
          itemNum = itemNum + 1;
        }else{
          %>
          <input type="hidden" name="description" value="<%=desc.getDisplayString()%>">
          <input type="hidden" name="quantity" value="<%=qty.getDisplayString()%>">
          <input type="hidden" name="priceeach" value="<%=priceEach.getDisplayString()%>">
          <%
        }

      %>
      <input type="hidden" name="lineid" value="<%=lineid.getMemberValue()%>">
      <input type="hidden" name="itemid" value="<%=itemid.getMemberValue()%>">
      <input type="hidden" name="sku" value="<%=sku.getDisplayString()%>">
      <input type="hidden" name="priceExtended" value="<%=priceExe.getMemberValue()%>">
      <input type="hidden" name="taxAmount" value=<%=taxAmount.getMemberValue()%>>
      <input type="hidden" name="linestatus" value="<%=ele.getLineStatus()%>">
      <%
    } //end of while
  %>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="itemsContainer" align="right">
      <table border="0" cellpadding="4" cellspacing="0">
        <tr>
          <td><span class="boldText"><bean:message key="label.sales.totalitems"/>:</span></td>
          <td align="right"><span class="boldText">0</span></td>
        </tr>
        <tr>
          <td><span class="boldText"><bean:message key="label.sales.subtotal"/>:</span></td>
          <td align="right"><span class="boldText">$0.00</span></td>
        </tr>
        <tr>
          <td><span class="boldText"><bean:message key="label.sales.tax"/>:</span></td>
          <td align="right"><span class="boldText">$0.00</span></td>
        </tr>
        <tr>
          <td class="itemTotalHR"><span class="boldText"><bean:message key="label.sales.ordertotal"/>:</span></td>
          <td class="itemTotalHR" align="right"><span class="boldText">$0.00</span></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td class="labelCellBold"><bean:message key="label.sales.specialinstructions"/>:</td>
    <td class="contentCell">
      <app:cvtextarea property="specialinstructions" styleId="specialinstructions" rows="4" cols="65" styleClass="inputBox" fieldname="specialinstructions" modulename="Proposals"/>
    </td>
  </tr>
  <tr>
    <td class="labelCellBold"><bean:message key="label.sales.attachments"/>:</td>
    <td class="contentCell">
      <%
        Vector attachFileValues = new Vector();
        pageContext.setAttribute("attachFileValues", attachFileValues, PageContext.PAGE_SCOPE);
      %>
      <table border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td>
            <html:select property="attachFileIds" styleId="attachFileIds" multiple="true" styleClass="inputBox" size="4" style="width:407px;">
              <html:options collection="attachFileValues" property="id" labelProperty="name" />
            </html:select>
          </td>
          <td>&nbsp;&nbsp;</td>
          <td>
            <app:cvbutton property="attachfile" styleId="attachfile" styleClass="normalButton" onclick="openemaillookup();">
              <bean:message key="label.sales.attachfile"/>
            </app:cvbutton>
            <br/><br/>
            <app:cvbutton property="removefile" styleId="removefile" styleClass="normalButton" onclick="removeAttachment();">
              <bean:message key="label.sales.removefile"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="detailButtonHeader">
      <app:cvsubmit property="save" styleId="save" styleClass="normalButton" onclick="return selectAll();" buttonoperationtype="20">
        <bean:message key="label.save"/>
      </app:cvsubmit>
      <app:cvsubmit property="save_close" styleId="save_close" styleClass="normalButton" onclick="return selectAll();" buttonoperationtype="20">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <app:cvsubmit property="save_new" styleId="save_new" styleClass="normalButton" onclick="return selectAll();" buttonoperationtype="20">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvbutton property="cancel" styleId="cancel" styleClass="normalButton" onclick="window.close();">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>