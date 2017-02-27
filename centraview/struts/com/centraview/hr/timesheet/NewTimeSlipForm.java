/*
 * $RCSfile: NewTimeSlipForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
/**
 * TimeSlipForm  All fields related to TimeSlip are defined in this class.
 * Validation is done at the time of save and edit of TimeSlip.
 *
 * @date   : 30-10-03
 * @version: 1.0
 */

package com.centraview.hr.timesheet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.CVUtility;

public class NewTimeSlipForm extends DynaValidatorForm {
  private static Logger logger = Logger.getLogger(NewTimeSlipForm.class);
  private float fBreakHrs;
  private float fBreakMins;
  private float fBreakTime;

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();

    try {
      if (request.getParameter("timeslip") != null) {
        if (request.getParameter("timeslip").equalsIgnoreCase("support")) {

          if ((String)get("description") == null
              || ((String)get("description")).trim().length() <= 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.general.requiredField", "Description"));
          }
        }
      } else {
        String project = (String)get("project");
        if (project != null && (!project.equals(""))) {

          if ((String)get("reference") == null || ((String)get("reference")).trim().length() <= 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.general.requiredField", "Reference"));
          }

          if ((String)get("task") == null || ((String)get("task")).trim().length() <= 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                "error.general.requiredField", "Task"));
          }
        }

        if ((String)get("description") == null || ((String)get("description")).trim().length() <= 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
              "error.general.requiredField", "Description"));
        }
      }

      int startHrs = 0, startMins = 0, endHrs = 0, endMins = 0;

      if (((String)get("startTime")) != null && !(((String)get("startTime")).equals(""))) {
        int[] startTime = CVUtility.convertTimeTo24HrsFormat((String)get("startTime"));
        startHrs = startTime[0];
        startMins = startTime[1];
      }

      if (((String)get("endTime")) != null && !(((String)get("endTime")).equals(""))) {
        int[] endTime = CVUtility.convertTimeTo24HrsFormat((String)get("endTime"));
        endHrs = endTime[0];
        endMins = endTime[1];
      }

      if (startHrs > endHrs) {
        ActionMessage error = new ActionMessage("error.application.timecomparison", "EndTime",
            "StartTime");
        errors.add("error.application.timecomparison", error);
      } else if (startHrs == endHrs && startMins > endMins) {
        ActionMessage error = new ActionMessage("error.application.timecomparison", "EndTime",
            "StartTime");
        errors.add("error.application.timecomparison", error);
      }

      fBreakHrs = Float.parseFloat((String)get("breakHours"));
      fBreakMins = Float.parseFloat((String)get("breakMinutes"));
      fBreakTime = 0;

      if (fBreakMins != 0) {
        fBreakTime = fBreakHrs + fBreakMins / 60;
      } else {
        fBreakTime = fBreakHrs;
      }

      float remMin = 0;
      if (startMins != 0 && endMins != 0) {
        if (endMins > startMins) {
          remMin = 60 / (endMins - startMins);
        } else {
          remMin = 60 / (startMins - endMins);
        }
      }

      Float Duration = new Float((endHrs - startHrs) + remMin - fBreakTime);
      if (Duration.floatValue() <= 0.0) {
        ActionMessage error = new ActionMessage("error.projects.timeslip.totaltimediff",
            "Start and End Time difference", "BreakTime");
        errors.add("error.projects.timeslip.totaltimediff", error);
      }
    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    }
    return errors;
  }
}
