/*
 * $RCSfile: NewProjectTaskHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:30 $ - $Author: mking_cv $
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
package com.centraview.projects.task;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/*
Author: Shekhar Chiplunkar
*/ 

public class  NewProjectTaskHandler extends org.apache.struts.action.Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		String title = request.getParameter("title");
		try
		{
			HttpSession session = request.getSession(true);

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "project");
			TaskForm tForm = (TaskForm)form;
				
			if (request.getParameter("taskid") != null)
			{
				tForm.setParenttaskid(""+ request.getParameter("taskid"));
			}
			if (request.getParameter("taskname") != null)
			{
				tForm.setParentTask(""+ request.getParameter("taskname"));
			}
			if (request.getParameter("projectid") != null)
			{
				tForm.setProjectid(""+ request.getParameter("projectid"));
			}
			if (request.getParameter("projectname") != null)
			{
				tForm.setProject(""+ request.getParameter("projectname"));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		return mapping.findForward(".view.projects.new.task");
	}		
}