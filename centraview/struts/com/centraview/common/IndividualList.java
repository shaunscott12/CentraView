/*
 * $RCSfile: IndividualList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:03 $ - $Author: mking_cv $
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
import javax.ejb.RemoveException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.individual.IndividualVO;

/**
 *
 * @author 
 */
public class IndividualList extends DisplayList
{

	//The individualId of List owner - pravink
	private int individualID;

	private String sortMember ;
	private String primaryMember ;
	private String primaryTable = "individual";
	private String PrimaryMemberType = "IndividualID";
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	protected  boolean dirtyFlag = false;
	private int entityId;
	private static Logger logger = Logger.getLogger(AddressList.class);
	private int dbID;

	// constructor
	public  IndividualList()
	{
		columnMap = new HashMap();
		columnMap.put( "Name" , new Integer(120) );
		columnMap.put( "Title" , new Integer(50) );
		columnMap.put( "Entity" , new Integer(60) ) ;
		columnMap.put( "Phone" , new Integer(100) ) ;
		columnMap.put( "Fax" , new Integer(120) );
		columnMap.put( "Email" , new Integer(140)  );
		this.setPrimaryMember("Name");
	}


	public  boolean getDirtyFlag()
	{
//		return IndividualList.dirtyFlag;
		return 	this.dirtyFlag ;
	}

	public  void setDirtyFlag( boolean value )
	{
		this.dirtyFlag = value ;
//		IndividualList.dirtystatic(value);
	}

//added a static method by sameer to check the dirty mechanism

/*	protected static void dirtystatic(boolean dirty)
	{
		IndividualList.dirtyFlag = dirty;
	}
*/

	public int getStartAT()
	{
	  return  this.startAT ;
	}

	public int getEndAT()
	{
	  return  this.endAT ;
	}

	public int getBeginIndex()
	{
		return  this.beginIndex ;
	}

	public int getEndIndex()
	{
	   return  this.endIndex ;
	}


	public  void  setRecordsPerPage( int value )
	{
		this.recordsPerPage = value ;
	}
	public  void  setTotalNoOfRecords(int value )
	{
		this.totalNoOfRecords = value;
	}




	public void setListID( long value )
	{
		this.ListID 	= value ;
	}


	public HashMap getColumnMap()
	{
		return	this.columnMap;
	}


	//abstract
	public void setSortMember( String value )
	{
		this.sortMember = value ;
	}

	//abstract
	public void setListType( String value )
	{
		super.listType = value ;
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
		return this.totalNoOfRecords;
	}

	//abstract
	public String getPrimaryTable()
	{
		return primaryTable ;
	}

	//abstract
	public String getSortMember()
	{
		return this.sortMember ;
	}

	//abstract
	public void  deleteElement( int indvID, String key) throws CommunicationException,NamingException
	{
		int elementID = Integer.parseInt(key);
		ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome","ContactFacade");
		try
		{
			ContactFacade remote =(ContactFacade)contactFacadeHome.create();
			remote.setDataSource(this.dataSource);
			remote.deleteIndividual( elementID,indvID);
			ListGenerator lg = ListGenerator.getListGenerator(dataSource);
			lg.makeListDirty("Individual");
			lg.makeListDirty("Group");

		}
		catch(Exception e )
		{
			logger.error("[Exception] IndividualList.deleteElement( int indvID, String key )", e);
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
		this.individualUsers = new ArrayList();
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
						if (!remote.isIndividualAUser(elementID))
						{
							remote.deleteIndividual( elementID,individualID);
						}
						else
						{
							IndividualVO individualVO = remote.getIndividual(elementID);
							this.individualUsers.add(individualVO.getFirstName() + " "
								+ individualVO.getLastName());
						} //end of else statement (!remote.isIndividualAUser(individualIDMarkedForDeletion))
					}//end of try block
					catch(AuthorizationFailedException ae){
						String errorMessage = ae.getExceptionDescription();
						resultDeleteLog.add(errorMessage);
					}//end of catch block
					catch(RemoveException re){
						resultDeleteLog.add(re);
					}//end of catch block
					catch(RemoteException remoteException){
						logger.error("[Exception] IndividualList.deleteElement( int indvID, String rowId[] ) ", remoteException);
					}//end of catch block
				}//end of if(recordID[i] != null && !recordID[i].equals(""))
			}//end of for (int i=0; i<recordID.length; i++)
		}//end of try block
		catch( CreateException e )
		{
			logger.error("[Exception] IndividualList.deleteElement( int indvID, String rowId[] ) ", e);
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
		this.primaryMember = value ;
	}

	public void setPrimaryTable( String value )
	{
		this.primaryTable = value ;
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




	public int getEntityId()
	{
		return this.entityId;
	}

	public void setEntityId(int entityId)
	{
		this.entityId = entityId;
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

