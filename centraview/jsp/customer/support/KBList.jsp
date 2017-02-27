<%--
 * $RCSfile: KBList.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:42 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import = "com.centraview.common.* ,java.util.*,org.apache.struts.util.MessageResources"%>
<%! public MessageResources messages = MessageResources.getMessageResources("ApplicationResources"); %>
<%

  UserObject userobject = (UserObject)session.getAttribute("userobject");
  DisplayList displaylist = (DisplayList)request.getAttribute("displaylist");

  boolean showCheckbox = (request.getAttribute("showCheckbox") == null) ? true : ((Boolean)request.getAttribute("showCheckbox")).booleanValue();

  boolean showDocumentIcon = (request.getAttribute("showDocumentIcon") == null) ? true : ((Boolean)request.getAttribute("showDocumentIcon")).booleanValue();

  String rowID = (String) request.getAttribute("rowId");

  if (userobject != null && displaylist!= null)
  {
    ListPreference listpreference = userobject.getListPreference(displaylist.getListType());
    String sortElement = displaylist.getSortMember();
    int recordsperpage = listpreference.getRecordsPerPage();
    ListView listview = null;

    if (listpreference.getDefaultView() > 0)
    {
      listview = listpreference.getListView(""+listpreference.getDefaultView());
    }else{
      listview = listpreference.getListView("1");
    }

    Vector listcolumns =  listview.getColumns();
    Enumeration listcolumns_enumeration = listcolumns.elements();
    HashMap clomnmapwidth = displaylist.getColumnMap();
    %>
    <form name="listrenderer" method="post">
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
      <tr height="1">
        <td class="leftPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
        <td class="pagingTableHeadShadow" colspan="<%=listcolumns.size()+3%>"><html:img page="/images/spacer.gif" height="1" /></td>
        <td class="rightPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
      </tr>
      <tr height="1">
        <td class="leftPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
        <td class="pagingTableHeadShadowLt" colspan="<%=listcolumns.size()+3%>"><html:img page="/images/spacer.gif" height="1" /></td>
        <td class="rightPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
      </tr>
      <tr>
        <td class="leftPagingBorder" width="4"><html:img page="/images/spacer.gif" height="1" width="4" /></td >
        <td width="1" class="pagingTableHeadText">&nbsp;</td>
        <td class="pagingTableHeadText"><html:img page="/images/spacer.gif" height="1" width="20" /></td>
        <%
          int counter = 0;
          String listcolumnName = null;

          while (listcolumns_enumeration.hasMoreElements())
          {
            counter++;
            if (counter == 2)
            {
              listcolumnName = "Entity";
            }else{
              listcolumnName = (String)listcolumns_enumeration.nextElement();
            }

            if (rowID != null)
            {
              %>
              <td class="pagingTableHeadText" nowrap><a class="pagingTableHeadText" href="<bean:message key='label.url.root' />/customer/SortHandler.do?listId=<%=displaylist.getListID()%>&columnname=<%=listcolumnName%>&rowId=<%=rowID%>"><%= messages.getMessage( displaylist.getListType()+"List."+listcolumnName ) %><html:img page="/images/spacer.gif" width="2" height="1" border="0" />
              <%
            }else{
              %>
              <td class="pagingTableHeadText" nowrap><a class="pagingTableHeadText" href="<bean:message key='label.url.root' />/customer/SortHandler.do?listId=<%=displaylist.getListID()%>&columnname=<%=listcolumnName%>"><%= messages.getMessage( displaylist.getListType()+"List."+listcolumnName ) %><html:img page="/images/spacer.gif" width="2" height="1" border="0" />
              <%
            }

            if (sortElement.equals(listcolumnName))
            {
              char sortType = displaylist.getSortType();
              if (sortType == 'A')
              {
                %><html:img page="/images/icon_sort_ascending.gif" width="7" height="4" border="0" alt="Sort Ascending" /><%
              }else{
                %><html:img page="/images/icon_sort_descending.gif" width="7" height="4" border="0" alt="Sort Descending" /><%
              }
            }

            %></a></td><%

          }   // end while (listcolumns_enumeration.hasMoreElements())
        %>
        <td class="rightPagingBorder" width="4"><html:img page="/images/spacer.gif" height="1" width="4" /></td>
      </tr>
      <tr height="1">
        <td class="leftPagingBorder"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
        <td class="pagingTableHeadBottom" colspan="<%=listcolumns.size()+3%>"><html:img page="/images/spacer.gif" height="1" /></td>
        <td class="rightPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
      </tr>
      <%
        int i = 0 ;

        if (displaylist.size() > 0)
        {
          Set listkey = displaylist.keySet();

          int startAt = displaylist.getStartAT();
          int endAt = displaylist.getEndAT();
          int beginindexAt = displaylist.getBeginIndex();
          int endindexAt = displaylist.getEndIndex();

          String str = null;
          Iterator it =  listkey.iterator();

          counter = 0;

          while (it.hasNext())
          {
            i++;
            str = (String)it.next();
            ListElement ele = (ListElement)displaylist.get(str);
            Enumeration enum =  listcolumns.elements();
            String requestURL = ((ListElementMember)ele.get(displaylist.getPrimaryMember())).getRequestURL();
            String oddOrEven = new String();    // string used for printing CSS class on odd/even rows

            if (i >= startAt && i <= endAt)
            {
              if (i % 2 == 0)
              {
                oddOrEven = "Even";
              }else{
                oddOrEven = "Odd";
              }

              String checkBoxClass = "contactTableCheck";  // CSS class for checkbox cell
              if (i == displaylist.size() || i == endAt)
              {
                // if this is the last row in the list (displaylist.size() is the last row in the result set,
                // endAt is the last row on the page), the CSS class should be:
                checkBoxClass = "contactTableCheckBottom";
              }

              %>
              <tr class="pagingTableRow<%=oddOrEven%>">
                <td class="leftPagingBorder"><html:img page="/images/spacer.gif" height="1" width="4" /></td>
                <%
                  if (showCheckbox)
                  {
                    %><td class="<%=checkBoxClass%>"><input type="hidden" name="rowId" value="<%=ele.getElementID()%>" onClick="selectRow(this, true);selectAll(this, false);"></td><%
                  }else{
                    %><td width = "20" class="pagingTableIcon<%=oddOrEven%>"><html:img page="/images/spacer.gif" width="1" height="1"  /></td><%
                  }


                  if (showDocumentIcon)
                  {
											ListElementMember sm1 = ( ListElementMember )ele.get( "CatKB" ) ;

											if ((sm1.getDisplayString()).equals("KBELEMENT"))
											{
                    %>
	                    <td width = "20" class="pagingTableIcon<%=oddOrEven%>"><html:img page="/images/spacer.gif" width="1" height="1"  /><a href="<%=requestURL%>"><html:img page="/images/icon_file.gif" border="0" alt="View Details" width="12" height="14" /></a></td>
										<% }else if ((sm1.getDisplayString()).equals("CATEGORY")){%>
	                    <td width = "20" class="pagingTableIcon<%=oddOrEven%>"><html:img page="/images/spacer.gif" width="1" height="1"  /><a href="<%=requestURL%>"><html:img page="/images/icon_folder.gif" border="0" alt="View Details" width="12" height="14" /></a></td>
                    <%
                    	}
                  }else{
                    %><td width = "20" class="pagingTableIcon<%=oddOrEven%>"><html:img page="/images/spacer.gif" width="1" height="1"  /></td><%
                  }

                  int columnCounter = 0;    // current counter of the column list/no recor

                  while (enum.hasMoreElements())
                  {
                    counter++;
                    if (counter == 2)
                    {
                      %>
                      <td class="pagingTable<%=oddOrEven%>"><%=userobject.getEntityName()%>&nbsp;</td>
                      <%
                      columnCounter--;
                      continue;
                    }

                    columnCounter++;
                    String strr = (String)enum.nextElement();
                    ListElementMember sm = (ListElementMember)ele.get(strr);
                    String endClass = "";     // String appended to the end of the CSS class

                    if (columnCounter == listcolumns.size())
                    {
                      // if this is the last column in the row, append "End" to the CSS class
                      endClass = "End";
                    }

                    if (sm != null)
                    {
                      if (sm.getLinkEnabled())
                      {
                        %><td class="pagingTable<%=oddOrEven%><%=endClass%>"><a href="<%=sm.getRequestURL()%>" class="popupLink"><%=sm.getDisplayString()%></a>&nbsp;</td><%
                      }else{
                        %><td class="pagingTable<%=oddOrEven%><%=endClass%>"><%=sm.getDisplayString()%>&nbsp;</td><%
                      }
                    }else{
                      %><td class="pagingTable<%=oddOrEven%><%=endClass%>" nowrap>&nbsp;</td><%
                    }
                  }   // end while (enum.hasMoreElements())
                  counter = 0;
                %>
                <td class="rightPagingBorder"><html:img page="/images/spacer.gif" height="1" width="4" /></td>
              </tr>
              <%
            }   // end if (i >= startAt && i <= endAt)
          }   // end while (it.hasNext())
        }else{
          // no records found
          %>
          <tr class="pagingTableRowOdd">
            <td class="leftPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
            <td class="pagingTableOddEnd" colspan="<%=listcolumns.size()+3%>"><html:img page="/images/spacer.gif" height="1" /><center><bean:message key="label.customer.support.norecordsfound"/></center></td>
            <td class="rightPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
          </tr>
          <%
        }   // end if (displaylist.size() > 0)
      %>
      <tr height="1">
        <td class="leftPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
        <td class="pagingTableHeadBottom" colspan="<%=listcolumns.size()+3%>"><html:img page="/images/spacer.gif" height="1" /></td>
        <td class="rightPagingBorder" width="4"><html:img page="/images/spacer.gif" width="4" height="1" /></td>
      </tr>
    </table>
    <input type=hidden name="listType" value="<%= displaylist.getListType() %>">
    <input type=hidden name="listId" value="<%=displaylist.getListID()%>">
    <input type=hidden name="recordperpage" value="10">
    <input type=hidden name="noofrecord" value="<%=i%>">
    </form>
    <%
  }   // end if (userobject != null && displaylist!= null)
%>