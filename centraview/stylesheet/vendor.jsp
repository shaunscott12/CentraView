<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<% response.setContentType("text/javascript"); %>
<!--
/**
 * $RCSfile: vendor.jsp,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:46 $ - $Author: mking_cv $
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
function setEntity(entityLookupValues){
  name = entityLookupValues.entName;
  id = entityLookupValues.entID;
  acctmgrid = entityLookupValues.acctManagerID;
  acctmgrname = entityLookupValues.acctManager;
  window.document.listrenderer.action="<html:rewrite page="/accounting/save_vendor.do"/>?SelectedEntityId="+id;
  window.document.listrenderer.target="_parent";
  window.document.listrenderer.submit();
}
//-->
