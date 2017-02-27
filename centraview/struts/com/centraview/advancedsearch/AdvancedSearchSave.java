/*
 * $RCSfile: AdvancedSearchSave.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:50 $ - $Author: mking_cv $
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

package com.centraview.advancedsearch;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class AdvancedSearchSave extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    // Get the datasource to be passed on to the EJB layer so we
    // all are talking about the same database.
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    // Need the userobject so we know who we are.
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    DynaActionForm advancedSearchForm = (DynaActionForm)form;

    SearchVO searchObject = new SearchVO();
    int moduleId = ((Integer)advancedSearchForm.get("moduleId")).intValue();
    searchObject.setModuleID(moduleId);
    searchObject.setName((String)advancedSearchForm.get("searchName"));
    
    // Maybe we should validate the criteria before we save it, but for-now
    // this is good enough.
    List searchCriteria = Arrays.asList((SearchCriteriaVO[])advancedSearchForm.get("searchCriteria"));
    searchObject.setSearchCriteria(searchCriteria);
    // Build a SearchVO and give it to the EJB layer
    AdvancedSearch remoteAdvancedSearch = null;
    try
    {
      AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
      remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);
    }
    catch (Exception e)
    {
      System.out.println("[Exception][SearchForm.execute] Exception Thrown getting EJB connection: " + e);
      throw new ServletException(e);
    }
    int searchId = Integer.parseInt((String)advancedSearchForm.get("searchId"));
    if (searchId < 1)
    {
      searchId = remoteAdvancedSearch.addNewSearch(individualId, searchObject);
    } else {
      int ownerId = Integer.parseInt((String)advancedSearchForm.get("ownerId"));
      searchObject.setSearchID(searchId);
      searchObject.setOwnerID(ownerId);
      remoteAdvancedSearch.updateSavedSearch(individualId, searchObject);
    }
    
    if (searchId == -1)
    {
      // sumptin went wrong.
      throw new ServletException("Could not create New Search");
    }
    
    String applySearch = (String)advancedSearchForm.get("applySearch");
    advancedSearchForm.set("searchId", String.valueOf(searchId));
    advancedSearchForm.set("saveSearch", "false");
    advancedSearchForm.set("applySearch", "false");

    // Forward to display search with the searchId
    StringBuffer path = new StringBuffer();
    // Get stub URI from mapping (/advancedsearch/dispatch.do?searchId=)
    if (applySearch.equals("true"))
    {
      path.append(mapping.findForward(AdvancedSearchDispatch.APPLYSEARCH).getPath());
      path.append(moduleId);
      if ((advancedSearchForm.get("valueList") != null) &&
          advancedSearchForm.get("valueList").equals("true"))
      {
        path.append("&valueList=true");
      }
      if (advancedSearchForm.get("listScope") != null)
      {
          path.append("&listScope="+advancedSearchForm.get("listScope"));
      }
      path.append("&searchId=");
    } else {
      path.append(mapping.findForward(AdvancedSearchDispatch.DISPLAYSEARCHFORM).getPath());
    }
     // Append the searchId (/advancedsearch/dispatch.do?searchId=searchId) 
    path.append(searchId);
    // Return a new forward based on stub+value  (with a redirect)
    return new ActionForward(path.toString(), true);
  } // end execute()
}
