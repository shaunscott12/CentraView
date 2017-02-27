/*
 * $RCSfile: MarketingListMemberList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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
package com.centraview.marketing;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.StringMember;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;

/**
 * This class is the object that contains a list of the Marketing List Members.
 *
 * @author   Ryan Grier <ryan@centraview.com>
 */
public class MarketingListMemberList extends DisplayList
{
  //the individualD of owner = pravink
  private int individualID;

	/** Determines whether the list needs to be updated. */
  protected static boolean dirtyFlag = false;

  /** The total number of records for this list. */
  private int totalNoofRecords;

  /** Where to start? */
  private int beginIndex;

  /** Where to end? */
  private int endIndex;

  /** I"m not exactly sure, but it's in all of the XXList classes. */
  private int startAT;

  /** I"m not exactly sure, but it's in all of the XXList classes. */
  private int endAT;

  /** What the List is sorted by. */
  private String sortMember;

  /** The primary member of the list. */
  private String primaryMember;

  /** The primary database table of the list. */
  private String primaryTable;

  /** I"m not exactly sure, but it's in all of the XXList classes. */
  private String PrimaryMemberType;

  private static Logger logger = Logger.getLogger(MarketingListMemberList.class);

  /** The MarketingListMemberList Constructor */
  public MarketingListMemberList()
  {
    columnMap = new HashMap();
    columnMap.put("Entity", new Integer(100));
    columnMap.put("Individual", new Integer(100));
    columnMap.put("Email", new Integer(100));
    columnMap.put("PhoneNumber", new Integer(100));
    this.setPrimaryMember("Entity");
    this.setSortMember("Entity");
  }  //end of MarketingListMemberList constructor

  /**
   * Returns the dirty flag
   *
   * @return   Whether the dirty flag is true or not.
   */
  public boolean getDirtyFlag()
  {
    return dirtyFlag;
  } //end of getDirtyFlag method

  /**
   * Sets the dirty flag
   *
   * @param dirtyFlag  The new dirtyFlag value
   */
  public void setDirtyFlag(boolean newDirtyFlag)
  {
    dirtyFlag = newDirtyFlag;
  }  //end of setDirtyFlag method

  /**
   * Returns the the StartAt. I'm not quite sure exactly what this value
   * represents yet.
   *
   * @return   The startAT value
   */
  public int getStartAT()
  {
    return this.startAT;
  } //end of getStartAT method

  /**
   * Sets the StartAt. I'm not quite sure exactly what this value represents
   * yet.
   *
   * @param startAT  The new startAT value.
   */
  public void setStartAT(int startAT)
  {
    this.startAT = startAT;
  } //end of setStartAT method

  /**
   * Returns the EndAt. I'm not quite sure exactly what this value represents
   * yet.
   *
   * @return   The endAT value
   */
  public int getEndAT()
  {
    return this.endAT;
  } //end of getEndAt method

  /**
   * Sets the the EndAt. I'm not quite sure exactly what this value represents
   * yet.
   *
   * @param endAT  The new startAT value.
   */
  public void setEndAT(int endAT)
  {
    this.endAT = endAT;
  } //end of setEndAt method

  /**
   * Returns the List's Begin Index
   *
   * @return   The begin index of the list.
   */
  public int getBeginIndex()
  {
    return this.beginIndex;
  } //end of getBeginIndex method

  /**
   * Sets he List's Begin Index
   *
   * @param beginIndex  The begin index of the list.
   */
  public void setBeginIndex(int beginIndex)
  {
    this.beginIndex = beginIndex;
  } //end of setBeginIndex method

  /**
   * Returns the List's End Index
   *
   * @return   The end index of the list.
   */
  public int getEndIndex()
  {
    return this.endIndex;
  } //end of getEndIndex method

  /**
   * Sets he List's End Index
   *
   * @param endIndex  The end index of the list.
   */
  public void setEndIndex(int endIndex)
  {
    this.endIndex = endIndex;
  } //end of setEndIndex method

  /**
   * Sets the number of Records Per Page
   *
   * @param recordsPerPage  How many records per page.
   */
  public void setRecordsPerPage(int recordsPerPage)
  {
    this.recordsPerPage = recordsPerPage;
  } //end of setRecordsPerPage method

  /**
   * Returns the Total Number of Records for the List.
   *
   * @return   The total number of records for the list.
   */
  public int getTotalNoofRecords()
  {
    return this.totalNoofRecords;
  } //end of getTotalNoOfRecords method

  /**
   * Sets the Total Number of Records for the List.
   *
   * @param totalNoOfRecords  The total number of records for the list.
   */
  public void setTotalNoOfRecords(int totalNoOfRecords)
  {
    this.totalNoOfRecords = totalNoOfRecords;
  } //end of setTotalNoOfRecords method

  /**
   * Sets the List Id.
   *
   * @param listId  The new List ID.
   */
  public void setListID(long listId)
  {
    super.ListID = listId;
  } //end of setListID method

  /**
   * Returns the HashMap with the Column Names used in this list.
   *
   * @return   The HashMap with the Column Names used in this list.
   */
  public HashMap getColumnMap()
  {
    return this.columnMap;
  } //end of getColumnMap method

  /**
   * Returns the Sort Member of the list. This specifies how the list is sorted.
   *
   * @return   The sort member of the list.
   */
  public String getSortMember()
  {
    return this.sortMember;
  } //end of getSortMember method

  /**
   * Sets the Sort Member of the list. This specifies how the list is sorted.
   *
   * @param sortMember  The sort member of the list.
   */
  public void setSortMember(String sortMember)
  {
    this.sortMember = sortMember;
  } //end of setSortMember method

  /**
   * Returns the List type of this list.
   *
   * @return   The Type of List this is.
   */
  public String getListType()
  {
    return this.listType;
  } //end of getListType method

  /**
   * Sets the list type of this list.
   *
   * @param listType  The Type of List this is.
   */
  public void setListType(String listType)
  {
    super.listType = listType;
  } //end of setListType method

  /**
   * Returns the Search String used in this list.
   *
   * @return   The search string used in this list.
   */
  public String getSearchString()
  {
    return this.searchString;
  } //end of getSearchString method

  /**
   * Sets the Search String used in this list.
   *
   * @param searchString  The search string used in this list.
   */
  public void setSearchString(String searchString)
  {
    this.searchString = searchString;
  } //end of setSearchString method

  /**
   * Returns the Type of the PrimaryMember.
   *
   * @return   The type of the primary member.
   */
  public String getPrimaryMemberType()
  {
    return this.PrimaryMemberType;
  } //end of getPrimaryMemberType method

  /**
   * sets the Primary Member Type
   *
   * @param primaryMemberType
   */
  public void setPrimaryMemberType(String primaryMemberType)
  {
    this.PrimaryMemberType = primaryMemberType;
  } //end of setPrimaryMemberType method

  /**
   * Returns the list's Primary Table
   *
   * @return   The List's primary table.
   */
  public String getPrimaryTable()
  {
    return this.primaryTable;
  } //end of getPrimaryTable method

  /**
   * Sets the list's Primary Table
   *
   * @param primaryTable  The List's primary table.
   */
  public void setPrimaryTable(String primaryTable)
  {
    this.primaryTable = primaryTable;
  } //end of setPrimaryTable method

  /**
   * Returns the Primary member of this list.
   *
   * @return   The Primary member of this list.
   */
  public String getPrimaryMember()
  {
    return this.primaryMember;
  } //end of getPrimaryMember method

  /**
   * Sets the Primary member of this list.
   *
   * @param primaryMember  The Primary member of this list.
   */
  public void setPrimaryMember(String primaryMember)
  {
    this.primaryMember = primaryMember;
  } //end of setPrimaryMember method

  /** Duplicate Element */
  public void duplicateElement()
  {
    //Not currently implemented
  } //end of duplicateElement method

  /**
   * Deletes an Element in the List.
   *
   * @param elementID  The elementID of the item to delete.
   */
  public void deleteElement(int indvID, String elementID)
  {
    //This int is an arbitrairy
    int elementIntID = Integer.parseInt(elementID);
    StringBuffer sb = new StringBuffer("00000000000");
    sb.setLength(11);
    String countString = Integer.toString(elementIntID);
    sb.replace((sb.length() - countString.length()), (sb.length()), countString);
    String newElementID = sb.toString();
    try
    {
      //TODO: implement to delete entity or individual
      MarketingListMemberListElement elementAt = (MarketingListMemberListElement) this.get(newElementID);

      //Get Individual ID
      String individualStringID = (String) ((StringMember) elementAt.get("IndividualID")).getMemberValue();
      String entityStringID = (String) ((StringMember) elementAt.get("EntityID")).getMemberValue();

      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)
      CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade contactFacade = contactFacadeHome.create();
      contactFacade.setDataSource(this.dataSource);

      if (individualStringID == null || individualStringID.equals("") || individualStringID.equals("0"))
      {
        //delete the entity
        //Argument individualID is added - pravink
        contactFacade.deleteEntity(Integer.parseInt(entityStringID),indvID);
      } //end of if statement (individualStringID == null ...
      else
      {
        //delete the individual

        //Argument individualID is added - pravink
        contactFacade.deleteIndividual(Integer.parseInt(individualStringID),indvID);
      } //end of else statement (individualStringID == null ...
    } //end of try block
    catch (Exception e)
    {
	  logger.error("[Exception] MarketingListMemberList.deleteElement( int indvID, String key ) ", e);
    } //end of catch block
  } //end of deleteElement method

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

		  ContactFacade contactFacade = contactFacadeHome.create();
		  contactFacade.setDataSource(this.dataSource);
		  for (int i=0; i<recordID.length; i++)
		  {
			  if(recordID[i] != null && !recordID[i].equals("")){
				  int elementID = Integer.parseInt(recordID[i]);
				  StringBuffer sb = new StringBuffer("00000000000");
				  sb.setLength(11);
				  String countString = Integer.toString(elementID);
				  sb.replace((sb.length() - countString.length()), (sb.length()), countString);
				  String newElementID = sb.toString();
				  MarketingListMemberListElement elementAt = (MarketingListMemberListElement) this.get(newElementID);
				  String individualStringID = (String) ((StringMember) elementAt.get("IndividualID")).getMemberValue();
				  String entityStringID = (String) ((StringMember) elementAt.get("EntityID")).getMemberValue();

				  try{
					if (individualStringID == null || individualStringID.equals("") || individualStringID.equals("0"))
					{
					  //delete the entity
					  //Argument individualID is added - pravink
					  contactFacade.deleteEntity(Integer.parseInt(entityStringID),individualID);
					} //end of if statement (individualStringID == null ...
					else
					{
					  //delete the individual
					  //Argument individualID is added - pravink
					  contactFacade.deleteIndividual(Integer.parseInt(individualStringID),individualID);
					} //end of else statement (individualStringID == null ...
				  }//end of try block
				  catch(AuthorizationFailedException ae){
					  String errorMessage = ae.getExceptionDescription();
					  resultDeleteLog.add(errorMessage);
				  }//end of catch block
				  catch(RemoteException remoteException){
					logger.error("[Exception] MarketingListMemberList.deleteElement( int indvID, String rowId[] ) ", remoteException);
				  }//end of catch block
			  }//end of if(recordID[i] != null && !recordID[i].equals(""))
		  }//end of for (int i=0; i<recordID.length; i++)
	  }//end of try block
	  catch( CreateException e )
	  {
	    logger.error("[Exception] MarketingListMemberList.deleteElement( int indvID, String rowId[] ) ", e);
		throw new CommunicationException(e.getMessage());
	  }//end of catch( CreateException e )
	  catch (RemoveException re)
	  {
	    logger.error("[Exception] MarketingListMemberList.deleteElement( int indvID, String rowId[] ) ", re);
	  }
	  this.setDirtyFlag(true);

	  return resultDeleteLog;
  }// end of deleteElement


  /**
   * Creates an empty copy of the LicenseList.
   *
   * @param list  A fully populated LicenseList.
   * @return      A new LicenseList with all of the settings set, but now
   *      elements (objects) in the list.
   */
  public final static MarketingListMemberList createEmptyObject(MarketingListMemberList list)
  {
    MarketingListMemberList emptyList = new MarketingListMemberList();
    emptyList.setListType(list.getListType());
    emptyList.setPrimaryMemberType(list.getPrimaryMemberType());
    emptyList.setPrimaryTable(list.getPrimaryTable());
    emptyList.setSortMember(list.getSortMember());
    emptyList.setSortType(list.getSortType());
    emptyList.setPrimaryMember(list.getPrimaryMember());
    emptyList.setStartAT(list.getStartAT());
    emptyList.setEndAT(list.getEndAT());
    emptyList.setBeginIndex(0);
    emptyList.setEndIndex(0);
    emptyList.setTotalNoOfRecords(list.getTotalNoOfRecords());

    return emptyList;
  } //end of createEmptyObject method

  /**
   * Returns a <code>,</code> delimited string with the settings of the list.
   * This method overrides the Object.toString() method.
   *
   * @return A <code>,</code> delimited string with the settings of the list.
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("List ID: " + Long.toString(this.getListID()) + ",");
    sb.append("Begin Index: " + Integer.toString(this.getBeginIndex()) + ",");
    sb.append("End Index: " + Integer.toString(this.getEndIndex()) + ",");
    sb.append("Start At: " + Integer.toString(this.getStartAT()) + ",");
    sb.append("End At: " + Integer.toString(this.getEndAT()) + ",");
    sb.append("Dirty?: " + Boolean.toString(this.getDirtyFlag()) + ",");
    sb.append("Complete?: " + Boolean.toString(this.getCompleteFlag()) + ",");
    sb.append("List Type: " + this.getListType() + ",");
    sb.append("Power String: " + this.getPowerString() + ",");
    sb.append("Search String: " + this.getSearchString() + ",");
    sb.append("Primary Member: " + this.getPrimaryMember() + ",");
    sb.append("Primary Member Type: " + this.getPrimaryMemberType() + ",");
    sb.append("Primary Table: " + this.getPrimaryTable() + ",");
    sb.append("Sort Member: " + this.getSortMember() + ",");
    sb.append("Sort Type: " + Character.toString(this.getSortType()) + ",");

    sb.append("Records in memory: " + Integer.toString(this.getRecordsInMemory()) + ",");
    sb.append("Records per page: " + Integer.toString(this.getrecordsPerPage()) + ",");
    sb.append("Total Records: " + Integer.toString(this.getTotalNoofRecords()) + ",");
    sb.append("}");

    return sb.toString();
  } //end of toString method


// Method to set the Uswer who generates this List - pravink

   /**
    * Set the individualID of owner
	* @param individualID
	*/
   public void setIndividualID(int individualID){
    	this.individualID = individualID;
   }

  /**
   * Returns the individualID
   * @return individualID
   */
  public int getIndividualID(){
  	return individualID;
  }
  //Endf pravink

}  //end of MarketingListMemberList class