/*
 * $RCSfile: TimeSheetVOX.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:43 $ - $Author: mcallist $
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

package com.centraview.hr.helper;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.DateUtility;
import com.centraview.common.UserObject;

public class TimeSheetVOX implements Serializable // extends TimeSheetVO
{
  private static Logger logger = Logger.getLogger(TimeSheetVOX.class);
  private TimeSheetVO objTimeSheetVO = null;

  public TimeSheetVOX(UserObject objUserObject, DynaValidatorForm form) {
    try {
      objTimeSheetVO = new TimeSheetVO();
      String reportingToId = (String)form.get("reportingToId");
      Integer iReportingToId = new Integer(reportingToId);
      int reportingId = iReportingToId.intValue();

      String semployeeId = (String)form.get("employeeId");
      Integer iEmployeeId = new Integer(semployeeId);
      int employeeId = iEmployeeId.intValue();
      String reportingTo = (String)form.get("reportingTo");
      String fromday = (String)form.get("fromday");
      String frommonth = (String)form.get("frommonth");
      String fromyear = (String)form.get("fromyear");
      String today = (String)form.get("today");
      String tomonth = (String)form.get("tomonth");
      String toyear = (String)form.get("toyear");
      String notes = (String)form.get("notes");
      String description = (String)form.get("description");
      int individualID = objUserObject.getIndividualID();
      Long lStatus = (Long)form.get("status1");

      java.sql.Date objTimeStampFrom = DateUtility.createDate(fromyear, frommonth, fromday);
      java.sql.Date objTimeStampTo = DateUtility.createDate(toyear, tomonth, today);

      String timesheetID = (String)form.get("timesheetID");
      if (timesheetID != null && !timesheetID.equals("")) {
        objTimeSheetVO.setTimesheetID(Integer.parseInt(timesheetID));
      }

      objTimeSheetVO.setReportingToId(reportingId);
      objTimeSheetVO.setReportingTo(reportingTo);
      objTimeSheetVO.setModifiedBy(individualID);
      objTimeSheetVO.setOwner(employeeId);
      objTimeSheetVO.setCreator(individualID);
      if (lStatus != null) {
        objTimeSheetVO.setStatus(lStatus.intValue());
      } else {
        objTimeSheetVO.setStatus(1);
      }
      objTimeSheetVO.setDescription(description);
      objTimeSheetVO.setNotes(notes);
      objTimeSheetVO.setFromDate(objTimeStampFrom);
      objTimeSheetVO.setToDate(objTimeStampTo);
    } catch (Exception e) {
      logger.error("[TimeSheetVOX]: Exception", e);
    }

  }

  public TimeSheetVO getTimeSheetVO()
  {
    return objTimeSheetVO;
  }
}