/*
 * $RCSfile: DuplicateProjectHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:28 $ - $Author: mking_cv $
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

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


public class DuplicateProjectHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    try
    {

      HttpSession session = request.getSession(true);
      ProjectFacadeHome pfh = (ProjectFacadeHome) 
        CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
      ProjectFacade remote = (ProjectFacade) pfh.create();
      remote.setDataSource(dataSource);
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();
      String[] rowId = null;

      if (request.getParameterValues("rowId") != null)
      {
        rowId = request.getParameterValues("rowId");
      }

      ProjectVO projectVO = remote.getProject(Integer.parseInt(rowId[0]), individualID);
      ProjectForm dynaForm = (ProjectForm) form;

      if (projectVO.getEntityID() != 0)
      {
        dynaForm.setEntity(projectVO.getEntityName());
        dynaForm.setEntityid(projectVO.getEntityID());
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
        dynaForm.setTitle(projectVO.getTitle());
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

      if (projectVO.getOwner() != 0)
      {
        dynaForm.setManager(projectVO.getOwnerName());
        dynaForm.setManagerID(projectVO.getOwner());
      }
      
	  Vector statusCol = remote.getProjectStatusList();
	  dynaForm.setProjectStatusVec(statusCol);
	  
      if (projectVO.getGroupName() != null)
      {
        dynaForm.setTeam(projectVO.getGroupName());
      }

      if (projectVO.getStart() != null)
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(projectVO.getStart());
        dynaForm.setStartday("" + calendar.get(Calendar.DAY_OF_MONTH));
        dynaForm.setStartmonth("" + (calendar.get(Calendar.MONTH) + 1));
        dynaForm.setStartyear("" + calendar.get(Calendar.YEAR));
      }

      if (projectVO.getEnd() != null)
      {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(projectVO.getEnd());
        dynaForm.setEndday("" + calendar.get(Calendar.DAY_OF_MONTH));
        dynaForm.setEndmonth("" + (calendar.get(Calendar.MONTH) + 1));
        dynaForm.setEndyear("" + calendar.get(Calendar.YEAR));
      }

      if (projectVO.getBudgetedHours() != 0)
      {
        dynaForm.setBudhr("" + projectVO.getBudgetedHours());
      }
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
      returnStatus = ".view.projects.new.project";
    }
    catch (Exception e)
    {
      System.out.println("[Exception] DuplicateProjectHandler.execute: " + e.toString());
      //e.printStackTrace();
    }

    return (mapping.findForward(returnStatus));
  } // end of method
} // end of class
