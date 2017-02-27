/*
 * $RCSfile: SearchCriteriaVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:46 $ - $Author: mking_cv $
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
import java.util.HashMap;

/**
 * The SearchCriteriaVO contains the field mappings for the types of records that
 * can be merged.  It also will allow for the data transfer of the specific search criteria selected
 * by the user, to the MergeEJB, so the Search method can be invoked.
 * It will consist of Search Type, Search Source, Threshhold, and a collection of field criteria.
 * 
 * The SearchCriteriaVO is being used for two purposes:
 * 1. to provide to the web tier a list of possible fields, and search types.
 * 2. to allow the web tier to communicate the user selected search criteria to the EJB layer.
 * 
 * Possibly these functions should be seperated as they could lead to unexpected complexity and coupling,
 * and uneccessary resource usage in passing extra values around over RMI 
 * However because the IDs passed int the criteria are tied directly to the arraylists sent out for display to the
 * web tier, I am currently keeping them together in the same VO.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class SearchCriteriaVO implements Serializable
{
  /**
   * The fields that are eligible for search on an entity merge.
   */
  private ArrayList entityFieldList = null;
  /**
   * The fields that are eligible for search on an individual merge.
   */
  private ArrayList individualFieldList = null;
  /**
   * type = 1 is entity, type = 2 is individual.
   */
  private int type = 0;
  /**
   * the searchDomain is the set of contacts the search should be done over.
   * 0 is all contacts of type this.type, and number > 0 corresponds to the
   * appropriate marketing listid.  If in the future we add saved searches in here
   * this will need to change.
   */
  private int searchDomain = 0;
  /**
   * The threshhold that must be exceeded by the sum of scores of successful search criteria
   * to consider two records to be identical.
   */
  private int threshhold = 0;
  /**
   * fieldCriteria will be an ArrayList that consists of HashMap objects.
   * The HashMaps should contain the following (String) keys
   * "Field"->new Integer(index from fieldList Array)
   * "SearchType"->new Integer(index from searchType Array) 
   * "MatchValue"->new Integer(value assigned by user)
   */
  private ArrayList fieldCriteria = null;

  // The following Static Strings are used to key the HashMaps stored in the fieldCriteria ArrayList
  /**
   * FIELD_KEY is the hashmap key for the FieldList Index, 
   * for the HashMap contained in the fieldCriteria ArrayList. 
   */
  public static String FIELD_KEY = "Field";
  /**
   * SEARCHTYPE_KEY is the hashmap key for the SearchType Index, 
   * for the HashMap contained in the fieldCriteria ArrayList. 
   */
  public static String SEARCHTYPE_KEY = "SearchType";
  /**
    * MATCHVALUE_KEY is the hashmap key for the Score value set by the user for a particular criterion match
    * it is used in the HashMap contained in the fieldCriteria ArrayList 
    */
public static String MATCHVALUE_KEY = "MatchValue";
  
  /**
   * searchType will contain the actual searchtypes that can be used.
   * this is a static (class) field.
   */
  private static ArrayList searchType = null;
  
  // Static Initializer to setup the searchTypes
  static {
    populateSearchType();
  }
  
  /**
   * populateSearchType() statically defines the valid
   * searchTypes.  To add a searchType you will have to add one
   * here to get it to show on the View layer, then write the code in the MergeEJB
   * also modify existing code in the MergeEJB to call your new code.
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * 
   */
  private static void populateSearchType()
  {
    searchType = new ArrayList();
    searchType.add("Exact Match (case-sensitive)");   // 0
    searchType.add("Exact Match (case-insensitive)"); // 1
    // TODO implement these search types.
    //searchType.add("Soundex");                              // 2
    //searchType.add("Metaphone");                           // 3
  }
  
  /**
   * The constructor simply pre-populates the field lists with the default field information
   * for each type.
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public SearchCriteriaVO()
  {
    super();
    // Set up the entityFieldList
    this.populateEntityList();
    // Set up the individualFieldList
    this.populateIndividualList();
  }

  /**
   * populateEntityList will simply take these hardcoded field values and
   * provide them as the list that is eligible for search comparisons on the Merge
   * functionallity for Entities.  This list was chosen rather than using the database because there
   * were fields that didn't make sense, and rather than including all fields and then
   * writing some convoluted logic to remove the ones we didn't want, I found it most
   * straightforward to simply hardcode the values I wanted.
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * 
   */
  private void populateEntityList()
  {
    this.entityFieldList = new ArrayList();
    this.entityFieldList.add(new MergeField("Entity Name", "SELECT name, entityid FROM entity WHERE entityid IN "));
    this.entityFieldList.add(new MergeField("Primary Contact Name", "SELECT CONCAT(i.firstname,i.lastname) AS name, e.entityid FROM individual i, entity e WHERE i.entity = e.entityid AND primarycontact = 'YES' AND e.entityid IN "));
    this.entityFieldList.add(new MergeField("ID2","SELECT externalid, entityid FROM entity WHERE entityid IN "));
    this.entityFieldList.add(new MergeField("Source", "SELECT source, entityid FROM entity WHERE entityid IN "));
    this.entityFieldList.add(new MergeField("Account Manager","SELECT accountmanagerid, entityid FROM entity WHERE entityid IN "));
    this.entityFieldList.add(new MergeField("Account Team","SELECT accountteamid, entityid FROM entity WHERE entityid IN "));
    this.entityFieldList.add(new MergeField("Street1", "SELECT a.street1, e.entityid FROM address AS a, addressrelate AS ar, entity AS e WHERE a.addressid = ar.address AND ar.contacttype = 1 AND ar.contact = e.entityid and e.entityid IN "));
    this.entityFieldList.add(new MergeField("Street2", "SELECT a.street2, e.entityid FROM address AS a, addressrelate AS ar, entity AS e WHERE a.addressid = ar.address AND ar.contacttype = 1 AND ar.contact = e.entityid and e.entityid IN "));
    this.entityFieldList.add(new MergeField("City", "SELECT a.city, e.entityid FROM address AS a, addressrelate AS ar, entity AS e WHERE a.addressid = ar.address AND ar.contacttype = 1 AND ar.contact = e.entityid and e.entityid IN "));
    this.entityFieldList.add(new MergeField("State", "SELECT a.state, e.entityid FROM address AS a, addressrelate AS ar, entity AS e WHERE a.addressid = ar.address AND ar.contacttype = 1 AND ar.contact = e.entityid and e.entityid IN "));
    this.entityFieldList.add(new MergeField("Zip", "SELECT a.zip, e.entityid FROM address AS a, addressrelate AS ar, entity AS e WHERE a.addressid = ar.address AND ar.contacttype = 1 AND ar.contact = e.entityid and e.entityid IN "));
    this.entityFieldList.add(new MergeField("Website", "SELECT a.website, e.entityid FROM address AS a, addressrelate AS ar, entity AS e WHERE a.addressid = ar.address AND ar.contacttype = 1 AND ar.contact = e.entityid and e.entityid IN "));
    this.entityFieldList.add(new MergeField("Contact Method", "SELECT moc.content, e.entityid FROM methodofcontact AS moc, mocrelate AS mr, entity AS e WHERE moc.mocid = mr.mocid AND mr.contacttype = 1 AND mr.contactid = e.entityid and e.entityid IN "));
    // TODO Do CustomField stuff
  }
  
  /**
   * populateIndividualList will simply take these hardcoded field values and
   * provide them as the list that is eligible for search comparisons on the Merge
   * functionallity for Individuals.  This list was chosen rather than using the database because there
   * were fields that didn't make sense, and rather than including all fields and then
   * writing some convoluted logic to remove the ones we didn't want, I found it most
   * straightforward to simply hardcode the values I wanted.
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * 
   */
  private void populateIndividualList()
  {
    // TODO make this work with new merge algorithm
    this.individualFieldList = new ArrayList();
    this.individualFieldList.add(new MergeField("First Name","SELECT firstname, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("Middle Initial","SELECT middleinitial, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("Last Name","SELECT lastname, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("ID2","SELECT externalid, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("Title","SELECT title, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("Entity","SELECT entity, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("Source","SELECT source, individualid FROM individual WHERE individualid IN "));
    this.individualFieldList.add(new MergeField("Street1", "SELECT a.street1, i.individualid FROM address AS a, addressrelate AS ar, individual AS i WHERE a.addressid = ar.address AND ar.contacttype = 2 AND ar.contact = i.individualid and i.individualid IN "));
    this.individualFieldList.add(new MergeField("Street2", "SELECT a.street2, i.individualid FROM address AS a, addressrelate AS ar, individual AS i WHERE a.addressid = ar.address AND ar.contacttype = 2 AND ar.contact = i.individualid and i.individualid IN "));
    this.individualFieldList.add(new MergeField("City", "SELECT a.city, i.individualid FROM address AS a, addressrelate AS ar, individual AS i WHERE a.addressid = ar.address AND ar.contacttype = 2 AND ar.contact = i.individualid and i.individualid IN "));
    this.individualFieldList.add(new MergeField("State", "SELECT a.state, i.individualid FROM address AS a, addressrelate AS ar, individual AS i WHERE a.addressid = ar.address AND ar.contacttype = 2 AND ar.contact = i.individualid and i.individualid IN "));
    this.individualFieldList.add(new MergeField("Zip", "SELECT a.zip, i.individualid FROM address AS a, addressrelate AS ar, individual AS i WHERE a.addressid = ar.address AND ar.contacttype = 2 AND ar.contact = i.individualid and i.individualid IN "));
    this.individualFieldList.add(new MergeField("Website", "SELECT a.website, i.individualid FROM address AS a, addressrelate AS ar, individual AS i WHERE a.addressid = ar.address AND ar.contacttype = 2 AND ar.contact = i.individualid and i.individualid IN "));
    this.individualFieldList.add(new MergeField("Contact Method", "SELECT moc.content, i.individualid FROM methodofcontact AS moc, mocrelate AS mr, individual AS i WHERE moc.mocid = mr.mocid AND mr.contacttype = 2 AND mr.contactid = i.individualid and i.individualid IN "));
    // TODO Do CustomField stuff.
  }
  
  /**
   * Gets the SearchType array List, primary for display purposes.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return the SearchType which is a class ArrayList of Strings 
   */
  public static ArrayList getSearchType()
  {
    return searchType;
  }

  /**
   * Returns the entityFieldList primarily for display purposes.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return ArrayList of Strings
   */
  public ArrayList getEntityFieldList()
  {
    return entityFieldList;
  }

  /**
   * Returns the FieldCriteria which will be built by the user.  This will be used
   * to do the actual searches in the EJB
   * @author Kevin McAllister <kevin@centraview.com>
   * @return and ArrayList containing the fieldCriteria.  Or an empty ArrayList
   */
  public ArrayList getFieldCriteria()
  {
    if (fieldCriteria == null)
    {
      return new ArrayList();
    }
    return fieldCriteria;
  }

  /**
   * getIndividualFieldList returns the fieldlist for an Individual primarily for display purposes.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return arraylist of Strings
   */
  public ArrayList getIndividualFieldList()
  {
    return individualFieldList;
  }

  /**
   * gets the Current Search domain which is an integer corresponding to
   * a particular marketinglist id or in the case of zero the union of all marketinglists
   * becomes the domain.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return int
   */
  public int getSearchDomain()
  {
    return searchDomain;
  }

  /**
   * gets the Merge type.  The type is 1 for entity, 2 for individual
   * @author Kevin McAllister <kevin@centraview.com>
   * @return int
   */
  public int getType()
  {
    return type;
  }

  /**
   * Sets the search domain to the passed in parameter.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param i an int corresponding to a marketing list id or 0 which means the union of all marketing lists.
   */
  public void setSearchDomain(int i)
  {
    searchDomain = i;
  }

  /**
   * set the type of merge Entity = 1 or Individual = 2 
   * @author Kevin McAllister <kevin@centraview.com>
   * @param i an int corresponding to merge type
   */
  public void setType(int i)
  {
    type = i;
  }
  
  /**
   * Creates a new Hashmap populates it with the three parameters and adds it
   * to the fieldCriteria ArrayList.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param fieldIndex an int index of the field that needs to match.
   * @param searchType int index of requested searchtype to be used to determine a match.
   * @param score int value to be assigned to this critereon, if a successfull match is found for this criterion
   * a sum will be calculated of the scores of each matching criterion, if the results of the sum exceed the 
   * threshhold, it is considered to be a match.  (The same record.) 
   */
  public void addFieldCriterion(int fieldIndex, int searchType, int score)
  {
   if (fieldCriteria == null) 
   {
     fieldCriteria = new ArrayList(); 
   }
   HashMap criterion = new HashMap();
   criterion.put(FIELD_KEY, new Integer(fieldIndex));
   criterion.put(SEARCHTYPE_KEY, new Integer(searchType));
   criterion.put(MATCHVALUE_KEY, new Integer(score));
   fieldCriteria.add(criterion);
  }
  /**
   * Returns the value of the threshhold selected by the user.
   * We probably should enforce some minimum value to ease use of the system
   * @author Kevin McAllister <kevin@centraview.com>
   * @return the threshhold int. (default is 0)
   */
  public int getThreshhold()
  {
    return threshhold;
  }

  /**
   * sets the value of the threshhold.  Should be set by the user.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param i the threshhold set by the user.
   */
  public void setThreshhold(int i)
  {
    threshhold = i;
  }

}
