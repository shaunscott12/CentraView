<%--
 * $RCSfile: new_edit_faq.jsp,v $ $Revision: 1.5 $ $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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
 * The developer of the Original Code is CentraView. Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved. The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
--%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.Constants" %>
<%@ page import="com.centraview.support.common.SupportConstantKeys" %>
<%@ page import="java.util.*,com.centraview.common.*,com.centraview.support.faq.*,org.apache.struts.util.MessageResources" %>

<html:form action="/support/save_faq.do">
<html:errors/>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr valign="top">
    <td class="sectionHeader">
<%
	String type = request.getParameter(Constants.TYPEOFOPERATION);
	if(type.equals(SupportConstantKeys.EDIT) || type.equals(SupportConstantKeys.DUPLICATE)) {
%>
<bean:message key='label.value.editfaq'/>
<%
	} else {
%>
<bean:message key='label.value.newfaq'/>
<%
	}
%>
    </td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="2" class="formTable">
	<tr>
		<td class="labelCell"><bean:message key="label.support.title"/>: </td>
		<td class="contentCell">
			<html:text property="title" styleClass="inputBox" size="45"/>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.status"/>: </td>
		<td class="contentCell">
			<html:select property="status" styleClass="inputBox">
				<html:option value="DRAFT"><bean:message key='label.value.draft'/></html:option>
				<html:option value="PUBLISH"><bean:message key='label.value.publish'/></html:option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td class="labelCell"><bean:message key="label.support.publishtocustomerview"/>: </td>
		<td class="contentCell"><html:checkbox property="publishToCustomerView"/></td>
	</tr>
</table>
<%
	if(type.equals(SupportConstantKeys.EDIT) || type.equals(SupportConstantKeys.DUPLICATE)) {
%>
<table border="0" cellspacing="0" cellpadding="3">
	<tr>
		<td class="cellContent">
			<app:cvbutton property="savenclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditFAQ('close');">
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="savennew" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditFAQ('new');">
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditFAQ('cancel');">
				<bean:message key="label.cancel"/>
			</app:cvbutton>
			<app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
        <bean:message key="label.support.permissions"/>
			</app:cvbutton>
		</td>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="3"></td>
	</tr>
	<tr>
		<td class="listHeader">
			<a href="javascript:void(0);" class="listHeader" onclick="vl_selectAllRows()"><img src="/centraview/images/icon_check.gif" title="Check All" alt="Check All" border="0"/><bean:message key="label.support.all"/></a>
		</td>
<%
	QuestionList DL = (QuestionList)request.getAttribute("questionlist");
	MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
	Vector vecColumns=(Vector)request.getAttribute("questionColumns");
	for (int i=0;i<vecColumns.size();i++) {
%>
		<td class="listHeader">
			<span class="listHeader"><%=messages.getMessage("QuestionList."+vecColumns.elementAt(i))%></span>
		</td>
<%
	}
%>
	</tr>
<%
	if(DL != null) {
		int i = 0 ;
		if(DL.size() > 0) {
			Set listkey = DL.keySet();
			Iterator iter=listkey.iterator();
			String str = null;

			while(iter.hasNext()) {
%>
	<tr>
<%
				i++;
				str = ( String )iter.next();
				ListElement ele  = ( ListElement )DL.get( str );
				Enumeration enum =  vecColumns.elements();
				String rowClass = null;
				if (i%2 == 0) {
					rowClass = "tableRowEven";
				} else {
					rowClass = "tableRowOdd";
				}
%>
		<td class="<%=rowClass%>" width="7">
			<input type="checkbox" class="checkBox" name="rowId" value="<%=ele.getElementID()%>" onclick="vl_selectRow(this, false);"/>
		</td>
<%
				while ( enum.hasMoreElements()) {
					String strr =  (String) enum.nextElement();
					ListElementMember sm = (ListElementMember)ele.get(strr);
					if (sm != null) {
						if (sm.getLinkEnabled()) {
%>
		<td class="<%=rowClass%>">
			<a href="javascript:<%=sm.getRequestURL()%>" class="plainLink"><%=sm.getDisplayString()%></a>
		</td>
<%
						} else {
%>
		<td class="<%=rowClass%>"><%=sm.getDisplayString()%></td>
<%
						}
					} else {
%>
		<td class="<%=rowClass%>"></td>
<%
					}
				}
%>
	</tr>
<%
			}
		}
	}
%>
	<tr>
		<td colspan="3"><img src="<bean:message key='label.url.images' />/spacer.gif" width="1" height="3"></td>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="3" width="100%">
	<tr>
		<td class="headerBG">
			<input name="button7of9" type="button" class="normalButton" value="<bean:message key='label.value.addquestion'/>" onclick="window.open('<bean:message key='label.url.root' />/support/display_question.do?typeofoperation=ADD&faqid='+window.document.faqform.faqid.value, '','toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=yes,width=380,height=270');">
			<input name="buttonheyheyhey" type="button" class="normalButton" value="<bean:message key='label.value.remove'/>" onClick="return deleteQuestion();">
		</td>
	</tr>
</table>
<%
	} // end if(request.getParameter(Constants.TYPEOFOPERATION).equals(SupportConstantKeys.EDIT))
%>
<table border="0" cellspacing="0" cellpadding="3">
	<tr>
		<td class="cellContent">
			<app:cvbutton property="savenclose" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditFAQ('close');">
				<bean:message key="label.saveandclose"/>
			</app:cvbutton>
			<app:cvbutton property="savennew" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditFAQ('new');">
				<bean:message key="label.saveandnew"/>
			</app:cvbutton>
			<app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditFAQ('cancel');">
				<bean:message key="label.cancel"/>
			</app:cvbutton>
<%
  if(type.equals(SupportConstantKeys.EDIT)) {
%>
			<app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
        <bean:message key="label.support.permissions"/>
			</app:cvbutton>
<% } %>
		</td>
	</tr>
</table>
<html:hidden property="faqid"/>
</html:form>

<script language="javascript">
function gotoPermission() {
	window.open("<bean:message key='label.url.root' />/common/record_permission.do?modulename=FAQ&title="+document.faqform.title.value+"&rowID="+document.faqform.faqid.value, '', 'toolbar=no,location=no,directories=no,status=no, menubar=no,scrollbars=yes, resizable=no,width=550,height=360');
}

function checkAddEditFAQ(closeornew)	{
	var url = "<bean:message key='label.url.root' />/support/save_faq.do?<%=Constants.TYPEOFOPERATION%>=<%=request.getParameter(Constants.TYPEOFOPERATION)%>&closeornew="+closeornew;
	window.document.faqform.action=url;
	window.document.faqform.submit();
	return false;
}

function deleteQuestion() {
	if(checkCheckBoxes()) {
		var url = "<bean:message key='label.url.root' />/support/delete_question.do?<%=Constants.TYPEOFOPERATION%>=<%=request.getParameter(Constants.TYPEOFOPERATION)%>&rowId="+window.document.faqform.faqid.value;
		window.document.faqform.action=url;
		window.document.faqform.submit();
	}
	return false;
}

function checkCheckBoxes() {
	var k = 0;
	for (i=0; i<document.faqform.elements.length; i++) {
		if (document.faqform.elements[i].type=="checkbox" ) {
			k++;
		}
	}
	if(k >= 1) {
		for (i=0 ;i<document.faqform.elements.length;i++) {
			if (document.faqform.elements[i].type=="checkbox") {
				if ((document.faqform.elements[i].checked)) {
					return true;
				}
			}
		}
		alert("<bean:message key='label.alert.selectonerecord'/>.");
		return false;
	} else {
		alert("<bean:message key='label.alert.norecordtodelete'/>");
		return false;
	}
}
</script>
