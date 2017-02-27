<%
/**
 * $RCSfile: expenseform_item_list.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/06 16:02:04 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.hr.expenses.HrExpenseLines" %>
<%@ page import="com.centraview.hr.expenses.HrExpenseLineElement" %>
<%@ page import="java.util.Enumeration,java.util.Iterator,java.util.Set"%>
<%@ page import="com.centraview.common.IntMember,com.centraview.common.StringMember,com.centraview.common.FloatMember"%>
<%@ page import="java.util.StringTokenizer" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%
response.setHeader("Pragma","no-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<script language="javascript1.2">
<!--
  var iIndex = 0;
  function displaylookup()
  {
    var i;
    for(i=0; i<document.HrExpenseForm.select.length ; i++)
    {
      if(document.HrExpenseForm.typeID.length > 1)
        document.HrExpenseForm.typeID[i].value =document.HrExpenseForm.select[i].value;
      else
        document.HrExpenseForm.typeID.value = document.HrExpenseForm.select.value;
    }
  }

  function showLookup(itemNum)
  {
    iIndex = parseInt(itemNum);
    var refereceselect;
    if(document.HrExpenseForm.typeID.length > 1)
      refereceselect = document.HrExpenseForm.typeID[iIndex].value;
    else
      refereceselect = document.HrExpenseForm.typeID.value;

    if(refereceselect == "1")
    {
      c_lookup('Ticket');
    }
    else if(refereceselect == "2")
    {
      c_lookup('Project');
    }
    else if(refereceselect == "3")
    {
      c_lookup('Opportunity');
    }
    else
    {
      alert("<bean:message key='label.alert.selectreferencetype'/>");
    }
  }

  function calculateExtPrice(itemNum)
  {
    var qtyObj      = null;
    var priceObj    = null;
    var priceExtObj = null;

    if(document.HrExpenseForm.quantity.length == null)
    {
      qtyObj = eval("document.HrExpenseForm.quantity");
      priceObj  = eval("document.HrExpenseForm.priceeach");
      priceExtObj = eval("document.HrExpenseForm.calculatedprice");
    }
    else
    {
      qtyObj = eval("document.HrExpenseForm.quantity["+parseInt(itemNum)+"]");
      priceObj  = eval("document.HrExpenseForm.priceeach["+parseInt(itemNum)+"]");
      priceExtObj = eval("document.HrExpenseForm.calculatedprice["+parseInt(itemNum)+"]");
    }

    var priceExt = 0.00;

    if(qtyObj.value.length != 0 && priceObj.value.length != 0)
    priceExt =  parseFloat(qtyObj.value) * parseFloat(priceObj.value);

    var temp = ""+priceExt;
    priceExtObj.readonly = false;

    var index = 0;
    var occOfDot = parseInt(temp.indexOf('.'));
    index = parseInt(occOfDot) + parseInt(3);

    if(occOfDot != -1)
    {
      if( parseInt(index) < temp.length )
        priceExtObj.value = temp.substring(0,index);
      else
        priceExtObj.value = temp.substring(0,index)+"0";
    }
    else
    {
      priceExtObj.value = priceExt+".00";
    }

    priceExtObj.readonly = true;
  }

  function calculateTotla()
  {

    var obj = document.getElementById("total");
    var GTotal = null;
    var price = null;
    GTotal =0;
    if(document.HrExpenseForm.calculatedprice.length== null)
    {
      GTotal = eval("document.HrExpenseForm.calculatedprice");
      obj.innerText="$"+GTotal;
    }
    else
    {
      var i=0;
      var len = document.HrExpenseForm.calculatedprice.length;
      for(i=0; i<len; i++)
      {
        price= eval("document.HrExpenseForm.calculatedprice["+i+"]");
        GTotal = GTotal+parseFloat(price.value);
      }
      obj.innerText="$"+GTotal;
    }
  }

  function getTotal()
  {
    return document.HrExpenseForm.GTotal.value;
  }

  function fnItemLookup()
  {
    var hidden="";
    var str="";
    var k=0;

    for (i=0 ;i<document.HrExpenseForm.elements.length;i++)
    {
      if (document.HrExpenseForm.elements[i].type=="hidden")
      {
        if (document.HrExpenseForm.elements[i].name=="checkbox")
          k++;
      }
    }
    if(k>1)
    {
      var i;
      for(i =0; i<document.HrExpenseForm.checkbox.length;i++)
      {
        hidden=document.HrExpenseForm.checkbox[i].value + ",";
        str=str+hidden;
      }
      str=(str.substring(0,str.length-1));
    }
    else if(k==1)
    {
      hidden=document.HrExpenseForm.checkbox.value + ",";
      str=str+hidden;
      str=(str.substring(0,str.length-1));
    }
    c_openWindow('/accounting/item_lookup.do');
  }

  function setProject(projectLookupValues)
  {
    name = projectLookupValues.Name;
    idValue = projectLookupValues.idValue;
    if(document.HrExpenseForm.typeID.length > 1)
    {
      document.HrExpenseForm.reference[iIndex].value=name;
      document.HrExpenseForm.referenceId[iIndex].value=idValue;
    }
    else
    {
      document.HrExpenseForm.reference.value=name;
      document.HrExpenseForm.referenceId.value=idValue;
    }
  }

  function setTicket(name,idValue)
  {
    if(document.HrExpenseForm.typeID.length > 1)
    {
      document.HrExpenseForm.reference[iIndex].value=name;
      document.HrExpenseForm.referenceId[iIndex].value=idValue;
    }
    else
    {
      document.HrExpenseForm.reference.value=name;
      document.HrExpenseForm.referenceId.value=idValue;
    }
  }

  function setOpp(opportunityLookupValues)
  {
    name = opportunityLookupValues.Name;
    idValue = opportunityLookupValues.ID;
    if(document.HrExpenseForm.typeID.length > 1)
    {
      document.HrExpenseForm.reference[iIndex].value=name;
      document.HrExpenseForm.referenceId[iIndex].value=idValue;
    }
    else
    {
      document.HrExpenseForm.reference.value=name;
      document.HrExpenseForm.referenceId.value=idValue;
    }
  }


  function calculateExtendedAmount()
  {
    var amt = 0;
    var unitPrice = document.HrExpenseForm.priceeach.value;
    var Quantity = document.HrExpenseForm.quantity.value
    amt = unitPrice*Quantity;
    document.HrExpenseForm.calculatedprice.value=amt;
    return amt;
  }

  function checkCheckbox()
  {
    if (document.HrExpenseForm.checkbox.length > 0)
    {
      for(i =0 ;i<document.HrExpenseForm.checkbox.length;i++)
      {
        if ( document.HrExpenseForm.checkbox[i].checked)
        {
          return true;
        }
      }
    }
    else
    {
      return document.HrExpenseForm.checkbox.checked;
    }
  }

  function deleteItems()
  {
    var selectedItemID = "";
    var strParam = "";
    if(checkCheckbox())
    {
      var k=0;
      for (i=0 ;i<document.HrExpenseForm.elements.length;i++)
      {
        if(document.HrExpenseForm.elements[i].type == "checkbox")
        {
          k++;
        }
      }
      if(k>1)
      {
        for(i =0 ; i<k ; i++)
        {
          if (document.HrExpenseForm.checkbox[i].checked)
          {
            strParam = strParam + i + ",";
          }
        }
        strParam = (strParam.substring(0,strParam.length-1));
      }
      else if (k==1)
      {
        strParam = "0";
      }

      document.HrExpenseForm.action = "<html:rewrite page='/hr/expenseform_new.do'/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>&theitemid="+strParam;
      document.HrExpenseForm.submit();
    }
    else
    {
      alert("<bean:message key='label.alert.norecordsselected'/>");
    }
  }
-->
</script>


<%
  Enumeration enmattr = request.getAttributeNames();
  HrExpenseLines al = (HrExpenseLines)request.getAttribute("HrExpenseLines");
  if(al == null)
  {
    al = new HrExpenseLines();
  }
  al.calculate();
  String fmt = "0.00#";
  DecimalFormat df = new DecimalFormat(fmt,new DecimalFormatSymbols(Locale.US));
  String tot = df.format(al.getTotal());
  Vector listcolumns =  al.getViewVector();
  Enumeration listcolumns_enumeration = listcolumns.elements();
  Set listkey = al.keySet();
  Iterator it =  listkey.iterator();
%>
	<tr>
	  <td colspan="8" class="mainHeader">
		<bean:message key="label.hr.relatedexpenseitems"/>
	  </td>
	</tr>
  <tr>
    <td class="detailButtonHeader" colspan="8">
      <input name="addItem" type="button" class="normalButton" value="<bean:message key='label.value.additem'/>"  onclick="fnItemLookup();">
      <input name="deleteItem" type="button" class="normalButton" value="<bean:message key='label.value.deleteitem'/>" onclick="deleteItems();">
    </td>
  </tr>
  <tr>
    <td class="listHeader">
      <a class="listHeader" href="javascript:void(0);" onclick="vl_selectAllRows();">
        <html:img page="/images/icon_check.gif" title="Check All" alt="Check All" border="0" /> <bean:message key="label.hr.all"/>
      </a>
    </td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.expenseitem"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.description"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.quantity"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.referencetype"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.reference"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.amount"/></span></td>
    <td class="listHeader"><span class="listHeader"><bean:message key="label.hr.extendedamount"/></span></td>
  </tr>
<%
  int i=1;
  int itemNum = 0;
  while (it.hasNext())
  {
    Enumeration enum =  listcolumns.elements();
    HrExpenseLineElement ele  = (HrExpenseLineElement)al.get(it.next());
    Set s = ele.keySet();
    String lineStatus = ele.getLineStatus();
    if(lineStatus == null)
      lineStatus = "";

    IntMember lineid = (IntMember)ele.get("LineId");
    IntMember itemid = (IntMember)ele.get("ExpenseItemID");
    StringMember sku = (StringMember)ele.get("SKU");
    FloatMember qty  = (FloatMember)ele.get("Quantity");
    StringTokenizer st = new StringTokenizer(qty.getDisplayString(),".");
    String qtystr = st.nextToken();

    FloatMember priceEach = (FloatMember)ele.get("PriceEach");
    FloatMember priceExe = (FloatMember)ele.get("PriceExtended");
    StringMember desc  = (StringMember)ele.get("Description");
    StringMember reference   = (StringMember)ele.get("Reference");

    IntMember typeID = (IntMember)ele.get("ReferenceType");

    IntMember ReferenceId = (IntMember)ele.get("ReferenceId");
    String cellclassstr;
    String tableclasstr;
    // This is for if status is deleted then do not show
    if(!lineStatus.equals("Deleted"))
    {
      if(i%2 == 0)
      {
        cellclassstr="tableRowEven";
      }
      else
      {
        cellclassstr="tableRowOdd";
      }
      %>
      <tr>
        <td class="<%=cellclassstr%>">
          <input type="checkbox" name="checkbox" value="<%=itemid.getDisplayString()%>" onclick="vl_selectRow(this, false);">
        </td>
        <td class ="<%=cellclassstr%>">
          <%=sku.getDisplayString()%></td>
        <td class ="<%=cellclassstr%>">
          <html:text property="description" styleClass="inputBox" value="<%=desc.getDisplayString()%>" size="10"/>
        </td>
        <td class="<%=cellclassstr%>">
          <input type="text" name="quantity" class="inputBox" value="<%=qtystr%>" onChange="calculateExtPrice(<%=itemNum%>);" onBlur="calculateTotla();"  size="3"/>
        </td>
        <td class="<%=cellclassstr%>">
          <select name="select" class="inputBox" value="<%=typeID.getDisplayString()%>" onchange="displaylookup()">
            <%
            int typeInt = ((Integer)typeID.getMemberValue()).intValue();
            String ticketSelect = "";
            String projectSelect = "";
            String opportunitySelect = "";
            if(typeInt == 1)
            {
              ticketSelect= "selected";
            }
            else if(typeInt == 2)
            {
              projectSelect = "selected";
            }
            else if(typeInt==3)
            {
              opportunitySelect = "selected";
            }
            %>
            <option value="4" ><bean:message key="label.hr.select"/>--  --</option>
            <option value="1" <%=ticketSelect%>><bean:message key="label.hr.ticket"/></option>
            <option value="2" <%=projectSelect%>><bean:message key="label.hr.project"/></option>
            <option value="3" <%=opportunitySelect%>><bean:message key="label.hr.opportunity"/></option>
          </select>
        </td>
        <td class="<%=cellclassstr%>">
          <html:text property="reference" styleClass="inputBox" size="10" value="<%=reference.getDisplayString()%>"/>
          <html:hidden property="typeID" value="<%=typeID.getDisplayString()%>"/>
          <%
          if(ReferenceId != null)
          {
          %>
            <html:hidden property="referenceId" value="<%=ReferenceId.getDisplayString()%>"/>
          <%
          }else
          {
          %>
            <html:hidden property="referenceId" value="0" />
          <%
          }
          %>
          <input name="button9322" type="button" class="normalButton" style="width:5em;" value="<bean:message key='label.value.lookup'/>" onClick="showLookup(<%=itemNum%>)" />
        </td>
        <td class="<%=cellclassstr%>">
          $<html:text property="priceeach" styleClass="inputBox" value="<%=priceEach.getDisplayString()%>" size="6" readonly="true" onclick="return calculateExtendedAmount()"/>
        </td>
        <td class="<%=cellclassstr%>">
          $<input type="text"  name="calculatedprice" class="inputBox" readonly size="6" value="<%=priceExe.getDisplayString()%>">
        </td>
       </tr>
      <%
      itemNum = itemNum + 1;
    } else {
      int theTypeID = ((Integer)typeID.getMemberValue()).intValue();
      if(theTypeID !=1 && theTypeID !=2 && theTypeID!=3) theTypeID=4;
      %>
      <input type="hidden" name="description" value="<%=desc.getDisplayString()%>">
      <input type="hidden" name="quantity" value="<%=qty.getDisplayString()%>">
      <input type="hidden" name="priceeach" value="<%=priceEach.getDisplayString()%>">
      <input type="hidden" name="typeID" value="<%=theTypeID%>">
      <input type="hidden" name="reference" value="<%=reference.getDisplayString()%>"/>
      <% if(ReferenceId != null)
      {
      %>
        <html:hidden property="referenceId" value="<%=ReferenceId.getDisplayString()%>"/>
      <%
      }else
      {
      %>
        <html:hidden property="referenceId" value="0" />
      <%
      }
    }
    %>
    <input type="hidden" name="sku" value="<%=sku.getDisplayString()%>">
    <input type="hidden" name="lineid" value="<%=lineid.getMemberValue()%>">
    <input type="hidden" name="itemid" value="<%=itemid.getDisplayString()%>">
    <input type="hidden" name="priceExtended" value=<%=priceExe.getMemberValue()%>>
    <input type="hidden" name="linestatus" value="<%=ele.getLineStatus()%>">
    <%
    if(!lineStatus.equals("Deleted"))
    {
      i++;
    }
  } //end of while
  if (!it.hasNext())
  {
%>
    <tr height="1">
      <td class="tableRowOdd" align="center" colspan="8"><span class="labelBold"><bean:message key="label.hr.noitemsfound"/></span></td>
    </tr>
<%
  }
%>
<tr>
  <td class="itemsContainer" colspan="6">&nbsp;</td>
  <td class="itemsContainer" align="right">
    <span class="boldText"><bean:message key="label.hr.total"/>:</span>
  </td>
  <td class="itemsContainer" align="center">
      <div id="total">
        $<%=tot%>
      <div>
  </td>
</tr>
<input type="hidden" name="theitemid">
<input type="hidden" name="itemname">
<input type="hidden" name="itemdesc">