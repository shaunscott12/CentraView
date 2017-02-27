/*
 * $RCSfile: LoginHandler.java,v $    $Revision: 1.4 $  $Date: 2005/07/26 20:11:47 $ - $Author: mcallist $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 (the "License"); you may not use this file except in
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
 */
 
package com.centraview.common;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.jobs.UserEmailCheck;
import com.centraview.login.Login;
import com.centraview.login.LoginHome;
import com.centraview.settings.Settings;
import com.centraview.settings.SettingsInterface;
import com.centraview.settings.SiteInfo;

public class LoginHandler extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.login.failure";
  private static final String FORWARD_login = ".view.home";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  private static Logger logger = Logger.getLogger(LoginHandler.class);

  /**
   * Handles login requests for the application. Either passes control
   * forwarding to Home page, or forwards to Login page with errors.
   * This execute method is long, and convoluted and should be simplified,
   * especially because it will be run quite often and alot is relying on it.
   * However for now the quickest path is to continue to cobble junk onto the
   * edges and hope it doesn't fall off.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, CommunicationException, NamingException
  {
    String host = CVUtility.getHostName(super.getServlet().getServletContext());
    SettingsInterface settings = Settings.getInstance();
    SiteInfo siteInfo = settings.getSiteInfo(host);
    final String dataSource = siteInfo.getDataSource();
    HashMap usrResult;
    UserPrefererences up;
    String formUsername = "";
    String formPassword = "";
    String remember = request.getParameter("checkbox");
    String vistLoginPage = request.getParameter("vistLoginPage");

    // check to see if the user has clicked the "I Agree" checkbox
    String agreedTerms = request.getParameter("agreedTerms");
    if (agreedTerms != null && agreedTerms.equals("Yes")) {
      // checkbox has been checked, continue login
    } else {
      // checkbox was not checked, forward back to login page
      return(mapping.findForward(".view.login.failure"));
    }

    if (remember == null) {
      remember = "";
    }

    if (vistLoginPage == null) {
      vistLoginPage = "";
    }

    TreeMap errorMap = new TreeMap();
    String userType = null;
    UserObject userObject = null;
    HttpSession session = request.getSession();
    ModuleFieldRightMatrix mfrm = null;
		LoginHome lh = (LoginHome)CVUtility.getHomeObject("com.centraview.login.LoginHome","Login");

    try {
      // first, let's get the username and password from the HTML form
      DynaActionForm daf = (DynaActionForm)form;
      formUsername = (String)daf.get("username");
      formPassword = (String) daf.get("password");

      // next, let's check for the existence of the CVRMID cookie.
      boolean rmCookieExists = false;
      Cookie requestCookie = null;
      Cookie cookieList[] = request.getCookies();

      if (cookieList != null) {
        for (int i = 0; i < cookieList.length; i++) {
          Cookie tmpCookie = cookieList[i];
          if (tmpCookie.getName().equals("CVRMID")) {
            rmCookieExists = true;
            requestCookie = tmpCookie;
          }
        }
      }
      String cookieUsername = "";
      String cookiePassword = "";

      boolean useFormValues = false;

      // now, if the cookie exists, then get the content
      if (rmCookieExists) {
        // unencode the content of the cookie
        String unEncodedString = new String(Base64.decode(requestCookie.getValue()));

        // split the parts of the string on the "/" character
        String stringParts[] = unEncodedString.split("/");

        // get the username and password values and save for use
        cookieUsername = stringParts[0];
        cookiePassword = stringParts[1];

        // Note: In login.jsp, we checked to see if the cookie was set. If so, we
        // got the username and password from the cookie; we set the username form
        // value to the username from the cookie, and the password to "CVRMID-xxxxxxxx".
        // Therefore, we will check the form password value here; if it is NOT
        // "CVRMID-xxxxxxxx", the we know the user has manually typed in a different
        // password, and we will use the form password vs. the cookie password.
        if (formPassword != null && ! formPassword.equals("CVRMID-xxxxxxxx")) {
          useFormValues = true;
        }

        if (remember == null || remember.equals("")) {
          // if the user has *UN*-checked the Remember Me
          // checkbox, then get rid of their cookie
          this.forgetMe(response);
        }
      }

      String username = "";
      String password = "";

      if (rmCookieExists) {
        if (cookieUsername.equals(formUsername) && ! useFormValues) {
          // if the userName in the cookie equals the username in the form,
          // then, we'll authenticate on the cookie content
          username = cookieUsername;
          password = cookiePassword;
        } else {
          // if the username in the cookie does not match the username in the form,
          // then, we'll authenticate on the form content
          username = formUsername;
          password = formPassword;
        }
      } else {
        // if the cookie does not exist at all, authenticate on the form values
        username = formUsername;
        password = formPassword;
      }

      if (lh == null) {
        return (mapping.findForward("dataerror"));
      }

      Login remote = lh.create();
      remote.setDataSource(dataSource);
      usrResult = remote.authenticateUser(username, password);
      // Check to make sure the usrResult has all the fields we expect of it.
      // if so then it was a valid login, if not, then we will fail with a general
      // authentication error.
      if (usrResult.containsKey("individualid") && usrResult.containsKey("firstName") && usrResult.containsKey("lastName") && usrResult.containsKey("type")) {
        int individualId = Integer.parseInt((String)usrResult.get("individualid"));
        userType = (String)usrResult.get("type");
        String firstName = (String)usrResult.get("firstName");
        String lastName = (String)usrResult.get("lastName");

        if ((! userType.equalsIgnoreCase((String)daf.get("userType"))) && ! ("EMPLOYEE".equals(daf.get("userType")) && userType.equalsIgnoreCase("ADMINISTRATOR"))) {
          String errorHeader = "Error occurred during login.";
          errorMap.put(new Integer(0), errorHeader);
          String error = "The username or password was incorrect, or the user is disabled.";
          errorMap.put(new Integer(1), error);
          request.setAttribute("error", errorMap);
          FORWARD_final = GLOBAL_FORWARD_failure;
          return (mapping.findForward(FORWARD_final));
        }

        userObject = remote.getUserObject(individualId, firstName, lastName, userType);
        userObject.setLoginName(username);

        // In a certain case we will need a blank rights matrix, so prepare the remote connection now.
        AuthorizationHome ah = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome","Authorization");
        Authorization authorizationRemote = ah.create();
        authorizationRemote.setDataSource(dataSource);

        if (remember.equals("on")) {
          // the "Remember Me" cookie contains the a string in the format
          // "<userName>/<password>". This string is then encrypted.
          // We should probably store the SHA1 of the password, this is a major security risk!!
          // TODO: encode the SHA1 of the password in the cookie content, and not the password itself.
          // and write the corresponding login method to take the SHA1 directly.
          String cookieContent = username + "/" + password;
          String encodedString = Base64.encode(cookieContent.getBytes());
          Cookie rememberMeCookie = new Cookie("CVRMID", encodedString);
          // set the expire time - to the largest int possible
          rememberMeCookie.setMaxAge(2147483647);
          rememberMeCookie.setPath("/");
          response.addCookie(rememberMeCookie);
        }

        // get the real mfrm and put it on the UserObject
        mfrm = authorizationRemote.getUserSecurityProfileMatrix(individualId);
        up = userObject.getUserPref();
        up.setModuleAuthorizationMatrix(mfrm);
        userObject.setUserPref(up);
        session.setAttribute("userobject",userObject);

        // User Email Check - will schedule the recurring check
        // of all the user's email accounts, every x minutes where
        // x is defined by the user's preferences.
        int userInterval = userObject.getUserPref().getEmailCheckInterval();
        if (userInterval > 0) {
          // only start this job if the user wants their mail checked
          // automatically. A value of 0 means do not check mail automatically.

          // minutes to seconds, then seconds to miliseconds...
          Integer interval = new Integer(userInterval * 60 * 1000);
          
          // make sure this job isn't already scheduled for some unknown reason...
          // .. by "make sure", I mean blindly cancel the job registered for this user.
          Timer currentTimer = siteInfo.getUserTimer(individualId);
          if (currentTimer != null) {
            currentTimer.cancel();
          }

          TimerTask currentTask = siteInfo.getUserTask(individualId);
          if (currentTask != null) {
            currentTask.cancel();
          }

          Timer newTimer = new Timer(true);
          TimerTask userEmailCheck = new UserEmailCheck(individualId, session, dataSource, host);
          newTimer.schedule(userEmailCheck, 300000L, interval.longValue());
          siteInfo.setUserTimer(individualId, newTimer, userEmailCheck);
        }

        // code added for concurrent user maintinance
        session.setAttribute(SessionAlive.IS_ALIVE, new SessionAlive());

        if (userType.equalsIgnoreCase("CUSTOMER")) {
          // if this is a customer user, they can only
          // see the customer view. All other users can
          // only see the employee view
          FORWARD_final = ".view.customer.home";
        } else if ((userType.equalsIgnoreCase("ADMINISTRATOR") || userType.equalsIgnoreCase("EMPLOYEE"))) {
          FORWARD_final = FORWARD_login;
        } else {
          FORWARD_final = GLOBAL_FORWARD_failure;
        }

        // Last, set a cookie so the user never sees the EULA again...
        Cookie eulaCookie = new Cookie("CVEULA", "Yes");
        eulaCookie.setMaxAge(2147483647);    // largest int possible, cookie never expires
        eulaCookie.setPath("/");
        response.addCookie(eulaCookie);
        // Don't add more code here. Add any new code above where the
        // agreedTerms cookie is set above.
      } else {
        // the usrResult from the loginEJB isn't All that it can be.
        // Telling the user if it is the wrong username or password
        // is a security risk.  We will just go with the generic logon.invalid error.
        // no need to look at the hashmap.
        String errorHeader = "Error occurred during login.";
        errorMap.put(new Integer(0), errorHeader);
        String error = "The username or password was incorrect, or the user is disabled.";
        errorMap.put(new Integer(1), error);
        request.setAttribute("error", errorMap);
        FORWARD_final = GLOBAL_FORWARD_failure;
        // kill the CVRMID cookie
        this.forgetMe(response);
      }
    } catch (Exception e) {
      logger.error("[Exception] LoginHandler.Execute Handler ", e);
    }
    return (mapping.findForward(FORWARD_final));
  } // end execute() method

  /** 
   * Sets a cookie with the key of "CVRMID" and value of ""; set
   * to expire immediately. This effectively gets rid of the "CVRMID"
   * cookie from the user's browser, thus "forgetting" the user.
   * @param response The HttpServletResponse on which to set the cookie.
   */
  private void forgetMe(HttpServletResponse response)
  {
    Cookie forgetMeCookie = new Cookie("CVRMID", "");
    forgetMeCookie.setMaxAge(0);    // this makes the cookie expire NOW
    forgetMeCookie.setPath("/");
    response.addCookie(forgetMeCookie);
  }
} // end class LoginHandler
