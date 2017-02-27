/*
 * $RCSfile: EmailList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
package com.centraview.email;

import java.util.ArrayList;
import java.util.HashMap;

import com.centraview.common.DisplayList;
import com.centraview.common.EntityListElement;

/**
 * @author shirish d
 */
public class EmailList extends DisplayList
{
	private String sortMember ;
	private String primaryMember ;
	private String primaryTable ;
	private String PrimaryMemberType;
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	protected static boolean dirtyFlag = false;


	// constructor
	public  EmailList()
	{

		columnMap = new HashMap();
		columnMap.put( "Subject" , new Integer(100) );
		columnMap.put( "From" , new Integer(120) );
		columnMap.put( "To" , new Integer(120) ) ;
		columnMap.put( "Received" , new Integer(150) ) ;
		columnMap.put( "Size" , new Integer(50)) ;

		this.setPrimaryMember("Subject");
	}


	public boolean getDirtyFlag()
	{
		return 	dirtyFlag ;
	}

	public  void setDirtyFlag( boolean value )
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
		super.ListID 	= value ;
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
	public void  deleteElement( int indvID, String key )
	{
	}

	/**
	  * We will process the rowId. Incase if we don't have the right to DELETE a record then it will raise the AuthorizationException.
	  * We will catch the Exception and Log the Description of the Exception.
	  *
	  * @param individualID  ID for the Individual who is try to delete the record.
	  * @param recordID[] A String array of the recordID which we are try to delete it from database.
	  * @return resultDeleteLog A Collection of the Error Message while deleting a particular record.
	  */
	public ArrayList deleteElement(int individualID, String recordID[])	
	{
		ArrayList resultDeleteLog = new ArrayList();
		return resultDeleteLog;
	}//end of deleteElement( int indvID, String rowId[] )

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


}

