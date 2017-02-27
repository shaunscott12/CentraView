<%--
 * $RCSfile: new_listmanager.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ page import="org.apache.struts.action.*, java.util.Iterator, com.centraview.marketing.* , com.centraview.common.* , java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<!-- BEGIN NewMarketingList JSP -->
<script language="JavaScript">
  function validatelistFormBean()
  {
    var flag=document.importListForm.masterlistid.value;
    if (flag == "-1")
    {
      if (document.importListForm.listname.value == "")
      {
        alert("<bean:message key='label.alert.listnamerequired'/>");
        return false;
      }
    }
    return true;
  }

  function selectListName()
  {
    var flag=document.importListForm.masterlistid.value;
    if (flag != "-1")
    {
      var index = parseInt(document.importListForm.masterlistid.selectedIndex);
      document.importListForm.listname.value = document.importListForm.masterlistid.options[index].text;
    }
  }
</script>
<html:errors/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" class="formTable">
  <html:form action="/marketing/save_new_listmanager.do" onsubmit="return validatelistFormBean();">
  <tr>
    <td>
      <table cellspacing="0" cellpadding="3" width="70%">
        <tr>
          <td valign="top">
            <table width="100%" border="0" cellpadding="3">
              <tr>
                <td class="labelCell"><bean:message key="label.marketing.importto"/>:</td>
                <td class="contentCell">
                  <html:select property="masterlistid" onchange="javascript:selectListName();" styleClass="inputBox">
                    <html:options collection="marketingList" property="strid" labelProperty="name"/>
                  </html:select>
                </td>
              </tr>
              <tr>
                <td class="labelCell"><bean:message key="label.marketing.listname"/>:</td>
                <td class="contentCell">
                  <html:text property="listname" styleId="listname" styleClass="inputBox" size="35"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell">
                  <bean:message key="label.marketing.description"/>:
                </td>
                <td class="contentCell">
                  <html:textarea property="listdescription" cols="34" rows="5"  styleClass="inputBox"/>
                </td>
              </tr>
              <tr>
                <td colspan="2" class="contentCell">
                  <bean:message key="label.marketing.listownership"/>.
                </td>
              </tr>
              <tr>
                <td class="labelCell">
                  <bean:message key="label.marketing.owner"/>:
                </td>
                <td class="contentCell">
                  <html:text property="ownername" styleClass="inputBox" readonly="true" size="35" />
                  <html:hidden property ="owner" />
                </td>
              </tr>
           </table>
         </td>
        </tr>
      </table>
    </td>
    <td width="30%" align="center" valign="top" class="labelCell">
      <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td class="sectionHeader"><bean:message key="label.marketing.instructions"/></td>
        </tr>
        <tr>
          <td class="contentCell">
            &nbsp;
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.marketing.importdata"/></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" align="right" class="sectionHeader">
        <html:submit styleClass="normalButton"><bean:message key="label.marketing.next"/> &raquo;</html:submit>
		&nbsp;
        <input name="close" type="button" class="normalButton" value="<bean:message key='label.marketing.cancel'/>" onClick="window.close();">
      </table>
    </td>
  </tr>
  <tr  height="7">
    <td colspan="2"></td>
  </tr>
  </html:form>
</table>