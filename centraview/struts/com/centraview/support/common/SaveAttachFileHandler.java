/*
 * $RCSfile: SaveAttachFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:47 $ - $Author: mking_cv $
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

package com.centraview.support.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class SaveAttachFileHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_success = ".view.support.ticket_detail";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    try {
      int ticketId = 0;
      if (request.getParameter("ticketId") != null) {
        ticketId = Integer.parseInt(request.getParameter("ticketId"));
      }

      if (ticketId == 0) {
        // just give up. no contingency plan here
        return mapping.findForward(FORWARD_success);
      }

      String fileIds = "";
      if (request.getParameter("fileIds") != null) {
        fileIds = (String)request.getParameter("fileIds");
      }
      
      SupportFacadeHome supportFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = (SupportFacade)supportFacade.create();
      remote.setDataSource(dataSource);

      // split string into array
      String[] fileIdArray = fileIds.split(",");
      
      // loop string array
      int[] idArray = new int[fileIdArray.length];
      for (int i = 0; i < fileIdArray.length; i++) {
        try {
          idArray[i] = Integer.parseInt(fileIdArray[i]);
        }catch(NumberFormatException nfe){
          // again, no contingency plan, give up
        }
      }
      
      remote.addFile(ticketId, idArray);
      
      request.setAttribute("ticketID", new Integer(ticketId));
      FORWARD_final = FORWARD_success;
    }catch (Exception e){
      System.out.println("[Exception] SaveAttachFileHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }
  
}

