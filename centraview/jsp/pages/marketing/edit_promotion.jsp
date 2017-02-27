<%--
* $RCSfile: edit_promotion.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:44 $ - $Author: mcallist $
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

<%-- tld reference --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import = "com.centraview.marketing.PromotionDetailListForm"%>
<%@ page import = "com.centraview.account.common.AccountConstantKeys"%>
<%
  PromotionDetailListForm promotionDetailListForm = (PromotionDetailListForm)request.getAttribute("promotionlistform");
%>

<script language="javascript">
<!--
  function gotosave(forwardto){
    var id = document.promotionlistform.promotionid.value;
    document.promotionlistform.action="<html:rewrite page='/marketing/save_promotion.do'/>?closeornew="+forwardto+"&operation=Edit&promotionid="+id;
    document.promotionlistform.submit();
  }

  function setItem(itemLookupValues)
  {
    newItemID = itemLookupValues.IDs;
    removeIDs = itemLookupValues.RemoveIDs;
    flag = itemLookupValues.Flag;
    document.promotionlistform.theitemid.value = newItemID;
    document.promotionlistform.itemname.value = '';
    document.promotionlistform.itemdesc.value = '';
    document.promotionlistform.removeID.value = removeIDs;
    if(flag)
    {
      document.promotionlistform.action = "<html:rewrite page='/marketing/view_promotion.do'/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.ADDITEM%>&operation=Edit";
    }
    else
    {
      document.promotionlistform.action = "<html:rewrite page='/marketing/view_promotion.do'/>?<%=AccountConstantKeys.TYPEOFOPERATION%>=<%=AccountConstantKeys.REMOVEITEM%>&operation=Edit";
    }
    document.promotionlistform.submit();
  }

  function saveRecord(forwardto , operation)
  {
    var id = document.promotionlistform.promotionid.value;
    document.promotionlistform.action="<html:rewrite page='/marketing/save_promotion.do'/>?closeornew="+forwardto+"&operation="+operation+"&promotionid="+id;
    document.promotionlistform.submit();
  }

  function popupCalendar(calendarId){
    var startDate = document.forms.promotionlistform.startmonth.value + "/" + document.forms.promotionlistform.startday.value + "/" + document.forms.promotionlistform.startyear.value;
    if (startDate == "//"){ startDate = ""; }
    var endDate = document.forms.promotionlistform.endmonth.value + "/" + document.forms.promotionlistform.endday.value + "/" + document.forms.promotionlistform.endyear.value;
    if (endDate == "//"){ endDate = ""; }
    c_openWindow('/calendar/select_date_time.do?dateTimeType=3&startDate='+startDate+'&endDate='+endDate, 'selectDateTime', 350, 500, '');
  }

  /*
    This function is called from the SelectDateTime.do popup, which passes
    the date and time information from that popup back to this JSP. The code
    within this function can be modified to do whatever you need to do with
    the data on this page, including munging it, setting form properties, etc.
    BUT YOU SHOULD NOT CHANGE THE NAME OR SIGNATURE OF THIS FUNCTION, WHATSOEVER!
  */
  function setDateTimeFromPopup(startDate, endDate, startTime, endTime)
  {
    var startDateArray = startDate.split("/")
    if (startDateArray == null || startDateArray.length < 3){
      document.forms.promotionlistform.startmonth.value = "";
      document.forms.promotionlistform.startday.value = "";
      document.forms.promotionlistform.startyear.value = "";
    }else{
      document.forms.promotionlistform.startmonth.value = startDateArray[0];
      document.forms.promotionlistform.startday.value = startDateArray[1];
      document.forms.promotionlistform.startyear.value = startDateArray[2];
    }

    var endDateArray = endDate.split("/")
    if (endDateArray == null || endDateArray.length < 3){
      document.forms.promotionlistform.endmonth.value = "";
      document.forms.promotionlistform.endday.value = "";
      document.forms.promotionlistform.endyear.value = "";
    }else{
      document.forms.promotionlistform.endmonth.value = endDateArray[0];
      document.forms.promotionlistform.endday.value = endDateArray[1];
      document.forms.promotionlistform.endyear.value = endDateArray[2];
    }
    return(true);
  }

  function openListing()
  {
    document.promotionlistform.action="<html:rewrite page='/marketing/promotions_list.do'/>";
    document.promotionlistform.submit();
  }

  function newPromotion()
  {
    document.promotionlistform.action = "<html:rewrite page='/marketing/new_promotion.do'/>?Forward=New";
    document.promotionlistform.submit();
  }

  function deletePromotion()
  {
    var id = document.promotionlistform.promotionid.value;
    document.promotionlistform.action="<html:rewrite page='/marketing/delete_promotion.do'/>?promotionid="+id;
    document.promotionlistform.submit();
  }
-->
</script>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="formTable">
<html:form action="/marketing/save_promotion">
  <tr>
    <td>
      <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr>
          <td colspan="2" class="sectionHeader">
            <bean:message key="label.marketing.promotions.promotiondetail"/>
          </td>
         </tr>
        <tr>
          <td valign="top" align="left" width="50%">
              <table width="100%" border="0" cellpadding="2" cellspacing="0">
                <tr>
                  <td class="labelCell">
                    <bean:message key="label.marketing.promotionid"/>:
                  </td>
                  <td align="left" valign="top" class="contentCell">
                    <c:out value="${promotionlistform.promotionid}"/>
                  </td>
                </tr>
                <tr>
                  <td class="labelCell">
                    <bean:message key="label.marketing.promotions.name"/>
                  </td>
                  <td align="left" valign="top" class="contentCell">
                    <html:text property="pname" styleId="pname" styleClass="inputBox" size="45" />
                  </td>
                </tr>
                <tr>
                  <td class="labelCell">
                    <bean:message key="label.marketing.promotions.description"/>
                  </td>
                  <td align="left" valign="top" class="contentCell">
                    <html:textarea property="pdescription" rows="5" cols="44"  styleClass="inputBox"/>
                  </td>
                </tr>
                <tr>
                  <td class="labelCell">
                    <bean:message key="label.marketing.promotions.startdate"/>
                  </td>
                  <td class="contentCell">
                       <html:text property="startmonth" styleClass="inputBox" size="2" />
                       /
                       <html:text property="startday" styleClass="inputBox" size="2" />
                       /
                       <html:text property="startyear" styleClass="inputBox" size="5" />
                       &nbsp;
                       <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" style="curosr:hand;" onclick="popupCalendar('START')" />
                  </td>
                </tr>
                <tr>
                  <td class="labelCell"><span class="labelCell"><bean:message key="label.marketing.promotions.enddate"/></span></td>
                  <td class="contentCell">
                     <html:text property="endmonth" styleClass="inputBox" size="2" />
                      /
                      <html:text property="endday" styleClass="inputBox" size="2" />
                      /
                      <html:text property="endyear" styleClass="inputBox" size="5" />
                      &nbsp;
                      <html:img page="/images/icon_calendar.gif" width="17" height="17" alt="Launch Calendar" style="curosr:hand;" onclick="popupCalendar('END')"/>
                  </td>
               </tr>
            </table>
          </td>
          <td valign="top" align="left" width="50%">
            <table width="100%" border="0" cellpadding="0">
              <tr>
                <td class="labelCell">
                   <bean:message key="label.marketing.promotions.status"/>
                </td>
                <td class="contentCell">
                  <html:select property="pstatus" styleClass="inputBox">
                    <html:options collection="colStatus" property="strid" labelProperty="name" />
                  </html:select>
                </td>
              </tr>
              <tr>
                <td colspan="2" class="sectionHeader">
                  <bean:message key="label.marketing.promotions.customfields"/>
                </td>
              </tr>
              <tr>
                <td>
                  <jsp:include page="/jsp/pages/common/custom_field.jsp">
                    <jsp:param name="Operation" value="Edit"/>
                    <jsp:param name="RecordType" value="Promotion"/>
                    <jsp:param name="RecordId" value="<%=promotionDetailListForm.getPromotionid()%>"/>
                  </jsp:include>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <jsp:include page="item_list.jsp"/>
    </td>
  </tr>
  <tr>
    <td align="left" class="sectionHeader">
        <html:button property="newrec" styleClass="normalButton" onclick="newPromotion()">
          <bean:message key="label.marketing.newpromotion"/>
        </html:button>
        <html:button property="saveclose" styleClass="normalButton" onclick="saveRecord('close' ,'Edit')">
          <bean:message key="label.saveandclose"/>
        </html:button>
        <html:button property="savenew" styleClass="normalButton" onclick="saveRecord('new','Edit')">
          <bean:message key="label.saveandnew"/>
        </html:button>
        <html:button property="deleterec" styleClass="normalButton" onclick="deletePromotion()">
          <bean:message key="label.marketing.deletepromotion"/>
        </html:button>
        <html:cancel  styleClass="normalButton"  onclick="openListing()">
          <bean:message key="label.cancel"/>
        </html:cancel>
    </td>
  </tr>
  <input type=hidden name="removeID">
  <html:hidden property="promotionid"/>
  </html:form>
</table>