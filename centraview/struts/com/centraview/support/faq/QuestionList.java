/*
 * $RCSfile: QuestionList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:48 $ - $Author: mking_cv $
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
/**
 * ThreadList.java
 * @version 	1.0 Date  2003/05/9
 * @author Amit Gandhe
 */
package com.centraview.support.faq;

import java.util.ArrayList;
import java.util.HashMap;

import com.centraview.common.DisplayList;
import com.centraview.common.EntityListElement;




/*
	This class is for storing the Thread List
*/

public class QuestionList extends DisplayList
{

	private String sortMember ;
	private String primaryMember ;
	private String primaryTable ="question";
	private String PrimaryMemberType="questionid";
	private int totalNoofRecords ;
	private int beginIndex;
	private int endIndex;
	private int startAT;
	private int endAT;
	protected static boolean dirtyFlag = false;

		private int curFaqID;




	/**
	 *
	 * Constructor
	 */
	public QuestionList() {
		columnMap=new HashMap();


		columnMap.put("Question",new Integer(15));
		columnMap.put("Answer",new Integer(15));


		this.setPrimaryMember("Question");


	}

	/**
	 * this method  is for retrieving whether list is dirty
	 *
	 * @return   boolean
	 */
	public boolean getDirtyFlag() {
		return 	dirtyFlag ;
	}



	/**
	 * this method  sets the list dirty
	 *
	 * @param   value
	 */
	public void setDirtyFlag( boolean value ) {
		dirtyFlag = value ;
	}



	/**
	 * this method  returns the start parameter
	 *
	 * @return    int
	 */
	public int getStartAT() {
		return  startAT ;
	}


	/**
	 * this method  returns the end parameter
	 *
	 * @return     int
	 */
	public int getEndAT() {
		return  endAT ;
	}



	/**
	 * this method  returns the BeginIndex parameter
	 *
	 * @return   int
	 */
	public int getBeginIndex() {
		return  beginIndex ;
	}


	/**
	 * this method  returns the EndIndex parameter
	 *
	 * @return     int
	 */
	public int getEndIndex() {
		return  endIndex ;
	}



	/**
	 * this method  is for RecordsPerPage
	 *
	 * @param   value
	 */
	public  void  setRecordsPerPage( int value ) {
		recordsPerPage = value ;
	}


	/**
	 * this method  is for setting TotalNoOfRecords
	 *
	 * @param   value
	 */
	public  void  setTotalNoOfRecords(int value ) {
		totalNoOfRecords = value;
	}

	/**
	 * this method  sets the ListID
	 *
	 * @param   value
	 */
	public void setListID( long value ) {
		super.ListID = value ;
	}


	/**
	 * return columnMap used in listing
	 *
	 * @return  HashMap
	 */
	public HashMap getColumnMap() {
		return	columnMap;
	}


	/**
	 * this method  sets the SortMember
	 *
	 * @param   value
	 */
	public void setSortMember( String value ) {
		sortMember = value ;
	}



	/**
	 * this method  sets ListType
	 *
	 * @param   value
	 */
	public void setListType( String value ) {
		super.listType = value ;
	}



	/**
	 * this method  returns SearchString
	 *
	 * @return   String
	 */
	public String getSearchString() {
		return 	searchString ;
	}



	/**
	 * this method  sets setSearchString
	 *
	 * @param   value
	 */
	public void setSearchString( String value ) {
		this.searchString = value ;
	}



	/**
	 * this method  returns ListType
	 *
	 * @return  String
	 */
	public String getListType() {
		return listType ;
	}



	/**
	 * this method  returns PrimaryMemberType
	 *
	 * @return   String
	 */
	public String getPrimaryMemberType() {
		return PrimaryMemberType ;
	}



	/**
	 * this method  sets PrimaryMemberType
	 *
	 * @param   value
	 */
	public void setPrimaryMemberType( String value ) {
		PrimaryMemberType = value  ;
	}



	/**
	 * this method  returns TotalNoofRecords
	 *
	 * @return int
	 */
	public int getTotalNoofRecords() {
		return totalNoofRecords;
	}



	/**
	 * this method  returns PrimaryTable
	 *
	 * @return  String
	 */
	public String getPrimaryTable() {
		return primaryTable ;
	}


	/**
	 * this method  returns SortMember
	 *
	 * @return String
	 */
	public String getSortMember() {
		return sortMember ;
	}



	/**
	 * this method  is for deleting a record from Note table
	 *
	 * @param   key
	 */
	public void  deleteElement( int indvID, String key ) {
	}


	public ArrayList deleteElement( int indvID, String rowId[] )
	{
		ArrayList resultDeleteLog = new ArrayList();
		return resultDeleteLog;		
	}//end of deleteElement( int indvID, String rowId[] )

	/**
	* this method  is for addelements to the collection
	*/

	//not in Display list
	/*public void addElement( String  ID , EntityListElement value )
	{
	put( ID , value );
	}*/



	/**
	 *this method  is to duplicate  elements
	 *
	 */
	public void duplicateElement() {

	}


	/**
	 * this method  is to set PrimaryMember
	 *
	 * @param   value
	 */
	public void setPrimaryMember( String value ) {
		primaryMember = value ;
	}


	/**
	 * this method  is to set PrimaryTable
	 *
	 * @param   value
	 */
	public void setPrimaryTable( String value ) {
		primaryTable = value ;
	}



	/**
	 * this method  is to addEntityElement
	 *
	 * @param   value
	 */
	public void addEntityElement( EntityListElement value ) {
	}



	/**
	 * this method  is for retrieving getEntityElement
	 *
	 * @param   String
	 */
	public void getEntityElement( String value ) {
	}



	/**
	 *
	 * this method  is for setting StartAT
	 * @param   startAT
	 */
	public void setStartAT( int startAT ) {
		this.startAT = startAT;
	}



	/**
	 * this method  sets the EndAT parameter
	 *
	 * @param   EndAt
	 */
	public void setEndAT( int EndAt ) {
		this.endAT = EndAt;
	}


	/**
	 * this method  sets BeginIndex
	 *
	 * @param   beginIndex
	 */
	public void setBeginIndex( int beginIndex ) {
		this.beginIndex = beginIndex ;
	}


	/**
	 * this method  sets EndIndex
	 *
	 * @param   endIndex
	 */
	public void setEndIndex( int endIndex ) {
		this.endIndex = endIndex;
	}


	/**
	 * this method  retrieves the PrimaryMember
	 *
	 * @return   String
	 */
	public String getPrimaryMember() {
		return primaryMember;
	}

	public int getCurFaqID() {
		return this.curFaqID;
	}

	public void setCurFaqID(int curFaqID) {
		this.curFaqID = curFaqID;
	}



}
