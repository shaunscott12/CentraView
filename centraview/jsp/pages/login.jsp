<%--
 * $RCSfile: login.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:49 $ - $Author: mcallist $
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

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.apache.axis.encoding.Base64" %>
<html:form action="/login">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="sectionHeader"><bean:message key="label.pages.registeredusers"/>:</td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="loginContent">
  <tr>
    <td>
      <p class="plainText">
        <bean:message key="label.pages.usernameandpassword"/>
      </p>
      <%
        // BEGIN ERROR PRINTING
        TreeMap hm = (TreeMap)request.getAttribute("error");
        if (hm != null)
        {
          %>
          <div class="errorBox">
          <%
            Set s = hm.keySet();
            Object[] o = s.toArray();
            for (int i = 0; i < o.length ; i++)
            {
              Integer key = (Integer) o[i];
              String error = (String) hm.get(key);
              String image = new String();
              if (i == 0)
              {
                image = "icon_error.gif";
              }else{
                image = "spacer.gif";
              }
              %>
              <img src="<%=request.getContextPath()%>/images/<%=image%>" width="12" height="11" alt="" /> <%=error%><br/>
              <%
            }   // end for loop
          %>
          </div><br>
          <%
        }   // end if (hm != null)
        // END ERROR PRINTING
      %>
      <%
        Cookie rememberMeCookie = null;
        Cookie licenseCookie = null;
        Cookie cookieList[] = request.getCookies();
        if (cookieList != null)
        {
          for (int i=0; i<cookieList.length; i++)
          {
            Cookie tmpCookie = cookieList[i];
            if (tmpCookie.getName().equals("CVRMID"))
            {
              rememberMeCookie = tmpCookie;
            }else if (tmpCookie.getName().equals("CVEULA")){
              licenseCookie = tmpCookie;
            }
          }
        }

        String cookieUserName = "";
        String cookiePassword = "";
        String rememberChecked = "";
        if (rememberMeCookie != null)
        {
          String unEncodedString = new String(Base64.decode(rememberMeCookie.getValue()));
          String stringParts[] = unEncodedString.split("/");
          cookieUserName = stringParts[0];
          // Note: we're setting the password to "CVRMID-xxxxxxxx" for security reasons.
          // It is not safe to send the password to the user in the form, as it will
          // be viewable in the page source and browser cache. So we're sending a bogus
          // string, but it's important that we use "CVRMID-xxxxxxxx" because in the
          // LoginHandler, we check the password field from the form. If it is NOT
          // "CVRMID-xxxxxxxx", the we know the user has manually typed in a different
          // password, and we will use the form password vs. the cookie password.
          cookiePassword = "CVRMID-xxxxxxxx";
          rememberChecked = "checked=\"checked\"";
        }
      %>
      <table border="0" cellspacing="0" cellpadding="3" >
        <tr>
          <td><span class="plainText"><bean:message key="label.pages.username"/>:</span></td>
          <td><html:text property="username" styleId="username" value="<%=cookieUserName%>" styleClass="inputBox" /></td>
        </tr>
      <tr>
        <td><span class="plainText"><bean:message key="label.pages.password"/>:</span></td>
        <td><html:password property="password" value="<%=cookiePassword%>" styleClass="inputBox" /></td>
      </tr>
    </table>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0">
      <tr>
        <td><input type="checkbox" name="checkbox" class="checkBox" <%=rememberChecked%>/></td>
        <td><span class="plainText"><bean:message key="label.pages.rememberme"/>.</span></td>
      </tr>
    </table>
    <%
      String buttonText = "I Agree";
      boolean showTerms = true;
      if (licenseCookie != null)
      {
        String cookieValue = licenseCookie.getValue();
        if (cookieValue != null && cookieValue.equals("Yes"))
        {
          showTerms = false;
          buttonText = "Login";
        }
      }
    %>
    <% if (showTerms){ %>
    <table border="0" cellpadding="3" cellspacing="0">
      <tr>
        <td><html:img page="/images/spacer.gif" width="1" height="1" /></td>
        <td>
          <jsp:include page="terms_c.jsp" />
        </td>
      </tr>
      <tr>
        <td valign="top"><input type="checkbox" name="agreedTerms" class="checkBox" value="Yes" /></td>
        <td>
          <span class="plainText">
            <bean:message key="label.pages.licenseagreement"/>.
          </span>
        </td>
      </tr>
    </table>
    <% }else{ %>
    <table border="0" cellpadding="8" cellspacing="0">
      <tr>
        <td>
          <span class="plainText">
            <bean:message key="label.pages.agreetofollowing"/> <a href="javascript:void(0);" class="plainLink" onclick="c_openWindow('/show_terms.do', 'Terms', 550, 180, '');"/><bean:message key="label.pages.termsandconditions"/></a>.
            <input type="hidden" name="agreedTerms" value="Yes"/>
          </span>
        </td>
      </tr>
    </table>
    <% }    // end if (showTerms) %>
    <table>
      <tr>
        <td valign="top">
          <html:submit property="signin" styleClass="normalButton">
            <%=buttonText%>
          </html:submit>
        </td>
      </tr>
    </table>
    <br>
    <span class="plainText">
      <bean:message key="label.pages.forgotyourpassword"/>?
      <a href="<%=request.getContextPath()%>/forgot.do?action=display" class="plainLink"><bean:message key="label.pages.clickhere"/>.</a>
    </span>
  </td>
</tr>
</table>
<input type="hidden" name="vistLoginPage" value="true">
</html:form>