/*
 * $RCSfile: ViewProjectTaskDetailHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
 * @author CentraView, LLC.
 */
public class ViewProjectTaskDetailHandler extends Action {
  private static Logger logger = Logger.getLogger(ViewProjectTaskDetailHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    TaskForm taskForm = (TaskForm)form;
    TaskVO taskVO = null;

    try {
      String listID = request.getParameter("listId");
      String rowId = request.getParameter("rowId");
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int userId = userobjectd.getIndividualID();
      String taskid = null;

      taskid = request.getParameter("taskid");

      if (taskid == null) {
        taskid = (String)request.getAttribute("taskid");
      }

      if (taskid == null) {
        taskid = taskForm.getTaskid();
      }

      if (rowId == null) {
        rowId = taskid;
      }

      if (rowId != null) {
        ProjectFacadeHome pfh = (ProjectFacadeHome)CVUtility.getHomeObject(
            "com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
        ProjectFacade remote = pfh.create();
        remote.setDataSource(dataSource);
        taskVO = remote.getTask(Integer.parseInt(rowId), userId);
      } else {
        logger
            .error("[execute]: Trying to retreive Task Object from the EJB, but from the session");
        taskVO = (TaskVO)session.getAttribute("taskForm" + taskid);
      }

      if (taskVO != null) {
        taskForm.setTitle(taskVO.getTitle());
        taskForm.setDescription(taskVO.getActivityDetails());
        taskForm.setTaskid(String.valueOf(taskVO.getActivityID()));
        taskForm.setProject(taskVO.getProjectName());
        taskForm.setProjectid(String.valueOf(taskVO.getProjectID()));
        if (taskVO.getParentTask() != null) {
          taskForm.setParentTask(taskVO.getParentTask());
        } else {
          taskForm.setParentTask("");
        }

        taskForm.setParenttaskid(String.valueOf(taskVO.getParentID()));

        HashMap assignedTo = taskVO.getAssignedTo();
        request.setAttribute("assignedTo", assignedTo);
        if (assignedTo != null) {
          String[] assignedToArray = new String[assignedTo.size()];
          Iterator it = assignedTo.keySet().iterator();
          int k = 0;
          while (it.hasNext()) {
            Integer assigneeId = (Integer)it.next();
            assignedToArray[k++] = assigneeId.toString();
          }
          taskForm.setAssignedTo(assignedToArray);
        }

        if (taskVO.getPercentComplete() != 0) {
          taskForm.setPercentComplete(String.valueOf(taskVO.getPercentComplete()));
        }

        Calendar workingCalendar = Calendar.getInstance();
        if (taskVO.getStart() != null) {
          workingCalendar.setTime(taskVO.getStart());
          taskForm.setStartday(String.valueOf(workingCalendar.get(Calendar.DAY_OF_MONTH)));
          taskForm.setStartmonth(String.valueOf((workingCalendar.get(Calendar.MONTH) + 1)));
          taskForm.setStartyear(String.valueOf(workingCalendar.get(Calendar.YEAR)));
        }

        if (taskVO.getEnd() != null) {
          workingCalendar.setTime(taskVO.getEnd());
          taskForm.setEndday(String.valueOf(workingCalendar.get(Calendar.DAY_OF_MONTH)));
          taskForm.setEndmonth(String.valueOf((workingCalendar.get(Calendar.MONTH) + 1)));
          taskForm.setEndyear(String.valueOf(workingCalendar.get(Calendar.YEAR)));
        }

        taskForm.setAlertTypeAlert("off");
        taskForm.setAlertTypeEmail("off");
        taskForm.setSendAlert("No");

        if (taskVO.getIsMileStone().equalsIgnoreCase("YES")) {
          taskForm.setMilestone("Yes");
        } else {
          taskForm.setMilestone("No");
        }
        if (taskVO.getIsMileStone().equalsIgnoreCase("YES")) {
          if ((taskVO.getAlerta() != null) && (taskVO.getAlerta().size() != 0)) {
            taskForm.setSendAlert("Yes");
            taskForm.setAlertTypeAlert("on");
          }

          if ((taskVO.getEmaila() != null) && (taskVO.getEmaila().size() != 0)) {
            taskForm.setSendAlert("Yes");
            taskForm.setAlertTypeEmail("on");
          }
        }

        HashMap alertRecipients = taskVO.getAlerta();
        request.setAttribute("sendTo", alertRecipients);
        if ((alertRecipients != null) && (alertRecipients.size() > 0)) {
          String[] alertRecipientStringArray = (String[])(alertRecipients.values().toArray());
          taskForm.setSendTo(alertRecipientStringArray);
        } else {
          alertRecipients = taskVO.getEmaila();
          if ((alertRecipients != null) && (alertRecipients.size() > 0)) {
            String[] alertRecipientStringArray = (String[])(alertRecipients.values().toArray());
            taskForm.setSendTo(alertRecipientStringArray);
          }
        }

        taskForm.setPercentComplete(taskVO.getPercentComplete() + "%");
        request.setAttribute("projecttaskcount", new Long(taskVO.getProjectTaskCount()));
        request.setAttribute("statusid", taskVO.getStat());
        taskForm.setSelectedStatus(taskVO.getSelectedStatus());
        taskForm.setStatus(String.valueOf(taskVO.getStatus()));
        request.setAttribute("setstatus", taskVO.getSelectedStatus());
        taskForm.setSelectedStatus(taskVO.getSelectedStatus());

        IndividualVO workIndividual = taskVO.getCreatedByVO();
        if (workIndividual != null) {
          taskForm.setCreated(workIndividual.getFirstName() + " " + workIndividual.getLastName());
        }
        workIndividual = taskVO.getModifiedByVO();
        if (workIndividual != null) {
          taskForm.setModified(workIndividual.getFirstName() + " " + workIndividual.getLastName());
        }

        // TODO l10n date.
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy - h:mm a");
        if (taskVO.getModifiedOn() != null) {
          String createdon = df.format(taskVO.getCreatedOn());
          taskForm.setCreatedOn(createdon);
        }
        if (taskVO.getModifiedOn() != null) {
          taskForm.setModifiedOn(df.format(taskVO.getModifiedOn()));
        }

        if (taskVO.getModifiedBy() != 0) {
          taskForm.setModifiedbyid(String.valueOf(taskVO.getModifiedBy()));
        }

        if (taskVO.getCreatedBy() != 0) {
          taskForm.setCreatedbyid(String.valueOf(taskVO.getCreatedBy()));
        }
        taskForm.setManager(taskVO.getIndividualName());
        taskForm.setManagerID(String.valueOf(taskVO.getIndividualID()));

        LinkedHashMap crumbs = taskVO.getCrumbs();
        request.setAttribute("crumbs", crumbs);
        request.setAttribute("taskid", taskid);
        request.setAttribute("projectid", String.valueOf(taskVO.getProjectID()));
        request.setAttribute("projectname", taskVO.getProjectName());

        // Build up the bottom list of subtasks for this task
        // Hierarchies are fun!
        Collection subTasks = taskVO.getSubTasks();

        if (subTasks != null) {
          Iterator subtaskIterator = subTasks.iterator();
          while (subtaskIterator.hasNext()) {
            // For some reason each subtask is in HashMap Form
            HashMap subtaskMap = (HashMap)subtaskIterator.next();
            // The keys on this Hashmap appear to be:
            // ActivityID, Title, Milestone, FirstName, LastName,
            // StartDate, DueDate, PercentComplete
            // The ID is some sort of Number Object,
            // The Dates are java.util.Date objects, everything else
            // is a string or can certainly be a string.
          }
        }
        session.setAttribute("taskForm" + taskid, taskVO);
      }
      // then of course go through and set links on the members of this list
      // which is the individual popup, and the view task popup.

      // set everything we can think of on the request and the
      // session arbitrarily so we can hunt for it later in the JSP
      // and on subsequent action calls.
      request.setAttribute("list", "SubTask");
      request.setAttribute("listId", listID);
      session.setAttribute("taskForm" + taskid, taskVO);
      request.setAttribute("taskid", taskid);
      request.setAttribute("taskname", taskVO.getTitle());
      returnStatus = ".view.projects.edit.task";
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    // FIXME lots of fixing needs to happen here. The subtask list is broken
    // now!
    // That probably means get the list of subtasks, and put it in a value list,
    // and then let the jsp just render the value list.
    return (mapping.findForward(returnStatus));
  }
}
