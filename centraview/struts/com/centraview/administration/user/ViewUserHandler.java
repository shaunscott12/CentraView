/*
 * $RCSfile: ViewUserHandler.java,v $    $Revision: 1.4 $  $Date: 2005/09/27 18:15:58 $ - $Author: mcallist $
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
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class ViewUserHandler extends Action
{
  private static String FORWARD_final = "failure";
  private static Logger logger = Logger.getLogger(ViewUserHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);

    try {
      //Get reference for Home object odf User BEAN
      UserHome userHome = (UserHome)CVUtility.getHomeObject("com.centraview.administration.user.UserHome", "User");
      User remote = userHome.create();
      remote.setDataSource(dataSource);

      //Get the userID of Usr from request
      int userID = Integer.parseInt(request.getParameter("rowId").toString());
      logger.debug("[ViewUserHandler] obtained userID from request: " + userID);
      int individualId = remote.getIndividualIdForUser(userID);
      logger.debug("[ViewUserHandler] obtained individualID for given userID: " + individualId);

      //Create the ValueObject od User using getUserFull(individualId) method of remote object 
      UserVO userVO = null;
      try {
        userVO = remote.getUserPlain(individualId);
        logger.debug("[ViewUserHandler] obtained UserVO from EJB layer: \n" + userVO);
      } catch (UserException ue) {
        logger.error("Could not obtain user information from the database for individualId = [" + individualId + "]", ue);
        throw new ServletException(ue);
      }

      //Create the object of UserSettigForm and set the values
      UserSettingsForm userSettingsform = new UserSettingsForm();
      userSettingsform.setContactID(userVO.getContactID());
      userSettingsform.setName(userVO.getFirstName() + " " + userVO.getLastName());
      userSettingsform.setUserID(userVO.getUserId());
      userSettingsform.setUserName(userVO.getLoginName());
      userSettingsform.setEnabled(userVO.getUserStatus());
      userSettingsform.setUserType(userVO.getUserType());

      request.setAttribute("userform", userSettingsform);

      //Set the security profile values
      Vector securityProfileVect = userVO.getUserSecurityProfile();
      session.setAttribute("securityProfileVect", securityProfileVect);

      FORWARD_final = ".view.administration.user_detail";
    } catch (Exception e) {
      logger.error("[Exception][ViewUserHandler] Exception Thrown: ", e);
    }

    return (mapping.findForward(FORWARD_final));
  }
}
