<%
/*
 * $RCSfile: literaturefulfillment_button.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:52 $ - $Author: mking_cv $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<table border="0" cellpadding="3" width="100%">
  <tr>
    <td class="sectionHeader">
      <html:button property="saveclose" styleClass="normalButton" onclick="save('close')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      &nbsp;
      <html:button property="savenew" styleClass="normalButton" onclick="save('new')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      &nbsp;
      <html:reset styleClass="normalButton">
        <bean:message key="label.resetfields"/>
      </html:reset>
      &nbsp;
      <html:cancel styleClass="normalButton"  onclick="window.close()">
        <bean:message key="label.cancel"/>
      </html:cancel>
    </td>
  </tr>
</table>
