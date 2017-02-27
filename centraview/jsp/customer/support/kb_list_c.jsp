<%--
 * $RCSfile: kb_list_c.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:25:24 $ - $Author: mking_cv $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<tiles:insert page="/jsp/customer/templates/main_template.jsp" flush="true" >
  <tiles:put name="header" value="/jsp/customer/common/header.jsp" />
  <tiles:put name="menu_tabs" value="/jsp/customer/common/tabs_main.jsp" />
  <tiles:put name="left_menu" value="/jsp/customer/common/left_menu.jsp" />
  <tiles:put name="body" value="/jsp/customer/support/kblist_renderer.jsp" />
  <tiles:put name="footer" value="/jsp/customer/common/footer.jsp" /> 
</tiles:insert>
<app:TitleWindow moduleName="Support: Knowledgebase" />
