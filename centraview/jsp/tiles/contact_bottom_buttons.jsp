<%--
 * $RCSfile: contact_bottom_buttons.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
 
<table border="0" cellspacing="0" cellpadding="0" width="100%" style="bottom: 0px;">
  <tr>
    <td class="detailBottomButtons">
      <input type="submit" name="savechanges" value="<bean:message key='label.value.savechanges'/>" class="normalButton" onclick="document.getElementById('contactForm').submit();">
      <input type="submit" name="saveandclose" value="<bean:message key='label.value.saveandclose'/>" class="normalButton" onclick="document.getElementById('closeWindow').value = 'true';document.getElementById('contactForm').submit();">
      <input type="button" name="cancel" value="<bean:message key='label.value.cancel'/>" class="normalButton" onclick="window.close();">
    </td>
  </tr>
</table>