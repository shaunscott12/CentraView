/*
 * $RCSfile: ActivityHelperEJB.java,v $    $Revision: 1.4 $  $Date: 2005/10/07 18:23:33 $ - $Author: mcallist $
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


package com.centraview.activity.helper;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.emailsettings.EmailSettingsLocal;
import com.centraview.administration.emailsettings.EmailSettingsLocalHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.file.CvFileLocal;
import com.centraview.file.CvFileLocalHome;
import com.centraview.file.CvFileVO;
import com.centraview.mail.MailMessageVO;
import com.centraview.projects.project.ProjectLocal;
import com.centraview.projects.project.ProjectLocalHome;
import com.centraview.sale.opportunity.OpportunityLocal;
import com.centraview.sale.opportunity.OpportunityLocalHome;

/**
 * This class performs any Database operations with the Activity Objects.
 * @author CentraView, LLC.
 */
public class ActivityHelperEJB implements SessionBean
{
  private String dataSource = "MySqlDS";
  // will be used by private class only,
  // in one perticular session hence should not be a problem
  // specially the link method (file)
  /** the userId set so the link method knows the individualid */
  private int gbUserId;
  /** the logger for this class */
  private static Logger logger = Logger.getLogger(ActivityHelperEJB.class);
  /** EJB Session Context. */
  private SessionContext sc;
  public void ejbCreate()
  {}
  public void ejbRemove()
  {}
  public void ejbActivate()
  {}
  public void ejbPassivate()
  {}
  public void setSessionContext(SessionContext sc)
  {
    this.sc = sc;
  }
  /**
   * Calls the appropriate method to add a specific activity type based
   * on the activityType value in the ActivityVO "actvo" passed in. For
   * example, if actvo.getActivityType() was "1" (which is appointment),
   * then this method would call this.addAppointment.
   * @param actvo   A populated ActivityVO object containing the details of the activity record we are creating.
   * @param userID  The userID of the user creating this activity (and thus, the owner)
   * @return int: the activityID of that activity record which was created.
   */
  public int addActivity(ActivityVO actvo, int userId)
  {
    gbUserId = userId;
    CVDal cvdal = new CVDal(dataSource);
    int newActivityID = -1;
    try {
      newActivityID = this.addBasicActivity(actvo, cvdal, userId);
      if (newActivityID > 0) {
        this.addAttendee(newActivityID, actvo.getAttendee(), cvdal);
        this.addRecurrence(newActivityID, actvo.getRecurrence(), cvdal);
        this.addAction(newActivityID, actvo.getActivityAction(), cvdal);
        this.addResource(newActivityID, actvo.getResource(), cvdal);
        this.addActivityLink(newActivityID, actvo, cvdal);
      }
      Collection filteredAttendees = this.filterAttendees(actvo);
      // extraActions() needs the newActivityID to be set on the actvo,
      // but stuff above breaks if actvo has a valid activityID set, so
      // I'm setting the activityID here for the purposes of extraActions
      actvo.setActivityID(newActivityID);
      this.extraActions(actvo, filteredAttendees, userId);
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return newActivityID;
  } // end addActivity(ActivityVO,int) method
  public ActivityVO getActivity(int activityId, int userid)
  {
    gbUserId = userid;
    CVDal dl = new CVDal(dataSource);
    ActivityVO activityVO = new ActivityVO();
    try {
      this.getBasicActivity(activityId, activityVO, dl);
      activityVO.setRecurrence(this.getRecurrence(activityId, dl)); // with except
      this.getAttendee(activityId, activityVO, dl);
      this.getResource(activityId, activityVO, dl);
      this.getAction(activityId, activityVO, dl);
      this.getActivityLink(activityId, activityVO, dl);
    } finally {
      dl.destroy();
      dl = null;
    }
    return activityVO;
  }
  public void updateActivity(ActivityVO avo, int userId)
  {
    gbUserId = userId;
    if (avo != null && avo.getActivityID() > 0) {
      CVDal dl = new CVDal(dataSource);
      try {
        int actId = avo.getActivityID();
        this.updateBasicActivity(avo, dl, userId);
        this.updateAttendee(actId, avo.getAttendee(), dl);
        this.updateRecurrence(actId, avo.getRecurrence(), dl);
        this.updateAction(actId, avo.getActivityAction(), dl);
        this.updateResource(actId, avo.getResource(), dl);
        this.addActivityLink(actId, avo, dl);
        dl.clearParameters();
      } finally {
        dl.destroy();
        dl = null;
      }
    } // end if (avo != null && avo.getActivityID() > 0)
    this.extraActions(avo, filterAttendees(avo), userId);
  } // end updateActivity() method
  public void deleteActivity(int activityId, int userId)
  {
    gbUserId = userId;
    CVDal cvdal = new CVDal(dataSource);
    try {
      this.deleteAttendee(activityId, cvdal);
      this.deleteRecurExcept(activityId, cvdal);
      this.deleteRecurrence(activityId, cvdal);
      this.deleteResourceRelate(activityId, cvdal);
      this.deleteAction(activityId, cvdal);
      this.deleteActivityAction(activityId, cvdal);
      this.deleteActivityLink(activityId, cvdal);
      this.deleteBasicActivity(activityId, cvdal);
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
  } // end deleteActivity() method
  public String getTypeOfActivity(int activityId)
  {
    String actType = null;
    CVDal dl = new CVDal(dataSource);
    try {
      dl.clearParameters();
      dl.setSql("activity.getbasicactivity");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        HashMap hm = (HashMap)it.next();
        if (hm != null) {
          actType = (String)hm.get("Name");
        }
        if (actType != null) {
          actType = actType.toUpperCase();
        }
      }
    } finally {
      dl.destroy();
      dl = null;
    }
    return actType;
  } // end getTypeOfActivity() method
  /**
   * I have added this method to get the ActivityType of an existing
   * activity. The current scheme insists that you know the activity
   * type BEFORE obtaining the Activity Information. <strong>Note:
   * This method will return -1 if (for some reason) the ActivityType
   * is not set.</strong>
   * @param activityID The ID of the activity.
   * @return An int with the activity type. -1 if the activity type isn't set.
   */
  public int getActivityType(int activityID)
  {
    int activityType = -1;
    CVDal dl = new CVDal(dataSource);
    try {
      dl.clearParameters();
      dl.setSql("activity.getbasicactivity");
      dl.setInt(1, activityID);
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        HashMap hm = (HashMap)it.next();
        if (hm != null) {
          activityType = ((Number)hm.get("Type")).intValue();
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getActivityType: ", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return activityType;
  }
  public String getActivityTypeString(int activityType)
  {
    String returnValue = "Error";
    CVDal cvdl = new CVDal(dataSource);
    try {
      cvdl.setSqlQuery("SELECT typeid, name FROM activitytype");
      Collection sqlResults = cvdl.executeQuery();
      if (sqlResults != null) {
        Iterator iter = sqlResults.iterator();
        while (iter.hasNext()) {
          HashMap sqlRow = (HashMap)iter.next();
          if (((Integer)sqlRow.get("typeid")).intValue() == activityType) {
            returnValue = (String)sqlRow.get("name");
            break;
          }
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getActivityTypeString: ", e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return (returnValue);
  } // end getActivityTypeString() method
  private int addBasicActivity(ActivityVO act, CVDal dl, int userId)
  {
    int actId = 0;
    logger.debug("start: "+act.getActivityStartDate());
    logger.debug("end: "+act.getActivityEndDate());
    try {
      // insert into activity
      // (Type, Priority, Status, Title, DueDate, Details, Creator, Start, End, Location, AllDay, Owner, visibility, AttachmentType, Notes, CompletedDate, Created) 
      // values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())")
      // 1 = type, 2 = pri, 3 = status, 4 = title, 5 = due, 6 = details, 7 = created
      // 8 = start, 9 = end, 10 = location, 11 = allday, 12 = owner
      // 13 = visibility, 14 = AttachmentType, 15 = Notes, 16 = CompletedDate
      dl.setSqlQueryToNull();
      dl.setSql("activity.addactivity");
      dl.setInt(1, act.getActivityType());
      dl.setInt(2, act.getPriority());
      dl.setInt(3, act.getStatus());
      dl.setString(4, act.getTitle());
      dl.setTimestamp(5, act.getActivityDueDate());
      dl.setString(6, act.getActivityDetails());
      if (act.getCreatedBy() <= 0) {
        act.setCreatedBy(userId);
      }
      dl.setInt(7, act.getCreatedBy());
      dl.setTimestamp(8, act.getActivityStartDate());
      dl.setTimestamp(9, act.getActivityEndDate());
      dl.setString(10, act.getLocation());
      dl.setString(11, act.getIsAllDay());
      if (act.getOwner() <= 0) {
        act.setOwner(userId);
      }
      dl.setInt(12, act.getOwner());
      dl.setString(13, act.getVisibility());
      dl.setString(14, act.getAttachmentType());
      dl.setString(15, act.getNotes());
      if (act.getStatus() != 2) {
        act.setCompletedDate(null);
      }
      dl.setRealTimestamp(16, act.getCompletedDate());
      dl.executeUpdate();
      actId = dl.getAutoGeneratedKey();
      dl.clearParameters();
      if (act.getActivityType() == 2) {
        dl.setSql("activity.addcall");
        dl.setInt(1, actId);
        dl.setInt(2, act.getCallTypeId());
        dl.executeUpdate();
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.addBasicActivity: ", e);
    }
    return actId;
  } // end addBasicActivity(ActivityVO,CVDal,int) method
  private void addAttendee(int activityId, Collection attn, CVDal dl)
  {
    try {
      if (attn == null) {
        return;
      }
      //IMPORTANT put the duplicate check
      dl.clearParameters();
      dl.setSql("activity.addattendee");
      Iterator it = attn.iterator();
      while (it.hasNext()) {
        AttendeeVO atv = (AttendeeVO)it.next();
        dl.setInt(1, activityId);
        dl.setInt(2, atv.getContactID());
        dl.setString(3, atv.getAttendeeType());
        dl.setInt(4, atv.getAttendeeStatus());
        dl.executeUpdate();
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.addAttendee: ", e);
    }
  } // end addAttendee(int,Collection,CVDal) method
  private void addRecurrence(int actId, RecurrenceVO rv, CVDal dl)
  {
    try {
      if (rv == null) {
        return;
      }
      int every = rv.getEvery();
      dl.clearParameters();
      dl.setSql("activity.addrecurrence");
      dl.setInt(1, actId);
      dl.setInt(2, every);
      dl.setString(3, rv.getTimePeriod());
      dl.setInt(4, rv.getOn());
      dl.setDate(5, rv.getStartDate());
      dl.setDate(6, rv.getUntil());
      dl.executeUpdate();
      int recurId = dl.getAutoGeneratedKey();
      ArrayList rexDt = rv.getRecurrenceExcept();
      if (rexDt != null) {
        Iterator it = rexDt.iterator();
        while (it.hasNext()) {
          Date rdt = (Date)it.next();
          dl.clearParameters();
          dl.setSql("activity.addrecurrenceexception");
          dl.setInt(1, recurId);
          dl.setDate(2, rdt);
          dl.executeUpdate();
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.addRecurrence: ", e);
    }
  }
  private void addAction(int actId, Collection actnCol, CVDal dl)
  {
    try {
      if (actnCol == null) {
        return;
      }
      Iterator it = actnCol.iterator();
      while (it.hasNext()) {
        ActivityActionVO aav = (ActivityActionVO)it.next();
        dl.clearParameters();
        dl.setSql("activity.addaction");
        dl.setString(1, aav.getActionType());
        dl.setString(2, aav.getMessage());
        dl.setTimestamp(3, aav.getActionTime());
        dl.setInt(4, aav.getRepeat());
        dl.setInt(5, aav.getInterval());
        dl.executeUpdate();
        int actnId = dl.getAutoGeneratedKey();
        dl.clearParameters();
        dl.setSql("activity.addactivityaction");
        ArrayList rcpn = aav.getRecipient();
        // To add recipient if there are any
        // or just enter a record w/o recipient ID in actionactivity
        if (rcpn != null) {
          Iterator it1 = rcpn.iterator();
          while (it1.hasNext()) {
            dl.setInt(1, actId);
            dl.setInt(2, actnId);
            dl.setInt(3, ((Integer)it1.next()).intValue());
            dl.executeUpdate();
          }
        } else {
          dl.setInt(1, actId);
          dl.setInt(2, actnId);
          dl.setInt(3, 0);
          dl.executeUpdate();
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.addAction: ", e);
    }
  } // end addAction() method
  private void addResource(int actId, Collection resCol, CVDal dl)
  {
    try {
      if (resCol == null) {
        return;
      }
      Iterator it = resCol.iterator();
      while (it.hasNext()) {
        int rid = ((ResourceVO)it.next()).getResourceId();
        dl.clearParameters();
        dl.setSql("activity.addresource");
        dl.setInt(1, actId);
        dl.setInt(2, rid);
        dl.executeUpdate();
        int actnId = dl.getAutoGeneratedKey();
        dl.clearParameters();
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.addResource: ", e);
    }
  } // end addResource() method
  /**
   * This method deletes the existing Activity Links in the system
   * and then adds all of the links associated with the ActivityVO.
   * @param actId The ID of the Activity ID.
   * @param actvo The ActivityVO object with the new Activity Links.
   * @param dl The Database Connection.
   */
  private void addActivityLink(int actId, ActivityVO actvo, CVDal dl)
  {
    try {
      if (actvo != null) {
        int indId = actvo.getIndividualID();
        int entId = actvo.getEntityID();
        int relatedFieldID = actvo.getRelatedFieldID();
        Vector attVec = actvo.getAttachmentId();
        // first delete all then add new
        deleteActivityLink(actId, dl);
        if (indId > 0) {
          dl.clearParameters();
          dl.setSql("activity.addactivitylink");
          dl.setInt(1, actId);
          dl.setInt(2, ActivityVO.ACTIVITY_LINK_INDIVIDUAL);
          dl.setInt(3, indId);
          dl.executeUpdate();
          if (actvo.getStatus() == 2) {
            HashMap historyInfo = new HashMap();
            historyInfo.put("recordID", new Integer(indId));
            historyInfo.put("recordTypeID", new Integer(Constants.IndividualModuleID));
            historyInfo.put("operation", new Integer(5));
            historyInfo.put("individualID", new Integer(gbUserId));
            historyInfo.put("referenceActivityID", new Integer(actvo.getActivityID()));
            historyInfo.put("recordName", actvo.getTitle());
            // please document the hardcoded int values below if you know what they are
            CVUtility.addHistoryRecord(historyInfo, dataSource);
          }
        } //end of if statement (indId > 0)
        if (entId > 0) {
          dl.clearParameters();
          dl.setSql("activity.addactivitylink");
          dl.setInt(1, actId);
          dl.setInt(2, ActivityVO.ACTIVITY_LINK_ENTITY);
          dl.setInt(3, entId);
          dl.executeUpdate();
          if (actvo.getStatus() == 2) {
            // please document the hardcoded int values below if you know what they are
            HashMap historyInfo = new HashMap();
            historyInfo.put("recordID", new Integer(entId));
            historyInfo.put("recordTypeID", new Integer(Constants.EntityModuleID));
            historyInfo.put("operation", new Integer(5));
            historyInfo.put("individualID", new Integer(gbUserId));
            historyInfo.put("referenceActivityID", new Integer(actvo.getActivityID()));
            historyInfo.put("recordName", actvo.getTitle());
            CVUtility.addHistoryRecord(historyInfo, dataSource);
          }
        } //end of if statement (entId > 0)
        if (attVec != null) {
          Iterator it = attVec.iterator();
          dl.clearParameters();
          dl.setSql("activity.addactivitylink");
          while (it.hasNext()) {
            int fileId = ((Integer)it.next()).intValue();
            dl.setInt(1, actId);
            dl.setInt(2, ActivityVO.ACTIVITY_LINK_FILE);
            dl.setInt(3, fileId);
            dl.executeUpdate();
          }
        } //end of if statement (attVec != null)
        //Add the related field.
        if (relatedFieldID > 0) {
          dl.clearParameters();
          dl.setSql("activity.addactivitylink");
          dl.setInt(1, actId);
          dl.setInt(2, actvo.getRelatedTypeID());
          dl.setInt(3, relatedFieldID);
          dl.executeUpdate();
        } //end of if statement (relatedFieldID > 0)
      } //end of if statement (actvo == null)
      dl.clearParameters();
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.addActivityLink: ", e);
    }
  } // end addActivityLink() method
  private void getAction(int activityId, ActivityVO acv, CVDal dl)
  {
    try {
      dl.clearParameters();
      dl.setSql("activity.getaction");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col == null) {
        return;
      }
      Iterator it = col.iterator();
      boolean moveNext = true;
      int mainActionId = 0;
      int currActionId = 0;
      ActivityActionVO aav = null;
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        currActionId = ((Long)hm.get("ActionID")).intValue();
        if (mainActionId != currActionId) {
          mainActionId = currActionId;
          aav = new ActivityActionVO();
          aav.setActionId(mainActionId);
          aav.setActionType((String)hm.get("Type"));
          aav.setMessage((String)hm.get("Message"));
          aav.setActionTime((Timestamp)hm.get("ActionTime"));
          if (hm.get("Interval") != null) {
            aav.setInterval(((Integer)hm.get("Interval")).intValue());
          }
          if (hm.get("Repeat") != null) {
            aav.setRepeat(((Number)hm.get("Repeat")).intValue());
          }
          acv.setActivityAction(aav);
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getAction: ", e);
    }
  } // end getAction() method
  private RecurrenceVO getRecurrence(int activityId, CVDal dl)
  {
    RecurrenceVO rec = null;
    try {
      dl.clearParameters();
      dl.setSql("activity.getrecurrence");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      rec = null;
      if (col == null) {
        return null;
      }
      Iterator it = col.iterator();
      if (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        rec = new RecurrenceVO();
        rec.setRecurrenceId(((Long)hm.get("RecurrenceID")).intValue());
        rec.setTimePeriod((String)hm.get("TimePeriod"));
        rec.setStartDate((Date)hm.get("startdate"));
        if (hm.get("Every") != null) {
          rec.setEvery(((Integer)hm.get("Every")).intValue());
        }
        if (hm.get("Until") != null) {
          rec.setUntil((Date)hm.get("Until"));
        }
        if (hm.get("RecurrOn") != null) {
          rec.setOn(((Number)hm.get("RecurrOn")).intValue());
        }
        rec.fillRecurrenceHashMap();
      }
      dl.clearParameters();
      dl.setSql("activity.getrecurrexcept");
      dl.setInt(1, activityId);
      Collection reccol = dl.executeQuery();
      it = reccol.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        rec.setRecurrenceExcept((Date)hm.get("Exception"));
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getRecurrence: ", e);
    }
    return rec;
  } // end getRecurrence() method
  private void getBasicActivity(int activityId, ActivityVO act, CVDal dl)
  {
    try {
      dl.clearParameters();
      dl.setSql("activity.getbasicactivity");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col == null) {
        return;
      }
      Iterator it = col.iterator();
      if (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        act.setActivityID(activityId);
        act.setActivityType(((Integer)hm.get("Type")).intValue());
        act.setPriority(((Integer)hm.get("Priority")).intValue());
        act.setStatus(((Integer)hm.get("Status")).intValue());
        act.setTitle((String)hm.get("Title"));
        act.setActivityDetails((String)hm.get("Details"));
        act.setActivityDueDate(((Timestamp)hm.get("DueDate")));
        act.setActivityStartDate((Timestamp)hm.get("Start"));
        act.setActivityEndDate((Timestamp)hm.get("End"));
        act.setLocation((String)hm.get("Location"));
        act.setIsAllDay((String)hm.get("AllDay"));
        act.setVisibility((String)hm.get("visibility"));
        act.setAttachmentType((String)hm.get("AttachmentType"));
        act.setNotes((String)hm.get("Notes"));
        try {
          // for getting the administrative data and other lookup data
          if (hm.get("Owner") != null) {
            act.setOwner(((Integer)hm.get("Owner")).intValue());
          }
          if (hm.get("Creator") != null) {
            act.setCreatedBy(((Integer)hm.get("Creator")).intValue());
          }
          act.setCreatedOn(((Timestamp)hm.get("Created")));
          if (hm.get("ModifiedBy") != null) {
            act.setModifiedBy(((Integer)hm.get("ModifiedBy")).intValue());
          }
          act.setModifiedOn(((Timestamp)hm.get("Modified")));
          col = null;
          if (act.getActivityType() == 2) {
            dl.setSql("activity.getcall");
            dl.setInt(1, activityId);
            col = dl.executeQuery();
            if (col != null) {
              it = col.iterator();
              if (it.hasNext()) {
                hm = (HashMap)it.next();
                if (hm != null) // being over cautious
                {
                  act.setCallTypeId(((Long)hm.get("CallType")).intValue());
                }
              }
            }
          }
          InitialContext ic = CVUtility.getInitialContext();
          ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
          ContactHelperLocal remote = home.create();
          remote.setDataSource(this.dataSource);
          String ownerName = remote.getIndividualName(act.getOwner());
          act.setOwnerID(act.getOwner());
          act.setOwnerName(ownerName);
        } catch (Exception e) {
          logger.error("[Exception] ActivityHelperEJB.getBasicActivity: ", e);
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getBasicActivity: ", e);
    }
  } // end getBasicActivity() method
  private void getAttendee(int activityId, ActivityVO clv, CVDal dl)
  {
    try {
      dl.clearParameters();
      dl.setSql("activity.getattendee");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col == null) {
        return;
      }
      Iterator it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        AttendeeVO atv = new AttendeeVO();
        atv.setContactID(((Long)hm.get("IndividualID")).intValue());
        atv.setAttendeeType((String)hm.get("Type"));
        if (hm.get("Status") != null) {
          atv.setAttendeeStatus(((Long)hm.get("Status")).intValue());
        }
        atv.setFirstName((String)hm.get("FirstName"));
        atv.setMiddleName((String)hm.get("MiddleInitial"));
        atv.setLastName((String)hm.get("LastName"));
        clv.setAttendee(atv);
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getAttendee: ", e);
    }
    //return act;
  }
  private void getResource(int activityId, ActivityVO clv, CVDal dl)
  {
    try {
      dl.clearParameters();
      dl.setSql("activity.getresource");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col == null) {
        return;
      }
      Iterator it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        ResourceVO rv = new ResourceVO();
        rv.setResourceId(((Long)hm.get("ActivityResourceID")).intValue());
        rv.setResourceName((String)hm.get("Name"));
        rv.setResourceDetail((String)hm.get("Detail"));
        clv.setResource(rv);
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getResource: ", e);
    }
  } // end getResource() method
  /**
   * Fills in the ActivityLinks for an Activity. This method fills in the
   * following links:
   * <ul><li> Entity
   * <li> Individual
   * <li> Files
   * <li> Project or Opportunity</ul>
   * @param activityId The Activity ID to get the Activity Links for.
   * @param app The ActivityVO to fill in.
   * @param dl The connection to the database.
   */
  private void getActivityLink(int activityId, ActivityVO app, CVDal dl)
  {
    try {
      dl.clearParameters();
      dl.setSql("activity.getactivitylink");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        int recordType = 0;
        int linkId = 0;
        InitialContext ic = CVUtility.getInitialContext();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          if (hm.get("RecordTypeID") != null) {
            recordType = ((Long)hm.get("RecordTypeID")).intValue();
          }
          if (hm.get("RecordID") != null) {
            linkId = ((Number)hm.get("RecordID")).intValue();
            switch (recordType) {
              case ActivityVO.ACTIVITY_LINK_ENTITY : // entity
                try {
                  ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
                  ContactHelperLocal remote = home.create();
                  remote.setDataSource(this.dataSource);
                  String entityName = remote.getEntityName(linkId);
                  app.setEntityName(entityName);
                  app.setEntityID(linkId);
                } catch (Exception e) {
                  logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
                }
                break;
              case ActivityVO.ACTIVITY_LINK_INDIVIDUAL : // Individual
                try {
                  ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
                  ContactHelperLocal remote = home.create();
                  remote.setDataSource(this.dataSource);
                  String individualName = remote.getIndividualName(linkId);
                  app.setIndividualName(individualName);
                  Collection contacts = remote.getPrimaryMOCForContact(0, linkId, 2);
                  app.setIndividualPrimaryContact("");
                  if(contacts.size() > 0){
                  	MethodOfContactVO mc = (MethodOfContactVO) contacts.toArray()[0];
                  	app.setIndividualPrimaryContact(mc.getContent());                  	
                  }
                  app.setIndividualID(linkId);
                } catch (Exception e) {
                  logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
                }
                break;
              case ActivityVO.ACTIVITY_LINK_FILE : // File
                try {
                  CvFileLocalHome fh = (CvFileLocalHome)ic.lookup("local/CvFile");
                  CvFileLocal fr = fh.create();
                  fr.setDataSource(this.dataSource);
                  CvFileVO flvo = fr.getFileBasic(gbUserId, linkId);
                  app.setAttachmentVec(flvo);
                } catch (Exception e) {
                  logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
                }
                break;
              case ActivityVO.ACTIVITY_LINK_OPPORTUNITY : // Opportunity - from module table
                try {
                  OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
                  OpportunityLocal local = home.create();
                  local.setDataSource(this.dataSource);
                  String opportunityName = local.getOpportunityName(linkId);
                  app.setRelatedFieldID(linkId);
                  app.setRelatedFieldValue(opportunityName);
                  app.setRelatedTypeID(ActivityVO.ACTIVITY_LINK_OPPORTUNITY);
                  app.setRelatedTypeValue("Opportunity");
                } catch (Exception e) {
                  logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
                }
                break;
              case ActivityVO.ACTIVITY_LINK_PROJECT : // Projects - from module table
                try {
                  ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
                  ProjectLocal remote = (ProjectLocal)home.create();
                  remote.setDataSource(this.dataSource);
                  String projectTitle = remote.getProjectName(linkId);
                  app.setRelatedFieldID(linkId);
                  app.setRelatedFieldValue(projectTitle);
                  app.setRelatedTypeID(ActivityVO.ACTIVITY_LINK_PROJECT);
                  app.setRelatedTypeValue("Project");
                } catch (Exception e) {
                  logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
                }
                break;
            } //end of switch statement (recordType)
          } //end of if statement (hm.get("RecordID") != null)
        } //end of while loop (it.hasNext())
      }
      dl.clearParameters();
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
    }
  } // end getActivityLink() method
  private void updateResource(int actId, Collection res, CVDal dl)
  {
    this.deleteResourceRelate(actId, dl);
    this.addResource(actId, res, dl);
  }
  private void updateRecurrence(int actId, RecurrenceVO arv, CVDal dl)
  {
    this.deleteRecurExcept(actId, dl);
    this.deleteRecurrence(actId, dl);
    this.addRecurrence(actId, arv, dl);
  }
  private void updateBasicActivity(ActivityVO act, CVDal dl, int userId)
  {
    dl.clearParameters();
    dl.setSql("activity.updateactivity");
    dl.setInt(1, act.getActivityType());
    dl.setInt(2, act.getPriority());
    dl.setInt(3, act.getStatus());
    dl.setString(4, act.getTitle());
    dl.setTimestamp(5, act.getActivityDueDate());
    dl.setString(6, act.getActivityDetails());
    dl.setTimestamp(7, act.getActivityStartDate());
    dl.setTimestamp(8, act.getActivityEndDate());
    dl.setString(9, act.getLocation());
    dl.setString(10, act.getIsAllDay());
    if (act.getModifiedBy() <= 0) {
      act.setModifiedBy(userId);
    }
    if (act.getModifiedBy() <= 0) {
      act.setModifiedBy(userId);
    }
    dl.setInt(11, act.getModifiedBy());
    if (act.getStatus() != 2) // what is 2?? please document..
    {
      act.setCompletedDate(null);
    }
    dl.setRealTimestamp(12, act.getCompletedDate());
    dl.setString(13, act.getVisibility());
    dl.setString(14, act.getAttachmentType());
    dl.setString(15, act.getNotes());
    dl.setInt(16, act.getActivityID());
    dl.executeUpdate();
    dl.clearParameters();
    if (act.getActivityType() == 2) // 2 == Call
    {
      dl.setSql("activity.updatecall");
      dl.setInt(1, act.getCallTypeId());
      dl.setInt(2, act.getActivityID());
      dl.executeUpdate();
    }
  } // end updateBasicActivity() method
  private void updateAttendee(int actId, Collection attn, CVDal dl)
  {
    this.deleteAttendee(actId, dl);
    this.addAttendee(actId, attn, dl);
  }
  private void updateAction(int actId, Collection actcol, CVDal dl)
  {
    this.deleteAction(actId, dl);
    this.deleteActivityAction(actId, dl);
    this.addAction(actId, actcol, dl);
  }
  private void deleteAttendee(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleteattendee");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  private void deleteRecurExcept(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleterecurexcept");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  private void deleteRecurrence(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleterecurrence");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  private void deleteResourceRelate(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleteresourcerelate");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  private void deleteAction(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleteaction");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  private void deleteActivityAction(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleteactivityaction");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  private void deleteBasicActivity(int activityId, CVDal dl)
  {
    dl.clearParameters();
    dl.setSql("activity.deleteactivity");
    dl.setInt(1, activityId);
    dl.executeUpdate();
    dl.clearParameters();
    dl.setSql("activity.deletecall");
    dl.setInt(1, activityId);
    dl.executeUpdate();
  }
  /**
   * Deletes all of the ActivityLinks associated with this activity.
   * @param activityId The ActivityID of the activity links to clear
   * @param dl The Database Connection.
   */
  private void deleteActivityLink(int activityId, CVDal dl)
  {
    try {
      dl.clearParameters();
      dl.setSql("activity.deleteactivitylink");
      dl.setInt(1, activityId);
      dl.executeUpdate();
      dl.clearParameters();
      // please document the hardcoded ints below if you know what they are
      CVUtility.deleteHistoryRecord(activityId, 14, 5, dataSource);
      CVUtility.deleteHistoryRecord(activityId, 15, 5, dataSource);
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.deleteActivityLink: ", e);
    }
  } // end deleteActivityLink() method
  /**
   * Performs the "extra actions" of creating a new activity record.
   * I only know of one thing this does so far, it sends the invitiation
   * emails to the attendees, if necessary.
   */
  private boolean extraActions(ActivityVO actVO, Collection attendees, int userId)
  {
    if (attendees == null) {
      return false;
    }
    try {
      InitialContext ic = new InitialContext();
      if (this.isActionTypeEmail(actVO)) {
        ActivityVO basicActivityVO = new ActivityVO();
        CVDal cvdal = new CVDal(this.dataSource);
        try {
          // I'm getting the basic activity from the database, even though we already
          // have an ActivityVO. This is because the activityVO we have is one that
          // was populated from the form, and is missing much information, such as
          // the Owner name and properly formatted (Y2K compliant) Timestamps.
          this.getBasicActivity(actVO.getActivityID(), basicActivityVO, cvdal);
        } finally {
          cvdal.destroy();
          cvdal = null;
        }
        ArrayList toList = new ArrayList();
        ContactHelperLocalHome contactHome = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
        ContactHelperLocal contactRemote = contactHome.create();
        contactRemote.setDataSource(this.dataSource);
        String fromAddress = contactRemote.getPrimaryEmailAddress(userId, 2); // what is 2???
        if (fromAddress == null || fromAddress.equals("")) {
          return false;
        }
        Iterator attendeesIter = attendees.iterator();
        while (attendeesIter.hasNext()) {
          AttendeeVO attendeeVO = (AttendeeVO)attendeesIter.next();
          if (attendeeVO.getContactID() != actVO.getOwner()) {
            Collection mocColl = contactRemote.getPrimaryMOCForContact(userId, attendeeVO.getContactID(), 2); // what is 2???
            if (mocColl != null) {
              Iterator mocIter = mocColl.iterator();
              while (mocIter.hasNext()) {
                MethodOfContactVO methCont = (MethodOfContactVO)mocIter.next();
                if (methCont.getMocType() == 1) // hardcoded for email
                {
                  toList.add(methCont.getContent());
                }
              }
            } // end while (mocIter.hasNext())
          } // end if (attendeeVO.getContactID() != actVO.getOwner())
        } // end while (attendeesIter.hasNext())
        EmailSettingsLocalHome emailSettingsHome = (EmailSettingsLocalHome)ic.lookup("local/EmailSettings");
        EmailSettingsLocal emailSettingsRemote = (EmailSettingsLocal)emailSettingsHome.create();
        emailSettingsRemote.setDataSource(dataSource);
        // get the template definition from the database. Some of it is dynamically configured by
        // the Administrator, and some of it is dynamically replaced here
        EmailTemplateForm activityTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_ACTIVITES);
        // Subject
        String activityType = this.getActivityTypeName(actVO.getActivityType());
        String activityTitle = basicActivityVO.getTitle();
        String subject = activityType + " Invitation: " + activityTitle;
        // Body
        StringBuffer body = new StringBuffer("");
        String bodyTemplate = activityTemplateForm.getBody();
        if (bodyTemplate != null && bodyTemplate.length() > 0) {
          body.append("\n\n" + bodyTemplate + "\n\n");
        }
        body.append("Type: " + activityType + "\n");
        body.append("Title: " + activityTitle + "\n");
        String details = "";
        if (basicActivityVO.getActivityDetails() != null && !basicActivityVO.getActivityDetails().equals("")) {
          details = basicActivityVO.getActivityDetails();
        }
        body.append("Details: " + details + "\n\n");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Timestamp startDateTime = basicActivityVO.getActivityStartDate();
        if (startDateTime != null) {
          String startDate = dateFormatter.format(startDateTime);
          body.append("Start: " + startDate + "\n");
        }
        Timestamp endDateTime = basicActivityVO.getActivityEndDate();
        if (endDateTime != null) {
          String endDate = dateFormatter.format(endDateTime);
          body.append("End: " + endDate + "\n");
        }
        body.append("Creator: " + basicActivityVO.getOwnerName() + "\n\n");
        MailMessageVO mailMessageVO = new MailMessageVO();
        mailMessageVO.setFromAddress(fromAddress);
        mailMessageVO.setHeaders("X-CentraView-Activity-Invitation: true");
        mailMessageVO.setSubject(subject);
        mailMessageVO.setBody(body.toString());
        mailMessageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);
        mailMessageVO.setReceivedDate(new java.util.Date());
        if (toList != null && toList.size() != 0) {
          mailMessageVO.setToList(toList);
          boolean sendFlag = emailSettingsRemote.simpleMessage(userId, mailMessageVO);
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.extraActions: ", e);
    }
    return true;
  } // end extraActions(ActivityVO,Collection,int) method
  private boolean isActionTypeEmail(ActivityVO actVO)
  {
    boolean actionEmail = false;
    if (actVO.getActivityAction() != null) {
      Iterator iter = (actVO.getActivityAction()).iterator();
      while (iter.hasNext()) {
        ActivityActionVO acnvo = (ActivityActionVO)iter.next();
        if ((acnvo.getActionType()).equals(ActivityActionVO.AA_EMAIL)) {
          actionEmail = true;
          break;
        }
      }
    }
    return actionEmail;
  }
  private Collection filterAttendees(ActivityVO acvo)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    Vector newAttendeesFinal = new Vector();
    try {
      ActivityVO localacVO = new ActivityVO();
      ActivityVO clbvoNew = (ActivityVO)acvo;
      Vector newAttendees = (Vector)clbvoNew.getAttendee();
      ActivityVO clbvoOld = new ActivityVO();
      this.getAttendee(acvo.getActivityID(), clbvoOld, cvdal);
      this.getAction(acvo.getActivityID(), localacVO, cvdal);
      if ((newAttendees != null) && this.isActionTypeEmail(localacVO)) {
        Vector oldAttendees = (Vector)clbvoOld.getAttendee();
        for (int i = 0; i < newAttendees.size(); i++) {
          AttendeeVO atvnew = (AttendeeVO)newAttendees.elementAt(i);
          boolean existFlag = true;
          for (int j = 0; j < oldAttendees.size(); j++) {
            AttendeeVO atvold = (AttendeeVO)oldAttendees.elementAt(j);
            if (atvold.getContactID() == atvnew.getContactID()) {
              existFlag = false;
              break;
            }
          }
          if (existFlag) {
            newAttendeesFinal.addElement(atvnew);
          }
        }
      } else if (newAttendees != null) {
        newAttendeesFinal = newAttendees;
      }
      Iterator it2 = newAttendeesFinal.iterator();
      while (it2.hasNext()) {
        AttendeeVO atv = (AttendeeVO)it2.next();
      }
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return newAttendeesFinal;
  } // end filterAttendees() method
  public void updateStatus(int activityID, int attendeeID, String status)
  {
    CVDal cvdl = new CVDal(dataSource);
    try {
      cvdl.setSql("activity.updateattendeestatus");
      cvdl.setString(1, status);
      cvdl.setInt(2, attendeeID);
      cvdl.setInt(3, activityID);
      cvdl.executeUpdate();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  } // end updateStatus(int,int,String) method
  /**
   * Fills in the activityLinkInfo for an Activity. This method fills in the
   * following links:
   * <ul><li>Entity Name &amp; Entity ID</ul>
   * @param activityId The Activity ID to get the Activity Links for.
   * @return activityLinkInfo The activityLinkInfo its a Map for Entity Name &amp; Entity ID
   */
  public HashMap getActivityLink(int activityId)
  {
    HashMap activityLinkInfo = new HashMap();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("activity.getactivitylink");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        int recordType = 0;
        int linkId = 0;
        InitialContext ic = CVUtility.getInitialContext();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          if (hm.get("RecordTypeID") != null) {
            recordType = ((Long)hm.get("RecordTypeID")).intValue();
          }
          if (hm.get("RecordID") != null) {
            linkId = ((Long)hm.get("RecordID")).intValue();
            switch (recordType) {
              case ActivityVO.ACTIVITY_LINK_ENTITY : // entity
                try {
                  ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
                  ContactHelperLocal remote = home.create();
                  remote.setDataSource(this.dataSource);
                  String entityName = remote.getEntityName(linkId);
                  activityLinkInfo.put("EntityName", entityName);
                  activityLinkInfo.put("EntityID", String.valueOf(linkId));
                } catch (Exception e) {
                  System.out.println("[Exception][ActivityHelperEJB.getActivityLink] Exception Thrown: " + e);
                  e.printStackTrace();
                  //do nothing
                } //end of catch block (Exception)
                break;
            } //end of switch statement (recordType)
          } //end of if statement (hm.get("RecordID") != null)
        } //end of while loop (it.hasNext())
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getActivityLink: ", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return activityLinkInfo;
  } //end of HashMap getActivityLink(int activityId)
  /**
   * Fills in the activityAttendeeInfo for an Activity. This method is
   * a collection of individual ID those are called as and attendee.
   * @param activityId The Activity ID to get the Activity Links for.
   * @return An ArrayList containing Strings representing the IndividualIDs
   *         of the attendees of the activity.
   */
  public ArrayList getActivityAttendee(int activityId)
  {
    ArrayList activityAttendeeInfo = new ArrayList();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQuery("select IndividualID from attendee WHERE ActivityID = ?");
      dl.setInt(1, activityId);
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        int recordType = 0;
        int linkId = 0;
        InitialContext ic = CVUtility.getInitialContext();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          Number individualID = ((Number)hm.get("IndividualID"));
          activityAttendeeInfo.add(individualID.toString());
        } //end of while loop (it.hasNext())
      }
    } catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getActivityAttendee: ", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return activityAttendeeInfo;
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
   * Returns a <code>String</code> representing the type of
   * Activity for the given <code>activityTypeID</code>. Valid
   * returned values are "Appointment", "Meeting", "Call", "To Do",
   * "Next Action", "Forecast Sale", "Literature Request", "Task".
   * @param activityTypeID int representing an activity type.
   * @return String representing activity type name.
   */
  private String getActivityTypeName(int activityTypeID)
  {
    switch (activityTypeID) {
      case ActivityVO.AT_APPOINTMENT : // 1
        return "Appointment";
      case ActivityVO.AT_MEETING : // 5
        return "Meeting";
      case ActivityVO.AT_CALL : // 2
        return "Call";
      case ActivityVO.AT_NEXTACTION : // 7
        return "Next Action";
      case ActivityVO.AT_TODO : // 6
        return "To Do";
      case ActivityVO.AT_FORCASTED_SALES : // 3
        return "Forecast Sale";
      case ActivityVO.AT_LITRATURE_REQUEST : // 4
        return "Literature Request";
      case ActivityVO.AT_TASK : // 8
        return "Task";
      default :
        return "Activity";
    }
  } // end getActivityTypeName(int) method
  /** 
   * Returns a <code>Vector</code> its a collection of Individual's, 
   * who are in the attendee's List.
   * 
   * @param activityId int representing an activity ID.
   * @param cvdal its a open database connection.
   * @return Vector its a collection of Individual, who are in the attendee's List.
   */
  private Vector getAttendeeVec(int activityId, CVDal cvdal)
  {
    Vector attendeeVec = new Vector();
    try {
      cvdal.clearParameters();
      cvdal.setSql("activity.getattendee");
      cvdal.setInt(1, activityId);
      Collection col = cvdal.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          if (hm.get("IndividualID") != null) {
            attendeeVec.add(((Number)hm.get("IndividualID")).toString());
          }//end of if(hm.get("IndividualID") != null)
        }//end of while (it.hasNext())
      }//end of if (col != null)
    }//end of try block
    catch (Exception e) {
      logger.error("[Exception] ActivityHelperEJB.getAttendeeVec: ", e);
    }//end of catch Exception
    return attendeeVec;
  }//end of getAttendeeVec(int activityId, CVDal cvdal)
  /** 
   * Delete a individual from the attendee's List for an activity.
   * Returns void
   * 
   * @param activityId int representing an activity Identification.
   * @param userId its a open database connection.
   * @return void
   */
  public void deleteIndividualFromAttendee(int activityId, int userId)
  {
    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.clearParameters();
      cvdal.setSqlQuery("DELETE FROM attendee WHERE ActivityID=? AND IndividualID=?");
      cvdal.setInt(1, activityId);
      cvdal.setInt(2, userId);
      cvdal.executeUpdate();
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
  }//end of deleteIndividualFromAttendee(int activityId, int userId)
} // end class definition
