<%--
/**
 * $RCSfile: mailmerge_success.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
 */
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td class="contentCell">
        <h4><bean:message key="label.marketing.success"/>!</h4>
        <p><bean:message key="label.marketing.printtemplatemerged"/>.</p>
        <c:choose>
          <c:when test="${(not empty requestScope.Message[1]) and (not empty requestScope.Message[2])}">
            <p><bean:message key="label.marketing.printtemplate"/>:&nbsp;<a href="<html:rewrite page="/files/file_download.do"/>?fileid=<c:out value="${requestScope.Message[2]}"/>">
              <bean:message key="label.marketing.clicktodownload"/><c:out value="${requestScope.Message[1]}"/></a>
            </p>
          </c:when>
          <c:otherwise>
            <bean:message key="label.marketing.messagesent"/>!
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
    <tr>
      <td align="left">
        <input type="button" name="button" value="<bean:message key='label.marketing.newmailmerge'/>" onclick="c_goTo('/marketing/mailmerge.do');" class="normalButton">
      </td>
    </tr>
</table>