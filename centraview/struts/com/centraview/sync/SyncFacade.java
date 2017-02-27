/*
 * $RCSfile: SyncFacade.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

package com.centraview.sync;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import com.centraview.activity.ActivityForm;
import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.activity.helper.ActivityVOX;
import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.login.LoginHome;
import com.centraview.syncfacade.SyncFacadeHome;

public class SyncFacade {
  private String dataSource = null;
  private static Logger logger = Logger.getLogger(SyncFacade.class);

  /**
   * Creates an instance of the LoginEJB and authenticates the user for the Sync
   * component of CentraView. This method is called from one place, the Login
   * action (SyncLogin.do). It gets the userName and password from the LoginForm
   * object and validates using the LoginEJB.authenticateUser() method. It then
   * stores a sessionID string in the UserObject in the HttpSession and prints
   * the System Date/Time and sessionID to the HttpServletResponse.
   * @param form The LoginForm object passed from the Login Action class
   * @param request The HttpServletRequest object passed from the Login Action
   *          class
   * @param response The HttpServletResponse object passed from the Login Action
   *          classs
   * @return boolean: true for successful login, false for unsuccessul
   */
  public boolean doLogin(ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws IOException
  {
    String userName = new String("");
    String password = new String("");
    PrintWriter writer = response.getWriter();

    if (form != null) {
      // use the LoginForm bean to get the username and password
      LoginForm LoginForm = (LoginForm)form;

      userName = LoginForm.getUserName();
      password = LoginForm.getPassword();
    } else {
      // if the form wasn't submitted, then the process failed, so return false
      return (false);
    }

    try {
      LoginHome lh = (LoginHome)CVUtility.getHomeObject("com.centraview.login.LoginHome", "Login");
      com.centraview.login.Login remote = lh.create();
      remote.setDataSource(this.dataSource);

      HashMap loginResult = remote.authenticateUser(userName, password);

      if (!loginResult.containsKey("error")) {
        // login was successful, generate a sessionID and stick it in the
        // HttpSession UserObject

        // then generate a sesionID
        HttpSession session = request.getSession();
        String sessionID = session.getId();

        // next, create a UserObject, and pass the sessionID to it
        String uoIndividualID = (String)loginResult.get("individualid");
        String uoFirstName = (String)loginResult.get("firstName");
        String uoLastName = (String)loginResult.get("lastName");
        String uoUserType = (String)loginResult.get("type");

        UserObject userObject = remote.getUserObject(Integer.parseInt(uoIndividualID),
            uoFirstName, uoLastName, uoUserType);
        userObject.setSessionID(sessionID);

        // Please note that the next line has a severe mis-spelling on it.
        // The class name was mis-spelled when it was created, and it was
        // never fixed. So please be careful when trying to use the class
        // "UserPrefererences" - make sure you spell it wrong intentionally
        UserPrefererences userPrefs = userObject.getUserPref();

        AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject(
            "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
        Authorization authRemote = authHome.create();
        authRemote.setDataSource(this.dataSource);

        ModuleFieldRightMatrix rightsMatrix = authRemote.getUserSecurityProfileMatrix(userObject
            .getIndividualID());

        if (!rightsMatrix.isModuleVisible("Synchronize")) {
          return (false);
        }

        userPrefs.setModuleAuthorizationMatrix(rightsMatrix);
        userObject.setUserPref(userPrefs);

        // now, save the userObject in the request session
        session.setAttribute("userobject", userObject);

        // print the server date/time and sessionID
        Date date = new Date();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormatter.format(date);

        writer.print(currentDateTime + "\n" + sessionID);
        return (true);
      }
      return (false);
    } catch (Exception e) {
      logger.error("[Exception] SyncFacade.doLogin ", e);
      return (false);
    } // end try/catach
  } // end doLogin()

  /*
   * ********************************* ACTIVITIES RELATED CODE STARTS *
   * *********************************
   */

  /**
   * ****************************** Appointment Start
   * *******************************
   */

  /**
   * This method adds appointment to DataBase
   */
  public String addActivity(ActivityForm form, int userId) throws CommunicationException,
      NamingException
  {
    int results = 0;

    ActivityVOX activityVOX = new ActivityVOX(form);
    ActivityVO activityVO = activityVOX.getVO();
    ActivityFacadeHome activityFacade = (ActivityFacadeHome)CVUtility.getHomeObject(
        "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");

    try {
      String entityName = form.getLinkCompany();
      if (entityName != null && !entityName.equals("")) {
        // first, check to see if a entity with a matching name exists
        // if yes, then associate this invidivual with that entity
        // if no, then create a new entity, and associate this individual with
        // that entity
        SyncFacadeHome syncHome = (SyncFacadeHome)CVUtility.getHomeObject(
            "com.centraview.syncfacade.SyncFacadeHome", "SyncFacade");
        com.centraview.syncfacade.SyncFacade sfremote = syncHome.create();
        sfremote.setDataSource(dataSource);
        int newEntityID = sfremote.findCompanyNameMatch(entityName, userId);
        activityVO.setEntityID(newEntityID);
      }
      ActivityFacade remote = activityFacade.create();
      remote.setDataSource(this.dataSource);
      results = remote.addActivity(activityVO, userId);
    } catch (Exception e) {
      logger.error("[Exception] SyncFacade.addActivity ", e);
      return ("FAIL");
    }
    return Integer.toString(results);
  }

  /**
   * This method update the appointment to DataBase
   */
  public String updateActivity(ActivityForm form, int userId) throws CommunicationException,
      NamingException
  {
    ActivityVOX activityVOX = new ActivityVOX(form);
    ActivityVO activityVO = activityVOX.getVO();
    ActivityFacadeHome activityFacade = (ActivityFacadeHome)CVUtility.getHomeObject(
        "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
    try {
      ActivityFacade remote = activityFacade.create();
      remote.setDataSource(this.dataSource);
      remote.updateActivity(activityVO, userId);
    } catch (Exception e) {
      logger.error("[Exception] SyncFacade.updateActivity ", e);
      return ("FAIL");
    }
    return ("OK");
  }

  /**
   * Returns a fully populated <code>ActivityVO</code> object representing the
   * Activity record for the given <code>activityId</code>.
   * @param activityId The activityID of the activity record being requested.
   * @param userId The <strong>IndividualID</strong> of the user who is
   *          requesting the data.
   * @return A fully populated <code>ActivityVO</code> object
   */
  public ActivityVO getActivity(int activityId, int userId)
      throws CommunicationException, NamingException
  {
    ActivityVO vo = null;
    ActivityFacadeHome activityFacade = (ActivityFacadeHome)CVUtility.getHomeObject(
        "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");

    try {
      ActivityFacade remote = activityFacade.create();
      remote.setDataSource(this.dataSource);
      vo = remote.getActivity(activityId, userId);
    } catch (Exception e) {
      logger.error("[Exception] SyncFacade.getActivity ", e);
    }
    return vo;
  }

  /**
   * I have added this method to get the ActivityType of an existing activity.
   * The current scheme insists that you know the activity type BEFORE obtaining
   * the Activity Information.
   * @param activityID The ID of the activity.
   * @return An int with the activity type. -1 if the activity type isn't set.
   */
  public int getActivityType(int activityID)
  {
    int returnValue = -1;
    try {
      ActivityFacadeHome activityFacade = (ActivityFacadeHome)CVUtility.getHomeObject(
          "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
      ActivityFacade remote = activityFacade.create();
      remote.setDataSource(this.dataSource);
      returnValue = remote.getActivityType(activityID);
    } catch (Exception e) {
      logger.error("[Exception] SyncFacade.getActivityType ", e);
    }
    return returnValue;
  } // end of getActivityType method

  /*
   * this method delete the activity // OK for success // FAIL for failure
   */
  public String deleteActivity(int individualID, String activityIDString, boolean adminstratorUserFlag) throws CommunicationException, NamingException
  {
    ActivityFacadeHome aa = (ActivityFacadeHome)CVUtility.getHomeObject(
        "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
    try {
      int activityID = Integer.parseInt(activityIDString);
      AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject(
          "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
      Authorization authorizationLocal = authHome.create();
      authorizationLocal.setDataSource(dataSource);
      HashMap ownerInfo = authorizationLocal.getOwner("Activities", activityID);
      String ownerIDString = ownerInfo.get("id").toString();
      int ownerID = -1;
      if (ownerIDString != null && !ownerIDString.equals("")) {
        ownerID = Integer.parseInt(ownerIDString);
      }

      ActivityFacade remote = aa.create();
      remote.setDataSource(this.dataSource);
      if (adminstratorUserFlag == true && ownerID != -1 && individualID != ownerID) {
        remote.deleteIndividualFromAttendee(activityID, individualID);
      } else {
        remote.deleteActivity(activityID, individualID);
      }
    } catch (Exception e) {
      logger.error("[Exception] SyncFacade.deleteActivity ", e);
      return ("FAIL");
    }
    return activityIDString;
  }

  /*
   * ******************************* ACTIVITIES RELATED CODE ENDS *
   * *******************************
   */

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

} // end class SyncFacade definition
