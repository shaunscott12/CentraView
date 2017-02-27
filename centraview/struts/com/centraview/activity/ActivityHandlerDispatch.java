/*
 * $RCSfile: ActivityHandlerDispatch.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.activity.activityfacade.ActivityFacade;
import com.centraview.activity.activityfacade.ActivityFacadeHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

/**
 * Determines the type of activity being dealt with and forwards to the
 * appropriate view/edit handler. Parameter <strong>rowId</strong> must be
 * supplied. Parameter <strong>typeId</strong> may optionally be supplied to
 * avoid a remote call. Created: Sep 17, 2004
 * @author CentraView, LLC.
 */
public class ActivityHandlerDispatch extends Action {
  private static Logger logger = Logger.getLogger(ActivityHandlerDispatch.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int typeId = 0;
    int rowId = 0;

    String type = request.getParameter("typeId");
    if ((type == null) || (type == "")) {
      String row = request.getParameter("rowId");
      if ((row == null) || (row == "")) {
        throw new ServletException("No activity selected");
      }
      rowId = Integer.parseInt(row);

      ActivityFacadeHome activityFacadeHome = (ActivityFacadeHome)CVUtility.getHomeObject(
          "com.centraview.activity.activityfacade.ActivityFacadeHome", "ActivityFacade");
      try {
        ActivityFacade remote = activityFacadeHome.create();
        remote.setDataSource(dataSource);
        typeId = remote.getActivityType(rowId);
      } catch (CreateException ce) {
        logger.error("[execute] Exception thrown.", ce);
        throw new ServletException(ce);
      }
    } else {
      typeId = Integer.parseInt(type);
    }

    ActionForward forward = null;
    switch (typeId) {
      case ActivityVO.AT_FORCASTED_SALES:
        forward = new ActionForward(
            "/sales/view_opportunity.do?TYPEOFOPERATION=EDIT&OPPORTUNITYID=" + rowId);
        break;
      case ActivityVO.AT_LITRATURE_REQUEST:
        forward = new ActionForward(
            "/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=EDIT&activityid=" + rowId);
        break;
      case ActivityVO.AT_TASK:
        forward = new ActionForward("/projects/view_task.do?rowId=" + rowId);
        break;
      default:
        forward = new ActionForward("/activities/view_activity_detail.do?rowId=" + rowId);
        break;
    }
    return forward;
  }
}
