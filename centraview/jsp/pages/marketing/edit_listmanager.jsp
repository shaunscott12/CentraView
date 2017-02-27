<%--
* $RCSfile: edit_listmanager.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <html:form action="/marketing/save_edit_listmanager" >
  <html:hidden property="listid"/>
  <tr valign="top">
    <td colspan="3">
      <html:errors/>
    </td>
  </tr>
  <tr>
    <td width="11"></td>
    <td>
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td valign="top">
            <table width="100%" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="9%"  class="labelCell">
                  <bean:message key="label.marketing.listname"/>:
                </td>
                <td width="91%" class="labelCell">
                  <app:cvtext property="listname" styleId="listname" styleClass="inputBox"  modulename="marketinglist" fieldname="listname" size="35"/>
                </td>
              </tr>
              <tr>
                <td class="labelCell">
                  <bean:message key="label.marketing.listid"/>:
                </td>
                <td class="labelCell">
                  <c:out value="${listFormBean.map.listid}"/>
                </td>
              </tr>
              <tr>
                <td  class="labelCell">
                  <bean:message key="label.marketing.description"/>:
                </td>
                <td class="labelCell">
                  <app:cvtextarea property="listdescription" rows="4" cols="34"  styleClass="inputBox" fieldname="listdescription" modulename="marketinglist" />
                </td>
              </tr>
            </table>
          </td>
          <td valign="top">
            <table width="100" border="0" cellpadding="3" cellspacing="0">
              <tr>
                <td width="9%"  class="labelCell">
                  <bean:message key="label.marketing.owner"/>:
                </td>
                <td width="45%"  class="labelCell">
                  <app:cvtext property="ownername" styleClass="clickableInputBox" onclick="c_openPopup_FCI('Individual', document.listFormBean.owner.value);" modulename="marketinglist" fieldname="ownername" readonly="true" size="30"/>
                  <html:hidden property ="owner" />
                </td>
                <td width="46%" class="labelCell"></td>
              </tr>
            </table>
          </td>
          <td align="right" valign="top" rowspan="2">
            <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
              <tr height="20">
                <td class="sectionHeader">
                  <bean:message key="label.marketing.helppermissions"/>
                </td>
              </tr>
              <tr>
                <td class="popupMediumTD"><bean:message key="label.marketing.definepermissions"/>
                </td>
              </tr>
            </table>
          </td>
        </tr>
    </table>
  </tr>
  <tr>
    <td colspan="3" class="sectionHeader">
      <table border="0" cellpadding="2" cellspacing="0">
        <tr>
        	<td>
	          <app:cvsubmit property="save" styleClass="normalButton" statuswindow="label.save" statuswindowarg="label.save" tooltip="false">
	       	    <bean:message key="label.save"/>
	    	    </app:cvsubmit>
	          <app:cvsubmit property="save_close" styleClass="normalButton" statuswindow="label.saveandclose" statuswindowarg="label.saveandclose" tooltip="false">
	            <bean:message key="label.saveandclose"/>
	          </app:cvsubmit>
	          <input name="button103" type="button" class="normalButton" value="<bean:message key='label.marketing.cancel'/>" onClick="c_goTo('/marketing/listmanager_list.do');"></td>
	        </td>
	        <td>
	          <c:if test='${sessionScope.userobject.individualID == listFormBean.map.owner || sessionScope.userobject.userType == "ADMINISTRATOR"}'>
	              <input name="button104" type="button" class="normalButton" value="<bean:message key='label.marketing.listpermissions'/>" onclick="c_openPermission('MarketingList', <c:out value="${listFormBean.map.listid}"/>, '<c:out value="${listFormBean.map.listname}"/>');" />
	              <input name="button104" type="button" class="normalButton" value="<bean:message key='label.marketing.memberpermissions'/>" onclick="c_openPermission('ListManager', <c:out value="${listFormBean.map.listid}"/>, '<c:out value="${listFormBean.map.listname}"/>');"  />
	          </c:if>
		    </td>
        </tr>
      </table>
  </td>
  </tr>
  </html:form>
  <tr>
    <td colspan="3">
      &nbsp;
    </td>
  </tr>
  <tr>
    <td colspan="3">
      <app:valuelist listObjectName="valueList" />
    </td>
  </tr>

</table>