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

package com.centraview.contacts.individual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.action.ActionMessages;

import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListRow;
import com.centraview.valuelist.ValueListVO;

/**
 * The class called by IndividualList.do which will retreive the list from the
 * EJB layer and pass it to the display layer.
 * @author CentraView, LLC.
 */
public class IndividualValueListHandler extends Action {
  private static Logger logger = Logger.getLogger(IndividualValueListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String final_Forward = ".view.contacts.individuallist";
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);

    // Check the session for an existing error message (possibly from the delete
    // handler)
    ActionMessages allErrors = (ActionMessages)session.getAttribute("listErrorMessage");
    if (allErrors != null) {
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null)
      moduleList = (HashMap)globalMasterLists.get("moduleList");

    String marketingListId = "1";
    String requestMarketingListId = request.getParameter("dbid");
    String sessionMarketingListId = (String)session.getAttribute("dbid");
    if (requestMarketingListId != null) {
      marketingListId = requestMarketingListId;
    } else if (sessionMarketingListId != null) {
      marketingListId = sessionMarketingListId;
    }
    session.setAttribute("dbid", marketingListId);

    // It will be to specify wheather Lookup is for Employee Group Member, Event
    // Attendee
    // If we don't specify any thing that means Individual Lookup
    String actionType = request.getParameter("actionType");

    int entityId = 0;
    if (((request.getParameter("entityId")) != null)
        && (!((request.getParameter("entityId")).equals("")))) {
      entityId = Integer.parseInt(request.getParameter("entityId"));
    }

    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    ListPreference listPreference = userObject.getListPreference("Individual");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request
        .getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.INDIVIDUAL_LIST_TYPE,
          listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the
              // request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.individualViewMap
          .get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }

    // Search handling
    String filter = null;
    String filterParameter = request.getParameter("filter");
    if (filterParameter != null) {
      filter = (String)session.getAttribute("listFilter");
      if (filter == null) {
        filter = "SELECT individual.individualId FROM individual WHERE 1=1 ";
      }
      // We don't want to add filter on the List in following condition
      // 1) if the Action Type is for Employee Lookup
      // 2) if user is seleting the All List will be identified by -1
      // then we shouldn't apply the filter on list.
      if ((actionType == null || (actionType != null && !actionType.equals("lookupEmployee")))
          && (marketingListId != null && !marketingListId.equals("-1"))) {
        filter = filter + " AND list = " + marketingListId;
      }
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      // Filter by the marketing List.
      filter = "SELECT individual.individualId FROM individual WHERE 1=1 ";

      // We don't want to add filter on the List in following condition
      // 1) if the Action Type is for Employee Lookup
      // 2) if user is seleting the All List will be identified by -1
      // then we shouldn't apply the filter on list.
      if ((actionType == null || (actionType != null && !actionType.equals("lookupEmployee")))
          && (marketingListId != null && !marketingListId.equals("-1"))) {
        filter += " AND list = " + marketingListId;
      }
      session.removeAttribute("listFilter");
    }

    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();

    if (actionType != null && actionType.startsWith("lookup")) {
      // start hack for search in lookups
      if (filter == null || filter.equals("")) {
        filter = "SELECT individual.individualId FROM individual WHERE 1=1 ";
      }
      // end hack for search in lookups
      Vector lookupViewColumns = new Vector();
      lookupViewColumns.add("Name");
      lookupViewColumns.add("Entity");
      ActionUtil.mapOldView(columns, lookupViewColumns, ValueListConstants.INDIVIDUAL_LIST_TYPE);
      listParameters.setRecordsPerPage(100);
      if (actionType != null && actionType.equals("lookupEmployee")) {
        filter += " AND Entity=1 ";
      }
      if (actionType.equals("lookup") || actionType.equals("lookup_attendee")) {
        if (entityId != 0) {
          filter += " AND Entity = " + entityId;
        }
      }
      final_Forward = "show" + actionType;
      // start hack for search in lookups
      request.setAttribute("showSearch", new Boolean(true));
      request.setAttribute("jsListType", "15");
      if (request.getParameter("filter") != null) {
        String searchString = request.getParameter("filter");
        filter += " AND (FirstName LIKE '%" + searchString + "%' OR LastName LIKE '%"
            + searchString + "%') ";
      }
      // end hack for search in lookups
    } else {
      ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.INDIVIDUAL_LIST_TYPE);
    }
    listParameters.setColumns(columns);
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

    ValueListDisplay displayParameters = null;

    if (actionType != null && actionType.startsWith("lookup")) {
      displayParameters = new ValueListDisplay(buttonList, false, false);
      if (actionType.equals("lookup_attendee")) {
        if (entityId != 0) {
          StringBuffer parameterValues = new StringBuffer();
          parameterValues.append(ValueListConstants.AMP);
          parameterValues.append("entityId=" + entityId);
          listObject.setCurrentPageParameters(parameterValues.toString());
          request.setAttribute("hideMarketingList", new Boolean(true));
        }
        // When we select the lookup then we should add the Select button to the
        // valueList
        buttonList.add(new Button("Select", "select", "lu_selectList('lookup_attendee');", false));
        buttonList.add(new Button("New Individual", "new", "c_newButton('Individual', 15)", false));
        request.setAttribute("dynamicTitle", "Individual Members");
        request.setAttribute("lookupType", "Individual Members");
        displayParameters.setRadioToCheckBox(true);
      }
      if (actionType.equals("lookup")) {
        if (entityId != 0) {
          StringBuffer parameterValues = new StringBuffer();
          parameterValues.append(ValueListConstants.AMP);
          parameterValues.append("entityId=" + entityId);
          listObject.setCurrentPageParameters(parameterValues.toString());
          request.setAttribute("hideMarketingList", new Boolean(true));
        }
        // When we select the lookup then we should add the Select button to the
        // valueList
        buttonList.add(new Button("Select", "select", "lu_selectList('Individual');", false));
        buttonList.add(new Button("New Individual", "new", "c_newButton('Individual', 15)", false));
        request.setAttribute("dynamicTitle", "Individual");
        request.setAttribute("lookupType", "Individual");
      }
      if (actionType.equals("lookupEmployee")) {
        request.setAttribute("hideMarketingList", new Boolean(true));
        // When we select the lookup then we should add the Select button to the
        // valueList
        if (request.getParameter("lookupFlag") != null) {
          buttonList.add(new Button("Select", "select", "lu_selectList('Employee', '"
              + request.getParameter("lookupFlag") + "');", false));
        } else {
          buttonList.add(new Button("Select", "select", "lu_selectList('Employee');", false));
        }
        // buttonList.add(new Button("New Individual", "new",
        // "c_newButton('Individual', 15)", false));
        String defaultEntityName = java.net.URLEncoder.encode(userObject.getEntityName(),
            "ISO-8859-1");
        buttonList.add(new Button("New Individual", "new",
            "c_openWindow('/contacts/new_individual.do?entityNo=1&entityName=" + defaultEntityName
                + "')", false));
        request.setAttribute("dynamicTitle", "Employee");
        request.setAttribute("lookupType", "Employee");
      }
      displayParameters.setRadio(true);
      listObject.setLookup(true);
      listObject.setLookupType(actionType);
    } else {
      buttonList.add(new Button("View", "view", "vl_viewList();", false));
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
      displayParameters = new ValueListDisplay(buttonList, true, true);
    }
    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);

    // Handle non-deleted users
    ArrayList userList = new ArrayList();
    ArrayList ar = (ArrayList)session.getAttribute("usersNotDeleted");
    session.removeAttribute("usersNotDeleted");
    if (ar != null && !ar.isEmpty()) {
      Iterator it = ar.iterator();
      while (it.hasNext()) {
        String rowId = (String)it.next();
        ArrayList rows = (ArrayList)listObject.getList();
        Iterator iter = rows.iterator();
        while (iter.hasNext()) {
          ValueListRow vlr = (ValueListRow)iter.next();
          if (vlr.getRowId() == Integer.parseInt(rowId)) {
            userList.add(vlr.getRowData().get(2));
          }
        }
        if (userList != null && !userList.isEmpty()) {
          request.setAttribute("usersNotDeleted", userList);
        }
      }
    }
    // Put the MarketingList names on the request so the dropdown on the left
    // can be rendered
    ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    ContactFacade remote = null;
    try {
      remote = cfh.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    remote.setDataSource(dataSource);
    Vector allDBList = remote.getDBList(individualId);
    request.setAttribute("AllDBList", allDBList);
    // For the searchBar
    String moduleID = (String)moduleList.get("Individual");
    request.setAttribute("moduleId", moduleID);
    request.setAttribute("listType", "Individual");
    session.setAttribute("highlightmodule", "contact");

    return mapping.findForward(final_Forward);
  }
}
