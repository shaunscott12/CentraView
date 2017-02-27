<%--
 * $RCSfile: view_faq.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 1.0.0 (the "License"); you may not use this file except in
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
<html:form action="/customer/view_faq.do">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:write name="customerFaqForm" property="title" ignore="true" /></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="3" class="formTable">
  <tr>
    <td class="contentCell">
      <p><span class="boldText"><bean:message key="label.customer.pages.support.questionlist"/>:</span></p>
      <ul>
        <c:forEach var="question" items="${customerFaqForm.map.questionList}">
        <li><a href="#q<bean:write name="question" property="questionID" />" class="plainLink"><bean:write name="question" property="question" /></a></li>
        </c:forEach>
      </ul>
    </td>
  </tr>
</table>
<br/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.support.answers"/></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="4" class="formTable">
  <c:forEach var="qDetail" items="${customerFaqForm.map.questionList}">
  <tr valign="top">
    <td class="labelCellBold" valign="top"><a name="q<bean:write name="qDetail" property="questionID" />"></a><bean:message key="label.customer.pages.support.question"/>:</td>
    <td class="contentCell" valign="top"><bean:write name="qDetail" property="question" ignore="true" /></td>
  </tr>
  <tr valign="top">
    <td class="labelCellBold" valign="top"><bean:message key="label.customer.pages.support.answer"/>:</td>
    <td class="contentCell"><bean:write name="qDetail" property="answer" ignore="true" filter="false" /></td>
  </tr>
  <tr>
    <td colspan="2"><html:img page="/images/spacer.gif" width="1" height="3" /></td>
  </tr>
  </c:forEach>
</table>
</html:form>