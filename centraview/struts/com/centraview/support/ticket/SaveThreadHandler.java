/*
 * $RCSfile: SaveThreadHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:53 $ - $Author: mking_cv $
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

package com.centraview.support.ticket;

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
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * This handler is used to add new thread entry.
 */
public class SaveThreadHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_savenewthread = ".forward.close_window";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      DynaActionForm dynaForm = (DynaActionForm)form;
      
      ThreadVO tVO = new ThreadVO();
      tVO.setTitle(dynaForm.get("title").toString());
      tVO.setDetail(dynaForm.get("threaddetail").toString());
      tVO.setTicketId(Integer.parseInt(request.getParameter("ticketId")));
      tVO.setPriorityId(Integer.parseInt((dynaForm.get("priority")).toString()));
      tVO.setCreatedBy(individualID);
      tVO.setOwner(individualID);
      
      SupportFacadeHome sfh = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = (SupportFacade) sfh.create();
      remote.setDataSource(dataSource);
      
      remote.addThread(individualID, tVO);
      
      String closeornew = (String) request.getParameter("closeornew");
      String saveandclose = null;
      String saveandnew = null;
      
      if (closeornew.equals("close")) {
        saveandclose = "saveandclose";
      }else if (closeornew.equals("new")){
        saveandnew = "saveandnew";
        dynaForm.set("title", "");
        dynaForm.set("threaddetail", "");
        return mapping.findForward(".view.support.new_thread");
      }
      
      if (saveandclose != null) {
        request.setAttribute("closeWindow", "true");
      }
      request.setAttribute("refreshWindow", "true");
      FORWARD_final = FORWARD_savenewthread;
    }catch (Exception e){
      System.out.println("[Exception] SaveThreadHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
