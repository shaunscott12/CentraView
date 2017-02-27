<%
/*
 * $RCSfile: literature_lookup.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<script language="javascript">
	function submitData()
	{
		i=0
		var strCheck="";
		var forwardValue = "";
		var forwardId = "";


		var len = document.literaturelookup.literaturename.length;

		if (len == undefined) {
			if(document.literaturelookup.literaturename.checked)
			{
				var literaturename = window.document.literaturelookup.literaturename.value;
				if (literaturename != "")
					x = literaturename.indexOf("#");
				var id = literaturename.substring(0,x);
				var value = literaturename.substring(x+1,literaturename.length);

				if (strCheck=="")
				{
					forwardValue = value + ",";
					forwardId = id + ",";
					strCheck="checked";
				}
				else
				{
					forwardValue = forwardValue +value + ",";
					forwardId = forwardId + id + ",";
				}
				i++;
			}
			else
				i++;
		}
		else {

			while(i<document.literaturelookup.literaturename.length)
			{
				if(document.literaturelookup.literaturename[i].checked)
				{
					var literaturename = window.document.literaturelookup.literaturename[i].value;
					if (literaturename != "")
						x = literaturename.indexOf("#");
					var id = literaturename.substring(0,x);
					var value = literaturename.substring(x+1,literaturename.length);

					if (strCheck=="")
					{
						forwardValue = value + ",";
						forwardId = id + ",";
						strCheck="checked";
					}
					else
					{
						forwardValue = forwardValue +value + ",";
						forwardId = forwardId + id + ",";
					}
					i++;
				}
				else
					i++;
			 }
		}
		window.opener.setData(forwardId,forwardValue);
		window.close();
	}

  function searchList()
	{
    document.literaturelookup.action = "<%=request.getContextPath()%>/marketing/literature_lookup.do";
		document.literaturelookup.submit();
	}
</script>

<form name="literaturelookup" action="/marketing/literature_lookup.do">
  <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
      <td class="popupTableContent">
        <input id="searchTextBox" name="searchTextBox" type="text" class="inputBox" value="">
        <input value="<bean:message key='label.marketing.search'/>" type="button" class="normalButton"  onclick="searchList()"/>
      </td>
    </tr>
  </table>
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
      <td class="pagingBarContainer" colspan="2">
        <html:button property="cmpselect" styleClass="normalButton" onclick="submitData()">
          <bean:message key="label.select"/>
        </html:button>
      </td>
    </tr>
    <tr>
      <td class="listHeader" width="14">&nbsp;</td>
      <td class="listHeader">
        <bean:message key="label.marketing.literaturename"/>
      </td>
    </tr>
    <c:set var="count" value="0" />
    <c:set var="classTD" value="tableRowOdd" />
    <c:forEach var="literature" items="${requestScope.literaturelist}">
      <c:if test="${count % 2 == 0}">
        <c:set var="classTD" value="tableRowEven" />
      </c:if>
      <c:if test="${count % 2 != 0}">
        <c:set var="classTD" value="tableRowOdd" />
      </c:if>
      <tr>
        <td class="<c:out value='${classTD}'/>">
          <c:set var="checkedValue" value="" />
          <c:forEach var="literatureIDList" items="${requestScope.literatureIDList}">
            <c:if test="${literatureIDList == literature.id}">
              <c:set var="checkedValue" value="checked" />
            </c:if>
          </c:forEach>
          <input type="checkbox" name="literaturename" value="<c:out value="${literature.id}#${literature.name}" />" <c:out value="${checkedValue}"/> >
        </td>
        <td class="<c:out value='${classTD}'/>">
          <a class="plainLink" href="javascript:void();" ><c:out value="${literature.name}" /></a>
        </td>
      </tr>
      <c:set var="count" value="${count + 1}" />
    </c:forEach>
    <tr>
      <td class="pagingBarContainer" colspan="2">
        <html:button property="cmpselect" styleClass="normalButton" onclick="submitData()">
          <bean:message key="label.select"/>
        </html:button>
      </td>
    </tr>
  </table>
  <input type="hidden" name="literatureid" value="<c:out value='${param.literatureid}'/>" />
</form>