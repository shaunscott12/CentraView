<%--
 * $RCSfile: home.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:47 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.home"/></td>
  </tr>
</table>
<table border="0" cellpadding="3" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td>
      <p><span class="boldText"><bean:message key="label.customer.pages.welcometosite"/>...</span></p>
      <p>
        <span class="plainText">
        <bean:message key="label.customer.pages.customizepage"/>
        </span>
      </p>
      <p>&nbsp;</p>
    </td>
  </tr>
</table>
