<%--
/**
 * $RCSfile: mailmerge_preview.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
--%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.struts.action.DynaActionForm" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%
DynaActionForm ptForm = (DynaActionForm) session.getAttribute("printtemplate");
%>
<script>
<!--
function backPage(){
    window.document.printtemplate.action="<bean:message key='label.url.root' />/marketing/mailmerge_detail.do?actionType=back";
		window.document.printtemplate.method="post";
		window.document.printtemplate.target="_self";
		window.document.printtemplate.submit();
}
-->
</script>
<html:form action="/marketing/mailmerge_success.do">
<table border="0" width="100%"  cellpadding="0" cellspacing="0"  class="formTable">
  <tr>
    <td class="contentCell">
     <bean:message key="label.marketing.resultsreview"/>
      .
      <c:if test="${printtemplate.map.mergetype == 'EMAIL'}">
        <b><bean:message key="label.marketing.separateemailforindividuals"/>.</b>
      </c:if>
      <bean:message key="label.marketing.completemerge"/>.
    </td>
	</tr>
	<tr>
	  <td width="162" valign="top">
	    <c:if test="${not empty printtemplate.map.toPrint}">
	      <table width="100%">
            <c:if test="${printtemplate.map.mergetype == 'EMAIL'}">
		    <tr>
          <td width="10%" class="labelCellBold"><strong><bean:message key="label.marketing.from"/>:&nbsp;</strong></td>
          <td width="90%" class="contentCell"><c:out value="${printtemplate.map.accountList[printtemplate.map.templatefrom-1].label}"/></td>
		    </tr>
		    <tr>
          <td width="10%" class="labelCellBold"><bean:message key="label.marketing.to"/>:&nbsp;</td>
          <td width="90%" class="contentCell">
		          <c:set var="firstAddress" value="true"/>
		          <c:forEach var="address" items="${printtemplate.map.emailList}">
		            <c:choose>
		              <c:when test="${firstAddress}">
		                <c:set var="firstAddress" value="false"/>
		              </c:when>
		              <c:otherwise>
		                <c:out value="," escapeXml="false"/>
		              </c:otherwise>
		            </c:choose>
		            <c:out value="${address}"/>
		          </c:forEach>
		      </td>
		    </tr>
				<tr valign="middle">
          <td align="right" class="labelCellBold" valign="top"><bean:message key="label.marketing.attachments"/>:</td>
          <td valign="top" class="contentCell">
					<%
						ArrayList tempAttachmentMap = (ArrayList)ptForm.get("attachmentList");
						if (tempAttachmentMap != null && tempAttachmentMap.size() > 0)
						{
							for (int i=0; i<tempAttachmentMap.size(); i++)
							{
								DDNameValue fileKeyInfo = (DDNameValue)tempAttachmentMap.get(i);
								String fileKeyName = fileKeyInfo.getStrid();
								if (fileKeyName != null)
								{
									int indexOfHash = fileKeyName.indexOf("#");
									if (indexOfHash != -1)
									{
										int lenString = fileKeyName.length();
										String fileID = fileKeyName.substring(0,indexOfHash);
										String fileName = fileKeyName.substring(indexOfHash+1,lenString);
										if (i % 2 != 0){
										%>
                    <a href="<html:rewrite page="/files/file_download.do"/>?fileid=<%=fileID%>&filename=<%=fileName%>" class="plainLink"><%=fileName%></a>, <br/>
										<%
										}
										if (i % 2 == 0){
										%>
                    <a href="<html:rewrite page="/files/file_download.do"/>?fileid=<%=fileID%>&filename=<%=fileName%>" class="plainLink"><%=fileName%></a>,
										<%
										}
									}
								}
							}// end of while (attachIter.hasNext())
						}// end of if (tempAttachmentMap != null && tempAttachmentMap.size() > 0)

					%>
					</td>
				</tr>

		    <tr>
          <td width="10%" class="labelCellBold"><bean:message key="label.marketing.subject"/>:&nbsp;</td>
          <td width="90%" class="contentCell"><c:out value="${printtemplate.map.templatesubject}"/></td>
		    </tr>

		    </c:if>
		    <tr>
          <td width="100%" colspan="2" class="labelCellBold"><bean:message key="label.marketing.message"/>:</td>
            </tr>
            <tr>
              <td width="100%" colspan="2" class="contentCell">
                <p>
                  <c:out value="${printtemplate.map.toPrint[0]}" escapeXml="false"/>
                </p>
		          </td>
            </tr>
          </table>
        </c:if>
      </td>
	</tr>
	<tr>
	  <td width="100%" class="sectionHeader" valign="top" align="left">
        <html:button property="cancelButton" styleClass="normalButton" onclick="javascript:backPage();">
	      &laquo;&nbsp;<bean:message key="label.marketing.back"/>
	    </html:button>
	    <c:choose>
	      <c:when test="${printtemplate.map.mergetype == 'EMAIL'}">
	        <html:submit styleClass="normalButton">
	          <bean:message key="label.marketing.send"/>&nbsp;&raquo;
	        </html:submit>
	      </c:when>
	      <c:otherwise>
	        <html:submit styleClass="normalButton">
	          <bean:message key="label.marketing.next"/>&nbsp;&raquo;
	        </html:submit>
	      </c:otherwise>
	    </c:choose>
     </td>
  </tr>
</table>
</html:form>