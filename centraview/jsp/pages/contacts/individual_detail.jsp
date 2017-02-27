<%--
 * $RCSfile: individual_detail.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:40 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<script language="javascript">
<!--
function detailNewWindow()
{
  c_openPopup('/contacts/view_individual.do?rowId=' + document.getElementById('individualId').value);
}
function changeMOC()
{
  var otherParams = "&recordName=<c:out value="${requestScope.recordName}"/>&parentId=<c:out value="${requestScope.parentId}"/>&parentName=<c:out value="${requestScope.parentName}"/>";
  c_goTo('/contacts/change_moc.do?rowId='+document.getElementById('individualId').value+otherParams);
}
//-->
</script>
<html:form action="/contacts/save_individual.do" styleId="contactForm">
<table border="0" cellspacing="0" cellpadding="0"  width="540" height="319">
  <tr>
    <td valign="top" width="50%" style="border-right: #cccccc 1px solid;">
      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="detailHighlightHeader">
            <bean:message key="label.contacts.individual"/>
          </td>
      </table>
      <table border="0" cellpadding="1" cellspacing="0" width="100%" class="detailHighlightSection">
        <tr>
          <td class="labelCellBold"><bean:message key="label.contacts.firstname"/>:</td>
          <td class="contentCell" nowrap>
            <html:text property="firstName" styleClass="inputBox" styleId="firstName" />
            <span class="labelBold">MI:</span>
            <html:text property="middleInitial" styleClass="inputBox" maxlength="1" size="2" />
          </td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.contacts.lastname"/>:</td>
          <td class="contentCell"><html:text property="lastName" styleClass="inputBox" size="27" /></td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.contacts.title"/>:</td>
          <td class="contentCell"><html:text property="title" styleClass="inputBox" size="27" /></td>
        </tr>
        <tr>
          <td class="labelCellBold"><bean:message key="label.contacts.id"/>(s):</td>
          <td class="contentCell">
            <html:hidden property="individualId" styleId="individualId" />
            <span class="labelBold">
              <bean:message key="label.contacts.id1"/>: <input type="text" name="id1" value="<c:out value="${individualForm.map.individualId}"/>" readonly="readonly" class="InputBox" size="2">
              <bean:message key="label.contacts.id2"/>: <html:text property="id2" styleClass="inputBox" size="2" />
            </span>
          </td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="1" width="100%" class="formTable">
        <tr>
          <html:hidden property="entityId" styleId="entityId"/>
          <td class="labelCell"><a href="javascript:c_lookup('Entity');" class="plainLink"><bean:message key="label.contacts.entity"/>:</a></td>
          <td class="contentCell"><a href="<html:rewrite page="/contacts/view_entity.do"/>?rowId=<c:out value="${individualForm.map.entityId}"/>" target="_self" class="plainLink"><c:out value="${individualForm.map.entityName}"/></a></td>
        </tr>
        <html:hidden property="addressId" />
        <html:hidden property="website" />
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.address1"/>:</td>
          <td class="contentCell"><html:text property="street1" styleClass="inputBox" size="27" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.address2"/>:</td>
          <td class="contentCell"><html:text property="street2" styleClass="inputBox" size="27" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.city"/>:</td>
          <td class="contentCell"><html:text property="city" styleClass="inputBox" size="27" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.state"/>:</td>
          <td class="contentCell" nowrap>
            <span class="plainText">
              <html:text property="state" styleClass="inputBox" size="4" />
              <bean:message key="label.contacts.zip"/>: <html:text property="zip" styleClass="inputBox" size="17" />
            </span>
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.country"/>:</td>
          <td class="contentCell"><html:text property="country" styleClass="inputBox" size="27" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.source"/>:</td>
          <td class="contentCell">
            <html:hidden property="sourceId" styleId="sourceId"/>
            <html:text property="sourceName" styleClass="inputBox" size="17" styleId="sourceName"/>
            <input type="button" name="lookup" value="<bean:message key='label.contacts.lookup'/>" class="normalButton" onclick="c_lookup('Source')">
          </td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.created"/>:</td>
          <td class="contentCell"><c:out value="${individualForm.map.created}" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.modified"/>:</td>
          <td class="contentCell"><c:out value="${individualForm.map.modified}" /></td>
        </tr>
      </table>
    </td>
    <td valign="top" width="50%">
      <table  border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.contacts.contactmethods"/></td>
          <td class="sectionHeader" align="right">
            <input type="button" name="changeMoc" value="<bean:message key='label.contacts.change'/>" class="normalButton" onclick="changeMOC();">
            <input type="button" name="relatedMoc" value="<bean:message key='label.contacts.viewall'/>" class="normalButton" onclick="changeRelatedInfo('ContactMethod');">
          </td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="1" width="100%" class="formTable">
        <tr>
          <td class="labelCell">
            <html:select property="mocType1" styleClass="inputBox">
              <html:optionsCollection property="mocTypeList" label="label" value="value" />
            </html:select>
          </td>
          <td><html:hidden property="mocId1"/><html:text property="mocContent1" styleClass="inputBox" size="20" /></td>
          <td class="plainText"><bean:message key="label.contacts.ext"/>:</td>
          <td class="contentCell"><html:text property="mocExt1" styleClass="inputBox" size="6" /></td>
        </tr>
        <tr>
          <td class="labelCell">
            <html:select property="mocType2" styleClass="inputBox">
              <html:optionsCollection property="mocTypeList" label="label" value="value" />
            </html:select>
          </td>
          <td><html:hidden property="mocId2"/><html:text property="mocContent2" styleClass="inputBox" size="20" /></td>
          <td class="plainText"><bean:message key="label.contacts.ext"/>:</td>
          <td class="contentCell"><html:text property="mocExt2" styleClass="inputBox" size="6" /></td>
        </tr>
        <tr>
          <td class="labelCell">
            <html:select property="mocType3" styleClass="inputBox">
              <html:optionsCollection property="mocTypeList" label="label" value="value" />
            </html:select>
          </td>
          <td><html:hidden property="mocId3"/><html:text property="mocContent3" styleClass="inputBox" size="20" /></td>
          <td class="plainText"><bean:message key="label.contacts.ext"/>:</td>
          <td class="contentCell"><html:text property="mocExt3" styleClass="inputBox" size="6" /></td>
        </tr>
        <tr>
          <td class="labelCell"><bean:message key="label.contacts.email"/>:</td>
          <td class="contentCell" colspan="3" nowrap>
            <html:hidden property="emailId"/>
            <html:text property="email" styleClass="inputBox" size="30" />
            <a class="plainLink" onclick="c_openWindow('/email/compose.do?to=<bean:write name="individualForm" property="email" />', 'compose', 780, 550, '');"><html:img page="/images/icon_email.gif" alt="" align="absbottom" title="Compose Email" width="18" height="17" border="0"/></a>
          </td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="0"  width="100%">
        <tr>
          <td class="sectionHeader"><bean:message key="label.contacts.customfields"/></td>
          <td align="right" class="sectionHeader">
            <input type="button" name="relatedCF" value="<bean:message key='label.contacts.viewall'/>" class="normalButton" onclick="changeRelatedInfo('CustomField');">
          </td>
        </tr>
      </table>
      <table border="0" cellspacing="0" cellpadding="1" width="100%" class="formTable">
        <%-- Iterate through an array of CustomFieldVOs objects and build the stuff --%>
        <%-- find the limit of how many custom field rows can go here --%>
        <c:forEach var="customFields" items="${individualForm.map.customFields}">
          <tr>
            <html:hidden indexed="true" name="customFields" property="fieldID" />
            <html:hidden indexed="true" name="customFields" property="fieldType" />
            <td class="labelCell"><c:out value="${customFields.label}"/>:</td>
            <c:choose>
              <c:when test="${customFields.fieldType == 'SCALAR'}">
                <td class="contentCell"><html:text indexed="true" name="customFields" property="value" styleClass="inputBox" size="25" /></td>
              </c:when>
              <c:otherwise>
                <td class="contentCell">
                  <html:select indexed="true" name="customFields" property="value" styleClass="inputBox">
                    <html:option value="0">-- <bean:message key="label.contacts.select"/> --</html:option>
                    <html:optionsCollection property="optionValues" name="customFields" label="name" value="id" />
                  </html:select>
                </td>
              </c:otherwise>
            </c:choose>
          </tr>
        </c:forEach>
      </table>
    </td>
  </tr>
  <html:hidden property="closeWindow" styleId="closeWindow" />
</table>
</html:form>