<%--
 * $RCSfile: new_note.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ page import="com.centraview.note.NoteConstantKeys" %>
<%@ page import="com.centraview.note.NoteForm" %>
<script>
<!--
  <% if (request.getAttribute("refreshWindow") != null && request.getAttribute("refreshWindow").equals("true")){ %>  
  window.opener.location.reload();
  <% } %>
  <% if (request.getAttribute("closeWindow") != null && request.getAttribute("closeWindow").equals("true")){ %>  
  window.close();
  <% } %>
-->
</script>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <html:form action="/notes/save_new_note.do">
  <tr>
    <td class="labelCell"><bean:message key="label.notes.title"/>:</td>
    <td class="labelCell"><html:text property="title" styleId="title" styleClass="inputBox" size="30" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.notes.detail"/>:</td>
    <td class="labelCell"><html:textarea property="detail" rows="4" cols="30" styleClass="inputBox" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.notes.entity"/>:</td>
    <td class="labelCell">
      <c:if test="${! empty param.entityid}">
        <html:hidden property="entityid" value="<%=(String)request.getParameter("entityid")%>" />
      </c:if>
      <c:if test="${empty param.entityid}">
        <html:hidden property="entityid"/>
      </c:if>

      <c:if test="${! empty param.entityname}">
        <html:text property="entityname" readonly="true" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.noteform.entityid.value);" size="30" value="<%=(String)request.getParameter("entityname")%>" />
      </c:if>
      <c:if test="${empty param.entityname}">
        <html:text property="entityname" readonly="true" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.noteform.entityid.value);" size="30"/>
      </c:if>

      <app:cvbutton property="changeentity" styleClass="normalButton" onclick="openLookup('entity');">
        <bean:message key="label.lookup"/>
      </app:cvbutton>
    </td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.notes.individual"/>:</td>
    <td class="labelCell">
      <html:hidden property="individualid"/>
      <html:text property="individualname" readonly="true" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual',document.noteform.individualid.value )" size="30" />
      <app:cvbutton property="changeindividual" styleClass="normalButton" onclick="openLookup('individual');">
        <bean:message key="label.lookup"/>
      </app:cvbutton>
    </td>
  </tr>
  <html:hidden property="listScope"/>
  <input type="hidden" name="bottomFrame" value="<c:out value='${param.bottomFrame}'/>" />
  </html:form>
</table>
