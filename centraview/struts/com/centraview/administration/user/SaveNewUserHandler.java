/*
 * $RCSfile: SaveNewUserHandler.java,v $    $Revision: 1.5 $  $Date: 2005/09/23 11:02:54 $ - $Author: mcallist $
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

package com.centraview.administration.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Handles the request to create a new user from the Administration -> New User
 * screen.
 */
public class SaveNewUserHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SaveNewUserHandler.class);
  private static String FORWARD_final = "failure";

  /**
   * Handles the request to create a new user from the Administration -> New
   * User screen.
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ActionForward forward = null;
    try {
      ActionMessages allErrors = new ActionMessages();
      HttpSession session = request.getSession();
      int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      UserVO userVO = new UserVO();
      UserSettingsForm userForm = (UserSettingsForm)form;
      if (userForm.getName() != null && (userForm.getName()).length() > 0) {
        userVO.setContactID(userForm.getContactID());
      } else {
        allErrors.add("error.administration.newuser.id.required", new ActionMessage(
            "error.administration.newuser.id.required"));
      }

      String userName = userForm.getUserName();

      if (userName == null || userName.equals("")) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.general.requiredField", "User Name"));
      }

      // letters (upper and lower), numbers, -, _, and . are valid characters in
      // usernames
      if (!userName.matches("[\\w-\\.\\@]+")) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm",
            "User Name may only contain letters, numbers, hyphens, underscores, and periods."));
      }

      userVO.setLoginName(userName);

      if (userForm.getPassword() != null && !(userForm.getPassword()).equals("")) {
        userVO.setPassword(userForm.getPassword());
      } else {
        allErrors.add("error.administration.newuser.password.required", new ActionMessage(
            "error.administration.newuser.password.required"));
      }

      if (userForm.getUserType() != null && !(userForm.getUserType()).equals("")) {
        userVO.setUserType(userForm.getUserType());
      } else {
        allErrors.add("error.administration.newuser.usertype.required", new ActionMessage(
            "error.administration.newuser.usertype.required"));
      }

      if (userForm.getEnabled() != null && !(userForm.getEnabled()).equals("")) {
        userVO.setUserStatus(userForm.getEnabled());
      } else {
        allErrors.add("error.administration.newuser.enabled.required", new ActionMessage(
            "error.administration.newuser.enabled.required"));
      }

      if (userForm.getSecurityProfiles() != null && !(userForm.getSecurityProfiles()).isEmpty()) {
        userVO.setUserSecurityProfileId(userForm.getSecurityProfiles());
      } else {
        // Customer users do not require a security profile
        if (!userVO.getUserType().equals("CUSTOMER")) {
          allErrors.add("error.administration.newuser.securityprofiles.required",
              new ActionMessage("error.administration.newuser.securityprofiles.required"));
        }
      }

      if (!allErrors.isEmpty()) {
        // if there are any errors reported from the
        // validation done above, then save the ActionErrors
        // to the request, and forward back to the new
        // user screen so the user can fix the errors.
        saveErrors(request, allErrors);
        FORWARD_final = "userexists";
        return (mapping.findForward(FORWARD_final));
      }

      UserHome userHome = (UserHome)CVUtility.getHomeObject(
          "com.centraview.administration.user.UserHome", "User");
      User remote = userHome.create();
      remote.setDataSource(dataSource);
      remote.addUser(userId, userVO);
      String closeornew = request.getParameter("saveclosenew");
      String saveandclose = null;
      String saveandnew = null;
      if (closeornew.equals("close") || closeornew.equals("save")) {
        saveandnew = "saveandclose";
      } else if (closeornew.equals("new")) {
        saveandnew = "saveandnew";
      } else if (closeornew.equals("cancel")) {
        saveandnew = "cancel";
      }
      // set refresh and closewindow flag
      if (saveandclose != null) {
        request.setAttribute("closeWindow", "true");
      }
      request.setAttribute("refreshWindow", "true");
      request.setAttribute("actionPath", "AddUser");
      // Set Attribute as Adminitration Constant Keys for Left Navigation
      request.setAttribute("typeofmodule", AdministrationConstantKeys.USERADMINISTRATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.USERADMINISTRATION_USER);
      FORWARD_final = saveandnew;
      forward = mapping.findForward(FORWARD_final);
    } catch (UserException ue) {
      logger.error("[execute] Exception thrown.", ue);
      ActionMessages errors = new ActionMessages();
      if (ue.getExceptionId() == UserException.INSERT_FAILED) {
        ActionMessage error = new ActionMessage("error.administration.newuser.db");
        errors.add("error.administration.newuser.db", error);
      } else if (ue.getExceptionId() == UserException.IO_FAILURE) {
        ActionMessage error = new ActionMessage("error.administration.newuser.io");
        errors.add("error.administration.newuser.io", error);
      }
      saveErrors(request, errors);
      FORWARD_final = "userexists";
      forward = mapping.findForward(FORWARD_final);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    return (forward);
  }

}
