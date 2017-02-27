/*
 * $RCSfile: NewProjectHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:28 $ - $Author: mking_cv $
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

package com.centraview.projects.project;

import java.io.IOException;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;

public class NewProjectHandler extends org.apache.struts.action.Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
	ProjectFacadeHome phh = (ProjectFacadeHome)CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");	
    try
    {
		HttpSession session = request.getSession(true);
		if (session.getAttribute("highlightmodule") != null)
		{
			session.setAttribute("highlightmodule", "project");
		}
		
		ProjectForm projectForm = new ProjectForm();
		
		if (request.getParameter("entityname") != null)
		{
			projectForm.setEntity((String) request.getParameter("entityname"));
		}
		
		if (request.getParameter("entityid") != null)
		{
			projectForm.setEntityid(Integer.parseInt(request.getParameter("entityid")));
		}
		
		if (request.getParameter("contact") != null)
		{
			projectForm.setContact((String) request.getParameter("contact"));
		}
		
		if (request.getParameter("contactID") != null)
		{
			projectForm.setContactID(Integer.parseInt(request.getParameter("contactID")));
		}
  
		ProjectFacade remote = (ProjectFacade) phh.create();
		remote.setDataSource(dataSource);
		Vector statusCol = remote.getProjectStatusList();
		projectForm.setProjectStatusVec(statusCol);
  
      request.setAttribute("projectForm", projectForm);
    } catch (Exception e)
    {
      System.out.println("[Exception][NewProjectHandler.execute] Exception Thrown: " + e);
      return mapping.findForward("failure");
    }

    return mapping.findForward(".view.projects.new.project");
  }
}