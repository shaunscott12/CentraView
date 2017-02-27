<%--
 * $RCSfile: customer_logo.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*"%>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import = "org.apache.struts.action.DynaActionForm" %>
<%@ page import = "com.centraview.administration.common.AdministrationConstantKeys" %>
<% String customerlogo =(String)request.getAttribute("Cust"); %>
<script language="javascript">
  function gotosave(param)
  {
    document.getElementById("CustomerLogo").action = "<html:rewrite page="/administration/save_customer_logo.do"/>?buttonpress="+param;
    document.getElementById("CustomerLogo").submit();
  }
</script>
<html:form action="/administration/save_customer_logo.do" enctype="multipart/form-data" styleId="CustomerLogo">
<html:hidden property ="customerlogo" value="<%=customerlogo%>" />
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.customerlogoconfig"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell">
      <span class="boldText">
        <bean:message key="label.administration.selectfilelogo"/>
      </span>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCell"><bean:message key="label.administration.currentfile"/>:</td>
    <td class="contentCell"><html:text property="currentfile" styleId="currentfile" size="35" styleClass="inputBox"/></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.newfile"/>:</td>
    <td class="contentCell"><html:file property="file" styleId="file" accept="gif,jpg,png" size="34" /></td>
  </tr>
  <tr>
    <td class="labelCell"><bean:message key="label.administration.preview"/>:</td>
    <td class="contentCell"><html:img page="/customerlogo" alt="Customer Logo"/></td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <app:cvbutton property="Save" styleId="Save" styleClass="normalButton" onclick="gotosave('save')">
        <bean:message key="label.save"/>
      </app:cvbutton>
      <app:cvbutton property="Delete" styleId="Delete" styleClass="normalButton" onclick="gotosave('delete')">
        <bean:message key="label.delete"/>
      </app:cvbutton>
    </td>
  </tr>
</table>
</html:form>