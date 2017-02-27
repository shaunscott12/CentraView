<%--
 * $RCSfile: bottom_custom_field.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ page import="com.centraview.contact.helper.CustomFieldVO" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="java.util.Vector" %>
<%
  CustomFieldVO field = (CustomFieldVO)request.getAttribute("CustomFieldVO");
  String fieldType = field.getFieldType();
  String fieldValue = field.getValue() ;
  String label  = field.getLabel() ;
  int recordID = field.getRecordID() ;

  if (fieldValue  == null){
    fieldValue = "";
  }
  if (fieldType  == null){
    fieldType  = "SCALAR";
  }
%>
<html:form action="/contacts/save_custom_field">
<html:hidden property="operation" />
<html:hidden property="recordName" />
<html:hidden property="listType" />
<html:hidden property="listFor" />
<input type="hidden" value="<%=field.getFieldID()%>" name="fieldid" />
<input type="hidden" value="<%=fieldType%>" name="fieldType" />
<input type="hidden" value="<%=recordID%>" name="recordID" />
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.fieldname"/>:</td>
    <td class="contentCell"><%=label%></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.contacts.content"/>:</td>
    <td class="contentCell">
      <%
        if (fieldType.equals("SCALAR")) {
          %><input name="text" type="text" class="textInputWhiteBlueBorder" size="30" value= "<%=fieldValue%>"><%
        }else{
          fieldValue = fieldValue.trim();
          if (fieldValue.equals("")) {
            fieldValue = "0";
          }
          Vector vec = field.getOptionValues();

          %>
          <select name="text" class="inputBox">
            <option value=""> -- <bean:message key="label.contacts.select"/> -- </option>
            <%
              if (vec != null) {
                for (int j=0; j<vec.size(); j++) {
                  DDNameValue ddName = (DDNameValue)vec.get(j);
                  int compId = Integer.parseInt(fieldValue);
                  if (ddName.getId() == compId) {
                    %><option value="<%=ddName.getId()%>" selected ><%=ddName.getName()%></option><%
                  }else{
                    %><option value="<%=ddName.getId()%>"><%=ddName.getName()%></option><%
                  }
                }
              }
            %>
          </select>
          <%
        }
      %>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="headerBG">
      <html:submit styleClass="normalButton" property="saveandclose">
        <bean:message key="label.saveandclose" />
      </html:submit>
      <html:reset styleClass="normalButton">
        <bean:message key="label.reset" />
      </html:reset>
      <html:submit styleClass="normalButton" property="cancel" onclick="goToPreviousPage();">
        <bean:message key="label.cancel" />
      </html:submit>
    </td>
  </tr>
</table>
</html:form>