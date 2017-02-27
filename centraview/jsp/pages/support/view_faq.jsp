<%--
 * $RCSfile: view_faq.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.common.Constants" %>
<%@ page import="com.centraview.support.common.SupportConstantKeys" %>
<%@ page import="java.util.*,com.centraview.common.*,com.centraview.support.faq.*" %>

<html:form action="/support/save_faq.do">
<table border="0" cellpadding="3" cellspacing="0" class="formTable" width="100%">
  <tr>
    <td width="10%" class="labelCellBold"><bean:write name="faqform" property="title" ignore="true"/></td>
 		<c:if test="${showEditRecordButton}">
	 		<td align="left" class="pagingButtonTD">
				<app:cvbutton property="editrecord" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="viewrecord()">
          <bean:message key="label.support.editrecord"/>
				</app:cvbutton>
	 		</td>
		</c:if>
  </tr>
  <tr>
  	<td class="contentCell" colspan="2">
			<ul>
<%
	QuestionList DL = (QuestionList)request.getAttribute("questionlist");
	Vector vecColumns = (Vector)request.getAttribute("questionColumns");
	if (DL != null) {
		int i = 0 ;
		if (DL.size() > 0) {
			Set listkey = DL.keySet();
			Iterator iter=listkey.iterator();
			String str = null;
			while(iter.hasNext()) {
				i++;
				str = (String)iter.next();
				ListElement ele  = (ListElement)DL.get(str);
				Enumeration enum =  vecColumns.elements();
				String strr =  (String) enum.nextElement();
				StringMember sm = (StringMember)ele.get("Question");
				if (sm != null) {
%>
				<li><a href="#q<%=i%>" class="plainLink"><%=sm.getDisplayString()%></a></li>
<%
				}
			}
		}
	}
%>
			</ul>
		</td>
	</tr>
	<tr>
<%
	if (DL != null && DL.size() > 0) {
		int i = 0 ;
		Set listkey = DL.keySet();
		Iterator iter=listkey.iterator();
		String str = null;
		while (iter.hasNext()) {
			i++;
			str = (String)iter.next();
			ListElement ele = (ListElement)DL.get(str);
			Enumeration enum = vecColumns.elements();
			while (enum.hasMoreElements()) {
				String strr = (String)enum.nextElement();
				ListElementMember sm = (ListElementMember)ele.get(strr);
					if (sm !=null) {
						if (strr.equalsIgnoreCase("Question")) {
%>
		<td class="labelCellBold" colspan="2"><a name="q<%=i%>"></a><%=sm.getDisplayString()%></td>
	</tr>
	<tr>
<%
						}else{
%>
		<td class="contentCell" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;<%=sm.getDisplayString()%></td>
	</tr>
<%
					}
				}
			}
		}
	}
%>
</table>
<html:hidden property="faqid"/>
</html:form>

<script language="javascript">
function viewrecord() {
	c_goTo("/support/faq_view.do?rowId=<%=request.getParameter("rowId")%>&<%=Constants.TYPEOFOPERATION%>=<%=SupportConstantKeys.EDIT%>");
	return false;
}
</script>