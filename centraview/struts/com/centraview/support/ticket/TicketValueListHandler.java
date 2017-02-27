/*
 * $RCSfile: TicketValueListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/14 21:11:43 $ - $Author: mking_cv $
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

package com.centraview.support.ticket;

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
import com.centraview.support.common.SupportConstantKeys;
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
 * The Class is used to process the My Open Ticket vs All Ticket The EJB layer
 * and pass it to the display layer.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class TicketValueListHandler extends Action
{
  private static Logger logger = Logger.getLogger(TicketValueListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
    String finalForward = ".view.support.tickets.my.list";
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);

    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null)
      moduleList = (HashMap)globalMasterLists.get("moduleList");

    long start = 0L;
    if (logger.isDebugEnabled()) {
      start = System.currentTimeMillis();
    }

    HttpSession session = request.getSession(true);

    // Check the session for an existing error message (possibly from the delete
    // handler)
    ActionErrors allErrors = (ActionErrors)session.getAttribute("listErrorMessage");
    if (allErrors != null) {
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    // Check wheather the actionType is it Lookup or not
    String actionType = (String)request.getParameter("actionType");

    String listScope = request.getParameter("listScope");
        
    if (listScope != null && listScope.equals("ALL")) {
      finalForward = ".view.support.tickets.all.list";
      listScope = "ALL";
    } else {
      finalForward = ".view.support.tickets.my.list";
      listScope = "MY";
    }

    if (actionType != null && actionType.equals("lookup")) {
        listScope = "ALL";
    }
    
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    ListPreference listPreference = userObject.getListPreference("Ticket");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.TICKET_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the
             // request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.ticketViewMap.get(listPreference.getSortElement());
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
      if (listScope != null && listScope.equalsIgnoreCase("MY")) {
        filter += " AND assignedto = " + individualId + " AND ocstatus = 'OPEN'";
      }
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      if (listScope != null && listScope.equalsIgnoreCase("MY")) {
        filter = " SELECT ticketid FROM ticket WHERE assignedto = " + individualId + " AND ocstatus = 'OPEN'";
      }
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);

    // TODO remove crappy map between old views and new views.
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    // We the Action Type is Ticket's Lookup then we must have
    // to set the columns which we are going to look
    if (actionType != null && actionType.equals("lookup")) {
      Vector lookupViewColumns = new Vector();
      lookupViewColumns.add("Number");
      lookupViewColumns.add("Subject");
      lookupViewColumns.add("Entity");
      ActionUtil.mapOldView(columns, lookupViewColumns, ValueListConstants.TICKET_LIST_TYPE);
      listParameters.setRecordsPerPage(100);
      finalForward = ".view.support.ticketslookup";
    } else {
      ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.TICKET_LIST_TYPE);
    }
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

    session.setAttribute("ticketPieChartParams", listParameters);
    session.setAttribute("ticketBarChartParams", listParameters);
    ArrayList buttonList = new ArrayList();
    // For the searchBar
    String moduleID = (String)moduleList.get("Ticket");

    ValueListDisplay displayParameters = null;
    if (actionType != null && actionType.equals("lookup")) {
      // When we select the lookup then we should add the Select button to the
      // valueList
      buttonList.add(new Button("Select", "select", "lu_selectList('Ticket');", false));
      buttonList.add(new Button("New Ticket", "new", "c_newButton('Ticket', " + moduleID + ")", false));
      displayParameters = new ValueListDisplay(buttonList, false, false);
      displayParameters.setRadio(true);
      listObject.setLookup(true);
      listObject.setLookupType(actionType);
      request.setAttribute("hideMarketingList", new Boolean(true));
      request.setAttribute("dynamicTitle", "Ticket");
      request.setAttribute("lookupType", "Ticket");
    } else {
      buttonList.add(new Button("View", "view", "vl_viewList();", false));
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
      buttonList.add(new Button("Duplicate", "duplicate", "vl_duplicateList();", false));
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
    request.setAttribute("listScope", listScope);
    request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE, "Ticket");
    request.setAttribute("moduleId", moduleID);
    request.setAttribute("listType", "Ticket");
    return mapping.findForward(finalForward);
  }
}