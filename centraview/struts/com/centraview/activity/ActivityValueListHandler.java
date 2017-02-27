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
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.CreateException;
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
import org.apache.struts.action.ActionMessages;

import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.common.EJBUtil;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * Master list handler for Activities. Created: Sep 14, 2004
 * @author CentraView, LLC.
 */
public class ActivityValueListHandler extends Action {
  private static Logger logger = Logger.getLogger(ActivityValueListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String final_Forward = ".view.activities.myactivitylist";
    try {
      String dataSource = Settings.getInstance().getSiteInfo(
          CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
      HttpSession session = request.getSession(true);
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualId = userObject.getIndividualID();

      // Check the session for an existing error message (possibly from the
      // delete handler)
      ActionMessages allErrors = (ActionMessages)session.getAttribute("listErrorMessage");
      if (allErrors != null) {
        saveErrors(request, allErrors);
        session.removeAttribute("listErrorMessage");
      }

      GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
      HashMap moduleList = new HashMap();
      if (globalMasterLists.get("moduleList") != null)
        moduleList = (HashMap)globalMasterLists.get("moduleList");

      // Handle left nav issues.
      String superScope = request.getParameter("superScope");
      String subScope = request.getParameter("subScope");
      if ((superScope == null) || (superScope.equals(""))) {
        superScope = (String)request.getAttribute("superScope");
        if ((superScope == null) || (superScope.equals("")))
          superScope = "My";
      }
      if ((subScope == null) || (subScope.equals(""))) {
        subScope = (String)request.getAttribute("subScope");
        if ((subScope == null) || (subScope.equals("")))
          subScope = "All";
      }
      boolean all = false;
      if (superScope.equals("All"))
        all = true;
      request.setAttribute("superScope", superScope);
      request.setAttribute("subScope", subScope);
      ListPreference listPreference = null;
      ValueListParameters listParameters = null;
      String moduleId = null;
      String filterOnType = null;
      if (subScope.equals("Appointment")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.APPOINTMENT_LIST_TYPE, ValueListConstants.appointmentViewMap, false);
        moduleId = (String)moduleList.get("Activities");
        if (all) {
          filterOnType = " Type = " + ActivityVO.AT_APPOINTMENT;
          final_Forward = ".view.activities.allappointmentslist";
        } else {
          filterOnType = " Type = " + ActivityVO.AT_APPOINTMENT + " AND Owner = " + individualId;
          final_Forward = ".view.activities.myappointmentslist";
        }
      } else if (subScope.equals("Call")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.CALL_LIST_TYPE, ValueListConstants.callViewMap, false);
        moduleId = (String)moduleList.get("Activities");
        if (all) {
          final_Forward = ".view.activities.allcallslist";
          filterOnType = " Type = " + ActivityVO.AT_CALL;
        } else {
          final_Forward = ".view.activities.mycallslist";
          filterOnType = " Type = " + ActivityVO.AT_CALL + " AND Owner = " + individualId;

        }
      } else if (subScope.equals("ForecastSales")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.FORECASTSALES_LIST_TYPE, ValueListConstants.forecastSalesViewMap,
            false);
        moduleId = (String)moduleList.get("Opportunities");
        if (all) {
          filterOnType = " activity.ActivityID= opportunity.ActivityID ";
          final_Forward = ".view.activities.allforecastsaleslist";
        } else {
          final_Forward = ".view.activities.myforecastsaleslist";
          filterOnType = " activity.ActivityID= opportunity.ActivityID AND opportunity.AccountManager = "
              + individualId;
        }
      } else if (subScope.equals("LiteratureFulfillment")) {
        listPreference = userObject.getListPreference("LiteratureFulfillment");
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.LITERATUREREQUEST_LIST_TYPE,
            ValueListConstants.literatureRequestViewMap, false);
        moduleId = (String)moduleList.get("LiteratureFulfilment");
        if (all) {
          final_Forward = ".view.activities.allliteraturerequestslist";
        } else {
          filterOnType = " activity.Owner = " + individualId
              + " AND literaturerequest.ActivityID = activity.ActivityID";
          final_Forward = ".view.activities.myliteraturerequestslist";
        }
      } else if (subScope.equals("Meeting")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.MEETING_LIST_TYPE, ValueListConstants.meetingViewMap, false);
        moduleId = (String)moduleList.get("Activities");
        if (all) {
          filterOnType = " Type = " + ActivityVO.AT_MEETING;
          final_Forward = ".view.activities.allmeetingslist";
        } else {
          filterOnType = " Type = " + ActivityVO.AT_MEETING + " AND Owner = " + individualId;
          final_Forward = ".view.activities.mymeetingslist";
        }
      } else if (subScope.equals("NextAction")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.NEXTACTION_LIST_TYPE, ValueListConstants.nextActionViewMap, false);
        moduleId = (String)moduleList.get("Activities");
        if (all) {
          filterOnType = " Type = " + ActivityVO.AT_NEXTACTION;
          final_Forward = ".view.activities.allnextactionslist";
        } else {
          filterOnType = " Type = " + ActivityVO.AT_NEXTACTION + " AND Owner = " + individualId;
          final_Forward = ".view.activities.mynextactionslist";
        }

      } else if (subScope.equals("ToDo")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.TODO_LIST_TYPE, ValueListConstants.todoViewMap, false);
        moduleId = (String)moduleList.get("Activities");
        if (all) {
          filterOnType = " Type = " + ActivityVO.AT_TODO;
          final_Forward = ".view.activities.alltodoslist";
        } else {
          filterOnType = " Type = " + ActivityVO.AT_TODO + " AND Owner = " + individualId;
          final_Forward = ".view.activities.mytodoslist";
        }

      } else if (subScope.equals("ActivityTask")) {
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.TASK_LIST_TYPE, ValueListConstants.taskViewMap, false);
        moduleId = (String)moduleList.get("Tasks");
        if (all) {
          filterOnType = " activity.Type = " + ActivityVO.AT_TASK;
          final_Forward = ".view.activities.alltaskslist";
        } else {
          filterOnType = " activity.Type = " + ActivityVO.AT_TASK + " AND activity.Owner = "
              + individualId;
          final_Forward = ".view.activities.mytaskslist";
        }
      } else {
        subScope = "MultiActivity";
        listPreference = userObject.getListPreference(subScope);
        listParameters = ActionUtil.valueListParametersSetUp(listPreference, request,
            ValueListConstants.ACTIVITY_LIST_TYPE, ValueListConstants.activityViewMap, false);
        moduleId = (String)moduleList.get("Activities");

        if (all) {
          final_Forward = ".view.activities.allactivitylist";
          filterOnType = " Type != 3 UNION ";
          String opportunityFilter = EJBUtil.buildListFilterQuery(individualId, 30, "opportunity",
              "OpportunityId", "activity.owner", "activityId");
          filterOnType += opportunityFilter;
        } else {
          // We have to eleminate the forcast sale type for my all Activities
          // We have to check wheather that opportunity is assigned to me
          // for that reason we have to build the query seperatly and clubb
          // it with normal query
          filterOnType = " Owner = " + individualId + " AND Type != 3 ";
          filterOnType += " UNION SELECT activity.ActivityId  FROM opportunity, activity WHERE "
              + " activity.ActivityID= opportunity.ActivityID AND opportunity.AccountManager = "
              + individualId;
        }
      }

      // Search handling
      String filter = null;
      String filterParameter = request.getParameter("filter");
      if (filterParameter != null) {
        filter = (String)session.getAttribute("listFilter");
        request.setAttribute("appliedSearch", filterParameter);
        if (filterOnType != null) {
          filter = filter + " AND " + filterOnType;
          // More ghettoness...
          // But there's a fine line between ghetto and genius...
          if (subScope.equals("ForecastSales") || subScope.equals("LiteratureFulfillment")
              || subScope.equals("ActivityTask"))
            filter = filter.replaceFirst("FROM ", "FROM activity, ");
        }
      } else {
        session.removeAttribute("listFilter");
        if (filterOnType != null) {
          if (subScope.equals("ForecastSales")) {
            filter = "SELECT OpportunityId FROM opportunity, activity WHERE" + filterOnType;
          } else if (subScope.equals("LiteratureFulfillment")) {
            filter = "SELECT literaturerequest.ActivityId FROM literaturerequest, activity WHERE";
            if (filterOnType != null) {
              filter += filterOnType;
            }
          } else {
            filter = "SELECT ActivityId FROM activity WHERE" + filterOnType;
          }
        }
      }
      listParameters.setFilter(filter);

      // Get the list!
      ValueListHome valueListHome = (ValueListHome)CVUtility.getHomeObject(
          "com.centraview.valuelist.ValueListHome", "ValueList");
      ValueList valueList = null;
      try {
        valueList = valueListHome.create();
      } catch (CreateException e) {
        logger.error("[execute] Exception thrown.", e);
        throw new ServletException(e);
      }
      valueList.setDataSource(dataSource);
      ValueListVO listObject = valueList.getValueList(individualId, listParameters);

      ArrayList buttonList = new ArrayList();
      buttonList.add(new Button("View", "view", "vl_viewList();", false));
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
      buttonList.add(new Button("Duplicate", "duplicate", "vl_duplicateList();", false));
      ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, true);
      displayParameters.setSortable(true);
      displayParameters.setPagingBar(true);
      displayParameters.setLink(true);
      listObject.setDisplay(displayParameters);
      request.setAttribute("valueList", listObject);

      // For the searchBar
      request.setAttribute("moduleId", moduleId);
      request.setAttribute("listType", subScope);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mapping.findForward(final_Forward);
  }
}
