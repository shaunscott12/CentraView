<%--
 * $RCSfile: new_folder.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script language="javascript">
<!--
  <%
    if (request.getAttribute("closeWindow") != null
        && request.getAttribute("closeWindow").equals("true"))
  {
  %>
        window.close();
  <%
  }
  if (request.getAttribute("refreshWindow") != null
      && request.getAttribute("refreshWindow").equals("true"))
  {
  %>
       window.opener.location.reload();
  <%
  }
  %>
  function changeParentFolder(newFolderID)
  {
    document.forms[0].parentId.value = newFolderID;
    document.forms[0].action = "<%=request.getContextPath()%>/files/folder_new.do";
    document.forms[0].submit();
  }

  function openLookup(){
    folderID=document.forms[0].parentId.value;
    var folder = c_openWindow('/folder_lookup.do?actionType=FILE&folderID='+folderID, '', 400, 400,'');
  }
-->
</script>

<html:form action="/files/save_new_folder.do">
<html:errors/>
<html:hidden property="folderId"  />
<html:hidden property="parentId" />
<input type="hidden" name="TYPEOFOPERATION" value="<c:out value='${requestScope.TYPEOFOPERATION}'/>" />
<input type="hidden" name="TYPEOFFILE" value="<c:out value='${requestScope.TYPEOFFILE}'/>" />
<table border="0" cellPadding="3" cellSpacing="0" class="formTable">
  <tr>
    <td class="labelCell"> <bean:message key="label.files.foldername"/>: </td>
    <td>
      <html:text property="foldername" styleId="foldername" styleClass="inputBox"/>
    </td>
  </tr>
  <tr>
    <td class="labelCell"> <bean:message key="label.files.parentfolder"/>:</td>
    <td>
      <html:text readonly="true" styleClass="inputBox" property="subfoldername"/>
      <html:button property="changefolder" styleClass="normalButton" onclick="openLookup()">
        <bean:message key="label.files.change"/>
      </html:button>
    </td>
  </tr>

  <tr>
    <td class="labelCell"> <bean:message key="label.files.access"/>: </td>
    <td>
      <table border="0" >
          <tr>
            <td class="labelCell">
              <html:radio property="access" value="PRIVATE"/> <bean:message key="label.files.private"/>
            </td>
            <td class="labelCell">
              <html:radio property="access" value="PUBLIC"/> <bean:message key="label.files.public"/>
            </td>
           </tr>
        </table>
     </td>
   </tr>
</table>
</html:form>