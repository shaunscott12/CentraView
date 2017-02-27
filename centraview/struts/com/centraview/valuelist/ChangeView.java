/*
 * $RCSfile: ChangeView.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 20:02:17 $ - $Author: mcallist $
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

import java.io.IOException;
import java.util.Vector;

import javax.ejb.CreateException;
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
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewHome;

/**
 * Simple action to change the view on a list and then redirect back
 * to the same list.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ChangeView extends Action
{
  private static Logger logger = Logger.getLogger(ChangeView.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, CommunicationException, IOException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String listType = request.getParameter("listType");
    int listTypeId = Integer.parseInt(request.getParameter("listTypeId"));
    int viewId = Integer.parseInt(request.getParameter("viewId"));
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    int individualId = user.getIndividualID();
    ListPreference listPreference = user.getListPreference(listType);
    int rpp = 0;
    String sortElement = "";
    boolean sortOrder = true;
    ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome","View");
    View remote = null;
    try 
    {
      remote = viewHome.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    remote.setDataSource(dataSource);
    Vector viewInfo = remote.getViewInfo(individualId, viewId);
    if (viewInfo != null && viewInfo.size() > 0)
    {
      String sortType = viewInfo.get(0).toString();
      if (sortType.equals("A"))
      {
        sortOrder = true;
      } else {
        sortOrder = false;
      }
      sortElement = viewInfo.get(1).toString();
      rpp = Integer.parseInt(viewInfo.get(2).toString());
    }
    // change the user preference
    listPreference.setDefaultView(viewId);
    listPreference.setRecordsPerPage(rpp);
    listPreference.setSortElement(sortElement);
    listPreference.setSortOrder(sortOrder);
    if (listType.equals("MultiActivity"))
      listType = "All";
    request.setAttribute("subScope", listType);
    return ActionUtil.listForward(listTypeId, mapping);
  }
}
