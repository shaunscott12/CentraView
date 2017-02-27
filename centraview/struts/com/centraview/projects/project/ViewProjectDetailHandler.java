/*
 * $RCSfile: ViewProjectDetailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:29 $ - $Author: mking_cv $
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
import com.centraview.common.UserObject;
import com.centraview.projects.helper.ProjectVO;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;

public class ViewProjectDetailHandler extends org.apache.struts.action.Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    request.setAttribute("recordType", "Project");
    String returnStatus = "failure";
    int iBudHrs = 0;
    float iUsedHrs = 0;
    String rowId = request.getParameter("rowId");
    if(rowId == null){
    	rowId = request.getParameter("projectid");
    }

    HttpSession session = request.getSession(true);
    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();

    ProjectForm dynaForm = (ProjectForm) form;
    ProjectVO projectVO = null;

    ProjectFacadeHome pfh = (ProjectFacadeHome) 
      CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");

    try
    {
      ProjectFacade remote = (ProjectFacade) pfh.create();
      remote.setDataSource(dataSource);
      projectVO = remote.getProject(Integer.parseInt(rowId), individualID);

      dynaForm.setProjectid(Integer.parseInt(rowId));
      request.setAttribute("recordId", String.valueOf(rowId));
      if (projectVO.getEntityID() != 0)
      {
        dynaForm.setEntity(projectVO.getEntityName());
        dynaForm.setEntityid(projectVO.getEntityID());
        request.setAttribute("parentId", String.valueOf(projectVO.getEntityID()));
        request.setAttribute("parentName", java.net.URLEncoder.encode(projectVO.getEntityName(), "ISO-8859-1"));
      }

      if (projectVO.getContactID() != 0)
      {
        dynaForm.setContact(projectVO.getContactName());
        dynaForm.setContactID(projectVO.getContactID());
      }

      if (projectVO.getGroupID() != 0)
      {
        dynaForm.setTeam(projectVO.getGroupName());
        dynaForm.setTeamID(projectVO.getGroupID());
      }

      if (projectVO.getTitle() != null)
      {
        String projectTitle = projectVO.getTitle();
        request.setAttribute("recordName", projectTitle);
        dynaForm.setTitle(projectTitle);
      }

      if (projectVO.getDescription() != null)
      {
        dynaForm.setDescription(projectVO.getDescription());
      }

      if (projectVO.getEntityName() != null)
      {
        dynaForm.setEntity(projectVO.getEntityName());
      }

      if (projectVO.getContactName() != null)
      {
        dynaForm.setContact(projectVO.getContactName());
      }

      if (projectVO.getManagerID() != 0)
      {
        dynaForm.setManager(projectVO.getManager());
        dynaForm.setManagerID(projectVO.getManagerID());
      }

      if (projectVO.getGroupName() != null)
      {
        dynaForm.setTeam(projectVO.getGroupName());
      }

      if (projectVO.getStart() != null)
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(projectVO.getStart());
        dynaForm.setStartday(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        dynaForm.setStartmonth(String.valueOf((calendar.get(Calendar.MONTH) + 1)));
        dynaForm.setStartyear(String.valueOf(calendar.get(Calendar.YEAR)));
      }

      if (projectVO.getEnd() != null)
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(projectVO.getEnd());
        dynaForm.setEndday(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        dynaForm.setEndmonth(String.valueOf((calendar.get(Calendar.MONTH) + 1)));
        dynaForm.setEndyear(String.valueOf(calendar.get(Calendar.YEAR)));
      }

	  Vector statusCol = remote.getProjectStatusList();
	  dynaForm.setProjectStatusVec(statusCol);
	  
      if (projectVO.getBudgetedHours() != 0)
      {
        iBudHrs = projectVO.getBudgetedHours();
        dynaForm.setBudhr("" + iBudHrs);
      }

      SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy - h:mm a");

      if (projectVO.getCreated() != null)
      {
      	dynaForm.setCreatedOn(dateFormat.format(projectVO.getCreated()));
      }

      if (projectVO.getModified() != null)
      {
      	dynaForm.setModifiedOn(dateFormat.format(projectVO.getModified()));
      }

      if (projectVO.getCreatorName() != null)
      {
      	dynaForm.setCreatorName(projectVO.getCreatorName());
      }

      if (projectVO.getModifierName() != null)
      {
      	dynaForm.setModifierName(projectVO.getModifierName());
      }

      iUsedHrs = projectVO.getUsedHours();
      
      dynaForm.setUsedHours((int) iUsedHrs);
      dynaForm.setAvailable((int) (iBudHrs - iUsedHrs));

      request.setAttribute("projectid", rowId);
      HashMap hhmm = projectVO.getStat();

      if (hhmm != null)
      {
        String[] mys = new String[hhmm.size()];

        if (hhmm != null)
        {
          Iterator it = hhmm.keySet().iterator();
          int k = 0;

          while (it.hasNext())
          {
            Object o = it.next();
            mys[k] = "" + ((Integer) o).intValue();
            k++;
          }
        }
      }

      dynaForm.setStatus(("" + projectVO.getStatusID()));
      request.setAttribute("statusmap", hhmm);
      returnStatus = ".view.projects.edit.project";
    }
    catch (Exception e)
    {
      System.out.println("[Exception] ViewProjectDetailHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    return (mapping.findForward(returnStatus));
  }
}
