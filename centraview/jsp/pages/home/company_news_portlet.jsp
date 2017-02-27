<%--
 * $RCSfile: company_news_portlet.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ page import="com.centraview.common.CVUtility" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<!-- Starts Company News  -->
<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader"><bean:message key="label.home.companynews"/></td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
<%
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource();
  com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
  java.util.ArrayList companyNewsList = lg.getCompanyNews();
  com.centraview.file.CompanyNewsVO companyNewsVO = new com.centraview.file.CompanyNewsVO();
  int length = companyNewsList.size();
  if (length != 0) {
    String rowClass = "tableRowOdd";
    int count = 0;
    for (int i=0; i<length; i++) {
      count++;
      rowClass = (count % 2 == 0) ? "tableRowEven" : "tableRowOdd";
      companyNewsVO = (com.centraview.file.CompanyNewsVO)companyNewsList.get(i);
      %>
      <tr>
        <td class="<%=rowClass%>">
        <b><%=companyNewsVO.getTitle() %></b>
        <a name="location" href="<html:rewrite page="/files/file_download.do"/>?fileid=<%=companyNewsVO.getFileID()%>" class="plainLink"><bean:message key="label.home.more"/>...</a><br>
        <%=companyNewsVO.getDescription()%><p>
        </td>
      </tr>
      <%
    }
  }else{
    %>
    <tr>
      <td class="tableRowOdd"><bean:message key="label.home.nocompanynews"/>.</td>
    </tr>
    <%
  }
%>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>