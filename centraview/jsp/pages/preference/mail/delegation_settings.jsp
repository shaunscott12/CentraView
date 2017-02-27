<%--
 * $RCSfile: delegation_settings.jsp,v $    $Revision: 1.5 $  $Date: 2005/09/01 17:28:41 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="java.util.*" %>
<%@ page import="com.centraview.common.*" %>
<%@ page import="java.util.*" %>
<%
  UserObject userobjectd = (UserObject)session.getAttribute("userobject");
  int uid = userobjectd.getIndividualID();

  Vector send = new Vector();

  Collection colsend = (Collection)request.getAttribute("colsend");
  if (colsend == null) { colsend = new Vector(); }
  pageContext.setAttribute("colsend", colsend, PageContext.PAGE_SCOPE);

  if ((Vector)request.getAttribute(Constants.SENDEMAIL) != null) {
    send = (Vector)request.getAttribute(Constants.SENDEMAIL);
  }
  DisplayList DL = (DisplayList)request.getAttribute("employeelist");

%>
<script language="javascript">
  function savedelegation(button)
  {
    button.disabled = true;
    button.className = "disabledButton";
    var senddel = document.getElementById('send').options.length;
    for (var i = 0; i < senddel; i++) {
      document.getElementById('send').options[i].selected = true;
    }
    document.delegationForm.action = "<html:rewrite page="/preference/mail/edit_delegation.do"/>?action=save";
    document.delegationForm.submit();
	}

  function removedel()
  {
    var alldel = document.getElementById('select').options.length;
    var senddel = document.getElementById('send').options.length;
    if (document.getElementById('send').selectedIndex == -1) {
      alert("Please select a record to be removed.");
    }else{
      for (var i=0; i<senddel; i++) {
        if (document.getElementById('send').options[i].selected) {
          document.getElementById('select').options[alldel] = new Option(document.getElementById('send').options[i].text, document.getElementById('send').options[i].value, "false", "false");
          alldel = alldel + 1;
        }
      }
      i = 0;
      while (i < document.getElementById('send').options.length) {
        if (document.getElementById('send').options[i].selected) {
          document.getElementById('send').options[i] = null;
          i = 0;
        }else{
          i++;
        }
      }
    }
  }

	function adddel()
	{
    var alldel = document.getElementById('select').options.length;
    var senddel = document.getElementById('send').options.length;
		if (window.document.getElementById('select').selectedIndex == -1) {
      alert("Please select a user to be added.");
    }else{
      for (var i = 0; i < alldel; i++) {
        var flag = 0;
        var flag1 = 0;
        if (document.getElementById('select').options[i].selected) {
          for (var k = 0; k < senddel; k++) {
            if (document.getElementById('send').options[k].value == document.getElementById('select').options[i].value) {
              alert("This user has already been selected ");
              flag1 = 1;
            }
          }
        }
        if (document.getElementById('select').options[i].selected) {
          if (flag == 0 && flag1 == 0) {
            document.getElementById('send').options[senddel] = new Option(document.getElementById('select').options[i].text, document.getElementById('select').options[i].value, "false", "false");
            senddel = senddel + 1;
          }
        }
      }
      i = 0;
      while (i < document.getElementById('select').options.length) {
        if (document.getElementById('select').options[i].selected) {
          document.getElementById('select').options[i] = null;
          i = 0;
        }else{
          i++;
        }
      }
    }
  }
</script>
<html:form action="/preference/mail/edit_delegation.do">
<input type="hidden" name="TYPEOFMODULE" value="<%=Constants.EMAILMODULE%>">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader"><bean:message key="label.preference.mail.emaildelegationsettings"/></td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td>
      <span class="plainText">
      <bean:message key="label.preference.mail.chooseemailsenders"/>
      </span>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td align="center" valign="top">
      <span class="boldText">&nbsp;</span><br>
      <select name="select" size="15" class="inputBox" style="width:20em;">
        <%
          if (DL.size() > 0) {
            Set s = DL.keySet();
            Iterator iter = s.iterator();
            while (iter.hasNext()) {
              String sKey = (String)iter.next();
              ListElement lem = (ListElement)DL.get(sKey);
              int empID = lem.getElementID();
              StringMember sm = (StringMember)lem.get("Name");
              String name = (String)sm.getMemberValue();
              // don't include user's who are already delegators, or the logged-in user
              if ((! send.contains(new Integer(empID))) && empID != uid) {
                %><option value="<%=empID %>"><%=sm.getDisplayString()%></option>
                <%
              }
            }
          }
        %>
      </select>
    </td>
    <td align="center">
      <app:cvbutton property="viewadd" styleId="viewadd" styleClass="normalButton" onclick="adddel();">
        <bean:message key="label.add"/> &raquo;
      </app:cvbutton>
      <br/><br/>
      <app:cvbutton property="viewremvove" styleId="viewremove" styleClass="normalButton" onclick="removedel();">
        &laquo; <bean:message key="label.remove"/>
      </app:cvbutton>
    </td>
    <td align="center" valign="top">
      <span class="boldText"><bean:message key="label.preference.mail.sendonmybehalf"/></span><br>
      <html:select property="send" styleId="send" styleClass="inputBox" size="15" multiple="true" style="width:20em;">
        <html:options collection="colsend" property="strid" labelProperty="name"/>
      </html:select>
    </td>
  </tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="buttonRow">
      <input name="savebutton" type="button" class="normalButton" value="<bean:message key='label.save'/>" onClick="savedelegation(this)" />
    </td>
  </tr>
</table>
</html:form>