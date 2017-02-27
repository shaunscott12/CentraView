/*
 * $RCSfile: EditTimeSlipHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:32 $ - $Author: mking_cv $
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

package com.centraview.projects.timeslip;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.projects.helper.TimeSlipVO;
import com.centraview.projects.helper.TimeSlipVOX;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;


/**
 * This handler is used when Adding TimeSlip
 *
 */
public final class EditTimeSlipHandler extends Action
{

  String currentTZ = "";

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String saveandclose = (String) request.getParameter("saveandclose");
    String saveandnew = (String) request.getParameter("saveandnew");
    String returnStatus = "failure";
    HttpSession session = request.getSession(true);
    UserObject userobject = (UserObject) session.getAttribute("userobject");

    currentTZ = userobject.getUserPref().getTimeZone();
	TimeSlipForm timeSlipForm = (TimeSlipForm)form;
    int indvID = 0;

    if (userobject != null)
    {
      indvID = userobject.getIndividualID();
    }

    TimeSlipVOX timeSlipVOX = new TimeSlipVOX(currentTZ, form);
    TimeSlipVO timeSlipVO = timeSlipVOX.getVO();
    ProjectFacadeHome aa = (ProjectFacadeHome)
      CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");

    try
    {
      ProjectFacade remote = (ProjectFacade) aa.create();
      remote.setDataSource(dataSource);
      remote.updateTimeSlip(indvID, timeSlipVO);
    }
    catch (Exception e)
    {
      System.out.println("[Exception] EditTimeSlipHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    
    
    if ((saveandclose != null))
    {
      returnStatus = ".forward.close_window";
    }
    else if ((saveandnew != null))
    {
      returnStatus = ".view.projects.new.timeslip";
    }

    return (mapping.findForward(returnStatus));
  }
}
