/*
 * $RCSfile: ProjectFacadeLocal.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.ProjectList;
import com.centraview.common.TimeSlipList;
import com.centraview.projects.helper.ProjectVO;
import com.centraview.projects.helper.TaskVO;
import com.centraview.projects.helper.TimeSlipVO;

public interface ProjectFacadeLocal extends EJBLocalObject {
  public int addProject(int userId, ProjectVO pvo);

  public ProjectVO getProject(int projectId, int userId);

  public void deleteProject(int indvID, int projectId) throws AuthorizationFailedException;

  public ProjectList getProjectList(int userID, HashMap info);

  public void updateProject(int userId, ProjectVO pvo);

  public int addProjectTask(int userId, TaskVO tvo);

  public void updateProjectTask(int userId, TaskVO tvo) throws AuthorizationFailedException;

  public void deleteTask(int taskId, int individualID) throws AuthorizationFailedException;

  public TaskVO getTask(int taskId, int userId);

  public int addTimeSlip(int userId, TimeSlipVO tsvo);

  public TimeSlipList getAllTimeSlipList(int userID, HashMap info);

  public void deleteTimeSlip(int timeSlipId);

  public TimeSlipVO getTimeSlip(int timeSlipId, int userId);

  public void updateTimeSlip(int userId, TimeSlipVO tsvo);

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId);

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId, HashMap info);

  /**
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public void setDataSource(String ds);

  /**
   * This method returns Project Name Of the Project
   * @param ProjectID The ProjectID to collect the Project Title
   * @return ProjectName The ProjectName
   */
  public String getProjectName(int ProjectID);

  public Vector getProjectStatusList();

}
