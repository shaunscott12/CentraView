<%--
 * $RCSfile: login_nav_tabs.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the "License"); you may not use this file except in
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<!-- start tabs.jsp -->
<table border="0" cellspacing="0" cellpadding="0" class="tabsContainer">
  <tr>
    <td class="tabsContainerTopRow">
      <table border="0" cellspacing="0" cellpadding="0" class="tabsTable">
        <tr>
          <td class="firstTabOff"><html:img page="/images/spacer.gif" width="15" height="11" alt="" /></td>
          <td class="tabsOn"><html:link action="/start.do" styleClass="tabsOn"><bean:message key="label.tiles.login"/></html:link></td>
          <td class="lastTabOff"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="tabsBottomBar"><html:img page="/images/clear.gif" width="1" height="6" alt="" /></td>
  </tr>
</table>
<!-- end tabs.jsp -->