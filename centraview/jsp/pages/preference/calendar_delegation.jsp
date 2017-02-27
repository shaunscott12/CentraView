<%--
 * $RCSfile: calendar_delegation.jsp,v $    $Revision: 1.6 $  $Date: 2005/09/01 17:28:41 $ - $Author: mcallist $
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

  Vector schedule = new Vector();
  Vector view = new Vector();
  Vector viewschedule = new Vector();

  Collection colschedule = (Collection)request.getAttribute("colschedule");
  if (colschedule == null) { colschedule = new Vector(); }
  pageContext.setAttribute("colschedule", colschedule, PageContext.PAGE_SCOPE);

  Collection colview = (Collection)request.getAttribute("colview");
  if (colview == null) { colview = new Vector(); }
  pageContext.setAttribute("colview", colview, PageContext.PAGE_SCOPE);

  Collection colviewschedule = (Collection)request.getAttribute("colviewschedule");
  if (colviewschedule == null) { colviewschedule = new Vector(); }
  pageContext.setAttribute("colviewschedule", colviewschedule, PageContext.PAGE_SCOPE);

  if ((Vector)request.getAttribute(Constants.SCHEDULEACTIVITY) != null) {
    schedule = (Vector)request.getAttribute(Constants.SCHEDULEACTIVITY);
  }

  if ((Vector)request.getAttribute(Constants.VIEW) != null) {
    view = (Vector)request.getAttribute(Constants.VIEW);
  }

  if ((Vector)request.getAttribute(Constants.VIEWSCHEDULEACTIVITY) != null) {
    viewschedule = (Vector)request.getAttribute(Constants.VIEWSCHEDULEACTIVITY);
  }

  DisplayList DL = (DisplayList)request.getAttribute("employeelist");

  String TYPEOFMODULE = (String) request.getAttribute("typeofmodule");
%>
<script language="javascript">
  function savedelegation(button)
  {
    button.disabled = true;
    button.className = "disabledButton";
    var scheduledel = document.getElementById('schedule').options.length;
    for (var i = 0; i < scheduledel; i++) {
      document.getElementById('schedule').options[i].selected = true;
    }
    var viewscheduledel = document.getElementById('viewschedule').options.length;
    for (var i = 0; i < viewscheduledel; i++) {
      document.getElementById('viewschedule').options[i].selected = true;
    }
    var viewdel = document.getElementById('view').options.length;
    for (var i = 0; i < viewdel; i++) {
      document.getElementById('view').options[i].selected = true;
    }
    document.delegationForm.action = "<html:rewrite page="/preference/calendar/edit_delegation.do"/>?action=save";
    document.delegationForm.submit();
 }

  function removedel(selectBoxName)
  {
    var alldel = document.getElementById('select').options.length;
    var scheduledel = document.getElementById(selectBoxName).options.length;
    if (document.getElementById(selectBoxName).selectedIndex == -1) {
      alert("Please select a record to be removed.");
    }else{
      for (var i=0; i<scheduledel; i++) {
        if (document.getElementById(selectBoxName).options[i].selected) {
          document.getElementById('select').options[alldel] = new Option(document.getElementById(selectBoxName).options[i].text, document.getElementById(selectBoxName).options[i].value, "false", "false");
          alldel = alldel + 1;
        }
      }
      i = 0;
      while (i < document.getElementById(selectBoxName).options.length) {
        if (document.getElementById(selectBoxName).options[i].selected) {
          document.getElementById(selectBoxName).options[i] = null;
          i = 0;
        }else{
          i++;
        }
      }
    }
  }

	function adddel(selectBoxName)
	{
    var alldel = document.getElementById('select').options.length;
    var selectBoxNamedel = document.getElementById(selectBoxName).options.length;
		if (window.document.getElementById('select').selectedIndex == -1) {
      alert("Please select a user to be added.");
    }else{
      for (var i = 0; i < alldel; i++) {
        var flag = 0;
        var flag1 = 0;
        if (document.getElementById('select').options[i].selected) {
          for (var k = 0; k < selectBoxNamedel; k++) {
            if (document.getElementById(selectBoxName).options[k].value == document.getElementById('select').options[i].value) {
              alert("This user has already been selected ");
              flag1 = 1;
            }
          }
        }
        if (document.getElementById('select').options[i].selected) {
          if (flag == 0 && flag1 == 0) {
            document.getElementById(selectBoxName).options[selectBoxNamedel] = new Option(document.getElementById('select').options[i].text, document.getElementById('select').options[i].value, "false", "false");
            selectBoxNamedel = selectBoxNamedel + 1;
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
<input type="hidden" name="TYPEOFMODULE" value="Activities">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td class="sectionHeader">Calendar Delegation Settings</td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" width="100%" class="formTable">
  <tr>
    <td>
      <span class="plainText">
      <bean:message key="label.preference.youmaychoose"/>
      </span>
    </td>
  </tr>
</table>
<table border="0" cellpadding="4" cellspacing="0" class="formTable">
  <tr>
    <td align="center" valign="top">
      <span class="boldText">&nbsp;</span><br>
      <select name="select" size="22" class="inputBox" style="width:20em;" id="select">
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
              if ((! schedule.contains(new Integer(empID)) && ! view.contains(new Integer(empID)) && ! viewschedule.contains(new Integer(empID))) && empID != uid) {
                %><option value="<%=empID %>"><%=sm.getDisplayString()%></option>
                <%
              }
            }
          }
        %>
      </select>
    </td>
    <td align="center" colspan="2">
	    <table>
	      <tr>
	      	 <td>
		      <app:cvbutton property="viewadd" styleId="viewadd" styleClass="normalButton" onclick="adddel('view');">
		        <bean:message key="label.add"/> &raquo;
		      </app:cvbutton>
		      <br/><br/>
		      <app:cvbutton property="viewremvove" styleId="viewremove" styleClass="normalButton" onclick="removedel('view');">
		        &laquo; <bean:message key="label.remove"/>
		      </app:cvbutton>
		     </td>
	       	 <td>
		      <span class="boldText"><bean:message key="label.preference.viewonmybehalf"/></span><br>
		      <html:select property="view" styleId="view" styleClass="inputBox" size="6" multiple="true" style="width:20em;">
		        <html:options collection="colview" property="strid" labelProperty="name"/>
		      </html:select>
		     </td>
		   </tr>
	      <tr>
	      	 <td>
		      <app:cvbutton property="viewadd" styleId="viewadd" styleClass="normalButton" onclick="adddel('viewschedule');">
		        <bean:message key="label.add"/> &raquo;
		      </app:cvbutton>
		      <br/><br/>
		      <app:cvbutton property="viewremvove" styleId="viewremove" styleClass="normalButton" onclick="removedel('viewschedule');">
		        &laquo; <bean:message key="label.remove"/>
		      </app:cvbutton>
		     </td>
	       	 <td>
		      <span class="boldText"><bean:message key="label.preference.viewscheduleonmybehalf"/></span><br>
		      <html:select property="viewschedule" styleId="viewschedule" styleClass="inputBox" size="6" multiple="true" style="width:20em;">
		        <html:options collection="colviewschedule" property="strid" labelProperty="name"/>
		      </html:select>
		     </td>
		   </tr>
	      <tr>
	      	 <td>
		      <app:cvbutton property="viewadd" styleId="viewadd" styleClass="normalButton" onclick="adddel('schedule');">
		        <bean:message key="label.add"/> &raquo;
		      </app:cvbutton>
		      <br/><br/>
		      <app:cvbutton property="viewremvove" styleId="viewremove" styleClass="normalButton" onclick="removedel('schedule');">
		        &laquo; <bean:message key="label.remove"/>
		      </app:cvbutton>
		     </td>
	       	 <td>
		      <span class="boldText"><bean:message key="label.preference.scheduleonmybehalf"/></span><br>
		      <html:select property="schedule" styleId="schedule" styleClass="inputBox" size="6" multiple="true" style="width:20em;">
		        <html:options collection="colschedule" property="strid" labelProperty="name"/>
		      </html:select>
		     </td>
		   </tr>

	    </table>
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
