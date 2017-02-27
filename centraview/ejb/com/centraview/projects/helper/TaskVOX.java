/*
 * $RCSfile: TaskVOX.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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

import java.sql.Timestamp;
import java.util.TimeZone;

import org.apache.struts.action.ActionForm;

import com.centraview.activity.helper.ActivityActionVO;
import com.centraview.common.CVUtility;
import com.centraview.common.DateUtility;
import com.centraview.projects.task.TaskForm;

public class TaskVOX extends TaskVO {

  /**
   * Constructor handling ActionForm
   * @param form ActionForm
   */
  public TaskVOX(String tz, ActionForm form) {
    TaskForm dynaForm = (TaskForm)form;

    setTitle(dynaForm.getTitle());
    setActivityDetails(dynaForm.getDescription());

    if (dynaForm.getTaskid() != null) {
      setTaskid(dynaForm.getTaskid());
      setActivityID(Integer.parseInt(dynaForm.getTaskid()));
    }

    setProjectID(Integer.parseInt(dynaForm.getProjectid()));
    setProjectName(dynaForm.getProject());

    if (dynaForm.getParenttaskid() != null && dynaForm.getParenttaskid().equals("")) {
      setParentID(0);
    } else {
      setParentID(Integer.parseInt(dynaForm.getParenttaskid()));
    }

    String status = dynaForm.getStatus();

    if (status != null && !status.equals("")) {
      setStat(Integer.parseInt(status), "");
      setStatus(Integer.parseInt(status));
    }

    String percent = dynaForm.getPercentComplete().trim();

    if ((!percent.trim().equals("")) && (percent.length() > 0)) {
      setPercentComplete(Integer.parseInt(percent.substring(0, percent.length() - 1)));
    }

    setIsMileStone(dynaForm.getMilestone());

    setIndividualID(Integer.parseInt(dynaForm.getManagerID()));

    if (!dynaForm.getStartyear().equals("")) {
      Timestamp startTS = DateUtility.createTimestamp(dynaForm.getStartyear(), dynaForm
          .getStartmonth(), dynaForm.getStartday());
      setStart(CVUtility.convertTimeZone(startTS, TimeZone.getTimeZone(tz), TimeZone
          .getTimeZone("EST")));
    }
    if (!dynaForm.getEndyear().equals("")) {
      Timestamp endTS = DateUtility.createTimestamp(dynaForm.getEndyear(), dynaForm.getEndmonth(),
          dynaForm.getEndday());
      setEnd(CVUtility
          .convertTimeZone(endTS, TimeZone.getTimeZone(tz), TimeZone.getTimeZone("EST")));
    }

    String[] AssignedTo = dynaForm.getAssignedTo();
    String[] SendTo = dynaForm.getSendTo();

    if (AssignedTo != null) {
      for (int i = 0; i < AssignedTo.length; i++) {
        if (!AssignedTo[i].trim().equals(""))
          setAssignedTo(Integer.parseInt(AssignedTo[i]), "");
      }
    }

    if (dynaForm.getSendAlert().trim().equals("Yes")) {
      setSetSendAlert("Yes");

      if (dynaForm.getAlertTypeAlert().equals("on")) {
        setActivityAction(getActivityActionVO(ActivityActionVO.AA_ALERT, dynaForm.getSendTo()));
      }

      if (dynaForm.getAlertTypeEmail().equals("on")) {
        setActivityAction(getActivityActionVO(ActivityActionVO.AA_EMAIL, dynaForm.getSendTo()));
      }

      // if (dynaForm.getAlertTypeEmail().equals("on"))
      {
        if (SendTo != null) {
          for (int i = 0; i < SendTo.length; i++) {
            if (!SendTo[i].trim().equals(""))
              setSendTo(Integer.parseInt(SendTo[i]), "");
          }
        }
      }
    } else {
      setSetSendAlert("No");
    }
  }

  /**
   * @return The CustomField value Object.
   */
  public TaskVO getValueObject()
  {
    return super.getValueObject();
  }

  public ActivityActionVO getActivityActionVO(String actiontype, String[] SentTo)
  {
    ActivityActionVO aavo = new ActivityActionVO();
    aavo.setActionType(actiontype);

    for (int i = 0; i < SentTo.length; i++) {
      if (!SentTo[i].trim().equals(""))
        aavo.setRecipient(Integer.parseInt(SentTo[i]));
    }
    return aavo;
  }
}
