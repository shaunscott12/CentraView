/*
 * $RCSfile: EmployeeList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:00 $ - $Author: mking_cv $
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
package com.centraview.common ;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;

/**
 * @author Rohit Nair
 */
public class EmployeeList extends IndividualList
{
	private static Logger logger = Logger.getLogger(EmployeeList.class);
	
	public EmployeeList()
	{
		super();
	}

/*
*<P>Overriding this methos from the superclass
*<P>
*/
	public void  deleteElement( int indvID, String key ) throws CommunicationException,NamingException
	{
		int elementID = Integer.parseInt(key);
		HrFacadeHome hrFacadeHome = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome","HrFacade");
		try
		{
			HrFacade remote =(HrFacade)hrFacadeHome.create();
			remote.setDataSource(this.dataSource);

		 	remote.deleteEmployee(elementID);
			//TODO individualID should be passed to deleteView.  User rights is fubar here.

			this.setDirtyFlag(true);
			ListGenerator lg = ListGenerator.getListGenerator(dataSource);
			lg.makeListDirty("Employee");

		}
		catch(Exception e )
		{
			logger.error("[Exception] EmployeeList.deleteElement( int indvID, String key )", e);
		}
		//call to ejbserver
		this.setDirtyFlag(true);
	}
	/**
	  * We will process the rowId. Incase if we don't have the right to DELETE a record then it will raise the AuthorizationException.
	  * We will catch the Exception and Log the Description of the Exception.
	  *
	  * @param individualID  ID for the Individual who is try to delete the record.
	  * @param recordID[] A String array of the recordID which we are try to delete it from database.
	  * @return resultDeleteLog A Collection of the Error Message while deleting a particular record.
	  */
	public ArrayList deleteElement(int individualID, String recordID[]) throws CommunicationException,NamingException,RemoteException
	{
		ArrayList resultDeleteLog = new ArrayList();
		HrFacadeHome hrFacadeHome = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome","HrFacade");
		try
		{
			//call to EJB server
			HrFacade remote =(HrFacade)hrFacadeHome.create();
			remote.setDataSource(this.dataSource);
			for (int i=0; i<recordID.length; i++)
			{
				if(recordID[i] != null && !recordID[i].equals("")){
					int elementID = Integer.parseInt(recordID[i]);
					try{
						remote.deleteEmployee(elementID);
						// TODO individualID should be passed to deleteView.  User rights is fubar here.
					}//end of try block
					catch(AuthorizationFailedException ae){
						String errorMessage = ae.getExceptionDescription();
						resultDeleteLog.add(errorMessage);
					}//end of catch block
				}//end of if(recordID[i] != null && !recordID[i].equals(""))
			}//end of for (int i=0; i<recordID.length; i++)
		}//end of try block
		catch( CreateException e )
		{
			logger.error("[Exception] EmployeeList.deleteElement( int indvID, String rowId[] ) ", e);
			throw new CommunicationException(e.getMessage());
		}//end of catch( CreateException e )
		this.setDirtyFlag(true);

		return resultDeleteLog;
	}// end of deleteElement
		
}