<%--
 * $RCSfile: edit_proposal.jsp,v $    $Revision: 1.6 $  $Date: 2005/09/02 20:14:50 $ - $Author: mcallist $
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
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.centraview.email.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.centraview.common.DDNameValue"%>
<%@ page import="com.centraview.common.*" %>
<%@ page import="com.centraview.email.emailfacade.*"%>
<%@ page import="com.centraview.sale.*"%>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.centraview.common.*,com.centraview.contact.*"%>
<%@ page import="com.centraview.sale.proposal.ProposalListForm" %>
<%@ page import="com.centraview.sale.proposal.ProposalConstantKeys" %>
<%@ page import="com.centraview.sale.proposal.ItemLines" %>
<%@ page import="com.centraview.sale.proposal.ItemElement" %>
<%@ page import="com.centraview.sale.OpportunityForm" %>
<%@ page import="com.centraview.administration.authorization.ModuleFieldRightMatrix" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<%
  OpportunityForm opportunityForm  = null;
  opportunityForm  = (OpportunityForm) request.getAttribute("opportunityForm");

  if (opportunityForm == null)
  {
    opportunityForm  = (OpportunityForm) session.getAttribute("opportunityForm");
  }

  ProposalListForm proposallistform = (ProposalListForm)request.getAttribute("proposallistform");
  session.setAttribute("proposallistform" , proposallistform);
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
  String emailDisableFlag = (String) request.getAttribute("emailDisableFlag");

  if (emailDisableFlag == null){
  	emailDisableFlag="";
  }
  String opportunity = opportunityForm.getTitle();
  int recordID = 0;
  if(proposallistform.getProposalid() != null && !proposallistform.getProposalid().equals("")){
  		recordID = Integer.parseInt(proposallistform.getProposalid());
  }
  request.setAttribute("recordID",recordID+"");
  int orderID = proposallistform.getOrderID();
	String entityId = opportunityForm.getEntityid();
	String entityname = opportunityForm.getEntityname();

  boolean newProposal = (request.getAttribute("newProposal") == null) ? false : ((Boolean)request.getAttribute("newProposal")).booleanValue();
  boolean openAddItem = (request.getAttribute("openAddItem") == null) ? false: ((Boolean)request.getAttribute("openAddItem")).booleanValue();

%>
<script language="Javascript">

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


  function askToDelete()
  {
    if (confirm("Are you sure you want to delete this record?")) {
      deleteProposal();
    }
  }
  function deleteProposal()
  {
    window.document.forms[0].action="<bean:message key="label.url.root" />/DeleteProposal.do";
    window.document.forms[0].submit();
    window.opener.location.reload();
    window.close();
  }
  function selectAll()
  {
    for (i=0; i < window.document.forms[0].attachFileIds.options.length; i++) {
      window.document.forms[0].attachFileIds.options[i].selected = true;
    }
    return true;
  }

  <% if (request.getAttribute("saveandclose") != null) { %>
  window.opener.parent.location.reload();
  window.close();
  <% } %>

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
      document.forms[0].action = "<html:rewrite page="/sales/view_proposal.do"/>?<%=ProposalConstantKeys.TYPEOFOPERATION%>=<%=ProposalConstantKeys.ADDITEM%>";
    }else{
      document.forms[0].action = "<html:rewrite page="/sales/view_proposal.do"/>?<%=ProposalConstantKeys.TYPEOFOPERATION%>=<%=ProposalConstantKeys.REMOVEITEM%>";
    }
    document.forms[0].submit();
  }

  function addRecord()
  {
    window.document.forms[0].submit();
  }

  function fnAddLookup(toBox)
  {
    toWhere = toBox;
    entityID = document.forms[0].entityID.value;
    if (entityID == 0) {
      alert("<bean:message key='label.alert.selectentity'/>")
      return;
    }
    c_openWindow('/AddressLookup.do?contactID=' + entityID + '&contactType=1', '', 400, 400,'');
  }

  function setOpp(opportunityLookupValues)
  {
    name = opportunityLookupValues.Name;
    id = opportunityLookupValues.ID;
    document.forms[0].opportunity.value = name;
    document.forms[0].opportunityid.value = id;
  }

  function fnInd()
  {
    c_openWindow("/PrContactLookup.do?entityId=<%=entityId%>&entityname='<%=entityname%>'", '', 400, 400,'');
  }

  var individualId = 0;
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

  function setAddress(addressLookupValues)
  {
    name = addressLookupValues.Name;
    id = addressLookupValues.ID;
    jurisdictionID = addressLookupValues.jurisdictionID;

    if (toWhere == "billto") {
      if (jurisdictionID != '0') {
        var attachWidget = document.forms[0].jurisdictionID;
        var newIndex = attachWidget.length ;
        var k = 0;
        while (k < newIndex) {
          if (attachWidget.options[k].value == jurisdictionID) {
            attachWidget.options[k].selected = true;
          }
          k++;
        }
      }
      document.forms[0].billingaddress.value = name;
      document.forms[0].billingaddressid.value = id;
      idAddress = id;
    }else if (toWhere == "shipto"){
      document.forms[0].shippingaddress.value = name;
      document.forms[0].shippingaddressid.value = id;
      idAddress = id;
    }
  }

  function popupCalendar(calendarId)
  {
    c_openWindow("/jsp/activity/popupcalendar_activity.jsp?calenderId="+calendarId, 'sd', 250, 200, 'scrollbars=no, resizable=no');
  }

  function callDetailDate(type,gt_month,gt_date,gt_year)
  {
    if (type == "ESTIMATED_CLOSE") {
      setEstimatedClose(gt_month,gt_date,gt_year);
    }else{
      setActualClose(gt_month,gt_date,gt_year);
    }
  }

  function setEstimatedClose(st_month,st_date,st_year)
  {
    document.forms[0].ecmon.value = st_month;
    document.forms[0].ecday.value = st_date;
    document.forms[0].ecyear.value = st_year;
  }

  function setActualClose(end_month,end_date,end_year)
  {
    document.forms[0].acmon.value = end_month;
    document.forms[0].acday.value = end_date;
    document.forms[0].acyear.value = end_year;
  }

  function openemaillookup()
  {
    window.open('/centraview/email/attachment_lookup.do', 'ProposalAttachments','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=715,height=200');
  }

  function updateAttachmentBlank()
  {
    var newIndex = document.forms[0].attachFileIds.length ;
    var k = newIndex - 1;
    while (k >= 0) {
      document.forms[0].attachFileIds.options[k] = null ;
      k--;
    }
  }

  function updateAttachments(strAttach, strAttachID)
  {
    var option0 = new Option( strAttach , strAttachID );
    alert(strAttach +strAttachID);
    var newIndex = document.forms[0].attachFileIds.length ;
    document.forms[0].attachFileIds.options[newIndex] = option0;
  }

  function removeAttachmentsContent()
  {
    var length = document.forms[0].attachFileIds.options.length - 1;
    for (var i = length; i >= 0 ; i--) {
      if (document.forms[0].attachFileIds.options[i].selected) {
        document.forms[0].attachFileIds.options[i] = null;
  		}
  	}
  }

  function callNewOrder()
  {
    entityid = <%=entityId%>;
    entityname = '<%=entityname%>';
    document.forms[0].action = "<html:rewrite page="/sales/edit_proposal.do"/>?entityId="+entityid+"&entityName="+entityname+"&orderFlag=yes";
    document.forms[0].submit();
  }

	var flag = true;

	function fnCheckBox()
	{
	  // function called for remove operation
	  var hidden="";
	  var str="";
	  var k=0;
	  if( document.forms[0].checkbox == null)
	  {
	    alert("<bean:message key='label.alert.noitemsavailable'/>");
	    return;
	  }
	  for (i=0 ;i<document.forms[0].elements.length;i++)
	  {
	    if (document.forms[0].elements[i].type=="checkbox")
	    {
	      k++;
	    }
	  }
	  if(k>1)
	  {
	    for(i =0 ;i<document.forms[0].checkbox.length;i++)
	    {
	      if ( document.forms[0].checkbox[i].checked)
	      {
	        hidden=document.forms[0].checkbox[i].value + ",";
	        str=str+hidden;
	      }
	    }
	    str=(str.substring(0,str.length-1));
	  }else if (k==1)
	  {
	    hidden=document.forms[0].checkbox.value + ",";
	    str=str+hidden;
	    str=(str.substring(0,str.length-1));
	  }
	  if(!checkCheckbox())
	  {
	    alert("<bean:message key='label.alert.selectrecord'/>");
	    return false;
	  }
	  flag = false;
	  lookupValues = {IDs: "", RemoveIDs: str, Flag: false}
	  setItem(lookupValues);
	}

	function checkCheckbox()
	{
	  if ( document.forms[0].checkbox.length )
	  {
	    for(i =0 ;i<document.forms[0].checkbox.length;i++)
	    {
	      if ( document.forms[0].checkbox[i].checked)
	      {
	        return true;
	      }
	    }
	  }
	  else
	  {
	    return document.forms[0].checkbox.checked;
	  }
	}

  <% if (openAddItem) { %>
  c_lookup('Item', '');
  <% } %>
  
</script>
<html:form action="/sales/edit_proposal.do">
<%
  if (proposallistform.getEcyear().length() !=0 || proposallistform.getAcyear().length() !=0)
  {
    if (proposallistform.getEcyear().length() ==3)
    {
      int s = Integer.parseInt(proposallistform.getEcyear())+1900;
      String i = String.valueOf(s);
      proposallistform.setEcyear(""+i);

      if (proposallistform.getAcyear().length() !=0)
      {
        proposallistform.setAcyear(""+String.valueOf((Integer.parseInt(proposallistform.getAcyear())+1900)));
      }else{
        proposallistform.setAcyear("");
      }

      if (proposallistform.getEcmon().length() !=0)
      {
        proposallistform.setEcmon(""+String.valueOf((Integer.parseInt(proposallistform.getEcmon())+1)));
      }else{
        proposallistform.setEcmon("");
      }

      if (proposallistform.getAcmon().length() !=0)
      {
        proposallistform.setAcmon(""+String.valueOf((Integer.parseInt(proposallistform.getAcmon())+1)));
      }else{
        proposallistform.setAcmon("");
      }
    }
  }

    Vector attachFileValues = proposallistform.getAttachFileValues();
  if (attachFileValues == null)
  {
	  attachFileValues = new Vector();
  }
  HashMap filelist = new HashMap();
  for (int i=0;i<attachFileValues.size();i++)
  {
	  DDNameValue dnv = (DDNameValue) attachFileValues.get(i);
	  filelist.put(new Integer(dnv.getId()),dnv.getName());
  }
  pageContext.setAttribute("attachFileValues",attachFileValues);
  session.setAttribute("AttachfileList",filelist);
     if (proposallistform.getAttachFileIds() != null) {

        //TODO: see on it
  }
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="detailButtonHeader">
      <app:cvsubmit property="save_close" styleId="save_close" styleClass="normalButton" onclick="return selectAll();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvsubmit property="save_new" styleId="save_new" styleClass="normalButton" onclick="return selectAll();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <app:cvsubmit property="delete" styleId="delete" styleClass="normalButton" onclick="askToDelete();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/>
      </app:cvsubmit>
      <input type="button" name="cancel" value="<bean:message key='label.sales.cancel'/>" class="normalButton" onclick="window.close();" />
      <input type="button" name="sendnow" value="<bean:message key='label.sales.emailproposal'/>" class="normalButton" onclick="c_openPopup('/sales/compose_proposal.do?action=Send&eventid=<%=request.getParameter("eventid")%>');" <%=emailDisableFlag%> />
      <c:if test="${!proposallistform.orderIsGenerated}">
      <app:cvsubmit property="delete" styleId="delete" styleClass="normalButton" onclick="callNewOrder();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.sales.submitorder"/>
      </app:cvsubmit>
      </c:if>
      <c:if test="${proposallistform.orderIsGenerated}">
      <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
      <a onmouseover="return overlib('<b>Order Submited.</b>', CAPTION, 'Order Number is <%=orderID%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();"><input type="button" name="neworder" value="<bean:message key='label.sales.submitorder'/>" class="normalButton" disabled="true"/></a>
      </c:if>
      <app:cvbutton property="editpermissions" styleId="editpermissions" styleClass="normalButton" onclick="gotoPermission()"  recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.sales.editpermissions"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.sales.opportunitysummary"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="1" width="100%" class="mediumTableBorders">
  <tr>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.sales.opportunity"/> :</td>
          <td class="contentCell">
            <a onclick="showOpprtunity(<c:out value="${requestScope.opportunityForm.opportunityid}"/>);" class="plainLink" target="_blank"><c:out value="${requestScope.opportunityForm.title}" /></a>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.description"/> :</td>
          <td class="contentCell"><c:out value="${requestScope.opportunityForm.description}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.entity"/> :</td>
          <td class="contentCell">
            <a class="plainLink" onclick="showEntity(<c:out value="${requestScope.opportunityForm.entityid}" />);"><c:out value="${requestScope.opportunityForm.entityname}" /></a>
          </td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.sales.status"/> :</td>
          <td class="contentCell"><c:out value="${requestScope.opportunityForm.statusname}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.stage"/> :</td>
          <td class="contentCell"><c:out value="${requestScope.opportunityForm.stagename}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.opportunitytype"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.opportunityForm.opportunitytypename}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.forecastamount"/>:</td>
          <td class="contentCell">$<c:out value="${requestScope.opportunityForm.forecastedamount}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.accountmanager"/>:</td>
          <td class="contentCell"><c:out value="${requestScope.opportunityForm.acctmgrname}" /></td>
        </tr>
      </table>
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
          <td class="labelCell"><bean:message key="label.sales.title"/>:</td>
          <td class="contentCell">
            <html:text property="proposal" styleId="proposal" styleClass="inputBox" size="35"/>
            ID: <c:out value="${requestScope.proposallistform.proposalid}" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.description"/>:</td>
          <td class="contentCell">
            <app:cvtextarea property="prodescription" styleId="prodescription" rows="5" cols="44" styleClass="inputBox" fieldname="prodescription" modulename="Proposals"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.individual"/>:</td>
          <td class="contentCell" nowrap>
            <app:cvtext property="individual" styleId="individual" readonly="true" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.getElementById('individualid').value);"  size="35" modulename="Proposals" fieldname="individual"/>
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="c_lookup('Individual', document.getElementById('entityID').value)" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.billingaddress"/>:</td>
          <td class="contentCell" nowrap>
            <app:cvtext property="billingaddress" readonly="true" styleClass="inputBox" size="35" modulename="Proposals" fieldname="billingaddress"/>
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="fnAddLookup('billto')"  recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.taxjurisdiction"/> :</td>
          <td class="contentCell">
            <html:select property="jurisdictionID" styleId="jurisdictionID" styleClass="inputBox" size="1">
              <html:option value="0">-- <bean:message key="label.sales.select"/>  --</html:option>
              <html:optionsCollection property="jurisdictionVec" value="id" label="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.shippingaddress"/>:</td>
          <td class="contentCell" nowrap>
            <app:cvtext property="shippingaddress" styleId="shippingaddress" readonly="true" styleClass="inputBox" size="35" modulename="Proposals" fieldname="shippingaddress"/>
            <app:cvbutton property="lookup" styleId="lookup" styleClass="normalButton" onclick="fnAddLookup('shipto')"  recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.created"/> :</td>
          <td class="contentCell"><c:out value="${requestScope.proposallistform.createdDate}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.modified"/> :</td>
          <td class="contentCell"><c:out value="${requestScope.proposallistform.modifyDate}" /></td>
        </tr>
      </table>
    </td>
    <td width="50%" valign="top" class="mediumTableBorders">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCell"><bean:message key="label.sales.status"/> :</td>
          <td class="contentCell">
            <%
              Vector statusVec = new Vector();
              if (gml.get("AllSaleStatus") != null) { statusVec = (Vector)gml.get("AllSaleStatus"); }
              pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="statuslist" styleId="statuslist" styleClass="inputBox" modulename="Proposals" fieldname="statuslist">
              <app:cvoptions collection="statusVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.stage"/> :</td>
          <td class="contentCell">
            <%
              Vector stageVec = new Vector();
              if (gml.get("AllSaleStage") != null) { stageVec = (Vector)gml.get("AllSaleStage"); }
              pageContext.setAttribute("stageVec", stageVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="stage" styleId="stage" styleClass="inputBox" modulename="Proposals" fieldname="stage">
              <app:cvoptions collection="stageVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.proposaltype"/>:</td>
          <td class="contentCell">
            <%
              Vector typeVec = new Vector();
              if (gml.get("AllSaleType") != null) { typeVec = (Vector)gml.get("AllSaleType"); }
              pageContext.setAttribute("typeVec", typeVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="typeid" styleId="typeid" styleClass="inputBox" modulename="Proposals" fieldname="typeid">
              <app:cvoptions collection="typeVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.probability"/> :</td>
          <td class="contentCell">
            <%
              Vector probabilityVec = new Vector();
              if (gml.get("AllSaleProbability") != null) { probabilityVec = (Vector)gml.get("AllSaleProbability"); }
              pageContext.setAttribute("probabilityVec", probabilityVec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="probability" styleId="probability" styleClass="inputBox">
              <html:options collection="probabilityVec" property="id" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.forecastamount"/> :</td>
          <td class="contentCell"><html:text property="forcastAmount" styleId="forcastAmount" styleClass="inputBox" size="18" /></td>
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
          <td class="labelCell"><bean:message key="label.sales.actualclose"/> :</td>
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
              if (gml.get("AllSaleTerm") != null) { termVec = (Vector)gml.get("AllSaleTerm"); }
              pageContext.setAttribute("termVec", termVec, PageContext.PAGE_SCOPE);
            %>
            <app:cvselect property="terms" styleId="terms" styleClass="inputBox" modulename="Proposals" fieldname="terms">
              <app:cvoptions collection="termVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.sales.includeinforecast"/> ?</td>
          <td class="contentCell">
            <html:radio property="forecastinc" value="YES" onclick=""/> <bean:message key="label.sales.yes"/> &nbsp;&nbsp;&nbsp;
            <html:radio property="forecastinc" value="NO" onclick=""/><bean:message key="label.sales.no"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="headerBG"><span class="plainText"><bean:message key="label.sales.forecastcalculation"/> </span></td>
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

  //int recordID = 0;
  String moduleName = null;
  String buttonOperation = "";
  if (request.getParameter("moduleName") != null) {
    moduleName = request.getParameter("moduleName");
  }
  String ID = (String)request.getAttribute("recordID");
  if (ID != null && !ID.equals("")) {
    //recordID = Integer.parseInt(ID);
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
    <td class="listHeader"><a class="listHeader" href="javascript:void(0);" onclick="vl_selectAllRows(null, true);"><html:img page="/images/icon_check.gif" width="8" height="8" border="0" alt="" /> <bean:message key="label.sales.all"/></a></td>
    <%
      while (listcolumns_enumeration.hasMoreElements()){
        String listcolumnName = (String)listcolumns_enumeration.nextElement();
        %><td class="listHeader"><span class="listHeader"><%=listcolumnName%></span></td><%
      }
    %>
  </tr>
    <%
      if (! it.hasNext()) {
        %><tr><td class="tableRowOdd" colspan="7" align="center"><a href="#" class="plainLink" onclick="fnItemLookup();"><bean:message key="label.sales.additem"/></a></td></tr><%
      }

      int i = 1;
      int itemNum = 0;
      while (it.hasNext()) {
        %><tr><%
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
          <td class="<%=rowClass%>"><input type="checkbox" name="checkbox" value="<%=itemid.getDisplayString()%>" onclick="vl_selectRow(this, true);"></td>
          <td nowrap class="<%=rowClass%>"><%=sku.getDisplayString()%></td>
					<td class="<%=rowClass%>"><html:text property="description" styleClass="inputBox" value="<%=desc.getDisplayString()%>" size="35"/></td>
          <td nowrap class="<%=rowClass%>"><div align="right"><input type="text" name="quantity" class="inputBox" value="<%=qty.getDisplayString()%>" size="6"/></div></td>
					<td nowrap class="<%=rowClass%>"><div align="right">$<html:text property="priceeach" styleClass="inputBox" value="<%=priceEachString%>" size="6"/></div></td>
          <td align="right" nowrap class="<%=rowClass%>"><%=taxAmount.getMemberValue()%>%</td>
          <td class="<%=rowClass%>" nowrap><div align="right">$<input type="text"  name="calculatedprice" class="inputBox" value="<%=priceExe.getDisplayString()%>" style="width:8em;font-size:100%;" dir="rtl" size="6" readonly></div></td>
          <%
          i++;
          itemNum = itemNum + 1;
        }else{
          %>
          <input type="hidden" name="description" id="description"  value="<%=desc.getDisplayString()%>">
          <input type="hidden" name="quantity" id="quantity"  value="<%=qty.getDisplayString()%>">
          <input type="hidden" name="priceeach" id="priceeach"  value="<%=priceEach.getDisplayString()%>">
          <%
        }

      %>
      <input type="hidden" name="lineid" id="lineid"  value="<%=lineid.getMemberValue()%>">
      <input type="hidden" name="itemid" id="itemid"  value="<%=itemid.getMemberValue()%>">
      <input type="hidden" name="sku" id="sku"  value="<%=sku.getDisplayString()%>">
      <input type="hidden" name="priceExtended" id="priceExtended"  value="<%=priceExe.getMemberValue()%>">
      <input type="hidden" name="taxAmount" id="taxAmount"  value=<%=taxAmount.getMemberValue()%>>
      <input type="hidden" name="linestatus" id="linestatus"  value="<%=ele.getLineStatus()%>">
      </tr>
      <%
    } //end of while
  %>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="itemsContainer" align="right">
      <table border="0" cellpadding="4" cellspacing="0">
        <tr>
          <td><span class="boldText"><bean:message key="label.sales.totalitems"/>:</span></td>
          <td align="right">
            <%
              String totalItems;
              try {
                totalItems = wholeNumberFormat.format(new Float(al.getTotalItems()));
              }catch (Exception e){
                totalItems = "0";
              }
            %>
            <span class="boldText"><%=totalItems%></span>
          </td>
        </tr>
        <tr>
          <td><span class="boldText"><bean:message key="label.sales.subtotal"/>:</span></td>
          <td align="right">
            <%
              String subTotal;
              try {
                subTotal = currencyFormat.format(new Float(al.getSubtotal()));
              }catch (Exception e){
                subTotal = "0.00";
              }
            %>
            <span class="boldText">$<%=subTotal%></span>
          </td>
        </tr>
        <tr>
          <td><span class="boldText"><bean:message key="label.sales.tax"/>:</span></td>
          <td align="right">
            <%
              String sTax;
              try {
                sTax = currencyFormat.format(new Float(al.getTax()));
              }catch (Exception e){
                sTax = "0.00";
              }
            %>
            <span class="boldText">$<%=sTax%></span>
            <input type="hidden" name="tax" id="tax"  value="<%=sTax%>">
          </td>
        </tr>
        <tr>
          <td class="itemTotalHR"><span class="boldText"><bean:message key="label.sales.ordertotal"/>:</span></td>
          <td class="itemTotalHR" align="right">
            <%
              String total;
              try {
                total = currencyFormat.format(new Float(al.getTotal()));
              }catch (Exception e){
                total = "0.00";
              }
            %>
            <span class="boldText">$<%=total%></span>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type="hidden" name="theitemid" id="theitemid"/>
<input type="hidden" name="itemname" id="itemname" />
<input type="hidden" name="itemdesc" id="itemdesc" />
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
      <table border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td>
            <html:select property="attachFileIds" styleId="attachFileIds" multiple="true" styleClass="inputBox" size="4" style="width:407px;">
              <html:options collection="attachFileValues" property="strid" labelProperty="name" />
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
      <app:cvsubmit property="save_close" styleId="save_close" styleClass="normalButton" onclick="return selectAll();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.saveandclose"/>
      </app:cvsubmit>
      <app:cvsubmit property="save_new" styleId="save_new" styleClass="normalButton" onclick="return selectAll();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.saveandnew"/>
      </app:cvsubmit>
      <app:cvsubmit property="delete" styleId="delete" styleClass="normalButton" onclick="askToDelete();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.delete"/>
      </app:cvsubmit>
      <input type="button" name="cancel" value="<bean:message key='label.sales.cancel'/>" class="normalButton" onclick="window.close();" />
      <input type="button" name="sendnow" value="<bean:message key='label.sales.emailproposal'/>" class="normalButton" onclick="c_openPopup('/ComposeProposal.do?action=Send&eventid=<%=request.getParameter("eventid")%>');" <%=emailDisableFlag%> />
      <c:if test="${!proposallistform.orderIsGenerated}">
      <app:cvsubmit property="delete" styleId="delete" styleClass="normalButton" onclick="callNewOrder();" recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.DELETE_RIGHT%>">
        <bean:message key="label.sales.submitorder"/>
      </app:cvsubmit>
      </c:if>
      <c:if test="${proposallistform.orderIsGenerated}">
      <div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
      <a onmouseover="return overlib('<b>Order Submited.</b>', CAPTION, 'Order Number is <%=orderID%>', OFFSETX, 20, FGCOLOR, '#ffffff');" onmouseout="return nd();"><input type="button" name="neworder" value="<bean:message key='label.sales.submitorder'/>" class="normalButton" disabled="true"/></a>
      </c:if>
      <app:cvbutton property="editpermissions" styleId="editpermissions" styleClass="normalButton" onclick="gotoPermission()"  recordID="<%=recordID%>" modulename="Proposals" buttonoperationtype="<%=ModuleFieldRightMatrix.UPDATE_RIGHT%>">
        <bean:message key="label.sales.editpermissions"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
<input type=hidden name="removeID" id="removeID" />
<html:hidden property="proposalid" styleId="proposalid" />
<html:hidden property="createdDate" styleId="createdDate" />
<html:hidden property="modifyDate" styleId="modifyDate" />
<html:hidden property="probability" styleId="probability" />
<html:hidden property="shippingaddressid" styleId="shippingaddressid" />
<html:hidden property="billingaddressid" styleId="billingaddressid" />
<html:hidden property="individualid" styleId="individualid" />
<html:hidden property="opportunityid" styleId="opportunityid" />
<html:hidden property="orderID" styleId="orderID" />
<html:hidden property="orderIsGenerated" styleId="orderIsGenerated" />
<input type=hidden name="entityID" id="entityID"  value="<%=entityId%>"/>
<input type=hidden name="opportunity" id="opportunity"  value="<%=opportunity%>"/>
<input type="hidden" name="eventid" id="eventid"  value="<%=request.getParameter("eventid")%>">
</html:form>
