/*
 * $RCSfile: DisplayList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:58 $ - $Author: mking_cv $
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

abstract public class DisplayList extends TreeMap
{
  private static Logger logger = Logger.getLogger(DisplayList.class);
  protected String dataSource;
  protected char listAuth;
  protected boolean completeFlag;
  protected String searchString;
  protected int recordsInMemory;
  protected int individualId;
  protected HashMap columnMap;
  protected long ListID;
  protected String listType;
  protected int recordsPerPage;
  protected int totalNoOfRecords;
  protected char sortType = 'A';
  private String powerString;
  protected boolean advanceSearchFlag = false;
  protected boolean customerViewFlag = false;
  protected ArrayList individualUsers;
  abstract public boolean getDirtyFlag();
  abstract public void setDirtyFlag(boolean value);
  abstract public void setSortMember(String value);
  abstract public void setListType(String value);
  abstract public String getPrimaryMemberType();
  abstract public String getPrimaryTable();
  abstract public String getSortMember();
  abstract public void deleteElement(int indvID, String key)throws CommunicationException,NamingException;
  /**
	* We will process the rowId. Incase if we don't have the right to DELETE a record then it will raise the AuthorizationException.
	* We will catch the Exception and Log the Description of the Exception.
	*
	* @param individualID  ID for the Individual who is try to delete the record.
	* @param recordID[] A String array of the recordID which we are try to delete it from database.
	* @return resultDeleteLog A Collection of the Error Message while deleting a particular record.
	*/
  abstract public ArrayList deleteElement(int indvID, String key[])throws CommunicationException,NamingException,RemoteException;
  abstract public void duplicateElement();
  abstract public HashMap getColumnMap();
  abstract public void setStartAT(int value);
  abstract public void setEndAT(int value);
  abstract public void setEndIndex(int value);
  abstract public void setBeginIndex(int value);
  abstract public int getStartAT();
  abstract public int getEndAT();
  abstract public int getEndIndex();
  abstract public int getBeginIndex();
  abstract public void setRecordsPerPage(int value);
  abstract public void setTotalNoOfRecords(int value);
  abstract public void setListID(long value);
  abstract public String getPrimaryMember();

  public int getrecordsPerPage()
  {
    return recordsPerPage;
  }

  public int getTotalNoOfRecords()
  {
    return totalNoOfRecords;
  }

  public String getListType()
  {
    return listType;
  }

  public long getListID()
  {
    return ListID;
  }

  //
  public char getListAuth()
  {
    return listAuth;
  }

  //
  public boolean getCompleteFlag()
  {
    return completeFlag;
  }

  public String getSearchString()
  {
    return searchString;
  }

  abstract public int getTotalNoofRecords();

  public int getRecordsInMemory()
  {
    return listAuth;
  }

  //set methods
  public void setListAuth(char value)
  {
    listAuth = value;
  }

  public int getIndividualId()
  {
    return individualId;
  }

  public void setIndividualId(int individualId)
  {
    this.individualId = individualId;
  }

  public void setCompleteFlag(boolean value)
  {
    completeFlag = value;
  }

  public void setSearchString(String value)
  {
    searchString = value;
  }

  public void setRecordsInMemory(int value)
  {
    recordsInMemory = value;
  }

  public char getSortType()
  {
    return this.sortType;
  }

  public void setSortType(char sortType)
  {
    this.sortType = sortType;
  }

  /**
   * In this method if search string is present then continue with
   * element otherwise remove the element.
   */
  public void search()
  {
    Set listkey = keySet();
    Iterator it = listkey.iterator();
    String str = null;

    while (it.hasNext())
    {
      str = (String) it.next();
      ListElement ele = (ListElement) get(str);

      if (!ele.search(searchString))
      {
        it.remove();
      } //end of if statement (!ele.search(searchString))
    } //end of while loop (it.hasNext())
  } //end of search method

  /**
   * In this method if search string is present then continue with
   * element otherwise remove the element.
   * Carry out the Search process on particular column only.
   */
  public void search(String searchColumn)
  {
    Set listkey = keySet();
    Iterator it = listkey.iterator();
    String str = null;

    while (it.hasNext())
    {
      str = (String) it.next();
      ListElement ele = (ListElement) get(str);

      if (!ele.search(searchString,searchColumn))
      {
        it.remove();
      } //end of if statement (!ele.search(searchString))
    } //end of while loop (it.hasNext())
  } //end of search method

  /**
   *  This method set the Power String in Advance search Case
   *
   * @return
   */
  public String getPowerString()
  {
    return this.powerString;
  }

  /**
   *  This method returns the Power String.
   *
   * @param   powerString
   */
  public void setPowerString(String powerString)
  {
    this.powerString = powerString;
  }

  /**
   *  This method set the Advance Search Flag
   *
   * @return boolean
   */
  public boolean getAdvanceSearchFlag()
  {
    return this.advanceSearchFlag;
  }

  /**
   *  This method returns the Advance Search Flag.
   *
   * @param   advanceSearchFlag
   */
  public void setAdvanceSearchFlag(boolean advanceSearchFlag)
  {
    this.advanceSearchFlag = advanceSearchFlag;
  }

  /**
   *  This method set the Customer View Flag
   *
   * @return boolean
   */
  public boolean getCustomerViewFlag()
  {
    return this.customerViewFlag;
  }

  /**
   *  This method returns the Customer View Flag.
   *
   * @param   customerViewFlag
   */
  public void setCustomerViewFlag(boolean customerViewFlag)
  {
    this.customerViewFlag = customerViewFlag;
  }


  // toString() now prints out the keys also.
  public String toString()
  {
    Collection keys = this.keySet();
    StringBuffer toReturn = new StringBuffer();

    for (Iterator i = keys.iterator(); i.hasNext();)
    {
      String key = (String)i.next();
      toReturn.append(key+": ");
      toReturn.append(this.get(key));
      toReturn.append("\n");
    }

    return (toReturn.toString());
  }

  public void setDataSource(String dataSource)
  {
    this.dataSource = dataSource;
  } //end of setDataSource method

	public ArrayList getIndividualUsers()
	{
		return this.individualUsers;
	}
	public void setIndividualUsers(String strIndividualUsers)
	{
		this.individualUsers.add(strIndividualUsers);
	}
  
  /**
   * Compares a few key fields in the lists.
   * 1. if the Ids are the same.
   * 2. this list is not dirty.
   * 3. StartAt == StartAt
   * 4. EndAt == EndAt
   * 5. The Sort parameters are the same.
   * @param dl display list to compare to this one.
   * @return true if they are the same and this isn't dirty. otherwise false.
   */
  public boolean compareList(DisplayList dl)
  {
    if (dl == null)
    {
      return false;
    }
    if (
        (this.getListID() == dl.getListID()) 
        && (this.getDirtyFlag() == false) 
        && (this.getStartAT() >= dl.getStartAT()) 
        && (this.getEndAT() <= dl.getEndAT()) 
        && (this.getSortMember().equals(dl.getSortMember()))
        && (this.getSortType() == dl.getSortType())
       )
    {
      return true;
    }
    return false;
  } // end compareList
}
