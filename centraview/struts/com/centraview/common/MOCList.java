/*
 * $RCSfile: MOCList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:13 $ - $Author: mking_cv $
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

import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;

public class MOCList extends DisplayList
{

	private String sortMember ;
	private String primaryMember ;
	private String primaryTable ;
	private String PrimaryMemberType;
	private String searchString  ;
	private int contactId;
	private int contactType;
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	protected static boolean dirtyFlag = false;
	private static Logger logger = Logger.getLogger(MOCList.class);


	// constructor
	public  MOCList( )
	{
		columnMap = new HashMap();
		columnMap.put( "Type" , new Integer(50) );
		columnMap.put( "Content" , new Integer(70) );
		columnMap.put( "Notes" , new Integer(120) ) ;
		columnMap.put( "SyncAs" , new Integer(120) ) ;
		this.setPrimaryMember("Type");
	}



	public boolean getDirtyFlag()
	{
		return 	dirtyFlag ;
	}

	public void setDirtyFlag( boolean value )
	{
		dirtyFlag = value ;
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
		ListID 	= value ;
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
		listType = value ;
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
	public void  deleteElement( int indvID, String key )throws CommunicationException,NamingException
	{
		ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
		try
		{
			//call to EJB server
			int elementID = Integer.parseInt(key);
			ContactFacade remote =(ContactFacade)cfh.create();
			remote.setDataSource(this.dataSource);
			remote.deleteMOC(elementID,this.contactId,this.contactType);
			// User-rights for DeleteMOC ?
		}
		catch(AuthorizationFailedException ae){
			logger.error("[Exception] MOCList.deleteElement( int indvID, String String key )", ae);
		}//end of catch block
		catch( CreateException e )
		{
			logger.error("[Exception] MOCList.deleteElement( int indvID, String String key )", e);
		}
		catch(RemoteException e )
		{
			logger.error("[Exception] MOCList.deleteElement( int indvID, String String key )", e);
		}
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
		ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
		try
		{
			//call to EJB server
			ContactFacade remote =(ContactFacade)contactFacadeHome.create();
			remote.setDataSource(this.dataSource);
			for (int i=0; i<recordID.length; i++)
			{
				if(recordID[i] != null && !recordID[i].equals("")){
					int elementID = Integer.parseInt(recordID[i]);
					try{
						remote.deleteMOC(elementID,this.contactId,this.contactType);
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
			logger.error("[Exception] MOCList.deleteElement( int indvID, String rowId[] ) ", e);
			throw new CommunicationException(e.getMessage());
		}//end of catch( CreateException e )
		this.setDirtyFlag(true);

		return resultDeleteLog;
	}// end of deleteElement

	public void addElement( String  ID , EntityListElement value )
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

	public void addMOCElement( AddressListElement value )
	{

	}

	// get methods
	public void getMOCElement( String value )
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


	public int getContactId()
	{
		return this.contactId;
	}

	public void setContactId(int contactId)
	{
		this.contactId = contactId;
	}


	public int getContactType()
	{
		return this.contactType;
	}

	public void setContactType(int contactType)
	{
		this.contactType = contactType;
	}

}