<%--
 * $RCSfile: confirm.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:53 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:form action="administration/global_replace.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.administration.global.globalreplace"/></td>
  </tr>
</table>
<p class="pageInstructions">
  <bean:write name="globalReplaceForm" property="failureOrSuccess" />
  </br></br>
  <span class="boldText"><bean:message key="label.pages.administration.global.fieldname"/>:</span> <bean:write name="globalReplaceForm" property="fieldName" ignore="true"/>
  </br></br>
  <bean:message key="label.pages.administration.global.withfollvalue"/>:
  </br></br>
  <span class="boldText"><bean:message key="label.pages.administration.global.value"/>:</span> <bean:write name="globalReplaceForm" property="actualValue" ignore="true"/>
  </br></br>
  <bean:message key="label.pages.administration.global.anotherglobalreplace"/>?
  </br></br>
  <input value="<bean:message key='label.value.nextglobalreplace'/>"  type="submit" class="normalButton"/>
</html:form>