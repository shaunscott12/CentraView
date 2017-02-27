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

package com.centraview.contacts.group;

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

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
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
 * @author CentraView, LLC.
 */
public final class GroupLookupHandler extends Action
{
  private static Logger logger = Logger.getLogger(GroupLookupHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    int individualId = user.getIndividualID();
    ListPreference listPreference = user.getListPreference("Group");

    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.GROUP_LOOKUP_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.groupLookupViewMap.get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }
    //  Search handling
    String filter = null;
    String filterParameter = request.getParameter("filter");
    if (filterParameter != null) {
      filter = (String)session.getAttribute("listFilter");
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);
    Vector viewColumns = new Vector();
    viewColumns.add("Name");
    ArrayList columns = new ArrayList();
    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.GROUP_LOOKUP_LIST_TYPE);
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
    buttonList.add(new Button("Select", "select", "lu_selectList('group');", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, false, false);
    displayParameters.setRadio(true);
    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("dynamicTitle", "Group");
    request.setAttribute("lookupType", "Group");
    request.setAttribute("valueList", listObject);
    listObject.setLookup(true);
    listObject.setLookupType("Group");
    return mapping.findForward(".view.lookup");
  }
}