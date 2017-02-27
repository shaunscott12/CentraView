<%--
 * $RCSfile: edit_note.jsp,v $    $Revision: 1.4 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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
<%@ page import="com.centraview.note.NoteConstantKeys" %>
<html:form  action="/notes/save_edit_note.do">
  <html:errors/>
  <table width="100%" class="formTable" border="0" cellpadding="0" cellspacing="0" >
    <tr>
      <td width="11">
        &nbsp;
      </td>
      <td width="100%" align="left" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="left" valign="top" nowrap width="450">
              <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.save" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditNote('save')" >
                <bean:message key="label.save"/>
              </app:cvbutton>
              <app:cvbutton property="saveclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditNote('saveclose')" >
                <bean:message key="label.saveandclose"/>
              </app:cvbutton>
              <app:cvreset property="reset" styleClass="normalButton" statuswindow="label.resetfields" tooltip="true" statuswindowarg= "label.general.blank">
                <bean:message key="label.resetfields"/>
              </app:cvreset>
              <app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.cancel" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditNote('close')">
                <bean:message key="label.cancel"/>
              </app:cvbutton>
              <app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
                <bean:message key="label.properties"/>
              </app:cvbutton>
          </td>
        </tr>
      </table>
    </td>
    <td width="11">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="3">
      &nbsp;
    </td>
  </tr>
  <tr>
    <td width="11">&nbsp;</td>
    <td>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <!-- left area -->
          <td valign="top" align="left" width="50%">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
              <tr height="17">
                <td class="sectionHeader" colspan="2"><bean:message key="label.notes.notedetail"/></td>
            </tr>
          </table>
          <table width="100%" border="0" cellpadding="0">
            <tr>
              <td width="6">
                &nbsp;
              </td>
              <td valign="top" class="labelCell"> <bean:message key="label.notes.title"/>: </td>
              <td width="5">
                &nbsp;
              </td>
              <td valign="top" class="labelCell">
                <app:cvtext property="title" styleId="title" styleClass="inputBox" modulename="Notes" fieldname="title" size="45"/>
              </td>
              <td width="6">
                &nbsp;
              </td>
            </tr>
            <tr>
              <td width="6">&nbsp;</td>
              <td valign="top" class="labelCell"> <bean:message key="label.notes.details"/>: </td>
              <td width="5">&nbsp;</td>
              <td valign="top" class="labelCell">
                <app:cvtextarea property="detail" rows="10" cols="44" styleClass="inputBox" fieldname="detail" modulename="Notes" />
              </td>
              <td width="6">&nbsp;</td>
            </tr>
            <tr>
              <td width="6">&nbsp;</td>
              <td valign="top" class="labelCell" nowrap> <bean:message key="label.notes.createdby"/>: </td>
              <td width="5">&nbsp;</td>
              <td class="labelCell">
                <a href="javascript:c_openPopup_FCI('Individual', <c:out value="${noteform.createdid}" />)" class="plainLink">
                   <c:out value="${noteform.createdby}" />
                </a>
              </td>
              <td width="6">&nbsp;</td>
            </tr>
            <tr>
              <td width="6">&nbsp;</td>
              <td class="labelCell"><bean:message key="label.notes.created"/>: </td>
              <td width="5">&nbsp;</td>
              <td class="labelCell">
                 <c:out value="${noteform.createddate}" />
              </td>
              <td width="6">&nbsp;</td>
            </tr>
            <tr>
              <td width="6">&nbsp;</td>
              <td class="labelCell"><bean:message key="label.notes.modified"/>: </td>
              <td width="5">&nbsp;</td>
              <td class="labelCell">
                 <c:out value="${noteform.modifieddate}" />
              </td>
              <td width="6">&nbsp;</td>
            </tr>
          </table>
        </td>
        <!-- right area -->
        <td valign="top" align="left" width="50%">
          <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr height="17">
              <td class="sectionHeader" colspan="2"> <bean:message key="label.notes.reference"/>: </td>
            </tr>
          </table>
          <table width="100%" border="0" cellpadding="0">
            <tr>
              <td width="5">&nbsp;</td>
              <td valign="top" class="labelCell"> <bean:message key="label.notes.entity"/>: </td>
              <td width="5">&nbsp;</td>
              <td valign="top" class="labelCell">
                <html:hidden property="entityid"/>
                <app:cvtext property="entityname" readonly="true" styleClass="clickableInputBox" onclick="javascript:c_openPopup_FCI('Entity', document.noteform.entityid.value)" size="30" fieldname="entity" modulename="Notes"/>
                <app:cvbutton property="changeentity" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('entity')" fieldname="entity" modulename="Notes">
                  <bean:message key="label.lookup"/>
                </app:cvbutton>
              </td>
              <td width="5">&nbsp;</td>
            </tr>
            <tr>
              <td width="5">&nbsp;</td>
              <td valign="top" class="labelCell"> <bean:message key="label.notes.individual"/>: </td>
              <td width="5">&nbsp;</td>
              <td valign="top" class="labelCell">
                <html:hidden property="individualid"/>
                <app:cvtext property="individualname" readonly="true" styleClass="clickableInputBox" onclick="javascript:c_openPopup_FCI('Individual',document.noteform.individualid.value )" size="30" fieldname="entity" modulename="Notes" />
                <app:cvbutton property="changeindividual" styleClass="normalButton"  statuswindow="label.general.blank" tooltip="true" statuswindowarg= "label.general.blank" onclick="return openLookup('individual')" fieldname="entity" modulename="Notes">
                  <bean:message key="label.lookup"/>
                </app:cvbutton>
              </td>
              <td width="5">&nbsp;</td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </td>
  <td width="11">&nbsp;</td>
 </tr>
 <tr>
  <td width="11">&nbsp;</td>
  <td align="left" valign="top" nowrap width="100%">
    <app:cvbutton property="save" styleClass="normalButton" statuswindow="label.save" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditNote('save')">
      <bean:message key="label.save"/>
    </app:cvbutton>
    <app:cvbutton property="saveclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg= "label.general.blank" onclick="return saveEditNote('saveclose')">
      <bean:message key="label.saveandclose"/>
    </app:cvbutton>
    <app:cvreset property="reset" styleClass="normalButton" statuswindow="label.resetfields" tooltip="true" statuswindowarg= "label.general.blank">
    <bean:message key="label.resetfields"/>
    </app:cvreset>
    <app:cvbutton property="cancel" styleClass="normalButton" statuswindow="label.cancel" tooltip="true" statuswindowarg= "label.general.blank"onclick="return saveEditNote('close')">
      <bean:message key="label.cancel"/>
    </app:cvbutton>
    <app:cvbutton property="propertyindividual" styleClass="normalButton" statuswindow="label.properties" statuswindowarg="label.general.blank" tooltip="false" onclick="gotoPermission()">
      <bean:message key="label.properties"/>
    </app:cvbutton>
  </td>
  <td width="11">&nbsp;</td>
 </tr>
</table>
<!-- END main content area -->

<%-- hidden text fields --%>
<html:hidden property="createdid"/>
<html:hidden property="listScope"/>
<html:hidden property="createdby"/>
<html:hidden property="createddate"/>
<html:hidden property="modifieddate"/>
<html:hidden property="noteId"/>
<input type="hidden" name="bottomFrame" value="<c:out value='${param.bottomFrame}'/>" />
</html:form>

<script language="javascript">
  function gotoPermission()
  {
	c_openPermission('Notes', <bean:write name='noteform' property='noteId'/>, '<bean:write name="noteform" property="title"/>');
  }

  function saveEditNote(saveClose) {
    window.document.noteform.action = "<%=request.getContextPath()%>/notes/save_edit_note.do?saveType="+saveClose;
    window.document.noteform.target = "_self";
    window.document.noteform.submit();
    return false;
  }

  function openLookup(typeOfLookup)	{
    if (typeOfLookup == 'entity')	{
      c_lookup('Entity');
    } else if (typeOfLookup == 'individual')	{
      if (document.noteform.entityid.value == "" && document.noteform.entityid.value <= 0)	{
        alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.");
        return false;
      }
      c_lookup('Individual', document.noteform.entityid.value);
    }
    return false;
  }

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.noteform.entityname.readonly = false;
    document.noteform.entityname.value = name;
    document.noteform.entityid.value = id;
    document.noteform.entityname.readonly = true;

    document.noteform.individualname.value = "";
    document.noteform.individualid.value = "";
  }

  function setIndividual(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.noteform.individualid.value = id;
    document.noteform.individualname.readonly = false;
    document.noteform.individualname.value = firstName+" "+middleName+" "+lastName;
    document.noteform.individualname.readonly = true;
  }
</script>