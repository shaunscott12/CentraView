/*
 * $RCSfile: ValueListPrintHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 20:02:17 $ - $Author: mcallist $
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

package com.centraview.valuelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

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

import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Handles print requests for the new list framework.
 *
 * @author CentraView, LLC. <marc@centrview.com>
 */
public class ValueListPrintHandler extends Action
{
  private static Logger logger = Logger.getLogger(ValueListPrintHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String final_Forward = "printValueList";
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    String listType = request.getParameter("listType");
    ListPreference listPreference = userObject.getListPreference(listType);
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));

    // Sorting
    int listId = ((Integer)ValueListConstants.listTypeMap.get(listType)).intValue();
    ValueListParameters listParameters = new ValueListParameters(listId, Integer.parseInt(request.getParameter("rpp")), Integer.parseInt(request.getParameter("page")));
    listParameters.setSortColumn(Integer.parseInt(request.getParameter("sortColumn")));
    listParameters.setSortDirection(request.getParameter("sortDirection"));

    String listScope = request.getParameter("listScope");
    if (listScope == null)
      listScope = "my";

    // Columns
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();

    ActionUtil.mapOldView(columns, viewColumns,
        ((Integer)ValueListConstants.listTypeMap.get(listType)).intValue());
    listParameters.setColumns(columns);
    if (listType.equals("Rule"))
    {
      int accountID = 0;
      if (request.getParameter("accountID") != null)
      {
        String accountIDString = request.getParameter("accountID");
        accountID = Integer.parseInt(accountIDString);
      }
      listParameters.setAccountID(accountID);
    } else if (listType.equals("Email")) {
      int folderID = 0;
      if (request.getParameter("folderID") != null)
      {
        String folderIDString = request.getParameter("folderID");
        folderID = Integer.parseInt(folderIDString);
      }
      listParameters.setFolderID(folderID);
    }

    // Activity BS
    String superScope = request.getParameter("superScope");
    if (superScope == null || superScope.equals(""))
    {
      superScope = "My";
    }
    boolean all = false;
    if (superScope.equals("All"))
      all = true;

    // Filter
    String filter = null;
    String filterParameter = request.getParameter("filter");
    if (filterParameter != null)
    {
      filter = (String)session.getAttribute("listFilter");
      if (listType.equals("Rule"))
      {
        filter = filterParameter;
      } else if (listType.equals("Ticket")) {
        if (listScope != null && listScope.equalsIgnoreCase("MY"))
        {
          filter += " AND assignedto = " + individualId + " AND ocstatus = 'OPEN'";
        }
      }
      request.setAttribute("appliedSearch", filterParameter);
    } else { // marketing id
      String marketingListId = (session.getAttribute("dbid") != null) ? (String)session.getAttribute("dbid") : "1";
      if (listType.equals("Entity"))
      {
        filter = "SELECT entity.entityId FROM entity WHERE list = " + marketingListId;
      } else if (listType.equals("Individual")) {
        filter = "SELECT individual.individualId FROM individual WHERE list = " + marketingListId;
      } else if (listType.equals("Appointment")) {
        if (all)
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_APPOINTMENT;
        else
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_APPOINTMENT + " AND Owner = " + individualId;
      } else if (listType.equals("Call")) {
        if (all)
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_CALL;
        else
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_CALL + " AND Owner = " + individualId;
      } else if (listType.equals("ForecastSales")) {
        if (all)
          filter = "SELECT OpportunityId FROM opportunity, activity WHERE " +
      			"opportunity.Status = '1'";
        else
          filter = "SELECT OpportunityId FROM opportunity, activity WHERE " +
      			"opportunity.Status = '1' AND activity.Owner = " + individualId;
      } else if (listType.equals("LiteratureFulfillment")) {
        if (!all)
          filter = "SELECT literaturerequest.ActivityId FROM literaturerequest, " +
		      		"activity WHERE activity.Owner = " + individualId + " AND " +
		      		"literaturerequest.ActivityID = activity.ActivityID";
      } else if (listType.equals("Meeting")) {
        if (all)
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_MEETING;
        else
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_MEETING + " AND Owner = " + individualId;
      } else if (listType.equals("NextAction")) {
        if (all)
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_NEXTACTION;
        else
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_NEXTACTION + " AND Owner = " + individualId;
      } else if (listType.equals("ToDo")) {
        if (all)
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_TODO;
        else
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
        	ActivityVO.AT_TODO + " AND Owner = " + individualId;
      } else if (listType.equals("ActivityTask")) {
        if (all)
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
          ActivityVO.AT_TASK;
        else
          filter = "SELECT ActivityId FROM activity WHERE Type = " +
          ActivityVO.AT_TASK + " AND activity.Owner = " + individualId;
      } else if (listType.equals("MultiActivity")) {
        if (all)
          filter = "SELECT ActivityId FROM activity";
        else
          filter = "SELECT ActivityId FROM activity WHERE Owner = " +
          individualId;
      } else if (listType.equals("Opportunity")) {
        if (listScope.equals("all"))
          filter = "SELECT OpportunityID FROM opportunity";
        else
          filter = "SELECT OpportunityID FROM opportunity WHERE Status = 1 " +
          		"AND AccountManager = " + individualId;
      } else if (listType.equals("Note")) {
        if (listScope.equals("all"))
          filter = "SELECT NoteID FROM note";
        else
          filter = "SELECT NoteID FROM note WHERE Owner = " + individualId;
      }
      if (listType.equals("Ticket"))
      {
        if (listScope != null && listScope.equalsIgnoreCase("MY"))
        {
          filter = " SELECT ticketid FROM ticket WHERE assignedto = " + individualId + " AND ocstatus = 'OPEN'";
        }
      }
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);

    // List
    ValueListHome valueListHome = (ValueListHome)CVUtility.getHomeObject("com.centraview.valuelist.ValueListHome", "ValueList");
    ValueList valueList = null;
    try
    {
      valueList = valueListHome.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    valueList.setDataSource(dataSource);
    ValueListVO listObject = valueList.getValueList(individualId, listParameters);

    // Display
    ValueListDisplay displayParameters = new ValueListDisplay(null, false, false);
    displayParameters.setSortable(false);
    displayParameters.setPagingBar(false);
    displayParameters.setLink(false);
    listObject.setDisplay(displayParameters);

    request.setAttribute("valueList", listObject);
    return mapping.findForward(final_Forward);
  }
}
