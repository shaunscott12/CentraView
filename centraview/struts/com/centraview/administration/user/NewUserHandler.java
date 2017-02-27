/*
 * $RCSfile: NewUserHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:50 $ - $Author: mking_cv $
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

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.DDNameValue;

/**
 * This class sets the Administration Constant key and
 * forward request to New User page
 */
public class NewUserHandler extends Action
{
  private static Logger logger = Logger.getLogger(NewUserHandler.class);
  private final static String GLOBAL_FORWARD_failure = "failure";
  private final static String FORWARD_NEWUSER = ".view.administration.user_detail";
  private String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   * Execute Method
   * @return ActionForword
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      HttpSession session = request.getSession();
      request.setAttribute("typeofmodule", AdministrationConstantKeys.USERADMINISTRATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.USERADMINISTRATION_USER);
      FORWARD_final = FORWARD_NEWUSER;
      UserSettingsForm userForm = (UserSettingsForm)form;
      Vector userTypeList = new Vector();
      if (userForm != null && userForm.getEntityID() != 1 && userForm != null && userForm.getEntityID() != 0) {
        userTypeList.add(new DDNameValue("CUSTOMER", "Customer"));
      } else {
        userTypeList.add(new DDNameValue("EMPLOYEE", "Employee"));
        userTypeList.add(new DDNameValue("ADMINISTRATOR", "Administrator"));
      }
      userForm.setUserTypeList(userTypeList);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    request.setAttribute("newUser", new Boolean(true));
    return mapping.findForward(FORWARD_final);
  }
}
