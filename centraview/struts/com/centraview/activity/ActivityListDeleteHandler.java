/*
 * $RCSfile: ActivityListDeleteHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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

package com.centraview.activity;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Delete handler for Activity list. Created: Sep 20, 2004
 * @author CentraView, LLC.
 */
public class ActivityListDeleteHandler extends Action {
  public static final String GLOBAL_FORWARD_failure = "failure";

  private static Logger logger = Logger.getLogger(ActivityListDeleteHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
    ArrayList deleteLog = new ArrayList();

    // Flag for identifying wheather any error Occured or not..
    boolean deletePopupErrorFlag = false;

    // define the delete Action is called from the View screen.
    String windowType = request.getParameter("windowType");

    String rowId[] = request.getParameterValues("rowId");
    ActivityFacadeHome activityFacadeHome = (ActivityFacadeHome)CVUtility.getHomeObject(
        "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
    try {
      ActivityFacade remote = activityFacadeHome.create();
      remote.setDataSource(dataSource);
      for (int i = 0; i < rowId.length; i++) {
        if (rowId[i] != null && !rowId[i].equals("")) {
          int elementId = Integer.parseInt(rowId[i]);
          try {
            remote.deleteActivity(elementId, individualId);
          } catch (AuthorizationFailedException ae) {
            String errorMessage = ae.getExceptionDescription();
            deleteLog.add(errorMessage);
          }
        }
      }
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new CommunicationException(e.getMessage());
    }

    if (!deleteLog.isEmpty()) {
      ActionErrors allErrors = new ActionErrors();
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm",
          "You do not have permission to delete one or more of the records you selected."));
      session.setAttribute("listErrorMessage", allErrors);
      deletePopupErrorFlag = true;
    }

    StringBuffer returnPath = new StringBuffer();

    // if we are trying to delete an activity.
    // a) if we are deleting form the view Screen
    // we will check wheather any error occured
    // if yes then we will go to the view screen and display the error
    // if no then we will close the window after deleting the activity
    // b) if we are deleting from the list screen
    // we will set back the currentPage action value
    // if the currentPage is null then
    // we will redirect to global Error Page.
    if (windowType != null && windowType.equalsIgnoreCase("Popup")) {
      if (deletePopupErrorFlag) {
        returnPath.append("/ViewActivityHandler.do");
        if (rowId != null && rowId.length != 0) {
          returnPath.append("?rowId=" + rowId[0]);
        }
      }// end of if(deletePopupErrorFlag)
      else {
        returnPath.append(mapping.findForward("deletePopup").getPath());
      }// end of else for if(deletePopupErrorFlag)
    }// end of if (windowType != null && windowType.equalsIgnoreCase("Popup"))
    else {
      if (request.getParameter("currentPage") != null) {
        returnPath.append(request.getParameter("currentPage"));
      } else {
        returnPath.append(mapping.findForward(GLOBAL_FORWARD_failure).getPath());
      }
    }// end of else for if (windowType != null &&
    // windowType.equalsIgnoreCase("Popup"))

    return new ActionForward(returnPath.toString(), true);
  }
}