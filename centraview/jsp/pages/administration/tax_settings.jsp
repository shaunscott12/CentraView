<%--
 * $RCSfile: tax_settings.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:43 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="com.centraview.common.DDNameValue" %>
<%@ page import="com.centraview.common.GlobalMasterLists" %>
<script language="javascript">
	var indexCount = 0;
	function addValue(valueList, valueTxt)
	{
    document.forms.masterdataform.action = "<html:rewrite page="/administration/save_tax_settings.do"/>?actionType=ADD";
    document.forms.masterdataform.submit();
	}

	function removeValue(valueList, valueTxt)
	{
    document.forms.masterdataform.action = "<html:rewrite page="/administration/save_tax_settings.do"/>?actionType=REMOVE";
    document.forms.masterdataform.submit();
	}


	function setText(valueList, valueTxt, valueId)
	{
    var listWidget = document.getElementById(valueList);
    var txtWidget = document.getElementById(valueTxt);
    var idWidget = document.getElementById(valueId);

	  len = listWidget.options.length;

		for (i = 0; i < len; i++) {
			selectFlag = listWidget.options[i].selected;
			if (selectFlag == true) {
			 	tempText = listWidget.options[i].text;
        txtWidget.value = tempText;

			 	tempValue = listWidget.options[i].value;
        idWidget.value = tempValue;
				i = listWidget.options.length;
				break;
			}
		}
		return true;
	}

	function checkValue(taxRate)
  {
    txtValue = taxRate.value;

    if (txtValue == ".") {
      alert ("<bean:message key='label.alert.taxrange'/>");
      taxRate.value = "";
      taxRate.focus();
      return false;
    }

    if (isInteger(txtValue) == false) {
      alert("<bean:message key='label.alert.entervalidnumber'/>")
      taxRate.value = "";
      taxRate.focus();
      return false;
    } else {
      if (txtValue > 100 || txtValue < 0) {
        alert ("<bean:message key='label.alert.taxrange'/>");
        taxRate.value = "";
        taxRate.focus();
        return false;
      }
    }
    return true;
  }

  function isInteger(s)
  {
    var i;
    for (i = 0; i < s.length; i++) {
      // Check that current character is number.
      var c = s.charAt(i);
      if (((c > "0") || (c < "9") || (c == "."))  ) return true;
    }
    // All characters are numbers.
    return false;
  }

  function gotosave(param)
  {
   document.forms.masterdataform.action = "<html:rewrite page="/administration/save_tax_settings.do"/>?actionType=" + param;
   document.forms.masterdataform.submit();
	}
</script>
<!-- BEGIN main content area -->
<html:form action="/administration/save_tax_settings.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.accountingmodulesettingstaxes"/></td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td>
      <table border="0" cellpadding="4" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold" colspan="2"><bean:message key="label.administration.taxclasses"/></td>
        </tr>
        <tr>
          <td class="contentCell" colspan="2">
            <html:text property="taxClassValue" styleId="taxClassValue" styleClass="inputBox" size="27"/>
            <html:hidden property="taxClassID" styleId="taxClassID" />
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <html:select property="taxClass" styleId="taxClass" styleClass="inputBox" size="10" multiple="true" onchange="setText('taxClass','taxClassValue','taxClassID');" style="width:15em;">
              <html:optionsCollection property="taxClassVec" value="id" label="name"/>
            </html:select>
          </td>
          <td class="contentCell">
            <html:button property="addButton" styleId="addButton" styleClass="normalButton" onclick="addValue('taxClass','taxClassValue');">
              <bean:message key="label.add" /> <bean:message key="label.administration.value"/>
            </html:button>
            <br/><br/>
            <html:button property="removeButton" styleId="removeButton" styleClass="normalButton" onclick="removeValue('taxClass','taxClassValue');">
              <bean:message key="label.remove" />
            </html:button>
          </td>
        </tr>
      </table>
    </td>
    <td>
      <table border="0" cellpadding="4" cellspacing="0" class="formTable">
        <tr>
          <td class="labelCellBold" colspan="2"><bean:message key="label.administration.taxjurisdiction"/></td>
        </tr>
        <tr>
          <td class="contentCell" colspan="2">
            <html:text property="taxJurisdictionsValue" styleId="taxJurisdictionsValue" styleClass="inputBox" size="27"/>
            <html:hidden property="taxJurisdictionsID" styleId="taxJurisdictionsID" />
          </td>
        </tr>
        <tr>
          <td class="contentCell">
            <html:select property="taxJurisdictions" styleId="taxJurisdictions" styleClass="inputBox" size="10" multiple="true" onchange="setText('taxJurisdictions','taxJurisdictionsValue','taxJurisdictionsID');" style="width:15em;">
              <html:optionsCollection property="taxJurisdictionsVec" value="id" label="name"/>
            </html:select>
          </td>
          <td class="contentCell">
            <html:button property="addButton" styleId="addButton" styleClass="normalButton" onclick="addValue('taxJurisdictions','taxJurisdictionsValue');">
              <bean:message key="label.add" /> <bean:message key="label.administration.value"/>
            </html:button>
            <br/><br/>
            <html:button property="removeButton" styleId="removeButton" styleClass="normalButton" onclick="removeValue('taxJurisdictions','taxJurisdictionsValue');">
              <bean:message key="label.remove" />
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:button property="save" styleId="save" styleClass="normalButton" onclick="gotosave('SAVE');">
        <bean:message key="label.save" />
      </html:button>
      <html:button property="savaandclose" styleId="savaandclose" styleClass="normalButton" onclick="gotosave('SAVEANDCLOSE');">
        <bean:message key="label.saveandclose" />
      </html:button>
      <html:button property="cancel" styleId="cancel" styleClass="normalButton" onclick="c_goTo('/administration/view_module_settings.do?typeofsubmodule=Accounting');">
        <bean:message key="label.cancel" />
      </html:button>
    </td>
  </tr>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.administration.taxmatrix"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td>&nbsp;</td>
    <c:forEach var="tempTaxClassLists" items="${masterdataform.map.taxClassVec}">
    <td class="labelCellBold"><c:out value='${tempTaxClassLists.name}'/></td>
    </c:forEach>
  </tr>
  <c:forEach var="taxJurisdictionsLists" items="${masterdataform.map.taxJurisdictionsVec}">
  <tr>
    <td class="labelCellBold"><c:out value='${taxJurisdictionsLists.name}' /></td>
    <c:forEach var="taxClassLists" items="${masterdataform.map.taxClassVec}">
    <c:set var="textValue" value="J${taxJurisdictionsLists.id}C${taxClassLists.id}" />
    <td class="contentCell">
      <input type="text" name="<c:out value='${textValue}'/>" value="<c:out value='${masterdataform.map.taxMatrix[textValue]}'/>" class="inputBox" size="3" onblur="checkValue(this);"> %
    </td>
    </c:forEach>
  </tr>
  </c:forEach>
</table>
<p/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <html:button property="save" styleId="save" styleClass="normalButton" onclick="gotosave('SAVE');">
        <bean:message key="label.save" />
      </html:button>
      <html:button property="savaandclose" styleId="savaandclose" styleClass="normalButton" onclick="gotosave('SAVEANDCLOSE');">
        <bean:message key="label.saveandclose" />
      </html:button>
      <html:button property="cancel" styleId="cancel" styleClass="normalButton" onclick="c_goTo('/administration/view_module_settings.do?typeofsubmodule=Accounting');">
        <bean:message key="label.cancel" />
      </html:button>
    </td>
  </tr>
</table>
</html:form>
