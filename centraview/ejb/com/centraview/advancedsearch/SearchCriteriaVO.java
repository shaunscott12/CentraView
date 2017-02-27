/*
 * $RCSfile: SearchCriteriaVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:51 $ - $Author: mking_cv $
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

package com.centraview.advancedsearch;

import java.io.Serializable;

/**
 * This class holds the values for the saved
 * search criteria.
 * 
 * @author Ryan Grier <ryan@centraview.com>
 */
public class SearchCriteriaVO implements Serializable
{
  /** The ID of the Search Criteria. */
  private String searchCriteriaID = "";
  
  /** The ID of the Search this criteria is related to. */
  private String searchID = "";
  
  /** The ID of the table for this Search Criteria. */
  private String tableID = "";
  
  /** The ID of the field for this Search Criteria. */
  private String fieldID = "";
  
  /** The ID of the Condition Choice for this Search Criteria. */
  private String conditionID = "";
  
  /** The ID of the Group this Search Criteria is linked to. */
  private String groupID = "";
  
  /** The Expression Type of this Search Criteria. And/Or. */
  private String expressionType = "";
  
  /** The Value of this Search Criteria. What to search for. */
  private String value = "";
  
 
  /**
   * Returns the ID of the Search Criteria.
   * 
   * @return The ID of the Search Criteria.
   */
  public String getSearchCriteriaID()
  {
    return this.searchCriteriaID;
  } //end of getSearchCriteriaID method
  
  /**
   * Sets the Search ID of the Search Criteria.
   * 
   * @param searchCriteriaID The Search ID of the 
   * Search Criteria.
   */
  public void setSearchID(String searchID)
  {
    this.searchID = searchID;
  } //end of setSearchID method
  
  /**
   * Returns the Search ID of the Search Criteria.
   * 
   * @return The Search ID of the Search Criteria.
   */
  public String getSearchID()
  {
    return this.searchID;
  } //end of getSearchID method
  
  /**
   * Sets the ID of the Search Criteria.
   * 
   * @param searchCriteriaID The ID of the Search Criteria.
   */
  public void setSearchCriteriaID(String searchCriteriaID)
  {
    this.searchCriteriaID = searchCriteriaID;
  } //end of setSearchCriteriaID method
  
  /**
   * Returns the ID of the table for this Search Criteria.
   * 
   * @return The ID of the table for this Search Criteria.
   */
  public String getTableID()
  {
    return this.tableID;
  } //end of getTableID method
  
  /**
   * Sets the ID of the table for this Search Criteria.
   * 
   * @param tableID The ID of the table for 
   * this Search Criteria.
   */
  public void setTableID(String tableID)
  {
    this.tableID = tableID;
  } //end of setTableID method
  
  /**
   * Returns the ID of the field for this Search Criteria.
   * 
   * @return The ID of the field for this Search Criteria.
   */
  public String getFieldID()
  {
    return this.fieldID;
  } //end of getFieldID method
  
  /**
   * Sets the ID of the field for this Search Criteria.
   * 
   * @param fieldID The ID of the field for 
   * this Search Criteria.
   */
  public void setFieldID(String fieldID)
  {
    this.fieldID = fieldID;
  } //end of setFieldID method
  
  /**
   * Returns the ID of the condition for this Search Criteria.
   * 
   * @return The ID of the condition for this Search Criteria.
   */
  public String getConditionID()
  {
    return this.conditionID;
  } //end of getConditionID method
  
  /**
   * Sets the ID of the condition for this Search Criteria.
   * 
   * @param conditionID The ID of the condition for 
   * this Search Criteria.
   */
  public void setConditionID(String conditionID)
  {
    this.conditionID = conditionID;
  } //end of setConditionID method
  
  /**
   * Returns the ID of the group for this Search Criteria.
   * 
   * @return The ID of the group for this Search Criteria.
   */
  public String getGroupID()
  {
    return this.groupID;
  } //end of getGroupID method
  
  /**
   * Sets the ID of the group for this Search Criteria.
   * 
   * @param fieldID The ID of the group for 
   * this Search Criteria.
   */
  public void setGroupID(String groupID)
  {
    this.groupID = groupID;
  } //end of setGroupID method
  
  /**
   * Returns the value for this Search Criteria.
   * 
   * @return The value for this Search Criteria.
   */
  public String getValue()
  {
    return this.value;
  } //end of getValue method
  
  /**
   * Sets the value for this Search Criteria.
   * 
   * @param fieldID The value for this Search Criteria.
   */
  public void setValue(String value)
  {
    this.value = value;
  } //end of setValue method
  
  /**
   * Returns the expression type for this Search Criteria.
   * 
   * @return The expression type for this Search Criteria.
   */
  public String getExpressionType()
  {
    return this.expressionType;
  } //end of getExpressionType method
  
  /**
   * Sets the expression type for this Search Criteria.
   * 
   * @param fieldID The expression type for 
   * this Search Criteria.
   */
  public void setExpressionType(String expressionType)
  {
    this.expressionType = expressionType;
  } //end of setExpressionType method
  
  public String toString()
  {
    StringBuffer string = new StringBuffer();
    string.append("And/Or: " + this.expressionType);
    string.append(", tableID: " + this.tableID);
    string.append(", fieldID: " + this.fieldID);
    string.append(", conditionID: " + this.conditionID);
    string.append(", criteria: " + this.value);
    string.append(", groupID: " + this.groupID);
    string.append(", searchID: " + this.searchID);
    string.append(", searchCriteriaID: " + this.searchCriteriaID);
    return string.toString();
  }
  
} //end of SearchCriteriaVO class