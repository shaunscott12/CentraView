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
package com.centraview.contacts.entity;

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
import com.centraview.valuelist.ValueListVO;

/**
 * The Class called by EntityList.do which will retreive the list from The EJB
 * layer and pass it to the display layer.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class EntityValueListHandler extends Action {
  private static Logger logger = Logger.getLogger(EntityValueListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String final_Forward = ".view.contacts.entitylist";
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);

    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null)
      moduleList = (HashMap)globalMasterLists.get("moduleList");

    long start = 0L;
    if (logger.isDebugEnabled()) {
      start = System.currentTimeMillis();
    }

    HttpSession session = request.getSession(true);
    session.setAttribute("highlightmodule", "contact");
    // Check the session for an existing error message (possibly from the delete
    // handler)
    ActionMessages allErrors = (ActionMessages)session.getAttribute("listErrorMessage");
    if (allErrors != null) {
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    // Check wheather the actionType is it Lookup or not
    String actionType = request.getParameter("actionType");

    String marketingListId = "1";
    String requestMarketingListId = request.getParameter("dbid");
    String sessionMarketingListId = (String)session.getAttribute("dbid");
    if (requestMarketingListId != null) {
      marketingListId = requestMarketingListId;
    } else if (sessionMarketingListId != null) {
      marketingListId = sessionMarketingListId;
    }
    session.setAttribute("dbid", marketingListId);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    ListPreference listPreference = userObject.getListPreference("Entity");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request
        .getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.ENTITY_LIST_TYPE, listPreference
          .getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the
      // request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.entityViewMap
          .get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }

    String filter = null;
    String filterParameter = request.getParameter("filter");
    if (filterParameter != null) {
      filter = (String)session.getAttribute("listFilter");

      // We don't want to add filter on the List in following condition
      // 1) if user is seleting the All List will be identified by -1
      // then we shouldn't apply the filter on list.
      if (marketingListId != null && !marketingListId.equals("-1")) {
        filter = filter + " AND list = " + marketingListId;
      }
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      // Filter by the marketing List.
      filter = "SELECT entity.entityId FROM entity ";
      // We don't want to add filter on the List in following condition
      // 1) if user is seleting the All List will be identified by -1
      // then we shouldn't apply the filter on list.
      if (marketingListId != null && !marketingListId.equals("-1")) {
        filter += " WHERE list = " + marketingListId;
      }
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);

    // TODO remove crappy map between old views and new views.
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    // We the Action Type is Entity's Lookup then we must have
    // to set the columns which we are going to look
    if (actionType != null && actionType.equals("lookup")) {
      Vector lookupViewColumns = new Vector();
      lookupViewColumns.add("Name");
      lookupViewColumns.add("PrimaryContact");
      ActionUtil.mapOldView(columns, lookupViewColumns, ValueListConstants.ENTITY_LIST_TYPE);
      listParameters.setRecordsPerPage(100);
      final_Forward = ".view.contacts.entitylookup";
      request.setAttribute("showSearch", new Boolean(true));
      request.setAttribute("jsListType", "14");
      if (request.getParameter("filter") != null) {
        StringBuffer luFilter = new StringBuffer(
            "SELECT entity.entityID FROM entity WHERE entity.name LIKE '%");
        luFilter.append(request.getParameter("filter"));
        luFilter.append("%'");
        listParameters.setFilter(luFilter.toString());
      }
    } else {
      ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.ENTITY_LIST_TYPE);
    }
    listParameters.setColumns(columns);

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
    if (actionType != null && actionType.equals("lookup")) {
      // When we select the lookup then we should add the Select button to the
      // valueList
      buttonList.add(new Button("Select", "select", "lu_selectList('Entity');", false, "label.select", request.getLocale()));
      buttonList.add(new Button("New Entity", "new", "c_newButton('Entity', 14)", false, "label.contact.newentity", request.getLocale()));
      displayParameters = new ValueListDisplay(buttonList, false, false);
      displayParameters.setRadio(true);
      listObject.setLookup(true);
      listObject.setLookupType(actionType);
      request.setAttribute("dynamicTitle", "Entity");
      request.setAttribute("lookupType", "Entity");
    } else {
      listObject.setMoveTo(true);
      buttonList.add(new Button("View", "view", "vl_viewList();", false, "label.view", request.getLocale()));
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false, "label.delete", request.getLocale()));
      displayParameters = new ValueListDisplay(buttonList, true, true);
    }

    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    // Stick the list on the request and then the custom list tag will handle
    // it.
    if (logger.isDebugEnabled()) {
      long debugTime = (System.currentTimeMillis() - start);
      logger.debug("[execute] End to End: " + debugTime + " ms");
      listObject.getParameters().setDebugTime(debugTime);
    }
    request.setAttribute("valueList", listObject);
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
    String moduleID = (String)moduleList.get("Entity");
    request.setAttribute("moduleId", moduleID);
    request.setAttribute("listType", "Entity");
    return mapping.findForward(final_Forward);
  }
}
