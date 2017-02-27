/*
 * $RCSfile: DuplicateProjectsTaskHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:30 $ - $Author: mking_cv $
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

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
import com.centraview.contact.individual.IndividualVO;
import com.centraview.projects.helper.TaskVO;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;


/**
 * @author: Vaijayanti Vaidya
 */
public class DuplicateProjectsTaskHandler extends Action
{

  Iterator it;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    try
    {
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();

      ProjectFacadeHome pfh = (ProjectFacadeHome)
        CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
      ProjectFacade remote = (ProjectFacade) pfh.create();
      remote.setDataSource(dataSource);

      String[] rowId = null;

      if (request.getParameterValues("rowId") != null)
      {
        rowId = request.getParameterValues("rowId");
      }

      TaskVO taskVO = remote.getTask(Integer.parseInt(rowId[0]), individualID);
      TaskForm taskForm = (TaskForm) form;
      this.setForm(taskForm, taskVO, request, response);
      returnStatus = ".view.projects.new.task";
    }
    catch (Exception e)
    {
      System.out.println("[Exception] DuplicateProjectsTaskHandler.execute: " + e.toString());
      //e.printStackTrace();
    }
    return (mapping.findForward(returnStatus));
  } // end of execute method

  public void setForm(TaskForm taskForm, TaskVO taskVO,
    HttpServletRequest request, HttpServletResponse response)
  {
    taskForm.setTitle(taskVO.getTitle());
    taskForm.setDescription(taskVO.getActivityDetails());
    taskForm.setTaskid("" + taskVO.getActivityID());
    taskForm.setProject(taskVO.getProjectName());

    taskForm.setProjectid("" + taskVO.getProjectID());

    if (taskVO.getParentTask() != null)
    {
      taskForm.setParentTask(taskVO.getParentTask());
    }
    else
    {
      taskForm.setParentTask("");
    }
    taskForm.setParenttaskid("" + taskVO.getParentID());

    if (taskVO.getIsMileStone().equalsIgnoreCase("YES"))
    {
      taskForm.setMilestone("Yes");
    }
    else
    {
      taskForm.setMilestone("No");
    }

    HashMap hhmm = taskVO.getAssignedTo();

    if (hhmm != null)
    {
      String[] mys = new String[hhmm.size()];

      if (hhmm != null)
      {
        it = hhmm.keySet().iterator();

        int k = 0;

        while (it.hasNext())
        {
          Object o = it.next();
          mys[k] = "" + ((Integer) o).intValue();
          k++;
        }
      }

      taskForm.setAssignedTo(mys);
    }

    Calendar calendar = Calendar.getInstance();

    if (taskVO.getStart() != null)
    {
      calendar.setTime(taskVO.getStart());
      taskForm.setStartday("" + calendar.get(Calendar.DAY_OF_MONTH));
      taskForm.setStartmonth("" + (calendar.get(Calendar.MONTH) + 1));
      taskForm.setStartyear("" + calendar.get(Calendar.YEAR));
    }

    if (taskVO.getEnd() != null)
    {
      calendar.setTime(taskVO.getEnd());
      taskForm.setEndday("" + calendar.get(Calendar.DAY_OF_MONTH));
      taskForm.setEndmonth("" + (calendar.get(Calendar.MONTH) + 1));
      taskForm.setEndyear("" + calendar.get(Calendar.YEAR));
    }

    taskForm.setAlertTypeAlert("false");
    taskForm.setAlertTypeEmail("false");
    taskForm.setSendAlert("No");

    if ((taskVO.getAlerta() != null) && (taskVO.getAlerta().size() > 0))
    {
      taskForm.setSendAlert("Yes");
      taskForm.setAlertTypeAlert("true");
    }

    if ((taskVO.getEmaila() != null) && (taskVO.getEmaila().size() > 0))
    {
      taskForm.setSendAlert("Yes");
      taskForm.setAlertTypeEmail("true");
    }

    HashMap hmmmm = taskVO.getAlerta();

    if ((hmmmm != null) && (hmmmm.size() > 0))
    {
      Object[] ale = hmmmm.values().toArray();
      String[] s = new String[ale.length];

      for (int h = 0; h < ale.length; h++)
      {
        s[h] = (String) ale[h];
      }

      taskForm.setSendTo(s);
      taskVO.setSendTo(hmmmm);
    }
    else
    {
      hmmmm = taskVO.getEmaila();

      if ((hmmmm != null) && (hmmmm.size() > 0))
      {
        Object[] ale = hmmmm.values().toArray();
        String[] s = new String[ale.length];

        for (int h = 0; h < ale.length; h++)
        {
          s[h] = (String) ale[h];
        }

        taskForm.setSendTo(s);
        taskVO.setSendTo(hmmmm);
      }
    }

    taskForm.setPercentComplete(taskVO.getPercentComplete() + "%");

    IndividualVO ivo = taskVO.getCreatedByVO();

    if (ivo != null)
    {
      taskForm.setCreated(ivo.getFirstName() + " " + ivo.getLastName());
      request.setAttribute("createdby", ivo);
    }

    ivo = taskVO.getModifiedByVO();

    if (ivo != null)
    {
      taskForm.setModified(ivo.getFirstName() + " " + ivo.getLastName());
      request.setAttribute("modifiedby", ivo);
    }

		taskForm.setManager(taskVO.getIndividualName());
		taskForm.setManagerID("" + taskVO.getIndividualID());

    String taskid = taskVO.getTaskid();

    request.setAttribute("sendTo", taskVO.getSendTo());
    request.setAttribute("assignedTo", taskVO.getAssignedTo());
    request.setAttribute("milestone", taskForm.getMilestone());
    request.setAttribute("projectid", "" + taskVO.getProjectID());
    request.setAttribute("projectname", taskVO.getProjectName());
  }
} // end of class
