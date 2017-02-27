/*
 * $RCSfile: SearchVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:51 $ - $Author: mking_cv $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.centraview.contact.individual.IndividualVO;

/**
 * This class holds the values for the saved
 * searches.
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public class SearchVO implements Serializable
{
  /** The Search ID of this Search Object. */
  private int searchID;

  /** The Module ID where this search is saved. */
  private int moduleID;

  /** The Individual ID of the owner of this search. */
  private int ownerID;

  /** The IndividualVO of the owner of this search. */
  private IndividualVO owner;

  /** The Individual ID of the creator of this search. */
  private int createdByID;

  /** The IndividualVO of the creator of this search. */
  private IndividualVO createdBy;

  /** The Individual ID of the modifier of this search. */
  private int modifiedByID;

  /** The IndividualVO of the modifier of this search. */
  private IndividualVO modifiedBy;

  /** The creation date of this search. */
  private Date creationDate;

  /** The modification date of this search. */
  private Date modifiedDate;

  /** The name of this Search. */
  private String name;

  /** A Collection of Search Criteria related to this search. */
  private Collection searchCriteria;

  /** The Begins With Condition String. */
  public static final String BEGINS_WITH_STRING = "Begins With";

  /** The Ends With Condition String. */
  public static final String ENDS_WITH_STRING = "Ends With";

  /** The Contains Condition String. */
  public static final String CONTAINS_STRING = "Contains";

  /** The Equals Condition String. */
  public static final String EQUALS_STRING = "Equals";

  /** The Greater Than Condition String. */
  public static final String GREATER_THAN_STRING = "Greater Than";

  /** The Less Than Condition String. */
  public static final String LESS_THAN_STRING = "Less Than";

  /** The Has Than Condition String. */
  public static final String HAS_STRING = "Has";


  /** The Begins With Condition Integer. */
  public static final Integer BEGINS_WITH_INTEGER = new Integer(1);

  /** The Contains Condition Integer. */
  public static final Integer CONTAINS_INTEGER = new Integer(2);

  /** The Ends With Condition Integer. */
  public static final Integer ENDS_WITH_INTEGER = new Integer(3);

  /** The Equals Condition Integer. */
  public static final Integer EQUALS_INTEGER = new Integer(4);

  /** The Greater Than Condition Integer. */
  public static final Integer GREATER_THAN_INTEGER = new Integer(5);

  /** The Has Than Condition Integer. */
  public static final Integer HAS_INTEGER = new Integer(6);

  /** The Less Than Condition Integer. */
  public static final Integer LESS_THAN_INTEGER = new Integer(7);

  /** The TableID for CustomFields in the searchtable */
  public static final String CUSTOM_FIELD_TABLEID = "5";


  /** Default Constructor. */
  public SearchVO()
  {
    searchCriteria = new ArrayList();
  } //end of SearchVO constructor

  /**
   * Returns the ID of the Search.
   *
   * @return The ID of the Search.
   */
  public int getSearchID()
  {
    return this.searchID;
  } //end of getSearchID method

  /**
   * Sets the ID of the Search.
   *
   * @param searchID The ID of the Search.
   */
  public void setSearchID(int searchID)
  {
    this.searchID = searchID;
  } //end of setSearchID method

  /**
   * Returns the ID of the Module linked to this Search.
   *
   * @return The ID of the Module linked to this Search.
   */
  public int getModuleID()
  {
    return this.moduleID;
  } //end of getModuleID method

  /**
   * Sets the ID of the Module linked to this Search.
   *
   * @param moduleID The ID of the
   * Module linked to this Search.
   */
  public void setModuleID(int moduleID)
  {
    this.moduleID = moduleID;
  } //end of setModuleID method

  /**
   * Returns the Owner ID of this Search.
   *
   * @return The Owner ID of this Search.
   */
  public int getOwnerID()
  {
    return this.ownerID;
  } //end of getOwnerID method

  /**
   * Sets the Owner ID of this Search.
   *
   * @param ownerID The Owner ID of this Search.
   */
  public void setOwnerID(int ownerID)
  {
    this.ownerID = ownerID;
  } //end of setOwnerID method

  /**
   * Returns the IndividualVO of the Owner of this Search.
   *
   * @return The IndividualVO of the Owner of this Search.
   */
  public IndividualVO getOwner()
  {
    return this.owner;
  } //end of getOwner method

  /**
   * Sets the IndividualVO of the Owner of this Search.
   *
   * @param owner The IndividualVO of the
   * Owner of this Search.
   */
  public void setOwner(IndividualVO owner)
  {
    this.owner = owner;
  } //end of setOwner method

  /**
   * Returns the Creator ID of this Search.
   *
   * @return The Creator ID of this Search.
   */
  public int getCreatedByID()
  {
    return this.createdByID;
  } //end of getCreatedByID method

  /**
   * Sets the Creator ID of this Search.
   *
   * @param createdByID The Creator ID of this Search.
   */
  public void setCreatedByID(int createdByID)
  {
    this.createdByID = createdByID;
  } //end of setCreatedByID method

  /**
   * Returns the IndividualVO of the Creator of this Search.
   *
   * @return The IndividualVO of the Creator of this Search.
   */
  public IndividualVO getCreatedBy()
  {
    return this.createdBy;
  } //end of getCreatedBy method

  /**
   * Sets the IndividualVO of the Creator of this Search.
   *
   * @param createdBy The IndividualVO of the
   * Creator of this Search.
   */
  public void setCreatedBy(IndividualVO createdBy)
  {
    this.createdBy = createdBy;
  } //end of setCreatedBy method

  /**
   * Returns the Modifier ID of this Search.
   *
   * @return The Modifier ID of this Search.
   */
  public int getModifiedByID()
  {
    return this.modifiedByID;
  } //end of getModifiedByID method

  /**
   * Sets the Modifier ID of this Search.
   *
   * @param modifiedByID The Modifier ID of this Search.
   */
  public void setModifiedByID(int modifiedByID)
  {
    this.modifiedByID = modifiedByID;
  } //end of setModifiedByID method

  /**
   * Returns the IndividualVO of the Modifier of this Search.
   *
   * @return The IndividualVO of the Modifier of this Search.
   */
  public IndividualVO getModifiedBy()
  {
    return this.modifiedBy;
  } //end of getModifiedBy method

  /**
   * Sets the IndividualVO of the Modifier of this Search.
   *
   * @param modifiedBy The IndividualVO of the
   * Modifier of this Search.
   */
  public void setModifiedBy(IndividualVO modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  } //end of setModifiedBy method

  /**
   * Returns the Creation Date of this Search.
   *
   * @return The Creation Date of this Search.
   */
  public Date getCreationDate()
  {
    return this.creationDate;
  } //end of getCreationDate method

  /**
   * Sets the Creation Date of this Search.
   *
   * @param creationDate The Creation Date of this Search.
   */
  public void setCreationDate(Date creationDate)
  {
    this.creationDate = creationDate;
  } //end of setCreationDate method

  /**
   * Returns the Modified Date of this Search.
   *
   * @return The Modified Date of this Search.
   */
  public Date getModifiedDate()
  {
    return this.modifiedDate;
  } //end of getModifiedDate method

  /**
   * Sets the Modified Date of this Search.
   *
   * @param modifiedDate The Modified Date of this Search.
   */
  public void setModifiedDate(Date modifiedDate)
  {
    this.modifiedDate = modifiedDate;
  } //end of setModifiedDate method

  /**
   * Returns the Name of this Search.
   *
   * @return The Name of this Search.
   */
  public String getName()
  {
    return this.name;
  } //end of getName method

  /**
   * Sets the Name of this Search.
   *
   * @param name The Name of this Search.
   */
  public void setName(String name)
  {
    this.name = name;
  } //end of setName method

  /**
   * Returns the Collection of Search Criteria VOs
   * for this Search.
   *
   * @return The Collection of Search Criteria VOs
   * for this Search.
   *
   * @see com.centraview.advancedsearch.SearchCriteriaVO
   */
  public Collection getSearchCriteria()
  {
    if (this.searchCriteria == null)
    {
      this.searchCriteria = new ArrayList();
    } //end of if statement (this.searchCriteria == null)
    return this.searchCriteria;
  } //end of getSearchCriteria method

  /**
   * Sets the Collection of Search Criteria VOs
   * for this Search.
   *
   * @param searchCriteria The Collection of Search
   * Criteria VOs for this Search.
   *
   * @see com.centraview.advancedsearch.SearchCriteriaVO
   */
  public void setSearchCriteria(Collection searchCriteria)
  {
    this.searchCriteria = searchCriteria;
  } //end of getSearchCriteria method

  /**
   * Adds a new SearchCriteriaVO to the Collection
   * of Search Criteria VOs for this Search.
   *
   * @param searchCriteriaVO The new SearchCriteriaVO
   * to be added to the Collection.
   *
   * @see com.centraview.advancedsearch.SearchCriteriaVO
   */
  public void addSearchCriteria(SearchCriteriaVO searchCriteriaVO)
  {
    if (this.searchCriteria == null)
    {
      this.searchCriteria = new ArrayList();
    } //end of if statement (this.searchCriteria == null)
    this.searchCriteria.add(searchCriteriaVO);
  } //end of addSearchCriteria method

  /**
   * Returns the number of different groups the
   * SearchCriteriaVOs are related to. If there
   * are no SearchCriteriaVOs, 0 will be returned.
   *
   * @return The number of different groups the
   * SearchCriteriaVOs are related to.
   */
  public int getNumberOfGroups()
  {
    int groups = 0;
    if (this.searchCriteria != null)
    {
      Iterator it = this.searchCriteria.iterator();
      HashMap tempHashMap = new HashMap();
      while (it.hasNext())
      {
        SearchCriteriaVO searchCriteriaVO = (SearchCriteriaVO) it.next();
        tempHashMap.put(new Integer(Integer.parseInt(searchCriteriaVO.getGroupID())), "");
      } //end of while loop (iterator.hasNext())
      groups = tempHashMap.size();
    } //end of if statement (this.searchCriteria != null)
    return groups;
  } //end of getNumberOfGroups method

  /**
   * Returns the number of
   * SearchCriteriaVOs related to this search. If there
   * are no SearchCriteriaVOs, 0 will be returned.
   *
   * @return The number of SearchCriteriaVOs
   * related to this search.
   */
  public int getNumberOfCriteria()
  {
    return this.searchCriteria.size();
  } //end of getNumberOfCriteria method

  /**
   * Returns an Integer Array of unique Group IDs.
   * Each GroupID is only listed once in the
   * Integer Array.
   *
   * @return An Integer Array of unique Group IDs.
   */
  public Integer[] getGroupIDs()
  {
    Collection groupIDs = new ArrayList();
    if (this.searchCriteria != null)
    {
      Iterator it = this.searchCriteria.iterator();
      while (it.hasNext())
      {
        SearchCriteriaVO searchCriteriaVO = (SearchCriteriaVO) it.next();
        if (!groupIDs.contains(new Integer(searchCriteriaVO.getGroupID())))
        {
          groupIDs.add(new Integer(searchCriteriaVO.getGroupID()));
        } //end of if statement (!groupIDs.contains(...
      } //end of while loop (iterator.hasNext())
    } //end of if statement (this.searchCriteria != null)
    return (Integer[]) groupIDs.toArray(new Integer[groupIDs.size()]);
  } //end of getGroupIDs method

  /**
   * Returns a HashMap with the available condition
   * options for the search criteria. The Key is
   * an Integer and the Value is the Display String.
   *
   * @return A HashMap of available condition
   * options for the search criteria.
   */
  public static final HashMap getConditionOptions()
  {
    HashMap conditions = new HashMap();
    conditions.put(BEGINS_WITH_INTEGER , BEGINS_WITH_STRING);
    conditions.put(CONTAINS_INTEGER , CONTAINS_STRING);
    conditions.put(ENDS_WITH_INTEGER , ENDS_WITH_STRING);
    conditions.put(EQUALS_INTEGER , EQUALS_STRING);
    conditions.put(GREATER_THAN_INTEGER, GREATER_THAN_STRING);
    conditions.put(HAS_INTEGER , HAS_STRING);
    conditions.put(LESS_THAN_INTEGER , LESS_THAN_STRING);
    return conditions;
  } //end of getConditionOptions method

  public String toString()
  {
    String string = "SearchVO: {SearchID: "+searchID+", moduleID: "+moduleID+", "+
                    "ownerID: "+ownerID+ ", name: "+name+", searchCritieria: "+searchCriteria+"}";
    return string.toString();
  }
} //end of SearchVO class;