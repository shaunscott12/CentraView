/*
 * $RCSfile: AdvancedSearchDelete.java,v $    $Revision: 1.2 $  $Date: 2005/09/06 16:43:12 $ - $Author: mcallist $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class AdvancedSearchDelete extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    // Get the datasource to be passed on to the EJB layer so we
    // all are talking about the same database.
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    DynaActionForm advancedSearchForm = (DynaActionForm)form;
    advancedSearchForm.set("deleteSearch", "false");
    advancedSearchForm.set("createNew", "true");

    Integer searchId = Integer.valueOf(request.getParameter("searchId"));
    if (searchId.compareTo(new Integer(0)) == 0)
    {
      return (mapping.findForward("displaySearchForm"));
    }
    
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

    remoteAdvancedSearch.deleteSearch(searchId.intValue());
    return (mapping.findForward("displaySearchForm"));
  }
}
