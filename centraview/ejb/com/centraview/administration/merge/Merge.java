/*
 * $RCSfile: Merge.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:45 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

/**
 * EJB Spec mandated Remote Interface for the MergeEJB SessionBean
 * The MergeEJB SessionBean will do all the heavy lifting for
 * the Merge / Purge functionallity.  The methods in here will
 * implement the business logic that populates the SearchCriteriaVO
 * with the right fieldlists.  That in turn, parses and does a search
 * based on the contents of a SearchCriteriaVO.  And returns a MergeSearchResultVO.
 *
 * Finally based on further selections by the user, Entity or Individual Records
 * will be updated, many many related records will be re-associated with the newly updated
 * (merged) record.  And purged records will be deleted through their
 * standard delete use methods.
 *
 * The phonetic algortihm searches (soundex, metaphone) will rely on the Jakarta commons
 * codec library.
 * <http://jakarta.apache.org/commons/codec/>
 * <http://jakarta.apache.org/commons/codec/apidocs/>

 * @author Kevin McAllister <kevin@centraview.com>
 *
 */
public interface Merge extends EJBObject
{
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
  /**
   * The SearchCriteriaVO is built initially in JBoss world, because we will need to stick
   * customfields on the fieldlists to provide more robust field criteria.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return
   */
  public SearchCriteriaVO getSearchCriteriaVO() throws RemoteException;
  /**
   * Where the rubber meets the road in terms of actually finding "matching"
   * records.  This method will interpret the searchCriteria, perform the checks
   * for each criterion and build and return the results.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param criteria a populated SearchCriteriaVO.
   * @return a MergeSearchResultVO with the matches, if there were no matches the VO.size() will be 0.
   */
  public MergeSearchResultVO performSearch(SearchCriteriaVO criteria) throws RemoteException;
  /**
   * This method takes an ArrayList of Integers representing now
   * deleted Entities and an int (entityId) of the new merged record.  All newly orphaned
   * records will be adopted by the passed in entityId.  This is done using a series of database
   * queries.  It returns boolean if it thinks that it successfully rolled everything up.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param deletedEntities an ArrayList of Integers of the old Entities.
   * @param entityId an int representing the adopting parent record
   * @return true if successful, false otherwise.
   */
  public boolean rollUpEntityOrphans(ArrayList deletedEntities, int entityId) throws RemoteException;

  /**
   * This method takes an ArrayList of Integers representing now
   * deleted Individuals and an int (individualID) of the new merged record.  All newly orphaned
   * records will be adopted by the passed in individualID.  This is done using a series of database
   * queries.  It returns boolean if it thinks that it successfully rolled everything up.
   *
   * @author Naresh Patel <npatel@centraview.com>
   * @param deletedIndividuals an ArrayList of Integers of the old Individuals.
   * @param individualID an int representing the adopting parent record
   * @return true if successful, false otherwise.
   */
  public boolean rollUpIndividualOrphans(ArrayList deletedIndividuals, int individualID) throws RemoteException;
}
