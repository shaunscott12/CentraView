<%--
 * $RCSfile: edit_file.jsp,v $ $Revision: 1.4 $ $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.Vector"%>
<%-- import --%>
<%@ page import="com.centraview.file.FileConstantKeys" %>
<%@ page import="com.centraview.file.FileForm" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.activity.helper.ActivityVO" %>
<html:errors/>
<html:form action="/files/save_edit_file.do" enctype="multipart/form-data">
<table border="0" cellpadding="0" cellspacing="0" class="formTable">
  <tr>
    <td>
      &nbsp;
    </td>
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>
            <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.save" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditFile('save')">
              <bean:message key="label.save"/>
            </app:cvbutton>
            <app:cvbutton property="saveclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditFile('saveclose')">
              <bean:message key="label.saveandclose"/>
            </app:cvbutton>
            <app:cvreset property="reset" styleClass="normalButton" statuswindow="label.resetfields" tooltip="true" statuswindowarg= "label.general.blank">
              <bean:message key="label.resetfields"/>
            </app:cvreset>
            <app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.cancel" tooltip="true" statuswindowarg= "label.general.blank"onclick="return saveEditFile('close')">
              <bean:message key="label.cancel"/>
            </app:cvbutton>
            <app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
              <bean:message key="label.properties"/>
            </app:cvbutton>
         </td>
        </tr>
      </table>
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td class="sectionHeader" colspan="2">
            <bean:message key="label.files.filedetails"/>
          </td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <!-- left area -->
          <td valign="top" align="left" width="50%">
            <table width="100%" border="0" cellpadding="0">
              <tr>
                <td>&nbsp;</td>
                <td class="labelCell">Title:</td>
                <td>&nbsp;</td>
                <td class="contentCell">
                  <html:text property="title" styleId="title" styleClass="inputBox" style="width:30em;"/>
                </td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td nowrap class="labelCell"><bean:message key="label.files.filename"/>: </td>
                <td align="left"></td>
                <td class="contentCell">
                  <html:text readonly="true" property="fileInfo" styleClass="inputBox"/>
                  <html:button property="downloadButton" styleId="downloadButton" onclick="downloadFile()" styleClass="normalButton" style="width:65px;">
                    <bean:message key="label.files.download"/>
                  </html:button>
                  </a>
                </td>
                <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td nowrap class="labelCell"><bean:message key="label.files.newfile"/></td>
              <td align="left"></td>
              <td class="contentCell">
                <html:file property="file" styleClass="inputBox" size="30" />
              </td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td nowrap class="labelCell"><bean:message key="label.files.location"/>: </td>
              <td align="left">&nbsp;</td>
              <td class="contentCell">
                <html:hidden property="uploadfolderid"/>
                <html:text property="uploadfoldername" styleClass="inputBox"/>
                <html:button property="upload" styleClass="normalButton" onclick="return file_openLookup('folder')" >
                  <bean:message key="label.files.change"/>
                </html:button>
              </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td >&nbsp;</td>
              <td class="labelCell"><bean:message key="label.files.description"/>: </td>
              <td align="left">&nbsp;</td>
              <td class="contentCell">
                <html:textarea property="description" cols="30" rows="4" styleClass="inputBox" style="width:30em"/>
              </td>
              <td>&nbsp;</td>
          </tr>
          <tr>
            <td >&nbsp;</td>
            <td align="left" class="labelCell"><bean:message key="label.files.owner"/>: </td>
            <td  align="left">&nbsp;</td>
            <td class="contentCell">
              <a href="javascript:c_openPopup('/contacts/view_individual.do?rowId=<c:out value="${fileform.ownerid}"/>')" class="plainLink" >
                <c:out value="${fileform.ownername}"/>
              </a>
            </td>
            <td >&nbsp;</td>
           </tr>
           <tr>
             <td>&nbsp;</td>
             <td class="labelCell"><bean:message key="label.files.author"/>: </td>
             <td>&nbsp;</td>
             <td class="contentCell">
               <html:hidden property="authorid"/>
               <html:text property="authorname"  styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.fileform.authorid.value )" />
               <html:button property="lookupauthor" styleClass="normalButton" onclick="c_lookup('Employee');">
                 <bean:message key="label.lookup"/>
               </html:button>
             </td>
             <td>&nbsp;</td>
           </tr>
           <tr>
            <td>&nbsp;</td>
            <td class="labelCell"><bean:message key="label.files.created"/>: </td>
            <td>&nbsp;</td>
            <td align="left" class="contentCell"><c:out value="${fileform.created}"/></td>
            <td>&nbsp;</td>
           </tr>
           <tr>
            <td>&nbsp;</td>
            <td class="labelCell"><bean:message key="label.files.modified"/>: </td>
            <td>&nbsp;</td>
            <td align="left" class="contentCell"><c:out value="${fileform.modified}"/></td>
            <td>&nbsp;</td>
           </tr>
         </table>
       </td>
       <td align="left" width="50%">
            <table width="100%" border="0" cellpadding="0">
             <tr>
              <td>&nbsp;</td>
              <td class="labelCell"><bean:message key="label.files.access"/>: </td>
              <td>&nbsp;</td>
              <td align="left" class="contentCell">
                <html:radio property="access" value="PRIVATE"/> <bean:message key="label.files.private"/>&nbsp;
                <html:radio property="access" value="PUBLIC"/> <bean:message key="label.files.public"/>
              </td>
              <td>&nbsp;</td>
             </tr>
            <tr>
              <td>&nbsp;</td>
              <td nowrap class="labelCell"><bean:message key="label.files.fileversion"/>: </td>
              <td>&nbsp;</td>
              <td align="left" class="contentCell">
                <html:text property="fileversion" styleClass="inputBox" />
              </td>
              <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td class="labelCell"><bean:message key="label.files.companynews"/>? </td>
              <td>&nbsp;</td>
              <td align="left" class="contentCell">
                <html:radio property="companynews" value="NO" onclick="selectCompanyNews()"/> <bean:message key="label.files.no"/> &nbsp;
                <html:radio property="companynews" value="YES" onclick="selectCompanyNews()"/> <bean:message key="label.files.yes"/>
             </td>
             <td>&nbsp;</td>
            </tr>
            <tr>
              <td>&nbsp;</td>
              <td class="labelCell"> <bean:message key="label.files.displayuntil"/>: </div>
              </td>
              <td>&nbsp;</td>
              <td align="left" nowrap class="contentCell">
                <div id= "tableDate">
                  <table border="0" cellpadding="0">
                    <tr>
                      <td class="labelCell">
                        <bean:message key="label.files.from"/>&nbsp;
                      </td>
                      <td class="labelCell">
                        <html:text property="startmonth" styleClass="inputBox" size="2" />
                      </td>
                      <td class="labelCell">/</td>
                      <td class="labelCell">
                        <html:text property="startday" styleClass="inputBox" size="2" />
                      </td>
                      <td class="labelCell">/</td>
                      <td class="labelCell">
                        <html:text property="startyear" styleClass="inputBox" size="4" />
                      </td>
                      <td>
                        &nbsp;
                      </td>
                      <td>
                        <a class="plainLink" onClick="popupCalendar('DETAIL_START')">
                          <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar"/>
                        </a>
                      </td>
                    </tr>
                    <tr>
                      <td class="labelCell"><bean:message key="label.files.to"/>&nbsp;</td>
                      <td class="labelCell">
                        <html:text property="endmonth" styleClass="inputBox" size="2" />
                      </td>
                      <td class="labelCell">/</td>
                      <td class="labelCell">
                        <html:text property="endday" styleClass="inputBox" size="2" />
                      </td>
                      <td class="labelCell">/</td>
                      <td class="labelCell">
                        <html:text property="endyear" styleClass="inputBox" size="4" />
                      </td>
                      <td>&nbsp;</td>
                      <td>
                         <a class="plainLink" onClick="popupCalendar('DETAIL_END')">
                          <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar"/>
                         </a>
                      </td>
                    </tr>
                  </table>
                </div>
              </td>
              <td align="left" class="labelCell">&nbsp;</td>
            </tr>
                <table border="0" cellspacing="0" cellpadding="0" width="100%">
                  <tr>
                    <td colspan="2" class="labelCell">&nbsp;</td>
                  </tr>
                  <tr>
                    <td class="sectionHeader" colspan="2"><bean:message key="label.files.reference"/>: </td>
                  </tr>
                </table>
                <table width="100%" border="0" cellpadding="0">
                  <tr>
                    <td >&nbsp;</td>
                    <td align="left" class="labelCell"><bean:message key="label.files.entity"/>: </td>
                    <td>
                      &nbsp;
                    </td>
                    <td align="left" class="contentCell">
                      <html:hidden property="entityid"/>
                      <html:text property="entityname" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.fileform.entityid.value )" />
                      <html:button property="lookupentity" styleClass="normalButton" onclick="c_lookup('Entity');">
                        <bean:message key="label.lookup"/>
                      </html:button>
                    </td>
                    <td >&nbsp;</td>
                </tr>
                <tr>
                    <td >&nbsp;</td>
                    <td  align="left" class="labelCell"><bean:message key="label.files.individual"/>: </td>
                    <td >&nbsp;</td>
                    <td align="left" class="contentCell">
                      <html:hidden property="individualid"/>
                      <html:text property="individualname"  styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.fileform.individualid.value )" />
                      <html:button property="lookupindividual" styleClass="normalButton" onclick="file_openLookup('individual')">
                        <bean:message key="label.lookup"/>
                      </html:button>
                    </td>
                    <td >&nbsp;</td>
                </tr>
                <tr>
                  <td >&nbsp;</td>
                  <td  align="left" class="labelCell"><bean:message key="label.files.relatedtype"/>: </td>
                  <td >&nbsp;</td>
                  <td align="left" class="contentCell">
                    <html:hidden property="relatedTypeID" />
                    <html:select property="relatedTypeValue" styleClass="inputBox" onchange="updateRelatedTypeID();">
                      <html:option value="" >-- <bean:message key="label.files.select"/> --</html:option>
                      <html:option value="Opportunity"><bean:message key="label.files.opportunity"/></html:option>
                      <html:option value="Project"><bean:message key="label.files.project"/></html:option>
                      <html:option value="Ticket"><bean:message key="label.files.ticket"/></html:option>
                    </html:select>
                  </td>
                  <td >&nbsp;</td>
                </tr>
                <tr>
                  <td >&nbsp;</td>
                  <td align="left" class="labelCell"><bean:message key="label.files.relatedfield"/>: </td>
                  <td >&nbsp;</td>
                  <td align="left" >
                    <html:hidden property="relatedFieldID" />
                    <html:text property="relatedFieldValue" styleClass="inputBox" size="32" readonly="true"/>
                    <app:cvbutton property="changeLookup1" styleClass="normalButton" onclick="openRelatedFieldList();">
                      <bean:message key="label.files.lookup"/>
                    </app:cvbutton>
                  </td>
                  <td >&nbsp;</td>
                </tr>
            </table>
        </td>
      </tr>
      <tr height="7">
        <td colspan="3"></td>
      </tr>
    </table>
  </td>
  <td>&nbsp;</td>
</tr>
<tr class="popupTableRow" height="7">
  <td colspan="3">&nbsp;</td>
</tr>
<tr>
  <td>&nbsp;</td>
  <td align="left" nowrap width="100%">
      <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.save" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditFile('save')">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="saveclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditFile('saveclose')">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      <app:cvreset property="reset" styleClass="normalButton" statuswindow="label.resetfields" tooltip="true" statuswindowarg= "label.general.blank">
        <bean:message key="label.resetfields"/>
      </app:cvreset>
      <app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.cancel" tooltip="true" statuswindowarg= "label.general.blank"onclick="return saveEditFile('close')">
        <bean:message key="label.cancel"/>
      </app:cvbutton>
      <app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission();">
        <bean:message key="label.properties"/>
      </app:cvbutton>
  </td>
  <td>&nbsp;</td>
 </tr>
</table>
<html:hidden property="otheruploadfoldername"/>
<input type="hidden" name="TYPEOFFILE" value="<%=request.getAttribute(FileConstantKeys.TYPEOFFILE).toString()%>">
<input type="hidden" name="CURRENTTAB" value="<%=request.getAttribute(FileConstantKeys.CURRENTTAB).toString()%>">
<input type="hidden" name="WINDOWID" value="<%=request.getAttribute(FileConstantKeys.WINDOWID).toString()%>">
<input type="hidden" name="TYPEOFOPERATION" value="<%=request.getAttribute(FileConstantKeys.TYPEOFOPERATION).toString()%>">
<input type="hidden" name="FFID" value="<%=request.getAttribute(FileConstantKeys.FFID).toString()%>">
<html:hidden property="ownername"/>
<html:hidden property="created"/>
<html:hidden property="modified"/>
<input type="hidden" name=rowId value="<c:out value='${fileform.fileId}'/>" />
</html:form>
<script>
<!--
  function gotoPermission()
  {
    var id = "<c:out value='${fileform.fileId}'/>";
    var title = "<c:out value='${fileform.title}'/>";
    c_openWindow('/common/record_permission.do?modulename=File&rowID=' + id + '&recordName=' + title, '', 400, 400, '');
  }

  function saveEditFile(saveClose) {
    //populateOtherFolderList();
    if (saveClose != 'save'){
      window.document.fileform.rowId.value = window.document.fileform.uploadfolderid.value;
    }
    window.document.fileform.action = "<%=request.getContextPath()%>/files/save_edit_file.do?saveType="+saveClose;
    window.document.fileform.target = "_self";
    window.document.fileform.submit();
    return false;
  }

  function file_openLookup(typeOfLookup)  {
    if (typeOfLookup == 'folder') {
      folderID=document.fileform.uploadfolderid.value;
      var folder = c_openWindow('/folder_lookup.do?actionType=FILE&folderID='+folderID, '', 400, 400,'');
    } else if (typeOfLookup == 'individual')  {
      if (document.fileform.entityid.value == "" && document.fileform.entityid.value <= 0)  {
        alert('Select entity');
        return false;
      }
      else{
        var entityname = document.fileform.entityname.value;
        c_lookup('Individual', document.fileform.entityid.value);
      }
    } else if (typeOfLookup == 'otherfolder') {
      folderID=document.fileform.uploadfolderid.value;
      var folder = openWindow('<bean:message key="label.url.root" />/files/folder_lookup.do?actionType=FILE&folderID='+folderID, '', 400, 400,'');
    }
    return false;
  }

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.fileform.entityname.readonly = false;
    document.fileform.entityname.value = name;
    document.fileform.entityid.value = id;
  }


  function setEmployee(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.fileform.authorname.readonly = false;
    document.fileform.authorid.value = id;
    document.fileform.authorname.value = firstName+" "+middleName+" "+lastName;
    document.fileform.authorname.readonly = true;
  }

  function setIndividual(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.fileform.individualid.value = id;
    document.fileform.individualname.readonly = false;
    document.fileform.individualname.value = firstName+" "+middleName+" "+lastName;
    document.fileform.individualname.readonly = true;
  }

  function changeParentFolder(newFolderID)
  {
    document.fileform.uploadfolderid.value = newFolderID;
    document.fileform.action = "<%=request.getContextPath()%>/files/file_view.do";
    document.fileform.submit();
  }

  function setOtherFolderList(name,id)  {
    var sizeOfOtherFolderList = window.document.fileform.otheruploadfoldername.options.length;
    window.document.fileform.otheruploadfoldername.options[sizeOfOtherFolderList] = new Option(name,id+"#"+name,"false","false");
  }

  function removeOtherFolderInfo()
  {
    var sizeOfOtherFolderList = window.document.fileform.otheruploadfoldername.options.length;
    if (window.document.fileform.otheruploadfoldername.selectedIndex == -1)
    {
      alert("Select a tag for removing");
    }
    if (window.document.fileform.otheruploadfoldername.selectedIndex >= 0)
    {
      i = 0;
      while (i < sizeOfOtherFolderList)
      {
        if(window.document.fileform.otheruploadfoldername.options[i].selected)
          document.fileform.otheruploadfoldername.options[i] = null;
        else
          i++;
      }
    }
  }

  function populateOtherFolderList()  {
    if (window.document.fileform.otheruploadfoldername != null) {
      var lengthSelected = window.document.fileform.otheruploadfoldername.options.length;
      for (var i=0;i<lengthSelected;i++)  {
        window.document.fileform.otheruploadfoldername.options[i].selected=true;
      }
    }
  }

  function updateRelatedTypeID()
  {
    var relatedType = document.fileform.relatedTypeValue.options[document.fileform.relatedTypeValue.selectedIndex].value;
    if (relatedType == "Opportunity")
    {
      document.fileform.relatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_OPPORTUNITY%>;
    }
    else if (relatedType == "Project")
    {
      document.fileform.relatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_PROJECT%>;
    }
    else if (relatedType == "Ticket")
    {
      document.fileform.relatedTypeID.value = <%=ActivityVO.ACTIVITY_LINK_TICKET%>;
    }
    else
    {
      document.fileform.relatedTypeID.value = 0;
    }
    document.fileform.relatedFieldID.value = "";
    document.fileform.relatedFieldValue.value = "";
  } //end of updateRelatedTypeID method

  function openRelatedFieldList()
  {
    var relatedType = document.fileform.relatedTypeValue.options[document.fileform.relatedTypeValue.selectedIndex].value;
    var entityID = document.fileform.entityid.value;
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
      c_lookup('Ticket');
    }
  } //end of openRelatedFieldList method

  function setOpp(opportunityLookupValues)
  {
    name = opportunityLookupValues.Name;
    id = opportunityLookupValues.ID;
    document.fileform.relatedFieldID.value = id;
    document.fileform.relatedFieldValue.value = name;
  }

  function setProject(projectLookupValues)
  {
    name = projectLookupValues.Name;
    id = projectLookupValues.idValue;
    document.fileform.relatedFieldID.value = id;
    document.fileform.relatedFieldValue.value = name;
  }

  function setTicket(ticketLookupValues)
  {
    name = ticketLookupValues.Name;
    id = ticketLookupValues.idValue;
    document.fileform.relatedFieldID.value = id;
    document.fileform.relatedFieldValue.value = name;
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
    if(document.fileform.companynews[0].checked==false)
    {
      document.fileform.access[1].checked = true;
      document.fileform.access[0].disabled = true;
      document.getElementById("detailDateLabel").style.display= "block";
      document.getElementById("tableDate").style.display= "block";
    }
    else
    {
      document.fileform.access[0].disabled = false;
      document.getElementById("tableDate").style.display= "none";
      document.getElementById("detailDateLabel").style.display= "none";
    }
  }//end function SelectCompanyNews

  selectCompanyNews();

  function downloadFile()
  {
    fileid = document.fileform.rowId.value;
    if (fileid != "") {
      location.href = "<html:rewrite page="/files/file_download.do"/>?fileid=" + fileid;
    }
  }
-->
</script>