/*
 * $RCSfile: SaveThreadHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:29 $ - $Author: mking_cv $
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

package com.centraview.customer.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;
import com.centraview.support.ticket.ThreadVO;

public class SaveThreadHandler extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_savenewthread = ".view.customer.view_ticket";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    try {
      HttpSession session = request.getSession();
      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      DynaActionForm dynaForm = (DynaActionForm)form;
      ThreadVO tVO = new ThreadVO();

      tVO.setTitle(dynaForm.get("title").toString());
      tVO.setTicketId(Integer.parseInt(request.getParameter("ticketId")));
      tVO.setPriorityId(Integer.parseInt((dynaForm.get("priority")).toString()));
      tVO.setDetail((String)dynaForm.get("threaddetail"));
      tVO.setCreatedBy(individualId);
      tVO.setOwner(individualId);

      SupportFacadeHome sfh = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = (SupportFacade)sfh.create();
      remote.setDataSource(dataSource);

      remote.addThread(individualId, tVO);

      FORWARD_final = FORWARD_savenewthread;
    } catch (Exception e) {
      System.out.println("[Exception][SaveThreadHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
  
}

