/*
 * $RCSfile: ProjectFacadeEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

package com.centraview.projects.projectfacade;

import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.ProjectList;
import com.centraview.common.TimeSlipList;
import com.centraview.projects.helper.ProjectVO;
import com.centraview.projects.helper.ProjectsHelperLocal;
import com.centraview.projects.helper.ProjectsHelperLocalHome;
import com.centraview.projects.helper.TaskVO;
import com.centraview.projects.helper.TimeSlipVO;
import com.centraview.projects.project.ProjectLocal;
import com.centraview.projects.project.ProjectLocalHome;
import com.centraview.projects.projectlist.ProjectListsLocal;
import com.centraview.projects.projectlist.ProjectListsLocalHome;
import com.centraview.projects.task.TaskLocal;
import com.centraview.projects.task.TaskLocalHome;
import com.centraview.projects.timeslip.TimeSlipLocal;
import com.centraview.projects.timeslip.TimeSlipLocalHome;

public class ProjectFacadeEJB implements SessionBean {
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public void ejbRemove()
  {}

  public void ejbCreate()
  {}

  /**
   * Deletes project
   */
  public void deleteProject(int indvID, int projectID) throws AuthorizationFailedException
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
      ProjectLocal remote = (ProjectLocal)home.create();
      remote.setDataSource(this.dataSource);
      remote.deleteProject(indvID, projectID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return;
  }

  public int addProject(int userId, ProjectVO pvo)
  {
    int projectId = 0;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
      ProjectLocal remote = (ProjectLocal)home.create();
      remote.setDataSource(this.dataSource);

      projectId = remote.addProject(userId, pvo);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.addProject] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return projectId;
  }

  public void updateProject(int userId, ProjectVO pvo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
      ProjectLocal remote = (ProjectLocal)home.create();
      remote.setDataSource(this.dataSource);

      remote.updateProject(userId, pvo);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.updateProject] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return;
  }

  public ProjectVO getProject(int projectId, int userId)
  {
    ProjectVO projectVO = null;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
      ProjectLocal remote = (ProjectLocal)home.create();
      remote.setDataSource(this.dataSource);

      projectVO = remote.getProject(projectId, userId);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getProject] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return projectVO;
  }

  public ProjectList getProjectList(int userID, HashMap info)
  {
    ProjectList projectList = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectListsLocalHome home = (ProjectListsLocalHome)ic.lookup("local/ProjectLists");
      ProjectListsLocal remote = (ProjectListsLocal)home.create();
      remote.setDataSource(this.dataSource);
      projectList = (ProjectList)remote.getProjectList(userID, info);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getProjectList] Exception Thrown: " + e);
      e.printStackTrace();
    }

    return projectList;
  }

  /**
   * adds New Project Task
   */
  public int addProjectTask(int userId, TaskVO tvo)
  {
    int id = 0;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TaskLocalHome home = (TaskLocalHome)ic.lookup("local/Task");
      TaskLocal remote = (TaskLocal)home.create();
      remote.setDataSource(this.dataSource);
      id = remote.addTask(userId, tvo);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.addProjectTask] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (id);
  }

  /**
   * Updates Task data
   */
  public void updateProjectTask(int userId, TaskVO tvo) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Tasks", tvo.getActivityID(),
        ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException(
          "<error> You don't have privilege to update some Records. </error>");
    }

    try {
      InitialContext ic = CVUtility.getInitialContext();
      TaskLocalHome home = (TaskLocalHome)ic.lookup("local/Task");
      TaskLocal remote = (TaskLocal)home.create();
      remote.setDataSource(this.dataSource);
      remote.updateTask(userId, tvo);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.updateProjectTask] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return;
  }

  public void deleteTask(int taskID, int individualID) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(individualID, "Tasks", taskID,
        ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException(
          "<error> You don't have privilege to Delete some Records. </error>");
    }

    try {
      InitialContext ic = CVUtility.getInitialContext();
      TaskLocalHome home = (TaskLocalHome)ic.lookup("local/Task");
      TaskLocal remote = (TaskLocal)home.create();
      remote.setDataSource(this.dataSource);

      remote.deleteTask(taskID, individualID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return;
  }

  public TaskVO getTask(int taskId, int userId)
  {
    TaskVO taskVO = null;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      TaskLocalHome home = (TaskLocalHome)ic.lookup("local/Task");
      TaskLocal remote = (TaskLocal)home.create();
      remote.setDataSource(this.dataSource);

      taskVO = remote.getTask(taskId, userId);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getTask] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return taskVO;
  }

  public int addTimeSlip(int userId, TimeSlipVO tsvo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TimeSlipLocalHome home = (TimeSlipLocalHome)ic.lookup("local/TimeSlip");
      TimeSlipLocal remote = (TimeSlipLocal)home.create();
      remote.setDataSource(this.dataSource);

      return remote.addTimeSlip(userId, tsvo);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.addTimeSlip] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return 0;
  }

  public void deleteTimeSlip(int timeslipID)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TimeSlipLocalHome home = (TimeSlipLocalHome)ic.lookup("local/TimeSlip");
      TimeSlipLocal remote = (TimeSlipLocal)home.create();
      remote.setDataSource(this.dataSource);

      remote.deleteTimeSlip(timeslipID);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return;
  }

  public TimeSlipList getAllTimeSlipList(int userID, HashMap info)
  {
    TimeSlipList timeSlipList = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectListsLocalHome home = (ProjectListsLocalHome)ic.lookup("local/ProjectLists");
      ProjectListsLocal remote = (ProjectListsLocal)home.create();
      remote.setDataSource(this.dataSource);
      timeSlipList = (TimeSlipList)remote.getAllTimeSlipList(userID, info);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getAllTimeSlipList] Exception Thrown: " + e);
      e.printStackTrace();
    }

    return timeSlipList;
  }

  public void duplicateTimeSlip(int userId, int timeSlipId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TimeSlipLocalHome home = (TimeSlipLocalHome)ic.lookup("local/TimeSlip");
      TimeSlipLocal remote = (TimeSlipLocal)home.create();
      remote.setDataSource(this.dataSource);

      remote.duplicateTimeSlip(userId, timeSlipId);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.duplicateTimeSlip] Exception Thrown: " + e);
      e.printStackTrace();
    }

  }

  public TimeSlipVO getTimeSlip(int timeSlipId, int userId)
  {
    TimeSlipVO tVO = null;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      TimeSlipLocalHome home = (TimeSlipLocalHome)ic.lookup("local/TimeSlip");
      TimeSlipLocal remote = (TimeSlipLocal)home.create();
      remote.setDataSource(this.dataSource);

      tVO = remote.getTimeSlip(timeSlipId, userId);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getTimeSlip] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return tVO;
  }

  public void updateTimeSlip(int userId, TimeSlipVO tsvo)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      TimeSlipLocalHome home = (TimeSlipLocalHome)ic.lookup("local/TimeSlip");
      TimeSlipLocal remote = (TimeSlipLocal)home.create();
      remote.setDataSource(this.dataSource);

      remote.updateTimeSlip(userId, tsvo);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.updateTimeSlip] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return;
  }

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId, HashMap info)
  {
    TimeSlipList timeSlipList = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectListsLocalHome home = (ProjectListsLocalHome)ic.lookup("local/ProjectLists");
      ProjectListsLocal remote = (ProjectListsLocal)home.create();
      remote.setDataSource(this.dataSource);
      timeSlipList = (TimeSlipList)remote.getTimeSlipListForProject(userId, projectId, info);
    } catch (Exception e) {
      System.out
          .println("[Exception][ProjectFacadeEJB.getTimeSlipListForProject] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return timeSlipList;
  }

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId)
  {
    TimeSlipList timeSlipList = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectListsLocalHome home = (ProjectListsLocalHome)ic.lookup("local/ProjectLists");
      ProjectListsLocal remote = (ProjectListsLocal)home.create();
      remote.setDataSource(this.dataSource);
      timeSlipList = (TimeSlipList)remote.getTimeSlipListForProject(userId, projectId);
    } catch (Exception e) {
      System.out
          .println("[Exception][ProjectFacadeEJB.getTimeSlipListForProject] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return timeSlipList;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * This method returns Project Name Of the Project
   * @param ProjectID The ProjectID to collect the Project Title
   * @return ProjectName The ProjectName
   */
  public String getProjectName(int ProjectID)
  {
    String ProjectName = "";
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
      ProjectLocal remote = (ProjectLocal)home.create();
      remote.setDataSource(this.dataSource);
      ProjectName = remote.getProjectName(ProjectID);
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getProjectName] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return ProjectName;
  }

  public Vector getProjectStatusList()
  {
    Vector projectStatus = new Vector();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ProjectsHelperLocalHome home = (ProjectsHelperLocalHome)ic.lookup("local/ProjectsHelper");
      ProjectsHelperLocal remote = (ProjectsHelperLocal)home.create();
      remote.setDataSource(this.dataSource);
      projectStatus = remote.getProjectStatusList();
    } catch (Exception e) {
      System.out.println("[Exception][ProjectFacadeEJB.getProjectStatusList] Exception Thrown: "
          + e);
      e.printStackTrace();
    }
    return projectStatus;
  }
}
