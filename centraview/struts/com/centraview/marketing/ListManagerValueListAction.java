/*
 * $RCSfile: ListManagerValueListAction.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:14 $ - $Author: mking_cv $
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

package com.centraview.marketing;

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
 * Marketing lists
 * Created: Oct 20, 2004
 * 
 * @author CentraView, LLC. 
 */
public class ListManagerValueListAction extends Action {
  private static Logger logger = Logger.getLogger(ListManagerValueListAction.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String finalForward = ".view.marketing.listmanager.list";
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
    
    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null)
      moduleList = (HashMap)globalMasterLists.get("moduleList");
    
    ListPreference listPreference = userObject.getListPreference("Marketing");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) { // build up new Parameters
      listParameters = new ValueListParameters(ValueListConstants.LISTMANAGER_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.listManagerViewMap.get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder())
        listParameters.setSortDirection("ASC");
      else
        listParameters.setSortDirection("DESC");
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
    
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();    
    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.LISTMANAGER_LIST_TYPE);
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
    buttonList.add(new Button("View", "view", "vl_viewList();", false));
    buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, true);
    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);
    
    //  For the searchBar
    String moduleID = (String)moduleList.get("ListManager");
    request.setAttribute("moduleId", moduleID);
    request.setAttribute("listType", "Marketing");
    
    return mapping.findForward(finalForward);
  }
}
