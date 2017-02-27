<%--
* $RCSfile: item_list.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.common.*,com.centraview.contact.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Vector" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import="com.centraview.contact.helper.*" %>
<%@ page import="com.centraview.marketing.AccountingLines" %>
<%@ page import="com.centraview.marketing.ItemLines" %>
<%@ page import="com.centraview.marketing.*" %>
<script language="javascript1.2">
<!--
  function fnItemLookup()
  {
    var hidden="";
    var str="";
    var k=0;
    for (i=0 ;i<document.promotionlistform.elements.length;i++)
    {
      if (document.promotionlistform.elements[i].type=="checkbox")
      {
        k++;
      }
    }
    if(k>1)
    {
      for(i =0 ;i<document.promotionlistform.checkbox.length;i++)
      {
        hidden=document.promotionlistform.checkbox[i].value + ",";
        str=str+hidden;
      }
      str=(str.substring(0,str.length-1));
    }
    else if(k==1)
    {
      hidden=document.promotionlistform.checkbox.value + ",";
      str=str+hidden;
      str=(str.substring(0,str.length-1));
    }
    c_lookup('Item',str);
  }

  function lookupCheckBox()
  {
    // function called for remove operation
    var hidden="";
    var str="";
    var k=0;
    if( document.promotionlistform.checkbox == null)
    {
      alert("<bean:message key='label.alert.noitemsavailable'/> ");
      return;
    }
    for (i=0 ;i<document.promotionlistform.elements.length;i++)
    {
      if (document.promotionlistform.elements[i].type=="checkbox")
      {
        k++;
      }
    }
    if(k>1)
    {
      for(i =0 ;i<document.promotionlistform.checkbox.length;i++)
      {
        if ( document.promotionlistform.checkbox[i].checked)
        {
          hidden=document.promotionlistform.checkbox[i].value + ",";
          str=str+hidden;
        }
      }
      str=(str.substring(0,str.length-1));
    }else if (k==1)
    {
      hidden=document.promotionlistform.checkbox.value + ",";
      str=str+hidden;
      str=(str.substring(0,str.length-1));
    }
    if(!checkCheckbox())
    {
      alert("<bean:message key='label.alert.selectrecord'/>");
      return false;
    }
    flag = false;
    lookupValues = {IDs: "", RemoveIDs: str, Flag: false}
    setItem(lookupValues);
  }

  function checkCheckbox()
  {
    if ( document.promotionlistform.checkbox.length )
    {
      for(i =0 ;i<document.promotionlistform.checkbox.length;i++)
      {
        if ( document.promotionlistform.checkbox[i].checked)
        {
          return true;
        }
      }
    }
    else
    {
      return document.promotionlistform.checkbox.checked;
    }
  }
-->
</script>
<%
ItemLines al = (ItemLines)request.getAttribute("ItemLines");
if(al == null){
  al = new ItemLines();
}

Vector listcolumns =  al.getViewVector();
Enumeration listcolumns_enumeration = listcolumns.elements();
Set listkey = al.keySet();
Iterator it =  listkey.iterator();
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
  <tr>
      <td class="listHeader">
        <a class="listHeader" href="javascript:void(0);" onClick="vl_selectAllRows();">
          <html:img page="/images/icon_check.gif" width="8" height="8" border="0"/>
        </a>
        <a class="listHeader" href="javascript:void(0);" onClick="vl_selectAllRows();">All</a>
      </td>
      <%
      while(listcolumns_enumeration.hasMoreElements())
      {
        String listcolumnName = (String)listcolumns_enumeration.nextElement();
        %>
        <td class="listHeader">
          <span class="listHeader">
            <%=listcolumnName%>
          </span>
        </td>
        <%
      }//end while
      %>
  </tr>
  <%
  if(!it.hasNext()){
  %>
    <tr>
      <td class="tableRowOdd" colspan="7" align="center"><bean:message key="label.marketing.noitemsfound"/></td>
    </tr>
  <%
  }
  int i=1;
  while (it.hasNext())
  {
    Enumeration enum =  listcolumns.elements();
    ItemElement ele  = (ItemElement)al.get(it.next());
    String lineStatus = ele.getLineStatus();

    if(lineStatus == null)
      lineStatus = "";

    IntMember lineid = (IntMember)ele.get("LineId");
    IntMember itemid = (IntMember)ele.get("ItemId");
    StringMember sku = (StringMember)ele.get("SKU");
    IntMember valueQuantity = (IntMember)ele.get("Value");
    StringMember rule = (StringMember)ele.get("Type");
    FloatMember price = (FloatMember)ele.get("DiscountedPrice");
    FloatMember listprice = (FloatMember)ele.get("ListPrice");
    FloatMember cost = (FloatMember)ele.get("Cost");
    StringMember desc = (StringMember)ele.get("Description");

    String cellclassstr;
    String tableclasstr;

    if(!lineStatus.equals("Deleted"))
    {
        if(i%2 == 0)
        {
          cellclassstr="tableRowEven";
          tableclasstr="tableRowEven";
        }
        else
        {
          cellclassstr="tableRowOdd";
          tableclasstr="tableRowOdd";
        }
      %>
      <tr class="<%=tableclasstr%>">
        <td class ="<%=cellclassstr%>">
          <input type="checkbox" name="checkbox" value="<%=itemid.getDisplayString()%>">
        </td>
        <td class ="<%=cellclassstr%>">
          <%=sku.getDisplayString()%>
        </td>
        <td class ="<%=cellclassstr%>">
          <html:text property="description" styleClass="inputBox" value="<%=desc.getDisplayString()%>" size="50"/>
        </td>
        <td class="<%=cellclassstr%>">
           <%=listprice.getDisplayString()%>
        </td>
        <td class="<%=cellclassstr%>">
            <%=cost.getDisplayString()%>
        </td>
        <td class="<%=cellclassstr%>">
           <html:text property="value" styleClass="inputBox" value="<%=valueQuantity.getDisplayString()%>" size="6"/>
        </td>
        <td class="<%=cellclassstr%>">
            <%=price.getDisplayString()%>
        </td>
      </tr>
    <%
    } else {
    %>
      <input type="hidden" name="description" value="<%=desc.getDisplayString()%>">
      <input type="hidden" name="type" value="<%=rule.getDisplayString()%>">
      <input type="hidden" name="value" value="<%=valueQuantity.getDisplayString()%>">
      <input type="hidden" name="discountedprice" value="<%=price.getDisplayString()%>">
    <%
    }
    %>
    <input type="hidden" name="lineid" value="<%=lineid.getMemberValue()%>">
    <input type="hidden" name="itemid" value="<%=itemid.getMemberValue()%>">
    <input type="hidden" name="sku" value="<%=sku.getDisplayString()%>">
    <input type="hidden" name="priceExtended" value=<%=price.getMemberValue()%>>
    <input type="hidden" name="unittax" value="0">
    <input type="hidden" name="taxrate" value="0">
    <input type="hidden" name="orderquantity" value="0">
    <input type="hidden" name="pendingquantity" value="0">
    <input type="hidden" name="linestatus" value="<%=ele.getLineStatus()%>">
    <input type="hidden" name="listprice" value="0">
    <input type="hidden" name="cost" value="0">
    <%
    if(!lineStatus.equals("Deleted"))
    {
       i++;
    }
  } //end of while
  %>
  <tr>
    <td colspan="6">
      <app:cvbutton property="additem" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" style="width:7em;" onclick="fnItemLookup()">
        <bean:message key="label.marketing.additem"/>
      </app:cvbutton>
      <app:cvbutton property="removeitem" styleClass="normalButton" style="width:11em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="lookupCheckBox()">
        <bean:message key="label.marketing.removeitem"/>
      </app:cvbutton>
      <app:cvbutton property="save" styleClass="normalButton" style="width:9em;" statuswindow="label.general.blank" tooltip="false" statuswindowarg= "label.general.blank" onclick="gotosave('save');">
        <bean:message key="label.marketing.savechanges"/>
      </app:cvbutton>
    </td>
  </tr>
  <tr>
    <td colspan="6">
     &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="6" >
      <table border="0">
        <td class="labelCell"><bean:message key="label.marketing.notes"/>: </td>
        <td class="contentCell">
          <html:textarea property="notes" styleClass="inputBox" cols="50" rows="3" />
        </td>
      </table>
    </td>
  </tr>
  <input type="hidden" name="theitemid">
  <input type="hidden" name="itemname">
  <input type="hidden" name="itemdesc">
</table>
