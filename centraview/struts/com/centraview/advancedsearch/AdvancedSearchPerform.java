/*
 * $RCSfile: AdvancedSearchPerform.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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
import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListConstants;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class AdvancedSearchPerform extends Action
{

  private static Logger logger = Logger.getLogger(AdvancedSearchPerform.class);

  // Get the datasource to be passed on to the EJB layer so we
  // all are talking about the same database.

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession(true);
    String returnStatus = "failure";

    // Need the userobject so we know who we are.
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    Integer searchId = new Integer(0);
    int individualId = userObject.getIndividualID();
    if (request.getParameter("searchId") != null) {
      searchId = Integer.valueOf(request.getParameter("searchId"));
    } else if (request.getAttribute("searchId") != null) {
      searchId = Integer.valueOf((String)request.getAttribute("searchId"));
    }

    Integer moduleId = new Integer(0);
    if (request.getParameter("moduleId") != null) {
      moduleId = Integer.valueOf(request.getParameter("moduleId"));
    } else if (request.getAttribute("moduleId") != null) {
      moduleId = Integer.valueOf((String)request.getAttribute("moduleId"));
    }

    int listTypeId = 0;
    if (request.getParameter("listTypeId") != null) {
      listTypeId = Integer.parseInt(request.getParameter("listTypeId"));
    }

    String searchType = "ADVANCE";
    if (request.getParameter("searchType") != null) {
      searchType = request.getParameter("searchType");
    } else if (request.getAttribute("searchType") != null) {
      searchType = (String)request.getAttribute("searchType");
    }

    String searchOn = null;
    if (request.getParameter("searchOn") != null) {
      searchOn = request.getParameter("searchOn");
    } else if (request.getAttribute("searchOn") != null) {
      searchOn = (String)request.getAttribute("searchOn");
    }

    // This is used to clear the search. When moduleId is Zero.
    boolean clearSearch = false;
    if (searchId.intValue() == -1) {
      clearSearch = true;
    }

    boolean valueList = false;
    if (request.getParameter("valueList") != null) {
      valueList = true;
    }

    AdvancedSearch remoteAdvancedSearch = null;
    ArrayList results = new ArrayList();
    if (!clearSearch) {
      try {
        AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
        remoteAdvancedSearch = advancedSearchHome.create();
        remoteAdvancedSearch.setDataSource(dataSource);
        if (searchType != null && searchType.equals("SIMPLE")) {
          searchId = remoteAdvancedSearch.getSearchID(moduleId.intValue());
        }
      } //end of try block
      catch (Exception e) {
        logger.error("[execute] Exception thrown.", e);
        throw new ServletException(e);
      } //end of catch block (Exception)

      // We shouldn't preform the simple search when we have the SearchOn as a
      // Blank String
      // we must have to check for the SearchType Simple. because for the
      // SearchType Advance we will always have the searchOn Null
      if (searchType != null && searchType.equals("SIMPLE") && searchOn != null && searchOn.equals("")) {
      } else {
        results.addAll(remoteAdvancedSearch.performSearch(individualId, searchId.intValue(), searchType, searchOn));
      }
    } // end if (!clearSearch)

    // For debugging.
    if (request.getParameter("debug") != null) {
      request.setAttribute("results", results);
      return (mapping.findForward("displaySearchResults"));
    } //end of if statement (request.getParameter("debug") != null)

    String destinationPath = null;
    if (valueList) {
      // This really sucks, but I'm sick of Activities list BULLSH#T...
      String scope = request.getParameter("subScope");
      if (!(scope == null) && !scope.equals("null") && (moduleId.intValue() == 3 || moduleId.intValue() == 34 || listTypeId == 14)) {
        destinationPath = "/activities/activity_list.do";
      } else {
        destinationPath = (String)ValueListConstants.moduleIdMap.get(moduleId.toString());
      }
      if (!clearSearch) {
        StringBuffer primaryIds = new StringBuffer();
        AdvancedSearchUtil.parseResults(results, primaryIds);
        String filter = null;
        filter = AdvancedSearchUtil.buildAdvancedSearchQuery(moduleId.intValue(), primaryIds.toString(), dataSource);
        filter = filter.substring(9);
        logger.debug("[execute] filter: " + filter);
        session.setAttribute("listFilter", filter);
        destinationPath = destinationPath + "?filter=" + searchId.toString();
      }
    } else {
      logger.error("A non-valuelist Advanced Search has been called: searchId "+searchId+", moduleId "+moduleId+", returnstatus "+returnStatus+".  This is broken!!");
      throw new ServletException("A DisplayList Search was called, this is deprecated, contact your friendly neighborhood J2EE developer.");
    }
    if (!clearSearch) {
      request.setAttribute("appliedSearch", searchId.toString());
    }
    request.setAttribute("superScope", request.getParameter("superScope"));
    request.setAttribute("subScope", request.getParameter("subScope"));
    request.setAttribute("listScope", request.getParameter("listScope"));
    ActionForward destination = new ActionForward(destinationPath);//, clearSearch);
    return (destination);
  } //end of execute method
}
