<%--
 * $RCSfile: kb_detail.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:24 $ - $Author: mking_cv $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html:errors />
<html:form action="/customer/ViewKB.do">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr height="3">
    <td colspan="2" class="sectionalHeaderShadow"><html:img page="/images/spacer.gif" width="4" height="3" /></td>
  </tr>
  <tr height="17">
    <td class="sectionalHeader" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
    <td class="sectionalHeader"><span class="sectionalHeaderText"><bean:write name="customerKbForm" property="title" ignore="true" /></span></td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td>
      <br>
      <span class="blueTextOnWhite">
        <bean:write name="customerKbForm" property="detail" ignore="true" />
      </span>
    </td>
  </tr>
</table>
</html:form>
