<%--
 * $RCSfile: login.jsp,v $    $Revision: 1.2 $  $Date: 2005/08/10 13:31:47 $ - $Author: mcallist $
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
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.apache.axis.encoding.Base64" %>
<html:form action="/customer/login">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td class="sectionHeader"><bean:message key="label.customer.pages.registeredusers"/>:</td>
  </tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" class="loginContent">
  <tr>
    <td>
      <p class="plainText">
        <bean:message key="label.customer.pages.usernameandpassword"/>.
      </p>
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
          <td><span class="plainText"><bean:message key="label.customer.pages.username"/>:</span></td>
          <td><html:text property="username" styleId="username" value="<%=cookieUserName%>" styleClass="inputBox" /></td>
        </tr>
      <tr>
        <td><span class="plainText"><bean:message key="label.customer.pages.password"/>:</span></td>
        <td><html:password property="password" value="<%=cookiePassword%>" styleClass="inputBox" /></td>
      </tr>
    </table>
    <br/>
    <table border="0" cellpadding="3" cellspacing="0">
      <tr>
        <td><html:checkbox property="rememberMe" /></td>
        <td><span class="plainText"><bean:message key="label.customer.pages.rememberme"/>.</span></td>
      </tr>
    </table>
    <table>
      <tr>
        <td valign="top">
          <html:submit property="signin" styleClass="normalButton">
            <bean:message key="label.customer.pages.login"/>
          </html:submit>
        </td>
      </tr>
    </table>
  </td>
</tr>
</table>
<input type="hidden" name="vistLoginPage" value="true">
<html:hidden property="agreedTerms" value="Yes" />
<html:hidden property="userType" />
</html:form>