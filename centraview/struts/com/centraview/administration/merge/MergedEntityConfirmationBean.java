/*
 * $RCSfile: MergedEntityConfirmationBean.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:19:18 $ - $Author: mcallist $
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
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class MergedEntityConfirmationBean implements Serializable
{
  private String entityName;
  private String marketingList;
  private int id;
  private String id2 = "";
  private String primaryContactName = "";
  private String accountManagerName = "";
  private String accountTeamName = "";
  private String sourceName = "";
  private ArrayList addressCollection;
  private ArrayList methodOfContactCollection;
  private int accountManagerID;
  private int accountTeamID;
  private int sourceID;
  private int listID;

  public String getAccountManagerName()
  {
    return accountManagerName;
  }

  public String getAccountTeamName()
  {
    return accountTeamName;
  }

  public ArrayList getAddressCollection()
  {
    return addressCollection;
  }

  public String getEntityName()
  {
    return entityName;
  }

  public int getId()
  {
    return id;
  }

  public String getId2()
  {
    return id2;
  }

  public ArrayList getMethodOfContactCollection()
  {
    return methodOfContactCollection;
  }

  public String getPrimaryContactName()
  {
    return primaryContactName;
  }


  public String getSourceName()
  {
    return sourceName;
  }

  public void setAccountManagerName(String string)
  {
    accountManagerName = string;
  }

  public void setAccountTeamName(String string)
  {
    accountTeamName = string;
  }

  public void setAddressCollection(ArrayList list)
  {
    addressCollection = list;
  }

  public void setEntityName(String string)
  {
    entityName = string;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setId2(String string)
  {
    id2 = string;
  }

  public void setMethodOfContactCollection(ArrayList list)
  {
    methodOfContactCollection = list;
  }

  public void setPrimaryContactName(String string)
  {
    primaryContactName = string;
  }

  public void setSourceName(String string)
  {
    sourceName = string;
  }

  public String toString()
  {
    StringBuffer string = new StringBuffer();
    string.append("MergedEntity: {entityName: "+entityName);
    string.append(", marketingList: "+marketingList);
    string.append(", id: "+id);
    string.append(", id2: "+id2);
    string.append(", primaryContactName: "+primaryContactName);
    string.append(", accountManagerName: "+accountManagerName);
    string.append(", accountTeamName: "+accountTeamName);
    string.append(", addressCollection: "+addressCollection.toString());
    string.append(", methodOfContactCollection: "+methodOfContactCollection.toString());
    return string.toString();
  }

  /**
   * @return Returns the marketingList.
   */
  public String getMarketingList() {
    return marketingList;
  }
  /**
   * @param marketingList The marketingList to set.
   */
  public void setMarketingList(String marketingList) {
    this.marketingList = marketingList;
  }

  /**
   * @return Returns the accountManagerID.
   */
  public int getAccountManagerID()
  {
    return accountManagerID;
  }

  /**
   * @param accountManagerID The accountManagerID to set.
   */
  public void setAccountManagerID(int accountManagerID)
  {
    this.accountManagerID = accountManagerID;
  }

  /**
   * @return Returns the accountTeamID.
   */
  public int getAccountTeamID()
  {
    return accountTeamID;
  }

  /**
   * @param accountTeamID The accountTeamID to set.
   */
  public void setAccountTeamID(int accountTeamID)
  {
    this.accountTeamID = accountTeamID;
  }

  /**
   * @return Returns the sourceID.
   */
  public int getSourceID()
  {
    return sourceID;
  }

  /**
   * @param sourceID The sourceID to set.
   */
  public void setSourceID(int sourceID)
  {
    this.sourceID = sourceID;
  }

  /**
   * @return Returns the listID.
   */
  public int getListID()
  {
    return this.listID;
  }

  /**
   * @param listID The listID to set.
   */
  public void setListID(int listID)
  {
    this.listID = listID;
  }

} // end MergedEntityConfirmationBean
