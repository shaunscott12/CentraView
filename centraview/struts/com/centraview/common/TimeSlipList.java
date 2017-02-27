/*
 * $RCSfile: TimeSlipList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:18 $ - $Author: mking_cv $
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
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;

public class TimeSlipList extends DisplayList
{
	private String sortMember ;
	private String primaryMember ;

	private String primaryTable = "timeslip";
	private String PrimaryMemberType = "timeslip.TimeSlipID";
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	protected  boolean dirtyFlag = false;
	private static Logger logger = Logger.getLogger(TimeSlipList.class);

	// constructor
	public  TimeSlipList( )
	{
		columnMap = new HashMap();
		columnMap.put( "TimeSlipID" , new Integer(140) );
		columnMap.put( "Description" , new Integer(140) );
		columnMap.put( "Project" , new Integer(130) );
		columnMap.put( "ProjectID" , new Integer(100) ) ;
		columnMap.put( "Task" , new Integer(130) );
		columnMap.put( "TaskID" , new Integer(100) ) ;
		columnMap.put( "Duration" , new Integer(100) ) ;
		columnMap.put( "CreatedBy" , new Integer(100) ) ;
		columnMap.put( "IndividualID" , new Integer(100) ) ;
		columnMap.put( "Date" , new Integer(100) );
		columnMap.put( "StartTime" , new Integer(100) ) ;
		columnMap.put( "EndTime" , new Integer(100) ) ;
		this.setPrimaryMember("ID");
	}



	public boolean getDirtyFlag()
	{
		return 	this.dirtyFlag ;
	}

	public void setDirtyFlag( boolean value )
	{
		this.dirtyFlag = value ;
	}

	public int getStartAT()
	{
	  return  startAT ;
	}

	public int getEndAT()
	{
	  return  endAT ;
	}

	public int getBeginIndex()
	{
		return  beginIndex ;
	}

	public int getEndIndex()
	{
	   return  endIndex ;
	}

	public  void  setRecordsPerPage( int value )
	{
		recordsPerPage = value ;
	}
	public  void  setTotalNoOfRecords(int value )
	{
		totalNoOfRecords = value;
	}


	public void setListID( long value )
	{
		super.ListID = value ;
	}

	public HashMap getColumnMap()
	{
		return	columnMap;
	}

	//abstract
	public void setSortMember( String value )
	{
		sortMember = value ;
	}

	//abstract
	public void setListType( String value )
	{
		super.listType = value ;
	}


	public String getSearchString()
	{
		return 	searchString ;
	}

	public void setSearchString( String value )
	{
		 searchString = value ;
	}


	//abstract
	public String getListType()
	{
		return listType ;
	}

	//abstract
	public String getPrimaryMemberType()
	{
		return PrimaryMemberType ;
	}

	//
	public void setPrimaryMemberType( String value )
	{
		 PrimaryMemberType = value  ;
	}

	public int getTotalNoofRecords()
	{
		return totalNoofRecords;
	}



	//abstract
	public String getPrimaryTable()
	{
		return primaryTable ;
	}

	//abstract
	public String getSortMember()
	{
		return sortMember ;
	}

	//abstract
	public void  deleteElement( int indvID, String key ) throws CommunicationException,NamingException
	{
		//call to EJB server
		int elementID = Integer.parseInt(key);
		ProjectFacadeHome projectFacadeHome = (ProjectFacadeHome)CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome","ProjectFacade");
		try
		{
			ProjectFacade remote =(ProjectFacade)projectFacadeHome.create();
			remote.setDataSource(this.dataSource);
			remote.deleteTimeSlip( elementID );
			// TODO deleteTimeSlip user-rights.
			this.setDirtyFlag(true);
			ListGenerator lg = ListGenerator.getListGenerator(dataSource);
			lg.makeListDirty("TimeSlip");
		}
		catch(Exception e )
		{
			logger.error("[Exception] TimeSlipList.deleteElement( int indvID, String key ) ", e);			
		}
	}// end of deleteElement


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
		ProjectFacadeHome projectFacadeHome = (ProjectFacadeHome)CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome","ProjectFacade");
		try
		{
			//call to EJB server
			ProjectFacade remote =(ProjectFacade)projectFacadeHome.create();
			remote.setDataSource(this.dataSource);
			for (int i=0; i<recordID.length; i++)
			{
				if(recordID[i] != null && !recordID[i].equals("")){
					int elementID = Integer.parseInt(recordID[i]);
					try{
						remote.deleteTimeSlip( elementID );
						// TODO deleteTimeSlip user-rights.
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
			logger.error("[Exception] TimeSlipList.deleteElement( int indvID, String rowId[] ) ", e);
			throw new CommunicationException(e.getMessage());
		}//end of catch( CreateException e )
		this.setDirtyFlag(true);

		return resultDeleteLog;
	}// end of deleteElement( int indvID, String rowId[] ) throws CommunicationException,NamingException


	public void addElement( String  ID , TimeSlipListElement value )
	{
		put( ID , value );
	}

	//abstract
	public void duplicateElement()
	{
	}


	public void setPrimaryMember( String value )
	{
		primaryMember = value ;
	}

	public void setPrimaryTable( String value )
	{
		primaryTable = value ;
	}

	public void addTimeSlipElement( TimeSlipListElement value )
	{

	}

	// get methods
	public void getTimeSlipElement( String value )
	{

	}

	public void setStartAT( int startAT )
	{
		this.startAT = startAT;
	}

	public void setEndAT( int EndAt )
	{
		this.endAT = EndAt;
	}

	public void setBeginIndex( int beginIndex )
	{
		this.beginIndex = beginIndex ;
	}

	public void setEndIndex( int endIndex )
	{
		this.endIndex = endIndex;
	}


	public String getPrimaryMember()
	{
		 return primaryMember;
	}
}
