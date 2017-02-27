/*
 * $RCSfile: DisplayEditViewHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/06 16:55:05 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.menu.LeftNavigation;
import com.centraview.common.menu.NestedMenuItem;
import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewHome;
import com.centraview.view.ViewVO;

/**
 * DisplayEditViewHandler is display view information.
 * @author CentraView, LLC.
 */
public class DisplayEditViewHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(DisplayEditViewHandler.class);

  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** To forward to jsp - newedit_view.jsp. */
  private static final String FORWARD_displayeditview = "displayeditview";

  /** Redirect constant. */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /*
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String forwardName = null;
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String currentPage = request.getParameter("currentPage");
    request.setAttribute("pageinfo", currentPage);
    String moduleId = request.getParameter("moduleId");
    request.setAttribute("moduleId", moduleId);
    // initialization of required parameter
    String listType = "entity";

    try {
      request.setAttribute("TYPEOFOPERATION", "EDIT");
      int selectedViewId = 0;
      if (request.getParameter("viewId") != null) {
        selectedViewId = Integer.parseInt(request.getParameter("viewId"));
        if (request.getParameter("listType") != null) {
          listType = request.getParameter("listType").toString();
        } else if (request.getAttribute("listType") != null) {
          listType = request.getAttribute("listType").toString();
        }
        HttpSession session = request.getSession();
        UserObject user = (UserObject) session.getAttribute("userobject");
        int individualId = user.getIndividualID();
        // get data from database
        ViewHome viewHome = (ViewHome) CVUtility.getHomeObject("com.centraview.view.ViewHome",
            "View");
        View remote = viewHome.create();
        remote.setDataSource(dataSource);
        ViewVO viewVO = remote.getView(individualId, selectedViewId);
        ViewForm viewForm = (ViewForm) form;
        if (viewVO != null) {
          viewForm.setViewId("" + viewVO.getViewId());
          viewForm.setViewName(viewVO.getViewName());
          viewForm.setSortMember(viewVO.getSortMember());
          viewForm.setSortType(viewVO.getSortType());
          viewForm.setNoOfRecord("" + viewVO.getNoOfRecord());
          viewForm.setSearchId("" + viewVO.getSearchId());
          viewForm.setSearchType(viewVO.getSearchType());
          viewForm.setSelectedColumnVec(viewVO.getViewColumnVO());
          viewForm.setSearchIdName(viewVO.getSearchIdName());
          viewForm.setDefaultSystemView(viewVO.getDefaultView());
        } // end of if statement (viewVO != null)

        Vector vecColumn = remote.getAllColumns(listType);
        Vector selectedColumn = new Vector();
        if (viewForm.getSelectedColumnVec() != null) {
          selectedColumn = viewForm.getSelectedColumnVec();
        }

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
        while (i < sizeOfAvailableList) {
          removed = false;
          ddAvailableListInfo = (DDNameValue) vecColumn.get(i);
          idOfAvailableList = ddAvailableListInfo.getStrid();
          while (j < sizeOfSelectedList) {
            ddSelectedListInfo = (DDNameValue) selectedColumn.get(j);
            idOfSelectedList = ddSelectedListInfo.getStrid();
            if (idOfAvailableList.equals(idOfSelectedList)) {
              vecColumn.remove(i);
              removed = true;
              sizeOfAvailableList--;
              break;
            }
            j++;
            ddSelectedListInfo = null;
          } // end of while loop (j < sizeOfSelectedList)
          j = 0;
          if (!removed) {
            i++;
          } // end of if statement (!removed)
          ddAvailableListInfo = null;
          removed = false;
        } // end of while loop (i < sizeOfAvailableList)
        viewForm.setAvailableColumnVec(vecColumn);
        request.setAttribute("viewform", form);

        // set up the left nav.
        GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
        ListPreference listPreference = user.getListPreference(listType);
        Vector vecViewList = gml.getViewComboData(listPreference);
        String URL = "/common/new_view.do?listType=" + listType + "&moduleId=" + moduleId;
        NestedMenuItem primaryMenuItem = new NestedMenuItem("Views", URL, true);
        ArrayList secondaryMenuItems = new ArrayList();
        Iterator viewIterator = vecViewList.iterator();
        while (viewIterator.hasNext()) {
          DDNameValue view = (DDNameValue) viewIterator.next();
          int viewId = view.getId();
          String viewName = view.getName();
          boolean selected = false;
          if (viewId == selectedViewId) {
            selected = true;
          }
          secondaryMenuItems.add(new NestedMenuItem(viewName, "/common/edit_view.do?viewId="
              + selectedViewId + "&listType=" + listType, selected));
        }
        primaryMenuItem.setItems(secondaryMenuItems);
        ArrayList primaryMenuItems = new ArrayList();
        primaryMenuItems.add(primaryMenuItem);
        LeftNavigation leftNav = new LeftNavigation("Custom Views", primaryMenuItems);
        request.setAttribute("leftNav", leftNav);

        FORWARD_final = FORWARD_displayeditview;
        switch (Integer.valueOf(moduleId).intValue()) {
          case 14:
          case 15:
          case 16:
            forwardName = ".view.customview.contacts";
            break;
          case 2:
          case 79:
            forwardName = ".view.customview.email";
            break;
          case 3:
            forwardName = ".view.customview.activities";
            break;
          case 6:
            forwardName = ".view.customview.files";
            break;
          case 5:
            forwardName = ".view.customview.notes";
            break;
          case 30:
          case 31:
            forwardName = ".view.customview.sales";
            break;
          case 32:
          case 33:
          case 34:
          case 35:
            forwardName = ".view.customview.marketing";
            break;
          case 36:
          case 37:
          case 38:
            forwardName = ".view.customview.projects";
            break;
          case 39:
          case 40:
          case 41:
            forwardName = ".view.customview.support";
            break;
          case 42:
          case 56:
          case 43:
          case 44:
          case 45:
          case 46:
          case 47:
          case 48:
          case 50:
            forwardName = ".view.customview.accounting";
            break;
          case 51:
          case 52:
          case 53:
          case 54:
            forwardName = ".view.customview.hr";
            break;
          default:
            forwardName = ".view.customview.contacts";
            break;
        }
      } // end of if statement (request.getParameter("viewId") != null)
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    String forwardPath = mapping.findForward(FORWARD_final).getPath();
    ActionForward forward = new ActionForward(forwardName, forwardPath, false);
    return forward;
  } // end of execute method
} // end of DisplayEditViewHandler class
