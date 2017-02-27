/*
 * $RCSfile: ContactListLocal.java,v $    $Revision: 1.2 $  $Date: 2005/06/24 15:39:17 $ - $Author: mking_cv $
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


package com.centraview.contact.contactlist;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AddressList;
import com.centraview.common.EntityList;
import com.centraview.common.GroupList;
import com.centraview.common.IndividualList;
import com.centraview.common.MOCList;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface ContactListLocal extends EJBLocalObject
{
  public EntityList getAllEntityList(int userId, HashMap preference);
  public IndividualList getAllIndividualList(int userId, HashMap preference);
  public GroupList getAllGroupList(int userId, HashMap preference);
  public AddressList getAddressList(int userId, int contactId, int contactType);
  public MOCList getMOCList(int userId, int contactId, int contactType);
  public Vector getDBList(int userId);
  public IndividualList getAllIndividualAndEntityEmailList(int individualId, HashMap preference);
  /**
   * @author Kevin McAllister <kevin@centraview.com>Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  /**
   * Calls the EJB/database layer to get a list of Individuals and returns a
   * Collection of SQL results to the EJB layer. The Action class which calls
   * this method should take the Collection returned and pass it to the Calling
   * method in order to create a List object for display to the end user.
   * 
   * @param individualID int representation of the IndividualID of the currently
   *          logged in user. Used for record permissions.
   * @param pagingMap HashMap containing all "paging" parameters. The required
   *          values are:
   *          <ul>
   *          <li>recordsPerPage - (Integer) the number of records to show on
   *          each page.</li>
   *          <li>currentPage - (Integer) the page number that you would like
   *          to view.</li>
   *          <li>sortColumn - (String) the name of the column on which to sort
   *          results.</li>
   *          </ul>
   *  
   */
  public Collection getAccessIndividualList(int individualId, HashMap preference);
  public ValueListVO getEntityValueList(int individualId, ValueListParameters parameters);
  public ValueListVO getIndividualValueList(int individualID, ValueListParameters parameters);
  public ValueListVO getGroupValueList(int individualID, ValueListParameters parameters);
  public ValueListVO getMOCValueList(int individualId, ValueListParameters parameters);
  public ValueListVO getAddressValueList(int individualId, ValueListParameters parameters);
  public ValueListVO getEmailLookupValueList(int individualId, ValueListParameters parameters);
  public ValueListVO getAddressLookupValueList(int individualId, ValueListParameters parameters);  
}