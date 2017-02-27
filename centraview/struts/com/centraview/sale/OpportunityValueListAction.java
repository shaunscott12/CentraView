/*
 * $RCSfile: OpportunityValueListAction.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:41 $ - $Author: mking_cv $
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

package com.centraview.sale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * Handle opportunity lists.
 * Created: Sep 8, 2004
 * 
 * @author CentraView, LLC. 
 */
public class OpportunityValueListAction extends Action
{
  private static Logger logger = Logger.getLogger(OpportunityValueListAction.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
    String finalForward = ".view.sales.opportunity.list.my";

    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    //  Check the session for an existing error message (possibly from the delete handler)
    ActionErrors allErrors = (ActionErrors)session.getAttribute("listErrorMessage");
    if (allErrors != null) {
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    ListPreference listPreference = userObject.getListPreference("Opportunity");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));

    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.OPPORTUNITY_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.opportunityViewMap.get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());

      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }

    //Columns and lookups
    String actionType = (String)request.getParameter("actionType");

    //TODO Consolodate listScope and superScope.
    // my pending or all
    String listScope = request.getParameter("listScope");
    if (listScope != null && listScope.equals("all")) {
      finalForward = ".view.sales.opportunity.list.all";
      listScope = "all";
    } else {
      finalForward = ".view.sales.opportunity.list.my";
      listScope = "my";
    }

    //If we are working on the lookup then we can't filter on my opportunity 
    //We have to do it with the help of all opportunity
    //  Search handling
    String filter = null;
    if (actionType != null && actionType.equals("lookup")) {
      listScope = "all";
      filter = " SELECT OpportunityID FROM opportunity ";
    }

    String filterParameter = request.getParameter("filter");
    if (filterParameter != null) {
      filter = (String)session.getAttribute("listFilter");
      if (listScope.equals("my")) {
        filter += " AND Status = '1' AND AccountManager = '" + individualId + "'";
        session.setAttribute("listFilter", filter);
      }
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      if (listScope.equals("my")) // 1 is pending
      {
        filter = "SELECT OpportunityID FROM opportunity WHERE Status = '1' AND AccountManager = '" + individualId + "'";
      }
      session.removeAttribute("listFilter");
    }

    int entityId = 0;
    if (((request.getParameter("entityId")) != null) && (!((request.getParameter("entityId")).equals("")))) {
      entityId = Integer.parseInt(request.getParameter("entityId"));
    }

    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    if (actionType != null && actionType.startsWith("lookup")) {
      Vector lookupViewColumns = new Vector();
      lookupViewColumns.add("Title");
      lookupViewColumns.add("Entity");
      ActionUtil.mapOldView(columns, lookupViewColumns, ValueListConstants.OPPORTUNITY_LIST_TYPE);
      listParameters.setRecordsPerPage(100);
      if (entityId > 0) {
        filter += " WHERE EntityID = " + entityId;
      }
      finalForward = "showLookup";
    } else {
      ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.OPPORTUNITY_LIST_TYPE);
    }
    listParameters.setFilter(filter);
    listParameters.setColumns(columns);

    // Get the list!
    ValueListHome valueListHome = (ValueListHome)CVUtility.getHomeObject("com.centraview.valuelist.ValueListHome", "ValueList");
    ValueList valueList = null;
    try {
      valueList = valueListHome.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    valueList.setDataSource(dataSource);
    ValueListVO listObject = valueList.getValueList(individualId, listParameters);

    session.setAttribute("salesPieChartParams", listParameters);
    session.setAttribute("salesBarChartParams", listParameters);

    ArrayList buttonList = new ArrayList();
    ValueListDisplay displayParameters = null;
    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null)
      moduleList = (HashMap)globalMasterLists.get("moduleList");
    //    For the searchBar
    String moduleID = (String)moduleList.get("Opportunities");
    request.setAttribute("moduleId", moduleID);

    if (actionType != null && actionType.equals("lookup")) {
      buttonList.add(new Button("Select", "select", "lu_selectList('Opportunity');", false));
      buttonList.add(new Button("New Opportunity", "new", "c_newButton('Opportunity', " + moduleID + ")", false));
      displayParameters = new ValueListDisplay(buttonList, false, false);
      displayParameters.setRadio(true);
      listObject.setLookup(true);
      listObject.setLookupType(actionType);
      if (entityId != 0) {
        StringBuffer pageParameter = new StringBuffer();
        pageParameter.append(ValueListConstants.AMP);
        pageParameter.append("entityId=" + entityId);
        listObject.setCurrentPageParameters(pageParameter.toString());
      }
      request.setAttribute("dynamicTitle", "Opportunity");
      request.setAttribute("lookupType", "Opportunity");
    } else {
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
      buttonList.add(new Button("Duplicate", "duplicate", "vl_duplicateList();", false));
      displayParameters = new ValueListDisplay(buttonList, true, true);
    }
    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);

    request.setAttribute("listScope", listScope);
    request.setAttribute("listType", "Opportunity");

    return mapping.findForward(finalForward);
  }
}