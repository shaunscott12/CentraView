/*
 * $RCSfile: MergeDetail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:44 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This action parses the mergeIds and then forwards to the appropriate
 * detail type, entity or individual.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class MergeDetail extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String forwardMapping = "entityMergeDetail";
    HttpSession session = request.getSession(true);
    MergeSearchResultVO searchResult = (MergeSearchResultVO)session.getAttribute("mergeSearchResult");
    int mergeType = searchResult.getMergeType();
    // Once we get this far we should kill the form bean on the first search page from the session.
    //session.removeAttribute("mergeSearchForm");
      
    String mergeIds = request.getParameter("mergeIds");
    String[] mergeIdArray = mergeIds.split(",");
    session.setAttribute("mergeIdArray", mergeIdArray);
    if (mergeType == 1) {
      forwardMapping = "entityMergeDetail";
    } else {
      forwardMapping = "individualMergeDetail";
    }
    return(mapping.findForward(forwardMapping));
  }    
}
