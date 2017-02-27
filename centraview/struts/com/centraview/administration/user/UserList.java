/*
 * $RCSfile: UserList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:50 $ - $Author: mking_cv $
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

package com.centraview.administration.user;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.EntityListElement;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFolderVO;

/**
 * This class is for storing the User List.
 * 
 * @author Iqbal
 */
public class UserList extends DisplayList
{
  protected static boolean dirtyFlag = false;
  private String sortMember;
  private String primaryMember;
  private String primaryTable = "user";
  private String PrimaryMemberType = "IndividualID";
  private int totalNoofRecords;
  private int beginIndex;
  private int endIndex;
  private int startAT;
  private int endAT;
  private static Logger logger = Logger.getLogger(UserList.class);

  /** Default Constructor. */
  public UserList()
  {
    columnMap = new HashMap();

    columnMap.put("UserID", new Integer(15));
    columnMap.put("IndividualID", new Integer(15));
    columnMap.put("Name", new Integer(15));
    columnMap.put("UserName", new Integer(15));
    columnMap.put("Entity", new Integer(15));
    columnMap.put("Email", new Integer(15));
    columnMap.put("Enabled", new Integer(15));

    this.setPrimaryMember("Name");
  }

  /**
   * this method is for retrieving whether list is dirty
   * 
   * @return boolean
   */
  public boolean getDirtyFlag()
  {
    return dirtyFlag;
  }

  /**
   * this method sets the list dirty
   * 
   * @param value
   */
  public void setDirtyFlag(boolean value)
  {
    dirtyFlag = value;
  }

  /**
   * this method returns the start parameter
   * 
   * @return int
   */
  public int getStartAT()
  {
    return startAT;
  }

  /**
   * this method returns the end parameter
   * 
   * @return int
   */
  public int getEndAT()
  {
    return endAT;
  }

  /**
   * this method returns the BeginIndex parameter
   * 
   * @return int
   */
  public int getBeginIndex()
  {
    return beginIndex;
  }

  /**
   * this method returns the EndIndex parameter
   * 
   * @return int
   */
  public int getEndIndex()
  {
    return endIndex;
  }

  /**
   * this method is for RecordsPerPage
   * 
   * @param value
   */
  public void setRecordsPerPage(int value)
  {
    recordsPerPage = value;
  }

  /**
   * this method is for setting TotalNoOfRecords
   * 
   * @param value
   */
  public void setTotalNoOfRecords(int value)
  {
    totalNoOfRecords = value;
  }

  /**
   * this method sets the ListID
   * 
   * @param value
   */
  public void setListID(long value)
  {
    super.ListID = value;
  }

  /**
   * return columnMap used in listing
   * 
   * @return HashMap
   */
  public HashMap getColumnMap()
  {
    return columnMap;
  }

  /**
   * this method sets the SortMember
   * 
   * @param value
   */
  public void setSortMember(String value)
  {
    sortMember = value;
  }

  /**
   * this method sets ListType
   * 
   * @param value
   */
  public void setListType(String value)
  {
    super.listType = value;
  }

  /**
   * this method returns SearchString
   * 
   * @return String
   */
  public String getSearchString()
  {
    return searchString;
  }

  /**
   * this method sets setSearchString
   * 
   * @param value
   */
  public void setSearchString(String value)
  {
    this.searchString = value;
  }

  /**
   * this method returns ListType
   * 
   * @return String
   */
  public String getListType()
  {
    return listType;
  }

  /**
   * this method returns PrimaryMemberType
   * 
   * @return String
   */
  public String getPrimaryMemberType()
  {
    return PrimaryMemberType;
  }

  /**
   * this method sets PrimaryMemberType
   * 
   * @param value
   */
  public void setPrimaryMemberType(String value)
  {
    PrimaryMemberType = value;
  }

  /**
   * this method returns TotalNoofRecords
   * 
   * @return int
   */
  public int getTotalNoofRecords()
  {
    return totalNoofRecords;
  }

  /**
   * this method returns PrimaryTable
   * 
   * @return String
   */
  public String getPrimaryTable()
  {
    return primaryTable;
  }

  /**
   * this method returns SortMember
   * 
   * @return String
   */
  public String getSortMember()
  {
    return sortMember;
  }

  /**
   * this method is for deleting a record from Faq table
   * 
   * @param key
   */
  public void deleteElement(int indvID, String key) throws CommunicationException, NamingException
  {
    int elementID = Integer.parseInt(key);
    UserHome userHome = (UserHome)CVUtility.getHomeObject("com.centraview.administration.user.UserHome", "User");
    try {

      User remote = userHome.create();
      remote.setDataSource(this.dataSource);

      CvFileFacade cvf = new CvFileFacade();
      CvFolderVO folder = cvf.getHomeFolder(elementID, this.dataSource);

      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      String yyyymmdd = sdf.format(new java.util.Date(System.currentTimeMillis()));

      folder.setName(folder.getName() + "." + yyyymmdd + "." + "deleted");
      folder.setOwner(indvID);
      cvf.updateFolder(indvID, folder, this.dataSource);

      remote.deleteUser(elementID);

      // TODO user-rights on deleteUser ?
    } catch (Exception e) {
      logger.error("[Exception] UserList.deleteElement( int indvID, String key)", e);
    }
  }
  /**
   * We will process the rowId. Incase if we don't have the right to DELETE a
   * record then it will raise the AuthorizationException. We will catch the
   * Exception and Log the Description of the Exception.
   * 
   * @param individualID ID for the Individual who is try to delete the record.
   * @param recordID[] A String array of the recordID which we are try to delete
   *          it from database.
   * @return resultDeleteLog A Collection of the Error Message while deleting a
   *         particular record.
   */
  public ArrayList deleteElement(int individualID, String recordID[]) throws CommunicationException, NamingException, RemoteException
  {
    ArrayList resultDeleteLog = new ArrayList();
    UserHome userHome = (UserHome)CVUtility.getHomeObject("com.centraview.administration.user.UserHome", "User");
    try {
      //call to EJB server
      User remote = userHome.create();
      remote.setDataSource(this.dataSource);
      for (int i = 0; i < recordID.length; i++) {
        if (recordID[i] != null && !recordID[i].equals("")) {
          int elementID = Integer.parseInt(recordID[i]);
          try {
            remote.deleteUser(elementID);
            // TODO to pass the individual ID and perform the user rights
          } catch (UserException ue) {
            String errorMessage = ue.getExceptionDescription();
            resultDeleteLog.add(errorMessage);
          }//end of catch block
        }//end of if(recordID[i] != null && !recordID[i].equals(""))
      }//end of for (int i=0; i<recordID.length; i++)
    }//end of try block
    catch (CreateException e) {
      logger.error("[Exception] UserList.deleteElement( int indvID, String rowId[] ) ", e);
      throw new CommunicationException(e.getMessage());
    }//end of catch( CreateException e )
    this.setDirtyFlag(true);

    return resultDeleteLog;
  }// end of deleteElement

  /**
   * This method is to duplicate elements.
   * 
   * @deprecated unimplemented method
   */
  public void duplicateElement()
  {
    System.out.println("!!!!!!! THIS METHOD IS NOT IMPLEMENTED !!!!!!");
  }

  /**
   * this method is to set PrimaryMember
   * 
   * @param value
   */
  public void setPrimaryMember(String value)
  {
    primaryMember = value;
  }

  /**
   * this method is to set PrimaryTable
   * 
   * @param value
   */
  public void setPrimaryTable(String value)
  {
    primaryTable = value;
  }

  /**
   * this method is to addEntityElement
   * 
   * @param value
   */
  public void addEntityElement(EntityListElement value)
  {}

  /**
   * this method is for retrieving getEntityElement
   * 
   * @param String
   */
  public void getEntityElement(String value)
  {}

  /**
   * 
   * this method is for setting StartAT
   * @param startAT
   */
  public void setStartAT(int startAT)
  {
    this.startAT = startAT;
  }

  /**
   * this method sets the EndAT parameter
   * 
   * @param EndAt
   */
  public void setEndAT(int EndAt)
  {
    this.endAT = EndAt;
  }

  /**
   * this method sets BeginIndex
   * 
   * @param beginIndex
   */
  public void setBeginIndex(int beginIndex)
  {
    this.beginIndex = beginIndex;
  }

  /**
   * this method sets EndIndex
   * 
   * @param endIndex
   */
  public void setEndIndex(int endIndex)
  {
    this.endIndex = endIndex;
  }

  /**
   * this method retrieves the PrimaryMember
   * 
   * @return String
   */
  public String getPrimaryMember()
  {
    return primaryMember;
  }
}