<%
/**
 * $RCSfile: item_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:58 $ - $Author: mcallist $
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
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.DDNameValue" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="com.centraview.account.item.ItemForm" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%
ItemForm itemForm = (ItemForm) request.getAttribute("itemform");
if (request.getAttribute("body") != null && request.getAttribute("body").equals("new"))
  itemForm = ItemForm.clearForm(itemForm);

Vector typeVec = itemForm.getItemtypevec();
if (typeVec == null)
  typeVec = new Vector();
pageContext.setAttribute("typeVec", typeVec, PageContext.PAGE_SCOPE);

Vector glAcntVec = itemForm.getGlaccountvec();
pageContext.setAttribute("glAcntVec", glAcntVec, PageContext.PAGE_SCOPE);

Vector taxVec = itemForm.getTaxclassvec();
pageContext.setAttribute("taxVec", taxVec, PageContext.PAGE_SCOPE);

String typeOfOperation = request.getParameter(AccountConstantKeys.TYPEOFOPERATION);
if(typeOfOperation == null){
  typeOfOperation = request.getAttribute(AccountConstantKeys.TYPEOFOPERATION).toString();
}
%>
<script language="javascript">
<!--
  var typeOfEntity;
  function saveitem(typeofsave) {
    if (document.itemform.TYPEOFOPERATION.value=='ADD')
      document.itemform.action="<html:rewrite page="/accounting/save_new_item.do"/>?typeofsave="+typeofsave;
    else
      document.itemform.action="<html:rewrite page="/accounting/save_edit_item.do"/>?typeofsave="+typeofsave;

    document.itemform.submit();
    return false;
  }

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.itemform.manufacturername.readonly = false;
    document.itemform.manufacturername.value = name;
    document.itemform.manufacturerid.value = id;
    document.itemform.manufacturername.readonly = true;
  }

  function setVendor(vendorLookupValues){
    name = vendorLookupValues.Name;
    id = vendorLookupValues.idValue;

    document.itemform.vendorname.readonly = false;
    document.itemform.vendorname.value = name;
    document.itemform.vendorid.value = id;
    document.itemform.vendorname.readonly = true;
  }

  function fnItemLookup() {
      itemid = document.itemform.itemid.value;
      if(itemid == ''){
        itemid= 0;
      }
      c_lookup('SubItem', itemid);
      return false;
  }

  function setSubItem(itemLookupValues){
    name = itemLookupValues.Name;
    id = itemLookupValues.ID;
    document.itemform.subitemname.readonly = false;
    document.itemform.subitemid.value = id;
    document.itemform.subitemname.value = name;
    document.itemform.subitemname.readonly = true;
  }

  function showInventoryList()
  {
    document.itemform.action="<html:rewrite page="/accounting/inventory_list.do"/>";
    document.itemform.submit();
  }
-->
</script>

<!-- BEGIN main table -->
<html:form action = "/accounting/view_item.do">
<!-- BEGIN main content area -->
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td colspan="2" class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="return saveitem('save')">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="return saveitem('saveclose')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="return saveitem('savenew')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="return saveitem('cancel')">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
  <tr>
    <td colspan="2">
      &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="2" class="sectionHeader"><bean:message key="label.accounting.detail"/></td>
  </tr>
  <tr>
    <td valign="top" width="50%">
      <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.sku"/>:</td>
          <td class="contentCell">
            <html:text property="sku" styleClass="inputBox" size="15"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.name"/>:</td>
          <td class="contentCell">
            <html:text property="itemname" styleClass="inputBox" size="45"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.type"/>:</td>
          <td class="contentCell">
            <html:select property="itemtypeid" styleClass="inputBox">
              <html:options collection="typeVec" property="id" labelProperty="name"/>
            </html:select>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.description"/>:</td>
          <td class="contentCell">
            <app:cvtextarea property="itemdesc" cols="44" rows="5" styleClass="inputBox" modulename="Item" fieldname="item" />
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.glaccount"/>:</td>
          <td class="contentCell">
            <app:cvselect property="glaccountid" styleClass="inputBox" modulename="Item" fieldname="glAccountId">
              <app:cvoptions collection="glAcntVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.taxclass"/>:</td>
          <td class="contentCell">
            <app:cvselect property="taxclassid" styleClass="inputBox" modulename="Item" fieldname="taxClassId">
              <app:cvoptions collection="taxVec" property="id" labelProperty="name"/>
            </app:cvselect>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.subitem"/>:</td>
          <td class="contentCell">
            <html:hidden property="subitemid"/>
            <app:cvtext property="subitemname" styleClass="inputBox" size="40" modulename="Item" fieldname="subItemOfId"/>
            <app:cvbutton property="lookupSubItem" styleClass="normalButton" onclick="return fnItemLookup()" modulename="Item" fieldname="subItemOfId">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.manufacturer"/>:</td>
          <td class="contentCell">
            <html:hidden property="manufacturerid"/>
            <html:text property="manufacturername" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.itemform.manufacturerid.value);"  size="40"/>
            <app:cvbutton property="lookupManufacturer" styleClass="normalButton"  onclick="c_lookup('Entity');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.vendor"/>:</td>
          <td class="contentCell">
            <html:hidden property="vendorid"/>
            <html:text property="vendorname" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.itemform.vendorid.value);"  size="40"/>
            <app:cvbutton property="lookupVendor" styleClass="normalButton" onclick="c_lookup('Vendor');">
              <bean:message key="label.lookup"/>
            </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top" align="left" width="50%">
      <table width="100%" border="0" cellpadding="2" cellspacing="0">
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.price"/>:</td>
          <td class="contentCell">
           <app:cvtext property="price" styleClass="inputBox" size="15" modulename="Item" fieldname="price"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.cost"/>:</td>
          <td class="contentCell">
           <app:cvtext property="cost" styleClass="inputBox" size="15" modulename="Item" fieldname="cost"/>
          </td>
        </tr>
        <tr>
          <td class="sectionHeader" colspan="2"><bean:message key="label.accounting.inventory"/></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.linkinventory"/>?</td>
          <td class="contentCell">
            <app:cvradio property="linktoinventory" value="YES" modulename="Item" fieldname="linkToInventory" onclick=""/><bean:message key="label.accounting.yes"/>
            <app:cvradio property="linktoinventory" value="NO" modulename="Item" fieldname="linkToInventory" onclick=""/><bean:message key="label.accounting.no"/> 
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.qtyhand"/></td>
          <td class="contentCell">
           <app:cvtext property="qtyonhand" styleClass="inputBox" size="15" modulename="Item" fieldname="qtyOnHand" readonly="true"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.qtyorder"/></td>
          <td class="contentCell">
           <app:cvtext property="qtyonorder" styleClass="inputBox" size="15" modulename="Item" fieldname="qtyOnOrder"/>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.qtybackorder"/></td>
          <td class="contentCell">
           <app:cvtext property="qtyonbackorder" styleClass="inputBox"  size="15" modulename="Item" fieldname="qtyOnBackOrder"/>
          </td>
        </tr>
        <%
        if(request.getAttribute(AccountConstantKeys.TYPEOFOPERATION) != null && request.getAttribute(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.EDIT)) {
        %>
        <tr>
          <td class="labelCell"><bean:message key="label.accounting.created"/>:</td>
          <td class="contentCell"><%=itemForm.getCreated()%></td>
        </tr>
        <%
          if (itemForm.getModified() != null && !itemForm.getModified().equals("null") && !itemForm.getModified().equals(""))  {
        %>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.modified"/>:</td>
            <td class="contentCell"><%=itemForm.getModified()%></td>
          </tr>
        <%
          }
        }
        %>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="return saveitem('save')">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="return saveitem('saveclose')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="return saveitem('savenew')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <html:button property="cancel" styleClass="normalButton" onclick="return saveitem('cancel')">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
</table>
<html:hidden property="itemid"/>
<html:hidden property="<%=AccountConstantKeys.TYPEOFOPERATION%>" value="<%=typeOfOperation%>"/>
<html:hidden property="<%=AccountConstantKeys.TYPEOFSUBMODULE%>" value="<%=request.getAttribute(AccountConstantKeys.TYPEOFSUBMODULE).toString()%>"/>
</html:form>