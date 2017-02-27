/*
 * $RCSfile: LoadSearchBarTilesController.java,v $    $Revision: 1.4 $  $Date: 2005/08/04 21:14:18 $ - $Author: mcallist $
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

package com.centraview.struts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.Controller;

import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * 
 * A simple LoadSearchBarTilesController to perform some steps and set it to the request
 * So that it will be very easy to just display it.
 * 
 * @author CentraView, LLC <info@centraview.com>
 */

public final class LoadSearchBarTilesController implements Controller
{
  private static Logger logger = Logger.getLogger(LoadSearchBarTilesController.class);

  public void perform(ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException, IOException
  {} // deprecated but part of the interface so it must be here.
  // hopefully it doesn't need to do anything.

  public void execute(ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException, IOException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(servletContext)).getDataSource();

    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
    Boolean showAdvancedSearch = (request.getAttribute("showAdvancedSearch") == null) ? new Boolean(true) : ((Boolean)request.getAttribute("showAdvancedSearch"));
    request.setAttribute("showAdvancedSearch", showAdvancedSearch);
    Boolean showSimpleSearch = (request.getAttribute("showSimpleSearch") == null) ? new Boolean(true) : ((Boolean)request.getAttribute("showSimpleSearch"));
    request.setAttribute("showSimpleSearch", showSimpleSearch);
    Boolean showCustomViews = (request.getAttribute("showCustomViews") == null) ? new Boolean(true) : ((Boolean)request.getAttribute("showCustomViews"));
    request.setAttribute("showCustomViews", showCustomViews);
    Boolean showComposeButton = (request.getAttribute("showComposeButton") == null) ? new Boolean(false) : ((Boolean)request.getAttribute("showComposeButton"));
    request.setAttribute("showComposeButton", showComposeButton);
    String searchButtonDescription = (request.getAttribute("searchButtonDescription") == null) ? "" : ((String)request.getAttribute("searchButtonDescription"));
    request.setAttribute("searchButtonDescription", searchButtonDescription);
    Boolean showNewButton = (request.getAttribute("showNewButton") == null) ? new Boolean(true) : ((Boolean)request.getAttribute("showNewButton"));
    request.setAttribute("showNewButton", showNewButton);
    Boolean showPrintButton = (request.getAttribute("showPrintButton") == null) ? new Boolean(true) : ((Boolean)request.getAttribute("showPrintButton"));
    request.setAttribute("showPrintButton", showPrintButton);

    // allow the request to change the text on the new button.
    String newButtonValue = (request.getAttribute("newButtonValue") == null) ? "New" : (String)request.getAttribute("newButtonValue");
    request.setAttribute("newButtonValue", newButtonValue);
    int individualId = userObject.getIndividualID();

    // Hopefully listType and the name of the Module match up.
    String moduleId = (String)request.getAttribute("moduleId");
    if (moduleId == null) {
      moduleId = "0";
    }
    request.setAttribute("moduleId", moduleId);

    // temporary hack to make advanced only show up in the working modules
    if (showAdvancedSearch.booleanValue()) {
      try {
        ArrayList searchList = AdvancedSearchUtil.getSavedSearchList(individualId, (Integer.valueOf(moduleId)).intValue(), dataSource);
        request.setAttribute("searchList", searchList);
      } catch (Exception e) {
        throw new ServletException(e);
      }
    }

    String listType = (String)request.getAttribute("listType");
    request.setAttribute("listType", listType);
    if (showCustomViews != null && showCustomViews.booleanValue()) {
      ListPreference listPreference = userObject.getListPreference(listType);
      if (listPreference != null) {
        Vector viewData = globalMasterLists.getViewComboData(listPreference);
        int sizeOfViewDataVector = viewData.size();
        String viewName = "";
        int userDefaultViewID = 0;
        if (sizeOfViewDataVector > 0) {
          userDefaultViewID = listPreference.getDefaultView();
          HashMap listViewHM = listPreference.getViewHashMap();
          ListView listView = (ListView)listViewHM.get(String.valueOf(userDefaultViewID));
          viewName = listView.getViewName();
        }
        request.setAttribute("userDefaultViewID", new Integer(userDefaultViewID));
        request.setAttribute("viewName", viewName);
        request.setAttribute("viewData", viewData);
      } else {
        logger.info("No user preferences for listType: "+listType);
      }
    } //end of if (showCustomViews != null && showCustomViews.booleanValue())
  } //end of perform(ComponentContext context, HttpServletRequest request,.....
} //end of LoadSearchBarTilesController