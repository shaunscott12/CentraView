/*
 * $RCSfile: SaveActivityHandler.java,v $    $Revision: 1.4 $  $Date: 2005/10/24 21:12:07 $ - $Author: mcallist $
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.activity.helper.ActivityVOX;
import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
 * This SaveActivityHandler handles collects the form and check for the last
 * typeofactivity and replace current request values of the form to session form
 * values Collect the Activity VO by passing the form object to ActivityVOX
 * Carry out the Saving Activity Set the Request Objects
 */

public class SaveActivityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SaveActivityHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_save = ".view.activities.details";
  private static final String FORWARD_new = ".view.activities.new_activity";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      FORWARD_final = FORWARD_save;

      ActionMessages allErrors = new ActionMessages();

      HttpSession session = request.getSession();

      ((ActivityForm)form).setLocale(request.getLocale());
      // populate form bean for previous sub-activity
      PopulateForm populateForm = new PopulateForm();

      // set the form elements
      populateForm.setForm(request, form);

      // get form with respect to new opening page
      form = populateForm.getForm(request, form, ConstantKeys.DETAIL);
      ((ActivityForm)form).setLocale(request.getLocale());

      // populate VOX
      ActivityVOX activityVOX = new ActivityVOX(form);

      // initialize VO
      ActivityVO activityVO = activityVOX.getVO();

      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();

      // this method will convert the date to GMT
      String currTimeZone = userObject.getUserPref().getTimeZone();
      activityVO.changeTimeZoneOfAllDates(currTimeZone, "EST");

      String emailinvitation = ((ActivityForm)form).getActivityEmailInvitation();
      if (emailinvitation != null && emailinvitation.equals("on")) {
        ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        try {
          ContactFacade remote = cfh.create();
          remote.setDataSource(dataSource);
          // Static 2 because we are looking for the Individual's Primary Email
          // Address
          String emailAddress = remote.getPrimaryEmailAddress(IndividualId, 2);
          if (emailAddress == null || emailAddress.equals("")) {
            allErrors
                .add(
                    ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage(
                        "error.freeForm",
                        "Error while sending email invitations. You do not have an email address configured. Please configure a primary email address in order to send email invitations."));
            saveErrors(request, allErrors);
            return (mapping.findForward(FORWARD_save));
          }
        } catch (Exception e) {
          logger.error("[Exception] SaveActivityHandler.Execute Handler ", e);
        }
      }

      // call to activity facade
      ActivityFacadeHome activityFacade = (ActivityFacadeHome)CVUtility.getHomeObject(
          "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");

      try {
        ActivityFacade remote = activityFacade.create();
        remote.setDataSource(dataSource);

        int rowId = remote.addActivity(activityVO, IndividualId);

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
          }// end of while (iterRequired.hasNext())
        }// end of if (iterRequired != null)

        if (iterOptional != null) {
          while (iterOptional.hasNext()) {
            DDNameValue nvalue = (DDNameValue)iterOptional.next();
            String strID = nvalue.getStrid();
            String arrStrings[] = strID.split("#");
            strID = arrStrings[0].trim();
            int id = Integer.parseInt(strID);
            arrView[arrIndex++] = id;
          }// end of while (iterOptional.hasNext())
        }// end of if (iterOptional != null)

        request.setAttribute(ConstantKeys.TYPEOFOPERATION, ConstantKeys.EDIT);
        String activityVisibility = ((ActivityForm)form).getActivityVisibility();
        if (activityVisibility != null && activityVisibility.equals("PRIVATE")) {
          authRemote.saveRecordPermission(IndividualId, 0, "Activities", rowId, arrView, arrBlank,
              arrBlank);
        }
        if (activityVisibility != null && activityVisibility.equals("PUBLIC")) {
          authRemote.saveRecordPermission(IndividualId, -1, "Activities", rowId, arrView, arrBlank,
              arrBlank);
        }

      }// end of try Block
      catch (Exception e) {
        logger.error("[execute]: Exception", e);
      }// end of catch Block

      String closeornew = request.getParameter("closeornew");

      if (closeornew != null && closeornew.equals("close")) {
        request.setAttribute("closeWindow", "true");
      } // end of if (closeornew != null && closeornew.equals("close"))
      else if (closeornew != null && closeornew.equals("new")) {
        populateForm.resetForm(request, form);
        request.setAttribute(ConstantKeys.TYPEOFOPERATION, ConstantKeys.ADD);
        FORWARD_final = FORWARD_new;
      } // end of else if (closeornew != null && closeornew.equals("new"))
      request.setAttribute("refreshWindow", "true");

    } catch (Exception e) {
      logger.error("[Exception] SaveActivityHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }

    return (mapping.findForward(FORWARD_final));
  } // end execute() method
}
