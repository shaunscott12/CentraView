/*
 * $RCSfile: SaveCustomViewHandler.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:37:48 $ - $Author: mcallist $
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
 
package com.centraview.administration.modulesettings;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.common.ViewForm;
import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewColumnVO;
import com.centraview.view.ViewHome;
import com.centraview.view.ViewVO;

/**
 * SaveCustomViewHandler is used to save / update view entry
 *
 * @author CentraView, LLC.
 */
public class SaveCustomViewHandler extends org.apache.struts.action.Action 
{

  /*	
  *	Global Forwards for exception handling
  */
  public static final String GLOBAL_FORWARD_failure = "failure";
 
  /*	
  *	To forward to jsp -
  */
  private static final String FORWARD_savenewview = "savenewview";

  /** Forward to Save and Apply. */
  private static final String FORWARD_saveapply = "saveapply";
  /*
  *	Redirect constant
  */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  
  /*
  *	Constructor
  */
  public SaveCustomViewHandler() {
  }

  /*
  *	Executes initialization of required parameters and open window for entry of note
  *	returns ActionForward
  */    
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // initialization of required parameter
    int viewId = 0;
    try 
    {
      ViewForm viewForm = (ViewForm)form;

      String listType = "entity";
      String primaryTableName = "";
      if (request.getParameter("listType") != null)
      {
        listType = request.getParameter("listType");  
      } //end of if statement (request.getParameter("listType") != null)
      if (request.getParameter("primarytablename") != null)	
      {
        primaryTableName = request.getParameter("primarytablename");
      } //end of if statement (request.getParameter("primarytablename") != null)

      String module = request.getParameter("module").toString();
	  
	  
	  viewForm.setListType(listType);
      // get userid from session
      HttpSession session = request.getSession();
      int IndividualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      ViewVO viewVO = new ViewVO();
      viewId = 0;

      if (viewForm.getViewId() != null && viewForm.getViewId().length() > 0)	
      {
        viewVO.setViewId(Integer.parseInt(viewForm.getViewId()));
        viewId = Integer.parseInt(viewForm.getViewId());
      } //end of if statement (viewForm.getViewId() != null && viewForm.getViewId().length() > 0)
      viewVO.setListType(viewForm.getListType());
      if (viewForm.getNoOfRecord() != null)
      {
        viewVO.setNoOfRecord(Integer.parseInt(viewForm.getNoOfRecord()));
      } //end of if statement (viewForm.getNoOfRecord() != null)
      if (viewForm.getSearchId() != null)	
      {
        viewVO.setSearchId(Integer.parseInt(viewForm.getSearchId()));
      } //end of if statement (viewForm.getSearchId() != null)
      viewVO.setSortMember(viewForm.getSortMember());
      viewVO.setSortType(viewForm.getSortType());
      viewVO.setSearchType(viewForm.getSearchType());
      viewVO.setViewName(viewForm.getViewName());
      viewVO.setCreatedBy(IndividualId);
      viewVO.setOwner(IndividualId);

      // get listpreference
      // get user object from session		
      UserObject userObject = (UserObject)session.getAttribute( "userobject" );
      // get listpreference associated with the user for required listtype
      ListPreference listPreference = userObject.getListPreference(listType);
      String userDefaultId = ""+listPreference.getDefaultView();

      // set column info
      Vector columnInfo = new Vector();
      Vector selectedColumn = new Vector();
      if (viewForm.getSelectedColumn() != null)
      {
        int sizeOfSelectedColumn = viewForm.getSelectedColumn().length;
        String[] selectedColumnArray = viewForm.getSelectedColumn();
        for (int i = 0; i < sizeOfSelectedColumn; i++)	
        {
          ViewColumnVO columnVO = new ViewColumnVO();
          DDNameValue ddSelectedColumn = new DDNameValue(selectedColumnArray[i], selectedColumnArray[i]);
          columnVO.setColumnOrder(i+1);
          columnVO.setColumnName(selectedColumnArray[i]);
          columnInfo.add(columnVO);
          selectedColumn.add(ddSelectedColumn);
        } //end of for loop (int i = 0; i < sizeOfSelectedColumn; i++)
      } //end of if statement (viewForm.getSelectedColumn() != null)

      viewVO.setViewColumnVO(columnInfo);

      ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome","View");
      View remote =viewHome.create();
      remote.setDataSource(dataSource);

      if (request.getParameter("TYPEOFOPERATION").equals("new"))		
      {
        viewId = remote.addView(IndividualId, viewVO);
        viewForm.setViewId(viewId+"");
        request.setAttribute("TYPEOFOPERATION", "edit");
      } //end of if statement (request.getParameter("TYPEOFOPERATION").equals("ADD"))
      else if (request.getParameter("TYPEOFOPERATION").equals("edit"))	
      {
        remote.updateView(IndividualId, viewVO);
      } //end of else if statement (request.getParameter("TYPEOFOPERATION").equals("EDIT"))

      ListView listView = new ListView(viewId);
      listView.setListType(listType);
      listView.setViewName(viewVO.getViewName());
      listView.setViewID(viewId);
      listView.setSearchID(viewVO.getSearchId());
      if (viewForm.getSelectedColumn() != null)
      {
        int sizeOfSelectedColumn = viewForm.getSelectedColumn().length;
        String[] selectedColumnArray = viewForm.getSelectedColumn();
        for (int i = 0; i < sizeOfSelectedColumn; i++)	
        {
          listView.addColumnName(selectedColumnArray[i]);				
        } //end of for loop (int i = 0; i < sizeOfSelectedColumn; i++)
      } //end of if statement (viewForm.getSelectedColumn() != null)
      listPreference.addListView(listView);

      Vector vecColumn = remote.getAllColumns(listType);
      Vector sortMemberVec = new Vector(vecColumn); 
      viewForm.setSortMemberVec(sortMemberVec);

      int sizeOfAvailableList = vecColumn.size();
      int sizeOfSelectedList = selectedColumn.size();
      int i = 0;
      int j = 0;
      String idOfAvailableList = "";
      String idOfSelectedList = "";
      DDNameValue ddAvailableListInfo = null;
      DDNameValue ddSelectedListInfo = null;
      boolean removed = false;
      while (i < sizeOfAvailableList)	
      {
        removed = false;
        ddAvailableListInfo = (DDNameValue) vecColumn.get(i);
        idOfAvailableList = ddAvailableListInfo.getStrid();
        while (j < sizeOfSelectedList)	
        {
          ddSelectedListInfo = (DDNameValue) selectedColumn.get(j);
          idOfSelectedList = ddSelectedListInfo.getStrid();
          if (idOfAvailableList.equals(idOfSelectedList))	
          {
            vecColumn.remove(i);	
            removed = true;	
            sizeOfAvailableList--;	
            break;		
          } //end of if statement (idOfAvailableList.equals(idOfSelectedList))	
          j++;
          ddSelectedListInfo = null;
        } //end of while loop (j < sizeOfSelectedList)
        j = 0;
        if (!removed)
        {
          i++;
        } //end of if statement (!removed)
        ddAvailableListInfo = null;
        removed = false;
      }	//end of while loop (i < sizeOfAvailableList)
      viewForm.setAvailableColumnVec(vecColumn);
      viewForm.setSelectedColumnVec(selectedColumn);

      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector vecViewList = gml.getViewComboData(listPreference);

      // set the user preference as per updated values
      // String userDefaultId = remote.getUserDefaultView(IndividualId, listType);
      if (userDefaultId != null && userDefaultId.equals(viewForm.getViewId()))	
      {
        // change the user preference
        listPreference.setDefaultView(Integer.parseInt(viewForm.getViewId()));
        listPreference.setRecordsPerPage(Integer.parseInt(viewForm.getNoOfRecord()));
        listPreference.setSortElement(viewForm.getSortMember());
        String sortType = viewForm.getSortType();
        boolean sortOrder = true;
        if (sortType.equals("A"))
        {
          sortOrder = true;
        } //end of if statement (sortType.equals("A"))
        else
        {
          sortOrder = false;		
        } //end of if statement (sortType.equals("A"))
        listPreference.setSortOrder(sortOrder);

        session.setAttribute("userobject",userObject);
      } //end of if statement (userDefaultId != null && userDefaultId.equals(viewForm.getViewId()))	

      request.setAttribute("vecViewList",vecViewList);
      request.setAttribute("viewform", form);
      request.setAttribute("listType", listType);
      request.setAttribute("listId", request.getParameter("listId"));
      request.setAttribute("primaryTableName", primaryTableName);

      if (request.getParameter("savetype").equals("savenewview"))	
      {
	  	request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS); 
		request.setAttribute("typeofsubmodule",module);
		request.setAttribute("module",module);
        FORWARD_final = FORWARD_savenewview;
        viewForm = ViewForm.clearForm(viewForm);
      } //end of else if statement (request.getParameter("savetype").equals("savenewview"))
      else if (request.getParameter("savetype").equals("saveapply"))	
      {
	  	request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS); 
		request.setAttribute("typeofsubmodule",module);
		request.setAttribute("module",module);
        FORWARD_final = FORWARD_saveapply;
      } //end of else if statement (request.getParameter("savetype").equals("saveapply"))
    } //end of try block
    catch (Exception e) 
    {
      System.out.println("[Exception] SaveCustomViewHandler.execute: " + e.toString());
      e.printStackTrace();
    } //end of catch block (Exception)

    ActionForward returnForward = mapping.findForward(FORWARD_final);
    StringBuffer newPathBuffer = new StringBuffer();
    newPathBuffer.append(returnForward.getPath());
    if (FORWARD_final.equals(FORWARD_saveapply))
    {
      newPathBuffer.append("?listId=");
      newPathBuffer.append(request.getParameter("listId"));
      newPathBuffer.append("&viewId=");
      newPathBuffer.append(Integer.toString(viewId));
    } //end of if statment (FORWARD_final.equals(FORWARD_saveapply))
    
    ActionForward newActionForward = new ActionForward();
    newActionForward.setPath(newPathBuffer.toString());
    newActionForward.setRedirect(returnForward.getRedirect());
    newActionForward.setContextRelative(returnForward.getContextRelative());
    newActionForward.setName(returnForward.getName());

    //System.out.println("[DEBUG] [SaveViewHandler] exit");
    return (newActionForward);			
  } //end of execute method
} //end of SaveViewHandler class
