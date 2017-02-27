/*
 * $RCSfile: DeleteUserViewHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/06 16:52:42 $ - $Author: mcallist $
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

import java.util.HashMap;
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
import com.centraview.view.ViewHome;

public class DeleteUserViewHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(DeleteUserViewHandler.class);
  /**
   * Global Forwards for exception handling
   */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /**
   * To forward to handler - DisplayUserListHandler
   */
  private static final String FORWARD_deleteview = "displaylist";
  /**
   * Redirect constant
   */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /*
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      String currentPage = request.getParameter("currentPage");
      request.setAttribute("pageinfo", currentPage);
      HttpSession session = request.getSession(true);
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();
      long listId = 0;
      if (request.getParameter("listId") != null) {
        listId = Long.parseLong(request.getParameter("listId"));
      }
      int viewId = 0;
      if (request.getParameter("viewId") != null) {
        viewId = Integer.parseInt(request.getParameter("viewId"));
      }
      // get listgenerator instance
      ListGenerator listGenerator = ListGenerator.getListGenerator(dataSource);
      // get displaylist
      DisplayList displayList = listGenerator.getDisplayList(listId);
      // get the listtype
      String listType = displayList.getListType();
      // get listpreference associated with the user for required listtype
      ListPreference listPreference = userObject.getListPreference(listType);
      ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome", "View");
      View remote = viewHome.create();
      remote.setDataSource(dataSource);
      // We will get the current View Id which is Selected for the Individual
      // who is logged in.
      String userDefaultId = "" + listPreference.getDefaultView();
      int userDefaultViewId = 0;
      if (userDefaultId != null && !userDefaultId.equals("")) {
        userDefaultViewId = Integer.parseInt(userDefaultId);
      }

      // Delete the ViewId
      remote.deleteView(viewId);

      // set system default view as user default view if user deletes view which
      // he as set right now.
      int recordPerPage = 0;
      String sortElement = "";
      boolean sortOrder = true;
      String sortType = "A";

      //Remove the View for the List Type
      //So that we can add the View Back
      HashMap listViewHM = listPreference.getViewHashMap();
      listViewHM.remove(String.valueOf(viewId));

      // We will check wheather the removed view was the user Defaulted view.
      // If true then set the default view for the user to System's Default
      // View..
      if (userDefaultViewId == viewId) {
        // Get the System default View Id for the User.
        int systemDefaultViewId = remote.getSystemDefaultView(listType);
        if (systemDefaultViewId != 0) {
          viewId = systemDefaultViewId;
          Vector viewInfoVec = remote.getViewInfo(IndividualId, viewId);
          if (viewInfoVec != null && viewInfoVec.size() > 0) {
            sortType = viewInfoVec.get(0).toString();
            if (sortType.equals("A"))
              sortOrder = true;
            else
              sortOrder = false;

            sortElement = viewInfoVec.get(1).toString();
            recordPerPage = Integer.parseInt(viewInfoVec.get(2).toString());
          }// end of if (systemDefaultViewId != 0)

          // update in database tables - listPreferences
          remote.updateUserDefaultView(viewId, IndividualId, sortType, sortElement, recordPerPage, listType);

          // change the user preference
          listPreference.setDefaultView(viewId);
          listPreference.setRecordsPerPage(recordPerPage);
          listPreference.setSortElement(sortElement);
          listPreference.setSortOrder(sortOrder);

          UserPrefererences userPref = userObject.getUserPref();
          userPref.addListPreferences(listPreference);
          userObject.setUserPref(userPref);
          session.setAttribute("userobject", userObject);
        }
      }
      request.setAttribute("displaylist", displayList);
      request.setAttribute("listId", "" + listId);

      // forward to list handler as per listtype
      FORWARD_final = FORWARD_deleteview;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}