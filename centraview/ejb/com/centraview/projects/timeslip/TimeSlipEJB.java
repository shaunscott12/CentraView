/*
 * $RCSfile: TimeSlipEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:52 $ - $Author: mking_cv $
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

package com.centraview.projects.timeslip;

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
import com.centraview.projects.helper.TimeSlipDBVO;
import com.centraview.projects.helper.TimeSlipVO;

public class TimeSlipEJB implements SessionBean
{
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



	public int addTimeSlip(int userId, TimeSlipVO tsvo)
	{
		int timeSlipId = 0;
        CVDal dl = new CVDal(dataSource);
		try
		{
			if(!CVUtility.isModuleVisible("Time Slips",userId, this.dataSource))
				throw new AuthorizationFailedException("Time Slips - addTimeSlip");

			dl.setSql("projecttimeslip.addtimeslip");
			dl.setInt(1,tsvo.getProjectID());
	    	dl.setInt(2,tsvo.getTaskID());
			dl.setString(3,tsvo.getDescription());
			dl.setDate(4,tsvo.getDate());
			dl.setTime(5,tsvo.getStart());
			dl.setTime(6,tsvo.getEnd());
			dl.setFloat(7,tsvo.getBreakTime());
			dl.setFloat(8,userId);
			dl.setFloat(9,tsvo.getHours());
			dl.setInt(10, tsvo.getTicketID());
			dl.setInt(11, tsvo.getTimesheetID());
			dl.executeUpdate();

			timeSlipId = dl.getAutoGeneratedKey();

            InitialContext ic = CVUtility.getInitialContext();
            AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
            AuthorizationLocal authorizationLocal = authorizationHome.create();
            authorizationLocal.setDataSource(dataSource);
            authorizationLocal.saveCurrentDefaultPermission("Time Slips", timeSlipId, userId);
		}
		catch(Exception e)
		{
			System.out.println("[Exception][TimeSlipEJB.addTimeSlip] Exception Thrown: "+e);
			e.printStackTrace();
		} finally {
          dl.destroy();
          dl = null;
		}
		return timeSlipId;
	}

	public TimeSlipVO getTimeSlip(int timeSlipId,int userId)
	{

		TimeSlipVO tvo = new TimeSlipVO();
		int projectId = 0;
		int activityId = 0;
		try
		{
			/*if(!CVUtility.canPerformRecordOperation(userId,"Time Slips",timeSlipId,ModuleFieldRightMatrix.VIEW_RIGHT))
    			throw new AuthorizationFailedException("Task - getTask");
    		*/
			if(!CVUtility.isModuleVisible("Time Slips",userId, this.dataSource))
				throw new AuthorizationFailedException("Time Slips - addTimeSlip");

			CVDal cvdl = new CVDal(dataSource);

			cvdl.setSql( "projecttimeslip.gettimeSlip" );
			cvdl.setInt( 1 , timeSlipId );
		 	Collection col = cvdl.executeQuery();
			Iterator ite = col.iterator();
			if (ite.hasNext())
			{
				HashMap hm = (HashMap)ite.next();
				tvo.setTimeSlipID(((Long)hm.get("timeslipid")).intValue());
		    	if(hm.get("projectid")!=null)
					tvo.setProjectID(((Long)hm.get("projectid")).intValue());
				//tvo.setProjectTitle((String)hm.get("projecttitle"));
				//tvo.setTaskTitle((String)hm.get("title"));
				if(hm.get("activityid")!=null)
					tvo.setTaskID(((Integer)hm.get("activityid")).intValue());
				if(hm.get("ticketid")!=null){
					tvo.setTicketID(((Long)hm.get("ticketid")).intValue());
				}
				else{
					tvo.setTicketID(0);
				}
				tvo.setDescription((String)hm.get("description"));
				tvo.setDate((java.sql.Date)hm.get("date"));
				tvo.setStart((java.sql.Time)hm.get("start"));
				tvo.setEnd((java.sql.Time)hm.get("end"));
				tvo.setBreakTime(((Double)hm.get("breaktime")).floatValue());
				tvo.setHours(((Double)hm.get("hours")).floatValue());

			}
		 	cvdl.clearParameters();
			if (tvo.getProjectID() !=0 )
			{
				cvdl.setSql("projecttimeslip.getprojecttimeSlip");
				cvdl.setInt(1, tvo.getProjectID());
				col = cvdl.executeQuery();
				ite = col.iterator();
				if(ite.hasNext())
				{
					HashMap hm = (HashMap)ite.next();
					tvo.setProjectTitle((String)hm.get("projecttitle"));
				}
				cvdl.clearParameters();
			}

			if (tvo.getTaskID() !=0 )
			{
				cvdl.setSql("projecttimeslip.getactivitytimeSlip");
				cvdl.setInt(1, tvo.getTaskID());
				col = cvdl.executeQuery();
				ite = col.iterator();
				if(ite.hasNext())
				{
					HashMap hm = (HashMap)ite.next();
					tvo.setTaskTitle((String)hm.get("title"));
				}
				cvdl.clearParameters();
			}
			if (tvo.getTicketID() !=0 )
			{
				cvdl.setSqlQuery("select subject from ticket where ticketid ="+tvo.getTicketID());
				col = cvdl.executeQuery();
				ite = col.iterator();
				if(ite.hasNext())
				{
					HashMap hm = (HashMap)ite.next();
					tvo.setTicket((String)hm.get("subject"));
				}
				cvdl.clearParameters();
			}
			cvdl.destroy();

		}catch( Exception e )
		{
			e.printStackTrace();
		}

		return tvo;
	}

	public void updateTimeSlip(int userId, TimeSlipVO tsvo)
	{
		try
		{
      if (! CVUtility.isModuleVisible("Time Slips",userId, this.dataSource))
      {
        throw new AuthorizationFailedException("Time Slips - addTimeSlip");
      }

			CVDal dl = new CVDal(dataSource);
			dl.setSql("projecttimeslip.updatetimeslip");

			// begin :: adding for fieldlevel rights...

			TimeSlipDBVO tsdbvo = new TimeSlipDBVO();
			tsdbvo = getDBVO(tsvo.getTimeSlipID(),userId);
			tsvo = (TimeSlipVO)CVUtility.replaceVO(tsdbvo, tsvo, "Time Slips", userId, this.dataSource);

			// end :: adding for fieldlevel rights...

			dl.setInt(1,tsvo.getProjectID());
	    	dl.setInt(2,tsvo.getTaskID());
	    	dl.setInt(3,tsvo.getTicketID());
			dl.setString(4,tsvo.getDescription());
			dl.setDate(5,tsvo.getDate());
			dl.setTime(6,tsvo.getStart());
			dl.setTime(7,tsvo.getEnd());
			dl.setFloat(8,tsvo.getBreakTime());
			dl.setFloat(9,tsvo.getHours());
			dl.setInt(10,tsvo.getTimeSlipID());


			dl.executeUpdate();

		    dl.clearParameters();

			dl.destroy();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public TimeSlipDBVO getDBVO(int timeSlipId, int userId)
	{

		TimeSlipDBVO tdbvo = new TimeSlipDBVO();
		int projectId = 0;
		int activityId = 0;
		try
		{


			CVDal cvdl = new CVDal(dataSource);

			cvdl.setSql( "projecttimeslip.gettimeSlip" );
			cvdl.setInt( 1 , timeSlipId );
		 	Collection col = cvdl.executeQuery();
			Iterator ite = col.iterator();
			if (ite.hasNext())
			{
				HashMap hm = (HashMap)ite.next();
				tdbvo.setTimeSlipID(((Long)hm.get("timeslipid")).intValue());
		    	if(hm.get("projectid")!=null)
					tdbvo.setProjectID(((Long)hm.get("projectid")).intValue());
				//tvo.setProjectTitle((String)hm.get("projecttitle"));
				//tvo.setTaskTitle((String)hm.get("title"));
				if(hm.get("activityid")!=null)
					tdbvo.setTaskID(((Integer)hm.get("activityid")).intValue());
				if(hm.get("ticketid")!=null){
					tdbvo.setTicketID(((Long)hm.get("ticketid")).intValue());
				}
				else{
					tdbvo.setTicketID(0);
				}
				tdbvo.setDescription((String)hm.get("description"));
				tdbvo.setDate((java.sql.Date)hm.get("date"));
				tdbvo.setStart((java.sql.Time)hm.get("start"));
				tdbvo.setEnd((java.sql.Time)hm.get("end"));
				tdbvo.setBreakTime(((Double)hm.get("breaktime")).floatValue());
				tdbvo.setHours(((Double)hm.get("hours")).floatValue());

			}
		 	cvdl.clearParameters();
			if (tdbvo.getProjectID() !=0 )
			{
				cvdl.setSql("projecttimeslip.getprojecttimeSlip");
				cvdl.setInt(1, tdbvo.getProjectID());
				col = cvdl.executeQuery();
				ite = col.iterator();
				if(ite.hasNext())
				{
					HashMap hm = (HashMap)ite.next();
					tdbvo.setProjectTitle((String)hm.get("projecttitle"));
				}
				cvdl.clearParameters();
			}

			if (tdbvo.getTaskID() !=0 )
			{
				cvdl.setSql("projecttimeslip.getactivitytimeSlip");
				cvdl.setInt(1, tdbvo.getTaskID());
				col = cvdl.executeQuery();
				ite = col.iterator();
				if(ite.hasNext())
				{
					HashMap hm = (HashMap)ite.next();
					tdbvo.setTaskTitle((String)hm.get("title"));
				}
				cvdl.clearParameters();
			}
			if (tdbvo.getTicketID() !=0 )
			{
				cvdl.setSqlQuery("select subject from ticket where ticketid ="+tdbvo.getTicketID());
				col = cvdl.executeQuery();
				ite = col.iterator();
				if(ite.hasNext())
				{
					HashMap hm = (HashMap)ite.next();
					tdbvo.setTicket((String)hm.get("subject"));
				}
				cvdl.clearParameters();
			}
			cvdl.destroy();

		}catch( Exception e )
		{
			e.printStackTrace();
		}

		return tdbvo;
	}

	public void duplicateTimeSlip(int userId, int timeSlipId)
	{
		try
		{

			CVDal cvdl = new CVDal(dataSource);
			cvdl.setSql( "projecttimeslip.selecttimeSlip" );
			cvdl.setInt( 1 , timeSlipId );
		 	Collection col = cvdl.executeQuery();
			Iterator ite = col.iterator();
			if (ite.hasNext())
			{
				HashMap hm = (HashMap)ite.next();

				cvdl.setSql("projecttimeslip.addtimeslip");
				cvdl.setInt(1,((Long)hm.get("ProjectID")).intValue());
		    	cvdl.setInt(2,((Long)hm.get("ActivityID")).intValue());
				cvdl.setString(3,((String)hm.get("Description")));
				cvdl.setDate(4,(java.sql.Date)hm.get("Date"));
				cvdl.setTime(5,(java.sql.Time)hm.get("Start"));
				cvdl.setTime(6,(java.sql.Time)hm.get("End"));
				cvdl.setFloat(7,((Float)hm.get("BreakTime")).floatValue());
				//cvdl.setFloat(8,userId);
				cvdl.setFloat(9,((Float)hm.get("hours")).floatValue());
				cvdl.executeUpdate();

			}
		 	cvdl.clearParameters();
			cvdl.destroy();

		}catch( Exception e )
		{
			System.out.println( "deletetimeslip"+e );
		}
	}



	/**
	this method delete TimeSlip
	*/
	public int deleteTimeSlip( int timeSlipId )
	{
		try
		{
			CVDal cvdl = new CVDal(dataSource);
			cvdl.setSql( "projecttimeslip.deletetimeSlip" );
			cvdl.setInt( 1 , timeSlipId );
		 	cvdl.executeUpdate();
		 	cvdl.clearParameters();
			cvdl.destroy();

		}catch( Exception e )
		{
			System.out.println( "deletetimeslip"+e );
		}
		return 0;
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


