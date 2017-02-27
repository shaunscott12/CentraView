<%--
 * $RCSfile: search_bar.jsp,v $    $Revision: 1.6 $  $Date: 2005/09/02 20:14:50 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<table border="0" cellpadding="3" cellspacing="0" class="searchTable">
  <tr> 
    <!-- TODO: fix this form -->
    <form name="searchBar" action="javascript:searchBar_searchList();"> 
    <c:choose>  
      <c:when test="${requestScope.showNewButton}">
        <td>
          <input type="button" class="normalButton" statuswindow="<c:out value="${requestScope.newButtonValue}"/>" statuswindowarg="" tooltip="false" onclick="return searchBar_NewButton('<c:out value='${requestScope.listType}'/>',<c:out value='${requestScope.moduleId}'/>);" value="<c:out value='${requestScope.newButtonValue}'/>"/>
        </td>
      </c:when>
      <c:otherwise>
      </c:otherwise>
    </c:choose>  
    <c:if test="${requestScope.showSimpleSearch}">
      <td>
        <input name="searchTextBox" id="searchTextBox" class="inputBox" type="text" > <%-- Dynamically put a value here ? --%>
      </td>
      <td>
        <input class="normalButton" type="button" value="Search" onClick="searchBar_searchList();">
        <c:out value="${requestScope.searchButtonDescription}"/>
      </td>
    </c:if>
    <c:if test="${requestScope.showAdvancedSearch}">
      <td>or</td>
      <td>
        <select name="searchDropDown" class="inputBox" onChange="searchBar_advancedSearch(this.value);">
          <option value="0">Select a Saved Search</option>
          <c:forEach var="searchBean" items="${requestScope.searchList}">
            <option value="<c:out value="${searchBean.value}" />" <c:if test="${searchBean.value == requestScope.appliedSearch}">selected="selected"</c:if>>
            <c:out value="${searchBean.label}" />
            </option>
          </c:forEach>
          <option value="zip">----------------------------</option>
          <option value="edit">Edit Saved Search</option>
          <option value="new">New Saved Search</option>
        </select>
      </td>
      <c:if test="${not empty requestScope.appliedSearch}">
        <td>
          <%-- Passing Zero to searchBar_advancedSearch() clears the search --%>
          <a class="plainLink" href="javascript:searchBar_advancedSearch(-1);" >Clear Search</a>
        </td>
      </c:if>
    </c:if>
    <td>
      <a href="#" class="plainLink" onclick="c_openWindow('/help/centraview.htm', 'helpindex', 700, 550, '');">?</a>
    </td>
    <c:if test="${requestScope.showCustomViews}">
      <td>
        <select name="viewData" class="inputBox" onchange="searchBar_selectedView(this.value);">
          <c:forEach var="viewData" items="${requestScope.viewData}">
            <option value="<c:out value="${viewData.id}"/>"  <c:if test="${viewData.id == requestScope.userDefaultViewID}">selected="selected"</c:if> ><c:out value="${viewData.name}"/></option>
          </c:forEach>
          <%-- What is the default value to do nothing? --%>
          <option value="-1">----------------</option>
          <%-- what value to pass to go to edit page? --%>
          <option value="<c:out value="${requestScope.userDefaultViewID}"/>">Edit View</option>
          <%-- what value to pass to go to NEW page? --%>
          <option value="-2">New View</option>
        </select>
      </td>
    </c:if>
    <c:if test="${requestScope.showPrintButton}">
    <td><a class="plainLink" href="javascript:searchBar_callPrint();"><html:img page="/images/button_print.gif" alt="Print" border="0" width="19" height="17" /></a></td>
  </c:if>
    <td>
      <c:choose>
        <c:when test="${requestScope.moduleId == 30 && sessionScope.userobject.userPref.showSalesCharts}">
          <a class="plainLink" href="<html:rewrite page='/preference/toggle_chart.do' />?type=sales&listScope=<c:out value='${listScope}' />">Hide Charts</a>
        </c:when>
        <c:when test="${requestScope.moduleId == 30 && ! sessionScope.userobject.userPref.showSalesCharts}">
          <a class="plainLink" href="<html:rewrite page='/preference/toggle_chart.do' />?type=sales&listScope=<c:out value='${listScope}' />">Show Charts</a>
        </c:when>
        <c:when test="${requestScope.moduleId == 39 && sessionScope.userobject.userPref.showTicketCharts}">
          <a class="plainLink" href="<html:rewrite page='/preference/toggle_chart.do' />?type=ticket&listScope=<c:out value='${listScope}' />">Hide Charts</a>
        </c:when>
        <c:when test="${requestScope.moduleId == 39 && ! sessionScope.userobject.userPref.showTicketCharts}">
          <a class="plainLink" href="<html:rewrite page='/preference/toggle_chart.do' />?type=ticket&listScope=<c:out value='${listScope}' />">Show Charts</a>
        </c:when>
      </c:choose>
    </td>
    <INPUT type="hidden" name="resetForm" value="true" >
    <INPUT type="hidden" name="valueList" value="true" >
    <INPUT type="hidden" name="searchOn" value="" >
    <INPUT type="hidden" name="searchType" value="" >
    <INPUT type="hidden" name="moduleId" value="<c:out value='${requestScope.moduleId}'/>" >
    <INPUT type="hidden" name="listScope" value="<c:out value='${requestScope.listScope}'/>" >
    <INPUT type="hidden" name="subScope" value="<c:out value='${requestScope.subScope}'/>" >
    <INPUT type="hidden" name="superScope" value="<c:out value='${requestScope.superScope}'/>" >
    </form>
  </tr>
</table>  
<c:set var="folderIDString" value=""/>
<c:choose>
  <c:when test="${sessionScope.currentMailFolder != null}">
    <c:set var="folderIDString" value="&folderID=${sessionScope.currentMailFolder}"/>
  </c:when>
</c:choose>
<script language="JavaScript" type="text/javascript">
<!--
  <%-- Simple Search --%>
  function searchBar_searchList()
  {
    searchTextBox = window.document.searchBar.searchTextBox.value;
    <c:choose>
      <c:when test="${requestScope.listType == 'Rule'}">
        document.searchBar.action = "<html:rewrite page='/email/rule_list.do' />?filter="+searchTextBox+"&accountID=<%=request.getParameter("accountID")%><c:out value="${folderIDString}"/>";
          document.searchBar.submit();        
      </c:when>
      <c:when test="${requestScope.listType == 'CustomFields'}">
        document.searchBar.action = "<html:rewrite page='/administration/list_custom_fields.do' />?module=Contacts&filter="+searchTextBox;
        document.searchBar.submit();        
      </c:when>
      <c:otherwise>
        document.searchBar.searchOn.value = searchTextBox;
        document.searchBar.searchType.value = "SIMPLE";        
        document.searchBar.action = "<html:rewrite page='/AdvancedSearchPerform.do' />?superScope=<%=request.getAttribute("superScope")%>&subScope=<%=request.getAttribute("subScope")%>&listScope=<c:out value='${listScope}' />&moduleId=<c:out value='${moduleId}' />&resetForm=true&searchType=SIMPLE&valueList=true<c:out value='${folderIDString}'/>&searchOn="+searchTextBox;
        document.searchBar.submit();        
      </c:otherwise>
    </c:choose>
  }
  function searchBar_advancedSearch(searchId)
  {
    if (searchId == 'zip' || searchId == '0') 
    {
      document.searchBar.searchDropDown.value = '0';
      return false;
    } else if (searchId == 'new') {
      c_goTo("/advancedsearch/dispatch.do" + "?moduleId=" + "<c:out value='${moduleId}' />&listScope=<c:out value='${listScope}' />"+"&resetForm=true<c:out value="${folderIDString}"/>&valueList=true");
    } else if (searchId == 'edit') {
      c_goTo('/advancedsearch/dispatch.do'+'?moduleId='+'<c:out value="${moduleId}" />&listScope=<c:out value='${listScope}' />'+'&searchId='+'<c:out value="${requestScope.appliedSearch}" default="0" />'+'&valueList=true<c:out value="${folderIDString}"/>&resetForm=true');
    } else {
      var moduleid = '<c:out value="${moduleId}" />';
      var extraparameter = "";
      if (moduleid == 5){
        noteType = "<%=request.getAttribute("TYPEOFNOTE")%>";
        extraparameter ="&TYPEOFNOTE="+noteType;
      }
      if (moduleid == 6){
        folderId = <%=request.getAttribute("folderId")%>;
        fileType = "<%=request.getAttribute("fileTypeRequest")%>";
        if (folderId != null){
          extraparameter += "&folderId="+folderId;
        }
        extraparameter +="&TYPEOFFILELIST="+fileType;
      }
      c_goTo('/AdvancedSearchPerform.do'+'?superScope=<%=request.getAttribute("superScope")%>&subScope=<%=request.getAttribute("subScope")%>&listScope=<c:out value='${listScope}' />&moduleId='+'<c:out value="${moduleId}" />'+'&searchId='+searchId+'&resetForm=true<c:out value="${folderIDString}"/>&valueList=true'+extraparameter);
    }
  }

  function searchBar_NewButton(listType,moduleID){
    if (moduleID == 0){
      if (listType == 'Rule'){
        c_goTo('/email/new_rule.do?accountID=<bean:write name="rulesListForm" property="accountID" ignore="true" />');
      }
      if (listType == 'CustomFields') {
        c_goTo('/administration/new_custom_field.do?module=<c:out value="${param.module}" />');
      }
    }
    if(moduleID == '15'){
      window.open('<html:rewrite page="/contacts/new_individual.do" />?marketingListId=<%=pageContext.findAttribute("dbid")%>', "NewIndividualWindow","toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=730,height=412");
      return false;
    }  
    if (moduleID == '14'){
      window.open('<html:rewrite page="/contacts/new_entity.do" />?marketingListId=<%=pageContext.findAttribute("dbid")%>', "NewEntityWindow","toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=660,height=410");
      return false;
    }
    if(moduleID == '16'){
      window.document.forms[1].action="<html:rewrite page='/contacts/new_group.do' />";
      window.document.forms[1].submit();
      return false;
    }  
    if(moduleID == 2){
      c_openWindow("/email/compose.do",'',720,585,'')
      return false;
    }  
    if(moduleID == 3){
      <%
        String subScope = request.getParameter("subScope");
        if ((subScope == null) || subScope.equals(""))
        {
          subScope = (String)request.getAttribute("subScope");
          if ((subScope == null) || subScope.equals(""))
            subScope = "";
        }
      %>  
      window.open("<html:rewrite page="/activities/new_activity.do" />?subScope=<%=subScope%>", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');
      return false;    
    }
    if(moduleID == 5){
      window.open("<html:rewrite page="/notes/new_note.do" />?TYPEOFOPERATION=ADD&listScope=<c:out value='${param.listScope}'/>", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');
      return false;    
    }
    if(moduleID == 30){
      window.open("<html:rewrite page="/sales/new_opportunity.do" />", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385');
      return false;
    }    
    if(moduleID == 31){
      window.open("<html:rewrite page="/sales/new_proposal.do" />", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=750,height=590');
      return false;
    }
    if(moduleID == 32){
      window.open("<html:rewrite page="/marketing/new_listmanager.do" />", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=600,height=405');
      return false;
    }
    if(moduleID == 33){
      window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/new_promotion.do?TYPEOFOPERATION=ADDITEM&Forward=New";
      window.document.listrenderer.submit();
    }
    if(moduleID == 34){
      window.open("<html:rewrite page="/marketing/new_literaturefulfillment.do" />", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=740,height=385');
      return false;
    }
    if(moduleID == 35){
      window.document.listrenderer.action="<%=request.getContextPath()%>/marketing/new_event.do";
      window.document.listrenderer.submit();
    }
    if(moduleID == 36){
      window.open("<html:rewrite page="/projects/new_project.do" />", '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=730,height=360');    
      return false;
    }
    if(moduleID == 37){
      window.open("<html:rewrite page="/projects/new_task.do" />", 'NewTaskWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=no,width=730,height=360');    
      return false;
    }
    if(moduleID == 38){
      window.open("<html:rewrite page="/projects/new_timeslip.do" />", 'NewTimeSlipWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=730,height=360');    
      return false;
    }
    if(moduleID == 39){
      window.open("<html:rewrite page="/support/new_ticket.do" />", 'NewTicketWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=350');    
      return false;
    }
    if(moduleID == 40){
      c_goTo('/support/faq_view.do?typeofoperation=ADD');
      return false;
    }
    if(moduleID == 41){
      window.open("<html:rewrite page="/support/knowledgebase_new.do" />", 'NewKnowledgebaseWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=600,height=360');    
      return false;
    }
    //Order Type
    if (moduleID == 42){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_order.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 42)    

    //Invoice Type
    if (moduleID == 56){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_invoice.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 56)    

    //Payment Type
    if (moduleID == 43){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_payment.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 43)    

    //Expense Type
    if (moduleID == 44){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_expense.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 44)    

    //Purchase Order Type
    if (moduleID == 45){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_purchaseorder.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 45)    

    //Item Type
    if (moduleID == 46){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_item.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 46)    

    //Inventory Type
    if (moduleID == 48){    
      window.document.listrenderer.action="<%=request.getContextPath()%>/accounting/new_inventory.do";
      window.document.listrenderer.target="_parent";
      window.document.listrenderer.submit();
    }//end of if (listTypeId == 48)    

    //Vendor Type
    if (moduleID == 50){  
      c_openWindow('/contacts/entity_lookup.do', '', 400, 400,'');
    }//end of if (listTypeId == 50)

    //File Type
    if (moduleID == 6){  
      window.open("<html:rewrite page="/files/file_new.do"/>?folderId=<c:out value="${requestScope.folderId}"/>", 'NewFileWindow','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=715,height=445');    
      return false;
    }

    // Expense Form Type
    if (moduleID == 51){
      c_goTo("/hr/expenseform_new.do?action=new");
      return false;
    }

    // Time Sheet Type
    if (moduleID == 52){
      c_goTo("/hr/timesheet_new.do");
      return false;
    }

    // Employee Handbook Type
    if (moduleID == 53){
      c_openWindow("/hr/employeehandbook_new.do");
      return false;
    }
  }

  function searchBar_selectedView(viewId)
  {
    var selectedViewID = document.searchBar.viewData.selectedIndex;
    var selectedViewText = document.searchBar.viewData.options[selectedViewID].text;
    var selectedViewValue = document.searchBar.viewData.options[selectedViewID].value;
    if(selectedViewText =='Edit View'){
      document.listrenderer.action = "<%=request.getContextPath()%>/common/edit_view.do?listType=<c:out value="${requestScope.listType}"/>&moduleId=<c:out value='${moduleId}' /><c:out value="${folderIDString}"/>&listScope=<c:out value='${listScope}' />&viewId=" + selectedViewValue;
      document.listrenderer.submit();    
    }
    else if(selectedViewValue == -2){
      document.listrenderer.action = "<%=request.getContextPath()%>/common/new_view.do?listType=<c:out value="${requestScope.listType}"/>&moduleId=<c:out value='${moduleId}' /><c:out value="${folderIDString}"/>&listScope=<c:out value='${listScope}' />";
      document.listrenderer.submit();
    }
    else{
      document.listrenderer.action = "<%=request.getContextPath()%>/ChangeView.do?listType=<c:out value="${requestScope.listType}"/>&listScope=<c:out value='${listScope}' /><c:out value="${folderIDString}"/>&superScope=<%=request.getParameter("superScope")%>&viewId=" + selectedViewValue;
      document.listrenderer.submit();
    }
  }

  function searchBar_callPrint()
  {
    var currentPage = document.listrenderer.currentPage.value;
    var params = currentPage.substr(9);
    var mychild = c_openWindow("/common/print_valuelist.do?superScope=<%=request.getAttribute("superScope")%>&listScope=<%=request.getAttribute("listScope")%>&listType=<c:out value="${requestScope.listType}"/>&listScope=<c:out value='${listScope}' /><c:out value="${folderIDString}"/>&"+params, '', 725, 400,'');
  }
//-->
</script>
<!-- end search_bar.jsp -->
