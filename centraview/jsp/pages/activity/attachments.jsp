<%--
 * $RCSfile: attachments.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.activity.ConstantKeys" %>
<%@ page import="com.centraview.activity.AttendeeList" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.activity.ActivityForm" %>
<html:hidden property="activityAttachedFileID" styleId="activityAttachedFileID" />
<input type="hidden" name="listSelectedFileId">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="49%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.attachfilefromcw"/>.</td>
        </tr>
        <tr>
          <td class="contentCell">
            <html:text property="activityAttachedCentraviewFile" styleId="activityAttachedCentraviewFile" styleClass="inputBox" size="35"/>
            <input name="button42" type="button" class="normalButton" value="<bean:message key='label.activity.lookup'/>" onClick="c_openWindow('/files/file_lookup.do', '', 400, 400,'');">
          </td>
        </tr>
        <tr>
          <td class="contentCell" style="height:25px;"><html:img page="/images/spacer.gif" width="1" height="1" border="0" alt="" /></td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.attachfilefromdesktop"/>.</td>
        </tr>
        <tr>
          <td class="contentCell">
            <html:file property="activityFile" styleId="activityFile" size="35" />
          </td>
        </tr>
      </table>
    </td>
    <td width="2%" align="center" valign="middle">
      <table border="0" cellpadding="3" cellspacing="0">
        <tr>
          <td>
            <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" style="width:65px;" onclick="act_attachFile()">
              <bean:message key="label.activity.attach"/> &raquo;
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td>
            <app:cvbutton property="changeLookup1" styleId="changeLookup1" styleClass="normalButton" style="width:65px;" onclick="act_removeFile()">
              &laquo; <bean:message key="label.remove"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td>
            <html:button property="downloadButton" styleId="downloadButton" onclick="act_downloadFile();" styleClass="normalButton" style="width:65px;">
              <bean:message key="label.activity.download"/>
            </html:button>
          </td>
        </tr>
      </table>
    </td>
    <td width="49%" valign="top" align="center">
      <table border="0" cellpadding="3" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold"><bean:message key="label.activity.attachedfiles"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            <%
              ActivityForm activityform = (ActivityForm)request.getAttribute("activityform");
              Vector fileListVec = new Vector();
              if (activityform.getActivityFilesListVec() != null) {
                fileListVec = (Vector)activityform.getActivityFilesListVec();
              }
              pageContext.setAttribute("fileListVec", fileListVec, PageContext.PAGE_SCOPE);
            %>
            <html:select property="activityFilesList" styleId="activityFilesList" size="7" multiple="true" style="width:250px;" styleClass="inputBox">
              <html:options collection="fileListVec" property="strid" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>