/*
 * $RCSfile: AlertEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:51 $ - $Author: mking_cv $
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

package com.centraview.alert;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.CVDal;

/**
This class is a Stateless session Bean
which acts as a Interface for alert Module
*/

public class AlertEJB implements SessionBean
{
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	/**
	This method sets the context for this Bean
	*/
	public void setSessionContext(SessionContext ctx) throws RemoteException
	{
		this.ctx = ctx;
	}

	/**
	These are life cycle methods from EJB
	*/
	public void ejbActivate()  throws RemoteException { }
	public void ejbPassivate()  throws RemoteException { }
	public void ejbRemove()  throws RemoteException { }
	public void ejbCreate() throws CreateException ,RemoteException { }


	/**
	This method returns AlertList
	*/
	public Collection getAlertList(HashMap preference )
	{
		Integer userid;

		userid 			= ( Integer )preference.get("UserID");


		String strSQL 	= "select an.type alerttype, ay.name modulename, at.title title, at.activityid id, at.start starttime, an.message, an.actiontime, '' project  from activity at, action an, activityaction aa, activitytype ay , activitystatus  aas where aa.recipient = "+ userid +" and ay.name != 'Task' and at.activityid = aa.activityid and at.status =  aas.StatusID and aas.Name = 'Pending' and aa.actionid = an.actionid  and ay.typeid = at.type  and  an.actiontime between concat(CURRENT_DATE) AND concat(CURRENT_DATE,' ',CURRENT_TIME) ";
		strSQL 			= strSQL + " UNION select an.type alerttype, ay.name modulename, at.title title, at.activityid id, at.start starttime, an.message, an.actiontime, p.projecttitle project from activity at, action an, activityaction aa, activitytype ay, task t, project p, activitystatus  aas where aa.recipient = "+ userid +" and ay.name = 'Task' and at.activityid = aa.activityid and at.status =  aas.StatusID and aas.Name = 'Pending' and  aa.actionid = an.actionid and ay.typeid = at.type and p.projectid = t.projectid and t.activityid = at.activityid  and  an.actiontime between concat(CURRENT_DATE) AND concat(CURRENT_DATE,' ',CURRENT_TIME) ";
		CVDal cvdl 		= new CVDal(dataSource);
//		cvdal.setSql("alert.getalerts");
//		cvdal.setInt(1 , userid );
//		cvdal.setInt(2 , date );
		cvdl.setSqlQuery( strSQL );
		Collection v 	= cvdl.executeQuery();
		cvdl.clearParameters();
		cvdl.destroy();
		return v;
	}


	public int addAlert ( int userId , HashMap data )
		throws RemoteException,Exception
	{
		return 0;
	};


	/*
	 *	Used to deleteAlert
	 *	@param  alertid
	 *	@return int
	 */
	public int deleteAlert(int activityid, int userid) throws RemoteException,Exception
	{
//		System.out.println("In delete Alert--activityid~"+activityid+"~~~userid~"+userid);
		int result=1;

		CVDal cvdal = new CVDal(dataSource);
		try
		{
			cvdal.setSql("alert.deletealert");
			cvdal.setInt(1 , activityid);
			cvdal.setInt(2 , userid);
			cvdal.executeUpdate();
			cvdal.clearParameters();
			cvdal.destroy();
		}
		catch(Exception e)
		{
			System.out.println("[Exception][AlertEJB.deleteAlert] Exception Thrown: "+e);
			e.printStackTrace();
			result = -1;
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