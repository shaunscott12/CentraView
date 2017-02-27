/*
 * $RCSfile: ActivityFacadeEJB.java,v $    $Revision: 1.4 $  $Date: 2005/09/13 22:04:29 $ - $Author: mcallist $
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

package com.centraview.activity.activityfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.activity.activitylist.ActivityListLocal;
import com.centraview.activity.activitylist.ActivityListLocalHome;
import com.centraview.activity.helper.ActivityHelperLocal;
import com.centraview.activity.helper.ActivityHelperLocalHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.file.CvFileFacade;
import com.centraview.projects.helper.TaskVO;
import com.centraview.projects.task.TaskLocal;
import com.centraview.projects.task.TaskLocalHome;

public class ActivityFacadeEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(ActivityFacadeEJB.class);

  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  public void ejbActivate() throws RemoteException
  {}

  public void ejbPassivate() throws RemoteException
  {}

  public void ejbRemove() throws RemoteException
  {}

  public void ejbCreate()
  {}

  public int addActivity(ActivityVO av, int userId) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Activities", userId, this.dataSource)) {
      throw new AuthorizationFailedException(
          "Activity add failed: isModuleVisible() returned false");
    }

    int newActivityID = 0;

    try {
      InitialContext ic = CVUtility.getInitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(dataSource);
      newActivityID = remote.addActivity(av, userId);

      commitAttachment(av, userId);
    } catch (Exception e) {
      System.out.println("[Exception][ActivityFacadeEJB] Exception thrown in add(): " + e);
      e.printStackTrace();
    }
    return (newActivityID);
  }

  public ActivityVO getActivity(int activityId, int userid) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userid, "Activities", activityId,
        ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("Activity - getActivity");
    }

    ActivityVO av = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(dataSource);
      av = remote.getActivity(activityId, userid);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    } catch (NamingException re) {
      throw new EJBException(re);
    }
    return av;
  }

  /**
   * I have added this method to get the ActivityType of an existing activity.
   * The current scheme insists that you know the activity type BEFORE obtaining
   * the Activity Information. <strong>Note:</strong> This method will return
   * -1 if (for some reason) the ActivityType is not set.
   * @param activityID The ID of the activity.
   * @return An int with the activity type. -1 if the activity type isn't set.
   */
  public int getActivityType(int activityId)
  {
    int activityType = -1;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(dataSource);
      activityType = remote.getActivityType(activityId);
    } catch (Exception e) {
      System.out.println("[Exception][ActivityFacadeEJB.getActivityType] : " + e.toString());
    }
    return activityType;
  } // end of getActivityType method

  public void updateActivity(ActivityVO activityvo, int userid) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Activities", userid, this.dataSource)) {
      throw new AuthorizationFailedException("Activity -- update");
    }

    if (!CVUtility.canPerformRecordOperation(userid, "Activities", activityvo.getActivityID(),
        ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("Activity - update");
    }

    try {
      InitialContext ic = CVUtility.getInitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(dataSource);
      remote.updateActivity(activityvo, userid);

      commitAttachment(activityvo, userid);

    } catch (Exception e) {
      System.out.println("[Exception][ActivityFacadeEJB.update] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }

  public void deleteActivity(int activityId, int userId) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Activities", userId, this.dataSource)) {
      throw new AuthorizationFailedException("Activity -- delete");
    }

    if (!CVUtility.canPerformRecordOperation(userId, "Activities", activityId,
        ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("Activity - deleteActivity");
    }

    try {
      InitialContext ic = CVUtility.getInitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(dataSource);
      remote.deleteActivity(activityId, userId);
    } catch (NamingException ne) {
      throw new EJBException(ne);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    }
  }

  // private method to call the commit on newly added files to the activity
  // (Attachment)
  private void commitAttachment(ActivityVO av, int userId) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Activities", userId, this.dataSource)) {
      throw new AuthorizationFailedException("Activity -- commitAttachment");
    }

    Vector idVec = av.getAttachmentId();

    if (idVec != null) {
      Iterator it = idVec.iterator();
      // will never know which one is new and which one is old hence commit all
      while (it.hasNext()) {
        int fileId = ((Integer)it.next()).intValue();
        try {
          CvFileFacade cvf = new CvFileFacade();
          cvf.commitEmailAttachment(userId, fileId, this.dataSource);
        } catch (Exception e) {
          System.out.println("[Exception][ActivityFacadeEJB.commitAttachment] Exception Thrown: "
              + e);
          e.printStackTrace();
        }
      }
    }
  }

  public Vector getAllResources()
  {
    Vector rv = null;
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ActivityListLocalHome home = (ActivityListLocalHome)ic.lookup("local/ActivityList");
      ActivityListLocal remote = home.create();
      remote.setDataSource(dataSource);
      rv = remote.getAllResources();
    } catch (Exception e) {
      System.out.println("[Exception][ActivityFacadeEJB.getAllResources] Exception Thrown: " + e);
      e.printStackTrace();
    }

    return rv;

  }

  public void updateStatus(int activityID, int attendeeID, String status)
  {
    try {
      InitialContext ctx = new InitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ctx.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(status);

      remote.updateStatus(activityID, attendeeID, status);
    } catch (Exception e) {
      System.out.println("[Exception][ActivityFacadeEJB.updateStatus] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * Delete a individual from the attendee's List for an activity. Returns void
   * @param activityId int representing an activity Identification.
   * @param userId its a open database connection.
   * @return void
   */
  public void deleteIndividualFromAttendee(int activityId, int userId)
  {
    try {
      InitialContext ctx = new InitialContext();
      ActivityHelperLocalHome home = (ActivityHelperLocalHome)ctx.lookup("local/ActivityHelper");
      ActivityHelperLocal remote = home.create();
      remote.setDataSource(dataSource);
      remote.deleteIndividualFromAttendee(activityId, userId);
    } catch (Exception e) {
      System.out.println("[Exception][ActivityFacadeEJB.updateStatus] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }

  /**
   * For use on the Home Page, get a list of tasks assigned to the individual
   * which are due today or were due in the past.
   * @param individualId
   * @return an ArrayList of TaskVOs
   */
  public ArrayList getTaskList(int individualId)
  {
    ArrayList taskList = new ArrayList();
    // Run a simple Query to get the IDs of all the tasks
    // that are assigned to the individual or the individual is the
    // manager of the task and the due date is <= NOW();
    StringBuffer query = new StringBuffer();
    query
        .append("SELECT a.activityId AS taskId FROM activity AS a, task AS t, taskassigned AS ta ");
    query.append("WHERE t.activityId = a.activityId AND t.activityId = ta.taskId ");
    query.append("AND (ta.assignedto = ? OR a.owner = ?) AND a.dueDate <= NOW()");
    CVDal cvdal = new CVDal(this.dataSource);
    try {
      cvdal.setSqlQuery(query.toString());
      cvdal.setInt(1, individualId);
      cvdal.setInt(2, individualId);
      Collection result = cvdal.executeQuery();
      if (result != null) {
        // if we have any results from the query set up the connection
        // to the local task session EJB
        InitialContext ctx = new InitialContext();
        TaskLocalHome taskHome = (TaskLocalHome)ctx.lookup("local/Task");
        TaskLocal taskBean = taskHome.create();
        taskBean.setDataSource(dataSource);
        // the iterate through the results of the query grabbing a taskVO
        // for each id.
        Iterator i = result.iterator();
        while (i.hasNext()) {
          HashMap resultRow = (HashMap)i.next();
          Number taskId = (Number)resultRow.get("taskId");
          try {
            TaskVO task = taskBean.getTask(taskId.intValue(), individualId);
            taskList.add(task);
          } catch (AuthorizationFailedException afe) {
            // no reason to completely fail here, it will just be one less thing
            // on our list. dump it in the logs, because it is indicative of an
            // underlying problem
            logger.warn("[getTaskList]: Tried to get unautorized Task, continuing.");
          }
        }
      }
    } catch (Exception e) {
      logger.error("[getTaskList]: Exception", e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
    }
    return taskList;
  }
}
