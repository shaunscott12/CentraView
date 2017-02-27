<%--
 * $RCSfile: account_list.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<script language="Javascript">
  function deleteAccount(accountID)
  {
    if (confirm('Are you sure you want to delete this email account?')) {
      c_goTo('/preference/mail/delete_account.do?accountID=' + accountID);
    }
  }
</script>
<html:form action="/preference/mail/account_list">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.mail.emailaccounts"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" class="formTable">
  <tr>
    <td valign="top" class="labelCell"><bean:message key="label.preference.mail.definedaccounts"/>:</td>
    <td valign="top" class="contentCell">
      <table border="0" cellspacing="0" cellpadding="3">
        <c:if test="${empty mailAccountListForm.map.accountList}">
          <tr>
            <td><span class="boldText"><bean:message key="label.preference.mail.nodefinedaccounts"/>.</span></td>
          </tr>
        </c:if>
        <c:forEach var="account" items="${mailAccountListForm.map.accountList}">
        <tr>
          <td><a href="<%=request.getContextPath()%>/preference/mail/edit_account.do?accountID=<c:out value="${account.accountID}" />" class="plainLink"><c:out value="${account.emailAddress}" /></a></td>
          <td><a href="<%=request.getContextPath()%>/preference/mail/edit_account.do?accountID=<c:out value="${account.accountID}" />" class="plainLink"><bean:message key="label.preference.mail.edit"/></a></td>
          <td><a class="plainLink" onclick="deleteAccount('<c:out value="${account.accountID}" />');"><bean:message key="label.preference.mail.delete"/></a></td>
        </tr>
      </c:forEach>
      </table>
      <br>
      <br>
      <p></p>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow"><html:button property="newAccount" styleClass="normalButton" onclick="c_goTo('/preference/mail/new_account.do');"><bean:message key='label.value.newaccount'/></html:button></td>
  </tr>
</table>
</html:form>