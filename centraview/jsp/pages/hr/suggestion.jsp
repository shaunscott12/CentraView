<%
/**
 * $RCSfile: suggestion.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:50 $ - $Author: mcallist $
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
%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:form action="/hr/send_suggestion">
  <table width="100%" border="0" cellpadding="3" cellspacing="0" class="formTable">
    <tr>
      <td class="labelCell"><bean:message key="label.hr.suggestionformanagement"/>.
      </td>
    </tr>
    <tr>
      <td class="contentCell">
        <html:textarea property="suggestion" styleClass="inputBox" cols="60" rows="8"  value=""/>
      </td>
    </tr>
    <tr>
      <td class="sectionHeader">
        <html:submit property="Submit" styleClass="normalButton">
          <bean:message key="label.submit"/>
        </html:submit>
      </td>
    </tr>
  </table>
</html:form>