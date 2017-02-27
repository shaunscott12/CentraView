/*
 * $RCSfile: ActivityVO.java,v $    $Revision: 1.5 $  $Date: 2005/10/24 14:54:20 $ - $Author: mcallist $
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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.centraview.common.CVAudit;
import com.centraview.common.CVUtility;
import com.centraview.file.CvFileVO;

/**
 * This class is the Value Object for the Activity Object.
 * @author CentraView, LLC.
 */
public class ActivityVO extends CVAudit implements Serializable
{
  private static Logger logger = Logger.getLogger(ActivityVO.class);
  public static final int AP_HIGH = 1;
  public static final int AP_MEDIUM = 2;
  public static final int AP_LOW = 3;
  public static final int AT_APPOINTMENT = 1;
  public static final int AT_MEETING = 5;
  public static final int AT_CALL = 2;
  public static final int AT_NEXTACTION = 7;
  public static final int AT_TODO = 6;
  public static final int AT_FORCASTED_SALES = 3;
  public static final int AT_LITRATURE_REQUEST = 4;
  public static final int AT_TASK = 8;
  public static final int AS_NEW = 1;
  public static final int AS_COMPLETED = 2;
  public static final int AS_INPROGRESS = 3;
  public static final int AS_CANCLED = 4;
  public static final String AD_YES = "YES";
  public static final String AD_NO = "NO";
  public static final String AV_PRIVATE = "PRIVATE";
  public static final String AV_PUBLIC = "PUBLIC";
  public static final String ATM_FILE = "FILE";
  public static final String ATM_LINK = "LINK";
  public static final String ATM_NONE = "NONE";
  protected int activityID;
  protected String title;
  protected int entityID;
  protected int individualID;
  protected int ownerID;
  protected String entityName;
  protected String individualName;
  protected String individualPrimaryContact;
  protected String ownerName;
  protected int opportunityID;
  protected int projectID;
  protected Timestamp activityStartDate;
  protected Timestamp activityEndDate;
  private Timestamp dueDate;
  private String activityDetails;
  private int status;
  private String isAllDay = AD_NO;
  private Timestamp completedDate;
  protected int priority;
  private int activityType;
  private String location;
  private String notes = "";
  private String visibility; //private/public
  protected Collection action; // Collection of ActivityActionVO
  protected RecurrenceVO reccurance; //
  private Vector attachmentVec; // vecotr of ddnamevale containing the file id and file name
  private Vector attachmentId; // vecotr of Integer's containing the file id
  private String attachmentType = ATM_NONE;
  /** The ID of the type of the item related to this activity. */
  protected int relatedTypeID;
  /** The value of the type of the item related to this activity. */
  protected String relatedTypeValue;
  /** The ID of the item related to this activity. */
  protected int relatedFieldID;
  /** The value of the item related to this activity. */
  protected String relatedFieldValue;
  /** The int value for the Activity Link type of entity. */
  public final static int ACTIVITY_LINK_ENTITY = 1;
  /** The int value for the Activity Link type of entity. */
  public final static int ACTIVITY_LINK_INDIVIDUAL = 2;
  /** The int value for the Activity Link type of entity. */
  public final static int ACTIVITY_LINK_FILE = 34;
  /** The int value for the Activity Link type of entity. */
  public final static int ACTIVITY_LINK_PROJECT = 36;
  /** The int value for the Activity Link type of entity. */
  public final static int ACTIVITY_LINK_OPPORTUNITY = 30;
  /** The int value for the Activity Link type of entity. */
  public final static int ACTIVITY_LINK_TICKET = 39;
  private Collection attendee;
  private Collection resource;
  public void setAttendee(AttendeeVO attendeevo)
  {
    if (attendee == null) {
      attendee = new Vector();
    }
    attendee.add(attendeevo);
  }
  public Collection getAttendee()
  {
    return (attendee);
  }
  public void setResource(ResourceVO resourceVO)
  {
    if (this.resource == null) {
      this.resource = new Vector();
    }
    this.resource.add(resourceVO);
  }
  public Collection getResource()
  {
    return (this.resource);
  }
  private int callTypeId;
  private String callTypeName;
  public int getCallTypeId()
  {
    return this.callTypeId;
  }
  public void setCallTypeId(int callTypeId)
  {
    this.callTypeId = callTypeId;
  }
  public String getCallTypeName()
  {
    return this.callTypeName;
  }
  public void setCallTypeName(String callTypeName)
  {
    this.callTypeName = callTypeName;
  }
  /**
   * Basic ActivityVO Constructor.
   */
  public ActivityVO()
  {
  //not implemented
  } //end of ActivityVO Constructor
  /**
   * Sets the Priority for this Activity.
   *
   * @param priorityID The new priority for the Activity.
   */
  public void setPriority(int priorityID)
  {
    this.priority = priorityID;
  } //end of setPriority method
  /**
   * Returns the Priority for this Activity.
   *
   * @return The priority for this Activity.
   */
  public int getPriority()
  {
    return this.priority;
  } //end of getPriority method
  /**
   * Sets the ActivityID for this Activity.
   *
   * @param activityID The new ActivityID for the Activity.
   */
  public void setActivityID(int activityID)
  {
    this.activityID = activityID;
  } //end of setActivityID method
  /**
   * Returns the ActivityID for this Activity.
   *
   * @return The ActivityID for the Activity.
   */
  public int getActivityID()
  {
    return this.activityID;
  } //end of getActivityID method
  /**
   * Sets the Title for this Activity.
   *
   * @param title The new Title for the Activity.
   */
  public void setTitle(String title)
  {
    this.title = title;
  } //end of setTitle method
  /**
   * Returns the title for this Activity.
   *
   * @return The title for the Activity.
   */
  public String getTitle()
  {
    return this.title;
  } //end of getTitle method
  /**
   * Sets the OpportunityID for this Activity.
   *
   * @param opportunityID The new OpportunityID for the Activity.
   */
  public void setOpportunityID(int opportunityID)
  {
    this.opportunityID = opportunityID;
  } //end of setOpportunityID method
  /**
   * Returns the OpportunityID for this Activity.
   *
   * @return The OpportunityID for the Activity.
   */
  public int getOpportunityID()
  {
    return this.opportunityID;
  } //end of getOpportunityID method
  /**
   * Sets the ProjectID for this Activity.
   *
   * @param projectID The new ProjectID for the Activity.
   */
  public void setProjectID(int projectID)
  {
    this.projectID = projectID;
  } //end of setProjectID method
  /**
   * Returns the ProjectID for this Activity.
   *
   * @return The ProjectID for the Activity.
   */
  public int getProjectID()
  {
    return this.projectID;
  } //end of getProjectID method
  /**
   * Sets the RelatedTypeValue for this Activity.
   *
   * @param relatedTypeValue The new RelatedTypeValue for the Activity.
   */
  public void setRelatedTypeValue(String relatedTypeValue)
  {
    this.relatedTypeValue = relatedTypeValue;
  } //end of setRelatedTypeValue method
  /**
   * Returns the RelatedTypeValue for this Activity.
   *
   * @return The RelatedTypeValue for the Activity.
   */
  public String getRelatedTypeValue()
  {
    return this.relatedTypeValue;
  } //end of getRelatedTypeValue method
  /**
   * Sets the RelatedTypeID for this Activity.
   *
   * @param relatedTypeID The new RelatedTypeID for the Activity.
   */
  public void setRelatedTypeID(int relatedTypeID)
  {
    this.relatedTypeID = relatedTypeID;
  } //end of setProjectID method
  /**
   * Returns the RelatedTypeID for this Activity.
   *
   * @return The RelatedTypeID for the Activity.
   */
  public int getRelatedTypeID()
  {
    return this.relatedTypeID;
  } //end of getRelatedTypeID method
  /**
   * Sets the RelatedFieldValue for this Activity.
   *
   * @param relatedFieldValue The new RelatedFieldValue for the Activity.
   */
  public void setRelatedFieldValue(String relatedFieldValue)
  {
    this.relatedFieldValue = relatedFieldValue;
  } //end of setRelatedFieldValue method
  /**
   * Returns the RelatedFieldValue for this Activity.
   *
   * @return The RelatedFieldValue for the Activity.
   */
  public String getRelatedFieldValue()
  {
    return this.relatedFieldValue;
  } //end of getRelatedFieldValue method
  /**
   * Sets the RelatedFieldID for this Activity.
   *
   * @param relatedFieldID The new RelatedFieldID for the Activity.
   */
  public void setRelatedFieldID(int relatedFieldID)
  {
    this.relatedFieldID = relatedFieldID;
  } //end of setRelatedFieldID method
  /**
   * Returns the RelatedFieldID for this Activity.
   *
   * @return The RelatedFieldID for the Activity.
   */
  public int getRelatedFieldID()
  {
    return this.relatedFieldID;
  } //end of getRelatedFieldID method
  /**
   * Sets the Start Date for this Activity.
   *
   * @param actStartDate The new Start Date for the Activity.
   */
  public void setActivityStartDate(Timestamp actStartDate)
  {
    this.activityStartDate = actStartDate;
  } //end of setActivityStartDate method
  /**
   * Returns the Start Date for this Activity.
   *
   * @return The Start Date for the Activity.
   */
  public Timestamp getActivityStartDate()
  {
    return activityStartDate;
  } //end of getActivityStartDate method
  /**
   * Sets the End Date for this Activity.
   *
   * @param actEndDate The new End Date for the Activity.
   */
  public void setActivityEndDate(Timestamp actEndDate)
  {
    this.activityEndDate = actEndDate;
  } //end of setActivityEndDate method
  /**
   * Returns the Start Date for this Activity.
   *
   * @return The Start Date for the Activity.
   */
  public Timestamp getActivityEndDate()
  {
    return activityEndDate;
  } //end of getActivityEndDate method
  /**
   * Sets the Recurrence Object for this Activity.
   *
   * @param rvo The new Recurrence Object for the Activity.
   */
  public void setRecurrence(RecurrenceVO rvo)
  {
    this.reccurance = rvo;
  } //end of setRecurrence method
  /**
   * Returns the Recurrence Object for this Activity.
   *
   * @return The Recurrence Object for the Activity.
   */
  public RecurrenceVO getRecurrence()
  {
    return reccurance;
  } //end of getRecurrence method
  /**
   * Sets the Activity Action for this Activity.
   *
   * @param aav The new Activity Action for the Activity.
   */
  public void setActivityAction(ActivityActionVO aav)
  {
    if (action == null) {
      action = new Vector();
    } //end of if statement (action == null)
    action.add(aav);
  } //end of setActivityAction method
  /**
   * Returns the Collection of Activity Actions for this Activity.
   *
   * @return The Collection of Activity Actions for the Activity.
   */
  public Collection getActivityAction()
  {
    return action;
  } //end of getActivityAction method
  /**
   * Sets the Activity Type for this Activity.
   *
   * @param actType The new Activity Type for the Activity.
   */
  public void setActivityType(int actType)
  {
    this.activityType = actType;
  } //end of setActivityType method
  /**
   * Returns the Activity Type for this Activity.
   *
   * @return The Activity Type for the Activity.
   */
  public int getActivityType()
  {
    return this.activityType;
  } //end of getActivityType method
  /**
   * Sets the Activity Due Date for this Activity.
   *
   * @param dueDate The new Activity Due Date for the Activity.
   */
  public void setActivityDueDate(Timestamp dueDate)
  {
    this.dueDate = dueDate;
  } //end of setActivityDueDate method
  /**
   * Returns the Activity Due Date for this Activity.
   *
   * @return The Activity Due Date for the Activity.
   */
  public Timestamp getActivityDueDate()
  {
    return this.dueDate;
  } //end of getActivityDueDate method
  /**
   * Sets the Activity Details for this Activity.
   *
   * @param activityDetails The new Activity Details for the Activity.
   */
  public void setActivityDetails(String activityDetails)
  {
    this.activityDetails = activityDetails;
  } //end of setActivityDetails method
  /**
   * Returns the Activity Details for this Activity.
   *
   * @return The Activity Details for the Activity.
   */
  public String getActivityDetails()
  {
    return this.activityDetails;
  } //end of getActivityDetails method
  /**
   * Sets the Activity Status for this Activity.
   *
   * @param status The new Activity Status for the Activity.
   */
  public void setStatus(int status)
  {
    this.status = status;
  } //end of setStatus method
  /**
   * Returns the Activity Status for this Activity.
   *
   * @return The Activity Status for the Activity.
   */
  public int getStatus()
  {
    return this.status;
  } //end of getStatus method
  /**
   * Sets whether this Activity is an all day activity.
   *
   * @param isAllDay Whether this Activity is an all day activity.
   */
  public void setIsAllDay(String isAllDay)
  {
    this.isAllDay = isAllDay;
  } //end of setIsAllDay method
  /**
   * Returns whether this Activity is an all day activity.
   *
   * @return Whether this Activity is an all day activity.
   */
  public String getIsAllDay()
  {
    return this.isAllDay;
  } //end of getIsAllDay method
  /**
   * Sets the Location for this Activity.
   *
   * @param location The Location for this Activity.
   */
  public void setLocation(String location)
  {
    this.location = location;
  } //end of setLocation method
  /**
   * Returns the Location for this Activity.
   *
   * @return The Location for this Activity.
   */
  public String getLocation()
  {
    return this.location;
  } //end of getLocation method
  /**
   * Sets the Completed Date for this Activity.
   *
   * @param completedDate The Completed Date for this Activity.
   */
  public void setCompletedDate(Timestamp completedDate)
  {
    this.completedDate = completedDate;
  } //end of setCompletedDate method
  /**
   * Returns the Completed Date for this Activity.
   *
   * @return The Completed Date for this Activity.
   */
  public Timestamp getCompletedDate()
  {
    return this.completedDate;
  } //end of getCompletedDate method
  /**
   * Sets the IndividualID for this Activity.
   *
   * @param individualID The IndividualID for this Activity.
   */
  public void setIndividualID(int individualID)
  {
    this.individualID = individualID;
  } //end of setIndividualID method
  /**
   * Returns the IndividualID for this Activity.
   *
   * @return The IndividualID for this Activity.
   */
  public int getIndividualID()
  {
    return this.individualID;
  } //end of setIndividualID method
  /**
   * Sets the EntityID for this Activity.
   *
   * @param entityID The EntityID for this Activity.
   */
  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  } //end of setEntityID method
  /**
   * Returns the EntityID for this Activity.
   *
   * @return The EntityID for this Activity.
   */
  public int getEntityID()
  {
    return this.entityID;
  } //end of getEntityID method
  /**
   * Sets the individualName for this Activity.
   *
   * @param individualName The individualName for this Activity.
   */
  public void setIndividualName(String individualName)
  {
    this.individualName = individualName;
  } //end of setIndividualName method
  /**
   * Returns the individualName for this Activity.
   *
   * @return The individualName for this Activity.
   */
  public String getIndividualName()
  {
    return this.individualName;
  } //end of getIndividualName method
  /**
   * Sets the entityName for this Activity.
   *
   * @param entityName The entityName for this Activity.
   */
  public void setIndividualPrimaryContact(String individualPrimaryContact)
  {
    this.individualPrimaryContact = individualPrimaryContact;
  } //end of setIndividualName method
  /**
   * Returns the individualName for this Activity.
   *
   * @return The individualName for this Activity.
   */
  public String getIndividualPrimaryContact()
  {
    return this.individualPrimaryContact;
  } //end of getIndividualName method
  /**
   * Sets the entityName for this Activity.
   *
   * @param entityName The entityName for this Activity.
   */
  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  } //end of setEntityName method
  /**
   * Returns the entityName for this Activity.
   *
   * @return The entityName for this Activity.
   */
  public String getEntityName()
  {
    return this.entityName;
  } //end of getEntityName method
  /**
   * Sets the ownerID for this Activity.
   *
   * @param ownerID The ownerID for this Activity.
   */
  public void setOwnerID(int ownerID)
  {
    this.ownerID = ownerID;
  } //end of setOwnerID method
  /**
   * Returns the ownerID for this Activity.
   *
   * @return The ownerID for this Activity.
   */
  public int getOwnerID()
  {
    return this.ownerID;
  } //end of getOwnerID method
  /**
   * Sets the ownerName for this Activity.
   *
   * @param ownerName The ownerName for this Activity.
   */
  public void setOwnerName(String ownerName)
  {
    this.ownerName = ownerName;
  } //end of setOwnerName method
  /**
   * Returns the ownerName for this Activity.
   *
   * @return The ownerName for this Activity.
   */
  public String getOwnerName()
  {
    return this.ownerName;
  } //end of getOwnerName method
  /**
   * Sets the Visibility (Private/Public) for this Activity.
   *
   * @param visibility The Visibility for this Activity.
   *
   * @see com.centraview.activity.helper.ActivityVO#AV_PRIVATE
   * @see com.centraview.activity.helper.ActivityVO#AV_PUBLIC
   */
  public void setVisibility(String visibility)
  {
    if (!(visibility.equals(AV_PRIVATE) || visibility.equals(AV_PUBLIC))) {
      this.visibility = AV_PRIVATE;
    } //end of if statement (!(visibility.equals(AV_PRIVATE) || visibility.equals(AV_PUBLIC)))
    else {
      this.visibility = visibility;
    } //end of else statement (!(visibility.equals(AV_PRIVATE) || visibility.equals(AV_PUBLIC)))
  } //end of setVisibility method
  /**
   * Returns the Visibility for this Activity.
   *
   * @return The Visibility for this Activity.
   *
   * @see com.centraview.activity.helper.ActivityVO#AV_PRIVATE
   * @see com.centraview.activity.helper.ActivityVO#AV_PUBLIC
   */
  public String getVisibility()
  {
    return this.visibility;
  } //end of getVisibility method
  /**
   * Adds a FileVO to the list of attachments for this Activity.
   *
   * @param attachment The FileVO to be added the list
   *  of attachments for this Activity.
   */
  public void setAttachmentVec(CvFileVO attachment)
  {
    if (this.attachmentVec == null) {
      this.attachmentVec = new Vector();
    } //end of if statement (this.attachmentVec == null)
    this.attachmentVec.add(attachment);
  } //end of setAttachmentVec method
  /**
   * Returns the Collection of attachments for this Activity.
   *
   * @return The Collection of attachments for this Activity.
   */
  public Vector getAttachmentVec()
  {
    return this.attachmentVec;
  } //end of getAttachmentVec method
  /**
   * Adds an ID of a file attachment to the list of attachmentIDs for this Activity.
   *
   * @param attachment The ID of a file attachment to the
   *  list of attachmentIDs for this Activity.
   */
  public void setAttachmentId(Integer attachmentId)
  {
    if (this.attachmentId == null) {
      this.attachmentId = new Vector();
    } //end of if statement (this.attachmentId == null)
    this.attachmentId.add(attachmentId);
  } //end of setAttachmentId method
  /**
   * Returns the Collection of attachmentIDs for this Activity.
   *
   * @return The Collection of attachmentIDs for this Activity.
   */
  public Vector getAttachmentId()
  {
    return this.attachmentId;
  } //end of setAttachmentId method
  /**
   * Sets the Attachment Type for this Activity.
   *
   * @param attachmentType The Attachment Type for this Activity.
   *
   * @see com.centraview.activity.helper.ActivityVO#ATM_FILE
   * @see com.centraview.activity.helper.ActivityVO#ATM_LINK
   * @see com.centraview.activity.helper.ActivityVO#ATM_NONE
   */
  public void setAttachmentType(String attachmentType)
  {
    if (!(attachmentType.equals(ATM_FILE) || attachmentType.equals(ATM_LINK))) {
      this.attachmentType = ATM_NONE;
    } //end of if statement (!(attachmentType.equals(ATM_FILE)...
    else {
      this.attachmentType = attachmentType;
    } //end of else statement (!(attachmentType.equals(ATM_FILE)...
  } //end of setAttachmentType method
  /**
   * Returns the Attachment Type for this Activity.
   *
   * @return The Attachment Type for this Activity.
   *
   * @see com.centraview.activity.helper.ActivityVO#ATM_FILE
   * @see com.centraview.activity.helper.ActivityVO#ATM_LINK
   * @see com.centraview.activity.helper.ActivityVO#ATM_NONE
   */
  public String getAttachmentType()
  {
    return this.attachmentType;
  } //end of getAttachmentType method
  /**
   * Sets the Notes field for this Activity.
   *
   * @param attachmentType The Notes field for this Activity.
   */
  public void setNotes(String notes)
  {
    this.notes = notes;
  } //end of setNotes method
  /**
   * Returns the Notes field for this Activity.
   *
   * @return The Notes field for this Activity.
   */
  public String getNotes()
  {
    return (this.notes);
  } //end of getNotes method
  /**
   * This is a convinence method that applies the supplied
   * timezone to all the dates of this Activity. This includes:
   *
   * <ul>
   * <li>startdate
   * <li>enddate
   * <li>duedate
   * <li>recurrencestartdate
   * <li>recurrenceenddate
   * <li>alertdate
   * <li>completeddate
   * </ul>
   *
   * @param currentTimeZone The current timezone of the activity.
   * @param newTimeZone The new timezone of the activity.
   */
  public void changeTimeZoneOfAllDates(String currentTimeZone, String newTimeZone)
  {
    logger.debug("start ts before changing timezone: "+this.getActivityStartDate().toString());
    logger.debug("end ts before changing timezone: "+this.getActivityEndDate().toString());
    TimeZone currentTZ = TimeZone.getTimeZone(currentTimeZone);
    TimeZone targetTZ = TimeZone.getTimeZone(newTimeZone);
    if (this.getActivityStartDate() != null) {
      this.setActivityStartDate(CVUtility.convertTimeZone(this.getActivityStartDate(), currentTZ, targetTZ));
    } //end of if statement (this.getActivityStartDate() != null)
    if (this.getActivityEndDate() != null) {
      this.setActivityEndDate(CVUtility.convertTimeZone(this.getActivityEndDate(), currentTZ, targetTZ));
    } //end of if statement (this.getActivityEndDate() != null)
    if (this.getActivityDueDate() != null) {
      this.setActivityDueDate(CVUtility.convertTimeZone(this.getActivityDueDate(), currentTZ, targetTZ));
    } //end of if statement (this.getActivityDueDate() != null)
    if (this.getCompletedDate() != null) {
      this.setCompletedDate(CVUtility.convertTimeZone(this.getCompletedDate(), currentTZ, targetTZ));
    } //end of if statement (this.getCompletedDate() != null)
    if (this.reccurance != null) {
      if (this.reccurance.getStartDate() != null) {
        this.reccurance.setStartDate(CVUtility.convertTimeZone(this.reccurance.getStartDate(), currentTZ, targetTZ));
      } //end of if statement (this.reccurance.getStartDate() != null)
      if (this.reccurance.getUntil() != null) {
        this.reccurance.setUntil(CVUtility.convertTimeZone(this.reccurance.getUntil(), currentTZ, targetTZ));
      } //end of if statement (this.reccurance.getUntil() != null)
    } //end of if statement (this.reccurance != null)
    logger.debug("start ts after changing timezone: "+this.getActivityStartDate().toString());
    logger.debug("end ts after changing timezone: "+this.getActivityEndDate().toString());
  } //end of changeTimeZoneOfAllDates method
  /**
   * Returns a comma seperated representation of this Activity.
   *
   * @return A comma seperated representation of this Activity.
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("ActivityID:[" + this.getActivityID() + "], ");
    sb.append("Title:[" + this.getTitle() + "], ");
    sb.append("Details:[" + this.getActivityDetails() + "], ");
    sb.append("StartDate:[" + this.getActivityStartDate() + "], ");
    sb.append("EndDate:[" + this.getActivityEndDate() + "], ");
    sb.append("DueDate:[" + this.getActivityDueDate() + "], ");
    sb.append("Completed Date:[" + this.getCompletedDate() + "], ");
    sb.append("Notes:[" + this.getNotes() + "], ");
    sb.append("AllDay:[" + this.getIsAllDay() + "], ");
    sb.append("Location:[" + this.getLocation() + "], ");
    sb.append("AttachmentType:[" + this.getAttachmentType() + "], ");
    sb.append("Visibility:[" + this.getVisibility() + "], ");
    sb.append("RelatedFieldID:[" + this.getRelatedFieldID() + "], ");
    sb.append("RelatedFieldValue:[" + this.getRelatedFieldValue() + "], ");
    sb.append("RelatedTypeID:[" + this.getRelatedTypeID() + "], ");
    sb.append("RelatedTypeValue:[" + this.getRelatedTypeValue() + "]");
    sb.append("Owner Name = [" + this.getOwnerName() + "]");
    sb.append("Attendees = [" + this.getAttendee() + "]");
    return (sb.toString());
  } //end of toString method
  public ActivityVO getVO()
  {
    return this;
  }
} //end of ActivityVO class
