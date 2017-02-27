/*
 * $RCSfile: MergedIndividualConfirmationBean.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:19:18 $ - $Author: mcallist $
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

package com.centraview.administration.merge;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This bean is used once.  It simply contains the details about a
 * merged entity based on the information selected by the user.
 * The JSP simply prints out all the properties.
 *
 * @author Naresh Patel <npatel@centraview.com>
 */
public class MergedIndividualConfirmationBean implements Serializable
{
  private String individualName;
  private String firstName;
  private String middleName;
  private String lastName;
  private String marketingList;
  private int id;
  private int sourceID;
  private int entityID;
  private int listID;
  private String id2 = "";
  private String entityName = "";
  private String sourceName = "";
  private ArrayList addressCollection;
  private ArrayList methodOfContactCollection;

  /**
   * @return Returns the individualName.
   */
  public String getIndividualName()
  {
    return individualName;
  }

  /**
   * @return Returns the firstName.
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * @return Returns the middleName.
   */
  public String getMiddleName()
  {
    return middleName;
  }

  /**
   * @return Returns the lastName.
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * @return Returns the marketingList.
   */
  public String getMarketingList() {
    return marketingList;
  }

  /**
   * @return Returns the id.
   */
  public int getId()
  {
    return id;
  }

  /**
   * @return Returns the listID.
   */
  public int getListID()
  {
    return this.listID;
  }

  /**
   * @return Returns the sourceID.
   */
  public int getSourceID()
  {
    return sourceID;
  }

  /**
   * @return Returns the entityID.
   */
  public int getEntityID()
  {
    return entityID;
  }

  /**
   * @return Returns the id2.
   */
  public String getId2()
  {
    return id2;
  }

  /**
   * @return Returns the methodOfContactCollection.
   */
  public ArrayList getMethodOfContactCollection()
  {
    return methodOfContactCollection;
  }

  /**
   * @return Returns the entityName.
   */
  public String getEntityName()
  {
    return entityName;
  }


  /**
   * @return Returns the sourceName.
   */
  public String getSourceName()
  {
    return sourceName;
  }

  /**
   * @return Returns the addressCollection.
   */
  public ArrayList getAddressCollection()
  {
    return addressCollection;
  }

  /**
   * @param individualName The individualName to set.
   */
  public void setIndividualName(String string)
  {
    individualName = string;
  }

  /**
   * @param firstName The firstName to set.
   */
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * @param middleName The middleName to set.
   */
  public void setMiddleName(String middleName)
  {
    this.middleName = middleName;
  }

  /**
   * @param lastName The lastName to set.
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  /**
   * @param marketingList The marketingList to set.
   */
  public void setMarketingList(String marketingList) {
    this.marketingList = marketingList;
  }

  /**
   * @param id The id to set.
   */
  public void setId(int id)
  {
    this.id = id;
  }

  /**
   * @param sourceID The sourceID to set.
   */
  public void setSourceID(int sourceID)
  {
    this.sourceID = sourceID;
  }

  /**
   * @param entityID The entityID to set.
   */
  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  }

  /**
   * @param listID The listID to set.
   */
  public void setListID(int listID)
  {
    this.listID = listID;
  }

  /**
   * @param id2 The id2 to set.
   */
  public void setId2(String id2)
  {
    this.id2 = id2;
  }

  /**
   * @param methodOfContactCollection The methodOfContactCollection to set.
   */
  public void setMethodOfContactCollection(ArrayList methodOfContactList)
  {
    this.methodOfContactCollection = methodOfContactList;
  }

  /**
   * @param entityName The entityName to set.
   */
  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }

  /**
   * @param sourceName The sourceName to set.
   */
  public void setSourceName(String sourceName)
  {
    this.sourceName = sourceName;
  }

  /**
   * @param addressCollection The addressCollection to set.
   */
  public void setAddressCollection(ArrayList addressList)
  {
    this.addressCollection = addressList;
  }

  public String toString()
  {
    StringBuffer string = new StringBuffer();
    string.append("MergedIndividual: {individualName: "+individualName);
    string.append(", marketingList: "+marketingList);
		string.append(", firstName: "+firstName);
		string.append(", middleName: "+middleName);
		string.append(", lastName: "+lastName);
		string.append(", sourceID: "+sourceID);
		string.append(", entityID: "+entityID);
		string.append(", listID: "+listID);
		string.append(", sourceName: "+sourceName);
		string.append(", sourceName: "+entityName);
    string.append(", id: "+id);
    string.append(", id2: "+id2);
    string.append(", entityName: "+entityName);
    string.append(", addressCollection: "+addressCollection.toString());
    string.append(", methodOfContactCollection: "+methodOfContactCollection.toString());
    return string.toString();
  }



} // end MergedIndividualConfirmationBean
