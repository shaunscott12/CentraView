/*
 * $RCSfile: TaskVO.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

package com.centraview.projects.helper;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import com.centraview.activity.helper.ActivityVO;

public class TaskVO extends ActivityVO implements Serializable {
  private int activityID;
  private int projectID;
  private int projectTaskCount;
  private String projectName;
  private String parentTask;
  private int parentID;
  private String isMileStone = "No";
  private Timestamp start;
  private Timestamp end;
  private Vector customFields;
  private int percentComplete;
  private HashMap assignedTo = null;
  private Collection subTasks;
  private Timestamp modified;
  private HashMap sendTo;
  private HashMap alerta;
  private HashMap emaila;
  private LinkedHashMap crumbs;
  private LinkedHashMap stat;
  private String taskid;
  private String setSendAlert;
  private String selectedStatus;

  public TaskVO() {
    this.customFields = new Vector();
  }

  public String getMessage()
  {
    return "The Task: " + getTitle() + " for the Project " + projectName + " has been completed.";
  }

  public int getProjectID()
  {
    return projectID;
  }

  public void setProjectID(int projectID)
  {
    this.projectID = projectID;
  }

  public int getProjectTaskCount()
  {
    return projectTaskCount;
  }

  public void setProjectTaskCount(int projectTaskCount)
  {
    this.projectTaskCount = projectTaskCount;
  }

  public int getParentID()
  {
    return parentID;
  }

  public void setParentID(int parentID)
  {
    this.parentID = parentID;
  }

  public int getPercentComplete()
  {
    return percentComplete;
  }

  public void setPercentComplete(int percentComplete)
  {
    this.percentComplete = percentComplete;
  }

  public String getIsMileStone()
  {
    return isMileStone;
  }

  public void setIsMileStone(String isMileStone)
  {
    this.isMileStone = isMileStone;
  }

  public HashMap getAssignedTo()
  {
    return this.assignedTo;
  }

  public void setAssignedTo(int individual, String name)
  {
    if (this.assignedTo == null)
      this.assignedTo = new HashMap();
    this.assignedTo.put(new Integer(individual), name);
  }

  public Timestamp getStart()
  {
    return start;
  }

  public void setStart(Timestamp start)
  {
    this.start = start;
  }

  public Timestamp getEnd()
  {
    return end;
  }

  public void setEnd(Timestamp end)
  {
    this.end = end;
  }

  /**
   * @return The Collection of CustomFields.
   */
  public Vector getCustomField()
  {
    return this.customFields;
  }

  /**
   * Set the Collection of CustomFields.
   * @param customFields Collection of CustomFields
   */
  public void setCustomField(CustomFieldVO customField)
  {
    this.customFields.add(customField);
  }

  /**
   * Set the Collection of CustomFieldVO.
   * @param customFields Collection of CustomFields
   */

  public void setCustomFieldVOs(Vector vec)
  {
    this.customFields = vec;
  }

  /**
   * @return The Task value Object.
   */
  public TaskVO getValueObject()
  {
    return this;
  }

  /**
   * @return
   */
  public Collection getSubTasks()
  {
    return this.subTasks;
  }

  /**
   * @param subTasks
   */
  public void setSubTasks(Collection subTasks)
  {
    this.subTasks = subTasks;
  }

  public String getProjectName()
  {
    return this.projectName;
  }

  public void setProjectName(String projectName)
  {
    this.projectName = projectName;
  }

  /**
   * @return
   */
  public String getParentTask()
  {
    return this.parentTask;
  }

  /**
   * @param parentTask
   */
  public void setParentTask(String parentTask)
  {
    this.parentTask = parentTask;
  }

  /**
   * @see com.centraview.activity.helper.ActivityVO#getActivityID()
   */
  /**
   * Returns the activityID.
   * @return int
   */
  public int getActivityID()
  {
    return activityID;
  }

  /**
   * Returns the customFields.
   * @return Vector
   */
  public Vector getCustomFields()
  {
    return customFields;
  }

  /**
   * Returns the modified.
   * @return Timestamp
   */
  public Timestamp getModified()
  {
    return modified;
  }

  /**
   * Sets the activityID.
   * @param activityID The activityID to set
   */
  public void setActivityID(int activityID)
  {
    this.activityID = activityID;
  }

  /**
   * Sets the assignedTo.
   * @param assignedTo The assignedTo to set
   */
  public void setAssignedTo(HashMap assignedTo)
  {
    this.assignedTo = assignedTo;
  }

  /**
   * Sets the customFields.
   * @param customFields The customFields to set
   */
  public void setCustomFields(Vector customFields)
  {
    this.customFields = customFields;
  }

  /**
   * Sets the modified.
   * @param modified The modified to set
   */
  public void setModified(Timestamp modified)
  {
    this.modified = modified;
  }

  /**
   * Returns the sendTo.
   * @return HashMap
   */
  public HashMap getSendTo()
  {
    return sendTo;
  }

  /**
   * Sets the sendTo.
   * @param sendTo The sendTo to set
   */
  public void setSendTo(int individual, String name)
  {
    if (this.sendTo == null)
      this.sendTo = new HashMap();
    this.sendTo.put(new Long(individual), name);
  }

  /**
   * Sets the sendTo.
   * @param sendTo The sendTo to set
   */
  public void setSendTo(HashMap sendTo)
  {
    this.sendTo = sendTo;
  }

  public HashMap getAlerta()
  {
    return this.alerta;
  }

  public void setAlerta(HashMap alerta)
  {
    this.alerta = alerta;
  }

  public HashMap getEmaila()
  {
    return this.emaila;
  }

  public void setEmaila(HashMap emaila)
  {
    this.emaila = emaila;
  }

  public LinkedHashMap getCrumbs()
  {
    return this.crumbs;
  }

  public void setCrumbs(LinkedHashMap crumbs)
  {
    this.crumbs = crumbs;
  }

  public LinkedHashMap getStat()
  {
    return this.stat;
  }

  public void setStat(LinkedHashMap stat)
  {
    this.stat = stat;
  }

  public void setStat(int id, String name)
  {
    if (this.stat == null)
      this.stat = new LinkedHashMap();
    this.stat.put(new Integer(id), name);
  }

  public String getSelectedStatus()
  {
    return this.selectedStatus;
  }

  public void setSelectedStatus(String selectedStatus)
  {
    this.selectedStatus = selectedStatus;
  }

  public String getTaskid()
  {
    return this.taskid;
  }

  public void setTaskid(String taskid)
  {
    this.taskid = taskid;
  }

  public String getSetSendAlert()
  {
    return this.setSendAlert;
  }

  public void setSetSendAlert(String setSendAlert)
  {
    this.setSendAlert = setSendAlert;
  }
}