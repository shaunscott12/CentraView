/*
 * $RCSfile: DeleteEventAttendeesHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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

package com.centraview.marketing;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListGenerator;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public final class DeleteEventAttendeesHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {

      String rowId[] = null;
      String listType = null;

      //added by sameer
      HttpSession session = request.getSession(true);

      rowId = request.getParameterValues("rowId");
      listType = request.getParameter("listType");

      String listId = request.getParameter("listId");

      long idd = 0;
      if (listId != null)
        idd = Long.parseLong(listId);

      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      DisplayList DL = lg.getDisplayList(idd);

      DisplayList displaylistSession = (DisplayList)session.getAttribute("displaylist");
      if ((displaylistSession != null) && (displaylistSession.getListID() == idd))
        displaylistSession.setDirtyFlag(true);

      String eventID = request.getParameter("eventid").toString();

      int iCount;

      HashMap hm = new HashMap();
      hm.put("eventid", eventID);
      hm.put("individualid", rowId);

      MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = (MarketingFacade)aa.create();
      remote.setDataSource(dataSource);
      iCount = remote.deleteEventRegister(hm);

      request.setAttribute("TYPEOFOPERATION", "EDIT");
      request.setAttribute("FromAttendee", "NO");
      request.setAttribute("eventid", eventID);
    }
    catch (Exception e)
    {
      System.out.println("[Exception][DeleteEventAttendeesHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return new ActionForward(request.getParameter("currentPage"), true);
  }
}
