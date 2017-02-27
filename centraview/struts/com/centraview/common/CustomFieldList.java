/*
 * $RCSfile: CustomFieldList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:56 $ - $Author: mking_cv $
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
package com.centraview.common;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
 
/**
 * @author Parshruam Walunjkar
 */
public class CustomFieldList extends DisplayList
{
 
	private String sortMember ;
	private String primaryMember ;
	private String primaryTable ="customfield";
	private String PrimaryMemberType="customfieldID";
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	private int contactId;
	private int contactType;
	private String moduleName;
	protected  boolean dirtyFlag = false;
	private String recordType; // ie Entity ,Individual etc
	private static Logger logger = Logger.getLogger(AddressList.class);
	
	// constructor
	public  CustomFieldList( )
	{
		columnMap = new HashMap();
		columnMap.put( "Field" , new Integer(10) );
		columnMap.put( "Value" , new Integer(20) );

		columnMap.put("CustomFieldID",new Integer(15));
		columnMap.put("Name",new Integer(25));
		columnMap.put("Type",new Integer(25));
		columnMap.put("Module",new Integer(50));
		columnMap.put("Record",new Integer(50));
//		this.setPrimaryMember("Name");	
		this.setPrimaryMember("Field");	

		
	}

	public boolean getDirtyFlag()
	{
		return 	dirtyFlag ;
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
	public void  deleteElement( int indvID, String key ) throws CommunicationException,NamingException
	{
		int elementID = Integer.parseInt(key);
		CustomFieldHome customFieldHome =(CustomFieldHome)CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome","CustomField");//call to CustomFieldEJB
		try
		{
			CustomField remote=(CustomField)customFieldHome.create();
			remote.setDataSource(this.dataSource);
			remote.deleteCustomField(indvID,elementID);
		}
		catch(Exception e)
		{
			logger.error("[Exception] CustomFieldList.deleteElement( int indvID, String key ) ", e);
		}
	}

	/**
	  * We will process the rowId. Incase if we don't have the right to DELETE a record then it will raise the AuthorizationException.
	  * We will catch the Exception and Log the Description of the Exception.
	  *
	  * @param individualID  ID for the Individual who is try to delete the record.
	  * @param recordID[] A String array of the recordID which we are try to delete it from database.
	  * @return resultDeleteLog A Collection of the Error Message while deleting a particular record.
	  */
	public ArrayList deleteElement( int individualID, String recordID[] ) throws CommunicationException,NamingException,RemoteException
	{
		ArrayList resultDeleteLog = new ArrayList();
		CustomFieldHome customFieldHome=(CustomFieldHome)CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome","CustomField");
		try
		{
			//call to EJB server
			CustomField remote=(CustomField)customFieldHome.create();
			remote.setDataSource(this.dataSource);
			for (int i=0; i<recordID.length; i++)
			{
				if(recordID[i] != null && !recordID[i].equals("")){
					int elementID = Integer.parseInt(recordID[i]);
					remote.deleteCustomField(individualID,elementID);
				}//end of if(recordID[i] != null && !recordID[i].equals(""))
			}//end of for (int i=0; i<recordID.length; i++)				
		}//end of try block
		catch( CreateException e )
		{
			logger.error("[Exception] CustomFieldList.deleteElement( int indvID, String rowId[] ) ", e);
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

/*	public void addAddressElement( AddressListElement value )
	{

	}
*/
	// get methods
/*	public void getAddressElement( String value )
	{

	}
*/
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



	public String getRecordType()
	{
		return this.recordType;
	}

	public void setRecordType(String recordType)
	{
		this.recordType = recordType;
	}


	
	public String getModuleName()
	{
		return this.moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
}



