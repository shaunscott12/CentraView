/*
 * $RCSfile: EntityList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:00 $ - $Author: mking_cv $
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


/**
 * @author 
 */
public class EntityList extends DisplayList
{

	private int individualID;

	private String sortMember ;
	private String primaryMember ;
	private String primaryTable = "entity";
	private String PrimaryMemberType = "EntityID";
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	private int dbID;
	protected  boolean dirtyFlag = false;
	private static Logger logger = Logger.getLogger(EntityList.class);

	// constructor
	public  EntityList( )
	{
		columnMap = new HashMap();
		columnMap.put( "Name" , new Integer(140) );
		columnMap.put( "PrimaryContact" , new Integer(130) );
		columnMap.put( "AccountManager" , new Integer(130) );
		columnMap.put( "Phone" , new Integer(100) ) ;
		columnMap.put( "Email" , new Integer(100) ) ;
		columnMap.put( "Website" , new Integer(100) );
		columnMap.put( "Address" , new Integer(100)  );
		columnMap.put( "IndividualID" , new Integer(20) );

		columnMap.put( "Street1" , new Integer(100) ) ;
		columnMap.put( "Street2" , new Integer(100) ) ;
		columnMap.put( "State" , new Integer(100) );
		columnMap.put( "Zip" , new Integer(100)  );
		columnMap.put( "Country" , new Integer(100) );
		columnMap.put( "City" , new Integer(100) );

		this.setPrimaryMember("Name");
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

	public String getPrimaryTable()
	{
		return primaryTable ;
	}

	//abstract
	public String getSortMember()
	{
		return sortMember ;
	}

	/**
   * Deletes the selected element from the list.
   *
   * @param individualID The Individual ID of the user deleting the element.
   * @param key The key of the element to delete.
	 */
	public void  deleteElement(int individualID, String key)throws CommunicationException,NamingException
	{
		int elementID = Integer.parseInt(key);
    ListGenerator lg = ListGenerator.getListGenerator(dataSource);
    ContactFacadeHome contactFacadeHome = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
		try
		{
			ContactFacade remote =(ContactFacade) contactFacadeHome.create();
			remote.setDataSource(this.dataSource);
			remote.deleteEntity(elementID, individualID);
		} //end of try block
		catch(Exception e )
		{
			logger.error("[Exception] EntityList.deleteElement( int indvID, String key )", e);
		} //end of catch block (Exception)
    finally
    {
      this.setDirtyFlag(true);
      lg.makeListDirty("Entity");
    } //end of finally block
	} // end of deleteElement method

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
						remote.deleteEntity(elementID, individualID);
					}//end of try block
					catch(AuthorizationFailedException ae){
						String errorMessage = ae.getExceptionDescription();
						resultDeleteLog.add(errorMessage);
					}//end of catch block
					catch(RemoteException remoteException){
						logger.error("[Exception] IndividualList.deleteElement( int indvID, String rowId[] ) ", remoteException);
					}//end of catch block
				}//end of if(recordID[i] != null && !recordID[i].equals(""))
			}//end of for (int i=0; i<recordID.length; i++)
		}//end of try block
		catch( CreateException e )
		{
			logger.error("[Exception] EntityList.deleteElement( int indvID, String rowId[] ) ", e);
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

	public void addEntityElement( EntityListElement value )
	{

	}

	// get methods
	public void getEntityElement( String value )
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

	public int getDbID()
	{
		return this.dbID;
	}

	public void setDbID(int dbID)
	{
		this.dbID = dbID;
	}

}

