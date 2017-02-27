<%--
 * $RCSfile: search_bar.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:46 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<table border="0" cellpadding="3" cellspacing="0" class="searchTable">
  <tr>
    <form name="HomeForm" action="#">
    <td>
      <input type="text" name="searchOn" value="search terms" class="inputBox" />
      <input type="submit" type="button" value="<bean:message key='label.value.search'/>" class="normalButton" />
    </td>
    <td>
      <a href="#" class="plainLink" onclick="c_openWindow('<html:rewrite page="/help/CentraView.htm"/>', 'helpindex', 700, 550, '');">?</a>
    </td>
    <td><span class="plainText"><bean:message key="label.tiles.calendar.performquicksearch"/>.</span></td>
    <INPUT type="hidden" name="resetForm" value="true" >
    <INPUT type="hidden" name="moduleId" value="14" >
    <INPUT type="hidden" name="searchType" value="SIMPLE" >
    </form>
  </tr>
</table>