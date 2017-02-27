<%--
 * $RCSfile: new_file.jsp,v $ $Revision: 1.2 $ $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
 * The developer of the Original Code is CentraView. Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved. The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.file.FileConstantKeys" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.activity.helper.ActivityVO" %>
<html:form action="/files/file_new.do" enctype="multipart/form-data">
<html:errors/>
<table border="0" cellspacing="0" cellpadding="2" class="formTable">
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.file"/>:  </td>
   <td>
    <c:if test="${requestScope.TYPEOFOPERATION == 'DUPLICATE'}">
      <html:text property="filename" readonly="true" styleClass="inputBox" disabled="false" size="30"/>
    </c:if>
    <c:if test="${requestScope.TYPEOFOPERATION != 'DUPLICATE'}">
     <html:file property="file" styleClass="inputBox" disabled="false" size="30" />
    </c:if>
   </td>
   <td class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.uploadtofolder"/>: </td>
   <td>
    <html:hidden property="uploadfolderid"/>
    <html:text property="uploadfoldername" styleClass="inputBox" size="30" readonly="true"/>
		 <c:choose>
		  <c:when test='${fileform.employeeHandBookFlag == "true"}'>
		  </c:when>
		  <c:otherwise>
		    <html:button property="changefolder" styleClass="normalButton" onclick="return openLookup('folder')" >
          <bean:message key="label.files.change"/>
		    </html:button>
	    </c:otherwise>
	  </c:choose>
   </td>
   <td class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.title"/>: </td>
   <td>
    <html:text property="title"  styleId="title"  styleClass="inputBox" size="30"/>
   </td>
   <td class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.description"/>: </td>
   <td>
    <html:textarea property="description" rows="5" styleClass="inputBox" style="width:25em;height:5em;"/>
   </td>
   <td class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.author"/>: </td>
   <td>
    <html:hidden property="authorid"/>
    <html:text readonly="true" property="authorname" styleClass="inputBox" size="30"/>
    <html:button property="changeauthor" styleClass="normalButton" onclick="javascrip:c_lookup('Employee');" >
    <bean:message key="label.lookup"/>
    </html:button>
    </td>
   <td  class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.entity"/>: </td>
   <td>
    <html:hidden property="entityid"/>
    <html:text readonly="true" property="entityname" styleClass="inputBox" size="30" />
    <html:button property="changeentity" styleClass="normalButton" onclick="javascrip:c_lookup('Entity');" >
    <bean:message key="label.lookup"/>
    </html:button>
   </td>
   <td class="labelCell">&nbsp;</td>
 </tr>

 <c:choose>
  <c:when test='${fileform.employeeHandBookFlag == "true"}'>
  </c:when>
  <c:otherwise>
     <tr>
       <td class="labelCell">&nbsp;</td>
       <td class="labelCell"><bean:message key="label.files.relatedtype"/>: </td>
       <td>
        <html:hidden property="relatedTypeID" />
        <html:select property="relatedTypeValue" styleClass="inputBox" onchange="updateRelatedTypeID();">
        <html:option value="" >-- <bean:message key="label.files.select"/> --</html:option>
        <html:option value="Opportunity"><bean:message key="label.files.opportunity"/></html:option>
        <html:option value="Project"><bean:message key="label.files.project"/></html:option>
        <html:option value="Ticket"><bean:message key="label.files.ticket"/></html:option>
        </html:select>
       </td>
       <td class="labelCell">&nbsp;</td>
     </tr>
     <tr>
       <td class="labelCell">&nbsp;</td>
       <td class="labelCell"><bean:message key="label.files.relatedfield"/>: </td>
       <td>
        <html:hidden property="relatedFieldID" />
        <html:text property="relatedFieldValue" styleClass="inputBox" size="32" readonly="true"/>
        <app:cvbutton property="changeLookup1" styleClass="normalButton" onclick="openRelatedFieldList();">
          <bean:message key="label.files.lookup"/>
        </app:cvbutton>
       </td>
       <td class="labelCell">&nbsp;</td>
     </tr>
    </c:otherwise>
  </c:choose>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.individual"/>: </td>
   <td>
    <html:hidden property="individualid"/>
    <html:text readonly="true" property="individualname" styleClass="inputBox" size="30"/>
    <html:button property="changeindividual" styleClass="normalButton" onclick="return openLookup('individual')" >
    <bean:message key="label.lookup"/>
    </html:button>
   </td>
   <td  class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td class="labelCell">&nbsp;</td>
   <td class="labelCell"><bean:message key="label.files.access"/>:</td>
   <td>
    <table width="235" border="0" cellpadding="0">
    <tr>
     <td>
      <html:radio property="access" value="PRIVATE"/> <bean:message key="label.files.private"/>
     </td>
     <td>
      <html:radio property="access" value="PUBLIC"/> <bean:message key="label.files.public"/>
     </td>
    </tr>
    </table>
   </td>
   <td  class="labelCell">&nbsp;</td>
 </tr>
 <tr>
   <td>
   </td>
   <td class="labelCell"><bean:message key="label.files.showincustomerview"/>?</td>
   <td>
    <table width="235" border="0" cellpadding="0">
    <tr>
     <td width="89" class="contentCell">
      <html:radio property="customerview" value="NO"/> <bean:message key="label.files.no"/>
     </td>
     <td width="91" class="contentCell">
      <html:radio property="customerview" value="YES"/> <bean:message key="label.files.yes"/>
     </td>
    </tr>
    </table>
   </td>
   <td  class="labelCell">&nbsp;</td>
 </tr>

 <c:choose>
  <c:when test='${fileform.employeeHandBookFlag == "true"}'>
  </c:when>
  <c:otherwise>
     <tr>
       <td>
       </td>
       <td class="labelCell"><bean:message key="label.files.companynews"/>?</td>
       <td>
         <table width="235" border="0" cellpadding="0">
         <tr>
           <td width="89" class="contentCell">
            <html:radio property="companynews" value="NO" onclick="selectCompanyNews()"/> No
           </td>
           <td width="91" class="contentCell">
            <html:radio property="companynews" value="YES" onclick="selectCompanyNews()"/> Yes
           </td>
         </tr>
         </table>
       </td>
       <td class="labelCell">&nbsp;</td>
     </tr>
     <tr>
       <td>
       </td>
       <td class="labelCell">
        <div id="detailDateLabel"> <bean:message key="label.files.showuntil"/>: </div>
       </td>
       <td nowrap>
        <div id= "tableDate">
          <table border="0" cellpadding="0">
           <tr>
             <td class="contentCell"><bean:message key="label.files.from"/>:&nbsp;</td>
             <td class="contentCell">
                <html:text property="startmonth" styleClass="inputBox" size="2" />
             </td>
            <td class="contentCell">/</td>
            <td class="contentCell">
              <html:text property="startday" styleClass="inputBox" size="2" />
            </td>
            <td class="contentCell">/</td>
            <td class="contentCell">
              <html:text property="startyear" styleClass="inputBox" size="4" />
            </td>
            <td>
            </td>
            <td>
              <a class="plainLink" onClick="popupCalendar('DETAIL_START')">
                <html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" />
              </a>
            </td>
           </tr>
           <tr>
            <td class="contentCell"><bean:message key="label.files.to"/>:&nbsp;</td>
            <td class="contentCell">
                <html:text property="endmonth" styleClass="inputBox" size="2" />
            </td>
            <td class="contentCell">/</td>
            <td class="contentCell">
              <html:text property="endday" styleClass="inputBox" size="2" />
            </td>
            <td class="contentCell">/</td>
            <td >
              <html:text property="endyear" styleClass="inputBox" size="4" />
            </td>
            <td>
            </td>
            <td>
              <a class="plainLink" onClick="popupCalendar('DETAIL_END')">
                <html:img page="/images/icon_calendar.gif" width="19" height="19" border="0" alt="" />
              </a>
            </td>
           </tr>
          </table>
        </div>
       </td>
       <td class="labelCell">&nbsp;</td>
     </tr>
    </c:otherwise>
  </c:choose>
</table>
<input type="hidden" name="TYPEOFOPERATION" value="<c:out value='${requestScope.TYPEOFOPERATION}'/>" />
<input type="hidden" name="TYPEOFFILE" value="<c:out value='${requestScope.TYPEOFFILE}'/>" />
<input type="hidden" name=rowId value="<c:out value='${fileform.fileId}'/>" />
</html:form>

<script language="javascript">
<!--
function openLookup(typeOfLookup) {
  if (typeOfLookup == 'folder') {
    folderID=document.forms[0].uploadfolderid.value;
    var folder = c_openWindow('/folder_lookup.do?actionType=FILE&folderID='+folderID, '', 400, 400,'');
  } else if (typeOfLookup == 'individual')  {
    var entityID = document.forms[0].entityid.value;
    if (entityID <= 0) {
      alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.");
    }else{
      c_lookup('Individual', entityID);
    }
  }
  return false;
}

function setEntity(entityLookupValues){
  name = entityLookupValues.entName;
  id = entityLookupValues.entID;
  acctmgrid = entityLookupValues.acctManagerID;
  acctmgrname = entityLookupValues.acctManager;

  document.forms[0].entityname.readonly = false;
  document.forms[0].entityname.value = name;
  document.forms[0].entityid.value = id;

  document.forms[0].individualname.value = "";
  document.forms[0].individualid.value = "";
  document.forms[0].entityname.readonly = true;
}

function setEmployee(individualLookupValues){
  firstName = individualLookupValues.firstName;
  id = individualLookupValues.individualID;
  middleName = individualLookupValues.middleName;
  lastName = individualLookupValues.lastName;
  title = individualLookupValues.title;

  document.forms[0].authorid.value = id;
  document.forms[0].authorname.readonly = false;
  document.forms[0].authorname.value = firstName+" "+middleName+" "+lastName;
  document.forms[0].authorname.readonly = true;
}

function setIndividual(individualLookupValues){
  firstName = individualLookupValues.firstName;
  id = individualLookupValues.individualID;
  middleName = individualLookupValues.middleName;
  lastName = individualLookupValues.lastName;
  title = individualLookupValues.title;

  document.forms[0].individualid.value = id;
  document.forms[0].individualname.readonly = false;
  document.forms[0].individualname.value = firstName+" "+middleName+" "+lastName;
  document.forms[0].individualname.readonly = true;
}

function changeParentFolder(newFolderID)
{
  document.forms[0].uploadfolderid.value = newFolderID;
  document.forms[0].action = "<%=request.getContextPath()%>/files/file_new.do";
  document.forms[0].submit();
}

function setOtherFolderList(name,id)  {
  var sizeOfOtherFolderList = window.document.fileform.otheruploadfoldername.options.length;
  window.document.fileform.otheruploadfoldername.options[sizeOfOtherFolderList] = new Option(name,id+"#"+name,"false","false");
}

function updateRelatedTypeID()
{
  var relatedType = document.forms[0].relatedTypeValue.options[document.forms[0].relatedTypeValue.selectedIndex].value;
  if (relatedType == "Opportunity")
  {
    document.forms[0].relatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_OPPORTUNITY%>;
  }
  else if (relatedType == "Project")
  {
    document.forms[0].relatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_PROJECT%>;
  }
  else if (relatedType == "Ticket")
  {
    document.forms[0].relatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_TICKET%>;
  }
  else{
    document.forms[0].relatedTypeID.value = 0;
  }
  document.forms[0].relatedFieldID.value = "";
  document.forms[0].relatedFieldValue.value = "";
} //end of updateRelatedTypeID method

function openRelatedFieldList()
{
  var relatedType = document.forms[0].relatedTypeValue.options[document.forms[0].relatedTypeValue.selectedIndex].value;
  var entityID = document.forms[0].entityid.value;
  if(entityID=="")
  {
    entityID="-1";//so the action class will not do filtering
  }
  if (relatedType == "Opportunity")
  {
    c_lookup('Opportunity', entityID);
  }
  else if (relatedType == "Project")
  {
    c_lookup('Project', entityID);
  }
  else if (relatedType == "Ticket")
  {
    var mychild = c_openWindow('/SupportTicketLookup.do', '', 400, 400,'');
  }
} //end of openRelatedFieldList method

function setOpp(opportunityLookupValues)
{
  name = opportunityLookupValues.Name;
  id = opportunityLookupValues.ID;
  document.forms[0].relatedFieldID.value = id;
  document.forms[0].relatedFieldValue.value = name;
}

function setProject(projectLookupValues)
{
  name = projectLookupValues.Name;
  id = projectLookupValues.idValue;
  document.forms[0].relatedFieldID.value = id;
  document.forms[0].relatedFieldValue.value = name;
}

function setTicket(ticketLookupValues)
{
  name = ticketLookupValues.Name;
  id = ticketLookupValues.idValue;
  document.forms[0].relatedFieldID.value = id;
  document.forms[0].relatedFieldValue.value = name;
}

function popupCalendar(calendarId)
{
  var startDate = document.forms.fileform.startmonth.value + "/" + document.forms.fileform.startday.value + "/" + document.forms.fileform.startyear.value;
  if (startDate == "//"){ startDate = ""; }
  var endDate = document.forms.fileform.endmonth.value + "/" + document.forms.fileform.endday.value  + "/" + document.forms.fileform.endyear.value;
  if (endDate == "//"){ endDate = ""; }
  c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate, 'selectDateTime', 350, 500, '');
}   // end function popupCalendar()

/*
  This function is called from the SelectDateTime.do popup, which passes
  the date and time information from that popup back to this JSP. The code
  within this function can be modified to do whatever you need to do with
  the data on this page, including munging it, setting form properties, etc.
  BUT YOU SHOULD NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, WHATSOEVER!
*/
function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
{
  var startDateArray = startDate.split("/")
  if (startDateArray == null || startDateArray.length < 3){
    document.forms.fileform.startmonth.value = "";
    document.forms.fileform.startday.value = "";
    document.forms.fileform.startyear.value = "";
  }else{
    document.forms.fileform.startmonth.value = startDateArray[0];
    document.forms.fileform.startday.value = startDateArray[1];
    document.forms.fileform.startyear.value = startDateArray[2];
  }

  var endDateArray = endDate.split("/")
  if (endDateArray == null || endDateArray.length < 3){
    document.forms.fileform.endmonth.value = "";
    document.forms.fileform.endday.value = "";
    document.forms.fileform.endyear.value = "";
  }else{
    document.forms.fileform.endmonth.value = endDateArray[0];
    document.forms.fileform.endday.value = endDateArray[1];
    document.forms.fileform.endyear.value = endDateArray[2];
  }
  return(true);
}

//check for companynews
function selectCompanyNews(){
  if(document.forms[0].companynews[0].checked==false)
  {
    document.forms[0].access[1].checked = true;
    document.forms[0].access[0].disabled = true;
    document.getElementById("detailDateLabel").style.display= "block";
    document.getElementById("tableDate").style.display= "block";
  }
  else
  {
    document.forms[0].access[0].disabled = false;
    document.getElementById("tableDate").style.display= "none";
    document.getElementById("detailDateLabel").style.display= "none";
  }
}//end function SelectCompanyNews
selectCompanyNews();

<%
  if (request.getAttribute("closeWindow") != null
      && request.getAttribute("closeWindow").equals("true"))
  {
%>
    window.close();
<%
  }
  if (request.getAttribute("refreshWindow") != null && request.getAttribute("refreshWindow").equals("true"))
  {
%>
  window.opener.location.reload();
<%
  }
%>
-->
</script>