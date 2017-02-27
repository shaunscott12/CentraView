/*
 * $RCSfile: LiteratureEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:40 $ - $Author: mking_cv $
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


package com.centraview.marketing.literature;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.activity.helper.ActivityHelperLocal;
import com.centraview.activity.helper.ActivityHelperLocalHome;
import com.centraview.activity.helper.ActivityVO;
import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.marketing.LiteratureDetails;

/*
 *  This Ejb is Staleless SessionBean
 *	Used for Add , Edit ,Delete ,Get Literature details.
 *	Date 19 Aug 2003
 * 	@author  Sunita
 * 	@version 1.0
 */

public class LiteratureEJB implements SessionBean
{
	private static Logger logger = Logger.getLogger(LiteratureEJB.class);
	protected SessionContext context;
	protected Context ctx;
	private String dataSource = "MySqlDS";

	/**
	This method sets the context for this Bean
	*/
	public void setSessionContext(SessionContext context)
	{
		this.context = context;
	}

	public void ejbPassivate() {}
	public void ejbActivate() {}
	public void ejbRemove() {}
	public void ejbCreate() {}

	/*
	 *	Used to add new literature
	 *	@param mapLiterature HashMap
	 *	@return int
	 */
	public int addLiterature(HashMap mapLiterature)
	{
		int result=0;
		String strTitle = (String)mapLiterature.get(" Title ");
		String strDetail = (String)mapLiterature.get(" Detail ");
		int assignedtoid = Integer.parseInt(mapLiterature.get(" AssignedTo ").toString());
		//int literatureid = Integer.parseInt(mapLiterature.get(" LiteratureId ").toString());
		int entityid = Integer.parseInt(mapLiterature.get(" EntityId ").toString());
		int individualid = Integer.parseInt(mapLiterature.get(" IndividualId ").toString());
		int statusid = Integer.parseInt(mapLiterature.get(" StatusId ").toString());
		int deliveryid = Integer.parseInt(mapLiterature.get(" DeliveryId ").toString());
		Timestamp duebydate = (Timestamp)mapLiterature.get(" DueBy ");
		int creator = Integer.parseInt(mapLiterature.get(" Creator ").toString());
		int ownerid = Integer.parseInt(mapLiterature.get(" Owner ").toString());
		String strLiteratureId = (String)mapLiterature.get(" LiteratureIds ");

		CVDal cvdal = new CVDal(dataSource);
		try
		{

			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);

			ActivityVO actVo = new ActivityVO();
			actVo.setEntityID(entityid);
			actVo.setIndividualID(individualid);
			actVo.setActivityType(4);
			actVo.setOwner(ownerid);
			actVo.setTitle(strTitle);
			actVo.setActivityDetails(strDetail);
			actVo.setActivityDetails(strDetail);
			actVo.setActivityDueDate(duebydate);
			actVo.setStatus(statusid);
			actVo.setVisibility("PUBLIC");
			int activityid = remote.addActivity(actVo,ownerid);

			cvdal.setSqlQuery("INSERT INTO literaturerequest VALUES(?,?,?)");
			cvdal.setInt(1,activityid);
			cvdal.setInt(2,assignedtoid);
			cvdal.setInt(3,deliveryid);
			cvdal.executeUpdate();
			cvdal.clearParameters();

			if(strLiteratureId != null){
				StringTokenizer stid = new StringTokenizer(strLiteratureId,",");
				String strId = "";
				while(stid.hasMoreTokens())
				{
					strId = stid.nextToken();
					if (strId != null && !strId.equals("")){
						cvdal.setSqlQuery("INSERT INTO literaturerequestlink VALUES (?,?)");
						cvdal.setInt(1,activityid);
						cvdal.setInt(2,Integer.parseInt(strId));
						cvdal.executeUpdate();
						cvdal.clearParameters();
					}//end of strId != null && !strId.equals("")
				}//end of while(stid.hasMoreTokens())
			}//end of if(strLiteratureId != null)
            AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
            AuthorizationLocal authorizationLocal = authorizationHome.create();
            authorizationLocal.setDataSource(dataSource);
            authorizationLocal.saveCurrentDefaultPermission("LiteratureFulfilment", deliveryid, individualid);
		}
		catch(Exception e)
		{
			logger.error("[execute] Exception thrown.", e);
			result = 1;
		}
		finally
		{
			cvdal.destroy();
			cvdal = null;
		}
		return result;
	}

	/*
	 *	Used to getAllliterature
	 *	@return Vector
	 */
	public Vector getAllLiterature()
	{
		Vector result = new Vector();
		CVDal cvdal = new CVDal(dataSource);
		Collection colExecute = null;
		try
		{
			cvdal.setSql("marketing.getallliterature");
			colExecute = cvdal.executeQuery();
			cvdal.clearParameters();
			cvdal.destroy();
			if (colExecute != null)
			{
				Iterator it = colExecute.iterator();
				while (it.hasNext())
				{
					HashMap mapOutput = (HashMap)it.next();
					int literatureid = ((Number)mapOutput.get("literatureid")).intValue();
					String strTitle = (String)mapOutput.get("title");
					DDNameValue ddnamevalue = new DDNameValue(literatureid , strTitle);
					result.add(ddnamevalue);
				}
			}
		}
		catch(Exception e)
		{
			logger.error("[execute] Exception thrown.", e);
			return null;
		}
		return result;
	}

	/*
	 *	Used to getalldeliverymethods
	 *	@param Vector
	 */
	public Vector getAllDeliveryMethod()
	{
		Vector result = new Vector();
		CVDal cvdal = new CVDal(dataSource);
		Collection colExecute = null;
		try
		{
			cvdal.setSql("marketing.getalldeliverymethod");
			colExecute = cvdal.executeQuery();
			cvdal.clearParameters();
			cvdal.destroy();
			if (colExecute != null)
			{
				Iterator it = colExecute.iterator();
				while (it.hasNext())
				{
					HashMap mapOutput = (HashMap)it.next();
					int deliveryid = ((Number)mapOutput.get("deliveryid")).intValue();
					String name = (String)mapOutput.get("name");
					DDNameValue ddnamevalue = new DDNameValue(deliveryid , name);
					result.add(ddnamevalue);
				}
			}
		}
		catch(Exception e)
		{
			logger.error("[execute] Exception thrown.", e);
			return null;
		}
		return result;
	}

	/*
	 *	Used to getallactivitystatus
	 *	@param Vector
	 */
	public Vector getAllActivityStatus()
	{
		Vector result = new Vector();
		CVDal cvdal = new CVDal(dataSource);
		Collection colExecute = null;
		try
		{
			cvdal.setSql("marketing.getallactivitystatus");
			colExecute = cvdal.executeQuery();
			cvdal.clearParameters();
			cvdal.destroy();
			if (colExecute != null)
			{
				Iterator it = colExecute.iterator();
				while (it.hasNext())
				{
					HashMap mapOutput = (HashMap)it.next();
					int statusid = Integer.parseInt(mapOutput.get("statusid").toString());
					String name = (String)mapOutput.get("name");
					DDNameValue ddnamevalue = new DDNameValue(statusid , name);
					result.add(ddnamevalue);
				}
			}
		}
		catch(Exception e)
		{
			logger.error("[execute] Exception thrown.", e);
			return null;
		}
		return result;
	}

	/*
	 *	Used to getliteraturedetails
	 *	@param  activityid int
	 *	@return LiteratureDetails
	 */
	public LiteratureDetails getLiteratureDetails(int activityID,int individualID)
	{
		LiteratureDetails literatureDetails = new LiteratureDetails();
		CVDal cvdal = new CVDal(dataSource);
		Collection colDetails = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);
			ActivityVO actVo = new ActivityVO();
			actVo = remote.getActivity(activityID,individualID);

			literatureDetails.setDuebydate(actVo.getActivityDueDate());
			if(actVo.getEntityID() != 0){
				literatureDetails.setEntityid(actVo.getEntityID());
				literatureDetails.setEntityname(actVo.getEntityName());
			}
			if(actVo.getIndividualID() != 0){
				literatureDetails.setIndividualid(actVo.getIndividualID());
				literatureDetails.setIndividualname(actVo.getIndividualName());
			}
			if(actVo.getTitle() != null){
				literatureDetails.setDetail(actVo.getTitle());
			}
			if(actVo.getActivityDetails() != null){
				literatureDetails.setTitle(actVo.getActivityDetails());
			}

			literatureDetails.setStatusid(actVo.getStatus());

			String selectQuery = " SELECT lr.deliverymethod deliveryMethod,   " +
				" lr.requestedby requestedBy, concat(req.firstname, ' ' , req.lastname) requestedByName   " +
				" FROM literaturerequest lr " +
				" LEFT OUTER JOIN individual req ON (lr.requestedby = req.individualid) " +
				" WHERE lr.activityid = ?";
						
			cvdal.setSqlQuery(selectQuery);
			cvdal.setInt(1,activityID);
			colDetails = cvdal.executeQuery();
			
			if(colDetails != null && colDetails.size() != 0){
				Iterator it = colDetails.iterator();
				while (it.hasNext())
				{
					HashMap mapResult = (HashMap)it.next();
					literatureDetails.setAssignedtoid(Integer.parseInt(mapResult.get("requestedBy").toString()));
					literatureDetails.setAssignedtoname((String)mapResult.get("requestedByName"));
					literatureDetails.setDeliveryid(Integer.parseInt(mapResult.get("deliveryMethod").toString()));
				}//end of while (it.hasNext())
			}//end of if(colDetails != null && colDetails.size() != 0)

			colDetails = null;

			cvdal.setSqlQueryToNull();
			cvdal.clearParameters();
			
			selectQuery = " SELECT lrl.literatureid , l.title FROM literaturerequestlink lrl "+
			 			  " LEFT OUTER JOIN literature l ON (lrl.literatureid = l.literatureid) "+
						  " WHERE lrl.activityid = ? "; 
			cvdal.setSqlQuery(selectQuery);
			cvdal.setInt(1,activityID);
			colDetails = cvdal.executeQuery();
			String literatureId = "";
			String literatureName = "";

			if(colDetails != null && colDetails.size() != 0){
				Iterator it = colDetails.iterator();
				while (it.hasNext())
				{
					HashMap mapResult = (HashMap)it.next();
					literatureId += ((Number)mapResult.get("literatureid")).intValue()+",";
					literatureName  += (String)mapResult.get("title")+",";
				}//end of while (it.hasNext())
			}//end of if(colDetails != null && colDetails.size() != 0)

			literatureDetails.setLiteratureId(literatureId);
			literatureDetails.setLiteratureName(literatureName);

		}
		catch(Exception e)
		{
			logger.error("[execute] Exception thrown.", e);
			return null;
		}
		finally{
			cvdal.clearParameters();
			cvdal.destroy();
			cvdal = null;
		}
		return literatureDetails;
	}

	/*
	 *	Used to editliterature
	 *	@param  mapLiterature HashMap
	 *	@return int
	 */
	public int editLiterature(HashMap mapLiterature)
	{
		int result=0;
		int activityid = Integer.parseInt(mapLiterature.get(" Activityid ").toString());
		String strTitle = (String)mapLiterature.get(" Title ");
		String strDetail = (String)mapLiterature.get(" Detail ");
		int assignedtoid = Integer.parseInt(mapLiterature.get(" AssignedTo ").toString());
		int entityid = Integer.parseInt(mapLiterature.get(" EntityId ").toString());
		int individualid = Integer.parseInt(mapLiterature.get(" IndividualId ").toString());
		int statusid = Integer.parseInt(mapLiterature.get(" StatusId ").toString());
		int deliveryid = Integer.parseInt(mapLiterature.get(" DeliveryId ").toString());
		Timestamp duebydate = (Timestamp)mapLiterature.get(" DueBy ");
		int creator = Integer.parseInt(mapLiterature.get(" Creator ").toString());
		int ownerid = Integer.parseInt(mapLiterature.get(" Owner ").toString());
		String strLiteratureId = (String)mapLiterature.get(" LiteratureIds ");

		CVDal cvdal = new CVDal(dataSource);
		try
		{

			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);

			ActivityVO actVo = new ActivityVO();
			actVo.setActivityID(activityid);
			actVo.setEntityID(entityid);
			actVo.setIndividualID(individualid);
			actVo.setActivityType(4);
			actVo.setTitle(strTitle);
			actVo.setActivityDetails(strDetail);
			actVo.setActivityDueDate(duebydate);
			actVo.setStatus(statusid);

			remote.updateActivity(actVo,ownerid);

			cvdal.setSqlQuery("DELETE FROM literaturerequestlink WHERE ActivityID = ? ");
			cvdal.setInt(1 , activityid );
			cvdal.executeUpdate();
			cvdal.clearParameters();

			if(strLiteratureId != null){
				StringTokenizer stid = new StringTokenizer(strLiteratureId,",");
				String strId = "";
				while(stid.hasMoreTokens())
				{
					strId = stid.nextToken();
					if (strId != null && !strId.equals("")){
						cvdal.setSqlQuery("INSERT INTO literaturerequestlink VALUES (?,?)");
						cvdal.setInt(1,activityid);
						cvdal.setInt(2,Integer.parseInt(strId));
						cvdal.executeUpdate();
						cvdal.clearParameters();
					}//end of strId != null && !strId.equals("")
				}//end of while(stid.hasMoreTokens())
			}//end of if(strLiteratureId != null)
		}//end of try block
		catch(Exception e)
		{
			logger.error("[execute] Exception thrown.", e);
			result = 1;
		}//end of catch block
		finally{
			cvdal.destroy();
			cvdal = null;
		}//end of finally block
		return result;
	}

	/*
	 *	Used to deleteliterature
	 *	@param  activityid int
	 *	@return int
	 */
	public int deleteLiterature(int activityid,int individualID) throws AuthorizationFailedException
	{
 		int result=0;
		CVDal cvdal = new CVDal(dataSource);
		try {
			InitialContext ic = CVUtility.getInitialContext();
			ActivityHelperLocalHome home = (ActivityHelperLocalHome)ic.lookup("local/ActivityHelper");
			ActivityHelperLocal remote = (ActivityHelperLocal) home.create();
			remote.setDataSource(dataSource);
			remote.deleteActivity(activityid,individualID);
			
			cvdal.setSqlQuery("DELETE FROM literaturerequest WHERE ActivityID = ? ");
			cvdal.setInt(1 , activityid );
			cvdal.executeUpdate();
			cvdal.clearParameters();

			cvdal.setSqlQuery("DELETE FROM literaturerequestlink WHERE ActivityID = ? ");
			cvdal.setInt(1 , activityid );
			cvdal.executeUpdate();
			cvdal.clearParameters();			
		} catch (NamingException e) {
 			logger.error("[execute] Exception thrown.", e);
			result = 1;
		} catch (CreateException e) {
 			logger.error("[execute] Exception thrown.", e);
			result = 1;
		} finally{
			cvdal.destroy();
			cvdal = null;
		}
		return result;
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
