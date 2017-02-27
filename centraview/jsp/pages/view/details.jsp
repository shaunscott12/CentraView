<%--
 * $RCSfile: details.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:57 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import = "java.util.Vector"%>
<%@ page import = "java.util.ArrayList"%>
<%@ page import = "java.util.Collection"%>
<%@ page import = "com.centraview.common.ViewForm"%>
<%@ page import = "com.centraview.common.DDNameValue"%>
<%@ page import = "com.centraview.common.* ,java.util.*"%>
<%@ page import = "com.centraview.advancedsearch.*,org.apache.struts.util.LabelValueBean"%>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>

<%
  ViewForm viewForm = (ViewForm)request.getAttribute("viewform");
  UserObject userobject = (UserObject)session.getAttribute("userobject");
  int individualId = userobject.getIndividualID();
  String currentPage = request.getParameter("currentPage");
  String stringTypeOfOperation = request.getAttribute("TYPEOFOPERATION").toString();
%>

<html:errors/>
<html:form action="/common/new_view">
              <table border="0" cellspacing="0" cellpadding="0" height="100%" width=100%"">
                  <td width="11"><html:img page="/images/spacer.gif" height="8" width="11" /></td>
                  <td><span class="boldtext"><bean:message key="label.view.viewname"/>:&nbsp;</span><span class="plainText"><html:text property="viewName" styleClass="inputBox"/></span></td>
                  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                </tr>
                <tr height="7"><td colspan="3"><html:img page="/images/spacer.gif" height="8" width="11" /></td></tr>
                <tr>
                  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                  <td>
                    <table border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><span class="boldText"><bean:message key="label.view.availablefields"/></td>
                        <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                        <td>&nbsp;</td>
                        <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                        <td><span class="boldText"><bean:message key="label.view.selectedfields"/></td>
                          <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td>
                        <%
                          Vector availableColumnVec =  (Vector)viewForm.getAvailableColumnVec();
                          if (availableColumnVec == null)
                            availableColumnVec = new Vector();
                          pageContext.setAttribute("availableColumnVec", availableColumnVec, PageContext.PAGE_SCOPE);
                        %>
                        <html:select property="availableColumn" size="8" styleClass="inputBox" style="height:17em;width:150px;" multiple="true">
                          <html:options collection="availableColumnVec" property="strid" labelProperty="name"/>
                        </html:select>
                        </td>
                        <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                        <td>
                          <html:button property="cmpadd" styleClass="normalButton" onclick="add()">
                            <bean:message key="label.add"/>
                          </html:button>
                          <br>
                          <html:button property="cmpremove" styleClass="normalButton" onclick="remove()">
                            <bean:message key="label.remove"/>
                          </html:button>
                        </td>
                        <td width="5"><html:img page="/images/spacer.gif" height="1" width="5" /></td>
                        <td>
                        <%
                          Vector selectedColumnVec =  (Vector)viewForm.getSelectedColumnVec();
                          if (selectedColumnVec == null)
                            selectedColumnVec = new Vector();
                          pageContext.setAttribute("selectedColumnVec", selectedColumnVec, PageContext.PAGE_SCOPE);
                        %>
                        <html:select property="selectedColumn" size="8" styleClass="inputBox" style="height:17em;width:150px;" multiple="true" value="">
                          <html:options collection="selectedColumnVec" property="strid" labelProperty="name"/>
                        </html:select>
                        </td>
                        <td>
                          <html:button property="cmpmoveup" styleClass="normalButton" onclick="moveup()">
                            <bean:message key="label.moveup"/>
                          </html:button>
                          <br>
                          <html:button property="cmpmovedown" styleClass="normalButton" onclick="movedown()">
                            <bean:message key="label.movedown"/>
                          </html:button>
                        </td>
                      </tr>
                    </table>
                  </td>
                  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                </tr>
                <tr height="3"><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="3" /></td></tr>
                <tr>
                  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                  <td><p class="pageInstructions"><bean:message key="label.view.deselectinstructions"/>.</p></td>
                  <td width="11"><html:img page="/images/spacer.gif" height="1" width="11" /></td>
                </tr>
                <tr><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
                <tr height="11">
                  <td>&nbsp;</td>
                  <td nowrap><span class="boldText"><bean:message key="label.view.defaultsort"/></span>
                  <%
                    Vector sortMemberVec =  (Vector)viewForm.getSortMemberVec();
                    if (sortMemberVec == null)
                      sortMemberVec = new Vector();
                    pageContext.setAttribute("sortMemberVec", sortMemberVec, PageContext.PAGE_SCOPE);
                  %>
                  <html:select property="sortMember" styleClass="inputBox">
                    <html:options collection="sortMemberVec" property="strid" labelProperty="name"/>
                  </html:select>
                  &nbsp;&nbsp;<span class="boldText">
                  <html:radio property="sortType" value="A"/>
                  <bean:message key="label.ascending"/>
                  <html:radio property="sortType" value="D"/>
                  <bean:message key="label.descending"/></span> </td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr height="11">
                      <td>&nbsp;</td>
                      <td nowrap><span class="boldText"><bean:message key="label.view.numberofrecords"/></span>
                      <%
                      Collection noOfRecord = new ArrayList();
                      noOfRecord.add(new DDNameValue("10","10"));
                      noOfRecord.add(new DDNameValue("20","20"));
                      noOfRecord.add(new DDNameValue("50","50"));
                      noOfRecord.add(new DDNameValue("100","100"));
                      noOfRecord.add(new DDNameValue("0","All"));
                      pageContext.setAttribute("noOfRecord", noOfRecord, PageContext.PAGE_SCOPE);
                      %>
                      <html:select property="noOfRecord" styleClass="inputBox" >
                        <html:options collection="noOfRecord" property="strid" labelProperty="name"/>
                      </html:select>
                &nbsp;&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
                <tr height="7"><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
                <tr>
                  <td class="buttonRow" colspan="3">
                  <html:button property="cmpnewview" styleClass="normalButton" onclick="saveview('new')">
                    <bean:message key="label.view.newview"/>
                  </html:button>
                  <html:button property="cmpsaveview" styleClass="normalButton" onclick="saveview('saveview')">
                    <bean:message key="label.view.saveview"/>
                  </html:button>
                  <html:button property="cmpsavenew" styleClass="normalButton" onclick="saveview('savenewview')">
                    <bean:message key="label.saveandnew"/>
                  </html:button>
                  <%-- remove save and apply temporarily
                  <html:button property="cmpsaveapply" styleClass="normalButton" onclick="saveview('saveapply')">
                    <bean:message key="label.view.saveapply"/>
                  </html:button>
                  --%>
                  <% if (!(stringTypeOfOperation != null && stringTypeOfOperation.length() > 0 && stringTypeOfOperation.equals("ADD"))) { %>
                  <html:button property="cmpdelete" styleClass="normalButton" onclick="deleteView()">
                    <bean:message key="label.delete"/>
                  </html:button>
                  <% } %>
                  <html:reset styleClass="normalButton">
                    <bean:message key="label.resetfields"/>
                  </html:reset>
                  <html:button property="cancel" styleClass="normalButton" onclick="return returnToList()">
                    <bean:message key="label.cancel"/>
                  </html:button>
                 </td>
                </tr>
                <tr height="7"><td colspan="3"><html:img page="/images/spacer.gif" width="1" height="7" /></td></tr>
                <tr height="100%">
                  <td colspan="3" height="100%"><html:img page="/images/spacer.gif" height="1" width="11" />
                  <input type="hidden" name="currentPage" value="<%=currentPage%>"/>
                  </td></tr>
              </table>
<html:hidden property="TYPEOFOPERATION" value="<%=request.getAttribute("TYPEOFOPERATION").toString()%>"/>
<html:hidden property="viewId"/>
<html:hidden property="listType" value="<%=request.getParameter("listType").toString()%>"/>
<html:hidden property="defaultSystemView"/>
<script language="javascript">
  function saveview(typeOfSave)    {

    if (typeOfSave == "new")
      document.viewform.action="<bean:message key='label.url.root' />/common/new_view.do";
    else {
      if (document.viewform.defaultSystemView.value == "true")  {
        alert ("<bean:message key='label.alert.cannoteditdefaultview'/>");
        return false;
      }
      document.viewform.action="<bean:message key='label.url.root' />/common/save_view.do?resetForm=true&savetype=" + typeOfSave;
    }

    var checkSelectFieldListBox = isSFListBoxEmpty();
    if (checkSelectFieldListBox == false && typeOfSave != "new")  {
      alert("<bean:message key='label.alert.selectedfieldsnottobeempty'/>");
      return false;
    }

    var sortMemberCheck = checkSortMember();
    if (sortMemberCheck == false && typeOfSave != "new")  {
      alert("<bean:message key='label.alert.defaultsortdoesnotexist'/>");
      return false;
    }
    selectAllSelectedColumn();
    document.viewform.submit();
  }

  function isSFListBoxEmpty()  {
    var lengthRequired = window.document.viewform.selectedColumn.options.length;
    if (lengthRequired > 0)
      return true;
    else
      return false;
  }

  function checkSortMember()  {
    var selectedSortMember = window.document.viewform.sortMember[window.document.viewform.sortMember.selectedIndex].text;
    var lengthRequired = window.document.viewform.selectedColumn.options.length;
    for (var i=0;i<lengthRequired;i++)  {
      if (selectedSortMember == window.document.viewform.selectedColumn.options[i].text)
        return true;
    }
    return false;
  }

  function selectAllSelectedColumn()  {
    if (window.document.viewform.selectedColumn != null)  {
      var lengthRequired = window.document.viewform.selectedColumn.options.length;
      for (var i=0;i<lengthRequired;i++)  {
        window.document.viewform.selectedColumn.options[i].selected=true;
      }
    }
  }

  function add()
  {
    var n = document.viewform.availableColumn.options.length;
    var m = document.viewform.selectedColumn.options.length;
    if(document.viewform.availableColumn.selectedIndex == -1)
    {
      alert("<bean:message key='label.alert.selectaddtag'/>");
    }
    else
    {
      i=0;
      while(i<n)
      {
        if(document.viewform.availableColumn.options[i].selected)
        {

          document.viewform.selectedColumn.options[m] = new Option(document.viewform.availableColumn.options[i].text,document.viewform.availableColumn.options[i].value,"false","false");
          document.viewform.availableColumn.options[i] = null;
          m = m+1;
          n = n - 1;
          i=0;
        }
        else
          i++;
      }
    }
  }

  function remove()
  {
    var m = document.viewform.availableColumn.options.length;
    var n = document.viewform.selectedColumn.options.length;

    if (document.viewform.selectedColumn.selectedIndex == -1)
    {
      alert("<bean:message key='label.alert.selectremovetag'/>");
    }
    else
    {
      i=0;
      while(i<n)
      {
        if(document.viewform.selectedColumn.options[i].selected)
        {
          document.viewform.availableColumn.options[m] = new Option(document.viewform.selectedColumn.options[i].text,document.viewform.selectedColumn.options[i].value,"false","false");
          m = m+1;
        }
        i++;
      }
      i = 0;
      while (i < n)  {
        if(document.viewform.selectedColumn.options[i].selected)  {
          document.viewform.selectedColumn.options[i] = null;
          n = n - 1;
        }
        else
          i++;
      }
    }
  }

  function moveup()
  {
    var n = document.viewform.selectedColumn.options.length;
    if (document.viewform.selectedColumn.selectedIndex == -1)
    {
      alert("<bean:message key='label.alert.selectmoveuptag'/>");
    }
    else
    {
      var index = parseInt(document.viewform.selectedColumn.selectedIndex);
      if (index != 0){
        var previousIndex = parseInt(index-1);
        var previousValue = document.viewform.selectedColumn.options[previousIndex].text;
        var currentValue = document.viewform.selectedColumn.options[index].text;
        var previousID = document.viewform.selectedColumn.options[previousIndex].value;
        var currentID = document.viewform.selectedColumn.options[index].value;
        document.viewform.selectedColumn.options[previousIndex] = new Option(currentValue,currentID,"false","false");
        document.viewform.selectedColumn.options[index] = new Option(previousValue,previousID,"false","false");
        document.viewform.selectedColumn.options[index].selected = false;
      }
    }
  }

  function movedown()
  {
    var n = document.viewform.selectedColumn.options.length;
    if (document.viewform.selectedColumn.selectedIndex == -1)
    {
      alert("<bean:message key='label.alert.selectmoveuptag'/>");
    }
    else
    {
      var index = parseInt(document.viewform.selectedColumn.selectedIndex);
      if (index < (n-1)) {
        var nextIndex = parseInt(index+1);
        var nextValue = document.viewform.selectedColumn.options[nextIndex].text;
        var currentValue = document.viewform.selectedColumn.options[index].text;
        var nextID = document.viewform.selectedColumn.options[nextIndex].value;
        var currentID = document.viewform.selectedColumn.options[index].value;
        document.viewform.selectedColumn.options[nextIndex] = new Option(currentValue,currentID,"false","false");
        document.viewform.selectedColumn.options[index] = new Option(nextValue,nextID,"false","false");
        document.viewform.selectedColumn.options[index].selected = false;
      }
    }
  }

  function returnToList()
  {
      <%
      if(request.getAttribute("pageinfo") == null){
      %>
      listId = <%=request.getParameter("listId")%>;
      document.viewform.action = "<bean:message key='label.url.root' />/DisplayUserlist.do?listId=" + listId;
      document.viewform.submit();
    <%
    }
    else{
    %>
      goTo('<%=request.getContextPath()%><%=request.getAttribute("pageinfo")%>');
      return false;
    <%
    }
    %>

  }

  function deleteView()    {
    if (document.viewform.defaultSystemView.value == "true")  {
      alert ("<bean:message key='label.alert.cannotdeletedefaultview'/>");
      return false;
    }
    else if (document.viewform.viewId.value == "")  {
      alert ("<bean:message key='label.alert.cannotdeletenewview'/>");
      return false;
    }
    else {
      listId = document.viewform.listId.value;
      document.viewform.action = "<bean:message key='label.url.root' />/DeleteUserView.do?listId=" + listId;
      document.viewform.submit();
      return false;
    }
  }

</script>

</html:form>
