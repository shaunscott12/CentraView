/*
 * $RCSfile: SaveUserProfileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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

package com.centraview.preference;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.user.User;
import com.centraview.administration.user.UserException;
import com.centraview.administration.user.UserHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.contact.individual.IndividualVOX;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

public class SaveUserProfileHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(SaveUserProfileHandler.class);
  
  // Local Forwards
  private static final String FORWARD_save = ".view.preference.user_profile";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ActionErrors allErrors = new ActionErrors();
    ActionForward forward = null;
    try
    {
      DynaActionForm dynaForm = (DynaActionForm)form;

      HttpSession session = request.getSession();
      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      String typeOfSave = "saveclose";

      if (request.getParameter("buttonpress") != null)
      {
        typeOfSave = request.getParameter("buttonpress");
      }

      UserHome uhome = (UserHome)CVUtility.getHomeObject("com.centraview.administration.user.UserHome", "User");
      User uRemote = (User)uhome.create();
      uRemote.setDataSource(dataSource);
      String oldPassword = "", newPassword = "";
      // TODO integrate password change with RememberMe functionallity.
      if (uRemote != null)
      {
        oldPassword = (String)request.getParameter("oldpassword");
        newPassword = (String)request.getParameter("newpassword");
      }

      if (newPassword != null && newPassword.length() != 0)
      {
        try
        {
          uRemote.changePassword(individualId, oldPassword, newPassword);
        } catch (UserException ue) {
          if (ue.getExceptionId() == UserException.COULDNOT_CHANGE_PASSWORD)
          {
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", 
                "Error while updating login information, " + ue.getExceptionDescription()));
            saveErrors(request, allErrors);
          } else {
            ue.printStackTrace();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = (ContactFacade)cfh.create();
      remote.setDataSource(dataSource);
      IndividualVO individualVO = new IndividualVOX(form, request, dataSource);

      if (typeOfSave.equals("save"))
      {
        try {
        	remote.updateIndividual(individualVO, individualId);
          } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
            throw new ServletException(e);
          }
        request.setAttribute("TYPEOFOPERATION", "EDIT");
      }
      forward = new ActionForward(mapping.findForward(FORWARD_save).getPath(), false);  
      request.setAttribute("TYPEOFSUBMODULE", AdminConstantKeys.PREFERENCEPAGE);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    return forward;
  }
}