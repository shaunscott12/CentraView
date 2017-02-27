/*
 * $RCSfile: EditActivityHandler.java,v $    $Revision: 1.3 $  $Date: 2005/10/24 21:12:07 $ - $Author: mcallist $
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
import java.util.Iterator;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.activity.helper.ActivityVOX;
import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * This EditActivityHandler handles collects the form and check for the last
 * typeofactivity and replace current request values of the form to session form
 * values Collect the Activity VO by passing the form object to ActivityVOX
 * Carry out the Editing Activity Set the Request Objects
 */
public class EditActivityHandler extends Action {
  private static Logger logger = Logger.getLogger(EditActivityHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_edit = ".view.activities.details";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    ActivityFacadeHome activityFacade = (ActivityFacadeHome)CVUtility.getHomeObject(
        "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");

    try {
      HttpSession session = request.getSession();
      ((ActivityForm)form).setLocale(request.getLocale());

      // populate form bean for previous sub-activity
      PopulateForm populateForm = new PopulateForm();
      // set the form elements
      populateForm.setForm(request, form);

      // set form with respect to new opening page
      form = populateForm.getForm(request, form, ConstantKeys.DETAIL);
      request.setAttribute("actionName", "");
      form = (ActionForm)request.getAttribute("activityform");
      ((ActivityForm)form).setLocale(request.getLocale());

      // populate VOX
      ActivityVOX activityVOX = new ActivityVOX(form);

      // initialize VO
      ActivityVO activityVO = activityVOX.getVO();

      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();

      // this method will convert the date to GMT
      // TODO timezone!
      String currTimeZone = userObject.getUserPref().getTimeZone();
      activityVO.changeTimeZoneOfAllDates(currTimeZone, "EST");

      try {
        ActivityFacade remote = activityFacade.create();
        remote.setDataSource(dataSource);

        int activityID = Integer.parseInt(request.getParameter(ConstantKeys.ACTIVITYID
            .toLowerCase()));
        activityVO.setActivityID(activityID); // to be removed

        remote.updateActivity(activityVO, IndividualId);

        AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject(
            "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
        Authorization authRemote = authHome.create();
        authRemote.setDataSource(dataSource);

        Vector vectOptional = ((ActivityForm)form).getActivityAttendeeOptionalVector();
        Vector vectRequired = ((ActivityForm)form).getActivityAttendeeRequiredVector();
        Iterator iterOptional = null;
        Iterator iterRequired = null;

        int l1 = 0, l2 = 0, arrIndex = 0;
        if (vectOptional != null) {
          iterOptional = vectOptional.iterator();
          l1 = vectOptional.size();
        }

        if (vectRequired != null) {
          iterRequired = vectRequired.iterator();
          l2 = vectRequired.size();
        }

        int arrView[] = new int[l1 + l2];
        int arrBlank[] = {};

        if (iterRequired != null) {
          while (iterRequired.hasNext()) {
            DDNameValue nvalue = (DDNameValue)iterRequired.next();
            String strID = nvalue.getStrid();
            String arrStrings[] = strID.split("#");
            strID = arrStrings[0].trim();
            int id = Integer.parseInt(strID);
            arrView[arrIndex++] = id;
          }
        }

        if (iterOptional != null) {
          while (iterOptional.hasNext()) {
            DDNameValue nvalue = (DDNameValue)iterOptional.next();
            String strID = nvalue.getStrid();
            String arrStrings[] = strID.split("#");
            strID = arrStrings[0].trim();
            int id = Integer.parseInt(strID);
            arrView[arrIndex++] = id;
          }
        }

        request.setAttribute(ConstantKeys.TYPEOFOPERATION, ConstantKeys.EDIT);
        String activityVisibility = ((ActivityForm)form).getActivityVisibility();

        if (activityVisibility != null && activityVisibility.equals("PRIVATE")) {
          authRemote.saveRecordPermission(IndividualId, 0, "Activities", activityID, arrView,
              arrBlank, arrBlank);
        }

        if (activityVisibility != null && activityVisibility.equals("PUBLIC")) {
          authRemote.saveRecordPermission(IndividualId, -1, "Activities", activityID, arrView,
              arrBlank, arrBlank);
        }
      } catch (Exception e) {
        logger.error("[Exception] EditActivityHandler.Execute Handler ", e);
      }

      String closeornew = request.getParameter("closeornew");

      FORWARD_final = FORWARD_edit;
      if (closeornew.equals("close")) {
        request.setAttribute("closeWindow", "true");
      } else if (closeornew.equals("new")) {
        request.setAttribute(ConstantKeys.TYPEOFOPERATION, ConstantKeys.ADD);
        populateForm.resetForm(request, form);
        FORWARD_final = ".view.activities.new_activity";
      } // end of else if statement (closeornew.equals("new"))

      request.setAttribute("refreshWindow", "true");
    } catch (Exception e) {
      logger.error("[Exception] EditActivityHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));

  } // end of execute method

} // end of EditActivityHandler class

