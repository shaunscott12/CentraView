<%
/**
 * $RCSfile: mail_merge.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ page import="com.centraview.common.UserObject" %>
<%@ page import="com.centraview.common.UserPrefererences" %>
<%@ page import="org.apache.struts.action.DynaActionForm" %>

<script language="Javascript1.2">
<!--
  function individualSelect()
  {
    document.printtemplate.entitysavedsearch[1].checked = true;
    document.printtemplate.savedsearch1[0].checked=false;
    document.printtemplate.savedsearch1[1].checked=false;
    document.printtemplate.specificentity[1].checked = false;
    document.printtemplate.specificentity[0].checked = false;
    document.printtemplate.specificentity[2].checked = false;
  }
  function entitySelect()
  {
    document.printtemplate.entitysavedsearch[0].checked = true;
    document.printtemplate.savedsearch1[0].checked=true;
    document.printtemplate.savedsearch1[1].checked=false;
    document.printtemplate.specificentity[1].checked = false;
    document.printtemplate.specificentity[0].checked = false;
    document.printtemplate.specificentity[2].checked = false;
  }
  function selectSearch(search)
  {
    if (search == "ENTITY")
    {
      document.printtemplate.savedsearch1[0].checked=true;
      document.printtemplate.ename.value = "";
      document.printtemplate.selectedEntityId.value = "";
      document.printtemplate.individualName.value = "";
      document.printtemplate.individualId.value = "";
      document.printtemplate.entitysavedsearch[2].checked = false;
      document.printtemplate.specificentity[0].checked = false;
      document.printtemplate.specificentity[1].checked = false;
      document.printtemplate.specificentity[2].checked = false;
      document.printtemplate.individualName.value = "";
      document.printtemplate.individualId.value = "";
    }
    if (search == "INDIVIDUAL")
    {
      document.printtemplate.ename.value = "";
      document.printtemplate.selectedEntityId.value = "";
      document.printtemplate.individualName.value = "";
      document.printtemplate.individualId.value = "";
      document.printtemplate.entitysavedsearch[2].checked = false;
      document.printtemplate.savedsearch1[0].checked=false;
      document.printtemplate.savedsearch1[1].checked=false;
      document.printtemplate.specificentity[0].checked = false;
      document.printtemplate.specificentity[1].checked = false;
      document.printtemplate.specificentity[2].checked = false;
    }
    if (search == "SPECIFICENTITY")
    {
      document.printtemplate.specificentity[0].checked = true;
      document.printtemplate.specificentity[0].checked = true;
      document.printtemplate.specificentity[1].checked = false;
      document.printtemplate.savedsearch1[0].checked=false;
      document.printtemplate.savedsearch1[1].checked=false;
    }
  }   // end selectSearch()

  function goNext()
  {
    j=document.printtemplate.mergetype.length;
    var MergeType ="";
    for (i=0; i<j; i++)
    {
      if(document.printtemplate.mergetype[i].checked)
      {
        MergeType = document.printtemplate.mergetype[i].value;
      }
    }
    if (document.printtemplate.entitysavedsearch[0].checked == true)
    {
      entityLen = document.printtemplate.entityId.length;
      if (entityLen == 1)
      {
        alert ("<bean:message key='label.alert.nosavedsearchforentities'/>.");
        return false;
      }
      entityValue = document.printtemplate.entityId.value;
      if(entityValue == "" || entityValue == "0")
      {
        alert ("<bean:message key='label.alert.selectsavedsearchforentities'/>.");
        return false;
      }
    }
    if (document.printtemplate.entitysavedsearch[1].checked == true)
    {
      individualLen = document.printtemplate.individualId.length;
      if (individualLen == 1)
      {
        alert ("<bean:message key='label.alert.nosavedsearchforindividuals'/>.");
        return false;
      }
      individualValue = document.printtemplate.individualSearchId.value;
      if(individualValue == "")
      {
        alert ("<bean:message key='label.alert.selectsavedsearchforindividual'/>.");
        return false;
      }
    }
    if (document.printtemplate.entitysavedsearch[2].checked == true)
    {
      var entityid = document.printtemplate.ename.value;
      var entityName = document.printtemplate.selectedEntityId.value;
      if (entityid == "" && entityName == "")
      {
        alert("<bean:message key='label.alert.selectentity'/>");
        return false;
      }
      if (document.printtemplate.specificentity[2].checked)
      {
        var individualId = document.printtemplate.individualId.value;
        var individualName = document.printtemplate.individualName.value;
        if (individualId == "")
        {
        alert("<bean:message key='label.alert.selectindividual'/>");
        return false;
        }
      }
    }
    window.document.printtemplate.submit();
  } // end goNext()

  function entityLookup()
  {
    var mychild = openWindow('<bean:message key='label.url.root' />/EntityLookup.do', '', 400, 400,'');
  }

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.printtemplate.ename.readonly = false;
    document.printtemplate.ename.value = name;
    document.printtemplate.selectedEntityId.value = id;
    document.printtemplate.ename.readonly = true;
    document.printtemplate.individualName.disabled = false;
    document.printtemplate.individualName.value = "";
    document.printtemplate.individualId.value = "";
    document.printtemplate.entitysavedsearch[2].checked = true;
    document.printtemplate.specificentity[0].checked = true;
    document.printtemplate.savedsearch1[0].checked=false;
    document.printtemplate.savedsearch1[1].checked=false;
  }

  function individualLookup()
  {
    if (document.printtemplate.selectedEntityId.value == "" && document.printtemplate.selectedEntityId.value <=0)
    {
      alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.")
      return false;
    }else{
      entityID = document.printtemplate.selectedEntityId.value;
      c_lookup('Individual', entityID);
    }
  }

  function fnSet(name,id)
  {
    document.printtemplate.individualName.readonly = false;
    document.printtemplate.individualName.value = name;
    document.printtemplate.individualId.value = id;
    document.printtemplate.individualName.readonly = true;
  }

  function setIndividual(individualLookupValues)
  {
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;
    document.printtemplate.individualId.value = id;
    document.printtemplate.individualName.readonly = false;
    document.printtemplate.individualName.value = firstName+" "+lastName;
    document.printtemplate.individualName.readonly = true;
    document.printtemplate.specificentity[2].checked = true;
    document.printtemplate.specificentity[1].checked = false;
    document.printtemplate.specificentity[0].checked = false;
    document.printtemplate.savedsearch1[0].checked=false;
    document.printtemplate.savedsearch1[1].checked=false;
  }
  function setInd(name,id)
  {
    document.printtemplate.individualName.disabled = false;
    document.printtemplate.individualName.value = name;
    document.printtemplate.individualId.value = id;
    document.printtemplate.individualName.disabled = true;
    document.printtemplate.specificentity[2].checked = true;
    document.printtemplate.specificentity[1].checked = false;
    document.printtemplate.specificentity[0].checked = false;
    document.printtemplate.savedsearch1[0].checked=false;
    document.printtemplate.savedsearch1[1].checked=false;
  }
//-->
</script>
<html:form action ="/marketing/mailmerge_detail.do">
  <html:hidden property="artifactname"/>
  <table width="100%" height="100%" border="0" cellpadding="1" cellspacing="2" class="formTable">
    <tr>
      <td colspan="2" class="sectionHeader"><bean:message key="label.printtemplate.mergetype"/></td>
    </tr>
    <tr>
      <td colspan="2" class="labelCell">
        <html:radio property="mergetype"  value="EMAIL" />
        <bean:message key="label.printtemplate.email"/><br>
        <html:radio property="mergetype"  value="PRINT" />
        <bean:message key="label.printtemplate.print"/><br>
      </td>
    </tr>
    <tr>
      <td colspan="2" class="sectionHeader"><bean:message key="label.marketing.selectdata"/>:</td>
    </tr>
    <tr>
      <td width="6%" class="labelCell"><html:radio property="entitysavedsearch"  value="ENTITY" onclick="javascript:selectSearch('ENTITY');" /></td>
      <td width="94%" class="labelCellBold"><bean:message key="label.printtemplate.entitysavedsearch"/></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="20%" valign="top" nowrap class="labelCell"><bean:message key="label.printtemplate.savedsearch"/></td>
            <td width="40%" valign="top" class="labelCell">
              <html:select style="width:150px;" property="entityId" styleClass="inputBox" onchange="entitySelect();">
                <html:options collection="entitySearchList" labelProperty="label" property="value" />
              </html:select>
            </td>
            <td width="40%">
            	<bean:message key="label.marketing.createentitysavedsearch"/>.
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="labelCell">
              <html:radio property="savedsearch1"  value="PRIMARY" />
              <bean:message key="label.printtemplate.primarycontact"/>
            </td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="labelCell">
              <html:radio property="savedsearch1"  value="ALL" />
              <bean:message key="label.printtemplate.allcontacts"/>
            </td>
            <td>&nbsp;</td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td width="6%"><html:radio property="entitysavedsearch"  value="INDIVIDUAL" onclick="javascript:selectSearch('INDIVIDUAL');" /></td>
      <td width="94%" class="labelCellBold"><bean:message key="label.printtemplate.individualsavedsearch"/></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="20%" nowrap valign="top" class="labelCell"><bean:message key="label.printtemplate.savedsearch"/></div></td>
            <td width="40%" valign="top" class="labelCell">
              <html:select style="width:150px;" property="individualSearchId" styleClass="inputBox" onchange="individualSelect(this);">
                <html:options collection="individualSearchList" labelProperty="label" property="value" />
              </html:select>
            </td>
            <td width="40%">
                <bean:message key="label.marketing.createindividualsearchlist"/>.
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td><html:radio property="entitysavedsearch"  value="SPECIFICENTITY" onclick="javascript:selectSearch('SPECIFICENTITY');" /></td>
      <td class="labelCellBold"><bean:message key="label.printtemplate.specificentityandorindividual"/></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr>
            <td class="labelCell"><div align="right"><bean:message key="label.printtemplate.entity"/></div></td>
            <td class="labelCell">
              <html:text readonly="true" property="ename" styleId="ename" styleClass="inputBox" size="30"/>
              <html:button property="cancel" styleClass="normalButton" onclick="c_lookup('Entity');">
                <bean:message key="label.lookup"/>
              </html:button>
              <html:hidden property="selectedEntityId"/>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="labelCell">
              <html:radio property="specificentity"  value="SPECIFICPRIMARY" />
              <bean:message key="label.printtemplate.primarycontact"/>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="labelCell">
              <html:radio property="specificentity"  value="SPECIFICALL" />
              <bean:message key="label.printtemplate.allcontacts"/>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="labelCell">
              <html:radio property="specificentity" value="SPECIFICCONTACT" />
              <bean:message key="label.printtemplate.specificcontact"/>
              <html:text readonly="true" property="individualName" styleClass="inputBox" size="30"/>
              <html:hidden property="individualId"/>
              <html:button property="cancelButton" styleClass="normalButton" onclick="individualLookup();">
                <bean:message key="label.lookup"/>
              </html:button>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="2">&nbsp;</td>
    </tr>
    <tr>
      <td class="sectionHeader" align="center" colspan="2">
          <input name="Submit4" type="button" class="normalButton" value="<bean:message key='label.marketing.next'/> &raquo;" onclick="goNext()">
      </td>
    </tr>
  </table>
</html:form>
<script>
  document.printtemplate.entitysavedsearch[0].checked=true;
  document.printtemplate.savedsearch1[0].checked=true;
</script>