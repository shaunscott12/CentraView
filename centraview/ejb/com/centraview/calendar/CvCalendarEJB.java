/*
 * $RCSfile: CvCalendarEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:52 $ - $Author: mking_cv $
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

 
package com.centraview.calendar;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.activity.helper.ActivityHelperLocal;
import com.centraview.activity.helper.ActivityHelperLocalHome;
import com.centraview.activity.helper.RecurrenceVO;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;

public class CvCalendarEJB implements SessionBean
{
 protected javax.ejb.SessionContext ctx;
 private String dataSource = "MySqlDS";
 protected Context environment;
 private static Logger logger = Logger.getLogger(CvCalendarEJB.class);
 /**
  * Creates a new CvCalendarEJB object.
  */
 public CvCalendarEJB ()
 {
 }

 public HashMap getAllCalendar(int userId, int individualId, GregorianCalendar stDate, GregorianCalendar endDate, String advSrchSQL)
 {
   Vector schAct = null;
   Vector unschAct = null;
   Vector scheduleAct = null;
   Collection srchCol = null;
   CVDal dl = new CVDal(dataSource);
   try
   {
     // This gets a list of search results based on a passed in SQL query.
     if ((advSrchSQL != null) && (advSrchSQL.length() > 0))
     {
       dl.setSqlQuery(advSrchSQL);
       srchCol = dl.executeQuery();
       dl.setSqlQueryToNull();
     }
     schAct = this.getScheduledActivity(userId, individualId, stDate, endDate, srchCol, dl);
     dl.setSqlQueryToNull();
     unschAct = this.getUnScheduledActivity(userId, individualId, stDate, endDate, srchCol, dl);
     dl.setSqlQueryToNull();
     scheduleAct = this.getScheduledActivityDaily(userId, individualId, stDate, endDate, srchCol, dl);
     dl.setSqlQueryToNull();
   } catch (Exception e) {
     logger.error("[Exception][CvCalendarEJB.getAllCalendar] Exception Thrown: " , e);
   } finally
   {
     dl.destroy();
     dl = null;
   }
   HashMap retHm = new HashMap();
   retHm.put("scheduleactivityvector", schAct);
   retHm.put("unscheduleactivityvector", unschAct);
   retHm.put("scheduleactivityvectordaily", scheduleAct);
   return (retHm);
 } // end getAllCalendar() method

 // returns a hashmap with key as the userid
 // and value as a collection of CalendarActivityObject
 public HashMap getAvailibility(int userId, int[] individualId, GregorianCalendar stDate, GregorianCalendar endDate)
 {
   // try to make getScheduledActivity take a int array
   // then it will be able to take advantage of prepared statement
   HashMap retHm = new HashMap();
   try
   {
     CVDal dl = new CVDal(dataSource);
     for (int i = 0; i < individualId.length; i++)
     {
       Vector schAct =
       getScheduledActivity(userId, individualId[i], stDate, endDate, null, dl);
       retHm.put(new Integer(individualId[i]), schAct);
     }
     dl.destroy();
     dl = null;
   }
   catch (Exception e)
   {
     logger.debug("[Exception][CvCalendarEJB.getAvailibility] Exception Thrown: ",e);
   }
   if (retHm.size() > 0)
   {
     return retHm;
   }
   else
   {
     return null;
   }
 }


 private Vector getScheduledActivity(int userId, int individualId, GregorianCalendar sd, GregorianCalendar ed, Collection searchCol, CVDal dl)
 {
   Vector schVec = null;
   try
   {
     dl.setSql("calendar.getallscheduledactivity");
     dl.setInt(1, individualId);
     dl.setInt(2, individualId);
     Collection col = dl.executeQuery();

     Iterator it = col.iterator();
     schVec = fillReturnVector(userId, individualId, it, sd, ed, searchCol);
   }catch (Exception e){
     logger.debug("[Exception][CvCalendarEJB.getScheduledActivity] Exception Thrown: ",e);
   }
   return(schVec);
 }   // end getScheduledActivity() method


 private Vector getUnScheduledActivity(int userID, int individualID, GregorianCalendar startDate, GregorianCalendar endDate, Collection searchCol, CVDal dl)
 {
   Vector schVec = null;
   
   try
   {
     dl.setSql("calendar.getallunscheduledactivity");
     dl.setInt(1, individualID);
     Collection col = dl.executeQuery();
     Iterator iter = col.iterator();
     schVec = fillReturnVector(userID, individualID, iter, startDate, endDate, searchCol);
   }catch (Exception e){
     logger.debug("[Exception][CvCalendarEJB.getUnScheduledActivity] Exception Thrown: ",e);
   }
   return schVec;
  }


 /*
 *
 * Its used to Collect the todo , TASK Should display as a Day long schedule Activity.
 */
 private Vector getScheduledActivityDaily (int userId, int individualId,
         GregorianCalendar sd, GregorianCalendar ed, Collection searchCol, CVDal dl)
 {
   Vector schVec = null;
   try
   {
     dl.setSql("calendar.getscheduledactivitydaily");
     dl.setInt(1, individualId);
     dl.setInt(2, individualId);
      dl.setInt(3, individualId);
      dl.setInt(4, individualId);
     Collection col = dl.executeQuery();

     Iterator it = col.iterator();
     schVec = fillReturnVector(userId, individualId, it, sd, ed, searchCol);
   }
   catch (Exception e)
   {
     logger.debug("[Exception][CvCalendarEJB.getUnScheduledActivity] Exception Thrown: ",e);
   }
   return schVec;
 }


 private Vector fillReturnVector(int userId, int individualId, Iterator iterator, GregorianCalendar passedStartDate, GregorianCalendar passedEndDate, Collection searchCol)
 {
   Vector retVec = new Vector();

   try
   {

   InitialContext ic = CVUtility.getInitialContext();
   ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
   ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
   remote.setDataSource(dataSource);

     int sequence = 0;
     boolean advSearchPresent;
     if (searchCol == null)
     {
       advSearchPresent = false;
     }else{
       advSearchPresent = true;
     }

     while (iterator.hasNext())
     {
       HashMap activityRecord = (HashMap)iterator.next();
       Integer activityId = new Integer(((Number)activityRecord.get("ActivityID")).intValue());

       if (((Number)activityRecord.get("Status")).intValue() == 2)
       {
         // TODO: HARDCODED HTML!!!
         activityRecord.put("Title", "<span class=completedActivity>"+ (String)activityRecord.get( "Title" ) + "</span>");
       }

       // In the getAllCalendar method we may have run a search and gotten results
       // Therefore as we iterate the list, we check the activity Id against the
       // passed in collection if(advSearchPresent) of course
       // and if the current activity doesn't match, then we just skip it.
       if (advSearchPresent)
       {
         Long searchActivityId = (Long)activityRecord.get("ActivityID");
         HashMap compareHashMap = new HashMap();
         compareHashMap.put("ActivityID", searchActivityId);
         if (!searchCol.contains(compareHashMap))
         {
           continue;
         }
       }

       // If we made it this far then we can get the activity detail and populate the object.
       GregorianCalendar activityRecordStart = new GregorianCalendar(TimeZone.getTimeZone("EST"));
       if (activityRecord.get("Start") != null)
       {
         Timestamp activityStartDate = (Timestamp)activityRecord.get("Start");
         activityRecordStart.setTimeInMillis(activityStartDate.getTime());
       }

       GregorianCalendar activityRecordEnd = new GregorianCalendar(TimeZone.getTimeZone("EST"));
       if (activityRecord.get("End") != null)
       {
         Timestamp activityEndDate = (Timestamp)activityRecord.get("End");
         activityRecordEnd.setTimeInMillis(activityEndDate.getTime());
       }
        
        int activityType = ((Number)activityRecord.get("Type")).intValue();
        // its a Special Case for the Forcast Sales. We will not have the end date for it..
        // we have to display as the whole day event, for that reason if we set the start as end date. 
        if(activityType == 3){
        	activityRecordEnd.set(GregorianCalendar.DAY_OF_MONTH,activityRecordStart.get(GregorianCalendar.DAY_OF_MONTH));
        	activityRecordEnd.set(GregorianCalendar.MONTH,activityRecordStart.get(GregorianCalendar.MONTH));
        	activityRecordEnd.set(GregorianCalendar.YEAR,activityRecordStart.get(GregorianCalendar.YEAR));
        	activityRecordEnd.set(GregorianCalendar.HOUR_OF_DAY,23);
        }
        
       // And what if they were null ... it means we have fubar data, and thats just the way it is.

       // if this is a recurring record, do some stuff.
       if ((activityRecord.get("startdate") != null))
       {
         RecurrenceVO recurrenceVO = new RecurrenceVO();
         recurrenceVO.setRecurrenceId(((Long)activityRecord.get("RecurrenceID")).intValue());
         recurrenceVO.setStartDate((Date)activityRecord.get("startdate"));
         recurrenceVO.setUntil((Date)activityRecord.get("Until"));
         recurrenceVO.setTimePeriod((String)activityRecord.get("TimePeriod"));
         recurrenceVO.setOn(((Number)activityRecord.get("RecurrOn")).intValue());
         recurrenceVO.setEvery(((Integer)activityRecord.get("Every")).intValue());
         recurrenceVO.fillRecurrenceHashMap();

         HashMap recurrenceRuleMap = recurrenceVO.getRecurrenceHM();
         if (recurrenceRuleMap != null && recurrenceRuleMap.size() > 0)
         {
           Date recurrStartDate = (Date)activityRecord.get("startdate");
           // For very long recurrences we only need to calculate out 18 months before of the
           // passedStartDate otherwise we spend all our time calculating recurring tasks.
           Calendar compareCalendar = (Calendar)passedStartDate.clone();
           compareCalendar.add(Calendar.MONTH, -18);
           Date compareDate = new Date(compareCalendar.getTimeInMillis());
           if (recurrStartDate.before(compareDate))
           {
             recurrStartDate = compareDate;
           }

           GregorianCalendar recurringStartDate = new GregorianCalendar(TimeZone.getTimeZone("EST"));
           if (recurrStartDate != null)
           {
             recurringStartDate.setTime(recurrStartDate);
             recurringStartDate.set(Calendar.HOUR, activityRecordStart.get(Calendar.HOUR));
             recurringStartDate.set(Calendar.MINUTE, activityRecordStart.get(Calendar.MINUTE));
             recurringStartDate.set(Calendar.AM_PM, activityRecordStart.get(Calendar.AM_PM));
           }

           Date recurrEndDate = (Date)activityRecord.get("Until");
           // if there is no end date (the recurrence is open ended)
           // then coerce it to the passed in end date, for calculation purposes
           // Also for very long recurrences we only need to calculate out 2 months ahead of the
           // passedEndDate otherwise we spend all our time calculating recurring tasks.
           compareCalendar = (Calendar)passedEndDate.clone();
           compareCalendar.add(Calendar.MONTH, 2);
           compareDate = new Date(compareCalendar.getTimeInMillis());
           if (recurrEndDate == null || recurrEndDate.after(compareDate))
           {
             recurrEndDate = compareDate;
           }

           // get an RFC 2445 compliant recurrance rule string.
           String rfcRuleString = recurrenceVO.getRecurrenceRule(recurrenceRuleMap);

           // This object can take a rule string, a start and end date
           // and supply a list of dates where the activity occurs.
           long start = System.currentTimeMillis();
           
           try{
             RecurranceRuleRfc rfcRecurranceRule = new RecurranceRuleRfc(rfcRuleString, recurrStartDate, recurrEndDate);
             List matchingDates = rfcRecurranceRule.getAllMatchingDates();
 
             // Iterate the list and add new activity objects onto the list.
             start = System.currentTimeMillis();
             for (int i = 0; i < matchingDates.size(); i++)
             {
               java.util.Date matchingDate = (java.util.Date)matchingDates.get(i);
               GregorianCalendar recurringInstanceStartDate = new GregorianCalendar(passedStartDate.getTimeZone());
               // This is the start Calendar for this instance of the recurrance
               recurringInstanceStartDate.setTime(matchingDate); // This will set the MONTH/DATE/YEAR
               // Get the Time of Day from the activity Record
               recurringInstanceStartDate.set(Calendar.HOUR, activityRecordStart.get(Calendar.HOUR));
               recurringInstanceStartDate.set(Calendar.MINUTE, activityRecordStart.get(Calendar.MINUTE));
               recurringInstanceStartDate.set(Calendar.AM_PM, activityRecordStart.get(Calendar.AM_PM));
               // This is the End Calendar for this instance of the recurrance
               GregorianCalendar recurringInstanceEndDate = new GregorianCalendar(passedEndDate.getTimeZone());
               // The only real way to set the instance end time
               // is to find out the duration of the activityRecord and set the
               // end time of this instance to be offset from the start time of
               // the same instance by the calculated duration.
               long activityDuration = activityRecordEnd.getTimeInMillis() - activityRecordStart.getTimeInMillis();
               recurringInstanceEndDate.setTimeInMillis(recurringInstanceStartDate.getTimeInMillis() + activityDuration);
               CalendarActivityObject recurringInstanceObject = new CalendarActivityObject(recurringInstanceStartDate, recurringInstanceEndDate, activityId, (String)activityRecord.get("Title"), (String)activityRecord.get("ActivityName"), userId, individualId, "",false,null,null);
               recurringInstanceObject.setActivityDetail((String)activityRecord.get("Details"));
 
               // set visibility
               recurringInstanceObject.setActivityVisibility((String)activityRecord.get("visibility"));
 
               // set owner
               recurringInstanceObject.setActivityOwnerId(((Number)activityRecord.get("Owner")).intValue());
 
               ArrayList activityAttendeeInfo = remote.getActivityAttendee(activityId.intValue());
               recurringInstanceObject.setActivityAttendee(activityAttendeeInfo);
 
               //Find the Entity
               HashMap activityLinkInfo = remote.getActivityLink(activityId.intValue());
               String entityName = (String)activityLinkInfo.get("EntityName");
               int entityID = -1;
               try
               {
                 Integer.parseInt((String)activityLinkInfo.get("EntityID"));
               }catch(NumberFormatException nfe){
                 // "Don't stand so, don't stand so, close to me..."
               }
 
               //Set the Entity
               recurringInstanceObject.setEntityName(entityName);
               recurringInstanceObject.setEntityID(entityID);
               retVec.add(recurringInstanceObject);
             } // end for (int ctr = 0; ctr < results.size(); ctr++)
           }catch (Exception e){
             logger.debug("[Exception][CvCalendarEJB.fillReturnVector] Exception Thrown: " , e);
           }             
         } // end if (mapRecurrence != null)
       }else if (activityRecordStart.before(passedEndDate) && activityRecordEnd.after(passedStartDate)){
         // else it is not a recurring type of activity.

         // We will collect the number of days in the StartDate and EndDate ArrayList
         // in between the Starting Time Activity and Ending Time of Activity.
         GregorianCalendar tempStartTime = new GregorianCalendar(TimeZone.getTimeZone("EST"));
         tempStartTime.setTimeInMillis(activityRecordStart.getTimeInMillis());

         GregorianCalendar tempEndTime = new GregorianCalendar(TimeZone.getTimeZone("EST"));

         ArrayList StartDate = new ArrayList();
         ArrayList EndDate = new ArrayList();

         StartDate.add(new Long(tempStartTime.getTimeInMillis()));
         while (tempStartTime.before(activityRecordEnd))
         {
           tempEndTime.setTimeInMillis(tempStartTime.getTimeInMillis());
           tempEndTime.set(GregorianCalendar.HOUR,11);
           tempEndTime.set(GregorianCalendar.MINUTE,59);
           tempEndTime.set(GregorianCalendar.SECOND,59);
           tempEndTime.set(GregorianCalendar.AM_PM,GregorianCalendar.PM);

           tempStartTime.add(GregorianCalendar.DAY_OF_MONTH,1);
           tempStartTime.set(GregorianCalendar.HOUR,0);
           tempStartTime.set(GregorianCalendar.MINUTE,0);
           tempStartTime.set(GregorianCalendar.SECOND,0);
           tempStartTime.set(GregorianCalendar.AM_PM,GregorianCalendar.AM);

           if (! tempStartTime.before(activityRecordEnd))
           {
             break;
             }else{
             EndDate.add(new Long(tempEndTime.getTimeInMillis()));
             StartDate.add(new Long(tempStartTime.getTimeInMillis()));
           }   // end of if(!tempStartTime.before(activityRecordEnd))

         }   // end of while (tempStartTime.before(activityRecordEnd))

         tempEndTime.setTimeInMillis(activityRecordEnd.getTimeInMillis());
         EndDate.add(new Long(tempEndTime.getTimeInMillis()));

         for(int i = 0; i<StartDate.size();i++)
         {
           long startTimeInMills = ((Long) StartDate.get(i)).longValue();
           long endTimeInMills = ((Long) EndDate.get(i)).longValue();

           GregorianCalendar ActivityStart = new GregorianCalendar(TimeZone.getTimeZone("EST"));
           ActivityStart.setTimeInMillis(startTimeInMills);

           GregorianCalendar ActivityEnd = new GregorianCalendar(TimeZone.getTimeZone("EST"));
           ActivityEnd.setTimeInMillis(endTimeInMills);

           CalendarActivityObject cvo = null;

           // Activity for only one day
           if (StartDate.size() == 1)
           {
             cvo = new CalendarActivityObject(ActivityStart, ActivityEnd, activityId, (String)activityRecord.get("Title"), (String)activityRecord.get("ActivityName"), userId, individualId, "",false,null,null);
           }else{
             // Activity Spaninng for multiple days. We will set the flag value
             // to True and pass the Actual starting and ending time of the Activity.
             cvo = new CalendarActivityObject(ActivityStart, ActivityEnd, activityId, (String)activityRecord.get("Title"), (String)activityRecord.get("ActivityName"), userId, individualId, "",true,activityRecordStart,activityRecordEnd);
           }   // end if (StartDate.size() == 1)

           cvo.setActivityDetail((String)activityRecord.get("Details"));
           cvo.setActivityVisibility((String)activityRecord.get("visibility"));
           cvo.setActivitySequence(sequence);

           // Find the Entity

           HashMap activityLinkInfo = remote.getActivityLink(activityId.intValue());
           String entityName = (String)activityLinkInfo.get("EntityName");
           int entityID = -1;
           try
           {
             entityID = Integer.parseInt((String)activityLinkInfo.get("EntityID"));
           }catch(NumberFormatException nfe){
             // "Don't stand so, don't stand so, close to me..."
           }

           // Set the Entity
           cvo.setEntityName(entityName);
           cvo.setEntityID(entityID);

           // Setting owner ID
           cvo.setActivityOwnerId(((Number)activityRecord.get("Owner")).intValue());

     ArrayList activityAttendeeInfo = remote.getActivityAttendee(activityId.intValue());
     cvo.setActivityAttendee(activityAttendeeInfo);

           sequence++;
           retVec.add(cvo);
         }  //end for (int i=0; i<StartDate.size(); i++)
       }   // end if ((activityRecord.get("startdate") != null))
     }   // end while (iterator.hasNext())
   }catch (Exception e){
     logger.debug("[Exception][CvCalendarEJB.fillReturnVector] Exception Thrown: " , e);
   }
   return retVec;
 }

 /**
  * Set the associated session context. The container calls this method after the instance
  * creation. The enterprise Bean instance should store the reference to the context
  * object in an instance variable. This method is called with no transaction context.
  */
 public void setSessionContext (SessionContext ctx)
 {
   this.ctx = ctx;
 }

 /**
  * Called by the container to create a session bean instance. Its parameters
  * typically contain the information the client uses to customize the bean
  * instance for its use. It requires a matching pair in the bean class and
  * its home interface.
  */
 public void ejbCreate ()
 {
 }

 /**
  * A container invokes this method before it ends the life of the session
  * object. This happens as a result of a client's invoking a remove
  * operation, or when a container decides to terminate the session object
  * after a timeout. This method is called with no transaction context.
  */
 public void ejbRemove ()
 {
 }

 /**
  * The activate method is called when the instance is activated from its
  * 'passive' state. The instance should acquire any resource that it has
  * released earlier in the ejbPassivate() method. This method is called with
  * no transaction context.
  */
 public void ejbActivate ()
 {
 }

 /**
  * The passivate method is called before the instance enters the 'passive'
  * state. The instance should release any resources that it can re-acquire
  * later in the ejbActivate() method. After the passivate method completes,
  * the instance must be in a state that allows the container to use the Java
  * Serialization protocol to externalize and store away the instance's
  * state. This method is called with no transaction context.
  */
 public void ejbPassivate ()
 {
 }

 /**
  * @author Kevin McAllister <kevin@centraview.com>
  * This simply sets the target datasource to be used for DB interaction
  * @param ds A string that contains the cannonical JNDI name of the datasource
  */
  public void setDataSource(String ds) {
   this.dataSource = ds;
  }

}
