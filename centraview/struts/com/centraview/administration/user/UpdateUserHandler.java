/*
 * $RCSfile: UpdateUserHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:50 $ - $Author: mking_cv $
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

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class UpdateUserHandler extends org.apache.struts.action.Action
{
  private static String FORWARD_final = "failure";
  private static Logger logger = Logger.getLogger(UpdateUserHandler.class);
  /**
   This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();

      // call ejb to insert record
      // initialize file vo
      UserVO userVO = new UserVO();
      UserSettingsForm userForm = (UserSettingsForm)form;

      if ((userForm.getName() != null) || ((userForm.getName()).length() > 0)) {
        userVO.setContactID(userForm.getContactID());
      }

      if (userForm.getUserName() != null) {
        userVO.setLoginName((String)userForm.getUserName());
      }

      if (userForm.getPassword() != null) {
        userVO.setPassword((String)userForm.getPassword());
      }

      if (userForm.getUserType() != null) {
        userVO.setUserType((String)userForm.getUserType());
      }

      if (userForm.getEnabled() != null) {
        userVO.setUserStatus((String)userForm.getEnabled());
      }

      if (userForm.getSecurityProfiles() != null) {
        userVO.setUserSecurityProfileId(userForm.getSecurityProfiles());
      }

      UserHome userHome = (UserHome)CVUtility.getHomeObject("com.centraview.administration.user.UserHome", "User");
      User remote = userHome.create();
      remote.setDataSource(dataSource);
      remote.updateUser(IndividualId, userVO);

      String saveclosenew = (String)request.getParameter("saveclosenew");
      String saveandnew = null;

      if (saveclosenew.equals("save")) {
        saveandnew = "saveedituser";
      } else if (saveclosenew.equals("close")) {
        saveandnew = "savecloseuser";
      } else if (saveclosenew.equals("new")) {
        saveandnew = "addnewuser";
      }
      // set refresh and closewindow flag 
      request.setAttribute("refreshWindow", "true");
      request.setAttribute("actionPath", "AddUser");
      //Set Attribute as Adminitration Constant Keys for Left Navigation
      request.setAttribute("typeofmodule", AdministrationConstantKeys.USERADMINISTRATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.USERADMINISTRATION_USER);
      FORWARD_final = saveandnew;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    return (mapping.findForward(FORWARD_final));
  }
}