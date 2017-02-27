/*
 * $RCSfile: AdvancedSearchDispatch.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:50 $ - $Author: mking_cv $
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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * This action class will simpy review the parameter map
 * and based on the values contained within, will Determine
 * where to forward the request.
 *
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class AdvancedSearchDispatch extends Action
{
  static final String MODULEID = "moduleId";
  static final String DISPLAYSEARCHFORM = "displaySearchForm";
  static final String SEARCHID = "searchId";
  static final String SAVESEARCH = "saveSearch";
  static final String APPLYSEARCH = "applySearch";
  static final String DELETESEARCH = "deleteSearch";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    // Check the parameters and forward to the appropriate action.
    Map parameters = request.getParameterMap();
    if (parameters.containsKey("resetForm")) {
      ((DynaActionForm)form).initialize(mapping);
    }
    String saveSearch = request.getParameter(SAVESEARCH);
    if ((saveSearch != null) && (!saveSearch.equals("false"))) {
      return (mapping.findForward(SAVESEARCH));
    }
    String deleteSearch = request.getParameter(DELETESEARCH);
    if ((deleteSearch != null) && (!deleteSearch.equals("false"))) {
      return (mapping.findForward(DELETESEARCH));
    }
    // Everything else is for the DisplaySearchForm action.
    if (parameters.containsKey(MODULEID) && request.getMethod().equals("GET")) {
      request.setAttribute(MODULEID, request.getParameter(MODULEID));
    }
    if (parameters.containsKey(SEARCHID) && request.getMethod().equals("GET")) {
      request.setAttribute(SEARCHID, request.getParameter(SEARCHID));
    }
    // Last resort forward to the display form
    return (mapping.findForward(DISPLAYSEARCHFORM));
  } // end execute()
}