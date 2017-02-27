/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
 * appropriate delete/duplicate handler. Parameter <strong>action</strong> will
 * either be 'del' or 'dup'. Parameter <strong>rowId</strong> must be supplied.
 * Parameter <strong>typeId</strong> may optionally be supplied to avoid a
 * remote call. Created: Sep 20, 2004
 * @author CentraView, LLC.
 */
public class ActivityDelAndDupDispatch extends Action {
  private static Logger logger = Logger.getLogger(ActivityDelAndDupDispatch.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int typeId = 0;
    int rowId = 0;

    String op = request.getParameter("op");
    if ((op == null) || (op == "")) {
      throw new ServletException("No operation selected");
    }

    String type = request.getParameter("typeId");
    if ((type == null) || (type == "")) {
      String row = request.getParameter("rowId");
      if (CVUtility.notEmpty(row)) {
        rowId = Integer.parseInt(row);
      } else {
        throw new ServletException("No activity selected");
      }

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
    if (op.equals("dup")) {
      switch (typeId) {
        case ActivityVO.AT_FORCASTED_SALES:
          forward = new ActionForward(
              "/sales/view_opportunity.do?Duplicate=true&TYPEOFOPERATION=ADD&OPPORTUNITYID="
                  + rowId);
          break;
        case ActivityVO.AT_LITRATURE_REQUEST:
          forward = new ActionForward(
              "/marketing/view_literaturefulfillment.do?TYPEOFOPERATION=ADD&activityid=" + rowId);
          break;
        case ActivityVO.AT_TASK:
          forward = new ActionForward("/projects/duplicate_task.do?rowId=" + rowId);
          break;
        default:
          forward = new ActionForward("/activities/duplicate_activity.do?rowId=" + rowId);
          break;
      }
    } else if (op.equals("del")) {
      switch (typeId) {
        case ActivityVO.AT_FORCASTED_SALES:
          forward = new ActionForward("/sales/delete_opportunitylist.do");
          break;
        case ActivityVO.AT_LITRATURE_REQUEST:
          forward = new ActionForward("/marketing/delete_literaturefulfillmentlist.do");
          break;
        case ActivityVO.AT_TASK:
          forward = new ActionForward("/projects/delete_tasklist.do");
          break;
        default:
          forward = new ActionForward("/activities/delete_activitylist.do");
          break;
      }
    } else {
      throw new ServletException("Invalid operation");
    }
    return forward;
  }
}
