<%--
 * $RCSfile: alerts_portlet.jsp,v $    $Revision: 1.3 $  $Date: 2005/09/02 14:46:35 $ - $Author: mcallist $
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
<%@ page import="java.util.*" %>
<%@ page import="org.apache.struts.util.MessageResources" %>
<%@ page import="com.centraview.common.CVUtility"%>
<%@ page import="com.centraview.common.UserObject" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.alert.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.centraview.settings.Settings" %>
<%@ page import="com.centraview.settings.SiteInfo" %>
<!-- Start Alerts  -->
<script language="javascript">
  function verifyCheckBox(formName, checkBoxName)
  {
    var ctr = 0;
    var formObj = eval("document." + formName);
    var checkBoxObj = eval("document." + formName + "." + checkBoxName);
    if (checkBoxObj != null){
      if (checkBoxObj.length == null){
        // only 1 item checkbox exists
        if (checkBoxObj.checked) ctr++;
      }else{
        for(i=0; i<checkBoxObj.length; i++){
          if (checkBoxObj[i].checked){ ctr++; }
        }
      }
      if (ctr > 0){
        return "CheckBox_checked";
      }else{
        return "CheckBox_not_checked";
      }
    }else{
      return "CheckBoxList_null";
    }
  }

  function deleteAlerts()
  {
    if (verifyCheckBox("alertForm","IdsToDelete") == "CheckBox_checked") {
      document.alertForm.submit();
    }else if (verifyCheckBox("alertForm","IdsToDelete")== "CheckBoxList_null"){
      alert("<bean:message key='label.alert.noalertstodismiss'/>.");
    }else{
      alert("<bean:message key='label.alert.selectalerttodismiss'/>.");
    }
  }
</script>
<%!
  public MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
  public ArrayList alertsArl;
  public static final int MAX_CHARS_IN_TITLE = 55;
%>
<%!
  // return ArrayList with Alets
  public ArrayList getAlertList(HttpSession session, String dataSource)
  {
    try {
      alertsArl = new ArrayList();
      HashMap hmConf = new HashMap();
      // get the userid fron the userobject
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int userid = userobjectd.getIndividualID();
      hmConf.put("UserID", new Integer(userid));

      // fetch the alerts from DB
      AlertHome ah = (AlertHome)CVUtility.getHomeObject("com.centraview.alert.AlertHome", "Alert");
      Alert remote = (Alert) ah.create();
      remote.setDataSource(dataSource);

      Collection rs = remote.getAlertList(hmConf);
      if (rs != null) {
        Iterator it = rs.iterator();
        String TitleToShow = "";
        String LinkToShow = "";
        String Title = "";
        String ModuleName = "";
        String AlertType = "";
        String rowid = "";
        alertsArl.clear();

        while (it.hasNext()) {
          TitleToShow = "";
          HashMap hm = (HashMap) it.next();
          Title = (String) hm.get("title");
          AlertType = (String) hm.get("alerttype");
          ModuleName = (String) hm.get("modulename");
          rowid = "" + hm.get("id");
          if (Title.length() > MAX_CHARS_IN_TITLE) {
            Title = Title.substring(0, MAX_CHARS_IN_TITLE) + "...";
          }

          if (AlertType.equals("ALERT")) {
            alertsArl.add(hm);
          }
        }
        return alertsArl;
      }else{
        return null;
      }
    }catch(Exception e){
      System.out.println("Exception in AlertHandler.getAlertList: " + e.toString());
      e.printStackTrace();
      return null;
    }
  }
  //create link on homepage to alerts
	public String getLinkToShow(HashMap hm)
  {
    String TitleToShow = "";
    String LinkToShow = "";
    String Title = "";
    String ModuleName = "";
    String AlertType = "";
    String rowid = "";
    Title = (String)hm.get("title");
    AlertType = (String)hm.get("alerttype");
    ModuleName = (String)hm.get("modulename");
    rowid = "" + hm.get("id");
    if (AlertType.equals("ALERT")) {
      int length = (new Integer(messages.getMessage("constants.alert.name.length"))).intValue();
      String cuttedTitle = Title;
      if (Title.length() > length) {
        cuttedTitle = Title.substring(0, (length - 1)) + "...";
      }
      String startTime = " ";
      if (hm.get("starttime") != null) {
        Timestamp ts = (Timestamp) hm.get("starttime");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:aaa");
        startTime = sdf.format(ts);
      }
      TitleToShow = ModuleName + ": " + cuttedTitle + " " + hm.get("project") + " " + startTime;
      LinkToShow = "<input type=\"checkbox\" name=\"IdsToDelete\" value=\"" + rowid + "\" class=\"checkBox\">" +
                   "<span class=\"alertText\"><a onclick=\"c_openPopup('/activities/view_activity.do?rowId=" + rowid + "&TYPEOFACTIVITY="
         + ModuleName
         + "');\" class=\"plainLink\""
         + "title='"
         + Title
         + "'>"
         + TitleToShow
         + "</a></span>";
    }
    return LinkToShow;
  }
%>
<form action="<html:rewrite page="/home/delete_alert.do"/>" method="GET" name="alertForm" id="alertForm">
<table border="0" cellspacing="0" cellpadding="0" width="97%">
  <tr>
    <td nowrap class="sectionHeader"><bean:message key="label.home.alerts"/></td>
    <td align="right" class="sectionHeader"><input class="normalButton" type="button" value="<bean:message key='label.home.dismiss'/>" id="button9" name="button9" onclick="deleteAlerts();"></td>
  </tr>
</table>
<table width="97%" border="0" cellpadding="0" cellspacing="0">
<%
  // get arraylist
  java.util.ArrayList arl = getAlertList(session, Settings.getInstance().getSiteInfo(CVUtility.getHostName(application)).getDataSource());
  if (arl != null){
    if (arl.size() > 0) {
      // alerts are present so show
      String rowClass = "tableRowOdd";
      int count = 0;
      for (int i=0; i<arl.size(); i++) {
        count++;
        rowClass = (count % 2 == 0) ? "tableRowEven" : "tableRowOdd";
        %><tr><td class="<%=rowClass%>"><%
        HashMap hm	= (HashMap)arl.get(i);
        out.println(getLinkToShow(hm));
        %></td></tr><%
      }
    }else{
      %>
      <tr>
        <td class="tableRowOdd"><bean:message key="label.home.noalerts"/>.</td>
      </tr>
      <%
    }
  }else{
    %>
    <tr>
      <td class="tableRowOdd"><bean:message key="label.home.noalerts"/>.</td>
    </tr>
    <%
  }
%>
  <tr>
    <td class="portletBottom"><html:img page="/images/spacer.gif" width="1" height="1" alt="" /></td>
  </tr>
</table>
</form>