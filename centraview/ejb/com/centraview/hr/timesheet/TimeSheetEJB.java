/*
 * $RCSfile: TimeSheetEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:27 $ - $Author: mking_cv $
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
 * TimeSheet bean includes all business logic realted
 * timesheet.
 *
 * @date   : 13-10-03
 * @version: 1.0
 */

package com.centraview.hr.timesheet;



import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.PureDateMember;
import com.centraview.common.PureTimeMember;
import com.centraview.common.StringMember;
import com.centraview.common.TimeSlipList;
import com.centraview.common.TimeSlipListElement;
import com.centraview.hr.helper.TimeSheetVO;

public class TimeSheetEJB  implements SessionBean
{
	//Standard EJB methods
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	public void setSessionContext(SessionContext ctx)
	{
		this.ctx = ctx;
	}

	public void ejbActivate()   { }
	public void ejbPassivate()   { }
	public void ejbRemove()   { }
	public void ejbCreate()  { }


	/*
	*<P>
	*Adds a new timesheet slip.
	*</P>
	*/

	 public int addTimeSheet(int indvId , TimeSheetVO tsvo)throws AuthorizationFailedException
	 {
	 	if(!CVUtility.isModuleVisible("Time Sheets",indvId, this.dataSource))
	 		throw new AuthorizationFailedException("Time Sheets - addTimeSheet");

	 	CVDal dl = new CVDal(dataSource);
    int timeSheetId = 0;

		try
		{
			dl.setSql("hr.addtimesheet");
			dl.setString(1,tsvo.getDescription());
			dl.setInt(2,tsvo.getOwner());
			dl.setInt(3,tsvo.getCreator());
			dl.setInt(4,tsvo.getModifiedBy());
			dl.setDate(5,tsvo.getFromDate());
			dl.setDate(6,tsvo.getToDate());
			dl.setInt(7,tsvo.getStatus());
			dl.setString(8,tsvo.getNotes());
			dl.setInt(9,tsvo.getReportingToId());
			dl.executeUpdate();
			timeSheetId = dl.getAutoGeneratedKey();

			InitialContext ic = CVUtility.getInitialContext();
			AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
			AuthorizationLocal authorizationLocal = authorizationHome.create();
			authorizationLocal.setDataSource(dataSource);
			// TODO I don't know if acouting is right.
			authorizationLocal.saveCurrentDefaultPermission("Accounting", timeSheetId, indvId);
		}
		catch(Exception e)
		{
			System.out.println("[Exception][TimeSheetEJB.addTimeSheet] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return timeSheetId;
	 }


	 public void deleteTimeSheet(int individualID, int timesheetID) throws AuthorizationFailedException
	 {
	 	if(!CVUtility.isModuleVisible("Time Sheets",individualID, this.dataSource))
	 		throw new AuthorizationFailedException("Time Sheets - deleteTimeSheet");

	 	try
	 	{
			CVDal dl = new CVDal(dataSource);
			dl.setSql("hr.deletetimesheet");
			dl.setInt(1,timesheetID);
			dl.executeUpdate();

			String strQuery = "select timeslipid from timeslip where timesheetid = " + timesheetID;
			Collection col = null;
			dl.setSqlQuery(strQuery);
			col = dl.executeQuery();
			if (col != null)
			{
				Iterator it = col.iterator();
				while( it.hasNext() )
				{
					HashMap hm1 = ( HashMap  )it.next();
					Long iTimeSlipID = (Long)hm1.get("timeslipid");
					if (iTimeSlipID != null)
					{
						//String sName = sUserFirstName+" "+sUserLastName;
						int timeSlipId = iTimeSlipID.intValue();
						dl.setSql("hr.deletetimeSlip");
						dl.setInt(1,timeSlipId);
						dl.executeUpdate();
					}
				}
			}

	 	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	 public TimeSheetVO getTimeSheet(int timesheetID)
	 {
	 	TimeSheetVO tvo = new TimeSheetVO();
		try
		{
			CVDal cvdl = new CVDal(dataSource);

			cvdl.setSql( "hr.gettimeSheet" );
			cvdl.setInt( 1 , timesheetID );
		 	Collection col = cvdl.executeQuery();
			Iterator ite = col.iterator();
			//int creatorId = AobjUserObject.getIndividualID();
			if(ite.hasNext())
			{
				HashMap hm = (HashMap)ite.next();
				Long ltimesheetID = (Long)hm.get("TimeSheetID");
				tvo.setTimesheetID(ltimesheetID.intValue());
				tvo.setDescription((String)hm.get("Description"));
				Long iOwner = (Long)hm.get("Owner");
				int ownerId = iOwner.intValue();
				if(iOwner != null)
					tvo.setOwner(ownerId);
				Long lReportingTo = (Long)hm.get("ReportingTo");
				int iReportingTo = 0;
				if (lReportingTo != null)
				{
					 iReportingTo = lReportingTo.intValue();
					 tvo.setReportingToId(iReportingTo);
				}
				Long lModifiedBy = (Long)hm.get("ModifiedBy");
				int iModifiedBy = 0;
				if (lModifiedBy != null)
				{
					iModifiedBy = lModifiedBy.intValue();
					tvo.setModifiedBy(iModifiedBy);
				}
				Long lCreator = (Long)hm.get("Creator");
				int iCreator = lCreator.intValue();
				String strQuery = "";
				strQuery = "select firstname,lastname from individual where individualid = " + iReportingTo;
				cvdl.setSqlQuery(strQuery);
				Collection col1 = cvdl.executeQuery();
				if (col1 != null)
				{
					Iterator it = col1.iterator();
					while( it.hasNext() )
					{
						HashMap hm1 = ( HashMap  )it.next();
						String sUserFirstName = (String)hm1.get("firstname");
						String sUserLastName = (String)hm1.get("lastname");
						if (sUserFirstName != null && sUserLastName != null)
						{
							String sName = sUserFirstName+" "+sUserLastName;
							tvo.setReportingTo(sName);
						}
					}
				}

				strQuery = "select firstname,lastname from individual where individualid = " + iCreator;
				cvdl.setSqlQuery(strQuery);
				col1 = cvdl.executeQuery();
				if (col1 != null)
				{
					Iterator it = col1.iterator();
					while( it.hasNext() )
					{
						HashMap hm1 = ( HashMap  )it.next();
						String sUserFirstName = (String)hm1.get("firstname");
						String sUserLastName = (String)hm1.get("lastname");
						if (sUserFirstName != null && sUserLastName != null)
						{
							String sName = sUserFirstName+" "+sUserLastName;
							tvo.setCreatorName(sName);
						}
					}
				}


				strQuery = "select firstname,lastname from individual where individualid = " + ownerId;
				cvdl.setSqlQuery(strQuery);
				col1 = cvdl.executeQuery();
				if (col1 != null)
				{
					Iterator it = col1.iterator();
					while( it.hasNext() )
					{
						HashMap hm1 = ( HashMap  )it.next();
						String sUserFirstName = (String)hm1.get("firstname");
						String sUserLastName = (String)hm1.get("lastname");
						if (sUserFirstName != null && sUserLastName != null)
						{
							String sName = sUserFirstName+" "+sUserLastName;
							tvo.setOwnerName(sName);
						}
					}
				}
				strQuery = "select firstname,lastname from individual where individualid = " + iModifiedBy;
				cvdl.setSqlQuery(strQuery);
				col1 = cvdl.executeQuery();
				if (col1 != null)
				{
					Iterator it = col1.iterator();
					while( it.hasNext() )
					{
						HashMap hm1 = ( HashMap  )it.next();
						String sUserFirstName = (String)hm1.get("firstname");
						String sUserLastName = (String)hm1.get("lastname");
						if (sUserFirstName != null && sUserLastName != null)
						{
							String sName = sUserFirstName+" "+sUserLastName;
							tvo.setModifiedByName(sName);
						}
					}
				}
				tvo.setCreator(iOwner.intValue());
				/*
				Long iModifiedby  = (Long)hm.get("ModifiedBy");
				tvo.setModifiedBy(iModifiedby.intValue());
				*/
				Timestamp created = (Timestamp)hm.get("Created");
				tvo.setCreatedDate(created);
				java.sql.Timestamp timestampModified = (java.sql.Timestamp)hm.get("Modified");
				tvo.setModifiedDate(timestampModified);
				java.sql.Date date = (java.sql.Date)hm.get("Start");
				tvo.setFromDate(date);
				date = (java.sql.Date)hm.get("End");
				tvo.setToDate(date);
				Long iStatus = (Long)hm.get("Status");
				tvo.setStatus(iStatus.intValue());
				tvo.setNotes((String)hm.get("Notes"));
			}
		 	cvdl.clearParameters();
			cvdl.destroy();
			//get timeslip list
			getTimeSlipList(tvo,"TimeSlipID","D");


		}catch( Exception e )
		{
			e.printStackTrace();
		}

		return tvo;
	 }

	 public int getTimeSheetID()
	 {
	 	int timesheetId = 0;
		try
		{
			CVDal cvdl = new CVDal(dataSource);
			cvdl.setSql( "hr.gettimeSheetID" );
			Collection col = cvdl.executeQuery();
			Iterator ite = col.iterator();

			while (ite.hasNext())
			{
				HashMap hm = (HashMap)ite.next();
				timesheetId = ((Integer)hm.get("count(*)")).intValue();
			}
		}
		catch(Exception e)
		{
			System.out.println("\n\n Exception in getTimeSheetID:");
			e.printStackTrace();
		}
		return  timesheetId;
	 }

	 private void getTimeSlipList(TimeSheetVO tvo, String sortElement, String sortOrder)
	 {
		 try{
			float totalDuration = 0;
			String strQuery = "";
			if(sortOrder.equalsIgnoreCase("A"))
				strQuery = "select TimeSlipID, ProjectID, ActivityID, TicketID, Description, Date, Start, End, BreakTime from timeslip where timesheetid = " +tvo.getTimesheetID();// + " order by " +sortElement;
			else
				strQuery = "select TimeSlipID, ProjectID, ActivityID, TicketID, Description, Date, Start, End, BreakTime from timeslip where timesheetid = " +tvo.getTimesheetID();// + "order by " +sortElement+ "desc";

			CVDal cvdl = new CVDal(dataSource);
			cvdl.setSqlQuery(strQuery);
			Collection col;
			col = cvdl.executeQuery();
			Iterator it = col.iterator();
			int i=0 ;
			TimeSlipList timeSlipList = new TimeSlipList();
			while( it.hasNext() )
			{
				i++;
				HashMap hm = ( HashMap  )it.next();
				int timeSlipID = ((Long)hm.get("TimeSlipID")).intValue();

				StringMember  reference = null, description, projectName = null, taskName =null, createdBy=null;
				PureDateMember date=null;
				PureTimeMember startTime=null, endTime=null;
				IntMember creator, taskId;
				IntMember referencetype = new IntMember( "Reference" ,0 , 10 , "", 'T' , true , 10 );

				IntMember intmem = new IntMember( "ID" , timeSlipID , 10 , "URL", 'T' , true , 10 );
				//IntMember projectId = new IntMember( "ProjectID" , projectID , 10 , "URL", 'T' , true , 10 );

				Time tStartTime = (Time)hm.get("Start");
				Time tEndTime = (Time)hm.get("End");

				Calendar calendarStart = Calendar.getInstance();
				calendarStart.setTime(tStartTime);

				Calendar calendarEnd = Calendar.getInstance();
				calendarEnd.setTime(tEndTime);

				//int startHrsmm[] = CVUtility.convertTimeTo24HrsFormat(StartTime);
				int startHrs = calendarStart.get(Calendar.HOUR_OF_DAY);//startHrsmm[0];
				int startMins = calendarStart.get(Calendar.MINUTE);//startHrsmm[1];

				//int endHrsmm[] = CVUtility.convertTimeTo24HrsFormat(EndTime);
				int endHrs = calendarEnd.get(Calendar.HOUR_OF_DAY);//endHrsmm[0];
				int endMins = calendarEnd.get(Calendar.MINUTE);//endHrsmm[1];


				float Duration = 0;

				Double dBreakTime = (Double)hm.get("BreakTime");
				float BreakTime = dBreakTime.floatValue();
				float remMin = 0;

				if ( startMins != 0 && endMins != 0 && (startMins-endMins !=0))
				{
					if(endMins > startMins)
						remMin = 60/(endMins - startMins);
					else
						remMin = 60/(startMins - endMins);
				}


				Duration = (endHrs - startHrs) + remMin - BreakTime ;

				totalDuration += Duration;


				if( hm.get( "ActivityID" ) != null && hm.get( "ProjectID" ) !=null  && hm.get( "TicketID" ) !=null ){

					int activityId = ((Integer)hm.get("ActivityID")).intValue();
					int projectId = ((Long)hm.get("ProjectID")).intValue();
					int ticketId = ((Long)hm.get("TicketID")).intValue();

					if( projectId != 0){

						String title = " Project : ";
						strQuery = "select ProjectTitle from project where ProjectID = "+projectId;
						cvdl.setSqlQuery(strQuery);
						Collection colProject = null;
						colProject = cvdl.executeQuery();

						if (colProject != null)
						{
							Iterator projectIterator = colProject.iterator();
							while( projectIterator.hasNext() )
							{
								HashMap hm1 = ( HashMap  )projectIterator.next();
								title = title + (String) hm1.get( "ProjectTitle" );
							}
						}

						if( activityId != 0){
							strQuery = "select Title from activity where activityId = "+activityId;
							cvdl.setSqlQuery(strQuery);
							Collection colActivity = null;
							colActivity = cvdl.executeQuery();
							if (colActivity != null)
							{

								Iterator activityIterator = colActivity.iterator();
								while( activityIterator.hasNext() )
								{
									HashMap hm1 = ( HashMap  )activityIterator.next();
									title = title +"/"+ (String) hm1.get( "Title" );
								}
							}
						}

						reference  = new StringMember( "Reference", title ,10 , "", 'T' , false  );
					}




					if( ticketId != 0){

						strQuery = "select subject from ticket where ticketid = "+ticketId;
						cvdl.setSqlQuery(strQuery);
						Collection colTicket = null;
						colTicket = cvdl.executeQuery();
						if (colTicket != null)
						{
							Iterator itTicket = colTicket.iterator();
							while( itTicket.hasNext() )
							{
								HashMap hm1 = ( HashMap  )itTicket.next();
								reference  = new StringMember( "Reference", " Ticket : "+(String) hm1.get( "subject" )  ,10 , "", 'T' , false  );
							}
						}

					}
				}
				else{
					referencetype = new IntMember( "ReferenceType" ,4 , 10 , "", 'T' , true , 10 );
					reference  = new StringMember( "Reference", "" ,10 , "", 'T' , false  );
				}

				if( hm.get( "Description" ) !=null)
					description  = new StringMember( "Description", (String) hm.get( "Description" ) ,10 , "", 'T' , false  );
				else
					description  = new StringMember( "Description", "" ,10 , "", 'T' , false  );



				if( hm.get( "Date" ) !=null)
					date  = new PureDateMember( "Date" ,(java.util.Date)hm.get("Date"),10 , "URL" , 'T' , false ,100 ,"EST");
				else
					date  = new PureDateMember( "Date" ,null,10 , "URL" , 'T' , false ,100 ,"EST"  );

				if( hm.get( "Start" ) !=null)
					startTime  = new PureTimeMember( "StartTime" ,(java.util.Date)hm.get("Start"),10 , "URL" , 'T' , false ,100 ,"EST");
				else
					startTime  = new PureTimeMember( "StartTime" ,null,10 , "URL" , 'T' , false ,100 ,"EST");


				if( hm.get( "End" ) !=null)
					endTime  = new PureTimeMember( "EndTime" ,(java.util.Date)hm.get("End"),10 , "URL" , 'T' , false ,100 ,"EST");
				else
					endTime  = new PureTimeMember( "EndTime" ,null,10 , "URL" , 'T' , false ,100 ,"EST");


				TimeSlipListElement ele = new TimeSlipListElement(timeSlipID);
				ele.put("ID", intmem );
				ele.put("Description", description );
				ele.put("Reference", reference );
				ele.put("ReferenceType", referencetype );
				ele.put("Task" ,  taskName );
				if ( Duration > 1.0 )
				{
					StringMember duration = new StringMember( "Duration" , Float.toString(Duration)+ " hours"  , 10 , "URL", 'T' , false );
					ele.put("Duration" ,  duration );
				}
				else if ( Duration == 1.0 )
				{
					StringMember duration = new StringMember( "Duration" , Float.toString(Duration)+ " hour"  , 10 , "URL", 'T' , false );
					ele.put("Duration" ,  duration );
				}
				else if ( Duration > 0.0 && Duration < 1.0 )
				{
				   	StringMember duration = new StringMember( "Duration" , Float.toString(Duration*60)+ " minutes"  , 10 , "URL", 'T' , false );
					ele.put("Duration" ,  duration );
				}
				else
				{
				   	StringMember duration = new StringMember( "Duration" , ""  , 10 , "URL", 'T' , false );
					ele.put("Duration" ,  duration );
				}
				ele.put("Date" , date );
				ele.put("StartTime" , startTime);
				ele.put("EndTime" , endTime);

				StringBuffer sb = new StringBuffer("00000000000");
				sb.setLength(11);
				String str = (new Integer(i)).toString();
				sb.replace((sb.length()-str.length()),(sb.length()),str);
				String newOrd = sb.toString();
				timeSlipList.put(newOrd , ele);
				//timeSlipList.setTotalNoOfRecords( timeSlipList.size() );
				timeSlipList.setListType( "Timeslip" );
				//Set timeslip list in vo
				tvo.setTimeSlipList(timeSlipList);


				tvo.setTotalDuration(totalDuration);
				//set totalduration in vo
			}//End Of While
			cvdl.clearParameters();
			cvdl.destroy();
		}
		catch(Exception e)
		{
			System.out.println("\n\n Exception in getTimeSheetID:");
			e.printStackTrace();
		}
	 }


	public void deleteTimeSlip(int individualID, String timeSlipIds) throws AuthorizationFailedException
	{

		if(!CVUtility.isModuleVisible("Time Sheets",individualID, this.dataSource))
			throw new AuthorizationFailedException("Time Sheets - deleteTimeSlip");


		if (timeSlipIds != null)
		{
			int timeSlipIdsLen = timeSlipIds.length();
			if (timeSlipIds.endsWith(",")){
				timeSlipIds = timeSlipIds.substring(0,(timeSlipIdsLen-1));
			}
		}
		try
		{
			CVDal dl = new CVDal(dataSource);
			dl.setSql("hr.deletetimeSlip");
			dl.setString(1,timeSlipIds);
			dl.executeUpdate();
			dl.destroy();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	public int updateTimeSheet(int individualID, TimeSheetVO tsvo) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("Time Sheets", individualID, this.dataSource))
			throw new AuthorizationFailedException("Time Sheets - updateTimeSheet");

		int iTimeSheetId = 0;
		try
		{

			CVDal dl = new CVDal(dataSource);
			dl.setSql("hr.updatetimesheet");
			dl.setString(1,tsvo.getDescription());
			dl.setInt(2,tsvo.getOwner());
			//dl.setInt(3,tsvo.getCreator());
			dl.setInt(3,tsvo.getModifiedBy());
			//dl.setTimestamp(5,tsvo.getCreatedDate());
			//dl.setTimestamp(6,tsvo.getModifiedDate());
			dl.setDate(4,tsvo.getFromDate());
			dl.setDate(5,tsvo.getToDate());
			dl.setInt(6,tsvo.getStatus());
			dl.setString(7,tsvo.getNotes());
			dl.setInt(8,tsvo.getReportingToId());
			dl.setInt(9,tsvo.getTimesheetID());
			dl.executeUpdate();

		    // dl.clearParameters();
		    dl.destroy();
			iTimeSheetId = tsvo.getTimesheetID();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return iTimeSheetId;
	}


	public Collection getStatusInfo()
	{
		CVDal cvdl = new CVDal(dataSource);
		String strQuery = "select StatusID,Title from timestatus";
		cvdl.setSqlQuery(strQuery);
		Collection col;
		col = cvdl.executeQuery();

		return col;
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
