/*
 * $RCSfile: DisplayUserViewHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

/**
 * DisplayUserViewHandler is display view as per user preference.
 *
 * @author CentraView, LLC.
 */
public class DisplayUserViewHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(DisplayUserViewHandler.class);

  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /**  Redirect constant. */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
	/**
	 *	Executes initialization of required parameters and open window for entry of note
	 *	returns ActionForward
	 */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws Exception
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // initialization of required parameter
    try
    {

      String currentPage = request.getParameter("currentPage");
      request.setAttribute("pageinfo", currentPage);

      // get session
      HttpSession session = request.getSession(true);
      int IndividualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      // get the list id
      long listId = 0;
      if (request.getParameter("listId") != null)
      {
        listId = Long.parseLong(request.getParameter("listId"));
      }
      // get the viewid
      int viewId = 0;
      if (request.getParameter("viewId") != null)
      {
        viewId = Integer.parseInt(request.getParameter("viewId"));
      }

      ListGenerator listGenerator = ListGenerator.getListGenerator(dataSource);
      DisplayList displayList = listGenerator.getDisplayList( listId );
      String listType = displayList.getListType() ;
      UserObject userObject = (UserObject)session.getAttribute( "userobject" );
      ListPreference listPreference = userObject.getListPreference(listType);

      // get the view info from db as per viewId
      int recordPerPage = 0;
      String sortElement = "";
      boolean sortOrder = true;
      String sortType = "A";
      String searchId = "0";

      ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome","View");
      View remote = viewHome.create();
      remote.setDataSource(dataSource);
      Vector viewInfoVec = remote.getViewInfo(IndividualId, viewId);

      if (viewInfoVec != null && viewInfoVec.size() > 0)
      {
        sortType = viewInfoVec.get(0).toString();
        if (sortType.equals("A"))
        {
          sortOrder = true;
        } //end of if statement (sortType.equals("A"))
        else
        {
          sortOrder = false;
        } //end of if statement (sortType.equals("A"))
        sortElement = viewInfoVec.get(1).toString();
        recordPerPage = Integer.parseInt(viewInfoVec.get(2).toString());
        searchId = viewInfoVec.get(3).toString();
      } //end of if statement (viewInfoVec != null && viewInfoVec.size() > 0)

      // update in database tables - listPreferences
      remote.updateUserDefaultView(viewId, IndividualId, sortType, sortElement, recordPerPage, listType);

      // change the user preference
      listPreference.setDefaultView(viewId);
      listPreference.setRecordsPerPage(recordPerPage);
      listPreference.setSortElement(sortElement);
      listPreference.setSortOrder(sortOrder);

      //Set the list options:
      displayList.setRecordsPerPage(recordPerPage);
      displayList.setSortMember(sortElement);
      displayList.setSortType(sortType.charAt(0));

      HashMap listViewHM = listPreference.getViewHashMap();
      ListView listView = (ListView) listViewHM.get(""+viewId);
      listPreference.addListView(listView);

      UserPrefererences userPref = userObject.getUserPref();
      userPref.addListPreferences(listPreference);
      userObject.setUserPref(userPref);
      session.setAttribute("userobject",userObject);

      // set listid
      request.setAttribute("listId", ""+listId);


      if(searchId != null && !searchId.equals("0")){
      	request.setAttribute("searchId",searchId);
      	FORWARD_final = "search";
      } else {
        request.setAttribute("displaylist", displayList);
        session.setAttribute("displaylist", displayList);
      	FORWARD_final = "search" + listType;
      }
      // forward to list handler as per listtype
    } //end of try block
    catch (Exception e)
    {
      logger.error("[execute]: Exception", e);
    } //end of catch block (Exception)
    //System.out.println("[DEBUG] [DisplayUserViewHandler] exit");
    return mapping.findForward(FORWARD_final);
  } //end of execute method
} //end of DisplayUserViewHandler class
