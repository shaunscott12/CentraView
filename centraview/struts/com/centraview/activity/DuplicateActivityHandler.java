/*
 * $RCSfile: DuplicateActivityHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/20 20:22:18 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.activity.helper.ActivityGenericFillVOX;
import com.centraview.activity.helper.ActivityHelper;
import com.centraview.activity.helper.ActivityHelperHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DuplicateActivityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(DuplicateActivityHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_duplicate = "duplicate";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {

    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      HttpSession session = request.getSession();
      String typeOfActivity = null;

      ActivityHelperHome home = (ActivityHelperHome)CVUtility.getHomeObject(
          "com.centraview.activity.helper.ActivityHelperHome", "ActivityHelper");
      try {
        ActivityHelper remote = home.create();
        remote.setDataSource(dataSource);
        String findActivityId = request.getParameter("rowId");
        typeOfActivity = remote.getTypeOfActivity(Integer.parseInt(findActivityId)).toUpperCase();
      } catch (Exception e) {
        logger.error("[execute]: Exception", e);
      }

      if (typeOfActivity.equals("NEXT ACTION"))
        typeOfActivity = ConstantKeys.NEXTACTION;
      else if (typeOfActivity.equals("TO DO"))
        typeOfActivity = ConstantKeys.TODO;

      // set request to pass to jsp
      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, typeOfActivity);
      // set default opening window to detail sub-type activity
      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, ConstantKeys.DETAIL);
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, ConstantKeys.ADD);

      UserObject userObject = (UserObject)session.getAttribute("userobject");

      int individualId = userObject.getIndividualID();

      String findActivityId = request.getParameter("rowId");
      ActivityVO activityVO = new ActivityVO();
      ActivityFacadeHome aa = (ActivityFacadeHome)CVUtility.getHomeObject(
          "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
      ActivityFacade remote = aa.create();
      remote.setDataSource(dataSource);
      activityVO = remote.getActivity(Integer.parseInt(findActivityId), individualId);

      ActivityForm actFrm = (ActivityForm)form;
      ActivityGenericFillVOX agf = new ActivityGenericFillVOX();
      actFrm.setLocale(request.getLocale());
      actFrm = agf.fillBasicForm(activityVO, actFrm);
      request.setAttribute("activityform", actFrm);
      session.setAttribute("activityform", actFrm);
      FORWARD_final = FORWARD_duplicate;

    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }
}
