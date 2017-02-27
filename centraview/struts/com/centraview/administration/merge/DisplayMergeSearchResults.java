/*
 * $RCSfile: DisplayMergeSearchResults.java,v $    $Revision: 1.2 $  $Date: 2005/07/12 20:53:50 $ - $Author: mcallist $
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

package com.centraview.administration.merge;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This handler is called directly from the MergeSearchResults Handler, which only
 * gets the form-bean and passes the SearchCriteriaVO to the EJB layer, getting the
 * MergeSearchResultVO back and sticking it on the session.
 *
 * This handler is concerned only with grabbing the SearchCriteriaVO from the session
 * and setting up the display List for the display.  It will build the list based on
 * the current grouping handled by the VO
 *
 * @author Kevin McAllister <kevin@centraview.com>
 *
 */
public class DisplayMergeSearchResults extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    HttpSession session = request.getSession(true);
    MergeSearchResultVO searchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult");
    // We should also make sure the details form isn't on the session, so we don't have garbage on the next page.
    session.removeAttribute("mergeEntityDetails");
    int mergeType = searchResult.getMergeType();
    request.setAttribute("mergeType", new Integer(mergeType));
    String next = request.getParameter("next");
    ArrayList currentGroup = null;
    if (next != null && next.equals("true")) {
      currentGroup = searchResult.nextGrouping();
    } else {
      currentGroup = searchResult.currentGrouping();
    }

    if (currentGroup == null) {
      if (next == null) {
        // this is the first time we are seeing this
        currentGroup = new ArrayList();
      } else { // We have done some merging or hit the skip button so we must be done.
        return (mapping.findForward(".view.administration.merge_complete"));
      }
    }
    request.setAttribute("currentGroup", currentGroup);

    return (mapping.findForward(".view.administration.merge_search_results"));
  }
}
