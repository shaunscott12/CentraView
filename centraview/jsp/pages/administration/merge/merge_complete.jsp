<%--
 * $RCSfile: merge_complete.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.mergewizardcomplete"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="contentCell">
      <bean:message key="label.pages.administration.merge.mergecompleterecordsupdated"/>.
    </td>
  </tr>
</table>
<p/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.merge.resultssummary"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.merge.actualmergesdone"/>:</td>
    <td class="contentCell"><bean:write scope="session" name="mergeResult" property="mergesCompleted" ignore="true" /></td>
  </tr>
  <tr>
    <td class="labelCellBold"><bean:message key="label.pages.administration.merge.recordsdeleted"/>:</td>
    <td class="contentCell"><bean:write scope="session" name="mergeResult" property="recordsPurged" ignore="true" /></td>
  </tr>
</table>
<p/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
    <td class="sectionHeader">
      <input name="button" type="button" class="normalButton" onClick="c_goTo('<html:rewrite page="/administration/merge_search.do"/>');" value="<bean:message key='label.value.beginnewmerge'/>" />
    </td>
  </tr>
</table>
