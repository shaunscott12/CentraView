/*
 * $RCSfile: ViewListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:20 $ - $Author: mking_cv $
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
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.marketing.List.ListVO;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
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

public class ViewListHandler extends Action
{
  private static Logger logger = Logger.getLogger(ViewListHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  	throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String FORWARD_final = ".view.marketing.editlistmanager";

    HttpSession session = request.getSession(true);
    String rowId[] = null;
    String row = null;
    if (request.getParameterValues("rowIdParent") != null)
    {
      rowId = request.getParameterValues("rowIdParent");
    } //end of if statement (request.getParameterValues("rowIdParent") != null)
    else if (request.getParameterValues("marketingListID") != null)
    {
      rowId = request.getParameterValues("marketingListID");
    } //end of else if statement (request.getParameterValues("marketingListID") != null)
	else if (session.getAttribute("marketingListID") != null)
	{
	   row = (String) session.getAttribute("marketingListID");
	   session.removeAttribute("marketingListID");
	} //end of else if statement (getAttribute("marketingListID") != null)    
	else if (request.getParameterValues("listid") != null)
	{
		rowId = request.getParameterValues("listid");
	} //end of else if statement (getAttribute("marketingListID") != null)  
	else
    {
      if (request.getParameterValues("rowId") != null)
      {
        rowId = request.getParameterValues("rowId");
      } //end of if statement (request.getParameterValues("rowId") !=null)
      else if (request.getAttribute("rowId") != null)
      {
        row = (String)request.getAttribute("rowId");
      } //end of else if statement (request.getAttribute("rowId") !=null)
      else
      {
        row = (String)request.getAttribute("groupid");
      } //end of else statement 
    } //end of else statement 

	// After performing the logic in the DeleteHanlder, we are generat a new request for the list
	// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
	// Then destory it after getting the Session value
	if (session.getAttribute("listErrorMessage") != null)
	{
		ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
		saveErrors(request, allErrors);
		session.removeAttribute("listErrorMessage");
	}//end of if (session.getAttribute("listErrorMessage") != null)

    if (rowId != null)
    {
      row = rowId[0];
    } //end of if statement (rowId != null)

    request.setAttribute("rowId", row);

	MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
    try
    {
    	MarketingFacade remote = (MarketingFacade)aa.create();
    	remote.setDataSource(dataSource);
		DynaActionForm dyna = (DynaActionForm)form;
		if (row != null && !row.equals("")){
			int rowID = Integer.parseInt(row);
			ListVO listVO = remote.viewList(rowID);
			dyna.set("listname", listVO.getTitle());
			dyna.set("listdescription", listVO.getDescription());
			dyna.set("listid", listVO.getListID()+"");
			dyna.set("create", listVO.getCreated());
			dyna.set("modify", listVO.getModified());
			dyna.set("owner", listVO.getOwnerID()+"");
			dyna.set("ownername", listVO.getOwnerName());
		}
		request.setAttribute("listFormBean", dyna);
    } //end of try block
    catch (Exception e)
    {
      logger.error("[Exception] ViewListHandler.Execute Handler ", e);
    }

    request.setAttribute("TypeOfOperation", "List Manager");
    request.setAttribute("showAddAttendeesButton", new Boolean(false));
    request.setAttribute("showImportButton", new Boolean(true));

    UserObject userobjectd = (UserObject)session.getAttribute("userobject");
    try
    {
      this.buildSubList(Integer.parseInt(row), userobjectd, request, session, dataSource);
    }
    catch (Exception e)
    {
      logger.error("[Exception] ViewListHandler.Execute Handler ", e);
    }
    
    return mapping.findForward(FORWARD_final);
  }
  
  /**
   * Builds the Sub List for the Marketing Import List Detail Page.
   *
   * @throws Exception Make the calling method deal 
   *  with any exceptions thrown here.
   */
  private void buildSubList(int marketingListId, UserObject userObject, HttpServletRequest request, HttpSession session, String dataSource) throws Exception
  {
    long start = 0L;
    if (logger.isDebugEnabled()) {
      start = System.currentTimeMillis();
    }
 
    int individualId = userObject.getIndividualID();
    ListPreference listPreference = userObject.getListPreference("MarketingListMembers");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request.getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.LISTMEMBER_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the
             // request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.listMemberViewMap.get(listPreference.getSortElement());
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
	  if (marketingListId > 0 ) {
	      filter = filter + " AND list = " + marketingListId;
	  }
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      // Filter by the marketing List.
      filter = "SELECT entity.entityId FROM entity ";
      // We don't want to add filter on the List in following condition
      // 1) if user is seleting the All List will be identified by -1
      //    then we shouldn't apply the filter on list.
      if (marketingListId > 0) {
        filter += " WHERE list = " + marketingListId;
      }
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);
    
    // TODO remove crappy map between old views and new views.
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.LISTMEMBER_LIST_TYPE);
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
    listObject.setCurrentPageParameters(ValueListConstants.AMP+"rowId="+marketingListId);
    ArrayList buttonList = new ArrayList();

    ValueListDisplay displayParameters = null;
 	listObject.setMoveTo(false);
   	buttonList.add(new Button("Import Members", "Import Members", "vl_addMembers();", false));
	buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
	displayParameters = new ValueListDisplay(buttonList, true, true);

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
    request.setAttribute("listType", "MarketingListMembers");
  } //end of buildSubList method
} //end of ViewListHandler class
