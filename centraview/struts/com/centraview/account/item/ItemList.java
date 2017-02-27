/*
 * $RCSfile: ItemList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:19 $ - $Author: mking_cv $
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

package com.centraview.account.item;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;


/*
	This class is for storing the Item List
*/

public class ItemList extends DisplayList
{

	private String sortMember ;
	private String primaryMember ;
	private String primaryTable ="item";
	private String PrimaryMemberType="itemid";
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	protected static boolean dirtyFlag = false;
	private static Logger logger = Logger.getLogger(ItemList.class);
 
	/**
	 *
	 * Constructor
	 */
	public ItemList()
	{
	
		columnMap=new HashMap();
		columnMap.put("ItemID",new Integer(15));
		columnMap.put("SKU",new Integer(25));
		columnMap.put("Name",new Integer(50));
//		columnMap.put("Category",new Integer(50));
		columnMap.put("Type",new Integer(50));
		columnMap.put("Price",new Integer(50));
		columnMap.put("OnHand",new Integer(50));
		columnMap.put("TaxClass",new Integer(50));
		columnMap.put("Cost",new Integer(50));
		
		//Added by Shilpa
		
		columnMap.put("VendorID",new Integer(50));
		columnMap.put("Vendor",new Integer(50));
		columnMap.put("ManufacturerID",new Integer(50));
		columnMap.put("Manufacturer",new Integer(50));
		
		//Added by Shilpa ends here
		
		
		this.setPrimaryMember("SKU");	
		
	}
	
	/**
	 * this method  is for retrieving whether list is dirty
	 *
	 * @return   boolean  
	 */
	public boolean getDirtyFlag()
	{
		return 	dirtyFlag ;
	}
	


	/**
	 * this method  sets the list dirty
	 *
	 * @param   value  
	 */
	public void setDirtyFlag( boolean value )
	{
		dirtyFlag = value ;
	}



	/**
	 * this method  returns the start parameter
	 *
	 * @return    int 
	 */
	public int getStartAT()
	{
	  return  startAT ;
	}


	/**
	 * this method  returns the end parameter
	 *
	 * @return     int
	 */
	public int getEndAT()
	{
	  return  endAT ;
	}



	/**
	 * this method  returns the BeginIndex parameter
	 *
	 * @return   int  
	 */
	public int getBeginIndex()
	{
		return  beginIndex ;
	}


	/**
	 * this method  returns the EndIndex parameter
	 *
	 * @return     int
	 */
	public int getEndIndex()
	{
	   return  endIndex ;
	}



	/**
	 * this method  is for RecordsPerPage
	 *
	 * @param   value  
	 */
	public  void  setRecordsPerPage( int value )
	{
		recordsPerPage = value ;
	}
	

	/**
	 * this method  is for setting TotalNoOfRecords
	 *
	 * @param   value  
	 */
	public  void  setTotalNoOfRecords(int value )
	{
		totalNoOfRecords = value;
	}

	/**
	 * this method  sets the ListID
	 *
	 * @param   value  
	 */
	public void setListID( long value )
	{
		super.ListID = value ;
	}


	/**
	 * return columnMap used in listing
	 *
	 * @return  HashMap   
	 */
	public HashMap getColumnMap()
	{
		return	columnMap;
	}


	/**
	 * this method  sets the SortMember
	 *
	 * @param   value  
	 */
	public void setSortMember( String value )
	{
		sortMember = value ;
	}



	/**
	 * this method  sets ListType
	 *
	 * @param   value  
	 */
	public void setListType( String value )
	{
		super.listType = value ;
	}



	/**
	 * this method  returns SearchString
	 *
	 * @return   String  
	 */
	public String getSearchString()
	{
		return 	searchString ;
	}



	/**
	 * this method  sets setSearchString
	 *
	 * @param   value  
	 */
	public void setSearchString( String value )
	{
		 this.searchString = value ;
	}



	/**
	 * this method  returns ListType
	 *
	 * @return  String   
	 */
	public String getListType()
	{
		return listType ;
	}



	/**
	 * this method  returns PrimaryMemberType
	 *
	 * @return   String  
	 */
	public String getPrimaryMemberType()
	{
		return PrimaryMemberType ;
	}



	/**
	 * this method  sets PrimaryMemberType
	 *
	 * @param   value  
	 */
	public void setPrimaryMemberType( String value )
	{
		 PrimaryMemberType = value  ;
	}



	/**
	 * this method  returns TotalNoofRecords
	 *
	 * @return int    
	 */
	public int getTotalNoofRecords()
	{
		return totalNoofRecords;
	}     



	/**
	 * this method  returns PrimaryTable
	 *
	 * @return  String   
	 */
	public String getPrimaryTable()
	{
		return this.primaryTable ;
	}


	/**
	 * this method  returns SortMember
	 *
	 * @return String    
	 */
	public String getSortMember()
	{
		return sortMember ;
	}



	/**
	 * this method  is for deleting a record from Note table
	 *
	 * @param   key  
	 */
	public void  deleteElement( int indvID, String key )throws CommunicationException,NamingException
	{
		int elementID = Integer.parseInt(key);
		ItemHome itemHome = (ItemHome)CVUtility.getHomeObject("com.centraview.account.item.ItemHome","Item");
		try
		{
			Item remote =(Item)itemHome.create();
			remote.setDataSource(this.dataSource);
			remote.deleteItem(indvID,elementID);
			// TODO com.centraview.account.item.ItemHome.deleteItem() user rights not implemented.
			// The Individual ID should be passed to the deleteItem method so it can be determined if this
			// user is allowed to do this.
		}
		catch(Exception e)
		{
			logger.error("[Exception] ItemList.deleteElement( int indvID, String key )", e);
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
	public ArrayList deleteElement(int individualID, String recordID[]) throws CommunicationException,NamingException,RemoteException
	{
		ArrayList resultDeleteLog = new ArrayList();
		ItemHome itemHome = (ItemHome)CVUtility.getHomeObject("com.centraview.account.item.ItemHome","Item");
		try
		{
			//call to EJB server
			Item remote =(Item)itemHome.create();
			remote.setDataSource(this.dataSource);
			for (int i=0; i<recordID.length; i++)
			{
				if(recordID[i] != null && !recordID[i].equals("")){
					int elementID = Integer.parseInt(recordID[i]);
					try{
						// TODO Pass the individualID and check for the authorization
						remote.deleteItem(individualID,elementID);
					}//end of try block
					catch(AuthorizationFailedException ae){
						String errorMessage = ae.getExceptionDescription();
						resultDeleteLog.add(errorMessage);
					}//end of catch block
					catch(ItemException ae){
						String errorMessage = ae.getExceptionDescription();
						resultDeleteLog.add(errorMessage);
					}//end of catch block					
				}//end of if(recordID[i] != null && !recordID[i].equals(""))
			}//end of for (int i=0; i<recordID.length; i++)
		}//end of try block
		catch( CreateException ce )
		{
			logger.error("[Exception] ItemList.deleteElement( int indvID, String rowId[] ) ", ce);
			throw new CommunicationException(ce.getMessage());
		}//end of catch( CreateException ce )
		this.setDirtyFlag(true);

		return resultDeleteLog;
	}// end of deleteElement	

	/**
	 *this method  is to duplicate  elements
	 *
	 */
	public void duplicateElement()
	{
	
	}


	/**
	 * this method  is to set PrimaryMember
	 *
	 * @param   value  
	 */
	public void setPrimaryMember( String value )
	{
		primaryMember = value ;
	}


	/**
	 * this method  is to set PrimaryTable
	 *
	 * @param   value  
	 */
	public void setPrimaryTable( String value )
	{
		this.primaryTable = value ;
	}
	


	/**
	 *
	 * this method  is for setting StartAT
	 * @param   startAT  
	 */
	public void setStartAT( int startAT )
	{
		this.startAT = startAT;
	}



	/**
	 * this method  sets the EndAT parameter
	 *
	 * @param   EndAt  
	 */
	public void setEndAT( int EndAt )
	{
		this.endAT = EndAt;
	}


	/**
	 * this method  sets BeginIndex
	 *
	 * @param   beginIndex  
	 */
	public void setBeginIndex( int beginIndex )
	{
		this.beginIndex = beginIndex ;
	}


	/**
	 * this method  sets EndIndex
	 *
	 * @param   endIndex  
	 */
	public void setEndIndex( int endIndex )
	{
		this.endIndex = endIndex;
	}


	/**
	 * this method  retrieves the PrimaryMember
	 *
	 * @return   String  
	 */
	public String getPrimaryMember()
	{
		 return primaryMember;
	}
	

}