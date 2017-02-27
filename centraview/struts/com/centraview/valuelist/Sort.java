/*
 * $RCSfile: Sort.java,v $    $Revision: 1.3 $  $Date: 2005/08/01 20:02:17 $ - $Author: mcallist $
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

package com.centraview.valuelist;

import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class Sort extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    int page = Integer.valueOf(request.getParameter("page")).intValue();
    int rpp = Integer.valueOf(request.getParameter("rpp")).intValue();
    int listType = Integer.valueOf(request.getParameter("listType")).intValue();
    int sortColumn = Integer.valueOf(request.getParameter("sortColumn")).intValue();
    String sortDirection = request.getParameter("sortDirection");
    ValueListParameters parameters = new ValueListParameters(listType, rpp, page);
    parameters.setSortColumn(sortColumn);
    parameters.setSortDirection(sortDirection);
    request.setAttribute("listParameters", parameters);
    String scope = null;
    Iterator iter = ValueListConstants.listTypeMap.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String)iter.next();
      int value = ((Integer)ValueListConstants.listTypeMap.get(key)).intValue();
      if (value == listType) {
        scope = key;
      }
    }
    if (scope != null && scope.equals("MultiActivity")) {
      scope = "All";
    }
    request.setAttribute("subScope", scope);
    ActionForward forward = null;
    // All related info lists forward to the same place regardless of listType.
    if (request.getParameter("relatedInfo") != null) {
      forward = mapping.findForward("relatedInfo");
    } else {
      forward = ActionUtil.listForward(listType, mapping);
    }
    return forward;
  }
}