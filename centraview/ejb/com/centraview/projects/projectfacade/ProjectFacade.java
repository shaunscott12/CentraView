/*
 * $RCSfile: ProjectFacade.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.ProjectList;
import com.centraview.common.TimeSlipList;
import com.centraview.projects.helper.ProjectVO;
import com.centraview.projects.helper.TaskVO;
import com.centraview.projects.helper.TimeSlipVO;

public interface ProjectFacade extends EJBObject {

  public int addProject(int userId, ProjectVO pvo) throws RemoteException;

  public ProjectVO getProject(int projectId, int userId) throws RemoteException;

  public void updateProject(int userId, ProjectVO tvo) throws RemoteException;

  public void deleteProject(int indvID, int projectId) throws RemoteException,
      AuthorizationFailedException;

  public ProjectList getProjectList(int userID, HashMap info) throws RemoteException;

  public int addProjectTask(int userId, TaskVO tvo) throws RemoteException;

  public void updateProjectTask(int userId, TaskVO tvo) throws RemoteException,
      AuthorizationFailedException;

  public void deleteTask(int taskId, int individualID) throws RemoteException,
      AuthorizationFailedException;

  public TaskVO getTask(int taskId, int userId) throws RemoteException;

  public int addTimeSlip(int userId, TimeSlipVO tsvo) throws RemoteException;

  public void updateTimeSlip(int userId, TimeSlipVO tsvo) throws RemoteException;

  public TimeSlipList getAllTimeSlipList(int userID, HashMap info) throws RemoteException;

  public void duplicateTimeSlip(int userId, int timeSlipId) throws RemoteException;

  public void deleteTimeSlip(int timeSlipId) throws RemoteException, AuthorizationFailedException;

  public TimeSlipVO getTimeSlip(int timeSlipId, int userId) throws RemoteException;

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId) throws RemoteException;

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId, HashMap info)
      throws RemoteException;

  /**
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public void setDataSource(String ds) throws RemoteException;

  /**
   * This method returns Project Name Of the Project
   * @param ProjectID The ProjectID to collect the Project Title
   * @return ProjectName The ProjectName
   */
  public String getProjectName(int ProjectID) throws RemoteException;

  public Vector getProjectStatusList() throws java.rmi.RemoteException;
}
