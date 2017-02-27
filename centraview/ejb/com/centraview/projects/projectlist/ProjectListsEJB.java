/*
 * $RCSfile: ProjectListsEJB.java,v $    $Revision: 1.4 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

package com.centraview.projects.projectlist;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.EJBUtil;
import com.centraview.common.IntMember;
import com.centraview.common.ProjectListElement;
import com.centraview.common.PureDateMember;
import com.centraview.common.PureTimeMember;
import com.centraview.common.StringMember;
import com.centraview.common.TimeSlipList;
import com.centraview.common.TimeSlipListElement;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This EJB gets all lists related to projects module.
 */
public class ProjectListsEJB implements SessionBean {
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "";

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

  public com.centraview.common.ProjectList getProjectList(int userID, HashMap info)
  {
    com.centraview.common.ProjectList projectsList = new com.centraview.common.ProjectList();
    try {
      Integer startATparam = (Integer)info.get("startATparam");
      Integer EndAtparam = (Integer)info.get("EndAtparam");
      String searchString = (String)info.get("searchString");
      String sortmem = (String)info.get("sortmem");
      Character chr = (Character)info.get("sortType");

      char sorttype = chr.charValue();
      int startat = startATparam.intValue();
      int endat = EndAtparam.intValue();
      int beginindex = Math.max(startat - 100, 1);
      int endindex = endat + 100;
      int entityID = ((Integer)info.get("entityID")).intValue();

      projectsList.setSortMember(sortmem);

      boolean allRecords = true;

      CVDal cvdl = new CVDal(dataSource);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `projectsearch`");
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `projectlist`");
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `projectaccess`");
      cvdl.executeUpdate();

      String otherWherClause = " (project.Owner = " + userID + " or project.Manager=" + userID
          + ")";
      CVUtility.getAllAccessibleRecords("Projects", "projectaccess", "project", "ProjectID",
          "Owner", otherWherClause, userID, cvdl);

      cvdl.clearParameters();

      if (sortmem.equals("Name"))
        sortmem = "projecttitle";
      else if (sortmem.equals("Entity"))
        sortmem = "EntityName";
      else if (sortmem.equals("DueDate"))
        sortmem = "End";

      String appendStr = "";
      Collection col = null;

      cvdl.setSql("project.createprojectlist");
      cvdl.executeUpdate();

      cvdl.setSql("project.insertprojectlist");
      cvdl.executeUpdate();

      cvdl.setSql("project.updateprojectlist");
      cvdl.executeUpdate();

      if (searchString != null && searchString.startsWith("ADVANCE:")) {
        searchString = searchString.substring(8);

        String str = "create TEMPORARY TABLE projectsearch " + searchString;
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.clearParameters();

        String sortStr = "";
        if (sorttype == 'A') {
          sortStr = " order by " + sortmem + " ";
        } else {
          sortStr = " order by " + sortmem + " DESc";
        }
        if (entityID == -1)// we get all
          str = "select pr.ProjectID, pr.ProjectTitle, pr.Description, pr.Status, pr.Start, pr.End, pr.EntityID, pr.EntityName from projectlist as pr, projectsearch WHERE pr.projectid = projectsearch.projectid "
              + sortStr;
        else
          // we filter over entity id
          str = "select pr.ProjectID, pr.ProjectTitle, pr.Description, pr.Status, pr.Start, pr.End, pr.EntityID, pr.EntityName from projectlist as pr, projectsearch WHERE pr.projectid = projectsearch.projectid and pr.EntityID="
              + entityID + sortStr;
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(str);
        col = cvdl.executeQuery();

        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE projectsearch");
        cvdl.executeUpdate();

        allRecords = false;

      } else {
        if (searchString.startsWith("SIMPLE :")) {
          searchString = searchString.substring(8);
          appendStr = " WHERE (ProjectID like '%" + searchString + "%' "
              + "OR projecttitle like '%" + searchString + "%' " + "OR Description like  '%"
              + searchString + "%' " + "OR status like  '%" + searchString + "%' "
              + "OR start like  '%" + searchString + "%' " + "OR end like  '%" + searchString
              + "%' " + "OR EntityID like  '%" + searchString + "%' " + "OR EntityName like '%"
              + searchString + "%' ) ";

          allRecords = false;
        }

        String str;// cwang
        if (entityID == -1)// we get all
          str = "SELECT * FROM projectlist";
        else
          // we filter over entity id
          str = "SELECT * FROM projectlist WHERE EntityID=" + entityID;

        if (sorttype == 'A') {
          str = str + appendStr + " order by " + sortmem + " limit " + (beginindex - 1) + " , "
              + (endindex + 1) + ";";
          cvdl.setSqlQuery(str);
        } else {
          str = str + appendStr + " order by " + sortmem + " desc limit " + (beginindex - 1)
              + " , " + (endindex + 1) + ";";
          cvdl.setSqlQuery(str);
        }

        col = cvdl.executeQuery();

      }

      cvdl.setSql("project.deleteprojectlist");
      cvdl.executeUpdate();

      Iterator it = col.iterator();
      int i = 0;
      ModuleFieldRightMatrix mfrmx = CVUtility.getUserModuleRight("Projects", userID, false,
          this.dataSource);
      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();

        int projectID = ((Long)hm.get("ProjectID")).intValue();

        StringMember title, entityName, status, five;
        PureDateMember dueDate;
        IntMember entityId;
        IntMember intmem = new IntMember("ProjectID", projectID, 10, "URL", 'T', false, 10);

        if (hm.get("ProjectTitle") != null)
          title = new StringMember("Name", (String)hm.get("ProjectTitle"), 10, "", 'T', true);
        else
          title = new StringMember("Name", "", 10, "", 'T', true);

        if (hm.get("EntityName") != null)
          entityName = new StringMember("Entity", (String)hm.get("EntityName"), mfrmx
              .getFieldRight("Projects", "entity"), "URL", 'T', true);
        else
          entityName = new StringMember("Entity", "", mfrmx.getFieldRight("Projects", "entity"),
              "URL", 'T', true);

        if (hm.get("EntityID") != null)
          entityId = new IntMember("EntityID", ((Long)hm.get("EntityID")).intValue(), 10, "URL",
              'T', false, 10);
        else
          entityId = new IntMember("EntityID", 0, 10, "URL", 'T', false, 10);

        status = new StringMember("Status", (String)hm.get("Status"), mfrmx.getFieldRight(
            "Projects", "status"), "URL", 'T', false);

        if (hm.get("End") != null) {
          java.util.Date dt = (java.util.Date)hm.get("End");
          Calendar cal = Calendar.getInstance();
          cal.setTime(dt);
          cal.set(Calendar.HOUR, 12);
          cal.set(Calendar.MINUTE, 0);
          dueDate = new PureDateMember("DueDate", cal.getTime(), mfrmx.getFieldRight("Projects",
              "end"), "URL", 'T', false, 100, "EST");
        } else
          dueDate = new PureDateMember("DueDate", null, mfrmx.getFieldRight("Projects", "end"),
              "URL", 'T', false, 100, "EST");

        ProjectListElement ele = new ProjectListElement(projectID);
        ele.put("ProjectID", intmem);
        ele.put("Name", title);
        ele.put("Entity", entityName);
        ele.put("Status", status);
        ele.put("DueDate", dueDate);
        ele.put("EntityID", entityId);

        StringBuffer sb = new StringBuffer("00000000000");
        sb.setLength(11);
        String str = (new Integer(i)).toString();
        sb.replace((sb.length() - str.length()), (sb.length()), str);
        String newOrd = sb.toString();

        cvdl.clearParameters();
        if (!allRecords) {
          projectsList.setTotalNoOfRecords(projectsList.size());
        } else {
          int count = 0;
          cvdl.setSql("project.selectprojectcount");
          Collection col2 = cvdl.executeQuery();
          Iterator ite2 = col2.iterator();
          if (ite2.hasNext()) {
            HashMap hm2 = (HashMap)ite2.next();
            count = ((Integer)hm2.get("count(pr.projectid)")).intValue();

          }
          projectsList.setTotalNoOfRecords(count);
        }
        projectsList.put(newOrd, ele);
        projectsList.setListType("Project");
        projectsList.setBeginIndex(beginindex);
        projectsList.setEndIndex(endindex);

      }

      cvdl.clearParameters();

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE projectaccess");
      cvdl.executeUpdate();

      cvdl.destroy();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return projectsList;
  }

  public com.centraview.common.TimeSlipList getAllTimeSlipList(int userID, HashMap info)
  {
    com.centraview.common.TimeSlipList timeSlipList = new com.centraview.common.TimeSlipList();
    try {
      Integer startATparam = (Integer)info.get("startATparam");
      Integer EndAtparam = (Integer)info.get("EndAtparam");
      String searchString = (String)info.get("searchString");
      String sortmem = (String)info.get("sortmem");
      Character chr = (Character)info.get("sortType");

      char sorttype = chr.charValue();
      int startat = startATparam.intValue();
      int endat = EndAtparam.intValue();
      int beginindex = Math.max(startat - 100, 1);
      int endindex = endat + 100;
      timeSlipList.setSortMember(sortmem);

      if (sortmem.equals("ID"))
        sortmem = "TimeSlipID";
      else if (sortmem.equals("Description"))
        sortmem = "Description";
      else if (sortmem.equals("Project"))
        sortmem = "project.ProjectTitle";
      else if (sortmem.equals("Task"))
        sortmem = "activity.title";
      else if (sortmem.equals("Duration"))
        sortmem = "Start";
      else if (sortmem.equals("CreatedBy"))
        sortmem = "CreatedBy";
      else if (sortmem.equals("Date"))
        sortmem = "Date";
      else if (sortmem.equals("StartTime"))
        sortmem = "Start";
      else if (sortmem.equals("EndTime"))
        sortmem = "End";

      boolean allRecords = true;

      CVDal cvdl = new CVDal(dataSource);
      CVUtility.getAllAccessibleRecords("Time Slips", "timeslipaccess", "timeslip", "TimeSlipID",
          "CreatedBy", null, userID, cvdl);

      Collection col = null;

      if (searchString != null && searchString.startsWith("ADVANCE:")) {
        searchString = searchString.substring(8);

        String str = "create TEMPORARY TABLE timeslipsearch " + searchString;
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.clearParameters();

        str = "select ts.TimeSlipID, ts.ProjectID, project.ProjectTitle, ts.Description, "
            + "ts.CreatedBy, individual.FirstName, individual.LastName, "
            + "ts.activityid, ts.BreakTime, ts.Date, ts.Start, ts.End, activity.title "
            + "from timeslip ts, timeslipsearch, timeslipaccess tla "
            + "left outer join project on  ts.ProjectID = project.ProjectID "
            + "left outer join individual on ts.CreatedBy = individual.IndividualID "
            + "left outer join task on ts.activityID = task.activityID "
            + "left outer join activity on task.activityid = activity.activityid "
            + "WHERE timeslipsearch.timeslipid =  ts.timeslipid and tla.TimeSlipID=ts.TimeSlipID";

        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(str);
        col = cvdl.executeQuery();

        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE timeslipsearch");
        cvdl.executeUpdate();

        allRecords = false;

      } else {
        String appendStr = "";
        if (searchString.startsWith("SIMPLE :")) {
          searchString = searchString.substring(8);
          appendStr = " and (ts.TimeSlipID like '%" + searchString + "%' "
              + "OR ts.ProjectID like '%" + searchString + "%' "
              + "OR project.ProjectTitle like  '%" + searchString + "%' "
              + "OR ts.Description like  '%" + searchString + "%' " + "OR ts.CreatedBy like  '%"
              + searchString + "%' " + "OR individual.FirstName like  '%" + searchString + "%' "
              + "OR individual.LastName like  '%" + searchString + "%' "
              + "OR ts.activityid like  '%" + searchString + "%' " + "OR ts.BreakTime like  '%"
              + searchString + "%' " + "OR ts.Date like  '%" + searchString + "%' "
              + "OR ts.End like  '%" + searchString + "%' " + "OR activity.title like  '%"
              + searchString + "%' " + "OR ts.Start like '%" + searchString + "%' ) ";

          allRecords = false;
        }

        if (sorttype == 'A') {
          String str = "select ts.TimeSlipID, ts.ProjectID, project.ProjectTitle, ts.Description, ts.CreatedBy, individual.FirstName, individual.LastName, ts.activityid, ts.BreakTime, ts.Date, ts.Start, ts.End, activity.title from timeslipaccess tla, timeslip  ts left outer join project on  ts.ProjectID = project.ProjectID left outer join individual on ts.CreatedBy = individual.IndividualID left outer join task on ts.activityID = task.activityID left outer join activity on task.activityid = activity.activityid where tla.TimeSlipID=ts.TimeSlipID "
              + appendStr
              + " order by "
              + sortmem
              + " limit "
              + (beginindex - 1)
              + " , "
              + (endindex + 1) + ";";
          cvdl.setSqlQuery(str);
        } else {
          String str = "select ts.TimeSlipID, ts.ProjectID, project.ProjectTitle, ts.Description, ts.CreatedBy, individual.FirstName, individual.LastName, ts.activityid, ts.BreakTime, ts.Date, ts.Start, ts.End, activity.title from timeslipaccess tla, timeslip  ts left outer join project on  ts.ProjectID = project.ProjectID left outer join individual on ts.CreatedBy = individual.IndividualID left outer join task on ts.activityID = task.activityID left outer join activity on task.activityid = activity.activityid where tla.TimeSlipID=ts.TimeSlipID "
              + appendStr
              + " order by "
              + sortmem
              + " desc limit "
              + (beginindex - 1)
              + " , "
              + (endindex + 1) + ";";
          cvdl.setSqlQuery(str);
        }

        col = cvdl.executeQuery();

      }

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();
      cvdl.setSqlQuery("drop table timeslipaccess");
      cvdl.executeUpdate();

      ModuleFieldRightMatrix mfrmx = CVUtility.getUserModuleRight("Time Slip", userID, false,
          this.dataSource);

      Iterator it = col.iterator();
      int i = 0;
      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();

        int timeSlipID = ((Long)hm.get("TimeSlipID")).intValue();
        int projectID = 0;

        if (hm.get("ProjectID") != null)
          projectID = ((Long)hm.get("ProjectID")).intValue();

        StringMember description, projectName = null, taskName = null, createdBy = null;
        PureDateMember date = null;
        PureTimeMember startTime = null, endTime = null;
        IntMember creator, taskId;

        IntMember intmem = new IntMember("ID", timeSlipID, 10, "URL", 'T', true, 10);
        IntMember projectId = new IntMember("ProjectID", projectID, 10, "URL", 'T', true, 10);

        Time tStartTime = (Time)hm.get("Start");
        Time tEndTime = (Time)hm.get("End");

        java.util.Date dt = (java.util.Date)hm.get("Date");

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(dt.getYear(), dt.getMonth(), dt.getDay(), tStartTime.getHours(),
            tStartTime.getMinutes());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(dt.getYear(), dt.getMonth(), dt.getDay(), tEndTime.getHours(), tEndTime
            .getMinutes());

        long startMili = calendarStart.getTimeInMillis();
        long endMili = calendarEnd.getTimeInMillis();

        long diff = endMili - startMili;

        Time difTime = new Time(diff);

        int startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);// startHrsmm[0];
        int startMins = calendarStart.get(Calendar.MINUTE);// startHrsmm[1];

        int endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);// endHrsmm[0];
        int endMins = calendarEnd.get(Calendar.MINUTE);// endHrsmm[1];

        float Duration = 0;

        Number dBreakTime = (Number)hm.get("BreakTime");
        float BreakTime = dBreakTime.floatValue();
        float remMin = 0;

        if (startMins != 0 && endMins != 0 && (startMins - endMins != 0)) {
          if (endMins > startMins)
            remMin = 60 / (endMins - startMins);
          else
            remMin = 60 / (startMins - endMins);
        }

        Duration = (endHrs - startHrs) + remMin - BreakTime;

        if (hm.get("Description") != null)
          description = new StringMember("Description", (String)hm.get("Description"), 10, "", 'T',
              false);
        else
          description = new StringMember("Description", "", 10, "", 'T', false);

        if (hm.get("ProjectTitle") != null)
          projectName = new StringMember("Project", (String)hm.get("ProjectTitle"), mfrmx
              .getFieldRight("Time Slip", "project"), "URL", 'T', true);
        else
          projectName = new StringMember("Project", "",
              mfrmx.getFieldRight("Time Slip", "project"), "URL", 'T', true);

        if (hm.get("title") != null)
          taskName = new StringMember("Task", (String)hm.get("title"), mfrmx.getFieldRight(
              "Time Slip", "task"), "URL", 'T', true);
        else
          taskName = new StringMember("Task", "", mfrmx.getFieldRight("Time Slip", "task"), "URL",
              'T', true);

        if (hm.get("activityid") != null)
          taskId = new IntMember("TaskID", ((Integer)hm.get("activityid")).intValue(), 10, "URL",
              'T', true, 10);
        else
          taskId = new IntMember("TaskID", 0, 10, "URL", 'T', true, 10);

        String firstName = (String)hm.get("FirstName");
        String lastName = (String)hm.get("LastName");

        String created = (firstName != null ? firstName + " " : "")
            + (lastName != null ? lastName : "");

        if (!created.equals(""))
          createdBy = new StringMember("CreatedBy", created, 10, "", 'T', true);
        else
          createdBy = new StringMember("CreatedBy", "", 10, "", 'T', true);

        if (hm.get("CreatedBy") != null)
          creator = new IntMember("Creator", ((Integer)hm.get("CreatedBy")).intValue(), 10, "URL",
              'T', false, 10);
        else
          creator = new IntMember("Creator", 0, 10, "URL", 'T', false, 10);

        if (hm.get("Date") != null)
          date = new PureDateMember("Date", (java.util.Date)hm.get("Date"), 10, "URL", 'T', false,
              100, "EST");
        else
          date = new PureDateMember("Date", null, 10, "URL", 'T', false, 100, "EST");

        if (hm.get("Start") != null)
          startTime = new PureTimeMember("StartTime", (java.util.Date)hm.get("Start"), 10, "URL",
              'T', false, 100, "EST");
        else
          startTime = new PureTimeMember("StartTime", null, 10, "URL", 'T', false, 100, "EST");

        if (hm.get("End") != null)
          endTime = new PureTimeMember("EndTime", (java.util.Date)hm.get("End"), 10, "URL", 'T',
              false, 100, "EST");
        else
          endTime = new PureTimeMember("EndTime", null, 10, "URL", 'T', false, 100, "EST");

        TimeSlipListElement ele = new TimeSlipListElement(timeSlipID);
        ele.put("ID", intmem);
        ele.put("TaskID", taskId);
        ele.put("Description", description);
        ele.put("Project", projectName);
        ele.put("ProjectID", projectId);
        ele.put("Task", taskName);
        if (Duration > 1.0) {
          StringMember duration = new StringMember("Duration", Float.toString(Duration) + " hours",
              10, "URL", 'T', false);
          ele.put("Duration", duration);
        } else if (Duration == 1.0) {
          StringMember duration = new StringMember("Duration", Float.toString(Duration) + " hour",
              10, "URL", 'T', false);
          ele.put("Duration", duration);
        } else if (Duration > 0.0 && Duration < 1.0) {
          StringMember duration = new StringMember("Duration", Float.toString(Duration * 60)
              + " minutes", 10, "URL", 'T', false);
          ele.put("Duration", duration);
        } else {
          StringMember duration = new StringMember("Duration", "", 10, "URL", 'T', false);
          ele.put("Duration", duration);
        }
        ele.put("CreatedBy", createdBy);
        ele.put("Creator", creator);
        ele.put("Date", date);
        ele.put("StartTime", startTime);
        ele.put("EndTime", endTime);

        StringBuffer sb = new StringBuffer("00000000000");
        sb.setLength(11);
        String str = (new Integer(i)).toString();
        sb.replace((sb.length() - str.length()), (sb.length()), str);
        String newOrd = sb.toString();

        if (!allRecords) {
          timeSlipList.setTotalNoOfRecords(timeSlipList.size());
        } else {
          int count = 0;
          cvdl.setSql("project.selecttimeslipcount");
          Collection col2 = cvdl.executeQuery();
          Iterator ite2 = col2.iterator();
          if (ite2.hasNext()) {
            HashMap hm2 = (HashMap)ite2.next();
            count = ((Integer)hm2.get("count(timeslipid)")).intValue();

          }
          timeSlipList.setTotalNoOfRecords(count);
        }

        timeSlipList.put(newOrd, ele);
        timeSlipList.setListType("Timeslip");
        timeSlipList.setBeginIndex(beginindex);
        timeSlipList.setEndIndex(endindex);

      }

      cvdl.clearParameters();
      cvdl.destroy();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return timeSlipList;
  }

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId, HashMap info)
  {

    com.centraview.common.TimeSlipList timeSlipList = new com.centraview.common.TimeSlipList();
    try {

      String sortmem = (String)info.get("sortmem");
      Character chr = (Character)info.get("sortType");

      if (sortmem.equals("Description"))
        sortmem = "Description";
      else if (sortmem.equals("Task"))
        sortmem = "activity.title";
      else if (sortmem.equals("Duration"))
        sortmem = "Start";
      else if (sortmem.equals("CreatedBy"))
        sortmem = "CreatedBy";
      else if (sortmem.equals("Date"))
        sortmem = "Date";
      else if (sortmem.equals("StartTime"))
        sortmem = "Start";
      else if (sortmem.equals("EndTime"))
        sortmem = "End";

      char sorttype = chr.charValue();

      String sortStr = "";
      if (sorttype == 'A') {
        sortStr = " order by " + sortmem + " ";
      } else {
        sortStr = " order by " + sortmem + " DESC";
      }

      CVDal cvdl = new CVDal(dataSource);

      Collection col = null;

      String strTs = "select ts.TimeSlipID, ts.ProjectID, project.ProjectTitle, ts.Description, ts.CreatedBy, individual.FirstName, individual.LastName, ts.activityid, ts.BreakTime, ts.Date, ts.Start, ts.End, activity.title from timeslip  ts left outer join project on  ts.ProjectID = project.ProjectID "
          + "left outer join individual on ts.CreatedBy = individual.IndividualID left outer join task on ts.activityID = task.activityID left outer join activity on task.activityid = activity.activityid where ts.projectid = "
          + projectId + " " + sortStr;

      cvdl.setSqlQuery(strTs);

      col = cvdl.executeQuery();

      Iterator it = col.iterator();
      int i = 0;
      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();

        int timeSlipID = ((Long)hm.get("TimeSlipID")).intValue();
        int projectID = ((Long)hm.get("ProjectID")).intValue();

        StringMember description, projectName = null, taskName = null, createdBy = null;
        PureDateMember date = null;
        PureTimeMember startTime = null, endTime = null;
        IntMember creator, taskId;

        IntMember intmem = new IntMember("ID", timeSlipID, 10, "URL", 'T', true, 10);
        IntMember projectId1 = new IntMember("ProjectID", projectID, 10, "URL", 'T', true, 10);

        Time tStartTime = (Time)hm.get("Start");
        Time tEndTime = (Time)hm.get("End");

        java.util.Date dt = (java.util.Date)hm.get("Date");

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(dt.getYear(), dt.getMonth(), dt.getDay(), tStartTime.getHours(),
            tStartTime.getMinutes());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(dt.getYear(), dt.getMonth(), dt.getDay(), tEndTime.getHours(), tEndTime
            .getMinutes());

        long startMili = calendarStart.getTimeInMillis();
        long endMili = calendarEnd.getTimeInMillis();

        long diff = endMili - startMili;
        Time difTime = new Time(diff);

        int startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);// startHrsmm[0];
        int startMins = calendarStart.get(Calendar.MINUTE);// startHrsmm[1];

        int endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);// endHrsmm[0];
        int endMins = calendarEnd.get(Calendar.MINUTE);// endHrsmm[1];

        float Duration = 0;

        Double dBreakTime = (Double)hm.get("BreakTime");
        float BreakTime = dBreakTime.floatValue();
        float remMin = 0;

        if (startMins != 0 && endMins != 0 && startMins != endMins) {
          if (endMins > startMins)
            remMin = 60 / (endMins - startMins);
          else
            remMin = 60 / (startMins - endMins);
        }

        Duration = (endHrs - startHrs) + remMin - BreakTime;

        StringMember duration = new StringMember("Duration", Float.toString(Duration), 10, "URL",
            'T', false);

        if (hm.get("Description") != null)
          description = new StringMember("Description", (String)hm.get("Description"), 10, "", 'T',
              false);
        else
          description = new StringMember("Description", "", 10, "", 'T', false);

        if (hm.get("ProjectTitle") != null)
          projectName = new StringMember("Project", (String)hm.get("ProjectTitle"), 10, "URL", 'T',
              true);
        else
          projectName = new StringMember("Project", "", 10, "URL", 'T', true);

        if (hm.get("title") != null)
          taskName = new StringMember("Task", (String)hm.get("title"), 10, "URL", 'T', true);
        else
          taskName = new StringMember("Task", "", 10, "URL", 'T', true);

        if (hm.get("activityid") != null)
          taskId = new IntMember("TaskID", ((Integer)hm.get("activityid")).intValue(), 10, "URL",
              'T', true, 10);
        else
          taskId = new IntMember("TaskID", 0, 10, "URL", 'T', true, 10);

        String firstName = (String)hm.get("FirstName");
        String lastName = (String)hm.get("LastName");

        String created = (firstName != null ? firstName + " " : "")
            + (lastName != null ? lastName : "");

        if (!created.equals(""))
          createdBy = new StringMember("CreatedBy", created, 10, "", 'T', true);
        else
          createdBy = new StringMember("CreatedBy", "", 10, "", 'T', true);

        if (hm.get("CreatedBy") != null)
          creator = new IntMember("Creator", ((Integer)hm.get("CreatedBy")).intValue(), 10, "URL",
              'T', false, 10);
        else
          creator = new IntMember("Creator", 0, 10, "URL", 'T', false, 10);

        if (hm.get("Date") != null)
          date = new PureDateMember("Date", (java.util.Date)hm.get("Date"), 10, "URL", 'T', false,
              100, "EST");
        else
          date = new PureDateMember("Date", null, 10, "URL", 'T', false, 100, "EST");

        if (hm.get("Start") != null)
          startTime = new PureTimeMember("StartTime", (java.util.Date)hm.get("Start"), 10, "URL",
              'T', false, 100, "EST");
        else
          startTime = new PureTimeMember("StartTime", null, 10, "URL", 'T', false, 100, "EST");

        if (hm.get("End") != null)
          endTime = new PureTimeMember("EndTime", (java.util.Date)hm.get("End"), 10, "URL", 'T',
              false, 100, "EST");
        else
          endTime = new PureTimeMember("EndTime", null, 10, "URL", 'T', false, 100, "EST");

        TimeSlipListElement ele = new TimeSlipListElement(timeSlipID);
        ele.put("ID", intmem);
        ele.put("TaskID", taskId);
        ele.put("Description", description);
        ele.put("Project", projectName);
        ele.put("ProjectID", projectId1);
        ele.put("Task", taskName);
        ele.put("Duration", duration);
        ele.put("CreatedBy", createdBy);
        ele.put("Creator", creator);
        ele.put("Date", date);
        ele.put("StartTime", startTime);
        ele.put("EndTime", endTime);

        StringBuffer sb = new StringBuffer("00000000000");
        sb.setLength(11);
        String str = (new Integer(i)).toString();
        sb.replace((sb.length() - str.length()), (sb.length()), str);
        String newOrd = sb.toString();

        timeSlipList.put(newOrd, ele);
        timeSlipList.setTotalNoOfRecords(timeSlipList.size());
        timeSlipList.setListType("Timeslip");
        timeSlipList.setBeginIndex(1);
        timeSlipList.setEndIndex(timeSlipList.size());

      }

      cvdl.clearParameters();
      cvdl.destroy();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return timeSlipList;
  }

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId)
  {

    com.centraview.common.TimeSlipList timeSlipList = new com.centraview.common.TimeSlipList();
    try {
      CVDal cvdl = new CVDal(dataSource);

      Collection col = null;

      cvdl.setSql("project.projecttimeslip");
      cvdl.setInt(1, projectId);
      col = cvdl.executeQuery();

      Iterator it = col.iterator();
      int i = 0;
      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();

        int timeSlipID = ((Long)hm.get("TimeSlipID")).intValue();
        int projectID = ((Long)hm.get("ProjectID")).intValue();

        StringMember description, projectName = null, taskName = null, createdBy = null;
        PureDateMember date = null;
        PureTimeMember startTime = null, endTime = null;
        IntMember creator, taskId;

        IntMember intmem = new IntMember("ID", timeSlipID, 10, "URL", 'T', true, 10);
        IntMember projectId1 = new IntMember("ProjectID", projectID, 10, "URL", 'T', true, 10);

        Time tStartTime = (Time)hm.get("Start");
        Time tEndTime = (Time)hm.get("End");

        java.util.Date dt = (java.util.Date)hm.get("Date");

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(dt.getYear(), dt.getMonth(), dt.getDay(), tStartTime.getHours(),
            tStartTime.getMinutes());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(dt.getYear(), dt.getMonth(), dt.getDay(), tEndTime.getHours(), tEndTime
            .getMinutes());

        long startMili = calendarStart.getTimeInMillis();
        long endMili = calendarEnd.getTimeInMillis();

        long diff = endMili - startMili;
        Time difTime = new Time(diff);
        int startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);// startHrsmm[0];
        int startMins = calendarStart.get(Calendar.MINUTE);// startHrsmm[1];

        int endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);// endHrsmm[0];
        int endMins = calendarEnd.get(Calendar.MINUTE);// endHrsmm[1];

        float Duration = 0;

        Double dBreakTime = (Double)hm.get("BreakTime");
        float BreakTime = dBreakTime.floatValue();
        float remMin = 0;

        if (startMins != 0 && endMins != 0 && startMins != endMins) {
          if (endMins > startMins)
            remMin = 60 / (endMins - startMins);
          else
            remMin = 60 / (startMins - endMins);
        }

        Duration = (endHrs - startHrs) + remMin - BreakTime;

        StringMember duration = new StringMember("Duration", Float.toString(Duration), 10, "URL",
            'T', false);

        if (hm.get("Description") != null)
          description = new StringMember("Description", (String)hm.get("Description"), 10, "", 'T',
              false);
        else
          description = new StringMember("Description", "", 10, "", 'T', false);

        if (hm.get("ProjectTitle") != null)
          projectName = new StringMember("Project", (String)hm.get("ProjectTitle"), 10, "URL", 'T',
              true);
        else
          projectName = new StringMember("Project", "", 10, "URL", 'T', true);

        if (hm.get("title") != null)
          taskName = new StringMember("Task", (String)hm.get("title"), 10, "URL", 'T', true);
        else
          taskName = new StringMember("Task", "", 10, "URL", 'T', true);

        if (hm.get("activityid") != null)
          taskId = new IntMember("TaskID", ((Integer)hm.get("activityid")).intValue(), 10, "URL",
              'T', true, 10);
        else
          taskId = new IntMember("TaskID", 0, 10, "URL", 'T', true, 10);

        String firstName = (String)hm.get("FirstName");
        String lastName = (String)hm.get("LastName");

        String created = (firstName != null ? firstName + " " : "")
            + (lastName != null ? lastName : "");

        if (!created.equals(""))
          createdBy = new StringMember("CreatedBy", created, 10, "", 'T', true);
        else
          createdBy = new StringMember("CreatedBy", "", 10, "", 'T', true);

        if (hm.get("CreatedBy") != null)
          creator = new IntMember("Creator", ((Integer)hm.get("CreatedBy")).intValue(), 10, "URL",
              'T', false, 10);
        else
          creator = new IntMember("Creator", 0, 10, "URL", 'T', false, 10);

        if (hm.get("Date") != null)
          date = new PureDateMember("Date", (java.util.Date)hm.get("Date"), 10, "URL", 'T', false,
              100, "EST");
        else
          date = new PureDateMember("Date", null, 10, "URL", 'T', false, 100, "EST");

        if (hm.get("Start") != null)
          startTime = new PureTimeMember("StartTime", (java.util.Date)hm.get("Start"), 10, "URL",
              'T', false, 100, "EST");
        else
          startTime = new PureTimeMember("StartTime", null, 10, "URL", 'T', false, 100, "EST");

        if (hm.get("End") != null)
          endTime = new PureTimeMember("EndTime", (java.util.Date)hm.get("End"), 10, "URL", 'T',
              false, 100, "EST");
        else
          endTime = new PureTimeMember("EndTime", null, 10, "URL", 'T', false, 100, "EST");

        TimeSlipListElement ele = new TimeSlipListElement(timeSlipID);
        ele.put("ID", intmem);
        ele.put("TaskID", taskId);
        ele.put("Description", description);
        ele.put("Project", projectName);
        ele.put("ProjectID", projectId1);
        ele.put("Task", taskName);
        ele.put("Duration", duration);
        ele.put("CreatedBy", createdBy);
        ele.put("Creator", creator);
        ele.put("Date", date);
        ele.put("StartTime", startTime);
        ele.put("EndTime", endTime);

        StringBuffer sb = new StringBuffer("00000000000");
        sb.setLength(11);
        String str = (new Integer(i)).toString();
        sb.replace((sb.length() - str.length()), (sb.length()), str);
        String newOrd = sb.toString();

        timeSlipList.put(newOrd, ele);
        timeSlipList.setTotalNoOfRecords(timeSlipList.size());
        timeSlipList.setListType("Timeslip");
        timeSlipList.setBeginIndex(1);
        timeSlipList.setEndIndex(timeSlipList.size());

      }

      cvdl.clearParameters();
      cvdl.destroy();

    } catch (Exception e) {
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
   * Returns a ValueListVO representing a list of Task records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getTaskValueList(int individualId, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();

    boolean applyFilter = false;
    String filter = parameters.getFilter();

    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE tasklistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }

    String query = this.buildTaskListQuery(applyFilter, individualId, cvdl, parameters);
    cvdl.setSqlQuery(query);
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE alltasklist");
    cvdl.executeUpdate();

    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE tasklistfilter");
      cvdl.executeUpdate();
    }

    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildTaskListQuery(boolean applyFilter, int individualId, CVDal cvdl,
      ValueListParameters parameters)
  {
    // Create table column definitions
    String create = "CREATE TEMPORARY TABLE alltasklist ";
    String select = "SELECT a.activityid, a.title, t.milestone, a.start, a.duedate, "
        + "t.percentcomplete, p.projectid, p.projecttitle, a.owner, "
        + "CONCAT(i.firstname, ' ', i.lastname) name, ap.name priority, ast.name "
        + "status, a.creator, CONCAT(i.firstname, ' ', i.lastname) createdby, "
        + "a.created, CONCAT(i.firstname, ' ', i.lastname) managername, 0 managerid ";
    String from = "FROM activity a, task t, project p, individual i, activitypriority ap, "
        + "activitystatus ast ";
    String where = "WHERE 1 = 0";
    String query = create + select + from + where;
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    // Populate all tasks
    String insert = "INSERT INTO alltasklist(activityid, title, milestone, Start, DueDate, "
        + "percentcomplete, projectid,projecttitle, owner, priority, status, "
        + "creator,created) ";
    select = "SELECT t.activityid, a.title, t.milestone, a.start, a.duedate, "
        + "t.percentcomplete, t.projectid, p.projecttitle, a.owner, ap.name, "
        + "ast.name, a.creator, a.created ";
    from = "FROM activity a, task t, project p";
    String joins = "LEFT OUTER JOIN activitypriority ap ON a.priority = ap.priorityid "
        + "LEFT OUTER JOIN activitystatus ast ON a.status = ast.statusid ";
    where = "WHERE t.projectid = p.projectid AND t.activityid = a.activityid AND " + "a.owner = '"
        + individualId + "' ";
    if (applyFilter) {
      from += ", tasklistfilter tlf ";
      where += " AND tlf.ActivityId = t.ActivityId ";
    }
    query = insert + select + from + joins + where + "UNION ";
    select = "SELECT t.activityid, a.title, t.milestone, a.start, a.duedate, "
        + "t.percentcomplete, t.projectid, p.projecttitle, a.owner, ap.name, "
        + "ast.name, a.creator, a.created ";
    from = "FROM activity a ";
    joins = "LEFT OUTER JOIN activitylink al ON a.activityid = al.activityid, "
        + "task t, project p "
        + "LEFT OUTER JOIN activitypriority ap ON a.priority = ap.priorityid "
        + "LEFT OUTER JOIN activitystatus ast ON a.status = ast.statusid ";
    where = "WHERE t.projectid = p.projectid AND t.activityid = a.activityid AND "
        + "al.RecordTypeID = 2 AND al.RecordID = '" + individualId + "' ";
    if (applyFilter) {
      from += ", tasklistfilter tlf ";
      where += " AND tlf.ActivityId = t.ActivityId ";
    }
    query += select + from + joins + where + "UNION ";
    select = "SELECT t.activityid, a.title, t.milestone, a.start, a.duedate, "
        + "t.percentcomplete, t.projectid, p.projecttitle, a.owner, ap.name, "
        + "ast.name, a.creator, a.created ";
    from = "FROM activity a, taskassigned ta, task t, project p ";
    joins = "LEFT OUTER JOIN activitypriority ap ON a.priority = ap.priorityid "
        + "LEFT OUTER JOIN activitystatus ast ON a.status = ast.statusid ";
    where = "WHERE t.projectid = p.projectid AND t.activityid = a.activityid AND "
        + "ta.taskid = t.activityid AND ta.AssignedTo = '" + individualId + "' ";
    if (applyFilter) {
      from += ", tasklistfilter tlf ";
      where += " AND tlf.ActivityId = t.ActivityId ";
    }
    query += select + from + joins + where;
    cvdl.setSqlQuery(query);
    int count = cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    parameters.setTotalRecords(count);

    // Various updates
    cvdl.setSqlQuery("UPDATE alltasklist atl, individual i SET atl.name = "
        + "CONCAT(i.firstname, ' ', i.lastname) WHERE atl.owner = i.individualid");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE alltasklist atl, individual i SET atl.createdby = "
        + "CONCAT(i.firstname, ' ', i.lastname) WHERE atl.creator = i.individualid");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("UPDATE alltasklist atl, individual i, activitylink al SET "
        + "atl.managername = CONCAT(i.firstname, ' ', i.lastname), atl.managerid = "
        + "i.IndividualID WHERE atl.activityid = al.ActivityID AND "
        + "al.RecordTypeID = 2 AND al.RecordID = i.IndividualID");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    // Order and limit
    String orderBy = "ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // Finally, the query.
    query = "SELECT activityid, title, status, milestone, start, duedate, "
        + "percentcomplete, projectid, projecttitle, owner, name, priority, creator, "
        + "createdby, created, managername, managerid FROM alltasklist ";
    return (query + orderBy + limit);
  }

  /**
   * Returns a ValueListVO representing a list of Project records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getProjectValueList(int individualId, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    // data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an
    // arraylist
    // Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();
    // permissionSwitch turns the permission parts of the query on and off.
    // if individualId is less than zero then the list is requested without
    // limiting
    // rows based on record rights. If it is true than the rights are used.
    boolean permissionSwitch = individualId < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE projectlistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "projectlistfilter", individualId, 36,
          "project", "ProjectID", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 36, "project",
          "ProjectID", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildProjectListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    cvdl
        .setSqlQuery("UPDATE projectlist, projectlink, entity SET projectlist.EntityID =  projectlink.recordid , projectlist.Entity = entity.name WHERE entity.entityid = projectlink.recordid and projectlink.projectid = projectlist.projectid and projectlink.recordtypeid = 14;");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    cvdl.setSqlQuery("SELECT * FROM projectlist");
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE projectlist");
    cvdl.executeUpdate();
    // throw away the temp filter table, if necessary.
    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE projectlistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildProjectListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = "SELECT p.ProjectID, p.ProjectTitle As Name, e.EntityID, e.Name As Entity, "
        + " projectstatus.Title AS Status, p.End AS DueDate ";

    String joinConditions = " LEFT OUTER JOIN entity e ON (e.entityid = 0 ) "
        + " LEFT OUTER JOIN projectstatus ON (p.statusid = projectstatus.statusid) ";

    StringBuffer from = new StringBuffer(" FROM project AS p ");
    StringBuffer where = new StringBuffer(" WHERE 1=1 ");
    String orderBy = " ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary Project list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND p.ProjectID = lf.ProjectID");
    }

    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE projectlist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }

  /**
   * Returns a ValueListVO representing a list of Project records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getTimeslipsValueList(int individualId, ValueListParameters parameters)
  {

    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    // data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an
    // arraylist
    // Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();
    // permissionSwitch turns the permission parts of the query on and off.
    // if individualId is less than zero then the list is requested without
    // limiting
    // rows based on record rights. If it is true than the rights are used.
    boolean permissionSwitch = individualId < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0) {
      String str = "CREATE TABLE timeslipslistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "timeslipslistfilter", individualId, 38,
          "timeslip", "TimeSlipID", "CreatedBy", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 38, "timeslip",
          "TimeSlipID", "CreatedBy", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildTimeslipsListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();

    cvdl.setSqlQuery("SELECT * FROM timeslipslist");
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE timeslipslist");
    cvdl.executeUpdate();
    // throw away the temp filter table, if necessary.
    if (applyFilter) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE timeslipslistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch) {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }

  private String buildTimeslipsListQuery(boolean applyFilter, boolean permissionSwitch,
      ValueListParameters parameters)
  {
    String select = " SELECT ts.TimeSlipID, ts.Description, ts.ProjectID, project.ProjectTitle AS Project,"
        + " ts.CreatedBy As createdByID, concat(i.FirstName,' ',i.LastName) AS CreatedBy, ts.activityid, "
        + " activity.title AS Task, ts.BreakTime AS Duration, ts.Date, ts.Start AS StartTime, ts.End AS EndTime  ";

    String joinConditions = " LEFT OUTER JOIN project ON ( ts.ProjectID = project.ProjectID ) "
        + " LEFT OUTER JOIN individual i ON (ts.CreatedBy = i.IndividualID) "
        + " LEFT OUTER JOIN task ON (ts.activityID = task.activityID) "
        + " LEFT OUTER JOIN activity ON (task.activityid = activity.activityid) ";

    StringBuffer from = new StringBuffer(" FROM timeslip ts ");
    StringBuffer where = new StringBuffer(" WHERE 1=1 ");
    String orderBy = " ORDER BY "
        + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary Project list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND ts.TimeSlipID = lf.TimeSlipID");
    }

    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE timeslipslist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }
}
