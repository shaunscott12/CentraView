<%
/**
 * $RCSfile: inventory_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:48 $ - $Author: mcallist $
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

<%@ page import="com.centraview.account.inventory.InventoryForm" %>
<%@ page import="com.centraview.account.common.AccountConstantKeys" %>
<%@ page import = "com.centraview.common.*"%>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>

<script language="javascript">
<!--
  var lookupType = "";
  function vendorLookup(name) {
    lookupType = name;
    c_lookup('Vendor');
  }

  function itemLookup() {
    c_lookup('SubItem', 0);
    return false;
  }


  function setVendor(vendorLookupValues){
    name = vendorLookupValues.Name;
    id = vendorLookupValues.idValue;

    if(lookupType == "vendor")
    {
      document.inventoryform.vendorName.disabled = false;
      document.inventoryform.vendorID.value = id;
      document.inventoryform.vendorName.value = name;
    }
    if(lookupType == "soldto")
    {
      document.inventoryform.soldToName.disabled = false;
      document.inventoryform.soldToID.value = id;
      document.inventoryform.soldToName.value = name;
    }
  }

  function setLocation(locationLookupValues){
    name = locationLookupValues.Name;
    id = locationLookupValues.idValue;
    document.inventoryform.locationName.disabled = false;
    document.inventoryform.locationID.value = id;
    document.inventoryform.locationName.value = name;
  }

  function setSubItem(itemLookupValues){
    name = itemLookupValues.Name;
    id = itemLookupValues.ID;
    venID = itemLookupValues.vendorID;
    venName = itemLookupValues.vendorName;
    manuID = itemLookupValues.manufacturerID;
    manuName = itemLookupValues.manufacturerName;

    document.inventoryform.itemName.readonly = false;
    document.inventoryform.itemID.value = id;
    document.inventoryform.itemName.value = name;
    document.inventoryform.itemName.readonly = true;

    document.inventoryform.manufacturer.readonly = false;
    document.inventoryform.manufactureID.value = manuID;
    document.inventoryform.manufacturer.value = manuName;
    document.inventoryform.manufacturer.readonly = true;

    document.inventoryform.vendorName.disabled = false;
    document.inventoryform.vendorID.value = venID;
    document.inventoryform.vendorName.value = venName;
    document.inventoryform.vendorName.readonly = true;
  }

  function saveInventory(typeOfSave)	{
    document.inventoryform.action="<html:rewrite page='/accounting/save_inventory.do'/>?typeOfSave="+typeOfSave;
    document.inventoryform.submit();
    return false;
  }

  function deleteInventory()	{
    inventoryid = document.inventoryform.inventoryID.value;
    document.inventoryform.action="<html:rewrite page='/accounting/delete_inventorylist.do'/>?rowId="+inventoryid;
    document.inventoryform.submit();
    return false;
  }

  function cancelInventory() {
    document.inventoryform.action="<html:rewrite page='/accounting/inventory_list.do'/>";
    document.inventoryform.submit();
    return false;
  }
-->
</script>

<%
InventoryForm invenForm = (InventoryForm) request.getAttribute("inventoryform");

String operation = "Add";
int invenID = invenForm.getInventoryID();
Vector statusVec = invenForm.getStatusVec();
pageContext.setAttribute("statusVec", statusVec, PageContext.PAGE_SCOPE);
%>

<html:form action="/accounting/view_inventory.do">
<html:hidden property="inventoryID" />
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="formTable">
  <tr>
    <td class="buttonRow" colspan="2">
      <html:button property="save" styleClass="normalButton" onclick="saveInventory('save')">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="saveInventory('saveclose')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="saveInventory('savenew')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <% if (invenID > 0) { %>
      <html:button property="deleteinventory" styleClass="normalButton" onclick="deleteInventory()">
        <bean:message key="label.delete"/>
      </html:button>
      <% } %>
      <html:button property="cancel" styleClass="normalButton" onclick="cancelInventory()">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" class="sectionHeader"><bean:message key="label.accounting.itemdetail"/></td>
  </tr>
  <tr>
     <td valign="top" width="50%">
        <table width="100%" border="0" cellpadding="2" cellspacing="0">
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.item"/>: </td>
            <td class="contentCell">
              <html:text property="itemName" styleClass="inputBox" readonly="true" size="40" />
              <html:hidden property="itemID" />
              <html:button property="lookup" styleClass="normalButton" onclick="itemLookup()">
                <bean:message key="label.lookup"/>
              </html:button>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.identifier"/>:</td>
            <td class="contentCell">
              <app:cvtext property="identifier" styleClass="inputBox" size="40" modulename="Inventory" fieldname="strIdentifier" />
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.location"/>:</td>
            <td class="contentCell">
              <app:cvtext property="locationName" styleClass="inputBox" readonly="true" size="40" modulename="Inventory" fieldname="intLocationID"/>
              <html:hidden property="locationID" />
              <html:button property="lookup" styleClass="normalButton" onclick="c_lookup('Location')">
                <bean:message key="label.lookup"/>
              </html:button>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.manufacturer"/>:</td>
            <td class="contentCell">
              <app:cvtext property="manufacturer" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.inventoryform.manufactureID.value);" size="40" readonly="true" modulename="Inventory" fieldname="manufacturerVO"/>
              <html:hidden property="manufactureID"/>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.vendor"/>:</td>
            <td class="contentCell">
              <app:cvtext property="vendorName" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.inventoryform.vendorID.value);" readonly="true" size="40" modulename="Inventory" fieldname="vendorVO"/>
              <html:hidden property="vendorID"/>
              <html:button property="lookup" styleClass="normalButton" onclick="vendorLookup('vendor')">
                <bean:message key="label.lookup"/>
              </html:button>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.description"/>:</td>
            <td class="contentCell">
              <app:cvtextarea property="description" styleClass="textBoxWhiteBlueBorder" cols="39" rows="5" modulename="Inventory" fieldname="strDescription"/>
            </td>
          </tr>
          <%
          if(invenID > 0)
          {
          %>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.created"/>:</td>
            <td class="contentCell"><%=invenForm.getCreated()%>
              <html:hidden property="created"/>
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.modified"/>:
              <html:hidden property="modified"/>
            </td>
            <td class="contentCell"><%=invenForm.getModified()%></td>
          </tr>
          <%
          }
          %>
        </table>
      </td>
      <td valign="top" width="50%">
        <table width="100%" border="0" cellpadding="2" cellspacing="0">
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.status"/>:
            </td>
            <td class="contentCell">
              <app:cvselect property="statusID" styleClass="textBoxWhiteBlueBorder" modulename="Inventory" fieldname="intStatusID">
                <app:cvoptions collection="statusVec" property="id" labelProperty="name" />
              </app:cvselect>
            </td>
          </tr>
			    <tr>
            <td class="labelCell"><bean:message key="label.accounting.quantity"/>:</td>
            <td class="contentCell">
              <html:text property="qty" styleClass="inputBox" readonly="false" size="20" />
            </td>
          </tr>
          <tr>
            <td class="labelCell"><bean:message key="label.accounting.soldto"/>:</td>
            <td class="contentCell">
              <app:cvtext property="soldToName" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Entity', document.inventoryform.soldToID.value);" readonly="true" size="40" modulename="Inventory" fieldname="soldToVo"/>
              <html:hidden property="soldToID"/>
              <html:button property="lookup" styleClass="normalButton" onclick="vendorLookup('soldto')">
                <bean:message key="label.lookup"/>
              </html:button>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="sectionHeader"><bean:message key="label.accounting.customfields"/>:</td>
          </tr>
          <tr>
            <td colspan="2">
              <%
              if(invenID > 0)
              {
                operation = "Edit";
              }
              %>
              <jsp:include page="\jsp\pages\common\custom_field.jsp">
                <jsp:param name="Operation" value="<%=operation%>"/>
                <jsp:param name="RecordType" value="inventory"/>
                <jsp:param name="RecordId" value="<%=invenID%>"/>
              </jsp:include>
            </td>
          </tr>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" class="buttonRow">
      <html:button property="save" styleClass="normalButton" onclick="saveInventory('save')">
        <bean:message key="label.save"/>
      </html:button>
      <html:button property="saveclose" styleClass="normalButton" onclick="saveInventory('saveclose')">
        <bean:message key="label.saveandclose"/>
      </html:button>
      <html:button property="savenew" styleClass="normalButton" onclick="saveInventory('savenew')">
        <bean:message key="label.saveandnew"/>
      </html:button>
      <% if (invenID > 0) { %>
      <html:button property="deleteinventory" styleClass="normalButton" onclick="deleteInventory()">
        <bean:message key="label.delete"/>
      </html:button>
      <% } %>
      <html:button property="cancel" styleClass="normalButton" onclick="cancelInventory()">
        <bean:message key="label.cancel"/>
      </html:button>
    </td>
  </tr>
</table>
</html:form>