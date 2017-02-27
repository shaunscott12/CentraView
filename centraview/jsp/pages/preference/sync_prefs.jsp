<%--
 * $RCSfile: sync_prefs.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:45 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="com.centraview.common.*"%>
<html:form action="/preference/update_sync_prefs.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.synchronization"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <bean:message key="label.preference.downloadableutility"/>.
    </td>
  </tr>
  <tr>
    <td class="contentCell">
      <a href="http://www.centraview.com/index.php?page=shop.browse&category_id=0867769725ccc7360e5c0013f4e42aa6&option=com_phpshop&Itemid=437" target="_blank" class="plainLink" title="Download Sync Client"><bean:message key="label.preference.downloadcvsyncclient"/></a>
    </td>
  </tr>
  <tr>
    <td class="contentCell">
      <bean:message key="label.preference.firstuseofsyncclient"/>.<br/> <bean:message key="label.preference.linktoconnecttosyncclient"/>:<br/><br/>
      <strong>URL:</strong> http://<%=request.getServerName()%>:<%=request.getServerPort()%>/<%=request.getContextPath()%>/
    </td>
  </tr>
  <tr>
    <td class="contentCell">
      <html:checkbox name="syncPreferencesForm" property="syncAsPrivate" styleId="syncAsPrivate" />
      <bean:message key="label.preference.privatesynchronizedrecords"/>.
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:submit styleClass="normalButton"><bean:message key='label.value.savepreferences'/> </html:submit>
    </td>
  </tr>
</table>
</html:form>