<%--
 * $RCSfile: button_bar.jsp,v $    $Revision: 1.3 $  $Date: 2005/08/10 13:31:41 $ - $Author: mcallist $
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

<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="sectionHeader">
      &nbsp;
      <app:cvbutton property="saveandclose" styleClass="normalButton" statuswindow="label.saveandclose" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditNote('close')">
        <bean:message key="label.saveandclose"/>
      </app:cvbutton>
      &nbsp;
      <app:cvbutton property="saveandnew" styleClass="normalButton"  statuswindow="label.saveandnew" tooltip="true" statuswindowarg= "label.general.blank" onclick="return checkAddEditNote('new')">
        <bean:message key="label.saveandnew"/>
      </app:cvbutton>
      &nbsp;
      <html:cancel  styleClass="normalButton"  onclick="doCancel();">
          <bean:message key="label.cancel"/>
       </html:cancel>
    </td>
  </tr>
</table>

<script language="javascript">

  <!-- check for add / edit -->
  function checkAddEditNote(closeornew)
  {
    window.document.forms[0].action = "<html:rewrite page="/notes/save_new_note.do"/>?closeornew="+closeornew+"&Button=yes&TYPEOFOPERATION=ADD";
    window.document.forms[0].submit();
    return false;
  }

  function setEntity(entityLookupValues){
    name = entityLookupValues.entName;
    id = entityLookupValues.entID;
    acctmgrid = entityLookupValues.acctManagerID;
    acctmgrname = entityLookupValues.acctManager;

    document.forms[0].entityname.readonly = false;
    document.forms[0].entityname.value = name;
    document.forms[0].entityid.value = id;
    document.forms[0].entityname.readonly = true;

    document.forms[0].individualname.value = "";
    document.forms[0].individualid.value = "";
  }

  function setIndividual(individualLookupValues){
    firstName = individualLookupValues.firstName;
    id = individualLookupValues.individualID;
    middleName = individualLookupValues.middleName;
    lastName = individualLookupValues.lastName;
    title = individualLookupValues.title;

    document.forms[0].individualid.value = id;
    document.forms[0].individualname.readonly = false;
    document.forms[0].individualname.value = firstName+" "+middleName+" "+lastName;
    document.forms[0].individualname.readonly = true;
  }

  function openLookup(typeOfLookup) {
    if (typeOfLookup == 'entity') {
      c_lookup('Entity');
    } else if (typeOfLookup == 'individual')  {
      if (document.noteform.entityid.value == "" && document.noteform.entityid.value <= 0)  {
        alert("<bean:message key='label.alert.selectentitybeforeindividual'/>.");
        return false;
      }
      c_lookup('Individual', document.noteform.entityid.value);
    }
    return false;
  }

  function doCancel() {
    var bottomFrame = "<c:out value='${param.bottomFrame}'/>";
    <%-- if the form is displayed in the Related Info Frame, we can't close the window --%>
    if (bottomFrame == "true") {
      history.go(-1);
    } else {
      window.close();
    }
  }

</script>
