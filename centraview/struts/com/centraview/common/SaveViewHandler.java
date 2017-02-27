/*
 * $RCSfile: SaveViewHandler.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

package com.centraview.common;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewColumnVO;
import com.centraview.view.ViewHome;
import com.centraview.view.ViewVO;

/**
 * SaveViewHandler is used to save / update view entry
 *
 * @author CentraView, LLC.
 */
public class SaveViewHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(SaveViewHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_saveview = "saveview";
  private static final String FORWARD_savenewview = "savenewview";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int viewId = 0;
    try
    {
      request.setAttribute("TYPEOFOPERATION", request.getParameter("TYPEOFOPERATION"));
      ViewForm viewForm = (ViewForm)form;
	  String currentPage = request.getParameter("currentPage");
	  request.setAttribute("pageinfo", currentPage);

      String listType = "entity";
      String primaryTableName = "";
      if (request.getParameter("listType") != null)
      {
        listType = request.getParameter("listType");
      }
      if (request.getParameter("primarytablename") != null)
      {
        primaryTableName = request.getParameter("primarytablename");
      }
      viewForm.setListType(listType);
      // get userid from session
      HttpSession session = request.getSession();
      int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      ViewVO viewVO = new ViewVO();
      viewId = 0;

      if (viewForm.getViewId() != null && viewForm.getViewId().length() > 0)
      {
        viewVO.setViewId(Integer.parseInt(viewForm.getViewId()));
        viewId = Integer.parseInt(viewForm.getViewId());
      }
      viewVO.setListType(viewForm.getListType());
      if (viewForm.getNoOfRecord() != null)
      {
        viewVO.setNoOfRecord(Integer.parseInt(viewForm.getNoOfRecord()));
      }
      if (viewForm.getSearchId() != null)
      {
        viewVO.setSearchId(Integer.parseInt(viewForm.getSearchId()));
      }
      viewVO.setSortMember(viewForm.getSortMember());
      viewVO.setSortType(viewForm.getSortType());
      viewVO.setSearchType(viewForm.getSearchType());
      viewVO.setViewName(viewForm.getViewName());
      viewVO.setCreatedBy(userId);
      viewVO.setOwner(userId);

      // get listpreference
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      // get listpreference associated with the user for required listtype
      ListPreference listPreference = userObject.getListPreference(listType);
      String userDefaultId = "" + listPreference.getDefaultView();
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
          columnVO.setColumnOrder(i + 1);
          columnVO.setColumnName(selectedColumnArray[i]);
          columnInfo.add(columnVO);
          selectedColumn.add(ddSelectedColumn);
        }
      }
      viewVO.setViewColumnVO(columnInfo);
      ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome", "View");
      View remote = viewHome.create();
      remote.setDataSource(dataSource);
      if (request.getParameter("TYPEOFOPERATION").equals("ADD"))
      {
        viewId = remote.addView(userId, viewVO);
        viewForm.setViewId(viewId + "");
        request.setAttribute("TYPEOFOPERATION", "EDIT");
      } else if (request.getParameter("TYPEOFOPERATION").equals("EDIT")) {
        remote.updateView(userId, viewVO);
      }
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
        }
      }
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
        ddAvailableListInfo = (DDNameValue)vecColumn.get(i);
        idOfAvailableList = ddAvailableListInfo.getStrid();
        while (j < sizeOfSelectedList)
        {
          ddSelectedListInfo = (DDNameValue)selectedColumn.get(j);
          idOfSelectedList = ddSelectedListInfo.getStrid();
          if (idOfAvailableList.equals(idOfSelectedList))
          {
            vecColumn.remove(i);
            removed = true;
            sizeOfAvailableList--;
            break;
          } 
          j++;
          ddSelectedListInfo = null;
        }
        j = 0;
        if (!removed)
        {
          i++;
        }
        ddAvailableListInfo = null;
        removed = false;
      }
      viewForm.setAvailableColumnVec(vecColumn);
      viewForm.setSelectedColumnVec(selectedColumn);
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector vecViewList = gml.getViewComboData(listPreference);
      if (userDefaultId != null && userDefaultId.equals(viewForm.getViewId()))
      {
        listPreference.setDefaultView(Integer.parseInt(viewForm.getViewId()));
        listPreference.setRecordsPerPage(Integer.parseInt(viewForm.getNoOfRecord()));
        listPreference.setSortElement(viewForm.getSortMember());
        String sortType = viewForm.getSortType();
        boolean sortOrder = true;
        if (sortType.equals("A"))
        {
          sortOrder = true;
        } else {
          sortOrder = false;
        }
        listPreference.setSortOrder(sortOrder);
        session.setAttribute("userobject", userObject);
      }
      request.setAttribute("vecViewList", vecViewList);
      request.setAttribute("viewform", form);
      request.setAttribute("listId", request.getParameter("listId"));
      request.setAttribute("primaryTableName", primaryTableName);
      if (request.getParameter("savetype").equals("saveview"))
      {
        FORWARD_final = FORWARD_saveview;
      } else if (request.getParameter("savetype").equals("savenewview")) {
        FORWARD_final = FORWARD_savenewview;
        viewForm = ViewForm.clearForm(viewForm);
      }
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    ActionForward returnForward = mapping.findForward(FORWARD_final);
    StringBuffer newPathBuffer = new StringBuffer();
    newPathBuffer.append(returnForward.getPath());
    ActionForward newActionForward = new ActionForward();
    newActionForward.setPath(newPathBuffer.toString());
    newActionForward.setRedirect(returnForward.getRedirect());
    newActionForward.setName(returnForward.getName());
    return (newActionForward);
  }
}
