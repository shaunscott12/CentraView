/*
 * $RCSfile: DisplayNewViewHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/06 19:04:49 $ - $Author: mcallist $
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

public class DisplayNewViewHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(DisplayNewViewHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_displaynewview = "displaynewview";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  /**
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String forwardName = null;
    // initialization of required parameter
    try
    {
      request.setAttribute("TYPEOFOPERATION", "ADD");
      String currentPage = request.getParameter("currentPage");
      request.setAttribute("pageinfo", currentPage);
      // get userid from session
      HttpSession session = request.getSession();
      UserObject user = (UserObject)session.getAttribute("userobject");

      String listType = "Entity";
      if (request.getParameter("listType") != null)
      {
        listType = request.getParameter("listType");
      }
      ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome", "View");
      View remote = viewHome.create();
      remote.setDataSource(dataSource);
      Vector vecColumn = remote.getAllColumns(listType);
      ViewForm viewform = (ViewForm)form;
      viewform.setAvailableColumnVec(vecColumn);
      viewform.setSortMemberVec(vecColumn);
      viewform = ViewForm.clearForm(viewform);
      request.setAttribute("viewform", form);
      String selectedId = request.getParameter("listId"); 
      request.setAttribute("listId", selectedId);
      if (selectedId == null) {
        selectedId = "0";
      }
      String moduleId = request.getParameter("moduleId");
      request.setAttribute("moduleId", moduleId);

      // set up the left nav.
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      ListPreference listPreference = user.getListPreference(listType);
      Vector vecViewList = gml.getViewComboData(listPreference);
      String URL = "/common/new_view.do?listType="+listType+"&moduleId="+moduleId;
      NestedMenuItem primaryMenuItem = new NestedMenuItem("Views", URL, true);
      ArrayList secondaryMenuItems = new ArrayList();
      Iterator viewIterator = vecViewList.iterator();
      while (viewIterator.hasNext()) {
        DDNameValue view = (DDNameValue)viewIterator.next();
        int viewId = view.getId();
        String viewName = view.getName();
        boolean selected = false;
        if (selectedId.equals(String.valueOf(viewId))) {
          selected = true;
        }
        secondaryMenuItems.add(new NestedMenuItem(viewName, "/common/edit_view.do?viewId="+viewId+"&listType="+listType, selected));
      }
      primaryMenuItem.setItems(secondaryMenuItems);
      ArrayList primaryMenuItems = new ArrayList();
      primaryMenuItems.add(primaryMenuItem);
      LeftNavigation leftNav = new LeftNavigation("Custom Views", primaryMenuItems);
      request.setAttribute("leftNav", leftNav);
      FORWARD_final = FORWARD_displaynewview;
      switch(Integer.valueOf(moduleId).intValue()) {
        case 14: 
        case 15:
        case 16:
          forwardName = ".view.customview.contacts"; break;
        case 2:
        case 79:
          forwardName = ".view.customview.email"; break;
        case 3: forwardName = ".view.customview.activities"; break;
        case 6: forwardName = ".view.customview.files"; break;
        case 5: forwardName = ".view.customview.notes"; break;
        case 30:
        case 31:
          forwardName = ".view.customview.sales"; break;
        case 32:
        case 33:
        case 34:
        case 35:
          forwardName = ".view.customview.marketing"; break;
        case 36:
        case 37:
        case 38:
          forwardName = ".view.customview.projects"; break;
        case 39:
        case 40:
        case 41:
          forwardName = ".view.customview.support"; break;
        case 42:
        case 56:
        case 43:
        case 44:
        case 45:
        case 46:
        case 47:
        case 48:
        case 50:
          forwardName = ".view.customview.accounting"; break;
        case 51:
        case 52:
        case 53:
        case 54:
          forwardName = ".view.customview.hr"; break;
        default: 
          forwardName = ".view.customview.contacts"; break;
      }
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    String forwardPath = mapping.findForward(FORWARD_final).getPath();
    ActionForward forward = new ActionForward(forwardName, forwardPath, false);
    return forward;
  }
}