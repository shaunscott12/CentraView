/*
 * $RCSfile: EmailLookupValueListAction.java,v $    $Revision: 1.3 $  $Date: 2005/07/27 20:17:30 $ - $Author: mcallist $
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

package com.centraview.mail;

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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
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
 * Build a list of individual email addresses for lookup.
 * Created: Nov 4, 2004
 * @author CentraView, LLC.
 */
public class EmailLookupValueListAction extends Action
{
  private static Logger logger = Logger.getLogger(EmailLookupValueListAction.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String finalForward = ".view.email.lookup.list";
    
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
    ListPreference listPreference = userObject.getListPreference("EmailLookup");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));

    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) { // build up new Parameters
      listParameters = new ValueListParameters(ValueListConstants.EMAIL_LOOKUP_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the request.
      listParameters = requestListParameters;
    }
    
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.emailLookupViewMap.get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }

    String marketingListId = request.getParameter("dbid");
    if (marketingListId == null) {
      marketingListId = (String)request.getAttribute("dbid");
      if (marketingListId == null) {
        marketingListId = "1";
      }
    }
    request.setAttribute("dbid", marketingListId);

    // TODO Remove this once the lookup controller is done.
    // This is for the marketinglist drop down.
    try {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = contactFacadeHome.create();
      remote.setDataSource(dataSource);
      Vector marketingLists = remote.getDBList(individualId);
      request.setAttribute("marketingLists", marketingLists);
      request.setAttribute("AllDBList", marketingLists);
    } catch (CreateException ce) {
      logger.error("Exception thrown in EmailLookupValueListAction.", ce);
    }

    Integer marketingListIdInt = new Integer(-1);
    try {
      marketingListIdInt = new Integer(Integer.parseInt(marketingListId));
    } catch (NumberFormatException nfe) {
      // already set default value
    }
    // Currently there is no search on the screen.
    StringBuffer filter = new StringBuffer("");
    // To make sure the count is correct filter against only those individuals with email addresses
    filter.append("SELECT i.individualId FROM individual AS i, mocrelate AS mr, methodofcontact AS moc ");
    filter.append("WHERE i.IndividualID = mr.ContactID AND moc.MOCID = mr.MOCID AND moc.MOCType = 1 AND mr.contactType = 2 ");
    if (marketingListIdInt.intValue() > 0) {
      // don't filter by list if list ID is -1 (shows all lists)
      filter.append("AND i.list = " + marketingListId);
    }
    listParameters.setFilter(filter.toString());

    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.EMAIL_LOOKUP_LIST_TYPE);
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

    ArrayList buttonList = new ArrayList();
    buttonList.add(new Button("Select", "select", "lu_submitAddrs();", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, false, false);
    displayParameters.setSortable(false);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    listObject.setLookup(true);
    listObject.setLookupType("Email");
    request.setAttribute("valueList", listObject);
    // TODO Fix search, a name module and all that goes with it needs to be created.
    // For the searchBar
    request.setAttribute("lookupType", "Email Address");
    request.setAttribute("listType", "EmailLookup");
    return mapping.findForward(finalForward);
  }
}