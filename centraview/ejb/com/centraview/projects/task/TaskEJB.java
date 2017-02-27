/*
 * $RCSfile: TaskEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:52 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.mail.SendFailedException;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.centraview.activity.helper.ActivityActionVO;
import com.centraview.activity.helper.ActivityHelperLocal;
import com.centraview.activity.helper.ActivityHelperLocalHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.emailsettings.EmailSettingsLocal;
import com.centraview.administration.emailsettings.EmailSettingsLocalHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.mail.MailMessageVO;
import com.centraview.projects.helper.CustomFieldVO;
import com.centraview.projects.helper.TaskDBVO;
import com.centraview.projects.helper.TaskVO;

public class TaskEJB implements SessionBean
{
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  public void ejbActivate() {}
  public void ejbPassivate() {}
  public void ejbRemove() {}
  public void ejbCreate() {}

  public int addTask(int userId, TaskVO tvo)
  {
    CVDal dl = new CVDal(dataSource);
    int activityId = 0;

    try
    {
      if (! CVUtility.isModuleVisible("Tasks", userId, this.dataSource))
      {
        throw new AuthorizationFailedException("User does not have access to Tasks");
      }

			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);

			ActivityVO actVo = new ActivityVO();
			actVo.setEntityID(0);
			actVo.setIndividualID(tvo.getIndividualID());
			actVo.setActivityType(8);
			actVo.setOwner(userId);
			actVo.setTitle(tvo.getTitle());
			actVo.setActivityDetails(tvo.getActivityDetails());
			actVo.setActivityStartDate(tvo.getStart());
			actVo.setActivityDueDate(tvo.getEnd());
			actVo.setVisibility("PUBLIC");
			activityId = remote.addActivity(actVo,userId);

      int taskCount = 1;
      dl.setSql("projecttask.selecttaskcount");
      dl.setInt(1, tvo.getProjectID());

      Collection countCol = dl.executeQuery();
      Iterator itCount = countCol.iterator();

      if (itCount.hasNext())
      {
        HashMap hm = (HashMap)itCount.next();
        if (hm.get("count(projecttaskcount)") != null)
        {
          taskCount = ((Integer)hm.get("count(projecttaskcount)")).intValue();
        }else{
          taskCount = 0;
        }
      }

      dl.clearParameters();

      dl.setSql("projecttask.addtask");
      dl.setInt(1, activityId);
      dl.setInt(2, tvo.getProjectID());
      dl.setInt(3, tvo.getParentID());
      dl.setString(4, tvo.getIsMileStone());
      dl.setInt(5, ++taskCount);
      dl.executeUpdate();

      HashMap assigned = (HashMap)tvo.getAssignedTo();
      Iterator ite = null;
      if (assigned != null)
      {
        ite = assigned.keySet().iterator();
        while (ite.hasNext())
        {
          int assignedTo = ((Integer)ite.next()).intValue();

          dl.setSql("projecttask.addtaskassigned");
          dl.setInt(1, activityId);
          dl.setInt(2, assignedTo);
          dl.executeUpdate();
          dl.clearParameters();
        }
      }

      Collection actions = tvo.getActivityAction();
      if (actions != null)
      {
        ite = actions.iterator();
        while (ite.hasNext())
        {
          ActivityActionVO action = (ActivityActionVO)ite.next();

          dl.setSql("projecttask.addtaskalertaction");
          dl.setString(1, action.getActionType());
          dl.setString(2, tvo.getMessage());
          dl.executeUpdate();

          int actionId = dl.getAutoGeneratedKey();
          dl.clearParameters();

          ArrayList recipients = action.getRecipient();

          if (recipients != null)
          {
            Iterator it1 = recipients.iterator();
            while (it1.hasNext())
            {
              dl.setSql("projecttask.addtaskalert");
              dl.setInt(1, activityId);
              dl.setInt(2, actionId);
              dl.setInt(3, ((Integer)it1.next()).intValue());
              dl.executeUpdate();
            }
          }   // end if (recipients != null)
        }   // end while (ite.hasNext())
      }   // end if (actions != null)

      Vector customField = tvo.getCustomField();
      if (customField != null && customField.size() != 0)
      {
        for (int i = 0; i < customField.size(); i++)
        {
          CustomFieldVO cvo = (CustomFieldVO)customField.get(i);
          if (cvo.getFieldID() != 0)
          {
            dl.setSql("project.addcustomfieldscalar");
            dl.setInt(1, cvo.getFieldID());
            dl.setInt(2, activityId);
            dl.setString(3, cvo.getValue());
            dl.executeUpdate();
          }
        }
      }

      // TODO: we may not need to do this as tasks are activites and activites don't seem to use recordauth.
      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(dataSource);
      authorizationLocal.saveCurrentDefaultPermission("Tasks", activityId, userId);
    }catch (Exception e){
      System.out.println("[Exception][TaskEJB.addTask] Exception Thrown: " + e);
      e.printStackTrace();
    }finally{
      dl.destroy();
      dl = null;
    }
    return activityId;
  }   // end addTask() method

  public TaskVO getTask(int taskId, int userId)throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Tasks", userId, this.dataSource))
    {
      throw new AuthorizationFailedException("User does not have access to Tasks");
    }

    TaskVO tvo = new TaskVO();
    tvo.setTaskid("" + taskId);
    tvo.setActivityID(taskId);

    try
    {
      CVDal dl = new CVDal(dataSource);

			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);
			ActivityVO actVo = new ActivityVO();
			actVo = remote.getActivity(taskId,userId);

			tvo.setTitle(actVo.getTitle());
			tvo.setActivityDetails(actVo.getActivityDetails());
			tvo.setCreatedBy(actVo.getCreatedBy());
			tvo.setCreatedOn(actVo.getCreatedOn());
			tvo.setModifiedOn(actVo.getModifiedOn());
			tvo.setOwner(tvo.getOwner());
			tvo.setModifiedBy(actVo.getModifiedBy());
			tvo.setStatus(actVo.getStatus());
			tvo.setStart(actVo.getActivityStartDate());
			tvo.setEnd(actVo.getActivityDueDate());
      tvo.fillAuditDetails(this.dataSource);

			if (actVo.getIndividualID() != 0)
      {
				tvo.setIndividualID(actVo.getIndividualID());
				tvo.setIndividualName(actVo.getIndividualName());
			}

      dl.setSql("projecttask.gettask");
      dl.setInt(1, taskId);
      Collection col = dl.executeQuery();
      Iterator ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("projectid") != null)
        {
          tvo.setProjectID(((Long)hm.get("projectid")).intValue());
        }

        if (hm.get("parent") != null)
        {
          tvo.setParentID(((Long)hm.get("parent")).intValue());
        }

        if (hm.get("milestone") != null)
        {
          tvo.setIsMileStone((String)hm.get("milestone"));
        }

        if (hm.get("percentcomplete") != null)
        {
          tvo.setPercentComplete(((Long)hm.get("percentcomplete")).intValue());
        }

        if (hm.get("projecttitle") != null)
        {
          tvo.setProjectName((String)hm.get("projecttitle"));
        }

        if (hm.get("projecttaskcount") != null)
        {
          tvo.setProjectTaskCount(((Integer)hm.get("projecttaskcount")).intValue());
        }
      }
      dl.clearParameters();

      dl.setSql("projecttask.gettaskassigned");
      dl.setInt(1, taskId);
      col = dl.executeQuery();
      ite = col.iterator();
      while (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("individualid") != null)
        {
          tvo.setAssignedTo(((Long)hm.get("individualid")).intValue(), (String)hm.get("CONCAT(firstname, ' ' , lastname)"));
        }
      }
      dl.clearParameters();

      Collection col1 = null;
      Iterator ite1 = null;

      int activityid = -1;
      dl.setSqlQuery("SELECT activityaction.actionid, activityaction.recipient, "+
								"concat(individual.firstname,' ', individual.lastname) IndividualName ,action.type "+
								"FROM individual INNER JOIN activityaction ON (individual.individualid = activityaction.recipient) ,action "+
								"where activityaction.activityid=? and activityaction.actionid = action.ActionID");
      dl.setInt(1, taskId);
      col = dl.executeQuery();
      ite = col.iterator();

      HashMap emaila = new HashMap();
      HashMap alerta = new HashMap();

      boolean email = false;
      boolean alert = false;

      while (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        String type = (String)hm.get("type");

				if (type.equals("ALERT"))
				{
					alert = true;
					alerta.put((Long)hm.get("recipient"), (String)hm.get("IndividualName"));
				}else if (type.equals("EMAIL")){
					email = true;
					emaila.put((Long)hm.get("recipient"), hm.get("IndividualName"));
				}
      }

      tvo.setAlerta(alerta);
      tvo.setEmaila(emaila);

      dl.clearParameters();

      dl.setSql("projecttask.getsubtask");
      dl.setInt(1, taskId);
      tvo.setActivityID(taskId);
      col = dl.executeQuery();

      ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("ActivityID") != null)
        {
          if (hm.get("parent") != null)
          {
            tvo.setParentID(((Long)hm.get("projectid")).intValue());
          }
        }

        if (hm.get("milestone") != null)
        {
          tvo.setIsMileStone((String)hm.get("milestone"));
        }
      }

      tvo.setSubTasks(col);
      dl.clearParameters();

      dl.setSql("projecttask.getparenttaskname");
      dl.setInt(1, tvo.getParentID());
      col = dl.executeQuery();
      ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("title") != null)
        {
          tvo.setParentTask((String)hm.get("title"));
        }
      }
      dl.clearParameters();

      dl.setSql("projecttask.gettaskstatus");
      col = dl.executeQuery();
      ite = col.iterator();
      while (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("statusid") != null)
        {
          tvo.setStat(((Integer)hm.get("statusid")).intValue(), (String)hm.get("name"));
        }
      }
      dl.clearParameters();

      int parentId = tvo.getParentID();

      if (parentId != 0)
      {
        LinkedHashMap lhm = new LinkedHashMap();

        boolean flag = true;
        while (flag)
        {
          dl.setSql("projecttask.gettaskparent");
          dl.setInt(1, parentId);
          col = dl.executeQuery();
          ite = col.iterator();
          if (ite.hasNext())
          {
            HashMap hm = (HashMap)ite.next();
            lhm.put("" + parentId, hm.get("title"));

            if (hm.get("parent") != null)
            {
              parentId = ((Long)hm.get("parent")).intValue();
              if (parentId == 0)
              {
                flag = false;
              }
            }else{
              flag = false;
            }
          }
        }

        tvo.setCrumbs(lhm);
      }
      dl.clearParameters();
      dl.destroy();
    }catch (Exception e){
      System.out.println("[Exception][TaskEJB.getTask] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return tvo;
  }

  public void updateTask(int userId, TaskVO tvo)
  {
    try
    {
      if (! CVUtility.isModuleVisible("Tasks", userId, this.dataSource))
      {
        throw new AuthorizationFailedException("User does not have access to Tasks");
      }

      TaskDBVO tdbvo = new TaskDBVO();
      tdbvo = getDBVO(tvo.getActivityID());
      tvo = (TaskVO)CVUtility.replaceVO(tdbvo, tvo, "Tasks", userId, this.dataSource);

      CVDal dl = new CVDal(dataSource);

			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);

			ActivityVO actVo = new ActivityVO();
			actVo.setActivityID(tvo.getActivityID());
			actVo.setEntityID(0);
			actVo.setIndividualID(tvo.getIndividualID());
			actVo.setActivityType(8);
			actVo.setTitle(tvo.getTitle());
			actVo.setActivityDetails(tvo.getActivityDetails());
			actVo.setActivityStartDate(tvo.getStart());
			actVo.setActivityDueDate(tvo.getEnd());
			actVo.setStatus(tvo.getStatus());

			remote.updateActivity(actVo,userId);

      dl.setSql("projecttask.updatetask");
      dl.setInt(1, tvo.getProjectID());
      dl.setInt(2, tvo.getParentID());
      dl.setString(3, tvo.getIsMileStone());
      dl.setInt(4, tvo.getPercentComplete());
      dl.setInt(5, tvo.getActivityID());
      dl.executeUpdate();
      dl.clearParameters();

      dl.setSql("projecttask.deletetaskassigned");
      dl.setInt(1, tvo.getActivityID());
      dl.executeUpdate();
      dl.clearParameters();

      HashMap assigned = (HashMap)tvo.getAssignedTo();
      Iterator ite = null;
      if (assigned != null)
      {
        ite = assigned.keySet().iterator();
        while (ite.hasNext())
        {
          int assignedTo = ((Integer)ite.next()).intValue();

          dl.setSql("projecttask.addtaskassigned");
          dl.setInt(1, tvo.getActivityID());
          dl.setInt(2, assignedTo);
          dl.executeUpdate();
          dl.clearParameters();
        }
      }

      dl.setSql("projecttask.selecttaskaction");
      dl.setInt(1, tvo.getActivityID());
      Collection countCol = dl.executeQuery();
      dl.clearParameters();
      Iterator itCount = countCol.iterator();
      while (itCount.hasNext())
      {
        HashMap hm = (HashMap)itCount.next();
        if (hm.get("actionid") != null)
        {
          int actionidd = ((Long)hm.get("actionid")).intValue();
          dl.setSql("projecttask.deletetaskalertaction");
          dl.setInt(1, actionidd);
          dl.executeUpdate();
          dl.clearParameters();
          dl.setSql("projecttask.deletetaskalert");
          dl.setInt(1, actionidd);
          dl.executeUpdate();
          dl.clearParameters();
        }
      }

      Vector customField = tvo.getCustomField();
      if (customField != null && customField.size() != 0)
      {
        for (int i = 0; i < customField.size(); i++)
        {
          CustomFieldVO cvo = (CustomFieldVO)customField.get(i);
          if (cvo.getFieldID() != 0)
          {
            dl.setSql("common.updateCustomFieldScalar");
            dl.setString(1, cvo.getValue());
            dl.setInt(2, cvo.getFieldID());
            dl.setInt(3, tvo.getActivityID());
            dl.executeUpdate();
            dl.clearParameters();
          }
        }
      }

      Collection actions = tvo.getActivityAction();
      if (actions != null)
      {
        ite = actions.iterator();
        while (ite.hasNext())
        {
          ActivityActionVO action = (ActivityActionVO)ite.next();
					String message = tvo.getMessage();
          dl.setSql("projecttask.addtaskalertaction");
          dl.setString(1, action.getActionType());
          dl.setString(2, message);
          dl.executeUpdate();

					String individualIDs = "";
          int actionId = dl.getAutoGeneratedKey();
          dl.clearParameters();
          ArrayList recipients = action.getRecipient();
          if (recipients != null)
          {
            Iterator it1 = recipients.iterator();
            while (it1.hasNext())
            {
							int individualID = ((Integer)it1.next()).intValue();
              dl.setSql("projecttask.addtaskalert");
              dl.setInt(1, tvo.getActivityID());
              dl.setInt(2, actionId);
              dl.setInt(3, individualID);
              dl.executeUpdate();
              dl.clearParameters();
              individualIDs += individualID + ",";
            }
          }

					if (individualIDs != null && individualIDs.endsWith(","))
          {
						int individualIDsLen = individualIDs.length();
						individualIDs = individualIDs.substring(0,(individualIDsLen-1));
					}

					if (tvo.getStatus() == 2)
          {
						if (action.getActionType().equals(ActivityActionVO.AA_EMAIL))
            {
								this.sendAlertEmail(userId,individualIDs,message);
						}
					}
				}   // end while (ite.hasNext())
      }   // end if (actions != null)
      dl.destroy();
    }catch (Exception e){
      System.out.println("[Exception][TaskEJB.updateTask] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }   // end updateTask() method

  public TaskDBVO getDBVO(int taskId) throws RemoteException
  {
    TaskDBVO tdbvo = new TaskDBVO();

    tdbvo.setTaskid("" + taskId);
    tdbvo.setActivityID(taskId);

    try
    {
      CVDal dl = new CVDal(dataSource);
      dl.setSql("projecttask.gettaskactivity");
      dl.setInt(1, taskId);
      tdbvo.setActivityID(taskId);
      Collection col = dl.executeQuery();
      Iterator ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        tdbvo.setTitle((String)hm.get("title"));
        tdbvo.setActivityDetails((String)hm.get("details"));
        tdbvo.setCreatedBy(((Integer)hm.get("creator")).intValue());
        tdbvo.setCreatedOn((Timestamp)hm.get("created"));

        if (hm.get("modified") != null)
        {
          tdbvo.setModifiedOn((Timestamp)hm.get("modified"));
        }

        if (hm.get("owner") != null)
        {
          tdbvo.setOwner(((Integer)hm.get("owner")).intValue());
        }

        if (hm.get("modifiedby") != null)
        {
          tdbvo.setModifiedBy(((Integer)hm.get("modifiedby")).intValue());
        }

        if (hm.get("status") != null)
        {
          tdbvo.setStatus(((Integer)hm.get("status")).intValue());
        }

        if (hm.get("name") != null)
        {
          tdbvo.setSelectedStatus((String)hm.get("name"));
        }

        if (hm.get("start") != null)
        {
          Object o = hm.get("start");
          tdbvo.setStart((java.sql.Timestamp)o);
        }

        if (hm.get("duedate") != null)
        {
          Object ox = hm.get("duedate");
          tdbvo.setEnd((java.sql.Timestamp)ox);
        }

        tdbvo.fillAuditDetails(this.dataSource);
      }
      dl.clearParameters();

      dl.setSql("projecttask.gettask");
      dl.setInt(1, taskId);
      col = dl.executeQuery();
      ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("projectid") != null)
        {
          tdbvo.setProjectID(((Long)hm.get("projectid")).intValue());
        }

        if (hm.get("parent") != null)
        {
          tdbvo.setParentID(((Long)hm.get("parent")).intValue());
        }

        if (hm.get("milestone") != null)
        {
          tdbvo.setIsMileStone((String)hm.get("milestone"));
        }

        if (hm.get("percentcomplete") != null)
        {
          tdbvo.setPercentComplete(((Long)hm.get("percentcomplete")).intValue());
        }

        if (hm.get("projecttitle") != null)
        {
          tdbvo.setProjectName((String)hm.get("projecttitle"));
        }

        if (hm.get("projecttaskcount") != null)
        {
          tdbvo.setProjectTaskCount(((Integer)hm.get("projecttaskcount")).intValue());
        }
      }   // end if (ite.hasNext())
      dl.clearParameters();

      dl.setSql("projecttask.gettaskassigned");
      dl.setInt(1, taskId);
      col = dl.executeQuery();
      ite = col.iterator();
      while (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("individualid") != null)
        {
          tdbvo.setAssignedTo(((Long)hm.get("individualid")).intValue(), (String)hm.get("CONCAT(firstname, ' ' , lastname)"));
        }
      }
      dl.clearParameters();

      Collection col1 = null;
      Iterator ite1 = null;

      int activityid = -1;
      dl.setSql("projecttask.gettaskalert");
      dl.setInt(1, taskId);
      col = dl.executeQuery();
      ite = col.iterator();

      HashMap emaila = new HashMap();
      HashMap alerta = new HashMap();

      boolean email = false;
      boolean alert = false;

      while (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("actionid") != null)
        {
          activityid = ((Long)hm.get("actionid")).intValue();
        }
        dl.setSql("projecttask.gettaskalertaction");
        dl.setInt(1, activityid);
        col1 = dl.executeQuery();
        ite1 = col1.iterator();
        if (ite1.hasNext())
        {
          Object o = ite1.next();

          HashMap hhm = (HashMap)o;

          String type = (String)hhm.get("type");

          if (type.equals("ALERT"))
          {
            alert = true;
            alerta.put((Long)hm.get("recipient"), (String)hm.get("concat(individual.firstname,' ', individual.lastname)"));
          }else if (type.equals("EMAIL")){
            email = true;
            emaila.put((Long)hm.get("recipient"), hm.get("concat(individual.firstname,' ', individual.lastname)"));
          }   // end if (type.equals("ALERT"))
        }   // end if (ite1.hasNext())
      }   // end while (ite.hasNext())

      tdbvo.setAlerta(alerta);
      tdbvo.setEmaila(emaila);

      dl.clearParameters();

      dl.setSql("projecttask.getsubtask");
      dl.setInt(1, taskId);
      tdbvo.setActivityID(taskId);
      col = dl.executeQuery();

      ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("ActivityID") != null)
        {
          if (hm.get("parent") != null)
          {
            tdbvo.setParentID(((Long)hm.get("projectid")).intValue());
          }
        }

        if (hm.get("milestone") != null)
        {
          tdbvo.setIsMileStone((String)hm.get("milestone"));
        }
      }   // end if (ite.hasNext())

      tdbvo.setSubTasks(col);
      dl.clearParameters();

      dl.setSql("projecttask.getparenttaskname");
      dl.setInt(1, tdbvo.getParentID());
      col = dl.executeQuery();
      ite = col.iterator();
      if (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("title") != null)
        {
          tdbvo.setParentTask((String)hm.get("title"));
        }
      }
      dl.clearParameters();

      dl.setSql("projecttask.gettaskstatus");
      col = dl.executeQuery();
      ite = col.iterator();
      while (ite.hasNext())
      {
        HashMap hm = (HashMap)ite.next();
        if (hm.get("statusid") != null)
        {
          tdbvo.setStat(((Integer)hm.get("statusid")).intValue(), (String)hm.get("name"));
        }
      }
      dl.clearParameters();

      int parentId = tdbvo.getParentID();

      if (parentId != 0)
      {
        LinkedHashMap lhm = new LinkedHashMap();

        boolean flag = true;
        while (flag)
        {
          dl.setSql("projecttask.gettaskparent");
          dl.setInt(1, parentId);
          col = dl.executeQuery();
          ite = col.iterator();
          if (ite.hasNext())
          {
            HashMap hm = (HashMap)ite.next();
            lhm.put("" + parentId, hm.get("title"));

            if (hm.get("parent") != null)
            {
              parentId = ((Long)hm.get("parent")).intValue();
              if (parentId == 0)
              {
                flag = false;
              }
            }else{
              flag = false;
            }
          }   // end if (ite.hasNext())
        }   // end while (flag)

        tdbvo.setCrumbs(lhm);
      }
      dl.clearParameters();
      dl.destroy();
    }catch (Exception e){
      System.out.println("[Exception][TaskEJB.getDBVO] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return tdbvo;
  }

  public int deleteTask(int taskId,int individualID)
  {
    CVDal cvdl = new CVDal(dataSource);
    cvdl.setSql("projecttask.getsubtask");
    cvdl.setInt(1, taskId);
    Collection col = cvdl.executeQuery();
    cvdl.clearParameters();

    deleteTask1(taskId,individualID);
    int activityID = 0;

    if (col != null)
    {
      Iterator it = col.iterator();
      while (it.hasNext())
      {
        HashMap hm = (HashMap)it.next();
        activityID = ((Long)hm.get("ActivityID")).intValue();
        deleteTask1(activityID,individualID);
      }
    }
    return 0;
  }

  /**
   * this method delete rule
   */
  public int deleteTask1(int taskId,int individualID)
  {
    CVDal cvdl = new CVDal(dataSource);

    try
    {
			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);
			remote.deleteActivity(taskId,individualID);

      cvdl.setSql("projecttask.selecttaskaction");
      cvdl.setInt(1, taskId);
      Collection col = cvdl.executeQuery();
      Iterator itCount = col.iterator();
      if (itCount.hasNext())
      {
        HashMap hm = (HashMap)itCount.next();
        int actionId = ((Long)hm.get("actionid")).intValue();
        cvdl.setSql("projecttask.deletetaskalertaction");
        cvdl.setInt(1, actionId);
        cvdl.executeUpdate();
      }
      cvdl.clearParameters();
      cvdl.setSql("projecttask.deletetaskalert");
      cvdl.setInt(1, taskId);
      cvdl.executeUpdate();

      cvdl.setSql("projecttask.setidnull");
      cvdl.setInt(1, taskId);
      cvdl.executeUpdate();

    }catch (Exception e){
      System.out.println("[Exception][TaskEJB.deleteTask1] Exception Thrown: " + e);
      e.printStackTrace();
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return 0;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

	/**
	* It will set all the MailMessageVO object and then call the simpleMessage Method which will use the
	* local adminstrator's account and send the email to the individual's
	* @param individualID The ID for the individual who is logged in.
	* @param individualIDs The IDs its a list of individualId seperated by comma.
	* @param body The body of the Message
	* @return void
	*/
	private void sendAlertEmail(int individualID,String individualIDs,String body) throws SendFailedException
	{
		try{
			MailMessageVO mailMessageVO = new MailMessageVO();
			ArrayList toList = new ArrayList();

			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome contactHome = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal contactRemote = (ContactHelperLocal)contactHome.create();
			contactRemote.setDataSource(dataSource);
			toList = contactRemote.getEmailContactForIndividuals(individualIDs);
			String fromAddress = contactRemote.getPrimaryEmailAddress(individualID,2);

			if (fromAddress != null && !fromAddress.equals("") && toList != null && toList.size() != 0)
			{
				EmailSettingsLocalHome emailSettingsHome = (EmailSettingsLocalHome)ic.lookup("local/EmailSettings");
				EmailSettingsLocal emailSettingsRemote = (EmailSettingsLocal)emailSettingsHome.create();
				emailSettingsRemote.setDataSource(dataSource);

				// Its a predefined Template for the replying message for Task Completion
				EmailTemplateForm taskTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_TASK);
				String subject = taskTemplateForm.getSubject();
				String bodyTemplate = taskTemplateForm.getBody()+ "\n";

				mailMessageVO.setToList(toList);
				mailMessageVO.setFromAddress(fromAddress);
				mailMessageVO.setHeaders("Task Completion");
				mailMessageVO.setSubject(subject);
				mailMessageVO.setBody(bodyTemplate+body);
				mailMessageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);

				boolean sendFlag = emailSettingsRemote.simpleMessage(individualID,mailMessageVO);
			}// end of if(fromAddress != null && !fromAddress.equals("") && toList != null && toList.size() != 0)
		}
		catch(Exception e){
			e.printStackTrace();
			String message =(String) e.getMessage();
			if(e instanceof SendFailedException){
				throw new SendFailedException(message);
			}
		}
	}// end sendAlertEmail(String,String,CVDal) method

}

